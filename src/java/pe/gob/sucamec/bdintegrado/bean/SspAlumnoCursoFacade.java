/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspAlumnoCurso;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspAlumnoCursoFacade extends AbstractFacade<SspAlumnoCurso> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspAlumnoCursoFacade() {
        super(SspAlumnoCurso.class);
    }
    public List<SspAlumnoCurso> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspAlumnoCurso s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    public SspAlumnoCurso buscarAlumnoCurso(String numDoc) {
        try {
            Query q = em.createQuery("select s from SspAlumnoCurso s where s.personaId.numDoc = :numDoc and s.activo = 1 ");
            q.setParameter("numDoc", numDoc);        

            if(!q.getResultList().isEmpty()){
                return (SspAlumnoCurso) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }            
        return null;
    }
    
    public int contarRegistrosAprobadosXAlumno(String doc) {
        int cont = 0;
        String jpql = "SELECT COUNT(*) FROM BDINTEGRADO.SSP_ALUMNO_CURSO A "
                        + " INNER JOIN BDINTEGRADO.SB_PERSONA P ON P.ID = A.PERSONA_ID "
                        + " INNER JOIN BDINTEGRADO.SSP_REGISTRO_CURSO C ON C.ID = A.REGISTRO_CURSO_ID "
                        + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD E ON E.ID = C.ESTADO_ID "
                        + " INNER JOIN BDINTEGRADO.TIPO_BASE TOPE ON TOPE.ID = A.TIPO_OPE_ID "
                        + " WHERE A.ACTIVO = 1 AND C.ACTIVO = 1 AND P.NUM_DOC = ?1 "
                        + " AND E.COD_PROG = 'TP_ECC_FIN' AND A.ESTADO_FINAL = 1 "
                        + " AND TOPE.COD_PROG = 'TP_OPE_INI' "
                        + " AND A.FECHA_FIN IS NOT NULL AND ADD_MONTHS(A.FECHA_FIN,24) >= TRUNC(CURRENT_DATE) ";       // Se verifica 2 años despues de la fecha de vencimiento (+24 meses)
        
        Query q = em.createNativeQuery(jpql);
        q.setParameter(1, doc.trim());
        
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarRegistrosAprobadosXAlumnoXTipoCurso(String doc, String tipoCurso) {
        int cont = 0;
        String jpql = "SELECT COUNT(*) FROM BDINTEGRADO.SSP_ALUMNO_CURSO A "
                        + " INNER JOIN BDINTEGRADO.SB_PERSONA P ON P.ID = A.PERSONA_ID "
                        + " INNER JOIN BDINTEGRADO.SSP_REGISTRO_CURSO C ON C.ID = A.REGISTRO_CURSO_ID "
                        + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD E ON E.ID = C.ESTADO_ID "
                        + " INNER JOIN BDINTEGRADO.TIPO_BASE TC ON TC.ID = C.TIPO_CURSO "
                        + " INNER JOIN BDINTEGRADO.TIPO_BASE TOPE ON TOPE.ID = A.TIPO_OPE_ID "
                        + " WHERE A.ACTIVO = 1 AND C.ACTIVO = 1 AND P.NUM_DOC = ?1 "
                        + " AND E.COD_PROG = 'TP_ECC_FIN' AND A.ESTADO_FINAL = 1 "
                        + " AND TC.COD_PROG = ?2 "
                        + " AND TOPE.COD_PROG = 'TP_OPE_INI' "
                        + " AND A.FECHA_FIN IS NOT NULL AND ADD_MONTHS(A.FECHA_FIN,24) >= TRUNC(CURRENT_DATE) ";       // Se verifica 2 años despues de la fecha de vencimiento (+24 meses)
        
        Query q = em.createNativeQuery(jpql);
        q.setParameter(1, doc.trim());
        q.setParameter(2, tipoCurso.trim());
        
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarRegistrosCursoPerfeccionamientoXAlumno(String numDoc, String tipoFormacion) {
        int cont = 0;
        try {            
            String jpql = "SELECT COUNT(DISTINCT C.ID) FROM BDINTEGRADO.SSP_ALUMNO_CURSO A "
                        + " INNER JOIN BDINTEGRADO.SB_PERSONA P ON P.ID = A.PERSONA_ID "
                        + " INNER JOIN BDINTEGRADO.SSP_REGISTRO_CURSO C ON C.ID = A.REGISTRO_CURSO_ID "
                        + " INNER JOIN BDINTEGRADO.SSP_PROGRAMACION PR ON PR.REGISTRO_CURSO_ID = C.ID "
                        + " INNER JOIN BDINTEGRADO.SSP_MODULO M ON M.ID = PR.MODULO_ID "
                        + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD E ON E.ID = C.ESTADO_ID "
                        + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD TC ON TC.ID = M.TIPO_CURSO_ID "
                        + " WHERE A.ACTIVO = 1 AND C.ACTIVO = 1 AND P.NUM_DOC = ?1 "
                        + " AND E.COD_PROG = 'TP_ECC_FIN' AND A.ESTADO_FINAL = 1 "
                        + " AND TC.COD_PROG = 'TP_FORMC_PRF' "
                        + " AND A.FECHA_FIN IS NOT NULL AND ADD_MONTHS(A.FECHA_FIN,24) >= TRUNC(CURRENT_DATE) ";       // Se verifica 2 años despues de la fecha de vencimiento (+24 meses)
        
            Query q = em.createNativeQuery(jpql);
            q.setParameter(1, numDoc.trim());

            List<BigDecimal> results = q.getResultList();
            for (BigDecimal _values : results) {
                cont = _values.intValue();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }            
        return cont;
    }
}
