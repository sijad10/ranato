/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.HashMap;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.turreg.data.TurPersona;

/**
 *
 * @author msalinas
 */
@Stateless
public class TurPersonaFacade extends AbstractFacade<TurPersona> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TurPersonaFacade() {
        super(TurPersona.class);
    }

    public List<TurPersona> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from TurPersona t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<TurPersona> buscarPersonaXNroDoc(String nroDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT p FROM TurPersona p "
                + "where p.numDoc = :nroDoc "
                + "and p.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }
    
    public List<TurPersona> buscarPersonaxRuc(String nroDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT p FROM TurPersona p "
                + "where p.ruc = :nroDoc "
                + "and p.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }
        
    public List<TurPersona> buscarPersonaXNroDocxRuc(String nroDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT p FROM TurPersona p "
                + "where ( p.numDoc = :nroDoc OR p.ruc = :nroDoc) "
                + "and p.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }
    
    public List<TurPersona> buscarPersonaXCitaConfirmada(Long sedeId, String codTipo, Date fechaHoy, Long programacionId) { 
        //Personas de todas las Citas que esten confirmadas en una fecha especifica y por Tipo de Turno
        String jpql = "SELECT distinct p FROM TurPersona p, TurProgramacion t, TurTurno tur ";
               jpql += "where p.id = tur.perExamenId.id and tur.programacionId.id = t.id and tur.activo = 1 and tur.programacionId.id = :programacionId ";
               jpql += "and t.tipoTurno.codProg = :tipo and t.sedeId.id = :sedeId ";
               jpql += "and FUNC('trunc', tur.fechaTurno) = FUNC('trunc',:fechaHoy) ";
               jpql += "and tur.estado.codProg = 'TP_EST_CON' order by p.apePat, p.apeMat, p.nombres";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("sedeId", sedeId);
        query.setParameter("tipo", codTipo == null ? "" : codTipo.toUpperCase().trim());
        query.setParameter("fechaHoy", fechaHoy);
        query.setParameter("programacionId", programacionId);
        return query.getResultList();
    }
        
}

