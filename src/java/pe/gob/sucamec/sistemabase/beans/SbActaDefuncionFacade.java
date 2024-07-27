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
import pe.gob.sucamec.sistemabase.data.SbActaDefuncion;

/**
 *
 * @author rarevalo
 */
@Stateless
public class SbActaDefuncionFacade extends AbstractFacade<SbActaDefuncion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbActaDefuncionFacade() {
        super(SbActaDefuncion.class);
    }

    public List<SbActaDefuncion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from SbActaDefuncion a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        return q.getResultList();
    }    

    public List<SbActaDefuncion> listaxPersona(Long personaId) {
        Query q = em.createQuery("select a from SbActaDefuncion a where a.personaId.id=:personaId and a.activo=1");
        q.setParameter("personaId", personaId);
        return q.getResultList();
    }    

}
