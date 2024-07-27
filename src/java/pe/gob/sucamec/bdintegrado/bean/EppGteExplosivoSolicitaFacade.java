/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.EppGteExplosivoSolicita;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppGteExplosivoSolicitaFacade extends AbstractFacade<EppGteExplosivoSolicita> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppGteExplosivoSolicitaFacade() {
        super(EppGteExplosivoSolicita.class);
    }
    
}
