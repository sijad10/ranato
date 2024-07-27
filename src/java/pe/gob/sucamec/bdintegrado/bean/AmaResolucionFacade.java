/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.AmaResolucion;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class AmaResolucionFacade extends AbstractFacade<AmaResolucion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaResolucionFacade() {
        super(AmaResolucion.class);
    }

    public AmaResolucion obtenerArmaVerificadaByNroExpediente(String nroExpediente) {
        Query q = em.createQuery("select a from AmaResolucion a where a.nroExpediente = :nroExpediente");
        q.setParameter("nroExpediente", nroExpediente.trim());
        return (AmaResolucion) q.getSingleResult();
    }

    public AmaResolucion obtenerArmaVerificadaById(Long id) {
        Query q = em.createQuery("select a from AmaResolucion a where a.id = :id");
        q.setParameter("id", id);
        return (AmaResolucion) q.getSingleResult();
    }

    /**
     * Función que obtiene las resoluciones por ruc y codigo de proceso.
     *
     * @param ruc
     * @param codProceso
     * @return
     * @author Gino Chávez
     */
    public List<AmaResolucion> obtenerResolucionesVigentes(String ruc) {
        Query q = em.createQuery("select a from AmaResolucion a where a.activo = 1 and a.personaId.ruc = :ruc and a.procesoId.codProg in ('TP_PRTUP_AMA_COM','PA340006C9') and func('trunc',current_date) between func('trunc',a.fechaInicio) and func('trunc',a.fechaVencimiento) ");
        q.setParameter("ruc", ruc);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return null;
    }

    /**
     * BUSCAR RESOLUCIONES
     *
     * @param per
     * @param proceso
     * @param fecha
     * @return
     */
    public List<AmaResolucion> buscarResolucionesVigentes(SbPersona per, String proceso, Date fecha) {
        Query q = em.createQuery("SELECT p FROM AmaResolucion p WHERE p.personaId = :per and p.eventoTrazaResId.codProg='TP_ESTAU_FIN' "
                + "and p.activo = 1 and p.procesoId.codProg in (" + proceso + ") "
                + "and FUNC('trunc',:fecha) between FUNC('trunc',p.fechaInicio) and FUNC('trunc',p.fechaVencimiento)");
        q.setParameter("per", per);
        q.setParameter("fecha", fecha, TemporalType.TIMESTAMP);
        return q.getResultList();
    }
    
}
