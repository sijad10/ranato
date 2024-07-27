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
public class CursosFacade {

    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CursosFacade() {
        super();
    }
    
    public List<Map> buscarInstructorByTipoDocByDoc(String tipoDoc, String numDoc) {
        try{
            String jpql = "SELECT " +
                            " I.TIP_USR, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.COD_USR ELSE P.COD_USR END) AS COD_USR, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN LPAD(E.CRNT_EXT,9,'0') ELSE LPAD(P.COD_USR,8,'0') END) AS NUM_DOC, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.APE_PAT || ' ' || E.APE_MAT ELSE P.APE_PAT || ' ' || P.APE_MAT END) AS APELLIDOS, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.NOMBRE ELSE P.NOMBRE END) AS NOMBRES, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.FEC_NAC ELSE P.FEC_NAC END) AS FECHA_NAC, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.SEXO ELSE P.SEXO END) AS GENERO, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.APE_PAT ELSE P.APE_PAT END) AS APE_PAT, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.APE_MAT ELSE P.APE_MAT END) AS APE_MAT, " +
                            " C.COD_CUR AS COD_CURSO, " +
                            " C.NOM_CUR AS NOM_CURSO, " +
                            " I.NRO_FICHA NRO_FICHA, " +
                            " I.fec_emi FECHA_EMISION, " +
                            " I.fec_ven FECHA_CADUCIDAD, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.CELULAR ELSE P.CELULAR END) AS CELULAR, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.EMAIL ELSE P.EMAIL END) AS EMAIL, " +
                            " I.RUC, " +
                            " EMP.RZN_SOC AS RZN_SOC_EMPRESA, "+
                            " EMP.TELF AS TELF_EMPRESA " +
                            " FROM RMA1369.SS_INSTRUCTOR@SUCAMEC I " +
                            " LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC P ON I.COD_USR = P.COD_USR " +
                            " LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC E ON I.COD_USR = E.COD_USR " +
                            " LEFT JOIN RMA1369.EMPRESA@SUCAMEC EMP ON EMP.RUC = I.RUC " +
                            " INNER JOIN RMA1369.SS_CURSO@SUCAMEC C ON I.COD_CUR = C.COD_CUR " +
                            " WHERE C.COD_CUR > 11 " +
                            " AND i.fec_ven >= TO_DATE ('03/08/2015', 'dd/MM/yyyy') " +
                            " AND i.fec_ven NOT IN (TO_DATE ('21/08/2015', 'dd/MM/yyyy')) "+
                            " AND i.fec_ven >= TRUNC(CURRENT_DATE) ";
            switch(tipoDoc){
                case "1":
                        jpql += " AND P.COD_USR = ?1 ";
                        break;
                case "2":
                        jpql += " AND E.CRNT_EXT = ?1 ";
                        break;
                case "3":
                        jpql += " AND I.NRO_FICHA = ?1 ";
                        break;
            }            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            switch(tipoDoc){
                case "1":                
                        q.setParameter(1, Long.parseLong(numDoc));
                        break;                
                case "2":
                case "3":
                        q.setParameter(1, numDoc);
                        break;
            }
            
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public List<Map> buscarInstructorByTipoDocByDocByCurso(String tipoDoc, String numDoc, String curso) {
        try{
            String jpql = "SELECT " +
                            " I.TIP_USR, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.COD_USR ELSE P.COD_USR END) AS COD_USR, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN LPAD(E.CRNT_EXT,9,'0') ELSE LPAD(P.COD_USR,8,'0') END) AS NUM_DOC, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.APE_PAT || ' ' || E.APE_MAT ELSE P.APE_PAT || ' ' || P.APE_MAT END) AS APELLIDOS, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.NOMBRE ELSE P.NOMBRE END) AS NOMBRES, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.FEC_NAC ELSE P.FEC_NAC END) AS FECHA_NAC, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.SEXO ELSE P.SEXO END) AS GENERO, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.APE_PAT ELSE P.APE_PAT END) AS APE_PAT, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.APE_MAT ELSE P.APE_MAT END) AS APE_MAT, " +
                            " C.COD_CUR AS COD_CURSO, " +
                            " C.NOM_CUR AS NOM_CURSO, " +
                            " I.NRO_FICHA NRO_FICHA, " +
                            " I.fec_emi FECHA_EMISION, " +
                            " I.fec_ven FECHA_CADUCIDAD, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.CELULAR ELSE P.CELULAR END) AS CELULAR, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.EMAIL ELSE P.EMAIL END) AS EMAIL, " +
                            " I.RUC, " +
                            " EMP.RZN_SOC AS RZN_SOC_EMPRESA, "+
                            " EMP.TELF AS TELF_EMPRESA " +
                            " FROM RMA1369.SS_INSTRUCTOR@SUCAMEC I " +
                            " LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC P ON I.COD_USR = P.COD_USR " +
                            " LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC E ON I.COD_USR = E.COD_USR " +
                            " LEFT JOIN RMA1369.EMPRESA@SUCAMEC EMP ON EMP.RUC = I.RUC " +
                            " INNER JOIN RMA1369.SS_CURSO@SUCAMEC C ON I.COD_CUR = C.COD_CUR " +
                            " WHERE C.COD_CUR > 11 " +
                            " AND i.fec_ven >= TO_DATE ('03/08/2015', 'dd/MM/yyyy') " +
                            " AND i.fec_ven NOT IN (TO_DATE ('21/08/2015', 'dd/MM/yyyy')) "+
                            " AND i.fec_ven >= TRUNC(CURRENT_DATE) " +
                            " AND C.NOM_CUR LIKE ?2 ";
            switch(tipoDoc){
                case "1":
                        jpql += " AND P.COD_USR = ?1 ";
                        break;
                case "2":
                        jpql += " AND E.CRNT_EXT = ?1 ";
                        break;
                case "3":
                        jpql += " AND I.NRO_FICHA = ?1 ";
                        break;
            }
            jpql += " ORDER BY I.fec_ven DESC";
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(2, "%"+curso+"%");
            switch(tipoDoc){
                case "1":                
                        q.setParameter(1, Long.parseLong(numDoc));
                        break;                
                case "2":
                case "3":
                        q.setParameter(1, numDoc);
                        break;
            }
            
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public int contarInstructorByDoc(String numDoc) {
        int cont = 0;
        try{
            String jpql = "SELECT COUNT(*) AS CONTADOR " +
                            " FROM RMA1369.WS_FICHA_ACRED_INSTRUCTOR_GSSP@SUCAMEC i" + 
                            " WHERE i.fechacaducidad >= CURRENT_DATE " +
                            " AND dni = ?1";

            Query q = em.createNativeQuery(jpql);
            q.setParameter(1, numDoc);
            
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
    
    public int contarInstructorConDiasLimiteVctoFichaByDoc(String numDoc, int diasLimite) {
        int cont = 0;
        try{
            String jpql = "SELECT COUNT(*) AS CONTADOR " +
                            " FROM RMA1369.WS_FICHA_ACRED_INSTRUCTOR_GSSP@SUCAMEC i" + 
                            " WHERE (i.fechacaducidad + "+diasLimite+") >= CURRENT_DATE " +
                            " AND dni = ?1";

            Query q = em.createNativeQuery(jpql);
            q.setParameter(1, numDoc);
            
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
    
    public List<Map> buscarInstructorByTipoDocByDocByCursoSinFechaLimiteCaducidad(String tipoDoc, String numDoc, String nomCurso) {
        try{
            String jpql = "SELECT " +
                            " I.TIP_USR, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.COD_USR ELSE P.COD_USR END) AS COD_USR, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN LPAD(E.CRNT_EXT,9,'0') ELSE LPAD(P.COD_USR,8,'0') END) AS NUM_DOC, " +                            
                            " (CASE WHEN I.TIP_USR = 5 THEN E.NOMBRE ELSE P.NOMBRE END) AS NOMBRES, " +                            
                            " (CASE WHEN I.TIP_USR = 5 THEN E.APE_PAT ELSE P.APE_PAT END) AS APE_PAT, " +
                            " (CASE WHEN I.TIP_USR = 5 THEN E.APE_MAT ELSE P.APE_MAT END) AS APE_MAT, " +
                            " C.COD_CUR AS COD_CURSO, " +
                            " C.NOM_CUR AS NOM_CURSO, " +
                            " I.NRO_FICHA NRO_FICHA, " +
                            " I.fec_emi FECHA_EMISION, " +
                            " I.fec_ven FECHA_CADUCIDAD, " +                            
                            " I.RUC " +                            
                            " FROM RMA1369.SS_INSTRUCTOR@SUCAMEC I " +
                            " LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC P ON I.COD_USR = P.COD_USR " +
                            " LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC E ON I.COD_USR = E.COD_USR " +
                            " LEFT JOIN RMA1369.EMPRESA@SUCAMEC EMP ON EMP.RUC = I.RUC " +
                            " INNER JOIN RMA1369.SS_CURSO@SUCAMEC C ON I.COD_CUR = C.COD_CUR " +
                            " WHERE C.COD_CUR > 11 " +
                            " AND i.fec_ven >= TO_DATE ('03/08/2015', 'dd/MM/yyyy') " +
                            " AND i.fec_ven NOT IN (TO_DATE ('21/08/2015', 'dd/MM/yyyy')) "+
                            " AND C.NOM_CUR = ?2  ";
            switch(tipoDoc){
                case "1":
                        jpql += " AND P.COD_USR = ?1 ";
                        break;
                case "2":
                        jpql += " AND E.CRNT_EXT = ?1 ";
                        break;
                case "3":
                        jpql += " AND I.NRO_FICHA = ?1 ";
                        break;
            }
            jpql += " ORDER BY I.fec_ven DESC";
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            switch(tipoDoc){
                case "1":                
                        q.setParameter(1, Long.parseLong(numDoc));
                        break;                
                case "2":
                case "3":
                        q.setParameter(1, numDoc);
                        break;
            }
            q.setParameter(2, nomCurso);
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
}
