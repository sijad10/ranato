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
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;
import pe.gob.sucamec.sistemabase.data.SbPagina;

/**
 *
 * @author Renato
 */
@Stateless
public class SbPaginaFacade extends AbstractFacade<SbPagina> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbPaginaFacade() {
        super(SbPagina.class);
    }

    public List<SbPagina> selectLike(String s, Long sistema) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPagina p where (p.nombre like :nombre or p.url like :url )and (p.sistemaId.id = :sistemaId)");
        q.setParameter("nombre", "%" + (s == null ? "" : s) + "%");
        q.setParameter("url", "%" + (s == null ? "" : s) + "%");
        q.setParameter("sistemaId", sistema);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbPagina> selectPaginasSistema(Long sistema) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPagina p where p.sistemaId.id = :sistemaId and p.activo = 1");
        q.setParameter("sistemaId", sistema);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbPagina> selectCategoria(Long id, Long sistema) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPagina p where p.categoria = 1 and p.id <> :id and p.sistemaId.id = :sistema order by p.nombre");
        q.setParameter("id", id);
        q.setParameter("sistema", sistema);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbPagina> selectCategoriasMenu(Long sistema) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPagina p where p.sistemaId.id = :sistema and p.categoria = 3 and p.menu = 1 and p.activo = 1 order by p.orden, p.nombre");
        q.setParameter("sistema", sistema);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbPagina> selectItemsCategoriasMenu(Long cat) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPagina p where p.menu = 1 and p.activo = 1 and p.categoriaId.id = :cat order by p.orden, p.nombre");
        q.setParameter("cat", cat);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

}
