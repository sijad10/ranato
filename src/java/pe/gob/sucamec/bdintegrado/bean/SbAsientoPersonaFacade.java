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
import pe.gob.sucamec.bdintegrado.data.SbAsientoPersona;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SbAsientoPersonaFacade extends AbstractFacade<SbAsientoPersona>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbAsientoPersonaFacade() {
        super(SbAsientoPersona.class);
    }
    
    public SbAsientoPersona buscarAsientoPersonaByPersonaId(Long personaId, Long empresaId) {
        String jpql = "select s from SbAsientoPersona s where s.activo = 1 and s.personaId.id = :personaId and s.empresaId.id = :empresaId order by s.id desc";         
        Query q = em.createQuery(jpql);
        q.setParameter("personaId", personaId );        
        q.setParameter("empresaId", empresaId ); 
        if(!q.getResultList().isEmpty()){
            return (SbAsientoPersona) q.getResultList().get(0);
        }
        return null;
    }
}
