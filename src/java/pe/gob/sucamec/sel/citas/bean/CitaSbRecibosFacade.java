/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.citas.data.CitaSbRecibos;

/**
 *
 * @author msalinas
 */
@Stateless
public class CitaSbRecibosFacade extends AbstractFacade<CitaSbRecibos> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaSbRecibosFacade() {
        super(CitaSbRecibos.class);
    }

    public List<CitaSbRecibos> listaRecibosXSecuencia(Long secuencia, BigDecimal importe, Date fecha) {
        List<CitaSbRecibos> respuesta = new ArrayList();
        String sentencia = "select t from CitaSbRecibos t where t.activo = 1 "
                + "and t.nroSecuencia = :secuencia "
                + "and t.importe = :importe "
                + "and FUNC('trunc',t.fechaMovimiento) = FUNC('trunc',:fecha)";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("secuencia", secuencia);
        query.setParameter("importe", importe);
        query.setParameter("fecha", fecha);
        respuesta = query.getResultList();
        return respuesta;
    }

}
