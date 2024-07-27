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
import pe.gob.sucamec.bdintegrado.data.SspLocalAutorizacion;

/**
 *
 * @author locador772.ogtic
 */
@Stateless
public class SspLocalAutorizacionFacade extends AbstractFacade<SspLocalAutorizacion> {
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspLocalAutorizacionFacade() {
        super(SspLocalAutorizacion.class);
    }
    
    public List<SspLocalAutorizacion> listarLocalAutorizacionXidDistritoXIdEmpresa(Long idEmpresa, Long idDistrito) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc "
                + " where loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        respuesta = query.getResultList(); 
        return respuesta;
    }
    
    public List<SspLocalAutorizacion> listarLocalAutorizacionDistritoEmpresa(Long idEmpresa, Long idDistrito) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc "
                + " where loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        respuesta = query.getResultList(); 
        return respuesta;
    }
    
    public SspLocalAutorizacion verDatosLocalAutorizacionXiDLocal(Long idLocal) {
        String sentencia = "select loc from SspLocalAutorizacion loc where loc.activo = 1 and loc.id = :idLocal ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idLocal", idLocal);
        return (SspLocalAutorizacion) query.getSingleResult();
    }
    
    
    
    public int existeLocalRegistrado_x_Campos(Long idEmpresa, Long idDistrito, Long idTipoUbic, String direccion, String nroFisico) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc "
                + " where loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito "
                + " and loc.tipoUbicacionId.id = :idTipoUbic "
                + " and loc.direccion = :direccion "
                + " and loc.nroFisico = :nroFisico "
                + " ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        query.setParameter("idTipoUbic", idTipoUbic);
        query.setParameter("direccion", direccion);
        query.setParameter("nroFisico", nroFisico);
        respuesta = query.getResultList();
        return respuesta.size();
    }
    
    /*
    public List<SspLocalAutorizacion> listarLocalAutorizacionXidDistritoXIdEmpresa_Native(Long idEmpresa, Long idDistrito) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        Query query = em.createNativeQuery("SELECT LOC.* \n" 
                + " FROM BDINTEGRADO.SSP_REGISTRO R \n" 
                + " INNER JOIN BDINTEGRADO.SSP_LOCAL_REGISTRO LR ON (R.ID = LR.REGISTRO_ID)\n" 
                + " INNER JOIN BDINTEGRADO.SSP_LOCAL_AUTORIZACION LOC ON (LOC.ID = LR.LOCAL_ID)\n" 
                + " WHERE LOC.ACTIVO = 1 AND R.EMPRESA_ID = ?1 AND LOC.DISTRITO = ?2 ");
        query.setParameter(1, idEmpresa);
        query.setParameter(2, idDistrito.toString());
        respuesta = query.getResultList();
        return respuesta;
    }
*/
    
    public List<SspLocalAutorizacion> listarLocalAutorizacionDistritoEmpresaTipoLocal(Long idEmpresa, Long idDistrito, Long idTipoLocal, Long modalidadId) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where loc.activo = 1 "
                + " and r.activo = 1 "
                + " and r.tipoProId.id = :modalidadId "
                + " and lr.activo = 1 "
                + " and loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito "
                + " and tl.tipoLocalId.id = :idTipoLocal ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        query.setParameter("idTipoLocal", idTipoLocal);
        query.setParameter("modalidadId", modalidadId);
        respuesta = query.getResultList(); 
        return respuesta;
    }

     public List<SspLocalAutorizacion> listarLocalAutorizacionDistritoEmpresaCefoesp(Long idEmpresa, Long idDistrito, Long modalidadId) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where loc.activo = 1 "
                + " and r.activo = 1 "
                + " and r.tipoProId.id = :modalidadId "
                + " and lr.activo = 1 "
                + " and loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        query.setParameter("modalidadId", modalidadId);
        respuesta = query.getResultList(); 
        return respuesta;
    }
     
          public List<SspLocalAutorizacion> listarLocalAutorizacionDistritoPoligonoCefoesp(Long idEmpresa, Long idDistrito, Long modalidadId) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspPoligono lr, SspLocalAutorizacion loc"
                + " where loc.activo = 1 "
                + " and r.activo = 1 "
          //      + " and r.tipoProId.id = :modalidadId "
                + " and lr.activo = 1 "
                + " and loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                //+ " and r.id = tl.registroId.id "                
                //+ " and loc.id = tl.localId.id "
                //+ " and tl.activo = 1 "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        //query.setParameter("modalidadId", modalidadId);
        respuesta = query.getResultList(); 
        return respuesta;
    }

    
    public List<SspLocalAutorizacion> listarLocalAutorizacionDistritoEmpresaTipoLocalValida(Long idEmpresa, Long idDistrito, Long idTipoLocal, Long modalidadId, Long registroId) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where loc.activo = 1 "
                + " and r.activo = 1 "
                + " and r.tipoProId.id = :modalidadId "
                + " and lr.activo = 1 "
                + " and loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito "
                + " and tl.tipoLocalId.id = :idTipoLocal ";
                //Los Locales que ya han sido registrados anteriormente ver que se toma tambien TP_ECC_APR porque el 2do query de abajo
                //filtra las resoluciones vigentes
                sentencia += " and (loc.id not in (select loc2.id "
                + " from SspRegistro r2, SspLocalRegistro lr2, SspLocalAutorizacion loc2, SspTipoUsoLocal tl2 "
                + " where r2.activo = 1 and r2.estadoId.codProg not in ('TP_ECC_FDP','TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU','TP_ECC_APR') "
                + " and r2.empresaId.id = :idEmpresa "
                + " and r2.tipoProId.id = :modalidadId "
                + " and loc2.distritoId.id = :idDistrito "
                + " and r2.id = lr2.registroId.id ";
                //Que no sea el mismo Registro de consulta
                if (registroId != null){
                    sentencia += " and r2.id != :registroId ";
                }                
                sentencia += " and lr2.activo = 1 "
                + " and lr2.localId.id = loc2.id"
                + " and loc2.activo = 1 "
                + " and r2.id = tl2.registroId.id "                
                + " and loc2.id = tl2.localId.id "
                + " and tl2.activo = 1 "
                + " and tl2.tipoLocalId.id = :idTipoLocal ) "
                        
                 //Los locales que ya han sido Autorizados vigentes
                + " and loc.id not in (select loc.id "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl, SspResolucion reso "
                + " where r.activo = 1 and r.estadoId.codProg = 'TP_ECC_APR' "
                + " and r.empresaId.id = :idEmpresa "
                + " and r.tipoProId.id = :modalidadId "
                + " and loc.distritoId.id = :idDistrito "
                + " and r.id = lr.registroId.id "                
                + " and lr.activo = 1 "
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1 "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and tl.tipoLocalId.id = :idTipoLocal "
                + " and r.id = reso.registroId.id "                
                + " and reso.activo = 1 "
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) ) ) ";
                
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        query.setParameter("idTipoLocal", idTipoLocal);
        query.setParameter("modalidadId", modalidadId);
        if(registroId != null){
            query.setParameter("registroId", registroId);
        }    
        respuesta = query.getResultList(); 
        return respuesta;
    }
 
    public List<SspLocalAutorizacion> listarLocalAutorizacionDistritoEmpresaCefoesp(Long idEmpresa, Long idDistrito,  Long modalidadId, Long registroId) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where loc.activo = 1 "
                + " and r.activo = 1 "
                + " and r.tipoProId.id = :modalidadId "
                + " and lr.activo = 1 "
                + " and loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito ";
                //Los Locales que ya han sido registrados anteriormente ver que se toma tambien TP_ECC_APR porque el 2do query de abajo
                //filtra las resoluciones vigentes
                sentencia += " and (loc.id not in (select loc2.id "
                + " from SspRegistro r2, SspLocalRegistro lr2, SspLocalAutorizacion loc2, SspTipoUsoLocal tl2 "
                + " where r2.activo = 1 and r2.estadoId.codProg not in ('TP_ECC_FDP','TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU','TP_ECC_APR') "
                + " and r2.empresaId.id = :idEmpresa "
                + " and r2.tipoProId.id = :modalidadId "
                + " and loc2.distritoId.id = :idDistrito "
                + " and r2.id = lr2.registroId.id ";
                //Que no sea el mismo Registro de consulta
                if (registroId != null){
                    sentencia += " and r2.id != :registroId ";
                }                
                sentencia += " and lr2.activo = 1 "
                + " and lr2.localId.id = loc2.id"
                + " and loc2.activo = 1 "
                + " and r2.id = tl2.registroId.id "                
                + " and loc2.id = tl2.localId.id "
                + " and tl2.activo = 1 ) "
                        
                 //Los locales que ya han sido Autorizados vigentes
                + " and loc.id not in (select loc.id "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl, SspResolucion reso "
                + " where r.activo = 1 and r.estadoId.codProg = 'TP_ECC_APR' "
                + " and r.empresaId.id = :idEmpresa "
                + " and r.tipoProId.id = :modalidadId "
                + " and loc.distritoId.id = :idDistrito "
                + " and r.id = lr.registroId.id "                
                + " and lr.activo = 1 "
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1 "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.id = reso.registroId.id "                
                + " and reso.activo = 1 "
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) ) ) ";
                
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        query.setParameter("modalidadId", modalidadId);
        if(registroId != null){
            query.setParameter("registroId", registroId);
        }    
        respuesta = query.getResultList(); 
        return respuesta;
    }
 

    public int existeLocalAutorizacionDistritoEmpresaTipoLocalRepetido(Long idEmpresa, Long idDistrito, Long idTipoLocal, Long modalidadId, Long idTipoUbic, String direccion, String nroFisico) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where loc.activo = 1 and r.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU') "
                + " and r.activo = 1 "
                + " and r.tipoProId.id = :modalidadId "
                + " and lr.activo = 1 "
                + " and loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito "
                + " and tl.tipoLocalId.id = :idTipoLocal "
                + " and loc.tipoUbicacionId.id = :idTipoUbic "
                + " and loc.direccion = :direccion "
                /*+ " and loc.nroFisico = :nroFisico "*/;
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        query.setParameter("idTipoLocal", idTipoLocal);
        query.setParameter("modalidadId", modalidadId);
        query.setParameter("idTipoUbic", idTipoUbic);
        query.setParameter("direccion", direccion);
//        query.setParameter("nroFisico", nroFisico);
        respuesta = query.getResultList();
        return respuesta.size();
    }
    
       public int existeLocalAutorizacionDistritoEmpresaCefoesp(Long idEmpresa, Long idDistrito, Long modalidadId, Long idTipoUbic, String direccion) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where loc.activo = 1 and r.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU') "
                + " and r.activo = 1 "
                + " and r.tipoProId.id = :modalidadId "
                + " and lr.activo = 1 "
                + " and loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.empresaId.id = :idEmpresa "
                + " and loc.distritoId.id = :idDistrito "
                + " and loc.tipoUbicacionId.id = :idTipoUbic "
                + " and loc.direccion = :direccion "
                //+ " and loc.nroFisico = :nroFisico "
                + "";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        query.setParameter("modalidadId", modalidadId);
        query.setParameter("idTipoUbic", idTipoUbic);
        query.setParameter("direccion", direccion);
        //query.setParameter("nroFisico", nroFisico);
        respuesta = query.getResultList();
        return respuesta.size();
    }
    
    
    /*QUERYS PARA PROTECCION PERSONAL*/
    public List<SspLocalAutorizacion> listarLocalAutorizacionEmpresaTipoLocal(Long idEmpresa,   Long idTipoLocal, Long modalidadId , Long idDistrito) {
        List<SspLocalAutorizacion> respuesta = new ArrayList();
        String sentencia = "select distinct loc "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where loc.activo = 1 "
                + " and r.activo = 1 "
                + " and r.tipoProId.id = :modalidadId "
                + " and lr.activo = 1 "
                + " and loc.activo = 1 "
                + " and r.id = lr.registroId.id "                
                + " and loc.id = lr.localId.id "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and r.empresaId.id = :idEmpresa " 
                + " and loc.distritoId.id = :idDistrito "
                + " and tl.tipoLocalId.id = :idTipoLocal ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("idDistrito", idDistrito);
        query.setParameter("idTipoLocal", idTipoLocal);
        query.setParameter("modalidadId", modalidadId);
        respuesta = query.getResultList(); 
        return respuesta;
    }
    /*QUERYS PARA PROTECCION PERSONAL*/
    
}
