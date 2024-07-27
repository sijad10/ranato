/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPerfil;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
//import pe.gob.sucamec.sistemabase.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.sistemabase.data.SbRelacionPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.bdintegrado.bean.AmaModelosFacade;
import pe.gob.sucamec.sistemabase.beans.SbDireccionFacade;
import pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.sistemabase.beans.SbPersonaFacade;
import pe.gob.sucamec.bdintegrado.bean.SbReciboRegistroFacade;
import pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade;
import pe.gob.sucamec.sistemabase.beans.SbRelacionPersonaFacade;
import pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaArmaInventarioDif;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaDocumento;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaMuniciones;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioAccesorios;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.jsf.util.AmaGuiaTransitoGenericoController;
import pe.gob.sucamec.bdintegrado.jsf.util.ArmaRepClass;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.ParametroElementoClass;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtilTurno;
import pe.gob.sucamec.renagi.jsf.util.StringUtil;
import pe.gob.sucamec.bdintegrado.bean.DocumentoFacade;
import pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade;
import pe.gob.sucamec.bdintegrado.bean.UsuarioFacade;
import pe.gob.sucamec.bdintegrado.data.Documento;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
//import pe.gob.sucamec.turreg.ws.WsTramDoc;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.PideMigraciones;
import wspide.PideMigraciones_Service;
import pe.gob.sucamec.bdintegrado.jsf.AmaDepositoArmasController; 
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
@Named("amaDevolucionArmasController")
@SessionScoped
public class AmaDevolucionArmasController implements Serializable {

    private EstadoCrud estado;
    private static String devolucion = "DEV";
    private static String ver = "VER";
    private static final Long L_CERO = 0L, L_UNO = 1L, L_DOS = 2L, L_TRES = 3L, L_CUATRO = 4L, L_CINCO = 5L, L_SEIS = 6L, L_SIETE = 7L, L_OCHO = 8L;
    private Boolean isCoordinador = Boolean.FALSE;
    private Boolean isCoorVerificador = Boolean.FALSE;
    private Boolean isJefeZonal = Boolean.FALSE;
    private Boolean flagRegInvArma;
    private Boolean perfilConsulta;
    private Boolean muestraBusqFecha;
    private Boolean checkAllAcces;
    private Boolean checkAllMuni;
    private Boolean esRectificatoria;
    private Boolean disabledBtVal;
    private Boolean flagCollapsed;
    private Boolean flagTipoDev;
    private Boolean isJudicializada;
    private Date fechaHoy = new Date();
    private Date fechaEmiActa;
    private Date fechaIniL;
    private Date fechaFinL;
    private Date fechaCompBus;

    private String tipoAlmaFisico;
    private String filtroL;
    private String tipoBusComp;
    private String filtroComp;
    private String tipoPersona;
    private String obsDevolucion;
    private String nroExpediente;
    private String msjRegSolicitante;
    private String cantNumeros;
    private String msjConfirmacionDevolucion;
    private String selectedAmaArma;
    private ListDataModel<Map> listGuiasTraslado;
    private TipoGamac modoBus;
    private TipoGamac idTipoGuia;
    private TipoGamac idTipoGuiaDep;
    private TipoGamac subTipoSelect;
    private TipoGamac subSubTipoSelect;
    private TipoGamac tipoOrigenSelect;
    private List<TipoGamac> listTipoGuiaDep;
    private List<TipoGamac> listTipoGuiaDepBus;
    private List<TipoGamac> listTipoGuia;
    private Long idTipoBusquedaL;
    private SbDireccion dirSucamecBus;
    private SbDireccion selectedOriSucamec;
    private int checkTipoDeposito;
    private AmaGuiaTransito actaDepositoSelect;
    private AmaGuiaTransito registro;
    private AmaGuiaTransito actaDevolucionSelect;
    private AmaGuiaTransito actaDevolucionSelectL;
    private AmaGuiaTransito amaGuiaSelect;
    private AmaGuiaTransito amaGuiaSelectL;
    private AmaGuiaTransito actaDepositoSelectL;
    private SbPersona personaReg;
    private SbPersona newPersona;
    private SbPersona repreSelect;
    private SbPersonaGt representante;
    private List<SbPersonaGt> listRepresentante;
    private AmaInventarioArma invArmaDeposito;
    private List<AmaInventarioArma> listaInvArmasTemp;
    private List<AmaArma> listaArmasTemp;
    private List<AmaGuiaMuniciones> listGuiaMunicion;
    private List<AmaGuiaMuniciones> listGuiaMunicionSelect;
    private List<ParametroElementoClass> listAccesorioMostrar;
    private List<ParametroElementoClass> listMunicionMostrar;
    private List<ParametroElementoClass> listAcceSelect;
    private List<ParametroElementoClass> listMuniSelect;
    private List<Documento> listDocumentoTD;
    private List<Documento> listDocumentoTDSelect;
    private List<Documento> listDocTD;
    private List<Map> listaArmasFinalTemp;
    private List<Map> listGuiasTrasladoSelect;
    private ListDataModel<Map> listaArmasFinal;
    private List<AmaInventarioAccesorios> listAccesoriosGuia;
    private List<AmaInventarioAccesorios> listInvAcceMostrar;
    private List<AmaInventarioAccesorios> lstAccesoriosActa;
    private SbRecibos comprobantePago;
    private List<SbRecibos> lstRecibosBusqueda;
    private BigDecimal idUsuarioTramDoc;
    private Documento docTDSelect;
    private AmaDocumento documentoSelect;
    private List<AmaDocumento> listDocumentos;
    private TipoBaseGt estadoFinal;
    private SbUsuario usuLogueado;
    private Boolean renderDocumentoSustento;

    private SbTipo tipoDocSelect;
    private String numeroDoc;

    // FOTOS DE ARMA
    private List<Map> lstFotos;
    private List<Map> lstFotosTemp;
    private boolean disabledBtnFoto;
    private StreamedContent foto;
    private byte[] fotoByte;
    private StreamedContent fotoImg;
    private UploadedFile file;

    @EJB
    private AmaGuiaTransitoFacade ejbAmaGuiaTransitoFacade;
    @EJB
    private SbDireccionFacade ejbSbDireccionFacade;
    @EJB
    private SbUsuarioFacade ejbSbUsuarioFacade;
    @EJB
    private TipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private AmaArmaFacade ejbAmaArmaFacade;
    @EJB
    private AmaInventarioArmaFacade ejbAmaInventarioArmaFacade;
    @EJB
    private AmaModelosFacade ejbAmaModelosFacade;
    @EJB
    private SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private SbPersonaFacade ejbSbPersonaFacade;
    @EJB
    private ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private DocumentoFacade ejbDocumentoFacade;
    @EJB
    private AmaDocumentoFacade ejbAmaDocumentoFacade;
    @EJB
    private AmaGuiaMunicionesFacade ejbAmaGuiaMunicionesFacade;
    @EJB
    private SbReciboRegistroFacade ejbSbReciboRegistroFacade;
    @EJB
    private SbRelacionPersonaFacade ejbSbRelacionPersonaFacade;
    @EJB
    private AmaInventarioAccesoriosFacade ejbAmaInventarioAccesoriosFacade;
    @EJB
    private AmaCatalogoFacade ejbAmaCatalogoFacade;
    @EJB
    private AmaArmaInventarioDifFacade ejbAmaArmaInventarioDifFacade;
    @EJB
    private SbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private UsuarioFacade usuarioTDFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;
    @EJB
    private SbTipoFacade ejbSbTipoFacade;
            
    @Inject
    private LoginController loginController;
    @Inject
    private WsTramDoc wsTramDocController;
    @Inject
    private AmaGuiaTransitoGenericoController amaGuiaTransitoGenericoController;
    @Inject
    private AmaDepositoArmasController amaDepositoArmasController;

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public AmaGuiaTransito getAmaGuiaTransito(Long id) {
        return ejbAmaGuiaTransitoFacade.find(id);
    }

    public void actualizarVista() {
        setModoBus(null);
        setFiltroL(null);
        setListGuiasTraslado(null);
        setIdTipoBusquedaL(null);
    }

    public List<SbDireccion> listaDireccionSucamec() {
        List<SbDireccion> listRes = null;
        if (getPerfilConsulta() == null) {
            setPerfilConsulta(Boolean.FALSE);
        }
        if (!getPerfilConsulta() && (getIsCoorVerificador() || getIsJefeZonal())) {//isOrgAlmacen
            listRes = ejbSbDireccionFacade.listarDireccionesPermitidasXUsuario(L_UNO, objetoUsuario().getId(), objetoUsuario().getAreaId().getId());
        } else {
            listRes = ejbSbDireccionFacade.listarDireccionesSucamec();
        }
        List<SbDireccion> listTemp = new ArrayList(listRes);
        for (SbDireccion dir : listTemp) {
            if (Objects.equals("POR REGULARIZAR", dir.getReferencia())) {//temporalmente
                listRes.remove(dir);
            }
        }
        return listRes;
    }

    /**
     * FUNCIÓN QUE OBTIENE EL OBJETO USUARIO
     *
     * @author Gino Chávez
     * @return Usuario
     */
    public SbUsuario objetoUsuario() {
        List<SbUsuario> listRes = ejbSbUsuarioFacade.selectUsuarioXLogin(loginController.getUsuario().getLogin());
        List<SbUsuario> listTemp = new ArrayList<>();
        SbUsuario usu = new SbUsuario();
        listTemp.addAll(listRes);
        for (SbUsuario u : listTemp) {
            if (!u.getPersonaId().getId().equals(loginController.getUsuario().getPersona().getId())) {
                listRes.remove(u);
            }
        }
        if (!listRes.isEmpty()) {
            usu = listRes.get(0);
        }
        return usu;
    }

    /**
     * FUNCIÓN QUE REALIZA LA BÚSQUEDA DE ACTAS (GUÍAS DE TRÁNSITO), SEGÚN LOS
     * VALORES INGRESADOS.
     *
     * @author Gino Chávez
     */
    public void buscarActas() {
        Date fecha = new Date();
        String tipoBusqueda = StringUtil.VACIO;
        String msjValidacion = StringUtil.VACIO;
        if (L_CUATRO.equals(idTipoBusquedaL)) {
            filtroL = null;
        } else {
            fechaEmiActa = null;
        }
        setFechaIniL(fechaFinL = null);
        if ((tipoAlmaFisico != null && !StringUtil.VACIO.equals(tipoAlmaFisico.trim()))) {
            if (!perfilConsulta && (/*!isOrgAlmacen && */(getIsJefeZonal() || getIsCoorVerificador()) && dirSucamecBus == null)) {
                msjValidacion = "Por favor, seleccione el almacén de SUCAMEC.";
            } else if (/* && */((idTipoBusquedaL == null && (filtroL == null || StringUtil.VACIO.equals(filtroL.trim()))) && fechaEmiActa == null && idTipoGuiaDep == null && dirSucamecBus == null
                    && (getFechaIniL() == null && getFechaFinL() == null))) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda1");
            } else if (idTipoBusquedaL != null && (filtroL == null || StringUtil.VACIO.equals(filtroL.trim())) && fechaEmiActa == null) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda3");
            } else if (idTipoBusquedaL == null && ((filtroL != null && !StringUtil.VACIO.equals(filtroL.trim())) || fechaEmiActa != null)) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda4");
            } else if (/*isOrgAlmacen && */((idTipoBusquedaL == null && (filtroL == null || StringUtil.VACIO.equals(filtroL.trim())) && fechaEmiActa == null) && (idTipoGuiaDep != null))) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda9");
            } else if ((/*isOrgAlmacen && */(idTipoBusquedaL == null && (filtroL == null || StringUtil.VACIO.equals(filtroL.trim())) && fechaEmiActa == null)) && (dirSucamecBus != null)) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda10");
            }
        } else {
            msjValidacion = "Seleccione Tipo para realizar la búsqueda.";
        }
        if (StringUtil.VACIO.equalsIgnoreCase(msjValidacion)) {
            if (Objects.equals(idTipoBusquedaL, L_UNO)) {
                tipoBusqueda = "A";
            } else if (Objects.equals(idTipoBusquedaL, L_DOS)) {
                tipoBusqueda = "B";
            } else if (Objects.equals(idTipoBusquedaL, L_TRES)) {
                tipoBusqueda = "C";
            } else if (Objects.equals(idTipoBusquedaL, L_CUATRO)) {
                try {
                    tipoBusqueda = "D";
                    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                    fecha = formateador.parse(fechaSimple(fechaEmiActa));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                filtroL = null;
            } else if (Objects.equals(idTipoBusquedaL, L_CINCO)) {
                tipoBusqueda = "E";
            } else if (Objects.equals(idTipoBusquedaL, L_SEIS)) {
                tipoBusqueda = "F";
            } else if (Objects.equals(idTipoBusquedaL, L_SIETE)) {
                tipoBusqueda = "G";
            } else if (Objects.equals(idTipoBusquedaL, L_OCHO)) {
                tipoBusqueda = "H";
            } else if (Objects.equals(idTipoBusquedaL, 9L)) {
                tipoBusqueda = "I";
            } else if (Objects.equals(idTipoBusquedaL, 10L)) {
                tipoBusqueda = "J";
            }

            List<Map> listGTTemporal = null;
//            String cad = StringUtil.VACIO;
//            cad = listaIdTipoGuiaBus(devolucion);
            listGTTemporal = ejbAmaGuiaTransitoFacade.obtenerActasDepDev(tipoBusqueda, (filtroL != null ? filtroL.trim().toUpperCase() : null), idTipoGuiaDep, fecha, (/*isOrgAlmacen ? objetoUsuario().getAreaId() :*/null), dirSucamecBus, (/*isOrgAlmacen ? null :*/ejbTipoBaseFacade.tipoPorCodProg("TP_GTGAMAC_FIN")), modoBus, getFechaIniL(), getFechaFinL(), null, null, null, null, "DEP".equals(tipoAlmaFisico) ? "TP_GTGAMAC_ALM" : "TP_GTGAMAC_DEV", Boolean.FALSE);
            listGuiasTraslado = new ListDataModel(listGTTemporal);
        } else {
            listGuiasTraslado = new ListDataModel<>(new ArrayList<Map>());
            JsfUtil.mensajeAdvertencia(msjValidacion);
            return;
        }
    }

    /**
     * FUNCIÓN QUE OBTIENE LA CADENA CON LOS IDS DE LOS TIPOS DE GUÍAS A LISTAR.
     *
     * @param origenBusq, String. Pantalla origen:GUÍAS DE TRÁNSITO, DEPÓSITO O
     * DEVOLUCIÓN.
     * @return Cadena con ids.
     * @author Gino Chávez
     */
    public String listaIdTipoGuiaBus(String origenBusq) {
        String cadena = StringUtil.VACIO;
        List<Long> listRes = new ArrayList<>();
        List<TipoGamac> listaTemp = null;
        listaTemp = ejbTipoGamacFacade.selectTipoGamac("TP_GTGAMAC_DEV");
        for (TipoGamac tipoGamac : listaTemp) {
            listRes.add(tipoGamac.getId());
        }
        listaTemp.clear();
        int tam = listRes.size();
        int j = 1;
        for (Long id : listRes) {
            cadena += id;
            if (j < tam) {
                cadena += ",";
            }
            j++;
        }
        return cadena;
    }

    /**
     * CAMBIAR CAMPO FECHA DE BUSQUEDA
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiaTipoBusquedaComprobante() {
        setFiltroComp(null);
        setFechaCompBus(null);
        setMuestraBusqFecha(Boolean.FALSE);
        if (tipoBusComp != null) {
            if (tipoBusComp.equals("fecha")) {
                setMuestraBusqFecha(Boolean.TRUE);
            }
        }
    }

    public void prepareCrearDevolucion(AmaGuiaTransito actaDeposito) {
        limpiarDatosDepDev();
        inicializarListasArmas();
        idUsuarioTramDoc = (usuarioTDFacade.obtenerIdUsuarioTD(loginController.getUsuario().getLogin())).get(0);
        usuLogueado = amaGuiaTransitoGenericoController.objetoUsuario(loginController.getUsuario());
        obtenerCantPerfiles();
        setTipoAlmaFisico(devolucion);
        setearDatosDevolucion(actaDeposito);
        setEstado(EstadoCrud.CREARDEV);
        setIdTipoGuia(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTGAMAC_DEV"));
        inicializarOrigenDestino(Boolean.FALSE);
        renderDocumentoSustento = mostrarDocumentosSustento();
    }

    public void prepareEditarDevolucion(Long idActaDep) {
        limpiarDatosDepDev();
        idUsuarioTramDoc = (usuarioTDFacade.obtenerIdUsuarioTD(loginController.getUsuario().getLogin())).get(0);
        usuLogueado = amaGuiaTransitoGenericoController.objetoUsuario(loginController.getUsuario());
        actaDevolucionSelect = ejbAmaGuiaTransitoFacade.find(idActaDep);
        actaDepositoSelect = actaDevolucionSelect.getGuiaReferenciadaId();
        personaReg = actaDevolucionSelect.getSolicitanteId();
        if ("TP_PER_JUR".equalsIgnoreCase(personaReg.getTipoId().getCodProg())) {
            repreSelect = actaDevolucionSelect.getRepresentanteId();
        }
        obtenerInvArma(actaDevolucionSelect);
        obtenerListDocTD(actaDevolucionSelect);
        obtenerListMunicionEditar(actaDepositoSelect, actaDevolucionSelect);
        if (!JsfUtil.isNullOrEmpty(actaDepositoSelect.getAmaInventarioAccesoriosList())) {
            obtenerListAccesoriosActaEditar(actaDepositoSelect, actaDevolucionSelect);
        } else {
            obtenerFotosYAccesoriosArma(actaDepositoSelect, Boolean.TRUE);
        }
        idTipoGuia = procesarTipoGuia(actaDevolucionSelect.getTipoGuiaId(), "EDITAR");
        obsDevolucion = actaDevolucionSelect.getObservacion();
        listDocumentos = actaDevolucionSelect.getAmaDocumentoList();
        estado = EstadoCrud.EDITARDEV;
        renderDocumentoSustento = mostrarDocumentosSustento();
    }

    /**
     * FUNCIÓN QUE VALIDA EL PERFIL DEL USUARIO LOGUEADO
     *
     * @return
     * @author Gino Chávez
     */
    public int obtenerCantPerfiles() {
        int c = 0;
        List<SbPerfil> listPerfil = usuLogueado.getSbPerfilList();
        for (SbPerfil p : listPerfil) {
            switch (p.getCodProg()) {
                case "AMA_COOCOM":
                    isCoordinador = Boolean.TRUE;
                    c += 1;
                    break;
                case "AMA_COOVER"://Coordinador arsenales
                    isCoorVerificador = Boolean.TRUE;
                    c += 1;
                    break;
                case "AMA_GERODS"://Gerente de la Jefatura zonal
                    isJefeZonal = Boolean.TRUE;
                    c += 1;
                    break;
                case "AMA_CTAARS"://Consulta arsenales
                    perfilConsulta = Boolean.TRUE;
                    c += 1;
                    break;
                default:
                    break;
            }
        }
        return c;
    }

//    public void accionCrearGuiaDepDev(Long idActaDep) {
//        if (idActaDep == null || amaGuiaTransitoGenericoController.isDevolucionValida(idActaDep)) {
//            limpiarDatosDepDev();
//            inicializarListasArmas();
//            setFlagRegInvArma(Boolean.FALSE);
//            setCheckTipoDeposito(0);
//            if (idActaDep != null) {
//                tipoAlmaFisico = devolucion;
//                setActaDepositoSelect(ejbAmaGuiaTransitoFacade.find(idActaDep));
//                setearDatosDevolucion();
//            }
//            estado = EstadoCrud.CREARDEV;
//            setIdTipoGuia(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTGAMAC_DEV"));
//        }
//    }
    public void inicializarOrigenDestino(Boolean mostrarValidacion) {
//        guardarValoresInicialesPorTG();
//        limpiarDatosTraslado();
//        inicializarcamposPerOriDest();
        if (idTipoGuia != null) {
            if (mostrarValidacion) {
                adecuarListaArmas(amaGuiaTransitoGenericoController.validarOrigenArma(devolucion, idTipoGuia, subTipoSelect), L_UNO);
            }
//            obtenerListasTipoOrgDest();
//            if (mostrarValidacion && idTipoGuia.equals(listIdTipoGuiaSelect.get(0))) {
//                restablecerValIniTipoOrgDest();
//            }
        }
//        RequestContext.getCurrentInstance().update("registroForm:datosTraslado");
//        RequestContext.getCurrentInstance().update("editarForm:datosTraslado");
    }

    public void adecuarListaArmas(Long idOrigenArma, Long idOrigenLLamada) {
        //se aprovecha para nulear el modo
//        modoSelect = null;
//        tipoInterSelect = null;
//        disableModo = Boolean.FALSE;
//        //
//        idOrigenTipoGuia = idOrigenLLamada;
//        //Eliminación de la direccion de origen seleccionada.
//        if (/*isOrgAlmacen &&*/!"TP_GTALM_VENCAN".equals(subTipoSelect.getCodProg())) {
//            tipoOrigenSelect = null;
//            eliminarDireccionOrg();
//        }
//        //
//        if (listIdSubTipoGuiaSelect == null) {
//            listIdSubTipoGuiaSelect = new ArrayList<>();
//        }
//        if (0 == listIdSubTipoGuiaSelect.size() && subTipoSelect != null) {
//            listIdSubTipoGuiaSelect.add(subTipoSelect);
//        }
        if (selectedAmaArma != null) {
            AmaInventarioArma amaInv = ejbAmaInventarioArmaFacade.find(Long.valueOf(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "0", L_UNO)));
            if ((amaInv != null && amaInv.getPropietarioId() == null)
                    && !"TP_GTALM_DIPJMP".equals(subTipoSelect.getCodProg())
                    && !"TP_GTALM_DEC".equals(subTipoSelect.getCodProg())
                    && !"TP_GTALM_INC".equals(subTipoSelect.getCodProg())) {
                selectedAmaArma = null;
//                RequestContext.getCurrentInstance().update("registroForm:gridDatosArma");
//                RequestContext.getCurrentInstance().update("editarForm:gridDatosArma");
            }
        }
    }

//    public void obtenerListasTipoOrgDest() {
//        List<TipoGamac> listO = new ArrayList<>();
//        List<TipoGamac> listD = new ArrayList<>();
////        listOrigen = new ArrayList<>();
////        listDestino = new ArrayList<>();
//        switch (idTipoGuia.getCodProg()) {
//            case "TP_GTGAMAC_REC":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                subTipoSelect = null;
//                break;
//            case "TP_GTGAMAC_EXH":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                subTipoSelect = null;
//                break;
//            case "TP_GTGAMAC_POL":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_POL"));
//                subTipoSelect = null;
//                break;
//            case "TP_GTGAMAC_IMP":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMA"));
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_PUER"));
//
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                subTipoSelect = null;
//                break;
//            case "TP_GTGAMAC_ALM":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_POL"));
//
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                break;
//            case "TP_GTGAMAC_DEV":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_POL"));
//                break;
//            case "TP_GTGAMAC_DON":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_DON"));
//                break;
//            case "TP_GTGAMAC_AGE":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                subTipoSelect = null;
////                setFlagCompletePer(Boolean.TRUE);
////                setOcultarCompleteDirOri(Boolean.TRUE);
////                setOcultarCompleteDirDest(Boolean.TRUE);
//                break;
//            case "TP_GTGAMAC_SUC":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                break;
//            case "TP_GTGAMAC_COL":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                subTipoSelect = null;
//                break;
//            case "TP_GTGAMAC_MUN":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMA"));
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_PUER"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                subTipoSelect = null;
//                break;
//            case "TP_GTGAMAC_REI":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                subTipoSelect = null;
//                break;
//            case "TP_GTGAMAC_SAL":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMA"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_PUER"));
//                subTipoSelect = null;
//                break;
//            case "TP_GTGAMAC_DDV":
//                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
//                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
//                subTipoSelect = null;
//                break;
//            default:
//                break;
//        }
//        setListOrigen(listO);
//        setListDestino(listD);
//    }
    public void accionEditarDepDev(Map itemGuia) {
        limpiarDatosDepDev();
        actaDevolucionSelect = ejbAmaGuiaTransitoFacade.find(Long.valueOf(itemGuia.get("ID").toString()));
        actaDepositoSelect = actaDevolucionSelect.getGuiaReferenciadaId();
        personaReg = actaDevolucionSelect.getSolicitanteId();
        if ("TP_PER_JUR".equalsIgnoreCase(personaReg.getTipoId().getCodProg())) {
            repreSelect = actaDevolucionSelect.getRepresentanteId();
        }
        obtenerInvArma(actaDevolucionSelect);
        obtenerListDocTD(actaDevolucionSelect);
        obtenerListMunicionEditar(actaDepositoSelect, actaDevolucionSelect);
        if (!JsfUtil.isNullOrEmpty(actaDepositoSelect.getAmaInventarioAccesoriosList())) {
            obtenerListAccesoriosActaEditar(actaDepositoSelect, actaDevolucionSelect);
        } else {
            obtenerFotosYAccesoriosArma(actaDepositoSelect, Boolean.TRUE);
        }
        idTipoGuia = procesarTipoGuia(actaDevolucionSelect.getTipoGuiaId(), "EDITAR");
        obsDevolucion = actaDevolucionSelect.getObservacion();
        setListDocumentos(actaDevolucionSelect.getAmaDocumentoList());
        estado = EstadoCrud.EDITARDEV;
    }

    public String obtenerNombrePersona(SbPersona persona) {
        String res = StringUtil.VACIO;
        if (persona != null) {
            res = persona.getRznSocial() == null ? persona.getNombres() + StringUtil.ESPACIO + persona.getApePat() + StringUtil.ESPACIO + (persona.getApeMat() == null ? StringUtil.VACIO : persona.getApeMat()) : persona.getRznSocial();
        }
        return res;
    }

    public void obtenerInvArma(AmaGuiaTransito amaGuia) {
        for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
            if (invArma.getActivo() == JsfUtil.TRUE && invArma.getActual() == JsfUtil.TRUE) {
                invArmaDeposito = invArma;
            } else {
                JsfUtil.mensajeError("El arma ha sido eliminado.");
            }
            break;
        }
    }

    public void obtenerListDocTD(AmaGuiaTransito amaGuia) {
        for (AmaDocumento amaDoc : amaGuia.getAmaDocumentoList()) {
            //Se obtiene el número del expediente del documento ingresado.
            nroExpediente = amaDoc.getNroExpediente();
            break;
        }
        List<Expediente> listTemp = ejbExpedienteFacade.selectExpedienteXNro(nroExpediente);
        BigDecimal idExpediente = BigDecimal.ZERO;
        if (!JsfUtil.isNullOrEmpty(listTemp)) {
            idExpediente = listTemp.get(0).getIdExpediente();
        }
        cargarDocumentos();
        listDocumentoTD = new ArrayList<>();
        if (listDocTD != null) {
            for (Documento docTD : listDocTD) {
                for (AmaDocumento amaDoc : amaGuia.getAmaDocumentoList()) {
                    if (idExpediente.equals(docTD.getExpediente().getIdExpediente())
                            && amaDoc.getAsunto().equalsIgnoreCase(docTD.getIdTipoDocumento().getNombre())
                            && amaDoc.getNumero().equals(docTD.getNumero())
                            && (JsfUtil.getFechaSinHora(amaDoc.getFechaFirma()).compareTo(JsfUtil.getFechaSinHora(docTD.getFechaCreacion())) == 0)) {
                        listDocumentoTD.add(docTD);
                    }
                }
            }
        }
        listDocTD = null;
        nroExpediente = null;
    }

    public void obtenerListMunicionEditar(AmaGuiaTransito actaDep, AmaGuiaTransito actaDev) {
        if (listMunicionMostrar == null) {
            listMunicionMostrar = new ArrayList<>();
        }
        ParametroElementoClass amaMuniMostrar = null;
        List<AmaGuiaMuniciones> listMuniDepActivos = obtenerListMuniActivos(actaDep);
        List<AmaGuiaMuniciones> listMuniDevActivos = obtenerListMuniActivos(actaDev);
        if (listMuniDepActivos != null) {
            for (AmaGuiaMuniciones muniDep : listMuniDepActivos) {
                Boolean check = Boolean.FALSE;
                amaMuniMostrar = new ParametroElementoClass();
                amaMuniMostrar.setId(muniDep.getId());
                amaMuniMostrar.setL_valor1(muniDep.getMunicionId().getId());
                amaMuniMostrar.setS_valor1(muniDep.getMunicionId().getCalibrearmaId() == null ? StringUtil.VACIO : muniDep.getMunicionId().getCalibrearmaId().getNombre());

                amaMuniMostrar.setS_valor2((muniDep.getMunicionId().getMarcaId() == null ? StringUtil.VACIO : muniDep.getMunicionId().getMarcaId().getNombre()) + StringUtil.VACIO
                        + (muniDep.getMunicionId().getDenominacionId() == null ? StringUtil.VACIO : muniDep.getMunicionId().getDenominacionId().getNombre()) + StringUtil.VACIO
                        + (muniDep.getMunicionId().getNroPerdigonId() == null ? StringUtil.VACIO : muniDep.getMunicionId().getNroPerdigonId().getNombre()) + StringUtil.VACIO
                        + (muniDep.getMunicionId().getPesoMunicionGranos() == null ? StringUtil.VACIO : muniDep.getMunicionId().getPesoMunicionGranos()));
                amaMuniMostrar.setD_valor1(muniDep.getCantidad());
                amaMuniMostrar.setS_valor3(muniDep.getUmedidaId() == null ? StringUtil.VACIO : muniDep.getUmedidaId().getNombre());
                amaMuniMostrar.setD_valor2(muniDep.getPeso());
                if (listMuniDevActivos != null) {
                    for (AmaGuiaMuniciones muniDev : listMuniDevActivos) {
                        if (muniDep.getMunicionId().equals(muniDev.getMunicionId())) {
                            check = Boolean.TRUE;
                            break;
                        }
                    }
                }
                amaMuniMostrar.setCheck(check);
                if (check) {
                    if (listMuniSelect == null) {
                        listMuniSelect = new ArrayList<>();
                    }
                    listMuniSelect.add(amaMuniMostrar);
                }
                getListMunicionMostrar().add(amaMuniMostrar);
            }
        }
    }

    public void obtenerListAccesoriosActaEditar(AmaGuiaTransito actaDep, AmaGuiaTransito actaDev) {
        if (listAccesorioMostrar == null) {
            listAccesorioMostrar = new ArrayList<>();
        }
        ParametroElementoClass invAccesMostrar = null;
        List<AmaInventarioAccesorios> listAccesDepActivos = amaGuiaTransitoGenericoController.obtenerListAccesActaActivos(actaDep);
        List<AmaInventarioAccesorios> listAccesDevActivos = amaGuiaTransitoGenericoController.obtenerListAccesActaActivos(actaDev);
        if (listAccesDepActivos != null) {
            for (AmaInventarioAccesorios accesDep : listAccesDepActivos) {
                Boolean check = Boolean.FALSE;
                invAccesMostrar = new ParametroElementoClass();
                invAccesMostrar.setId(accesDep.getId());
                invAccesMostrar.setS_valor1(accesDep.getArticuloConexoId().getNombre());
                invAccesMostrar.setL_valor1(accesDep.getCantidad());
                if (listAccesDevActivos != null) {
                    for (AmaInventarioAccesorios accesDev : listAccesDevActivos) {
                        if (Objects.equals(accesDep.getArticuloConexoId(), accesDev.getArticuloConexoId())) {
                            check = Boolean.TRUE;
                            break;
                        }
                    }
                }
                invAccesMostrar.setCheck(check);
                if (check) {
                    if (listAcceSelect == null) {
                        listAcceSelect = new ArrayList<>();
                    }
                    listAcceSelect.add(invAccesMostrar);
                }
                getListAccesorioMostrar().add(invAccesMostrar);
            }
        }
    }

    public AmaCatalogo obtenerTipoDeposito(AmaGuiaTransito amaGuia) {
        AmaCatalogo tipoArt = null;
        if (amaGuia.getAmaInventarioArmaList() != null) {
            for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
                //Obtiene el tipo de articulo del arma seleccionada y prepara el formulario para mostrarlo.
                tipoArt = invArma.getModeloId().getTipoArticuloId();
                break;
            }
        }
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaGuiaMunicionesList())) {
            //Obtiene el tipo de articulo del arma seleccionada y prepara el formulario para mostrarlo.
            tipoArt = ejbAmaCatalogoFacade.obtenerPorCodProg("TP_ART_MUNI");
            //
        }
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioAccesoriosList())) {
            tipoArt = ejbAmaCatalogoFacade.obtenerPorCodProg("TP_ART_ACCE");
            lstAccesoriosActa = new ArrayList<>();
            for (AmaInventarioAccesorios acces : amaGuia.getAmaInventarioAccesoriosList()) {
                if (acces.getActivo() == JsfUtil.TRUE) {
                    getLstAccesoriosActa().add(acces);
                }
            }
        }
        return tipoArt;
    }

    public TipoGamac procesarTipoGuia(TipoGamac tipoGuiaIn, String origen) {
        TipoGamac tipoGuiaTemp1 = null;
        TipoGamac tipoGuiaTemp2 = null;
        tipoGuiaTemp1 = tipoGuiaIn;
        tipoGuiaTemp2 = tipoGuiaTemp1.getTipoId();
        if (tieneSubTipo(tipoGuiaTemp2.getCodProg())) {
            if (tieneSubTipo(tipoGuiaTemp2.getTipoId().getCodProg())) {
                if (ver.equalsIgnoreCase(origen)) {
                    getActaDepositoSelectL().setTipoGuiaId(tipoGuiaTemp2.getTipoId());
                } else {
                    idTipoGuia = tipoGuiaTemp2.getTipoId();
                }
                subTipoSelect = tipoGuiaTemp2;
                subSubTipoSelect = tipoGuiaTemp1;
            } else {
                if (ver.equalsIgnoreCase(origen)) {
                    getActaDepositoSelectL().setTipoGuiaId(tipoGuiaTemp1.getTipoId());
                } else {
                    idTipoGuia = tipoGuiaTemp1.getTipoId();
                }
                subTipoSelect = tipoGuiaTemp1;
                subSubTipoSelect = null;
            }
        } else {
            if (ver.equalsIgnoreCase(origen)) {
                getActaDepositoSelectL().setTipoGuiaId(tipoGuiaTemp1);
            } else {
                idTipoGuia = tipoGuiaTemp1;
            }
            subTipoSelect = null;
            subSubTipoSelect = null;
        }
        //El retorno solamente se hizo para el autocompletar clave en la devolución de armas y 
        //el popup de notificación de Guías Firmadas. Además para añadir nueva arma a inventario arma
        return idTipoGuia;
    }

    public void setearDatosDevolucion(AmaGuiaTransito actaDeposito) {
        if (actaDeposito != null) {
            actaDepositoSelect = actaDeposito;
            if (!JsfUtil.isNullOrEmpty(actaDepositoSelect.getAmaInventarioArmaList())) {
                for (AmaInventarioArma invArma : actaDepositoSelect.getAmaInventarioArmaList()) {
                    if (invArma.getActivo() == JsfUtil.TRUE) {
                        setInvArmaDeposito(invArma);
                        break;
                    }
                }
            }
            //Lista de Accesorios
            obtenerFotosYAccesoriosArma(actaDepositoSelect, Boolean.TRUE);
            //Seteo de tipo de devolución
            setearTipoDevolucion(actaDeposito);
        }
        //Lista de municiones
        obtenerListaMuniciones(obtenerListMuniActivos(actaDepositoSelect), Boolean.TRUE);
    }

    /**
     * FUNCIÓN QUE SETEA EL TIPO DE DEVOLUCIÓN DE ACUERDO AL TIPO DE DEPÓSITO
     * DEL ARMA.
     *
     * @param actaDeposito
     */
    public void setearTipoDevolucion(AmaGuiaTransito actaDeposito) {
        flagTipoDev = Boolean.FALSE;
        if (actaDeposito != null) {
            switch (actaDeposito.getTipoGuiaId().getCodProg()) {
                case "TP_GTALM_MANVOL":
                case "TP_GTALM_FALTIT":
                case "TP_GTALM_VENCAN":
                //Subtipos del tipo de depósito: Por disposisción aduanera.
                case "TP_DISADU_AUTPE":
                case "TP_DISADU_NOAUT":
                case "TP_GTALM_INDEF":
                case "TP_GTALM_SEGUSO":
                case "TP_GTALM_HPR":
                    //Fin
                    subTipoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTDEV_DEPTEM");//Por depósito temporal
                    break;
                case "TP_GTALM_VENPLA":
                    subTipoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTDEV_VENREC");//Arma nueva con plazo de recojo vencida
                    break;
                case "TP_GTALM_DIPJMP":
                    subTipoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTDEV_MANJUD");//Por mandato Judicial
                    break;
                case "TP_GTALM_INC":
                    //EVALUAR
                    if (Objects.equals(actaDeposito.getInstitucionOrdenaId() == null ? StringUtil.VACIO : actaDeposito.getInstitucionOrdenaId().getCodProg(), "TP_INST_SUC")) {
                        subTipoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTDEV_RESINC");//Por incautación de SUCAMEC
                    } else {
                        //Se habilitará el combo tipo de devolución para que el usuario pueda seleccionarlo.
                        flagTipoDev = Boolean.TRUE;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void cambiarEstadoActa(String codEstado) {
        if (!JsfUtil.isNullOrEmpty(listGuiasTrasladoSelect)) {
            List<Map> listTemp = new ArrayList(listGuiasTrasladoSelect);
            for (Map acta : listTemp) {
                if (!amaGuiaTransitoGenericoController.flagActaSeleccionable(acta)) {
                    listGuiasTrasladoSelect.remove(acta);
                }
            }
            for (Map acta : listGuiasTrasladoSelect) {
                if (acta != null) {
                    if (acta.get("ID") != null) {
                        AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.find(acta.get("ID") == null ? L_CERO : Long.valueOf(acta.get("ID").toString()));
                        if (amaGuia != null) {
                            amaGuia.setEstadoId(ejbTipoBaseFacade.tipoPorCodProg(codEstado));
                            ejbAmaGuiaTransitoFacade.edit(amaGuia);
                        }
                    }
                }
            }
            buscarActas();
//            RequestContext.getCurrentInstance().update("listaActasArmaForm:listaActas");
        } else {
            JsfUtil.mensajeAdvertencia("Seleccione el Acta a ser rectificada.");
        }
    }

    public void obtenerFotosYAccesoriosArma(AmaGuiaTransito acta, Boolean actualizaCheck) {
        if (listAccesorioMostrar == null) {
            listAccesorioMostrar = new ArrayList<>();
        }
        List<AmaInventarioAccesorios> listAcces = new ArrayList<>();
        if (!JsfUtil.isNullOrEmpty(acta.getAmaInventarioArmaList())) {
            for (AmaInventarioArma invArma : acta.getAmaInventarioArmaList()) {
                if (invArma.getActivo() == JsfUtil.TRUE && invArma.getActual() == JsfUtil.TRUE) {
                    setLstFotosTemp(null);
                    cargarFotos(invArma, Boolean.FALSE);
                    listAcces.addAll(invArma.getAmaInventarioAccesoriosList());
                }
                break;
            }
        } else if (!JsfUtil.isNullOrEmpty(acta.getAmaInventarioAccesoriosList())) {
            listAcces.addAll(acta.getAmaInventarioAccesoriosList());
            setListAccesoriosGuia((List<AmaInventarioAccesorios>) new ArrayList(listAcces));
        }
        if (!listAcces.isEmpty()) {
            ParametroElementoClass invAccesMostrar = null;
            for (AmaInventarioAccesorios acces : listAcces) {
                if (acces.getActivo() == JsfUtil.TRUE) {
                    invAccesMostrar = new ParametroElementoClass();
                    invAccesMostrar.setId(acces.getId());
                    invAccesMostrar.setS_valor1(acces.getArticuloConexoId().getNombre());
                    invAccesMostrar.setL_valor1(acces.getCantidad());
                    if (actualizaCheck) {
                        if (acces.getEstadoDeposito() == JsfUtil.FALSE) {
                            invAccesMostrar.setCheck(Boolean.TRUE);
                        } else {
                            invAccesMostrar.setCheck(Boolean.FALSE);
                        }
                        getListAccesorioMostrar().add(invAccesMostrar);
                    } else if (devolucion.equals(tipoAlmaFisico)) {
                        if (acces.getEstadoDeposito() == JsfUtil.FALSE) {
                            getListAccesorioMostrar().add(invAccesMostrar);
                        }
                    }
                }
            }
        }
    }

    public List<AmaGuiaMuniciones> obtenerListMuniActivos(AmaGuiaTransito amaGuia) {
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaGuiaMunicionesList())) {
            listGuiaMunicion = new ArrayList<>();
            for (AmaGuiaMuniciones muniTemp : amaGuia.getAmaGuiaMunicionesList()) {
                if (muniTemp.getActivo() == JsfUtil.TRUE) {
                    listGuiaMunicion.add(muniTemp);
                }
            }
        } else {
            listGuiaMunicion = null;
        }
        //Return lista se usa solamente para el reporte en pdf
        return listGuiaMunicion;
    }

    /**
     * FUNCION PARA CARGAR LAS FOTOS
     *
     * @author Gino Chávez
     * @version 1.0
     * @param invArma
     * @param isArmaNueva
     */
    public void cargarFotos(AmaInventarioArma invArma, Boolean isArmaNueva) {
        boolean cargaDatos = true;
        Map mapa = null;
        setDisabledBtnFoto(true);
        lstFotos = new ArrayList();
        if (invArma.getFoto1() != null && invArma.getFoto2() != null) {
            if (lstFotosTemp == null) {
                lstFotosTemp = new ArrayList<>();
            }
            try {
                if (!JsfUtil.isNullOrEmpty(lstFotosTemp)) {
                    for (Map temp : lstFotosTemp) {
                        if (Objects.equals((Long) temp.get("idArma"), invArma.getId())) {
                            if (Objects.equals((String) temp.get("nombre"), invArma.getFoto1()) || Objects.equals((String) temp.get("nombre"), invArma.getFoto2()) || Objects.equals((String) temp.get("nombre"), invArma.getFoto3())) {
                                mapa = temp;
                                lstFotos.add(mapa);
                            }
                        }
                    }
                } else {
                    int cantFotos = 2;
                    if (invArma.getFoto3() != null) {
                        cantFotos = 3;
                    } else {
                        //Si no se guardó el valor de una tercera foto, entonces debe habilitarse el botón para agregar otra foto.
                        setDisabledBtnFoto(false);
                    }
                    try {
                        for (int i = 0; i < cantFotos; i++) {
                            mapa = new HashMap();
                            switch (i) {
                                case 0:
                                    if (invArma.getFoto1() != null) {
                                        mapa.put("nro", i + 1);//                if (!isArmaNueva && invArma.getId() > 0) {
                                        mapa.put("idArma", invArma.getId());
                                        mapa.put("nombre", invArma.getFoto1());
                                        mapa.put("byte", FileUtils.readFileToByteArray(new File((ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto_inventario").getValor()) + invArma.getFoto1())));
                                        mapa.put("isAdjunto", (invArma.getFoto1().startsWith("ADJ") ? JsfUtil.TRUE : JsfUtil.FALSE));
                                    }
                                    break;
                                case 1:
                                    if (invArma.getFoto2() != null) {
                                        mapa.put("nro", i + 1);
                                        mapa.put("idArma", invArma.getId());
                                        mapa.put("nombre", invArma.getFoto2());
                                        mapa.put("byte", FileUtils.readFileToByteArray(new File((ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto_inventario").getValor()) + invArma.getFoto2())));
                                        mapa.put("isAdjunto", (invArma.getFoto2().startsWith("ADJ") ? JsfUtil.TRUE : JsfUtil.FALSE));
                                    }
                                    break;
                                case 2:
                                    if (invArma.getFoto3() != null) {
                                        mapa.put("nro", i + 1);
                                        mapa.put("idArma", invArma.getId());
                                        mapa.put("nombre", invArma.getFoto3());
                                        mapa.put("byte", FileUtils.readFileToByteArray(new File((ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto_inventario").getValor()) + invArma.getFoto3())));
                                        mapa.put("isAdjunto", (invArma.getFoto3().startsWith("ADJ") ? JsfUtil.TRUE : JsfUtil.FALSE));
                                    }
                                    break;
                                default:
                                    break;
                            }
                            lstFotos.add(mapa);
                        }
                    } catch (Exception e) {
                        JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorCargaFotosArma"));
                    }
                }
                for (Map itemFoto : lstFotos) {
                    if (!lstFotosTemp.contains(itemFoto)) {
                        lstFotosTemp.add(itemFoto);
                    }
                }
                if (lstFotos.size() < 3) {
                    setDisabledBtnFoto(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                cargaDatos = false;
            }
            if (!cargaDatos) {
                JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorCargaFotosArma"));
            }
        } else if (lstFotos != null && lstFotos.size() == 3) {
            setDisabledBtnFoto(true);
        } else if (lstFotos.size() == 3) {
            setDisabledBtnFoto(true);
        } else {
            setDisabledBtnFoto(false);
        }
    }

    public void cargarDocumentos() {
        setListDocumentoTD(null);
        setListDocTD(null);
        if (nroExpediente != null && !StringUtil.VACIO.equals(nroExpediente.trim())) {
            if (ejbExpedienteFacade.expedientesAsignadosXIdUsuario(idUsuarioTramDoc == null ? StringUtil.VACIO : idUsuarioTramDoc.toString(), nroExpediente)) {
                listDocumentoTD = new ArrayList<>();
                //Bloque que valida que al ingresar otro número de expediente y si ya existiera una lista con documentos, entonces se elimina la lista
                //ya que solamente se pueden adjuntar documentos de un mismo expediente.
                Boolean remover = Boolean.FALSE;
                for (Documento doc : getListDocumentoTD()) {
                    if (!nroExpediente.equalsIgnoreCase(doc.getExpediente().getNumero())) {
                        remover = Boolean.TRUE;
                    }
                    break;
                }
                if (remover) {
                    setListDocumentoTD(null);
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeListaDocEliminado"));
                }
                //Fin Bloque
                setListDocTD(ejbDocumentoFacade.listDocumentoPorNroExp(nroExpediente));
                if (!JsfUtil.isNullOrEmpty(listDocTD)) {
                    List<Documento> listTemp = new ArrayList<>();
                    listTemp.addAll(listDocTD);
                    for (Documento doc : listTemp) {
                        if (!"1".equals(doc.getIdTipoDocumento().getIdTipoDocumento().toString())
                                && !"2".equals(doc.getIdTipoDocumento().getIdTipoDocumento().toString())
                                && !"4".equals(doc.getIdTipoDocumento().getIdTipoDocumento().toString())
                                && !"6".equals(doc.getIdTipoDocumento().getIdTipoDocumento().toString())
                                && !"12".equals(doc.getIdTipoDocumento().getIdTipoDocumento().toString())) {
                            listDocTD.remove(doc);
                        }
                    }
                    if (listDocTD.isEmpty()) {
                        //JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeDocumentosNoValidos"));
                    }
                } else {
                    //JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeNoExisteDocumento"));
                }
            } else {
                JsfUtil.mensajeError(JsfUtil.bundle("MensajeExpedienteNoExiste"));
            }
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeIngreseNroExpediente"));
        }
    }

    /**
     * Función para campo almacén autocompletable
     *
     * @param per Propietario a buscar
     * @return Descripción de propietario (Tipo Doc : Nro. Documento)
     * @author Gino Chávez
     * @version 1.0
     */
    public String obtenerDescPersona(SbPersona per) {
        String nombre = StringUtil.VACIO;

        if (per != null) {
            if (per.getTipoDoc() == null) {
                if (per.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    nombre = "RUC: " + per.getRuc() + StringUtil.ESPACIO + StringUtil.GUION + StringUtil.ESPACIO + per.getRznSocial();
                } else {
                    nombre = "DNI: " + per.getNumDoc() + StringUtil.ESPACIO + StringUtil.GUION + StringUtil.ESPACIO + per.getApePat() + StringUtil.ESPACIO + per.getApeMat() + StringUtil.ESPACIO + per.getNombres();
                }
            } else {
                nombre = per.getTipoDoc().getNombre() + ": ";
                if (per.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")
                        || per.getTipoDoc().getCodProg().equals("TP_DOCID_EXT")) {
                    nombre += per.getRuc() + StringUtil.ESPACIO + StringUtil.GUION + StringUtil.ESPACIO + per.getRznSocial();
                } else {
                    nombre += per.getNumDoc() + StringUtil.ESPACIO + StringUtil.GUION + StringUtil.ESPACIO + per.getApePat() + StringUtil.ESPACIO + (per.getApeMat() == null ? StringUtil.VACIO : per.getApeMat()) + StringUtil.ESPACIO + per.getNombres();
                }
            }
        }

        return nombre;
    }

    public List<AmaInventarioAccesorios> obtenerAccesoriosVer(AmaGuiaTransito amaGuia) {
        List<AmaInventarioAccesorios> listRes = null;
        obtenerInvArma(amaGuia);
        if (invArmaDeposito != null && (!JsfUtil.isNullOrEmpty(invArmaDeposito.getAmaInventarioAccesoriosList()))) {
            listRes = new ArrayList<>();
            for (AmaInventarioAccesorios acces : invArmaDeposito.getAmaInventarioAccesoriosList()) {
                if (acces.getActivo() == JsfUtil.TRUE) {
                    if (acces.getEstadoDeposito() == (JsfUtil.FALSE)) {
                        listRes.add(acces);
                    }
                }
            }
        }
        invArmaDeposito = null;
        return listRes;
    }

    /**
     * ABRIR FORMULARIO DE FOTO
     *
     * @author Richar Fernández
     * @version 1.0
     * @param fotoSelected Map de foto selccionadas.
     */
    public void openDlgFoto(Map fotoSelected) {
        try {
            fotoByte = (byte[]) fotoSelected.get("byte");
            InputStream is = new ByteArrayInputStream(fotoByte);
            setFoto(new DefaultStreamedContent(is, "image/jpeg", StringUtil.VACIO + fotoSelected.get("nombre")));

            RequestContext.getCurrentInstance().execute("PF('wvDialogFoto').show()");
            RequestContext.getCurrentInstance().update("frmFoto");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorCargaFotosArma"));
        }
    }

    public void openCrearPersona(String tipo, Boolean disca, String nroDoc) {
        tipoPersona = tipo;
        tipoDocSelect = null;
        numeroDoc = null;
        cantNumeros = null;
        newPersona = new SbPersona();
        disabledBtVal = Boolean.TRUE;
        setNewPersona(new SbPersona());
        if (!disca) {
            RequestContext.getCurrentInstance().execute("PF('CrearPersonaViewDialog').show()");
            RequestContext.getCurrentInstance().update("formCrearPersona");
        }
    }

    public void openDlgAgregarRep(String tipo) {
        setTipoPersona(tipo);
        setRepresentante(null);
        RequestContext.getCurrentInstance().execute("PF('AgregarRepViewDialog').show()");
        RequestContext.getCurrentInstance().update("formAgregarRep");
    }

    /**
     * BUSCAR COMPROBANTE DE PAGO
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void buscarDlgComprobante() {
        try {
            if (getPersonaReg() != null) {
                if (tipoBusComp != null) {
                    if (!(tipoBusComp != null && ((filtroComp == null || filtroComp.isEmpty()) && fechaCompBus == null))) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        HashMap mMap = new HashMap();
                        mMap.put("tipo", tipoBusComp);
                        mMap.put("filtro", filtroComp);
                        mMap.put("fecha", fechaCompBus);
                        mMap.put("importe", Double.parseDouble(JsfUtil.bundle("ActaDevolucion_recibo_precio")));
                        mMap.put("codigo", Long.parseLong(JsfUtil.bundle("ActaDevolucion_recibo_codigo")));
                        setLstRecibosBusqueda(ejbSbRecibosFacade.listarRecibosPorCriterios(getPersonaReg(), mMap));
                        if (!JsfUtil.isNullOrEmpty(lstRecibosBusqueda)) {
                            setComprobantePago(lstRecibosBusqueda.get(0));
                            tipoBusComp = null;
                            filtroComp = null;
                        } else {
                            setComprobantePago(null);
                            JsfUtil.mensajeAdvertencia("No se encontraron resultados con el número de secuencia ingresado.");
                        }
                    } else {
                        setComprobantePago(null);
                        JsfUtil.mensajeAdvertencia("Por favor escriba el criterio de búsqueda.");
                    }
                } else {
                    setComprobantePago(null);
                    JsfUtil.mensajeAdvertencia("Por favor seleccione un tipo de búsqueda.");
                }
            } else {
                JsfUtil.mensajeAdvertencia("Por favor, ingrese solicitante.");
            }
        } catch (Exception ex) {
            JsfUtil.mensajeAdvertencia("hubo un error al buscar el comprobante de pago.");
        }
    }

    public void agregarDocumentoTD() {
        listDocumentoTDSelect = null;
        if (listDocumentoTD == null) {
            listDocumentoTD = new ArrayList<>();
        }
        if (docTDSelect != null) {
            if (!listDocumentoTD.contains(docTDSelect)) {
                listDocumentoTD.add(docTDSelect);
            } else {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajExisteDocumentoTabla"));
            }
            docTDSelect = null;
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeSeleccioneDoc"));
            return;
        }
    }

    public void removerDocumentosTD() {
        if (!JsfUtil.isNullOrEmpty(listDocumentoTDSelect)) {
            if (listDocumentoTD.containsAll(listDocumentoTDSelect)) {
                listDocumentoTD.removeAll(listDocumentoTDSelect);
            }
            if (listDocumentoTD.isEmpty()) {
                listDocumentoTD = null;
            }
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeSeleccionElementoEliminar"));
        }
    }

    public TipoGamac obtenerPadreTipoGuia(TipoGamac tipoGamac) {
        TipoGamac res = null;
        if (tipoGamac != null) {
            if (tipoGamac.getTipoId() != null) {
                res = tipoGamac.getTipoId();
            }
        }
        return res;
    }

    public void eliminarSolicitante() {
        personaReg = null;
        RequestContext.getCurrentInstance().update("registroForm:datosSolicitante");
        RequestContext.getCurrentInstance().update("editarForm:datosSolicitante");
    }

    public AmaModelos obtenerModelo(Long idModelo) {
        return ejbAmaModelosFacade.find(idModelo);
    }

    public void inicializarListasArmas() {
        if (listaArmasTemp != null) {
            listaArmasTemp.clear();
        } else {
            listaArmasTemp = new ArrayList<>();
        }
        if (listaInvArmasTemp != null) {
            listaInvArmasTemp.clear();
        } else {
            listaInvArmasTemp = new ArrayList<>();
        }
        if (listaArmasFinalTemp != null) {
            listaArmasFinalTemp.clear();
        } else {
            listaArmasFinalTemp = new ArrayList<>();
        }
    }

    public void limpiarDatosDepDev() {
//        personaReg = null;
        listRepresentante = null;
        repreSelect = null;
        obsDevolucion = null;
        newPersona = new SbPersona();
//        listSTGuia = null;
//        listSubSTGuia = null;
        subTipoSelect = null;
        subSubTipoSelect = null;
        invArmaDeposito = null;
        listAccesorioMostrar = new ArrayList<>();
        listMunicionMostrar = new ArrayList<>();
        listAcceSelect = null;
        listMuniSelect = null;
        actaDepositoSelect = null;
        nroExpediente = null;
        docTDSelect = null;
        listDocTD = null;
        listDocumentoTD = null;
        listDocumentoTDSelect = null;
        //Municiones
        listGuiaMunicion = null;
        listGuiaMunicionSelect = null;
        esRectificatoria = Boolean.FALSE;
        listAccesoriosGuia = null;
        RequestContext.getCurrentInstance().update("registroDevForm");
        RequestContext.getCurrentInstance().update("editarDevForm");
    }

    public void obtenerListaMuniciones(List<AmaGuiaMuniciones> listMuniciones, Boolean actualizaCheck) {
        if (listMunicionMostrar == null) {
            listMunicionMostrar = new ArrayList<>();
        }
        if (!JsfUtil.isNullOrEmpty(listMuniciones)) {
            ParametroElementoClass amaMuniMostrar = null;
            for (AmaGuiaMuniciones muni : listMuniciones) {
                amaMuniMostrar = new ParametroElementoClass();
                amaMuniMostrar.setId(muni.getId());
                amaMuniMostrar.setS_valor1(muni.getMunicionId().getCalibrearmaId() == null ? StringUtil.VACIO : muni.getMunicionId().getCalibrearmaId().getNombre());
                amaMuniMostrar.setS_valor2((muni.getMunicionId().getMarcaId() == null ? StringUtil.VACIO : muni.getMunicionId().getMarcaId().getNombre()) + StringUtil.VACIO
                        + (muni.getMunicionId().getDenominacionId() == null ? StringUtil.VACIO : muni.getMunicionId().getDenominacionId().getNombre()) + StringUtil.VACIO
                        + (muni.getMunicionId().getNroPerdigonId() == null ? StringUtil.VACIO : muni.getMunicionId().getNroPerdigonId().getNombre()) + StringUtil.VACIO
                        + (muni.getMunicionId().getPesoMunicionGranos() == null ? StringUtil.VACIO : muni.getMunicionId().getPesoMunicionGranos()));
                amaMuniMostrar.setD_valor1(muni.getCantidad());
                amaMuniMostrar.setS_valor3(muni.getUmedidaId() == null ? StringUtil.VACIO : muni.getUmedidaId().getNombre());
                amaMuniMostrar.setD_valor2(muni.getPeso());
                amaMuniMostrar.setCheck(Boolean.FALSE);
                getListMunicionMostrar().add(amaMuniMostrar);
            }
        }
    }

    public void checkarAccesorio(ParametroElementoClass acces, List<ParametroElementoClass> listAcces, Boolean accion) {
        if (listAcceSelect == null) {
            listAcceSelect = new ArrayList<>();
        }
        if (acces != null) {
            if (acces.getCheck()) {
                if (!listAcceSelect.contains(acces)) {
                    listAcceSelect.add(acces);
                }
            } else if (listAcceSelect.contains(acces)) {
                listAcceSelect.remove(acces);
            }
        } else if (!JsfUtil.isNullOrEmpty(listAcces)) {
            listAcceSelect = new ArrayList<>();
            listAcceSelect.addAll(listAcces);
            for (ParametroElementoClass accTemp : listAcceSelect) {
                accTemp.setCheck(accion);
            }
            checkAllAcces = accion;
        }
    }

    public void checkarMunicion(ParametroElementoClass muni, List<ParametroElementoClass> listMuni, Boolean accion) {
        if (listMuniSelect == null) {
            listMuniSelect = new ArrayList<>();
        }
        if (muni != null) {
            if (muni.getCheck()) {
                if (!listMuniSelect.contains(muni)) {
                    listMuniSelect.add(muni);
                }
            } else if (listMuniSelect.contains(muni)) {
                listMuniSelect.remove(muni);
            }
        } else if (!JsfUtil.isNullOrEmpty(listMuni)) {
            listMuniSelect = new ArrayList<>();
            listMuniSelect.addAll(listMuni);
            for (ParametroElementoClass muniTemp : listMuniSelect) {
                muniTemp.setCheck(accion);
            }
            checkAllMuni = accion;
        }
        List<ParametroElementoClass> listTemp = new ArrayList<>();
        listTemp.addAll(listMuniSelect);
        if (!listTemp.isEmpty()) {
            for (ParametroElementoClass muniTemp : listTemp) {
                if (!muniTemp.getCheck()) {
                    listMuniSelect.remove(muniTemp);
                }
            }
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String guardar() {
        try {
            String msj = StringUtil.VACIO;
            if (true) {
                if (listDocumentoTD == null) {
                    listDocumentoTD = new ArrayList();
                }
                if (renderDocumentoSustento) {
                    if (JsfUtil.isNullOrEmpty(listDocumentoTD)) {
                        JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeListaDocumentosVacia"));
                        return null;
                    }
                } else if (!ejbExpedienteFacade.expedientesAsignadosXIdUsuario(idUsuarioTramDoc == null ? StringUtil.VACIO : idUsuarioTramDoc.toString(), nroExpediente)) {
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeExpedienteNoExiste"));
                    return null;
                }

                //if (ejbExpedienteFacade.expedientesAsignadosXIdUsuario(idUsuarioTramDoc == null ? StringUtil.VACIO : idUsuarioTramDoc.toString(), nroExpediente)) {
                //Si existe una lista de accesorios.
                List<AmaInventarioAccesorios> listAccesoriosSave = null;
                //Fin
                Boolean nuevoRegistro = Boolean.TRUE;
                String cadenaExp = StringUtil.VACIO;
                if (estado == EstadoCrud.EDITARDEV) {
                    nuevoRegistro = Boolean.FALSE;
                    setRegistro(actaDevolucionSelect);
                    registro.setSolicitanteId(personaReg);
                } else {
                    registro = new AmaGuiaTransito();
                    registro.setTipoRegistroId(ejbTipoBaseFacade.tipoPorCodProg("TP_REGIST_NOR"));
                    //Solamente para actas rectificadas.
                    if (esRectificatoria && actaDepositoSelect != null) {
                        setRegistro(actaDepositoSelect);
                    }
                    //Fin
                    // Datos en común a todos los tipos de guía
                    registro.setActivo(JsfUtil.TRUE);
                    registro.setSolicitanteId(personaReg);
                    registro.setNroGuia("0");
                    registro.setEstadoId(ejbTipoBaseFacade.tipoPorCodProg("TP_GTGAMAC_CRE"));
                    registro.setUsuarioId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(loginController.getUsuario().getLogin()));
                    //Se setea y se actualiza el campo devolucion_id del registro de deposito para poder vincularlo al registro de devolucion que se esta creando.
                    registro.setGuiaReferenciadaId(actaDepositoSelect);
                    //
                }
                //Guardado del tipo de guía, según tipo de guía y sus subtipos
                registro.setTipoGuiaId(tipoGuiaSave());
                // Datos en común a todos los tipos de guía
                if ("TP_PER_JUR".equalsIgnoreCase(personaReg.getTipoId().getCodProg())) {
                    registro.setRepresentanteId(repreSelect);
                }
//                    if (devolucion.equals(tipoAlmaFisico)) {
                //Setear datos para la devolución de armas
                registro.setNroGuia("0");//solamente para edicion.
                //Temporal                    
                List<Documento> listDocTemp = new ArrayList(listDocumentoTD);
                //Fin
                registro.setAmaDocumentoList(amaGuiaTransitoGenericoController.listAmaDocumento(listDocTemp, registro, usuLogueado, personaReg));

                if (renderDocumentoSustento) {
                    for (AmaDocumento amaDoc : registro.getAmaDocumentoList()) {
                        //Se setea el número de Guía del documento, éste deocumento tiene origen en la creación del expediente inicial.
                        registro.setNroExpediente(amaDoc.getNroExpediente());
                        break;
                    }
                } else {
                    registro.setNroExpediente(nroExpediente);
                }

//                    for (AmaDocumento amaDoc : registro.getAmaDocumentoList()) {
//                        //Se setea el número de Guía del documento, éste deocumento tiene origen en la creación del expediente inicial.
//                        registro.setNroExpediente(amaDoc.getNroExpediente());
//                        break;
//                    }
                registro.setObservacion(obsDevolucion);
                AmaInventarioArma invArma = inventarioArmaSave();
                //############## Variable que valida que en la devolución se incluyan accesorios o municiones. ############
                if (invArma == null && (JsfUtil.isNullOrEmpty(listMuniSelect))) {
                    Boolean validaCheck = Boolean.FALSE;
                    if (!JsfUtil.isNullOrEmpty(listAccesorioMostrar)) {
                        for (ParametroElementoClass acc : listAccesorioMostrar) {
                            if (acc.getCheck()) {
                                validaCheck = Boolean.TRUE;
                                break;
                            }
                        }
                    }
                    if (!validaCheck) {
                        JsfUtil.mensajeError("Debe seleccionar al menos una munición o un accesorio.");
                        return null;
                    }
                }
                //############################################### Fin #####################################################
                if (invArma != null) {
                    registro.setAmaInventarioArmaList(new ArrayList<AmaInventarioArma>());
                    registro.getAmaInventarioArmaList().add(invArma);
                }
                if (!nuevoRegistro) {
                    actualizarListaMunicionesDev(listMuniSelect);
                    if (!JsfUtil.isNullOrEmpty(registro.getAmaInventarioAccesoriosList())) {
                        actualizarListaAccesoriosDev(listAccesorioMostrar);
                    }
                }
                setTipoOrigenSelect(actaDepositoSelect.getTipoOrigen());
                selectedOriSucamec = actaDepositoSelect.getDireccionDestinoId();
                //Temporal, en la devolución no se registra la direccion de destino.
//                    }

                registro.setTipoOrigen(tipoOrigenSelect);
                registro.setDireccionOrigenId(selectedOriSucamec);
                registro = (AmaGuiaTransito) JsfUtil.entidadMayusculas(registro, StringUtil.VACIO);
                if (!nuevoRegistro) {
                    ejbAmaGuiaTransitoFacade.edit(registro);
                    msj = JsfUtil.bundle("MensajeConfirmacionActualizacionActaDev");
                } else {
                    ejbAmaGuiaTransitoFacade.create(registro);
                    //Se setea y se actualiza el campo devolucion_id del registro de deposito para poder vincularlo al registro de devolucion que se esta creando.
                    registro.getGuiaReferenciadaId().setDevolucionId(registro);
                    ejbAmaGuiaTransitoFacade.edit(registro.getGuiaReferenciadaId());
                    //
                    msj = JsfUtil.bundle("MensajeConfirmacionCreacionActaDev");
                }
                //Tipo de artículo accesorios
                if (!JsfUtil.isNullOrEmpty(listAccesoriosSave)) {
                    for (AmaInventarioAccesorios acces : listAccesoriosSave) {
                        acces.setGuiaTransitoId(registro);
                        ejbAmaInventarioAccesoriosFacade.edit(acces);
                    }
                }
                //Actualizar campo expedient de comprobante,
                if (comprobantePago != null) {
                    SbReciboRegistro recReg = new SbReciboRegistro();
                    Boolean agregar = Boolean.TRUE;
                    List<SbReciboRegistro> listReg = ejbSbReciboRegistroFacade.obtenerRecibosPorExpediente("PENDIENTE_" + registro.getId());
                    if (listReg != null) {
                        for (SbReciboRegistro reg : listReg) {
                            if (Objects.equals(comprobantePago.getId(), reg.getReciboId().getId())) {
                                agregar = Boolean.FALSE;
                                break;
                            } else {
                                reg.setActivo(JsfUtil.FALSE);
                                ejbSbReciboRegistroFacade.edit(reg);
                            }
                        }
                    }
                    if (agregar) {
                        if (!StringUtil.VACIO.equals(cadenaExp)) {
                            recReg.setNroExpediente(cadenaExp + "_" + registro.getId());
                        } else {
                            recReg.setNroExpediente(registro.getNroExpediente());
                        }
                        recReg.setActivo(JsfUtil.TRUE);
                        recReg.setReciboId(comprobantePago);
                        comprobantePago.setSbReciboRegistroList(new ArrayList<SbReciboRegistro>());
                        comprobantePago.getSbReciboRegistroList().add(recReg);
                        ejbSbRecibosFacade.edit(comprobantePago);
                    }
                } else {
                    List<SbReciboRegistro> listReg = ejbSbReciboRegistroFacade.obtenerRecibosPorExpediente("PENDIENTE_" + registro.getId());
                    if (listReg != null) {
                        for (SbReciboRegistro reg : listReg) {
                            reg.setActivo(JsfUtil.FALSE);
                            ejbSbReciboRegistroFacade.edit(reg);
                        }
                    }
                }
                //Se crean registros de municiones si se añadio una lista de municiones.
                if (nuevoRegistro) {
                    if (!JsfUtil.isNullOrEmpty(listMuniSelect)) {
                        registro.setAmaGuiaMunicionesList(new ArrayList<AmaGuiaMuniciones>());
                        AmaGuiaMuniciones muniNew = null;
                        for (ParametroElementoClass elem : listMuniSelect) {
                            muniNew = new AmaGuiaMuniciones();
                            AmaGuiaMuniciones muni = ejbAmaGuiaMunicionesFacade.find(elem.getId());
                            muniNew.setActivo(JsfUtil.TRUE);
                            muniNew.setCantidad(muni.getCantidad());
                            muniNew.setGuiaTransitoId(registro);
                            muniNew.setMunicionId(muni.getMunicionId());
                            muniNew.setPeso(muni.getPeso());
                            muniNew.setUmedidaId(muni.getUmedidaId());
                            registro.getAmaGuiaMunicionesList().add(muniNew);
                        }
                    }
                    registro.setAmaInventarioAccesoriosList(copiarDatosAccesorios(accesoriosActaSave(listAccesoriosGuia), null, registro));
                    JsfUtil.borrarIdsN(registro.getAmaInventarioAccesoriosList());
                    ejbAmaGuiaTransitoFacade.edit(registro);
                    //Si el tipo de Guía es Importación de municiones o De Salida definitiva, entonces se actualiza la lista de municiones
                }
                regresar();
                JsfUtil.mensaje(msj);
                List<Map> listGTTemporal = ejbAmaGuiaTransitoFacade.obtenerListGuiaCreada(registro.getId());
                listGuiasTraslado = new ListDataModel(listGTTemporal);
//                } else {
//                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeExpedienteNoExiste"));
////                    if ("TP_GTGAMAC_AGE".equals(idTipoGuia.getCodProg())
////                            || "TP_GTGAMAC_SAL".equals(idTipoGuia.getCodProg())
////                            || "TP_GTGAMAC_SUC".equals(idTipoGuia.getCodProg())
////                            || "TP_GTGAMAC_DDV".equals(idTipoGuia.getCodProg())) {
////                        JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeListaArmasMuniVacia"));
////                    } else {
////                        JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeListaArmasVacia"));
////                    }
//                    return null;
//                }
                amaDepositoArmasController.setListGuiasTraslado(listGuiasTraslado);
                return amaDepositoArmasController.prepareBandejaDepDev();
            } else {
                JsfUtil.mensajeError(JsfUtil.bundle("MensajeDireccion_Requerido"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void regresar() {
        estado = EstadoCrud.BUSCARGUIA;
    }

    public List<AmaInventarioAccesorios> copiarDatosAccesorios(List<AmaInventarioAccesorios> listAcces, AmaInventarioArma invArma, AmaGuiaTransito amaGuia) {
        List<AmaInventarioAccesorios> listRes = new ArrayList<>();
        AmaInventarioAccesorios accesAdd = null;
        if (listAcces != null) {
            for (AmaInventarioAccesorios acces : listAcces) {
                accesAdd = new AmaInventarioAccesorios();
                accesAdd.setId(JsfUtil.tempIdN());
                accesAdd.setActivo(acces.getActivo());
                accesAdd.setEstadoDeposito(acces.getEstadoDeposito());
                accesAdd.setArticuloConexoId(acces.getArticuloConexoId());
                accesAdd.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                accesAdd.setAudNumIp(JsfUtil.getIpAddress());
                accesAdd.setCantidad(acces.getCantidad());
                accesAdd.setInventarioArmaId(invArma);
                accesAdd.setGuiaTransitoId(amaGuia);
                listRes.add(accesAdd);
            }
        }
        return listRes;
    }

    public List<AmaInventarioAccesorios> accesoriosActaSave(List<AmaInventarioAccesorios> listAccesGuia) {
        if (!JsfUtil.isNullOrEmpty(listAccesGuia)) {
            List<AmaInventarioAccesorios> listTemp = new ArrayList(listAccesGuia);
            for (AmaInventarioAccesorios accesDep : listTemp) {
                for (ParametroElementoClass acces : listAccesorioMostrar) {
                    if (accesDep.getId().equals(acces.getId())) {
                        if (!acces.getCheck()) {
                            listAccesGuia.remove(accesDep);
                        } else {
                            accesDep.setEstadoDeposito(JsfUtil.FALSE);
                        }
//                        else {
//                            accesDep.setEstadoDeposito(JsfUtil.TRUE);
//                        }
                        break;
                    }
                }
            }
        }
        return listAccesGuia;
    }

    public AmaInventarioArma inventarioArmaSave() {
        if (invArmaDeposito != null && invArmaDeposito.getAmaInventarioAccesoriosList() != null) {
            for (AmaInventarioAccesorios accesDep : invArmaDeposito.getAmaInventarioAccesoriosList()) {
                for (ParametroElementoClass acces : listAccesorioMostrar) {
                    if (accesDep.getId().equals(acces.getId())) {
                        if (acces.getCheck()) {
                            accesDep.setEstadoDeposito(JsfUtil.FALSE);
                        } else {
                            accesDep.setEstadoDeposito(JsfUtil.TRUE);
                        }
                        break;
                    }
                }
            }
            ejbAmaInventarioArmaFacade.edit(invArmaDeposito);
        }
        return invArmaDeposito;
    }

    public void actualizarListaMunicionesDev(List<ParametroElementoClass> listaMuniSelect) {
        if (listaMuniSelect != null) {
            List<AmaGuiaMuniciones> listMuniActivos = new ArrayList<>();
            if (registro.getAmaGuiaMunicionesList() == null) {
                registro.setAmaGuiaMunicionesList(new ArrayList<AmaGuiaMuniciones>());
            } else {
                listMuniActivos = obtenerListMuniActivos(registro);
            }
            //Bloque para Inactivar las municiones que no estan checadas
            if (listMuniActivos != null) {
                for (AmaGuiaMuniciones muniBD : listMuniActivos) {
                    Boolean inactivar = Boolean.TRUE;
                    //if (!listaMuniSelect.isEmpty()) {
                    for (ParametroElementoClass muniPE : listaMuniSelect) {
                        if (muniPE.getL_valor1().equals(muniBD.getMunicionId().getId())) {
                            inactivar = Boolean.FALSE;
                            break;
                        }
                    }
                    //}
                    if (inactivar) {
                        muniBD.setActivo(JsfUtil.FALSE);
                        ejbAmaGuiaMunicionesFacade.edit(muniBD);
                    }
                }
            }
            //Fin del Bloque
            //Bloque para añadir un registro si no existe en la lista original
            for (ParametroElementoClass elem : listaMuniSelect) {
                AmaGuiaMuniciones muni = ejbAmaGuiaMunicionesFacade.find(elem.getId());
                Boolean anadir = Boolean.TRUE;
                if (listMuniActivos != null) {
                    for (AmaGuiaMuniciones muniTemp : listMuniActivos) {
                        if (muniTemp.getMunicionId().equals(muni.getMunicionId())) {
                            anadir = Boolean.FALSE;
                            break;
                        }
                    }
                }
                AmaGuiaMuniciones muniNew = null;
                if (anadir) {
                    muniNew = new AmaGuiaMuniciones();
                    muniNew.setActivo(JsfUtil.TRUE);
                    muniNew.setCantidad(muni.getCantidad());
                    muniNew.setGuiaTransitoId(registro);
                    muniNew.setMunicionId(muni.getMunicionId());
                    muniNew.setPeso(muni.getPeso());
                    muniNew.setUmedidaId(muni.getUmedidaId());
                    registro.getAmaGuiaMunicionesList().add(muniNew);
                }
            }
            //Fin del Bloque
        }
    }

    public void actualizarListaAccesoriosDev(List<ParametroElementoClass> listAccesActa) {
        if (listAccesActa != null && listAccesActa.size() > 0) {
            List<AmaInventarioAccesorios> listAccesActivos = new ArrayList<>();
            if (registro.getAmaInventarioAccesoriosList() == null) {
                registro.setAmaInventarioAccesoriosList(new ArrayList<AmaInventarioAccesorios>());
            } else {
                listAccesActivos = amaGuiaTransitoGenericoController.obtenerListAccesActaActivos(registro);
            }
            //Bloque para Inactivar las municiones que no estan checadas
            for (AmaInventarioAccesorios accesiBD : listAccesActivos) {
                Boolean inactivar = Boolean.TRUE;
                //if (!listaMuniSelect.isEmpty()) {
                for (ParametroElementoClass accesPE : listAccesActa) {
                    if (accesPE.getCheck()) {
                        if (Objects.equals(accesPE.getS_valor1(), accesiBD.getArticuloConexoId().getNombre())) {
                            inactivar = Boolean.FALSE;
                            break;
                        }
                    }
                }
                //}
                if (inactivar) {
                    accesiBD.setActivo(JsfUtil.FALSE);
                    ejbAmaInventarioAccesoriosFacade.edit(accesiBD);
                }
            }
            //Fin del Bloque
            //Bloque para añadir un registro si no existe en la lista original
            for (ParametroElementoClass elem : listAccesActa) {
                if (elem.getCheck()) {
                    AmaInventarioAccesorios acces = ejbAmaInventarioAccesoriosFacade.find(elem.getId());
                    Boolean anadir = Boolean.TRUE;
                    for (AmaInventarioAccesorios accesTemp : listAccesActivos) {
                        if (Objects.equals(accesTemp.getArticuloConexoId(), acces.getArticuloConexoId())) {
                            anadir = Boolean.FALSE;
                            break;
                        }
                    }
                    AmaInventarioAccesorios accesNew = null;
                    if (anadir) {
                        accesNew = new AmaInventarioAccesorios();
                        accesNew.setActivo(JsfUtil.TRUE);
                        accesNew.setEstadoDeposito(JsfUtil.FALSE);
                        accesNew.setArticuloConexoId(acces.getArticuloConexoId());
                        accesNew.setCantidad(acces.getCantidad());
                        accesNew.setInventarioArmaId(acces.getInventarioArmaId());
                        accesNew.setGuiaTransitoId(registro);
                        registro.getAmaInventarioAccesoriosList().add(accesNew);
                    }
                }
            }
            //Fin del Bloque
        }
    }

    /**
     * EVENTO PARA ABRIR FORMULARIO DE CONFIRMACION AL REGISTRAR ENTREGA DE
     * ARMAS
     *
     * @author Gino Chávez
     * @version 2.0
     * @param item
     * @param idGuia
     */
    public void openDlgConfEmisionGTComercio(Map item, Long idGuia) {
//        if (item != null) {
//            numExpGuia = (String) item.get("NROEXP");
//            setAmaGuiaSelect(ejbAmaGuiaTransitoFacade.obtenerGuiaXNroExp(numExpGuia));
//        } else 
        if (idGuia != null) {
            setAmaGuiaSelect(ejbAmaGuiaTransitoFacade.find(idGuia));
        }
        inicializarListasArmas();
        llenarListasArmasMap(getAmaGuiaSelect().getAmaArmaList(), getAmaGuiaSelect().getAmaInventarioArmaList());
        String nombreCompleto = StringUtil.VACIO;
        setListInvAcceMostrar(obtenerAccesoriosVer(getAmaGuiaSelect()));
        obtenerListMuniActivos(getAmaGuiaSelect());
        obtenerInvArma(getAmaGuiaSelect());
        nombreCompleto = (obtenerNombrePersona(getAmaGuiaSelect().getSolicitanteId()));
        AmaCatalogo tipoDep = obtenerTipoDeposito(getAmaGuiaSelect());
        setMsjConfirmacionDevolucion("¿Confirma que el día de hoy " + ReportUtilTurno.mostrarFechaString(new Date()) + " se procede a la devolución " + ("TP_ART_MUNI".equals(tipoDep.getCodProg()) ? "de las municiones" : ("TP_ART_REPU".equals(tipoDep.getCodProg()) ? "de los artículos conexos" : ("TP_ART_ACCE".equals(tipoDep.getCodProg()) ? StringUtil.VACIO : "del arma"))) + ", a solicitud de "
                + nombreCompleto + ", y cuyas características y accesorios del arma se detallan a continuación?");
        RequestContext.getCurrentInstance().execute("PF('ConfDepositoViewDialog').show()");
        RequestContext.getCurrentInstance().update("formConfDepDev");
//        }
    }

    public void llenarListasArmasMap(List<AmaArma> listAmaArma, List<AmaInventarioArma> listInvArmas) {
        Map<String, String> item = null;
        if (!JsfUtil.isNullOrEmpty(listAmaArma)) {
            for (AmaArma amaArma : listAmaArma) {
                if (amaArma.getActivo() == JsfUtil.TRUE) {
                    item = new HashMap<String, String>();
                    item.put("id", String.valueOf(amaArma.getId()));//indice 0
                    item.put("nroRua", amaArma.getNroRua());//indice 1
                    item.put("tipoArma", amaArma.getModeloId().getTipoArmaId().getNombre());//indice 2
                    item.put("marca", amaArma.getModeloId().getMarcaId().getNombre());//indice 3
                    item.put("modelo", amaArma.getModeloId().getModelo());//indice 4
                    item.put("calibre", amaGuiaTransitoGenericoController.obtenerCalibres(amaArma.getModeloId()));//indice 5
                    item.put("serie", amaArma.getSerie());//indice 6
                    item.put("codigo", StringUtil.NULO);//indice 7
                    item.put("propietario", StringUtil.NULO);//indice 8
                    item.put("idModelo", String.valueOf(amaArma.getModeloId().getId()));//indice 9
                    item.put("estadoSerie", StringUtil.NULO);//indice 10
                    item.put("estadoFun", amaArma.getEstadoId() == null ? StringUtil.VACIO : amaArma.getEstadoId().getNombre());//indice 11
                    item.put("estadoConsv", StringUtil.NULO);//indice 12
                    item.put("matCacha", StringUtil.NULO);//indice 13
                    item.put("estadoCacha", StringUtil.NULO);//indice 14
                    item.put("mecanismo", StringUtil.NULO);//indice 15
                    item.put("novCanon", StringUtil.NULO);//indice 16
                    item.put("modTiro", StringUtil.NULO);//indice 17
                    item.put("exterior", StringUtil.NULO);//indice 18
                    item.put("peso", amaArma.getPesoInicial() == null ? StringUtil.VACIO : String.valueOf(amaArma.getPesoInicial()));//indice 19
                    item.put("existeIArma", String.valueOf(0));//indice 20 --> Indicador de que es un arma registrado en la tabla Inventario arma. 1:Existe en inventario arma, 0:No existe en Inventario arma
                    item.put("almSucamecId", StringUtil.VACIO);//indice 21
                    item.put("conTarjeta", "1");//indice 22
                    if (!listaArmasFinalTemp.contains(item)) {
                        listaArmasFinalTemp.add(item);
                    }
                    if (!listaArmasTemp.contains(amaArma)) {
                        listaArmasTemp.add(amaArma);
                    }
                }
            }
        }
        if (!JsfUtil.isNullOrEmpty(listInvArmas)) {
            for (AmaInventarioArma amaInvArma : listInvArmas) {
                if (amaInvArma.getActivo() == JsfUtil.TRUE && amaInvArma.getActual() == JsfUtil.TRUE) {
                    item = new HashMap<String, String>();
                    item.put("id", String.valueOf(amaInvArma.getId()));//indice 0
                    item.put("nroRua", amaInvArma.getNroRua());//indice 1
                    item.put("tipoArma", amaInvArma.getModeloId().getTipoArmaId().getNombre());//indice 2
                    item.put("marca", amaInvArma.getModeloId().getMarcaId().getNombre());//indice 3
                    item.put("modelo", amaInvArma.getModeloId().getModelo());//indice 4
                    item.put("calibre", amaGuiaTransitoGenericoController.obtenerCalibres(amaInvArma.getModeloId()));//indice 5
                    item.put("serie", amaInvArma.getSerie());//indice 6
                    item.put("codigo", String.valueOf(amaInvArma.getCodigo()));//indice 7
                    item.put("propietario", amaInvArma.getPropietarioId() == null ? StringUtil.VACIO : obtenerDescPersona(amaInvArma.getPropietarioId()));//indice 8
                    item.put("idModelo", String.valueOf(amaInvArma.getModeloId().getId()));//indice 9
                    item.put("estadoSerie", amaInvArma.getEstadoserieId() == null ? StringUtil.VACIO : amaInvArma.getEstadoserieId().getNombre());//indice 10
                    item.put("estadoFun", amaInvArma.getEstadofuncionalId() == null ? StringUtil.VACIO : amaInvArma.getEstadofuncionalId().getNombre());//indice 11
                    item.put("estadoConsv", amaInvArma.getEstadoconservacionId() == null ? StringUtil.VACIO : amaInvArma.getEstadoconservacionId().getNombre());//indice 12
                    item.put("matCacha", amaInvArma.getMaterialCachaId() == null ? StringUtil.VACIO : amaInvArma.getMaterialCachaId().getNombre());//indice 13
                    item.put("estadoCacha", amaInvArma.getEstadoCachaId() == null ? StringUtil.VACIO : amaInvArma.getEstadoCachaId().getNombre());//indice 14
                    item.put("mecanismo", amaInvArma.getMecanismoId() == null ? StringUtil.VACIO : amaInvArma.getMecanismoId().getNombre());//indice 15
                    item.put("novCanon", amaInvArma.getNovedadCanonId() == null ? StringUtil.VACIO : amaInvArma.getNovedadCanonId().getNombre());//indice 16
                    item.put("modTiro", amaInvArma.getModalidadTiroId() == null ? StringUtil.VACIO : amaInvArma.getModalidadTiroId().getNombre());//indice 17
                    item.put("exterior", amaInvArma.getExteriorId() == null ? StringUtil.VACIO : amaInvArma.getExteriorId().getNombre());//indice 18
                    item.put("peso", amaInvArma.getPeso() == null ? StringUtil.VACIO : String.valueOf(amaInvArma.getPeso()));//indice 19
                    item.put("existeIArma", String.valueOf(1));//indice 20 --> Indicador de que es un arma registrado en la tabla Inventario arma. 1:Existe en inventario arma, 0:No existe en Inventario arma
                    item.put("almSucamecId", amaInvArma.getAlmacenSucamecId() == null ? StringUtil.VACIO : String.valueOf(amaInvArma.getAlmacenSucamecId().getId()));//indice 21
                    item.put("conTarjeta", (amaInvArma.getNroRua() != null && !StringUtil.VACIO.equals(amaInvArma.getNroRua().trim())) ? "1" : "0");//indice 22
                    List<Map> listTemp = new ArrayList<>();
                    listTemp.addAll(listaArmasFinalTemp);
                    Boolean anadir = Boolean.TRUE;
                    if (!listTemp.isEmpty()) {
                        for (Map arma : listTemp) {
                            if (Objects.equals(arma.get("modeloId"), item.get("modeloId"))
                                    && Objects.equals(arma.get("serie"), item.get("serie"))) {
                                anadir = Boolean.FALSE;
                                break;
                            }
                        }
                    }
                    if (anadir) {
                        listaArmasFinalTemp.add(item);
                    }
                    if (!listaInvArmasTemp.contains(amaInvArma)) {
                        listaInvArmasTemp.add(amaInvArma);
                    }
                }
            }
        }
        setListaArmasFinal((ListDataModel<Map>) new ListDataModel(listaArmasFinalTemp));
    }

//    public List<AmaInventarioAccesorios> obtenerListAccesActaActivos(AmaGuiaTransito amaGuia) {
//        List<AmaInventarioAccesorios> listAccesActa = null;
//        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioAccesoriosList())) {
//            listAccesActa = new ArrayList<>();
//            for (AmaInventarioAccesorios accesTemp : amaGuia.getAmaInventarioAccesoriosList()) {
//                if (accesTemp.getActivo() == JsfUtil.TRUE) {
//                    listAccesActa.add(accesTemp);
//                }
//            }
//        }
//        return listAccesActa;
//    }
    public TipoGamac tipoGuiaSave() {
        TipoGamac res = null;
        if (tieneSubTipo(idTipoGuia.getCodProg())) {
            if (subTipoSelect != null) {
                if (tieneSubTipo(subTipoSelect.getCodProg())) {
                    if (getSubSubTipoSelect() != null) {
                        res = getSubSubTipoSelect();
                    } else {
                        JsfUtil.mensajeError(JsfUtil.bundle("MensajeSeleccionTipoSubTipo"));
                        return null;
                    }
                } else {
                    res = subTipoSelect;
                }
            }
        } else {
            res = idTipoGuia;
        }
        return res;
    }

    public Boolean tieneSubTipo(String codProg) {
        Boolean res = Boolean.FALSE;
        if ("TP_GTGAMAC_ALM".equalsIgnoreCase(codProg)
                || "TP_GTGAMAC_DEV".equalsIgnoreCase(codProg)
                || "TP_GTGAMAC_SUC".equalsIgnoreCase(codProg)
                || "TP_GTGAMAC_DON".equalsIgnoreCase(codProg)
                || "TP_GTALM_DISADU".equalsIgnoreCase(codProg)) {
            res = Boolean.TRUE;
        }
        return res;
    }

    public String fechaSimple(Date fecha) {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    public void registroUnitarioTrazas() {
        Boolean cambiaEstado = Boolean.TRUE;
        StringBuilder listNroExp = new StringBuilder();
        String userDestino = StringUtil.VACIO;
        if (amaGuiaSelect != null) {
            setEstadoFinal(ejbTipoBaseFacade.tipoPorCodProg("TP_GTGAMAC_FIN"));
            amaGuiaSelect.setEstadoId(getEstadoFinal());
            //Destino, según iteración y formulario origen, si es deposito o devolución de armas (actas) ó Guías de Tránsito.
            userDestino = JsfUtil.getLoggedUser().getLogin();
            //Cambio de estado y registro de la respectiva traza.
            cambiaEstado = registraTrazaExpediente(amaGuiaSelect, userDestino);
            //Edición de valores de Guía o Acta de acuerdo a los cambios realizados.
            if (cambiaEstado) {
                ejbAmaGuiaTransitoFacade.edit(amaGuiaSelect);
                listNroExp.append("\n").append(amaGuiaSelect.getNroExpediente()).append("\n");
            } else {
                return;
            }
            //Mensaje según formulario origen.
            JsfUtil.mensaje(JsfUtil.bundle("MensajeExitoActActaDev") + StringUtil.ESPACIO + listNroExp);
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeArchivarExpedienteDev"));
            buscarActas();
            List<Map> listGt = ejbAmaGuiaTransitoFacade.obtenerListGuiaCreada(amaGuiaSelect.getId());
            listGuiasTraslado = new ListDataModel(listGt);
            RequestContext.getCurrentInstance().update("listaActasArmaForm:listaActas");
            RequestContext.getCurrentInstance().execute("PF('ConfDepositoViewDialog').hide()");
        }
    }

    /**
     * REGISTRAR TRAZA DE EXPEDIENTE
     *
     * @author Gino Chávez
     * @version 2.0
     * @param amaGuia Registro a crear traza
     * @param userDestino
     * @return Flag de registro de traza correctamente
     */
    public boolean registraTrazaExpediente(AmaGuiaTransito amaGuia, String userDestino) {
        Boolean estadoTraza = Boolean.FALSE;
        try {
            List<Integer> acciones = new ArrayList();
            String observacion = StringUtil.VACIO;
            boolean adjuntar = false;
            if (amaGuia.getEstadoId().getCodProg() != null && !StringUtil.VACIO.equalsIgnoreCase(amaGuia.getEstadoId().getCodProg().trim())) {
                switch (amaGuia.getEstadoId().getCodProg()) {
                    case "TP_GTGAMAC_APR":
                        acciones.add(19);   // Aprobado
                        observacion = "Se dio evaluación al expediente.";
                        break;
                    case "TP_GTGAMAC_DEN":
                        userDestino = JsfUtil.getLoggedUser().getLogin();
                        acciones.add(26);   // Denegado
                        observacion = "Se dio evaluación al expediente.";
                        break;
                    case "TP_GTGAMAC_IMP":
                        userDestino = JsfUtil.getLoggedUser().getLogin();
                        acciones.add(28);   // Improcedente
                        observacion = "Se dio evaluación al expediente.";
                        break;
                    case "TP_GTGAMAC_OBS":
                        acciones.add(18);   // Observado
                        observacion = "Se dio evaluación al expediente.";
                        break;
                    case "TP_GTGAMAC_VIS":
                        acciones.add(20);   // A procesar
                        observacion = "Se verificó.";
                        break;
                    case "TP_GTGAMAC_FIR":
                        acciones.add(21);   // Procesado
                        observacion = "Se procesó la Guía de Tránsito.";
                        break;
                    case "TP_GTGAMAC_FIN":
                        // Archivar    
                        //La accion es diferente en los casos de Guias de Tránsito y en Actas de depósito y Devolución.
                        amaGuia.setFechaEmision(new Date());
                        amaGuia.setFechaSalida(new Date());
                        acciones.add(21);
                        observacion = "Se procede a la entrega del Acta de Devolución con fecha " + ReportUtilTurno.formatoFechaDdMmYyyy(amaGuia.getFechaEmision()) + StringUtil.VACIO;
                        break;
                    default:
                        break;
                }
                Boolean expDerivado = Boolean.TRUE;
                if (!amaGuia.getEstadoId().getCodProg().equals("TP_GTGAMAC_FIN")) {
                    expDerivado = estadoTraza = wsTramDocController.asignarExpediente(amaGuia.getNroExpediente(), JsfUtil.getLoggedUser().getLogin(), userDestino, observacion, StringUtil.VACIO, acciones, adjuntar, null);
                } else {
                    expDerivado = estadoTraza = wsTramDocController.asignarExpediente(amaGuia.getNroExpediente(), JsfUtil.getLoggedUser().getLogin(), userDestino, observacion, StringUtil.VACIO, acciones, adjuntar, null);
                    if (!estadoTraza) {
                        JsfUtil.mensajeError(JsfUtil.bundle("MensajeImposibleArchivarExp") + StringUtil.ESPACIO + amaGuia.getNroExpediente());
                        return estadoTraza;
                    }
                }
                if (!expDerivado) {
                    JsfUtil.mensajeError(JsfUtil.bundle("MensajeImposibleGenerarTraza") + StringUtil.ESPACIO + amaGuia.getNroExpediente());
                    return expDerivado;
                }
            }
            if (estadoTraza && "TP_GTGAMAC_FIN".equals(amaGuia.getEstadoId().getCodProg())) {
                actualizarDatosGuia(amaGuia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return estadoTraza;
    }

    public void actualizarDatosGuia(AmaGuiaTransito amaGuia) {
        if (amaGuia != null) {
            Date fechaVenc = null;
            amaGuia.setFechaVencimiento(fechaVenc);
            //Actualiza la situacion del armas si el tipo de Guía es diferenete a Importación de Municiones.
            //Además se valida que si el destino y origen es algún Almacén de SUCAMEC, entonces no se actualice la situación del arma.
            if (!"TP_GTGAMAC_MUN".equalsIgnoreCase(amaGuia.getTipoGuiaId().getCodProg())
                    && !("TP_GTORDES_ALMS".equalsIgnoreCase(amaGuia.getTipoOrigen() == null ? StringUtil.VACIO : amaGuia.getTipoOrigen().getCodProg())
                    && "TP_GTORDES_ALMS".equalsIgnoreCase(amaGuia.getTipoDestino() == null ? StringUtil.VACIO : amaGuia.getTipoDestino().getCodProg()))) {
                actualizarSituacionArma(amaGuia, Boolean.FALSE);
            }
            actualizarCodigoArmas(amaGuia.getAmaInventarioArmaList());
            //Seteo de número de acta
            String numeroActa = StringUtil.VACIO;
            //Actualizacion del estado de deposito del arma y accesorios.
            numeroActa = amaGuiaTransitoGenericoController.numeroActa("TP_NUM_DEV_AR", amaGuia);
            actualizarDatosAlmacen(amaGuia.getAmaInventarioArmaList(), null, Boolean.FALSE);
            //Número de Guía
            amaGuia.setNroGuia(numeroActa);
            //Hash
            amaGuia.setHashQr(JsfUtil.crearHash(amaGuia.getId() + amaGuia.getNroGuia() + "ley 30299"));
        } else {
            JsfUtil.mensajeError("Error: La Guía de Tránsito no existe.");
        }
    }

    /**
     *
     * @param amaGuia
     * @param isIngresoAlm Es el indicador referido al ingreso a almacen de las
     * armas segun Guía de Tránsito
     */
    //public void actualizarSituacionArma(AmaGuiaTransito amaGuia, Boolean isIngresoAlm) {
    public void actualizarSituacionArma(AmaGuiaTransito amaGuia, Boolean isIngresoAlm) {
        AmaArmaInventarioDif amaInvDif = null;
        TipoGamac tipoGamac = null;
        String codProg = StringUtil.VACIO;
        if (isIngresoAlm) {
            codProg = "TP_SITU_INT";
        } else {
            codProg = obtenerSituacionSegunTG(amaGuia.getTipoGuiaId());
        }
        if (!codProg.equals(StringUtil.VACIO)) {
            tipoGamac = ejbTipoGamacFacade.buscarTipoGamacXCodProg(codProg);
            if (tipoGamac != null && tipoGamac.getId() != null) {
                if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaArmaList())) {
                    for (AmaArma amaArma : amaGuia.getAmaArmaList()) {
                        amaArma.setSituacionId(tipoGamac);
                        ejbAmaArmaFacade.edit(amaArma);
                        amaInvDif = ejbAmaArmaInventarioDifFacade.obtenerPorIdArma(amaArma.getId());
                        if (amaInvDif != null && amaInvDif.getId() != null) {
                            amaInvDif.setSituacionId(tipoGamac);
                            amaInvDif.setActivo(JsfUtil.FALSE);
                            ejbAmaArmaInventarioDifFacade.edit(amaInvDif);
                        }
                        amaInvDif = new AmaArmaInventarioDif();
                        amaInvDif.setActivo(JsfUtil.TRUE);
                        amaInvDif.setFecha(new Date());
                        amaInvDif.setArmaId(amaArma);
                        amaInvDif.setInventarioArmaId(null);
                        amaInvDif.setSituacionId(tipoGamac);
                        ejbAmaArmaInventarioDifFacade.create(amaInvDif);
                    }
                }
                if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList())) {
                    for (AmaInventarioArma amaInvArma : amaGuia.getAmaInventarioArmaList()) {
                        //Actualización de la situación del arma en la tabla ama_arma
//                        AmaArma arma = ejbAmaArmaFacade.obtenerArmaPorModeloSerie(amaInvArma.getModeloId().getId(), amaInvArma.getSerie(), amaInvArma.getNroRua());
                        if (amaInvArma.getArmaId() != null) {
                            amaInvArma.getArmaId().setSituacionId(tipoGamac);
                            ejbAmaArmaFacade.edit(amaInvArma.getArmaId());
                        }
                        //Fin
                        amaInvArma.setSituacionId(tipoGamac);
                        //Si el metodo es invocado desde el modulo de Ingreso a Almacén, según tipo de Guía, entonces se actualiza el ambiente y anaquel
                        ejbAmaInventarioArmaFacade.edit(amaInvArma);
                        amaInvDif = ejbAmaArmaInventarioDifFacade.obtenerPorIdInventarioArma(amaInvArma.getId());
                        if (amaInvDif != null && amaInvDif.getId() != null) {
                            amaInvDif.setSituacionId(tipoGamac);
                            amaInvDif.setActivo(JsfUtil.FALSE);
                            ejbAmaArmaInventarioDifFacade.edit(amaInvDif);
                        }
                        amaInvDif = new AmaArmaInventarioDif();
                        amaInvDif.setActivo(JsfUtil.TRUE);
                        amaInvDif.setFecha(new Date());
                        amaInvDif.setArmaId(amaInvArma.getArmaId());
                        amaInvDif.setInventarioArmaId(amaInvArma);
                        amaInvDif.setSituacionId(tipoGamac);
                        ejbAmaArmaInventarioDifFacade.create(amaInvDif);
                    }
                }
            }
        }
    }

    public String obtenerSituacionSegunTG(TipoGamac tipoGuia) {
        String codProg = StringUtil.VACIO;
        switch (tipoGuia.getCodProg()) {
            case "TP_GTGAMAC_REC":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_EXH":
                codProg = "TP_SITU_EXH";
                break;
            case "TP_GTGAMAC_POL":
                codProg = "TP_SITU_EGT";
                break;
            case "TP_GTGAMAC_IMP":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_ALM":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_DEV":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_DON":
                codProg = "TP_SITU_DON";
                break;
            case "TP_GTGAMAC_AGE":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_SUC":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_COL":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_MUN":
                codProg = StringUtil.VACIO;//No existe arma
                break;
            case "TP_GTGAMAC_REI":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_SAL":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_DDV":
                codProg = "TP_SITU_POS";
                break;
            default:
                switch (tipoGuia.getTipoId().getCodProg()) {
                    case "TP_GTGAMAC_ALM":
                        switch (tipoGuia.getCodProg()) {
                            case "TP_GTALM_DEC":
                                codProg = "TP_SITU_INT";
                                break;
                            case "TP_GTALM_INC":
                                codProg = "TP_SITU_INT";
                                break;
                            default:
                                codProg = "TP_SITU_INT";
                                break;
                        }
                        break;
                    case "TP_GTGAMAC_DEV":
                        codProg = "TP_SITU_POS";
                        break;
                    case "TP_GTGAMAC_SUC":
                        codProg = "TP_SITU_INT";
                        break;
                    case "TP_GTGAMAC_DON":
                        switch (tipoGuia.getCodProg()) {
                            case "TP_GTDON_DONA":
                                codProg = "TP_SITU_DON";
                                break;
                            case "TP_GTDON_DEST":
                                codProg = "TP_SITU_DES";
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        switch (tipoGuia.getTipoId().getTipoId().getCodProg()) {
                            case "TP_GTGAMAC_ALM":
                                codProg = "TP_SITU_INT";
                                break;
                            default:
                                break;
                        }
                        break;
                }
        }
        return codProg;
    }

    public void actualizarDatosAlmacen(List<AmaInventarioArma> listInvArma, SbDireccion dirSucamec, Boolean inSucamec) {
        if (!JsfUtil.isNullOrEmpty(listInvArma)) {
            for (AmaInventarioArma invArma : listInvArma) {
                if (inSucamec) {
                    invArma.setCondicionAlmacen(JsfUtil.TRUE);
                    if (dirSucamec != null && !Objects.equals(dirSucamec, invArma.getAlmacenSucamecId())) {
                        invArma.setAlmacenSucamecId(dirSucamec);
                    }
                } else {
                    invArma.setCondicionAlmacen(JsfUtil.FALSE);
                }
                ejbAmaInventarioArmaFacade.edit(invArma);
            }
        }
    }

    public void actualizarCodigoArmas(List<AmaInventarioArma> listInvArma) {
        if (!JsfUtil.isNullOrEmpty(listInvArma)) {
            for (AmaInventarioArma invArma : listInvArma) {
                if (invArma.getCodigo() == null || Objects.equals(invArma.getCodigo(), L_CERO)) {
                    invArma.setCodigo(ejbSbNumeracionFacade.selectNumeroCodProg("NUM_INVARM"));
                }
                ejbAmaInventarioArmaFacade.edit(invArma);
            }
        }
    }

    //FUNCIONES Y METODOS PARA EL FORMULARIO DE CREAR PERSONA
    /**
     * @return the listTipoDoc
     */
    public List<TipoBaseGt> getListTipoDoc() {
        List<TipoBaseGt> listRes = ejbTipoBaseFacade.lstTipoBase("TP_DOCID");
        List<TipoBaseGt> listTemp = new ArrayList(listRes);
        for (TipoBaseGt tipoDoc : listTemp) {
            if ("rep".equals(tipoPersona)) {
                if (!"TP_DOCID_DNI".equals(tipoDoc.getCodProg())
                        && !"TP_DOCID_CE".equals(tipoDoc.getCodProg())) {
                    listRes.remove(tipoDoc);
                }
            }
        }
        return listRes;
    }

    /**
     * @return the fechaMaxima
     */
    public Date getFechaMinMayorEdad() {
        return amaGuiaTransitoGenericoController.calculoFechaVencMeses(fechaHoy, -12 * 18);
    }

    public void obtenerNumCaract() {
//        TipoBase tipoDoc = newPersona.getTipoDoc();
        newPersona = new SbPersona();
//        newPersona.setTipoDoc(tipoDoc);
        if (tipoDocSelect != null) {
            if ("TP_DOCID_DNI".equalsIgnoreCase(tipoDocSelect.getCodProg())) {
                setCantNumeros("8");
                setDisabledBtVal(Boolean.FALSE);
            } else if ("TP_DOCID_CE".equalsIgnoreCase(tipoDocSelect.getCodProg())) {
                setCantNumeros("12");
                setDisabledBtVal(Boolean.FALSE);
            } else if ("TP_DOCID_RUC".equalsIgnoreCase(tipoDocSelect.getCodProg())) {
                setCantNumeros("11");
                setDisabledBtVal(Boolean.FALSE);
            } else {
                setCantNumeros("15");
                setDisabledBtVal(Boolean.TRUE);
            }
        }
    }

    public void validarDocumento() {
        String msjErrorWsPide = StringUtil.VACIO;
        try {
            Consulta_Service serR = new Consulta_Service();
            Consulta port = serR.getConsultaPort();
            PideMigraciones_Service serMigra = new PideMigraciones_Service();
            PideMigraciones portMigra = serMigra.getPideMigracionesPort();
            wspide.BeanDdp pj = null;
            wspide.Persona pn = null;
            wspide.ResPideMigra pext = null;

            newPersona = new SbPersona();
            if (getTipoDocSelect() != null) {
                newPersona.setTipoDoc(getTipoDocSelect());
                if (!StringUtil.isNullOrEmpty(numeroDoc)) {
                    if ("TP_DOCID_DNI".equalsIgnoreCase(getTipoDocSelect().getCodProg())) {
                        newPersona.setNumDoc(getNumeroDoc());
                        pn = port.consultaDNI(getNumeroDoc());
                        if (pn != null) {
                            newPersona.setApePat(pn.getAPPAT());
                            newPersona.setApeMat(pn.getAPMAT());
                            newPersona.setNombres(pn.getNOMBRES());
                            if (pn.getFENAC() != null && !StringUtil.VACIO.equals(pn.getFENAC())) {
                                Date date = null;
                                try {
                                    date = date = JsfUtil.obtenerFechaPorDatos(pn.getFENAC().substring(6, 8), pn.getFENAC().substring(4, 6), pn.getFENAC().substring(0, 4));
                                } catch (Exception ex) {
                                    date = null;
                                }
                                if (date != null) {
                                    newPersona.setFechaNac(date);
                                }
                            }
                            if (pn.getSEXO() != null && !StringUtil.VACIO.equals(pn.getSEXO())) {
                                newPersona.setGeneroId(null);
                                if ("F".equals(pn.getSEXO())) {
                                    newPersona.setGeneroId(ejbTipoBaseFacade.tipoPorCodProg("TP_GEN_FEM"));
                                } else {
                                    newPersona.setGeneroId(ejbTipoBaseFacade.tipoPorCodProg("TP_GEN_MAS"));
                                }
                            }
                        } else {
                            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeServicioConsultaReniecNulo"));
                        }
                    } else if ("TP_DOCID_CE".equalsIgnoreCase(getTipoDocSelect().getCodProg())) {
                        newPersona.setNumDoc(getNumeroDoc());
                        pext = portMigra.consultarDocExt(loginController.getUsuario().getLogin(), getNumeroDoc(), getTipoDocSelect().getAbreviatura());
                        if (pext != null) {
                            if (pext.isRspta()) {
                                if (pext.getMensaje().contains("0000")) {
                                    newPersona.setApePat(pext.getResultado().getStrPrimerApellido());
                                    newPersona.setApeMat(pext.getResultado().getStrSegundoApellido());
                                    newPersona.setNombres(pext.getResultado().getStrNombres());
                                } else {
                                    JsfUtil.mensajeError("No existen datos con el número de Carnet de Extranjería ingresado o no se encuentra vigente.");
                                }
                            } else {
                                String msj = StringUtil.VACIO;
                                if (pext.getMensaje().contains("0007")) {
                                    msj = "La información del documento consultado no puede ser mostrada porque pertenece a un menor de edad.";
                                } else if (pext.getMensaje().contains("0006")) {
                                    msj = "El tipo de documento ingresado no es el correcto.";
                                } else if (pext.getMensaje().contains("0004")) {
                                    msj = "No está permitido el uso de valores nulos o vacíos en la consulta.";
                                } else if (pext.getMensaje().contains("0003")) {
                                    msj = "Transacción no exitosa.";
                                } else if (pext.getMensaje().contains("0002")) {
                                    msj = "Sin conexión.";
                                }
                                JsfUtil.mensajeError(msj);
                            }
                        } else {
                            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeServicioMigracionesNulo"));
                        }
                    } else if ("TP_DOCID_RUC".equalsIgnoreCase(getTipoDocSelect().getCodProg())) {
                        if (getNumeroDoc().startsWith("20")) {
                            newPersona.setRuc(getNumeroDoc());
                            pj = port.consultaRuc(getNumeroDoc());
                            if (pj != null) {
//                                if (pj.isEsHabido()) {
                                newPersona.setRznSocial(pj.getDdpNombre());
//                                } else {
//                                    JsfUtil.mensajeAdvertencia("No se encontró resultado con el RUC ingresado.");
//                                }
                            } else {
//                                newPersona.setNumDoc(null);
                                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeServicioConsultaSunatNulo"));
                            }
                        } else {
                            JsfUtil.mensajeAdvertencia("El RUC ingresado no corresponde a una persona jurídica.");
                        }
                    }
                } else {
                    JsfUtil.mensajeAdvertencia("Ingrese el número de documento a validar.");
                }
            } else {
                JsfUtil.mensajeAdvertencia("No se encontraron resultados. Por favor, inténtelo nuevamente.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("Error al consultar el documento ingresado."));
        }
    }

    public void validarDatoIngresado(String in) {
        String tipo = StringUtil.VACIO;
        int tam = 0;
        String valor = StringUtil.VACIO;
        String idCampo = StringUtil.VACIO;
        switch (in) {
            case "NUMDOC":
                disabledBtVal = Boolean.TRUE;
                if (newPersona.getNumDoc() != null && !StringUtil.VACIO.equals(newPersona.getNumDoc().trim())) {
                    tam = newPersona.getNumDoc().length();
                    valor = newPersona.getNumDoc();
                    tipo = "N";
                }
                RequestContext.getCurrentInstance().update("formCrearPersona");
                idCampo = "numDoc";
                break;
            case "NOM":
                if (newPersona.getNombres() != null && !StringUtil.VACIO.equals(newPersona.getNombres().trim())) {
                    tam = newPersona.getNombres().length();
                    valor = newPersona.getNombres();
                    tipo = "L";
                }
                idCampo = "nomb";
                break;
            case "APPAT":
                if (newPersona.getApePat() != null && !StringUtil.VACIO.equals(newPersona.getApePat().trim())) {
                    tam = newPersona.getApePat().length();
                    valor = newPersona.getApePat();
                    tipo = "L";
                }
                idCampo = "appat";
                break;
            case "APMAT":
                if (newPersona.getApeMat() != null && !StringUtil.VACIO.equals(newPersona.getApeMat().trim())) {
                    tam = newPersona.getApeMat().length();
                    valor = newPersona.getApeMat();
                    tipo = "L";
                }
                idCampo = "apmat";
                break;
            default:
                break;
        }
        String nomCampo = StringUtil.VACIO;
        Boolean isLetra = Boolean.TRUE;
        Boolean isNumero = Boolean.TRUE;
        if ("N".equalsIgnoreCase(tipo)) {
            for (int i = 0; i < tam; i++) {
                if (!Character.isDigit(valor.charAt(i))
                        || ' ' == valor.charAt(i)) {
                    isNumero = Boolean.FALSE;
                    break;
                }
            }
            if (!isNumero) {
                nomCampo = limpiarObtenerCampo(in);
                JsfUtil.mensajeAdvertencia("El campo " + nomCampo + " debe contener solamente números");
            }
        } else {
            for (int i = 0; i < tam; i++) {
                if (!Character.isLetter(valor.charAt(i))
                        && !Character.isSpaceChar(valor.charAt(i))) {
                    isLetra = Boolean.FALSE;
                    break;
                }
            }
            if (!isLetra) {
                nomCampo = limpiarObtenerCampo(in);
                JsfUtil.mensajeAdvertencia("El campo " + nomCampo + " debe contener solamente letras");
            }
        }
        if (!isLetra || !isNumero) {
            RequestContext.getCurrentInstance().update("formCrearPersona:" + idCampo);
            return;
        } else if ("NUMDOC".equalsIgnoreCase(in)) {
            if (!documentoOk(newPersona)) {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeValidacionNumeroTipoDocumento"));
                return;
            }
        }
    }

    public String limpiarObtenerCampo(String campo) {
        String nomCampo = StringUtil.VACIO;
        switch (campo) {
            case "NUMDOC":
                newPersona.setNumDoc(null);
                nomCampo = "Número de documento";
                break;
            case "NOM":
                newPersona.setNombres(null);
                nomCampo = "Nombres";
                break;
            case "APPAT":
                newPersona.setApePat(null);
                nomCampo = "Apellido paterno";
                break;
            case "APMAT":
                newPersona.setApeMat(null);
                nomCampo = "Apellido materno";
                break;
            default:
                break;
        }
        return nomCampo;
    }

    public Boolean documentoOk(SbPersona per) {
        Boolean res = Boolean.TRUE;
        if ("TP_DOCID_DNI".equalsIgnoreCase(per.getTipoDoc().getCodProg())
                && per.getNumDoc().length() < 8) {
            res = Boolean.FALSE;
        }
        if ("TP_DOCID_CE".equalsIgnoreCase(per.getTipoDoc().getCodProg())
                && per.getNumDoc().length() < 9) {
            res = Boolean.FALSE;
        }
        if ("TP_DOCID_RUC".equalsIgnoreCase(per.getTipoDoc().getCodProg())
                && per.getRuc().length() < 11) {
            res = Boolean.FALSE;
        }
        return res;
    }

    public void crearPersona() {
        if (validarDatosPersona(newPersona)) {
//            setearDatosPersona(newPersona);
            if (documentoOk(newPersona)) {
                List<SbPersona> listPer = ejbSbPersonaFacade.buscarPersonaXNumDoc(newPersona.getNumDoc() == null ? newPersona.getRuc() : newPersona.getNumDoc());
                if (!JsfUtil.isNullOrEmpty(listPer)) {
                    for (SbPersona per : listPer) {
                        if (per.getActivo() == JsfUtil.FALSE) {
                            per.setActivo(JsfUtil.TRUE);
                            ejbSbPersonaFacade.edit(per);
                        }
                    }
                }
                if (JsfUtil.isNullOrEmpty(listPer)) {
                    try {
                        if ("TP_DOCID_DNI".equalsIgnoreCase(newPersona.getTipoDoc().getCodProg())
                                || "TP_DOCID_CE".equalsIgnoreCase(newPersona.getTipoDoc().getCodProg())) {
                            newPersona.setTipoId(ejbSbTipoFacade.tipoPorCodProg("TP_PER_NAT"));
                            
                            newPersona.setRznSocial(null);
                            newPersona.setRuc(null);
                            //Validación 
                        } else if ("TP_DOCID_RUC".equalsIgnoreCase(newPersona.getTipoDoc().getCodProg())
                                || "TP_DOCID_EXT".equalsIgnoreCase(newPersona.getTipoDoc().getCodProg())) {
                            if (newPersona.getRuc().startsWith("20")) {
                                newPersona.setTipoId(ejbSbTipoFacade.tipoPorCodProg("TP_PER_JUR"));
                                newPersona.setNombres(null);
                                newPersona.setApePat(null);
                                newPersona.setApeMat(null);
                                newPersona.setNumDoc(null);
                            } else {
                                JsfUtil.mensajeError("El RUC ingresado no corresponde a una persona jurídica.");
                                return;
                            }
                        }
                        newPersona.setId(null);
                        newPersona.setFechaReg(new Date());
                        newPersona.setActivo(JsfUtil.TRUE);
                        newPersona = (SbPersona) JsfUtil.entidadMayusculas(newPersona, StringUtil.VACIO);
                        ejbSbPersonaFacade.create(newPersona);
                        JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroPersonaExito"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorRegistroPersona"));
                    }
                } else {
                    newPersona = (SbPersona) listPer.get(0);
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeNumeroDocRegistrado"));
                }
                if (newPersona.getId() != null) {
                    RequestContext.getCurrentInstance().execute("PF('CrearPersonaViewDialog').hide()");
                    switch (tipoPersona) {
                        case "sol":
                            personaReg = newPersona;
                            JsfUtil.mensaje("Se estableció correctamente el solicitante.");
                            RequestContext.getCurrentInstance().update("registroForm:gridSolicitante");
                            RequestContext.getCurrentInstance().update("editarForm:gridSolicitante");
                            //Formulario de devolución.
                            RequestContext.getCurrentInstance().update("registroDevForm:panelSolicitante");
                            RequestContext.getCurrentInstance().update("editarDevForm:panelSolicitante");
                            break;
                        case "rep":
                            vincularRepresentante(newPersona);
                            break;
                    }
                }
            } else {
                JsfUtil.mensajeError(JsfUtil.bundle("MensajeValidacionNumeroTipoDocumento"));
            }
        } else {
            return;
        }
    }

    public Boolean validarDatosPersona(SbPersona persona) {
        Boolean res = Boolean.TRUE;
        if (tipoDocSelect != null) {
            if ("TP_DOCID_DNI".equals(tipoDocSelect.getCodProg())
                    || "TP_DOCID_CE".equals(tipoDocSelect.getCodProg())) {
                if (numeroDoc == null || StringUtil.VACIO.equals(numeroDoc.trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("CrearPersonaRequiredMessage_NumDoc"));
                    res = Boolean.FALSE;
                }
                if (persona.getNombres() == null || StringUtil.VACIO.equals(persona.getNombres().trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("CrearPersonaRequiredMessage_Nombres"));
                    res = Boolean.FALSE;
                }
                if (persona.getApePat() == null || StringUtil.VACIO.equals(persona.getApePat().trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("CrearPersonaRequiredMessage_ApePat"));
                    res = Boolean.FALSE;
                }
                if ((persona.getApeMat() == null || StringUtil.VACIO.equals(persona.getApeMat().trim()))
                        && "TP_DOCID_DNI".equals(tipoDocSelect.getCodProg())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("CrearPersonaRequiredMessage_ApeMat"));
                    res = Boolean.FALSE;
                }
//                if (persona.getGeneroId() == null) {
//                    JsfUtil.mensajeError(JsfUtil.bundle("CrearPersonaRequiredMessage_Genero"));
//                    res = Boolean.FALSE;
//                }
//                if (persona.getOcupacionId() == null) {
//                    JsfUtil.mensajeError(JsfUtil.bundle("CrearPersonaRequiredMessage_Ocupacion"));
//                    res = Boolean.FALSE;
//                }
            } else if ("TP_DOCID_RUC".equals(tipoDocSelect.getCodProg())) {
                if (persona.getRuc() == null || StringUtil.VACIO.equals(persona.getRuc().trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("CrearPersonaRequiredMessage_NumDoc"));
                    res = Boolean.FALSE;
                }
                if (persona.getRznSocial() == null || StringUtil.VACIO.equals(persona.getRznSocial().trim())) {
                    JsfUtil.mensajeError("El campo Razón Social es obligatorio");
                    res = Boolean.FALSE;
                }
            } else {
                JsfUtil.mensajeAdvertencia("Tipo de documento no válido.");
                res = Boolean.FALSE;
            }
        } else {
            JsfUtil.mensajeError(JsfUtil.bundle("CrearPersonaRequiredMessage_TipoDoc"));
            res = Boolean.FALSE;
        }

        return res;
    }

    public void vincularRepresentante(SbPersona per) {
        if (per != null) {
            repreSelect = per;
            SbRelacionPersona rp = new SbRelacionPersona();
            rp.setTipoId(ejbTipoBaseFacade.tipoPorCodProg("TP_REL_RL"));
            rp.setPersonaOriId(personaReg);
            rp.setPersonaDestId(repreSelect);
            rp.setActivo(JsfUtil.TRUE);
            rp.setFecha(new Date());
            ejbSbRelacionPersonaFacade.create(rp);
            RequestContext.getCurrentInstance().execute("PF('AgregarRepViewDialog').hide()");
            JsfUtil.mensaje("Se vinculó correctamente a la persona.");
            RequestContext.getCurrentInstance().update("registroForm:pnlRepre");
            RequestContext.getCurrentInstance().update("editarForm:pnlRepre");
            RequestContext.getCurrentInstance().update("registroDevForm:panelSolicitante");
            RequestContext.getCurrentInstance().update("editarDevForm:panelSolicitante");
        } else {
            JsfUtil.mensajeAdvertencia("Ingrese persona a vincular.");
        }
    }

    public void eliminarRepresentante() {
        representante = null;
        RequestContext.getCurrentInstance().update("formAgregarRep");
    }

    public void regresarRegPersona() {
        RequestContext.getCurrentInstance().execute("PF('CrearPersonaViewDialog').hide()");
    }

    /**
     * Función para campo propietario autocompletable
     *
     * @param query Campo a buscar
     * @return Listado de personas
     * @author Gino Chávez
     * @version 1.0
     */
    public List<SbPersona> completePersona(String query) {
        List<SbPersona> lista = ejbSbPersonaFacade.listarPorNombresORznSocialODoc(query, null);
        return lista;
    }

    /**
     * Función para campo propietario autocompletable
     *
     * @param query Campo a buscar
     * @return Listado de personas
     * @author Gino Chávez
     * @version 1.0
     */
    public List<SbPersona> completePersonaRznNomDoc(String query) {
        List<SbPersona> lista = ejbSbPersonaFacade.selectLikePersonasNoSucamec(query);
        return lista;
    }

    /**
     * METODO PARA GENERAR EL PDF DE LA SOLICITUD DE RECOJO
     *
     * @param objSelect
     * @return Un archivo Pdf
     */
    public StreamedContent verDepDevPdf(Map objSelect) {
        try {
            String jasper = "/aplicacion/amaDepositoArmas/reportes/DepositoDevolucion.jasper";
            List<AmaGuiaTransito> agt = new ArrayList();
            AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.find(Long.valueOf(objSelect.get("ID").toString()));
            agt.add(amaGuia);
            if ("TP_GTGAMAC_FIN".equalsIgnoreCase(amaGuia.getEstadoId().getCodProg())) {
                StreamedContent pdf = null;
                try {
                    pdf = JsfUtil.obtenerArchivo((ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_actasDepDev").getValor()), amaGuia.getId() + ".pdf", amaGuia.getId() + ".pdf", "application/pdf");
                } catch (Exception e) {
                }
                if (pdf == null) {
                    if (subirActaPdf(amaGuia, jasper, agt)) {
                        return JsfUtil.obtenerArchivo((ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_actasDepDev").getValor()), amaGuia.getId() + ".pdf", amaGuia.getId() + ".pdf", "application/pdf");
                    }
                } else {
                    return pdf;
                }

            } else {
                return JsfUtil.generarReportePdf(jasper, agt, parametrosAlmacenPdf(amaGuia), amaGuia.getId() + "_borrador.pdf");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: AmaGuiaTransito.reporte :", ex);
        }
        return null;
    }

    /**
     * FUNCION PARA SUBIR PDF A SERVIDOR
     *
     * @author Gino Chávez
     * @version 2.0
     * @param amaGuia Registro a crear reporte
     * @param nombreJasper Nombre de reporte (.jasper)
     * @return Flag de proceso de subir pdf
     */
    private Boolean subirActaPdf(AmaGuiaTransito amaGuia, String nombreJasper, List<AmaGuiaTransito> lp) {
        boolean valida = true;
        try {
            HashMap parametros = parametrosAlmacenPdf(amaGuia);
            if (parametros != null) {
                JsfUtil.subirPdf(amaGuia.getId().toString(), parametros, lp, nombreJasper, (ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_actasDepDev").getValor()));
            } else {
                valida = false;
            }
        } catch (Exception ex) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeNoSubioArchivo") + StringUtil.ESPACIO + ex.getMessage());
            valida = false;
        }
        return valida;
    }

    public HashMap parametrosAlmacenPdf(AmaGuiaTransito amaGuia) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap p = new HashMap();
        SbPersona solicitante = amaGuia.getSolicitanteId();
        SbDireccion dirSucamec = obtenerDireccionSedeSegunActa(amaGuia);
        p.put("TP_DIR_SEDE", dirSucamec == null ? null : StringUtils.capitalize(dirSucamec.getDistritoId().getNombre().toLowerCase()));
        if ("TP_PER_JUR".equalsIgnoreCase(solicitante.getTipoId().getCodProg())) {
            solicitante = amaGuia.getRepresentanteId();
        }
        p.put("P_TIPO_DEP", obtenerTipoDeposito(amaGuia));
        List<ArmaRepClass> listRp = amaGuiaTransitoGenericoController.tablaArmas(amaGuia);
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        p.put("P_FECHA_EMI", JsfUtil.mostrarFechaDateFormat(amaGuia.getFechaEmision()));
        p.put("P_HORA_EMI", amaGuia.getFechaEmision() == null ? StringUtil.VACIO : formatoHora.format(amaGuia.getFechaEmision()));
        //p.put("P_EXTERIOR", StringUtil.VACIO);//falta--Es un campo de AmaGuiaTRansito
        for (ArmaRepClass arma : listRp) {
            p.put("P_SERIE_ARMA", arma.getSerie());
            p.put("P_MODELO_ARMA", arma.getModelo());
            p.put("P_CALIBRE_ARMA", arma.getCalibre());
            p.put("P_MARCA_ARMA", arma.getMarca() == null ? StringUtil.VACIO : arma.getMarca().getNombre());
            p.put("P_ESTADO_SERIE", arma.getEstadoserieId() == null ? StringUtil.VACIO : arma.getEstadoserieId().getNombre());
            p.put("P_TIPO_ARMA", arma.getTipoArma() == null ? StringUtil.VACIO : arma.getTipoArma().getNombre());
            p.put("P_MOD_TIRO", arma.getModalidadTiroId() == null ? StringUtil.VACIO : arma.getModalidadTiroId().getNombre());
            p.put("P_PROPIETARIO", obtenerDescPersona(arma.getPropietarioId()));
            p.put("P_NOVEDAD_CANON", arma.getNovedadCanonId() == null ? StringUtil.VACIO : arma.getNovedadCanonId().getNombre());
            p.put("P_MECANISMOS", arma.getMecanismoId() == null ? StringUtil.VACIO : arma.getMecanismoId().getNombre());
            p.put("P_EXTERIOR", arma.getExteriorId() == null ? StringUtil.VACIO : arma.getExteriorId().getNombre());
            p.put("P_CACHAS", (arma.getMaterialCachaId() == null ? StringUtil.VACIO : (arma.getMaterialCachaId().getNombre())) + (arma.getEstadoCachaId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoCachaId().getNombre())));
            p.put("P_GUARDAMANO", (arma.getMaterialGuardamanoId() == null ? StringUtil.VACIO : arma.getMaterialGuardamanoId().getNombre()) + (arma.getEstadoGuardamanoId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoGuardamanoId().getNombre())));
            p.put("P_CULATA", (arma.getMaterialCulataId() == null ? StringUtil.VACIO : arma.getMaterialCulataId().getNombre()) + (arma.getEstadoCulataId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoCulataId().getNombre())));
            p.put("P_CANTONERA", (arma.getMaterialCantoneraId() == null ? StringUtil.VACIO : arma.getMaterialCantoneraId().getNombre()) + (arma.getEstadoCantoneraId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoCantoneraId().getNombre())));
            p.put("P_EMPUNADURA", (arma.getMaterialEmpunaduraId() == null ? StringUtil.VACIO : arma.getMaterialEmpunaduraId().getNombre()) + (arma.getEstadoEmpunaduraId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoEmpunaduraId().getNombre())));
            p.put("P_ESTADO_CONSER", arma.getEstadoconservacionId() == null ? StringUtil.VACIO : arma.getEstadoconservacionId().getNombre());
            p.put("P_ESTADO_ARMA", arma.getEstadofuncionalId() == null ? StringUtil.VACIO : arma.getEstadofuncionalId().getNombre());
            p.put("P_SITUACION", arma.getSituacionId() == null ? StringUtil.VACIO : arma.getSituacionId().getNombre());
            break;
        }
        AmaGuiaTransito amaGuiaDep = amaGuia.getGuiaReferenciadaId();
        SbPersona solDeposito = amaGuiaDep.getSolicitanteId();
        if ("TP_PER_JUR".equalsIgnoreCase(solDeposito.getTipoId().getCodProg())) {
            solDeposito = amaGuiaDep.getRepresentanteId();
            p.put("P_SOLICITANTE_DEP", obtenerNombrePersona(solDeposito));
            p.put("P_SOLICITANTE_DEV", obtenerNombrePersona(solicitante));
            p.put("P_IS_DEPOSITO", false);
            p.put("P_FECHA_DEV", amaGuia.getFechaEmision());
        }
        p.put("P_DOC_SOLICITANTE", obtenerDocumentoPersona(solicitante));
        p.put("P_TIPO", tipoAlmaFisico);
        p.put("P_LISTA_INFRACTORES", (amaGuia.getSbPersonaList() != null && amaGuia.getSbPersonaList().size() > 0) ? amaGuia.getSbPersonaList() : null);
        p.put("P_LISTA_ACCESORIOS", obtenerAccesMap(obtenerAccesoriosVer(amaGuia)));
        p.put("P_LISTA_ACCESORIOS_ACTA", lstAccesoriosActa);
        p.put("P_LISTA_MUNICIONES", obtenerListMuniActivos(amaGuia));
        p.put("P_LISTA_DOCUMENTOS", amaGuia.getAmaDocumentoList());
        p.put("P_LOGO_SUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        p.put("P_HASHQR", "https://www.sucamec.gob.pe/sel/faces/qr/gamacGT.xhtml?h=" + amaGuia.getHashQr());
        return p;
    }

//    public List<ArmaRepClass> tablaArmas(AmaGuiaTransito amaGuia) {
//        List<ArmaRepClass> armasRepLst = new ArrayList();
//        if (amaGuia != null) {
//            if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaArmaList())) {
//                for (AmaArma amaArma : amaGuia.getAmaArmaList()) {
//                    ArmaRepClass armaRep = new ArmaRepClass();
//                    armaRep.setTipoArma(amaArma.getModeloId().getTipoArmaId());
//                    armaRep.setMarca(amaArma.getModeloId().getMarcaId());
//                    armaRep.setModelo(amaArma.getModeloId().getModelo());
//                    armaRep.setCalibre(ordenarCalibres(amaArma.getModeloId().getAmaCatalogoList()));
//                    armaRep.setSerie(amaArma.getSerie());
//                    armaRep.setNroRua(amaArma.getNroRua());
//                    armasRepLst.add(armaRep);
//                }
//            }
//            if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList())) {
//                for (AmaInventarioArma amaInvArma : amaGuia.getAmaInventarioArmaList()) {
//                    ArmaRepClass armaRep = new ArmaRepClass();
//                    armaRep.setTipoArma(amaInvArma.getModeloId().getTipoArmaId());
//                    armaRep.setMarca(amaInvArma.getModeloId().getMarcaId());
//                    armaRep.setModelo(amaInvArma.getModeloId().getModelo());
//                    armaRep.setCalibre(ordenarCalibres(amaInvArma.getModeloId().getAmaCatalogoList()));
//                    armaRep.setSerie(amaInvArma.getSerie());
//                    armaRep.setNroRua(amaInvArma.getNroRua());
//                    armaRep.setEstadoserieId(amaInvArma.getEstadoserieId());
//                    armaRep.setModalidadTiroId(amaInvArma.getModalidadTiroId());
//                    armaRep.setPropietarioId(amaInvArma.getPropietarioId());
//                    armaRep.setNovedadCanonId(amaInvArma.getNovedadCanonId());
//                    armaRep.setMecanismoId(amaInvArma.getMecanismoId());
//                    armaRep.setExteriorId(amaInvArma.getExteriorId());
//                    armaRep.setMaterialCachaId(amaInvArma.getMaterialCachaId());
//                    armaRep.setEstadoCachaId(amaInvArma.getEstadoCachaId());
//                    armaRep.setMaterialCantoneraId(amaInvArma.getMaterialCantoneraId());
//                    armaRep.setEstadoCantoneraId(amaInvArma.getEstadoCantoneraId());
//                    armaRep.setMaterialCulataId(amaInvArma.getMaterialCulataId());
//                    armaRep.setEstadoCulataId(amaInvArma.getEstadoCulataId());
//                    armaRep.setMaterialEmpunaduraId(amaInvArma.getMaterialEmpunaduraId());
//                    armaRep.setEstadoEmpunaduraId(amaInvArma.getEstadoEmpunaduraId());
//                    armaRep.setMaterialGuardamanoId(amaInvArma.getMaterialGuardamanoId());
//                    armaRep.setEstadoGuardamanoId(amaInvArma.getEstadoGuardamanoId());
//                    armaRep.setEstadoconservacionId(amaInvArma.getEstadoconservacionId());
//                    armaRep.setEstadofuncionalId(amaInvArma.getEstadofuncionalId());
//                    armaRep.setSituacionId(amaInvArma.getSituacionId());
//                    armasRepLst.add(armaRep);
//                }
//
//            }
//        }
//        return armasRepLst;
//    }
    public String ordenarCalibres(List<AmaCatalogo> calibresLst) {
        //ordernar calibres de un arma
        Collections.sort(calibresLst, new Comparator<AmaCatalogo>() {
            @Override
            public int compare(AmaCatalogo one, AmaCatalogo other) {
                return one.getNombre().compareTo(other.getNombre());
            }
        });

        //formar cadena de calibres para un arma
        String calibresStr = StringUtil.VACIO;
        for (AmaCatalogo cal : calibresLst) {
            if (cal.getActivo() == 1) {
                if (StringUtil.VACIO.equals(calibresStr)) {
                    calibresStr = cal.getNombre();
                } else {
                    calibresStr = calibresStr + " / " + cal.getNombre();
                }
            }
        }
        return calibresStr;
    }

    /**
     * OBTENER LISTADO DE ACCESORIOS EN 4 COLUMNAS PARA TABLA
     *
     * @author Gino Chávez
     * @version 2.0
     * @param listAcces Listado de Accesorios para preparar Tabla de reporte
     * @return Listado de Accesorios
     */
    public List<Map> obtenerAccesMap(List<AmaInventarioAccesorios> listAcces) {
        short cont = 0;
        List<Map> lista = new ArrayList();
        Map mMap = new HashMap();
        if (listAcces != null) {
            for (AmaInventarioAccesorios acces : listAcces) {
                cont++;
                switch (cont) {
                    case 1:
                        mMap.put("cant1", String.valueOf(acces.getCantidad()));
                        mMap.put("nombre1", acces.getArticuloConexoId().getNombre());
                        break;
                    case 2:
                        mMap.put("cant2", String.valueOf(acces.getCantidad()));
                        mMap.put("nombre2", acces.getArticuloConexoId().getNombre());
                        break;
                    case 3:
                        mMap.put("cant3", String.valueOf(acces.getCantidad()));
                        mMap.put("nombre3", acces.getArticuloConexoId().getNombre());
                        lista.add(mMap);
                        mMap = new HashMap();
                        cont = 0;
                        break;
                }
            }

            if (cont < 3 && cont > 0) {
                lista.add(mMap);
            }
        }

        return lista;
    }

    /**
     * FUNCIÓN QUE OBTIENE LA DIRECCIÓN DEL ALMACÉN SUCAMEC SEGÚN EL TIPO DE
     * GUIA (DEPÓSITO/DEVOLUCIÓN)
     *
     * @param amaGuia
     * @return
     */
    public SbDireccion obtenerDireccionSedeSegunActa(AmaGuiaTransito amaGuia) {
        SbDireccion res = null;
        if (amaGuia != null) {
            List<TipoGamac> listTipoDeposito = ejbTipoGamacFacade.selectTipoGamac("TP_GTGAMAC_ALM");
            List<TipoGamac> listTipoDevolucion = ejbTipoGamacFacade.selectTipoGamac("TP_GTGAMAC_DEV");
            String tipoActa = StringUtil.VACIO;
            if (devolucion.equals(tipoAlmaFisico) && listTipoDevolucion != null) {
                for (TipoGamac tg : listTipoDevolucion) {
                    if (Objects.equals(tg, amaGuia.getTipoGuiaId())) {
                        tipoActa = "dev";
                        break;
                    }
                }
            }
            if (Objects.equals("dep", tipoActa)) {
                res = amaGuia.getDireccionDestinoId();
            } else if (Objects.equals("dev", tipoActa)) {
                res = amaGuia.getDireccionOrigenId();
            }
        }
        return res;
    }

    public String obtenerDocumentoPersona(SbPersona persona) {
        String res = StringUtil.VACIO;
        if (persona != null) {
            res = ("TP_PER_NAT".equals(persona.getTipoId().getCodProg()) && persona.getTipoDoc() == null) ? "DNI - " + persona.getNumDoc()
                    : (("TP_PER_NAT".equals(persona.getTipoId().getCodProg()) && persona.getTipoDoc() != null) ? ("TP_DOCID_DNI".equals(persona.getTipoDoc().getCodProg()) ? "DNI - " + persona.getNumDoc()
                    : ("TP_DOCID_CE".equals(persona.getTipoDoc().getCodProg())) ? "C. E. - " + persona.getNumDoc() : StringUtil.VACIO) : "RUC - " + persona.getRuc());
        }
        return res;
    }

    public void openDlgDatosActa(Long idActa) {
        setFlagCollapsed(Boolean.TRUE);
        if (idActa != null) {
            limpiarDatosDepDev();
            AmaGuiaTransito actaTemp = ejbAmaGuiaTransitoFacade.find(idActa);
            actaDepositoSelectL = actaTemp.getGuiaReferenciadaId();
            actaDevolucionSelectL = actaTemp;
            procesarTipoGuia(actaTemp.getTipoGuiaId(), StringUtil.VACIO);
            obtenerInvArma(actaTemp);
            for (AmaDocumento documento : actaTemp.getAmaDocumentoList()) {
                if (documento.getActivo() == JsfUtil.TRUE) {
                    setDocumentoSelect(documento);
                }
                break;
            }
            for (AmaInventarioArma invArma : actaTemp.getAmaInventarioArmaList()) {
                if (invArma.getActivo() == JsfUtil.TRUE && invArma.getActual() == JsfUtil.TRUE) {
                    setSelectedAmaArma(amaGuiaTransitoGenericoController.armaString(null, String.valueOf(invArma.getId()), null));
                }
                break;
            }
            obtenerFotosYAccesoriosArma(actaTemp, Boolean.FALSE);
            lstFotosTemp = null;
            obtenerListMuniActivos(actaTemp);
            RequestContext.getCurrentInstance().execute("PF('VerDatosActaViewDialog').show()");
            RequestContext.getCurrentInstance().update("verActaForm");
        }
    }

    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public String getTipoAlmaFisico() {
        return tipoAlmaFisico;
    }

    public void setTipoAlmaFisico(String tipoAlmaFisico) {
        this.tipoAlmaFisico = tipoAlmaFisico;
    }

    public String getFiltroL() {
        return filtroL;
    }

    public void setFiltroL(String filtroL) {
        this.filtroL = filtroL;
    }

    public ListDataModel<Map> getListGuiasTraslado() {
        return listGuiasTraslado;
    }

    public void setListGuiasTraslado(ListDataModel<Map> listGuiasTraslado) {
        this.listGuiasTraslado = listGuiasTraslado;
    }

    public TipoGamac getModoBus() {
        return modoBus;
    }

    public void setModoBus(TipoGamac modoBus) {
        this.modoBus = modoBus;
    }

    public Long getIdTipoBusquedaL() {
        return idTipoBusquedaL;
    }

    public void setIdTipoBusquedaL(Long idTipoBusquedaL) {
        this.idTipoBusquedaL = idTipoBusquedaL;
    }

    public SbDireccion getDirSucamecBus() {
        return dirSucamecBus;
    }

    public void setDirSucamecBus(SbDireccion dirSucamecBus) {
        this.dirSucamecBus = dirSucamecBus;
    }

    public Boolean getPerfilConsulta() {
        return perfilConsulta;
    }

    public void setPerfilConsulta(Boolean perfilConsulta) {
        this.perfilConsulta = perfilConsulta;
    }

    public TipoGamac getIdTipoGuiaDep() {
        return idTipoGuiaDep;
    }

    public void setIdTipoGuiaDep(TipoGamac idTipoGuiaDep) {
        this.idTipoGuiaDep = idTipoGuiaDep;
    }

    public List<TipoGamac> getListTipoGuiaDep() {
        String codProg = StringUtil.VACIO;
//        if (deposito.equalsIgnoreCase(tipoAlmaFisico)) {
//            codProg = "TP_GTGAMAC_ALM";
//        } else if (devolucion.equalsIgnoreCase(tipoAlmaFisico)) {
        codProg = "TP_GTGAMAC_DEV";
//        }
        listTipoGuiaDep = ejbTipoGamacFacade.selectTipoGamac(codProg);
        return listTipoGuiaDep;
    }

    public void setListTipoGuiaDep(List<TipoGamac> listTipoGuiaDep) {
        this.listTipoGuiaDep = listTipoGuiaDep;
    }

    public List<TipoGamac> getListTipoGuiaDepBus() {
        return getListTipoGuiaDep();
    }

    public void setListTipoGuiaDepBus(List<TipoGamac> listTipoGuiaDepBus) {
        this.listTipoGuiaDepBus = listTipoGuiaDepBus;
    }

    public Date getFechaHoy() {
        return fechaHoy;
    }

    public void setFechaHoy(Date fechaHoy) {
        this.fechaHoy = fechaHoy;
    }

    public Date getFechaEmiActa() {
        return fechaEmiActa;
    }

    public void setFechaEmiActa(Date fechaEmiActa) {
        this.fechaEmiActa = fechaEmiActa;
    }

    public Date getFechaIniL() {
        return fechaIniL;
    }

    public void setFechaIniL(Date fechaIniL) {
        this.fechaIniL = fechaIniL;
    }

    public Date getFechaFinL() {
        return fechaFinL;
    }

    public void setFechaFinL(Date fechaFinL) {
        this.fechaFinL = fechaFinL;
    }

    public Boolean getFlagRegInvArma() {
        return flagRegInvArma;
    }

    public void setFlagRegInvArma(Boolean flagRegInvArma) {
        this.flagRegInvArma = flagRegInvArma;
    }

    public void setListTipoGuia(List<TipoGamac> listTipoGuia) {
        this.listTipoGuia = listTipoGuia;
    }

    public int getCheckTipoDeposito() {
        return checkTipoDeposito;
    }

    public void setCheckTipoDeposito(int checkTipoDeposito) {
        this.checkTipoDeposito = checkTipoDeposito;
    }

    public AmaGuiaTransito getActaDepositoSelect() {
        return actaDepositoSelect;
    }

    public void setActaDepositoSelect(AmaGuiaTransito actaDepositoSelect) {
        this.actaDepositoSelect = actaDepositoSelect;
    }

    public TipoGamac getIdTipoGuia() {
        return idTipoGuia;
    }

    public void setIdTipoGuia(TipoGamac idTipoGuia) {
        this.idTipoGuia = idTipoGuia;
    }

    public AmaInventarioArma getInvArmaDeposito() {
        return invArmaDeposito;
    }

    public void setInvArmaDeposito(AmaInventarioArma invArmaDeposito) {
        this.invArmaDeposito = invArmaDeposito;
    }

    public List<AmaArma> getListaArmasTemp() {
        return listaArmasTemp;
    }

    public void setListaArmasTemp(List<AmaArma> listaArmasTemp) {
        this.listaArmasTemp = listaArmasTemp;
    }

    public List<AmaInventarioArma> getListaInvArmasTemp() {
        return listaInvArmasTemp;
    }

    public void setListaInvArmasTemp(List<AmaInventarioArma> listaInvArmasTemp) {
        this.listaInvArmasTemp = listaInvArmasTemp;
    }

    public List<Map> getListaArmasFinalTemp() {
        return listaArmasFinalTemp;
    }

    public void setListaArmasFinalTemp(List<Map> listaArmasFinalTemp) {
        this.listaArmasFinalTemp = listaArmasFinalTemp;
    }

    public List<ParametroElementoClass> getListAccesorioMostrar() {
        return listAccesorioMostrar;
    }

    public void setListAccesorioMostrar(List<ParametroElementoClass> listAccesorioMostrar) {
        this.listAccesorioMostrar = listAccesorioMostrar;
    }

    public List<Map> getLstFotos() {
        return lstFotos;
    }

    public void setLstFotos(List<Map> lstFotos) {
        this.lstFotos = lstFotos;
    }

    public List<Map> getLstFotosTemp() {
        return lstFotosTemp;
    }

    public void setLstFotosTemp(List<Map> lstFotosTemp) {
        this.lstFotosTemp = lstFotosTemp;
    }

    public boolean isDisabledBtnFoto() {
        return disabledBtnFoto;
    }

    public void setDisabledBtnFoto(boolean disabledBtnFoto) {
        this.disabledBtnFoto = disabledBtnFoto;
    }

    public StreamedContent getFoto() {
        return foto;
    }

    public void setFoto(StreamedContent foto) {
        this.foto = foto;
    }

    public StreamedContent getFotoImg() {
        return fotoImg;
    }

    public void setFotoImg(StreamedContent fotoImg) {
        this.fotoImg = fotoImg;
    }

    public List<AmaGuiaMuniciones> getListGuiaMunicion() {
        return listGuiaMunicion;
    }

    public void setListGuiaMunicion(List<AmaGuiaMuniciones> listGuiaMunicion) {
        this.listGuiaMunicion = listGuiaMunicion;
    }

    public List<AmaGuiaMuniciones> getListGuiaMunicionSelect() {
        return listGuiaMunicionSelect;
    }

    public void setListGuiaMunicionSelect(List<AmaGuiaMuniciones> listGuiaMunicionSelect) {
        this.listGuiaMunicionSelect = listGuiaMunicionSelect;
    }

    public List<ParametroElementoClass> getListMunicionMostrar() {
        return listMunicionMostrar;
    }

    public void setListMunicionMostrar(List<ParametroElementoClass> listMunicionMostrar) {
        this.listMunicionMostrar = listMunicionMostrar;
    }

    public List<AmaInventarioAccesorios> getListAccesoriosGuia() {
        return listAccesoriosGuia;
    }

    public void setListAccesoriosGuia(List<AmaInventarioAccesorios> listAccesoriosGuia) {
        this.listAccesoriosGuia = listAccesoriosGuia;
    }

    public List<Map> getListGuiasTrasladoSelect() {
        return listGuiasTrasladoSelect;
    }

    public void setListGuiasTrasladoSelect(List<Map> listGuiasTrasladoSelect) {
        this.listGuiasTrasladoSelect = listGuiasTrasladoSelect;
    }

    public Boolean getIsCoorVerificador() {
        return isCoorVerificador;
    }

    public void setIsCoorVerificador(Boolean isCoorVerificador) {
        this.isCoorVerificador = isCoorVerificador;
    }

    public Boolean getIsJefeZonal() {
        return isJefeZonal;
    }

    public void setIsJefeZonal(Boolean isJefeZonal) {
        this.isJefeZonal = isJefeZonal;
    }

    public Boolean getIsCoordinador() {
        return isCoordinador;
    }

    public void setIsCoordinador(Boolean isCoordinador) {
        this.isCoordinador = isCoordinador;
    }

    public String getTipoBusComp() {
        return tipoBusComp;
    }

    public void setTipoBusComp(String tipoBusComp) {
        this.tipoBusComp = tipoBusComp;
    }

    public String getFiltroComp() {
        return filtroComp;
    }

    public void setFiltroComp(String filtroComp) {
        this.filtroComp = filtroComp;
    }

    public Date getFechaCompBus() {
        return fechaCompBus;
    }

    public void setFechaCompBus(Date fechaCompBus) {
        this.fechaCompBus = fechaCompBus;
    }

    public Boolean getMuestraBusqFecha() {
        return muestraBusqFecha;
    }

    public void setMuestraBusqFecha(Boolean muestraBusqFecha) {
        this.muestraBusqFecha = muestraBusqFecha;
    }

    public SbPersona getPersonaReg() {
        return personaReg;
    }

    public void setPersonaReg(SbPersona personaReg) {
        this.personaReg = personaReg;
    }

    public List<SbRecibos> getLstRecibosBusqueda() {
        return lstRecibosBusqueda;
    }

    public void setLstRecibosBusqueda(List<SbRecibos> lstRecibosBusqueda) {
        this.lstRecibosBusqueda = lstRecibosBusqueda;
    }

    public SbRecibos getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(SbRecibos comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public Boolean getCheckAllAcces() {
        return checkAllAcces;
    }

    public void setCheckAllAcces(Boolean checkAllAcces) {
        this.checkAllAcces = checkAllAcces;
    }

    public Boolean getCheckAllMuni() {
        return checkAllMuni;
    }

    public void setCheckAllMuni(Boolean checkAllMuni) {
        this.checkAllMuni = checkAllMuni;
    }

    public List<ParametroElementoClass> getListAcceSelect() {
        return listAcceSelect;
    }

    public void setListAcceSelect(List<ParametroElementoClass> listAcceSelect) {
        this.listAcceSelect = listAcceSelect;
    }

    public List<ParametroElementoClass> getListMuniSelect() {
        return listMuniSelect;
    }

    public void setListMuniSelect(List<ParametroElementoClass> listMuniSelect) {
        this.listMuniSelect = listMuniSelect;
    }

    public TipoGamac getSubTipoSelect() {
        return subTipoSelect;
    }

    public void setSubTipoSelect(TipoGamac subTipoSelect) {
        this.subTipoSelect = subTipoSelect;
    }

    public SbPersona getNewPersona() {
        return newPersona;
    }

    public void setNewPersona(SbPersona newPersona) {
        this.newPersona = newPersona;
    }

    public SbPersona getRepreSelect() {
        return repreSelect;
    }

    public void setRepreSelect(SbPersona repreSelect) {
        this.repreSelect = repreSelect;
    }

    /**
     * @return the listRepresentante
     */
    public List<SbPersona> getListRepresentante() {
        if (personaReg != null) {
            return ejbSbPersonaFacade.obtenerRepresentantes(personaReg.getId());
        }
        return null;
    }

    /**
     * @param listRepresentante the listRepresentante to set
     */
    public void setListRepresentante(List<SbPersonaGt> listRepresentante) {
        this.listRepresentante = listRepresentante;
    }

    public SbPersonaGt getRepresentante() {
        return representante;
    }

    public void setRepresentante(SbPersonaGt representante) {
        this.representante = representante;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getObsDevolucion() {
        return obsDevolucion;
    }

    public void setObsDevolucion(String obsDevolucion) {
        this.obsDevolucion = obsDevolucion;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public List<Documento> getListDocumentoTD() {
        return listDocumentoTD;
    }

    public void setListDocumentoTD(List<Documento> listDocumentoTD) {
        this.listDocumentoTD = listDocumentoTD;
    }

    public List<Documento> getListDocumentoTDSelect() {
        return listDocumentoTDSelect;
    }

    public void setListDocumentoTDSelect(List<Documento> listDocumentoTDSelect) {
        this.listDocumentoTDSelect = listDocumentoTDSelect;
    }

    public List<Documento> getListDocTD() {
        return listDocTD;
    }

    public void setListDocTD(List<Documento> listDocTD) {
        this.listDocTD = listDocTD;
    }

    public BigDecimal getIdUsuarioTramDoc() {
        return idUsuarioTramDoc;
    }

    public void setIdUsuarioTramDoc(BigDecimal idUsuarioTramDoc) {
        this.idUsuarioTramDoc = idUsuarioTramDoc;
    }

    public Documento getDocTDSelect() {
        return docTDSelect;
    }

    public void setDocTDSelect(Documento docTDSelect) {
        this.docTDSelect = docTDSelect;
    }

    public Boolean getEsRectificatoria() {
        return esRectificatoria;
    }

    public void setEsRectificatoria(Boolean esRectificatoria) {
        this.esRectificatoria = esRectificatoria;
    }

    public AmaGuiaTransito getRegistro() {
        return registro;
    }

    public void setRegistro(AmaGuiaTransito registro) {
        this.registro = registro;
    }

    public AmaGuiaTransito getActaDevolucionSelect() {
        return actaDevolucionSelect;
    }

    public void setActaDevolucionSelect(AmaGuiaTransito actaDevolucionSelect) {
        this.actaDevolucionSelect = actaDevolucionSelect;
    }

    public AmaGuiaTransito getActaDevolucionSelectL() {
        return actaDevolucionSelectL;
    }

    public void setActaDevolucionSelectL(AmaGuiaTransito actaDevolucionSelectL) {
        this.actaDevolucionSelectL = actaDevolucionSelectL;
    }

    public TipoGamac getSubSubTipoSelect() {
        return subSubTipoSelect;
    }

    public void setSubSubTipoSelect(TipoGamac subSubTipoSelect) {
        this.subSubTipoSelect = subSubTipoSelect;
    }

    public TipoGamac getTipoOrigenSelect() {
        return tipoOrigenSelect;
    }

    public void setTipoOrigenSelect(TipoGamac tipoOrigenSelect) {
        this.tipoOrigenSelect = tipoOrigenSelect;
    }

    public String getMsjRegSolicitante() {
        return msjRegSolicitante;
    }

    public void setMsjRegSolicitante(String msjRegSolicitante) {
        this.msjRegSolicitante = msjRegSolicitante;
    }

    public Boolean getDisabledBtVal() {
        return disabledBtVal;
    }

    public void setDisabledBtVal(Boolean disabledBtVal) {
        this.disabledBtVal = disabledBtVal;
    }

    public String getCantNumeros() {
        return cantNumeros;
    }

    public void setCantNumeros(String cantNumeros) {
        this.cantNumeros = cantNumeros;
    }

    public AmaGuiaTransito getAmaGuiaSelect() {
        return amaGuiaSelect;
    }

    public void setAmaGuiaSelect(AmaGuiaTransito amaGuiaSelect) {
        this.amaGuiaSelect = amaGuiaSelect;
    }

    public AmaGuiaTransito getAmaGuiaSelectL() {
        return amaGuiaSelectL;
    }

    public void setAmaGuiaSelectL(AmaGuiaTransito amaGuiaSelectL) {
        this.amaGuiaSelectL = amaGuiaSelectL;
    }

    public AmaGuiaTransito getActaDepositoSelectL() {
        return actaDepositoSelectL;
    }

    public void setActaDepositoSelectL(AmaGuiaTransito actaDepositoSelectL) {
        this.actaDepositoSelectL = actaDepositoSelectL;
    }

    public List<AmaDocumento> getListDocumentos() {
        return listDocumentos;
    }

    public void setListDocumentos(List<AmaDocumento> listDocumentos) {
        this.listDocumentos = listDocumentos;
    }

    public ListDataModel<Map> getListaArmasFinal() {
        return listaArmasFinal;
    }

    public void setListaArmasFinal(ListDataModel<Map> listaArmasFinal) {
        this.listaArmasFinal = listaArmasFinal;
    }

    public List<AmaInventarioAccesorios> getListInvAcceMostrar() {
        return listInvAcceMostrar;
    }

    public void setListInvAcceMostrar(List<AmaInventarioAccesorios> listInvAcceMostrar) {
        this.listInvAcceMostrar = listInvAcceMostrar;
    }

    public String getMsjConfirmacionDevolucion() {
        return msjConfirmacionDevolucion;
    }

    public void setMsjConfirmacionDevolucion(String msjConfirmacionDevolucion) {
        this.msjConfirmacionDevolucion = msjConfirmacionDevolucion;
    }

    public List<AmaInventarioAccesorios> getLstAccesoriosActa() {
        return lstAccesoriosActa;
    }

    public void setLstAccesoriosActa(List<AmaInventarioAccesorios> lstAccesoriosActa) {
        this.lstAccesoriosActa = lstAccesoriosActa;
    }

    public TipoBaseGt getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(TipoBaseGt estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public Boolean getFlagCollapsed() {
        return flagCollapsed;
    }

    public void setFlagCollapsed(Boolean flagCollapsed) {
        this.flagCollapsed = flagCollapsed;
    }

    public String getSelectedAmaArma() {
        return selectedAmaArma;
    }

    public void setSelectedAmaArma(String selectedAmaArma) {
        this.selectedAmaArma = selectedAmaArma;
    }

    public AmaDocumento getDocumentoSelect() {
        return documentoSelect;
    }

    public void setDocumentoSelect(AmaDocumento documentoSelect) {
        this.documentoSelect = documentoSelect;
    }

    public Boolean getFlagTipoDev() {
        return flagTipoDev;
    }

    public void setFlagTipoDev(Boolean flagTipoDev) {
        this.flagTipoDev = flagTipoDev;
    }

    /**
     * @return the tipoDocSelect
     */
    public SbTipo getTipoDocSelect() {
        return tipoDocSelect;
    }

    /**
     * @param tipoDocSelect the tipoDocSelect to set
     */
    public void setTipoDocSelect(SbTipo tipoDocSelect) {
        this.tipoDocSelect = tipoDocSelect;
    }

    /**
     * @return the numeroDoc
     */
    public String getNumeroDoc() {
        return numeroDoc;
    }

    /**
     * @param numeroDoc the numeroDoc to set
     */
    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public Boolean getIsJudicializada() {
        return isJudicializada;
    }

    public void setIsJudicializada(Boolean isJudicializada) {
        this.isJudicializada = isJudicializada;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "amaDevolucionArmasConverter")
    public static class AmaDevolucionArmasControllerConverterN extends AmaDevolucionArmasControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = AmaGuiaTransito.class)
    public static class AmaDevolucionArmasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AmaDevolucionArmasController c = (AmaDevolucionArmasController) JsfUtil.obtenerBean("amaDevolucionArmasController", AmaDevolucionArmasController.class);
            return c.getAmaGuiaTransito(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AmaGuiaTransito) {
                AmaGuiaTransito o = (AmaGuiaTransito) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + AmaGuiaTransito.class.getName());
            }
        }
    }

    public Boolean getRenderDocumentoSustento() {
        return renderDocumentoSustento;
    }

    public void setRenderDocumentoSustento(Boolean renderDocumentoSustento) {
        this.renderDocumentoSustento = renderDocumentoSustento;
    }

    public Boolean mostrarDocumentosSustento() {
        Boolean render = Boolean.FALSE;

//        switch (estado) {
//            case CREARDEV:
//                render = Boolean.FALSE;
//                if (actaDepositoSelect != null) {
//                    if (actaDepositoSelect.getDireccionDestinoId() != null && actaDepositoSelect.getDireccionDestinoId().getAreaId().getCodProg().equals("TP_AREA_GAMAC")) {
//                        render = Boolean.FALSE;
//                    } else {
//                        render = Boolean.TRUE;
//                    }
//                }
//                break;
//            case EDITARDEV:
//                render = Boolean.FALSE;
//                if (actaDevolucionSelect != null) {
//                    if (actaDevolucionSelect.getDireccionOrigenId() != null && actaDevolucionSelect.getDireccionOrigenId().getAreaId().getCodProg().equals("TP_AREA_GAMAC")) {
//                        render = Boolean.FALSE;
//                    } else {
//                        render = Boolean.TRUE;
//                    }
//                }
//                break;
//        }
        return render;
    }
}
