/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.SspCarne;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspCarneFacade extends AbstractFacade<SspCarne> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspCarneFacade() {
        super(SspCarne.class);
    }
    
    public List<Map> buscarBandeja(HashMap nmap) {
        String jpql = "select distinct r.id as ID, r.nroExpediente AS NRO_EXPEDIENTE, "
                      + " CONCAT(s.vigilanteId.nombres, ' ', s.vigilanteId.apePat, ' ', s.vigilanteId.apeMat) AS VIGILANTE, "
                      + " s.fechaIni AS FECHA_INI, s.fechaFin AS FECHA_FIN, "
                      + " r.tipoOpeId.codProg AS TIPO_OPE_CODPROG, r.tipoOpeId.nombre AS TIPO_OPE_NOMBRE, "
                      + " r.tipoRegId.codProg AS TIPO_REG_CODPROG, r.tipoRegId.nombre AS TIPO_REG_NOMBRE, "
                      + " s.modalidadId.codProg AS MODALIDAD_CODPROG, s.modalidadId.nombre AS MODALIDAD_NOMBRE, "
                      + " r.estadoId.codProg AS ESTADO_CODPROG, r.estadoId.nombre AS ESTADO_NOMBRE, "
                      + " s.estadoId.codProg AS ESTADO_CARNE_CODPROG, s.estadoId.nombre AS ESTADO_CARNE_NOMBRE, "
                      + " td.nombre AS TIPO_DOC, s.vigilanteId.numDoc AS NUM_DOC, "
                      + " r.nroSolicitiud as NRO_SOLICITUD, s.nroCarne NRO_CARNE, "
                      + " cu.tipoServicio.nombre TIPO_SERVICIO, "
                      + " (case when ((select max(cc.fechaFin) from SspAlumnoCurso cc where cc.personaId.id = s.vigilanteId.id ) >= current_date) then 'SI' else 'NO' end) as ESTADOCURSO "                      
                      + " from SspCarne s "
                      + " left join s.sspRegistroList r"
                      + " left join s.vigilanteId.tipoDoc td "
                      + " left join r.sspRegistroEventoList l "
                      + " left join s.vigilanteId.sspAlumnoCursoList c "
                      + " left join c.registroCursoId cu "
                      + " where r.activo = 1 and s.activo = 1 and r.empresaId.id = :administrado and c.activo = 1 ";
                      //+ " where r.activo = 1 and s.activo = 1 and r.empresaId.id = :administrado and c.activo = 1 and c.fechaFin >= current_date ";
        
        if(nmap.get("tipoCarne") != null){
            switch(nmap.get("tipoCarne").toString()){
                case "TODOS":
                        break;
                case "ESTANDAR":
                        jpql += " and s.modalidadId.codProg not in ('TP_MCO_EVT')";
                        break;
                default:
                        jpql += " and s.modalidadId.codProg in ('TP_MCO_EVT')";
                        break;
            }
        }
        if(nmap.get("tipoPro") != null){
            jpql += " and r.tipoProId.codProg = :tipoPro";
        }
        if(nmap.get("fechaIni") != null || nmap.get("fechaFin") != null){
            jpql += " and l.tipoEventoId.codProg = 'TP_ECC_TRA' ";
        }
        
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1: //ID
                        jpql += " and r.id = :filtro";
                        break;
                case 2: //Expediente
                        jpql += " and r.nroExpediente = :filtro";
                        break;
                case 3: //Nombre Prospecto/Vigilante
                        jpql += " and ( CONCAT(s.vigilanteId.nombres,' ',s.vigilanteId.apePat, ' ', s.vigilanteId.apeMat) like :filtro )";
                        break;
                case 4: //DNI Nombre Prospecto/Vigilante
                        jpql += " and ( s.vigilanteId.numDoc = :filtro )";
                        break;
                case 5: //Modalidad
                        jpql += " and ( s.modalidadId.id = :filtro )";
                        break;
                case 6: //Nro. Solicitud
                        jpql += " and ( r.nroSolicitiud like :filtro )";
                        break;
                case 7: //tipo de servicio
                        jpql += " and ( cu.tipoServicio.id = :filtro )";
                        break;
            }
        }
        if(nmap.get("fechaIni") != null){
            jpql += " and FUNC('trunc',l.fecha) >= FUNC('trunc', :fechaIni ) ";
        }
        if(nmap.get("fechaFin") != null){
            jpql += " and FUNC('TRUNC',l.fecha) <= FUNC('TRUNC', :fechaFin ) ";
        }
        if(nmap.get("tipoReg") != null){
            jpql += " and r.tipoRegId.id = :tipoReg ";
        }
        if(nmap.get("estado") != null){
            jpql += " and r.estadoId.id = :estado ";
        }
        jpql += " order by r.id desc";
        
        Query q = em.createQuery(jpql);
        
        q.setParameter("administrado", (Long) nmap.get("administrado") );
        
        if(nmap.get("tipoPro") != null){
            q.setParameter("tipoPro", nmap.get("tipoPro").toString() );
        }
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1:                
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("filtro")) );
                        break;                
                case 2:
                case 4:
                        q.setParameter("filtro", ""+ nmap.get("filtro") );
                        break;
                case 3:
                case 6:
                        q.setParameter("filtro", "%"+ nmap.get("filtro")+"%" );
                        break;
                case 5:
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("modalidad")) );
                        break;
                case 7:
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("tipoServicio")) );
                        break;
            }
        }
        if(nmap.get("fechaIni") != null){
            q.setParameter("fechaIni", (Date) nmap.get("fechaIni"), TemporalType.DATE );
        }
        if(nmap.get("fechaFin") != null){
            q.setParameter("fechaFin", (Date) nmap.get("fechaFin"), TemporalType.DATE );
        }
        if(nmap.get("tipoReg") != null){
            q.setParameter("tipoReg", (Long) nmap.get("tipoReg") );
        }
        if(nmap.get("estado") != null){
            q.setParameter("estado", (Long) nmap.get("estado") );
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> buscarBandejaCeseDev(HashMap nmap) {
        String jpql = "select distinct r.id as ID, r.nroExpediente AS NRO_EXPEDIENTE, "
                      + " CONCAT(s.vigilanteId.nombres, ' ', s.vigilanteId.apePat, ' ', s.vigilanteId.apeMat) AS VIGILANTE, "
                      + " s.fechaIni AS FECHA_INI, s.fechaFin AS FECHA_FIN, "
                      + " r.tipoOpeId.codProg AS TIPO_OPE_CODPROG, r.tipoOpeId.nombre AS TIPO_OPE_NOMBRE, "
                      + " r.tipoRegId.codProg AS TIPO_REG_CODPROG, r.tipoRegId.nombre AS TIPO_REG_NOMBRE, "
                      + " s.modalidadId.codProg AS MODALIDAD_CODPROG, s.modalidadId.nombre AS MODALIDAD_NOMBRE, "
                      + " r.estadoId.codProg AS ESTADO_CODPROG, r.estadoId.nombre AS ESTADO_NOMBRE, "
                      + " s.estadoId.codProg AS ESTADO_CARNE_CODPROG, s.estadoId.nombre AS ESTADO_CARNE_NOMBRE, "
                      + " s.fechaBaja as FECHA_BAJA, s.nroCarne NRO_CARNE, "
                      + " td.nombre AS TIPO_DOC, s.vigilanteId.numDoc AS NUM_DOC, "
                      + " r.nroSolicitiud as NRO_SOLICITUD "
                      + " from SspCarne s "
                      + " left join s.sspRegistroList r"
                      + " left join s.vigilanteId.tipoDoc td "
                      + " where r.activo = 1 and r.empresaId.id = :administrado ";
        
        if(nmap.get("tipoPro") != null){
            jpql += " and r.tipoProId.codProg = :tipoPro";
        }
        if(nmap.get("esDevuelto") != null){
            if((boolean)nmap.get("esDevuelto")){
                jpql += " and s.estadoId.codProg = 'TP_ECRN_DVT' ";    
            }else{
                jpql += " and s.estadoId.codProg = 'TP_ECRN_PTE' ";
            }
        }
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1: //ID
                        jpql += " and r.id = :filtro";
                        break;
                case 2: //Expediente
                        jpql += " and r.nroExpediente = :filtro";
                        break;
                case 3: //Nombre Prospecto/Vigilante
                        jpql += " and ( CONCAT(s.vigilanteId.nombres,' ',s.vigilanteId.apePat, ' ', s.vigilanteId.apeMat) like :filtro )";
                        break;
                case 4: //DNI Nombre Prospecto/Vigilante
                        jpql += " and ( s.vigilanteId.numDoc = :filtro )";
                        break;
                case 5: //Modalidad
                        jpql += " and ( s.modalidadId.id = :filtro )";
                        break;
                case 6: //Nro. Solicitud
                        jpql += " and ( r.nroSolicitiud like :filtro )";
                        break;
            }
        }
        if(nmap.get("fechaIni") != null){
            jpql += " and FUNC('trunc',r.fecha) >= FUNC('trunc', :fechaIni ) ";
        }
        if(nmap.get("fechaFin") != null){
            jpql += " and FUNC('TRUNC',r.fecha) <= FUNC('TRUNC', :fechaFin ) ";
        }
        if(nmap.get("tipoReg") != null){
            jpql += " and s.registroId.tipoRegId.id = :tipoReg ";
        }
        if(nmap.get("estado") != null){
            jpql += " and s.registroId.estadoId.id = :estado ";
        }
        jpql += " order by r.id desc";
        
        Query q = em.createQuery(jpql);
        q.setParameter("administrado", (Long) nmap.get("administrado") );
        if(nmap.get("tipoPro") != null){
            q.setParameter("tipoPro", nmap.get("tipoPro").toString() );
        }
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1:                
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("filtro")) );
                        break;                
                case 2:
                case 4:
                        q.setParameter("filtro", ""+ nmap.get("filtro") );
                        break;
                case 3:
                case 6:
                        q.setParameter("filtro", "%"+ nmap.get("filtro")+"%" );
                        break;
                case 5:
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("modalidad")) );
                        break;
            }
        }
        if(nmap.get("fechaIni") != null){
            q.setParameter("fechaIni", (Date) nmap.get("fechaIni"), TemporalType.DATE );
        }
        if(nmap.get("fechaFin") != null){
            q.setParameter("fechaFin", (Date) nmap.get("fechaFin"), TemporalType.DATE );
        }
        if(nmap.get("tipoReg") != null){
            q.setParameter("tipoReg", (Long) nmap.get("tipoReg") );
        }
        if(nmap.get("estado") != null){
            q.setParameter("estado", (Long) nmap.get("estado") );
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public SspCarne buscarCarneCesadoXRucXModalidadXDoc(Long administradoId, Long modalidadId, Long vigilanteId, Long carneId) {
        String jpql = "select s "                      
                      + " from SspCarne s "
                      + " where s.registroId.empresaId.id = :administradoId "
                      + " and s.modalidadId.id = :modalidadId "
                      + " and s.vigilanteId.id = :vigilanteId "
                      + " and s.registroId.tipoProId.codProg = 'TP_GSSP_CCRN' ";
        if(carneId != null){
            jpql += " and s.id != :carneId ";
        }
        jpql += " order by s.id desc ";
        
        Query q = em.createQuery(jpql);
        q.setParameter("administradoId", administradoId );
        q.setParameter("modalidadId", modalidadId );
        q.setParameter("vigilanteId", vigilanteId );
        if(carneId != null){
            q.setParameter("carneId", carneId );
        }
        if(!q.getResultList().isEmpty()){
            return (SspCarne) q.getResultList().get(0);
        }
        
        return null;
    }
    
    public Map buscarVigilanteCarteraCliByNroCarne(Long nroCarne, Long empresaId) {
        try {
            String jpql = "select (case when td.id is null then 2 when td.codProg = 'TP_DOCID_DNI' then 2 else 5 end) as TIP_USR,"
                          + " td.codProg TIPO_DOC, s.vigilanteId.numDoc DOC, "
                          + " s.vigilanteId.nombres NOMBRES, CONCAT(s.vigilanteId.apePat, ' ', s.vigilanteId.apeMat) AS APELLIDOS, "
                          + " s.nroCarne NRO_CRN_VIG, s.fechaIni AS FEC_EMI, s.fechaFin AS FEC_VENC, s.activo ACTIVO, "
                          + " s.modalidadId.nombre MODALIDAD, s.modalidadId.codProg MODALIDAD_CODPROG "
                          + " from SspCarne s "
                          + " left join s.vigilanteId.tipoDoc td"
                          + " left join s.sspRegistroList r"
                          + " where r.activo = 1 "
                          + " and r.empresaId.id = :empresaId and s.nroCarne = :nroCarne "
                          + " and s.emitida = 1 and r.tipoProId.codProg = 'TP_GSSP_ECRN' "
                          + " order by s.id desc ";

            Query q = em.createQuery(jpql);
            q.setParameter("nroCarne", nroCarne);
            q.setParameter("empresaId", empresaId);
            q.setHint("eclipselink.result-type", "Map");
            if(!q.getResultList().isEmpty()){
                return (Map) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }            
        return null;
    }
    
    public List<Map> buscarVigilanteVigenteCarteraCli(String tipoDoc, String numDoc, String ruc) {
        try {
            String jpql = " SELECT TIP_USR, COD_USR AS DOC, NOMBRE, NOMBRE_VIG, APE_PAT, APE_MAT, "+
                          " NRO_CRN_VIG, FEC_EMI, FEC_VENC, ESTADO, DES_MOD AS MODALIDAD, MODALIDAD_ID, " +
                          " ID AS REGISTRO_ID " +
                          " FROM BDINTEGRADO.WS_VIGILANTES " +
                          " WHERE RUC = ?1 ";

            switch(tipoDoc){
                case "2":
                case "5":
                        jpql += " AND COD_USR = ?2 ";
                        break;
                case "1":
                        //jpql += " and s.nroCarne = :numDoc ";
                        break;
            }
            //jpql += " order by s.id desc ";

            Query q = em.createNativeQuery(jpql);
            switch(tipoDoc){
                case "2":
                case "5":
                        q.setParameter(2, Long.parseLong(numDoc));
                        break;
                case "1":                    
                        //
                        break;
            }        
            q.setParameter(1, ruc);        
            q.setHint("eclipselink.result-type", "Map");

            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
    
    public List<HashMap> listarVigilantes(String numdoc, String carne, String estado, String ruc) {
        String sql = "SELECT 1 AS TIPO, A.* FROM BDINTEGRADO.WS_VIGILANTES A "
                + " WHERE COD_USR LIKE ?1 AND NRO_CRN_VIG LIKE ?2 AND ESTADO LIKE ?3 AND RUC = ?4  ";

        Query q = em.createNativeQuery(sql);
        q.setParameter(1, JsfUtil.nullATodo(numdoc));
        q.setParameter(2, JsfUtil.nullATodo(carne));
        q.setParameter(3, JsfUtil.nullATodo(estado));
        q.setParameter(4, JsfUtil.nullATodo(ruc));
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> listarTotVigilantes(String numdoc, String carne) {
        String sql = "SELECT 1 AS TIPO, WV.*,'V' AS TIPO_REG FROM BDINTEGRADO.WS_VIGILANTES WV "
                + "WHERE (1=1) ";

        if (numdoc != null) {
            sql = sql + " AND ( WV.COD_USR LIKE ?1 ) ";
        }
        if (carne != null) {
            sql = sql + " AND WV.NRO_CRN_VIG LIKE ?2 ";
        }
        Query q = em.createNativeQuery(sql);
        if (numdoc != null) {
            q.setParameter(1, JsfUtil.nullATodo(numdoc));
        }
        if (carne != null) {
            q.setParameter(2, JsfUtil.nullATodo(carne));
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<HashMap> listarHistVigilantes(String numDoc) {
        String jpql = "select distinct r.empresaId.ruc as RUC, r.empresaId.rznSocial as RZN_SOC, s.modalidadId.nombre as DES_MOD, s.modalidadId.codProg MODALIDAD_CODPROG, "
                      + " s.fechaIni as FEC_EMI, s.fechaFin as FEC_VENC, s.fechaBaja as FEC_BAJA, "
                      + " r.tipoOpeId.codProg as TIPO_OPE_CODPROG, r.tipoOpeId.nombre as TIPO_OPE, "
                      + " r.tipoRegId.codProg as TIPO_REG_CODPROG, r.tipoRegId.nombre as TIPO_REG, "
                      + " r.carneId.cese as CESE "
                      + " from SspCarne s "
                      + " left join s.sspRegistroList r"
                      + " where r.activo = 1 and s.activo = 0 "
                      + "  and s.vigilanteId.numDoc = :numDoc "
                      + "  and s.emitida = 1 "
                      + "  AND r.tipoOpeId.codProg != 'TP_REGIST_NULL' "
                      + "  and r.tipoProId.codProg = 'TP_GSSP_ECRN' ";
        jpql += " order by s.fechaBaja desc ";
        
        Query q = em.createQuery(jpql);        
        q.setParameter("numDoc", numDoc);
        q.setHint("eclipselink.result-type", "Map");
        
        return q.getResultList();
    }
    
    public List<Map> listarVigilantesHistorico(String numdoc, String carne) {
        String nroCarne = "";
        String sql = " SELECT 1 AS TIPO, WH.*, 'H' AS TIPO_REG  FROM BDINTEGRADO.WS_VIGILANTES_HIS_SEL WH "
                + " WHERE (1=1) ";

        if (numdoc != null) {
            sql += " AND (WH.COD_USR LIKE ?1 )";
        }
        if (carne != null) {
            sql += " AND WH.NRO_CRN_VIG LIKE ?2";
        }
        sql += " ORDER BY WH.NRO_CRN_VIG, WH.FEC_EMI DESC ";

        Query q = em.createNativeQuery(sql);
        if (numdoc != null) {
            q.setParameter(1, JsfUtil.nullATodo(numdoc));
        }
        if (carne != null) {
            q.setParameter(2, JsfUtil.nullATodo(carne));
        }
        q.setHint("eclipselink.result-type", "Map");
        List<Map> lstIDs = q.getResultList();
        List<Map> lstResultado = new ArrayList();                
        for (Map item : lstIDs) {
            if(item.get("NRO_CRN_VIG").toString().equals(nroCarne)){
               continue; 
            }else{
                lstResultado.add(item);
            }
            nroCarne = item.get("NRO_CRN_VIG").toString();
        }
        
        return lstResultado;
    }
    
    public List<HashMap> listarVigilantesxFiltro(String tipo, String filtro) {
        String campo = "", sql;
        switch (tipo){
            case "numdoc":
                campo = "COD_USR = ?1 ";
                break;
            case "carne":
                campo = "NRO_CRN_VIG = ?1 ";
                break;
            case "nombre":
                campo = "NOMBRE LIKE ?1 ";
                break;
            case "ruc":
                campo = "RUC = ?1 ";
                break;
        }
        sql = "SELECT * FROM BDINTEGRADO.WS_VIGILANTES ";
        Query q = em.createNativeQuery(
                sql
                + "WHERE " + campo
        );
        switch (tipo){
            case "carne":                
            case "ruc":
            case "numdoc":
                q.setParameter(1, filtro.trim().toUpperCase());
                break;
            case "nombre":
                q.setParameter(1, JsfUtil.nullATodoComodin(filtro).toUpperCase());
                break;
        }        
        
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> listarVigilantesXNumDoc(String numdoc) {
        Query q = em.createNativeQuery(
                "SELECT * FROM BDINTEGRADO.WS_VIGILANTES"
                + "WHERE COD_USR = ?1"
        );
        q.setParameter(1, JsfUtil.nullATodo(numdoc));
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> buscarBandejaDevolucion(HashMap parametros) {
        String jpql = "select r.nroSolicitiud AS NRO_SOLICITUD, count(s.id) AS TOTAL_INSCRITOS, "
                      + " s.fechaBaja as FECHA_BAJA, s.estadoId.codProg AS ESTADO_CODPRG, s.estadoId.nombre AS ESTADO_NOMBRE "
                      + " from SspCarne s "
                      + " left join s.sspRegistroList r"
                      + " where r.activo = 1 and s.emitida = 1 "
                      + "  and r.empresaId.id = :empresaId "
                      + "  and s.estadoId.codProg in ('TP_ECRN_PTE','TP_ECRN_DVT') ";
        if(parametros.get("fechaIni") != null){
            jpql += " and func('trunc',s.fechaBaja) >= func('trunc',:fechaIni)";
        }
        if(parametros.get("fechaFin") != null){
            jpql += " and func('trunc',s.fechaBaja) <= func('trunc',:fechaFin)";
        }
        jpql +=  " group by r.nroSolicitiud, s.fechaBaja, s.estadoId.codProg, s.estadoId.nombre " 
                +" order by r.nroSolicitiud ";
        
        Query q = em.createQuery(jpql);
        q.setParameter("empresaId", (Long) parametros.get("empresaId"));
        if(parametros.get("fechaIni") != null){
            q.setParameter("fechaIni", (Date) parametros.get("fechaIni"), TemporalType.DATE);
        }
        if(parametros.get("fechaFin") != null){
            q.setParameter("fechaFin", (Date) parametros.get("fechaFin"), TemporalType.DATE);
        }
        q.setHint("eclipselink.result-type", "Map");
        
        return q.getResultList();
    }
    
    public List<Map> buscarVigilanteXEmpresaXNumDoc(String numDoc, Long empresaId) {
        String jpql = "select distinct r.id AS ID, td.nombre AS TIPODOC, td.codProg TIPODOC_CODPROG, s.vigilanteId.numDoc NUMDOC, "
                      + " s.vigilanteId.nombres NOMBRES, s.vigilanteId.apePat AS APAT, s.vigilanteId.apeMat AS AMAT, "
                      + " s.modalidadId.nombre DES_MOD, s.modalidadId.codProg TIP_MOD, "
                      + " s.nroCarne NRO_CRN_VIG, s.fechaIni AS FEC_EMI, s.fechaFin AS FEC_VENC "
                      + " from SspCarne s "
                      + " left join s.vigilanteId.tipoDoc td"
                      + " left join s.sspRegistroList r"
                      + " where r.activo = 1 and s.activo = 1 "
                      + "  and r.empresaId.id = :empresaId "
                      + "  and s.emitida = 1 and r.tipoProId.codProg = 'TP_GSSP_ECRN' "
                      + "  and s.vigilanteId.numDoc = :numDoc "
                      + " order by s.id desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("numDoc", numDoc);
        q.setParameter("empresaId", empresaId);
        q.setHint("eclipselink.result-type", "Map");
        
        return q.getResultList();
    }
    
    public SspCarne buscarCarneById(Long carneId) {
        String jpql = "select s "                      
                      + " from SspCarne s "
                      + " where s.id = :carneId ";
        
        Query q = em.createQuery(jpql);
        q.setParameter("carneId", carneId );
        
        if(!q.getResultList().isEmpty()){
            return (SspCarne) q.getResultList().get(0);
        }
        return null;
    }
    
    public int contarModalidadVigenteByRucByModalidad(String ruc, Long modalidadId) {
        int cont = 0;
        javax.persistence.Query q = em.createNativeQuery(
                    "SELECT COUNT(*) FROM TRAMDOC.GSSP_VW_MODALIDAD_EMPRESA M " +
                    " INNER JOIN RMA1369.HOMOLOGAR_MODALIDAD@SUCAMEC H ON H.EMPMOD_ID = M.ID " +
                    " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD TMD ON TMD.ID = H.TIPO_ID " +
                    " WHERE RUC = ?1 AND H.TIPO_SEGURIDAD_ID = ?2 " + 
                    "   AND TMD.COD_PROG = 'TP_HOMOL_MODAL' ");
        q.setParameter(1, ruc);
        q.setParameter(2, modalidadId);
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }
    
    public SspCarne buscarCarneXRucXModalidadXDoc(String ruc, Long modalidadId, String numDoc, Long carneId) {
        String jpql = "select s "                      
                      + " from SspCarne s "
                      + " where s.registroId.empresaId.ruc = :ruc "
                      + " and s.modalidadId.id = :modalidadId "
                      + " and s.vigilanteId.numDoc = :numDoc "
                      + " and s.registroId.tipoProId.codProg = 'TP_GSSP_ECRN' "
                      + " and s.activo = 1 and s.registroId.activo = 1 "
                      + " and s.fechaFin >= current_date ";
        if(carneId != null){
            jpql += " and s.id != :carneId ";
        }
        jpql += " order by s.id desc ";
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc );
        q.setParameter("modalidadId", modalidadId );
        q.setParameter("numDoc", numDoc );
        if(carneId != null){
            q.setParameter("carneId", carneId );
        }
        if(!q.getResultList().isEmpty()){
            return (SspCarne) q.getResultList().get(0);
        }
        
        return null;
    }
    
    public Date calcularFechaLimite(Date fecha, int dias) {
        Date fechaCalculada = null;
        String jpql= "SELECT BDINTEGRADO.FNC_FECHA_CALCULADA(?1, ?2) FROM DUAL ";
        javax.persistence.Query q = em.createNativeQuery(jpql);
        q.setParameter(1, fecha, TemporalType.DATE);
        q.setParameter(2, dias);
        
        List<Date> results = q.getResultList();
        for (Date _values : results) {
            fechaCalculada = _values;
            break;
        }

        return fechaCalculada;
    }
    
    public List<Map> buscarBandejaImpresion(HashMap nmap) {
        String jpql = "select distinct r.id as ID, r.nroExpediente AS NRO_EXPEDIENTE, "
                      + " CONCAT(s.vigilanteId.nombres, ' ', s.vigilanteId.apePat, ' ', s.vigilanteId.apeMat) AS VIGILANTE, "
                      + " s.vigilanteId.nombres AS NOMBRES, s.vigilanteId.apePat AS APE_PAT, s.vigilanteId.apeMat AS APE_MAT, "
                      + " r.empresaId.ruc AS RUC, r.empresaId.rznSocial as RZN_SOCIAL, r.empresaId.nombres AS EMPRESA_NOMBRES, r.empresaId.apePat AS EMPRESA_APE_PAT, r.empresaId.apeMat AS EMPRESA_APE_MAT,  "
                      + " s.fechaIni AS FECHA_INI, s.fechaFin AS FECHA_FIN, "
                      + " r.tipoOpeId.codProg AS TIPO_OPE_CODPROG, r.tipoOpeId.nombre AS TIPO_OPE_NOMBRE, "
                      + " r.tipoRegId.codProg AS TIPO_REG_CODPROG, r.tipoRegId.nombre AS TIPO_REG_NOMBRE, "
                      + " s.modalidadId.codProg AS MODALIDAD_CODPROG, s.modalidadId.nombre AS MODALIDAD_NOMBRE, s.modalidadId.descripcion AS MODALIDAD_DESCRIPCION, "
                      + " td.nombre AS TIPO_DOC, s.vigilanteId.numDoc AS NUM_DOC, "
                      + " s.nroCarne NRO_CARNE, s.fotoId.nombreFoto as CARNE_FOTO, s.hashQr AS HASH_QR, "
                      + " s.formatoId.firmaId.postfirma AS POSTFIRMA, s.formatoId.firmaId.nombreImg AS NOMBRE_FIRMA, s.formatoId.versionId.fondo AS NOMBRE_FONDO, "
                      + " s.formatoId.firmaId.personaId.nombres AS NOMBRE_JEFE, s.formatoId.firmaId.personaId.apePat AS APE_PAT_JEFE, s.formatoId.firmaId.personaId.apeMat APE_MAT_JEFE "
                      + " from SspCarne s "
                      + " left join s.sspRegistroList r"
                      + " left join s.vigilanteId.tipoDoc td "
                      + " left join r.sspRegistroEventoList l "
                      + " where r.activo = 1 and s.digital = 1 and s.activo = 1 and s.cese = 0 and r.empresaId.id = :administrado "
                      + "  and r.tipoProId.codProg = 'TP_GSSP_ECRN' and func('trunc',s.fechaFin) >= func('trunc',current_date) "
                      + "  and r.estadoId.codProg = 'TP_ECC_APR' and l.tipoEventoId.codProg = 'TP_ECC_APR' ";
        
        if(nmap.get("fechaLimiteDuplicado") != null){
            jpql += " and func('trunc',s.fechaIni) >= func('trunc', :fechaLimiteDuplicado) ";
        }
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1: //ID
                        jpql += " and r.id = :filtro";
                        break;
                case 2: //Expediente
                        jpql += " and r.nroExpediente = :filtro";
                        break;
                case 3: //Nombre Prospecto/Vigilante
                        jpql += " and ( CONCAT(s.vigilanteId.nombres,' ',s.vigilanteId.apePat, ' ', s.vigilanteId.apeMat) like :filtro )";
                        break;
                case 4: //DNI Nombre Prospecto/Vigilante
                        jpql += " and ( s.vigilanteId.numDoc = :filtro )";
                        break;
                case 5: //Modalidad
                        jpql += " and ( s.modalidadId.id = :modalidad )";
                        break;
            }
        }
        if(nmap.get("sede") != null){
            jpql += " and ( r.sedeSucamec.id = :sede )";
        }
        if(nmap.get("fechaIni") != null){
            jpql += " and FUNC('trunc',l.fecha) >= FUNC('trunc', :fechaIni ) ";
        }
        if(nmap.get("fechaFin") != null){
            jpql += " and FUNC('TRUNC',l.fecha) <= FUNC('TRUNC', :fechaFin ) ";
        }
        jpql += " order by r.id desc";
        
        Query q = em.createQuery(jpql);
        q.setParameter("administrado", (Long) nmap.get("administrado") );
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1:                
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("filtro")) );
                        break;                
                case 2:
                case 4:
                        q.setParameter("filtro", ""+ nmap.get("filtro") );
                        break;
                case 3:
                        q.setParameter("filtro", "%"+ nmap.get("filtro")+"%" );
                        break;
                case 5:
                        q.setParameter("modalidad", Long.parseLong(""+nmap.get("modalidad")) );
                        break;
            }
        }
        
        if(nmap.get("sede") != null){
            q.setParameter("sede", Long.parseLong(""+nmap.get("sede")) );
        }
        if(nmap.get("fechaIni") != null){
            q.setParameter("fechaIni", (Date) nmap.get("fechaIni"), TemporalType.DATE );
        }
        if(nmap.get("fechaFin") != null){
            q.setParameter("fechaFin", (Date) nmap.get("fechaFin"), TemporalType.DATE );
        }
        if(nmap.get("fechaLimiteDuplicado") != null){
            q.setParameter("fechaLimiteDuplicado", (Date) nmap.get("fechaLimiteDuplicado"), TemporalType.DATE );
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public Map buscarBandejaMovil(String filtro, String filtroNroCarne) {
        try {
            String jpql = "select distinct r.id as ID, r.nroExpediente AS NRO_EXPEDIENTE, "
                          + " CONCAT(s.vigilanteId.nombres, ' ', s.vigilanteId.apePat, ' ', s.vigilanteId.apeMat) AS VIGILANTE, "
                          + " s.vigilanteId.nombres AS NOMBRES, s.vigilanteId.apePat AS APE_PAT, s.vigilanteId.apeMat AS APE_MAT, "
                          + " r.empresaId.ruc AS RUC, r.empresaId.rznSocial as RZN_SOCIAL, r.empresaId.nombres AS EMPRESA_NOMBRES, r.empresaId.apePat AS EMPRESA_APE_PAT, r.empresaId.apeMat AS EMPRESA_APE_MAT,  "
                          + " s.fechaIni AS FECHA_INI, s.fechaFin AS FECHA_FIN, "
                          + " r.tipoOpeId.codProg AS TIPO_OPE_CODPROG, r.tipoOpeId.nombre AS TIPO_OPE_NOMBRE, "
                          + " r.tipoRegId.codProg AS TIPO_REG_CODPROG, r.tipoRegId.nombre AS TIPO_REG_NOMBRE, "
                          + " s.modalidadId.codProg AS MODALIDAD_CODPROG, s.modalidadId.nombre AS MODALIDAD_NOMBRE, s.modalidadId.descripcion AS MODALIDAD_DESCRIPCION, "
                          + " td.nombre AS TIPO_DOC, s.vigilanteId.numDoc AS NUM_DOC, "
                          + " s.nroCarne NRO_CARNE, s.fotoId.nombreFoto as CARNE_FOTO, s.hashQr AS HASH_QR, "
                          + " s.activo AS ACTIVO, s.cese AS CESE, s.id as ID_CARNE "
                          + " from SspCarne s "
                          + " left join s.sspRegistroList r"
                          + " left join s.vigilanteId.tipoDoc td "
                          + " left join r.sspRegistroEventoList l "
                          + " where r.activo = 1 "
                          + "  and r.tipoProId.codProg = 'TP_GSSP_ECRN' "
                          + "  and s.vigilanteId.numDoc = :filtro"
                          + "  and s.nroCarne = :filtroNroCarne"
                          + "  and s.formatoId is not null "
                          + "  and r.estadoId.codProg = 'TP_ECC_APR' and l.tipoEventoId.codProg = 'TP_ECC_APR' ";

            jpql += " order by s.fechaIni desc";

            //System.out.println("QUERY --- " + jpql);
            
            Query q = em.createQuery(jpql);
            q.setParameter("filtro", filtro); 
            q.setParameter("filtroNroCarne", Integer.parseInt(filtroNroCarne));  
            q.setHint("eclipselink.result-type", "Map");

            if(!q.getResultList().isEmpty()){
                return (Map) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
