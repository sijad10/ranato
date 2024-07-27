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
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.SbValidacionWebGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.sistemabase.data.SbValidacionWeb;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;

/**
 *
 * @author Renato
 */
@Stateless
public class SbValidacionWebFacadeGt extends AbstractFacade<SbValidacionWebGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbValidacionWebFacadeGt() {
        super(SbValidacionWebGt.class);
    }

    public SbValidacionWeb SbValidacionWebGt(String h) {
        Query q = em.createQuery("select v from SbValidacionWebGt v where v.activo=1 and v.urlValidacion = :hash and v.fechaFin > :ahora");
        q.setParameter("hash", h);
        q.setParameter("ahora", new Date());
        q.setMaxResults(1);
        List<SbValidacionWeb> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }
    
    public SbValidacionWebGt obtenerValidacionPorIdUsuario(Long idUsu) {
        Query q = em.createQuery("select v from SbValidacionWebGt v where v.activo=1 and v.usuarioId.id = :idUsu and v.fechaFin <= :ahora");
        q.setParameter("idUsu", idUsu);
        q.setParameter("ahora", new Date());
        q.setMaxResults(1);
        List<SbValidacionWebGt> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

     public void desactivarValidacionesWeb(SbUsuarioGt u) {
        Query q = em.createQuery("update SbValidacionWebGt v set v.activo = 0 where v.usuarioId = :usuario");
        q.setParameter("usuario", u);
        q.executeUpdate();
    }
    
    public String crearValidacionWebGt(TipoBaseGt tipo, SbUsuarioGt usuario) {
        Date ahora = new Date();
        Date fin = DateUtils.addDays(ahora, 10);
        SbValidacionWebGt vw = new SbValidacionWebGt();
        vw.setId(null);
        vw.setActivo((short) 1);
        vw.setFechaIni(ahora);
        vw.setFechaFin(fin);
        vw.setTipoId(tipo);
        vw.setUsuarioId(usuario);
        vw.setAudLogin(usuario.getNumDoc());
        vw.setAudNumIp(JsfUtil.getIpAddress());
        String hash = JsfUtil.crearHash("10329836dfljkjhnsasQASWED$%&AQWS_VAL_WEB_" + usuario.getId()
                + "_" + ahora.getTime() + "_" + tipo.getCodProg());
        vw.setUrlValidacion(hash);
        create(vw);
        return hash;
    }
}
