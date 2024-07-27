/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.SbFeriado;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbFeriadoFacade extends AbstractFacade<SbFeriado> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbFeriadoFacade() {
        super(SbFeriado.class);
    }

    public int agregarDiasXFeriado(Date fechaIni, Date fechaFin) {
        int cant = 0;
        String jpql = "select count(f.id) from BDINTEGRADO.SB_FERIADO f where trunc(f.fecha_feriado) between trunc(?1) and trunc(?2)";
        Query query = getEntityManager().createNativeQuery(jpql);
        query.setParameter(1, fechaIni);
        query.setParameter(2, fechaFin);
        cant = ((BigDecimal) query.getSingleResult()).intValue();
        return cant;
    }

    public int feriados(Date fecIni, Date fecFin) {
        int cant = 0;
        Query q = em.createQuery("SELECT count (p) FROM SbFeriado p where p.fechaFeriado BETWEEN FUNC('trunc',:ffini) and FUNC('trunc',:ffin) and p.activo = 1");
        q.setParameter("ffini", fecIni, TemporalType.DATE);
        q.setParameter("ffin", fecFin, TemporalType.DATE);
        cant = ((Long) q.getSingleResult()).intValue();
        return cant;
    }
    
    public int feriadosCalendario(Date fecIni, Date fecFin) {
        int cant = 0;
        Query q = em.createQuery("SELECT count (p) FROM SbFeriado p where p.fechaFeriado BETWEEN FUNC('trunc',:ffini) and FUNC('trunc',:ffin) and p.activo = 1 and p.tipoFeriadoId.codProg = 'TP_FER_CAL' ");
        q.setParameter("ffini", fecIni, TemporalType.DATE);
        q.setParameter("ffin", fecFin, TemporalType.DATE);
        cant = ((Long) q.getSingleResult()).intValue();
        return cant;
    }
    
    public int feriadosCalendarioSinSabado(Date fecIni, Date fecFin) {
        try {
            int cant = 0;
            Query q = em.createNativeQuery("SELECT COUNT(*) FROM BDINTEGRADO.SB_FERIADO F \n" +
                                            " INNER JOIN BDINTEGRADO.TIPO_BASE T ON T.ID = F.TIPO_FERIADO_ID \n" +
                                            " WHERE F.FECHA_FERIADO BETWEEN TRUNC(?1) AND TRUNC(?2) AND F.ACTIVO = 1 AND T.COD_PROG = 'TP_FER_CAL' "+
                                            "  AND TO_NUMBER(TO_CHAR( FECHA_FERIADO , 'd')) != 7 ");
            q.setParameter(1, fecIni, TemporalType.DATE);
            q.setParameter(2, fecFin, TemporalType.DATE);
            cant = ((BigDecimal) q.getSingleResult()).intValue();
            return cant;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public List<Date> listFeriados(Date fecIni, Date fecFin) {
        Query q = em.createQuery("SELECT p.fechaFeriado FROM SbFeriado p where FUNC('trunc',p.fechaFeriado) between FUNC('trunc',:ffini) and FUNC('trunc',:ffin) and p.activo = 1 ");
        q.setParameter("ffini", fecIni, TemporalType.DATE);
        q.setParameter("ffin", fecFin, TemporalType.DATE);
        return q.getResultList();
    }
    
    public List<Date> listFeriadosCalendario(Date fecIni, Date fecFin) {
        Query q = em.createQuery("SELECT p.fechaFeriado FROM SbFeriado p where FUNC('trunc',p.fechaFeriado) between FUNC('trunc',:ffini) and FUNC('trunc',:ffin) and p.activo = 1 and p.tipoFeriadoId.codProg = 'TP_FER_CAL' ");
        q.setParameter("ffini", fecIni, TemporalType.DATE);
        q.setParameter("ffin", fecFin, TemporalType.DATE);
        return q.getResultList();
    }

    public int diasNoLaborables(Date fecIni, Date fecFin) {
        try {
            int cant = 0;
            Query q = em.createNativeQuery("SELECT BDINTEGRADO.DIAS_LABORABLES(?1,?2) from dual");
            q.setParameter(1, fecIni, TemporalType.DATE);
            q.setParameter(2, fecFin, TemporalType.DATE);
            cant = ((BigDecimal) q.getSingleResult()).intValue();
            return cant;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int diasLaborables(Date fecIni, Date fecFin) {
        try {
            int cant = 0;
            Query q = em.createNativeQuery("SELECT BDINTEGRADO.DIAS_LABORABLES_RANGO_FECHAS(?1,?2) from dual");
            q.setParameter(1, fecIni, TemporalType.DATE);
            q.setParameter(2, fecFin, TemporalType.DATE);
            cant = ((BigDecimal) q.getSingleResult()).intValue();
            return cant;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public List<SbFeriado> listaFeriados() {
        List<SbFeriado> respuesta = new ArrayList();
        String sentencia = "select t from SbFeriado t where t.activo =1";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<SbFeriado> listaFeriadosCalendario() {
        List<SbFeriado> respuesta = new ArrayList();
        String sentencia = "select t from SbFeriado t where t.activo = 1 and t.tipoFeriadoId.codProg = 'TP_FER_CAL' ";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
}
