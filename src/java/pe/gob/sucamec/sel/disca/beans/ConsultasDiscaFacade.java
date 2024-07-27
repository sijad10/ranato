/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.disca.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.bdintegrado.bean.AmaMaestroArmasFacade;
import pe.gob.sucamec.renagi.jsf.util.JsfUtil;

/**
 *
 * @author Renato
 */
@Stateless
public class ConsultasDiscaFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager emSucamec;

    private static final int MAX_RES = 20000;

    @EJB
    private AmaMaestroArmasFacade ejbAmaMaestroArmasFacade;
    
    public String nullATodo(String s) {
        return nullATodo(s, false);
    }

    public String nullATodo(String s, boolean parcial) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        if (parcial) {
            return "%" + s.replace("%", "") + "%";
        }
        return s.replace("%", "");
    }

    public String nullATodoComodin(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s + "%";
    }

    public List<HashMap> listarArmasDeFuego(String licencia, String serie, String portador, String doc, String cip) {
        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                + "WHERE NRO_LIC =  ?1 AND NRO_SERIE LIKE ?2 AND ( DOC_PORTADOR LIKE ?3 or DOC_PROPIETARIO LIKE ?3 ) AND (DOC_PROPIETARIO = ?4 OR DOC_PROPIETARIO = ?5) "
                + "ORDER BY NRO_LIC"
        );
        q.setParameter(1, licencia == null ? "" : licencia);
        q.setParameter(2, nullATodo(serie));
        q.setParameter(3, nullATodo(portador));
        q.setParameter(4, doc);
        q.setParameter(5, nullATodo(cip));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarArmasDeFuegoTipo(String dato, String tipo, String docUsr, String cipUsr) {
        String campo = "";
        String filtroUsr = "('" + docUsr + "'";
        if (cipUsr != null && !cipUsr.isEmpty()) {
            filtroUsr += ", '" + cipUsr + "'";
        }
        filtroUsr += ")";

        if (tipo != null && !tipo.isEmpty()) {
            switch (tipo) {
                case "licencia":
                    campo = "WHERE NRO_LIC = ?1";
                    break;
                case "serie":
                    campo = "WHERE NRO_SERIE = ?1";
                    break;
                case "portador":
                    campo = "WHERE DOC_PORTADOR = ?1";
                    break;
            }
            campo += " AND DOC_PROPIETARIO IN" + filtroUsr;
        } else {
            campo = "WHERE DOC_PROPIETARIO IN" + filtroUsr;
        }

        String sql = "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                + campo
                + " ORDER BY NRO_LIC DESC";
        Query q = em.createNativeQuery(sql);
        if (tipo != null && !tipo.isEmpty()) {
            switch (tipo) {
                case "licencia":
                    try {
                        q.setParameter(1, Integer.valueOf(dato.trim()));
                        break;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                case "serie":
                case "portador":
                    q.setParameter(1, dato == null ? "" : dato);
                    break;
            }
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    /**
     * CONSULTA MIGRACIÓN ARMA
     * 
     * @param dato
     * @param tipo
     * @param docUsr
     * @param cipUsr
     * @param personaId
     * @return 
     */
    public List<HashMap> listarArmasDeFuegoTipoMigra(String dato, String tipo, String docUsr, String cipUsr, Long personaId, String nombreAdministrado) {
        
        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_GENERAL() + ") DF ";        
        String campo = "";
        String filtroUsr = "('" + docUsr + "'";
        if (cipUsr != null && !cipUsr.isEmpty()) {
            filtroUsr += ", '" + cipUsr + "'";
        }
        filtroUsr += ")";

        if (tipo != null && !tipo.isEmpty()) {
            switch (tipo) {
                case "licencia":
                    campo = "WHERE DF.LICENCIA_DISCA = ?1";
                    break;
                case "serie":
                    campo = "WHERE DF.SERIE = ?1";
                    break;
                case "portador":
                    campo = "WHERE DOC_PORTADOR = ?1";
                    break;
            }
            campo += " AND (DF.DOC_PROPIETARIO IN" + filtroUsr;
        } else {
            campo = "WHERE (DF.DOC_PROPIETARIO IN" + filtroUsr;
        }
    
        if (nombreAdministrado != null) {
            campo += " AND DF.PROPIETARIO = '" + nombreAdministrado + "') ";   
        } else {
            campo += ")";   
        }
    
        if (personaId != null) {
            campo += " OR (DF.PERSONA_ID =" + personaId + ") ";            
        }

        String sql = "SELECT "
                + " DF.LICENCIA_DISCA NRO_LIC,"
                + " DF.NRO_RUA,"
                + " DF.TIPO_PROPIETARIO,"
                + " DF.DOC_PROPIETARIO,"
                + " DF.PROPIETARIO,"
                + " DF.SERIE NRO_SERIE,"
                + " DF.TIPO_ARMA,"
                + " DF.MARCA,"
                + " DF.MODELO,"
                + " DF.CALIBRE,"
                + " DF.FECHA_EMISION FEC_EMISION,"
                + " DF.FECHA_VEN_DISCA FEC_VENCIMIENTO,"
                + " DF.ESTADO_ARMA ESTADO,"
                + " DF.SITUACION_ARMA SITUACION,"
                + " DF.SISTEMA"
                + " FROM " + sqlMigra 
                + campo
                + " ORDER BY DF.LICENCIA_DISCA DESC";
        //sout(sql);
        Query q = em.createNativeQuery(sql);
        if (tipo != null && !tipo.isEmpty()) {
            switch (tipo) {
                case "licencia":
                    try {
                        q.setParameter(1, Integer.valueOf(dato.trim()));
                        break;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                case "serie":
                case "portador":
                    q.setParameter(1, dato == null ? "" : dato);
                    break;
            }
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    /**
     * Selecciona una foto del DISCA
     *
     * @param codUsr Número de documento de la persona
     * @param vigilante
     * @return Arreglo de bytes con la información de la imagen
     */
    public byte[] selectFoto(String codUsr, boolean vigilante) {
        Query q;
        List<byte[]> r;
        if (vigilante) {
            q = em.createNativeQuery("SELECT FOTO FROM RMA1369.SS_VIG_FOTO@SUCAMEC WHERE COD_USR = '" + codUsr + "'");
            r = q.getResultList();
            if (r.size() == 1) {
                return r.get(0);
            }
        } else {
            q = em.createNativeQuery("SELECT FOTO FROM RMA1369.SG_FOTO@SUCAMEC WHERE COD_USR = '" + codUsr + "'");
            r = q.getResultList();
            if (r.size() == 1) {
                return r.get(0);
            }
        }
        return null;
    }

    public List<Map> listarTotVigilantes(String numdoc, String carne) {
        try {
        //        String sql = "SELECT 1 AS TIPO, WV.*,'V' AS TIPO_REG FROM RMA1369.WS_VIGILANTES@SUCAMEC WV "
        //                + "WHERE (1=1) ";
                String sql = " SELECT 1 AS TIPO, 'V' AS TIPO_REG, V.ROWID || PN.ROWID AS ID, " +
                            "          (CASE WHEN P.APE_PAT IS NOT NULL THEN (P.APE_PAT || ' ' || P.APE_MAT || ' ' || P.NOMBRES) WHEN V.TIP_USR = 2 THEN (PN.APE_PAT || ' ' || PN.APE_MAT || ' ' || PN.NOMBRE) ELSE (EXT.APE_PAT || ' ' || EXT.APE_MAT || ' ' || EXT.NOMBRE) END) AS NOMBRE, " +
                            "          E.RZN_SOC AS EMPRESA,\n" +
                            "          V.RUC,\n" +
                            "          V.NRO_CRN_VIG,\n" +
                            "          V.NRO_EXP,\n" +
                            "          V.ANO_EXP,\n" +
                            "          V.FEC_EMI,\n" +
                            "          V.FEC_VENC,\n" +
                            "          TU.DES_USR,\n" +
                            "          CASE\n" +
                            "             WHEN V.TIP_USR = 2\n" +
                            "             THEN\n" +
                            "                TRIM (TO_CHAR (V.COD_USR, '00000000'))\n" +
                            "             WHEN V.TIP_USR = 5\n" +
                            "             THEN\n" +
                            "                TRIM (TO_CHAR (EXT.CRNT_EXT, '000000000'))\n" +
                            "          END\n" +
                            "             AS COD_USR,\n" +
                            "          CASE\n" +
                            "             WHEN V.FEC_VENC >= TRUNC (SYSDATE) THEN 'VIGENTE'\n" +
                            "             ELSE 'VENCIDO'\n" +
                            "          END\n" +
                            "             AS ESTADO,\n" +
                            "          DEP.NOM_DPTO,\n" +
                            "          MODA.DES_MOD,\n" +
                            "          V.EST_CAR,          \n" +
                            "          (CASE WHEN P.APE_PAT IS NOT NULL THEN P.APE_PAT WHEN V.TIP_USR = 2 THEN PN.APE_PAT ELSE EXT.APE_PAT END) AS APE_PAT,\n" +
                            "          (CASE WHEN P.APE_MAT IS NOT NULL THEN P.APE_MAT WHEN V.TIP_USR = 2 THEN PN.APE_MAT ELSE EXT.APE_MAT END) AS APE_MAT,\n" +
                            "          (CASE WHEN P.NOMBRES IS NOT NULL THEN P.NOMBRES WHEN V.TIP_USR = 2 THEN PN.NOMBRE ELSE EXT.NOMBRE END) AS NOMBRE_VIG,\n" +
                            "          V.HASH_QR,\n" +
                            "          CRNT_EXT,\n" +
                            "          V.TIP_USR\n" +
                            "     FROM RMA1369.SS_EMP_VIG@SUCAMEC V          \n" +
                            "          LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC PN ON V.COD_USR = PN.COD_USR\n" +
                            "          LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC EXT ON V.COD_USR = EXT.COD_USR\n" +
                            "          LEFT JOIN BDINTEGRADO.SB_PERSONA P ON P.NUM_DOC = TRIM(CASE WHEN V.TIP_USR = 2 THEN TO_CHAR (V.COD_USR, '00000000') ELSE TO_CHAR (V.COD_USR, '000000000') END)              \n" +
                            "          LEFT JOIN RMA1369.EMPRESA@SUCAMEC E ON V.RUC = E.RUC\n" +
                            "          LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC DEP ON V.COD_DPTO = DEP.COD_DPTO\n" +
                            "          LEFT JOIN RMA1369.MOD_EMPR@SUCAMEC MODA ON V.TIP_MOD = MODA.TIP_MOD\n" +
                            "          LEFT JOIN RMA1369.TIP_USR@SUCAMEC TU ON V.TIP_USR = TU.TIP_USR\n" +
                            "    WHERE  \n" +
                            "         MODA.AREA_MOD = 2 ";

                if (numdoc != null && !numdoc.isEmpty()) {
                    sql = sql + " AND ( V.COD_USR = ?1 ) ";
                }
                if (carne != null && !carne.isEmpty()) {
                    sql = sql + " AND V.NRO_CRN_VIG = ?2 ";
                }

                Query q = em.createNativeQuery(sql);
                if (numdoc != null && !numdoc.isEmpty()) {
                    q.setParameter(1, Long.parseLong(numdoc));
                }
                if (carne != null && !carne.isEmpty()) {
                    q.setParameter(2, Long.parseLong(carne));
                }
                q.setHint("eclipselink.result-type", "Map");
                return q.setMaxResults(MAX_RES).getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HashMap> listarVigilantes(String numdoc, String carne, String estado, String ruc) {
        try {
            //String sql = "SELECT 1 AS TIPO, A.* FROM RMA1369.WS_VIGILANTES@SUCAMEC A "
            //        + " WHERE COD_USR LIKE ?1 AND NRO_CRN_VIG LIKE ?2 AND ESTADO LIKE ?3 AND RUC = ?4  ";
            String sql = "  SELECT 1 AS TIPO, V.ROWID || PN.ROWID AS ID, \n" +
                        "          (CASE WHEN P.APE_PAT IS NOT NULL THEN (P.APE_PAT || ' ' || P.APE_MAT || ' ' || P.NOMBRES) WHEN V.TIP_USR = 2 THEN (PN.APE_PAT || ' ' || PN.APE_MAT || ' ' || PN.NOMBRE) ELSE (EXT.APE_PAT || ' ' || EXT.APE_MAT || ' ' || EXT.NOMBRE) END) AS NOMBRE,\n" +
                        "          E.RZN_SOC AS EMPRESA,\n" +
                        "          V.RUC,\n" +
                        "          V.NRO_CRN_VIG,\n" +
                        "          V.NRO_EXP,\n" +
                        "          V.ANO_EXP,\n" +
                        "          V.FEC_EMI,\n" +
                        "          V.FEC_VENC,\n" +
                        "          TU.DES_USR,\n" +
                        "          CASE\n" +
                        "             WHEN V.TIP_USR = 2\n" +
                        "             THEN\n" +
                        "                TRIM (TO_CHAR (V.COD_USR, '00000000'))\n" +
                        "             WHEN V.TIP_USR = 5\n" +
                        "             THEN\n" +
                        "                TRIM (TO_CHAR (EXT.CRNT_EXT, '000000000'))\n" +
                        "          END\n" +
                        "             AS COD_USR,\n" +
                        "          CASE\n" +
                        "             WHEN V.FEC_VENC >= TRUNC (SYSDATE) THEN 'VIGENTE'\n" +
                        "             ELSE 'VENCIDO'\n" +
                        "          END\n" +
                        "             AS ESTADO,\n" +
                        "          DEP.NOM_DPTO,\n" +
                        "          MODA.DES_MOD,\n" +
                        "          V.EST_CAR,          \n" +
                        "          (CASE WHEN P.APE_PAT IS NOT NULL THEN P.APE_PAT WHEN V.TIP_USR = 2 THEN PN.APE_PAT ELSE EXT.APE_PAT END) AS APE_PAT,\n" +
                        "          (CASE WHEN P.APE_MAT IS NOT NULL THEN P.APE_MAT WHEN V.TIP_USR = 2 THEN PN.APE_MAT ELSE EXT.APE_MAT END) AS APE_MAT,\n" +
                        "          (CASE WHEN P.NOMBRES IS NOT NULL THEN P.NOMBRES WHEN V.TIP_USR = 2 THEN PN.NOMBRE ELSE EXT.NOMBRE END) AS NOMBRE_VIG,\n" +
                        "          V.HASH_QR,\n" +
                        "          CRNT_EXT,\n" +
                        "          V.TIP_USR\n" +
                        "     FROM RMA1369.SS_EMP_VIG@SUCAMEC V\n" +
                        "          LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC PN ON V.COD_USR = PN.COD_USR\n" +
                        "          LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC EXT ON V.COD_USR = EXT.COD_USR\n" +
                        "          LEFT JOIN BDINTEGRADO.SB_PERSONA P ON P.NUM_DOC = TRIM(CASE WHEN V.TIP_USR = 2 THEN TO_CHAR(V.COD_USR, '00000000') ELSE TO_CHAR (V.COD_USR, '000000000') END)  \n" +
                        "          LEFT JOIN RMA1369.EMPRESA@SUCAMEC E ON V.RUC = E.RUC\n" +
                        "          LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC DEP ON V.COD_DPTO = DEP.COD_DPTO\n" +
                        "          LEFT JOIN RMA1369.MOD_EMPR@SUCAMEC MODA ON V.TIP_MOD = MODA.TIP_MOD\n" +
                        "          LEFT JOIN RMA1369.TIP_USR@SUCAMEC TU ON V.TIP_USR = TU.TIP_USR\n" +
                        "    WHERE  \n" +
                        "         MODA.AREA_MOD = 2 " +
                        " AND RUC = ?4 ";
            if(numdoc != null && !numdoc.isEmpty()){
                sql += " AND V.COD_USR = ?1 ";
            }
            if(carne != null && !carne.isEmpty()){
                sql += "  AND NRO_CRN_VIG = ?2";
            }
            if(estado != null && !estado.isEmpty()){
                if(estado.equals("VIGENTE")){
                    sql += " AND V.FEC_VENC >= TRUNC (SYSDATE) ";
                }else{
                    sql += " AND V.FEC_VENC < TRUNC (SYSDATE) ";
                }
            }
            Query q = em.createNativeQuery(sql);
            q.setParameter(4, nullATodo(ruc));
            if(numdoc != null && !numdoc.isEmpty()){
                q.setParameter(1, nullATodo(numdoc));
            }
            if(carne != null && !carne.isEmpty()){
                q.setParameter(2, nullATodo(carne));    
            }
            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HashMap> obtenerVigilante(String numdoc, String estado) {
        String sql = "SELECT 1 AS TIPO, A.* FROM RMA1369.WS_VIGILANTES@SUCAMEC A "
                + " WHERE (COD_USR LIKE ?1 OR RUC LIKE ?1) AND ESTADO LIKE ?2 ";//RUC = ?1 AND 

        Query q = em.createNativeQuery(sql);
//        q.setParameter(1, nullATodo(ruc));
        q.setParameter(1, nullATodo(numdoc));
        q.setParameter(2, nullATodo(estado));
        q.setHint("eclipselink.result-type", "Map");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return null;
    }

    public List<HashMap> listarHistVigilantes(String numdoc) {
        Query q = em.createNativeQuery(
                "SELECT E.RUC, E.RZN_SOC, ME.DES_MOD, FEC_EMI, FEC_VENC, FEC_BAJA FROM RMA1369.SS_EMP_VIG_HIS@SUCAMEC VH "
                + "INNER JOIN RMA1369.MOD_EMPR@SUCAMEC ME ON VH.TIP_MOD=ME.TIP_MOD AND ME.AREA_MOD = 2 "
                + "INNER JOIN RMA1369.EMPRESA@SUCAMEC E ON E.RUC = VH.RUC "
                + "WHERE COD_USR = ?1 ORDER BY FEC_EMI DESC"
        );
        q.setParameter(1, nullATodo(numdoc));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<Map> listarCursosVigilantes(String numdoc) {
        Query q = em.createNativeQuery(
                "SELECT  E.RUC, E.RZN_SOC, CASE WHEN EVALUACION = 1 THEN 'APROBADO' ELSE 'DESAPROBADO' END AS EVALUACION, "
                + "CASE WHEN TIP_INST = 1 THEN 'ENTRENAMIENTO' WHEN TIP_INST=2 THEN 'REENTRENAMIENTO' WHEN TIP_INST=3 THEN 'INSTRUCCION' WHEN TIP_INST=4 "
                + "THEN 'CAPACITACION' ELSE 'PRACTICA DE TIRO' END AS TIPO, "
                + "FEC_FINAL, ADD_MONTHS(A1.FEC_FINAL, CASE WHEN A1.FEC_INICIO >= TO_DATE('03/08/2015', 'DD/MM/YYYY') THEN 24 ELSE 12 END) AS FEC_VENC, "
                + "CASE WHEN ADD_MONTHS(A1.FEC_FINAL, CASE WHEN A1.FEC_INICIO >= TO_DATE('03/08/2015', 'DD/MM/YYYY') THEN 24 ELSE 12 END) >= TRUNC(SYSDATE) "
                + "THEN 'VIGENTE' ELSE 'VENCIDA' END AS VIGENTE "
                + "FROM RMA1369.SS_CAPAC@SUCAMEC A1, RMA1369.SS_CAPAC_VIG@SUCAMEC B1, RMA1369.EMPRESA@SUCAMEC E "
                + "WHERE "
                + "E.RUC = A1.RUC AND A1.NRO_EXP = B1.NRO_EXP AND A1.ANO_EXP = B1.ANO_EXP AND B1.TIP_INST IN(2, 3, 4, 5) "
                + "AND B1.COD_USR = ?1 AND B1.TIP_USR = 2 ORDER BY FEC_VENC DESC"
        );
        q.setParameter(1, nullATodo(numdoc));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarEmpresasAutorizadas(String filtro) {
        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE "
                + "RZN_SOC LIKE ?1 OR RUC LIKE ?2 OR NRO_RD LIKE ?3"
        );
        q.setParameter(1, nullATodo(filtro, true));
        q.setParameter(2, nullATodo(filtro));
        q.setParameter(3, nullATodo(filtro, true));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<ArrayRecord> listarInstructoresAutorizados(String filtro, String curso) {
        try {
            String sql = "SELECT * FROM RMA1369.WS_CURSOS_INSTRUCTOR_GSSP@SUCAMEC " +
                         " WHERE CODCURSO LIKE ?1 ";
            try {
                if(filtro != null && !filtro.isEmpty()){
                    long numero = Long.parseLong(filtro);
                    sql += " AND DNI = ?2 ";
                }
            } catch (Exception e) {
                sql += " AND NOMBRES LIKE ?3 ";
            }
            sql += " ORDER BY NOMBRES ";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, nullATodo(curso));
            q.setParameter(2, filtro);
            q.setParameter(3, nullATodo(filtro, true));
            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ArrayRecord> listarCursos() {
        Query q = em.createNativeQuery(
                "select * from rma1369.ss_curso@SUCAMEC where cod_cur > 11"
        );
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<Map> listarCursosVigilantesByTipoDoc(String numdoc, String tipoDoc) {
        Query q = em.createNativeQuery(
                "SELECT  E.RUC, E.RZN_SOC, CASE WHEN EVALUACION = 1 THEN 'APROBADO' ELSE 'DESAPROBADO' END AS EVALUACION, "
                + "CASE WHEN TIP_INST = 1 THEN 'ENTRENAMIENTO' WHEN TIP_INST=2 THEN 'REENTRENAMIENTO' WHEN TIP_INST=3 THEN 'INSTRUCCION' WHEN TIP_INST=4 "
                + "THEN 'CAPACITACION' ELSE 'PRACTICA DE TIRO' END AS TIPO, "
                + "FEC_FINAL, ADD_MONTHS(A1.FEC_FINAL, CASE WHEN A1.FEC_INICIO >= TO_DATE('03/08/2015', 'DD/MM/YYYY') THEN 24 ELSE 12 END) AS FEC_VENC, "
                + "CASE WHEN ADD_MONTHS(A1.FEC_FINAL, CASE WHEN A1.FEC_INICIO >= TO_DATE('03/08/2015', 'DD/MM/YYYY') THEN 24 ELSE 12 END) >= TRUNC(SYSDATE) "
                + "THEN 'VIGENTE' ELSE 'VENCIDA' END AS VIGENTE "
                + " FROM RMA1369.SS_CAPAC@SUCAMEC A1 "
                + " INNER JOIN RMA1369.SS_CAPAC_VIG@SUCAMEC B1 ON A1.NRO_EXP = B1.NRO_EXP AND A1.ANO_EXP = B1.ANO_EXP "
                + " INNER JOIN RMA1369.EMPRESA@SUCAMEC E ON E.RUC = A1.RUC "
                + " LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC EXT ON B1.COD_USR = EXT.COD_USR "
                + " WHERE  B1.TIP_INST IN(2, 3, 4, 5) AND B1.TIP_USR IN (2,5) "
                + " AND " + (tipoDoc.equals("5") ? " EXT.CRNT_EXT LIKE ?1 " : " B1.COD_USR = ?1 ") + " ORDER BY FEC_VENC DESC"
        );
        q.setParameter(1, nullATodo(numdoc));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<Map> listarVigilantesHistorico(String numdoc, String carne, String carnesNotIn) {
        String sql = "";
        String subQuery = "SELECT V.NRO_CRN_VIG, \n" +
                    "           MAX(V.FEC_VENC) AS FEC_VENC \n " +
                    "     FROM RMA1369.SS_EMP_VIG_HIS@SUCAMEC V\n" +
                    "          LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC PN ON V.COD_USR = PN.COD_USR\n" +
                    "          LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC EXT ON V.COD_USR = EXT.COD_USR\n" +
                    "          LEFT JOIN BDINTEGRADO.SB_PERSONA P ON P.NUM_DOC = TRIM(CASE WHEN V.TIP_USR = 2 THEN TO_CHAR (V.COD_USR, '00000000') ELSE TO_CHAR (V.COD_USR, '000000000') END)\n" +
                    "          LEFT JOIN RMA1369.EMPRESA@SUCAMEC E ON V.RUC = E.RUC\n" +
                    "          LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC DEP ON V.COD_DPTO = DEP.COD_DPTO\n" +
                    "          LEFT JOIN RMA1369.MOD_EMPR@SUCAMEC MODA ON V.TIP_MOD = MODA.TIP_MOD\n" +
                    "          LEFT JOIN RMA1369.TIP_USR@SUCAMEC TU ON V.TIP_USR = TU.TIP_USR\n" +
                    "    WHERE MODA.AREA_MOD = 2 " +
                     (!carnesNotIn.isEmpty() ? " AND v.NRO_CRN_VIG NOT IN (" + carnesNotIn + ")" : "");

        if (numdoc != null && !numdoc.isEmpty()) {
            subQuery += " AND (V.COD_USR = ?1 )";
        }
        if (carne != null && !carne.isEmpty()) {
            subQuery += " AND V.NRO_CRN_VIG = ?2";
        }
        subQuery += " GROUP BY V.NRO_CRN_VIG ";

        sql = " SELECT 1 AS TIPO, 'H' AS TIPO_REG, V.ROWID AS ID,\n" +
                "         (CASE WHEN P.APE_PAT IS NOT NULL THEN (P.APE_PAT || ' ' || P.APE_MAT || ' ' || P.NOMBRES) WHEN V.TIP_USR = 2 THEN (PN.APE_PAT || ' ' || PN.APE_MAT || ' ' || PN.NOMBRE) ELSE (EXT.APE_PAT || ' ' || EXT.APE_MAT || ' ' || EXT.NOMBRE) END) AS NOMBRE,\n" +
                "          E.RZN_SOC AS EMPRESA,\n" +
                "          V.RUC,\n" +
                "          V.NRO_CRN_VIG,\n" +
                "          V.NRO_EXP,\n" +
                "          V.ANO_EXP,\n" +
                "          V.FEC_EMI,\n" +
                "          V.FEC_VENC,\n" +
                "          TU.DES_USR,\n" +
                "          CASE\n" +
                "             WHEN V.TIP_USR = 2\n" +
                "             THEN\n" +
                "                TRIM (TO_CHAR (V.COD_USR, '00000000'))\n" +
                "             WHEN V.TIP_USR = 5\n" +
                "             THEN\n" +
                "                TRIM (TO_CHAR (EXT.CRNT_EXT, '000000000'))\n" +
                "          END\n" +
                "             AS COD_USR,\n" +
                "          'CESADO' AS ESTADO,\n" +
                "          DEP.NOM_DPTO,\n" +
                "          MODA.DES_MOD,\n" +
                "          V.EST_CAR,          \n" +
                "          (CASE WHEN P.APE_PAT IS NOT NULL THEN P.APE_PAT WHEN V.TIP_USR = 2 THEN PN.APE_PAT ELSE EXT.APE_PAT END) AS APE_PAT,\n" +
                "          (CASE WHEN P.APE_MAT IS NOT NULL THEN P.APE_MAT WHEN V.TIP_USR = 2 THEN PN.APE_MAT ELSE EXT.APE_MAT END) AS APE_MAT,\n" +
                "          (CASE WHEN P.NOMBRES IS NOT NULL THEN P.NOMBRES WHEN V.TIP_USR = 2 THEN PN.NOMBRE ELSE EXT.NOMBRE END) AS NOMBRE_VIG,\n" +
                "          V.HASH_QR,\n" +
                "          CRNT_EXT,\n" +
                "          V.TIP_USR,\n" +
                "          V.FEC_BAJA\n" +
                "     FROM RMA1369.SS_EMP_VIG_HIS@SUCAMEC V\n" +
                "          LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC PN ON V.COD_USR = PN.COD_USR\n" +
                "          LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC EXT ON V.COD_USR = EXT.COD_USR\n" +
                "          LEFT JOIN BDINTEGRADO.SB_PERSONA P ON P.NUM_DOC = TRIM(CASE WHEN V.TIP_USR = 2 THEN TO_CHAR (V.COD_USR, '00000000') ELSE TO_CHAR (V.COD_USR, '000000000') END)\n" +
                "          LEFT JOIN RMA1369.EMPRESA@SUCAMEC E ON V.RUC = E.RUC\n" +
                "          LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC DEP ON V.COD_DPTO = DEP.COD_DPTO\n" +
                "          LEFT JOIN RMA1369.MOD_EMPR@SUCAMEC MODA ON V.TIP_MOD = MODA.TIP_MOD\n" +
                "          LEFT JOIN RMA1369.TIP_USR@SUCAMEC TU ON V.TIP_USR = TU.TIP_USR\n" +
                "    WHERE MODA.AREA_MOD = 2 " +
                " AND (V.NRO_CRN_VIG, V.FEC_VENC) IN (" + subQuery + ") ";

        Query q = em.createNativeQuery(sql);
        if (numdoc != null && !numdoc.isEmpty()) {
            q.setParameter(1, Long.parseLong(numdoc));
        }
        if (carne != null && !carne.isEmpty()) {
            q.setParameter(2, Long.parseLong(carne));
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<Map> listarInfVigilantesSoloCursos(String numdoc, String codUsrNotIn, String crnExtNotIn) {
        String jpql = " SELECT DISTINCT 2 AS TIPO, NULL AS ID, "
                + "        (CASE WHEN B1.TIP_USR = 2 THEN PN.APE_PAT || ' ' || PN.APE_MAT || ' ' || PN.NOMBRE  ELSE EXT.APE_PAT || ' ' || EXT.APE_MAT || ' ' || EXT.NOMBRE  END) AS NOMBRE, "
                + "        NULL AS EMPRESA, "
                + "        NULL AS RUC, "
                + "        NULL AS NRO_CRN_VIG, "
                + "        NULL AS NRO_EXP, "
                + "        NULL AS ANO_EXP, "
                + "        NULL AS FEC_EMI, "
                + "        NULL AS FEC_VENC, "
                + "        TU.DES_USR, "
                + "        TRIM (TO_CHAR (B1.COD_USR, '00000000')) AS COD_USR, "
                + "        NULL AS ESTADO, "
                + "        NULL AS NOM_DPTO, "
                + "        NULL AS DES_MOD, "
                + "        NULL AS EST_CAR, "
                + "        (CASE WHEN B1.TIP_USR = 2 THEN PN.APE_PAT ELSE EXT.APE_PAT END) AS APE_PAT, "
                + "        (CASE WHEN B1.TIP_USR = 2 THEN PN.APE_MAT ELSE EXT.APE_MAT END) AS APE_MAT, "
                + "        (CASE WHEN B1.TIP_USR = 2 THEN PN.NOMBRE ELSE EXT.NOMBRE END) AS NOMBRE_VIG, "
                + "        NULL AS HASH_QR, "
                + "        EXT.CRNT_EXT, "
                + "        B1.TIP_USR, "
                + "        'C' AS TIPO_REG "
                + " FROM RMA1369.SS_CAPAC@SUCAMEC A1 "
                + " INNER JOIN RMA1369.SS_CAPAC_VIG@SUCAMEC B1 ON A1.NRO_EXP = B1.NRO_EXP AND A1.ANO_EXP = B1.ANO_EXP "
                + " INNER JOIN RMA1369.EMPRESA@SUCAMEC E ON E.RUC = A1.RUC "
                + " INNER JOIN RMA1369.TIP_USR@SUCAMEC TU ON B1.TIP_USR = TU.TIP_USR "
                + " LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC PN ON B1.COD_USR = PN.COD_USR "
                + " LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC EXT ON B1.COD_USR = EXT.COD_USR "
                + " WHERE  B1.TIP_INST IN(2, 3, 4, 5) "
                + " AND B1.TIP_USR IN (2,5) "
                + " AND ADD_MONTHS(A1.FEC_FINAL, CASE WHEN A1.FEC_INICIO >= TO_DATE('03/08/2015', 'DD/MM/YYYY') THEN 24 ELSE 12 END) >= TRUNC(SYSDATE) "
                + ((!codUsrNotIn.isEmpty()) ? " AND B1.COD_USR NOT IN (" + codUsrNotIn + ")" : "")
                + ((!crnExtNotIn.isEmpty()) ? " AND EXT.CRNT_EXT NOT IN (" + crnExtNotIn + ")" : "");
        if (numdoc != null) {
            jpql += " AND ( B1.COD_USR = ?1  OR EXT.CRNT_EXT = ?1 ) ";
        }

        Query q = em.createNativeQuery(jpql);
        if (numdoc != null) {
            q.setParameter(1, numdoc);
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarArmasDeFuego(String licencia, String serie,
            String doc_prop, String nom_prop, String doc_por, String nom_portador) {

        String jpql = "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                + "WHERE (1=1) ";

        if (licencia != null) {
            jpql += " AND NRO_LIC =  ?1 ";
        }
        if (serie != null) {
            jpql += " AND NRO_SERIE = ?2 ";
        }
        if (doc_prop != null) {
            jpql += " AND DOC_PROPIETARIO = ?3 ";
        }
        if (nom_prop != null) {
            jpql += " AND PROPIETARIO LIKE ?4 ";
        }
        if (doc_por != null) {
            jpql += " AND (DOC_PORTADOR = ?5 or DOC_PROPIETARIO = ?5 ) ";
        }
        if (nom_portador != null) {
            jpql += " AND PORTADOR LIKE ?6 ";
        }
        jpql += " ORDER BY NRO_LIC ";

        Query q = em.createNativeQuery(jpql);

        if (licencia != null) {
            q.setParameter(1, licencia);
        }
        if (serie != null) {
            q.setParameter(2, serie);
        }
        if (doc_prop != null) {
            q.setParameter(3, doc_prop);
        }
        if (nom_prop != null) {
            q.setParameter(4, nullATodoComodin(nom_prop));
        }
        if (doc_por != null) {
            q.setParameter(5, doc_por);
        }
        if (nom_portador != null) {
            q.setParameter(6, nullATodoComodin(nom_portador));
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public String obtenerCodUsrExtranjero(String numdoc) {
        Query q = em.createNativeQuery(
                "SELECT  EXT.COD_USR FROM RMA1369.EXTRANJERO@SUCAMEC EXT WHERE EXT.CRNT_EXT = ?1 "
        );
        q.setParameter(1, numdoc == null ? "" : numdoc.trim());
        q.setHint("eclipselink.result-type", "Map");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            ArrayRecord ar = (ArrayRecord) q.getResultList().get(0);
            return (String) ar.get("COD_USR").toString();
        }
        return null;
    }

    public List<Map> listarCursosVigilantesIntegrado(String numdoc) {
        String jpql = "SELECT DISTINCT c.administradoId.ruc AS RUC, c.administradoId.rznSocial as RZN_SOC, (CASE WHEN a.estadoFinal = 1 THEN 'APROBADO' ELSE 'DESAPROBADO' end) AS EVALUACION, "
                + " p.moduloId.tipoCursoId.nombre AS TIPO, a.fechaInicio AS FEC_FINAL, a.fechaFin AS FEC_VENC, "
                + " (CASE WHEN a.fechaFin >= current_date THEN 'VIGENTE' ELSE 'VENCIDA' END) AS VIGENTE "
                + " FROM SspRegistroCurso c "
                + " LEFT JOIN c.sspAlumnoCursoList a "
                + " LEFT JOIN c.sspProgramacionList p "
                + " WHERE c.activo = 1 and a.activo = 1 and p.activo = 1 "
                + " and a.personaId.numDoc = :numdoc "
                + " ORDER BY a.fechaFin DESC ";
        Query q = em.createQuery(jpql);
        q.setParameter("numdoc", nullATodo(numdoc));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<ArrayRecord> listarPropietariosxFiltro(String tipo, String filtro) {
        String campo = "";
        String tipo_doc = "";
        if (tipo.equals("dni")) {
            tipo_doc = " 'DNI' TIPO_DOC,";
            campo = "TIPO_PROPIETARIO IN('PERS. NATURAL') AND DOC_PROPIETARIO = ?1 ";
        }
        if (tipo.equals("cip")) {
            tipo_doc = " 'CIP' TIPO_DOC,";
            campo = "TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND DOC_PROPIETARIO LIKE ?1 ";
        }
        if (tipo.equals("ruc")) {
            tipo_doc = " 'RUC' TIPO_DOC,";            
            campo = "TIPO_PROPIETARIO IN('PERS. JURIDICA', 'DELEGACION') AND DOC_PROPIETARIO = ?1 ";
        }
        if (tipo.equals("ce")) {
            tipo_doc = " 'CE' TIPO_DOC,";
            campo = "TIPO_PROPIETARIO IN('EXTRANJERO') AND DOC_PROPIETARIO LIKE ?1 ";
        }
        if (tipo.equals("lic")) {
            tipo_doc = " CASE"
                    + " WHEN TIPO_PROPIETARIO IN('PERS. NATURAL') THEN 'DNI'"
                    + " WHEN TIPO_PROPIETARIO IN('EXTRANJERO') THEN 'CE'"
                    + " WHEN TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                    + " END"
                    + " AS TIPO_DOC, ";
            campo = "TIPO_PROPIETARIO IN('EXTRANJERO', 'PERS. NATURAL', 'FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND DOC_PROPIETARIO = ?1 ";
        }
        if (tipo.equals("ape")) {
            tipo_doc = " CASE"
                    + " WHEN TIPO_PROPIETARIO IN('PERS. NATURAL') THEN 'DNI'"
                    + " WHEN TIPO_PROPIETARIO IN('EXTRANJERO') THEN 'CE'"
                    + " WHEN TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                    + " END"
                    + " AS TIPO_DOC, ";
            campo = "TIPO_PROPIETARIO IN('EXTRANJERO', 'PERS. NATURAL', 'FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND PROPIETARIO LIKE ?1 ";
        }
        
        try {
            String sql = "SELECT"
                    + tipo_doc
                    + " TIPO_PROPIETARIO,"
                    + " DOC_PROPIETARIO,"
                    + " TRIM(PROPIETARIO) PROPIETARIO"
                    + "  "
                    + "FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                    + "WHERE "+ campo 
                    + "GROUP BY TIPO_PROPIETARIO, DOC_PROPIETARIO, TRIM(PROPIETARIO) ";

            Query q = em.createNativeQuery(sql);
            //Syso(sql);
            
            switch(tipo){
                case "dni":
                case "ruc":
                case "lic":
                    q.setParameter(1, nullATodo(filtro.trim().toUpperCase()));
                    break;
                case "cip":
                case "ce":
                case "ape":
                    q.setParameter(1, "%" + filtro.trim().toUpperCase() + "%");
                    break;
            }
            q.setHint("eclipselink.result-type", "Map");
            List<ArrayRecord> list = q.setMaxResults(MAX_RES).getResultList(); 
            
            //* Remover duplicados, La vista devuelve nombre diferentes para el mismo nro de doc *//
            if(list.size()<=20){
                List<ArrayRecord> list1 = list;
                int i;
                for(i=0; i < list1.size(); i++)
                {
                    int x;
                    String valor = list1.get(i).get("DOC_PROPIETARIO").toString();
                    for(x=i; x < list.size(); x++){
                        //So("v: " + i + "-" + x + " : " + valor + "==" + list.get(x).get("DOC_PROPIETARIO").toString());
                        if(x != i){
                            if(valor.equals(list.get(x).get("DOC_PROPIETARIO").toString())){
                                //So("removiendo " + x + ": " + list.get(x).get("DOC_PROPIETARIO").toString());                            
                                list.remove(x);
                            }                        
                        }
                    }
                }                
            }
            /* */
            return list;
        } catch (Exception e) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorEnBusquedaBD"));
            e.printStackTrace();
            return null;
        }
    }

    public List<HashMap> listarPersonaNatural(String filtro) {
        String campo = "";
        campo = "p.COD_USR = ?1 ";
        
        String sql = "SELECT"
                + " p.COD_USR,"
                + " p.NOMBRE,"
                + " p.APE_PAT,"
                + " COALESCE(p.APE_MAT, ' ') APE_MAT,"
                + " COALESCE(p.DOMICILIO, ' ') DOMICILIO,"
                + " COALESCE(p.SEXO, ' ') SEXO,"
                + " p.FEC_NAC,"
                + " COALESCE(d.NOM_DPTO, ' ') NOM_DPTO,"
                + " COALESCE(pr.NOM_PROV,' ') NOM_PROV,"
                + " COALESCE(di.NOM_DIST, ' ') NOM_DIST"
                + " FROM"
                + " RMA1369.PERSONA_NATURAL@SUCAMEC p"
                + " LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC d ON p.COD_DPTO=d.COD_DPTO"
                + " LEFT JOIN RMA1369.PROVINCIA@SUCAMEC pr ON pr.COD_DPTO=d.COD_DPTO AND pr.COD_PROV=p.COD_PROV"
                + " LEFT JOIN RMA1369.DISTRITO@SUCAMEC di ON pr.COD_PROV=di.COD_PROV AND di.COD_DPTO=d.COD_DPTO AND di.COD_DIST=p.COD_DIST"
                + " WHERE "+ campo 
                + "";
        
        Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, filtro.trim());
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarEfectivo(String filtro) {
        String campo = "";
        campo = "p.COD_USR = ?1 ";
        
        String sql = "SELECT"
                + " p.COD_USR,"
                + " p.NOMBRE,"
                + " p.APE_PAT,"
                + " COALESCE(p.APE_MAT, ' ') APE_MAT,"
                + " COALESCE(p.DOMICILIO, ' ') DOMICILIO,"
                + " COALESCE(p.SEXO, ' ') SEXO,"
                + " p.FEC_NAC,"
                + " COALESCE(d.NOM_DPTO, ' ') NOM_DPTO,"
                + " COALESCE(pr.NOM_PROV,' ') NOM_PROV,"
                + " COALESCE(di.NOM_DIST, ' ') NOM_DIST"
                + " FROM"
                + " RMA1369.EFECTIVO@SUCAMEC p"
                + " LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC d ON p.COD_DPTO=d.COD_DPTO"
                + " LEFT JOIN RMA1369.PROVINCIA@SUCAMEC pr ON pr.COD_DPTO=d.COD_DPTO AND pr.COD_PROV=p.COD_PROV"
                + " LEFT JOIN RMA1369.DISTRITO@SUCAMEC di ON pr.COD_PROV=di.COD_PROV AND di.COD_DPTO=d.COD_DPTO AND di.COD_DIST=p.COD_DIST"
                + " WHERE "+ campo 
                + "";
        
        Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, filtro.trim());
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    public List<HashMap> listarExtranjero(String filtro) {
        String campo = "";
        filtro = filtro.replaceFirst("^0*", "");
        //campo = "LTRIM(p.COD_USR, '0')=?1 OR LTRIM(p.CRNT_EXT,'0')=?2";
        campo = "p.COD_USR=?1 OR p.CRNT_EXT=?2";
        
        String sql = "SELECT"
                + " p.COD_USR,"
                + " p.NOMBRE,"
                + " p.APE_PAT,"
                + " COALESCE(p.APE_MAT, ' ') APE_MAT,"
                + " COALESCE(p.DOMICILIO, ' ') DOMICILIO,"
                + " COALESCE(p.SEXO, ' ') SEXO,"
                + " p.FEC_NAC,"
                + " COALESCE(d.NOM_DPTO, ' ') NOM_DPTO,"
                + " COALESCE(pr.NOM_PROV,' ') NOM_PROV,"
                + " COALESCE(di.NOM_DIST, ' ') NOM_DIST"
                + " FROM"
                + " RMA1369.EXTRANJERO@SUCAMEC p"
                + " LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC d ON p.COD_DPTO=d.COD_DPTO"
                + " LEFT JOIN RMA1369.PROVINCIA@SUCAMEC pr ON pr.COD_DPTO=d.COD_DPTO AND pr.COD_PROV=p.COD_PROV"
                + " LEFT JOIN RMA1369.DISTRITO@SUCAMEC di ON pr.COD_PROV=di.COD_PROV AND di.COD_DPTO=d.COD_DPTO AND di.COD_DIST=p.COD_DIST"
                + " WHERE "+ campo 
                + "";
        
        Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, nullATodo(filtro.trim()));
        q.setParameter(2, nullATodo(filtro.trim()));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    public List<HashMap> listarEmpresa( String filtro) {
        String campo = "";
        campo = "p.RUC = ?1 ";
        
        String sql = "SELECT"
                + " p.RUC,"
                + " p.RZN_SOC,"
                + " COALESCE(p.DOM_ACT, ' ') DOMICILIO,"
                + " COALESCE(d.NOM_DPTO, ' ') NOM_DPTO,"
                + " COALESCE(pr.NOM_PROV,' ') NOM_PROV,"
                + " COALESCE(di.NOM_DIST, ' ') NOM_DIST"
                + " FROM"
                + " RMA1369.EMPRESA@SUCAMEC p"
                + " LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC d ON p.COD_DPTO=d.COD_DPTO"
                + " LEFT JOIN RMA1369.PROVINCIA@SUCAMEC pr ON pr.COD_DPTO=d.COD_DPTO AND pr.COD_PROV=p.COD_PROV"
                + " LEFT JOIN RMA1369.DISTRITO@SUCAMEC di ON pr.COD_PROV=di.COD_PROV AND di.COD_DPTO=d.COD_DPTO AND di.COD_DIST=p.COD_DIST"
                + " WHERE "+ campo 
                + "";
        
        Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, filtro.trim());
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarArmasxNroReferencia(String tipo, String filtro, String tipoDoc) {
        String campo = "";
        if (tipo.equals("licencia")) {
            campo = "NRO_LIC = ?1 ";
        }
        if (tipo.equals("serie")) {
            campo = "NRO_SERIE = ?1 ";  
        }
        if (tipo.equals("doc_prop")) {
            switch(tipoDoc){
                case "RUC":
                    //campo = "(TIPO_PROPIETARIO IN('PERS. JURIDICA') AND REGEXP_LIKE(DOC_PROPIETARIO, '[0-9]{2}" +filtro.trim()+"[0-9]{1}')) ";
                    campo = "(TIPO_PROPIETARIO IN('PERS. JURIDICA') AND DOC_PROPIETARIO LIKE '1_" +filtro.trim()+"_' )";
                    break;
                case "CIP":
                    campo = "(TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND DOC_PROPIETARIO = '" +filtro.trim()+"') ";
                    break;
                default:
                    campo = "DOC_PROPIETARIO = ?1 ";
            }
            /*if(tipoDoc.equals("DNI")){
                //campo = "(TIPO_PROPIETARIO IN('PERS. JURIDICA') AND REGEXP_LIKE(DOC_PROPIETARIO, '[0-9]{2}" +filtro.trim()+"[0-9]{1}')) ";
                campo = "(TIPO_PROPIETARIO IN('PERS. JURIDICA') AND DOC_PROPIETARIO LIKE '__" +filtro.trim()+"_') ";
            }else{
                campo = "DOC_PROPIETARIO = ?1 ";
            }*/
        }
        if (tipo.equals("nom_prop")) {
            campo = "PROPIETARIO LIKE ?1 ";
        }
        if (tipo.equals("doc_portador")) {
            campo = "DOC_PORTADOR = ?1 ";
        }
        if (tipo.equals("nom_portador")) {
            campo = "PORTADOR LIKE ?1 ";
        }
        
        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                + "WHERE "+ campo 
                + " AND ID IN(SELECT MIN(ID) FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                + " AND SISTEMA='DISCA'"
                + "ORDER BY NRO_LIC"
        );
        switch (tipo){
            case "licencia":
            case "serie":
            case "doc_prop":
            case "doc_portador":
                q.setParameter(1, filtro.trim().toUpperCase());
                break;
            case "nom_prop":
            case "nom_portador":                
                q.setParameter(1, nullATodoComodin(filtro.toUpperCase()));
                break;
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
        
    public List<ArrayRecord> listarxNumDoc(String tipo, String filtro) {
        String campo = "";
        //campo = "LTRIM(p.COD_USR, '0')=?1 OR LTRIM(p.CRNT_EXT,'0')=?2";        
        switch(tipo){
            case "DNI":
                campo = "TIPO_PROPIETARIO IN('PERS. NATURAL') AND DOC_PROPIETARIO = ?1 ";
                break;
            case "RUC":
                campo = "TIPO_PROPIETARIO IN('PERS. JURIDICA', 'DELEGACION') AND DOC_PROPIETARIO = ?1 ";
                break;
            case "CE":
                filtro = JsfUtil.leftpadString(filtro, 9, "0");
                campo = "TIPO_PROPIETARIO IN('EXTRANJERO') AND LPAD(DOC_PROPIETARIO, 9, '0') = ?1 ";
                break;
            case "CIP":
            case "FAP":
            case "POLICIA":
            case "MARINA":
            case "MILITAR":
            case "EJERCITO":
                campo = "TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND DOC_PROPIETARIO = ?1 ";
                break;
        }
        
        try {
            String sql = "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                    + "WHERE "+ campo 
                    + " AND ID IN(SELECT MIN(ID) FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                    + " AND SISTEMA='DISCA'"
                    + "ORDER BY NRO_LIC"
                    + "";

            Query q = em.createNativeQuery(sql);
            //Syso(sql);
            
            switch(tipo){
                case "DNI":
                case "RUC":
                case "CE":
                case "CIP":
                case "FAP":
                case "POLICIA":
                case "MARINA":
                case "MILITAR":
                case "EJERCITO":
                    q.setParameter(1, nullATodo(filtro.trim().toUpperCase()));
                    break;
                case "ape":
                    q.setParameter(1, "%" + filtro.trim().toUpperCase() + "%");
                    break;
            }
            q.setHint("eclipselink.result-type", "Map");
            List<ArrayRecord> list = q.getResultList(); 
            
            return list;
        } catch (Exception e) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorEnBusquedaBD"));
            e.printStackTrace();
            return null;
        }
    }
    
    public List<ArrayRecord> listarDenegadosxFiltro(String filtro, String tipoDoc) {
        String campo = "";
        String inner = "";
        String where = "";
        boolean buscar = false;
        if (tipoDoc.equals("DNI")) {
            //campo = " TO_CHAR(p.COD_USR, '00000000') NRO_DOC,";
            campo = " TO_CHAR(d.COD_USR, '00000000') NRO_DOC,";            
            inner = " LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC p ON p.COD_USR=d.COD_USR";
            where = " WHERE d.TIP_USR=2 AND TRIM(TO_CHAR(d.COD_USR, '00000000')) = ?1";
            buscar = true;
        }
        if (tipoDoc.equals("CIP")) {
            //campo = " p.CIP NRO_DOC,";
            campo = " d.COD_USR NRO_DOC,";            
            inner = " LEFT JOIN RMA1369.EFECTIVO@SUCAMEC p ON p.COD_USR=d.COD_USR";
            where = " WHERE d.TIP_USR=4 AND d.COD_USR=?1"; //AND p.CIP=?1";
            buscar = true;
        }
        if (tipoDoc.equals("RUC")) {
            //campo = " p.RUC_OK NRO_DOC,";            
            campo = " d.COD_USR NRO_DOC,";            
            inner = " LEFT JOIN RMA1369.EMPRESA@SUCAMEC p ON p.RUC=d.COD_USR";
            where = " WHERE d.TIP_USR=1 AND d.COD_USR=?1"; //AND RUC_OK=?1";
        }
        if (tipoDoc.equals("CE")) {
            //campo = " p.CRNT_EXT NRO_DOC,";
            campo = " d.COD_USR NRO_DOC,";            
            inner = " LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC p ON p.COD_USR=d.COD_USR";
            where = " WHERE d.TIP_USR=5 AND d.COD_USR=?1"; //AND (p.CRNT_EXT=?1)";
            buscar = true;
        }
        
        String sql = "SELECT"
                + " d.COD_USR,"
                + campo
                + " d.ANO_EXP, d.NRO_EXP,"
                + " TO_CHAR(d.FEC_INI, 'DD/MM/YYYY') AS FEC_INI,"
                + " TO_CHAR(d.FEC_FIN, 'DD/MM/YYYY') AS FEC_FIN,"
                + " d.JUZGADO, d.TIPO_SUSPENCION"
                + " FROM RMA1369.AM_DENEGADO@SUCAMEC d"
                + inner
                + where
                + " AND d.FLAG_ALTA IS NULL"
                + " ORDER BY d.COD_USR, d.ANO_EXP DESC, d.NRO_EXP DESC"
                + " ";
        if(buscar){
            try {
                Query q = em.createNativeQuery(sql);
                q.setParameter(1, filtro.trim().toUpperCase());
                q.setHint("eclipselink.result-type", "Map");
                List<ArrayRecord> list = q.setMaxResults(MAX_RES).getResultList(); 
                if(list.size()>0){
                    return list;                    
                }else{
                    return null;                    
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }            
        }else{
            return null;
        }
    }

}
