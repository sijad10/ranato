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
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author msalinas
 */
@Stateless
public class TipoBaseTVFacade extends AbstractFacade<TipoBase> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoBaseTVFacade() {
        super(TipoBase.class);
    }

    public List<TipoBase> findByCodProg(String s) {
        Query q = em.createNamedQuery("TipoBase.findByCodProg");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }

    public TipoBase buscaTipoBaseXCodProg(String codprog) {
        TipoBase tipo = null;
        String sentencia = "select t from TipoBase t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (TipoBase) query.getSingleResult();
        return tipo;
    }

    public TipoBase tipoBaseXCodProg(String codprog) {
        TipoBase tipo = null;
        String sentencia = "select t from TipoBase t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (TipoBase) query.getSingleResult();
        return tipo;
    }

    public List<TipoBase> lstTipoBase(String s) {
        Query query = getEntityManager().createQuery("select t from  TipoBase t where t.activo =1 and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        query.setParameter("codProg", s != null ? s : "");
        return query.getResultList();
    }

    public List<TipoBase> lstTipoBase(String s, int orden) {
        Query query = getEntityManager().createQuery("select t from  TipoBase t where t.activo =1 and t.orden = :orden  and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        query.setParameter("codProg", s != null ? s : "");
        query.setParameter("orden", orden);
        return query.getResultList();
    }

    public TipoBase tipoBaseXNombre(String nom) {
        TipoBase res = null;
        Query query = em.createQuery("select t from  TipoBase t where t.activo =1 and t.nombre = :nombre ");
        query.setParameter("nombre", nom != null ? nom : "");
        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            res = (TipoBase) query.getResultList().get(0);
        }
        return res;
    }

    public List<TipoBase> lstTiposDocumentosPersona() {
        Query query = getEntityManager().createQuery("select t from  TipoBase t where t.activo =1 and t.codProg in ('TP_DOCID_DNI','TP_DOCID_CE') order by t.orden, t.nombre");
        return query.getResultList();
    }

    public List<TipoBase> lstTiposXCodigosProg(String codigos) {
        Query query = getEntityManager().createQuery("select t from  TipoBase t where t.activo =1 and t.codProg in (" + codigos.trim() + ") order by t.orden, t.nombre");
        return query.getResultList();
    }

    public List<TipoBase> lstTelefonoContacto() {
        Query query = getEntityManager().createQuery("select t from  TipoBase t where t.activo =1 and t.codProg in ('TP_MEDCON_FIJ','TP_MEDCON_MOV') order by t.orden, t.nombre");
        return query.getResultList();
    }

    public List<TipoBase> lstEstadosSolicitud() {
        Query query = getEntityManager().createQuery("select t from  TipoBase t where t.activo =1 and t.tipoId.codProg in ('TP_EXVIEST') order by t.orden, t.nombre");
        return query.getResultList();
    }
}
