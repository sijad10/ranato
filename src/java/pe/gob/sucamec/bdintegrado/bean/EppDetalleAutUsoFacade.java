/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppCom;
import pe.gob.sucamec.bdintegrado.data.EppDetalleAutUso;
import pe.gob.sucamec.bdintegrado.data.EppExplosivo;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppDetalleAutUsoFacade extends AbstractFacade<EppDetalleAutUso> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppDetalleAutUsoFacade() {
        super(EppDetalleAutUso.class);
    }

    //SALDOS EN CALIENTE
    public double saldoActualAutUso(EppCom c, EppExplosivo e) {
        double cantidadTotalGTtipo1 = 0;
        double cantidadTotalGTtipo2 = 0;
        double cantidadTotalGTtipo3 = 0;

        String jpql1 = "select c.cantidad \n"
                + "from \n"
                + "bdintegrado.epp_detalle_com c \n"
                + "where \n"
                + "c.com_id = ?1 and \n"
                + "c.explosivo_id = ?2";

        String jpql2 = "select sum(es.cantidad) \n"
                + "from \n"
                + "bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id \n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_registro regResol on res.registro_id = regResol.id \n"
                + "inner join bdintegrado.epp_com c on regResol.com_id = c.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado es on es.registro_id = rgt.id\n"
                + "inner join bdintegrado.tipo_explosivo te on rgt.tipo_tramite = te.id \n"
                + "where \n"
                + "r.activo = 1 and \n"
                + "te.cod_prog = 'TP_RGUIA_ADQUI1' and\n"
                + "regResol.com_id = ?1 and\n"
                + "es.explosivo_id = ?2";

        String jpql3 = "select sum(es.cantidad_extornada) \n"
                + "from \n"
                + "bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id \n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_registro regResol on res.registro_id = regResol.id \n"
                + "inner join bdintegrado.epp_com c on regResol.com_id = c.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado es on es.registro_id = rgt.id\n"
                + "inner join bdintegrado.tipo_explosivo te on rgt.tipo_tramite = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te1 on r.estado = te1.id \n"
                + "where \n"
                + "te1.cod_prog in ('TP_REGEV_EXT','TP_REGEV_EXTPAR','TP_REGEV_EXTTOT') and \n"
                + "regResol.com_id = ?1 and\n"
                + "es.explosivo_id = ?2";

        Query q1 = em.createNativeQuery(jpql1);
        Query q2 = em.createNativeQuery(jpql2);
        Query q3 = em.createNativeQuery(jpql3);

        q1.setParameter(1, c.getId());
        q1.setParameter(2, e.getId());

        q2.setParameter(1, c.getId());
        q2.setParameter(2, e.getId());

        q3.setParameter(1, c.getId());
        q3.setParameter(2, e.getId());

        if (!q1.getResultList().isEmpty() && !(q1.getResultList().get(0) == null)) {
            cantidadTotalGTtipo1 = Double.valueOf(q1.getResultList().get(0).toString());
        }

        if (!q2.getResultList().isEmpty() && !(q2.getResultList().get(0) == null)) {
            cantidadTotalGTtipo2 = Double.valueOf(q2.getResultList().get(0).toString());
        }

        if (!q3.getResultList().isEmpty() && !(q3.getResultList().get(0) == null)) {
            cantidadTotalGTtipo3 = Double.valueOf(q3.getResultList().get(0).toString());
        }

        return (cantidadTotalGTtipo1 - (cantidadTotalGTtipo2 - cantidadTotalGTtipo3));
    }

}
