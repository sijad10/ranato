/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbAsientoPersona;
import pe.gob.sucamec.bdintegrado.data.SbAsientoSunarp;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbPartidaSunarp;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.SspAlmacen;
import pe.gob.sucamec.bdintegrado.data.SspCartaFianza;
import pe.gob.sucamec.bdintegrado.data.SspContacto;
import pe.gob.sucamec.bdintegrado.data.SspForma;
import pe.gob.sucamec.bdintegrado.data.SspLicenciaMunicipal;
import pe.gob.sucamec.bdintegrado.data.SspLocalAutorizacion;
import pe.gob.sucamec.bdintegrado.data.SspLocalRegistro;
import pe.gob.sucamec.bdintegrado.data.SspParticipante;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SspRegistroEvento;
import pe.gob.sucamec.bdintegrado.data.SspRepresentantePublico;
import pe.gob.sucamec.bdintegrado.data.SspRepresentanteRegistro;
import pe.gob.sucamec.bdintegrado.data.SspServicio;
import pe.gob.sucamec.bdintegrado.data.SspTipoUsoLocal;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.UploadFilesController;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbPaisGt;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SspArchivo;
import pe.gob.sucamec.bdintegrado.data.SspRequisito;
import pe.gob.sucamec.bdintegrado.data.SspVisacion;
import pe.gob.sucamec.notificacion.data.NeArchivo;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.PideMigraciones;
import wspide.PideMigraciones_Service;
import pe.gob.sucamec.sel.gssp.data.LocalAutorizacionDetalle;
import javax.faces.context.ExternalContext;
import java.util.Collections;
import java.util.Comparator;
import java.util.Calendar;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import pe.gob.sucamec.bdintegrado.data.SspCarne;
import pe.gob.sucamec.bdintegrado.data.SspCarneInstructor;
import pe.gob.sucamec.bdintegrado.data.SspCefoespInstructor;
import pe.gob.sucamec.bdintegrado.data.SspPersonaFoto;
import pe.gob.sucamec.bdintegrado.data.SspPoligono;
import pe.gob.sucamec.bdintegrado.data.SspRegistroRegDisca;
import pe.gob.sucamec.bdintegrado.data.SspResolucion;
import pe.gob.sucamec.sel.gssp.data.PersonaDetalle;

/**
 *
 * @author locador772.ogtic
 */
@Named(value = "gsspSolicitudAutorizacionDepartamentoController")
@SessionScoped
public class GsspSolicitudAutorizacionDepartamentoController implements Serializable {

    @Inject
    WsTramDoc wsTramDocController;

    @Inject
    UploadFilesController uploadFilesController;

    @Inject
    GsspSolicitudAutorizacionSISPEController gsspSolicitudAutorizacionSISPEController;

    @Inject
    GsspSolicitudAutorizacionSPPController gsspSolicitudAutorizacionSPPController;

    @Inject
    GsspSolicitudAutorizacionSISPAController gsspSolicitudAutorizacionSISPAController;

    @Inject
    GsspSolicitudAutorizacionSCBCController gsspSolicitudAutorizacionSCBCController;

    @Inject
    GsspSolicitudAutorizacionSSEController gsspSolicitudAutorizacionSSEController;

    @Inject
    GsspSolicitudAutorizacionSTSController gsspSolicitudAutorizacionSTSController;

    @Inject
    GsspSolicitudAutorizacionSPCPController gsspSolicitudAutorizacionSPCPController;
    
    @Inject
    GsspSolicitudAutorizacionDepartamentoController gsspSolicitudAutorizacionDepartamentoController;

    private EstadoCrud estado;
    private Expediente expediente;

    private SspRegistro registro;
    private SspRegistro registroN2;
    private SspRepresentanteRegistro representanteRegistro;

    private SbPartidaSunarp partidaSunarpRL;
    private SbAsientoSunarp asientoSunarpRL;
    private SbAsientoPersona asientoPersonaRL;
    private SbRelacionPersonaGt relacionPersonaGtRL;
    private SspRepresentantePublico representantePublico;

    private SspRegistroEvento registroEvento;
    private SspServicio servicio;
    private SspForma forma;

    private SspLocalAutorizacion localAutorizacion;
    private SspLocalAutorizacion localAutorizacionAnterior;
    private SspLocalAutorizacion localAutorizacionAnterior2;
    private SspLocalAutorizacion localAutorizacionN2;
    private SspTipoUsoLocal tipoUsoLocal;
    private SspLocalRegistro localRegistro;
    private SspLicenciaMunicipal licenciaMunicipal;
    private SspCartaFianza cartaFianza;

    private SspVisacion vizacion;

    //=================   BANDEJA =======================
    private String filtroBuscarPor;
    private String filtroNumero;

    private List<TipoBaseGt> filtroTipoSeguridadListado;
    private TipoBaseGt filtroTipoSeguridadSelected;

    private List<TipoBaseGt> filtroTipoOpeListado;
    private TipoBaseGt filtroTipoOpeSelected;

    private List<TipoBaseGt> filtroTipoAutListado;
    private TipoBaseGt filtroTipoAutSelected;

    private List<TipoSeguridad> filtroTipoEstadoListado;
    private TipoSeguridad filtroTipoEstadoSelected;

    private Date filtroFechaIniSelected;
    private Date filtroFechaFinSelected;

    private List<Map> resultados;
    private List<Map> resultadosSeleccionados;
    private boolean disabledBtnTransmitir;

    private boolean blnProcesoTransmision = false;
    private Integer totalProcesados = 0;
    private Integer actualProcesado = 0;
    private Integer progress = 0;

    //============= CREAR SOLICITUD =====================
    //private boolean termCondic_DatosAdministradoUsuario;
    private String tipoAdministradoUsuario;
    private boolean administradoConRUC;

    //=======================================================
    //==========     Panel Administ PJ    ===================
    //=======================================================
    private SbPersonaGt regAdminist;
    private SbPersonaGt regUserAdminist;

    private String administRazonSocial;
    private String administRUC;

    //=======================================================
    //=============   Panel Administ PN   ===================
    //=======================================================
    private String administNombres;
    private String administTipDoc;
    private String administNumDoc;

    //=======================================================
    //==============    Panel Modalidad    ==================
    //=======================================================
    private List<TipoBaseGt> regTipoSeguridadList;
    private TipoBaseGt regTipoSeguridadSelected;

    private TipoBaseGt regTipoProcesoId;

    private List<TipoBaseGt> regFormaTipoSeguridadList;
    private TipoBaseGt regFormaTipoSeguridadSelected;

    private List<TipoSeguridad> regServicioPrestadoList;
    private TipoSeguridad regServicioPrestadoSelected;

    private List<SbRecibos> regListDatoRecibos;
    private SbRecibos regDatoRecibos;
    private String regNroComprobante;

    private List<TipoBaseGt> regTipoRegistroList;
    private TipoBaseGt regTipoRegistroSelected;
    private List<TipoBaseGt> regTipoOperacionList;
    private TipoBaseGt regTipoOperacionSelected;

    //=======================================================
    //==============    Foto   ==================
    //=======================================================
    private boolean habilitarAdjuntarFoto;
    private UploadedFile fileFOTO;
    private byte[] fotoByte;
    private StreamedContent archivoFOTO;
    private String nomArchivoFOTO;

    //=======================================================
    //==============    Cetificado Fisico Mental   ===========
    //=======================================================
    private boolean habilitarAdjuntarCertFiscoMntal;
    private UploadedFile fileCertFiscoMntal;
    private byte[] certFiscoMntalByte;
    private StreamedContent archivoCertFiscoMntal;
    private String nomArchivoCertFiscoMntal;

    //=======================================================
    //==============    Copia Contrato   ===========
    //=======================================================
    private boolean habilitarAdjuntarCopyContrato;
    private UploadedFile fileCopyContrato;
    private byte[] copyContratoByte;
    private StreamedContent archivoCopyContrato;
    private String nomArchivoCopyContrato;

    //=======================================================================
    //==============    Copia de Poliza Seguro Trabajo de Riesgo  ===========
    //=======================================================================
    private boolean habilitarAdjuntarCopyPolizaSeguro;
    private UploadedFile fileCopyPolizaSeguro;
    private byte[] copyPolizaSeguroByte;
    private StreamedContent archivoCopyPolizaSeguro;
    private String nomArchivoCopyPolizaSeguro;

    //=======================================================================
    //==============    Copia de Poliza Seguro Vida  ===========
    //=======================================================================
    private boolean habilitarAdjuntarCopyPolizaSegVida;
    private UploadedFile fileCopyPolizaSegVida;
    private byte[] copyPolizaSegVidaByte;
    private StreamedContent archivoCopyPolizaSegVida;
    private String nomArchivoCopyPolizaSegVida;

    //=======================================================
    //=======    Panel Representante Legal    ===============
    //=======================================================
    private String regRLPartidaRegistral;
    private String regRLAsientoRegistral;
    private List<TipoBaseGt> regOficinaRegListRL;
    private TipoBaseGt regOficinaRegSelectedRL;
    private List<TipoBaseGt> regZonaRegListRL;
    private TipoBaseGt regZonaRegSelectedRL;

    private List<TipoBaseGt> regTipoViasList;
    private TipoBaseGt regTipoViasSelected;

    private List<TipoBaseGt> regTipoDocList;
    private TipoBaseGt regTipoDocSelected;

    private String regNroDocBuscar;
    private SbPersonaGt regDatoPersonaByNumDocRL;

    private List<SbPersonaGt> regPersonasRLList;
    private SbPersonaGt regPersonasRLSelected;

    private SbRelacionPersonaGt registroSbRelacionPersonaGt;

    private String regDomicilioRLSelected;

    private SbDireccionGt regSbDireccionRL;

    private List<SbDistritoGt> regDistritoRLSList;
    private SbDistritoGt regDistritoRLSelected;

    private boolean existeAsientoSunarpRL;

    private boolean habilitarRepresentanteLegal;

    private List<PersonaDetalle> personaDetalleListado;
    private String personaDetalleSelectedString;

    //Para el Panel de Representante Legal Ver - Principal
    private SbPersonaGt representanteLegalVer;
    private String regRLPartidaRegistralVer;
    private String regRLAsientoRegistralVer;
    private List<TipoBaseGt> regZonaRegListRLVer;
    private TipoBaseGt regZonaRegSelectedRLVer;
    private List<TipoBaseGt> regOficinaRegListRLVer;
    private TipoBaseGt regOficinaRegSelectedRLVer;
    private List<TipoBaseGt> regTipoViasListVer;
    private TipoBaseGt regTipoViasSelectedVer;
    private String regDomicilioRLSelectedVer;
    private List<SbDistritoGt> regDistritoRLSListVer;
    private SbDistritoGt regDistritoRLSelectedVer;

    //=======================================================
    //=======    Panel Resol. Repres Legal E.P.   ===========
    //=======================================================
    private List<TipoBaseGt> regTipoDocEPList;
    private TipoBaseGt regTipoDocEPSelected;

    private String regNombDocEP;
    private String regNumDocEP;
    private Date regFechaEmisEP;

    //=======================================================
    //======    Panel Local Prestacion Servicio   ===========
    //=======================================================
    private MapModel emptyModel;
    private Marker marker;
    private boolean habilitarLocalPrestacionServicio;

    private List<SbDistritoGt> regDistritoLPSList;
    private SbDistritoGt regDistritoLPSSelected;

    private List<LocalAutorizacionDetalle> localAutorizacionListado;
    private SspLocalAutorizacion localAutorizacionSelected;
    private String localAutorizacionSelectedString;

    private List<TipoBaseGt> regTipoViasLPSList;
    private TipoBaseGt regTipoViasLPSSelected;

    private List<TipoSeguridad> regTipLocalLPSList;

    private List<TipoSeguridad> regTipUsoLPSList;
    private TipoSeguridad regTipUsoLPSSelected;

    private String regDireccionLPS;
    private String regNroFisicoLPS;
    private String regReferenciaLPS;

    private List<TipoBaseGt> regTipoViasLPSList_Form;
    private TipoBaseGt regTipoViasLPSSelected_Form;

    private List<TipoSeguridad> regTipLocalLPSList_Form;
    private TipoSeguridad regTipLocalLPSSelected_Form;

    private List<TipoSeguridad> regTipUsoLPSList_Form;
    private TipoSeguridad regTipUsoLPSSelected_Form;

    private String regDireccionLPS_Form;
    private String regNroFisicoLPS_Form;
    private String regReferenciaLPS_Form;
    private String regGeoLatitudLPS_Form;
    private String regGeoLongitudPS_Form;

    private List<TipoSeguridad> regAlmacenLPSList;
    private TipoSeguridad regAlmacenLPSSelected;

    private BigInteger regCantArmeriaLPS;
    private ArrayList<SspAlmacen> lstAlmacenes;
    private List<SspAlmacen> lstAlmacenesList;
    private SspAlmacen selectedAlmacen;
    private Long contRegAlmacen;

    //=======================================================
    //======    Medios Contacto   ===========
    //=======================================================
    private boolean habilitarMediosContactos;

    private List<TipoBaseGt> regPrioridadLPSList;
    private TipoBaseGt regPrioridadLPSSelected;

    private List<TipoBaseGt> regTipoMedioContactoLPSList;
    private TipoBaseGt regTipoMedioContactoLPSSelected;

    private ArrayList<SspContacto> lstContactos;
    private List<SspContacto> lstContactosList;
    private SspContacto selectedContacto;
    private List<SspContacto> listaUpdateContactosActivos = new ArrayList<>();
    private Long contRegContacto;
    private String regtextoContactoLPS;
    private String regtextoCorreoContactoLPS;

    //=======================================================
    //======    Licencia Municipal   ===========
    //=======================================================
    private boolean habilitarLicenciaMunicipal;

    private List<TipoBaseGt> regTipMunicipalidadLPSList;
    private TipoBaseGt regTipMunicipalidadLPSSelected;

    private List<TipoBaseGt> regTipoVisacionList;
    private TipoBaseGt regTipoVisacionSelected;

    private List<TipoBaseGt> regMunicLPSList;
    private TipoBaseGt regMunicLPSSelected;

    private String regNroLicenciaMunicLPS;
    private String regGiroComercialLPS;
    private Date regFechaEmisLPS;
    private Date regFechaVencLPS;
    private boolean regIndeterminadoLPS;
    private boolean regDisabledCalendar;
    ;
    
    //=======================================================
    //======    Accionistas   ===========
    //=======================================================
    
    
    private boolean habilitarAccionistas;

    private List<TipoBaseGt> regTipDocAccionistaList;
    private TipoBaseGt regTipDocAccionistaSelected;

    private List<TipoBaseGt> regTipoSocioList;
    private TipoBaseGt regTipoSocioSelected;

    private SbPersonaGt regDatosSocioEncontrado;
    private String regNombreSocioEncontrado;

    private String regCargoSocio;
    private String regNumeroDocAccionista;
    private Long contRegSocio;

    private ArrayList<SspParticipante> lstSocios;
    private SspParticipante selectedSocio;
    private List<SspParticipante> listaUpdateParticipanteActivos = new ArrayList<>();

    private String extension;
    private String tipoArchivo;

    private boolean noAntecedentesPenales = false;
    private boolean noLaboresEntidad = false;
    private boolean noEmpresaSeguridad = false;

    private int checkTipoAporte;
    private BigDecimal regMontoAportante;
    private boolean regDisabledMontoAportante = false;

    private int checkTipoAcciones;
    private BigDecimal regNumeroAcciones;
    private BigDecimal regPorcentajeAcciones;
    private boolean regDisabledNumerojeAcciones = false;
    private boolean regDisabledPorcentajeAcciones = true;

    //=======================================================
    //======    CARTA FIANZA   ===========
    //=======================================================
    private List<SspCartaFianza> regBuscaCartaFianzaList;
    private String regBuscaCartaFianzaSelectedString;
    private boolean regDisabledListaCartaFianza = false;
    private boolean regDisabledDatosCartaFianza = false;

    private boolean habilitarCartaFianza;

    private List<TipoSeguridad> regTipoFinancieraCFList;
    private TipoSeguridad regTipoFinancieraCFSelected;

    private List<TipoSeguridad> regEntidadFinancieraCFList;
    private TipoSeguridad regEntidadFinancieraCFSelected;

    private List<TipoBaseGt> regTipoMonedaCFList;
    private TipoBaseGt regTipoMonedaCFSelected;

    private String regNroCartaFianza;
    private BigDecimal regMontoCartaFianza;
    private Date regFechVigenciaIni;
    private Date regFechVigenciaFin;

    private UploadedFile fileRP;
    private byte[] rpByte;
    private StreamedContent archivoRP;
    private String nomArchivoRP;

    private UploadedFile fileCF;
    private byte[] cfByte;
    private StreamedContent archivoCF;
    private String nomArchivoCF;
    private String nomArchivoCFAnterior;

    private UploadedFile fileLM;
    private byte[] lmByte;
    private StreamedContent archivoLM;
    private String nomArchivoLM;
    private String nomArchivoLMAnterior;

    private UploadedFile fileLocal;
    private byte[] localByte;
    private StreamedContent archivoLocal;
    private String nomArchivoLocal;
    private String nomArchivoLocalAnterior;

    private UploadedFile fileSocio;
    private byte[] socioByte;
    private StreamedContent archivoSocio;
    private String nomArchivoSocio;

    //=======================================================
    //====    CHECKED - FORMAS DE SERVICIO DE SEGURIDAD  ====
    //=======================================================
    private List<TipoSeguridad> regFormasServTecnoSegList;

    private String[] selectedOptions;

    private TipoSeguridad regForma_TP_MCO_TEC_CRAIDMR;
    private TipoSeguridad regForma_TP_MCO_TEC_PSGPS;
    private TipoSeguridad regForma_TP_MCO_TEC_IDMSVD;
    private TipoSeguridad regForma_TP_MCO_TEC_IDMSDEI;
    private TipoSeguridad regForma_TP_MCO_TEC_IDMSCA;
    private TipoSeguridad regForma_TP_MCO_TEC_EPIS;
    private TipoSeguridad regForma_TP_MCO_TEC_SCM;
    private TipoSeguridad regForma_TP_MCO_TEC_OTRO;

    private ArrayList<SspForma> lstCheckedForma;
    private Long contRegCheckForma;
    private String selectOptionOtroVal;
    private String xNroSolicitiud;
    private String usuarioArchivar;
    private boolean regDisabledFormServTecn;

    private boolean regDisabledFormResolucionRL;

    //====================================================================
    //==========       AUTORIZACION CAMBIO DOMICILIO LEGAL     ===========
    //====================================================================
    private boolean termCondic_CambDomLegal;
    private String nroAutoBuscarACDL;
    private boolean habilitarFormaTipServACDL;
    private MapModel emptyModelACDL;
    private Marker markerACDL;
    private String regVizacNro;
    private String regVizacNroOficio;

    //====================================================================
    //=========      AUTORIZACION CAMBIO REPRESENTANTE LEGAL     =========
    //====================================================================
    private boolean termCondic_CambRepresLegal;
    private String nroAutoBuscarACRL;
    private boolean habilitarFormaTipServACRL;
    private MapModel emptyModelACRL;
    private Marker markerACRL;

    private MapModel modelMap;
    private Marker markerSelected;
    private String google_srcapi_key = "";
    private String google_jsapi = "";

    //Observaciones 
    private List<SspRegistroEvento> lstObservaciones;
    private boolean buscaNuevoModalRepresentanteLegal = false;
    private boolean buscaNuevoModalAccionista = false;
    private String tituloBuscaModalRepresentanteLegal;
    private Date fechaMinimaCalendario;
    private Date fechaMaximaCalendario;
    private Date fechaHoy = new Date();
    private int cantNumerosMinimaCarnetTipoDocRepresentanteLegal;
    private String cantNumerosDniCarnetTipoDocRepresentanteLegal;
    private Date regRepresentanteFechaNacimiento;
    private boolean regDisabledRepresentanteFechaNacimiento = true;
    private Date fechaMayorDeEdadCalendario;
    //Lista para Personas de la Declaracion Jurada
    private List<SspParticipante> lstPersonasDJ;
    private boolean regBuscaDepartamentoPrincipal = false;
    private boolean grabaSolicitud = false;
    private SspResolucion resolucionPrincipal;
    private boolean localPrincipalTransmitir = false;
    private String observacion;

    private List<TipoBaseGt> regTipoDocCapList;
    private TipoBaseGt regTipoDocCapSelected;
    private String regNroDocCapBuscar;
    private String cantNumerosDniCarnetTipoDocCapacitador;
    private String regNombreCapacitadorEncontrado;
    private String regMsgCapacitador;
    private List<SspCefoespInstructor> lstCapacitadores;
    private Long contRegCapacitador;
    private String nomArchivoPE;
    private String nomArchivoPEAnterior;
    private SbPersonaGt regSbPersonaCapacitador;
    private SspCarneInstructor carneInstructor;
    private UploadedFile fileCA;
    private byte[] caByte;
    private StreamedContent archivoCA;

    //*MAPA*//
    private boolean renderVerMapa;
    private Double lat_x = 0.0;
    private Double long_x = 0.0;
    private List<SbDistritoGt> listaDistritoMapaLocal;

    //poligonos
    private String cefoeNumResol;
    private Date cefoeFechaResolIni;
    private Date cefoeFechaResolFin;
    private String cefoeTipoLocal;

    private SspPoligono sspPoligono;

    private String regFormModalMapa_tipoModal;
    private boolean renderVerMapa2;

    private Double lat_x2 = 0.0;
    private Double long_x2 = 0.0;

    private List<SbDistritoGt> regDistritoLPSList2;
    private SbDistritoGt regDistritoLPSSelected2;

    private List<LocalAutorizacionDetalle> localAutorizacionListado2;
    private SspLocalAutorizacion localAutorizacionSelected2;
    private String localAutorizacionSelectedString2;

    private List<TipoBaseGt> regTipoViasLPSList2;
    private TipoBaseGt regTipoViasLPSSelected2;

    private List<TipoSeguridad> regTipLocalLPSList2;

    private List<TipoSeguridad> regTipUsoLPSList2;
    private TipoSeguridad regTipUsoLPSSelected2;

    private String regDireccionLPS2;
    private String regNroFisicoLPS2;
    private String regReferenciaLPS2;

    public String getRegFormModalMapa_tipoModal() {
        return regFormModalMapa_tipoModal;
    }

    public void setRegFormModalMapa_tipoModal(String regFormModalMapa_tipoModal) {
        this.regFormModalMapa_tipoModal = regFormModalMapa_tipoModal;
    }

    public boolean getRenderVerMapa2() {
        return renderVerMapa2;
    }

    public void setRenderVerMapa2(boolean renderVerMapa2) {
        this.renderVerMapa2 = renderVerMapa2;
    }

    public Double getLat_x2() {
        return lat_x2;
    }

    public void setLat_x2(Double lat_x2) {
        this.lat_x2 = lat_x2;
    }

    public Double getLong_x2() {
        return long_x2;
    }

    public void setLong_x2(Double long_x2) {
        this.long_x2 = long_x2;
    }

    public List<SbDistritoGt> getRegDistritoLPSList2() {
        return regDistritoLPSList2;
    }

    public void setRegDistritoLPSList2(List<SbDistritoGt> regDistritoLPSList2) {
        this.regDistritoLPSList2 = regDistritoLPSList2;
    }

    public SbDistritoGt getRegDistritoLPSSelected2() {
        return regDistritoLPSSelected2;
    }

    public void setRegDistritoLPSSelected2(SbDistritoGt regDistritoLPSSelected2) {
        this.regDistritoLPSSelected2 = regDistritoLPSSelected2;
    }

    public List<LocalAutorizacionDetalle> getLocalAutorizacionListado2() {
        return localAutorizacionListado2;
    }

    public void setLocalAutorizacionListado2(List<LocalAutorizacionDetalle> localAutorizacionListado2) {
        this.localAutorizacionListado2 = localAutorizacionListado2;
    }

    public SspLocalAutorizacion getLocalAutorizacionSelected2() {
        return localAutorizacionSelected2;
    }

    public void setLocalAutorizacionSelected2(SspLocalAutorizacion localAutorizacionSelected2) {
        this.localAutorizacionSelected2 = localAutorizacionSelected2;
    }

    public String getLocalAutorizacionSelectedString2() {
        return localAutorizacionSelectedString2;
    }

    public void setLocalAutorizacionSelectedString2(String localAutorizacionSelectedString2) {
        this.localAutorizacionSelectedString2 = localAutorizacionSelectedString2;
    }

    public List<TipoBaseGt> getRegTipoViasLPSList2() {
        return regTipoViasLPSList2;
    }

    public void setRegTipoViasLPSList2(List<TipoBaseGt> regTipoViasLPSList2) {
        this.regTipoViasLPSList2 = regTipoViasLPSList2;
    }

    public TipoBaseGt getRegTipoViasLPSSelected2() {
        return regTipoViasLPSSelected2;
    }

    public void setRegTipoViasLPSSelected2(TipoBaseGt regTipoViasLPSSelected2) {
        this.regTipoViasLPSSelected2 = regTipoViasLPSSelected2;
    }

    public List<TipoSeguridad> getRegTipLocalLPSList2() {
        return regTipLocalLPSList2;
    }

    public void setRegTipLocalLPSList2(List<TipoSeguridad> regTipLocalLPSList2) {
        this.regTipLocalLPSList2 = regTipLocalLPSList2;
    }

    public List<TipoSeguridad> getRegTipUsoLPSList2() {
        return regTipUsoLPSList2;
    }

    public void setRegTipUsoLPSList2(List<TipoSeguridad> regTipUsoLPSList2) {
        this.regTipUsoLPSList2 = regTipUsoLPSList2;
    }

    public TipoSeguridad getRegTipUsoLPSSelected2() {
        return regTipUsoLPSSelected2;
    }

    public void setRegTipUsoLPSSelected2(TipoSeguridad regTipUsoLPSSelected2) {
        this.regTipUsoLPSSelected2 = regTipUsoLPSSelected2;
    }

    public String getRegDireccionLPS2() {
        return regDireccionLPS2;
    }

    public void setRegDireccionLPS2(String regDireccionLPS2) {
        this.regDireccionLPS2 = regDireccionLPS2;
    }

    public String getRegNroFisicoLPS2() {
        return regNroFisicoLPS2;
    }

    public void setRegNroFisicoLPS2(String regNroFisicoLPS2) {
        this.regNroFisicoLPS2 = regNroFisicoLPS2;
    }

    public String getRegReferenciaLPS2() {
        return regReferenciaLPS2;
    }

    public void setRegReferenciaLPS2(String regReferenciaLPS2) {
        this.regReferenciaLPS2 = regReferenciaLPS2;
    }
    //=================================================================================

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroFacade ejbSspRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade ejbTipoSeguridadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt ejbSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPartidaSunarpFacade ejbSbPartidaSunarpFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbAsientoSunarpFacade ejbSbAsientoSunarpFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbAsientoPersonaFacade ejbSbAsientoPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRepresentanteRegistroFacade ejbSspRepresentanteRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroEventoFacade ejbSspRegistroEventoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspServicioFacade ejbSspServicioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspFormaFacade ejbSspFormaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRepresentantePublicoFacade ejbRepresentantePublicoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt ejbSbDistritoFacadeGtFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspLocalAutorizacionFacade ejbSspLocalAutorizacionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspTipoUsoLocalFacade ejbSspTipoUsoLocalFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspAlmacenFacade ejbSspAlmacenFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspContactoFacade ejbSspContactoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspLocalRegistroFacade ejbSspLocalRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspLicenciaMunicipalFacade ejbSspLicenciaMunicipalFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspParticipanteFacade ejbSspParticipanteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCartaFianzaFacade ejbSspCartaFianzaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRelacionPersonaFacadeGt ejbSbRelacionPersonaFacadeGt;    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade ejbSbDerivacionCydocFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPaisFacadeGt ejbSbPaisFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspVisacionFacade ejbSspVisacionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRequisitoFacade ejbSspRequisitoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspArchivoFacade ejbSspArchivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspResolucionFacade ejbSspResolucionFacade;
    @EJB
    private pe.gob.sucamec.notificacion.beans.NeDocumentoFacade neDocumentoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCefoespInstructorFacade ejbSspCefoespInstructorFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCarneInstructorFacade ejbSspCarneInstructorFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspPoligonoFacade ejbSspPoligonoFacade;

    public GsspSolicitudAutorizacionDepartamentoController() {
    }

    public String mostrarCrear() {

        tipoAdministradoUsuario = "PersonaNatural";
        String URL_Formulario = "";

        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXNumDoc(p_user.getLogin());

        if (regAdminist.getRuc() != null) {
            administradoConRUC = true;
            regTipoSeguridadSelected = null;

            if (regAdminist.getTipoId().getId().equals(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR").getId())) {
                tipoAdministradoUsuario = "PersonaJuridica";
                if (regAdminist.getRuc().substring(0, 2).equals("20")) {
                    regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_DEP','TP_GSSP_MOD_DEP','TP_GSSP_MOD_DEP', 'TP_GSSP_MOD_TEC', 'TP_GSSP_MOD_PCP', 'TP_GSSP_MOD_PP', 'TP_AUTORIZ_SEL_CLO', 'TP_GSSP_MOD_SCBC', 'TP_GSSP_MOD_SSE'");
                } else {
                    regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_DEP','TP_GSSP_MOD_DEP', 'TP_GSSP_MOD_PCP', 'TP_GSSP_MOD_PP', 'TP_AUTORIZ_SEL_CLO', 'TP_GSSP_MOD_SCBC', 'TP_GSSP_MOD_SSE'");
                }
            } else {
                tipoAdministradoUsuario = "PersonaNatural";
                regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_SISPE','TP_GSSP_MOD_SISPA'");
            }

            URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/Create";
        } else {
            administradoConRUC = false;
            URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/Mensaje";
        }

        return URL_Formulario;
    }

    public String prepareList() {

        estado = EstadoCrud.BUSCAR;

        filtroBuscarPor = null;
        filtroNumero = null;

        //filtroTipoSeguridadListado = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_GSSP_AUTORIZACION");
        filtroTipoSeguridadListado = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_DEP', 'TP_GSSP_MOD_PP', 'TP_GSSP_MOD_TDV', 'TP_GSSP_MOD_PCP', 'TP_GSSP_MOD_CYA', 'TP_GSSP_MOD_SISPE', 'TP_GSSP_MOD_SISPA', 'TP_GSSP_MOD_TEC', 'TP_GSSP_MOD_ICYA', 'TP_GSSP_MOD_SCBC', 'TP_GSSP_MOD_SSE', 'TP_GSSP_MOD_DEP', 'TP_AUTORIZ_SEL_CLO', 'TP_GSSP_MOD_DEP' ");
        filtroTipoSeguridadSelected = null;

        filtroTipoOpeListado = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_OPE_INI', 'TP_OPE_REN'");
        filtroTipoOpeSelected = null;

        filtroTipoEstadoListado = ejbTipoSeguridadFacade.listarTipoSeguridadXCodProgs("'TP_ECC_CRE', 'TP_ECC_TRA', 'TP_ECC_OBS', 'TP_ECC_NPR', 'TP_ECC_CAN', 'TP_ECC_APR', 'TP_ECC_DES', 'TP_ECC_ANU' ");
        filtroTipoEstadoSelected = null;

        filtroTipoAutListado = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_AUTORIZ_SEL_ASS', 'TP_AUTORIZ_SEL_CDL', 'TP_AUTORIZ_SEL_CRL'");
        filtroTipoAutSelected = null;

        filtroFechaIniSelected = null;
        filtroFechaFinSelected = null;

        calcularFechaMaximaCalendario();
        calcularFechaMinimaCalendario();

        resultados = new ArrayList();
        resultadosSeleccionados = new ArrayList();
        blnProcesoTransmision = false;

        totalProcesados = 0;
        actualProcesado = 0;
        progress = 0;

        return "/aplicacion/gssp/gsspSolicitudAutorizacion/List";

    }

    public void buscarBandejaSolicitud() {
        boolean validacion = true;

        if (filtroBuscarPor != null) {

            if (filtroBuscarPor.equals("1") || filtroBuscarPor.equals("2") || filtroBuscarPor.equals("3")) {
                if (filtroNumero == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":buscarPor");
                    JsfUtil.mensajeAdvertencia("Es necesario ingresar el número del documento.");
                } else if (filtroNumero.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":buscarPor");
                    JsfUtil.mensajeAdvertencia("Es necesario ingresar el número del documento");
                }
            }

            if (filtroBuscarPor.equals("4")) {
                if (filtroTipoSeguridadSelected == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":modalidades");
                    JsfUtil.mensajeAdvertencia("Es necesario seleccionar una modalidad");
                }
            }
        }

        if ((filtroFechaIniSelected != null && filtroFechaFinSelected == null) || (filtroFechaIniSelected == null && filtroFechaFinSelected != null)) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":txfFechaInicio");
            JsfUtil.invalidar(obtenerForm() + ":txfFechaFinal");
            JsfUtil.mensajeAdvertencia("Es necesario seleccionar un rango de fecha");
        }

        if (validacion) {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt administ = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

            HashMap mMap = new HashMap();
            mMap.put("buscarPor", filtroBuscarPor);
            mMap.put("filtroNumero", filtroNumero);
            mMap.put("filtroTipoSeguridadId", (filtroTipoSeguridadSelected != null) ? filtroTipoSeguridadSelected.getId() : null);
            mMap.put("filtroTipoAutID", (filtroTipoAutSelected != null) ? filtroTipoAutSelected.getId() : null);
            mMap.put("filtroTipoOpeID", (filtroTipoOpeSelected != null) ? filtroTipoOpeSelected.getId() : null);
            mMap.put("filtroFechaIni", filtroFechaIniSelected);
            mMap.put("filtroFechaFin", filtroFechaFinSelected);
            mMap.put("filtroTipoEstadoId", (filtroTipoEstadoSelected != null) ? filtroTipoEstadoSelected.getId() : null);
            mMap.put("filtroAdministradoId", (administ != null) ? administ.getId() : null);

            resultados = ejbSspRegistroFacade.buscarBandejaSolicitudAutorizacion(mMap);
        }
    }

    public void reiniciarCamposBusqueda() {
        filtroBuscarPor = null;
        filtroNumero = null;
        filtroTipoSeguridadSelected = null;
        filtroTipoAutSelected = null;
        filtroTipoOpeSelected = null;
        filtroFechaIniSelected = null;
        filtroFechaFinSelected = null;
        filtroTipoEstadoSelected = null;
        setDisabledBtnTransmitir(false);
    }

    public boolean renderBtnEditar(Map item) {
        boolean validar = false;

        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE") || item.get("ESTADO_CODPROG").equals("TP_ECC_OBS")) {
                validar = true;
            }
        }

        return validar;
    }

    public void mostrarEditarSolicitud(Map item) {

        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SISPE")) {
            gsspSolicitudAutorizacionSISPEController.setEstado(EstadoCrud.EDITARSISPE);
            gsspSolicitudAutorizacionSISPEController.setTermCondicionAceptaDDJJ(false);
            gsspSolicitudAutorizacionSISPEController.setRegistro(registro);
            gsspSolicitudAutorizacionSISPEController.iniciarValores_SISPE_Editar(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/sispe/UpdateSispe.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_DEP")) {
             gsspSolicitudAutorizacionDepartamentoController.iniciarValores_CrearSolicitud();
             estado = EstadoCrud.EDITARDEPARTAMENTO;
            gsspSolicitudAutorizacionDepartamentoController.setEstado(estado);
            gsspSolicitudAutorizacionDepartamentoController.cargarDatosEditarSolicitud(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/departamento/EditarAutorizaServicioSeguridad.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // iniciarValores_CrearSolicitud();   SE COMENTA 
            //cargarDatosEditarSolicitud(registro);
        } else if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SCBC")) {
            gsspSolicitudAutorizacionSCBCController.iniciarValores_CrearSolicitud();
            estado = EstadoCrud.EDITARSCBC;
            gsspSolicitudAutorizacionSCBCController.setEstado(estado);
            gsspSolicitudAutorizacionSCBCController.cargarDatosEditarSolicitud(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/custodia/EditarSCBC.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SSE")) {
            gsspSolicitudAutorizacionSSEController.iniciarValores_CrearSolicitud();
            estado = EstadoCrud.EDITARSSE;
            gsspSolicitudAutorizacionSSEController.setEstado(estado);
            gsspSolicitudAutorizacionSSEController.cargarDatosEditarSolicitud(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/eventos/EditarSSE.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SISPA")) {
            gsspSolicitudAutorizacionSISPAController.setEstado(EstadoCrud.EDITARSISPA);
            gsspSolicitudAutorizacionSISPAController.setTermCondicionAceptaDDJJ(false);
            gsspSolicitudAutorizacionSISPAController.setRegistro(registro);
            gsspSolicitudAutorizacionSISPAController.iniciarValores_SISPA_Editar(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/sispa/UpdateSispa.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            JsfUtil.mensajeError("COD_PROG->" + registro.getTipoProId().getCodProg());
        }
    }

    public void cargarDatosEditarSolicitud(SspRegistro item) {

        registro = null;
        if (item != null) {
            registro = item;

            lstObservaciones = null;
            //Busca las Observaciones
            if (lstObservaciones == null) {
                lstObservaciones = new ArrayList();
            }
            for (SspRegistroEvento ce : registro.getSspRegistroEventoList()) {
                if (ce.getActivo() == 1 && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")) {
                    lstObservaciones.add(ce);
                }
            }
            //Fin de Busca las Observaciones

            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            regAdminist = registro.getEmpresaId();
            regUserAdminist = ejbSbPersonaFacade.buscarPersonaXNumDoc(p_user.getLogin());

            administradoConRUC = true;

            //=============================================
            //============  Administrado PJ ===============
            //=============================================
            administRazonSocial = regAdminist.getRznSocial();
            administRUC = regAdminist.getRuc();

            //=============================================
            //============  Administrado PN ===============
            //=============================================
            administNombres = "";
            administNombres = p_user.getNombres() + " " + p_user.getApePat() + "" + p_user.getApeMat();
            administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
            administNumDoc = regUserAdminist.getNumDoc();

            //=============================================
            //======  Datos de la  Modalidad    ===========
            //=============================================
            //Tipo de Proceso de SERVICIO DE VIGILANCIA PRIVADA
            regTipoProcesoId = registro.getTipoProId();
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_DEP'");
            regTipoProcesoId = regTipoSeguridadSelected;
            regTipoSeguridadSelected = registro.getTipoProId();

            //Tipo de Registro
            regTipoRegistroList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_REGIST");
            regTipoRegistroSelected = registro.getTipoRegId();

            //Tipo de Operación
            regTipoOperacionList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_OPE");
            regTipoOperacionSelected = registro.getTipoOpeId();

            //Servicio Prestado CON ARMA, SIN ARMA
            regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP");
            if (registro.getSspServicioList() != null) {
                regServicioPrestadoSelected = registro.getSspServicioList().get(0).getServicioPrestadoId();
            }

            //No se habilita el Comprobante de Pago
            regNroComprobante = null;
            regDatoRecibos = null;

            //=============================================================
            //===============    Local Prestacion servicio     ============
            //=============================================================
            //Limpia los datos del Panel de Representante Legal Ver - PRINCIPAL
            representanteLegalVer = null;
            regRLPartidaRegistralVer = null;
            regRLAsientoRegistralVer = null;
            //regZonaRegListRLVer = regZonaRegListRL;
            regZonaRegSelectedRLVer = null;
            regOficinaRegListRLVer = null;
            regOficinaRegSelectedRLVer = null;
            //regTipoViasListVer = regTipoViasList;
            regTipoViasSelectedVer = null;
            regDomicilioRLSelectedVer = null;
            //regDistritoRLSListVer = regDistritoRLSList;
            regDistritoRLSelectedVer = null;

            habilitarLocalPrestacionServicio = true;
            emptyModel = new DefaultMapModel();

            regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
            regTipLocalLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_LOCAL");
            regTipUsoLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");

            localAutorizacionListado = null;
            localAutorizacionListado = new ArrayList<LocalAutorizacionDetalle>();

            if (registro.getSspLocalRegistroList() != null) {
                listarDepartamentosDistritoTipoLocal();
                regDistritoLPSSelected = registro.getSspLocalRegistroList().get(0).getLocalId().getDistritoId();
                //Detalle de Locales del distrito Seleccionado
                List<SspLocalAutorizacion> listDetalleLocal = new ArrayList<>();
                //listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocal(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId());
                listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaCefoesp(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipoSeguridadSelected.getId(), registro.getId());
                if (listDetalleLocal != null) {
                    LocalAutorizacionDetalle detalleLocalTemp;
                    for (SspLocalAutorizacion localDetalle : listDetalleLocal) {
                        detalleLocalTemp = new LocalAutorizacionDetalle();
                        detalleLocalTemp.setId(localDetalle.getId());
                        detalleLocalTemp.setTipoUbicacionId(localDetalle.getTipoUbicacionId());
                        detalleLocalTemp.setDireccion((localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "");
                        detalleLocalTemp.setNroFisico((localDetalle.getNroFisico() != null) ? localDetalle.getNroFisico() : "");
                        detalleLocalTemp.setDistritoId(localDetalle.getDistritoId());
                        detalleLocalTemp.setReferencia((localDetalle.getReferencia() != null) ? localDetalle.getReferencia() : "");
                        if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                            detalleLocalTemp.setTipoLocalId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoLocalId());
                        } else {
                            detalleLocalTemp.setTipoLocalId(null);
                        }
                        if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                            detalleLocalTemp.setTipoUsoId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoUsoId());
                        } else {
                            detalleLocalTemp.setTipoUsoId(null);
                        }
                        detalleLocalTemp.setGeoLatitud((localDetalle.getGeoLatitud() != null) ? localDetalle.getGeoLatitud() : "");
                        detalleLocalTemp.setGeoLongitud((localDetalle.getGeoLongitud() != null) ? localDetalle.getGeoLongitud() : "");
                        localAutorizacionListado.add(detalleLocalTemp);
                    }

                    //BUSCA LOCAL PRINCIPAL
                    for (SspLocalRegistro sspLocalReg : registro.getSspLocalRegistroList()) {
                        if (sspLocalReg.getActivo() == JsfUtil.TRUE) {//SI LOCAL ACTIVO
                            localAutorizacionSelectedString = sspLocalReg.getLocalId().getId().toString();
                            localAutorizacionAnterior = sspLocalReg.getLocalId();
                        }
                    }
                    mostrarDatosLocalSeleccionado();
                }

            } else {
                localAutorizacionSelectedString = null;
                regDistritoLPSSelected = null;
                localAutorizacionSelectedString = null;
                localAutorizacionAnterior = null;
                regTipoViasLPSSelected = null;
                regDireccionLPS = null;
                regNroFisicoLPS = null;
                regTipUsoLPSSelected = null;
                regReferenciaLPS = null;

            }

            //Nombre del Archivo de Plano de Distribucion
            nomArchivoLocal = null;
            nomArchivoLocalAnterior = null;
            SspArchivo archPlanoDistribucion = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_PD");
            if (archPlanoDistribucion != null) {
                nomArchivoLocalAnterior = archPlanoDistribucion.getNombre();
            }

            //=============================================
            //=========  Representante Legal   ============
            //=============================================
            registroSbRelacionPersonaGt = new SbRelacionPersonaGt();
            habilitarRepresentanteLegal = true;

            //Representantes Legal del Administrado 
            //regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
            //regPersonasRLSelected = registro.getRepresentanteId();
            /**
             * *******************ver***
             */
            //Zonas Registrales
            regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
            //Tipos de Vias
            regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
            //Distrito del domicilio del Representante Legal 
            regDistritoRLSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
            //Lista de Tipo de Documento para el Representante Legal
            regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");

            personaDetalleListado = null;
            personaDetalleListado = new ArrayList<PersonaDetalle>();

            TipoBaseGt sTipoBase = new TipoBaseGt();
            //Si es Sucursal es Responsable Legal
            //Caso contrario es Representante Legal
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL

            //Detalle de relacion Persona como Representante Legal o Responsable Legal
            List<SbPersonaGt> listDetallePersona = new ArrayList<>();
            listDetallePersona = ejbSbPersonaFacade.listarPersonaXIdRelacionPersonaUnionSspRegistro(regAdminist.getId(), registro.getId(), sTipoBase.getId());
            if (listDetallePersona != null) {
                PersonaDetalle detallePersonaTemp;
                for (SbPersonaGt PersonaDetalle : listDetallePersona) {
                    detallePersonaTemp = new PersonaDetalle();
                    detallePersonaTemp.setId(PersonaDetalle.getId());
                    detallePersonaTemp.setNumDoc(PersonaDetalle.getNumDoc());
                    detallePersonaTemp.setApePat(PersonaDetalle.getApePat());
                    detallePersonaTemp.setApeMat(PersonaDetalle.getApeMat());
                    detallePersonaTemp.setNombres(PersonaDetalle.getNombres());
                    personaDetalleListado.add(detallePersonaTemp);
                }
                personaDetalleSelectedString = registro.getRepresentanteId().getId().toString();
                regTipoDocSelected = registro.getRepresentanteId().getTipoDoc();
                //localAutorizacionAnterior = registro.getSspLocalRegistroList().get(0).getLocalId();   
                mostrarDomicilioRepresentanteLegal();
            }

            /*SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
            xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(registro.getRepresentanteId().getId());
            if(xSbAsientoPersonaRL != null){
                regRLPartidaRegistral = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getPartidaRegistral();;
                regRLAsientoRegistral = xSbAsientoPersonaRL.getAsientoId().getNroAsiento();
                if (xSbAsientoPersonaRL.getAsientoId().getPartidaId() != null) {
                    regZonaRegSelectedRL = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getZonaRegistral();
                    listarOficinasRegistrales_RL();
                    regOficinaRegSelectedRL = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getOficinaRegistral();
                } else {
                    regZonaRegSelectedRL = null;
                    regOficinaRegSelectedRL = null;
                }
            }*/
 /*List<SbDireccionGt> direccionRepresentante = ejbSbDireccionFacade.listarDireccionesXPersona(regPersonasRLSelected.getId());
            if(direccionRepresentante.size()>0){       
                regTipoViasSelected = direccionRepresentante.get(0).getViaId();     
                regDomicilioRLSelected = direccionRepresentante.get(0).getDireccion();
                regDistritoRLSelected = direccionRepresentante.get(0).getDistritoId();
            }else{
                regTipoViasSelected = null;
                regDomicilioRLSelected = null;
                regDistritoRLSelected = null;
            }  */
            //regNroDocBuscar = null;
            //regDatoPersonaByNumDocRL = null;
            existeAsientoSunarpRL = true;

            //=============================================================
            //==============          Medios contactos           ==========
            //=============================================================
            habilitarMediosContactos = true;

            lstContactos = new ArrayList<SspContacto>();
            for (SspContacto contactoActivo : registro.getSspContactoList()) {
                if (contactoActivo.getActivo() == 1) {
                    lstContactos.add(contactoActivo);
                }
            }

            //=============================================================
            //===============    Licencia Municipal     ============
            //=============================================================
            habilitarLicenciaMunicipal = true;

            regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MUNI");
            licenciaMunicipal = ejbSspLicenciaMunicipalFacade.verUltimaLicenciaMunicipalXIdLocal(localAutorizacionAnterior.getId());
            //if (registro.getSspLocalRegistroList().get(0).getLocalId().getSspLicenciaMunicipalList().size() > 0) {
            if (licenciaMunicipal != null) {
                regTipMunicipalidadLPSSelected = licenciaMunicipal.getTipoMunicipalidad();
                listarMunicipalidades();
                regMunicLPSSelected = licenciaMunicipal.getMunicipalidadId();
                regNroLicenciaMunicLPS = licenciaMunicipal.getNumeroLicencia().toString();
                regGiroComercialLPS = licenciaMunicipal.getGiroComercial().toString();
                regFechaEmisLPS = licenciaMunicipal.getFechaIni();
                regFechaVencLPS = licenciaMunicipal.getFechaFin();
                regDisabledCalendar = false;
                if (regFechaVencLPS == null) {
                    regDisabledCalendar = true;
                    regIndeterminadoLPS = true;
                }
            } else {
                regTipMunicipalidadLPSSelected = null;
                regNroLicenciaMunicLPS = null;
                regGiroComercialLPS = null;
                regFechaEmisLPS = null;
                regFechaVencLPS = null;
                regIndeterminadoLPS = false;
                regDisabledCalendar = false;
                regMunicLPSList = null;
                regMunicLPSSelected = null;
            }

            //Nombre del Archivo de Licencia Municipal
            nomArchivoLM = null;
            nomArchivoLMAnterior = null;
            SspArchivo archLicenciaMunicipal = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_LM");
            if (archLicenciaMunicipal != null) {
                nomArchivoLMAnterior = archLicenciaMunicipal.getNombre();
            }

            //=============================================================
            //=======================    ACCIONISTAS      ================
            //=============================================================
            habilitarAccionistas = true;

            lstSocios = new ArrayList<SspParticipante>();
            for (SspParticipante socioActivo : registro.getSspParticipanteList()) {
                if (socioActivo.getActivo() == 1) {
                    lstSocios.add(socioActivo);
                }
            }

            //=============================================================
            //=======================    CAPACITADORES      ================
            //=============================================================
            //habilitarCapacitadores = true;
            lstCapacitadores = new ArrayList<SspCefoespInstructor>();
            List<SspCefoespInstructor> listaInstructores = ejbSspCefoespInstructorFacade.buscarInstructoresPorIdRegistroModalidad(registro.getId(), "TP_GSSP_MOD_DEP");
            if (listaInstructores != null) {

                for (SspCefoespInstructor capacitador : listaInstructores) {
                    if (capacitador.getActivo() == 1) {
                        lstCapacitadores.add(capacitador);
                    }
                }
            }

            //Nombre del Archivo de Licencia Municipal
            nomArchivoPE = null;
            nomArchivoPEAnterior = null;
            SspArchivo archPlanEstudios = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_PE");
            if (archPlanEstudios != null) {
                nomArchivoPEAnterior = archPlanEstudios.getNombre();
            }

            //=============================================================
            //=======================    CARTA FIANZA      ================
            //=============================================================
            regTipoFinancieraCFList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_FIN_SSP");
            if (registro.getCartaFianzaId() != null) {
                //Selecciona la Carta Fianza de la Lista de Buscar Carta Fianza
                regBuscaCartaFianzaSelectedString = registro.getCartaFianzaId().getId().toString();
                buscaCartaFianza();
                /*regNroCartaFianza = registro.getCartaFianzaId().getNumero();
                regTipoFinancieraCFSelected = registro.getCartaFianzaId().getTipoFinancieraId();
                listarEntidadFinancieras();
                regEntidadFinancieraCFSelected = registro.getCartaFianzaId().getFinancieraId();
                regMontoCartaFianza = registro.getCartaFianzaId().getMonto();
                regFechVigenciaIni = registro.getCartaFianzaId().getVigenciaInicio();
                regFechVigenciaFin = registro.getCartaFianzaId().getVigenciaFin();*/

                //Nombre del Archivo de Carta Fianza
                SspArchivo archCartaFianza = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_CF");
                if (archCartaFianza != null) {
                    nomArchivoCFAnterior = archCartaFianza.getNombre();
                }

            } else {
                regBuscaCartaFianzaSelectedString = null;
                regNroCartaFianza = null;
                regTipoFinancieraCFSelected = null;
                regEntidadFinancieraCFList = null;
                regEntidadFinancieraCFSelected = null;
                regMontoCartaFianza = null;
                regFechVigenciaIni = null;
                regFechVigenciaFin = null;
                nomArchivoCF = null;
                nomArchivoCFAnterior = null;
            }

        } else {
            JsfUtil.mensajeError("No se encontró datos de la solicitud");
        }
    }

    public boolean renderBtnBorrar(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE")) {
                validar = true;
            }
        }
        return validar;
    }

    public void borrarRegistro(Map item) {

        try {
            if (item != null) {
                SspRegistro reg = ejbSspRegistroFacade.find((Long) item.get("ID"));
                reg.setActivo(JsfUtil.FALSE);
                ejbSspRegistroFacade.edit(reg);

                //Actualiza como inactivos los contactos del Registro
                if (reg.getSspContactoList().size() > 0) {
                    for (SspContacto listaContacto : reg.getSspContactoList()) {
                        if (listaContacto.getActivo() == 1) {
                            listaContacto.setActivo(JsfUtil.FALSE);
                            ejbSspContactoFacade.edit(listaContacto);
                        }
                    }
                }

                //Actualiza como inactivos los Participantes del Registro
                if (reg.getSspParticipanteList().size() > 0) {
                    for (SspParticipante listaParticipante : reg.getSspParticipanteList()) {
                        if (listaParticipante.getActivo() == 1) {
                            listaParticipante.setActivo(JsfUtil.FALSE);
                            ejbSspParticipanteFacade.edit(listaParticipante);
                        }
                    }
                }

                //Actualiza como inactivo el Representante del Registro
                if (reg.getRepresentanteId() != null) {
                    SspRepresentanteRegistro RepresentanteBusca = new SspRepresentanteRegistro();
                    RepresentanteBusca = ejbSspRepresentanteRegistroFacade.representRegFindByRegistroId(reg.getId(), reg.getEmpresaId().getId());
                    if (RepresentanteBusca != null) {
                        RepresentanteBusca.setActivo(JsfUtil.FALSE);
                        ejbSspRepresentanteRegistroFacade.edit(RepresentanteBusca);
                    }
                }

                //Actualiza como inactivos los contactos del Registro
                if (reg.getSspRequisitoList().size() > 0) {
                    for (SspRequisito listaRequisito : reg.getSspRequisitoList()) {
                        if (listaRequisito.getActivo() == 1) {
                            if (listaRequisito.getSspArchivoList().size() > 0) {
                                //Inactiva los Archivos relacionados
                                for (SspArchivo listaArchivos : listaRequisito.getSspArchivoList()) {
                                    listaArchivos.setActivo(JsfUtil.FALSE);
                                    ejbSspArchivoFacade.edit(listaArchivos);
                                }
                            }
                            listaRequisito.setActivo(JsfUtil.FALSE);
                            ejbSspRequisitoFacade.edit(listaRequisito);
                        }
                    }
                }

                //Actualiza como inactivo el Servicio del Registro
                if (reg.getSspServicioList().size() > 0) {
                    for (SspServicio listaServicio : reg.getSspServicioList()) {
                        if (listaServicio.getActivo() == 1) {
                            listaServicio.setActivo(JsfUtil.FALSE);
                            ejbSspServicioFacade.edit(listaServicio);
                        }
                    }
                }

                //Actualiza como inactivos los Eventos del Registro
                /*if (reg.getSspRegistroEventoList().size() > 0) {
                    for (SspRegistroEvento listaEventos : reg.getSspRegistroEventoList()) {
                        if (listaEventos.getActivo() == 1) {
                            listaEventos.setActivo(JsfUtil.FALSE);
                            ejbSspRegistroEventoFacade.edit(listaEventos);
                        }
                    }
                }*/
                //Actualiza como inactivos los Locales del Registro
                if (reg.getSspLocalRegistroList().size() > 0) {
                    for (SspLocalRegistro listaLocalRegistro : reg.getSspLocalRegistroList()) {
                        if (listaLocalRegistro.getActivo() == 1) {
                            listaLocalRegistro.setActivo(JsfUtil.FALSE);
                            ejbSspLocalRegistroFacade.edit(listaLocalRegistro);
                        }
                    }
                }

                //Actualiza como inactivos los Tipo uso Local del Registro
                if (reg.getSspTipoUsoLocalList().size() > 0) {
                    for (SspTipoUsoLocal listaTipoUsoLocal : reg.getSspTipoUsoLocalList()) {
                        if (listaTipoUsoLocal.getActivo() == 1) {
                            listaTipoUsoLocal.setActivo(JsfUtil.FALSE);
                            ejbSspTipoUsoLocalFacade.edit(listaTipoUsoLocal);
                        }
                    }
                }

                resultados.remove(item);
                resultadosSeleccionados.remove(item);

                JsfUtil.mensaje("Se ha borrado correctamente el registro.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
        }

    }

    public boolean renderBtnVer() {
        return true;
    }

    public String mostrarVerSolicitud(Map item) {
        String URL_Formulario = "";

        //Busca el Registro
        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SISPE")) {
            gsspSolicitudAutorizacionSISPEController.setRegistro(registro);
            gsspSolicitudAutorizacionSISPEController.setEstado(EstadoCrud.VERSISPE);
            gsspSolicitudAutorizacionSISPEController.setTermCondicionAceptaDDJJ(false);
            gsspSolicitudAutorizacionSISPEController.iniciarValores_SISPE_Ver(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/sispe/ViewSispe.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/sispe/ViewSispe"; 
        } else if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_DEP")) {
            estado = EstadoCrud.VERDEPARTAMENTO;
            cargarDatosSolicitud(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/departamento/VerSolicitudAutorizaSeg.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //cargarDatosSolicitud(registro);
            //URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/cefoesp/VerSolicitudAutorizaSeg";
        } else if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SCBC")) {
            gsspSolicitudAutorizacionSCBCController.setEstado(EstadoCrud.VERSCBC);
            gsspSolicitudAutorizacionSCBCController.cargarDatosSolicitud(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/custodia/VerSCBC.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SSE")) {
            gsspSolicitudAutorizacionSSEController.setEstado(EstadoCrud.VERSSE);
            gsspSolicitudAutorizacionSSEController.cargarDatosSolicitud(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/eventos/VerSSE.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SISPA")) {
            gsspSolicitudAutorizacionSISPAController.setRegistro(registro);
            gsspSolicitudAutorizacionSISPAController.setEstado(EstadoCrud.VERSISPA);
            gsspSolicitudAutorizacionSISPAController.setTermCondicionAceptaDDJJ(false);
            gsspSolicitudAutorizacionSISPAController.iniciarValores_SISPA_Ver(registro);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/sispa/ViewSispa.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return URL_Formulario;
    }

    public void cargarDatosSolicitud(SspRegistro item) {
        registro = null;
        if (item != null) {
            registro = item;

            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            regAdminist = registro.getEmpresaId();
            regUserAdminist = ejbSbPersonaFacade.buscarPersonaXNumDoc(p_user.getLogin());

            administradoConRUC = true;

            //=============================================
            //============  Administrado PJ ===============
            //=============================================
            administRazonSocial = regAdminist.getRznSocial();
            administRUC = regAdminist.getRuc();

            //=============================================
            //============  Administrado PN ===============
            //=============================================
            administNombres = "";
            administNombres = p_user.getNombres() + " " + p_user.getApePat() + "" + p_user.getApeMat();
            administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
            administNumDoc = regUserAdminist.getNumDoc();

            //=============================================
            //======  Datos de la  Modalidad    ===========
            //=============================================
            //Tipo de Proceso de SERVICIO DE VIGILANCIA PRIVADA
            regTipoProcesoId = registro.getTipoProId();
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_DEP'");
            regTipoProcesoId = regTipoSeguridadSelected;
            regTipoSeguridadSelected = registro.getTipoProId();

            //Tipo de Registro
            regTipoRegistroList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_REGIST");
            regTipoRegistroSelected = registro.getTipoRegId();

            //Tipo de Operación
            regTipoOperacionList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_OPE");
            regTipoOperacionSelected = registro.getTipoOpeId();

            //Servicio Prestado CON ARMA, SIN ARMA
            regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP");
            if (registro.getSspServicioList() != null) {
                regServicioPrestadoSelected = registro.getSspServicioList().get(0).getServicioPrestadoId();
            }

            //No se habilita el Comprobante de Pago
            regNroComprobante = null;
            regDatoRecibos = null;

            //=============================================================
            //===============    Local Prestacion servicio     ============
            //=============================================================
            regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
            regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
            regDistritoRLSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");

            //Limpia los datos del Panel de Representante Legal Ver - PRINCIPAL
            representanteLegalVer = null;
            regRLPartidaRegistralVer = null;
            regRLAsientoRegistralVer = null;
            regZonaRegListRLVer = regZonaRegListRL;
            regZonaRegSelectedRLVer = null;
            regOficinaRegListRLVer = null;
            regOficinaRegSelectedRLVer = null;
            //regTipoViasListVer = regTipoViasList;
            regTipoViasSelectedVer = null;
            regDomicilioRLSelectedVer = null;
            //regDistritoRLSListVer = regDistritoRLSList;
            regDistritoRLSelectedVer = null;

            habilitarLocalPrestacionServicio = true;
            emptyModel = new DefaultMapModel();

            regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
            regTipLocalLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_LOCAL");
            regTipUsoLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");

            localAutorizacionListado = null;
            localAutorizacionListado = new ArrayList<LocalAutorizacionDetalle>();

            regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
            if (registro.getSspLocalRegistroList() != null) {
                regDistritoLPSSelected = registro.getSspLocalRegistroList().get(0).getLocalId().getDistritoId();
                //Detalle de Locales del distrito Seleccionado
                List<SspLocalAutorizacion> listDetalleLocal = new ArrayList<>();
                //En este caso como esta deshabilitado muestra todo
                listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaCefoesp(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipoSeguridadSelected.getId());
                //listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocalValida(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId(), registro.getId());
                if (listDetalleLocal != null) {
                    LocalAutorizacionDetalle detalleLocalTemp;
                    for (SspLocalAutorizacion localDetalle : listDetalleLocal) {
                        detalleLocalTemp = new LocalAutorizacionDetalle();
                        detalleLocalTemp.setId(localDetalle.getId());
                        detalleLocalTemp.setTipoUbicacionId(localDetalle.getTipoUbicacionId());
                        detalleLocalTemp.setDireccion((localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "");
                        detalleLocalTemp.setNroFisico((localDetalle.getNroFisico() != null) ? localDetalle.getNroFisico() : "");
                        detalleLocalTemp.setDistritoId(localDetalle.getDistritoId());
                        detalleLocalTemp.setReferencia((localDetalle.getReferencia() != null) ? localDetalle.getReferencia() : "");
                        if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                            detalleLocalTemp.setTipoLocalId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoLocalId());
                        } else {
                            detalleLocalTemp.setTipoLocalId(null);
                        }
                        if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                            detalleLocalTemp.setTipoUsoId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoUsoId());
                        } else {
                            detalleLocalTemp.setTipoUsoId(null);
                        }
                        detalleLocalTemp.setGeoLatitud((localDetalle.getGeoLatitud() != null) ? localDetalle.getGeoLatitud() : "");
                        detalleLocalTemp.setGeoLongitud((localDetalle.getGeoLongitud() != null) ? localDetalle.getGeoLongitud() : "");
                        localAutorizacionListado.add(detalleLocalTemp);
                    }
                    //BUSCA LOCAL PRINCIPAL
                    for (SspLocalRegistro sspLocalReg : registro.getSspLocalRegistroList()) {
                        if (sspLocalReg.getActivo() == JsfUtil.TRUE) {//SI LOCAL ACTIVO
                            localAutorizacionSelectedString = sspLocalReg.getLocalId().getId().toString();
                            localAutorizacionAnterior = sspLocalReg.getLocalId();
                        }
                    }
                    mostrarDatosLocalSeleccionado();
                }

                //Cuando tiene un Registro de una Resolución Asociada, buscamos los datos de la Resolución
            } else {
                localAutorizacionSelectedString = null;
                regDistritoLPSSelected = null;
                localAutorizacionSelectedString = null;
                localAutorizacionAnterior = null;

                regTipoViasLPSSelected = null;
                regDireccionLPS = null;
                regNroFisicoLPS = null;
                regTipUsoLPSSelected = null;
                regReferenciaLPS = null;
                nomArchivoLocal = null;
            }

           
            
            //=============================================================
            //===============    Local Prestacion servicio     ============
            //=============================================================
            lat_x2 = 0.0;
            long_x2 = 0.0;

            regDistritoLPSList2 = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
            regDistritoLPSSelected2 = null;

            regTipoViasLPSList2 = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");;
            regTipoViasLPSSelected2 = null;

//             regTipLocalLPSList2= null;
            regTipUsoLPSList2 = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");;
            regTipUsoLPSSelected2 = null;

            regDireccionLPS2 = null;
            regNroFisicoLPS2 = null;
            regReferenciaLPS2 = null;

            localAutorizacionListado = null;
            localAutorizacionListado = new ArrayList<LocalAutorizacionDetalle>();
            localAutorizacionListado2 = new ArrayList<>();
            localAutorizacionSelected2 = null;
            localAutorizacionSelectedString2 = null;

            if (registro.getSspLocalRegistroList() != null) {
                regDistritoLPSSelected2 = registro.getSspLocalRegistroList().get(0).getLocalId().getDistritoId();
                //Detalle de Locales del distrito Seleccionado
                List<SspLocalAutorizacion> listDetalleLocal = new ArrayList<>();
                //En este caso como esta deshabilitado muestra todo
                listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaCefoesp(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipoSeguridadSelected.getId());
                //listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocalValida(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId(), registro.getId());
                if (listDetalleLocal != null) {
                    LocalAutorizacionDetalle detalleLocalTemp;
                    for (SspLocalAutorizacion localDetalle : listDetalleLocal) {
                        detalleLocalTemp = new LocalAutorizacionDetalle();
                        detalleLocalTemp.setId(localDetalle.getId());
                        detalleLocalTemp.setTipoUbicacionId(localDetalle.getTipoUbicacionId());
                        detalleLocalTemp.setDireccion((localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "");
                        detalleLocalTemp.setNroFisico((localDetalle.getNroFisico() != null) ? localDetalle.getNroFisico() : "");
                        detalleLocalTemp.setDistritoId(localDetalle.getDistritoId());
                        detalleLocalTemp.setReferencia((localDetalle.getReferencia() != null) ? localDetalle.getReferencia() : "");
                        if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                            detalleLocalTemp.setTipoLocalId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoLocalId());
                        } else {
                            detalleLocalTemp.setTipoLocalId(null);
                        }
                        if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                            detalleLocalTemp.setTipoUsoId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoUsoId());
                        } else {
                            detalleLocalTemp.setTipoUsoId(null);
                        }
                        detalleLocalTemp.setGeoLatitud((localDetalle.getGeoLatitud() != null) ? localDetalle.getGeoLatitud() : "");
                        detalleLocalTemp.setGeoLongitud((localDetalle.getGeoLongitud() != null) ? localDetalle.getGeoLongitud() : "");
                        localAutorizacionListado2.add(detalleLocalTemp);
                    }
                    //BUSCA LOCAL PRINCIPAL
                    
                    for (SspLocalRegistro sspLocalReg : registro.getSspLocalRegistroList()) {
                        if (sspLocalReg.getActivo() == JsfUtil.TRUE) {//SI LOCAL ACTIVO
                            localAutorizacionSelectedString2 = sspLocalReg.getLocalId().getId().toString();
                            localAutorizacionAnterior2 = sspLocalReg.getLocalId();
                        }
                    }
                    mostrarDatosLocalSeleccionado2();
                }

                //Cuando tiene un Registro de una Resolución Asociada, buscamos los datos de la Resolución
            } else {
                localAutorizacionSelectedString2 = null;
                regDistritoLPSSelected2 = null;
                localAutorizacionSelectedString2 = null;
                localAutorizacionAnterior2 = null;

                regTipoViasLPSSelected2 = null;
                regDireccionLPS2 = null;
                regNroFisicoLPS2 = null;
                regTipUsoLPSSelected = null;
                regReferenciaLPS2 = null;

            }

            //=============================================
            //=========  Representante Legal   ============
            //=============================================
            habilitarRepresentanteLegal = true;

            //Representantes Legal del Administrado 
            //regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
            //regPersonasRLSelected = registro.getRepresentanteId();
            //Zonas Registrales
            regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
            //Tipos de Vias
            regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
            //Distrito del domicilio del Representante Legal 
            regDistritoRLSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
            //Lista de Tipo de Documento para el Representante Legal
            regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");

            personaDetalleListado = null;
            personaDetalleListado = new ArrayList<PersonaDetalle>();

            TipoBaseGt sTipoBase = new TipoBaseGt();
            //Si es Sucursal es Responsable Legal
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL

            //Detalle de relacion Persona como Representante Legal o Responsable Legal
            List<SbPersonaGt> listDetallePersona = new ArrayList<>();
            listDetallePersona = ejbSbPersonaFacade.listarPersonaXIdRelacionPersonaUnionSspRegistro(regAdminist.getId(), registro.getId(), sTipoBase.getId());
            if (listDetallePersona != null) {
                PersonaDetalle detallePersonaTemp;
                for (SbPersonaGt PersonaDetalle : listDetallePersona) {
                    detallePersonaTemp = new PersonaDetalle();
                    detallePersonaTemp.setId(PersonaDetalle.getId());
                    detallePersonaTemp.setNumDoc(PersonaDetalle.getNumDoc());
                    detallePersonaTemp.setApePat(PersonaDetalle.getApePat());
                    detallePersonaTemp.setApeMat(PersonaDetalle.getApeMat());
                    detallePersonaTemp.setNombres(PersonaDetalle.getNombres());
                    personaDetalleListado.add(detallePersonaTemp);
                }
                personaDetalleSelectedString = registro.getRepresentanteId().getId().toString();
                regTipoDocSelected = registro.getRepresentanteId().getTipoDoc();
                mostrarDomicilioRepresentanteLegal();
            }

            existeAsientoSunarpRL = true;

            //=============================================================
            //==============          Medios contactos           ==========
            //=============================================================
            habilitarMediosContactos = true;

            lstContactos = new ArrayList<SspContacto>();
            for (SspContacto contactoActivo : registro.getSspContactoList()) {
                if (contactoActivo.getActivo() == 1) {
                    lstContactos.add(contactoActivo);
                }
            }

            //=============================================================
            //===============    Licencia Municipal     ============
            //=============================================================
            habilitarLicenciaMunicipal = true;

            regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MUNI");

            licenciaMunicipal = ejbSspLicenciaMunicipalFacade.verUltimaLicenciaMunicipalXIdLocal(localAutorizacionAnterior.getId());
            if (licenciaMunicipal != null) {
                regTipMunicipalidadLPSSelected = licenciaMunicipal.getTipoMunicipalidad();
                listarMunicipalidades();
                regMunicLPSSelected = licenciaMunicipal.getMunicipalidadId();
                regNroLicenciaMunicLPS = licenciaMunicipal.getNumeroLicencia().toString();
                regGiroComercialLPS = licenciaMunicipal.getGiroComercial().toString();
                regFechaEmisLPS = licenciaMunicipal.getFechaIni();
                regFechaVencLPS = licenciaMunicipal.getFechaFin();
                regDisabledCalendar = false;
                if (regFechaVencLPS == null) {
                    regDisabledCalendar = true;
                    regIndeterminadoLPS = true;
                }
            } else {
                regTipMunicipalidadLPSSelected = null;
                regNroLicenciaMunicLPS = null;
                regGiroComercialLPS = null;
                regFechaEmisLPS = null;
                regFechaVencLPS = null;
                regIndeterminadoLPS = false;
                regDisabledCalendar = true;
                regMunicLPSList = null;
                regMunicLPSSelected = null;
            }

            //Falta el Archivo
            nomArchivoLM = null;

            //=============================================================
            //=======================    ACCIONISTAS      ================
            //=============================================================
            habilitarAccionistas = true;

            lstSocios = new ArrayList<SspParticipante>();
            for (SspParticipante socioActivo : registro.getSspParticipanteList()) {
                if (socioActivo.getActivo() == 1) {
                    lstSocios.add(socioActivo);
                }
            }
            
            //=============================================================
            //=======================    INSTRUCTOR      ================
            //=============================================================
            lstCapacitadores=registro.getSspCefoespInstructorList();
            //=============================================================
            //=======================    POLIGONO      ================
            //=============================================================
           
            regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");

            
            
            cefoeNumResol=registro.getSspPoligonoList().get(0).getNroResolucion();
            System.out.println("cefoeNumResol"+cefoeNumResol);
            cefoeFechaResolIni=registro.getSspPoligonoList().get(0).getFechaIni();
            System.out.println("cefoeFechaResolIni"+cefoeFechaResolIni);
            cefoeFechaResolFin=registro.getSspPoligonoList().get(0).getFechaFin();
            cefoeTipoLocal=registro.getSspPoligonoList().get(0).getAlquilado().toString();
            if(registro.getSspPoligonoList().get(0).getAlquilado().equals(0)){
                cefoeTipoLocal="A";
            }else{
                 cefoeTipoLocal="P";
            }
            System.out.println("cefoeTipoLocal"+cefoeTipoLocal);
            
            
            /*
                //poligonos
    private String cefoeNumResol;
    private Date cefoeFechaResolIni;
    private Date cefoeFechaResolFin;
    private String cefoeTipoLocal;

    private SspPoligono sspPoligono;

    private String regFormModalMapa_tipoModal;
    private boolean renderVerMapa2;

    private Double lat_x2 = 0.0;
    private Double long_x2 = 0.0;

    private List<SbDistritoGt> regDistritoLPSList2;
    private SbDistritoGt regDistritoLPSSelected2;

    private List<LocalAutorizacionDetalle> localAutorizacionListado2;
    private SspLocalAutorizacion localAutorizacionSelected2;
    private String localAutorizacionSelectedString2;

    private List<TipoBaseGt> regTipoViasLPSList2;
    private TipoBaseGt regTipoViasLPSSelected2;

    private List<TipoSeguridad> regTipLocalLPSList2;

    private List<TipoSeguridad> regTipUsoLPSList2;
    private TipoSeguridad regTipUsoLPSSelected2;

    private String regDireccionLPS2;
    private String regNroFisicoLPS2;
    private String regReferenciaLPS2;
            
            
            */
            //=============================================================
            //=======================    CARTA FIANZA      ================
            //=============================================================
            regTipoFinancieraCFList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_FIN_SSP");
            if (registro.getCartaFianzaId() != null) {
                regBuscaCartaFianzaSelectedString = registro.getCartaFianzaId().getId().toString();
                regNroCartaFianza = registro.getCartaFianzaId().getNumero();
                regTipoFinancieraCFSelected = registro.getCartaFianzaId().getTipoFinancieraId();
                listarEntidadFinancieras();
                regEntidadFinancieraCFSelected = registro.getCartaFianzaId().getFinancieraId();
                regMontoCartaFianza = registro.getCartaFianzaId().getMonto();
                regFechVigenciaIni = registro.getCartaFianzaId().getVigenciaInicio();
                regFechVigenciaFin = registro.getCartaFianzaId().getVigenciaFin();
            } else {
                regBuscaCartaFianzaSelectedString = null;
                regNroCartaFianza = null;
                regTipoFinancieraCFSelected = null;
                regEntidadFinancieraCFList = null;
                regEntidadFinancieraCFSelected = null;
                regMontoCartaFianza = null;
                regFechVigenciaIni = null;
                regFechVigenciaFin = null;
            }
            //Falta el Archivo
            nomArchivoCF = null;

            //poligonos
            sspPoligono = new SspPoligono();

        } else {
            JsfUtil.mensajeError("No se encontró datos de la solicitud");
        }
    }

    public boolean renderBtnConstancia(Map item) {
        boolean validar = false;
        //Solo APROBADO
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_APR")) {
                validar = true;
            }
        }
        return validar;
    }

    public StreamedContent imprimirConstancia(Map reg) {
        NeArchivo a = neDocumentoFacade.archivoResolucionGSSP((Long) reg.get("ID"));
        if (a == null) {
            return null;
        }
        StreamedContent r = null;
        try {
            String path = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.bundle("PathUpload");
            FileInputStream f = new FileInputStream(path + a.getNombre());
            r = new DefaultStreamedContent(f, "application/pdf", a.getNombre());
        } catch (FileNotFoundException ex) {
            r = null;
            JsfUtil.mensajeError(ex, "No se encontro el archivo");
        }
        return r;
    }

    public boolean renderBtnRenovar(Map item) {
        boolean validar = false;
        /*
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_APR")
                    && item.get("FECHA_FIN") != null && item.get("FECHA_INI") != null) {
                validar = true;
            }
        }
         */
        return validar;
    }

    public void mostrarRenovar(Map item) {

    }

    public void openDlgSolicitudDuplicado(Map item) {

    }

    public boolean renderBtnFinProcedimiento(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_TRA") || item.get("ESTADO_CODPROG").equals("TP_ECC_OBS") || item.get("ESTADO_CODPROG").equals("TP_ECC_PRE")) {
                validar = true;
            }
        }
        return validar;
    }

    public void openDlgConfirmarDesestimiento(Map item) {
        if (item == null) {
            JsfUtil.mensajeError("No se puede generar el desestimiento");
            return;
        }

        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        observacion = "";
        RequestContext.getCurrentInstance().execute("PF('wvConfirmarDesestimiento').show()");
        RequestContext.getCurrentInstance().update("confirmarDesestimientoForm");
    }

    /*public void openDlgcompletarProceso() {
        if (!resultadosSeleccionados.isEmpty()) {
            progress = 0;
            actualProcesado = 0;
            totalProcesados = 0;
            blnProcesoTransmision = false;
            RequestContext.getCurrentInstance().execute("PF('wvDialogCompletarProceso').show()");
            RequestContext.getCurrentInstance().update("frmCompletarProceso");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar al menos un registro para transmitir");
        }
    }*/
    public Integer getProgressCompletarProceso() {
        if (totalProcesados == null) {
            totalProcesados = 0;
        }
        if (actualProcesado == null) {
            actualProcesado = 0;
        }
        if (progress == null) {
            progress = 0;
        } else if (totalProcesados > 0) {
            progress = ((100 * actualProcesado) / totalProcesados);
            if (progress > 100) {
                progress = 100;
            }
            if (progress < 0) {
                progress = 0;
            }
        }

        return progress;
    }

    public void transmitirSolicitud() {
        if (!resultadosSeleccionados.isEmpty()) {

            int cont = 0;

            blnProcesoTransmision = true;
            totalProcesados = resultadosSeleccionados.size();

            for (Map item : resultadosSeleccionados) {
                actualProcesado++;
                registro = ejbSspRegistroFacade.buscarRegistroById((Long) item.get("ID"));

                String estadoAnterior = registro.getEstadoId().getCodProg();

                if (!validaTransmitir()) {
                    continue;
                }

                if (!guardarDatosTransmiteGeneraSolicitudAutoServSeg(registro)) {
                    continue;
                }

                if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SISPE") && estadoAnterior.equals("TP_ECC_CRE")) { //REGISTRAR CARNE + GENERAR EXPEDIENTE EXPEDIENTE CARNE
                    String xTipSeguridadModalidadCarne = "TP_MCO_SIS";
                    if (!guardarDatosCarne_GenerarExpCarne(registro, estadoAnterior, xTipSeguridadModalidadCarne)) {
                        continue;
                    }
                }

                if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SISPA") && estadoAnterior.equals("TP_ECC_CRE")) { //REGISTRAR CARNE + GENERAR EXPEDIENTE EXPEDIENTE CARNE
                    String xTipSeguridadModalidadCarne = "TP_MCO_PAT";
                    if (!guardarDatosCarne_GenerarExpCarne(registro, estadoAnterior, xTipSeguridadModalidadCarne)) {
                        continue;
                    }
                }

                cont++;
                JsfUtil.mensaje("Se transmitió el registro Id: " + registro.getId() + ", con Nro Solicitud: " + registro.getNroSolicitiud());
                registro = null;
            }
            reiniciarCamposBusqueda();
            resultados = new ArrayList();
            blnProcesoTransmision = true;

            RequestContext.getCurrentInstance().execute("PF('wvDialogCompletarProceso').hide()");
            RequestContext.getCurrentInstance().update("listForm");
            if (cont > 0) {
                JsfUtil.mensaje("Se procesó " + cont + " registro(s) correctamente.");
            } else {
                JsfUtil.mensajeAdvertencia("No se procesó ningún registro.");
            }

        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar al menos un registro para transmitir");
        }
    }

    public boolean guardarDatosCarne_GenerarExpCarne(SspRegistro sspRegistroSolicSeguridad, String estadoAnterior, String xTipSeguridadModalidadCarne) {

        try {

            String fileName = null;
            String nombreArchivoFoto = null;

            SspPersonaFoto foto = new SspPersonaFoto();
            foto.setAlumnoId(null);
            foto.setId(null);
            foto.setFecha(new Date());
            foto.setActivo(JsfUtil.TRUE);
            foto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            foto.setAudNumIp(JsfUtil.getIpAddress());
            foto.setModuloId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN"));

            SspRequisito requisitoFoto = new SspRequisito();
            requisitoFoto = ejbSspRequisitoFacade.buscarSspRequisito_By_RegistroId_TipoRequisitoId(sspRegistroSolicSeguridad, ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_FD"));

            SspArchivo sspArchivoFoto = new SspArchivo();
            sspArchivoFoto = ejbSspArchivoFacade.buscarSspArchivo_By_RequisitoId(requisitoFoto);
            nombreArchivoFoto = sspArchivoFoto.getNombre();
            fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_FOTO").getValor() + nombreArchivoFoto));

            if (fotoByte != null) {
                fileName = nombreArchivoFoto.toUpperCase();
                fileName = "FOTO_C" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_FOTOCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + fileName), fotoByte);
            } else {
                fileName = null;
            }
            foto.setNombreFoto(fileName);
            foto = (SspPersonaFoto) JsfUtil.entidadMayusculas(foto, "");

            SspRegistro SspRegistroCarnet = new SspRegistro();
            SspRegistroCarnet.setActivo(JsfUtil.TRUE);
            SspRegistroCarnet.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            SspRegistroCarnet.setAudNumIp(JsfUtil.getIpAddress());
            SspRegistroCarnet.setCarneId(new SspCarne());
            SspRegistroCarnet.setComprobanteId(null);
            SspRegistroCarnet.setEmpresaId(sspRegistroSolicSeguridad.getEmpresaId());
            SspRegistroCarnet.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_TRA"));
            SspRegistroCarnet.setFecha(new Date());
            SspRegistroCarnet.setFechaIni(null);
            SspRegistroCarnet.setFechaFin(null);
            SspRegistroCarnet.setId(null);
            SspRegistroCarnet.setNroExpediente(null);
            SspRegistroCarnet.setNroSolicitiud(obtenerNroSolicitudEmision());
            SspRegistroCarnet.setObservacion("POR EMITIR CARNE");
            SspRegistroCarnet.setRegistroId(null);
            SspRegistroCarnet.setRepresentanteId(null);
            SspRegistroCarnet.setSedeSucamec(sspRegistroSolicSeguridad.getSedeSucamec());
            SspRegistroCarnet.setCarneSolicitudId(sspRegistroSolicSeguridad);
            SspRegistroCarnet.setSspRegistroEventoList(new ArrayList());
            SspRegistroCarnet.setSspRegistroList(new ArrayList());
            SspRegistroCarnet.setSspRequisitoList(new ArrayList());
            SspRegistroCarnet.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR"));
            SspRegistroCarnet.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));
            SspRegistroCarnet.setTipoProId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN"));
            SspRegistroCarnet.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
            SspRegistroCarnet = (SspRegistro) JsfUtil.entidadMayusculas(SspRegistroCarnet, "");
            SspRegistroCarnet.getCarneId().setActivo(JsfUtil.TRUE);
            SspRegistroCarnet.getCarneId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
            SspRegistroCarnet.getCarneId().setAudNumIp(JsfUtil.getIpAddress());
            SspRegistroCarnet.getCarneId().setCese(JsfUtil.FALSE);
            SspRegistroCarnet.getCarneId().setDetalleDupli(null);
            SspRegistroCarnet.getCarneId().setEmitida(JsfUtil.FALSE);
            SspRegistroCarnet.getCarneId().setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECRN_NVO"));
            SspRegistroCarnet.getCarneId().setFechaBaja(null);
            SspRegistroCarnet.getCarneId().setFechaEmision(null);
            SspRegistroCarnet.getCarneId().setFechaIni(null);
            SspRegistroCarnet.getCarneId().setFechaFin(null);
            SspRegistroCarnet.getCarneId().setFotoId(foto);
            SspRegistroCarnet.getCarneId().setHashQr(null);
            SspRegistroCarnet.getCarneId().setId(null);
            SspRegistroCarnet.getCarneId().setModalidadId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg(xTipSeguridadModalidadCarne));
            SspRegistroCarnet.getCarneId().setNroCarne(0);
            SspRegistroCarnet.getCarneId().setSspRegistroList(new ArrayList());
            SspRegistroCarnet.getCarneId().setVigilanteId(sspRegistroSolicSeguridad.getEmpresaId());
            SspRegistroCarnet.setCarneId((SspCarne) JsfUtil.entidadMayusculas(SspRegistroCarnet.getCarneId(), ""));

            adicionaEvento(SspRegistroCarnet, "POR EMITIR CARNE");
            ejbSspRegistroFacade.create(SspRegistroCarnet);

            //Generar Expediente Carne
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt cliente = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
            Integer usuaTramDoc = ejbSbUsuarioFacadeGt.obtenerIdUsuarioTramDoc("USRWEB");

            if (!generaExpedienteCarne(SspRegistroCarnet, usuaTramDoc, cliente, estadoAnterior, sspRegistroSolicSeguridad)) {
                JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro con código: " + registro.getId());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean generaExpedienteCarne(SspRegistro reg, Integer usuaTramDoc, SbPersonaGt cliente, String estadoAnterior, SspRegistro SspRegistroAnt) {
        boolean validacion = true, estadoTraza = true;
        Integer nroProceso = null;

        try {

            SspRegistroAnt.setCarneSolicitudId(reg);
            ejbSspRegistroFacade.edit(SspRegistroAnt);

            registro = ejbSspRegistroFacade.buscarRegistroById(reg.getId());

            if (estadoAnterior.equals("TP_ECC_CRE")) {
                if (registro.getNroExpediente() != null && !registro.getNroExpediente().isEmpty()) {
                    return true;
                }
                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                String asunto = ((reg.getEmpresaId().getRuc() != null) ? (((reg.getEmpresaId().getRznSocial() != null) ? reg.getEmpresaId().getRznSocial().trim() : reg.getEmpresaId().getNombresYApellidos().trim()) + " - RUC: " + reg.getEmpresaId().getRuc()) : (reg.getEmpresaId().getApePat() + " " + reg.getEmpresaId().getApeMat() + " " + reg.getEmpresaId().getNombres() + " - " + (reg.getEmpresaId().getTipoDoc() != null ? " - " + reg.getEmpresaId().getTipoDoc().getNombre() : " - DNI") + ": " + reg.getEmpresaId().getNumDoc()));
                String titulo = "";
                if (registro.getCarneId().getVigilanteId().getTipoDoc() != null) {
                    titulo = registro.getCarneId().getVigilanteId().getTipoDoc().getNombre() + ": ";
                }
                titulo += registro.getCarneId().getVigilanteId().getNumDoc() + ", " + registro.getCarneId().getVigilanteId().getNombreCompleto() + " - Referencia a la Solictud Nro. " + SspRegistroAnt.getNroSolicitiud();
                nroProceso = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_nroProceso").getValor());//1430-> GSSP-CARNE (WEB)

                String expediente = wsTramDocController.crearExpediente_gssp(reg.getComprobanteId(), cliente, asunto, nroProceso, usuaTramDoc, usuaTramDoc, titulo);

                System.out.println("expediente->" + expediente);
                if (expediente != null) {
                    List<Integer> acciones = new ArrayList();
                    acciones.add(17);   // En Evaluación
                    registro.setNroExpediente(expediente);

                    try {
                        ///////// Para obtener usuario a derivar /////////
                        Long sistemaId = 2L; //p_user.getSistema()
                        Long sedeLimaId = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AREA_TRAM").getId(); //reg.getSedeSucamec().getId()
                        SbDerivacionCydoc derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionCyDoc(sistemaId, ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN").getId(), sedeLimaId);
                        if (derivacion == null) {
                            System.err.println("***Error derivación, ID:" + registro.getId() + ", expediente: " + expediente);
                            JsfUtil.mensajeError("No se pudo encontrar a un usuario parametrizado para derivación del expediente");
                            return true;
                        }
                        /////////

                        // asignar a personal de TD para que lo atienda
                        String strUsuarioDerivar = ejbSbParametroFacade.obtenerParametroXNombre("TransmiteCarneGSSP_SISPE").getValor();//MRIVAS                        
                        estadoTraza = wsTramDocController.asignarExpediente(expediente, "USRWEB", strUsuarioDerivar, asunto, "", acciones, false, null);
                        registro.setUsuarioRecepcionId(derivacion.getUsuarioDestinoId());

                        if (!estadoTraza) {
                            System.err.println("***No se pudo generar la traza del expediente " + expediente + ", ID:" + registro.getId());
                            //JsfUtil.mensajeError("No se pudo generar la traza del expediente " + expediente);
                        } else {
                            usuarioArchivar = derivacion.getUsuarioDestinoId().getLogin();
                        }
                        ////////

                        ejbSspRegistroFacade.edit(registro); //Actualizamos parametros

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    validacion = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            validacion = false;
        }
        return validacion;
    }

    public boolean guardarDatosTransmiteGeneraSolicitudAutoServSeg(SspRegistro sspRegistro) {
        try {
            //Solo cuando esta en estado CREADO u OBSERVADO

            String estadoRegistro = sspRegistro.getEstadoId().getCodProg();
            if (estadoRegistro.equals("TP_ECC_CRE") || estadoRegistro.equals("TP_ECC_OBS")) {

                if (sspRegistro.getNroSolicitiud().equals("S/N") || sspRegistro.getNroSolicitiud() == null) {//Si no tiene numero de Solicitud le genera una
                    sspRegistro.setNroSolicitiud(obtenerNroSolicitudEmision());
                }

                if (estadoRegistro.equals("TP_ECC_OBS") && sspRegistro.getNroExpediente() != null) {
                    sspRegistro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_PRE")); //Actualiza a PRESENTADO A SIST. GGSP
                    sspRegistro.setObservacion(null);
                } else {
                    sspRegistro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_TRA")); //Actualiza a TRANSMITIDO A SIST. INTEGRADO
                    sspRegistro.setObservacion(null);
                }

                //Asignar a personal de TD para que lo atienda
                String strUsuarios = "";
                if (ejbSbParametroFacade.obtenerParametroXNombre("TransmiteAutorizaGSSP_usuTD_TP_AREA_TRAM") != null) {
                    strUsuarios = ejbSbParametroFacade.obtenerParametroXNombre("TransmiteAutorizaGSSP_usuTD_TP_AREA_TRAM").getValor();
                    sspRegistro.setUsuarioRecepcionId(ejbSbUsuarioFacadeGt.find(ejbSspRegistroFacade.obtenerIdUsuarioTDparaTransmitirAutorizacionSeguridad(strUsuarios)));
                } else {
                    JsfUtil.mensajeAdvertencia("No ha sido configurado la persona de Tramite Documentario que recepciona la Solicitud");
                }

                if (sspRegistro.getSspRegistroEventoList() == null) {
                    sspRegistro.setSspRegistroEventoList(new ArrayList());
                }
                adicionaEvento(sspRegistro, "");
                ejbSspRegistroFacade.edit(sspRegistro);
            }
        } catch (EJBException e) {
            System.err.println("Exception ID:" + registro.getId());
            Exception cause = e.getCausedByException();
            if (cause instanceof ConstraintViolationException) {
                @SuppressWarnings("ThrowableResultIgnored")
                ConstraintViolationException cve = (ConstraintViolationException) e.getCausedByException();
                for (ConstraintViolation<? extends Object> v : cve.getConstraintViolations()) {
                    System.err.println(v);
                    System.err.println("==>>" + v.getMessage());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean guardarDatosSolicitudAutoServSeg() {
        try {
            registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_TRA"));
            if (registro.getSspRegistroEventoList() == null) {
                registro.setSspRegistroEventoList(new ArrayList());
            }
            adicionaEvento(registro, "");
            ejbSspRegistroFacade.edit(registro);

        } catch (EJBException e) {
            System.err.println("Exception ID:" + registro.getId());
            Exception cause = e.getCausedByException();
            if (cause instanceof ConstraintViolationException) {
                @SuppressWarnings("ThrowableResultIgnored")
                ConstraintViolationException cve = (ConstraintViolationException) e.getCausedByException();
                for (ConstraintViolation<? extends Object> v : cve.getConstraintViolations()) {
                    System.err.println(v);
                    System.err.println("==>>" + v.getMessage());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void archivarExpediente(String nroExpediente, String usuario) {
        boolean estadoTraza = wsTramDocController.archivarExpediente(nroExpediente, usuario, "");
    }

    public boolean guardarDatosRecibo(boolean flagComprobante) {
        try {
            if (flagComprobante && registro.getComprobanteId() != null) {
                for (SbReciboRegistro recReg : registro.getComprobanteId().getSbReciboRegistroList()) {
                    if (recReg.getActivo() == 1) {
                        recReg.setNroExpediente(registro.getNroExpediente());
                        break;
                    }
                }
                ejbSbRecibosFacade.edit(registro.getComprobanteId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void adicionaEvento(SspRegistro reg, String observacion) {
        SspRegistroEvento evento = new SspRegistroEvento();
        evento.setId(null);
        evento.setActivo(JsfUtil.TRUE);
        evento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        evento.setAudNumIp(JsfUtil.getIpAddress());
        evento.setRegistroId(reg);
        evento.setFecha(new Date());
        evento.setObservacion(observacion);
        evento.setTipoEventoId(reg.getEstadoId());
        evento.setUserId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
        evento = (SspRegistroEvento) JsfUtil.entidadMayusculas(evento, "");
        if (reg.getSspRegistroEventoList() == null) {
            reg.setSspRegistroEventoList(new ArrayList());
        }
        reg.getSspRegistroEventoList().add(evento);
    }

    public boolean validaTransmitir() {
        if (registro.getEstadoId().getCodProg().equals("TP_ECC_TRA")) {//TRANSMITIDO
            JsfUtil.mensajeAdvertencia("El registro con ID " + registro.getId() + " ya ha sido transmitido");
            return false;
        }
        /*if (registro != null && registro.getTipoRegId().getCodProg().equals("TP_REGIST_NOR") && registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
            if (registro.getCarneId().getFechaFin() != null && JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(registro.getCarneId().getFechaFin())) > 0) {
                JsfUtil.mensajeAdvertencia("Ud. no puede transmitir este DUPLICADO porque La Solicitud de Autorizacion de Seguridad se encuentra vencido.");
                return false;
            }

        }*/
        return true;
    }

    public boolean renderBtnDuplicado(Map item) {
        boolean validar = false;
        /*
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_APR")
                    && item.get("FECHA_FIN") != null && JsfUtil.getFechaSinHora((Date) item.get("FECHA_FIN")).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0) {
                validar = true;
            }
            if (item.get("FECHA_INI") != null && JsfUtil.getFechaSinHora((Date) item.get("FECHA_INI")).compareTo(JsfUtil.getFechaSinHora(fechaLimiteDuplicado)) >= 0) {
                validar = false;
            }
        }
         */
        return validar;
    }

    public void onloadCreateSolicitudDefault() {
        RequestContext.getCurrentInstance().execute("PF('wvDlgTerminosCondicionesUsuario').show()");
    }

    public void onloadCreateCambioRLDefault() {
        RequestContext.getCurrentInstance().execute("PF('wvDlgTerminosCondicionesACRL').show()");
    }

    public void onloadCreateCambioDomicLegalDefault() {
        RequestContext.getCurrentInstance().execute("PF('wvDlgTerminosCondicionesACDL').show()");
    }

    public void iniciarValores_VerSolicitud() {

        //DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        
        //regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        //regUserAdminist = ejbSbPersonaFacade.buscarPersonaXNumDoc(p_user.getLogin());
        administradoConRUC = false;

        //=============================================
        //============  Administrado PJ ===============
        //=============================================
        estado = null;
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();

        //=============================================
        //============  Administrado PN ===============
        //=============================================
        administNombres = "";
        //administNombres = p_user.getNombres()+" "+p_user.getApePat()+""+p_user.getApeMat();
        administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
        administNumDoc = regUserAdminist.getNumDoc();

        //=============================================
        //======  Datos de la  Modalidad    ===========
        //=============================================
        regTipoProcesoId = null;

        if (tipoAdministradoUsuario.equals("PersonaJuridica")) {
            //TP_GSSP_MOD_DEP        SERVICIO DE VIGILANCIA PRIVADA
            //TP_GSSP_MOD_SCBC      SERVICIO DE CUSTODIA DE BIENES CONTROLADOS
            //TP_GSSP_MOD_TEC       SERVICIO DE TECNOLOGÍA DE SEGURIDAD
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_DEP'");
            regTipoSeguridadSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_DEP");
            regTipoProcesoId = regTipoSeguridadSelected;
        } else {
            //TP_GSSP_MOD_SISPE     SERVICIO INDIVIDUAL DE SEGURIDAD PERSONAL
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_SISPE'");
            regTipoSeguridadSelected = null;
        }

        regFormaTipoSeguridadSelected = null;

        regTipoRegistroList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_REGIST");
        regTipoRegistroSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR"); //INICIAL
        regTipoOperacionList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_OPE");
        regTipoOperacionSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"); //INICIAL 

        //SI_ARMA_SSP   SIN ARMA
        //NO_ARMA_SSP   CON ARMA
        //regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP', 'NO_ARMA_SSP'");
        regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP");
        regServicioPrestadoSelected = null;

        regNroComprobante = null;
        regDatoRecibos = null;

        //=============================================
        //=========  Representante Legal   ============
        //=============================================
        habilitarRepresentanteLegal = false;

        //regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_ZR_1','TP_ZR_2','TP_ZR_3','TP_ZR_5','TP_ZR_6','TP_ZR_7','TP_ZR_8','TP_ZR_9','TP_ZR_10','TP_ZR_11','TP_ZR_12','TP_ZR_13','TP_ZR_14'");
        regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
        regZonaRegSelectedRL = null;

        //regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        regTipoViasSelected = null;

        regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");
        regTipoDocSelected = null;

        regNroDocBuscar = null;
        regDatoPersonaByNumDocRL = null;

        regDomicilioRLSelected = null;

        regRLPartidaRegistral = null;
        regRLAsientoRegistral = null;

        existeAsientoSunarpRL = false;

        //=============================================================
        //========  Resolucion Designa Representante Legal   ==========
        //=============================================================
        regTipoDocEPList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOC_CON','TP_DOC_REP','TP_DOC_OFI','TP_DOC_RES','TP_DOC_CVN','TP_DOC_CAR','TP_DOC_NOTVIR','TP_DOC_OTRO'");
        regTipoDocEPSelected = null;

        regNombDocEP = null;
        regNumDocEP = null;
        regFechaEmisEP = null;

        //Representantes Legal del Administrado 
        regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
        regPersonasRLSelected = null;

        regDisabledFormResolucionRL = false;

        //=============================================================
        //==============          Medios contactos           ==========
        //=============================================================
        habilitarMediosContactos = false;

        //regPrioridadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_CONTAC_PRIN','TP_CONTAC_SEC'");
        regPrioridadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_CONTAC");
        regPrioridadLPSSelected = null;

        //regTipoMedioContactoLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_MEDCO_COR','TP_MEDCO_FAX','TP_MEDCO_FIJ','TP_MEDCO_MOV'");
        regTipoMedioContactoLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MEDCO");
        regTipoMedioContactoLPSSelected = null;

        lstContactos = new ArrayList<SspContacto>();
        lstContactosList = null;
        selectedContacto = null;
        contRegContacto = 0L;
        regtextoContactoLPS = null;
        regtextoCorreoContactoLPS = null;

        //=============================================================
        //===============    Local Prestacion servicio     ============
        //=============================================================
        habilitarLocalPrestacionServicio = false;
        emptyModel = new DefaultMapModel();

        regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
        regDistritoLPSSelected = null;

        localAutorizacionListado = null; // ejbSspLocalAutorizacionFacade.listarLocalAutorizacionXidDistritoXIdEmpresa(0L, 0L);
        localAutorizacionSelected = null;
        localAutorizacionSelectedString = null;

        //regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");

        regTipoViasLPSSelected = null;

        //regTipLocalLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");        
        regTipLocalLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_LOCAL");

        regTipUsoLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
        //regTipUsoLPSSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);
        if (regTipUsoLPSList != null) {
            regTipUsoLPSSelected = regTipUsoLPSList.get(0);
        } else {
            regTipUsoLPSSelected = null;
        }

        regDireccionLPS = null;
        regNroFisicoLPS = null;
        regReferenciaLPS = null;

        nomArchivoLocal = null;

        //===================================
        //regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        regTipoViasLPSSelected_Form = null;

        //regTipLocalLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");        
        regTipLocalLPSList_Form = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_LOCAL");
        regTipLocalLPSList_Form = null;

        regTipUsoLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
        //regTipUsoLPSSelected_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);
        if (regTipUsoLPSList_Form != null) {
            regTipUsoLPSSelected_Form = regTipUsoLPSList_Form.get(0);
        } else {
            regTipUsoLPSSelected_Form = null;
        }

        regDireccionLPS_Form = null;
        regNroFisicoLPS_Form = null;
        regReferenciaLPS_Form = null;

        regGeoLatitudLPS_Form = null;
        regGeoLongitudPS_Form = null;

        //=============================================================
        //========================    ALMACEN     ==================
        //=============================================================
        //regAlmacenLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'CAJ_FUE_ARM','ARMERIA_ARM'");        
        regAlmacenLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_ALM_ARM");
        regAlmacenLPSSelected = null;

        regCantArmeriaLPS = new BigInteger("0");

        lstAlmacenes = new ArrayList<SspAlmacen>();
        selectedAlmacen = null;
        contRegAlmacen = 0L;

        //=============================================================
        //===============    Licencia Municipal     ============
        //=============================================================
        habilitarLicenciaMunicipal = false;

        regNroLicenciaMunicLPS = null;
        regFechaEmisLPS = null;
        regFechaVencLPS = null;

        //regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MUNI");
        regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MUNI");
        regTipMunicipalidadLPSSelected = null;

        regMunicLPSList = null;
        regMunicLPSSelected = null;

        regGiroComercialLPS = null;
        nomArchivoLM = null;

        regIndeterminadoLPS = false;
        regDisabledCalendar = false;

        //=============================================================
        //=======================    ACCIONISTAS      ================
        //=============================================================
        habilitarAccionistas = false;

        regTipDocAccionistaList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");
        regTipDocAccionistaSelected = null;

        regNumeroDocAccionista = null;

        regDatosSocioEncontrado = null;
        regNombreSocioEncontrado = null;

        //regTipoSocioList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_ACCNSTA_ACC','TP_ACCNSTA_PARTC','TP_ACCNSTA_SOCI','TP_ACCNSTA_DIRECT','TP_ACCNSTA_APOD'");      
        regTipoSocioList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ACCNSTA");
        regTipoSocioSelected = null;

        regCargoSocio = null;
        nomArchivoSocio = null;
        lstSocios = new ArrayList<SspParticipante>();
        contRegSocio = 0L;

        //===============================================
        //================CAPACITADORES ACREDITADOS===============================
        //===============================================
        lstCapacitadores = new ArrayList<SspCefoespInstructor>();
        contRegCapacitador = 0L;
        nomArchivoPE = null;
        //=============================================================
        //=======================    CARTA FIANZA      ================
        //=============================================================

        habilitarCartaFianza = false;

        regNroCartaFianza = null;

        regTipoFinancieraCFList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_FIN_SSP");
        regTipoFinancieraCFSelected = null;

        regEntidadFinancieraCFList = null;
        regEntidadFinancieraCFSelected = null;

        regMontoCartaFianza = null;
        regFechVigenciaIni = null;
        regFechVigenciaFin = null;

        nomArchivoCF = null;

        //regTipoMonedaCFList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MNDA");        
        regTipoMonedaCFList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MNDA");
        regTipoMonedaCFSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_MNDA_SOL");

        //======================================================================
        //=====    CHECKED - FORMAS DE TECNOLOGIA DE SEGURIDAD     =============
        //======================================================================
        regFormasServTecnoSegList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_MCO_TEC"); //TECNOLOGÍA DE SEGURIDAD	       

        regForma_TP_MCO_TEC_CRAIDMR = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_CRAIDMR"); //Con centrales receptoras de Alarmas, instalación, desinstalación, monitoreo y respuesta.
        regForma_TP_MCO_TEC_PSGPS = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_PSGPS"); //Posicionamiento Satelital (GPS)
        regForma_TP_MCO_TEC_IDMSVD = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_IDMSVD"); //Instalación, desinstalación y monitoreo de sistema de video a distancia.
        regForma_TP_MCO_TEC_IDMSDEI = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_IDMSDEI"); //Instalación, desinstalación y monitoreo de sistema de detección y de extinción de incendios.       
        regForma_TP_MCO_TEC_IDMSCA = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_IDMSCA"); //Instalación, desinstalación y monitoreo de sistema de control de accesos.
        regForma_TP_MCO_TEC_EPIS = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_EPIS"); //Ejecución de proyectos de ingeniería de seguridad.
        regForma_TP_MCO_TEC_SCM = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_SCM"); //Sistema de control de mercaderías.
        regForma_TP_MCO_TEC_OTRO = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_OTRO"); //Otros.

        selectedOptions = null;
        lstCheckedForma = new ArrayList<SspForma>();
        contRegCheckForma = 0L;
        selectOptionOtroVal = null;
        regDisabledFormServTecn = false;

        usuarioArchivar = "";

        //=============================================
        //=========  Foto   ============
        //=============================================
        fileFOTO = null;
        fotoByte = null;
        archivoFOTO = null;
        nomArchivoFOTO = null;
        habilitarAdjuntarFoto = false;

        //===================================================
        //=========  Certificado Fisico Mental   ============
        //===================================================
        fileCertFiscoMntal = null;
        certFiscoMntalByte = null;
        archivoCertFiscoMntal = null;
        nomArchivoCertFiscoMntal = null;
        habilitarAdjuntarCertFiscoMntal = false;

        //===================================================
        //=========  Copia de Contrato   ============
        //===================================================
        fileCopyContrato = null;
        copyContratoByte = null;
        archivoCopyContrato = null;
        nomArchivoCopyContrato = null;
        habilitarAdjuntarCopyContrato = false;

        //==================================================================
        //=========  Copia de Poliza Seguro Trabajo de Riesgo   ============
        //==================================================================
        fileCopyPolizaSeguro = null;
        copyPolizaSeguroByte = null;
        archivoCopyPolizaSeguro = null;
        nomArchivoCopyPolizaSeguro = null;
        habilitarAdjuntarCopyPolizaSeguro = false;

        //===================================================
        //=========  Copia de Poliza Seguro Vida   ============
        //===================================================
        fileCopyPolizaSegVida = null;
        copyPolizaSegVidaByte = null;
        archivoCopyPolizaSegVida = null;
        nomArchivoCopyPolizaSegVida = null;
        habilitarAdjuntarCopyPolizaSegVida = false;

    }

    public void reiniciarValores() {
        registro = new SspRegistro();
        resolucionPrincipal = new SspResolucion();
        representanteRegistro = new SspRepresentanteRegistro();
        registroEvento = new SspRegistroEvento();
        servicio = new SspServicio();
        partidaSunarpRL = new SbPartidaSunarp();
        asientoSunarpRL = new SbAsientoSunarp();

        localAutorizacion = new SspLocalAutorizacion();
        tipoUsoLocal = new SspTipoUsoLocal();
        localRegistro = new SspLocalRegistro();
        licenciaMunicipal = new SspLicenciaMunicipal();
        //limpiar datos modalidad
        regTipoSeguridadList = null;
        regTipoSeguridadSelected = null;
        regTipoRegistroList = null;
        regTipoRegistroSelected = null;
        regTipoOperacionList = null;
        regTipoOperacionSelected = null;
        regServicioPrestadoList = null;
        regServicioPrestadoSelected = null;
        //limpiar datos del local
        regTipLocalLPSList = null;
        regDistritoLPSList = null;
        regDistritoLPSSelected = null;
        localAutorizacionListado = null;
        localAutorizacionSelectedString = null;
        regTipoViasLPSList = null;
        regTipoViasLPSSelected = null;
        regDireccionLPS = null;
        regNroFisicoLPS = null;
        regReferenciaLPS = null;
        regTipUsoLPSList = null;
        regTipUsoLPSSelected = null;
        nomArchivoLocal = null;
        lat_x = null;
        long_x = null;
        //----modal local/mapa
        regTipoViasLPSList_Form = null;
        regTipoViasLPSSelected_Form = null;
        regDireccionLPS_Form = null;
        regNroFisicoLPS_Form = null;
        regReferenciaLPS_Form = null;
        regTipLocalLPSList_Form = null;
        regTipLocalLPSSelected_Form = null;
        regTipUsoLPSList_Form = null;
        regTipUsoLPSSelected_Form = null;
        regGeoLatitudLPS_Form = null;
        regGeoLongitudPS_Form = null;
        //limpiar representante legal
        personaDetalleListado = new ArrayList<>();;
        personaDetalleSelectedString = null;
        regRLPartidaRegistral = null;
        regRLAsientoRegistral = null;
        regZonaRegListRL = null;
        regZonaRegSelectedRL = null;
        regOficinaRegListRL = null;
        regOficinaRegSelectedRL = null;
        regTipoViasList = null;
        regTipoViasSelected = null;
        regDomicilioRLSelected = null;
        regDistritoRLSList = null;
        regDistritoRLSelected = null;
        //------modal representante
        regTipoDocList = null;
        regTipoDocSelected = null;
        regNroDocBuscar = null;
        regDatoPersonaByNumDocRL = null;
        regRepresentanteFechaNacimiento = null;
        //limpiar licencia municipal
        regNroLicenciaMunicLPS = null;
        regFechaEmisLPS = null;
        regFechaVencLPS = null;
        calcularFechaMinimaCalendario();
        calcularFechaMaximaCalendario();
        regIndeterminadoLPS = false;
        regTipMunicipalidadLPSList = null;
        regTipMunicipalidadLPSSelected = null;
        regMunicLPSList = null;
        regMunicLPSSelected = null;
        regGiroComercialLPS = null;
        nomArchivoLM = null;

        //Modal Declaracion Jurada
        lstPersonasDJ = new ArrayList<>();;
        noAntecedentesPenales = false;
        noEmpresaSeguridad = false;
        noAntecedentesPenales = false;
        //boton GRABAR 
        grabaSolicitud = false;
        //
//        lstLocales = new ArrayList();
//        lstProgramacion = new ArrayList();
//        lstAlumnosVigilantes = new ArrayList();
//        lstInstructores = new ArrayList();
//        lstNotasTemporales = new ArrayList();
//        lstModalidades = new ArrayList();
//        tipoFormacion = null;
//        instructorPersona = null;
//        cambioLocal = false;
//        cambioInstructor = false;
//        cambioHorario = false;
//        setEsNuevo(false);
//        setDisabledNombres(false);
//        setEsEvento(false);
//        limpiarLocal();
    }

    public void iniciarValores_CrearSolicitud() {

        regFormModalMapa_tipoModal = null;
        renderVerMapa2 = false;

        lat_x2 = 0.0;
        long_x2 = 0.0;

        regDistritoLPSList2 = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
        regDistritoLPSSelected2 = null;

        localAutorizacionListado2 = null;
        localAutorizacionSelected2 = null;
        localAutorizacionSelectedString2 = null;

        regTipoViasLPSList2 = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");;
        regTipoViasLPSSelected2 = null;

//             regTipLocalLPSList2= null;
        regTipUsoLPSList2 = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");;
        regTipUsoLPSSelected2 = null;

        regDireccionLPS2 = null;
        regNroFisicoLPS2 = null;
        regReferenciaLPS2 = null;

        renderVerMapa = false;
        lat_x = 0.0;
        long_x = 0.0;

        //Resolución del Local Principal (Autorizacion Aprobada del Departamento)
        resolucionPrincipal = null;

        //El boton aceptar para grabar esta deshabilitado por defecto
        grabaSolicitud = false;

        //El boton nuevo Local se habilita por defecto
        regBuscaDepartamentoPrincipal = false;

        //Habilita para crear una nueva Carta Fianza
        nuevaCartaFianza();

        //Inicializa los valores como DNI por defecto 
        cantNumerosDniCarnetTipoDocRepresentanteLegal = "8";

        //Inicializa la fecha minima para ser mayor de edad
        fechaMayorDeEdadCalendario = getFechaMinMayorEdad();

        calcularFechaMinimaCalendario();
        calcularFechaMaximaCalendario();

        //Por defecto el modal esta para visualizar el Representante Legal 
        buscaNuevoModalRepresentanteLegal = true;
        buscaNuevoModalAccionista = false;
        tituloBuscaModalRepresentanteLegal = JsfUtil.bundleBDIntegrado("gsspSolicitudAutorizacion_Titulo_Modal_Representante_Legal");

        //Para ver los que se eliminan en Contactos y Participantes
        //Al grabar se inactivan sus Ids, para crear los nuevos o actualizar los que existen
        listaUpdateContactosActivos = new ArrayList<>();
        listaUpdateParticipanteActivos = new ArrayList<>();

        //Setea por defecto por Monto del Accionista por monto
        checkTipoAporte = 1;
        regDisabledMontoAportante = false;

        //Setea por defecto por numero de Acciones del Accionista por monto
        checkTipoAcciones = 1;
        regDisabledNumerojeAcciones = false;
        regDisabledPorcentajeAcciones = true;

        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXNumDoc(p_user.getLogin());

        administradoConRUC = false;

        if (regAdminist.getRuc() != null) {
            administradoConRUC = true;
            if (regAdminist.getTipoId().getId().equals(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR").getId())) {
                tipoAdministradoUsuario = "PersonaJuridica";
            } else {
                tipoAdministradoUsuario = "PersonaNatural";
            }
        } else {
            administradoConRUC = false;
        }

        //Selecciona las Cartas Fianzas Vigentes
        regBuscaCartaFianzaSelectedString = null;
        regBuscaCartaFianzaList = null;
        listarCartaFianzas();

        //=============================================
        //============  Administrado PJ ===============
        //=============================================
        //estado = null;
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();

        //=============================================
        //============  Administrado PN ===============
        //=============================================
        administNombres = p_user.getNombres() + " " + p_user.getApePat() + "" + p_user.getApeMat();
        administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
        administNumDoc = regUserAdminist.getNumDoc();

        //=============================================
        //======  Datos de la  Modalidad    ===========
        //=============================================
        regTipoProcesoId = null;

        if (tipoAdministradoUsuario.equals("PersonaJuridica")) {
            //TP_GSSP_MOD_DEP        SERVICIO DE VIGILANCIA PRIVADA
            //TP_GSSP_MOD_SCBC      SERVICIO DE CUSTODIA DE BIENES CONTROLADOS
            //TP_GSSP_MOD_TEC       SERVICIO DE TECNOLOGÍA DE SEGURIDAD
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_DEP'");
            regTipoSeguridadSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_DEP");
            regTipoProcesoId = regTipoSeguridadSelected;
//            listarDepartamentosDistritoTipoLocal();
        } else {
            //TP_GSSP_MOD_SISPE     SERVICIO INDIVIDUAL DE SEGURIDAD PERSONAL
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_SISPE'");
            regTipoSeguridadSelected = null;

        }

        regFormaTipoSeguridadSelected = null;

        regTipoRegistroList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_REGIST");
        regTipoRegistroSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR"); //INICIAL
        regTipoOperacionList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_OPE");
        regTipoOperacionSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"); //INICIAL 

        //SI_ARMA_SSP   SIN ARMA
        //NO_ARMA_SSP   CON ARMA
        //regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP', 'NO_ARMA_SSP'");
        regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP");
        regServicioPrestadoSelected = null;

        regNroComprobante = null;
        regDatoRecibos = null;

        //=============================================================
        //===============    Local Prestacion servicio     ============
        //=============================================================
        habilitarLocalPrestacionServicio = false;
        emptyModel = new DefaultMapModel();

        regDistritoLPSList = null;
        regDistritoLPSSelected = null;

        localAutorizacionListado = null; // ejbSspLocalAutorizacionFacade.listarLocalAutorizacionXidDistritoXIdEmpresa(0L, 0L);
        localAutorizacionSelected = null;
        localAutorizacionSelectedString = null;

        //regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        regTipoViasLPSSelected = null;

        regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
        regDistritoRLSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");

        //regTipLocalLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");        
        regTipLocalLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_LOCAL");

        regTipUsoLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
        //regTipUsoLPSSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);
        if (regTipUsoLPSList != null) {
            regTipUsoLPSSelected = regTipUsoLPSList.get(0);
        } else {
            regTipUsoLPSSelected = null;
        }

        regDireccionLPS = null;
        regNroFisicoLPS = null;
        regReferenciaLPS = null;

        nomArchivoLocal = null;
        nomArchivoLocalAnterior = null;

        //===================================
        //regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        regTipoViasLPSSelected_Form = null;

        //PRINCIPAL Y SUCURSAL 
        //regTipLocalLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");        
        //regTipLocalLPSList_Form = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_LOCAL");
        //regTipLocalLPSSelected_Form = null;
        //Selecciona PRINCIPAL y SUCURSAL para el modal de nuevo local (esta deshabilitado)
        regTipLocalLPSList_Form = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_LOCAL");

        regTipUsoLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
        //regTipUsoLPSSelected_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);
        if (regTipUsoLPSList_Form != null) {
            regTipUsoLPSSelected_Form = regTipUsoLPSList_Form.get(0);
        } else {
            regTipUsoLPSSelected_Form = null;
        }

        regDireccionLPS_Form = null;
        regNroFisicoLPS_Form = null;
        regReferenciaLPS_Form = null;

        regGeoLatitudLPS_Form = null;
        regGeoLongitudPS_Form = null;

        //=============================================
        //=========  Representante Legal   ============
        //=============================================
        habilitarRepresentanteLegal = false;

        //regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_ZR_1','TP_ZR_2','TP_ZR_3','TP_ZR_5','TP_ZR_6','TP_ZR_7','TP_ZR_8','TP_ZR_9','TP_ZR_10','TP_ZR_11','TP_ZR_12','TP_ZR_13','TP_ZR_14'");
        //Zonas Registrales
        regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
        regZonaRegSelectedRL = null;
        regOficinaRegSelectedRL = null;
        regOficinaRegListRL = null;

        //regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        //Tipos de Vias
        regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        regTipoViasSelected = null;

        //Lista de Tipo de Documento para el Representante Legal
        regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");
        regTipoDocSelected = null;

        regNroDocBuscar = null;
        regDatoPersonaByNumDocRL = null;

        regDomicilioRLSelected = null;
        regDistritoRLSelected = null;

        regRLPartidaRegistral = null;
        regRLAsientoRegistral = null;

        existeAsientoSunarpRL = false;

        //Panel de Representante Legal Ver - PRINCIPAL
        representanteLegalVer = null;
        regRLPartidaRegistralVer = null;
        regRLAsientoRegistralVer = null;
        regZonaRegListRLVer = regZonaRegListRL;
        regZonaRegSelectedRLVer = null;
        regOficinaRegListRLVer = null;
        regOficinaRegSelectedRLVer = null;
        regTipoViasListVer = regTipoViasList;
        regTipoViasSelectedVer = null;
        regDomicilioRLSelectedVer = null;
        regDistritoRLSListVer = regDistritoRLSList;
        regDistritoRLSelectedVer = null;

        //=============================================================
        //========  Resolucion Designa Representante Legal   ==========
        //=============================================================
        regTipoDocEPList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOC_CON','TP_DOC_REP','TP_DOC_OFI','TP_DOC_RES','TP_DOC_CVN','TP_DOC_CAR','TP_DOC_NOTVIR','TP_DOC_OTRO'");
        regTipoDocEPSelected = null;

        regNombDocEP = null;
        regNumDocEP = null;
        regFechaEmisEP = null;

        regPersonasRLSelected = null;

        //Representantes Legal del Administrado 
        //regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
        //regPersonasRLSelected = null;
        personaDetalleSelectedString = null;
        personaDetalleListado = null;
        personaDetalleListado = new ArrayList<PersonaDetalle>();

        TipoBaseGt sTipoBase = new TipoBaseGt();

        //Caso contrario es Representante Legal
        sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL

        //Detalle de relacion Persona como Representante Legal o Responsable Legal
        List<SbPersonaGt> listDetallePersona = new ArrayList<>();
        listDetallePersona = ejbSbPersonaFacade.listarPersonaXIdRelacionPersonaUnionSspRegistro(regAdminist.getId(), null, sTipoBase.getId());
        if (listDetallePersona != null) {
            PersonaDetalle detallePersonaTemp;
            for (SbPersonaGt PersonaDetalle : listDetallePersona) {
                detallePersonaTemp = new PersonaDetalle();
                detallePersonaTemp.setId(PersonaDetalle.getId());
                detallePersonaTemp.setNumDoc(PersonaDetalle.getNumDoc());
                detallePersonaTemp.setApePat(PersonaDetalle.getApePat());
                detallePersonaTemp.setApeMat(PersonaDetalle.getApeMat());
                detallePersonaTemp.setNombres(PersonaDetalle.getNombres());
                personaDetalleListado.add(detallePersonaTemp);
            }
            //personaDetalleSelectedString = registro.getRepresentanteId().getId().toString();
            //regTipoDocSelected = registro.getRepresentanteId().getTipoDoc();
            //mostrarDomicilioRepresentanteLegal();
        }

        regDisabledFormResolucionRL = false;

        //=============================================================
        //==============          Medios contactos           ==========
        //=============================================================
        habilitarMediosContactos = false;

        //regPrioridadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_CONTAC_PRIN','TP_CONTAC_SEC'");
        regPrioridadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_CONTAC");
        regPrioridadLPSSelected = null;

        //regTipoMedioContactoLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_MEDCO_COR','TP_MEDCO_FAX','TP_MEDCO_FIJ','TP_MEDCO_MOV'");
        regTipoMedioContactoLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MEDCO");
        regTipoMedioContactoLPSSelected = null;

        lstContactos = new ArrayList<SspContacto>();
        lstContactosList = null;
        selectedContacto = null;
        contRegContacto = 0L;
        regtextoContactoLPS = null;
        regtextoCorreoContactoLPS = null;

        //=============================================================
        //========================    ALMACEN     ==================
        //=============================================================
        //regAlmacenLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'CAJ_FUE_ARM','ARMERIA_ARM'");        
        regAlmacenLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_ALM_ARM");
        regAlmacenLPSSelected = null;

        regCantArmeriaLPS = new BigInteger("0");

        lstAlmacenes = new ArrayList<SspAlmacen>();
        selectedAlmacen = null;
        contRegAlmacen = 0L;

        //=============================================================
        //===============    Licencia Municipal     ============
        //=============================================================
        habilitarLicenciaMunicipal = false;

        regNroLicenciaMunicLPS = null;
        regFechaEmisLPS = null;
        regFechaVencLPS = null;

        //regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MUNI");
        regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MUNI");
        regTipMunicipalidadLPSSelected = null;

        regMunicLPSList = null;
        regMunicLPSSelected = null;

        regGiroComercialLPS = null;
        nomArchivoLM = null;
        nomArchivoLMAnterior = null;

        regIndeterminadoLPS = false;
        regDisabledCalendar = false;

        //=============================================================
        //=======================    ACCIONISTAS      ================
        //=============================================================
        habilitarAccionistas = false;

        regTipDocAccionistaList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");
        regTipDocAccionistaSelected = null;

        regNumeroDocAccionista = null;

        regDatosSocioEncontrado = null;
        regNombreSocioEncontrado = null;

        //regTipoSocioList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_ACCNSTA_ACC','TP_ACCNSTA_PARTC','TP_ACCNSTA_SOCI','TP_ACCNSTA_DIRECT','TP_ACCNSTA_APOD'");      
        regTipoSocioList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ACCNSTA");
        regTipoSocioSelected = null;

        regCargoSocio = null;
        nomArchivoSocio = null;
        lstSocios = new ArrayList<SspParticipante>();
        contRegSocio = 0L;

        //=============================================================
        //=======================  CAPACITADORES ACREDITADOS  ================
        //=============================================================
        lstCapacitadores = new ArrayList<SspCefoespInstructor>();
        contRegCapacitador = 0L;
        carneInstructor = null;
        nomArchivoPE = null;

        regTipoDocCapList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");
        regTipoDocCapSelected = null;
        regNroDocCapBuscar = null;

        nomArchivoPE = null;
        nomArchivoPEAnterior = null;

        //=============================================================
        //=======================    CARTA FIANZA      ================
        //=============================================================
        habilitarCartaFianza = false;

        regNroCartaFianza = null;

        regTipoFinancieraCFList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_FIN_SSP");
        regTipoFinancieraCFSelected = null;

        regEntidadFinancieraCFList = null;
        regEntidadFinancieraCFSelected = null;

        regMontoCartaFianza = null;
        regFechVigenciaIni = null;
        regFechVigenciaFin = null;

        nomArchivoCF = null;
        nomArchivoCFAnterior = null;

        //regTipoMonedaCFList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MNDA");        
        regTipoMonedaCFList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MNDA");
        regTipoMonedaCFSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_MNDA_SOL");

        //======================================================================
        //=====    CHECKED - FORMAS DE TECNOLOGIA DE SEGURIDAD     =============
        //======================================================================
        regFormasServTecnoSegList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_MCO_TEC"); //TECNOLOGÍA DE SEGURIDAD	       

        regForma_TP_MCO_TEC_CRAIDMR = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_CRAIDMR"); //Con centrales receptoras de Alarmas, instalación, desinstalación, monitoreo y respuesta.
        regForma_TP_MCO_TEC_PSGPS = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_PSGPS"); //Posicionamiento Satelital (GPS)
        regForma_TP_MCO_TEC_IDMSVD = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_IDMSVD"); //Instalación, desinstalación y monitoreo de sistema de video a distancia.
        regForma_TP_MCO_TEC_IDMSDEI = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_IDMSDEI"); //Instalación, desinstalación y monitoreo de sistema de detección y de extinción de incendios.       
        regForma_TP_MCO_TEC_IDMSCA = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_IDMSCA"); //Instalación, desinstalación y monitoreo de sistema de control de accesos.
        regForma_TP_MCO_TEC_EPIS = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_EPIS"); //Ejecución de proyectos de ingeniería de seguridad.
        regForma_TP_MCO_TEC_SCM = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_SCM"); //Sistema de control de mercaderías.
        regForma_TP_MCO_TEC_OTRO = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_TEC_OTRO"); //Otros.

        selectedOptions = null;
        lstCheckedForma = new ArrayList<SspForma>();
        contRegCheckForma = 0L;
        selectOptionOtroVal = null;
        regDisabledFormServTecn = false;

        usuarioArchivar = "";

        //=============================================
        //=========  Foto   ============
        //=============================================
        fileFOTO = null;
        fotoByte = null;
        archivoFOTO = null;
        nomArchivoFOTO = null;
        habilitarAdjuntarFoto = false;

        //===================================================
        //=========  Certificado Fisico Mental   ============
        //===================================================
        fileCertFiscoMntal = null;
        certFiscoMntalByte = null;
        archivoCertFiscoMntal = null;
        nomArchivoCertFiscoMntal = null;
        habilitarAdjuntarCertFiscoMntal = false;

        //===================================================
        //=========  Copia de Contrato   ============
        //===================================================
        fileCopyContrato = null;
        copyContratoByte = null;
        archivoCopyContrato = null;
        nomArchivoCopyContrato = null;
        habilitarAdjuntarCopyContrato = false;

        //==================================================================
        //=========  Copia de Poliza Seguro Trabajo de Riesgo   ============
        //==================================================================
        fileCopyPolizaSeguro = null;
        copyPolizaSeguroByte = null;
        archivoCopyPolizaSeguro = null;
        nomArchivoCopyPolizaSeguro = null;
        habilitarAdjuntarCopyPolizaSeguro = false;

        //===================================================
        //=========  Copia de Poliza Seguro Vida   ============
        //===================================================
        fileCopyPolizaSegVida = null;
        copyPolizaSegVidaByte = null;
        archivoCopyPolizaSegVida = null;
        nomArchivoCopyPolizaSegVida = null;
        habilitarAdjuntarCopyPolizaSegVida = false;

    }

    /*public void terminosCondicionUsuario(boolean estadoTerminosCondUser){
        
        if(estadoTerminosCondUser == true){
            termCondic_DatosAdministradoUsuario = true;            
        }else{
            termCondic_DatosAdministradoUsuario = false;
        }
        
        RequestContext.getCurrentInstance().execute("PF('wvDlgTerminosCondicionesUsuario').hide()");
    }*/
    public void createFormAutorizacionServicioSeguridad() {

        iniciarValores_CrearSolicitud();

        estado = EstadoCrud.CREARDEPARTAMENTO;

        registro = new SspRegistro();
        registro.setId(null);
        registro.setCarneId(null);
        registro.setTipoRegId(null);
        registro.setTipoOpeId(null);
        registro.setTipoProId(null);
        registro.setEmpresaId(null);
        registro.setEstadoId(null);
        registro.setComprobanteId(null);
        registro.setRepresentanteId(null);
        registro.setRegistroId(null);
        registro.setNroSolicitiud(null);
        registro.setSedeSucamec(null);
        registro.setNroExpediente(null);
        registro.setFechaIni(null);
        registro.setFechaFin(null);
        registro.setObservacion(null);
        registro.setUsuarioCreacionId(null);
        registro.setFecha(new Date());
        registro.setActivo(JsfUtil.TRUE);
        registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registro.setAudNumIp(JsfUtil.getIpAddress());
        registro.setSspRegistroEventoList(new ArrayList());
        registro.setSspRegistroList(new ArrayList());
        registro.setSspRequisitoList(new ArrayList());

        //======================================================
        //========== REGISTRO PARA PERSONA JURIDICA
        //======================================================
        representanteRegistro = new SspRepresentanteRegistro();
        representanteRegistro.setId(null);
        representanteRegistro.setRegistroId(null);
        representanteRegistro.setRepresentanteId(null);
        representanteRegistro.setFecha(new Date());
        representanteRegistro.setActivo(JsfUtil.TRUE);
        representanteRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        representanteRegistro.setAudNumIp(JsfUtil.getIpAddress());
        //registroSbRelacionPersonaGt = new SbRelacionPersonaGt();

        //======================================================
        //========== REGISTRO DE REPRESENTALTE LEGAL
        //======================================================
        partidaSunarpRL = new SbPartidaSunarp();
        partidaSunarpRL.setId(null);
        partidaSunarpRL.setLibroRegistral(null);
        partidaSunarpRL.setPartidaRegistral(null);
        partidaSunarpRL.setFechaRegistral(null);
        partidaSunarpRL.setZonaRegistral(null);
        partidaSunarpRL.setOficinaRegistral(null);
        partidaSunarpRL.setEstadoPartida(null);
        partidaSunarpRL.setValidacionWs(null);
        partidaSunarpRL.setEstadoWs(null);
        partidaSunarpRL.setFecha(new Date());
        partidaSunarpRL.setActivo(JsfUtil.TRUE);
        partidaSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        partidaSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
        partidaSunarpRL.setSbAsientoSunarpList(new ArrayList());

        asientoSunarpRL = new SbAsientoSunarp();
        asientoSunarpRL.setId(null);
        asientoSunarpRL.setPartidaId(null);
        asientoSunarpRL.setNroAsiento(null);
        asientoSunarpRL.setFecha(new Date());
        asientoSunarpRL.setActivo(JsfUtil.TRUE);
        asientoSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        asientoSunarpRL.setAudNumIp(JsfUtil.getIpAddress());

        asientoPersonaRL = new SbAsientoPersona();
        asientoPersonaRL.setId(null);
        asientoPersonaRL.setAsientoId(null);
        asientoPersonaRL.setPersonaId(null);
        asientoPersonaRL.setFecha(new Date());
        asientoPersonaRL.setActivo(JsfUtil.TRUE);
        asientoPersonaRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        asientoPersonaRL.setAudNumIp(JsfUtil.getIpAddress());

        relacionPersonaGtRL = null;

        /*registroSbAsientoRepresentante = new SbAsientoRepresentante();
        registroSbAsientoRepresentante.setId(null);
        registroSbAsientoRepresentante.setAsientoId(null);
        registroSbAsientoRepresentante.setRepresentanteId(null);
        registroSbAsientoRepresentante.setFecha(new Date());
        registroSbAsientoRepresentante.setActivo(JsfUtil.TRUE);
        registroSbAsientoRepresentante.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registroSbAsientoRepresentante.setAudNumIp(JsfUtil.getIpAddress());*/
        representantePublico = new SspRepresentantePublico();
        representantePublico.setId(null);
        representantePublico.setRepresentanteId(null);
        representantePublico.setTipoDocumentoId(null);
        representantePublico.setNombreDocuemnto(null);
        representantePublico.setNumeroDocuemnto(null);
        representantePublico.setFechaEmision(null);
        representantePublico.setNombreArchivo(null);
        representantePublico.setFecha(new Date());
        representantePublico.setActivo(JsfUtil.TRUE);
        representantePublico.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        representantePublico.setAudNumIp(JsfUtil.getIpAddress());

        //===========================================================
        //===========================================================
        registroEvento = new SspRegistroEvento();
        registroEvento.setId(null);
        registroEvento.setRegistroId(null);
        registroEvento.setTipoEventoId(null);
        registroEvento.setObservacion(null);
        registroEvento.setUserId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
        registroEvento.setFecha(new Date());
        registroEvento.setActivo(JsfUtil.TRUE);
        registroEvento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registroEvento.setAudNumIp(JsfUtil.getIpAddress());

        servicio = new SspServicio();
        servicio.setId(null);
        servicio.setRegistroId(null);
        servicio.setServicioPrestadoId(null);
        servicio.setFecha(new Date());
        servicio.setActivo(JsfUtil.TRUE);
        servicio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        servicio.setAudNumIp(JsfUtil.getIpAddress());

        forma = new SspForma();
        forma.setId(null);
        forma.setServicioId(null);
        forma.setTipoFormaId(null);
        forma.setDescripcion(null);
        forma.setFecha(new Date());
        forma.setActivo(JsfUtil.TRUE);
        forma.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        forma.setAudNumIp(JsfUtil.getIpAddress());

        localAutorizacion = new SspLocalAutorizacion();
        localAutorizacion.setId(null);
        localAutorizacion.setTipoUbicacionId(null);
        localAutorizacion.setDireccion(null);
        localAutorizacion.setDistritoId(null);
        localAutorizacion.setReferencia(null);
        localAutorizacion.setGeoLatitud(null);
        localAutorizacion.setGeoLongitud(null);
        localAutorizacion.setFecha(new Date());
        localAutorizacion.setActivo(JsfUtil.TRUE);
        localAutorizacion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        localAutorizacion.setAudNumIp(JsfUtil.getIpAddress());

        tipoUsoLocal = new SspTipoUsoLocal();
        tipoUsoLocal.setId(null);
        tipoUsoLocal.setLocalId(null);
        tipoUsoLocal.setRegistroId(null);
        tipoUsoLocal.setTipoLocalId(null);
        tipoUsoLocal.setTipoUsoId(null);
        tipoUsoLocal.setFecha(new Date());
        tipoUsoLocal.setActivo(JsfUtil.TRUE);
        tipoUsoLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        tipoUsoLocal.setAudNumIp(JsfUtil.getIpAddress());

        localRegistro = new SspLocalRegistro();
        localRegistro.setId(null);
        localRegistro.setRegistroId(null);
        localRegistro.setLocalId(null);
        localRegistro.setFecha(new Date());
        localRegistro.setActivo(JsfUtil.TRUE);
        localRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        localRegistro.setAudNumIp(JsfUtil.getIpAddress());

        licenciaMunicipal = new SspLicenciaMunicipal();
        licenciaMunicipal.setId(null);
        licenciaMunicipal.setLocalId(null);
        licenciaMunicipal.setNumeroLicencia(null);
        licenciaMunicipal.setTipoMunicipalidad(null);
        licenciaMunicipal.setMunicipalidadId(null);
        licenciaMunicipal.setFechaIni(null);
        licenciaMunicipal.setFechaFin(null);
        licenciaMunicipal.setIndeterminado(null);
        licenciaMunicipal.setGiroComercial(null);
        licenciaMunicipal.setNombreArchivo(null);
        licenciaMunicipal.setValidacionWs(null);
        licenciaMunicipal.setEstadoWs(null);
        licenciaMunicipal.setFecha(new Date());
        licenciaMunicipal.setActivo(JsfUtil.TRUE);
        licenciaMunicipal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        licenciaMunicipal.setAudNumIp(JsfUtil.getIpAddress());

        cartaFianza = new SspCartaFianza();
        cartaFianza.setId(null);
        cartaFianza.setNumero(null);
        cartaFianza.setTipoFinancieraId(null);
        cartaFianza.setFinancieraId(null);
        cartaFianza.setMoneda(null);
        cartaFianza.setMonto(null);
        cartaFianza.setVigenciaInicio(null);
        cartaFianza.setVigenciaFin(null);
        cartaFianza.setNombreArchivo(null);
        cartaFianza.setFecha(new Date());
        cartaFianza.setActivo(JsfUtil.TRUE);
        cartaFianza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        cartaFianza.setAudNumIp(JsfUtil.getIpAddress());

        regDisabledFormServTecn = false;
        //return "/aplicacion/gssp/gsspSolicitudAutorizacion/CreateAutoServSeg";
    }

    public void listarLocalesAutorizXDistrito() {

        localAutorizacionListado = null;
        localAutorizacionListado = new ArrayList<LocalAutorizacionDetalle>();

        regTipoViasLPSSelected = null;
        regDireccionLPS = null;
        regNroFisicoLPS = null;
        regTipUsoLPSSelected = null;
        regReferenciaLPS = null;
        localAutorizacionSelectedString = null;
        regBuscaDepartamentoPrincipal = false;

        renderVerMapa = false;
        lat_x = 0.0;
        long_x = 0.0;

        //Limpia los datos del Panel de Representante Legal Ver - PRINCIPAL
        representanteLegalVer = null;
        regRLPartidaRegistralVer = null;
        regRLAsientoRegistralVer = null;
        //regZonaRegListRLVer = regZonaRegListRL;
        regZonaRegSelectedRLVer = null;
        regOficinaRegListRLVer = null;
        regOficinaRegSelectedRLVer = null;
        //regTipoViasListVer = regTipoViasList;
        regTipoViasSelectedVer = null;
        regDomicilioRLSelectedVer = null;
        //regDistritoRLSListVer = regDistritoRLSList;
        regDistritoRLSelectedVer = null;

        if (regDistritoLPSSelected != null) {

            List<SspLocalAutorizacion> listDetalleLocal = new ArrayList<>();
            //listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocal(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId());
            listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaCefoesp(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipoSeguridadSelected.getId(), registro.getId());
            if (listDetalleLocal != null) {
                LocalAutorizacionDetalle detalleLocalTemp;
                for (SspLocalAutorizacion localDetalle : listDetalleLocal) {
                    detalleLocalTemp = new LocalAutorizacionDetalle();
                    detalleLocalTemp.setId(localDetalle.getId());
                    detalleLocalTemp.setTipoUbicacionId(localDetalle.getTipoUbicacionId());
                    detalleLocalTemp.setDireccion((localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "");
                    detalleLocalTemp.setNroFisico((localDetalle.getNroFisico() != null) ? localDetalle.getNroFisico() : "");
                    detalleLocalTemp.setDistritoId(localDetalle.getDistritoId());
                    detalleLocalTemp.setReferencia((localDetalle.getReferencia() != null) ? localDetalle.getReferencia() : "");
                    if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                        detalleLocalTemp.setTipoLocalId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoLocalId());
                    } else {
                        detalleLocalTemp.setTipoLocalId(null);
                    }
                    if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                        detalleLocalTemp.setTipoUsoId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoUsoId());
                    } else {
                        detalleLocalTemp.setTipoUsoId(null);
                    }
                    detalleLocalTemp.setGeoLatitud((localDetalle.getGeoLatitud() != null) ? localDetalle.getGeoLatitud() : "");
                    detalleLocalTemp.setGeoLongitud((localDetalle.getGeoLongitud() != null) ? localDetalle.getGeoLongitud() : "");
                    localAutorizacionListado.add(detalleLocalTemp);
                }
                RequestContext.getCurrentInstance().execute("updateController = true;");
            }

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el distrito");
            localAutorizacionListado = null;
        }
    }

    public void listarLocalesAutorizXDistrito2() {

        localAutorizacionListado2 = null;
        localAutorizacionListado2 = new ArrayList<LocalAutorizacionDetalle>();

        regTipoViasLPSSelected2 = null;
        regDireccionLPS2 = null;
        regNroFisicoLPS2 = null;
        regTipUsoLPSSelected2 = null;
        regReferenciaLPS2 = null;
        localAutorizacionSelectedString2 = null;
        regBuscaDepartamentoPrincipal = false;

        renderVerMapa2 = false;
        lat_x2 = 0.0;
        long_x2 = 0.0;

        //regTipoViasListVer = regTipoViasList;
        regTipoViasSelectedVer = null;
        regDomicilioRLSelectedVer = null;
        //regDistritoRLSListVer = regDistritoRLSList;
        regDistritoRLSelectedVer = null;

        if (regDistritoLPSSelected2 != null) {

            List<SspLocalAutorizacion> listDetalleLocal = new ArrayList<>();
            //listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocal(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId());
            listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaCefoesp(regAdminist.getId(), regDistritoLPSSelected2.getId(), regTipoSeguridadSelected.getId(), registro.getId());
            if (listDetalleLocal != null) {
                LocalAutorizacionDetalle detalleLocalTemp;
                for (SspLocalAutorizacion localDetalle : listDetalleLocal) {
                    detalleLocalTemp = new LocalAutorizacionDetalle();
                    detalleLocalTemp.setId(localDetalle.getId());
                    detalleLocalTemp.setTipoUbicacionId(localDetalle.getTipoUbicacionId());
                    detalleLocalTemp.setDireccion((localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "");
                    detalleLocalTemp.setNroFisico((localDetalle.getNroFisico() != null) ? localDetalle.getNroFisico() : "");
                    detalleLocalTemp.setDistritoId(localDetalle.getDistritoId());
                    detalleLocalTemp.setReferencia((localDetalle.getReferencia() != null) ? localDetalle.getReferencia() : "");
                    if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                        detalleLocalTemp.setTipoLocalId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoLocalId());
                    } else {
                        detalleLocalTemp.setTipoLocalId(null);
                    }
                    if (localDetalle.getSspTipoUsoLocalList().size() > 0) {
                        detalleLocalTemp.setTipoUsoId(localDetalle.getSspTipoUsoLocalList().get(0).getTipoUsoId());
                    } else {
                        detalleLocalTemp.setTipoUsoId(null);
                    }
                    detalleLocalTemp.setGeoLatitud((localDetalle.getGeoLatitud() != null) ? localDetalle.getGeoLatitud() : "");
                    detalleLocalTemp.setGeoLongitud((localDetalle.getGeoLongitud() != null) ? localDetalle.getGeoLongitud() : "");
                    localAutorizacionListado2.add(detalleLocalTemp);
                }
                RequestContext.getCurrentInstance().execute("updateController2 = true;");
            }

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el distrito del polígono");
            localAutorizacionListado2 = null;
        }
    }
    
    
    public String mostrarLabelLocal(LocalAutorizacionDetalle item){        
        String cboLocal = "";
        cboLocal = (item.getDireccion() != null) ? item.getDireccion() : "";
        cboLocal += (item.getNroFisico() != null) ? " "+item.getNroFisico() : "";
        cboLocal = cboLocal.toUpperCase();
        cboLocal = cboLocal.replaceAll("NULL", "");
        cboLocal = cboLocal.trim();        
        return cboLocal;        
    }
    

    public void mostrarDatosLocalSeleccionado() {
        if (localAutorizacionListado != null) {
            if (localAutorizacionSelectedString != null) {
                renderVerMapa = true;
                for (LocalAutorizacionDetalle localDetalle : localAutorizacionListado) {
                    if (localDetalle.getId().equals(Long.parseLong(localAutorizacionSelectedString))) {
                        //Setea los datos encontrados
                        //Cuando esta editando si se selecciona el Tipo de Local PRINCIPAL / SUCURSAL
                        regTipoViasLPSSelected = (localDetalle.getTipoUbicacionId() != null) ? localDetalle.getTipoUbicacionId() : null;
                        regDireccionLPS = (localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "";
                        regDireccionLPS += (localDetalle.getNroFisico() != null) ? " "+localDetalle.getNroFisico() : "";
                        regDireccionLPS = regDireccionLPS.toUpperCase();
                        regDireccionLPS = regDireccionLPS.replaceAll("NULL", "");
                        regDireccionLPS = regDireccionLPS.trim();
                        
                        regNroFisicoLPS = (localDetalle.getNroFisico() != null) ? localDetalle.getNroFisico() : "";

                        regTipUsoLPSSelected = (localDetalle.getTipoUsoId() != null) ? localDetalle.getTipoUsoId() : null;
                        regReferenciaLPS = (localDetalle.getReferencia() != null) ? localDetalle.getReferencia() : "";

                        lat_x = (!StringUtils.isEmpty(localDetalle.getGeoLatitud())) ? Double.parseDouble(localDetalle.getGeoLatitud()) : 0.0;
                        long_x = (!StringUtils.isEmpty(localDetalle.getGeoLongitud())) ? Double.parseDouble(localDetalle.getGeoLongitud()) : 0.0;
                        break;
                    }
                }
            } else {
                //limpiar
                regTipoViasLPSSelected = null;
                regDireccionLPS = null;
                regNroFisicoLPS = null;
                regReferenciaLPS = null;
                regTipUsoLPSSelected = null;
                nomArchivoLocal = null;
                nomArchivoLocalAnterior = null;
                renderVerMapa = false;
                lat_x = 0.0;
                long_x = 0.0;
                RequestContext.getCurrentInstance().execute("updateController = false;");
            }
        }
    }

    public void mostrarDatosLocalSeleccionado2() {
        if (localAutorizacionListado2 != null) {
            if (localAutorizacionSelectedString2 != null) {
                renderVerMapa2 = true;
                for (LocalAutorizacionDetalle localDetalle : localAutorizacionListado2) {
                    if (localDetalle.getId().equals(Long.parseLong(localAutorizacionSelectedString2))) {
                        //Setea los datos encontrados
                        //Cuando esta editando si se selecciona el Tipo de Local PRINCIPAL / SUCURSAL
                        regTipoViasLPSSelected2 = (localDetalle.getTipoUbicacionId() != null) ? localDetalle.getTipoUbicacionId() : null;
                        regDireccionLPS2 = (localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "";
                        regNroFisicoLPS2 = (localDetalle.getNroFisico() != null) ? localDetalle.getNroFisico() : "";

                        regTipUsoLPSSelected2 = (localDetalle.getTipoUsoId() != null) ? localDetalle.getTipoUsoId() : null;
                        regReferenciaLPS2 = (localDetalle.getReferencia() != null) ? localDetalle.getReferencia() : "";

                        lat_x2 = (!StringUtils.isEmpty(localDetalle.getGeoLatitud())) ? Double.parseDouble(localDetalle.getGeoLatitud()) : 0.0;
                        long_x2 = (!StringUtils.isEmpty(localDetalle.getGeoLongitud())) ? Double.parseDouble(localDetalle.getGeoLongitud()) : 0.0;
                        break;
                    }
                }
            } else {
                //limpiar
                regTipoViasLPSSelected2 = null;
                regDireccionLPS2 = null;
                regNroFisicoLPS2 = null;
                regReferenciaLPS2 = null;
                regTipUsoLPSSelected2 = null;
//                nomArchivoLocal2                 = null;nomArchivoLocalAnterior2 = null;
                renderVerMapa2 = false;
                lat_x2 = 0.0;
                long_x2 = 0.0;
                RequestContext.getCurrentInstance().execute("updateController = false;");
            }
        }
    }

    public void listarOficinasRegistrales_RL() {

        if (regZonaRegSelectedRL != null) {
            regOficinaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg(regZonaRegSelectedRL.getCodProg());
            regOficinaRegSelectedRL = null;

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la zona registral");
            regOficinaRegListRL = null;
            regOficinaRegSelectedRL = null;
        }

    }

    public void listarMunicipalidades() {
        if (regTipMunicipalidadLPSSelected != null) {
            regMunicLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg(regTipMunicipalidadLPSSelected.getCodProg());
            regMunicLPSSelected = null;

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de municipalidad");
            regMunicLPSList = null;
            regMunicLPSSelected = null;
        }
    }

    public void listarFormasTipoSeguridad() {

        regDisabledFormServTecn = false;
        lstCheckedForma = new ArrayList<SspForma>();

        if (regTipoSeguridadSelected != null) {

            if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_DEP")) {//SERVICIO DE VIGILANCIA PRIVADA
                habilitarAdjuntarFoto = false;
                habilitarAdjuntarCertFiscoMntal = false;
                habilitarAdjuntarCopyContrato = false;
                habilitarAdjuntarCopyPolizaSeguro = false;
                habilitarAdjuntarCopyPolizaSegVida = false;

                habilitarRepresentanteLegal = true;
                regDisabledFormResolucionRL = true;
                habilitarMediosContactos = true;
                habilitarLocalPrestacionServicio = true;
                habilitarLicenciaMunicipal = true;
                habilitarAccionistas = true;

                regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP', 'NO_ARMA_SSP'");
                regServicioPrestadoSelected = null;

            } else if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_SISPE")) {//SERVICIO INDIVIDUAL DE SEGURIDAD PERSONAL - SISPE
                habilitarAdjuntarFoto = true;
                habilitarAdjuntarCertFiscoMntal = true;
                habilitarAdjuntarCopyContrato = true;
                habilitarAdjuntarCopyPolizaSeguro = true;
                habilitarAdjuntarCopyPolizaSegVida = true;

                habilitarRepresentanteLegal = false;
                regDisabledFormResolucionRL = false;
                habilitarMediosContactos = true;
                habilitarLocalPrestacionServicio = false;
                habilitarLicenciaMunicipal = false;
                habilitarAccionistas = false;

                regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP', 'NO_ARMA_SSP'");
                regServicioPrestadoSelected = null;

            } else if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_SCBC")) {//SERVICIO DE CUSTODIA DE BIENES CONTROLADOS
                habilitarAdjuntarFoto = false;
                habilitarAdjuntarCertFiscoMntal = false;
                habilitarAdjuntarCopyContrato = false;
                habilitarAdjuntarCopyPolizaSeguro = false;
                habilitarAdjuntarCopyPolizaSegVida = false;

                habilitarRepresentanteLegal = true;
                regDisabledFormResolucionRL = true;
                habilitarMediosContactos = true;
                habilitarLocalPrestacionServicio = true;
                habilitarLicenciaMunicipal = true;
                habilitarAccionistas = true;

                regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP'");
                regServicioPrestadoSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("SI_ARMA_SSP");

            } else if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_TEC")) {//SERVICIO DE TECNOLOGÍA DE SEGURIDAD
                habilitarAdjuntarFoto = false;
                habilitarAdjuntarCertFiscoMntal = false;
                habilitarAdjuntarCopyContrato = false;
                habilitarAdjuntarCopyPolizaSeguro = false;
                habilitarAdjuntarCopyPolizaSegVida = false;

                habilitarRepresentanteLegal = true;
                regDisabledFormResolucionRL = true;
                habilitarMediosContactos = true;
                habilitarLocalPrestacionServicio = true;
                habilitarLicenciaMunicipal = true;
                habilitarAccionistas = true;

                regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP'");
                regServicioPrestadoSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("SI_ARMA_SSP");

            } else {
                habilitarAdjuntarFoto = false;
                habilitarAdjuntarCertFiscoMntal = false;
                habilitarAdjuntarCopyContrato = false;
                habilitarAdjuntarCopyPolizaSeguro = false;
                habilitarAdjuntarCopyPolizaSegVida = false;

                habilitarRepresentanteLegal = false;
                regDisabledFormResolucionRL = false;
                habilitarMediosContactos = false;
                habilitarLocalPrestacionServicio = false;
                habilitarLicenciaMunicipal = false;
                habilitarAccionistas = false;
            }

            if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_PCP")) {//SERVICIO DE PROTECCIÓN POR CUENTA PROPIA

                regFormaTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_FRM_SPIIPEPR','TP_GSSP_FRM_SPIIPEPU','TP_GSSP_FRM_TDVPEPR','TP_GSSP_FRM_TDVPEPU'");
                regFormaTipoSeguridadSelected = null;

                regTipoProcesoId = null;
                selectedOptions = null;

            } else {

                regFormaTipoSeguridadList = null;
                regFormaTipoSeguridadSelected = null;

                if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_TEC")) {
                    regDisabledFormServTecn = true;
                } else {
                    regDisabledFormServTecn = false;
                    selectedOptions = null;
                }

                regTipoProcesoId = regTipoSeguridadSelected;
                regDisabledFormResolucionRL = false;
            }

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de seguridad");

            regFormaTipoSeguridadList = null;
            regFormaTipoSeguridadSelected = null;

            regTipoProcesoId = null;
            selectedOptions = null;
            regDisabledFormResolucionRL = false;
        }

    }

    public void actualizaFormasTipoSeguridad() {

        regDisabledFormResolucionRL = false;

        if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_PCP")) {
            regTipoProcesoId = regFormaTipoSeguridadSelected;

            if (regFormaTipoSeguridadSelected.getCodProg().equals("TP_GSSP_FRM_SPIIPEPU") || regFormaTipoSeguridadSelected.getCodProg().equals("TP_GSSP_FRM_TDVPEPU")) {
                regDisabledFormResolucionRL = true;
            } else {
                regDisabledFormResolucionRL = false;
            }

        } else {
            regTipoProcesoId = regTipoSeguridadSelected;
        }

    }

    public void mostrarDomicilioRepresentanteLegal() {
        if (personaDetalleListado != null) {
            if (personaDetalleSelectedString != null) {
                for (PersonaDetalle personaDetalle : personaDetalleListado) {
                    if (personaDetalle.getId().equals(Long.parseLong(personaDetalleSelectedString))) {

                        SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
                        Long xEmpresaId = regAdminist.getId();
                        xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(Long.parseLong(personaDetalleSelectedString), xEmpresaId);

                        if (xSbAsientoPersonaRL != null) {
                            existeAsientoSunarpRL = true;
                            regRLPartidaRegistral = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getPartidaRegistral();
                            regRLAsientoRegistral = xSbAsientoPersonaRL.getAsientoId().getNroAsiento();
                            if (xSbAsientoPersonaRL.getAsientoId().getPartidaId() != null) {
                                regZonaRegSelectedRL = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getZonaRegistral().getId());
                                listarOficinasRegistrales_RL();
                                regOficinaRegSelectedRL = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getOficinaRegistral().getId());
                            } else {
                                regZonaRegSelectedRL = null;
                                regOficinaRegSelectedRL = null;
                                regOficinaRegListRL = null;
                            }
                        } else {
                            existeAsientoSunarpRL = false;
                            regRLPartidaRegistral = null;
                            regRLAsientoRegistral = null;
                            regZonaRegSelectedRL = null;
                            regOficinaRegSelectedRL = null;
                            regOficinaRegListRL = null;
                        }

                        List<SbDireccionGt> listDireccionPersona = ejbSbDireccionFacade.listarDireccionesXPersona(Long.parseLong(personaDetalleSelectedString));
                        if (listDireccionPersona.size() > 0) {
                            regTipoViasSelected = listDireccionPersona.get(0).getViaId();
                            regDomicilioRLSelected = listDireccionPersona.get(0).getDireccion();
                            regDistritoRLSelected = listDireccionPersona.get(0).getDistritoId();
                        } else {
                            regTipoViasSelected = null;
                            regDomicilioRLSelected = null;
                            regDistritoRLSelected = null;
                        }
                        //Setea los datos encontrados
                        //Cuando esta editando si se selecciona el Tipo de Local PRINCIPAL / SUCURSAL
                        /*if (registro.getId() != null) {
                            regTipLocalLPSSelected = (localDetalle.getTipoLocalId() != null) ? localDetalle.getTipoLocalId() : null;
                            //Actualiza la lista del Modal de Nuevo Local con el mismo tipo local PRINCIPAL o SUCURSAL
                            regTipLocalLPSSelected_Form = regTipLocalLPSSelected;
                        }    */
                        break;
                    }
                }

            } else {
                //Si es nulo la selección del Representante Legal
                existeAsientoSunarpRL = false;
                regRLPartidaRegistral = null;
                regRLAsientoRegistral = null;
                regZonaRegSelectedRL = null;
                regOficinaRegSelectedRL = null;
                regTipoViasSelected = null;
                regDomicilioRLSelected = null;
                regOficinaRegListRL = null;
            }
        } else {
            //Si es nulo la selección del Representante Legal
            existeAsientoSunarpRL = false;
            regRLPartidaRegistral = null;
            regRLAsientoRegistral = null;
            regZonaRegSelectedRL = null;
            regOficinaRegSelectedRL = null;
            regTipoViasSelected = null;
            regDomicilioRLSelected = null;
            regOficinaRegListRL = null;
        }
    }

    public boolean cambiarDireccionRepresentanteLegal() {
        boolean validarCambioDomicilio = true;

        if (regPersonasRLSelected == null) {
            validarCambioDomicilio = false;
            JsfUtil.invalidar(obtenerForm() + ":cboRepresLegal");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el representante legal");
        }

        if (regTipoViasSelected == null) {
            validarCambioDomicilio = false;
            JsfUtil.invalidar(obtenerForm() + ":cboRLDomicilioLegal_RL");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de via");
        }

        if (regDomicilioRLSelected == null) {
            validarCambioDomicilio = false;
            JsfUtil.invalidar(obtenerForm() + ":txtRLDomicLegal_RL");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el domicilio legal");
        }

        if (regDomicilioRLSelected != null) {
            if (regDomicilioRLSelected.equals("")) {
                validarCambioDomicilio = false;
                JsfUtil.invalidar(obtenerForm() + ":txtRLDomicLegal_RL");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el domicilio legal");
            }
        }

        if (validarCambioDomicilio) {
            regSbDireccionRL = new SbDireccionGt();
            if (ejbSbDireccionFacade.listarDireccionesXPersona(regPersonasRLSelected.getId()).size() > 0) {
                regSbDireccionRL = ejbSbDireccionFacade.listarDireccionesXPersona(regPersonasRLSelected.getId()).get(0);
                regSbDireccionRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DIRECB_FIS"));
                regSbDireccionRL.setPersonaId(regPersonasRLSelected);
                regSbDireccionRL.setZonaId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_ZON_URB"));
                regSbDireccionRL.setViaId(regTipoViasSelected);
                regSbDireccionRL.setDireccion(regDomicilioRLSelected);
                regSbDireccionRL.setActivo(JsfUtil.TRUE);
                regSbDireccionRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                regSbDireccionRL.setAudNumIp(JsfUtil.getIpAddress());
                regSbDireccionRL.setPaisId(ejbSbPaisFacadeGt.obtenerPaisByNombre("PERU"));
                regSbDireccionRL = (SbDireccionGt) JsfUtil.entidadMayusculas(regSbDireccionRL, "");
                ejbSbDireccionFacade.edit(regSbDireccionRL);
                JsfUtil.mensaje("Se actualizó la dirección del represenatnte legal seleccionado");
            } else {
                regSbDireccionRL.setId(null);
                regSbDireccionRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DIRECB_FIS"));
                regSbDireccionRL.setPersonaId(regPersonasRLSelected);
                regSbDireccionRL.setZonaId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_ZON_URB"));
                regSbDireccionRL.setViaId(regTipoViasSelected);
                regSbDireccionRL.setDireccion(regDomicilioRLSelected);
                regSbDireccionRL.setNumero(null);
                regSbDireccionRL.setReferencia(null);
                regSbDireccionRL.setGeoLat(null);
                regSbDireccionRL.setGeoLong(null);
                regSbDireccionRL.setActivo(JsfUtil.TRUE);
                regSbDireccionRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                regSbDireccionRL.setAudNumIp(JsfUtil.getIpAddress());
                regSbDireccionRL.setPaisId(ejbSbPaisFacadeGt.obtenerPaisByNombre("PERU"));
                regSbDireccionRL = (SbDireccionGt) JsfUtil.entidadMayusculas(regSbDireccionRL, "");
                ejbSbDireccionFacade.create(regSbDireccionRL);
                JsfUtil.mensaje("Se registró la dirección del representante legal seleccionado");
            }
        }
        return validarCambioDomicilio;
    }

    public void verificarCheckIndeterminado() {
        String checkedIndeterminado = (regIndeterminadoLPS) ? "Checked" : "Unchecked";

        if (checkedIndeterminado == "Checked") {
            regFechaVencLPS = null;
            regDisabledCalendar = true;
        } else {
            regDisabledCalendar = false;
        }
    }

    public void listarEntidadFinancieras() {

        if (regTipoFinancieraCFSelected != null) {

            regEntidadFinancieraCFList = ejbTipoSeguridadFacade.lstTipoSeguridad(regTipoFinancieraCFSelected.getCodProg());
            regEntidadFinancieraCFSelected = null;

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de seguridad.");
        }

    }

    public String obtenerForm() {
        switch (estado) {
            case CREARVIGILANCIAPRIVADA:
                return "FormRegSolicAuto";
            case EDITARVIGILANCIAPRIVADA:
                return "FormRegSolicAuto";
            case VERVIGILANCIAPRIVADA:
                return "FormVerSolicAuto";
        }
        return null;
    }

    public boolean validaRegistroSolicitudAutorizacion() {
        boolean validacion = true;

        //============================================
        //Validar Panel - Datos de la Modalidad
        //============================================
        if (regTipoSeguridadSelected == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":bcoTipServicio");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de servicio");
            return false;
        }

        if (regTipoSeguridadSelected != null) {
            if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_PCP")) {
                if (regFormaTipoSeguridadSelected == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":bcoFormaTipoServ");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de servicio");
                    return false;
                }
            }
        }

        /*
        if(regServicioPrestadoSelected == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":cboServPrestado");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el servicio prestado");
            return false;
        }        
        
         */
 /*if(regDatoRecibos == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":txtNroComprobante");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el número del comprobante");
            return false;
        }*/
 /*if(regListDatoRecibos == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":txtNroComprobante");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el numero del voucher. Los pagos son reconocidos por el sistema 24 horas despues de haberlos hecho.");
            return false;
        }*/
        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP")) {

            //===============         Validar Panel - Representante Legal   ===============
            /*if(regPersonasRLSelected == null){
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboRepresLegal");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el representante legal");            
            }*/
            //Validamos cuando es Representante Legal o Responsable Legal
            String tipoPersonaLegal;
            //Si es Sucursal es Responsable Legal
            tipoPersonaLegal = "representante";

            if (personaDetalleSelectedString == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboRepresLegal");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el " + tipoPersonaLegal + " legal");
            } else if (personaDetalleSelectedString.equals("")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboRepresLegal");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el " + tipoPersonaLegal + " legal");
            }

            if (regRLPartidaRegistral == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtPartReg_RL");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar la partida registral del " + tipoPersonaLegal + " legal");
            } else if (regRLPartidaRegistral.equals("")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtPartReg_RL");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar la partida registral del " + tipoPersonaLegal + " legal");
            }

            if (regRLAsientoRegistral == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtAsientoReg_RL");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el asiento registral del " + tipoPersonaLegal + " legal");
            } else if (regRLAsientoRegistral.equals("")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtAsientoReg_RL");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el asiento registral del " + tipoPersonaLegal + " legal");
            }

            if (regZonaRegSelectedRL == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboPJZonaRegistral_RL");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar la zona registral del " + tipoPersonaLegal + " legal");
            }

            if (regOficinaRegSelectedRL == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboPJOficinaRegistral_RL");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar la oficina registral del " + tipoPersonaLegal + " legal");
            }

            if (regTipoViasSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboRLDomicilioLegal_RL");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de ubicación del " + tipoPersonaLegal + " legal");
            }

            if (regDomicilioRLSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtRLDomicLegal_RL");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el domicilio del " + tipoPersonaLegal + " legal");
            } else if (regDomicilioRLSelected.equals("")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtRLDomicLegal_RL");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el domicilio del " + tipoPersonaLegal + " legal");
            }

            if (regDistritoRLSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboRLDistrito_RL");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el Ubigeo (Dpto/Prov/Distrito)");
            }

        }

        //=============      Medios de Contactos    =====================       
        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SISPE")) {

            if (lstContactos.size() == 0) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
                JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
                JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un registro en el medios de contacto");
            }
        }

        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP")) {

            //=================     Local de Prestación de Servicio - LOCAL     ===========
            if (regDistritoLPSSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":bcoLPE_AmbitoOperaDIstrito");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el Ambito de operaciones");
            }

            if (localAutorizacionSelectedString == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":bcoLPE_LocalAutorizacion");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el local");
            } else if (localAutorizacionSelectedString.equals("")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":bcoLPE_LocalAutorizacion");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el local");
            }

            if (regTipoViasLPSSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":bcoLPE_TipUbica");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de ubicacion");
            }

            if (regDireccionLPS == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtLPE_DomiciLegal");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el domicilio legal");
            } else if (regDireccionLPS.equals("")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtLPE_DomiciLegal");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el domicilio legal");
            }

            if (regTipUsoLPSSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboLPE_TipUso");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de uso");
            }

            if (nomArchivoLocal == null && nomArchivoLocalAnterior == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txf_ArchivoLocal");
                JsfUtil.mensajeAdvertencia("Debe de adjuntar el Plano de Distribución");
            }

        }

        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP")) {

            //=============     Local Licencia Municipal   ============        
            if (regNroLicenciaMunicLPS == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txt_LM_NroLicMuni");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de Licencia Municipal");
            } else if (regNroLicenciaMunicLPS.equals("")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txt_LM_NroLicMuni");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de Licencia Municipal");
            }

            if (regIndeterminadoLPS == false && regFechaEmisLPS == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txt_LM_FechaEmis");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar la fecha de emisión de la Licencia Municipal");
            }

            if (regIndeterminadoLPS == false && regFechaVencLPS == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txt_LM_FechaVenc");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar la fecha de vencimiento de la Licencia Municipal");
            }

            //Fecha de Emisión no debe ser mayor a la de hoy dia
            if ((regFechaEmisLPS != null)) {
                if (JsfUtil.getFechaSinHora(regFechaEmisLPS).compareTo(JsfUtil.getFechaSinHora(getFechaHoy())) > 0) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txt_LM_FechaEmis");
                    JsfUtil.mensajeAdvertencia("La fecha de Emisión no puede ser mayor que la fecha Actual");
                }
            }

            //Fecha de Vencimiento no debe ser menor a la de hoy dia
            if ((regFechaVencLPS != null)) {
                if (JsfUtil.getFechaSinHora(getFechaHoy()).compareTo(JsfUtil.getFechaSinHora(regFechaVencLPS)) > 0) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txt_LM_FechaVenc");
                    JsfUtil.mensajeAdvertencia("La fecha de Vencimiento no puede ser menor que la fecha Actual");
                }
            }

            //Valida que la Fecha Emisión no sea mayor a la fecha de Vencimiento
            if ((regFechaEmisLPS != null && regFechaVencLPS != null)) {
                if (JsfUtil.getFechaSinHora(regFechaEmisLPS).compareTo(JsfUtil.getFechaSinHora(regFechaVencLPS)) > 0) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txt_LM_FechaEmis");
                    JsfUtil.mensajeAdvertencia("La fecha de Emisión no puede ser mayor que la fecha de Vencimiento");
                }
            }

            if (regTipMunicipalidadLPSSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cbo_LM_TipMunic");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de municipalidad");
            }

            if (regMunicLPSSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cbo_LM_Municip");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar la municipalidad");
            }

            if (regGiroComercialLPS == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txt_LM_GiroComercial");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el giro comercial");
            } else if (regGiroComercialLPS.equals("")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txt_LM_GiroComercial");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el giro comercial");
            }

            if (nomArchivoLM == null && nomArchivoLMAnterior == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txf_ArchivoLM");
                JsfUtil.mensajeAdvertencia("Debe de adjuntar el archivo de Licencia Municipal");
            }
        }

        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP")) {

            //===========   Accionista, participantes, Socios, Miembros de Directorio o Apoderados  ==============
            if (lstSocios.size() == 0) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipDoc");
                JsfUtil.invalidar(obtenerForm() + ":txtAcc_NroDoc");
                JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipAccionista");
                JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un registro como miembro de socios");
            }

        }

        return validacion;
    }

    public String estiloPorTiempo(String observacion, String codProg) {
        //System.out.println("Mobservacionap->"+observacion+"---"+"codProg->"+codProg);                

        if (observacion != null && codProg.equals("TP_ECC_OBS")) {
            return "datatable-row-alerta";
        }

        /*else if ((observacion == null || observacion.equals("")) && codProg.equals("TP_ECC_TRA")) {
            return "datatable-row-derivado";
        } */
        return "datatable-row-normal";
    }

    private boolean generarExpedienteForSolicitudAutoPSSeg(SspRegistro sspRegistro) {
        boolean validacion = true, estadoTraza = true;

        try {

            if (sspRegistro.getEstadoId().getCodProg().equals("TP_ECC_CRE")) {

                if (sspRegistro.getNroExpediente() != null && !sspRegistro.getNroExpediente().isEmpty()) {
                    return true;
                }

                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                String p_asunto = ((sspRegistro.getEmpresaId().getRuc() != null) ? (((sspRegistro.getEmpresaId().getRznSocial() != null) ? sspRegistro.getEmpresaId().getRznSocial().trim() : sspRegistro.getEmpresaId().getNombresYApellidos().trim()) + " - RUC: " + sspRegistro.getEmpresaId().getRuc()) : (sspRegistro.getEmpresaId().getApePat() + " " + sspRegistro.getEmpresaId().getApeMat() + " " + sspRegistro.getEmpresaId().getNombres() + " - " + (sspRegistro.getEmpresaId().getTipoDoc() != null ? " - " + sspRegistro.getEmpresaId().getTipoDoc().getNombre() : " - DNI") + ": " + sspRegistro.getEmpresaId().getNumDoc()));
                String p_titulo = sspRegistro.getEmpresaId().getTipoId().getCodProg().equals("TP_PER_JUR") ? sspRegistro.getEmpresaId().getRuc() + ", " + sspRegistro.getEmpresaId().getRznSocial() : sspRegistro.getEmpresaId().getNumDoc() + " " + sspRegistro.getEmpresaId().getNombreCompleto();

                SbRecibos comprobante = sspRegistro.getComprobanteId();
                SbPersonaGt cliente = sspRegistro.getEmpresaId();
                registro = ejbSspRegistroFacade.buscarRegistroById(sspRegistro.getId());

                if (!validaTransmitir()) {
                    return false;
                }

                SbDerivacionCydoc derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionCyDoc(p_user.getSistema(), sspRegistro.getTipoProId().getId(), sspRegistro.getSedeSucamec().getId());

                if (derivacion == null) {
                    System.err.println("***Error derivación, ID:" + registro.getId() + ", Solicitud: " + registro.getNroSolicitiud());
                    JsfUtil.mensajeAdvertencia("No se pudo encontrar a un usuario parametrizado para derivación del expediente");
                    return true;
                }

                Integer usuaTramDoc = ejbSbUsuarioFacadeGt.obtenerIdUsuarioTramDoc("USRWEB");
                Integer idProcesoCYDOC = Integer.parseInt(derivacion.getCydocProcesoId() + "");

                /*
                System.out.println("#####################################################################################");
                System.out.println("SbDerivacionCydoc---->"+derivacion);
                System.out.println("idProcesoCYDOC---->"+idProcesoCYDOC);
                System.out.println("comprobante---->"+comprobante);
                System.out.println("cliente---->"+cliente);
                System.out.println("p_asunto---->"+p_asunto);
                System.out.println("idProcesoCYDOC---->"+idProcesoCYDOC);
                System.out.println("p_titulo---->"+p_titulo);
                System.out.println("#####################################################################################");
                System.out.println("***** p_user.getSistema---->"+p_user.getSistema());
                System.out.println("***** idProcesoCYDOC---->"+idProcesoCYDOC);
                System.out.println("***** getSedeSucamec.ID---->"+sspRegistro.getSedeSucamec().getId());
                System.out.println("#####################################################################################");
                 */
                String expediente = wsTramDocController.crearExpediente_gssp(comprobante, cliente, p_asunto, idProcesoCYDOC, usuaTramDoc, usuaTramDoc, p_titulo);
                if (expediente != null) {
                    List<Integer> acciones = new ArrayList();
                    acciones.add(17);   // En Evaluación //TRAMDOC.ACCION
                    registro.setNroExpediente(expediente);

                    try {

                        String loginUserTramDocOrigen = ejbSbUsuarioFacadeGt.obtenerUsuarioTramDocTrazaActualExpediente(expediente); //"USRWEB"
                        String loginUserTramDocDestino = ejbSbUsuarioFacadeGt.obtenerUsuarioTramDoc_x_ID(derivacion.getUsuarioDestinoId().getId()).getUsuario(); //"CMUNOZ

                        /*
                            System.out.println("***** ===============================================");
                            System.out.println("***** expediente---->"+expediente);
                            System.out.println("***** loginUserTramDocOrigen:  ---->"+loginUserTramDocOrigen);
                            System.out.println("***** loginUserTramDocDestino: ---->"+loginUserTramDocDestino);
                            System.out.println("***** p_asunto---->"+p_asunto);
                            System.out.println("***** acciones---->"+acciones);
                            System.out.println("***** ===============================================");
                         */
                        estadoTraza = wsTramDocController.asignarExpediente(expediente, loginUserTramDocOrigen, loginUserTramDocDestino, p_asunto, "", acciones, false, null);
                        if (!estadoTraza) {
                            System.err.println("***No se pudo generar la traza del expediente " + expediente + ", ID:" + registro.getId());
                            JsfUtil.mensajeAdvertencia("No se pudo generar la traza del expediente " + expediente);
                        } else {
                            //System.out.println("<usuarioArchivar: usuarioArchivar>");
                            usuarioArchivar = derivacion.getUsuarioDestinoId().getLogin();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    validacion = false;
                }

            }

        } catch (Exception e) {
            JsfUtil.mensajeError("Ocurrio un error al generar el número de Expediente. ID SSP_Registro: " + sspRegistro.getId());
            e.printStackTrace();
            validacion = false;
        }

        return validacion;
    }

    public void mostrarTblAlmacenamiento() {

        if (regServicioPrestadoSelected != null) {
            regAlmacenLPSSelected = null;
            regCantArmeriaLPS = null;
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar el servicio prestado");
            regAlmacenLPSSelected = null;
            regCantArmeriaLPS = null;
            return;
        }

    }

    public String guardarDatosSolicitud() {

        if (registro.getId() == null) {
            return crearSolicitud();
        } else {
            return guardarEditarSolicitud();
        }
    }

    public String crearSolicitud() {

        try {

            boolean validacionDJ = true;

            if (noAntecedentesPenales == false) {
                validacionDJ = false;
                JsfUtil.mensajeAdvertencia("Debe de marcar No tener antecedentes penales");
            }

            if (noLaboresEntidad == false) {
                validacionDJ = false;
                JsfUtil.mensajeAdvertencia("Debe de marcar No ejercer o haber desempeñado funciones o labores en la SUCAMEC");
            }

            if (noEmpresaSeguridad == false) {
                validacionDJ = false;
                JsfUtil.mensajeAdvertencia("Debe de marcar No haber sido accionista, socio, director, gerente o representante legal de una empresa de seguridad privada");
            }

            if (validacionDJ) {

                //Setea el objeto persona una sola vez del Representante legal o Responsable Legal
                regPersonasRLSelected = ejbSbPersonaFacade.find(Long.parseLong(personaDetalleSelectedString));

                /*if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")){
                    relacionPersonaGtRL = ejbSbRelacionPersonaFacadeGt.listRepresentantes(regPersonasRLSelected, regAdminist).get(0); //(regPersonasRLSelected: destino | regAdminist:origen)
                }else{
                    relacionPersonaGtRL = null;
                }*/
                //Actualiza el Domicilio del Representante Legal o Responsable Legal
                regSbDireccionRL = new SbDireccionGt();
                if (ejbSbDireccionFacade.listarDireccionesXPersona(regPersonasRLSelected.getId()).size() > 0) {
                    regSbDireccionRL = ejbSbDireccionFacade.listarDireccionesXPersona(regPersonasRLSelected.getId()).get(0);
                    regSbDireccionRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DIRECB_FIS"));
                    regSbDireccionRL.setPersonaId(regPersonasRLSelected);
                    regSbDireccionRL.setZonaId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_ZON_URB"));
                    regSbDireccionRL.setViaId(regTipoViasSelected);
                    regSbDireccionRL.setDireccion(regDomicilioRLSelected);
                    regSbDireccionRL.setDistritoId(regDistritoRLSelected);
                    regSbDireccionRL.setActivo(JsfUtil.TRUE);
                    regSbDireccionRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbDireccionRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbDireccionRL.setPaisId(ejbSbPaisFacadeGt.obtenerPaisByNombre("PERU"));
                    regSbDireccionRL = (SbDireccionGt) JsfUtil.entidadMayusculas(regSbDireccionRL, "");
                    ejbSbDireccionFacade.edit(regSbDireccionRL);
                } else {
                    regSbDireccionRL.setId(null);
                    regSbDireccionRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DIRECB_FIS"));
                    regSbDireccionRL.setPersonaId(regPersonasRLSelected);
                    regSbDireccionRL.setZonaId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_ZON_URB"));
                    regSbDireccionRL.setViaId(regTipoViasSelected);
                    regSbDireccionRL.setDireccion(regDomicilioRLSelected);
                    regSbDireccionRL.setDistritoId(regDistritoRLSelected);
                    regSbDireccionRL.setNumero(null);
                    regSbDireccionRL.setReferencia(null);
                    regSbDireccionRL.setGeoLat(null);
                    regSbDireccionRL.setGeoLong(null);
                    regSbDireccionRL.setActivo(JsfUtil.TRUE);
                    regSbDireccionRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbDireccionRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbDireccionRL.setPaisId(ejbSbPaisFacadeGt.obtenerPaisByNombre("PERU"));
                    regSbDireccionRL = (SbDireccionGt) JsfUtil.entidadMayusculas(regSbDireccionRL, "");
                    ejbSbDireccionFacade.create(regSbDireccionRL);
                }
                //Fin de Actualiza el Domicilio del Representante Legal o Responsable Legal

                registro.setId(null);
                registro.setTipoProId(regTipoProcesoId);
                registro.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_ASS"));
                registro.setEmpresaId(regAdminist);

                registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                //registro.setComprobanteId(regDatoRecibos);//Datos del Comprobante Pagado
                registro.setComprobanteId(null);

                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    registro.setRepresentanteId(regPersonasRLSelected);
                }

                registro.setRegistroId(null);
                registro.setNroSolicitiud("S/N");
                //registro.setNroSolicitiud(xNroSolicitiud);       
                registro.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registro.setFecha(new Date());
                registro.setNroExpediente(null);
                //Cuando es Tipo de Local Sucursal graba el registro relacionado de la Resolución del Departamento Principal
                //registro.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR")); //TP_REGIST_NOR / TP_REGIST_MOD
                //registro.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));    //TP_OPE_INI / TP_OPE_REN / TP_OPE_AMP
                registro.setTipoRegId(regTipoRegistroSelected);
                registro.setTipoOpeId(regTipoOperacionSelected);
                registro.setTipoProId(regTipoProcesoId);
                registro.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_ASS"));
                registro.setEmpresaId(regAdminist);
                registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                registro.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registro.setFecha(new Date());
                registro.setActivo(JsfUtil.TRUE);
                registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.setAudNumIp(JsfUtil.getIpAddress());
                registro.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                registro = (SspRegistro) JsfUtil.entidadMayusculas(registro, "");
                if (registro.getId() == null) {
                    ejbSspRegistroFacade.create(registro);
                } else {
                    ejbSspRegistroFacade.edit(registro);
                }

                /* GUARDA REPRESENTANTE LEGAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    representanteRegistro.setId(null);
                    representanteRegistro.setRegistroId(registro);
                    representanteRegistro.setRepresentanteId(regPersonasRLSelected);
                    representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));
                    representanteRegistro.setFecha(new Date());
                    representanteRegistro.setActivo(JsfUtil.TRUE);
                    representanteRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    representanteRegistro.setAudNumIp(JsfUtil.getIpAddress());
                    representanteRegistro = (SspRepresentanteRegistro) JsfUtil.entidadMayusculas(representanteRegistro, "");
                    ejbSspRepresentanteRegistroFacade.create(representanteRegistro);
                }
                /* FIN DE GUARDA REPRESENTANTE LEGAL */

                registroEvento.setId(null);
                registroEvento.setRegistroId(registro);
                registroEvento.setTipoEventoId(registro.getEstadoId());
                registroEvento.setObservacion("Crear solicitud de autorización para la prestación de servicio de seguridad.");
                registroEvento.setFecha(new Date());
                registroEvento.setActivo(JsfUtil.TRUE);
                registroEvento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registroEvento.setAudNumIp(JsfUtil.getIpAddress());
                registroEvento = (SspRegistroEvento) JsfUtil.entidadMayusculas(registroEvento, "");
                ejbSspRegistroEventoFacade.create(registroEvento);

                servicio.setId(null);
                servicio.setRegistroId(registro);
                servicio.setServicioPrestadoId(regServicioPrestadoSelected);
                servicio.setFecha(new Date());
                servicio.setActivo(JsfUtil.TRUE);
                servicio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                servicio.setAudNumIp(JsfUtil.getIpAddress());
                servicio = (SspServicio) JsfUtil.entidadMayusculas(servicio, "");
                ejbSspServicioFacade.create(servicio);

                /* GUARDA DATOS DE REPRESENTANTE LEGAL/RESPONSABLE LEGAL  Y LOCAL DE PRESTACION DE SERVICIO*/
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
                    Long xEmpresaId = regAdminist.getId();
                    xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(regPersonasRLSelected.getId(), xEmpresaId);

                    if (xSbAsientoPersonaRL != null) {
                        //Busca la partida sunarp del Representante para actualizarle los datos
                        partidaSunarpRL = new SbPartidaSunarp();
                        partidaSunarpRL = xSbAsientoPersonaRL.getAsientoId().getPartidaId();
                        partidaSunarpRL.setPartidaRegistral(regRLPartidaRegistral);                
                        partidaSunarpRL.setZonaRegistral(regZonaRegSelectedRL);
                        partidaSunarpRL.setOficinaRegistral(regOficinaRegSelectedRL);
                        partidaSunarpRL.setFecha(new Date());  
                        partidaSunarpRL.setActivo(JsfUtil.TRUE);
                        partidaSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        partidaSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                        partidaSunarpRL = (SbPartidaSunarp) JsfUtil.entidadMayusculas(partidaSunarpRL, "");
                        ejbSbPartidaSunarpFacade.edit(partidaSunarpRL);
                        
                        asientoSunarpRL = new SbAsientoSunarp();
                        asientoSunarpRL = xSbAsientoPersonaRL.getAsientoId();
                        asientoSunarpRL.setPartidaId(partidaSunarpRL);
                        asientoSunarpRL.setNroAsiento(regRLAsientoRegistral);
                        asientoSunarpRL.setFecha(new Date());  
                        asientoSunarpRL.setActivo(JsfUtil.TRUE);
                        asientoSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                        asientoSunarpRL = (SbAsientoSunarp) JsfUtil.entidadMayusculas(asientoSunarpRL, "");
                        ejbSbAsientoSunarpFacade.edit(asientoSunarpRL);
                    
                        asientoPersonaRL = new SbAsientoPersona();
                        asientoPersonaRL = xSbAsientoPersonaRL;
                        asientoPersonaRL.setAsientoId(asientoSunarpRL);
                        asientoPersonaRL.setPersonaId(regPersonasRLSelected);
                        asientoPersonaRL.setEmpresaId(registro.getEmpresaId());
                        asientoPersonaRL.setFecha(new Date());  
                        asientoPersonaRL.setActivo(JsfUtil.TRUE);
                        asientoPersonaRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoPersonaRL.setAudNumIp(JsfUtil.getIpAddress());
                        asientoPersonaRL = (SbAsientoPersona) JsfUtil.entidadMayusculas(asientoPersonaRL, "");
                        ejbSbAsientoPersonaFacade.edit(asientoPersonaRL); 
                        
                    } else {
                        partidaSunarpRL.setId(null);
                        partidaSunarpRL.setPartidaRegistral(regRLPartidaRegistral);
                        partidaSunarpRL.setZonaRegistral(regZonaRegSelectedRL);
                        partidaSunarpRL.setOficinaRegistral(regOficinaRegSelectedRL);
                        partidaSunarpRL.setFecha(new Date());
                        partidaSunarpRL.setActivo(JsfUtil.TRUE);
                        partidaSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        partidaSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                        partidaSunarpRL = (SbPartidaSunarp) JsfUtil.entidadMayusculas(partidaSunarpRL, "");
                        ejbSbPartidaSunarpFacade.create(partidaSunarpRL);

                        asientoSunarpRL.setId(null);
                        asientoSunarpRL.setPartidaId(partidaSunarpRL);
                        asientoSunarpRL.setNroAsiento(regRLAsientoRegistral);
                        asientoSunarpRL.setFecha(new Date());
                        asientoSunarpRL.setActivo(JsfUtil.TRUE);
                        asientoSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                        asientoSunarpRL = (SbAsientoSunarp) JsfUtil.entidadMayusculas(asientoSunarpRL, "");
                        ejbSbAsientoSunarpFacade.create(asientoSunarpRL);

                        asientoPersonaRL.setId(null);
                        asientoPersonaRL.setAsientoId(asientoSunarpRL);
                        asientoPersonaRL.setPersonaId(regPersonasRLSelected);
                        asientoPersonaRL.setEmpresaId(registro.getEmpresaId());
                        asientoPersonaRL.setFecha(new Date());
                        asientoPersonaRL.setActivo(JsfUtil.TRUE);
                        asientoPersonaRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoPersonaRL.setAudNumIp(JsfUtil.getIpAddress());
                        asientoPersonaRL = (SbAsientoPersona) JsfUtil.entidadMayusculas(asientoPersonaRL, "");
                        ejbSbAsientoPersonaFacade.create(asientoPersonaRL);
                    }
                    
                    representanteRegistro.setDireccionRLId(regSbDireccionRL);//BROWN   
                    representanteRegistro.setAsientoRLId(asientoPersonaRL);//BROWN
                    ejbSspRepresentanteRegistroFacade.edit(representanteRegistro);//BROWN

                    /*Panel de Local de Prestación de Servicio*/
                    //Si es un nuevo local se registra
                    if ((Long.parseLong(localAutorizacionSelectedString)) < 0) {
                        localAutorizacion.setId(null);
                        localAutorizacion.setTipoUbicacionId(regTipoViasLPSSelected_Form);
                        localAutorizacion.setDireccion(regDireccionLPS_Form);
                        localAutorizacion.setNroFisico(regNroFisicoLPS_Form);
                        localAutorizacion.setDistritoId(regDistritoLPSSelected);
                        localAutorizacion.setReferencia(regReferenciaLPS_Form);
                        //localAutorizacion.setGeoLatitud(null);
                        //localAutorizacion.setGeoLongitud(null);
                        localAutorizacion.setGeoLatitud(regGeoLatitudLPS_Form);
                        localAutorizacion.setGeoLongitud(regGeoLongitudPS_Form);
                        localAutorizacion.setFecha(new Date());
                        localAutorizacion.setActivo(JsfUtil.TRUE);
                        localAutorizacion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        localAutorizacion.setAudNumIp(JsfUtil.getIpAddress());
                        localAutorizacion = (SspLocalAutorizacion) JsfUtil.entidadMayusculas(localAutorizacion, "");
                        ejbSspLocalAutorizacionFacade.create(localAutorizacion);
                    } else {
                        localAutorizacion = ejbSspLocalAutorizacionFacade.verDatosLocalAutorizacionXiDLocal(Long.parseLong(localAutorizacionSelectedString));
                    }

                    if (localRegistro.getId() == null) {
                        localRegistro.setId(null);
                        localRegistro.setRegistroId(registro);
                        localRegistro.setLocalId(localAutorizacion);
                        localRegistro.setFecha(new Date());
                        localRegistro.setActivo(JsfUtil.TRUE);
                        localRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        localRegistro.setAudNumIp(JsfUtil.getIpAddress());
                        localRegistro = (SspLocalRegistro) JsfUtil.entidadMayusculas(localRegistro, "");
                        ejbSspLocalRegistroFacade.create(localRegistro);
                    } else {
                        localRegistro = ejbSspLocalRegistroFacade.verLocalRegistroXId(localRegistro.getId());
                    }

                    if (tipoUsoLocal.getId() == null) {
                        tipoUsoLocal.setId(null);
                        tipoUsoLocal.setLocalId(localAutorizacion);
                        tipoUsoLocal.setRegistroId(registro);
                        tipoUsoLocal.setTipoLocalId(null);
                        tipoUsoLocal.setTipoUsoId(regTipUsoLPSSelected);
                        tipoUsoLocal.setFecha(new Date());
                        tipoUsoLocal.setActivo(JsfUtil.TRUE);
                        tipoUsoLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        tipoUsoLocal.setAudNumIp(JsfUtil.getIpAddress());
                        tipoUsoLocal = (SspTipoUsoLocal) JsfUtil.entidadMayusculas(tipoUsoLocal, "");
                        ejbSspTipoUsoLocalFacade.create(tipoUsoLocal);
                    } else {
                        tipoUsoLocal = ejbSspTipoUsoLocalFacade.verTipoUsoLocalXId(tipoUsoLocal.getId());
                    }

                }
                /* FIN DE GUARDA DATOS DE REPRESENTANTE LEGAL/RESPONSABLE LEGAL  Y LOCAL DE PRESTACION DE SERVICIO*/

 /* GUARDA DATOS DE MEDIOS DE CONTACTO */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SISPE") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    for (SspContacto itemContacto : lstContactos) {
                        itemContacto.setId(null);
                        itemContacto.setRegistroId(registro);
                        itemContacto.setValor(itemContacto.getValor());
                        itemContacto.setTipoMedioId(itemContacto.getTipoMedioId());
                        itemContacto.setDescripcion(itemContacto.getDescripcion());
                        itemContacto.setFecha(new Date());
                        itemContacto.setActivo(JsfUtil.TRUE);
                        itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                        itemContacto = (SspContacto) JsfUtil.entidadMayusculas(itemContacto, "");
                        ejbSspContactoFacade.create(itemContacto);
                    }
                }
                /* FIN DE GUARDA DATOS DE MEDIOS DE CONTACTO */

 /* GUARDA DATOS DE LICENCIA MUNICIPAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    String fileNameArchivoLM = "";
                    fileNameArchivoLM = nomArchivoLM;
                    fileNameArchivoLM = "ASSGssp_LM_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_LM").toString() + fileNameArchivoLM.substring(fileNameArchivoLM.lastIndexOf('.'), fileNameArchivoLM.length());
                    fileNameArchivoLM = fileNameArchivoLM.toUpperCase();

                    licenciaMunicipal.setId(null);
                    licenciaMunicipal.setLocalId(localAutorizacion);
                    licenciaMunicipal.setNumeroLicencia(regNroLicenciaMunicLPS);
                    licenciaMunicipal.setFechaIni(regFechaEmisLPS);
                    licenciaMunicipal.setFechaFin(regFechaVencLPS);
                    licenciaMunicipal.setIndeterminado(Short.parseShort((regIndeterminadoLPS) ? "1" : "0"));
                    licenciaMunicipal.setTipoMunicipalidad(regTipMunicipalidadLPSSelected);
                    licenciaMunicipal.setMunicipalidadId(regMunicLPSSelected);
                    licenciaMunicipal.setGiroComercial(regGiroComercialLPS);
                    licenciaMunicipal.setNombreArchivo(fileNameArchivoLM);
                    licenciaMunicipal.setValidacionWs(null);
                    licenciaMunicipal.setEstadoWs(null);
                    licenciaMunicipal.setFecha(new Date());
                    licenciaMunicipal.setActivo(JsfUtil.TRUE);
                    licenciaMunicipal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    licenciaMunicipal.setAudNumIp(JsfUtil.getIpAddress());
                    licenciaMunicipal = (SspLicenciaMunicipal) JsfUtil.entidadMayusculas(licenciaMunicipal, "");
                    ejbSspLicenciaMunicipalFacade.create(licenciaMunicipal);
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_LM").getValor() + licenciaMunicipal.getNombreArchivo()), lmByte);

                }
                /* FIN DE GUARDA DATOS DE LICENCIA MUNICIPAL */


 /* GUARDA LOS PARTICIPANTES/SOCIOS */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    for (SspParticipante itemSocio : lstSocios) {
                        itemSocio.setId(null);
                        itemSocio.setRegistroId(registro);
                        itemSocio.setTipoParticipacion(itemSocio.getTipoParticipacion());
                        itemSocio.setCargo((itemSocio.getCargo() != null) ? itemSocio.getCargo() : "");
                        itemSocio.setAporteMonto(itemSocio.getAporteMonto());
                        itemSocio.setNumeroAccion(itemSocio.getNumeroAccion());
                        itemSocio.setPorcentajeAccion(itemSocio.getPorcentajeAccion());
                        itemSocio.setFecha(new Date());
                        itemSocio.setActivo(JsfUtil.TRUE);
                        itemSocio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        itemSocio.setAudNumIp(JsfUtil.getIpAddress());
                        ejbSspParticipanteFacade.create(itemSocio);
                    }
                }
                /* FIN DE GUARDA LOS PARTICIPANTES/SOCIOS */

 /* GUARDA ARCHIVO DE LICENCIA MUNICIPAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    if (licenciaMunicipal.getNombreArchivo() != null && lmByte != null) {

                        SspRequisito requisito = new SspRequisito();
                        requisito.setActivo(JsfUtil.TRUE);
                        requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        requisito.setAudNumIp(JsfUtil.getIpAddress());
                        requisito.setFecha(new Date());
                        requisito.setId(null);
                        requisito.setRegistroId(registro);
                        requisito.setSspArchivoList(new ArrayList());
                        requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_LM"));
                        requisito.setValorRequisito(licenciaMunicipal.getNumeroLicencia());
                        requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");
                        ejbSspRequisitoFacade.create(requisito);

                        SspArchivo archivo = new SspArchivo();
                        archivo.setActivo(JsfUtil.TRUE);
                        archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        archivo.setAudNumIp(JsfUtil.getIpAddress());
                        archivo.setId(null);
                        archivo.setPathupload("Documentos_pathUpload_ASSGssp_LM");
                        archivo.setNombre(licenciaMunicipal.getNombreArchivo());
                        archivo.setRequisitoId(requisito);
                        archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                        archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                        ejbSspArchivoFacade.create(archivo);

                    }
                }
                /* FIN DE GUARDA ARCHIVO DE LICENCIA MUNICIPAL */

 /* GUARDA ARCHIVO DEL PLANO DE DISTRIBUCION */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    //localAutorizacion = ejbSspLocalAutorizacionFacade.verDatosLocalAutorizacionXiDLocal(localRegistro.getLocalId().getId());
                    String fileNameArchivoPlanoDistribucion = "";
                    fileNameArchivoPlanoDistribucion = nomArchivoLocal;
                    fileNameArchivoPlanoDistribucion = "ASSGSSP_LOC" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_FOTO_AS").toString() + fileNameArchivoPlanoDistribucion.substring(fileNameArchivoPlanoDistribucion.lastIndexOf('.'), fileNameArchivoPlanoDistribucion.length());
                    fileNameArchivoPlanoDistribucion = fileNameArchivoPlanoDistribucion.toUpperCase();

                    SspRequisito requisito = new SspRequisito();
                    requisito.setActivo(JsfUtil.TRUE);
                    requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito.setAudNumIp(JsfUtil.getIpAddress());
                    requisito.setFecha(new Date());
                    requisito.setId(null);
                    requisito.setRegistroId(registro);
                    requisito.setSspArchivoList(new ArrayList());
                    requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_PD"));
                    requisito.setValorRequisito(null);
                    requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");
                    ejbSspRequisitoFacade.create(requisito);

                    SspArchivo archivo = new SspArchivo();
                    archivo.setActivo(JsfUtil.TRUE);
                    archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivo.setAudNumIp(JsfUtil.getIpAddress());
                    archivo.setId(null);
                    archivo.setPathupload("Documentos_pathUpload_ASSGssp_LOC");
                    archivo.setNombre(fileNameArchivoPlanoDistribucion);
                    archivo.setRequisitoId(requisito);
                    archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                    ejbSspArchivoFacade.create(archivo);

                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_LOC").getValor() + fileNameArchivoPlanoDistribucion), localByte);
                }
                /* FIN DE GUARDA ARCHIVO DEL PLANO DE DISTRIBUCION */

                //REGISTRAR POLIGONOS
                sspPoligono = new SspPoligono();

                sspPoligono.setRegistroId(registro);
                sspPoligono.setLocalId(localAutorizacion);
                sspPoligono.setNroResolucion(cefoeNumResol);
                sspPoligono.setFechaIni(cefoeFechaResolIni);
                sspPoligono.setFechaFin(cefoeFechaResolFin);
                sspPoligono.setActivo(JsfUtil.TRUE);
                sspPoligono.setPropietarioId(registro.getEmpresaId());
                sspPoligono.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                sspPoligono.setAudNumIp(JsfUtil.getIpAddress());

                SspCefoespInstructor sspCefoespInstructor = new SspCefoespInstructor();

                if (lstCapacitadores.size() > 0) {
                    for (SspCefoespInstructor ItemCapa : lstCapacitadores) {

                        sspCefoespInstructor.setId(null);
                        sspCefoespInstructor.setPersonaId(ItemCapa.getPersonaId());
                        sspCefoespInstructor.setRegistroId(registro);
                        sspCefoespInstructor.setActivo(JsfUtil.TRUE);
                        sspCefoespInstructor.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        sspCefoespInstructor.setAudNumIp(JsfUtil.getIpAddress());
                        ejbSspCefoespInstructorFacade.create(sspCefoespInstructor);

                    }
                }

                System.out.println("localAutorizacion->" + localAutorizacion);
                System.out.println("cefoeNumResol->" + cefoeNumResol);
                System.out.println("cefoeFechaResolIni->" + cefoeFechaResolIni);
                System.out.println("cefoeFechaResolFin->" + cefoeFechaResolFin);
                System.out.println("registro->" + registro);
                System.out.println("getEmpresaId->" + registro.getEmpresaId());
                System.out.println("cefoeTipoLocal->" + cefoeTipoLocal);

                if (cefoeTipoLocal.equals("P")) {
                    sspPoligono.setAlquilado(JsfUtil.FALSE);
                } else if (cefoeTipoLocal.equals("A")) {
                    sspPoligono.setAlquilado(JsfUtil.TRUE);
                } else {
                    sspPoligono.setAlquilado(null);
                }

                ejbSspPoligonoFacade.create(sspPoligono);

                JsfUtil.mensaje("Se registró la solicitud con Código: " + registro.getId());

                return prepareList();

            }

        } catch (Exception e) {
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        }

        return null;
    }

    public String guardarEditarSolicitud() {

        try {

            boolean validacionDJ = true;

            if (noAntecedentesPenales == false) {
                validacionDJ = false;
                JsfUtil.mensajeAdvertencia("Debe de marcar No tener antecedentes penales");
            }

            if (noLaboresEntidad == false) {
                validacionDJ = false;
                JsfUtil.mensajeAdvertencia("Debe de marcar No ejercer o haber desempeñado funciones o labores en la SUCAMEC");
            }

            if (noEmpresaSeguridad == false) {
                validacionDJ = false;
                JsfUtil.mensajeAdvertencia("Debe de marcar No haber sido accionista, socio, director, gerente o representante legal de una empresa de seguridad privada");
            }

            if (validacionDJ) {

                //Fin Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 
                /*if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")){
                    relacionPersonaGtRL = ejbSbRelacionPersonaFacadeGt.listRepresentantes(regPersonasRLSelected, regAdminist).get(0); //(regPersonasRLSelected: destino | regAdminist:origen)
                }else{
                    relacionPersonaGtRL = null;
                }*/
                //Setea el objeto persona una sola vez del Representante legal o Responsable Legal
                regPersonasRLSelected = ejbSbPersonaFacade.find(Long.parseLong(personaDetalleSelectedString));

                //Actualiza el Domicilio del Representante Legal
                regSbDireccionRL = new SbDireccionGt();
                if (ejbSbDireccionFacade.listarDireccionesXPersona(regPersonasRLSelected.getId()).size() > 0) {
                    regSbDireccionRL = ejbSbDireccionFacade.listarDireccionesXPersona(regPersonasRLSelected.getId()).get(0);
                    regSbDireccionRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DIRECB_FIS"));
                    regSbDireccionRL.setPersonaId(regPersonasRLSelected);
                    regSbDireccionRL.setZonaId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_ZON_URB"));
                    regSbDireccionRL.setViaId(regTipoViasSelected);
                    regSbDireccionRL.setDireccion(regDomicilioRLSelected);
                    regSbDireccionRL.setDistritoId(regDistritoRLSelected);
                    regSbDireccionRL.setActivo(JsfUtil.TRUE);
                    regSbDireccionRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbDireccionRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbDireccionRL.setPaisId(ejbSbPaisFacadeGt.obtenerPaisByNombre("PERU"));
                    regSbDireccionRL = (SbDireccionGt) JsfUtil.entidadMayusculas(regSbDireccionRL, "");
                    ejbSbDireccionFacade.edit(regSbDireccionRL);
                } else {
                    regSbDireccionRL.setId(null);
                    regSbDireccionRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DIRECB_FIS"));
                    regSbDireccionRL.setPersonaId(regPersonasRLSelected);
                    regSbDireccionRL.setZonaId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_ZON_URB"));
                    regSbDireccionRL.setViaId(regTipoViasSelected);
                    regSbDireccionRL.setDireccion(regDomicilioRLSelected);
                    regSbDireccionRL.setDistritoId(regDistritoRLSelected);
                    regSbDireccionRL.setNumero(null);
                    regSbDireccionRL.setReferencia(null);
                    regSbDireccionRL.setGeoLat(null);
                    regSbDireccionRL.setGeoLong(null);
                    regSbDireccionRL.setActivo(JsfUtil.TRUE);
                    regSbDireccionRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbDireccionRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbDireccionRL.setPaisId(ejbSbPaisFacadeGt.obtenerPaisByNombre("PERU"));
                    regSbDireccionRL = (SbDireccionGt) JsfUtil.entidadMayusculas(regSbDireccionRL, "");
                    ejbSbDireccionFacade.create(regSbDireccionRL);
                }
                //Fin de Actualiza el Domicilio del Representante Legal

                //registro.setId(null);
                registro.setTipoProId(regTipoProcesoId);
                registro.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_ASS"));
                registro.setEmpresaId(regAdminist);

                //Cuando es Tipo de Local Sucursal graba el registro relacionado de la Resolución del Departamento Principal
                //El estado CREADO al inicio no cambia, cuando modifica o corrige las observaciones se tiene que ver
                //registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                //registro.setComprobanteId(regDatoRecibos);//Datos del Comprobante Pagado
                //El comprobante de pago todavia no esta habilitado
                //registro.setComprobanteId(null);
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    registro.setRepresentanteId(regPersonasRLSelected);
                }

                //La referencia recursiva de otro Registro se debe ver la casuistica mas adelante
                //registro.setRegistroId(null);                
                //El numero de solicitud no cambia               
                //registro.setNroSolicitiud("S/N");   
                //registro.setNroSolicitiud(xNroSolicitiud);       
                //El numero de expediente no cambia al editar
                //registro.setNroExpediente(null);
                //El tipo de Registro y operación como esta inhabilitado graba lo mismo    
                registro.setTipoRegId(regTipoRegistroSelected);  //TP_REGIST_NOR / TP_REGIST_MOD
                registro.setTipoOpeId(regTipoOperacionSelected); //TP_OPE_INI / TP_OPE_REN / TP_OPE_AMP
                registro.setTipoProId(regTipoProcesoId);
                registro.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_ASS"));
                registro.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registro.setActivo(JsfUtil.TRUE);
                registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.setAudNumIp(JsfUtil.getIpAddress());
                registro.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                registro = (SspRegistro) JsfUtil.entidadMayusculas(registro, "");
                ejbSspRegistroFacade.edit(registro);

                //Buscamos las cartas fianzas que no estan relacionadas a ningun registro para inactivarlas
                List<SspCartaFianza> lstNoRegCarta = new ArrayList<>();
                lstNoRegCarta = ejbSspCartaFianzaFacade.buscarCartasFianzasSinRelacionRegistros();
                if (lstNoRegCarta != null) {
                    for (SspCartaFianza loDetalle : lstNoRegCarta) {
                        loDetalle.setActivo(JsfUtil.FALSE);
                        ejbSspCartaFianzaFacade.edit(loDetalle);
                    }
                }

                /* GUARDA REPRESENTANTE LEGAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    //Busca el Representante Registro y actualiza
                    representanteRegistro = ejbSspRepresentanteRegistroFacade.representRegFindByRegistroId(registro.getId(), registro.getEmpresaId().getId());
                    if (registro.getRepresentanteId() == null) {
                        //Si no existe crea el registro
                        representanteRegistro.setId(null);
                        representanteRegistro.setRegistroId(registro);
                        representanteRegistro.setRepresentanteId(regPersonasRLSelected);
                        representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));

                        representanteRegistro.setFecha(new Date());
                        representanteRegistro.setActivo(JsfUtil.TRUE);
                        representanteRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        representanteRegistro.setAudNumIp(JsfUtil.getIpAddress());
                        representanteRegistro = (SspRepresentanteRegistro) JsfUtil.entidadMayusculas(representanteRegistro, "");
                        ejbSspRepresentanteRegistroFacade.create(representanteRegistro);
                    } else {
                        //Si ya existe un registro lo actualiza con el nuevo/o el mismo
                        representanteRegistro.setRegistroId(registro);
                        representanteRegistro.setRepresentanteId(regPersonasRLSelected);
                        //Graba el Representante Legal
                        representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));
                        representanteRegistro.setFecha(new Date());
                        representanteRegistro.setActivo(JsfUtil.TRUE);
                        representanteRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        representanteRegistro.setAudNumIp(JsfUtil.getIpAddress());
                        representanteRegistro = (SspRepresentanteRegistro) JsfUtil.entidadMayusculas(representanteRegistro, "");
                        ejbSspRepresentanteRegistroFacade.edit(representanteRegistro);
                    }

                }
                /* FIN DE GUARDA REPRESENTANTE LEGAL */

                //Busca el Servicio y actualiza
                servicio = ejbSspServicioFacade.buscarServicioByRegistroId(registro);
                if (registro.getRepresentanteId() == null) {
                    //Si no existe crea el registro
                    servicio = new SspServicio();
                    servicio.setId(null);
                    servicio.setRegistroId(registro);
                    servicio.setServicioPrestadoId(regServicioPrestadoSelected);
                    servicio.setFecha(new Date());
                    servicio.setActivo(JsfUtil.TRUE);
                    servicio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    servicio.setAudNumIp(JsfUtil.getIpAddress());
                    servicio = (SspServicio) JsfUtil.entidadMayusculas(servicio, "");
                    ejbSspServicioFacade.create(servicio);
                } else {
                    //Si ya existe un registro lo actualiza con el nuevo servicio/o el mismo
                    servicio.setRegistroId(registro);
                    servicio.setServicioPrestadoId(regServicioPrestadoSelected);
                    servicio.setFecha(new Date());
                    servicio.setActivo(JsfUtil.TRUE);
                    servicio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    servicio.setAudNumIp(JsfUtil.getIpAddress());
                    servicio = (SspServicio) JsfUtil.entidadMayusculas(servicio, "");
                    ejbSspServicioFacade.edit(servicio);
                }

                /* GUARDA DATOS DE REPRESENTANTE LEGAL Y LOCAL DE PRESTACION DE SERVICIO*/
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
                    Long xEmpresaId = regAdminist.getId();
                    xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(regPersonasRLSelected.getId(), xEmpresaId);

                    if (xSbAsientoPersonaRL != null) {
                        //Busca la partida sunarp del Representante para actualizarle los datos
                        partidaSunarpRL = new SbPartidaSunarp();
                        partidaSunarpRL = xSbAsientoPersonaRL.getAsientoId().getPartidaId();
                        partidaSunarpRL.setPartidaRegistral(regRLPartidaRegistral);
                        partidaSunarpRL.setZonaRegistral(regZonaRegSelectedRL);
                        partidaSunarpRL.setOficinaRegistral(regOficinaRegSelectedRL);
                        partidaSunarpRL.setFecha(new Date());
                        partidaSunarpRL.setActivo(JsfUtil.TRUE);
                        partidaSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        partidaSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                        partidaSunarpRL = (SbPartidaSunarp) JsfUtil.entidadMayusculas(partidaSunarpRL, "");
                        ejbSbPartidaSunarpFacade.edit(partidaSunarpRL);

                        asientoSunarpRL = new SbAsientoSunarp();
                        asientoSunarpRL = xSbAsientoPersonaRL.getAsientoId();
                        asientoSunarpRL.setPartidaId(partidaSunarpRL);
                        asientoSunarpRL.setNroAsiento(regRLAsientoRegistral);
                        asientoSunarpRL.setFecha(new Date());
                        asientoSunarpRL.setActivo(JsfUtil.TRUE);
                        asientoSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                        asientoSunarpRL = (SbAsientoSunarp) JsfUtil.entidadMayusculas(asientoSunarpRL, "");
                        ejbSbAsientoSunarpFacade.edit(asientoSunarpRL);

                        asientoPersonaRL = new SbAsientoPersona();
                        asientoPersonaRL = xSbAsientoPersonaRL;
                        asientoPersonaRL.setAsientoId(asientoSunarpRL);
                        asientoPersonaRL.setPersonaId(regPersonasRLSelected);
                        asientoPersonaRL.setEmpresaId(registro.getEmpresaId());
                        asientoPersonaRL.setFecha(new Date());
                        asientoPersonaRL.setActivo(JsfUtil.TRUE);
                        asientoPersonaRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoPersonaRL.setAudNumIp(JsfUtil.getIpAddress());
                        asientoPersonaRL = (SbAsientoPersona) JsfUtil.entidadMayusculas(asientoPersonaRL, "");
                        ejbSbAsientoPersonaFacade.edit(asientoPersonaRL);

                    } else {
                        partidaSunarpRL = new SbPartidaSunarp();
                        partidaSunarpRL.setId(null);
                        partidaSunarpRL.setPartidaRegistral(regRLPartidaRegistral);
                        partidaSunarpRL.setZonaRegistral(regZonaRegSelectedRL);
                        partidaSunarpRL.setOficinaRegistral(regOficinaRegSelectedRL);
                        partidaSunarpRL.setFecha(new Date());
                        partidaSunarpRL.setActivo(JsfUtil.TRUE);
                        partidaSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        partidaSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                        partidaSunarpRL = (SbPartidaSunarp) JsfUtil.entidadMayusculas(partidaSunarpRL, "");
                        ejbSbPartidaSunarpFacade.create(partidaSunarpRL);

                        asientoSunarpRL = new SbAsientoSunarp();
                        asientoSunarpRL.setId(null);
                        asientoSunarpRL.setPartidaId(partidaSunarpRL);
                        asientoSunarpRL.setNroAsiento(regRLAsientoRegistral);
                        asientoSunarpRL.setFecha(new Date());
                        asientoSunarpRL.setActivo(JsfUtil.TRUE);
                        asientoSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                        asientoSunarpRL = (SbAsientoSunarp) JsfUtil.entidadMayusculas(asientoSunarpRL, "");
                        ejbSbAsientoSunarpFacade.create(asientoSunarpRL);

                        asientoPersonaRL = new SbAsientoPersona();
                        asientoPersonaRL.setId(null);
                        asientoPersonaRL.setAsientoId(asientoSunarpRL);
                        asientoPersonaRL.setPersonaId(regPersonasRLSelected);
                        asientoPersonaRL.setEmpresaId(registro.getEmpresaId());
                        asientoPersonaRL.setFecha(new Date());
                        asientoPersonaRL.setActivo(JsfUtil.TRUE);
                        asientoPersonaRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoPersonaRL.setAudNumIp(JsfUtil.getIpAddress());
                        asientoPersonaRL = (SbAsientoPersona) JsfUtil.entidadMayusculas(asientoPersonaRL, "");
                        ejbSbAsientoPersonaFacade.create(asientoPersonaRL);
                    }
                    
                    representanteRegistro.setDireccionRLId(regSbDireccionRL);//BROWN   
                    representanteRegistro.setAsientoRLId(asientoPersonaRL);//BROWN
                    ejbSspRepresentanteRegistroFacade.edit(representanteRegistro);//BROWN
                    

                    /*Panel de Local de Prestación de Servicio*/
                    //Si es un nuevo local se registra
                    if ((Long.parseLong(localAutorizacionSelectedString)) < 0) {
                        localAutorizacion = new SspLocalAutorizacion();
                        localAutorizacion.setId(null);
                        localAutorizacion.setTipoUbicacionId(regTipoViasLPSSelected_Form);
                        localAutorizacion.setDireccion(regDireccionLPS_Form);
                        localAutorizacion.setNroFisico(regNroFisicoLPS_Form);
                        localAutorizacion.setDistritoId(regDistritoLPSSelected);
                        localAutorizacion.setReferencia(regReferenciaLPS_Form);
                        localAutorizacion.setGeoLatitud(regGeoLatitudLPS_Form);
                        localAutorizacion.setGeoLongitud(regGeoLongitudPS_Form);
                        localAutorizacion.setFecha(new Date());
                        localAutorizacion.setActivo(JsfUtil.TRUE);
                        localAutorizacion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        localAutorizacion.setAudNumIp(JsfUtil.getIpAddress());
                        localAutorizacion = (SspLocalAutorizacion) JsfUtil.entidadMayusculas(localAutorizacion, "");
                        ejbSspLocalAutorizacionFacade.create(localAutorizacion);
                    } else {
                        localAutorizacion = ejbSspLocalAutorizacionFacade.verDatosLocalAutorizacionXiDLocal(Long.parseLong(localAutorizacionSelectedString));
                    }

                    //Busca el Local Registro anterior para editarlo
                    localRegistro = ejbSspLocalRegistroFacade.verLocalRegistroXIdSspReg(registro.getId());
                    if (localRegistro == null) {
                        //Si es nuevo lo crea
                        localRegistro = new SspLocalRegistro();
                        localRegistro.setId(null);
                        localRegistro.setRegistroId(registro);
                        localRegistro.setLocalId(localAutorizacion);
                        localRegistro.setFecha(new Date());
                        localRegistro.setActivo(JsfUtil.TRUE);
                        localRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        localRegistro.setAudNumIp(JsfUtil.getIpAddress());
                        localRegistro = (SspLocalRegistro) JsfUtil.entidadMayusculas(localRegistro, "");
                        ejbSspLocalRegistroFacade.create(localRegistro);
                    } else {
                        //Actualiza con el nuevo local
                        localRegistro.setRegistroId(registro);
                        localRegistro.setLocalId(localAutorizacion);
                        localRegistro.setFecha(new Date());
                        localRegistro.setActivo(JsfUtil.TRUE);
                        localRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        localRegistro.setAudNumIp(JsfUtil.getIpAddress());
                        localRegistro = (SspLocalRegistro) JsfUtil.entidadMayusculas(localRegistro, "");
                        ejbSspLocalRegistroFacade.edit(localRegistro);
                    }

                    //Busca el Tipo Uso del Registro anterior para editarlo
                    tipoUsoLocal = ejbSspTipoUsoLocalFacade.verTipoUsoLocalXIdSspReg(registro.getId());
                    if (tipoUsoLocal == null) {
                        //Si es nuevo lo crea
                        tipoUsoLocal = new SspTipoUsoLocal();
                        tipoUsoLocal.setId(null);
                        tipoUsoLocal.setLocalId(localAutorizacion);
                        tipoUsoLocal.setRegistroId(registro);
                        tipoUsoLocal.setTipoLocalId(null);
                        tipoUsoLocal.setTipoUsoId(regTipUsoLPSSelected);
                        tipoUsoLocal.setFecha(new Date());
                        tipoUsoLocal.setActivo(JsfUtil.TRUE);
                        tipoUsoLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        tipoUsoLocal.setAudNumIp(JsfUtil.getIpAddress());
                        tipoUsoLocal = (SspTipoUsoLocal) JsfUtil.entidadMayusculas(tipoUsoLocal, "");
                        ejbSspTipoUsoLocalFacade.create(tipoUsoLocal);
                    } else {
                        //Actualiza con el nuevo local
                        tipoUsoLocal.setLocalId(localAutorizacion);
                        tipoUsoLocal.setRegistroId(registro);
                        tipoUsoLocal.setTipoLocalId(null);
                        tipoUsoLocal.setTipoUsoId(regTipUsoLPSSelected);
                        tipoUsoLocal.setFecha(new Date());
                        tipoUsoLocal.setActivo(JsfUtil.TRUE);
                        tipoUsoLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        tipoUsoLocal.setAudNumIp(JsfUtil.getIpAddress());
                        tipoUsoLocal = (SspTipoUsoLocal) JsfUtil.entidadMayusculas(tipoUsoLocal, "");
                        ejbSspTipoUsoLocalFacade.edit(tipoUsoLocal);
                    }

                }
                /* FIN DE GUARDA DATOS DE REPRESENTANTE LEGAL Y LOCAL DE PRESTACION DE SERVICIO*/

 /* GUARDA DATOS DE MEDIOS DE CONTACTO */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SISPE") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    for (SspContacto itemContacto : lstContactos) {
                        //Si es nuevo contacto lo crea
                        if (itemContacto.getId() < 0) {
                            itemContacto.setId(null);
                            itemContacto.setRegistroId(registro);
                            itemContacto.setValor(itemContacto.getValor());
                            itemContacto.setTipoMedioId(itemContacto.getTipoMedioId());
                            itemContacto.setDescripcion(itemContacto.getDescripcion());
                            itemContacto.setFecha(new Date());
                            itemContacto.setActivo(JsfUtil.TRUE);
                            itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                            itemContacto = (SspContacto) JsfUtil.entidadMayusculas(itemContacto, "");
                            ejbSspContactoFacade.create(itemContacto);
                        } else {
                            //Si es un contacto existente edita los datos
                            itemContacto.setRegistroId(registro);
                            itemContacto.setValor(itemContacto.getValor());
                            itemContacto.setTipoMedioId(itemContacto.getTipoMedioId());
                            itemContacto.setDescripcion(itemContacto.getDescripcion());
                            itemContacto.setFecha(new Date());
                            itemContacto.setActivo(JsfUtil.TRUE);
                            itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                            itemContacto = (SspContacto) JsfUtil.entidadMayusculas(itemContacto, "");
                            ejbSspContactoFacade.edit(itemContacto);
                        }
                    }

                    //Inactiva los IDS que fueron eliminados
                    if (listaUpdateContactosActivos.size() > 0) {
                        for (SspContacto it : listaUpdateContactosActivos) {
                            it.setActivo(JsfUtil.FALSE);
                            ejbSspContactoFacade.edit(it);
                        }
                    }

                }
                /* FIN DE GUARDA DATOS DE MEDIOS DE CONTACTO */

 /* GUARDA DATOS DE LICENCIA MUNICIPAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    String fileNameArchivoLM = "";

                    //Si ha seleccionado otro archivo genera nombre del archivo
                    if (nomArchivoLM != null) {
                        fileNameArchivoLM = nomArchivoLM;
                        fileNameArchivoLM = "ASSGssp_LM_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_LM").toString() + fileNameArchivoLM.substring(fileNameArchivoLM.lastIndexOf('.'), fileNameArchivoLM.length());
                        fileNameArchivoLM = fileNameArchivoLM.toUpperCase();
                    } else {
                        //Setea el nombre anterior del archivo
                        fileNameArchivoLM = nomArchivoLMAnterior;
                        fileNameArchivoLM = fileNameArchivoLM.toUpperCase();
                    }

                    //Si es un nuevo local inactiva la Anterior Licencia Municipal
                    if ((Long.parseLong(localAutorizacionSelectedString)) < 0) {
                        //Inactiva la anterior Licencia Municipal para crear una nueva
                        SspLicenciaMunicipal licenciaMunicipalAnterior = new SspLicenciaMunicipal();
                        licenciaMunicipalAnterior = ejbSspLicenciaMunicipalFacade.verUltimaLicenciaMunicipalXIdLocal(localAutorizacionAnterior.getId());
                        if (licenciaMunicipalAnterior != null) {
                            licenciaMunicipalAnterior.setActivo(JsfUtil.FALSE);
                            ejbSspLicenciaMunicipalFacade.edit(licenciaMunicipalAnterior);
                        }
                    }

                    //Busca la ultima licencia municipal del Local (deberia ser la misma del Registro de la Autorización)
                    //Si era nuevo Local no va a traer registros y va crear una nueva licencia municipal
                    licenciaMunicipal = ejbSspLicenciaMunicipalFacade.verUltimaLicenciaMunicipalXIdLocal(localAutorizacion.getId());
                    if (licenciaMunicipal == null) {
                        //Si no existe lo crea
                        licenciaMunicipal = new SspLicenciaMunicipal();
                        licenciaMunicipal.setId(null);
                        licenciaMunicipal.setLocalId(localAutorizacion);
                        licenciaMunicipal.setNumeroLicencia(regNroLicenciaMunicLPS);
                        licenciaMunicipal.setFechaIni(regFechaEmisLPS);
                        licenciaMunicipal.setFechaFin(regFechaVencLPS);
                        licenciaMunicipal.setIndeterminado(Short.parseShort((regIndeterminadoLPS) ? "1" : "0"));
                        licenciaMunicipal.setTipoMunicipalidad(regTipMunicipalidadLPSSelected);
                        licenciaMunicipal.setMunicipalidadId(regMunicLPSSelected);
                        licenciaMunicipal.setGiroComercial(regGiroComercialLPS);
                        licenciaMunicipal.setNombreArchivo(fileNameArchivoLM);
                        licenciaMunicipal.setValidacionWs(null);
                        licenciaMunicipal.setEstadoWs(null);
                        licenciaMunicipal.setFecha(new Date());
                        licenciaMunicipal.setActivo(JsfUtil.TRUE);
                        licenciaMunicipal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        licenciaMunicipal.setAudNumIp(JsfUtil.getIpAddress());
                        licenciaMunicipal = (SspLicenciaMunicipal) JsfUtil.entidadMayusculas(licenciaMunicipal, "");
                        ejbSspLicenciaMunicipalFacade.create(licenciaMunicipal);
                    } else {
                        //Si existe actualiza los datos
                        licenciaMunicipal.setLocalId(localAutorizacion);
                        licenciaMunicipal.setNumeroLicencia(regNroLicenciaMunicLPS);
                        licenciaMunicipal.setFechaIni(regFechaEmisLPS);
                        licenciaMunicipal.setFechaFin(regFechaVencLPS);
                        licenciaMunicipal.setIndeterminado(Short.parseShort((regIndeterminadoLPS) ? "1" : "0"));
                        licenciaMunicipal.setTipoMunicipalidad(regTipMunicipalidadLPSSelected);
                        licenciaMunicipal.setMunicipalidadId(regMunicLPSSelected);
                        licenciaMunicipal.setGiroComercial(regGiroComercialLPS);
                        licenciaMunicipal.setNombreArchivo(fileNameArchivoLM);
                        licenciaMunicipal.setValidacionWs(null);
                        licenciaMunicipal.setEstadoWs(null);
                        licenciaMunicipal.setFecha(new Date());
                        licenciaMunicipal.setActivo(JsfUtil.TRUE);
                        licenciaMunicipal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        licenciaMunicipal.setAudNumIp(JsfUtil.getIpAddress());
                        licenciaMunicipal = (SspLicenciaMunicipal) JsfUtil.entidadMayusculas(licenciaMunicipal, "");
                        ejbSspLicenciaMunicipalFacade.edit(licenciaMunicipal);
                    }

                    //Si ha seleccionado otro archivo de Licencia Municipal lo convierte en byte
                    if (lmByte != null) {
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_LM").getValor() + licenciaMunicipal.getNombreArchivo()), lmByte);
                    }

                }
                /* FIN DE GUARDA DATOS DE LICENCIA MUNICIPAL */

 /* GUARDA DATOS DE CAPACITADORES ACREDITADOS */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP")) {
                    for (SspCefoespInstructor itemCapacitador : lstCapacitadores) {
                        if (itemCapacitador.getId() < 0) {
                            itemCapacitador.setId(null);
                            itemCapacitador.setPersonaId(itemCapacitador.getPersonaId());
                            itemCapacitador.setRegistroId(registro);
                            itemCapacitador.setActivo(JsfUtil.TRUE);
                            itemCapacitador.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemCapacitador.setAudNumIp(JsfUtil.getIpAddress());
                            itemCapacitador = (SspCefoespInstructor) JsfUtil.entidadMayusculas(itemCapacitador, "");
                            ejbSspCefoespInstructorFacade.create(itemCapacitador);
                        } else {
                            //Si es un capacitador existente edita los datos                    
                            //itemCapacitador.setId(null);
                            itemCapacitador.setPersonaId(itemCapacitador.getPersonaId());
                            itemCapacitador.setRegistroId(registro);
                            itemCapacitador.setActivo(JsfUtil.TRUE);
                            itemCapacitador.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemCapacitador.setAudNumIp(JsfUtil.getIpAddress());
                            itemCapacitador = (SspCefoespInstructor) JsfUtil.entidadMayusculas(itemCapacitador, "");
                            ejbSspCefoespInstructorFacade.edit(itemCapacitador);
                        }
                    }
                }
                /* FIN DE GUARDA DATOS DE CAPACITADORES ACREDITADOS */

                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    for (SspParticipante itemSocio : lstSocios) {
                        //Si es nuevo contacto lo crea
                        if (itemSocio.getId() < 0) {
                            itemSocio.setId(null);
                            itemSocio.setRegistroId(registro);
                            itemSocio.setTipoParticipacion(itemSocio.getTipoParticipacion());
                            itemSocio.setCargo((itemSocio.getCargo() != null) ? itemSocio.getCargo() : "");
                            itemSocio.setAporteMonto(itemSocio.getAporteMonto());
                            itemSocio.setNumeroAccion(itemSocio.getNumeroAccion());
                            itemSocio.setPorcentajeAccion(itemSocio.getPorcentajeAccion());
                            itemSocio.setFecha(new Date());
                            itemSocio.setActivo(JsfUtil.TRUE);
                            itemSocio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemSocio.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspParticipanteFacade.create(itemSocio);
                        } else {
                            //Si es un contacto existente edita los datos
                            itemSocio.setRegistroId(registro);
                            itemSocio.setTipoParticipacion(itemSocio.getTipoParticipacion());
                            itemSocio.setCargo((itemSocio.getCargo() != null) ? itemSocio.getCargo() : "");
                            itemSocio.setFecha(new Date());
                            itemSocio.setActivo(JsfUtil.TRUE);
                            itemSocio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemSocio.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspParticipanteFacade.edit(itemSocio);
                        }
                    }

                    //Inactiva los IDS que fueron eliminados
                    if (listaUpdateParticipanteActivos.size() > 0) {
                        for (SspParticipante it : listaUpdateParticipanteActivos) {
                            it.setActivo(JsfUtil.FALSE);
                            ejbSspParticipanteFacade.edit(it);
                        }
                    }
                }

                /* GUARDA ARCHIVO DE LICENCIA MUNICIPAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    if (licenciaMunicipal.getNombreArchivo() != null) {

                        SspArchivo archLicenciaMunicipal = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_LM");
                        if (archLicenciaMunicipal != null) {
                            //Inactiva el Anterior Requisito del Archivo de Carta Fianza
                            SspRequisito requisitoAnteriorLicenciaMunicipal = new SspRequisito();
                            requisitoAnteriorLicenciaMunicipal = archLicenciaMunicipal.getRequisitoId();
                            requisitoAnteriorLicenciaMunicipal.setActivo(JsfUtil.FALSE);
                            requisitoAnteriorLicenciaMunicipal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            requisitoAnteriorLicenciaMunicipal.setAudNumIp(JsfUtil.getIpAddress());
                            requisitoAnteriorLicenciaMunicipal.setFecha(new Date());
                            requisitoAnteriorLicenciaMunicipal = (SspRequisito) JsfUtil.entidadMayusculas(requisitoAnteriorLicenciaMunicipal, "");
                            ejbSspRequisitoFacade.edit(requisitoAnteriorLicenciaMunicipal);

                            //Inactiva el Anterior Archivo de Licencia Municipal
                            SspArchivo archivoAnteriorLicenciaMunicipal = new SspArchivo();
                            archivoAnteriorLicenciaMunicipal = archLicenciaMunicipal;
                            archivoAnteriorLicenciaMunicipal.setActivo(JsfUtil.FALSE);
                            archivoAnteriorLicenciaMunicipal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            archivoAnteriorLicenciaMunicipal.setAudNumIp(JsfUtil.getIpAddress());
                            archivoAnteriorLicenciaMunicipal = (SspArchivo) JsfUtil.entidadMayusculas(archivoAnteriorLicenciaMunicipal, "");
                            ejbSspArchivoFacade.edit(archivoAnteriorLicenciaMunicipal);
                        }

                        //Crea un nuevo requisito y archivo para el Registro
                        SspRequisito requisito = new SspRequisito();
                        requisito.setActivo(JsfUtil.TRUE);
                        requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        requisito.setAudNumIp(JsfUtil.getIpAddress());
                        requisito.setFecha(new Date());
                        requisito.setId(null);
                        requisito.setRegistroId(registro);
                        requisito.setSspArchivoList(new ArrayList());
                        requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_LM"));
                        requisito.setValorRequisito(licenciaMunicipal.getNumeroLicencia());
                        requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");
                        ejbSspRequisitoFacade.create(requisito);

                        SspArchivo archivo = new SspArchivo();
                        archivo.setActivo(JsfUtil.TRUE);
                        archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        archivo.setAudNumIp(JsfUtil.getIpAddress());
                        archivo.setId(null);
                        archivo.setPathupload("Documentos_pathUpload_ASSGssp_LM");
                        archivo.setNombre(licenciaMunicipal.getNombreArchivo());
                        archivo.setRequisitoId(requisito);
                        archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                        archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                        ejbSspArchivoFacade.create(archivo);

                    }
                }
                /* FIN DE GUARDA ARCHIVO DE LICENCIA MUNICIPAL */

 /* GUARDA ARCHIVO DEL PLANO DE DISTRIBUCION */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_DEP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    String fileNameArchivoPlanoDistribucion = "";

                    //Si ha seleccionado otro archivo genera nombre del archivo
                    if (nomArchivoLocal != null) {
                        fileNameArchivoPlanoDistribucion = nomArchivoLocal;
                        fileNameArchivoPlanoDistribucion = "ASSGSSP_LOC" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_FOTO_AS").toString() + fileNameArchivoPlanoDistribucion.substring(fileNameArchivoPlanoDistribucion.lastIndexOf('.'), fileNameArchivoPlanoDistribucion.length());
                        fileNameArchivoPlanoDistribucion = fileNameArchivoPlanoDistribucion.toUpperCase();
                    } else {
                        //Setea el nombre anterior del archivo
                        fileNameArchivoPlanoDistribucion = nomArchivoLocalAnterior;
                        fileNameArchivoPlanoDistribucion = fileNameArchivoPlanoDistribucion.toUpperCase();
                    }

                    SspArchivo archPlanoDistribucion = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_PD");
                    if (archPlanoDistribucion != null) {
                        //Inactiva el Anterior Requisito del Archivo de Plano de Distribucion
                        SspRequisito requisitoAnteriorPlanoDistribucion = new SspRequisito();
                        requisitoAnteriorPlanoDistribucion = archPlanoDistribucion.getRequisitoId();
                        requisitoAnteriorPlanoDistribucion.setActivo(JsfUtil.FALSE);
                        requisitoAnteriorPlanoDistribucion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        requisitoAnteriorPlanoDistribucion.setAudNumIp(JsfUtil.getIpAddress());
                        requisitoAnteriorPlanoDistribucion.setFecha(new Date());
                        requisitoAnteriorPlanoDistribucion = (SspRequisito) JsfUtil.entidadMayusculas(requisitoAnteriorPlanoDistribucion, "");
                        ejbSspRequisitoFacade.edit(requisitoAnteriorPlanoDistribucion);

                        //Inactiva el Anterior Archivo de Plano de Distribucion
                        SspArchivo archivoAnteriorPlanoDistribucion = new SspArchivo();
                        archivoAnteriorPlanoDistribucion = archPlanoDistribucion;
                        archivoAnteriorPlanoDistribucion.setActivo(JsfUtil.FALSE);
                        archivoAnteriorPlanoDistribucion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        archivoAnteriorPlanoDistribucion.setAudNumIp(JsfUtil.getIpAddress());
                        archivoAnteriorPlanoDistribucion = (SspArchivo) JsfUtil.entidadMayusculas(archivoAnteriorPlanoDistribucion, "");
                        ejbSspArchivoFacade.edit(archivoAnteriorPlanoDistribucion);
                    }

                    //Crea un nuevo requisito y archivo para el Registro
                    SspRequisito requisito = new SspRequisito();
                    requisito.setActivo(JsfUtil.TRUE);
                    requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito.setAudNumIp(JsfUtil.getIpAddress());
                    requisito.setFecha(new Date());
                    requisito.setId(null);
                    requisito.setRegistroId(registro);
                    requisito.setSspArchivoList(new ArrayList());
                    requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_PD"));
                    requisito.setValorRequisito(null);
                    requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");
                    ejbSspRequisitoFacade.create(requisito);

                    SspArchivo archivo = new SspArchivo();
                    archivo.setActivo(JsfUtil.TRUE);
                    archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivo.setAudNumIp(JsfUtil.getIpAddress());
                    archivo.setId(null);
                    archivo.setPathupload("Documentos_pathUpload_ASSGssp_LOC");
                    archivo.setNombre(fileNameArchivoPlanoDistribucion);
                    archivo.setRequisitoId(requisito);
                    archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                    ejbSspArchivoFacade.create(archivo);

                    //Si ha seleccionado otro archivo de Plano de Distribucion lo convierte en byte
                    if (localByte != null) {
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_LOC").getValor() + fileNameArchivoPlanoDistribucion), localByte);
                    }

                }
                /* FIN DE GUARDA ARCHIVO DEL PLANO DE DISTRIBUCION */

                JsfUtil.mensaje("Se actualizó la solicitud con Código: " + registro.getId() + " y con Nro. de Solicitud " + registro.getNroSolicitiud());
                return prepareList();

            }
        } catch (Exception e) {
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        }

        return null;
    }

    public String obtenerNroSolicitudEmision() {
        String nroSolicitud = "";
        nroSolicitud = ejbSspRegistroFacade.obtenerNroSolicitudEmisionRegistro() + "-" + JsfUtil.mostrarAnio(new Date()) + "-SUCAMEC/GSSP-EC";
        return nroSolicitud;
    }

    public void handleFileUploadPDF_RP(FileUploadEvent event) {
        try {
            if (event != null) {
                fileRP = event.getFile();
                if (JsfUtil.verificarPDF(fileRP)) {
                    rpByte = IOUtils.toByteArray(fileRP.getInputstream());
                    archivoRP = new DefaultStreamedContent(fileRP.getInputstream(), "application/pdf");
                    nomArchivoRP = fileRP.getFileName();
                } else {
                    fileRP = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadPDF_LM(FileUploadEvent event) {
        try {
            if (event != null) {
                fileLM = event.getFile();
                if (JsfUtil.verificarPDF(fileLM)) {
                    lmByte = IOUtils.toByteArray(fileLM.getInputstream());
                    archivoLM = new DefaultStreamedContent(fileLM.getInputstream(), "application/pdf");
                    nomArchivoLM = fileLM.getFileName();
                } else {
                    fileLM = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadPDF_PE(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCA = event.getFile();
                if (JsfUtil.verificarPDF(fileCA)) {
                    caByte = IOUtils.toByteArray(fileCA.getInputstream());
                    archivoCA = new DefaultStreamedContent(fileCA.getInputstream(), "application/pdf");
                    nomArchivoPE = fileCA.getFileName();
                } else {
                    fileCA = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadPDF_CertFiscoMntal(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCertFiscoMntal = event.getFile();
                if (JsfUtil.verificarPDF(fileCertFiscoMntal)) {
                    certFiscoMntalByte = IOUtils.toByteArray(fileCertFiscoMntal.getInputstream());
                    archivoCertFiscoMntal = new DefaultStreamedContent(fileCertFiscoMntal.getInputstream(), "application/pdf");
                    nomArchivoCertFiscoMntal = fileCertFiscoMntal.getFileName();
                } else {
                    fileCertFiscoMntal = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadPDF_CopyContrato(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCopyContrato = event.getFile();
                if (JsfUtil.verificarPDF(fileCopyContrato)) {
                    copyContratoByte = IOUtils.toByteArray(fileCopyContrato.getInputstream());
                    archivoCopyContrato = new DefaultStreamedContent(fileCopyContrato.getInputstream(), "application/pdf");
                    nomArchivoCopyContrato = fileCopyContrato.getFileName();
                } else {
                    fileCopyContrato = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadPDF_CopyPolizaSeguro(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCopyPolizaSeguro = event.getFile();
                if (JsfUtil.verificarPDF(fileCopyPolizaSeguro)) {
                    copyPolizaSeguroByte = IOUtils.toByteArray(fileCopyPolizaSeguro.getInputstream());
                    archivoCopyPolizaSeguro = new DefaultStreamedContent(fileCopyPolizaSeguro.getInputstream(), "application/pdf");
                    nomArchivoCopyPolizaSeguro = fileCopyPolizaSeguro.getFileName();
                } else {
                    fileCopyPolizaSeguro = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadPDF_CopyPolizaSeguroVidaSepelInvalidez(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCopyPolizaSegVida = event.getFile();
                if (JsfUtil.verificarPDF(fileCopyPolizaSegVida)) {
                    copyPolizaSegVidaByte = IOUtils.toByteArray(fileCopyPolizaSegVida.getInputstream());
                    archivoCopyPolizaSegVida = new DefaultStreamedContent(fileCopyPolizaSegVida.getInputstream(), "application/pdf");
                    nomArchivoCopyPolizaSegVida = fileCopyPolizaSegVida.getFileName();
                } else {
                    fileCopyPolizaSegVida = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadPDF_CF(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCF = event.getFile();
                if (JsfUtil.verificarPDF(fileCF)) {
                    cfByte = IOUtils.toByteArray(fileCF.getInputstream());
                    archivoCF = new DefaultStreamedContent(fileCF.getInputstream(), "application/pdf");
                    nomArchivoCF = fileCF.getFileName();
                } else {
                    fileCF = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String obtenerMsjeInvalidSizeFileSelAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selAutoPrestacServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }

    public String obtenerMsjeInvalidSizeFileSelCartaFianzaAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selCartaFianzaPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }

    public String obtenerMsjeInvalidSizeFileSelLicenciaMunicipalAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selLicenciaMunicipalPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }

    public String obtenerMsjeInvalidSizeFileSelPlanEstudiosAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selPlanEstudiosCefoesp_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }

    public String obtenerMsjeInvalidSizeFileSelPlanoDistribucionAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selPlanoDistribucionPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }

    public void agregarTipoAlmacen() {
        boolean validacionAlm = true;

        if (regAlmacenLPSSelected == null) {
            validacionAlm = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Almacen");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de almacén");
        }

        if (regCantArmeriaLPS == null) {
            validacionAlm = false;
            JsfUtil.invalidar(obtenerForm() + ":txtLPE_Numero");
            JsfUtil.mensajeAdvertencia("Debe de ingresar la cantidad");
        }

        if (!(Integer.parseInt(regCantArmeriaLPS + "") > 0)) {
            validacionAlm = false;
            JsfUtil.invalidar(obtenerForm() + ":txtLPE_Numero");
            JsfUtil.mensajeAdvertencia("Debe de ingresar la cantidad");
        }

        if (validacionAlm) {

            SspAlmacen ItemAlmacen = new SspAlmacen();
            contRegAlmacen++;

            ItemAlmacen.setId(contRegAlmacen);
            ItemAlmacen.setTipoAlmacenId(regAlmacenLPSSelected);
            ItemAlmacen.setCantidad(regCantArmeriaLPS);

            lstAlmacenes.add(ItemAlmacen);

            /*
            for (SspAlmacen alm : lstAlmacenes) {
                System.out.println("\n\r"+ alm.getId()+" - "+alm.getTipoAlmacenId().getNombre()+" - "+alm.getCantidad());
            }
             */
        }

        regAlmacenLPSSelected = null;
        regCantArmeriaLPS = null;
    }

    public void deleteAlmacen(Long id) {

        for (int i = 0; i < lstAlmacenes.size(); i++) {
            if (lstAlmacenes.get(i).getId().equals(id)) {
                lstAlmacenes.remove(i);
                i--;
            }
        }

        /*
        for (SspAlmacen alm : lstAlmacenes) {
            System.out.println("\n\r"+ alm.getId()+" - "+alm.getTipoAlmacenId().getNombre()+" - "+alm.getCantidad());
        }
         */
    }

    public void agregarTipoContacto() {
        boolean validacionCont = true;

        if (regPrioridadLPSSelected == null) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la prioridad");
        }

        if (regTipoMedioContactoLPSSelected == null) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el medio de contacto");
        }

        if (regTipoMedioContactoLPSSelected != null) {
            //Valida cuando ha seleccionado el tipo de Correo Electrónico
            if (regTipoMedioContactoLPSSelected.getCodProg().equals("TP_MEDCO_COR")) {
                if (regtextoCorreoContactoLPS == null) {
                    validacionCont = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_CorreoElectronico");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el Correo Electrónico");
                } else if (regtextoCorreoContactoLPS.equals("")) {
                    validacionCont = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_CorreoElectronico");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el Correo Electrónico");
                }
            } else //Valida cuando no es Tipo de Correo Electrónico
             if (regtextoContactoLPS == null) {
                    validacionCont = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el contacto");
                } else if (regtextoContactoLPS.equals("")) {
                    validacionCont = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el contacto");
                }
        }

        int contPrincCorreo = 0;
        int contPrincTelFijo = 0;
        int contPrincTelMovil = 0;
        int contPrincFax = 0;

        if (validacionCont) {
            TipoBaseGt ccPrioridad = ejbTipoBaseFacade.selectLike(regPrioridadLPSSelected.getId().toString()).get(0);
            Long xCodPrioridadPrincipal = (ccPrioridad.getCodProg().equals("TP_CONTAC_PRIN")) ? ccPrioridad.getId() : null;

            TipoBaseGt cMedContacto = ejbTipoBaseFacade.selectLike(regTipoMedioContactoLPSSelected.getId().toString()).get(0);
            Long xIdCorreo = (cMedContacto.getCodProg().equals("TP_MEDCO_COR")) ? cMedContacto.getId() : null;
            Long xIdTelFijo = (cMedContacto.getCodProg().equals("TP_MEDCO_FIJ")) ? cMedContacto.getId() : null;
            Long xIdTelMovil = (cMedContacto.getCodProg().equals("TP_MEDCO_MOV")) ? cMedContacto.getId() : null;
            Long xIdFax = (cMedContacto.getCodProg().equals("TP_MEDCO_FAX")) ? cMedContacto.getId() : null;

            if (xCodPrioridadPrincipal != null) {

                for (SspContacto contact : lstContactos) {

                    if ((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdCorreo))) {
                        contPrincCorreo++;
                    }

                    if ((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdTelFijo))) {
                        contPrincTelFijo++;
                    }

                    if ((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdTelMovil))) {
                        contPrincTelMovil++;
                    }

                    if ((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdFax))) {
                        contPrincFax++;
                    }

                }

            }
        }

        if (contPrincCorreo > 0) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un correo principal");
        }

        if (contPrincTelFijo > 0) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un teléfono fijo principal");
        }

        if (contPrincTelMovil > 0) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un teléfono movil principal");
        }

        if (contPrincFax > 0) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un fax principal");
        }

        if (validacionCont) {
            SspContacto ItemContacto = new SspContacto();
            contRegContacto++;

            ItemContacto.setId(contRegContacto);
            ItemContacto.setRegistroId(null);
            ItemContacto.setValor(regPrioridadLPSSelected.getId() + "");
            ItemContacto.setTipoMedioId(regTipoMedioContactoLPSSelected);

            //Si es Tipo Correo Electronico
            if (regTipoMedioContactoLPSSelected.getCodProg().equals("TP_MEDCO_COR")) {
                ItemContacto.setDescripcion(regtextoCorreoContactoLPS.toUpperCase());
            } else {
                //Si es diferente al Tipo Correo Electronico
                ItemContacto.setDescripcion(regtextoContactoLPS.toUpperCase());
            }
            lstContactos.add(ItemContacto);
        }

        regPrioridadLPSSelected = null;
        regTipoMedioContactoLPSSelected = null;
        regtextoContactoLPS = null;
    }

    public void deleteContacto(Long id) {

        for (int i = 0; i < lstContactos.size(); i++) {
            if (lstContactos.get(i).getId().equals(id)) {
                //Agrega en la lista si el Contacto ya existia con un ID real
                if (lstContactos.get(i).getId() > 0) {
                    listaUpdateContactosActivos.add(lstContactos.get(i));
                }
                lstContactos.remove(i);
                i--;
            }
        }
    }

    public void actualizarDatosCargo() {
        regCargoSocio = null;
    }

    public String MostrarDescripcionTipoSocio(Long IdTipoSocio) {
        TipoBaseGt xTipoSocio = new TipoBaseGt();
        xTipoSocio = ejbTipoBaseFacade.find(IdTipoSocio);
        return xTipoSocio.getNombre();
    }

    public StreamedContent descagarArchivoSocio(SspParticipante item) {
        try {
            if (item == null) {
                return null;
            }
            verificaExtension(item.getNombreArchivo());
            return JsfUtil.obtenerArchivoPath(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_SOC").getValor(), item.getNombreArchivo(), item.getNombreArchivo(), tipoArchivo);
        } catch (Exception e) {
            JsfUtil.mensajeError("Hubo un error al descargar el archivo");
        }
        return null;
    }

    public void verificaExtension(String item) {

        if (item.trim().toUpperCase().contains(".PDF")) {
            extension = "PDF";
            tipoArchivo = "application/pdf";
        }
        if (item.trim().toUpperCase().contains(".DOC")) {
            extension = "DOC";
            tipoArchivo = "application/msword";
        }
        if (item.trim().toUpperCase().contains(".XLS")) {
            extension = "XLS";
            tipoArchivo = "application/vnd.ms-excel";
        }
    }

    public void deleteSocio(Long id) {

        for (int i = 0; i < lstSocios.size(); i++) {
            if (lstSocios.get(i).getId().equals(id)) {
                if (lstSocios.get(i).getId() > 0) {
                    listaUpdateParticipanteActivos.add(lstSocios.get(i));
                }
                lstSocios.remove(i);
                i--;
            }
        }

        /*for (SspParticipante socio : lstSocios) {
            System.out.println("\n\r"+ socio.getId()+" - "+socio.getPersonaId()+" - "+socio.getTipoParticipacion()+" - "+socio.getCargo());
        }*/
    }

    public void deleteCapacitador(Long id) {

        for (int i = 0; i < lstCapacitadores.size(); i++) {
            if (lstCapacitadores.get(i).getId().equals(id)) {
                if (lstCapacitadores.get(i).getId() > 0) {
                    //listaUpdateParticipanteActivos.add(lstCapacitadores.get(i));
                }
                lstCapacitadores.remove(i);
                i--;
            }
        }
    }

    public void selectedOptionsChanged() {

        lstCheckedForma = new ArrayList<SspForma>();
        contRegCheckForma = 0L;
        int contOtro = 0;

        if (selectedOptions != null) {

            for (int i = 0; i < selectedOptions.length; i++) {

                contRegCheckForma++;
                SspForma ItemFormaServTecSeg = new SspForma();

                TipoSeguridad tipoFormaId = new TipoSeguridad();

                String codProgItem = selectedOptions[i];
                tipoFormaId = ejbTipoSeguridadFacade.tipoSeguridadXCodProg(codProgItem);

                ItemFormaServTecSeg.setId(contRegCheckForma);
                ItemFormaServTecSeg.setTipoFormaId(regTipoProcesoId);
                ItemFormaServTecSeg.setDescripcion((tipoFormaId.getCodProg().equals("TP_MCO_TEC_OTRO")) ? selectOptionOtroVal : null);

                if (codProgItem.equals("TP_MCO_TEC_OTRO")) {
                    contOtro++;
                }

                lstCheckedForma.add(ItemFormaServTecSeg);
            }

        }

        if (contOtro == 0) {
            selectOptionOtroVal = null;
        }

        /*
        for (SspForma formaServTecSeg : lstCheckedForma) {
            System.out.println("\n\r"+ formaServTecSeg.getId()+" - "+formaServTecSeg.getTipoFormaId().getNombre()+" - "+ formaServTecSeg.getTipoFormaId().getCodProg()+" - "+formaServTecSeg.getDescripcion());
        }
         */
    }

    public void validarComprobante() {
        boolean validacionComprobante = true;

        if (regTipoSeguridadSelected == null) {
            validacionComprobante = false;
            JsfUtil.invalidar(obtenerForm() + ":bcoTipServicio");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de servicio");
        }

        if (regServicioPrestadoSelected == null) {
            validacionComprobante = false;
            JsfUtil.invalidar(obtenerForm() + ":cboServPrestado");
            JsfUtil.mensajeAdvertencia("Debe de seleccioanr el servicio prestado");
        }

        if (regNroComprobante == null) {
            validacionComprobante = false;
            JsfUtil.invalidar(obtenerForm() + ":txtNroComprobante");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el numero del voucher de pago");
        }

        if (regNroComprobante != null) {
            if (regNroComprobante.equals("")) {
                validacionComprobante = false;
                JsfUtil.invalidar(obtenerForm() + ":txtNroComprobante");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el numero del voucher de pago");
            }
        }

        if (validacionComprobante) {

            String codAtributo = JsfUtil.bundleBDIntegrado("Codigo_Tributo_Vigilancia_Privada");
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt xAdministrado = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

            HashMap mMap = new HashMap();
            mMap.put("codigo", codAtributo);
            mMap.put("nroSecuencia", regNroComprobante);

            if (tipoAdministradoUsuario.equals("PersonaJuridica")) {
                mMap.put("ruc", xAdministrado.getRuc());
            } else {
                mMap.put("ruc", xAdministrado.getNumDoc());
            }

            regListDatoRecibos = ejbSbRecibosFacade.validaRecibosForSolicitudAutorizacion(mMap);

            if (regListDatoRecibos.size() > 0) {
                regDatoRecibos = regListDatoRecibos.get(0);
                //Verifica si el Voucher de Pago ya ha sido utilizado
                if (validarVoucherPagoUtilizado()) {
                    JsfUtil.mensajeAdvertencia("El voucher de pago número " + regDatoRecibos.getNroSecuencia() + " ya ha sido utilizado");
                    regDatoRecibos = null;
                }
            } else {
                regDatoRecibos = null;
                JsfUtil.mensajeAdvertencia("El número de comprobante no se encuentra registrado en nuestra base de datos");
            }

        }

    }

    public boolean validarVoucherPagoUtilizado() {
        boolean resp = false;
        List<SspRegistro> lstRegistroVoucher = new ArrayList<>();
        lstRegistroVoucher = ejbSspRegistroFacade.listarRegistroVoucherPago(regDatoRecibos.getId());
        if (lstRegistroVoucher.isEmpty()) {
            resp = false;
        } else {
            resp = true;
        }
        return resp;
    }

    public String formatoFechaToDDMMYY(Date Fecha) {
        String nuevaFecha = "";

        return nuevaFecha;
    }

    public void openDlgPersonaRepresentanteLegal() {

        //Setea el Titulo del Modal de Representante Legal y la busqueda para retornar los valores
        //Se utiliza el mismo modal para Representante Legal y Accionista/Socio
        buscaNuevoModalRepresentanteLegal = true;
        buscaNuevoModalAccionista = false;
        //Titulo como Representante Legal o Responsable Legal
        tituloBuscaModalRepresentanteLegal = JsfUtil.bundleBDIntegrado("gsspSolicitudAutorizacion_Titulo_Modal_Representante_Legal");

        regTipoDocSelected = null;
        regNroDocBuscar = null;
        regDatoPersonaByNumDocRL = null;
        regRepresentanteFechaNacimiento = null;
        regDisabledRepresentanteFechaNacimiento = true;

        RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').show()");
        RequestContext.getCurrentInstance().update("frmCuerpoPersona");
        RequestContext.getCurrentInstance().update("dlgRepresentanteLegal");
    }

    public void openDlgPersonaAccionistaSocio() {
        //Setea el Titulo del Modal de Accionista/Socio y la busqueda para retornar los valores
        //Se utiliza el mismo modal para Representante Legal y Accionista/Socio
        buscaNuevoModalRepresentanteLegal = false;
        buscaNuevoModalAccionista = true;
        tituloBuscaModalRepresentanteLegal = JsfUtil.bundleBDIntegrado("gsspSolicitudAutorizacion_Titulo_Modal_Accionista_Socio");

        regTipoDocSelected = null;
        regNroDocBuscar = null;
        regDatoPersonaByNumDocRL = null;
        regRepresentanteFechaNacimiento = null;
        regDisabledRepresentanteFechaNacimiento = true;
        RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').show()");
        RequestContext.getCurrentInstance().update("frmCuerpoPersona");
        RequestContext.getCurrentInstance().update("dlgRepresentanteLegal");

    }

    public void openDlgMapa(String tipo_modal) {

//          System.out.println("tipo_modla:"+ tipo_modal);  
        regFormModalMapa_tipoModal = tipo_modal;

        if (regTipoProcesoId == null) {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de servicio");
            return;
        }

        if (tipo_modal.equals("poligono_modal")) {
            if (regDistritoLPSSelected2 == null) {
                JsfUtil.mensajeAdvertencia("Debe seleccionar el ambito de operaciones del polígono");
                return;
            }
        } else if (regDistritoLPSSelected == null) {
            JsfUtil.mensajeAdvertencia("Debe seleccionar el ambito de operaciones");
            return;
        }
        //regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        regTipoViasLPSSelected_Form = null;

        //regTipLocalLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");        
        //regTipLocalLPSSelected_Form = null;
        regTipUsoLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
        regTipUsoLPSSelected_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);

        regDireccionLPS_Form = null;
        regNroFisicoLPS_Form = null;
        regReferenciaLPS_Form = null;
        regGeoLatitudLPS_Form = null;
        regGeoLongitudPS_Form = null;

        String nombre_dis = "";
        String nombre_dep = "";
        long iddist = 1L;
        iddist = (tipo_modal.equals("poligono_modal")) ? regDistritoLPSSelected2.getId() : regDistritoLPSSelected.getId();//10101;
        //listaDistritoMapaLocal = ejbSbDistritoFacadeGtFacade.lstDistritosxID(iddist);

        SbDistritoGt distrito_x = ejbSbDistritoFacadeGtFacade.find(iddist);

        nombre_dis = distrito_x.getNombre();//#{item.provinciaId.departamentoId.nombre} / #{item.provinciaId.nombre} / #{item.nombre};
        nombre_dep = distrito_x.getProvinciaId().getDepartamentoId().getNombre();

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvDlgMapa').show()");
        context.update("frmCuerpoMapa");
//            context.execute("irMapa(["+d_lat+","+d_lon+"], 13);");
        String concat1 = "'" + nombre_dep + "', '" + nombre_dis + "'";
        context.execute("irMapa()");
        context.execute("consumirApi(" + concat1 + ")");

//        }        
    }

    public void onPointSelect(PointSelectEvent event) {
        LatLng latlng = event.getLatLng();

        regGeoLatitudLPS_Form = latlng.getLat() + "";
        regGeoLongitudPS_Form = latlng.getLng() + "";

        RequestContext.getCurrentInstance().update("txtLatitud");
        RequestContext.getCurrentInstance().update("txtLongitud");

        emptyModel = new DefaultMapModel();
        Marker marker = new Marker(new LatLng(latlng.getLat(), latlng.getLng()), "Ubicación del local");
        emptyModel.addOverlay(marker);
        RequestContext.getCurrentInstance().update("gmap");
    }

    public void onStateChange(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();

        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));
    }

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void btnAgregarLocalAutorizacionMapa() {
        boolean validacionRegLocal = true;

        if (regTipoViasLPSSelected_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de ubicación");
        }

        if (regDireccionLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de iIngresar el domicilio legal");
        } else if (regDireccionLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese domicilio legal");
        }

        if (regNroFisicoLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese nro fisico");
        } else if (regNroFisicoLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese nro fisico");
        }

        if (regReferenciaLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese referencia");
        } else if (regReferenciaLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese referencia");
        }

        if (regTipUsoLPSSelected_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de uso");
        }

        if (regGeoLatitudLPS_Form == null || regGeoLatitudLPS_Form == "" || regGeoLatitudLPS_Form.isEmpty()
                || regGeoLongitudPS_Form == null || regGeoLongitudPS_Form == "" || regGeoLongitudPS_Form.isEmpty()) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la ubicación de coordenadas en el mapa");
        }

        if (regTipoProcesoId == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de seguridad");
        }

        if (validacionRegLocal) {

            Long distrito_id = 0L;

            if (regFormModalMapa_tipoModal.equals("poligono_modal")) {
                distrito_id = regDistritoLPSSelected2.getId();
            } else {
                distrito_id = regDistritoLPSSelected.getId();
            }

            int totLocalExiste = ejbSspLocalAutorizacionFacade.existeLocalAutorizacionDistritoEmpresaCefoesp(regAdminist.getId(), distrito_id, regTipoSeguridadSelected.getId(), regTipoViasLPSSelected_Form.getId(), regDireccionLPS_Form.toUpperCase());

            if (totLocalExiste > 0) {
                JsfUtil.mensajeAdvertencia("Los datos de este local ya se encuentra registrado");
                return;
            } else {

                try {

                    LocalAutorizacionDetalle detalleLocalTemp = new LocalAutorizacionDetalle();
                    detalleLocalTemp.setId(JsfUtil.tempIdN());
                    detalleLocalTemp.setTipoUbicacionId(regTipoViasLPSSelected_Form);
                    detalleLocalTemp.setDireccion(regDireccionLPS_Form.toUpperCase());
                    detalleLocalTemp.setNroFisico(regNroFisicoLPS_Form.toUpperCase());
                    if (regFormModalMapa_tipoModal.equals("poligono_modal")) {
                        detalleLocalTemp.setDistritoId(regDistritoLPSSelected2);
                    } else {
                        detalleLocalTemp.setDistritoId(regDistritoLPSSelected);
                    }

                    detalleLocalTemp.setReferencia(regReferenciaLPS_Form.toUpperCase());
                    detalleLocalTemp.setTipoLocalId(regTipLocalLPSSelected_Form);
                    detalleLocalTemp.setTipoUsoId(regTipUsoLPSSelected_Form);
                    detalleLocalTemp.setGeoLatitud(regGeoLatitudLPS_Form);
                    detalleLocalTemp.setGeoLongitud(regGeoLongitudPS_Form);

                    //Agrega a la lista en forma temporal
                    if (regFormModalMapa_tipoModal.equals("poligono_modal")) {
                        localAutorizacionListado2.add(detalleLocalTemp);
                        localAutorizacionSelectedString2 = detalleLocalTemp.getId().toString();
                        mostrarDatosLocalSeleccionado2();
                    } else {
                        localAutorizacionListado.add(detalleLocalTemp);
                        localAutorizacionSelectedString = detalleLocalTemp.getId().toString();
                        mostrarDatosLocalSeleccionado();
                    }

//                    localAutorizacionSelectedString = detalleLocalTemp.getId().toString();
                    RequestContext.getCurrentInstance().execute("PF('wvDlgMapa').hide()");
                    if (regFormModalMapa_tipoModal.equals("poligono_modal")) {
                        RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_AdministradoPoligonoPJ_Title");
                    } else {
                        RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_LocalPresEstadoTU");
                    }

                    RequestContext.getCurrentInstance().execute("updateController = true;");
                } catch (Exception e) {
                    System.out.println("Exception->" + e.getMessage());
                }

            }

        }

    }

    public void btnAgregarLocalAutorizacionMapaSCDL() {
        boolean validacionRegLocal = true;

        if (regTipoViasLPSSelected_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de ubicación");
        }

        if (regDireccionLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de ingresar el domicilio legal");
        } else if (regDireccionLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese domicilio legal");
        }

        if (regNroFisicoLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese nro fisico");
        } else if (regNroFisicoLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese nro fisico");
        }

        if (regReferenciaLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese referencia");
        } else if (regReferenciaLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese referencia");
        }

        if (regTipUsoLPSSelected_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de uso");
        }

        if (regGeoLatitudLPS_Form == null || regGeoLongitudPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la ubicacion de coordenadas en el mapa");
        }

        if (validacionRegLocal) {

            int totRegistro = ejbSspLocalAutorizacionFacade.existeLocalRegistrado_x_Campos(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipoViasLPSSelected_Form.getId(), regDireccionLPS_Form, regNroFisicoLPS_Form);

            if (totRegistro > 0) {
                JsfUtil.mensajeAdvertencia("Los datos de este local ya se encuentra registrado");
                return;
            } else {

                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

                registroN2 = (SspRegistro) JsfUtil.entidadMayusculas(registroN2, "");
                registroN2.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR")); //TP_REGIST_NOR / TP_REGIST_MOD
                registroN2.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));    //TP_OPE_INI / TP_OPE_REN / TP_OPE_AMP
                registroN2.setTipoProId(registro.getTipoProId());
                registroN2.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_CDL")); //CAMBIO DOMICILIO LEGAL        
                registroN2.setEmpresaId(regAdminist);
                registroN2.setCartaFianzaId(null);
                registroN2.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                registroN2.setRegistroId(registro);
                registroN2.setNroSolicitiud("*");
                registroN2.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registroN2.setFecha(new Date());
                registroN2.setActivo(JsfUtil.TRUE);
                registroN2.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registroN2.setAudNumIp(JsfUtil.getIpAddress());
                registroN2.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));

                if (registroN2.getId() != null) {
                    ejbSspRegistroFacade.edit(registroN2);
                } else {
                    ejbSspRegistroFacade.create(registroN2);
                }

                localAutorizacionN2 = (SspLocalAutorizacion) JsfUtil.entidadMayusculas(localAutorizacionN2, "");
                localAutorizacionN2.setId(null);
                localAutorizacionN2.setTipoUbicacionId(regTipoViasLPSSelected_Form);
                localAutorizacionN2.setDireccion(regDireccionLPS_Form);
                localAutorizacionN2.setNroFisico(regNroFisicoLPS_Form);
                localAutorizacionN2.setDistritoId(regDistritoLPSSelected);
                localAutorizacionN2.setReferencia(regReferenciaLPS_Form);
                localAutorizacionN2.setGeoLatitud(regGeoLatitudLPS_Form);
                localAutorizacionN2.setGeoLongitud(regGeoLongitudPS_Form);
                localAutorizacionN2.setFecha(new Date());
                localAutorizacionN2.setActivo(JsfUtil.TRUE);
                localAutorizacionN2.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                localAutorizacionN2.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspLocalAutorizacionFacade.create(localAutorizacionN2);

                tipoUsoLocal = (SspTipoUsoLocal) JsfUtil.entidadMayusculas(tipoUsoLocal, "");
                tipoUsoLocal.setId(null);
                tipoUsoLocal.setLocalId(localAutorizacionN2);
                tipoUsoLocal.setRegistroId(registroN2);
                tipoUsoLocal.setTipoLocalId(regTipLocalLPSSelected_Form);
                tipoUsoLocal.setTipoUsoId(regTipUsoLPSSelected_Form);
                tipoUsoLocal.setFecha(new Date());
                tipoUsoLocal.setActivo(JsfUtil.TRUE);
                tipoUsoLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                tipoUsoLocal.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspTipoUsoLocalFacade.create(tipoUsoLocal);

                localRegistro = (SspLocalRegistro) JsfUtil.entidadMayusculas(localRegistro, "");
                localRegistro.setId(null);
                localRegistro.setRegistroId(registroN2);
                localRegistro.setLocalId(localAutorizacionN2);
                localRegistro.setFecha(new Date());
                localRegistro.setActivo(JsfUtil.TRUE);
                localRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                localRegistro.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspLocalRegistroFacade.create(localRegistro);

                listarLocalesAutorizXDistrito();
                RequestContext.getCurrentInstance().execute("PF('dlgMapaSCDL').hide()");
                RequestContext.getCurrentInstance().update("FormRegCambDomiciLegal");

            }

        }

    }

    public void handleFileUploadFoto(FileUploadEvent event) {
        try {
            if (event != null) {
                fileFOTO = event.getFile();
                if (JsfUtil.verificarJPG(fileFOTO)) {
                    fotoByte = IOUtils.toByteArray(fileFOTO.getInputstream());
                    fotoByte = JsfUtil.escalarRotarImagen(fotoByte, 480, 640, 0);
                    archivoFOTO = new DefaultStreamedContent(fileFOTO.getInputstream(), "image/jpeg");
                    nomArchivoFOTO = fileFOTO.getFileName();
                } else {
                    fileFOTO = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String obtenerMsjeInvalidSizeFoto() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_sizeFoto").getValor()) / 1024) + " Kb. ";
    }

    public void BuscarPersonaRepresentanteLegal() {
        boolean validacionPersonaRL = true;

        if (regTipoDocSelected == null) {
            validacionPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo documento de la persona");
            return;
        }

        if (regNroDocBuscar == null) {
            validacionPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el numero de documento de la persona");
            return;
        }

        if (regNroDocBuscar != null) {
            if (regNroDocBuscar.equals("")) {
                validacionPersonaRL = false;
                JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el numero de documento de la persona");
                return;
            }
        }

        if (regTipoDocSelected != null) {
            //Valida el DNI
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {
                if (regNroDocBuscar.length() != Integer.parseInt(cantNumerosDniCarnetTipoDocRepresentanteLegal)) {
                    validacionPersonaRL = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                }
            }

            //Valida el Carnet de Extranjeria
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                if (regNroDocBuscar.length() < cantNumerosMinimaCarnetTipoDocRepresentanteLegal) {
                    validacionPersonaRL = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar mínimo " + cantNumerosMinimaCarnetTipoDocRepresentanteLegal + " dígitos, en el número de documento para Carnet de Extranjeria");
                }
                if (regNroDocBuscar.length() > Integer.parseInt(cantNumerosDniCarnetTipoDocRepresentanteLegal)) {
                    validacionPersonaRL = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar máximo " + cantNumerosDniCarnetTipoDocRepresentanteLegal + " dígitos, en el número de documento para Carnet de Extranjeria");
                }
            }
        }

        if (validacionPersonaRL) {

            Consulta_Service serR = new Consulta_Service();
            PideMigraciones_Service serMigra = new PideMigraciones_Service();

            Consulta port = serR.getConsultaPort();
            PideMigraciones portMigra = serMigra.getPideMigracionesPort();

            wspide.Persona pn = null;
            wspide.ResPideMigra pext = null;

            try {

                regDatoPersonaByNumDocRL = ejbSbPersonaFacade.buscarPersonaSel(regTipoDocSelected.getId().toString(), regNroDocBuscar);
                if (regDatoPersonaByNumDocRL == null) {

                    if ("TP_DOCID_DNI".equals(regTipoDocSelected.getCodProg())) {

                        pn = port.consultaDNI(regNroDocBuscar);
                        if (pn != null) {
                            String xApePat = pn.getAPPAT();
                            String xApeMat = pn.getAPMAT();
                            String xNombre = pn.getNOMBRES();

                            if (pn.getFENAC() != null) {
                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                    regRepresentanteFechaNacimiento = formatter.parse(pn.getFENAC());
                                } catch (Exception e) {
                                }
                            } else {
                                regRepresentanteFechaNacimiento = null;
                            }

                            regDatoPersonaByNumDocRL = new SbPersonaGt();
                            regDatoPersonaByNumDocRL.setId(null);
                            regDatoPersonaByNumDocRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER"));
                            regDatoPersonaByNumDocRL.setTipoDoc(regTipoDocSelected);
                            regDatoPersonaByNumDocRL.setNumDoc(regNroDocBuscar);
                            regDatoPersonaByNumDocRL.setApePat(xApePat);
                            regDatoPersonaByNumDocRL.setApeMat(xApeMat);
                            regDatoPersonaByNumDocRL.setNombres(xNombre);
                            if (regRepresentanteFechaNacimiento != null) {
                                regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                            };
                            regDatoPersonaByNumDocRL.setFechaReg(new Date());
                            regDatoPersonaByNumDocRL.setActivo(JsfUtil.TRUE);
                            regDatoPersonaByNumDocRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            regDatoPersonaByNumDocRL.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSbPersonaFacade.create(regDatoPersonaByNumDocRL);

                        } else {
                            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeServicioConsultaReniecNulo"));
                        }

                    } else if ("TP_DOCID_CE".equals(regTipoDocSelected.getCodProg())) {
                        pext = portMigra.consultarDocExt(JsfUtil.getLoggedUser().getLogin(), regNroDocBuscar, regTipoDocSelected.getAbreviatura());
                        if (pext != null) {
                            if (pext.isRspta()) {

                                if (pext.getMensaje().contains("0000")) {

                                    regDatoPersonaByNumDocRL = new SbPersonaGt();
                                    regDatoPersonaByNumDocRL.setId(null);
                                    regDatoPersonaByNumDocRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER"));
                                    regDatoPersonaByNumDocRL.setTipoDoc(regTipoDocSelected);
                                    regDatoPersonaByNumDocRL.setNumDoc(regNroDocBuscar);
                                    regDatoPersonaByNumDocRL.setApePat(pext.getResultado().getStrPrimerApellido());
                                    regDatoPersonaByNumDocRL.setApeMat(pext.getResultado().getStrSegundoApellido());
                                    regDatoPersonaByNumDocRL.setNombres(pext.getResultado().getStrNombres());
                                    if (regRepresentanteFechaNacimiento != null) {
                                        regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                                    };
                                    regDatoPersonaByNumDocRL.setFechaReg(new Date());
                                    regDatoPersonaByNumDocRL.setActivo(JsfUtil.TRUE);
                                    regDatoPersonaByNumDocRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                    regDatoPersonaByNumDocRL.setAudNumIp(JsfUtil.getIpAddress());
                                    ejbSbPersonaFacade.create(regDatoPersonaByNumDocRL);

                                } else {
                                    JsfUtil.mensajeAdvertencia("No existen datos con el número de Carnet de Extranjería ingresado o no se encuentra vigente");
                                }

                            } else {
                                String msj = "";
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
                                JsfUtil.mensajeAdvertencia(msj);
                            }
                        }
                    }
                } else {
                    //Cuando lo Encuentra en el base de datos
                    //Cuando lo crea lo inactiva la fecha
                    regRepresentanteFechaNacimiento = null;
                    //Setea la Fecha de Nacimiento de la busqueda
                    if (regDatoPersonaByNumDocRL.getFechaNac() != null) {
                        regRepresentanteFechaNacimiento = regDatoPersonaByNumDocRL.getFechaNac();
                    };
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.mensajeError(ex, JsfUtil.bundle("ErrorDePersistencia"));
            }

        }
    }

    public String regresarFormCrearSolicAutorizacion() {

        regDatoPersonaByNumDocRL = new SbPersonaGt();
        regDatoPersonaByNumDocRL = null;

        regTipoDocSelected = null;
        regNroDocBuscar = null;

        return "/aplicacion/gssp/gsspSolicitudAutorizacion/Create";

    }

    public void btnRegistrarPersonaRL() {
        boolean validarRegistroPersonaRL = true;

        if (regTipoDocSelected == null) {
            validarRegistroPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            JsfUtil.mensajeAdvertencia("Seleccione un tipo de documento");
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            return;
        }

        if (StringUtils.isEmpty(regNroDocBuscar)) {
            validarRegistroPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el nro de documento");
            RequestContext.getCurrentInstance().update("frmCuerpoPersona:txtPersonaNumDoc");
            return;
        }

        if (regDatoPersonaByNumDocRL == null) {
            validarRegistroPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar los datos de la persona");
        }

        //Si es Carnet de Extranjeria valida la Fecha de Nacimiento que tenga
        if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
            if (regRepresentanteFechaNacimiento == null) {
                validarRegistroPersonaRL = false;
                JsfUtil.invalidar("frmCuerpoPersona:txt_Representante_FechaNac");
                JsfUtil.mensajeAdvertencia("Debe de ingresar la Fecha de Nacimiento");
            }

            //Fecha de Nacimiento no debe ser mayor a la de hoy dia
            if ((regRepresentanteFechaNacimiento != null)) {
                if (JsfUtil.getFechaSinHora(regRepresentanteFechaNacimiento).compareTo(JsfUtil.getFechaSinHora(fechaMayorDeEdadCalendario)) > 0) {
                    validarRegistroPersonaRL = false;
                    JsfUtil.invalidar("frmCuerpoPersona:txt_Representante_FechaNac");
                    JsfUtil.mensajeAdvertencia("La fecha de Nacimiento debe ser de una persona mayor de Edad");
                }
            }

            //Fecha de Nacimiento no debe ser mayor a la de hoy dia
            /*if ((regRepresentanteFechaNacimiento != null)) {
                if(JsfUtil.getFechaSinHora(regRepresentanteFechaNacimiento).compareTo(JsfUtil.getFechaSinHora(getFechaHoy())) >0 ){                    
                    validarRegistroPersonaRL = false;
                    JsfUtil.invalidar("frmCuerpoPersona:txt_Representante_FechaNac");
                    JsfUtil.mensajeAdvertencia("La fecha de Nacimiento no puede ser mayor que la fecha Actual");
                }
            }*/
        }

        //Si se ha llamado el Modal por Registro/Busqueda de Representante Legal
        if (buscaNuevoModalRepresentanteLegal) {
            if (validarRegistroPersonaRL) {

                TipoBaseGt sTipoBase = new TipoBaseGt();

                //Caso contrario es Representante Legal
                sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL

                //Valida si existe en la lista de Representantes Legal o Rsponsables Legal
                boolean encontroPersonaLegal = false;
                //Si tiene datos en la lista busca si ya existe
                if (personaDetalleListado != null) {
                    for (PersonaDetalle personaBusca : personaDetalleListado) {
                        if (personaBusca.getNumDoc().equals(regDatoPersonaByNumDocRL.getNumDoc())) {
                            encontroPersonaLegal = true;
                            break;
                        }
                    }
                }

                if (encontroPersonaLegal) {
                    JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Representante Legal, con este Número de documento: " + regNroDocBuscar);
                    return;
                }

                List<SbPersonaGt> xRLPersona = new ArrayList<SbPersonaGt>();
                xRLPersona = ejbSbPersonaFacade.listarRelacionPersonaXTipoId_idPersOri_idPersDest(sTipoBase.getId(), regAdminist.getId(), regDatoPersonaByNumDocRL.getId());

                if (xRLPersona.size() > 0) {
                    validarRegistroPersonaRL = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                    //Si es Sucursal es Responsable Legal
                    JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Representante Legal, con este Número de documento: " + regNroDocBuscar);
                    return;
                } else {

                    PersonaDetalle detallePersonaTemp = new PersonaDetalle();
                    detallePersonaTemp = new PersonaDetalle();
                    //detallePersonaTemp.setId(JsfUtil.tempIdN());        
                    detallePersonaTemp.setId(regDatoPersonaByNumDocRL.getId());
                    detallePersonaTemp.setNumDoc(regDatoPersonaByNumDocRL.getNumDoc());
                    detallePersonaTemp.setApePat(regDatoPersonaByNumDocRL.getApePat());
                    detallePersonaTemp.setApeMat(regDatoPersonaByNumDocRL.getApeMat());
                    detallePersonaTemp.setNombres(regDatoPersonaByNumDocRL.getNombres());
                    //Agrega a la lista en forma temporal, ya se creo en Persona, no en relacionPersona
                    personaDetalleListado.add(detallePersonaTemp);

                    /*registroSbRelacionPersonaGt.setId(null);
                    registroSbRelacionPersonaGt.setTipoId(sTipoBase);
                    registroSbRelacionPersonaGt.setPersonaOriId(regAdminist);
                    registroSbRelacionPersonaGt.setPersonaDestId(regDatoPersonaByNumDocRL);
                    registroSbRelacionPersonaGt.setFecha(new Date());
                    registroSbRelacionPersonaGt.setObservacion("Se Registro desde Solicitud de Autorizacion");
                    registroSbRelacionPersonaGt.setActivo(JsfUtil.TRUE);
                    registroSbRelacionPersonaGt.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    registroSbRelacionPersonaGt.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSbRelacionPersonaFacadeGt.create(registroSbRelacionPersonaGt);*/
                    //regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
                    //regPersonasRLSelected = regDatoPersonaByNumDocRL;
                    personaDetalleSelectedString = detallePersonaTemp.getId().toString();
                    regTipoDocSelected = regDatoPersonaByNumDocRL.getTipoDoc();

                    //Actualiza la Fecha de Nacimiento si es Carnet de Extranjeria 
                    if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                        if (regDatoPersonaByNumDocRL != null) {
                            regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                            ejbSbPersonaFacade.edit(regDatoPersonaByNumDocRL);
                        }
                    }

                    RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
                    RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_RepreLegal");

                    mostrarDomicilioRepresentanteLegal();
                }
            }

        };

        //Si se ha llamado el Modal por Registro/Busqueda de Accionista/Socio 
        if (buscaNuevoModalAccionista) {
            if (validarRegistroPersonaRL) {
                //Actualiza la Fecha de Nacimiento si es Carnet de Extranjeria 
                if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                    if (regDatoPersonaByNumDocRL != null) {
                        regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                        ejbSbPersonaFacade.edit(regDatoPersonaByNumDocRL);
                    }
                }
                regNombreSocioEncontrado = regDatoPersonaByNumDocRL.getTipoDoc().getNombre() + " " + regDatoPersonaByNumDocRL.getNumDoc() + " - " + regDatoPersonaByNumDocRL.getApePat() + " " + regDatoPersonaByNumDocRL.getApeMat() + " " + regDatoPersonaByNumDocRL.getNombres();
                regTipDocAccionistaSelected = regDatoPersonaByNumDocRL.getTipoDoc();
                regNumeroDocAccionista = regDatoPersonaByNumDocRL.getNumDoc();
                regDatosSocioEncontrado = regDatoPersonaByNumDocRL;
                RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
                RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_Accionistas");
            }
        }
    }

    //=============================================================================================
    //=============================================================================================
    //=========================          CAMBIO DE DOMICILIO LEGAL          =======================
    //=============================================================================================
    //=============================================================================================
    public String createFormCambioDomicilioLegal() {

        iniciarValores_CambioDomicilioLegal();
        estado = EstadoCrud.CREARAUTCAMDOMLEGAL;
        termCondic_CambDomLegal = false;
        habilitarFormaTipServACDL = false;

        registro = new SspRegistro();
        registroN2 = new SspRegistro();

        servicio = new SspServicio();
        localAutorizacion = new SspLocalAutorizacion();
        tipoUsoLocal = new SspTipoUsoLocal();
        localRegistro = new SspLocalRegistro();

        localAutorizacionN2 = new SspLocalAutorizacion();

        vizacion = new SspVisacion();

        return "/aplicacion/gssp/gsspSolicitudAutorizacion/CreateAutoCambDomLegal";
    }

    public void iniciarValores_CambioDomicilioLegal() {

        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXNumDoc(p_user.getLogin());

        //=============================================
        //============  Administrado PJ ===============
        //=============================================
        estado = null;
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();

        //aaaaaaa
        //administPJAsientoReg = ejbSbAsientoPersonaFacade.find(this);
        //=============================================
        //============  Administrado PN ===============
        //=============================================
        administNombres = p_user.getNombres() + " " + p_user.getApePat() + "" + p_user.getApeMat();
        administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
        administNumDoc = regUserAdminist.getNumDoc();

        //=============================================
        //======  Datos de la  Modalidad    ===========
        //=============================================
        regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_GSSP_AUTORIZACION");
        regTipoSeguridadSelected = null;

        nroAutoBuscarACDL = null;
        regFormaTipoSeguridadSelected = null;

        regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP', 'NO_ARMA_SSP'");
        regServicioPrestadoSelected = null;

        representanteRegistro = null;

        //=============================================
        //======  Local de Prestacion Servicio    =====
        //=============================================
        emptyModelACDL = new DefaultMapModel();

        //regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        regTipoViasLPSSelected = null;

        regTipLocalLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");

        regTipUsoLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
        regTipUsoLPSSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);

        regTipoViasLPSSelected_Form = null;
        regDireccionLPS_Form = null;
        regNroFisicoLPS_Form = null;
        regReferenciaLPS_Form = null;
        regTipLocalLPSSelected_Form = null;
        regTipUsoLPSSelected_Form = null;
        regGeoLatitudLPS_Form = null;
        regGeoLongitudPS_Form = null;

        regServicioPrestadoSelected = null;

        regAlmacenLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'CAJ_FUE_ARM','ARMERIA_ARM'");
        regAlmacenLPSSelected = null;

        lstAlmacenes = new ArrayList<SspAlmacen>();

        regPrioridadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_CONTAC_PRIN','TP_CONTAC_SEC'");
        regPrioridadLPSSelected = null;

        regTipoMedioContactoLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_MEDCO_COR','TP_MEDCO_FAX','TP_MEDCO_FIJ','TP_MEDCO_MOV'");
        regTipoMedioContactoLPSSelected = null;

        regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MUNI");
        regTipMunicipalidadLPSSelected = null;

        regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MUNI");
        regTipMunicipalidadLPSSelected = null;

        regMunicLPSList = null;
        regMunicLPSSelected = null;

        regIndeterminadoLPS = false;
        regDisabledCalendar = false;

        //VIZACION
        regTipoVisacionList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_VISACION");
        regTipoVisacionSelected = null;

        regVizacNro = null;
        regVizacNroOficio = null;

        //==========================================
        registroN2 = new SspRegistro();
        localAutorizacionN2 = new SspLocalAutorizacion();
        tipoUsoLocal = new SspTipoUsoLocal();
        localRegistro = new SspLocalRegistro();
        licenciaMunicipal = new SspLicenciaMunicipal();
        representanteRegistro = new SspRepresentanteRegistro();
        registroEvento = new SspRegistroEvento();
        servicio = new SspServicio();

    }

    public void btnAceptarTerminosCondicionACDL(boolean estadoTerminosCondACDL) {

        if (estadoTerminosCondACDL == true) {
            termCondic_CambDomLegal = true;
        } else {
            termCondic_CambDomLegal = false;
        }

        RequestContext.getCurrentInstance().execute("PF('wvDlgTerminosCondicionesACDL').hide()");
    }

    public void Buscar_NumeroAutorizacion_Dialog() {
        boolean validacionBuscarNumAutorizacion = true;

        if (nroAutoBuscarACDL == null) {

            validacionBuscarNumAutorizacion = false;
            JsfUtil.invalidar(obtenerForm() + ":txtFiltroNumAutorizacion");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el número de la autorización");

        } else if (nroAutoBuscarACDL != null) {
            if (nroAutoBuscarACDL.equals("")) {
                validacionBuscarNumAutorizacion = false;
                JsfUtil.invalidar(obtenerForm() + ":txtFiltroNumAutorizacion");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el número de la autorización");
            }
        }

        if (validacionBuscarNumAutorizacion) {

            registro = ejbSspRegistroFacade.buscarRegistroByNroSolicitud(nroAutoBuscarACDL);

            if (registro != null) {

                String xFormaTipoServCodProg = registro.getTipoProId().getCodProg();
                if (xFormaTipoServCodProg.equals("TP_GSSP_FRM_SPIIPEPR") || xFormaTipoServCodProg.equals("TP_GSSP_FRM_SPIIPEPU") || xFormaTipoServCodProg.equals("TP_GSSP_FRM_TDVPEPR") || xFormaTipoServCodProg.equals("TP_GSSP_FRM_TDVPEPU")) {
                    habilitarFormaTipServACDL = true;
                    regTipoSeguridadSelected = ejbTipoBaseFacade.tipoPorCodProg("TP_GSSP_MOD_PCP");

                    regFormaTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_FRM_SPIIPEPR', 'TP_GSSP_FRM_SPIIPEPU', 'TP_GSSP_FRM_TDVPEPR', 'TP_GSSP_FRM_TDVPEPU'");
                    regFormaTipoSeguridadSelected = registro.getTipoProId();
                } else {
                    habilitarFormaTipServACDL = false;
                    regTipoSeguridadSelected = registro.getTipoProId();
                    regFormaTipoSeguridadList = null;
                    regFormaTipoSeguridadSelected = null;
                }

                servicio = (registro != null) ? ejbSspServicioFacade.buscarServicioByRegistroId(registro) : null;
                regServicioPrestadoSelected = (servicio != null) ? ejbTipoSeguridadFacade.tipoSeguridadXId(servicio.getServicioPrestadoId().getId()) : null;

            } else {
                habilitarFormaTipServACDL = false;
                regTipoSeguridadSelected = null;
                regFormaTipoSeguridadList = null;
                regFormaTipoSeguridadSelected = null;
                regServicioPrestadoSelected = null;
                nroAutoBuscarACDL = null;
                JsfUtil.mensajeAdvertencia("Número de autorización no se encuentra registrada, verifique nuevamente");
                return;
            }

            //=====================================
            //DATOS DEL REPRESENTANTE LEGAL
            //=====================================
            regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
            regPersonasRLSelected = ejbSbPersonaFacade.obtenerEmpresaId(registro.getRepresentanteId().getId());

            SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
            Long xEmpresaId = regAdminist.getId();
            xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(regPersonasRLSelected.getId(), xEmpresaId);

            if (xSbAsientoPersonaRL != null) {

                regRLPartidaRegistral = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getPartidaRegistral();
                regRLAsientoRegistral = xSbAsientoPersonaRL.getAsientoId().getNroAsiento();

                regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");

                regZonaRegSelectedRL = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getZonaRegistral());

                regOficinaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg(regZonaRegSelectedRL.getCodProg());
                regOficinaRegSelectedRL = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getOficinaRegistral());
            } else {

                regRLPartidaRegistral = null;
                regRLAsientoRegistral = null;

                regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
                regZonaRegSelectedRL = null;

                regOficinaRegListRL = null;
                regOficinaRegSelectedRL = null;

            }

            if (ejbSbDireccionFacade.listarDireccionesXPersona(registro.getRepresentanteId().getId()).size() > 0) {

                //regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");
                regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
                regTipoViasSelected = ejbSbDireccionFacade.listarDireccionesXPersona(registro.getRepresentanteId().getId()).get(0).getViaId();

                regDomicilioRLSelected = ejbSbDireccionFacade.listarDireccionesXPersona(registro.getRepresentanteId().getId()).get(0).getDireccion();

            } else {
                //regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");
                regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
                regTipoViasSelected = null;

                regDomicilioRLSelected = null;
            }

            //=====================================
            //RESOLUC ACREDITA DESIGNACION DEL REPRESENTANTE LEGAL
            //=====================================
            relacionPersonaGtRL = ejbSbRelacionPersonaFacadeGt.listRepresentantes(registro.getRepresentanteId(), regAdminist).get(0); //(regPersonasRLSelected: destino | regAdminist:origen)
            if (relacionPersonaGtRL != null) {

                SspRepresentantePublico xSspReprePublico = new SspRepresentantePublico();
                xSspReprePublico = ejbRepresentantePublicoFacade.findRepresentatePublicoXIdRepPersona(relacionPersonaGtRL.getId());

                regTipoDocEPList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOC_CON','TP_DOC_REP','TP_DOC_OFI','TP_DOC_RES','TP_DOC_CVN','TP_DOC_CAR','TP_DOC_NOTVIR','TP_DOC_OTRO'");
                regTipoDocEPSelected = xSspReprePublico.getTipoDocumentoId();

                regNombDocEP = xSspReprePublico.getNombreDocuemnto();
                regNumDocEP = xSspReprePublico.getNumeroDocuemnto();
                regFechaEmisEP = xSspReprePublico.getFechaEmision();

            } else {
                regTipoDocEPList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOC_CON','TP_DOC_REP','TP_DOC_OFI','TP_DOC_RES','TP_DOC_CVN','TP_DOC_CAR','TP_DOC_NOTVIR','TP_DOC_OTRO'");
                regTipoDocEPSelected = null;

                regNombDocEP = null;
                regNumDocEP = null;
                regFechaEmisEP = null;
            }

            //=====================================
            //LOCAL AMBITO DE OPERACIONES
            //=====================================
            if (registro != null) {

                regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");//Ambito de Operaciones
                regDistritoLPSSelected = ejbSspLocalRegistroFacade.verLocalRegistroXIdSspReg(registro.getId()).getLocalId().getDistritoId();

                //localAutorizacionListado = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionXidDistritoXIdEmpresa(regAdminist.getId(), regDistritoLPSSelected.getId());                
                localAutorizacionSelectedString = ejbSspLocalRegistroFacade.verLocalRegistroXIdSspReg(registro.getId()).getLocalId().toString();

                mostrarDatosLocalSeleccionado();

            } else {

                regDistritoLPSList = null;
                regDistritoLPSSelected = null;

                localAutorizacionListado = null;
                localAutorizacionSelectedString = null;
            }

            if (registro != null) {
                regServicioPrestadoSelected = ejbSspServicioFacade.buscarServicioByRegistroId(registro).getServicioPrestadoId();
                lstAlmacenesList = ejbSspAlmacenFacade.listarAlmacenxIdRegistro(registro.getId());
            } else {
                regServicioPrestadoSelected = null;
                lstAlmacenesList = null;
            }

            if (registro != null) {
                lstContactosList = ejbSspContactoFacade.listarContactoxIdRegistro(registro.getId());
            } else {
                lstContactosList = null;
            }

            //=====================================
            //LICENCIA MUNICIPAL
            //=====================================
            regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MUNI");
            regTipMunicipalidadLPSSelected = null;

            regMunicLPSList = null;
            regMunicLPSSelected = null;

            RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarAutorizacionACDL').hide()");
        }

    }

    public void validarComprobanteSCDL() {
        boolean validacionComprobanteSCDL = true;

        if (regNroComprobante == null) {
            validacionComprobanteSCDL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtNroComprobante");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el numero del voucher de pago");
        } else if (regNroComprobante.equals("")) {
            validacionComprobanteSCDL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtNroComprobante");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el numero del voucher de pago");
        }

        if (validacionComprobanteSCDL) {

            String codAtributo = "5371";
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt xAdministrado = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

            HashMap mMap = new HashMap();
            mMap.put("ruc", xAdministrado.getRuc());
            mMap.put("codigo", codAtributo);
            mMap.put("nroSecuencia", regNroComprobante);

            regListDatoRecibos = ejbSbRecibosFacade.validaRecibosForSolicitudAutorizacion(mMap);
            if (regListDatoRecibos.size() > 0) {
                regDatoRecibos = regListDatoRecibos.get(0);
            } else {
                regDatoRecibos = null;
                JsfUtil.mensajeAdvertencia("El número de comprobante no se encuentra registrado en nuestra base de datos");
            }

        }

    }

    public void openDlgMapaSCDL() {

        if (registro.getId() == null) {
            JsfUtil.mensajeAdvertencia("Debe de buscar una solicitud de autorización");
        } else if (regDistritoLPSSelected == null) {
            JsfUtil.mensajeAdvertencia("Seleccione ambito de operaciones");
        } else {

            //regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
            regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
            regTipoViasLPSSelected_Form = null;

            regTipLocalLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");
            regTipLocalLPSSelected_Form = null;

            regTipUsoLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
            regTipUsoLPSSelected_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);

            regDireccionLPS_Form = null;
            regNroFisicoLPS_Form = null;
            regReferenciaLPS_Form = null;
            regGeoLatitudLPS_Form = null;
            regGeoLongitudPS_Form = null;

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("frmCuerpoMapaSCDL");
            context.execute("PF('dlgMapaSCDL').show()");
        }

    }

    public void openDlgMapaACRL() {

        if (registro == null) {
            JsfUtil.mensajeAdvertencia("Debe de buscar una solicitud de autorización");
        } else if (regDistritoLPSSelected == null) {
            JsfUtil.mensajeAdvertencia("Seleccione ambito de operaciones");
        } else {

            //regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
            regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
            regTipoViasLPSSelected_Form = null;

            regTipLocalLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");
            regTipLocalLPSSelected_Form = null;

            regTipUsoLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
            regTipUsoLPSSelected_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);

            regDireccionLPS_Form = null;
            regNroFisicoLPS_Form = null;
            regReferenciaLPS_Form = null;
            regGeoLatitudLPS_Form = null;
            regGeoLongitudPS_Form = null;

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("frmCuerpoMapaACRL");
            context.execute("PF('dlgMapaACRL').show()");
        }

    }

    public void onPointSelectSCDL(PointSelectEvent event) {

        emptyModelACDL = new DefaultMapModel();

        LatLng latlng = event.getLatLng();

        regGeoLatitudLPS_Form = latlng.getLat() + "";
        regGeoLongitudPS_Form = latlng.getLng() + "";

        markerACDL = new Marker(new LatLng(latlng.getLat(), latlng.getLng()), "Punto Seleccionado");
        emptyModelACDL.addOverlay(markerACDL);

        RequestContext.getCurrentInstance().update("frmCuerpoMapaSCDL");
        RequestContext.getCurrentInstance().update("gmapSCDL");

        //JsfUtil.mensajeError("Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng());
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));
    }

    public void onStateChangeSCDL(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();

        addMessageSCDL(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));
        addMessageSCDL(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));
        addMessageSCDL(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));
        addMessageSCDL(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));
    }

    public void addMessageSCDL(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void btnAceptarCoordenadasSCDL() {
        boolean validacionCoordenadas = true;

        if (regGeoLatitudLPS_Form == null || regGeoLongitudPS_Form == null) {
            validacionCoordenadas = false;
            JsfUtil.invalidar(obtenerForm() + ":txtLatitudSCDL");
            JsfUtil.invalidar(obtenerForm() + ":txtLongitudSCDL");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la ubicacion de coordenadas en el mapa");
        }

        if (validacionCoordenadas) {
            RequestContext.getCurrentInstance().execute("PF('dlgMapaSCDL').hide()");
        }

    }

    public void agregarTipoAlmacenSCDL() {
        boolean validacionAlm = true;

        if (regAlmacenLPSSelected == null) {
            validacionAlm = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Almacen");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de almacén");
        }

        if (regCantArmeriaLPS == null) {
            validacionAlm = false;
            JsfUtil.invalidar(obtenerForm() + ":txtLPE_Numero");
            JsfUtil.mensajeAdvertencia("Debe de ingresar la cantidad");
        }

        if (!(Integer.parseInt(regCantArmeriaLPS + "") > 0)) {
            validacionAlm = false;
            JsfUtil.invalidar(obtenerForm() + ":txtLPE_Numero");
            JsfUtil.mensajeAdvertencia("Debe de ingresar la cantidad");
        }

        contRegAlmacen = Long.parseLong(lstAlmacenesList.size() + "");

        if (validacionAlm) {

            SspAlmacen ItemAlmacen = new SspAlmacen();
            contRegAlmacen++;

            ItemAlmacen.setId(contRegAlmacen);
            ItemAlmacen.setTipoAlmacenId(regAlmacenLPSSelected);
            ItemAlmacen.setCantidad(regCantArmeriaLPS);

            lstAlmacenesList.add(ItemAlmacen);
        }

        regAlmacenLPSSelected = null;
        regCantArmeriaLPS = null;
    }

    public void agregarTipoContactoSCDL() {
        boolean validacionCont = true;

        if (regPrioridadLPSSelected == null) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la prioridad");
        }

        if (regTipoMedioContactoLPSSelected == null) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el medio de contacto");
        }

        if (regtextoContactoLPS == null) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el contacto");
        }

        int contPrincCorreo = 0;
        int contPrincTelFijo = 0;
        int contPrincTelMovil = 0;
        int contPrincFax = 0;

        if (validacionCont) {
            TipoBaseGt ccPrioridad = ejbTipoBaseFacade.selectLike(regPrioridadLPSSelected.getId().toString()).get(0);
            Long xCodPrioridadPrincipal = (ccPrioridad.getCodProg().equals("TP_CONTAC_PRIN")) ? ccPrioridad.getId() : null;

            TipoBaseGt cMedContacto = ejbTipoBaseFacade.selectLike(regTipoMedioContactoLPSSelected.getId().toString()).get(0);
            Long xIdCorreo = (cMedContacto.getCodProg().equals("TP_MEDCO_COR")) ? cMedContacto.getId() : null;
            Long xIdTelFijo = (cMedContacto.getCodProg().equals("TP_MEDCO_FIJ")) ? cMedContacto.getId() : null;
            Long xIdTelMovil = (cMedContacto.getCodProg().equals("TP_MEDCO_MOV")) ? cMedContacto.getId() : null;
            Long xIdFax = (cMedContacto.getCodProg().equals("TP_MEDCO_FAX")) ? cMedContacto.getId() : null;

            if (xCodPrioridadPrincipal != null) {

                for (SspContacto contact : lstContactosList) {

                    if ((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdCorreo))) {
                        contPrincCorreo++;
                    }

                    if ((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdTelFijo))) {
                        contPrincTelFijo++;
                    }

                    if ((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdTelMovil))) {
                        contPrincTelMovil++;
                    }

                    if ((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdFax))) {
                        contPrincFax++;
                    }

                }

            }
        }

        if (contPrincCorreo > 0) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un correo principal");
        }

        if (contPrincTelFijo > 0) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un teléfono fijo principal");
        }

        if (contPrincTelMovil > 0) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un teléfono movil principal");
        }

        if (contPrincFax > 0) {
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un fax principal");
        }

        contRegContacto = Long.parseLong(lstContactosList.size() + "");
        if (validacionCont) {
            SspContacto ItemContacto = new SspContacto();
            contRegContacto++;

            ItemContacto.setId(contRegContacto);
            ItemContacto.setRegistroId(null);
            ItemContacto.setValor(regPrioridadLPSSelected.getId() + "");
            ItemContacto.setTipoMedioId(regTipoMedioContactoLPSSelected);
            ItemContacto.setDescripcion(regtextoContactoLPS);
            lstContactosList.add(ItemContacto);
        }

        regPrioridadLPSSelected = null;
        regTipoMedioContactoLPSSelected = null;
        regtextoContactoLPS = null;
    }

    public void deleteAlmacenSCDL(Long id) {

        for (int i = 0; i < lstAlmacenesList.size(); i++) {
            if (lstAlmacenesList.get(i).getId().equals(id)) {
                lstAlmacenesList.remove(i);
                i--;
            }
        }

        /*
        for (SspAlmacen alm : lstAlmacenesList) {
            System.out.println("\n\r"+ alm.getId()+" - "+alm.getTipoAlmacenId().getNombre()+" - "+alm.getCantidad());
        }
         */
    }

    public void deleteContactoSCDL(Long id) {

        for (int i = 0; i < lstContactosList.size(); i++) {
            if (lstContactosList.get(i).getId().equals(id)) {
                lstContactosList.remove(i);
                i--;
            }
        }

        /*
        for (SspContacto contact : lstContactosList) {
            System.out.println("\n\r"+ contact.getId()+" - "+contact.getValor()+" - "+contact.getTipoMedioId().getDescripcion()+" - "+contact.getDescripcion());
        }
         */
    }

    public String crearSolicitudCambioDomicilioLegal() {
        boolean validacion = true;

        try {

            if (registro == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de buscar la Autorización para Prestación de Servicios de seguridad");
            }

            if (localAutorizacionSelectedString == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el local");
            }

            if (regNroLicenciaMunicLPS == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de Licencia Municipal");
            } else if (regNroLicenciaMunicLPS.equals("")) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de ingersar el Nro. de Licencia Municipal");
            }

            if (regTipMunicipalidadLPSSelected == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de municipalidad");
            }

            if (regMunicLPSSelected == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de seleccionar la municipalidad");
            }

            if (regGiroComercialLPS == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de Licencia Municipal");
            } else if (regGiroComercialLPS.equals("")) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de Licencia Municipal");
            }

            if (nomArchivoLM == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de adjuntar el documento que sustenta la Licencia Municipal");
            } else if (nomArchivoLM.equals("")) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de adjuntar el documento que sustenta la Licencia Municipal");
            }

            if (regTipoVisacionSelected == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de vización");
            }

            if (regVizacNro == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de vización");
            } else if (regVizacNro.equals("")) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro de vización");
            }

            if (regVizacNroOficio == null) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de oficio del CYDOC");
            } else if (regVizacNroOficio.equals("")) {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de oficio del CYDOC");
            }

            if (validacion) {

                if (registroN2.getId() == null) {

                    registroN2 = (SspRegistro) JsfUtil.entidadMayusculas(registroN2, "");
                    registroN2.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR")); //TP_REGIST_NOR / TP_REGIST_MOD
                    registroN2.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));    //TP_OPE_INI / TP_OPE_REN / TP_OPE_AMP
                    registroN2.setTipoProId(registro.getTipoProId());
                    registroN2.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_CDL"));        //CAMBIO DOMICILIO LEGAL        
                    registroN2.setEmpresaId(regAdminist);
                    registroN2.setCartaFianzaId(null);
                    registroN2.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                    registroN2.setRegistroId(registro);
                    registroN2.setNroSolicitiud("*");
                    registroN2.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                    registroN2.setFecha(new Date());
                    registroN2.setActivo(JsfUtil.TRUE);
                    registroN2.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    registroN2.setAudNumIp(JsfUtil.getIpAddress());
                    registroN2.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                    ejbSspRegistroFacade.create(registroN2);

                }

                regTipoProcesoId = registro.getTipoProId();
                relacionPersonaGtRL = ejbSbRelacionPersonaFacadeGt.listRepresentantes(regPersonasRLSelected, regAdminist).get(0); //(regPersonasRLSelected: destino | regAdminist:origen)

                registroN2.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR")); //TP_REGIST_NOR / TP_REGIST_MOD
                registroN2.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));    //TP_OPE_INI / TP_OPE_REN / TP_OPE_AMP
                registroN2.setTipoProId(registro.getTipoProId());
                registroN2.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_CDL")); //CAMBIO DE DOMICILIO LEGAL
                registroN2.setEmpresaId(regAdminist);
                registroN2.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                registroN2.setRepresentanteId(relacionPersonaGtRL.getPersonaDestId());
                registroN2.setRegistroId(registro);

                xNroSolicitiud = obtenerNroSolicitudEmision();

                registroN2.setNroSolicitiud(xNroSolicitiud);
                registroN2.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registroN2.setFecha(new Date());
                registroN2.setNroExpediente(null);
                registroN2.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                ejbSspRegistroFacade.edit(registroN2);

                representanteRegistro = (SspRepresentanteRegistro) JsfUtil.entidadMayusculas(representanteRegistro, "");
                representanteRegistro.setId(null);
                representanteRegistro.setRegistroId(registroN2);
                representanteRegistro.setRepresentanteId(regPersonasRLSelected);
                representanteRegistro.setFecha(new Date());
                representanteRegistro.setActivo(JsfUtil.TRUE);
                representanteRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                representanteRegistro.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspRepresentanteRegistroFacade.create(representanteRegistro);

                //=========================================================================================
                registroEvento = (SspRegistroEvento) JsfUtil.entidadMayusculas(registroEvento, "");
                registroEvento.setId(null);
                registroEvento.setRegistroId(registroN2);
                registroEvento.setTipoEventoId(registroN2.getEstadoId());
                registroEvento.setObservacion("Crear solicitud de Autorización para el Cambio de Domicilio Legal");
                registroEvento.setUserId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                registroEvento.setFecha(new Date());
                registroEvento.setActivo(JsfUtil.TRUE);
                registroEvento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registroEvento.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspRegistroEventoFacade.create(registroEvento);

                servicio = (SspServicio) JsfUtil.entidadMayusculas(servicio, "");
                servicio.setId(null);
                servicio.setRegistroId(registroN2);
                servicio.setServicioPrestadoId(regServicioPrestadoSelected);
                servicio.setFecha(new Date());
                servicio.setActivo(JsfUtil.TRUE);
                servicio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                servicio.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspServicioFacade.create(servicio);

                localAutorizacion = ejbSspLocalAutorizacionFacade.verDatosLocalAutorizacionXiDLocal(Long.parseLong(localAutorizacionSelectedString));

                if (localRegistro.getId() == null) {
                    localRegistro = (SspLocalRegistro) JsfUtil.entidadMayusculas(localRegistro, "");
                    localRegistro.setId(null);
                    localRegistro.setRegistroId(registroN2);
                    localRegistro.setLocalId(localAutorizacion);
                    localRegistro.setFecha(new Date());
                    localRegistro.setActivo(JsfUtil.TRUE);
                    localRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    localRegistro.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSspLocalRegistroFacade.create(localRegistro);
                } else {
                    localRegistro = ejbSspLocalRegistroFacade.verLocalRegistroXId(localRegistro.getId());
                }

                if (tipoUsoLocal.getId() == null) {
                    tipoUsoLocal = (SspTipoUsoLocal) JsfUtil.entidadMayusculas(tipoUsoLocal, "");
                    tipoUsoLocal.setId(null);
                    tipoUsoLocal.setLocalId(localAutorizacion);
                    tipoUsoLocal.setRegistroId(registroN2);
                    tipoUsoLocal.setTipoLocalId(null);
                    tipoUsoLocal.setTipoUsoId(regTipUsoLPSSelected);
                    tipoUsoLocal.setFecha(new Date());
                    tipoUsoLocal.setActivo(JsfUtil.TRUE);
                    tipoUsoLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    tipoUsoLocal.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSspTipoUsoLocalFacade.create(tipoUsoLocal);
                } else {
                    tipoUsoLocal = ejbSspTipoUsoLocalFacade.verTipoUsoLocalXId(tipoUsoLocal.getId());
                }

                for (SspAlmacen itemAlmacen : lstAlmacenesList) {
                    itemAlmacen.setId(null);
                    itemAlmacen.setLocalId(localAutorizacion);
                    itemAlmacen.setFecha(new Date());
                    itemAlmacen.setActivo(JsfUtil.TRUE);
                    itemAlmacen.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    itemAlmacen.setAudNumIp(JsfUtil.getIpAddress());
                    itemAlmacen.setSspRegistroId(registroN2);
                    ejbSspAlmacenFacade.create(itemAlmacen);
                }

                for (SspContacto itemContacto : lstContactosList) {
                    itemContacto.setId(null);
                    itemContacto.setRegistroId(registroN2);
                    itemContacto.setValor(itemContacto.getValor());
                    itemContacto.setTipoMedioId(itemContacto.getTipoMedioId());
                    itemContacto.setDescripcion(itemContacto.getDescripcion());
                    itemContacto.setFecha(new Date());
                    itemContacto.setActivo(JsfUtil.TRUE);
                    itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSspContactoFacade.create(itemContacto);
                }

                String fileNameArchivoLM = "";
                fileNameArchivoLM = nomArchivoLM;
                fileNameArchivoLM = "ASSGssp_LM_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_LM").toString() + fileNameArchivoLM.substring(fileNameArchivoLM.lastIndexOf('.'), fileNameArchivoLM.length());
                fileNameArchivoLM = fileNameArchivoLM.toUpperCase();

                licenciaMunicipal = (SspLicenciaMunicipal) JsfUtil.entidadMayusculas(licenciaMunicipal, "");
                licenciaMunicipal.setId(null);
                licenciaMunicipal.setLocalId(localAutorizacion);
                licenciaMunicipal.setNumeroLicencia(regNroLicenciaMunicLPS);
                licenciaMunicipal.setFechaIni(regFechaEmisLPS);
                licenciaMunicipal.setFechaFin(regFechaVencLPS);
                licenciaMunicipal.setIndeterminado(Short.parseShort((regIndeterminadoLPS) ? "1" : "0"));
                licenciaMunicipal.setTipoMunicipalidad(regTipMunicipalidadLPSSelected);
                licenciaMunicipal.setMunicipalidadId(regMunicLPSSelected);
                licenciaMunicipal.setGiroComercial(regGiroComercialLPS);
                licenciaMunicipal.setNombreArchivo(fileNameArchivoLM);
                licenciaMunicipal.setValidacionWs(null);
                licenciaMunicipal.setEstadoWs(null);
                licenciaMunicipal.setFecha(new Date());
                licenciaMunicipal.setActivo(JsfUtil.TRUE);
                licenciaMunicipal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                licenciaMunicipal.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspLicenciaMunicipalFacade.create(licenciaMunicipal);
                FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_LM").getValor() + licenciaMunicipal.getNombreArchivo()), lmByte);

                vizacion = (SspVisacion) JsfUtil.entidadMayusculas(vizacion, "");
                vizacion.setId(null);
                vizacion.setLocalId(localAutorizacion);
                vizacion.setTipoVizacionId(regTipoVisacionSelected);
                vizacion.setNroVisacion(Integer.parseInt(regVizacNro));
                vizacion.setFechaVisacion(new Date());
                vizacion.setDetalle("-");
                vizacion.setNroOficio(regVizacNroOficio);
                vizacion.setActivo(JsfUtil.TRUE);
                vizacion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                vizacion.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspVisacionFacade.create(vizacion);

                JsfUtil.mensaje("Se registró la solciitud con Nro." + registroN2.getNroSolicitiud());
                return "/aplicacion/gssp/gsspSolicitudAutorizacion/List";

            }

        } catch (Exception e) {
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        }
        return null;
    }

    //=============================================================================================
    //=============================================================================================
    //======================          CAMBIO DE REPRESENTANTE LEGAL          ======================
    //=============================================================================================
    //=============================================================================================
    public String createFormCambioRepresentanteLegal() {

        iniciarValores_CambioRepresentanteLegal();
        estado = EstadoCrud.CREARAUTCAMRPRESLEGAL;
        termCondic_CambRepresLegal = false;
        /*
        habilitarFormaTipServACDL = false;
        
        registro = new SspRegistro();
        registroN2 = new SspRegistro();
        
        servicio = new SspServicio();
        localAutorizacion = new SspLocalAutorizacion();
        tipoUsoLocal = new SspTipoUsoLocal();
        localRegistro = new SspLocalRegistro();
        
        localAutorizacionN2 = new SspLocalAutorizacion();
        
         */

        return "/aplicacion/gssp/gsspSolicitudAutorizacion/CreateAutoCambRepresLegal";
    }

    public void iniciarValores_CambioRepresentanteLegal() {

        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXNumDoc(p_user.getLogin());

        //=============================================
        //============  Administrado PJ ===============
        //=============================================
        estado = null;
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();

        //=============================================
        //============  Administrado PN ===============
        //=============================================
        administNombres = p_user.getNombres() + " " + p_user.getApePat() + "" + p_user.getApeMat();
        administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
        administNumDoc = regUserAdminist.getNumDoc();

        //=============================================
        //======  Datos de la  Modalidad    ===========
        //=============================================
        nroAutoBuscarACRL = null;

        regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_GSSP_AUTORIZACION");
        regTipoSeguridadSelected = null;

        regFormaTipoSeguridadSelected = null;

        regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP', 'NO_ARMA_SSP'");
        regServicioPrestadoSelected = null;

        representanteRegistro = null;

        //=============================================
        //======  Local de Prestacion Servicio    =====
        //=============================================
        emptyModelACRL = new DefaultMapModel();

        //regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
        regTipoViasLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        regTipoViasLPSSelected = null;

        regTipLocalLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");

        regTipUsoLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
        regTipUsoLPSSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);

        regTipoViasLPSSelected_Form = null;
        regDireccionLPS_Form = null;
        regNroFisicoLPS_Form = null;
        regReferenciaLPS_Form = null;
        regTipLocalLPSSelected_Form = null;
        regTipUsoLPSSelected_Form = null;
        regGeoLatitudLPS_Form = null;
        regGeoLongitudPS_Form = null;

        regServicioPrestadoSelected = null;

        regAlmacenLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'CAJ_FUE_ARM','ARMERIA_ARM'");
        regAlmacenLPSSelected = null;

        lstAlmacenes = new ArrayList<SspAlmacen>();

        regPrioridadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_CONTAC_PRIN','TP_CONTAC_SEC'");
        regPrioridadLPSSelected = null;

        regTipoMedioContactoLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_MEDCO_COR','TP_MEDCO_FAX','TP_MEDCO_FIJ','TP_MEDCO_MOV'");
        regTipoMedioContactoLPSSelected = null;

        regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MUNI");
        regTipMunicipalidadLPSSelected = null;

        regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MUNI");
        regTipMunicipalidadLPSSelected = null;

        regMunicLPSList = null;
        regMunicLPSSelected = null;

        regIndeterminadoLPS = false;
        regDisabledCalendar = false;

        //==========================================
        registroN2 = new SspRegistro();
        localAutorizacionN2 = new SspLocalAutorizacion();
        tipoUsoLocal = new SspTipoUsoLocal();
        localRegistro = new SspLocalRegistro();
        licenciaMunicipal = new SspLicenciaMunicipal();
        representanteRegistro = new SspRepresentanteRegistro();
        registroEvento = new SspRegistroEvento();
        servicio = new SspServicio();
        registroSbRelacionPersonaGt = new SbRelacionPersonaGt();

    }

    public void btnAceptarTerminosCondicionACRL(boolean estadoTerminosCondACRL) {

        if (estadoTerminosCondACRL == true) {
            termCondic_CambRepresLegal = true;
        } else {
            termCondic_CambRepresLegal = false;
        }

        RequestContext.getCurrentInstance().execute("PF('wvDlgTerminosCondicionesACRL').hide()");
    }

    public void Buscar_NumeroAutorizacion_Dialog_ACRL() {

        boolean validacionBuscarNumAutorizacionACRL = true;

        if (nroAutoBuscarACRL == null) {

            validacionBuscarNumAutorizacionACRL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtFiltroNumAutorizacion");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el número de la autorización");

        } else if (nroAutoBuscarACRL != null) {
            if (nroAutoBuscarACRL.equals("")) {
                validacionBuscarNumAutorizacionACRL = false;
                JsfUtil.invalidar(obtenerForm() + ":txtFiltroNumAutorizacion");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el número de la autorización");
            }
        }

        if (validacionBuscarNumAutorizacionACRL) {

            registro = ejbSspRegistroFacade.buscarRegistroByNroSolicitud(nroAutoBuscarACRL);
            if (registro != null) {

                String xFormaTipoServCodProg = registro.getTipoProId().getCodProg();
                if (xFormaTipoServCodProg.equals("TP_GSSP_FRM_SPIIPEPR") || xFormaTipoServCodProg.equals("TP_GSSP_FRM_SPIIPEPU") || xFormaTipoServCodProg.equals("TP_GSSP_FRM_TDVPEPR") || xFormaTipoServCodProg.equals("TP_GSSP_FRM_TDVPEPU")) {
                    habilitarFormaTipServACRL = true;
                    regTipoSeguridadSelected = ejbTipoBaseFacade.tipoPorCodProg("TP_GSSP_MOD_PCP");

                    regFormaTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_FRM_SPIIPEPR', 'TP_GSSP_FRM_SPIIPEPU', 'TP_GSSP_FRM_TDVPEPR', 'TP_GSSP_FRM_TDVPEPU'");
                    regFormaTipoSeguridadSelected = registro.getTipoProId();
                } else {
                    habilitarFormaTipServACRL = false;
                    regTipoSeguridadSelected = registro.getTipoProId();
                    regFormaTipoSeguridadList = null;
                    regFormaTipoSeguridadSelected = null;
                }

                servicio = (registro != null) ? ejbSspServicioFacade.buscarServicioByRegistroId(registro) : null;
                regServicioPrestadoSelected = (servicio != null) ? ejbTipoSeguridadFacade.tipoSeguridadXId(servicio.getServicioPrestadoId().getId()) : null;

            } else {
                habilitarFormaTipServACRL = false;
                regTipoSeguridadSelected = null;
                regFormaTipoSeguridadList = null;
                regFormaTipoSeguridadSelected = null;
                regServicioPrestadoSelected = null;
                nroAutoBuscarACRL = null;

                validacionBuscarNumAutorizacionACRL = false;
                JsfUtil.mensajeAdvertencia("El número de la autorización no existe en nuestra base de datos. Verifique nuevamente el número de autorización");
                return;
            }

            //=====================================
            //DATOS DEL REPRESENTANTE LEGAL
            //=====================================
            regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
            regPersonasRLSelected = ejbSbPersonaFacade.obtenerEmpresaId(registro.getRepresentanteId().getId());

            SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
            Long xEmpresaId = regAdminist.getId();
            xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(regPersonasRLSelected.getId(), xEmpresaId);

            if (xSbAsientoPersonaRL != null) {

                regRLPartidaRegistral = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getPartidaRegistral();
                regRLAsientoRegistral = xSbAsientoPersonaRL.getAsientoId().getNroAsiento();

                regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
                regZonaRegSelectedRL = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getZonaRegistral());

                regOficinaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg(regZonaRegSelectedRL.getCodProg());
                regOficinaRegSelectedRL = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getOficinaRegistral());
            } else {

                regRLPartidaRegistral = null;
                regRLAsientoRegistral = null;

                regZonaRegListRL = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
                regZonaRegSelectedRL = null;

                regOficinaRegListRL = null;
                regOficinaRegSelectedRL = null;
            }

            if (ejbSbDireccionFacade.listarDireccionesXPersona(registro.getRepresentanteId().getId()).size() > 0) {

                //regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");
                regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
                regTipoViasSelected = ejbSbDireccionFacade.listarDireccionesXPersona(registro.getRepresentanteId().getId()).get(0).getViaId();

                regDomicilioRLSelected = ejbSbDireccionFacade.listarDireccionesXPersona(registro.getRepresentanteId().getId()).get(0).getDireccion();

            } else {
                //regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");
                regTipoViasList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
                regTipoViasSelected = null;

                regDomicilioRLSelected = null;
            }

            //=====================================
            //RESOLUC ACREDITA DESIGNACION DEL REPRESENTANTE LEGAL
            //=====================================
            relacionPersonaGtRL = ejbSbRelacionPersonaFacadeGt.listRepresentantes(registro.getRepresentanteId(), regAdminist).get(0); //(regPersonasRLSelected: destino | regAdminist:origen)
            if (relacionPersonaGtRL != null) {

                SspRepresentantePublico xSspReprePublico = new SspRepresentantePublico();
                xSspReprePublico = ejbRepresentantePublicoFacade.findRepresentatePublicoXIdRepPersona(relacionPersonaGtRL.getId());

                regTipoDocEPList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOC_CON','TP_DOC_REP','TP_DOC_OFI','TP_DOC_RES','TP_DOC_CVN','TP_DOC_CAR','TP_DOC_NOTVIR','TP_DOC_OTRO'");
                regTipoDocEPSelected = xSspReprePublico.getTipoDocumentoId();

                regNombDocEP = xSspReprePublico.getNombreDocuemnto();
                regNumDocEP = xSspReprePublico.getNumeroDocuemnto();
                regFechaEmisEP = xSspReprePublico.getFechaEmision();

            } else {
                regTipoDocEPList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOC_CON','TP_DOC_REP','TP_DOC_OFI','TP_DOC_RES','TP_DOC_CVN','TP_DOC_CAR','TP_DOC_NOTVIR','TP_DOC_OTRO'");
                regTipoDocEPSelected = null;

                regNombDocEP = null;
                regNumDocEP = null;
                regFechaEmisEP = null;
            }

            //=====================================
            //LOCAL AMBITO DE OPERACIONES
            //=====================================
            if (registro != null) {

                regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");//Ambito de Operaciones
                regDistritoLPSSelected = ejbSspLocalRegistroFacade.verLocalRegistroXIdSspReg(registro.getId()).getLocalId().getDistritoId();

                //localAutorizacionListado = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionXidDistritoXIdEmpresa(regAdminist.getId(), regDistritoLPSSelected.getId());                
                localAutorizacionSelectedString = ejbSspLocalRegistroFacade.verLocalRegistroXIdSspReg(registro.getId()).getLocalId().toString();

                mostrarDatosLocalSeleccionado();

            } else {

                regDistritoLPSList = null;
                regDistritoLPSSelected = null;

                localAutorizacionListado = null;
                localAutorizacionSelectedString = null;
            }

            if (registro != null) {
                regServicioPrestadoSelected = ejbSspServicioFacade.buscarServicioByRegistroId(registro).getServicioPrestadoId();
                lstAlmacenesList = ejbSspAlmacenFacade.listarAlmacenxIdRegistro(registro.getId());
            } else {
                regServicioPrestadoSelected = null;
                lstAlmacenesList = null;
            }

            if (registro != null) {
                lstContactosList = ejbSspContactoFacade.listarContactoxIdRegistro(registro.getId());
            } else {
                lstContactosList = null;
            }

            //=====================================
            //LICENCIA MUNICIPAL
            //=====================================
            regTipMunicipalidadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_MUNI");
            regTipMunicipalidadLPSSelected = null;

            regMunicLPSList = null;
            regMunicLPSSelected = null;
            RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarAutorizacionACRL').hide()");
        }

    }

    public void validarComprobanteACRL() {
        boolean validacionComprobanteACRL = true;

        if (regNroComprobante == null) {
            validacionComprobanteACRL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtNroComprobante");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el número del voucher de pago");
        } else if (regNroComprobante.equals("")) {
            validacionComprobanteACRL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtNroComprobante");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el número del voucher de pago");
        }

        if (validacionComprobanteACRL) {

            String codAtributo = "5371";
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt xAdministrado = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

            HashMap mMap = new HashMap();
            mMap.put("ruc", xAdministrado.getRuc());
            mMap.put("codigo", codAtributo);
            mMap.put("nroSecuencia", regNroComprobante);

            regListDatoRecibos = ejbSbRecibosFacade.validaRecibosForSolicitudAutorizacion(mMap);
            if (regListDatoRecibos.size() > 0) {
                regDatoRecibos = regListDatoRecibos.get(0);
            } else {
                regDatoRecibos = null;
                JsfUtil.mensajeAdvertencia("El número de comprobante no se encuentra registrado en nuestra base de datos");
            }

        }

    }

    public void openDlgPersonaACRL() {

        regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");
        regTipoDocSelected = null;

        RequestContext.getCurrentInstance().execute("PF('dlgPersona').show()");
        RequestContext.getCurrentInstance().update("frmCuerpoPersona");
    }

    public void btnRegistrarPersonaRL_ACRL() {
        boolean validarRegistroPersonaRL = true;

        if (regDatoPersonaByNumDocRL == null) {
            validarRegistroPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar los datos de la persona");
        }

        if (validarRegistroPersonaRL) {

            TipoBaseGt sTipoBase = new TipoBaseGt();
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL");

            List<SbPersonaGt> xRLPersona = new ArrayList<SbPersonaGt>();
            xRLPersona = ejbSbPersonaFacade.listarRelacionPersonaXTipoId_idPersOri_idPersDest(sTipoBase.getId(), regAdminist.getId(), regDatoPersonaByNumDocRL.getId());

            if (xRLPersona.size() > 0) {

                validarRegistroPersonaRL = false;
                JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                JsfUtil.mensajeAdvertencia("Esta persona ya fue registrado con este Número de documento: " + regNroDocBuscar);
                RequestContext.getCurrentInstance().update("FormRegCambRepresLegal");
                return;

            } else {

                registroSbRelacionPersonaGt.setId(null);
                registroSbRelacionPersonaGt.setTipoId(sTipoBase);
                registroSbRelacionPersonaGt.setPersonaOriId(regAdminist);
                registroSbRelacionPersonaGt.setPersonaDestId(regDatoPersonaByNumDocRL);
                registroSbRelacionPersonaGt.setFecha(new Date());
                registroSbRelacionPersonaGt.setObservacion("Se Registro desde Solicitud de Autorizacion");
                registroSbRelacionPersonaGt.setActivo(JsfUtil.TRUE);
                registroSbRelacionPersonaGt.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registroSbRelacionPersonaGt.setAudNumIp(JsfUtil.getIpAddress());
                ejbSbRelacionPersonaFacadeGt.create(registroSbRelacionPersonaGt);

                regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
                regPersonasRLSelected = regDatoPersonaByNumDocRL;

                RequestContext.getCurrentInstance().update("FormRegCambRepresLegal");
                RequestContext.getCurrentInstance().update("frmCuerpoPersona");
                RequestContext.getCurrentInstance().update("pnl_RepreLegal");
                RequestContext.getCurrentInstance().execute("PF('dlgPersona').hide()");

                mostrarDomicilioRepresentanteLegal();

            }

        }

    }

    public void regresarRegPersona() {
        //Si se ha llamado el Modal por Registro/Busqueda de Representante Legal
        if (buscaNuevoModalRepresentanteLegal) {
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
            RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_RepreLegal");
        };

        //Si se ha llamado el Modal por Registro/Busqueda de Accionista/Socio 
        if (buscaNuevoModalAccionista) {
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
            RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_Accionistas");
        }

    }

    public void regresarRegPersonaSCRL() {

        RequestContext.getCurrentInstance().update("frmCuerpoPersona");
        RequestContext.getCurrentInstance().update("pnl_RepreLegal");
        RequestContext.getCurrentInstance().execute("PF('dlgPersona').hide()");

    }

    public void onStateChangeACRL(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();

        addMessageACRL(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));
        addMessageACRL(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));
        addMessageACRL(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));
        addMessageACRL(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));
    }

    public void onPointSelectACRL(PointSelectEvent event) {

        emptyModelACRL = new DefaultMapModel();

        LatLng latlng = event.getLatLng();

        regGeoLatitudLPS_Form = latlng.getLat() + "";
        regGeoLongitudPS_Form = latlng.getLng() + "";

        markerACRL = new Marker(new LatLng(latlng.getLat(), latlng.getLng()), "Punto Seleccionado");
        emptyModelACRL.addOverlay(markerACRL);

        RequestContext.getCurrentInstance().update("frmCuerpoMapaACRL");
        RequestContext.getCurrentInstance().update("gmapACRL");

        //JsfUtil.mensajeError("Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng());
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));
    }

    public void messagesACRL(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addMessageACRL(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void btnAgregarLocalAutorizacionMapaACRL() {
        boolean validacionRegLocal = true;

        if (regTipoViasLPSSelected_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de ubicación");
        }

        if (regDireccionLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de ingresar el domicilio legal");
        } else if (regDireccionLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese domicilio legal");
        }

        if (regNroFisicoLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese nro fisico");
        } else if (regNroFisicoLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese nro fisico");
        }

        if (regReferenciaLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese referencia");
        } else if (regReferenciaLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese referencia");
        }

        if (regTipLocalLPSSelected_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de local");
        }

        if (regTipUsoLPSSelected_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccioanr el tipo de uso");
        }

        if (regGeoLatitudLPS_Form == null || regGeoLongitudPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la ubicación de coordenadas en el mapa");
        }

        if (validacionRegLocal) {

            int totRegistro = ejbSspLocalAutorizacionFacade.existeLocalRegistrado_x_Campos(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipoViasLPSSelected_Form.getId(), regDireccionLPS_Form, regNroFisicoLPS_Form);
            if (totRegistro > 0) {
                JsfUtil.mensajeAdvertencia("Los datos de este local ya se encuentran registrados");
                return;
            } else {

                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

                registroN2 = (SspRegistro) JsfUtil.entidadMayusculas(registroN2, "");
                registroN2.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR")); //TP_REGIST_NOR / TP_REGIST_MOD
                registroN2.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));    //TP_OPE_INI / TP_OPE_REN / TP_OPE_AMP
                registroN2.setTipoProId(registro.getTipoProId());
                registroN2.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_CRL")); //CAMBIO DE REPRESENTANTE LEGAL    
                registroN2.setEmpresaId(regAdminist);
                registroN2.setCartaFianzaId(null);
                registroN2.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                registroN2.setRegistroId(registro);
                registroN2.setNroSolicitiud("*");
                registroN2.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registroN2.setFecha(new Date());
                registroN2.setActivo(JsfUtil.TRUE);
                registroN2.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registroN2.setAudNumIp(JsfUtil.getIpAddress());
                registroN2.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));

                if (registroN2.getId() != null) {
                    ejbSspRegistroFacade.edit(registroN2);
                } else {
                    ejbSspRegistroFacade.create(registroN2);
                }

                localAutorizacionN2 = (SspLocalAutorizacion) JsfUtil.entidadMayusculas(localAutorizacionN2, "");
                localAutorizacionN2.setId(null);
                localAutorizacionN2.setTipoUbicacionId(regTipoViasLPSSelected_Form);
                localAutorizacionN2.setDireccion(regDireccionLPS_Form);
                localAutorizacionN2.setNroFisico(regNroFisicoLPS_Form);
                localAutorizacionN2.setDistritoId(regDistritoLPSSelected);
                localAutorizacionN2.setReferencia(regReferenciaLPS_Form);
                localAutorizacionN2.setGeoLatitud(regGeoLatitudLPS_Form);
                localAutorizacionN2.setGeoLongitud(regGeoLongitudPS_Form);
                localAutorizacionN2.setFecha(new Date());
                localAutorizacionN2.setActivo(JsfUtil.TRUE);
                localAutorizacionN2.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                localAutorizacionN2.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspLocalAutorizacionFacade.create(localAutorizacionN2);

                tipoUsoLocal = (SspTipoUsoLocal) JsfUtil.entidadMayusculas(tipoUsoLocal, "");
                tipoUsoLocal.setId(null);
                tipoUsoLocal.setLocalId(localAutorizacionN2);
                tipoUsoLocal.setRegistroId(registroN2);
                tipoUsoLocal.setTipoLocalId(regTipLocalLPSSelected_Form);
                tipoUsoLocal.setTipoUsoId(regTipUsoLPSSelected_Form);
                tipoUsoLocal.setFecha(new Date());
                tipoUsoLocal.setActivo(JsfUtil.TRUE);
                tipoUsoLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                tipoUsoLocal.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspTipoUsoLocalFacade.create(tipoUsoLocal);

                localRegistro = (SspLocalRegistro) JsfUtil.entidadMayusculas(localRegistro, "");
                localRegistro.setId(null);
                localRegistro.setRegistroId(registroN2);
                localRegistro.setLocalId(localAutorizacionN2);
                localRegistro.setFecha(new Date());
                localRegistro.setActivo(JsfUtil.TRUE);
                localRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                localRegistro.setAudNumIp(JsfUtil.getIpAddress());
                ejbSspLocalRegistroFacade.create(localRegistro);

                listarLocalesAutorizXDistrito();
                RequestContext.getCurrentInstance().execute("PF('dlgMapaACRL').hide()");
                RequestContext.getCurrentInstance().update("FormRegCambRepresLegal");

            }

        }

    }

    public void handleFileUploadPDF_Local(FileUploadEvent event) {
        try {
            if (event != null) {
                fileLocal = event.getFile();
                if (JsfUtil.verificarPDF(fileLocal)) {
                    localByte = IOUtils.toByteArray(fileLocal.getInputstream());
                    archivoLocal = new DefaultStreamedContent(fileLocal.getInputstream(), "application/pdf");
                    nomArchivoLocal = fileLocal.getFileName();
                } else {
                    fileLocal = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadPDF_SOCIO(FileUploadEvent event) {
        try {
            if (event != null) {
                fileSocio = event.getFile();
                if (JsfUtil.verificarPDF(fileSocio)) {
                    socioByte = IOUtils.toByteArray(fileSocio.getInputstream());
                    archivoSocio = new DefaultStreamedContent(fileSocio.getInputstream(), "application/pdf");
                    nomArchivoSocio = fileSocio.getFileName();
                } else {
                    fileSocio = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=====================================================================
    //=====================    METODOS GET SET  ===========================
    //=====================================================================
    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public SspRegistro getRegistro() {
        return registro;
    }

    public void setRegistro(SspRegistro registro) {
        this.registro = registro;
    }

    /*public boolean isTermCondic_DatosAdministradoUsuario() {
        return termCondic_DatosAdministradoUsuario;
    }

    public void setTermCondic_DatosAdministradoUsuario(boolean termCondic_DatosAdministradoUsuario) {
        this.termCondic_DatosAdministradoUsuario = termCondic_DatosAdministradoUsuario;
    }*/
    public SbPersonaGt getRegAdminist() {
        return regAdminist;
    }

    public void setRegAdminist(SbPersonaGt regAdminist) {
        this.regAdminist = regAdminist;
    }

    public SbPersonaGt getRegUserAdminist() {
        return regUserAdminist;
    }

    public void setRegUserAdminist(SbPersonaGt regUserAdminist) {
        this.regUserAdminist = regUserAdminist;
    }

    public String getAdministRazonSocial() {
        return administRazonSocial;
    }

    public void setAdministRazonSocial(String administRazonSocial) {
        this.administRazonSocial = administRazonSocial;
    }

    public String getAdministRUC() {
        return administRUC;
    }

    public void setAdministRUC(String administRUC) {
        this.administRUC = administRUC;
    }

    public String getAdministNombres() {
        return administNombres;
    }

    public void setAdministNombres(String administNombres) {
        this.administNombres = administNombres;
    }

    public String getAdministTipDoc() {
        return administTipDoc;
    }

    public void setAdministTipDoc(String administTipDoc) {
        this.administTipDoc = administTipDoc;
    }

    public String getAdministNumDoc() {
        return administNumDoc;
    }

    public void setAdministNumDoc(String administNumDoc) {
        this.administNumDoc = administNumDoc;
    }

    public List<TipoBaseGt> getRegTipoSeguridadList() {
        return regTipoSeguridadList;
    }

    public void setRegTipoSeguridadList(List<TipoBaseGt> regTipoSeguridadList) {
        this.regTipoSeguridadList = regTipoSeguridadList;
    }

    public TipoBaseGt getRegTipoSeguridadSelected() {
        return regTipoSeguridadSelected;
    }

    public void setRegTipoSeguridadSelected(TipoBaseGt regTipoSeguridadSelected) {
        this.regTipoSeguridadSelected = regTipoSeguridadSelected;
    }

    public List<TipoBaseGt> getRegFormaTipoSeguridadList() {
        return regFormaTipoSeguridadList;
    }

    public void setRegFormaTipoSeguridadList(List<TipoBaseGt> regFormaTipoSeguridadList) {
        this.regFormaTipoSeguridadList = regFormaTipoSeguridadList;
    }

    public TipoBaseGt getRegFormaTipoSeguridadSelected() {
        return regFormaTipoSeguridadSelected;
    }

    public void setRegFormaTipoSeguridadSelected(TipoBaseGt regFormaTipoSeguridadSelected) {
        this.regFormaTipoSeguridadSelected = regFormaTipoSeguridadSelected;
    }

    public List<TipoBaseGt> getRegTipoViasList() {
        return regTipoViasList;
    }

    public void setRegTipoViasList(List<TipoBaseGt> regTipoViasList) {
        this.regTipoViasList = regTipoViasList;
    }

    public TipoBaseGt getRegTipoViasSelected() {
        return regTipoViasSelected;
    }

    public void setRegTipoViasSelected(TipoBaseGt regTipoViasSelected) {
        this.regTipoViasSelected = regTipoViasSelected;
    }

    public SspRepresentanteRegistro getRepresentanteRegistro() {
        return representanteRegistro;
    }

    public void setRepresentanteRegistro(SspRepresentanteRegistro representanteRegistro) {
        this.representanteRegistro = representanteRegistro;
    }

    public SspRegistroEvento getRegistroEvento() {
        return registroEvento;
    }

    public void setRegistroEvento(SspRegistroEvento registroEvento) {
        this.registroEvento = registroEvento;
    }

    public SspServicio getServicio() {
        return servicio;
    }

    public void setServicio(SspServicio servicio) {
        this.servicio = servicio;
    }

    public SspForma getForma() {
        return forma;
    }

    public void setForma(SspForma forma) {
        this.forma = forma;
    }

    public List<TipoSeguridad> getRegServicioPrestadoList() {
        return regServicioPrestadoList;
    }

    public void setRegServicioPrestadoList(List<TipoSeguridad> regServicioPrestadoList) {
        this.regServicioPrestadoList = regServicioPrestadoList;
    }

    public TipoSeguridad getRegServicioPrestadoSelected() {
        return regServicioPrestadoSelected;
    }

    public void setRegServicioPrestadoSelected(TipoSeguridad regServicioPrestadoSelected) {
        this.regServicioPrestadoSelected = regServicioPrestadoSelected;
    }

    public SspRepresentantePublico getRepresentantePublico() {
        return representantePublico;
    }

    public void setRepresentantePublico(SspRepresentantePublico representantePublico) {
        this.representantePublico = representantePublico;
    }

    public List<TipoBaseGt> getRegTipoDocEPList() {
        return regTipoDocEPList;
    }

    public void setRegTipoDocEPList(List<TipoBaseGt> regTipoDocEPList) {
        this.regTipoDocEPList = regTipoDocEPList;
    }

    public TipoBaseGt getRegTipoDocEPSelected() {
        return regTipoDocEPSelected;
    }

    public void setRegTipoDocEPSelected(TipoBaseGt regTipoDocEPSelected) {
        this.regTipoDocEPSelected = regTipoDocEPSelected;
    }

    public String getRegNombDocEP() {
        return regNombDocEP;
    }

    public void setRegNombDocEP(String regNombDocEP) {
        this.regNombDocEP = regNombDocEP;
    }

    public String getRegNumDocEP() {
        return regNumDocEP;
    }

    public void setRegNumDocEP(String regNumDocEP) {
        this.regNumDocEP = regNumDocEP;
    }

    public Date getRegFechaEmisEP() {
        return regFechaEmisEP;
    }

    public void setRegFechaEmisEP(Date regFechaEmisEP) {
        this.regFechaEmisEP = regFechaEmisEP;
    }

    public List<SbDistritoGt> getRegDistritoLPSList() {
        return regDistritoLPSList;
    }

    public void setRegDistritoLPSList(List<SbDistritoGt> regDistritoLPSList) {
        this.regDistritoLPSList = regDistritoLPSList;
    }

    public SbDistritoGt getRegDistritoLPSSelected() {
        return regDistritoLPSSelected;
    }

    public void setRegDistritoLPSSelected(SbDistritoGt regDistritoLPSSelected) {
        this.regDistritoLPSSelected = regDistritoLPSSelected;
    }

    public List<TipoBaseGt> getRegTipoViasLPSList() {
        return regTipoViasLPSList;
    }

    public void setRegTipoViasLPSList(List<TipoBaseGt> regTipoViasLPSList) {
        this.regTipoViasLPSList = regTipoViasLPSList;
    }

    public TipoBaseGt getRegTipoViasLPSSelected() {
        return regTipoViasLPSSelected;
    }

    public void setRegTipoViasLPSSelected(TipoBaseGt regTipoViasLPSSelected) {
        this.regTipoViasLPSSelected = regTipoViasLPSSelected;
    }

    public List<TipoSeguridad> getRegTipLocalLPSList() {
        return regTipLocalLPSList;
    }

    public void setRegTipLocalLPSList(List<TipoSeguridad> regTipLocalLPSList) {
        this.regTipLocalLPSList = regTipLocalLPSList;
    }

    public List<TipoSeguridad> getRegTipUsoLPSList() {
        return regTipUsoLPSList;
    }

    public void setRegTipUsoLPSList(List<TipoSeguridad> regTipUsoLPSList) {
        this.regTipUsoLPSList = regTipUsoLPSList;
    }

    public TipoSeguridad getRegTipUsoLPSSelected() {
        return regTipUsoLPSSelected;
    }

    public void setRegTipUsoLPSSelected(TipoSeguridad regTipUsoLPSSelected) {
        this.regTipUsoLPSSelected = regTipUsoLPSSelected;
    }

    public String getRegDireccionLPS() {
        return regDireccionLPS;
    }

    public void setRegDireccionLPS(String regDireccionLPS) {
        this.regDireccionLPS = regDireccionLPS;
    }

    public String getRegReferenciaLPS() {
        return regReferenciaLPS;
    }

    public void setRegReferenciaLPS(String regReferenciaLPS) {
        this.regReferenciaLPS = regReferenciaLPS;
    }

    public SspLocalAutorizacion getLocalAutorizacion() {
        return localAutorizacion;
    }

    public void setLocalAutorizacion(SspLocalAutorizacion localAutorizacion) {
        this.localAutorizacion = localAutorizacion;
    }

    public SspTipoUsoLocal getTipoUsoLocal() {
        return tipoUsoLocal;
    }

    public void setTipoUsoLocal(SspTipoUsoLocal tipoUsoLocal) {
        this.tipoUsoLocal = tipoUsoLocal;
    }

    public SspLocalRegistro getLocalRegistro() {
        return localRegistro;
    }

    public void setLocalRegistro(SspLocalRegistro localRegistro) {
        this.localRegistro = localRegistro;
    }

    public List<TipoSeguridad> getRegAlmacenLPSList() {
        return regAlmacenLPSList;
    }

    public void setRegAlmacenLPSList(List<TipoSeguridad> regAlmacenLPSList) {
        this.regAlmacenLPSList = regAlmacenLPSList;
    }

    public TipoSeguridad getRegAlmacenLPSSelected() {
        return regAlmacenLPSSelected;
    }

    public void setRegAlmacenLPSSelected(TipoSeguridad regAlmacenLPSSelected) {
        this.regAlmacenLPSSelected = regAlmacenLPSSelected;
    }

    public List<TipoBaseGt> getRegPrioridadLPSList() {
        return regPrioridadLPSList;
    }

    public void setRegPrioridadLPSList(List<TipoBaseGt> regPrioridadLPSList) {
        this.regPrioridadLPSList = regPrioridadLPSList;
    }

    public TipoBaseGt getRegPrioridadLPSSelected() {
        return regPrioridadLPSSelected;
    }

    public void setRegPrioridadLPSSelected(TipoBaseGt regPrioridadLPSSelected) {
        this.regPrioridadLPSSelected = regPrioridadLPSSelected;
    }

    public List<TipoBaseGt> getRegTipoMedioContactoLPSList() {
        return regTipoMedioContactoLPSList;
    }

    public void setRegTipoMedioContactoLPSList(List<TipoBaseGt> regTipoMedioContactoLPSList) {
        this.regTipoMedioContactoLPSList = regTipoMedioContactoLPSList;
    }

    public TipoBaseGt getRegTipoMedioContactoLPSSelected() {
        return regTipoMedioContactoLPSSelected;
    }

    public void setRegTipoMedioContactoLPSSelected(TipoBaseGt regTipoMedioContactoLPSSelected) {
        this.regTipoMedioContactoLPSSelected = regTipoMedioContactoLPSSelected;
    }

    public SspLicenciaMunicipal getLicenciaMunicipal() {
        return licenciaMunicipal;
    }

    public void setLicenciaMunicipal(SspLicenciaMunicipal licenciaMunicipal) {
        this.licenciaMunicipal = licenciaMunicipal;
    }

    public List<TipoBaseGt> getRegTipMunicipalidadLPSList() {
        return regTipMunicipalidadLPSList;
    }

    public void setRegTipMunicipalidadLPSList(List<TipoBaseGt> regTipMunicipalidadLPSList) {
        this.regTipMunicipalidadLPSList = regTipMunicipalidadLPSList;
    }

    public TipoBaseGt getRegTipMunicipalidadLPSSelected() {
        return regTipMunicipalidadLPSSelected;
    }

    public void setRegTipMunicipalidadLPSSelected(TipoBaseGt regTipMunicipalidadLPSSelected) {
        this.regTipMunicipalidadLPSSelected = regTipMunicipalidadLPSSelected;
    }

    public List<TipoBaseGt> getRegMunicLPSList() {
        return regMunicLPSList;
    }

    public void setRegMunicLPSList(List<TipoBaseGt> regMunicLPSList) {
        this.regMunicLPSList = regMunicLPSList;
    }

    public TipoBaseGt getRegMunicLPSSelected() {
        return regMunicLPSSelected;
    }

    public void setRegMunicLPSSelected(TipoBaseGt regMunicLPSSelected) {
        this.regMunicLPSSelected = regMunicLPSSelected;
    }

    public String getRegNroLicenciaMunicLPS() {
        return regNroLicenciaMunicLPS;
    }

    public void setRegNroLicenciaMunicLPS(String regNroLicenciaMunicLPS) {
        this.regNroLicenciaMunicLPS = regNroLicenciaMunicLPS;
    }

    public String getRegGiroComercialLPS() {
        return regGiroComercialLPS;
    }

    public void setRegGiroComercialLPS(String regGiroComercialLPS) {
        this.regGiroComercialLPS = regGiroComercialLPS;
    }

    public Date getRegFechaEmisLPS() {
        return regFechaEmisLPS;
    }

    public void setRegFechaEmisLPS(Date regFechaEmisLPS) {
        this.regFechaEmisLPS = regFechaEmisLPS;
    }

    public Date getRegFechaVencLPS() {
        return regFechaVencLPS;
    }

    public void setRegFechaVencLPS(Date regFechaVencLPS) {
        this.regFechaVencLPS = regFechaVencLPS;
    }

    public boolean isRegIndeterminadoLPS() {
        return regIndeterminadoLPS;
    }

    public void setRegIndeterminadoLPS(boolean regIndeterminadoLPS) {
        this.regIndeterminadoLPS = regIndeterminadoLPS;
    }

    public SspCartaFianza getCartaFianza() {
        return cartaFianza;
    }

    public void setCartaFianza(SspCartaFianza cartaFianza) {
        this.cartaFianza = cartaFianza;
    }

    public List<TipoSeguridad> getRegTipoFinancieraCFList() {
        return regTipoFinancieraCFList;
    }

    public void setRegTipoFinancieraCFList(List<TipoSeguridad> regTipoFinancieraCFList) {
        this.regTipoFinancieraCFList = regTipoFinancieraCFList;
    }

    public TipoSeguridad getRegTipoFinancieraCFSelected() {
        return regTipoFinancieraCFSelected;
    }

    public void setRegTipoFinancieraCFSelected(TipoSeguridad regTipoFinancieraCFSelected) {
        this.regTipoFinancieraCFSelected = regTipoFinancieraCFSelected;
    }

    public List<TipoSeguridad> getRegEntidadFinancieraCFList() {
        return regEntidadFinancieraCFList;
    }

    public void setRegEntidadFinancieraCFList(List<TipoSeguridad> regEntidadFinancieraCFList) {
        this.regEntidadFinancieraCFList = regEntidadFinancieraCFList;
    }

    public TipoSeguridad getRegEntidadFinancieraCFSelected() {
        return regEntidadFinancieraCFSelected;
    }

    public void setRegEntidadFinancieraCFSelected(TipoSeguridad regEntidadFinancieraCFSelected) {
        this.regEntidadFinancieraCFSelected = regEntidadFinancieraCFSelected;
    }

    public List<TipoBaseGt> getRegTipoMonedaCFList() {
        return regTipoMonedaCFList;
    }

    public void setRegTipoMonedaCFList(List<TipoBaseGt> regTipoMonedaCFList) {
        this.regTipoMonedaCFList = regTipoMonedaCFList;
    }

    public TipoBaseGt getRegTipoMonedaCFSelected() {
        return regTipoMonedaCFSelected;
    }

    public void setRegTipoMonedaCFSelected(TipoBaseGt regTipoMonedaCFSelected) {
        this.regTipoMonedaCFSelected = regTipoMonedaCFSelected;
    }

    public String getRegNroCartaFianza() {
        return regNroCartaFianza;
    }

    public void setRegNroCartaFianza(String regNroCartaFianza) {
        this.regNroCartaFianza = regNroCartaFianza;
    }

    public Date getRegFechVigenciaIni() {
        return regFechVigenciaIni;
    }

    public void setRegFechVigenciaIni(Date regFechVigenciaIni) {
        this.regFechVigenciaIni = regFechVigenciaIni;
    }

    public BigDecimal getRegMontoCartaFianza() {
        return regMontoCartaFianza;
    }

    public void setRegMontoCartaFianza(BigDecimal regMontoCartaFianza) {
        this.regMontoCartaFianza = regMontoCartaFianza;
    }

    public Date getRegFechVigenciaFin() {
        return regFechVigenciaFin;
    }

    public void setRegFechVigenciaFin(Date regFechVigenciaFin) {
        this.regFechVigenciaFin = regFechVigenciaFin;
    }

    public UploadedFile getFileCF() {
        return fileCF;
    }

    public void setFileCF(UploadedFile fileCF) {
        this.fileCF = fileCF;
    }

    public byte[] getCfByte() {
        return cfByte;
    }

    public void setCfByte(byte[] cfByte) {
        this.cfByte = cfByte;
    }

    public StreamedContent getArchivoCF() {
        return archivoCF;
    }

    public void setArchivoCF(StreamedContent archivoCF) {
        this.archivoCF = archivoCF;
    }

    public String getNomArchivoCF() {
        return nomArchivoCF;
    }

    public void setNomArchivoCF(String nomArchivoCF) {
        this.nomArchivoCF = nomArchivoCF;
    }

    public UploadedFile getFileLM() {
        return fileLM;
    }

    public void setFileLM(UploadedFile fileLM) {
        this.fileLM = fileLM;
    }

    public byte[] getLmByte() {
        return lmByte;
    }

    public void setLmByte(byte[] lmByte) {
        this.lmByte = lmByte;
    }

    public StreamedContent getArchivoLM() {
        return archivoLM;
    }

    public void setArchivoLM(StreamedContent archivoLM) {
        this.archivoLM = archivoLM;
    }

    public String getNomArchivoLM() {
        return nomArchivoLM;
    }

    public void setNomArchivoLM(String nomArchivoLM) {
        this.nomArchivoLM = nomArchivoLM;
    }

    public UploadedFile getFileRP() {
        return fileRP;
    }

    public void setFileRP(UploadedFile fileRP) {
        this.fileRP = fileRP;
    }

    public byte[] getRpByte() {
        return rpByte;
    }

    public void setRpByte(byte[] rpByte) {
        this.rpByte = rpByte;
    }

    public StreamedContent getArchivoRP() {
        return archivoRP;
    }

    public void setArchivoRP(StreamedContent archivoRP) {
        this.archivoRP = archivoRP;
    }

    public String getNomArchivoRP() {
        return nomArchivoRP;
    }

    public void setNomArchivoRP(String nomArchivoRP) {
        this.nomArchivoRP = nomArchivoRP;
    }

    public BigInteger getRegCantArmeriaLPS() {
        return regCantArmeriaLPS;
    }

    public void setRegCantArmeriaLPS(BigInteger regCantArmeriaLPS) {
        this.regCantArmeriaLPS = regCantArmeriaLPS;
    }

    public ArrayList<SspContacto> getLstContactos() {
        return lstContactos;
    }

    public void setLstContactos(ArrayList<SspContacto> lstContactos) {
        this.lstContactos = lstContactos;
    }

    public SspContacto getSelectedContacto() {
        return selectedContacto;
    }

    public void setSelectedContacto(SspContacto selectedContacto) {
        this.selectedContacto = selectedContacto;
    }

    public Long getContRegContacto() {
        return contRegContacto;
    }

    public void setContRegContacto(Long contRegContacto) {
        this.contRegContacto = contRegContacto;
    }

    public SspAlmacen getSelectedAlmacen() {
        return selectedAlmacen;
    }

    public void setSelectedAlmacen(SspAlmacen selectedAlmacen) {
        this.selectedAlmacen = selectedAlmacen;
    }

    public ArrayList<SspAlmacen> getLstAlmacenes() {
        return lstAlmacenes;
    }

    public void setLstAlmacenes(ArrayList<SspAlmacen> lstAlmacenes) {
        this.lstAlmacenes = lstAlmacenes;
    }

    public Long getContRegAlmacen() {
        return contRegAlmacen;
    }

    public void setContRegAlmacen(Long contRegAlmacen) {
        this.contRegAlmacen = contRegAlmacen;
    }

    public String getRegtextoContactoLPS() {
        return regtextoContactoLPS;
    }

    public void setRegtextoContactoLPS(String regtextoContactoLPS) {
        this.regtextoContactoLPS = regtextoContactoLPS;
    }

    public List<TipoBaseGt> getRegTipDocAccionistaList() {
        return regTipDocAccionistaList;
    }

    public void setRegTipDocAccionistaList(List<TipoBaseGt> regTipDocAccionistaList) {
        this.regTipDocAccionistaList = regTipDocAccionistaList;
    }

    public TipoBaseGt getRegTipDocAccionistaSelected() {
        return regTipDocAccionistaSelected;
    }

    public void setRegTipDocAccionistaSelected(TipoBaseGt regTipDocAccionistaSelected) {
        this.regTipDocAccionistaSelected = regTipDocAccionistaSelected;
    }

    public List<TipoBaseGt> getRegTipoSocioList() {
        return regTipoSocioList;
    }

    public void setRegTipoSocioList(List<TipoBaseGt> regTipoSocioList) {
        this.regTipoSocioList = regTipoSocioList;
    }

    public TipoBaseGt getRegTipoSocioSelected() {
        return regTipoSocioSelected;
    }

    public void setRegTipoSocioSelected(TipoBaseGt regTipoSocioSelected) {
        this.regTipoSocioSelected = regTipoSocioSelected;
    }

    public String getRegCargoSocio() {
        return regCargoSocio;
    }

    public void setRegCargoSocio(String regCargoSocio) {
        this.regCargoSocio = regCargoSocio;
    }

    public String getRegNumeroDocAccionista() {
        return regNumeroDocAccionista;
    }

    public void setRegNumeroDocAccionista(String regNumeroDocAccionista) {
        this.regNumeroDocAccionista = regNumeroDocAccionista;
    }

    public Long getContRegSocio() {
        return contRegSocio;
    }

    public void setContRegSocio(Long contRegSocio) {
        this.contRegSocio = contRegSocio;
    }

    public SbPersonaGt getRegDatosSocioEncontrado() {
        return regDatosSocioEncontrado;
    }

    public void setRegDatosSocioEncontrado(SbPersonaGt regDatosSocioEncontrado) {
        this.regDatosSocioEncontrado = regDatosSocioEncontrado;
    }

    public ArrayList<SspParticipante> getLstSocios() {
        return lstSocios;
    }

    public void setLstSocios(ArrayList<SspParticipante> lstSocios) {
        this.lstSocios = lstSocios;
    }

    public SspParticipante getSelectedSocio() {
        return selectedSocio;
    }

    public void setSelectedSocio(SspParticipante selectedSocio) {
        this.selectedSocio = selectedSocio;
    }

    public String getRegNombreSocioEncontrado() {
        return regNombreSocioEncontrado;
    }

    public void setRegNombreSocioEncontrado(String regNombreSocioEncontrado) {
        this.regNombreSocioEncontrado = regNombreSocioEncontrado;
    }

    public List<TipoSeguridad> getRegFormasServTecnoSegList() {
        return regFormasServTecnoSegList;
    }

    public void setRegFormasServTecnoSegList(List<TipoSeguridad> regFormasServTecnoSegList) {
        this.regFormasServTecnoSegList = regFormasServTecnoSegList;
    }

    public TipoSeguridad getRegForma_TP_MCO_TEC_CRAIDMR() {
        return regForma_TP_MCO_TEC_CRAIDMR;
    }

    public void setRegForma_TP_MCO_TEC_CRAIDMR(TipoSeguridad regForma_TP_MCO_TEC_CRAIDMR) {
        this.regForma_TP_MCO_TEC_CRAIDMR = regForma_TP_MCO_TEC_CRAIDMR;
    }

    public TipoSeguridad getRegForma_TP_MCO_TEC_PSGPS() {
        return regForma_TP_MCO_TEC_PSGPS;
    }

    public void setRegForma_TP_MCO_TEC_PSGPS(TipoSeguridad regForma_TP_MCO_TEC_PSGPS) {
        this.regForma_TP_MCO_TEC_PSGPS = regForma_TP_MCO_TEC_PSGPS;
    }

    public TipoSeguridad getRegForma_TP_MCO_TEC_IDMSVD() {
        return regForma_TP_MCO_TEC_IDMSVD;
    }

    public void setRegForma_TP_MCO_TEC_IDMSVD(TipoSeguridad regForma_TP_MCO_TEC_IDMSVD) {
        this.regForma_TP_MCO_TEC_IDMSVD = regForma_TP_MCO_TEC_IDMSVD;
    }

    public TipoSeguridad getRegForma_TP_MCO_TEC_IDMSDEI() {
        return regForma_TP_MCO_TEC_IDMSDEI;
    }

    public void setRegForma_TP_MCO_TEC_IDMSDEI(TipoSeguridad regForma_TP_MCO_TEC_IDMSDEI) {
        this.regForma_TP_MCO_TEC_IDMSDEI = regForma_TP_MCO_TEC_IDMSDEI;
    }

    public TipoSeguridad getRegForma_TP_MCO_TEC_IDMSCA() {
        return regForma_TP_MCO_TEC_IDMSCA;
    }

    public void setRegForma_TP_MCO_TEC_IDMSCA(TipoSeguridad regForma_TP_MCO_TEC_IDMSCA) {
        this.regForma_TP_MCO_TEC_IDMSCA = regForma_TP_MCO_TEC_IDMSCA;
    }

    public TipoSeguridad getRegForma_TP_MCO_TEC_EPIS() {
        return regForma_TP_MCO_TEC_EPIS;
    }

    public void setRegForma_TP_MCO_TEC_EPIS(TipoSeguridad regForma_TP_MCO_TEC_EPIS) {
        this.regForma_TP_MCO_TEC_EPIS = regForma_TP_MCO_TEC_EPIS;
    }

    public TipoSeguridad getRegForma_TP_MCO_TEC_SCM() {
        return regForma_TP_MCO_TEC_SCM;
    }

    public void setRegForma_TP_MCO_TEC_SCM(TipoSeguridad regForma_TP_MCO_TEC_SCM) {
        this.regForma_TP_MCO_TEC_SCM = regForma_TP_MCO_TEC_SCM;
    }

    public TipoSeguridad getRegForma_TP_MCO_TEC_OTRO() {
        return regForma_TP_MCO_TEC_OTRO;
    }

    public void setRegForma_TP_MCO_TEC_OTRO(TipoSeguridad regForma_TP_MCO_TEC_OTRO) {
        this.regForma_TP_MCO_TEC_OTRO = regForma_TP_MCO_TEC_OTRO;
    }

    public String[] getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(String[] selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public String getSelectOptionOtroVal() {
        return selectOptionOtroVal;
    }

    public void setSelectOptionOtroVal(String selectOptionOtroVal) {
        this.selectOptionOtroVal = selectOptionOtroVal;
    }

    public ArrayList<SspForma> getLstCheckedForma() {
        return lstCheckedForma;
    }

    public void setLstCheckedForma(ArrayList<SspForma> lstCheckedForma) {
        this.lstCheckedForma = lstCheckedForma;
    }

    public Long getContRegCheckForma() {
        return contRegCheckForma;
    }

    public void setContRegCheckForma(Long contRegCheckForma) {
        this.contRegCheckForma = contRegCheckForma;
    }

    public String getRegNroComprobante() {
        return regNroComprobante;
    }

    public void setRegNroComprobante(String regNroComprobante) {
        this.regNroComprobante = regNroComprobante;
    }

    public SbRecibos getRegDatoRecibos() {
        return regDatoRecibos;
    }

    public void setRegDatoRecibos(SbRecibos regDatoRecibos) {
        this.regDatoRecibos = regDatoRecibos;
    }

    public List<SbRecibos> getRegListDatoRecibos() {
        return regListDatoRecibos;
    }

    public void setRegListDatoRecibos(List<SbRecibos> regListDatoRecibos) {
        this.regListDatoRecibos = regListDatoRecibos;
    }

    public MapModel getEmptyModel() {
        return emptyModel;
    }

    public void setEmptyModel(MapModel emptyModel) {
        this.emptyModel = emptyModel;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getRegNroDocBuscar() {
        return regNroDocBuscar;
    }

    public void setRegNroDocBuscar(String regNroDocBuscar) {
        this.regNroDocBuscar = regNroDocBuscar;
    }

    public SbPersonaGt getRegDatoPersonaByNumDocRL() {
        return regDatoPersonaByNumDocRL;
    }

    public void setRegDatoPersonaByNumDocRL(SbPersonaGt regDatoPersonaByNumDocRL) {
        this.regDatoPersonaByNumDocRL = regDatoPersonaByNumDocRL;
    }

    public List<TipoBaseGt> getRegTipoDocList() {
        return regTipoDocList;
    }

    public void setRegTipoDocList(List<TipoBaseGt> regTipoDocList) {
        this.regTipoDocList = regTipoDocList;
    }

    public TipoBaseGt getRegTipoDocSelected() {
        return regTipoDocSelected;
    }

    public void setRegTipoDocSelected(TipoBaseGt regTipoDocSelected) {
        this.regTipoDocSelected = regTipoDocSelected;
    }

    public List<SbPersonaGt> getRegPersonasRLList() {
        return regPersonasRLList;
    }

    public void setRegPersonasRLList(List<SbPersonaGt> regPersonasRLList) {
        this.regPersonasRLList = regPersonasRLList;
    }

    public SbRelacionPersonaGt getRegistroSbRelacionPersonaGt() {
        return registroSbRelacionPersonaGt;
    }

    public void setRegistroSbRelacionPersonaGt(SbRelacionPersonaGt registroSbRelacionPersonaGt) {
        this.registroSbRelacionPersonaGt = registroSbRelacionPersonaGt;
    }

    public String getxNroSolicitiud() {
        return xNroSolicitiud;
    }

    public void setxNroSolicitiud(String xNroSolicitiud) {
        this.xNroSolicitiud = xNroSolicitiud;
    }

    public String getUsuarioArchivar() {
        return usuarioArchivar;
    }

    public void setUsuarioArchivar(String usuarioArchivar) {
        this.usuarioArchivar = usuarioArchivar;
    }

    public TipoBaseGt getRegTipoProcesoId() {
        return regTipoProcesoId;
    }

    public void setRegTipoProcesoId(TipoBaseGt regTipoProcesoId) {
        this.regTipoProcesoId = regTipoProcesoId;
    }

    public String getTipoAdministradoUsuario() {
        return tipoAdministradoUsuario;
    }

    public void setTipoAdministradoUsuario(String tipoAdministradoUsuario) {
        this.tipoAdministradoUsuario = tipoAdministradoUsuario;
    }

    public List<TipoBaseGt> getRegZonaRegListRL() {
        return regZonaRegListRL;
    }

    public void setRegZonaRegListRL(List<TipoBaseGt> regZonaRegListRL) {
        this.regZonaRegListRL = regZonaRegListRL;
    }

    public TipoBaseGt getRegZonaRegSelectedRL() {
        return regZonaRegSelectedRL;
    }

    public void setRegZonaRegSelectedRL(TipoBaseGt regZonaRegSelectedRL) {
        this.regZonaRegSelectedRL = regZonaRegSelectedRL;
    }

    public List<TipoBaseGt> getRegOficinaRegListRL() {
        return regOficinaRegListRL;
    }

    public void setRegOficinaRegListRL(List<TipoBaseGt> regOficinaRegListRL) {
        this.regOficinaRegListRL = regOficinaRegListRL;
    }

    public TipoBaseGt getRegOficinaRegSelectedRL() {
        return regOficinaRegSelectedRL;
    }

    public void setRegOficinaRegSelectedRL(TipoBaseGt regOficinaRegSelectedRL) {
        this.regOficinaRegSelectedRL = regOficinaRegSelectedRL;
    }

    public String getRegDomicilioRLSelected() {
        return regDomicilioRLSelected;
    }

    public void setRegDomicilioRLSelected(String regDomicilioRLSelected) {
        this.regDomicilioRLSelected = regDomicilioRLSelected;
    }

    public SbDireccionGt getRegSbDireccionRL() {
        return regSbDireccionRL;
    }

    public void setRegSbDireccionRL(SbDireccionGt regSbDireccionRL) {
        this.regSbDireccionRL = regSbDireccionRL;
    }

    public String getRegRLPartidaRegistral() {
        return regRLPartidaRegistral;
    }

    public void setRegRLPartidaRegistral(String regRLPartidaRegistral) {
        this.regRLPartidaRegistral = regRLPartidaRegistral;
    }

    public String getRegRLAsientoRegistral() {
        return regRLAsientoRegistral;
    }

    public void setRegRLAsientoRegistral(String regRLAsientoRegistral) {
        this.regRLAsientoRegistral = regRLAsientoRegistral;
    }

    public SbPartidaSunarp getPartidaSunarpRL() {
        return partidaSunarpRL;
    }

    public void setPartidaSunarpRL(SbPartidaSunarp partidaSunarpRL) {
        this.partidaSunarpRL = partidaSunarpRL;
    }

    public SbAsientoSunarp getAsientoSunarpRL() {
        return asientoSunarpRL;
    }

    public void setAsientoSunarpRL(SbAsientoSunarp asientoSunarpRL) {
        this.asientoSunarpRL = asientoSunarpRL;
    }

    public SbAsientoPersona getAsientoPersonaRL() {
        return asientoPersonaRL;
    }

    public void setAsientoPersonaRL(SbAsientoPersona asientoPersonaRL) {
        this.asientoPersonaRL = asientoPersonaRL;
    }

    public SbRelacionPersonaGt getRelacionPersonaGtRL() {
        return relacionPersonaGtRL;
    }

    public void setRelacionPersonaGtRL(SbRelacionPersonaGt relacionPersonaGtRL) {
        this.relacionPersonaGtRL = relacionPersonaGtRL;
    }

    public SbPersonaGt getRegPersonasRLSelected() {
        return regPersonasRLSelected;
    }

    public void setRegPersonasRLSelected(SbPersonaGt regPersonasRLSelected) {
        this.regPersonasRLSelected = regPersonasRLSelected;
    }

    public boolean isRegDisabledCalendar() {
        return regDisabledCalendar;
    }

    public void setRegDisabledCalendar(boolean regDisabledCalendar) {
        this.regDisabledCalendar = regDisabledCalendar;
    }

    public boolean isRegDisabledFormServTecn() {
        return regDisabledFormServTecn;
    }

    public void setRegDisabledFormServTecn(boolean regDisabledFormServTecn) {
        this.regDisabledFormServTecn = regDisabledFormServTecn;
    }

    public String getFiltroBuscarPor() {
        return filtroBuscarPor;
    }

    public void setFiltroBuscarPor(String filtroBuscarPor) {
        this.filtroBuscarPor = filtroBuscarPor;
    }

    public TipoBaseGt getFiltroTipoOpeSelected() {
        return filtroTipoOpeSelected;
    }

    public void setFiltroTipoOpeSelected(TipoBaseGt filtroTipoOpeSelected) {
        this.filtroTipoOpeSelected = filtroTipoOpeSelected;
    }

    public TipoSeguridad getFiltroTipoEstadoSelected() {
        return filtroTipoEstadoSelected;
    }

    public void setFiltroTipoEstadoSelected(TipoSeguridad filtroTipoEstadoSelected) {
        this.filtroTipoEstadoSelected = filtroTipoEstadoSelected;
    }

    public Date getFiltroFechaIniSelected() {
        return filtroFechaIniSelected;
    }

    public void setFiltroFechaIniSelected(Date filtroFechaIniSelected) {
        this.filtroFechaIniSelected = filtroFechaIniSelected;
    }

    public Date getFiltroFechaFinSelected() {
        return filtroFechaFinSelected;
    }

    public void setFiltroFechaFinSelected(Date filtroFechaFinSelected) {
        this.filtroFechaFinSelected = filtroFechaFinSelected;
    }

    public List<Map> getResultados() {
        return resultados;
    }

    public void setResultados(List<Map> resultados) {
        this.resultados = resultados;
    }

    public List<Map> getResultadosSeleccionados() {
        return resultadosSeleccionados;
    }

    public void setResultadosSeleccionados(List<Map> resultadosSeleccionados) {
        this.resultadosSeleccionados = resultadosSeleccionados;
    }

    public void seleccionarFilaTabla(SelectEvent event) {
        eventoSeleccion();
    }

    public void deseleccionarFilaTabla(UnselectEvent event) {
        eventoSeleccion();
    }

    public void toggleEventFila(ToggleSelectEvent event) {
        eventoSeleccion();
    }

    public void eventoSeleccion() {
        setDisabledBtnTransmitir(false);
        for (Map reg : resultadosSeleccionados) {
            if (!reg.get("ESTADO_CODPROG").equals("TP_ECC_CRE") && !reg.get("ESTADO_CODPROG").equals("TP_ECC_OBS")) {
                JsfUtil.mensajeAdvertencia("El cambio es sólo para los estados CREADO u OBSERVADO");
                setDisabledBtnTransmitir(true);
                return;
            }
        }
    }

    public boolean isDisabledBtnTransmitir() {
        return disabledBtnTransmitir;
    }

    public void setDisabledBtnTransmitir(boolean disabledBtnTransmitir) {
        this.disabledBtnTransmitir = disabledBtnTransmitir;
    }

    public TipoBaseGt getFiltroTipoSeguridadSelected() {
        return filtroTipoSeguridadSelected;
    }

    public void setFiltroTipoSeguridadSelected(TipoBaseGt filtroTipoSeguridadSelected) {
        this.filtroTipoSeguridadSelected = filtroTipoSeguridadSelected;
    }

    public String getFiltroNumero() {
        return filtroNumero;
    }

    public void setFiltroNumero(String filtroNumero) {
        this.filtroNumero = filtroNumero;
    }

    public List<TipoBaseGt> getFiltroTipoSeguridadListado() {
        return filtroTipoSeguridadListado;
    }

    public void setFiltroTipoSeguridadListado(List<TipoBaseGt> filtroTipoSeguridadListado) {
        this.filtroTipoSeguridadListado = filtroTipoSeguridadListado;
    }

    public List<TipoBaseGt> getFiltroTipoOpeListado() {
        return filtroTipoOpeListado;
    }

    public void setFiltroTipoOpeListado(List<TipoBaseGt> filtroTipoOpeListado) {
        this.filtroTipoOpeListado = filtroTipoOpeListado;
    }

    public List<TipoSeguridad> getFiltroTipoEstadoListado() {
        return filtroTipoEstadoListado;
    }

    public void setFiltroTipoEstadoListado(List<TipoSeguridad> filtroTipoEstadoListado) {
        this.filtroTipoEstadoListado = filtroTipoEstadoListado;
    }

    public boolean isTermCondic_CambDomLegal() {
        return termCondic_CambDomLegal;
    }

    public void setTermCondic_CambDomLegal(boolean termCondic_CambDomLegal) {
        this.termCondic_CambDomLegal = termCondic_CambDomLegal;
    }

    public String getNroAutoBuscarACDL() {
        return nroAutoBuscarACDL;
    }

    public void setNroAutoBuscarACDL(String nroAutoBuscarACDL) {
        this.nroAutoBuscarACDL = nroAutoBuscarACDL;
    }

    public boolean isHabilitarFormaTipServACDL() {
        return habilitarFormaTipServACDL;
    }

    public void setHabilitarFormaTipServACDL(boolean habilitarFormaTipServACDL) {
        this.habilitarFormaTipServACDL = habilitarFormaTipServACDL;
    }

    public List<TipoBaseGt> getFiltroTipoAutListado() {
        return filtroTipoAutListado;
    }

    public void setFiltroTipoAutListado(List<TipoBaseGt> filtroTipoAutListado) {
        this.filtroTipoAutListado = filtroTipoAutListado;
    }

    public TipoBaseGt getFiltroTipoAutSelected() {
        return filtroTipoAutSelected;
    }

    public void setFiltroTipoAutSelected(TipoBaseGt filtroTipoAutSelected) {
        this.filtroTipoAutSelected = filtroTipoAutSelected;
    }

    public boolean isExisteAsientoSunarpRL() {
        return existeAsientoSunarpRL;
    }

    public void setExisteAsientoSunarpRL(boolean existeAsientoSunarpRL) {
        this.existeAsientoSunarpRL = existeAsientoSunarpRL;
    }

    public MapModel getEmptyModelACDL() {
        return emptyModelACDL;
    }

    public void setEmptyModelACDL(MapModel emptyModelACDL) {
        this.emptyModelACDL = emptyModelACDL;
    }

    public Marker getMarkerACDL() {
        return markerACDL;
    }

    public void setMarkerACDL(Marker markerACDL) {
        this.markerACDL = markerACDL;
    }

    public List<LocalAutorizacionDetalle> getLocalAutorizacionListado() {
        return localAutorizacionListado;
    }

    public void setLocalAutorizacionListado(List<LocalAutorizacionDetalle> localAutorizacionListado) {
        this.localAutorizacionListado = localAutorizacionListado;
    }

    public SspLocalAutorizacion getLocalAutorizacionSelected() {
        return localAutorizacionSelected;
    }

    public void setLocalAutorizacionSelected(SspLocalAutorizacion localAutorizacionSelected) {
        this.localAutorizacionSelected = localAutorizacionSelected;
    }

    public String getLocalAutorizacionSelectedString() {
        return localAutorizacionSelectedString;
    }

    public void setLocalAutorizacionSelectedString(String localAutorizacionSelectedString) {
        this.localAutorizacionSelectedString = localAutorizacionSelectedString;
    }

    public String getRegNroFisicoLPS() {
        return regNroFisicoLPS;
    }

    public void setRegNroFisicoLPS(String regNroFisicoLPS) {
        this.regNroFisicoLPS = regNroFisicoLPS;
    }

    public List<TipoBaseGt> getRegTipoViasLPSList_Form() {
        return regTipoViasLPSList_Form;
    }

    public void setRegTipoViasLPSList_Form(List<TipoBaseGt> regTipoViasLPSList_Form) {
        this.regTipoViasLPSList_Form = regTipoViasLPSList_Form;
    }

    public TipoBaseGt getRegTipoViasLPSSelected_Form() {
        return regTipoViasLPSSelected_Form;
    }

    public void setRegTipoViasLPSSelected_Form(TipoBaseGt regTipoViasLPSSelected_Form) {
        this.regTipoViasLPSSelected_Form = regTipoViasLPSSelected_Form;
    }

    public String getRegDireccionLPS_Form() {
        return regDireccionLPS_Form;
    }

    public void setRegDireccionLPS_Form(String regDireccionLPS_Form) {
        this.regDireccionLPS_Form = regDireccionLPS_Form;
    }

    public String getRegNroFisicoLPS_Form() {
        return regNroFisicoLPS_Form;
    }

    public void setRegNroFisicoLPS_Form(String regNroFisicoLPS_Form) {
        this.regNroFisicoLPS_Form = regNroFisicoLPS_Form;
    }

    public String getRegReferenciaLPS_Form() {
        return regReferenciaLPS_Form;
    }

    public void setRegReferenciaLPS_Form(String regReferenciaLPS_Form) {
        this.regReferenciaLPS_Form = regReferenciaLPS_Form;
    }

    public String getRegGeoLatitudLPS_Form() {
        return regGeoLatitudLPS_Form;
    }

    public void setRegGeoLatitudLPS_Form(String regGeoLatitudLPS_Form) {
        this.regGeoLatitudLPS_Form = regGeoLatitudLPS_Form;
    }

    public String getRegGeoLongitudPS_Form() {
        return regGeoLongitudPS_Form;
    }

    public void setRegGeoLongitudPS_Form(String regGeoLongitudPS_Form) {
        this.regGeoLongitudPS_Form = regGeoLongitudPS_Form;
    }

    public List<TipoSeguridad> getRegTipLocalLPSList_Form() {
        return regTipLocalLPSList_Form;
    }

    public void setRegTipLocalLPSList_Form(List<TipoSeguridad> regTipLocalLPSList_Form) {
        this.regTipLocalLPSList_Form = regTipLocalLPSList_Form;
    }

    public TipoSeguridad getRegTipLocalLPSSelected_Form() {
        return regTipLocalLPSSelected_Form;
    }

    public void setRegTipLocalLPSSelected_Form(TipoSeguridad regTipLocalLPSSelected_Form) {
        this.regTipLocalLPSSelected_Form = regTipLocalLPSSelected_Form;
    }

    public List<TipoSeguridad> getRegTipUsoLPSList_Form() {
        return regTipUsoLPSList_Form;
    }

    public void setRegTipUsoLPSList_Form(List<TipoSeguridad> regTipUsoLPSList_Form) {
        this.regTipUsoLPSList_Form = regTipUsoLPSList_Form;
    }

    public TipoSeguridad getRegTipUsoLPSSelected_Form() {
        return regTipUsoLPSSelected_Form;
    }

    public void setRegTipUsoLPSSelected_Form(TipoSeguridad regTipUsoLPSSelected_Form) {
        this.regTipUsoLPSSelected_Form = regTipUsoLPSSelected_Form;
    }

    public SspRegistro getRegistroN2() {
        return registroN2;
    }

    public void setRegistroN2(SspRegistro registroN2) {
        this.registroN2 = registroN2;
    }

    public SspLocalAutorizacion getLocalAutorizacionN2() {
        return localAutorizacionN2;
    }

    public void setLocalAutorizacionN2(SspLocalAutorizacion localAutorizacionN2) {
        this.localAutorizacionN2 = localAutorizacionN2;
    }

    public List<SspAlmacen> getLstAlmacenesList() {
        return lstAlmacenesList;
    }

    public void setLstAlmacenesList(List<SspAlmacen> lstAlmacenesList) {
        this.lstAlmacenesList = lstAlmacenesList;
    }

    public List<SspContacto> getLstContactosList() {
        return lstContactosList;
    }

    public void setLstContactosList(List<SspContacto> lstContactosList) {
        this.lstContactosList = lstContactosList;
    }

    public List<TipoBaseGt> getRegTipoVisacionList() {
        return regTipoVisacionList;
    }

    public void setRegTipoVisacionList(List<TipoBaseGt> regTipoVisacionList) {
        this.regTipoVisacionList = regTipoVisacionList;
    }

    public TipoBaseGt getRegTipoVisacionSelected() {
        return regTipoVisacionSelected;
    }

    public void setRegTipoVisacionSelected(TipoBaseGt regTipoVisacionSelected) {
        this.regTipoVisacionSelected = regTipoVisacionSelected;
    }

    public String getRegVizacNro() {
        return regVizacNro;
    }

    public void setRegVizacNro(String regVizacNro) {
        this.regVizacNro = regVizacNro;
    }

    public String getRegVizacNroOficio() {
        return regVizacNroOficio;
    }

    public void setRegVizacNroOficio(String regVizacNroOficio) {
        this.regVizacNroOficio = regVizacNroOficio;
    }

    public SspVisacion getVizacion() {
        return vizacion;
    }

    public void setVizacion(SspVisacion vizacion) {
        this.vizacion = vizacion;
    }

    public boolean isTermCondic_CambRepresLegal() {
        return termCondic_CambRepresLegal;
    }

    public void setTermCondic_CambRepresLegal(boolean termCondic_CambRepresLegal) {
        this.termCondic_CambRepresLegal = termCondic_CambRepresLegal;
    }

    public String getNroAutoBuscarACRL() {
        return nroAutoBuscarACRL;
    }

    public void setNroAutoBuscarACRL(String nroAutoBuscarACRL) {
        this.nroAutoBuscarACRL = nroAutoBuscarACRL;
    }

    public boolean isHabilitarFormaTipServACRL() {
        return habilitarFormaTipServACRL;
    }

    public void setHabilitarFormaTipServACRL(boolean habilitarFormaTipServACRL) {
        this.habilitarFormaTipServACRL = habilitarFormaTipServACRL;
    }

    public MapModel getEmptyModelACRL() {
        return emptyModelACRL;
    }

    public void setEmptyModelACRL(MapModel emptyModelACRL) {
        this.emptyModelACRL = emptyModelACRL;
    }

    public Marker getMarkerACRL() {
        return markerACRL;
    }

    public void setMarkerACRL(Marker markerACRL) {
        this.markerACRL = markerACRL;
    }

    public UploadedFile getFileLocal() {
        return fileLocal;
    }

    public void setFileLocal(UploadedFile fileLocal) {
        this.fileLocal = fileLocal;
    }

    public byte[] getLocalByte() {
        return localByte;
    }

    public void setLocalByte(byte[] localByte) {
        this.localByte = localByte;
    }

    public StreamedContent getArchivoLocal() {
        return archivoLocal;
    }

    public void setArchivoLocal(StreamedContent archivoLocal) {
        this.archivoLocal = archivoLocal;
    }

    public String getNomArchivoLocal() {
        return nomArchivoLocal;
    }

    public void setNomArchivoLocal(String nomArchivoLocal) {
        this.nomArchivoLocal = nomArchivoLocal;
    }

    public UploadedFile getFileSocio() {
        return fileSocio;
    }

    public void setFileSocio(UploadedFile fileSocio) {
        this.fileSocio = fileSocio;
    }

    public byte[] getSocioByte() {
        return socioByte;
    }

    public void setSocioByte(byte[] socioByte) {
        this.socioByte = socioByte;
    }

    public StreamedContent getArchivoSocio() {
        return archivoSocio;
    }

    public void setArchivoSocio(StreamedContent archivoSocio) {
        this.archivoSocio = archivoSocio;
    }

    public String getNomArchivoSocio() {
        return nomArchivoSocio;
    }

    public void setNomArchivoSocio(String nomArchivoSocio) {
        this.nomArchivoSocio = nomArchivoSocio;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public boolean isRegDisabledFormResolucionRL() {
        return regDisabledFormResolucionRL;
    }

    public void setRegDisabledFormResolucionRL(boolean regDisabledFormResolucionRL) {
        this.regDisabledFormResolucionRL = regDisabledFormResolucionRL;
    }

    public boolean isBlnProcesoTransmision() {
        return blnProcesoTransmision;
    }

    public void setBlnProcesoTransmision(boolean blnProcesoTransmision) {
        this.blnProcesoTransmision = blnProcesoTransmision;
    }

    public Integer getTotalProcesados() {
        return totalProcesados;
    }

    public void setTotalProcesados(Integer totalProcesados) {
        this.totalProcesados = totalProcesados;
    }

    public Integer getActualProcesado() {
        return actualProcesado;
    }

    public void setActualProcesado(Integer actualProcesado) {
        this.actualProcesado = actualProcesado;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public UploadedFile getFileFOTO() {
        return fileFOTO;
    }

    public void setFileFOTO(UploadedFile fileFOTO) {
        this.fileFOTO = fileFOTO;
    }

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public List<SspCefoespInstructor> getLstCapacitadores() {
        return lstCapacitadores;
    }

    public void setLstCapacitadores(List<SspCefoespInstructor> lstCapacitador) {
        this.lstCapacitadores = lstCapacitador;
    }

    public StreamedContent getArchivoFOTO() {
        try {
            if (fotoByte != null) {
                InputStream is = new ByteArrayInputStream(fotoByte);
                archivoFOTO = new DefaultStreamedContent(is, "image/jpeg", "foto");
            } else if (fileFOTO != null) {
                fotoByte = IOUtils.toByteArray(fileFOTO.getInputstream());
                archivoFOTO = new DefaultStreamedContent(fileFOTO.getInputstream(), "image/jpeg");
            } else {
                return new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg"), "image/jpeg");
            }
        } catch (Exception ex) {
            fotoByte = null;
            archivoFOTO = null;
        }

        return archivoFOTO;
    }

    public void setArchivoFOTO(StreamedContent archivoFOTO) {
        this.archivoFOTO = archivoFOTO;
    }

    public String getNomArchivoFOTO() {
        return nomArchivoFOTO;
    }

    public void setNomArchivoFOTO(String nomArchivoFOTO) {
        this.nomArchivoFOTO = nomArchivoFOTO;
    }

    public boolean isHabilitarAdjuntarFoto() {
        return habilitarAdjuntarFoto;
    }

    public void setHabilitarAdjuntarFoto(boolean habilitarAdjuntarFoto) {
        this.habilitarAdjuntarFoto = habilitarAdjuntarFoto;
    }

    public boolean isHabilitarAdjuntarCertFiscoMntal() {
        return habilitarAdjuntarCertFiscoMntal;
    }

    public void setHabilitarAdjuntarCertFiscoMntal(boolean habilitarAdjuntarCertFiscoMntal) {
        this.habilitarAdjuntarCertFiscoMntal = habilitarAdjuntarCertFiscoMntal;
    }

    public UploadedFile getFileCertFiscoMntal() {
        return fileCertFiscoMntal;
    }

    public void setFileCertFiscoMntal(UploadedFile fileCertFiscoMntal) {
        this.fileCertFiscoMntal = fileCertFiscoMntal;
    }

    public byte[] getCertFiscoMntalByte() {
        return certFiscoMntalByte;
    }

    public void setCertFiscoMntalByte(byte[] certFiscoMntalByte) {
        this.certFiscoMntalByte = certFiscoMntalByte;
    }

    public StreamedContent getArchivoCertFiscoMntal() {
        return archivoCertFiscoMntal;
    }

    public void setArchivoCertFiscoMntal(StreamedContent archivoCertFiscoMntal) {
        this.archivoCertFiscoMntal = archivoCertFiscoMntal;
    }

    public String getNomArchivoCertFiscoMntal() {
        return nomArchivoCertFiscoMntal;
    }

    public void setNomArchivoCertFiscoMntal(String nomArchivoCertFiscoMntal) {
        this.nomArchivoCertFiscoMntal = nomArchivoCertFiscoMntal;
    }

    public boolean isHabilitarAdjuntarCopyContrato() {
        return habilitarAdjuntarCopyContrato;
    }

    public void setHabilitarAdjuntarCopyContrato(boolean habilitarAdjuntarCopyContrato) {
        this.habilitarAdjuntarCopyContrato = habilitarAdjuntarCopyContrato;
    }

    public UploadedFile getFileCopyContrato() {
        return fileCopyContrato;
    }

    public void setFileCopyContrato(UploadedFile fileCopyContrato) {
        this.fileCopyContrato = fileCopyContrato;
    }

    public byte[] getCopyContratoByte() {
        return copyContratoByte;
    }

    public void setCopyContratoByte(byte[] copyContratoByte) {
        this.copyContratoByte = copyContratoByte;
    }

    public StreamedContent getArchivoCopyContrato() {
        return archivoCopyContrato;
    }

    public void setArchivoCopyContrato(StreamedContent archivoCopyContrato) {
        this.archivoCopyContrato = archivoCopyContrato;
    }

    public String getNomArchivoCopyContrato() {
        return nomArchivoCopyContrato;
    }

    public void setNomArchivoCopyContrato(String nomArchivoCopyContrato) {
        this.nomArchivoCopyContrato = nomArchivoCopyContrato;
    }

    public boolean isHabilitarAdjuntarCopyPolizaSeguro() {
        return habilitarAdjuntarCopyPolizaSeguro;
    }

    public void setHabilitarAdjuntarCopyPolizaSeguro(boolean habilitarAdjuntarCopyPolizaSeguro) {
        this.habilitarAdjuntarCopyPolizaSeguro = habilitarAdjuntarCopyPolizaSeguro;
    }

    public UploadedFile getFileCopyPolizaSeguro() {
        return fileCopyPolizaSeguro;
    }

    public void setFileCopyPolizaSeguro(UploadedFile fileCopyPolizaSeguro) {
        this.fileCopyPolizaSeguro = fileCopyPolizaSeguro;
    }

    public byte[] getCopyPolizaSeguroByte() {
        return copyPolizaSeguroByte;
    }

    public void setCopyPolizaSeguroByte(byte[] copyPolizaSeguroByte) {
        this.copyPolizaSeguroByte = copyPolizaSeguroByte;
    }

    public StreamedContent getArchivoCopyPolizaSeguro() {
        return archivoCopyPolizaSeguro;
    }

    public void setArchivoCopyPolizaSeguro(StreamedContent archivoCopyPolizaSeguro) {
        this.archivoCopyPolizaSeguro = archivoCopyPolizaSeguro;
    }

    public String getNomArchivoCopyPolizaSeguro() {
        return nomArchivoCopyPolizaSeguro;
    }

    public void setNomArchivoCopyPolizaSeguro(String nomArchivoCopyPolizaSeguro) {
        this.nomArchivoCopyPolizaSeguro = nomArchivoCopyPolizaSeguro;
    }

    public boolean isHabilitarAdjuntarCopyPolizaSegVida() {
        return habilitarAdjuntarCopyPolizaSegVida;
    }

    public void setHabilitarAdjuntarCopyPolizaSegVida(boolean habilitarAdjuntarCopyPolizaSegVida) {
        this.habilitarAdjuntarCopyPolizaSegVida = habilitarAdjuntarCopyPolizaSegVida;
    }

    public UploadedFile getFileCopyPolizaSegVida() {
        return fileCopyPolizaSegVida;
    }

    public void setFileCopyPolizaSegVida(UploadedFile fileCopyPolizaSegVida) {
        this.fileCopyPolizaSegVida = fileCopyPolizaSegVida;
    }

    public byte[] getCopyPolizaSegVidaByte() {
        return copyPolizaSegVidaByte;
    }

    public void setCopyPolizaSegVidaByte(byte[] copyPolizaSegVidaByte) {
        this.copyPolizaSegVidaByte = copyPolizaSegVidaByte;
    }

    public StreamedContent getArchivoCopyPolizaSegVida() {
        return archivoCopyPolizaSegVida;
    }

    public void setArchivoCopyPolizaSegVida(StreamedContent archivoCopyPolizaSegVida) {
        this.archivoCopyPolizaSegVida = archivoCopyPolizaSegVida;
    }

    public String getNomArchivoCopyPolizaSegVida() {
        return nomArchivoCopyPolizaSegVida;
    }

    public void setNomArchivoCopyPolizaSegVida(String nomArchivoCopyPolizaSegVida) {
        this.nomArchivoCopyPolizaSegVida = nomArchivoCopyPolizaSegVida;
    }

    public boolean isHabilitarRepresentanteLegal() {
        return habilitarRepresentanteLegal;
    }

    public void setHabilitarRepresentanteLegal(boolean habilitarRepresentanteLegal) {
        this.habilitarRepresentanteLegal = habilitarRepresentanteLegal;
    }

    public boolean isHabilitarMediosContactos() {
        return habilitarMediosContactos;
    }

    public void setHabilitarMediosContactos(boolean habilitarMediosContactos) {
        this.habilitarMediosContactos = habilitarMediosContactos;
    }

    public boolean isHabilitarLocalPrestacionServicio() {
        return habilitarLocalPrestacionServicio;
    }

    public void setHabilitarLocalPrestacionServicio(boolean habilitarLocalPrestacionServicio) {
        this.habilitarLocalPrestacionServicio = habilitarLocalPrestacionServicio;
    }

    public boolean isHabilitarLicenciaMunicipal() {
        return habilitarLicenciaMunicipal;
    }

    public void setHabilitarLicenciaMunicipal(boolean habilitarLicenciaMunicipal) {
        this.habilitarLicenciaMunicipal = habilitarLicenciaMunicipal;
    }

    public boolean isHabilitarAccionistas() {
        return habilitarAccionistas;
    }

    public void setHabilitarAccionistas(boolean habilitarAccionistas) {
        this.habilitarAccionistas = habilitarAccionistas;
    }

    public boolean isHabilitarCartaFianza() {
        return habilitarCartaFianza;
    }

    public void setHabilitarCartaFianza(boolean habilitarCartaFianza) {
        this.habilitarCartaFianza = habilitarCartaFianza;
    }

    public boolean isAdministradoConRUC() {
        return administradoConRUC;
    }

    public void setAdministradoConRUC(boolean administradoConRUC) {
        this.administradoConRUC = administradoConRUC;
    }

    public boolean isNoAntecedentesPenales() {
        return noAntecedentesPenales;
    }

    public void setNoAntecedentesPenales(boolean noAntecedentesPenales) {
        this.noAntecedentesPenales = noAntecedentesPenales;
    }

    public boolean isNoLaboresEntidad() {
        return noLaboresEntidad;
    }

    public void setNoLaboresEntidad(boolean noLaboresEntidad) {
        this.noLaboresEntidad = noLaboresEntidad;
    }

    public boolean isNoEmpresaSeguridad() {
        return noEmpresaSeguridad;
    }

    public void setNoEmpresaSeguridad(boolean noEmpresaSeguridad) {
        this.noEmpresaSeguridad = noEmpresaSeguridad;
    }

    public List<TipoBaseGt> getRegTipoRegistroList() {
        return regTipoRegistroList;
    }

    public void setRegTipoRegistroList(List<TipoBaseGt> regTipoRegistroList) {
        this.regTipoRegistroList = regTipoRegistroList;
    }

    public TipoBaseGt getRegTipoRegistroSelected() {
        return regTipoRegistroSelected;
    }

    public void setRegTipoRegistroSelected(TipoBaseGt regTipoRegistroSelected) {
        this.regTipoRegistroSelected = regTipoRegistroSelected;
    }

    public List<TipoBaseGt> getRegTipoOperacionList() {
        return regTipoOperacionList;
    }

    public void setRegTipoOperacionList(List<TipoBaseGt> regTipoOperacionList) {
        this.regTipoOperacionList = regTipoOperacionList;
    }

    public TipoBaseGt getRegTipoOperacionSelected() {
        return regTipoOperacionSelected;
    }

    public void setRegTipoOperacionSelected(TipoBaseGt regTipoOperacionSelected) {
        this.regTipoOperacionSelected = regTipoOperacionSelected;
    }

    public List<SspContacto> getListaUpdateContactosActivos() {
        return listaUpdateContactosActivos;
    }

    public void setListaUpdateContactosActivos(List<SspContacto> listaUpdateContactosActivos) {
        this.listaUpdateContactosActivos = listaUpdateContactosActivos;
    }

    public List<SspParticipante> getListaUpdateParticipanteActivos() {
        return listaUpdateParticipanteActivos;
    }

    public void setListaUpdateParticipanteActivos(List<SspParticipante> listaUpdateParticipanteActivos) {
        this.listaUpdateParticipanteActivos = listaUpdateParticipanteActivos;
    }

    public String getNomArchivoCFAnterior() {
        return nomArchivoCFAnterior;
    }

    public void setNomArchivoCFAnterior(String nomArchivoCFAnterior) {
        this.nomArchivoCFAnterior = nomArchivoCFAnterior;
    }

    public String getNomArchivoLMAnterior() {
        return nomArchivoLMAnterior;
    }

    public void setNomArchivoLMAnterior(String nomArchivoLMAnterior) {
        this.nomArchivoLMAnterior = nomArchivoLMAnterior;
    }

    public String getNomArchivoLocalAnterior() {
        return nomArchivoLocalAnterior;
    }

    public void setNomArchivoLocalAnterior(String nomArchivoLocalAnterior) {
        this.nomArchivoLocalAnterior = nomArchivoLocalAnterior;
    }

    public SspLocalAutorizacion getLocalAutorizacionAnterior() {
        return localAutorizacionAnterior;
    }

    public void setLocalAutorizacionAnterior(SspLocalAutorizacion localAutorizacionAnterior) {
        this.localAutorizacionAnterior = localAutorizacionAnterior;
    }

    public SspLocalAutorizacion getLocalAutorizacionAnterior2() {
        return localAutorizacionAnterior2;
    }

    public void setLocalAutorizacionAnterior2(SspLocalAutorizacion localAutorizacionAnterior2) {
        this.localAutorizacionAnterior2 = localAutorizacionAnterior2;
    }
    
    public List<SspRegistroEvento> getLstObservaciones() {
        return lstObservaciones;
    }

    public void setLstObservaciones(List<SspRegistroEvento> lstObservaciones) {
        this.lstObservaciones = lstObservaciones;
    }

    public String getNomArchivoPE() {
        return nomArchivoPE;
    }

    public void setNomArchivoPE(String nomArchivoPE) {
        this.nomArchivoPE = nomArchivoPE;
    }

    public List<SspRegistroEvento> getLstObservacionesOrdenadasDesc() {
        if (lstObservaciones != null) {
            Collections.sort(lstObservaciones, new Comparator<SspRegistroEvento>() {
                @Override
                public int compare(SspRegistroEvento one, SspRegistroEvento other) {
                    return other.getFecha().compareTo(one.getFecha());
                }
            });
        }
        return lstObservaciones;
    }

    public BigDecimal getRegMontoAportante() {
        return regMontoAportante;
    }

    public void setRegMontoAportante(BigDecimal regMontoAportante) {
        this.regMontoAportante = regMontoAportante;
    }

    public int getCheckTipoAporte() {
        return checkTipoAporte;
    }

    public void setCheckTipoAporte(int checkTipoAporte) {
        this.checkTipoAporte = checkTipoAporte;
    }

    public boolean isRegDisabledMontoAportante() {
        return regDisabledMontoAportante;
    }

    public void setRegDisabledMontoAportante(boolean regDisabledMontoAportante) {
        this.regDisabledMontoAportante = regDisabledMontoAportante;
    }

    public int getCheckTipoAcciones() {
        return checkTipoAcciones;
    }

    public void setCheckTipoAcciones(int checkTipoAcciones) {
        this.checkTipoAcciones = checkTipoAcciones;
    }

    public BigDecimal getRegNumeroAcciones() {
        return regNumeroAcciones;
    }

    public void setRegNumeroAcciones(BigDecimal regNumeroAcciones) {
        this.regNumeroAcciones = regNumeroAcciones;
    }

    public BigDecimal getRegPorcentajeAcciones() {
        return regPorcentajeAcciones;
    }

    public void setRegPorcentajeAcciones(BigDecimal regPorcentajeAcciones) {
        this.regPorcentajeAcciones = regPorcentajeAcciones;
    }

    public boolean isRegDisabledNumerojeAcciones() {
        return regDisabledNumerojeAcciones;
    }

    public void setRegDisabledNumerojeAcciones(boolean regDisabledNumerojeAcciones) {
        this.regDisabledNumerojeAcciones = regDisabledNumerojeAcciones;
    }

    public boolean isRegDisabledPorcentajeAcciones() {
        return regDisabledPorcentajeAcciones;
    }

    public void setRegDisabledPorcentajeAcciones(boolean regDisabledPorcentajeAcciones) {
        this.regDisabledPorcentajeAcciones = regDisabledPorcentajeAcciones;
    }

    public boolean isBuscaNuevoModalRepresentanteLegal() {
        return buscaNuevoModalRepresentanteLegal;
    }

    public void setBuscaNuevoModalRepresentanteLegal(boolean buscaNuevoModalRepresentanteLegal) {
        this.buscaNuevoModalRepresentanteLegal = buscaNuevoModalRepresentanteLegal;
    }

    public boolean isBuscaNuevoModalAccionista() {
        return buscaNuevoModalAccionista;
    }

    public void setBuscaNuevoModalAccionista(boolean buscaNuevoModalAccionista) {
        this.buscaNuevoModalAccionista = buscaNuevoModalAccionista;
    }

    public String getTituloBuscaModalRepresentanteLegal() {
        return tituloBuscaModalRepresentanteLegal;
    }

    public void setTituloBuscaModalRepresentanteLegal(String tituloBuscaModalRepresentanteLegal) {
        this.tituloBuscaModalRepresentanteLegal = tituloBuscaModalRepresentanteLegal;
    }

    public Date getFechaMinimaCalendario() {
        return fechaMinimaCalendario;
    }

    public void setFechaMinimaCalendario(Date fechaMinimaCalendario) {
        this.fechaMinimaCalendario = fechaMinimaCalendario;
    }

    public Date getFechaMaximaCalendario() {
        return fechaMaximaCalendario;
    }

    public void setFechaMaximaCalendario(Date fechaMaximaCalendario) {
        this.fechaMaximaCalendario = fechaMaximaCalendario;
    }

    public Date getFechaHoy() {
        return fechaHoy;
    }

    public void setFechaHoy(Date fechaHoy) {
        this.fechaHoy = fechaHoy;
    }

    public String getRegtextoCorreoContactoLPS() {
        return regtextoCorreoContactoLPS;
    }

    public void setRegtextoCorreoContactoLPS(String regtextoCorreoContactoLPS) {
        this.regtextoCorreoContactoLPS = regtextoCorreoContactoLPS;
    }

    public String getCantNumerosDniCarnetTipoDocRepresentanteLegal() {
        return cantNumerosDniCarnetTipoDocRepresentanteLegal;
    }

    public void setCantNumerosDniCarnetTipoDocRepresentanteLegal(String cantNumerosDniCarnetTipoDocRepresentanteLegal) {
        this.cantNumerosDniCarnetTipoDocRepresentanteLegal = cantNumerosDniCarnetTipoDocRepresentanteLegal;
    }

    public Date getRegRepresentanteFechaNacimiento() {
        return regRepresentanteFechaNacimiento;
    }

    public void setRegRepresentanteFechaNacimiento(Date regRepresentanteFechaNacimiento) {
        this.regRepresentanteFechaNacimiento = regRepresentanteFechaNacimiento;
    }

    public boolean isRegDisabledRepresentanteFechaNacimiento() {
        return regDisabledRepresentanteFechaNacimiento;
    }

    public void setRegDisabledRepresentanteFechaNacimiento(boolean regDisabledRepresentanteFechaNacimiento) {
        this.regDisabledRepresentanteFechaNacimiento = regDisabledRepresentanteFechaNacimiento;
    }

    public Date getFechaMayorDeEdadCalendario() {
        return fechaMayorDeEdadCalendario;
    }

    public void setFechaMayorDeEdadCalendario(Date fechaMayorDeEdadCalendario) {
        this.fechaMayorDeEdadCalendario = fechaMayorDeEdadCalendario;
    }

    public List<SspCartaFianza> getRegBuscaCartaFianzaList() {
        return regBuscaCartaFianzaList;
    }

    public void setRegBuscaCartaFianzaList(List<SspCartaFianza> regBuscaCartaFianzaList) {
        this.regBuscaCartaFianzaList = regBuscaCartaFianzaList;
    }

    public boolean isRegDisabledListaCartaFianza() {
        return regDisabledListaCartaFianza;
    }

    public void setRegDisabledListaCartaFianza(boolean regDisabledListaCartaFianza) {
        this.regDisabledListaCartaFianza = regDisabledListaCartaFianza;
    }

    public boolean isRegDisabledDatosCartaFianza() {
        return regDisabledDatosCartaFianza;
    }

    public void setRegDisabledDatosCartaFianza(boolean regDisabledDatosCartaFianza) {
        this.regDisabledDatosCartaFianza = regDisabledDatosCartaFianza;
    }

    public String getRegBuscaCartaFianzaSelectedString() {
        return regBuscaCartaFianzaSelectedString;
    }

    public void setRegBuscaCartaFianzaSelectedString(String regBuscaCartaFianzaSelectedString) {
        this.regBuscaCartaFianzaSelectedString = regBuscaCartaFianzaSelectedString;
    }

    public int getCantNumerosMinimaCarnetTipoDocRepresentanteLegal() {
        return cantNumerosMinimaCarnetTipoDocRepresentanteLegal;
    }

    public void setCantNumerosMinimaCarnetTipoDocRepresentanteLegal(int cantNumerosMinimaCarnetTipoDocRepresentanteLegal) {
        this.cantNumerosMinimaCarnetTipoDocRepresentanteLegal = cantNumerosMinimaCarnetTipoDocRepresentanteLegal;
    }

    public List<SspParticipante> getLstPersonasDJ() {
        return lstPersonasDJ;
    }

    public void setLstPersonasDJ(List<SspParticipante> lstPersonasDJ) {
        this.lstPersonasDJ = lstPersonasDJ;
    }

    public boolean isRegBuscaDepartamentoPrincipal() {
        return regBuscaDepartamentoPrincipal;
    }

    public void setRegBuscaDepartamentoPrincipal(boolean regBuscaDepartamentoPrincipal) {
        this.regBuscaDepartamentoPrincipal = regBuscaDepartamentoPrincipal;
    }

    public boolean isGrabaSolicitud() {
        return grabaSolicitud;
    }

    public void setGrabaSolicitud(boolean grabaSolicitud) {
        this.grabaSolicitud = grabaSolicitud;
    }

    public SspResolucion getResolucionPrincipal() {
        return resolucionPrincipal;
    }

    public void setResolucionPrincipal(SspResolucion resolucionPrincipal) {
        this.resolucionPrincipal = resolucionPrincipal;
    }

    public boolean isLocalPrincipalTransmitir() {
        return localPrincipalTransmitir;
    }

    public void setLocalPrincipalTransmitir(boolean localPrincipalTransmitir) {
        this.localPrincipalTransmitir = localPrincipalTransmitir;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<SbDistritoGt> getRegDistritoRLSList() {
        return regDistritoRLSList;
    }

    public void setRegDistritoRLSList(List<SbDistritoGt> regDistritoRLSList) {
        this.regDistritoRLSList = regDistritoRLSList;
    }

    public SbDistritoGt getRegDistritoRLSelected() {
        return regDistritoRLSelected;
    }

    public void setRegDistritoRLSelected(SbDistritoGt regDistritoRLSelected) {
        this.regDistritoRLSelected = regDistritoRLSelected;
    }

    public List<PersonaDetalle> getPersonaDetalleListado() {
        return personaDetalleListado;
    }

    public void setPersonaDetalleListado(List<PersonaDetalle> personaDetalleListado) {
        this.personaDetalleListado = personaDetalleListado;
    }

    public String getPersonaDetalleSelectedString() {
        return personaDetalleSelectedString;
    }

    public void setPersonaDetalleSelectedString(String personaDetalleSelectedString) {
        this.personaDetalleSelectedString = personaDetalleSelectedString;
    }

    public SbPersonaGt getRepresentanteLegalVer() {
        return representanteLegalVer;
    }

    public void setRepresentanteLegalVer(SbPersonaGt representanteLegalVer) {
        this.representanteLegalVer = representanteLegalVer;
    }

    public String getRegRLPartidaRegistralVer() {
        return regRLPartidaRegistralVer;
    }

    public void setRegRLPartidaRegistralVer(String regRLPartidaRegistralVer) {
        this.regRLPartidaRegistralVer = regRLPartidaRegistralVer;
    }

    public String getRegRLAsientoRegistralVer() {
        return regRLAsientoRegistralVer;
    }

    public void setRegRLAsientoRegistralVer(String regRLAsientoRegistralVer) {
        this.regRLAsientoRegistralVer = regRLAsientoRegistralVer;
    }

    public List<TipoBaseGt> getRegZonaRegListRLVer() {
        return regZonaRegListRLVer;
    }

    public void setRegZonaRegListRLVer(List<TipoBaseGt> regZonaRegListRLVer) {
        this.regZonaRegListRLVer = regZonaRegListRLVer;
    }

    public TipoBaseGt getRegZonaRegSelectedRLVer() {
        return regZonaRegSelectedRLVer;
    }

    public void setRegZonaRegSelectedRLVer(TipoBaseGt regZonaRegSelectedRLVer) {
        this.regZonaRegSelectedRLVer = regZonaRegSelectedRLVer;
    }

    public List<TipoBaseGt> getRegOficinaRegListRLVer() {
        return regOficinaRegListRLVer;
    }

    public void setRegOficinaRegListRLVer(List<TipoBaseGt> regOficinaRegListRLVer) {
        this.regOficinaRegListRLVer = regOficinaRegListRLVer;
    }

    public TipoBaseGt getRegOficinaRegSelectedRLVer() {
        return regOficinaRegSelectedRLVer;
    }

    public void setRegOficinaRegSelectedRLVer(TipoBaseGt regOficinaRegSelectedRLVer) {
        this.regOficinaRegSelectedRLVer = regOficinaRegSelectedRLVer;
    }

    public List<TipoBaseGt> getRegTipoViasListVer() {
        return regTipoViasListVer;
    }

    public void setRegTipoViasListVer(List<TipoBaseGt> regTipoViasListVer) {
        this.regTipoViasListVer = regTipoViasListVer;
    }

    public TipoBaseGt getRegTipoViasSelectedVer() {
        return regTipoViasSelectedVer;
    }

    public void setRegTipoViasSelectedVer(TipoBaseGt regTipoViasSelectedVer) {
        this.regTipoViasSelectedVer = regTipoViasSelectedVer;
    }

    public String getRegDomicilioRLSelectedVer() {
        return regDomicilioRLSelectedVer;
    }

    public void setRegDomicilioRLSelectedVer(String regDomicilioRLSelectedVer) {
        this.regDomicilioRLSelectedVer = regDomicilioRLSelectedVer;
    }

    public List<SbDistritoGt> getRegDistritoRLSListVer() {
        return regDistritoRLSListVer;
    }

    public void setRegDistritoRLSListVer(List<SbDistritoGt> regDistritoRLSListVer) {
        this.regDistritoRLSListVer = regDistritoRLSListVer;
    }

    public SbDistritoGt getRegDistritoRLSelectedVer() {
        return regDistritoRLSelectedVer;
    }

    public void setRegDistritoRLSelectedVer(SbDistritoGt regDistritoRLSelectedVer) {
        this.regDistritoRLSelectedVer = regDistritoRLSelectedVer;
    }

    public MapModel getModelMap() {
        return modelMap;
    }

    public void setModelMap(MapModel modelMap) {
        this.modelMap = modelMap;
    }

    public Marker getMarkerSelected() {
        return markerSelected;
    }

    public void setMarkerSelected(Marker markerSelected) {
        this.markerSelected = markerSelected;
    }

    public String getGoogle_srcapi_key() {
        return google_srcapi_key;
    }

    public void setGoogle_srcapi_key(String google_srcapi_key) {
        this.google_srcapi_key = google_srcapi_key;
    }

    public String getGoogle_jsapi() {
        return google_jsapi;
    }

    public void setGoogle_jsapi(String google_jsapi) {
        this.google_jsapi = google_jsapi;
    }

    public List<TipoBaseGt> getRegTipoDocCapList() {
        return regTipoDocCapList;
    }

    public TipoBaseGt getRegTipoDocCapSelected() {
        return regTipoDocCapSelected;
    }

    public void setRegTipoDocCapSelected(TipoBaseGt regTipoDocCapSelected) {
        this.regTipoDocCapSelected = regTipoDocCapSelected;
    }

    public void setRegTipoDocCapList(List<TipoBaseGt> regTipoDocCapList) {
        this.regTipoDocCapList = regTipoDocCapList;
    }

    public String getRegNroDocCapBuscar() {
        return regNroDocCapBuscar;
    }

    public void setRegNroDocCapBuscar(String regNroDocCapBuscar) {
        this.regNroDocCapBuscar = regNroDocCapBuscar;
    }

    public String getCantNumerosDniCarnetTipoDocCapacitador() {
        return cantNumerosDniCarnetTipoDocCapacitador;
    }

    public void setCantNumerosDniCarnetTipoDocCapacitador(String cantNumerosDniCarnetTipoDocCapacitador) {
        this.cantNumerosDniCarnetTipoDocCapacitador = cantNumerosDniCarnetTipoDocCapacitador;
    }

    public String getRegNombreCapacitadorEncontrado() {
        return regNombreCapacitadorEncontrado;
    }

    public void setRegNombreCapacitadorEncontrado(String regNombreCapacitadorEncontrado) {
        this.regNombreCapacitadorEncontrado = regNombreCapacitadorEncontrado;
    }

    public Long getContRegCapacitador() {
        return contRegCapacitador;
    }

    public void setContRegCapacitador(Long contRegCapacitador) {
        this.contRegCapacitador = contRegCapacitador;
    }

    public String getNomArchivoPEAnterior() {
        return nomArchivoPEAnterior;
    }

    public void setNomArchivoPEAnterior(String nomArchivoPEAnterior) {
        this.nomArchivoPEAnterior = nomArchivoPEAnterior;
    }

    public SbPersonaGt getRegSbPersonaCapacitador() {
        return regSbPersonaCapacitador;
    }

    public void setRegSbPersonaCapacitador(SbPersonaGt regSbPersonaCapacitador) {
        this.regSbPersonaCapacitador = regSbPersonaCapacitador;
    }

    public SspCarneInstructor getCarneInstructor() {
        return carneInstructor;
    }

    public void setCarne(SspCarneInstructor carneInstructor) {
        this.carneInstructor = carneInstructor;
    }

    public UploadedFile getFileCA() {
        return fileCA;
    }

    public void setFileCA(UploadedFile fileCA) {
        this.fileCA = fileCA;
    }

    public byte[] getCaByte() {
        return caByte;
    }

    public void setCaByte(byte[] caByte) {
        this.caByte = caByte;
    }

    public StreamedContent getArchivoCA() {
        return archivoCA;
    }

    public void setArchivoCA(StreamedContent archivoCA) {
        this.archivoCA = archivoCA;
    }

    public boolean getRenderVerMapa() {
        return renderVerMapa;
    }

    public void setRenderVerMapa(boolean renderVerMapa) {
        this.renderVerMapa = renderVerMapa;
    }

    public Double getLat_x() {
        return lat_x;
    }

    public void setLat_x(Double lat_x) {
        this.lat_x = lat_x;
    }

    public Double getLong_x() {
        return long_x;
    }

    public void setLong_x(Double long_x) {
        this.long_x = long_x;
    }

    public List<SbDistritoGt> getListaDistritoMapaLocal() {
        return listaDistritoMapaLocal;
    }

    public void setListaDistritoMapaLocal(List<SbDistritoGt> listaDistritoMapaLocal) {
        this.listaDistritoMapaLocal = listaDistritoMapaLocal;
    }

    public void agregarListaPersonasDJ() {
        lstPersonasDJ = null;
        lstPersonasDJ = new ArrayList<SspParticipante>();

        SspParticipante detallePersona;

        boolean encontroPersona = false;

        //Primero se agrega al Representante Legal
        detallePersona = new SspParticipante();
        //detallePersona.setId(regPersonasRLSelected.getId());
        //detallePersona.setPersonaId(regPersonasRLSelected);
        detallePersona.setId(Long.parseLong(personaDetalleSelectedString));
        detallePersona.setPersonaId(ejbSbPersonaFacade.find(Long.parseLong(personaDetalleSelectedString)));
        lstPersonasDJ.add(detallePersona);

        for (SspParticipante socioActivo : lstSocios) {
            encontroPersona = false;
            detallePersona = new SspParticipante();
            detallePersona.setId(socioActivo.getId());
            detallePersona.setPersonaId(socioActivo.getPersonaId());

            //Antes de Agregarse Se busca si existe la misma persona en la lista
            //Si tiene datos agregados
            if (lstPersonasDJ.size() > 0) {
                for (SspParticipante personaBusca : lstPersonasDJ) {
                    if (personaBusca.getPersonaId().getNumDoc().equals(detallePersona.getPersonaId().getNumDoc())) {
                        encontroPersona = true;
                        break;
                    }
                }
            }
            //Si no lo encontro, lo agrega
            if (!encontroPersona) {
                lstPersonasDJ.add(detallePersona);
            }
        }
    }

    public void openDlgDeclaracionJurada() {
        try {
            if (validaRegistroSolicitudAutorizacion()) {
                //El botón aceptar esta deshabilitado
                grabaSolicitud = false;
                //Inicializa las marcas de la DJ
                noAntecedentesPenales = false;
                noLaboresEntidad = false;
                noEmpresaSeguridad = false;

                //Se genera la lista de Personas para marcar en la Declaración Jurada
                agregarListaPersonasDJ();

                RequestContext.getCurrentInstance().execute("PF('wvDlgDeclaracionJuradaAcc').show()");
                RequestContext.getCurrentInstance().update("frmDeclaraJuradaAccionista");

            }
        } catch (Exception e) {
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    public void validarDatosCapacitador() {

        boolean validacionCapacitador = true;
        if (regTipoDocCapSelected == null) {
            validacionCapacitador = false;
            JsfUtil.invalidar(obtenerForm() + ":cboCapacitadorTipoDoc");
            JsfUtil.mensajeAdvertencia("Seleccione un tipo de documento");
            RequestContext.getCurrentInstance().update("FormRegSolicAuto");

        } else if (regNroDocCapBuscar == null || regNroDocCapBuscar.equals("")) {
            validacionCapacitador = false;
            JsfUtil.invalidar(obtenerForm() + ":txtCapacitadorNumDoc");
            JsfUtil.mensajeAdvertencia("Ingrese Numero de Documento");
            RequestContext.getCurrentInstance().update("FormRegSolicAuto");
        }

        if (validacionCapacitador == true) {

            List<Map> listPersona = ejbSspCefoespInstructorFacade.verificacionInstructor_ByTipoDocDISCA(regTipoDocCapSelected.getId(), regNroDocCapBuscar);

            if (listPersona != null) {

                Long xIdPersona = Long.parseLong(listPersona.get(0).get("ID").toString());
                regNombreCapacitadorEncontrado = listPersona.get(0).get("APE_PAT") + " " + listPersona.get(0).get("APE_MAT") + " " + listPersona.get(0).get("NOMBRES");
                String xValorPersona = listPersona.get(0).get("AUTORIZADO").toString();

                if (xValorPersona.equals("SI")) {
                    regMsgCapacitador = "ACREDIADO";
                    validacionCapacitador = true;
                    regSbPersonaCapacitador = ejbSbPersonaFacade.obtenerEmpresaId(xIdPersona);
                } else {
                    regMsgCapacitador = "NO-ACREDIADO";
                    validacionCapacitador = false;
                    regSbPersonaCapacitador = null;
                }

            } else {
                validacionCapacitador = false;
                regNombreCapacitadorEncontrado = null;
                JsfUtil.mensajeAdvertencia("La Persona no se encuentra registrado en nuestra base de datos");
            }

        }

    }

    public void agregarCapacitadorAcreditado() {

        if (regSbPersonaCapacitador == null) {
            JsfUtil.mensajeAdvertencia("Busque persona acreditado");
        } else {

            List<Map> listPersona = ejbSspCefoespInstructorFacade.verificacionInstructor_ByTipoDocDISCA(regTipoDocCapSelected.getId(), regNroDocCapBuscar);
            regNombreCapacitadorEncontrado = listPersona.get(0).get("APE_PAT") + " " + listPersona.get(0).get("APE_MAT") + " " + listPersona.get(0).get("NOMBRES");
            String xValorPersona = listPersona.get(0).get("AUTORIZADO").toString();

            //lstCapacitadores = new ArrayList<SspCefoespInstructor>();
            int contadorCapacitadores = lstCapacitadores.size();
            if (xValorPersona.equals("SI")) {

                contadorCapacitadores++;
                SspCefoespInstructor xObjInstructor = new SspCefoespInstructor();
                xObjInstructor.setId(Long.parseLong(contadorCapacitadores + ""));
                xObjInstructor.setPersonaId(regSbPersonaCapacitador);
                xObjInstructor.setRegistroId(null);
                xObjInstructor.setActivo(JsfUtil.TRUE);
                xObjInstructor.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                xObjInstructor.setAudNumIp(JsfUtil.getIpAddress());
                lstCapacitadores.add(xObjInstructor);

                regNombreCapacitadorEncontrado = null;
                regSbPersonaCapacitador = null;
                regNroDocCapBuscar = "";
                regMsgCapacitador = "";

            } else {
                JsfUtil.mensajeAdvertencia("La Persona " + regNombreCapacitadorEncontrado + " NO ESTA ACREDITADO");
            }

        }

    }

    public void agregarTipoSocio() {

        boolean validacionSocio = true;

        /*if(regTipDocAccionistaSelected == null){
            validacionSocio = false;
            JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipDoc");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo documento del socio");
        }
        
        if(regNumeroDocAccionista == null){
            validacionSocio = false;
            JsfUtil.invalidar(obtenerForm() + ":txtAcc_NroDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el número de documento del socio");
        }else{
            if(regNumeroDocAccionista.equals("")){
                validacionSocio = false;
                JsfUtil.invalidar(obtenerForm() + ":txtAcc_NroDoc");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el número de documento del socio");
            }
        }*/
        if (regNombreSocioEncontrado == null) {
            validacionSocio = false;
            JsfUtil.mensajeAdvertencia("Debe de buscar el Documento de la persona para agregarse");
        }

        if (regTipoSocioSelected == null) {
            validacionSocio = false;
            JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipAccionista");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de socio");
        }

        if (regTipoSocioSelected != null) {
            if (regTipoSocioSelected.getCodProg().equals("TP_ACCNSTA_DIRECT")) {
                if (regCargoSocio == null) {
                    validacionSocio = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtAcc_Cargo");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el cargo del socio");
                } else if (regCargoSocio.equals("")) {
                    validacionSocio = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtAcc_Cargo");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el cargo del socio");
                }
            }
        }

        boolean encontroNumeroTipo = false;
        //Si tiene el Numero del documento y el Tipo de Accionista/Socio, valida que no existe uno igual
        if (regNumeroDocAccionista != null && regTipoSocioSelected != null) {
            //Si tiene datos en la lista busca si ya existe
            if (lstSocios != null) {
                for (SspParticipante socioBusca : lstSocios) {
                    if (socioBusca.getPersonaId().getNumDoc().equals(regNumeroDocAccionista) && socioBusca.getTipoParticipacion().equals(regTipoSocioSelected.getId())) {
                        encontroNumeroTipo = true;
                        break;
                    }
                }
            }
        }

        if (encontroNumeroTipo) {
            validacionSocio = false;
            //JsfUtil.invalidar(obtenerForm() + ":txtAcc_NroDoc");
            JsfUtil.mensajeAdvertencia("Ya se ha agregado la persona con el mismo Tipo en la lista");
        }

        //Si esta marcado el Panel Aportes con la opcion Monto
        if (checkTipoAporte == 1) {
            //Verifica que el monto no este vacio
            if (regMontoAportante == null) {
                validacionSocio = false;
                JsfUtil.invalidar(obtenerForm() + ":txtAportante_Monto");
                JsfUtil.mensajeAdvertencia("Debe de registrar el monto del Aporte");
            }

            //Valida si no es nulo y que sea mayor que cero
            if (regMontoAportante != null) {
                if (Double.parseDouble(("" + regMontoAportante).trim()) <= 0) {
                    validacionSocio = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtAportante_Monto");
                    JsfUtil.mensajeAdvertencia("Debe de registrar el monto del Aporte");
                }
            }
        }

        //Si esta marcado el Panel Acciones con la opcion Numero
        if (checkTipoAcciones == 1) {
            //Verifica que el numero de acciones no este vacio
            if (regNumeroAcciones == null) {
                validacionSocio = false;
                JsfUtil.invalidar(obtenerForm() + ":txtNumero_Acciones");
                JsfUtil.mensajeAdvertencia("Debe de registrar el número de Acciones");
            }

            //Valida si no es nulo y que sea mayor que cero
            if (regNumeroAcciones != null) {
                if (Double.parseDouble(("" + regNumeroAcciones).trim()) <= 0) {
                    validacionSocio = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtNumero_Acciones");
                    JsfUtil.mensajeAdvertencia("Debe de registrar el número de Acciones");
                }
            }
        }

        //Si esta marcado el Panel Acciones con la opcion Porcentaje %
        if (checkTipoAcciones == 2) {
            //Verifica que el porcentaje de acciones no este vacio
            if (regPorcentajeAcciones == null) {
                validacionSocio = false;
                JsfUtil.invalidar(obtenerForm() + ":txtPorcentaje_Acciones");
                JsfUtil.mensajeAdvertencia("Debe de registrar el Porcentaje (%) de Acciones");
            }

            //Valida si no es nulo y que sea mayor que cero
            if (regPorcentajeAcciones != null) {
                if (Double.parseDouble(("" + regPorcentajeAcciones).trim()) <= 0) {
                    validacionSocio = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPorcentaje_Acciones");
                    JsfUtil.mensajeAdvertencia("Debe de registrar el Porcentaje (%) de Acciones");
                }
            }
        }

        if (validacionSocio) {
            try {

                SspParticipante ItemSocio = new SspParticipante();
                contRegSocio++;

                ItemSocio.setId(contRegSocio);
                ItemSocio.setPersonaId(regDatosSocioEncontrado);
                ItemSocio.setTipoParticipacion(regTipoSocioSelected.getId());
                if (regCargoSocio != null) {
                    ItemSocio.setCargo(regCargoSocio.toUpperCase());
                } else {
                    ItemSocio.setCargo(null);
                }

                //Si esta marcado el Panel Aportes con la opcion Monto
                if (checkTipoAporte == 1) {
                    ItemSocio.setAporteMonto(regMontoAportante);
                }

                //Si esta marcado el Panel Acciones con la opcion Numero
                if (checkTipoAcciones == 1) {
                    ItemSocio.setNumeroAccion(regNumeroAcciones);
                }

                //Si esta marcado el Panel Acciones con la opcion Porcentaje %
                if (checkTipoAcciones == 2) {
                    ItemSocio.setPorcentajeAccion(regPorcentajeAcciones);
                }

                lstSocios.add(ItemSocio);

                regDatosSocioEncontrado = null;
                regTipDocAccionistaSelected = null;
                regNumeroDocAccionista = null;
                regTipoSocioSelected = null;
                regCargoSocio = null;
                regNombreSocioEncontrado = null;
                checkTipoAporte = 1; //Por defecto Aporte por Monto
                regMontoAportante = null;
                regDisabledMontoAportante = false;
                checkTipoAcciones = 1; //Por defecto Tipo Acciones por Numero
                regNumeroAcciones = null;
                regPorcentajeAcciones = null;
                regDisabledNumerojeAcciones = false;
                regDisabledPorcentajeAcciones = true;

                RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_Accionistas");

            } catch (Exception e) {
                System.out.println("Exception->" + e.getMessage());
            }
        }

    }

    public String obtenerKeyGoogle() {
        if (google_srcapi_key == null || google_srcapi_key.isEmpty()) {
            google_srcapi_key = ejbSbParametroFacade.obtenerParametroXNombre("google_srcapi_key").getValor();
        }
        return google_srcapi_key;
    }

    public String obtenerApiGoogle() {
        if (google_jsapi == null || google_jsapi.isEmpty()) {
            //google_jsapi = ejbSbParametroFacade.obtenerParametroXNombre("google_jsapi").getValor() + "&callback=initMap()";
            //google_jsapi = ejbSbParametroFacade.obtenerParametroXNombre("google_jsapi").getValor() + "&libraries=places&sensor=false";
            google_jsapi = ejbSbParametroFacade.obtenerParametroXNombre("google_jsapi").getValor();
        }
        return google_jsapi;
    }

    public String obtenerMapCenterBuscarMap() {
        LatLng coord1 = null;
        modelMap = new DefaultMapModel();

        //Marker SUCAMEC
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_lima")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_lima")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_TRAM"));

        //Marker Ancash
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_ancash")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_ancash")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_ANCASH"));

        //Marker Arequipa
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_arequipa")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_arequipa")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_AQP"));

        //Marker Cajamarca
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_cajamarca")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_cajamarca")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_CAJAMARCA"));

        //Marker Chiclayo
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_chiclayo")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_chiclayo")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_CHICLA"));

        //Marker Cusco
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_cusco")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_cusco")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_CUSCO"));

        //Marker Ica
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_ica")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_ica")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_ICA"));

        //Marker Junin
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_junin")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_junin")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_JUNIN"));

        //Marker La Libertad
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_laLibertad")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_laLibertad")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_LIBERTA"));

        //Marker Piura
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_piura")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_piura")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_PIURA"));

        //Marker Puno
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_puno")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_puno")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_PUNO"));

        //Marker Tacna
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_tacna")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_tacna")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_TACNA"));

        //Marker Iquitos
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_iquitos")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_iquitos")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_LOR"));

        return JsfUtil.bundleBDIntegrado("google_coordenadas_pointCenter");
    }

    public StreamedContent obtenerArchivoPlanoDistribucion() {
        SspArchivo arch = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_PD");
        if (arch == null) {
            return null;
        }
        StreamedContent r = null;
        try {
            String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_LOC").getValor();
            FileInputStream f = new FileInputStream(path + arch.getNombre());
            r = new DefaultStreamedContent(f, "application/pdf", arch.getNombre());
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }

    public StreamedContent obtenerArchivoLicenciaMunicipal() {
        SspArchivo arch = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_LM");
        if (arch == null) {
            return null;
        }
        StreamedContent r = null;
        try {
            String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_LM").getValor();
            FileInputStream f = new FileInputStream(path + arch.getNombre());
            r = new DefaultStreamedContent(f, "application/pdf", arch.getNombre());
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }

    public StreamedContent obtenerArchivoCartaFianza() {
        //SspArchivo arch = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_CF");
        SspCartaFianza arch = ejbSspCartaFianzaFacade.buscarArchivoCartaFianza(Long.parseLong(regBuscaCartaFianzaSelectedString));
        if (arch == null) {
            return null;
        }
        StreamedContent r = null;
        try {
            String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CF").getValor();
            FileInputStream f = new FileInputStream(path + arch.getNombreArchivo());
            r = new DefaultStreamedContent(f, "application/pdf", arch.getNombreArchivo());
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }

    public void regresarLista(boolean mostrarMsjPendienteTransmitir) {
        estado = EstadoCrud.BUSCAR;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/List.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    public String validarSiguienteSolicAutorizacion() {
    public void validarSiguienteSolicAutorizacion(TipoBaseGt regTipoSeguridadSelected, TipoBaseGt tbTipoRegistroSelected) {

        regTipoSeguridadSelected = regTipoSeguridadSelected;
        if (regTipoSeguridadSelected == null) {
            JsfUtil.mensajeAdvertencia("Es necesario seleccionar tipo de servicio.");
            return;
        } else {
            boolean validacion = true;
            if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_DEP")) {

                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

                regNombreCapacitadorEncontrado = null;
                regMsgCapacitador = null;

                if (validacion == true) {
                    setEstado(EstadoCrud.CREARDEPARTAMENTO);
                    createFormAutorizacionServicioSeguridad();
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    try {
                        ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/departamento/CreateAutorizaServicioSeguridad.xhtml");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }
    }

    public void verificarCheckAporteMontoAccionista() {
        //Si esta marcado Monto
        if (checkTipoAporte == 1) {
            regDisabledMontoAportante = false;
        } else {
            regMontoAportante = null;
            regDisabledMontoAportante = true;
        }
    }

    public void verificarCheckNumeroPorcentajeAcciones() {
        //Si esta marcado por Numero
        if (checkTipoAcciones == 1) {
            regPorcentajeAcciones = null;
            regDisabledNumerojeAcciones = false;
            regDisabledPorcentajeAcciones = true;
        } else {
            //Si esta marcado por Porcentaje
            regNumeroAcciones = null;
            regDisabledNumerojeAcciones = true;
            regDisabledPorcentajeAcciones = false;
        }
    }

    public void calcularFechaMinimaCalendario() {
        Calendar FechaActual = Calendar.getInstance();
        fechaMinimaCalendario = JsfUtil.getFechaSinHora(FechaActual.getTime());
    }

    public void calcularFechaMaximaCalendario() {
        Calendar FechaActual = Calendar.getInstance();
        fechaMaximaCalendario = JsfUtil.getFechaSinHora(FechaActual.getTime());
    }

    public void obtenerNumCaractDocumentoRepresentanteLegal() {
        if (regTipoDocSelected != null) {
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {
                cantNumerosDniCarnetTipoDocRepresentanteLegal = "8";
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Deshabilita para DNI
                regDisabledRepresentanteFechaNacimiento = true;
            } else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                cantNumerosMinimaCarnetTipoDocRepresentanteLegal = 9;
                cantNumerosDniCarnetTipoDocRepresentanteLegal = "12";
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Habilita para Carnet de Extranjeria
                regDisabledRepresentanteFechaNacimiento = false;
            }
        }
    }

    public void obtenerNumCaractDocumentoCapacitador() {
        if (regTipoDocCapSelected != null) {
            if (regTipoDocCapSelected.getCodProg().equals("TP_DOCID_DNI")) {
                cantNumerosDniCarnetTipoDocCapacitador = "8";
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Deshabilita para DNI
                regDisabledRepresentanteFechaNacimiento = true;
            } else if (regTipoDocCapSelected.getCodProg().equals("TP_DOCID_CE")) {
                cantNumerosMinimaCarnetTipoDocRepresentanteLegal = 9;
                cantNumerosDniCarnetTipoDocCapacitador = "12";
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Habilita para Carnet de Extranjeria
                regDisabledRepresentanteFechaNacimiento = false;
            }
        }
    }

    public void editarNumeroDocAccionista() {
        regNombreSocioEncontrado = null;
    }

    public Date getFechaMinMayorEdad() {
        return calculoFechaVencMeses(getFechaHoy(), -12 * 18);
    }

    public Date calculoFechaVencMeses(Date fecha, int meses) {
        String hoy = fechaSimple(fecha);
        String[] dataTemp = hoy.split("/");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(dataTemp[2]), Integer.parseInt(dataTemp[1]) - 1, Integer.parseInt(dataTemp[0]));
        c.add(Calendar.MONTH, meses);
        return c.getTime();
    }

    public String fechaSimple(Date fecha) {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    public boolean renderBtnTransmitir(Map item) {
        boolean validar = false;

        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE") || item.get("ESTADO_CODPROG").equals("TP_ECC_OBS")) {
                validar = true;
            }
        }

        return validar;
    }

    public void listarCartaFianzas() {
        if (regBuscaCartaFianzaList == null) {;
            regBuscaCartaFianzaList = new ArrayList<SspCartaFianza>();
        }
        List<SspCartaFianza> lstRegCarta = new ArrayList<>();
        lstRegCarta = ejbSspCartaFianzaFacade.buscarCartasFianzasVigentes(regAdminist.getId());
        if (lstRegCarta != null) {
            for (SspCartaFianza loDetalle : lstRegCarta) {
                regBuscaCartaFianzaList.add(loDetalle);
            }
        }

    }

    public void buscaCartaFianza() {
        if (regBuscaCartaFianzaSelectedString != null) {
            SspCartaFianza reg = ejbSspCartaFianzaFacade.find(Long.parseLong(regBuscaCartaFianzaSelectedString));
            if (reg != null) {
                regNroCartaFianza = reg.getNumero();
                regTipoFinancieraCFSelected = reg.getTipoFinancieraId();
                listarEntidadFinancieras();
                regEntidadFinancieraCFSelected = reg.getFinancieraId();
                regMontoCartaFianza = reg.getMonto();
                regFechVigenciaIni = reg.getVigenciaInicio();
                regFechVigenciaFin = reg.getVigenciaFin();
                //Siempre el archivo es null ya que el adjuntar contiene los datos del nombre del archivo
                nomArchivoCF = null;
                //El nombre anterior se setea cuando busca el archivo de la carta fianza
                nomArchivoCFAnterior = reg.getNombreArchivo();
                //Si esta Editando
                /*if (registro.getId() != null) {
                    nomArchivoCF = null;
                } else {
                    //Si esta creando el registro
                    nomArchivoCF = reg.getNombreArchivo();
                } */
                cfByte = null;
                //Si es un R7egistro que ya existe, esta editandose
                if (registro.getId() != null) {
                    //Se verifica si es el unico registro que esta relacionado para habilitar edición de los datos de la Carta Fianza
                    regDisabledDatosCartaFianza = false;
                    SspCartaFianza encuentraCarta = ejbSspCartaFianzaFacade.buscarCartaFianzaPorNumero(regAdminist.getId(), regNroCartaFianza.toUpperCase().trim(), registro.getId());
                    if (encuentraCarta != null) {
                        //Existe la carta en otros registros y ya no se puede editar
                        regDisabledDatosCartaFianza = true;
                    }
                } else {
                    //Si es un Registro Nuevo siempre se deshabilita cuando busca una Carta Fianza
                    regDisabledDatosCartaFianza = true;
                }
            } else {
                nuevaCartaFianza();
            }
        } else {
            nuevaCartaFianza();
        }
    }

    public void nuevaCartaFianza() {
        regBuscaCartaFianzaSelectedString = null;
        regDisabledDatosCartaFianza = false;
        regNroCartaFianza = null;
        regTipoFinancieraCFSelected = null;
        regEntidadFinancieraCFSelected = null;
        regMontoCartaFianza = null;
        regFechVigenciaIni = null;
        regFechVigenciaFin = null;
        nomArchivoCF = null;
        cfByte = null;
        RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_CartaFianza");
    }

    public void regresarListaMensaje(boolean mostrarMsjPendienteTransmitir, String mensaje) {
        estado = EstadoCrud.BUSCAR;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/List.xhtml");
            JsfUtil.mensaje(mensaje);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void activaGrabarBotonAceptar() {
        grabaSolicitud = false;

        if (noAntecedentesPenales == true && noLaboresEntidad == true && noEmpresaSeguridad == true) {
            grabaSolicitud = true;
        }

    }

    public void listarDepartamentosDistritoTipoLocal() {

        //Limpia los datos del Panel de Representante Legal Ver - PRINCIPAL
        representanteLegalVer = null;
        regRLPartidaRegistralVer = null;
        regRLAsientoRegistralVer = null;
        //regZonaRegListRLVer = regZonaRegListRL;
        regZonaRegSelectedRLVer = null;
        regOficinaRegListRLVer = null;
        regOficinaRegSelectedRLVer = null;
        //regTipoViasListVer = regTipoViasList;
        regTipoViasSelectedVer = null;
        regDomicilioRLSelectedVer = null;
        //regDistritoRLSListVer = regDistritoRLSList;
        regDistritoRLSelectedVer = null;

        //Busca los Representantes Legal o Responsables Legal
        regRLPartidaRegistral = null;
        regRLAsientoRegistral = null;
        regZonaRegSelectedRL = null;
        regOficinaRegSelectedRL = null;
        regOficinaRegListRL = null;
        regTipoViasSelected = null;
        regDomicilioRLSelected = null;
        regDistritoRLSelected = null;

        personaDetalleSelectedString = null;
        personaDetalleListado = null;
        personaDetalleListado = new ArrayList<PersonaDetalle>();

        TipoBaseGt sTipoBase = new TipoBaseGt();

        sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL
        //Detalle de relacion Persona como Representante Legal o Responsable Legal
        List<SbPersonaGt> listDetallePersona = new ArrayList<>();
        listDetallePersona = ejbSbPersonaFacade.listarPersonaXIdRelacionPersonaUnionSspRegistro(regAdminist.getId(), null, sTipoBase.getId());
        if (listDetallePersona != null) {
            PersonaDetalle detallePersonaTemp;
            for (SbPersonaGt PersonaDetalle : listDetallePersona) {
                detallePersonaTemp = new PersonaDetalle();
                detallePersonaTemp.setId(PersonaDetalle.getId());
                detallePersonaTemp.setNumDoc(PersonaDetalle.getNumDoc());
                detallePersonaTemp.setApePat(PersonaDetalle.getApePat());
                detallePersonaTemp.setApeMat(PersonaDetalle.getApeMat());
                detallePersonaTemp.setNombres(PersonaDetalle.getNombres());
                personaDetalleListado.add(detallePersonaTemp);
            }
        }
        //Fin de Busca los Representantes Legal o Responsables Legal

        //Inicializa los Distritos
        regDistritoLPSSelected = null;
        regDistritoLPSList = null;

        //Inicializa el Listado de Locales
        localAutorizacionSelectedString = null;
        localAutorizacionListado = null;
        localAutorizacionListado = new ArrayList<LocalAutorizacionDetalle>();
        regTipoViasLPSSelected = null;
        regDireccionLPS = null;
        regNroFisicoLPS = null;
        regTipUsoLPSSelected = null;
        regReferenciaLPS = null;
        regBuscaDepartamentoPrincipal = false;

        //Busca los distritos por Tipo de Local PRINCIPAL y SUCURSAL
        resolucionPrincipal = null; //Limpia la Resolución Principal por Departamento
        regDistritoLPSList = ejbSbDistritoFacadeGtFacade.buscarDistritosEmpresaXDepartamentoSolicitudCefoesp(regAdminist.getId(), regTipoSeguridadSelected.getId(), registro.getId());

    }

    public void confirmarDesestimiento() {
        try {
            if (observacion == null || observacion.trim().isEmpty()) {
                JsfUtil.mensajeAdvertencia("Debe de registrar el motivo del desistimiento");
                return;
            }

            observacion = observacion.toUpperCase();
            TipoSeguridad tipo = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_DES");

            if (generaTraza(tipo)) {
                registro.setEstadoId(tipo);
                adicionaEvento(registro, observacion);
                ejbSspRegistroFacade.edit(registro);

                //SE VA GENERAR DESISTIMIENTO PARA CARNE
                if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SISPE") || registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_SISPA")) {
                    registro = registro.getCarneSolicitudId();
                    if (registro != null) {
                        if (generaTraza(tipo)) {
                            registro.setEstadoId(tipo);
                            adicionaEvento(registro, observacion);
                            ejbSspRegistroFacade.edit(registro);
                        }
                    }
                }
                //===================================

                registro = null;
                reiniciarCamposBusqueda();
                resultados = new ArrayList();

                JsfUtil.mensaje("Registro actualizado correctamente.");
                RequestContext.getCurrentInstance().execute("PF('wvConfirmarDesestimiento').hide()");
                RequestContext.getCurrentInstance().update("listForm");
            } else {
                JsfUtil.mensajeError("Hubo un error al archivar el expediente");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    public static String obtnerRutaServer(String directorio) {

//     ServletContext servletContext = (ServletContext)
//    FacesContext.getCurrentInstance().getExternalContext().getContext();
//    String ruta = servletContext.getRealPath("") + File.separator + "images" + File.separator + "prime_logo.png";
//    String ruta = servletContext.getRealPath("") + File.separator + (directorio !=null && directorio != "" ? directorio : "images") + File.separator;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            URL resourceUrl = externalContext.getResource("/resources/");
            if (resourceUrl != null) {
                String fullPath = resourceUrl.getPath();
                int index = fullPath.indexOf("/resources/");
                if (index >= 0) {
                    return (fullPath.substring(0, index) + "/resources/").replace("/server/", "/");
                }
            }
        } catch (MalformedURLException e) {
            // Manejar la excepción o mostrar un mensaje de error
        }
        return null;
//    String ruta = servletContext.getRealPath("") + File.separator + (directorio !=null && directorio != "" ? directorio : "images") + File.separator;

    }

    public boolean generaTraza(TipoSeguridad nuevoEstadoCurso) {
        boolean validacion = true;
        String loginUsuarioAct = "", asunto = "";

        try {
            /////////////////////////////////////////////////////////////////////
            if (nuevoEstadoCurso.getCodProg().equals("TP_ECC_DES") && registro.getNroExpediente() != null) {
                loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(registro.getNroExpediente());
                asunto = nuevoEstadoCurso.getNombre() + " A SOLICITUD DEL ADMINISTRADO. " + observacion;
                //ARCHIVAMIENTO DEL EXPEDIENTE                
                boolean estadoNE = wsTramDocController.archivarExpediente(registro.getNroExpediente(), loginUsuarioAct, asunto);
                if (!estadoNE) {
                    JsfUtil.mensajeError("Ocurrió un error al archivar el expediente " + registro.getNroExpediente());
                }
            } else {
                validacion = true;
            }
            /////////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            e.printStackTrace();
            validacion = false;
            JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro Código: " + registro.getId());
        }

        return validacion;
    }

    public String getCefoeNumResol() {
        return cefoeNumResol;
    }

    public void setCefoeNumResol(String cefoeNumResol) {
        this.cefoeNumResol = cefoeNumResol;
    }

    public Date getCefoeFechaResolIni() {
        return cefoeFechaResolIni;
    }

    public void setCefoeFechaResolIni(Date cefoeFechaResolIni) {
        this.cefoeFechaResolIni = cefoeFechaResolIni;
    }

    public Date getCefoeFechaResolFin() {
        return cefoeFechaResolFin;
    }

    public void setCefoeFechaResolFin(Date cefoeFechaResolFin) {
        this.cefoeFechaResolFin = cefoeFechaResolFin;
    }

    public String getCefoeTipoLocal() {
        return cefoeTipoLocal;
    }

    public void setCefoeTipoLocal(String cefoeTipoLocal) {
        this.cefoeTipoLocal = cefoeTipoLocal;
    }

    public SspPoligono getSspPoligono() {
        return sspPoligono;
    }

    public void setSspPoligono(SspPoligono sspPoligono) {
        this.sspPoligono = sspPoligono;
    }

    public String getRegMsgCapacitador() {
        return regMsgCapacitador;
    }

    public void setRegMsgCapacitador(String regMsgCapacitador) {
        this.regMsgCapacitador = regMsgCapacitador;
    }

}
