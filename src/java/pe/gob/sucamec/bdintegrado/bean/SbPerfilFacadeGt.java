/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SbPerfilGt;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**I
 *
 * @author Renato
 */
@Stateless
public class SbPerfilFacadeGt extends AbstractFacade<SbPerfilGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SbPerfilGt> perfilesNuevoSel() {
        javax.persistence.Query q = em.createQuery("select p from SbPerfilGt p where p.codProg in ('SEL_NOTIF')");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public SbPerfilFacadeGt() {
        super(SbPerfilGt.class);
    }
    
    public SbPerfilGt perfilCodProg(String s) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPerfilGt p where p.codProg = :codProg");
        q.setParameter("codProg", s);
        q.setMaxResults(1);
        List l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return (SbPerfilGt) l.get(0);
    }

}
