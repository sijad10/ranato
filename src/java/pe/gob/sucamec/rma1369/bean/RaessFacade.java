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
public class RaessFacade {

    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public RaessFacade() {
        super();
    }

    public int contarResolucionesVigentesEmpresa(String ruc){
        int cont = 0;
        try{
            String jpql = "SELECT COUNT(*) FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC "+
                          " WHERE TIP_MOD in (1,3,5) " + //AND FEC_VEN >= CURRENT_DATE " +
                          " AND RUC = ?1 ";

            Query q = em.createNativeQuery(jpql);
            q.setParameter(1, ruc);

            List<BigDecimal> results = q.getResultList();
            for (BigDecimal _values : results) {
                cont = _values.intValue();
                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cont;
    }    
    
    public List<Map> obtenerListadoSituacionesArma(){
        try{
            String jpql = "SELECT * FROM RMA1369.SIT_ARMA@SUCAMEC ";

            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }  
    
    public List<String> buscarClaveActaInternamientoXSerie(String serie){
        try {
            String jpql = "SELECT DISTINCT CLAVE FROM ARMASINT.VS_ARMASITUA_REPORTE@SUCAMEC " +
                            " WHERE SERIE = ?1 AND CLAVE IS NOT NULL ";
            
            Query q = em.createNativeQuery(jpql);
            q.setParameter(1, serie);            
            
            if(q.getResultList().isEmpty()){
                return null;
            }
            return (List<String>) q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Map buscarActaInternamientoXSerieXClave(String serie, String clave){
        try {
            String jpql = "SELECT AUTOGENE, TDEPAR_DES, PROCED, DOCUM, FECHA, MOTIVO, \"TIPO ARMA\" as TIPO_ARMA, MARCA, MODELO, CALIBRE,CONSERVACION, FUNCIONAMIENTO, SITUACION, SERIE "+
                            " FROM ARMASINT.VS_ARMASITUA_REPORTE@SUCAMEC " +
                            " WHERE SERIE = ?1 AND CLAVE = ?2 ";
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, serie.trim());
            q.setParameter(2, clave.trim());
            
            if(q.getResultList().isEmpty()){
                return null;
            }
            return (Map) q.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
