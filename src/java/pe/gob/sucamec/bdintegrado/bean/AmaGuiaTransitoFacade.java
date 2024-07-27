/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.sistemabase.data.SbTipo;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaGuiaTransitoFacade extends AbstractFacade<AmaGuiaTransito> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @EJB
    private AmaMaestroArmasFacade ejbAmaMaestroArmasFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaGuiaTransitoFacade() {
        super(AmaGuiaTransito.class);
    }

    public List<AmaGuiaTransito> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaGuiaTransito a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public Date obtenerFechaEmision(Long idArma) {
        Date fecha = null;
        Query q = em.createQuery("select gt.fechaEmision from AmaGuiaTransito gt LEFT JOIN gt.amaArmaList ar "
                + "where  ar.id = :idar ");
        q.setParameter("idar", idArma);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            fecha = (Date) q.getResultList().get(0);
        }
        return fecha;
    }

    public List<Long> listaIdsEncontrados(Long idPersona) {
        List<Long> listRes = new ArrayList<>();
        Query q = em.createQuery("select distinct aa.id from AmaGuiaTransito gt LEFT JOIN gt.amaArmaList aa "
                + "where  aa.id in (select tp1.armaId.id from AmaTarjetaPropiedad tp1 where tp1.personaVendedorId.id = :idPerUsu and tp1.activo = 1) and gt.activo=1 ");
        q.setParameter("idPerUsu", idPersona);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public int diasLaborables(Date fecIni, Date fecFin) {
        try {
            int cant = 0;
            Query q = em.createNativeQuery("SELECT BDINTEGRADO.DIAS_LABORABLES(?1,?2) from dual");
            q.setParameter(1, fecIni, TemporalType.DATE);
            q.setParameter(2, fecFin, TemporalType.DATE);
            cant = ((BigDecimal) q.getSingleResult()).intValue();
            return cant;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public AmaGuiaTransito guiaPorIdSolRecojo(Long idSol) {
        Query q = em.createQuery("select a from AmaGuiaTransito a where a.activo = 1 and a.solicitudRecojoId.id = :sol ");
        q.setParameter("sol", idSol);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaGuiaTransito) q.getResultList().get(0);
        }
        return null;
    }
    
    public List<AmaGuiaTransito> selectxLicDisca(Long licDisca) {
        String sql = "select a from AmaGuiaTransito a "
                + " inner join a.amaInventarioArmaList inv"
                + " where inv.licenciaDiscaId = :licDisca"
                + " order by a.fechaEmision desc"
                + "";
        
        Query q = em.createQuery(sql);
        q.setParameter("licDisca", licDisca);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaGuiaTransito> selectxInternadoxLicDisca(Long licDisca) {
        String sql = "select a from AmaGuiaTransito a "
                + " inner join a.amaInventarioArmaList inv"
                + " where inv.licenciaDiscaId = :licDisca and inv.situacionId.codProg='TP_SITU_INT' and a.activo=1"
                + "";
        
        Query q = em.createQuery(sql);
        q.setParameter("licDisca", licDisca);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<AmaGuiaTransito> selectxInternadoxSerieRua(String serie, String rua) {
        String sql = "select a from AmaGuiaTransito a "
                + " inner join a.amaInventarioArmaList inv"
                + " where (inv.serie=:serie and inv.nroRua=:rua) and inv.situacionId.codProg='TP_SITU_INT' and a.activo=1"
                + "";
        
        Query q = em.createQuery(sql);
        q.setParameter("serie", serie);
        q.setParameter("rua", rua);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public AmaGuiaTransito buscarGuiaTransitoById(Long gtId) {
        String plsql = "select a from AmaGuiaTransito a where a.id = :gtId  ";
        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("gtId", gtId);
        
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaGuiaTransito) q.getResultList().get(0);
        }
        return null;
    }

    /**
     * @param idGuia
     * @return
     */
    public List<Map> obtenerListGuiaCreada(Long idGuia) {
        List<Map> listRes = new ArrayList<>();
        try {
            String jpql = "select distinct gt.id, gt.nro_expediente as nroExpediente, gt.fecha_emision as fechEmi, gt.fecha_vencimiento as fechVen, tb.nombre as estadoGuia, "
                    + "pe.rzn_social as rznSocial, pe.nombres, pe.ape_pat as apPat, pe.ape_mat as apMat, "
                    + "tb1.nombre as tipodoc, pe.ruc, pe.num_doc as numDoc, gt.nro_guia as nroGuia, tg2.cod_prog as codModo, dirSuc.area_id as area, "
                    //Ultimos datos
                    + "dirSuc.referencia as almacen, tg.nombre as tipoGuia, tg.cod_prog as codTg, ia.serie, ia.nro_rua as nroRua, tipoAr.nombre as tipoArma, modd.id as modeloId, marca.nombre as marca, usu.login "
                    //Fin
                    + "from bdintegrado.ama_guia_transito gt "
                    + "left join bdintegrado.sb_usuario usu on (gt.usuario_id = usu.id) "
                    + "left join bdintegrado.ama_guia_municiones mun on (gt.id=mun.guia_transito_id) "
                    + "left join bdintegrado.ama_guia_transito_inventario inv on (gt.id=inv.gamac_guia_transito_id) "
                    + "left join bdintegrado.ama_inventario_arma ia on (inv.inventario_arma_id=ia.id) left join bdintegrado.ama_modelos modd on (ia.modelo_id=modd.id) "
                    + "left join bdintegrado.ama_inventario_accesorios acc on (acc.guia_transito_id = gt.id) "
                    //Ultimas relaciones para obtener los datos.
                    + "left join bdintegrado.ama_catalogo tipoAr on (tipoAr.id = modd.tipo_arma_id) "
                    + "left join bdintegrado.sb_direccion dirSuc on (ia.almacen_sucamec_id = dirSuc.id) "
                    //
                    + "left join bdintegrado.ama_catalogo marca on (modd.marca_id=marca.id) "
                    + "inner join bdintegrado.tipo_base tb on (gt.estado_id=tb.id) "
                    + "inner join bdintegrado.sb_persona pe on (pe.id = gt.solicitante_id) "
                    + "left join bdintegrado.tipo_base tb1 on (pe.tipo_doc=tb1.id) "
                    + "inner join bdintegrado.tipo_gamac tg on (gt.tipo_guia_id=tg.id) "
                    + "left join bdintegrado.tipo_gamac tg2 on (gt.tipo_ingreso=tg2.id) "
                    + "where "
                    + "gt.activo = 1 and gt.id = ?1";//and tg.cod_prog!='TP_GTGAMAC_REC' 

//            String jpql = "select gt.id, gt.nro_expediente as nroExpediente, gt.fecha_emision as fechEmi, gt.fecha_vencimiento as fechVen, tb.nombre as estadoGuia, "
//                    + "pe.rzn_social as rznSocial, pe.nombres, pe.ape_pat as apPat, pe.ape_mat as apMat, "
//                    + "tb1.nombre as tipodoc, pe.ruc, pe.num_doc as numDoc, gt.nro_guia as nroGuia, "
//                    //Ultimos datos
//                    + "dirSuc.referencia as almacen, tg.nombre as tipoGuia, ia.serie, ia.nro_rua as nroRua, tipoAr.nombre as tipoArma, modd.id as modeloId, marca.nombre as marca "
//                    //Fin
//                    + "from bdintegrado.ama_guia_transito gt "
//                    + "left join bdintegrado.ama_guia_transito_inventario inv on (gt.id=inv.gamac_guia_transito_id) "
//                    + "inner join bdintegrado.ama_inventario_arma ia on (inv.inventario_arma_id=ia.id) inner join bdintegrado.ama_modelos modd on (ia.modelo_id=modd.id) "
//                    //Ultimas relaciones para obtener los datos.
//                    + "inner join bdintegrado.ama_catalogo tipoAr on (tipoAr.id = modd.tipo_arma_id) "
//                    + "inner join bdintegrado.sb_direccion dirSuc on (ia.almacen_sucamec_id = dirSuc.id) "
//                    //
//                    + "inner join bdintegrado.ama_catalogo marca on (modd.marca_id=marca.id) "
//                    + "inner join bdintegrado.tipo_base tb on (gt.estado_id=tb.id) "
//                    + "inner join bdintegrado.sb_persona pe on (pe.id = gt.solicitante_id) "
//                    + "left join bdintegrado.tipo_base tb1 on (pe.tipo_doc=tb1.id) "
//                    + "inner join bdintegrado.tipo_gamac tg on (gt.tipo_guia_id=tg.id) "
//                    + "where "
//                    + "gt.activo = 1 and gt.id = ?1 ";// and tg.cod_prog!='TP_GTGAMAC_REC'
            Query q = em.createNativeQuery(jpql);
            q.setParameter(1, idGuia);
            q.setHint("eclipselink.result-type", "Map");
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listRes;
    }

    /**
     * Consulta BUSCAR ARMA RENAGI Y MIGRA por serie y/o docPropietario y/o numero licencia DISCA
     * @param nroSerie
     * @param docProp
     * @param nroLic
     * 
     * @return 
     */
    public List<Map> buscarArmaRenagiMigra(String nroSerie, String docProp, String nroLic) {    
        String sql = "SELECT 0 ID, LICENCIA_DISCA NRO_LIC, NRO_RUA, SERIE NRO_SERIE, TIPO_ARMA, MARCA, MODELO, CALIBRE, TIPO_PROPIETARIO,PERSONA_ID, \n "
                +        " DOC_PROPIETARIO, PROPIETARIO, SISTEMA, SITUACION_ARMA SITUACION, ESTADO_ARMA, CP_SITUACION, ESTADO_TARJETA, TIPO_DOCUMENTO, NRO_CIP,  \n"
                +        " CASE WHEN SISTEMA='DISCA' THEN MA.ID END ID_DISCA, TP.ID ID_TARJETA  \n"
                + "FROM (" + ejbAmaMaestroArmasFacade.getSQL_GENERAL() + ") MA "
                + "LEFT JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD TP ON TP.NRO_EXPEDIENTE = MA.NRO_EXPEDIENTE AND TP.ARMA_ID = MA.ID AND TP.ACTIVO=1 AND MA.SISTEMA='GAMAC' "
                + "WHERE SERIE IS NOT NULL  ";
        try {
            if (nroSerie != null && !"".equals(nroSerie.trim())) {
                sql += "AND SERIE = ?1 ";
            }
            if (docProp != null && !"".equals(docProp.trim())) {
                sql += "AND DOC_PROPIETARIO = ?2 ";
            }
            if (nroLic != null && !"".equals(nroLic.trim())) {
                sql += "AND TO_NUMBER(LICENCIA_DISCA) = ?3 ";
            }
            String jpql = sql ;
            //System.out.println(jpql);
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
 
    public AmaGuiaTransito obtenerGuiaXNroExp(String nroExp) {
        AmaGuiaTransito res = null;
        Query q = em.createQuery("select a from AmaGuiaTransito a where a.nroExpediente = :nroExp and a.activo = 1 ");
        q.setParameter("nroExp", nroExp);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = (AmaGuiaTransito) q.getResultList().get(0);
        }
        return res;
    }

    public List<AmaGuiaTransito> obtenerGuiasPorArma(Long idModelo, String serie) {
        List<AmaGuiaTransito> listRes = null;
        Query q = em.createQuery("select gt from AmaInventarioArma ar left join ar.amaGuiaTransitoList gt where  ar.serie = :serie and ar.modeloId.id = :idMod and ar.activo = 1 and gt.activo = 1 order by gt.id desc");
        q.setParameter("idMod", idModelo);
        q.setParameter("serie", serie);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    /**
     *
     * @param devolucionId
     * @return
     */
    public Boolean existeActaDevolucion(Long devolucionId) {
        Boolean res = Boolean.FALSE;
        Query q = em.createQuery("select a.id from AmaGuiaTransito a where a.id = :idActa and a.activo = 1 and a.tipoGuiaId.tipoId.codProg = 'TP_GTGAMAC_DEV' ");
        q.setParameter("idActa", devolucionId);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = Boolean.TRUE;
        }
        return res;
    }

    /**
     * @param tipoBus
     * @param filtro
     * @param tipoGuia
     * @param cadenaIds
     * @param fechEmision
     * @param area
     * @param dirSuc
     * @param estadoActa
     * @param modoInt
     * @param fechaIni
     * @param fechaFin
     * @return
     */
    public List<Map> obtenerActasReporte(String tipoBus, String filtro, TipoGamac tipoGuia, Date fechEmision, SbTipo area,
            SbDireccion dirSuc, TipoBaseGt estadoActa, TipoGamac modoInt, Date fechaIni, Date fechaFin, List<TipoGamac> situaciones, TipoGamac estadoArma, AmaCatalogo tipoArt, AmaCatalogo tipoArma, String codMovPadre, Boolean ultimo) {
        List<Map> listRes = new ArrayList<>();
        try {
//            String idsTipo = "";
//            Query q0 = em.createNativeQuery("SELECT "
//                    + "LISTAGG(ID,',') WITHIN GROUP (ORDER BY ID DESC) IDS "
//                    + "FROM BDINTEGRADO.TIPO_GAMAC "
//                    + "WHERE ACTIVO = 1 "
//                    + "CONNECT BY PRIOR ID = TIPO_ID "
//                    + "START WITH id =(SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE COD_PROG = ?15) ");
//            q0.setHint("eclipselink.result-type", "Map");
//            q0.setParameter(15, codMovPadre);
//            if (q0.getResultList() != null && !q0.getResultList().isEmpty()) {
//                idsTipo = (String) ((Map) q0.getResultList().get(0)).get("IDS");
//            }
            String jpql = "select distinct gt.id, gt.nro_expediente as nroExpediente, gt.fecha_emision as fechEmi, gt.fecha_vencimiento as fechVen, est.nombre as estadoGuia, gt.devolucion_id, "
                    + "pe.rzn_social as rznSocial, pe.nombres, pe.ape_pat as apPat, pe.ape_mat as apMat, "
                    + "tdoc.nombre as tipodoc, pe.ruc, pe.num_doc as numDoc, gt.nro_guia as nroGuia, tg2.cod_prog as codModo, dirSuc.area_id as area, "
                    //Ultimos datos
                    + "dirSuc.referencia as almacen, tg.nombre as tipoGuia, tg.cod_prog as codTg, ia.serie, ia.nro_rua as nroRua, tipoAr.nombre as tipoArma, modd.id as modeloId, modd.modelo as modelo, marca.nombre as marca, usu.login, "
                    //Campos para reporte
                    + "proc.nombre proced, inst.nombre inst_ordena, mot.nombre motivo, "
                    + "listagg(decode(inf.rzn_social,null, inf.nombres||' '||inf.ape_pat||' '||inf.ape_mat,inf.rzn_social),', ') within group (order by inf.id asc) infractor, "
                    + "doc.numero num_docsust, "
                    + "decode(prop.rzn_social,null, prop.nombres||' '||prop.ape_pat||' '||prop.ape_mat,prop.rzn_social) propietario, efun.nombre est_fun, econ.nombre est_cons, sit.nombre situacion "
                    //Fin
                    + "from ";
            if (ultimo) {
                jpql += "(SELECT  "
                        + "IA.SERIE,IA.MODELO_ID,MAX(GT.ID) ID_MAX FROM BDINTEGRADO.AMA_GUIA_TRANSITO GT "
                        + "INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO GTI ON GT.ID=GTI.GAMAC_GUIA_TRANSITO_ID "
                        + "INNER JOIN BDINTEGRADO.AMA_INVENTARIO_ARMA IA ON GTI.INVENTARIO_ARMA_ID=IA.ID "
                        + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON GT.TIPO_GUIA_ID=TG.ID "
                        + "LEFT JOIN BDINTEGRADO.TIPO_BASE EST ON GT.ESTADO_ID=EST.ID "
                        + "LEFT JOIN BDINTEGRADO.SB_DIRECCION DIRSUC ON (IA.ALMACEN_SUCAMEC_ID = DIRSUC.ID) "
                        + "WHERE GT.ACTIVO = 1  "
                        + "AND DIRSUC.ID = ?6 "
                        + "AND TG.ID IN (SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE ACTIVO = 1 "
                        + "CONNECT BY PRIOR ID = TIPO_ID "
                        + "START WITH id =(SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE COD_PROG = ?15)) /*IDS TIPOS DE GUIA*/ "
                        + "GROUP BY   "
                        + "IA.SERIE,IA.MODELO_ID "
                        + "ORDER BY IA.SERIE DESC) DATA "
                        + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID=DATA.ID_MAX ";
            } else {
                jpql += "BDINTEGRADO.AMA_GUIA_TRANSITO GT ";
            }
//                    + "bdintegrado.ama_guia_transito gt "
            jpql += "left join bdintegrado.sb_usuario usu on (gt.usuario_id = usu.id) "
                    + "inner join bdintegrado.tipo_base tbarea on (usu.area_id = tbarea.id) "
                    + "left join bdintegrado.ama_guia_municiones mun on (gt.id=mun.guia_transito_id) "
                    + "left join bdintegrado.ama_guia_transito_inventario inv on (gt.id=inv.gamac_guia_transito_id) "
                    //+ "left join bdintegrado.ama_inventario_arma ia on (inv.inventario_arma_id=ia.id) left join bdintegrado.ama_modelos modd on (ia.modelo_id=modd.id) "
                    + "left join bdintegrado.ama_inventario_arma ia on (inv.inventario_arma_id=ia.id) "
                    + "left join bdintegrado.ama_modelos modd on (ia.modelo_id=modd.id) "
                    + "left join bdintegrado.ama_inventario_accesorios acc on (acc.guia_transito_id = gt.id) "
                    //Ultimas relaciones para obtener los datos.
                    + "left join bdintegrado.ama_catalogo tipoAr on (tipoAr.id = modd.tipo_arma_id) "
                    + "left join bdintegrado.sb_direccion dirSuc on (ia.almacen_sucamec_id = dirSuc.id) "
                    + "left join bdintegrado.ama_catalogo tipoArt on (tipoArt.id = modd.tipo_articulo_id) "
                    //
                    + "left join bdintegrado.ama_catalogo marca on (modd.marca_id=marca.id) "
                    + "inner join bdintegrado.tipo_base est on (gt.estado_id=est.id) "
                    + "inner join bdintegrado.sb_persona pe on (pe.id = gt.solicitante_id) "
                    + "left join bdintegrado.tipo_base tdoc on (pe.tipo_doc=tdoc.id) "
                    + "inner join bdintegrado.tipo_gamac tg on (gt.tipo_guia_id=tg.id) "
                    + "left join bdintegrado.tipo_gamac tg2 on (gt.tipo_ingreso=tg2.id) "
                    //Ultimos cambios para reporte
                    + "left join bdintegrado.tipo_gamac proc on (gt.procedencia_id=proc.id) "//procedencia
                    + "left join bdintegrado.tipo_gamac inst on (gt.institucion_ordena_id=inst.id) "//institucion que ordena
                    + "left join bdintegrado.tipo_gamac mot on (gt.motivo_id=mot.id) "//motivo internamiento
                    + "left join bdintegrado.ama_acta_infractor ainf on gt.id=ainf.guia_transito_id "
                    + "left join bdintegrado.sb_persona inf on ainf.persona_id=inf.id "//infractor
                    + "left join bdintegrado.ama_acta_documento adoc on gt.id=adoc.guia_transito_id "
                    + "left join bdintegrado.ama_documento doc on adoc.documento_id=doc.id "//documento sustento
                    + "left join bdintegrado.sb_persona prop on (ia.propietario_id = prop.id) "//propietario
                    + "left join bdintegrado.tipo_gamac efun on (ia.estadofuncional_id=efun.id) "//estado funcional arma 
                    + "left join bdintegrado.tipo_gamac econ on (ia.estadoconservacion_id=econ.id) "//estado conservación del arma
                    + "left join bdintegrado.tipo_gamac sit on (ia.situacion_id=sit.id) "//situación actual del arma
                    + "where "
                    + "gt.activo = 1 ";
            if (!ultimo) {
                jpql += "and tg.id in ("
                        + "SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE ACTIVO = 1 "
                        + "CONNECT BY PRIOR ID = TIPO_ID "
                        + "START WITH id =(SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE COD_PROG = ?15)"
                        + ") ";
                if (dirSuc != null) {
                    jpql += "and dirSuc.id = ?6 ";
                }
            }
//                    + "and tg.id in (" + cadenaIds + ") ";// and tg.cod_prog!='TP_GTGAMAC_REC'
            if (tipoGuia != null) {
                jpql += "and (tg.id = ?3 or tg.tipo_id in (?3)) ";
            }
//            if (dirSuc != null) {
//                jpql += "and dirSuc.id = ?6 ";
//            }
            if (estadoActa != null) {
                jpql += "and est.id = ?7 ";
            }
            if (modoInt != null) {
                jpql += "and gt.tipo_ingreso = ?8 ";
            }
            if (area != null) {
                jpql += "and tbarea.id = ?5 ";
            }
            if (fechaIni != null && fechaFin != null) {
                jpql += "and (TRUNC(gt.fecha_emision) between TRUNC(?9) and TRUNC(?10)) ";
            }
            if (situaciones != null && !situaciones.isEmpty()) {
                String sit = "";
                for (TipoGamac s : situaciones) {
                    sit += s.getId() + ",";
                }
                sit = sit.substring(0, sit.length() - 1);
                jpql += "and sit.id in (" + sit + ") ";
            }
            if (estadoArma != null) {
                jpql += "and efun.id = ?12 ";
            }
            if (tipoArt != null) {
                jpql += "and tipoArt.id = ?13 ";
            }
            if (tipoArma != null) {
                jpql += "and tipoAr.id = ?14 ";
            }
            if (tipoBus != null && (filtro != null || fechEmision != null)) {
                switch (tipoBus) {
                    case "A":
                        jpql += "and (concat(pe.nombres, concat(' ',concat(pe.ape_Pat, concat(' ',pe.ape_mat)))) like ?2 or pe.rzn_social like ?2) ";
                        break;
                    case "B":
                        jpql += "and (concat(prop.nombres, concat(' ',concat(prop.ape_Pat, concat(' ',prop.ape_mat)))) like ?2 or prop.rzn_social like ?2) ";
                        break;
                    case "C":
                        jpql += "and usu.login like ?2 ";
                        break;
                    case "D":
                        jpql += "and TRUNC(gt.fecha_emision) = TRUNC(?4) ";
                        break;
                    case "E":
                        jpql += "and gt.nro_expediente like ?2 ";
                        break;
                    case "F":
                        jpql += "and gt.nro_guia like ?2 ";
                        break;
                    case "G":
                        jpql += "and ia.serie like ?1 ";
                        break;
                    case "H":
                        jpql += "and ia.nro_rua like ?2 ";
                        break;
                    case "I":
                        jpql += "and marca.nombre like ?2 ";
                        break;
                    case "J":
                        jpql += "and modd.modelo like ?2 ";
                        break;
                    default:
                        break;
                }
            }
            jpql += "GROUP BY gt.id, gt.nro_expediente, gt.fecha_emision, gt.fecha_vencimiento, est.nombre, gt.devolucion_id, pe.rzn_social, pe.nombres, pe.ape_pat, pe.ape_mat, "
                    + "tdoc.nombre, pe.ruc, pe.num_doc, gt.nro_guia, tg2.cod_prog, dirSuc.area_id, dirSuc.referencia, tg.nombre, tg.cod_prog, ia.serie, ia.nro_rua, "
                    + "tipoAr.nombre, modd.id, modd.modelo, marca.nombre, usu.login, proc.nombre, inst.nombre, mot.nombre, doc.numero, "
                    + "decode(prop.rzn_social,null, prop.nombres||' '||prop.ape_pat||' '||prop.ape_mat,prop.rzn_social), efun.nombre, econ.nombre, sit.nombre ";
            jpql += " order by gt.fecha_emision desc ";
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(15, codMovPadre);
            if (area != null) {
                q.setParameter(5, area.getId());
            }
            if (tipoGuia != null) {
                q.setParameter(3, tipoGuia.getId());
            }
            if (dirSuc != null) {
                q.setParameter(6, dirSuc.getId());
            }
            if (estadoActa != null) {
                q.setParameter(7, estadoActa.getId());
            }
            if (modoInt != null) {
                q.setParameter(8, modoInt.getId());
            }
            if (fechaIni != null && fechaFin != null) {
                q.setParameter(9, fechaIni);
                q.setParameter(10, fechaFin);
            }
            if (estadoArma != null) {
                q.setParameter(12, estadoArma.getId());
            }
            if (tipoArt != null) {
                q.setParameter(13, tipoArt.getId());
            }
            if (tipoArma != null) {
                q.setParameter(14, tipoArma.getId());
            }
            if (tipoBus != null && (filtro != null || fechEmision != null)) {
                if ("G".equals(tipoBus)) {
                    q.setParameter(1, filtro);
                } else if ("D".equals(tipoBus)) {
                    q.setParameter(4, fechEmision, TemporalType.DATE);
                } else {
                    q.setParameter(2, "%" + filtro + "%");
                }
            }
            //q.setMaxResults(MAX_RES_M);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listRes;
    }

    /**
     * @param tipoBus
     * @param filtro
     * @param tipoGuia
     * @param cadenaIds
     * @param fechEmision
     * @param area
     * @param dirSuc
     * @param estadoActa
     * @param modoInt
     * @param fechaIni
     * @param fechaFin
     * @param situacion
     * @param estadoArma
     * @param tipoArma
     * @return
     * @author Gino Chávez
     */
    public List<Map> obtenerActasDepDev(String tipoBus, String filtro, TipoGamac tipoGuia, Date fechEmision, SbTipo area, SbDireccion dirSuc, TipoBaseGt estadoActa,
            TipoGamac modoInt, Date fechaIni, Date fechaFin, List<TipoGamac> situaciones, TipoGamac estadoArma, AmaCatalogo tipoArt, AmaCatalogo tipoArma, String codMovPadre, Boolean ultimo) {
        List<Map> listRes = new ArrayList<>();
        try {
            String idsTipo = "";
            Query q0 = em.createNativeQuery("SELECT "
                    + "LISTAGG(ID,',') WITHIN GROUP (ORDER BY ID DESC) IDS "
                    + "FROM BDINTEGRADO.TIPO_GAMAC "
                    + "WHERE ACTIVO = 1 "
                    + "CONNECT BY PRIOR ID = TIPO_ID "
                    + "START WITH id =(SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE COD_PROG = ?15) ");
            q0.setHint("eclipselink.result-type", "Map");
            q0.setParameter(15, codMovPadre);
            if (q0.getResultList() != null && !q0.getResultList().isEmpty()) {
                idsTipo = (String) ((Map) q0.getResultList().get(0)).get("IDS");
            }
            String jpql = "select distinct gt.id, gt.nro_expediente as nroExpediente, gt.fecha_emision as fechEmi, gt.fecha_vencimiento as fechVen, tb.nombre as estadoGuia, gt.devolucion_id, "
                    + "pe.rzn_social as rznSocial, pe.nombres, pe.ape_pat as apPat, pe.ape_mat as apMat, "
                    + "tb1.nombre as tipodoc, pe.ruc, pe.num_doc as numDoc, gt.nro_guia as nroGuia, tg2.cod_prog as codModo, dirSuc.area_id as area, "
                    //Ultimos datos
                    + "dirSuc.referencia as almacen, tg.nombre as tipoGuia, tg.cod_prog as codTg, ia.serie, ia.nro_rua as nroRua, tipoAr.nombre as tipoArma, modd.id as modeloId, modd.modelo as modelo, marca.nombre as marca, usu.login, sit.nombre situacion "
                    //Fin
                    + "from ";
            if (ultimo) {
                jpql += "(SELECT  "
                        + "IA.SERIE,IA.MODELO_ID,MAX(GT.ID) ID_MAX FROM BDINTEGRADO.AMA_GUIA_TRANSITO GT "
                        + "INNER JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO GTI ON GT.ID=GTI.GAMAC_GUIA_TRANSITO_ID "
                        + "INNER JOIN BDINTEGRADO.AMA_INVENTARIO_ARMA IA ON GTI.INVENTARIO_ARMA_ID=IA.ID "
                        + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON GT.TIPO_GUIA_ID=TG.ID "
                        + "LEFT JOIN BDINTEGRADO.TIPO_BASE EST ON GT.ESTADO_ID=EST.ID "
                        + "LEFT JOIN BDINTEGRADO.SB_DIRECCION DIRSUC ON (IA.ALMACEN_SUCAMEC_ID = DIRSUC.ID) "
                        + "WHERE GT.ACTIVO = 1  "
                        + " AND DIRSUC.ID            = ?6 "
                        + "AND TG.ID IN (" + idsTipo + ") /*IDS TIPOS DE GUIA*/ "
                        + "GROUP BY   "
                        + "IA.SERIE,IA.MODELO_ID "
                        + "ORDER BY IA.SERIE DESC) DATA "
                        + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO GT ON GT.ID=DATA.ID_MAX ";
            } else {
                jpql += "BDINTEGRADO.AMA_GUIA_TRANSITO GT ";
            }
            jpql += "left join bdintegrado.sb_usuario usu on (gt.usuario_id = usu.id) "
                    + "INNER JOIN BDINTEGRADO.TIPO_BASE TBAREA ON (USU.AREA_ID = TBAREA.ID) "
                    + "LEFT JOIN BDINTEGRADO.AMA_GUIA_MUNICIONES MUN ON (GT.ID=MUN.GUIA_TRANSITO_ID) "
                    + "LEFT JOIN BDINTEGRADO.AMA_GUIA_TRANSITO_INVENTARIO INV ON (GT.ID=INV.GAMAC_GUIA_TRANSITO_ID) "
                    + "LEFT JOIN BDINTEGRADO.AMA_INVENTARIO_ARMA IA ON (INV.INVENTARIO_ARMA_ID=IA.ID) "
                    + "LEFT JOIN BDINTEGRADO.AMA_MODELOS MODD ON (IA.MODELO_ID=MODD.ID) "
                    + "LEFT JOIN BDINTEGRADO.AMA_INVENTARIO_ACCESORIOS ACC ON (ACC.GUIA_TRANSITO_ID = GT.ID) "
                    //Ultimas relaciones para obtener los datos.
                    + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO TIPOAR ON (TIPOAR.ID = MODD.TIPO_ARMA_ID) "
                    + "left join bdintegrado.sb_direccion dirSuc on (ia.almacen_sucamec_id = dirSuc.id) "
                    + "LEFT JOIN BDINTEGRADO.SB_PERSONA PROP ON (IA.PROPIETARIO_ID = PROP.ID) "
                    + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO TIPOART ON (TIPOART.ID = MODD.TIPO_ARTICULO_ID)  "
                    //
                    + "LEFT JOIN BDINTEGRADO.AMA_CATALOGO MARCA ON (MODD.MARCA_ID=MARCA.ID) "
                    + "INNER JOIN BDINTEGRADO.TIPO_BASE TB ON (GT.ESTADO_ID=TB.ID) "
                    + "INNER JOIN BDINTEGRADO.SB_PERSONA PE ON (PE.ID = GT.SOLICITANTE_ID) "
                    + "LEFT JOIN BDINTEGRADO.TIPO_BASE TB1 ON (PE.TIPO_DOC=TB1.ID) "
                    + "INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON (GT.TIPO_GUIA_ID=TG.ID) "
                    + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG2 ON (GT.TIPO_INGRESO=TG2.ID) "
                    + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC SIT ON (IA.SITUACION_ID=SIT.ID) "//situación actual del arma
                    + "LEFT JOIN BDINTEGRADO.TIPO_GAMAC EFUN ON (IA.ESTADOFUNCIONAL_ID=EFUN.ID) "//estado funcional arma 
                    + "where "
                    + "gt.activo = 1 ";
            if (!ultimo) {
                jpql += "and tg.id in (" + idsTipo + ") ";
                if (dirSuc != null) {
                    jpql += "and dirSuc.id = ?6 ";
                }
            }
            if (tipoGuia != null) {
                jpql += "and (tg.id = ?3 or tg.tipo_id in (?3)) ";
            }
            if (estadoActa != null) {
                jpql += "and tb.id = ?7 ";
            }
            if (modoInt != null) {
                jpql += "and gt.tipo_ingreso = ?8 ";
            }
            if (area != null) {
                jpql += "and tbarea.id = ?5 ";
            }
            if (fechaIni != null && fechaFin != null) {
                jpql += "and (TRUNC(gt.fecha_emision) between TRUNC(?9) and TRUNC(?10)) ";
            }
            if (situaciones != null && !situaciones.isEmpty()) {
                String sit = "";
                for (TipoGamac s : situaciones) {
                    sit += s.getId() + ",";
                }
                sit = sit.substring(0, sit.length() - 1);
                jpql += "and sit.id in (" + sit + ") ";
            }
            if (estadoArma != null) {
                jpql += "and efun.id = ?12 ";
            }
            if (tipoArt != null) {
                jpql += "and tipoArt.id = ?13 ";
            }
            if (tipoArma != null) {
                jpql += "and tipoAr.id = ?14 ";
            }
            if (tipoBus != null && (filtro != null || fechEmision != null)) {
                switch (tipoBus) {
                    case "A":
                        jpql += "and (concat(pe.nombres, concat(' ',concat(pe.ape_Pat, concat(' ',pe.ape_mat)))) like ?2 or pe.rzn_social like ?2) ";
                        break;
                    case "B":
                        jpql += "and (concat(prop.nombres, concat(' ',concat(prop.ape_Pat, concat(' ',prop.ape_mat)))) like ?2 or prop.rzn_social like ?2) ";
                        break;
                    case "C":
                        jpql += "and usu.login like ?2 ";
                        break;
                    case "D":
                        jpql += "and TRUNC(gt.fecha_emision) = TRUNC(?4) ";
                        break;
                    case "E":
                        jpql += "and gt.nro_expediente like ?2 ";
                        break;
                    case "F":
                        jpql += "and gt.nro_guia like ?2 ";
                        break;
                    case "G":
                        jpql += "and ia.serie like ?1 ";
                        break;
                    case "H":
                        jpql += "and ia.nro_rua like ?2 ";
                        break;
                    case "I":
                        jpql += "and marca.nombre like ?2 ";
                        break;
                    case "J":
                        jpql += "and modd.modelo like ?2 ";
                        break;
                    default:
                        break;
                }
            }
            jpql += " order by gt.fecha_emision desc ";
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            //q.setParameter(6, idUsuarioTD);
            if (area != null) {
                q.setParameter(5, area.getId());
            }
            if (tipoGuia != null) {
                q.setParameter(3, tipoGuia.getId());
            }
//            if (dirSuc != null) {
            q.setParameter(6, dirSuc == null ? 0L : dirSuc.getId());
//            }
            if (estadoActa != null) {
                q.setParameter(7, estadoActa.getId());
            }
            if (modoInt != null) {
                q.setParameter(8, modoInt.getId());
            }
            if (fechaIni != null && fechaFin != null) {
                q.setParameter(9, fechaIni);
                q.setParameter(10, fechaFin);
            }
            if (estadoArma != null) {
                q.setParameter(12, estadoArma.getId());
            }
            if (tipoArt != null) {
                q.setParameter(13, tipoArt.getId());
            }
            if (tipoArma != null) {
                q.setParameter(14, tipoArma.getId());
            }
            if (tipoBus != null && (filtro != null || fechEmision != null)) {
                if ("G".equals(tipoBus)) {
                    q.setParameter(1, filtro);
                } else if ("D".equals(tipoBus)) {
                    q.setParameter(4, fechEmision, TemporalType.DATE);
                } else {
                    q.setParameter(2, "%" + filtro + "%");
                }
            }
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listRes;
    }
    
}
