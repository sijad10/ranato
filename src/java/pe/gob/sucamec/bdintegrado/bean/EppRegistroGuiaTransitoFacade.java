/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.EppExplosivo;
import pe.gob.sucamec.bdintegrado.data.EppGteRegistro;
import pe.gob.sucamec.bdintegrado.data.EppLugarUso;
import pe.gob.sucamec.bdintegrado.data.EppLugarUsoUbigeo;
import pe.gob.sucamec.bdintegrado.data.EppPolvorin;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.EppRegistroGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.EppResolucion;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppRegistroGuiaTransitoFacade extends AbstractFacade<EppRegistroGuiaTransito> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppRegistroGuiaTransitoFacade() {
        super(EppRegistroGuiaTransito.class);
    }

    /**
     *
     * @param empresa
     * @param estado
     * @param nroExp
     * @param nroGuia
     * @param fecha
     * @param nroRg
     * @param nroRecibo
     * @param gtes
     * @return
     */
    public List<EppRegistroGuiaTransito> buscarGuiasExternoUsuario(SbPersonaGt empresa, TipoExplosivoGt estado, String nroExp, String nroGuia, Date fecha, String nroRg, String nroRecibo, String gtes) {
        String jpql = "select x from EppRegistroGuiaTransito x "
                + "left join x.resolucionId res "
                + "inner join x.registroId r "
                + "left join r.eppResolucion resrg "
                + "where "
                + "r.activo = 1 and "
                + "r.tipoProId.id = 418 and "   // TP_PRTUP_GUIS
                + "r.empresaId.id = :empresa and "
                + "x.tipoTramite.codProg in ("+gtes+") ";    // solo guias tipo (1 y 6) o (3 y 5)
                
                //+ "(trim(y.nroSecuencia) like :nroRecibo or :nroRecibo is null) ";
        if (fecha != null) {
            jpql = jpql + "and (func('TRUNC',resrg.fecha) = func('TRUNC',:fecha) or :fecha is null)";
        }
        if (estado != null) {
            jpql += " and (r.estado.id = :estado) ";
        }
        if (nroExp != null) {
            jpql += " and (r.nroExpediente like :nroExp ) ";
        }
        if (nroGuia != null) {
            jpql += " and (resrg.numero like :nroGuia ) ";
        }
        if (nroRg != null) {
            jpql += " and (res.numero like :nroRg ) ";
        }

        //ORDENANDO LISTADO
        jpql = jpql + " order by x.id desc";
        //GENERANDO QUERY
        Query query = em.createQuery(jpql);
        query.setParameter("empresa", empresa.getId());
        //query.setParameter("nroRecibo", nroRecibo == null ? null : ("%" + nroRecibo + "%"));
        if (fecha != null) {
            query.setParameter("fecha", fecha, TemporalType.DATE);
        }
        if (estado != null) {
            query.setParameter("estado", estado.getId());
        }
        if (nroExp != null) {
            query.setParameter("nroExp", ("%" + nroExp + "%"));
        }
        if (nroGuia != null) {
            query.setParameter("nroGuia", ("%" + nroGuia + "%"));
        }
        if (nroRg != null) {
            query.setParameter("nroRg", ("%" + nroRg + "%"));
        }
        return query.setMaxResults(500).getResultList();
    }

    /**
     *
     * @param empresa
     * @param estado
     * @param nroRg
     * @param gtes
     * @param fecha
     * @return
     */
    public List<EppGteRegistro> buscarGuiasExternoUsuarioBulk(SbPersonaGt empresa, TipoExplosivoGt estado, String nroRg, String gtes, Date fecha) {
        String jpql = "select x from EppGteRegistro x "
                + " left join x.resolucionId r "
                + " where "
                + " x.empresaId.id = :empresa and "
                + " x.activo = 1 and "
                + " x.tipoTramite.codProg in ("+gtes+") and " // solo guias tipo 1 y 6
                + " (x.estado.id = :estado or :estado is null) and "
                + " (r.numero like :nroRg or :nroRg is null) ";
        if(fecha != null){
            jpql += " and func('trunc',x.fecha) = func('trunc',:fecha)";
        }
        //ORDENANDO LISTADO
        jpql = jpql + " order by x.id desc";
        //GENERANDO QUERY
        Query query = em.createQuery(jpql);
        query.setParameter("empresa", empresa.getId());
        query.setParameter("estado", estado == null ? null : estado.getId());
        query.setParameter("nroRg", nroRg == null ? null : ("%" + nroRg + "%"));
        if(fecha != null){
            query.setParameter("fecha", fecha, TemporalType.DATE);
        }
        return query.setMaxResults(500).getResultList();
    }

    /**
     *
     * @param empresa
     * @param estado
     * @param nomCompra
     * @param rucCompra
     * @param destino
     * @param nroGuia
     * @return
     */
    public List<EppRegistroGuiaTransito> buscarGuiasExternoFabricante(SbPersonaGt empresa, TipoExplosivoGt estado, String nomCompra, String rucCompra, String destino, String nroGuia) {
        String jpql = "select x from EppRegistroGuiaTransito x "
                + "inner join x.registroId r "
                + "left join x.destinoPolvorin polv "
                + "left join x.lugarUsoDestino lud "
                + "left join r.eppResolucion a1 "
                + "where "
                + "r.activo = 1 and "
                + "r.tipoProId.codProg = 'TP_PRTUP_GUIS' and "
                + "(x.origenPolvorin.propietarioId.id = :empresa or "
                + "x.origenPolvorin in (select cap.polvorinId from EppContratoAlqPolv cap where cap.arrendatariaId.id = :empresa)) and "
                + "(r.estado.id = :estado or :estado is null) and "
                + "(r.empresaId.rznSocial like :nomCompra or :nomCompra is null) and "
                + "(r.empresaId.ruc like :rucCompra or :rucCompra is null) and "
                + "(polv.descripcion like :destino or lud.nombre like :destino or :destino is null) and "
                + "(a1.numero like :nroguia or :nroguia is null)";

        //ORDENANDO LISTADO
        jpql = jpql + " order by r.id desc";
        //GENERANDO QUERY
        Query query = em.createQuery(jpql);
        query.setParameter("empresa", empresa.getId());
        query.setParameter("estado", estado == null ? null : estado.getId());
        query.setParameter("nomCompra", nomCompra == null ? null : ("%" + nomCompra + "%"));
        query.setParameter("rucCompra", rucCompra == null ? null : ("%" + rucCompra + "%"));
        query.setParameter("destino", destino == null ? null : ("%" + destino + "%"));
        query.setParameter("nroguia", nroGuia == null ? null : ("%" + nroGuia + "%"));

        return query.setMaxResults(100).getResultList();
    }
    
    
    public List<Map> buscarGuiasExternoFabricanteMap(SbPersonaGt empresa, TipoExplosivoGt estado, String nomCompra, String rucCompra, String destino, String nroGuia, String explosivo) {
        String jpql = "select distinct x.id AS ID, r.empresaId.tipoDoc.nombre AS TIPO_DOC, r.empresaId.ruc AS RUC, r.empresaId.rznSocial as RZN_SOCIAL, "
                + " r.estado.nombre AS ESTADO, r.eppResolucion.numero AS RESOLUCION, r.audLogin AS LOGIN, "
                + " alOri.razonSocial as ALMORI_NOMBRE, alDes.razonSocial AS ALMDES_NOMBRE, "
                + " polOri.descripcion AS POLORI_NOMBRE, polDes.descripcion AS POLDES_NOMBRE, "
                + " aduOri.razonSocial AS ADUAORI_NOMBRE, aduDes.razonSocial AS ADUADES_NOMBRE, "
                + " pueOri.nombre AS PUEORI_NOMBRE, pueDes.nombre AS PUEDES_NOMBRE, "
                + " lugOri.nombre AS LUGORI_NOMBRE, lugDes.nombre AS LUGDES_NOMBRE, "
                + " r.estado.codProg AS ESTADO_CODPROG, r.id AS ID_REGISTRO, a1.numero AS RESOLUCION_NUMERO, "
                + " CONCAT(r.empresaId.apePat,' ',r.empresaId.apeMat,' ',r.empresaId.nombres) AS NOMBRE_DNI, "
                + " r.empresaId.tipoDoc.codProg AS TIPO_DOC_CODPROG, "
                + " perFabOri.rznSocial as FABORI_NOMBRE, "
                + " perFabDes.rznSocial as FABDES_NOMBRE, "
                + " arrOri.rznSocial as ALQORI_NOMBRE, "
                + " arrDes.rznSocial as ALQDES_NOMBRE, "
                + " x.tipoTramite.codProg as TIPO_TRAMITE "
                + " from EppRegistroGuiaTransito x "
                + " inner join x.registroId r "
                + " inner join x.eppExplosivoSolicitadoList es "
                + " left join r.eppResolucion a1 "
                + " left join x.origenAlmacen alOri "
                + " left join x.destinoAlmacen alDes "
                + " left join x.origenPolvorin polOri "
                + " left join polOri.propietarioId polProOri "
                + " left join x.destinoPolvorin polDes "
                + " left join x.origenAlmacenAduana aduOri "
                + " left join x.destinoAlmacenAduana aduDes "
                + " left join x.puertoAduaneroOrigen pueOri "
                + " left join x.puertoAduaneroDestino pueDes "
                + " left join x.lugarUsoOrigen lugOri "
                + " left join x.lugarUsoDestino lugDes "
                + " left join x.origenFabrica fabOri "
                + " left join fabOri.personaId perFabOri "
                + " left join x.origenAlquiler AlqOri "
                + " left join AlqOri.arrendatariaId arrOri "
                + " left join x.destinoFabrica fabDes "
                + " left join fabDes.personaId perFabDes "
                + " left join x.destinoAlquiler AlqDes "
                + " left join AlqDes.arrendatariaId arrDes "
                + "where "
                + "r.activo = 1 and "
                + "r.tipoProId.codProg = 'TP_PRTUP_GUIS' and "
                + " x.tipoTramite.codProg in ('TP_RGUIA_ADQUI1','TP_RGUIA_COMFAB') and " // solo guias tipo 1 y 6
                + "(polProOri.id = :empresa or "
                + "polOri in (select cap.polvorinId from EppContratoAlqPolv cap where cap.arrendatariaId.id = :empresa) or perFabOri.id = :empresa or arrOri.id = :empresa ) and "
                + "(r.estado.id = :estado or :estado is null) and "
                + "(r.empresaId.rznSocial like :nomCompra or :nomCompra is null) and "
                + "(r.empresaId.ruc like :rucCompra or :rucCompra is null) and "
                + "(polDes.descripcion like :destino or lugDes.nombre like :destino or :destino is null) and "
                + "(a1.numero like :nroguia or :nroguia is null) and "
                + "(es.explosivoId.nombre like :explosivo or :explosivo is null)";

        //ORDENANDO LISTADO
        jpql = jpql + " order by r.id desc";
        //GENERANDO QUERY
        Query query = em.createQuery(jpql);
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter("empresa", empresa.getId());
        query.setParameter("estado", estado == null ? null : estado.getId());
        query.setParameter("nomCompra", nomCompra == null ? null : ("%" + nomCompra + "%"));
        query.setParameter("rucCompra", rucCompra == null ? null : ("%" + rucCompra + "%"));
        query.setParameter("destino", destino == null ? null : ("%" + destino + "%"));
        query.setParameter("nroguia", nroGuia == null ? null : ("%" + nroGuia + "%"));
        query.setParameter("explosivo", explosivo == null ? null : ("%" + explosivo + "%"));

        return query.getResultList();
    }

    public List<EppGteRegistro> buscarGuiasExternoFabricanteBulk(SbPersonaGt empresa, TipoExplosivoGt estado, String nomCompra, String rucCompra, String destino) {
        String jpql = "select x from EppGteRegistro x "
                + "left join x.destinoPolvorin polv "
                + " left join x.origenPolvorin polOri "
                + " left join polOri.propietarioId polProOri "
                + " left join x.origenFabrica fabOri "
                + " left join fabOri.personaId perFabOri "
                + "left join x.lugarUsoDestino lud "
                //+ "left join x.eppResolucion a1 "
                + "where "
                + "x.activo = 1 and "
                + "x.tipoProId.codProg = 'TP_PRTUP_GUIS' and "
                + "(polProOri.id = :empresa or "
                + "polOri in (select cap.polvorinId from EppContratoAlqPolv cap where cap.arrendatariaId.id = :empresa) or perFabOri.id = :empresa ) and "
                + "(x.estado.id = :estado or :estado is null) and "
                + "(x.empresaId.rznSocial like :nomCompra or :nomCompra is null) and "
                + "(x.empresaId.ruc like :rucCompra or :rucCompra is null) and "
                + "(polv.descripcion like :destino or lud.nombre like :destino or :destino is null) ";

        //ORDENANDO LISTADO
        jpql = jpql + " order by x.id desc";
        //GENERANDO QUERY
        Query query = em.createQuery(jpql);
        query.setParameter("empresa", empresa.getId());
        query.setParameter("estado", estado == null ? null : estado.getId());
        query.setParameter("nomCompra", nomCompra == null ? null : ("%" + nomCompra + "%"));
        query.setParameter("rucCompra", rucCompra == null ? null : ("%" + rucCompra + "%"));
        query.setParameter("destino", destino == null ? null : ("%" + destino + "%"));

        return query.setMaxResults(100).getResultList();
    }
    
    public List<Map> buscarGuiasExternoFabricanteBulkMap(SbPersonaGt empresa, TipoExplosivoGt estado, String nomCompra, String rucCompra, String destino, String explosivo) {
        String jpql = "select distinct x.id AS ID, x.empresaId.tipoDoc.nombre AS TIPO_DOC, x.empresaId.ruc AS RUC, x.empresaId.rznSocial AS RZN_SOCIAL, "
                + " x.estado.nombre AS ESTADO, x.resolucionId.numero AS RESOLUCION, x.nroSolicitud AS NROSOLICITUD, x.audLogin AS LOGIN, "
                + " alOri.razonSocial as ALMORI_NOMBRE, alDes.razonSocial AS ALMDES_NOMBRE, "
                + " polOri.descripcion AS POLORI_NOMBRE, polDes.descripcion AS POLDES_NOMBRE, "
                + " aduOri.razonSocial AS ADUAORI_NOMBRE, aduDes.razonSocial AS ADUADES_NOMBRE, "
                + " pueOri.nombre AS PUEORI_NOMBRE, pueDes.nombre AS PUEDES_NOMBRE, "
                + " lugOri.nombre AS LUGORI_NOMBRE, lugDes.nombre AS LUGDES_NOMBRE, "
                + " x.estado.codProg AS ESTADO_CODPROG, "
                + " CONCAT(x.empresaId.apePat,' ',x.empresaId.apeMat,' ',x.empresaId.nombres) AS NOMBRE_DNI, "
                + " x.empresaId.tipoDoc.codProg AS TIPO_DOC_CODPROG, "
                + " perFabOri.rznSocial as FABORI_NOMBRE, "
                + " perFabDes.rznSocial as FABDES_NOMBRE, "
                + " arrOri.rznSocial as ALQORI_NOMBRE, "
                + " arrDes.rznSocial as ALQDES_NOMBRE, "
                + " x.tipoTramite.codProg AS TIPO_TRAMITE, "
                + " x.empresaId.id AS EMPRESA_ID "
                + " from EppGteRegistro x "
                + " inner join x.eppGteExplosivoSolicitaList es "
                + " left join x.origenAlmacen alOri "
                + " left join x.destinoAlmacen alDes "
                + " left join x.origenPolvorin polOri "
                + " left join polOri.propietarioId polProOri "
                + " left join x.destinoPolvorin polDes "
                + " left join x.origenAlmacenAduana aduOri "
                + " left join x.destinoAlmacenAduana aduDes "
                + " left join x.puertoAduaneroOrigen pueOri "
                + " left join x.puertoAduaneroDestino pueDes "
                + " left join x.lugarUsoOrigen lugOri "
                + " left join x.lugarUsoDestino lugDes "
                + " left join x.origenFabrica fabOri "
                + " left join fabOri.personaId perFabOri "                
                + " left join x.origenAlquilerId AlqOri "
                + " left join AlqOri.arrendatariaId arrOri "
                + " left join x.destinoFabrica fabDes "
                + " left join fabDes.personaId perFabDes "
                + " left join x.destinoAlquilerId AlqDes "
                + " left join AlqDes.arrendatariaId arrDes "
                + "where "
                + "x.activo = 1 and "
                + "x.tipoProId.codProg = 'TP_PRTUP_GUIS' and "
                + " x.tipoTramite.codProg in ('TP_RGUIA_ADQUI1','TP_RGUIA_COMFAB') and " // solo guias tipo 1 y 6
                + " ( (polProOri.id = :empresa and AlqOri.id is null) or "
                + "   (perFabOri.id = :empresa and AlqOri.id is null ) or (arrOri.id = :empresa and AlqOri.id is not null) ) and "
                + "(x.estado.id = :estado or :estado is null) and "
                + "(x.empresaId.rznSocial like :nomCompra or :nomCompra is null) and "
                + "(x.empresaId.ruc like :rucCompra or :rucCompra is null) and "
                + "(polDes.descripcion like :destino or lugDes.nombre like :destino or :destino is null) and "
                + "(es.explosivoId.nombre like :explosivo or :explosivo is null) ";
        
        //ORDENANDO LISTADO
        jpql = jpql + " order by x.id desc";
        //GENERANDO QUERY
        Query query = em.createQuery(jpql);
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter("empresa", empresa.getId());
        query.setParameter("estado", estado == null ? null : estado.getId());
        query.setParameter("nomCompra", nomCompra == null ? null : ("%" + nomCompra + "%"));
        query.setParameter("rucCompra", rucCompra == null ? null : ("%" + rucCompra + "%"));
        query.setParameter("destino", destino == null ? null : ("%" + destino + "%"));
        query.setParameter("explosivo", explosivo == null ? null : ("%" + explosivo + "%"));

        return query.getResultList();
    }

    /**
     * LIBRO USO
     *
     * @param lugarUso
     * @return
     */
    public List<EppRegistroGuiaTransito> listRegistroGuiaTransitoxlugarUsoDestinino(EppLugarUso lugarUso) {
        Query q = em.createQuery("SELECT p FROM EppRegistroGuiaTransito p WHERE p.lugarUsoDestino = :lugar AND p.registroId.estado.codProg = 'TP_REGEV_FIN' "
                + " and p.activo = 1 and p.lugarUsoDestino is not null");
        q.setParameter("lugar", lugarUso);
        return q.getResultList();
    }

    public List<EppRegistroGuiaTransito> buscarGuiasReporte(EppResolucion prs) {
        String jpql = "select x from EppRegistroGuiaTransito x "
                + "inner join x.registroId r "
                + "where "
                + "r.estado.codProg in ('TP_REGEV_FIN','TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and "
                + "x.tipoTramite.codProg in ('TP_RGUIA_ADQUI1','TP_RGUIA_EXTOR') and "
                + "r.activo = 1 and x.activo = 1 and "
                + "(x.resolucionId.id = :rs)";

        //INCLUYENDO GUIAS DE DEVOLUCION
        jpql = jpql + "union "
                + "select x from EppRegistroGuiaTransito x "
                + "inner join x.registroId r "
                + "where "
                + "r.estado.codProg in ('TP_REGEV_FIN','TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and "
                + "x.tipoTramite.codProg in ('TP_RGUIA_DEVOL') and "
                + "r.activo = 1 and x.activo = 1 and "
                + "(x.resolucionId.registroId.eppRegistroGuiaTransito.resolucionId.id = :rs)";

        Query q = em.createQuery(jpql);
        q.setParameter("rs", prs.getId());        
        return q.getResultList();
    }

    /**
     * Metodo para calcular el saldo en caliente de las guias de tipo 1
     *
     * @param pRes
     * @param pInExplo
     * @return
     */
    public double calculaSaldoExplosivoRgGee(EppResolucion pRes, EppExplosivo pInExplo) {
        try {
            double cantidadTotalGTtipo1 = 0;
            double cantidadTotalGTtipo2 = 0;
            double cantidadTotalGTtipo3 = 0;
            double cantidadTotalGTtipo4 = 0;
            double cantidadTotalGTtipo5 = 0;            
            double cantidadTotalGTtipo6 = 0;
            double cantidadTotalGTtipo7 = 0;
            double cantidadTotalGTtipo8 = 0;
            double cantidadTotalGTtipo9 = 0;
            double cantidadTotalGTtipo10 = 0;
            double cantidadTotalGTtipo12 = 0;
           
            String jpql1 = "select d.cantidad \"TOTAL1\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro r \n"
                    + "inner join bdintegrado.epp_resolucion res on res.registro_id = r.id \n"
                    + "inner join bdintegrado.epp_detalle_aut_uso d on d.registro_id = r.id \n"
                    + "where \n"
                    + "d.activo = 1 and \n"
                    + "res.id = ?1 and\n"
                    + "d.explosivo_id = ?2  ";

            String jpql2 = "select sum(es.cantidad) \"TOTAL2\"\n"
                    + "from \n"
                    + "--REGISTRO DE LA GUIA DE TRANSITO\n"
                    + "bdintegrado.epp_registro r \n"
                    + "--ESTADO DEL REGISTRO DE LA GUIA DE TRANSITO\n"
                    + "inner join bdintegrado.tipo_explosivo ter on r.estado = ter.id\n"
                    + "--GUIA DE TRANSITO\n"
                    + "inner join bdintegrado.epp_registro_guia_transito rgt \n"
                    + "on rgt.registro_id = r.id\n"
                    + "--RESOLUCION UTILIZADA EN LA GUIA DE TRANSITO\n"
                    + "inner join bdintegrado.epp_resolucion res \n"
                    + "on rgt.resolucion_id = res.id \n"
                    + "--REGISTRO DE LA RESOLUCION UTILIZADA EN LA GUIA DE TRANSITO\n"
                    + "inner join bdintegrado.epp_registro r_res \n"
                    + "on res.registro_id = r_res.id \n"
                    + "--ESTADO DEL REGISTRO DE LA RESOLUCION UTILIZADA EN LA GUIA DE TRANSITO\n"
                    + "inner join bdintegrado.tipo_explosivo te \n"
                    + "on r_res.estado = te.id\n"
                    + "--TIPO DE TRAMITE DE LA GUIA DE TRANSITO\n"
                    + "inner join bdintegrado.tipo_explosivo te2 \n"
                    + "on rgt.tipo_tramite = te2.id\n"
                    + "--DETALLE DE LA GUIA DE TRANSITO\n"
                    + "inner join bdintegrado.epp_explosivo_solicitado es \n"
                    + "on es.registro_id = rgt.id \n"
                    + "where \n"
                    + "r.activo = 1 and \n"
                    + "--te.cod_prog not in 'TP_REGEV_ANU' and \n"
                    + "te2.cod_prog = 'TP_RGUIA_ADQUI1' and \n"
                    + "ter.cod_prog not in ('TP_REGEV_ANU') and --'TP_REGEV_EXTPAR',\n"
                    + "es.activo = 1 and \n"
                    + "res.id = ?1 and \n"
                    + "es.explosivo_id = ?2";

            String jpql3 = "select sum(e.cantidad_extornada) \"TOTAL3\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                    + "inner join bdintegrado.epp_resolucion res on rgt1.resolucion_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on rgt1.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on r9.estado = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo tt on tt.id = rgt1.tipo_tramite \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado e on rgt1.id = e.registro_id \n"
                    + "where \n"
                    + "e.activo = 1 and \n"
                    + "t9.cod_prog in ('TP_REGEV_EXT','TP_REGEV_EXTPAR','TP_REGEV_EXTTOT') and \n"
                    + "res.id = ?1 and \n"
                    + "e.explosivo_id = ?2 and \n"
                    + "tt.cod_prog = 'TP_RGUIA_ADQUI1' ";

            String jpql4 = "select sum(e.cantidad) \"TOTAL4\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado e on rgt1.id = e.registro_id \n"
                    + "inner join bdintegrado.tipo_explosivo te1 on rgt1.tipo_tramite =te1.id \n"
                    + "inner join bdintegrado.epp_resolucion res1 on rgt1.resolucion_id = res1.id \n"
                    + "inner join bdintegrado.epp_registro r_gt on rgt1.registro_id = r_gt.id\n"
                    + "inner join bdintegrado.tipo_explosivo t_r_gt on r_gt.estado = t_r_gt.id \n"
                    + "--REFERENCIANDO A REGISTRO DE LA GUIA REFERENCIADA\n"
                    + "inner join bdintegrado.epp_registro rx on res1.registro_id = rx.id \n"
                    + "inner join bdintegrado.epp_registro_guia_transito rgtx on rgtx.registro_id = rx.id \n"
                    + "inner join bdintegrado.epp_resolucion resx on rgtx.resolucion_id= resx.id\n"
                    + "where \n"
                    + "e.activo = 1 and \n"
                    + "te1.cod_prog = 'TP_RGUIA_DEVOL' and \n"
                    + "t_r_gt.cod_prog = 'TP_REGEV_EXT' and "
                    + "resx.id = ?1 and \n"
                    + "e.explosivo_id = ?2";

            // Para descuento de guia
            String jpql5 = "select sum(e.cantidad) \"TOTAL5\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                    + "inner join bdintegrado.epp_resolucion res on rgt1.resolucion_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on rgt1.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on rgt1.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado e on rgt1.id = e.registro_id \n"
                    + "where \n"
                    + "rgt1.activo = 1 and r9.activo = 1 and e.activo = 1 and \n"
                    + "t9.cod_prog in ('TP_RGUIA_DSCTO') and \n"
                    + "res.id = ?1 and \n"
                    + "e.explosivo_id = ?2";
            
            // Vinculada de traslado de saldos (Destino)
            String jpql6 = "select sum(e.cantidad) \"TOTAL6\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                    + "inner join bdintegrado.epp_resolucion res on rgt1.resolucion_dest_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on rgt1.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on rgt1.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo te on te.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado e on rgt1.id = e.registro_id \n"
                    + "where \n"
                    + "e.activo = 1 and \n"
                    + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                    + "t9.cod_prog in ('TP_RGUIA_VINC') and \n"
                    + "res.id = ?1 and \n"
                    + "e.explosivo_id = ?2";
            
            // Para descuento de guia (Origen)
            String jpql7 = "select sum(e.cantidad) \"TOTAL7\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                    + "inner join bdintegrado.epp_resolucion res on rgt1.resolucion_dest_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on rgt1.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on rgt1.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo est on est.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado e on rgt1.id = e.registro_id \n"
                    + "where \n"
                    + "rgt1.activo = 1 and r9.activo = 1 and e.activo = 1 and est.cod_prog = 'TP_REGEV_FIN' and \n"
                    + "t9.cod_prog = 'TP_RGUIA_TRANSF' and \n"
                    + "res.id = ?1 and \n"
                    + "e.explosivo_id = ?2";
            
            // Para descuento de guia
            String jpql8 = "select sum(e.cantidad) \"TOTAL8\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                    + "inner join bdintegrado.epp_resolucion res on rgt1.resolucion_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on rgt1.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on rgt1.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo est on est.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado e on rgt1.id = e.registro_id \n"
                    + "where \n"
                    + "rgt1.activo = 1 and r9.activo = 1 and e.activo = 1 and est.cod_prog = 'TP_REGEV_FIN' and \n"
                    + "t9.cod_prog = 'TP_RGUIA_TRANSF' and \n"
                    + "res.id = ?1 and \n"
                    + "e.explosivo_id = ?2";
            
            // Para descuento de guia especial (Origen)
            String jpql9 = "select sum(es.cantidad) \"TOTAL9\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                    + "inner join bdintegrado.epp_resolucion res on rgt1.resolucion_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on rgt1.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on rgt1.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo est on est.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado es on rgt1.id = es.registro_id \n"
                    + "inner join bdintegrado.epp_homologacion_reg e on r9.id = e.registro_id \n"
                    + "where \n"
                    + "rgt1.activo = 1 and r9.activo = 1 and e.activo = 1 and est.cod_prog = 'TP_REGEV_FIN' and \n"
                    + "t9.cod_prog = 'TP_RGUIA_DTPARC' and \n"
                    + "res.id = ?1 and \n"
                    + "e.explo_vencido = ?2";
            
            // Para descuento de guia especial (destino)
            String jpql10 = "select sum(es.cantidad) \"TOTAL10\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito rgt1 \n"
                    + "inner join bdintegrado.epp_resolucion res on rgt1.resolucion_dest_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on rgt1.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on rgt1.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo est on est.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado es on rgt1.id = es.registro_id \n"
                    + "inner join bdintegrado.epp_homologacion_reg e on r9.id = e.registro_id \n"
                    + "where \n"
                    + "rgt1.activo = 1 and r9.activo = 1 and e.activo = 1 and est.cod_prog = 'TP_REGEV_FIN' and \n"
                    + "t9.cod_prog = 'TP_RGUIA_DTPARC' and \n"
                    + "res.id = ?1 and \n"
                    + "e.explo_vigente = ?2 and es.explosivo_id=?2";
            
             String jpql12 = "select sum(es.cantidad) \"TOTAL12\"\n"
                    + "from \n"
                    + "bdintegrado.epp_registro r \n"
                    + "inner join bdintegrado.tipo_explosivo ter on r.estado = ter.id\n"
                    + "inner join bdintegrado.epp_registro_guia_transito rgt \n"
                    + "on rgt.registro_id = r.id\n"
                    + "inner join bdintegrado.epp_resolucion res \n"
                    + "on rgt.resolucion_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r_res \n"
                    + "on res.registro_id = r_res.id \n"
                    + "inner join bdintegrado.tipo_explosivo te \n"
                    + "on r_res.estado = te.id\n"
                    + "inner join bdintegrado.tipo_explosivo te2 \n"
                    + "on rgt.tipo_tramite = te2.id\n"
                    + "inner join bdintegrado.epp_explosivo_solicitado es \n"
                    + "on es.registro_id = rgt.id \n"
                    + "where \n"
                    + "r.activo = 1 and \n"
                    + "te2.cod_prog = 'TP_RGUIA_INTUSO' and \n"
                    + "ter.cod_prog not in ('TP_REGEV_ANU') and "
                    + "es.activo = 1 and \n"
                    + "res.id = ?1 and \n"
                    + "es.explosivo_id = ?2";
            
            Query q1 = em.createNativeQuery(jpql1);
            Query q2 = em.createNativeQuery(jpql2);
            Query q3 = em.createNativeQuery(jpql3);
            Query q4 = em.createNativeQuery(jpql4);
            Query q5 = em.createNativeQuery(jpql5);
            Query q6 = em.createNativeQuery(jpql6);
            Query q7 = em.createNativeQuery(jpql7);
            Query q8 = em.createNativeQuery(jpql8);
            Query q9 = em.createNativeQuery(jpql9);
            Query q10 = em.createNativeQuery(jpql10);
            Query q12 = em.createNativeQuery(jpql12);
            
            q1.setParameter(1, pRes.getId());
            q1.setParameter(2, pInExplo.getId());

            q2.setParameter(1, pRes.getId());
            q2.setParameter(2, pInExplo.getId());

            q3.setParameter(1, pRes.getId());
            q3.setParameter(2, pInExplo.getId());

            q4.setParameter(1, pRes.getId());
            q4.setParameter(2, pInExplo.getId());
            
            q5.setParameter(1, pRes.getId());
            q5.setParameter(2, pInExplo.getId());
            
            q6.setParameter(1, pRes.getId());
            q6.setParameter(2, pInExplo.getId());
            
            q7.setParameter(1, pRes.getId());
            q7.setParameter(2, pInExplo.getId());
            
            q8.setParameter(1, pRes.getId());
            q8.setParameter(2, pInExplo.getId());
            
            q9.setParameter(1, pRes.getId());
            q9.setParameter(2, pInExplo.getId());
            
            q10.setParameter(1, pRes.getId());
            q10.setParameter(2, pInExplo.getId());
            
            q12.setParameter(1, pRes.getId());
            q12.setParameter(2, pInExplo.getId());
            
            if (!q1.getResultList().isEmpty() && !(q1.getResultList().get(0) == null)) {
                cantidadTotalGTtipo1 = Double.valueOf(q1.getResultList().get(0).toString());
            }

            if (!q2.getResultList().isEmpty() && !(q2.getResultList().get(0) == null)) {
                cantidadTotalGTtipo2 = Double.valueOf(q2.getResultList().get(0).toString());
            }

            if (!q3.getResultList().isEmpty() && !(q3.getResultList().get(0) == null)) {
                cantidadTotalGTtipo3 = Double.valueOf(q3.getResultList().get(0).toString());
            }

            if (!q4.getResultList().isEmpty() && !(q4.getResultList().get(0) == null)) {
                cantidadTotalGTtipo4 = Double.valueOf(q4.getResultList().get(0).toString());
            }
            if (!q5.getResultList().isEmpty() && !(q5.getResultList().get(0) == null)) {
                cantidadTotalGTtipo5 = Double.valueOf(q5.getResultList().get(0).toString());
            }
            if (!q6.getResultList().isEmpty() && !(q6.getResultList().get(0) == null)) {
                cantidadTotalGTtipo6 = Double.valueOf(q6.getResultList().get(0).toString());
            }
            if (!q7.getResultList().isEmpty() && !(q7.getResultList().get(0) == null)) {
                cantidadTotalGTtipo7 = Double.valueOf(q7.getResultList().get(0).toString());
            }
            if (!q8.getResultList().isEmpty() && !(q8.getResultList().get(0) == null)) {
                cantidadTotalGTtipo8 = Double.valueOf(q8.getResultList().get(0).toString());
            }
            if (!q9.getResultList().isEmpty() && !(q9.getResultList().get(0) == null)) {
                cantidadTotalGTtipo9 = Double.valueOf(q9.getResultList().get(0).toString());
            }
            if (!q10.getResultList().isEmpty() && !(q10.getResultList().get(0) == null)) {
                cantidadTotalGTtipo10 = Double.valueOf(q10.getResultList().get(0).toString());
            }

            if (!q12.getResultList().isEmpty() && !(q12.getResultList().get(0) == null)) {
                cantidadTotalGTtipo12 = Double.valueOf(q12.getResultList().get(0).toString());
            }
            
            double saldo = (cantidadTotalGTtipo1 - cantidadTotalGTtipo2 + cantidadTotalGTtipo3 + cantidadTotalGTtipo4 - 
                            cantidadTotalGTtipo5 - cantidadTotalGTtipo6 - cantidadTotalGTtipo7 - cantidadTotalGTtipo8 - 
                            cantidadTotalGTtipo9 - cantidadTotalGTtipo10 - cantidadTotalGTtipo12);
            if(saldo < 0){
                saldo = 0;
            }
            return saldo;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<EppExplosivo> listaExplosivosXRgComercializacionExp(SbPersonaGt prs) {
        String jpql = "select distinct(pel.explosivoId) from EppRegistro r \n"
                + "inner join r.eppPlantaExplosivoList pel \n"
                + "where \n"
                + "r.activo = 1 and \n"
                + "pel.activo = 1 and \n"
                + "r.estado.codProg = 'TP_REGEV_FIN' and \n"
                + "r.tipoProId.codProg in ('TP_PRTUP_COM','TP_PRTUP_ACOM') and \n"
                + "r.empresaId.id = :emp";
        Query q = em.createQuery(jpql);
        q.setParameter("emp", prs.getId());
        return q.getResultList();
    }

    
    public EppRegistroGuiaTransito obtenerGuiTransitoById(Long id) {
        String jpql = "select e from EppRegistroGuiaTransito e "+
                        " where e.id = :id";

        //GENERANDO QUERY
        Query query = em.createQuery(jpql);        
        query.setParameter("id", id);
        
        if(query.getResultList().isEmpty()){
           return null; 
        }
        return (EppRegistroGuiaTransito) query.getSingleResult();
    }
    
    public double calculaSaldoExplosivoRgIs(EppResolucion pRes, EppExplosivo pInExplo) {

        double cantidadTotalGTtipo1 = 0;
        double cantidadTotalGTtipo2 = 0;
        double cantidadTotalGTtipo3 = 0;
        double cantidadTotalGTtipo4 = 0;

        String jpql1 = "select d.cantidad_autorizada \"TOTAL1\" \n"
                + "from bdintegrado.epp_resolucion res \n"
                + "inner join bdintegrado.epp_registro r on res.registro_id = r.id \n"
                + "inner join BDINTEGRADO.EPP_REGISTRO_INT_SAL ris on ris.registro_id = r.id \n"
                + "inner join BDINTEGRADO.EPP_DETALLE_INT_SAL d on d.registro_id = ris.id \n"
                + "where "
                + "d.activo = 1 and "
                + "res.id = ?1 and\n"
                + "d.explosivo_id = ?2";

        String jpql2 = "select sum(es.cantidad) \"TOTAL2\"\n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.tipo_explosivo tt on rgt.tipo_tramite = tt.id \n"
                + "inner join bdintegrado.epp_registro rx on rgt.registro_id = rx.id \n"
                + "inner join bdintegrado.tipo_explosivo tx on rx.estado = tx.id \n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_registro r_res on res.registro_id = r_res.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r_res.estado = te.id\n"
                + "inner join bdintegrado.epp_explosivo_solicitado es on es.registro_id = rgt.id \n"
                + "where \n"
                + "tx.cod_prog not in 'TP_REGEV_ANU' and \n"
                + "tt.cod_prog not in ('TP_RGUIA_DSCTO', 'TP_RGUIA_TRANSF', 'TP_RGUIA_DTPARC') and \n"
                + "es.activo = 1 and \n"
                + "res.id = ?1 and \n"
                + "es.explosivo_id = ?2";

        String jpql3 = "select sum(es.cantidad) \"TOTAL3\"\n"
                + "from \n"
                + "bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_registro r2 on res.registro_id = r2.id\n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt2 on rgt2.registro_id = r2.id\n"
                + "inner join bdintegrado.epp_resolucion res2 on rgt2.resolucion_id = res2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado es on es.registro_id = rgt.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r.estado = te.id \n"
                + "where \n"
                + "te.cod_prog in ('TP_REGEV_EXT','TP_REGEV_EXTPAR','TP_REGEV_EXTTOT') and \n"
                + "res2.id = ?1 and \n"
                + "es.explosivo_id = ?2";

        String jpql4 = "select sum(es.cantidad_extornada) \"TOTAL4\"\n"
                + "from \n"
                + "bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado es on es.registro_id = rgt.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r.estado = te.id \n"
                + "where \n"
                + "te.cod_prog in ('TP_REGEV_EXT','TP_REGEV_EXTPAR','TP_REGEV_EXTTOT') and \n"
                + "res.id = ?1 and \n"
                + "es.explosivo_id = ?2";
        
        Query q1 = em.createNativeQuery(jpql1);
        Query q2 = em.createNativeQuery(jpql2);
        Query q3 = em.createNativeQuery(jpql3);
        Query q4 = em.createNativeQuery(jpql4);

        q1.setParameter(1, pRes.getId());
        q1.setParameter(2, pInExplo.getId());

        q2.setParameter(1, pRes.getId());
        q2.setParameter(2, pInExplo.getId());

        q3.setParameter(1, pRes.getId());
        q3.setParameter(2, pInExplo.getId());
        
        q4.setParameter(1, pRes.getId());
        q4.setParameter(2, pInExplo.getId());

        if (!q1.getResultList().isEmpty() && !(q1.getResultList().get(0) == null)) {
            cantidadTotalGTtipo1 = Double.valueOf(q1.getResultList().get(0).toString());
        }

        if (!q2.getResultList().isEmpty() && !(q2.getResultList().get(0) == null)) {
            cantidadTotalGTtipo2 = Double.valueOf(q2.getResultList().get(0).toString());
        }

        if (!q3.getResultList().isEmpty() && !(q3.getResultList().get(0) == null)) {
            cantidadTotalGTtipo3 = Double.valueOf(q3.getResultList().get(0).toString());
        }
        
        if (!q4.getResultList().isEmpty() && !(q4.getResultList().get(0) == null)) {
            cantidadTotalGTtipo4 = Double.valueOf(q4.getResultList().get(0).toString());
        }
        double saldo = (cantidadTotalGTtipo1 - (cantidadTotalGTtipo2 - cantidadTotalGTtipo3) + cantidadTotalGTtipo4);
        if(saldo < 0){
            saldo = 0;
        }
        return saldo;
    }
    
    public Double calculaSaldoExplosivoSumatoriaPolvLuu(EppResolucion pres, EppExplosivo pInExplo, EppPolvorin ppolv) {

        double cantidadTotalGTtipo1 = 0;
        double cantidadTotalGTtipo2 = 0;
        double cantidadTotalGTtipo3 = 0;
        double cantidadTotalGTtipo4 = 0;
        double cantidadTotalGTtipo5 = 0;
        double cantidadTotalGTtipo6 = 0;
        double cantidadTotalGTtipo7 = 0;
        double cantidadTotalGTtipo8 = 0;
        double cantidadTotalGTtipo9 = 0;
        double cantidadTotalGTtipo10 = 0;
        double cantidadTotalGTtipo11 = 0;
        double cantidadTotalGTtipo12 = 0;
        double cantidadTotalGTtipo13 = 0;
        double cantidadTotalGTtipo14 = 0;
        double cantidadTotalGTtipo15 = 0;

        String jpql1 = "select sum(e.cantidad) \"TOTALT1\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI1' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.destino_polvorin = ?3";

        String jpql2 = "select sum(e.cantidad) \"TOTALT2\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI2' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.destino_polvorin = ?3)";

        String jpql3 = "select sum(e.cantidad) \"TOTALT3\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_INTER' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.destino_polvorin = ?3)";

        String jpql4 = "select sum(e.cantidad) \"TOTALT4\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI2' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin = ?3)";

        String jpql5 = "select sum(e.cantidad) \"TOTALT5\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_INTER' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin = ?3)";

        String jpql6 = "select sum(e.cantidad) \"TOTALT6\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on r.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_registro r2 on res.registro_id = r2.id \n"
                + "inner join bdintegrado.epp_registro_guia_transito r3 on r3.registro_id = r2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te.cod_prog = 'TP_REGEV_EXT' and \n"
                + "te2.cod_prog = 'TP_RGUIA_DEVOL' and \n"
                + "r3.resolucion_id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin = ?3)";

        String jpql7 = "select sum(e.cantidad_extornada) \"TOTALT7\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_ADQUI1'\n"
                + "and e.activo = 1\n"
                + "and res.id = ?1 \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.destino_polvorin = ?3)";

        String jpql8 = "select sum(e.cantidad_extornada) \"TOTALT8\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_ADQUI2'\n"
                + "and e.activo = 1\n"
                + "and res.id = ?1 \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.origen_polvorin = ?3)";

        String jpql9 = "select sum(e.cantidad_extornada) \"TOTAL_9\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_ADQUI2'\n"
                + "and e.activo = 1\n"
                + "and res.id = ?1 \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.destino_polvorin = ?3)";

        String jpql10 = "select sum(e.cantidad_extornada) \"TOTAL_10\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_INTER'\n"
                + "and e.activo = 1\n"
                + "and res.id = ?1 \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.origen_polvorin = ?3)";

        String jpql11 = "select sum(e.cantidad_extornada) \"TOTAL_11\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_INTER'\n"
                + "and e.activo = 1\n"
                + "and res.id = ?1 \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.destino_polvorin = ?3)";
        
        String jpql12 = "select sum(e.cantidad) \"TOTALT12\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin = ?3)";
        
        String jpql13 = "select sum(e.cantidad) \"TOTALT13\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_DEST_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.destino_polvorin = ?3)";

        String jpql14 = "select sum(e.cantidad_extornada) \"TOTALT14\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog in ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin = ?3)";
        
        String jpql15 = "select sum(e.cantidad_extornada) \"TOTALT15\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_DEST_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog in ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.destino_polvorin = ?3)";
        
        Query q1 = em.createNativeQuery(jpql1);
        Query q2 = em.createNativeQuery(jpql2);
        Query q3 = em.createNativeQuery(jpql3);
        Query q4 = em.createNativeQuery(jpql4);
        Query q5 = em.createNativeQuery(jpql5);
        Query q6 = em.createNativeQuery(jpql6);
        Query q7 = em.createNativeQuery(jpql7);
        Query q8 = em.createNativeQuery(jpql8);
        Query q9 = em.createNativeQuery(jpql9);
        Query q10 = em.createNativeQuery(jpql10);
        Query q11 = em.createNativeQuery(jpql11);
        Query q12 = em.createNativeQuery(jpql12);
        Query q13 = em.createNativeQuery(jpql13);
        Query q14 = em.createNativeQuery(jpql14);
        Query q15 = em.createNativeQuery(jpql15);

        q1.setParameter(1, pres.getId());
        q1.setParameter(2, pInExplo.getId());
        q1.setParameter(3, ppolv.getId());

        q2.setParameter(1, pres.getId());
        q2.setParameter(2, pInExplo.getId());
        q2.setParameter(3, ppolv.getId());

        q3.setParameter(1, pres.getId());
        q3.setParameter(2, pInExplo.getId());
        q3.setParameter(3, ppolv.getId());

        q4.setParameter(1, pres.getId());
        q4.setParameter(2, pInExplo.getId());
        q4.setParameter(3, ppolv.getId());

        q5.setParameter(1, pres.getId());
        q5.setParameter(2, pInExplo.getId());
        q5.setParameter(3, ppolv.getId());

        q6.setParameter(1, pres.getId());
        q6.setParameter(2, pInExplo.getId());
        q6.setParameter(3, ppolv.getId());

        q7.setParameter(1, pres.getId());
        q7.setParameter(2, pInExplo.getId());
        q7.setParameter(3, ppolv.getId());

        q8.setParameter(1, pres.getId());
        q8.setParameter(2, pInExplo.getId());
        q8.setParameter(3, ppolv.getId());

        q9.setParameter(1, pres.getId());
        q9.setParameter(2, pInExplo.getId());
        q9.setParameter(3, ppolv.getId());

        q10.setParameter(1, pres.getId());
        q10.setParameter(2, pInExplo.getId());
        q10.setParameter(3, ppolv.getId());

        q11.setParameter(1, pres.getId());
        q11.setParameter(2, pInExplo.getId());
        q11.setParameter(3, ppolv.getId());
        
        q12.setParameter(1, pres.getId());
        q12.setParameter(2, pInExplo.getId());
        q12.setParameter(3, ppolv.getId());
        
        q13.setParameter(1, pres.getId());
        q13.setParameter(2, pInExplo.getId());
        q13.setParameter(3, ppolv.getId());
        
        q14.setParameter(1, pres.getId());
        q14.setParameter(2, pInExplo.getId());
        q14.setParameter(3, ppolv.getId());
        
        q15.setParameter(1, pres.getId());
        q15.setParameter(2, pInExplo.getId());
        q15.setParameter(3, ppolv.getId());

        if (!q1.getResultList().isEmpty() && !(q1.getResultList().get(0) == null)) {
            cantidadTotalGTtipo1 = Double.valueOf(q1.getResultList().get(0).toString());
        }

        if (!q2.getResultList().isEmpty() && !(q2.getResultList().get(0) == null)) {
            cantidadTotalGTtipo2 = Double.valueOf(q2.getResultList().get(0).toString());
        }

        if (!q3.getResultList().isEmpty() && !(q3.getResultList().get(0) == null)) {
            cantidadTotalGTtipo3 = Double.valueOf(q3.getResultList().get(0).toString());
        }

        if (!q4.getResultList().isEmpty() && !(q4.getResultList().get(0) == null)) {
            cantidadTotalGTtipo4 = Double.valueOf(q4.getResultList().get(0).toString());
        }

        if (!q5.getResultList().isEmpty() && !(q5.getResultList().get(0) == null)) {
            cantidadTotalGTtipo5 = Double.valueOf(q5.getResultList().get(0).toString());
        }

        if (!q6.getResultList().isEmpty() && !(q6.getResultList().get(0) == null)) {
            cantidadTotalGTtipo6 = Double.valueOf(q6.getResultList().get(0).toString());
        }

        if (!q7.getResultList().isEmpty() && !(q7.getResultList().get(0) == null)) {
            cantidadTotalGTtipo7 = Double.valueOf(q7.getResultList().get(0).toString());
        }

        if (!q8.getResultList().isEmpty() && !(q8.getResultList().get(0) == null)) {
            cantidadTotalGTtipo8 = Double.valueOf(q8.getResultList().get(0).toString());
        }

        if (!q9.getResultList().isEmpty() && !(q9.getResultList().get(0) == null)) {
            cantidadTotalGTtipo9 = Double.valueOf(q9.getResultList().get(0).toString());
        }

        if (!q10.getResultList().isEmpty() && !(q10.getResultList().get(0) == null)) {
            cantidadTotalGTtipo10 = Double.valueOf(q10.getResultList().get(0).toString());
        }

        if (!q11.getResultList().isEmpty() && !(q11.getResultList().get(0) == null)) {
            cantidadTotalGTtipo11 = Double.valueOf(q11.getResultList().get(0).toString());
        }
        
        if (!q12.getResultList().isEmpty() && !(q12.getResultList().get(0) == null)) {
            cantidadTotalGTtipo12 = Double.valueOf(q12.getResultList().get(0).toString());
        }
        
        if (!q13.getResultList().isEmpty() && !(q13.getResultList().get(0) == null)) {
            cantidadTotalGTtipo13 = Double.valueOf(q13.getResultList().get(0).toString());
        }
        
        if (!q14.getResultList().isEmpty() && !(q14.getResultList().get(0) == null)) {
            cantidadTotalGTtipo14 = Double.valueOf(q14.getResultList().get(0).toString());
        }
        
        if (!q15.getResultList().isEmpty() && !(q15.getResultList().get(0) == null)) {
            cantidadTotalGTtipo15 = Double.valueOf(q15.getResultList().get(0).toString());
        }
        double saldo = cantidadTotalGTtipo1
                        + cantidadTotalGTtipo2
                        + cantidadTotalGTtipo3
                        - cantidadTotalGTtipo4
                        - cantidadTotalGTtipo5
                        - cantidadTotalGTtipo6
                        - cantidadTotalGTtipo7
                        + cantidadTotalGTtipo8
                        - cantidadTotalGTtipo9
                        + cantidadTotalGTtipo10
                        - cantidadTotalGTtipo11
                        - cantidadTotalGTtipo12
                        + cantidadTotalGTtipo13
                        + cantidadTotalGTtipo14
                        - cantidadTotalGTtipo15;
        return saldo;
    }
    
    public Double calculaSaldoExplosivoTipo2PolvLuu(EppRegistro pres, EppExplosivo pInExplo, EppPolvorin ppolv) {
        double saldo = 0.0, saldoTemp = 0.0;
        EppRegistro regTemp = pres;
        boolean flagCalculaSaldo = true;
        
        while(flagCalculaSaldo){
            saldoTemp = calculaSaldoExplosivoSumatoriaPolvLuu(
                                        regTemp.getEppResolucion(),
                                        pInExplo,
                                        ppolv);
            if(regTemp.getRegistroId() != null){
                regTemp = regTemp.getRegistroId();
                if(regTemp.getEppResolucion() == null){
                    flagCalculaSaldo = false;
                }
            }else{
                flagCalculaSaldo = false;
            }
            saldo += saldoTemp;
        }
        
        // Sumando Internamientos
        //saldo += calculaSaldoExplosivoTipo2Internamientos(pres.getEmpresaId(),pInExplo, ppolv);
        
        if(saldo < 0){
            saldo = 0;
        }
        
        return saldo;
    }
    
    public Double calculaSaldoExplosivoTipo2Internamientos(SbPersonaGt empresa, EppExplosivo pInExplo, EppPolvorin ppolv) {

        double cantidadTotalGTtipo1 = 0;
        double cantidadTotalGTtipo2 = 0;

        String jpql1 = "select sum(e.cantidad) \"TOTALT1\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_registro reg on reg.id = r.registro_id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_INT' and \n"
                + "reg.empresa_id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.destino_polvorin = ?3";


        String jpql2 = "select sum(e.cantidad_extornada) \"TOTALT2\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro reg on reg.id = r.registro_id \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_INT' \n"
                + "and e.activo = 1\n"
                + "and reg.empresa_id = ?1 \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.destino_polvorin = ?3)";

        Query q1 = em.createNativeQuery(jpql1);
        Query q2 = em.createNativeQuery(jpql2);

        q1.setParameter(1, empresa.getId());
        q1.setParameter(2, pInExplo.getId());
        q1.setParameter(3, ppolv.getId());

        q2.setParameter(1, empresa.getId());
        q2.setParameter(2, pInExplo.getId());
        q2.setParameter(3, ppolv.getId());
        

        if (!q1.getResultList().isEmpty() && !(q1.getResultList().get(0) == null)) {
            cantidadTotalGTtipo1 = Double.valueOf(q1.getResultList().get(0).toString());
        }

        if (!q2.getResultList().isEmpty() && !(q2.getResultList().get(0) == null)) {
            cantidadTotalGTtipo2 = Double.valueOf(q2.getResultList().get(0).toString());
        }

        double saldo = cantidadTotalGTtipo1
                        - cantidadTotalGTtipo2;

        return saldo;
    }
    
    public Double calculaSaldoExplosivoSumatoriaLuuPolv(EppResolucion pres, EppExplosivo pInExplo, EppLugarUso plu, EppLugarUsoUbigeo pluu) {

        double cantidadTotalGTtipo1 = 0;
        double cantidadTotalGTtipo2 = 0;
        double cantidadTotalGTtipo3 = 0;
        double cantidadTotalGTtipo4 = 0;
        double cantidadTotalGTtipo5 = 0;
        double cantidadTotalGTtipo6 = 0;
        double cantidadTotalGTtipo7 = 0;
        double cantidadTotalGTtipo8 = 0;
        double cantidadTotalGTtipo9 = 0;

        String jpql1 = "select sum(e.cantidad) \"TOTALT1\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU','TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI2' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.lugar_uso_destino = ?3 \n"
                + "and r.lugar_uso_ubigeo_destino = ?4";

        String jpql2 = "select sum(e.cantidad - e.cantidad_extornada) \"TOTALT2\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog not in ('TP_RGUIA_DEVOL','TP_RGUIA_VINC') \n"
                + "and e.activo = 1\n"
                + "and res.id = ?1 \n"
                + "and e.explosivo_id = ?2 \n"
                + "and rgt.lugar_uso_destino = ?3 \n"
                + "and rgt.lugar_uso_ubigeo_destino = ?4";

        String jpql3 = "select sum(e.cantidad) \"TOTALT3\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI2' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.lugar_uso_origen = ?3 \n"
                + "and r.lugar_uso_ubigeo_origen = ?4";

        String jpql4 = "select sum(e.cantidad) \"TOTALT4\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI1' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.lugar_uso_destino = ?3 \n"
                + "and r.lugar_uso_ubigeo_destino = ?4";
        
        String jpql5 = "select sum(e.cantidad) \"TOTALT5\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.lugar_uso_origen = ?3 \n"
                + "and r.lugar_uso_ubigeo_origen = ?4";
        
        String jpql6 = "select sum(e.cantidad) \"TOTALT6\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_DEST_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.lugar_uso_destino = ?3 \n"
                + "and r.lugar_uso_ubigeo_destino = ?4";
        
//        String jpql7 = "select sum(e.cantidad_extornada) \"TOTALT7\" \n"
//                + "from bdintegrado.epp_registro r \n"
//                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
//                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
//                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
//                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
//                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
//                + "where \n"
//                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT') \n"
//                + "and te2.cod_prog not in ('TP_RGUIA_DEVOL') \n"
//                + "and e.activo = 1\n"
//                + "and res.id = ?1 \n"
//                + "and e.explosivo_id = ?2 \n"
//                + "and rgt.lugar_uso_origen = ?3 \n"
//                + "and rgt.lugar_uso_ubigeo_origen = ?4";

        String jpql8 = "select sum(e.cantidad_extornada) \"TOTALT8\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog in ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.lugar_uso_origen = ?3 \n"
                + "and r.lugar_uso_ubigeo_origen = ?4";
        
        String jpql9 = "select sum(e.cantidad_extornada) \"TOTALT9\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_DEST_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog in ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id = ?1 and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.lugar_uso_destino = ?3 \n"
                + "and r.lugar_uso_ubigeo_destino = ?4";

        Query q1 = em.createNativeQuery(jpql1);
        Query q2 = em.createNativeQuery(jpql2);
        Query q3 = em.createNativeQuery(jpql3);
        Query q4 = em.createNativeQuery(jpql4);
        Query q5 = em.createNativeQuery(jpql5);
        Query q6 = em.createNativeQuery(jpql6);
        Query q8 = em.createNativeQuery(jpql8);
        Query q9 = em.createNativeQuery(jpql9);

        q1.setParameter(1, pres.getId());
        q1.setParameter(2, pInExplo.getId());
        q1.setParameter(3, plu.getId());
        q1.setParameter(4, pluu.getId());

        q2.setParameter(1, pres.getId());
        q2.setParameter(2, pInExplo.getId());
        q2.setParameter(3, plu.getId());
        q2.setParameter(4, pluu.getId());

        q3.setParameter(1, pres.getId());
        q3.setParameter(2, pInExplo.getId());
        q3.setParameter(3, plu.getId());
        q3.setParameter(4, pluu.getId());

        q4.setParameter(1, pres.getId());
        q4.setParameter(2, pInExplo.getId());
        q4.setParameter(3, plu.getId());
        q4.setParameter(4, pluu.getId());
        
        q5.setParameter(1, pres.getId());
        q5.setParameter(2, pInExplo.getId());
        q5.setParameter(3, plu.getId());
        q5.setParameter(4, pluu.getId());
        
        q6.setParameter(1, pres.getId());
        q6.setParameter(2, pInExplo.getId());
        q6.setParameter(3, plu.getId());
        q6.setParameter(4, pluu.getId());
        
        q8.setParameter(1, pres.getId());
        q8.setParameter(2, pInExplo.getId());
        q8.setParameter(3, plu.getId());
        q8.setParameter(4, pluu.getId());
        
        q9.setParameter(1, pres.getId());
        q9.setParameter(2, pInExplo.getId());
        q9.setParameter(3, plu.getId());
        q9.setParameter(4, pluu.getId());

        if (!q1.getResultList().isEmpty() && !(q1.getResultList().get(0) == null)) {
            cantidadTotalGTtipo1 = Double.valueOf(q1.getResultList().get(0).toString());
        }

        if (!q2.getResultList().isEmpty() && !(q2.getResultList().get(0) == null)) {
            cantidadTotalGTtipo2 = Double.valueOf(q2.getResultList().get(0).toString());
        }

        if (!q3.getResultList().isEmpty() && !(q3.getResultList().get(0) == null)) {
            cantidadTotalGTtipo3 = Double.valueOf(q3.getResultList().get(0).toString());
        }
        if (!q4.getResultList().isEmpty() && !(q4.getResultList().get(0) == null)) {
            cantidadTotalGTtipo4 = Double.valueOf(q4.getResultList().get(0).toString());
        }
        if (!q5.getResultList().isEmpty() && !(q5.getResultList().get(0) == null)) {
            cantidadTotalGTtipo5 = Double.valueOf(q5.getResultList().get(0).toString());
        }
        if (!q6.getResultList().isEmpty() && !(q6.getResultList().get(0) == null)) {
            cantidadTotalGTtipo6 = Double.valueOf(q6.getResultList().get(0).toString());
        }
        if (!q8.getResultList().isEmpty() && !(q8.getResultList().get(0) == null)) {
            cantidadTotalGTtipo8 = Double.valueOf(q8.getResultList().get(0).toString());
        }
        if (!q9.getResultList().isEmpty() && !(q9.getResultList().get(0) == null)) {
            cantidadTotalGTtipo9 = Double.valueOf(q9.getResultList().get(0).toString());
        }

        double saldo = (cantidadTotalGTtipo1 + cantidadTotalGTtipo2 + cantidadTotalGTtipo4) - cantidadTotalGTtipo3 - cantidadTotalGTtipo5 + cantidadTotalGTtipo6 
                        + cantidadTotalGTtipo8 - cantidadTotalGTtipo9;
        if(saldo < 0){
            saldo = 0;
        }
        return saldo;
    }


    ///
    public Double calculaSaldoExplosivoSumatoriaPolvLuuAnidados(String resolucionIds, EppExplosivo pInExplo, String polvorinesIds) {

        double cantidadTotalGTtipo1 = 0;
        double cantidadTotalGTtipo2 = 0;
        double cantidadTotalGTtipo3 = 0;
        double cantidadTotalGTtipo4 = 0;
        double cantidadTotalGTtipo5 = 0;
        double cantidadTotalGTtipo6 = 0;
        double cantidadTotalGTtipo7 = 0;
        double cantidadTotalGTtipo8 = 0;
        double cantidadTotalGTtipo9 = 0;
        double cantidadTotalGTtipo10 = 0;
        double cantidadTotalGTtipo11 = 0;
        double cantidadTotalGTtipo12 = 0;
        double cantidadTotalGTtipo13 = 0;
        double cantidadTotalGTtipo14 = 0;
        double cantidadTotalGTtipo15 = 0;
        double cantidadTotalGTtipo16 = 0;
        double cantidadTotalGTtipo17 = 0;
        double cantidadTotalGTtipo18 = 0;
        double cantidadTotalGTtipo19 = 0;

        String jpql1 = "select sum(e.cantidad) \"TOTALT1\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI1' and \n"
                + "res.id IN ("+resolucionIds+") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and r.destino_polvorin IN ("+ polvorinesIds + ") ";

        String jpql2 = "select sum(e.cantidad) \"TOTALT2\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI2' and \n"
                + "res.id IN ("+ resolucionIds + ") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.destino_polvorin IN ("+ polvorinesIds + "))";

        String jpql3 = "select sum(e.cantidad) \"TOTALT3\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_INTER' and \n"
                + "res.id IN ("+ resolucionIds + ") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.destino_polvorin IN ("+ polvorinesIds +"))";

        String jpql4 = "select sum(e.cantidad) \"TOTALT4\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_ADQUI2' and \n"
                + "res.id IN ("+ resolucionIds +") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin IN ("+ polvorinesIds +"))";

        String jpql5 = "select sum(e.cantidad) \"TOTALT5\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_INTER' and \n"
                + "res.id IN ("+ resolucionIds +") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin IN ("+ polvorinesIds +"))";

        String jpql6 = "select sum(e.cantidad) \"TOTALT6\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on r.resolucion_id = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_registro r2 on res.registro_id = r2.id \n"
                + "inner join bdintegrado.epp_registro_guia_transito r3 on r3.registro_id = r2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te.cod_prog = 'TP_REGEV_EXT' and \n"
                + "te2.cod_prog = 'TP_RGUIA_DEVOL' and \n"
                + "r3.resolucion_id IN ("+ resolucionIds +") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin IN ("+ polvorinesIds +"))";

        String jpql7 = "select sum(e.cantidad_extornada) \"TOTALT7\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_ADQUI1'\n"
                + "and e.activo = 1\n"
                + "and res.id IN ("+ resolucionIds +") \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.destino_polvorin IN ("+ polvorinesIds +"))";

        String jpql8 = "select sum(e.cantidad_extornada) \"TOTALT8\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_ADQUI2'\n"
                + "and e.activo = 1\n"
                + "and res.id IN ("+ resolucionIds +") \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.origen_polvorin IN ("+ polvorinesIds +"))";

        String jpql9 = "select sum(e.cantidad_extornada) \"TOTAL_9\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_ADQUI2'\n"
                + "and e.activo = 1\n"
                + "and res.id IN ("+ resolucionIds +") \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.destino_polvorin IN ("+ polvorinesIds +"))";

        String jpql10 = "select sum(e.cantidad_extornada) \"TOTAL_10\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_INTER'\n"
                + "and e.activo = 1\n"
                + "and res.id IN ("+ resolucionIds +") \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.origen_polvorin IN ("+ polvorinesIds +") )";

        String jpql11 = "select sum(e.cantidad_extornada) \"TOTAL_11\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_guia_transito rgt on rgt.registro_id = r.id\n"
                + "inner join bdintegrado.epp_resolucion res on rgt.resolucion_id = res.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on rgt.tipo_tramite =te2.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on rgt.id = e.registro_id \n"
                + "inner join bdintegrado.tipo_explosivo t on r.estado = t.id \n"
                + "where \n"
                + "t.cod_prog IN ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') \n"
                + "and te2.cod_prog = 'TP_RGUIA_INTER'\n"
                + "and e.activo = 1\n"
                + "and res.id IN ("+ resolucionIds +") \n"
                + "and e.explosivo_id = ?2 \n"
                + "and (rgt.destino_polvorin IN ("+ polvorinesIds +") )";
        
        String jpql12 = "select sum(e.cantidad) \"TOTALT12\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id IN ("+ resolucionIds +") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin IN ("+ polvorinesIds +") )";
        
        String jpql13 = "select sum(e.cantidad) \"TOTALT13\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_DEST_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog not in ('TP_REGEV_ANU') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id IN ("+ resolucionIds +") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.destino_polvorin IN ("+ polvorinesIds +") )";

        String jpql14 = "select sum(e.cantidad_extornada) \"TOTALT14\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog in ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id IN ("+ resolucionIds +") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.origen_polvorin IN ("+ polvorinesIds +") )";
        
        String jpql15 = "select sum(e.cantidad_extornada) \"TOTALT15\" \n"
                + "from bdintegrado.epp_registro_guia_transito r \n"
                + "inner join bdintegrado.epp_resolucion res on R.RESOLUCION_DEST_ID = res.id \n"
                + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                + "inner join bdintegrado.epp_registro r1 on r.registro_id = r1.id \n"
                + "inner join bdintegrado.tipo_explosivo te on r1.estado = te.id \n"
                + "inner join bdintegrado.tipo_explosivo te2 on r.tipo_tramite =te2.id \n"
                + "where \n"
                + "e.activo = 1 and \n"
                + "te.cod_prog in ('TP_REGEV_EXTPAR','TP_REGEV_EXT','TP_REGEV_EXTTOT') and \n"
                + "te2.cod_prog = 'TP_RGUIA_VINC' and \n"
                + "res.id IN ("+ resolucionIds +") and \n"
                + "e.explosivo_id = ?2 \n"
                + "and (r.destino_polvorin IN ("+ polvorinesIds +") )";
        
        // Para GT de descuento de saldos (origen)
        String jpql16 = "select sum(e.cantidad) \"TOTAL16\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito r \n"
                    + "inner join bdintegrado.epp_resolucion res on r.resolucion_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on r.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on r.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo est on est.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                    + "where \n"
                    + "r.activo = 1 and r9.activo = 1 and e.activo = 1 and est.cod_prog = 'TP_REGEV_FIN' and \n"
                    + "t9.cod_prog = 'TP_RGUIA_TRANSF' and \n"
                    + "res.id IN ("+ resolucionIds +") and \n"
                    + "e.explosivo_id = ?2 \n"
                    + "and (r.destino_polvorin IN ("+ polvorinesIds +") )";
        
        // Para GT de descuento de saldos (destino)
        String jpql17 = "select sum(e.cantidad) \"TOTAL17\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito r \n"
                    + "inner join bdintegrado.epp_resolucion res on r.resolucion_dest_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on r.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on r.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo est on est.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado e on r.id = e.registro_id \n"
                    + "where \n"
                    + "r.activo = 1 and r9.activo = 1 and e.activo = 1 and est.cod_prog = 'TP_REGEV_FIN' and \n"
                    + "t9.cod_prog = 'TP_RGUIA_TRANSF' and \n"
                    + "res.id IN ("+ resolucionIds +") and \n"
                    + "e.explosivo_id = ?2 \n"
                    + "and (r.destino_polvorin IN ("+ polvorinesIds +") )";
        
        // Para GT de descuento de saldos (origen)
        String jpql18 = "select sum(es.cantidad) \"TOTAL18\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito r \n"
                    + "inner join bdintegrado.epp_resolucion res on r.resolucion_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on r.registro_id = r9.id \n"                    
                    + "inner join bdintegrado.tipo_explosivo t9 on r.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo est on est.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado es on r.id = es.registro_id \n"
                    + "inner join bdintegrado.epp_homologacion_reg e on r9.id = e.registro_id \n"
                    + "where \n"
                    + "r.activo = 1 and r9.activo = 1 and e.activo = 1 and est.cod_prog = 'TP_REGEV_FIN' and \n"
                    + "t9.cod_prog = 'TP_RGUIA_DTPARC' and \n"
                    + "res.id IN ("+ resolucionIds +") and \n"
                    + "e.explo_vencido = ?2 \n"
                    + "and (r.destino_polvorin IN ("+ polvorinesIds +") )";
        
        // Para GT de descuento de saldos especial (destino)
        String jpql19 = "select sum(es.cantidad) \"TOTAL19\" \n"
                    + "from \n"
                    + "bdintegrado.epp_registro_guia_transito r \n"
                    + "inner join bdintegrado.epp_resolucion res on r.resolucion_dest_id = res.id \n"
                    + "inner join bdintegrado.epp_registro r9 on r.registro_id = r9.id \n"
                    + "inner join bdintegrado.tipo_explosivo t9 on r.tipo_tramite = t9.id \n"
                    + "inner join bdintegrado.tipo_explosivo est on est.id = r9.estado \n"
                    + "inner join bdintegrado.epp_explosivo_solicitado es on r.id = es.registro_id \n"
                    + "inner join bdintegrado.epp_homologacion_reg e on r9.id = e.registro_id \n"
                    + "where \n"
                    + "r.activo = 1 and r9.activo = 1 and e.activo = 1 and est.cod_prog = 'TP_REGEV_FIN' and \n"
                    + "t9.cod_prog = 'TP_RGUIA_DTPARC' and \n"
                    + "res.id IN ("+ resolucionIds +") and \n"
                    + "e.explo_vigente = ?2 \n"
                    + "and (r.destino_polvorin IN ("+ polvorinesIds +") )";
        
        Query q1 = em.createNativeQuery(jpql1);
        Query q2 = em.createNativeQuery(jpql2);
        Query q3 = em.createNativeQuery(jpql3);
        Query q4 = em.createNativeQuery(jpql4);
        Query q5 = em.createNativeQuery(jpql5);
        Query q6 = em.createNativeQuery(jpql6);
        Query q7 = em.createNativeQuery(jpql7);
        Query q8 = em.createNativeQuery(jpql8);
        Query q9 = em.createNativeQuery(jpql9);
        Query q10 = em.createNativeQuery(jpql10);
        Query q11 = em.createNativeQuery(jpql11);
        Query q12 = em.createNativeQuery(jpql12);
        Query q13 = em.createNativeQuery(jpql13);
        Query q14 = em.createNativeQuery(jpql14);
        Query q15 = em.createNativeQuery(jpql15);
        Query q16 = em.createNativeQuery(jpql16);
        Query q17 = em.createNativeQuery(jpql17);
        Query q18 = em.createNativeQuery(jpql18);
        Query q19 = em.createNativeQuery(jpql19);

        q1.setParameter(2, pInExplo.getId());
        q2.setParameter(2, pInExplo.getId());
        q3.setParameter(2, pInExplo.getId());
        q4.setParameter(2, pInExplo.getId());
        q5.setParameter(2, pInExplo.getId());
        q6.setParameter(2, pInExplo.getId());
        q7.setParameter(2, pInExplo.getId());
        q8.setParameter(2, pInExplo.getId());
        q9.setParameter(2, pInExplo.getId());
        q10.setParameter(2, pInExplo.getId());
        q11.setParameter(2, pInExplo.getId());
        q12.setParameter(2, pInExplo.getId());
        q13.setParameter(2, pInExplo.getId());
        q14.setParameter(2, pInExplo.getId());
        q15.setParameter(2, pInExplo.getId());
        q16.setParameter(2, pInExplo.getId());
        q17.setParameter(2, pInExplo.getId());
        q18.setParameter(2, pInExplo.getId());
        q19.setParameter(2, pInExplo.getId());

        if (!q1.getResultList().isEmpty() && !(q1.getResultList().get(0) == null)) {
            cantidadTotalGTtipo1 = Double.valueOf(q1.getResultList().get(0).toString());
        }

        if (!q2.getResultList().isEmpty() && !(q2.getResultList().get(0) == null)) {
            cantidadTotalGTtipo2 = Double.valueOf(q2.getResultList().get(0).toString());
        }

        if (!q3.getResultList().isEmpty() && !(q3.getResultList().get(0) == null)) {
            cantidadTotalGTtipo3 = Double.valueOf(q3.getResultList().get(0).toString());
        }

        if (!q4.getResultList().isEmpty() && !(q4.getResultList().get(0) == null)) {
            cantidadTotalGTtipo4 = Double.valueOf(q4.getResultList().get(0).toString());
        }

        if (!q5.getResultList().isEmpty() && !(q5.getResultList().get(0) == null)) {
            cantidadTotalGTtipo5 = Double.valueOf(q5.getResultList().get(0).toString());
        }

        if (!q6.getResultList().isEmpty() && !(q6.getResultList().get(0) == null)) {
            cantidadTotalGTtipo6 = Double.valueOf(q6.getResultList().get(0).toString());
        }

        if (!q7.getResultList().isEmpty() && !(q7.getResultList().get(0) == null)) {
            cantidadTotalGTtipo7 = Double.valueOf(q7.getResultList().get(0).toString());
        }

        if (!q8.getResultList().isEmpty() && !(q8.getResultList().get(0) == null)) {
            cantidadTotalGTtipo8 = Double.valueOf(q8.getResultList().get(0).toString());
        }

        if (!q9.getResultList().isEmpty() && !(q9.getResultList().get(0) == null)) {
            cantidadTotalGTtipo9 = Double.valueOf(q9.getResultList().get(0).toString());
        }

        if (!q10.getResultList().isEmpty() && !(q10.getResultList().get(0) == null)) {
            cantidadTotalGTtipo10 = Double.valueOf(q10.getResultList().get(0).toString());
        }

        if (!q11.getResultList().isEmpty() && !(q11.getResultList().get(0) == null)) {
            cantidadTotalGTtipo11 = Double.valueOf(q11.getResultList().get(0).toString());
        }
        
        if (!q12.getResultList().isEmpty() && !(q12.getResultList().get(0) == null)) {
            cantidadTotalGTtipo12 = Double.valueOf(q12.getResultList().get(0).toString());
        }
        
        if (!q13.getResultList().isEmpty() && !(q13.getResultList().get(0) == null)) {
            cantidadTotalGTtipo13 = Double.valueOf(q13.getResultList().get(0).toString());
        }
        
        if (!q14.getResultList().isEmpty() && !(q14.getResultList().get(0) == null)) {
            cantidadTotalGTtipo14 = Double.valueOf(q14.getResultList().get(0).toString());
        }
        
        if (!q15.getResultList().isEmpty() && !(q15.getResultList().get(0) == null)) {
            cantidadTotalGTtipo15 = Double.valueOf(q15.getResultList().get(0).toString());
        }
        
        if (!q16.getResultList().isEmpty() && !(q16.getResultList().get(0) == null)) {
            cantidadTotalGTtipo16 = Double.valueOf(q16.getResultList().get(0).toString());
        }
        
        if (!q17.getResultList().isEmpty() && !(q17.getResultList().get(0) == null)) {
            cantidadTotalGTtipo17 = Double.valueOf(q17.getResultList().get(0).toString());
        }
        if (!q18.getResultList().isEmpty() && !(q18.getResultList().get(0) == null)) {
            cantidadTotalGTtipo18 = Double.valueOf(q18.getResultList().get(0).toString());
        }
        
        if (!q19.getResultList().isEmpty() && !(q19.getResultList().get(0) == null)) {
            cantidadTotalGTtipo19 = Double.valueOf(q19.getResultList().get(0).toString());
        }
        
        double saldo = cantidadTotalGTtipo1
                        + cantidadTotalGTtipo2
                        + cantidadTotalGTtipo3
                        - cantidadTotalGTtipo4
                        - cantidadTotalGTtipo5
                        - cantidadTotalGTtipo6
                        - cantidadTotalGTtipo7
                        + cantidadTotalGTtipo8
                        - cantidadTotalGTtipo9
                        + cantidadTotalGTtipo10
                        - cantidadTotalGTtipo11
                        - cantidadTotalGTtipo12
                        + cantidadTotalGTtipo13
                        + cantidadTotalGTtipo14
                        - cantidadTotalGTtipo15
                        - cantidadTotalGTtipo16
                        + cantidadTotalGTtipo17
                        - cantidadTotalGTtipo18
                        + cantidadTotalGTtipo19;
        return saldo;
    }
    ///
}


