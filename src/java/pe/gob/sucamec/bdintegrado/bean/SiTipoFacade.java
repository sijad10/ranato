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
import pe.gob.sucamec.bdintegrado.data.SiTipo;

/**
 *
 * @author Renato
 */
@Stateless
public class SiTipoFacade extends AbstractFacade<SiTipo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SiTipoFacade() {
        super(SiTipo.class);
    }

    public List<SiTipo> findByTkEst() {
        Query q = em.createNamedQuery("SiTipo.findByTkEst");
        return q.getResultList();
    }

    public List<SiTipo> findByTipo(String tipo) {
        Query q = em.createNamedQuery("SiTipo.findByTipo");
        q.setParameter("tipo", tipo);
        return q.getResultList();
    }

    public List<SiTipo> findByCodProg(String codProg) {
        Query q = em.createNamedQuery("SiTipo.findByCodProg");
        q.setParameter("codProg", codProg);
        return q.getResultList();
    }

    public List<SiTipo> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select t from SiTipo t where t.nombre like :nombre");
        q.setParameter("nombre", "%" + s + "%");
        return q.getResultList();

    }
    
    public SiTipo tipoBaseXCodProg(String codprog) {
        SiTipo tipo = null;
        String sentencia = "select t from SiTipo t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (SiTipo) query.getSingleResult();
        return tipo;
    }
}
