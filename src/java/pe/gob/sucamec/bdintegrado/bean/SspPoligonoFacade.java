/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspPoligono;

/**
 *
 * @author jrosales
 */
@Stateless
public class SspPoligonoFacade extends AbstractFacade<SspPoligono> {
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspPoligonoFacade() {
        super(SspPoligono.class);
    }

    public SspPoligonoFacade(EntityManager em, Class<SspPoligono> entityClass) {
        super(entityClass);
        this.em = em;
    }
    
    
    
    
    
}
