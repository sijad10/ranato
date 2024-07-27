/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import pe.gob.sucamec.bdintegrado.data.SiLogNumAud;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Renato
 */
@Stateless
public class LogNumAudFacade extends AbstractFacade<SiLogNumAud> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LogNumAudFacade() {
        super(SiLogNumAud.class);
    }

    public List<SiLogNumAud> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = getEntityManager().createQuery(
                "select l from SbLogNumAud l where l.tabla like :tabla or l.accion like :accion");
        q.setParameter("tabla", "%" + s + "%");
        q.setParameter("accion", "%" + s + "%");
        return q.getResultList();
    }
}
