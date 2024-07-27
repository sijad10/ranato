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
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppCertificado;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppCertificadoFacade extends AbstractFacade<EppCertificado> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppCertificadoFacade() {
        super(EppCertificado.class);
    }
    
    public List<EppCertificado> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select e from EppCertificado e where trim(e.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    /**
     * Obtener total de inscritos a una determinada capacitacion
     * @author Richar Fernández
     * @version 2.0
     * @param idCap
     * @return Total de inscritos
     */
    public int contarInscritos(String idCap) {
        int cont = 0;
        if(idCap == null){
            idCap = "";
        }
        Query q = em.createQuery("select count(e) from EppCertificado e where trim(e.capacitacionId.id) = :idCap and e.activo = 1 and e.registroId.activo = 1 ");
        q.setParameter("idCap", idCap.trim() );
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont =  _values.intValue();
            break;
        }
        
        return cont;
    }
    
    /**
     * Obtener total de inscritos a una determinada capacitacion
     * @author Richar Fernández
     * @version 2.0
     * @param idCap
     * @param idPersona
     * @return Total de inscritos
     */
    public int buscarPersonaInscritaCapacitacion(String idCap, String idPersona) {
        int cont = 0;
        if(idCap == null){
            idCap = "";
        }
        Query q = em.createQuery("select count(e.id) from EppCertificado e where trim(e.capacitacionId.id) = :idCap and trim(e.personaId.id) = :idPersona  and e.activo = 1 ");
        q.setParameter("idCap", idCap.trim() );
        q.setParameter("idPersona", idPersona.trim() );
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont =  _values.intValue();
            break;
        }
        
        return cont;
    }
}
