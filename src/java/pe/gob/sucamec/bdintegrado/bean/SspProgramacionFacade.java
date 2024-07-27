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
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.SspProgramacion;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspProgramacionFacade extends AbstractFacade<SspProgramacion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspProgramacionFacade() {
        super(SspProgramacion.class);
    }
    public List<SspProgramacion> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspProgramacion s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public SspProgramacion obtenerProgramacionById(Long id) {
        Query q = em.createQuery("select s from SspProgramacion s where s.id = :id");
        q.setParameter("id", id);
        if(!q.getResultList().isEmpty()){
            return (SspProgramacion) q.getResultList().get(0);
        }
        return null;
    }
    
    public int contarFechaRangoProgramacion(Long programacionId, String numDoc, Date horaIni, Date horaFin, Date fecha) {
        int cont = 0;
        String jpql = "select count(s) " + 
                      " from SspProgramacion s " + 
                      " left join s.sspPrograHoraList h " +
                      " where s.id != :programacionId " +
                      "  and s.activo = 1 and h.activo = 1 and s.registroCursoId.activo = 1 " +
                      "  and s.registroCursoId.estadoId.codProg not in ('TP_ECC_CAN','TP_ECC_NPR','TP_ECC_NDR','TP_ECC_FDP','TP_ECC_DES') " +
                      "  and func('trunc',h.fecha) = :fecha " +
                      "  and s.instructorId.personaId.numDoc = :numDoc " +
                      "  and ( (:horaIni > h.horaInicio and :horaIni < h.horaFin) or " +
                      "   (:horaFin > h.horaInicio and :horaFin < h.horaFin ) or " +
                      "   (:horaIni <= h.horaInicio and :horaFin >= h.horaFin) " +
                      " ) ";
        
        Query q = em.createQuery(jpql);
        q.setParameter("programacionId", programacionId);
        q.setParameter("numDoc", numDoc);
        q.setParameter("fecha", fecha, TemporalType.DATE);
        q.setParameter("horaIni", horaIni, TemporalType.TIMESTAMP);
        q.setParameter("horaFin", horaFin, TemporalType.TIMESTAMP);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }
}
