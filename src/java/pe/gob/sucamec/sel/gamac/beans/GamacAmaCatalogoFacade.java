/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.gamac.data.GamacAmaCatalogo;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacAmaCatalogoFacade extends AbstractFacade<GamacAmaCatalogo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacAmaCatalogoFacade() {
        super(GamacAmaCatalogo.class);
    }
    
    public List<GamacAmaCatalogo> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from GamacAmaCatalogo a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<GamacAmaCatalogo> listxMarca(String s) {
        if (s == null) {
            s = "";
        }
        
        try {
            Query q = em.createQuery("select a from GamacAmaCatalogo a where trim(a.nombre) like :nombre and a.tipoId.codProg='TP_MCMUN' and a.activo=1");
            q.setParameter("nombre", "%" + s.toUpperCase().trim() + "%");
            q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();            
        } catch (Exception e) {
            return null;
        }
    }
    
    public String listarCatalogoMunicionesValidos(String s) {
        String sql = "SELECT c.nombre, min(C.ID) AS ID " +
                        "FROM BDINTEGRADO.AMA_CATALOGO c " +
                        "INNER JOIN BDINTEGRADO.AMA_CATALOGO tp on tp.ID = c.TIPO_ID " +
                        "WHERE c.nombre like ?1 " +
                        "AND tp.COD_PROG='TP_MCMUN' " +
                        "GROUP BY c.nombre";
        Query nq = em.createNativeQuery(sql);
        nq.setParameter("1", "%" + s.toUpperCase().trim() + "%");
        
        nq.setHint("eclipselink.result-type", "Map");
        List<Map> mapLst = (List<Map>) nq.getResultList();
        String catValidos = "";
        for (Map mapa : mapLst) {
            if (catValidos.equals("")) {
                catValidos = "" + mapa.get("ID");
            } else {
                catValidos = catValidos + ", " + mapa.get("ID");
            }
        }
        return catValidos;
    }
    
    public List<GamacAmaCatalogo> listxMarcaMunicion(String s) {
        if (s == null) {
            s = "";
        }
        
        try {
            String marcasValidos = listarCatalogoMunicionesValidos(s);
            
            Query q = em.createQuery("select a from GamacAmaCatalogo a "
                    + "where trim(a.nombre) like :nombre "
                    + "and a.tipoId.codProg='TP_MCMUN' "
                    + "and a.activo=1 "
                    + "and a.id in ("+ marcasValidos + ") "
            );
            q.setParameter("nombre", "%" + s.toUpperCase().trim() + "%");
            //q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();            
        } catch (Exception e) {
            return null;
        }
    }

    public List<GamacAmaCatalogo> listxCalibreArma(String s) {
        if (s == null) {
            s = "";
        }
        
        try {
            String calibresValidos = listarCatalogoMunicionesValidos(s);
            
            Query q = em.createQuery("select a from GamacAmaCatalogo a "
                    + "where trim(a.nombre) like :nombre "
                    + "and a.tipoId.codProg='TP_CAL' "
                    + "and a.activo=1 "
                    + "and a.id in ("+ calibresValidos + ") "
            );
            q.setParameter("nombre", "%" + s.toUpperCase().trim() + "%");
            //q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList();            
        } catch (Exception e) {
            return null;
        }
    }
    
}
