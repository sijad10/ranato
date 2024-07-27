/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author rmoscoso
 */
@Stateless
public class ConsultaGuiasPNPFacade {

    private static final int MAX_RES = 10000;

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    public ConsultaGuiasPNPFacade() {
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

    public List<HashMap> listarGuias(String guia, String exp, String rg, String ruc) {
        Query q = em.createNativeQuery(
                "SELECT REG.ID \"PDF_ID\", PER1.RZN_SOCIAL||PER1.APE_PAT|| ' ' ||PER1.APE_MAT ||' ' ||PER1.NOMBRES AS RAZON_SOCIAL, PER1.RUC, TB.NOMBRE,\n"
                + "RES.NUMERO \"NRO_RG\", REG.NRO_EXPEDIENTE \"NRO_EXPEDIENTE\", RES2.NUMERO \"NRO_GT\", RES2.FECHA_INI, RES2.FECHA_FIN,\n"
                + "TE.NOMBRE \"ESTADO_GT\", REG.AUD_LOGIN \"USUARIO\", RES2.HASH_QR \n"
                + "FROM BDINTEGRADO.EPP_REGISTRO_GUIA_TRANSITO RGT\n"
                + "INNER JOIN BDINTEGRADO.EPP_REGISTRO REG ON RGT.REGISTRO_ID = REG.ID\n"
                + "INNER JOIN BDINTEGRADO.SB_PERSONA PER1 ON RGT.EMPRESA_ID = PER1.ID\n"
                + "INNER JOIN BDINTEGRADO.TIPO_EXPLOSIVO TE ON REG.ESTADO = TE.ID\n"
                + "LEFT JOIN BDINTEGRADO.EPP_RESOLUCION RES ON RGT.RESOLUCION_ID = RES.ID\n"
                + "LEFT JOIN BDINTEGRADO.EPP_REGISTRO REG2 ON RES.REGISTRO_ID = REG2.ID\n"
                + "LEFT JOIN BDINTEGRADO.TIPO_BASE TB ON REG2.TIPO_PRO_ID = TB.ID\n"
                + "INNER JOIN BDINTEGRADO.EPP_RESOLUCION RES2 ON RGT.REGISTRO_ID = RES2.REGISTRO_ID\n"
                + "WHERE REG.ACTIVO=1 AND REG.ESTADO=563 AND RES2.NUMERO IS NOT NULL\n"
                + "AND PER1.RUC LIKE ?1 AND RES2.NUMERO LIKE ?2 AND REG.NRO_EXPEDIENTE LIKE ?3 AND RES.NUMERO LIKE ?4\n"
                + "ORDER BY FECHA_INI DESC"
        );
        q.setParameter(1, nullATodo(ruc));
        q.setParameter(2, nullATodoParcial(guia));
        q.setParameter(3, nullATodoParcial(exp));
        q.setParameter(4, nullATodoParcial(rg));
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
    
    public List<HashMap> listarSolicitudesGuia(String filtro, SbPersona p) {

        if ("".equals(filtro)) {
            filtro = null;
        }

        try {
            Query q = em.createNativeQuery("select \n"
                    + "r.id \"ID\",\n"
                    + "r.nro_solicitud \"NRO_SOLICITUD\",\n"
                    + "decode(p.ruc,null,p.num_doc,p.ruc) \"RUC_DNI\",\n"
                    + "decode(p.rzn_social, null, (p.nombres||' '||p.ape_pat||' '||p.ape_mat), p.rzn_social) \"SOLICITANTE\",\n"
                    + "ao.razon_social||po.descripcion||aao.razon_social||pao.nombre||luo.nombre \"ORIGEN_SD\",\n"
                    + "ad.razon_social||pd.descripcion||aad.razon_social||pad.nombre||lud.nombre \"DESTINO_SD\",\n"
                    + "'<b>Para que pueda remitir desde:</b> '||ao.razon_social||po.descripcion||aao.razon_social||pao.nombre||luo.nombre||\n"
                    + "'. Dirección: '||\n"
                    + "ao.direccion||po.direccion||aao.direccion||pao.direccion ||\n"
                    + "', Distrito de: '||dso1.nombre||dso2.nombre||dso3.nombre||dso4.nombre||dso5.nombre|| \n"
                    + "', Departamento de: '||dpo1.nombre||dpo2.nombre||dpo3.nombre||dpo4.nombre||dpo5.nombre||\n"
                    + "', Provincia de: '||po1.nombre||po2.nombre||po3.nombre||po4.nombre||po5.nombre \"ORIGEN\",\n"
                    + "'<b>Con destino a:</b> '||ad.razon_social||pd.descripcion||aad.razon_social||pad.nombre||lud.nombre||\n"
                    + "'. Dirección: '||\n"
                    + "ad.direccion||pd.direccion||aad.direccion||pad.direccion ||\n"
                    + "', Distrito de: '||dsd1.nombre||dsd2.nombre||dsd3.nombre||dsd4.nombre||dsd5.nombre|| \n"
                    + "', Departamento de: '||dpd1.nombre||dpd2.nombre||dpd3.nombre||dpd4.nombre||dpd5.nombre||\n"
                    + "', Provincia de: '||pd1.nombre||pd2.nombre||pd3.nombre||pd4.nombre||pd5.nombre \"DESTINO\",\n"
                    + "'A favor de: <b>ELLOS MISMOS.</b>' \"A_FAVOR\",\n"
                    + "res.numero \"NRO_RG\",\n"
                    + "decode(r.custodia,1,'REQUIERE CSUTODIA','NO REQUIERE CSUTODIA') \"REQUIERE_CUSTODIA\",\n"
                    + "tet.nombre \"TIPO_TRANSPORTE\",\n"
                    + "decode(et.rzn_social, null, (decode(et.nombres, null,null,(et.nombres||' '||et.ape_pat||' '||et.ape_mat))), et.rzn_social) \"EMP_TRANSPORT\"\n"
                    + "from bdintegrado.epp_gte_registro r \n"
                    + "inner join bdintegrado.sb_persona p on r.empresa_id = p.id\n"
                    + "left join bdintegrado.tipo_explosivo tet on r.tipo_transporte = tet.id \n"
                    + "left join bdintegrado.sb_persona et on r.empresa_transp_id = p.id  \n"
                    + "inner join bdintegrado.epp_resolucion res on r.resolucion_id = res.id \n"
                    + "--datos origen\n"
                    + "left join bdintegrado.epp_almacen ao on r.origen_almacen = ao.id\n"
                    + "left join bdintegrado.epp_polvorin po on r.origen_polvorin = po.id \n"
                    + "left join bdintegrado.epp_almacen_aduanero aao on r.origen_almacen_aduana = aao.id\n"
                    + "left join bdintegrado.epp_puerto_aduanero pao on r.puerto_aduanero_origen = pao.id \n"
                    + "left join bdintegrado.epp_lugar_uso luo on r.lugar_uso_origen = luo.id\n"
                    + "left join bdintegrado.epp_lugar_uso_ubigeo luuo on r.lugar_uso_ubigeo_origen = luuo.id  \n"
                    + "--ubigeo origen\n"
                    + "--almacen\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dso1 on ao.ubigeo_id = dso1.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpo1 on dso1.provincia_id = dpo1.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO po1 on dpo1.departamento_id = po1.id\n"
                    + "--polvorin\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dso2 on po.distrito_id = dso2.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpo2 on dso2.provincia_id = dpo2.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO po2 on dpo2.departamento_id = po2.id\n"
                    + "--almacen aduanero\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dso3 on aao.ubigeo_id = dso3.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpo3 on dso3.provincia_id = dpo3.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO po3 on dpo3.departamento_id = po3.id\n"
                    + "--puerto aduanero\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dso4 on pao.ubigeo_id = dso4.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpo4 on dso4.provincia_id = dpo4.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO po4 on dpo4.departamento_id = po4.id\n"
                    + "--lugar uso\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dso5 on luuo.dist_id = dso5.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpo5 on dso5.provincia_id = dpo5.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO po5 on dpo5.departamento_id = po5.id\n"
                    + "--datos destino\n"
                    + "left join bdintegrado.epp_almacen ad on r.destino_almacen = ad.id\n"
                    + "left join bdintegrado.epp_polvorin pd on r.destino_polvorin = pd.id \n"
                    + "left join bdintegrado.epp_almacen_aduanero aad on r.destino_almacen_aduana = aad.id\n"
                    + "left join bdintegrado.epp_puerto_aduanero pad on r.puerto_aduanero_destino = pad.id \n"
                    + "left join bdintegrado.epp_lugar_uso lud on r.lugar_uso_destino = lud.id\n"
                    + "left join bdintegrado.epp_lugar_uso_ubigeo luud on r.lugar_uso_ubigeo_destino = luud.id \n"
                    + "--ubigeo destino\n"
                    + "--almacen\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dsd1 on ad.ubigeo_id = dsd1.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpd1 on dsd1.provincia_id = dpd1.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO pd1 on dpd1.departamento_id = pd1.id\n"
                    + "--polvorin\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dsd2 on pd.distrito_id = dsd2.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpd2 on dsd2.provincia_id = dpd2.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO pd2 on dpd2.departamento_id = pd2.id\n"
                    + "--almacen aduanero\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dsd3 on aad.ubigeo_id = dsd3.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpd3 on dsd3.provincia_id = dpd3.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO pd3 on dpd3.departamento_id = pd3.id\n"
                    + "--puerto aduanero\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dsd4 on pad.ubigeo_id = dsd4.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpd4 on dsd4.provincia_id = dpd4.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO pd4 on dpd4.departamento_id = pd4.id\n"
                    + "--lugar uso\n"
                    + "left join BDINTEGRADO.SB_DISTRITO dsd5 on luud.dist_id = dsd5.id\n"
                    + "left join BDINTEGRADO.sb_provincia dpd5 on dsd5.provincia_id = dpd5.id\n"
                    + "left join BDINTEGRADO.SB_DEPARTAMENTO pd5 on dpd5.departamento_id = pd5.id\n"
                    + "where \n"
                    + "r.nro_solicitud is not null and \n"
                    + "r.activo = 1 and \n"
                    + "(r.nro_solicitud LIKE ?1 )");// "and r.empresa_id = ?2
                    //+ "(r.nro_solicitud = ?1 or ?1 is null)");// "and r.empresa_id = ?2            
            q.setParameter(1, nullATodoParcial(filtro)); //q.setParameter(1, filtro == null ? null : filtro);
            //q.setParameter(2, p.getId());
            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<HashMap> listarSolicitudesConduct(Long idGuia) {
        Query q = em.createNativeQuery("select \n"
                + "p.num_doc \"DNI\",\n"
                + "p.nombres||' '||p.ape_pat||' '||p.ape_mat \"NOMBRES\",\n"
                + "l.nro_licencia \"NRO_LIC\"\n"
                + "from \n"
                + "bdintegrado.epp_gte_conductor_detalle cd \n"
                + "inner join bdintegrado.epp_licencia l on cd.conductor_id = l.id \n"
                + "inner join bdintegrado.sb_persona p on l.persona_id = p.id \n"
                + "where \n"
                + "cd.guia_transito_id = ?1");
        q.setParameter(1, idGuia);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarSolicitudesVehiculo(Long idGuia) {
        Query q = em.createNativeQuery("select \n"
                + "gtv.marca \"MARCA\",\n"
                + "gtv.placa \"PLACA\"\n"
                + "from bdintegrado.epp_gte_vehiculo_detalle v \n"
                + "inner join bdintegrado.epp_guia_transito_vehiculo gtv on v.vehiculo_id = gtv.id\n"
                + "where v.guia_transito_id = ?1");
        q.setParameter(1, idGuia);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }

    public List<HashMap> listarSolicitudesExplosivos(Long idGuia) {
        Query q = em.createNativeQuery("select \n"
                + "ex.nombre \"EXPLOSIVO\",\n"
                + "u.nombre \"UNIDAD_MEDIDA\",\n"
                + "es.cantidad \"CANTIDAD\"\n"
                + "from \n"
                + "bdintegrado.epp_gte_explosivo_solicita es \n"
                + "inner join bdintegrado.epp_explosivo ex on es.explosivo_id = ex.id \n"
                + "left join bdintegrado.epp_explosivo_unidad eu on ex.id = eu.explosivo_id\n"
                + "left join bdintegrado.unidad_medida u on eu.unidad_medida_id = u.id\n"
                + "where \n"
                + "es.activo = 1 and \n"
                + "es.registro_id = ?1");
        q.setParameter(1, idGuia);
        q.setHint("eclipselink.result-type", "Map");
        return q.setMaxResults(MAX_RES).getResultList();
    }
}
