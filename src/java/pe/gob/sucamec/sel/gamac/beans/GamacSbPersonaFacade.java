/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.gamac.data.GamacSbPersona;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacSbPersonaFacade extends AbstractFacade<GamacSbPersona> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacSbPersonaFacade() {
        super(GamacSbPersona.class);
    }

    public List<GamacSbPersona> listxRepresentante(String filtro) {
        if (filtro != null) {
            Query q = em.createQuery(
                    "select distinct p from GamacSbPersona p"
                            + " inner join GamacSbRelacionPersona rp ON rp.personaDestId.id = p.id and rp.personaOriId.id = :idOrigen "                            
                            + " where"
                            + " p.activo = 1 and p.numDoc is not null"
                            + " order by p.apePat, p.apeMat, p.nombres");
            
            q.setParameter("idOrigen", Long.valueOf(filtro)); 
            return q.getResultList();

        }else{
            return null;
        }

    }
    
    public List<GamacSbPersona> selectLikePersonaNatural() {
        Query q = em.createQuery(
                "select p from GamacSbPersona p where p.activo = 1 and p.numDoc is not null order by p.apePat, p.apeMat, p.nombres");
        return q.getResultList();

    }
    
}
