/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.data.SbConstancias;

/**
 *
 * @author rarevalo
 */
@Stateless
public class SbConstanciasFacade extends AbstractFacade<SbConstancias> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbConstanciasFacade() {
        super(SbConstancias.class);
    }

    public List<SbConstancias> listaConstanciaTraslado(HashMap mMap) {
        try {
            String jpql = "SELECT r FROM SbConstancias r"
                    + " WHERE r.activo = 1"
                    + " and r.referenciaId = :referenciaId"
                    + " and r.personaId.id = :personaId"
                    + " and r.tipoConstanciaId.codProg = :tipo";

            Query q = em.createQuery(jpql);
            q.setParameter("referenciaId", (Long) mMap.get("referenciaId"));
            q.setParameter("personaId", (Long) mMap.get("personaId"));
            q.setParameter("tipo", mMap.get("tipo").toString());

            return q.getResultList();
                
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
}
