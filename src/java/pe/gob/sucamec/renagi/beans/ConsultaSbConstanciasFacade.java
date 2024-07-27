/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.renagi.data.ConsultaSbConstancias;

/**
 *
 * @author rarevalo
 */
@Stateless
public class ConsultaSbConstanciasFacade extends AbstractFacade<ConsultaSbConstancias> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaSbConstanciasFacade() {
        super(ConsultaSbConstancias.class);
    }

    public List<ConsultaSbConstancias> listxRepresentante(String filtro) {
        if (filtro != null) {
            Query q = em.createQuery(
                    "select p from ConsultaSbPersona p"
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
    
    public List<ConsultaSbConstancias> selectLikePersonaNatural() {
        Query q = em.createQuery(
                "select p from ConsultaSbPersona p where p.activo = 1 and p.numDoc is not null order by p.apePat, p.apeMat, p.nombres");
        return q.getResultList();

    } 

}
