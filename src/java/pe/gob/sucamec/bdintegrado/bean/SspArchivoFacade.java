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
import pe.gob.sucamec.bdintegrado.data.SspArchivo;
import pe.gob.sucamec.bdintegrado.data.SspRequisito;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspArchivoFacade extends AbstractFacade<SspArchivo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspArchivoFacade() {
        super(SspArchivo.class);
    }
    
    public SspArchivo buscarSspArchivo_By_RequisitoId(SspRequisito requisitoId) {
        String jpql = "select s "                      
                      + " from SspArchivo s "
                      + " where s.requisitoId = :requisitoId ";        
        
        javax.persistence.Query q = em.createQuery(jpql);
        q.setParameter("requisitoId", requisitoId );    
        if(!q.getResultList().isEmpty()){
            return (SspArchivo) q.getResultList().get(0);
        }
        return null;
    }
    
    
    public List<SspArchivo> listarArchivos_By_RegistroId_By_Requisitoid(Long registroId, String tipoRequisito) {
        javax.persistence.Query q = em.createQuery(
                "select a from SspArchivo a where a.activo = 1 and a.requisitoId.registroId.id = :registroId and a.requisitoId.tipoRequisitoId.codProg = :tipoRequisito");
        q.setParameter("registroId", registroId);
        q.setParameter("tipoRequisito", tipoRequisito);
        return q.getResultList();
    }
}
