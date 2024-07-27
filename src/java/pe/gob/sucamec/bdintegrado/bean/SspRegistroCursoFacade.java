/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.SspRegistroCurso;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspRegistroCursoFacade extends AbstractFacade<SspRegistroCurso> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspRegistroCursoFacade() {
        super(SspRegistroCurso.class);
    }
    
    public List<SspRegistroCurso> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspRegistroCurso s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SspRegistroCurso> buscarCursosBandeja(HashMap nmap) {
        String jpql = "select distinct s "
                      + " from SspRegistroCurso s "
                      + " left join s.sspAlumnoCursoList a "
                      + " left join s.sspProgramacionList p "
                      + " left join s.sspInstructorModuloList i "
                      + " where s.activo = 1 and p.activo = 1 and s.administradoId.id = :administrado "
                      + " and s.tipoCurso.codProg in ("+nmap.get("tipocurso").toString()+") ";
        
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1:
                        jpql += " and s.id = :filtro";
                        break;
                case 2:
                        jpql += " and s.nroExpediente = :filtro";
                        break;
                case 3:
                        jpql += " and ( a.personaId.numDoc = :filtro )";
                        break;
                case 4:
                        jpql += " and i.activo = 1 and ( i.instructorId.personaId.numDoc = :filtro )";
                        break;
            }
        }
        if(nmap.get("fechaIni") != null){
            jpql += " and FUNC('trunc',s.fechaRegistro) >= FUNC('trunc', :fechaIni ) ";
        }
        if(nmap.get("fechaFin") != null){
            jpql += " and FUNC('TRUNC',s.fechaRegistro) <= FUNC('TRUNC', :fechaFin ) ";
        }
        if(nmap.get("tipoFormacion") != null){
            jpql += " and p.moduloId.tipoCursoId.id = :tipoFormacion and  p.moduloId.codModulo not in ('REFB','REFP') ";
        }
        if(nmap.get("local") != null){
            jpql += " and p.localId.id = :local ";
        }
        if(nmap.get("estadoCurso") != null){
            jpql += " and s.estadoId.id = :estadoCurso ";
        }
        if(nmap.get("tipoServicio") != null){
            jpql += " and s.tipoServicio.id = :tipoServicio ";
        }
        jpql += " order by s.id desc";
        
        Query q = em.createQuery(jpql);
        q.setParameter("administrado", (Long) nmap.get("administrado") );
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1:                
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("filtro")) );
                        break;
                case 2:
                case 3:
                case 4:
                        q.setParameter("filtro", ""+ nmap.get("filtro") );
                        break;
            }
        }
        if(nmap.get("fechaIni") != null){
            q.setParameter("fechaIni", (Date) nmap.get("fechaIni"), TemporalType.DATE );
        }
        if(nmap.get("fechaFin") != null){
            q.setParameter("fechaFin", (Date) nmap.get("fechaFin"), TemporalType.DATE );
        }
        if(nmap.get("tipoFormacion") != null){
            q.setParameter("tipoFormacion", (Long) nmap.get("tipoFormacion") );
        }
        if(nmap.get("local") != null){
            q.setParameter("local", (Long) nmap.get("local") );
        }
        if(nmap.get("estadoCurso") != null){
            q.setParameter("estadoCurso", (Long) nmap.get("estadoCurso") );
        }
        if(nmap.get("tipoServicio") != null){
            q.setParameter("tipoServicio", (Long) nmap.get("tipoServicio") );
        }
        q.getResultList();
        return q.getResultList();
    }
    
    public List<SspRegistroCurso> buscarCursosBandejaInstructor(HashMap nmap) {
        String jpql = "select distinct s "
                      + " from SspRegistroCurso s "
                      + " left join Expediente ex on s.nroExpediente = ex.numero and ex.idProceso.idProceso in ("+nmap.get("nroProceso").toString()+")"
                      + " left join s.sspAlumnoCursoList a "
                      + " left join s.sspProgramacionList p "
                      + " left join s.sspInstructorModuloList i "
                      + " where s.activo = 1 and p.activo = 1 and i.activo = 1 " 
                      + " and i.instructorId.personaId.id = :instructor ";
        
        if(nmap.get("tipoEstado") != null){
            switch(Integer.parseInt(nmap.get("tipoEstado").toString())){
                case 1: // POR CONFIRMAR
                        jpql += " and s.estadoId.codProg in ('TP_ECC_CRE','TP_ECC_OBS','TP_ECC_TRA','TP_ECC_EVA') and p.instructorId is null ";
                        break;
                case 2: // CONFIRMADO
                        jpql += " and s.estadoId.codProg in ('TP_ECC_CRE','TP_ECC_OBS','TP_ECC_TRA','TP_ECC_EVA') and p.instructorId.id is not null ";
                        break;
                case 3: // SIN NOTA
                        jpql += " and s.estadoId.codProg = 'TP_ECC_EVA' ";
                        break;
                case 4: // CON NOTA
                        jpql += " and s.estadoId.codProg in ('TP_ECC_ACT', 'TP_ECC_FIN') ";
                        break;
            }
        }
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1:
                        jpql += " and s.id = :filtro";
                        break;
                case 2:
                        jpql += " and s.nroExpediente = :filtro";
                        break;
                case 3:
                        jpql += " and ( a.personaId.numDoc = :filtro )";
                        break;
            }
        }
        if(nmap.get("fechaIni") != null){
            jpql += " and FUNC('trunc',s.fechaRegistro) >= FUNC('trunc', :fechaIni ) ";
        }
        if(nmap.get("fechaFin") != null){
            jpql += " and FUNC('TRUNC',s.fechaRegistro) <= FUNC('TRUNC', :fechaFin ) ";
        }
        if(nmap.get("tipoFormacion") != null){
            jpql += " and p.moduloId.tipoCursoId.id = :tipoFormacion ";
        }
        if(nmap.get("local") != null){
            jpql += " and p.localId.id = :local ";
        }
        jpql += " order by s.id desc";
        
        Query q = em.createQuery(jpql);
        q.setParameter("instructor", (Long) nmap.get("instructor") );
        
        if(nmap.get("buscarPor") != null){
            switch(Integer.parseInt(nmap.get("buscarPor").toString())){
                case 1:                
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("filtro")) );
                        break;
                case 2:
                case 3:
                case 4:
                        q.setParameter("filtro", ""+ nmap.get("filtro") );
                        break;
            }
        }
        if(nmap.get("fechaIni") != null){
            q.setParameter("fechaIni", (Date) nmap.get("fechaIni"), TemporalType.DATE );
        }
        if(nmap.get("fechaFin") != null){
            q.setParameter("fechaFin", (Date) nmap.get("fechaFin"), TemporalType.DATE );
        }
        if(nmap.get("tipoFormacion") != null){
            q.setParameter("tipoFormacion", (Long) nmap.get("tipoFormacion") );
        }
        if(nmap.get("local") != null){
            q.setParameter("local", (Long) nmap.get("local") );
        }
        return q.getResultList();
    }
    
    public int obtenerTotalCursosVigentesXPersonaId(Long vigilanteId) {
        int cont = 0;
        String jpql = "SELECT COUNT(c) "
                    + " FROM SspRegistroCurso c "
                    + " LEFT JOIN c.sspAlumnoCursoList a "
                    + " WHERE c.activo = 1 and a.activo = 1 and a.estadoFinal = 1 "
                    + " and c.tipoOpeId.codProg != 'TP_REGIST_NULL' "
                    + " and a.personaId.id = :vigilanteId "
                    + " and a.fechaFin >= current_date " 
                    + " and c.estadoId.codProg = 'TP_ECC_FIN' ";
                    
        Query q = em.createQuery(jpql);
        q.setParameter("vigilanteId", vigilanteId);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int obtenerTotalCursosVigentesXPersonaIdXTipoCurso(Long vigilanteId, String tipoCurso) {
        int cont = 0;
        String jpql = "SELECT COUNT(c) "
                    + " FROM SspRegistroCurso c "
                    + " LEFT JOIN c.sspAlumnoCursoList a "
                    + " WHERE c.activo = 1 and a.activo = 1 and a.estadoFinal = 1 "
                    + " and c.tipoOpeId.codProg != 'TP_REGIST_NULL' "
                    + " and a.personaId.id = :vigilanteId "
                    + " and a.fechaFin >= current_date " 
                    + " and c.tipoCurso.codProg in ("+tipoCurso+") "
                    + " and c.estadoId.codProg = 'TP_ECC_FIN' ";
                    
        Query q = em.createQuery(jpql);
        q.setParameter("vigilanteId", vigilanteId);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int obtenerTotalCursosObservados(String doc) {
        int cont = 0;
        String jpql = "SELECT COUNT(c) "
                    + " FROM SspRegistroCurso c "
                    + " WHERE c.activo = 1 "
                    + " and (c.administradoId.ruc = :doc or c.administradoId.numDoc = :doc) "
                    + " and c.estadoId.codProg = 'TP_ECC_OBS' ";
                    
        Query q = em.createQuery(jpql);
        q.setParameter("doc", doc);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public List<Map> listarCursosVigilantesIntegrado(String numdoc) {
        try {
            String jpql = "SELECT DISTINCT c.id, c.administradoId.ruc AS RUC, c.administradoId.rznSocial as RZN_SOC, (CASE WHEN (a.estadoFinal = 1) THEN 'APROBADO' ELSE 'DESAPROBADO' END) AS EVALUACION, "
                        + " p.moduloId.tipoCursoId.nombre AS TIPO, a.fechaInicio AS FEC_FINAL, a.fechaFin AS FEC_VENC, "
                        + " (CASE WHEN (a.fechaFin >= current_date) THEN 'VIGENTE' ELSE 'VENCIDA' END) AS VIGENTE "
                        + " FROM SspRegistroCurso c "
                        + " LEFT JOIN c.sspAlumnoCursoList a "
                        + " LEFT JOIN c.sspProgramacionList p "
                        + " WHERE c.activo = 1 and a.activo = 1 and p.activo = 1 and c.estadoId.codProg in ('TP_ECC_FIN')  "
                        + " and a.personaId.numDoc = :numdoc and p.moduloId.codModulo not in ('REFB','REFP') "
                        + " and a.tipoOpeId.codProg = 'TP_OPE_INI' "
                        + " ORDER BY a.fechaInicio DESC ";
            Query q = em.createQuery(jpql);
            q.setParameter("numdoc", JsfUtil.nullATodo(numdoc));
            q.setHint("eclipselink.result-type", "Map");
            if(!q.getResultList().isEmpty()){
                return q.getResultList();    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Map> listarInfVigilantesSoloCursosIntegrado(String numDoc) {
        String jpql =  "SELECT DISTINCT 2 AS TIPO, NULL AS ID," +
                        " P.APE_PAT||' '||P.APE_MAT||' '||P.NOMBRES AS NOMBRE, "+
                        " NULL AS EMPRESA, NULL AS RUC, NULL AS NRO_CRN_VIG, NULL AS NRO_EXP, NULL AS ANO_EXP, NULL AS FEC_EMI, NULL AS FEC_VENC, "+
                        " TB.NOMBRE AS DES_USR, P.NUM_DOC AS COD_USR, " +
                        " NULL AS ESTADO, NULL AS NOM_DPTO, NULL AS DES_MOD, NULL AS EST_CAR, " +
                        " P.APE_PAT, P.APE_MAT, P.NOMBRES AS NOMBRE_VIG, " +
                        " NULL AS HASH_QR, " +
                        " P.NUM_DOC AS CRNT_EXT, " +
                        " (CASE WHEN TB.COD_PROG = 'TP_DOCID_DNI' THEN 2 WHEN length(P.NUM_DOC) = 8 THEN 2 ELSE 5 END) as TIP_USR, " +
                        " 'C' AS TIPO_REG,  NULL AS FECHA_FIN  " +
                        " FROM BDINTEGRADO.SSP_REGISTRO_CURSO C "+
                        " INNER JOIN BDINTEGRADO.SSP_ALUMNO_CURSO A ON A.REGISTRO_CURSO_ID = C.ID " +
                        " INNER JOIN BDINTEGRADO.SB_PERSONA P ON P.ID = A.PERSONA_ID " +
                        " INNER JOIN BDINTEGRADO.TIPO_BASE TOPE ON TOPE.ID = A.TIPO_OPE_ID " +
                        " LEFT JOIN BDINTEGRADO.TIPO_BASE TB ON TB.ID = P.TIPO_DOC  " +                        
                        " WHERE C.ACTIVO = 1 AND A.ACTIVO = 1 AND TOPE.COD_PROG = 'TP_OPE_INI'  "+
                        "  AND ADD_MONTHS(A.FECHA_FIN,24) >= TRUNC(CURRENT_DATE) ";
        
        if (numDoc != null) {
            jpql += " and P.NUM_DOC = ?1 ";
        }
        jpql += " ORDER BY P.APE_PAT DESC  ";

        Query q = em.createNativeQuery(jpql);
        if (numDoc != null) {
            q.setParameter(1, numDoc);
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<SspRegistroCurso> obtenerTotalCursosPendientes(String doc) {
        int cont = 0;
        String jpql = "SELECT distinct c "
                    + " FROM SspRegistroCurso c "
                    + " left join c.sspAlumnoCursoList a "
                    + " WHERE c.activo = 1 and a.activo = 1 "
                    + " and a.personaId.numDoc = :doc "
                    + " and c.estadoId.codProg IN ('TP_ECC_CRE','TP_ECC_OBS','TP_ECC_TRA', 'TP_ECC_EVA', 'TP_ECC_ACT') ";
                    
        Query q = em.createQuery(jpql);
        q.setParameter("doc", doc);
        
        return q.getResultList();
    }
}
