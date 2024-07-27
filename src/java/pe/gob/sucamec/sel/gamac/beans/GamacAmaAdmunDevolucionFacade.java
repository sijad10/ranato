/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunDevolucion;

/**
 *
 * @author msalinas
 */
@Stateless
public class GamacAmaAdmunDevolucionFacade extends AbstractFacade<GamacAmaAdmunDevolucion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacAmaAdmunDevolucionFacade() {
        super(GamacAmaAdmunDevolucion.class);
    }

}
