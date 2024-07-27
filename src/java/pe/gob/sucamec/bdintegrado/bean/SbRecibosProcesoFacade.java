/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SbRecibosProceso;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbRecibosProcesoFacade extends AbstractFacade<SbRecibosProceso> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbRecibosProcesoFacade() {
        super(SbRecibosProceso.class);
    }
    
    public boolean validaArchivo(String nombreArchivo) {
          
        javax.persistence.Query q = em.createQuery("select r from SbRecibosProceso r where r.rutaArchivo = :criterio and r.codProceso = 5");
        q.setParameter("criterio", nombreArchivo);
       
        return q.getResultList().isEmpty();
    }
    
}
