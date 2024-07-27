/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppLocal;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppLocalFacade extends AbstractFacade<EppLocal> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppLocalFacade() {
        super(EppLocal.class);
    }

    public List<EppLocal> buscarLocalByDireccionByRznSocial(String filtro, Long empresaId) {
        try {
            String jpql = "SELECT distinct(p) FROM EppLocal p "+
                          " LEFT JOIN p.eppRegistroList l " +
                          " WHERE p.activo = 1 " +
                          " and l.tipoProId.codProg in ('TP_PRTUP_AFAB','TP_PRTUP_PLA') " +
                          " and (l.eppResolucion.fechaFin is null or l.eppResolucion.fechaFin >= current_date) " ;
            if(filtro != null && !filtro.isEmpty()){
                jpql += " and ( p.direccion like :direccion OR p.personaId.rznSocial like :rznSocial ) ";
            }
            if(empresaId != null){
                jpql += " and l.empresaId.id = :empresaId ";
            }
            
            Query q = em.createQuery(jpql);
            if(filtro != null && !filtro.isEmpty()){
                q.setParameter("direccion", "%"+filtro + "%");
                q.setParameter("rznSocial", "%"+filtro + "%");
            }
            if(empresaId != null){
                q.setParameter("empresaId", empresaId);
            }
            
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EppLocal> buscarLocalByEmpresa(Long empresaId) {
        try {
            String jpql = "SELECT distinct(p) FROM EppLocal p "+
                          " LEFT JOIN p.eppRegistroList l " +
                          " WHERE p.activo = 1  " +
                          " and l.tipoProId.codProg in ('TP_PRTUP_AFAB','TP_PRTUP_PLA') " +
                          " and (l.eppResolucion.fechaFin is null or l.eppResolucion.fechaFin >= current_date) " ;
            
            if(empresaId != null){
                jpql += " and l.empresaId.id = :empresaId ";
            }
            
            Query q = em.createQuery(jpql);
            if(empresaId != null){
                q.setParameter("empresaId", empresaId);
            }
            
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Map> buscarLocalByEmpresaMap(Long empresaId, boolean esFabrica) {
        try {
            String jpql = "SELECT distinct 1 AS TIPO, p.id AS ID, p.personaId.rznSocial AS RZN_SOCIAL, "+
                          " CONCAT(p.ubigeoId.provinciaId.departamentoId.nombre, ' / ', p.ubigeoId.provinciaId.nombre, ' / ', p.ubigeoId.nombre) as UBIGEO, " + 
                          " p.direccion AS DIRECCION, 0 as CONTRATO_ID " +
                          " FROM EppLocal p "+
                          " LEFT JOIN p.eppRegistroList l " +
                          " WHERE p.activo = 1  " +
                          " and l.tipoProId.codProg in ('TP_PRTUP_AFAB','TP_PRTUP_PLA') " +
                          " and (l.eppResolucion.fechaFin is null or l.eppResolucion.fechaFin >= current_date) " ;
            
            String jpqlAlq = " select distinct 2 AS TIPO, l.id AS ID, p.arrendatariaId.rznSocial AS RZN_SOCIAL, "+
                            " CONCAT(l.ubigeoId.provinciaId.departamentoId.nombre, ' ', l.ubigeoId.provinciaId.nombre, ' ', l.ubigeoId.nombre) as UBIGEO, " +
                            " l.direccion AS DIRECCION, p.id as CONTRATO_ID "+
                            " from EppContratoAlqPolv p "+
                            " LEFT JOIN p.registro.eppLocalList l " +
                            " where p.activo = 1 and l.activo = 1 and l.direccion is not null and p.registro.eppResolucion.activo = 1 " +
                            " and p.registro.tipoProId.codProg in ('TP_PRTUP_AFAB','TP_PRTUP_PLA') "+
                            " and ( p.registro.eppResolucion.fechaFin is null or p.registro.eppResolucion.fechaFin >= current_date) " +
                            " and ( p.fecfinAlq >= current_date) ";
            
            if(empresaId != null){
                jpql += " and l.empresaId.id = :empresaId ";
                if(esFabrica){
                    jpqlAlq += " and p.arrendadoraId.id = :empresaId ";    
                }else{
                    jpqlAlq += " and p.arrendatariaId.id = :empresaId ";
                }
            }
            jpql = jpql + " UNION " + jpqlAlq;
            
            Query q = em.createQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            if(empresaId != null){
                q.setParameter("empresaId", empresaId);
            }
            
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Map> buscarLocalByDireccionByRznSocialMap(String filtro, Long empresaId) {
        try {
            String jpql = "SELECT distinct 1 AS TIPO, p.id AS ID, p.personaId.rznSocial AS RZN_SOCIAL, "+
                          " CONCAT(p.ubigeoId.provinciaId.departamentoId.nombre, ' / ', p.ubigeoId.provinciaId.nombre, ' / ', p.ubigeoId.nombre) as UBIGEO, " + 
                          " p.direccion AS DIRECCION, 0 as CONTRATO_ID " +
                          " FROM EppLocal p "+
                          " LEFT JOIN p.eppRegistroList l " +
                          " WHERE p.activo = 1 " +
                          " and l.tipoProId.codProg in ('TP_PRTUP_AFAB','TP_PRTUP_PLA') " +
                          " and (l.eppResolucion.fechaFin is null or l.eppResolucion.fechaFin >= current_date) " ;
            
            String jpqlAlq = " select distinct 2 AS TIPO, l.id AS ID, p.arrendatariaId.rznSocial AS RZN_SOCIAL, "+
                            " CONCAT(l.ubigeoId.provinciaId.departamentoId.nombre, ' ', l.ubigeoId.provinciaId.nombre, ' ', l.ubigeoId.nombre) as UBIGEO, " +
                            " l.direccion AS DIRECCION, p.id as CONTRATO_ID "+
                            " from EppContratoAlqPolv p "+
                            " LEFT JOIN p.registro.eppLocalList l " +
                            " where p.activo = 1 and l.activo = 1 and l.direccion is not null and p.registro.eppResolucion.activo = 1 " +
                            " and p.registro.tipoProId.codProg in ('TP_PRTUP_AFAB','TP_PRTUP_PLA') "+
                            " and ( p.registro.eppResolucion.fechaFin is null or p.registro.eppResolucion.fechaFin >= current_date) " +
                            " and ( p.fecfinAlq >= current_date) ";

            if(filtro != null && !filtro.isEmpty()){
                jpql += " and ( p.direccion like :direccion OR p.personaId.rznSocial like :rznSocial ) ";
                jpqlAlq += " and ( l.direccion like :direccion OR p.arrendatariaId.rznSocial like :rznSocial ) ";
            }
            if(empresaId != null){
                jpql += " and l.empresaId.id = :empresaId ";
                jpqlAlq += " and p.arrendatariaId.id = :empresaId ";
            }
            jpql = jpql + " UNION " + jpqlAlq;
            
            Query q = em.createQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            
            if(filtro != null && !filtro.isEmpty()){
                q.setParameter("direccion", "%"+filtro + "%");
                q.setParameter("rznSocial", "%"+filtro + "%");
            }
            if(empresaId != null){
                q.setParameter("empresaId", empresaId);
            }
            
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
