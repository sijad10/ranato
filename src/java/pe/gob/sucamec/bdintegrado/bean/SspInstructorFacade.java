/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspInstructor;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspInstructorFacade extends AbstractFacade<SspInstructor> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspInstructorFacade() {
        super(SspInstructor.class);
    }
    
    public List<SspInstructor> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspInstructor s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public SspInstructor buscarInstructorByNumDocVigente(String numDoc) {
        try {
            Query q = em.createQuery("select s from SspInstructor s where s.activo = 1 and s.personaId.numDoc = :numDoc and FUNC('TRUNC', s.fechaVencimiento) >= current_date ");
            q.setParameter("numDoc", numDoc);

            if(!q.getResultList().isEmpty()){
               return (SspInstructor) q.getResultList().get(0); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public SspInstructor buscarInstructorByNumDoc(String numDoc) {
        try {
            Query q = em.createQuery("select s from SspInstructor s where s.activo = 1 and s.personaId.numDoc = :numDoc ");
            q.setParameter("numDoc", numDoc);

            if(!q.getResultList().isEmpty()){
               return (SspInstructor) q.getResultList().get(0); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
