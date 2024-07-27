/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspLugarServicio;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspLugarServicioFacade extends AbstractFacade<SspLugarServicio> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspLugarServicioFacade() {
        super(SspLugarServicio.class);
    }
    public List<SspLugarServicio> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspLugarServicio s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
}
