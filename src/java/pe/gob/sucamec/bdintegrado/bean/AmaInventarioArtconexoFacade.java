/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArtconexo;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class AmaInventarioArtconexoFacade extends AbstractFacade<AmaInventarioArtconexo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaInventarioArtconexoFacade() {
        super(AmaInventarioArtconexo.class);
    }

    public List<AmaInventarioArtconexo> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaInventarioArtconexo a where trim(a.id) like :id and a.activo = 1 ");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<AmaInventarioArtconexo> buscarArticuloXDescripcion(String desc) {
        if (desc == null) {
            desc = "";
        }
        Query q = em.createQuery("select a from AmaInventarioArtconexo a where a.activo = 1 and (trim(a.nombre) like :desc or trim(a.descripcion) like :desc) ");
        q.setParameter("desc", "%" + desc + "%");
        return q.getResultList();
    }

    /**
     * autocomplete material relacionados proc Fabricacion
     *
     * @param desc
     * @return
     */
    public List<AmaInventarioArtconexo> autocomAmaInventarioArtconexo(String desc) {
        Query q = em.createQuery("select a from AmaInventarioArtconexo a left join a.marcaId c "
                + "where a.activo = 1 and (a.nombre like :desc or c.nombre like :desc or a.descripcion like :desc)");
        q.setParameter("desc", desc + "%");
        return q.getResultList();
    }

    public List<AmaInventarioArtconexo> cargarKitPistola() {
        Query q = em.createQuery("select a from AmaInventarioArtconexo a where a.id in (5,7,2,10,11,1,4) and a.activo = 1");
        return q.getResultList();
    }

    public List<AmaInventarioArtconexo> cargarKitRevolver() {
        Query q = em.createQuery("select a from AmaInventarioArtconexo a where a.id in (5,2,10,11,4) and a.activo = 1 ");
        return q.getResultList();
    }

    public List<AmaInventarioArtconexo> obtenerArmasAlmacenadas(Long estado, String tipoBus, String filtro) {
        List<AmaInventarioArtconexo> listRes = new ArrayList<>();
        try {
            String jpql = "select ac from AmaInventarioArtconexo ac  where ac.id != null ";
            if (estado != null) {
                jpql += "and ac.activo = :estado ";
            }
            if (tipoBus != null && filtro != null && !"".equals(filtro)) {
                switch (tipoBus) {
                    case "A":
                        jpql += "and ac.nombre like :filtro ";
                        break;
                    case "B":
                        jpql += "and ac.tipoId.nombre like :filtro ";
                        break;
                    default:
                        break;
                }
            }
            jpql += " order by ac.id desc ";
            Query q = em.createQuery(jpql);
            if (estado != null) {
                q.setParameter("estado", estado);
            }
            if (tipoBus != null && filtro != null && !"".equals(filtro)) {
                q.setParameter("filtro", "%" + filtro + "%");
            }
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listRes;
    }
}
