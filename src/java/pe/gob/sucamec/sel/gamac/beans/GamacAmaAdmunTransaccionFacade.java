/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunTransaccion;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacAmaAdmunTransaccionFacade extends AbstractFacade<GamacAmaAdmunTransaccion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacAmaAdmunTransaccionFacade() {
        super(GamacAmaAdmunTransaccion.class);
    }    
    
    public List<ArrayRecord> buscarBandejaListaVentas(HashMap mMap) {
        try {
            List<ArrayRecord> lst;

            String sql = "SELECT"
                    + " tra.ID,"
                    + " TRIM(dir.DIRECCION) || ' ' || TRIM(dir.NUMERO) DIRECCION,"
                    + " CASE"
                    + " WHEN (SELECT NOMBRE FROM BDINTEGRADO.TIPO_BASE WHERE ID=p.TIPO_DOC) IS NULL THEN 'RUC'"
                    + " ELSE (SELECT NOMBRE FROM BDINTEGRADO.TIPO_BASE WHERE ID=p.TIPO_DOC)"
                    + " END TIPO_DOC,"
                    + " CASE"
                    + " WHEN p.TIPO_ID=92 THEN p.NUM_DOC"
                    + " WHEN p.TIPO_ID=93 THEN p.RUC"
                    + " END AS NRO_DOC,"
                    + " CASE"
                    + " WHEN p.TIPO_ID=92 THEN p.NOMBRES || ' ' || p.APE_PAT || ' ' || p.APE_MAT"
                    + " WHEN p.TIPO_ID=93 THEN p.RZN_SOCIAL"
                    + " END AS COMPRADOR_NOMBRE,"
                    + " t.NOMBRE AS TIPO_COMPRADOR,"
                    + " CASE"
                    + " WHEN tra.TIPOCLIENTEPROVEEDOR_ID=(SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE BDINTEGRADO.TIPO_GAMAC.COD_PROG='TP_CLTE_AGTECOM') THEN 'RESOLUCIÓN'"
                    + " ELSE 'LICENCIA'"
                    + " END AS TIPO_AUTORIZACION,"
                    + " COALESCE(tra.RG_AUTO_CLIPRO, TO_CHAR(lic.nro_licencia), ' ') AS NRO_AUTORIZACION,"
                    + " tra.FECHATRANSACCION AS FECHA_VENTA,"
                    + " (SELECT SUM(det.CANTIDAD_MUNICIONES) FROM BDINTEGRADO.AMA_ADMUN_DETALLE_TRANS det WHERE det.TRANSACCION_ID=tra.ID ) TOTAL_MUNICIONES,"
                    + " (SELECT usr.NOMBRES || ' ' || usr.APE_PAT || ' ' || usr.APE_MAT FROM BDINTEGRADO.SB_USUARIO usr WHERE usr.login = tra.AUD_LOGIN AND ROWNUM = 1) NOMBRE_RESPONSABLE,"
                    + " tra.CIERRE_INVENTARIO,"
                    + " tg.ABREVIATURA AS TIPO_TRANSACCION"
                    + " FROM BDINTEGRADO.AMA_ADMUN_TRANSACCION tra"
                    + " INNER JOIN BDINTEGRADO.TIPO_GAMAC t ON tra.TIPOCLIENTEPROVEEDOR_ID=t.ID"
                    + " INNER JOIN BDINTEGRADO.TIPO_GAMAC tg ON tra.TIPO_TRANSACCION_ID=tg.ID"
                    + " INNER JOIN BDINTEGRADO.SB_PERSONA p ON tra.CLIENTEPROVEEDOR_ID=p.ID"
                    + " INNER JOIN BDINTEGRADO.SB_DIRECCION dir ON tra.LOCALCOMERCIAL_ID=dir.ID"
                    + " LEFT JOIN BDINTEGRADO.ama_licencia_de_uso lic ON tra.licencia_id = lic.id"
                    + " WHERE"
                    + " tra.TIPO_TRANSACCION_ID IN (SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE BDINTEGRADO.TIPO_GAMAC.COD_PROG IN ('TP_TRANS_VTA', 'TP_TRANS_ANUVTA'))"
                    + " AND tra.AGENTECOMER_ID=?1 "//AND TO_CHAR(tra.PERIODO)=?2"
                    + " AND tra.ACTIVO=1"
                    + "";
            
            if (mMap.get("tipoFiltro") != null) {
                switch (mMap.get("tipoFiltro").toString()) {
                    case "1":   // RUC O DNI
                        sql += " AND (CASE"
                                + " WHEN p.TIPO_ID=92 THEN ' ' || p.NUM_DOC"
                                + " WHEN p.TIPO_ID=93 THEN ' ' || p.RUC"
                                + " END LIKE ?3)";
                        break;
                    case "2":   // NOMBRE COMPRADOR
                        sql += " AND (CASE"
                                + " WHEN p.TIPO_ID=92 THEN ' ' || p.NOMBRES || ' ' || p.APE_PAT || ' ' || p.APE_MAT"
                                + " WHEN p.TIPO_ID=93 THEN ' ' || p.RZN_SOCIAL"
                                + " END LIKE ?3)";
                        break;
                    case "3":   // FECHA VENTA
                        sql += " AND TO_CHAR(tra.FECHATRANSACCION,'DD/MM/YYYY') = TO_CHAR(?3,'DD/MM/YYYY')";
                        break;
                    /*case "4":   // MUNICION
                        sql += " AND tra.LOCALCOMERCIAL_ID = ?3";
                        break;*/
                    case "4":   // LOCAL VENTA
                        sql += " AND tra.LOCALCOMERCIAL_ID = ?3";
                        break;
                        
                }
            }

            sql += " ORDER BY tra.FECHATRANSACCION DESC";
            Query q = em.createNativeQuery(sql);

            q.setParameter(1, (Long) mMap.get("agenteComerId"));
            //q.setParameter(2, mMap.get("periodo").toString());

            if (mMap.get("tipoFiltro") != null) {
                switch (mMap.get("tipoFiltro").toString()) {
                    case "1":
                    case "2":
                        q.setParameter(3, "%" + mMap.get("filtro").toString().toUpperCase() + "%");
                        break;  
                    case "3":
                        q.setParameter(3, mMap.get("findFechaVenta"));
                        break;
                    /*case "4":
                        q.setParameter(3, mMap.get("findLocal"));
                        break;*/
                    case "4":
                        q.setParameter(3, mMap.get("findLocal"));
                        break;
                }
            }

            q.setHint("eclipselink.result-type", "Map");
            lst = q.setMaxResults(MAX_RES).getResultList();
            if (lst != null) {
                return lst;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<ArrayRecord> buscarBandejaListaCompras(HashMap mMap) {
        try {
            List<ArrayRecord> lst;

            String sql = "SELECT"
                    + " tra.ID,"
                    + " TRIM(dir.DIRECCION) || ' ' || TRIM(dir.NUMERO) DIRECCION,"
                    + " CASE"
                    + " WHEN (SELECT NOMBRE FROM BDINTEGRADO.TIPO_BASE WHERE ID=p.TIPO_DOC) IS NULL THEN 'RUC'"
                    + " ELSE (SELECT NOMBRE FROM BDINTEGRADO.TIPO_BASE WHERE ID=p.TIPO_DOC)"
                    + " END TIPO_DOC,"
                    + " CASE"
                    + " WHEN p.TIPO_ID=92 THEN p.NUM_DOC"
                    + " WHEN p.TIPO_ID=93 THEN p.RUC"
                    + " END AS NRO_DOC,"
                    + " CASE"
                    + " WHEN p.TIPO_ID=92 THEN p.NOMBRES || ' ' || p.APE_PAT || ' ' || p.APE_MAT"
                    + " WHEN p.TIPO_ID=93 THEN p.RZN_SOCIAL"
                    + " END AS VENDEDOR_NOMBRE,"
                    + " t.NOMBRE AS TIPO_VENDEDOR,"
                    + " CASE"
                    + " WHEN tra.TIPOCLIENTEPROVEEDOR_ID=(SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE BDINTEGRADO.TIPO_GAMAC.COD_PROG='TP_PROV_AGTECOM') THEN 'RESOLUCIÓN'"
                    + " ELSE 'LICENCIA'"
                    + " END AS TIPO_AUTORIZACION,"
                    + " COALESCE(tra.RG_AUTO_CLIPRO, '0') AS NRO_AUTORIZACION,"
                    + " tra.FECHATRANSACCION AS FECHA_COMPRA,"
                    + " (SELECT SUM(det.CANTIDAD_MUNICIONES) FROM BDINTEGRADO.AMA_ADMUN_DETALLE_TRANS det WHERE det.TRANSACCION_ID=tra.ID ) TOTAL_MUNICIONES,"
                    + " (SELECT usr.NOMBRES || ' ' || usr.APE_PAT || ' ' || usr.APE_MAT FROM BDINTEGRADO.SB_USUARIO usr WHERE usr.login = tra.AUD_LOGIN AND ROWNUM = 1) NOMBRE_RESPONSABLE,"
                    + " tra.CIERRE_INVENTARIO,"
                    + " tg.ABREVIATURA AS TIPO_TRANSACCION"
                    /*+ " p.ID PERSONA_ID,"
                    + " tra.LOCALCOMERCIAL_ID,"                    
                    + " (SELECT SUM(det.CANTIDAD_EMPAQUES) FROM BDINTEGRADO.AMA_ADMUN_DETALLE_TRANS det WHERE det.TRANSACCION_ID=tra.ID ) CANTIDAD_EMPAQUES,"
                    + " tra.AUD_LOGIN,"
                    + " tra.ACTIVO,"
                    + " est.COD_PROG AS ESTADO"*/
                    + " FROM BDINTEGRADO.AMA_ADMUN_TRANSACCION tra"
                    + " INNER JOIN BDINTEGRADO.TIPO_GAMAC t ON tra.TIPOCLIENTEPROVEEDOR_ID=t.ID"
                    + " INNER JOIN BDINTEGRADO.TIPO_GAMAC tg ON tra.TIPO_TRANSACCION_ID=tg.ID"
                    + " INNER JOIN BDINTEGRADO.SB_PERSONA p ON tra.CLIENTEPROVEEDOR_ID=p.ID"
                    + " INNER JOIN BDINTEGRADO.SB_DIRECCION dir ON tra.LOCALCOMERCIAL_ID=dir.ID"
                    //+ " LEFT JOIN BDINTEGRADO.TIPO_GAMAC est ON tra.ESTADOPRESEN_ID =est.ID"
                    + " WHERE"
                    + " tra.TIPO_TRANSACCION_ID IN (SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE BDINTEGRADO.TIPO_GAMAC.COD_PROG IN ('TP_TRANS_COM', 'TP_TRANS_ANUCOM'))"
                    + " AND tra.AGENTECOMER_ID=?1 "// AND TO_CHAR(tra.PERIODO)=?2"
                    + " AND tra.ACTIVO=1"
                    + "";
            
            if (mMap.get("tipoFiltro") != null) {
                switch (mMap.get("tipoFiltro").toString()) {
                    case "1":   // RUC O DNI
                        sql += " AND (CASE"
                                + " WHEN p.TIPO_ID=92 THEN ' ' || p.NUM_DOC"
                                + " WHEN p.TIPO_ID=93 THEN ' ' || p.RUC"
                                + " END LIKE ?3)";
                        break;
                    case "2":   // NOMBRE COMPRADOR
                        sql += " AND (CASE"
                                + " WHEN p.TIPO_ID=92 THEN ' ' || p.NOMBRES || ' ' || p.APE_PAT || ' ' || p.APE_MAT"
                                + " WHEN p.TIPO_ID=93 THEN ' ' || p.RZN_SOCIAL"
                                + " END LIKE ?3)";
                        break;
                    case "3":   // FECHA VENTA
                        sql += " AND TO_CHAR(tra.FECHATRANSACCION,'DD/MM/YYYY') = TO_CHAR(?3,'DD/MM/YYYY')";
                        break;
                    /*case "4":   // MUNICION
                        sql += " AND tra.LOCALCOMERCIAL_ID = ?3";
                        break;*/
                    case "4":   // LOCAL VENTA
                        sql += " AND tra.LOCALCOMERCIAL_ID = ?3";
                        break;
                        
                }
            }

            sql += " ORDER BY tra.FECHATRANSACCION DESC";
            Query q = em.createNativeQuery(sql);

            q.setParameter(1, (Long) mMap.get("agenteComerId"));
//            q.setParameter(2, mMap.get("periodo").toString());

            if (mMap.get("tipoFiltro") != null) {
                switch (mMap.get("tipoFiltro").toString()) {
                    case "1":
                    case "2":
                        q.setParameter(3, "%" + mMap.get("filtro").toString().toUpperCase() + "%");
                        break;  
                    case "3":
                        q.setParameter(3, mMap.get("findFechaVenta"));
                        break;
                    /*case "4":
                        q.setParameter(3, mMap.get("findLocal"));
                        break;*/
                    case "4":
                        q.setParameter(3, mMap.get("findLocal"));
                        break;
                }
            }
            q.setHint("eclipselink.result-type", "Map");
            lst = q.setMaxResults(MAX_RES).getResultList();
            if (lst != null) {
                return lst;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(GamacAmaAdmunTransaccionFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }    

    /*public List<GamacAmaAdmunTransaccion> listComprasNuevas(HashMap mMap){
        try {
            Query q = em.createQuery("select a from GamacAmaAdmunTransaccion a where a.agentecomerId.id=:agentecomerId "
                    + "and a.tipoTransaccionId.codProg='TP_TRANS_COM' and a.estadopresenId.codProg='TP_ESTPRE_CRE' and a.activo=1");
            q.setParameter("agentecomerId", (Long) mMap.get("agenteComerId"));
            q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }*/

    /*public List<GamacAmaAdmunTransaccion> listVentasNuevas(HashMap mMap){
        try {
            Query q = em.createQuery("select a from GamacAmaAdmunTransaccion a where a.agentecomerId.id=:agentecomerId "
                    + "and a.tipoTransaccionId.codProg='TP_TRANS_VEN' and a.estadopresenId.codProg='TP_ESTPRE_CRE' and a.activo=1");
            q.setParameter("agentecomerId", (Long) mMap.get("agenteComerId"));
            q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }*/
    
}
