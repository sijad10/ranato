/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SbPublicacionDoc;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbPublicacionDocFacade extends AbstractFacade<SbPublicacionDoc> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbPublicacionDocFacade() {
        super(SbPublicacionDoc.class);
    }
    public List<SbPublicacionDoc> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbPublicacionDoc s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SbPublicacionDoc> obtenerListadoPorArea(Long areaId) {
        try {
            String jpql = "select s from SbPublicacionDoc s where s.activo = 1 ";
            if(areaId != null){
                jpql += " and s.areaId.id = :areaId ";
            }
            jpql += " order by s.fechaDoc desc";
            
            Query q = em.createQuery(jpql);
            if(areaId != null){
                q.setParameter("areaId", areaId);
            }
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<SbPublicacionDoc> buscarPublicacionMovil(String filtro, Long areaId, Long anio, Long tipoDocId) {
        try {
            String jpql = "select s from SbPublicacionDoc s where s.activo = 1 and s.estadoId.codProg = 'TP_EPUB_PUB' ";
            if(filtro != null && !filtro.trim().isEmpty()){
                jpql += " and (s.numeroDoc like :filtro or s.titulo like :filtro ) ";
            }
            if(areaId != null){
                jpql += " and s.areaId.id = :areaId ";
            }
            if(anio != null){
                jpql += " and extract(year from s.fechaDoc) = :anio ";
            }
            if(tipoDocId != null){
                jpql += " and s.tipoDoc.id = :tipoDocId ";
            }
            jpql += " order by s.fechaDoc desc";
            
            Query q = em.createQuery(jpql);
            if(filtro != null && !filtro.trim().isEmpty()){
                q.setParameter("filtro", "%"+filtro+"%");
            }
            if(areaId != null){
                q.setParameter("areaId", areaId);
            }
            if(anio != null){
                q.setParameter("anio", anio);
            }
            if(tipoDocId != null){
                q.setParameter("tipoDocId", tipoDocId);
            }
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
