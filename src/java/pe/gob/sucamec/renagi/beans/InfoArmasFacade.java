/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.AmaFoto;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.Expediente;
//import pe.gob.sucamec.bdintegrado.data.Expediente;
//import pe.gob.sucamec.notificacion.data.ExpedienteCydoc;
//import pe.gob.sucamec.sisturno.data.ExpedienteTD;

/**
 *
 * @author Renato
 */
@Stateless
public class InfoArmasFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    private static final int MAX_RES = 10000;

    public String nullATodo(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return s.replace("%", "");
    }

    public String nullATodoParcial(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s.replace("%", "") + "%";
    }

    public String nullATodoComodin(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s + "%";
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public List<AmaTarjetaPropiedad> listarTarjetasXnroSerie(String filtro) {
        try {
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("select a from AmaTarjetaPropiedad a where a.activo = 1 ");
            sbQuery.append("and a.armaId.serie like :serie");
    //                + "a.personaCompradorId.numDoc like :dni or a.personaCompradorId.ruc like :ruc ");
    //        sbQuery.append("or  ");
    //        sbQuery.append("or a.armaId.nroRua like :nrorua)");

            Query q = em.createQuery(sbQuery.toString());
    //        q.setParameter("dni", filtro);
    //        q.setParameter("ruc", filtro);
            q.setParameter("serie", filtro.toUpperCase());
    //        q.setParameter("nrorua", filtro.toUpperCase());
            q.setMaxResults(MAX_RES);
            return q.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public AmaTarjetaPropiedad obtenerArmaPorSerieRua(String nroRua, String serie) {
        try {
            Query q = em.createQuery("select a from AmaTarjetaPropiedad a where a.activo = 1 and a.armaId.serie = :serie and a.armaId.nroRua = :rua ");
            q.setParameter("rua", nroRua);
            q.setParameter("serie", serie.toUpperCase());
            q.setHint("eclipselink.batch.type", "IN");
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                return (AmaTarjetaPropiedad) q.getResultList().get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Expediente> selectExpedienteXNro(String nroExp) {
        if (nroExp == null) {
            nroExp = "";
        }
        Query q = em.createQuery("select e from Expediente e where e.numero = :nroExp");
        q.setParameter("nroExp", nroExp);
        return q.getResultList();
    }

    public List<AmaInventarioArma> selectInventarioArma(String nroRua, String serie) {
        try {
            Query q = em.createQuery("select a from AmaInventarioArma a where a.serie = :serie and a.nroRua = :rua order by a.id desc");
            q.setParameter("rua", nroRua);
            q.setParameter("serie", serie.toUpperCase());
            return q.getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<AmaInventarioArma> selectInventarioArmaDisca(String nroLic, String serie) {
        try {
            Query q = em.createQuery("select a from AmaInventarioArma a where a.serie = :serie and a.licenciaDiscaId = :licenciaDiscaId order by a.id desc");
            q.setParameter("licenciaDiscaId", Long.valueOf(nroLic));
            q.setParameter("serie", serie.toUpperCase());
            return q.getResultList();            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<AmaFoto> listarAmaFotoXNroExp(String nroExp) {
        Query q = em.createQuery("select a from AmaFoto a where a.nroExpediente = :nroExp and a.activo = 1");
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaFoto> listarAmaFotoXLicencia(Long licenciaId) {
        Query q = em.createQuery("select a from AmaFoto a"
                + " join a.amaLicenciaDeUsoList l"
                + " where l.id = :licenciaId and a.activo = 1"
                + " order by a.id desc");
        q.setParameter("licenciaId", licenciaId);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaFoto> listarAmaFotoXPersona(Long personaId) {
        Query q = em.createQuery("select a from AmaFoto a"
                + " join a.amaLicenciaDeUsoList l"
                + " where l.personaLicenciaId.id = :personaId and a.activo = 1"
                + " order by a.id desc");
        q.setParameter("personaId", personaId);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    /* LICENCIAS DE USO */
    public List<AmaLicenciaDeUso> buscarLicXNroActivo(Long nro) {
        Query q = em.createQuery("select a from AmaLicenciaDeUso a where a.nroLicencia = :nro and a.activo = 1 and a.estadoId.codProg<>'TP_EST_NULO' order by 1 asc");
        q.setParameter("nro", nro);
        return q.getResultList();
    }
    
    public List<AmaLicenciaDeUso> listarLicenciasXFiltro(HashMap mMap) {
        String tipoFiltro = mMap.get("tipoFiltro").toString();
        String filtro = ((mMap.get("filtro")!=null)? mMap.get("filtro").toString():"");
        Date fechaIni = ((mMap.get("fechaIni")!=null)? (Date) mMap.get("fechaIni"):null);
        Date fechaFin = ((mMap.get("fechaFin")!=null)? (Date) mMap.get("fechaFin"):null);
        String campo = "";
        String inner = "";        
        
        if (fechaIni != null) {
            campo = " and (FUNC('trunc',a.fechaEmision) >= FUNC('trunc',:fechaIni)) ";
        }
        if (fechaFin != null) {
            campo = " and (FUNC('trunc',a.fechaEmision) <= FUNC('trunc',:fechaFin)) ";
        }
        
        if (tipoFiltro.equals("nroDni") || tipoFiltro.equals("ce")) {
            inner = " inner join a.amaTipoLicenciaList l";        
            campo = " and (a.personaLicenciaId.numDoc=:numDoc"
                    + " or l.nroExpediente in(select t.numero from ExpedienteCydoc t where t.idCliente.numeroIdentificacion=:numDoc and t.numero is not NULL)"
                    + ") ";
        }
        if (tipoFiltro.equals("ruc")) {
            inner = " inner join a.amaTipoLicenciaList l";        
            campo = " and (a.personaPadreId.ruc=:ruc"
                    + " or l.nroExpediente in(select t.numero from ExpedienteCydoc t where t.idCliente.numeroIdentificacion=:ruc and t.numero is not NULL)"
                    + ") ";
        }
        if (tipoFiltro.equals("docSoli")) {
            inner = " inner join a.amaTipoLicenciaList l";        
            campo = " and ( l.nroExpediente in(select t.numero from ExpedienteCydoc t where t.idCliente.numeroIdentificacion=:numDoc and t.numero is not NULL) ) ";
        }
        if (tipoFiltro.equals("nroExp")) {
            campo = " and a.nroExpediente like :nroExp ";
        }
        if (tipoFiltro.equals("nroLic")) {
            campo = " and trim(a.nroLicencia) like :nroLic ";
        }
        if (tipoFiltro.equals("nomPortador")) {
            campo = " and (a.personaLicenciaId.nombres like :nomPortador OR a.personaLicenciaId.apePat like :nomPortador or a.personaLicenciaId.apeMat like :nomPortador)";
        }
        
        String sql = "select a from AmaLicenciaDeUso a "
                + inner
                + " where a.activo = 1"
                + campo 
                + "";

        Query query = em.createQuery(sql);
        
        switch(tipoFiltro){
            case "ce":
            case "nroDni":
            case "docSoli":
                query.setParameter("numDoc", nullATodo(filtro.trim().toUpperCase()));
                break;
            case "ruc":
                query.setParameter("ruc", nullATodo(filtro.trim().toUpperCase()));
                break;                
            case "nomPortador":
                query.setParameter("nomPortador", nullATodoParcial(filtro.trim()));
                break;
            case "nroLic":
                query.setParameter("nroLic", nullATodoParcial(filtro.trim()));
                break;
        }
        
        query.setMaxResults(MAX_RES);
        return query.getResultList();
    }    
    
    /*public List<ExpedienteCydoc> selectExpedienteXGest(String nroExp) {
        if (nroExp == null) {
            nroExp = "";
        }
        
        String sql = "select e from ExpedienteCydoc e"
                + " where e.numero = :nroExp"
                + "";

        Query q = em.createQuery(sql);
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }*/   
        
}
