/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.rma1369.bean;

import java.util.ArrayList;
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
public class CarteraClienteFacade {

    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CarteraClienteFacade() {
        super();
    }
    
    public List<Map> buscarVigilanteCarteraCli(String tipoDoc, String numDoc, Long ruc) {
        try{
            String jpql = "SELECT DISTINCT E.TIP_USR, (CASE WHEN E.TIP_USR = 2 THEN ''||E.COD_USR ELSE EXT.CRNT_EXT END) AS DOC, "+
                            " (CASE WHEN E.TIP_USR = 2 THEN C.NOMBRE ELSE EXT.NOMBRE END) AS NOMBRES, " +
                            " (CASE WHEN E.TIP_USR = 2 THEN C.APE_PAT||' '||C.APE_MAT ELSE EXT.APE_PAT||' '||EXT.APE_MAT END) AS APELLIDOS, " +
                            " E.NRO_CRN_VIG, E.FEC_EMI, E.FEC_VENC, D.DES_MOD AS MODALIDAD " +
                            " FROM RMA1369.SS_EMP_VIG E" +
                            " JOIN RMA1369.EMPRESA B ON E.RUC = B.RUC" +
                            " LEFT JOIN RMA1369.PERSONA_NATURAL C ON C.COD_USR = E.COD_USR" +
                            " LEFT JOIN RMA1369.EXTRANJERO EXT ON EXT.COD_USR = E.COD_USR" +
                            " JOIN RMA1369.MOD_EMPR D ON D.TIP_MOD = E.TIP_MOD AND D.AREA_MOD = 2" +
                            " WHERE E.TIP_USR IN (2,5) AND FEC_VENC >= CURRENT_DATE AND E.NRO_CRN_VIG IS NOT NULL "+
                            " AND E.RUC = ?3 ";

            switch(tipoDoc){
                case "2":
                        jpql += " AND E.TIP_USR = 2 AND E.COD_USR = ?2 ";
                        break;
                case "5":
                        jpql += " AND E.TIP_USR = 5 AND TRIM(EXT.CRNT_EXT) = ?2 ";
                        break;
                case "1":
                        jpql += " AND E.NRO_CRN_VIG = ?2 ";
                        break;
            }
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(3, ruc);
            
            switch(tipoDoc){
                case "2":
                        q.setParameter(2, Long.parseLong(numDoc));
                        break;
                case "5":
                case "1":    
                        q.setParameter(2, numDoc);
                        break;
            }
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            return null;
        }
    }
    
    public Map buscarVigilanteCarteraCliByNroCarne(Long nroCarne, Long ruc) {
        try{
            String jpql = "SELECT DISTINCT E.TIP_USR, (CASE WHEN E.TIP_USR = 2 THEN ''||E.COD_USR ELSE EXT.CRNT_EXT END) AS DOC, "+
                            " (CASE WHEN E.TIP_USR = 2 THEN C.NOMBRE ELSE EXT.NOMBRE END) AS NOMBRES, " +
                            " (CASE WHEN E.TIP_USR = 2 THEN C.APE_PAT||' '||C.APE_MAT ELSE EXT.APE_PAT||' '||EXT.APE_MAT END) AS APELLIDOS, " +
                            " E.NRO_CRN_VIG, E.FEC_EMI, E.FEC_VENC, D.DES_MOD AS MODALIDAD " +
                            " FROM RMA1369.SS_EMP_VIG E" +
                            " JOIN RMA1369.EMPRESA B ON E.RUC = B.RUC" +
                            " LEFT JOIN RMA1369.PERSONA_NATURAL C ON C.COD_USR = E.COD_USR" +
                            " LEFT JOIN RMA1369.EXTRANJERO EXT ON EXT.COD_USR = E.COD_USR" +
                            " JOIN RMA1369.MOD_EMPR D ON D.TIP_MOD = E.TIP_MOD AND D.AREA_MOD = 2" +
                            " WHERE E.TIP_USR IN (2,5) AND TRUNC(FEC_VENC) >= TRUNC(CURRENT_DATE) AND E.NRO_CRN_VIG IS NOT NULL "+
                            " AND E.RUC = ?1  AND E.NRO_CRN_VIG = ?2  ";

            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, ruc);
            q.setParameter(2, nroCarne);
            
            if(!q.getResultList().isEmpty()){
                return (Map) q.getResultList().get(0);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public List<Map> buscarModalidadesPermitidasEmpresa(String ruc) {
        try{
            String jpql = "SELECT DISTINCT MODALIDAD "
                         + " FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC "
                         + " WHERE RUC = ?1 "
                         + " AND FEC_VEN >= TRUNC(CURRENT_DATE) ";

            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, ruc);
            
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
            return new ArrayList();
        }
    }
    
    public List<Map> buscarJefaturasPermitidasByEmpresa(String ruc) {
        try{
            String jpql = "SELECT DISTINCT MODALIDAD "
                         + " FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC "
                         + " WHERE RUC = ?1 "
                         + " AND FEC_VEN >= CURRENT_DATE ";

            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, ruc);
            
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public List<Map> buscarCefoesDpto(String ruc) {
        try{
            String jpql = "SELECT DISTINCT R.NRO_EXPEDIENTE "
                         + " FROM BDINTEGRADO.SSP_REGISTRO@RENAGI R "
                         + " INNER JOIN BDINTEGRADO.TIPO_BASE@RENAGI B ON B.ID=R.TIPO_PRO_ID "
                         + " INNER JOIN BDINTEGRADO.SB_PERSONA@RENAGI P ON P.ID=R.EMPRESA_ID "
                         + " INNER JOIN BDINTEGRADO.SSP_RESOLUCION@RENAGI RES ON RES.REGISTRO_ID = R.ID "
                         + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD@RENAGI E ON E.ID = R.ESTADO_ID "
                         + " WHERE P.RUC = ?1 AND B.COD_PROG IN('TP_GSSP_MOD_DEP', 'TP_GSSP_MOD_CEF') "
                         + " AND RES.FECHA_FIN >= CURRENT_DATE AND RES.ACTIVO=1  AND E.COD_PROG IN('TP_ECC_APR') ";

            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, ruc);
            
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
