/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.Usuario;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbUsuarioFacadeGt extends AbstractFacade<SbUsuarioGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbUsuarioFacadeGt() {
        super(SbUsuarioGt.class);
    }

    public List<SbUsuarioGt> selectUsuario(String s) {
        javax.persistence.Query q = em.createNamedQuery("SbUsuarioGt.findByLogin");
        q.setParameter("login", s != null ? s : "");
        return q.getResultList();
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

    public List<Usuario> obtenerUsuarioTramDoc(String p_usuario) {
        try {
            Query q = em.createQuery("SELECT p FROM Usuario p WHERE p.usuario = :user");
            q.setParameter("user", p_usuario);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return q.getResultList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Usuario obtenerUsuarioTramDoc_x_ID(Long xID) {
        try {
            Query q = em.createQuery("SELECT p FROM Usuario p WHERE p.idUsuario = :xID");
            q.setParameter("xID", xID);          
            return (Usuario) q.getResultList().get(0);          
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Usuario obtenerUsuarioTramDoc_x_Login(String xLogin) {
        try {
            Query q = em.createQuery("SELECT p FROM Usuario p WHERE p.usuario = :xLogin");
            q.setParameter("xLogin", xLogin);          
            return (Usuario) q.getResultList().get(0);          
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String obtenerUsuarioTramDocTrazaActualExpediente(String nroExpediente) {
        String usuario = null;
        Query q = em.createNativeQuery("select u.usuario from tramdoc.traza t " +
                                        " inner join tramdoc.expediente ex on ex.id_expediente = t.id_expediente " +
                                        " inner join tramdoc.usuario_por_traza ut on ut.traza = t.id_traza " +
                                        " inner join tramdoc.usuario u on u.id_usuario = ut.usuario " +
                                        " where ex.numero = ?1 and t.actual = 1 " +
                                        " ORDER BY t.id_traza asc " );
        q.setParameter(1, nroExpediente);
        if(!q.getResultList().isEmpty()){
            usuario = (String) q.getResultList().get(0);        
        }
        
        return usuario;
    }

    public String obtenerResponsableProcesoTramDoc(int idProceso) {
        try {
            Query q = em.createNativeQuery("SELECT U.USUARIO FROM TRAMDOC.RESPONSABLE_USUARIO R, TRAMDOC.USUARIO U WHERE R.ID_USUARIO=U.ID_USUARIO AND ID_PROCESO = ?1");
            q.setParameter(1, idProceso);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                String bd = (String) q.getResultList().get(0);
                return bd;
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
    public List<SbUsuarioGt> obtenerCoordinarExplosivoxArea(String areaId) {
        //33,34 = COORDINADOR EXPLOSIVOS PARA LME
        javax.persistence.Query q = em.createQuery(
                " SELECT u FROM SbUsuarioGt u join u.sbPerfilList p WHERE u.activo = 1 AND p.id in (33,34) and trim(u.areaId.id) = :areaId "
        );
        q.setParameter("areaId", areaId);
        return q.getResultList();
    }

    public SbUsuarioGt selectUsuarioMaestro(Long persona) {
        if (persona == null) {
            return null;
        }
        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuarioGt u where u.personaId.id = :persona and u.tipoId.codProg = 'TP_USR_MA' "
        );
        q.setParameter("persona", persona);
        q.setMaxResults(1);
        List<SbUsuarioGt> u = q.getResultList();
        return u.size() == 1 ? u.get(0) : null;
    }

    public Boolean existeUsuario(String login, String tipoDoc, String numDoc) {
        javax.persistence.Query q;
        Boolean res = Boolean.FALSE;
        if (tipoDoc.equals("RUC")) {
            q = em.createQuery(
                    "select u from SbUsuarioGt u where u.login = :login and u.personaId.tipoId.codProg='TP_PER_JUR' and u.personaId.ruc like :ruc and (u.tipoId.codProg = 'TP_USR_EXT' or u.tipoId.codProg = 'TP_USR_MA')"
            );
            q.setParameter("ruc", numDoc);
        } else {
            q = em.createQuery(
                    "select u from SbUsuarioGt u where u.login = :login and u.personaId.tipoId.codProg='TP_PER_NAT' and u.personaId.tipoDoc.nombre = :tipoDoc and u.personaId.numDoc = :numDoc and (u.tipoId.codProg = 'TP_USR_EXT' or u.tipoId.codProg = 'TP_USR_MA')"
            );
            q.setParameter("tipoDoc", tipoDoc);
            q.setParameter("numDoc", numDoc);
        }
        q.setParameter("login", login);
        q.setMaxResults(1);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = Boolean.TRUE;
        }
        return res;
    }

    public SbUsuarioGt usuInactivoPersonaPorIdPersona(Long idPersona) {
        SbUsuarioGt res = null;
        if (idPersona == null) {
            return null;
        }
        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuarioGt u where u.personaId.id = :id and u.activo = 0 "
        );
        q.setParameter("id", idPersona);
        q.setMaxResults(1);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = (SbUsuarioGt) q.getResultList().get(0);
        }
        return res;
    }

    public SbUsuarioGt buscarUsuarioByLogin(String login) {
        SbUsuarioGt res = null;

        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuarioGt u where u.login = :login "
        );
        q.setParameter("login", login);
        q.setMaxResults(1);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = (SbUsuarioGt) q.getResultList().get(0);
        }
        return res;
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
    
    public SbUsuarioGt obtenerUsuarioXCorreo(String correo) {
        SbUsuarioGt usuarioRes = null;
        javax.persistence.Query q = em.createQuery("select u from SbUsuarioGt u where u.correo = :correo and u.personaId.id != 1 ");
        q.setParameter("correo", correo != null ? correo.trim().toUpperCase() : "");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            usuarioRes = (SbUsuarioGt) q.getResultList().get(0);
        }
        return usuarioRes;
    }
    
    public SbUsuarioGt obtenerUsuarioXDocActivo(String numDoc) {
        SbUsuarioGt usuarioRes = null;
        javax.persistence.Query q = em.createQuery("select u from SbUsuarioGt u where u.numDoc = :numDoc and u.personaId.id != 1 and u.activo=1");
        q.setParameter("numDoc", numDoc != null ? numDoc.trim().toUpperCase() : "");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            usuarioRes = (SbUsuarioGt) q.getResultList().get(0);
        }
        return usuarioRes;
    }

    public Integer obtenerIdTramdocUsuarioGerente() {
        Integer idUsuario = null;
        javax.persistence.Query q = em.createNativeQuery("SELECT UT.ID_USUARIO FROM BDINTEGRADO.SB_USUARIO UI "
                + " INNER JOIN TRAMDOC.USUARIO UT ON UI.LOGIN = UT.USUARIO "
                + " INNER JOIN BDINTEGRADO.SB_PERFIL_USUARIO PU ON PU.USUARIO_ID = UI.ID "
                + " INNER JOIN BDINTEGRADO.SB_PERFIL P ON P.ID = PU.PERFIL_ID "
                + " WHERE UI.ACTIVO = 1 "
                + " AND P.COD_PROG = 'EXP_GER' "
                + " ORDER BY UT.FECHA_CREACION DESC ");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            BigDecimal bd = (BigDecimal) q.getResultList().get(0);
            idUsuario = bd.intValue();
        }
        return idUsuario;
    }

    public SbUsuarioGt buscarUsuarioExonerado(String ruc) {

        javax.persistence.Query q = em.createQuery(
                "select u from SbUsuarioGt u where u.personaId.ruc = :ruc and u.tipoId.codProg = 'TP_USR_MA' "
        );
        q.setParameter("ruc", ruc);
        q.setMaxResults(1);
        List<SbUsuarioGt> u = q.getResultList();
        return u.size() == 1 ? u.get(0) : null;
    }
    
    public Integer buscarPerfilInstructorUsuario(String numDoc) {
        try {
            Query q = em.createQuery("select count(p) from SbUsuarioGt e " +
                                    " left join e.sbPerfilList p " +
                                    " where e.activo = 1 and p.activo = 1 and e.personaId.numDoc = :numDoc " +
                                    " and p.codProg = 'SEL_INST' ");
            q.setParameter("numDoc", numDoc);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                Long bd = (Long) q.getResultList().get(0);
                return bd.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public SbUsuarioGt obtenerUsuarioxPerfil(String perfilCodProg) {
        //33,34 = COORDINADOR EXPLOSIVOS PARA LME
        javax.persistence.Query q = em.createQuery(
                " SELECT u FROM SbUsuarioGt u join u.sbPerfilList p WHERE u.activo = 1 AND p.codProg = :perfilCodProg "
        );
        q.setParameter("perfilCodProg", perfilCodProg);
        if(!q.getResultList().isEmpty()){
            return (SbUsuarioGt) q.getResultList().get(0);
        }
        
        return null;
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

}
