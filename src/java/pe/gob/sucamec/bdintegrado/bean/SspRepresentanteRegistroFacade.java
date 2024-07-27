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
import pe.gob.sucamec.bdintegrado.data.SspRepresentanteRegistro;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspRepresentanteRegistroFacade extends AbstractFacade<SspRepresentanteRegistro>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspRepresentanteRegistroFacade() {
        super(SspRepresentanteRegistro.class);
    }
    
    public SspRepresentanteRegistro representRegFindByRegistroId(Long registroID, Long empresaID) {
        Query q = em.createQuery("select r from SspRepresentanteRegistro r where r.registroId.id = :registroID and r.registroId.empresaId.id = :empresaID");
        q.setParameter("registroID", registroID);
        q.setParameter("empresaID", empresaID);
        List<SspRepresentanteRegistro> tb = q.getResultList();
        if (tb.isEmpty()) {
            return null;
        }
        return tb.get(0);
    }
}
