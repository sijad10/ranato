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
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunDetalleTrans;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacAmaAdmunDetalleTransFacade extends AbstractFacade<GamacAmaAdmunDetalleTrans> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacAmaAdmunDetalleTransFacade() {
        super(GamacAmaAdmunDetalleTrans.class);
    }
    
    public List<ArrayRecord> saldoMesAnterior(HashMap mMap) {
        try {
            List<ArrayRecord> lst;

            String sql = "SELECT"
                    + " COALESCE( SUM(a.CANTIDAD_EMPAQUES*a.MUNI_POR_EMPAQUE), 0) AS SALDO_MES_ANT"
                    + " FROM BDINTEGRADO.AMA_ADMUN_DETALLE_TRANS a"
                    + " INNER JOIN BDINTEGRADO.AMA_ADMUN_TRANSACCION b ON a.TRANSACCION_ID=b.ID"
                    + " INNER JOIN BDINTEGRADO.AMA_MUNICION mu ON a.ARTICULO_MUNICION_ID=mu.ID"
                    + " INNER JOIN BDINTEGRADO.AMA_CATALOGO ca ON mu.CALIBREARMA_ID=ca.ID"
                    + " WHERE TO_CHAR(b.FECHATRANSACCION, 'MM')=  TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'MM')"
                    + " AND a.TARJETA_PROPIEDAD_ID= ?1"
                    + " AND ca.ID= ?2"
                    + " GROUP BY a.TARJETA_PROPIEDAD_ID" //a.ARTICULO_MUNICION_ID
                    + "";
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, mMap.get("tarjetaPropiedadId"));
            q.setParameter(2, mMap.get("calibreId"));
            
            q.setHint("eclipselink.result-type", "Map");
            lst = q.setMaxResults(MAX_RES).getResultList();
            if (lst != null) {
                return lst;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;            
        }
    }

    public List<ArrayRecord> saldoMesActual(HashMap mMap) {
        try {
            List<ArrayRecord> lst;

            String sql = "SELECT"
                    + " COALESCE( SUM(a.CANTIDAD_MUNICIONES), 0) AS SALDO_MES_ACT"
                    + " FROM BDINTEGRADO.AMA_ADMUN_DETALLE_TRANS a"
                    + " INNER JOIN BDINTEGRADO.AMA_ADMUN_TRANSACCION b ON a.TRANSACCION_ID=b.ID"
                    + " INNER JOIN BDINTEGRADO.AMA_MUNICION mu ON a.ARTICULO_MUNICION_ID=mu.ID"
                    + " INNER JOIN BDINTEGRADO.AMA_CATALOGO ca ON mu.CALIBREARMA_ID=ca.ID"
                    + " WHERE TO_CHAR(b.FECHATRANSACCION, 'MMYYYY') = TO_CHAR(ADD_MONTHS(SYSDATE, 0), 'MMYYYY')"
                    + " AND a.TARJETA_PROPIEDAD_ID= ?1"
                    //+ " AND ca.ID= ?2"
                    + " GROUP BY a.TARJETA_PROPIEDAD_ID " //a.ARTICULO_MUNICION_ID
                    + "";
            
            //Syso("sql CanAct: " + sql + mMap.get("tarjetaPropiedadId"));
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, mMap.get("tarjetaPropiedadId"));
            //q.setParameter(2, mMap.get("calibreId"));
            
            q.setHint("eclipselink.result-type", "Map");
            lst = q.setMaxResults(MAX_RES).getResultList();
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;            
        }
    }

    public List<ArrayRecord> listInventarioxLogin(HashMap mMap) {
        try {
            List<ArrayRecord> lst;

            String sql = "SELECT"
                    + " cab.LOCALCOMERCIAL_ID || ',' || det.ARTICULO_MUNICION_ID ID,"                    
                    + " cab.LOCALCOMERCIAL_ID,"
                    + " lo.DIRECCION || ' ' || lo.NUMERO LOCAL_COMERCIAL,"
                    + " det.ARTICULO_MUNICION_ID,"
                    + " ma.NOMBRE MARCA_NOMBRE,"
                    + " ca.NOMBRE CALIBRE_MUNICION,"
                    + " COALESCE(de.NOMBRE, '-') DENOMINACION,"
                    + " tm.NOMBRE TIPO_MUNICION,"
                    + " COALESCE(tp.NOMBRE, '-') TIPO_PROYECTIL,"
                    + " SUM( CASE   WHEN t.COD_PROG IN ('TP_TRANS_COM', 'TP_TRANS_STCINI', 'TP_TRANS_INTERN', 'TP_TRANS_TLDIN') THEN det.CANTIDAD_MUNICIONES   ELSE 0 END ) AS SUM_MUNI_COMPRAS_BRUTO, \n"
                    + " SUM( CASE   WHEN t.COD_PROG IN ('TP_TRANS_DEVC')   THEN det.CANTIDAD_MUNICIONES   ELSE 0 END ) AS SUM_MUNI_COMPRAS_DEV, \n"
                    + " ABS(nvl(SUM( CASE   WHEN t.COD_PROG IN ('TP_TRANS_COM', 'TP_TRANS_STCINI', 'TP_TRANS_INTERN', 'TP_TRANS_TLDIN') THEN det.CANTIDAD_MUNICIONES   ELSE 0 END ),0) - nvl(SUM( CASE   WHEN t.COD_PROG IN ('TP_TRANS_DEVC')   THEN det.CANTIDAD_MUNICIONES   ELSE 0 END ),0)) AS SUM_MUNI_COMPRAS, \n"
                    + " SUM(   CASE     WHEN t.COD_PROG IN ('TP_TRANS_VTA', 'TP_TRANS_PERDID', 'TP_TRANS_TLDOUT') THEN det.CANTIDAD_MUNICIONES     ELSE 0   END ) AS SUM_MUNI_VENTAS_BRUTO, \n"
                    + " SUM(   CASE     WHEN (t.COD_PROG)IN ('TP_TRANS_DEVV')     THEN det.CANTIDAD_MUNICIONES     ELSE 0   END ) AS SUM_MUNI_VENTAS_DEV, \n"
                    + " ABS(nvl(SUM(   CASE     WHEN t.COD_PROG IN ('TP_TRANS_VTA', 'TP_TRANS_PERDID', 'TP_TRANS_TLDOUT') THEN det.CANTIDAD_MUNICIONES     ELSE 0   END ),0) - nvl(SUM(   CASE     WHEN (t.COD_PROG)IN ('TP_TRANS_DEVV')     THEN det.CANTIDAD_MUNICIONES     ELSE 0   END ),0)) AS SUM_MUNI_VENTAS"
                    + " FROM"
                    + "   BDINTEGRADO.AMA_ADMUN_DETALLE_TRANS det"
                    + "   INNER JOIN BDINTEGRADO.AMA_ADMUN_TRANSACCION cab ON cab.ID=det.TRANSACCION_ID"
                    + "   INNER JOIN BDINTEGRADO.SB_DIRECCION lo ON cab.LOCALCOMERCIAL_ID=lo.ID"
                    + "   INNER JOIN BDINTEGRADO.TIPO_GAMAC t ON t.ID=cab.TIPO_TRANSACCION_ID"
                    + "   INNER JOIN BDINTEGRADO.AMA_MUNICION muni ON muni.ID=det.ARTICULO_MUNICION_ID"
                    + "     INNER JOIN BDINTEGRADO.AMA_CATALOGO ma ON ma.ID=muni.MARCA_ID"
                    + "     INNER JOIN BDINTEGRADO.AMA_CATALOGO ca ON ca.ID=muni.CALIBREARMA_ID"
                    + "     INNER JOIN BDINTEGRADO.TIPO_GAMAC tm ON tm.ID=muni.TIPO_MUNICION_ID"
                    + "     LEFT JOIN BDINTEGRADO.TIPO_GAMAC tp ON tp.ID=muni.TIPO_PROYECTIL_ID"
                    + "     LEFT JOIN BDINTEGRADO.TIPO_GAMAC de ON de.ID=muni.DENOMINACION_ID"
                    + " WHERE"
                    + "  cab.AGENTECOMER_ID=?1"
                    + "";
            
            if (mMap.get("tipoFiltro") != null) {
                switch (mMap.get("tipoFiltro").toString()) {
                    case "1":   // LOCAL COMERCIAL
                        sql += " AND cab.LOCALCOMERCIAL_ID=?2";
                        break;
                    case "2":   // MARCA
                        //sql += " AND muni.MARCA_ID=?2";
                        sql += " AND ma.NOMBRE=?2";
                        break;
                    case "3":   // CALIBRE ARMA
                        //sql += " AND muni.CALIBREARMA_ID=?2";
                        sql += " AND ca.NOMBRE=?2";
                        break;
                    case "4":   // DENOMINACION
                        sql += " AND muni.DENOMINACION_ID=?2";
                        break;
                        
                }
            }

            sql += " GROUP BY"
                    + "   cab.LOCALCOMERCIAL_ID,"
                    + "   lo.DIRECCION || ' ' || lo.NUMERO,"
                    + "   det.ARTICULO_MUNICION_ID,"
                    + "   ma.NOMBRE,"
                    + "   ca.NOMBRE,"
                    + "   de.NOMBRE,"
                    + "   tm.NOMBRE,"
                    + "   tp.NOMBRE"
                    + "";
            //System.out.println("QUERY = " + sql);
            Query q = em.createNativeQuery(sql);
            Logger.getLogger(GamacAmaAdmunDetalleTransFacade.class.getName()).log(Level.INFO, "query: " + sql);
            Logger.getLogger(GamacAmaAdmunDetalleTransFacade.class.getName()).log(Level.INFO, "tipoFiltro: " + mMap.get("tipoFiltro"));
            Logger.getLogger(GamacAmaAdmunDetalleTransFacade.class.getName()).log(Level.INFO, "agenteComerId: " + mMap.get("agenteComerId"));
            q.setParameter(1, (Long) mMap.get("agenteComerId"));

            if (mMap.get("tipoFiltro") != null) {
                switch (mMap.get("tipoFiltro").toString()) {
                    case "1":
                        q.setParameter(2, mMap.get("findLocal"));
                        Logger.getLogger(GamacAmaAdmunDetalleTransFacade.class.getName()).log(Level.INFO, "findLocal: " + mMap.get("findLocal"));
                        break;  
                    case "2":
                        q.setParameter(2, mMap.get("findMarca"));
                        Logger.getLogger(GamacAmaAdmunDetalleTransFacade.class.getName()).log(Level.INFO, "findMarca: " + mMap.get("findMarca"));
                        break;
                    case "3":
                        q.setParameter(2, mMap.get("findCalibreArma"));
                        Logger.getLogger(GamacAmaAdmunDetalleTransFacade.class.getName()).log(Level.INFO, "findCalibreArma: " + mMap.get("findCalibreArma"));
                        break;
                    case "4":
                        q.setParameter(2, mMap.get("findDenominacion"));
                        Logger.getLogger(GamacAmaAdmunDetalleTransFacade.class.getName()).log(Level.INFO, "findDenominacion: " + mMap.get("findDenominacion"));
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
    
}
