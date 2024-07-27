/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaLicenciaDeUsoFacade extends AbstractFacade<AmaLicenciaDeUso> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaLicenciaDeUsoFacade() {
        super(AmaLicenciaDeUso.class);
    }

    public List<AmaLicenciaDeUso> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaLicenciaDeUso a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public AmaLicenciaDeUso obtenerLicenciaDeUso(Long empresaId, String docLicencia) {
        Query q = em.createQuery("select a from AmaLicenciaDeUso a where a.activo = 1 and a.personaPadreId.id = :empresaId and a.personaLicenciaId.numDoc = :docLicencia ");
        q.setParameter("empresaId", empresaId);
        q.setParameter("docLicencia", docLicencia);
        
        if(!q.getResultList().isEmpty()){
            return (AmaLicenciaDeUso) q.getResultList().get(0);
        }
        return null;
    }
    
    public List<AmaLicenciaDeUso> listarAmaLicenciaVigenteXPersonaId(String nroDoc) {
        Query q = em.createQuery("select a from AmaLicenciaDeUso a"
                //+ " inner join a.amaTipoLicenciaList t"
                + " where a.fechaVencimietnto >= :hoy and a.personaLicenciaId.numDoc = :nroDoc and a.activo = 1 and a.estadoId.codProg not in ('TP_EST_CAN','TP_EST_ANU','TP_EST_NULIDAD') "
                //+ " and t.estadoId.codProg not in('TP_EST_ANU_OFI', 'TP_EST_NULIDAD')"
                //+ " and t.estadoId is null"
                + "");
        q.setParameter("nroDoc", nroDoc);
        q.setParameter("hoy", new Date());
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<AmaLicenciaDeUso> listarAmaLicenciaXPerLicencia(Long perLicenciaId) {
        Query q = em.createQuery("select a from AmaLicenciaDeUso a where a.personaLicenciaId.id = :personaLicenciaId and a.activo = 1");
        q.setParameter("personaLicenciaId", perLicenciaId);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaLicenciaDeUso> listarLicenciaXPerLicenciaXRenovar(Long perLicenciaId) {
        Query q = em.createQuery("select a from AmaLicenciaDeUso a "
                + "inner join a.amaTipoLicenciaList tl "
                + "where a.personaLicenciaId.id = :personaLicenciaId and a.activo = 1 "
                + "and tl.modalidadId.codProg in ('TP_MOD_SEG', 'TP_MOD_SIS') ");
        q.setParameter("personaLicenciaId", perLicenciaId);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaLicenciaDeUso> listarLicenciaXPerLicenciaXRenovarJuridica(Long perLicenciaId) {
        Query q = em.createQuery("select a from AmaLicenciaDeUso a "
                + "inner join a.amaTipoLicenciaList tl "
                + "where a.personaLicenciaId.id = :personaLicenciaId and a.activo = 1 "
                + "and tl.modalidadId.codProg in ('TP_MOD_SEG') ");
        q.setParameter("personaLicenciaId", perLicenciaId);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<AmaLicenciaDeUso> listarLicenciaSegVigXPerLicenciaXRenovar(String numDoc) {
        Query q = em.createQuery("select a from AmaLicenciaDeUso a "
                + "inner join a.amaTipoLicenciaList tl "
                + "where a.personaLicenciaId.numDoc = :numDoc "
                + "and a.activo = 1 "
                + "and tl.modalidadId.codProg in ('TP_MOD_SEG') "
                + "and tl.activo = 1 "
                + "and tl.estadoId.codProg not in ('TP_EST_NULIDAD','TP_EST_ANU') "
                + "and a.estadoId.codProg in ('TP_EST_VIG','TP_EST_VEN') ");
        q.setParameter("numDoc", numDoc);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<AmaLicenciaDeUso> listarLicenciaElectronica(String tipoDocLogueado, String nroDocLogueado, String tipoFiltro, String filtro, Date fechaIni, Date fechaFin) {
        String filtroSimple = "";

        if (tipoFiltro == null) {
            tipoFiltro = "0";
        }

        switch (tipoFiltro) {
            case "1":
                filtroSimple = " and tl.nroExpediente = :filtro ";
                break;
            case "2":
                filtroSimple = " and (a.personaLicenciaId.nombres = :filtro or a.personaLicenciaId.apePat = :filtro)";
                break;
            case "3":
                filtroSimple = " and a.personaLicenciaId.numDoc = :filtro ";
                break;
            case "4":
                filtroSimple = " and a.nroLicencia = :filtro ";
                break;
            default:
                filtroSimple = " and a.fechaEmision >= :fecIni and a.fechaEmision <= :fecFin ";
                break;
        }

        String qryStr = "select a from AmaLicenciaDeUso a "
                + "inner join a.amaTipoLicenciaList tl "
                + "where a.activo = 1 "
                + "and tl.activo = 1 ";

        if (tipoDocLogueado.equals("RUC")) {
            qryStr += "and a.personaPadreId.ruc = :nroDocLogueado ";;
        } else {
            qryStr += "and a.personaLicenciaId.numDoc = :nroDocLogueado ";;
        }

        qryStr += filtroSimple;

        //+ "and tl.modalidadId.codProg in ('TP_MOD_SEG') "
        //+ "and tl.estadoId.codProg not in ('TP_EST_NULIDAD','TP_EST_ANU') "
        //+ "and a.estadoId.codProg in ('TP_EST_VIG','TP_EST_VEN') "
        Query q = em.createQuery(qryStr);

        if (tipoFiltro != null) {
            q.setParameter("filtro", filtro);
        } else {
            q.setParameter("fecIni", fechaIni);
            q.setParameter("fecFin", fechaFin);
        }
        q.setParameter("nroDocLogueado", nroDocLogueado);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
}
