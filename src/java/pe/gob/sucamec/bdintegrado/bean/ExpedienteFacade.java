/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

/**
 *
 * @author mhermoza
 */
@Stateless
public class ExpedienteFacade extends AbstractFacade<Expediente> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExpedienteFacade() {
        super(Expediente.class);
    }

    /**
     * BUSCA EXPEDIENTE POR NUMERO
     *
     * @param nroExp
     * @param procesos
     * @param p_usua
     * @return
     */
    //public List<Expediente> lisExpedienteXNroExp(String nroExp, String procesos, int p_usua) {
    public List<Expediente> lisExpedienteXNroExp(String nroExp, String procesos) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e"
                    + " where trim(e.numero) like :nroExp "
                    + " and trim(e.idProceso.idProceso) in  " + procesos
                    //+ " and e.idUsuario = :p_usua"
                    //+ " and e.idExpediente not in (select r.expedienteId from Registro r where r.activo = 1)"
                    + " order by e.fechaCreacion");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            //q.setParameter("p_usua", new BigDecimal(""+p_usua));
            return q.setMaxResults(80).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * BUSCAR EXPEDIENTE POR REFERENCIA
     *
     * @param parametros
     * @param procesos
     * @param p_usua
     * @return
     */
    public List<Expediente> lstBusquedaExpedienteReferencia(Map parametros, String procesos) {
        String query = "select e from Expediente e "
                + " where trim(e.idProceso.idProceso) in " + procesos
                + " and (e.idCliente.razonSocial like :razsoc) ";

        if (parametros.get("fechaini") != null && parametros.get("fechafin") != null) {
            query = query + "and (e.fechaCreacion between :fechaini and :fechafin)";
        }

        javax.persistence.Query q = em.createQuery(query);
        q.setParameter("razsoc", "%" + parametros.get("razsoc") + "%");
        if (parametros.get("fechaini") != null && parametros.get("fechafin") != null) {
            q.setParameter("fechaini", (Date) parametros.get("fechaini"), TemporalType.DATE);
            q.setParameter("fechafin", (Date) parametros.get("fechafin"), TemporalType.DATE);
        }
        return q.setMaxResults(80).getResultList();
    }

    public List<Expediente> lisExpedienteByNroExpPOlvorin(String nroExp) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e where trim(e.numero) like :nroExp and e.idProceso.idProceso IN (303,314,485,486) order by e.fechaCreacion");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            return q.setMaxResults(200).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Expediente> lisExpedienteByNroExpRegLicManip(String nroExp) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e "
                    + "where trim(e.numero) like :nroExp "
                    + "and e.idExpediente not in (select r.expedienteId from Registro r where r.activo = 1) "
                    + "and e.idProceso.idProceso IN (214,219,234,240,291,292,305,306,325,336,475,476,477,478,479,480,481,482) "
                    + "AND e.estado='R' "
                    + "order by e.fechaCreacion");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            return q.setMaxResults(200).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Expediente> lisExpedienteByNroExpRegExp(String nroExp) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e where trim(e.numero) like :nroExp and e.idProceso.idProceso IN (119,178, 461, 462)"
                    + " and e.idExpediente not in (select r.expedienteId from Registro r where r.activo = 1) and e.estado='R'"
                    + " order by e.fechaCreacion");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            return q.setMaxResults(200).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Expediente> lisExpedienteModByNroExpRegExp(String nroExp) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e where trim(e.numero) like :nroExp and e.idProceso.idProceso IN (119,178, 461, 462)"
                    + " order by e.fechaCreacion");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            return q.setMaxResults(200).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Expediente> lisExpedienteByNroExpRegImp(String nroExp) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e where trim(e.numero) like :nroExp and e.idProceso.idProceso IN (67,92,103,189,204, 456, 457, 458, 459, 460)"
                    + " and e.idExpediente not in (select r.expedienteId from Registro r where r.activo = 1) and e.estado='R'"
                    + " order by e.fechaCreacion");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            return q.setMaxResults(200).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Expediente> lisExpedienteModByNroExpRegImp(String nroExp) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e where trim(e.numero) like :nroExp and e.idProceso.idProceso IN (67,92,103,189,204, 456, 457, 458, 459, 460)"
                    + " order by e.fechaCreacion");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            return q.setMaxResults(200).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Expediente> lisExpedienteByNroExp(String nroExp, String filtroSubPro) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e "
                    + " where trim(e.numero) like :nroExp "
                    + " and e.estado='R'"
                    + " and trim(e.idProceso.idProceso) = :filtroSubPro"
                    + " order by e.fechaCreacion");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            q.setParameter("filtroSubPro", filtroSubPro);
            return q.setMaxResults(200).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Expediente obtenerExpedienteXId(String id) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e"
                    + " where e.idExpediente = :parametro");
            q.setParameter("parametro", new BigDecimal("" + id));
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return (Expediente) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * polvorin
     *
     * @param id
     * @return
     */
    public List<Expediente> obtenerExpedienteXIdList(String id) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e"
                    + " where e.idExpediente = :parametro");
            q.setParameter("parametro", new BigDecimal("" + id));
            return q.getResultList();

        } catch (Exception e) {
            return null;

        }

    }

    public String mostrarNumeroExpediente(String id) {
        try {
            String p_numexp = "";
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e"
                    + " where e.idExpediente = :parametro");
            q.setParameter("parametro", new BigDecimal("" + id));
            if (q.getResultList().isEmpty()) {
                return "MIGRACION";
            } else {
                p_numexp = "" + ((Expediente) q.getResultList().get(0)).getNumero();
                return p_numexp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Expediente obtenerExpedienteXNumero(String id) { 
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e"
                    + " where e.numero = :parametro");
            q.setParameter("parametro", (id != null)?id.trim():null);
            return (Expediente) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public int contarExpedientesUtilizados(String p_numexp) {
        String sql = "SELECT COUNT(R.ID) \"cant\" \n"
                + "FROM BDEXPLOSIVO.REGISTRO R \n"
                + "INNER JOIN TRAMDOC.EXPEDIENTE E\n"
                + "ON R.EXPEDIENTE_ID = E.ID_EXPEDIENTE \n"
                + "INNER JOIN BDEXPLOSIVO.TIPO_EXPLOSIVO T\n"
                + "ON R.ESTADO = T.ID \n"
                + "INNER JOIN BDEXPLOSIVO.TIPO_BASE TB\n"
                + "ON R.TIPO_PRO_ID = TB.ID \n"
                + "WHERE \n"
                + "T.COD_PROG NOT IN ('TP_REGEV_ANU') AND \n"
                + "TB.COD_PROG = 'TP_PRTUP_GUI' AND \n"
                + "E.NUMERO = ?1";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, p_numexp);
        return Integer.parseInt("" + query.getSingleResult());
    }

    public List<Expediente> buscarExpedienteXNumero(String nroExp) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e"
                    + " where e.numero like :nroExp ");
            q.setParameter("nroExp", "%" + nroExp.trim() + "%");
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Expediente> buscarExpedienteXNumeroCan(String nroExp) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e "
                    + " where e.numero= :nroExp and e.idProceso.idProceso in (303,314,485,486) ");
            q.setParameter("nroExp", nroExp);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Expediente> obtenerExpedienteXNumeroList(String id) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery("select e from Expediente e where e.numero = :parametro");
            q.setParameter("parametro", id);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * OBTENER DATOS SUCE
     *
     * @param id
     * @return Cadena = Nro. Suce + Fecha Suce
     */
    public String obtener_DOCSUCE(String id) { //obtenerDocSce
        String sql = "", nroSuce = null;
        Map mMap = new HashMap();

        sql = "select d.titulo, d.fechaDocumento from Documento d where d.expediente.idExpediente = :idExpe and d.idTipoDocumento.idTipoDocumento = 382";

        javax.persistence.Query q = em.createQuery(sql);
        q.setParameter("idExpe", Long.parseLong(id));

        List<Object[]> results = q.getResultList();

        for (Object[] _values : results) {
            mMap.put("titulo", _values[0]);
            mMap.put("fecha_documento", _values[1]);
            break;
        }
        if (mMap.get("titulo") != null) {
            nroSuce = " relacionado con la Solicitud Única de Comercio Exterior (SUCE) N° " + mMap.get("titulo").toString();

            if (mMap.get("fecha_documento") != null) {
                nroSuce = nroSuce + " del " + mostrarFechaString((Date) mMap.get("fecha_documento"));
            }
        }

        return nroSuce;
    }
    
    public String mostrarNumeroDisca(String id) {
        try {
            String numeroDisca = null;
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e.numeroDisca from Expediente e"
                    + " where e.numero = :parametro");
            q.setParameter("parametro", id.trim());
           
            List<String> results = q.getResultList();
            for (String _values : results) {
                if(_values != null){
                    numeroDisca = "" + _values;
                    break;
                }
            }
            return numeroDisca;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String mostrarFechaString(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());

            return dia + " de " + mes + " de " + anio;
        } else {
            return "BORRADOR";
        }
    }
    
    public void evaluarTrazaExiste(){
        //Query q = em.createNativeQuery("")
    }

    public String obtenerUsuarioTrazaActualExpediente(String nroExpediente) {
        String usuario = null;
        Query q = em.createNativeQuery("select u.usuario from tramdoc.traza t " +
                                        " inner join tramdoc.expediente ex on ex.id_expediente = t.id_expediente " +
                                        " inner join tramdoc.usuario_por_traza ut on ut.traza = t.id_traza " +
                                        " inner join tramdoc.usuario u on u.id_usuario = ut.usuario " +
                                        " where ex.numero = ?1 and t.actual = 1 " +
                                        " ORDER BY t.id_traza asc " );
        q.setParameter(1, nroExpediente);
        if(!q.getResultList().isEmpty()){
            usuario = (String) q.getResultList().get(0);        
        }
        
        return usuario;
    }
    
    public Date mostrarFechaExpedienteByNumero(String nroExpediente) {
        try {
            javax.persistence.Query q;
            q = getEntityManager().createQuery(
                    "select e from Expediente e"
                    + " where e.numero = :nroExpediente");
            q.setParameter("nroExpediente", nroExpediente);
            if (!q.getResultList().isEmpty()) {
                return ((Expediente) q.getResultList().get(0)).getFechaCreacion();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Expediente> selectExpedientesSuspensionCSByNroExp(String nroExp) {
        if (nroExp == null) {
            nroExp = "";
        }
        Query q = em.createQuery("select e from Expediente e where e.idProceso.idProceso in (328, 748) and e.estado = 'R' and e.numero like :nroExp");
        q.setParameter("nroExp", "%"+nroExp+"%");
        return q.getResultList();
    }
    
    public List<Expediente> selectExpedientesNoFfaa(String nroExp) {
        if (nroExp == null) {
            nroExp = "";
        }
        Query q = em.createQuery("select e from Expediente e where e.idProceso.idProceso in (742, 769 ,664 ,695 ,923 ,741 ,768 ,1156 ,1159 ,962 ,964 ,920 ,1625 ,1626) and e.estado = 'R' and e.numero like :nroExp");
        q.setParameter("nroExp", "%"+nroExp+"%");
        return q.getResultList();
    }
    
    public List<Expediente> selectExpedienteXNro(String nroExp) {
        if (nroExp == null) {
            nroExp = "";
        }
        Query q = em.createQuery("select e from Expediente e where e.numero = :nroExp");
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public Boolean expedientesAsignadosXIdUsuario(String listIdUsu, String nroExp) {
        Boolean res = Boolean.FALSE;
        try {
            String jpql = "select ex.id_expediente "
                    + "from tramdoc.expediente ex "
                    + "where "
                    + "ex.numero = ?2 "
                    + "and ex.numero in (select e.numero from tramdoc.expediente e "
                    + "inner join tramdoc.traza t on t.id_expediente = e.id_expediente and t.actual = 1 "
                    + "inner join tramdoc.usuario_por_traza ut on ut.traza = t.id_traza, tramdoc.usuario u "
                    + " where ut.usuario = u.id_usuario and u.id_usuario in (" + listIdUsu + "))";
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            //q.setParameter(1, listIdUsu);
            q.setParameter(2, nroExp);

            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                res = Boolean.TRUE;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return res;
    }
    
    public List<Map> obtenerExpedientes1(Date fechaIni, Date fechaFin, String tipoBus, String filtro, Date fecRegistro, String listProcesos, String listIdsUsuTD) {
        List<Map> listRes = new ArrayList<>();
        try {
            String jpql = " select distinct(ex.id_expediente) as idExp, gt.id as idGuia, gt.observacion, ex.numero as nroexp, ex.fecha_creacion fechCre,  "
                    + "clie.id_cliente as idClie, clie.razon_social as rznSocial, clie.nombre, clie.apellido_paterno as apPat, clie.apellido_materno as apMat, "
                    + "clie.numero_identificacion as numDoc, tg.nombre as tipoGuia, tg.cod_prog as cod_TG, tg.id as id_TG,  pro.nombre as proceso, "
                    + "tb.nombre as estGuia, tb.cod_prog as cod_EG, gt.fecha_llegada as fec_Llegada, gt.fecha_salida as fec_Salida, gt.fecha_emision as fec_emision, gt.fecha_vencimiento as fec_venc "
                    + "from tramdoc.expediente ex "
                    + "inner join tramdoc.traza t on (ex.id_expediente  = t.id_expediente and t.actual = 1) "
                    //+ "left join tramdoc.accion_traza at on (at.id_traza=t.id_traza) "
                    //+ "left join tramdoc.accion ac on (at.id_accion=ac.id_accion) "
                    + "inner join tramdoc.usuario_por_traza ut on (t.id_traza = ut.traza) "
                    + "inner join tramdoc.usuario u on (u.id_usuario = ut.usuario) "
                    + "inner join tramdoc.cliente clie on (ex.id_cliente = clie.id_cliente) "
                    + "inner join tramdoc.proceso pro on (ex.id_proceso = pro.id_proceso) "
                    + "left join bdintegrado.ama_guia_transito gt on (ex.numero = gt.nro_expediente and gt.activo = 1) "
                    + "left join bdintegrado.tipo_gamac tg on (gt.tipo_guia_id = tg.id) "
                    + "left join bdintegrado.tipo_base tb on (gt.estado_id=tb.id) "
                    + "where pro.id_proceso in (" + listProcesos + ") and ex.estado != 'X' "
                    //+ "where pro.id_proceso in (" + listProcesos + ") and ex.estado != 'X' and (ac.nombre != 'Denegado' and ac.nombre != 'Improcedente') "
                    + "and u.id_usuario in (" + listIdsUsuTD + ") ";
            if (fechaIni != null && fechaFin != null) {
                jpql += "and TRUNC(ex.fecha_creacion) between ?1 and ?2 ";
            }
            if (tipoBus != null && (filtro != null || fecRegistro != null)) {
                switch (tipoBus) {
                    case "A":
                        jpql += "and ex.numero = ?3 ";
                        break;
                    case "B":
                        jpql += "and (concat(clie.nombre, concat(' ',concat(clie.apellido_paterno, concat(' ',clie.apellido_paterno)))) like ?4 or clie.razon_social like ?4) ";
                        break;
                    case "C":
                        jpql += "and (clie.numero_identificacion like ?3 ) ";
                        break;
                    case "D":
                        jpql += "and TRUNC(ex.fecha_creacion) = TRUNC(?5) ";
                        break;
                    case "E":
                        jpql += "and pro.nombre like ?4 ";
                        break;
                    default:
                        break;
                }
            }
            jpql += " order by ex.fecha_creacion desc ";
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            //q.setParameter(6, idUsuarioTD);
            if (fechaIni != null && fechaFin != null) {
                q.setParameter(1, fechaIni);
                q.setParameter(2, fechaFin);
            }
            if (tipoBus != null && (filtro != null || fecRegistro != null)) {
                switch (tipoBus) {
                    case "A":
                        q.setParameter(3, filtro);
                        break;
                    case "B":
                        q.setParameter(4, "%" + filtro + "%");
                        break;
                    case "C":
                        q.setParameter(3, filtro + "%");
                        break;
                    case "D":
                        q.setParameter(5, fecRegistro, TemporalType.DATE);
                        break;
                    case "E":
                        q.setParameter(4, "%" + filtro + "%");
                        break;
                    default:
                        break;
                }
            }
            //q.setMaxResults(MAX_RES_M);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return listRes;
    }

    public List<Map> obtenerExpedientes2(Date fechaIni, Date fechaFin, String tipoBus, String filtro, Date fecRegistro, String listProcesos, String listIdsUsuTD) {
        List<Map> listRes = new ArrayList<>();
        try {
            String jpql = " select ex.id_expediente as idExp, gt.id as idGuia, gt.observacion, ex.numero as nroexp, ex.fecha_creacion fechCre,  "
                    + "clie.id_cliente as idClie, clie.razon_social as rznSocial, clie.nombre, clie.apellido_paterno as apPat, clie.apellido_materno as apMat, "
                    + "clie.numero_identificacion as numDoc, tg.nombre as tipoGuia, tg.cod_prog as cod_TG, tg.id as id_TG,  pro.nombre as proceso, "
                    + "tb.nombre as estGuia, tb.cod_prog as cod_EG, gt.fecha_llegada as fec_Llegada, gt.fecha_salida as fec_Salida, gt.fecha_emision as fec_emision, gt.fecha_vencimiento as fec_venc "
                    + "from tramdoc.expediente ex "
                    + "inner join tramdoc.traza t on (ex.id_expediente  = t.id_expediente and t.actual = 1) "
                    //+ "inner join tramdoc.accion_traza at on (at.id_traza=t.id_traza) "
                    //+ "inner join tramdoc.accion ac on (at.id_accion=ac.id_accion) "
                    + "inner join tramdoc.usuario_por_traza ut on (t.id_traza = ut.traza) "
                    + "inner join tramdoc.usuario u on (u.id_usuario = ut.usuario) "
                    + "inner join tramdoc.cliente clie on (ex.id_cliente = clie.id_cliente) "
                    + "inner join tramdoc.proceso pro on (ex.id_proceso = pro.id_proceso) "
                    + "left join bdintegrado.ama_guia_transito gt on (ex.numero = gt.nro_expediente and gt.activo = 1) "
                    + "left join bdintegrado.tipo_gamac tg on (gt.tipo_guia_id = tg.id) "
                    + "left join bdintegrado.tipo_base tb on (gt.estado_id=tb.id) "
                    + "where pro.id_proceso in (" + listProcesos + ") and ex.estado = 'X' "
                    + "and u.id_usuario in (" + listIdsUsuTD + ") ";
            if (fechaIni != null && fechaFin != null) {
                jpql += "and TRUNC(ex.fecha_creacion) between ?1 and ?2 ";
            }
            if (tipoBus != null && (filtro != null || fecRegistro != null)) {
                switch (tipoBus) {
                    case "A":
                        jpql += "and ex.numero = ?3 ";
                        break;
                    case "B":
                        jpql += "and (concat(clie.nombre, concat(' ',concat(clie.apellido_paterno, concat(' ',clie.apellido_paterno)))) like ?4 or clie.razon_social like ?4) ";
                        break;
                    case "C":
                        jpql += "and (clie.numero_identificacion like ?3 ) ";
                        break;
                    case "D":
                        jpql += "and TRUNC(ex.fecha_creacion) = TRUNC(?5) ";
                        break;
                    case "E":
                        jpql += "and pro.nombre like ?4 ";
                        break;
                    default:
                        break;
                }
            }
            jpql += " order by ex.fecha_creacion desc ";
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            //q.setParameter(6, idUsuarioTD);
            if (fechaIni != null && fechaFin != null) {
                q.setParameter(1, fechaIni);
                q.setParameter(2, fechaFin);
            }
            if (tipoBus != null && (filtro != null || fecRegistro != null)) {
                switch (tipoBus) {
                    case "A":
                        q.setParameter(3, filtro);
                        break;
                    case "B":
                        q.setParameter(4, "%" + filtro + "%");
                        break;
                    case "C":
                        q.setParameter(3, filtro + "%");
                        break;
                    case "D":
                        q.setParameter(5, fecRegistro, TemporalType.DATE);
                        break;
                    case "E":
                        q.setParameter(4, "%" + filtro + "%");
                        break;
                    default:
                        break;
                }
            }
            //q.setMaxResults(MAX_RES_M);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return listRes;
    }

    public String obtenerAccionXidExpediente(BigDecimal idExp) {
        String accion = "";
        String sql = "SELECT AC.NOMBRE FROM TRAMDOC.ACCION  AC LEFT JOIN TRAMDOC.ACCION_TRAZA ACT ON (AC.ID_ACCION=ACT.ID_ACCION) LEFT JOIN TRAMDOC.TRAZA TR ON (ACT.ID_TRAZA=TR.ID_TRAZA) "
                + "WHERE TR.ID_EXPEDIENTE = ?1 AND TR.ACTUAL = 1";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, idExp);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            accion = (String) q.setMaxResults(1).getSingleResult();
        }
        return accion;
    }
    
}
