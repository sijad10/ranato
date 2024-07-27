/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.disca.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.bean.AmaMaestroArmasFacade;
import org.eclipse.persistence.internal.sessions.ArrayRecord;

/**
 *
 * @author Renato
 */
@Stateless
public class MvDcGamacLicenciaFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @EJB
    private AmaMaestroArmasFacade ejbAmaMaestroArmasFacade;  

    private static final int MAX_RES = 20000;

    public String nullATodo(String s) {
        return nullATodo(s, false);
    }

    public String nullATodo(String s, boolean parcial) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        if (parcial) {
            return "%" + s.replace("%", "") + "%";
        }
        return s.replace("%", "");
    }
    
    public String nullATodoComodin(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s + "%";
    }

    public List<HashMap> listarLicenciasDisca(String filtro) {
        Query q = em.createNativeQuery(
                "SELECT A.* FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC A "+
                " WHERE NRO_LIC = ?1 " +
                " ORDER BY A.FEC_VENCIMIENTO DESC "
        );
        q.setParameter(1, nullATodo(filtro));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    /**
     * CONSULTA MIGRA ARMA
     * 
     * @param filtro
     * @return 
     */
    public List<HashMap> listarLicenciasDiscaMigra(String filtro) {
        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_GENERAL() + ") L ";        
        String sql = "SELECT "
                + " CASE"
                + " WHEN L.TIPO_DOCUMENTO IN('DNI') THEN 'DNI'"
                + " WHEN L.TIPO_DOCUMENTO IN('CE') THEN 'CE'"
                //+ " WHEN L.TIPO_PROPIETARIO IN('MIEMBRO FFA O PNP') THEN 'CIP'"
                + " WHEN L.TIPO_DOCUMENTO IN('RUC') THEN 'RUC'"
                + " END"
                + " AS TIPO_DOC,"
                + " L.LICENCIA_DISCA NRO_LIC,"
                + " L.NRO_RUA,"
                + " L.TIPO_PROPIETARIO,"
                + " L.DOC_PROPIETARIO,"
                + " L.PROPIETARIO,"
                + " L.SERIE NRO_SERIE,"
                + " L.TIPO_ARMA,"
                + " L.MARCA,"
                + " L.MODELO,"
                + " L.CALIBRE,"
                + " L.FECHA_EMISION FEC_EMISION,"
                + " L.FECHA_VEN_DISCA FEC_VENCIMIENTO,"
                + " L.ESTADO_ARMA ESTADO,"
                + " L.SITUACION_ARMA SITUACION,"
                + " L.SISTEMA,"
                + " L.ID ID_MIGRA_DISCA_FOX"
                + " FROM " + sqlMigra
                + " WHERE L.LICENCIA_DISCA LIKE ?1"
                + " ORDER BY L.FECHA_VEN_DISCA DESC"                
                + "";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, nullATodo(filtro));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public String obtenerEstadoLicDisca(Long id){
        return ejbAmaMaestroArmasFacade.estadoLicenciaDisca(id);
    }    
    
    public Map listarLicenciasMovil(String filtro) {
        Query q = em.createNativeQuery(
                        "SELECT LU.NRO_LICENCIA, SB.NUM_DOC,SB.NOMBRES, SB.APE_PAT,SB.APE_MAT,TG.NOMBRE MODALIDAD,TL.NRO_EXPEDIENTE, LU.FECHA_EMISION, LU.FECHA_VENCIMIETNTO AS FECHA_VENCIMIENTO,TG1.NOMBRE RESTRICCION, \n" +
                        "DIR.DIRECCION,DIR.DISTRITO,DIR.PROVINCIA,DIR.DEPARTAMENTO FROM BDINTEGRADO.AMA_LICENCIA_DE_USO LU \n" +
                        "INNER JOIN BDINTEGRADO.AMA_TIPO_LICENCIA TL ON LU.ID = TL.LICENCIA_ID \n" +
                        "INNER JOIN BDINTEGRADO.SB_PERSONA SB ON LU.PERSONA_LICENCIA_ID=SB.ID \n" +
                        "INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID=TL.MODALIDAD_ID \n" +
                        "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG1 ON TG1.ID=LU.RESTRICCION_ID \n" +
                        "INNER JOIN (select DD.ID, DD.DIRECCION,DS.NOMBRE DISTRITO,PP.NOMBRE PROVINCIA,DP.NOMBRE DEPARTAMENTO from BDINTEGRADO.SB_DIRECCION DD \n" +
                        "INNER JOIN BDINTEGRADO.SB_DISTRITO DS ON DD.DISTRITO_ID=DS.ID \n" +
                        "INNER JOIN BDINTEGRADO.SB_PROVINCIA PP ON DS.PROVINCIA_ID=PP.ID \n" +
                        "INNER JOIN BDINTEGRADO.SB_DEPARTAMENTO DP ON PP.DEPARTAMENTO_ID=DP.ID) DIR ON DIR.ID=LU.DIRECCION_ID \n" +
                        "WHERE LU.ACTIVO=1 AND TL.ACTIVO=1 \n" +
                        "AND SB.NUM_DOC = ?1 \n" +
                        "order by 1 ASC "
        );
        q.setParameter(1, filtro.trim());
        q.setHint("eclipselink.result-type", "Map");
        if(!q.getResultList().isEmpty()){
            return (Map) q.getResultList().get(0);
        }
        return null;
    }
}
