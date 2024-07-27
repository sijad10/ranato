/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.renagi.data.ConsultaAmaLicenciaDeUso;
import pe.gob.sucamec.renagi.jsf.util.JsfUtil;
import pe.gob.sucamec.wstramdoc.ws.ArchivosWs;
import pe.gob.sucamec.wstramdoc.ws.DocumentosWs;
import pe.gob.sucamec.wstramdoc.ws.ServiciosSTD;
import pe.gob.sucamec.wstramdoc.ws.ServiciosSTDEndpoint;

/**
 *
 * @author msalinas
 */
@Stateless
public class ConsultaAmaLicenciaDeUsoFacade extends AbstractFacade<ConsultaAmaLicenciaDeUso> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    private ServiciosSTD wsCydoc = new ServiciosSTD();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaAmaLicenciaDeUsoFacade() {
        super(ConsultaAmaLicenciaDeUso.class);
    }

    public List<ConsultaAmaLicenciaDeUso> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from ConsultaAmaLicenciaDeUso a where trim(a.id) like :id and a.activo = 1");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ConsultaAmaLicenciaDeUso> listarAmaLicenciaXPerPadre(Long perPadreId) {
        Query q = em.createQuery("select a from ConsultaAmaLicenciaDeUso a where a.personaPadreId.id = :perPadreId and a.activo = 1");
        q.setParameter("perPadreId", perPadreId);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ConsultaAmaLicenciaDeUso> listarAmaLicenciaXPerLicencia(Long perLicenciaId) {
        Query q = em.createQuery("select a from ConsultaAmaLicenciaDeUso a where a.personaLicenciaId.id = :personaLicenciaId and a.activo = 1");
        q.setParameter("personaLicenciaId", perLicenciaId);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<ConsultaAmaLicenciaDeUso> listarAmaLicenciaXExpediente(String nroExp) {
        Query q = em.createQuery("select a from ConsultaAmaLicenciaDeUso a where a.fotoId.nroExpediente = :nroExp and a.activo = 1");
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ConsultaAmaLicenciaDeUso> listarAmaLicenciaXNroLic(Long nroLic) {
        Query q = em.createQuery("select a from ConsultaAmaLicenciaDeUso a where a.nroLicencia = :nroLic and a.activo = 1");
        q.setParameter("nroLic", nroLic);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<Map> listarLicenciasXPersona(String nroDoc) {
        String sql = "SELECT DISTINCT(L.TIPO_LICENCIA) \"MODALIDAD\" "
                + "FROM rma1369.ws_licencias@sucamec L "
                + "WHERE L.DOC_PORTADOR = ? "
                + "ORDER BY 1 ASC";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroDoc);
        res = query.getResultList();
        return res;
    }

    public List<StreamedContent> verFoto(String expediente) {
        ServiciosSTDEndpoint portCydoc = wsCydoc.getServiciosSTDEndpointPort();
        List<StreamedContent> fotos = new ArrayList<>();
        try {
            List<DocumentosWs> documentos = portCydoc.listarDocumentos(expediente, true);
            for (DocumentosWs documento : documentos) {
                if (documento.getTipoDocumento().equals("Fotografías")) {
                    for (ArchivosWs archivo : documento.getArchivos()) {
                        String url = portCydoc.descargarArchivo(archivo.getId());
                        fotos.add(verArchivo(url));
                    }
                }
            }
            return fotos;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    private StreamedContent verArchivo(String sUrl) {
        try {
            InputStream input;
            input = new URL(sUrl).openStream();
            return new DefaultStreamedContent(new ByteArrayInputStream(JsfUtil.oracleJpg(org.apache.commons.io.IOUtils.toByteArray(input), 240, 320)), "image/jpeg");
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConsultaAmaLicenciaDeUsoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConsultaAmaLicenciaDeUsoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<ConsultaAmaLicenciaDeUso> buscarLicXNroDoc(String nro) {
        Query q = em.createQuery("select a from ConsultaAmaLicenciaDeUso a where a.personaLicenciaId.numDoc = :nro and a.activo = 1 order by 1 asc");
        q.setParameter("nro", nro);
        return q.getResultList();
    }

}
