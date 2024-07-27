/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SbPaisGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbPaisFacadeGt extends AbstractFacade<SbPaisGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbPaisFacadeGt() {
        super(SbPaisGt.class);
    }
    
    public SbPaisGt obtenerPaisByNombre(String nombre) {
        if (nombre == null) {
            nombre = "";
        }
        javax.persistence.Query q = em.createQuery("select p from SbPaisGt p where p.nombre like :nombre and p.activo = 1 ");
        q.setParameter("nombre", nombre);
        if(!q.getResultList().isEmpty()){
            return (SbPaisGt) q.getResultList().get(0);
        }        
        return null;
    }
    
    public List<SbPaisGt> listarPaises() {
        List<SbPaisGt> xlista = new ArrayList<SbPaisGt>();
        javax.persistence.Query q = em.createQuery("select p from SbPaisGt p where p.activo = 1 order by p.nombre asc");
        xlista = q.getResultList();       
        return xlista;
    }
}
