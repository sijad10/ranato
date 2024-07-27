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
import pe.gob.sucamec.bdintegrado.data.AmaDocumento;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class AmaDocumentoFacade extends AbstractFacade<AmaDocumento> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaDocumentoFacade() {
        super(AmaDocumento.class);
    }
    public List<AmaDocumento> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaDocumento a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public AmaDocumento buscarAmaDocumentoById(Long id) {
        Query q = em.createQuery("select a from AmaDocumento a where a.id = :id");
        q.setParameter("id", id);
        
        if(q.getResultList().isEmpty()){
            return null;
        }
        
        return (AmaDocumento) q.getSingleResult();
    }
    
    public AmaDocumento buscarAmaDocumentoByNroExpediente(String nroExpediente) {
        Query q = em.createQuery("select a from AmaDocumento a where a.nroExpediente = :nroExpediente");
        q.setParameter("nroExpediente", nroExpediente);
        
        if(q.getResultList().isEmpty()){
            return null;
        }
        
        return (AmaDocumento) q.getSingleResult();
    }
    
    public List<AmaDocumento> listDocumentosPorNumero(String valor) {
        List<AmaDocumento> listRes = null;
        Query q = em.createQuery(" SELECT d FROM AmaDocumento d where d.numero like :valor AND d.activo = 1 "
                + "order by d.id desc");
        q.setParameter("valor", "%" + valor.toUpperCase() + "%");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
}
