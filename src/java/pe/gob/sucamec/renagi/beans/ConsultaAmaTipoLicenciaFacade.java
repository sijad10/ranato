/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.renagi.data.ConsultaAmaTipoLicencia;

/**
 *
 * @author msalinas
 */
@Stateless
public class ConsultaAmaTipoLicenciaFacade extends AbstractFacade<ConsultaAmaTipoLicencia> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaAmaTipoLicenciaFacade() {
        super(ConsultaAmaTipoLicencia.class);
    }
    public List<ConsultaAmaTipoLicencia> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from ConsultaAmaTipoLicencia a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
}
