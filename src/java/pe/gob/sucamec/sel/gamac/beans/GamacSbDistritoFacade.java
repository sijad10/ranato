/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans; 

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.gamac.data.GamacSbDistrito;

/**
 *
 * @author mespinoza
 */
@Stateless
public class GamacSbDistritoFacade extends AbstractFacade<GamacSbDistrito> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacSbDistritoFacade() {
        super(GamacSbDistrito.class);
    }
    
    public List<GamacSbDistrito> lstDistritos(GamacSbDistrito parametro) {
        List<GamacSbDistrito> respuesta = new ArrayList();
        String sentencia = "select d from GamacSbDistrito d where d.provinciaId = :parametro ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", parametro);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<GamacSbDistrito> listarDistritos() {
        Query q = em.createQuery("SELECT D FROM GamacSbDistrito D where D.activo = 1 ");
        return q.getResultList();
    }
    
    public List<GamacSbDistrito> obtenerUbigeo(String valor) {
        List<GamacSbDistrito> respuesta = new ArrayList();
        String jpql = "select d from GamacSbDistrito d where  d.nombre like :valor or d.provinciaId.nombre like :valor or d.provinciaId.departamentoId.nombre like :valor ";
        Query q = getEntityManager().createQuery(jpql);
        q.setParameter("valor","%"+ valor+"%");
        respuesta = q.getResultList();
        return respuesta;
    }
    
}
