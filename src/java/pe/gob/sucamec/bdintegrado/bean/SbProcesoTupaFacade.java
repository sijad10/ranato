/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbProcesoTupa;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbProcesoTupaFacade extends AbstractFacade<SbProcesoTupa> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbProcesoTupaFacade() {
        super(SbProcesoTupa.class);
    }
    
    public SbProcesoTupa obtenerTupaByCydocProcesoId(Integer procesoId) {
        Query q = em.createQuery("select t from SbProcesoTupa t where t.activo = 1 and t.cydocIdProceso = :cydocIdProceso");
        q.setParameter("cydocIdProceso", procesoId);
        
        if(!q.getResultList().isEmpty()){
            return (SbProcesoTupa) q.getResultList().get(0);
        }
        return null;
    }
    
    public SbProcesoTupa obtenerTupaByTupaCodProgByCydocProcesoId(String tupaCodProg, Integer procesoId) {
        Query q = em.createQuery("select t from SbProcesoTupa t where t.activo = 1 and t.cydocIdProceso = :cydocIdProceso and t.tupaId.codProg = :tupaCodProg ");
        q.setParameter("cydocIdProceso", procesoId);
        q.setParameter("tupaCodProg", tupaCodProg);
        
        if(!q.getResultList().isEmpty()){
            return (SbProcesoTupa) q.getResultList().get(0);
        }
        return null;
    }
    
    public SbProcesoTupa obtenerTupaByTupaCodProgByCydocProcesoIdByTipoOpeCodProg(String tupaCodProg, Integer procesoId, String tipoOpeIdCodProg) {
        Query q = em.createQuery("select t from SbProcesoTupa t where t.activo = 1 and t.cydocIdProceso = :procesoId and t.tupaId.codProg = :tupaCodProg and t.tipoOperacionId.codProg = :tipoOpeIdCodProg ");
        q.setParameter("procesoId", procesoId);
        q.setParameter("tupaCodProg", tupaCodProg);
        q.setParameter("tipoOpeIdCodProg", tipoOpeIdCodProg);
        
        if(!q.getResultList().isEmpty()){
            return (SbProcesoTupa) q.getResultList().get(0);
        }
        return null;
    }
    
    public SbProcesoTupa obtenerTupaByTupaIdByCydocProcesoIdByTipoOperacion(Long tupaId, Integer procesoId, Long tipoOperacionId) {
        Query q = em.createQuery("select t from SbProcesoTupa t where t.activo = 1 and t.cydocIdProceso = :cydocIdProceso and t.tupaId.id = :tupaId and t.tipoOperacionId.id = :tipoOperacionId ");
        q.setParameter("cydocIdProceso", procesoId);
        q.setParameter("tupaId", tupaId);
        q.setParameter("tipoOperacionId", tipoOperacionId);
        
        if(!q.getResultList().isEmpty()){
            return (SbProcesoTupa) q.getResultList().get(0);
        }
        return null;
    }    

    public SbProcesoTupa obtenerTupaByTupaIdByTipoProceso(String tupa, String modalidad) {
        Query q = em.createQuery("select t from SbProcesoTupa t where t.activo = 1 and t.tupaId.codProg = :tupa and t.procesoIntegradoId.codProg = :modalidad ");
        q.setParameter("tupa", tupa);
        q.setParameter("modalidad", modalidad);
        
        if(!q.getResultList().isEmpty()){
            return (SbProcesoTupa) q.getResultList().get(0);
        }
        return null;
    }    

}
