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
import pe.gob.sucamec.bdintegrado.data.SspRequisito;
import pe.gob.sucamec.bdintegrado.data.SspArchivo;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspRequisitoFacade extends AbstractFacade<SspRequisito> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspRequisitoFacade() {
        super(SspRequisito.class);
    }
    
    
    public SspArchivo buscarRequisitoArchivoPorRegistroAutoriza(Long registroId, String tipoRequisito) {
        Query q = em.createQuery(
                "select a from SspArchivo a where a.activo = 1 and a.requisitoId.registroId.id = :registroId and a.requisitoId.tipoRequisitoId.codProg = :tipoRequisito order by a.requisitoId.id desc");
        q.setParameter("registroId", registroId);
        q.setParameter("tipoRequisito", tipoRequisito);
        if (q.setMaxResults(1).getResultList().isEmpty()) {
            return null;
        }
        return (SspArchivo) q.getResultList().get(0);
    }
    
    public SspRequisito buscarSspRequisito_By_RegistroId_TipoRequisitoId(SspRegistro registroId, TipoSeguridad tipoRequisitoId) {
        String jpql = "select s "                      
                      + " from SspRequisito s "
                      + " where s.activo = 1 and s.registroId = :registroId "
                      + " and s.tipoRequisitoId = :tipoRequisitoId ";        
        
        javax.persistence.Query q = em.createQuery(jpql);
        q.setParameter("registroId", registroId );        
        q.setParameter("tipoRequisitoId", tipoRequisitoId );      
        if(!q.getResultList().isEmpty()){
            return (SspRequisito) q.getResultList().get(0);
        }
        return null;
    }
    
    public List<SspRequisito> listarSspRequisito_By_RegistroId_TipoRequisitoId(Long registroId, String tipoRequisito) {
        javax.persistence.Query q = em.createQuery("select r from SspRequisito r where r.activo = 1 and r.registroId.id = :registroId and r.tipoRequisitoId.codProg = :tipoRequisito");
        q.setParameter("registroId", registroId);
        q.setParameter("tipoRequisito", tipoRequisito);
        return q.getResultList();
    }
}
