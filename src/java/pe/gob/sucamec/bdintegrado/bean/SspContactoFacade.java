/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspContacto;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspContactoFacade extends AbstractFacade<SspContacto>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SspContactoFacade(){
        super(SspContacto.class);
    }
    
    
    public List<SspContacto> listarContactoxIdRegistro(Long registroId) {
        List<SspContacto> respuesta = new ArrayList();
        String sentencia = "select s from SspContacto s where s.activo = 1 and s.registroId.id = :registroId ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("registroId", registroId);
        respuesta = query.getResultList(); 
        return respuesta;
    }
    
    public void anularContactosByIdRegistro(Long registroId) {
        
        String sentencia = "UPDATE BDINTEGRADO.SSP_CONTACTO SET ACTIVO = 0 WHERE REGISTRO_ID = ?1 ";
        Query query = getEntityManager().createNativeQuery(sentencia);
        query.setParameter(1, registroId);
        query.executeUpdate();
    }
}
