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
import org.apache.commons.lang3.StringUtils;
import pe.gob.sucamec.bdintegrado.data.EppLicencia;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppLicenciaFacade extends AbstractFacade<EppLicencia> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppLicenciaFacade() {
        super(EppLicencia.class);
    }

    public List<EppLicencia> buscarManipuladorAutUso(String id) {
        try {
            String jpql = "select u from EppLicencia u where u.activo = 1 and u.tipoCargoId.codProg = 'TP_CARGO_MAN' and u.fecVenc >= current_date and u.registroId.tipoOpeId.codProg not in ('TP_OPE_CAN','TP_OPE_COF') ";
            if (!StringUtils.isNumeric(id)) {
                jpql = jpql + " and (u.personaId.nombres like :p_param)";
            } else {
                jpql = jpql + " and (u.personaId.numDoc like :p_nro or trim(u.nroLicencia) like :p_nro)";
            }
            Query q = getEntityManager().createQuery(jpql);
            if (!StringUtils.isNumeric(id)) {
                q.setParameter("p_param", "%" + id + "%");
            } else {
                q.setParameter("p_nro", "%" + id + "%");
            }
            return q.setMaxResults(50).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 
     * @param p_nombre
     * @param p_dni
     * @return 
     */
    public List<EppLicencia> buscarConductor(String p_nombre, String p_dni) {
        Query q = getEntityManager().createQuery("select r from EppLicencia r "
                + "where "
                //+ "r.tipoCargoId.codProg = 'TP_CARGO_TRANS' and "
                + "r.activo = 1 and "
                + "r.registroId.tipoOpeId.codProg not in ('TP_OPE_CAN','TP_OPE_COF') and "
                + "r.fecVenc >= current_date and "
                + "(r.personaId.nombres like :p_nom or :p_nom = null) and "
                + "(r.personaId.numDoc like :p_dni or :p_dni = null)");
        q.setParameter("p_nom", "%" + p_nombre + "%");
        q.setParameter("p_dni", "%" + p_dni + "%");
        return q.setMaxResults(50).getResultList();
    }

    /**
     * 
     * @param p_nombre
     * @param p_dni
     * @return 
     */
    public List<EppLicencia> buscarCustodio(String p_nombre, String p_dni) {
        Query q = getEntityManager().createQuery("select r from EppLicencia r "
                + "where "
                + "r.tipoCargoId.codProg = 'TP_CARGO_CUST' and "
                + "r.activo = 1 and "
                + "r.registroId.tipoOpeId.codProg not in ('TP_OPE_CAN','TP_OPE_COF') and "
                + "(r.personaId.nombres like :p_nom or :p_nom = null) and "
                + "(r.personaId.numDoc like :p_dni or :p_dni = null)");
        q.setParameter("p_nom", "%" + p_nombre + "%");
        q.setParameter("p_dni", "%" + p_dni + "%");
        return q.setMaxResults(50).getResultList();
    }
    public List<Map> listarLicenciasActaEntrega(String nroExpediente,String tipoLic, Date fechaIni, Date fechaFin, String empresa, String manipulador,  Long nroLicencia) {
        List<Map> res = new ArrayList<>();
        String jpql = "";

        jpql = "SELECT distinct l.id as id , l.registroId.fecha as fecha , l.registroId.empresaId.rznSocial as titular , l.registroId.nroExpediente as expe , l.registroId.tipoRegId.nombre as tipoAut , l.nroLicencia,l.personaId.numDoc AS DNI, CONCAT(l.personaId.apePat,' ',l.personaId.apeMat,', ',l.personaId.nombres) AS NOMBRE, "
                + " l.registroId.empresaId.ruc, (CASE WHEN l.registroId.empresaId.rznSocial IS NULL THEN CONCAT(l.registroId.empresaId.apePat,' ',l.registroId.empresaId.apeMat,', ',l.registroId.empresaId.nombres) ELSE l.registroId.empresaId.rznSocial END) AS RZN_SOCIAL ,"
                + " l.fecEmi, l.fecVenc, l.tipoEmisionId.nombre AS TIPO_EMISION ,"
                + " l.registroId.audLogin, tb.nombre AS AREA, l.id, l.tipoCargoId.abreviatura AS CATEGORIA, l.tipoCargoId.nombre AS CATEGORIA_NOMBRE, "
                + "  l.registroId.tipoOpeId.nombre as tipoOpe "
                + " FROM EppLicencia l "
                + " LEFT JOIN l.registroId.eppRegistroEventoList ev "
                + " INNER JOIN TipoBase tb on tb.id = u.areaId.id "
                + " INNER JOIN SbUsuario u on l.registroId.audLogin = u.login "
                + " WHERE l.activo = 1 and l.registroId.activo = 1 and l.nroLicencia is not null "
                + " AND l.registroId.tipoProId.codProg not in ('TP_PRTUP_AMANFA')"
                //+ " and l.registroId.estado.codProg = 'TP_REGEV_FIR' "
                + " and ev.tipoEventoId.codProg = 'TP_REGEV_FIN' "
                + " and ev.activo = 1 and l.digital=1";
                
        if (nroExpediente != null) {
            jpql = jpql + " and l.registroId.nroExpediente = :nroExpediente";
        }
        if (tipoLic != null) {
            jpql = jpql + " and l.tipoCargoId.codProg = :tipoLic";
        }
        if (fechaIni != null) {
            jpql = jpql + " and FUNC('trunc',l.fecEmi) >= FUNC('trunc',:fechaIni) ";
        }
        if (fechaFin != null) {
            jpql = jpql + " and FUNC('trunc',l.fecEmi) <= FUNC('trunc',:fechaFin) ";
        }
        if (empresa != null && !empresa.isEmpty()) {
            jpql = jpql + " and ( l.registroId.empresaId.ruc like :empresaId  OR l.registroId.empresaId.rznSocial like :empresaId)";
        }
        if (manipulador != null && !manipulador.isEmpty()) {
            jpql = jpql + " and ( l.personaId.numDoc = :manipuladorDni OR CONCAT(l.personaId.apePat, ' ', l.personaId.apeMat, ' ', l.personaId.nombres) like :manipuladorNombres)";
        }
        
        if(nroLicencia != null){
            jpql = jpql + " and l.nroLicencia = :nroLicencia ";
        }
        jpql = jpql + " order by l.nroLicencia asc ";
        

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
    public List<EppLicencia> buscarLicenciasxIds(String IDs) {
        Query q = em.createQuery("select e from EppLicencia e where e.id in (" + IDs + ") and e.activo = 1");

        return q.getResultList();
    }
    
}
