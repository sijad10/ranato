/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.epp.data.Registro;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

/**
 *
 * @author ddiaz
 */
@Path("/rs/gepp/ferias")
@Singleton
public class GeppRSFeriasFacade {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    
    @GET
    //@Produces({MediaType.APPLICATION_JSON})
    @Produces("application/json;charset=utf-8")
    @Path("list")
    public List<ArrayRecord> getFeriasByGPS() {
        try {
            return selectFerias();
        } catch(Exception e) {
            return null;
        }
    }

    public List<ArrayRecord> selectFerias() {
        String sql = "SELECT * FROM BDINTEGRADO.TMP_FERIAS WHERE ESTADO = 1";
        Query q = em.createNativeQuery( sql );
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }

    /*
    public List<ArrayRecord> selectFerias() {
        String sql = "SELECT LOC.DIRECCION, PE.RZN_SOCIAL || PE.NOMBRES || PE.APE_PAT || PE.APE_MAT AS PROPIETARIO, LOC.LATITUD, LOC.LONGITUD, RES.FECHA_INI, RES.FECHA_FIN, RES.NUMERO,\n" +
"DEP.NOMBRE DEPARTAMENTO, PR.NOMBRE PROVINCIA, DIS.NOMBRE DISTRITO\n" +
"FROM BDINTEGRADO.EPP_REGISTRO REG\n" +
"INNER JOIN BDINTEGRADO.SB_PERSONA PE ON REG.EMPRESA_ID = PE.ID\n" +
"INNER JOIN BDINTEGRADO.EPP_RESOLUCION RES ON REG.ID = RES.REGISTRO_ID\n" +
"INNER JOIN BDINTEGRADO.EPP_REGISTRO_AUTORIZACION RA ON REG.ID = RA.REGISTRO_ID\n" +
"INNER JOIN BDINTEGRADO.EPP_LOCAL LOC ON RA.LOCAL_ID = LOC.ID\n" +
"INNER JOIN BDINTEGRADO.SB_DISTRITO DIS ON LOC.UBIGEO_ID = DIS.ID\n" +
"INNER JOIN BDINTEGRADO.SB_PROVINCIA PR ON DIS.PROVINCIA_ID = PR.ID\n" +
"INNER JOIN BDINTEGRADO.SB_DEPARTAMENTO DEP ON PR.DEPARTAMENTO_ID = DEP.ID\n" +
"WHERE REG.TIPO_PRO_ID = 362 AND REG.ESTADO = 563 AND REG.ACTIVO = 1";
        Query q = em.createNativeQuery( sql );
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
*/

}
