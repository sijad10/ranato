/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.TicketObs;
import pe.gob.sucamec.bdintegrado.data.Tickets;

/**
 *
 * @author Renato
 */
@Stateless
public class GraficosTicketsFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    private Calendar cal = GregorianCalendar.getInstance();

    protected EntityManager getEntityManager() {
        return em;
    }

    public GraficosTicketsFacade() {
    }

    private String parsearUsuarios(String sa[]) {
        String r = "";
        if ((sa == null) || sa.length == 0) {
            return "";
        }
        for (String s : sa) {
            s = s.replace("'", "''");
            r += ",'" + s + "'";
        }
        r = " and t.usuario.login in ( " + r.substring(1) + ") ";
        return r;
    }

    public List<Map> countTicketsAreas(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.area.abreviatura as area  FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado group by t.area.abreviatura");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> countTicketsTipo(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.tipo.nombre as tipo  FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado group by t.tipo.nombre");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> countTicketsCerrado(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.cerrado as cerrado  FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado group by t.cerrado");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> countTicketsCanal(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.canal.nombre as canal  FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado group by t.canal.nombre");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> countTicketsTramite(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.tramite.nombre as tramite  FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado group by t.tramite.nombre");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> countObsUsuario(Date fechaIni, Date fechaFin, String[] usuarios) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.usuario.login as usuario  FROM TicketObs t where t.ticket.fechaIni >= :fechaIni and t.ticket.fechaIni <= :fechaFin " + parsearUsuarios(usuarios) + " group by t.usuario.login");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }

    public List<Map> countTicketsUsuario(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.usuario.login as usuario  FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado group by t.usuario.login");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.result-type", "Map");

        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> countTicketsDias(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, operator('round', coalesce(t.fechaFin, CURRENT_TIMESTAMP) - t.fechaIni) as dias, t.cerrado as cerrado FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado group by operator('round', coalesce(t.fechaFin, CURRENT_TIMESTAMP) - t.fechaIni), t.cerrado");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> getTicketsPorDia(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, concat(extract(YEAR t.fechaIni), '.', extract(MONTH t.fechaIni), '.', extract(DAY t.fechaIni) ) as dias FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado group by extract(YEAR t.fechaIni), extract(MONTH t.fechaIni), extract(DAY t.fechaIni) order by extract(YEAR t.fechaIni), extract(MONTH t.fechaIni), extract(DAY t.fechaIni)");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<TicketObs> getObsData(Date fechaIni, Date fechaFin, String[] usuarios) {
        Query q;
        q = em.createQuery(
                "SELECT t FROM TicketObs t where t.ticket.fechaIni >= :fechaIni and t.ticket.fechaIni <= :fechaFin " + parsearUsuarios(usuarios) + " ");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<Tickets> getTicketsData(Date fechaIni, Date fechaFin, String area, String canal, String[] usuarios, String cerrado) {
        Query q;
        q = em.createQuery(
                "SELECT t FROM Tickets t where t.area.abreviatura like :area and t.fechaIni >= :fechaIni and t.fechaIni <= :fechaFin and t.canal.nombre like :canal " + parsearUsuarios(usuarios) + " and trim(t.cerrado) like :cerrado");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("area", area == null ? "%" : area);
        q.setParameter("canal", canal == null ? "%" : canal);
        q.setParameter("cerrado", cerrado == null ? "%" : cerrado);
        q.setHint("eclipselink.batch", "t.area");
        q.setHint("eclipselink.batch", "t.usuario");
        q.setHint("eclipselink.batch", "t.usuarioActual");
        q.setHint("eclipselink.batch", "t.canal");
        q.setHint("eclipselink.batch", "t.tipo");
        q.setHint("eclipselink.batch", "t.tramite");
        q.setHint("eclipselink.batch", "t.ticketRel");
        q.setHint("eclipselink.batch", "t.estado");
        q.setHint("eclipselink.batch.type", "IN");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }
}
