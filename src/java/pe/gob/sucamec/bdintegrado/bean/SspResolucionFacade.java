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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.ejb.Stateless;
import net.sf.jasperreports.data.cache.LongArrayValues;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SspResolucion;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.gssp.jsf.GsspSolicitudAutorizacionSPPController;
/**
 *
 * @author ocastillo
 */
@Stateless
public class SspResolucionFacade extends AbstractFacade<SspResolucion> {
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspResolucionFacade() {
        super(SspResolucion.class);
    }

    public SspResolucionFacade(EntityManager em, Class<SspResolucion> entityClass) {
        super(entityClass);
        this.em = em;
    }
    
    public List<SspResolucion> buscarResolucionVigenteEmpresaXDepartamentoTipoLocalPrincipal(Long empresaId, Long distritoId, Long modalidadId) {
        //Solo el estado APROBADO TP_ECC_APR
        //Busca la ResoluciÃ³n del Departamento Principal con estado APROBADO
        String jpql = "select reso "
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
                //Busca todos los distritos del Departamento
                + " and loc.distritoId.id in (select dist.id from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and dist2.id = :distritoId ) )";
        jpql += " order by reso.fechaFin desc ";
        
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }        
        if(distritoId != null){
            q.setParameter("distritoId", distritoId);
        }
        if(modalidadId != null){
            q.setParameter("modalidadId", modalidadId);
        }        
        return q.getResultList();
    }

    public SspResolucion buscarResolucionPorRegistro(Long registroId) {
        String jpql = "select s "                      
                      + " from SspResolucion s "
                      + " where s.activo = 1 "
                      + " and s.registroId.id = :registroId ";
        if(registroId != null){
            jpql += " and s.id != :registroId ";
        }
        jpql += " order by s.fecha desc ";
        Query q = em.createQuery(jpql);        
        
        if(registroId != null){
            q.setParameter("registroId", registroId );
        }
        if(!q.getResultList().isEmpty()){
            return (SspResolucion) q.getResultList().get(0);
        }
        return null;
    }
    
    public SspResolucion buscarResolucionPorNroResolucion(String nroResolucion) {
        String jpql = "select s "                      
                      + " from SspResolucion s "
                      + " where s.activo = 1 "
                      + " and s.numero = :numero ";
     
        jpql += " order by s.fecha desc ";
        Query q = em.createQuery(jpql);        
        q.setParameter("numero", nroResolucion );
       
        if(!q.getResultList().isEmpty()){
            return (SspResolucion) q.getResultList().get(0);
        }
        return null;
    }
    
    
    /*QUERYS PARA PROTECCION PERSONAL*/
    public List<SspResolucion> buscarResolucionVigenteEmpresaTipoLocalPrincipalAprobado(Long empresaId, Long modalidadId) {
        //Solo el estado APROBADO TP_ECC_APR
        //Busca la ResoluciÃ³n del con estado APROBADO
        String jpql = "select reso "
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
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) ";
        jpql += " order by reso.fechaFin desc ";
        
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }         
        if(modalidadId != null){
            q.setParameter("modalidadId", modalidadId);
        }        
        return q.getResultList();
    }
    
//    public List<ArrayRecord> buscarAutorizacion_vistaDISCA(String nro_resolucion, String ruc) {
//    public List<ArrayRecord> buscarAutorizacion_vistaDISCA(String nro_resolucion) {
//    public Object[] buscarAutorizacion_vistaDISCA(String nro_resolucion) {
    public HashMap buscarAutorizacion_vistaDISCA(String nro_resolucion) {
//    public Object buscarAutorizacion_vistaDISCA(String nro_resolucion) {
        //                "SELECT * FROM RMA1369.RESOLUCION@SUCAMEC WHERE COD_AREA=3 AND COD_SUB_AREA=1 AND COD_TRA IN ('1','2') AND TIP_USR = 1 AND FEC_VEN >= SYSDATE AND COD_USR LIKE ?1 "
        
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' "
        );
        q.setParameter(1, nro_resolucion);
 
//         q.setHint("eclipselink.result-type", "List");
         q.setHint("eclipselink.result-type", "Map");
//        return 
        List<ArrayRecord> la = q.setMaxResults(1).getResultList();
        
        HashMap<String, String> hashMap = new HashMap<>();
 
        // For-each loop for iteration
        for (ArrayRecord  str : la) {
            //System.out.println(str.get("RUC")); 
            hashMap.put("ID", (str.get("ID") != null) ? str.get("ID").toString() : "");
            hashMap.put("RUC", (str.get("RUC") != null) ? str.get("RUC").toString() : "");
            hashMap.put("RZN_SOC", (str.get("RZN_SOC") != null) ? str.get("RZN_SOC") .toString() : "");
            hashMap.put("REPRESENTANTE", (str.get("REPRESENTANTE") != null) ? str.get("REPRESENTANTE").toString(): "");
            hashMap.put("DOC_REPRESENTANTE",(str.get("DOC_REPRESENTANTE") != null) ? str.get("DOC_REPRESENTANTE").toString() : "" );
            hashMap.put("NRO_RD", (str.get("NRO_RD") != null) ? str.get("NRO_RD").toString(): "");
            
            String fecha_emi = null;
            String fecha_ven = null;
            fecha_emi = (str.get("FEC_EMI").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_EMI")) : null;
            fecha_ven = (str.get("FEC_VEN").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_VEN")) : null;
            hashMap.put("FEC_EMI", (fecha_emi != null) ? fecha_emi: "");
            hashMap.put("FEC_VEN", (fecha_ven != null) ? fecha_ven: "");
            hashMap.put("PRINCIPAL", (str.get("PRINCIPAL") != null) ? str.get("PRINCIPAL").toString(): "");
            hashMap.put("DOM_ACT", (str.get("DOM_ACT") != null) ? str.get("DOM_ACT").toString(): "");
            hashMap.put("NOM_DPTO", (str.get("NOM_DPTO") != null) ? str.get("NOM_DPTO").toString(): "");
            hashMap.put("NOM_PROV", (str.get("NOM_PROV") != null) ? str.get("NOM_PROV").toString(): "");
            hashMap.put("NOM_DIST", (str.get("NOM_DIST") != null) ? str.get("NOM_DIST").toString(): "");
            hashMap.put("TIP_MOD", (str.get("TIP_MOD") != null) ? str.get("TIP_MOD") .toString(): "");
            hashMap.put("MODALIDAD", (str.get("MODALIDAD") != null) ? str.get("MODALIDAD").toString(): "");
            hashMap.put("DES_MOD", (str.get("DES_MOD") != null) ? str.get("DES_MOD").toString(): "");
            hashMap.put("ESTADO", (str.get("ESTADO") != null) ? str.get("ESTADO").toString(): "");
            hashMap.put("COD_ARM", (str.get("COD_ARM") != null) ? str.get("COD_ARM").toString(): "");
            hashMap.put("DES_TRAM", (str.get("DES_TRAM") != null) ? str.get("DES_TRAM") .toString(): "");
            hashMap.put("NRO_FISICO", (str.get("NRO_FISICO") != null) ? str.get("NRO_FISICO") .toString(): "");
            hashMap.put("NRO_EXP", (str.get("NRO_EXP") != null) ? str.get("NRO_EXP").toString(): "");
            hashMap.put("MOD_EMP_DISCA", (str.get("MOD_EMP_DISCA") != null) ? str.get("MOD_EMP_DISCA").toString(): "");
            hashMap.put("NRO_RD_DISCA", (str.get("NRO_RD_DISCA") != null) ? str.get("NRO_RD_DISCA").toString(): "");
            hashMap.put("ANO_RD_DISCA", (str.get("ANO_RD_DISCA") != null) ? str.get("ANO_RD_DISCA").toString(): "");
            hashMap.put("REGISTRO_ID", (str.get("REGISTRO_ID") != null) ? str.get("REGISTRO_ID").toString(): ""); 
            hashMap.put("RESOL_FORMATEADO", (str.get("RESOL_FORMATEADO") != null) ? str.get("RESOL_FORMATEADO").toString(): "");
//            hashMap.put(str[0], str.toString());
//                    for (Map.Entry<String, String> entry : str.entrySet()) {
//                        System.out.println(entry.getKey() + " : "
//                                            + entry.getValue());
//                     }
        }

//        Object sr = q.getSingleResult(); 
         
        return hashMap;
    }
    
     
    public List<Object[]> list_autocomplete_vistaDISCA(String nro_resolucion, String ruc) {
        if (nro_resolucion != null) {
            String sql = "SELECT ID as ID,  NRO_RD as NRO_RD, RESOL_FORMATEADO as RESOL_FORMATEADO  FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE RESOL_FORMATEADO LIKE ?1 AND RUC =?2 AND ESTADO = 'VIGENTE' ";
            
            //sql += " AND TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) LIKE ?1";
            //sql += " GROUP BY a.DOC_PORTADOR";
//            javax.persistence.Query q = em.createNativeQuery(
//                    "SELECT ID as ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, 
//NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE NRO_RD LIKE '%1420-22%' AND RUC ='10001242470' AND ESTADO = 'VIGENTE' "
//            );
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + nro_resolucion + "%");
            q.setParameter(2, ruc);
//            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(100);            
            return  q.getResultList();
        } else {
            return null;
        }
            
    }
     
    public HashMap buscarAutorizacion_vistaDISCA_xModalidad(String nro_resolucion, boolean b_vigencia, String tip_mod) {
        // TIP_MOD = 1   PRESTACION DE SERVICIOS DE VIGILANCIA PRIVADA
        // TIP_MOD = 5   PRESTACION DE SERVICIO DE PROTECCION PERSONAL
        
        System.out.println("xxx_nro_resolucion->"+nro_resolucion);
        System.out.println("xxx_b_vigencia->"+b_vigencia);
        System.out.println("xxx_tip_mod->"+tip_mod);
        
        
        String sql;
        if (b_vigencia) {
            //Cuando es la Vista de Resoluciones Vigentes del DISCA
            sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' AND TIP_MOD = ?2 AND ANO_RD_DISCA IS NOT NULL";
        } else {
            //Cuando es la Vista de Todas las Resoluciones del DISCA, inclusive las no Vigentes
            sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCION_GSSP_TODO@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' AND TIP_MOD = ?2 AND ANO_RD_DISCA IS NOT NULL";
        }       
        
        javax.persistence.Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, nro_resolucion);
        q.setParameter(2, tip_mod);
        q.setHint("eclipselink.result-type", "Map");
        List<ArrayRecord> la = q.setMaxResults(1).getResultList();
        System.out.println("la.size()->"+la.size());
        HashMap<String, String> hashMap = new HashMap<>();
        for (ArrayRecord  str : la) {
            
            System.out.println("MOD_EMP_DISCA->"+str.get("MOD_EMP_DISCA"));
            
            hashMap.put("ID", (str.get("ID") != null) ? str.get("ID").toString() : "");
            hashMap.put("RUC", (str.get("RUC") != null) ? str.get("RUC").toString() : "");
            hashMap.put("RZN_SOC", (str.get("RZN_SOC") != null) ? str.get("RZN_SOC") .toString() : "");
            hashMap.put("REPRESENTANTE", (str.get("REPRESENTANTE") != null) ? str.get("REPRESENTANTE").toString(): "");
            hashMap.put("DOC_REPRESENTANTE",(str.get("DOC_REPRESENTANTE") != null) ? str.get("DOC_REPRESENTANTE").toString() : "" );
            hashMap.put("NRO_RD", (str.get("NRO_RD") != null) ? str.get("NRO_RD").toString(): "");
            
            String fecha_emi = null;
            String fecha_ven = null;
            fecha_emi = (str.get("FEC_EMI").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_EMI")) : null;
            fecha_ven = (str.get("FEC_VEN").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_VEN")) : null;
            hashMap.put("FEC_EMI", (fecha_emi != null) ? fecha_emi: "");
            hashMap.put("FEC_VEN", (fecha_ven != null) ? fecha_ven: "");
            hashMap.put("PRINCIPAL", (str.get("PRINCIPAL") != null) ? str.get("PRINCIPAL").toString(): "");
            hashMap.put("DOM_ACT", (str.get("DOM_ACT") != null) ? str.get("DOM_ACT").toString(): "");
            hashMap.put("NOM_DPTO", (str.get("NOM_DPTO") != null) ? str.get("NOM_DPTO").toString(): "");
            hashMap.put("NOM_PROV", (str.get("NOM_PROV") != null) ? str.get("NOM_PROV").toString(): "");
            hashMap.put("NOM_DIST", (str.get("NOM_DIST") != null) ? str.get("NOM_DIST").toString(): "");
            hashMap.put("TIP_MOD", (str.get("TIP_MOD") != null) ? str.get("TIP_MOD") .toString(): "");
            hashMap.put("MODALIDAD", (str.get("MODALIDAD") != null) ? str.get("MODALIDAD").toString(): "");
            hashMap.put("DES_MOD", (str.get("DES_MOD") != null) ? str.get("DES_MOD").toString(): "");
            hashMap.put("ESTADO", (str.get("ESTADO") != null) ? str.get("ESTADO").toString(): "");
            hashMap.put("COD_ARM", (str.get("COD_ARM") != null) ? str.get("COD_ARM").toString(): "");
            hashMap.put("DES_TRAM", (str.get("DES_TRAM") != null) ? str.get("DES_TRAM") .toString(): "");
            hashMap.put("NRO_FISICO", (str.get("NRO_FISICO") != null) ? str.get("NRO_FISICO") .toString(): "");
            hashMap.put("NRO_EXP", (str.get("NRO_EXP") != null) ? str.get("NRO_EXP").toString(): "");
            hashMap.put("MOD_EMP_DISCA", (str.get("MOD_EMP_DISCA") != null) ? str.get("MOD_EMP_DISCA").toString(): "");
            hashMap.put("NRO_RD_DISCA", (str.get("NRO_RD_DISCA") != null) ? str.get("NRO_RD_DISCA").toString(): "");
            hashMap.put("ANO_RD_DISCA", (str.get("ANO_RD_DISCA") != null) ? str.get("ANO_RD_DISCA").toString(): "");
            hashMap.put("REGISTRO_ID", (str.get("REGISTRO_ID") != null) ? str.get("REGISTRO_ID").toString(): ""); 
            hashMap.put("RESOL_FORMATEADO", (str.get("RESOL_FORMATEADO") != null) ? str.get("RESOL_FORMATEADO").toString(): "");
        }
         
        return hashMap;
    }
    
    public HashMap buscarAutorizacion_vistaDISCA_Tecnologia(String nro_resolucion, boolean b_vigencia, String tip_mod) {
        // TIP_MOD = 1   PRESTACION DE SERVICIOS DE VIGILANCIA PRIVADA
        // TIP_MOD = 5   PRESTACION DE SERVICIO DE PROTECCION PERSONAL
        
        
        String sql;
        if (b_vigencia) {
            //Cuando es la Vista de Resoluciones Vigentes del DISCA
            sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' AND TIP_MOD in ("+tip_mod+") AND ANO_RD_DISCA IS NOT NULL";
        } else {
            //Cuando es la Vista de Todas las Resoluciones del DISCA, inclusive las no Vigentes
            sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCION_GSSP_TODO@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' AND TIP_MOD in ("+tip_mod+") AND ANO_RD_DISCA IS NOT NULL";
        }       
        
        javax.persistence.Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, nro_resolucion);
//        q.setParameter(2, tip_mod);
        q.setHint("eclipselink.result-type", "Map");
        List<ArrayRecord> la = q.setMaxResults(1).getResultList();
        HashMap<String, String> hashMap = new HashMap<>();
        for (ArrayRecord  str : la) {
            hashMap.put("ID", (str.get("ID") != null) ? str.get("ID").toString() : "");
            hashMap.put("RUC", (str.get("RUC") != null) ? str.get("RUC").toString() : "");
            hashMap.put("RZN_SOC", (str.get("RZN_SOC") != null) ? str.get("RZN_SOC") .toString() : "");
            hashMap.put("REPRESENTANTE", (str.get("REPRESENTANTE") != null) ? str.get("REPRESENTANTE").toString(): "");
            hashMap.put("DOC_REPRESENTANTE",(str.get("DOC_REPRESENTANTE") != null) ? str.get("DOC_REPRESENTANTE").toString() : "" );
            hashMap.put("NRO_RD", (str.get("NRO_RD") != null) ? str.get("NRO_RD").toString(): "");
            
            String fecha_emi = null;
            String fecha_ven = null;
            fecha_emi = (str.get("FEC_EMI").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_EMI")) : null;
            fecha_ven = (str.get("FEC_VEN").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_VEN")) : null;
            hashMap.put("FEC_EMI", (fecha_emi != null) ? fecha_emi: "");
            hashMap.put("FEC_VEN", (fecha_ven != null) ? fecha_ven: "");
            hashMap.put("PRINCIPAL", (str.get("PRINCIPAL") != null) ? str.get("PRINCIPAL").toString(): "");
            hashMap.put("DOM_ACT", (str.get("DOM_ACT") != null) ? str.get("DOM_ACT").toString(): "");
            hashMap.put("NOM_DPTO", (str.get("NOM_DPTO") != null) ? str.get("NOM_DPTO").toString(): "");
            hashMap.put("NOM_PROV", (str.get("NOM_PROV") != null) ? str.get("NOM_PROV").toString(): "");
            hashMap.put("NOM_DIST", (str.get("NOM_DIST") != null) ? str.get("NOM_DIST").toString(): "");
            hashMap.put("TIP_MOD", (str.get("TIP_MOD") != null) ? str.get("TIP_MOD") .toString(): "");
            hashMap.put("MODALIDAD", (str.get("MODALIDAD") != null) ? str.get("MODALIDAD").toString(): "");
            hashMap.put("DES_MOD", (str.get("DES_MOD") != null) ? str.get("DES_MOD").toString(): "");
            hashMap.put("ESTADO", (str.get("ESTADO") != null) ? str.get("ESTADO").toString(): "");
            hashMap.put("COD_ARM", (str.get("COD_ARM") != null) ? str.get("COD_ARM").toString(): "");
            hashMap.put("DES_TRAM", (str.get("DES_TRAM") != null) ? str.get("DES_TRAM") .toString(): "");
            hashMap.put("NRO_FISICO", (str.get("NRO_FISICO") != null) ? str.get("NRO_FISICO") .toString(): "");
            hashMap.put("NRO_EXP", (str.get("NRO_EXP") != null) ? str.get("NRO_EXP").toString(): "");
            hashMap.put("MOD_EMP_DISCA", (str.get("MOD_EMP_DISCA") != null) ? str.get("MOD_EMP_DISCA").toString(): "");
            hashMap.put("NRO_RD_DISCA", (str.get("NRO_RD_DISCA") != null) ? str.get("NRO_RD_DISCA").toString(): "");
            hashMap.put("ANO_RD_DISCA", (str.get("ANO_RD_DISCA") != null) ? str.get("ANO_RD_DISCA").toString(): "");
            hashMap.put("REGISTRO_ID", (str.get("REGISTRO_ID") != null) ? str.get("REGISTRO_ID").toString(): ""); 
            hashMap.put("RESOL_FORMATEADO", (str.get("RESOL_FORMATEADO") != null) ? str.get("RESOL_FORMATEADO").toString(): "");
        }
         
        return hashMap;
    }
    
    public List<GsspSolicitudAutorizacionSPPController.resol_vista> list_autocomplete_vistaDISCA_xModalidad(String nro_resolucion, String ruc , String tip_mod) {
//        if (ruc != null) {
//            String sql = "SELECT DISTINCT RESOL_FORMATEADO as RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE RUC =?1 AND ESTADO = 'VIGENTE' and ANO_RD_DISCA IS NOT NULL";
//             
//            Query q = em.createNativeQuery(sql);
////            q.setParameter(1, "%" + nro_resolucion + "%");
//            q.setParameter(1, ruc);
//            q.setHint("eclipselink.result-type", "Map");
//            q.setMaxResults(100);            
//            return  (List<GsspSolicitudAutorizacionSPPController.resol_vista>)q.getResultList();
//        } else {
//            return null;
//        }
        
        try {
//        if (nro_resolucion != null && ruc != null && tip_mod!= null) {
            String sql = "SELECT ID as ID, NRO_RD as NRO_RD, RESOL_FORMATEADO as RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE RESOL_FORMATEADO LIKE ?1 AND RUC =?2 AND ESTADO = 'VIGENTE' AND TIP_MOD =?3  AND ANO_RD_DISCA IS NOT NULL";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + nro_resolucion + "%");
            q.setParameter(2, ruc);
            q.setParameter(3, tip_mod);
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(100);            
//            return  q.getResultList();
            return  (List<GsspSolicitudAutorizacionSPPController.resol_vista>)q.getResultList();
//        } else {
//            return null;
//        }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
            
    }
    
    public List<GsspSolicitudAutorizacionSPPController.resol_vista> list_autocomplete_vistaDISCA_Tecnologia(String nro_resolucion, String ruc , String tip_mod) {
//        if (ruc != null) {
//            String sql = "SELECT DISTINCT RESOL_FORMATEADO as RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE RUC =?1 AND ESTADO = 'VIGENTE' and ANO_RD_DISCA IS NOT NULL";
//             
//            Query q = em.createNativeQuery(sql);
////            q.setParameter(1, "%" + nro_resolucion + "%");
//            q.setParameter(1, ruc);
//            q.setHint("eclipselink.result-type", "Map");
//            q.setMaxResults(100);            
//            return  (List<GsspSolicitudAutorizacionSPPController.resol_vista>)q.getResultList();
//        } else {
//            return null;
//        }
        
        try {
//        if (nro_resolucion != null && ruc != null && tip_mod!= null) {
            String sql = "SELECT ID as ID, NRO_RD as NRO_RD, RESOL_FORMATEADO as RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE RESOL_FORMATEADO LIKE ?1 AND RUC =?2 AND ESTADO = 'VIGENTE' AND TIP_MOD in ("+tip_mod+")  AND ANO_RD_DISCA IS NOT NULL";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + nro_resolucion + "%");
            q.setParameter(2, ruc);
//            q.setParameter(3, tip_mod);
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(100);            
//            return  q.getResultList();
            return  (List<GsspSolicitudAutorizacionSPPController.resol_vista>)q.getResultList();
//        } else {
//            return null;
//        }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
            
    }
    
    
    /*QUERYS PARA PROTECCION PERSONAL*/
    /*QUERYS PARA SCBC Y SSE*/
public List<SspResolucion> buscarResolucionesVigentes(Long empresaId, Long modalidadId) {
        //Busca la Resoluciones activas, vigentes y filtradas
        String jpql = "select reso "
                + " from SspResolucion reso, SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where r.empresaId.id = :empresaId "
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
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) ";
                //+ " and r.estadoId not in (218,16,334,59,198) ";
        jpql += " order by reso.fechaFin desc ";
        
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }
        if(modalidadId != null){
            q.setParameter("modalidadId", modalidadId);
        }
        return q.getResultList();
    }
public SspRegistro ObtenerResolucionRGVigente(Long empresaId, Long modalidadId) {
        //Busca la Resoluciones activas, vigentes y filtradas
        String jpql = "select r "
                + " from SspResolucion reso, SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where r.empresaId.id = :empresaId "
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
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) ";
                //+ " and r.estadoId not in (218,16,334,59,198) ";
        jpql += " order by reso.fechaFin desc ";
        
        Query q = em.createQuery(jpql);
        if(empresaId != null){
            q.setParameter("empresaId", empresaId);
        }
        if(modalidadId != null){
            q.setParameter("modalidadId", modalidadId);
        }
        return (SspRegistro) q.getResultList().get(0);
    }
    /*QUERYS PARA SCBC Y SSE*/

    public List<Object[]> list_autocomplete_vistaDISCA_VigilanciaPrivada(String nro_resolucion, String ruc) {
        // TIP_MOD = 1   PRESTACION DE SERVICIOS DE VIGILANCIA PRIVADA
        if (nro_resolucion != null) {
            String sql = "SELECT ID as ID, NRO_RD as NRO_RD, RESOL_FORMATEADO as RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE RESOL_FORMATEADO LIKE ?1 AND RUC =?2 AND ESTADO = 'VIGENTE' AND TIP_MOD = 1 AND ANO_RD_DISCA IS NOT NULL";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + nro_resolucion + "%");
            q.setParameter(2, ruc);
            q.setMaxResults(100);            
            return  q.getResultList();
        } else {
            return null;
        }
    }
    
    public List<Object[]> list_autocomplete_BuscarResolucionAprobado_ServTransYCustDineroValores(String nro_resolucion, String ruc) {
        
        //MODALIDAD = TP_GSSP_MOD_TDV
        
        if (nro_resolucion != null) {            
            String sql = ""
                    + " SELECT "
                    + "     '' || EMPRE.RUC || RESO.NUMERO as ID, "
                     + "     RESO.NUMERO as NRO_RD,  "
                    + "     TRIM(RESO.NUMERO) AS RESOL_FORMATEADO, "
                    + "     RESO.ID AS RESOLUCION_ID, "
                    + "     EMPRE.RUC, "
                    + "     (CASE WHEN ESTA.COD_PROG = 'TP_ECC_CAN' THEN 'CANCELADA' WHEN RESO.FECHA_FIN >= TRUNC (SYSDATE) THEN 'VIGENTE' ELSE 'VENCIDO' END) AS ESTADO, "
                    + "     MODA.NOMBRE, "
                    + "     MODA.COD_PROG "
                    + " FROM BDINTEGRADO.SSP_RESOLUCION RESO "
                    + " INNER JOIN BDINTEGRADO.SSP_REGISTRO EREG ON (RESO.REGISTRO_ID = EREG.ID) "
                    + " INNER JOIN BDINTEGRADO.SB_PERSONA EMPRE ON (EMPRE.ID = EREG.EMPRESA_ID) "
                    + " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD ESTA ON (ESTA.ID = EREG.ESTADO_ID) "
                    + " INNER JOIN BDINTEGRADO.TIPO_BASE MODA ON (MODA.ID = EREG.TIPO_PRO_ID) "
                    + " WHERE RESO.ACTIVO = 1 "
                    + " AND TRIM(RESO.NUMERO) LIKE ?1 "
                    + " AND EMPRE.RUC = ?2 "
                    + " AND RESO.FECHA_FIN >= TRUNC (SYSDATE) "
                    + " AND MODA.COD_PROG IN ('TP_GSSP_MOD_TDV') ";
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + nro_resolucion + "%");
            q.setParameter(2, ruc);
            q.setMaxResults(100);            
            return  q.getResultList();
        } else {
            return null;
        }
    }

    //--------------
    public HashMap buscarAutorizacion_vistaDISCA_VigilanciaPrivada(String nro_resolucion, String opcion) {
        // TIP_MOD = 1   PRESTACION DE SERVICIOS DE VIGILANCIA PRIVADA
        String sql;
        if (opcion == "1") {
            //Cuando es la Vista de Resoluciones Vigentes del DISCA
            sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' AND TIP_MOD = 1 AND ANO_RD_DISCA IS NOT NULL";
        } else {
            //Cuando es la Vista de Todas las Resoluciones del DISCA, inclusive las no Vigentes
            sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCION_GSSP_TODO@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' AND TIP_MOD = 1 AND ANO_RD_DISCA IS NOT NULL";
        }       
        
        javax.persistence.Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, nro_resolucion);
        q.setHint("eclipselink.result-type", "Map");
        List<ArrayRecord> la = q.setMaxResults(1).getResultList();
        HashMap<String, String> hashMap = new HashMap<>();
        for (ArrayRecord  str : la) {
            hashMap.put("ID", (str.get("ID") != null) ? str.get("ID").toString() : "");
            hashMap.put("RUC", (str.get("RUC") != null) ? str.get("RUC").toString() : "");
            hashMap.put("RZN_SOC", (str.get("RZN_SOC") != null) ? str.get("RZN_SOC") .toString() : "");
            hashMap.put("REPRESENTANTE", (str.get("REPRESENTANTE") != null) ? str.get("REPRESENTANTE").toString(): "");
            hashMap.put("DOC_REPRESENTANTE",(str.get("DOC_REPRESENTANTE") != null) ? str.get("DOC_REPRESENTANTE").toString() : "" );
            hashMap.put("NRO_RD", (str.get("NRO_RD") != null) ? str.get("NRO_RD").toString(): "");
            
            String fecha_emi = null;
            String fecha_ven = null;
            fecha_emi = (str.get("FEC_EMI").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_EMI")) : null;
            fecha_ven = (str.get("FEC_VEN").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_VEN")) : null;
            hashMap.put("FEC_EMI", (fecha_emi != null) ? fecha_emi: "");
            hashMap.put("FEC_VEN", (fecha_ven != null) ? fecha_ven: "");
            hashMap.put("PRINCIPAL", (str.get("PRINCIPAL") != null) ? str.get("PRINCIPAL").toString(): "");
            hashMap.put("DOM_ACT", (str.get("DOM_ACT") != null) ? str.get("DOM_ACT").toString(): "");
            hashMap.put("NOM_DPTO", (str.get("NOM_DPTO") != null) ? str.get("NOM_DPTO").toString(): "");
            hashMap.put("NOM_PROV", (str.get("NOM_PROV") != null) ? str.get("NOM_PROV").toString(): "");
            hashMap.put("NOM_DIST", (str.get("NOM_DIST") != null) ? str.get("NOM_DIST").toString(): "");
            hashMap.put("TIP_MOD", (str.get("TIP_MOD") != null) ? str.get("TIP_MOD") .toString(): "");
            hashMap.put("MODALIDAD", (str.get("MODALIDAD") != null) ? str.get("MODALIDAD").toString(): "");
            hashMap.put("DES_MOD", (str.get("DES_MOD") != null) ? str.get("DES_MOD").toString(): "");
            hashMap.put("ESTADO", (str.get("ESTADO") != null) ? str.get("ESTADO").toString(): "");
            hashMap.put("COD_ARM", (str.get("COD_ARM") != null) ? str.get("COD_ARM").toString(): "");
            hashMap.put("DES_TRAM", (str.get("DES_TRAM") != null) ? str.get("DES_TRAM") .toString(): "");
            hashMap.put("NRO_FISICO", (str.get("NRO_FISICO") != null) ? str.get("NRO_FISICO") .toString(): "");
            hashMap.put("NRO_EXP", (str.get("NRO_EXP") != null) ? str.get("NRO_EXP").toString(): "");
            hashMap.put("MOD_EMP_DISCA", (str.get("MOD_EMP_DISCA") != null) ? str.get("MOD_EMP_DISCA").toString(): "");
            hashMap.put("NRO_RD_DISCA", (str.get("NRO_RD_DISCA") != null) ? str.get("NRO_RD_DISCA").toString(): "");
            hashMap.put("ANO_RD_DISCA", (str.get("ANO_RD_DISCA") != null) ? str.get("ANO_RD_DISCA").toString(): "");
            hashMap.put("REGISTRO_ID", (str.get("REGISTRO_ID") != null) ? str.get("REGISTRO_ID").toString(): ""); 
            hashMap.put("RESOL_FORMATEADO", (str.get("RESOL_FORMATEADO") != null) ? str.get("RESOL_FORMATEADO").toString(): "");
        }
         
        return hashMap;
    }
    
    
    
    public List<Object[]> list_autocomplete_vistaDISCA_CuentaPropia(String nro_resolucion, String ruc) {
        // TIP_MOD = 2   CUENTA PROPIA
        if (nro_resolucion != null) {
            String sql = "SELECT ID as ID, NRO_RD as NRO_RD, RESOL_FORMATEADO as RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE RESOL_FORMATEADO LIKE ?1 AND RUC =?2 AND ESTADO = 'VIGENTE' AND TIP_MOD = 2 AND ANO_RD_DISCA IS NOT NULL";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + nro_resolucion + "%");
            q.setParameter(2, ruc);
            q.setMaxResults(100);            
            return  q.getResultList();
        } else {
            return null;
        }
    }
    
    
    //******
    public HashMap buscarAutorizacion_vistaDISCA_CuentaPropia(String nro_resolucion, String opcion) {
        // TIP_MOD = 2   CUENTA PROPIA
        String sql;
        if (opcion == "2") {
            //Cuando es la Vista de Resoluciones Vigentes del DISCA
            sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' AND TIP_MOD = 2 AND ANO_RD_DISCA IS NOT NULL";
        } else {
            //Cuando es la Vista de Todas las Resoluciones del DISCA, inclusive las no Vigentes
            sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCION_GSSP_TODO@SUCAMEC WHERE ID = ?1  AND ESTADO = 'VIGENTE' AND TIP_MOD = 2 AND ANO_RD_DISCA IS NOT NULL";
        }       
        
        javax.persistence.Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, nro_resolucion);
        q.setHint("eclipselink.result-type", "Map");
        List<ArrayRecord> la = q.setMaxResults(1).getResultList();
        HashMap<String, String> hashMap = new HashMap<>();
        for (ArrayRecord  str : la) {
            hashMap.put("ID", (str.get("ID") != null) ? str.get("ID").toString() : "");
            hashMap.put("RUC", (str.get("RUC") != null) ? str.get("RUC").toString() : "");
            hashMap.put("RZN_SOC", (str.get("RZN_SOC") != null) ? str.get("RZN_SOC") .toString() : "");
            hashMap.put("REPRESENTANTE", (str.get("REPRESENTANTE") != null) ? str.get("REPRESENTANTE").toString(): "");
            hashMap.put("DOC_REPRESENTANTE",(str.get("DOC_REPRESENTANTE") != null) ? str.get("DOC_REPRESENTANTE").toString() : "" );
            hashMap.put("NRO_RD", (str.get("NRO_RD") != null) ? str.get("NRO_RD").toString(): "");
            
            String fecha_emi = null;
            String fecha_ven = null;
            fecha_emi = (str.get("FEC_EMI").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_EMI")) : null;
            fecha_ven = (str.get("FEC_VEN").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_VEN")) : null;
            hashMap.put("FEC_EMI", (fecha_emi != null) ? fecha_emi: "");
            hashMap.put("FEC_VEN", (fecha_ven != null) ? fecha_ven: "");
            hashMap.put("PRINCIPAL", (str.get("PRINCIPAL") != null) ? str.get("PRINCIPAL").toString(): "");
            hashMap.put("DOM_ACT", (str.get("DOM_ACT") != null) ? str.get("DOM_ACT").toString(): "");
            hashMap.put("NOM_DPTO", (str.get("NOM_DPTO") != null) ? str.get("NOM_DPTO").toString(): "");
            hashMap.put("NOM_PROV", (str.get("NOM_PROV") != null) ? str.get("NOM_PROV").toString(): "");
            hashMap.put("NOM_DIST", (str.get("NOM_DIST") != null) ? str.get("NOM_DIST").toString(): "");
            hashMap.put("TIP_MOD", (str.get("TIP_MOD") != null) ? str.get("TIP_MOD") .toString(): "");
            hashMap.put("MODALIDAD", (str.get("MODALIDAD") != null) ? str.get("MODALIDAD").toString(): "");
            hashMap.put("DES_MOD", (str.get("DES_MOD") != null) ? str.get("DES_MOD").toString(): "");
            hashMap.put("ESTADO", (str.get("ESTADO") != null) ? str.get("ESTADO").toString(): "");
            hashMap.put("COD_ARM", (str.get("COD_ARM") != null) ? str.get("COD_ARM").toString(): "");
            hashMap.put("DES_TRAM", (str.get("DES_TRAM") != null) ? str.get("DES_TRAM") .toString(): "");
            hashMap.put("NRO_FISICO", (str.get("NRO_FISICO") != null) ? str.get("NRO_FISICO") .toString(): "");
            hashMap.put("NRO_EXP", (str.get("NRO_EXP") != null) ? str.get("NRO_EXP").toString(): "");
            hashMap.put("MOD_EMP_DISCA", (str.get("MOD_EMP_DISCA") != null) ? str.get("MOD_EMP_DISCA").toString(): "");
            hashMap.put("NRO_RD_DISCA", (str.get("NRO_RD_DISCA") != null) ? str.get("NRO_RD_DISCA").toString(): "");
            hashMap.put("ANO_RD_DISCA", (str.get("ANO_RD_DISCA") != null) ? str.get("ANO_RD_DISCA").toString(): "");
            hashMap.put("REGISTRO_ID", (str.get("REGISTRO_ID") != null) ? str.get("REGISTRO_ID").toString(): ""); 
            hashMap.put("RESOL_FORMATEADO", (str.get("RESOL_FORMATEADO") != null) ? str.get("RESOL_FORMATEADO").toString(): "");
        }
         
        return hashMap;
    }
    
    
    
    
    public HashMap BuscarResolucionAprobado_ServTransYCustDineroValores_ByNumResolucionFormateado(String nro_resolucion_formateado, String numRUC) {
        // TIP_MOD = 3   //SERVICIO DE TRANSPORTE Y CUSTODIA DE DINERO Y VALORES
        String sql;       
        sql = "SELECT ID, RUC, RZN_SOC, REPRESENTANTE, DOC_REPRESENTANTE, NRO_RD, FEC_EMI, FEC_VEN, PRINCIPAL, DOM_ACT, NOM_DPTO, NOM_PROV, NOM_DIST, TIP_MOD, MODALIDAD, DES_MOD, ESTADO, COD_ARM, DES_TRAM, NRO_FISICO, NRO_EXP, MOD_EMP_DISCA, NRO_RD_DISCA, ANO_RD_DISCA, REGISTRO_ID, RESOL_FORMATEADO FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC WHERE RESOL_FORMATEADO = ?1 AND RUC = ?2  AND ESTADO = 'VIGENTE' AND TIP_MOD = 3";
                
        javax.persistence.Query q = em.createNativeQuery(sql);        
        q.setParameter(1, nro_resolucion_formateado);
        q.setParameter(2, numRUC);
        
        q.setHint("eclipselink.result-type", "Map");
        List<ArrayRecord> la = q.setMaxResults(1).getResultList();
        HashMap<String, String> hashMap = new HashMap<>();
        for (ArrayRecord  str : la) {
            hashMap.put("ID", (str.get("ID") != null) ? str.get("ID").toString() : "");
            hashMap.put("RUC", (str.get("RUC") != null) ? str.get("RUC").toString() : "");
            hashMap.put("RZN_SOC", (str.get("RZN_SOC") != null) ? str.get("RZN_SOC") .toString() : "");
            hashMap.put("REPRESENTANTE", (str.get("REPRESENTANTE") != null) ? str.get("REPRESENTANTE").toString(): "");
            hashMap.put("DOC_REPRESENTANTE",(str.get("DOC_REPRESENTANTE") != null) ? str.get("DOC_REPRESENTANTE").toString() : "" );
            hashMap.put("NRO_RD", (str.get("NRO_RD") != null) ? str.get("NRO_RD").toString(): "");
            
            String fecha_emi = null;
            String fecha_ven = null;
            fecha_emi = (str.get("FEC_EMI").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_EMI")) : null;
            fecha_ven = (str.get("FEC_VEN").toString()!=null) ? JsfUtil.formatoFechaDdMmYyyy((Date)str.get("FEC_VEN")) : null;
            hashMap.put("FEC_EMI", (fecha_emi != null) ? fecha_emi: "");
            hashMap.put("FEC_VEN", (fecha_ven != null) ? fecha_ven: "");
            hashMap.put("PRINCIPAL", (str.get("PRINCIPAL") != null) ? str.get("PRINCIPAL").toString(): "");
            hashMap.put("DOM_ACT", (str.get("DOM_ACT") != null) ? str.get("DOM_ACT").toString(): "");
            hashMap.put("NOM_DPTO", (str.get("NOM_DPTO") != null) ? str.get("NOM_DPTO").toString(): "");
            hashMap.put("NOM_PROV", (str.get("NOM_PROV") != null) ? str.get("NOM_PROV").toString(): "");
            hashMap.put("NOM_DIST", (str.get("NOM_DIST") != null) ? str.get("NOM_DIST").toString(): "");
            hashMap.put("TIP_MOD", (str.get("TIP_MOD") != null) ? str.get("TIP_MOD") .toString(): "");
            hashMap.put("MODALIDAD", (str.get("MODALIDAD") != null) ? str.get("MODALIDAD").toString(): "");
            hashMap.put("DES_MOD", (str.get("DES_MOD") != null) ? str.get("DES_MOD").toString(): "");
            hashMap.put("ESTADO", (str.get("ESTADO") != null) ? str.get("ESTADO").toString(): "");
            hashMap.put("COD_ARM", (str.get("COD_ARM") != null) ? str.get("COD_ARM").toString(): "");
            hashMap.put("DES_TRAM", (str.get("DES_TRAM") != null) ? str.get("DES_TRAM") .toString(): "");
            hashMap.put("NRO_FISICO", (str.get("NRO_FISICO") != null) ? str.get("NRO_FISICO") .toString(): "");
            hashMap.put("NRO_EXP", (str.get("NRO_EXP") != null) ? str.get("NRO_EXP").toString(): "");
            hashMap.put("MOD_EMP_DISCA", (str.get("MOD_EMP_DISCA") != null) ? str.get("MOD_EMP_DISCA").toString(): "");
            hashMap.put("NRO_RD_DISCA", (str.get("NRO_RD_DISCA") != null) ? str.get("NRO_RD_DISCA").toString(): "");
            hashMap.put("ANO_RD_DISCA", (str.get("ANO_RD_DISCA") != null) ? str.get("ANO_RD_DISCA").toString(): "");
            hashMap.put("REGISTRO_ID", (str.get("REGISTRO_ID") != null) ? str.get("REGISTRO_ID").toString(): ""); 
            hashMap.put("RESOL_FORMATEADO", (str.get("RESOL_FORMATEADO") != null) ? str.get("RESOL_FORMATEADO").toString(): "");
        }
         
        return hashMap;
    }


}
