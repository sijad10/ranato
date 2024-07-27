/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbTupa;

/**
 *
 * @author msalinas
 */
@Stateless
public class SbTupaFacade extends AbstractFacade<SbTupa> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbTupaFacade() {
        super(SbTupa.class);
    }

    public List<SbTupa> obtenerTupaLike(String filtro) {
        Query q = em.createQuery("select t from SbTupa t where t.activo = 1 and t.tupaProcedimientoId.nombre like :filtro");
        q.setParameter("filtro", "%" + filtro + "%");

        if (!q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return new ArrayList();
    }

}
