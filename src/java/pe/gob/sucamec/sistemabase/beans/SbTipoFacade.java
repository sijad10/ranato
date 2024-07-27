/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.data.SbTipo;


/**
 *
 * @author Renato
 */
@Stateless
public class SbTipoFacade extends AbstractFacade<SbTipo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbTipoFacade() {
        super(SbTipo.class);
    }

    public SbTipo tipoPorCodProg(String s) {
        Query q = em.createNamedQuery("SbTipo.findByCodProg");
        q.setParameter("codProg", s != null ? s : "");
        List<SbTipo> r = q.getResultList();
        return r.size() == 1 ? r.get(0) : null;
    }

    public SbTipo buscarTipoBaseXCodProg(String codProg) {
        SbTipo respuesta = new SbTipo();
        String sentencia = "select t from SbTipo t where t.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        respuesta = (SbTipo) query.getSingleResult();
        return respuesta;
    }
    
    public List<SbTipo> selectItemsTipo(String s) {
        Query q = em.createQuery("select t from SbTipo t where t.activo =true and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }

    public List<SbTipo> selectItemsSbTipo() {
        Query q = em.createQuery("select t from SbTipo t where t.tipoId = null");
        return q.getResultList();
    }

    public SbTipo selectAreaPorSiglas(String s) {
        Query q = em.createQuery("select t from SbTipo t where t.tipoId.codProg='TP_AREA' and t.abreviatura = :abreviatura");
        q.setParameter("abreviatura", s != null ? s : "");
        List<SbTipo> tb = q.getResultList();
        if (tb.isEmpty()) {
            return null;
        }
        return tb.get(0);
    }

    public List<SbTipo> selectLike(String s, String tipo) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select t from SbTipo t left join t.tipoId tp where  (trim(tp.nombre) like :tipo "
                + (tipo == null ? " or tp is null " : "") + ") and ( t.nombre like :nombre or t.abreviatura "
                + "like :abreviatura or t.codProg like :codProg )");
        q.setParameter("nombre", "%" + s + "%");
        q.setParameter("abreviatura", "%" + s + "%");
        q.setParameter("codProg", "%" + s + "%");
        q.setParameter("tipo", tipo == null ? "%" : tipo);
        q.setMaxResults(MAX_RES);
        return q.getResultList();

    }

    public List<SbTipo> findByCodProg(String s) {
        Query q = em.createNamedQuery("SbTipo.findByCodProg");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }
    
    public SbTipo tipoBaseXCodProg(String codprog) {
        SbTipo tipo = null;
        String sentencia = "select t from SbTipo t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (SbTipo) query.getSingleResult();
        return tipo;
    }
    
    public List<SbTipo> listaTipos(String codProg) {
        //No filtrar por ACTIVO!
        String sentencia = "select t from SbTipo t where t.tipoId.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            return query.getResultList();
        }
        return null;
    }
}
