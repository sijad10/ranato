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
import pe.gob.sucamec.bdintegrado.data.EppGuiaTransitoPolicia;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppGuiaTransitoPoliciaFacade extends AbstractFacade<EppGuiaTransitoPolicia> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppGuiaTransitoPoliciaFacade() {
        super(EppGuiaTransitoPolicia.class);
    }

    public List<EppGuiaTransitoPolicia> buscarPolicias(String filtro) {
        try {
            String jpql = "select r from EppGuiaTransitoPolicia r "
                    + "where "
                    + "(r.nombresPolicia like :filtro or "
                    + "r.apaternoPolicia like :filtro or "
                    + "r.amaternoPolicia like :filtro or "
                    + "trim(r.nroPlaca) like :filtro) and "
                    + "r.activo = 1";

            //ORDENANDO LISTADO
            jpql = jpql + " order by r.id desc";
            //GENERANDO QUERY
            Query query = em.createQuery(jpql);
            query.setParameter("filtro", filtro == null ? "%%" : ("%" + filtro.trim() + "%"));
            return query.setMaxResults(50).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EppGuiaTransitoPolicia> buscarPoliciaDuplicado(String filtro) {
        try {
            String jpql = "select r from EppGuiaTransitoPolicia r "
                    + "where "
                    + "trim(r.dniPolicia) = :filtro "
                    + "and r.activo = 1";
            //GENERANDO QUERY
            Query query = em.createQuery(jpql);
            query.setParameter("filtro", filtro);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public EppGuiaTransitoPolicia buscarPoliciaById(Long id) {
        try {
            String jpql = "select r from EppGuiaTransitoPolicia r "
                    + "where "
                    + " r.id = :id "
                    + "and r.activo = 1";
            //GENERANDO QUERY
            Query query = em.createQuery(jpql);
            query.setParameter("id", id);
            
            if(!query.getResultList().isEmpty()){
                return (EppGuiaTransitoPolicia) query.getSingleResult();
            }
        } catch (Exception e) {
            e.printStackTrace();            
        }
        return null;
    }
}
