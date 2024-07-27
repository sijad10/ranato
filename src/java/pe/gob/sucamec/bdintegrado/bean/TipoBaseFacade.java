/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author mespinoza
 */
@Stateless
public class TipoBaseFacade extends AbstractFacade<TipoBaseGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoBaseFacade() {
        super(TipoBaseGt.class);
    }

    public List<TipoBaseGt> findByCodProg(String s) {
        Query q = em.createNamedQuery("TipoBaseGt.findByCodProg");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }
    
    public TipoBaseGt buscaTipoBaseXCodProg(String codprog) {
        TipoBaseGt tipo = null;
        String sentencia = "select t from TipoBaseGt t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (TipoBaseGt) query.getSingleResult();
        return tipo;
    }

    public TipoBaseGt tipoBaseXCodProg(String codprog) {
        TipoBaseGt tipo = null;
        String sentencia = "select t from TipoBaseGt t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (TipoBaseGt) query.getSingleResult();
        return tipo;
    }

    public List<TipoBaseGt> lstTipoBase(String s) {
        Query query = getEntityManager().createQuery("select t from  TipoBaseGt t where t.activo =1 and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        query.setParameter("codProg", s != null ? s : "");
        return query.getResultList();
    }

    public List<TipoBaseGt> lstTipoBase(String s, int orden) {
        Query query = getEntityManager().createQuery("select t from  TipoBaseGt t where t.activo =1 and t.orden = :orden  and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        query.setParameter("codProg", s != null ? s : "");
        query.setParameter("orden", orden);
        return query.getResultList();
    }

    public TipoBaseGt tipoBaseXNombre(String nom) {
        TipoBaseGt res = null;
        Query query = em.createQuery("select t from  TipoBaseGt t where t.activo =1 and t.nombre = :nombre ");
        query.setParameter("nombre", nom != null ? nom : "");
        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            res = (TipoBaseGt) query.getResultList().get(0);
        }
        return res;
    }

    public List<TipoBaseGt> lstTiposDocumentosPersona() {
        Query query = getEntityManager().createQuery("select t from  TipoBaseGt t where t.activo =1 and t.codProg in ('TP_DOCID_DNI','TP_DOCID_CE') order by t.orden, t.nombre");
        return query.getResultList();
    }

    public List<TipoBaseGt> lstTiposXCodigosProg(String codigos) {
        Query query = getEntityManager().createQuery("select t from  TipoBaseGt t where t.activo =1 and t.codProg in (" + codigos.trim() + ") order by t.orden, t.nombre");
        return query.getResultList();
    }
    
    public List<TipoBaseGt> lstTelefonoContacto() {
        Query query = getEntityManager().createQuery("select t from  TipoBaseGt t where t.activo =1 and t.codProg in ('TP_MEDCON_FIJ','TP_MEDCON_MOV') order by t.orden, t.nombre");
        return query.getResultList();
    }
    
    public List<TipoBaseGt> lstEstadosSolicitud() {
        Query query = getEntityManager().createQuery("select t from  TipoBaseGt t where t.activo =1 and t.tipoId.codProg in ('TP_EXVIEST') order by t.orden, t.nombre");
        return query.getResultList();
    }
    
    public TipoBase buscarTipoBaseXCodProg(String codProg) {
        TipoBase respuesta = null;
        String sentencia = "select t from TipoBaseGt t where t.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            respuesta = (TipoBase) query.getResultList().get(0);
        }
        return respuesta;
    }
    
    public List<TipoBaseGt> lstTipoBaseInactivo(String s) {
        Query query = getEntityManager().createQuery("select t from  TipoBaseGt t where t.activo=0 and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        query.setParameter("codProg", s != null ? s : "");
        return query.getResultList();
    }
        
    public TipoBaseGt buscarTipoBasePorCodProg(String codProg) {
        TipoBaseGt respuesta = null;
        String sentencia = "select t from TipoBaseGt t where t.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            respuesta = (TipoBaseGt) query.getResultList().get(0);
        }
        return respuesta;
    }

    public TipoBaseGt tipoPorCodProg(String s) {
        List<TipoBaseGt> r = findByCodProg(s);
        return r.size() == 1 ? r.get(0) : null;
    }    
    
}
