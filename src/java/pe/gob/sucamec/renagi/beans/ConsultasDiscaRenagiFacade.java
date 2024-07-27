/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.bdintegrado.bean.AmaMaestroArmasFacade;

/**
 *
 * @author Renato
 */
@Stateless
public class ConsultasDiscaRenagiFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    private static final int MAX_RES = 10000;
    
    @EJB
    private AmaMaestroArmasFacade ejbAmaMaestroArmasFacade;    

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

    public String nullATodoComodin(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s + "%";
    }

    public List<HashMap> listarArmasDeFuego(String licencia, String serie,
            String doc_prop, String nom_prop, String doc_por, String nom_portador) {
        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                + "WHERE NRO_LIC LIKE ?1 AND NRO_SERIE LIKE ?2 AND DOC_PROPIETARIO LIKE ?3 AND PROPIETARIO LIKE ?4 "
                + "AND DOC_PORTADOR LIKE ?5 AND PORTADOR LIKE ?6 "
                + "ORDER BY NRO_LIC"
        );
        q.setParameter(1, nullATodo(licencia));
        q.setParameter(2, nullATodo(serie));
        q.setParameter(3, nullATodo(doc_prop));
        q.setParameter(4, nullATodoComodin(nom_prop));
        q.setParameter(5, nullATodo(doc_por));
        q.setParameter(6, nullATodoComodin(nom_portador));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    /**
     * ONSULTA MIGRACIÓN ARMA
     * 
     * @param licencia
     * @param serie
     * @param doc_prop
     * @param nom_prop
     * @param doc_por
     * @param nom_portador
     * @return 
     */
    public List<HashMap> listarArmasDeFuegoMigra(String licencia, String serie,
            String doc_prop, String nom_prop, String doc_por, String nom_portador) {
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
                + " L.SISTEMA"
                + " FROM " + sqlMigra
                + " WHERE L.LICENCIA_DISCA LIKE ?1"
                + " AND L.SERIE LIKE ?2"
                + " AND L.DOC_PROPIETARIO LIKE ?3"
                + " AND L.PROPIETARIO LIKE ?4 "
                //+ "AND DOC_PORTADOR LIKE ?5 AND PORTADOR LIKE ?6 "
                + "ORDER BY L.LICENCIA_DISCA"                
                + "";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, nullATodo(licencia));
        q.setParameter(2, nullATodo(serie));
        q.setParameter(3, nullATodo(doc_prop));
        q.setParameter(4, nullATodoComodin(nom_prop));
        q.setParameter(5, nullATodo(doc_por));
        q.setParameter(6, nullATodoComodin(nom_portador));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    public List<HashMap> listarArmasxFiltro(String tipo, String filtro) {
        String campo = "";
        if (tipo.equals("licencia")) {
            campo = "L.NRO_LIC = ?1 ";
            //campo = "REPLACE(REPLACE(NRO_LIC, ' ', ''),'-','') = REPLACE(REPLACE(?1, ' ', ''),'-','') ";
            //campo = "REGEXP_REPLACE(NRO_LIC, ' |-', '') = REGEXP_REPLACE(?1, ' |-', '')";
        }
        if (tipo.equals("serie")) {
            campo = "REPLACE(REPLACE(L.NRO_SERIE, ' ', ''),'-','') = REPLACE(REPLACE(?1, ' ', ''),'-','') ";
            //campo = "REGEXP_REPLACE(NRO_SERIE, ' |-', '') = REGEXP_REPLACE(?1, ' |-', '')";
        }
        if (tipo.equals("doc_prop")) {
            campo = "REPLACE(L.DOC_PROPIETARIO, ' ', '') = ?1 ";
        }
        if (tipo.equals("nom_prop")) {
            campo = "REPLACE(L.PROPIETARIO, ' ', '') LIKE ?1 ";
        }
        if (tipo.equals("doc_portador")) {
            campo = "REPLACE(L.DOC_PORTADOR, ' ', '') = ?1 ";
        }
        if (tipo.equals("nom_portador")) {
            campo = "REPLACE(L.PORTADOR, ' ', '') LIKE ?1 ";
        }
        
        Query q = em.createNativeQuery(
                "SELECT "
                    + " CASE"
                    + " WHEN L.TIPO_PROPIETARIO IN('PERS. NATURAL') THEN 'DNI'"
                    + " WHEN L.TIPO_PROPIETARIO IN('EXTRANJERO') THEN 'CE'"
                    + " WHEN L.TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                    + " WHEN L.TIPO_PROPIETARIO IN('PERS. JURIDICA', 'DELEGACION') THEN 'RUC'"
                    + " END"
                    + " AS TIPO_DOC, L.* "
                    + " FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC L"
                + " WHERE "+ campo 
                + " ORDER BY L.NRO_LIC"
        );
        switch (tipo){
            case "licencia":
                try{
                    q.setParameter(1, Integer.valueOf(filtro.trim()));
                    break;                    
                }catch (NumberFormatException e) {
                    return null;
                }
            case "serie":
            case "doc_prop":
            case "doc_portador":
                q.setParameter(1, filtro.trim().toUpperCase().replace(" ", ""));
                break;
            case "nom_prop":
            case "nom_portador":
                q.setParameter(1, nullATodoComodin(filtro.toUpperCase().replace(" ", "")));
                break;
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    /**
     * CONSULTA MIGRACIÓN ARMA
     * 
     * @param tipo
     * @param filtro
     * @param personaId
     * @return 
     */
    public List<HashMap> listarArmasxFiltroMigra(String tipo, String filtro, Long personaId) {

        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_GENERAL() + ") L ";
        String campo = "";
        if (tipo.equals("licencia")) {
            campo = "L.LICENCIA_DISCA = ?1 ";
            //campo = "REPLACE(REPLACE(NRO_LIC, ' ', ''),'-','') = REPLACE(REPLACE(?1, ' ', ''),'-','') ";
            //campo = "REGEXP_REPLACE(NRO_LIC, ' |-', '') = REGEXP_REPLACE(?1, ' |-', '')";
        }
        if (tipo.equals("serie")) {
            campo = "REPLACE(REPLACE(L.SERIE, ' ', ''),'-','') = REPLACE(REPLACE(?1, ' ', ''),'-','') ";
            //campo = "REGEXP_REPLACE(NRO_SERIE, ' |-', '') = REGEXP_REPLACE(?1, ' |-', '')";
        }
        if (tipo.equals("doc_prop")) {
            campo = "L.DOC_PROPIETARIO = ?1 ";
            if (personaId!=null) {
                campo = "(" + campo + " OR L.PERSONA_ID=" + personaId + ")";
            }
            //campo = "REPLACE(L.DOC_PROPIETARIO, ' ', '') = ?1 ";
        }
        if (tipo.equals("nom_prop")) {
            campo = "REPLACE(L.PROPIETARIO, ' ', '') LIKE ?1 ";
        }
        if (tipo.equals("doc_portador")) {
            campo = "REPLACE(L.DOC_PROPIETARIO, ' ', '') = ?1 ";
        }
        if (tipo.equals("nom_portador")) {
            campo = "REPLACE(L.PROPIETARIO, ' ', '') LIKE ?1 ";
        }
        
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
                + " L.MODALIDAD_ARMA TIPO_LICENCIA,"
                + " L.FECHA_EMISION FEC_EMISION,"
                + " L.FECHA_VEN_DISCA FEC_VENCIMIENTO,"
                + " L.ESTADO_ARMA ESTADO,"
                + " L.SITUACION_ARMA SITUACION,"
                + " L.SISTEMA"
                + " FROM " + sqlMigra
                + " WHERE "+ campo 
                + " ORDER BY L.LICENCIA_DISCA"
                + "";

        //Syso(sql);
        Query q = em.createNativeQuery(sql);
        switch (tipo){
            case "licencia":
                try{
                    q.setParameter(1, Integer.valueOf(filtro.trim()));
                    break;                    
                }catch (NumberFormatException e) {
                    return null;
                }
            case "serie":
            case "doc_prop":
            case "doc_portador":
                q.setParameter(1, filtro.trim().toUpperCase().replace(" ", ""));
                break;
            case "nom_prop":
            case "nom_portador":
                q.setParameter(1, nullATodoComodin(filtro.toUpperCase().replace(" ", "")));
                break;
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    public List<HashMap> listarPersonaNatural( String filtro) {
        String campo = "";
        campo = "p.COD_USR = ?1 ";
        
        String sql = "SELECT"
                + " p.COD_USR,"
                + " p.NOMBRE,"
                + " p.APE_PAT,"
                + " p.APE_MAT,"
                + " COALESCE(p.DOMICILIO, ' ') DOMICILIO,"
                + " COALESCE(p.SEXO, ' ') SEXO,"
                + " p.FEC_NAC,"
                + " COALESCE(d.NOM_DPTO, ' ') NOM_DPTO,"
                + " COALESCE(pr.NOM_PROV,' ') NOM_PROV,"
                + " COALESCE(di.NOM_DIST, ' ') NOM_DIST"
                + " FROM"
                + " RMA1369.PERSONA_NATURAL@SUCAMEC p"
                + " LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC d ON p.COD_DPTO=d.COD_DPTO"
                + " LEFT JOIN RMA1369.PROVINCIA@SUCAMEC pr ON pr.COD_DPTO=d.COD_DPTO AND pr.COD_PROV=p.COD_PROV"
                + " LEFT JOIN RMA1369.DISTRITO@SUCAMEC di ON pr.COD_PROV=di.COD_PROV AND di.COD_DPTO=d.COD_DPTO AND di.COD_DIST=p.COD_DIST"
                + " WHERE "+ campo 
                + "";
        
        Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, filtro.trim());
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    /**
     * Selecciona una foto del DISCA
     *
     * @param codUsr Número de documento de la persona
     * @param vigilante Si solo se busca en la tabla de vigilantes
     * @return Arreglo de bytes con la información de la imagen
     */
    public byte[] selectFoto(String codUsr, boolean vigilante) {
        Query q;
        List<byte[]> r;
        String sql = "";
        if (vigilante) {
            sql = "SELECT FOTO FROM RMA1369.SS_VIG_FOTO@SUCAMEC WHERE COD_USR=?1";            
        } else {
            sql = "SELECT FOTO FROM RMA1369.SG_FOTO@SUCAMEC WHERE COD_USR=?1";
        }
        
        q = em.createNativeQuery(sql);
        q.setParameter(1, codUsr.trim());
        r = q.getResultList();
        if (r.size() == 1) {
            byte[] foto = r.get(0);
            return foto;
        }else{
            return null;
        }
    }

    public List<HashMap> listarVigilantes(String numdoc, String carne, String ruc, String nombre) {
        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.WS_VIGILANTES@SUCAMEC "
                + "WHERE COD_USR LIKE ?1 AND NRO_CRN_VIG LIKE ?2 AND RUC LIKE ?3 AND NOMBRE LIKE ?4"
        );
        q.setParameter(1, nullATodo(numdoc));
        q.setParameter(2, nullATodo(carne));
        q.setParameter(3, nullATodo(ruc));
        q.setParameter(4, nullATodoComodin(nombre));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarVigilantesxFiltro(String tipo, String filtro) {
        String campo = "";        
        switch (tipo){
            case "numdoc":
                campo = "COD_USR = ?1 ";
                break;
            case "carne":
                campo = "NRO_CRN_VIG = ?1 ";
                break;
            case "nombre":
                campo = "NOMBRE LIKE ?1 ";
                break;
            case "ruc":
                campo = "RUC = ?1 ";
                break;
        }        

        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.WS_VIGILANTES@SUCAMEC "
                + "WHERE " + campo
        );
        switch (tipo){
            case "carne":                
            case "ruc":
            case "numdoc":
                q.setParameter(1, filtro.trim().toUpperCase());
                break;
            case "nombre":
                q.setParameter(1, nullATodoComodin(filtro).toUpperCase());
                break;
        }        
        
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarHistVigilantes(String numdoc) {
        Query q = em.createNativeQuery(
                "SELECT E.RUC, E.RZN_SOC, ME.DES_MOD, FEC_EMI, FEC_VENC, FEC_BAJA FROM RMA1369.SS_EMP_VIG_HIS@SUCAMEC VH "
                + "INNER JOIN RMA1369.MOD_EMPR@SUCAMEC ME ON VH.TIP_MOD=ME.TIP_MOD AND ME.AREA_MOD = 2 "
                + "INNER JOIN RMA1369.EMPRESA@SUCAMEC E ON E.RUC = VH.RUC "
                + "WHERE COD_USR = ?1 ORDER BY FEC_EMI DESC"
        );
        q.setParameter(1, nullATodo(numdoc));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarCursosVigilantes(String numdoc) {
        Query q = em.createNativeQuery(
                "SELECT  E.RUC, E.RZN_SOC, CASE WHEN EVALUACION = 1 THEN 'APROBADO' ELSE 'DESAPROBADO' END AS EVALUACION, "
                + "CASE WHEN TIP_INST = 1 THEN 'ENTRENAMIENTO' WHEN TIP_INST=2 THEN 'REENTRENAMIENTO' WHEN TIP_INST=3 THEN 'INSTRUCCION' WHEN TIP_INST=4 "
                + "THEN 'CAPACITACION' ELSE 'PRACTICA DE TIRO' END AS TIPO, "
                + "FEC_FINAL, ADD_MONTHS(A1.FEC_FINAL, CASE WHEN A1.FEC_INICIO >= TO_DATE('03/08/2015', 'DD/MM/YYYY') THEN 24 ELSE 12 END) AS FEC_VENC, "
                + "CASE WHEN ADD_MONTHS(A1.FEC_FINAL, CASE WHEN A1.FEC_INICIO >= TO_DATE('03/08/2015', 'DD/MM/YYYY') THEN 24 ELSE 12 END) >= TRUNC(SYSDATE) "
                + "THEN 'VIGENTE' ELSE 'VENCIDA' END AS VIGENTE "
                + "FROM RMA1369.SS_CAPAC@SUCAMEC A1, RMA1369.SS_CAPAC_VIG@SUCAMEC B1, RMA1369.EMPRESA@SUCAMEC E "
                + "WHERE "
                + "E.RUC = A1.RUC AND A1.NRO_EXP = B1.NRO_EXP AND A1.ANO_EXP = B1.ANO_EXP AND B1.TIP_INST IN(2, 3, 4, 5) "
                + "AND B1.COD_USR = ?1 AND B1.TIP_USR = 2 ORDER BY FEC_VENC DESC"
        );
        q.setParameter(1, nullATodo(numdoc));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<ArrayRecord> listarDenegadosxFiltro(String filtro, String tipoDoc) {
        String campo = "";
        String inner = "";
        String where = "";
        boolean buscar = false;
        if (tipoDoc.equals("DNI")) {
            //campo = " TO_CHAR(p.COD_USR, '00000000') NRO_DOC,";
            campo = " TO_CHAR(d.COD_USR, '00000000') NRO_DOC,";            
            inner = " LEFT JOIN RMA1369.PERSONA_NATURAL@SUCAMEC p ON p.COD_USR=d.COD_USR";
            where = " WHERE d.TIP_USR=2 AND TRIM(TO_CHAR(d.COD_USR, '00000000')) = ?1";
            buscar = true;
        }
        if (tipoDoc.equals("CIP")) {
            //campo = " p.CIP NRO_DOC,";
            campo = " d.COD_USR NRO_DOC,";            
            inner = " LEFT JOIN RMA1369.EFECTIVO@SUCAMEC p ON p.COD_USR=d.COD_USR";
            where = " WHERE d.TIP_USR=4 AND d.COD_USR=?1"; //AND p.CIP=?1";
            buscar = true;
        }
        if (tipoDoc.equals("RUC")) {
            //campo = " p.RUC_OK NRO_DOC,";            
            campo = " d.COD_USR NRO_DOC,";            
            inner = " LEFT JOIN RMA1369.EMPRESA@SUCAMEC p ON p.RUC=d.COD_USR";
            where = " WHERE d.TIP_USR=1 AND d.COD_USR=?1"; //AND RUC_OK=?1";
        }
        if (tipoDoc.equals("CE")) {
            //campo = " p.CRNT_EXT NRO_DOC,";
            campo = " d.COD_USR NRO_DOC,";            
            inner = " LEFT JOIN RMA1369.EXTRANJERO@SUCAMEC p ON p.COD_USR=d.COD_USR";
            where = " WHERE d.TIP_USR=5 AND d.COD_USR=?1"; //AND (p.CRNT_EXT=?1)";
            buscar = true;
        }
        
        String sql = "SELECT"
                + " d.COD_USR,"
                + campo
                + " d.ANO_EXP, d.NRO_EXP,"
                + " TO_CHAR(d.FEC_INI, 'DD/MM/YYYY') AS FEC_INI,"
                + " TO_CHAR(d.FEC_FIN, 'DD/MM/YYYY') AS FEC_FIN,"
                + " d.JUZGADO, d.TIPO_SUSPENCION"
                + " FROM RMA1369.AM_DENEGADO@SUCAMEC d"
                + inner
                + where
                + " AND d.FLAG_ALTA IS NULL"
                + " ORDER BY d.COD_USR, d.ANO_EXP DESC, d.NRO_EXP DESC"
                + " ";
        if(buscar){
            try {
                Query q = em.createNativeQuery(sql);
                q.setParameter(1, filtro.trim().toUpperCase());
                q.setHint("eclipselink.result-type", "Map");
                List<ArrayRecord> list = q.setMaxResults(MAX_RES).getResultList(); 
                if(list.size()>0){
                    return list;                    
                }else{
                    return null;                    
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }            
        }else{
            return null;
        }
    }

}
