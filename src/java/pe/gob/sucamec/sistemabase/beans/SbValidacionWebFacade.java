/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.time.DateUtils;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.data.SbValidacionWeb;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;

/**
 *
 * @author Renato
 */
@Stateless
public class SbValidacionWebFacade extends AbstractFacade<SbValidacionWeb> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbValidacionWebFacade() {
        super(SbValidacionWeb.class);
    }

    public SbValidacionWeb buscarPorHash(String h) {
        List<SbValidacionWeb> l = null;
        try {
            Query q = em.createQuery("select v from SbValidacionWeb v where v.activo=1 and v.urlValidacion = :hash and v.fechaFin > :ahora");
            q.setParameter("hash", h);
            q.setParameter("ahora", new Date());
            q.setMaxResults(1);
            l = q.getResultList();
            if (l.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l.get(0);
    }

    public void desactivarValidacionesWeb(SbUsuario u) {
        Query q = em.createQuery("update SbValidacionWeb v set v.activo = 0 where v.usuarioId = :usuario");
        q.setParameter("usuario", u);
        q.executeUpdate();
    }

    public void desactivarValidacionesWeb(SbUsuario u, Long id) {
        Query q = em.createQuery("update SbValidacionWeb v set v.activo = 0 where v.usuarioId = :usuario and v.id<>:id");
        q.setParameter("usuario", u);
        q.setParameter("id", id);
        q.executeUpdate();
    }
    
    public String crearValidacionWeb(SbTipo tipo, SbUsuario usuario) {
        Date ahora = new Date();
        Date fin = DateUtils.addDays(ahora, 10);
        SbValidacionWeb vw = new SbValidacionWeb();
        vw.setActivo((short) 1);
        vw.setFechaIni(ahora);
        vw.setFechaFin(fin);
        vw.setTipoId(tipo);
        vw.setUsuarioId(usuario);
        String hash = JsfUtil.crearHash("10329836dfljkjhnsasQASWED$%&AQWS_VAL_WEB_" + usuario.getId()
                + "_" + ahora.getTime() + "_" + tipo.getCodProg());
        vw.setUrlValidacion(hash);
        create(vw);
        return hash;
    }

    public SbValidacionWeb buscarPorHashValido(String h) {
        List<SbValidacionWeb> l = null;
        try {
            Query q = em.createQuery("select v from SbValidacionWeb v where v.urlValidacion = :hash");
            q.setParameter("hash", h);
            //q.setParameter("ahora", new Date());
            q.setMaxResults(1);
            l = q.getResultList();
            if (l.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l.get(0);
    }


}
