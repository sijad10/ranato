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
import pe.gob.sucamec.bdintegrado.data.SspModulo;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspModuloFacade extends AbstractFacade<SspModulo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspModuloFacade() {
        super(SspModulo.class);
    }
    public List<SspModulo> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspModulo s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SspModulo> listarModuloByTipoCurso(Long tipoCurso) {
        Query q = em.createQuery("select s from SspModulo s where s.activo = 1 and s.tipoCursoId.id = :tipoCurso ");
        q.setParameter("tipoCurso", tipoCurso );
        return q.getResultList();
    }
    
    public List<SspModulo> listarModuloByTipoCursoSinRefrigerio(Long tipoCurso) {
        Query q = em.createQuery("select s from SspModulo s where s.activo = 1 and s.tipoCursoId.id = :tipoCurso and s.codModulo not in ('REFB','CMGRP','CMGRB') order by s.id asc ");
        q.setParameter("tipoCurso", tipoCurso );
        return q.getResultList();
    }
    
    public SspModulo obtenerModuloByCodMoulo(String codModulo) {
        Query q = em.createQuery("select s from SspModulo s where s.activo = 1 and s.codModulo = :codModulo ");
        q.setParameter("codModulo", codModulo );
        if(!q.getResultList().isEmpty()){
            return (SspModulo) q.getResultList().get(0);
        }
        return null;
    }
}