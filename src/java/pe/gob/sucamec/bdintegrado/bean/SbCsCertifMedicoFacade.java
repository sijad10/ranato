/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SbCsCertifMedico;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbCsCertifMedicoFacade extends AbstractFacade<SbCsCertifMedico> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbCsCertifMedicoFacade() {
        super(SbCsCertifMedico.class);
    }
    public List<SbCsCertifMedico> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbCsCertifMedico s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SbCsCertifMedico> obtenerCertifDeMedico(Long medicoId) {
        Query q = em.createQuery("select s from SbCsCertifMedico s where s.activo = 1 and s.medicoId.id = :medicoId");
        q.setParameter("medicoId", medicoId);
        return q.getResultList();
    }
    
    public int contarRegistrosCertifMedico(Long medicoId, Long establecimientoId) {
        int cont = 0;
        Query q = em.createQuery("select count(s) from SbCsCertifMedico s where s.activo = 1 and s.certifmedicoId.activo = 1 and s.medicoId.id = :medicoId and s.certifmedicoId.establecimientoId.id = :establecimientoId " );
        q.setParameter("medicoId", medicoId);
        q.setParameter("establecimientoId", establecimientoId);        
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public Long buscarRegistrosCertifMedico(Long medicoId, Long establecimientoId) {
        String sql = "select 1 from bdintegrado.Sb_Cs_Certif_Medico s \n" +
                " inner join bdintegrado.SB_CS_CERTIFSALUD cs on certifmedico_id = cs.id\n" +
                " where s.activo = 1 and cs.activo = 1 and medico_id=" + medicoId +
                " and establecimiento_id =" + establecimientoId +" and ROWNUM = 1";
        Query query = em.createNativeQuery(sql);
        if(query.getResultList().isEmpty()){
            return 0L;
        }
        Long cant = ((BigDecimal) query.getSingleResult()).longValue();
        return cant;
    }
}
