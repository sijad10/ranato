/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author Renato
 */
@Stateless
public class SbDireccionFacadeGt extends AbstractFacade<SbDireccionGt> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbDireccionFacadeGt() {
        super(SbDireccionGt.class);
    }
    
    public List<SbDireccionGt> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbDireccionGt s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<SbDireccionGt> listarDirecionPorPersona(SbPersonaGt persona) {
        Query q = em.createQuery("SELECT D FROM SbDireccionGt D WHERE D.personaId.id = :parametro AND d.activo = 1 ORDER BY d.id desc");
        q.setParameter("parametro", persona.getId());
        return q.getResultList();
    }

    public List<SbDireccionGt> listarDirecionPorPersonaS(String id) {
        Query q = em.createQuery("SELECT D FROM SbDireccionGt D WHERE trim(D.id) = :parametro AND d.activo = 1 ORDER BY d.id desc");
        q.setParameter("parametro", id);
        return q.getResultList();
    }    
    
    public List<SbDireccionGt> listarDireccionesXPersona(Long perId) {
        Query q = em.createQuery("select s from SbDireccionGt s where s.personaId.id = :perId and s.activo = 1 order by s.id desc");
        q.setParameter("perId", perId);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<Map> listarDireccionesXPersonaMap(Long perId) {
        Query q = em.createQuery("select s.direccion, s.referencia, s.distritoId.nombre as distrito, s.distritoId.provinciaId.nombre as provincia, s.distritoId.provinciaId.departamentoId.nombre as depart, s.numero "
                + "from SbDireccionGt s where s.personaId.id = :perId and s.activo = 1");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("perId", perId);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<SbDireccionGt> listarDireccionesSucamec() {
        Query q = em.createQuery("select s from SbDireccionGt s where s.personaId.nomCom like :nomCom and s.activo = 1 and s.areaId is not null ");
        q.setParameter("nomCom", "SUCAMEC%");
        return q.getResultList();
    }
    
    public List<SbDireccionGt> listarDireccionesXPersonaByRuc(String ruc) {
        Query q = em.createQuery("select s from SbDireccionGt s where s.personaId.ruc = :ruc and s.activo = 1");
        q.setParameter("ruc", ruc);
        return q.getResultList();
    }
    
      public SbDireccionGt mostrarDireccionXRegionInpe(String codRegionInpe) {
        Query q = em.createQuery("select s from SbDireccionGt s join s.regionList r where r.codProg = :codRegionInpe ");
        q.setParameter("codRegionInpe", codRegionInpe);
        if(q.getResultList().isEmpty()){
            return null;
        }
        return (SbDireccionGt) q.getResultList().get(0);
    }
    
  
}
