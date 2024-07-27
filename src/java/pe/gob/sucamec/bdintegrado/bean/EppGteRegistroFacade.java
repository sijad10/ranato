/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppGteRegistro;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppGteRegistroFacade extends AbstractFacade<EppGteRegistro> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppGteRegistroFacade() {
        super(EppGteRegistro.class);
    }

    /**
     *
     * @param r
     * @return
     */
    public String nroGteAsociada(EppRegistro r) {
        try {
            String jpql = "select r from EppGteRegistro r "
                    + "where "
                    + "r.registroGtId.id = :filtro and "
                    + "r.activo = 1";

            //GENERANDO QUERY
            Query query = em.createQuery(jpql);
            query.setParameter("filtro", r.getId());

            if (!query.getResultList().isEmpty()) {
                EppGteRegistro gter = (EppGteRegistro) query.getResultList().get(0);
                return gter.getNroSolicitud();
            } else {
                return "No se encontró datos de la solicitud.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public EppGteRegistro obtenerGuiaGteById(Long id) {
        try {
            String jpql = "select r from EppGteRegistro r "
                    + "where r.id = :id ";

            //GENERANDO QUERY
            Query query = em.createQuery(jpql);
            query.setParameter("id", id);

            if (query.getResultList().isEmpty()) {
                return null;
            }
            
            return (EppGteRegistro) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
