/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sistemabase.data.SbLogUsuario;

/**
 *
 * @author Renato
 */
@Stateless
public class SbLogUsuarioFacade extends AbstractFacade<SbLogUsuario> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbLogUsuarioFacade() {
        super(SbLogUsuario.class);
    }

    public List<SbLogUsuario> buscarAntecedentesPenalesLog(String numDoc) {
        javax.persistence.Query q = em.createQuery("SELECT s FROM SbLogUsuario s where (s.datos like '%CONHOMONIMIA%' or s.datos like '%CONOFICIOS%') and s.datos like :numDoc"
        );
        q.setParameter("numDoc", "%" + numDoc + "%");
        return q.getResultList();
    }
}
