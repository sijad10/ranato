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
import pe.gob.sucamec.bdintegrado.data.EppGuiaTransitoVehiculo;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppGuiaTransitoVehiculoFacade extends AbstractFacade<EppGuiaTransitoVehiculo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppGuiaTransitoVehiculoFacade() {
        super(EppGuiaTransitoVehiculo.class);
    }
    
    public List<EppGuiaTransitoVehiculo> buscarVehiculo(String filtro) {
        try {
            String jpql = "select r from EppGuiaTransitoVehiculo r "
                    + "where "
                    + "(r.placa like :filtro or "
                    + "r.marca like :filtro or "
                    + "trim(r.anio) like :filtro) and "
                    + "r.activo = 1";

            //ORDENANDO LISTADO
            jpql = jpql + " order by r.id desc";
            //GENERANDO QUERY
            Query query = em.createQuery(jpql);
            query.setParameter("filtro", filtro == null ? "%%" : ("%" + filtro + "%"));
            return query.setMaxResults(50).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
