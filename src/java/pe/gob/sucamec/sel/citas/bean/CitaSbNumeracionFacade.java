/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.citas.data.CitaSbNumeracion;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaSbNumeracionFacade extends AbstractFacade<CitaSbNumeracion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaSbNumeracionFacade() {
        super(CitaSbNumeracion.class);
    }

    public List<CitaSbNumeracion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select n from CitaSbNumeracion n where n.tipo.nombre like :nombre");
        q.setParameter("nombre", "%" + s + "%");
        return q.getResultList();
    }

    public List<CitaSbNumeracion> selectNumero(long tipo) {
        javax.persistence.Query q = getEntityManager().createQuery(
                "select n from CitaSbNumeracion n where n.tipo.id = :tipo");
        q.setParameter("tipo", tipo);
        return q.getResultList();
    }
    
    public List<CitaSbNumeracion> selectNumeroXcodProg(String codProg) {
        javax.persistence.Query q = getEntityManager().createQuery(
                "select n from CitaSbNumeracion n where n.tipo.codProg = :codProg");
        q.setParameter("codProg", codProg);
        return q.getResultList();
    }
    
    /**con transacciones */
    public long selectNumero(String codProg) {
        long r = 0;
        EntityManager em = getEntityManager();
//        EntityTransaction et = em.getTransaction();
//        et.begin();
        javax.persistence.Query q = getEntityManager().createQuery(
                "select n from CitaSbNumeracion n where n.tipo.codProg = :codProg");
        q.setParameter("codProg", codProg);
        CitaSbNumeracion num = (CitaSbNumeracion) q.getResultList().get(0);
        r = num.getValor() + 1;
        num.setValor(r);
        em.merge(num);
//        et.commit();
        return r;
    }
}
