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
//import pe.gob.sucamec.bdintegrado.data.AmaMunicion;
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaMunicionFacade extends AbstractFacade<GamacAmaMunicion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaMunicionFacade() {
        super(GamacAmaMunicion.class);
    }

    public List<GamacAmaMunicion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from GamacAmaMunicion a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<GamacAmaMunicion> obtenerPorMarcaCalibre(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select distinct(a) from GamacAmaMunicion a where (a.marcaId.nombre like :valor or a.calibrearmaId.nombre like :valor or a.tipoMunicionId.nombre like :valor) and a.activo = 1 ");
        q.setParameter("valor", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<GamacAmaMunicion> obtenerPorCriterios(String tipoBus, String valor) {
        String jpql = "select a from GamacAmaMunicion a where a.activo = 1 ";
        if (tipoBus != null && valor != null && !"".equals(valor)) {
            switch (tipoBus) {
                case "A":
                    jpql += "and a.marcaId.nombre like :valor ";
                    break;
                case "B":
                    jpql += "and a.calibrearmaId.nombre like :valor ";
                    break;
                case "C":
                    jpql += "and a.tipoMunicionId.nombre like :valor ";
                    break;
                default:
                    break;
            }
        }
        Query q = em.createQuery(jpql);
        if (tipoBus != null && valor != null && !"".equals(valor)) {
            q.setParameter("valor", "%" + valor + "%");
        }
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    /**
     * autocomplete munición proc fabricacion
     *
     * @param valor
     * @return
     */
    public List<GamacAmaMunicion> obtenerMuniciones(String valor) {
        Query q = em.createQuery("select a from GamacAmaMunicion a where a.activo = 1 "
                + "and (a.marcaId.nombre like :valor or a.modelo like :valor or a.calibrearmaId.nombre like :valor) ");
        q.setParameter("valor", valor + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    /**
     * autocomplete munición proc fabricacion
     *
     * @param valor
     * @return
     */
    public List<GamacAmaMunicion> obtenerMuniciones(String valor1, String valor2, String valor3) {
        Query q = em.createQuery("select a from GamacAmaMunicion a where a.activo = 1 and (a.marcaId.nombre like :valor1 and  a.modelo like :valor2 and  a.calibrearmaId.nombre like :valor3) ");
        q.setParameter("valor1", valor1 + "%");
        q.setParameter("valor2", valor2 + "%");
        q.setParameter("valor3", valor3 + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

}
