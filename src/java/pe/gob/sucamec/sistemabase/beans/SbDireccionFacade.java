/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author Renato
 */
@Stateless
public class SbDireccionFacade extends AbstractFacade<SbDireccion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbDireccionFacade() {
        super(SbDireccion.class);
    }

    public List<SbDireccion> listarDirecionPorPersona(SbPersona persona) {
        Query q = em.createQuery("SELECT D FROM SbDireccion D WHERE D.personaId.id = :parametro AND d.activo = 1 ORDER BY d.id desc");
        q.setParameter("parametro", persona.getId());
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return null;
    }

    public List<SbDireccion> listarDirecionPorPersonaS(String id) {
        Query q = em.createQuery("SELECT D FROM SbDireccion D WHERE trim(D.id) = :parametro AND d.activo = 1 ORDER BY d.id desc");
        q.setParameter("parametro", id);
        return q.getResultList();
    }

    public List<SbDireccion> listarDireccionesSucamec() {
        Query q = em.createQuery("select s from SbDireccion s where s.personaId.nomCom like :nomCom and s.activo = 1 and s.areaId is not null and s.referencia is not null");
        q.setParameter("nomCom", "SUCAMEC%");
        return q.getResultList();
    }

    public List<SbDireccion> listarDireccionesSucamec(Long areaId) {
        Query q = em.createQuery("select s from SbDireccion s where s.personaId.nomCom like :nomCom and s.activo = 1 "
                + "and s.areaId.id = :areaId "
                + "and s.areaId is not null ");
        q.setParameter("nomCom", "SUCAMEC%");
        q.setParameter("areaId", areaId);
        return q.getResultList();
    }

    public List<SbDireccion> listarDireccionesPermitidasXUsuario(Long perId, Long usuarioId, Long areaId) {
        String plsql ="select distinct(s) from SbDireccion s left join s.sbUsuarioList l "+
                    " where s.personaId.id = :perId and s.activo = 1  and s.tipoId.codProg != 'TP_DIRECB_SUC' ";
        if(areaId != null){
            plsql += " and ((s.areaId.id = :areaId OR l.id = :usuarioId ) OR s.referencia = 'POR REGULARIZAR'  ) ";
        }else{
            plsql += " and ( l.id = :usuarioId OR s.referencia = 'POR REGULARIZAR') ";
        }
        
        Query q = em.createQuery(plsql);
        q.setParameter("perId", perId);
        q.setParameter("areaId", areaId);
        q.setParameter("usuarioId", usuarioId);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbDireccion> listarDirecionPorDireccionOReferencia(String direccion) {
        String sql = "SELECT d FROM SbDireccion d WHERE d.activo = 1 "
                + " AND ( d.direccion like :direccion OR d.referencia like :direccion ) ORDER BY d.id desc";
        Query q = em.createQuery(sql);
        q.setParameter("direccion", "%" + direccion.trim() + "%");
        return q.getResultList();
    }

    public List<SbDireccion> listarDireccionesXPersona(Long perId) {
        Query q = em.createQuery("select s from SbDireccion s where s.personaId.id = :perId and s.activo = 1");
        q.setParameter("perId", perId);
        return q.getResultList();
    }
}
