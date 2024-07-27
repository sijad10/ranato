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
import pe.gob.sucamec.bdintegrado.data.SspRegistroRegDisca;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspRegistroRegDiscaFacade extends AbstractFacade<SspRegistroRegDisca> {
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspRegistroRegDiscaFacade() {
        super(SspRegistroRegDisca.class);
    }

    public SspRegistroRegDiscaFacade(EntityManager em, Class<SspRegistroRegDisca> entityClass) {
        super(entityClass);
        this.em = em;
    }
    
    

}
