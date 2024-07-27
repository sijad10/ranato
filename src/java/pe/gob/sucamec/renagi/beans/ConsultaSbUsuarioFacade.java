/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.renagi.data.ConsultaSbUsuario;

/**
 *
 * @author mespinoza
 */
@Stateless
public class ConsultaSbUsuarioFacade extends AbstractFacade<ConsultaSbUsuario> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaSbUsuarioFacade() {
        super(ConsultaSbUsuario.class);
    }

    public List<ConsultaSbUsuario> selectUsuario(String s) {
        javax.persistence.Query q = em.createNamedQuery("ConsultaSbUsuario.findByLogin");
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

    public List<ConsultaSbUsuario> selectUsuarioInterno(String s) {
        javax.persistence.Query q = em.createQuery(
                "select u from ConsultaSbUsuario u where u.login = :login and u.tipoId.codProg = 'TP_USR_INT' "
        );
        q.setParameter("login", s != null ? s : "");
        return q.getResultList();
    }
    
    public List<ConsultaSbUsuario> selectUsuarioXLogin(String s) {
        javax.persistence.Query q = em.createQuery(
                "select u from ConsultaSbUsuario u where u.login = :login "
        );
        q.setParameter("login", s != null ? s : "");
        return q.getResultList();
    }

    public ConsultaSbUsuario obtenerJefeId(Long userId) {
        ConsultaSbUsuario usuarioRes = null;
        javax.persistence.Query q = em.createQuery("select u.jefeId from ConsultaSbUsuario u where u.id = :id ");
        q.setParameter("id", userId);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            usuarioRes = (ConsultaSbUsuario) q.getResultList().get(0);
            return usuarioRes;
        }else{
            return null;
        }
    }
    
}
