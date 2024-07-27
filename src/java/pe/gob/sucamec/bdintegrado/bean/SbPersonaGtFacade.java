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
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbPersonaGtFacade extends AbstractFacade<SbPersonaGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbPersonaGtFacade() {
        super(SbPersonaGt.class);
    }
    
    /**
     * BUSCAR A PERSONA LOGUEADA EN SISTEMA SEL
     * @param tipoDoc
     * @param numDoc
     * @return 
     */
    public SbPersonaGt buscarPersonaSel(String tipoDoc, String numDoc){
        if (tipoDoc == null) {
            tipoDoc = "";
        }
        if (numDoc == null) {
            numDoc = "";
        }

        javax.persistence.Query q = em.createQuery(
                "select p from SbPersonaGt p where (p.ruc = :numDoc or p.numDoc = :numDoc) AND "
                + " p.activo = 1 order by p.id desc"
        );
        q.setParameter("numDoc", numDoc.toUpperCase() );
        
        if(q.getResultList().isEmpty()){
            return null;
        }else{
            return (SbPersonaGt) q.getResultList().get(0);
        }
    }   
    
    public List<SbPersonaGt> buscarPersonaSel(String numDoc){
        
        if (numDoc == null) {
            numDoc = "";
        }

        javax.persistence.Query q = em.createQuery(
                "select p from SbPersonaGt p where (p.ruc like :numDoc or p.numDoc like :numDoc) AND "
                + " p.activo = 1 order by p.id desc"
        );
        q.setParameter("numDoc", "%"+numDoc.toUpperCase()+"%");
        
        if(q.getResultList().isEmpty()){
            return null;
        }else{
            return q.getResultList();
        }
    }   
   
}   
