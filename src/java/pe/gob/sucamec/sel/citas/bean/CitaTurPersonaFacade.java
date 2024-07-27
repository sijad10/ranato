/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.citas.data.CitaTurPersona;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaTurPersonaFacade extends AbstractFacade<CitaTurPersona> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTurPersonaFacade() {
        super(CitaTurPersona.class);
    }

    public List<CitaTurPersona> buscarPersonaXNroDoc(String nroDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT p FROM CitaTurPersona p "
                + "where p.numDoc = :nroDoc "
                + "and p.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }

    public List<CitaTurPersona> buscarPersonaXNroRuc(String nroRuc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT p FROM CitaTurPersona p "
                + "where p.ruc = :nroRuc "
                + "and p.activo = 1"
        );
        q.setParameter("nroRuc", nroRuc);
        return q.getResultList();
    }

    public List<CitaTurPersona> selectEmpresas() {
        javax.persistence.Query q = em.createQuery(
                "SELECT p FROM CitaTurPersona p where p.rznSocial is not null and  p.activo = 1 "
        );
        return q.getResultList();
    }

}
