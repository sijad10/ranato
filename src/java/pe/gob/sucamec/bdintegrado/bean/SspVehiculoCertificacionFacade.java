/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SspVehiculoCertificacion;

/**
 *
 * @author lbartolo
 */
@Stateless
public class SspVehiculoCertificacionFacade extends AbstractFacade<SspVehiculoCertificacion>{
   
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspVehiculoCertificacionFacade() {
        super(SspVehiculoCertificacion.class);
    }
    
    
    public SspVehiculoCertificacion buscarSspVehiculoCertificacionByRegistroId(Long registroId) {
        
        String jpql = "select s from SspVehiculoCertificacion s where s.activo = 1 and s.registroId.id = :registroId ";
        
        Query q = em.createQuery(jpql);        
        q.setParameter("registroId", Long.parseLong(registroId+"") );
       
        if(!q.getResultList().isEmpty()){
            return (SspVehiculoCertificacion) q.getResultList().get(0);
        }
        return null;
    }
}
