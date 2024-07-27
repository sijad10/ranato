/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.bdintegrado.data.SbCsEstablecimiento;
import pe.gob.sucamec.sel.epp.data.Registro;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

/**
 *
 * @author ddiaz
 */
@Path("/rs/gamac/csalud")
@Singleton
public class GamacRSCSaludFacade {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    
    @GET
    //@Produces({MediaType.APPLICATION_JSON})
    @Produces("application/json;charset=utf-8")
    @Path("list")
    public List<ArrayRecord> getCSaludByGPS(@QueryParam("q")String q) {
        try {
            return selectCSalud(q);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ArrayRecord> selectCSalud(String q) {
        String sql = "SELECT DISTINCT DIR.ID, TRIM(t2.NOMBRE) AS NOMBRE, t1.ruc, TO_CHAR(t2.FECHA_FIN, 'DD/MM/YYYY') AS FECHA_FIN, TO_CHAR(t2.FECHA_INI, 'DD/MM/YYYY') AS FECHA_INI, dir.geo_lat, dir.geo_long, dir.DIRECCION, DIS.NOMBRE AS DISTITO, PRO.NOMBRE AS PROVINCIA, DEP.NOMBRE AS DEPARTAMENTO --, t2.NRO_RES_FUNCIONAMIENTO, t2.DIRECCION_ID, t2.PROPIETARIO_ID, t2.TIPO_ID, t2.USUARIO_ID \n" +
                    "FROM \n" +
                    "BDINTEGRADO.SB_PERSONA t1 \n" +
                    "LEFT OUTER JOIN BDINTEGRADO.SB_USUARIO t0 ON (t0.PERSONA_ID = t1.ID) \n" +
                    "LEFT OUTER JOIN (BDINTEGRADO.SB_PERFIL_USUARIO t4 \n" +
                    "JOIN BDINTEGRADO.SB_PERFIL t3 ON (t3.ID = t4.PERFIL_ID)) ON (t4.USUARIO_ID = t0.ID), BDINTEGRADO.SB_CS_ESTABLECIMIENTO t2\n" +
                    "INNER JOIN BDINTEGRADO.SB_DIRECCION DIR ON DIR.ID = T2.DIRECCION_ID  \n" +
                    "INNER JOIN BDINTEGRADO.SB_DISTRITO DIS ON DIR.DISTRITO_ID = DIS.ID\n" +
                    "INNER JOIN BDINTEGRADO.SB_PROVINCIA PRO ON DIS.PROVINCIA_ID = PRO.ID\n" +
                    "INNER JOIN BDINTEGRADO.SB_DEPARTAMENTO DEP ON PRO.DEPARTAMENTO_ID = DEP.ID\n" +
                    "WHERE \n" +
                    "((((((((t2.ACTIVO = 1) AND (t2.HABILITADO = 1)) AND (t0.ACTIVO = 1)) AND (t1.ACTIVO = 1)) AND (t0.ID IS NOT NULL)) AND (t3.COD_PROG = 'AMA_CSALUD')) AND ( TRUNC(T2.FECHA_FIN) > TRUNC(SYSDATE))) AND (t1.ID = t2.PROPIETARIO_ID)) \n" +
                    "AND (dir.geo_lat IS NOT NULL AND dir.geo_long IS NOT NULL)";
        if (q != null && !"".equals(q)) {
            sql = sql + " AND TRIM(t2.NOMBRE) LIKE '%" + q.trim() + "%'";
        }
        Query q1 = em.createNativeQuery( sql );
        q1.setHint("eclipselink.result-type", "Map");
        return q1.getResultList();
    }

}
