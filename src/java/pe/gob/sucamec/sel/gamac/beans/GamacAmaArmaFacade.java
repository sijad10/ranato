/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.gamac.data.GamacAmaArma;
import pe.gob.sucamec.sel.gamac.data.GamacSbPersona;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacAmaArmaFacade extends AbstractFacade<GamacAmaArma> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacAmaArmaFacade() {
        super(GamacAmaArma.class);
    }
    
    public List<GamacAmaArma> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from GamacAmaArma a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public GamacAmaArma findBySerie(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createNamedQuery("GamacAmaArma.findBySerie", GamacAmaArma.class).setParameter("serie", s);
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        List<GamacAmaArma> list = q.getResultList();
        if(list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }
    
    public List<GamacAmaArma> listaArmasPorSolicitudRecojo(Long idSol){
        List<GamacAmaArma> listRes=null;
        Query q = em.createQuery("select ar from AmaTarjetaPropiedad tp, AmaArma ar where tp.id in "
                + "(select distinct tp.id "
                + "from AmaSolicitudRecojo sr  LEFT JOIN sr.amaTarjetaPropiedadList tp "
                + "where  sr.id = :idSol and sr.activo=1 ) and ar.id = tp.armaId.id ");
        q.setParameter("idSol",idSol);
        q.setMaxResults(MAX_RES);
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            listRes=q.getResultList();
        }
        return listRes;
    }
    
    public List<GamacAmaArma> listarArmasXCriterios(String filtro, Long idCriterio, GamacSbPersona perVendedor) {
        StringBuilder sbQuery = new StringBuilder();
        List<GamacAmaArma> listRes = new ArrayList<>();
        try {
            sbQuery.append("select a.armaId from AmaTarjetaPropiedad a where a.activo = 1 and a.armaId.activo = 1 and a.personaVendedorId.id = :idVend ");
            if (idCriterio == 1L) {
                sbQuery.append("and a.armaId.serie like :filtro ");
            }
            if (idCriterio == 2L) {
                sbQuery.append("and a.armaId.nroRua like :filtro ");
            }
            if (idCriterio == 3L) {
                sbQuery.append("and a.armaId.serie like :filtro or a.armaId.nroRua like :filtro ");
            }

            Query q = em.createQuery(sbQuery.toString());
            q.setParameter("filtro", "%"+filtro+"%");
            q.setParameter("idVend", perVendedor.getId());
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }
    
    public Map buscarDatosEmitidas(Long nroLicencia){
        try {
            String jpql = "select a.estadoId.id as estadoId, a.situacionId.id as situacionId"+
                          " from GamacAmaArma a where a.activo = 1 and a.licenciaDisca = :licencia ";
            
            Query q = em.createQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("licencia", nroLicencia);
            
            if(q.getResultList().isEmpty()){
                return null;
            }else{
                return (Map) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
}
