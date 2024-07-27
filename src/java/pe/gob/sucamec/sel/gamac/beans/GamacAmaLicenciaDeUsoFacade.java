/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.sel.gamac.data.GamacAmaLicenciaDeUso;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.*;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacAmaLicenciaDeUsoFacade extends AbstractFacade<GamacAmaLicenciaDeUso> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacAmaLicenciaDeUsoFacade() {
        super(GamacAmaLicenciaDeUso.class);
    }
    
    public List<GamacAmaLicenciaDeUso> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from GamacAmaLicenciaDeUso a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<GamacAmaLicenciaDeUso> selectPersonaLicencia(String s) {
        if (s == null) {
            s = "";
        }
        
        String sql = "select a from GamacAmaLicenciaDeUso a where"
                + " trim(a.personaLicenciaId.numDoc) = :numDoc and a.activo=1 "
                //+ " and func('TRUNC', a.fechaVencimietnto) >= func('TRUNC', :hoy)"
                + " and a.estadoId.codProg IN ('TP_EST_VIG', 'TP_EST_VEN')";
        
        Query q = em.createQuery(sql);
        q.setParameter("numDoc", s);
        //q.setParameter("hoy", new Date(), TemporalType.DATE);
        
        //q.setParameter("hoy", new Date());        
        q.setMaxResults(MAX_RES);
        //q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<GamacAmaLicenciaDeUso> listLicencia(String filtro) {
        String sql = "select"
                + " a"
                + " from GamacAmaLicenciaDeUso a"
                + " where"
                + " a.activo=1 and a.estadoId.id=121";

        if (filtro != null) {
            sql += " and trim(a.nroLicencia) like :nroLicencia";
        }
        Query q = em.createQuery(sql);

        if (filtro != null) {
            q.setParameter("nroLicencia", "" + filtro + "%");
        }
        q.setMaxResults(MAX_RES_C);
        //q.setHint("eclipselink.batch.type", "IN");        
        return q.getResultList();
    }
    
}
