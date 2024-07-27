/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.rma1369.bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class amaArmaFacade {

    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager em;
    @PersistenceContext(unitName = "SELPU")
    private EntityManager emRenagi;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    protected EntityManager getEntityManagerRenagi() {
        return emRenagi;
    }

    public amaArmaFacade() {
        super();
    }
    
    public Map buscarArmaRMA(String nroLicencia){
        try {
            String jpql = "SELECT AR.NRO_LIC, AR.NRO_SERIE,AR.TIP_ARM AS TIP_ARM, ART.DES_ARM AS DES_ARM, AR.COD_MARCA AS COD_MARCA, " +
                            " MARC.DES_MARCA AS DES_MARCA,AR.COD_MODELO, MARC.DES_MODELO, AR.CALIBRE, SI.DES_SIT ESTADO " +
                            " FROM RMA1369.AM_ARMA AR " +
                            " INNER JOIN RMA1369.SIT_ARMA SI ON SI.SIT_ARMA = AR.SIT_ARM " + 
                            " LEFT JOIN RMA1369.ARTICULO ART ON ART.TIP_ART = AR.TIP_ART AND ART.TIP_ARM = AR.TIP_ARM " +
                            " LEFT JOIN RMA1369.AM_MARCA MARC ON MARC.TIP_ART = AR.TIP_ART AND MARC.TIP_ARM = AR.TIP_ARM AND MARC.COD_MARCA = AR.COD_MARCA AND MARC.COD_MODELO = AR.COD_MODELO " +
                            " WHERE AR.NRO_LIC = ?1 ";
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, nroLicencia);
            
            if(q.getResultList().isEmpty()){
                return null;
            }else{
                return (Map) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public Map buscarArmaRMABySerie(String serie){
        try {
            String jpql = "SELECT AR.NRO_LIC, AR.NRO_SERIE,AR.TIP_ARM AS TIP_ARM, ART.DES_ARM AS DES_ARM, AR.COD_MARCA AS COD_MARCA, " +
                            " MARC.DES_MARCA AS DES_MARCA,AR.COD_MODELO, MARC.DES_MODELO, AR.CALIBRE, SI.DES_SIT ESTADO " +
                            " FROM RMA1369.AM_ARMA AR " +
                            " INNER JOIN RMA1369.SIT_ARMA SI ON SI.SIT_ARMA = AR.SIT_ARM " + 
                            " LEFT JOIN RMA1369.ARTICULO ART ON ART.TIP_ART = AR.TIP_ART AND ART.TIP_ARM = AR.TIP_ARM " +
                            " LEFT JOIN RMA1369.AM_MARCA MARC ON MARC.TIP_ART = AR.TIP_ART AND MARC.TIP_ARM = AR.TIP_ARM AND MARC.COD_MARCA = AR.COD_MARCA AND MARC.COD_MODELO = AR.COD_MODELO " +
                            " WHERE AR.SERIE = ?1 ";
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, serie);
            
            if(q.getResultList().isEmpty()){
                return null;
            }else{
                return (Map) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Map> buscarArmasRegularizacionJuridicas(Map mMap, boolean isFinalizado) {
        try{
            String jpql = "";
            
            // Sin verificar
            // 16	DESTRUIDO  no debe tomar
            // 17	DONADO     no debe tomar
            // 31	PARA INT POR ORDEN DE DECOMISO  no debe tomar
            String jpql_sinverificar = "SELECT DISTINCT 5 AS TIPO, 5 AS ESTADO_VERFICA, NULL AS ID,NULL AS NROEXP, NULL AS FECEXP, A.NRO_LIC, A.NRO_SERIE, A.TIP_ARM, ART.DES_ARM, A.COD_MARCA, \n"+
                            " M.DES_MARCA, A.COD_MODELO, M.DES_MODELO, A.CALIBRE,  NULL AS FEC_VEN, A.SIT_ARM AS COD_ESTADO, SI.DES_SIT ESTADO, NULL AS PROCESO_ID, NULL AS EXPEDIENTE_MASTER \n" +
                            " FROM RMA1369.AM_ARMA@SUCAMEC A \n" +
                            " INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC \n" +
                            " INNER JOIN RMA1369.SIT_ARMA@SUCAMEC SI ON SI.SIT_ARMA = A.SIT_ARM \n" +
                            " LEFT JOIN RMA1369.ARTICULO@SUCAMEC ART ON ART.TIP_ART = A.TIP_ART AND ART.TIP_ARM = A.TIP_ARM \n" +
                            " LEFT JOIN RMA1369.AM_MARCA@SUCAMEC M ON M.TIP_ART = A.TIP_ART AND M.TIP_ARM = A.TIP_ARM AND M.COD_MARCA = A.COD_MARCA AND M.COD_MODELO = A.COD_MODELO \n" +
                            " WHERE L.COD_USR = ?1 AND L.TIP_USR = 1 AND A.SIT_ARM NOT IN (16,17,31) AND A.NRO_LIC IN ( \n" +
                            "      SELECT A.NRO_LIC \n" +
                            "       FROM RMA1369.AM_ARMA@SUCAMEC A  \n" +
                            "       INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC \n" +
                            "       WHERE L.COD_USR = ?1 AND L.TIP_USR = 1 \n" +
                            "      MINUS  \n" +
                            "       SELECT A.LIC_DISCA_ID FROM BDINTEGRADO.AMA_REG_EMP_ARMA A WHERE A.ACTIVO = 1 AND (TIPO_REGU_ID = 13497 OR EXPEDIENTE_MASTER IS NOT NULL) \n" +
                            "      MINUS \n" +
                            "       SELECT A.LICENCIA_DISCA FROM BDINTEGRADO.AMA_ARMA A WHERE A.ACTIVO = 1 \n"+
                            "  )";
            
           // Nuevo
            String jpql_generico = "SELECT ESTADO_VERFICA AS TIPO, ESTADO_VERFICA, A.ID,EXPEDIENTE AS NROEXP, EX.FECHA_CREACION AS FECEXP, LIC_DISCA_ID AS NRO_LIC, \n" +
                                    " SERIE AS NRO_SERIE,T.ID AS TIP_ARM,  T.NOMBRE AS DES_ARM, MA.ID AS COD_MARCA, \n" +
                                    " MA.NOMBRE AS DES_MARCA, M.ID AS COD_MODELO,M.MODELO AS DES_MODELO, LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, \n"+
                                    " NULL AS FEC_VEN, A.SITUACION_ARMA AS COD_ESTADO, TG.NOMBRE AS ESTADO, NULL AS PROCESO_ID, A.EXPEDIENTE_MASTER \n" +
                                    " FROM BDINTEGRADO.AMA_REG_EMP_ARMA A  \n" +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = A.TIPO_REGU_ID \n" +
                                    " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = A.SITUACION_ARMA \n" +
                                    " LEFT JOIN TRAMDOC.EXPEDIENTE EX ON EX.NUMERO = A.EXPEDIENTE \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON A.MODELO_ID = M.ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO T ON M.TIPO_ARMA_ID = T.ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID \n" +
                                    " WHERE A.ACTIVO = 1 AND A.PERSONA_ID = ?3 AND A.ESTADO_VERFICA != 4  \n"+
                                    " AND (A.MODELO_ID, A.SERIE, A.LIC_DISCA_ID) NOT IN (SELECT AR.MODELO_ID, AR.SERIE, AR.LICENCIA_DISCA FROM BDINTEGRADO.AMA_ARMA AR \n" +
                                    "       INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = AR.ID \n" +
                                    "       WHERE AR.ACTIVO = 1 AND T.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?3) AND TREG.COD_PROG = 'TP_PR_RAESS_SEG' \n";

            // Procesados (con tarjeta de propiedad)
            String jpql_procesados = "SELECT DISTINCT 4 AS TIPO, REG.ESTADO_VERFICA, REG.ID AS ID, EX.NUMERO AS NROEXP, EX.FECHA_CREACION AS FECEXP, A.LICENCIA_DISCA AS NRO_LIC, A.SERIE AS NRO_SERIE, \n"+
                                    " TA.ID AS TIP_ARM, TA.NOMBRE AS DES_ARM, MA.ID AS COD_MARCA, MA.NOMBRE AS DES_MARCA, M.ID AS COD_MODELO, M.MODELO AS DES_MODELO, \n" +
                                    " LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, NULL AS FEC_VEN, A.ESTADO_ID AS COD_ESTADO, TG.NOMBRE AS ESTADO, PROC.ID AS PROCESO_ID, REG.EXPEDIENTE_MASTER \n" +
                                    " FROM BDINTEGRADO.AMA_ARMA A \n" +
                                    " INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = A.ID  \n" +
                                    " INNER JOIN TRAMDOC.EXPEDIENTE EX ON EX.NUMERO = T.NRO_EXPEDIENTE \n" +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = A.SITUACION_ID \n" +
                                    " INNER JOIN BDINTEGRADO.AMA_REG_EMP_ARMA REG ON REG.PERSONA_ID = T.PERSONA_COMPRADOR_ID AND REG.SERIE = A.SERIE AND (A.MODELO_ID = REG.MODELO_ID OR A.LICENCIA_DISCA = REG.LIC_DISCA_ID) \n" +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = REG.TIPO_REGU_ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_REG_PRO_ARMA RPRO ON RPRO.REG_ARMA_ID = REG.ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_REG_EMP_PROCESO PROC ON PROC.ID = RPRO.REG_PROCESO_ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON A.MODELO_ID = M.ID  \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID  \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID  \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID  \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID  \n" +
                                    " WHERE T.ACTIVO = 1 AND REG.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?3 AND A.ACTIVO = 1 AND TREG.COD_PROG = 'TP_PR_RAESS_SEG'  \n";

            if(mMap.get("tipoBusqueda") != null){
                switch(mMap.get("tipoBusqueda").toString()){
                    case "1": 
                            jpql_sinverificar = jpql_sinverificar + " AND A.NRO_LIC = ?2 ";
                            jpql_generico = jpql_generico + " AND LIC_DISCA_ID = ?2 ";                            
                            jpql_procesados = jpql_procesados + " AND A.LICENCIA_DISCA = ?2 ";
                            break;
                    case "2":
                            jpql_sinverificar = jpql_sinverificar + " AND A.NRO_SERIE = ?2 ";
                            jpql_generico = jpql_generico + " AND SERIE = ?2 ";
                            jpql_procesados = jpql_procesados + " AND A.SERIE = ?2 ";
                            break;
                }
            }
            
            if(mMap.get("tipoEstado") != null){
                switch(mMap.get("tipoEstado").toString()){
                    case "sinverificar":
                                        jpql = jpql_sinverificar;
                                        if(isFinalizado){
                                            return null;
                                        }
                                        break;
                    case "verificado":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 1 \n"+                                        
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE, A.EXPEDIENTE_MASTER";
                                        break;
                    case "nopertenece":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 2 \n"+
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE, A.EXPEDIENTE_MASTER";
                                        break;
                    case "nuevo":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 3 \n"+
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE, A.EXPEDIENTE_MASTER";
                                        break;
                    case "emitidos":
                                        jpql =  jpql_procesados +
                                                " GROUP BY REG.ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID, REG.EXPEDIENTE_MASTER ";
                                        
                                        break;
                }
            }else{
                jpql_generico = jpql_generico + " GROUP BY ESTADO_VERFICA,A.ID,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE, A.EXPEDIENTE_MASTER";
                jpql_procesados = jpql_procesados + " GROUP BY REG.ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID, REG.EXPEDIENTE_MASTER ";
                        
                jpql = ((isFinalizado)?"":jpql_sinverificar + " UNION \n") + jpql_generico + " UNION \n"+ jpql_procesados+ " ORDER BY ID ASC";
            }
            
            Query q = emRenagi.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            
            if(mMap.get("tipoBusqueda") != null){
                switch(mMap.get("tipoBusqueda").toString()){
                    case "1":
                    case "2":
                            q.setParameter(2, mMap.get("filtro"));
                            break;
                    case "3":
                            break;
                }
            }
            if(mMap.get("tipoEstado") != null){
                switch(mMap.get("tipoEstado").toString()){
                    case "sinverificar":
                            q.setParameter(1, mMap.get("ruc").toString());
                            break;
                    default:
                            q.setParameter(3, (Long) mMap.get("idEmpresa"));
                            break;
                }
            }else{
                q.setParameter(3, (Long) mMap.get("idEmpresa"));
                if(!isFinalizado){
                    q.setParameter(1, mMap.get("ruc").toString());
                }
            }

            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            return null;
        }
    }
    
public List<Map> buscarArmasRegularizacionInpe(Map mMap, boolean isFinalizado) {
        try{
            String jpql = "";
            
            // Sin verificar
            // 16	DESTRUIDO  no debe tomar
            // 17	DONADO     no debe tomar
            // 31	PARA INT POR ORDEN DE DECOMISO  no debe tomar
            String jpql_sinverificar = "SELECT DISTINCT 5 AS TIPO, 5 AS ESTADO_VERFICA, NULL AS ID,NULL AS NROEXP, NULL AS FECEXP, A.NRO_LIC, A.NRO_SERIE, A.TIP_ARM, ART.DES_ARM, A.COD_MARCA, "+
                            " M.DES_MARCA, A.COD_MODELO, M.DES_MODELO, A.CALIBRE,  NULL AS FEC_VEN, A.SIT_ARM AS COD_ESTADO, SI.DES_SIT ESTADO, NULL AS PROCESO_ID, NULL AS SERIE_PROP " +
                            " FROM RMA1369.AM_ARMA@SUCAMEC A " +
                            " INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC " +
                            " INNER JOIN RMA1369.SIT_ARMA@SUCAMEC SI ON SI.SIT_ARMA = A.SIT_ARM " +
                            " LEFT JOIN RMA1369.ARTICULO@SUCAMEC ART ON ART.TIP_ART = A.TIP_ART AND ART.TIP_ARM = A.TIP_ARM " +
                            " LEFT JOIN RMA1369.AM_MARCA@SUCAMEC M ON M.TIP_ART = A.TIP_ART AND M.TIP_ARM = A.TIP_ARM AND M.COD_MARCA = A.COD_MARCA AND M.COD_MODELO = A.COD_MODELO " +
                            " WHERE L.COD_USR = ?1 AND L.TIP_USR = 1 AND A.SIT_ARM NOT IN (16,17,31) AND A.NRO_LIC IN ( " +
                            "      SELECT A.NRO_LIC" +
                            "       FROM RMA1369.AM_ARMA@SUCAMEC A" +
                            "       INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC" +
                            "       WHERE L.COD_USR = ?1 AND L.TIP_USR = 1" +
                            "      MINUS" +
                            "       SELECT A.LIC_DISCA_ID FROM BDINTEGRADO.AMA_REG_EMP_ARMA A WHERE A.ACTIVO = 1" +
                            "      MINUS" +
                            "       SELECT A.LICENCIA_DISCA FROM BDINTEGRADO.AMA_ARMA A WHERE A.ACTIVO = 1 "+
                            "  )";
            
           // Nuevo
            String jpql_generico = "SELECT ESTADO_VERFICA AS TIPO, ESTADO_VERFICA, A.ID,EXPEDIENTE AS NROEXP, EX.FECHA_CREACION AS FECEXP, LIC_DISCA_ID AS NRO_LIC," +
                                    " SERIE AS NRO_SERIE,T.ID AS TIP_ARM,  T.NOMBRE AS DES_ARM, MA.ID AS COD_MARCA, " +
                                    " MA.NOMBRE AS DES_MARCA, M.ID AS COD_MODELO,M.MODELO AS DES_MODELO, LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, "+
                                    " NULL AS FEC_VEN, A.SITUACION_ARMA AS COD_ESTADO, TG.NOMBRE AS ESTADO, NULL AS PROCESO_ID, A.SERIE_PROPUESTA AS SERIE_PROP " +
                                    " FROM BDINTEGRADO.AMA_REG_EMP_ARMA A" +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = A.TIPO_REGU_ID \n" +
                                    " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = A.SITUACION_ARMA " +
                                    " LEFT JOIN TRAMDOC.EXPEDIENTE EX ON EX.NUMERO = A.EXPEDIENTE" +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON A.MODELO_ID = M.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO T ON M.TIPO_ARMA_ID = T.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID " +
                                    " WHERE A.ACTIVO = 1 AND A.PERSONA_ID = ?3 AND A.ESTADO_VERFICA != 4  "+
                                    " AND (A.MODELO_ID, A.SERIE, A.LIC_DISCA_ID) NOT IN (SELECT AR.MODELO_ID, AR.SERIE, AR.LICENCIA_DISCA FROM BDINTEGRADO.AMA_ARMA AR " +
                                    "       INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = AR.ID " +
                                    "       WHERE AR.ACTIVO = 1 AND T.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?3) AND TREG.COD_PROG = 'TP_PR_RAESS_INP' ";
            
            // Emitidos
            String jpql_emitidos = "SELECT DISTINCT 4 AS TIPO, REG.ESTADO_VERFICA, REG.ID AS ID, EX.NUMERO AS NROEXP, EX.FECHA_CREACION AS FECEXP, A.LICENCIA_DISCA AS NRO_LIC, A.SERIE AS NRO_SERIE," +
                                    "  TA.ID AS TIP_ARM, TA.NOMBRE AS DES_ARM,MA.ID AS COD_MARCA, MA.NOMBRE AS DES_MARCA, M.ID AS COD_MODELO, M.MODELO AS DES_MODELO," +
                                    "  LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, NULL AS FEC_VEN, A.ESTADO_ID AS COD_ESTADO, TG.NOMBRE AS ESTADO, PROC.ID AS PROCESO_ID, NULL AS SERIE_PROP " +
                                    " FROM BDINTEGRADO.AMA_ARMA A" +
                                    "  INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = A.ID" +
                                    "  INNER JOIN TRAMDOC.EXPEDIENTE EX ON EX.NUMERO = T.NRO_EXPEDIENTE" +
                                    "  INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = A.ESTADO_ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_REG_EMP_ARMA REG ON REG.LIC_DISCA_ID = A.LICENCIA_DISCA AND T.PERSONA_COMPRADOR_ID = REG.PERSONA_ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_REG_PRO_ARMA RPRO ON RPRO.REG_ARMA_ID = REG.ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_REG_EMP_PROCESO PROC ON PROC.ID = RPRO.REG_PROCESO_ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON A.MODELO_ID = M.ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID" +
                                    "  LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID" +
                                    "  LEFT JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID" +
                                    "  LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID" +
                                    " WHERE T.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?3 AND A.ACTIVO = 1 AND A.LICENCIA_DISCA IS NOT NULL AND REG.ESTADO_VERFICA IS NULL";

            // Procesados (con tarjeta de propiedad)
            String jpql_procesados = "SELECT DISTINCT 4 AS TIPO, REG.ESTADO_VERFICA, REG.ID AS ID, EX.NUMERO AS NROEXP, EX.FECHA_CREACION AS FECEXP, A.LICENCIA_DISCA AS NRO_LIC, A.SERIE AS NRO_SERIE, "+
                                    " TA.ID AS TIP_ARM, TA.NOMBRE AS DES_ARM, MA.ID AS COD_MARCA, MA.NOMBRE AS DES_MARCA, M.ID AS COD_MODELO, M.MODELO AS DES_MODELO, " +
                                    " LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, NULL AS FEC_VEN, A.ESTADO_ID AS COD_ESTADO, TG.NOMBRE AS ESTADO, PROC.ID AS PROCESO_ID, NULL AS SERIE_PROP " +
                                    " FROM BDINTEGRADO.AMA_ARMA A " +
                                    " INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = A.ID  " +
                                    " INNER JOIN TRAMDOC.EXPEDIENTE EX ON EX.NUMERO = T.NRO_EXPEDIENTE " +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = A.SITUACION_ID " +
                                    " INNER JOIN BDINTEGRADO.AMA_REG_EMP_ARMA REG ON REG.PERSONA_ID = T.PERSONA_COMPRADOR_ID AND REG.SERIE = A.SERIE AND (A.MODELO_ID = REG.MODELO_ID OR A.LICENCIA_DISCA = REG.LIC_DISCA_ID) " +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = REG.TIPO_REGU_ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_REG_PRO_ARMA RPRO ON RPRO.REG_ARMA_ID = REG.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_REG_EMP_PROCESO PROC ON PROC.ID = RPRO.REG_PROCESO_ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON A.MODELO_ID = M.ID  " +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID " +
                                    " WHERE T.ACTIVO = 1 AND REG.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?3 AND A.ACTIVO = 1 AND TREG.COD_PROG = 'TP_PR_RAESS_INP'  ";

            if(mMap.get("tipoBusqueda") != null){
                switch(mMap.get("tipoBusqueda").toString()){
                    case "1": 
                            jpql_sinverificar = jpql_sinverificar + " AND A.NRO_LIC = ?2 ";
                            jpql_generico = jpql_generico + " AND LIC_DISCA_ID = ?2 ";                            
                            jpql_emitidos = jpql_emitidos + " AND A.LICENCIA_DISCA = ?2 ";
                            jpql_procesados = jpql_procesados + " AND A.LICENCIA_DISCA = ?2 ";
                            break;
                    case "2":
                            jpql_sinverificar = jpql_sinverificar + " AND A.NRO_SERIE = ?2 ";
                            jpql_generico = jpql_generico + " AND SERIE = ?2 ";
                            jpql_emitidos = jpql_emitidos + " AND A.SERIE = ?2 ";
                            jpql_procesados = jpql_procesados + " AND A.SERIE = ?2 ";
                            break;
                }
            }
            if(mMap.get("regionInpeSelected") != null){                
                jpql_generico = jpql_generico + " AND A.REGION_ID = ?7 ";
                jpql_emitidos = jpql_emitidos + " AND ( REG.ID IS NULL OR (REG.ID IS NOT NULL AND REG.REGION_ID = ?7) ) ";
                jpql_procesados = jpql_procesados + " AND REG.REGION_ID = ?7 ";
            }
            if(mMap.get("tipoEstado") != null){
                switch(mMap.get("tipoEstado").toString()){
                    case "sinverificar":
                                        jpql = jpql_sinverificar;
                                        if(mMap.get("regionInpeSelected") != null){
                                            return null;
                                        }
                                        if(isFinalizado){
                                            return null;
                                        }
                                        break;
                    case "verificado":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 1 "+                                        
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE,A.SERIE_PROPUESTA";
                                        break;
                    case "nopertenece":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 2 "+
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE,A.SERIE_PROPUESTA";
                                        break;
                    case "nuevo":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 3 "+
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE,A.SERIE_PROPUESTA";
                                        break;
                    case "emitidos":
                                        jpql = jpql_emitidos+
                                                " GROUP BY ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID" +
                                                " UNION " +
                                                jpql_procesados +
                                                " GROUP BY REG.ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID ";
                                        
                                        break;
                }
            }else{
                jpql_generico = jpql_generico + " GROUP BY ESTADO_VERFICA,A.ID,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE,A.SERIE_PROPUESTA";
                jpql_emitidos = jpql_emitidos + " GROUP BY ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID";
                jpql_procesados = jpql_procesados + " GROUP BY REG.ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID ";
                        
                jpql = ((isFinalizado || mMap.get("regionInpeSelected") != null)?"":jpql_sinverificar + " UNION ") + jpql_generico + " UNION "+ jpql_emitidos + " UNION "+ jpql_procesados+ " ORDER BY ID ASC";
            }
            
            Query q = emRenagi.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            
            if(mMap.get("tipoBusqueda") != null){
                switch(mMap.get("tipoBusqueda").toString()){
                    case "1":
                    case "2":
                            q.setParameter(2, mMap.get("filtro"));
                            break;
                    case "3":
                            break;
                }
            }
            if(mMap.get("tipoEstado") != null){
                switch(mMap.get("tipoEstado").toString()){
                    case "sinverificar":
                            q.setParameter(1, mMap.get("ruc").toString());
                            break;
                    default:
                            q.setParameter(3, (Long) mMap.get("idEmpresa"));
                            if(mMap.get("regionInpeSelected") != null){
                                q.setParameter(7, (Long) mMap.get("regionInpeSelected"));    
                            }
                            break;
                }
            }else{
                q.setParameter(3, (Long) mMap.get("idEmpresa"));
                if(!isFinalizado){
                    q.setParameter(1, mMap.get("ruc").toString());
                }
                if(mMap.get("regionInpeSelected") != null){
                    q.setParameter(7, (Long) mMap.get("regionInpeSelected"));    
                }
            }
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            return null;
        }
    }

    public int contarArmasConLicenciaDuplicadas(Long licencia) {
        int cont = 0;
        String jpql = "SELECT COUNT(*) FROM (\n" +
                        "    SELECT NRO_SERIE,TIP_ART,TIP_ARM,COD_MARCA,COD_MODELO,CALIBRE,L.COD_USR,COUNT(DISTINCT A.NRO_LIC),LISTAGG(A.NRO_LIC,', ') WITHIN GROUP (ORDER BY A.NRO_LIC ASC) NRO_LICS    \n" +
                        "    FROM RMA1369.AM_ARMA@SUCAMEC A \n" +
                        "    INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON A.NRO_LIC=L.NRO_LIC \n" +
                        "    LEFT JOIN BDINTEGRADO.AMA_ARMA AR ON AR.LICENCIA_DISCA = L.NRO_LIC\n" +
                        "    WHERE LENGTH(L.COD_USR)>=11 \n" +
                        "    GROUP BY NRO_SERIE,TIP_ART,TIP_ARM,COD_MARCA,COD_MODELO,CALIBRE,L.COD_USR \n" +
                        "    HAVING COUNT(DISTINCT A.NRO_LIC)>1   \n" +
                        " ) \n" +
                        " WHERE NRO_LICS LIKE ?1 ";
        
        Query q = emRenagi.createNativeQuery(jpql);
        q.setParameter(1, "%"+ licencia + "%");        
        
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
       
    
}
