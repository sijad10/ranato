/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.data.Proceso;

/**
 *
 * @author msalinas
 */
@Stateless
public class ProcesoFacade extends AbstractFacade<Proceso> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcesoFacade() {
        super(Proceso.class);
    }

    public List<Proceso> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select p from Proceso p where trim(p.id) like :id");
        q.setParameter("id", "%" + s + "%");
        return q.getResultList();
    }

    public List<Proceso> listarProcesos() {
        Query q = em.createQuery("select p from Proceso p where p.idProceso in (635,636,637,638,642,644,646,650,921,1571,651,922,1572)");
        return q.getResultList();
    }

    /**
     * @param numExp
     * @return
     */
    public Proceso obtenerProcesoPorExpediente(String numExp) {
        Proceso res = new Proceso();
        try {
            Query q = em.createQuery("select ex.idProceso from Expediente ex inner join Proceso pro on (pro.idProceso=ex.idProceso.idProceso) where ex.numero = :numExp");
            q.setParameter("numExp", numExp);

            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                res = (Proceso) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
