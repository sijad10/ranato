/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.EppExplosivo;
import pe.gob.sucamec.bdintegrado.data.EppImpExpExplosivo;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppImpExpExplosivoFacade extends AbstractFacade<EppImpExpExplosivo> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppImpExpExplosivoFacade() {
        super(EppImpExpExplosivo.class);
    }
    
    /**
     * BUSQUEDA DE CANTIDAD DE DETERMINADO EXPLOSIVO DE UNA IMPORTACION/EXPORTACION
     * @param exp
     * @return 
     */
    public Double buscarCantidadExplosivo(EppExplosivo exp) {
        double cantidad = 0;
        
        javax.persistence.Query q = em.createNativeQuery("SELECT e.cantidad FROM EppImpExpExplosivo e where e.explosivoId.id = ?1 ");
        q.setParameter(1, exp.getId());        
        
        cantidad = Double.valueOf(q.getResultList().get(0).toString());        
        return cantidad;
    }
}
