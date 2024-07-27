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
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppExplosivo;
import pe.gob.sucamec.bdintegrado.data.EppLibroMes;
import pe.gob.sucamec.bdintegrado.data.EppLibroUsoDiario;
import pe.gob.sucamec.bdintegrado.data.EppRegistroGuiaTransito;

/**
 *
 * @author rchipana
 */
@Stateless
public class EppLibroUsoDiarioFacade extends AbstractFacade<EppLibroUsoDiario> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppLibroUsoDiarioFacade() {
        super(EppLibroUsoDiario.class);
    }

    public List<EppLibroUsoDiario> listLibroUsoDiario(EppLibroMes libroMes) {
        Query q = em.createQuery("SELECT p FROM EppLibroUsoDiario p WHERE p.activo = 1 and p.libroMesId = :libroMes");
        q.setParameter("libroMes", libroMes);
        return q.getResultList();
    }

    public List<EppLibroUsoDiario> listLibroUsoDiarioAll() {
        Query q = em.createQuery("SELECT p FROM EppLibroUsoDiario p WHERE p.activo = 1");
        return q.getResultList();
    }

    public List<EppLibroUsoDiario> listLibroUsoDiarioXguia(EppRegistroGuiaTransito re) {
        Query q = em.createQuery("SELECT p FROM EppLibroUsoDiario p WHERE p.activo = 1 and p.guiaTransitoId = :re");
        q.setParameter("re", re);
        return q.getResultList();
    }

    public List<EppRegistroGuiaTransito> listLibroUsoDiarioXguia(List<EppRegistroGuiaTransito> list) {
        Query q = em.createQuery("SELECT p.guiaTransitoId FROM EppLibroUsoDiario p WHERE p.activo = 1 and p.guiaTransitoId in :re");
        q.setParameter("re", list);
        return q.getResultList();
    }

    public List<EppLibroUsoDiario> listLibroUsoDiarioXexplosivo(EppExplosivo re) {
        Query q = em.createQuery("SELECT p FROM EppLibroUsoDiario p WHERE p.activo = 1 and p.explosivoId = :re");
        q.setParameter("re", re);
        return q.getResultList();
    }

    public List<EppLibroUsoDiario> listLibroUsoDiarioXexplosivoLibroMes(EppExplosivo re, EppLibroMes ep) {
        String jpql = "SELECT p FROM EppLibroUsoDiario p WHERE p.activo = 1 and p.explosivoId = :re";
        if (ep != null) {
            jpql = jpql + " and p.libroMesId = :ep";
        }
        Query q = em.createQuery(jpql);

        if (ep != null) {
            q.setParameter("ep", ep);
        }
        q.setParameter("re", re);

        return q.getResultList();
    }

    public Double cantidadBasedatos(Long re) {
        try {
            Query q = em.createQuery("SELECT p.cantidad FROM EppLibroUsoDiario p WHERE p.activo = 1 and p.id = :re");
            q.setParameter("re", re);
            return (Double) q.getSingleResult();
        } catch (Exception e) {
            return 0.0;
        }

    }

}
