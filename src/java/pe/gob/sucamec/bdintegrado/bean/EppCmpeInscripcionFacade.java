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
import pe.gob.sucamec.bdintegrado.data.EppCmpeInscripcion;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class EppCmpeInscripcionFacade extends AbstractFacade<EppCmpeInscripcion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppCmpeInscripcionFacade() {
        super(EppCmpeInscripcion.class);
    }
    public EppCmpeInscripcion selectLike(Long id) {
        Query q = em.createQuery("select e from EppCmpeInscripcion e where e.id = :id");
        q.setParameter("id", id);

        return (EppCmpeInscripcion) q.getResultList().get(0);
    }
}
