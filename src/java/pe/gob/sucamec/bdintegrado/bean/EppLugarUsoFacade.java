/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppLugarUso;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.SbDepartamentoGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbProvinciaGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppLugarUsoFacade extends AbstractFacade<EppLugarUso> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppLugarUsoFacade() {
        super(EppLugarUso.class);
    }

    /**
     * METODO PARA BUSCAR LUGAR DE USO SEGUN FILTRO Y UBIGEO
     *
     * @param filtro
     * @param ubigeo
     * @param r
     * @return
     */
    public List<EppLugarUso> buscarLugarUso(String filtro, SbDistritoGt ubigeo, EppRegistro r) {
        String sentencia = "";
        if (r.getTipoProId().getCodProg().equals("TP_PRTUP_EVE")) {
            sentencia = "select l from EppRegistro r "
                    + "inner join r.eppLugarUsoList l "
                    + "inner join l.eppLugarUsoUbigeoList d "
                    + "where "
                    + "r.id = :rid and "
                    + "(l.nombre like :filtro or "
                    + "l.latitud like :filtro or "
                    + "l.longitud like :filtro or "
                    + "l.otraUbicacion like :filtro or :filtro is null) and "
                    + "(d.distId.id = :ubi or :ubi is null)";
        } else {
            sentencia = "select l from EppRegistro r "
                    + "inner join r.comId.eppLugarUsoList l "
                    + "inner join l.eppLugarUsoUbigeoList d "
                    + "where "
                    + "r.id = :rid and "
                    + "(l.nombre like :filtro or "
                    + "l.latitud like :filtro or "
                    + "l.longitud like :filtro or "
                    + "l.otraUbicacion like :filtro or :filtro is null) and "
                    + "(d.distId.id = :ubi or :ubi is null)";
        }

        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("rid", r.getId());
        query.setParameter("filtro", filtro == null ? null : ("%" + filtro + "%"));
        query.setParameter("ubi", ubigeo == null ? null : ubigeo.getId());
        return query.getResultList();
    }

}
