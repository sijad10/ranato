/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.SspCarteraCliente;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspCarteraClienteFacade extends AbstractFacade<SspCarteraCliente> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspCarteraClienteFacade() {
        super(SspCarteraCliente.class);
    }
    
    public List<SspCarteraCliente> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SspCarteraCliente s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public SspCarteraCliente buscarCarteraCliById(Long carteraId) {
        Query q = em.createQuery("select s from SspCarteraCliente s where s.id = :carteraId ");
        q.setParameter("carteraId", carteraId);
        
        if(!q.getResultList().isEmpty()){
            return (SspCarteraCliente) q.getResultList().get(0);
        }
        
        return null;
    }
    
    public List<Map> buscarCarteraClientes(HashMap nmap) {
        try {
            String jpql = "select distinct s.id as ID, s.tipoOpeId.nombre AS TIPO_OPE_NOMBRE, s.tipoOpeId.codProg AS TIPO_OPE_CODPROG, "
                          + " reprAdmin.rznSocial AS ADM_RZN_SOCIAL, raDoc.nombre as ADM_TIPO_DOC, reprAdmin.numDoc AS ADM_NUM_DOC, reprAdmin.ruc AS ADM_RUC, CONCAT(reprAdmin.apePat, ' ', reprAdmin.apeMat, ' ', reprAdmin.nombres) AS ADM_NOMBRES, "
                          + " s.clienteId.rznSocial AS CLI_RZN_SOCIAL, clDoc.nombre as CLI_TIPO_DOC, s.clienteId.numDoc AS CLI_NUM_DOC, s.clienteId.ruc AS CLI_RUC, CONCAT(s.clienteId.apePat, ' ', s.clienteId.apeMat, ' ', s.clienteId.nombres) AS CLI_NOMBRES, "
                          + " s.estadoId.nombre ESTADO_NOMBRE, s.estadoId.codProg ESTADO_CODPROG, s.fechaInicio AS FECHA_INICIO, s.fechaFin AS FECHA_FIN, s.expediente AS EXPEDIENTE, "
                          + " s.flagEvaluacion AS FLAG_EVALUACION, s.fechaActualizacion AS FECHA_DECLARACION, s.detalleMotivo AS DETALLE_MOTIVO "
                          + " from SspCarteraCliente s "
                          + " left join s.reprAdminId reprAdmin"
                          + " left join reprAdmin.tipoDoc raDoc"
                          + " left join s.clienteId.tipoDoc clDoc"
                          + " left join s.tipoSeguridadList l"
                          + " where s.activo = 1 and l.activo = 1 and s.administradoId.id = :administrado ";

            if(nmap.get("tipoBuscarPor") != null){
                switch(Integer.parseInt(nmap.get("tipoBuscarPor").toString())){
                    case 1:
                            jpql += " and s.expediente = :filtro";
                            break;
                    case 2:
                            jpql += " and ( s.clienteId.rznSocial like :filtro OR concat(s.clienteId.apePat, ' ', s.clienteId.apeMat, ' ', s.clienteId.nombres ) like :filtro )";
                            break;
                    case 3:
                            jpql += " and ( s.clienteId.ruc = :filtro )";
                            break;
                    case 4:
                            jpql += " and ( l.id = :filtro )";
                            break;
                    case 5:
                            jpql += " and ( s.id = :filtro )";
                            break;
                }
            }
            if(nmap.get("vigEventos") != null){
                if((boolean) nmap.get("vigEventos")){
                    jpql += " and s.evento = 1 ";
                }else{
                    jpql += " and s.evento = 0 ";
                }
            }
            if(nmap.get("fechaIni") != null){
                jpql += " and FUNC('trunc',s.fechaInicio) >= FUNC('trunc',:fechaIni)";
            }
            if(nmap.get("fechaFin") != null){
                jpql += " and FUNC('trunc',s.fechaFin) <= FUNC('trunc',:fechaFin)";
            }
            if(nmap.get("tipoDeclaracion") != null){
                jpql += " and s.tipoOpeId.id = :tipoDeclaracion ";
            }
            if(nmap.get("estadoCartera") != null){
                jpql += " and s.estadoId.id = :estadoCartera ";
            }
            jpql += " order by s.id desc";

            Query q = em.createQuery(jpql);
            q.setParameter("administrado", (Long) nmap.get("administrado") );
            if(nmap.get("tipoBuscarPor") != null){
                switch(Integer.parseInt(nmap.get("tipoBuscarPor").toString())){
                    case 1:
                    case 5:
                            q.setParameter("filtro", Long.parseLong(""+nmap.get("filtro")) );
                            break;
                    case 2:
                            q.setParameter("filtro", "%" + nmap.get("filtro") + "%");
                            break;
                    case 3:
                            q.setParameter("filtro", ""+ nmap.get("filtro") );
                            break;
                    case 4:
                            q.setParameter("filtro", Long.parseLong(""+nmap.get("tipoModalidad")) );
                            break;
                }
            }
            if(nmap.get("fechaIni") != null){
                q.setParameter("fechaIni", (Date) nmap.get("fechaIni"), TemporalType.DATE );
            }
            if(nmap.get("fechaFin") != null){
                q.setParameter("fechaFin", (Date) nmap.get("fechaFin"), TemporalType.DATE );
            }
            if(nmap.get("tipoDeclaracion") != null){
                q.setParameter("tipoDeclaracion", (Long) nmap.get("tipoDeclaracion") );
            }
            if(nmap.get("estadoCartera") != null){
                q.setParameter("estadoCartera", (Long) nmap.get("estadoCartera") );
            }
            q.setHint("eclipselink.result-type", "Map");
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;   
    }
    
    public List<SspCarteraCliente> buscarContratoClientes(HashMap nmap) {
        String jpql = "select s "
                      + " from SspCarteraCliente s "
                      + " left join s.tipoSeguridadList l"
                      + " where s.activo = 1 and l.activo = 1 "
                      + " and s.tipoOpeId.codProg in ('TP_TDEC_NUEVO','TP_TDEC_ADENDA') "   // Nuevo y Adenda de Contrato
                      + " and s.estadoId.codProg = 'TP_ECC_APR' "
                      + " and ( s.fechaFin is null or FUNC('TRUNC',s.fechaFin) >= current_date) " ;  
        
        if(nmap.get("tipoBuscarPor") != null){
            switch(Integer.parseInt(nmap.get("tipoBuscarPor").toString())){
                case 1:
                        jpql += " and s.expediente = :filtro";
                        break;
                case 2:
                        jpql += " and ( s.clienteId.rznSocial like :filtro OR concat(s.clienteId.apePat, ' ', s.clienteId.apeMat, ' ', s.clienteId.nombres ) like :filtro )";
                        break;
                case 3:
                        jpql += " and ( s.clienteId.ruc = :filtro )";
                        break;
                case 4:
                        jpql += " and ( l.id = :filtro )";
                        break;
            }
        }
        Query q = em.createQuery(jpql);
        if(nmap.get("tipoBuscarPor") != null){
            switch(Integer.parseInt(nmap.get("tipoBuscarPor").toString())){
                case 1:
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("filtro")) );
                        break;
                case 2:
                        q.setParameter("filtro", "%" + nmap.get("filtro") + "%");
                        break;
                case 3:
                        q.setParameter("filtro", ""+ nmap.get("filtro") );
                        break;
                case 4:
                        q.setParameter("filtro", Long.parseLong(""+nmap.get("tipoModalidad")) );
                        break;
            }
        }
        return q.setMaxResults(200).getResultList();
    }
    
    public String obtenerModalidadByIdCarteraCli(Long id) {
        String modalidades = null;
        Query q = em.createNativeQuery("SELECT LISTAGG(M.NOMBRE, ' / ') " +
                                "    WITHIN GROUP (ORDER BY M.ID) AS MODALIDAD " +
                                "  FROM BDINTEGRADO.SSP_CARTERA_CLIENTE CC " +
                                "  INNER JOIN BDINTEGRADO.SSP_CARTERA_MODALIDAD CM ON CM.CARTERA_ID = CC.ID " +
                                "  INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD M ON M.ID = CM.TIPO_ID " +
                                "  WHERE CC.ACTIVO = 1 AND CC.ID = ?1 " );
        q.setParameter(1, id);
        if(!q.getResultList().isEmpty()){
            modalidades = (String) q.getResultList().get(0);        
        }
        
        return modalidades;
    }
    
    public int contContratosConObservaciones(String doc) {
        int cont = 0;
        String jpql = " select count(s)"
                    + " from SspCarteraCliente s "
                    + " where s.activo = 1 "
                    + " and (s.administradoId.ruc = :doc or s.administradoId.numDoc = :doc ) "
                    + " and s.estadoId.codProg = 'TP_ECC_OBS' ";
        
        Query q = em.createQuery(jpql);
        q.setParameter("doc", doc);
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contContratosRepetidosActivos(Long contratoId, Long clienteId, Date fechaInicio, Date fechaFin, String local) {
        int cont = 0;
        String jpql = " select count(s)"
                    + " from SspCarteraCliente s "
                    + " join s.sspLugarServicioList l"
                    + " where s.activo = 1 "
                    + " and s.estadoId.codProg not in ('TP_ECC_NDR','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_FDP','TP_ECC_DES') "
                    + " and ( s.fechaFin is null or s.fechaFin >= current_date )"
                    + " and s.clienteId.id = :clienteId "                    
                    + " and s.fechaInicio = :fechaInicio "
                    + " and s.fechaFin = :fechaFin "
                    + " and l.direccion like :local";
        if(contratoId != null){
            jpql += " and s.id != :contratoId ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("clienteId", clienteId);
        q.setParameter("fechaInicio", fechaInicio, TemporalType.DATE);
        q.setParameter("fechaFin", fechaFin, TemporalType.DATE);
        q.setParameter("local", "%"+local.trim()+"%");
        if(contratoId != null){
            q.setParameter("contratoId", contratoId);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
}
