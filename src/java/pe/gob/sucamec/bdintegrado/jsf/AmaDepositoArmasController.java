/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.bean.AmaMaestroArmasFacade;
import pe.gob.sucamec.sistemabase.beans.SbActaDefuncionFacade;
import pe.gob.sucamec.sistemabase.data.SbActaDefuncion;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.sistemabase.data.SbPerfil;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
//import pe.gob.sucamec.sistemabase.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.sistemabase.data.SbRelacionPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.UnidadMedida;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.bdintegrado.bean.AmaArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaArmaInventarioDifFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaCatalogoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaDocumentoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaGuiaMunicionesFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioAccesoriosFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioArtconexoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaModelosFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaMunicionFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade;
//import pe.gob.sucamec.sistemagamac.beans.Rma1369Facade;
import pe.gob.sucamec.rma1369.bean.Rma1369Facade;
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
import pe.gob.sucamec.bdintegrado.bean.UnidadMedidaFacade;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaArmaInventarioDif;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaDocumento;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaMuniciones;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioAccesorios;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArtconexo;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
//import pe.gob.sucamec.bdintegrado.data.AmaMunicion;
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.jsf.util.AmaGuiaTransitoGenericoController;
import pe.gob.sucamec.bdintegrado.jsf.util.ArmaRepClass;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.ParametroElementoClass;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtilTurno;
import pe.gob.sucamec.renagi.jsf.util.StringUtil;
import pe.gob.sucamec.bdintegrado.bean.ClienteFacade;
import pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoClienteFacade;
import pe.gob.sucamec.bdintegrado.bean.UsuarioFacade;
import pe.gob.sucamec.bdintegrado.data.Cliente;
import pe.gob.sucamec.bdintegrado.data.Documento;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.TipoCliente;
import pe.gob.sucamec.bdintegrado.bean.AmaTurnoActaFacade;
import pe.gob.sucamec.bdintegrado.bean.SbFeriadoFacade;
import pe.gob.sucamec.bdintegrado.bean.TurPersonaFacade;
import pe.gob.sucamec.bdintegrado.bean.TurProgramacionFacade;
import pe.gob.sucamec.bdintegrado.bean.TurTurnoFacade;
import pe.gob.sucamec.turreg.data.AmaTurnoActa;
import pe.gob.sucamec.turreg.data.TurPersona;
import pe.gob.sucamec.turreg.data.TurProgramacion;
import pe.gob.sucamec.turreg.data.TurTurno;
//import pe.gob.sucamec.turreg.ws.WsTramDoc;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.PideMigraciones;
import wspide.PideMigraciones_Service;
import pe.gob.sucamec.sel.citas.data.TurMunicion;
import pe.gob.sucamec.sel.citas.data.CitaDetalleArma;
import pe.gob.sucamec.turreg.data.TurLicenciaReg;
import pe.gob.sucamec.bdintegrado.bean.AmaDevolucionArmasController;
import pe.gob.sucamec.bdintegrado.bean.DocumentoFacade;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
import pe.gob.sucamec.sistemabase.data.SbTipo;

@Named("amaDepositoArmasController")
@SessionScoped
public class AmaDepositoArmasController implements Serializable {

    private static String DEPOSITO = "DEP", DEVOLUCION = "DEV";
    private static String VER = "VER";
    private final static String ART_ACCESORIO = "accesorio", ART_MUNICION = "municion", ART_ARMA = "arma", ART_INSUMO = "insumo", ART_OTROS = "otros";
    private final static String CREAR_ARMA = "crear", EDITAR_ARMA = "editar", COMPLETAR_ARMA = "completar";
    private static final Long L_UNO_NEG = -1L, L_DOS_NEG = -2L, L_TRES_NEG = -3L, L_CUATRO_NEG = -4L, L_CERO = 0L, L_UNO = 1L, L_DOS = 2L, L_TRES = 3L, L_CUATRO = 4L, L_CINCO = 5L, L_SEIS = 6L, L_SIETE = 7L, L_OCHO = 8L;
    private EstadoCrud estado;
    private Boolean isCoorVerificador = Boolean.FALSE;
    private Boolean isCoordinador = Boolean.FALSE;
    private Boolean isJefeZonal = Boolean.FALSE;
    private Boolean perfilConsulta;
    private Boolean flagRegInvArma;
    private Boolean flagCollapsed;
    private Boolean disabledBtVal;
    private Boolean esRectificatoria;
    private Boolean disableGuardar;
    private Boolean disableModo;
    private Boolean disableTipoDep;
    private Boolean reqDependencia1;
    private Boolean reqDependencia2;
    private Boolean existeCitaOk;
    private Boolean flagCrearConArmaInex;
    private Boolean flagCrearSinCita;
    private Boolean renderX;
    private Boolean renderPropietario;
    private Boolean existeArmaInList;
    private Boolean muestraBusqFecha;
    private Boolean isSolExpSucamec;
    private Boolean resInvArma;
    private Boolean addArmaActa;
    private Boolean isConsulta;
    private Boolean isArmaEditable;
    private Boolean selTipoArma;
//    private Boolean conOrdenIncDec;
    //Adjuntar foto del arma
    private Boolean adjuntarFoto;
    private Date fechaHoy = new Date();
    private Date fechaEmiActa;
    private Date fechaIniL;
    private Date fechaFinL;
    private Date fechaCompBus;
    private Date fechaArmaJud;
    private String tipoAlmaFisico;
    private String tipoArma;
    private String filtroL;
    private String tipoPersona;
    private String cantNumeros;
    private String selectedAmaArma;
    private String obsDeposito;
    private String accionArma;
    private String nroExpActa;
    private String tipoBusComp;
    private String filtroComp;
    private String tipoArtMostrar;
    private String tipoArtLibre;
    private String numExpGuia;
    private String msjConfirmacionDeposito;
    private String msjConfirmacionDevolucion;
    private String valorBusLic;
    private String nroRuaTemp;
    private String selectedArmaString;
    private String valorBusDisca;
    private String serieCita;
    private String nroLicCita;
    private String anaquel;
    private String dependenciaProc;
    private String dependenciaInst;
    private String docSolicitanteCita;
    private String msjRegSolicitante;
    private String msjeRegistro;
    private String msjConfirmacionDevArma;
    //Adjuntar foto del arma
    private String archivo;
    private TipoGamac modoBus;
    private TipoGamac idTipoGuiaDep;
    private TipoGamac idTipoGuia;
    private TipoGamac subTipoSelect;
    private TipoGamac subSubTipoSelect;
    private TipoGamac modoSelect;
    private TipoGamac tipoOrigenSelect;
    private TipoGamac procedenciaSelect;
    private TipoGamac tipoInterSelect;
    private TipoGamac ambienteSelect;
    private TipoGamac instOrdenaSelect;
    private TipoGamac motivoInSelect;
    private TipoGamac tipoDestinoSelect;
    private TipoGamac situacionBus;
    private TipoGamac estadoBus;
    private List<TipoGamac> listSituacionBus;
//    private List<TipoGamac> listInstOrdena;
//    private List<TipoGamac> listProcedencia;
    private List<TipoGamac> listSTGuia;
    private List<TipoGamac> listSubSTGuia;
    private List<TipoGamac> listTipoGuiaDep;
    private List<TipoGamac> listTipoGuiaDepBus;
    private List<TipoGamac> listTipoGuia;
    private List<TipoGamac> listMotivoIn;
    private List<TipoGamac> listIdSubTipoGuiaSelect;
    private List<TipoGamac> listOrigen;
    private List<TipoGamac> listDestino;
    private Long idTipoBusquedaL;
    private Long opcDepositoSelect;
    private Long idCriterioLic;
    private Long idOrigenTipoGuia;
    private Long idListaEliminar;
    private Long cantidad;
    private Long idAreaArma;
    private Long cantAccesActa;
    private SbDireccion dirSucamecBus;
    private SbDireccion selectedDestSucamec;
    private SbDireccion sbDireccionOrigen;
    private SbDireccion selectedOriSucamec;
    private SbDireccion selectedOriOtro;
    private SbDireccion selectedOriLocal;
    private SbDireccion sbDireccionDest;
    private int checkTipoDeposito;
    private AmaGuiaTransito actaDepositoSelect;
    private AmaGuiaTransito actaDepositoSelectL;
    private AmaGuiaTransito actaDevolucionSelectL;
    private AmaGuiaTransito amaGuiaSelect;
    private AmaGuiaTransito actaRectificada;
    private AmaGuiaTransito registro;
    private AmaInventarioArma invArmaDeposito;
    private AmaInventarioArma copiaInvArmaTemp;
    private AmaInventarioArma newInvArma;
    private List<AmaInventarioArma> listaInvArmasTemp;
    private List<AmaInventarioArma> listCopiaInvArmaTemp;
    private List<AmaInventarioArma> listInvArmasSeleccion;
    private List<AmaInventarioAccesorios> listInvAcceMostrar;
    private List<AmaInventarioAccesorios> listAccesArma;
    private List<AmaInventarioAccesorios> lstAccesoriosActa;
    private List<AmaInventarioAccesorios> listAccesoriosGuia;
    private List<AmaInventarioAccesorios> lstAccesorios;
    private List<AmaInventarioAccesorios> lstEliminadosAccesorios;
    private List<AmaInventarioAccesorios> lstAccesoriosActaSelect;
    private List<AmaGuiaMuniciones> listGuiaMunicion;
    private List<AmaGuiaMuniciones> listGuiaMunicionSelect;
    private ListDataModel<Map> listaArmasFinal;
    private ListDataModel<Map> listGuiasTraslado;
    private ListDataModel<Map> listActasReporte;
    private List<Map> listGuiasTrasladoSelect;
    private List<Map> listArmaLic = null;
    private List<Map> listaArmasFinalTemp;
    private List<AmaArma> listaArmasTemp;
    private Map armaDisca;
    private Map armaLic;
    private SbPersona newPersona;
    private SbPersona solicitante;
    private SbPersona repreSelect;
    private SbPersonaGt representante;
    private SbPersona solicitanteTemp;
    private SbPersona solicitanteCita;
    private SbPersona infractorSelect;
    private SbPersona propietario;
    private List<SbPersona> listInfractores;
    private List<SbPersona> listInfractoresSelect;
    private AmaDocumento documentoSelect;
    private Documento documentoCancelacionSelect;
    private AmaDocumento newDocumento;
    private Documento documentoRgInDefSelect;
    private List<ParametroElementoClass> listAccesorioMostrar;
    private TurTurno turnoDeposito;
    private TurPersona solDeposito;
    private List<TurTurno> listTurnosCita;
    private List<TurTurno> listTurnosCitaFiltro;
    private List<String> listHoraTurno;
    private SbRecibos comprobantePago;
    private List<SbRecibos> lstRecibosBusqueda;
    private Expediente expActaDeposito;
    private AmaCatalogo tpArticuloSelect;
    private AmaCatalogo tipoArticuloBus;
    private AmaCatalogo tipoArmaBus;
    private BigDecimal idUsuarioTramDoc;
    private TipoBaseGt tipoOpeSelect;
    private TipoBaseGt estadoFinal;
    private GamacAmaMunicion amaMuniSelect;
    private Double cantMuni;
    private Double pesoMuni;
    private UnidadMedida uniMedidaMuni;
    private SbUsuario usuLogueado;
    //Variable que tomará el valor del registro de inventario arma y lo mantendrá temporalmente 
    //hasta que se presione el botón guardar [Guía de Tránsito]. Tomará un id negativo para ser identificado.
    private boolean disabledEstadoSerie;
    private Boolean renderPropietario2;
    private boolean renderBtnKitPistola;
    private boolean renderBtnKitRevolver;
    private boolean renderBtnGuardar;
    private Boolean tieneResolucion;
    private Boolean isJudicializada;
    /**
     * Flag que determina si se modificará o no el tipo de Internamiento del
     * arma
     */
    private Boolean actualizarTipoInt;
    private List<Map> lstPropietario;
    private AmaInventarioArtconexo accesorio;
    private AmaInventarioArtconexo accesorioActa;
    private AmaModelos modeloAnterior;
    private int tipoMensajeValidacion;

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

    private int ultimoReg;

    private boolean flagFallec60dias;

    private Date hoy = new Date();
    private Date filtroFecha;
    private TurProgramacion programacion;
    private List<TurPersona> listPersonaCita;
    private TurPersona selectedPersonaCita;
    private List<CitaDetalleArma> lstDetalleArma; 
    private List<TurMunicion> lstDetalleMunicion;
    private CitaDetalleArma detalleArma;  
    private List<TurTurno> lstTurnosBusqueda;
    private TurTurno turnoSeleccionado;
    private String horaProgra;
    private boolean visiblePanelSeleccionCitas = false;
    private Long actaInternamientoMunicionId;

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
    private SbPersonaFacade ejbSbPersonaFacade;
    @EJB
    private SbRelacionPersonaFacade ejbSbRelacionPersonaFacade;
    @EJB
    private AmaCatalogoFacade ejbAmaCatalogoFacade;
    @EJB
    private TurProgramacionFacade ejbTurProgramacionFacade;
    @EJB
    private SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private AmaInventarioAccesoriosFacade ejbAmaInventarioAccesoriosFacade;
    @EJB
    private SbReciboRegistroFacade ejbSbReciboRegistroFacade;
    @EJB
    private AmaModelosFacade ejbAmaModelosFacade;
    @EJB
    private Rma1369Facade ejbRma1369Facade;
    @EJB
    private AmaTurnoActaFacade ejbAmaTurnoActaFacade;
    @EJB
    private SbFeriadoFacade ejbSbFeriadoFacade;
    @EJB
    private AmaDocumentoFacade ejbAmaDocumentoFacade;
    @EJB
    private AmaGuiaMunicionesFacade ejbAmaGuiaMunicionesFacade;
    @EJB
    private SbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private AmaMunicionFacade ejbAmaMunicionFacade;
    @EJB
    private UnidadMedidaFacade ejbUnidadMedidaFacade;
    @EJB
    private AmaInventarioArtconexoFacade ejbAmaInventarioArtconexoFacade;
    @EJB
    private TurTurnoFacade ejbTurTurnoFacade;
    @EJB
    private TurPersonaFacade ejbTurPersonaFacade;
    @EJB
    private ClienteFacade ejbClienteFacade;
    @EJB
    private TipoClienteFacade ejbTipoClienteFacade;
    @EJB
    private AmaArmaInventarioDifFacade ejbAmaArmaInventarioDifFacade;
    @EJB
    private UsuarioFacade usuarioTDFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;
    @EJB
    private SbActaDefuncionFacade ejbSbActaDefuncionFacade;
    @EJB
    private DocumentoFacade ejbDocumentoFacade;
    @EJB
    private AmaMaestroArmasFacade ejbAmaMaestroArmasFacade;
    @EJB
    private AmaTarjetaPropiedadFacade ejbAmaTarjetaProdiedadFacade;
    @EJB
    private SbTipoFacade ejbSbTipoFacade;

    @Inject
    private LoginController loginController;
    @Inject
    private WsTramDoc wsTramDocController;
    @Inject
    private AmaGuiaTransitoGenericoController amaGuiaTransitoGenericoController;
    @Inject
    private AmaDevolucionArmasController amaDevolucionArmasController;

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public AmaGuiaTransito getAmaGuiaTransito(Long id) {
        return ejbAmaGuiaTransitoFacade.find(id);
    }

    /**
     * FUNCIÓN QUE PREPARA LAS VARIABLES PARA MOSTRAR LA LISTA DE ACTAS DE
     * DEPÓSITO Y DEVOLUCIÓN
     *
     * @author Gino Chávez
     * @return PÁGINA
     */
    public String prepareDepositoFisico() {
        return inicializarValoresDeposito();
    }

    /**
     * FUNCIÓN QUE INICIALIZA LAS VARIABLES PROPIOS DE ACTAS DE DEPÓSITO Y
     * DEVOLUCIÓN
     *
     * @author Gino Chávez
     * @return PÁGINA
     */
    public String inicializarValoresDeposito() {
        //Para el filtro de Fecha de la busqueda de las Citas de Empadronamiento y Amnistía
        filtroFecha = hoy;
        
        String res = null;
        tipoAlmaFisico = DEPOSITO;
        actualizarVista();
//        userSession = JsfUtil.getLoggedUser().getLogin();
        List<BigDecimal> lisIds = usuarioTDFacade.obtenerIdUsuarioTD(loginController.getUsuario().getLogin());
        idUsuarioTramDoc = lisIds.isEmpty() ? null : lisIds.get(0);
//        usuLogueado = amaGuiaTransitoGenericoController.objetoUsuario(loginController.getUsuario());
        if (idUsuarioTramDoc != null) {
            obtenerCantPerfiles();
            estado = EstadoCrud.BUSCARGUIA;
            isConsulta = Boolean.FALSE;
            //tipoAlmaFisico = null;
            dirSucamecBus = null;
            registro = null;
            listGuiasTraslado = null;
            idTipoBusquedaL = null;
            listTipoGuiaDep = null;
            idTipoGuiaDep = null;
            filtroL = null;
            fechaEmiActa = null;
            newPersona = new SbPersona();
            newDocumento = new AmaDocumento();
            newInvArma = new AmaInventarioArma();
            existeArmaInList = Boolean.FALSE;
            setListCopiaInvArmaTemp(null);
            copiaInvArmaTemp = null;
            accionArma = StringUtil.VACIO;
            //setarDatosInvArma();
            res = "/aplicacion/amaDepositoArmas/BandejaDepositoDevolucion";
        } else {
            JsfUtil.mensajeError("Usted no cuenta con los permisos requeridos para éste módulo.");
        }
        return res;
    }

    /**
     * FUNCIÓN QUE VALIDA EL PERFIL DEL USUARIO LOGUEADO
     *
     * @return
     * @author Gino Chávez
     */
    public int obtenerCantPerfiles() {
        int c = 0;
        usuLogueado = amaGuiaTransitoGenericoController.objetoUsuario(loginController.getUsuario());
        List<SbPerfil> listPerfil = usuLogueado.getSbPerfilList();
        for (SbPerfil p : listPerfil) {
            switch (p.getCodProg()) {
                case "AMA_EVACOM":
//                    isEvaluador = Boolean.TRUE;
                    c += 1;
                    break;
                case "AMA_COOCOM":
                    isCoordinador = Boolean.TRUE;
                    c += 1;
                    break;
                case "AMA_GERCOM":
//                    isGerente = Boolean.TRUE;
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

    /**
     * Función que construye el formulario
     */
    public void prepararFormulario() {
        limpiarDatosDepDev();
        inicializarListasArmas();
        listIdSubTipoGuiaSelect = null;
        flagRegInvArma = Boolean.FALSE;
        idTipoGuia = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTGAMAC_ALM");
        cargarListSTGuia();
//        getListTipoGuia();
        armaLic = null;
        flagCrearConArmaInex = Boolean.FALSE;
        flagCrearSinCita = Boolean.FALSE;
        if (checkTipoDeposito > 0) {
            disableGuardar = Boolean.TRUE;
            switch (checkTipoDeposito) {
                case 1:
                    tipoArtLibre = ART_ARMA;
                    break;
                case 2:
                    tipoArtLibre = ART_ARMA;
                    disableGuardar = Boolean.FALSE;
                    break;
                case 3:
                    tipoArtLibre = ART_OTROS;
                    disableGuardar = Boolean.FALSE;
                    break;
                default:
                    break;
            }
        }
    }

    public void actualizarVista() {
        setModoBus(null);
        setFiltroL(null);
        setListGuiasTraslado(null);
        setIdTipoBusquedaL(null);
    }

    public List<SbDireccion> listaDireccionSucamec() {
        List<SbDireccion> listRes = null;
        if (perfilConsulta == null) {
            setPerfilConsulta(Boolean.FALSE);
        }
        if (!perfilConsulta) {
            listRes = ejbSbDireccionFacade.listarDireccionesPermitidasXUsuario(L_UNO, usuLogueado.getId(), usuLogueado.getAreaId().getId());
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

    public boolean isFlagFallec60dias() {
        return flagFallec60dias;
    }

    public void setFlagFallec60dias(boolean flagFallec60dias) {
        this.flagFallec60dias = flagFallec60dias;
    }

    /**
     * BUSCAR COMPROBANTE DE PAGO
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void buscarDlgComprobante() {
        try {
            SbPersona perBusqueda = null;
            String reciboXtipo="";
            /*if (propietario != null) {
                perBusqueda = propietario;
            } else if (solicitante != null) {
                perBusqueda = solicitante;
            }*/
            perBusqueda = solicitante;
            if(tipoArma!=null)
            {
             String arma=tipoArma;
             
             if(arma.equals("AC"))
             {
                 reciboXtipo="Deposito_recibo_ac";
             }else
             {
                 reciboXtipo="Deposito_recibo_al";
             }
             
             if (perBusqueda != null) {
                tipoBusComp = "nro";
                //if (tipoBusComp != null) {
                //if (!(tipoBusComp != null && ((filtroComp == null || filtroComp.isEmpty()) && fechaCompBus == null))) {
                if (!((filtroComp == null || filtroComp.isEmpty()))) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    HashMap mMap = new HashMap();
                    mMap.put("tipo", tipoBusComp);
                    mMap.put("filtro", filtroComp);
                    mMap.put("fecha", fechaCompBus);
                    //mMap.put("importe", Double.parseDouble(ejbSbParametroFacade.obtenerParametroXNombreGamac("ActaDeposito_recibo_precio").getValor()));
                    mMap.put("importe", Double.parseDouble(ejbSbParametroFacade.obtenerParametroXNombreGamac(reciboXtipo).getValor()));
                    mMap.put("codigo", Long.parseLong(JsfUtil.bundle("ActaDeposito_recibo_codigo")));
                    setLstRecibosBusqueda(ejbSbRecibosFacade.listarRecibosPorCriterios(perBusqueda, mMap));
                    if (!JsfUtil.isNullOrEmpty(lstRecibosBusqueda)) {
                        comprobantePago = lstRecibosBusqueda.get(0);
                        tipoBusComp = null;
                        filtroComp = null;
                    } else {
                        comprobantePago = null;
                        JsfUtil.mensajeAdvertencia("No se encontraron resultados con el número de secuencia ingresado.");
                    }
                } else {
                    comprobantePago = null;
                    JsfUtil.mensajeAdvertencia("Por favor escriba el criterio de búsqueda.");
                }
                //} else {
                //comprobantePago = null;
                //JsfUtil.mensajeAdvertencia("Por favor seleccione un tipo de búsqueda.");
                //}
            } else {
                JsfUtil.mensajeAdvertencia("Por favor, ingrese solicitante.");
            }
            }else
            {
                    JsfUtil.mensajeAdvertencia("Por favor, ingrese el tipo de arma!.");
            }

            
        } catch (Exception ex) {
            JsfUtil.mensajeAdvertencia("hubo un error al buscar el comprobante de pago.");
        }
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
        if (!isConsulta) {
            fechaIniL = fechaFinL = null;
        }
        if (!StringUtil.isNullOrEmpty(tipoAlmaFisico)) {
            if (!perfilConsulta && (isConsulta && (isJefeZonal || isCoorVerificador) && dirSucamecBus == null)) {
                msjValidacion = "Por favor, seleccione el almacén de SUCAMEC.";
            } else if (/* && */((idTipoBusquedaL == null && StringUtil.isNullOrEmpty(filtroL)) && fechaEmiActa == null && idTipoGuiaDep == null && dirSucamecBus == null
                    && (getFechaIniL() == null && getFechaFinL() == null))) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda1");
            } else if (idTipoBusquedaL != null && StringUtil.isNullOrEmpty(filtroL) && fechaEmiActa == null) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda3");
            } else if (idTipoBusquedaL == null && (!StringUtil.isNullOrEmpty(filtroL) || fechaEmiActa != null)) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda4");
            } else if (!isConsulta && ((idTipoBusquedaL == null && StringUtil.isNullOrEmpty(filtroL) && fechaEmiActa == null) && (idTipoGuiaDep != null))) {
                msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda9");
            } else if ((!isConsulta && (idTipoBusquedaL == null && StringUtil.isNullOrEmpty(filtroL) && fechaEmiActa == null)) && (dirSucamecBus != null)) {
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
//            String cad = StringUtil.VACIO;
//            if (DEPOSITO.equalsIgnoreCase(tipoAlmaFisico)) {
//                cad = amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO);
//            } else if (DEVOLUCION.equalsIgnoreCase(tipoAlmaFisico)) {
//                cad = amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION);
//            }
            List<Map> listGTTemporal = ejbAmaGuiaTransitoFacade.obtenerActasDepDev(tipoBusqueda, (filtroL != null ? filtroL.trim().toUpperCase() : null),
                    idTipoGuiaDep, fecha, (!isConsulta ? usuLogueado.getAreaId() : null),
                    dirSucamecBus, null, modoBus, fechaIniL, fechaFinL,
                    listSituacionBus, estadoBus, tipoArticuloBus, tipoArmaBus, DEPOSITO.equals(tipoAlmaFisico) ? "TP_GTGAMAC_ALM" : "TP_GTGAMAC_DEV", ultimoReg == 1 ? Boolean.TRUE : Boolean.FALSE);
            //Listado para reporte
            List<Map> listReport = ejbAmaGuiaTransitoFacade.obtenerActasReporte(tipoBusqueda, (filtroL != null ? filtroL.trim().toUpperCase() : null),
                    idTipoGuiaDep, fecha, (!isConsulta ? usuLogueado.getAreaId() : null), dirSucamecBus, null, modoBus, fechaIniL, fechaFinL,
                    listSituacionBus, estadoBus, tipoArticuloBus, tipoArmaBus, DEPOSITO.equals(tipoAlmaFisico) ? "TP_GTGAMAC_ALM" : "TP_GTGAMAC_DEV", ultimoReg == 1 ? Boolean.TRUE : Boolean.FALSE
            );
            //Fin
            listGuiasTraslado = new ListDataModel(listGTTemporal);
            listActasReporte = new ListDataModel(listReport);
        } else {
            listGuiasTraslado = new ListDataModel<>(new ArrayList<Map>());
            JsfUtil.mensajeAdvertencia(msjValidacion);
            return;
        }
    }

    public void buscarCitas(Boolean todos) {
        try {
            addArmaActa = Boolean.FALSE;
            String codArea = StringUtil.VACIO;
            Long idArea = L_CERO;
            if (Objects.equals(usuLogueado.getAreaId().getCodProg(), "TP_AREA_GAMAC")) {
                idArea = ejbTipoBaseFacade.tipoPorCodProg("TP_AREA_TRAM") == null ? L_CERO : ejbTipoBaseFacade.tipoPorCodProg("TP_AREA_TRAM").getId();
                codArea = "TP_AREA_TRAM";
            } else {
                codArea = usuLogueado.getAreaId().getCodProg();
                idArea = usuLogueado.getAreaId().getId();
            }
            limpiarDatosCitaProgramada();
            if (todos) {
                docSolicitanteCita = null;
                listTurnosCita = ejbTurTurnoFacade.listarTurnosConfirmadosXProgramacionYDia(null, fechaHoy, idArea, "TP_TRAM_INT", "TP_EST_CON");
                if (JsfUtil.isNullOrEmpty(listTurnosCita)) {
                    JsfUtil.mensajeAdvertencia("No existen citas confirmadas para el día de hoy.");
                }
            } else if (!StringUtil.isNullOrEmpty(docSolicitanteCita)) {
                List<TurPersona> listTemp = ejbTurPersonaFacade.buscarPersonaXNroDoc(docSolicitanteCita.trim());
                String msj = StringUtil.VACIO;
                if (!JsfUtil.isNullOrEmpty(listTemp)) {
                    solDeposito = listTemp.get(0);
                    if (!JsfUtil.isNullOrEmpty(solDeposito.getTurTurnoList())) {
                        listTurnosCita = new ArrayList<>();
                        for (TurTurno turno : solDeposito.getTurTurnoList()) {
                            msj = StringUtil.VACIO;
                            if (turno.getActivo() == JsfUtil.TRUE
                                    && JsfUtil.getFechaSinHora(fechaHoy).compareTo(JsfUtil.getFechaSinHora(turno.getFechaTurno())) == 0
                                    && Objects.equals("TP_TRAM_INT", turno.getTipoTramiteId().getCodProg())) {
                                if (Objects.equals("TP_EST_CON", turno.getEstado().getCodProg())) {
                                    if (codArea.equals(turno.getProgramacionId().getSedeId().getCodProg()) || codArea.equals(turno.getTipoSedeId().getCodProg())) {
                                        listTurnosCita.add(turno);
                                    } else {
                                        msj = "El solicitante tiene cita en otra sede.";
                                    }
                                } else {
                                    msj = "El solicitante cuenta con cita No Confirmada.";
                                }
                            }
                        }
                    }
                    if (StringUtil.VACIO.equals(msj)) {
                        if (listTurnosCita.isEmpty()) {
                            JsfUtil.mensajeAdvertencia("El solicitante no cuenta con cita para el día de hoy.");
                            RequestContext.getCurrentInstance().update("formCita");
                        } else {
                            seleccionCitaProgramada(listTurnosCita.get(0));
                            listTurnosCita = null;
                            listTurnosCitaFiltro = null;
                        }
                    } else {
                        JsfUtil.mensajeAdvertencia(msj);
                    }
                } else {
                    limpiarDatosCitaProgramada();
                    JsfUtil.mensajeAdvertencia("No se encontró cita programada con el número de documento ingresado.");
                }
            } else {
                JsfUtil.mensajeAdvertencia("Por favor, ingrese el DNI o CE de la persona que va a realizar el internamiento.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accionCrearGuiaDepDevEmpadronamiento() {
        actaInternamientoMunicionId = null;
        visiblePanelSeleccionCitas = false;
        programacion = null;
        selectedPersonaCita = null;
        listPersonaCita = null;
        //lstTurnosBusqueda = null;
        lstTurnosBusqueda = new ArrayList<TurTurno>();
        turnoSeleccionado = null;
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        lstDetalleMunicion = new ArrayList<TurMunicion>();
        
        if (listarProgramacionFechaActual().size() == 0)  {
            JsfUtil.mensajeAdvertencia("No hay Citas confirmadas para el dia de Hoy");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("dlgCitaEmpadronamientoActaForm");
            context.execute("PF('wvDlgCitaEmpadronamientoActa').show();");
        }    
    }
    
    public List<TurProgramacion> listarProgramacionFechaActual() {
        //selectedPersonaCita = null;
        //listPersonaCita = null;
        if (hoy == null) {
            JsfUtil.mensajeError("Debe seleccionar la fecha");
            return null;
        }
        SbUsuarioGt usuarioLog = ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin());
        SbTipo sede = new SbTipo();

        switch (usuarioLog.getAreaId().getCodProg()) {
            case "TP_AREA_GAMAC":
            case "TP_AREA_TRAM":
                sede = ejbSbTipoFacade.buscarTipoBaseXCodProg("TP_AREA_TRAM");
                break;
            default:
                sede = usuarioLog.getAreaId();
                break;
        }
        return ejbTurProgramacionFacade.lstTurProgramacionFechaActual(sede.getId(), "TP_PROG_EMP", hoy);
    }
    
    public void cerrarCitaEmpadronamiento() {
        RequestContext.getCurrentInstance().execute("PF('wvDlgCitaEmpadronamientoActa').hide()");
    }
    
    public String accionCrearGuiaDepDev(Long idActaDep) {
        try {
            verificarArmaId(idActaDep);
            actaDepositoSelect = ejbAmaGuiaTransitoFacade.find(idActaDep == null ? 0 : idActaDep);
            if (actaDepositoSelect == null || (isArmaInstitucionEstado(actaDepositoSelect) || amaGuiaTransitoGenericoController.isDevolucionValida(actaDepositoSelect))) {
                if (!isArmaJudicializada(actaDepositoSelect)) {
                    return iniciarDepositoDevolucion(Boolean.FALSE);
                } else {
                    openDlgConfDevolucion();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean isArmaInstitucionEstado(AmaGuiaTransito amaGuia) {
        if (amaGuia != null) {
            if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList())) {
                for (AmaInventarioArma ia : amaGuia.getAmaInventarioArmaList()) {
                    if (ia.getActivo() == JsfUtil.TRUE) {
                        if (ia.getPropietarioId() != null) {
                            SbParametro par = ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_USU_1RUCIE", L_CINCO);
                            if (ia.getPropietarioId().getRuc() == null) {
                                if ("TP_PER_JUR".equals(ia.getPropietarioId().getTipoId().getCodProg())) {
                                    JsfUtil.mensajeError("El propietario no tiene RUC.");
                                }
                                return Boolean.FALSE;
                            }else{
                                if (par != null && par.getValor().contains(ia.getPropietarioId().getRuc())) {
                                    return Boolean.TRUE;
                                }
                            }
                        } else if (!Objects.equals(ia.getModeloId().getTipoArticuloId().getCodProg(), "TP_ART_ARNEU")) {
                            //Mensaje para aquellos internamientos de armas de fuego de uso civil, que fueron generados sin el campo propietario.
                            JsfUtil.mensajeError("El arma que desea devolver, no cuenta con propietario. Por favor, comuníquese con OGTIC.");
                        }
                    }
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * FUNCION QUE INICIALIZA LAS VARIABLES DE DEPÓSITO O DEVOLUCIÓN DE ARMAS
     *
     * @author Gino Chávez
     * @param isArmaJud
     */
    public String iniciarDepositoDevolucion(Boolean isArmaJud) {
        isJudicializada = isArmaJud;
        limpiarDatosDepDev();
        inicializarListasArmas();
        listIdSubTipoGuiaSelect = null;
        flagRegInvArma = Boolean.FALSE;
//        getListTipoGuia();
        idTipoGuia = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTGAMAC_ALM");
        cargarListSTGuia();
        checkTipoDeposito = 0;
        if (isArmaJud) {
            RequestContext.getCurrentInstance().execute("PF('ConfDevViewDialog').hide()");
        }
        if (actaDepositoSelect != null) {
            amaDevolucionArmasController.prepareCrearDevolucion(actaDepositoSelect);
            return "/aplicacion/amaDevolucionArmas/CrearDevolucionArma.xhtml";
        }
        actaDepositoSelect = null;
        openFormCitaProgramada(null);
        return null;
    }

    /**
     * FUNCIÓN QUE VALIDA SI SE TRATA DE UNA ARM JUDICIALIZADA.
     *
     * @param actaDeposito
     * @return
     */
    public Boolean isArmaJudicializada(AmaGuiaTransito actaDeposito) {
        if (actaDeposito != null) {
            if (Objects.equals(actaDeposito.getTipoGuiaId().getCodProg(), "TP_GTALM_DIPJMP")) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * FUNCION PARA DIRECCIONAR A LA BANDEJA DE EXPEDIENTES
     *
     * @author Gino Chávez
     * @version 1.0
     * @return Página para redireccionar
     */
    public String prepareBandejaDepDev() {
        estado = EstadoCrud.BUSCARGUIA;
        return "/aplicacion/amaDepositoArmas/BandejaDepositoDevolucion";
    }

    public String accionEditarDepDev(Map itemGuia) {
        limpiarDatosDepDev();
        if (DEPOSITO.equalsIgnoreCase(tipoAlmaFisico)) {
            disableGuardar = Boolean.FALSE;
            actaDepositoSelect = ejbAmaGuiaTransitoFacade.find(Long.valueOf(itemGuia.get("ID").toString()));
            solicitante = actaDepositoSelect.getSolicitanteId();
            if ("TP_PER_JUR".equalsIgnoreCase(solicitante.getTipoId().getCodProg())) {
                repreSelect = actaDepositoSelect.getRepresentanteId();
            }

            idTipoGuia = procesarTipoGuia(actaDepositoSelect.getTipoGuiaId(), "EDITAR");
            cargarListSTGuia();
            inicializarOrigenDestino(Boolean.FALSE);
            //se aprovecha para nulear el modo
            modoSelect = null;
            tipoInterSelect = null;
            disableModo = Boolean.FALSE;
            //
            amaGuiaTransitoGenericoController.validarOrigenArma(tipoAlmaFisico, idTipoGuia, subTipoSelect);
            disableTipoDep = Boolean.FALSE;
            disableModo = Boolean.FALSE;
            if (subSubTipoSelect != null) {
                if ("TP_DISADU_AUTPE".equals(subSubTipoSelect.getCodProg())
                        || "TP_DISADU_NOAUT".equals(subSubTipoSelect.getCodProg())
                        || "TP_DISADU_DECAD".equals(subSubTipoSelect.getCodProg())
                        || "TP_DISADU_NOAUI".equals(subSubTipoSelect.getCodProg())) {
                    disableModo = Boolean.TRUE;
                }
            }
            modoSelect = actaDepositoSelect.getTipoIngreso();
            tipoDestinoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTORDES_ALMS");
            selectedDestSucamec = actaDepositoSelect.getDireccionDestinoId();
            tipoOrigenSelect = actaDepositoSelect.getTipoOrigen();
            if (tipoOrigenSelect != null) {
                if ("TP_GTORDES_LOC".equalsIgnoreCase(tipoOrigenSelect.getCodProg())) {
                    selectedOriOtro = null;
                    selectedOriSucamec = null;
                    sbDireccionOrigen = selectedOriLocal = actaDepositoSelect.getDireccionOrigenId();
                } else if ("TP_GTORDES_ALMS".equalsIgnoreCase(tipoOrigenSelect.getCodProg())) {
                    selectedOriOtro = null;
                    sbDireccionOrigen = selectedOriSucamec = actaDepositoSelect.getDireccionOrigenId();
                    selectedOriLocal = null;
                } else {
                    sbDireccionOrigen = selectedOriOtro = actaDepositoSelect.getDireccionOrigenId();
                    selectedOriSucamec = null;
                    selectedOriLocal = null;
                }
            }
            solicitanteTemp = propietario = amaGuiaTransitoGenericoController.obtenerTitularArma(actaDepositoSelect);
            if (solicitanteTemp != null) {
                renderPropietario2 = Boolean.FALSE;
            } else {
                renderPropietario2 = Boolean.TRUE;
            }
            reqDependencia1 = Boolean.FALSE;
            reqDependencia2 = Boolean.FALSE;
            procedenciaSelect = actaDepositoSelect.getProcedenciaId();
            if (procedenciaSelect != null) {
                reqDependencia1 = Boolean.TRUE;
            }
            dependenciaProc = actaDepositoSelect.getDependenciaProc();
            instOrdenaSelect = actaDepositoSelect.getInstitucionOrdenaId();
            if (instOrdenaSelect != null) {
                reqDependencia2 = Boolean.TRUE;
            }
            dependenciaInst = actaDepositoSelect.getDependenciaInst();
            motivoInSelect = actaDepositoSelect.getMotivoId();
            listInfractores = actaDepositoSelect.getSbPersonaList();
            obsDeposito = actaDepositoSelect.getObservacion();
            existeArmaInList = Boolean.FALSE;
            setListCopiaInvArmaTemp(null);
            copiaInvArmaTemp = null;
            accionArma = StringUtil.VACIO;
            nroExpActa = null;
            armaLic = null;
            //Comprobante
            if (solicitante != null) {
                HashMap mMap = new HashMap();
                if (DEPOSITO.equals(tipoAlmaFisico)) {
                    mMap.put("importe", Double.parseDouble(ejbSbParametroFacade.obtenerParametroXNombreGamac("ActaDeposito_recibo_precio").getValor()));
                    mMap.put("codigo", Long.parseLong(JsfUtil.bundle("ActaDeposito_recibo_codigo")));
                } else if (DEVOLUCION.equals(tipoAlmaFisico)) {
                    mMap.put("importe", Double.parseDouble(JsfUtil.bundle("ActaDevolucion_recibo_precio")));
                    mMap.put("codigo", Long.parseLong(JsfUtil.bundle("ActaDevolucion_recibo_codigo")));
                }
                comprobantePago = null;
                List<SbRecibos> listTemp = ejbSbRecibosFacade.listarRecibosPendientes(solicitante, mMap);
                if (!JsfUtil.isNullOrEmpty(listTemp)) {
                    Boolean encontrado = Boolean.FALSE;
                    for (SbRecibos rec : listTemp) {
                        if (rec.getSbReciboRegistroList() != null) {
                            for (SbReciboRegistro reg : rec.getSbReciboRegistroList()) {
                                if (reg.getNroExpediente().startsWith("PENDIENTE")) {
                                    String[] cad = reg.getNroExpediente().split("_");
                                    if (cad.length == 2) {
                                        String idActa = cad[1];
                                        if (Objects.equals(actaDepositoSelect.getId().toString(), idActa)) {
                                            comprobantePago = rec;
                                            encontrado = Boolean.TRUE;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (encontrado) {
                            break;
                        }
                    }
                }
            }
            tipoBusComp = null;
            filtroComp = null;
            //En el caso de que ya existe un expediente previo, entonces toma el número para mostrar los datos.
            if (!StringUtil.isNullOrEmpty(actaDepositoSelect.getNroExpediente())) {
                List<Expediente> listTemp = ejbExpedienteFacade.selectExpedienteXNro(actaDepositoSelect.getNroExpediente());
                if (!JsfUtil.isNullOrEmpty(listTemp)) {
                    expActaDeposito = listTemp.get(0);
                }
            }
            for (AmaDocumento documento : actaDepositoSelect.getAmaDocumentoList()) {
                documentoSelect = documento;
                break;
            }
            tpArticuloSelect = obtenerTipoDeposito(actaDepositoSelect);
            obtenerTipoArticulo(Boolean.TRUE);
            if (actaDepositoSelect.getAmaInventarioArmaList() != null) {
                for (AmaInventarioArma invArma : actaDepositoSelect.getAmaInventarioArmaList()) {
                    lstFotosTemp = null;
                    cargarFotos(invArma, Boolean.FALSE);
                    //Se almacena el arma en la lista temporal.
                    selectedAmaArma = amaGuiaTransitoGenericoController.armaString(null, String.valueOf(invArma.getId()), null);
                    break;
                }
            }
            
            //Asigna el Documento de Cancelación
            if (actaDepositoSelect.getResoluCancelacion() != null) {
                List<Documento> lista = ejbDocumentoFacade.listarDocumentosCancelacionLicencia(actaDepositoSelect.getResoluCancelacion().toString());
                if (!JsfUtil.isNullOrEmpty(lista)) {
                    for (Documento doc : lista) {
                        documentoCancelacionSelect = doc; 
                    }
                }
            }
            
            obtenerListMuniActivos(actaDepositoSelect);
            lstFotosTemp = new ArrayList<>();
            estado = EstadoCrud.EDITARDEP;
        } else if (DEVOLUCION.equalsIgnoreCase(tipoAlmaFisico)) {
            tipoAlmaFisico = DEVOLUCION;
            amaDevolucionArmasController.prepareEditarDevolucion(Long.valueOf(itemGuia.get("ID").toString()));
            return "/aplicacion/amaDevolucionArmas/EditarDevolucionArma.xhtml";
        }
        return null;
    }

    public void inicializarOrigenDestino(Boolean mostrarValidacion) {
        if (idTipoGuia != null) {
            if (mostrarValidacion) {
                adecuarListaArmas(amaGuiaTransitoGenericoController.validarOrigenArma(DEPOSITO, idTipoGuia, subTipoSelect), L_UNO);
            }
            obtenerListasTipoOrgDest();
        }
        RequestContext.getCurrentInstance().update("registroForm:datosTraslado");
        RequestContext.getCurrentInstance().update("editarForm:datosTraslado");
    }

    public void openFormCitaProgramada(Boolean flag) {
        Long idArea = L_CERO;
        if (Objects.equals(usuLogueado.getAreaId().getCodProg(), "TP_AREA_GAMAC")) {
            idArea = ejbTipoBaseFacade.tipoPorCodProg("TP_AREA_TRAM") == null ? L_CERO : ejbTipoBaseFacade.tipoPorCodProg("TP_AREA_TRAM").getId();
        } else {
            idArea = usuLogueado.getAreaId().getId();
        }
        List<TurProgramacion> listTemp = ejbTurProgramacionFacade.lstTurProgramacion(idArea, "TP_PROG_INT");
        if (!JsfUtil.isNullOrEmpty(listTemp)) {
            listHoraTurno = new ArrayList<>();
            for (TurProgramacion pro : listTemp) {
                getListHoraTurno().add(pro.getHora());
            }
        }
        setDocSolicitanteCita(null);
        limpiarDatosCitaProgramada();
        RequestContext.getCurrentInstance().execute("PF('CitaDepositoViewDialog').show()");
        RequestContext.getCurrentInstance().update("formCita");
    }

    /**
     * EVENTO PARA ABRIR VENTANA DE CONFIRMACIÓN DE DEVOLUCIÓN
     *
     * @author Gino Chávez
     */
    public void openDlgConfDevolucion() {
        msjConfirmacionDevArma = "El arma se internó por medida judicial. ¿Confirma la devolución del arma? ";
        RequestContext.getCurrentInstance().execute("PF('ConfDevViewDialog').show()");
        RequestContext.getCurrentInstance().update("formConfDev");
    }

    public void limpiarDatosCitaProgramada() {
        setOpcDepositoSelect(null);
        solDeposito = null;
        listTurnosCita = null;
        listTurnosCitaFiltro = null;
        turnoDeposito = null;
        setMsjRegSolicitante(null);
    }

    /**
     * Limpiar valores del arma
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void reiniciarValores() {
        accesorio = null;
        cantidad = null;
        modeloAnterior = null;
        nroRuaTemp = null;
        tipoMensajeValidacion = 0;
        if (!existeArmaInList) {
            selectedArmaString = null;
        }
        setRenderPropietario(Boolean.TRUE);
        setRenderBtnGuardar(true);
        setRenderBtnKitPistola(false);
        setRenderBtnKitRevolver(false);
        setDisabledEstadoSerie(false);
        lstEliminadosAccesorios = new ArrayList();
        lstPropietario = new ArrayList();
        lstAccesorios = new ArrayList();
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

    public void inicializarDepositoArma(AmaTurnoActa turnoActa) {
        String codProg = "TP_GTGAMAC_ALM";
        disableGuardar = Boolean.TRUE;
        existeArmaInList = Boolean.FALSE;
        flagCrearSinCita = Boolean.FALSE;
        flagCrearConArmaInex = Boolean.FALSE;
        tipoArtLibre = null;
        setListCopiaInvArmaTemp(null);
        copiaInvArmaTemp = null;
        accionArma = StringUtil.VACIO;
        expActaDeposito = null;
        nroExpActa = null;
        comprobantePago = null;
        tipoBusComp = null;
        filtroComp = null;
        disableTipoDep = Boolean.FALSE;
        disableModo = Boolean.FALSE;
        estado = EstadoCrud.CREARDEP;
        renderX = true;
        tipoDestinoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTORDES_ALMS");
        tpArticuloSelect = null;
        tipoArtMostrar = StringUtil.VACIO;
        lstFotos = new ArrayList();
        lstFotosTemp = new ArrayList<>();
        armaLic = null;
        idCriterioLic = null;
        valorBusLic = null;
        existeCitaOk = Boolean.FALSE;
        idTipoGuia = ejbTipoGamacFacade.buscarTipoGamacXCodProg(codProg);
        inicializarOrigenDestino(Boolean.FALSE);
        RequestContext.getCurrentInstance().execute("PF('CitaDepositoViewDialog').hide()");
        if (turnoActa != null) {
            idCriterioLic = L_UNO;
            valorBusLic = String.valueOf(turnoActa.getNroLic());
            checkTipoDeposito = 1;
            obtenerDatosLicencia(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
            limpiarDatosCitaProgramada();
        }
    }

    /**
     * BUSCAR PROPIETARIO DE ARMA CON SERIE INGRESADA
     *
     * @author Gino Chávez
     * @version 1.0
     * @param invArma
     * @return
     */
    public Boolean validarSerieModelo(AmaInventarioArma invArma) {
        if (!Objects.equals(accionArma, EDITAR_ARMA)) {
            if (invArma != null && !StringUtil.isNullOrEmpty(invArma.getSerie())) {
                if (invArma.getModeloId() != null) {
                    int cant = ejbAmaInventarioArmaFacade.buscarModeloInventario(invArma.getModeloId().getId(), (invArma.getSerie() == null ? StringUtil.VACIO : invArma.getSerie().toUpperCase().trim()));
                    if (cant > 0) {
                        JsfUtil.mensajeError(JsfUtil.bundle("MensajeExisteArmaMismaSerie"));
                        return Boolean.FALSE;
                    }
                } else {
                    invArma.setSerie(null);
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeNecesitaIngresoArma"));
                }
            }
        }
        if (armaLic == null && !Objects.equals(subTipoSelect == null ? StringUtil.VACIO : subTipoSelect.getCodProg(), "TP_GTALM_DIPJMP")) {
            if (!amaGuiaTransitoGenericoController.verificarSerieEnDisca(invArma == null ? null : invArma.getSerie())) {
                if (newInvArma != null) {
                    newInvArma.setSerie(null);
                }
                JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorArmaLicCancelada"));
            }
        }
        return Boolean.TRUE;
    }

    /**
     * FUNCION QUE ESTABLECE EL FLAG PARA HACER POSIBLE LA EDICION DE LA SERIE
     * AL MOMENTO DE EDITAR EL ARMA.
     *
     * @return
     */
    public Boolean validarEditarSerie() {
        if (newInvArma != null
                && newInvArma.getId() != null
                && newInvArma.getId() > 0
                && newInvArma.getLicenciaDiscaId() != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void validarExisteInvArma() {
        accionArma = StringUtil.VACIO;
        newInvArma = new AmaInventarioArma();
        //Valida si el arma seleccionada existe ya en la lista temporal.
        String selectArmaTemp = buscarEnListTemporal(Long.parseLong(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "0", L_UNO)));
        selectedAmaArma = StringUtil.VACIO.equals(selectArmaTemp) ? selectedAmaArma : selectArmaTemp;

        if (!existeArmaInList) {//Si el codigo es nulo, quiere decir que es un registro de la tabla AmaArma
            AmaInventarioArma invArma = ejbAmaInventarioArmaFacade.obtenerInvArmaPorModeloSerie(Long.parseLong(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "9", L_UNO)), (amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "6", L_UNO) == null ? StringUtil.VACIO : amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "6", L_UNO)));
            if (invArma != null) {
                selectedAmaArma = amaGuiaTransitoGenericoController.armaString(null, String.valueOf(invArma.getId()), null);
                newInvArma = invArma;
            } else {
                //Este bloque es para todos los tipos de Actas cuyas armas a buscar provienen de AmaArma.
                AmaArma amaArma = ejbAmaArmaFacade.find(Long.parseLong(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "0", L_UNO)));
                Map modeloMap = ejbAmaModelosFacade.obtenerArmaMap(amaArma.getModeloId());
                newInvArma.setSerie(amaArma.getSerie());
                newInvArma.setNroRua(amaArma.getNroRua());
                newInvArma.setModeloId(amaArma.getModeloId());
                newInvArma.setEstadofuncionalId(amaArma.getEstadoId());
                if (amaArma.getPesoInicial() != null) {
                    newInvArma.setPeso(BigDecimal.valueOf(amaArma.getPesoInicial()));
                }
                if (!JsfUtil.isNullOrEmpty(amaArma.getAmaTarjetaPropiedadList())) {
                    for (AmaTarjetaPropiedad tarj : amaArma.getAmaTarjetaPropiedadList()) {
                        propietario = tarj.getPersonaCompradorId();
                        break;
                    }
                }
                newInvArma.setPropietarioId(propietario);
                selectedArmaString = amaGuiaTransitoGenericoController.invArmaString(modeloMap);
                accionArma = COMPLETAR_ARMA;
                if (selectedDestSucamec != null) {
                    newInvArma.setAlmacenSucamecId(selectedDestSucamec);
                    openCrearInventArma(accionArma, null);
                    return;
                } else {
                    RequestContext.getCurrentInstance().execute("PF('IngresoDirSucViewDialog').show()");
                    RequestContext.getCurrentInstance().update("formSeleccionDir");
                }
                //Fin del bloque
            }
        } else if (copiaInvArmaTemp != null && Long.parseLong(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "0", L_UNO)) < L_CERO) {
            newInvArma = copiaInvArmaTemp;
        } else {
            //Si el arma se encuentra registrado en la tabla amainvarma, entonces se obtiene el arma por id.
            newInvArma = ejbAmaInventarioArmaFacade.find(Long.parseLong(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "0", L_UNO)));
        }
        if (existeArmaInList) {
            String strId = amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "9", L_UNO);
            if (!StringUtil.isNullOrEmpty(strId)) {
                Long idModelo = Long.parseLong(strId);
                Map modeloMap = ejbAmaModelosFacade.obtenerArmaMap(ejbAmaModelosFacade.find(idModelo));
                selectedArmaString = amaGuiaTransitoGenericoController.invArmaString(modeloMap);
                accionArma = EDITAR_ARMA;
                openCrearInventArma(accionArma, null);
            }
        }
    }

    public void actualizarRequiredDep1() {
        reqDependencia1 = Boolean.FALSE;
        if (procedenciaSelect != null) {
            reqDependencia1 = Boolean.TRUE;
        }
        RequestContext.getCurrentInstance().update("registroForm:idDependencia1");
        RequestContext.getCurrentInstance().update("editarForm:idDependencia1");
    }

    public void actualizarRequiredDep2() {
        reqDependencia2 = Boolean.FALSE;
        if (instOrdenaSelect != null) {
            reqDependencia2 = Boolean.TRUE;
        }
        RequestContext.getCurrentInstance().update("registroForm:idDependencia2");
        RequestContext.getCurrentInstance().update("editarForm:idDependencia2");
    }

    public void accionRectificarActa(Long idActa) {
        limpiarDatosDepDev();
        if (idActa != null) {
            setEsRectificatoria(Boolean.FALSE);
            AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.find(idActa);
            if (amaGuia != null) {
                setearDatosActaRectificar(amaGuia, amaGuiaTransitoGenericoController.copiarAnularGuia(Boolean.TRUE, DEPOSITO, armaLic, loginController.getUsuario(), amaGuia));
                //No mover la posición de esta línea
                setEsRectificatoria(Boolean.TRUE);
                //Fin No mover
                setActaRectificada(amaGuia);
                ejbAmaGuiaTransitoFacade.edit(amaGuia);
//                        RequestContext.getCurrentInstance().update("editarForm");
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
            int contNoRect = 0;
            for (Map acta : listGuiasTrasladoSelect) {
                if (acta != null) {
                    if (acta.get("ID") != null) {
                        if (JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora((Date) acta.get("FECHEMI"))) == 0) {
                            AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.find(acta.get("ID") == null ? L_CERO : Long.valueOf(acta.get("ID").toString()));
                            if (amaGuia != null) {
                                amaGuia.setEstadoId(ejbTipoBaseFacade.tipoPorCodProg(codEstado));
                                ejbAmaGuiaTransitoFacade.edit(amaGuia);
                                //Syso("RECTIFICADO: "+JsfUtil.getFechaSinHora(new Date())+" | "+JsfUtil.getFechaSinHora((Date) acta.get("FECHEMI"))+" | "+(Date) acta.get("FECHEMI"));
                            }
                        } else {
                            contNoRect++;
                        }
                    }
                }
            }
            if (contNoRect > 0) {
                JsfUtil.mensajeAdvertencia("Sólo podrá rectificar un acta el mismo día en que fue emitida.");
            }
            buscarActas();
//            RequestContext.getCurrentInstance().update("listaActasArmaForm:listaActas");
        } else {
            JsfUtil.mensajeAdvertencia("Seleccione el Acta a ser rectificada.");
        }
    }

    public void setearDatosActaRectificar(AmaGuiaTransito amaGuiaOrig, AmaGuiaTransito amaGuiaNew) {
        actaDepositoSelect = amaGuiaNew;
        actaDepositoSelect.setTipoRegistroId(ejbTipoBaseFacade.tipoPorCodProg("TP_REGIST_REC"));
        actaDepositoSelect.setTipoOperacionId(amaGuiaOrig.getTipoOperacionId());
        actaDepositoSelect.setGuiaReferenciadaId(amaGuiaOrig);
        if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(actaDepositoSelect.getTipoGuiaId().getId()))) {
            setDisableGuardar(Boolean.FALSE);
            solicitante = actaDepositoSelect.getSolicitanteId();
            if ("TP_PER_JUR".equalsIgnoreCase(solicitante.getTipoId().getCodProg())) {
                repreSelect = actaDepositoSelect.getRepresentanteId();
            }

            idTipoGuia = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTGAMAC_ALM");
            cargarListSTGuia();
            idTipoGuia = procesarTipoGuia(actaDepositoSelect.getTipoGuiaId(), "EDITAR");
            //se aprovecha para nulear el modo
            modoSelect = null;
            tipoInterSelect = null;
            disableModo = Boolean.FALSE;
            //
            amaGuiaTransitoGenericoController.validarOrigenArma(DEPOSITO, idTipoGuia, subTipoSelect);
            setDisableTipoDep(Boolean.FALSE);
            setDisableModo(Boolean.FALSE);
            if (subSubTipoSelect != null) {
                if ("TP_DISADU_AUTPE".equals(subSubTipoSelect.getCodProg())
                        || "TP_DISADU_NOAUT".equals(subSubTipoSelect.getCodProg())
                        || "TP_DISADU_DECAD".equals(subSubTipoSelect.getCodProg())
                        || "TP_DISADU_NOAUI".equals(subSubTipoSelect.getCodProg())) {
                    setDisableModo(Boolean.TRUE);
                }
            }
            setModoSelect(actaDepositoSelect.getTipoIngreso());
            setTipoDestinoSelect(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTORDES_ALMS"));
            setSelectedDestSucamec(actaDepositoSelect.getDireccionDestinoId());
            setTipoOrigenSelect(actaDepositoSelect.getTipoOrigen());
            if (tipoOrigenSelect != null) {
                if ("TP_GTORDES_ALMS".equalsIgnoreCase(tipoOrigenSelect.getCodProg())) {
                    sbDireccionOrigen = selectedOriSucamec = actaDepositoSelect.getDireccionOrigenId();
                } else {
                    sbDireccionOrigen = selectedOriOtro = actaDepositoSelect.getDireccionOrigenId();
                }
            }
            setSolicitanteTemp(propietario = amaGuiaTransitoGenericoController.obtenerTitularArma(actaDepositoSelect));
            if (getSolicitanteTemp() != null) {
                renderPropietario2 = Boolean.FALSE;
            } else {
                renderPropietario2 = Boolean.TRUE;
            }
            setReqDependencia1(Boolean.FALSE);
            setReqDependencia2(Boolean.FALSE);
            procedenciaSelect = actaDepositoSelect.getProcedenciaId();
            if (procedenciaSelect != null) {
                setReqDependencia1(Boolean.TRUE);
            }
            dependenciaProc = actaDepositoSelect.getDependenciaProc();
            instOrdenaSelect = actaDepositoSelect.getInstitucionOrdenaId();
            if (instOrdenaSelect != null) {
                setReqDependencia2(Boolean.TRUE);
            }
            dependenciaInst = actaDepositoSelect.getDependenciaInst();
            motivoInSelect = actaDepositoSelect.getMotivoId();
            listInfractores = actaDepositoSelect.getSbPersonaList();
            obsDeposito = actaDepositoSelect.getObservacion();
            accionArma = StringUtil.VACIO;
            nroExpActa = null;
            armaLic = null;
            //Comprobante
            if (solicitante != null) {
                HashMap mMap = new HashMap();
                mMap.put("importe", Double.parseDouble(ejbSbParametroFacade.obtenerParametroXNombreGamac("ActaDeposito_recibo_precio").getValor()));
                mMap.put("codigo", Long.parseLong(JsfUtil.bundle("ActaDeposito_recibo_codigo")));
                comprobantePago = null;
                List<SbRecibos> listTemp = ejbSbRecibosFacade.listarRecibosPendientes(solicitante, mMap);
                if (!JsfUtil.isNullOrEmpty(listTemp)) {
                    Boolean encontrado = Boolean.FALSE;
                    for (SbRecibos rec : listTemp) {
                        if (rec.getSbReciboRegistroList() != null) {
                            for (SbReciboRegistro reg : rec.getSbReciboRegistroList()) {
                                if (reg.getNroExpediente().startsWith("PENDIENTE")) {
                                    String[] cad = reg.getNroExpediente().split("_");
                                    if (cad.length == 2) {
                                        String idActa = cad[1];
                                        if (Objects.equals(actaDepositoSelect.getId().toString(), idActa)) {
                                            comprobantePago = rec;
                                            encontrado = Boolean.TRUE;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (encontrado) {
                            break;
                        }
                    }
                }
            }
            tipoBusComp = null;
            filtroComp = null;
            //En el caso de que ya existe un expediente previo, entonces toma el número para mostrar los datos.
            if (actaDepositoSelect.getNroExpediente() != null && !StringUtil.VACIO.equals(actaDepositoSelect.getNroExpediente().trim())) {
                List<Expediente> listTemp = ejbExpedienteFacade.selectExpedienteXNro(actaDepositoSelect.getNroExpediente());
                if (!JsfUtil.isNullOrEmpty(listTemp)) {
                    expActaDeposito = listTemp.get(0);
                }
            }
            if (actaDepositoSelect.getAmaDocumentoList() != null) {
                for (AmaDocumento documento : actaDepositoSelect.getAmaDocumentoList()) {
                    if (documento.getActivo() == JsfUtil.TRUE) {
                        documentoSelect = documento;
                    }
                    break;
                }
            }
            tpArticuloSelect = obtenerTipoDeposito(actaDepositoSelect);
            obtenerTipoArticulo(Boolean.FALSE);
            if (actaDepositoSelect.getAmaInventarioArmaList() != null) {
//                AmaArma arma = null;
                for (AmaInventarioArma invArma : actaDepositoSelect.getAmaInventarioArmaList()) {
                    lstFotosTemp = null;
                    cargarFotos(invArma, Boolean.TRUE);
                    //Se almacena el arma en la lista temporal.
                    if (invArma.getActivo() == JsfUtil.TRUE) {
                        selectedAmaArma = amaGuiaTransitoGenericoController.armaStringTemp(invArma);
                    }
                    //Validación necesaria para que los datos del inventario sean modificables o no.
                    //Si ya cuentan con tarjeta, no se podrán modificar algunos campos, propios de ama_arma.
//                    arma = ejbAmaArmaFacade.obtenerArmaPorModeloSerie(null, invArma.getSerie(), invArma.getNroRua());
                    if (invArma.getArmaId() != null) {
                        isArmaEditable = Boolean.FALSE;
                    }
                    //Se muestra una advertencia si el modelo del arma difiere del modelo del inventario_arma 
                    if (!Objects.equals(invArma.getModeloId(), invArma.getArmaId() == null ? null : invArma.getArmaId().getModeloId())) {
                        JsfUtil.mensajeAdvertencia("El modelo del arma es diferente al modelo registrado al emitir la tarjeta de propiedad.");
                    }
                    break;
                }
            }
            obtenerListMuniActivos(actaDepositoSelect);
            lstFotosTemp = new ArrayList<>();
            estado = EstadoCrud.CREARDEP;
        }
    }

    public Boolean setearSelectedAmaArma(String serie, String rua, String docPropietario) {
        //Si el arma se encuentra en amaArma entonces se crea un registro temporal de inventario arma
        try {
            AmaInventarioArma invArma = null;
            if (serie != null) {
                if (rua != null) {
                    AmaArma ar = ejbAmaArmaFacade.buscaArmasXSerieRUA(serie, rua);
                    if (ar != null) {
                        invArma = ejbAmaInventarioArmaFacade.obtenerInvArmaPorModeloSerie(ar.getModeloId().getId(), ar.getSerie());
                        if (invArma == null) {
                            invArma = new AmaInventarioArma();
                            invArma.setId(JsfUtil.tempIdN());
                            invArma.setCodigo(L_CERO);
                            invArma.setSerie(ar.getSerie());
                            invArma.setNroRua(ar.getNroRua());
                            invArma.setModeloId(ar.getModeloId());
                            invArma.setArmaId(ar);
//                            if (ar.getDireccionSucamecId() != null) {
                            invArma.setAlmacenSucamecId(ar.getDireccionSucamecId());
//                            } 
//                            else {
//                                List<SbDireccion> listDir = ejbSbDireccionFacade.listarDireccionesXRegularizar(L_UNO);
//                                if (!JsfUtil.isNullOrEmpty(listDir)) {
//                                    invArma.setAlmacenSucamecId((SbDireccion) listDir.get(0));
//                                } else {
//                                    return Boolean.TRUE;
//                                }
//                            }
                            invArma.setEstadofuncionalId(ar.getEstadoId());
                            //El valor seteado en el campo estado conservación no pertenece a los tipados correspondientes al arma. Es un valor temporal.
                            SbPersona prop = null;
                            for (AmaTarjetaPropiedad tar : ar.getAmaTarjetaPropiedadList()) {
                                if (tar.getActivo() == JsfUtil.TRUE) {
                                    prop = tar.getPersonaCompradorId();
                                }
                            }
                            if (prop != null) {
                                invArma.setPropietarioId(prop);
                                setRenderPropietario(Boolean.FALSE);
                            }
                            invArma.setAmaInventarioAccesoriosList(new ArrayList());
                            invArma.setAmbienteId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_AMB_CJUD"));
//                        invArma.setSituacionId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_SITU_INT"));
                            invArma.setCondicionAlmacen(JsfUtil.FALSE);
                            existeArmaInList = Boolean.FALSE;
                            lstFotos = new ArrayList();
                            setDisabledBtnFoto(Boolean.FALSE);
                            invArma.setCierreInventario(JsfUtil.TRUE);
                            invArma.setActivo(JsfUtil.TRUE);
                            invArma.setActual(JsfUtil.FALSE);
                        } else //Si ya se encontró un arma en inventario_arma, entonces se evalúa el nro de rua para setarlo en caso no se encuentre actualizado.
                         if (invArma.getNroRua() == null) {
                                invArma.setNroRua(ar.getNroRua());
                            }
                    }
                } else {
                    invArma = null;
                    SbPersona prop = ejbSbPersonaFacade.selectPersonaxRucNumDoc(docPropietario);
                    if (prop != null) {
                        invArma = ejbAmaInventarioArmaFacade.obtenerArmasPorSeriePropietario(serie, prop.getId());
                    }
                    if (invArma == null) {
                        invArma = new AmaInventarioArma();
                        invArma.setId(JsfUtil.tempIdN());
                        invArma.setCodigo(L_CERO);
                        invArma.setSerie(serie);
                        invArma.setNroRua(rua);
                        //Si el arma no sen encuentra en bdintegrado, entonces se busca  a la persona por documento para ser seteada en los datos de propietario del arma.
                        prop = null;
                        List<SbPersona> listPer = ejbSbPersonaFacade.buscarPersonaXNumDoc(docPropietario);
                        if (!JsfUtil.isNullOrEmpty(listPer)) {
                            prop = listPer.get(0);
                        }
                        invArma.setPropietarioId(prop);
                        invArma.setAmaInventarioAccesoriosList(new ArrayList());
                        invArma.setAmbienteId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_AMB_CJUD"));
                        invArma.setCondicionAlmacen(JsfUtil.FALSE);
                        existeArmaInList = Boolean.FALSE;
                        lstFotos = new ArrayList();
                        setDisabledBtnFoto(Boolean.FALSE);
                        invArma.setCierreInventario(JsfUtil.TRUE);
                        invArma.setActivo(JsfUtil.TRUE);
                        invArma.setActual(JsfUtil.FALSE);
                    }
                }
            }
            if (invArma != null) {
                //Bloque para reportar al usuario que existe acta de deposito creada con el arma encontrada.
                if (!JsfUtil.isNullOrEmpty(invArma.getAmaGuiaTransitoList())) {
                    for (AmaGuiaTransito guiaBd : invArma.getAmaGuiaTransitoList()) {
                        if (guiaBd.getActivo() == JsfUtil.TRUE) {
                            String cad = amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO);
                            if (cad != null && cad.contains(String.valueOf(guiaBd.getTipoGuiaId().getId()))) {
                                if (Objects.equals(guiaBd.getEstadoId().getCodProg(), "TP_GTGAMAC_CRE")) {
                                    JsfUtil.mensajeAdvertencia("Ya existe un acta de Internamiento del arma con estado " + StringUtils.capitalize(guiaBd.getEstadoId().getNombre().toLowerCase()) + ". El usuario responsable: " + guiaBd.getUsuarioId().getLogin());
                                    return Boolean.TRUE;
                                }
                            }
                        }
                    }
                }
                //Fin
                if (armaLic.get("NRO_LIC") != null && armaLic.get("SISTEMA").toString().equals("DISCA")) {//&& amaGuiaTransitoGenericoController.setearNroLicDisca(invArma.getSerie(), armaLic.get("NRO_LIC").toString())) {
                    invArma.setLicenciaDiscaId(Long.valueOf(armaLic.get("NRO_LIC").toString()));
                }
                if (armaLic.get("ID_TARJETA") != null) {
                    invArma.setTarjetaPropiedadId(ejbAmaTarjetaProdiedadFacade.find(Long.valueOf(armaLic.get("ID_TARJETA").toString())));
                }
                if (armaLic.get("ID_DISCA") != null) {
                    invArma.setDiscaFoxId(ejbAmaMaestroArmasFacade.find(Long.valueOf(armaLic.get("ID_DISCA").toString())));
                }
                amaGuiaTransitoGenericoController.llenarListArmasTemp(Boolean.TRUE, armaLic, invArma, Boolean.FALSE);
                selectedAmaArma = buscarEnListTemporal(invArma.getId());
            }
        } catch (Exception e) {
            JsfUtil.mensajeError("Error: Ha ocurrido un error inesperado.");
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public void setearModo() {
        if (subSubTipoSelect != null) {
            if ("TP_DISADU_AUTPE".equals(subSubTipoSelect.getCodProg())
                    || "TP_DISADU_NOAUT".equals(subSubTipoSelect.getCodProg())) {
                modoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ING_TEM");
                tipoInterSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_TEM");
                disableModo = Boolean.TRUE;
            } else if ("TP_DISADU_DECAD".equals(subSubTipoSelect.getCodProg())
                    || "TP_DISADU_NOAUI".equals(subSubTipoSelect.getCodProg())) {
                modoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ING_DEF");
                tipoInterSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_DEF");
                disableModo = Boolean.TRUE;
            } else {
                modoSelect = null;
                tipoInterSelect = null;
                disableModo = Boolean.FALSE;
            }
        }
    }

    public String buscarEnListTemporal(Long idArma) {
        String selectedArma = StringUtil.VACIO;
        existeArmaInList = Boolean.FALSE;
        if (!JsfUtil.isNullOrEmpty(listCopiaInvArmaTemp)) {
            copiaInvArmaTemp = null;
            for (AmaInventarioArma amaInv : getListCopiaInvArmaTemp()) {
                if (Objects.equals(idArma, amaInv.getId())) {
                    //El arma temporal toma el valor del arma seleccionada.
                    copiaInvArmaTemp = amaInv;
                    selectedArma = amaGuiaTransitoGenericoController.obtenerArmaString(copiaInvArmaTemp);
                    existeArmaInList = Boolean.TRUE;
                    break;
                }
            }
        }
        return selectedArma;
    }

    public void obtenerTipoArticulo(Boolean limpiarCopia) {
        if (tpArticuloSelect != null) {
            listGuiaMunicion = null;
            selectedAmaArma = null;
            if (limpiarCopia) {
                copiaInvArmaTemp = null;
            }
            tipoArtMostrar = StringUtil.VACIO;
            if ("TP_ART_MUNI".equals(tpArticuloSelect.getCodProg())) {
                tipoArtMostrar = ART_MUNICION;
            } else if ("TP_ART_ACCE".equals(tpArticuloSelect.getCodProg())) {
                tipoArtMostrar = ART_ACCESORIO;
            } else if ("TP_ART_ARMA".equals(tpArticuloSelect.getCodProg())) {
                tipoArtMostrar = ART_ARMA;
            } else {
                tipoArtMostrar = ART_OTROS;
            }
        }
    }

    public void obtenerDatosArmaDisca() {
        armaDisca = null;
        if (idTipoGuia != null && idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_REI")) {
            if (solicitante != null) {
                if (valorBusDisca != null && !StringUtil.VACIO.equals(valorBusDisca)) {
                    //List<Map> listTemp = ejbRma1369Facade.buscarArmaRMA(valorBusDisca, null, null);//(solicitante.getRuc() == null ? solicitante.getNumDoc() : solicitante.getRuc())
                    List<Map> listTemp = ejbAmaGuiaTransitoFacade.buscarArmaRenagiMigra(valorBusDisca, null, null);
                    if (!JsfUtil.isNullOrEmpty(listTemp)) {
                        for (Map item : listTemp) {
                            if (Objects.equals(item.get("DOC_PROPIETARIO"), (solicitante.getRuc() == null ? solicitante.getNumDoc() : solicitante.getRuc()))) {
                                armaDisca = item;
                            }
                        }
                    }
                    if (armaDisca == null) {
                        JsfUtil.mensajeAdvertencia("No se encontraron resultados con el número de serie ingresado.");
                        return;
                    }
                    valorBusDisca = null;
                }
            } else {
                JsfUtil.mensajeAdvertencia("Para poder realizar la búsqueda es necesario ingresar el solicitante.");
            }
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

    public void obtenerListasTipoOrgDest() {
        List<TipoGamac> listO = new ArrayList<>();
        List<TipoGamac> listD = new ArrayList<>();
        listOrigen = new ArrayList<>();
        listDestino = new ArrayList<>();
        switch (idTipoGuia.getCodProg()) {
            case "TP_GTGAMAC_REC":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_EXH":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_POL":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));

                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_POL"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_IMP":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMA"));
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_PUER"));

                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_ALM":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_POL"));

                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                break;
            case "TP_GTGAMAC_DEV":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));

                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_POL"));
                break;
            case "TP_GTGAMAC_DON":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_DON"));
                break;
            case "TP_GTGAMAC_AGE":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_SUC":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));

                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                break;
            case "TP_GTGAMAC_COL":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_MUN":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMA"));
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_PUER"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_REI":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_SAL":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMA"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_PUER"));
                subTipoSelect = null;
                break;
            case "TP_GTGAMAC_DDV":
                listO.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_ALMS"));
                listD.add(amaGuiaTransitoGenericoController.tipoGamac("TP_GTORDES_LOC"));
                subTipoSelect = null;
                break;
            default:
                break;
        }
        setListOrigen(listO);
        setListDestino(listD);
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
                    item.put("propietario", amaInvArma.getPropietarioId() == null ? StringUtil.VACIO : amaGuiaTransitoGenericoController.obtenerDescPersona(amaInvArma.getPropietarioId()));//indice 8
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
        listaArmasFinal = new ListDataModel(listaArmasFinalTemp);
    }

    /**
     * METODO PARA GENERAR EL PDF DE LA SOLICITUD DE RECOJO
     *
     * @param idActa
     * @param itemSelectExp
     */
    public void imprimirCodigoActa(Long idActa) {
        try {
            if (idActa != null) {
                setAmaGuiaSelect(ejbAmaGuiaTransitoFacade.find(idActa));
                RequestContext.getCurrentInstance().execute("PF('ImpBarcodeViewDialog').show()");
                RequestContext.getCurrentInstance().update("formImpBarcode");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Boolean habilitarCompleteArma() {
        Boolean res = Boolean.TRUE;
        if (idTipoGuia != null) {
            if ("TP_GTGAMAC_DON".equalsIgnoreCase(idTipoGuia.getCodProg())
                    || "TP_GTGAMAC_SUC".equalsIgnoreCase(idTipoGuia.getCodProg())
                    || "TP_GTGAMAC_ALM".equalsIgnoreCase(idTipoGuia.getCodProg())
                    || "TP_GTGAMAC_DEV".equalsIgnoreCase(idTipoGuia.getCodProg())) {
                if (subTipoSelect != null) {
                    res = Boolean.FALSE;
                }
            } else {
                res = Boolean.FALSE;
            }
        }
        return res;
    }

    public boolean habilitaPropietario() {
        boolean render = true;

        switch (checkTipoDeposito) {
            case 1:
                if (!renderX) {
                    render = false;
                }
                break;
            case 2:
                render = false;
                break;
        }

        return render;
    }

    public void limpiarValorBusLic() {
        valorBusLic = null;
    }

    public void limpiarDatosDepDev() {
        solicitante = null;
        repreSelect = null;
        propietario = null;
        obsDeposito = null;
        renderPropietario2 = Boolean.TRUE;
        listSTGuia = null;
        listSubSTGuia = null;
        subTipoSelect = null;
        subSubTipoSelect = null;
        modoSelect = null;
        tipoInterSelect = null;
        procedenciaSelect = null;
        instOrdenaSelect = null;
        dependenciaProc = null;
        dependenciaInst = null;
        motivoInSelect = null;
        infractorSelect = null;
        listInfractores = null;
        listInfractoresSelect = null;
        documentoSelect = null;
        documentoCancelacionSelect = null;
        selectedAmaArma = null;
        listInvAcceMostrar = null;
        invArmaDeposito = null;
        listAccesorioMostrar = new ArrayList<>();
        //Municiones
        listGuiaMunicion = null;
        listGuiaMunicionSelect = null;
        lstAccesoriosActa = null;
        lstAccesoriosActaSelect = null;
        lstAccesorios = null;
        //
        selectedDestSucamec = null;
        tpArticuloSelect = null;

        esRectificatoria = Boolean.FALSE;
        actaRectificada = null;
        tipoArtMostrar = StringUtil.VACIO;
        disableTipoDep = Boolean.FALSE;
        listAccesoriosGuia = null;
        isArmaEditable = Boolean.TRUE;
        actualizarTipoInt = Boolean.TRUE;
        if (DEPOSITO.equals(tipoAlmaFisico)) {
            RequestContext.getCurrentInstance().update("registroForm");
            RequestContext.getCurrentInstance().update("editarForm");
        } else if (DEVOLUCION.equals(tipoAlmaFisico)) {
            RequestContext.getCurrentInstance().update("registroDevForm");
            RequestContext.getCurrentInstance().update("editarDevForm");
        }
        setFlagFallec60dias(false);
    }

    /**
     * LIMPAIR ACCESORIOS DE KIT DE LA TABLA
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void limpiarAccesoriosKit() {
        List<AmaInventarioAccesorios> lstTemporal = new ArrayList();
        for (AmaInventarioAccesorios acce : lstAccesorios) {
            lstTemporal.add(acce);
        }
        for (AmaInventarioAccesorios acce : lstTemporal) {
            if ("5,7,2,10,11,1,4".contains(acce.getArticuloConexoId().getId().toString())) {
                lstAccesorios.remove(acce);
            }
        }
    }

//    public List<TipoGamac> getListTipoGuia() {
//        listTipoGuia = ejbTipoGamacFacade.selectTipoGamac("TP_GTGAMAC");
//        if (!JsfUtil.isNullOrEmpty(listTipoGuia)) {
//            List<TipoGamac> listTemporal = new ArrayList<>();
//            listTemporal.addAll(listTipoGuia);
//            for (TipoGamac tipoGamac : listTemporal) {
//                listTipoGuia.remove(tipoGamac);
//            }
//        }
//        return listTipoGuia;
//    }
    //FUNCIONES Y METODOS PARA EL FORMULARIO DE CREAR PERSONA
    /**
     * @return the listTipoDoc
     */
    public List<TipoBaseGt> getListTipoDoc() {
        List<TipoBaseGt> listRes = ejbTipoBaseFacade.lstTipoBase("TP_DOCID");
        List<TipoBaseGt> listTemp = new ArrayList(listRes);
        for (TipoBaseGt tipoDoc : listTemp) {
            if ("rep".equals(getTipoPersona())) {
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
//        TipoBase tipoDoc = tipoDocSelect;
        setNewPersona(new SbPersona());
//        newPersona.setTipoDoc(tipoDoc);
        if (armaLic != null && armaLic.get("NRO_CIP") != null) {
            newPersona.setNroCip(armaLic.get("NRO_CIP").toString());
        }
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
                setDisabledBtVal(Boolean.TRUE);
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
                        setNewPersona((SbPersona) JsfUtil.entidadMayusculas(newPersona, StringUtil.VACIO));
                        ejbSbPersonaFacade.create(newPersona);
                        JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroPersonaExito"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorRegistroPersona"));
                    }
                } else {
                    setNewPersona((SbPersona) listPer.get(0));
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeNumeroDocRegistrado"));
                }
                if (newPersona.getId() != null) {
                    RequestContext.getCurrentInstance().execute("PF('CrearPersonaViewDialog').hide()");
                    switch (getTipoPersona()) {
                        case "inf":
                            infractorSelect = newPersona;
                            JsfUtil.mensaje("Se estableció correctamente la persona infractora.");
                            agregarInfractor();
                            RequestContext.getCurrentInstance().update("registroForm:gridListInfrac");
                            RequestContext.getCurrentInstance().update("editarForm:gridListInfrac");
                            break;
                        case "prop":
                            propietario = newPersona;
                            JsfUtil.mensaje("Se estableció correctamente el propietario.");
                            if (armaLic != null) {
                                //Si se trata de una validación por número de licencia desde el formulario de registro de un acta de internamiento (depósito), entonces se setea 
                                //en el campo propietario la persona que acaba de ser creado. Además tanto propietario como solicitante serían la misma persona creada.
                                if (solicitanteCita == null) {
                                    solicitante = propietario;
                                }
                                if (copiaInvArmaTemp != null && copiaInvArmaTemp.getPropietarioId() == null) {
                                    copiaInvArmaTemp.setPropietarioId(propietario);
                                    selectedAmaArma = amaGuiaTransitoGenericoController.obtenerArmaString(copiaInvArmaTemp);
                                }
                                RequestContext.getCurrentInstance().update("registroForm:gridSolicitante");
                                RequestContext.getCurrentInstance().update("editarForm:gridSolicitante");
                            }
                            RequestContext.getCurrentInstance().update("registroForm:gridPropietario");
                            RequestContext.getCurrentInstance().update("editarForm:gridPropietario");
                            break;
                        case "sol":
                            setSolicitante(newPersona);
                            JsfUtil.mensaje("Se estableció correctamente el solicitante.");
                            RequestContext.getCurrentInstance().update("registroForm:gridSolicitante");
                            RequestContext.getCurrentInstance().update("editarForm:gridSolicitante");
                            //Formulario de devolución.
                            RequestContext.getCurrentInstance().update("registroDevForm:panelSolicitante");
                            RequestContext.getCurrentInstance().update("editarDevForm:panelSolicitante");
                            if (turnoDeposito != null) {
                                solicitanteCita = solicitante;
                                RequestContext.getCurrentInstance().execute("PF('CitaDepositoViewDialog').show()");
                            }
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

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     * 2=Guardar y mostrar inventario)
     *
     * con confirmacion)
     *
     * @param tipoGuardado Tipo de guardado
     * @return Página para redireccionar
     * @author Gino Chávez
     * @version 1.0
     */
    public String crearInvArmaTemporal(Long tipoGuardado) {
        try {
            if (validacionCreateEdit(newInvArma) && validarSerieModelo(newInvArma)) {
                newInvArma = (AmaInventarioArma) JsfUtil.entidadMayusculas(newInvArma, StringUtil.VACIO);
                if (lstAccesorios != null) {
                    if (newInvArma.getAmaInventarioAccesoriosList() == null) {
                        newInvArma.setAmaInventarioAccesoriosList(new ArrayList<AmaInventarioAccesorios>());
                    }
                    for (AmaInventarioAccesorios accesBD : newInvArma.getAmaInventarioAccesoriosList()) {
                        Boolean inactivar = Boolean.TRUE;
                        for (AmaInventarioAccesorios acces : lstAccesorios) {
                            if (Objects.equals(accesBD, acces)) {
                                inactivar = Boolean.FALSE;
                                break;
                            }
                        }
                        if (inactivar) {
                            accesBD.setActivo(JsfUtil.FALSE);
                        }
                    }
                    for (AmaInventarioAccesorios acces : lstAccesorios) {
                        if (acces.getId() < L_CERO) {
                            acces.setActivo(JsfUtil.TRUE);
                            acces.setInventarioArmaId(newInvArma);
                            if (!newInvArma.getAmaInventarioAccesoriosList().contains(acces)) {
                                newInvArma.getAmaInventarioAccesoriosList().add(acces);
                            }
                        }
                    }
                }
                //Bloque para copiar los datos del arma seleccionada y almacenarlos en una variable temporal.
                amaGuiaTransitoGenericoController.llenarListArmasTemp(Boolean.TRUE, armaLic, newInvArma, Boolean.TRUE);
                newInvArma = copiaInvArmaTemp;
                propietario = newInvArma.getPropietarioId();
                //Si existe número de serie pero el estado de la serie tiene el valor 'SIN SERIE' entonces no se guarda.
                if (newInvArma.getSerie() != null && "TP_SERINV_SSE".equals(newInvArma.getEstadoserieId().getCodProg())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("MensajeNoModificaSinSerie"));
                    return null;
                }
                if (newInvArma.getId() == null) {
                    newInvArma.setId(JsfUtil.tempIdN());
                }
                //Si el arma temporal [No existe en BD] existe, se convierte a String para poder trabajarlo
                ///////////// Bloque para guardar la lista de fotos /////////
                if (!JsfUtil.isNullOrEmpty(lstFotos)) {
                    int i = 0;
                    String fileName = StringUtil.VACIO;
                    for (Map itemFoto : lstFotos) {
                        fileName = ((String) itemFoto.get("nombre")).toUpperCase();
                        switch (i) {
                            case 0:
                                newInvArma.setFoto1(fileName);
                                break;
                            case 1:
                                //                                newInvArma.setFoto1("GAMAC_FOTO1_" + newInvArma.getId() + fileName.substring(fileName.lastIndexOf('.'), fileName.length()).toUpperCase());
                                newInvArma.setFoto2(fileName);
                                break;
                            case 2:
                                //                                newInvArma.setFoto1("GAMAC_FOTO1_" + newInvArma.getId() + fileName.substring(fileName.lastIndexOf('.'), fileName.length()).toUpperCase());
                                newInvArma.setFoto3(fileName);
                                break;
                            default:
                                break;
                        }
                        itemFoto.put("idArma", newInvArma.getId());
                        // Si no existe el arma en la lista temporal, entonces lo agrega
                        if (!lstFotosTemp.isEmpty()) {
                            if (!lstFotosTemp.contains(itemFoto)) {
                                lstFotosTemp.add(itemFoto);
//                                        }
                            }
//                                }
                        } else {
                            lstFotosTemp.add(itemFoto);
                        }
                        //
                        i++;
                    }
                    //Si la lista de las fotos cargadas son solamente 2, entonces se nulea el valor de la tercera foto.
                    if (lstFotos.size() < 3) {
                        newInvArma.setFoto3(null);
                    }
                }
                selectedAmaArma = amaGuiaTransitoGenericoController.obtenerArmaString(newInvArma);
                RequestContext.getCurrentInstance().execute("PF('CrearInvenArmaViewDialog').hide()");
                RequestContext.getCurrentInstance().update("registroForm:gridPropietario");
                RequestContext.getCurrentInstance().update("editarForm:gridPropietario");
//                }
//                if (isOrgAlmacen) {
                RequestContext.getCurrentInstance().update("registroForm:gridDatosArma");
                RequestContext.getCurrentInstance().update("editarForm:gridDatosArma");
                RequestContext.getCurrentInstance().update("registroForm:groupDatosArma");
                RequestContext.getCurrentInstance().update("editarForm:groupDatosArma");
//                } else {
//                    RequestContext.getCurrentInstance().update("registroForm:datosArmas");
//                    RequestContext.getCurrentInstance().update("editarForm:datosArmas");
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
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
            setRepreSelect(per);
            SbRelacionPersona rp = new SbRelacionPersona();
            rp.setTipoId(ejbTipoBaseFacade.tipoPorCodProg("TP_REL_RL"));
            rp.setPersonaOriId(getSolicitante());
            rp.setPersonaDestId(getRepreSelect());
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

    public void actualizarGridTraslado(Long org) {
        if (L_UNO.equals(org)) {
            eliminarDireccionOrg();
        }
        RequestContext.getCurrentInstance().update("registroForm:gridOrigen");
        RequestContext.getCurrentInstance().update("editarForm:gridOrigen");
        RequestContext.getCurrentInstance().update("registroForm:gridDestino");
        RequestContext.getCurrentInstance().update("editarForm:gridDestino");
    }

    public void eliminarArma() {
        selectedAmaArma = null;
        lstFotos = new ArrayList();
    }

    /**
     * Función al eliminar propietario de campo autocompletable
     *
     * @author Gino Chávez
     * @version 1.0
     * @param isRegInvArma
     */
    public void eliminarPropietarioRegistro(Boolean isRegInvArma) {
        if (isRegInvArma) {
            newInvArma.setPropietarioId(null);
            setRenderPropietario(Boolean.TRUE);
        } else {
            propietario = null;
            setRenderPropietario2(Boolean.TRUE);
        }
    }

    public void eliminarSolicitante() {
        solicitante = null;
        RequestContext.getCurrentInstance().update("registroForm:datosSolicitante");
        RequestContext.getCurrentInstance().update("editarForm:datosSolicitante");
    }

    public void eliminarDireccionOrg() {
        if (sbDireccionOrigen != null) {
            sbDireccionOrigen = null;
            selectedOriOtro = null;
            selectedOriSucamec = null;
        }
    }

    public void eliminarRepresentante() {
        setRepresentante(null);
        RequestContext.getCurrentInstance().update("formAgregarRep");
    }

    public void eliminarAmaDocumento() {
        documentoSelect = null;
    }

    public void eliminarDocumentoCancelacion() {
        documentoCancelacionSelect = null;
    }

    /**
     * FUNCION PARA ELIMINAR FOTO DE ARMA CARGADA
     *
     * @author Gino Chávez
     * @version 1.0
     * @param fotoSelec Map con datos de arma seleccionada
     */
    public void eliminarFotoVerificacion(Map fotoSelec) {
        for (Map fotoV : lstFotos) {
            if (Objects.equals(fotoV.get("nro"), fotoSelec.get("nro"))) {
                lstFotos.remove(fotoSelec);
                break;
            }
        }
        if (lstFotosTemp != null) {
            for (Map fotoTemp : lstFotosTemp) {
                if (Objects.equals(fotoTemp.get("nombre"), fotoSelec.get("nombre"))) {
                    lstFotosTemp.remove(fotoSelec);
                    break;
                }
            }
        }
        if (lstFotos.size() == 3) {
            setDisabledBtnFoto(true);
        } else {
            setDisabledBtnFoto(false);
        }
    }

    public void vaciarDirSucamec() {

        if (selectedDestSucamec == null) {
            sbDireccionDest = null;
            if (newInvArma != null) {
                newInvArma.setAlmacenSucamecId(null);
            }
        }
    }

    public void regresarRegPersona() {
        if (Objects.equals(tipoPersona, "prop")) {
            JsfUtil.mensajeError("Es obligatorio registrar el propietario.");
            prepararFormulario();
        }
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
     * Función para campo propietario autocompletable
     *
     * @param query Campo a buscar
     * @return Listado de documentos
     * @author Gino Chávez
     * @version 1.0
     */
    public List<AmaDocumento> completeDocumento(String query) {
        List<AmaDocumento> filteredDocumento = new ArrayList<>();
        List<AmaDocumento> lista = ejbAmaDocumentoFacade.listDocumentosPorNumero(query);
        if (!JsfUtil.isNullOrEmpty(lista)) {
            for (AmaDocumento doc : lista) {
                if (doc.getNumero().contains(query.toUpperCase())) {
                    filteredDocumento.add(doc);
                }
            }
        }
        return filteredDocumento;
    }

    public List<Documento> completeDocumentoCancelacion(String query) {
        List<Documento> filteredDocumento = new ArrayList<>();
        List<Documento> lista = ejbDocumentoFacade.listarDocumentosCancelacionLicencia(query);
        if (!JsfUtil.isNullOrEmpty(lista)) {
            for (Documento doc : lista) {
                if (doc.getNumero().contains(query.toUpperCase())) {
                    filteredDocumento.add(doc);
                }
            }
        }
        return filteredDocumento;
    }
        
    public List<SbDireccion> completeSbDireccion(String query) {
        FacesContext context = FacesContext.getCurrentInstance();
        String tipoEntrada = (String) UIComponent.getCurrentComponent(context).getAttributes().get("filter");
        List<SbDireccion> filteredSbDireccion = new ArrayList<>();
        List<SbDireccion> lstDireccion = null;
        if ("dirLocOri".equals(tipoEntrada)) {
            if ("TP_GTGAMAC_ALM".equalsIgnoreCase(idTipoGuia.getCodProg())
                    || "TP_GTGAMAC_REI".equalsIgnoreCase(idTipoGuia.getCodProg())
                    || "TP_GTGAMAC_SAL".equalsIgnoreCase(idTipoGuia.getCodProg())) {
                if (propietario != null) {
                    lstDireccion = ejbSbDireccionFacade.listarDireccionesXPersona(propietario.getId());
                } else {
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeIngresePropietario"));
                }
            } else {
                lstDireccion = ejbSbDireccionFacade.listarDirecionPorDireccionOReferencia(query == null ? StringUtil.VACIO : query.toUpperCase());
            }
        } else if ("dirLocDes".equals(tipoEntrada)) {
            if ("TP_GTGAMAC_REC".equalsIgnoreCase(idTipoGuia.getCodProg())
                    || ("TP_GTGAMAC_EXH".equalsIgnoreCase(idTipoGuia.getCodProg()))
                    || ("TP_GTGAMAC_POL".equalsIgnoreCase(idTipoGuia.getCodProg()))
                    || ("TP_GTGAMAC_DEV".equalsIgnoreCase(idTipoGuia.getCodProg()) && "TP_GTORDES_LOC".equalsIgnoreCase(tipoDestinoSelect.getCodProg()))
                    || ("TP_GTGAMAC_MUN".equalsIgnoreCase(idTipoGuia.getCodProg()) && "TP_GTORDES_LOC".equalsIgnoreCase(tipoDestinoSelect.getCodProg()))
                    || ("TP_GTGAMAC_DDV".equalsIgnoreCase(idTipoGuia.getCodProg()) && "TP_GTORDES_LOC".equalsIgnoreCase(tipoDestinoSelect.getCodProg()))) {
                lstDireccion = ejbSbDireccionFacade.listarDireccionesXPersona(solicitante.getId());
            } else if ("TP_GTGAMAC_AGE".equalsIgnoreCase(idTipoGuia.getCodProg())) {
//                if (perDestinoSelect != null) {
//                    lstDireccion = ejbSbDireccionFacade.listarDireccionesXPersona(perDestinoSelect.getId());
//                } else {
//                    JsfUtil.mensajeAdvertencia("Ingrese a la persona destino.");
//                }
            } else {
                lstDireccion = ejbSbDireccionFacade.listarDirecionPorDireccionOReferencia(query == null ? StringUtil.VACIO : query.toUpperCase());
            }
        } else if ("dirSucamec".equals(tipoEntrada)) {
            lstDireccion = ejbSbDireccionFacade.listarDireccionesSucamec();
            List<SbDireccion> listTemp = new ArrayList(lstDireccion);
            for (SbDireccion dir : listTemp) {
                if (Objects.equals("POR REGULARIZAR", dir.getReferencia())) {//temporalmente
                    lstDireccion.remove(dir);
                }
            }
        } else if ("dirOtro".equals(tipoEntrada)) {
            lstDireccion = ejbSbDireccionFacade.listarDirecionPorDireccionOReferencia(query == null ? StringUtil.VACIO : query.toUpperCase());
        }

        if (!JsfUtil.isNullOrEmpty(lstDireccion)) {
            for (SbDireccion dir : lstDireccion) {
                if (dir.getDireccion().contains(query.toUpperCase())
                        || (dir.getReferencia() != null && dir.getReferencia().contains(query.toUpperCase()))) {
                    filteredSbDireccion.add(dir);
                }
            }
        }
        return filteredSbDireccion;
    }

    /**
     * FUNCION PARA AUTOCOMPLETAR DATOS DE Usuario
     *
     * @author Gino Chávez Pajuelo
     * @version 2.0
     * @param query Nombre de usuario a buscar
     * @return Listado de Usuarios
     */
    public List<String> completeDatosArma(String query) {
        Long idTG = amaGuiaTransitoGenericoController.obtenerIdTipoBus(idTipoGuia);
        List<String> listRes = new ArrayList<>();
        String item = null;
        solicitanteTemp = propietario;
        //se aprovecha para nulear el modo
        modoSelect = null;
        tipoInterSelect = null;
        disableModo = Boolean.FALSE;
        //
        Long idOrigen = amaGuiaTransitoGenericoController.validarOrigenArma(DEPOSITO, idTipoGuia, subTipoSelect);
        short condicion = amaGuiaTransitoGenericoController.condicionAlmacen(idTipoGuia, tipoOrigenSelect);
        //La persona podrá ser nulo solamente si el tipo de deposito es por incautacion, decomiso o disposicion del min pub.
        if (solicitanteTemp != null) {
            if (L_CERO.equals(idOrigen)) {
                //Hacer algo
            } else if (L_UNO.equals(idOrigen)) {
                List<Map> lstArma = ejbAmaArmaFacade.listarArmasXCriterios(query.toUpperCase(), idTG, solicitanteTemp);
                if (!JsfUtil.isNullOrEmpty(lstArma)) {
                    for (Map amaArma : lstArma) {
                        if (((String) amaArma.get("nroRua")).contains(query.toUpperCase())
                                || ((String) amaArma.get("serie")).contains(query.toUpperCase())) {
                            listRes.add(amaGuiaTransitoGenericoController.armaString(String.valueOf(amaArma.get("id")), null, null));
                        }
                    }
                }
            } else if (L_DOS.equals(idOrigen)) {
                List<Map> lstInvArma = null;
                if (isSolExpSucamec) {
                    lstInvArma = ejbAmaInventarioArmaFacade.listarArmasXCriterios(query.toUpperCase(), idTG, null, Boolean.TRUE, condicion);
                } else {
                    lstInvArma = ejbAmaInventarioArmaFacade.listarArmasXCriterios(query.toUpperCase(), idTG, solicitanteTemp, Boolean.TRUE, condicion);
                }
                if (!JsfUtil.isNullOrEmpty(lstInvArma)) {
//                    amaGuiaTransitoGenericoController.removeArmasMapDuplicadas(lstInvArma);
                    removerSegunAlmacenSuc(lstInvArma);
                    for (Map amaInvArma : lstInvArma) {
                        listRes.add(amaGuiaTransitoGenericoController.armaString(null, String.valueOf(amaInvArma.get("id")), null));
                    }
                }
            } else if (L_TRES.equals(idOrigen)) {
                //Hacer algo
            } else if (L_UNO_NEG == idOrigen) {
                //Hacer algo
            }
        } else {
            idOrigen = L_TRES;
        }
        if (L_TRES.equals(idOrigen)) {
            if (!"TP_GTGAMAC_REC".equals(idTipoGuia.getCodProg())) {
                List<Map> lstArma = ejbAmaArmaFacade.listarArmasXCriterios(query.toUpperCase(), idTG, solicitanteTemp);
                List<Map> lstInvArma = ejbAmaInventarioArmaFacade.listarArmasXCriterios(query.toUpperCase(), idTG, solicitanteTemp, Boolean.TRUE, condicion);
                if (!"TP_GTGAMAC_POL".equals(idTipoGuia.getCodProg())
                        && !"TP_GTGAMAC_DDV".equals(idTipoGuia.getCodProg())) {
                    if (!JsfUtil.isNullOrEmpty(lstArma)) {
                        for (Map amaArma : lstArma) {
                            if (((String) amaArma.get("nroRua")).contains(query.toUpperCase())
                                    || ((String) amaArma.get("serie")).contains(query.toUpperCase())) {
                                listRes.add(amaGuiaTransitoGenericoController.armaString(String.valueOf(amaArma.get("id")), null, null));
                            }
                        }
                    }
                    if (!JsfUtil.isNullOrEmpty(lstInvArma)) {
//                        amaGuiaTransitoGenericoController.removeArmasMapDuplicadas(lstInvArma);
                        removerSegunAlmacenSuc(lstInvArma);
                        for (Map amaInvArma : lstInvArma) {
                            listRes.add(amaGuiaTransitoGenericoController.armaString(null, String.valueOf(amaInvArma.get("id")), null));
                        }
//                        amaGuiaTransitoGenericoController.removeArmaStringDuplicadas(listRes);
                    }
                } else {
                    amaGuiaTransitoGenericoController.verificarArmasNoRegistradas(lstInvArma, lstArma);
                    removerSegunAlmacenSuc(lstInvArma);
                    for (Map amaInvArma : lstInvArma) {
                        listRes.add(amaGuiaTransitoGenericoController.armaString(null, String.valueOf(amaInvArma.get("id")), null));
                    }
                }
            } else if ("TP_GTGAMAC_REC".equals(idTipoGuia.getCodProg())) {
                //Bloque para la búsqueda de armas importadas con tarjeta de propiedad emitida para el tipo de Guía RECOJO DE ARMA NUEVA
                List<Map> lstArmas = ejbAmaInventarioArmaFacade.listArmasImportadasNuevasConTP(query.toUpperCase(), idTG, solicitanteTemp.getId(), null, selectedOriSucamec.getId());
                if (!JsfUtil.isNullOrEmpty(lstArmas)) {
                    for (Map amaInvArma : lstArmas) {
                        listRes.add(amaGuiaTransitoGenericoController.armaString(null, String.valueOf(amaInvArma.get("id")), null));
                    }
                }
                return listRes;
                //Fin del bloque
            }
        }
        //Bloque para la búsqueda en la lista temporal.
        if (!JsfUtil.isNullOrEmpty(listCopiaInvArmaTemp)) {
            for (AmaInventarioArma invArma : getListCopiaInvArmaTemp()) {
                if ((invArma.getSerie() == null ? StringUtil.VACIO : invArma.getSerie().trim().toUpperCase()).contains(query.trim().toUpperCase())
                        || (invArma.getNroRua() == null ? StringUtil.VACIO : invArma.getNroRua().trim().toUpperCase()).contains(query.trim().toUpperCase())) {
                    List<String> listResTemp = new ArrayList<>();
                    listResTemp.addAll(listRes);
                    for (String strArma : listResTemp) {
                        if (Objects.equals(invArma.getSerie(), amaGuiaTransitoGenericoController.obtenerDatosArma(strArma, "6", L_UNO))
                                && Objects.equals(invArma.getModeloId() == null ? null : invArma.getModeloId().getId(), Long.parseLong(amaGuiaTransitoGenericoController.obtenerDatosArma(strArma, "9", L_UNO)))) {
                            listRes.remove(strArma);
                        }
                    }
                    listRes.add(amaGuiaTransitoGenericoController.armaStringTemp(invArma));
                }
            }
        }
        if (subTipoSelect != null) {
            if (("TP_GTALM_DIPJMP".equals(subTipoSelect.getCodProg())
                    || "TP_GTALM_DISADU".equals(subTipoSelect.getCodProg())
                    || "TP_GTALM_DEC".equals(subTipoSelect.getCodProg())
                    || "TP_GTALM_INC".equals(subTipoSelect.getCodProg()))
                    && propietario != null) {
                JsfUtil.mensajeAdvertencia("Si desea una búsqueda mas completa, es necesario que borre el propietario");
            }
        }
        //Fin de bloque
        resInvArma = Boolean.FALSE;
        return listRes;
    }

    /**
     * FUNCION PARA AUTOCOMPLETAR DATOS DE MUNICION
     *
     * @author Gino Chávez Pajuelo
     * @version 2.0
     * @param query Nombre de usuario a buscar
     * @return Listado de Municiones
     */
    public List<GamacAmaMunicion> completeDatosMunicion(String query) {
        amaMuniSelect = null;
        List<GamacAmaMunicion> lstAmaMunicion = ejbAmaMunicionFacade.obtenerPorMarcaCalibre(query.toUpperCase());
        return lstAmaMunicion;
    }

    /**
     * FUNCIÓN PARA CAMPO AUTOCOMPLETABLE DE ARMA
     *
     * @author Gino Chávez
     * @version 1.0
     * @param query Accesorio a buscar
     * @return Listado de artículos del arma
     */
    public List<AmaInventarioArtconexo> completeAccesorio(String query) {
        return ejbAmaInventarioArtconexoFacade.buscarArticuloXDescripcion(query.toUpperCase());
    }

    /**
     * FUNCIÓN PARA OBTENER LISTADO DE ARMAS PARA CAMPO AUTOCOMPLETABLE
     *
     * @author Gino Chávez
     * @version 1.0
     * @param query Campo a buscar
     * @return Listado de modelo de armas
     */
    public List<String> completeArmaRegistro(String query) {
        List<String> filteredArma = new ArrayList<>();
        String[] lstVariosBusqueda = null;
        List<Map> lstArma;
        int cont = 0;
        query = query.toUpperCase();

        if (query.contains("S/M")) {
            query = query.replaceAll("S/M", StringUtil.VACIO);
            lstVariosBusqueda = query.split("/");
            if (lstVariosBusqueda.length == 1 && lstVariosBusqueda[0].isEmpty()) {
                lstVariosBusqueda[0] = "S/M";
            } else {
                lstVariosBusqueda = append(lstVariosBusqueda, "S/M");
            }
        } else {
            lstVariosBusqueda = query.split("/");
        }

        lstVariosBusqueda = limpiarArrayArma(lstVariosBusqueda);
        lstArma = ejbAmaModelosFacade.obtenerListadoArmasMap(tpArticuloSelect, null);
        for (Map p : lstArma) {
            cont = 0;
            for (String cadena : lstVariosBusqueda) {
                if (!cadena.isEmpty()) {
                    if (p.get("TIPO_ARMA").toString().contains(cadena.toUpperCase().trim())
                            || p.get("MARCA").toString().contains(cadena.toUpperCase().trim())
                            || p.get("MODELO").toString().contains(cadena.toUpperCase().trim())
                            || p.get("CALIBRE").toString().contains(cadena.toUpperCase().trim())) {

                        cont++;
                    }
                }
            }
            if (cont == lstVariosBusqueda.length) {
                filteredArma.add(amaGuiaTransitoGenericoController.invArmaString(p));
            }
        }
        return filteredArma;
    }

    /**
     * ADICIONAR NUEVA FILA A ARRAY
     *
     * @author Gino Chávez
     * @version 1.0
     */
    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    /**
     * QUITAR FILA VACIAS DE ARRAY
     *
     * @author Gino Chávez
     * @version 1.0
     * @param array de cadena
     * @return Array de cadena
     */
    public String[] limpiarArrayArma(String[] array) {
        String[] nuevoArray = new String[0];
        for (String cadena : array) {
            if (!cadena.isEmpty()) {
                nuevoArray = append(nuevoArray, cadena);
            }
        }
        return nuevoArray;
    }

    /**
     * FUNCIÓN PARA OBTENER DESCRIPCIÓN DE ARMA PARA CAMPO AUTOCOMPLETABLE
     *
     * @author Gino Chávez
     * @version 1.0
     * @param mod Modelo de arma
     * @return Descripción de arma (Nombre de Arma / Marca / Modelo /
     * Calibre(s))
     */
    public String obtenerDescripArmaListadoString(String mod) {
        String cadena = StringUtil.VACIO;
        if (mod != null && !mod.isEmpty()) {
            String[] lstVariosBusqueda = mod.toUpperCase().split("@/@");
            cadena = lstVariosBusqueda[5] + " / " + lstVariosBusqueda[6] + " / " + lstVariosBusqueda[1] + " / " + lstVariosBusqueda[4];
        }
        return cadena;
    }

    public StreamedContent verActaArmaEmpadronamientoPdf(CitaDetalleArma objSelect) {
        try {
            String jasper = "/aplicacion/amaDepositoArmas/reportes/DepositoDevolucion.jasper";
            List<AmaGuiaTransito> agt = new ArrayList();
            AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.find(objSelect.getActaInternamientoId());
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

    public StreamedContent verActaMunicionEmpadronamientoPdf(Long idActa) {
        try {
            //actaInternamientoId
            String jasper = "/aplicacion/amaDepositoArmas/reportes/DepositoDevolucion.jasper";
            List<AmaGuiaTransito> agt = new ArrayList();
            AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.find(idActa);
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
        p.put("P_TIPO_ART_ARMA", obtenerTipoArticulArma(amaGuia));
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
            p.put("P_PROPIETARIO", amaGuiaTransitoGenericoController.obtenerDescPersona(arma.getPropietarioId()));
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
        if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
            p.put("P_SOLICITANTE_DEP", amaGuiaTransitoGenericoController.obtenerNombrePersona(solicitante));
            p.put("P_SOLICITANTE_DEV", StringUtil.VACIO);
            p.put("P_IS_DEPOSITO", true);
            p.put("P_TIPO", DEPOSITO);
        } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
            AmaGuiaTransito amaGuiaDep = amaGuia.getGuiaReferenciadaId();
            SbPersona solDeposito = amaGuiaDep.getSolicitanteId();
            if ("TP_PER_JUR".equalsIgnoreCase(solDeposito.getTipoId().getCodProg())) {
                solDeposito = amaGuiaDep.getRepresentanteId();
            }
            p.put("P_SOLICITANTE_DEP", amaGuiaTransitoGenericoController.obtenerNombrePersona(solDeposito));
            p.put("P_SOLICITANTE_DEV", amaGuiaTransitoGenericoController.obtenerNombrePersona(solicitante));
            p.put("P_IS_DEPOSITO", false);
            p.put("P_FECHA_DEV", amaGuia.getFechaEmision());
            p.put("P_TIPO", DEVOLUCION);
        }
        p.put("P_DOC_SOLICITANTE", amaGuiaTransitoGenericoController.obtenerDocumentoPersona(solicitante));
        p.put("P_LISTA_INFRACTORES", (amaGuia.getSbPersonaList() != null && amaGuia.getSbPersonaList().size() > 0) ? amaGuia.getSbPersonaList() : null);
        p.put("P_LISTA_ACCESORIOS", obtenerAccesMap(obtenerAccesoriosVer(amaGuia)));
        p.put("P_LISTA_ACCESORIOS_ACTA", lstAccesoriosActa);
        p.put("P_LISTA_MUNICIONES", obtenerListMuniActivos(amaGuia));
        p.put("P_LISTA_DOCUMENTOS", amaGuia.getAmaDocumentoList());
        p.put("P_LOGO_SUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        p.put("P_HASHQR", "https://www.sucamec.gob.pe/sel/faces/qr/gamacGT.xhtml?h=" + amaGuia.getHashQr());
        return p;
    }

    public List<AmaGuiaMuniciones> obtenerListMuniActivos(AmaGuiaTransito amaGuia) {
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaGuiaMunicionesList())) {
            listGuiaMunicion = new ArrayList<>();
            for (AmaGuiaMuniciones muniTemp : amaGuia.getAmaGuiaMunicionesList()) {
                if (muniTemp.getActivo() == JsfUtil.TRUE) {
                    getListGuiaMunicion().add(muniTemp);
                }
            }
        } else {
            setListGuiaMunicion(null);
        }
        //Return lista se usa solamente para el reporte en pdf
        return getListGuiaMunicion();
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
            String tipoActa = StringUtil.VACIO;
            if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                res = amaGuia.getDireccionDestinoId();
            } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                res = amaGuia.getDireccionOrigenId();
            }
        }
        return res;
    }

//    public String obtenerDocumentoPersona(SbPersona persona) {
//        String res = StringUtil.VACIO;
//        if (persona != null) {
//            res = ("TP_PER_NAT".equals(persona.getTipoId().getCodProg()) && persona.getTipoDoc() == null) ? "DNI - " + persona.getNumDoc()
//                    : (("TP_PER_NAT".equals(persona.getTipoId().getCodProg()) && persona.getTipoDoc() != null) ? ("TP_DOCID_DNI".equals(persona.getTipoDoc().getCodProg()) ? "DNI - " + persona.getNumDoc()
//                    : ("TP_DOCID_CE".equals(persona.getTipoDoc().getCodProg())) ? "C. E. - " + persona.getNumDoc() : StringUtil.VACIO) : "RUC - " + persona.getRuc());
//        }
//        return res;
//    }
    public void setearPropietarioLicencia(String nroDoc) {
        String tipoProp = StringUtil.VACIO;
        String tipoDoc = StringUtil.VACIO;
        if (armaLic != null && armaLic.get("TIPO_PROPIETARIO") != null) {
            tipoProp = (String) armaLic.get("TIPO_PROPIETARIO");
        }
        if (armaLic != null && armaLic.get("TIPO_DOCUMENTO") != null) {
            tipoDoc = (String) armaLic.get("TIPO_DOCUMENTO");
        }
        String codDoc = StringUtil.VACIO;
        switch (tipoProp.trim()) {
            case "PERSONA JURIDICA":
            case "PERS. JURIDICA":
            case "DELEGACION":
            case "DEPENDENCIA POLICIAL":
                disabledBtVal = Boolean.FALSE;
                codDoc = "TP_DOCID_RUC";
                break;
            case "PERSONA NATURAL":
            case "PASAPORTE":
            case "EMPRESA EXTRANJERA":
                if (tipoDoc.trim().equals("CE")) {
                    codDoc = "TP_DOCID_CE";
                } else {
                    codDoc = "TP_DOCID_DNI";
                }
                break;
            default:
                disabledBtVal = Boolean.FALSE;
                codDoc = "TP_DOCID_DNI";
                break;
        }
        newPersona.setTipoDoc(ejbSbTipoFacade.tipoPorCodProg(codDoc));
        obtenerNumCaract();
        newPersona.setNumDoc(nroDoc == null ? (String) armaLic.get("DOC_PROPIETARIO") : nroDoc);
        RequestContext.getCurrentInstance().execute("PF('CrearPersonaViewDialog').show()");
        RequestContext.getCurrentInstance().update("formCrearPersona");
    }

    /**
     *
     * @param accion
     * @param invArma, Arma a editar
     */
    public void openCrearInventArma(String accion, Map invArma) {
        if (invArma != null) {
            newInvArma = new AmaInventarioArma();
            selectedAmaArma = null;
        }
        String msj = StringUtil.VACIO;
        if (!COMPLETAR_ARMA.equals(accionArma)) {
            selectedArmaString = null;
        }
        AmaInventarioArma invArmaTemp = newInvArma;
        if (invArmaTemp.getAlmacenSucamecId() != null || selectedDestSucamec != null) {
            newInvArma = new AmaInventarioArma();
            lstFotos = null;
            accionArma = StringUtil.VACIO.equals(accion) ? accionArma : accion;
            //Esta condicion solamente se cumplira cuando se desea editar desde la lista de armas en el formulario de Registro de Guias de Transito
            switch (accionArma) {
                case EDITAR_ARMA:
                    String selectArmaTemp = StringUtil.VACIO;
                    if (selectedAmaArma != null) {
                        selectArmaTemp = buscarEnListTemporal(Long.parseLong(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "0", L_UNO)));
                        selectedAmaArma = StringUtil.VACIO.equals(selectArmaTemp) ? selectedAmaArma : selectArmaTemp;
                    } else if (invArma != null) {
                        selectArmaTemp = buscarEnListTemporal(Long.parseLong((invArma.get("id") == null ? invArma.get("ID") : invArma.get("id")).toString()));
                        selectedAmaArma = StringUtil.VACIO.equals(selectArmaTemp) ? selectedAmaArma : selectArmaTemp;
                        if (selectedAmaArma == null) {
                            selectedAmaArma = amaGuiaTransitoGenericoController.obtenerArmaString(ejbAmaInventarioArmaFacade.find(Long.parseLong((invArma.get("id") == null ? invArma.get("ID") : invArma.get("id")).toString())));
                        }
                    }
                    newInvArma = copiaInvArmaTemp;
                    //Este bloque de codigo es en el caso de que se este editando el arma desde el formulario de editar acta (GT)
                    if (newInvArma == null) {
                        if (!StringUtil.VACIO.equalsIgnoreCase(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "7", L_UNO))) {
                            newInvArma = ejbAmaInventarioArmaFacade.find(Long.valueOf(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "0", L_UNO)));
                        }
                    }
                    //
                    cargarFotos(newInvArma, Boolean.FALSE);
                    selectedAmaArma = amaGuiaTransitoGenericoController.obtenerArmaString(newInvArma);
                    if (selectedArmaString == null) {
                        Map invArmaMap = ejbAmaModelosFacade.obtenerArmaMap(newInvArma.getModeloId());
                        selectedArmaString = amaGuiaTransitoGenericoController.invArmaString(invArmaMap);
                    }
                    setRenderPropietario(Boolean.FALSE);
                    newInvArma.setPropietarioId(propietario);
                    if (newInvArma.getPropietarioId() == null) {
                        setRenderPropietario(Boolean.TRUE);
                    }
                    break;
                case COMPLETAR_ARMA:
                    newInvArma.setSerie(invArmaTemp.getSerie());
                    newInvArma.setNroRua(invArmaTemp.getNroRua());
                    newInvArma.setModeloId(invArmaTemp.getModeloId());
                    newInvArma.setActual(JsfUtil.FALSE);
                    newInvArma.setEstadofuncionalId(invArmaTemp.getEstadofuncionalId());
                    newInvArma.setPeso(invArmaTemp.getPeso());
                    newInvArma.setPropietarioId(invArmaTemp.getPropietarioId());
                    newInvArma.setAmaInventarioAccesoriosList(new ArrayList());
                    newInvArma.setAmbienteId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_AMB_CJUD"));
                    newInvArma.setCondicionAlmacen(JsfUtil.FALSE);
                    setRenderPropietario(Boolean.FALSE);
                    cargarFotos(newInvArma, Boolean.TRUE);
                    break;
                case CREAR_ARMA:
                    reiniciarValores();
                    newInvArma.setSerie(null);
                    newInvArma.setNroRua(null);
                    newInvArma.setModeloId(null);
                    newInvArma.setActual(JsfUtil.FALSE);
                    if (propietario != null) {
                        newInvArma.setPropietarioId(propietario);
                        setRenderPropietario(Boolean.FALSE);
                    }
                    newInvArma.setAmaInventarioAccesoriosList(new ArrayList());
                    newInvArma.setAmbienteId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_AMB_CJUD"));
                    newInvArma.setCondicionAlmacen(JsfUtil.FALSE);
                    existeArmaInList = Boolean.FALSE;
                    lstFotos = new ArrayList();
                    setDisabledBtnFoto(Boolean.FALSE);
                    setRenderPropietario(Boolean.FALSE);
                    break;
                default:
                    break;
            }
            newInvArma.setCierreInventario(JsfUtil.TRUE);
            newInvArma.setActivo(JsfUtil.TRUE);
            cargarAccesoriosListado();
            seleccionaArma();
            //Almacen SUCAMEC
            newInvArma.setAlmacenSucamecId(selectedDestSucamec == null ? invArmaTemp.getAlmacenSucamecId() : selectedDestSucamec);
            //Si es un arma cuyos campos fueron seteados a partir de los datos de una licencia, entonces se setea también el almacén SUCAMEC
            if (copiaInvArmaTemp != null && copiaInvArmaTemp.getAlmacenSucamecId() == null) {
                copiaInvArmaTemp.setAlmacenSucamecId(selectedDestSucamec == null ? invArmaTemp.getAlmacenSucamecId() : selectedDestSucamec);
            }
            //Habilitar grid para adjuntar fotos, en caso el almacen de destino del arma sea MININTER
            adjuntarFoto = Boolean.FALSE;
            if (newInvArma.getAlmacenSucamecId() != null
                    && newInvArma.getAlmacenSucamecId().getReferencia() != null
                    && newInvArma.getAlmacenSucamecId().getReferencia().contains("MININTER")) {
                adjuntarFoto = Boolean.TRUE;
            }
            RequestContext.getCurrentInstance().execute("PF('CrearInvenArmaViewDialog').show()");
            RequestContext.getCurrentInstance().update("createInvForm");
            RequestContext.getCurrentInstance().execute("PF('IngresoDirSucViewDialog').hide()");
            //Se actualiza el combo Destio del formulario crear deposito o editar deposito
            RequestContext.getCurrentInstance().update("registroForm:cbDirDestino");
            RequestContext.getCurrentInstance().update("editarForm:cbDirDestino");
        } else {
            msj = "Debe seleccionar el Destino para poder registrar el arma.";
            JsfUtil.mensajeAdvertencia(msj);
        }
    }

    public void openCrearDocumento() {
        if (solicitante != null) {
            newDocumento = new AmaDocumento();
            RequestContext.getCurrentInstance().execute("PF('CrearDocumentoViewDialog').show()");
            RequestContext.getCurrentInstance().update("formCrearDoc");
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeIngreseSolicitante"));
        }
    }

    public void openRegistroInternamiento(String tipoArticulo) {
        tipoArtLibre = tipoArticulo;
        if (tipoArtLibre != null) {
            disableGuardar = Boolean.FALSE;
            disableTipoDep = Boolean.FALSE;
            existeCitaOk = Boolean.FALSE;
            limpiarDatosDepDev();
            idTipoGuia = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTGAMAC_ALM");
            cargarListSTGuia();
            amaGuiaTransitoGenericoController.listTipoArticulos(tipoArtLibre);
        }
    }

    public void openDlgAgregarRep(String tipo) {
        tipoPersona = tipo;
        representante = null;
        RequestContext.getCurrentInstance().execute("PF('AgregarRepViewDialog').show()");
        RequestContext.getCurrentInstance().update("formAgregarRep");
    }

    public void openCrearPersona(String tipo, Boolean disca, String nroDoc) {
        tipoPersona = tipo;
        tipoDocSelect = null;
        numeroDoc = null;
        cantNumeros = null;
        newPersona = new SbPersona();
        disabledBtVal = Boolean.TRUE;
        if (!disca) {
            //Si el popup se abre desde el formulario de listado de citas, entonces se setea los valores iniciales para registrar al solicitante.
            if (turnoDeposito != null && turnoDeposito.getPerExamenId() != null) {
                newPersona.setTipoDoc(turnoDeposito.getPerExamenId().getTipoDoc());
                obtenerNumCaract();
                newPersona.setNumDoc(turnoDeposito.getPerExamenId().getNumDoc());
            }
            //Fin
            RequestContext.getCurrentInstance().execute("PF('CrearPersonaViewDialog').show()");
            RequestContext.getCurrentInstance().update("formCrearPersona");
        } else {
            // Si se abre el popup desde la consulta por número de licencia, entonces se hace la validación para setear algunos campos.
            setearPropietarioLicencia(nroDoc);
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
        if (item != null) {
            numExpGuia = (String) item.get("NROEXP");
            amaGuiaSelect = ejbAmaGuiaTransitoFacade.obtenerGuiaXNroExp(numExpGuia);
        } else if (idGuia != null) {
            amaGuiaSelect = ejbAmaGuiaTransitoFacade.find(idGuia);
        }
        inicializarListasArmas();
        llenarListasArmasMap(amaGuiaSelect.getAmaArmaList(), amaGuiaSelect.getAmaInventarioArmaList());
        String nombreCompleto = StringUtil.VACIO;
        listInvAcceMostrar = obtenerAccesoriosVer(amaGuiaSelect);
        obtenerListMuniActivos(amaGuiaSelect);
        obtenerInvArma(amaGuiaSelect);
        nombreCompleto = (amaGuiaTransitoGenericoController.obtenerNombrePersona(amaGuiaSelect.getSolicitanteId()));
        AmaCatalogo tipoDep = obtenerTipoDeposito(amaGuiaSelect);
        if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuiaSelect.getTipoGuiaId().getId()))) {
            msjConfirmacionDeposito = "¿Confirma que el día de hoy " + ReportUtilTurno.mostrarFechaString(new Date()) + " se está recepcionando " + ("TP_ART_MUNI".equals(tipoDep.getCodProg()) ? "las municiones" : ("TP_ART_REPU".equals(tipoDep.getCodProg()) ? "los artículos conexos" : ("TP_ART_ACCE".equals(tipoDep.getCodProg()) ? StringUtil.VACIO : "el arma"))) + ", a solicitud de "
                    + nombreCompleto + ", y cuyas características y accesorios del arma se detallan a continuación?";
        } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuiaSelect.getTipoGuiaId().getId()))) {
            msjConfirmacionDevolucion = "¿Confirma que el día de hoy " + ReportUtilTurno.mostrarFechaString(new Date()) + " se procede a la devolución " + ("TP_ART_MUNI".equals(tipoDep.getCodProg()) ? "de las municiones" : ("TP_ART_REPU".equals(tipoDep.getCodProg()) ? "de los artículos conexos" : ("TP_ART_ACCE".equals(tipoDep.getCodProg()) ? StringUtil.VACIO : "del arma"))) + ", a solicitud de "
                    + nombreCompleto + ", y cuyas características y accesorios del arma se detallan a continuación?";
        }
        fechaArmaJud = null;
        RequestContext.getCurrentInstance().execute("PF('ConfDepositoViewDialog').show()");
        RequestContext.getCurrentInstance().update("formConfDepDev");
    }

    public void obtenerInvArma(AmaGuiaTransito amaGuia) {
        for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
            if (invArma.getActivo() == JsfUtil.TRUE) {
                invArmaDeposito = invArma;
                if (!JsfUtil.isNullOrEmpty(invArma.getAmaInventarioAccesoriosList())) {
                    listAccesArma = new ArrayList<>();
                    for (AmaInventarioAccesorios acc : invArmaDeposito.getAmaInventarioAccesoriosList()) {
                        if (acc.getActivo() == JsfUtil.TRUE) {
                            listAccesArma.add(acc);
                        }
                    }
                }

            } else {
                JsfUtil.mensajeError("El arma ha sido eliminado.");
            }

            break;
        }
    }

    public void openDlgDatosActa(Long idActa) {
        setFlagCollapsed(Boolean.TRUE);
        if (idActa != null) {
            limpiarDatosDepDev();
            AmaGuiaTransito actaTemp = ejbAmaGuiaTransitoFacade.find(idActa);
            if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(actaTemp.getTipoGuiaId().getId()))) {
                actaDepositoSelectL = actaTemp.getGuiaReferenciadaId();
                actaDevolucionSelectL = actaTemp;
            } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(actaTemp.getTipoGuiaId().getId()))) {
                actaDepositoSelectL = actaTemp;
            }
            procesarTipoGuia(actaTemp.getTipoGuiaId(), StringUtil.VACIO);
            obtenerInvArma(actaTemp);
            for (AmaDocumento documento : actaTemp.getAmaDocumentoList()) {
                if (documento.getActivo() == JsfUtil.TRUE) {
                    documentoSelect = documento;
                }
                break;
            }
            for (AmaInventarioArma invArma : actaTemp.getAmaInventarioArmaList()) {
                if (invArma.getActivo() == JsfUtil.TRUE && invArma.getActual() == JsfUtil.TRUE) {
//                    lstFotosTemp = null;
//                    cargarFotos(invArma, Boolean.FALSE);
                    selectedAmaArma = amaGuiaTransitoGenericoController.armaString(null, String.valueOf(invArma.getId()), null);
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

    public void openTomarFotoArma() {
        fotoByte = null;
        setFoto(null);
        setFotoImg(null);
        RequestContext.getCurrentInstance().update("tomarFotoForm");
    }

    public AmaModelos obtenerModelo(Long idModelo) {
        return ejbAmaModelosFacade.find(idModelo);
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
            listAccesoriosGuia = (List<AmaInventarioAccesorios>) new ArrayList(listAcces);
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
                        listAccesorioMostrar.add(invAccesMostrar);
                    }
                }
            }
        }
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

    public void cargarDireccionOrigen(AmaGuiaTransito amaGuia, TipoGamac tipoOrg) {
        //ORIGEN
        if (amaGuia != null && tipoOrg != null) {
            if ("TP_GTORDES_LOC".equalsIgnoreCase(tipoOrg.getCodProg())) {
                selectedOriOtro = null;
                selectedOriSucamec = null;
                selectedOriLocal = amaGuia.getDireccionOrigenId();
//                selectedOriPuer = null;
//                selectedOriAlm = null;
                verificarDireccion(L_DOS);
            } else if ("TP_GTORDES_ALMS".equalsIgnoreCase(tipoOrg.getCodProg())) {
                selectedOriOtro = null;
                selectedOriSucamec = amaGuia.getDireccionOrigenId();
                selectedOriLocal = null;
//                selectedOriPuer = null;
//                selectedOriAlm = null;
                verificarDireccion(L_UNO);
            } else if ("TP_GTORDES_PUER".equalsIgnoreCase(tipoOrg.getCodProg())) {
                selectedOriOtro = null;
                selectedOriSucamec = null;
                selectedOriLocal = null;
//                selectedOriPuer = amaGuia.getOrigenPuerto();
//                selectedOriAlm = null;
                verificarDireccion(L_UNO_NEG);
            } else if ("TP_GTORDES_ALMA".equalsIgnoreCase(tipoOrg.getCodProg())) {
                selectedOriOtro = null;
                selectedOriSucamec = null;
                selectedOriLocal = null;
//                selectedOriPuer = null;
//                selectedOriAlm = amaGuia.getOrigenAlmacenAduana();
                verificarDireccion(L_DOS_NEG);
            } else {
                selectedOriOtro = amaGuia.getDireccionOrigenId();
                selectedOriSucamec = null;
                selectedOriLocal = null;
//                selectedOriPuer = null;
//                selectedOriAlm = null;
                verificarDireccion(L_TRES);
            }
        }
    }

    /**
     * CARGAR LISTADO DE ACCESORIOS PARA MOSTRAR
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void cargarAccesoriosListado() {
        lstAccesorios = new ArrayList<>();
        if (newInvArma != null) {
            if (newInvArma.getAmaInventarioAccesoriosList() != null) {
                for (AmaInventarioAccesorios acce : newInvArma.getAmaInventarioAccesoriosList()) {
                    if (acce.getActivo() == JsfUtil.TRUE) {
                        if (!lstAccesorios.contains(acce)) {
                            lstAccesorios.add(acce);
                        }
                    }
                }
            }
        }
    }

    /**
     * CARGAR KIT DE ACCESORIOS DE revolver
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void cargarKitRevolver() {
        List<AmaInventarioArtconexo> lstAccesoriosKit = ejbAmaInventarioArtconexoFacade.cargarKitRevolver();
        boolean valida;
        limpiarAccesoriosKit();
        for (AmaInventarioArtconexo kit : lstAccesoriosKit) {
            valida = true;
            for (AmaInventarioAccesorios acce : lstAccesorios) {
                if (Objects.equals(acce.getArticuloConexoId().getId(), kit.getId())) {
                    valida = false;
                }
            }
            if (valida) {
                AmaInventarioAccesorios acce = new AmaInventarioAccesorios();
                acce.setId(JsfUtil.tempIdN());
                acce.setActivo(JsfUtil.TRUE);
                acce.setEstadoDeposito(JsfUtil.FALSE);
                acce.setArticuloConexoId(kit);
                acce.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                acce.setAudNumIp(JsfUtil.getIpAddress());
                acce.setCantidad(L_UNO);
                acce.setInventarioArmaId(newInvArma);
                lstAccesorios.add(acce);
                accesorio = null;
                cantidad = null;
            }
        }
    }

    /**
     * CARGAR KIT DE ACCESORIOS DE PISTOLA
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void cargarKitPistola() {
        List<AmaInventarioArtconexo> lstAccesoriosKit = ejbAmaInventarioArtconexoFacade.cargarKitPistola();
        boolean valida;
        limpiarAccesoriosKit();
        for (AmaInventarioArtconexo kit : lstAccesoriosKit) {
            valida = true;
            for (AmaInventarioAccesorios acce : lstAccesorios) {
                if (Objects.equals(acce.getArticuloConexoId().getId(), kit.getId())) {
                    valida = false;
                }
            }
            if (valida) {
                AmaInventarioAccesorios acce = new AmaInventarioAccesorios();
                acce.setId(JsfUtil.tempIdN());
                acce.setActivo(JsfUtil.TRUE);
                acce.setEstadoDeposito(JsfUtil.FALSE);
                acce.setArticuloConexoId(kit);
                acce.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                acce.setAudNumIp(JsfUtil.getIpAddress());
                if (kit.getId() == 7) {
                    acce.setCantidad(2L);
                } else {
                    acce.setCantidad(L_UNO);
                }
                acce.setInventarioArmaId(newInvArma);
                lstAccesorios.add(acce);
                accesorio = null;
                cantidad = null;
            }
        }
    }

    /**
     * FUNCIÓN PARA LIMPIAR SERIE AL SELECCION TIPO "SIN SERIE"
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void seleccionaEstadoSerie() {
        if (newInvArma.getEstadoserieId() != null) {
            if (newInvArma.getEstadoserieId().getCodProg().equals("TP_SERINV_SSE")) {
                if (!accionArma.equals(EDITAR_ARMA) && !accionArma.equals(CREAR_ARMA)) {
                    if (newInvArma.getSerie() != null) {
                        JsfUtil.mensajeError(JsfUtil.bundle("MensajeNoModificaSinSerie"));
                    }
                } else {
                    newInvArma.setSerie(null);
                }
            }
        }
    }

    /**
     * FUNCIÓN AL SELECCIONAR ARMA DE CAMPO AUTOCOMPLETABLE
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void seleccionaArma() {
        if (selectedArmaString != null) {
            if (!selectedArmaString.isEmpty()) {
                String[] lstVariosBusqueda = selectedArmaString.toUpperCase().split("@/@");
                newInvArma.setModeloId(ejbAmaModelosFacade.obtenerModelo(Long.parseLong(StringUtil.VACIO + lstVariosBusqueda[0])));
            }
        }
        if (!Objects.equals(modeloAnterior, newInvArma.getModeloId())) {
            int cant = ejbAmaInventarioArmaFacade.buscarModeloInventario(newInvArma.getModeloId().getId(), (newInvArma.getSerie() == null ? StringUtil.VACIO : newInvArma.getSerie().trim()));
            if (cant > 0) {
                setRenderBtnGuardar(false);
            } else {
                setRenderBtnGuardar(true);
            }
        }
        if (newInvArma.getModeloId() != null) {
            setRenderBtnKitRevolver(false);
            setRenderBtnKitPistola(false);

            if (newInvArma.getModeloId().getTipoArmaId().getCodProg().equals("TP_ARMREV")) {
                setRenderBtnKitRevolver(true);
            }
            if (newInvArma.getModeloId().getTipoArmaId().getCodProg().equals("TP_ARMPIS")) {
                setRenderBtnKitPistola(true);
            }
        }
    }

    public void seleccionCitaProgramada(TurTurno turno) {
        msjRegSolicitante = null;
        addArmaActa = Boolean.FALSE;
        if (turno != null) {
            turnoDeposito = turno;
            if (turnoDeposito.getPerExamenId() != null) {
                solicitanteCita = ejbSbPersonaFacade.selectPersonaxRucNumDoc(turnoDeposito.getPerExamenId().getNumDoc() == null ? StringUtil.VACIO : turnoDeposito.getPerExamenId().getNumDoc());
                if (solicitanteCita == null) {
                    JsfUtil.mensajeAdvertencia("Por favor, sírvase registrar el solicitante para continuar.");
                    msjRegSolicitante = "Por favor, registre el solicitante para continuar.";
                    openCrearPersona("sol", Boolean.FALSE, null);
                    RequestContext.getCurrentInstance().execute("PF('CitaDepositoViewDialog').hide()");
                }
            }
            RequestContext.getCurrentInstance().update("formCita");
        }
    }

    public void verificarDireccion(Long selectDir) {
        Boolean actualiza = Boolean.FALSE;
        if (L_UNO.equals(selectDir)) {
            if (L_CERO.equals(selectedOriSucamec.getDistritoId().getId())) {
                actualiza = Boolean.TRUE;
                selectedOriSucamec = null;
            } else {
                sbDireccionOrigen = selectedOriSucamec;
            }
        } else if (L_DOS.equals(selectDir)) {
            if (L_CERO.equals(selectedOriLocal.getDistritoId().getId())) {
                actualiza = Boolean.TRUE;
                selectedOriLocal = null;
            } else {
                sbDireccionOrigen = selectedOriLocal;
            }
        } else if (L_TRES.equals(selectDir)) {
            if (L_CERO.equals(selectedOriOtro.getDistritoId().getId())) {
                actualiza = Boolean.TRUE;
                selectedOriOtro = null;
            } else {
                sbDireccionOrigen = selectedOriOtro;
            }
        }
        if (actualiza) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeDireccionDesactualizada"));
        }
    }

    public void obtenerDatosLicencia(Boolean existeCita, Boolean sinCita, Boolean buscarEnInventario) {
        existeCitaOk = existeCita;
        disableGuardar = Boolean.TRUE;
        flagCrearConArmaInex = Boolean.FALSE;
        flagCrearSinCita = Boolean.FALSE;
        tipoArtLibre = ART_ARMA;
        listInvArmasSeleccion = null;
        renderX = true;
        actualizarTipoInt = Boolean.TRUE;
        if (idCriterioLic != null) {
            //Inicializar variables
            subTipoSelect = null;
            modoSelect = null;
            tpArticuloSelect = null;
            if (!existeCita) {
                solicitanteCita = null;
            }
            propietario = solicitante = null;
            if (!sinCita) {
                armaLic = null;
            }
            selectedAmaArma = null;
            //
            if (valorBusLic != null && !StringUtil.VACIO.equals(valorBusLic)) {
                //Busca por NRO de Serie
                if (Objects.equals(idCriterioLic, L_DOS) && buscarEnInventario) {
                    Map datos = new HashMap();
                    datos.put("condicion", null);//JsfUtil.FALSE
                    datos.put("codSituacion", null);//datos.put("codSituacion", "TP_SITU_EXH");
                    datos.put("codTipoInter", null);
                    datos.put("propietario", null);
                    listInvArmasSeleccion = ejbAmaInventarioArmaFacade.obtenerArmasxCriterios(Boolean.FALSE, valorBusLic.toUpperCase(), datos);
                    if (!JsfUtil.isNullOrEmpty(listInvArmasSeleccion)) {
                        actualizarTipoInt = Boolean.FALSE;
//                        amaGuiaTransitoGenericoController.removeArmasEntityDuplicadas(listInvArmasSeleccion);
                        RequestContext.getCurrentInstance().execute("PF('SelectInvViewDialog').show()");
                        RequestContext.getCurrentInstance().update("formSeleccionArmaInv");
                        return;
                    }
                }
                //Busca por NRO de Licencia
                if (Objects.equals(idCriterioLic, L_UNO)) {
                    if (isNumerico(valorBusLic)) {
                        //setListArmaLic(ejbRma1369Facade.buscarArmaRMA(null, null, valorBusLic));
                        setListArmaLic(ejbAmaGuiaTransitoFacade.buscarArmaRenagiMigra(null, null, valorBusLic));
                        //Setear id lista
                        if (!JsfUtil.isNullOrEmpty(listArmaLic)) {
                            for (Map item : listArmaLic) {
                                item.put("ID", JsfUtil.tempId());
                            }
                        }
//                        amaGuiaTransitoGenericoController.removeLicMapDuplicadas(listArmaLic);
                    } else {
                        JsfUtil.mensajeError("Ingrese un número válido para la búsqueda por Número de Licencia.");
                        return;
                    }
                } else if (Objects.equals(idCriterioLic, L_DOS)) {
                    //setListArmaLic(ejbRma1369Facade.buscarArmaRMA(valorBusLic, null, null));
                    setListArmaLic(ejbAmaGuiaTransitoFacade.buscarArmaRenagiMigra(valorBusLic, null, null));
                    //Setear id lista
                    if (!JsfUtil.isNullOrEmpty(listArmaLic)) {
                        for (Map item : listArmaLic) {
                            item.put("ID", JsfUtil.tempId());
                        }
                    }
//                    amaGuiaTransitoGenericoController.removeLicMapDuplicadas(listArmaLic);
                }
                if (!JsfUtil.isNullOrEmpty(listArmaLic)) {
                    if (getListArmaLic().size() > 1) {
                        RequestContext.getCurrentInstance().execute("PF('SelectArmaViewDialog').show()");
                        RequestContext.getCurrentInstance().update("formSeleccionArma");
//                        openSeleccionArmaLic();
                    } else {
                        armaLic = getListArmaLic().get(0);
                        procesarDatosArmaLic(sinCita);
                    }
                } else {
                    renderX = false;
                    flagCrearConArmaInex = Boolean.TRUE;
                    JsfUtil.mensajeAdvertencia("No se encontraron datos con el valor ingresado");
                    return;
                }
                valorBusLic = null;
            } else if (armaLic != null && sinCita) {
                procesarDatosArmaLic(sinCita);
            } else {
                flagCrearSinCita = Boolean.FALSE;
                JsfUtil.mensajeAdvertencia("Ingrese el valor a buscar.");
            }
        } else {
            JsfUtil.mensajeAdvertencia("Seleccione el criterio de búsqueda.");
        }
    }

    public void mostrarFormularioInventarioArma(AmaInventarioArma invArma) {
        //if (!JsfUtil.isNullOrEmpty(ejbRma1369Facade.buscarArmaRMA(valorBusLic, null, null))) {
        if (!JsfUtil.isNullOrEmpty(ejbAmaGuiaTransitoFacade.buscarArmaRenagiMigra(valorBusLic, null, null))) {
            RequestContext.getCurrentInstance().execute("PF('SelectInvViewDialog').hide()");
            obtenerDatosLicencia(false, false, false);
            return;
        }

        if (invArma.getModeloId().getActivo() == JsfUtil.TRUE) {
            tpArticuloSelect = ejbAmaCatalogoFacade.obtenerPorCodProg("TP_ART_ARMA");
            obtenerTipoArticulo(true);
            selectedAmaArma = amaGuiaTransitoGenericoController.obtenerArmaString(invArma);
            disableGuardar = Boolean.FALSE;
            propietario = invArma.getPropietarioId();
//        cargarListSTGuia();
            if (Objects.equals((invArma.getSituacionId() == null ? "" : invArma.getSituacionId().getCodProg()), "TP_SITU_EXH")) {
                subTipoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTALM_INC");
                if (!JsfUtil.isNullOrEmpty(listSTGuia)) {
                    List<TipoGamac> listAux = new ArrayList(listSTGuia);
                    for (TipoGamac tg : listAux) {
                        if (Objects.equals(tg.getCodProg(), "TP_GTALM_MANVOL")
                                || Objects.equals(tg.getCodProg(), "TP_GTALM_FALTIT")
                                || Objects.equals(tg.getCodProg(), "TP_GTALM_DISADU")
                                || Objects.equals(tg.getCodProg(), "TP_GTALM_VENPLA")) {
                            listSTGuia.remove(tg);
                        }
                    }
                }
            }
            llenarListasArmasMap(invArma);
            RequestContext.getCurrentInstance().execute("PF('SelectInvViewDialog').hide()");
            RequestContext.getCurrentInstance().update("registroForm");
            RequestContext.getCurrentInstance().update("editarForm");
        } else {
            JsfUtil.mensajeError("El arma cuenta con Modelo Inactivo. Por favor, comuníquese con OGTIC.");
        }
    }

    public void llenarListasArmasMap(AmaInventarioArma amaInvArma) {
        Map<String, String> item = null;
        if (amaInvArma != null) {
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
                item.put("propietario", amaInvArma.getPropietarioId() == null ? StringUtil.VACIO : amaGuiaTransitoGenericoController.obtenerDescPersona(amaInvArma.getPropietarioId()));//indice 8
                item.put("modeloId", String.valueOf(amaInvArma.getModeloId().getId()));//indice 9
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
        listaArmasFinal = new ListDataModel(listaArmasFinalTemp);
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
                    lstAccesoriosActa.add(acces);
                }
            }
        }
        return tipoArt;
    }

    public String obtenerTipoArticulArma(AmaGuiaTransito amaGuia) {
        AmaCatalogo tipoArt = null;
        if (amaGuia.getAmaInventarioArmaList() != null) {
            for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
                //Obtiene el tipo de articulo del arma seleccionada y prepara el formulario para mostrarlo.
                tipoArt = invArma.getModeloId().getTipoArticuloId();
                break;
            }
        }
        return tipoArt == null ? null : JsfUtil.convertirNombrePropio(tipoArt.getNombre());
    }

    public List<AmaInventarioAccesorios> obtenerAccesoriosVer(AmaGuiaTransito amaGuia) {
        List<AmaInventarioAccesorios> listRes = null;
        obtenerInvArma(amaGuia);
        if (invArmaDeposito != null && (!JsfUtil.isNullOrEmpty(invArmaDeposito.getAmaInventarioAccesoriosList()))) {
            listRes = new ArrayList<>();
            for (AmaInventarioAccesorios acces : invArmaDeposito.getAmaInventarioAccesoriosList()) {
                if (acces.getActivo() == JsfUtil.TRUE) {
                    listRes.add(acces);
                }
            }
        }
        invArmaDeposito = null;
        return listRes;
    }

    public void procesarDatosArmaLic(Boolean sinCita) {
        if (armaLic != null) {
            idTipoGuia = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_GTGAMAC_ALM");
            cargarListSTGuia();
            //Bloque que valida si el arma cuenta con acta de internamiento en estado creado.
            List<AmaInventarioArma> listArmas = new ArrayList<>();
            if (armaLic.get("NRO_LIC") != null) {
                listArmas = ejbAmaInventarioArmaFacade.listInvArmaPorLicenciaSerie(Long.valueOf(String.valueOf(armaLic.get("NRO_LIC"))), String.valueOf(armaLic.get("NRO_SERIE")));
            }
            if (!JsfUtil.isNullOrEmpty(listArmas)) {
                for (AmaInventarioArma arma : listArmas) {
                    if (arma.getActivo() == JsfUtil.TRUE && arma.getActual() == JsfUtil.TRUE) {
                        if (!JsfUtil.isNullOrEmpty(arma.getAmaGuiaTransitoList())) {
                            for (AmaGuiaTransito gt : arma.getAmaGuiaTransitoList()) {
                                if (gt.getActivo() == JsfUtil.TRUE
                                        && amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(gt.getTipoGuiaId().getId()))) {
                                    if ((Objects.equals(gt.getEstadoId().getCodProg(), "TP_GTGAMAC_CRE") || Objects.equals(gt.getEstadoId().getCodProg(), "TP_GTGAMAC_REV"))) {
                                        JsfUtil.mensajeError("El arma cuenta con un Acta de Internamiento en estado " + gt.getEstadoId().getNombre() + "."
                                                + " Fué ingresado en " + (gt.getDireccionDestinoId() == null ? "No Registrado. " : (gt.getDireccionDestinoId().getReferencia() + " - " + amaGuiaTransitoGenericoController.descripcionDireccion(gt.getDireccionDestinoId(), " / ") + "."))
                                                + " Por favor verifique el acta buscando por serie del arma.");
                                        return;
                                    } else if (gt.getDevolucionId() == null) {
                                        JsfUtil.mensajeError("El arma cuenta con un Acta de Internamiento en estado " + gt.getEstadoId().getNombre() + ", el cual no cuenta con Acta de devolución."
                                                + " Fué ingresado en " + (gt.getDireccionDestinoId() == null ? "No Registrado. " : (gt.getDireccionDestinoId().getReferencia() + " - " + amaGuiaTransitoGenericoController.descripcionDireccion(gt.getDireccionDestinoId(), " / ") + "."))
                                                + " Por favor verifique el acta buscando por serie del arma.");
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Se obtiene los datos de armas con licencias anuladas con los datos de nro de licencia y nro serie.
//            Map fechas = ejbRma1369Facade.obtenerFechasArmaAnulada(String.valueOf(armaLic.get("NRO_LIC")), String.valueOf(armaLic.get("NRO_SERIE")));
            if (porcesarSegunArmasAnuladas(sinCita)) {
//                disableGuardar = Boolean.FALSE;
                tpArticuloSelect = ejbAmaCatalogoFacade.obtenerPorCodProg("TP_ART_ARMA");
                obtenerTipoArticulo(Boolean.TRUE);
                disableGuardar = setearSelectedAmaArma((String) armaLic.get("NRO_SERIE"), (String) armaLic.get("NRO_RUA"), (String) armaLic.get("DOC_PROPIETARIO"));
                if (!disableGuardar) {
                    List<SbPersona> listPer = null;
                    String nroDoc = StringUtil.VACIO;
                    /*if (!Objects.equals((String) armaLic.get("DOC_PROPIETARIO"), "0") && !Objects.equals((String) armaLic.get("DOC_PROPIETARIO"), "#########")) {
                        if (Objects.equals(armaLic.get("TIPO_PROPIETARIO"), "MARINA")
                                || Objects.equals(armaLic.get("TIPO_PROPIETARIO"), "MILITAR")
                                || Objects.equals(armaLic.get("TIPO_PROPIETARIO"), "FAP")
                                || Objects.equals(armaLic.get("TIPO_PROPIETARIO"), "POLICIA")
                                || Objects.equals(armaLic.get("TIPO_PROPIETARIO"), "EJERCITO")) {
//                            if (Objects.equals((String)armaLic.get("DOC_PROPIETARIO"), (String)armaLic.get("DOC_PORTADOR"))) {
                            listPer = ejbSbPersonaFacade.buscarPersonaXNroCip((String) armaLic.get("DOC_PROPIETARIO"));
                            if (JsfUtil.isNullOrEmpty(listPer)) {
                                nroDoc = (String) armaLic.get("DOC_PORTADOR");
                            }
//                            }
                        }
                    } else {
                        nroDoc = (String) armaLic.get("DOC_PORTADOR");
                    }
                    if (Objects.equals(nroDoc, StringUtil.VACIO)) {*/
                    nroDoc = (String) armaLic.get("DOC_PROPIETARIO");
                    //}
                    if (JsfUtil.isNullOrEmpty(listPer) && armaLic.get("PERSONA_ID") != null && armaLic.get("PERSONA_ID").toString().trim() != "") {
                        //listPer = ejbSbPersonaFacade.buscarPersonaXNumDoc(nroDoc);
                        SbPersona p = ejbSbPersonaFacade.find(Long.parseLong(armaLic.get("PERSONA_ID").toString()));
                        if (p != null) {
                            listPer = new ArrayList();
                            listPer.add(p);
                        }
                    }
                    if (!JsfUtil.isNullOrEmpty(listPer)) {
                        propietario = solicitante = listPer.get(0);
                    } else {
//                        setRequeridoRegPer(Boolean.TRUE);
                        //openCrearPersona("prop", Boolean.TRUE, nroDoc); No se permitirá crear Persona al internar
                    }
                    //Bloque para setear el solicitante en caso de que exista una cita previa.
                    if (solicitanteCita != null) {
                        solicitante = solicitanteCita;
                    }
                    //Fin
                }
            }
        }
        RequestContext.getCurrentInstance().execute("PF('SelectArmaViewDialog').hide()");
        RequestContext.getCurrentInstance().update("registroForm");
    }

    public Boolean porcesarSegunArmasAnuladas(Boolean sinCita) {
        try {
            String codTipoDep = StringUtil.VACIO;
            String codModoDep = StringUtil.VACIO;
            String estadoLic = StringUtil.VACIO;
            int caso = 0;
            disableModo = Boolean.FALSE;
            disableTipoDep = Boolean.FALSE;
            flagCrearSinCita = Boolean.FALSE;
            if (armaLic != null) {
                if (armaLic.get("ID_DISCA") != null) {
                    estadoLic = ejbAmaMaestroArmasFacade.estadoLicenciaDisca(Long.valueOf(armaLic.get("ID_DISCA").toString()));
                    armaLic.put("ESTADO_LICENCIA_DISCA", estadoLic);
                }
                //System.out.println(armaLic.keySet());
                //System.out.println(armaLic.values());
                if (armaLic.get("SISTEMA").toString().equals("GAMAC")) {
                    if (armaLic.get("ESTADO_TARJETA") != null
                            && (armaLic.get("ESTADO_TARJETA").toString().trim().equals("NULIDAD")
                            || armaLic.get("ESTADO_TARJETA").toString().trim().equals("INACTIVO POR CANCELACIÓN DE LICENCIA"))) {
                        //Bloque para setear el tipo de depósito y el modo.
                        List<TipoGamac> listTemp = new ArrayList(listSTGuia);
                        for (TipoGamac tgDep : listTemp) {
                            if (!("TP_GTALM_DIPJMP".equals(tgDep.getCodProg())
                                    || "TP_GTALM_MANVOL".equals(tgDep.getCodProg())
                                    || "TP_GTALM_INC".equals(tgDep.getCodProg())
                                    || "TP_GTALM_FALTIT".equals(tgDep.getCodProg())
                                    || "TP_GTALM_HPR".equals(tgDep.getCodProg())
                                    || "TP_GTALM_DEC".equals(tgDep.getCodProg()))) {
                                listSTGuia.remove(tgDep);
                            }
                        }
                        String sit = armaLic.get("CP_SITUACION") == null ? StringUtil.VACIO : (String) armaLic.get("CP_SITUACION");
                        switch (sit) {
                            case "TP_SITU_ROB":
                            case "TP_SITU_PER":
                                codTipoDep = "TP_GTALM_HPR";
                                codModoDep = "TP_ING_TEM";
//                                disableTipoDep = Boolean.TRUE;
//                                disableModo = Boolean.TRUE;
                                break;
                            case "TP_SITU_CAN":
                            case "TP_SITU_INT_NUL":
                                codTipoDep = "TP_GTALM_MANVOL";
                                codModoDep = StringUtil.VACIO;
                                break;
                            default:
                                codTipoDep = "TP_GTALM_MANVOL";
                                codModoDep = StringUtil.VACIO;
                                break;
                        }
                    }
                } else if (Objects.equals(estadoLic, "CANCELADO") || Objects.equals(estadoLic, "VENCIDO")) {
                    //                    String fechaLimite = ejbSbParametroFacade.obtenerParametroXNombre("amaDeposito_cita_fecha_limite").getValor();//JsfUtil.bundle("fecha_parametrizacion_guias");
                    //                    Date c_hoy = new Date();
                    //                    if (JsfUtil.getFechaSinHora(c_hoy).compareTo(JsfUtil.stringToDate(fechaLimite, "dd/MM/yyyy")) > 0) {
                    //                        if ((Objects.equals((String) armaLic.get("TIPO_PROPIETARIO"), "PERS. NATURAL") || Objects.equals((String) armaLic.get("TIPO_PROPIETARIO"), "EXTRANJERO"))) {
                    //                            //Validación para que no sea posible registrar un acta sin cita previa
                    //                            List<AmaTurnoActa> listTurActa = ejbAmaTurnoActaFacade.listarPorLicSerie(Long.valueOf(armaLic.get("NRO_LIC") == null ? "0" : armaLic.get("NRO_LIC").toString()), String.valueOf(armaLic.get("NRO_SERIE")), fechaHoy);
                    //                            if (listTurActa == null && !sinCita && !amaGuiaTransitoGenericoController.fueraHorario()) {
                    //                                flagCrearSinCita = Boolean.TRUE;
                    //                                disableGuardar = Boolean.TRUE;
                    //                                JsfUtil.mensajeError("La licencia del arma se encuentra cancelada, por lo que el solicitante debe separar su cita para poder internarlo.");
                    //                                return Boolean.FALSE;
                    //                            }
                    //                        }
                    //                        if (!amaGuiaTransitoGenericoController.fueraHorario()) {
                    //                            //Fin validación
                    //                            disableTipoDep = Boolean.TRUE;
                    //                        }
                    //                    } else {
                    //                        List<AmaTurnoActa> listTurActa = ejbAmaTurnoActaFacade.listarPorLicSerie(Long.valueOf(armaLic.get("NRO_LIC") == null ? "0" : armaLic.get("NRO_LIC").toString()), String.valueOf(armaLic.get("NRO_SERIE")), fechaHoy);
                    //                        if (listTurActa != null) {
                    //                            disableTipoDep = Boolean.TRUE;
                    //                        }
                    //                    }

                    // En el caso de que se quiera registrar un acta de un arma sin que se haya sacado cita y que se encuentre cancelada, entonces se setea el tipo de deposito 
                    // con POR DISPOSICIÓN DEL PODER JUDICIAL O MINISTERIO PÚBLICO.
                    if (sinCita) {
                        codTipoDep = "TP_GTALM_DIPJMP";
                        codModoDep = StringUtil.VACIO;
                    } else {
                        //Bloque para setear el tipo de depósito y el modo.
                        String sit = armaLic.get("SITUACION") == null ? StringUtil.VACIO : (String) armaLic.get("SITUACION");
                        switch (sit) {
                            case "EN POSESIÓN PARA INTERNAR POR INCAUTACIÓN":
                            case "PARA INT POR ORDEN DE INCAUTACION":
                                codTipoDep = "TP_GTALM_INC";
                                codModoDep = "TP_ING_TEM";
                                disableTipoDep = Boolean.TRUE;
                                disableModo = Boolean.TRUE;
                                break;
                            case "EN POSESIÓN PARA INTERNAR POR DECOMISO":
                            case "PARA INT POR ORDEN DE DECOMISO":

                                List<TipoGamac> listTemp = new ArrayList(listSTGuia);
                                for (TipoGamac tgDep : listTemp) {
                                    if (!("TP_GTALM_DIPJMP".equals(tgDep.getCodProg())
                                            || "TP_GTALM_DEC".equals(tgDep.getCodProg()))) {
                                        listSTGuia.remove(tgDep);
                                    }
                                }

                                codTipoDep = "TP_GTALM_DEC";
                                codModoDep = "TP_ING_DEF";
                                //                                disableTipoDep = Boolean.TRUE;
                                disableModo = Boolean.TRUE;
                                break;
                            default:
                                codTipoDep = "TP_GTALM_MANVOL";
                                codModoDep = StringUtil.VACIO;
                                break;
                        }
                    }
                } else if (Objects.equals(estadoLic, "ANULADO")) {
                    codTipoDep = "TP_GTALM_MANVOL";
                    codModoDep = "TP_ING_TEM";
                }
                //Fin
            }
            if (!StringUtil.VACIO.equals(codTipoDep)) {
                subTipoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg(codTipoDep);
                adecuarListaArmas(amaGuiaTransitoGenericoController.validarOrigenArma(DEPOSITO, idTipoGuia, subTipoSelect), L_UNO);
            }
            if (!StringUtil.VACIO.equals(codModoDep)) {
                modoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg(codModoDep);
            }
//            RequestContext.getCurrentInstance().update("formSeleccionArma");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Boolean.TRUE;
    }

    /**
     * FUNCIÓN QUE VALIDA QUE EXISTA EL EXPEDIENTE PREVIO A LA SOLICITUD DE ACTA
     * DE DEPÓSITO
     *
     * @author Gino Chávez
     */
    public void validarExpedienteActa() {
        if (nroExpActa != null && !StringUtil.VACIO.equals(nroExpActa)) {
            List<Expediente> listTemp = ejbExpedienteFacade.selectExpedienteXNro(nroExpActa);
            if (!JsfUtil.isNullOrEmpty(listTemp)) {
                expActaDeposito = listTemp.get(0);
                if (ejbExpedienteFacade.expedientesAsignadosXIdUsuario(idUsuarioTramDoc.toString(), nroExpActa)) {
                    if (expActaDeposito.getEstado() != 'X') {
                        if (expActaDeposito.getIdProceso().getIdProceso() == 676L
                                || expActaDeposito.getIdProceso().getIdProceso() == 677L) {
                            nroExpActa = null;
                        } else {
                            expActaDeposito = null;
                            JsfUtil.mensajeAdvertencia("El Expediente ingresado está creado con Proceso que no corresponde a éste trámite.");
                            return;
                        }
                    } else {
                        expActaDeposito = null;
                        JsfUtil.mensajeAdvertencia("El Expediente ingresado se encuentra en estado Archivado.");
                        return;
                    }
                } else {
                    expActaDeposito = null;
                    JsfUtil.mensajeAdvertencia("El Expediente ingresado está asignado a otro Usuario.");
                    return;
                }
            } else {
                JsfUtil.mensajeAdvertencia("No se encontró resultado con el número de Expediente ingresado.");
                return;
            }
        } else {
            expActaDeposito = null;
            JsfUtil.mensajeAdvertencia("Ingrese número de expediente.");
            return;
        }
    }

    /**
     * FUNCIÓN LLAMADA DESDE LA VISTA, EL CUAL SE ENCARGA DE INVOCAR OTRO METODO
     *
     * @param idOrigenLLamada
     */
    public void preAdecuarListaArmas(Long idOrigenLLamada) {
        adecuarListaArmas(amaGuiaTransitoGenericoController.validarOrigenArma(DEPOSITO, idTipoGuia, subTipoSelect), idOrigenLLamada);
        //System.out.println("Combo 1: " + subTipoSelect);
        //System.out.println("Combo sub: " + subSubTipoSelect);

        if (subTipoSelect != null) {
            if (subTipoSelect.getCodProg().equals("TP_GTALM_FALTIT")) {
                if (estado.equals(EstadoCrud.EDITARDEP)) {
                    if (actaRectificada != null) {
                        if (actaDepositoSelect.getAmaInventarioArmaList() != null) {
                            for (AmaInventarioArma inv : actaDepositoSelect.getAmaInventarioArmaList()) {
                                if (inv.getPropietarioId() != null) {
                                    setFlagFallec60dias(validarFallecimiento(inv.getPropietarioId(), actaRectificada.getFechaEmision()));
                                    //System.out.println("Buscar en SB ACTA DEFUNCION por PROPIETARIO");
                                } else {
                                    setFlagFallec60dias(validarFallecimiento(inv.getPosibleTitularId(), actaRectificada.getFechaEmision()));
                                    //System.out.println("Buscar en SB ACTA DEFUNCION por POSIBLE TITULAR");
                                }
                            }
                        }
                    }
                }

                if (estado.equals(EstadoCrud.CREARDEP)) {
                    if (propietario != null) {
                        setFlagFallec60dias(validarFallecimiento(propietario, new Date()));
                        //System.out.println("Buscar en SB ACTA DEFUNCION por PROPIETARIO");
                    } else {
                        setFlagFallec60dias(validarFallecimiento(propietario, new Date()));
                        //System.out.println("Buscar en SB ACTA DEFUNCION por POSIBLE TITULAR");
                    }
                }
            }
        }

    }

    public boolean validarFallecimiento(SbPersona per, Date fechaGuia) {
        //System.out.println("VF subTipoSelect.getCodProg(): " + subTipoSelect.getCodProg());
        boolean res = true;
        fechaGuia = (fechaGuia == null) ? new Date() : fechaGuia;
        List<SbActaDefuncion> listActaDef = ejbSbActaDefuncionFacade.listaxPersona(per.getId());
        if (!listActaDef.isEmpty()) {
            if (listActaDef.size() > 0) {
                SbActaDefuncion actaDef = listActaDef.get(0);
                //System.out.println("fechaGuia: " + fechaGuia + " / fechaFall: " + actaDef.getFechaFallecimiento() + " / fechaFall+60: " + aumentarDias(actaDef.getFechaFallecimiento(), 61));
                if (fechaGuia.compareTo(actaDef.getFechaFallecimiento()) > 0 && fechaGuia.compareTo(aumentarDias(actaDef.getFechaFallecimiento(), 61)) < 0) {
                    res = false;
                } else {
                    res = true;
                }
            }

        }
        //System.out.println("Resultado ValidarFall(): " + res);
        return res;
    }

    public Date aumentarDias(Date fec, int nroDias) {
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(fec);
        fecha.add(fecha.DATE, nroDias);
        return fecha.getTime();
    }

    public boolean validarShowEmpoce() { // hito 1
        boolean res = false;
        if (subTipoSelect != null) {
            //System.out.println("VE subTipoSelect.getCodProg(): " + subTipoSelect.getCodProg());
            switch (subTipoSelect.getCodProg()) {
                case "TP_GTALM_MANVOL":
                    if (modoSelect != null) {
                        if (modoSelect.getCodProg().equals("TP_ING_TEM")) {
                            /*Verifica si es que no ha seleccionado la Resolucion de Cancelacion de Licencia
                            para que no solicite un empoce */
                            if (documentoCancelacionSelect == null) {
                                res = true;
                            }        
                        } else {
                            documentoCancelacionSelect = null;        
                        }
                    }
                    break;
                case "TP_GTALM_FALTIT":
                    documentoCancelacionSelect = null;
                    //System.out.println("FlagFallec60dias: " + isFlagFallec60dias());
                    if (isFlagFallec60dias()) {
                        res = true;
                    }
                    break;
                case "TP_GTALM_VENCAN":
                    documentoCancelacionSelect = null;
                    res = true;
                    break;
                case "TP_GTALM_SEGUSO":
                    documentoCancelacionSelect = null;
                    res = true;
                    break;
                default: 
                     documentoCancelacionSelect = null;
            }
        }
        return res;
    }

    public void adecuarListaArmas(Long idOrigenArma, Long idOrigenLLamada) {
        //se aprovecha para nulear el modo
        modoSelect = null;
        tipoInterSelect = null;
        disableModo = Boolean.FALSE;
        //
        idOrigenTipoGuia = idOrigenLLamada;
        //Eliminación de la direccion de origen seleccionada.        
        if (/*isOrgAlmacen &&*/!"TP_GTALM_VENCAN".equals(subTipoSelect.getCodProg())) {
            tipoOrigenSelect = null;
            eliminarDireccionOrg();
        }
        //
        if (listIdSubTipoGuiaSelect == null) {
            listIdSubTipoGuiaSelect = new ArrayList<>();
        }
        if (0 == listIdSubTipoGuiaSelect.size() && subTipoSelect != null) {
            listIdSubTipoGuiaSelect.add(subTipoSelect);
        }

        if (!StringUtil.isNullOrEmpty(selectedAmaArma)) {
            AmaInventarioArma amaInv = ejbAmaInventarioArmaFacade.find(Long.valueOf(amaGuiaTransitoGenericoController.obtenerDatosArma(selectedAmaArma, "0", L_UNO)));

            if ((amaInv != null && amaInv.getPropietarioId() == null)
                    && !"TP_GTALM_DIPJMP".equals(subTipoSelect.getCodProg())
                    && !"TP_GTALM_DEC".equals(subTipoSelect.getCodProg())
                    && !"TP_GTALM_INC".equals(subTipoSelect.getCodProg())) {
                selectedAmaArma = null;
                RequestContext.getCurrentInstance().update("registroForm:gridDatosArma");
                RequestContext.getCurrentInstance().update("editarForm:gridDatosArma");
            }
        }
    }

    public TipoGamac procesarTipoGuia(TipoGamac tipoGuiaIn, String origen) {
        TipoGamac tipoGuiaTemp1 = null;
        TipoGamac tipoGuiaTemp2 = null;
        tipoGuiaTemp1 = tipoGuiaIn;
        tipoGuiaTemp2 = tipoGuiaTemp1.getTipoId();
        if (tieneSubTipo(tipoGuiaTemp2.getCodProg())) {
            if (tieneSubTipo(tipoGuiaTemp2.getTipoId().getCodProg())) {
                if (VER.equalsIgnoreCase(origen)) {
                    getActaDepositoSelectL().setTipoGuiaId(tipoGuiaTemp2.getTipoId());
                } else {
                    idTipoGuia = tipoGuiaTemp2.getTipoId();
                }
                setSubTipoSelect(tipoGuiaTemp2);
                setSubSubTipoSelect(tipoGuiaTemp1);
            } else {
                if (VER.equalsIgnoreCase(origen)) {
                    getActaDepositoSelectL().setTipoGuiaId(tipoGuiaTemp1.getTipoId());
                } else {
                    idTipoGuia = tipoGuiaTemp1.getTipoId();
                }
                setSubTipoSelect(tipoGuiaTemp1);
                setSubSubTipoSelect(null);
            }
        } else {
            if (VER.equalsIgnoreCase(origen)) {
                getActaDepositoSelectL().setTipoGuiaId(tipoGuiaTemp1);
            } else {
                idTipoGuia = tipoGuiaTemp1;
            }
            setSubTipoSelect(null);
            setSubSubTipoSelect(null);
        }
        //El retorno solamente se hizo para el autocompletar clave en la devolución de armas y 
        //el popup de notificación de Guías Firmadas. Además para añadir nueva arma a inventario arma
        return idTipoGuia;
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

    public Boolean isNumerico(String valor) {
        Boolean isNum = Boolean.TRUE;
        if (valor != null && !StringUtil.VACIO.equals(valor.trim())) {
            for (int i = 0; i < valor.length(); i++) {
                if (!Character.isDigit(valor.charAt(i))) {
                    isNum = Boolean.FALSE;
                    break;
                }
            }
        } else {
            isNum = Boolean.FALSE;
        }
        return isNum;
    }

    /**
     * CAMBIAR CAMPO FECHA DE BUSQUEDA
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiaTipoBusquedaComprobante() {
        filtroComp = null;
        fechaCompBus = null;
        setMuestraBusqFecha(Boolean.FALSE);
        if (tipoBusComp != null) {
            if (tipoBusComp.equals("fecha")) {
                setMuestraBusqFecha(Boolean.TRUE);
            }
        }
    }

    public void verificarArmasPertenencia() {
        if ((solicitante != null || propietario != null) && solicitanteTemp != null) {
            if (solicitanteTemp == propietario) {
//                    isOtroPropietario = Boolean.FALSE;
                solicitanteTemp = null;
            } else if (selectedAmaArma != null) {
//                    isOtroPropietario = Boolean.TRUE;
//                    openDlgConfQuitarArmas();
            }
        }
        seleccionaPropietario(Boolean.FALSE);
    }

    public void agregarMunicion() {
        if (listGuiaMunicion == null) {
            listGuiaMunicion = new ArrayList<>();
        }
        if (amaMuniSelect != null && cantMuni != null) {
            AmaGuiaMuniciones amaGuiaMun = new AmaGuiaMuniciones();
            amaGuiaMun.setId(JsfUtil.tempIdN());
            amaGuiaMun.setCantidad(cantMuni);
            amaGuiaMun.setUmedidaId(uniMedidaMuni);
            amaGuiaMun.setPeso(pesoMuni);
            amaGuiaMun.setActivo(JsfUtil.TRUE);
            amaGuiaMun.setMunicionId(amaMuniSelect);
            List<AmaGuiaMuniciones> listTemp = new ArrayList<>();
            Boolean agregar = Boolean.TRUE;
            if (!listGuiaMunicion.isEmpty()) {
                listTemp.addAll(listGuiaMunicion);
                for (AmaGuiaMuniciones muni : listTemp) {
                    if (muni.getMunicionId().equals(amaGuiaMun.getMunicionId())) {
                        agregar = Boolean.FALSE;
                        break;
                    }
                }
            }
            if (agregar) {
                listGuiaMunicion.add(amaGuiaMun);
            } else {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajExisteMunicionTabla"));
            }
            amaMuniSelect = null;
            cantMuni = null;
            uniMedidaMuni = null;
            pesoMuni = null;
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeIngreseMunicion"));
            return;
        }
    }

    /**
     * FUNCIÓN PARA SELECCIONAR CAMPO PROPIETARIO DEL ARMA
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void agregarAccesorioActa() {
        boolean validacion = true;
        if (accesorioActa == null) {
            validacion = false;
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeIngreseAccesorio"));
        }
        if (getCantAccesActa() == null) {
            validacion = false;
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeIngreseCantAccesorio"));
        } else if (getCantAccesActa() <= 0) {
            validacion = false;
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeIngreseValorMayorCero"));
        }
        if (validacion) {
            if (getLstAccesoriosActa() == null) {
                setLstAccesoriosActa(new ArrayList<AmaInventarioAccesorios>());
            }
            for (AmaInventarioAccesorios acce : getLstAccesoriosActa()) {
                if (Objects.equals(acce.getArticuloConexoId().getId(), accesorioActa.getId())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("MensajeAccesorioExisteTabla"));
                    setAccesorioActa(null);
                    setCantAccesActa(null);
                    return;
                }
            }
            AmaInventarioAccesorios acce = new AmaInventarioAccesorios();
            acce.setId(JsfUtil.tempIdN());
            acce.setActivo(JsfUtil.TRUE);
            acce.setEstadoDeposito(JsfUtil.FALSE);
            acce.setArticuloConexoId(accesorioActa);
            acce.setCantidad(getCantAccesActa());
            acce.setInventarioArmaId(null);
            getLstAccesoriosActa().add(acce);
            setAccesorioActa(null);
            setCantAccesActa(null);
        }
    }

    /**
     * FUNCIÓN PARA SELECCIONAR CAMPO PROPIETARIO DEL ARMA
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void agregarAccesorio() {
        boolean validacion = true;
        if (accesorio == null) {
            validacion = false;
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeIngreseAccesorio"));
        }
        if (cantidad == null) {
            validacion = false;
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeIngreseCantAccesorio"));
        } else if (cantidad <= 0) {
            validacion = false;
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeIngreseValorMayorCero"));
        }
        if (validacion) {
            if (lstAccesorios == null) {
                lstAccesorios = new ArrayList<>();
            }
            for (AmaInventarioAccesorios acce : lstAccesorios) {
                if (Objects.equals(acce.getArticuloConexoId().getId(), accesorio.getId())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("MensajeAccesorioExisteTabla"));
                    accesorio = null;
                    cantidad = null;
                    return;
                }
            }
            AmaInventarioAccesorios acce = new AmaInventarioAccesorios();
            acce.setId(JsfUtil.tempIdN());
            acce.setActivo(JsfUtil.TRUE);
            acce.setEstadoDeposito(JsfUtil.FALSE);
            acce.setArticuloConexoId(accesorio);
            acce.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            acce.setAudNumIp(JsfUtil.getIpAddress());
            acce.setCantidad(cantidad);
            acce.setInventarioArmaId(newInvArma);
            lstAccesorios.add(acce);
            accesorio = null;
            cantidad = null;
        }
    }

    public void agregarInfractor() {
        if (listInfractores == null) {
            listInfractores = new ArrayList<>();
        }
        if (infractorSelect != null) {
            if (!listInfractores.contains(infractorSelect)) {
                listInfractores.add(infractorSelect);
            } else {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajExisteInfractorTabla"));
            }
            infractorSelect = null;
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeIngreseInfractor"));
            return;
        }
    }

    public void agregarTurnoArmaCita() {
        AmaTurnoActa amaTurnoTemp = new AmaTurnoActa(null, Long.valueOf(nroLicCita), serieCita, JsfUtil.TRUE, JsfUtil.getLoggedUser().getLogin(), JsfUtil.getIpAddress());
        amaTurnoTemp.setTurnoId(turnoDeposito);
        amaTurnoTemp = (AmaTurnoActa) JsfUtil.entidadMayusculas(amaTurnoTemp, StringUtil.VACIO);
        ejbAmaTurnoActaFacade.create(amaTurnoTemp);
        turnoDeposito.getAmaTurnoActaList().add(amaTurnoTemp);
        serieCita = null;
        nroLicCita = null;
    }

    public void addArmaGrilla() {
        boolean existeId = false;
        if (serieCita != null && !StringUtil.VACIO.equals(serieCita)
                && nroLicCita != null && !StringUtil.VACIO.equals(nroLicCita)) {
            if (turnoDeposito != null && !turnoDeposito.getAmaTurnoActaList().isEmpty()) {
                if (turnoDeposito.getAmaTurnoActaList().size() < 10) {
                    for (AmaTurnoActa turActa : turnoDeposito.getAmaTurnoActaList()) {
                        if (turActa.getSerie().equals(serieCita.toUpperCase()) && turActa.getNroLic().equals(Long.valueOf(nroLicCita))) {
                            existeId = true;
                            break;
                        }
                    }
                    if (existeId) {
                        JsfUtil.mensajeAdvertencia("Los datos que se intentan añadir ya se encuentran en la tabla.");
                        return;
                    }

                    List<Map> listLicencias = ejbRma1369Facade.listarArmasCanceladasxFiltro(serieCita, nroLicCita);
                    if (!JsfUtil.isNullOrEmpty(listLicencias)) {
                        ListDataModel<ArrayRecord> rs = new ListDataModel(listLicencias);
                        Iterator<ArrayRecord> iteRs = rs.iterator();
                        while (iteRs.hasNext()) {
                            ArrayRecord row = iteRs.next();
                            if (!row.get("ESTADO").toString().equals("CANCELADO")) {
                                JsfUtil.mensajeAdvertencia("La licencia NO está cancelada por pérdida de vigencia. No es necesario programar una cita.");
                                return;
                            }
                        }
                    } else {
                        JsfUtil.mensajeAdvertencia("La licencia NO está cancelada por pérdida de vigencia. No es necesario incluirla en una cita.");
                        return;
                    }
                } else {
                    JsfUtil.mensajeAdvertencia("No es posible agregar más de 10 armas");
                    return;
                }
            }
            agregarTurnoArmaCita();
        } else {
            JsfUtil.mensajeAdvertencia("Ingrese el número de licencia y número de serie.");
        }
    }

    /**
     * FUNCIÓN PARA ELIMINAR MUNICIONES SELECCIONADOS
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void removerMuniciones() {
        if (!JsfUtil.isNullOrEmpty(listGuiaMunicionSelect)) {
            if (listGuiaMunicion.containsAll(listGuiaMunicionSelect)) {
                listGuiaMunicion.removeAll(listGuiaMunicionSelect);
            }
            if (listGuiaMunicion.isEmpty()) {
                listGuiaMunicion = null;
            }
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeSeleccionElementoEliminar"));
        }
    }

    /**
     * FUNCIÓN PARA ELIMINAR ACCESORIO SELECCIONADO DE ACTAS
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void removerAccesoriosActa() {
        if (!JsfUtil.isNullOrEmpty(lstAccesoriosActaSelect)) {
            if (getLstAccesoriosActa().containsAll(getLstAccesoriosActaSelect())) {
                getLstAccesoriosActa().removeAll(getLstAccesoriosActaSelect());
            }
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeSeleccionElementoEliminar"));
        }
    }

    public void removerInfractores() {
        if (!JsfUtil.isNullOrEmpty(listInfractoresSelect)) {
            if (listInfractores.containsAll(listInfractoresSelect)) {
                listInfractores.removeAll(listInfractoresSelect);
            }
            if (listInfractores.isEmpty()) {
                listInfractores = null;
            }
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeSeleccionElementoEliminar"));
        }
    }

    public void removerSegunAlmacenSuc(List<Map> listInvArma) {
        if (isTipoOrigenSucamec()) {
            //StringBuilder strMsj = new StringBuilder(StringUtil.VACIO);
            if (listInvArma != null && selectedOriSucamec != null) {
                List<Map> listTemp = new ArrayList(listInvArma);
                Boolean remover = Boolean.FALSE;
                Boolean almacenDif = Boolean.FALSE;
                for (Map invArma : listTemp) {
                    remover = Boolean.FALSE;
                    if (Objects.equals(invArma.get("enAlmacen"), JsfUtil.FALSE)) {
                        remover = Boolean.TRUE;
                    } else if (!Objects.equals(invArma.get("idDirSucamec"), selectedOriSucamec.getId())) {
                        almacenDif = Boolean.TRUE;
                        remover = Boolean.TRUE;
                    }
                    if (remover) {
                        listInvArma.remove(invArma);
                    }
                }
//                if (!Objects.equals(strMsj.toString(), StringUtil.VACIO)) {
                if (almacenDif) {
                    JsfUtil.mensajeAdvertencia("La búsqueda ha obtenido armas que no se encuentran en el almacén de origen seleccionado.");
                }
            }
        }
    }

    /**
     * FUNCIÓN PARA ELIMINAR ACCESORIO SELECCIONADO
     *
     * @author Gino Chávez
     * @version 1.0
     */
    public void removerAccesorios() {
        if (!JsfUtil.isNullOrEmpty(lstEliminadosAccesorios)) {
            if (lstAccesorios.containsAll(lstEliminadosAccesorios)) {
                lstAccesorios.removeAll(lstEliminadosAccesorios);
            }
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeSeleccionElementoEliminar"));
        }
    }

    public Boolean isTipoOrigenSucamec() {
        if ("TP_GTGAMAC_REC".equals(idTipoGuia.getCodProg())
                || "TP_GTGAMAC_EXH".equals(idTipoGuia.getCodProg())
                || ("TP_GTGAMAC_POL".equals(idTipoGuia.getCodProg()) && !"TP_GTORDES_LOC".equals(tipoOrigenSelect == null ? StringUtil.VACIO : tipoOrigenSelect.getCodProg()))
                || "TP_GTGAMAC_DON".equals(idTipoGuia.getCodProg())
                || "TP_GTGAMAC_SUC".equals(idTipoGuia.getCodProg())
                || "TP_GTGAMAC_DDV".equals(idTipoGuia.getCodProg())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void guardar() {
        try {
            if (validarIngresoComprobante().equals(Boolean.FALSE)) {
                JsfUtil.mensajeError("Debe ingresar el comprobante de pago para registrar el Internamiento.");
                return;
            }

            if (tipoGuiaSave() != null && tipoGuiaSave().getCodProg().equals("TP_GTALM_INDEF")) {
                if (documentoSelect == null) {
                    JsfUtil.mensajeError("Debe seleccionar la Resolución de Ingreso Definitivo de arma de fuego para uso personal.");
                    return;
                }
            }
            String msj = StringUtil.VACIO;
            if (DEPOSITO.equals(tipoAlmaFisico) && selectedDestSucamec != null) {
                if ((!"TP_ART_ACCE".equals(tpArticuloSelect == null ? StringUtil.VACIO : tpArticuloSelect.getCodProg()) && !"TP_ART_MUNI".equals(tpArticuloSelect == null ? StringUtil.VACIO : tpArticuloSelect.getCodProg()) && selectedAmaArma != null && !StringUtil.VACIO.equals(selectedAmaArma.trim()))
                        || ("TP_ART_MUNI".equals(tpArticuloSelect == null ? StringUtil.VACIO : tpArticuloSelect.getCodProg()) && (!JsfUtil.isNullOrEmpty(listGuiaMunicion)))
                        || ("TP_ART_ACCE".equals(tpArticuloSelect == null ? StringUtil.VACIO : tpArticuloSelect.getCodProg()) && (!JsfUtil.isNullOrEmpty(lstAccesoriosActa)))) {
                    //Si existe una lista de accesorios.
                    List<AmaInventarioAccesorios> listAccesoriosSave = null;
                    //Fin
                    Boolean nuevoRegistro = Boolean.TRUE;
                    String cadenaExp = StringUtil.VACIO;
                    if (tipoOpeSelect == null) {
                        tipoOpeSelect = ejbTipoBaseFacade.tipoPorCodProg("TP_OPE_INI");
                    }
                    if (estado == EstadoCrud.EDITAR || estado == EstadoCrud.EDITARDEP || estado == EstadoCrud.EDITARDEV) {
                        nuevoRegistro = Boolean.FALSE;
                        registro = actaDepositoSelect;
                        registro.setSolicitanteId(solicitante);
                    } else {
                        registro = new AmaGuiaTransito();
                        registro.setTipoRegistroId(ejbTipoBaseFacade.tipoPorCodProg("TP_REGIST_NOR"));
                        registro.setTipoOperacionId(tipoOpeSelect);
                        //Solamente para actas rectificadas.
                        if (esRectificatoria && actaDepositoSelect != null) {
                            registro = actaDepositoSelect;
                        }
                        //Fin
                        // Datos en común a todos los tipos de guía
                        registro.setActivo(JsfUtil.TRUE);
                        registro.setSolicitanteId(solicitante);
                        registro.setNroGuia("0");
                        registro.setEstadoId(ejbTipoBaseFacade.tipoPorCodProg("TP_GTGAMAC_CRE"));
                        registro.setUsuarioId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(loginController.getUsuario().getLogin()));
                    }
                    //Si tiene documento de cancelacion de licencia
                    if (documentoCancelacionSelect != null) {
                        registro.setResoluCancelacion(documentoCancelacionSelect.getNumero());
                        registro.setFechaResoluCancelacion(documentoCancelacionSelect.getFechaCreacion());
                    }
                    //Seteo de Tipo de Guía y modo
                    //Guardado del tipo de guía, según tipo de guía y sus subtipos
                    registro.setTipoGuiaId(tipoGuiaSave());
                    // Si el modo es nulo, pasa a procesar segun el tipo de guía a guardar
                    if (modoSelect == null) {
                        //Procesado de modo y tipo de internamiento.
                        procesarModoTipoInt(registro.getTipoGuiaId());
                    } else if ("TP_ING_TEM".equals(modoSelect.getCodProg())) {
                        tipoInterSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_TEM");
                    } else if ("TP_ING_DEF".equals(modoSelect.getCodProg())) {
                        tipoInterSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_DEF");
                    }
                    registro.setTipoIngreso(modoSelect);
                    // Datos en común a todos los tipos de guía
                    if ("TP_PER_JUR".equalsIgnoreCase(solicitante.getTipoId().getCodProg())) {
                        registro.setRepresentanteId(repreSelect);
                    }
                    if (DEPOSITO.equals(tipoAlmaFisico)) {
                        //Limpiar datos de rectificatoria
                        actaRectificada = null;
                        //Fin
                        //Setear datos para el depósito de armas
                        if ("TP_GTALM_MANVOL".equals(subTipoSelect.getCodProg())
                                || "TP_GTALM_FALTIT".equals(subTipoSelect.getCodProg())
                                || "TP_GTALM_VENCAN".equals(subTipoSelect.getCodProg())
                                || "TP_GTALM_VENPLA".equals(subTipoSelect.getCodProg())) {
                            procedenciaSelect = null;
                            dependenciaProc = null;
                            instOrdenaSelect = null;
                            dependenciaInst = null;
                            motivoInSelect = null;
                            listInfractores = null;
                        }
                        registro.setProcedenciaId(procedenciaSelect);
                        registro.setDependenciaProc(dependenciaProc);
                        registro.setInstitucionOrdenaId(instOrdenaSelect);
                        registro.setDependenciaInst(dependenciaInst);
                        registro.setMotivoId(motivoInSelect);
                        registro.setSbPersonaList(listInfractores);
                        registro.setObservacion(obsDeposito);

                        if (documentoSelect != null) {
                            if (documentoSelect.getId() == null) {
                                ejbAmaDocumentoFacade.create(documentoSelect);
                            }
                            if (documentoSelect.getId() != null) {
                                registro.setAmaDocumentoList(new ArrayList<AmaDocumento>());
                                registro.getAmaDocumentoList().add(documentoSelect);
                            }
                        }
                        //Setear número de Expediente si es que el tipo de depósito es por Manifestación Voluntaria y que se haya ingresado el Expediente previo (si existiera)
                        if ("TP_GTALM_MANVOL".equals(subTipoSelect.getCodProg()) && expActaDeposito != null) {
                            registro.setNroExpediente(expActaDeposito.getNumero());
                        }
                        if ("TP_ART_ACCE".equals(tpArticuloSelect == null ? StringUtil.VACIO : tpArticuloSelect.getCodProg())) {
                            copiaInvArmaTemp = null;
                            newInvArma = new AmaInventarioArma();
                            listGuiaMunicion = null;
                            if (listAccesoriosSave == null) {
                                listAccesoriosSave = new ArrayList<>();
                            }
                            if (!nuevoRegistro) {
                                if (!JsfUtil.isNullOrEmpty(registro.getAmaInventarioAccesoriosList())) {
                                    for (AmaInventarioAccesorios accesBD : registro.getAmaInventarioAccesoriosList()) {
                                        Boolean inactivar = Boolean.TRUE;
                                        for (AmaInventarioAccesorios accesTemp : lstAccesoriosActa) {
                                            if (accesTemp.getId().equals(accesBD.getId())) {
                                                inactivar = Boolean.FALSE;
                                                break;
                                            }
                                        }
                                        if (inactivar) {
                                            accesBD.setActivo(JsfUtil.FALSE);
                                            ejbAmaInventarioAccesoriosFacade.edit(accesBD);
                                        }
                                    }
                                }
                                //Fin Bloque
                                //Bloque para añadir registros nuevos a la lista
                                JsfUtil.borrarIdsN(lstAccesoriosActa);
                                for (AmaInventarioAccesorios acces : lstAccesoriosActa) {
                                    if (acces.getId() == null) {
                                        ejbAmaInventarioAccesoriosFacade.create(acces);
                                        listAccesoriosSave.add(acces);
                                    }
                                }
                                //Fin Bloque
                            } else if (!JsfUtil.isNullOrEmpty(lstAccesoriosActa)) {
                                JsfUtil.borrarIdsN(lstAccesoriosActa);
                                for (AmaInventarioAccesorios acces : lstAccesoriosActa) {
//                                        if(acces.getId()==null){
                                    ejbAmaInventarioAccesoriosFacade.create(acces);
//                                        }else{
//                                            ejbAmaInventarioAccesoriosFcade.edit(acces);
//                                        }
                                    listAccesoriosSave.add(acces);
                                }
                            }
                        } else if ("TP_ART_MUNI".equals(tpArticuloSelect == null ? StringUtil.VACIO : tpArticuloSelect.getCodProg())) {
                            copiaInvArmaTemp = null;
                            newInvArma = new AmaInventarioArma();
                            lstAccesoriosActa = null;
                            if (!nuevoRegistro) {
                                actualizarListaMunicionesDep(listGuiaMunicion);
                            }
                            if (esRectificatoria) {
                                if (!JsfUtil.isNullOrEmpty(registro.getAmaGuiaMunicionesList())) {
                                    JsfUtil.borrarIdsN(registro.getAmaGuiaMunicionesList());
                                }
                            }
                        } else {
                            listGuiaMunicion = null;
                            lstAccesoriosActa = null;
                            if (copiaInvArmaTemp != null || (newInvArma != null && newInvArma.getId() != null)) {
                                //Si nunca se actualizaron los datos del arma seleccionada, entonces es aca donde se genera una copia del arma a guardar.
                                if (copiaInvArmaTemp == null) {
                                    amaGuiaTransitoGenericoController.llenarListArmasTemp(Boolean.TRUE, armaLic, newInvArma, Boolean.FALSE);
                                }
                                if (copiaInvArmaTemp.getId() != null) {
                                    if (actualizarTipoInt) {
                                        copiaInvArmaTemp.setTipoInternamientoId(tipoInterSelect);
                                    }
                                    accionArma = EDITAR_ARMA;
                                    if (validacionCreateEdit(copiaInvArmaTemp)) {
                                        amaGuiaTransitoGenericoController.guardarInvArma(copiaInvArmaTemp, nuevoRegistro, null);
                                        registro.setAmaInventarioArmaList(new ArrayList<AmaInventarioArma>());
                                        registro.getAmaInventarioArmaList().add(copiaInvArmaTemp);
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }
                        if (registro.getNroExpediente() == null || "0".equals(registro.getNroExpediente().trim())) {
                            cadenaExp = "PENDIENTE";
                        }
                    }
                    registro.setTipoOrigen(tipoOrigenSelect);
                    registro.setTipoDestino(tipoDestinoSelect);
                    if (selectedDestSucamec != null) {
                        registro.setDireccionDestinoId(selectedDestSucamec);
                    } else {
                        registro.setDireccionDestinoId(null);
                    }
                    if (selectedOriOtro != null) {
                        registro.setDireccionOrigenId(selectedOriOtro);
                    } else if (selectedOriSucamec != null) {
                        registro.setDireccionOrigenId(selectedOriSucamec);
                    } else if (selectedOriLocal != null) {
                        registro.setDireccionOrigenId(selectedOriLocal);
                    } else {
                        registro.setDireccionOrigenId(null);
                    }

                    registro = (AmaGuiaTransito) JsfUtil.entidadMayusculas(registro, StringUtil.VACIO);
                    if (!nuevoRegistro) {
                        ejbAmaGuiaTransitoFacade.edit(registro);
                        msj = JsfUtil.bundle("MensajeConfirmacionActualizacionActaDep");
                    } else {
                        ejbAmaGuiaTransitoFacade.create(registro);
                        //Se actualiza el id del acta en lista de citas
                        if (!JsfUtil.isNullOrEmpty(registro.getAmaInventarioArmaList())) {
                            for (AmaInventarioArma invArma : registro.getAmaInventarioArmaList()) {
                                List<AmaTurnoActa> listTurnos = ejbAmaTurnoActaFacade.listarPorLicSerie(null, invArma.getSerie(), fechaHoy);
                                if (listTurnos != null) {
                                    for (AmaTurnoActa turActa : listTurnos) {
                                        if (Objects.equals(turActa.getSerie(), invArma.getSerie()) && Objects.equals(turActa.getActaId(), null)) {
                                            turActa.setActaId(registro);
                                            ejbAmaTurnoActaFacade.edit(turActa);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        msj = JsfUtil.bundle("MensajeConfirmacionCreacionActaDep");
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
                        if ((tipoAlmaFisico == null || StringUtil.VACIO.equals(tipoAlmaFisico.trim()))
                                || DEPOSITO.equals(tipoAlmaFisico)) {
                            //Si se trata de Actas (isOrgAlmacen) entonces se guarda la lista de municiones sin restricciones.
                            if (!esRectificatoria) {
                                if (!JsfUtil.isNullOrEmpty(listGuiaMunicion)) {
                                    JsfUtil.borrarIdsN(listGuiaMunicion);
                                    for (AmaGuiaMuniciones muni : listGuiaMunicion) {
                                        muni.setGuiaTransitoId(registro);
                                        registro.getAmaGuiaMunicionesList().add(muni);
                                    }
                                }
                            }
                            ejbAmaGuiaTransitoFacade.edit(registro);
                        }
                    }
                    regresar();
                    JsfUtil.mensaje(msj);
                    List<Map> listGTTemporal = ejbAmaGuiaTransitoFacade.obtenerListGuiaCreada(registro.getId());
                    listGuiasTraslado = new ListDataModel(listGTTemporal);
                } else {
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeListaArmasVaciaDep"));
                    return;
                }
            } else {
                JsfUtil.mensajeError(JsfUtil.bundle("MensajeDireccion_Requerido"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarFoto(short isAdjunto) {
        if (fotoByte != null) {
            InputStream is = new ByteArrayInputStream(fotoByte);
            setFoto(new DefaultStreamedContent(is, "image/jpeg"));
            setFotoImg(new DefaultStreamedContent(is, "image/jpeg"));
            if (lstFotos == null) {
                lstFotos = new ArrayList();
            }
            Map mapa = new HashMap();
            mapa.put("idArma", StringUtil.VACIO);
            mapa.put("nro", lstFotos.size() + 1);
            mapa.put("nombre", "GAMAC_" + new Date().getTime() + ".JPG");
            mapa.put("byte", fotoByte);
            mapa.put("isAdjunto", isAdjunto);
            lstFotos.add(mapa);
            if (lstFotos.size() == 3) {
                setDisabledBtnFoto(true);
            } else {
                setDisabledBtnFoto(false);
            }
//            RequestContext.getCurrentInstance().update("createInvForm:pnlFotosArma");
            RequestContext.getCurrentInstance().update("createInvForm:tabCreateInv:tblFotos");
            RequestContext.getCurrentInstance().execute("PF('dlgTomarFoto').hide()");
        } else {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeTomarFoto"));
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void registroUnitarioTrazas() {
        Boolean cambiaEstado = Boolean.TRUE;
        StringBuilder listNroExp = new StringBuilder();
        if (amaGuiaSelect != null) {
            estadoFinal = ejbTipoBaseFacade.tipoPorCodProg("TP_GTGAMAC_FIN");
            amaGuiaSelect.setEstadoId(estadoFinal);
            //Destino, según iteración y formulario origen, si es deposito o devolución de armas (actas) ó Guías de Tránsito.
            if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuiaSelect.getTipoGuiaId().getId()))) {
                if (amaGuiaSelect.getNroExpediente() == null) {
                    amaGuiaSelect.setNroExpediente(crearExpediente());
                }
            }
//            else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(devolucion).contains(String.valueOf(amaGuiaSelect.getTipoGuiaId().getId()))) {
            //seteo del id de devolucion al acta de deposito
//                amaGuiaSelect.getGuiaReferenciadaId().setDevolucionId(amaGuiaSelect);
//                ejbAmaGuiaTransitoFacade.edit(amaGuiaSelect.getGuiaReferenciadaId());
//            }
            //Cambio de estado y registro de la respectiva traza.
            cambiaEstado = registraTrazaExpediente(amaGuiaSelect, JsfUtil.getLoggedUser().getLogin());
            //Edición de valores de Guía o Acta de acuerdo a los cambios realizados.
            if (cambiaEstado) {
                ejbAmaGuiaTransitoFacade.edit(amaGuiaSelect);
                listNroExp.append("\n").append(amaGuiaSelect.getNroExpediente()).append("\n");
            } else {
                return;
            }
            //Mensaje según formulario origen.
            if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuiaSelect.getTipoGuiaId().getId()))) {
                JsfUtil.mensaje(JsfUtil.bundle("MensajeExitoActActaDep") + StringUtil.ESPACIO + listNroExp);
            } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuiaSelect.getTipoGuiaId().getId()))) {
                JsfUtil.mensaje(JsfUtil.bundle("MensajeExitoActActaDev") + StringUtil.ESPACIO + listNroExp);
                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeArchivarExpedienteDev"));
            }
            buscarActas();
            List<Map> listGt = ejbAmaGuiaTransitoFacade.obtenerListGuiaCreada(amaGuiaSelect.getId());
            listGuiasTraslado = new ListDataModel(listGt);
            RequestContext.getCurrentInstance().update("listaActasArmaForm:listaActas");
            RequestContext.getCurrentInstance().execute("PF('ConfDepositoViewDialog').hide()");
        }
    }

    public void crearAmaDocumento() {
        newDocumento.setId(null);
        newDocumento.setActivo(JsfUtil.TRUE);
        newDocumento.setNroExpediente("TEMPORAL");
        newDocumento.setUsuarioCreacionId(usuLogueado);
        newDocumento.setAreaId(usuLogueado.getAreaId());
        newDocumento.setPersonaId(solicitante);
        newDocumento.setFechaCreacion(new Date());
        newDocumento.setEventoTrazaDocId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EVETR_CRE"));
        newDocumento = (AmaDocumento) JsfUtil.entidadMayusculas(newDocumento, StringUtil.VACIO);
        ejbAmaDocumentoFacade.create(newDocumento);
        if (newDocumento.getId() != null) {
            documentoSelect = newDocumento;
            if (DEPOSITO.equalsIgnoreCase(tipoAlmaFisico)) {
                RequestContext.getCurrentInstance().update("registroForm:gridDatosDocumento");
                RequestContext.getCurrentInstance().update("editarForm:gridDatosDocumento");
            } else {
                RequestContext.getCurrentInstance().update("registroDevForm:gridDatosDocumento");
                RequestContext.getCurrentInstance().update("editarDevForm:gridDatosDocumento");
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroDocumentoExito"));
        } else {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorRegistroDocumento"));
        }
        RequestContext.getCurrentInstance().execute("PF('CrearDocumentoViewDialog').hide()");
    }

    public String crearExpediente() {
        //WEB SERVICE PARA CREAR EXPEDIENTE
        if (isConsulta == null) {
            isConsulta = Boolean.FALSE;
        }
        String expedienteId = StringUtil.VACIO;
        String numDoc = StringUtil.VACIO;
        TipoCliente tipoC = null;
        SbPersona sol = null;
        Cliente cli = null;
        for (AmaInventarioArma arma : amaGuiaSelect.getAmaInventarioArmaList()) {
            sol = arma.getPropietarioId();
            break;
        }
        if (sol == null) {
            sol = amaGuiaSelect.getSolicitanteId();
        }
        if (sol.getRuc() != null) {
            numDoc = sol.getRuc();
        } else {
            numDoc = sol.getNumDoc();
        }
        cli = ejbClienteFacade.onbtenerClientePorNumDoc(numDoc);
        if (cli == null) {
            cli = new Cliente();
            if (sol.getTipoDoc() != null) {
                switch (sol.getTipoDoc().getCodProg()) {
                    case "TP_DOCID_RUC":
                        tipoC = ejbTipoClienteFacade.find(BigDecimal.valueOf(1));
                        cli.setRazonSocial(sol.getRznSocial());
                        cli.setNumeroIdentificacion(sol.getRuc());
                        break;
                    case "TP_DOCID_DNI":
                        tipoC = ejbTipoClienteFacade.find(BigDecimal.valueOf(2));
                        cli.setNombre(sol.getNombres());
                        cli.setApellidoPaterno(sol.getApePat());
                        cli.setApellidoMaterno(sol.getApeMat());
                        cli.setNumeroIdentificacion(sol.getNumDoc());
                        break;
                    case "TP_DOCID_CE":
                        tipoC = ejbTipoClienteFacade.find(BigDecimal.valueOf(3));
                        cli.setNombre(sol.getNombres());
                        cli.setApellidoPaterno(sol.getApePat());
                        cli.setApellidoMaterno(sol.getApeMat());
                        cli.setNumeroIdentificacion(sol.getNumDoc());
                        break;
                    case "TP_DOCID_PAS":
                        tipoC = ejbTipoClienteFacade.find(BigDecimal.valueOf(5));
                        cli.setNombre(sol.getNombres());
                        cli.setApellidoPaterno(sol.getApePat());
                        cli.setApellidoMaterno(sol.getApeMat());
                        cli.setNumeroIdentificacion(sol.getNumDoc());
                        break;
                }
            } else if (sol.getRznSocial() != null) {
                tipoC = ejbTipoClienteFacade.find(BigDecimal.valueOf(1));
                cli.setRazonSocial(sol.getRznSocial());
                cli.setNumeroIdentificacion(sol.getRuc());
            } else {
                tipoC = ejbTipoClienteFacade.find(BigDecimal.valueOf(2));
                cli.setNombre(sol.getNombres());
                cli.setApellidoPaterno(sol.getApePat());
                cli.setApellidoMaterno(sol.getApeMat());
                cli.setNumeroIdentificacion(sol.getNumDoc());
            }

            cli.setIdTipoCliente(tipoC);
        }
        //SE BUSCAR EL USUARIO DE TRAMDOC PARA ASIGNARLO COMO PARAMETRO
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        Integer usuaTramDoc = ejbSbUsuarioFacade.obtenerIdUsuarioTramDoc(p_user.getLogin());
        int idProceso = 0;
        boolean valEmpoce = false;
        if (amaGuiaSelect.getTipoIngreso() != null) {
            if ("TP_GTALM_MANVOL".equalsIgnoreCase(amaGuiaSelect.getTipoGuiaId().getCodProg())
                    || "TP_GTALM_FALTIT".equalsIgnoreCase(amaGuiaSelect.getTipoGuiaId().getCodProg())
                    || "TP_GTALM_VENCAN".equalsIgnoreCase(amaGuiaSelect.getTipoGuiaId().getCodProg())
                    || "TP_GTALM_SEGUSO".equalsIgnoreCase(amaGuiaSelect.getTipoGuiaId().getCodProg())) {
                if ("TP_ING_DEF".equalsIgnoreCase(amaGuiaSelect.getTipoIngreso().getCodProg())) {
                    /*idProceso = ((amaGuiaSelect.getDireccionDestinoId().getReferencia().equals("SEDE CENTRAL")
                            || amaGuiaSelect.getDireccionDestinoId().getReferencia().equals("SEDE MININTER")) ? 1070 : 1215);*/
                    idProceso = ((amaGuiaSelect.getDireccionDestinoId().getReferencia().equals("SEDE CENTRAL")
                            || amaGuiaSelect.getDireccionDestinoId().getReferencia().equals("SEDE MININTER")) ? 1593 : 1593);
                } else if ("TP_ING_TEM".equalsIgnoreCase(amaGuiaSelect.getTipoIngreso().getCodProg())) {
                    /*idProceso = ((amaGuiaSelect.getDireccionDestinoId().getReferencia().equals("SEDE CENTRAL")
                            || amaGuiaSelect.getDireccionDestinoId().getReferencia().equals("SEDE MININTER")) ? 1069 : 1214);*/
                    idProceso = ((amaGuiaSelect.getDireccionDestinoId().getReferencia().equals("SEDE CENTRAL")
                            || amaGuiaSelect.getDireccionDestinoId().getReferencia().equals("SEDE MININTER")) ? 1592 : 1592);
                    valEmpoce = true;
                    if ("TP_GTALM_FALTIT".equalsIgnoreCase(amaGuiaSelect.getTipoGuiaId().getCodProg())) {
                        if (amaGuiaSelect.getAmaInventarioArmaList().get(0).getPropietarioId() != null) {
                            valEmpoce = validarFallecimiento(amaGuiaSelect.getAmaInventarioArmaList().get(0).getPropietarioId(), amaGuiaSelect.getFechaEmision());
                        } else if (amaGuiaSelect.getAmaInventarioArmaList().get(0).getPosibleTitularId() != null) {
                            valEmpoce = validarFallecimiento(amaGuiaSelect.getAmaInventarioArmaList().get(0).getPosibleTitularId(), amaGuiaSelect.getFechaEmision());
                        }
                    }
                }
            } else if ("TP_ING_DEF".equalsIgnoreCase(amaGuiaSelect.getTipoIngreso().getCodProg())) {
                idProceso = 677;
            } else if ("TP_ING_TEM".equalsIgnoreCase(amaGuiaSelect.getTipoIngreso().getCodProg())) {
                idProceso = 676;
            }
        } else {
            idProceso = 676;
        }
        String nroEmpoce = "0";
        Date fechaEmpoce = null;
        if (valEmpoce) {
            List<SbReciboRegistro> lstRecibos = ejbSbReciboRegistroFacade.obtenerRecibosPorExpediente("PENDIENTE_" + amaGuiaSelect.getId());
            if (lstRecibos != null && !lstRecibos.isEmpty()) {
                nroEmpoce = lstRecibos.get(0).getReciboId().getNroSecuencia().toString();
                fechaEmpoce = lstRecibos.get(0).getReciboId().getFechaMovimiento();
            }
        }

        expedienteId = wsTramDocController.crearExpedienteGT(amaGuiaSelect, cli, "Depósito de arma en arsenal de SUCAMEC", idProceso, usuaTramDoc, usuaTramDoc, sol, nroEmpoce, fechaEmpoce);
        if (expedienteId == null) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorWebService"));
            return null;
        }
        return expedienteId;
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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
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
                        amaGuia.setFechaEmision(fechaArmaJud == null ? (new Date()) : fechaArmaJud);
//                            setearNroGuiaHash(amaGuia);
                        if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                            amaGuia.setFechaLlegada(new Date());
                            //If is internment of  arms, so update the number of case file.
                            List<SbReciboRegistro> listReg = ejbSbReciboRegistroFacade.obtenerRecibosPorExpediente("PENDIENTE_" + amaGuia.getId());
                            if (listReg != null) {
                                for (SbReciboRegistro reg : listReg) {
                                    reg.setNroExpediente(amaGuia.getNroExpediente());
                                    ejbSbReciboRegistroFacade.edit(reg);
                                }
                            }
                            //
                            acciones.add(13);
                            observacion = "Trámite concluido. Se procede a la entrega del Acta de Depósito con fecha " + ReportUtilTurno.formatoFechaDdMmYyyy(amaGuia.getFechaEmision()) + StringUtil.VACIO;
                        } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                            amaGuia.setFechaSalida(new Date());
                            acciones.add(21);
                            observacion = "Se procede a la entrega del Acta de Devolución con fecha " + ReportUtilTurno.formatoFechaDdMmYyyy(amaGuia.getFechaEmision()) + StringUtil.VACIO;
                        }
                        break;
                    default:
                        break;
                }
                Boolean expDerivado = Boolean.TRUE;
                if (!amaGuia.getEstadoId().getCodProg().equals("TP_GTGAMAC_FIN")) {
                    expDerivado = estadoTraza = wsTramDocController.asignarExpediente(amaGuia.getNroExpediente(), JsfUtil.getLoggedUser().getLogin(), userDestino, observacion, StringUtil.VACIO, acciones, adjuntar, null);
                } else {
                    if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                        expDerivado = estadoTraza = wsTramDocController.asignarExpediente(amaGuia.getNroExpediente(), JsfUtil.getLoggedUser().getLogin(), userDestino, observacion, StringUtil.VACIO, acciones, adjuntar, null);
                    } else {
                        estadoTraza = wsTramDocController.archivarExpediente(amaGuia.getNroExpediente(), userDestino, observacion);
                    }
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
//            amaGuia.setFechaVencimiento(null);
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
            if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                numeroActa = amaGuiaTransitoGenericoController.numeroActa("TP_NUM_DEP_AR", amaGuia);
                actualizarDatosAlmacen(amaGuia.getAmaInventarioArmaList(), amaGuia.getDireccionDestinoId(), Boolean.TRUE);
                actEstDepositoAccesorios(amaGuia, Boolean.TRUE);
            } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                numeroActa = amaGuiaTransitoGenericoController.numeroActa("TP_NUM_DEV_AR", amaGuia);
                actualizarDatosAlmacen(amaGuia.getAmaInventarioArmaList(), null, Boolean.FALSE);
            }
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
    public void actualizarSituacionArma(AmaGuiaTransito amaGuia, Boolean isIngresoAlm) {
        AmaArmaInventarioDif amaInvDif = null;
        TipoGamac tipoGamac = null;
        String codProg = StringUtil.VACIO;
        if (isIngresoAlm) {
            codProg = "TP_SITU_INT";
        } else {
            codProg = amaGuiaTransitoGenericoController.obtenerSituacionSegunTG(amaGuia.getTipoGuiaId());
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
//                        AmaArma arma = null;
                        if (!StringUtil.isNullOrEmpty(amaInvArma.getSerie())) {
                            //Actualización de la situación del arma en la tabla ama_arma
//                            arma = ejbAmaArmaFacade.obtenerArmaPorModeloSerie(amaInvArma.getModeloId().getId(), amaInvArma.getSerie(), amaInvArma.getNroRua());
                            if (amaInvArma.getArmaId() != null) {
                                if (!isIngresoAlm) {
                                    AmaTarjetaPropiedad tp = obtenerTarjetaArma(amaInvArma.getArmaId().getAmaTarjetaPropiedadList());
                                    if (Objects.equals(amaInvArma.getTipoInternamientoId().getCodProg(), "TP_INTER_INM") && !StringUtil.isNullOrEmpty(tp.getRgComprador())) {
                                        tipoGamac = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_SITU_POS_EP");
                                    }
                                }
                                amaInvArma.getArmaId().setSituacionId(tipoGamac);
                                ejbAmaArmaFacade.edit(amaInvArma.getArmaId());
                            } else if (amaInvArma.getDiscaFoxId() != null) {
                                if (codProg.equals("TP_SITU_INT")) {
                                    amaInvArma.getDiscaFoxId().setSituacionDisca(14L);
                                    ejbAmaMaestroArmasFacade.edit(amaInvArma.getDiscaFoxId());
                                }
                            }
                        }
                        //Fin
                        amaInvArma.setSituacionId(tipoGamac);
                        //Si el metodo es invocado desde el modulo de Ingreso a Almacén, según tipo de Guía, entonces se actualiza el ambiente y anaquel
                        if (isIngresoAlm) {
                            amaInvArma.setAmbienteId(ambienteSelect);
                            amaInvArma.setAnaquel(anaquel);
                        }
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

    public AmaTarjetaPropiedad obtenerTarjetaArma(List<AmaTarjetaPropiedad> listadoTarjetas) {

        if (!JsfUtil.isNullOrEmpty(listadoTarjetas)) {
            for (AmaTarjetaPropiedad tarjeta : listadoTarjetas) {
                if (tarjeta.getActivo() == 1 && tarjeta.getEmitido() == 1) {
                    return tarjeta;
                }
            }
        }
        return null;
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

    public void actEstDepositoAccesorios(AmaGuiaTransito amaGuia, Boolean inSucamec) {
        if (amaGuia != null) {
            if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList())) {
                for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
                    if (invArma.getActivo() == JsfUtil.TRUE && invArma.getActual() == JsfUtil.TRUE) {
                        if (!JsfUtil.isNullOrEmpty(invArma.getAmaInventarioAccesoriosList())) {
                            for (AmaInventarioAccesorios acces : invArma.getAmaInventarioAccesoriosList()) {
                                if (acces.getActivo() == JsfUtil.TRUE) {
                                    if (inSucamec) {
                                        acces.setEstadoDeposito(JsfUtil.TRUE);
                                    } else {
                                        acces.setEstadoDeposito(JsfUtil.FALSE);
                                    }
                                }
                            }
                        }
                        ejbAmaInventarioArmaFacade.edit(invArma);
                    }
                }
            } else if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioAccesoriosList())) {
                for (AmaInventarioAccesorios acces : amaGuia.getAmaInventarioAccesoriosList()) {
                    if (acces.getActivo() == JsfUtil.TRUE) {
                        if (inSucamec) {
                            acces.setEstadoDeposito(JsfUtil.TRUE);
                        } else {
                            acces.setEstadoDeposito(JsfUtil.FALSE);
                        }
                        ejbAmaInventarioAccesoriosFacade.edit(acces);
                    }
                }
            }
        }
    }

    public TipoGamac tipoGuiaSave() {
        TipoGamac res = null;
        if (tieneSubTipo(idTipoGuia.getCodProg())) {
            if (subTipoSelect != null) {
                if (tieneSubTipo(subTipoSelect.getCodProg())) {
                    if (subSubTipoSelect != null) {
                        res = subSubTipoSelect;
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

    public void procesarModoTipoInt(TipoGamac tipoGuia) {
        if (tipoGuia != null) {
            if ("TP_GTGAMAC_IMP".equals(tipoGuia.getCodProg())) {
                modoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ING_TEM");
                tipoInterSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_TEM");
            } else if ("TP_GTALM_VENPLA".equals(tipoGuia.getCodProg())
                    || "TP_GTGAMAC_REI".equals(tipoGuia.getCodProg())
                    || "TP_GTALM_FALTIT".equals(tipoGuia.getCodProg())
                    || "TP_GTALM_INDEF".equals(tipoGuia.getCodProg())
                    || "TP_GTALM_INC".equals(tipoGuia.getCodProg())
                    || "TP_GTALM_VENCAN".equals(tipoGuia.getCodProg())
                    || "TP_GTALM_HPR".equals(tipoGuia.getCodProg())
                    || "TP_GTALM_SEGUSO".equals(tipoGuia.getCodProg())) {
                modoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ING_TEM");
                tipoInterSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_TEM");
            } else if ("TP_GTALM_DEC".equals(tipoGuia.getCodProg())) {
                modoSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ING_DEF");
                tipoInterSelect = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_DEF");
            }
        }
    }

    public void actualizarListaMunicionesDep(List<AmaGuiaMuniciones> listMuniciones) {
        try {
            if (!JsfUtil.isNullOrEmpty(listMuniciones)) {
                //Bloque para inactivar los registros que se eliminaron de la lista original
                if (!JsfUtil.isNullOrEmpty(registro.getAmaGuiaMunicionesList())) {
                    for (AmaGuiaMuniciones muniBD : registro.getAmaGuiaMunicionesList()) {
                        Boolean inactivar = Boolean.TRUE;
                        for (AmaGuiaMuniciones muniTemp : listMuniciones) {
                            if (muniTemp.getId().equals(muniBD.getId())) {
                                inactivar = Boolean.FALSE;
                                break;
                            }
                        }
                        if (inactivar) {
                            muniBD.setActivo(JsfUtil.FALSE);
                            ejbAmaGuiaMunicionesFacade.edit(muniBD);
                        }
                    }
                }
                //Fin Bloque
                //Bloque para añadir registros nuevos a la lista
                JsfUtil.borrarIdsN(listMuniciones);
                for (AmaGuiaMuniciones muni : listMuniciones) {
                    if (registro.getAmaGuiaMunicionesList() == null) {
                        registro.setAmaGuiaMunicionesList(new ArrayList<AmaGuiaMuniciones>());
                    }
                    if (muni.getId() == null) {
                        muni.setActivo(JsfUtil.TRUE);
                        muni.setGuiaTransitoId(registro);
                        registro.getAmaGuiaMunicionesList().add(muni);
                    }
                }
                //Fin Bloque
            } else //Bloque para inactivar los registros que se eliminaron de la lista original
            {
                if (!JsfUtil.isNullOrEmpty(registro.getAmaGuiaMunicionesList())) {
                    for (AmaGuiaMuniciones muniBD : registro.getAmaGuiaMunicionesList()) {
                        muniBD.setActivo(JsfUtil.FALSE);
                        ejbAmaGuiaMunicionesFacade.edit(muniBD);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validación antes de guardado o edición de datos.
     *
     * @param invArma
     * @return Booleano de validación correcta o no
     * @author Gino Chávez
     * @version 1.0
     */
    public boolean validacionCreateEdit(AmaInventarioArma invArma) {
        boolean validacion = true;
        Boolean isReqSerie = Boolean.TRUE;
        TipoGamac estSerie = null;
        if (invArma != null && invArma.getEstadoserieId() != null) {
            estSerie = invArma.getEstadoserieId();
        }
        if ("TP_GTGAMAC_SUC".equals(idTipoGuia.getCodProg())
                || (subTipoSelect != null && ("TP_GTALM_DIPJMP".equals(subTipoSelect.getCodProg())
                || "TP_GTALM_DISADU".equals(subTipoSelect.getCodProg())
                || "TP_GTALM_DEC".equals(subTipoSelect.getCodProg())
                || "TP_GTALM_INC".equals(subTipoSelect.getCodProg())))
                || "TP_SERINV_SSE".equals(estSerie == null ? StringUtil.VACIO : estSerie.getCodProg())) {
            isReqSerie = Boolean.FALSE;
        }
        if (Objects.equals(checkTipoDeposito, 2)) {
            if (estSerie != null) {
                if ("TP_SERINV_SSE".equals(estSerie.getCodProg())
                        || "TP_SERINV_BOR".equals(estSerie.getCodProg())
                        || "TP_SERINV_ILE".equals(estSerie.getCodProg())
                        || "TP_SERINV_ERR".equals(estSerie.getCodProg())) {
                    isReqSerie = Boolean.FALSE;
                }
            }
        }
        if ((invArma.getSerie() == null || StringUtil.VACIO.equals(invArma.getSerie().trim())) && isReqSerie) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeValCrearArmaSerie"));
            validacion = false;
        }
        if (invArma.getAlmacenSucamecId() == null) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeValCrearArmaAlmacen"));
            validacion = false;
        }
        if (invArma.getModeloId() == null) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeValCrearArmaArma"));
            validacion = false;
        }
        if (invArma.getEstadoserieId() == null) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeValCrearArmaEstSerie"));
            validacion = false;
        }
        if (invArma.getEstadofuncionalId() == null) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeValCrearArmaEstArma"));
            validacion = false;
        }
        if (invArma.getEstadoconservacionId() == null) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeValCrearArmaEstConser"));
            validacion = false;
        }
        if (invArma.getNroRua() != null) {
            if (!invArma.getNroRua().isEmpty() && !Objects.equals(nroRuaTemp, invArma.getNroRua())) {
                if (!accionArma.equals(EDITAR_ARMA) && ejbAmaInventarioArmaFacade.buscarExistenciaNroRua(invArma.getNroRua(), Boolean.FALSE)) {
                    JsfUtil.mensajeError(JsfUtil.bundle("MensajeValCrearArmaNroRuaExiste"));
                    validacion = false;
                } else if (!validacionRUA(invArma.getNroRua().trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundle("MensajeValCrearArmaNroRuaNoFormato"));
                    validacion = false;
                }
            }
        }
        
        if (JsfUtil.isNullOrEmpty(lstFotos) || (lstFotos.size() < 2 || lstFotos.size() > 3)) {
            validacion = false;
            JsfUtil.mensajeError("Por favor, tome las 2 fotos solicitadas (serie y panorámica)");
        }
        
        return validacion;
    }

    public void regresar() {
        if (esRectificatoria) {
            if (actaRectificada != null) {
                amaGuiaTransitoGenericoController.cambiarEstadoCascadeActa(actaRectificada, JsfUtil.TRUE);
            }
        }
        estado = EstadoCrud.BUSCARGUIA;
    }

    /**
     * Validación del nro. RUA
     *
     * @param nroRUA Nro. RUA
     * @return Booleano de validación correcta o no del Nro. RUA
     * @author Gino Chávez
     * @version 1.0
     */
    public boolean validacionRUA(String nroRUA) {
        try {
            boolean validacion = true;
            String[] lstVariosRUA = nroRUA.toUpperCase().split("-");

            if (lstVariosRUA.length == 2) {
                if (!lstVariosRUA[0].contains("PE")) {
                    validacion = false;
                } else if (lstVariosRUA[1].length() == 7) {
                    String ruaHex = lstVariosRUA[1].substring(0, 6);
                    String ruaCodVerif = lstVariosRUA[1].substring(6, 7);
                    String ruaDec = StringUtil.VACIO + Long.parseLong(ruaHex, 16);

                    if (!generarCodVerificador(ruaDec.trim()).equals(ruaCodVerif.trim())) {
                        validacion = false;
                    }
                } else {
                    validacion = false;
                }
            } else {
                validacion = false;
            }
            return validacion;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void handleFileUploadAdjunto(FileUploadEvent event) {
        try {
            if (event != null) {
                setFile(event.getFile());
                if (JsfUtil.verificarJPG(getFile())) {
                    setFotoByte(IOUtils.toByteArray(getFile().getInputstream()));
                    setFoto(new DefaultStreamedContent(getFile().getInputstream(), "image/jpeg"));
                    setArchivo(getFile().getFileName());
                    guardarFoto(JsfUtil.TRUE);
                    RequestContext.getCurrentInstance().update("createInvForm:tabCreateInv:tblFotos");
                } else {
                    setFile(null);
                    JsfUtil.mensajeAdvertencia("El archivo que trató de cargar no tiene formato JPG/JPEG");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarAnadirArma() {
        serieCita = null;
        nroLicCita = null;
        addArmaActa = Boolean.TRUE;
    }

    /**
     * Función al seleccionar el propietario del campo autocompletable
     *
     * @author Gino Chávez
     * @version 1.0
     * @param isRegInvArma
     */
    public void seleccionaPropietario(Boolean isRegInvArma) {
        if (isRegInvArma) {
            setRenderPropietario(Boolean.FALSE);
        } else {
            setRenderPropietario2(Boolean.FALSE);
        }
    }

    /**
     * Genera codigo de verificación con nro. RUA
     *
     * @param decimal8 Nro. RUA
     * @return Nro. verificador
     * @author Gino Chávez
     * @version 1.0
     */
    public String generarCodVerificador(String decimal8) {
        String strVerificador = StringUtil.VACIO;
        while (decimal8.length() < 8) {
            decimal8 = "0" + decimal8;
        }
        JsfUtil.ValidarDniRuc(decimal8, JsfUtil.TipoDoc.DNI);
        for (int i = 0; i <= 9; i++) {
            if (JsfUtil.ValidarDniRuc(decimal8 + i, JsfUtil.TipoDoc.DNI)) {
                strVerificador = StringUtil.VACIO + i;
            }
        }
        return strVerificador;
    }

    public void oncapture(CaptureEvent captureEvent) {
        fotoByte = captureEvent.getData();
        InputStream is = new ByteArrayInputStream(fotoByte);
        setFoto(new DefaultStreamedContent(is, "image/jpeg"));
        setFotoImg(new DefaultStreamedContent(is, "image/jpeg"));
//        RequestContext.getCurrentInstance().update("createInvForm:pnlFotosArma");
    }

    public void cancelarSeleccionDir() {
        selectedAmaArma = null;
        propietario = null;
        RequestContext.getCurrentInstance().update("registroForm:gridPropietario");
        RequestContext.getCurrentInstance().update("editarForm:gridPropietario");
        RequestContext.getCurrentInstance().update("registroForm:gridDatosArma");
        RequestContext.getCurrentInstance().update("editarForm:gridDatosArma");
        RequestContext.getCurrentInstance().execute("PF('IngresoDirSucViewDialog').hide()");
    }

    public void cerrarCrearInv() {
//        if (isOrgAlmacen) {
        if (!accionArma.equals(EDITAR_ARMA)) {
            selectedAmaArma = null;
            propietario = null;
        }
//        } else {
//            selectedAmaArma = null;
//        }
        RequestContext.getCurrentInstance().execute("PF('CrearInvenArmaViewDialog').hide()");
        RequestContext.getCurrentInstance().update("registroForm:gridDatosArma");
        RequestContext.getCurrentInstance().update("editarForm:gridDatosArma");

        RequestContext.getCurrentInstance().update("registroForm:gridPropietario");
        RequestContext.getCurrentInstance().update("editarForm:gridPropietario");
    }

    /**
     * FUNCIÓN QUE INICIALIZA LAS VARIABLES PARA LA CONSULTA DE ACTAS DE
     * DEPOSITO Y DEVOLUCION.
     *
     * @author Gino Chávez
     * @return PÁGINA
     */
    public String prepareListConsultaActas() {
        tipoAlmaFisico = DEPOSITO;
        actualizarVista();
        perfilConsulta = Boolean.FALSE;
        obtenerCantPerfiles();
        idAreaArma = usuLogueado.getAreaId() == null ? L_CERO : usuLogueado.getAreaId().getId();
//        userSession = JsfUtil.getLoggedUser().getLogin();
        idUsuarioTramDoc = (usuarioTDFacade.obtenerIdUsuarioTD(loginController.getUsuario().getLogin())).get(0);
        estado = EstadoCrud.BUSCARACTA;
//        isOrgAlmacen = Boolean.FALSE;
        isConsulta = Boolean.TRUE;
        dirSucamecBus = null;
        modoBus = null;
        listTipoGuiaDep = null;
        idTipoGuiaDep = null;
        fechaEmiActa = null;
        return "/aplicacion/amaDepositoArmas/ListConsultaDepDev";
    }

    public List<UnidadMedida> listaUnidadMedida() {
        return ejbUnidadMedidaFacade.selectKilogramo();
    }

    /**
     * @return the listRepresentante
     */
    public List<SbPersona> getListRepresentante() {
        if (solicitante != null) {
            return ejbSbPersonaFacade.obtenerRepresentantes(solicitante.getId());
        }
        return null;
    }

    //FIN
    public String fechaSimple(Date fecha) {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
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
        codProg = "TP_GTGAMAC_ALM";
//        } else if (devolucion.equalsIgnoreCase(tipoAlmaFisico)) {
//        codProg = "TP_GTGAMAC_DEV";
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

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public SbPersona getNewPersona() {
        return newPersona;
    }

    public void setNewPersona(SbPersona newPersona) {
        this.newPersona = newPersona;
    }

    public String getCantNumeros() {
        return cantNumeros;
    }

    public void setCantNumeros(String cantNumeros) {
        this.cantNumeros = cantNumeros;
    }

    public Boolean getDisabledBtVal() {
        return disabledBtVal;
    }

    public void setDisabledBtVal(Boolean disabledBtVal) {
        this.disabledBtVal = disabledBtVal;
    }

    public SbPersona getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(SbPersona solicitante) {
        this.solicitante = solicitante;
    }

    public SbPersona getRepreSelect() {
        return repreSelect;
    }

    public void setRepreSelect(SbPersona repreSelect) {
        this.repreSelect = repreSelect;
    }

    public SbPersonaGt getRepresentante() {
        return representante;
    }

    public void setRepresentante(SbPersonaGt representante) {
        this.representante = representante;
    }

    public List<AmaInventarioAccesorios> getLstAccesoriosActa() {
        return lstAccesoriosActa;
    }

    public void setLstAccesoriosActa(List<AmaInventarioAccesorios> lstAccesoriosActa) {
        this.lstAccesoriosActa = lstAccesoriosActa;
    }

    public List<AmaGuiaMuniciones> getListGuiaMunicion() {
        return listGuiaMunicion;
    }

    public void setListGuiaMunicion(List<AmaGuiaMuniciones> listGuiaMunicion) {
        this.listGuiaMunicion = listGuiaMunicion;
    }

    public Boolean getFlagCollapsed() {
        return flagCollapsed;
    }

    public void setFlagCollapsed(Boolean flagCollapsed) {
        this.flagCollapsed = flagCollapsed;
    }

    public AmaGuiaTransito getActaDepositoSelectL() {
        return actaDepositoSelectL;
    }

    public void setActaDepositoSelectL(AmaGuiaTransito actaDepositoSelectL) {
        this.actaDepositoSelectL = actaDepositoSelectL;
    }

    public AmaGuiaTransito getActaDevolucionSelectL() {
        return actaDevolucionSelectL;
    }

    public void setActaDevolucionSelectL(AmaGuiaTransito actaDevolucionSelectL) {
        this.actaDevolucionSelectL = actaDevolucionSelectL;
    }

    public TipoGamac getSubTipoSelect() {
        return subTipoSelect;
    }

    public Boolean getSelTipoArma() {
        return selTipoArma;
    }

    public void setSelTipoArma(Boolean selTipoArma) {
        this.selTipoArma = selTipoArma;
    }

    public String getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(String tipoArma) {
        this.tipoArma = tipoArma;
    }

    public void setSubTipoSelect(TipoGamac subTipoSelect) {
        this.subTipoSelect = subTipoSelect;
    }

    public TipoGamac getSubSubTipoSelect() {
        return subSubTipoSelect;
    }

    public void setSubSubTipoSelect(TipoGamac subSubTipoSelect) {
        this.subSubTipoSelect = subSubTipoSelect;
    }

    public AmaDocumento getDocumentoSelect() {
        return documentoSelect;
    }

    public void setDocumentoSelect(AmaDocumento documentoSelect) {
        this.documentoSelect = documentoSelect;
    }

    public Documento getDocumentoCancelacionSelect() {
        return documentoCancelacionSelect;
    }

    public void setDocumentoCancelacionSelect(Documento documentoCancelacionSelect) {
        this.documentoCancelacionSelect = documentoCancelacionSelect;
    }
    
    public Documento getDocumentoRgInDefSelect() {
        return documentoRgInDefSelect;
    }

    public void setDocumentoRgInDefSelect(Documento documentoRgInDefSelect) {
        this.documentoRgInDefSelect = documentoRgInDefSelect;
    }

    public String getSelectedAmaArma() {
        return selectedAmaArma;
    }

    public void setSelectedAmaArma(String selectedAmaArma) {
        this.selectedAmaArma = selectedAmaArma;
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

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public List<AmaInventarioAccesorios> getListAccesoriosGuia() {
        return listAccesoriosGuia;
    }

    public void setListAccesoriosGuia(List<AmaInventarioAccesorios> listAccesoriosGuia) {
        this.listAccesoriosGuia = listAccesoriosGuia;
    }

    public List<Map> getListaArmasFinalTemp() {
        return listaArmasFinalTemp;
    }

    public void setListaArmasFinalTemp(List<Map> listaArmasFinalTemp) {
        this.listaArmasFinalTemp = listaArmasFinalTemp;
    }

    public TurPersona getSolDeposito() {
        return solDeposito;
    }

    public void setSolDeposito(TurPersona solDeposito) {
        this.solDeposito = solDeposito;
    }

    public List<TurTurno> getListTurnosCita() {
        return listTurnosCita;
    }

    public void setListTurnosCita(List<TurTurno> listTurnosCita) {
        this.listTurnosCita = listTurnosCita;
    }

    public List<TurTurno> getListTurnosCitaFiltro() {
        return listTurnosCitaFiltro;
    }

    public void setListTurnosCitaFiltro(List<TurTurno> listTurnosCitaFiltro) {
        this.listTurnosCitaFiltro = listTurnosCitaFiltro;
    }

    public TurTurno getTurnoDeposito() {
        return turnoDeposito;
    }

    public void setTurnoDeposito(TurTurno turnoDeposito) {
        this.turnoDeposito = turnoDeposito;
    }

    public List<String> getListHoraTurno() {
        return listHoraTurno;
    }

    public void setListHoraTurno(List<String> listHoraTurno) {
        this.listHoraTurno = listHoraTurno;
    }

    public String getDocSolicitanteCita() {
        return docSolicitanteCita;
    }

    public void setDocSolicitanteCita(String docSolicitanteCita) {
        this.docSolicitanteCita = docSolicitanteCita;
    }

    public Long getOpcDepositoSelect() {
        return opcDepositoSelect;
    }

    public void setOpcDepositoSelect(Long opcDepositoSelect) {
        this.opcDepositoSelect = opcDepositoSelect;
    }

    public String getMsjRegSolicitante() {
        return msjRegSolicitante;
    }

    public void setMsjRegSolicitante(String msjRegSolicitante) {
        this.msjRegSolicitante = msjRegSolicitante;
    }

    public AmaGuiaTransito getAmaGuiaSelect() {
        return amaGuiaSelect;
    }

    public void setAmaGuiaSelect(AmaGuiaTransito amaGuiaSelect) {
        this.amaGuiaSelect = amaGuiaSelect;
    }

    public Boolean getEsRectificatoria() {
        return esRectificatoria;
    }

    public void setEsRectificatoria(Boolean esRectificatoria) {
        this.esRectificatoria = esRectificatoria;
    }

    public AmaGuiaTransito getActaRectificada() {
        return actaRectificada;
    }

    public void setActaRectificada(AmaGuiaTransito actaRectificada) {
        this.actaRectificada = actaRectificada;
    }

    public Boolean getDisableGuardar() {
        return disableGuardar;
    }

    public void setDisableGuardar(Boolean disableGuardar) {
        this.disableGuardar = disableGuardar;
    }

    public List<AmaInventarioAccesorios> getLstAccesorios() {
        return lstAccesorios;
    }

    public void setLstAccesorios(List<AmaInventarioAccesorios> lstAccesorios) {
        this.lstAccesorios = lstAccesorios;
    }

    public AmaInventarioArma getCopiaInvArmaTemp() {
        return copiaInvArmaTemp;
    }

    public void setCopiaInvArmaTemp(AmaInventarioArma copiaInvArmaTemp) {
        this.copiaInvArmaTemp = copiaInvArmaTemp;
    }

    public Boolean getDisableModo() {
        return disableModo;
    }

    public void setDisableModo(Boolean disableModo) {
        this.disableModo = disableModo;
    }

    public Boolean getDisableTipoDep() {
        return disableTipoDep;
    }

    public void setDisableTipoDep(Boolean disableTipoDep) {
        this.disableTipoDep = disableTipoDep;
    }

    public TipoGamac getModoSelect() {
        return modoSelect;
    }

    public void setModoSelect(TipoGamac modoSelect) {
        this.modoSelect = modoSelect;
    }

    public SbPersona getSolicitanteTemp() {
        return solicitanteTemp;
    }

    public void setSolicitanteTemp(SbPersona solicitanteTemp) {
        this.solicitanteTemp = solicitanteTemp;
    }

    public TipoGamac getTipoOrigenSelect() {
        return tipoOrigenSelect;
    }

    public void setTipoOrigenSelect(TipoGamac tipoOrigenSelect) {
        this.tipoOrigenSelect = tipoOrigenSelect;
    }

    public Boolean getReqDependencia1() {
        return reqDependencia1;
    }

    public void setReqDependencia1(Boolean reqDependencia1) {
        this.reqDependencia1 = reqDependencia1;
    }

    public Boolean getReqDependencia2() {
        return reqDependencia2;
    }

    public void setReqDependencia2(Boolean reqDependencia2) {
        this.reqDependencia2 = reqDependencia2;
    }

    public TipoGamac getTipoDestinoSelect() {
        return tipoDestinoSelect;
    }

    public void setTipoDestinoSelect(TipoGamac tipoDestinoSelect) {
        this.tipoDestinoSelect = tipoDestinoSelect;
    }

    public SbDireccion getSelectedDestSucamec() {
        return selectedDestSucamec;
    }

    public void setSelectedDestSucamec(SbDireccion selectedDestSucamec) {
        this.selectedDestSucamec = selectedDestSucamec;
    }

    public List<AmaInventarioArma> getListCopiaInvArmaTemp() {
        return listCopiaInvArmaTemp;
    }

    public void setListCopiaInvArmaTemp(List<AmaInventarioArma> listCopiaInvArmaTemp) {
        this.listCopiaInvArmaTemp = listCopiaInvArmaTemp;
    }

    public SbDireccion getSbDireccionOrigen() {
        return sbDireccionOrigen;
    }

    public void setSbDireccionOrigen(SbDireccion sbDireccionOrigen) {
        this.sbDireccionOrigen = sbDireccionOrigen;
    }

    public SbDireccion getSelectedOriSucamec() {
        return selectedOriSucamec;
    }

    public void setSelectedOriSucamec(SbDireccion selectedOriSucamec) {
        this.selectedOriSucamec = selectedOriSucamec;
    }

    public SbDireccion getSelectedOriOtro() {
        return selectedOriOtro;
    }

    public void setSelectedOriOtro(SbDireccion selectedOriOtro) {
        this.selectedOriOtro = selectedOriOtro;
    }

    public TipoGamac getProcedenciaSelect() {
        return procedenciaSelect;
    }

    public void setProcedenciaSelect(TipoGamac procedenciaSelect) {
        this.procedenciaSelect = procedenciaSelect;
    }

    public TipoGamac getInstOrdenaSelect() {
        return instOrdenaSelect;
    }

    public void setInstOrdenaSelect(TipoGamac instOrdenaSelect) {
        this.instOrdenaSelect = instOrdenaSelect;
    }

    public String getDependenciaProc() {
        return dependenciaProc;
    }

    public void setDependenciaProc(String dependenciaProc) {
        this.dependenciaProc = dependenciaProc;
    }

    public String getDependenciaInst() {
        return dependenciaInst;
    }

    public void setDependenciaInst(String dependenciaInst) {
        this.dependenciaInst = dependenciaInst;
    }

//    public List<TipoGamac> getListMotivoIn() {
//        return listMotivoIn;
//    }
//
//    public void setListMotivoIn(List<TipoGamac> listMotivoIn) {
//        this.listMotivoIn = listMotivoIn;
//    }
    public TipoGamac getMotivoInSelect() {
        return motivoInSelect;
    }

    public void setMotivoInSelect(TipoGamac motivoInSelect) {
        this.motivoInSelect = motivoInSelect;
    }

    public List<SbPersona> getListInfractores() {
        return listInfractores;
    }

    public void setListInfractores(List<SbPersona> listInfractores) {
        this.listInfractores = listInfractores;
    }

    public List<SbPersona> getListInfractoresSelect() {
        return listInfractoresSelect;
    }

    public void setListInfractoresSelect(List<SbPersona> listInfractoresSelect) {
        this.listInfractoresSelect = listInfractoresSelect;
    }

    public String getObsDeposito() {
        return obsDeposito;
    }

    public void setObsDeposito(String obsDeposito) {
        this.obsDeposito = obsDeposito;
    }

    public String getAccionArma() {
        return accionArma;
    }

    public void setAccionArma(String accionArma) {
        this.accionArma = accionArma;
    }

    public String getNroExpActa() {
        return nroExpActa;
    }

    public void setNroExpActa(String nroExpActa) {
        this.nroExpActa = nroExpActa;
    }

    public SbPersona getPropietario() {
        return propietario;
    }

    public void setPropietario(SbPersona propietario) {
        this.propietario = propietario;
    }

    public Map getArmaDisca() {
        return armaDisca;
    }

    public void setArmaDisca(Map armaDisca) {
        this.armaDisca = armaDisca;
    }

    public Map getArmaLic() {
        return armaLic;
    }

    public void setArmaLic(Map armaLic) {
        this.armaLic = armaLic;
    }

    public SbRecibos getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(SbRecibos comprobantePago) {
        this.comprobantePago = comprobantePago;
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

    public Expediente getExpActaDeposito() {
        return expActaDeposito;
    }

    public void setExpActaDeposito(Expediente expActaDeposito) {
        this.expActaDeposito = expActaDeposito;
    }

    public AmaCatalogo getTpArticuloSelect() {
        return tpArticuloSelect;
    }

    public void setTpArticuloSelect(AmaCatalogo tpArticuloSelect) {
        this.tpArticuloSelect = tpArticuloSelect;
    }

    public String getTipoArtMostrar() {
        return tipoArtMostrar;
    }

    public void setTipoArtMostrar(String tipoArtMostrar) {
        this.tipoArtMostrar = tipoArtMostrar;
    }

    public String getTipoArtLibre() {
        return tipoArtLibre;
    }

    public void setTipoArtLibre(String tipoArtLibre) {
        this.tipoArtLibre = tipoArtLibre;
    }

    public List<Map> getListGuiasTrasladoSelect() {
        return listGuiasTrasladoSelect;
    }

    public void setListGuiasTrasladoSelect(List<Map> listGuiasTrasladoSelect) {
        this.listGuiasTrasladoSelect = listGuiasTrasladoSelect;
    }

    public String getNumExpGuia() {
        return numExpGuia;
    }

    public void setNumExpGuia(String numExpGuia) {
        this.numExpGuia = numExpGuia;
    }

    public List<AmaInventarioAccesorios> getListInvAcceMostrar() {
        return listInvAcceMostrar;
    }

    public void setListInvAcceMostrar(List<AmaInventarioAccesorios> listInvAcceMostrar) {
        this.listInvAcceMostrar = listInvAcceMostrar;
    }

    public List<AmaInventarioAccesorios> getListAccesArma() {
        return listAccesArma;
    }

    public void setListAccesArma(List<AmaInventarioAccesorios> listAccesArma) {
        this.listAccesArma = listAccesArma;
    }

    public String getMsjConfirmacionDeposito() {
        return msjConfirmacionDeposito;
    }

    public void setMsjConfirmacionDeposito(String msjConfirmacionDeposito) {
        this.msjConfirmacionDeposito = msjConfirmacionDeposito;
    }

    public ListDataModel<Map> getListaArmasFinal() {
        return listaArmasFinal;
    }

    public void setListaArmasFinal(ListDataModel<Map> listaArmasFinal) {
        this.listaArmasFinal = listaArmasFinal;
    }

    public Boolean getExisteCitaOk() {
        return existeCitaOk;
    }

    public void setExisteCitaOk(Boolean existeCitaOk) {
        this.existeCitaOk = existeCitaOk;
    }

    public Boolean getFlagCrearConArmaInex() {
        return flagCrearConArmaInex;
    }

    public void setFlagCrearConArmaInex(Boolean flagCrearConArmaInex) {
        this.flagCrearConArmaInex = flagCrearConArmaInex;
    }

    public Boolean getFlagCrearSinCita() {
        return flagCrearSinCita;
    }

    public void setFlagCrearSinCita(Boolean flagCrearSinCita) {
        this.flagCrearSinCita = flagCrearSinCita;
    }

    public Long getIdCriterioLic() {
        return idCriterioLic;
    }

    public void setIdCriterioLic(Long idCriterioLic) {
        this.idCriterioLic = idCriterioLic;
    }

    public String getValorBusLic() {
        return valorBusLic;
    }

    public void setValorBusLic(String valorBusLic) {
        this.valorBusLic = valorBusLic;
    }

    public Boolean getRenderX() {
        return renderX;
    }

    public void setRenderX(Boolean renderX) {
        this.renderX = renderX;
    }

    public SbPersona getSolicitanteCita() {
        return solicitanteCita;
    }

    public void setSolicitanteCita(SbPersona solicitanteCita) {
        this.solicitanteCita = solicitanteCita;
    }

    public List<Map> getListArmaLic() {
        return listArmaLic;
    }

    public void setListArmaLic(List<Map> listArmaLic) {
        this.listArmaLic = listArmaLic;
    }

    public TipoGamac getTipoInterSelect() {
        return tipoInterSelect;
    }

    public void setTipoInterSelect(TipoGamac tipoInterSelect) {
        this.tipoInterSelect = tipoInterSelect;
    }

    public Long getIdOrigenTipoGuia() {
        return idOrigenTipoGuia;
    }

    public void setIdOrigenTipoGuia(Long idOrigenTipoGuia) {
        this.idOrigenTipoGuia = idOrigenTipoGuia;
    }

    public Long getIdListaEliminar() {
        return idListaEliminar;
    }

    public void setIdListaEliminar(Long idListaEliminar) {
        this.idListaEliminar = idListaEliminar;
    }

    public List<TipoGamac> getListIdSubTipoGuiaSelect() {
        return listIdSubTipoGuiaSelect;
    }

    public void setListIdSubTipoGuiaSelect(List<TipoGamac> listIdSubTipoGuiaSelect) {
        this.listIdSubTipoGuiaSelect = listIdSubTipoGuiaSelect;
    }

    public Boolean getRenderPropietario() {
        return renderPropietario;
    }

    public void setRenderPropietario(Boolean renderPropietario) {
        this.renderPropietario = renderPropietario;
    }

    public Boolean getExisteArmaInList() {
        return existeArmaInList;
    }

    public void setExisteArmaInList(Boolean existeArmaInList) {
        this.existeArmaInList = existeArmaInList;
    }

    public BigDecimal getIdUsuarioTramDoc() {
        return idUsuarioTramDoc;
    }

    public void setIdUsuarioTramDoc(BigDecimal idUsuarioTramDoc) {
        this.idUsuarioTramDoc = idUsuarioTramDoc;
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

    public List<SbRecibos> getLstRecibosBusqueda() {
        return lstRecibosBusqueda;
    }

    public void setLstRecibosBusqueda(List<SbRecibos> lstRecibosBusqueda) {
        this.lstRecibosBusqueda = lstRecibosBusqueda;
    }

    public AmaInventarioArma getNewInvArma() {
        return newInvArma;
    }

    public void setNewInvArma(AmaInventarioArma newInvArma) {
        this.newInvArma = newInvArma;
    }

    public void cargarListSTGuia() {
        listSTGuia = ejbTipoGamacFacade.selectTipoGamac(idTipoGuia.getCodProg());
        List<TipoGamac> listTemp = new ArrayList(listSTGuia);
        for (TipoGamac tg : listTemp) {
            if (("TP_GTSUC_SOLADM".equals(tg.getCodProg()) && isSolExpSucamec)
                    || ("TP_GTSUC_INISUC".equals(tg.getCodProg()) && !isSolExpSucamec)) {
                listSTGuia.remove(tg);
            }
        }
        if (amaGuiaTransitoGenericoController.fueraHorario()) {
            for (TipoGamac tgDep : listTemp) {
                if (/*"TP_GTALM_FALTIT".equals(tgDep.getCodProg())
                        || */"TP_GTALM_DISADU".equals(tgDep.getCodProg())
                        || "TP_GTALM_VENCAN".equals(tgDep.getCodProg())
                        || "TP_GTALM_VENPLA".equals(tgDep.getCodProg())) {
                    listSTGuia.remove(tgDep);
                }
            }
        }
    }

    public void setListSTGuia(List<TipoGamac> listSTGuia) {
        this.listSTGuia = listSTGuia;
    }

    public List<TipoGamac> getListSubSTGuia() {
        listSubSTGuia = ejbTipoGamacFacade.selectTipoGamac(subTipoSelect.getCodProg());
        return listSubSTGuia;
    }

    public void setListSubSTGuia(List<TipoGamac> listSubSTGuia) {
        this.listSubSTGuia = listSubSTGuia;
    }

    public SbPersona getInfractorSelect() {
        return infractorSelect;
    }

    public void setInfractorSelect(SbPersona infractorSelect) {
        this.infractorSelect = infractorSelect;
    }

    public AmaDocumento getNewDocumento() {
        return newDocumento;
    }

    public void setNewDocumento(AmaDocumento newDocumento) {
        this.newDocumento = newDocumento;
    }

    public List<TipoGamac> getListOrigen() {
        return listOrigen;
    }

    public void setListOrigen(List<TipoGamac> listOrigen) {
        this.listOrigen = listOrigen;
    }

    public SbDireccion getSelectedOriLocal() {
        return selectedOriLocal;
    }

    public void setSelectedOriLocal(SbDireccion selectedOriLocal) {
        this.selectedOriLocal = selectedOriLocal;
    }

    public List<TipoGamac> getListDestino() {
        return listDestino;
    }

    public void setListDestino(List<TipoGamac> listDestino) {
        this.listDestino = listDestino;
    }

    public SbDireccion getSbDireccionDest() {
        return sbDireccionDest;
    }

    public void setSbDireccionDest(SbDireccion sbDireccionDest) {
        this.sbDireccionDest = sbDireccionDest;
    }

    public Boolean getIsSolExpSucamec() {
        return isSolExpSucamec;
    }

    public void setIsSolExpSucamec(Boolean isSolExpSucamec) {
        this.isSolExpSucamec = isSolExpSucamec;
    }

    public Boolean getResInvArma() {
        return resInvArma;
    }

    public void setResInvArma(Boolean resInvArma) {
        this.resInvArma = resInvArma;
    }

    public TipoBaseGt getTipoOpeSelect() {
        return tipoOpeSelect;
    }

    public void setTipoOpeSelect(TipoBaseGt tipoOpeSelect) {
        this.tipoOpeSelect = tipoOpeSelect;
    }

    public AmaGuiaTransito getRegistro() {
        return registro;
    }

    public void setRegistro(AmaGuiaTransito registro) {
        this.registro = registro;
    }

    public String getNroRuaTemp() {
        return nroRuaTemp;
    }

    public void setNroRuaTemp(String nroRuaTemp) {
        this.nroRuaTemp = nroRuaTemp;
    }

    public String getSelectedArmaString() {
        return selectedArmaString;
    }

    public void setSelectedArmaString(String selectedArmaString) {
        this.selectedArmaString = selectedArmaString;
    }

    public boolean isDisabledEstadoSerie() {
        return disabledEstadoSerie;
    }

    public void setDisabledEstadoSerie(boolean disabledEstadoSerie) {
        this.disabledEstadoSerie = disabledEstadoSerie;
    }

    public Boolean getRenderPropietario2() {
        return renderPropietario2;
    }

    public void setRenderPropietario2(Boolean renderPropietario2) {
        this.renderPropietario2 = renderPropietario2;
    }

    public boolean isRenderBtnKitPistola() {
        return renderBtnKitPistola;
    }

    public void setRenderBtnKitPistola(boolean renderBtnKitPistola) {
        this.renderBtnKitPistola = renderBtnKitPistola;
    }

    public boolean isRenderBtnKitRevolver() {
        return renderBtnKitRevolver;
    }

    public void setRenderBtnKitRevolver(boolean renderBtnKitRevolver) {
        this.renderBtnKitRevolver = renderBtnKitRevolver;
    }

    public boolean isRenderBtnGuardar() {
        return renderBtnGuardar;
    }

    public void setRenderBtnGuardar(boolean renderBtnGuardar) {
        this.renderBtnGuardar = renderBtnGuardar;
    }

    public Boolean getTieneResolucion() {
        return tieneResolucion;
    }

    public void setTieneResolucion(Boolean tieneResolucion) {
        this.tieneResolucion = tieneResolucion;
    }

    public List<Map> getLstPropietario() {
        return lstPropietario;
    }

    public void setLstPropietario(List<Map> lstPropietario) {
        this.lstPropietario = lstPropietario;
    }

    public List<AmaInventarioAccesorios> getLstEliminadosAccesorios() {
        return lstEliminadosAccesorios;
    }

    public void setLstEliminadosAccesorios(List<AmaInventarioAccesorios> lstEliminadosAccesorios) {
        this.lstEliminadosAccesorios = lstEliminadosAccesorios;
    }

    public List<AmaInventarioAccesorios> getLstAccesoriosActaSelect() {
        return lstAccesoriosActaSelect;
    }

    public void setLstAccesoriosActaSelect(List<AmaInventarioAccesorios> lstAccesoriosActaSelect) {
        this.lstAccesoriosActaSelect = lstAccesoriosActaSelect;
    }

    public AmaInventarioArtconexo getAccesorio() {
        return accesorio;
    }

    public void setAccesorio(AmaInventarioArtconexo accesorio) {
        this.accesorio = accesorio;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public AmaInventarioArtconexo getAccesorioActa() {
        return accesorioActa;
    }

    public void setAccesorioActa(AmaInventarioArtconexo accesorioActa) {
        this.accesorioActa = accesorioActa;
    }

    public Long getCantAccesActa() {
        return cantAccesActa;
    }

    public void setCantAccesActa(Long cantAccesActa) {
        this.cantAccesActa = cantAccesActa;
    }

    public AmaModelos getModeloAnterior() {
        return modeloAnterior;
    }

    public void setModeloAnterior(AmaModelos modeloAnterior) {
        this.modeloAnterior = modeloAnterior;
    }

    public String getMsjeRegistro() {
        return msjeRegistro;
    }

    public void setMsjeRegistro(String msjeRegistro) {
        this.msjeRegistro = msjeRegistro;
    }

    public int getTipoMensajeValidacion() {
        return tipoMensajeValidacion;
    }

    public void setTipoMensajeValidacion(int tipoMensajeValidacion) {
        this.tipoMensajeValidacion = tipoMensajeValidacion;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Boolean getAdjuntarFoto() {
        return adjuntarFoto;
    }

    public void setAdjuntarFoto(Boolean adjuntarFoto) {
        this.adjuntarFoto = adjuntarFoto;
    }

    public GamacAmaMunicion getAmaMuniSelect() {
        return amaMuniSelect;
    }

    public void setAmaMuniSelect(GamacAmaMunicion amaMuniSelect) {
        this.amaMuniSelect = amaMuniSelect;
    }

    public Double getCantMuni() {
        return cantMuni;
    }

    public void setCantMuni(Double cantMuni) {
        this.cantMuni = cantMuni;
    }

    public UnidadMedida getUniMedidaMuni() {
        return uniMedidaMuni;
    }

    public void setUniMedidaMuni(UnidadMedida uniMedidaMuni) {
        this.uniMedidaMuni = uniMedidaMuni;
    }

    public Double getPesoMuni() {
        return pesoMuni;
    }

    public void setPesoMuni(Double pesoMuni) {
        this.pesoMuni = pesoMuni;
    }

    public List<AmaGuiaMuniciones> getListGuiaMunicionSelect() {
        return listGuiaMunicionSelect;
    }

    public void setListGuiaMunicionSelect(List<AmaGuiaMuniciones> listGuiaMunicionSelect) {
        this.listGuiaMunicionSelect = listGuiaMunicionSelect;
    }

    public String getValorBusDisca() {
        return valorBusDisca;
    }

    public void setValorBusDisca(String valorBusDisca) {
        this.valorBusDisca = valorBusDisca;
    }

    public StreamedContent getFotoImg() {
        return fotoImg;
    }

    public void setFotoImg(StreamedContent fotoImg) {
        this.fotoImg = fotoImg;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Boolean getAddArmaActa() {
        return addArmaActa;
    }

    public void setAddArmaActa(Boolean addArmaActa) {
        this.addArmaActa = addArmaActa;
    }

    public String getSerieCita() {
        return serieCita;
    }

    public void setSerieCita(String serieCita) {
        this.serieCita = serieCita;
    }

    public String getNroLicCita() {
        return nroLicCita;
    }

    public void setNroLicCita(String nroLicCita) {
        this.nroLicCita = nroLicCita;
    }

    public Date getFechaArmaJud() {
        return fechaArmaJud;
    }

    public void setFechaArmaJud(Date fechaArmaJud) {
        this.fechaArmaJud = fechaArmaJud;
    }

    public TipoBaseGt getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(TipoBaseGt estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public TipoGamac getAmbienteSelect() {
        return ambienteSelect;
    }

    public void setAmbienteSelect(TipoGamac ambienteSelect) {
        this.ambienteSelect = ambienteSelect;
    }

    public String getAnaquel() {
        return anaquel;
    }

    public void setAnaquel(String anaquel) {
        this.anaquel = anaquel;
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

    public String getMsjConfirmacionDevolucion() {
        return msjConfirmacionDevolucion;
    }

    public void setMsjConfirmacionDevolucion(String msjConfirmacionDevolucion) {
        this.msjConfirmacionDevolucion = msjConfirmacionDevolucion;
    }

    public Long getIdAreaArma() {
        return idAreaArma;
    }

    public void setIdAreaArma(Long idAreaArma) {
        this.idAreaArma = idAreaArma;
    }

    public Boolean getIsConsulta() {
        return isConsulta;
    }

    public void setIsConsulta(Boolean isConsulta) {
        this.isConsulta = isConsulta;
    }

    public String getMsjConfirmacionDevArma() {
        return msjConfirmacionDevArma;
    }

    public void setMsjConfirmacionDevArma(String msjConfirmacionDevArma) {
        this.msjConfirmacionDevArma = msjConfirmacionDevArma;
    }

    public Boolean getIsArmaEditable() {
        return isArmaEditable;
    }

    public void setIsArmaEditable(Boolean isArmaEditable) {
        this.isArmaEditable = isArmaEditable;
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

    public List<AmaInventarioArma> getListInvArmasSeleccion() {
        return listInvArmasSeleccion;
    }

    public void setListInvArmasSeleccion(List<AmaInventarioArma> listInvArmasSeleccion) {
        this.listInvArmasSeleccion = listInvArmasSeleccion;
    }

    public List<TipoGamac> getListSTGuia() {
        return listSTGuia;
    }

    public Boolean getActualizarTipoInt() {
        return actualizarTipoInt;
    }

    public void setActualizarTipoInt(Boolean actualizarTipoInt) {
        this.actualizarTipoInt = actualizarTipoInt;
    }

    public ListDataModel<Map> getListActasReporte() {
        return listActasReporte;
    }

    public void setListActasReporte(ListDataModel<Map> listActasReporte) {
        this.listActasReporte = listActasReporte;
    }

    public TipoGamac getSituacionBus() {
        return situacionBus;
    }

    public void setSituacionBus(TipoGamac situacionBus) {
        this.situacionBus = situacionBus;
    }

    public TipoGamac getEstadoBus() {
        return estadoBus;
    }

    public void setEstadoBus(TipoGamac estadoBus) {
        this.estadoBus = estadoBus;
    }

    public AmaCatalogo getTipoArticuloBus() {
        return tipoArticuloBus;
    }

    public void setTipoArticuloBus(AmaCatalogo tipoArticuloBus) {
        this.tipoArticuloBus = tipoArticuloBus;
    }

    public AmaCatalogo getTipoArmaBus() {
        return tipoArmaBus;
    }

    public void setTipoArmaBus(AmaCatalogo tipoArmaBus) {
        this.tipoArmaBus = tipoArmaBus;
    }

    public int getUltimoReg() {
        return ultimoReg;
    }

    public void setUltimoReg(int ultimoReg) {
        this.ultimoReg = ultimoReg;
    }

    public List<TipoGamac> getListSituacionBus() {
        return listSituacionBus;
    }

    public void setListSituacionBus(List<TipoGamac> listSituacionBus) {
        this.listSituacionBus = listSituacionBus;
    }

    public Boolean getIsJudicializada() {
        return isJudicializada;
    }

    public void setIsJudicializada(Boolean isJudicializada) {
        this.isJudicializada = isJudicializada;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public Date getFiltroFecha() {
        return filtroFecha;
    }

    public void setFiltroFecha(Date filtroFecha) {
        this.filtroFecha = filtroFecha;
    }

    public TurProgramacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(TurProgramacion programacion) {
        this.programacion = programacion;
    }

    public List<TurPersona> getListPersonaCita() {
        return listPersonaCita;
    }

    public void setListPersonaCita(List<TurPersona> listPersonaCita) {
        this.listPersonaCita = listPersonaCita;
    }

    public TurPersona getSelectedPersonaCita() {
        return selectedPersonaCita;
    }

    public void setSelectedPersonaCita(TurPersona selectedPersonaCita) {
        this.selectedPersonaCita = selectedPersonaCita;
    }

    public List<CitaDetalleArma> getLstDetalleArma() {
        return lstDetalleArma;
    }

    public void setLstDetalleArma(List<CitaDetalleArma> lstDetalleArma) {
        this.lstDetalleArma = lstDetalleArma;
    }

    public List<TurMunicion> getLstDetalleMunicion() {
        return lstDetalleMunicion;
    }

    public void setLstDetalleMunicion(List<TurMunicion> lstDetalleMunicion) {
        this.lstDetalleMunicion = lstDetalleMunicion;
    }    

    public CitaDetalleArma getDetalleArma() {
        return detalleArma;
    }

    public void setDetalleArma(CitaDetalleArma detalleArma) {
        this.detalleArma = detalleArma;
    }

    public List<TurTurno> getLstTurnosBusqueda() {
        return lstTurnosBusqueda;
    }

    public void setLstTurnosBusqueda(List<TurTurno> lstTurnosBusqueda) {
        this.lstTurnosBusqueda = lstTurnosBusqueda;
    }

    public TurTurno getTurnoSeleccionado() {
        return turnoSeleccionado;
    }

    public void setTurnoSeleccionado(TurTurno turnoSeleccionado) {
        this.turnoSeleccionado = turnoSeleccionado;
    }

    public String getHoraProgra() {
        return horaProgra;
    }

    public void setHoraProgra(String horaProgra) {
        this.horaProgra = horaProgra;
    }

    public boolean isVisiblePanelSeleccionCitas() {
        return visiblePanelSeleccionCitas;
    }

    public void setVisiblePanelSeleccionCitas(boolean visiblePanelSeleccionCitas) {
        this.visiblePanelSeleccionCitas = visiblePanelSeleccionCitas;
    }

    public Long getActaInternamientoMunicionId() {
        return actaInternamientoMunicionId;
    }

    public void setActaInternamientoMunicionId(Long actaInternamientoMunicionId) {
        this.actaInternamientoMunicionId = actaInternamientoMunicionId;
    }
    
    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "amaDepositoArmasConverter")
    public static class AmaDepositoArmasControllerConverterN extends AmaDepositoArmasControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = AmaGuiaTransito.class)
    public static class AmaDepositoArmasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AmaDepositoArmasController c = (AmaDepositoArmasController) JsfUtil.obtenerBean("amaDepositoArmasController", AmaDepositoArmasController.class);
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

    public Boolean validarIngresoComprobante() {
        /*if (("TP_GTALM_MANVOL".equals(subTipoSelect.getCodProg()) && "TP_ING_TEM".equals(modoSelect.getCodProg()))
                || ("TP_GTALM_FALTIT".equals(subTipoSelect.getCodProg()) && isFlagFallec60dias()))
                || "TP_GTALM_VENCAN".equals(subTipoSelect.getCodProg())
                || "TP_GTALM_SEGUSO".equals(subTipoSelect.getCodProg())) {*/
        if (validarShowEmpoce()) {
            if (comprobantePago == null) {
                return Boolean.FALSE;
            }
        } else {
            comprobantePago = null;
        }
        return Boolean.TRUE;
    }

    public void openDlgVoucher() {
        RequestContext context = RequestContext.getCurrentInstance();
        /*Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 480);
        options.put("contentHeight", 430);
        // Modificar directorio y agregar archivo ListDialog //
        context.openDialog("dlgVoucher.xhtml", options, null);*/
        context.execute("PF('wvDlgHlpVoucher').show();");

    }

    public List<Documento> listadoDocumentosCydocIngDef() {
        List<Documento> listDocumentosTramdoc = new ArrayList();
        try {
            if (solicitante.getNumDoc() != null) {
                boolean buscaPorRuc = false;
                if (subTipoSelect != null) {
                    if (subTipoSelect.getCodProg().equals("TP_GTALM_INDEF")) {
                        buscaPorRuc = true;
                    }
                }
                listDocumentosTramdoc = ejbDocumentoFacade.lisDocumentoTramdocPorNroDoc(solicitante.getNumDoc(), "4", "1610", buscaPorRuc);
                
            } else {
                JsfUtil.mensajeAdvertencia("Por favor ingrese el solicitante");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listDocumentosTramdoc;
    }

    public String obtenerDescripcionDocumento(Documento doc) {
        if (doc == null) {
            return null;
        }
        return doc.getNumero() + ", Fecha: " + JsfUtil.getFechaFormato(doc.getFechaCreacion(), "dd/MM/yyyy");
    }

    public void actualizarDocIngDef() {
        if (documentoRgInDefSelect != null) {
            AmaDocumento d = new AmaDocumento();
            d.setId(null);
            d.setActivo(JsfUtil.TRUE);
            d.setNroExpediente(documentoRgInDefSelect.getExpediente().getNumero());
            d.setUsuarioCreacionId(usuLogueado);
            d.setAreaId(usuLogueado.getAreaId());
            d.setPersonaId(solicitante);
            d.setFechaCreacion(new Date());
            d.setEventoTrazaDocId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EVETR_CRE"));
            d.setAsunto("RG INGRESO DEFINITIVO AL PAIS");
            d.setTipoDocumentoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_DOCUM_RES"));
            d.setFechaFirma(documentoRgInDefSelect.getFechaCreacion());
            d.setNumero(documentoRgInDefSelect.getNumero());
            d = (AmaDocumento) JsfUtil.entidadMayusculas(d, StringUtil.VACIO);
            documentoSelect = d;
        }
    }

    public void verificarArmaId(Long idActaDep) {
        AmaGuiaTransito acta = ejbAmaGuiaTransitoFacade.find(idActaDep == null ? 0 : idActaDep);
        AmaInventarioArma ia = null;
        if (acta != null && (acta.getAmaInventarioArmaList() != null && !acta.getAmaInventarioArmaList().isEmpty())) {
            for (AmaInventarioArma invArma : acta.getAmaInventarioArmaList()) {
                if (invArma.getActivo() == JsfUtil.TRUE) {
                    ia = invArma;
                    break;
                }
            }
            if (ia != null) {
                if (ia.getSerie() != null && ia.getNroRua() != null && ia.getArmaId() == null && ia.getModeloId() != null) {
                    AmaArma arma = ejbAmaArmaFacade.obtenerArmaPorModeloSerie(ia.getModeloId().getId(), ia.getSerie(), ia.getNroRua());
                    if (arma != null) {
                        ia.setArmaId(arma);
                        ejbAmaInventarioArmaFacade.edit(ia);
                    }
                }
            }

        }
    }

    public void seleccionaPersonaCita() {
        actaInternamientoMunicionId = null;
        visiblePanelSeleccionCitas = false;
        lstTurnosBusqueda = new ArrayList<TurTurno>();
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        lstDetalleMunicion = new ArrayList<TurMunicion>();
        turnoSeleccionado = null;
    }
    
    public List<TurPersona> listarPersonasCitaHorario() {
        actaInternamientoMunicionId = null;
        visiblePanelSeleccionCitas = false;
        lstTurnosBusqueda = new ArrayList<TurTurno>();
        selectedPersonaCita = null;
        listPersonaCita = null;
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        lstDetalleMunicion = new ArrayList<TurMunicion>();
        turnoSeleccionado = null;
        
        if (hoy == null) {
            JsfUtil.mensajeError("Debe seleccionar la fecha");
            return null;
        }
        if (programacion != null) {
            SbUsuarioGt usuarioLog = ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin());
            SbTipo sede = new SbTipo();

            switch (usuarioLog.getAreaId().getCodProg()) {
                case "TP_AREA_GAMAC":
                case "TP_AREA_TRAM":
                    sede = ejbSbTipoFacade.buscarTipoBaseXCodProg("TP_AREA_TRAM");
                    break;
                default:
                    sede = usuarioLog.getAreaId();
                    break;
            }
            listPersonaCita = ejbTurPersonaFacade.buscarPersonaXCitaConfirmada(sede.getId(), "TP_PROG_EMP", hoy, programacion.getId());
            return listPersonaCita; 
        }    
        else {    
            return listPersonaCita; 
        }    
    }

    public void buscarBandejaCitas() {
        actaInternamientoMunicionId = null;
        visiblePanelSeleccionCitas = false;
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        lstDetalleMunicion = new ArrayList<TurMunicion>();
        turnoSeleccionado = null;
        horaProgra ="";
        
        Boolean buscaDatos = true;
        
        if (hoy == null) {
            JsfUtil.mensajeAdvertencia("Debe seleccionar la fecha");
            buscaDatos = false;
            return;
        }
        
        if (programacion == null) {
            JsfUtil.mensajeAdvertencia("Debe seleccionar el Horario");
            buscaDatos = false;
            return;
        }   

        if (selectedPersonaCita == null) {
            JsfUtil.mensajeAdvertencia("Debe seleccionar el Administrado");
            buscaDatos = false;
            return;
        }   

        if (buscaDatos) {
            SbUsuarioGt usuarioLog = ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin());
            SbTipo sede = new SbTipo();

            switch (usuarioLog.getAreaId().getCodProg()) {
                case "TP_AREA_GAMAC":
                case "TP_AREA_TRAM":
                    sede = ejbSbTipoFacade.buscarTipoBaseXCodProg("TP_AREA_TRAM");
                    break;
                default:
                    sede = usuarioLog.getAreaId();
                    break;
            }
            
            //Buscamos los turnos de la persona
            lstTurnosBusqueda = ejbTurTurnoFacade.buscarPersonaXCitaConfirmadaTurno(sede.getId(), "TP_PROG_EMP", hoy, programacion.getId(),selectedPersonaCita.getId());
            if (lstTurnosBusqueda.isEmpty()) {
                //lstTurnosBusqueda = null;
                //lstTurnosBusqueda = new ArrayList<TurTurno>();
            } else {
                //Si el Administrado solo tiene un turno confirmado en el dia en ese horario
                if (lstTurnosBusqueda.size() == 1) {
                    listaArmasMunicionesEmpadronamiento(lstTurnosBusqueda.get(0));
                    //lstTurnosBusqueda = null;
                    //lstTurnosBusqueda = new ArrayList<TurTurno>();
                } else {
                    //Si el Administrado tiene varios turnos confirmados en el dia en ese horario
                    //Debe de Seleccionarlo de la lista de horarios
                    visiblePanelSeleccionCitas = true;
                }
            }
        }    
    }
    
    
    public void listaArmasMunicionesEmpadronamiento(TurTurno reg){
        actaInternamientoMunicionId = null;
        if (reg != null) {
            turnoSeleccionado = reg;
            String horaProg = turnoSeleccionado.getProgramacionId().getHora();
            horaProgra = horaProg.substring(0, 5);
            //Armas
            lstDetalleArma = new ArrayList<CitaDetalleArma>();
            for (TurLicenciaReg det : reg.getTurLicenciaRegList()) {
                detalleArma = new CitaDetalleArma();
                detalleArma.setTurLicenciaRegId(det.getId());
                detalleArma.setTipoArma(det.getTipoArmaId().getNombre());
                detalleArma.setTipoInternamiento(det.getTipoInternamientoId().getNombre());
                detalleArma.setNroSerie(det.getSerie());
                detalleArma.setMarca(det.getMarcaNombre());
                detalleArma.setModelo(det.getModeloNombre());
                detalleArma.setCalibre(det.getCalibreNombre());
                if (det.getComprobanteId() != null) {
                    detalleArma.setComprobantePago(det.getComprobanteId());
                }
                detalleArma.setActaInternamientoId(det.getActaInternamientoId());
                lstDetalleArma.add(detalleArma); 
            }

            //Municiones
            TurMunicion detalleMunicionTemp;
            lstDetalleMunicion = new ArrayList<TurMunicion>();
            for (TurMunicion det : reg.getTurMunicionList()) {
                detalleMunicionTemp = new TurMunicion();
                detalleMunicionTemp.setId(det.getId());
                detalleMunicionTemp.setMarca(det.getMarca());
                detalleMunicionTemp.setCalibre(det.getCalibre());
                detalleMunicionTemp.setCantidad(det.getCantidad());
                detalleMunicionTemp.setActaInternamientoId(det.getActaInternamientoId());
                if (det.getActaInternamientoId() != null) {
                    actaInternamientoMunicionId = det.getActaInternamientoId();
                }
                lstDetalleMunicion.add(detalleMunicionTemp); 
            }
        }    
    }
    
    public String mostrarHora(TurTurno turno) {
        if(turno!=null){
            return turno.getProgramacionId().getHora().substring(0, 5);            
        }else{
            return "";
        }
    }

    
}
