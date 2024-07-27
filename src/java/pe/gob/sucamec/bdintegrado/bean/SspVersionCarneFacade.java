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
import pe.gob.sucamec.bdintegrado.data.SspVersionCarne;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspVersionCarneFacade extends AbstractFacade<SspVersionCarne> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspVersionCarneFacade() {
        super(SspVersionCarne.class);
    }
    public List<SspVersionCarne> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspVersionCarne s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    public SspVersionCarne obtenerUltimaVersionCarne() {
        Query q = em.createQuery("select s from SspVersionCarne s where s.activo = 1 order by s.id desc");
        if(!q.getResultList().isEmpty()){
            return (SspVersionCarne) q.getResultList().get(0);
        }
        return null;
    }
}
