/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.turreg.data.TurProgramacion;
import pe.gob.sucamec.turreg.data.TurTurno;
import java.util.Date;

/**
 *
 * @author msalinas
 */
@Stateless
public class TurProgramacionFacade extends AbstractFacade<TurProgramacion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TurProgramacionFacade() {
        super(TurProgramacion.class);
    }

    public List<TurProgramacion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from TurProgramacion t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<TurProgramacion> lstTurProgramacion(Long sedeId) {
        Query query = getEntityManager().createQuery("select t from TurProgramacion t where t.tipoTurno.codProg = 'TP_PROG_REG' and (t.sedeId.id = :sedeId or :sedeId = null) and t.activo = 1 order by t.hora asc");
        query.setParameter("sedeId", sedeId);
        return query.getResultList();
    }
    
    public List<TurProgramacion> lstTurProgramacionXTipoTurno(Long sedeId, String tipoTurnoCP) {
        Query query = getEntityManager().createQuery("select t from TurProgramacion t where t.tipoTurno.codProg = :tipoTurnoCP and t.sedeId.id = :sedeId and t.activo = 1 order by t.hora asc");
        query.setParameter("sedeId", sedeId);
        query.setParameter("tipoTurnoCP", tipoTurnoCP);
        return query.getResultList();
    }

    public List<TurProgramacion> lstTurProgramacion(Long sedeId, String codTipo) {
        Query query = getEntityManager().createQuery("select t from TurProgramacion t where t.tipoTurno.codProg = :tipo and (t.sedeId.id = :sedeId or :sedeId = null) and t.activo = 1 order by t.hora asc");
        query.setParameter("sedeId", sedeId);
        query.setParameter("tipo", codTipo == null ? "" : codTipo.toUpperCase().trim());
        return query.getResultList();
    }

    public List<TurProgramacion> lstTurProgramacionDom() {
        Query query = getEntityManager().createQuery("select t from TurProgramacion t where t.tipoTurno.codProg = 'TP_PROG_VDOM' and t.activo = 1 order by t.hora asc");
        return query.getResultList();
    }
   
    public List<TurProgramacion> lstTurProgramacionFechaActual(Long sedeId, String codTipo, Date fechaHoy) {
        //Horarios de todas las Citas que esten confirmadas en una fecha especifica y por Tipo de Turno 
        String jpql = "select distinct t from TurProgramacion t, TurTurno tur ";
               jpql += "where tur.programacionId.id = t.id and tur.activo = 1 and t.tipoTurno.codProg = :tipo and t.sedeId.id = :sedeId ";
               jpql += "and FUNC('trunc', tur.fechaTurno) = FUNC('trunc',:fechaHoy) ";
               jpql += "and tur.estado.codProg = 'TP_EST_CON' order by t.hora asc";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("sedeId", sedeId);
        query.setParameter("tipo", codTipo == null ? "" : codTipo.toUpperCase().trim());
        query.setParameter("fechaHoy", fechaHoy);
        return query.getResultList();
    }
    
}
