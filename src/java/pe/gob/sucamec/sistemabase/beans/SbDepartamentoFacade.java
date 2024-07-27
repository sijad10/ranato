/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.data.SbDepartamento;

/**
 *
 * @author Renato
 */
@Stateless
public class SbDepartamentoFacade extends AbstractFacade<SbDepartamento> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbDepartamentoFacade() {
        super(SbDepartamento.class);
    }
    
    public List<SbDepartamento> lstDepartamentos() {
        List<SbDepartamento> respuesta = new ArrayList();
        String sentencia = "select d from SbDepartamento d where d.activo = 1 ";
        Query query = getEntityManager().createQuery(sentencia);
        //query.setParameter("parametro", "%" + parametro + "%");
        respuesta = query.getResultList();
        respuesta.remove(0);
        return respuesta;
    }
}
