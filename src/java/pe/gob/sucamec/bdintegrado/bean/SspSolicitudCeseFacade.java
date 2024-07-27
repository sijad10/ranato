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
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.SspSolicitudCese;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspSolicitudCeseFacade extends AbstractFacade<SspSolicitudCese> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspSolicitudCeseFacade() {
        super(SspSolicitudCese.class);
    }
    public List<SspSolicitudCese> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspSolicitudCese s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        //q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    /**
     * LISTADO DE SOLICITUDES DE CESE
     * @param mMap
     * @return Listado de solicitudes de cese
     */
    public List<SspSolicitudCese> buscarSolicitudesCese(HashMap mMap) {
        String jpql = "select c from SspSolicitudCese c where c.activo = 1 and c.personaId.id = :personaId and c.tipoSolicitudId.codProg = 'TP_TIPOCESE_WEB' ";        
        if (mMap.get("fechaIni") != null && mMap.get("fechaFin") != null) {
            jpql = jpql + " AND (FUNC('trunc',c.fechaSolicitud) >= FUNC('trunc',:fechaIni)) "
                        + " AND (FUNC('trunc',c.fechaSolicitud) <= FUNC('trunc',:fechaFin)) ";
        }
        
        jpql = jpql + "order by c.id desc";
        Query q = em.createQuery(jpql);
        q.setParameter("personaId", (Long) mMap.get("personaId"));
        
        if (mMap.get("fechaIni") != null && mMap.get("fechaFin") != null) {
            q.setParameter("fechaIni", (Date) mMap.get("fechaIni"), TemporalType.DATE);
            q.setParameter("fechaFin", (Date) mMap.get("fechaFin"), TemporalType.DATE);
        }
        
        return q.setMaxResults(200).getResultList();
    }
    
    
    public List<Map> obtenerVigilanteCese(String tipoDoc, String numDoc, String rucAdministrado){
        String jpql = "";
        
        Query q = em.createNativeQuery(jpql);
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
//        q.setParameter(1, id);
        res = q.setMaxResults(80).getResultList();
        return res;
    }
}
