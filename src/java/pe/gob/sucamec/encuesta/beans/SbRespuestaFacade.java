/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.encuesta.beans;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.bean.AbstractFacade;
import pe.gob.sucamec.encuesta.data.SbEncuesta;
import pe.gob.sucamec.encuesta.data.SbRespuesta;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author gchavez
 */
@Stateless
public class SbRespuestaFacade extends AbstractFacade<SbRespuesta> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbRespuestaFacade() {
        super(SbRespuesta.class);
    }
    public List<SbRespuesta> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbRespuesta s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SbRespuesta> selectRespuestas(SbEncuesta encuesta, SbUsuario usu) {
        Query q = em.createQuery("select s from SbRespuesta s where s.activo = 1 and s.preguntaId.encuestaId = :enc and s.usuarioId = :usu ");
        q.setParameter("enc", encuesta);
        q.setParameter("usu", usu);
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
}
