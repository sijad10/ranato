/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SspRepresentantePublico;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspRepresentantePublicoFacade extends AbstractFacade<SspRepresentantePublico> {
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SspRepresentantePublicoFacade(){
        super(SspRepresentantePublico.class);
    }
    
    
    public SspRepresentantePublico findRepresentatePublicoXIdRepPersona(Long idRelacionPersona) {
        Query q = em.createQuery("select s from SspRepresentantePublico s where s.representanteId.id = :idRelacionPersona");
        q.setParameter("idRelacionPersona", idRelacionPersona);
        if(q.getResultList().isEmpty()){
            return null;
        }
        return (SspRepresentantePublico) q.getResultList().get(0);
    }
}
