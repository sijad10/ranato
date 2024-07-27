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
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaCatalogoFacade extends AbstractFacade<AmaCatalogo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaCatalogoFacade() {
        super(AmaCatalogo.class);
    }
    public List<AmaCatalogo> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaCatalogo a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
     public List<AmaCatalogo> buscarCatalogoByCodProg(String codProg) {
        if (codProg == null) {
            codProg = "";
        }
        Query q = em.createQuery("select a from AmaCatalogo a where trim(a.codProg) like :codProg");
        q.setParameter("codProg", codProg);
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
     public List<AmaCatalogo> buscarTipoArma() {
        Query q = em.createQuery("select a from AmaCatalogo a where trim(a.codProg) in ('TP_ARMCAR','TP_ARMESC','TP_ARMPIS','TP_ARMREV','TP_ARMCAE')");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public AmaCatalogo obtenerPorCodProg(String codProg) {
        Query q = em.createQuery("select a from AmaCatalogo a where a.codProg = :cod and a.activo = 1");
        q.setParameter("cod", codProg);

        if (q.getResultList().isEmpty()) {
            return null;
        }

        return (AmaCatalogo) q.getResultList().get(0);
    }

    public List<AmaCatalogo> listarTiposArticulo() {
        Query q = em.createQuery("select a from AmaCatalogo a where a.tipoId.codProg = 'TP_ART' and a.activo = 1 order by a.nombre asc");
//        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<AmaCatalogo> listarMarcasXTipoArma(Long tipoArmaId) {
        Query q = em.createQuery("select distinct a.marcaId from AmaModelos a "
                + "where a.tipoArmaId.id = :tipoArmaId "
                + "and a.activo = 1 "
                + "and a.marcaId.codProg = 'TP_MAR' "
                + "and a.marcaId.activo = 1 "
                + "order by a.marcaId.nombre");
        q.setParameter("tipoArmaId", tipoArmaId);
        return q.getResultList();
    }
	
	
            
    
}
