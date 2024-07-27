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
import pe.gob.sucamec.bdintegrado.data.SspCartaFianza;
/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspCartaFianzaFacade extends AbstractFacade<SspCartaFianza>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspCartaFianzaFacade() {
        super(SspCartaFianza.class);
    }

    public List<SspCartaFianza> buscarCartasFianzasVigentes(Long empresaId) {
        //and r.estadoId.codProg in ('TP_ECC_CRE','TP_ECC_OBS','TP_ECC_TRA') 
        String jpql = "select distinct cf "
                + " from SspRegistro r, SspCartaFianza cf"
                + " where r.activo = 1 "
                + " and r.empresaId.id = :empresaId "
                + " and r.cartaFianzaId.id = cf.id "                
                + " and cf.activo = 1"
                + " and func('trunc',current_date) between func('trunc',cf.vigenciaInicio) and func('trunc',cf.vigenciaFin) ";
        jpql += " order by cf.vigenciaFin desc ";
        
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }
        return q.getResultList();
    }
    
    public SspCartaFianza buscarArchivoCartaFianza(Long cartaFianzaId) {
        Query q = em.createQuery(
                "select c from SspCartaFianza c where c.activo = 1 and c.id = :cartaFianzaId");
        q.setParameter("cartaFianzaId", cartaFianzaId);
        if (q.setMaxResults(1).getResultList().isEmpty()) {
            return null;
        }
        return (SspCartaFianza) q.getResultList().get(0);
    }
    
    public SspCartaFianza buscarCartaFianzaPorNumero(Long empresaId, String numeroCarta, Long registroId) {
        String jpql = "select distinct cf "
                + " from SspRegistro r, SspCartaFianza cf"
                + " where r.activo = 1 "
                + " and r.empresaId.id = :empresaId "
                + " and cf.numero = :numeroCarta "
                + " and r.cartaFianzaId.id = cf.id "                
                + " and cf.activo = 1";
        
        //Que no sea el mismo Registro de consulta
        if(registroId != null){
            jpql += " and r.id != :registroId ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("empresaId", empresaId);
        q.setParameter("numeroCarta", numeroCarta);
        if(registroId != null){
            q.setParameter("registroId", registroId);
        }
        if (q.setMaxResults(1).getResultList().isEmpty()) {
            return null;
        }
        return (SspCartaFianza) q.getResultList().get(0);
    }
    
    public List<SspCartaFianza> buscarCartasFianzasSinRelacionRegistros() {
        String jpql = "select cf "
                + " from SspCartaFianza cf where cf.activo = 1 " 
                + " and cf.id not in (select distinct r.cartaFianzaId.id from SspRegistro r where r.activo = 1) ";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }
        
}
