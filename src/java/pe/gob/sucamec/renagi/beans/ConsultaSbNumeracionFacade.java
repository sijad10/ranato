/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.renagi.data.ConsultaSbNumeracion;

/**
 *
 * @author rarevalo
 */
@Stateless
public class ConsultaSbNumeracionFacade extends AbstractFacade<ConsultaSbNumeracion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaSbNumeracionFacade() {
        super(ConsultaSbNumeracion.class);
    }

    public List<ConsultaSbNumeracion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select n from SbNumeracion n where n.tipo.nombre like :nombre");
        q.setParameter("nombre", "%" + s + "%");
        return q.getResultList();
    }

    public long selectNumero(String codProg) {
        long r = 0;
        //EntityManager em = getEntityManager();
        javax.persistence.Query q = getEntityManager().createQuery("select n from ConsultaSbNumeracion n where n.tipo.codProg = :codProg");
        q.setParameter("codProg", codProg);
        ConsultaSbNumeracion num = (ConsultaSbNumeracion) q.getResultList().get(0);
        r = num.getValor() + 1;
        num.setValor(r);
        em.merge(num);
        return r;
    }
    
    public ConsultaSbNumeracion selectNumeroLicencia(String codProg) {
        long r = 0;
        javax.persistence.Query q = getEntityManager().createQuery("select n from ConsultaSbNumeracion n where n.tipo.codProg = :codProg");
        q.setParameter("codProg", codProg);
        ConsultaSbNumeracion num = (ConsultaSbNumeracion) q.getResultList().get(0);
        /*r = num.getValor() + 1;
        num.setValor(r);
        em.merge(num);
        return r;
        */
        return num;
    }

    
}
