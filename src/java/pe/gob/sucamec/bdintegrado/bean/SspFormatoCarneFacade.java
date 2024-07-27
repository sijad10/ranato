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
import pe.gob.sucamec.bdintegrado.data.SspFormatoCarne;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspFormatoCarneFacade extends AbstractFacade<SspFormatoCarne> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspFormatoCarneFacade() {
        super(SspFormatoCarne.class);
    }
    public List<SspFormatoCarne> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspFormatoCarne s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public SspFormatoCarne obtenerFormatoActivo() {        
        Query q = em.createQuery("select s from SspFormatoCarne s where s.activo = 1 order by s.id ");
        if(!q.getResultList().isEmpty()){
            return (SspFormatoCarne) q.getResultList().get(0);
        }
        return null;
    }
}
