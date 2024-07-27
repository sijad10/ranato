/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.cydoc.beans;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;

/**
 *
 * @author ddiaz
 */
@Stateless
public class CdExpedienteFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    private static final int MAX_RES = 500;

    public String nullATodo(String s, boolean like) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        if (like) {
            return "%" + s + "%";
        } else {
            return s.replace("%", "");
        }
    }

    public List<ArrayRecord> listarExpedientePub(String nroExpediente) {
        Query q = em.createNativeQuery(
                "SELECT E.ID_EXPEDIENTE, E.NUMERO, C.NUMERO_IDENTIFICACION, P.NOMBRE AS PROCESO, "
                + "TRUNC(E.FECHA_CREACION) AS FECHA, E.TITULO, E.OBSERVACIONMP, EXTRACT (YEAR FROM E.FECHA_CREACION) AS ANHO, "
                + "CASE WHEN E.ESTADO = 'R' THEN NVL(A.NOMBRE, 'Ingresado') WHEN E.ESTADO = 'X' THEN 'Archivado' ELSE ' ' END AS ESTADO\n"
                + "FROM\n"
                + "    TRAMDOC.EXPEDIENTE E INNER JOIN TRAMDOC.CLIENTE C ON E.ID_CLIENTE = C.ID_CLIENTE \n"
                + "    INNER JOIN TRAMDOC.PROCESO P ON E.ID_PROCESO = P.ID_PROCESO \n"
                + "    INNER JOIN TRAMDOC.TRAZA T ON E.ID_EXPEDIENTE = T.ID_EXPEDIENTE AND T.ACTUAL = 1\n"
                + "    LEFT JOIN TRAMDOC.ACCION_TRAZA AT ON T.ID_TRAZA = AT.ID_TRAZA\n"
                + "    LEFT JOIN TRAMDOC.ACCION A ON AT.ID_ACCION = A.ID_ACCION \n"
                + "WHERE\n"
                + "    E.NUMERO = ?1 \n"
                + "ORDER BY 1 DESC"
        );
        q.setParameter(1,nroExpediente);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<ArrayRecord> listarExpedientePub(String nroExpediente, String doc, String anho) {
        Query q = em.createNativeQuery(
                "SELECT E.ID_EXPEDIENTE, E.NUMERO, C.NUMERO_IDENTIFICACION, P.NOMBRE AS PROCESO, "
                + "TRUNC(E.FECHA_CREACION) AS FECHA, E.TITULO, E.OBSERVACIONMP, EXTRACT (YEAR FROM E.FECHA_CREACION) AS ANHO, "
                + "CASE WHEN E.ESTADO = 'R' THEN NVL(A.NOMBRE, 'Ingresado') WHEN E.ESTADO = 'X' THEN 'Archivado' ELSE ' ' END AS ESTADO\n"
                + "FROM\n"
                + "    TRAMDOC.EXPEDIENTE E INNER JOIN TRAMDOC.CLIENTE C ON E.ID_CLIENTE = C.ID_CLIENTE \n"
                + "    INNER JOIN TRAMDOC.PROCESO P ON E.ID_PROCESO = P.ID_PROCESO \n"
                + "    INNER JOIN TRAMDOC.TRAZA T ON E.ID_EXPEDIENTE = T.ID_EXPEDIENTE AND T.ACTUAL = 1\n"
                + "    LEFT JOIN TRAMDOC.ACCION_TRAZA AT ON T.ID_TRAZA = AT.ID_TRAZA\n"
                + "    LEFT JOIN TRAMDOC.ACCION A ON AT.ID_ACCION = A.ID_ACCION \n"
                + "WHERE\n"
                + "    (E.NUMERO LIKE ?1 OR \n"
                + "    C.NUMERO_IDENTIFICACION LIKE ?2 ) AND \n"
                + "    EXTRACT (YEAR FROM E.FECHA_CREACION) = ?3 \n"
                + "ORDER BY 1 DESC"
        );
        q.setParameter(1, nullATodo(nroExpediente, false));
        q.setParameter(2, nullATodo(doc, false));
        q.setParameter(3, anho);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<ArrayRecord> listarTrazaPub(Long idExpediente) {
        Query q = em.createNativeQuery(
                "SELECT TRUNC(T.FECHA_CREACION) AS FECHA, AR.NOMBRE AS AREA, CASE WHEN T.ACCION = 'Registrar' THEN 'Ingresado' WHEN T.ACCION = 'Archivar' THEN 'Archivado' ELSE NVL(A.NOMBRE, 'Pendiente') END AS ESTADO, NVL(T.OBSERVACION_ADMINISTRADO, T.OBSERVACION) AS OBSERVACION\n"
                + "FROM\n"
                + "    TRAMDOC.EXPEDIENTE E INNER JOIN TRAMDOC.CLIENTE C ON E.ID_CLIENTE = C.ID_CLIENTE \n"
                + "    INNER JOIN TRAMDOC.PROCESO P ON E.ID_PROCESO = P.ID_PROCESO \n"
                + "    INNER JOIN TRAMDOC.TRAZA T ON E.ID_EXPEDIENTE = T.ID_EXPEDIENTE \n"
                + "    LEFT JOIN TRAMDOC.ACCION_TRAZA AT ON T.ID_TRAZA = AT.ID_TRAZA AND AT.ID_ACCION IN (17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 32)\n"
                + "    LEFT JOIN TRAMDOC.ACCION A ON AT.ID_ACCION = A.ID_ACCION \n"
                + "    LEFT JOIN TRAMDOC.USUARIO_POR_TRAZA UPT ON T.ID_TRAZA = UPT.TRAZA AND UPT.RESPONSABLE = 1\n"
                + "    LEFT JOIN TRAMDOC.USUARIO U ON UPT.USUARIO = U.ID_USUARIO \n"
                + "    LEFT JOIN TRAMDOC.AREA AR ON U.AREA = AR.ID_AREA\n"
                + "WHERE\n"
                + "    E.ID_EXPEDIENTE = ?1\n"
                + "    AND CASE WHEN T.ACCION = 'Registrar' THEN 'Ingresado' WHEN T.ACCION = 'Archivar' THEN 'Archivado' ELSE NVL(A.NOMBRE, 'Pendiente') END != 'Pendiente' \n"
                + "ORDER BY T.FECHA_CREACION DESC, T.ID_TRAZA, A.ID_ACCION"
        );
        q.setParameter(1, idExpediente);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<ArrayRecord> listarExpediente(String numDoc, String nroExpediente, String titulo, String estado, Date fechaInicio, Date fechaFin) {
        Query q = em.createNativeQuery(
                "SELECT E.ID_EXPEDIENTE, E.NUMERO, P.NOMBRE AS PROCESO, TRUNC(E.FECHA_CREACION) AS FECHA, E.TITULO, E.OBSERVACIONMP, CASE WHEN E.ESTADO = 'R' THEN NVL(A.NOMBRE, 'Ingresado') WHEN E.ESTADO = 'X' THEN 'Archivado' ELSE ' ' END AS ESTADO\n"
                + "FROM\n"
                + "    TRAMDOC.EXPEDIENTE E INNER JOIN TRAMDOC.CLIENTE C ON E.ID_CLIENTE = C.ID_CLIENTE \n"
                + "    INNER JOIN TRAMDOC.PROCESO P ON E.ID_PROCESO = P.ID_PROCESO \n"
                + "    INNER JOIN TRAMDOC.TRAZA T ON E.ID_EXPEDIENTE = T.ID_EXPEDIENTE AND T.ACTUAL = 1\n"
                + "    LEFT JOIN TRAMDOC.ACCION_TRAZA AT ON T.ID_TRAZA = AT.ID_TRAZA\n"
                + "    LEFT JOIN TRAMDOC.ACCION A ON AT.ID_ACCION = A.ID_ACCION \n"
                + "WHERE\n"
                + "    C.NUMERO_IDENTIFICACION = ?1 AND \n"
                + "    E.NUMERO LIKE ?2 AND \n"
                + "    E.TITULO LIKE ?3 AND \n"
                + "    E.ESTADO LIKE ?4 AND \n"
                + "    TRUNC(E.FECHA_CREACION) BETWEEN ?5 AND ?6 \n"
                + "ORDER BY 1 DESC"
        );
        q.setParameter(1, numDoc);
        q.setParameter(2, nullATodo(nroExpediente, false));
        q.setParameter(3, nullATodo(titulo, true));
        q.setParameter(4, nullATodo(estado, false));
        q.setParameter(5, fechaInicio);
        q.setParameter(6, fechaFin);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<ArrayRecord> listarTraza(Long idExpediente) {
        Query q = em.createNativeQuery(
                "SELECT TRUNC(T.FECHA_CREACION) AS FECHA, AR.NOMBRE AS AREA, CASE WHEN T.ACCION = 'Registrar' THEN 'Ingresado' WHEN T.ACCION = 'Archivar' THEN 'Archivado' ELSE NVL(A.NOMBRE, 'Pendiente') END AS ESTADO, NVL(T.OBSERVACION_ADMINISTRADO, T.OBSERVACION) AS OBSERVACION\n"
                + "FROM\n"
                + "    TRAMDOC.EXPEDIENTE E INNER JOIN TRAMDOC.CLIENTE C ON E.ID_CLIENTE = C.ID_CLIENTE \n"
                + "    INNER JOIN TRAMDOC.PROCESO P ON E.ID_PROCESO = P.ID_PROCESO \n"
                + "    INNER JOIN TRAMDOC.TRAZA T ON E.ID_EXPEDIENTE = T.ID_EXPEDIENTE \n"
                + "    LEFT JOIN TRAMDOC.ACCION_TRAZA AT ON T.ID_TRAZA = AT.ID_TRAZA\n"
                + "    LEFT JOIN TRAMDOC.ACCION A ON AT.ID_ACCION = A.ID_ACCION \n"
                + "    LEFT JOIN TRAMDOC.USUARIO_POR_TRAZA UPT ON T.ID_TRAZA = UPT.TRAZA AND UPT.RESPONSABLE = 1\n"
                + "    LEFT JOIN TRAMDOC.USUARIO U ON UPT.USUARIO = U.ID_USUARIO \n"
                + "    LEFT JOIN TRAMDOC.AREA AR ON U.AREA = AR.ID_AREA\n"
                + "WHERE\n"
                + "    E.ID_EXPEDIENTE = ?1\n"
                + "ORDER BY T.FECHA_CREACION DESC, T.ID_TRAZA, A.ID_ACCION"
        );
        q.setParameter(1, idExpediente);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    public List<ArrayRecord> buscaProcesoTramiteRevisa(int procesoId) {
        String sql = "SELECT ID_PROCESO FROM BDINTEGRADO.SB_AREA_PROCESO_TRAMITE\n" +
                     "WHERE ID_PROCESO = " + procesoId;
        javax.persistence.Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
}
