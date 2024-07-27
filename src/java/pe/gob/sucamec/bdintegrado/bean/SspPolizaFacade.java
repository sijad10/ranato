/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspPoliza;

/**
 *
 * @author mpalomino
 */
@Stateless
public class SspPolizaFacade extends AbstractFacade<SspPoliza>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SspPolizaFacade(){
        super(SspPoliza.class);
    }
    
    public SspPoliza buscarSspPolizaByRegistroId(Long registroId) {
        
        String jpql = "select s from SspPoliza s where s.activo = 1 and s.registroId.id = :registroId ";
        
        javax.persistence.Query q = em.createQuery(jpql);        
        q.setParameter("registroId", Long.parseLong(registroId+"") );
       
        if(!q.getResultList().isEmpty()){
            return (SspPoliza) q.getResultList().get(0);
        }
        return null;
    }
    
}
