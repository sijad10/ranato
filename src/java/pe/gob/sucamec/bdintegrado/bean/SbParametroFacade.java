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
import pe.gob.sucamec.bdintegrado.data.SbParametro;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbParametroFacade extends AbstractFacade<SbParametro> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbParametroFacade() {
        super(SbParametro.class);
    }
    
    public SbParametro obtenerParametroXCodProg(String s) {
        Query q = em.createQuery("select t from SbParametro t where t.activo = 1 and t.codProg = :codProg and t.sistemaId.id = 2 ");
        q.setParameter("codProg", s != null ? s : "");
        
        if(!q.getResultList().isEmpty()){
            return (SbParametro) q.getResultList().get(0);
        }
        
        return null;
    }
    
        public SbParametro obtenerParametroXNombre(String s) {
            Query q = em.createQuery("select t from SbParametro t where t.activo = 1 and t.nombre = :codProg and t.sistemaId.id = 2 ");
        q.setParameter("codProg", s != null ? s : "");
        
        if(!q.getResultList().isEmpty()){
            return (SbParametro) q.getResultList().get(0);
        }
        
        return null;
    }
    
    public List<SbParametro> obtenerListadoParametroXNombre(String listado) {
        Query q = em.createQuery("select t from SbParametro t where t.activo = 1 and t.nombre in ("+listado+") and t.sistemaId.id = 2 ");
        return q.getResultList();
    }

    public SbParametro obtenerParametroXNombreGamac(String s) {
        Query q = em.createQuery("select t from SbParametro t where t.activo = 1 and t.nombre = :codProg and t.sistemaId.id = 5 ");
        q.setParameter("codProg", s != null ? s : "");
        
        if(!q.getResultList().isEmpty()){
            return (SbParametro) q.getResultList().get(0);
        }
        
        return null;
    }

    public SbParametro obtenerParametroXNombreIntegrado(String s) {
        Query q = em.createQuery("select t from SbParametro t where t.activo = 1 and t.nombre = :codProg and t.sistemaId.id = 1 ");
        q.setParameter("codProg", s != null ? s : "");
        
        if(!q.getResultList().isEmpty()){
            return (SbParametro) q.getResultList().get(0);
        }
        
        return null;
    }
    
    public SbParametro obtenerParametroXSistemaCodProg(String s, Long idSistema) {
        Query q = em.createQuery("select t from SbParametro t where t.activo = 1 and t.codProg = :codProg and t.sistemaId.id = :idSis ");
        q.setParameter("codProg", s != null ? s : "");
        q.setParameter("idSis", idSistema);
        try {
            if (!q.getResultList().isEmpty()) {
                return (SbParametro) q.getResultList().get(0);
            }            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
}
