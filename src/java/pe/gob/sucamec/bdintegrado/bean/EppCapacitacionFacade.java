/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.EppCapacitacion;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppCapacitacionFacade extends AbstractFacade<EppCapacitacion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppCapacitacionFacade() {
        super(EppCapacitacion.class);
    }
    
    public List<EppCapacitacion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select e from EppCapacitacion e where trim(e.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    /**
     * LISTADO DE CAPACITACIONES DE MEDIDAS DE SEGURIDAD
     * @param mMap
     * @param tipo
     * @return Listado de Capacitaciones
     */
    public List<EppCapacitacion> buscarCapacitacionesMS(HashMap mMap, String tipo){
        try {
            List<EppCapacitacion> lst = new ArrayList();
            String jpql = "select distinct(r) from EppCapacitacion r left join r.tipoExplosivoList e where r.activo = 1  ";
            
            if (mMap.get("fechaIni") != null && mMap.get("fechaFin") != null) {
                jpql = jpql + " AND r.fechaInicio>= :fechaIni AND r.fechaFin <= :fechaFin";
            }
            if (mMap.get("tipoBusqueda") != null) {
                switch (mMap.get("tipoBusqueda").toString()) {
                    case "capa":   // CAPACITACION
                        jpql = jpql + " AND CONCAT(r.ubigeoId.nombre,' ',r.ubigeoId.provinciaId.nombre,' ',r.ubigeoId.provinciaId.departamentoId.nombre) like :campo ";
                        break;
                    case "sede":   // SEDE
                        jpql = jpql + " AND TRIM(r.sedeOrganizaId.nombre) like :campo";
                        break;
                    case "diri":   // DIRIGIDO A LOS QUE REALIZAN
                        jpql = jpql + " AND TRIM(e.nombre) like :campo";
                        break;
                }
            }
            jpql = jpql + " order by r.id desc";
            Query q = em.createQuery(jpql);
            if (mMap.get("tipoBusqueda") != null) {
                q.setParameter("campo", "%" + mMap.get("filtro") + "%");
            }
            if (mMap.get("fechaIni") != null && mMap.get("fechaFin") != null) {
                q.setParameter("fechaIni", (Date) mMap.get("fechaIni"), TemporalType.DATE);
                q.setParameter("fechaFin", (Date) mMap.get("fechaFin"), TemporalType.DATE);
            }

            lst = q.setMaxResults(200).getResultList();
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * LISTADO DE CAPACITACIONES PARA BANDEJA
     * @return Listado de Capacitaciones
     */
    public List<EppCapacitacion> buscarCapacitacionCertificadoMSBandeja() {
        Query q = em.createQuery("select c from EppCapacitacion c where c.activo = 1 order by c.id desc");
        return q.setMaxResults(200).getResultList();
    }
    
    /**
     * LISTADO DE CAPACITACIONES NO TERMINADAS
     * @return Listado de Capacitaciones
     */
    public List<EppCapacitacion> buscarCapacitacionCertificadoMS() {
        Query q = em.createQuery("select c from EppCapacitacion c where c.activo = 1 AND c.fechaFin >= current_date order by c.id desc");
        return q.setMaxResults(200).getResultList();
    }
    
    /**
     * LISTADO DE CAPACITACIONES CON FECHA DE INSCRIPCION ACTIVAS
     * @return Listado de Capacitaciones
     */
    public List<EppCapacitacion> buscarCapacitacionConFechaLimiteInscripcion() {
        Query q = em.createQuery("select c from EppCapacitacion c where c.activo = 1 AND c.fechaLimInscrip >= current_date order by c.id desc");
        return q.setMaxResults(200).getResultList();
    }
    
    /**
     * BUSQUEDA DE MINIMA Y MAXIMA FECHA DE CAPACITACION
     * @param idCap
     * @return Map con fechas minima y maxima de capacitacion
     */
    public Map buscarRangoFechasCertificado(String idCap) {
        String sql = "";
        Map mMap = new HashMap();
        
        sql = "select MIN(h.fecha), MAX(h.fecha) from EppCapacitacion c join c.eppCapaHorariodirList h where c.activo = 1 AND h.activo =1 AND trim(c.id) = :idCap ";

        javax.persistence.Query q = em.createQuery(sql);
        q.setParameter("idCap", idCap);     
        
        List<Object[]> results = q.getResultList();

        for (Object[] _values : results) {
            mMap.put("fechaMin", _values[0]);
            mMap.put("fechaMax", _values[1]);            
        }

        return mMap;
    }
}
