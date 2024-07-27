/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.bdintegrado.bean.AmaMaestroArmasFacade;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.sel.citas.data.CitaTurSede;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;

/**
 *
 * @author msalinas
 */
@Stateless
public class CitaRma1369Facade extends AbstractFacade<CitaTurSede> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @EJB
    private AmaMaestroArmasFacade ejbAmaMaestroArmasFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaRma1369Facade() {
        super(CitaTurSede.class);
    }

    public List<String> buscarVigilanteEmpresa(String RUC, String nroDoc) {
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT S.COD_USR "
                //                + "FROM RMA1369.SS_EMP_VIG@SUCAMEC S "
                + "FROM RMA1369.WS_VIGILANTES@SUCAMEC S "
                + "WHERE S.RUC = ? "
                + "AND S.COD_USR = ? "
                + "AND FEC_VENC > SYSDATE"
        );
        q.setParameter(1, RUC);
        q.setParameter(2, nroDoc);
        return q.getResultList();
    }

    public List<String> buscarVigilanteEmpresaIntegrado(String RUC, String nroDoc, String tipo) {
        String filtroTipo = "";
        if (tipo!=null && !tipo.equals("")) {
            switch (tipo){
                case "TP_TRA_VP":
                    filtroTipo = " AND C.MODALIDAD_ID IN (SELECT ID FROM BDINTEGRADO.TIPO_SEGURIDAD WHERE COD_PROG IN('TP_MCO_PRO', 'TP_MCO_TRA', 'TP_MCO_VIG'))";
                    break;
                case "TP_TRA_SIS":
                    filtroTipo = " AND C.MODALIDAD_ID IN (SELECT ID FROM BDINTEGRADO.TIPO_SEGURIDAD WHERE COD_PROG IN('TP_MCO_SIS'))";
                    break;
            }
        }
        
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT C.*"
                    + " FROM BDINTEGRADO.SSP_REGISTRO R"
                    + " INNER JOIN BDINTEGRADO.SB_PERSONA E ON R.EMPRESA_ID=E.ID"
                    + " INNER JOIN BDINTEGRADO.SSP_CARNE C ON C.ID = R.CARNE_ID"
                    + " INNER JOIN BDINTEGRADO.SB_PERSONA P ON P.ID = C.VIGILANTE_ID"
                    + " WHERE R.ACTIVO = 1 AND C.ACTIVO = 1 AND TIPO_PRO_ID = (SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_GSSP_ECRN')"
                    + filtroTipo                        
                    + " AND TRUNC(C.FECHA_FIN) >= TRUNC(TO_DATE('" + JsfUtil.dateToString(new Date(), "dd/MM/yyyy") + "'))"
                    + " AND E.RUC = ?"
                    + " AND P.NUM_DOC = ?"
        );
        q.setParameter(1, RUC);
        q.setParameter(2, nroDoc);
        return q.getResultList();
    }
    
    public List<String> buscarVigilanteEmpresaFecha(String RUC, String nroDoc, Date fecha) {
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT S.COD_USR "
                //                + "FROM RMA1369.SS_EMP_VIG@SUCAMEC S "
                + "FROM RMA1369.WS_VIGILANTES@SUCAMEC S "
                + "WHERE S.RUC = ? "
                + "AND S.COD_USR = ? "
                //+ "AND FEC_VENC > SYSDATE"
                + "AND TRUNC(FEC_VENC) >= TRUNC(TO_DATE('" + JsfUtil.dateToString(fecha, "dd/MM/yyyy") + "'))"
        );
        q.setParameter(1, RUC);
        q.setParameter(2, nroDoc);
        return q.getResultList();
    }    

    public List<String> buscarVigilanteEmpresaFechaIntegrado(String RUC, String nroDoc, String tipo, Date fecha, boolean validaVigencia) {
        String filtroTipo = "";
        if (tipo!=null && !tipo.equals("")) {
            switch (tipo){
                case "TP_TRA_VP":
                    filtroTipo = " AND C.MODALIDAD_ID IN (SELECT ID FROM BDINTEGRADO.TIPO_SEGURIDAD WHERE COD_PROG IN('TP_MCO_PRO', 'TP_MCO_TRA', 'TP_MCO_VIG'))";
                    break;
                case "TP_TRA_SIS":
                    filtroTipo = " AND C.MODALIDAD_ID IN (SELECT ID FROM BDINTEGRADO.TIPO_SEGURIDAD WHERE COD_PROG IN('TP_MCO_SIS'))";
                    break;
            }
        }
        
        String filtroVigencia = "";
        if (validaVigencia) {
            filtroVigencia = " AND TRUNC(C.FECHA_FIN) >= TRUNC(TO_DATE('" + JsfUtil.dateToString(fecha, "dd/MM/yyyy") + "'))";
        }
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT C.*"
                        + " FROM BDINTEGRADO.SSP_REGISTRO R"
                        + " INNER JOIN BDINTEGRADO.SB_PERSONA E ON R.EMPRESA_ID=E.ID"
                        + " INNER JOIN BDINTEGRADO.SSP_CARNE C ON C.ID = R.CARNE_ID"
                        + " INNER JOIN BDINTEGRADO.SB_PERSONA P ON P.ID = C.VIGILANTE_ID"
                        + " WHERE R.ACTIVO = 1 AND C.ACTIVO = 1 AND TIPO_PRO_ID = (SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_GSSP_ECRN')"
                        + filtroTipo
                        + filtroVigencia
                        + " AND E.RUC = ?"
                        + " AND P.NUM_DOC = ?"
                //+ "AND FEC_VENC > SYSDATE"
                //+ "AND TRUNC(FEC_VENC) >= TRUNC(TO_DATE('" + JsfUtil.dateToString(fecha, "dd/MM/yyyy") + "'))"
        );
        q.setParameter(1, RUC);
        q.setParameter(2, nroDoc);
        return q.getResultList();
    }    
    
    public List<Map> buscarEmpresaSeguridadXRuc(String ruc) {
        String sql = "SELECT RS.EMPRESA \"empresa\" \n"
                + "FROM RMA1369.WS_VIGILANTES@SUCAMEC RS\n"
                //                + "WHERE RS.DES_MOD = 'SERVICIO INDIVIDUAL DE SEGURIDAD PERSONAL'\n"
                + "WHERE RS.RUC = ?1";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, ruc);
        res = query.getResultList();
        return res;
    }

    public List<Map> buscarEmpresaSeguridadXRucIntegrado(String ruc) {
        String sql = "SELECT RS.EMPRESA \"empresa\" \n"
                + "FROM RMA1369.WS_VIGILANTES@SUCAMEC RS\n"
                //                + "WHERE RS.DES_MOD = 'SERVICIO INDIVIDUAL DE SEGURIDAD PERSONAL'\n"
                + "WHERE RS.RUC = ?1";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, ruc);
        res = query.getResultList();
        return res;
    }
    
    public List<Map> buscarPerJurSispeXRuc(String ruc) {
        String sql = "select RS.ape_pat \"apePat\", RS.ape_mat \"apeMat\", RS.nombre_vig \"nombre\"\n"
                + "FROM RMA1369.WS_VIGILANTES@SUCAMEC RS\n"
                //                + "WHERE RS.DES_MOD = 'SERVICIO INDIVIDUAL DE SEGURIDAD PERSONAL'\n"
                + "WHERE RS.RUC = ?1";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, ruc);
        res = query.getResultList();
        return res;
    }

    public List<Map> listarArmasCanceladasxFiltro(String serie, String licencia) {
        String campo = "";
        campo = "li.NRO_LIC LIKE ?1 AND li.NRO_SERIE LIKE ?2 ";
        
        String sql = "SELECT li.NRO_SERIE, li.NRO_LIC, li.ESTADO, an.FEC_PRC"
                //+ " FROM RMA1369.WS_LICENCIAS@SUCAMEC li"
                + " FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC li"
                + " LEFT JOIN RMA1369.AM_LIC_ANULADA@SUCAMEC an ON li.NRO_LIC=an.NRO_LIC"
                + " WHERE "+ campo 
                + " AND li.SISTEMA='DISCA' AND li.TIPO_PROPIETARIO in('PERS. NATURAL', 'EXTRANJERO')"
                + " AND an.FEC_DOC>=TO_DATE('17-05-2017','DD-MM-YYYY')"
                + " AND an.MOT_ID=11"
                //+ " AND ID IN(SELECT MIN(ID) FROM RMA1369.WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                //+ " ORDER BY li.NRO_LIC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            
            q.setParameter(1, Long.valueOf(licencia.trim()));
            q.setParameter(2, serie.trim().toUpperCase());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();            
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ArrayRecord> listarArrayRecord() {
        boolean buscar = true;

        String sql = "SELECT ANU_ID, NRO_LIC FROM RMA1369.AM_LIC_ANULADA@SUCAMEC WHERE ANU_ID IN (21012, 21960, 21961)";
        if(buscar){
            try {
                Query q = em.createNativeQuery(sql);
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
    
    public List<Map> listarArmasxUsuario(String docPropietario) {
        String campo = "";
        campo = "li.DOC_PROPIETARIO = ?1 ";
        
        String sql = "SELECT"
                + " li.NRO_LIC, li.FEC_VENCIMIENTO,"
                + " COALESCE(li.TIPO_LICENCIA,'-') TIPO_LICENCIA,"
                + " li.NRO_SERIE,"
                + " li.TIPO_ARMA,"
                + " COALESCE(li.MARCA, '-') MARCA,"
                + " COALESCE(li.MODELO, '-') MODELO,"
                + " COALESCE(li.CALIBRE, '-') CALIBRE,"
                + " li.ESTADO, li.SITUACION"
                //+ " FROM RMA1369.WS_LICENCIAS@SUCAMEC li"
                + " FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC li"
                + " WHERE "+ campo 
                + " AND li.SISTEMA='DISCA'"
                + " AND li.ESTADO IN('VENCIDO', 'VIGENTE')"
                //+ " AND li.TIPO_PROPIETARIO in('PERS. NATURAL', 'EXTRANJERO')"
                //+ " AND ID IN(SELECT MIN(ID) FROM RMA1369.WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                + " ORDER BY li.FEC_VENCIMIENTO DESC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            
            q.setParameter(1, docPropietario.trim().toUpperCase());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map> listarArmasxUsrLicSerie(String docPropietario, Long nroLic, String nroSerie) {
        String campo = "";
        //campo = "li.DOC_PROPIETARIO = ?1";
        campo = "(li.DOC_PROPIETARIO = ?1 OR li.DOC_PROPIETARIO LIKE '1_" + docPropietario.trim() + "_')";
        
        String sql = "SELECT"
                + " li.NRO_LIC, li.FEC_VENCIMIENTO,"
                + " COALESCE(li.TIPO_LICENCIA,'-') TIPO_LICENCIA,"
                + " li.NRO_SERIE,"
                + " li.TIPO_ARMA,"
                + " COALESCE(li.MARCA, '-') MARCA,"
                + " COALESCE(li.MODELO, '-') MODELO,"
                + " COALESCE(li.CALIBRE, '-') CALIBRE,"
                + " li.ESTADO, li.SITUACION"
                + " FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC li"
                + " WHERE "+ campo 
                + " AND li.SISTEMA='DISCA'"
                + " AND li.NRO_LIC=?2"
                + " AND li.NRO_SERIE=?3"
                //+ " AND li.TIPO_PROPIETARIO in('PERS. NATURAL', 'EXTRANJERO')"
                //+ " AND ID IN(SELECT MIN(ID) FROM RMA1369.WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                + " ORDER BY li.FEC_VENCIMIENTO DESC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            
            q.setParameter(1, docPropietario.trim().toUpperCase());
            q.setParameter(2, nroLic);
            q.setParameter(3, nroSerie.trim());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * CONSULTA MIGRACIÓN ARMA
     * @param docPropietario
     * @param nroLic
     * @param nroSerie
     * @param personaId
     * @return 
     */
    public List<Map> listarArmasxUsrLicSerieMigra(String docPropietario, Long nroLic, String nroSerie, Long personaId) {
        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_GENERAL() + ")";        
        String campo = "";
        //campo = "li.DOC_PROPIETARIO = ?1";
        campo = "(li.DOC_PROPIETARIO = ?1 OR li.DOC_PROPIETARIO LIKE '1_" + docPropietario.trim() + "_'";
        if (personaId != null) {
            campo += " OR li.PERSONA_ID =" + personaId;
        }
        campo += ")";
        
        String sql = "SELECT"
                + " li.LICENCIA_DISCA NRO_LIC,"
                + " li.FECHA_VEN_DISCA FEC_VENCIMIENTO,"
                + " COALESCE(li.MODALIDAD_ARMA,'-') TIPO_LICENCIA,"
                + " li.SERIE NRO_SERIE,"
                + " li.TIPO_ARMA,"
                + " COALESCE(li.MARCA, '-') MARCA,"
                + " COALESCE(li.MODELO, '-') MODELO,"
                + " COALESCE(li.CALIBRE, '-') CALIBRE,"
                + " li.ESTADO_ARMA ESTADO,"
                + " li.SITUACION_ARMA SITUACION"
                + " FROM " + sqlMigra + " li"
                + " WHERE "+ campo 
                + " AND li.SISTEMA='DISCA'"
                + " AND li.LICENCIA_DISCA=?2"
                + " AND li.SERIE=?3"
                //+ " AND li.TIPO_PROPIETARIO in('PERS. NATURAL', 'EXTRANJERO')"
                //+ " AND ID IN(SELECT MIN(ID) FROM RMA1369.WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                + " ORDER BY li.FECHA_VEN_DISCA DESC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            
            q.setParameter(1, docPropietario.trim().toUpperCase());
            q.setParameter(2, nroLic);
            q.setParameter(3, nroSerie.trim());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map> listarArmasxUsrRuaSerie(String docPropietario, String nroRua, String nroSerie) {
        String campo = "";
        //campo = "li.DOC_PROPIETARIO = ?1";
        campo = "(li.DOC_PROPIETARIO = ?1 OR li.DOC_PROPIETARIO LIKE '1_" + docPropietario.trim() + "_')";
        
        String sql = "SELECT"
                + " li.NRO_LIC, li.FEC_VENCIMIENTO,"
                + " COALESCE(li.TIPO_LICENCIA,'-') TIPO_LICENCIA,"
                + " li.NRO_SERIE,"
                + " li.TIPO_ARMA,"
                + " COALESCE(li.MARCA, '-') MARCA,"
                + " COALESCE(li.MODELO, '-') MODELO,"
                + " COALESCE(li.CALIBRE, '-') CALIBRE,"
                + " li.ESTADO, li.SITUACION"
                + " FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC li"
                + " WHERE "+ campo 
                + " AND li.SISTEMA='GAMAC'"
                + " AND li.NRO_RUA=?2"
                + " AND li.NRO_SERIE=?3"
                //+ " AND li.TIPO_PROPIETARIO in('PERS. NATURAL', 'EXTRANJERO')"
                //+ " AND ID IN(SELECT MIN(ID) FROM RMA1369.WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                + " ORDER BY li.FEC_VENCIMIENTO DESC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            
            q.setParameter(1, docPropietario.trim().toUpperCase());
            q.setParameter(2, nroRua.trim());
            q.setParameter(3, nroSerie.trim());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * CONSULTA MIGRACIÓN ARMA
     * @param docPropietario
     * @param nroRua
     * @param nroSerie
     * @param personaId
     * @return 
     */
    public List<Map> listarArmasxUsrRuaSerieMigra(String docPropietario, String nroRua, String nroSerie, Long personaId) {
        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_GENERAL() + ")";
        String campo = "";
        //campo = "li.DOC_PROPIETARIO = ?1";
        campo = "(li.DOC_PROPIETARIO = ?1 OR li.DOC_PROPIETARIO LIKE '1_" + docPropietario.trim() + "_')";
        if (personaId != null) {
            campo += " OR li.PERSONA_ID =" + personaId;
        }
        campo += ")";
        
        String sql = "SELECT"
                + " li.LICENCIA_DISCA NRO_LIC,"
                + " li.FECHA_VEN_DISCA FEC_VENCIMIENTO,"
                + " COALESCE(li.MODALIDAD_ARMA,'-') TIPO_LICENCIA,"
                + " li.SERIE NRO_SERIE,"
                + " li.TIPO_ARMA,"
                + " COALESCE(li.MARCA, '-') MARCA,"
                + " COALESCE(li.MODELO, '-') MODELO,"
                + " COALESCE(li.CALIBRE, '-') CALIBRE,"
                + " li.ESTADO_ARMA ESTADO,"
                + " li.SITUACION_ARMA SITUACION"
                + " FROM " + sqlMigra + " li"
                + " WHERE "+ campo 
                + " AND li.SISTEMA='GAMAC'"
                + " AND li.NRO_RUA=?2"
                + " AND li.SERIE=?3"
                //+ " AND li.TIPO_PROPIETARIO in('PERS. NATURAL', 'EXTRANJERO')"
                //+ " AND ID IN(SELECT MIN(ID) FROM RMA1369.WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                + " ORDER BY li.FECHA_VEN_DISCA DESC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            
            q.setParameter(1, docPropietario.trim().toUpperCase());
            q.setParameter(2, nroRua.trim());
            q.setParameter(3, nroSerie.trim());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
      
    public List<Map> listarArmasxUsuarioNoVal(String docPropietario, Long turnoId, Long tipoTramite, String estados) {
        String campo = "";
        String innerTurno = "";
        String caseTipo = "";
        campo = "(li.DOC_PROPIETARIO = ?1 OR li.DOC_PROPIETARIO LIKE '1_" + docPropietario.trim() + "_')";
        
        if (turnoId != null) {
            innerTurno = " LEFT JOIN BDINTEGRADO.TUR_TURNO t ON t.ID=tl.TURNO_ID"
                    + " LEFT JOIN BDINTEGRADO.TIPO_GAMAC tt ON t.TIPO_TRAMITE_ID=tt.ID AND tt.COD_PROG='TP_TRAM_VAL' "; //AND t.ID=" + turnoId + " ";
            caseTipo = " CASE WHEN (t.ID IS NOT NULL) THEN tl.SERIE ELSE NULL END SERIE,"
                    //Se cambia para que no valide que ha pasado por Validación de Armas
                    + " NULL AS TIPO_ESTADO,"
                    //+ " CASE WHEN (t.ID IS NOT NULL) THEN tl.TIPO_ESTADO ELSE NULL END TIPO_ESTADO,"
                + " NULL SITUACION_ID";            

            //innerTurno = " AND tl.TURNO_ID=" + turnoId + " ";
            //caseTipo = " CASE WHEN (tl.TURNO_ID IS NOT NULL) THEN tl.SERIE ELSE NULL END SERIE,"
                //+ " CASE WHEN (tl.TURNO_ID IS NOT NULL) THEN tl.TIPO_ESTADO ELSE NULL END TIPO_ESTADO,"
                //+ " NULL SITUACION_ID";            

        } else {
            caseTipo = " CASE WHEN (t.ID IS NOT NULL) THEN tl.SERIE ELSE NULL END SERIE,"
                    //Se cambia para que no valide que ha pasado por Validación de Armas
                    + " NULL AS TIPO_ESTADO,"
                //+ " CASE WHEN (t.ID IS NOT NULL) THEN tl.TIPO_ESTADO ELSE NULL END TIPO_ESTADO,"
                + " CASE WHEN (t.ID IS NOT NULL) THEN tl.SITUACION_ID ELSE NULL END SITUACION_ID";
        }
        if (tipoTramite != null) {
            innerTurno = " LEFT JOIN BDINTEGRADO.TUR_TURNO t ON t.ID=tl.TURNO_ID AND t.TIPO_TRAMITE_ID=" + tipoTramite + " ";
        }
        String sql = "SELECT DISTINCT"
                + " li.NRO_LIC, li.FEC_VENCIMIENTO,"
                + " COALESCE(li.TIPO_LICENCIA,'-') TIPO_LICENCIA,"
                + " li.NRO_RUA,"
                + " li.NRO_SERIE,"
                + " li.TIPO_ARMA,"
                + " COALESCE(li.MARCA, '-') MARCA,"
                + " COALESCE(li.MODELO, '-') MODELO,"
                + " COALESCE(li.CALIBRE, '-') CALIBRE,"
                + " li.ESTADO, li.SITUACION, li.SISTEMA,"
                //+ " t.ID TURNO_ID,"
                + caseTipo
                //+ " FROM RMA1369.WS_LICENCIAS@SUCAMEC li"
                + " FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC li"
                + "  LEFT JOIN BDINTEGRADO.TUR_LICENCIA_REG tl ON (TRIM(tl.SERIE)=TRIM(li.NRO_SERIE) AND tl.ACTUAL=1 AND tl.activo=1)" //tl.NUM_LIC=li.NRO_LIC AND TRIM(tl.SERIE)=TRIM(li.NRO_SERIE)"
                +  innerTurno //AND t.ESTADO IN" + estados
                + " WHERE "+ campo
                + " AND li.TIPO_PROPIETARIO IN('PERS. NATURAL', 'PERS. JURIDICA', 'EXTRANJERO')"
                //+ " AND li.SISTEMA='DISCA'"
                + " AND li.ESTADO IN('VENCIDO', 'VIGENTE')"
                //+ " AND li.TIPO_PROPIETARIO in('PERS. NATURAL', 'EXTRANJERO')"
                //+ " AND ID IN(SELECT MIN(ID) FROM RMA1369.WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                + " ORDER BY li.FEC_VENCIMIENTO DESC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            //Syso(sql);
            q.setParameter(1, docPropietario.trim().toUpperCase());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * CONSULTA MIGRACIÓN ARMA
     * @param docPropietario
     * @param turnoId
     * @param tipoTramite
     * @param estados
     * @param personaId
     * @return 
     */
    public List<Map> listarArmasxUsuarioNoValMigra(String docPropietario, Long turnoId, Long tipoTramite, String estados, Long personaId) {
        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_GENERAL() + ")";        
        String campo = "";
        String innerTurno = "";
        String caseTipo = "";
        campo = "(li.DOC_PROPIETARIO = ?1 OR li.DOC_PROPIETARIO LIKE '1_" + docPropietario.trim() + "_'";
        if (personaId != null) {
            campo += " OR li.PERSONA_ID = " + personaId;
        }
        campo += ") ";
        
        if (turnoId != null) {
            innerTurno = " LEFT JOIN BDINTEGRADO.TUR_TURNO t ON t.ID=tl.TURNO_ID"
                    + " LEFT JOIN BDINTEGRADO.TIPO_GAMAC tt ON t.TIPO_TRAMITE_ID=tt.ID AND tt.COD_PROG='TP_TRAM_VAL' "; //AND t.ID=" + turnoId + " ";
            caseTipo = " CASE WHEN (t.ID IS NOT NULL) THEN tl.SERIE ELSE NULL END SERIE,"
                    //Se cambia para que no valide que ha pasado por Validación de Armas
                + " NULL AS TIPO_ESTADO,"    
                //+ " CASE WHEN (t.ID IS NOT NULL) THEN tl.TIPO_ESTADO ELSE NULL END TIPO_ESTADO,"
                + " NULL SITUACION_ID";            

            //innerTurno = " AND tl.TURNO_ID=" + turnoId + " ";
            //caseTipo = " CASE WHEN (tl.TURNO_ID IS NOT NULL) THEN tl.SERIE ELSE NULL END SERIE,"
                //+ " CASE WHEN (tl.TURNO_ID IS NOT NULL) THEN tl.TIPO_ESTADO ELSE NULL END TIPO_ESTADO,"
                //+ " NULL SITUACION_ID";            

        } else {
            caseTipo = " CASE WHEN (t.ID IS NOT NULL) THEN tl.SERIE ELSE NULL END SERIE,"
                    //Se cambia para que no valide que ha pasado por Validación de Armas
                    + " NULL AS TIPO_ESTADO,"
                //+ " CASE WHEN (t.ID IS NOT NULL) THEN tl.TIPO_ESTADO ELSE NULL END TIPO_ESTADO,"
                + " CASE WHEN (t.ID IS NOT NULL) THEN tl.SITUACION_ID ELSE NULL END SITUACION_ID";
        }
        if (tipoTramite != null) {
            innerTurno = " LEFT JOIN BDINTEGRADO.TUR_TURNO t ON t.ID=tl.TURNO_ID AND t.TIPO_TRAMITE_ID=" + tipoTramite + " ";
        }
        String sql = "SELECT DISTINCT"
                + " li.ID ID,"
                + " li.LICENCIA_DISCA NRO_LIC,"
                + " li.FECHA_VEN_DISCA FEC_VENCIMIENTO,"
                + " COALESCE(li.MODALIDAD_ARMA,'-') TIPO_LICENCIA,"
                + " li.NRO_RUA,"
                + " li.SERIE NRO_SERIE,"
                + " li.TIPO_ARMA,"
                + " COALESCE(li.MARCA, '-') MARCA,"
                + " COALESCE(li.MODELO, '-') MODELO,"
                + " COALESCE(li.CALIBRE, '-') CALIBRE,"
                + " li.ESTADO_LIC_DISCA ESTADO,"
                + " li.SITUACION_ARMA SITUACION,"
                + " li.SISTEMA,"
                //+ " t.ID TURNO_ID,"
                + caseTipo
                + " FROM " + sqlMigra + " li"
                + "  LEFT JOIN BDINTEGRADO.TUR_LICENCIA_REG tl ON (TRIM(tl.SERIE)=TRIM(li.SERIE) AND tl.ACTUAL=1 AND tl.activo=1)" //tl.NUM_LIC=li.NRO_LIC AND TRIM(tl.SERIE)=TRIM(li.NRO_SERIE)"
                +  innerTurno //AND t.ESTADO IN" + estados
                + " WHERE "+ campo
                + " AND li.TIPO_PROPIETARIO IN('PERSONA NATURAL', 'PERSONA JURIDICA', 'MIEMBRO FFA O PNP')"
                //+ " AND li.SISTEMA='DISCA'"
                //+ " AND li.ESTADO_LIC_DISCA IN('VENCIDO', 'VIGENTE')"
                + " ORDER BY li.FECHA_VEN_DISCA DESC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            //Syso(sql);
            q.setParameter(1, docPropietario.trim().toUpperCase());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();            
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ArrayRecord> buscarEmpresaVigilante(String RUC) {
        /*String sql = "SELECT"
                + " p.ID || ',' || p.RUC || ',' || rg.NRO_RD || ',' || rg.FEC_VEN ID,"
                + " p.ID PERSONA_ID, p.RUC"
                + " FROM"
                + " BDINTEGRADO.SB_PERSONA p"
                + " INNER JOIN RMA1369.WS_RESOLUCION_GAMAC@SUCAMEC rg ON TRIM(p.RUC)=TRIM(rg.RUC)"  
                + " WHERE"
                + " p.TIPO_ID=93"
                + " AND p.ID<>?1"
                + " AND p.RUC LIKE ?2"
                + " GROUP BY p.ID, p.RUC, rg.NRO_RD, rg.FEC_VEN"
                + " ORDER BY p.ID"
                + "";*/
        
        String sql = "SELECT DISTINCT"
                + " TO_CHAR(S.RUC) || ',' || S.EMPRESA ID,"
                + " S.EMPRESA,"
                + " TO_CHAR(S.RUC) RUC"
                //+ " S. "
                + " FROM RMA1369.WS_VIGILANTES@SUCAMEC S "
                + " WHERE S.RUC = ? "
                //+ "AND S.COD_USR = ? "
                //+ " AND FEC_VENC > SYSDATE"
                + "";

        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");        
        q.setParameter(1, RUC);
        //q.setParameter(2, nroDoc);
        return q.getResultList();
    }

    public List<ArrayRecord> buscarEmpresaVigilanteIntegrado(String RUC) {
        String sql = "SELECT DISTINCT"
                + " TO_CHAR(S.RUC) || ',' || COALESCE(S.RZN_SOCIAL, S.APE_PAT || ' ' || S.APE_MAT || ' ' || S.NOMBRES) ID,"
                + " COALESCE(S.RZN_SOCIAL, S.APE_PAT || ' ' || S.APE_MAT || ' ' || S.NOMBRES) EMPRESA,"
                + " TO_CHAR(S.RUC) RUC"
                + " FROM BDINTEGRADO.SB_PERSONA S "
                + " WHERE S.RUC = ? "
                + "";
        
        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");        
        q.setParameter(1, RUC);
        //q.setParameter(2, nroDoc);
        return q.getResultList();
    }
    
    public List<ArrayRecord> buscarVigilanteXEmpresa(String nroDoc, String RUC) {
        String sql = "SELECT DISTINCT"
                + " TO_CHAR(S.COD_USR) || ',' || S.APE_PAT || '-' || S.APE_MAT || '-' || S.NOMBRE_VIG ID,"
                + " S.EMPRESA,"
                + " TO_CHAR(S.RUC) RUC"
                //+ " S. "
                + " FROM RMA1369.WS_VIGILANTES@SUCAMEC S"
                + " WHERE S.RUC = ?"
                + " AND S.COD_USR = ?"
                + " AND FEC_VENC > SYSDATE"
                + "";

        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");        
        q.setParameter(1, RUC);
        q.setParameter(2, nroDoc);
        return q.getResultList();
    }

    public List<ArrayRecord> buscarVigilanteXEmpresaJur(String nroDoc, String RUC) {
        String sql = "SELECT DISTINCT"
                + " TO_CHAR(S.COD_USR) || ',' || COALESCE(S.APE_PAT,' ') || '-' || COALESCE(S.APE_MAT,' ') || '-' || COALESCE(S.NOMBRE_VIG,' ') ID,"
                + " S.EMPRESA,"
                + " TO_CHAR(S.RUC) RUC"
                //+ " S. "
                + " FROM RMA1369.WS_VIGILANTES@SUCAMEC S"
                + " WHERE S.RUC = ?"
                + " AND S.COD_USR = ?"
                //+ " AND FEC_VENC > SYSDATE"
                + "";

        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");        
        q.setParameter(1, RUC);
        q.setParameter(2, nroDoc);
        return q.getResultList();
    }
    
  public List<ArrayRecord> buscarVigilanteXEmpresaJurIntegrado(String nroDoc, String RUC) {
        String sql = "SELECT TO_CHAR(P.NUM_DOC) || ',' || P.APE_PAT || '-' || P.APE_MAT || '-' || P.NOMBRES ID,"
                + " E.RZN_SOCIAL EMPRESA,"
                + " TO_CHAR(E.RUC) RUC"
                + " FROM BDINTEGRADO.SSP_REGISTRO R"
                + " INNER JOIN BDINTEGRADO.SB_PERSONA E ON R.EMPRESA_ID=E.ID"
                + " INNER JOIN BDINTEGRADO.SSP_CARNE C ON C.ID = R.CARNE_ID"
                + " INNER JOIN BDINTEGRADO.SB_PERSONA P ON P.ID = C.VIGILANTE_ID"
                + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD EST ON EST.ID = R.ESTADO_ID" 
                + " WHERE R.ACTIVO = 1 AND C.ACTIVO = 1 AND TIPO_PRO_ID = (SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_GSSP_ECRN')"
                //+ " AND TRUNC(C.FECHA_FIN) >= TRUNC(TO_DATE('" + JsfUtil.dateToString(new Date(), "dd/MM/yyyy") + "'))"
                + " AND E.RUC = ?"
                + " AND P.NUM_DOC = ?"
                + " AND EST.COD_PROG= 'TP_ECC_APR'" ;
        
                /*"SELECT DISTINCT"
                + " TO_CHAR(S.COD_USR) || ',' || COALESCE(S.APE_PAT,' ') || '-' || COALESCE(S.APE_MAT,' ') || '-' || COALESCE(S.NOMBRE_VIG,' ') ID,"
                + " S.EMPRESA,"
                + " TO_CHAR(S.RUC) RUC"
                //+ " S. "
                + " FROM RMA1369.WS_VIGILANTES@SUCAMEC S"
                + " WHERE S.RUC = ?"
                + " AND S.COD_USR = ?"
                //+ " AND FEC_VENC > SYSDATE"
                + "";*/
        
        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");        
        q.setParameter(1, RUC);
        q.setParameter(2, nroDoc);
        return q.getResultList();
    }

    /* VERIFICACION DE ARMAS */
    public List<Map> listarTarjetasDiscaXpropietario(String nroDoc) {
        String sql = "SELECT distinct"
                + " l.doc_propietario||' - '||l.propietario \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                /*+ " CASE"
                + " WHEN l.tipo_propietario IN('PERS. NATURAL') THEN 'DNI'"
                + " WHEN l.tipo_propietario IN('PERS. JURIDICA') THEN 'RUC'"
                + " WHEN l.tipo_propietario IN('EXTRANJERO') THEN 'CE'"
                + " WHEN l.tipo_propietario IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "*/
                + " l.doc_propietario \"docComprador\", "
                + " l.nro_serie \"serie\","
                + " l.tipo_arma \"tipoArma\","
                + " l.calibre \"calibre\","
                + " l.marca \"marca\","
                + " l.modelo \"modelo\", "
                + " l.situacion \"situacion\","
                + " l.estado \"estado\","
                + " l.nro_lic \"nroLic\""
                + " FROM rma1369.ws_licencias@SUCAMEC l where l.doc_propietario = ?1"
                //+ " and l.nro_lic not in (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                + " and l.sistema = 'DISCA'";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroDoc);
        res = query.getResultList();
        return res;
    } 
    /* */

    /**
     * CONSULTA PARA BUSCAR ARMAS EN EL DISCA PARA RENOVACION DE LICENCIAS
     * @param nroDoc
     * @return 
     */
    public List<Map> listarTarjetasDiscaXpropRLU(String nroDoc) {
        String sql = "SELECT distinct"
                + " l.doc_propietario||' - '||l.propietario \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                /*+ " CASE"
                + " WHEN l.tipo_propietario IN('PERS. NATURAL') THEN 'DNI'"
                + " WHEN l.tipo_propietario IN('PERS. JURIDICA') THEN 'RUC'"
                + " WHEN l.tipo_propietario IN('EXTRANJERO') THEN 'CE'"
                + " WHEN l.tipo_propietario IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "*/
                + " l.doc_propietario \"docComprador\", "
                + " l.nro_serie \"serie\","
                + " l.tipo_arma \"tipoArma\","
                + " l.calibre \"calibre\","
                + " l.marca \"marca\","
                + " l.modelo \"modelo\", "
                + " l.situacion \"situacion\","
                + " l.estado \"estado\","
                + " l.nro_lic \"nroLic\""
                + " FROM rma1369.interop_ws_licencias@SUCAMEC l where l.doc_propietario = ?1"
                //+ " and l.nro_lic not in (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                + " and l.sistema = 'DISCA'";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroDoc);
        res = query.getResultList();
        return res;
    } 
    
}
