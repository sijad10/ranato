/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.sel.citas.data.CitaTurComprobante;
import pe.gob.sucamec.sel.citas.data.CitaTurPersona;
import pe.gob.sucamec.sel.citas.data.CitaTurTurno;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaTurTurnoFacade extends AbstractFacade<CitaTurTurno> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTurTurnoFacade() {
        super(CitaTurTurno.class);
    }

    public List<CitaTurTurno> listarTurnosXId(Long id) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t where t.id = :id and t.activo = 1"
        );
        q.setParameter("id", id);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXFechaTurno(Date d, Long programacionId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.fechaTurno = :fecha "
                + "and t.programacionId.id = :programacionId "
                + "and t.activo = 1"
        );
        q.setParameter("fecha", d);
        q.setParameter("programacionId", programacionId);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXPersonaYDia(Long personaId, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.fechaTurno = :fecha "
                + "and t.activo = 1"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXPersonaDiaConfirmado(Long personaId, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.fechaTurno = :fecha "
                + "and t.estado.codProg = 'TP_EST_CON' "
                + "and t.activo = 1"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosConfirmadosXProgramacionYDia(Long programacionId, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.programacionId.id = :programacionId "
                + "and t.fechaTurno = :fecha "
                + "and t.estado.codProg = 'TP_EST_CON' "
                + "and t.activo = 1"
        );
        q.setParameter("programacionId", programacionId);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosCC(String claveC, String nroDoc, Date fechaTurno) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t FROM CitaTurTurno t where t.activo = 1 ");
        if (fechaTurno != null) {
            sb.append("and t.fechaTurno = :fechaTurno ");
        }
        if (nroDoc != null && !nroDoc.equals("")) {
            sb.append("and t.perExamenId.numDoc = :nroDoc ");
        }
        if (claveC != null && !claveC.equals("")) {
            sb.append("and t.claveConsulta = :claveC");
        }
        javax.persistence.Query q = em.createQuery(sb.toString());
        if (fechaTurno != null) {
            q.setParameter("fechaTurno", fechaTurno);
        }
        if (nroDoc != null && !nroDoc.equals("")) {
            q.setParameter("nroDoc", nroDoc);
        }
        if (claveC != null && !claveC.equals("")) {
            q.setParameter("claveC", claveC);
        }
        return q.getResultList();
    }

    /**
     * REPORTE EXP MOD
     *
     * @param constanciaId
     * @return
     */
    public List<Map> listadoConstanciaArma(String constanciaId) {
        Query q = em.createNativeQuery(
                "SELECT\n"
                + "TCNS.ID \"ID\",\n"
                + "TCNS.HASH_QR \"P_HASHQR\",\n"
                + "DECODE(TCNS.COD_VERIFICA,NULL,'Fecha de Vencimiento: '||TCNS.FECHA_VENCIMIENTO,NULL) \"SUMILLA\",\n"
                + "'La SUCAMEC, hace constar por medio de la presente que el '||DECODE(TG.COD_PROG,'TP_TRA_DEF','administrado ','Personal Operativo ')||PE.NOMBRES||' '||PE.APE_PAT||' '||PE.APE_MAT||\n"
                + "', identificado con '||TD.NOMBRE||' N°'||PE.NUM_DOC||\n"
                + "DECODE(TG.COD_PROG,'TP_TRA_DEF',NULL,' y carnet de Identidad N°'||(SELECT S.NRO_CRN_VIG\n"
                + "FROM RMA1369.SS_EMP_VIG@SUCAMEC S\n"
                + "WHERE S.COD_USR = PE.NUM_DOC))||\n"
                + "'; quien en mérito del expediente N°'||TC.NRO_EXPEDIENTE||\n"
                + "DECODE(TG.COD_PROG,'TP_TRA_DEF',' y portando el arma de fuego de uso civil con las siguientes características:',\n"
                + "' se presenta como trabajador de '||PP.RZN_SOCIAL||', portando arma de fuego de uso civil asignada, con las siguientes caracteristicas:') \"PARRAFO1\",\n"
                + "'Con fecha '||TT.FECHA_CONFIRMA||' se llevó a cabo el Examen de Manejo de Arma de Fuego y Tiro, obteniendo el siguiente resultado:' \"PARRAFO2\",\n"
                + "'Como resultado final de la evaluación se considera '||TR.NOMBRE||'.' \"PARRAFO3\",\n"
                + "'Magdalena, '||TO_CHAR (TT.FECHA_CONFIRMA, 'DD')\n"
                + "|| ' de '\n"
                + "|| TRIM (TO_CHAR (TT.FECHA_CONFIRMA, 'Month'))\n"
                + "|| ' del '\n"
                + "|| TO_CHAR (TT.FECHA_CONFIRMA, 'YYYY') \"PIE1\",\n"
                + "'Documentos presentados:<br/>-Formulario de solicitud virtual\n"
                + "<br/>-Comprobante de pago:'||TC.VOUCHER_TASA||'-'||TC.VOUCHER_SEQ||'-'||TC.VOUCHER_CTA||'-'||TC.VOUCHER_AGENCIA||'-'||TC.VOUCHER_AUTENTICA||\n"
                + "DECODE(TCNS.CANT_MUNICION,NULL,NULL,'<br/>-Se entregaron '||TCNS.CANT_MUNICION||' municiones') \"PIE2\",\n"
                + "'<u><a href=\"https://www.sucamec.gob.pe/sel\">https://www.sucamec.gob.pe/sel</a></u>\n"
                + "<br/>'||DECODE(TCNS.COD_VERIFICA,NULL,NULL,'código de verificación: '||TCNS.COD_VERIFICA) \"URLSEL\"\n"
                + "FROM BDINTEGRADO.TUR_TURNO TT\n"
                + "INNER JOIN BDINTEGRADO.TUR_PERSONA PE ON PE.ID = TT.PER_EXAMEN_ID\n"
                + "INNER JOIN BDINTEGRADO.TUR_PERSONA PP ON PP.ID = TT.PER_PAGO_ID\n"
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TD ON TD.ID = PE.TIPO_DOC\n"
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = TT.TIPO_TRAMITE_ID\n"
                + "INNER JOIN BDINTEGRADO.TUR_TURNO_COMPRO TTC ON TTC.TURNO_ID = TT.ID\n"
                + "INNER JOIN BDINTEGRADO.TUR_COMPROBANTE TC ON TC.ID = TTC.COMPROBANTE_ID\n"
                + "INNER JOIN BDINTEGRADO.TUR_CONSTANCIA TCNS ON TCNS.COMPROBANTE_ID = TC.ID\n"
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TR ON TR.ID = TCNS.RESUL_GRAL\n"
                + "WHERE TCNS.ID = ?");
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, constanciaId);
        res = q.setMaxResults(200).getResultList();
        return res;
    }

    public List<CitaTurTurno> listarTurnosXNroDoc(String nroDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.perExamenId.numDoc = :nroDoc "
                //+ "and t.tipoTramiteId.codProg in ('TP_TRA_DEF','TP_TRA_PP','TP_TRA_TDV','TP_TRA_SIS','TP_TRA_VP','TP_TRA_CAZ','TP_TRA_DEP') "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_VAL') "
                + "and t.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXNroDocXTipo(String nroDoc, String tipo) {
        String condTipo = "";
        switch(tipo){
            case "POL":
                condTipo = "and t.tipoTramiteId.codProg in ('TP_TRA_DEF','TP_TRA_MUL','TP_TRA_PP','TP_TRA_TDV','TP_TRA_SIS','TP_TRA_VP','TP_TRA_CAZ','TP_TRA_DEP') ";
                break;
            case "VAL":
                condTipo = "and t.tipoTramiteId.codProg in ('TP_TRAM_VAL') ";
                break;                
            case "VER":
                condTipo = "and t.tipoTramiteId.codProg in ('TP_TRAM_VER') ";
                break;                
            case "EMPADRONAMIENTO":
                condTipo = "and t.tipoTramiteId.codProg in ('TP_TRAM_EMP') ";
                break;                     
        }
        
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.perExamenId.numDoc = :nroDoc "
                + condTipo
                + "and t.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXRucDocXTipo(String ruc, String tipo) {
        String condTipo = "";
        switch(tipo){
            case "POL":
                condTipo = "and t.tipoTramiteId.codProg in ('TP_TRA_DEF','TP_TRA_MUL','TP_TRA_PP','TP_TRA_TDV','TP_TRA_SIS','TP_TRA_VP','TP_TRA_CAZ','TP_TRA_DEP') ";
                break;
            case "VAL":
                condTipo = "and t.tipoTramiteId.codProg in ('TP_TRAM_VAL') ";
                break;                
            case "VER":
                condTipo = "and t.tipoTramiteId.codProg in ('TP_TRAM_VER') ";
                break;                
        }
        
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.perPagoId.ruc = :ruc "
                + condTipo
                + "and t.activo = 1"
        );
        q.setParameter("ruc", ruc);
        return q.getResultList();
    }
    
    public List<CitaTurTurno> listarTurnosPendientesXPersona(Long programacionId, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.programacionId.id = :programacionId "
                + "and t.fechaTurno = :fecha "
                + "and t.estado.codProg = 'TP_EST_CON' "
                + "and t.activo = 1"
        );
        q.setParameter("programacionId", programacionId);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXNroDocYNroConsulta(String nroDoc, String nroCons) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.claveConsulta = :nroCons "
                + "and t.perExamenId.numDoc = :nroDoc "
                + "and t.estado.codProg = 'TP_EST_PEN' "
                + "and t.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        q.setParameter("nroCons", nroCons);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosReprogramadosUlt10D(Date fechaPro, CitaTurPersona per) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t where t.fechaPrograma >= :fechaPro "
                + "and t.perExamenId.id = :perId "
                + "and t.activo = 1 "
                + "and t.nroReprogramacion is not null"
        );

        q.setParameter("fechaPro", fechaPro);
        q.setParameter("perId", per.getId());
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXCodVerificacion(String codVer) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t where t.codVerifica = :codVer and t.activo = 1 "
        );

        q.setParameter("codVer", codVer);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXCodVerificacionYNroDoc(String codVer, String nroDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t where t.codVerifica = :codVer and t.activo = 1 and t.perExamenId.numDoc like :nroDoc "
        );
        q.setParameter("codVer", codVer);
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }

    public List<CitaTurTurno> buscarTurnoXClaveConsulta(String clave) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t where t.claveConsulta like :clave and t.activo = 1"
        );
        q.setParameter("clave", clave);
        return q.getResultList();
    }
        
    public List<CitaTurTurno> listarTurnosXVoucher(Long secuencia, BigDecimal importe, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where t.secuenciaEmpoce = :secuencia "
                + "and t.montoEmpoce = :importe "
                + "and FUNC('trunc',t.fechaEmpoce) = FUNC('trunc',:fecha)"
                + "and t.activo = 1 order by t.id desc"
        );
        q.setParameter("secuencia", secuencia);
        q.setParameter("importe", importe);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }
    
    public List<CitaTurTurno> listarTurnosXFechaTurno(Date d, Long programacionId, Long sedeId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + "where FUNC('trunc',t.fechaTurno) = FUNC('trunc',:fecha) "
                + "and t.programacionId.id = :programacionId "
                + "and t.tipoSedeId.id = :sedeId "
                + "and t.activo = 1"
        );
        q.setParameter("fecha", d);
        q.setParameter("programacionId", programacionId);
        q.setParameter("sedeId", sedeId);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarCitasReservadas(HashMap mMap) {
        String tipoTramite = mMap.get("tipoTramite").toString();
        String docUser = mMap.get("docUser").toString();
        Date fechaIni = (mMap.get("fechaIni")!=null)? (Date) mMap.get("fechaIni"):null;
        Date fechaFin = (mMap.get("fechaFin")!=null)? (Date) mMap.get("fechaFin"):null;
        String tipUser = (mMap.get("tipUser")!=null)? mMap.get("tipUser").toString():"";
        
        String sql = "SELECT t FROM CitaTurTurno t "
                + " where t.activo=1 "
                //+ " and t.estado.codProg in ('TP_ESTUR_PEN','TP_ESTUR_CON','TP_EST_PEN','TP_EST_CON') "
                + " ";

        if (docUser != null && !docUser.isEmpty()) {
            switch(tipUser){
                case "TP_PER_NAT":
                    sql = sql + " and t.perExamenId.numDoc = :docUser ";
                    break;
                case "TP_PER_JUR":
                    sql = sql + " and t.perPagoId.ruc = :docUser ";
                    break;

            }
        }
        if (tipoTramite != null && !tipoTramite.isEmpty()) {
            switch (tipoTramite) {
                case "TP_TRAM_VAL":
                case "TP_TRAM_VER":
                case "TP_TRAM_EMP":
                    sql = sql + " and t.tipoTramiteId.codProg = :tipoTramite ";
                    break;

                case "TP_TRAM_POL":
                    sql = sql + " and t.tipoTramiteId.codProg IN ('TP_TRA_DEF', 'TP_TRA_PP', 'TP_TRA_TDV', 'TP_TRA_SIS', 'TP_TRA_VP', 'TP_TRA_CAZ', 'TP_TRA_DEP', 'TP_TRA_COL', 'TP_TRA_MUL') ";
                    break;

            }
        }
        if (fechaIni != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) >= FUNC('trunc',:fechaIni)) ";
        }
        if (fechaFin != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) <= FUNC('trunc',:fechaFin)) ";
        }

        sql = sql + " order by t.fechaTurno desc";
        javax.persistence.Query q = em.createQuery(sql);
        if (docUser != null && !docUser.isEmpty()) {
            q.setParameter("docUser", docUser);
        }
        if (tipoTramite != null && !tipoTramite.isEmpty()) {
            switch (tipoTramite) {
                case "TP_TRAM_VAL":
                case "TP_TRAM_VER":
                case "TP_TRAM_EMP":    
                    q.setParameter("tipoTramite", tipoTramite);
                    break;
            }
        }
        if (fechaIni != null) {
            q.setParameter("fechaIni", fechaIni);
        }
        if (fechaFin != null) {
            q.setParameter("fechaFin", fechaFin);
        }
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosConfirmadosValXPersona(String nroDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + " where "
                + " t.perExamenId.numDoc = :nroDoc "
                + "and t.estado.codProg IN('TP_EST_CON','TP_EST_CON') "
                + "and t.tipoTramiteId.codProg = 'TP_TRAM_VAL' "
                + "and t.activo = 1 "
                + " order by t.fechaTurno desc"
        );
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }
    
    public List<CitaTurTurno> listarTurnosConfirmadosXPersonaXTipo(String nroDoc, String tipo) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t "
                + " where "
                + " t.perExamenId.numDoc = :nroDoc "
                + "and t.estado.codProg IN('TP_EST_CON','TP_EST_CON') "
                + "and t.tipoTramiteId.codProg = ' " + tipo +"' "
                + "and t.activo = 1 "
                + " order by t.fechaTurno desc"
        );
        q.setParameter("nroDoc", nroDoc);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosXVoucherCta(Long secuencia, BigDecimal importe, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT DISTINCT t FROM CitaTurTurno t"
                        + " inner join t.turConstanciaList c"
                        + " inner join c.comprobanteId r "
                        + " where r.voucherSeq = :secuencia "
                        //+ "and r.voucherTasa = :importe "
                        + " and FUNC('trunc',r.fechaPago) = FUNC('trunc',:fecha) "
                        + " and t.activo = 1 "
                        + " and r.activo = 1 "
                        + " and r.voucherAutentica = 'TUPA-2018'"
                        //+ " and r.nroExpediente is not null "
                        + " order by t.fechaTurno desc"
        );
        q.setParameter("secuencia", String.valueOf(secuencia));
        //q.setParameter("importe", importe);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<CitaTurTurno> listarTurnosVeriXVoucherCta(Long secuencia, BigDecimal importe, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT DISTINCT t FROM CitaTurTurno t"
                        + " inner join t.turLicenciaRegList c"
                        + " inner join c.comprobanteId r"
                        + " where r.voucherSeq = :secuencia"
                        + " and r.voucherTasa = :importe"
                        + " and FUNC('trunc',r.fechaPago) = FUNC('trunc',:fecha)"
                        + " and r.activo = 1"
                        + " and r.voucherAutentica = 'TUPA-2018'"
                        //+ " and r.nroExpediente is not null "
                        + " and t.estado.id=102"
                        + " and t.fechaConfirma is not null"
                        + " and FUNC('trunc',t.fechaTurno) <= FUNC('trunc',current_date)"
                        + " order by t.fechaTurno desc"
        );
        q.setParameter("secuencia", String.valueOf(secuencia));
        q.setParameter("importe", importe.toString());
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }
    
    public List<Map> listaConfiguraSede(Long idTipoTurno, Long idArea, Long idTipoCita) {
        Query q = em.createNativeQuery(
                "SELECT ID, LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO, MINUTOS_ANTES_INICIO, MINUTOS_TOLERANCIA, NUM_DIAS_RESERVA, NUM_DIAS_RESERVA_HABILES, EXAMEN_VIRTUAL \n"
                + "FROM BDINTEGRADO.TUR_PROGRAMACION_SEDE TU_SED \n"
                + "WHERE TU_SED.TIPO_TURNO = ? AND TU_SED.SEDE_ID = ? AND TU_SED.TIPO_CITA_ID = ? AND ACTIVO = 1");
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, idTipoTurno);
        q.setParameter(2, idArea);
        q.setParameter(3, idTipoCita);
        res = q.getResultList();
        return res;
    }

    public List<Date> listaFechaHabilitadaSede(Long idTipoTurno, Long idArea, Long idTipoCita) {
        Query q = em.createNativeQuery(
                "SELECT SEDE_FECHA.FECHA FROM BDINTEGRADO.TUR_PROGRAMA_SEDE_FECHA SEDE_FECHA\n"
                + "INNER JOIN BDINTEGRADO.TUR_PROGRAMACION_SEDE SEDE ON (SEDE.ID = SEDE_FECHA.PROGRAMA_SEDE_ID) \n"
                + "INNER JOIN BDINTEGRADO.TIPO_BASE TIPO ON (TIPO.ID = SEDE_FECHA.TIPO_REGISTRO) \n"
                + "WHERE SEDE.TIPO_TURNO = ? AND SEDE.SEDE_ID = ? AND SEDE.TIPO_CITA_ID = ? AND SEDE_FECHA.ACTIVO = 1 AND TIPO.COD_PROG ='FECHAHABILITASEDE' ORDER BY 1");
        q.setParameter(1, idTipoTurno);
        q.setParameter(2, idArea);
        q.setParameter(3, idTipoCita);
        return q.getResultList();
    }

    public List<Map> listaSedeFechaHabilitadaFeriado(Long idTipoTurno, Long idArea, Long idTipoCita, Date fecIni, Date fecFin) {
        Query q = em.createNativeQuery("SELECT XX.FECHA, XX.TIPO FROM \n"
            + " (SELECT SEDE_FECHA.FECHA, \n"
            + " (CASE WHEN TIPO.COD_PROG = 'FECHAHABILITASEDE' THEN 'HABILITA' \n"
            + " ELSE 'FERIADO' END) AS TIPO \n"
            + " FROM BDINTEGRADO.TUR_PROGRAMA_SEDE_FECHA SEDE_FECHA \n"
            + " INNER JOIN BDINTEGRADO.TUR_PROGRAMACION_SEDE SEDE ON ( SEDE.ID = SEDE_FECHA.PROGRAMA_SEDE_ID ) \n"
            + " INNER JOIN BDINTEGRADO.TIPO_BASE TIPO ON ( TIPO.ID = SEDE_FECHA.TIPO_REGISTRO ) \n"
            + " WHERE SEDE.TIPO_TURNO = ?1 AND SEDE.SEDE_ID = ?2 AND SEDE.TIPO_CITA_ID = ?3 AND SEDE_FECHA.ACTIVO = 1 \n"  
            + " AND TRUNC(SEDE_FECHA.FECHA) BETWEEN TRUNC(?4) AND TRUNC(?5) \n"                
            + " UNION \n"
            + " SELECT FE.FECHA_FERIADO AS FECHA, 'FERIADO' AS TIPO FROM BDINTEGRADO.SB_FERIADO FE \n"
            + " WHERE TRUNC(FE.FECHA_FERIADO) BETWEEN TRUNC(?4) AND TRUNC(?5) \n"
            + " AND ACTIVO = 1 ) XX \n"
            + " ORDER BY 1");

        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, idTipoTurno);
        q.setParameter(2, idArea);
        q.setParameter(3, idTipoCita);
        q.setParameter(4, fecIni, TemporalType.DATE);
        q.setParameter(5, fecFin, TemporalType.DATE);
        res = q.getResultList();
        return res;
    }

    public List<Map> listaSedeFechaHabilitadaFeriadoEspefifica(Long idTipoTurno, Long idArea, Long idTipoCita, Date fecha) {
        Query q = em.createNativeQuery("SELECT XX.FECHA, XX.TIPO FROM \n"
            + " (SELECT SEDE_FECHA.FECHA, \n"
            + " (CASE WHEN TIPO.COD_PROG = 'FECHAHABILITASEDE' THEN 'HABILITA' \n"
            + " ELSE 'FERIADO' END) AS TIPO \n"
            + " FROM BDINTEGRADO.TUR_PROGRAMA_SEDE_FECHA SEDE_FECHA \n"
            + " INNER JOIN BDINTEGRADO.TUR_PROGRAMACION_SEDE SEDE ON ( SEDE.ID = SEDE_FECHA.PROGRAMA_SEDE_ID ) \n"
            + " INNER JOIN BDINTEGRADO.TIPO_BASE TIPO ON ( TIPO.ID = SEDE_FECHA.TIPO_REGISTRO ) \n"
            + " WHERE SEDE.TIPO_TURNO = ?1 AND SEDE.SEDE_ID = ?2 AND SEDE.TIPO_CITA_ID = ?3 AND SEDE_FECHA.ACTIVO = 1 \n"  
            + " AND TRUNC(SEDE_FECHA.FECHA) = TRUNC(?4) \n"                  
            + " UNION \n"
            + " SELECT FE.FECHA_FERIADO AS FECHA, 'FERIADO' AS TIPO FROM BDINTEGRADO.SB_FERIADO FE \n"
            + " WHERE TRUNC(FE.FECHA_FERIADO) = TRUNC(?4) \n"
            + " AND ACTIVO = 1 ) XX \n"
            + " ORDER BY 1");

        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, idTipoTurno);
        q.setParameter(2, idArea);
        q.setParameter(3, idTipoCita);
        q.setParameter(4, fecha, TemporalType.DATE);
        res = q.getResultList();
        return res;
    }
    
    public List<Map> listaSedeTurnosCupos(Long idTipoTurno, Long idArea, Long idTipoCita, Date fecha) {
        Query q = em.createNativeQuery("SELECT TUR_PROG.SEDE_ID, SED.COD_PROG, TUR_PROG.ID AS ID_PROGRAMACION, \n"
            + " TUR_PROG.HORA, TUR_PROG.CANT_CUPOS, \n"
            + " NVL(COUNT(TUR.ID),0) AS CANT_TURNO,  \n"
            + " (TUR_PROG.CANT_CUPOS - NVL(COUNT(TUR.ID),0) ) AS CANT_LIBRES \n"
            + " FROM BDINTEGRADO.TUR_PROGRAMACION TUR_PROG  \n"
            + " INNER JOIN BDINTEGRADO.TIPO_BASE SED ON (SED.ID = TUR_PROG.SEDE_ID) \n"
            + " LEFT JOIN BDINTEGRADO.TUR_TURNO TUR ON (TUR_PROG.ID = TUR.PROGRAMACION_ID AND TRUNC(TUR.FECHA_TURNO) = TRUNC(?4) AND TUR.ACTIVO = 1 ) \n"
            + " WHERE TUR_PROG.TIPO_TURNO = ?1 AND TUR_PROG.SEDE_ID = ?2 AND TUR_PROG.TIPO_CITA_ID = ?3 AND TUR_PROG.ACTIVO = 1 \n"
            + " GROUP BY TUR_PROG.SEDE_ID, SED.COD_PROG, TUR_PROG.ID, TUR_PROG.HORA, TUR_PROG.CANT_CUPOS \n"  
            + " ORDER BY TUR_PROG.HORA");
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, idTipoTurno);
        q.setParameter(2, idArea);
        q.setParameter(3, idTipoCita);
        q.setParameter(4, fecha, TemporalType.DATE);
        res = q.getResultList();
        return res;
    }

    public List<Map> listaSedeTurnosCuposFechaHabilitada(Long idTipoTurno, Long idArea, Long idTipoCita, Date fecha) {
        Query q = em.createNativeQuery("SELECT TUR_PROG.SEDE_ID, SED.COD_PROG, TUR_PROG.ID AS ID_PROGRAMACION, \n"
            + " TUR_PROG.HORA, TUR_PROG.CANT_CUPOS, \n"
            + " NVL(COUNT(TUR.ID),0) AS CANT_TURNO, \n"
            + " (TUR_PROG.CANT_CUPOS - NVL(COUNT(TUR.ID),0) ) AS CANT_LIBRES \n"
            + " FROM BDINTEGRADO.TUR_PROGRAMACION TUR_PROG  \n"
            + " INNER JOIN BDINTEGRADO.TUR_PROGRAMACION_SEDE TUR_SED ON (TUR_SED.SEDE_ID = TUR_PROG.SEDE_ID) \n"
            + " INNER JOIN BDINTEGRADO.TUR_PROGRAMA_SEDE_FECHA SED_FECHA ON (SED_FECHA.PROGRAMA_SEDE_ID = TUR_SED.ID AND TRUNC(SED_FECHA.FECHA) = TRUNC(?4)) \n"
            + " INNER JOIN BDINTEGRADO.TUR_PROGRAMA_SEDE_FECHA_DET PROG_DET ON ( PROG_DET.PROGRAMA_SEDE_FECHA_ID = SED_FECHA.ID AND TUR_PROG.ID = PROG_DET.PROGRAMACION_ID ) \n"
            + " INNER JOIN BDINTEGRADO.TIPO_BASE SED ON (SED.ID = TUR_PROG.SEDE_ID) \n"
            + " LEFT JOIN BDINTEGRADO.TUR_TURNO TUR ON (TUR_PROG.ID = TUR.PROGRAMACION_ID AND TUR.FECHA_TURNO = SED_FECHA.FECHA AND TUR.ACTIVO = 1 ) \n"
            + " WHERE TUR_PROG.TIPO_TURNO = ?1 AND TUR_PROG.SEDE_ID = ?2 AND TUR_PROG.TIPO_CITA_ID = ?3 AND TUR_PROG.ACTIVO = 1 \n"
            + " AND PROG_DET.ACTIVO = 1 \n"                
            + " GROUP BY TUR_PROG.SEDE_ID, SED.COD_PROG, TUR_PROG.ID, TUR_PROG.HORA, TUR_PROG.CANT_CUPOS \n"  
            + " ORDER BY TUR_PROG.HORA");
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, idTipoTurno);
        q.setParameter(2, idArea);
        q.setParameter(3, idTipoCita);
        q.setParameter(4, fecha, TemporalType.DATE);
        res = q.getResultList();
        return res;
    }
        
    public List<CitaTurTurno> buscarTurnoXSolicitud(Long solicitudId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurTurno t where t.expVirtualSolicitudId.id = :dato and t.activo = 1"
        );
        q.setParameter("dato", solicitudId);
        return q.getResultList();
    }
    
}
