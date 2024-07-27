/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbProvincia;

/**
 *
 * @author Renato
 */
@Stateless
public class SbDistritoFacade extends AbstractFacade<SbDistrito> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbDistritoFacade() {
        super(SbDistrito.class);
    }
    
    public List<SbDistrito> listarDistritos() {
        Query q = em.createQuery("SELECT D FROM SbDistrito D where D.activo = 1");
        return q.getResultList();
    }   
    
    public List<SbDistrito> obtenerUbigeo(String valor) {
        List<SbDistrito> respuesta = new ArrayList();
        String jpql = "select d from SbDistrito d where  d.nombre like :valor or d.provinciaId.nombre like :valor or d.provinciaId.departamentoId.nombre like :valor ";
        Query q = getEntityManager().createQuery(jpql);
        q.setParameter("valor","%"+ valor+"%");
        respuesta = q.getResultList();
        return respuesta;
    }
    
    public List<SbDistrito> lstDistritos(SbProvincia parametro) {
        List<SbDistrito> respuesta = new ArrayList();
        String sentencia = "select d from SbDistrito d where d.activo = 1 and d.provinciaId = :parametro ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", parametro);
        respuesta = query.getResultList();
        return respuesta;
    }
}
