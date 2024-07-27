/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.sistemabase.data.SbPersona;
/**
 *
 * @author rfernandezv
 */
@Stateless
public class AmaInventarioArmaFacade extends AbstractFacade<AmaInventarioArma> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaInventarioArmaFacade() {
        super(AmaInventarioArma.class);
    }

    public List<AmaInventarioArma> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaInventarioArma a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<AmaInventarioArma> obtenerArmaPorSerieRua(String nroRua, String serie) {
        Query q = em.createQuery("select a from AmaInventarioArma a where a.activo = 1 and a.serie = :serie and a.nroRua = :rua ");
        q.setParameter("rua", nroRua);
        q.setParameter("serie", serie);
        q.setHint("eclipselink.batch.type", "IN");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return null;
    }

    public List<AmaInventarioArma> listaArmasPorSolicitudRecojo(Long idSol) {
        List<AmaInventarioArma> listRes = new ArrayList<>();
        Query q = em.createQuery("select ia from AmaInventarioArma ia left join ia.amaSolicitudRecojoList sr "
                + "where  ia.activo = 1 and sr.activo = 1 and sr.id = :idSol");
        q.setParameter("idSol", idSol);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<Map> listArmasNuevasMap(Long idDirSede, Date fecIni, Date fecFin, String tipoBus, String filtro, Long idVendedor) {
        List<Map> listRes = new ArrayList<>();
        //Arma con Guía de Tránsito y Solicitud de Recojo.
        String jpql = "SELECT "
                + "TP.ID, AR.NRO_RUA, PC.ID AS IDPERSONAC, PC.NOMBRES, PC.APE_PAT, PC.APE_MAT, PC.RZN_SOCIAL, "
                + "TD.NOMBRE AS TIPODOC, PC.NUM_DOC,  PC.RUC, TP.FECHA_EMISION, ESTTP.COD_PROG COD_ESTTP, ESTTP.NOMBRE ESTADO_TP, TAR.NOMBRE AS TIPARMA, "
                + "AR.SERIE, MAR.NOMBRE AS MARCA, MOD.MODELO, AR.ID AS IDARMA, IA.ALMACEN_SUCAMEC_ID AS IDALMACEN, IA.ID AS IDINVARMA, TSOL.COD_PROG AS CODTIPOSOL "
                + "FROM BDINTEGRADO.AMA_INVENTARIO_ARMA IA  "
                + "INNER JOIN BDINTEGRADO.AMA_ARMA AR ON IA.SERIE=AR.SERIE AND IA.NRO_RUA=AR.NRO_RUA "
                + "LEFT JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON TP.ARMA_ID=AR.ID "
                + "LEFT JOIN BDINTEGRADO.SB_PERSONA PC ON TP.PERSONA_COMPRADOR_ID=PC.ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TD ON PC.TIPO_DOC = TD.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_SOLICITUD_INVENTARIO SRI ON SRI.INVENTARIO_ARMA_ID=IA.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_SOLICITUD_RECOJO SR ON SRI.SOLICITUD_RECOJO_ID=SR.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO GTI ON GTI.INVENTARIO_ARMA_ID=IA.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID=GTI.GAMAC_GUIA_TRANSITO_ID "
                + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GTSR ON GTSR.SOLICITUD_RECOJO_ID = SR.ID AND GTSR.ACTIVO=1 "
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE ESTSRGT ON GTSR.ESTADO_ID=ESTSRGT.ID "
                + " "
                + "INNER JOIN BDINTEGRADO.TIPO_BASE ESTGT ON GT.ESTADO_ID=ESTGT.ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TDEST ON TDEST.ID=GT.TIPO_DESTINO "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TINT ON TINT.ID=IA.TIPO_INTERNAMIENTO_ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TSOL ON TSOL.ID = SR.TIPO_SOLICITUD_ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC ESTTP ON TP.ESTADO_ID=ESTTP.ID "
                + "INNER JOIN BDINTEGRADO.AMA_MODELOS MOD ON AR.MODELO_ID = MOD.ID "
                + "INNER JOIN BDINTEGRADO.AMA_CATALOGO TAR ON MOD.TIPO_ARMA_ID = TAR.ID "
                + "INNER JOIN BDINTEGRADO.AMA_CATALOGO MAR ON MOD.MARCA_ID = MAR.ID "
                + "WHERE  "
                + "IA.ACTIVO = 1 AND IA.ACTUAL = 1 " //AND IA.ACTUAL = 1 "
                + "AND (ESTGT.COD_PROG = 'TP_GTGAMAC_FIN' AND TDEST.COD_PROG = 'TP_GTORDES_ALMS') "
                + "AND TINT.COD_PROG = 'TP_INTER_TEM' AND IA.CONDICION_ALMACEN = 1 "
                + "AND TP.ACTIVO = 1 AND TP.EMITIDO = 1 AND AR.ACTIVO = 1 "
                + "AND (CASE WHEN TSOL.COD_PROG = 'TP_SOLIC_RECARM' AND SR.ACTIVO = 0 THEN 1 WHEN TSOL.COD_PROG = 'TP_SOLIC_REIARM' THEN 1 WHEN GTSR.ID IS NOT NULL AND ESTSRGT.COD_PROG = 'TP_GTGAMAC_FIN' AND GTSR.FECHA_SALIDA IS NULL THEN 1 ELSE 0 END) = 1 "; 
        //Arma con Guía de Tránsito pero sin Solicitud de Recojo.
        String jpql1 = "SELECT "
                + "TP.ID, AR.NRO_RUA, PC.ID AS IDPERSONAC, PC.NOMBRES, PC.APE_PAT, PC.APE_MAT, PC.RZN_SOCIAL, "
                + "TD.NOMBRE AS TIPODOC, PC.NUM_DOC,  PC.RUC, TP.FECHA_EMISION, ESTTP.COD_PROG COD_ESTTP, ESTTP.NOMBRE ESTADO_TP, TAR.NOMBRE AS TIPARMA, "
                + "AR.SERIE, MAR.NOMBRE AS MARCA, MOD.MODELO, AR.ID AS IDARMA, IA.ALMACEN_SUCAMEC_ID AS IDALMACEN, IA.ID AS IDINVARMA, '' AS CODTIPOSOL "
                + "FROM BDINTEGRADO.AMA_INVENTARIO_ARMA IA  "
                + "INNER JOIN BDINTEGRADO.AMA_ARMA AR ON IA.SERIE=AR.SERIE AND IA.NRO_RUA=AR.NRO_RUA "
                + "LEFT JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON TP.ARMA_ID=AR.ID "
                + "LEFT JOIN BDINTEGRADO.SB_PERSONA PC ON TP.PERSONA_COMPRADOR_ID=PC.ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TD ON PC.TIPO_DOC = TD.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_SOLICITUD_INVENTARIO SRI ON SRI.INVENTARIO_ARMA_ID=IA.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_SOLICITUD_RECOJO SR ON SR.ID=SRI.SOLICITUD_RECOJO_ID "
                + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO GTI ON GTI.INVENTARIO_ARMA_ID=IA.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GTI.GAMAC_GUIA_TRANSITO_ID=GT.ID "
                + " "
                + "INNER JOIN BDINTEGRADO.TIPO_BASE ESTGT ON GT.ESTADO_ID=ESTGT.ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TDEST ON TDEST.ID=GT.TIPO_DESTINO "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TINT ON TINT.ID=IA.TIPO_INTERNAMIENTO_ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC ESTTP ON TP.ESTADO_ID=ESTTP.ID "
                + "INNER JOIN BDINTEGRADO.AMA_MODELOS MOD ON AR.MODELO_ID = MOD.ID "
                + "INNER JOIN BDINTEGRADO.AMA_CATALOGO TAR ON MOD.TIPO_ARMA_ID = TAR.ID "
                + "INNER JOIN BDINTEGRADO.AMA_CATALOGO MAR ON MOD.MARCA_ID = MAR.ID "
                + "WHERE  "
                + "IA.ACTIVO = 1 AND IA.ACTUAL = 1 " //AND IA.ACTUAL = 1 "
                + "AND (ESTGT.COD_PROG = 'TP_GTGAMAC_FIN' AND TDEST.COD_PROG = 'TP_GTORDES_ALMS') "
                + "AND TINT.COD_PROG = 'TP_INTER_TEM' AND IA.CONDICION_ALMACEN = 1 "
                + "AND TP.ACTIVO = 1 AND TP.EMITIDO = 1 AND AR.ACTIVO = 1 "
                + "AND (SR.ID IS NULL OR SR.ACTIVO = 0) ";
        //Arma sin Guía de Tránsito ni Solicitud de Recojo.
        String jpql2 = "SELECT "
                + "TP.ID, AR.NRO_RUA, PC.ID AS IDPERSONAC, PC.NOMBRES, PC.APE_PAT, PC.APE_MAT, PC.RZN_SOCIAL, "
                + "TD.NOMBRE AS TIPODOC, PC.NUM_DOC,  PC.RUC, TP.FECHA_EMISION, ESTTP.COD_PROG COD_ESTTP, ESTTP.NOMBRE ESTADO_TP, TAR.NOMBRE AS TIPARMA, "
                + "AR.SERIE, MAR.NOMBRE AS MARCA, MOD.MODELO, AR.ID AS IDARMA, IA.ALMACEN_SUCAMEC_ID AS IDALMACEN, IA.ID AS IDINVARMA, '' AS CODTIPOSOL "
                + "FROM BDINTEGRADO.AMA_INVENTARIO_ARMA IA  "
                + "INNER JOIN BDINTEGRADO.AMA_ARMA AR ON IA.SERIE=AR.SERIE AND IA.NRO_RUA=AR.NRO_RUA "
                + "LEFT JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON TP.ARMA_ID=AR.ID "
                + "LEFT JOIN BDINTEGRADO.SB_PERSONA PC ON TP.PERSONA_COMPRADOR_ID=PC.ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TD ON PC.TIPO_DOC = TD.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_SOLICITUD_INVENTARIO SRI ON SRI.INVENTARIO_ARMA_ID=IA.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_SOLICITUD_RECOJO SR ON SR.ID=SRI.SOLICITUD_RECOJO_ID "
                + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO GTI ON GTI.INVENTARIO_ARMA_ID=IA.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GTI.GAMAC_GUIA_TRANSITO_ID=GT.ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TINT ON TINT.ID=IA.TIPO_INTERNAMIENTO_ID "
                + "LEFT JOIN (SELECT SI.INVENTARIO_ARMA_ID CSID,COUNT(ID) CAN FROM "
                + "BDINTEGRADO.AMA_SOLICITUD_INVENTARIO SI INNER JOIN BDINTEGRADO.AMA_SOLICITUD_RECOJO R ON R.ID=SI.SOLICITUD_RECOJO_ID WHERE R.ACTIVO=1 GROUP BY SI.INVENTARIO_ARMA_ID ) CAN ON CAN.CSID = IA.ID "
                + "INNER JOIN BDINTEGRADO.AMA_MODELOS MOD ON AR.MODELO_ID = MOD.ID "
                + "INNER JOIN BDINTEGRADO.AMA_CATALOGO TAR ON MOD.TIPO_ARMA_ID = TAR.ID "
                + "INNER JOIN BDINTEGRADO.AMA_CATALOGO MAR ON MOD.MARCA_ID = MAR.ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC ESTTP ON TP.ESTADO_ID=ESTTP.ID "
                + "WHERE  "
                + "IA.ACTIVO = 1 " // AND IA.ACTUAL = 1
                + "AND TINT.COD_PROG = 'TP_INTER_IAN' AND IA.CONDICION_ALMACEN = 1 "
                + "AND TP.ACTIVO = 1 AND TP.EMITIDO = 1 AND AR.ACTIVO = 1 "
                + "AND (CAN.CAN = 0 OR CAN.CAN IS NULL) "
                //+ "AND (SR.ID IS NULL OR SR.ACTIVO = 0) "
                + "AND (GT.ID IS NULL OR GT.ACTIVO = 0) ";
        if (idDirSede != null) {
            jpql += "AND IA.ALMACEN_SUCAMEC_ID = ?1 ";
            jpql1 += "AND IA.ALMACEN_SUCAMEC_ID = ?1 ";
            jpql2 += "AND IA.ALMACEN_SUCAMEC_ID = ?1 ";
        }
        if (fecIni != null && fecFin != null) {
            jpql += "AND TRUNC(TP.FECHA_EMISION) BETWEEN TRUNC(?2) AND TRUNC(?3) ";
            jpql1 += "AND TRUNC(TP.FECHA_EMISION) BETWEEN TRUNC(?2) AND TRUNC(?3) ";
            jpql2 += "AND TRUNC(TP.FECHA_EMISION) BETWEEN TRUNC(?2) AND TRUNC(?3) ";
        }
        if (tipoBus != null && !"".equals(tipoBus)) {
            switch (tipoBus) {
                case "A":
                    jpql += "AND (CONCAT(PC.NOMBRES, CONCAT(' ',CONCAT(PC.APE_PAT, CONCAT(' ',PC.APE_MAT)))) LIKE ?4 OR PC.RZN_SOCIAL LIKE ?4) ";
                    jpql1 += "AND (CONCAT(PC.NOMBRES, CONCAT(' ',CONCAT(PC.APE_PAT, CONCAT(' ',PC.APE_MAT)))) LIKE ?4 OR PC.RZN_SOCIAL LIKE ?4) ";
                    jpql2 += "AND (CONCAT(PC.NOMBRES, CONCAT(' ',CONCAT(PC.APE_PAT, CONCAT(' ',PC.APE_MAT)))) LIKE ?4 OR PC.RZN_SOCIAL LIKE ?4) ";
                    break;
                case "B":
                    jpql += "AND PC.RUC LIKE ?4 ";
                    jpql1 += "AND PC.RUC LIKE ?4 ";
                    jpql2 += "AND PC.RUC LIKE ?4 ";
                    break;
                case "C":
                    jpql += "AND PC.NUM_DOC LIKE ?4 ";
                    jpql1 += "AND PC.NUM_DOC LIKE ?4 ";
                    jpql2 += "AND PC.NUM_DOC LIKE ?4 ";
                    break;
                case "D":
                    jpql += "AND AR.SERIE LIKE ?4 ";
                    jpql1 += "AND AR.SERIE LIKE ?4 ";
                    jpql2 += "AND AR.SERIE LIKE ?4 ";
                    break;
                default:
                    break;
            }
        }
        jpql += "AND TP.PERSONA_VENDEDOR_ID = ?5 ";
        jpql1 += "AND TP.PERSONA_VENDEDOR_ID = ?5 ";
        jpql2 += "AND TP.PERSONA_VENDEDOR_ID = ?5 ";
        Query q = em.createNativeQuery(jpql + " UNION " + jpql1 );//+ " UNION " + jpql2
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(5, idVendedor);
//        q.setMaxResults(MAX_RES);
        if (idDirSede != null) {
            q.setParameter(1, idDirSede);
        }
        if (fecIni != null && fecFin != null) {
            q.setParameter(2, fecIni, TemporalType.DATE);
            q.setParameter(3, fecFin, TemporalType.DATE);
        }
        if (tipoBus != null && !"".equals(tipoBus)) {
            q.setParameter(4, filtro == null ? "" : "%" + filtro.toUpperCase().trim() + "%");
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<Map> listArmasNuevasMap(Date fecIni, Date fecFin, String tipoBus, String filtro, Long idPropietario) {
        List<Map> listRes = new ArrayList<>();
        String jpql = "select distinct ia.id, ia.codigo, ia.nroRua, ia.serie, ia.propietarioId, ia.modeloId, ia.almacenSucamecId, gt.fechaEmision, gt.fechaVencimiento, gt.nroGuia, gt.direccionDestinoId, gt.id gt_id "
                + "from AmaInventarioArma ia left join ia.amaGuiaTransitoList gt "
                + "left join  ia.amaSolicitudRecojoList sr "
                + "where "
                + "ia.activo = 1 and ia.actual = 1 and ia.condicionAlmacen = 0 and ia.situacionId.codProg = 'TP_SITU_EXH' and " //and ia.actual = 1 
                + "(case "
                + "when sr.id is null "
                + "then 1 "
                + "when sr.activo = 0 "
                + "then 1 "
                + "when sr.activo = 1 and sr.fechaEmision is not null and (sr.nroExpediente is not null or sr.fechaRetiroProgramada is not null) "
                //+ "when sr.activo = 1 and sr.fechaEmision is not null and sr.nroExpediente is not null "
                + "then 1 "
                + "else 0 "
                + "end) = 1 "
                //+ "and (sr.id is null or (sr.id is not null and sr.activo = 0 and sr.fechaEmision is not null)) "
                + "and gt.activo = 1 and gt.tipoGuiaId.codProg = 'TP_GTGAMAC_EXH' and gt.estadoId.codProg = 'TP_GTGAMAC_FIN' and gt.fechaSalida is not null and gt.fechaLlegada is null ";
        if (fecIni != null && fecFin != null) {
            jpql += "and FUNC('trunc',gt.fechaEmision) between FUNC('trunc',:fecIni) and FUNC('trunc',:fecFin) ";
        }
        if (tipoBus != null && !"".equals(tipoBus)) {
            switch (tipoBus) {
                case "D":
                    jpql += "and ia.serie like :filtro ";
                    break;
                case "E":
                    jpql += "and gt.nroGuia like :filtro ";
                    break;
                default:
                    break;
            }
        }
        jpql += " and ia.propietarioId.id = :idProp order by gt.fechaEmision desc ";
        Query q = em.createQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("idProp", idPropietario);
//        q.setMaxResults(MAX_RES);
        if (fecIni != null && fecFin != null) {
            q.setParameter("fecIni", fecIni, TemporalType.DATE);
            q.setParameter("fecFin", fecFin, TemporalType.DATE);
        }
        if (tipoBus != null && !"".equals(tipoBus)) {
            q.setParameter("filtro", filtro == null ? "" : "%" + filtro.toUpperCase().trim() + "%");
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<Map> listTarjetaArmasVendidasEntrega(String tipoBus, String filtro, Long idVendedor) {
        List<Map> listRes = new ArrayList<>();
        String jpql = "select tp.id, tp.armaId.nroRua, tp.personaCompradorId.id as idPersonaC, tp.personaCompradorId.nombres, tp.personaCompradorId.apePat, tp.personaCompradorId.apeMat, tp.personaCompradorId.rznSocial, "
                + " td.nombre as tipoDoc, tp.personaCompradorId.numDoc,  tp.personaCompradorId.ruc, tp.fechaEmision, tp.armaId.modeloId.tipoArmaId.nombre as tipArma, "
                + " tp.armaId.serie, tp.armaId.calibreId.nombre as calibre, tp.armaId.modeloId.marcaId.nombre as marca, tp.armaId.modeloId.modelo, tp.armaId.id as idArma "
                + " from AmaTarjetaPropiedad tp left join tp.personaCompradorId.tipoDoc td where tp.activo=1 and tp.emitido=1 ";
        if (tipoBus != null && !"".equals(tipoBus)) {
            switch (tipoBus) {
                case "B":
                    jpql += "and (CONCAT(tp.personaCompradorId.nombres,' ',tp.personaCompradorId.apePat,' ',tp.personaCompradorId.apeMat) like :filtro or tp.personaCompradorId.rznSocial like :filtro) ";
                    break;
                case "A":
                    jpql += "and (tp.personaCompradorId.numDoc like :filtro or tp.personaCompradorId.ruc like :filtro) ";
                    break;
                case "C":
                    jpql += "and tp.armaId.serie like :filtro ";
                    break;
                default:
                    break;
            }
        }
        jpql += " and tp.personaVendedorId.id = :idVend order by tp.fechaEmision desc ";
        Query q = em.createQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("idVend", idVendedor);
        //q.setMaxResults(MAX_RES);
        if (tipoBus != null && !"".equals(tipoBus)) {
            q.setParameter("filtro", filtro == null ? "" : "%" + filtro.toUpperCase().trim() + "%");
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<AmaInventarioArma> listArmasRecojo(String tipoBus, String filtro, Long idVendedor) {
        List<AmaInventarioArma> listRes = new ArrayList<>();
        String jpql = "select ia from AmaInventarioArma ia left join ia.amaGuiaTransitoList gt , AmaArma ar "
                + "left join ar.amaTarjetaPropiedadList tp  "
                + "where "
                + "gt.activo = 1 "
                + "and gt.tipoGuiaId.codProg = 'TP_GTGAMAC_REC' "
                + "and gt.fechaSalida is not null "
                + "and ia.condicionAlmacen = 0 "
                + "and ia.tipoInternamientoId.codProg = 'TP_INTER_TEM' "
                + "and (ia.serie = ar.serie and ia.nroRua = ar.nroRua) "
                + "and tp.activo = 1 and tp.emitido = 1 and ar.activo = 1 ";
        if (tipoBus != null && !"".equals(tipoBus)) {
            switch (tipoBus) {
                case "A":
                    jpql += "and (CONCAT(tp.personaCompradorId.nombres,' ',tp.personaCompradorId.apePat,' ',tp.personaCompradorId.apeMat) like :filtro or tp.personaCompradorId.rznSocial like :filtro) ";
                    break;
                case "B":
                    jpql += "and tp.armaId.serie like :filtro ";
                    break;
                case "C":
                    jpql += "and tp.armaId.nroRua like :filtro ";
                    break;
                default:
                    break;
            }
        }
        jpql += " and tp.personaVendedorId.id = :idVend order by tp.fechaEmision desc ";
        Query q = em.createQuery(jpql);
        q.setParameter("idVend", idVendedor);
        if (tipoBus != null && !"".equals(tipoBus)) {
            q.setParameter("filtro", filtro == null ? "" : "%" + filtro.toUpperCase().trim() + "%");
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<Map> listCantidadArmasRecojoMap(Long idVendedor) {
        List<Map> listRes = new ArrayList<>();
        String jpql = "SELECT ESTADO,COUNT(*) CANTIDAD FROM ( "
                + "SELECT "
                + "CASE WHEN SIT.COD_PROG = 'TP_SITU_POS' THEN 'ENTREGADO' ELSE (CASE WHEN TRUNC(BDINTEGRADO.FNC_FECHA_CALCULADA(GT.FECHA_EMISION ,3)) >= TRUNC(CURRENT_DATE) THEN 'POR ENTREGAR' ELSE 'VENCIDO' END) END ESTADO "
                + "FROM "
                + "BDINTEGRADO.AMA_INVENTARIO_ARMA IA "
                + "inner JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO GTI ON GTI.INVENTARIO_ARMA_ID=IA.ID "
                + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID=GTI.GAMAC_GUIA_TRANSITO_ID "
                + "LEFT JOIN BDINTEGRADO.AMA_ARMA AR ON AR.SERIE=IA.SERIE AND AR.NRO_RUA=IA.NRO_RUA "
                + "LEFT JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON TP.ARMA_ID=AR.ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG ON GT.TIPO_GUIA_ID=TG.ID "
                + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TI ON IA.TIPO_INTERNAMIENTO_ID=TI.ID "
                + "INNER JOIN BDINTEGRADO.TIPO_GAMAC SIT ON AR.SITUACION_ID = SIT.ID "
                + "WHERE GT.ACTIVO=1 "
                + "AND TG.COD_PROG = 'TP_GTGAMAC_REC' "
                + "AND GT.FECHA_SALIDA IS NOT NULL "
                + "AND IA.CONDICION_ALMACEN = 0 AND IA.ACTUAL=1 "
                + "AND (SIT.COD_PROG = 'TP_SITU_POS' OR SIT.COD_PROG = 'TP_SITU_POS_LC') "
                + "AND TI.COD_PROG = 'TP_INTER_TEM' "
                + "AND TP.ACTIVO = 1 AND TP.EMITIDO = 1 AND AR.ACTIVO = 1 "
                + "AND TP.PERSONA_VENDEDOR_ID = ?2 "
                + ") D "
                + "GROUP BY D.ESTADO ";
        Query q = em.createNativeQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(2, idVendedor);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    /**
     * Función de búsqueda de armas por entregar, entregado y/o con fecha de
     * entrega vencida
     *
     * @param estadoBus
     * @param tipoBus
     * @param filtro
     * @param idVendedor
     * @return
     */
    public List<Map> listArmasRecojoMap(String estadoBus, String tipoBus, String filtro, Long idVendedor) {
        List<Map> listRes = new ArrayList<>();
        String jpql = "SELECT * FROM (SELECT DATO.*, "
                + "CASE WHEN DATO.COD_PROG = 'TP_SITU_POS' THEN 'ENTREGADO' ELSE (CASE WHEN TRUNC(FECHA_LIMITE) >= TRUNC(SYSDATE) THEN 'POR ENTREGAR' ELSE 'VENCIDO' END) END ESTADO "
                + "FROM ( "
                + "SELECT IA.ID,  "
                + " DECODE(PC.RZN_SOCIAL, NULL,PC.NOMBRES||' '||PC.APE_PAT||' '||PC.APE_MAT, PC.RZN_SOCIAL) PROPIETARIO  "
                + " , PC.ID IDPROP  "
                + " , AR.NRO_RUA "
                + " , AR.SERIE  "
                + " ,CATI.* "
                + " ,TP.FECHA_ENTREGA_PROPIETARIO FECHA_ENTREGA  "
                + " ,TP.PERSONA_VENDEDOR_ID "
                + " ,BDINTEGRADO.FNC_FECHA_CALCULADA(GT.FECHA_SALIDA ,3) FECHA_LIMITE "
                + " ,SIT.COD_PROG "
                + " FROM  "
                + " BDINTEGRADO.AMA_INVENTARIO_ARMA IA  "
                + " LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO GTI ON GTI.INVENTARIO_ARMA_ID=IA.ID  "
                + " LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID=GTI.GAMAC_GUIA_TRANSITO_ID  "
                + " LEFT JOIN BDINTEGRADO.AMA_ARMA AR ON AR.SERIE=IA.SERIE AND AR.NRO_RUA=IA.NRO_RUA  "
                + " LEFT JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON TP.ARMA_ID=AR.ID  "
                + " LEFT JOIN BDINTEGRADO.SB_PERSONA PC ON TP.PERSONA_COMPRADOR_ID = PC.ID  "
                + " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG ON GT.TIPO_GUIA_ID=TG.ID  "
                + " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TI ON IA.TIPO_INTERNAMIENTO_ID=TI.ID  "
                + " INNER JOIN BDINTEGRADO.TIPO_GAMAC SIT ON AR.SITUACION_ID = SIT.ID  "
                + " LEFT JOIN ( "
                + "    SELECT M.ID MODEL_ID,TA.NOMBRE TIPO_ARMA ,MA.NOMBRE MARCA,M.MODELO,LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE  "
                + "    FROM BDINTEGRADO.AMA_MODELOS M INNER JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID "
                + "    INNER JOIN BDINTEGRADO.AMA_CATALOGO TAT ON TAT.ID = M.TIPO_ARTICULO_ID INNER JOIN BDINTEGRADO.AMA_CATALOGO TA ON TA.ID = M.TIPO_ARMA_ID "
                + "    INNER JOIN BDINTEGRADO.AMA_CATALOGO MA ON MA.ID = M.MARCA_ID INNER JOIN BDINTEGRADO.AMA_CATALOGO CA ON CA.ID = MC.CATALOGO_ID "
                + "    GROUP BY M.ID,TAT.NOMBRE,TA.NOMBRE,MA.NOMBRE,M.MODELO   "
                + " ) CATI ON CATI.MODEL_ID = AR.MODELO_ID "
                + " WHERE GT.ACTIVO=1  "
                + " AND TG.COD_PROG = 'TP_GTGAMAC_REC'  "
                + " AND GT.FECHA_SALIDA IS NOT NULL  "
                + " AND IA.CONDICION_ALMACEN = 0  AND IA.ACTUAL=1  "
                + " AND TI.COD_PROG = 'TP_INTER_TEM'  "
                + " AND TP.ACTIVO = 1 AND TP.EMITIDO = 1 AND AR.ACTIVO = 1  "
                + " AND TP.PERSONA_VENDEDOR_ID = ?2 ";
        if (estadoBus != null && !"".equals(estadoBus)) {
            if ("ENTREGADO".equals(estadoBus)) {
                jpql += "AND SIT.COD_PROG = 'TP_SITU_POS' ";
            } else if ("VENCIDO".equals(estadoBus) || "POR ENTREGAR".equals(estadoBus)) {
                jpql += "AND SIT.COD_PROG = 'TP_SITU_POS_LC' ";
            }
        } else {
            jpql += "AND (SIT.COD_PROG = 'TP_SITU_POS' OR SIT.COD_PROG = 'TP_SITU_POS_LC') ";
        }
        if (tipoBus != null && !"".equals(tipoBus)) {
            switch (tipoBus) {
                case "A":
                    jpql += "AND (CONCAT(PC.NOMBRES, CONCAT(' ',CONCAT(PC.APE_PAT, CONCAT(' ',PC.APE_MAT)))) LIKE ?1 OR PC.RZN_SOCIAL LIKE ?1) ";
                    break;
                case "B":
                    jpql += "AND AR.SERIE LIKE ?1 ";
                    break;
                case "C":
                    jpql += "AND AR.NRO_RUA LIKE ?1 ";
                    break;
                default:
                    break;
            }
        }
        jpql += " ORDER BY FECHA_LIMITE DESC) DATO) ";
        if (estadoBus != null && !"".equals(estadoBus)) {
            jpql += "WHERE ESTADO = ?3 ";
        }

        jpql += "ORDER BY FECHA_LIMITE DESC ";
        Query q = em.createNativeQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(2, idVendedor);
        if (tipoBus != null && !"".equals(tipoBus)) {
            q.setParameter(1, filtro == null ? "" : "%" + filtro.toUpperCase().trim() + "%");
        }
        if (estadoBus != null && !"".equals(estadoBus)) {
            q.setParameter(3, estadoBus);
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public AmaInventarioArma obtenerArmasPorSerieRua(String rua, String serie) {
        String plsql = "select a from AmaInventarioArma a where a.activo = 1 and a.serie = :serie and a.nroRua = :rua "
                + " order by a.id desc ";
        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("serie", serie.trim());
        q.setParameter("rua", rua);

        if (q.getResultList().isEmpty()) {
            return null;
        }

        return (AmaInventarioArma) q.getResultList().get(0);
    }

    public AmaInventarioArma obtenerUltimaArmaSerie(String serie) {
        Query q = em.createQuery("select a from AmaInventarioArma a where a.activo = 1 and a.serie = :serie order by a.id desc");
        q.setParameter("serie", serie == null ? null : serie.trim().toUpperCase());
        q.setHint("eclipselink.batch.type", "IN");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaInventarioArma) q.getResultList().get(0);
        }
        return null;
    }
    
    public Map buscarDatosInternamientoSolicitud(Long inventarioId, Long guiaTransitoId){
        try {
            String jpql = "SELECT GT.NRO_GUIA AS NRO_ACTA, GT.FECHA_EMISION AS FECHA_ACTA, \n" +
                            "  AI.SERIE AS NRO_SERIE, TA.NOMBRE AS TIPO_ARMA, \n" +
                            "  MA.NOMBRE AS MARCA, M.MODELO, LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, \n" +
                            "  ALM.DIRECCION, ALM.DISTRITO_ID, DEP.NOMBRE||'/'||PRO.NOMBRE||'/'||DIS.NOMBRE AS UBIGEO, \n" +
                            "  TI.NOMBRE AS TIPO_INTERNAMIENTO, TD.NOMBRE AS TIPO_DEPOSITO \n" +
                            " FROM BDINTEGRADO.AMA_INVENTARIO_ARMA AI \n" +
                            " INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO AGTI ON AGTI.INVENTARIO_ARMA_ID = AI.ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID = AGTI.GAMAC_GUIA_TRANSITO_ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_MODELOS M ON M.ID = AI.MODELO_ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC TI ON TI.ID = AI.TIPO_INTERNAMIENTO_ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC TD ON TD.ID = GT.TIPO_GUIA_ID \n " + 
                            " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID  \n" +
                            " INNER JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID \n" +
                            " INNER JOIN BDINTEGRADO.SB_DIRECCION ALM ON ALM.ID = AI.ALMACEN_SUCAMEC_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_DISTRITO DIS ON DIS.ID = ALM.DISTRITO_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_PROVINCIA PRO ON PRO.ID = DIS.PROVINCIA_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_DEPARTAMENTO DEP ON DEP.ID = PRO.DEPARTAMENTO_ID \n" +
                            "  WHERE AI.ACTIVO = 1 AND AI.ID = ?1 AND GT.ID = ?2 \n" +
                            " GROUP BY GT.NRO_GUIA, GT.FECHA_EMISION, AI.SERIE, TA.NOMBRE, MA.NOMBRE, M.MODELO, ALM.DIRECCION, \n" +
                            " ALM.DISTRITO_ID, DEP.NOMBRE, PRO.NOMBRE, DIS.NOMBRE, TI.NOMBRE, TD.NOMBRE ";
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, inventarioId);
            q.setParameter(2, guiaTransitoId);
            
            if(q.getResultList().isEmpty()){
                return null;
            }else{
                return (Map) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public Map buscarDatosInternamientoRaessXLicencia(Long nroLicencia, String idsTipadosGT){
        try {
            String jpql = "SELECT AI.ID AS INVENTARIO_ID, GT.NRO_GUIA AS NRO_ACTA, GT.FECHA_EMISION AS FECHA_ACTA, \n" +
                            "  AI.SERIE AS NRO_SERIE, TA.NOMBRE AS TIPO_ARMA, AI.MODELO_ID ID_MODELO, \n" +
                            "  MA.NOMBRE AS MARCA, M.MODELO, LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, \n" +
                            "  ALM.DIRECCION, ALM.DISTRITO_ID, DEP.NOMBRE||'/'||PRO.NOMBRE||'/'||DIS.NOMBRE AS UBIGEO, \n" +
                            "  TI.NOMBRE AS TIPO_INTERNAMIENTO, TD.NOMBRE AS TIPO_DEPOSITO, TD.COD_PROG AS TIPO_DEPOSITO_CODPROG, \n" +
                            "  EF.COD_PROG AS ESTADOFUNC_CODPROG, EF.NOMBRE AS ESTADO_FUNCIONAL " +
                            " FROM BDINTEGRADO.AMA_INVENTARIO_ARMA AI \n" +
                            " INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO AGTI ON AGTI.INVENTARIO_ARMA_ID = AI.ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID = AGTI.GAMAC_GUIA_TRANSITO_ID AND GT.TIPO_GUIA_ID IN ("+ idsTipadosGT +") AND GT.ACTIVO = 1 AND GT.ESTADO_ID=392 AND GT.DEVOLUCION_ID IS NULL" +
                            " INNER JOIN BDINTEGRADO.AMA_MODELOS M ON M.ID = AI.MODELO_ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC TI ON TI.ID = AI.TIPO_INTERNAMIENTO_ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC TD ON TD.ID = GT.TIPO_GUIA_ID \n " + 
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC EF ON EF.ID = AI.ESTADOFUNCIONAL_ID \n " + 
                            " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID  \n" +
                            " INNER JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID \n" +
                            " INNER JOIN BDINTEGRADO.SB_DIRECCION ALM ON ALM.ID = AI.ALMACEN_SUCAMEC_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_DISTRITO DIS ON DIS.ID = ALM.DISTRITO_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_PROVINCIA PRO ON PRO.ID = DIS.PROVINCIA_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_DEPARTAMENTO DEP ON DEP.ID = PRO.DEPARTAMENTO_ID \n" +
                            "  WHERE AI.ACTIVO = 1 AND AI.LICENCIA_DISCA_ID = ?1 \n" +
                            " GROUP BY AI.ID, GT.NRO_GUIA, GT.FECHA_EMISION, AI.SERIE, TA.NOMBRE, MA.NOMBRE, M.MODELO, ALM.DIRECCION, \n" +
                            " ALM.DISTRITO_ID, DEP.NOMBRE, PRO.NOMBRE, DIS.NOMBRE, TI.NOMBRE, TD.NOMBRE, TD.COD_PROG ,EF.COD_PROG ,EF.NOMBRE,AI.MODELO_ID ";
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, nroLicencia);
            
            if(q.getResultList().isEmpty()){
                return null;
            }else{
                return (Map) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public Map buscarDatosInternamientoSimplificadoSolicitud(Long inventarioId, Long guiaTransitoId){
        try {
            String jpql = "SELECT AI.SERIE AS NRO_SERIE, TA.NOMBRE AS TIPO_ARMA, \n" +
                            "  MA.NOMBRE AS MARCA, M.MODELO, LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE \n" +                            
                            " FROM BDINTEGRADO.AMA_INVENTARIO_ARMA AI \n" +
                            " INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO AGTI ON AGTI.INVENTARIO_ARMA_ID = AI.ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID = AGTI.GAMAC_GUIA_TRANSITO_ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_MODELOS M ON M.ID = AI.MODELO_ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC TI ON TI.ID = AI.TIPO_INTERNAMIENTO_ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC TD ON TD.ID = GT.TIPO_GUIA_ID \n " + 
                            " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID  \n" +
                            " INNER JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID \n" +
                            "  WHERE AI.ACTIVO = 1 AND AI.ID = ?1 AND GT.ID = ?2 \n" +
                            " GROUP BY AI.SERIE, TA.NOMBRE, MA.NOMBRE, M.MODELO \n";
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, inventarioId);
            q.setParameter(2, guiaTransitoId);
            
            if(q.getResultList().isEmpty()){
                return null;
            }else{
                return (Map) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public Map buscarDatosInternamientoRaessXSerieXModelo(String nroSerie, Long modeloId, String idsTipadosGT){
        try {
            String jpql = "SELECT AI.ID AS INVENTARIO_ID, GT.NRO_GUIA AS NRO_ACTA, GT.FECHA_EMISION AS FECHA_ACTA, \n" +
                            "  AI.SERIE AS NRO_SERIE, TA.NOMBRE AS TIPO_ARMA, AI.MODELO_ID ID_MODELO, \n" +
                            "  MA.NOMBRE AS MARCA, M.MODELO, LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, \n" +
                            "  ALM.DIRECCION, ALM.DISTRITO_ID, DEP.NOMBRE||'/'||PRO.NOMBRE||'/'||DIS.NOMBRE AS UBIGEO, \n" +
                            "  TI.NOMBRE AS TIPO_INTERNAMIENTO, TD.NOMBRE AS TIPO_DEPOSITO, TD.COD_PROG AS TIPO_DEPOSITO_CODPROG, \n" +
                            "  EF.COD_PROG AS ESTADOFUNC_CODPROG, EF.NOMBRE AS ESTADO_FUNCIONAL " +
                            " FROM BDINTEGRADO.AMA_INVENTARIO_ARMA AI \n" +
                            " INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO AGTI ON AGTI.INVENTARIO_ARMA_ID = AI.ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID = AGTI.GAMAC_GUIA_TRANSITO_ID AND GT.TIPO_GUIA_ID IN ("+ idsTipadosGT +") AND GT.ACTIVO = 1 AND GT.ESTADO_ID=392 AND GT.DEVOLUCION_ID IS NULL" +
                            " INNER JOIN BDINTEGRADO.AMA_MODELOS M ON M.ID = AI.MODELO_ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC TI ON TI.ID = AI.TIPO_INTERNAMIENTO_ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC TD ON TD.ID = GT.TIPO_GUIA_ID \n " + 
                            " INNER JOIN BDINTEGRADO.TIPO_GAMAC EF ON EF.ID = AI.ESTADOFUNCIONAL_ID \n " + 
                            " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID  \n" +
                            " INNER JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID \n" +
                            " INNER JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID \n" +
                            " INNER JOIN BDINTEGRADO.SB_DIRECCION ALM ON ALM.ID = AI.ALMACEN_SUCAMEC_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_DISTRITO DIS ON DIS.ID = ALM.DISTRITO_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_PROVINCIA PRO ON PRO.ID = DIS.PROVINCIA_ID \n" +
                            " LEFT JOIN BDINTEGRADO.SB_DEPARTAMENTO DEP ON DEP.ID = PRO.DEPARTAMENTO_ID \n" +
                            "  WHERE AI.ACTIVO = 1 AND AI.ACTUAL=1 AND AI.SERIE = ?1 AND AI.MODELO_ID = ?2 \n" +
                            " GROUP BY AI.ID, GT.NRO_GUIA, GT.FECHA_EMISION, AI.SERIE, TA.NOMBRE, MA.NOMBRE, M.MODELO, ALM.DIRECCION, \n" +
                            " ALM.DISTRITO_ID, DEP.NOMBRE, PRO.NOMBRE, DIS.NOMBRE, TI.NOMBRE, TD.NOMBRE, TD.COD_PROG ,EF.COD_PROG ,EF.NOMBRE,AI.MODELO_ID ";
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, nroSerie);
            q.setParameter(2, modeloId);
            
            if(q.getResultList().isEmpty()){
                return null;
            }else{
                return (Map) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public int buscarModeloInventario(Long modeloId, String serie) {
        int cont = 0;
        String plsql = "select count(a) from AmaInventarioArma a where a.activo = 1 and a.actual = 1 and a.modeloId.id = :modeloId";
        if (!serie.trim().isEmpty()) {
            plsql = plsql + " and a.serie = :serie ";
        }

        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("modeloId", modeloId);
        if (!serie.trim().isEmpty()) {
            q.setParameter("serie", serie.trim());
        }

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }

        return cont;
    }
    
    public AmaInventarioArma obtenerInvArmaPorModeloSerie(Long modeloId, String serie) {
        AmaInventarioArma res = null;
        String plsql = "select a from AmaInventarioArma a where a.activo = 1 and a.actual = 1 and a.modeloId.id = :modeloId";
        if (!serie.trim().isEmpty()) {
            plsql = plsql + " and a.serie = :serie ";
        }
        plsql = plsql + " order by a.id desc ";
        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("modeloId", modeloId);
        if (!serie.trim().isEmpty()) {
            q.setParameter("serie", serie.trim());
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = (AmaInventarioArma) q.getResultList().get(0);
        }
        return res;
    }

    public AmaInventarioArma obtenerArmasPorSeriePropietario(String serie, Long propietarioId) {
        String plsql = "select a from AmaInventarioArma a where a.activo = 1 and a.actual = 1 and a.serie = :serie and a.propietarioId.id = :propietarioId "
                + " order by a.id desc ";
        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("serie", serie.trim());
        q.setParameter("propietarioId", propietarioId);

        if (q.getResultList().isEmpty()) {
            return null;
        }

        return (AmaInventarioArma) q.getResultList().get(0);
    }

    public List<Map> listarArmasXCriterios(String filtro, Long idCriterio, SbPersona perPropietario, Boolean tienePropietario, short condicion) {
        StringBuilder sbQuery = new StringBuilder();
        List<Map> listRes = new ArrayList<>();
        try {
            if (perPropietario != null) {
                sbQuery.append("select a.id, a.nroRua, a.modeloId.tipoArmaId.nombre as tipoArma, a.modeloId.marcaId.nombre as marca, a.modeloId.modelo, "
                        + "a.serie, a.codigo, a.propietarioId.id as propietarioId, a.modeloId.id as modeloId, a.almacenSucamecId.id as IDDIRSUCAMEC, a.condicionAlmacen as ENALMACEN "
                        + "from AmaInventarioArma a where a.activo = 1 and a.actual = 1 ");
                sbQuery.append("and a.propietarioId.id = :idVend ");
            } else {
                sbQuery.append("select a.id, a.nroRua, a.modeloId.tipoArmaId.nombre as tipoArma, a.modeloId.marcaId.nombre as marca, a.modeloId.modelo, a.serie, a.codigo, a.modeloId.id as modeloId, a.almacenSucamecId.id as IDDIRSUCAMEC, a.condicionAlmacen as ENALMACEN "
                        + "from AmaInventarioArma a where a.activo = 1 and a.actual = 1 ");
            }
            if (!Objects.equals((short) -1, condicion)) {
                sbQuery.append("and a.condicionAlmacen = :cond ");
            }
            if (idCriterio == 1L) {
                sbQuery.append("and trim(a.serie) = :filtro ");
            }
            if (idCriterio == 2L) {
                sbQuery.append("and trim(a.nroRua) = :filtro ");
            }
            if (idCriterio == 3L) {
                sbQuery.append("and (trim(a.serie) = :filtro or trim(a.nroRua) = :filtro) ");
            }

            Query q = em.createQuery(sbQuery.toString());
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("filtro", filtro);
            if (!Objects.equals((short) -1, condicion)) {
                q.setParameter("cond", condicion);
            }
            if (perPropietario != null) {
                q.setParameter("idVend", perPropietario.getId());
            }
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listRes;
    }

    public List<Map> listArmasImportadasNuevasConTP(String filtro, Long idCriterio, Long idVendedor, Long idComprador, Long idAlmacenSUC) {
        List<Map> listRes = new ArrayList<>();
        try {
            String jpql = "SELECT IA.id, IA.nro_Rua, \n" +
                    " TIPAR.NOMBRE as tipoArma, MA.NOMBRE as marca, MO.modelo as modelo, IA.serie, IA.codigo, \n" +
                    " IA.PROPIETARIO_ID as propietarioId, IA.MODELO_ID as modeloId, IA.ALMACEN_SUCAMEC_ID as idDirSucamec, \n" +
                    " IA.CONDICION_ALMACEN as enAlmacen from BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP \n" +
                    " INNER JOIN BDINTEGRADO.AMA_ARMA AR ON (TP.ARMA_ID = AR.ID) \n" +
                    " INNER JOIN BDINTEGRADO.AMA_INVENTARIO_ARMA IA ON (AR.ID = IA.ARMA_ID AND AR.SERIE = IA.SERIE AND AR.NRO_RUA = IA.NRO_RUA AND AR.MODELO_ID = IA.MODELO_ID ) \n" +
                    " INNER JOIN BDINTEGRADO.AMA_MODELOS MO ON (IA.MODELO_ID = MO.ID) \n" +
                    " INNER JOIN BDINTEGRADO.AMA_CATALOGO MA ON (MO.MARCA_ID = MA.ID) \n" +
                    " INNER JOIN BDINTEGRADO.AMA_CATALOGO TIPAR ON (MO.TIPO_ARMA_ID = TIPAR.ID) \n" +
                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TGA ON (IA.TIPO_INTERNAMIENTO_ID = TGA.ID) \n" +
                    " WHERE TP.ACTIVO = 1 AND AR.ACTIVO = 1 and IA.ACTIVO = 1 \n" +
                    " AND TP.EMITIDO = 1 AND IA.CONDICION_ALMACEN = 1 \n" +
                    " AND (TGA.COD_PROG = 'TP_INTER_TEM' OR (TGA.COD_PROG = 'TP_INTER_INM' AND TP.RG_COMPRADOR is not null)) ";

            if (idAlmacenSUC != null) {
                jpql += " AND IA.ALMACEN_SUCAMEC_ID = ?1 "; //idAlmacenSUC
            }
            if (idVendedor != null) {
                jpql += " AND TP.PERSONA_VENDEDOR_ID = ?2 ";
            }
            if (idComprador != null) {
                jpql += " AND (TP.PERSONA_COMPRADOR_ID = ?3 AND IA.PROPIETARIO_ID = ?3 ) ";
            }
            if (idCriterio == 1L) {
                jpql += " AND IA.serie = ?4 "; //filtro
            }
            if (idCriterio == 2L) {
                jpql += " AND IA.nro_Rua = ?4 "; //filtro
            }
            if (idCriterio == 3L) {
                jpql += " AND (IA.serie = ?4 OR IA.nro_Rua = ?4 ) "; //filtro
            }
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            if (idAlmacenSUC != null) {
                q.setParameter(1, idAlmacenSUC);
                System.out.println("1>>>> " + idAlmacenSUC);
            }
            if (idVendedor != null) {
                q.setParameter(2, idVendedor);
                System.out.println("2>>>> " + idVendedor);
            }
            if (idComprador != null) {
                q.setParameter(3, idComprador);
                System.out.println("3>>>> " + idComprador);
            }
            if (filtro != null) {
                q.setParameter(4, filtro.trim().toUpperCase());
                System.out.println("4>>>> " + filtro.trim().toUpperCase());
            }
            System.out.println("qry>>>> " + jpql);
            listRes = q.getResultList();
            if (listRes == null && listRes.isEmpty()) {
                listRes = null;
            }
        } catch (Exception e) {
            listRes = null;
            e.printStackTrace();
        }
        return listRes;
    }

    public boolean buscarExistenciaNroRua(String nroRUA, Boolean buscarEnArma) {
        int cont = 0;
        // BUSCANDO NRO. RUA EN TABLA DE INVENTARIO
        javax.persistence.Query q = em.createQuery("select count(a) from AmaInventarioArma a where a.activo = 1 and a.actual = 1 and a.nroRua = :nroRUA ");
        q.setParameter("nroRUA", nroRUA.trim());

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        if (cont > 0) {
            return true;
        }
        if (buscarEnArma) {
            // BUSCANDO NRO. RUA EN TABLA DE ARMA
            cont = 0;
            q = em.createQuery("select count(a) from AmaArma a where a.activo = 1 and a.nroRua = :nroRUA ");
            q.setParameter("nroRUA", nroRUA.trim());
            results = q.getResultList();
            for (Long _values : results) {
                cont = _values.intValue();
                break;
            }
            if (cont > 0) {
                return true;
            }
        }

        return false;
    }

    public List<AmaInventarioArma> obtenerArmasxCriterios(Boolean like, String serie, Map datos) {
        List<AmaInventarioArma> res = null;
        String jpql = "select a from AmaInventarioArma a where a.activo = 1 and a.actual = 1 and a.serie like :serie ";
        if (datos != null) {
            if (datos.get("condicion") != null) {
                jpql += "and a.condicionAlmacen = :condi ";
            }
            if (datos.get("codSituacion") != null) {
                jpql += "and a.situacionId.codProg = :situacion ";
            }
            if (datos.get("codTipoInter") != null) {
                jpql += "and a.tipoInternamientoId.codProg = :tipoInter ";
            }
            if (datos.get("propietario") != null) {
                jpql += "and a.propietarioId = :propi ";
            }
        }
        javax.persistence.Query q = em.createQuery(jpql);
        q.setParameter("serie", like ? "%" + serie.trim() + "%" : serie.trim());

        if (datos != null) {
            if (datos.get("condicion") != null) {
                q.setParameter("condi", datos.get("condicion"));
            }
            if (datos.get("codSituacion") != null) {
                q.setParameter("situacion", datos.get("codSituacion"));
            }
            if (datos.get("codTipoInter") != null) {
                q.setParameter("tipoInter", datos.get("codTipoInter"));
            }
            if (datos.get("propietario") != null) {
                q.setParameter("propi", datos.get("propietario"));
            }
        }

        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = q.getResultList();
        }
        return res;
    }

    public List<AmaInventarioArma> listInvArmaPorLicenciaSerie(Long licencia, String serie) {
        List<AmaInventarioArma> listRes = null;
        String plsql = "select a from AmaInventarioArma a where a.activo = 1 and a.actual = 1 and a.licenciaDiscaId = :licencia and a.serie = :serie ";
        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("licencia", licencia);
        q.setParameter("serie", serie.trim());
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return listRes;
    }

    public List<AmaInventarioArma> obtenerArmasSinActual(Long modeloId, String serie) {
        String plsql = "select a from AmaInventarioArma a where a.activo = 1 and a.modeloId.id = :modeloId  and a.serie = :serie"
                + " order by a.id desc ";
        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("modeloId", modeloId);
        q.setParameter("serie", serie.trim());

        return (List<AmaInventarioArma>) q.getResultList();
    }
    
}
