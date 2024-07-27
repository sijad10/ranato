/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacAmaMunicionFacade extends AbstractFacade<GamacAmaMunicion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacAmaMunicionFacade() {
        super(GamacAmaMunicion.class);
    }
    
    public List<GamacAmaMunicion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from GamacAmaMunicion a where trim(a.id) like :id");
        q.setParameter("id",s);
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public String listarIdsCatalogoMuniciones() {
        String sql = "select \n"
                + "MIN(a.id) AS ID\n"
                + "from BDINTEGRADO.AMA_MUNICION a \n"
                + "left join bdintegrado.ama_catalogo c on c.id=a.MARCA_ID\n"
                + "left join bdintegrado.ama_catalogo ca on ca.id=a.CALIBREARMA_ID\n"
                + "left join bdintegrado.tipo_gamac m on m.id=a.TIPO_MUNICION_ID\n"
                + "group by ca.nombre , c.nombre , m.nombre";
        Query nq = em.createNativeQuery(sql);
        nq.setHint("eclipselink.result-type", "Map");
        List<Map> mapLst = (List<Map>) nq.getResultList();
        String idsValidos = "";
        for (Map mapa : mapLst) {
            if (idsValidos.equals("")) {
                idsValidos = "" + mapa.get("ID");
            } else {
                idsValidos = idsValidos + ", " + mapa.get("ID");
            }
        }
        return idsValidos;
    }

    public List<GamacAmaMunicion> listxCalibre(String calibreId, String s) {
        if (s == null) {
            s = "";
        }
        try {
            
            String idsValidos = listarIdsCatalogoMuniciones();
            
            String jpsql = "select distinct"
                    + " mu"
                    + " from GamacAmaMunicion mu"
                    + " inner join mu.calibrearmaId ca"
                    + " where"
                    + " ca.id = :calibreId "
                    + " and ("
                    + " mu.marcaId.nombre like :nombreMarca or ca.nombre like :nombreCalibre"
                    + " )"
                    + " and mu.id in ("+ idsValidos + ") "
                    + "";
            Query q = em.createQuery(jpsql);
            q.setParameter("calibreId", Long.valueOf(calibreId));
            q.setParameter("nombreMarca", "%" + s.trim().toUpperCase() + "%");
            q.setParameter("nombreCalibre", "%" + s.trim().toUpperCase() + "%"); 

            q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GamacAmaMunicion> listxPerJur(String personaId, String s) {
        if (s == null) {
            s = "";
        }
        try {
            String idsValidos = listarIdsCatalogoMuniciones();
            
            String jpsql = "select distinct"
                    + " mu"
                    + " from GamacAmaMunicion mu"
                    + " inner join mu.calibrearmaId ca"
                    + " inner join ca.gamacAmaModelosCollection mc"
                    + " inner join mc.gamacAmaArmaCollection ar"
                    + " inner join ar.gamacAmaTarjetaPropiedadCollection ta"
                    + " inner join ta.personaCompradorId pe"
                    + " where"
                    + " pe.id = :personaId "
                    + " and ("
                    + " mu.marcaId.nombre like :nombreMarca or ca.nombre like :nombreCalibre"
                    + " )"
                    + " and mu.id in ("+ idsValidos + ") "
                    + "";
            Query q = em.createQuery(jpsql);
            q.setParameter("personaId", Long.valueOf(personaId));
            q.setParameter("nombreMarca", "%" + s.trim().toUpperCase() + "%");
            q.setParameter("nombreCalibre", "%" + s.trim().toUpperCase() + "%"); 
            q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GamacAmaMunicion> listxPerJurCom(String s) {
        if (s == null) {
            s = "";
        }
        try {
            String idsValidos = listarIdsCatalogoMuniciones();
            
            String jpsql = "select distinct"
                    + " mu"
                    + " from GamacAmaMunicion mu"
                    + " inner join mu.calibrearmaId ca"
                    + " inner join ca.gamacAmaModelosCollection mc"
                    + " inner join mc.gamacAmaArmaCollection ar"
                    + " inner join ar.gamacAmaTarjetaPropiedadCollection ta"
                    + " where"
                    + " ("
                    + " mu.marcaId.nombre like :nombreMarca or ca.nombre like :nombreCalibre"
                    + " )"
                    + " and mu.id in ("+ idsValidos + ") "
                    + "";
            Query q = em.createQuery(jpsql);
            q.setParameter("nombreMarca", "%" + s.trim().toUpperCase() + "%");
            q.setParameter("nombreCalibre", "%" + s.trim().toUpperCase() + "%");   
            q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**public List<GamacAmaMunicion> listxMarcaCalibre(String s) {
        if (s == null) {
            s = "";
        }
        try {
            String jpsql = "select distinct"
                    + " mu"
                    + " from GamacAmaMunicion mu"
                    + " inner join mu.calibrearmaId ca"
                    //+ " inner join ca.gamacAmaModelosCollection mc"
                    + " where"
                    + " ("
                    + " mu.marcaId.nombre like :nombreMarca or ca.nombre like :nombreCalibre or mu.modelo like :nombreModelo"
                    + " )"
                    + " order by mu.marcaId.nombre, mu.modelo";
            Query q = em.createQuery(jpsql);
            q.setParameter("nombreMarca", "%" + s.trim().toUpperCase() + "%");
            q.setParameter("nombreModelo", "%" + s.trim().toUpperCase() + "%");
            q.setParameter("nombreCalibre", "%" + s.trim().toUpperCase() + "%");        
            q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/
    
    public List<GamacAmaMunicion> obtenerPorMarcaCalibre(String s) {
        if (s == null) {
            s = "";
        }
        String idsValidos = listarIdsCatalogoMuniciones();
        Query q = em.createQuery(
                "select distinct(a) "
                        + "from GamacAmaMunicion a "
                        + "where ("
                        + "a.marcaId.nombre like :valor "
                        + "or a.calibrearmaId.nombre like :valor "
                        + "or a.tipoMunicionId.nombre like :valor"
                        + ") "
                        + "and a.activo = 1 "
                        + "and a.id in ("+ idsValidos + ") ");

        q.setParameter("valor", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<ArrayRecord> stockMunicion(HashMap mMap) {
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
                    + "  AND det.ARTICULO_MUNICION_ID=?2"
                    + "  AND cab.LOCALCOMERCIAL_ID=?3"
                    //+ "   --cab.TIPO_TRANSACCION_ID"
                    //+ "  ORDER BY det.ID"
                    + "";
            
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
            Query q = em.createNativeQuery(sql);

            q.setParameter(1, (Long) mMap.get("agenteComerId"));
            q.setParameter(2, (Long) mMap.get("municionId"));
            q.setParameter(3, (Long) mMap.get("localId"));            
            //System.out.println("qry: " + sql);
            //Syso("sql Stock:" + sql + mMap.get("agenteComerId") + "-" + mMap.get("localId") + "-" + mMap.get("municionId"));
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
