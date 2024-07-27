/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.Usuario;

/**
 *
 * @author gchavez
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public List<BigDecimal> obtenerIdUsuarioTD(String login){
        List<BigDecimal> listaId=new ArrayList<>();
        try{
            Query q= em.createQuery("select u.idUsuario from Usuario u where u.usuario = ?1"); 
            q.setParameter(1, login);
            if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
                listaId=q.getResultList();
            }
        } catch (Exception ex) {
            System.out.println("Error:Usuario.getExpediente:" + ex.getMessage());
        }
        
        return listaId; 
    }
    
    public Usuario obtenerUsuarioTD(String login){
        
        try{
            Query q= em.createQuery("select u from Usuario u where u.usuario = ?1"); 
            q.setParameter(1, login);
            if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
                return (Usuario) q.getResultList().get(0);
            }
        } catch (Exception ex) {
            System.out.println("Error:Usuario.getExpediente:" + ex.getMessage());
        }
        
        return null; 
    }
    
}
