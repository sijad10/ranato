/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.turreg.data.TurTurno;

/**
 *
 * @author msalinas
 */
@Stateless
public class TurTurnoFacade extends AbstractFacade<TurTurno> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TurTurnoFacade() {
        super(TurTurno.class);
    }

    public List<TurTurno> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from TurTurno t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosXPersonaDiaConfirmado(Long personaId, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.fechaTurno = :fecha "
                + "and t.estado.codProg = 'TP_ESTUR_PEN' " //TP_ESTUR_CON
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_INT') "
                + "and t.activo = 1 "
                + "order by t.programacionId.hora desc"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosXPersonaYDia(Long personaId, Date fecha, String cpArea) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_REG','TP_TRAM_PSI') "
                + "and t.programacionId.sedeId.codProg = :cpArea "
                + "and t.fechaTurno = :fecha "
                + "and t.activo = 1 "
                + "order by t.programacionId.hora desc"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        q.setParameter("cpArea", cpArea);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosIntXPersonaYDia(Long personaId, Date fecha, Long sedeId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_INT') "
                + "and t.fechaTurno = :fecha "
                + "and t.tipoSedeId.id = :sedeId "
                + "and t.activo = 1 "
                + "order by t.programacionId.hora desc"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("sedeId", sedeId);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosConfirmadosXProgramacionYDia(Long programacionId, Date fecha, Long sedeId, String codTramite, String codEstado) {
        String jpql = "SELECT t FROM TurTurno t "
                + "where t.activo = 1 "
                + "and (FUNC('trunc',t.fechaTurno) = FUNC('trunc',:fecha))"
                + "and (t.tipoSedeId.id = :sedeId or t.programacionId.sedeId.id = :sedeId) ";
        if (programacionId != null) {
            jpql += "and t.programacionId.id = :programacionId ";
        }
        if (codTramite != null && !"".equals(codTramite.trim())) {
            jpql += "and t.tipoTramiteId.codProg = :codTra ";
        }
        if (codEstado != null && !"".equals(codEstado.trim())) {
            jpql += "and t.estado.codProg = :codEst ";
        }
        jpql += "order by t.nroTurno, t.id";
        javax.persistence.Query q = em.createQuery(jpql);
        if (programacionId != null) {
            q.setParameter("programacionId", programacionId);
        }
        if (codTramite != null && !"".equals(codTramite.trim())) {
            q.setParameter("codTra", codTramite.trim());
        }
        if (codEstado != null && !"".equals(codEstado.trim())) {
            q.setParameter("codEst", codEstado.trim());
        }
        q.setParameter("fecha", fecha);
        q.setParameter("sedeId", sedeId);
        return q.getResultList();
    }

    public List<Map> listarTarjetasDisca(String filtro) {
        String sql = "SELECT distinct l.doc_propietario||' - '||l.propietario \"personaComprador\", \n"
                + "                l.nro_serie \"serie\", l.tipo_arma \"tipoArma\", l.calibre \"calibre\", l.marca \"marca\",\n"
                + "                l.modelo \"modelo\", l.situacion \"situacion\", l.estado \"estado\", l.tipo_licencia \"tipoLicencia\", l.nro_lic \"nroLic\",\n"
                + "                l.fec_vencimiento \"fecVencimiento\" FROM rma1369.ws_licencias@SUCAMEC l \n"
                + "                where l.doc_propietario like ?1 or l.nro_serie like ?2 or l.nro_lic like ?3 ";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro);
        query.setParameter(2, filtro);
        query.setParameter(3, filtro);
        res = query.getResultList();
        return res;
    }

    public List<Map> listarTarjetasDiscaXNroLic(String nroLic) {
        String sql = "SELECT distinct l.doc_propietario||' - '||l.propietario \"personaComprador\", \n"
                + "                l.nro_serie \"serie\", l.tipo_arma \"tipoArma\", l.calibre \"calibre\", l.marca \"marca\",\n"
                + "                l.modelo \"modelo\", l.situacion \"situacion\", l.estado \"estadoDisca\", l.tipo_licencia \"tipoLicencia\", l.nro_lic \"nroLic\",\n"
                + "                l.fec_vencimiento \"fecVencimiento\" FROM rma1369.INTEROP_WS_LICENCIAS@SUCAMEC l \n"
                + "                where l.nro_lic like ?1 ";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroLic);
        res = query.getResultList();
        return res;
    }

    public List<Map> listarWsLicenciasXNroSerie(String nroSerie) {
        String sql = "SELECT distinct l.doc_propietario||' - '||l.propietario \"personaComprador\", "
                + " l.nro_serie \"serie\", l.tipo_arma \"tipoArma\", l.calibre \"calibre\", l.marca \"marca\", "
                + " l.modelo \"modelo\", l.situacion \"situacion\", l.estado \"estadoDisca\", l.tipo_licencia \"tipoLicencia\", l.nro_lic \"nroLic\", "
                + " l.fec_vencimiento \"fecVencimiento\", l.sistema \"sistema\" FROM rma1369.INTEROP_WS_LICENCIAS@SUCAMEC l "
                + " where l.nro_serie = ?1"
                + " order by l.sistema desc ";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroSerie);
        res = query.getResultList();
        return res;
    }

    public List<Map> listarTarjetasDiscaXNroLicPropietario(String nroLic, String nroDoc, String ruc) {
        String sql = "SELECT distinct l.doc_propietario||' - '||l.propietario \"personaComprador\",\n"
                + "l.nro_serie \"serie\", l.tipo_arma \"tipoArma\", l.calibre \"calibre\", l.marca \"marca\",\n"
                + "l.modelo \"modelo\", l.situacion \"situacion\", l.tipo_licencia \"tipoLicencia\", l.nro_lic \"nroLic\",\n"
                + "l.fec_vencimiento \"fecVencimiento\" from rma1369.ws_licencias@SUCAMEC l\n";
        if (ruc != null) {
            sql = sql + "where l.nro_lic like ?1 and (l.doc_propietario = ?2 or l.doc_propietario = ?3)";
        } else {
            sql = sql + "where l.nro_lic like ?1 and l.doc_propietario = ?2";
        }

        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, nroLic);
        query.setParameter(2, nroDoc);
        if (ruc != null) {
            query.setParameter(3, ruc.trim());
        }
        res = query.getResultList();
        return res;
    }

    public List<TurTurno> listarCitasReservadas(String filtro, Date fecIni, Date fecFin, Long sedeId, String codTipoTra) {
        String sql = "SELECT t FROM TurTurno t "
                + " where t.activo = 1 "
                + " and t.estado.codProg in ('TP_ESTUR_PEN','TP_ESTUR_CON','TP_EST_PEN','TP_EST_CON') ";

        if (filtro != null && !filtro.isEmpty()) {
            sql = sql + " and (t.perExamenId.numDoc like :campo OR t.perExamenId.apePat like :campo OR t.perExamenId.apeMat like :campo OR t.perExamenId.nombres like :campo) ";
        }
        if (sedeId != null && !Objects.equals(sedeId, "")) {
            sql = sql + " and t.programacionId.sedeId.id = :sedeId ";
        }
        if (fecIni != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) >= FUNC('trunc',:fecIni)) ";
        }
        if (fecFin != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) <= FUNC('trunc',:fecFin)) ";
        }
        if (codTipoTra != null && !"".equals(codTipoTra)) {
            sql = sql + " and t.tipoTramiteId.codProg = :tram ";
        }

        sql = sql + " order by t.fechaTurno desc";
        javax.persistence.Query q = em.createQuery(sql);
        if (filtro != null && !filtro.isEmpty()) {
            q.setParameter("campo", '%' + filtro + '%');
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }
        if (fecIni != null) {
            q.setParameter("fecIni", fecIni);
        }
        if (fecFin != null) {
            q.setParameter("fecFin", fecFin);
        }
        if (codTipoTra != null && !"".equals(codTipoTra)) {
            q.setParameter("tram", codTipoTra.toUpperCase().trim());
        }
        return q.getResultList();
    }

    public List<TurTurno> listarCitasReservadasInt(String filtro, Date fecIni, Date fecFin, Long sedeId) {
        String sql = "SELECT t FROM TurTurno t "
                + " where t.activo = 1 "
                + " and t.estado.codProg in ('TP_ESTUR_PEN','TP_ESTUR_CON','TP_EST_PEN','TP_EST_CON') "
                + " and t.tipoTramiteId.codProg = 'TP_TRAM_INT' ";

        if (filtro != null && !filtro.isEmpty()) {
            sql = sql + " and (t.perExamenId.numDoc like :campo OR t.perExamenId.apePat like :campo OR t.perExamenId.apeMat like :campo OR t.perExamenId.nombres like :campo) ";
        }
        if (sedeId != null && !Objects.equals(sedeId, "")) {
            sql = sql + " and t.programacionId.sedeId.id = :sedeId ";
        }
        if (fecIni != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) >= FUNC('trunc',:fecIni)) ";
        }
        if (fecFin != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) <= FUNC('trunc',:fecFin)) ";
        }

        sql = sql + " order by t.fechaTurno desc";
        javax.persistence.Query q = em.createQuery(sql);
        if (filtro != null && !filtro.isEmpty()) {
            q.setParameter("campo", '%' + filtro + '%');
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }
        if (fecIni != null) {
            q.setParameter("fecIni", fecIni);
        }
        if (fecFin != null) {
            q.setParameter("fecFin", fecFin);
        }
        return q.getResultList();
    }

    public List<TurTurno> buscarTurnoXId(Long id) {
        Query q = em.createQuery("select t from TurTurno t where t.id = :id");
        q.setParameter("id", id);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosXPersona(String numDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.numDoc like :numDoc "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_REG','TP_TRAM_PSI') "
                + "and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + "and t.activo = 1 "
                + "order by t.id desc"
        );
        q.setParameter("numDoc", numDoc);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosXPersonaPoligono(String numDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where (t.perExamenId.numDoc = :numDoc1 or t.perPagoId.ruc = :numDoc2) "
                + "and t.tipoTramiteId.codProg in ('TP_TRA_DEF','TP_TRA_PP','TP_TRA_TDV','TP_TRA_SIS','TP_TRA_VP','TP_TRA_CAZ','TP_TRA_DEP','TP_TRA_COL','TP_TRA_MUL') "
                + "and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + "and t.activo = 1 "
                + "order by t.id desc"
        );
        q.setParameter("numDoc1", numDoc);
        q.setParameter("numDoc2", numDoc);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosXPersonaPoligonoMulti(String numDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.numDoc like :numDoc "
                + "and t.tipoTramiteId.codProg in ('TP_TRA_DEF','TP_TRA_CAZ','TP_TRA_DEP','TP_TRA_COL','TP_TRA_MUL') "
                + "and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + "and t.activo = 1 "
                + "order by t.id desc"
        );
        q.setParameter("numDoc", numDoc);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosSedeCentral(String numDoc, String cpTipTram) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.numDoc like :numDoc "
                + "and t.tipoTramiteId.codProg = :cpTipTram "
                + "and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + "and t.activo = 1 "
                + "order by t.id desc"
        );
        q.setParameter("numDoc", numDoc);
        q.setParameter("cpTipTram", cpTipTram);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosXCodProg(String numDoc, String cpTipTram) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.numDoc like :numDoc "
                + "and t.tipoTramiteId.codProg = :cpTipTram "
                + "and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + "and t.activo = 1 "
                + "order by t.id desc"
        );
        q.setParameter("numDoc", numDoc);
        q.setParameter("cpTipTram", cpTipTram);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosProvinciaXCodProg(String numDoc, String cpTipTram) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.numDoc like :numDoc "
                + "and t.tipoTramiteId.codProg = :cpTipTram "
                + "and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + "and t.programacionId.sedeId.codProg != 'TP_AREA_TRAM' "
                + "and t.activo = 1 "
                + "order by t.id desc"
        );
        q.setParameter("numDoc", numDoc);
        q.setParameter("cpTipTram", cpTipTram);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosProvincia(String numDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.numDoc like :numDoc "
                + "and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + "and t.programacionId.sedeId.codProg != 'TP_AREA_TRAM' "
                + "and t.activo = 1 "
                + "order by t.id desc"
        );
        q.setParameter("numDoc", numDoc);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosRegXPersona(String numDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.numDoc = :numDoc "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_REG','TP_TRAM_PSI') "
                + "and t.activo = 1"
        );
        q.setParameter("numDoc", numDoc);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosRegConfirmadoXPersona(String numDoc) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.numDoc = :numDoc "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_REG','TP_TRAM_PSI') "
                + "and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + "and t.activo = 1 "
                + "order by t.id desc"
        );
        q.setParameter("numDoc", numDoc);
        return q.getResultList();
    }

    /*
    * FUNCIONES PARA VALIDACION DE ARMAS
     */
    public List<TurTurno> listarTurnosValXPersonaYDia(Long personaId, Date fecha, String cpArea) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_VAL') "
                + "and t.programacionId.sedeId.codProg = :cpArea "                        
                + "and (FUNC('trunc',t.fechaTurno) = FUNC('trunc',:fecha))"
                + "and t.activo = 1 "
                + "order by t.programacionId.hora desc"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        q.setParameter("cpArea", cpArea);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosValXPersonaDiaConfirmado(Long personaId, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.fechaTurno = :fecha "
                + "and t.estado.codProg = 'TP_ESTUR_PEN' " //TP_ESTUR_CON
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_VAL') "
                + "and t.activo = 1 "
                + "order by t.programacionId.hora desc"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<TurTurno> listarCitasConfirmadasVal(HashMap mMap, Long sedeId) {
        String tipoTramite = mMap.get("tipoTramite").toString();
        Date fechaIni = (mMap.get("fechaIni") != null) ? (Date) mMap.get("fechaIni") : null;
        Long programacionId = (mMap.get("programacionId") != null) ? (Long) mMap.get("programacionId") : null;
        //Long sede = (mMap.get("sedeId")!=null)? (Long) mMap.get("sedeId"):null;

        String sql = "SELECT t FROM TurTurno t "
                + " where t.activo = 1 "
                + " and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                + " and t.tipoTramiteId.codProg = 'TP_TRAM_VAL' ";

        /*if (filtro != null && !filtro.isEmpty()) {
            sql = sql + " and (t.perExamenId.numDoc like :campo OR t.perExamenId.apePat like :campo OR t.perExamenId.apeMat like :campo OR t.perExamenId.nombres like :campo) ";
        }
        if (sedeId != null && !Objects.equals(sedeId, "")) {
            sql = sql + " and t.programacionId.sedeId.id = :sedeId ";
        }*/
        if (tipoTramite != null && !tipoTramite.isEmpty()) {
            sql = sql + " and t.tipoTramiteId.codProg = :tipoTramite ";
        }
        if (fechaIni != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) >= FUNC('trunc',:fechaIni)) ";
        }
        if (programacionId != null) {
            sql = sql + " and t.programacionId.id = :programacionId ";
        }
        if (sedeId != null) {
            sql = sql + " and t.tipoSedeId.id = :sedeId ";
        }

        sql = sql + " order by t.fechaTurno desc";
        javax.persistence.Query q = em.createQuery(sql);
        /*if (filtro != null && !filtro.isEmpty()) {
            q.setParameter("campo", '%' + filtro + '%');
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }*/
        if (tipoTramite != null && !tipoTramite.isEmpty()) {
            q.setParameter("tipoTramite", tipoTramite);
        }
        if (fechaIni != null) {
            q.setParameter("fechaIni", fechaIni);
        }
        if (programacionId != null) {
            q.setParameter("programacionId", programacionId);
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }
        return q.getResultList();
    }

    public List<TurTurno> listarCitasReservadasVal(String filtro, Date fecIni, Date fecFin, Long sedeId) {
        String sql = "SELECT t FROM TurTurno t "
                + " where t.activo = 1 "
                + " and t.estado.codProg in ('TP_ESTUR_PEN','TP_ESTUR_CON','TP_EST_PEN','TP_EST_CON') "
                + " and t.tipoTramiteId.codProg = 'TP_TRAM_VAL' "
                + "";

        if (filtro != null && !filtro.isEmpty()) {
            sql = sql + " and (t.perExamenId.numDoc like :campo OR t.perExamenId.apePat like :campo OR t.perExamenId.apeMat like :campo OR t.perExamenId.nombres like :campo) ";
        }
        if (sedeId != null && !Objects.equals(sedeId, "")) {
            sql = sql + " and t.programacionId.sedeId.id = :sedeId ";
        }
        if (fecIni != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) >= FUNC('trunc',:fecIni)) ";
        }
        if (fecFin != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) <= FUNC('trunc',:fecFin)) ";
        }

        sql = sql + " order by t.fechaTurno desc, t.id desc";
        javax.persistence.Query q = em.createQuery(sql);
        if (filtro != null && !filtro.isEmpty()) {
            q.setParameter("campo", '%' + filtro + '%');
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }
        if (fecIni != null) {
            q.setParameter("fecIni", fecIni);
        }
        if (fecFin != null) {
            q.setParameter("fecFin", fecFin);
        }
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosConfirmadosXProgramacionYDiaVal(Long programacionId, Date fecha, Long sedeId, String codTramite, String codEstado) {
        String jpql = "SELECT t FROM TurTurno t "
                + "where t.activo = 1 "
                + "and (FUNC('trunc',t.fechaTurno) = FUNC('trunc',:fecha))"
                + "and (t.tipoSedeId.id = :sedeId or t.programacionId.sedeId.id = :sedeId) ";
        if (programacionId != null) {
            jpql += "and t.programacionId.id = :programacionId ";
        }
        if (codTramite != null && !"".equals(codTramite.trim())) {
            jpql += "and t.tipoTramiteId.codProg = :codTra ";
        }
        if (codEstado != null && !"".equals(codEstado.trim())) {
            jpql += "and t.estado.codProg = :codEst ";
        }
        jpql += "order by t.nroTurno, t.id";
        javax.persistence.Query q = em.createQuery(jpql);
        if (programacionId != null) {
            q.setParameter("programacionId", programacionId);
        }
        if (codTramite != null && !"".equals(codTramite.trim())) {
            q.setParameter("codTra", codTramite.trim());
        }
        if (codEstado != null && !"".equals(codEstado.trim())) {
            q.setParameter("codEst", codEstado.trim());
        }
        q.setParameter("fecha", fecha);
        q.setParameter("sedeId", sedeId);
        return q.getResultList();
    }

    /* */

 /*
    * FUNCIONES PARA VERIFICACION DE ARMAS
     */
    public List<TurTurno> listarTurnosValXPersonaYDiaVer(Long personaId, Date fecha, String cpArea) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_VER') "
                + "and t.programacionId.sedeId.codProg = :cpArea "
                + "and (FUNC('trunc',t.fechaTurno) = FUNC('trunc',:fecha))"
                + "and t.activo = 1 "
                + "order by t.programacionId.hora desc"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        q.setParameter("cpArea", cpArea);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosValXPersonaYDiaEmpadronamiento(Long personaId, Date fecha, String cpArea) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_EMP') "
                + "and t.programacionId.sedeId.codProg = :cpArea "
                + "and (FUNC('trunc',t.fechaTurno) = FUNC('trunc',:fecha))"
                + "and t.estado.codProg IN ('TP_ESTUR_PEN', 'TP_EST_PEN') " //Que este pendiente de confirmación
                + "and t.activo = 1 "
                + "order by t.programacionId.hora asc"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        q.setParameter("cpArea", cpArea);
        return q.getResultList();
    }
    
    public List<TurTurno> listarTurnosValXPersonaDiaConfirmadoVer(Long personaId, Date fecha) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.perExamenId.id = :personaId "
                + "and t.fechaTurno = :fecha "
                + "and t.estado.codProg IN ('TP_ESTUR_PEN', 'TP_EST_PEN') " //TP_ESTUR_CON
                + "and t.tipoTramiteId.codProg in ('TP_TRAM_VER') "
                + "and t.activo = 1 "
                + "order by t.programacionId.hora desc"
        );
        q.setParameter("personaId", personaId);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosConfirmadosXProgramacionYDiaVer(Long programacionId, Date fecha, Long sedeId, String codTramite, String codEstado) {
        String jpql = "SELECT t FROM TurTurno t "
                + "where t.activo = 1 "
                + "and t.fechaTurno = :fecha "
                //+ "and (FUNC('trunc',t.fechaTurno) = FUNC('trunc',:fecha))"
                + "and (t.tipoSedeId.id = :sedeId or t.programacionId.sedeId.id = :sedeId) ";
        if (programacionId != null) {
            jpql += "and t.programacionId.id = :programacionId ";
        }
        if (codTramite != null && !"".equals(codTramite.trim())) {
            jpql += "and t.tipoTramiteId.codProg = :codTra ";
        }
        if (codEstado != null && !"".equals(codEstado.trim())) {
            jpql += "and t.estado.codProg = :codEst ";
        }
        jpql += "order by t.nroTurno, t.id";
        javax.persistence.Query q = em.createQuery(jpql);
        if (programacionId != null) {
            q.setParameter("programacionId", programacionId);
        }
        if (codTramite != null && !"".equals(codTramite.trim())) {
            q.setParameter("codTra", codTramite.trim());
        }
        if (codEstado != null && !"".equals(codEstado.trim())) {
            q.setParameter("codEst", codEstado.trim());
        }
        q.setParameter("fecha", fecha);
        q.setParameter("sedeId", sedeId);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosXNroDocYNroConsulta(String nroDoc, String nroCons) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.claveConsulta = :nroCons "
                + "and t.perExamenId.numDoc = :nroDoc "
                + "and t.estado.codProg = 'TP_EST_PEN' "
                + "and t.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        q.setParameter("nroCons", nroCons);
        return q.getResultList();
    }

    public List<TurTurno> listarTurnosXRUCyNroConsulta(String nroDoc, String nroCons) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
                + "where t.claveConsulta = :nroCons "
                + "and t.perExamenId.ruc = :nroDoc "
                + "and t.estado.codProg = 'TP_EST_PEN' "
                + "and t.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        q.setParameter("nroCons", nroCons);
        return q.getResultList();
    }
        
    /*
    * FUNCIONES PARA ATENCION DE CITAS
     */
    public List<TurTurno> listarCitasReservadasxTipo(String filtro, Date fecIni, Date fecFin, Long sedeId, String TipoTramite) {
        String sql = "SELECT t FROM TurTurno t "
                + " where t.activo = 1 "
                + " and t.estado.codProg in ('TP_ESTUR_PEN', 'TP_ESTUR_CON', 'TP_EST_PEN', 'TP_EST_CON') "
                + " and (t.tipoTramiteId.codProg IN (" + TipoTramite + ") or t.subTramiteId.codProg IN (" + TipoTramite + ")) "
                + "";

        if (filtro != null && !filtro.isEmpty()) {
            sql = sql + " and (t.perExamenId.numDoc like :campo OR t.perExamenId.apePat like :campo OR t.perExamenId.apeMat like :campo OR t.perExamenId.nombres like :campo ";
            sql = sql + " OR t.perExamenId.ruc like :campo OR t.perExamenId.rznSocial like :campo ";
            sql = sql + " OR t.perPagoId.numDoc like :campo OR t.perPagoId.apePat like :campo OR t.perPagoId.apeMat like :campo OR t.perPagoId.nombres like :campo ";
            sql = sql + " OR t.perPagoId.ruc like :campo OR t.perPagoId.rznSocial like :campo) ";
        }
        
        if (sedeId != null && !Objects.equals(sedeId, "")) {
            sql = sql + " and t.programacionId.sedeId.id = :sedeId ";
        }
        if (fecIni != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) >= FUNC('trunc',:fecIni)) ";
        }
        if (fecFin != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) <= FUNC('trunc',:fecFin)) ";
        }

        sql = sql + " order by t.fechaTurno desc, t.id desc";
        javax.persistence.Query q = em.createQuery(sql);
        if (filtro != null && !filtro.isEmpty()) {
            q.setParameter("campo", '%' + filtro + '%');
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }
        if (fecIni != null) {
            q.setParameter("fecIni", fecIni);
        }
        if (fecFin != null) {
            q.setParameter("fecFin", fecFin);
        }
        return q.getResultList();
    }

    public List<TurTurno> listarCitasReservadasTramiteSubtramite(String filtro, Date fecIni, Date fecFin, Long sedeId, String TipoTramite, String SubTramite) {
        String sql = "SELECT t FROM TurTurno t "
                + " where t.activo = 1 "
                + " and t.estado.codProg in ('TP_ESTUR_PEN', 'TP_ESTUR_CON', 'TP_EST_PEN', 'TP_EST_CON') "
                + " and t.tipoTramiteId.codProg IN (" + TipoTramite + ") "
                + "";

        if (SubTramite != null && !SubTramite.isEmpty()) {
            sql = sql + " and t.subTramiteId.codProg IN (" + SubTramite + ")";
        }
        if (filtro != null && !filtro.isEmpty()) {
            sql = sql + " and (t.perExamenId.numDoc like :campo OR t.perExamenId.apePat like :campo OR t.perExamenId.apeMat like :campo OR t.perExamenId.nombres like :campo ";
            sql = sql + " OR t.perExamenId.ruc like :campo OR t.perExamenId.rznSocial like :campo ";
            sql = sql + " OR t.perPagoId.numDoc like :campo OR t.perPagoId.apePat like :campo OR t.perPagoId.apeMat like :campo OR t.perPagoId.nombres like :campo ";
            sql = sql + " OR t.perPagoId.ruc like :campo OR t.perPagoId.rznSocial like :campo) ";
        }
        
        if (sedeId != null && !Objects.equals(sedeId, "")) {
            sql = sql + " and t.programacionId.sedeId.id = :sedeId ";
        }
        if (fecIni != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) >= FUNC('trunc',:fecIni)) ";
        }
        if (fecFin != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) <= FUNC('trunc',:fecFin)) ";
        }

        sql = sql + " order by t.fechaTurno desc, t.id desc";
        javax.persistence.Query q = em.createQuery(sql);
        if (filtro != null && !filtro.isEmpty()) {
            q.setParameter("campo", '%' + filtro + '%');
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }
        if (fecIni != null) {
            q.setParameter("fecIni", fecIni);
        }
        if (fecFin != null) {
            q.setParameter("fecFin", fecFin);
        }
        return q.getResultList();
    }
    
    public List<TurTurno> listarCitasConfirmadas(HashMap mMap, Long sedeId) {
        String tipoTramite = mMap.get("tipoTramite").toString();
        Date fechaIni = (mMap.get("fechaIni") != null) ? (Date) mMap.get("fechaIni") : null;
        Long programacionId = (mMap.get("programacionId") != null) ? (Long) mMap.get("programacionId") : null;
        //Long sede = (mMap.get("sedeId")!=null)? (Long) mMap.get("sedeId"):null;

        String sql = "SELECT t FROM TurTurno t"
                + " where t.activo = 1 "
                + " and t.estado.codProg in ('TP_ESTUR_CON','TP_EST_CON') "
                //+ " and t.tipoTramiteId.codProg = '"+ tipo +"' ";
                + "";


        /*if (filtro != null && !filtro.isEmpty()) {
            sql = sql + " and (t.perExamenId.numDoc like :campo OR t.perExamenId.apePat like :campo OR t.perExamenId.apeMat like :campo OR t.perExamenId.nombres like :campo) ";
        }
        if (sedeId != null && !Objects.equals(sedeId, "")) {
            sql = sql + " and t.programacionId.sedeId.id = :sedeId ";
        }*/
        if (tipoTramite != null && !tipoTramite.isEmpty()) {
            sql = sql + " and t.tipoTramiteId.codProg = :tipoTramite ";
        }
        if (fechaIni != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) >= FUNC('trunc',:fechaIni)) ";
        }
        if (programacionId != null) {
            sql = sql + " and t.programacionId.id = :programacionId ";
        }
        if (sedeId != null) {
            sql = sql + " and t.tipoSedeId.id = :sedeId ";
        }

        sql = sql + " order by t.fechaTurno desc";
        javax.persistence.Query q = em.createQuery(sql);
        /*if (filtro != null && !filtro.isEmpty()) {
            q.setParameter("campo", '%' + filtro + '%');
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }*/
        if (tipoTramite != null && !tipoTramite.isEmpty()) {
            q.setParameter("tipoTramite", tipoTramite);
        }
        if (fechaIni != null) {
            q.setParameter("fechaIni", fechaIni);
        }
        if (programacionId != null) {
            q.setParameter("programacionId", programacionId);
        }
        if (sedeId != null) {
            q.setParameter("sedeId", sedeId);
        }
        return q.getResultList();
    }
    
    public List<TurTurno> listarCitasSinConfirmar(Long programacionId, Date fecha) {
        String sql = "SELECT t FROM TurTurno t"
                + " where t.activo = 1 "
                + " and t.estado.codProg in ('TP_ESTUR_PEN','TP_EST_PEN') ";

        if (fecha != null) {
            sql = sql + " and (FUNC('trunc',t.fechaTurno) = FUNC('trunc',:fecha)) ";
        }
        if (programacionId != null) {
            sql = sql + " and t.programacionId.id = :programacionId ";
        }

        sql = sql + " order by t.fechaTurno desc";
        javax.persistence.Query q = em.createQuery(sql);
        
        if (fecha != null) {
            q.setParameter("fecha", fecha);
        }
        if (programacionId != null) {
            q.setParameter("programacionId", programacionId);
        }
        return q.getResultList();
    }
    
    public List<TurTurno> listarTurnosXFechaTurno(Date d, Long programacionId, Long sedeId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM TurTurno t "
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

    public List<Map> listaConfiguraSede(Long idTipoTurno, Long idArea, Long idTipoCita) {
        Query q = em.createNativeQuery(
                "SELECT ID, LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO, MINUTOS_ANTES_INICIO, MINUTOS_TOLERANCIA, NUM_DIAS_RESERVA, EXAMEN_VIRTUAL \n"
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
    
    public List<TurTurno> buscarPersonaXCitaConfirmadaTurno(Long sedeId, String codTipo, Date fechaHoy, Long programacionId, Long personaId) { 
        //Turno de la persona de la Cita que este confirmadas en una fecha especifica y por Tipo de Turno
        String jpql = "SELECT tur FROM TurPersona p, TurProgramacion t, TurTurno tur ";
               jpql += "where p.id = tur.perExamenId.id and tur.perExamenId.id = :personaId and tur.programacionId.id = t.id and tur.activo = 1 and tur.programacionId.id = :programacionId ";
               jpql += "and t.tipoTurno.codProg = :tipo and t.sedeId.id = :sedeId ";
               jpql += "and FUNC('trunc', tur.fechaTurno) = FUNC('trunc',:fechaHoy) ";
               jpql += "and tur.estado.codProg = 'TP_EST_CON'";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("sedeId", sedeId);
        query.setParameter("tipo", codTipo == null ? "" : codTipo.toUpperCase().trim());
        query.setParameter("fechaHoy", fechaHoy);
        query.setParameter("programacionId", programacionId);
        query.setParameter("personaId", personaId);
        return query.getResultList();
    }
}
