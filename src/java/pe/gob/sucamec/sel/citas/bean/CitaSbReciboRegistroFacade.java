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
import pe.gob.sucamec.sel.citas.data.CitaSbReciboRegistro;

/**
 *
 * @author msalinas
 */
@Stateless
public class CitaSbReciboRegistroFacade extends AbstractFacade<CitaSbReciboRegistro> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaSbReciboRegistroFacade() {
        super(CitaSbReciboRegistro.class);
    }

    public List<CitaSbReciboRegistro> listaRecibosRegistroUso(Long secuencia, BigDecimal importe, Date fecha) {
        List<CitaSbReciboRegistro> respuesta = new ArrayList();
        String sentencia = "select t from CitaSbReciboRegistro t where t.reciboId.activo = 1 and t.activo = 1 "
                + "and t.reciboId.nroSecuencia = :secuencia "
                + "and t.reciboId.importe = :importe "
                + "and FUNC('trunc',t.reciboId.fechaMovimiento) = FUNC('trunc',:fecha)";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("secuencia", secuencia);
        query.setParameter("importe", importe);
        query.setParameter("fecha", fecha);
        respuesta = query.getResultList();
        return respuesta;
    }

}
