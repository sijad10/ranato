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
import pe.gob.sucamec.bdintegrado.data.SspSolicitudCeseDet;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspSolicitudCeseDetFacade extends AbstractFacade<SspSolicitudCeseDet> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspSolicitudCeseDetFacade() {
        super(SspSolicitudCeseDet.class);
    }
    public List<SspSolicitudCeseDet> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspSolicitudCeseDet s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        //q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SspSolicitudCeseDet> buscaDetallePorSolicitud(Long id) {
        Query q = em.createQuery("select s from SspSolicitudCeseDet s where s.solicitudId.id like :id");
        q.setParameter("id", id);        
        return q.getResultList();
    }
}
