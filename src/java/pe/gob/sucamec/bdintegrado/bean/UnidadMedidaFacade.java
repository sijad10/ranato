/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.UnidadMedida;

/**
 *
 * @author msalinas
 */
@Stateless
public class UnidadMedidaFacade extends AbstractFacade<UnidadMedida> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UnidadMedidaFacade() {
        super(UnidadMedida.class);
    }

    public List<UnidadMedida> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select u from UnidadMedida u where trim(u.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<UnidadMedida> selectKilogramo() {
        Query q = em.createQuery("select u from UnidadMedida u where u.simbolo like 'KG'");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<UnidadMedida> listUnidadDiametro() {
        Query q = em.createQuery("SELECT u FROM UnidadMedida u where u.simbolo in ('PUL','MM') and u.activo = 1 ");
        return q.getResultList();
    }
    
    public List<UnidadMedida> listUnidadVelocidad() {
        Query q = em.createQuery("SELECT u FROM UnidadMedida u where u.simbolo in ('MPS') and u.activo = 1 ");
        return q.getResultList();
    }
    
    public List<UnidadMedida> listUnidadesPeso() {
        Query q = em.createQuery("SELECT u FROM UnidadMedida u where u.simbolo in ('KG','GR','KL9','KG6','KL6','KI6','TN','TM','KI','KI6')");
        return q.getResultList();

    }
}
