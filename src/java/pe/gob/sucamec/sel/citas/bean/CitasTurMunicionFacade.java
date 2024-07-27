/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.citas.data.TurMunicion;

/**
 *
 * @author ocastillo
 */
@Stateless
public class CitasTurMunicionFacade extends AbstractFacade<TurMunicion> {
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitasTurMunicionFacade() {
        super(TurMunicion.class);
    }
    
    public List<TurMunicion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from TurMunicion t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
        
    public List<TurMunicion> listarCitasEmpadronamientoPorTurno(Long turnoId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurMunicion t "
                + "where t.activo = 1 "
                + "and t.turnoId.id = :turnoId "
                + "order by t.id"
        );
        q.setParameter("turnoId", turnoId);
        return q.getResultList();
    }

}
