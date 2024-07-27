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
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author Renato
 */
@Stateless
public class GraficosNumeradorFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    private Calendar cal = GregorianCalendar.getInstance();

    protected EntityManager getEntityManager() {
        return em;
    }

    public GraficosNumeradorFacade() {
    }

    public Date maxDocumentosDate() {
        Date d = new Date();
        Query q = em.createQuery("select max(d.fechaIngreso) from Documentos as d");
        List<Date> l = q.getResultList();
        if (!l.isEmpty()) {
            d = l.get(0);
        }
        return d;
    }

    public void setCalendar() {
        cal.setTime(maxDocumentosDate());
    }

    public List<Map> countDocumentosAreas() {
        Calendar fecha = cal;
        fecha.add(Calendar.DAY_OF_MONTH, -30);
        Query q;
        q = em.createQuery(
                "SELECT Count(d) as c, d.area.abreviatura as area  FROM Documentos d where d.fecha > :fecha group by d.area.abreviatura");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> countTicketsAreas() {
        Calendar fecha = cal;
        fecha.add(Calendar.DAY_OF_MONTH, -30);
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.area.abreviatura as area  FROM Tickets t group by t.area.abreviatura");
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

        public List<Map> countTicketsTipo() {
        Calendar fecha = cal;
        fecha.add(Calendar.DAY_OF_MONTH, -30);
        Query q;
        q = em.createQuery(
                "SELECT Count(t) as c, t.tipo.nombre as tipo  FROM Tickets t group by t.tipo.nombre");
        q.setHint("eclipselink.result-type", "Map");
        //q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }
        
    public List<Map> countDocumentosArchivoArea() {
        Query q;
        q = em.createQuery(
                "SELECT Count(d) as c, d.area.abreviatura as area  FROM Documentos d where d.copiaArchivo = 0 group by d.area.abreviatura");
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }

    public List<Map> countDocumentosArchivo() {
        Query q;
        q = em.createQuery(
                "SELECT Count(d) as c, d.area.abreviatura as area FROM Documentos d group by d.area.abreviatura");
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }

    public List<Map> countTiposDocumentos() {
        Query q;
        q = em.createQuery(
                "SELECT Count(d) as c, d.tipo.nombre as tipo FROM Documentos d group by d.tipo.nombre");
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }

    public List<TipoBase> selectAreasGraficoDocumentos() {
        Calendar fecha = cal;
        fecha.add(Calendar.DAY_OF_MONTH, -15);
        Query q;
        q = em.createQuery(
                "SELECT DISTINCT d.area from Documentos d where d.fecha > :fecha");
        q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }

    public List<Map> countDocumentosGraficoDocumentos() {
        Calendar fecha = cal;
        fecha.add(Calendar.DAY_OF_MONTH, -15);
        Query q;
        // Usar 'AS' con fechas no funciona bien //
        q = em.createQuery(
                "SELECT Count(d) as c, d.fecha, d.area.id FROM Documentos d where d.fecha > :fecha group by d.fecha, d.area.id order by d.fecha");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("fecha", fecha.getTime());
        return q.getResultList();
    }
}
