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
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.sistemabase.data.SbTipo;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class TipoSeguridadFacade extends AbstractFacade<TipoSeguridad> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoSeguridadFacade() {
        super(TipoSeguridad.class);
    }
    
    public List<TipoSeguridad> listarTipoSeguridadXCodProgs(String codigos) {
        List<TipoSeguridad> respuesta = new ArrayList();
        String sentencia = "select t from TipoSeguridad t where t.codProg in ("+codigos+") and t.activo = 1 ORDER BY t.id asc";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<TipoSeguridad> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from TipoSeguridad t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<TipoSeguridad> lstTipoSeguridad(String s) {
        List<TipoSeguridad> respuesta = new ArrayList();
        Query query = getEntityManager().createQuery("select t from TipoSeguridad t where t.activo = 1 and t.tipoId.codProg = :codProg order by t.nombre ASC");
        query.setParameter("codProg", s != null ? s : "");
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public TipoSeguridad tipoSeguridadXCodProg(String codprog) {
        TipoSeguridad tipo = null;
        String sentencia = "select t from TipoSeguridad t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (TipoSeguridad) query.getSingleResult();
        return tipo;
    }
    
    public TipoSeguridad tipoSeguridadXId(Long codTipSeg) {
        TipoSeguridad tipo = null;
        String sentencia = "select t from TipoSeguridad t where t.id = :codTipSeg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codTipSeg", codTipSeg);
        tipo = (TipoSeguridad) query.getSingleResult();
        return tipo;
    }
        
    public List<TipoSeguridad> lstTiposEstadosBandejaCartera() {
        Query query = getEntityManager().createQuery("select t from  TipoSeguridad t where t.activo =1 and t.codProg in ('TP_ECC_CRE','TP_ECC_OBS','TP_ECC_APR','TP_ECC_NPR','TP_ECC_FDP','TP_ECC_DES') order by t.orden, t.nombre");
        return query.getResultList();
    }
    
    public List<TipoSeguridad> tipoSeguridadXCodigosProgs(String codprog) {
        String sentencia = "select t from TipoSeguridad t where t.codProg in ("+codprog+") ";
        Query query = getEntityManager().createQuery(sentencia);        
        return query.getResultList();
    }
    
    public List<TipoSeguridad> tipoSeguridadXCodigosProgsOrderById(String codprog) {
        String sentencia = "select t from TipoSeguridad t where t.codProg in ("+codprog+") order by t.id ";
        Query query = getEntityManager().createQuery(sentencia);        
        return query.getResultList();
    }
    
    public List<TipoSeguridad> lstTiposEstadosBandejaCurso() {
        Query query = getEntityManager().createQuery("select t from  TipoSeguridad t where t.activo =1 and t.codProg in ('TP_ECC_CRE','TP_ECC_OBS','TP_ECC_TRA') order by t.orden, t.nombre");
        return query.getResultList();
    }
    
    public List<TipoSeguridad> lstModalidadesSinEventos() {
        Query query = getEntityManager().createQuery("select t from TipoSeguridad t where t.activo = 1 and t.tipoId.codProg = 'TP_MCO' and t.codProg != 'TP_MCO_EVT' order by t.orden, t.nombre");
        return query.getResultList();
    }
    public List<TipoSeguridad> lstSituacionArmas() {
        Query query = getEntityManager().createQuery("select t from TipoSeguridad t where t.activo = 1 and t.tipoId.codProg = 'TP_SITU' order by t.orden, t.nombre");
        return query.getResultList();
    }
     public List<TipoSeguridad> lstEstadoArmas() {
        Query query = getEntityManager().createQuery("select t from TipoSeguridad t where t.activo = 1 and t.tipoId.codProg = 'TP_ESTA' order by t.orden, t.nombre");
        return query.getResultList();
    }
     public List<TipoSeguridad> tipoServicioXCodigosProgs() {
        //Query query = getEntityManager().createQuery("select t from TipoSeguridad t where t.activo = 1 and t.tipoId.codProg in ('TP_SER_GEN','TP_SER_EVE') order by t.orden, t.nombre");
        //return query.getResultList();
        
        String sentencia = "select t from TipoSeguridad t where t.codProg in ('TP_GEN_SSP','TP_EVE_SSP') ";
        Query query = getEntityManager().createQuery(sentencia);        
        return query.getResultList();
    }
     
    /*public TipoSeguridad tipoSeguridadXDescripcion(String descripcion) {
        TipoSeguridad tipo = null;
        String sentencia = "select t from TipoSeguridad t where t.nombre = :descripcion";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("descripcion", descripcion != null ? descripcion : "");
        tipo = (TipoSeguridad) query.getSingleResult();
        return tipo;
    }*/

    public TipoSeguridad tipoSeguridadXDescripcion(String descripcion) {
        String sentencia = "select t from TipoSeguridad t where t.nombre = :descripcion";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("descripcion", descripcion != null ? descripcion : "");
         if(!query.getResultList().isEmpty()){
            return (TipoSeguridad) query.getResultList().get(0);
        }
        return null;
    }

}
