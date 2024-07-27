/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sistemabase.data.SbSistema;

/**
 *
 * @author Renato
 */
@Stateless
public class SbSistemaFacade extends AbstractFacade<SbSistema> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbSistemaFacade() {
        super(SbSistema.class);
    }
    
}
