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
import pe.gob.sucamec.sistemabase.data.SbPais;

/**
 *
 * @author Renato
 */
@Stateless
public class SbPaisFacade extends AbstractFacade<SbPais> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbPaisFacade() {
        super(SbPais.class);
    }

    public List<SbPais> selectPaisesActivos() {
        Query q = em.createQuery("select p from SbPais p where p.activo = 1 order by p.nombre asc");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return null;
    }
}
