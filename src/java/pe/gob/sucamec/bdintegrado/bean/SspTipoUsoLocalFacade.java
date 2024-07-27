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
import pe.gob.sucamec.bdintegrado.data.SspTipoUsoLocal;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspTipoUsoLocalFacade extends AbstractFacade<SspTipoUsoLocal>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspTipoUsoLocalFacade() {
        super(SspTipoUsoLocal.class);
    }
    
    public SspTipoUsoLocal verTipoUsoLocalXId(Long idUsoLoc) {
        String sentencia = "select s from SspTipoUsoLocal s where s.activo = 1 and s.id = :idUsoLoc ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idUsoLoc", idUsoLoc);
        if(query.getResultList().isEmpty()){
            return null;
        }else{
            return (SspTipoUsoLocal) query.getSingleResult();
        }
    }
    
    public SspTipoUsoLocal verTipoUsoLocalXIdSspReg(Long idSspReg) {
        String sentencia = "select s from SspTipoUsoLocal s where s.activo = 1 and s.registroId.id = :idSspReg ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idSspReg", idSspReg);
        if(query.getResultList().isEmpty()){
            return null;
        }else{
            return (SspTipoUsoLocal) query.getSingleResult();
        }
    }
    
}
