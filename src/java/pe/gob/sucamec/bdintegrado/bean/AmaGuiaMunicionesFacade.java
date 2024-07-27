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
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaMuniciones;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaGuiaMunicionesFacade extends AbstractFacade<AmaGuiaMuniciones> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaGuiaMunicionesFacade() {
        super(AmaGuiaMuniciones.class);
    }

    public List<AmaGuiaMuniciones> obtenerMuniPorIdGuia(Long idGuia) {
        List<AmaGuiaMuniciones> listRes = null;

        Query q = em.createQuery("select a from AmaGuiaMuniciones a where a.activo = 1 and a.guiaTransitoId.id = :idGuia ");
        q.setParameter("idGuia", idGuia);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

}
