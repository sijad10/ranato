/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SbAsientoPersona;
import pe.gob.sucamec.bdintegrado.data.SbAsientoVehiculo;

/**
 *
 * @author mpalomino
 */
@Stateless
public class SbAsientoVehiculoFacade extends AbstractFacade<SbAsientoVehiculo>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SbAsientoVehiculoFacade() {
        super(SbAsientoVehiculo.class);
    }
    
    public SbAsientoVehiculo buscarAsientoByVehiculoId(Long registroId) {
        String jpql = "select s from SbAsientoVehiculo s where s.activo = 1 and s.registroId.id = :registroId ";         
        javax.persistence.Query q = em.createQuery(jpql);
        q.setParameter("registroId", registroId );        
        //q.setParameter("empresaId", empresaId ); 
        if(!q.getResultList().isEmpty()){
            return (SbAsientoVehiculo) q.getResultList().get(0);
        }
        return null;
    }
    
}
