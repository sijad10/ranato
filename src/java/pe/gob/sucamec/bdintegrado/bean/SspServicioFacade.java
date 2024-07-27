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
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SspServicio;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspServicioFacade extends AbstractFacade<SspServicio> {
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SspServicioFacade() {
        super(SspServicio.class);
    }
    
    public SspServicio buscarServicioByRegistroId(SspRegistro registroId) {
        String jpql = "select s "                      
                      + " from SspServicio s "
                      + " where s.registroId = :registroId ";        
        
        Query q = em.createQuery(jpql);
        q.setParameter("registroId", registroId );        
        if(!q.getResultList().isEmpty()){
            return (SspServicio) q.getResultList().get(0);
        }
        return null;
    }
}
