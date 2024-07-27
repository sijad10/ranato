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
import pe.gob.sucamec.sistemabase.data.SbRegla;

/**
 *
 * @author Renato
 */
@Stateless
public class SbReglaFacade extends AbstractFacade<SbRegla> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SbRegla> selectLike(String s, String perfil, Long pagina) {
        javax.persistence.Query q = em.createQuery(
                "select r from SbRegla r where (r.nombre like :nombre or r.tabla like :tabla) and (r.perfilId.nombre like :perfil) and r.paginaId.id = :paginaId order by r.orden");
        q.setParameter("nombre", "%" + (s == null ? "" : s) + "%");
        q.setParameter("tabla", "%" + (s == null ? "" : s) + "%");
        q.setParameter("perfil", "%" + (perfil == null ? "" : perfil) + "%");
        q.setParameter("paginaId", pagina);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbRegla> selectReglasPagina(Long pagina, String perfiles) {
        // Pendiente con perfiles null = se aplican todas las reglas //
        javax.persistence.Query q = em.createQuery(
                "select r from SbRegla r where r.paginaId.id = :paginaId and (r.perfilId.codProg in (" + perfiles + ")) and r.activo = 1 order by r.orden");
        q.setParameter("paginaId", pagina);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public SbReglaFacade() {
        super(SbRegla.class);
    }

}
