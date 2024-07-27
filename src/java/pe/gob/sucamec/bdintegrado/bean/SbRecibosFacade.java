/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbRecibosFacade extends AbstractFacade<SbRecibos> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbRecibosFacade() {
        super(SbRecibos.class);
    }

    public List<SbRecibos> lstRecibos(String nrodoc, String filtro) {
        try {
            String jpql = "select r from SbRecibos r "
                    + "where "
                    + "r.importe = 163.50 and "
                    + "r.nroExpediente is null and \n"
                    + "r.devolucion is null and \n"
                    + "r.registroId is null and \n"
                    + "r.nroDocumento = :nrodoc and "
                    + "(trim(r.nroSecuencia) like :filtro or :filtro is null)";
            javax.persistence.Query q = em.createQuery(jpql);
            q.setParameter("nrodoc", nrodoc);
            q.setParameter("filtro", filtro == null ? null : ("%" + filtro + "%"));
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SbRecibos buscarReciboXIdRegistro(EppRegistro r) {
        try {
            String jpql = "select r from SbRecibos r where r.registroId.id = :id";
            javax.persistence.Query q = em.createQuery(jpql);
            q.setParameter("id", r.getId());
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return (SbRecibos) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * LISTAR RECIBOS PARA BUSQUEDA SEL
     *
     * @param persona
     * @param mMap
     * @return
     */
    public List<Map> listarMapRecibosBusquedaSEL(SbPersonaGt persona, HashMap mMap) {
        String query1 = "  SELECT R.NRO_SECUENCIA FROM BDINTEGRADO.SB_RECIBOS R WHERE R.ACTIVO = 1 AND R.COD_TRIBUTO = ?1 "
                + "   AND (R.NRO_DOCUMENTO = ?2 OR R.NRO_DOCUMENTO = ?3 ) "
                + ((mMap.get("importe") != null) ? " AND R.IMPORTE = ?4" : "");
        if (mMap.get("tipo") != null) {
            switch (mMap.get("tipo").toString()) {
                case "nro":
                    query1 = query1 + " AND trim(R.NRO_SECUENCIA) = ?5";
                    break;
                case "fecha":
                    query1 = query1 + " AND R.FECHA_MOVIMIENTO = ?6 ";
                    break;
            }
        }

        String jpql = "SELECT RE.ID,RE.NRO_SECUENCIA,RE.FECHA_MOVIMIENTO,RE.IMPORTE FROM BDINTEGRADO.SB_RECIBOS RE WHERE RE.NRO_SECUENCIA IN "
                + " ("
                + query1
                + " MINUS "
                + "  SELECT TO_NUMBER(NVL(A.VALOR, 0)) "
                + "  FROM TRAMDOC.DOCUMENTO D "
                + "  INNER JOIN TRAMDOC.CAMPO_POR_DOCUMENTO A ON D.ID_DOCUMENTO = A.ID_DOCUMENTO AND A.ID_CAMPO = 3"
                + "  WHERE D.ID_TIPO_DOCUMENTO = 25 and TRANSLATE(A.VALOR,'.0123456789','T') IS NULL "
                + " MINUS "
                + "  SELECT REC.NRO_SECUENCIA FROM BDINTEGRADO.SB_RECIBOS REC "
                + "  INNER JOIN BDINTEGRADO.SB_RECIBO_REGISTRO REG ON REG.RECIBO_ID=REC.ID "
                + "  WHERE REC.ACTIVO = 1 AND REG.ACTIVO = 1 "
                + ")";

        Query q = em.createNativeQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, (Long) mMap.get("codigo"));
        q.setParameter(2, persona.getRuc());
        q.setParameter(3, persona.getNumDoc());
        if (mMap.get("importe") != null) {
            q.setParameter(4, (Double) mMap.get("importe"));
        }
        if (mMap.get("tipo") != null) {
            switch (mMap.get("tipo").toString()) {
                case "nro":
                    q.setParameter(5, "" + mMap.get("filtro"));
                    break;
                case "fecha":
                    q.setParameter(6, (Date) mMap.get("fecha"), TemporalType.DATE);
                    break;
            }
        }

        return q.setMaxResults(200).getResultList();
    }

    /**
     * LISTAR RECIBOS PARA BUSQUEDA SEL
     *
     * @param persona
     * @param mMap
     * @return
     */
    public List<SbRecibos> listarRecibosBusquedaSEL(SbPersonaGt persona, HashMap mMap) {
        String jpql = "SELECT distinct(r) FROM SbRecibos r LEFT JOIN r.sbReciboRegistroList l ON l.activo =1 "
                + " WHERE r.activo = 1 AND l.id IS NULL and (trim(r.nroDocumento) = :ruc OR trim(r.nroDocumento) = :numdoc) "
                + " AND r.codTributo =:codigo "
                + " AND ( r.devolucion IS NULL OR r.devolucion = 0 )";

        if (mMap.get("importe") != null) {
            jpql = jpql + " AND r.importe = :importe ";
        }
        if (mMap.get("nroSecuencia") != null) {
            jpql = jpql + " AND trim(r.nroSecuencia) like :nro";
        }

        Query q = em.createQuery(jpql);
        q.setParameter("ruc", persona.getRuc());
        q.setParameter("numdoc", persona.getNumDoc());
        q.setParameter("codigo", (Long) mMap.get("codigo"));
        if (mMap.get("importe") != null) {
            q.setParameter("importe", (Double) mMap.get("importe"));
        }
        if (mMap.get("nroSecuencia") != null) {
            q.setParameter("nro", "%" + mMap.get("nroSecuencia") + "%");
        }

        return q.getResultList();
    }

    public List<SbRecibos> listarRecibosByImporteByCodAtributo(SbPersonaGt persona, HashMap mMap) {
        try {
            String jpql = "SELECT distinct(r) FROM SbRecibos r LEFT JOIN r.sbReciboRegistroList l ON l.activo =1"
                    + " WHERE r.activo = 1 AND l.id IS NULL"
                    + " AND (trim(r.nroDocumento) = :ruc OR trim(r.nroDocumento) = :numdoc)"
                    + " AND r.codTributo =:codigo "
                    + " AND ( r.devolucion IS NULL OR r.devolucion = 0 ) AND MOD(r.importe,:importe) = 0 AND r.nroExpediente IS NULL ";

            if (mMap.get("tipo") != null) {
                switch (mMap.get("tipo").toString()) {
                    case "nro":
                        jpql = jpql + " AND r.nroSecuencia = :nro";
                        break;

                    case "fecha":
                        jpql = jpql + " AND FUNC('trunc',r.fechaMovimiento) = FUNC('trunc',:fecha) ";
                        break;
                }
            }

            jpql += " AND l.reciboId is null";

            Query q = em.createQuery(jpql);
            q.setParameter("ruc", persona.getRuc());
            q.setParameter("numdoc", persona.getNumDoc());
            q.setParameter("importe", (Double) mMap.get("importe"));
            q.setParameter("codigo", (Long) mMap.get("codigo"));

            if (mMap.get("tipo") != null) {
                switch (mMap.get("tipo").toString()) {
                    case "nro":
                        q.setParameter("nro", Long.parseLong("" + mMap.get("filtro")));
                        break;
                    case "fecha":
                        q.setParameter("fecha", (Date) mMap.get("fecha"), TemporalType.DATE);
                        break;
                }
            }

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SbRecibos buscarReciboXId(Long id) {
        try {
            String jpql = "select r from SbRecibos r where r.id = :id";
            javax.persistence.Query q = em.createQuery(jpql);
            q.setParameter("id", id);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return (SbRecibos) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SbRecibos> listarRecibosByRucByDocByImporteByCodAtributo(SbPersonaGt persona, HashMap mMap) {
        try {
            String jpql = "SELECT distinct(r) FROM SbRecibos r "
                    + " WHERE r.activo = 1 AND (trim(r.nroDocumento) = :ruc OR trim(r.nroDocumento) = :numdoc) "
                    + " AND r.codTributo =:codigo "
                    + " AND ( r.devolucion IS NULL OR r.devolucion = 0 ) AND (r.importe = :importe ) AND r.nroExpediente IS NULL "
                    + " AND r.id not in (SELECT s.reciboId.id FROM SbReciboRegistro s where s.activo = 1 " + ((mMap.get("registroId") != null && mMap.get("comprobanteId") != null) ? " and s.reciboId.id != :comprobanteId " : "") + " ) ";

            Query q = em.createQuery(jpql);
            q.setParameter("ruc", persona.getRuc());
            q.setParameter("numdoc", persona.getNumDoc());
            q.setParameter("importe", (Double) mMap.get("importe"));
            q.setParameter("codigo", (Long) mMap.get("codigo"));
            if (mMap.get("registroId") != null && mMap.get("comprobanteId") != null) {
                q.setParameter("comprobanteId", (Long) mMap.get("comprobanteId"));
            }

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SbRecibos buscarReciboByRucByDocByImporteByCodAtributoBySecuencia(SbPersonaGt persona, HashMap mMap) {
        try {
            String jpql = "SELECT distinct(r) FROM SbRecibos r "
                    + " WHERE r.activo = 1 AND (trim(r.nroDocumento) = :ruc OR trim(r.nroDocumento) = :numdoc) "
                    + " AND r.codTributo =:codigo "
                    + " AND r.nroSecuencia = :nroSecuencia"
                    + " AND ( r.devolucion IS NULL OR r.devolucion = 0 ) AND (r.importe = :importe ) AND r.nroExpediente IS NULL "
                    + " AND r.id not in (SELECT s.reciboId.id FROM SbReciboRegistro s where s.activo = 1 " + ((mMap.get("registroId") != null && mMap.get("comprobanteId") != null) ? " and s.reciboId.id != :comprobanteId " : "") + " ) ";

            Query q = em.createQuery(jpql);
            q.setParameter("ruc", persona.getRuc());
            q.setParameter("numdoc", persona.getNumDoc());
            q.setParameter("importe", (Double) mMap.get("importe"));
            q.setParameter("codigo", (Long) mMap.get("codigo"));
            q.setParameter("nroSecuencia", (Long) mMap.get("nroSecuencia"));
            if (mMap.get("registroId") != null && mMap.get("comprobanteId") != null) {
                q.setParameter("comprobanteId", (Long) mMap.get("comprobanteId"));
            }

            if (!q.getResultList().isEmpty()) {
                return (SbRecibos) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SbRecibos> listarRecibosByFiltro(HashMap mMap) {
        try {
            String jpql = "SELECT distinct(r) FROM SbRecibos r LEFT JOIN r.sbReciboRegistroList l ON l.activo =1"
                    + " WHERE r.activo = 1 AND l.id IS NULL AND (trim(r.nroDocumento) = :numdoc) "
                    + " AND r.codTributo =:codigo "
                    + " AND ( r.devolucion IS NULL OR r.devolucion = 0 ) AND MOD(r.importe,:importe) = 0 AND r.nroExpediente IS NULL ";

            if (mMap.get("tipo") != null) {
                switch (mMap.get("tipo").toString()) {
                    case "nro":
                        jpql = jpql + " AND r.nroSecuencia = :nro";
                        break;

                    case "fecha":
                        jpql = jpql + " AND FUNC('trunc',r.fechaMovimiento) = FUNC('trunc',:fecha) ";
                        break;
                }
            }

            jpql += " AND l.reciboId is null ";
            jpql += " AND FUNC('trunc',r.fechaMovimiento) > FUNC('trunc',:fecha) ";
            //jpql += " AND r.fechaMovimiento > TO_DATE('30/11/2018') ";            

            Query q = em.createQuery(jpql);
            q.setParameter("numdoc", mMap.get("numDoc").toString());
            q.setParameter("importe", (Double) mMap.get("importe"));
            q.setParameter("codigo", (Long) mMap.get("codigo"));
            Date fechaMinEmpoce = new GregorianCalendar(2018, Calendar.DECEMBER, 1).getTime();
            q.setParameter("fecha", fechaMinEmpoce, TemporalType.DATE);

            if (mMap.get("tipo") != null) {
                switch (mMap.get("tipo").toString()) {
                    case "nro":
                        q.setParameter("nro", Long.parseLong("" + mMap.get("filtro")));
                        //q.setParameter("nro", Integer.valueOf(mMap.get("filtro").toString()) );
                        break;
                    case "fecha":
                        q.setParameter("fecha", (Date) mMap.get("fecha"), TemporalType.DATE);
                        break;
                }
            }

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SbRecibos> listarRecibosTramiteVirtualByFiltro(HashMap mMap) {
        try {            
            String jpql = "SELECT distinct(r) FROM SbRecibos r LEFT JOIN r.sbReciboRegistroList l ON l.activo =1";
            if (mMap.get("flag_citas_verif") != null)  {
                jpql += " AND l.nroExpediente != 'CONFIRMADO' ";
            }
            jpql +=  " WHERE r.activo = 1 AND l.id IS NULL "
                    + " AND r.codTributo =:codigo "
                    + " AND ( r.devolucion IS NULL OR r.devolucion = 0 ) AND MOD(r.importe,:importe) = 0 AND r.nroExpediente IS NULL ";
            jpql += " AND r.nroSecuencia = :nro";
            jpql += " AND l.reciboId is null ";
            jpql += " AND (trim(r.nroDocumento) IN ("+ ((mMap.get("numDoc").toString().equals(""))? "0" : mMap.get("numDoc").toString()) +")) ";
            
            Query q = em.createQuery(jpql);
            q.setParameter("importe", (Double) mMap.get("importe"));
            q.setParameter("codigo", (Long) mMap.get("codigo"));
            q.setParameter("nro", Long.parseLong("" + mMap.get("filtro")));

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SbRecibos> listarRecibosVeriByFiltro(HashMap mMap) {
        try {
            String jpql = "SELECT distinct(r) FROM SbRecibos r LEFT JOIN r.sbReciboRegistroList l ON l.activo =1"
                    + " WHERE r.activo = 1 AND l.id IS NULL"
                    + " AND r.codTributo =:codigo"
                    + " AND ( r.devolucion IS NULL OR r.devolucion = 0 ) AND r.nroExpediente IS NULL ";

            if (mMap.get("numDoc") != null) {
                jpql += " AND (trim(r.nroDocumento) = :numdoc)";
            }

            /*if (mMap.get("importes") != null) {
                jpql += " AND ( MOD(r.importe,32.90) = 0 OR MOD(r.importe,35.6) = 0 OR MOD(r.importe,26.3) = 0 OR MOD(r.importe,28.7) = 0 )";
            } else {
                jpql += " AND MOD(r.importe,:importe) = 0";
            }*/
            if (mMap.get("importe") != null) {
                jpql += " AND MOD(r.importe,:importe) = 0 ";
            }
            
            if (mMap.get("secuencia") != null) {
                jpql += " AND r.nroSecuencia = :nroSec";
            }

            jpql += " AND l.reciboId is null ";
            jpql += " AND FUNC('trunc',r.fechaMovimiento) > FUNC('trunc',:fecha) ";

            Query q = em.createQuery(jpql);
            q.setParameter("codigo", (Long) mMap.get("codigo"));
            Date fechaMinEmpoce = new GregorianCalendar(2018, Calendar.DECEMBER, 1).getTime();
            q.setParameter("fecha", fechaMinEmpoce, TemporalType.DATE);
            if (mMap.get("numDoc") != null) {
                q.setParameter("numdoc", mMap.get("numDoc").toString());
            }

            if (mMap.get("importe") != null) {
                q.setParameter("importe", (Double) mMap.get("importe"));
            }

            if (mMap.get("secuencia") != null) {
                q.setParameter("nroSec", Long.parseLong("" + mMap.get("secuencia")));
            }
            
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SbRecibos> validaRecibosByFiltro(HashMap mMap) {
        try {
            String jpql = "SELECT distinct(r) FROM SbRecibos r LEFT JOIN r.sbReciboRegistroList l ON l.activo =1"
                    + " WHERE r.activo = 1 AND l.id IS NULL AND (trim(r.nroDocumento) = :numdoc)"
                    + " AND r.codTributo = :codigo"
                    + " AND r.nroSecuencia = :nroSecuencia"
                    + " AND ( r.devolucion IS NULL OR r.devolucion = 0 ) AND MOD(r.importe,:importe) = 0 AND r.nroExpediente IS NULL"
                    + " AND r.id = :reciboId";

            jpql += " AND l.reciboId is null";
            jpql += " AND FUNC('trunc',r.fechaMovimiento) > FUNC('trunc',:fecha) ";

            Query q = em.createQuery(jpql);
            q.setParameter("numdoc", mMap.get("numDoc").toString());
            q.setParameter("importe", (Double) mMap.get("importe"));
            q.setParameter("codigo", (Long) mMap.get("codigo"));
            q.setParameter("reciboId", (Long) mMap.get("reciboId"));
            q.setParameter("nroSecuencia", (Long) mMap.get("nroSecuencia"));
            Date fechaMinEmpoce = new GregorianCalendar(2018, Calendar.DECEMBER, 1).getTime();
            q.setParameter("fecha", fechaMinEmpoce, TemporalType.DATE);

            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    
    public List<SbRecibos> validaRecibosForSolicitudAutorizacion(HashMap mMap) {
        try {
            String jpql = "SELECT distinct(r) FROM SbRecibos r"
                    + " LEFT JOIN r.sbReciboRegistroList l ON l.activo = 1"
                    + " WHERE r.activo = 1"
                    + " AND l.id IS NULL"
                    + " AND (r.devolucion IS NULL OR r.devolucion = 0 )"
                    + " AND r.nroExpediente IS NULL"
                    + " AND l.reciboId is null"
                    + " AND (trim(r.nroDocumento) = :ruc)"
                    + " AND r.codTributo = :codigo"
                    + " AND r.nroSecuencia = :nroSecuencia";

            Query q = em.createQuery(jpql);
            
            String sRUC = mMap.get("ruc").toString();
            Long sCodigo = Long.parseLong(mMap.get("codigo")+"");
            Long sNroSecuencia = Long.parseLong(mMap.get("nroSecuencia")+"");
            
            q.setParameter("ruc", sRUC);
            q.setParameter("codigo", sCodigo);            
            q.setParameter("nroSecuencia", sNroSecuencia);

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    public SbRecibos listarRecibosBusquedaSELXNroSecuencia(SbPersonaGt persona, HashMap mMap) {
        String jpql = "SELECT distinct(r) FROM SbRecibos r LEFT JOIN r.sbReciboRegistroList l ON l.activo =1 "
                + " WHERE r.activo = 1 AND l.id IS NULL and (trim(r.nroDocumento) = :ruc OR trim(r.nroDocumento) = :numdoc) "
                + " AND r.codTributo = :codigo "
                + " AND ( r.devolucion IS NULL OR r.devolucion = 0 )";

        if (mMap.get("importe") != null) {
            jpql = jpql + " AND r.importe = :importe ";
        }
        if (mMap.get("nroSecuencia") != null) {
            jpql = jpql + " AND r.nroSecuencia = :nroSecuencia";
        }

        Query q = em.createQuery(jpql);
        q.setParameter("ruc", persona.getRuc());
        q.setParameter("numdoc", persona.getNumDoc());
        q.setParameter("codigo", (Long) mMap.get("codigo"));
        if (mMap.get("importe") != null) {
            q.setParameter("importe", (Double) mMap.get("importe"));
        }
        if (mMap.get("nroSecuencia") != null) {
            q.setParameter("nroSecuencia", Long.parseLong(mMap.get("nroSecuencia").toString()));
        }

        if (!q.getResultList().isEmpty()) {
            return (SbRecibos) q.getResultList().get(0);
        }
        return null;
    }

    public SbRecibos obtenerReciboCydoc(HashMap mMap) {
        String jpql = "SELECT * FROM TRAMDOC.VST_RECIBOS_CYDOC WHERE EMPOCE = ?1  AND FECHA = TRUNC(?2) AND IMPORTE = ?3 ";

        Query q = em.createQuery(jpql);
        q.setParameter(1, Long.parseLong(mMap.get("nroSecuencia").toString()));
        q.setParameter(2, (Date) mMap.get("fecha"), TemporalType.DATE);
        q.setParameter(3, (Double) mMap.get("importe"));

        if (!q.getResultList().isEmpty()) {
            return (SbRecibos) q.getResultList().get(0);
        }
        return null;
    }
    
    public SbRecibos buscarReciboXNroExpediente(String nroExpediente) {
        try {
            String jpql = "select r.reciboId from SbReciboRegistro r where r.nroExpediente = :nroExpediente order by r.reciboId.id desc";
            javax.persistence.Query q = em.createQuery(jpql);
            q.setParameter("nroExpediente", nroExpediente);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return (SbRecibos) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SbRecibos> listarRecibosPorCriterios(SbPersona persona, HashMap mMap) {
        try {
            String jpql = "SELECT distinct(r) FROM SbRecibos r LEFT JOIN r.sbReciboRegistroList l ON l.activo =1 "
                    + " WHERE r.activo = 1 AND l.id IS NULL AND (trim(r.nroDocumento) = :ruc OR trim(r.nroDocumento) = :numdoc) "
                    + " AND r.codTributo =:codigo "
                    + " AND ( r.devolucion IS NULL OR r.devolucion = 0 ) AND MOD(r.importe,:importe) = 0 AND r.nroExpediente IS NULL ";

            if (mMap.get("tipo") != null) {
                switch (mMap.get("tipo").toString()) {
                    case "nro":
                        jpql = jpql + " AND r.nroSecuencia = :nro";
                        break;

                    case "fecha":
                        jpql = jpql + " AND FUNC('trunc',r.fechaMovimiento) = FUNC('trunc',:fecha) ";
                        break;
                }
            }
            Query q = em.createQuery(jpql);
            q.setParameter("ruc", persona.getRuc());
            q.setParameter("numdoc", persona.getNumDoc());
            q.setParameter("importe", (Double) mMap.get("importe"));
            q.setParameter("codigo", (Long) mMap.get("codigo"));

            if (mMap.get("tipo") != null) {
                switch (mMap.get("tipo").toString()) {
                    case "nro":
                        q.setParameter("nro", Long.parseLong("" + mMap.get("filtro")));
                        break;
                    case "fecha":
                        q.setParameter("fecha", (Date) mMap.get("fecha"), TemporalType.DATE);
                        break;
                }
            }

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SbRecibos> listarRecibosPendientes(SbPersona persona, HashMap mMap) {
        try {
            String jpql = "SELECT r from SbRecibos r LEFT JOIN r.sbReciboRegistroList l "
                    + "where r.activo=1 AND l.activo = 1 "
                    + "AND r.codTributo =:codigo "
                    + "AND MOD(r.importe,:importe) = 0 "
                    + "AND (trim(r.nroDocumento) = :ruc OR trim(r.nroDocumento) = :numdoc) ";
            Query q = em.createQuery(jpql);
            q.setParameter("ruc", persona.getRuc());
            q.setParameter("numdoc", persona.getNumDoc());
            q.setParameter("importe", (Double) mMap.get("importe"));
            q.setParameter("codigo", (Long) mMap.get("codigo"));
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
