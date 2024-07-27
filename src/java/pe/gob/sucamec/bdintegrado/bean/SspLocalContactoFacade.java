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
import pe.gob.sucamec.bdintegrado.data.SspLocalContacto;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspLocalContactoFacade extends AbstractFacade<SspLocalContacto> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspLocalContactoFacade() {
        super(SspLocalContacto.class);
    }
    public List<SspLocalContacto> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspLocalContacto s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
}
