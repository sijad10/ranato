/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaTarjetaPropiedadFacade extends AbstractFacade<AmaTarjetaPropiedad> {
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public AmaTarjetaPropiedadFacade() {
        super(AmaTarjetaPropiedad.class);
    }
    
    public List<AmaTarjetaPropiedad> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaTarjetaPropiedad a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public Boolean validarArmaEntregada(Long idTarjet) {
        Boolean r = Boolean.FALSE;
        Query q = em.createQuery("select a from AmaTarjetaPropiedad a where a.id = :id and a.fechaEntregaPropietario is not null ");
        q.setParameter("id", idTarjet);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            r = Boolean.TRUE;
        }
        return r;
    }
    
    public Date obtenerFechaEmision(Long idTarjPro) {
        Date fecha = null;
        Query q = em.createQuery("select a.fechaEmision from AmaTarjetaPropiedad a where a.id = :id and a.fechaEmision is not null and a.activo=1 ");
        q.setParameter("id", idTarjPro);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            fecha = (Date) q.getResultList().get(0);
        }
        return fecha;
    }
    
    public Date obtenerFechaEntregaPropietario(Long idTarjPro) {
        Date fecha = null;
        Query q = em.createQuery("select a.fechaEntregaPropietario from AmaTarjetaPropiedad a where a.id = :id and a.fechaEntregaPropietario is not null and a.activo=1 ");
        q.setParameter("id", idTarjPro);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            fecha = (Date) q.getResultList().get(0);
        }
        return fecha;
    }
    
    public AmaTarjetaPropiedad obtenerTarjetaPorRuaSerie(String nroRua, String serie) {
        Query q = em.createQuery("select a from AmaTarjetaPropiedad a where a.activo = 1 and a.armaId.serie = :serie and a.armaId.nroRua = :rua ");
        q.setParameter("rua", nroRua);
        q.setParameter("serie", serie);
        q.setHint("eclipselink.batch.type", "IN");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaTarjetaPropiedad) q.getResultList().get(0);
        }
        return null;
    }
    
    public List<AmaTarjetaPropiedad> listTarjetaArmasByNroRuaByPropietario(String tipoDoc, String filtro, Long propietarioId) {
        List<AmaTarjetaPropiedad> listRes = new ArrayList<>();
        String jpql = "select tp from AmaTarjetaPropiedad tp "
                    + " where tp.activo=1 and tp.personaCompradorId.id = :propietarioId "
                    + " and tp.armaId.estadoId.codProg = 'TP_ESTA_OPE' and tp.armaId.situacionId.codProg = 'TP_SITU_POS' ";
        
        switch(tipoDoc){
            case "1":
                    jpql += " and tp.armaId.nroRua = :filtro";
                    break;
            case "2":
                    jpql += " and tp.armaId.serie = :filtro ";
                    break;
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("filtro", filtro);
        q.setParameter("propietarioId", propietarioId);        
        
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
    public List<AmaTarjetaPropiedad> listTarjetaArmasBySerie(String filtro) {
        List<AmaTarjetaPropiedad> listRes = new ArrayList<>();
        String jpql = "select tp from AmaTarjetaPropiedad tp "
                + " where tp.activo=1 "
                + " and tp.armaId.serie = :filtro "
                //+ " and tp.armaId.estadoId.codProg = 'TP_ESTA_OPE' and tp.armaId.situacionId.codProg = 'TP_SITU_POS'"
                + " ";
               
        Query q = em.createQuery(jpql);
        q.setParameter("filtro", filtro);
        
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public int contarTarjetaPropiedadEmitidasBySerie(String serie, Long tipoArmaId) {
        int cont = 0;
        
        String jpql = "select count(a) " +
                      " from AmaTarjetaPropiedad a " +
                      " where a.activo = 1 and a.armaId.activo = 1 " +
                      " and a.armaId.serie = :serie and a.armaId.modeloId.tipoArmaId.id = :tipoArmaId  ";

        Query q = em.createQuery(jpql);
        q.setParameter("serie", serie);
        q.setParameter("tipoArmaId", tipoArmaId);
         
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }
    
    public List<AmaTarjetaPropiedad> listarTarjetaElectronica(String tipoDocLogueado, String nroDocLogueado, String tipoFiltro, String filtro, Date fechaIni, Date fechaFin) {
        String filtroSimple = "";

        if (tipoFiltro == null) {
            tipoFiltro = "0";
        }

        switch (tipoFiltro) {
            case "1":
                //nro de Expediente
                filtroSimple = " and a.nroExpediente = :filtro ";
                break;
            case "2":
                //nombre del propietario
                filtroSimple = " and (a.personaCompradorId.nombres = :filtro or a.personaCompradorId.apePat = :filtro)";
                break;
            case "3":
                //nro. doc propietario
                filtroSimple = " and a.personaCompradorId.numDoc = :filtro ";
                break;
            case "4":
                //serie
                filtroSimple = " and a.armaId.serie = :filtro ";
                break;
            case "5":
                //nro rua
                filtroSimple = " and a.armaId.nroRua = :filtro ";
                break;
            default:
                filtroSimple = " and a.fechaEmision >= :fecIni and a.fechaEmision <= :fecFin ";
                break;
        }

        String qryStr = "select a from AmaTarjetaPropiedad a "
                + "where a.activo = 1 and a.emitido = 1 ";

        if (tipoDocLogueado.equals("RUC")) {
            qryStr += "and a.personaCompradorId.ruc = :nroDocLogueado ";;
        } else {
            qryStr += "and a.personaCompradorId.numDoc = :nroDocLogueado ";;
        }

        qryStr += filtroSimple;

        Query q = em.createQuery(qryStr);

        if (tipoFiltro != "0") {
            q.setParameter("filtro", filtro);
        } else {
            q.setParameter("fecIni", fechaIni);
            q.setParameter("fecFin", fechaFin);
        }
        q.setParameter("nroDocLogueado", nroDocLogueado);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaTarjetaPropiedad> buscarTarXNroRua(String nroRua) {
        Query q = em.createQuery("select a from AmaTarjetaPropiedad a where a.armaId.nroRua = :nroRua order by 1 asc");
        q.setParameter("nroRua", nroRua);
        return q.getResultList();
    }

        public List<AmaTarjetaPropiedad> listTarjetaArmasSerieRuaTipoArma(String tipoDoc, String filtro, Long tipoArmaId, Long MarcaId) {
        //Tarjetas de propiedad en estado VIGENTE por Serie o Nro RUA
        List<AmaTarjetaPropiedad> listRes = new ArrayList<>();
        String jpql = "select tp from AmaTarjetaPropiedad tp "
                    + " where tp.activo=1 "
                    + " and tp.estadoId.codProg = 'TP_EST_VIG' and tp.armaId.modeloId.tipoArmaId.id = :tipoArmaId "
                    + " and tp.armaId.modeloId.marcaId.id = :MarcaId ";
        
        switch(tipoDoc){
            case "1":
                    jpql += " and tp.armaId.nroRua = :filtro";
                    break;
            case "2":
                    jpql += " and tp.armaId.serie = :filtro ";
                    break;
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("filtro", filtro);
        q.setParameter("tipoArmaId", tipoArmaId);
        q.setParameter("MarcaId", MarcaId);
        
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
        
    public List<AmaTarjetaPropiedad> listTarjetaArmasSerieRuaPropietarioTipoArma(String tipoDoc, String filtro, Long propietarioId, Long tipoArmaId) {
        //Tarjetas de propiedad en estado VIGENTE de un Comprador/Propietario por Serie o Nro RUA
        List<AmaTarjetaPropiedad> listRes = new ArrayList<>();
        String jpql = "select tp from AmaTarjetaPropiedad tp "
                    + " where tp.activo=1 and tp.personaCompradorId.id = :propietarioId "
                    + " and tp.estadoId.codProg = 'TP_EST_VIG' and tp.armaId.modeloId.tipoArmaId.id = :tipoArmaId ";
        
        switch(tipoDoc){
            case "1":
                    jpql += " and tp.armaId.nroRua = :filtro";
                    break;
            case "2":
                    jpql += " and tp.armaId.serie = :filtro ";
                    break;
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("filtro", filtro);
        q.setParameter("propietarioId", propietarioId);        
        q.setParameter("tipoArmaId", tipoArmaId);
        
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
}
