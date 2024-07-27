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
import pe.gob.sucamec.sel.citas.data.CitaTurFotoReg;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaTurFotoRegFacade extends AbstractFacade<CitaTurFotoReg> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTurFotoRegFacade() {
        super(CitaTurFotoReg.class);
    }
    public List<CitaTurFotoReg> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from CitaTurFotoReg t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
}
