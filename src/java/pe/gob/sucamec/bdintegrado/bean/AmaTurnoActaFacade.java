/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.turreg.data.AmaTurnoActa;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaTurnoActaFacade extends AbstractFacade<AmaTurnoActa> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaTurnoActaFacade() {
        super(AmaTurnoActa.class);
    }

    public List<AmaTurnoActa> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaTurnoActa a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaTurnoActa> listarPorLicSerie(Long nroLic, String serie,Date fechaHoy) {
        try {
            String jpql = "select a from AmaTurnoActa a where a.activo=1 and a.serie = :serie and a.turnoId.estado.codProg = 'TP_EST_CON' and FUNC('trunc', a.turnoId.fechaTurno) >= FUNC('trunc',:fechaHoy)";
            if (nroLic != null) {
                jpql += "and a.nroLic = :lic ";
            }
            Query q = em.createQuery(jpql);
            if (nroLic != null) {
                q.setParameter("lic", nroLic);
            }
            q.setParameter("serie", serie == null ? "" : serie.toUpperCase().trim());
            q.setParameter("fechaHoy", fechaHoy);
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                return q.getResultList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
