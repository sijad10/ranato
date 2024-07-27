/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.citas.data.CitaAmaCatalogo;
import pe.gob.sucamec.sistemabase.beans.AbstractFacade;

/**
 *
 * @author msalinas
 */
@Stateless
public class CitaAmaCatalogoFacade extends AbstractFacade<CitaAmaCatalogo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaAmaCatalogoFacade() {
        super(CitaAmaCatalogo.class);
    }

    public List<CitaAmaCatalogo> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from CitaAmaCatalogo a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public CitaAmaCatalogo buscarAmaCatalogoXId(Long id) {
        CitaAmaCatalogo respuesta = new CitaAmaCatalogo();
        String sentencia = "select t from AmaCatalogo t where t.id = :id";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("id", id);
        respuesta = (CitaAmaCatalogo) query.getSingleResult();
        return respuesta;
    }

    public List<CitaAmaCatalogo> listarTiposArma() {
        Query q = em.createQuery("select a from CitaAmaCatalogo a where a.tipoId.codProg = 'TP_ARM' and a.activo = 1 order by a.nombre ASC");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

//    public List<AmaCatalogo> selectCatalogoXPadre(String codProg) {
//        Query q = em.createQuery("select b from AmaCatalogo a inner join a.amaCatalogoList b where a.codProg = :codProg order by b.nombre ASC");
//        q.setParameter("codProg", codProg);
//        q.setMaxResults(MAX_RES);
//        q.setHint("eclipselink.batch.type", "IN");
//        return q.getResultList();
//    }
    public List<CitaAmaCatalogo> selectCatalogoXPadre(Long idPadre) {
        Query q = em.createQuery("select b from CitaAmaCatalogo a inner join a.amaCatalogoList b where a.id = :idPadre order by b.nombre ASC");
        q.setParameter("idPadre", idPadre);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<CitaAmaCatalogo> listarTipoArma() {
        javax.persistence.Query q = em.createQuery(
                "select t from CitaAmaCatalogo t where t.tipoId.codProg = 'TP_ARM'"
        );
        return q.getResultList();
    }

    public List<CitaAmaCatalogo> listarTipoArmaTeo() {
        javax.persistence.Query q = em.createQuery(
                "select t from CitaAmaCatalogo t where t.codProg IN ('TP_ARMPIS','TP_ARMREV','TP_ARMCAR','TP_ARMESC')"
        );
        return q.getResultList();
    }
    
    public List<CitaAmaCatalogo> listarTipoArmaTeoCorta() {
        javax.persistence.Query q = em.createQuery(
                "select t from CitaAmaCatalogo t where t.codProg IN ('TP_ARMPIS','TP_ARMREV')"
        );
        return q.getResultList();
    }
    
    public List<CitaAmaCatalogo> listarTipoArmaTeoLarga() {
        javax.persistence.Query q = em.createQuery(
                "select t from CitaAmaCatalogo t where t.codProg IN ('TP_ARMCAR','TP_ARMESC')"
        );
        return q.getResultList();
    }

    public List<CitaAmaCatalogo> listarTipoArmaTiro() {
        javax.persistence.Query q = em.createQuery(
                "select t from CitaAmaCatalogo t where t.codProg IN ('TP_ARMPIS','TP_ARMREV')"
        );
        return q.getResultList();
    }

}
