/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.citas.data.CitaTurConstancia;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaTurConstanciaFacade extends AbstractFacade<CitaTurConstancia> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTurConstanciaFacade() {
        super(CitaTurConstancia.class);
    }

    public List<CitaTurConstancia> listarConstanciasXCodVerifica(String codVerfica) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurConstancia t"
                + " where t.codVerifica = :codVerfica"
                + " and t.activo = 1"
        );
        q.setParameter("codVerfica", codVerfica);
        return q.getResultList();
    }

    public CitaTurConstancia buscarTurConstanciaXId(Long id) {
        CitaTurConstancia respuesta = new CitaTurConstancia();
        String sentencia = "select t from CitaTurConstancia t where t.id = :id";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("id", id);
        respuesta = (CitaTurConstancia) query.getSingleResult();
        return respuesta;
    }

}
