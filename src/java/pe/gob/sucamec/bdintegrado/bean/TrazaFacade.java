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
import pe.gob.sucamec.bdintegrado.data.Traza;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class TrazaFacade extends AbstractFacade<Traza> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrazaFacade() {
        super(Traza.class);
    }

    public List<String> obtenerObsTrazasPorExp(BigDecimal idExpediente) {
        List<String> listaObs = new ArrayList<>();
        try {
            javax.persistence.Query q = em.createQuery("select t.observacion from Traza t where t.idExpediente.idExpediente = :idExp");
            q.setParameter("idExp", idExpediente);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listaObs = q.getResultList();
            }
        } catch (Exception ex) {
            System.out.println("Error:Usuario.getExpediente:" + ex.getMessage());
        }

        return listaObs;
    }    
}
