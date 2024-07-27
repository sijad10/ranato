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
import pe.gob.sucamec.bdintegrado.data.AmaTipoLicencia;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaTipoLicenciaFacade extends AbstractFacade<AmaTipoLicencia> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaTipoLicenciaFacade() {
        super(AmaTipoLicencia.class);
    }
    public List<AmaTipoLicencia> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaTipoLicencia a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<AmaTipoLicencia> selectLicenciasUso(String nroDoc) {
        Query q = em.createQuery("select tl from AmaTipoLicencia tl where tl.activo = 1 and tl.licenciaId.activo = 1 and (tl.licenciaId.personaLicenciaId.numDoc = :doc or tl.licenciaId.personaLicenciaId.ruc = :doc) order by tl.licenciaId.fechaEmision desc ");
        q.setParameter("doc", nroDoc);
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return null;
    }
}
