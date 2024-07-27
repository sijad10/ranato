/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.TicketArchivo;
import pe.gob.sucamec.bdintegrado.data.Tickets;

/**
 *
 * @author locador463.ogtic
 */

@Stateless
public class TicketArchivoFacade extends AbstractFacade<TicketArchivo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TicketArchivoFacade() {
        super(TicketArchivo.class);
    }

    public List<Tickets> selectLikeTicket(long tid) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TicketObs t WHERE t.ticket.id = :ticket order by t.id desc");
        q.setParameter("ticket", tid);
        return q.setMaxResults(1000).getResultList();

    }
    
    public List<TicketArchivo> selectUsuario(String s) {
        javax.persistence.Query q = em.createNamedQuery("TicketArchivo.findByLogin");
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

    public List<TicketArchivo> selectUsuarioInterno(String s) {
        javax.persistence.Query q = em.createQuery(
                "select u from TicketArchivo u where u.login = :login and u.tipoId.codProg = 'TP_USR_INT' "
        );
        q.setParameter("login", s != null ? s : "");
        return q.getResultList();
    }
    
    public List<TicketArchivo> selectUsuarioXLogin(String s) {
        javax.persistence.Query q = em.createQuery(
                "select u from TicketArchivo u where u.login = :login "
        );
        q.setParameter("login", s != null ? s : "");
        return q.getResultList();
    }

    public TicketArchivo obtenerJefeId(Long userId) {
        TicketArchivo usuarioRes = null;
        javax.persistence.Query q = em.createQuery("select u.jefeId from TicketArchivo u where u.id = :id ");
        q.setParameter("id", userId);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            usuarioRes = (TicketArchivo) q.getResultList().get(0);
            return usuarioRes;
        }else{
            return null;
        }
    }
    
}

