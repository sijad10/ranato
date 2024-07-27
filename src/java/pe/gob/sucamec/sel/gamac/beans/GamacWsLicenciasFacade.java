/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.gamac.data.GamacWsLicencias;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.*;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacWsLicenciasFacade extends AbstractFacade<GamacWsLicencias> {

    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager emSucamec;

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return emSucamec;
    }

    public GamacWsLicenciasFacade() {
        super(GamacWsLicencias.class);
    }

    public List<ArrayRecord> listPerNatPnpFa(String filtro) {
        if (filtro != null) {
            /*String sql = "SELECT"
                    + " TO_CHAR(a.NRO_LIC) || ',' || a.ESTADO AS ID,"
                    + " TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) DOC_PORTADOR,"
                    + " a.TIPO_ARMA, a.MARCA, a.MODELO, a.CALIBRE"
                    + " FROM RMA1369.INTEROP_WS_LICENCIAS a"
                    + " WHERE a.TIP_USR_PROPIETARIO IN(45) "
                    + " AND a.TIP_USR_PORTADOR IN(45, 432, 293, 294, 295) "
                    + "";*/

            String sql = "SELECT"
                    + " a.ID || ',' || a.ESTADO_ORG AS ID,"
                    + " TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) DOC_PORTADOR,"
                    + " a.TIPO_ARMA, a.MARCA, a.MODELO, a.CALIBRE"
                    + " FROM BDINTEGRADO.WS_LICENCIAS a"
                    + " WHERE a.TIP_USR_PROPIETARIO IN(45) "
                    + " AND a.TIP_USR_PORTADOR IN(45, 432, 293, 294, 295) "
                    + "";
            
            sql += " AND TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) LIKE ?1";
            //sql += " GROUP BY a.DOC_PORTADOR";
            
            
            
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + filtro + "%");
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(MAX_RES_C);            
            return q.getResultList();
        } else {
            return null;
        }
            
    }
    
    public List<ArrayRecord> listPersonaNatLic(String filtro) {
        if (filtro != null) {
            String sql = "SELECT "
                    + " TO_CHAR(a.NRO_LIC) || ',' || a.ESTADO AS ID,"
                    + " a.TIPO_LICENCIA, TO_CHAR(a.NRO_LIC) as NRO_LIC, a.ESTADO, a.TIPO_PROPIETARIO, a.DOC_PROPIETARIO, a.PROPIETARIO"
                    + " FROM RMA1369.INTEROP_WS_LICENCIAS a"
                    + " WHERE"
                    + " a.TIP_USR_PROPIETARIO in(2, 45) AND"
                    + " trim(a.NRO_LIC) LIKE ?1"
                    + " GROUP BY a.TIPO_LICENCIA, a.NRO_LIC, a.ESTADO, a.TIPO_PROPIETARIO, a.DOC_PROPIETARIO, a.PROPIETARIO"
                    + " ORDER BY a.NRO_LIC";

            Query q = emSucamec.createNativeQuery(sql);
            filtro = filtro.toUpperCase();
            q.setParameter(1, "" + filtro + "%");
            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES_C).getResultList();
        } else {
            return null;
        }
    }

    public List<GamacWsLicencias> listPersonaNatLicencia(String filtro) {
        String sql = "select"
                + " a.nroLic"
                + " from WsLicencias a"
                + " where"
                + " a.tipUsrPropietario in(2,45)";

        if (filtro != null) {
            sql += " and trim(a.nroLic) like :nroLic";
        }
        sql += " group by a.nroLic";
        Query q = emSucamec.createQuery(sql);

        if (filtro != null) {
            q.setParameter("nroLic", "" + filtro + "%");
        }
        q.setMaxResults(MAX_RES_C);
        //q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<GamacWsLicencias> listPersonaPnpFa(String filtro) {
        String sql = "select"
                + " a"
                + " from GamacWsLicencias a"
                + " where"
                + " a.tipUsrPropietario in(45)"
                + " and a.tipUsrPortador in(45, 432, 293, 294, 295)";

        if (filtro != null) {
            sql += " and trim(a.docPortador) like :docPortador";
        }
        //sql += " group by a.nroLic";
        Query q = emSucamec.createQuery(sql);

        if (filtro != null) {
            q.setParameter("docPortador", "" + filtro + "%");
        }
        q.setMaxResults(MAX_RES_C);
        //q.setHint("eclipselink.batch.type", "IN");        
        return q.getResultList();

    }

    public List<ArrayRecord> listPersonaPnpFaN(String filtro) {
        if (filtro != null) {
            String sql = "("
                    + " SELECT DISTINCT"
                    + " TO_CHAR(CASE WHEN C.TIPO_ID = 93 THEN C.RUC ELSE C.NUM_DOC END) || ',' || TO_CHAR(CASE WHEN NVL(E.FECHA_VENCIMIETNTO, SYSDATE)>=TRUNC (SYSDATE) THEN 'VIGENTE' ELSE 'VENCIDO' END) AS ID,"
                    + " CASE"
                    + "     WHEN C.TIPO_ID = 93 THEN 'PERS. JURIDICA'"
                    + "     ELSE"
                    + "         CASE"
                    + "         WHEN C.TIPO_DOC = 45 THEN 'PERS. NATURAL'"
                    + "         WHEN C.TIPO_DOC = 48 THEN 'EXTRANJERO'"
                    + "         END"
                    + " END AS TIPO_PROPIETARIO,"
                    + " TO_CHAR(CASE WHEN C.TIPO_ID = 93 THEN C.RUC ELSE C.NUM_DOC END) AS DOC_PROPIETARIO,"
                    + " C.RZN_SOCIAL || C.APE_PAT || ' ' || C.APE_MAT || ' ' || C.NOMBRES AS PROPIETARIO,"
                    + " CASE"
                    + "     WHEN C.TIPO_ID = 93 THEN 'PERS. JURIDICA'"
                    + "     ELSE"
                    + "     CASE WHEN C.TIPO_DOC = 45 THEN"
                    + "         CASE"
                    + "         WHEN C.INSTITU_LAB_ID = 432 THEN 'POLICIA'"
                    + "         WHEN C.INSTITU_LAB_ID IN (293, 294, 295, 430) THEN 'MILITAR'"
                    + "         ELSE 'PERS. NATURAL'"
                    + "         END"
                    + "     WHEN C.TIPO_DOC = 48 THEN 'EXTRANJERO'"
                    + " END"
                    + " END AS TIPO_PORTADOR,"
                    + " TO_CHAR(CASE "
                    + "     WHEN C.TIPO_ID = 93 THEN C.RUC"
                    + "     ELSE"
                    + "         CASE WHEN C.INSTITU_LAB_ID IN (432, 293, 294, 295, 430) THEN TO_CHAR (C.NRO_CIP)"
                    + "         ELSE C.NUM_DOC"
                    + "         END"
                    + "     END)"
                    + " AS DOC_PORTADOR,"
                    + " C.RZN_SOCIAL || C.APE_PAT || ' ' || C.APE_MAT || ' ' || C.NOMBRES AS PORTADOR,"
                    + " CASE"
                    + "     WHEN NVL(E.FECHA_VENCIMIETNTO, SYSDATE) >= TRUNC (SYSDATE) THEN 'VIGENTE'"
                    + "     ELSE 'VENCIDO'"
                    + "     END AS ESTADO"
                    + " FROM BDINTEGRADO.AMA_TARJETA_PROPIEDAD@RENAGI A"
                    + " LEFT JOIN BDINTEGRADO.AMA_ARMA@RENAGI B ON A.ARMA_ID = B.ID"
                    + " LEFT JOIN BDINTEGRADO.SB_PERSONA@RENAGI C ON A.PERSONA_COMPRADOR_ID = C.ID"
                    + " LEFT JOIN BDINTEGRADO.TIPO_GAMAC@RENAGI D ON A.MODALIDAD_ID = D.ID"
                    + " LEFT JOIN BDINTEGRADO.AMA_LICENCIA_DE_USO@RENAGI E ON A.LICENCIA_ID = E.ID"
                    + " LEFT JOIN BDINTEGRADO.TIPO_GAMAC@RENAGI SITUA ON B.ESTADO_ID = SITUA.ID"
                    + " WHERE"
                    + " A.ACTIVO=1 AND B.ACTIVO=1 AND C.ACTIVO=1 AND C.TIPO_ID<>93 AND C.INSTITU_LAB_ID IN (432, 293, 294, 295, 430) AND (TO_CHAR(C.NRO_CIP) LIKE ?1 OR TO_CHAR(C.NUM_DOC) LIKE ?2)"
                    + " )"
                    + " UNION ALL"
                    + " ("
                    + " SELECT DISTINCT"
                    + " TO_CHAR(L.COD_USR) || ',' || TO_CHAR(CASE WHEN LANU.NRO_LIC IS NOT NULL THEN 'ANULADO' ELSE CASE WHEN NVL (LEV.FEC_VENC_LIC, NVL (LM.FEC_VEN, L.FEC_VEN))>=SYSDATE THEN 'VIGENTE' ELSE 'VENCIDO' END END) AS ID,"
                    + " TUPRO.DES_USR AS TIPO_PROPIETARIO,"
                    + " TO_CHAR(L.COD_USR) AS DOC_PROPIETARIO,"
                    + " CASE WHEN L.TIP_USR = 1 THEN (SELECT EM.RZN_SOC FROM RMA1369.EMPRESA@SUCAMEC EM WHERE EM.RUC = L.COD_USR) END"
                    + " ||"
                    + " CASE"
                    + " WHEN L.TIP_USR = 2 THEN (SELECT NN.APE_PAT || ' ' || NN.APE_MAT || ' ' || NN.NOMBRE FROM RMA1369.PERSONA_NATURAL@SUCAMEC NN WHERE NN.COD_USR = L.COD_USR)"
                    + " WHEN L.TIP_USR = 5 THEN (SELECT NN.APE_PAT || ' ' || NN.APE_MAT || ' ' || NN.NOMBRE FROM RMA1369.EXTRANJERO@SUCAMEC NN WHERE NN.COD_USR = L.COD_USR)"
                    + " WHEN L.TIP_USR IN (3, 4) THEN (SELECT NN.APE_PAT || ' ' || NN.APE_MAT || ' ' || NN.NOMBRE FROM RMA1369.efectivo@SUCAMEC NN WHERE NN.COD_USR = L.COD_USR AND l.tip_usr = nn.tip_usr)"
                    + " END AS PROPIETARIO,"
                    + " (SELECT TUPORTA.DES_USR FROM RMA1369.TIP_USR@SUCAMEC TUPORTA WHERE TUPORTA.TIP_USR = NVL (LEV.TIP_USR_VIG, NVL (LM.TIP_USR, L.TIP_USR))) AS TIPO_PORTADOR,"
                    + " TO_CHAR(NVL (LEV.COD_USR_VIG, NVL (LM.COD_USR, L.COD_USR))) AS DOC_PORTADOR,"
                    + " CASE WHEN NVL (LEV.TIP_USR_VIG, NVL (LM.TIP_USR, L.TIP_USR)) = 1 THEN (SELECT EM.RZN_SOC FROM RMA1369.EMPRESA@SUCAMEC EM WHERE EM.RUC = NVL (LEV.COD_USR_VIG, NVL (LM.COD_USR, L.COD_USR))) END"
                    + " ||"
                    + " CASE"
                    + "     WHEN NVL (LEV.TIP_USR_VIG, NVL (LM.TIP_USR, L.TIP_USR)) = 2 THEN"
                    + "     (SELECT NN.APE_PAT || ' ' || NN.APE_MAT || ' ' || NN.NOMBRE FROM RMA1369.PERSONA_NATURAL@SUCAMEC NN WHERE NN.COD_USR = NVL (LEV.COD_USR_VIG, NVL (LM.COD_USR, L.COD_USR)))"
                    + "     WHEN NVL (LEV.TIP_USR_VIG, NVL (LM.TIP_USR, L.TIP_USR)) = 5 THEN"
                    + "     (SELECT NN.APE_PAT || ' ' || NN.APE_MAT || ' ' || NN.NOMBRE FROM RMA1369.EXTRANJERO@SUCAMEC NN WHERE NN.COD_USR = NVL (LEV.COD_USR_VIG, NVL (LM.COD_USR, L.COD_USR)))"
                    + "     WHEN NVL (LEV.TIP_USR_VIG, NVL (LM.TIP_USR, L.TIP_USR)) IN (3, 4) THEN"
                    + "     (SELECT NN.APE_PAT || ' ' || NN.APE_MAT || ' ' || NN.NOMBRE FROM RMA1369.efectivo@SUCAMEC NN WHERE NN.COD_USR = NVL (LEV.COD_USR_VIG, NVL (LM.COD_USR, L.COD_USR)) AND nn.tip_usr = NVL (LEV.TIP_USR_VIG, NVL (LM.TIP_USR, L.TIP_USR)))"
                    + " END AS PORTADOR,"
                    + " CASE"
                    + "     WHEN LANU.NRO_LIC IS NOT NULL THEN 'ANULADO'"
                    + "     ELSE"
                    + "         CASE WHEN NVL (LEV.FEC_VENC_LIC, NVL (LM.FEC_VEN, L.FEC_VEN)) >= SYSDATE THEN 'VIGENTE'"
                    + "         ELSE 'VENCIDO'"
                    + "         END"
                    + "     END AS ESTADO"
                    + " FROM RMA1369.AM_LICENCIA@SUCAMEC L,"
                    + "     RMA1369.AM_TIP_LIC@SUCAMEC TL, RMA1369.AM_TIP_LIC@SUCAMEC TLV, RMA1369.AM_ARMA@SUCAMEC AR,"
                    + "     RMA1369.ARTICULO@SUCAMEC ART, RMA1369.AM_MARCA@SUCAMEC MAR, RMA1369.TIP_USR@SUCAMEC TUPRO,"
                    + "     RMA1369.AM_LIC_EMPR_VIG@SUCAMEC LEV, RMA1369.AM_LIC_MAN@SUCAMEC LM, RMA1369.SIT_ARMA@SUCAMEC SIT,"
                    + "     RMA1369.AM_LIC_ANULADA@SUCAMEC LANU "
                    + " WHERE L.TIP_LIC = TL.TIP_LIC(+)"
                    + "     AND LEV.TIP_LIC = TLV.TIP_LIC(+) AND L.NRO_LIC = AR.NRO_LIC AND AR.TIP_ART = ART.TIP_ART(+)"
                    + "     AND AR.TIP_ARM = ART.TIP_ARM(+) AND AR.TIP_ART = MAR.TIP_ART(+) AND AR.TIP_ARM = MAR.TIP_ARM(+)"
                    + "     AND AR.COD_MARCA = MAR.COD_MARCA(+) AND AR.COD_MODELO = MAR.COD_MODELO(+) AND L.TIP_USR = TUPRO.TIP_USR(+)"
                    + "     AND L.NRO_LIC = LEV.NRO_LIC(+) AND L.NRO_LIC = LM.NRO_LIC(+) AND AR.SIT_ARM = SIT.SIT_ARMA(+)"
                    + "     AND L.NRO_LIC = LANU.NRO_LIC(+) AND L.TIP_USR IN(3, 4, 18) AND L.COD_USR LIKE ?3"
                    + " )"
                    + " ORDER BY DOC_PROPIETARIO";

            Query q = emSucamec.createNativeQuery(sql);
            filtro = filtro.toUpperCase();
            q.setParameter(1, "" + filtro + "%");
            q.setParameter(2, "" + filtro + "%");
            q.setParameter(3, "" + filtro + "%");
            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES_C).getResultList();
        } else {
            return null;
        }
    }

    public List<GamacWsLicencias> listNroSerie(String filtro, String nroLic) {
        if (nroLic!=null) {
            
            String sql = "select"
                    + " a"
                    + " from GamacWsLicencias a"
                    + " where"
                    + " trim(a.nroLic) = :nroLic";

            if(filtro!=null){
                sql += " and trim(a.nroSerie) like :nroSerie";
            }

            Query q = emSucamec.createQuery(sql);
            q.setParameter("nroLic", nroLic);
            if(filtro!=null){
                q.setParameter("nroSerie", "" + filtro + "%");
            }
            q.setMaxResults(MAX_RES_C);
            //q.setHint("eclipselink.batch.type", "IN");          
            return q.getResultList();
        } else {
            return null;
        }
    }

}
