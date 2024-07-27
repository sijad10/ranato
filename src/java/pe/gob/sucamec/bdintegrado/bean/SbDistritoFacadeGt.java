/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean; 

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbProvinciaGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbDistritoFacadeGt extends AbstractFacade<SbDistritoGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbDistritoFacadeGt() {
        super(SbDistritoGt.class);
    }
    
    public List<SbDistritoGt> lstDistritos(SbProvinciaGt parametro) {
        List<SbDistritoGt> respuesta = new ArrayList();
        String sentencia = "select d from SbDistritoGt d where d.provinciaId = :parametro ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", parametro);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<SbDistritoGt> lstDistritosxID(Long parametro) {
        List<SbDistritoGt> respuesta = new ArrayList();
        String sentencia = "select d from SbDistritoGt d where d.id = :parametro ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", parametro);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public SbDistritoGt findNombre(String nombre) {
        String sentencia = "select d from SbDistritoGt d where d.nombre = :nombre ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("nombre", nombre);
        return (SbDistritoGt) query.getSingleResult();
    }
    
    public List<SbDistritoGt> listarDistritos() {
        Query q = em.createQuery("SELECT D FROM SbDistritoGt D where D.activo = 1 order by d.provinciaId.departamentoId.nombre asc ");
        return q.getResultList();
    }
    
    public List<SbDistritoGt> obtenerUbigeo(String valor) {
        List<SbDistritoGt> respuesta = new ArrayList();
        String jpql = "select d from SbDistritoGt d where  d.nombre like :valor or d.provinciaId.nombre like :valor or d.provinciaId.departamentoId.nombre like :valor and d.activo=1 and d.provinciaId.activo = 1 and d.provinciaId.departamentoId.activo = 1 order by d.provinciaId.departamentoId.nombre, d.provinciaId.nombre, d.nombre ";
        Query q = getEntityManager().createQuery(jpql);
        q.setParameter("valor","%"+ valor+"%");
        respuesta = q.getResultList();
        return respuesta;
    }
    
    public List<SbDistritoGt> buscarDistritosEmpresaXDepartamentoSolicitudLocalPrincipal(Long empresaId, Long modalidadId, Long registroId) {
        //Solo el estado APROBADO TP_ECC_APR
        //Busca todos los distritos del Departamento que esta Autorizado con tipo de local PRINCIPAL para la Empresa y Modalidad
        String jpql = "select dist from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.activo = 1 and dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id not in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and (dist2.id in "
                //Los distritos de los locales PRINCIPAL que han sido Autorizados
                + " (select loc.distritoId.id "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl, SspResolucion reso "
                + " where r.activo = 1 and r.estadoId.codProg = 'TP_ECC_APR' "
                + " and r.empresaId.id = :empresaId "
                + " and r.tipoProId.id = :modalidadId "
                + " and r.id = lr.registroId.id "                
                + " and lr.activo = 1 "
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1 "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and tl.tipoLocalId.codProg = 'TP_LOC_PRIN'"
                + " and r.id = reso.registroId.id "                
                + " and reso.activo = 1 "
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) ) "
                //Une con los distritos de las Solicitudes que estan en espera de atención, no se toma los APROBADOS TP_ECC_APR, ya que en el
                //query de arriba se toman los aprobados Vigentes
                + " or dist2.id in "
                + " (select loc2.distritoId.id "
                + " from SspRegistro r2, SspLocalRegistro lr2, SspLocalAutorizacion loc2, SspTipoUsoLocal tl2 "
                + " where r2.activo = 1 and r2.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU','TP_ECC_APR','TP_ECC_FDP') "
                + " and r2.empresaId.id = :empresaId "
                + " and r2.tipoProId.id = :modalidadId "
                + " and r2.id = lr2.registroId.id ";
                //Que no sea el mismo Registro de consulta
                if (registroId != null){
                    jpql += " and r2.id != :registroId ";
                }                
                jpql += " and lr2.activo = 1 "
                + " and lr2.localId.id = loc2.id"
                + " and loc2.activo = 1 "
                + " and r2.id = tl2.registroId.id "                
                + " and loc2.id = tl2.localId.id "
                + " and tl2.activo = 1 "
                + " and tl2.tipoLocalId.codProg = 'TP_LOC_PRIN' "
                + " ) ) )";
                jpql += " order by dept.nombre, prov.nombre, dist.nombre ";
                
        /*String jpql = "select dist from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id not in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and dist2.id in "
                //Los distritos de las Solicitudes que estan en espera de atención
                 + " (select loc2.distritoId.id "
                + " from SspRegistro r2, SspLocalRegistro lr2, SspLocalAutorizacion loc2, SspTipoUsoLocal tl2 "
                + " where r2.activo = 1 and r2.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU') "
                + " and r2.empresaId.id = :empresaId "
                + " and r2.tipoProId.id = :modalidadId "
                + " and r2.id = lr2.registroId.id ";
                //Que no sea el mismo Registro de consulta
                if (registroId != null){
                    jpql += " and r2.id != :registroId ";
                }                
                jpql += " and lr2.activo = 1 "
                + " and lr2.localId.id = loc2.id"
                + " and loc2.activo = 1 "
                + " and r2.id = tl2.registroId.id "                
                + " and loc2.id = tl2.localId.id "
                + " and tl2.activo = 1 "
                + " and tl2.tipoLocalId.codProg = 'TP_LOC_PRIN' "
                + " ) ) "
                + " order by dept.nombre, prov.nombre, dist.nombre ";*/                
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }        
        if(modalidadId != null){
            q.setParameter("modalidadId", modalidadId);
        }        
        if(registroId != null){
            q.setParameter("registroId", registroId);
        }
        return q.getResultList();
    }

    public List<SbDistritoGt> buscarDistritosEmpresaXDepartamentoSolicitudCefoesp(Long empresaId, Long modalidadId, Long registroId) {
        //Solo el estado APROBADO TP_ECC_APR
        //Busca todos los distritos del Departamento que esta Autorizado con tipo de local PRINCIPAL para la Empresa y Modalidad
        String jpql = "select dist from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.activo = 1 and dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id not in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and (dist2.id in "
                //Los distritos de los locales PRINCIPAL que han sido Autorizados
                + " (select loc.distritoId.id "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl, SspResolucion reso "
                + " where r.activo = 1 and r.estadoId.codProg = 'TP_ECC_APR' "
                + " and r.empresaId.id = :empresaId "
                + " and r.tipoProId.id = :modalidadId "
                + " and r.id = lr.registroId.id "                
                + " and lr.activo = 1 "
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1 "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.id = reso.registroId.id "                
                + " and reso.activo = 1 "
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) ) "
                //Une con los distritos de las Solicitudes que estan en espera de atención, no se toma los APROBADOS TP_ECC_APR, ya que en el
                //query de arriba se toman los aprobados Vigentes
                + " or dist2.id in "
                + " (select loc2.distritoId.id "
                + " from SspRegistro r2, SspLocalRegistro lr2, SspLocalAutorizacion loc2, SspTipoUsoLocal tl2 "
                + " where r2.activo = 1 and r2.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU','TP_ECC_APR') "
                + " and r2.empresaId.id = :empresaId "
                + " and r2.tipoProId.id = :modalidadId "
                + " and r2.id = lr2.registroId.id ";
                //Que no sea el mismo Registro de consulta
                if (registroId != null){
                    jpql += " and r2.id != :registroId ";
                }                
                jpql += " and lr2.activo = 1 "
                + " and lr2.localId.id = loc2.id"
                + " and loc2.activo = 1 "
                + " and r2.id = tl2.registroId.id "                
                + " and loc2.id = tl2.localId.id "
                + " and tl2.activo = 1 "
                + " ) ) )";
                jpql += " order by dept.nombre, prov.nombre, dist.nombre ";
                
        /*String jpql = "select dist from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id not in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and dist2.id in "
                //Los distritos de las Solicitudes que estan en espera de atención
                 + " (select loc2.distritoId.id "
                + " from SspRegistro r2, SspLocalRegistro lr2, SspLocalAutorizacion loc2, SspTipoUsoLocal tl2 "
                + " where r2.activo = 1 and r2.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU') "
                + " and r2.empresaId.id = :empresaId "
                + " and r2.tipoProId.id = :modalidadId "
                + " and r2.id = lr2.registroId.id ";
                //Que no sea el mismo Registro de consulta
                if (registroId != null){
                    jpql += " and r2.id != :registroId ";
                }                
                jpql += " and lr2.activo = 1 "
                + " and lr2.localId.id = loc2.id"
                + " and loc2.activo = 1 "
                + " and r2.id = tl2.registroId.id "                
                + " and loc2.id = tl2.localId.id "
                + " and tl2.activo = 1 "
                + " and tl2.tipoLocalId.codProg = 'TP_LOC_PRIN' "
                + " ) ) "
                + " order by dept.nombre, prov.nombre, dist.nombre ";*/                
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }        
        if(modalidadId != null){
            q.setParameter("modalidadId", modalidadId);
        }        
        if(registroId != null){
            q.setParameter("registroId", registroId);
        }
        return q.getResultList();
    }

    
    public List<SbDistritoGt> buscarDistritosEmpresaXDepartamentoTipoLocalPrincipal(Long empresaId, Long modalidadId) {
        //Solo el estado APROBADO TP_ECC_APR
        //Busca todos los distritos del Departamento que esta Autorizado con tipo de local PRINCIPAL para la Empresa y Modalidad
        String jpql = "select dist from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.activo = 1 and dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and dist2.id in "
                //Los distritos de los locales PRINCIPAL que han sido Autorizados
                + " (select distinct loc.distritoId.id "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl, SspResolucion reso "
                + " where r.activo = 1 and r.estadoId.codProg = 'TP_ECC_APR' "
                + " and r.empresaId.id = :empresaId "
                + " and r.tipoProId.id = :modalidadId "
                + " and r.id = lr.registroId.id "                
                + " and lr.activo = 1 "
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1 "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and tl.tipoLocalId.codProg = 'TP_LOC_PRIN'"
                + " and r.id = reso.registroId.id "                
                + " and reso.activo = 1 "
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) "
                + " ) )";
                jpql += " order by dept.nombre, prov.nombre, dist.nombre ";
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }        
        if(modalidadId != null){
            q.setParameter("modalidadId", modalidadId);
        }        
        return q.getResultList();
    }


    public List<SbDistritoGt> buscarDistritosEmpresaXDepartamentoSolicitudLocalPrincipalxAdecuacion(Long empresaId, Long modalidadId, Long registroId, String nombreDepartamento) {
        //Solo el estado APROBADO TP_ECC_APR
        //Busca todos los distritos del Departamento que esta Autorizado con tipo de local PRINCIPAL para la Empresa y Modalidad
        String jpql = "select dist from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.activo = 1 and dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id "
                + " and dept.nombre = :nombreDepartamento " //que sea del Departamento enviado de la Resolución por Adecuación 1213
                + " and dept.id not in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and (dist2.id in "
                //Los distritos de los locales PRINCIPAL que han sido Autorizados
                + " (select loc.distritoId.id "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl, SspResolucion reso "
                + " where r.activo = 1 and r.estadoId.codProg = 'TP_ECC_APR' "
                + " and r.empresaId.id = :empresaId "
                + " and r.tipoProId.id = :modalidadId "
                + " and r.id = lr.registroId.id "                
                + " and lr.activo = 1 "
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1 "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and tl.tipoLocalId.codProg = 'TP_LOC_PRIN'"
                + " and r.id = reso.registroId.id "                
                + " and reso.activo = 1 "
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) ) "
                //Une con los distritos de las Solicitudes que estan en espera de atención, no se toma los APROBADOS TP_ECC_APR, ya que en el
                //query de arriba se toman los aprobados Vigentes
                + " or dist2.id in "
                + " (select loc2.distritoId.id "
                + " from SspRegistro r2, SspLocalRegistro lr2, SspLocalAutorizacion loc2, SspTipoUsoLocal tl2 "
                + " where r2.activo = 1 and r2.estadoId.codProg not in ('TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU','TP_ECC_APR','TP_ECC_APR','TP_ECC_FDP') "
                + " and r2.empresaId.id = :empresaId "
                + " and r2.tipoProId.id = :modalidadId "
                + " and r2.id = lr2.registroId.id ";
                //Que no sea el mismo Registro de consulta
                if (registroId != null){
                    jpql += " and r2.id != :registroId ";
                }                
                jpql += " and lr2.activo = 1 "
                + " and lr2.localId.id = loc2.id"
                + " and loc2.activo = 1 "
                + " and r2.id = tl2.registroId.id "                
                + " and loc2.id = tl2.localId.id "
                + " and tl2.activo = 1 "
                + " and tl2.tipoLocalId.codProg = 'TP_LOC_PRIN' "
                + " ) ) )";
                jpql += " order by dept.nombre, prov.nombre, dist.nombre ";                
                
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }        
        if(modalidadId != null){
            q.setParameter("modalidadId", modalidadId);
        }        
        if(registroId != null){
            q.setParameter("registroId", registroId);
        }
        
        if(nombreDepartamento != null){
            q.setParameter("nombreDepartamento", nombreDepartamento);
        }
        
        return q.getResultList();
    }

    
}
