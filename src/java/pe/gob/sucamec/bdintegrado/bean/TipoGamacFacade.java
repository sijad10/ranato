/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class TipoGamacFacade extends AbstractFacade<TipoGamac> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoGamacFacade() {
        super(TipoGamac.class);
    }
    public List<TipoGamac> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from TipoGamac t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerEstadosOperativoInoperativo() {
        Query q = em.createQuery("select t from TipoGamac t where t.codProg in ('TP_ESTA_OPE','TP_ESTA_INO') and t.activo = 1 order by t.nombre  ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerEstadosOperativo() {
        Query q = em.createQuery("select t from TipoGamac t where t.codProg = 'TP_ESTA_OPE' and t.activo = 1 ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerEstadosArmas() {
        Query q = em.createQuery("select t from TipoGamac t where t.tipoId.codProg = 'TP_ESTA' and t.activo = 1 order by t.nombre ");
        return q.getResultList();
    }
    
    
    public List<TipoGamac> obtenerSituacionNuevaArma() {
        Query q = em.createQuery("select t from TipoGamac t where t.tipoId.codProg = 'TP_SITU' and t.codProg not in ('TP_SITU_VEN','TP_EST_EMP_NOP') and t.activo = 1 order by t.nombre  ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerSituacionNuevaArmaInpe() {
        Query q = em.createQuery("select t from TipoGamac t where t.tipoId.codProg = 'TP_SITU' and t.codProg in ('TP_SITU_POS','TP_SITU_INT') and t.activo = 1 order by t.nombre  ");
        return q.getResultList();
    }
            
    public List<TipoGamac> obtenerSituacionArmaNoPosee() {
        Query q = em.createQuery("select t from TipoGamac t where t.tipoId.codProg = 'TP_SITU' and t.codProg not in ('TP_SITU_POS') and t.activo = 1 order by t.nombre ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerSituacionArmaEnPosesion() {
        Query q = em.createQuery("select t from TipoGamac t where t.codProg = 'TP_SITU_POS' and t.activo = 1 order by t.nombre ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerSituacionArmaInternamiento() {
        Query q = em.createQuery("select t from TipoGamac t where t.codProg = 'TP_SITU_INT' and t.activo = 1 ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerSituacionArmaInternamientoNoEsPropiedad() {
        Query q = em.createQuery("select t from TipoGamac t where t.codProg in ('TP_SITU_INT', 'TP_EST_EMP_NOP') and t.activo = 1 ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerSituacionArmaJuridicas() {
        Query q = em.createQuery("select t from TipoGamac t where t.codProg in ('TP_SITU_ILI', 'TP_EST_EMP_NOP', 'TP_SITU_TIT', 'TP_SITU_PER', 'TP_SITU_ROB') and t.activo = 1 ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerSituacionArmaInpe() {
        Query q = em.createQuery("select t from TipoGamac t where t.codProg in ('TP_EST_EMP_NOP', 'TP_SITU_VEN', 'TP_SITU_PER', 'TP_SITU_ROB') and t.activo = 1 ");
        return q.getResultList();
    }
    
    public List<TipoGamac> obtenerXId(Long id) {
        Query q = em.createQuery("select t from TipoGamac t where t.id = :id");
        q.setParameter("id", id);
        return q.getResultList();
    }
    
    public List<TipoGamac> selectTipoGamac(String codProg) {
        Query q = em.createQuery("select t from TipoGamac t where t.tipoId.codProg = :codProg and t.activo = 1");
        q.setParameter("codProg", codProg);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public TipoGamac buscarTipoGamacXCodProg(String codProg) {
        TipoGamac respuesta = new TipoGamac();
        String sentencia = "select t from TipoGamac t where t.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        respuesta = (TipoGamac) query.getSingleResult();
        return respuesta;
    }
    
    public List<TipoGamac> listarTipoGamacByManyCodProgs(String codigos) {
        Query query = em.createQuery("select t from TipoGamac t where t.codProg in (" + codigos + ") and t.activo = 1 ");
        return query.getResultList();
    }
    
    public List<TipoGamac> listarTipadoRecursivoById(Long tipadoId) {
        String sql = "SELECT * FROM BDINTEGRADO.TIPO_GAMAC CONNECT BY PRIOR ID = TIPO_ID START WITH TIPO_ID = ?1";
        Query q = em.createNativeQuery(sql, TipoGamac.class);
        q.setParameter(1, tipadoId);        
        return q.getResultList();
    }
    public List<TipoGamac> obtenerTipoArmas() {
        Query q = em.createQuery("select t from TipoGamac t where t.tipoId.codProg = 'TP_ARM' and t.activo = 1 order by t.nombre ");
        return q.getResultList();
    }
}
