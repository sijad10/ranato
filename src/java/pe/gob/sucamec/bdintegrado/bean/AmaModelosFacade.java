/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaModelosFacade extends AbstractFacade<AmaModelos> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaModelosFacade() {
        super(AmaModelos.class);
    }
    public List<AmaModelos> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaModelos a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<AmaModelos> obtenerListadoArmas(String tipoArma, String filtro) {
        String sql = "select a from AmaModelos a where a.activo = 1 and size(a.amaCatalogoList) > 0 and a.marcaId.codProg = 'TP_MAR'"+
                        " and a.tipoArmaId.activo = 1 and a.marcaId.activo = 1  ";
        if(tipoArma != null){
            sql = sql +" and UPPER(a.tipoArmaId.nombre) like :tipoArma ";
        }
        if(filtro != null){
            sql = sql +" and ( a.tipoArmaId.nombre like :campo "+
                        " OR a.marcaId.nombre like :campo "+
                        " OR a.modelo like :campo "+
                        " )";
        }
        sql = sql + " order by a.tipoArmaId.nombre,a.marcaId.nombre,a.modelo ";
        Query q = em.createQuery(sql);
        if(tipoArma != null){
            q.setParameter("tipoArma", "%" + tipoArma + "%");
        }
        if(filtro != null){
            q.setParameter("campo", "%" + filtro + "%");
        }
        return q.getResultList();
    }
    
    public List<Map> obtenerListadoArmasMap(String filtro) {
        String sql = "SELECT t0.ID, t0.MODELO, t0.MARCA_ID, t0.TIPO_ARMA_ID, " +
                    " LISTAGG(t6.NOMBRE, '/') WITHIN GROUP (ORDER BY t0.ID) AS CALIBRE, t2.nombre AS TIPO_ARMA, t1.nombre AS MARCA " +
                    " FROM BDINTEGRADO.AMA_MODELOS t0, BDINTEGRADO.AMA_CATALOGO t2, BDINTEGRADO.AMA_CATALOGO t1, BDINTEGRADO.AMA_MODELO_CALIBRE t5, BDINTEGRADO.AMA_CATALOGO t6 " +
                    " WHERE (((((t0.ACTIVO = 1 " +
                    " AND (t1.COD_PROG = 'TP_MAR')) AND (t2.ACTIVO = 1)) AND (t1.ACTIVO = 1))" +
                    " AND ((t5.MODELO_ID = t0.ID) AND (t6.ID = t5.CATALOGO_ID)) AND t6.activo = 1 )" +
                    " AND ((t1.ID = t0.MARCA_ID) AND (t2.ID = t0.TIPO_ARMA_ID)))";                    
        
        if(filtro != null){
            sql = sql +" AND ((t2.NOMBRE LIKE ?1 OR t1.NOMBRE LIKE ?1) OR t0.MODELO LIKE ?1 )";
        }
        sql = sql + " GROUP BY t0.ID, t0.ACTIVO, t0.MODELO, t0.DISPOSICION_ID, t0.MARCA_ID, t0.TIPO_ARMA_ID, t2.nombre, t1.nombre "+
                    " ORDER BY  t2.nombre, t1.nombre, t0.modelo ";
        
        Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        if(filtro != null){
            q.setParameter(1, "%" + filtro + "%");
        }
        return q.getResultList();
    }
    
    public AmaModelos obtenerModelo(long id) {
        Query q = em.createQuery("select a from AmaModelos a where a.activo = 1 and a.id = :id");
        q.setParameter("id", id);
        
        if(!q.getResultList().isEmpty()){
            return (AmaModelos) q.getResultList().get(0);    
        }
        
        return null;    
    }
    
    public AmaModelos obtenerModeloById(long id) {
        Query q = em.createQuery("select a from AmaModelos a where a.id = :id");
        q.setParameter("id", id);
        return (AmaModelos) q.getResultList().get(0);
    }
    
    public List<AmaCatalogo> obtenerCalibreByModelo(Long modeloId, String calibre) {
        Query q = em.createQuery("select e from AmaCatalogo e " +
                                "join e.amaModelosList m " +
                                "where m.id = :modeloId and e.nombre = :calibre ");
        q.setParameter("modeloId", modeloId);
        q.setParameter("calibre", calibre);
        return q.getResultList();
    }

    public List<Map> obtenerListadoArmasMap(AmaCatalogo tipoArt, String filtro) {
        String sql = "SELECT t0.ID, t0.MODELO, t0.MARCA_ID, t0.TIPO_ARMA_ID, "
                + " LISTAGG(t6.NOMBRE, '/') WITHIN GROUP (ORDER BY t0.ID) AS CALIBRE, t2.nombre AS TIPO_ARMA, t1.nombre AS MARCA "
                + " FROM BDINTEGRADO.AMA_MODELOS t0, BDINTEGRADO.AMA_CATALOGO t2, BDINTEGRADO.AMA_CATALOGO t1, BDINTEGRADO.AMA_MODELO_CALIBRE t5, BDINTEGRADO.AMA_CATALOGO t6, BDINTEGRADO.AMA_CATALOGO t7 "
                + " WHERE ((((((t0.ACTIVO = 1 "
                + " AND (t7.ID = t0.TIPO_ARTICULO_ID)) "
                + " AND (t1.COD_PROG = 'TP_MAR')) AND (t2.ACTIVO = 1)) AND (t1.ACTIVO = 1))"
                + " AND ((t5.MODELO_ID = t0.ID) AND (t6.ID = t5.CATALOGO_ID)) AND t6.activo = 1 )"
                + " AND ((t1.ID = t0.MARCA_ID) AND (t2.ID = t0.TIPO_ARMA_ID)))";

        if (filtro != null) {
            sql = sql + " AND ((t2.NOMBRE LIKE ?1 OR t1.NOMBRE LIKE ?1) OR t0.MODELO LIKE ?1 )";
        }
        if (tipoArt != null) {
            sql = sql + " AND (t7.COD_PROG = ?2) ";
        }
        sql = sql + " GROUP BY t0.ID, t0.ACTIVO, t0.AUD_LOGIN, t0.AUD_NUM_IP, t0.LONG_ARMA, t0.LONG_CANON, t0.MODELO, t0.DISPOSICION_ID, t0.MARCA_ID, t0.TIPO_ARMA_ID, t2.nombre, t1.nombre "
                + " ORDER BY  t2.nombre, t1.nombre, t0.modelo ";

        Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        if (filtro != null) {
            q.setParameter(1, "%" + filtro + "%");
        }
        if (tipoArt != null) {
            q.setParameter(2, tipoArt.getCodProg());
        }
        return q.getResultList();
    }

    public Map obtenerArmaMap(AmaModelos amaModelo) {
        Map res = null;
        if (amaModelo != null) {
            String sql = "SELECT t0.ID, t0.MODELO, t0.MARCA_ID, t0.TIPO_ARMA_ID, "
                    + " LISTAGG(t6.NOMBRE, '/') WITHIN GROUP (ORDER BY t0.ID) AS CALIBRE, t2.nombre AS TIPO_ARMA, t1.nombre AS MARCA "
                    + " FROM BDINTEGRADO.AMA_MODELOS t0, BDINTEGRADO.AMA_CATALOGO t2, BDINTEGRADO.AMA_CATALOGO t1, BDINTEGRADO.AMA_MODELO_CALIBRE t5, BDINTEGRADO.AMA_CATALOGO t6 "
                    + " WHERE (((((t0.ACTIVO = 1 "
                    + " AND (t1.COD_PROG = 'TP_MAR')) AND (t2.ACTIVO = 1)) AND (t1.ACTIVO = 1))"
                    + " AND ((t5.MODELO_ID = t0.ID) AND (t6.ID = t5.CATALOGO_ID)) AND t6.activo = 1 )"
                    + " AND ((t1.ID = t0.MARCA_ID) AND (t2.ID = t0.TIPO_ARMA_ID)))";

            if (amaModelo != null) {
                sql = sql + " AND ((t2.ID = ?3 AND t1.ID = ?2) AND t0.ID = ?1 )";
            }
            sql = sql + " GROUP BY t0.ID, t0.ACTIVO, t0.AUD_LOGIN, t0.AUD_NUM_IP, t0.LONG_ARMA, t0.LONG_CANON, t0.MODELO, t0.DISPOSICION_ID, t0.MARCA_ID, t0.TIPO_ARMA_ID, t2.nombre, t1.nombre "
                    + " ORDER BY  t2.nombre, t1.nombre, t0.modelo ";

            Query q = em.createNativeQuery(sql);
            q.setHint("eclipselink.result-type", "Map");
            if (amaModelo != null) {
                q.setParameter(1, amaModelo.getId());
                q.setParameter(2, amaModelo.getMarcaId().getId());
                q.setParameter(3, amaModelo.getTipoArmaId().getId());
            }
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                res = (Map) q.getResultList().get(0);
            }
        }
        return res;
    }
    
}
