/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.Documento;

/**
 *
 * @author gchavez
 */
@Stateless
public class DocumentoFacade extends AbstractFacade<Documento> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoFacade() {
        super(Documento.class);
    }

    public List<Documento> listDocumentoPorNroExp(String nroExp) {
        List<Documento> listRes = null;
        Query q = em.createQuery("select d from Documento d where d.expediente.numero = :nroExp and d.expediente.estado like 'R' ");
        q.setParameter("nroExp", nroExp);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
    public List<Map> listDocumentosMap(String nroExp) {
        List<Map> listRes = null;
        Query q = em.createQuery("select d.idDocumento as id, d.numero, d.titulo, d.expediente.numero, d.fechaCreacion, d.fechaDocumento from Documento d where d.expediente.numero = :nroExp and d.expediente.estado like 'R' ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("nroExp", nroExp);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
    public List<Documento> listDocumentosSuspensionCS(String nroExp) {
        List<Documento> listRes = null;
        Query q = em.createQuery("select d from Documento d where d.expediente.numero = :nroExp and d.idTipoDocumento.idTipoDocumento in (1,2,3,4) and d.expediente.idProceso.idProceso in (328, 748) ");
        q.setParameter("nroExp", nroExp);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
    
    public Documento buscarDocumentoByNroDoc(String nroDoc) {
        Query q = em.createQuery("select d from Documento d where d.numero = :nroDoc");
        q.setParameter("nroDoc", nroDoc);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (Documento) q.getResultList().get(0);
        }
        return null;
    }
    
    public List<Documento> lisDocumentoTramdocOficio(String tipoDoc, String nroExp, String serie, String numDoc) {
        try {
            javax.persistence.Query q = em.createQuery("SELECT p FROM Documento p "
                    + "WHERE p.titulo like :serie "
                    + "and p.expediente.numero = :nroExp "
                    + "and p.expediente.idCliente.numeroIdentificacion = :numDoc "
                    + "and p.idTipoDocumento.idTipoDocumento in (" + tipoDoc + ") "
                    + "order by p.idDocumento desc");
            q.setParameter("nroExp", nroExp);
            q.setParameter("serie", "%" + serie + "%");
            q.setParameter("numDoc", numDoc);
            return q.getResultList();
        } catch (NullPointerException e) {
            return new ArrayList();
        }
    }

    public List<Documento> listarDocumentosCancelacionLicencia(String nroDocumento) {
        List<Documento> listRes = new ArrayList();
        Query q = em.createQuery("select d from Documento d where d.idTipoDocumento.idTipoDocumento = 4 and d.estado = 'A' and d.numero like :nroDocumento");
        nroDocumento = nroDocumento.replace("GAMAC", "");
        q.setParameter("nroDocumento", "%" + nroDocumento + "%GAMAC%");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<Documento> lisDocumentoTramdocPorNroDoc(String nroDoc, String tipoDoc, String listaProc, Boolean buscaPorRuc) {
        try {
            javax.persistence.Query q;
            if (buscaPorRuc) {
                q = em.createQuery("SELECT p FROM Documento p WHERE p.estado = 'A' AND (p.expediente.idCliente.numeroIdentificacion = :numero or p.expediente.idCliente.numeroIdentificacion like '%" + nroDoc.trim() + "%')"
                            + " AND p.expediente.idProceso.idProceso in (" + listaProc + ") AND p.idTipoDocumento.idTipoDocumento in (" + tipoDoc + ")"
                            + " order by p.idDocumento desc");
            } else{
                q = em.createQuery("SELECT p FROM Documento p WHERE p.estado = 'A' AND p.expediente.idCliente.numeroIdentificacion = :numero AND p.expediente.idProceso.idProceso in (" + listaProc + ") AND p.idTipoDocumento.idTipoDocumento in (" + tipoDoc + ")"
                            + " order by p.idDocumento desc");
            }
            q.setParameter("numero", nroDoc);
            return q.getResultList();
        } catch (NullPointerException e) {
            return null;
        }
    }    
    
}
