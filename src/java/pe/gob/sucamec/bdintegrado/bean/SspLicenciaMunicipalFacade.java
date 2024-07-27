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
import pe.gob.sucamec.bdintegrado.data.SspLicenciaMunicipal;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspLicenciaMunicipalFacade extends AbstractFacade<SspLicenciaMunicipal>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspLicenciaMunicipalFacade() {
        super(SspLicenciaMunicipal.class);
    }
    
    public SspLicenciaMunicipal verUltimaLicenciaMunicipalXIdLocal(Long localId) {
        List<SspLicenciaMunicipal> respuesta = new ArrayList();
        String sentencia = "select s from SspLicenciaMunicipal s where s.activo = 1 and s.localId.id = :localId order by s.id desc";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("localId", localId);
        
        if (query.getResultList().isEmpty()) {
            return null;            
        }else{
            return (SspLicenciaMunicipal) query.getResultList().get(0);
        }
    }
}
