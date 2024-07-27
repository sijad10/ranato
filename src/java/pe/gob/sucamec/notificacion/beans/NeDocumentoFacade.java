/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.notificacion.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.notificacion.data.NeArchivo;
import pe.gob.sucamec.notificacion.data.NeDocumento;

/**
 *
 * @author Renato
 */
@Stateless
public class NeDocumentoFacade extends AbstractFacade<NeDocumento> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NeArchivo archivoFirmado(NeDocumento d) {
        Query q = em.createQuery(
                "select a from NeArchivo a where a.activo = 1 and a.tipoId.codProg = 'TP_ADJ_FIR' and a.documentoId = :documento");
        q.setParameter("documento", d);
        List<NeArchivo> l = q.setMaxResults(1).getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    public List<NeDocumento> selectLike(String filtro, String anho, long personaId) {
        filtro = filtro == null ? "%" : "%" + filtro + "%";
        anho = anho == null ? "%" : anho;
        Query q = em.createQuery(
                "select d from NeDocumento d where (d.referencia like :referencia or d.nroDoc like :nroDoc or trim(d.nroExp) like :nroExp or trim(d.nroExpediente) like :nroExp) and d.activo = 1 and (d.estadoId.codProg = 'TP_EV_ENV' or d.estadoId.codProg = 'TP_EV_LEI') and trim(extract(YEAR from d.fechaDoc)) like :anho and d.personaId.id = :personaId  order by d.id desc");
        // and d.personaId.id = :personaId 
        q.setParameter("referencia", filtro);
        q.setParameter("nroDoc", filtro);
        q.setParameter("nroExp", filtro);
        q.setParameter("anho", anho);
        q.setParameter("personaId", personaId);

        q.setHint("eclipselink.batch", "d.usuarioId");
        q.setHint("eclipselink.batch", "d.tipoId");
        q.setHint("eclipselink.batch", "d.estadoId");
        q.setHint("eclipselink.batch", "d.personaId");
        q.setHint("eclipselink.batch.type", "IN");

        return q.setMaxResults(MAX_RES).getResultList();
    }

    public NeDocumentoFacade() {
        super(NeDocumento.class);
    }

    public NeDocumento buscarNeDocumentoRaessByNroExpediente(String documento) {
        Query q = em.createQuery(
                "select a from NeDocumento a where a.activo = 1 and a.tipoId.codProg = 'TP_DOC_NOTVIR' and a.nroExpediente = :documento order by a.id desc");
        q.setParameter("documento", documento);
        if (q.setMaxResults(1).getResultList().isEmpty()) {
            return null;
        }
        return (NeDocumento) q.getResultList().get(0);
    }
    
    public NeArchivo archivoResolucionGSSP(Long registroId) {
         String jpql = "select a "
                + " from SspRegistro r, NeDocumento doc, NeArchivo a "
                + " where doc.id = a.documentoId.id and r.activo = 1 and doc.activo = 1 and doc.tipoId.codProg = 'TP_DOC_AUTO'"
                + " and r.nroExpediente = doc.nroExpediente and a.nombre like '%[R]%'"
                + " and r.id = :registroId and a.activo = 1";
        Query q = em.createQuery(jpql);
        q.setParameter("registroId", registroId);
        List<NeArchivo> l = q.setMaxResults(1).getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

        
}
