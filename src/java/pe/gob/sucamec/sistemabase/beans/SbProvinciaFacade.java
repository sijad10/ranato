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
import pe.gob.sucamec.sistemabase.data.SbProvincia;

/**
 *
 * @author Renato
 */
@Stateless
public class SbProvinciaFacade extends AbstractFacade<SbProvincia> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbProvinciaFacade() {
        super(SbProvincia.class);
    }
    
    public List<SbProvincia> lstProvincias(SbDepartamento parametro) {
        List<SbProvincia> respuesta = new ArrayList();
        String sentencia = "select p from SbProvincia p where p.activo = 1 and p.departamentoId = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", parametro);
        respuesta = query.getResultList();
        return respuesta;
    }
    
}
