/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.AmaSolicitudRecojo;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaSolicitudRecojoFacade extends AbstractFacade<AmaSolicitudRecojo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaSolicitudRecojoFacade() {
        super(AmaSolicitudRecojo.class);
    }

    public List<AmaSolicitudRecojo> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaSolicitudRecojo a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<Long> listaIdsEncontrados(Long idPersona) {
        List<Long> listRes = new ArrayList<>();
        Query q = em.createQuery("select distinct tp.id from AmaSolicitudRecojo sr LEFT JOIN sr.amaTarjetaPropiedadList tp "
                + "where  tp.id in (select tp1.id from AmaTarjetaPropiedad tp1 where tp1.personaVendedorId.id = :idPerUsu and tp1.activo = 1) and sr.activo=1 ");
        q.setParameter("idPerUsu", idPersona);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<AmaSolicitudRecojo> listaSolicitudesPorIdPersona(Long idPersona, TipoGamac tipoSol, String valor) {
        List<AmaSolicitudRecojo> listRes = new ArrayList<>();
        String jpql = "select distinct sr from AmaSolicitudRecojo sr left join sr.amaInventarioArmaList arma "
                + "where sr.tipoSolicitudId = :tipo and sr.personaArmeriaId.id = :id and sr.activo = 1 and (sr.estado = 1 or sr.estado = -1) ";

        if (valor != null && !"".equalsIgnoreCase(valor.trim())) {
            jpql += "and (arma.serie = :valor or OPERATOR('ToChar',sr.correlativo) = :valor) ";
        }
        jpql += "order by sr.correlativo desc ";
        Query q = em.createQuery(jpql);
        if (valor != null && !"".equalsIgnoreCase(valor.trim())) {
            q.setParameter("valor", valor.trim().toUpperCase());
        }
        q.setParameter("id", idPersona);
        q.setParameter("tipo", tipoSol);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public void anularPorId(AmaSolicitudRecojo objAmaSol) {
        em.merge(objAmaSol);
    }

    public Date obtenerFechaEmision(Long idTarjPro) {
        Date fecha = null;
        Query q = em.createQuery("select sr.fechaEmision from AmaSolicitudRecojo sr LEFT JOIN sr.amaTarjetaPropiedadList tp "
                + "where  tp.id = :idtp ");
        q.setParameter("idtp", idTarjPro);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            fecha = (Date) q.getResultList().get(0);
        }
        return fecha;
    }

    public AmaSolicitudRecojo obtenerSolicitudPorCorrelativo(Long correlativo) {
        AmaSolicitudRecojo res = null;
        Query q = em.createQuery("select sr from AmaSolicitudRecojo sr where sr.correlativo = ?1 and sr.activo = 1 ");
        q.setParameter(1, correlativo);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = (AmaSolicitudRecojo) q.getResultList().get(0);
        }
        return res;
    }
}
