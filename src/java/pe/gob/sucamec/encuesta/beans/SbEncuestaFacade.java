/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.encuesta.beans;

import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.bean.AbstractFacade;
import pe.gob.sucamec.encuesta.data.SbEncuesta;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author gchavez
 */
@Stateless
public class SbEncuestaFacade extends AbstractFacade<SbEncuesta> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbEncuestaFacade() {
        super(SbEncuesta.class);
    }
    public List<SbEncuesta> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbEncuesta s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
//    public List<SbEncuesta> selectVigentes(Date fechaHoy) {
//        Query q = em.createQuery("select e from SbEncuesta e where e.activo = 1 and FUNC('trunc',:hoy) between FUNC('trunc', e.fechaIni) and FUNC('trunc', e.fechaFin) ");
//        q.setParameter("hoy", fechaHoy);
//        q.setMaxResults(MAX_RES);
//        q.setHint("eclipselink.batch.type", "IN");
//        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
//            return q.getResultList();
//        }
//        return null;
//    }
    
    /**
     * FUNCION QUE OBTIENE TODAS LAS ENCUESTAS ACTIVAS, LAS CUALES SE ENCUENTREN
     * DENTRO DE LA FECHA Y HORA ACTUAL
     *
     * @param sistemaId
     * @return
     */
    public List<SbEncuesta> selectPendientes(Long sistemaId) {
        Query q = em.createQuery("select s from SbEncuesta s where s.activo = 1 and s.sistemaId.id = :idSis and (func('trunc',current_date) between func('trunc',s.fechaIni) and func('trunc',s.fechaFin)) ");
        q.setParameter("idSis", sistemaId);
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
}
