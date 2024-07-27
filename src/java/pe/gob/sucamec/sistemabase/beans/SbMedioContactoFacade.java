/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.data.SbMedioContacto;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author Renato
 */
@Stateless
public class SbMedioContactoFacade extends AbstractFacade<SbMedioContacto> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbMedioContactoFacade() {
        super(SbMedioContacto.class);
    }
    public List<SbMedioContacto> listarMedioContactoPorPersona(SbPersona persona) {
        Query q = em.createQuery("SELECT M FROM SbMedioContacto M WHERE M.personaId.id= :parametro and M.activo=1");
        q.setParameter("parametro", persona.getId());
        return q.getResultList();
    }
    
    
}
