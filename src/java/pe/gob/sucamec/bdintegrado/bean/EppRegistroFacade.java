/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.EppGteRegistro;
import pe.gob.sucamec.bdintegrado.data.EppLibro;
import pe.gob.sucamec.bdintegrado.data.EppLibroMes;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppRegistroFacade extends AbstractFacade<EppRegistro> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppRegistroFacade() {
        super(EppRegistro.class);
    }

    public List<EppRegistro> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select p from EppRegistro p where p.tipoProId.codProg in ('TP_PRTUP_GLO','TP_PRTUP_EVE','TP_PRTUP_EXC')");
        //q.setParameter("nombre", "%" + s.toUpperCase() + "%");
        return q.getResultList();
    }

    public List<EppRegistro> buscarResolucionesGuiaExterna(String filtro, SbPersonaGt p) {
        try {
            Query q = getEntityManager().createQuery(""
                    + "select r from EppRegistro r "
                    + "inner join r.eppResolucion x "
                    + "where "
                    + "r.activo = 1 and x.activo = 1 and "
                    + "r.estado.codProg = 'TP_REGEV_FIN' and "
                    + "r.tipoProId.codProg in ('TP_PRTUP_GLO','TP_PRTUP_EVE','TP_PRTUP_EXC','TP_PRTUP_AAFOR','TP_PRTUP_AAHID','TP_PRTUP_AAPOR') and "
                    + "(FUNC('trunc',x.fechaFin) >= FUNC('trunc',CURRENT_DATE)) and "
                    + "(FUNC('trunc',CURRENT_DATE) >= FUNC('trunc',x.fechaIni)) and "
                    + "(x.numero like :parametro or :parametro is null) and "
                    + "trim(r.empresaId.id) = :per");
            q.setParameter("parametro", filtro == null ? null : ("%" + filtro + "%"));
            q.setParameter("per", p.getId());
            return q.setMaxResults(50).getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busqueda de Solicitudes de GTE
     *
     * @param filtro
     * @param p
     * @return
     */
    public List<EppGteRegistro> buscarSolicitudesGuiaExterna(String filtro, SbPersonaGt p) {
        try {
            Query q = getEntityManager().createQuery(""
                    + "select r from EppGteRegistro r "
                    + "where "
                    + "r.activo = 1 and "
                    + "r.nroSolicitud is not null and "
                    //+ "r.registroGtId is null and "
                    + "(r.nroSolicitud = :parametro or :parametro is null) and "
                    + "trim(r.empresaId.id) = :per");
            q.setParameter("parametro", filtro == null ? null : filtro);//("%" + filtro + "%")
            q.setParameter("per", p.getId());
            return q.setMaxResults(50).getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EppRegistro> buscarResolucionesPlanta(String filtro, SbPersonaGt p) {
        try {
            Query q = getEntityManager().createQuery(""
                    + "select r from EppRegistro r "
                    + "inner join r.eppResolucion x "
                    + "where "
                    + "r.activo = 1 and x.activo = 1 and "
                    + "r.tipoProId.codProg in ('TP_PRTUP_PLA','TP_PRTUP_AFAB') and "
                    + "( x.fechaFin is null or (FUNC('trunc',x.fechaFin) >= FUNC('trunc',CURRENT_DATE))) and "
                    + "(x.numero like :parametro or :parametro is null) and "
                    + "trim(r.empresaId.id) = :per order by r.fecha desc");
            q.setParameter("parametro", filtro == null ? null : ("%" + filtro + "%"));
            q.setParameter("per", p.getId());
            return q.setMaxResults(50).getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * BUSQUEDA DE LICENCIAS PAR BANDEJA DE ANULACION DE LME
     *
     * @param mMap
     * @return
     */
    public List<Map> buscarCapacitacionListadoSEL(HashMap mMap) {
        try {
            String jpqlBorrador = "SELECT 2 as TIPOREG, t0.ID, t1.APE_PAT, t1.APE_MAT, t1.NOMBRES, t2.NOMBRE AS TIPODOC, t1.NUM_DOC, t0.FECHA, "
                    + " t3.NOMBRE AS DEPARTAMENTO, t4.NOMBRE AS PROVINCIA, t5.NOMBRE AS DISTRITO, NULL AS NRO_EXPEDIENTE, "
                    + " NULL AS NOTA_EVALUACION, t6.FECHA_INICIO, t6.FECHA_FIN, t6.ID ID_CPACITACION "
                    + " FROM BDINTEGRADO.SB_PERSONA t7, BDINTEGRADO.EPP_CAPACITACION t6, BDINTEGRADO.SB_DISTRITO t5, BDINTEGRADO.SB_PROVINCIA t4, "
                    + " BDINTEGRADO.SB_DEPARTAMENTO t3, BDINTEGRADO.TIPO_BASE t2, BDINTEGRADO.SB_PERSONA t1, BDINTEGRADO.EPP_CMPE_INSCRIPCION t0 "
                    + " WHERE (((t0.ACTIVO = 1) AND ((t7.NUM_DOC = ?1) OR (t7.RUC = ?1))) AND (((((((t7.ID = t0.PERSONA_ID) "
                    + " AND (t1.ID = t0.PARTICIPANTE_ID)) AND (t2.ID = t1.TIPO_DOC)) AND (t6.ID = t0.CAPACITACION_ID)) "
                    + " AND (t5.ID = t6.UBIGEO_ID)) AND (t4.ID = t5.PROVINCIA_ID)) AND (t3.ID = t4.DEPARTAMENTO_ID))) ";

            String jpql = "SELECT 1 as TIPOREG, t0.ID, t1.APE_PAT, t1.APE_MAT, t1.NOMBRES, t3.NOMBRE AS TIPODOC, t1.NUM_DOC, t0.FECHA, "
                    + " t4.NOMBRE AS DEPARTAMENTO, t5.NOMBRE AS PROVINCIA, t6.NOMBRE as DISTRITO, t0.NRO_EXPEDIENTE, "
                    + " t2.NOTA_EVALUACION, t7.FECHA_INICIO, t7.FECHA_FIN, t7.ID ID_CPACITACION "
                    + " FROM BDINTEGRADO.SB_PERSONA t9, BDINTEGRADO.TIPO_BASE t8, BDINTEGRADO.EPP_CAPACITACION t7, "
                    + " BDINTEGRADO.SB_DISTRITO t6, BDINTEGRADO.SB_PROVINCIA t5, BDINTEGRADO.SB_DEPARTAMENTO t4, "
                    + " BDINTEGRADO.TIPO_BASE t3, BDINTEGRADO.EPP_CERTIFICADO t2, BDINTEGRADO.SB_PERSONA t1, "
                    + " BDINTEGRADO.EPP_REGISTRO t0 "
                    + " WHERE t2.ACTIVO = 1 AND (((((t0.ACTIVO = 1) AND (t8.COD_PROG IN ('TP_PIRO_SEGU'))) AND (t2.ACTIVO = 1)) AND ((t9.NUM_DOC = ?1) OR (t9.RUC = ?1))) "
                    + " AND (((((((((t8.ID = t0.TIPO_PRO_ID) AND (t2.REGISTRO_ID = t0.ID)) AND (t9.ID = t0.EMPRESA_ID)) "
                    + " AND (t1.ID = t2.PERSONA_ID)) AND (t3.ID = t1.TIPO_DOC)) AND (t7.ID = t2.CAPACITACION_ID)) "
                    + " AND (t6.ID = t7.UBIGEO_ID)) AND (t5.ID = t6.PROVINCIA_ID)) AND (t4.ID = t5.DEPARTAMENTO_ID)))";
            /*
            String jpql = "select 1 as tipoReg, r.id, r.eppCertificado.personaId.apePat, r.eppCertificado.personaId.apeMat, r.eppCertificado.personaId.nombres, "
                    + " r.eppCertificado.personaId.tipoDoc.nombre as tipoDoc, r.eppCertificado.personaId.numDoc, r.fecha, "
                    + " r.eppCertificado.capacitacionId.ubigeoId.provinciaId.departamentoId.nombre as departamento, r.eppCertificado.capacitacionId.ubigeoId.provinciaId.nombre as provincia, "
                    + " r.eppCertificado.capacitacionId.ubigeoId.nombre as distrito, r.nroExpediente, "
                    + " r.eppCertificado.notaEvaluacion, r.eppCertificado.capacitacionId.fechaInicio, r.eppCertificado.capacitacionId.fechaFin "
                    + " from EppRegistro r where r.activo = 1 and r.tipoProId.codProg in ('TP_PIRO_SEGU') and r.eppCertificado.activo = 1 and "
                    + " (r.empresaId.numDoc = :doc OR r.empresaId.ruc = :doc ) ";*/

            if (mMap.get("capacitacionId") != null) {
                jpql = jpql + " and (t7.ID = ?2 )";
                jpqlBorrador = jpqlBorrador + " and (t6.ID = ?2 )";
            }
            if (mMap.get("fechaInicio") != null && mMap.get("fechaFin") != null) {
                jpql = jpql + " AND TRUNC(t0.FECHA) BETWEEN ?3 AND ?4 ";
                jpqlBorrador = jpqlBorrador + " AND TRUNC(t0.FECHA) BETWEEN ?3 AND ?4 ";
            }

            if (mMap.get("tipoBusqueda") != null) {
                switch (mMap.get("tipoBusqueda").toString()) {
                    case "1":   // APELLIDOS Y NOMBRES
                        jpql = jpql + " AND (T1.APE_PAT||' '|| t1.APE_MAT||' '||t1.NOMBRES) like ?5 ";
                        jpqlBorrador = jpqlBorrador + " AND (T1.APE_PAT||' '|| t1.APE_MAT||' '||t1.NOMBRES) like ?5 ";
                        break;
                    case "2":   // NRO DOCUMENTO
                        jpql = jpql + " AND trim(t1.NUM_DOC) = ?5 ";
                        jpqlBorrador = jpqlBorrador + " AND trim(t1.NUM_DOC) = ?5 ";
                        break;
                    case "3":   // RESULTADO DE LA CAPACITACION
                        if (mMap.get("tipoResultado").toString().equals("APROBADO")) {
                            jpql = jpql + " AND t2.NOTA_EVALUACION IS NOT NULL AND t2.NOTA_EVALUACION >= 12";
                            jpqlBorrador = jpqlBorrador + " and 1 = 2 ";
                        } else {
                            jpql = jpql + " AND t2.NOTA_EVALUACION IS NOT NULL AND t2.NOTA_EVALUACION < 12 ";
                            jpqlBorrador = jpqlBorrador + " and 1 = 2 ";
                        }
                        break;
                }
            }
            jpqlBorrador = jpqlBorrador + " UNION " + jpql;
            jpqlBorrador = jpqlBorrador + " order by TIPOREG desc, FECHA desc";
            Query q = em.createNativeQuery(jpqlBorrador);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, mMap.get("numDoc"));

            if (mMap.get("capacitacionId") != null) {
                q.setParameter(2, mMap.get("capacitacionId"));
            }
            if (mMap.get("fechaInicio") != null && mMap.get("fechaFin") != null) {
                q.setParameter(3, (Date) mMap.get("fechaInicio"), TemporalType.DATE);
                q.setParameter(4, (Date) mMap.get("fechaFin"), TemporalType.DATE);
            }
            if (mMap.get("tipoBusqueda") != null) {
                switch (mMap.get("tipoBusqueda").toString()) {
                    case "1":
                        q.setParameter(5, "%" + mMap.get("filtro") + "%");
                        break;
                    case "2":
                        q.setParameter(5, mMap.get("filtro"));
                        break;
                }
            }

            return q.setMaxResults(500).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * prueba
     *
     * @param tipoBase
     * @param empresa
     * @return
     */
    public List<EppRegistro> listarRGXautorizacionPirotecnicosDos(String tipoBase, String empresa) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select r from EppRegistro r "
                + " where r.activo = 1 and r.estado.codProg = 'TP_REGEV_FIN'"
                + " and r.tipoProId.codProg in " + tipoBase
                + " and r.eppResolucion.activo = 1 ");
        sqlBuilder.append(" and (trim(r.empresaId.id) = :empresa)");
        Query query = em.createQuery(sqlBuilder.toString());
        query.setParameter("empresa", empresa);
        query.setHint("eclipselink.batch.type", "IN");
        return query.setMaxResults(200).getResultList();
    }

    /**
     * LIBROS
     *
     * @param tipoBase
     * @param empresa
     * @return
     */
    public List<EppRegistro> listRegistrosLibros(String tipoBase, String empresa) {
        String jpql = "select r from EppRegistro r "
                + " where r.activo = 1 and FUNC('trunc',r.eppResolucion.fechaFin) >= FUNC('trunc',CURRENT_DATE) "
                + " and r.estado.codProg = 'TP_REGEV_FIN'"
                + " and r.tipoProId.codProg in " + tipoBase
                + " and r.eppResolucion.activo = 1 "
                + " and (trim(r.empresaId.id) = :empresa)"
                ;
        Query query = em.createQuery(jpql);
        query.setParameter("empresa", empresa);
        return query.setMaxResults(200).getResultList();
    }

    public List<EppLibro> listLibros(long p) {
        Query q = em.createQuery("SELECT p FROM EppLibro p where p.empresaId.id = :per and p.activo = 1 and p.registroId.eppResolucion.numero is not null");
        q.setParameter("per", p);
        return q.getResultList();
    }

    public List<EppLibroMes> listLibrosMes(EppLibro p) {
        Query q = em.createQuery("SELECT p FROM EppLibroMes p where p.libroId = :libro and p.activo = 1");
        q.setParameter("libro", p);
        return q.getResultList();
    }

    /**
     * LIBROS
     *
     * @param s
     * @param procesos
     * @param empresa
     * @return
     */
    public List<EppRegistro> buscarResolucion(String s, String procesos, String empresa) {
        String jpql = "";

        jpql = jpql = "SELECT p FROM EppRegistro p \n"
                + "WHERE \n"
                + "p.activo = 1";
//                + " AND p.eppResolucion.numero IS NOT NULL";

        if (procesos.equals("('TP_PRTUP_PLA','TP_PRTUP_AFAB')")) {
            jpql = jpql + " AND (p.tipoProId.codProg)in " + procesos
                    + " AND (FUNC('trunc',p.eppResolucion.fechaFin) >= FUNC('trunc',CURRENT_DATE)"
                    + " OR (p.eppResolucion.fechaFin is null and p.estado.codProg = 'TP_REGEV_FIN'))";
        } else {
            jpql = jpql + " AND (p.tipoProId.codProg)in " + procesos
                    + " and FUNC('trunc',p.eppResolucion.fechaFin) >= FUNC('trunc',CURRENT_DATE) and p.estado.codProg = 'TP_REGEV_FIN'";
        }
        if (empresa != null) {
            jpql = jpql + " and trim(p.empresaId.id) = :empresa ";
        }
        if (s != null) {
            jpql = jpql + " and  (p.eppResolucion.numero like :campo or p.empresaId.ruc like :campo or p.empresaId.rznSocial like :campo ) ";
        }
        jpql = jpql + "order by p.eppResolucion.id desc";
        Query query = em.createQuery(jpql);

        if (s != null) {
            query.setParameter("campo", "%" + s.toUpperCase().trim() + "%");
        }
        if (empresa != null) {
            query.setParameter("empresa", empresa);
        }

        return query.setMaxResults(50).getResultList();

    }

    /**
     *
     * @param regId
     * @param expId
     * @return
     */
    public double saldoActualAutUsoRept(Long regId, Long expId) {
        double cantidadTotalGTtipo1 = 0;
        double cantidadTotalGTtipo2 = 0;
        double cantidadTotalGTtipo3 = 0;
        double cantidadTotalGTtipo4 = 0;

        String jpql1 = "select d.cantidad \"TOTAL1\" \n"
                + "from \n"
                + "bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_resolucion res on res.registro_id = r.id \n"
                + "inner join bdintegrado.epp_detalle_aut_uso d on d.registro_id = r.id \n"
                + "where \n"
                + "d.activo = 1 and \n"
                + "r.id = ?1 and\n"
                + "d.explosivo_id = ?2";

        String jpql2 = "select sum(es.cantidad) \"TOTAL2\"\n"
                + "from \n"
                + "--REGISTRO DE LA GUIA DE TRANSITO\n"
                + "bdintegrado.epp_registro r \n"
                + "--ESTADO DEL REGISTRO DE LA GUIA DE TRANSITO\n"
                + "inner join bdintegrado.tipo_explosivo ter on r.estado = ter.id\n"
                + "--GUIA DE TRANSITO\n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt \n"
                + "on rgt.registro_id = r.id\n"
                + "--RESOLUCION UTILIZADA EN LA GUIA DE TRANSITO\n"
                + "inner join bdintegrado.epp_resolucion res \n"
                + "on rgt.resolucion_id = res.id \n"
                + "--REGISTRO DE LA RESOLUCION UTILIZADA EN LA GUIA DE TRANSITO\n"
                + "inner join bdintegrado.epp_registro r_res \n"
                + "on res.registro_id = r_res.id \n"
                + "--ESTADO DEL REGISTRO DE LA RESOLUCION UTILIZADA EN LA GUIA DE TRANSITO\n"
                + "inner join bdintegrado.tipo_explosivo te \n"
                + "on r_res.estado = te.id\n"
                + "--TIPO DE TRAMITE DE LA GUIA DE TRANSITO\n"
                + "inner join bdintegrado.tipo_explosivo te2 \n"
                + "on rgt.tipo_tramite = te2.id\n"
                + "--DETALLE DE LA GUIA DE TRANSITO\n"
                + "inner join bdintegrado.epp_explosivo_solicitado es \n"
                + "on es.registro_id = rgt.id \n"
                + "where \n"
                + "r.activo = 1 and \n"
                + "--te.cod_prog not in 'TP_REGEV_ANU' and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI1' and \n"
                + "ter.cod_prog not in ('TP_REGEV_ANU') and --'TP_REGEV_EXTPAR',\n"
                + "es.activo = 1 and \n"
                + "r_res.id = ?1 and \n"
                + "es.explosivo_id = ?2";

        String jpql3 = "select sum(e.cantidad_extornada) \"TOTAL3\" \n"
                + "from \n"
                + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                + "inner join bdintegrado.epp_resolucion res on rgt1.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_registro r9 on rgt1.registro_id = r9.id \n"
                + "--REGISTRO DE LA RESOLUCION REFERENCIADA EN LA GUIA DE TRANSITO\n"
                + "inner join bdintegrado.epp_registro rrgt on res.registro_id = rrgt.id\n"
                + "inner join bdintegrado.tipo_explosivo t9 on r9.estado = t9.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt1.id = e.registro_id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "t9.cod_prog in ('TP_REGEV_EXT','TP_REGEV_EXTPAR','TP_REGEV_EXTTOT') and \n"
                + "rrgt.id = ?1 and \n"
                + "e.explosivo_id = ?2";

        String jpql4 = "select sum(e.cantidad) \"TOTAL4\" \n"
                + "from \n"
                + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt1.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo te1 on rgt1.tipo_tramite =te1.id \n"
                + "inner join bdintegrado.epp_resolucion res1 on rgt1.resolucion_id = res1.id \n"
                + "--REFERENCIANDO A REGISTRO DE LA GUIA REFERENCIADA\n"
                + "inner join bdintegrado.epp_registro rx on res1.registro_id = rx.id \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgtx on rgtx.registro_id = rx.id \n"
                + "inner join bdintegrado.epp_resolucion resx on rgtx.resolucion_id= resx.id\n"
                + "inner join bdintegrado.epp_registro resxr on resx.registro_id= resxr.id\n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te1.cod_prog = 'TP_RGUIA_DEVOL' and \n"
                + "resxr.id = ?1 and \n"
                + "e.explosivo_id = ?2";

        Query q1 = em.createNativeQuery(jpql1);
        Query q2 = em.createNativeQuery(jpql2);
        Query q3 = em.createNativeQuery(jpql3);
        Query q4 = em.createNativeQuery(jpql4);

        q1.setParameter(1, regId);
        q1.setParameter(2, expId);

        q2.setParameter(1, regId);
        q2.setParameter(2, expId);

        q3.setParameter(1, regId);
        q3.setParameter(2, expId);

        q4.setParameter(1, regId);
        q4.setParameter(2, expId);

        if (!q1.getResultList().isEmpty() && !(q1.getResultList().get(0) == null)) {
            cantidadTotalGTtipo1 = Double.valueOf(q1.getResultList().get(0).toString());
        }

        if (!q2.getResultList().isEmpty() && !(q2.getResultList().get(0) == null)) {
            cantidadTotalGTtipo2 = Double.valueOf(q2.getResultList().get(0).toString());
        }

        if (!q3.getResultList().isEmpty() && !(q3.getResultList().get(0) == null)) {
            cantidadTotalGTtipo3 = Double.valueOf(q3.getResultList().get(0).toString());
        }

        if (!q4.getResultList().isEmpty() && !(q4.getResultList().get(0) == null)) {
            cantidadTotalGTtipo4 = Double.valueOf(q4.getResultList().get(0).toString());
        }

        return (cantidadTotalGTtipo1 - cantidadTotalGTtipo2 + cantidadTotalGTtipo3 + cantidadTotalGTtipo4);

    }

    public List<EppRegistro> buscarRegistroXCertificado(Long id) {
        String jpql = "";

        jpql = "select p from EppRegistro p "
                + " WHERE p.activo = 1 and p.id = :id "
                + " and p.eppCertificado.activo = 1 ";

        Query query = em.createQuery(jpql);
        query.setParameter("id", id);

        return query.getResultList();
    }
    
    
    public List<Map> rptGuiaDetailExp(String id) {
        String sql = "SELECT\n"
                + "RGT.ID,\n"
                + "E.NOMBRE \"EXPS_NOM\",\n"
                + "EXS.CANTIDAD \"EXPS_CANT_AUT\",\n"
                + "DECODE(EXS.CANTIDAD_EXTORNADA,NULL,EXS.CANTIDAD,EXS.CANTIDAD_EXTORNADA) \"EXPS_CANT_SOLIC\",\n"
                + "UM.NOMBRE \"EXPS_UM\"\n"
                + "FROM BDINTEGRADO.EPP_REGISTRO_GUIA_TRANSITO RGT \n"
                + "LEFT JOIN BDINTEGRADO.EPP_RESOLUCION RES ON RGT.RESOLUCION_ID = RES.ID --LEFT\n"
                + "LEFT JOIN BDINTEGRADO.EPP_REGISTRO R ON RES.REGISTRO_ID = R.ID --LEFT\n"
                + "INNER JOIN BDINTEGRADO.EPP_EXPLOSIVO_SOLICITADO EXS ON EXS.REGISTRO_ID = RGT.ID \n"
                + "INNER JOIN BDINTEGRADO.EPP_EXPLOSIVO E ON EXS.EXPLOSIVO_ID = E.ID  \n"
                + "INNER JOIN BDINTEGRADO.EPP_EXPLOSIVO_UNIDAD EU ON EU.EXPLOSIVO_ID = E.ID  \n"
                + "INNER JOIN BDINTEGRADO.UNIDAD_MEDIDA UM ON UM.ID = EU.UNIDAD_MEDIDA_ID  \n"
                + "WHERE RGT.ID = ? AND EXS.ACTIVO = 1 \n"
                + "ORDER BY 1";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, id);
        res = query.getResultList();
        return res;
    }
    
    public List<Map> rptGuiaDetailExpGte(String id) {
        String sql = "SELECT"
                + " REG.ID,"
                + " E.NOMBRE \"EXPS_NOM\", "
                + " EXS.CANTIDAD \"EXPS_CANT_AUT\","
                + " UM.NOMBRE \"EXPS_UM\"\n"
                + " FROM BDINTEGRADO.EPP_GTE_REGISTRO REG "
                + " INNER JOIN BDINTEGRADO.EPP_GTE_EXPLOSIVO_SOLICITA EXS ON EXS.REGISTRO_ID = REG.ID "
                + " INNER JOIN BDINTEGRADO.EPP_EXPLOSIVO E ON EXS.EXPLOSIVO_ID = E.ID  "
                + " INNER JOIN BDINTEGRADO.EPP_EXPLOSIVO_UNIDAD EU ON EU.EXPLOSIVO_ID = E.ID  "
                + " INNER JOIN BDINTEGRADO.UNIDAD_MEDIDA UM ON UM.ID = EU.UNIDAD_MEDIDA_ID  "
                + " WHERE REG.ID = ? AND EXS.ACTIVO = 1 \n"
                + " ORDER BY 1";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, id);
        res = query.getResultList();
        return res;
    }
    
    public List<EppRegistro> buscarAutorizacionesComercializacionGTE(SbPersonaGt per, SbPersonaGt perRestricc) {

        String jpaQ = "select distinct(r) from EppRegistro r \n"
                    + "inner join r.eppResolucion res\n"
                    + "where \n"
                    + "r.tipoProId.codProg in ('TP_PRTUP_COM','TP_PRTUP_ACOM') and \n"
                    + "r.activo = 1 and res.activo = 1 and \n"
                    + "r.estado.codProg = 'TP_REGEV_FIN' and \n"
                    + "(FUNC('trunc',CURRENT_DATE) >= FUNC('trunc',res.fechaIni)) ";
                    
        if(per != null){
            jpaQ += " and (r.empresaId.id = ?1) ";
        }
        if(perRestricc != null){
            jpaQ += " and (r.empresaId.id != ?2) ";
        }
        Query query = em.createQuery(jpaQ);
        if(per != null){
            query.setParameter(1, per.getId());
        }
        if(perRestricc != null){
            query.setParameter(2, perRestricc.getId());
        }
        return query.setMaxResults(50).getResultList();
    }
    
    
    public List<EppRegistro> buscarLMEBandeja(HashMap mMap) {
        try {
            List<EppRegistro> lst = new ArrayList();
            String jpql = "select distinct(r) from EppRegistro r left join r.eppLicenciaList1 l where r.activo = 1 and r.tipoProId.codProg in ('TP_PRTUP_AMAN','TP_PRTUP_MAN') "
                    + " and r.estado.codProg = 'TP_REGEV_FIN' "
                    + " AND r.tipoOpeId.codProg not in ('TP_OPE_CAN','TP_OPE_COF') ";

            if (mMap.get("tipoBusqueda") != null) {
                switch (mMap.get("tipoBusqueda").toString()) {
                    case "1":   // PORTADOR
                        jpql = jpql + " AND (trim(l.personaId.numDoc) like :campo OR UPPER(trim(l.personaId.apePat)) like :campo OR UPPER(trim(l.personaId.apeMat)) like :campo OR UPPER(trim(l.personaId.nombres)) like :campo )";
                        break;
                    case "2":   // NRO. LICENCIA
                        jpql = jpql + " AND l.nroLicencia is not null and trim(l.nroLicencia) = :campo";
                        break;
                }
            }
            jpql = jpql + " order by r.id desc";
            Query q = em.createQuery(jpql);

            if (mMap.get("tipoBusqueda") != null) {
                switch (mMap.get("tipoBusqueda").toString()) {
                    case "1":
                            q.setParameter("campo", "%" + mMap.get("filtro") + "%");
                            break;
                    case "2":
                            q.setParameter("campo", mMap.get("filtro"));
                            break;
                }
            }

            lst = q.setMaxResults(200).getResultList();
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Integer contarRegistrosReferenciadasXId(Long registroId) {
        try {
            int cont = 0;
            String jpql = "select count(r) from EppRegistro r "
                    + " where r.activo = 1 and r.estado.codProg not in ('TP_REGEV_CRE','TP_REGEV_ANU','TP_REGEV_EXT','TP_REGEV_EXTPAR') "
                    + " and r.registroId.id = :registroId ";

            Query query = em.createQuery(jpql);
            query.setParameter("registroId", registroId);

            List<Long> results = query.getResultList();
            for (Long _values : results) {
                cont = _values.intValue();
                break;
            }

            return cont;
        } catch (Exception e) {
            e.printStackTrace();            
        }   
        return null;
    }
    
    public EppRegistro buscarRegistrosReferenciadasXId(Long registroId) {
        try {
            String jpql = "select distinct r from EppRegistro r "
                    + " where r.activo = 1 and r.estado.codProg not in ('TP_REGEV_CRE','TP_REGEV_ANU','TP_REGEV_EXT','TP_REGEV_EXTPAR') "
                    + " and r.registroId.id = :registroId "
                    + " order by r.id desc ";

            Query query = em.createQuery(jpql);
            query.setParameter("registroId", registroId);

            if(query.getResultList().isEmpty()){
               return null; 
            }
            return (EppRegistro) query.getResultList().get(0);
        } catch (Exception e) {
            e.printStackTrace();            
        }   
        return null;
    }
}
