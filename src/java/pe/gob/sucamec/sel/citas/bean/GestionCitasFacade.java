/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.bdintegrado.bean.AmaMaestroArmasFacade;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
//import pe.gob.sucamec.bdintegrado.data.AmaGestionArma;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.AmaTipoLicencia;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.sel.citas.data.CitaTurLicenciaReg;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GestionCitasFacade {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    public final int MAX_RES = 500;

    @EJB
    private AmaMaestroArmasFacade ejbAmaMaestroArmasFacade;
    @EJB
    private SbParametroFacade ejbParametroFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public GestionCitasFacade() {
        super();
        //super(TurTurno.class);
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

    public String nullATodoComodin(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s + "%";
    }

    public List<Map> listarArmasCanceladasXNroLic(String nroLic) {
        String sql = "SELECT * FROM RMA1369.WS_LICENCIAS@SUCAMEC WHERE ESTADO = 'CANCELADO' AND SITUACION != 'ROBADO' AND NRO_LIC = ?";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroLic);
        res = query.getResultList();
        return res;
    }

    public List<Map> listarArmasCanceladasXNroDoc(String nroDoc) {
        String sql = "SELECT NRO_SERIE \"nroSerie\", NRO_LIC \"nroLic\", SITUACION \"situacion\" FROM RMA1369.WS_LICENCIAS@SUCAMEC WHERE ESTADO = 'CANCELADO' AND DOC_PROPIETARIO = ?";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroDoc);
        res = query.getResultList();
        return res;
    }

    public List<Map> buscarArmaRMA(String nroSerie, String docProp, String nroLic) {
        try {
            String jpql = "SELECT DISTINCT AR.NRO_LIC, AR.NRO_SERIE, AR.TIPO_ARMA, AR.MARCA , AR.MODELO,  AR.CALIBRE, AR.TIPO_PROPIETARIO, "
                    + "AR.DOC_PROPIETARIO, AR.PROPIETARIO, AR.TIP_USR_PROPIETARIO, AR.ESTADO, AR.NRO_RUA, AR.SISTEMA "
                    + " FROM RMA1369.INTEROP_WS_LICENCIAS@SUCAMEC AR WHERE AR.NRO_LIC IS NOT NULL ";
            if (nroSerie != null && !"".equals(nroSerie.trim())) {
                jpql += "AND AR.NRO_SERIE = ?1 ";
            }
            if (docProp != null && !"".equals(docProp.trim())) {
                jpql += "AND AR.DOC_PROPIETARIO = ?2 ";
            }
            if (nroLic != null && !"".equals(nroLic.trim())) {
                jpql += "AND TO_NUMBER(AR.NRO_LIC) = ?3 ";
            }
            jpql += "GROUP BY AR.NRO_LIC, AR.NRO_SERIE, AR.TIPO_ARMA, AR.MARCA , AR.MODELO,  AR.CALIBRE, AR.TIPO_PROPIETARIO, AR.DOC_PROPIETARIO, AR.PROPIETARIO, TIP_USR_PROPIETARIO, AR.ESTADO, AR.NRO_RUA, AR.SISTEMA ";
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            if (nroSerie != null && !"".equals(nroSerie.trim())) {
                q.setParameter(1, nroSerie.toUpperCase().trim());
            }
            if (docProp != null && !"".equals(docProp.trim())) {
                q.setParameter(2, docProp.toUpperCase().trim());
            }
            if (nroLic != null && !"".equals(nroLic.trim())) {
                q.setParameter(3, nroLic.toUpperCase().trim());
            }

            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                return q.getResultList();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<ArrayRecord> listarLicenciaxCancelar(HashMap mMap) {
        String tipoFiltro = mMap.get("tipoFiltro").toString();
        String fechaIni = (mMap.get("fechaIni") != null) ? mMap.get("fechaIni").toString() : "";
        String fechaFin = (mMap.get("fechaFin") != null) ? mMap.get("fechaFin").toString() : "";
        String filtroCampo = "";

        if (tipoFiltro != "") {
            switch (tipoFiltro) {
                case "nroLic":
                    filtroCampo = " li.NRO_LIC = ?1";
                    break;
                case "nroDni":
                    filtroCampo = " tp.NUM_DOC = ?1";
                    break;
                case "nroRuc":
                    filtroCampo = " tp.NUM_DOC = ?1";
                    break;
            }
        }

        String filtroFecha = "";
        if (fechaIni != "" && fechaFin != "") {
            filtroFecha = " ((TO_CHAR(li.FEC_EMISION,'DD/MM/YYYY') BETWEEN ?1 AND ?2) OR TO_CHAR(li.FEC_VENCIMIENTO,'DD/MM/YYYY') BETWEEN ?1 AND ?2)) ";
        }
        String sql = " SELECT"
                + "  ta.ID, ta.TURNO_ID, t.PER_EXAMEN_ID, ta.NRO_LIC, ta.SERIE,"
                + "  tp.APE_PAT || ' ' || tp.APE_MAT || ' ' || tp.NOMBRES || ' ' || tp.NUM_DOC SOLICITANTE,"
                + "  li.PORTADOR || ' ' || li.DOC_PORTADOR PORTADOR,"
                + "  li.FEC_EMISION,"
                + "  li.FEC_VENCIMIENTO,"
                + "  li.ESTADO,"
                + "  li.TIPO_LICENCIA MODALIDAD"
                + " FROM BDINTEGRADO.AMA_TURNO_ACTA ta"
                + "  INNER JOIN BDINTEGRADO.TUR_TURNO t ON ta.TURNO_ID=t.ID"
                + "  INNER JOIN BDINTEGRADO.TUR_PERSONA tp ON t.PER_EXAMEN_ID=tp.ID"
                + "  INNER JOIN RMA1369.WS_LICENCIAS@SUCAMEC li ON ta.NRO_LIC=li.NRO_LIC"
                + " WHERE"
                + filtroCampo
                + filtroFecha
                + "";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, mMap.get("filtro").toString().trim());
            if (fechaIni != "" && fechaFin != "") {
                q.setParameter(2, fechaIni);
                q.setParameter(3, fechaFin);
            }

            q.setHint("eclipselink.result-type", "Map");
            List<ArrayRecord> list = q.setMaxResults(MAX_RES).getResultList();
            return list;

        } catch (Exception e) {

            return null;
        }
    }

    public List<AmaLicenciaDeUso> listarLicenciasxCancelar(HashMap mMap) {
        StringBuilder strBld = new StringBuilder();
        String tipoFiltro = ((mMap.get("tipoFiltro") != null) ? mMap.get("tipoFiltro").toString() : "");
        String filtro = ((mMap.get("filtro") != null) ? mMap.get("filtro").toString() : "");
        Date fechaIni = ((mMap.get("fechaIni") != null) ? (Date) mMap.get("fechaIni") : null);
        Date fechaFin = ((mMap.get("fechaFin") != null) ? (Date) mMap.get("fechaFin") : null);

        strBld.append("select a from AmaLicenciaDeUso a where a.activo = 1 ");
        if (fechaIni != null) {
            strBld.append("and (FUNC('trunc',a.fechaEmision) >= FUNC('trunc',:fechaIni)) ");
        }
        if (fechaFin != null) {
            strBld.append("and (FUNC('trunc',a.fechaEmision) <= FUNC('trunc',:fechaFin)) ");
        }
        switch (tipoFiltro) {
            case "nroLic":
                strBld.append("and trim(a.nroLicencia) like :nroLic ");
                break;
            case "nroDni":
                //strBld.append("and a.personaLicenciaId.numDoc in (:numDoc) or a.personaPadreId.numDoc in (:numDoc) ");
                //strBld.append("and a.personaLicenciaId.numDoc = :numDoc or a.personaPadreId.numDoc = :numDoc ");
                strBld.append("and (a.personaLicenciaId.numDoc like :numDoc or a.personaPadreId.numDoc like :numDoc) ");
                break;
            case "nroRuc":
                //strBld.append("and a.personaPadreId.ruc in (:ruc) ");
                strBld.append("and a.personaPadreId.ruc like :ruc ");
                break;

        }
        /*if (filtro != null) {
            if (Objects.equals(campo, "2")) {
                strBld.append("and a.personaLicenciaId.numDoc in (:numDoc) ");
            } else if (Objects.equals(campo, "1")) {
                strBld.append("and a.fotoId.nroExpediente in (:nroExp) ");
            } else if (Objects.equals(campo, "3")) {
                strBld.append("and a.personaPadreId.ruc in (:ruc)");
            }
        }*/
        strBld.append("order by a.personaPadreId.rznSocial, a.personaLicenciaId.apePat, a.personaLicenciaId.apeMat, a.personaLicenciaId.nombres asc");
        Query q = em.createQuery(strBld.toString());
        if (fechaIni != null) {
            q.setParameter("fechaIni", fechaIni);
        }
        if (fechaFin != null) {
            q.setParameter("fechaFin", fechaFin);
        }
        switch (tipoFiltro) {
            case "nroLic":
                q.setParameter("nroLic", nullATodoComodin(filtro));
                break;
            case "nroDni":
                q.setParameter("numDoc", nullATodoComodin(filtro));
                break;
            case "nroRuc":
                q.setParameter("ruc", nullATodoComodin(filtro));
                break;

        }

        return q.getResultList();
    }

    public List<Expediente> listarExpedientesXNroDni(HashMap mMap) {
        String tipoFiltro = mMap.get("tipoFiltro").toString();
        String filtro = ((mMap.get("filtro") != null) ? mMap.get("filtro").toString() : "");

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select e from Expediente e ");
        strBuilder.append("where ");
        //strBuilder.append("e.estado like 'R' ");

        //strBuilder.append("and FUNC('TO_CHAR',e.fechaCreacion,'dd/MM/YYYY') >= :fechaIni and FUNC('TO_CHAR',e.fechaCreacion,'dd/MM/YYYY') <= :fechaFin ");
        //strBuilder.append("and e.idProceso.idProceso in (631,632,633,634,641,643,645,635,636,637,638,642,644,646,639,648,649,650,651,640,647,664,695,694,268,412,296,413,301,414,229,416,228,427,246,428,264,429,202,431,313,438,125,439,130,440,176,441,65,442,116,444,751,722,721,720,719,732,752,730,734,727,726,725,724,733,731,735,728,753,736,742,741,738,737,740,739,729,760,761,762,763,764,765,766,767,768,769,933,935,936,944,945) ");
        strBuilder.append("( ");
        strBuilder.append("e.numero in(select t.nroExpediente from AmaTipoLicencia t where t.nroExpediente is not NULL) or ");
        strBuilder.append("e.numero in(select tp.nroExpediente from AmaTarjetaPropiedad tp where tp.nroExpediente is not NULL)");
        strBuilder.append(" ) ");

        switch (tipoFiltro) {
            case "nroExp":
                strBuilder.append("and (e.numero like :nroExp) ");
                break;
            case "nroDni":
                //strBld.append("and a.personaLicenciaId.numDoc in (:numDoc) or a.personaPadreId.numDoc in (:numDoc) ");
                //strBld.append("and a.personaLicenciaId.numDoc = :numDoc or a.personaPadreId.numDoc = :numDoc ");
                //strBuilder.append("and a.personaLicenciaId.numDoc like :numDoc or a.personaPadreId.numDoc like :numDoc ");
                strBuilder.append("and (e.idCliente.numeroIdentificacion like :numDoc or e.titulo like :numDoc) ");
                break;
        }

        Query q = em.createQuery(strBuilder.toString());
        switch (tipoFiltro) {
            case "nroExp":
                q.setParameter("nroExp", nullATodoComodin(filtro));
                break;
            case "nroDni":
                q.setParameter("numDoc", nullATodoComodin(filtro));
                break;

        }

        return q.getResultList();
    }

    public List<AmaLicenciaDeUso> listarAmaLicenciaXExpediente(String nroExp) {
        Query q = em.createQuery("select distinct a from AmaLicenciaDeUso a "
                + "inner join a.amaTipoLicenciaList tip "
                + "where tip.nroExpediente = :nroExp");
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public AmaLicenciaDeUso AmaLicenciaxExpAnt(Long nroLic) {
        try {
            Query q = em.createQuery("select a from AmaLicenciaDeUso a where a.nroLicencia = :nroLic and a.activo = 0 and (a.estadoId.codProg!='TP_EST_ANU_OFI')"
                    + " order by a.id desc");
            q.setParameter("nroLic", nroLic);
            q.setMaxResults(MAX_RES);
            AmaLicenciaDeUso lic = (AmaLicenciaDeUso) q.getResultList().get(0);
            return lic;
        } catch (Exception e) {
            return null;
        }
    }

    public AmaTarjetaPropiedad AmaTarjetaxExpAnt(Long armaId) {
        try {
            Query q = em.createQuery("select a from AmaTarjetaPropiedad a where a.armaId.id = :armaId and a.activo = 0 and (a.estadoId.codProg!='TP_EST_ANU_OFI')"
                    + " order by a.id desc");
            q.setParameter("armaId", armaId);
            q.setMaxResults(MAX_RES);
            AmaTarjetaPropiedad tar = (AmaTarjetaPropiedad) q.getResultList().get(0);
            return tar;
        } catch (Exception e) {
            return null;
        }
    }

    public List<AmaTipoLicencia> listarAmaTipoLicenciaXExpediente(String nroExp) {
        Query q = em.createQuery("select a from AmaTipoLicencia a where a.nroExpediente = :nroExp and a.activo = 1");
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaTarjetaPropiedad> listarAmaTarjetaXExpediente(String nroExp) {
        Query q = em.createQuery("select a from AmaTarjetaPropiedad a where a.nroExpediente = :nroExp");
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<Map> listarArmasRobPerxFiltro(HashMap mMap) {
        String tipoFiltro = mMap.get("tipoFiltro").toString();
        String filtro = ((mMap.get("filtro") != null) ? mMap.get("filtro").toString() : "");
        String filtroSit = ((mMap.get("filtroSit") != null) ? mMap.get("filtroSit").toString() : "");
        String campo = "";
        String tipo_doc = "";

        if (tipoFiltro.equals("nroDni")) {
            campo = "TIPO_PROPIETARIO IN('PERS. NATURAL') AND DOC_PROPIETARIO = ?1 ";
        }
        if (tipoFiltro.equals("ruc")) {
            campo = "TIPO_PROPIETARIO IN('PERS. JURIDICA') AND DOC_PROPIETARIO = ?1 ";
        }
        if (tipoFiltro.equals("serie")) {
            campo = "NRO_SERIE LIKE ?1 ";
        }

        if (!filtroSit.equals("")) {
            campo += " AND SITUACION IN('" + filtroSit + "')";
        }
        String sql = "SELECT * "
                + "FROM RMA1369.WS_LICENCIAS@SUCAMEC WHERE "
                + campo
                //+ " ESTADO = 'CANCELADO' AND DOC_PROPIETARIO = ?"
                + "";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");

        switch (tipoFiltro) {
            case "nroDni":
            case "ruc":
                query.setParameter(1, nullATodo(filtro.trim().toUpperCase()));
                break;
            case "serie":
                query.setParameter(1, "%" + filtro.trim().toUpperCase() + "%");
                break;
        }

        query.setParameter(1, filtro);
        res = query.getResultList();
        return res;
    }

    public List<AmaArma> listarArmasRobPer(HashMap mMap) {
        String tipoFiltro = ((mMap.get("tipoFiltro") != null) ? mMap.get("tipoFiltro").toString() : "");
        String filtro = ((mMap.get("filtro") != null) ? mMap.get("filtro").toString() : "");
        String filtroSit = ((mMap.get("filtroSit") != null) ? mMap.get("filtroSit").toString() : "");
        Date fechaIni = ((mMap.get("fechaIni") != null) ? (Date) mMap.get("fechaIni") : null);
        Date fechaFin = ((mMap.get("fechaFin") != null) ? (Date) mMap.get("fechaFin") : null);
        String campo = "";
        String inner = "";

        if (tipoFiltro.equals("nroDni")) {
            campo = " and listTar.personaCompradorId.numDoc=:numDoc ";
            inner = " inner join a.amaTarjetaPropiedadList listTar";
        }
        if (tipoFiltro.equals("ruc")) {
            campo = " and listTar.personaCompradorId.ruc=:ruc ";
            inner = " inner join a.amaTarjetaPropiedadList listTar";
        }
        if (tipoFiltro.equals("serie")) {
            campo = " and a.serie like :serie ";
            inner = " inner join a.amaTarjetaPropiedadList listTar";
        }

        if (!filtroSit.equals("")) {
            campo += " and a.situacionId.codProg IN('" + filtroSit + "')";
        }

        if (fechaIni != null) {
            campo += " and (FUNC('trunc',listTar.fechaEmision) >= FUNC('trunc',:fechaIni)) ";
            inner = " inner join a.amaTarjetaPropiedadList listTar";
        }
        if (fechaFin != null) {
            campo += " and (FUNC('trunc',listTar.fechaEmision) <= FUNC('trunc',:fechaFin)) ";
            inner = " inner join a.amaTarjetaPropiedadList listTar";
        }

        String sql = "select a from AmaArma a "
                + inner
                + " where a.activo = 1 " //and listTar.activo = 1"
                + campo
                + "";

        Query query = em.createQuery(sql);
        //Query query = em.createNativeQuery(sql);
        //@SuppressWarnings("UnusedAssignment")
        //List<Map> res = new ArrayList<>();
        //query.setHint("eclipselink.result-type", "Map");

        if (fechaIni != null) {
            query.setParameter("fechaIni", fechaIni);
        }
        if (fechaFin != null) {
            query.setParameter("fechaFin", fechaFin);
        }

        switch (tipoFiltro) {
            case "nroDni":
                query.setParameter("numDoc", nullATodo(filtro.trim().toUpperCase()));
                break;
            case "ruc":
                query.setParameter("ruc", nullATodo(filtro.trim().toUpperCase()));
                break;
            case "serie":
                query.setParameter("serie", nullATodoParcial(filtro.trim()));
                break;
        }

        return query.getResultList();
    }

    public List<AmaTarjetaPropiedad> listarTarjetasXnroDoc(String filtro) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("select a from AmaTarjetaPropiedad a where a.activo = 1 ");
        sbQuery.append("and (a.armaId.serie like :serie ");
        sbQuery.append("or a.personaCompradorId.numDoc like :dni ");
        sbQuery.append("or a.personaCompradorId.ruc like :ruc ");
        sbQuery.append("or a.armaId.nroRua like :nrorua");
        sbQuery.append(") ");
        //sbQuery.append("and a.armaId.situacionId.codProg IN('TP_SITU_POS') ");

        Query q = em.createQuery(sbQuery.toString());
        q.setParameter("dni", filtro.toString());
        q.setParameter("ruc", filtro.toString());
        q.setParameter("serie", filtro.toUpperCase());
        q.setParameter("nrorua", filtro.toUpperCase());
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public SbPersona findByNumDoc(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("SbPersona.findByNumDoc", SbPersona.class).setParameter("numDoc", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<SbPersona> list = q.getResultList();
                if (list.size() > 0) {
                    return list.get(0);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    public SbPersona findByNroCip(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("SbPersona.findByNroCip", SbPersona.class).setParameter("nroCip", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<SbPersona> list = q.getResultList();
                if (list.size() > 0) {
                    return list.get(0);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    public SbPersona findByRuc(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("SbPersona.findByRuc", SbPersona.class).setParameter("ruc", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<SbPersona> list = q.getResultList();
                if (list.size() > 0) {
                    return list.get(0);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

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
                + " WHERE " + campo
                + "";

        Query q = em.createNativeQuery(sql);

        q.setParameter(1, nullATodo(filtro.trim()));
        q.setParameter(2, nullATodo(filtro.trim()));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<TipoGamac> listarTipoGamacSitArmRob(String codigos) {
        List<TipoGamac> respuesta = new ArrayList();
        Query query = em.createQuery("select t from TipoGamac t where t.codProg in (" + codigos + ") and t.activo = 1 ");
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<Expediente> selectExpedienteXNro(String nroExp, String listIdsUsuTD) {
        if (nroExp == null) {
            nroExp = "";
        }

        /*String sql = "select e from Expediente e"
                + " inner join tramdoc.traza t on t.id_expediente = e.id_expediente and t.actual = 1 "
                + " inner join tramdoc.usuario_por_traza ut on (ut.traza = t.id_traza) "
                + " inner join tramdoc.usuario u on (ut.usuario = u.id_usuario) "
                + " where e.numero = :nroExp"
                + " and u.usuario in (" + listIdsUsuTD + ")"
                + " and e.estado != 'X'";*/
        String sql = "select e from Expediente e"
                + " inner join e.trazaList t"
                + " inner join t.usuarioPorTrazaList ut"
                + " where e.numero = :nroExp"
                + " and t.actual = 1"
                + " and ut.usuario1.usuario in (" + listIdsUsuTD + ")"
                + " and e.estado != 'X'";

        Query q = em.createQuery(sql);
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<Expediente> selectExpedienteXGest(String nroExp) {
        if (nroExp == null) {
            nroExp = "";
        }

        String sql = "select e from Expediente e"
                + " where e.numero = :nroExp"
                + "";

        Query q = em.createQuery(sql);
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ArrayRecord> findUbigeo(String filtro) {
        if (filtro != null) {
            String sqlWhere = "";
            if (filtro.trim().contains("/")) {
                sqlWhere = " (trim(a.NOMBRE) || ' / ' || trim(b.NOMBRE) || ' / ' || trim(c.NOMBRE) LIKE ?1)";
            } else {
                sqlWhere = " (trim(c.NOMBRE) LIKE ?1"
                        + " OR trim(b.NOMBRE) LIKE ?2"
                        + " OR trim(a.NOMBRE) LIKE ?3)"; //|| ' ' LIKE ?3)";
            }

            String sql = "SELECT"
                    + " a.ID || ',' || b.ID || ',' || c.ID AS ID,"
                    + " a.NOMBRE || ' / ' || b.NOMBRE || ' / ' || c.NOMBRE AS NOMBRE_UBIGEO"
                    + " FROM BDINTEGRADO.SB_DEPARTAMENTO a"
                    + " LEFT JOIN BDINTEGRADO.SB_PROVINCIA b ON a.ID = b.DEPARTAMENTO_ID"
                    + " LEFT JOIN BDINTEGRADO.SB_DISTRITO c ON b.ID = c.PROVINCIA_ID"
                    + " WHERE"
                    + sqlWhere
                    + " AND (a.ACTIVO=1 AND b.ACTIVO=1 AND c.ACTIVO=1)"
                    + " ORDER BY a.ID, b.ID, c.ID"
                    + "";

            Query q = em.createNativeQuery(sql);
            filtro = filtro.toUpperCase();

            if (filtro.trim().contains("/")) {
                q.setParameter(1, "%" + filtro + "%");
            } else {
                q.setParameter(1, "%" + filtro + "%");
                q.setParameter(2, "%" + filtro + "%");
                q.setParameter(3, filtro + "%");
            }
            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(200).getResultList();
        } else {
            return null;
        }
    }

    /* VERIFICACION DE ARMAS */
    public List<AmaTarjetaPropiedad> listarTarjetasXnroDoc(String filtro, String nroDoc) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("select a from AmaTarjetaPropiedad a where a.activo = 1 ");
        sbQuery.append("and (a.armaId.serie = :serie ");
        sbQuery.append("or a.armaId.nroRua like :nrorua");
        sbQuery.append(") ");
        sbQuery.append("and a.personaCompradorId.numDoc = :dni ");
        //sbQuery.append("and a.armaId.situacionId.codProg IN('TP_SITU_POS') ");

        Query q = em.createQuery(sbQuery.toString());
        q.setParameter("dni", nroDoc.trim());
        q.setParameter("serie", filtro.toUpperCase());
        q.setParameter("nrorua", filtro.toUpperCase());
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<Map> listarTarjetasDiscaXpropietario(String nroDoc) {
        String sql = "SELECT distinct"
                + " l.doc_propietario||' - '||l.propietario \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                /*+ " CASE"
                + " WHEN l.tipo_propietario IN('PERS. NATURAL') THEN 'DNI'"
                + " WHEN l.tipo_propietario IN('PERS. JURIDICA') THEN 'RUC'"
                + " WHEN l.tipo_propietario IN('EXTRANJERO') THEN 'CE'"
                + " WHEN l.tipo_propietario IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "*/
                + " l.doc_propietario \"docComprador\", "
                + " l.nro_serie \"serie\","
                + " l.tipo_arma \"tipoArma\","
                + " l.calibre \"calibre\","
                + " l.marca \"marca\","
                + " l.modelo \"modelo\", "
                + " l.situacion \"situacion\","
                + " l.estado \"estado\","
                + " l.nro_lic \"nroLic\""
                + " FROM rma1369.ws_licencias@SUCAMEC l where l.doc_propietario = ?1"
                //+ " and l.nro_lic not in (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                + " and l.sistema = 'DISCA'";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroDoc);
        res = query.getResultList();
        return res;
    }

    public List<Map> listarTarjetasDiscaXnroDoc(String filtro, String nroDoc) {
        String sql = "SELECT distinct"
                + " l.doc_propietario||' - '||l.propietario \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                + " CASE"
                + " WHEN l.tipo_propietario IN('PERS. NATURAL') THEN 'DNI'"
                + " WHEN l.tipo_propietario IN('PERS. JURIDICA') THEN 'RUC'"
                + " WHEN l.tipo_propietario IN('EXTRANJERO') THEN 'CE'"
                + " WHEN l.tipo_propietario IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "
                + " l.doc_propietario \"docComprador\", "
                + " l.nro_serie \"serie\","
                + " l.tipo_arma \"tipoArma\","
                + " l.calibre \"calibre\","
                + " l.marca \"marca\","
                + " l.modelo \"modelo\", "
                + " l.situacion \"situacion\","
                + " l.estado \"estado\","
                + " l.nro_lic \"nroLic\""
                + " FROM rma1369.ws_licencias@SUCAMEC l where l.nro_serie = ?1"
                + " and (l.doc_propietario = ?2 OR (l.doc_portador = ?2 AND SUBSTR(l.doc_propietario, 0, 2) IN ('15', '10')))"
                + " and l.nro_lic not in (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                + " and l.sistema like 'DISCA'";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro);
        query.setParameter(2, nroDoc);
        res = query.getResultList();
        return res;
    }

    /**
     * CONSULTA MIGRACIÓN ARMA
     * @param filtro
     * @param nroDoc
     * @return 
     */
    public List<Map> listarTarjetasDiscaXnroDocMigra(String filtro, String nroDoc) {
        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_DISCA() + ")";
        String sql = "SELECT DISTINCT"
                + " l.DOC_PROPIETARIO ||' - '||l.propietario \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                + " CASE"
                + " WHEN l.TIPO_PROPIETARIO IN('PERS. NATURAL') THEN 'DNI'"
                + " WHEN l.TIPO_PROPIETARIO IN('PERS. JURIDICA') THEN 'RUC'"
                + " WHEN l.TIPO_PROPIETARIO IN('EXTRANJERO') THEN 'CE'"
                + " WHEN l.TIPO_PROPIETARIO IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "
                + " l.DOC_PROPIETARIO \"docComprador\", "
                + " l.SERIE \"serie\","
                + " l.TIPO_ARMA \"tipoArma\","
                + " l.CALIBRE \"calibre\","
                + " l.MARCA \"marca\","
                + " l.MODELO \"modelo\", "
                + " l.SITUACION_ARMA \"situacion\","
                + " l.ESTADO_ARMA \"estado\","
                + " l.LICENCIA_DISCA \"nroLic\""
                + " FROM " + sqlMigra + " l WHERE UPPER(l.SERIE) = ?1"
                + " AND (l.DOC_PROPIETARIO = ?2 OR (l.DOC_PROPIETARIO LIKE '__" + nroDoc + "_' AND SUBSTR(l.DOC_PROPIETARIO, 0, 2) NOT IN ('20'))) "
                + " AND l.LICENCIA_DISCA NOT IN (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                + " AND l.SISTEMA like 'DISCA'";
        //syso(sql);
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro.toUpperCase());
        query.setParameter(2, nroDoc);
        res = query.getResultList();
        return res;
    }

    public List<Map> listarTarjetasDiscaXSerie(String filtro) {
        String sql = "SELECT distinct"
                + " l.doc_propietario||' - '||l.propietario \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                + " CASE"
                + " WHEN l.tipo_propietario IN('PERS. NATURAL') THEN 'DNI'"
                + " WHEN l.tipo_propietario IN('PERS. JURIDICA') THEN 'RUC'"
                + " WHEN l.tipo_propietario IN('EXTRANJERO') THEN 'CE'"
                + " WHEN l.tipo_propietario IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "
                + " l.doc_propietario \"docComprador\", "
                + " l.nro_serie \"serie\","
                + " l.tipo_arma \"tipoArma\","
                + " l.calibre \"calibre\","
                + " l.marca \"marca\","
                + " l.modelo \"modelo\", "
                + " l.situacion \"situacion\","
                + " l.estado \"estado\","
                + " l.nro_lic \"nroLic\""
                + " FROM rma1369.ws_licencias@SUCAMEC l where l.nro_serie = ?1"
                //+ " and l.doc_propietario = ?2"
                + " and l.nro_lic not in (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                + " and l.sistema like 'DISCA'";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro);
        //query.setParameter(2, nroDoc);
        res = query.getResultList();
        return res;
    }

    /**
     * CONSULTA MIGRACIÓN ARMA
     *
     * @param filtro
     * @return
     */
    public List<Map> listarTarjetasDiscaXSerieMigra(String filtro) {

        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_DISCA() + ")";
        String sql = "SELECT DISTINCT"
                + " DIS.DOC_PROPIETARIO ||' - '|| DIS.PROPIETARIO \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                + " CASE"
                + " WHEN DIS.TIPO_PROPIETARIO IN('PERSONA NATURAL') THEN 'DNI'"
                + " WHEN DIS.TIPO_PROPIETARIO IN('PERSONA JURIDICA') THEN 'RUC'"
                + " WHEN DIS.TIPO_PROPIETARIO IN('EXTRANJERO') THEN 'CE'"
                + " WHEN DIS.TIPO_PROPIETARIO IN('MIEMBRO FFA O PNP') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "
                + " DIS.DOC_PROPIETARIO \"docComprador\", "
                + " DIS.SERIE \"serie\","
                + " DIS.TIPO_ARMA \"tipoArma\","
                + " DIS.CALIBRE \"calibre\","
                + " DIS.MARCA \"marca\","
                + " DIS.MODELO \"modelo\", "
                + " DIS.SITUACION_ARMA \"situacion\","
                + " DIS.ESTADO_ARMA \"estado\","
                + " DIS.LICENCIA_DISCA \"nroLic\""
                + " FROM " + sqlMigra + " DIS WHERE UPPER(DIS.SERIE) = ?1"
                //+ " and l.doc_propietario = ?2"
                + " AND DIS.LICENCIA_DISCA NOT IN (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                //+ " and DIS.SISTEMA like 'DISCA'"
                + "";
        //Syso(sql);
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro.toUpperCase());
        //query.setParameter(2, nroDoc);
        res = query.getResultList();
        return res;
    }

    public AmaArma AmaArmaxSerie(String serie) {
        try {
            Query q = em.createQuery("select a from AmaArma a where a.serie = :serie and a.activo = 1 order by a.id desc");
            q.setParameter("serie", serie);
            q.setMaxResults(MAX_RES);
            AmaArma tar = (AmaArma) q.getResultList().get(0);
            return tar;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map> validarSerieWSLicencias(String serie) {

        serie = serie.replace("%", "");

        String sql = "SELECT l.*"
                + " FROM rma1369.ws_licencias@SUCAMEC l where l.nro_serie = ?1"
                + " ORDER BY l.SISTEMA DESC"
                + "";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, serie);
        res = query.getResultList();

        return res;
        /*if (res.size()>0) {
            return true;
        } else {
            return false;
        }*/
    }

    /**
     * CONSULA MIGRACIÓN ARMA
     * @param serie
     * @param personaId
     * @return 
     */
    public List<Map> validarSerieGeneral(String serie, Long personaId) {

        serie = serie.replace("%", "");

        String sql = ejbAmaMaestroArmasFacade.getSQL_GENERAL() + " LA"
                + " ";

        String condicion = "(LA.SERIE=?1";
        if (personaId!=null) {
            condicion += " OR LA.PERSONA_ID=" + personaId;
        }
        condicion += ")";
        
        sql += " WHERE " + condicion;
        //Syso("sql: " + sql);
        try {
            Query query = em.createNativeQuery(sql);
            @SuppressWarnings("UnusedAssignment")
            List<Map> res = new ArrayList<>();
            query.setHint("eclipselink.result-type", "Map");
            query.setParameter(1, serie);
            res = query.getResultList();

            return res;

        } catch (Exception e) {
            return null;
        }
        /*if (res.size()>0) {
            return true;
        } else {
            return false;
        }*/
    }

    public List<AmaTarjetaPropiedad> listarTarjetasXRuc(String filtro, String ruc) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("select a from AmaTarjetaPropiedad a where a.activo = 1 ");
        sbQuery.append("and (a.armaId.serie like :serie ");
        sbQuery.append("or a.armaId.nroRua like :nrorua");
        sbQuery.append(") ");
        sbQuery.append("and a.personaCompradorId.ruc = :ruc ");
        //sbQuery.append("and a.armaId.situacionId.codProg IN('TP_SITU_POS') ");

        Query q = em.createQuery(sbQuery.toString());
        q.setParameter("ruc", ruc.trim());
        q.setParameter("serie", filtro.toUpperCase());
        q.setParameter("nrorua", filtro.toUpperCase());
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<Map> listarTarjetasDiscaXRuc(String filtro, String ruc) {
        String sql = "SELECT distinct"
                + " l.doc_propietario||' - '||l.propietario \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                + " CASE"
                + " WHEN l.tipo_propietario IN('PERS. NATURAL') THEN 'DNI'"
                + " WHEN l.tipo_propietario IN('PERS. JURIDICA') THEN 'RUC'"
                + " WHEN l.tipo_propietario IN('EXTRANJERO') THEN 'CE'"
                + " WHEN l.tipo_propietario IN('FAP', 'POLICIA', 'MARINA', 'MILITAR', 'EJERCITO') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "
                + " l.doc_propietario \"docComprador\", "
                + " l.nro_serie \"serie\","
                + " l.tipo_arma \"tipoArma\","
                + " l.calibre \"calibre\","
                + " l.marca \"marca\","
                + " l.modelo \"modelo\", "
                + " l.situacion \"situacion\","
                + " l.estado \"estado\","
                + " l.nro_lic \"nroLic\""
                + " FROM rma1369.ws_licencias@SUCAMEC l where l.nro_serie like ?1"
                + " and l.doc_propietario = ?2"
                + " and l.nro_lic not in (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                + " and l.sistema like 'DISCA'";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro);
        query.setParameter(2, ruc);
        res = query.getResultList();
        return res;
    }

    /**
     * CONSULTA MIGRACIÓN ARMA
     * @param filtro
     * @param ruc
     * @return 
     */
    public List<Map> listarTarjetasDiscaXRucMigra(String filtro, String ruc) {
        
        String sqlMigra = "(" + ejbAmaMaestroArmasFacade.getSQL_DISCA() + ")";
        String sql = "SELECT DISTINCT"
                + " DIS.DOC_PROPIETARIO ||' - '|| DIS.PROPIETARIO \"personaComprador\","
                //+ " l.tipo_propietario \"tipoComprador\","
                + " CASE"
                + " WHEN DIS.TIPO_PROPIETARIO IN('PERSONA NATURAL') THEN 'DNI'"
                + " WHEN DIS.TIPO_PROPIETARIO IN('PERSONA JURIDICA') THEN 'RUC'"
                + " WHEN DIS.TIPO_PROPIETARIO IN('EXTRANJERO') THEN 'CE'"
                + " WHEN DIS.TIPO_PROPIETARIO IN('MIEMBRO FFA O PNP') THEN 'CIP'"
                + " END"
                + " AS \"tipoComprador\", "
                + " DIS.DOC_PROPIETARIO \"docComprador\", "
                + " DIS.SERIE \"serie\","
                + " DIS.TIPO_ARMA \"tipoArma\","
                + " DIS.CALIBRE \"calibre\","
                + " DIS.MARCA \"marca\","
                + " DIS.MODELO \"modelo\", "
                + " DIS.SITUACION_ARMA \"situacion\","
                + " DIS.ESTADO_LIC_DISCA \"estado\","
                + " DIS.LICENCIA_DISCA \"nroLic\""
                + " FROM " + sqlMigra + " DIS WHERE DIS.SERIE = ?1"
                + " AND DIS.doc_propietario = ?2"
                + " AND DIS.LICENCIA_DISCA NOT IN (select a.licencia_Disca from bdintegrado.ama_arma a where a.licencia_disca is not null and a.activo = 1) "
                + " and DIS.SISTEMA like 'DISCA'"
                + "";
        
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();  
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro);
        query.setParameter(2, ruc);
        res = query.getResultList();
        return res;
    }

    public List<AmaTarjetaPropiedad> listTarjetaArmasPropietario(Long propietarioId, String situacionArma) {
        List<AmaTarjetaPropiedad> listRes = new ArrayList<>();
        String jpql = "select tp from AmaTarjetaPropiedad tp "
                + " where tp.personaCompradorId.id = :propietarioId and tp.activo=1"
                + " and tp.armaId.situacionId.codProg IN('" + situacionArma + "')";
        Query q = em.createQuery(jpql);
        q.setParameter("propietarioId", propietarioId);

        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<CitaTurLicenciaReg> listarArmasVerificacion(Long armaId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurLicenciaReg t "
                + "where t.turnoId.tipoTramiteId.codProg = 'TP_TRAM_VER' "
                + "and t.activo = 1 "
                + "and t.actual = 1 "
                + "and t.armaId.id = :armaId "
                + "order by t.id"
        );
        q.setParameter("armaId", armaId);
        return q.getResultList();
    }

    public List<AmaTarjetaPropiedad> listTarjetaSispePropietario(Long propietarioId) {
        List<AmaTarjetaPropiedad> listRes = new ArrayList<>();
        String jpql = "select tp from AmaTarjetaPropiedad tp "
                + " where tp.activo=1 and tp.personaCompradorId.id = :propietarioId "
                //+ " and tp.armaId.estadoId.codProg = 'TP_ESTA_OPE'"
                + " and tp.armaId.situacionId.codProg in ('TP_SITU_POS', 'TP_SITU_POS_EP', 'TP_SITU_POS_LC')"
                + " and tp.modalidadId.codProg = 'TP_MOD_SIS'"
                + "";

        Query q = em.createQuery(jpql);
        q.setParameter("propietarioId", propietarioId);

        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            //Syso("armas: " + q.getResultList().size());
            listRes = q.getResultList();
        }
        return listRes;
    }

}
