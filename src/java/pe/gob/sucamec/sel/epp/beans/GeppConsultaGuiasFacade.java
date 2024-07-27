/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.beans;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.epp.data.Registro;

/**
 *
 * @author rmoscoso
 */
@Stateless
public class GeppConsultaGuiasFacade extends AbstractFacade<Registro> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GeppConsultaGuiasFacade() {
        super(Registro.class);
    }

    public String nullATodo(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return s.replace("%", "");
    }

    public String nullATodoParcial(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s.replace("%", "") + "%";
    }

    public List<HashMap> listarGuias(String guia, String exp, String rg, String ruc) {
        //TODO:MEJORAR QUERIES QUITAR TRAMDOC
        Query q = em.createNativeQuery(
                "SELECT REG.ID \"PDF_ID\", PER1.RZN_SOCIAL||PER1.APE_PAT|| ' ' ||PER1.APE_MAT ||' ' ||PER1.NOMBRES AS RAZON_SOCIAL, PER1.RUC, TB.NOMBRE,\n"
                + "RES.NUMERO \"NRO_RG\", REG.NRO_EXPEDIENTE \"NRO_EXPEDIENTE\", RES2.NUMERO \"NRO_GT\", RES2.FECHA_INI, RES2.FECHA_FIN,\n"
                + "TE.NOMBRE \"ESTADO_GT\", REG.AUD_LOGIN \"USUARIO\"\n"
                + "FROM BDINTEGRADO.EPP_REGISTRO_GUIA_TRANSITO RGT\n"
                + "INNER JOIN BDINTEGRADO.EPP_REGISTRO REG ON RGT.REGISTRO_ID = REG.ID\n"
                + "INNER JOIN BDINTEGRADO.SB_PERSONA PER1 ON RGT.EMPRESA_ID = PER1.ID\n"
                + "INNER JOIN BDINTEGRADO.TIPO_EXPLOSIVO TE ON REG.ESTADO = TE.ID\n"
                + "LEFT JOIN BDINTEGRADO.EPP_RESOLUCION RES ON RGT.RESOLUCION_ID = RES.ID\n"
                + "LEFT JOIN BDINTEGRADO.EPP_REGISTRO REG2 ON RES.REGISTRO_ID = REG2.ID\n"
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TB ON REG2.TIPO_PRO_ID = TB.ID\n"
                + "INNER JOIN BDINTEGRADO.EPP_RESOLUCION RES2 ON RGT.REGISTRO_ID = RES2.REGISTRO_ID\n"
                + "WHERE REG.ACTIVO=1 AND REG.ESTADO=563 AND RES2.NUMERO IS NOT NULL\n"
                + "AND PER1.RUC = ?1 AND RES2.NUMERO LIKE ?2 AND REG.NRO_EXPEDIENTE LIKE ?3 AND RES.NUMERO LIKE ?4\n"
                + "ORDER BY FECHA_INI DESC"
        );
        q.setParameter(1, nullATodo(ruc));
        q.setParameter(2, nullATodoParcial(guia));
        q.setParameter(3, nullATodoParcial(exp));
        q.setParameter(4, nullATodoParcial(rg));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
}
