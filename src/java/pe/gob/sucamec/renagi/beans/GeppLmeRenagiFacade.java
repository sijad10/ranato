/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

/**
 *
 * @author ddiaz
 */
@Stateless
public class GeppLmeRenagiFacade {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    private static final int MAX_RES = 500;

    public String nullATodo(String s, boolean like) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        if ( like ) {
            return "%" + s + "%";
        } else {
            return s.replace("%", "");
        }
    }

    public List<HashMap> selectLike(LoginController usuario, String tipo, String filtro) {
        String sql = "SELECT L.ID, L.NRO_LICENCIA, E.RUC, E.RZN_SOCIAL, TB.NOMBRE AS TIPO_DOC, P.NUM_DOC, P.APE_PAT, P.APE_MAT, P.NOMBRES, R.NRO_EXPEDIENTE, TC.NOMBRE AS TIPO_CARGO, FEC_EMI, FEC_VENC, TE.NOMBRE AS TIPO_EMISION, TL.NOMBRE AS TIPO_LICENCIA\n" +
                    "FROM\n" +
                    "BDINTEGRADO.EPP_LICENCIA L \n" +
                    "INNER JOIN BDINTEGRADO.SB_PERSONA P ON L.PERSONA_ID = P.ID\n" +
                    "INNER JOIN BDINTEGRADO.EPP_REGISTRO R ON L.REGISTRO_ID = R.ID\n" +
                    "INNER JOIN BDINTEGRADO.SB_PERSONA E ON R.EMPRESA_ID = E.ID\n" +
                    "INNER JOIN BDINTEGRADO.TIPO_BASE TB ON P.TIPO_DOC = TB.ID\n" +
                    "INNER JOIN BDINTEGRADO.TIPO_EXPLOSIVO TC ON L.TIPO_CARGO_ID = TC.ID  \n" +
                    "INNER JOIN BDINTEGRADO.TIPO_EXPLOSIVO TE ON L.TIPO_EMISION_ID = TE.ID\n" +
                    "INNER JOIN BDINTEGRADO.TIPO_EXPLOSIVO TL ON L.TIPO_LICENCIA_ID = TL.ID\n" +
                    "WHERE L.ACTIVO = 1 AND L.EMITIDA = 1 AND FEC_VENC >= SYSDATE  ";
        if( tipo.equals("nroexp") ) {
            sql += " AND R.NRO_EXPEDIENTE = ?1 ";
        } else if( tipo.equals("nrolic") ) {
            sql += " AND L.NRO_LICENCIA = ?1 ";
        } else if( tipo.equals("numdoc") ) {
            sql += " AND P.NUM_DOC = ?1 ";
        }
        sql += " ORDER BY L.FEC_EMI DESC";
        Query q = em.createNativeQuery( sql );
        //q.setParameter(1, usuario.getUsuario().getNumDoc());
        q.setParameter(1, nullATodo(filtro.toUpperCase(), false));
        //q.setParameter(2, nullATodo(nroExpediente, false));
        //q.setParameter(3, nullATodo(nroLic, false));
        //q.setParameter(4, nullATodo(documento, false));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }    
}
