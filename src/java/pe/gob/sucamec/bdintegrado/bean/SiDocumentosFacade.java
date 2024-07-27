/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SiDocumentos;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

/**
 *
 * @author Renato
 */
@Stateless
public class SiDocumentosFacade extends AbstractFacade<SiDocumentos> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SiDocumentosFacade() {
        super(SiDocumentos.class);
    }

    public List<SiDocumentos> selectLike(int an, String s, String ar) {
        String qAnho = "";
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = null;
        if (an > 0) {
            qAnho = " and d.anho = :anho ";
        }
        if (ar == null) {
            q = getEntityManager().createQuery(
                    "select d from SiDocumentos d where (d.descripcion like :desc or TRIM(d.numero) like :numero) " + qAnho + " ORDER BY d.id DESC");
            q.setParameter("desc", "%" + s + "%");
            if (an > 0) {
                q.setParameter("anho", an);
            }
            q.setParameter("numero", "%" + s + "%");
        } else {
            q = getEntityManager().createQuery(
                    "select d from SiDocumentos d where (d.descripcion like :desc or TRIM(d.numero) like :numero) and trim(d.area.id)=:area " + qAnho + " ORDER BY d.id DESC");
            q.setParameter("desc", "%" + s + "%");
            q.setParameter("numero", "%" + s + "%");
            if (an > 0) {
                q.setParameter("anho", an);
            }
            q.setParameter("area", ar);
        }
        return q.setMaxResults(5500).getResultList();
    }

    public List<SiDocumentos> selectLikeMios(int a, String s) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        LoginController lc = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{loginController}", LoginController.class);

        if (s == null) {
            s = "";
        }
        javax.persistence.Query q;
        if (a > 0) {
            q = getEntityManager().createQuery(
                    "select d from SiDocumentos d where (d.descripcion like :desc or TRIM(d.numero) like :numero) and d.usuario = :usuario and d.anho = :anho ORDER BY d.id DESC");
            q.setParameter("anho", a);
        } else {
            q = getEntityManager().createQuery(
                    "select d from SiDocumentos d where (d.descripcion like :desc or TRIM(d.numero) like :numero) and d.usuario = :usuario ORDER BY d.id DESC");
        }
        q.setParameter("desc", "%" + s + "%");
        q.setParameter("numero", "%" + s + "%");
        q.setParameter("usuario", new SbUsuario((Long) lc.getUsuario().getId()));
        return q.setMaxResults(500).getResultList();

    }

    public List<SiDocumentos> selectLikeArchivar(int an, String s, String ar) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = null;
        if (ar == null) {
            q = getEntityManager().createQuery(
                    "select d from SiDocumentos d where (d.descripcion like :desc or TRIM(d.numero) like :numero) and d.copiaArchivo = 0 and d.anho = :anho ORDER BY d.id DESC");
            q.setParameter("desc", "%" + s + "%");
            q.setParameter("numero", "%" + s + "%");
            q.setParameter("anho", an);
        } else {
            q = getEntityManager().createQuery(
                    "select d from SiDocumentos d where (d.descripcion like :desc or TRIM(d.numero) like :numero) and d.copiaArchivo = 0 and d.area.id=:area and d.anho = :anho ORDER BY d.id DESC");
            q.setParameter("desc", "%" + s + "%");
            q.setParameter("numero", "%" + s + "%");
            q.setParameter("area", ar);
            q.setParameter("anho", an);
        }
        return q.setMaxResults(500).getResultList();

    }
}
