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
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbReciboRegistroFacade extends AbstractFacade<SbReciboRegistro> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbReciboRegistroFacade() {
        super(SbReciboRegistro.class);
    }
 
    public SbReciboRegistro buscarReciboRegistroByExpediente(String nroExpediente) {
        try {
            String jpql = "select r from SbReciboRegistro r where r.activo = 1 and r.nroExpediente = :nroExpediente";
            javax.persistence.Query q = em.createQuery(jpql);
            q.setParameter("nroExpediente", nroExpediente.trim());
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return (SbReciboRegistro)q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SbReciboRegistro> obtenerRecibosPorExpediente(String nroExp) {
        List<SbReciboRegistro> listRes = null;
        String jpql = "select reg from SbReciboRegistro reg where reg.activo = 1 ";
        if (nroExp != null && !"".equals(nroExp.trim())) {
            jpql += "and  reg.nroExpediente = :nroExp ";
        }
        javax.persistence.Query q = em.createQuery(jpql);
        if (nroExp != null && !"".equals(nroExp.trim())) {
            q.setParameter("nroExp", nroExp.trim());
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
}
