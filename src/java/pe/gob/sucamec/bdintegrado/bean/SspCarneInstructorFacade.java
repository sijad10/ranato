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
//import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspCarneInstructor;


/**
 *
 * @author locador845.ogtic
 */
@Stateless
public class SspCarneInstructorFacade extends AbstractFacade<SspCarneInstructor>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SspCarneInstructorFacade() {
        super(SspCarneInstructor.class);
    }
    
    public SspCarneInstructor buscarCarneInstructorPersonaId(Long idPersona){
        
        String jpql= "SELECT ci FROM SspCarneInstructor ci "
                + " where ci.instructorId.id = :idper "
                + " and ci.activo = 1";
        //SELECT ci FROM SspCarneInstructor ci where ci.instructorId.id = 709435 and ci.activo = 1
        try{
            Query q =em.createQuery(jpql);
            q.setParameter("idper", idPersona);
            return (SspCarneInstructor) q.getResultList().get(0);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
       
    }
}
