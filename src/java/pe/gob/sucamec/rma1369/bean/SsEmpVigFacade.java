/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.rma1369.bean;

import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SsEmpVigFacade {

    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SsEmpVigFacade() {
        super();
    }
    
    public List<Map> buscarVigilanteCese(Long ruc, int tipoDoc, String numDoc) {
        try{
            String jpql = "SELECT DISTINCT E.TIP_USR AS TIPODOC, E.COD_USR AS NUMDOC, C.APE_PAT AS APAT, C.APE_MAT AS AMAT, C.NOMBRE AS NOMBRES, "+
                            " E.NRO_CRN_VIG, E.TIP_MOD, D.DES_MOD,  E.FEC_EMI, E.FEC_VENC, " +
                            " EXT.APE_PAT AS APAT_EXT, EXT.APE_MAT AS AMAT_EXT, EXT.NOMBRE AS NOMBRES_EXT, EXT.CRNT_EXT " +
                            " FROM RMA1369.SS_EMP_VIG E" +
                            " JOIN RMA1369.EMPRESA B ON E.RUC = B.RUC" +
                            " LEFT JOIN RMA1369.PERSONA_NATURAL C ON C.COD_USR = E.COD_USR" +
                            " LEFT JOIN RMA1369.EXTRANJERO EXT ON EXT.COD_USR = E.COD_USR" +
                            " JOIN RMA1369.MOD_EMPR D ON D.TIP_MOD = E.TIP_MOD AND D.AREA_MOD = 2" +
                            " WHERE E.RUC = ?1 AND E.TIP_USR = ?2 ";

            if(tipoDoc == 2){
                jpql = jpql + " AND E.COD_USR = ?3 ";
            }else{
                jpql = jpql + " AND TRIM(EXT.CRNT_EXT) = ?3 ";
            }
            
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, ruc);
            q.setParameter(2, tipoDoc);            
            if(tipoDoc == 2){
                q.setParameter(3, Long.parseLong(numDoc));
            }else{
                q.setParameter(3, numDoc);
            }

            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            return null;
        }
    }
    
     public List<Map> buscarVigilanteDetalleEntregado(Long ruc, String nroCarne) {
        try{
            String jpql = "SELECT DISTINCT E.TIP_USR AS TIPODOC, E.COD_USR AS NUMDOC, " +
                            " C.APE_PAT AS APAT, C.APE_MAT AS AMAT, C.NOMBRE AS NOMBRES, "+
                            " D.DES_MOD," +
                            " EXT.APE_PAT AS APAT_EXT, EXT.APE_MAT AS AMAT_EXT, EXT.NOMBRE AS NOMBRES_EXT, EXT.CRNT_EXT " +
                            " FROM RMA1369.SS_EMP_VIG_HIS E" +
                            " JOIN RMA1369.EMPRESA B ON E.RUC = B.RUC" +
                            " LEFT JOIN RMA1369.PERSONA_NATURAL C ON C.COD_USR = E.COD_USR" +
                            " LEFT JOIN RMA1369.EXTRANJERO EXT ON EXT.COD_USR = E.COD_USR" +
                            " JOIN RMA1369.MOD_EMPR D ON D.TIP_MOD = E.TIP_MOD AND D.AREA_MOD = 2" +
                            " WHERE E.RUC = ?1 AND E.NRO_CRN_VIG = ?2 AND E.TIP_USR IN (2,5)";

            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, ruc);
            q.setParameter(2, nroCarne);            

            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            return null;
        }
    }
    
    public boolean cesarVigilanteRMA(String usuario, Map cese) {
        try {     
            
            int result = 0;
            String mensaje = "";
            String exp[] = cese.get("numero_disca").toString().split("-");
            
            //INICIO: CESAR VIGILANTE
            StoredProcedureQuery sp = em.createStoredProcedureQuery("RMA1369.GSSP_CARNES.CESAR_VIGILANTE_SEL");
            sp.registerStoredProcedureParameter("RUC_CESE", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("DNI_CESE", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("TIPO_USR", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("MODALIDAD_CESE", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("NRO_EXP_CESE", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("ANO_EXP_CESE", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("USR_PRC_CESE", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("IP_CESE", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("NRO_CYDOC", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("RECIBO_ID", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("TIPO_CESE", String.class, ParameterMode.IN);            
            sp.registerStoredProcedureParameter("RESULT", Integer.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("MENSAJE", String.class, ParameterMode.OUT);
            
            sp.setParameter("RUC_CESE", ""+cese.get("ruc"));
            sp.setParameter("DNI_CESE", ""+cese.get("dni"));
            sp.setParameter("TIPO_USR", ""+cese.get("TIP_USR"));
            sp.setParameter("MODALIDAD_CESE", "" + cese.get("modalidad"));
            sp.setParameter("NRO_EXP_CESE", ""+exp[0]);
            sp.setParameter("ANO_EXP_CESE", ""+exp[1]);
            sp.setParameter("USR_PRC_CESE", usuario);
            sp.setParameter("IP_CESE", "");
            sp.setParameter("NRO_CYDOC", ""+cese.get("numero"));
            sp.setParameter("RECIBO_ID", "0");
            sp.setParameter("TIPO_CESE", "2");
            sp.setParameter("RESULT", result);
            sp.setParameter("MENSAJE", mensaje);
            
            boolean resultSet = sp.execute();
            result = (Integer) sp.getOutputParameterValue("RESULT");
            mensaje = (String) sp.getOutputParameterValue("MENSAJE");
            
            if(result < 0){
                System.err.println("Error: " + mensaje );
                return false;
            }
            return true;
            //FIN: CESAR VIGILANTE
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
}
