/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author Renato
 */
@Stateless
public class SbUsuarioFacade extends AbstractFacade<SbUsuario> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbUsuarioFacade() {
        super(SbUsuario.class);
    }

    public Long cantidadUsuarios(Long u) {
        javax.persistence.Query q = em.createQuery(
                "select count(u) as c from SbUsuario u where u.usuarioId.id = :usu and u.activo = 1 "
        );
        q.setParameter("usu", u);
        q.setHint("eclipselink.result-type", "Map");
        List<Map> l = q.getResultList();
        return (Long) l.get(0).get("c");
    }

    public boolean validaLogin(String p) {
        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuario u where u.login = :persona"
        );
        q.setParameter("persona", p);
        return !q.getResultList().isEmpty();
    }

    public List<SbUsuario> selectUsuario(String s) {
        javax.persistence.Query q = em.createNamedQuery("SbUsuario.findByLogin");
        q.setParameter("login", s != null ? s : "");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbUsuario> selectLike(String s, Long u) {
        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuario u where (u.login like :login or u.apeMat like :apeMat or u.apePat like :apePat or u.nombres like :nombres) and (u.tipoId.codProg = 'TP_USR_EXT' or u.tipoId.codProg = 'TP_USR_MA') and u.usuarioId.id = :usu "
        );
        s = (s == null || s.equals("")) ? "%" : "%" + s + "%";
        q.setParameter("login", s);
        q.setParameter("apePat", s);
        q.setParameter("apeMat", s);
        q.setParameter("nombres", s);
        q.setParameter("usu", u);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbUsuario> selectUsuarioExterno(String login, String tipoDoc, String numDoc) {
        javax.persistence.Query q;
        if (tipoDoc.equals("RUC")) {
            q = em.createQuery(
                    "select u from SbUsuario u where u.activo = 1 and u.login = :login and u.personaId.ruc like :ruc and (u.tipoId.codProg = 'TP_USR_EXT' or u.tipoId.codProg = 'TP_USR_MA')"
            );
            q.setParameter("ruc", numDoc);
        } else {
            q = em.createQuery(
                    "select u from SbUsuario u where u.activo = 1 and u.login = :login and u.personaId.tipoId.codProg='TP_PER_NAT' and u.personaId.tipoDoc.nombre = :tipoDoc and u.personaId.numDoc = :numDoc and (u.tipoId.codProg = 'TP_USR_EXT' or u.tipoId.codProg = 'TP_USR_MA')"
            );
            q.setParameter("tipoDoc", tipoDoc);
            q.setParameter("numDoc", numDoc);
        }
        q.setParameter("login", login);
        q.setMaxResults(1);
        return q.getResultList();
    }

    public List<SbUsuario> selectUsuarioInterno(String s) {
        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuario u where u.login = :login and u.tipoId.codProg = 'TP_USR_INT' "
        );
        q.setParameter("login", s != null ? s : "");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<SbUsuario> selectUsuariosInternos() {
        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuario u where u.tipoId.codProg = 'TP_USR_INT' order by u.login"
        );
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public SbUsuario selectUsuarioMaestro(Long persona) {
        if (persona == null) {
            return null;
        }
        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuario u where u.personaId.id = :persona and u.tipoId.codProg = 'TP_USR_MA' "
        );
        q.setParameter("persona", persona);
        q.setMaxResults(MAX_RES);
        List<SbUsuario> u = q.getResultList();
        return u.size() == 1 ? u.get(0) : null;
    }

    public List<SbUsuario> findUsuarioByTipoAndCod(int tipUsr, String codUsr) {
        List<SbUsuario> lis;
        try {
            javax.persistence.Query q = getEntityManager().createQuery(
                    "select u from Usuarios u INNER JOIN Tipos t ON u.tipousr.codigo = t.codigo where t.orden = :tipusr and u.nrodoc = :nrodoc");
            q.setParameter("tipusr", tipUsr);
            q.setParameter("nrodoc", codUsr);
            lis = q.getResultList();
            return lis;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public List<SbUsuario> findUsuarioByTipoAndCodAndUsu(int tipUsr, String codUsr, String usuLogin) {
        List<SbUsuario> lis = new ArrayList<>();
        try {
            javax.persistence.Query q = getEntityManager().createQuery(
                    "select u from Usuarios u where u.tipousr.codigo =:tipusr and u.nrodoc =:nrodoc and u.usuario =:usuario and u.tipaut.codigo=20");
            q.setParameter("tipusr", tipUsr);
            q.setParameter("nrodoc", codUsr);
            q.setParameter("usuario", usuLogin);
            lis = q.getResultList();
            return lis;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public SbUsuario findAdminByTipoAndCodAndUsu(int tipUsr, String codUsr, String usuLogin) {
        try {
            javax.persistence.Query q = getEntityManager().createQuery(
                    "select u from Usuarios u where u.tipousr.codigo =:tipusr and u.nrodoc =:nrodoc and u.usuario =:usuario and u.tipaut.codigo=19");
            q.setParameter("tipusr", tipUsr);
            q.setParameter("nrodoc", codUsr);
            q.setParameter("usuario", usuLogin);
            return (SbUsuario) q.getSingleResult();

        } catch (Exception e) {
            //e.printStackTrace();
            return new SbUsuario();
        }
    }

    public Integer obtenerIdUsuarioTramDoc(String p_usuario) {
        try {
            Query q = em.createNativeQuery("SELECT UT.ID_USUARIO FROM TRAMDOC.USUARIO UT WHERE UT.USUARIO = ?1");
            q.setParameter(1, p_usuario);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                BigDecimal bd = (BigDecimal) q.getResultList().get(0);
                return bd.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * OBTENER USUARIO COORDINADOR DE LME DE AREA ESPECIFICA
     *
     * @param areaId
     * @return
     */
    public List<SbUsuario> obtenerCoordinarExplosivoxArea(String areaId) {
        //33,34 = COORDINADOR EXPLOSIVOS PARA LME
        javax.persistence.Query q = em.createQuery(
                " SELECT u FROM SbUsuario u join u.sbPerfilList p WHERE u.activo = 1 AND p.id in (33,34) and trim(u.areaId.id) = :areaId "
        );
        q.setParameter("areaId", areaId);
        return q.getResultList();
    }
    
    public List<SbUsuario> selectUsuarioXLogin(String s) {
        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuario u where u.login = :login and u.activo=1 "
        );
        q.setParameter("login", s != null ? s : "");
        return q.getResultList();
    }

    public SbUsuario selectUsuarioXId(Long s) {
        if (s != null) {
            javax.persistence.Query q = em.createQuery("select u from SbUsuario u where u.id = :id and u.activo=1 ");
            q.setParameter("id", s);
            //q.setParameter("id", s != null ? s : "");
            return (SbUsuario) q.getSingleResult();   
        } else {
            return null;
        }
    }
    
    public SbUsuarioGt obtenerUsuarioXLogin(String login) {
        SbUsuarioGt usuarioRes = new SbUsuarioGt();
        javax.persistence.Query q = em.createQuery("select u from SbUsuarioGt u where u.login = :login ");
        q.setParameter("login", login != null ? login : "");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            usuarioRes = (SbUsuarioGt) q.getResultList().get(0);
        }
        return usuarioRes;
    }
    
    public SbUsuario obtenerJefeId(String login) {
        SbUsuario usuarioRes = null;
        javax.persistence.Query q = em.createQuery("select u.jefeId from SbUsuario u where u.login = :login ");
        q.setParameter("login", login != null ? login : "");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            usuarioRes = (SbUsuario) q.getResultList().get(0);
        }
        return usuarioRes;
    }

}
