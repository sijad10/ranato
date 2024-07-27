/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.EppProveedorCliente;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppProveedorClienteFacade extends AbstractFacade<EppProveedorCliente> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppProveedorClienteFacade() {
        super(EppProveedorCliente.class);
    }
    
}
