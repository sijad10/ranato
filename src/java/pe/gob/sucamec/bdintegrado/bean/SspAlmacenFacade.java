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
import pe.gob.sucamec.bdintegrado.data.SspAlmacen;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspAlmacenFacade extends AbstractFacade<SspAlmacen>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspAlmacenFacade() {
        super(SspAlmacen.class);
    }
    
    
    public List<SspAlmacen> listarAlmacenxIdRegistro(Long sspRegistroId) {
        List<SspAlmacen> respuesta = new ArrayList();
        String sentencia = "select s from SspAlmacen s where s.activo = 1 and s.sspRegistroId.id = :sspRegistroId ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("sspRegistroId", sspRegistroId);
        respuesta = query.getResultList(); 
        return respuesta;
    }
}
