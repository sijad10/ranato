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
import pe.gob.sucamec.bdintegrado.data.EppPuertoAduanero;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppPuertoAduaneroFacade extends AbstractFacade<EppPuertoAduanero> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppPuertoAduaneroFacade() {
        super(EppPuertoAduanero.class);
    }
    public List<EppPuertoAduanero> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select p from EppPuertoAduanero p where trim(p.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
}
