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
import pe.gob.sucamec.bdintegrado.data.EppContratoAlqPolv;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppContratoAlqPolvFacade extends AbstractFacade<EppContratoAlqPolv> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppContratoAlqPolvFacade() {
        super(EppContratoAlqPolv.class);
    }
    
    public EppContratoAlqPolv buscarContratoById(Long contratoId) {
        Query query = em.createQuery("SELECT p FROM EppContratoAlqPolv p "+
                                     " WHERE p.activo = 1 and p.id = :contratoId " );
        query.setParameter("contratoId", contratoId);
        if(!query.getResultList().isEmpty()){
            return (EppContratoAlqPolv) query.getResultList().get(0);
        }
        return null;
    }
}
