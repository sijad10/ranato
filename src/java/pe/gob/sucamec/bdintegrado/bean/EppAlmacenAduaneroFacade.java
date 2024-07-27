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
import pe.gob.sucamec.bdintegrado.data.EppAlmacenAduanero;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppAlmacenAduaneroFacade extends AbstractFacade<EppAlmacenAduanero> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppAlmacenAduaneroFacade() {
        super(EppAlmacenAduanero.class);
    }
    public List<EppAlmacenAduanero> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from EppAlmacenAduanero a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
}
