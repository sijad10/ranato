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
import pe.gob.sucamec.bdintegrado.data.EppPlantaExplosivo;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;


/**
 *
 * @author mespinoza
 */
@Stateless
public class EppPlantaExplosivoFacade extends AbstractFacade<EppPlantaExplosivo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppPlantaExplosivoFacade() {
        super(EppPlantaExplosivo.class);
    }

    public List<EppPlantaExplosivo> listPlantaExplosivo(EppRegistro r) {
        Query q = em.createQuery("SELECT D FROM EppPlantaExplosivo D where d.activo = 1 AND D.registroId = :r");
        q.setParameter("r", r);
        return q.getResultList();
    }

}
