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
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;
import pe.gob.sucamec.sistemabase.data.SbPerfil;

/**
 *
 * @author Renato
 */
@Stateless
public class SbPerfilFacade extends AbstractFacade<SbPerfil> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SbPerfil> perfilesNuevoSel() {
        javax.persistence.Query q = em.createQuery("select p from SbPerfil p where p.codProg in ('SEL_NOTIF','SEL_PJ')");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public SbPerfilFacade() {
        super(SbPerfil.class);
    }
    
    public SbPerfil perfilCodProg(String s) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPerfil p where p.codProg = :codProg");
        q.setParameter("codProg", s);
        q.setMaxResults(1);
        List l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return (SbPerfil) l.get(0);
    }

}
