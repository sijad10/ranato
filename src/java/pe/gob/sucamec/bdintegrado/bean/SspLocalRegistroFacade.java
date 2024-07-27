/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspLocalRegistro;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspLocalRegistroFacade extends AbstractFacade<SspLocalRegistro>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspLocalRegistroFacade() {
        super(SspLocalRegistro.class);
    }
    
    public SspLocalRegistro verLocalRegistroXId(Long idLocReg) {
        String sentencia = "select s from SspLocalRegistro s where s.activo = 1 and s.id = :idLocReg ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idLocReg", idLocReg);
        if(query.getResultList().isEmpty()){
            return null;
        }else{
            return (SspLocalRegistro) query.getSingleResult();
        }
    }
    
    public SspLocalRegistro verLocalRegistroXIdSspReg(Long idSspReg) {
        String sentencia = "select s from SspLocalRegistro s where s.activo = 1 and s.registroId.id = :idSspReg ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idSspReg", idSspReg);
        if(query.getResultList().isEmpty()){
            return null;
        }else{
            return (SspLocalRegistro) query.getSingleResult();
        }
    }
    
}
