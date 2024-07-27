/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.TicketObs;
import pe.gob.sucamec.bdintegrado.data.Tickets;

/**
 *
 * @author Renato
 */
@Stateless
public class TicketObsFacade extends AbstractFacade<TicketObs> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TicketObsFacade() {
        super(TicketObs.class);
    }

    public List<Tickets> selectLikeTicket(long tid) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TicketObs t WHERE t.ticket.id = :ticket order by t.id desc");
        q.setParameter("ticket", tid);
        return q.setMaxResults(1000).getResultList();

    }
    public List<TicketObs> getTicketObsSel(long tid,String codProg) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TicketObs t WHERE t.ticket.id = :ticket and t.tipo.codProg=:codProg and t.activo=1 order by t.id desc");
        q.setParameter("ticket", tid);
        q.setParameter("codProg", codProg);
        return q.setMaxResults(1000).getResultList();

    }

}
