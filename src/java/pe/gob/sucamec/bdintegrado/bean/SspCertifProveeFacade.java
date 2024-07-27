/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspCertifProvee;

/**
 *
 * @author lbartolo
 */
@Stateless
public class SspCertifProveeFacade extends AbstractFacade<SspCertifProvee>{
   
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspCertifProveeFacade() {
        super(SspCertifProvee.class);
    }
    
    public SspCertifProvee buscarSspCertifProveeByRegistroId(Long registroId) {
        
        String jpql = "select s from SspCertifProvee s where s.activo = 1 and s.registroId.id = :registroId ";
        
        javax.persistence.Query q = em.createQuery(jpql);        
        q.setParameter("registroId", Long.parseLong(registroId+"") );
       
        if(!q.getResultList().isEmpty()){
            return (SspCertifProvee) q.getResultList().get(0);
        }
        return null;
    }
}

