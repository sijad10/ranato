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
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import pe.gob.sucamec.bdintegrado.data.SbEmpresaExtranjera;
import pe.gob.sucamec.bdintegrado.data.SbPaisGt;

/**
 *
 * @author lbartolo
 */

@Stateless
public class SbEmpresaExtranjeraFacade extends AbstractFacade<SbEmpresaExtranjera>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbEmpresaExtranjeraFacade() {
        super(SbEmpresaExtranjera.class);
    }
    
    
    public List<SbPaisGt> findAllPaises() { 
        TypedQuery<SbPaisGt> queryLista =
                em.createNamedQuery("SbPaisGt.findAll", SbPaisGt.class);
        List<SbPaisGt> paises = queryLista.getResultList(); 
        return paises;
        
    }
    
    public SbEmpresaExtranjera selectEmpresaExtActivaxTipoDocyNumDoc(Long tipoDoc, String doc) {
        try {
            javax.persistence.Query q = em.createQuery(
                    "select p from SbEmpresaExtranjera p where p.activo = 1 and p.tipoDoc.id = :tipoDoc and p.numDoc = :doc "
            );
            q.setParameter("tipoDoc", tipoDoc);
            q.setParameter("doc", doc);

            if(!q.getResultList().isEmpty()){
                return (SbEmpresaExtranjera) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
