/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.renagi.data.ConsultaTipoBase;

/**
 *
 * @author msalinas
 */
@Stateless
public class ConsultaTipoBaseFacade extends AbstractFacade<ConsultaTipoBase> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaTipoBaseFacade() {
        super(ConsultaTipoBase.class);
    }

    public List<ConsultaTipoBase> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from ConsultaTipoBase t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public ConsultaTipoBase buscarTipoBaseXCodProg(String codProg) {
        ConsultaTipoBase respuesta = new ConsultaTipoBase();
        String sentencia = "select t from ConsultaTipoBase t where t.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        respuesta = (ConsultaTipoBase) query.getSingleResult();
        return respuesta;
    }

    public List<ConsultaTipoBase> listarTipoBaseXCodProg(String codProg) {
        List<ConsultaTipoBase> respuesta = new ArrayList();
        String sentencia = "select t from ConsultaTipoBase t where t.tipoId.codProg = :codProg and t.activo = 1";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        respuesta = query.getResultList();
        return respuesta;
    }

    public ConsultaTipoBase tipoPorCodProg(String s) {
        List<ConsultaTipoBase> r = findByCodProg(s);
        return r.size() == 1 ? r.get(0) : null;
    }

    public List<ConsultaTipoBase> findByCodProg(String s) {
        Query q = em.createNamedQuery("ConsultaTipoBase.findByCodProg");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }

    public List<ConsultaTipoBase> lstTipoBase(String s) {
        Query query = getEntityManager().createQuery("select t from  TipoBase t where t.activo =1 and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        query.setParameter("codProg", s != null ? s : "");
        return query.getResultList();
    }

    public List<ConsultaTipoBase> lstTipoDoc() {
        List<ConsultaTipoBase> respuesta = new ArrayList();
        String sentencia = "select t from ConsultaTipoBase t "
                + "where t.activo = 1 "
                + "and (t.codProg = 'TP_DOCID_DNI' "
                + "or t.codProg = 'TP_DOCID_CE' "
                + "or t.codProg = 'TP_DOCID_RUC' "
                + "or t.codProg = 'TP_DOCID_PAS') "
                + "order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public ConsultaTipoBase buscarTipoBaseXId(Long id) {
        ConsultaTipoBase respuesta = new ConsultaTipoBase();
        String sentencia = "select t from ConsultaTipoBase t where t.id = :id";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("id", id);
        respuesta = (ConsultaTipoBase) query.getSingleResult();
        return respuesta;
    }
    
    public List<ConsultaTipoBase> lstTipoDocCita() {
        List<ConsultaTipoBase> respuesta = new ArrayList();
        String sentencia = "select t from ConsultaTipoBase t "
                + "where t.activo = 1 "
                + "and (t.codProg = 'TP_DOCID_DNI' "
                + "or t.codProg = 'TP_DOCID_CE') "
                + "order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
}
