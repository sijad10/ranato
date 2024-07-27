/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbDepartamentoGt;
import pe.gob.sucamec.bdintegrado.data.SbProvinciaGt;


/**
 *
 * @author mespinoza
 */
@Stateless
public class SbProvinciaFacadeGt extends AbstractFacade<SbProvinciaGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbProvinciaFacadeGt() {
        super(SbProvinciaGt.class);
    }

    public List<SbProvinciaGt> lstProvincias(SbDepartamentoGt parametro) {
        List<SbProvinciaGt> respuesta = new ArrayList();
        String sentencia = "select p from SbProvinciaGt p where p.departamentoId = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", parametro);
        respuesta = query.getResultList();
        return respuesta;
    }

}
