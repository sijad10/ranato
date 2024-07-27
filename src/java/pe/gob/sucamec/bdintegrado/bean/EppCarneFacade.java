/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.EppCarne;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppCarneFacade extends AbstractFacade<EppCarne> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppCarneFacade() {
        super(EppCarne.class);
    }
    
    public List<EppCarne> buscarCarnesMovil(String numDoc) {
        String jpql = "select r from EppRegistro r where r.activo = 1 and r.tipoProId.codProg = 'TP_PIRO_MANI' "
                    + " and r.estado.codProg in ('TP_REGEV_FIN', 'TP_REGEV_ANU') AND r.tipoOpeId.codProg != 'TP_OPE_CAN' "
                    + " and r.eppCarne.personaId.numDoc = :numDoc " ;
        String jpqlAnulados = "select r from EppRegistro r where r.activo = 1 and r.tipoProId.codProg = 'TP_PIRO_MANI' "
                            + " and r.tipoOpeId.codProg = 'TP_OPE_CAN' "
                            + " and r.registroId.eppCarne.personaId.numDoc = :numDoc " ;
        
        jpql = jpql + " UNION " + jpqlAnulados;
        Query q = em.createQuery(jpql);
        q.setParameter("numDoc", numDoc);
        return q.getResultList();
    }
    
    public List<EppRegistro> buscarRegistrosCarnesMovil(String numDoc) {
        String jpql = "select r from EppRegistro r where r.tipoProId.codProg = 'TP_PIRO_MANI' "
                    + " and r.estado.codProg in ('TP_REGEV_FIN', 'TP_REGEV_ANU') "
                    + " and r.eppCarne.personaId.numDoc = :numDoc " 
                    + " order by r.eppCarne.fechaEmision desc ";
        
        Query q = em.createQuery(jpql);
        q.setParameter("numDoc", numDoc);
        return q.getResultList();
    }
    public List<Map> listarCarneMPxFirmados(String nroExpediente,String tipoLic, Date fechaIni, Date fechaFin, String empresa, String manipulador,  Long nroLicencia) {
        List<Map> res = new ArrayList<>();
        
        String jpql = "SELECT l.id as ID,l.nroCarne as CARNE  , l.registroId.nroExpediente as NRO_EXPEDIENTE ,l.personaId.numDoc AS DNI, CONCAT(l.personaId.apePat,' ',l.personaId.apeMat,', ',l.personaId.nombres) AS NOMBRE, "
                + " l.registroId.empresaId.ruc as RUC , (CASE WHEN l.registroId.empresaId.rznSocial IS NULL THEN CONCAT(l.registroId.empresaId.apePat,' ',l.registroId.empresaId.apeMat,', ',l.registroId.empresaId.nombres) ELSE l.registroId.empresaId.rznSocial END) AS RZN_SOCIAL ,"
                + " l.fechaEmision as FEC_EMI, l.fechaVencimiento as FEC_VENC, "
                + " l.registroId.audLogin as user , tb.nombre AS AREA, l.id, te.abreviatura AS CATEGORIA, "
                + " l.registroId.tipoOpeId.nombre as tipoOpe, "
                + " CONCAT(l.registroId.usuarioEvaluadorId.nombres,' ',l.registroId.usuarioEvaluadorId.apePat,' ',l.registroId.usuarioEvaluadorId.apeMat) as EVALUADOR "
                + " FROM EppCarne l "
                + " INNER JOIN TipoBase tb on tb.id = u.areaId.id "
                + " INNER JOIN SbUsuario u on l.registroId.audLogin = u.login "
                
                + " LEFT JOIN l.tipoActividadId te "
                + " WHERE l.activo = 1 AND l.registroId.activo = 1 "
                //+ " and l.nroCarne is not null and l.nroCarne > 0 "
                + " and l.registroId.estado.codProg = 'TP_REGEV_FIN' "
                + " and l.registroId.tipoOpeId.codProg not in ('TP_OPE_CAN','TP_OPE_COF') and l.digital=1";

       
        if (nroExpediente != null) {
            jpql = jpql + " and l.registroId.nroExpediente = :nroExpediente";
        }
        if (tipoLic != null) {
            jpql = jpql + " and l.tipoActividadId.codProg = :tipoLic";
        }
        if (fechaIni != null) {
            jpql = jpql + " and FUNC('trunc',l.fechaEmision) >= FUNC('trunc',:fechaIni) ";
        }
        if (fechaFin != null) {
            jpql = jpql + " and FUNC('trunc',l.fechaEmision) <= FUNC('trunc',:fechaFin) ";
        }
        if (empresa != null && !empresa.isEmpty()) {
            jpql = jpql + " and ( l.registroId.empresaId.ruc like :empresaId  OR l.registroId.empresaId.rznSocial like :empresaId)";
        }
        if (manipulador != null && !manipulador.isEmpty()) {
            jpql = jpql + " and ( l.personaId.numDoc = :manipuladorDni OR CONCAT(l.personaId.apePat, ' ', l.personaId.apeMat, ' ', l.personaId.nombres) like :manipuladorNombres)";
        }
        
        if(nroLicencia != null){
            jpql = jpql + " and l.nroCarne = :nroLicencia ";
        }
        jpql = jpql + " order by l.nroCarne asc ";
        

        javax.persistence.Query q = em.createQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");

        if (tipoLic != null) {
            q.setParameter("tipoLic", tipoLic);
        }
        if (nroExpediente != null) {
            q.setParameter("nroExpediente", nroExpediente);
        }
        if (fechaIni != null) {
            q.setParameter("fechaIni", fechaIni, TemporalType.DATE);
        }
        if (fechaFin != null) {
            q.setParameter("fechaFin", fechaFin, TemporalType.DATE);
        }
        if (empresa != null && !empresa.isEmpty()) {
            q.setParameter("empresaId", "%" + empresa.trim().toUpperCase() + "%");
        }
        if (manipulador != null && !manipulador.isEmpty()) {
            q.setParameter("manipuladorDni", manipulador.trim() );
            q.setParameter("manipuladorNombres", "%" + manipulador.trim().toUpperCase() + "%");
        }
        
        if(nroLicencia != null){
            q.setParameter("nroLicencia", nroLicencia);
        }
        res = q.getResultList();

        return res;

    }
    /**
     * Obtener persona de licencia
     *
     * @param IDs Listado de ID de Licencia concatenadas
     * @return Listado de Licencias
     */
    public List<EppCarne> buscarLicenciasxIds(String IDs) {
        Query q = em.createQuery("select e from EppCarne e where e.id in (" + IDs + ") and e.activo = 1");

        return q.getResultList();
    }
}
