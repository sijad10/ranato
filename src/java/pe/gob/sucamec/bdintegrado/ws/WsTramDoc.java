package pe.gob.sucamec.bdintegrado.ws;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.TipoCliente;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;
import pe.gob.sucamec.wstramdoc.ws.AdjuntarDocumentoWS;
import pe.gob.sucamec.wstramdoc.ws.CamposIn;
import pe.gob.sucamec.wstramdoc.ws.ClienteIn;
import pe.gob.sucamec.wstramdoc.ws.CreacionExpedienteWs;
import pe.gob.sucamec.wstramdoc.ws.DocumentoIn;
import pe.gob.sucamec.wstramdoc.ws.DocumentosWs;
import pe.gob.sucamec.wstramdoc.ws.ExpedienteIn;
import pe.gob.sucamec.wstramdoc.ws.RespuestaArchivo;
import pe.gob.sucamec.wstramdoc.ws.ServiciosSTD;
import pe.gob.sucamec.wstramdoc.ws.ServiciosSTDEndpoint;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.Cliente;
        
@Named("wsTramDoc")
@SessionScoped
public class WsTramDoc implements Serializable {

    public String crearExpediente(Map mapa) {
        try {
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();

            ClienteIn cliente = new ClienteIn();
            cliente.setTipoCliente("" + mapa.get("tipocliente"));//1:JURIDICA
            cliente.setNumeroDocumentoIdentidad("" + mapa.get("ruc"));
            cliente.setRazonSocial("" + mapa.get("razonsocial"));

            //**PRIMER DOCUMENTO
            DocumentoIn documento = new DocumentoIn();
            documento.setTipoDocumento(25);
            documento.setFechaDocumento("" + mapa.get("fechaDeposito"));
            documento.setAsunto("DEP. BANCO DE LA NACION");
            //PRIMER CAMPO
            CamposIn nroCpb = new CamposIn();
            nroCpb.setIdCampo(3);//empoce
            nroCpb.setValor("" + mapa.get("depositoNro"));
            documento.getCampos().add(nroCpb);
            //SEGUNDO CAMPO
            CamposIn impCpb = new CamposIn();
            impCpb.setIdCampo(4);//importe
            impCpb.setValor("" + mapa.get("depositoImporte"));
            documento.getCampos().add(impCpb);
            //TERCER CAMPO
            CamposIn fecCpb = new CamposIn();
            fecCpb.setIdCampo(5);//fecha
            fecCpb.setValor("" + mapa.get("depositoFecha"));
            documento.getCampos().add(fecCpb);

            //**SEGUNDO DOCUMENTO
//            DocumentoIn documentoSuse = new DocumentoIn();
//            documentoSuse.setTipoDocumento(309);
//            documentoSuse.setFechaDocumento("" + mapa.get("fechaFactura"));
//            documentoSuse.setAsunto("NRO FACTURA");
//            //PRIMER CAMPO
//            CamposIn nroFac = new CamposIn();
//            nroFac.setIdCampo(174);//COPIA
//            nroFac.setValor("" + mapa.get("nrofactura"));
//            documento.getCampos().add(nroFac);
            //**CREA EXPEDIENTE
            ExpedienteIn exp = new ExpedienteIn();
            exp.setAsunto("EXPEDIENTE DE GUÍA DE TRÁNSITO ELECTRÓNICA - GEPP");
            exp.setCliente(cliente);
            exp.setObservacion("GUÍA DE TRÁNSITO CREADA POR EL ADMINISTRADO");//DATOS ADICIONALES
            exp.setIdProceso(Integer.parseInt("710"));//GUIA DE TRANSITO //PRUEBAS: 712 /  PRODUCCION: 710
            exp.getDocumentos().add(documento);
            //exp.getDocumentos().add(documentoSuse);
            exp.setIdUsuarioCreacion(1003);
            //exp.setIdUsuarioResponsable(222);

            CreacionExpedienteWs res = port.crearExpediente(exp);

            return res.getNumeroExpediente();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String crearResolucion(String p_numexp, Integer p_tipo_doc, Integer p_user, String p_fecha, String p_tipor) {
        try {
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();

            DocumentoIn docI = new DocumentoIn();
            docI.setAsunto(p_tipor);
            docI.setTipoDocumento(p_tipo_doc);//4: resolucion , 62:guia transito
            docI.setNumerarDocumento(true);
            docI.setFechaDocumento(p_fecha);
            docI.setIdAutor(p_user);//se debe consultar el usuario entre BDEXPLOSIVO Y TRAMDOC
            AdjuntarDocumentoWS docws = port.adjuntarDocumento(p_numexp, docI);

            DocumentosWs dws = port.buscarDocumento(docws.getIdDocumento(), false);
            if (dws.getId() != null) {
                return dws.getNumero();
            } else {
                System.err.println(dws.getMensaje());
                return null;
            }

        } catch (Exception e) {
            JsfUtil.mensajeError("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ASIGNAR EXPEDIENTE CYDOC
     *
     * @param exp
     * @param usuarioRemitente
     * @param usuarioDestino
     * @param comentario
     * @param actividad
     * @param acciones
     * @param adjuntar
     * @param nroRes
     * @return Respuesta de WS de proceso de asignación de expediente
     */
    public boolean asignarExpediente(String exp, String usuarioRemitente, String usuarioDestino, String comentario, String actividad, List<Integer> acciones, boolean adjuntar, String nroRes) {
        try {
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint portCydoc = service.getServiciosSTDEndpointPort();
            String respuesta = portCydoc.derivarExpedienteUsuario(0, exp, usuarioRemitente, usuarioDestino, 0, comentario, actividad, acciones);

            if (respuesta != null && !respuesta.toUpperCase().trim().equals("OK")) {
                System.err.println("Error asignarExpediente: " + respuesta);
                JsfUtil.mensajeError("Error de WEB SERVICE :" + respuesta);
                return false;
            }

            return true;
        } catch (Exception e) {
            JsfUtil.mensajeError("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ARCHIVAR EXPEDIENTE CYDOC
     *
     * @param nroExp
     * @param usuarioRemitente
     * @param observacion
     * @return Respuesta de WS de proceso de archivación de expediente
     */
    public boolean archivarExpediente(String nroExp, String usuarioRemitente, String observacion) {
        try {
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint portCydoc = service.getServiciosSTDEndpointPort();
            String respuesta = portCydoc.archivarExpediente(0, nroExp, usuarioRemitente, observacion);

            if (respuesta != null && !respuesta.toUpperCase().equals("OK")) {
                JsfUtil.mensajeError("Error de WEB SERVICE :" + respuesta);
                return false;
            }

            return true;
        } catch (Exception e) {
            JsfUtil.mensajeError("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * CREAR EXPEDIENTE
     *
     * @author Richar Fernández
     * @version 2.0
     * @param tc Recibo
     * @param cli Administrado
     * @param p_asunto Asunto
     * @param p_id_proceso Proceso
     * @param p_usu_creacion usuario que crea expediente
     * @param p_usu_resp usuario responsable expediente
     * @return Nro. de Expediente
     */
    public String crearExpediente_capacitacion(SbRecibos tc, SbPersonaGt cli, String p_asunto, Integer p_id_proceso, Integer p_usu_creacion, Integer p_usu_resp) {
        try {
            String tipoDoc = "";
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();
            ClienteIn cI = new ClienteIn();

            if (cli.getTipoDoc() == null) {
                if (cli.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    tipoDoc = "TP_DOCID_RUC";
                } else {
                    tipoDoc = "TP_DOCID_DNI";
                }
            } else {
                tipoDoc = cli.getTipoDoc().getCodProg();
            }

            TipoCliente tipoC = new TipoCliente();
            switch (tipoDoc) {
                case "TP_DOCID_RUC":
                    tipoC = new TipoCliente(BigDecimal.valueOf(1));
                    cI.setNumeroDocumentoIdentidad(cli.getRuc());
                    break;
                case "TP_DOCID_DNI":
                    tipoC = new TipoCliente(BigDecimal.valueOf(2));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                    break;
                case "TP_DOCID_CE":
                    tipoC = new TipoCliente(BigDecimal.valueOf(3));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                    break;
                case "TP_DOCID_PAS":
                    tipoC = new TipoCliente(BigDecimal.valueOf(5));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());

                    break;
            }

            cI.setApellidoPaterno(cli.getApePat());
            cI.setApellidoMaterno(cli.getApeMat());
            cI.setNombres(cli.getNombres());
            cI.setRazonSocial(cli.getRznSocial());
            cI.setTipoCliente(tipoC.getIdTipoCliente().toString());

            Date hoy = new Date();
            DocumentoIn dI = new DocumentoIn();
            dI.setTipoDocumento(25);
            dI.setFechaDocumento("" + JsfUtil.obtenerFechaXCriterio(hoy, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 1));
            dI.setAsunto(p_asunto);
            dI.setIdAutor(p_usu_resp);

            Date fecEmpoce = (tc != null) ? tc.getFechaMovimiento() : (new Date());
            CamposIn campI1 = new CamposIn();
            campI1.setIdCampo(3);
            campI1.setValor((tc == null) ? "0" : tc.getNroSecuencia().toString());
            dI.getCampos().add(campI1);
            CamposIn campI2 = new CamposIn();
            campI2.setIdCampo(4);
            campI2.setValor((tc == null) ? "0" : tc.getImporte().toString());
            dI.getCampos().add(campI2);
            CamposIn campI3 = new CamposIn();
            campI3.setIdCampo(5);
            campI3.setValor("" + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 1));
            dI.getCampos().add(campI3);

            ExpedienteIn eI = new ExpedienteIn();
            eI.setAsunto(p_asunto);
            eI.setCliente(cI);
            eI.setIdProceso(p_id_proceso);
            eI.setIdUsuarioCreacion(p_usu_creacion);
            eI.setIdUsuarioResponsable(p_usu_resp);
            eI.setObservacion("Ingreso automático desde web");
            eI.getDocumentos().add(dI);

            CreacionExpedienteWs ress = port.crearExpediente(eI);
            if (ress != null && ress.getNumeroExpediente() == null) {
                System.out.println("Error: " + ress.getError());
            }
            return ress.getNumeroExpediente();

        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * CREAR EXPEDIENTE
     *
     * @author Richar Fernández
     * @version 2.0
     * @param tc Recibo
     * @param cli Administrado
     * @param p_asunto Asunto
     * @param p_id_proceso Proceso
     * @param p_usu_creacion usuario que crea expediente
     * @param p_usu_resp usuario responsable expediente
     * @param asuntoCydoc
     * @return Nro. de Expediente
     */
    public String crearExpediente_regularizacion(SbRecibos tc, SbPersonaGt cli, String p_asunto, Integer p_id_proceso, Integer p_usu_creacion, Integer p_usu_resp, String asuntoCydoc) {
        try {
            String tipoDoc = "";
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();
            ClienteIn cI = new ClienteIn();

            if (cli.getTipoDoc() == null) {
                if (cli.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    tipoDoc = "TP_DOCID_RUC";
                } else {
                    tipoDoc = "TP_DOCID_DNI";
                }
            } else {
                tipoDoc = cli.getTipoDoc().getCodProg();
            }

            TipoCliente tipoC = new TipoCliente();
            switch (tipoDoc) {
                case "TP_DOCID_RUC":
                    tipoC = new TipoCliente(BigDecimal.valueOf(1));
                    cI.setNumeroDocumentoIdentidad(cli.getRuc());
                    break;
                case "TP_DOCID_DNI":
                    tipoC = new TipoCliente(BigDecimal.valueOf(2));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                    break;
                case "TP_DOCID_CE":
                    tipoC = new TipoCliente(BigDecimal.valueOf(3));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                    break;
                case "TP_DOCID_PAS":
                    tipoC = new TipoCliente(BigDecimal.valueOf(5));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                    break;
            }

            cI.setApellidoPaterno(cli.getApePat());
            cI.setApellidoMaterno(cli.getApeMat());
            cI.setNombres(cli.getNombres());
            cI.setRazonSocial(cli.getRznSocial());
            cI.setTipoCliente(tipoC.getIdTipoCliente().toString());

            Date hoy = new Date();
            DocumentoIn dI = new DocumentoIn();
            dI.setTipoDocumento(28);
            dI.setFechaDocumento("" + JsfUtil.obtenerFechaXCriterio(hoy, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 1));
            dI.setAsunto(asuntoCydoc);
            dI.setIdAutor(p_usu_resp);

//            CamposIn campI1 = new CamposIn();
//            campI1.setIdCampo(3);
//            campI1.setValor((tc == null) ? "0" : tc.getNroSecuencia().toString());
//            dI.getCampos().add(campI1);
//            CamposIn campI2 = new CamposIn();
//            campI2.setIdCampo(4);
//            campI2.setValor((tc == null) ? "0" : tc.getImporte().toString());
//            dI.getCampos().add(campI2);
//            CamposIn campI3 = new CamposIn();
//            campI3.setIdCampo(5);
//            campI3.setValor("" + JsfUtil.obtenerFechaXCriterio(hoy, 3) + "/"
//                    + JsfUtil.obtenerFechaXCriterio(hoy, 2) + "/"
//                    + JsfUtil.obtenerFechaXCriterio(hoy, 1));
//            dI.getCampos().add(campI3);
            ExpedienteIn eI = new ExpedienteIn();
            eI.setAsunto(asuntoCydoc);
            eI.setCliente(cI);
            eI.setIdProceso(p_id_proceso);
            eI.setIdUsuarioCreacion(p_usu_creacion);
            eI.setIdUsuarioResponsable(p_usu_resp);
            eI.setObservacion(p_asunto);
            eI.getDocumentos().add(dI);

            CreacionExpedienteWs ress = port.crearExpediente(eI);

            return ress.getNumeroExpediente();

        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean adjuntarDeclaracionJurada(Integer usuario, String login, String expediente, String asunto, byte[] archivo) {
        try {
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();

            List<DocumentosWs> lstDoc = port.listarDocumentos(expediente, true);
            for (DocumentosWs doc : lstDoc) {
                RespuestaArchivo resp = port.adjuntarArchivo(doc.getId(), "DJ_" + expediente + ".pdf", login, archivo);
                System.err.println(resp.getMensaje());
                break;
            }

            return true;
        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * CREAR EXPEDIENTE
     *
     * @author Richar Fernández
     * @version 2.0
     * @param tc Recibo
     * @param cli Administrado
     * @param p_asunto Asunto
     * @param p_id_proceso Proceso
     * @param p_usu_creacion usuario que crea expediente
     * @param p_usu_resp usuario responsable expediente
     * @return Nro. de Expediente
     */
    public String crearExpediente_libro(SbRecibos tc, SbPersonaGt cli, String p_asunto, Integer p_id_proceso, Integer p_usu_creacion, Integer p_usu_resp) {
        try {
            String tipoDoc = "";
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();
            ClienteIn cI = new ClienteIn();

            if (cli.getTipoDoc() == null) {
                if (cli.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    tipoDoc = "TP_DOCID_RUC";
                } else {
                    tipoDoc = "TP_DOCID_DNI";
                }
            } else {
                tipoDoc = cli.getTipoDoc().getCodProg();
            }

            TipoCliente tipoC = new TipoCliente();
            switch (tipoDoc) {
                case "TP_DOCID_RUC":
                    tipoC = new TipoCliente(BigDecimal.valueOf(1));
                    cI.setNumeroDocumentoIdentidad(cli.getRuc());
                    break;
                case "TP_DOCID_DNI":
                    tipoC = new TipoCliente(BigDecimal.valueOf(2));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                    break;
                case "TP_DOCID_CE":
                    tipoC = new TipoCliente(BigDecimal.valueOf(3));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                    break;
                case "TP_DOCID_PAS":
                    tipoC = new TipoCliente(BigDecimal.valueOf(5));
                    cI.setNumeroDocumentoIdentidad(cli.getNumDoc());

                    break;
            }

            cI.setApellidoPaterno(cli.getApePat());
            cI.setApellidoMaterno(cli.getApeMat());
            cI.setNombres(cli.getNombres());
            cI.setRazonSocial(cli.getRznSocial());
            cI.setTipoCliente(tipoC.getIdTipoCliente().toString());

            Date hoy = new Date();
            DocumentoIn dI = new DocumentoIn();
            dI.setTipoDocumento(28);
            dI.setFechaDocumento("" + JsfUtil.obtenerFechaXCriterio(hoy, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 1));
            dI.setAsunto(p_asunto);
            dI.setIdAutor(p_usu_resp);

            // Date fecEmpoce = (tc != null) ? tc.getFechaMovimiento() : (new Date());
//            CamposIn campI1 = new CamposIn();
//            campI1.setIdCampo(3);
//            campI1.setValor((tc == null) ? "0" : tc.getNroSecuencia().toString());
//            dI.getCampos().add(campI1);
//            CamposIn campI2 = new CamposIn();
//            campI2.setIdCampo(4);
//            campI2.setValor((tc == null) ? "0" : tc.getImporte().toString());
//            dI.getCampos().add(campI2);
//            CamposIn campI3 = new CamposIn();
//            campI3.setIdCampo(5);
//            campI3.setValor("" + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 3) + "/"
//                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 2) + "/"
//                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 1));
//            dI.getCampos().add(campI3);
            ExpedienteIn eI = new ExpedienteIn();
            eI.setAsunto(p_asunto);
            eI.setCliente(cI);
            eI.setIdProceso(p_id_proceso);
            eI.setIdUsuarioCreacion(p_usu_creacion);
            eI.setIdUsuarioResponsable(p_usu_resp);
            eI.setObservacion("Ingreso automático desde web");
            eI.getDocumentos().add(dI);

            CreacionExpedienteWs ress = port.crearExpediente(eI);
            return ress.getNumeroExpediente();

        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ClienteIn preparaCliente(SbPersonaGt cli) {
        ClienteIn cI = new ClienteIn();
        String tipoDoc = "";

        if (cli.getTipoDoc() == null) {
            if (cli.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                tipoDoc = "TP_DOCID_RUC";
            } else {
                tipoDoc = "TP_DOCID_DNI";
            }
        } else {
            tipoDoc = cli.getTipoDoc().getCodProg();
        }

        TipoCliente tipoC = new TipoCliente();
        switch (tipoDoc) {
            case "TP_DOCID_RUC":
                tipoC = new TipoCliente(BigDecimal.valueOf(1));
                cI.setNumeroDocumentoIdentidad(cli.getRuc());
                break;
            case "TP_DOCID_DNI":
                tipoC = new TipoCliente(BigDecimal.valueOf(2));
                cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                break;
            case "TP_DOCID_CE":
                tipoC = new TipoCliente(BigDecimal.valueOf(3));
                cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                break;
            case "TP_DOCID_PAS":
                tipoC = new TipoCliente(BigDecimal.valueOf(5));
                cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                break;
        }

        cI.setApellidoPaterno(((cli.getApePat() != null) ? cli.getApePat().trim() : null));
        cI.setApellidoMaterno(((cli.getApeMat() != null) ? cli.getApeMat().trim() : null));
        cI.setNombres(((cli.getNombres() != null) ? cli.getNombres().trim() : null));
        cI.setRazonSocial(((cli.getRznSocial() != null) ? cli.getRznSocial().trim() : null));
        cI.setTipoCliente(tipoC.getIdTipoCliente().toString());
        return cI;
    }
    
    public ClienteIn preparaClienteTV(SbPersona cli) {
        ClienteIn cI = new ClienteIn();
        String tipoDoc = "";

        if (cli.getTipoDoc() == null) {
            if (cli.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                tipoDoc = "TP_DOCID_RUC";
            } else {
                tipoDoc = "TP_DOCID_DNI";
            }
        } else {
            tipoDoc = cli.getTipoDoc().getCodProg();
        }

        TipoCliente tipoC = new TipoCliente();
        switch (tipoDoc) {
            case "TP_DOCID_RUC":
                tipoC = new TipoCliente(BigDecimal.valueOf(1));
                cI.setNumeroDocumentoIdentidad(cli.getRuc());
                break;
            case "TP_DOCID_DNI":
                tipoC = new TipoCliente(BigDecimal.valueOf(2));
                cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                break;
            case "TP_DOCID_CE":
                tipoC = new TipoCliente(BigDecimal.valueOf(3));
                cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                break;
            case "TP_DOCID_PAS":
                tipoC = new TipoCliente(BigDecimal.valueOf(5));
                cI.setNumeroDocumentoIdentidad(cli.getNumDoc());
                break;
        }

        cI.setApellidoPaterno(((cli.getApePat() != null) ? cli.getApePat().trim() : null));
        cI.setApellidoMaterno(((cli.getApeMat() != null) ? cli.getApeMat().trim() : null));
        cI.setNombres(((cli.getNombres() != null) ? cli.getNombres().trim() : null));
        cI.setRazonSocial(((cli.getRznSocial() != null) ? cli.getRznSocial().trim() : null));
        cI.setTipoCliente(tipoC.getIdTipoCliente().toString());
        return cI;
    }

    /**
     * CREAR EXPEDIENTE PARA CARTERA DE CLIENTE
     *
     * @author Richar Fernández
     * @version 2.0
     * @param tc Recibo
     * @param cli Administrado
     * @param p_asunto Asunto
     * @param p_id_proceso Proceso
     * @param p_usu_creacion usuario que crea expediente
     * @param p_usu_resp usuario responsable expediente
     * @param titulo
     * @return Nro. de Expediente
     */
    public String crearExpediente_gssp(SbRecibos tc, SbPersonaGt cli, String p_asunto, Integer p_id_proceso, Integer p_usu_creacion, Integer p_usu_resp, String titulo) {
        try {
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();
            ClienteIn cI = preparaCliente(cli);

            Date hoy = new Date();
            DocumentoIn dI = new DocumentoIn();
            dI.setFechaDocumento("" + JsfUtil.obtenerFechaXCriterio(hoy, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 1));
            dI.setAsunto(titulo);
            dI.setIdAutor(p_usu_resp);
            dI.setTipoDocumento(25);

            Date fecEmpoce = (tc != null) ? tc.getFechaMovimiento() : (new Date());
            CamposIn campI1 = new CamposIn();
            campI1.setIdCampo(3);
            campI1.setValor((tc == null) ? "0" : tc.getNroSecuencia().toString());
            dI.getCampos().add(campI1);
            CamposIn campI2 = new CamposIn();
            campI2.setIdCampo(4);
            campI2.setValor((tc == null) ? "0" : tc.getImporte().toString());
            dI.getCampos().add(campI2);
            CamposIn campI3 = new CamposIn();
            campI3.setIdCampo(5);
            campI3.setValor("" + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 1));
            dI.getCampos().add(campI3);

            ExpedienteIn eI = new ExpedienteIn();
            eI.setAsunto(titulo);
            eI.setCliente(cI);
            eI.setIdProceso(p_id_proceso);
            eI.setIdUsuarioCreacion(p_usu_creacion);
            eI.setIdUsuarioResponsable(p_usu_resp);
            eI.setObservacion(p_asunto);
            eI.getDocumentos().add(dI);
            
           /*
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            
            System.out.println("ExpedienteIn setAsunto->"+eI.getAsunto());
            System.out.println("ExpedienteIn setCliente->"+eI.getCliente());
            System.out.println("ExpedienteIn apepat->"+eI.getCliente().getApellidoPaterno());
            System.out.println("ExpedienteIn apemat->"+eI.getCliente().getApellidoMaterno());
            System.out.println("ExpedienteIn nombres->"+eI.getCliente().getNombres());
            System.out.println("ExpedienteIn numdoc->"+eI.getCliente().getNumeroDocumentoIdentidad());
            System.out.println("ExpedienteIn getRazonSocial->"+eI.getCliente().getRazonSocial());
            System.out.println("ExpedienteIn getTipoCliente->"+eI.getCliente().getTipoCliente());
            
            System.out.println("ExpedienteIn getDocumentos->"+eI.getDocumentos());
            System.out.println("ExpedienteIn getDocumentos.getAsunto->"+eI.getDocumentos().get(0).getAsunto());
            System.out.println("ExpedienteIn getDocumentos.getCampos.getIdCampo->"+eI.getDocumentos().get(0).getCampos().get(0).getIdCampo());
            System.out.println("ExpedienteIn getDocumentos.getCampos.getValor->"+eI.getDocumentos().get(0).getCampos().get(0).getValor());
            System.out.println("ExpedienteIn getDocumentos.getFechaDocumento->"+eI.getDocumentos().get(0).getFechaDocumento());
            System.out.println("ExpedienteIn getDocumentos.getIdAutor->"+eI.getDocumentos().get(0).getIdAutor());
            System.out.println("ExpedienteIn getDocumentos.getNumeroDocumento->"+eI.getDocumentos().get(0).getNumeroDocumento());
            System.out.println("ExpedienteIn getDocumentos.getTipoDocumento->"+eI.getDocumentos().get(0).getTipoDocumento());
            
            System.out.println("ExpedienteIn setIdProceso->"+eI.getIdProceso());
            System.out.println("ExpedienteIn setIdUsuarioCreacion->"+eI.getIdUsuarioCreacion());
            System.out.println("ExpedienteIn setIdUsuarioResponsable->"+eI.getIdUsuarioResponsable());
            System.out.println("ExpedienteIn getNumeroExpedienteDicse->"+eI.getNumeroExpedienteDicse());
            System.out.println("ExpedienteIn setObservacion->"+eI.getObservacion());
            
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
           */

            CreacionExpedienteWs ress = port.crearExpediente(eI);

            return ress.getNumeroExpediente();

        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * CREAR EXPEDIENTE PARA GUIA DE RECOJO
     *
     * @author Richar Fernández
     * @version 2.0
     * @param tc Recibo
     * @param cli Administrado
     * @param p_asunto Asunto
     * @param p_id_proceso Proceso
     * @param p_usu_creacion usuario que crea expediente
     * @param p_usu_resp usuario responsable expediente
     * @param titulo
     * @return Nro. de Expediente
     */
    public String crearExpediente_guiaExp(SbRecibos tc, SbPersonaGt cli, String p_asunto, Integer p_id_proceso, Integer p_usu_creacion, Integer p_usu_resp, String titulo) {
        try {
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();
            ClienteIn cI = preparaCliente(cli);

            Date hoy = new Date();
            DocumentoIn dI = new DocumentoIn();
            dI.setFechaDocumento("" + JsfUtil.obtenerFechaXCriterio(hoy, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 1));
            dI.setAsunto(titulo);
            dI.setIdAutor(p_usu_resp);
            dI.setTipoDocumento(25);

            Date fecEmpoce = (tc != null) ? tc.getFechaMovimiento() : (new Date());
            CamposIn campI1 = new CamposIn();
            campI1.setIdCampo(3);
            campI1.setValor((tc == null) ? "0" : tc.getNroSecuencia().toString());
            dI.getCampos().add(campI1);
            CamposIn campI2 = new CamposIn();
            campI2.setIdCampo(4);
            campI2.setValor((tc == null) ? "0" : tc.getImporte().toString());
            dI.getCampos().add(campI2);
            CamposIn campI3 = new CamposIn();
            campI3.setIdCampo(5);
            campI3.setValor("" + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 1));
            dI.getCampos().add(campI3);

            ExpedienteIn eI = new ExpedienteIn();
            eI.setAsunto(titulo);
            eI.setCliente(cI);
            eI.setIdProceso(p_id_proceso);
            eI.setIdUsuarioCreacion(p_usu_creacion);
            eI.setIdUsuarioResponsable(p_usu_resp);
            eI.setObservacion(p_asunto);
            eI.getDocumentos().add(dI);

            CreacionExpedienteWs ress = port.crearExpediente(eI);

            return ress.getNumeroExpediente();

        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * CREAR EXPEDIENTE PARA TRAMITE VIRTUAL
     *
     * @author Mario Salinas
     * @version 2.0
     * @param tc Recibo
     * @param cli Administrado
     * @param p_asunto Asunto
     * @param p_id_proceso Proceso
     * @param p_usu_creacion usuario que crea expediente
     * @param p_usu_resp usuario responsable expediente
     * @param titulo
     * @return Nro. de Expediente
     */
    public String crearExpediente_tramiteVirtual(SbRecibos tc, SbPersona cli, String p_asunto, Integer p_id_proceso, Integer p_usu_creacion, Integer p_usu_resp, String titulo) {
        try {            
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();
            ClienteIn cI = preparaClienteTV(cli);
            
            Date hoy = new Date();
            DocumentoIn dI = new DocumentoIn();
            dI.setFechaDocumento("" + JsfUtil.obtenerFechaXCriterio(hoy, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 1));
            dI.setAsunto(titulo);
            dI.setIdAutor(p_usu_resp);
            dI.setTipoDocumento(25);

            Date fecEmpoce = (tc != null) ? tc.getFechaMovimiento() : (new Date());
            CamposIn campI1 = new CamposIn();
            campI1.setIdCampo(3);
            campI1.setValor((tc == null) ? "0" : tc.getNroSecuencia().toString());
            dI.getCampos().add(campI1);
            CamposIn campI2 = new CamposIn();
            campI2.setIdCampo(4);
            campI2.setValor((tc == null) ? "0" : tc.getImporte().toString());
            dI.getCampos().add(campI2);
            CamposIn campI3 = new CamposIn();
            campI3.setIdCampo(5);
            campI3.setValor("" + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fecEmpoce, 1));
            dI.getCampos().add(campI3);

            ExpedienteIn eI = new ExpedienteIn();
            eI.setAsunto(titulo);
            eI.setCliente(cI);
            eI.setIdProceso(p_id_proceso);
            eI.setIdUsuarioCreacion(p_usu_creacion);
            eI.setIdUsuarioResponsable(p_usu_resp);
            eI.setObservacion(p_asunto);
            eI.getDocumentos().add(dI);
            
            CreacionExpedienteWs ress = port.crearExpediente(eI);

            return ress.getNumeroExpediente();

        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String crearExpedienteGT(AmaGuiaTransito gt, Cliente cli, String p_asunto, Integer p_id_proceso, Integer p_usu_creacion, Integer p_usu_resp, SbPersona propietario, String nroEmpoce, Date fechaEmpoce) {
        try {
            ServiciosSTD service = new ServiciosSTD();
            ServiciosSTDEndpoint port = service.getServiciosSTDEndpointPort();

//            ClienteWs cws = port.buscarCliente(cli.getIdTipoCliente().getIdTipoCliente().intValue(), cli.getNumeroIdentificacion());
            ClienteIn cI = new ClienteIn();
            cI.setApellidoPaterno(cli.getApellidoPaterno());
            cI.setApellidoMaterno(cli.getApellidoMaterno());
            cI.setNombres(cli.getNombre());
            cI.setRazonSocial(cli.getRazonSocial());
            cI.setTipoCliente(cli.getIdTipoCliente().getIdTipoCliente().toString());
            cI.setNumeroDocumentoIdentidad(cli.getNumeroIdentificacion());

            Date hoy = new Date();

            DocumentoIn dI = new DocumentoIn();
            dI.setTipoDocumento(25);
            dI.setFechaDocumento("" + JsfUtil.obtenerFechaXCriterio(hoy, 3) + "/"
                    + JsfUtil.leftpadString(JsfUtil.obtenerFechaXCriterio(hoy, 2), 2, "0")  + "/"
                    + JsfUtil.obtenerFechaXCriterio(hoy, 1));
            dI.setAsunto(p_asunto);
            dI.setIdAutor(p_usu_resp);

            CamposIn campI1 = new CamposIn();
            campI1.setIdCampo(3);
            campI1.setValor(nroEmpoce);
            dI.getCampos().add(campI1);

            String montoEmpoce = "0";
            switch(p_id_proceso){
                case 1069:
                    montoEmpoce = "35.50";
                    break;
                case 1592:
                    montoEmpoce = "64.00"; 
                    /*TUPA 2022 SE340066DA GAMAC DEPÓSITO TEMPORAL DE ARMAS DE FUEGO*/
                    /*Solicitud de depósito temporal de armas de fuego S/. 64.00 
                    Almacenamiento de cada arma corta S/. 61.00 
                    Almacenamiento de cada arma larga S/. 83.40*/
                    break;
            }
            if (nroEmpoce.equals("0")) {
                montoEmpoce = "0";
            }
            
            CamposIn campI2 = new CamposIn();
            campI2.setIdCampo(4);
            campI2.setValor(montoEmpoce);
            dI.getCampos().add(campI2);
            
            CamposIn campI3 = new CamposIn();
            campI3.setIdCampo(5);
            Date fechaCamI3 = hoy;
            if (!nroEmpoce.equals("0")) {
                fechaCamI3 = fechaEmpoce;
            }
            campI3.setValor("" + JsfUtil.obtenerFechaXCriterio(fechaCamI3, 3) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fechaCamI3, 2) + "/"
                    + JsfUtil.obtenerFechaXCriterio(fechaCamI3, 1));
            dI.getCampos().add(campI3);

            ExpedienteIn eI = new ExpedienteIn();
            eI.setAsunto(p_asunto);
            eI.setCliente(cI);
            eI.setIdProceso(p_id_proceso);
            eI.setIdUsuarioCreacion(p_usu_creacion);
//            eI.setObservacion("TIPO DE ARMA: " + tc.getModelo().getTipoArmaId().getNombre()
//                    + ",CALIBRE: " + tc.getModelo().getCalibre().getNombre()
//                    + ",NRO. SERIE: " + tc.getSerie()
//            );
            if (propietario != null) {
                eI.setObservacion("USUARIO AUTORIZADO DE RECOJO: " + propietario.getRznSocial() == null ? (propietario.getNombres() + " " + propietario.getApePat() + " " + propietario.getApeMat()) : (propietario.getRznSocial()));
            } else {
                eI.setObservacion("USUARIO AUTORIZADO DE RECOJO: " + gt.getSolicitudRecojoId().getPersonaAutRecojoId().getNombreCompleto());
            }

            eI.setIdUsuarioResponsable(p_usu_resp);
            eI.getDocumentos().add(dI);

            CreacionExpedienteWs ress = port.crearExpediente(eI);
            if (ress.getNumeroExpediente() == null) {
                JsfUtil.mensajeError(ress.getError());
            }
            return ress.getNumeroExpediente();

            //return eI.getNumeroExpedienteDicse();
        } catch (Exception e) {
            JsfUtil.mensajeError("Error de WEB SERVICE :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
}
