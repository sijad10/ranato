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
import pe.gob.sucamec.bdintegrado.data.SspLocal;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspLocalFacade extends AbstractFacade<SspLocal> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspLocalFacade() {
        super(SspLocal.class);
    }
    
    public SspLocal buscarLocalById(Long localId) {
        Query q = em.createQuery("select s from SspLocal s where s.activo = 1 and s.id = :localId ");
        q.setParameter("localId", localId );
        if(!q.getResultList().isEmpty()){
            return (SspLocal) q.getResultList().get(0);
        }
        return null;
    }
    
    public List<SspLocal> listarTodosLocales() {
        Query q = em.createQuery("select s from SspLocal s where s.activo = 1 ");
        return q.getResultList();
    }
    
    public List<SspLocal> buscarLocalByNombreDireccionByUbigeo(String direccion, Long ubigeoId, Long localId) {
        String jpql = "select s from SspLocal s where s.activo = 1 ";
        if(direccion != null && !direccion.isEmpty()){
            jpql += " and (s.direccion like :direccion or s.nombreLocal like :direccion ) ";
        }
        if(ubigeoId != null){
            jpql += " and s.distritoId.id = :ubigeoId ";
        }
        if(localId != null){
            jpql += " and s.id != :localId ";
        }
        Query q = em.createQuery(jpql);
        if(direccion != null && !direccion.isEmpty()){
            q.setParameter("direccion", "%" + direccion + "%");
        }
        if(ubigeoId != null){
            q.setParameter("ubigeoId", ubigeoId);
        }
        if(localId != null){
            q.setParameter("localId", localId);
        }
        return q.getResultList();
    }
    
    public int contarLocalesByNombre(String nombre, Long localId) {
        int cont = 0;
        String jpql = "select count(s) from SspLocal s where s.activo = 1 "+
                                  " and s.nombreLocal = :nombre ";
        if(localId != null){
            jpql += " and s.id != :localId ";
        }
        Query q = em.createQuery(jpql);
        q.setParameter("nombre", nombre);
        if(localId != null){
            q.setParameter("localId", localId);
        }
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarLocalesByDireccion(String nombre, Long localId) {
        int cont = 0;
        String jpql = "select count(s) from SspLocal s where s.activo = 1 "+
                                  " and s.direccion = :nombre ";
        if(localId != null){
            jpql += " and s.id != :localId ";
        }
        Query q = em.createQuery(jpql);
        q.setParameter("nombre", nombre);
        if(localId != null){
            q.setParameter("localId", localId);
        }
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
}
