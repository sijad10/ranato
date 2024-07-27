/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbNumeracion;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbNumeracionFacade extends AbstractFacade<SbNumeracion> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbNumeracionFacade() {
        super(SbNumeracion.class);
    }
    
    public Long buscarNumeracionActual(String codProg) {
        long r = 0;
        Query q = em.createQuery("select t from SbNumeracion t where t.tipo.codProg = :codProg ");
        q.setParameter("codProg", codProg.trim());  
        
        SbNumeracion num = (SbNumeracion) q.getResultList().get(0);
        r = num.getValor() + 1;
        num.setValor(r);
        em.merge(num);
        
        return r;
    }
    
    public List<pe.gob.sucamec.bdintegrado.data.SbNumeracion> selectNumero(long tipo) {
        javax.persistence.Query q = getEntityManager().createQuery(
                "select n from SbNumeracion n where n.tipo.id = :tipo");
        q.setParameter("tipo", tipo);
        return q.getResultList();
    }
    
    /**
     * con transacciones
     */
    public long selectNumeroCodProg(String codProg) {
        long r = 0;
        EntityManager em = getEntityManager();
//        EntityTransaction et = em.getTransaction();
//        et.begin();
        javax.persistence.Query q = getEntityManager().createQuery(
                "select n from SbNumeracion n where n.tipo.codProg = :codProg");
        q.setParameter("codProg", codProg);
        SbNumeracion num = (SbNumeracion) q.getResultList().get(0);
        r = num.getValor() + 1;
        num.setValor(r);
        em.merge(num);
//        et.commit();
        return r;
    }

    
    public List<pe.gob.sucamec.bdintegrado.data.SbNumeracion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select n from SbNumeracion n where n.tipo.nombre like :nombre");
        q.setParameter("nombre", "%" + s + "%");
        return q.getResultList();
    }
        
}
