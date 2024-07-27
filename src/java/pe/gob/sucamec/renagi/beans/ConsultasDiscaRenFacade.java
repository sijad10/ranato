/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.renagi.jsf.util.JsfUtil;

/**
 *
 * @author Renato
 */
@Stateless
public class ConsultasDiscaRenFacade {

    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager em;

    private static final int MAX_RES = 10000;

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
                "SELECT * FROM RMA1369.WS_LICENCIAS@SUCAMEC "
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

    public List<HashMap> listarArmasxFiltro(String tipo, String filtro) {
        String campo = "";
        if (tipo.equals("licencia")) {
            campo = "NRO_LIC = ?1 ";
        }
        if (tipo.equals("serie")) {
            campo = "NRO_SERIE = ?1 ";
        }
        if (tipo.equals("doc_prop")) {
            campo = "DOC_PROPIETARIO = ?1 ";
        }
        if (tipo.equals("nom_prop")) {
            campo = "PROPIETARIO LIKE ?1 ";
        }
        if (tipo.equals("doc_portador")) {
            campo = "DOC_PORTADOR = ?1 ";
        }
        if (tipo.equals("nom_portador")) {
            campo = "PORTADOR LIKE ?1 ";
        }
        
        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                + "WHERE "+ campo 
                + " AND ID IN(SELECT MIN(ID) FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC) "
                + "ORDER BY NRO_LIC"
        );
        switch (tipo){
            case "licencia":
            case "serie":
            case "doc_prop":
            case "doc_portador":
                q.setParameter(1, filtro.trim().toUpperCase());
                break;
            case "nom_prop":
            case "nom_portador":                
                q.setParameter(1, nullATodoComodin(filtro.toUpperCase()));
                break;
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }

    public List<HashMap> listarArmasxNroReferencia(String tipo, String filtro, String tipoDoc) {
        String campo = "";
        if (tipo.equals("licencia")) {
            campo = "NRO_LIC = ?1 ";
        }
        if (tipo.equals("serie")) {
            campo = "NRO_SERIE = ?1 ";  
        }
        if (tipo.equals("doc_prop")) {
            switch(tipoDoc){
                case "RUC":
                    //campo = "(TIPO_PROPIETARIO IN('PERS. JURIDICA') AND REGEXP_LIKE(DOC_PROPIETARIO, '[0-9]{2}" +filtro.trim()+"[0-9]{1}')) ";
                    campo = "(TIPO_PROPIETARIO IN('PERS. JURIDICA') AND DOC_PROPIETARIO LIKE '1_" +filtro.trim()+"_' )";
                    break;
                case "CIP":
                    campo = "(TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND DOC_PROPIETARIO = '" +filtro.trim()+"') ";
                    break;
                default:
                    campo = "DOC_PROPIETARIO = ?1 ";
            }
            /*if(tipoDoc.equals("DNI")){
                //campo = "(TIPO_PROPIETARIO IN('PERS. JURIDICA') AND REGEXP_LIKE(DOC_PROPIETARIO, '[0-9]{2}" +filtro.trim()+"[0-9]{1}')) ";
                campo = "(TIPO_PROPIETARIO IN('PERS. JURIDICA') AND DOC_PROPIETARIO LIKE '__" +filtro.trim()+"_') ";
            }else{
                campo = "DOC_PROPIETARIO = ?1 ";
            }*/
        }
        if (tipo.equals("nom_prop")) {
            campo = "PROPIETARIO LIKE ?1 ";
        }
        if (tipo.equals("doc_portador")) {
            campo = "DOC_PORTADOR = ?1 ";
        }
        if (tipo.equals("nom_portador")) {
            campo = "PORTADOR LIKE ?1 ";
        }
        
        Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                + "WHERE "+ campo 
                + " AND ID IN(SELECT MIN(ID) FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                + " AND SISTEMA='DISCA'"
                + "ORDER BY NRO_LIC"
        );
        switch (tipo){
            case "licencia":
            case "serie":
            case "doc_prop":
            case "doc_portador":
                q.setParameter(1, filtro.trim().toUpperCase());
                break;
            case "nom_prop":
            case "nom_portador":                
                q.setParameter(1, nullATodoComodin(filtro.toUpperCase()));
                break;
        }
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<ArrayRecord> listarxNumDoc(String tipo, String filtro) {
        String campo = "";
        //campo = "LTRIM(p.COD_USR, '0')=?1 OR LTRIM(p.CRNT_EXT,'0')=?2";        
        switch(tipo){
            case "DNI":
                campo = "TIPO_PROPIETARIO IN('PERS. NATURAL') AND DOC_PROPIETARIO = ?1 ";
                break;
            case "RUC":
                campo = "TIPO_PROPIETARIO IN('PERS. JURIDICA', 'DELEGACION') AND DOC_PROPIETARIO = ?1 ";
                break;
            case "CE":
                filtro = JsfUtil.leftpadString(filtro, 9, "0");
                campo = "TIPO_PROPIETARIO IN('EXTRANJERO') AND LPAD(DOC_PROPIETARIO, 9, '0') = ?1 ";
                break;
            case "CIP":
            case "FAP":
            case "POLICIA":
            case "MARINA":
            case "MILITAR":
            case "EJERCITO":
                campo = "TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND DOC_PROPIETARIO = ?1 ";
                break;
        }
        
        try {
            String sql = "SELECT * FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                    + "WHERE "+ campo 
                    + " AND ID IN(SELECT MIN(ID) FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                    + " AND SISTEMA='DISCA'"
                    + "ORDER BY NRO_LIC"
                    + "";

            Query q = em.createNativeQuery(sql);
            //Syso(sql);
            
            switch(tipo){
                case "DNI":
                case "RUC":
                case "CE":
                case "CIP":
                case "FAP":
                case "POLICIA":
                case "MARINA":
                case "MILITAR":
                case "EJERCITO":
                    q.setParameter(1, nullATodo(filtro.trim().toUpperCase()));
                    break;
                case "ape":
                    q.setParameter(1, "%" + filtro.trim().toUpperCase() + "%");
                    break;
            }
            q.setHint("eclipselink.result-type", "Map");
            List<ArrayRecord> list = q.getResultList(); 
            
            return list;
        } catch (Exception e) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorEnBusquedaBD"));
            e.printStackTrace();
            return null;
        }
    }
    
    public List<ArrayRecord> listarPropietariosxFiltro(String tipo, String filtro) {
        String campo = "";
        String tipo_doc = "";
        if (tipo.equals("dni")) {
            tipo_doc = " 'DNI' TIPO_DOC,";
            campo = "TIPO_PROPIETARIO IN('PERS. NATURAL') AND DOC_PROPIETARIO = ?1 ";
        }
        if (tipo.equals("cip")) {
            tipo_doc = " 'CIP' TIPO_DOC,";
            campo = "TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND DOC_PROPIETARIO LIKE ?1 ";
        }
        if (tipo.equals("ruc")) {
            tipo_doc = " 'RUC' TIPO_DOC,";            
            campo = "TIPO_PROPIETARIO IN('PERS. JURIDICA', 'DELEGACION') AND DOC_PROPIETARIO = ?1 ";
        }
        if (tipo.equals("ce")) {
            tipo_doc = " 'CE' TIPO_DOC,";
            campo = "TIPO_PROPIETARIO IN('EXTRANJERO') AND DOC_PROPIETARIO LIKE ?1 ";
        }
        if (tipo.equals("lic")) {
            tipo_doc = " CASE"
                    + " WHEN TIPO_PROPIETARIO IN('PERS. NATURAL') THEN 'DNI'"
                    + " WHEN TIPO_PROPIETARIO IN('EXTRANJERO') THEN 'CE'"
                    + " WHEN TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                    + " END"
                    + " AS TIPO_DOC, ";
            campo = "TIPO_PROPIETARIO IN('EXTRANJERO', 'PERS. NATURAL', 'FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND DOC_PROPIETARIO = ?1 ";
        }
        if (tipo.equals("ape")) {
            tipo_doc = " CASE"
                    + " WHEN TIPO_PROPIETARIO IN('PERS. NATURAL') THEN 'DNI'"
                    + " WHEN TIPO_PROPIETARIO IN('EXTRANJERO') THEN 'CE'"
                    + " WHEN TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                    + " END"
                    + " AS TIPO_DOC, ";
            campo = "TIPO_PROPIETARIO IN('EXTRANJERO', 'PERS. NATURAL', 'FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') AND PROPIETARIO LIKE ?1 ";
        }
        
        try {
            String sql = "SELECT"
                    + tipo_doc
                    + " TIPO_PROPIETARIO,"
                    + " DOC_PROPIETARIO,"
                    + " TRIM(PROPIETARIO) PROPIETARIO"
                    + "  "
                    + "FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC "
                    + "WHERE "+ campo 
                    + "GROUP BY TIPO_PROPIETARIO, DOC_PROPIETARIO, TRIM(PROPIETARIO) ";

            Query q = em.createNativeQuery(sql);
            //Syso(sql);
            
            switch(tipo){
                case "dni":
                case "ruc":
                case "lic":
                    q.setParameter(1, nullATodo(filtro.trim().toUpperCase()));
                    break;
                case "cip":
                case "ce":
                case "ape":
                    q.setParameter(1, "%" + filtro.trim().toUpperCase() + "%");
                    break;
            }
            q.setHint("eclipselink.result-type", "Map");
            List<ArrayRecord> list = q.setMaxResults(MAX_RES).getResultList(); 
            
            //* Remover duplicados, La vista devuelve nombre diferentes para el mismo nro de doc *//
            if(list.size()<=20){
                List<ArrayRecord> list1 = list;
                int i;
                for(i=0; i < list1.size(); i++)
                {
                    int x;
                    String valor = list1.get(i).get("DOC_PROPIETARIO").toString();
                    for(x=i; x < list.size(); x++){
                        //So("v: " + i + "-" + x + " : " + valor + "==" + list.get(x).get("DOC_PROPIETARIO").toString());
                        if(x != i){
                            if(valor.equals(list.get(x).get("DOC_PROPIETARIO").toString())){
                                //So("removiendo " + x + ": " + list.get(x).get("DOC_PROPIETARIO").toString());                            
                                list.remove(x);
                            }                        
                        }
                    }
                }                
            }
            /* */
            return list;
        } catch (Exception e) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorEnBusquedaBD"));
            e.printStackTrace();
            return null;
        }
    }
    
    public List<HashMap> listarPersonaNatural(String filtro) {
        String campo = "";
        campo = "p.COD_USR = ?1 ";
        
        String sql = "SELECT"
                + " p.COD_USR,"
                + " p.NOMBRE,"
                + " p.APE_PAT,"
                + " COALESCE(p.APE_MAT, ' ') APE_MAT,"
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

    public List<HashMap> listarEfectivo(String filtro) {
        String campo = "";
        campo = "p.COD_USR = ?1 ";
        
        String sql = "SELECT"
                + " p.COD_USR,"
                + " p.NOMBRE,"
                + " p.APE_PAT,"
                + " COALESCE(p.APE_MAT, ' ') APE_MAT,"
                + " COALESCE(p.DOMICILIO, ' ') DOMICILIO,"
                + " COALESCE(p.SEXO, ' ') SEXO,"
                + " p.FEC_NAC,"
                + " COALESCE(d.NOM_DPTO, ' ') NOM_DPTO,"
                + " COALESCE(pr.NOM_PROV,' ') NOM_PROV,"
                + " COALESCE(di.NOM_DIST, ' ') NOM_DIST"
                + " FROM"
                + " RMA1369.EFECTIVO@SUCAMEC p"
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
    
    public List<HashMap> listarExtranjero(String filtro) {
        String campo = "";
        filtro = filtro.replaceFirst("^0*", "");
        //campo = "LTRIM(p.COD_USR, '0')=?1 OR LTRIM(p.CRNT_EXT,'0')=?2";
        campo = "p.COD_USR=?1 OR p.CRNT_EXT=?2";
        
        String sql = "SELECT"
                + " p.COD_USR,"
                + " p.NOMBRE,"
                + " p.APE_PAT,"
                + " COALESCE(p.APE_MAT, ' ') APE_MAT,"
                + " COALESCE(p.DOMICILIO, ' ') DOMICILIO,"
                + " COALESCE(p.SEXO, ' ') SEXO,"
                + " p.FEC_NAC,"
                + " COALESCE(d.NOM_DPTO, ' ') NOM_DPTO,"
                + " COALESCE(pr.NOM_PROV,' ') NOM_PROV,"
                + " COALESCE(di.NOM_DIST, ' ') NOM_DIST"
                + " FROM"
                + " RMA1369.EXTRANJERO@SUCAMEC p"
                + " LEFT JOIN RMA1369.DEPARTAMENTO@SUCAMEC d ON p.COD_DPTO=d.COD_DPTO"
                + " LEFT JOIN RMA1369.PROVINCIA@SUCAMEC pr ON pr.COD_DPTO=d.COD_DPTO AND pr.COD_PROV=p.COD_PROV"
                + " LEFT JOIN RMA1369.DISTRITO@SUCAMEC di ON pr.COD_PROV=di.COD_PROV AND di.COD_DPTO=d.COD_DPTO AND di.COD_DIST=p.COD_DIST"
                + " WHERE "+ campo 
                + "";
        
        Query q = em.createNativeQuery(sql);
        
        q.setParameter(1, nullATodo(filtro.trim()));
        q.setParameter(2, nullATodo(filtro.trim()));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    public List<HashMap> listarEmpresa( String filtro) {
        String campo = "";
        campo = "p.RUC = ?1 ";
        
        String sql = "SELECT"
                + " p.RUC,"
                + " p.RZN_SOC,"
                + " COALESCE(p.DOM_ACT, ' ') DOMICILIO,"
                + " COALESCE(d.NOM_DPTO, ' ') NOM_DPTO,"
                + " COALESCE(pr.NOM_PROV,' ') NOM_PROV,"
                + " COALESCE(di.NOM_DIST, ' ') NOM_DIST"
                + " FROM"
                + " RMA1369.EMPRESA@SUCAMEC p"
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
            //So("foto: " + foto);
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
