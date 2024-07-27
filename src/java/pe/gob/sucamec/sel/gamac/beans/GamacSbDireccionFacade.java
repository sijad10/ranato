/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package pe.gob.sucamec.sel.gamac.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.gamac.data.GamacSbDireccion;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacSbDireccionFacade extends AbstractFacade<GamacSbDireccion> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacSbDireccionFacade() {
        super(GamacSbDireccion.class);
    }
    
    public List<GamacSbDireccion> listarDirecionPorPersona(GamacSbDireccion persona) {
        Query q = em.createQuery("SELECT D FROM GamacSbDireccion D WHERE D.personaId.id = :parametro AND d.activo = 1 ORDER BY d.id desc");
        q.setParameter("parametro", persona.getId());
        return q.getResultList();
    }

    public List<GamacSbDireccion> listarDirecionPorPersonaS(String id) {
        Query q = em.createQuery("SELECT D FROM GamacSbDireccion D WHERE trim(D.personaId.id) = :parametro AND d.activo = 1 ORDER BY d.id desc");
        q.setParameter("parametro", id);
        return q.getResultList();
    }
    
    public List<ArrayRecord> findUbigeo(String filtro) {
        if (filtro != null) {
            String sql = "SELECT"
                    + " a.ID || ',' || b.ID || ',' || c.ID AS ID,"
                    + " a.NOMBRE || ' / ' || b.NOMBRE || ' / ' || c.NOMBRE AS NOMBRE_UBIGEO"
                    + " FROM BDINTEGRADO.SB_DEPARTAMENTO a"
                    + " LEFT JOIN BDINTEGRADO.SB_PROVINCIA b ON a.ID = b.DEPARTAMENTO_ID"
                    + " LEFT JOIN BDINTEGRADO.SB_DISTRITO c ON b.ID = c.PROVINCIA_ID"
                    + " WHERE"
                    + " (trim(c.NOMBRE) LIKE ?1 OR trim(b.NOMBRE) LIKE ?2)"
                    + " AND (a.ACTIVO=1 AND b.ACTIVO=1 AND c.ACTIVO=1)"
                    + " ORDER BY a.ID, b.ID, c.ID"
                    + "";

            Query q = em.createNativeQuery(sql);
            filtro = filtro.toUpperCase();
            q.setParameter(1, "%" + filtro + "%");
            q.setParameter(2, "%" + filtro + "%");
            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(200).getResultList();
        } else {
            return null;
        }
    }
    
}
