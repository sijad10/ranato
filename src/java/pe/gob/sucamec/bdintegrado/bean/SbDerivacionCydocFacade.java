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
import pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbDerivacionCydocFacade extends AbstractFacade<SbDerivacionCydoc> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbDerivacionCydocFacade() {
        super(SbDerivacionCydoc.class);
    }
    
    public SbDerivacionCydoc obtenerUsuarioDerivacionDistritoCyDoc(Long sistemaId, Long tipoProcesoId, Long distritoId) {
        String jpql = "select s from SbDerivacionCydoc s " +
                        " inner join SbAsignacionUbicacion a on s.sedeId = a.areaId and s.sistemaId = a.sistemaId and s.tipoProcesoId = a.tipoProcesoId " +
                        " where s.activo = 1 and a.activo = 1 " +
                        " and a.sistemaId.id = :sistemaId " +
                        " and a.tipoProcesoId.id = :tipoProcesoId " +
                        " and a.distritoId.id = :distritoId ";
        Query q = em.createQuery(jpql);
        q.setParameter("sistemaId", sistemaId);
        q.setParameter("tipoProcesoId", tipoProcesoId);
        q.setParameter("distritoId", distritoId);
        
        if(!q.getResultList().isEmpty()){
            return (SbDerivacionCydoc) q.getResultList().get(0);
        }        
        return null;
    }
    
    public SbDerivacionCydoc obtenerUsuarioDerivacionDepartamentoCyDoc(Long sistemaId, Long tipoProcesoId, Long departamentoId) {
        String jpql = "select s from SbDerivacionCydoc s " +
                        " inner join SbAsignacionUbicacion a on s.sedeId = a.areaId and s.sistemaId = a.sistemaId and s.tipoProcesoId = a.tipoProcesoId " +
                        " where s.activo = 1 and a.activo = 1 " +
                        " and a.sistemaId.id = :sistemaId " +
                        " and a.tipoProcesoId.id = :tipoProcesoId " +
                        " and a.departamentoId.id = :departamentoId ";
        Query q = em.createQuery(jpql);
        q.setParameter("sistemaId", sistemaId);
        q.setParameter("tipoProcesoId", tipoProcesoId);
        q.setParameter("departamentoId", departamentoId);
        
        if(!q.getResultList().isEmpty()){
            return (SbDerivacionCydoc) q.getResultList().get(0);
        }        
        return null;
    }
    
    public SbDerivacionCydoc obtenerDerivacionCyDoc(Long sistemaId, Long tipoProcesoId, Long sedeId) {
        String jpql = "select s from SbDerivacionCydoc s " +
                        " where s.activo = 1 " +
                        " and s.sistemaId.id = :sistemaId " +
                        " and s.tipoProcesoId.id = :tipoProcesoId " +
                        " and s.sedeId.id = :sedeId ";
        Query q = em.createQuery(jpql);
        q.setParameter("sistemaId", sistemaId);
        q.setParameter("tipoProcesoId", tipoProcesoId);
        q.setParameter("sedeId", sedeId);
        
        if(!q.getResultList().isEmpty()){
            return (SbDerivacionCydoc) q.getResultList().get(0);
        }        
        return null;
    }
    
    public SbDerivacionCydoc obtenerDerivacionXSistemaXProcesoCodXCydocProceso(Long sistemaId, String tipoProcesoCodProg, Long cydocProceso) {
        String jpql = "select s from SbDerivacionCydoc s " +
                        " where s.activo = 1 " +
                        " and s.sistemaId.id = :sistemaId " +
                        " and s.tipoProcesoId.codProg = :tipoProcesoCodProg " +
                        " and s.cydocProcesoId = :cydocProceso ";
        Query q = em.createQuery(jpql);
        q.setParameter("sistemaId", sistemaId);
        q.setParameter("tipoProcesoCodProg", tipoProcesoCodProg);
        q.setParameter("cydocProceso", cydocProceso);
        
        if(!q.getResultList().isEmpty()){
            return (SbDerivacionCydoc) q.getResultList().get(0);
        }        
        return null;
    }

}
