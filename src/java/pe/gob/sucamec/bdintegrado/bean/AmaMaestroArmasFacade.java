/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.AmaMigraDiscaFox;

/**
 *
 * @author rarevalo
 */
@Stateless
public class AmaMaestroArmasFacade extends AbstractFacade<AmaMigraDiscaFox> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    //private static final int MAX_RES = 10000;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaMaestroArmasFacade() {
        super(AmaMigraDiscaFox.class);

        //getSQL_GENERAL();
    }

    public String nullATodo(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return s.replace("%", "");
    }

    public String nullATodoParcial(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s.replace("%", "") + "%";
    }

    public String nullATodoComodin(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s + "%";
    }

    public final String getSQL_NUEVALEY() {
        String SQL_NUEVALEY = "SELECT \n"
                + "    AA.ID ID,\n"
                + "    -- TIPO PROPIETARIO\n"
                + "    CASE \n"
                + "      WHEN SP.NRO_CIP IS NOT NULL THEN 'MIEMBRO FFA O PNP' \n"
                + "      WHEN SP.NRO_CIP IS NULL AND SP.NUM_DOC IS NULL THEN 'PERSONA JURIDICA' \n"
                + "      ELSE 'PERSONA NATURAL'\n"
                + "    END \n"
                + "    AS TIPO_PROPIETARIO,\n"
                + "    -- DATOS DEL COMPRADOR\n"
                + "    TB1.NOMBRE TIPO_DOCUMENTO,\n"
                + "    SP.NRO_CIP,\n"
                + "    TB.NOMBRE INSTITUCION_LABORA,\n"
                + "    SP.ID PERSONA_ID,\n"
                + "    NVL( SP.RUC,SP.NUM_DOC) DOC_PROPIETARIO,\n"
                + "    NVL( SP.RZN_SOCIAL,SP.APE_PAT||' '||SP.APE_MAT||' '||SP.NOMBRES) PROPIETARIO,\n"
                + "    -- DATOS DEL ARMA \n"
                + "    AA.NRO_RUA,\n"
                + "    AA.SERIE,\n"
                + "    CAT.TIPO_ARMA, \n"
                + "    CAT.MARCA, \n"
                + "    CAT.MODELO, \n"
                + "    CAT.CALIBRE,\n"
                + "    TG.NOMBRE ESTADO_ARMA, \n"
                + "    TG.COD_PROG CP_ESTADO_ARMA,\n"
                + "    TG1.NOMBRE SITUACION_ARMA, \n"
                + "    TG1.COD_PROG CP_SITUACION,\n"
                + "    TG3.NOMBRE MODALIDAD_ARMA, \n"
                + "    TG3.COD_PROG CP_MODALIDAD,\n"
                + "    TG4.NOMBRE RESTRICCION_ARMA,\n"
                + "    TG4.COD_PROG CP_RESTRICCION, \n"
                + "    --- DATOS DE LA TARJETA\n"
                + "    TP.NRO_EXPEDIENTE, \n"
                + "    TP.EMITIDO,\n"
                + "    TG2.NOMBRE ESTADO_TARJETA,\n"
                + "    TG2.COD_PROG CP_ESTADO_TP,\n"
                + "    TP.FECHA_EMISION,\n"
                + "    TP.LICENCIA_ID LICENCIA_USO_VINCULADA,\n"
                + "    TO_CHAR(AA.LICENCIA_DISCA) LICENCIA_DISCA, \n"
                + "    '-' ESTADO_LIC_DISCA,\n"
                + "    NULL FECHA_VEN_DISCA,\n"
                + "    'GAMAC' SISTEMA\n"
                + "FROM BDINTEGRADO.AMA_ARMA AA \n"
                + "    INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON AA.ID=TP.ARMA_ID\n"
                + "    INNER JOIN (\n"
                + "        SELECT M.ID ID,TAT.NOMBRE TIPO_ARTI,TA.NOMBRE TIPO_ARMA ,MA.NOMBRE MARCA,M.MODELO,LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE\n"
                + "        FROM BDINTEGRADO.AMA_MODELOS M\n"
                + "            INNER JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID\n"
                + "            INNER JOIN BDINTEGRADO.AMA_CATALOGO TAT ON TAT.ID = M.TIPO_ARTICULO_ID \n"
                + "            INNER JOIN BDINTEGRADO.AMA_CATALOGO TA ON TA.ID = M.TIPO_ARMA_ID --AND M.ACTIVO=1 \n"
                + "            INNER JOIN BDINTEGRADO.AMA_CATALOGO MA ON MA.ID = M.MARCA_ID ---AND MA.ACTIVO = 1 \n"
                + "            INNER JOIN BDINTEGRADO.AMA_CATALOGO CA ON CA.ID = MC.CATALOGO_ID --AND CA.ACTIVO = 1\n"
                + "        GROUP BY M.ID,TAT.NOMBRE,TA.NOMBRE,MA.NOMBRE,M.MODELO ) CAT ON AA.MODELO_ID=CAT.ID\n"
                + "    INNER JOIN BDINTEGRADO.SB_PERSONA SP ON SP.ID=TP.PERSONA_COMPRADOR_ID\n"
                + "    LEFT JOIN BDINTEGRADO.SB_PERSONA SP1 ON SP1.ID=TP.PERSONA_VENDEDOR_ID\n"
                + "    INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID=AA.ESTADO_ID\n"
                + "    INNER JOIN BDINTEGRADO.TIPO_GAMAC TG1 ON TG1.ID=AA.SITUACION_ID\n"
                + "    INNER JOIN BDINTEGRADO.TIPO_GAMAC TG2 ON TG2.ID=TP.ESTADO_ID\n"
                + "    INNER JOIN BDINTEGRADO.TIPO_GAMAC TG3 ON TG3.ID=TP.MODALIDAD_ID\n"
                + "    INNER JOIN BDINTEGRADO.TIPO_GAMAC TG4 ON TG4.ID=TP.RESTRICCION_ID\n"
                + "    LEFT JOIN BDINTEGRADO.TIPO_BASE TB ON TB.ID=SP.INSTITU_LAB_ID\n"
                + "    LEFT JOIN BDINTEGRADO.TIPO_BASE TB1 ON TB1.ID=SP.TIPO_DOC\n"
                + "WHERE AA.ACTIVO=1 AND TP.ACTIVO=1"
                + "";

        return SQL_NUEVALEY;
    }

    public final String getSQL_DISCA() {
        String SQL_DISCA = "SELECT \n"  
                + "    DF.ID ID,\n"
                + "    -- DATOS DEL PROPIETARIO\n"
                + "    CASE \n"
                + "      WHEN DF.NRO_CIP IS NOT NULL THEN 'MIEMBRO FFA O PNP' \n"
                + "      WHEN DF.NRO_CIP IS NULL AND DF.DOC_PROP IS NULL THEN 'PERSONA JURIDICA' \n"
                + "      ELSE 'PERSONA NATURAL'\n"
                + "    END \n"
                + "    AS TIPO_PROPIETARIO,\n"
                + "    DF.TIPO_DOCUMENTO TIPO_DOCUMENTO,\n"
                + "    DF.NRO_CIP,\n"
                + "    TB.NOMBRE INSTITUCION_LABORA,\n"
                + "    DF.PERSONA_ID,\n"
                + "    NVL( DF.RUC_PROP,DF.DOC_PROP) DOC_PROPIETARIO,\n"
                + "    NVL( DF.RZN_SOCIAL,DF.APE_PAT||' '||DF.APE_MAT||' '||DF.NOMBRES) PROPIETARIO,\n"
                + "    -- DATOS DEL ARMA\n"
                + "    '-' NRO_RUA,\n"
                + "    DF.SERIE,\n"
                + "    DF.TIPO_ARMA,\n"
                + "    DF.MARCA,\n"
                + "    NVL(DF.MODELO, '-') MODELO,\n"
                + "    DF.CALIBRE,\n"
                + "    NVL(TG3.NOMBRE, '-') ESTADO_ARMA,\n"
                + "    NVL(TG3.COD_PROG, '-') CP_ESTADO_ARMA,\n"
                + "    NVL(TG2.NOMBRE, HS.DESCRIPCION_DISCA) SITUACION_ARMA,\n"
                + "    TG2.COD_PROG CP_SITUACION, \n"
                + "    --TG1.NOMBRE MODALIDAD_ARMA,\n"
                + "    SH1.DESCRIPCION_DISCA MODALIDAD_ARMA,"
                + "    TG1.COD_PROG CP_MODALIDAD,\n"
                + "    '-' RESTRICCION_ARMA,\n"
                + "    '-' CP_RESTRICCION,\n"
                + "    DF.NRO_EXPE NRO_EXPEDIENTE,\n"
                + "    0 EMITIDO,\n"
                + "    '-'ESTADO_TARJETA,\n"
                + "    '-' CP_ESTADO_TP,\n"
                + "    DF.FEC_EMI FECHA_EMISION,\n"
                + "    0 LICENCIA_USO_VINCULADA,\n"
                + "    DF.NRO_LIC LICENCIA_DISCA,\n"
                + "    DF.ESTADO_LICENCIA_ID ESTADO_LIC_DISCA,\n"
                + "    DF.FEC_VEN FECHA_VEN_DISCA,\n"
                + "    DF.SISTEMA\n"
                + "FROM BDINTEGRADO.AMA_MIGRA_DISCA_FOX DF \n"
                + "    LEFT JOIN (\n"
                + "       SELECT SH1.TIPO_BDINTEGRADO,SH1.DESCRIPCION_DISCA,SH1.TIPO_DISCA  FROM BDINTEGRADO.SB_HOMOLOGAR SH1\n"
                + "       INNER JOIN BDINTEGRADO.TIPO_BASE TH1 ON TH1.ID = SH1.TIPO_HOMOLOGAR AND TH1.COD_PROG = 'TP_HOM_ML'\n"
                + "    ) SH1 ON SH1.TIPO_DISCA=DF.TIPO_LICENCIA_ID\n"
                + "    LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG1 ON TG1.ID=SH1.TIPO_BDINTEGRADO\n"
                + "    INNER JOIN BDINTEGRADO.AMA_HOMO_SIT_ARMA HS ON HS.SITUACION_DISCA=DF.SITUACION_DISCA \n"
                + "    LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG2 ON TG2.ID=HS.SITUACION_ARMA\n"
                + "    INNER JOIN BDINTEGRADO.AMA_HOMO_SIT_ARMA HS2 ON HS2.SITUACION_DISCA=DF.SITUACION_DISCA \n"
                + "    LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG3 ON TG3.ID=HS.ESTADO_ARMA\n"
                + "    LEFT JOIN (\n"
                + "        SELECT * FROM BDINTEGRADO.SB_HOMOLOGAR SH \n"
                + "        INNER JOIN BDINTEGRADO.TIPO_BASE TH ON TH.ID = SH.TIPO_HOMOLOGAR\n"
                + "        WHERE TH.COD_PROG = 'TP_HOM_IN'\n"
                + "        ) SH ON SH.TIPO_BDINTEGRADO = DF.INSTITUCION\n"
                + "    LEFT JOIN BDINTEGRADO.TIPO_BASE TB ON TB.ID=SH.TIPO_BDINTEGRADO\n"
                + "WHERE DF.ACTUAL=1\n"
                + "    --AND TH1.COD_PROG = 'TP_HOM_ML'\n"
                + "";
        return SQL_DISCA;
    }

    public final String getSQL_ESTADO_DISCA() {
        String SQL_ESTADO_DISCA = "SELECT\n"
                + "    DF.ID,\n"
                + "    DF.SERIE,\n"
                + "    DF.TIPO_ARMA,\n"
                + "    DF.MARCA,\n"
                + "    DF.MODELO,\n"
                + "    DF.CALIBRE,\n"
                + "    DF.ESTADO_LICENCIA_ID ESTADO_LIC_DISCA,\n"
                + "    CASE\n"
                + "        WHEN LDC.ID_LIC_MIGRA IS NOT NULL THEN 'CANCELADO'\n"
                + "        WHEN LDC.ID_LIC_MIGRA IS NULL AND DF.FEC_VEN >= SYSDATE THEN 'VIGENTE'\n"
                + "        ELSE 'VENCIDO'\n"
                + "    END AS ESTADO_LICEN_DISCA\n"
                + "FROM BDINTEGRADO.AMA_MIGRA_DISCA_FOX DF\n"
                + "    LEFT JOIN BDINTEGRADO.AMA_LICENCIA_DISCA_CANCE LDC ON DF.ID=LDC.ID_LIC_MIGRA AND LDC.ACTUAL=1 AND LDC.FLAG_DISCA IS NULL\n"
                + "WHERE DF.ACTUAL=1\n"
                + " ";
        return SQL_ESTADO_DISCA;
    }

    public final String getSQL_GENERAL() {
        String sql = "SELECT * FROM (\n"
                + getSQL_NUEVALEY()
                + " UNION ALL \n"
                + getSQL_DISCA()
                + " ) "
                + "";
        //SQL_GENERAL = sql;
        //return SQL_GENERAL;
        return sql;
    }

    public List<AmaMigraDiscaFox> listarLicsIn(String idLics) {
        javax.persistence.Query q = em.createQuery(
                "select p from AmaMigraDiscaFox p where p.id in (:idLics)");
        q.setParameter("idLics", idLics);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return null;

    }

    public List<Map> listaArmasGeneral() {
        String sql = getSQL_GENERAL();

        try {
            Query q = em.createNativeQuery(sql);

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map> estadoLicDisca(Long idLicFox) {
        String sql = getSQL_ESTADO_DISCA()
                + " AND DF.ID=?1"
                + "";

        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, idLicFox);

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String estadoLicenciaDisca(Long idMigra) {
        String res = "-";
        
        if (idMigra!=null) {
            List<Map> lstEstadoDis = estadoLicDisca(idMigra);
            if (lstEstadoDis!=null && lstEstadoDis.size()>0) {
                res = lstEstadoDis.get(0).get("ESTADO_LICEN_DISCA").toString();
            }            
        }
        return res;
    }
    
}
