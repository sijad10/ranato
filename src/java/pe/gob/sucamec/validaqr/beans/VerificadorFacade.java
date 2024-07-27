/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.validaqr.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;

/**
 *
 * @author mespinoza
 */
@Stateless
public class VerificadorFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     *
     * @param hash
     * @return
     */
    public String buscarHashGepp(String hash) {
        if (hash != null) {
            //TODO: Debe ser reemplazado por entidades //
            javax.persistence.Query q
                    = em.createNativeQuery("select r.registro_id ID from BDINTEGRADO.EPP_resolucion r where r.hash_qr = ?1");
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, hash);
            List<Map> res = (List) q.setMaxResults(10).getResultList();
            if (res.size() == 1) {
                return res.get(0).get("ID").toString();
            }
        }
        return null;
    }

    public String buscarHashGamac(String hash) {
        if (hash != null) {
            //TODO: Debe ser reemplazado por entidades //
            javax.persistence.Query q
                    = em.createNativeQuery("select r.id from BDINTEGRADO.AMA_resolucion r where r.hash_qr = ?1");
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, hash);
            List<Map> res = (List) q.setMaxResults(10).getResultList();
            if (res.size() == 1) {
                return res.get(0).get("ID").toString();
            }
        }
        return null;
    }

    /**
     *
     * @param p_subqr
     * @param p_regid
     * @param p_anio
     * @return
     */
    public String buscarHashGeppWeb(String p_subqr, String p_regid, String p_anio) {
        javax.persistence.Query q
                = em.createNativeQuery("select r.registro_id ID from BDINTEGRADO.EPP_resolucion r where r.hash_qr like ?1 and r.numero like ?2 ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr + "%");
        q.setParameter(2, "%" + p_regid + "-" + p_anio + "%");
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return res.get(0).get("ID").toString();
        }
        return null;
    }

    /**
     *
     * @param p_subqr
     * @param p_regid
     * @param p_anio
     * @return
     */
    public String buscarHashGamacResWeb(String p_subqr, String p_regid, String p_anio) {
        try {
            javax.persistence.Query q
                    = em.createNativeQuery("select r.ID ID from BDINTEGRADO.AMA_RESOLUCION r where r.hash_qr like ?1 and r.NRO_RESOLUCION like ?2 ");
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, p_subqr + "%");
            q.setParameter(2, "%" + p_regid + "-" + p_anio + "%");
            List<Map> res = q.setMaxResults(10).getResultList();
            if (res.size() == 1) {
                return res.get(0).get("ID").toString();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }

        return null;
    }

    /**
     *
     * @param p_subqr
     * @param p_regid
     * @param p_anio
     * @return
     */
//    public String buscarHashGeepGTWeb(String p_subqr, String p_regid, String p_anio) {
//        javax.persistence.Query q
//                = em.createNativeQuery("select gt.id from bdintegrado.ama_guia_transito gt where gt.hash_qr like ?1 and gt.id = ?2 and TO_CHAR(gt.fecha_emision,'YYYY') = ?3 ");
//        q.setHint("eclipselink.result-type", "Map");
//        q.setParameter(1, p_subqr + "%");
//        q.setParameter(2, p_regid);
//        q.setParameter(3, p_anio);
//        List<Map> res = q.setMaxResults(10).getResultList();
//        if (res.size() == 1) {
//            return res.get(0).get("ID").toString();
//        }
//        return null;
//    }
    /**
     *
     * @param p_subqr
     * @param p_regid
     * @param p_anio
     * @return
     */
    public String buscarHashGamacGTWeb(String p_subqr, String p_regid, String p_anio) {
        javax.persistence.Query q
                = em.createNativeQuery("select gt.id from bdintegrado.ama_guia_transito gt where gt.hash_qr like ?1 and gt.nro_guia like ?2 and TO_CHAR(gt.fecha_emision,'YYYY') = ?3 ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr + "%");
        q.setParameter(2, "%" + p_regid + "%");
        q.setParameter(3, p_anio);
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return res.get(0).get("ID").toString();
        }
        return null;
    }

    /**
     *
     * @param p_subqr
     * @param p_regid
     * @param p_anio
     * @return
     */
    public String buscarHashGamacActaWeb(String p_subqr, String p_regid, String p_anio) {
        javax.persistence.Query q
                = em.createNativeQuery("select gt.id from bdintegrado.ama_guia_transito gt where gt.hash_qr like ?1 and gt.nro_acta like ?2 and TO_CHAR(gt.fecha_emision,'YYYY') = ?3 ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr + "%");
        q.setParameter(2, "%" + p_regid + "%");
        q.setParameter(3, p_anio);
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return res.get(0).get("ID").toString();
        }
        return null;
    }

    public String buscarHashGamacRaessWeb(String p_subqr, String p_regid, String p_anio) {
        javax.persistence.Query q
                = em.createNativeQuery("select r.id from bdintegrado.ama_resolucion r where UPPER(r.hash_qr) like ?1 and r.nro_resolucion like ?2 and TO_CHAR(r.fecha_firma,'YYYY') = ?3 and r.activo = 1 ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr.trim().toUpperCase() + "%");
        q.setParameter(2, "%" + p_regid + "%");
        q.setParameter(3, p_anio);
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return res.get(0).get("ID").toString();
        }
        return null;
    }

    public String buscarHashGamacConstLicTarWeb(String p_subqr, String p_num, String p_anio) {
        String sql = "SELECT TO_CHAR(C.fecha_emision, 'YYYYMMDDHH24MISS') || '_' || TO_CHAR(C.ID) AS ID"
                + " FROM BDINTEGRADO.SB_CONSTANCIAS C"
                + " WHERE UPPER(C.HASH_QR) LIKE ?1 AND C.NRO_CONSTANCIA=?2 "
                + " AND TO_CHAR(C.FECHA_EMISION, 'YYYY')=?3 AND C.ACTIVO=1";
        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr.trim().toUpperCase() + "%");
        q.setParameter(2, Long.valueOf(p_num));
        q.setParameter(3, p_anio);
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return res.get(0).get("ID").toString();
        }
        return null;
    }

    public String buscarHashGssp(String p_hash) {
        if (p_hash == null) {
            p_hash = "";
        }

        String jpaQ = "select distinct(i.cod_usr) ID from rma1369.ss_instructor@SUCAMEC i where i.hash_qr = ?";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash);
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("ID").toString();
        }
    }
    
    public String buscarHashGsspResolAutoSeg(String p_hash) {
        if (p_hash == null) {
            p_hash = "";
        }

        String jpaQ = "SELECT DISTINCT(R.ID) ID from BDINTEGRADO.SSP_RESOLUCION R where R.HASH_QR = ?";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash);
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("ID").toString();
        }
    }

    public String buscarHashRAESS(String p_hash) {
        if (p_hash == null) {
            p_hash = "";
        }

        String jpaQ = "SELECT DISTINCT(A.ID) ID FROM BDINTEGRADO.AMA_RESOLUCION A WHERE A.HASH_QR = ?1 ";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash.trim());
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("ID").toString();
        }
    }

    public String buscarHashConstLicTar(String p_hash) {
        if (p_hash == null) {
            p_hash = "";
        } else {
            String[] valUrl = p_hash.split("-");
            p_hash = valUrl[0];
        }

        String jpaQ = "SELECT DISTINCT TO_CHAR(C.fecha_emision, 'YYYYMMDDHH24MISS') || '_' || TO_CHAR(C.ID) AS ID FROM BDINTEGRADO.SB_CONSTANCIAS C WHERE UPPER(C.HASH_QR) = ?1 ";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash.trim());
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("ID").toString();
        }
    }

    /**
     *
     * @param p_subqr
     * @param p_dni
     * @return
     */
    public String buscarHashGsspWeb(String p_subqr, String p_dni) {

        String jpaQ = "select distinct(r.cod_usr) ID from rma1369.ss_instructor@SUCAMEC r where r.hash_qr like ?1 and r.cod_usr = ?2";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res;
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr + "%");
        q.setParameter(2, p_dni);
        res = q.getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("ID").toString();
        }
    }

    public String buscarHashPoligono(String p_hash) {
        if (p_hash == null) {
            p_hash = "";
        }

        String jpaQ = "select c.id ID from BDINTEGRADO.tur_constancia c where c.hash_qr = ? ORDER BY c.id desc";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res;
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash);
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("ID").toString();
        }
    }
    
    public String buscarHashOficio(String p_hash, String p_num, String p_anio) {
        if (p_hash == null) {
            p_hash = "";
        }
        //Control Carne vigente con Curso/autorizacion vencida   
        String jpaQ = "select d.id ID from BDINTEGRADO.NE_documento d where UPPER(d.hash_qr) like ?1 and "+
                " nro_doc like 'N°%'|| ?2 || '-' || ?3 || '-SUCAMEC-GSSP' and TO_CHAR(d.fecha_doc, 'YYYY') = ?3"+
                " ORDER BY d.id desc";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res;
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash.trim().toUpperCase() + "%");
        q.setParameter(2, Long.valueOf(p_num));
        q.setParameter(3, p_anio);
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("ID").toString();
        }
    }

    /**
     *
     * @param p_subqr
     * @param p_num
     * @return
     */
    public String buscarHashPoligonoWeb(String p_subqr, String p_num) {

        String jpaQ = "select c.id ID from BDINTEGRADO.tur_constancia c where substr(upper(c.hash_qr),0,4) like ?1 and (c.id = ?2 or c.turno_id = ?2 ) order by c.id desc";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res;
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, "%" + p_subqr.toUpperCase() + "%");
        q.setParameter(2, p_num);
        res = q.getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("ID").toString();
        }
    }

    public List<ArrayRecord> buscarHashGamacTar(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT AR.NRO_RUA, "
                + "  TARM.NOMBRE AS TIPO, "
                + "  MAR.NOMBRE  AS MARCA, "
                + "  modl.modelo AS MODELO, "
                + "  LISTAGG(CAL.NOMBRE , ' / ') WITHIN GROUP (ORDER BY CAL.NOMBRE)  AS CALIBRE, "
                + "  AR.SERIE, "
                + "  COALESCE(SIT.COD_PROG, '') COD, "
                + "  SIT.NOMBRE SITUACION, "
                + "  CASE "
                + "    WHEN TPER.COD_PROG = 'TP_PER_JUR' " //RUC IS NOT NULL
                + "    THEN 'RUC' "
                + "      || ' ' "
                + "      ||P.RUC "
                + "    ELSE "
                + "      CASE "
                + "        WHEN TPER.COD_PROG = 'TP_PER_JUR' "
                + "        THEN TDOC.NOMBRE "
                + "          || ' ' "
                + "          || P.RUC "
                + "        ELSE TDOC.NOMBRE "
                + "          || ' ' "
                + "          || P.NUM_DOC "
                + "      END "
                + "  END AS DOC_PROPIETARIO, "
                + "  TRIM(P.APE_PAT "
                + "  || ' ' "
                + "  || P.APE_MAT "
                + "  || ' ' "
                + "  || P.NOMBRES "
                + "  || ' ' "
                + "  || P.RZN_SOCIAL) AS NOMBRES, "
                + "  MODA.NOMBRE      AS MODALIDAD, "
                + "  RES.NOMBRE       AS RESTRICCION, "
                + "  EST.NOMBRE       AS ESTADO, "
                + "  T.FECHA_EMISION, "
                + "  T.ACTIVO, "
                + "  T.HASH_QR "
                + "FROM BDINTEGRADO.AMA_TARJETA_PROPIEDAD T "
                + "LEFT JOIN BDINTEGRADO.AMA_ARMA AR ON AR.ID = T.ARMA_ID "
                + "INNER JOIN BDINTEGRADO.AMA_MODELOS MODL ON MODL.ID = AR.MODELO_ID "
                + "LEFT JOIN BDINTEGRADO.ama_modelo_calibre CA ON MODL.ID=ca.modelo_id "
                + "INNER JOIN BDINTEGRADO.AMA_CATALOGO CAL ON CAL.ID = ca.catalogo_id "
                + "INNER JOIN BDINTEGRADO.ama_catalogo MAR ON MAR.ID = MODL.marca_id "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC SIT ON SIT.ID = AR.SITUACION_ID "
                + "INNER JOIN BDINTEGRADO.AMA_CATALOGO TARM ON TARM.ID = modl.tipo_arma_id "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC EST ON EST.ID = T.ESTADO_ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC RES ON RES.ID = T.RESTRICCION_ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC MODA ON MODA.ID = T.MODALIDAD_ID "
                + "INNER JOIN BDINTEGRADO.SB_PERSONA P ON P.ID = T.PERSONA_COMPRADOR_ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TDOC ON TDOC.ID = P.TIPO_DOC "
                + "INNER JOIN BDINTEGRADO.TIPO_BASE TPER ON TPER.ID = P.TIPO_ID "
                + "WHERE "
                + " T.HASH_QR = ?1"
                + " GROUP BY AR.NRO_RUA, TARM.NOMBRE, MAR.NOMBRE, modl.modelo, AR.SERIE, SIT.COD_PROG, SIT.NOMBRE, RUC, TPER.COD_PROG, TDOC.NOMBRE, P.NUM_DOC,"
                + " P.APE_PAT, P.APE_MAT, P.NOMBRES, P.RZN_SOCIAL, "
                + " MODA.NOMBRE, RES.NOMBRE, EST.NOMBRE, T.FECHA_EMISION, T.ACTIVO, T.HASH_QR "
        );
        q.setParameter(1, hash);
        q.setHint("eclipselink.result-type", "Map");
        //q.getResultList();
        return q.setMaxResults(1).getResultList();
    }

    public List<ArrayRecord> buscarHashGamacLic(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT L.NRO_LICENCIA AS NRO_LIC, "
                + "  P.APE_PAT "
                + "  || ' ' "
                + "  || P.APE_MAT "
                + "  || ' ' "
                + "  || P.NOMBRES         AS PORTADOR, "
                + "  L.HASH_QR            AS HASH, "
                + "  L.FECHA_EMISION      AS FEC_EMISION, "
                + "  L.FECHA_VENCIMIETNTO AS FEC_VENCIMIENTO, "
                + "  TG.NOMBRE            AS ESTADO, "
                + "  TB.NOMBRE "
                + "  || ' ' "
                + "  || P.NUM_DOC          AS DOC_PROPIETARIO, "
                + "  LISTAGG(TG1.NOMBRE) AS MODALIDADES "
                + "FROM BDINTEGRADO.AMA_LICENCIA_DE_USO L "
                + "LEFT JOIN BDINTEGRADO.SB_PERSONA P "
                + "ON P.ID = L.PERSONA_LICENCIA_ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TG "
                + "ON TG.ID = L.ESTADO_ID "
                + "INNER JOIN BDINTEGRADO.TIPO_BASE TB "
                + "ON TB.ID = P.TIPO_DOC "
                + "INNER JOIN BDINTEGRADO.AMA_TIPO_LICENCIA ATL "
                + "ON L.ID = ATL.LICENCIA_ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TG1 "
                + "ON TG1.ID = ATL.MODALIDAD_ID "
                + "  WHERE "
                + "  L.HASH_QR = ?1 AND ATL.ACTIVO = 1  "
                + "GROUP BY L.NRO_LICENCIA, "
                + "  L.HASH_QR, "
                + "  L.FECHA_EMISION, "
                + "  L.FECHA_VENCIMIETNTO, "
                + "  TG.NOMBRE, "
                + "  P.NUM_DOC, "
                + "  TB.NOMBRE, "
                + "  P.NOMBRES, "
                + "  P.APE_PAT, "
                + "  P.APE_MAT, "
                + "  L.ESTADO_ID"
        );
        q.setParameter(1, hash);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(1).getResultList();
    }

    public List<ArrayRecord> buscarHashLic(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT A.*, B.NUMERO "
                + "FROM "
                + "RMA1369.WS_LICENCIAS_CUBO@SUCAMEC A "
                + "INNER JOIN TRAMDOC.EXPEDIENTE B ON A.NRO_EXP || '-' || A.ANO_EXP = B.NUMERO_DISCA "
                + "INNER JOIN RMA1369.AM_HASH@SUCAMEC C ON A.NRO_LIC = C.NRO_LIC AND B.NUMERO = C.EXPEDIENTE   "
                + "WHERE "
                + "C.HASH = ?1"
        );
        q.setParameter(1, hash);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(1).getResultList();
    }

    public List<ArrayRecord> buscarHashLuaspeGamac(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT A.*, B.NUMERO "
                + "FROM RMA1369.WS_LUASPE@SUCAMEC A "
                + "INNER JOIN TRAMDOC.EXPEDIENTE B ON A.NRO_EXP || '-' || A.ANO_EXP = B.NUMERO_DISCA "
                + "WHERE HASH_QR = ?1"
        );
        q.setParameter(1, hash);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(1).getResultList();
    }

    public List<ArrayRecord> buscarHashCarGssp(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT A.*, B.NUMERO "
                + "FROM RMA1369.WS_VIGILANTES@SUCAMEC A "
                + "INNER JOIN TRAMDOC.EXPEDIENTE B ON A.NRO_EXP || '-' || A.ANO_EXP = B.NUMERO_DISCA "
                + "WHERE A.HASH_QR = ?1"
        );
        q.setParameter(1, hash);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(1).getResultList();
    }

    public List<ArrayRecord> buscarHashCarGsspHis(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.SS_EMP_VIG_HIS@SUCAMEC WHERE HASH_QR = ?1 "
        );
        q.setParameter(1, hash);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(1).getResultList();
    }

    public List<ArrayRecord> buscarHashCMP(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT R.ID AS REGISTRO_ID, C.ID, C.NRO_CARNE, C.LICENCIA_CONDUCTOR, C.FECHA_EMISION, C.FECHA_VENCIMIENTO,"
                + " CASE WHEN TP.COD_PROG = 'TP_PER_JUR' THEN TD.NOMBRE || ' ' || P.RUC ELSE TD.NOMBRE || ' '|| P.NUM_DOC END AS DOC_PROPIETARIO,"
                + " TRIM(P.APE_PAT || ' ' || P.APE_MAT || ' ' || P.NOMBRES) AS NOMBRES,"
                + " TA.NOMBRE AS ACTIVIDAD, (CASE WHEN TOPE.COD_PROG = 'TP_OPE_CAN' THEN 'CANCELADO' WHEN TOPE.COD_PROG = 'TP_OPE_COF' THEN 'CANCELADO' WHEN SYSDATE <= FECHA_VENCIMIENTO THEN 'VIGENTE' ELSE 'VENCIDA' END) AS ESTADO"
                + " FROM BDINTEGRADO.EPP_CARNE C"
                + " INNER JOIN BDINTEGRADO.EPP_REGISTRO R ON R.ID = C.REGISTRO_ID "
                + " INNER JOIN BDINTEGRADO.SB_PERSONA P ON C.PERSONA_ID = P.ID"
                + " INNER JOIN BDINTEGRADO.TIPO_BASE TP ON TP.ID = P.TIPO_ID"
                + " INNER JOIN BDINTEGRADO.TIPO_BASE TD ON TD.ID = P.TIPO_DOC"
                + " INNER JOIN BDINTEGRADO.TIPO_BASE TPRO ON TPRO.ID = R.TIPO_PRO_ID "        
                + " INNER JOIN BDINTEGRADO.TIPO_BASE TOPE ON TOPE.ID = R.TIPO_OPE_ID "
                + " INNER JOIN BDINTEGRADO.TIPO_EXPLOSIVO TE ON TE.ID = R.ESTADO "
                + " LEFT JOIN BDINTEGRADO.TIPO_EXPLOSIVO TA ON TA.ID = C.TIPO_ACTIVIDAD_ID"
                + " WHERE C.HASH_QR = ?1 AND TPRO.COD_PROG = 'TP_PIRO_MANI' AND TOPE.COD_PROG != 'TP_OPE_CAN' AND TE.COD_PROG IN ('TP_REGEV_FIN', 'TP_REGEV_ANU') "
                + " UNION "
                + " SELECT R.ID AS REGISTRO_ID, C.ID, C.NRO_CARNE, C.LICENCIA_CONDUCTOR, C.FECHA_EMISION, C.FECHA_VENCIMIENTO,"
                + " CASE WHEN TP.COD_PROG = 'TP_PER_JUR' THEN TD.NOMBRE || ' ' || P.RUC ELSE TD.NOMBRE || ' '|| P.NUM_DOC END AS DOC_PROPIETARIO,"
                + " TRIM(P.APE_PAT || ' ' || P.APE_MAT || ' ' || P.NOMBRES) AS NOMBRES,"
                + " TA.NOMBRE AS ACTIVIDAD, (CASE WHEN TOPE.COD_PROG = 'TP_OPE_CAN' THEN 'CANCELADO' WHEN TOPE.COD_PROG = 'TP_OPE_COF' THEN 'CANCELADO' WHEN SYSDATE <= FECHA_VENCIMIENTO THEN 'VIGENTE' ELSE 'VENCIDA' END) AS ESTADO"
                + " FROM BDINTEGRADO.EPP_REGISTRO R "
                + " INNER JOIN BDINTEGRADO.EPP_REGISTRO RM ON RM.ID = R.REGISTRO_ID "
                + " INNER JOIN BDINTEGRADO.EPP_CARNE C ON C.REGISTRO_ID = RM.ID   "
                + " INNER JOIN BDINTEGRADO.SB_PERSONA P ON C.PERSONA_ID = P.ID"
                + " INNER JOIN BDINTEGRADO.TIPO_BASE TP ON TP.ID = P.TIPO_ID"
                + " INNER JOIN BDINTEGRADO.TIPO_BASE TD ON TD.ID = P.TIPO_DOC"
                + " INNER JOIN BDINTEGRADO.TIPO_BASE TPRO ON TPRO.ID = R.TIPO_PRO_ID "        
                + " INNER JOIN BDINTEGRADO.TIPO_BASE TOPE ON TOPE.ID = R.TIPO_OPE_ID "
                + " INNER JOIN BDINTEGRADO.TIPO_EXPLOSIVO TE ON TE.ID = R.ESTADO "
                + " LEFT JOIN BDINTEGRADO.TIPO_EXPLOSIVO TA ON TA.ID = C.TIPO_ACTIVIDAD_ID"
                + " WHERE C.HASH_QR = ?1 AND TPRO.COD_PROG = 'TP_PIRO_MANI' AND TOPE.COD_PROG = 'TP_OPE_CAN' AND TE.COD_PROG IN ('TP_REGEV_FIN', 'TP_REGEV_ANU') "
                + " ORDER BY REGISTRO_ID DESC "
        );
        q.setParameter(1, hash.trim());
        q.setHint("eclipselink.result-type", "Map");

        return q.setMaxResults(1).getResultList();
    }

    public List<ArrayRecord> buscarHashLME(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT DECODE(L.ACTIVO,1,'ACTIVO','INACTIVO') AS ACTIVO, L.ID, L.NRO_LICENCIA, L.FEC_EMI, L.FEC_VENC,\n" +
"               CASE WHEN TP.COD_PROG = 'TP_PER_JUR' THEN TD.NOMBRE || ' ' || P.RUC ELSE TD.NOMBRE || ' '|| P.NUM_DOC END AS DOC_PROPIETARIO,\n" +
"               TRIM(P.APE_PAT || ' ' || P.APE_MAT || ' ' || P.NOMBRES) AS NOMBRES,\n" +
"               CASE WHEN TP2.COD_PROG = 'TP_PER_JUR' THEN TD2.NOMBRE || ' ' || P2.RUC ELSE TD2.NOMBRE || ' '|| P2.NUM_DOC END AS DOC_EMPRESA,\n" +
"               CASE WHEN TP2.COD_PROG = 'TP_PER_JUR' THEN TRIM(P2.RZN_SOCIAL) ELSE TRIM(P2.APE_PAT || ' ' || P2.APE_MAT || ' ' || P2.NOMBRES) END AS RZN_SOCIAL,\n" +
"                (CASE WHEN SYSDATE <= L.FEC_VENC THEN 'VIGENTE' ELSE 'VENCIDA' END) AS ESTADO, L.HASH_QR\n" +
"                FROM BDINTEGRADO.EPP_LICENCIA L\n" +
"                INNER JOIN BDINTEGRADO.SB_PERSONA P ON L.PERSONA_ID = P.ID\n" +
"                INNER JOIN BDINTEGRADO.TIPO_BASE TP ON TP.ID = P.TIPO_ID\n" +
"                INNER JOIN BDINTEGRADO.TIPO_BASE TD ON TD.ID = P.TIPO_DOC\n" +
"                INNER JOIN BDINTEGRADO.EPP_REGISTRO REG ON REG.ID = L.REGISTRO_ID\n" +
"                LEFT JOIN BDINTEGRADO.SB_PERSONA P2 ON REG.EMPRESA_ID = P2.ID\n" +
"                LEFT JOIN BDINTEGRADO.TIPO_BASE TP2 ON TP2.ID = P2.TIPO_ID\n" +
"                LEFT JOIN BDINTEGRADO.TIPO_BASE TD2 ON TD2.ID = P2.TIPO_DOC "
                //+ " WHERE L.ACTIVO = 1 AND L.HASH_QR = ?1 "
                + " WHERE L.HASH_QR = ?1 and L.DIGITAL is null"
        );
        q.setParameter(1, hash.trim());
        q.setHint("eclipselink.result-type", "Map");

        return q.setMaxResults(1).getResultList();
    }
    public List<ArrayRecord> buscarHashLMED(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT DECODE(L.ACTIVO,1,'ACTIVO','INACTIVO') AS ACTIVO, L.ID, L.NRO_LICENCIA, L.FEC_EMI, L.FEC_VENC,\n" +
"               CASE WHEN TP.COD_PROG = 'TP_PER_JUR' THEN TD.NOMBRE || ' ' || P.RUC ELSE TD.NOMBRE || ' '|| P.NUM_DOC END AS DOC_PROPIETARIO,\n" +
"               TRIM(P.APE_PAT || ' ' || P.APE_MAT || ' ' || P.NOMBRES) AS NOMBRES,\n" +
"               CASE WHEN TP2.COD_PROG = 'TP_PER_JUR' THEN TD2.NOMBRE || ' ' || P2.RUC ELSE TD2.NOMBRE || ' '|| P2.NUM_DOC END AS DOC_EMPRESA,\n" +
"               CASE WHEN TP2.COD_PROG = 'TP_PER_JUR' THEN TRIM(P2.RZN_SOCIAL) ELSE TRIM(P2.APE_PAT || ' ' || P2.APE_MAT || ' ' || P2.NOMBRES) END AS RZN_SOCIAL,\n" +
"                (CASE WHEN SYSDATE <= L.FEC_VENC THEN 'VIGENTE' ELSE 'VENCIDA' END) AS ESTADO, L.HASH_QR\n" +
"                FROM BDINTEGRADO.EPP_LICENCIA L\n" +
"                INNER JOIN BDINTEGRADO.SB_PERSONA P ON L.PERSONA_ID = P.ID\n" +
"                INNER JOIN BDINTEGRADO.TIPO_BASE TP ON TP.ID = P.TIPO_ID\n" +
"                INNER JOIN BDINTEGRADO.TIPO_BASE TD ON TD.ID = P.TIPO_DOC\n" +
"                INNER JOIN BDINTEGRADO.EPP_REGISTRO REG ON REG.ID = L.REGISTRO_ID\n" +
"                LEFT JOIN BDINTEGRADO.SB_PERSONA P2 ON REG.EMPRESA_ID = P2.ID\n" +
"                LEFT JOIN BDINTEGRADO.TIPO_BASE TP2 ON TP2.ID = P2.TIPO_ID\n" +
"                LEFT JOIN BDINTEGRADO.TIPO_BASE TD2 ON TD2.ID = P2.TIPO_DOC "
                //+ " WHERE L.ACTIVO = 1 AND L.HASH_QR = ?1 "
                + " WHERE L.HASH_QR = ?1  and L.DIGITAL=1"
        );
        q.setParameter(1, hash.trim());
        q.setHint("eclipselink.result-type", "Map");

        return q.setMaxResults(1).getResultList();
    }
    public List<ArrayRecord> buscarHashLMPP(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT DECODE(L.ACTIVO,1,'ACTIVO','INACTIVO') AS ACTIVO, L.ID, L.NRO_CARNE, L.FECHA_EMISION, L.FECHA_VENCIMIENTO,\n" +
"               CASE WHEN TP.COD_PROG = 'TP_PER_JUR' THEN TD.NOMBRE || ' ' || P.RUC ELSE TD.NOMBRE || ' '|| P.NUM_DOC END AS DOC_PROPIETARIO,\n" +
"               TRIM(P.APE_PAT || ' ' || P.APE_MAT || ' ' || P.NOMBRES) AS NOMBRES,\n" +
"               CASE WHEN TP2.COD_PROG = 'TP_PER_JUR' THEN TD2.NOMBRE || ' ' || P2.RUC ELSE TD2.NOMBRE || ' '|| P2.NUM_DOC END AS DOC_EMPRESA,\n" +
"               CASE WHEN TP2.COD_PROG = 'TP_PER_JUR' THEN TRIM(P2.RZN_SOCIAL) ELSE TRIM(P2.APE_PAT || ' ' || P2.APE_MAT || ' ' || P2.NOMBRES) END AS RZN_SOCIAL,\n" +
"                (CASE WHEN SYSDATE <= L.FECHA_VENCIMIENTO THEN 'VIGENTE' ELSE 'VENCIDA' END) AS ESTADO, L.HASH_QR\n" +
"                FROM BDINTEGRADO.EPP_CARNE L\n" +
"                INNER JOIN BDINTEGRADO.SB_PERSONA P ON L.PERSONA_ID = P.ID\n" +
"                INNER JOIN BDINTEGRADO.TIPO_BASE TP ON TP.ID = P.TIPO_ID\n" +
"                INNER JOIN BDINTEGRADO.TIPO_BASE TD ON TD.ID = P.TIPO_DOC\n" +
"                INNER JOIN BDINTEGRADO.EPP_REGISTRO REG ON REG.ID = L.REGISTRO_ID\n" +
"                LEFT JOIN BDINTEGRADO.SB_PERSONA P2 ON REG.EMPRESA_ID = P2.ID\n" +
"                LEFT JOIN BDINTEGRADO.TIPO_BASE TP2 ON TP2.ID = P2.TIPO_ID\n" +
"                LEFT JOIN BDINTEGRADO.TIPO_BASE TD2 ON TD2.ID = P2.TIPO_DOC "
                //+ " WHERE L.ACTIVO = 1 AND L.HASH_QR = ?1 "
                + " WHERE L.HASH_QR = ?1  AND L.DIGITAL=1"
        );
        q.setParameter(1, hash.trim());
        q.setHint("eclipselink.result-type", "Map");

        return q.setMaxResults(1).getResultList();
    }

    public Map buscarHashGamacConstValWeb(String p_subqr, String p_nro, String p_anio) {
        String sql = "select TO_CHAR(r.fecha_emision, 'YYYY/MM') PATH, "
                + " TO_CHAR(r.fecha_emision, 'YYYYMMDDHH24MI') || '_' || TO_CHAR(r.PERSONA_ID) AS NOMBRE "
                + " from bdintegrado.sb_constancias r"
                + " where UPPER(r.hash_qr) like ?1"
                + " and r.nro_constancia = ?2"
                + " and TO_CHAR(r.fecha_emision,'YYYY') = ?3"
                + " and r.activo = 1 ";
        //Syso(sql);
        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr.trim().toUpperCase() + "%");
        q.setParameter(2, Long.valueOf(p_nro));
        q.setParameter(3, p_anio);
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return (Map) res.get(0);
        }
        return null;
    }

    public Map buscarHashGamacConstVerWeb(String p_subqr, String p_nro, String p_anio) {
        String sql = "select TO_CHAR(r.fecha_emision, 'YYYY/MM') PATH, "
                + " TO_CHAR(r.fecha_emision, 'YYYYMMDDHH24MI') || '_' || COALESCE(TO_CHAR(r.REFERENCIA_ID), '') || '_' || TO_CHAR(r.PERSONA_ID) AS NOMBRE, "
                + " TO_CHAR(r.fecha_emision, 'YYYYMMDDHH24MI') || '_' || TO_CHAR(r.PERSONA_ID) || '_' || COALESCE(TO_CHAR(r.REFERENCIA_ID), '') AS NOMBRE_OPCION2 "
                + " from bdintegrado.sb_constancias r"
                + " where UPPER(r.hash_qr) like ?1"
                + " and r.nro_constancia = ?2"
                + " and TO_CHAR(r.fecha_emision,'YYYY') = ?3"
                + " and r.activo = 1 ";
        //Syso(sql);
        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr.trim().toUpperCase() + "%");
        q.setParameter(2, Long.valueOf(p_nro));
        q.setParameter(3, p_anio);
        /*System.out.println("sql Sel QR: " + sql);
        System.out.println("p_subqr: " + p_subqr.trim().toUpperCase() + "%");
        System.out.println("p_nro: " + p_nro);
        System.out.println("p_anio: " + p_anio);*/
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return (Map) res.get(0);
        }
        return null;
    }

    public Map buscarHashGamacConstExamenPoligonoVerWeb(String p_subqr, String p_nro, String p_anio) {
        String sql = "select TO_CHAR(r.fecha_emision, 'YYYY/MM') PATH, "
                + " TO_CHAR(r.fecha_emision, 'YYYYMMDDHH24MI') || '_' || COALESCE(TO_CHAR(r.REFERENCIA_ID), '') || '_' || TO_CHAR(r.PERSONA_ID) AS NOMBRE "
                + " from bdintegrado.sb_constancias r"
                + " where UPPER(r.hash_qr) like ?1"
                + " and r.nro_constancia = ?2"
                + " and TO_CHAR(r.fecha_emision,'YYYY') = ?3"
                + " and r.tipo_constancia_id=(SELECT id FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_CONST_EXATEO_POL')"
                + " and r.activo = 1 ";
        //Syso(sql);
        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_subqr.trim().toUpperCase() + "%");
        q.setParameter(2, Long.valueOf(p_nro));
        q.setParameter(3, p_anio);
        /*System.out.println("sql Sel QR: " + sql);
        System.out.println("p_subqr: " + p_subqr.trim().toUpperCase() + "%");
        System.out.println("p_nro: " + p_nro);
        System.out.println("p_anio: " + p_anio);*/
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return (Map) res.get(0);
        }
        return null;
    }
    
            
    public List<Map> buscarHashActaDepDev(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT GT.NRO_GUIA,TGUIA.COD_PROG AS CTP_GUIA, TGUIA.NOMBRE AS TP_GUIA, IA.ID AS ID_ARMA,IA.SERIE, TPARMA.NOMBRE AS TARMA, MARCA.NOMBRE AS MARMA,MOD.MODELO AS MODARMA,LISTAGG(CAL.NOMBRE, '/') WITHIN GROUP (ORDER BY MOD.ID) AS CALARMA, "
                + "MUN.ID AS ID_MUNI, MUN.DENOMINACION_ID AS DENMUNI,MUN.MODELO AS MODMUNI, MARCAMUN.NOMBRE AS MMUNI, CALMUN.NOMBRE AS CALMUNI, TMUNI.NOMBRE AS TMUNI,GM.CANTIDAD AS CMUNI, UMED.NOMBRE AS UMEDIDA, GM.PESO AS PMUNI, "
                + "ACC.ID AS ID_ACCES, ACC.CANTIDAD, TACC.NOMBRE AS TACCES, ART.NOMBRE AS ACCES, "
                + "CASE WHEN IA.ID IS NOT NULL THEN 1 WHEN MUN.ID IS NOT NULL THEN 2 WHEN ACC.ID IS NOT NULL THEN 3 ELSE 0 END AS FLAG "
                + "FROM BDINTEGRADO.AMA_GUIA_TRANSITO GT "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TGUIA ON (TGUIA.ID=GT.TIPO_GUIA_ID) "
                + //ARMA
                "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO GTI ON (GT.ID=GTI.GAMAC_GUIA_TRANSITO_ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_INVENTARIO_ARMA IA ON (GTI.INVENTARIO_ARMA_ID=IA.ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_MODELOS MOD ON (IA.MODELO_ID=MOD.ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO TPARMA ON (TPARMA.ID=MOD.TIPO_ARMA_ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO MARCA ON (MARCA.ID=MOD.MARCA_ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MCAL ON (MCAL.MODELO_ID=MOD.ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO CAL ON (CAL.ID=MCAL.CATALOGO_ID) "
                + //MUNICIONES
                "LEFT JOIN BDINTEGRADO.AMA_GUIA_MUNICIONES GM ON (GM.GUIA_TRANSITO_ID = GT.ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_MUNICION MUN ON (MUN.ID=GM.MUNICION_ID) "
                + "LEFT JOIN BDINTEGRADO.UNIDAD_MEDIDA UMED ON (GM.UMEDIDA_ID=UMED.ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO CALMUN ON (CALMUN.ID=MUN.CALIBREARMA_ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO MARCAMUN ON (MARCAMUN.ID=MUN.MARCA_ID) "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TMUNI ON (TMUNI.ID=MUN.TIPO_MUNICION_ID) "
                + //ACCESORIOS
                "LEFT JOIN BDINTEGRADO.AMA_INVENTARIO_ACCESORIOS ACC ON (ACC.GUIA_TRANSITO_ID=GT.ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_INVENTARIO_ARTCONEXO ART ON (ART.ID=ACC.ARTICULO_CONEXO_ID) "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TACC ON (TACC.ID=ART.TIPO_ID) "
                //GENERAL
                + "WHERE GT.ACTIVO=1 AND GT.ESTADO_ID = 392 AND GT.HASH_QR = ?1 "
                + "AND TGUIA.ID IN (SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE TIPO_ID = 195 OR TIPO_ID = 196 OR TIPO_ID = 12637) "
                + "AND (CASE WHEN IA.ID IS NOT NULL AND IA.ACTIVO =1 THEN 1 WHEN MUN.ID IS NOT NULL AND MUN.ACTIVO=1 THEN 1 WHEN ACC.ID IS NOT NULL AND ACC.ACTIVO = 1 THEN 1 ELSE 0 END)=1 "
                + "GROUP BY GT.NRO_GUIA,TGUIA.COD_PROG, TGUIA.NOMBRE,IA.ID, IA.SERIE, TPARMA.NOMBRE, MARCA.NOMBRE,MOD.MODELO, "
                + "MUN.ID,MUN.DENOMINACION_ID,MUN.MODELO, MARCAMUN.NOMBRE, CALMUN.NOMBRE, TMUNI.NOMBRE,GM.CANTIDAD, UMED.NOMBRE, GM.PESO, "
                + "ACC.ID, ACC.CANTIDAD, TACC.NOMBRE, ART.NOMBRE"
        );
        q.setParameter(1, hash.trim());
        q.setHint("eclipselink.result-type", "Map");

        return q.getResultList();
    }
    public List<Map> buscarHashActaPevam(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT TDESTADO.nombre as ESTADO, A.NRO_ACTA, TACTA.NOMBRE AS TIPO_ACTA,A.SERIE, A.TIPO_ARMA, A.MARCA,A.MODELO,A.CALIBRE,A.FECHA_INTERNAMIENTO,"
                + "TDOCENT.NOMBRE AS TP_DOC_ENT,A.PER_ENT_NUM_DOC,(PER_ENT_NOMBRES||' '||PER_ENT_APE_PAT||' '||PER_ENT_APE_MAT) as PERSONA_ENTREGA,"   
                + "'LOS SIGUIENTES DATOS CORRESPONDEN CON UN ACTA DE INTERNAMIENTO VÁLIDA QUE OTORGA EL DERECHO A INSENTIVO SEGÚN LO ESTABLECIDO EN EL PEVAM' AS TIPO  "        
                + "FROM BDINTEGRADO.AMA_PEVAM A "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TACTA ON (TACTA.ID=A.TIPO_ACTA_ID) "
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TDOCENT ON (TDOCENT.ID=A.PER_ENT_TIPO_DOC_ID) "         
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TDESTADO ON (TDESTADO.ID=A.ESTADO_ID) "                
                //GENERAL
                + "WHERE A.ACTIVO=1 AND A.HASH_QR = ?1 "        
        );
        q.setParameter(1, hash.trim());
        q.setHint("eclipselink.result-type", "Map");

        return q.getResultList();
    }
    public List<Map> buscarHashActaPevamMuni(String hash) {
        hash = hash == null ? "" : hash;
         Query q = em.createNativeQuery(
                "SELECT TDESTADO.nombre as ESTADO, A.NRO_ACTA, TACTA.NOMBRE AS TIPO_ACTA,A.SERIE, A.TIPO_ARMA, A.MARCA,A.MODELO,A.CALIBRE,A.FECHA_INTERNAMIENTO,"
                + "TDOCENT.NOMBRE AS TP_DOC_ENT,A.PER_ENT_NUM_DOC,(PER_ENT_NOMBRES||' '||PER_ENT_APE_PAT||' '||PER_ENT_APE_MAT) as PERSONA_ENTREGA,"   
                + "CALMUN.NOMBRE AS CALMUNI,UMED.NOMBRE AS UMEDIDA,GM.MARCA||' '||GM.DENOMINACION||' '||GM.NRO_PERDIGON AS DENOMINACION,GM.CANTIDAD,GM.PESO,"
                + "'LOS SIGUIENTES DATOS CORRESPONDEN CON UN ACTA DE INTERNAMIENTO VÁLIDA QUE OTORGA EL DERECHO A INSENTIVO SEGÚN LO ESTABLECIDO EN EL PEVAM' AS TIPO  "        
                + "FROM BDINTEGRADO.AMA_PEVAM A "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TACTA ON (TACTA.ID=A.TIPO_ACTA_ID) "
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TDOCENT ON (TDOCENT.ID=A.PER_ENT_TIPO_DOC_ID) "
                + //MUNICIONES
                "LEFT JOIN BDINTEGRADO.AMA_PEVAM_MUNICIONES GM ON (GM.PEVAM_ID = A.ID) "                
                + "LEFT JOIN BDINTEGRADO.UNIDAD_MEDIDA UMED ON (GM.UMEDIDA_ID=UMED.ID) "
                + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO CALMUN ON (CALMUN.ID=GM.CALIBREARMA_ID) "        
                //GENERAL
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TDESTADO ON (TDESTADO.ID=A.ESTADO_ID) " 
                + "WHERE A.ACTIVO=1 AND A.HASH_QR = ?1 "
                
        );
        
        q.setParameter(1, hash.trim());
        q.setHint("eclipselink.result-type", "Map");

        return q.getResultList();
    }

    /**
     *
     * @param hash
     * @return
     */
    public String buscarHashGamacGuia(String hash) {
        javax.persistence.Query q
                = em.createNativeQuery("select gt.id from bdintegrado.ama_guia_transito gt where gt.hash_qr = ?1 and gt.activo = 1 "
                        + "and gt.tipo_guia_id in ((select id from bdintegrado.tipo_gamac where activo = 1 "
                        + "connect by prior id=tipo_id start with cod_prog='TP_GTGAMAC' "
                        + "minus "
                        + "select id from bdintegrado.tipo_gamac where activo = 1 "
                        + "connect by prior id=tipo_id start with cod_prog in ('TP_GTGAMAC_ALM','TP_GTGAMAC_DEV')) "
                        + "union select id from bdintegrado.tipo_gamac where activo = 1 "
                        + "connect by prior id=tipo_id start with cod_prog in ('TP_ACTMOV')) ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, hash);
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return res.get(0).get("ID").toString();
        }
        return null;
    }

    public String obtenerTipoGuia(String hash) {
        javax.persistence.Query q
                = em.createNativeQuery("select gt.tipo_guia_id from bdintegrado.ama_guia_transito gt where gt.hash_qr = ?1 and gt.activo = 1 ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, hash);
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return res.get(0).get("TIPO_GUIA_ID").toString();
        }
        return null;
    }

    public Map buscarConstanciaConstanciaValWeb(String hash) {
        javax.persistence.Query q
                = em.createNativeQuery("select TO_CHAR(r.fecha_emision, 'YYYY/MM') PATH, "
                        + " TO_CHAR(r.fecha_emision, 'YYYYMMDDHH24MI') || '_' || TO_CHAR(r.PERSONA_ID) AS NOMBRE "
                        + " from BDINTEGRADO.SB_CONSTANCIAS r where r.hash_qr like ?1 ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, hash);
        List<Map> res = q.setMaxResults(1).getResultList();
        if (res.size() == 1) {
            return (Map) res.get(0);
        }
        return null;
    }

    public Map buscarConstanciaConstanciaVerWeb(String hash) {
        javax.persistence.Query q
                = em.createNativeQuery("select TO_CHAR(r.fecha_emision, 'YYYY/MM') PATH, "
                        + " TO_CHAR(r.fecha_emision, 'YYYYMMDDHH24MI') || '_' || COALESCE(TO_CHAR(r.REFERENCIA_ID), '') || '_' || TO_CHAR(r.PERSONA_ID) AS NOMBRE, "
                        + " TO_CHAR(r.fecha_emision, 'YYYYMMDDHH24MI') || '_' || TO_CHAR(r.PERSONA_ID) || '_' || COALESCE(TO_CHAR(r.REFERENCIA_ID), '') AS NOMBRE_OPCION2 "
                        + " from BDINTEGRADO.SB_CONSTANCIAS r where r.hash_qr like ?1"
                        + " and ("
                        + " r.tipo_constancia_id=(SELECT id FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_CONST_VER')"
                        + "  OR r.tipo_constancia_id=(SELECT id FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_NUM_CONST_VER_ARM')"
                        + " )"
                        + " and r.activo = 1");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, hash);
        List<Map> res = q.setMaxResults(1).getResultList();
        if (res.size() == 1) {
            return (Map) res.get(0);
        }
        return null;
    }

    public Map buscarConstanciaVerExamenPoligono(String hash) {
        javax.persistence.Query q
                = em.createNativeQuery("select TO_CHAR(r.fecha_emision, 'YYYY/MM') PATH, "
                        + " TO_CHAR(r.fecha_emision, 'YYYYMMDDHH24MI') || '_' || COALESCE(TO_CHAR(r.REFERENCIA_ID), '') || '_' || TO_CHAR(r.PERSONA_ID) AS NOMBRE "
                        + " from BDINTEGRADO.SB_CONSTANCIAS r where r.hash_qr like ?1"
                        + " and r.tipo_constancia_id=(SELECT id FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_CONST_EXATEO_POL')"
                        + " and r.activo = 1");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, hash.substring(0,24) + "%");
        List<Map> res = q.setMaxResults(1).getResultList();
        if (res.size() == 1) {
            return (Map) res.get(0);
        }
        return null;
    }
       
    public Map buscarHashGsspCarteraClientes(String hash) {
        if (hash != null) {
            //TODO: Debe ser reemplazado por entidades //
            javax.persistence.Query q
                    = em.createNativeQuery("select r.id as ID, e.nombre as ESTADO from BDINTEGRADO.SSP_CARTERA_CLIENTE r "
                            + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD E ON r.ESTADO_ID = E.id "
                            + " where r.hash_qr = ?1");
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, hash);
            List<Map> res = (List) q.setMaxResults(10).getResultList();
            if (res.size() == 1) {
                return (Map) res.get(0);
            }
        }
        return null;
    }

    public Map buscarHashGsspProgramaciónCursos(String hash) {
        if (hash != null) {
            //TODO: Debe ser reemplazado por entidades //
            javax.persistence.Query q
                    = em.createNativeQuery("SELECT r.ID, e.COD_PROG as ESTADO, (CASE WHEN r.hash_qr = ?1 THEN 1 ELSE 2 END) AS TIPO_HASH "
                            + " FROM BDINTEGRADO.SSP_REGISTRO_CURSO r "
                            + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD E ON r.ESTADO_ID = E.id "
                            + " WHERE r.hash_qr = ?1 OR r.hash_qr_const = ?1  ");
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, hash);
            List<Map> res = (List) q.setMaxResults(10).getResultList();
            if (res.size() == 1) {
                return (Map) res.get(0);
            }
        }
        return null;
    }

    public List<ArrayRecord> buscarHashCarGsspMigrado(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT A.*, B.NUMERO "
                + "FROM BDINTEGRADO.WS_VIGILANTES A "
                + "INNER JOIN TRAMDOC.EXPEDIENTE B ON A.EXPEDIENTE = B.NUMERO "
                + "WHERE A.HASH_QR = ?1"
        );
        q.setParameter(1, hash);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(1).getResultList();
    }

    public List<ArrayRecord> buscarHashCarGsspHisMigrado(String hash) {
        hash = hash == null ? "" : hash;
        Query q = em.createNativeQuery(
                "SELECT * FROM BDINTEGRADO.WS_VIGILANTES_HIS WHERE HASH_QR = ?1 "
        );
        q.setParameter(1, hash);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(1).getResultList();
    }
    
    public String buscarHashReportesyConstancias(String p_hash) {
        if (p_hash == null) {
            p_hash = "";
        }

        String jpaQ = "SELECT REP.NOMBRE_ARCHIVO FROM BDINTEGRADO.SSP_REPORTE REP\n" +
                        " INNER JOIN BDINTEGRADO.SSP_REGISTRO REG ON REG.ID = REP.REGISTRO_ID\n" +
                        " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD E ON E.ID = REG.ESTADO_ID \n" +
                        " WHERE REP.ACTIVO = 1 AND REG.ACTIVO = 1 AND E.COD_PROG != 'TP_ECC_ANU'\n" +
                        " AND REP.HASH_QR = ?1 ";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash.trim());
        res = q.getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("NOMBRE_ARCHIVO").toString();
        }
    }
    public String buscarHashNotQR(String p_hash) {  
        String jpaQ = "SELECT A.NOMBRE NOMBRE FROM BDINTEGRADO.NE_ARCHIVO A INNER JOIN BDINTEGRADO.NE_DOCUMENTO D ON D.ID = A.DOCUMENTO_ID " +
                "INNER JOIN BDINTEGRADO.TIPO_BASE TA ON TA.ID = A.TIPO_ID AND TA.COD_PROG = 'TP_ADJ_FIR' " +
                "WHERE A.ACTIVO = 1 AND D.ACTIVO = 1 AND D.ESTADO_ID IN (21,22,23) " +
                "AND UPPER(D.HASH_QR) LIKE ?1 "+
                " ORDER BY a.id desc";
        //System.out.println(jpaQ);
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res;
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash.trim().toUpperCase() + "%");
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            Map m = res.get(0);
            return m.get("NOMBRE").toString();
        }
    }
    
    public Map buscarHashCertImp(String p_hash, String p_num, String p_anio) {
        if (p_hash == null) {
            p_hash = "";
        }
        String jpaQ = "SELECT A.NOMBRE NOMBRE,D.HASH_QR HASH_QR FROM BDINTEGRADO.NE_ARCHIVO A INNER JOIN BDINTEGRADO.NE_DOCUMENTO D ON D.ID = A.DOCUMENTO_ID " +
                "INNER JOIN BDINTEGRADO.TIPO_BASE TA ON TA.ID = A.TIPO_ID AND TA.COD_PROG = 'TP_ADJ_FIR' " +
                "WHERE A.ACTIVO = 1 AND D.ACTIVO = 1 AND D.ESTADO_ID IN (21,22,23) " +
                "AND UPPER(D.HASH_QR) LIKE ?1 AND D.NRO_DOC LIKE 'N%'|| ?2 || '-' || ?3 || '-SUCAMEC-GAMAC-UFNO-CAMR' "+
                "AND TO_CHAR(d.fecha_doc, 'YYYY') = ?3 ORDER BY a.id desc";
//        System.out.println(jpaQ);
//        System.out.println(p_hash);
//        System.out.println(p_num);
//        System.out.println(p_anio);
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res;
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash.trim().toUpperCase() + "%");
        q.setParameter(2, Long.valueOf(p_num));
        q.setParameter(3, p_anio);
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            return (Map) res.get(0);
        }
    }
    
    public Map buscarHashConstTempCarne(String p_hash, String p_num, String p_anio) {
        if (p_hash == null) {
            p_hash = "";
        }
        String jpaQ = "SELECT A.NOMBRE NOMBRE,D.HASH_QR HASH_QR FROM BDINTEGRADO.NE_ARCHIVO A INNER JOIN BDINTEGRADO.NE_DOCUMENTO D ON D.ID = A.DOCUMENTO_ID " +
                "INNER JOIN BDINTEGRADO.TIPO_BASE TA ON TA.ID = A.TIPO_ID AND TA.COD_PROG = 'TP_ADJ_FIR' " +
                "INNER JOIN BDINTEGRADO.TIPO_BASE TD ON TD.ID = D.TIPO_ID AND TD.COD_PROG = 'TP_DOC_CTCPS' " +
                "WHERE A.ACTIVO = 1 AND D.ACTIVO = 1 AND D.ESTADO_ID IN (21,22,23) " +
                "AND UPPER(D.HASH_QR) LIKE ?1 AND D.NRO_DOC LIKE '%'|| ?2 || '' "+
                //"AND TO_CHAR(d.fecha_doc, 'YYYY') = ?3 " +
                "ORDER BY a.id desc";
        //System.out.println(jpaQ);
        //System.out.println(p_hash);
        //System.out.println(p_num);
        //System.out.println(p_anio);
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res;
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash.trim().toUpperCase() + "%");
        q.setParameter(2, Long.valueOf(p_num));
        //q.setParameter(3, p_anio);
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            return (Map) res.get(0);
        }
    }
    public Map buscarHashOficioGamac(String p_hash, String p_num, String p_anio) {
        if (p_hash == null) {
            p_hash = "";
        }
        //Oficios GAMAC
        String jpaQ = "select A.NOMBRE NOMBRE,D.HASH_QR HASH_QR from BDINTEGRADO.NE_documento d " +
                " INNER JOIN BDINTEGRADO.NE_ARCHIVO A ON A.DOCUMENTO_ID = D.ID " +
                " INNER JOIN BDINTEGRADO.TIPO_BASE TA ON TA.ID = A.TIPO_ID AND TA.COD_PROG = 'TP_ADJ_FIR' " +
                " inner join bdintegrado.tipo_base td on td.id = d.tipo_id and UPPER(td.nombre) like '%OFICIO%'" +
                " INNER JOIN BDINTEGRADO.TIPO_BASE AR ON AR.ID = d.AREA_ID AND AR.ABREVIATURA = 'GAMAC'" +
                " where A.ACTIVO = 1 AND D.ACTIVO = 1 AND D.ESTADO_ID IN (21,22,23) " +
                " AND UPPER(d.hash_qr) like ?1 and "+
                " nro_doc like 'N%'|| ?2 || '-' || ?3 || '-SUCAMEC_GAMAC' and TO_CHAR(d.fecha_doc, 'YYYY') = ?3"+
                " ORDER BY d.id desc";
        javax.persistence.Query q = em.createNativeQuery(jpaQ);
        List<Map> res;
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, p_hash.trim().toUpperCase() + "%");
        q.setParameter(2, Long.valueOf(p_num));
        q.setParameter(3, p_anio);
        res = q.setMaxResults(10).getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            return (Map) res.get(0);
        }
    }
    
    
    
    public Map mostrarDatosResolucionGSSPAutorizaciones(String ResolucionId) {
        String sql = " SELECT " +
                    " RES.ID AS RESOLUCION_ID, " +
                    " RES.NUMERO AS RESOLUCION_NUM, " +
                    " RES.HASH_QR AS RESOLUCION_RQ, " +
                    " to_date(RES.FECHA, 'dd/mm/YYYY') as RESOLUCION_FECHA, " +
                    " to_date(RES.FECHA_INI, 'dd/mm/YYYY') as RESOLUCION_FECHAINI, " +
                    " to_date(RES.FECHA_FIN, 'dd/mm/YYYY') as RESOLUCION_FECHAFIN, " +
                    " (CASE WHEN (to_date(SYSDATE, 'dd/mm/YYYY')>= to_date(RES.FECHA_INI, 'dd/mm/YYYY') AND to_date(SYSDATE, 'dd/mm/YYYY')<= to_date(RES.FECHA_FIN, 'dd/mm/YYYY') ) THEN 'VIGENTE' ELSE 'VENCIDO' END) AS VIGENCIA, " +
                    " REG.ID AS REGISTRO_ID, " +
                    " REG.TIPO_REG_ID AS TIPO_REG_ID, " +
                    " TR.NOMBRE AS TIPO_REG_NOMBRE, " +
                    " REG.TIPO_OPE_ID AS TIPO_OPE_ID, " +
                    " TOP.NOMBRE AS TIPO_OPE_NOMBRE, " +
                    " REG.TIPO_PRO_ID AS TIPO_PRO_ID, " +
                    " TPRO.NOMBRE AS TIPO_PRO_NOMBRE,  " +
                    " REG.EMPRESA_ID AS EMPRESA_ID, " +
                    " (CASE WHEN CLI.RZN_SOCIAL IS NOT NULL THEN CLI.RZN_SOCIAL ELSE (CLI.APE_PAT || ' ' || CLI.APE_MAT || ' ' || CLI.NOMBRES) END) AS EMPRESA_NOMBRE, " +
                    " (CASE WHEN CLI.RZN_SOCIAL IS NOT NULL THEN CLI.RUC ELSE CLI.NUM_DOC END) AS EMPRESA_NUMDOC, " +
                    " (CASE WHEN CLI.RZN_SOCIAL IS NOT NULL THEN 'RUC' ELSE 'DNI' END) AS TIPO_DOCUMENTO, " +
                    " REG.ESTADO_ID AS ESTADO_ID, " +
                    " REG.NRO_SOLICITIUD AS NRO_SOLICITUD, " +
                    " REG.SEDE_SUCAMEC AS SEDE_SUCAMEC_ID, " +
                    " to_date(REG.FECHA, 'dd/mm/YYYY') as REGISTRO_FECHA, " +
                    " REG.NRO_EXPEDIENTE AS NRO_EXPEDIENTE " +
                    " FROM BDINTEGRADO.SSP_RESOLUCION RES " +
                    " INNER JOIN BDINTEGRADO.SSP_REGISTRO REG ON (RES.REGISTRO_ID = REG.ID) " +
                    " LEFT JOIN BDINTEGRADO.TIPO_BASE TR ON (TR.ID = REG.TIPO_REG_ID) " +
                    " LEFT JOIN BDINTEGRADO.TIPO_BASE TOP ON (TOP.ID = REG.TIPO_OPE_ID) " +
                    " LEFT JOIN BDINTEGRADO.TIPO_BASE TPRO ON (TPRO.ID = REG.TIPO_PRO_ID) " +
                    " LEFT JOIN BDINTEGRADO.SB_PERSONA CLI ON (CLI.ID = REG.EMPRESA_ID) " +
                    " WHERE RES.ACTIVO IN (0,1) " +
                    " AND RES.ID = ?1";
    
        //Syso(sql);
        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, Long.valueOf(ResolucionId));
        
        List<Map> res = q.setMaxResults(10).getResultList();
        if (res.size() == 1) {
            return (Map) res.get(0);
        }
        return null;
    }
}