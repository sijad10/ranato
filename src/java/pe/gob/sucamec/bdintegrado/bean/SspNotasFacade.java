/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspNotas;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspNotasFacade extends AbstractFacade<SspNotas> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspNotasFacade() {
        super(SspNotas.class);
    }
    
    public List<SspNotas> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspNotas s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public int contNotasAlumnoByRegistroIdByModulo(Long registroId, Long moduloId, Long alumnoId) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from SspNotas a where a.activo = 1 "+
                                  " and a.regcursoId.id = :registroId and a.alumnoId.id = :alumnoId " +
                                  " and a.moduloId.id = :moduloId "
                                );
        q.setParameter("registroId", registroId);
        q.setParameter("alumnoId", alumnoId);
        q.setParameter("moduloId", moduloId);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
}
