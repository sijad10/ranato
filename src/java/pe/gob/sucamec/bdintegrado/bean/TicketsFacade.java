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
import pe.gob.sucamec.bdintegrado.data.Tickets;

/**
 *
 * @author Renato
 */
@Stateless
public class TicketsFacade extends AbstractFacade<Tickets> {

    private static int maxResult = 5000;

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TicketsFacade() {
        super(Tickets.class);
    }

    // Antiguo No se usa.
    /*
    public List<Tickets> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select t from Tickets t where t.nombre like :nombre or t.asunto like :asunto "
                + "or t.dni like :dni or trim(t.numero) like :numero order by t.id desc ");
        q.setParameter("nombre", "%" + s + "%");
        q.setParameter("asunto", "%" + s + "%");
        q.setParameter("dni", "%" + s + "%");
        q.setParameter("numero", "%" + s + "%");
        return q.setMaxResults(maxResult).getResultList();

    }
     */
    public long selectCountRel(Long id) {
        javax.persistence.Query q = em.createQuery(
                "select count(t) from Tickets t where (t.ticketRel.id = :ticketRel) "
        );
        q.setParameter("ticketRel", id);
        return (long) q.getSingleResult();
    }

    /**
     * Busqueda de tickets similares al ingresar un nuevo ticket
     *
     * @param dni
     * @return Relación de tickets similares
     */
    public List<Tickets> selectSim(String dni) {
        javax.persistence.Query q = em.createQuery(
                "select t from Tickets t where (t.dni = :dni) order by t.fechaAct desc "
        );
        q.setParameter("dni", dni);
        return q.setMaxResults(maxResult).getResultList();
    }

    /**
     * Busqueda para mostrar tickets relacionados
     *
     * @param id
     * @return La relación de tickets relacionados.
     */
    public List<Tickets> selectRel(Long id) {
        javax.persistence.Query q = em.createQuery(
                "select t from Tickets t where (t.ticketRel.id = :ticketRel) "
        );
        q.setParameter("ticketRel", id);
        return q.setMaxResults(maxResult).getResultList();
    }

    /**
     * Busqueda para relacionar Tickets
     *
     * @param s Texto a buscar
     * @return Lista de tickets
     */
    public List<Tickets> selectRelBusqueda(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select t from Tickets t where (t.nombre like :nombre or t.asunto like :asunto or t.dni like :dni or trim(t.numero) like :numero) "
                + " and ( t.activo = 1 ) "
                + "order by t.id desc "
        );
        q.setParameter("nombre", "%" + s + "%");
        q.setParameter("asunto", "%" + s + "%");
        q.setParameter("dni", "%" + s + "%");
        q.setParameter("numero", "%" + s + "%");
        return q.setMaxResults(maxResult).getResultList();
    }

    /**
     * Busqueda de tickets
     *
     * @param tipob
     * @param s Texto a buscar
     * @param estado
     * @param tipo
     * @param tramite
     * @param area
     * @param ruc
     * @param canal
     * @param activo
     * @param cerrado
     * @return Relación de tickets
     */
    public List<Tickets> selectLike(String tipob, String s, String estado, String tipo, String tramite, String area, String ruc, String usuario, String activo, String cerrado) {
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
            case "SOLICITUD":
                campo = "trim(t.oficioRef)";
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

        javax.persistence.Query q = em.createQuery(
                "select t from Tickets t where ( "+campo+" like :filtro ) "
                + "and (t.estado.nombre like :estado and t.tipo.nombre like :tipo and t.tramite.nombre like :tramite and trim(t.area.id) like :area "
                + "and  (t.ruc like :ruc or t.dni like :usuario) and trim(t.activo) like :activo and trim(t.cerrado) like :cerrado)"
                + "order by t.fechaAct desc "
        );
        q.setParameter("filtro", s);
        q.setParameter("estado", estado);
        q.setParameter("tipo", tipo);
        q.setParameter("tramite", tramite);
        q.setParameter("area", area);
        q.setParameter("usuario", usuario);
        q.setParameter("ruc", ruc);
        //q.setParameter("canal", canal);
        q.setParameter("activo", activo);
        q.setParameter("cerrado", cerrado);

        q.setHint("eclipselink.batch", "t.usuarioActual");
        q.setHint("eclipselink.batch", "t.estado");
        q.setHint("eclipselink.batch", "t.area");
        q.setHint("eclipselink.batch", "t.canal");
        q.setHint("eclipselink.batch", "t.tipo");
        q.setHint("eclipselink.batch", "t.ticketRel");
        q.setHint("eclipselink.batch.type", "IN");
        return q.setMaxResults(maxResult).getResultList();
    }

}
