/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbExpVirtualSolicitud;

/**
 *
 * @author msalinas
 */
@Stateless
public class SbExpVirtualSolicitudFacade extends AbstractFacade<SbExpVirtualSolicitud> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbExpVirtualSolicitudFacade() {
        super(SbExpVirtualSolicitud.class);
    }

    public Long obtenerNroSolicitud() {
        Long secuencia = 0L;
        String jpql = "SELECT BDINTEGRADO.SEQ_EXP_VIRTUAL_SOLIC_NUMERO.NEXTVAL FROM DUAL";
        Query q = em.createNativeQuery(jpql);
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            secuencia = _values.longValue();
            break;
        }
        return secuencia;
    }

    public List<SbExpVirtualSolicitud> listarSolicitudesXCriterios(String filtro, String criterio, Long perAdministradoId) {
        String sql = "select a from SbExpVirtualSolicitud a where a.activo = 1 and a.administradoId.id = "
                + perAdministradoId;
        List<SbExpVirtualSolicitud> listRes = new ArrayList<>();
        try {
            if (criterio.equals("procedTupa")) {
                sql += " and a.procedimientoTupaId.tupaProcedimientoId.nombre like :filtro ";
            }
            if (criterio.equals("nroExpediente")) {
                sql += " and a.nroExpediente like :filtro ";
            }
            if (criterio.equals("nroSolicitud")) {
                sql += " and a.numeroSolicitud like :filtro ";
            }
            Query q = em.createQuery(sql);
            q.setParameter("filtro", "%" + filtro + "%");
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }

    public List<SbExpVirtualSolicitud> listarSolicitudesXCriterios(String filtro, String criterio, Long perAdministradoId, String estado) {
        String sql = "select a from SbExpVirtualSolicitud a where a.activo = 1 and a.administradoId.id = "
                + perAdministradoId;
        List<SbExpVirtualSolicitud> listRes = new ArrayList<>();
        try {
            if (criterio.equals("procedTupa")) {
                sql += " and a.procedimientoTupaId.tupaProcedimientoId.nombre like :filtro ";
            }
            if (criterio.equals("nroExpediente")) {
                sql += " and a.nroExpediente like :filtro ";
            }
            if (criterio.equals("nroSolicitud")) {
                sql += " and a.numeroSolicitud like :filtro ";
            }
            if (estado != null) {
                sql += " and a.estadoId.codProg = '" + estado + "'";
            }
            sql += " order by a.id desc";
            //System.out.println(sql);
            Query q = em.createQuery(sql);
            if (criterio != "") {
                q.setParameter("filtro", "%" + filtro + "%");
            }

            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }

    public List<SbExpVirtualSolicitud> listarSolicitudesXCriterios(String filtro, String criterio, Long perAdministradoId, String estado, Long expSede) {
        String sql = "select a from SbExpVirtualSolicitud a where a.activo = 1 and a.administradoId.id = "
                + perAdministradoId + " and a.sedeSucamecId.id= "
                + expSede;
        List<SbExpVirtualSolicitud> listRes = new ArrayList<>();
        try {
            if (criterio.equals("procedTupa")) {
                sql += " and a.procedimientoTupaId.tupaProcedimientoId.nombre like :filtro ";
            }
            if (criterio.equals("nroExpediente")) {
                sql += " and a.nroExpediente like :filtro ";
            }
            if (criterio.equals("nroSolicitud")) {
                sql += " and a.numeroSolicitud like :filtro ";
            }
            if (estado != null) {
                sql += " and a.estadoId.codProg in (" + estado + ")";
            }
            sql += " order by a.id desc";
            //System.out.println(sql);
            Query q = em.createQuery(sql);
            if (criterio != "") {
                q.setParameter("filtro", "%" + filtro + "%");
            }
            //System.out.println("QUERY = " + sql);
            
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }

    public List<SbExpVirtualSolicitud> listarSolicitudesXCliente(String numDoc, String codProgSubTramite) {
        String sql = "select a from SbExpVirtualSolicitud a where a.activo = 1 and a.administradoId.numDoc = :numDoc";
        List<SbExpVirtualSolicitud> listRes = new ArrayList<>();
        try {
            switch (codProgSubTramite) {
                case "TP_TRAM_REC":
                    sql += " and a.procedimientoTupaId.cydocIdProceso in (788)";
                    sql += " and a.estadoId.codProg in ('TP_EXVIEST_APRO')";
                    break;
                case "TP_TRAM_VEF":
                    break;
                default:
                    break;
            }

            sql += " order by a.id desc";
            //System.out.println(sql);

            Query q = em.createQuery(sql);
            q.setParameter("numDoc", numDoc);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }

    public List<SbExpVirtualSolicitud> listarSolicitudesXCliente(String numDoc) {
        String sql = "select a from SbExpVirtualSolicitud a where a.activo = 1 and a.administradoId.numDoc = :numDoc";
        List<SbExpVirtualSolicitud> listRes = new ArrayList<>();
        try {

            sql += " order by a.id desc";
            Query q = em.createQuery(sql);
            q.setParameter("numDoc", numDoc);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }

    public Long obtenerIdUsuarioTDSegunListaSede(String strListaUsuTD) {
        List<Map> listRes = new ArrayList<>();
        String sql = "SELECT USUARIO FROM (\n"
                + "SELECT U.ID USUARIO,COUNT(EVS.ID) CANT FROM (\n"
                + "SELECT \n"
                + "regexp_substr(regexp_substr('" + strListaUsuTD.trim() + "','[^,]+', 1, level),'[^+]+',1,1) USU\n"
                + "FROM DUAL connect by regexp_substr('" + strListaUsuTD.trim() + "', '[^,]+', 1, level) is not null) UP\n"
                + "INNER JOIN BDINTEGRADO.SB_USUARIO U ON U.LOGIN = UP.USU AND U.ACTIVO = 1\n"
                + "LEFT JOIN BDINTEGRADO.SB_EXP_VIRTUAL_SOLICITUD EVS ON EVS.USUARIO_RECEPCION_ID = U.ID AND EVS.ACTIVO =1 AND TRUNC(SYSDATE) = TRUNC(EVS.FECHA_SOLICITUD) \n"
                + "GROUP BY U.ID ORDER BY COUNT(EVS.ID) ASC, U.ID ASC\n"
                + ") WHERE ROWNUM = 1";
        Query query = em.createNativeQuery(sql);
        Long cant = ((BigDecimal) query.getSingleResult()).longValue();
        return cant;
    }

    public SbExpVirtualSolicitud buscarSolicitudPorRecibo(Map datos) {
        String sql = "select a from SbExpVirtualSolicitud a where a.activo = 1 AND a.comprobanteId.activo =1 AND a.comprobanteId.codTributo =:codigo ";
        SbExpVirtualSolicitud res = null;
        try {
            sql += " AND ( a.comprobanteId.devolucion IS NULL OR a.comprobanteId.devolucion = 0 ) AND MOD(a.comprobanteId.importe,:importe) = 0 AND a.comprobanteId.nroExpediente IS NULL ";
            sql += " AND a.estadoId.codProg not in ('TP_EXVIEST_NPR','TP_EXVIEST_APRO','TP_EXVIEST_TRA') ";
            sql += " AND a.comprobanteId.nroSecuencia = :nro AND (trim(a.comprobanteId.nroDocumento) IN (" + datos.get("numDoc").toString() + ")) ";
            sql += " order by a.id desc";
            //System.out.println("QUERY : " + sql);
            Query q = em.createQuery(sql);
            q.setParameter("importe", (Double) datos.get("importe"));
            q.setParameter("codigo", (Long) datos.get("codigo"));
            q.setParameter("nro", Long.parseLong("" + datos.get("filtro")));
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                res = (SbExpVirtualSolicitud) q.getResultList().get(0);
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return res;
    }

    public int contarSolicitudesPorSolicitanteYEstado(Long solicitanteId, Long estadoId) {
        int cont = 0;
        String jpql = "select count(s) from SbExpVirtualSolicitud s where s.activo = 1 "
                + " and s.administradoId.id = :solicitanteId "
                + " and s.estadoId.id = :idEstado ";

        Query q = em.createQuery(jpql);
        q.setParameter("solicitanteId", solicitanteId);
        q.setParameter("idEstado", estadoId);

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }

        return cont;
    }
    
    public List<SbExpVirtualSolicitud> listarSolicitudesXIdSolicitud(Long idSolicitud) {
            String sql = "select a from SbExpVirtualSolicitud a where a.activo = 1 and a.id = :idSolicitud";
            List<SbExpVirtualSolicitud> listRes = new ArrayList<>();
        try {    
            Query q = em.createQuery(sql);
            q.setParameter("idSolicitud",idSolicitud);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                    listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }

}