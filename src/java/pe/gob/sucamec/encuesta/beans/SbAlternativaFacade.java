/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.encuesta.beans;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.bean.AbstractFacade;
import pe.gob.sucamec.encuesta.data.SbAlternativa;

/**
 *
 * @author gchavez
 */
@Stateless
public class SbAlternativaFacade extends AbstractFacade<SbAlternativa> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbAlternativaFacade() {
        super(SbAlternativa.class);
    }
    public List<SbAlternativa> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbAlternativa s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
//        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
}
