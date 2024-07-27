/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.gamac.data.GamacTipoGamac;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class GamacTipoGamacFacade extends AbstractFacade<GamacTipoGamac> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacTipoGamacFacade() {
        super(GamacTipoGamac.class);
    }
    
    public List<GamacTipoGamac> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from GamacTipoGamac t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<GamacTipoGamac> obtenerEstadosArma() {
        Query q = em.createQuery("select t from GamacTipoGamac t where t.tipoId.codProg = 'TP_ESTA' AND t.codProg not in ('TP_ESTA_OPE','TP_ESTA_REC') ");
        return q.getResultList();
    }
    
    public List<GamacTipoGamac> obtenerEstadosOperativo() {
        Query q = em.createQuery("select t from GamacTipoGamac t where t.codProg = 'TP_ESTA_OPE' ");
        return q.getResultList();
    }
    
    public List<GamacTipoGamac> obtenerEstadosArmas() {
        Query q = em.createQuery("select t from GamacTipoGamac t where t.tipoId.codProg = 'TP_ESTA' ");
        return q.getResultList();
    }

    public List<GamacTipoGamac> lstTipoComprobante() {
        Query q = em.createQuery("select t from GamacTipoGamac t where t.tipoId.codProg = 'TP_CMP' ");
        return q.getResultList();
    }

    /*public List<GamacTipoGamac> lstTipoDocTransaccion() {
        Query q = em.createQuery("select t from GamacTipoGamac t where t.tipoId.codProg = 'TP_CMP' ");
        return q.getResultList();
    }*/
    
    public List<GamacTipoGamac> obtenerXId(Long id) {
        Query q = em.createQuery("select t from GamacTipoGamac t where t.id = :id");
        q.setParameter("id", id);
        return q.getResultList();
    }
    
    public GamacTipoGamac tipoGamacXCodProg(String codprog) {
        GamacTipoGamac tipo = null;
        String sentencia = "select t from GamacTipoGamac t where t.codProg = :parametro";
        Query q = em.createQuery(sentencia);
        q.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (GamacTipoGamac) q.getSingleResult();
        return tipo;
    }

    public List<GamacTipoGamac> listxDenominacion(String s) {
        //Query q = em.createQuery("select t from GamacTipoGamac t where t.tipoId.codProg = 'TP_DTRAN' ");
        
        if (s == null) {
            s = "";
        }
        
        try {
            Query q = em.createQuery("select t from GamacTipoGamac t where trim(t.nombre) like :nombre and t.tipoId.codProg = 'TP_DENMUN' and t.activo=1");
            q.setParameter("nombre", "%" + s.toUpperCase().trim() + "%");
            //q.setMaxResults(MAX_RES);
            q.setHint("eclipselink.batch.type", "IN");
            return q.getResultList(); 
        } catch (Exception e) {
            return null;
        }
        
    }
    
}
