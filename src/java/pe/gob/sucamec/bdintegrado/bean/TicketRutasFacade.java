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
import pe.gob.sucamec.bdintegrado.data.TicketRutas;
import pe.gob.sucamec.bdintegrado.data.Tickets;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author Renato
 */
@Stateless
public class TicketRutasFacade extends AbstractFacade<TicketRutas> {

    private static int maxResult = 5000;

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TicketRutasFacade() {
        super(TicketRutas.class);
    }

    public List<Tickets> selectLikeTicket(long tid) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TicketRutas t WHERE t.ticket.id = :ticket order by t.id desc");
        q.setParameter("ticket", tid);
        return q.setMaxResults(maxResult).getResultList();

    }

    public List<TicketRutas> selectLikeTicketRutasDerivados(String tipob, String s, String estado, String tipo, String tramite, String area, String usuario, String canal, String cerrado, Long login, boolean ultimos) {

        String filtro = "";

        if (tipob == null) {
            tipob = "";
        }
        s = s == null? "%" : "%" + s + "%";
        String campo = "t.asunto";

        switch (tipob) {
            case "NOMBRE":
                campo = "t.nombre";
                break;
            case "ASUNTO":
                campo = "t.asunto";
                break;
            case "DNI":
                campo = "t.dni";
                break;
            case "NUMERO":
                campo = "trim(t.numero)";
                break;
            case "TELEFONO":
                campo = "t.telefono";
                break;
            case "EXPEDIENTE":
                campo = "t.expediente";
                break;
            case "RUC":
                campo = "t.ruc";
                break;
        }
        
        if (ultimos) {
            filtro = " and tr.id = (select max(tr2.id) from TicketRutas tr2 where tr.ticket.id = tr2.ticket.id )";//in (select max(tr2.id) from TicketRutas tr2 where tr.usuario.id = :login2 and tr.ticket.activo = 1 group by tr2.ticket) ";
        }

        javax.persistence.Query q = em.createQuery(
                "select tr from TicketRutas tr left join tr.ticket t where ( "+campo+" like :filtro ) and (tr.ticket.estado.nombre like :estado and tr.ticket.tipo.nombre like :tipo and tr.ticket.tramite.nombre like :tramite and trim(tr.ticket.area.id) like :area and  tr.ticket.usuarioActual.login like :usuario and tr.ticket.canal.nombre like :canal and tr.ticket.activo = 1 and trim(tr.ticket.cerrado) like :cerrado) and tr.usuario.id=:login " + filtro + " order by tr.ticket.fechaAct desc");
        
        q.setParameter("filtro", s);
        q.setParameter("estado", estado == null ? "%" : estado);
        q.setParameter("tipo", tipo == null ? "%" : tipo);
        q.setParameter("tramite", tramite == null ? "%" : tramite);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("usuario", usuario == null ? "%" : usuario);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setParameter("login", login == null ? "%" : login);

        q.setHint("eclipselink.batch", "tr.tipo");
        q.setHint("eclipselink.batch", "tr.ticket");
        q.setHint("eclipselink.batch", "tr.ticket.usuarioActual");
        q.setHint("eclipselink.batch", "tr.usuarioDestino");
        q.setHint("eclipselink.batch", "tr.usuarioDestino.areaId");
        q.setHint("eclipselink.batch", "tr.ticket.estado");
        q.setHint("eclipselink.batch", "tr.ticket.area");
        q.setHint("eclipselink.batch", "tr.ticket.canal");

        q.setHint("eclipselink.batch.type", "IN");

        return q.setMaxResults(maxResult).getResultList();

    }

    public List<SbUsuario> selectLoginAreaTickets(String usr) {
        return selectLoginAreaTickets(usr, false);
    }

    public List<SbUsuario> selectLoginAreaTickets(String usr, boolean orderLogin) {
        String ols = " u.areaId.abreviatura, u.login ";
        if (orderLogin) {
            ols = " u.login ";
        }
        javax.persistence.Query q = getEntityManager().createQuery(
                "select u from SbUsuario u join u.sbPerfilList p where u.login <> :login and p.codProg = 'SB_TICKET' order by " + ols);
        q.setParameter("login", usr);
        q.setHint("eclipselink.batch", "u.sbPerfilList");
        if (!orderLogin) {
            q.setHint("eclipselink.batch", "u.areaId");
        }
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

}
