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
import pe.gob.sucamec.bdintegrado.data.AmaSolicitudTarjeta;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class AmaSolicitudTarjetaFacade extends AbstractFacade<AmaSolicitudTarjeta> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaSolicitudTarjetaFacade() {
        super(AmaSolicitudTarjeta.class);
    }
    public List<AmaSolicitudTarjeta> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaSolicitudTarjeta a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<Map> buscarArmasNoRegularizadasSinTarjetaProp(Map items){
        try{
            String jpql_sinVerificar =  " SELECT ROWNUM AS ID_TEMP, A.*  FROM (" +
                                        " SELECT ST.NRO_EXPEDIENTE, EXP.FECHA_CREACION AS FECHA_EXPEDIENTE, A.NRO_LIC, A.NRO_SERIE, \n" +
                                        "  (CASE WHEN AI.ID IS NULL THEN ART.DES_ARM ELSE TA.NOMBRE END) AS DES_ARM, \n" +
                                        "  (CASE WHEN AI.ID IS NULL THEN MM.DES_MARCA ELSE MA.NOMBRE END) AS DES_MARCA, \n" +
                                        "  (CASE WHEN AI.ID IS NULL THEN MM.DES_MODELO ELSE M.MODELO END) AS DES_MODELO, \n" +
                                        "  (CASE WHEN AI.ID IS NULL THEN A.SIT_ARM ELSE AI.SITUACION_ID END) AS SIT_ARM, \n" +
                                        "  (CASE WHEN AI.ID IS NULL THEN SI.DES_SIT ELSE TG.NOMBRE END) AS DES_SIT, \n" +
                                        "  A.CALIBRE AS CALIBRE_DISCA, \n" +
                                        "  LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, \n" +
                                        "  EST.COD_PROG AS ESTADO_CODPROG, EST.NOMBRE AS ESTADO_NOMBRE, \n" +
                                        "  AI.ARMA_ID, AI.ID AS INVENTARIO_ID, GT.ID AS GT_ID, ST.ID AS SOLICITUD_ID, \n" +
                                        "  TI.NOMBRE AS TIPO_INTERNAMIENTO, TI.COD_PROG AS TIPO_INTERNAMIENTO_CODPROG " +
                                        " FROM RMA1369.AM_ARMA@SUCAMEC A \n" +
                                        " INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC \n" +
                                        " INNER JOIN RMA1369.SIT_ARMA@SUCAMEC SI ON SI.SIT_ARMA = A.SIT_ARM \n" +
                                        " LEFT JOIN RMA1369.ARTICULO@SUCAMEC ART ON ART.TIP_ART = A.TIP_ART AND ART.TIP_ARM = A.TIP_ARM \n" +
                                        " LEFT JOIN RMA1369.AM_MARCA@SUCAMEC MM ON MM.TIP_ART = A.TIP_ART AND MM.TIP_ARM = A.TIP_ARM AND MM.COD_MARCA = A.COD_MARCA AND MM.COD_MODELO = A.COD_MODELO \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_INVENTARIO_ARMA AI ON AI.LICENCIA_DISCA_ID = L.NRO_LIC AND AI.ACTUAL = 1 AND AI.ACTIVO = 1 \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO AGTI ON AGTI.INVENTARIO_ARMA_ID = AI.ID \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID = AGTI.GAMAC_GUIA_TRANSITO_ID AND GT.TIPO_GUIA_ID IN ("+ items.get("tipados").toString() +") AND GT.ACTIVO = 1 AND GT.ESTADO_ID=392 AND GT.DEVOLUCION_ID IS NULL \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_SOLICITUD_TARJETA ST ON ST.ACTA_INTERNAMIENTO_ID = GT.ID AND ST.ACTIVO = 1 \n" +
                                        " LEFT JOIN TRAMDOC.EXPEDIENTE EXP ON EXP.NUMERO = ST.NRO_EXPEDIENTE " +
                                        " LEFT JOIN BDINTEGRADO.AMA_ARMA AA ON AA.LICENCIA_DISCA = A.NRO_LIC \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON TP.ARMA_ID = AA.ID AND TP.ACTIVO = 1 \n" +
                                        " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = AI.SITUACION_ID \n" +
                                        " LEFT JOIN BDINTEGRADO.TIPO_GAMAC EST ON EST.ID = ST.ESTADO_ID  \n" +
                                        " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TI ON TI.ID = AI.TIPO_INTERNAMIENTO_ID \n"+
                                        " LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON M.ID = AI.MODELO_ID \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID  \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID \n" +
                                        " LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID \n" +
                                        " WHERE L.COD_USR = ?1 AND L.TIP_USR = 1 AND A.SIT_ARM != 16 AND A.NRO_LIC IN ( \n" +
                                        "                     SELECT A.NRO_LIC \n" +
                                        "                      FROM RMA1369.AM_ARMA@SUCAMEC A \n" +
                                        "                      INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC \n" +
                                        "                      WHERE L.COD_USR = ?1 AND L.TIP_USR = 1 \n" +
                                        "                     MINUS \n" +
                                        "                      SELECT A.LICENCIA_DISCA FROM BDINTEGRADO.AMA_ARMA A \n" +
                                        "                      INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON TP.ARMA_ID = A.ID\n" +
                                        "                      WHERE A.ACTIVO = 1 AND LICENCIA_DISCA IS NOT NULL  AND TP.ESTADO_ID != 13217 \n" +
                                        ") AND (TP.ID IS NULL OR (TP.ID IS NOT NULL AND (TP.MODALIDAD_ID = 13107 OR TP.ESTADO_ID = 13217)) ) \n" +   // PROVENIENTE DEL DISCA Y NULIDAD
                                        "  AND ( (AI.ID IS NULL AND GT.ID IS NULL AND TG.ID IS NULL) OR "+
                                        "        (AI.ID IS NOT NULL AND GT.ID IS NOT NULL AND TG.ID NOT IN (12912, 12225) ) " +   // DONADAS Y DESTRUIDAS
                                        "      ) ";
                                        
            if(items.get("tipoFiltro") != null){
                switch(items.get("tipoFiltro").toString()){
                    case "1":
                            jpql_sinVerificar += " AND ST.ID IS NULL AND AI.ID IS NOT NULL AND TI.COD_PROG NOT IN ('TP_INTER_DEF','TP_INTER_IDD') ";
                            break;
                    case "2":
                            jpql_sinVerificar += " AND ST.ID IS NOT NULL ";
                            break;
                }
            }
            if(items.get("tipoBusqueda") != null){
                switch(items.get("tipoBusqueda").toString()){
                    case "licencia":
                            jpql_sinVerificar += " AND A.NRO_LIC = ?2 ";
                            break;
                    case "serie":
                            jpql_sinVerificar += " AND A.NRO_SERIE = ?2 ";
                            break;
                    case "estado":
                            jpql_sinVerificar += " AND ST.ESTADO_ID = ?2 ";
                            break;
                }
            }
            jpql_sinVerificar += " GROUP BY ST.NRO_EXPEDIENTE, EXP.FECHA_CREACION, A.NRO_LIC, A.NRO_SERIE, A.CALIBRE, EST.COD_PROG, EST.NOMBRE,  \n" +
                                 "  ART.DES_ARM, TA.NOMBRE, MM.DES_MARCA , MA.NOMBRE, MM.DES_MODELO, M.MODELO, A.SIT_ARM, AI.SITUACION_ID, SI.DES_SIT, TG.NOMBRE, AI.ARMA_ID, AI.ID, GT.ID, ST.ID, \n" +
                                 "  TI.NOMBRE, TI.COD_PROG " +
                                 "  ORDER BY 1 ASC, ST.ID ASC  " +
                                 ") A";
            
            Query q = em.createNativeQuery(jpql_sinVerificar);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, items.get("ruc"));
            
            if(items.get("tipoBusqueda") != null){
                switch(items.get("tipoBusqueda").toString()){
                    case "licencia":
                            q.setParameter(2, Long.parseLong(items.get("filtro").toString()));
                            break;
                    case "serie":
                            q.setParameter(2, items.get("filtro").toString());
                            break;
                    case "estado":
                            q.setParameter(2, (long) items.get("tipoEstado"));
                            break;
                }
            }

            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public AmaSolicitudTarjeta buscarSolicitudTarjetaById(Long solicitudId) {
        Query q = em.createQuery("select a from AmaSolicitudTarjeta a where a.activo = 1 and a.id = :solicitudId ");
        q.setParameter("solicitudId", solicitudId);
        
        if(!q.getResultList().isEmpty()){
            return (AmaSolicitudTarjeta) q.getResultList().get(0);
        }
        return null;
    }    
}
