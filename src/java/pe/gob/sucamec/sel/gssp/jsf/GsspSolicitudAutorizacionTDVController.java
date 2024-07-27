/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.jsf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
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
import pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade;
import pe.gob.sucamec.bdintegrado.bean.SbAsientoPersonaFacade;
import pe.gob.sucamec.bdintegrado.bean.SbAsientoSunarpFacade;
import pe.gob.sucamec.bdintegrado.bean.SbAsientoVehiculoFacade;
import pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade;
import pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbEmpresaExtranjeraFacade;
import pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade;
import pe.gob.sucamec.bdintegrado.bean.SbPaisFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.bean.SbPartidaSunarpFacade;
import pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade;
import pe.gob.sucamec.bdintegrado.bean.SbRelacionPersonaFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SspAlmacenFacade;
import pe.gob.sucamec.bdintegrado.bean.SspArchivoFacade;
import pe.gob.sucamec.bdintegrado.bean.SspCartaFianzaFacade;
import pe.gob.sucamec.bdintegrado.bean.SspContactoFacade;
import pe.gob.sucamec.bdintegrado.bean.SspFormaFacade;
import pe.gob.sucamec.bdintegrado.bean.SspLicenciaMunicipalFacade;
import pe.gob.sucamec.bdintegrado.bean.SspLocalAutorizacionFacade;
import pe.gob.sucamec.bdintegrado.bean.SspLocalRegistroFacade;
import pe.gob.sucamec.bdintegrado.bean.SspParticipanteFacade;
import pe.gob.sucamec.bdintegrado.bean.SspPolizaFacade;
import pe.gob.sucamec.bdintegrado.bean.SspRegistroEventoFacade;
import pe.gob.sucamec.bdintegrado.bean.SspRegistroFacade;
import pe.gob.sucamec.bdintegrado.bean.SspRegistroRegDiscaFacade;
import pe.gob.sucamec.bdintegrado.bean.SspRepresentantePublicoFacade;
import pe.gob.sucamec.bdintegrado.bean.SspRepresentanteRegistroFacade;
import pe.gob.sucamec.bdintegrado.bean.SspRequisitoFacade;
import pe.gob.sucamec.bdintegrado.bean.SspResolucionFacade;
import pe.gob.sucamec.bdintegrado.bean.SspServicioFacade;
import pe.gob.sucamec.bdintegrado.bean.SspTipoUsoLocalFacade;
import pe.gob.sucamec.bdintegrado.bean.SspVehiculoDineroFacade;
import pe.gob.sucamec.bdintegrado.bean.SspVehiculoFacade;
import pe.gob.sucamec.bdintegrado.bean.SspVisacionFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbAsientoPersona;
import pe.gob.sucamec.bdintegrado.data.SbAsientoSunarp;
import pe.gob.sucamec.bdintegrado.data.SbAsientoVehiculo;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbEmpresaExtranjera;
import pe.gob.sucamec.bdintegrado.data.SbPaisGt;
import pe.gob.sucamec.bdintegrado.data.SbPartidaSunarp;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspAlmacen;
import pe.gob.sucamec.bdintegrado.data.SspArchivo;
import pe.gob.sucamec.bdintegrado.data.SspCartaFianza;
import pe.gob.sucamec.bdintegrado.data.SspContacto;
import pe.gob.sucamec.bdintegrado.data.SspForma;
import pe.gob.sucamec.bdintegrado.data.SspLicenciaMunicipal;
import pe.gob.sucamec.bdintegrado.data.SspLocalAutorizacion;
import pe.gob.sucamec.bdintegrado.data.SspLocalRegistro;
import pe.gob.sucamec.bdintegrado.data.SspParticipante;
import pe.gob.sucamec.bdintegrado.data.SspPoliza;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SspRegistroEvento;
import pe.gob.sucamec.bdintegrado.data.SspRegistroRegDisca;
import pe.gob.sucamec.bdintegrado.data.SspRepresentantePublico;
import pe.gob.sucamec.bdintegrado.data.SspRepresentanteRegistro;
import pe.gob.sucamec.bdintegrado.data.SspRequisito;
import pe.gob.sucamec.bdintegrado.data.SspResolucion;
import pe.gob.sucamec.bdintegrado.data.SspServicio;
import pe.gob.sucamec.bdintegrado.data.SspTipoUsoLocal;
import pe.gob.sucamec.bdintegrado.data.SspVehiculoDinero;
import pe.gob.sucamec.bdintegrado.data.SspVisacion;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.UploadFilesController;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.notificacion.beans.NeDocumentoFacade;
import pe.gob.sucamec.sel.gssp.data.LocalAutorizacionDetalle;
import pe.gob.sucamec.sel.gssp.data.PersonaDetalle;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.PideMigraciones;
import wspide.PideMigraciones_Service;

/**
 *
 * @author mpalomino
 */
@Named(value = "gsspSolicitudAutorizacionTDVController")
@SessionScoped
public class GsspSolicitudAutorizacionTDVController implements Serializable{
    
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
    
    private SbPartidaSunarp partidaSunarpVEH;
    private SbAsientoSunarp asientoSunarpVEH;
    private SbAsientoVehiculo asientoPersonaVEH;
    

    private SspRegistroEvento registroEvento;
    private SspServicio servicio;
    private SspForma forma;

    private SspLocalAutorizacion localAutorizacion;
    private SspLocalAutorizacion localAutorizacionAnterior;
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

    private List<TipoBaseGt> formTipoRegistroLista;
    private TipoBaseGt formTipoRegistroSelected;

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
    private SspLocalAutorizacion localAutorizacionTEMP;
    private SspLocalAutorizacion localAutorizacionSelected;
    private String localAutorizacionSelectedString;

    private List<TipoBaseGt> regTipoViasLPSList;
    private TipoBaseGt regTipoViasLPSSelected;

    private List<TipoSeguridad> regTipLocalLPSList;
    private TipoSeguridad regTipLocalLPSSelected;

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
    
    //=======================================================
    //======    Accionistas   ===========
    //=======================================================
    
    
    private boolean habilitarAccionistas;

    private List<TipoBaseGt> regTipDocAccionistaList;
    private TipoBaseGt regTipDocAccionistaSelected;

    private List<TipoBaseGt> regTipoSocioList;
    private TipoBaseGt regTipoSocioSelected;

    private SbPersonaGt regDatosSocioEncontrado;
    private Long regIdSocioEncontrado;//ID de la PERSONA o EMPRESA EXTRANJERA
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
    private int checkTipoPropiedad;
    private BigDecimal regMontoAportante;
    private boolean regDisabledMontoAportante = false;

    private int checkTipoAcciones;
    private BigDecimal regNumeroAcciones;
    private BigDecimal regPorcentajeAcciones;
    private boolean regDisabledNumerojeAcciones = false;
    private boolean regDisabledPorcentajeAcciones = true;

    private boolean renderEsAccionista = false;
    private boolean renderAccionesAportesNoaplica = false;
    private String accionistaNumDoc, accionistaApePat, accionistaApeMat, accionistaNombres;

    private List<TipoBaseGt> regTipoPersonaLista;
    private TipoBaseGt regTipoPersonaSelected;
    
    private List<SbPaisGt> regPaisLista;
    private SbPaisGt regPaisSelected;
    private String regPaisSelected_id;
    
    private SbEmpresaExtranjera regParticipacionistaData;
    
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
    
    //Ley 1213 segun opción de Adecuacion
    private String tipoRegistroCodProg;
    private boolean renderLocalDISCA = false;
    private boolean renderMostrarFormVigilanciaPrivada = false;
    private HashMap emptyModel_vistaDISCA = new HashMap<>();
    private String registro_id_vistaDISCA;
    private SspRegistro registro_resol;
    private SspRegistroRegDisca registro_regDisca;
    private TipoBaseGt mod_vinculadaSelected;

    //*MAPA*//
    private boolean renderVerMapa;
    private Double lat_x = 0.0;
    private Double long_x = 0.0;
    private List<SbDistritoGt> listaDistritoMapaLocal;
    
    
    //=======================================================
    //======     POLIZA    ===========
    //=======================================================
    private String empresaAseguradora;
    private String rucEmpAseguradora;
    private String nroPoliza;
    private Date fecIniPoliza;
    private Date fecFinPoliza;
    private TipoBaseGt tipoMoneda;
    private String bienesAcustodiar;
    private BigDecimal valorMontoMaximo;
    private List<TipoBaseGt> lstMonedas;
    private String nomArchivoPOL;
    private String nomArchivoPOLAnterior;
    private SspPoliza poliza;
    
    private UploadedFile filePOL;
    private byte[] polByte;
    private StreamedContent archivoPOL;
    
    
     //=======================================================
    //======     VEHICULOS    ===========
    //=======================================================
    private SspVehiculoDinero vehiculo;
    private String categoria;
    private String carroseria;
    private String placa;
    private String partidaRegVeh;
    private String asientoRegVeh;
    private String ubicacionVeh;
    private List<TipoBaseGt> regOficinaRegListVEH;
    private TipoBaseGt regOficinaRegSelectedVEH;
    private List<TipoBaseGt> regZonaRegListVEH;
    private TipoBaseGt regZonaRegSelectedVEH;
    private List<SbDistritoGt> regDistritoVEHList;
    private SbDistritoGt regDistritoVEHSelected;
    private boolean esArrendado;
    
    private String nomArchivoPartidaRegVeh;
    private String nomArchivoPartidaRegVehAnterior;
    private UploadedFile filePartidaRegVeh;
    private byte[] partidaVehByte;
    private StreamedContent archivoPartidaRegVeh;
    
    private String nomArchivoCAV;
    private String nomArchivoCAVAnterior;
    private UploadedFile filePartidaCAV;
    private byte[] cavByte;
    private StreamedContent archivoCAV;
    
    
    public String getAccionistaNumDoc() {
        return accionistaNumDoc;
    }

    public void setAccionistaNumDoc(String accionistaNumDoc) {
        this.accionistaNumDoc = accionistaNumDoc;
    }

    public String getAccionistaApePat() {
        return accionistaApePat;
    }

    public void setAccionistaApePat(String accionistaApePat) {
        this.accionistaApePat = accionistaApePat;
    }

    public String getAccionistaApeMat() {
        return accionistaApeMat;
    }

    public void setAccionistaApeMat(String accionistaApeMat) {
        this.accionistaApeMat = accionistaApeMat;
    }

    public String getAccionistaNombres() {
        return accionistaNombres;
    }

    public void setAccionistaNombres(String accionistaNombres) {
        this.accionistaNombres = accionistaNombres;
    }
    
    public List<SbPaisGt> getRegPaisLista() {
        return regPaisLista;
    }

    public void setRegPaisLista(List<SbPaisGt> regPaisLista) {
        this.regPaisLista = regPaisLista;
    }

    public SbPaisGt getRegPaisSelected() {
        return regPaisSelected;
    }

    public void setRegPaisSelected(SbPaisGt regPaisSelected) {
        this.regPaisSelected = regPaisSelected;
    }   
    
    public List<TipoBaseGt> getRegTipoPersonaLista() {
        return regTipoPersonaLista;
    }

    public void setRegTipoPersonaLista(List<TipoBaseGt> regTipoPersonaLista) {
        this.regTipoPersonaLista = regTipoPersonaLista;
    }

    public TipoBaseGt getRegTipoPersonaSelected() {
        return regTipoPersonaSelected;
    }

    public void setRegTipoPersonaSelected(TipoBaseGt regTipoPersonaSelected) {
        this.regTipoPersonaSelected = regTipoPersonaSelected;
    }   
    
    public Long getRegIdSocioEncontrado() {
        return regIdSocioEncontrado;
    }

    public void setRegIdSocioEncontrado(Long regIdSocioEncontrado) {
        this.regIdSocioEncontrado = regIdSocioEncontrado;
    }
    
    public String getRegPaisSelected_id() {
        return regPaisSelected_id;
    }

    public void setRegPaisSelected_id(String regPaisSelected_id) {
        this.regPaisSelected_id = regPaisSelected_id;
    }  
    
    public SbEmpresaExtranjera getRegParticipacionistaData() {
        return regParticipacionistaData;
    }

    public void setRegParticipacionistaData(SbEmpresaExtranjera regParticipacionistaData) {
        this.regParticipacionistaData = regParticipacionistaData;
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
    
    public TipoBaseGt getRegTipoSeguridadSelected() {
        return regTipoSeguridadSelected;
    }

    public void setRegTipoSeguridadSelected(TipoBaseGt regTipoSeguridadSelected) {
        this.regTipoSeguridadSelected = regTipoSeguridadSelected;
    }
    
    public TipoBaseGt getRegTipoRegistroSelected() {
        return regTipoRegistroSelected;
    }

    public void setRegTipoRegistroSelected(TipoBaseGt regTipoRegistroSelected) {
        this.regTipoRegistroSelected = regTipoRegistroSelected;
    }
    
    public TipoBaseGt getRegTipoOperacionSelected() {
        return regTipoOperacionSelected;
    }

    public void setRegTipoOperacionSelected(TipoBaseGt regTipoOperacionSelected) {
        this.regTipoOperacionSelected = regTipoOperacionSelected;
    }
    
    public TipoSeguridad getRegServicioPrestadoSelected() {
        return regServicioPrestadoSelected;
    }

    public void setRegServicioPrestadoSelected(TipoSeguridad regServicioPrestadoSelected) {
        this.regServicioPrestadoSelected = regServicioPrestadoSelected;
    }
    
    public List<TipoBaseGt> getRegTipoRegistroList() {
        return regTipoRegistroList;
    }

    public void setRegTipoRegistroList(List<TipoBaseGt> regTipoRegistroList) {
        this.regTipoRegistroList = regTipoRegistroList;
    }
    
    public List<TipoBaseGt> getRegTipoOperacionList() {
        return regTipoOperacionList;
    }

    public void setRegTipoOperacionList(List<TipoBaseGt> regTipoOperacionList) {
        this.regTipoOperacionList = regTipoOperacionList;
    }
    
    public List<TipoSeguridad> getRegServicioPrestadoList() {
        return regServicioPrestadoList;
    }

    public void setRegServicioPrestadoList(List<TipoSeguridad> regServicioPrestadoList) {
        this.regServicioPrestadoList = regServicioPrestadoList;
    }
    
    public List<TipoSeguridad> getRegTipLocalLPSList() {
        return regTipLocalLPSList;
    }

    public void setRegTipLocalLPSList(List<TipoSeguridad> regTipLocalLPSList) {
        this.regTipLocalLPSList = regTipLocalLPSList;
    }
    
    public TipoSeguridad getRegTipLocalLPSSelected() {
        return regTipLocalLPSSelected;
    }

    public void setRegTipLocalLPSSelected(TipoSeguridad regTipLocalLPSSelected) {
        this.regTipLocalLPSSelected = regTipLocalLPSSelected;
    }
    
    public ArrayList<SspContacto> getLstContactos() {
        return lstContactos;
    }

    public void setLstContactos(ArrayList<SspContacto> lstContactos) {
        this.lstContactos = lstContactos;
    }
    
    public SspResolucion getResolucionPrincipal() {
        return resolucionPrincipal;
    }

    public void setResolucionPrincipal(SspResolucion resolucionPrincipal) {
        this.resolucionPrincipal = resolucionPrincipal;
    }
    
    public SbDistritoGt getRegDistritoLPSSelected() {
        return regDistritoLPSSelected;
    }

    public void setRegDistritoLPSSelected(SbDistritoGt regDistritoLPSSelected) {
        this.regDistritoLPSSelected = regDistritoLPSSelected;
    }
    
    public List<SbDistritoGt> getRegDistritoLPSList() {
        return regDistritoLPSList;
    }

    public void setRegDistritoLPSList(List<SbDistritoGt> regDistritoLPSList) {
        this.regDistritoLPSList = regDistritoLPSList;
    }
    
    public String getLocalAutorizacionSelectedString() {
        return localAutorizacionSelectedString;
    }

    public void setLocalAutorizacionSelectedString(String localAutorizacionSelectedString) {
        this.localAutorizacionSelectedString = localAutorizacionSelectedString;
    }
    
    public List<LocalAutorizacionDetalle> getLocalAutorizacionListado() {
        return localAutorizacionListado;
    }

    public void setLocalAutorizacionListado(List<LocalAutorizacionDetalle> localAutorizacionListado) {
        this.localAutorizacionListado = localAutorizacionListado;
    }
    
    public TipoBaseGt getRegTipoViasLPSSelected_Form() {
        return regTipoViasLPSSelected_Form;
    }

    public void setRegTipoViasLPSSelected_Form(TipoBaseGt regTipoViasLPSSelected_Form) {
        this.regTipoViasLPSSelected_Form = regTipoViasLPSSelected_Form;
    }
    
    public List<TipoBaseGt> getRegTipoViasLPSList_Form() {
        return regTipoViasLPSList_Form;
    }

    public void setRegTipoViasLPSList_Form(List<TipoBaseGt> regTipoViasLPSList_Form) {
        this.regTipoViasLPSList_Form = regTipoViasLPSList_Form;
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
    
    public TipoSeguridad getRegTipLocalLPSSelected_Form() {
        return regTipLocalLPSSelected_Form;
    }

    public void setRegTipLocalLPSSelected_Form(TipoSeguridad regTipLocalLPSSelected_Form) {
        this.regTipLocalLPSSelected_Form = regTipLocalLPSSelected_Form;
    }
    
    public List<TipoSeguridad> getRegTipLocalLPSList_Form() {
        return regTipLocalLPSList_Form;
    }

    public void setRegTipLocalLPSList_Form(List<TipoSeguridad> regTipLocalLPSList_Form) {
        this.regTipLocalLPSList_Form = regTipLocalLPSList_Form;
    }
    
    public TipoSeguridad getRegTipUsoLPSSelected_Form() {
        return regTipUsoLPSSelected_Form;
    }

    public void setRegTipUsoLPSSelected_Form(TipoSeguridad regTipUsoLPSSelected_Form) {
        this.regTipUsoLPSSelected_Form = regTipUsoLPSSelected_Form;
    }
    
    public List<TipoSeguridad> getRegTipUsoLPSList_Form() {
        return regTipUsoLPSList_Form;
    }

    public void setRegTipUsoLPSList_Form(List<TipoSeguridad> regTipUsoLPSList_Form) {
        this.regTipUsoLPSList_Form = regTipUsoLPSList_Form;
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
    
    public MapModel getEmptyModel() {
        return emptyModel;
    }

    public void setEmptyModel(MapModel emptyModel) {
        this.emptyModel = emptyModel;
    }
    
    public String getTipoAdministradoUsuario() {
        return tipoAdministradoUsuario;
    }

    public void setTipoAdministradoUsuario(String tipoAdministradoUsuario) {
        this.tipoAdministradoUsuario = tipoAdministradoUsuario;
    }
    
    public List<TipoBaseGt> getRegTipoSeguridadList() {
        return regTipoSeguridadList;
    }

    public void setRegTipoSeguridadList(List<TipoBaseGt> regTipoSeguridadList) {
        this.regTipoSeguridadList = regTipoSeguridadList;
    }
    
    public boolean isRegBuscaDepartamentoPrincipal() {
        return regBuscaDepartamentoPrincipal;
    }

    public void setRegBuscaDepartamentoPrincipal(boolean regBuscaDepartamentoPrincipal) {
        this.regBuscaDepartamentoPrincipal = regBuscaDepartamentoPrincipal;
    }
    
    public TipoBaseGt getRegTipoViasLPSSelected() {
        return regTipoViasLPSSelected;
    }

    public void setRegTipoViasLPSSelected(TipoBaseGt regTipoViasLPSSelected) {
        this.regTipoViasLPSSelected = regTipoViasLPSSelected;
    }
    
    public List<TipoBaseGt> getRegTipoViasLPSList() {
        return regTipoViasLPSList;
    }

    public void setRegTipoViasLPSList(List<TipoBaseGt> regTipoViasLPSList) {
        this.regTipoViasLPSList = regTipoViasLPSList;
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
    
    public TipoSeguridad getRegTipUsoLPSSelected() {
        return regTipUsoLPSSelected;
    }

    public void setRegTipUsoLPSSelected(TipoSeguridad regTipUsoLPSSelected) {
        this.regTipUsoLPSSelected = regTipUsoLPSSelected;
    }
    
    public List<TipoSeguridad> getRegTipUsoLPSList() {
        return regTipUsoLPSList;
    }

    public void setRegTipUsoLPSList(List<TipoSeguridad> regTipUsoLPSList) {
        this.regTipUsoLPSList = regTipUsoLPSList;
    }
    
    public String getNomArchivoLocal() {
        return nomArchivoLocal;
    }

    public void setNomArchivoLocal(String nomArchivoLocal) {
        this.nomArchivoLocal = nomArchivoLocal;
    }
    
    public boolean isRenderVerMapa() {
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
    
    public String getRegtextoCorreoContactoLPS() {
        return regtextoCorreoContactoLPS;
    }

    public void setRegtextoCorreoContactoLPS(String regtextoCorreoContactoLPS) {
        this.regtextoCorreoContactoLPS = regtextoCorreoContactoLPS;
    }
    
    public String getRegtextoContactoLPS() {
        return regtextoContactoLPS;
    }

    public void setRegtextoContactoLPS(String regtextoContactoLPS) {
        this.regtextoContactoLPS = regtextoContactoLPS;
    }
    
    public List<TipoBaseGt> getRegPrioridadLPSList() {
        return regPrioridadLPSList;
    }

    public void setRegPrioridadLPSList(List<TipoBaseGt> regPrioridadLPSList) {
        this.regPrioridadLPSList = regPrioridadLPSList;
    }
    
    public TipoBaseGt getRegTipoMedioContactoLPSSelected() {
        return regTipoMedioContactoLPSSelected;
    }

    public void setRegTipoMedioContactoLPSSelected(TipoBaseGt regTipoMedioContactoLPSSelected) {
        this.regTipoMedioContactoLPSSelected = regTipoMedioContactoLPSSelected;
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

    public boolean isHabilitarCartaFianza() {
        return habilitarCartaFianza;
    }

    public void setHabilitarCartaFianza(boolean habilitarCartaFianza) {
        this.habilitarCartaFianza = habilitarCartaFianza;
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

    public String getRegDomicilioRLSelected() {
        return regDomicilioRLSelected;
    }

    public void setRegDomicilioRLSelected(String regDomicilioRLSelected) {
        this.regDomicilioRLSelected = regDomicilioRLSelected;
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

    public boolean isRenderEsAccionista() {
        return renderEsAccionista;
    }

    public void setRenderEsAccionista(boolean renderEsAccionista) {
        this.renderEsAccionista = renderEsAccionista;
    }

    public String getTituloBuscaModalRepresentanteLegal() {
        return tituloBuscaModalRepresentanteLegal;
    }

    public void setTituloBuscaModalRepresentanteLegal(String tituloBuscaModalRepresentanteLegal) {
        this.tituloBuscaModalRepresentanteLegal = tituloBuscaModalRepresentanteLegal;
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

    public WsTramDoc getWsTramDocController() {
        return wsTramDocController;
    }

    public void setWsTramDocController(WsTramDoc wsTramDocController) {
        this.wsTramDocController = wsTramDocController;
    }

    public UploadFilesController getUploadFilesController() {
        return uploadFilesController;
    }

    public void setUploadFilesController(UploadFilesController uploadFilesController) {
        this.uploadFilesController = uploadFilesController;
    }

    public GsspSolicitudAutorizacionSISPEController getGsspSolicitudAutorizacionSISPEController() {
        return gsspSolicitudAutorizacionSISPEController;
    }

    public void setGsspSolicitudAutorizacionSISPEController(GsspSolicitudAutorizacionSISPEController gsspSolicitudAutorizacionSISPEController) {
        this.gsspSolicitudAutorizacionSISPEController = gsspSolicitudAutorizacionSISPEController;
    }

    public GsspSolicitudAutorizacionSPPController getGsspSolicitudAutorizacionSPPController() {
        return gsspSolicitudAutorizacionSPPController;
    }

    public void setGsspSolicitudAutorizacionSPPController(GsspSolicitudAutorizacionSPPController gsspSolicitudAutorizacionSPPController) {
        this.gsspSolicitudAutorizacionSPPController = gsspSolicitudAutorizacionSPPController;
    }

    public GsspSolicitudAutorizacionSISPAController getGsspSolicitudAutorizacionSISPAController() {
        return gsspSolicitudAutorizacionSISPAController;
    }

    public void setGsspSolicitudAutorizacionSISPAController(GsspSolicitudAutorizacionSISPAController gsspSolicitudAutorizacionSISPAController) {
        this.gsspSolicitudAutorizacionSISPAController = gsspSolicitudAutorizacionSISPAController;
    }

    public GsspSolicitudAutorizacionSCBCController getGsspSolicitudAutorizacionSCBCController() {
        return gsspSolicitudAutorizacionSCBCController;
    }

    public void setGsspSolicitudAutorizacionSCBCController(GsspSolicitudAutorizacionSCBCController gsspSolicitudAutorizacionSCBCController) {
        this.gsspSolicitudAutorizacionSCBCController = gsspSolicitudAutorizacionSCBCController;
    }

    public GsspSolicitudAutorizacionSSEController getGsspSolicitudAutorizacionSSEController() {
        return gsspSolicitudAutorizacionSSEController;
    }

    public void setGsspSolicitudAutorizacionSSEController(GsspSolicitudAutorizacionSSEController gsspSolicitudAutorizacionSSEController) {
        this.gsspSolicitudAutorizacionSSEController = gsspSolicitudAutorizacionSSEController;
    }

    public GsspSolicitudAutorizacionSTSController getGsspSolicitudAutorizacionSTSController() {
        return gsspSolicitudAutorizacionSTSController;
    }

    public void setGsspSolicitudAutorizacionSTSController(GsspSolicitudAutorizacionSTSController gsspSolicitudAutorizacionSTSController) {
        this.gsspSolicitudAutorizacionSTSController = gsspSolicitudAutorizacionSTSController;
    }

    public GsspSolicitudAutorizacionSPCPController getGsspSolicitudAutorizacionSPCPController() {
        return gsspSolicitudAutorizacionSPCPController;
    }

    public void setGsspSolicitudAutorizacionSPCPController(GsspSolicitudAutorizacionSPCPController gsspSolicitudAutorizacionSPCPController) {
        this.gsspSolicitudAutorizacionSPCPController = gsspSolicitudAutorizacionSPCPController;
    }

    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public SspRegistro getRegistro() {
        return registro;
    }

    public void setRegistro(SspRegistro registro) {
        this.registro = registro;
    }

    public SspRegistro getRegistroN2() {
        return registroN2;
    }

    public void setRegistroN2(SspRegistro registroN2) {
        this.registroN2 = registroN2;
    }

    public SspRepresentanteRegistro getRepresentanteRegistro() {
        return representanteRegistro;
    }

    public void setRepresentanteRegistro(SspRepresentanteRegistro representanteRegistro) {
        this.representanteRegistro = representanteRegistro;
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

    public SspRepresentantePublico getRepresentantePublico() {
        return representantePublico;
    }

    public void setRepresentantePublico(SspRepresentantePublico representantePublico) {
        this.representantePublico = representantePublico;
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

    public SspLocalAutorizacion getLocalAutorizacion() {
        return localAutorizacion;
    }

    public void setLocalAutorizacion(SspLocalAutorizacion localAutorizacion) {
        this.localAutorizacion = localAutorizacion;
    }

    public SspLocalAutorizacion getLocalAutorizacionAnterior() {
        return localAutorizacionAnterior;
    }

    public void setLocalAutorizacionAnterior(SspLocalAutorizacion localAutorizacionAnterior) {
        this.localAutorizacionAnterior = localAutorizacionAnterior;
    }

    public SspLocalAutorizacion getLocalAutorizacionN2() {
        return localAutorizacionN2;
    }

    public void setLocalAutorizacionN2(SspLocalAutorizacion localAutorizacionN2) {
        this.localAutorizacionN2 = localAutorizacionN2;
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

    public SspLicenciaMunicipal getLicenciaMunicipal() {
        return licenciaMunicipal;
    }

    public void setLicenciaMunicipal(SspLicenciaMunicipal licenciaMunicipal) {
        this.licenciaMunicipal = licenciaMunicipal;
    }

    public SspCartaFianza getCartaFianza() {
        return cartaFianza;
    }

    public void setCartaFianza(SspCartaFianza cartaFianza) {
        this.cartaFianza = cartaFianza;
    }

    public SspVisacion getVizacion() {
        return vizacion;
    }

    public void setVizacion(SspVisacion vizacion) {
        this.vizacion = vizacion;
    }

    public String getFiltroBuscarPor() {
        return filtroBuscarPor;
    }

    public void setFiltroBuscarPor(String filtroBuscarPor) {
        this.filtroBuscarPor = filtroBuscarPor;
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

    public TipoBaseGt getFiltroTipoSeguridadSelected() {
        return filtroTipoSeguridadSelected;
    }

    public void setFiltroTipoSeguridadSelected(TipoBaseGt filtroTipoSeguridadSelected) {
        this.filtroTipoSeguridadSelected = filtroTipoSeguridadSelected;
    }

    public List<TipoBaseGt> getFiltroTipoOpeListado() {
        return filtroTipoOpeListado;
    }

    public void setFiltroTipoOpeListado(List<TipoBaseGt> filtroTipoOpeListado) {
        this.filtroTipoOpeListado = filtroTipoOpeListado;
    }

    public TipoBaseGt getFiltroTipoOpeSelected() {
        return filtroTipoOpeSelected;
    }

    public void setFiltroTipoOpeSelected(TipoBaseGt filtroTipoOpeSelected) {
        this.filtroTipoOpeSelected = filtroTipoOpeSelected;
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

    public List<TipoSeguridad> getFiltroTipoEstadoListado() {
        return filtroTipoEstadoListado;
    }

    public void setFiltroTipoEstadoListado(List<TipoSeguridad> filtroTipoEstadoListado) {
        this.filtroTipoEstadoListado = filtroTipoEstadoListado;
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

    public boolean isDisabledBtnTransmitir() {
        return disabledBtnTransmitir;
    }

    public void setDisabledBtnTransmitir(boolean disabledBtnTransmitir) {
        this.disabledBtnTransmitir = disabledBtnTransmitir;
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

    public boolean isAdministradoConRUC() {
        return administradoConRUC;
    }

    public void setAdministradoConRUC(boolean administradoConRUC) {
        this.administradoConRUC = administradoConRUC;
    }

    public List<TipoBaseGt> getFormTipoRegistroLista() {
        return formTipoRegistroLista;
    }

    public void setFormTipoRegistroLista(List<TipoBaseGt> formTipoRegistroLista) {
        this.formTipoRegistroLista = formTipoRegistroLista;
    }

    public TipoBaseGt getFormTipoRegistroSelected() {
        return formTipoRegistroSelected;
    }

    public void setFormTipoRegistroSelected(TipoBaseGt formTipoRegistroSelected) {
        this.formTipoRegistroSelected = formTipoRegistroSelected;
    }

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

    public TipoBaseGt getRegTipoProcesoId() {
        return regTipoProcesoId;
    }

    public void setRegTipoProcesoId(TipoBaseGt regTipoProcesoId) {
        this.regTipoProcesoId = regTipoProcesoId;
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

    public List<SbRecibos> getRegListDatoRecibos() {
        return regListDatoRecibos;
    }

    public void setRegListDatoRecibos(List<SbRecibos> regListDatoRecibos) {
        this.regListDatoRecibos = regListDatoRecibos;
    }

    public SbRecibos getRegDatoRecibos() {
        return regDatoRecibos;
    }

    public void setRegDatoRecibos(SbRecibos regDatoRecibos) {
        this.regDatoRecibos = regDatoRecibos;
    }

    public String getRegNroComprobante() {
        return regNroComprobante;
    }

    public void setRegNroComprobante(String regNroComprobante) {
        this.regNroComprobante = regNroComprobante;
    }

    public boolean isHabilitarAdjuntarFoto() {
        return habilitarAdjuntarFoto;
    }

    public void setHabilitarAdjuntarFoto(boolean habilitarAdjuntarFoto) {
        this.habilitarAdjuntarFoto = habilitarAdjuntarFoto;
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

    public StreamedContent getArchivoFOTO() {
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

    public List<SbPersonaGt> getRegPersonasRLList() {
        return regPersonasRLList;
    }

    public void setRegPersonasRLList(List<SbPersonaGt> regPersonasRLList) {
        this.regPersonasRLList = regPersonasRLList;
    }

    public SbPersonaGt getRegPersonasRLSelected() {
        return regPersonasRLSelected;
    }

    public void setRegPersonasRLSelected(SbPersonaGt regPersonasRLSelected) {
        this.regPersonasRLSelected = regPersonasRLSelected;
    }

    public SbRelacionPersonaGt getRegistroSbRelacionPersonaGt() {
        return registroSbRelacionPersonaGt;
    }

    public void setRegistroSbRelacionPersonaGt(SbRelacionPersonaGt registroSbRelacionPersonaGt) {
        this.registroSbRelacionPersonaGt = registroSbRelacionPersonaGt;
    }

    public SbDireccionGt getRegSbDireccionRL() {
        return regSbDireccionRL;
    }

    public void setRegSbDireccionRL(SbDireccionGt regSbDireccionRL) {
        this.regSbDireccionRL = regSbDireccionRL;
    }

    public boolean isExisteAsientoSunarpRL() {
        return existeAsientoSunarpRL;
    }

    public void setExisteAsientoSunarpRL(boolean existeAsientoSunarpRL) {
        this.existeAsientoSunarpRL = existeAsientoSunarpRL;
    }

    public boolean isHabilitarRepresentanteLegal() {
        return habilitarRepresentanteLegal;
    }

    public void setHabilitarRepresentanteLegal(boolean habilitarRepresentanteLegal) {
        this.habilitarRepresentanteLegal = habilitarRepresentanteLegal;
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

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public boolean isHabilitarLocalPrestacionServicio() {
        return habilitarLocalPrestacionServicio;
    }

    public void setHabilitarLocalPrestacionServicio(boolean habilitarLocalPrestacionServicio) {
        this.habilitarLocalPrestacionServicio = habilitarLocalPrestacionServicio;
    }

    public SspLocalAutorizacion getLocalAutorizacionTEMP() {
        return localAutorizacionTEMP;
    }

    public void setLocalAutorizacionTEMP(SspLocalAutorizacion localAutorizacionTEMP) {
        this.localAutorizacionTEMP = localAutorizacionTEMP;
    }

    public SspLocalAutorizacion getLocalAutorizacionSelected() {
        return localAutorizacionSelected;
    }

    public void setLocalAutorizacionSelected(SspLocalAutorizacion localAutorizacionSelected) {
        this.localAutorizacionSelected = localAutorizacionSelected;
    }

    public String getRegNroFisicoLPS() {
        return regNroFisicoLPS;
    }

    public void setRegNroFisicoLPS(String regNroFisicoLPS) {
        this.regNroFisicoLPS = regNroFisicoLPS;
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

    public BigInteger getRegCantArmeriaLPS() {
        return regCantArmeriaLPS;
    }

    public void setRegCantArmeriaLPS(BigInteger regCantArmeriaLPS) {
        this.regCantArmeriaLPS = regCantArmeriaLPS;
    }

    public ArrayList<SspAlmacen> getLstAlmacenes() {
        return lstAlmacenes;
    }

    public void setLstAlmacenes(ArrayList<SspAlmacen> lstAlmacenes) {
        this.lstAlmacenes = lstAlmacenes;
    }

    public List<SspAlmacen> getLstAlmacenesList() {
        return lstAlmacenesList;
    }

    public void setLstAlmacenesList(List<SspAlmacen> lstAlmacenesList) {
        this.lstAlmacenesList = lstAlmacenesList;
    }

    public SspAlmacen getSelectedAlmacen() {
        return selectedAlmacen;
    }

    public void setSelectedAlmacen(SspAlmacen selectedAlmacen) {
        this.selectedAlmacen = selectedAlmacen;
    }

    public Long getContRegAlmacen() {
        return contRegAlmacen;
    }

    public void setContRegAlmacen(Long contRegAlmacen) {
        this.contRegAlmacen = contRegAlmacen;
    }

    public boolean isHabilitarMediosContactos() {
        return habilitarMediosContactos;
    }

    public void setHabilitarMediosContactos(boolean habilitarMediosContactos) {
        this.habilitarMediosContactos = habilitarMediosContactos;
    }

    public List<SspContacto> getLstContactosList() {
        return lstContactosList;
    }

    public void setLstContactosList(List<SspContacto> lstContactosList) {
        this.lstContactosList = lstContactosList;
    }

    public SspContacto getSelectedContacto() {
        return selectedContacto;
    }

    public void setSelectedContacto(SspContacto selectedContacto) {
        this.selectedContacto = selectedContacto;
    }

    public List<SspContacto> getListaUpdateContactosActivos() {
        return listaUpdateContactosActivos;
    }

    public void setListaUpdateContactosActivos(List<SspContacto> listaUpdateContactosActivos) {
        this.listaUpdateContactosActivos = listaUpdateContactosActivos;
    }

    public Long getContRegContacto() {
        return contRegContacto;
    }

    public void setContRegContacto(Long contRegContacto) {
        this.contRegContacto = contRegContacto;
    }

    public boolean isHabilitarLicenciaMunicipal() {
        return habilitarLicenciaMunicipal;
    }

    public void setHabilitarLicenciaMunicipal(boolean habilitarLicenciaMunicipal) {
        this.habilitarLicenciaMunicipal = habilitarLicenciaMunicipal;
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

    public boolean isRegDisabledCalendar() {
        return regDisabledCalendar;
    }

    public void setRegDisabledCalendar(boolean regDisabledCalendar) {
        this.regDisabledCalendar = regDisabledCalendar;
    }

    public boolean isHabilitarAccionistas() {
        return habilitarAccionistas;
    }

    public void setHabilitarAccionistas(boolean habilitarAccionistas) {
        this.habilitarAccionistas = habilitarAccionistas;
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

    public SbPersonaGt getRegDatosSocioEncontrado() {
        return regDatosSocioEncontrado;
    }

    public void setRegDatosSocioEncontrado(SbPersonaGt regDatosSocioEncontrado) {
        this.regDatosSocioEncontrado = regDatosSocioEncontrado;
    }

    public String getRegNombreSocioEncontrado() {
        return regNombreSocioEncontrado;
    }

    public void setRegNombreSocioEncontrado(String regNombreSocioEncontrado) {
        this.regNombreSocioEncontrado = regNombreSocioEncontrado;
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

    public List<SspParticipante> getListaUpdateParticipanteActivos() {
        return listaUpdateParticipanteActivos;
    }

    public void setListaUpdateParticipanteActivos(List<SspParticipante> listaUpdateParticipanteActivos) {
        this.listaUpdateParticipanteActivos = listaUpdateParticipanteActivos;
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

    public int getCheckTipoAporte() {
        return checkTipoAporte;
    }

    public void setCheckTipoAporte(int checkTipoAporte) {
        this.checkTipoAporte = checkTipoAporte;
    }

    public BigDecimal getRegMontoAportante() {
        return regMontoAportante;
    }

    public void setRegMontoAportante(BigDecimal regMontoAportante) {
        this.regMontoAportante = regMontoAportante;
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

    public boolean isRenderAccionesAportesNoaplica() {
        return renderAccionesAportesNoaplica;
    }

    public void setRenderAccionesAportesNoaplica(boolean renderAccionesAportesNoaplica) {
        this.renderAccionesAportesNoaplica = renderAccionesAportesNoaplica;
    }

    public List<SspCartaFianza> getRegBuscaCartaFianzaList() {
        return regBuscaCartaFianzaList;
    }

    public void setRegBuscaCartaFianzaList(List<SspCartaFianza> regBuscaCartaFianzaList) {
        this.regBuscaCartaFianzaList = regBuscaCartaFianzaList;
    }

    public String getRegBuscaCartaFianzaSelectedString() {
        return regBuscaCartaFianzaSelectedString;
    }

    public void setRegBuscaCartaFianzaSelectedString(String regBuscaCartaFianzaSelectedString) {
        this.regBuscaCartaFianzaSelectedString = regBuscaCartaFianzaSelectedString;
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

    public BigDecimal getRegMontoCartaFianza() {
        return regMontoCartaFianza;
    }

    public void setRegMontoCartaFianza(BigDecimal regMontoCartaFianza) {
        this.regMontoCartaFianza = regMontoCartaFianza;
    }

    public Date getRegFechVigenciaIni() {
        return regFechVigenciaIni;
    }

    public void setRegFechVigenciaIni(Date regFechVigenciaIni) {
        this.regFechVigenciaIni = regFechVigenciaIni;
    }

    public Date getRegFechVigenciaFin() {
        return regFechVigenciaFin;
    }

    public void setRegFechVigenciaFin(Date regFechVigenciaFin) {
        this.regFechVigenciaFin = regFechVigenciaFin;
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

    public String getNomArchivoCFAnterior() {
        return nomArchivoCFAnterior;
    }

    public void setNomArchivoCFAnterior(String nomArchivoCFAnterior) {
        this.nomArchivoCFAnterior = nomArchivoCFAnterior;
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

    public String getNomArchivoLMAnterior() {
        return nomArchivoLMAnterior;
    }

    public void setNomArchivoLMAnterior(String nomArchivoLMAnterior) {
        this.nomArchivoLMAnterior = nomArchivoLMAnterior;
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

    public String getNomArchivoLocalAnterior() {
        return nomArchivoLocalAnterior;
    }

    public void setNomArchivoLocalAnterior(String nomArchivoLocalAnterior) {
        this.nomArchivoLocalAnterior = nomArchivoLocalAnterior;
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

    public List<TipoSeguridad> getRegFormasServTecnoSegList() {
        return regFormasServTecnoSegList;
    }

    public void setRegFormasServTecnoSegList(List<TipoSeguridad> regFormasServTecnoSegList) {
        this.regFormasServTecnoSegList = regFormasServTecnoSegList;
    }

    public String[] getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(String[] selectedOptions) {
        this.selectedOptions = selectedOptions;
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

    public String getSelectOptionOtroVal() {
        return selectOptionOtroVal;
    }

    public void setSelectOptionOtroVal(String selectOptionOtroVal) {
        this.selectOptionOtroVal = selectOptionOtroVal;
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

    public boolean isRegDisabledFormServTecn() {
        return regDisabledFormServTecn;
    }

    public void setRegDisabledFormServTecn(boolean regDisabledFormServTecn) {
        this.regDisabledFormServTecn = regDisabledFormServTecn;
    }

    public boolean isRegDisabledFormResolucionRL() {
        return regDisabledFormResolucionRL;
    }

    public void setRegDisabledFormResolucionRL(boolean regDisabledFormResolucionRL) {
        this.regDisabledFormResolucionRL = regDisabledFormResolucionRL;
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

    public List<SspRegistroEvento> getLstObservaciones() {
        return lstObservaciones;
    }

    public void setLstObservaciones(List<SspRegistroEvento> lstObservaciones) {
        this.lstObservaciones = lstObservaciones;
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

    public boolean isGrabaSolicitud() {
        return grabaSolicitud;
    }

    public void setGrabaSolicitud(boolean grabaSolicitud) {
        this.grabaSolicitud = grabaSolicitud;
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

    public String getTipoRegistroCodProg() {
        return tipoRegistroCodProg;
    }

    public void setTipoRegistroCodProg(String tipoRegistroCodProg) {
        this.tipoRegistroCodProg = tipoRegistroCodProg;
    }

    public boolean isRenderLocalDISCA() {
        return renderLocalDISCA;
    }

    public void setRenderLocalDISCA(boolean renderLocalDISCA) {
        this.renderLocalDISCA = renderLocalDISCA;
    }

    public boolean isRenderMostrarFormVigilanciaPrivada() {
        return renderMostrarFormVigilanciaPrivada;
    }

    public void setRenderMostrarFormVigilanciaPrivada(boolean renderMostrarFormVigilanciaPrivada) {
        this.renderMostrarFormVigilanciaPrivada = renderMostrarFormVigilanciaPrivada;
    }

    public HashMap getEmptyModel_vistaDISCA() {
        return emptyModel_vistaDISCA;
    }

    public void setEmptyModel_vistaDISCA(HashMap emptyModel_vistaDISCA) {
        this.emptyModel_vistaDISCA = emptyModel_vistaDISCA;
    }

    public String getRegistro_id_vistaDISCA() {
        return registro_id_vistaDISCA;
    }

    public void setRegistro_id_vistaDISCA(String registro_id_vistaDISCA) {
        this.registro_id_vistaDISCA = registro_id_vistaDISCA;
    }

    public SspRegistro getRegistro_resol() {
        return registro_resol;
    }

    public void setRegistro_resol(SspRegistro registro_resol) {
        this.registro_resol = registro_resol;
    }

    public SspRegistroRegDisca getRegistro_regDisca() {
        return registro_regDisca;
    }

    public void setRegistro_regDisca(SspRegistroRegDisca registro_regDisca) {
        this.registro_regDisca = registro_regDisca;
    }

    public TipoBaseGt getMod_vinculadaSelected() {
        return mod_vinculadaSelected;
    }

    public void setMod_vinculadaSelected(TipoBaseGt mod_vinculadaSelected) {
        this.mod_vinculadaSelected = mod_vinculadaSelected;
    }

    public List<SbDistritoGt> getListaDistritoMapaLocal() {
        return listaDistritoMapaLocal;
    }

    public void setListaDistritoMapaLocal(List<SbDistritoGt> listaDistritoMapaLocal) {
        this.listaDistritoMapaLocal = listaDistritoMapaLocal;
    }

    public SspRegistroFacade getEjbSspRegistroFacade() {
        return ejbSspRegistroFacade;
    }

    public void setEjbSspRegistroFacade(SspRegistroFacade ejbSspRegistroFacade) {
        this.ejbSspRegistroFacade = ejbSspRegistroFacade;
    }

    public TipoBaseFacadeGt getEjbTipoBaseFacade() {
        return ejbTipoBaseFacade;
    }

    public void setEjbTipoBaseFacade(TipoBaseFacadeGt ejbTipoBaseFacade) {
        this.ejbTipoBaseFacade = ejbTipoBaseFacade;
    }

    public TipoSeguridadFacade getEjbTipoSeguridadFacade() {
        return ejbTipoSeguridadFacade;
    }

    public void setEjbTipoSeguridadFacade(TipoSeguridadFacade ejbTipoSeguridadFacade) {
        this.ejbTipoSeguridadFacade = ejbTipoSeguridadFacade;
    }

    public SbPersonaFacadeGt getEjbSbPersonaFacade() {
        return ejbSbPersonaFacade;
    }

    public void setEjbSbPersonaFacade(SbPersonaFacadeGt ejbSbPersonaFacade) {
        this.ejbSbPersonaFacade = ejbSbPersonaFacade;
    }

    public SbDireccionFacadeGt getEjbSbDireccionFacade() {
        return ejbSbDireccionFacade;
    }

    public void setEjbSbDireccionFacade(SbDireccionFacadeGt ejbSbDireccionFacade) {
        this.ejbSbDireccionFacade = ejbSbDireccionFacade;
    }

    public SbUsuarioFacadeGt getEjbSbUsuarioFacadeGt() {
        return ejbSbUsuarioFacadeGt;
    }

    public void setEjbSbUsuarioFacadeGt(SbUsuarioFacadeGt ejbSbUsuarioFacadeGt) {
        this.ejbSbUsuarioFacadeGt = ejbSbUsuarioFacadeGt;
    }

    public SbPartidaSunarpFacade getEjbSbPartidaSunarpFacade() {
        return ejbSbPartidaSunarpFacade;
    }

    public void setEjbSbPartidaSunarpFacade(SbPartidaSunarpFacade ejbSbPartidaSunarpFacade) {
        this.ejbSbPartidaSunarpFacade = ejbSbPartidaSunarpFacade;
    }

    public SbAsientoSunarpFacade getEjbSbAsientoSunarpFacade() {
        return ejbSbAsientoSunarpFacade;
    }

    public void setEjbSbAsientoSunarpFacade(SbAsientoSunarpFacade ejbSbAsientoSunarpFacade) {
        this.ejbSbAsientoSunarpFacade = ejbSbAsientoSunarpFacade;
    }

    public SbAsientoPersonaFacade getEjbSbAsientoPersonaFacade() {
        return ejbSbAsientoPersonaFacade;
    }

    public void setEjbSbAsientoPersonaFacade(SbAsientoPersonaFacade ejbSbAsientoPersonaFacade) {
        this.ejbSbAsientoPersonaFacade = ejbSbAsientoPersonaFacade;
    }

    public SbPartidaSunarp getPartidaSunarpVEH() {
        return partidaSunarpVEH;
    }

    public void setPartidaSunarpVEH(SbPartidaSunarp partidaSunarpVEH) {
        this.partidaSunarpVEH = partidaSunarpVEH;
    }

    public SbAsientoSunarp getAsientoSunarpVEH() {
        return asientoSunarpVEH;
    }

    public void setAsientoSunarpVEH(SbAsientoSunarp asientoSunarpVEH) {
        this.asientoSunarpVEH = asientoSunarpVEH;
    }

    public SbAsientoVehiculo getAsientoPersonaVEH() {
        return asientoPersonaVEH;
    }

    public void setAsientoPersonaVEH(SbAsientoVehiculo asientoPersonaVEH) {
        this.asientoPersonaVEH = asientoPersonaVEH;
    }

    public SbAsientoVehiculoFacade getEjbSbAsientoVehiculoFacade() {
        return ejbSbAsientoVehiculoFacade;
    }

    public void setEjbSbAsientoVehiculoFacade(SbAsientoVehiculoFacade ejbSbAsientoVehiculoFacade) {
        this.ejbSbAsientoVehiculoFacade = ejbSbAsientoVehiculoFacade;
    }

    public SspRepresentanteRegistroFacade getEjbSspRepresentanteRegistroFacade() {
        return ejbSspRepresentanteRegistroFacade;
    }

    public void setEjbSspRepresentanteRegistroFacade(SspRepresentanteRegistroFacade ejbSspRepresentanteRegistroFacade) {
        this.ejbSspRepresentanteRegistroFacade = ejbSspRepresentanteRegistroFacade;
    }

    public SspRegistroEventoFacade getEjbSspRegistroEventoFacade() {
        return ejbSspRegistroEventoFacade;
    }

    public void setEjbSspRegistroEventoFacade(SspRegistroEventoFacade ejbSspRegistroEventoFacade) {
        this.ejbSspRegistroEventoFacade = ejbSspRegistroEventoFacade;
    }

    public SspServicioFacade getEjbSspServicioFacade() {
        return ejbSspServicioFacade;
    }

    public void setEjbSspServicioFacade(SspServicioFacade ejbSspServicioFacade) {
        this.ejbSspServicioFacade = ejbSspServicioFacade;
    }

    public SspFormaFacade getEjbSspFormaFacade() {
        return ejbSspFormaFacade;
    }

    public void setEjbSspFormaFacade(SspFormaFacade ejbSspFormaFacade) {
        this.ejbSspFormaFacade = ejbSspFormaFacade;
    }

    public SspRepresentantePublicoFacade getEjbRepresentantePublicoFacade() {
        return ejbRepresentantePublicoFacade;
    }

    public void setEjbRepresentantePublicoFacade(SspRepresentantePublicoFacade ejbRepresentantePublicoFacade) {
        this.ejbRepresentantePublicoFacade = ejbRepresentantePublicoFacade;
    }

    public SbDistritoFacadeGt getEjbSbDistritoFacadeGtFacade() {
        return ejbSbDistritoFacadeGtFacade;
    }

    public void setEjbSbDistritoFacadeGtFacade(SbDistritoFacadeGt ejbSbDistritoFacadeGtFacade) {
        this.ejbSbDistritoFacadeGtFacade = ejbSbDistritoFacadeGtFacade;
    }

    public SspLocalAutorizacionFacade getEjbSspLocalAutorizacionFacade() {
        return ejbSspLocalAutorizacionFacade;
    }

    public void setEjbSspLocalAutorizacionFacade(SspLocalAutorizacionFacade ejbSspLocalAutorizacionFacade) {
        this.ejbSspLocalAutorizacionFacade = ejbSspLocalAutorizacionFacade;
    }

    public SspTipoUsoLocalFacade getEjbSspTipoUsoLocalFacade() {
        return ejbSspTipoUsoLocalFacade;
    }

    public void setEjbSspTipoUsoLocalFacade(SspTipoUsoLocalFacade ejbSspTipoUsoLocalFacade) {
        this.ejbSspTipoUsoLocalFacade = ejbSspTipoUsoLocalFacade;
    }

    public SspAlmacenFacade getEjbSspAlmacenFacade() {
        return ejbSspAlmacenFacade;
    }

    public void setEjbSspAlmacenFacade(SspAlmacenFacade ejbSspAlmacenFacade) {
        this.ejbSspAlmacenFacade = ejbSspAlmacenFacade;
    }

    public SspContactoFacade getEjbSspContactoFacade() {
        return ejbSspContactoFacade;
    }

    public void setEjbSspContactoFacade(SspContactoFacade ejbSspContactoFacade) {
        this.ejbSspContactoFacade = ejbSspContactoFacade;
    }

    public SspLocalRegistroFacade getEjbSspLocalRegistroFacade() {
        return ejbSspLocalRegistroFacade;
    }

    public void setEjbSspLocalRegistroFacade(SspLocalRegistroFacade ejbSspLocalRegistroFacade) {
        this.ejbSspLocalRegistroFacade = ejbSspLocalRegistroFacade;
    }

    public SspLicenciaMunicipalFacade getEjbSspLicenciaMunicipalFacade() {
        return ejbSspLicenciaMunicipalFacade;
    }

    public void setEjbSspLicenciaMunicipalFacade(SspLicenciaMunicipalFacade ejbSspLicenciaMunicipalFacade) {
        this.ejbSspLicenciaMunicipalFacade = ejbSspLicenciaMunicipalFacade;
    }

    public SspParticipanteFacade getEjbSspParticipanteFacade() {
        return ejbSspParticipanteFacade;
    }

    public void setEjbSspParticipanteFacade(SspParticipanteFacade ejbSspParticipanteFacade) {
        this.ejbSspParticipanteFacade = ejbSspParticipanteFacade;
    }

    public SspCartaFianzaFacade getEjbSspCartaFianzaFacade() {
        return ejbSspCartaFianzaFacade;
    }

    public void setEjbSspCartaFianzaFacade(SspCartaFianzaFacade ejbSspCartaFianzaFacade) {
        this.ejbSspCartaFianzaFacade = ejbSspCartaFianzaFacade;
    }

    public SbParametroFacade getEjbSbParametroFacade() {
        return ejbSbParametroFacade;
    }

    public void setEjbSbParametroFacade(SbParametroFacade ejbSbParametroFacade) {
        this.ejbSbParametroFacade = ejbSbParametroFacade;
    }

    public SbNumeracionFacade getEjbSbNumeracionFacade() {
        return ejbSbNumeracionFacade;
    }

    public void setEjbSbNumeracionFacade(SbNumeracionFacade ejbSbNumeracionFacade) {
        this.ejbSbNumeracionFacade = ejbSbNumeracionFacade;
    }

    public SbRecibosFacade getEjbSbRecibosFacade() {
        return ejbSbRecibosFacade;
    }

    public void setEjbSbRecibosFacade(SbRecibosFacade ejbSbRecibosFacade) {
        this.ejbSbRecibosFacade = ejbSbRecibosFacade;
    }

    public ExpedienteFacade getEjbExpedienteFacade() {
        return ejbExpedienteFacade;
    }

    public void setEjbExpedienteFacade(ExpedienteFacade ejbExpedienteFacade) {
        this.ejbExpedienteFacade = ejbExpedienteFacade;
    }

    public SbRelacionPersonaFacadeGt getEjbSbRelacionPersonaFacadeGt() {
        return ejbSbRelacionPersonaFacadeGt;
    }

    public void setEjbSbRelacionPersonaFacadeGt(SbRelacionPersonaFacadeGt ejbSbRelacionPersonaFacadeGt) {
        this.ejbSbRelacionPersonaFacadeGt = ejbSbRelacionPersonaFacadeGt;
    }

    public SbDerivacionCydocFacade getEjbSbDerivacionCydocFacade() {
        return ejbSbDerivacionCydocFacade;
    }

    public void setEjbSbDerivacionCydocFacade(SbDerivacionCydocFacade ejbSbDerivacionCydocFacade) {
        this.ejbSbDerivacionCydocFacade = ejbSbDerivacionCydocFacade;
    }

    public SbPaisFacadeGt getEjbSbPaisFacadeGt() {
        return ejbSbPaisFacadeGt;
    }

    public void setEjbSbPaisFacadeGt(SbPaisFacadeGt ejbSbPaisFacadeGt) {
        this.ejbSbPaisFacadeGt = ejbSbPaisFacadeGt;
    }

    public SspVisacionFacade getEjbSspVisacionFacade() {
        return ejbSspVisacionFacade;
    }

    public void setEjbSspVisacionFacade(SspVisacionFacade ejbSspVisacionFacade) {
        this.ejbSspVisacionFacade = ejbSspVisacionFacade;
    }

    public SspRequisitoFacade getEjbSspRequisitoFacade() {
        return ejbSspRequisitoFacade;
    }

    public void setEjbSspRequisitoFacade(SspRequisitoFacade ejbSspRequisitoFacade) {
        this.ejbSspRequisitoFacade = ejbSspRequisitoFacade;
    }

    public SspArchivoFacade getEjbSspArchivoFacade() {
        return ejbSspArchivoFacade;
    }

    public void setEjbSspArchivoFacade(SspArchivoFacade ejbSspArchivoFacade) {
        this.ejbSspArchivoFacade = ejbSspArchivoFacade;
    }

    public SbNumeracionFacade getEjbNumeracionFacade() {
        return ejbNumeracionFacade;
    }

    public void setEjbNumeracionFacade(SbNumeracionFacade ejbNumeracionFacade) {
        this.ejbNumeracionFacade = ejbNumeracionFacade;
    }

    public SspResolucionFacade getEjbSspResolucionFacade() {
        return ejbSspResolucionFacade;
    }

    public void setEjbSspResolucionFacade(SspResolucionFacade ejbSspResolucionFacade) {
        this.ejbSspResolucionFacade = ejbSspResolucionFacade;
    }

    public NeDocumentoFacade getNeDocumentoFacade() {
        return neDocumentoFacade;
    }

    public void setNeDocumentoFacade(NeDocumentoFacade neDocumentoFacade) {
        this.neDocumentoFacade = neDocumentoFacade;
    }

    public SspRegistroRegDiscaFacade getEjbSspRegistroRegDiscaFacade() {
        return ejbSspRegistroRegDiscaFacade;
    }

    public void setEjbSspRegistroRegDiscaFacade(SspRegistroRegDiscaFacade ejbSspRegistroRegDiscaFacade) {
        this.ejbSspRegistroRegDiscaFacade = ejbSspRegistroRegDiscaFacade;
    }

    public SbEmpresaExtranjeraFacade getEjbSbEmpresaExtranjera() {
        return ejbSbEmpresaExtranjera;
    }

    public void setEjbSbEmpresaExtranjera(SbEmpresaExtranjeraFacade ejbSbEmpresaExtranjera) {
        this.ejbSbEmpresaExtranjera = ejbSbEmpresaExtranjera;
    }

    public String getEmpresaAseguradora() {
        return empresaAseguradora;
    }

    public void setEmpresaAseguradora(String empresaAseguradora) {
        this.empresaAseguradora = empresaAseguradora;
    }

    public String getRucEmpAseguradora() {
        return rucEmpAseguradora;
    }

    public void setRucEmpAseguradora(String rucEmpAseguradora) {
        this.rucEmpAseguradora = rucEmpAseguradora;
    }

    public String getNroPoliza() {
        return nroPoliza;
    }

    public void setNroPoliza(String nroPoliza) {
        this.nroPoliza = nroPoliza;
    }

    public Date getFecIniPoliza() {
        return fecIniPoliza;
    }

    public void setFecIniPoliza(Date fecIniPoliza) {
        this.fecIniPoliza = fecIniPoliza;
    }

    public Date getFecFinPoliza() {
        return fecFinPoliza;
    }

    public void setFecFinPoliza(Date fecFinPoliza) {
        this.fecFinPoliza = fecFinPoliza;
    }

    public TipoBaseGt getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoBaseGt tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public String getBienesAcustodiar() {
        return bienesAcustodiar;
    }

    public void setBienesAcustodiar(String bienesAcustodiar) {
        this.bienesAcustodiar = bienesAcustodiar;
    }

    public BigDecimal getValorMontoMaximo() {
        return valorMontoMaximo;
    }

    public void setValorMontoMaximo(BigDecimal valorMontoMaximo) {
        this.valorMontoMaximo = valorMontoMaximo;
    }

    public List<TipoBaseGt> getLstMonedas() {
        lstMonedas=ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_MNDA_DOL', 'TP_MNDA_SOL'");
        return lstMonedas;
    }

    public void setLstMondedas(List<TipoBaseGt> lstMonedas) {
        this.lstMonedas = lstMonedas;
    }

    public String getNomArchivoPOL() {
        return nomArchivoPOL;
    }

    public void setNomArchivoPOL(String nomArchivoPOL) {
        this.nomArchivoPOL = nomArchivoPOL;
    }

    public String getNomArchivoPOLAnterior() {
        return nomArchivoPOLAnterior;
    }

    public void setNomArchivoPOLAnterior(String nomArchivoPOLAnterior) {
        this.nomArchivoPOLAnterior = nomArchivoPOLAnterior;
    }

    public SspPoliza getPoliza() {
        return poliza;
    }

    public void setPoliza(SspPoliza poliza) {
        this.poliza = poliza;
    }

    public UploadedFile getFilePOL() {
        return filePOL;
    }

    public void setFilePOL(UploadedFile filePOL) {
        this.filePOL = filePOL;
    }

    public byte[] getPolByte() {
        return polByte;
    }

    public void setPolByte(byte[] polByte) {
        this.polByte = polByte;
    }

    public StreamedContent getArchivoPOL() {
        return archivoPOL;
    }

    public void setArchivoPOL(StreamedContent archivoPOL) {
        this.archivoPOL = archivoPOL;
    }

    public SspVehiculoDinero getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(SspVehiculoDinero vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCarroseria() {
        return carroseria;
    }

    public void setCarroseria(String carroseria) {
        this.carroseria = carroseria;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPartidaRegVeh() {
        return partidaRegVeh;
    }

    public void setPartidaRegVeh(String partidaRegVeh) {
        this.partidaRegVeh = partidaRegVeh;
    }

    public String getAsientoRegVeh() {
        return asientoRegVeh;
    }

    public void setAsientoRegVeh(String asientoRegVeh) {
        this.asientoRegVeh = asientoRegVeh;
    }

    public String getUbicacionVeh() {
        return ubicacionVeh;
    }

    public void setUbicacionVeh(String ubicacionVeh) {
        this.ubicacionVeh = ubicacionVeh;
    }

    public List<TipoBaseGt> getRegOficinaRegListVEH() {
        return regOficinaRegListVEH;
    }

    public void setRegOficinaRegListVEH(List<TipoBaseGt> regOficinaRegListVEH) {
        this.regOficinaRegListVEH = regOficinaRegListVEH;
    }

    public TipoBaseGt getRegOficinaRegSelectedVEH() {
        return regOficinaRegSelectedVEH;
    }

    public void setRegOficinaRegSelectedVEH(TipoBaseGt regOficinaRegSelectedVEH) {
        this.regOficinaRegSelectedVEH = regOficinaRegSelectedVEH;
    }

    public List<TipoBaseGt> getRegZonaRegListVEH() {
        return regZonaRegListVEH;
    }

    public void setRegZonaRegListVEH(List<TipoBaseGt> regZonaRegListVEH) {
        this.regZonaRegListVEH = regZonaRegListVEH;
    }

    public TipoBaseGt getRegZonaRegSelectedVEH() {
        return regZonaRegSelectedVEH;
    }

    public void setRegZonaRegSelectedVEH(TipoBaseGt regZonaRegSelectedVEH) {
        this.regZonaRegSelectedVEH = regZonaRegSelectedVEH;
    }

    public List<SbDistritoGt> getRegDistritoVEHList() {
        return regDistritoVEHList;
    }

    public void setRegDistritoVEHList(List<SbDistritoGt> regDistritoVEHList) {
        this.regDistritoVEHList = regDistritoVEHList;
    }

    public SbDistritoGt getRegDistritoVEHSelected() {
        return regDistritoVEHSelected;
    }

    public void setRegDistritoVEHSelected(SbDistritoGt regDistritoVEHSelected) {
        this.regDistritoVEHSelected = regDistritoVEHSelected;
    }

    public boolean isEsArrendado() {
        return esArrendado;
    }

    public void setEsArrendado(boolean esArrendado) {
        this.esArrendado = esArrendado;
    }

    public String getNomArchivoPartidaRegVeh() {
        return nomArchivoPartidaRegVeh;
    }

    public void setNomArchivoPartidaRegVeh(String nomArchivoPartidaRegVeh) {
        this.nomArchivoPartidaRegVeh = nomArchivoPartidaRegVeh;
    }

    public String getNomArchivoPartidaRegVehAnterior() {
        return nomArchivoPartidaRegVehAnterior;
    }

    public void setNomArchivoPartidaRegVehAnterior(String nomArchivoPartidaRegVehAnterior) {
        this.nomArchivoPartidaRegVehAnterior = nomArchivoPartidaRegVehAnterior;
    }

    public UploadedFile getFilePartidaRegVeh() {
        return filePartidaRegVeh;
    }

    public void setFilePartidaRegVeh(UploadedFile filePartidaRegVeh) {
        this.filePartidaRegVeh = filePartidaRegVeh;
    }

    public byte[] getPartidaVehByte() {
        return partidaVehByte;
    }

    public void setPartidaVehByte(byte[] partidaVehByte) {
        this.partidaVehByte = partidaVehByte;
    }

    public StreamedContent getArchivoPartidaRegVeh() {
        return archivoPartidaRegVeh;
    }

    public void setArchivoPartidaRegVeh(StreamedContent archivoPartidaRegVeh) {
        this.archivoPartidaRegVeh = archivoPartidaRegVeh;
    }

    public String getNomArchivoCAV() {
        return nomArchivoCAV;
    }

    public void setNomArchivoCAV(String nomArchivoCAV) {
        this.nomArchivoCAV = nomArchivoCAV;
    }

    public String getNomArchivoCAVAnterior() {
        return nomArchivoCAVAnterior;
    }

    public void setNomArchivoCAVAnterior(String nomArchivoCAVAnterior) {
        this.nomArchivoCAVAnterior = nomArchivoCAVAnterior;
    }

    public UploadedFile getFilePartidaCAV() {
        return filePartidaCAV;
    }

    public void setFilePartidaCAV(UploadedFile filePartidaCAV) {
        this.filePartidaCAV = filePartidaCAV;
    }

    public byte[] getCavByte() {
        return cavByte;
    }

    public void setCavByte(byte[] cavByte) {
        this.cavByte = cavByte;
    }

    public StreamedContent getArchivoCAV() {
        return archivoCAV;
    }

    public void setArchivoCAV(StreamedContent archivoCAV) {
        this.archivoCAV = archivoCAV;
    }

    public int getCheckTipoPropiedad() {
        return checkTipoPropiedad;
    }

    public void setCheckTipoPropiedad(int checkTipoPropiedad) {
        this.checkTipoPropiedad = checkTipoPropiedad;
    }

    public SspVehiculoDineroFacade getEjbSspVehiculoDineroFacade() {
        return ejbSspVehiculoDineroFacade;
    }

    public void setEjbSspVehiculoDineroFacade(SspVehiculoDineroFacade ejbSspVehiculoDineroFacade) {
        this.ejbSspVehiculoDineroFacade = ejbSspVehiculoDineroFacade;
    }

    public SspPolizaFacade getEjbSspPolizaFacade() {
        return ejbSspPolizaFacade;
    }

    public void setEjbSspPolizaFacade(SspPolizaFacade ejbSspPolizaFacade) {
        this.ejbSspPolizaFacade = ejbSspPolizaFacade;
    }

    
    
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
    private pe.gob.sucamec.bdintegrado.bean.SbAsientoVehiculoFacade ejbSbAsientoVehiculoFacade;
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
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroRegDiscaFacade ejbSspRegistroRegDiscaFacade; 
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbEmpresaExtranjeraFacade ejbSbEmpresaExtranjera; 
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspVehiculoDineroFacade ejbSspVehiculoDineroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspPolizaFacade ejbSspPolizaFacade;
    
    public GsspSolicitudAutorizacionTDVController() {
    }
    
    public String mostrarCrear() {

        regTipoSeguridadSelected = null;
        tipoAdministradoUsuario = "PersonaNatural";
        String URL_Formulario = "";

        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());

        //LISTA RADIO TIPO ADEACUACION
        formTipoRegistroLista = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_REGIST_NOR', 'TP_REGIST_ADECU'");
        formTipoRegistroSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR");
        
        if (regAdminist.getRuc() != null) {
            administradoConRUC = true;
            ActualizarListaModalidadesCreate();
            URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/transporte_cdv/Create";
        } else {
            administradoConRUC = false;
            URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/Mensaje";
        }

        return URL_Formulario;
    }
    
    public void ActualizarListaModalidadesCreate() {

        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

        String inicialRUC = regAdminist.getRuc().substring(0, 2);
        if (inicialRUC.equals("20")) {

            tipoAdministradoUsuario = "PersonaJuridica";
            if (formTipoRegistroSelected.getCodProg().equals("TP_REGIST_ADECU")) {

                //MODALIDADES DESARROLLADOS PARA [ADECUACION]
                regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_VP', 'TP_GSSP_MOD_PP', 'TP_GSSP_MOD_TEC'"); //, 'TP_GSSP_MOD_PCP' , 'TP_GSSP_MOD_PP'

            } else {
                regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_VP', 'TP_GSSP_MOD_TEC', 'TP_GSSP_MOD_PCP', 'TP_GSSP_MOD_PP', 'TP_AUTORIZ_SEL_CLO', 'TP_GSSP_MOD_SCBC', 'TP_GSSP_MOD_SSE', 'TP_GSSP_MOD_DEP', 'TP_GSSP_MOD_CEF', 'TP_GSSP_MOD_TDV'");
            }

        } else {
            tipoAdministradoUsuario = "PersonaNatural";
            if (formTipoRegistroSelected.getCodProg().equals("TP_REGIST_ADECU")) {
                regTipoSeguridadList = null;
            } else {
                regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_SISPE','TP_GSSP_MOD_SISPA'");
            }
        }

    }
    
    public String validarSiguienteSolicAutorizacion(TipoBaseGt regTipoSeguridadSelected, TipoBaseGt tbTipoRegistroSelected) {

        boolean validacion = true;
        String URL_Formulario = "";

        if (tbTipoRegistroSelected == null) {
            JsfUtil.mensajeAdvertencia("Seleccione tipo de registro.");
            validacion = false;

        } else if (regTipoSeguridadSelected == null) {
            validacion = false;
            JsfUtil.mensajeAdvertencia("Es necesario seleccionar tipo de servicio.");

        } else if (tbTipoRegistroSelected.getCodProg().equals("TP_REGIST_ADECU")) {

            formTipoRegistroSelected = tbTipoRegistroSelected;

            Calendar xFechaActualCalendar = Calendar.getInstance();
            Date xFechaActual = JsfUtil.getFechaSinHora(xFechaActualCalendar.getTime());

            String xFechaLimiteAdeacuacion = ejbSbParametroFacade.obtenerParametroXCodProg("TP_VIG_2FECFINADECUA").getValor();
            Date formFechaFinAdeacuacion = JsfUtil.stringToDate(xFechaLimiteAdeacuacion, "dd/MM/yyyy");

            if (xFechaActual.compareTo(JsfUtil.getFechaSinHora(formFechaFinAdeacuacion)) <= 0) {
                validacion = true;
            } else {
                JsfUtil.mensajeAdvertencia("Ud. ya no puede generar un registro de Adecuación. La fecha límite para el registro es hasta " + xFechaLimiteAdeacuacion);
                validacion = false;
            }

            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

            String valDiscaTipoModalidad = "1"; //SEGURIDAD PRIVADA - DISCA
            String xExistResolVigenteDisca = ejbSspRegistroFacade.verificaDISCA_ExisteResolucionVigentePrincipal_BYModalidad(regAdminist, valDiscaTipoModalidad);

            if (xExistResolVigenteDisca.equals("SI")) {
                validacion = true;
            } else {
                validacion = false;
                JsfUtil.mensajeAdvertencia("Ud. No tiene una Resolución Vigente para registrar una Adecuación DL 1213 de la modalidad " + regTipoSeguridadSelected.getNombre());
            }

            if (validacion == true) {
                Integer sp_verificar = ejbSspRegistroFacade.obtenerContadorRegistrosSPP_Aprob_Vigentes_VP(regAdminist.getId());
                System.out.println("sp_verificar->" + sp_verificar);

                if (sp_verificar == 0) {
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("No tiene una resolución aprobada para realizar la modalidad de " + regTipoSeguridadSelected.getNombre());
                } else if (sp_verificar == 1) {
                    validacion = true;
                    //JsfUtil.mensajeAdvertencia("Se pueden crear sucursales");                
                } else if (sp_verificar == 3 || sp_verificar == -3) {
                    validacion = true;
                    //JsfUtil.mensajeAdvertencia("Se puede crear principal"); //hay 2 validadores cuando se vea lo de renovación
                } else if (sp_verificar == 2) {
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("No se puede crear el REGISTRO PRINCIPAL, existe en su bandeja una solicitud en trámite");
                } else {
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("Ocurrió un problema, intentar nuevamente");
                }
            }

        }

        if (validacion == true) {

            if (regTipoSeguridadSelected.getCodProg().equals("TP_GSSP_MOD_TDV")) { //SERVICIO DE TRANSPORTE Y CUSTODIA DE DINERO Y VALORES                
                //tipoRegistroCodProg = "TP_REGIST_NOR"; //INICIAL
                //tipoRegistroCodProg = "TP_REGIST_ADECU"; //ADECUACIÓN 1213

                formTipoRegistroSelected = tbTipoRegistroSelected;
                tipoRegistroCodProg = tbTipoRegistroSelected.getCodProg();

                System.out.println("***********[Crear-tipoRegistroCodProg-->" + tipoRegistroCodProg);

                createFormAutorizacionServicioSeguridad();
                URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/transporte_cdv/Create";

            } else {
                URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/Create";
            }
        }
        return URL_Formulario;
    }
    
    public void createFormAutorizacionServicioSeguridad() {

        iniciarValores_CrearSolicitud();

        estado = EstadoCrud.CREARVIGILANCIAPRIVADA;

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
        
        partidaSunarpVEH = new SbPartidaSunarp();
        partidaSunarpVEH.setId(null);
        partidaSunarpVEH.setLibroRegistral(null);
        partidaSunarpVEH.setPartidaRegistral(null);
        partidaSunarpVEH.setFechaRegistral(null);
        partidaSunarpVEH.setZonaRegistral(null);
        partidaSunarpVEH.setOficinaRegistral(null);
        partidaSunarpVEH.setEstadoPartida(null);
        partidaSunarpVEH.setValidacionWs(null);
        partidaSunarpVEH.setEstadoWs(null);
        partidaSunarpVEH.setFecha(new Date());
        partidaSunarpVEH.setActivo(JsfUtil.TRUE);
        partidaSunarpVEH.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        partidaSunarpVEH.setAudNumIp(JsfUtil.getIpAddress());
        partidaSunarpVEH.setSbAsientoSunarpList(new ArrayList());
        
        asientoSunarpVEH = new SbAsientoSunarp();
        asientoSunarpVEH.setId(null);
        asientoSunarpVEH.setPartidaId(null);
        asientoSunarpVEH.setNroAsiento(null);
        asientoSunarpVEH.setFecha(new Date());
        asientoSunarpVEH.setActivo(JsfUtil.TRUE);
        asientoSunarpVEH.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        asientoSunarpVEH.setAudNumIp(JsfUtil.getIpAddress());
        
        asientoPersonaVEH = new SbAsientoVehiculo();
        asientoPersonaVEH.setId(null);
        asientoPersonaVEH.setAsientoId(null);
        asientoPersonaVEH.setFecha(new Date());
        asientoPersonaVEH.setActivo(JsfUtil.TRUE);
        asientoPersonaVEH.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        asientoPersonaVEH.setAudNumIp(JsfUtil.getIpAddress());
        
        vehiculo = new SspVehiculoDinero();
        poliza = new SspPoliza();

        regDisabledFormServTecn = false;
        //return "/aplicacion/gssp/gsspSolicitudAutorizacion/CreateAutoServSeg";
    }
    
    public void iniciarValores_CrearSolicitud() { 
        
        regParticipacionistaData = null;
        
        accionistaNumDoc =null;  accionistaApePat = null; accionistaApeMat=null; accionistaNombres = null;
        
        regTipoPersonaLista     = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_PER_NAT', 'TP_PER_JUR'");  
        regTipoPersonaSelected  = null; 
        
        regPaisLista            = ejbSbEmpresaExtranjera.findAllPaises();
        regPaisSelected         = null;
        
        regIdSocioEncontrado    = null;
        esArrendado=false;
        
        //Inicializa valos para el MAPA
        renderVerMapa = false;
        lat_x = 0.0;
        long_x = 0.0;

        registro_id_vistaDISCA = null;
        registro_regDisca = new SspRegistroRegDisca();
        renderMostrarFormVigilanciaPrivada = false;

        //Resolución del Local Principal (Autorizacion Aprobada del Departamento)
        resolucionPrincipal = null;

        //El boton aceptar para grabar esta deshabilitado por defecto
        grabaSolicitud = false;

        //El boton nuevo Local se habilita por defecto
        regBuscaDepartamentoPrincipal = false;

        //Habilita para crear una nueva Carta Fianza
        //nuevaCartaFianza();**

        //Inicializa los valores como DNI por defecto 
        cantNumerosDniCarnetTipoDocRepresentanteLegal = "8";

        //Inicializa la fecha minima para ser mayor de edad
        //fechaMayorDeEdadCalendario = getFechaMinMayorEdad();**

        //calcularFechaMinimaCalendario();****
        //calcularFechaMaximaCalendario();****

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
        System.out.println("p_user->" + p_user);
        System.out.println("getTipoDoc->" + p_user.getTipoDoc());
        System.out.println("getNumDoc->" + p_user.getNumDoc());
        System.out.println("getLogin->" + p_user.getLogin());
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());

        System.out.println("=========================================");
        System.out.println("regUserAdminist->" + regUserAdminist);
        System.out.println("regUserAdminist.getId()->" + regUserAdminist.getId());

        administradoConRUC = false;

        if (regAdminist.getRuc() != null) {
            administradoConRUC = true;
            if (regAdminist.getRuc().subSequence(0, 2).equals("20")) {
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
        //listarCartaFianzas();*****

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
        regTipoProcesoId = null;

        if (tipoAdministradoUsuario.equals("PersonaJuridica")) {
            //TP_GSSP_MOD_VP        SERVICIO DE VIGILANCIA PRIVADA
            //TP_GSSP_MOD_SCBC      SERVICIO DE CUSTODIA DE BIENES CONTROLADOS
            //TP_GSSP_MOD_TEC       SERVICIO DE TECNOLOGÍA DE SEGURIDAD
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_TDV'");
            regTipoSeguridadSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_TDV");
            regTipoProcesoId = regTipoSeguridadSelected;
        } else {
            //TP_GSSP_MOD_SISPE     SERVICIO INDIVIDUAL DE SEGURIDAD PERSONAL
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_SISPE'");
            regTipoSeguridadSelected = null;
        }

        regFormaTipoSeguridadSelected = null;

        regTipoRegistroList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_REGIST");
        //Cuando es un registro normal INICIAL
        if (tipoRegistroCodProg.equals("TP_REGIST_NOR")) {
            regTipoRegistroSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR"); //INICIAL
            renderMostrarFormVigilanciaPrivada = true;
        } else if (tipoRegistroCodProg.equals("TP_REGIST_ADECU")) {
            //Para el Tipo de Registro para ADECUACION 1213
            renderMostrarFormVigilanciaPrivada = false;
            regTipoRegistroSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_ADECU"); //ADECUACIÓN 1213
        }

        regTipoOperacionList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_OPE");
        regTipoOperacionSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"); //INICIAL 

        //SI_ARMA_SSP   SIN ARMA
        //NO_ARMA_SSP   CON ARMA
        regServicioPrestadoList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'SI_ARMA_SSP'");
        //regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP");
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

        regDistritoRLSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");

        //regTipLocalLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");        
        regTipLocalLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_LOCAL");
        regTipLocalLPSSelected = null;

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
        regZonaRegListVEH = regZonaRegListRL;
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

        if (regTipLocalLPSSelected == null) {
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL
        } else //Si es Sucursal es Responsable Legal
        if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RESLEG"); //RESPONSABLE LEGAL
        } else {
            //Caso contrario es Representante Legal
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL
        }
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
        
        empresaAseguradora=null;
        rucEmpAseguradora=null;
        nroPoliza=null;
        fecIniPoliza=null;
        fecFinPoliza=null;
        tipoMoneda=null;
        bienesAcustodiar=null;
        valorMontoMaximo=null;
        lstMonedas=null;
        nomArchivoPOL=null;
        nomArchivoPOLAnterior=null;
        poliza=null;
        filePOL=null;
        polByte=null;
        archivoPOL=null;
        
        //===================================================
        //=========  VEHICULOS   ============
        //===================================================
        
        vehiculo=null;
        categoria=null;
        carroseria=null;
        placa=null;
        partidaRegVeh=null;
        asientoRegVeh=null;
        ubicacionVeh=null;
        //regOficinaRegListVEH=null;
        regOficinaRegSelectedVEH=null;
        //regZonaRegListVEH=null;
        regZonaRegSelectedVEH=null;
        regDistritoVEHList=null;
        regDistritoVEHSelected=null;
        esArrendado=false;
    
        nomArchivoPartidaRegVeh=null;
        nomArchivoPartidaRegVehAnterior=null;
        filePartidaRegVeh=null;
        partidaVehByte=null;
        archivoPartidaRegVeh=null;
    
        nomArchivoCAV=null;
        nomArchivoCAVAnterior=null;
        filePartidaCAV=null;
        cavByte=null;
        archivoCAV=null;
        
        

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

        if (regTipLocalLPSSelected == null) {
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL
        } else //Si es Sucursal es Responsable Legal
        if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RESLEG"); //RESPONSABLE LEGAL
        } else {
            //Caso contrario es Representante Legal
            sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL
        }
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
        if (regTipLocalLPSSelected != null) {

            //Actualiza la lista del Modal de Nuevo Local con el mismo tipo local PRINCIPAL o SUCURSAL
            regTipLocalLPSSelected_Form = regTipLocalLPSSelected;

            resolucionPrincipal = null; //Limpia la Resolución Principal por Departamento
            //Local Principal
            System.out.println("emptyModel_vistaDISCA->"+emptyModel_vistaDISCA);
            if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                //Filtra todos los departamentos que no tengan solicitudes para el Departamento y Autorizacion aprobadas por Local Principal  
                //regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo(""); 

                //Cuando es un registro normal INICIAL
                if (tipoRegistroCodProg.equals("TP_REGIST_NOR")) {
                    regDistritoLPSList = ejbSbDistritoFacadeGtFacade.buscarDistritosEmpresaXDepartamentoSolicitudLocalPrincipal(regAdminist.getId(), regTipoSeguridadSelected.getId(), registro.getId());
                } else if (tipoRegistroCodProg.equals("TP_REGIST_ADECU")) {
                    //Para el Tipo de Registro para ADECUACION 1213
                    //Filtra por el Departamento de la Resolución del DISCA                  
                    if (emptyModel_vistaDISCA != null) {
                        String xIdDepa = (emptyModel_vistaDISCA.get("NOM_DPTO").toString().equals("CALLAO")) ? "LIMA" : emptyModel_vistaDISCA.get("NOM_DPTO").toString();
                        regDistritoLPSList = ejbSbDistritoFacadeGtFacade.buscarDistritosEmpresaXDepartamentoSolicitudLocalPrincipalxAdecuacion(regAdminist.getId(), regTipoSeguridadSelected.getId(), registro.getId(), xIdDepa);
                    }
                }

                habilitarCartaFianza = true; //Se Visualiza el Panel de Carta Fianza
            } else if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                //Local Sucursal que tienen una Principal de un departamento
                //Solo departamentos con sus distritos que tienen un Local Principal aprobado
                //regDistritoLPSList = ejbSbDistritoFacadeGtFacade.buscarDistritosEmpresaXDepartamentoTipoLocalPrincipal(regAdminist.getId(), regTipoSeguridadSelected.getId());
                regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
                habilitarCartaFianza = false; //Se Oculta el Panel de Carta Fianza
            }
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
            
        //recorre la lista DE ACCIONISTAS
        for (SspParticipante socioActivo : lstSocios) {
            encontroPersona = false;
            System.out.println("->");
            System.out.println("||||||||||||||=============SOCIO==================||||||||||||||| : "+socioActivo.getId()+" ****"+ socioActivo.getEmpresaExtId());
            //solo los activos
            if(socioActivo.getActivo() == JsfUtil.TRUE){
                
                if (lstPersonasDJ.size() > 0) {
                    for (SspParticipante personaBusca : lstPersonasDJ) {
                            System.out.println("|||||=============PERSONA PJ============= :  "+personaBusca.getId()+" ******* "+ personaBusca.getEmpresaExtId());
                            //SI El ACCIONISTA ->NO<- es EC
                            if(socioActivo.getEmpresaExtId()== null){ 
                                if(personaBusca.getEmpresaExtId()!= null){
                                    //COMPARA de la Lista de PJ con el ACCIONISTA
                                    // empresa extranjera vs persona normal
                                    System.out.println("|||||======================================= empresa extranjera vs persona normal ============= :  ");
                                    if (personaBusca.getEmpresaExtId().getNumDoc().equals(socioActivo.getPersonaId().getNumDoc())) {
                                        System.out.println("|||||=======================================  SI  ============= :  ");
                                        encontroPersona = true;
                                        break;
                                    }
                                }else{
                                    //COMPARA de la Lista de PJ con el ACCIONISTA
                                    // persona normal vs persona normal
                                    System.out.println("|||||======================================= persona normal vs persona normal ============= :  ");
                                    if (personaBusca.getPersonaId().getNumDoc().equals(socioActivo.getPersonaId().getNumDoc())) {
                                        System.out.println("|||||=======================================  SI  ============= :  ");
                                        encontroPersona = true;
                                        break;
                                    }
                                }
                            }else{
                                // si en la lista de PJ la persona -> es EC <- 
                                if(personaBusca.getEmpresaExtId()!=null){
                                    System.out.println("|||||=============si en la lista de PJ la persona -> es EC============= :  ");
                                    if (personaBusca.getEmpresaExtId().getNumDoc().equals(socioActivo.getEmpresaExtId().getNumDoc())) {
                                        System.out.println("|||||=======================================  SI  ============= :  ");
                                        encontroPersona = true;
                                        break;
                                    }
                                }
                                System.out.println("|||||=======================================  NOES  ============= :  ");
                            }

                    }
                }
                
                //Si no lo encontro, lo agrega
                if (!encontroPersona) {
                    System.out.println("|||||=======================================  ADD SOCIO TO PJLIST  ============= :  SI");
                    lstPersonasDJ.add(socioActivo);
                }
                
                /*
                encontroPersona = false;
            detallePersona = new SspParticipante();
            detallePersona.setId(socioActivo.getId());
                if(socioActivo.getEmpresaExtId()!= null){
                    if(socioActivo.getTipoDoc().getCodProg().equals("TP_DOCID_EC")){
                        detallePersona.setEmpresaExtId(socioActivo.getEmpresaExtId());
                        detallePersona.setTipoDoc(socioActivo.getTipoDoc());
                    }
                }
                else{
            detallePersona.setPersonaId(socioActivo.getPersonaId());
                }

            //Antes de Agregarse Se busca si existe la misma persona en la lista
            //Si tiene datos agregados
            if (lstPersonasDJ.size() > 0) {
                for (SspParticipante personaBusca : lstPersonasDJ) {
                        /*if(socioActivo.getEmpresaExtId()== null){
    //                        if(detallePersona.getEmpresaExtId() != null){
    //                            if (personaBusca.getPersonaId().getNumDoc().equals(detallePersona.getEmpresaExtId().getNumDoc())) {
    //                                encontroPersona = true;
    //                                break;
    //                            }
    //                        }
    //                    }else{
                    if (personaBusca.getPersonaId().getNumDoc().equals(detallePersona.getPersonaId().getNumDoc())) {
                        encontroPersona = true;
                        break;
                    }
                }
                        */
                        /*7
                        if(personaBusca.equals(detallePersona)){
                            encontroPersona = true;
                                break;
            }

                    }
                }*/
        /*
            //Si no lo encontro, lo agrega
            if (!encontroPersona) {
                lstPersonasDJ.add(detallePersona);
            }
                
                */
        }
    }
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

        if (regServicioPrestadoSelected == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":cboServPrestado");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el servicio prestado");
            return false;
        }
        if (regTipLocalLPSSelected == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_TipLocal");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el Tipo de Local");
            return false;
        }

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
        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV")) {

            //===============         Validar Panel - Representante Legal   ===============
            /*if(regPersonasRLSelected == null){
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboRepresLegal");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el representante legal");            
            }*/
            //Validamos cuando es Representante Legal o Responsable Legal
            String tipoPersonaLegal;
            //Si es Sucursal es Responsable Legal
            if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                tipoPersonaLegal = "responsable";
            } else {
                //Caso contratrio es Representante Legal
                tipoPersonaLegal = "representante";
            }

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

        //=============      Medios de Contactos - Validación preRegistro   =====================       
        
        if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SISPE")){
            
            if(lstContactos.size() == 0){
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
//                JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
//                JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un registro en Medios de Contacto");
            }else{
                if(lstContactos.size() > 0){
                    short contador_activos = 0;
                    for (SspContacto contacto_x : lstContactos) {
                        //si hay al menos un contacto con ESTADO activo
                        if(contacto_x.getActivo()== JsfUtil.TRUE){
                            contador_activos++;
                        }
                    }

                    if(contador_activos <= 0){  
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
//                        JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
//                        JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
                        JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un registro en Medios de Contacto");
                    }
                }
            }
        }

        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV")) {

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

            if (regTipLocalLPSSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboLPE_TipLocal");
                JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo local");
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

        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV")) {

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

        if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV")) {

            //===========   Accionista, participantes, Socios, Miembros de Directorio o Apoderados  ==============
            if (lstSocios.size() == 0) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipDoc");
                JsfUtil.invalidar(obtenerForm() + ":txtAcc_NroDoc");
                JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipAccionista");
                JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un registro como miembro de socios");
            }else{
                if(lstSocios.size() > 0){
                    short contador_activos = 0;
                    for (SspParticipante sociox : lstSocios) {
                        //si hay al menos un socio con ESTADO activo
                        if(sociox.getActivo()== JsfUtil.TRUE){
                            contador_activos++;
                        }
                    }
            
                    if(contador_activos <= 0){  
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipDoc");
                        JsfUtil.invalidar(obtenerForm() + ":txtAcc_NroDoc");
                        JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipAccionista");
                        JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un registro como miembro de socios");
                    }
                }
            }

        }
        
        
        if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV")){
            //Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 
            if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                //============================   Carta Fianza   ===================================

                if (regNroCartaFianza == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtCF_NroCartaFianza");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de Carta Fianza");
                } else if (regNroCartaFianza.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtCF_NroCartaFianza");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. de Carta Fianza");
                }

                if (regTipoFinancieraCFSelected == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":cboCF_TipFinanciera");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo financiera");
                }

                if (regEntidadFinancieraCFSelected == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":cboCF_EntFinanciera");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar la entidad financiera");
                }

                if (regMontoCartaFianza == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtCF_Monto");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el monto de la carta fianza");
                }

                //Valida si no es nulo y que sea mayor que cero
                if (regMontoCartaFianza != null) {
                    if (Double.parseDouble(("" + regMontoCartaFianza).trim()) <= 0) {
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm() + ":txtCF_Monto");
                        JsfUtil.mensajeAdvertencia("Debe de ingresar el monto de la carta fianza");
                    }
                }

                if (regFechVigenciaIni == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txfCF_FechVigenciaIni");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar la fecha de inicio de la vigencia");
                }

                if (regFechVigenciaFin == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txfCF_FechVigenciaFin");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar la fecha de termino de la vigencia");
                }

                //Vigencia Desde no debe ser mayor a la de hoy dia
                if ((regFechVigenciaIni != null)) {
                    if (JsfUtil.getFechaSinHora(regFechVigenciaIni).compareTo(JsfUtil.getFechaSinHora(getFechaHoy())) > 0) {
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm() + ":txfCF_FechVigenciaIni");
                        JsfUtil.mensajeAdvertencia("La fecha de Vigencia Desde no puede ser mayor que la fecha Actual");

                    }
                }

                //Vigencia Hasta no debe ser menor a la de hoy dia
                if ((regFechVigenciaFin != null)) {
                    if (JsfUtil.getFechaSinHora(getFechaHoy()).compareTo(JsfUtil.getFechaSinHora(regFechVigenciaFin)) > 0) {
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm() + ":txfCF_FechVigenciaFin");
                        JsfUtil.mensajeAdvertencia("La fecha de Vigencia Hasta no puede ser menor que la fecha Actual");

                    }
                }

                //Valida que la Fecha Vigencia Desde no sea mayor a la fecha de Vigencia Hasta
                if ((regFechVigenciaIni != null && regFechVigenciaFin != null)) {
                    if (JsfUtil.getFechaSinHora(regFechVigenciaIni).compareTo(JsfUtil.getFechaSinHora(regFechVigenciaFin)) > 0) {
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm() + ":txfCF_FechVigenciaIni");
                        JsfUtil.mensajeAdvertencia("La fecha de Vigencia Desde no puede ser mayor que la fecha Vigencia Hasta");

                    }
                }

                if (nomArchivoCF == null && nomArchivoCFAnterior == null) {
                    //if(nomArchivoCF == null){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txf_ArchivoCF");
                    JsfUtil.mensajeAdvertencia("Debe de adjuntar el archivo Carta Fianza");
                }

                //Si el Número de la Carta Fianza ya existe y esta Creando una nueva Carta
                //Si es nuevo el registro.getId es null y esta Editando tiene su Id el registro para que no tome en cuenta este mismo registro
                //Si esta habilitado para editar la carta fianza
                if (!regDisabledDatosCartaFianza) {
                    if (regNroCartaFianza != null) {
                        SspCartaFianza encuentraCarta = ejbSspCartaFianzaFacade.buscarCartaFianzaPorNumero(regAdminist.getId(), regNroCartaFianza.toUpperCase().trim(), registro.getId());
                        if (encuentraCarta != null) {
                            validacion = false;
                            JsfUtil.invalidar(obtenerForm() + ":txtCF_NroCartaFianza");
                            JsfUtil.mensajeAdvertencia("El número de Carta Fianza : " + regNroCartaFianza.toUpperCase() + " ya se encuentra registrada en una Solicitud anterior. Debe seleccionarla de la Lista de Buscar Carta Fianza.");
                        }
                    }
                }
            }
            //Fin de Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 
            
            
            //===========   POLIZA  ==============
            
            if (nroPoliza == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtNroPoliza");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. Póliza");
                } else if (nroPoliza.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtNroPoliza");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el Nro. Póliza");
                }
            
            if (fecIniPoliza == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txt_POL_FechaEmis");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar la fecha de inicio de la Póliza");
                }

            if (fecFinPoliza == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txt_POL_FechaFin");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar la fecha de término de la Póliza");
                }
            
            //Valida que la Fecha Vigencia Desde no sea mayor a la fecha de Vigencia Hasta
                if ((fecIniPoliza != null && fecFinPoliza != null)) {
                    if (JsfUtil.getFechaSinHora(fecIniPoliza).compareTo(JsfUtil.getFechaSinHora(fecFinPoliza)) > 0) {
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm() + ":txt_POL_FechaEmis");
                        JsfUtil.mensajeAdvertencia("La fecha de Vigencia Desde no puede ser mayor que la fecha Vigencia Hasta");

                    }
                }
            
            if (empresaAseguradora == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtEmpAseg");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la empresa aseguradora");
                } else if (empresaAseguradora.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtEmpAseg");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la empresa aseguradora");
                }
            
            if (valorMontoMaximo == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPol_Monto");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el monto de la Póliza");
                }
            
            //Valida si no es nulo y que sea mayor que cero
            if (valorMontoMaximo != null) {
                    if (Double.parseDouble(("" + valorMontoMaximo).trim()) <= 0) {
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm() + ":txtPol_Monto");
                        JsfUtil.mensajeAdvertencia("Debe de ingresar el monto de la Póliza");
                    }
                }
            
            if (tipoMoneda == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":cbo_monedas");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de moneda");
                }
            
            if (bienesAcustodiar == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txt_bienes_custodia");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar bienes a transportar y custodiar");
                } else if (bienesAcustodiar.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txt_bienes_custodia");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar bienes a transportar y custodiar");
                }
            
            if (nomArchivoPOL == null && nomArchivoPOLAnterior == null) {
                    //if(nomArchivoCF == null){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txf_ArchivoPOL");
                    JsfUtil.mensajeAdvertencia("Debe de adjuntar el archivo de la Póliza");
                }
            
             //=========== FIN  POLIZA  ==============
             
             //===========   VEHICULO  ==============
             
            if (categoria == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtCategoria");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la categoría");
                } else if (categoria.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtCategoria");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la categoría");
                }
            
            if (carroseria == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtCarroseria");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la carrosería");
                } else if (carroseria.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtCarroseria");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la carrosería");
                }
            
            if (placa == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPlaca");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la placa");
                } else if (placa.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPlaca");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la placa");
                }
            
            if (partidaRegVeh == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPRV");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la partida registral del vehículo");
                } else if (partidaRegVeh.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPRV");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la partida registral del vehículo");
                }
            
            if (asientoRegVeh == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtARV");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el asiento registral del vehículo");
                } else if (asientoRegVeh.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtARV");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el asiento registral del vehículo");
                }
             
            if (regZonaRegSelectedVEH == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":cboZonaRegistral_VEH");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar la zona registral del vehículo");
                }
            
            if (regOficinaRegSelectedVEH == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":cboOficinaRegistral_VEH");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar la oficina registral del vehículo");
                }
            
            /*if (regDistritoVEHSelected == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":cboRLDistrito_VEH");
                    JsfUtil.mensajeAdvertencia("Debe de seleccionar el distrito de ubicación del vehículo");
                }
            
            if (ubicacionVeh == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtUbiVeh");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la ubicación del vehículo");
                } else if (ubicacionVeh.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtUbiVeh");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar la ubicación del vehículo");
                }*/
            
            if (nomArchivoPartidaRegVeh == null && nomArchivoPartidaRegVehAnterior == null) {
                    //if(nomArchivoCF == null){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txf_ArchivoPRVEH");
                    JsfUtil.mensajeAdvertencia("Debe de adjuntar el archivo de la partida registral del vehículo");
                }
            
            if(checkTipoPropiedad==0){
                validacion = false;
                JsfUtil.mensajeAdvertencia("Debe elegir si el vehículo es propio o arrendado");
            } else if (checkTipoPropiedad==2) {
                 
                if (nomArchivoCAV == null && nomArchivoCAVAnterior == null) {
                    //if(nomArchivoCF == null){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":txf_ArchivoConArr");
                    JsfUtil.mensajeAdvertencia("Debe de adjuntar el contrato de arrendamiento del vehículo");
                }
            
            }
                
        }

        return validacion;
    }
    
    public void mostrarDatosLocalSeleccionado() {
        if (localAutorizacionListado != null) {
            if (localAutorizacionSelectedString != null) {
                renderVerMapa = true;
                for (LocalAutorizacionDetalle localDetalle : localAutorizacionListado) {
                    System.out.println("(Local Seleccionado->" + localAutorizacionSelectedString + ")");

                    if (localDetalle.getId().equals(Long.parseLong(localAutorizacionSelectedString))) {
                        //Setea los datos encontrados
                        //Cuando esta editando si se selecciona el Tipo de Local PRINCIPAL / SUCURSAL
                        if (registro.getId() != null) {
                            regTipLocalLPSSelected = (localDetalle.getTipoLocalId() != null) ? localDetalle.getTipoLocalId() : null;
                            //Actualiza la lista del Modal de Nuevo Local con el mismo tipo local PRINCIPAL o SUCURSAL
                            regTipLocalLPSSelected_Form = regTipLocalLPSSelected;
                        }
                        regTipoViasLPSSelected = (localDetalle.getTipoUbicacionId() != null) ? localDetalle.getTipoUbicacionId() : null;
                        regDireccionLPS = (localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "";
                        regDireccionLPS += (localDetalle.getNroFisico() != null) ? " "+localDetalle.getNroFisico() : "";
                        regDireccionLPS = regDireccionLPS.toUpperCase();
                        regDireccionLPS = regDireccionLPS.replaceAll("NULL", "");
                        regDireccionLPS = regDireccionLPS.trim();

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
    
    public void listarLocalesAutorizXDistrito() {

        //Validamos que haya seleccionado el Tipo Local PRINCIPAL o SUCURSAL
        if (regTipLocalLPSSelected == null) {
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_TipLocal");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el Tipo de Local");
            return;
        }

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

            //Valida cuando es tipo de local PRINCIPAL
            if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                //Validacion si tiene el Administrado una autorizacion por este Departamento
                //que se encuentra activa con estado CREADO, OBSERVADO, TRANSMITIDO, APROBADO
                //Se comenta porque ya se filtran los departamentos y la validacion hace lo mismo
                /*List<SspRegistro> listaRegistroDepartamento = ejbSspRegistroFacade.buscarRegistroEmpresaXDepartamentoTipoLocalPrincipal(regAdminist.getId(), registro.getId(),regDistritoLPSSelected.getId(),regTipoSeguridadSelected.getId(),regTipLocalLPSSelected.getId());
                if (listaRegistroDepartamento != null) {
                    if (listaRegistroDepartamento.size() > 0) {
                        JsfUtil.mensajeAdvertencia("Ya cuenta con una Solicitud para esta Modalidad, con el código de registro: " + listaRegistroDepartamento.get(0).getId() + " de Tipo Local PRINCIPAL, registrada para el Departamento del distrito seleccionado");
                        RequestContext.getCurrentInstance().update("FormRegSolicAuto:pnl_LocalPresEstadoTU"); 
                        regBuscaDepartamentoPrincipal = true;
                        return;
                    }
                }*/
            } else if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                //Sucursal
                //Busca la Resolución Principal Vigente de la Autorización Aprobada del Deparatmento
                resolucionPrincipal = null;
                //List<SspResolucion> listaResolucionDepartamento = ejbSspResolucionFacade.buscarResolucionVigenteEmpresaXDepartamentoTipoLocalPrincipal(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipoSeguridadSelected.getId());
                List<SspResolucion> listaResolucionDepartamento = ejbSspResolucionFacade.buscarResolucionVigenteEmpresaTipoLocalPrincipalAprobado(regAdminist.getId(), regTipoSeguridadSelected.getId());
                if (listaResolucionDepartamento != null) {
                    if (listaResolucionDepartamento.size() > 0) {
                        resolucionPrincipal = listaResolucionDepartamento.get(0);
                        //resolucionPrincipal = resolucionBuscaPrincipal;

                        //Buscamos los datos del Representante legal Ver - PRINCIPAL
                        representanteLegalVer = listaResolucionDepartamento.get(0).getRegistroId().getRepresentanteId();

                        SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
                        Long xEmpresaId = regAdminist.getId();
                        xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(representanteLegalVer.getId(), xEmpresaId);

                        if (xSbAsientoPersonaRL != null) {
                            //existeAsientoSunarpRL = true;
                            regRLPartidaRegistralVer = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getPartidaRegistral();
                            regRLAsientoRegistralVer = xSbAsientoPersonaRL.getAsientoId().getNroAsiento();
                            if (xSbAsientoPersonaRL.getAsientoId().getPartidaId() != null) {
                                regZonaRegSelectedRLVer = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getZonaRegistral().getId());
                                //Filtra las Oficinas Registrales
                                regOficinaRegListRLVer = ejbTipoBaseFacade.listarTipoBaseXCodProg(regZonaRegSelectedRLVer.getCodProg());
                                regOficinaRegSelectedRLVer = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getOficinaRegistral().getId());
                            } else {
                                regZonaRegSelectedRLVer = null;
                                regOficinaRegSelectedRLVer = null;
                                regOficinaRegListRLVer = null;
                            }
                        } else {
                            //existeAsientoSunarpRL = false;
                            regRLPartidaRegistralVer = null;
                            regRLAsientoRegistralVer = null;
                            regZonaRegSelectedRLVer = null;
                            regOficinaRegSelectedRLVer = null;
                            regOficinaRegListRLVer = null;
                        }

                        List<SbDireccionGt> listDireccionPersona = ejbSbDireccionFacade.listarDireccionesXPersona(representanteLegalVer.getId());
                        if (listDireccionPersona.size() > 0) {
                            regTipoViasSelectedVer = listDireccionPersona.get(0).getViaId();
                            regDomicilioRLSelectedVer = listDireccionPersona.get(0).getDireccion();
                            regDistritoRLSelectedVer = listDireccionPersona.get(0).getDistritoId();
                        } else {
                            regTipoViasSelectedVer = null;
                            regDomicilioRLSelectedVer = null;
                            regDistritoRLSelectedVer = null;
                        }
                        //Fin de Buscamos los datos del Representante legal Ver - PRINCIPAL
                    }
                }
            }

            List<SspLocalAutorizacion> listDetalleLocal = new ArrayList<>();
            //listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocal(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId());
            listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocalValida(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId(), registro.getId());
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
    
    public String obtenerForm() {
        switch (estado) {
            case CREARVIGILANCIAPRIVADA:
                return "FormRegSolicAutoTDV";
            case EDITARTDV:
                return "FormRegSolicAutoTDV";
            case VERVIGILANCIAPRIVADA:
                return "FormRegSolicAutoTDV";
        }
        return null;
    }
    
    public static String obtnerRutaServer(String directorio) {
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
    }
    
    public String obtenerMsjeInvalidSizeFileSelLicenciaMunicipalAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selLicenciaMunicipalPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
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
    
    public void btnAgregarLocalAutorizacionMapa() {
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

//        if (regNroFisicoLPS_Form == null) {
//            validacionRegLocal = false;
//            JsfUtil.mensajeAdvertencia("Ingrese nro fisico");
//        } else if (regNroFisicoLPS_Form.equals("")) {
//            validacionRegLocal = false;
//            JsfUtil.mensajeAdvertencia("Ingrese nro fisico");
//        }

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

//            int totLocalExiste = ejbSspLocalAutorizacionFacade.existeLocalAutorizacionDistritoEmpresaTipoLocalRepetido(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId(), regTipoViasLPSSelected_Form.getId(), regDireccionLPS_Form.toUpperCase(), regNroFisicoLPS_Form.toUpperCase());
            int totLocalExiste = ejbSspLocalAutorizacionFacade.existeLocalAutorizacionDistritoEmpresaTipoLocalRepetido(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId(), regTipoViasLPSSelected_Form.getId(), regDireccionLPS_Form.toUpperCase(), "");

            if (totLocalExiste > 0) {
                JsfUtil.mensajeAdvertencia("Los datos de este local ya se encuentra registrado");
                return;
            } else {

                try {

                    LocalAutorizacionDetalle detalleLocalTemp = new LocalAutorizacionDetalle();
                    detalleLocalTemp.setId(JsfUtil.tempIdN());
                    detalleLocalTemp.setTipoUbicacionId(regTipoViasLPSSelected_Form);
                    detalleLocalTemp.setDireccion(regDireccionLPS_Form.toUpperCase());
//                    detalleLocalTemp.setNroFisico(regNroFisicoLPS_Form.toUpperCase());
                    detalleLocalTemp.setDistritoId(regDistritoLPSSelected);
                    detalleLocalTemp.setReferencia(regReferenciaLPS_Form.toUpperCase());
                    detalleLocalTemp.setTipoLocalId(regTipLocalLPSSelected_Form);
                    detalleLocalTemp.setTipoUsoId(regTipUsoLPSSelected_Form);
                    detalleLocalTemp.setGeoLatitud(regGeoLatitudLPS_Form);
                    detalleLocalTemp.setGeoLongitud(regGeoLongitudPS_Form);

                    //Agrega a la lista en forma temporal
                    localAutorizacionListado.add(detalleLocalTemp);
                    localAutorizacionSelectedString = detalleLocalTemp.getId().toString();

                    mostrarDatosLocalSeleccionado();

                    RequestContext.getCurrentInstance().execute("PF('wvDlgMapa').hide()");
                    RequestContext.getCurrentInstance().update("FormRegSolicAutoTDV:pnl_LocalPresEstadoTU");

                    RequestContext.getCurrentInstance().execute("updateController = true;");

                } catch (Exception e) {
                    System.out.println("Exception->" + e.getMessage());
                }

            }

        }

    }
    
    public void onStateChange(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();

        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));
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
    
    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void openDlgMapa() {

        if (regTipoProcesoId == null) {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de servicio");
        } else if (regDistritoLPSSelected == null) {
            JsfUtil.mensajeAdvertencia("Debe seleccionar el ambito de operaciones");
        } else {

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
            String nombre_prov = "";
            String nombre_dep = "";
            long iddist = regDistritoLPSSelected.getId();//10101;
            listaDistritoMapaLocal = ejbSbDistritoFacadeGtFacade.lstDistritosxID(iddist); 
            
            SbDistritoGt distrito_x =  ejbSbDistritoFacadeGtFacade.find(iddist);
            
            nombre_dis      = distrito_x.getNombre();//#{item.provinciaId.departamentoId.nombre} / #{item.provinciaId.nombre} / #{item.nombre};
            nombre_prov      = distrito_x.getProvinciaId().getNombre(); 
            nombre_dep      = distrito_x.getProvinciaId().getDepartamentoId().getNombre();
            
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('wvDlgMapa').show()");
            context.update("frmCuerpoMapa");
            
            String concat1 = "'"+nombre_dep+"', '"+ nombre_prov +"', '"+ nombre_dis+"'";  
            context.execute("irMapa()");
            context.execute("consumirApi(" + concat1 + ")");

        }

    }
    
    public String obtenerMsjeInvalidSizeFileSelPlanoDistribucionAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selPlanoDistribucionPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
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
    
    public void mostrarDomicilioRepresentanteLegal() {
        if (personaDetalleListado != null) {
            if (personaDetalleSelectedString != null) {
                for (PersonaDetalle personaDetalle : personaDetalleListado) {
                    if (personaDetalle.getId().equals(Long.parseLong(personaDetalleSelectedString))) {                       
                                                
                        SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
                        Long xEmpresaId = regAdminist.getId();

                        System.out.println("*****************************************************************");
                        System.out.println("personaDetalleSelectedString->"+personaDetalleSelectedString);                        
                        System.out.println("xEmpresaId->"+xEmpresaId);                        
                        System.out.println("*****************************************************************");
                        
                        xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(Long.parseLong(personaDetalleSelectedString), xEmpresaId);

                        if (xSbAsientoPersonaRL != null) {
                            //valida si es la misma empresa
                            if(xSbAsientoPersonaRL.getEmpresaId().equals(registro.getEmpresaId())){ //=============
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
                            }else{
                                existeAsientoSunarpRL = false;
                                regRLPartidaRegistral = null;
                                regRLAsientoRegistral = null;
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
    
    public void listarOficinasRegistrales_VEH() {

        if (regZonaRegSelectedVEH != null) {
            regOficinaRegListVEH = ejbTipoBaseFacade.listarTipoBaseXCodProg(regZonaRegSelectedVEH.getCodProg());
            regOficinaRegSelectedVEH = null;

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la zona registral");
            regOficinaRegListVEH = null;
            regOficinaRegSelectedVEH = null;
        }

    }
    
    public void openDlgPersonaRepresentanteLegal() {

        //Validamos que haya seleccionado el Tipo Local PRINCIPAL o SUCURSAL
        if (regTipLocalLPSSelected == null) {
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_TipLocal");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el Tipo de Local");
            return;
        }

        //Setea el Titulo del Modal de Representante Legal y la busqueda para retornar los valores
        //Se utiliza el mismo modal para Representante Legal y Accionista/Socio
        buscaNuevoModalRepresentanteLegal = true;
        buscaNuevoModalAccionista = false;
        //Titulo como Representante Legal o Responsable Legal
        if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
            tituloBuscaModalRepresentanteLegal = JsfUtil.bundleBDIntegrado("gsspSolicitudAutorizacion_Titulo_Modal_Responsable_Legal");
        } else {
            tituloBuscaModalRepresentanteLegal = JsfUtil.bundleBDIntegrado("gsspSolicitudAutorizacion_Titulo_Modal_Representante_Legal");
        }

        regTipoDocSelected = null;
        regNroDocBuscar = null;
        regDatoPersonaByNumDocRL = null;
        regRepresentanteFechaNacimiento = null;
        regDisabledRepresentanteFechaNacimiento = true;

        //Lista de Tipo de Documento para el Representante Legal
        regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE' ");
        regTipoDocSelected = null;
        renderEsAccionista = false;

        RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').show()");
        RequestContext.getCurrentInstance().update("frmCuerpoPersona");
        RequestContext.getCurrentInstance().update("dlgRepresentanteLegal");
    }
    
    public void obtenerNumCaractDocumentoRepresentanteLegal() {
        limpiarModalFormAccionista();
        if (regTipoDocSelected != null) {
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {
                regNroDocBuscar             = null;
                cantNumerosDniCarnetTipoDocRepresentanteLegal = "8";
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Deshabilita para DNI
                regDisabledRepresentanteFechaNacimiento = true;
                renderEsAccionista = false;
                
            } else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                regNroDocBuscar             = null;
                cantNumerosMinimaCarnetTipoDocRepresentanteLegal = 9;
                cantNumerosDniCarnetTipoDocRepresentanteLegal = "12";
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Habilita para Carnet de Extranjeria
                regDisabledRepresentanteFechaNacimiento = false;
                renderEsAccionista = false;
            }  else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")) {
                regNroDocBuscar             = null;
                cantNumerosMinimaCarnetTipoDocRepresentanteLegal = 7;
                cantNumerosDniCarnetTipoDocRepresentanteLegal = "20";
                
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Habilita para Carnet de Extranjeria
                regDisabledRepresentanteFechaNacimiento = true;
                renderEsAccionista = false;
//                renderEsAccionista = true;

            }
        }
    }
    
    public void limpiarModalFormAccionista(){
        
        regPaisSelected_id          = null;
        accionistaNombres           = null;
        regDatoPersonaByNumDocRL    = null;
    }
    
    public void BuscarPersonaRepresentanteLegal() {
        boolean validacionPersonaRL = true;
        renderEsAccionista = false;
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
            
            //Valida Empresa consorcio
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")) {
                validacionPersonaRL = validarLongitudEC();
            }
        }
        
        if(validacionPersonaRL){
            
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC") ){
                regParticipacionistaData = new SbEmpresaExtranjera();
                regParticipacionistaData = ejbSbEmpresaExtranjera.selectEmpresaExtActivaxTipoDocyNumDoc(regTipoDocSelected.getId(), regNroDocBuscar);
                if(regParticipacionistaData == null){  
                    //si NO encuentra un registro de Empresa Extranjera 
                    // por tipo de documento y nro de documento

                    //verifica si existe la persona con el NRO DOC
                    /*regDatoPersonaByNumDocRL = ejbSbPersonaFacade.buscarPersonaSel(regTipoDocSelected.getId().toString(), regNroDocBuscar);
                    if(regDatoPersonaByNumDocRL != null){
                        
                        //SI el tipo de documento de la persona encontrada no coincide con el tipo de documento seleccionado en el modal form
                        if(!regDatoPersonaByNumDocRL.getTipoDoc().getCodProg().equals(regTipoDocSelected.getCodProg())){
                            JsfUtil.mensajeAdvertencia("Se encontró un registro con el nro de documento ingresado, pero no coincide con el tipo de documento seleccionado, verifique nuevamente.");
                            regDatoPersonaByNumDocRL = null;
                            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
                            renderEsAccionista = false;
                            return;
                        }                    
                    }else{*/
                        //activar los campos para ingresar los datos 
                        renderEsAccionista = true;
                    /*}*/
                    //limpiar
                    limpiarModalFormAccionista();
//                    renderEsAccionista = false;
                }else{
                    // SI encuentra un registro de Empresa Extranjera 
                    
                    //lenar data form EC
//                    regTipoPersonaSelected  = regParticipacionistaData.getTipoDoc();  
                    regPaisSelected         = regParticipacionistaData.getPaisId();
                    regPaisSelected_id      = regPaisSelected.getId().toString();
                    accionistaNombres       = regParticipacionistaData.getDenominacion();
                    renderEsAccionista = false;
                }
            }else{
            
            Consulta_Service serR = new Consulta_Service();
            PideMigraciones_Service serMigra = new PideMigraciones_Service();

            Consulta port = serR.getConsultaPort();
            PideMigraciones portMigra = serMigra.getPideMigracionesPort();

            wspide.Persona pn = null;
            wspide.ResPideMigra pext = null;

            try {

                //verifica si existe la persona con TIPO DE DOC y NRO DOC
//                regDatoPersonaByNumDocRL = ejbSbPersonaFacade.buscarPersonaSel(regTipoDocSelected.getId().toString(), regNroDocBuscar);
                regDatoPersonaByNumDocRL = ejbSbPersonaFacade.selectPersonaActivaxTipoDocyNumDoc(regTipoDocSelected.getId(), regNroDocBuscar);
                if (regDatoPersonaByNumDocRL == null) {

                    //verifica si existe la persona con el NRO DOC
                    regDatoPersonaByNumDocRL = ejbSbPersonaFacade.buscarPersonaSel(regTipoDocSelected.getId().toString(), regNroDocBuscar);
                    if (regDatoPersonaByNumDocRL != null) {
                        //si el tipo de documento de la persona encontrada no coincide con el tipo de documento seleccionado en el modal form
                        if (!regDatoPersonaByNumDocRL.getTipoDoc().getCodProg().equals(regTipoDocSelected.getCodProg())) {
                            JsfUtil.mensajeAdvertencia("Se encontró un registro con el nro de documento ingresado, pero no coincide con el tipo de documento seleccionado, verifique nuevamente.");
                            regDatoPersonaByNumDocRL = null;
                            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
                            renderEsAccionista = false;
                            return;
                        }
                    }

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
                        }else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC") ) {
                        renderEsAccionista = true;
//                            regDisabledRepresentanteFechaNacimiento = false;
                        JsfUtil.mensaje("No existen datos con el número de Documento de Identidad ingresado, ingrese los datos para registrarlos.");
                    }
                } else {
                    //Cuando lo Encuentra en el base de datos
                    //Cuando lo crea lo inactiva la fecha
                    regRepresentanteFechaNacimiento = null;
                    //Setea la Fecha de Nacimiento de la busqueda
                    if (regDatoPersonaByNumDocRL.getFechaNac() != null) {
                        regRepresentanteFechaNacimiento = regDatoPersonaByNumDocRL.getFechaNac();
                    }                    
                }
//                    }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.mensajeError(ex, JsfUtil.bundle("ErrorDePersistencia"));
            }

        }
    }
    }
    
    public boolean  validarLongitudEC(){
        if (regNroDocBuscar.length() < cantNumerosMinimaCarnetTipoDocRepresentanteLegal) {
//            validacionPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar mínimo " + cantNumerosMinimaCarnetTipoDocRepresentanteLegal + " dígitos, en el número de documento para Carnet de Extranjeria");
            return false;
        }
        if (regNroDocBuscar.length() > Integer.parseInt(cantNumerosDniCarnetTipoDocRepresentanteLegal)) {    
//            validacionPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar máximo " + cantNumerosDniCarnetTipoDocRepresentanteLegal + " dígitos, en el número de documento para Carnet de Extranjeria");
            return false;
        }
        return true;
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
    
    public void btnRegistrarPersonaRL() {
        boolean validarRegistroPersonaRL = true;

        if (regTipoDocSelected == null) {
            validarRegistroPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            JsfUtil.mensajeAdvertencia("Seleccione un tipo de documento");
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            return;
        }
        
        if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")  ) {
            
//            <!--accionistaNumDoc,accionistaApePat,accionistaApeMat,accionistaNombres-->
//            if(regNroDocBuscar == null || regNroDocBuscar == ""){
            if (StringUtils.isEmpty(regNroDocBuscar)) {
                validarRegistroPersonaRL = false;
                JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el nro de documento");
                RequestContext.getCurrentInstance().update("frmCuerpoPersona:txtPersonaNumDoc");
                return;
            }
            
            validarRegistroPersonaRL = validarLongitudEC();
            if(!validarRegistroPersonaRL) return;
            
            if(regPaisSelected_id == null ){
                validarRegistroPersonaRL = false;
                JsfUtil.invalidar(obtenerForm() + ":cboPaisLista"); 
                JsfUtil.mensajeAdvertencia("Debe de seleccionar un País");
                RequestContext.getCurrentInstance().update("frmCuerpoPersona");
                return;
            }
            
//            if(accionistaNombres == null || accionistaNombres == ""){
            if(StringUtils.isEmpty(accionistaNombres) ){
                validarRegistroPersonaRL = false;
                JsfUtil.invalidar(obtenerForm() + ":txtRazonSocial"); 
                JsfUtil.mensajeAdvertencia("Debe de ingresar la Razón Social");
                RequestContext.getCurrentInstance().update("frmCuerpoPersona");
                return;
            } 
            
            }
            
        if(regDatoPersonaByNumDocRL == null && !regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")){
                    validarRegistroPersonaRL = false;
            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar los datos de la persona");
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            return;
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
                //Si es Sucursal es Responsable Legal
                if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                    sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RESLEG"); //RESPONSABLE LEGAL
                } else {
                    //Caso contrario es Representante Legal
                    sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL
                }

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
                    //Si es Sucursal es Responsable Legal
                    if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                        JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Responsable Legal, con este Número de documento: " + regNroDocBuscar);
                    } else {
                        JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Representante Legal, con este Número de documento: " + regNroDocBuscar);
                    }
                    return;
                }

                List<SbPersonaGt> xRLPersona = new ArrayList<SbPersonaGt>();
                xRLPersona = ejbSbPersonaFacade.listarRelacionPersonaXTipoId_idPersOri_idPersDest(sTipoBase.getId(), regAdminist.getId(), regDatoPersonaByNumDocRL.getId());

                if (xRLPersona.size() > 0) {
                    validarRegistroPersonaRL = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                    //Si es Sucursal es Responsable Legal
                    if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                        JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Responsable Legal, con este Número de documento: " + regNroDocBuscar);
                    } else {
                        JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Representante Legal, con este Número de documento: " + regNroDocBuscar);
                    }
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

        }

        //Si se ha llamado el Modal por Registro/Busqueda de Accionista/Socio 
        if (buscaNuevoModalAccionista) {
            if (validarRegistroPersonaRL) {
                if (regTipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {

                    if (regDatoPersonaByNumDocRL != null) {
                        regNombreSocioEncontrado = regDatoPersonaByNumDocRL.getTipoDoc().getNombre() + " " + regDatoPersonaByNumDocRL.getNumDoc() + " - " + regDatoPersonaByNumDocRL.getApePat() + " " + regDatoPersonaByNumDocRL.getApeMat() + " " + regDatoPersonaByNumDocRL.getNombres();
                        regTipDocAccionistaSelected = regDatoPersonaByNumDocRL.getTipoDoc();
                        regNumeroDocAccionista = regDatoPersonaByNumDocRL.getNumDoc();
                        regDatosSocioEncontrado = regDatoPersonaByNumDocRL;
                    }

                } //Actualiza la Fecha de Nacimiento si es Carnet de Extranjeria 
                else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                    if (regDatoPersonaByNumDocRL != null) {
                        regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                        ejbSbPersonaFacade.edit(regDatoPersonaByNumDocRL);
                    }

                    regNombreSocioEncontrado = regDatoPersonaByNumDocRL.getTipoDoc().getNombre() + " " + regDatoPersonaByNumDocRL.getNumDoc() + " - " + regDatoPersonaByNumDocRL.getApePat() + " " + regDatoPersonaByNumDocRL.getApeMat() + " " + regDatoPersonaByNumDocRL.getNombres();
                    regTipDocAccionistaSelected = regDatoPersonaByNumDocRL.getTipoDoc();
                    regNumeroDocAccionista = regDatoPersonaByNumDocRL.getNumDoc();
                    regDatosSocioEncontrado = regDatoPersonaByNumDocRL;
                    
                }else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")){
                    
                    if(regParticipacionistaData != null){ 
                        //VISUALIZAR 
                        regNombreSocioEncontrado    = regParticipacionistaData.getTipoDoc().getNombre() + " " + regParticipacionistaData.getNumDoc() + " - " +  regParticipacionistaData.getDenominacion() ;
                        regNombreSocioEncontrado    = regNombreSocioEncontrado.toUpperCase();
                        regTipDocAccionistaSelected = regParticipacionistaData.getTipoDoc();
                        regNumeroDocAccionista      = regParticipacionistaData.getNumDoc();
//                        regDatosSocioEncontrado = regParticipacionistaData.getDenominacion();
                        regIdSocioEncontrado        = regParticipacionistaData.getId();
                        }else{ 
                            //validar si no existe en la tabla Empresa 
                            SbEmpresaExtranjera existe_Emp_ext = new SbEmpresaExtranjera(); 
                            existe_Emp_ext      = ejbSbEmpresaExtranjera.selectEmpresaExtActivaxTipoDocyNumDoc(regTipoDocSelected.getId(), regNroDocBuscar.trim());
                            if(existe_Emp_ext != null){  
                                //si existe mostrar mensaje 
                                JsfUtil.mensajeAdvertencia("No se puede registrar esta Empresa Consorcio, ya existe un registro con el nro de documento ingresado.");
                                regDatoPersonaByNumDocRL = null; 
                                return;
                            }else{
                                //CREARLO
                                SbEmpresaExtranjera regEmpExt = new SbEmpresaExtranjera();
                                regEmpExt.setId(null);

                                regEmpExt.setPaisId(ejbSbPaisFacadeGt.find(Long.parseLong(regPaisSelected_id)));
                                regEmpExt.setTipoDoc(regTipoDocSelected);
                                regEmpExt.setNumDoc(regNroDocBuscar);
                                regEmpExt.setDenominacion(accionistaNombres.toUpperCase());
                                regEmpExt.setActivo(JsfUtil.TRUE);
                                regEmpExt.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                regEmpExt.setAudNumIp(JsfUtil.getIpAddress());
                                ejbSbEmpresaExtranjera.create(regEmpExt);

                                regNombreSocioEncontrado    = regEmpExt.getTipoDoc().getNombre() + " " + regEmpExt.getNumDoc() + " - " +  regEmpExt.getDenominacion();
                                regNombreSocioEncontrado    = regNombreSocioEncontrado.toUpperCase();
                                regTipDocAccionistaSelected = regEmpExt.getTipoDoc();
                                regNumeroDocAccionista      = regEmpExt.getNumDoc();
        //                        regDatosSocioEncontrado     = regDatoPersonaByNumDocRL;//REVISAR

                                regIdSocioEncontrado        = regEmpExt.getId();   
                            }
                        }                     
                    
                }    
                
                RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm() + ":pnl_Accionistas");
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
//                if(regtextoCorreoContactoLPS == null){
                if(regtextoContactoLPS == null){
                    validacionCont = false;
//                    JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_CorreoElectronico");
                    JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el Correo Electrónico");
                }else{
//                    if(regtextoCorreoContactoLPS.equals("")){
                    if(regtextoContactoLPS.equals("")){
                        validacionCont = false;
//                        JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_CorreoElectronico");
                        JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
                        JsfUtil.mensajeAdvertencia("Debe de ingresar el Correo Electrónico");
                    }
                }
            }else //Valida cuando no es Tipo de Correo Electrónico
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
        
        Boolean existe_princ = false;
        Boolean existe_sec   = false;
        
        if(validacionCont){
            //regPrioridadLPSSelected           PRIORIDAD           tipobase        valor(String    )
//            regTipoMedioContactoLPSSelected   TIPO MEDIO          tipobase        tipoMedioId(tipomedio)
//            regtextoContactoLPS               NRO o CORREO        string          descripcion(String)
            if (lstContactos.size() > 0) {
                    System.out.println("==========agregarTipoContacto()");
                for (SspContacto contact_x : lstContactos) {
                    if(contact_x.getActivo() == JsfUtil.TRUE) {
                        TipoBaseGt cod_prog_prioridad = ejbTipoBaseFacade.verDatosTipoBaseXId(Long.parseLong(contact_x.getValor()) );

                        System.out.println("=====  CONTACTO ENCONTRADO =====");
                        System.out.println("=====contact_x=====: " + contact_x.getValor()+"["+cod_prog_prioridad.getCodProg()+"]" +" |  " + contact_x.getTipoMedioId().getCodProg() + " |  " + contact_x.getDescripcion());
                        System.out.println("==========");
                        System.out.println("regPrioridadLPSSelected.getCodProg(): "+regPrioridadLPSSelected.getCodProg());
                        System.out.println("regTipoMedioContactoLPSSelected.getCodProg(): "+regTipoMedioContactoLPSSelected.getCodProg());
                        System.out.println("regtextoContactoLPS: "+regtextoContactoLPS );
                        //si hay un contacto con Prioridad PRINCIPAL[TP_CONTAC_PRIN] y el 
                        // mismo tipo medio
                        if ("TP_CONTAC_PRIN".equals(regPrioridadLPSSelected.getCodProg()) 
                                && contact_x.getActivo() == JsfUtil.TRUE ) {
    //                        contPrincCorreo++;
                            System.out.println("=====   TRUE PRINC  =====");
                            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
                            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
                            JsfUtil.mensajeAdvertencia("Ya existe un Contacto Principal ");
                            existe_princ = true;
                            regtextoContactoLPS = null;
                            break;

                        }
                        if (cod_prog_prioridad.getCodProg().equals(regPrioridadLPSSelected.getCodProg())
                                && contact_x.getTipoMedioId().getCodProg().equals(regTipoMedioContactoLPSSelected.getCodProg()) 
                                && contact_x.getDescripcion().equals(regtextoContactoLPS.toUpperCase().trim())  
                                && contact_x.getActivo() == JsfUtil.TRUE    ) {
    //                        contPrincCorreo++;
                            System.out.println("=====   TRUE SEC  =====");
                            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
                            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
                            JsfUtil.mensajeAdvertencia("Ya existe un "+contact_x.getTipoMedioId().getNombre()+" secundario");
                            existe_sec = true;
                            regtextoContactoLPS = null;
                            break;

                        } 
                    } 

                }
                
                if(existe_princ || existe_sec) return;
            }
            
            //agregar contacto nuevo a la lista tempral de contactos
            SspContacto ItemContacto = new SspContacto();
            ItemContacto.setId(null);
            ItemContacto.setRegistroId(null);
            ItemContacto.setValor(regPrioridadLPSSelected.getId()+"");
            ItemContacto.setTipoMedioId(regTipoMedioContactoLPSSelected);
            ItemContacto.setDescripcion(regtextoContactoLPS.toUpperCase()); 
            ItemContacto.setActivo(JsfUtil.TRUE); 
            lstContactos.add(ItemContacto);            
             
             
        }

         
        regPrioridadLPSSelected = null;
        regTipoMedioContactoLPSSelected = null;
        regtextoContactoLPS = null;
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

        //Lista de Tipo de Documento para el Representante Legal
        regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE', 'TP_DOCID_EC' ");    
        regTipoDocSelected = null;
        
        regTipoPersonaSelected  = null; 
        regPaisSelected         = null;
        accionistaNombres       = null;
        regPaisSelected_id      = null;
        
        RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').show()");
        RequestContext.getCurrentInstance().update("frmCuerpoPersona");
        RequestContext.getCurrentInstance().update("dlgRepresentanteLegal");

    }
    
    public void actualizarDatosCargo() {
        regCargoSocio = null;
        if (regTipoSocioSelected != null) {
            if (regTipoSocioSelected.getCodProg().equals("TP_ACCNSTA_APOD")
                    || regTipoSocioSelected.getCodProg().equals("TP_ACCNSTA_DIRECT")
                    || regTipoSocioSelected.getCodProg().equals("TP_ACCNSTA_PJAC")) {
                renderAccionesAportesNoaplica = true;
            } else {
                renderAccionesAportesNoaplica = false;
            }
        }else {
            renderAccionesAportesNoaplica = false;
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
            return;
        }

        if (regTipoSocioSelected == null) {
            validacionSocio = false;
            JsfUtil.invalidar(obtenerForm() + ":cboAcc_TipAccionista");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de socio");
            return;
        }

        if (regTipoSocioSelected != null) {
            if (regTipoSocioSelected.getCodProg().equals("TP_ACCNSTA_DIRECT")) {
                if (regCargoSocio == null) {
                    validacionSocio = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtAcc_Cargo");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el cargo del socio");
                    return;
                } else if (regCargoSocio.equals("")) {
                    validacionSocio = false;
                    JsfUtil.invalidar(obtenerForm() + ":txtAcc_Cargo");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el cargo del socio");
                    return;
                }
            }
        }

        boolean encontroNumeroTipo = false;
        //Si tiene el Numero del documento y el Tipo de Accionista/Socio, valida que no existe uno igual
        if (regNumeroDocAccionista != null && regTipoSocioSelected != null) {
            //Si tiene datos en la lista busca si ya existe
            if (lstSocios != null) {
                for (SspParticipante socioBusca : lstSocios) {
                    if(socioBusca.getPersonaId()!=null){
                        if (socioBusca.getPersonaId().getNumDoc().equals(regNumeroDocAccionista) 
                                && socioBusca.getTipoParticipacion().equals(regTipoSocioSelected.getId()) 
                                && socioBusca.getActivo() == JsfUtil.TRUE) {
                        encontroNumeroTipo = true;
                        break;
                    }
                }
                    if(socioBusca.getEmpresaExtId()!=null){
                        if (socioBusca.getEmpresaExtId().getNumDoc().equals(regNumeroDocAccionista) 
                                && socioBusca.getTipoParticipacion().equals(regTipoSocioSelected.getId()) 
                                    && socioBusca.getActivo() == JsfUtil.TRUE) {
                            encontroNumeroTipo = true;
                            break;
                        }
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

//                ItemSocio.setId(contRegSocio);  
                ItemSocio.setId(null);  
                ItemSocio.setTipoDoc(regTipoDocSelected);
                ItemSocio.setActivo(JsfUtil.TRUE);
                //si el tipo de documento de la persona que se agregará como socio es 
                //Empresa Consorcio, entonces registrar empresa_exntrajera_id
                //si es DNI O CE, entonces registro persona_id
                if(regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")){
                    if(regIdSocioEncontrado !=null){
                        //BUSCAR la empresa extranjera para insertarlo en la temporal
                        //o mandar un objeto empresa_ext desde el regitrar persona/empresa
//                        SbEmpresaExtranjera n_empext= new SbEmpresaExtranjera(regIdSocioEncontrado);//REVISAR
                        SbEmpresaExtranjera n_empext_bd = ejbSbEmpresaExtranjera.find(regIdSocioEncontrado);
                        ItemSocio.setEmpresaExtId(n_empext_bd); 
                    }
                }else{
                    ItemSocio.setPersonaId(regDatosSocioEncontrado);
                }
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
                renderAccionesAportesNoaplica = false;

                RequestContext.getCurrentInstance().update(obtenerForm()+":pnl_Accionistas");  
                RequestContext.getCurrentInstance().reset(obtenerForm()+":pnl_Accionistas");
                
            } catch (Exception e) {
                System.out.println("agregaTipoSocio()");
                e.printStackTrace();
            }
        }
        
    }
    
    public String MostrarDescripcionTipoSocio(Long IdTipoSocio) {
        TipoBaseGt xTipoSocio = new TipoBaseGt();
        xTipoSocio = ejbTipoBaseFacade.find(IdTipoSocio);
        return xTipoSocio.getNombre();
    }
    
    public void deleteSocio(SspParticipante socio) {
         
        try {
            for (SspParticipante sociox : lstSocios) {
    //            System.out.println("\n\r"+ socio.getId()+" - "+socio.getPersonaId()+" - "+socio.getTipoParticipacion()+" - "+socio.getCargo());
                if(sociox.getId() !=null){
                    if(sociox.equals(socio)){
//                    listaUpdateParticipanteActivos.add(socio);
                        sociox.setActivo(JsfUtil.FALSE);
                }
                }else{
                    //si el SOCIO es personal normal
                    if(sociox.getPersonaId() !=null){
                        if(sociox.getTipoParticipacion().equals(socio.getTipoParticipacion()) 
                                && sociox.getTipoDoc().equals(socio.getTipoDoc())
                                && sociox.getPersonaId().getNumDoc().equals(socio.getPersonaId().getNumDoc())  ){
    //                    listaUpdateParticipanteActivos.add(socio);
                            sociox.setActivo(JsfUtil.FALSE);
            }
                    }else{
                    //si el SOCIO es EMPRESA EXTRANJERA
                        if(sociox.getTipoParticipacion().equals(socio.getTipoParticipacion()) 
                                && sociox.getTipoDoc().equals(socio.getTipoDoc())
                                && sociox.getEmpresaExtId().getNumDoc().equals(socio.getEmpresaExtId().getNumDoc())  ){
    //                    listaUpdateParticipanteActivos.add(socio);
                            sociox.setActivo(JsfUtil.FALSE);
        }
                    }
                }

//                lstSocios.remove(sociox);
            } 
    
    
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        
     
    }
    
    public void verificarCheckNumeroPorcentajeAcciones() {
        //Si esta marcado por Numero
        if (checkTipoAcciones == 1) {
            regPorcentajeAcciones = null;
            regDisabledNumerojeAcciones = false;
            regDisabledPorcentajeAcciones = true;
        } else if (checkTipoAcciones == 3) {
            //Si esta marcado por Porcentaje
            regNumeroAcciones = null;
            regDisabledNumerojeAcciones = true;
            regDisabledPorcentajeAcciones = true;
        } else {
            //Si esta marcado por Porcentaje
            regNumeroAcciones = null;
            regDisabledNumerojeAcciones = true;
            regDisabledPorcentajeAcciones = false;
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
    
    public void listarEntidadFinancieras() {

        if (regTipoFinancieraCFSelected != null) {

            regEntidadFinancieraCFList = ejbTipoSeguridadFacade.lstTipoSeguridad(regTipoFinancieraCFSelected.getCodProg());
            regEntidadFinancieraCFSelected = null;

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de seguridad.");
        }

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
    
    public String obtenerMsjeInvalidSizeFileSelCartaFianzaAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selCartaFianzaPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
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
    
    public void handleFileUploadPDF_POL(FileUploadEvent event) {
        try {
            if (event != null) {
                filePOL = event.getFile();
                if (JsfUtil.verificarPDF(filePOL)) {
                    polByte = IOUtils.toByteArray(filePOL.getInputstream());
                    archivoPOL = new DefaultStreamedContent(filePOL.getInputstream(), "application/pdf");
                    nomArchivoPOL = filePOL.getFileName();
                } else {
                    filePOL = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleFileUploadPDF_PARVEH(FileUploadEvent event) {
        try {
            if (event != null) {
                filePartidaRegVeh = event.getFile();
                if (JsfUtil.verificarPDF(filePartidaRegVeh)) {
                    partidaVehByte = IOUtils.toByteArray(filePartidaRegVeh.getInputstream());
                    archivoPartidaRegVeh = new DefaultStreamedContent(filePartidaRegVeh.getInputstream(), "application/pdf");
                    nomArchivoPartidaRegVeh = filePartidaRegVeh.getFileName();
                } else {
                    filePartidaRegVeh = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void verificarCheckArrendado() {
        //Si esta marcado Monto
        if (checkTipoPropiedad == 1) {
            esArrendado = false;
        } else {
            esArrendado = true;
        }
    }
    
    public void handleFileUploadPDF_CAV(FileUploadEvent event) {
        try {
            if (event != null) {
                filePartidaCAV = event.getFile();
                if (JsfUtil.verificarPDF(filePartidaCAV)) {
                    cavByte = IOUtils.toByteArray(filePartidaCAV.getInputstream());
                    archivoCAV = new DefaultStreamedContent(filePartidaCAV.getInputstream(), "application/pdf");
                    nomArchivoCAV = filePartidaCAV.getFileName();
                } else {
                    filePartidaCAV = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void activaGrabarBotonAceptar() {
        grabaSolicitud = false;

        if (noAntecedentesPenales == true && noLaboresEntidad == true && noEmpresaSeguridad == true) {
            grabaSolicitud = true;
        }

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

                //Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 
                if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                    //Si no ha seleccionado una Carta Fianza de la Lista
                    if (regBuscaCartaFianzaSelectedString == null) {
                        /* GUARDA NUEVA CARTA FIANZA */

                        String fileNameArchivoCF = "";
                        fileNameArchivoCF = nomArchivoCF;
                        fileNameArchivoCF = "ASSGssp_CF_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CF").toString() + fileNameArchivoCF.substring(fileNameArchivoCF.lastIndexOf('.'), fileNameArchivoCF.length());
                        fileNameArchivoCF = fileNameArchivoCF.toUpperCase();

                        cartaFianza.setId(null);
                        cartaFianza.setNumero(regNroCartaFianza.trim());
                        cartaFianza.setTipoFinancieraId(regTipoFinancieraCFSelected);
                        cartaFianza.setFinancieraId(regEntidadFinancieraCFSelected);
                        cartaFianza.setMoneda(regTipoMonedaCFSelected.getId());
                        cartaFianza.setMonto(regMontoCartaFianza);
                        cartaFianza.setVigenciaInicio(regFechVigenciaIni);
                        cartaFianza.setVigenciaFin(regFechVigenciaFin);
                        cartaFianza.setNombreArchivo(fileNameArchivoCF);
                        cartaFianza.setFecha(new Date());
                        cartaFianza.setActivo(JsfUtil.TRUE);
                        cartaFianza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        cartaFianza.setAudNumIp(JsfUtil.getIpAddress());
                        cartaFianza = (SspCartaFianza) JsfUtil.entidadMayusculas(cartaFianza, "");
                        ejbSspCartaFianzaFacade.create(cartaFianza);
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CF").getValor() + cartaFianza.getNombreArchivo()), cfByte);

                        registro.setCartaFianzaId(cartaFianza);
                        /* FIN DE GUARDA NUEVA CARTA FIANZA */
                    } else {
                        //Si ha seleccionado una Carta Fianza de la Lista hace la relación con la Carta Fianza
                        cartaFianza = ejbSspCartaFianzaFacade.find(Long.parseLong(regBuscaCartaFianzaSelectedString));
                        registro.setCartaFianzaId(cartaFianza);
                    }
                }
                //Fin de Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 

                //Setea el objeto persona una sola vez del Representante legal o Responsable Legal
                regPersonasRLSelected = ejbSbPersonaFacade.find(Long.parseLong(personaDetalleSelectedString));

                /*if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_VP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")){
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

                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    registro.setRepresentanteId(regPersonasRLSelected);
                }

                registro.setRegistroId(null);
                registro.setNroSolicitiud("S/N");
                //registro.setNroSolicitiud(xNroSolicitiud);       
                registro.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registro.setFecha(new Date());
                registro.setNroExpediente(null);

                //Si es por Adecuación ley 1213 guarda la modalidad vinculada del DISCA
                if (tipoRegistroCodProg.equals("TP_REGIST_ADECU")) {
                    registro.setModalidadVinculadaId(mod_vinculadaSelected);
                } else {
                    registro.setModalidadVinculadaId(null);
                }

                //Cuando es Tipo de Local Sucursal graba el registro relacionado de la Resolución del Departamento Principal
                if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                    registro.setRegistroId(resolucionPrincipal.getRegistroId());
                }
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
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    representanteRegistro.setId(null);
                    representanteRegistro.setRegistroId(registro);
                    representanteRegistro.setRepresentanteId(regPersonasRLSelected);
                    if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                        //Graba el Representante Legal
                        representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));
                    } else if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                        //Graba el Responsable Legal
                        representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RESLEG"));
                    }
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
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

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
                            
                    }else{    
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
                        for (LocalAutorizacionDetalle xItemLocal : localAutorizacionListado) {
                            if (localAutorizacionSelectedString.equals(xItemLocal.getId().toString())) {
                                localAutorizacion = new SspLocalAutorizacion();
                                localAutorizacion.setId(null);
                                localAutorizacion.setTipoUbicacionId(xItemLocal.getTipoUbicacionId());
                                localAutorizacion.setDireccion(xItemLocal.getDireccion());
                                localAutorizacion.setNroFisico(xItemLocal.getNroFisico());
                                localAutorizacion.setDistritoId(xItemLocal.getDistritoId());
                                localAutorizacion.setReferencia(xItemLocal.getReferencia());
                                localAutorizacion.setGeoLatitud(xItemLocal.getGeoLatitud());
                                localAutorizacion.setGeoLongitud(xItemLocal.getGeoLongitud());
                                localAutorizacion.setFecha(new Date());
                                localAutorizacion.setActivo(JsfUtil.TRUE);
                                localAutorizacion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                localAutorizacion.setAudNumIp(JsfUtil.getIpAddress());
                                localAutorizacion = (SspLocalAutorizacion) JsfUtil.entidadMayusculas(localAutorizacion, "");
                                ejbSspLocalAutorizacionFacade.create(localAutorizacion);
                            }
                        }
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
                        tipoUsoLocal.setTipoLocalId(regTipLocalLPSSelected);
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
                         
                /* GUARDA DATOS DE MEDIOS DE CONTACTO - form CREAR*/
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SISPE") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
                    if (lstContactos.size() > 0) {
                        for (SspContacto itemContacto : lstContactos) {
                            if (itemContacto.getActivo() == JsfUtil.TRUE) {
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
                    } else {
                        throw new NullPointerException("Lista de Contactos vacía");
//                        JsfUtil.mensajeError("Se necesita registar un medio de contacto.");
                    }
                }
                /* FIN DE GUARDA DATOS DE MEDIOS DE CONTACTO */

 /* GUARDA DATOS DE LICENCIA MUNICIPAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

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
                if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")){   
                    for(SspParticipante itemSocio : lstSocios){
                        if(itemSocio.getActivo() == JsfUtil.TRUE){
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
                }
                /* FIN DE GUARDA LOS PARTICIPANTES/SOCIOS */

                //Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 
                if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                    /* GUARDA ARCHIVO DE CARTA FIANZA */
                    if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                        if (cartaFianza.getNombreArchivo() != null) {

                            SspRequisito requisito = new SspRequisito();
                            requisito.setActivo(JsfUtil.TRUE);
                            requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            requisito.setAudNumIp(JsfUtil.getIpAddress());
                            requisito.setFecha(new Date());
                            requisito.setId(null);
                            requisito.setRegistroId(registro);
                            requisito.setSspArchivoList(new ArrayList());
                            requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CF"));
                            requisito.setValorRequisito(cartaFianza.getNumero());
                            requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");
                            ejbSspRequisitoFacade.create(requisito);

                            SspArchivo archivo = new SspArchivo();
                            archivo.setActivo(JsfUtil.TRUE);
                            archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            archivo.setAudNumIp(JsfUtil.getIpAddress());
                            archivo.setId(null);
                            archivo.setPathupload("Documentos_pathUpload_ASSGssp_CF");
                            archivo.setNombre(cartaFianza.getNombreArchivo());
                            archivo.setRequisitoId(requisito);
                            archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                            archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                            ejbSspArchivoFacade.create(archivo);
                        }

                    }
                    /* FIN DE GUARDA ARCHIVO DE CARTA FIANZA */
                }
                //Fin de Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 

                /* GUARDA ARCHIVO DE LICENCIA MUNICIPAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

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
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

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

                /* GUARDA DATOS DE DISCA DE LA RESOLUCION POR ADECUACION 1213 */
                if (tipoRegistroCodProg.equals("TP_REGIST_ADECU")) {
                    // SI LA RESOLUCION ES DISCA, GRABA LOS DATOS

                    System.out.println("==========================================================================");                    
                    
                    if (renderLocalDISCA) {
                        registro_regDisca.setRegistroId(registro);
                    
                        emptyModel_vistaDISCA = ejbSspResolucionFacade.buscarAutorizacion_vistaDISCA_VigilanciaPrivada(registro_id_vistaDISCA, "1");//para vigilancia privada
                        registro_regDisca.setModEmp(Short.valueOf(emptyModel_vistaDISCA.get("MOD_EMP_DISCA").toString()));
                        registro_regDisca.setNroRd(Integer.parseInt(emptyModel_vistaDISCA.get("NRO_RD_DISCA").toString()));
                        registro_regDisca.setAnoRd(Short.valueOf(emptyModel_vistaDISCA.get("ANO_RD_DISCA").toString()));
                        
                        registro_regDisca.setFecha(new Date());
                        registro_regDisca.setActivo(JsfUtil.TRUE);
                        registro_regDisca.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        registro_regDisca.setAudNumIp(JsfUtil.getIpAddress());
                        registro_regDisca.setIdResolucionDISCA(registro_id_vistaDISCA);
                        ejbSspRegistroRegDiscaFacade.create(registro_regDisca);
                    }
                    System.out.println("ID->" + registro_regDisca.getId());
                    System.out.println("REGISTRO_ID->" + registro_regDisca.getRegistroId().getId());
                    System.out.println("MOD_EMP->" + registro_regDisca.getModEmp());
                    System.out.println("NRO_RD->" + registro_regDisca.getNroRd());
                    System.out.println("ANO_RD->" + registro_regDisca.getAnoRd());
                    System.out.println("ID_RESOLUCION_DISCA->" + registro_regDisca.getIdResolucionDISCA());
                    
                    System.out.println("==========================================================================");
                }
                
                SbAsientoVehiculo xSbAsientoVeh = new SbAsientoVehiculo();
                //xSbAsientoVeh = ejbSbAsientoVehiculoFacade.buscarAsientoByVehiculoId(registro.getId());
                
                if(true){//xSbAsientoVeh==null
                    
                        //partidaSunarpVEH.setId(null);
                        partidaSunarpVEH.setPartidaRegistral(partidaRegVeh);
                        partidaSunarpVEH.setZonaRegistral(regZonaRegSelectedVEH);
                        partidaSunarpVEH.setOficinaRegistral(regOficinaRegSelectedVEH);
                        partidaSunarpVEH.setFecha(new Date());
                        partidaSunarpVEH.setActivo(JsfUtil.TRUE);
                        partidaSunarpVEH.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        partidaSunarpVEH.setAudNumIp(JsfUtil.getIpAddress());
                        partidaSunarpVEH = (SbPartidaSunarp) JsfUtil.entidadMayusculas(partidaSunarpVEH, "");
                        ejbSbPartidaSunarpFacade.create(partidaSunarpVEH);
                        
                        //asientoSunarpVEH.setId(null);
                        asientoSunarpVEH.setPartidaId(partidaSunarpVEH);
                        asientoSunarpVEH.setNroAsiento(asientoRegVeh);
                        asientoSunarpVEH.setFecha(new Date());
                        asientoSunarpVEH.setActivo(JsfUtil.TRUE);
                        asientoSunarpVEH.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoSunarpVEH.setAudNumIp(JsfUtil.getIpAddress());
                        asientoSunarpVEH = (SbAsientoSunarp) JsfUtil.entidadMayusculas(asientoSunarpVEH, "");
                        ejbSbAsientoSunarpFacade.create(asientoSunarpVEH);
                        
                        
                        //SspVehiculoDinero vehiculo=new SspVehiculoDinero();
                        String fileNameArchivoPAR_REG_VEH = "";
                        fileNameArchivoPAR_REG_VEH = nomArchivoPartidaRegVeh;
                        fileNameArchivoPAR_REG_VEH = "ASSGssp_PR_VEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_PR_VEH").toString() + fileNameArchivoPAR_REG_VEH.substring(fileNameArchivoPAR_REG_VEH.lastIndexOf('.'), fileNameArchivoPAR_REG_VEH.length());
                        fileNameArchivoPAR_REG_VEH = fileNameArchivoPAR_REG_VEH.toUpperCase();
                        
                        String fileNameArchivo_CAV = "";
                        
                        if(checkTipoPropiedad==2){
                            fileNameArchivo_CAV = nomArchivoCAV;
                            fileNameArchivo_CAV = "ASSGssp_CAV_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CAV").toString() + fileNameArchivo_CAV.substring(fileNameArchivo_CAV.lastIndexOf('.'), fileNameArchivo_CAV.length());
                            fileNameArchivo_CAV = fileNameArchivo_CAV.toUpperCase();
                        }
                        
                        vehiculo.setId(null);
                        vehiculo.setCarroceria(carroseria);
                        vehiculo.setCategoria(categoria);
                        vehiculo.setPlaca(placa);
                        vehiculo.setRegistroId(registro);
                        vehiculo.setActivo(JsfUtil.TRUE);
                        vehiculo.setAudNumIp(JsfUtil.getIpAddress());
                        vehiculo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        vehiculo.setArrendado((short) checkTipoPropiedad);
                        vehiculo.setInformacionRegistral(asientoSunarpVEH);
                        //vehiculo.setDireccion(ubicacionVeh);
                        //vehiculo.setDistrito(regDistritoVEHSelected);
                        vehiculo.setArchivoRegistral(fileNameArchivoPAR_REG_VEH);
                        vehiculo.setArchivoArrendamiento(fileNameArchivo_CAV);
                        vehiculo = (SspVehiculoDinero) JsfUtil.entidadMayusculas(vehiculo, "");
                        ejbSspVehiculoDineroFacade.create(vehiculo);
                        
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_PRVEH").getValor() + vehiculo.getArchivoRegistral()), partidaVehByte);
                        
                        if(checkTipoPropiedad==2){
                            
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CAV").getValor() + vehiculo.getArchivoArrendamiento()), cavByte);
                        
                        }
                        
                        asientoPersonaVEH.setId(null);
                        asientoPersonaVEH.setAsientoId(asientoSunarpVEH);
                        asientoPersonaVEH.setVehiculoId(vehiculo);
                        asientoPersonaVEH.setFecha(new Date());
                        asientoPersonaVEH.setActivo(JsfUtil.TRUE);
                        asientoPersonaVEH.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoPersonaVEH.setAudNumIp(JsfUtil.getIpAddress());
                        asientoPersonaVEH = (SbAsientoVehiculo) JsfUtil.entidadMayusculas(asientoPersonaVEH, "");
                        ejbSbAsientoVehiculoFacade.create(asientoPersonaVEH);
                        
                        
                        String fileNameArchivoPOL = "";
                        fileNameArchivoPOL = nomArchivoPOL;
                        fileNameArchivoPOL = "ASSGssp_POLIZA_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_POLIZA").toString() + fileNameArchivoPOL.substring(fileNameArchivoPOL.lastIndexOf('.'), fileNameArchivoPOL.length());
                        fileNameArchivoPOL = fileNameArchivoPOL.toUpperCase();
                        
                        poliza.setId(null);
                        poliza.setNroPoliza(nroPoliza);
                        poliza.setEmpresaAseguradora(empresaAseguradora);
                        poliza.setVigenciaIni(fecIniPoliza);
                        poliza.setVigenciaFin(fecFinPoliza);
                        poliza.setMontoMaximo(valorMontoMaximo);
                        poliza.setRegistroId(registro);
                        poliza.setTipoMoneda(tipoMoneda);
                        poliza.setBienes(bienesAcustodiar);
                        poliza.setArchivoPoliza(fileNameArchivoPOL);
                        poliza.setActivo(JsfUtil.TRUE);
                        poliza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        poliza.setAudNumIp(JsfUtil.getIpAddress());
                        poliza = (SspPoliza) JsfUtil.entidadMayusculas(poliza, "");
                        ejbSspPolizaFacade.create(poliza);
                        
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_POLIZA").getValor() + poliza.getArchivoPoliza()), polByte);
                        
                }

                /* FIN GUARDA DATOS DE DISCA DE LA RESOLUCION POR ADECUACION 1213 */
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
    
    public String prepareList() {

        estado = EstadoCrud.BUSCAR;

        filtroBuscarPor = null;
        filtroNumero = null;

        //filtroTipoSeguridadListado = ejbTipoBaseFacade.listarTipoBaseXCodProgsV2("TP_GSSP_AUTORIZACION");
        filtroTipoSeguridadListado = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_VP', 'TP_GSSP_MOD_PP', 'TP_GSSP_MOD_TDV', 'TP_GSSP_MOD_PCP', 'TP_GSSP_MOD_CYA', 'TP_GSSP_MOD_SISPE', 'TP_GSSP_MOD_SISPA', 'TP_GSSP_MOD_TEC', 'TP_GSSP_MOD_ICYA', 'TP_GSSP_MOD_SCBC', 'TP_GSSP_MOD_SSE', 'TP_GSSP_MOD_CEF', 'TP_AUTORIZ_SEL_CLO', 'TP_GSSP_MOD_DEP' ");
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
    
    public void calcularFechaMinimaCalendario() {
        Calendar FechaActual = Calendar.getInstance();
        fechaMinimaCalendario = JsfUtil.getFechaSinHora(FechaActual.getTime());
    }

    public void calcularFechaMaximaCalendario() {
        Calendar FechaActual = Calendar.getInstance();
        fechaMaximaCalendario = JsfUtil.getFechaSinHora(FechaActual.getTime());
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
    
    public boolean renderBtnVer() {
        return true;
    }
    
    public String guardarEditarSolicitud() {

        try {

            boolean validacionDJ = true;
            regTipoMonedaCFSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_MNDA_SOL");

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

                System.out.println("localAutorizacionSelectedString-->" + localAutorizacionSelectedString);

                //Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 
                if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                    /* GUARDA LA CARTA FIANZA */
                    String fileNameArchivoCF = "";

                    //Si ha seleccionado otro archivo genera nombre del archivo
                    if (nomArchivoCF != null) {
                        fileNameArchivoCF = nomArchivoCF;
                        fileNameArchivoCF = "ASSGssp_CF_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CF").toString() + fileNameArchivoCF.substring(fileNameArchivoCF.lastIndexOf('.'), fileNameArchivoCF.length());
                        fileNameArchivoCF = fileNameArchivoCF.toUpperCase();
                    } else {
                        //Setea el nombre anterior del archivo
                        fileNameArchivoCF = nomArchivoCFAnterior;
                        fileNameArchivoCF = fileNameArchivoCF.toUpperCase();
                    }

                    //Si no tiene carta fianza
                    if (registro.getCartaFianzaId() == null) {
                        cartaFianza = new SspCartaFianza();
                        cartaFianza.setId(null);
                        cartaFianza.setNumero(regNroCartaFianza.trim());
                        cartaFianza.setTipoFinancieraId(regTipoFinancieraCFSelected);
                        cartaFianza.setFinancieraId(regEntidadFinancieraCFSelected);
                        cartaFianza.setMoneda(regTipoMonedaCFSelected.getId());
                        cartaFianza.setMonto(regMontoCartaFianza);
                        cartaFianza.setVigenciaInicio(regFechVigenciaIni);
                        cartaFianza.setVigenciaFin(regFechVigenciaFin);
                        cartaFianza.setNombreArchivo(fileNameArchivoCF);
                        cartaFianza.setFecha(new Date());
                        cartaFianza.setActivo(JsfUtil.TRUE);
                        cartaFianza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        cartaFianza.setAudNumIp(JsfUtil.getIpAddress());
                        cartaFianza = (SspCartaFianza) JsfUtil.entidadMayusculas(cartaFianza, "");
                        ejbSspCartaFianzaFacade.create(cartaFianza);
                    } else //Si los datos estan deshabilitados
                    if (regDisabledDatosCartaFianza) {
                        //Si los datos de la carta fianza estan deshabilitados debe haber seleccionado de la lista y se realiza la relación
                        cartaFianza = ejbSspCartaFianzaFacade.find(Long.parseLong(regBuscaCartaFianzaSelectedString));
                        registro.setCartaFianzaId(cartaFianza);
                    } else //Si los datos no estan deshabilitados
                    //Puede que este actualizandose la misma carta fianza  
                    //Si ha seleccionado de la lista
                    if (regBuscaCartaFianzaSelectedString != null) {
                        //Si es la misma Carta Fianza la puede editar los datos
                        if (Long.parseLong(regBuscaCartaFianzaSelectedString) == registro.getCartaFianzaId().getId()) {
                            cartaFianza = registro.getCartaFianzaId();
                            cartaFianza.setNumero(regNroCartaFianza.trim());
                            cartaFianza.setTipoFinancieraId(regTipoFinancieraCFSelected);
                            cartaFianza.setFinancieraId(regEntidadFinancieraCFSelected);
                            cartaFianza.setMoneda(regTipoMonedaCFSelected.getId());
                            cartaFianza.setMonto(regMontoCartaFianza);
                            cartaFianza.setVigenciaInicio(regFechVigenciaIni);
                            cartaFianza.setVigenciaFin(regFechVigenciaFin);
                            cartaFianza.setNombreArchivo(fileNameArchivoCF);
                            cartaFianza.setFecha(new Date());
                            cartaFianza.setActivo(JsfUtil.TRUE);
                            cartaFianza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            cartaFianza.setAudNumIp(JsfUtil.getIpAddress());
                            cartaFianza = (SspCartaFianza) JsfUtil.entidadMayusculas(cartaFianza, "");
                            ejbSspCartaFianzaFacade.edit(cartaFianza);
                        } else {
                            //Si no es la misma la asocia con la Carta Fianza de la lista, no crea una nueva carta
                            cartaFianza = ejbSspCartaFianzaFacade.find(Long.parseLong(regBuscaCartaFianzaSelectedString));
                            registro.setCartaFianzaId(cartaFianza);
                        }
                    } else {
                        //No ha seleccionado de la lista nada y esta editando los datos
                        //Caso cuando le da clic en nuevo
                        //Busca la anterior Carta Fianza grabada para ver si esta en otro registro para crear o editar
                        SspCartaFianza encuentraCarta = ejbSspCartaFianzaFacade.buscarCartaFianzaPorNumero(regAdminist.getId(), registro.getCartaFianzaId().getNumero().toUpperCase().trim(), registro.getId());
                        if (encuentraCarta != null) {
                            //Crea una nueva carta fianza si ve que la carta se encuentra en otros registros 
                            cartaFianza = new SspCartaFianza();
                            cartaFianza.setId(null);
                            cartaFianza.setNumero(regNroCartaFianza.trim());
                            cartaFianza.setTipoFinancieraId(regTipoFinancieraCFSelected);
                            cartaFianza.setFinancieraId(regEntidadFinancieraCFSelected);
                            cartaFianza.setMoneda(regTipoMonedaCFSelected.getId());
                            cartaFianza.setMonto(regMontoCartaFianza);
                            cartaFianza.setVigenciaInicio(regFechVigenciaIni);
                            cartaFianza.setVigenciaFin(regFechVigenciaFin);
                            cartaFianza.setNombreArchivo(fileNameArchivoCF);
                            cartaFianza.setFecha(new Date());
                            cartaFianza.setActivo(JsfUtil.TRUE);
                            cartaFianza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            cartaFianza.setAudNumIp(JsfUtil.getIpAddress());
                            cartaFianza = (SspCartaFianza) JsfUtil.entidadMayusculas(cartaFianza, "");
                            ejbSspCartaFianzaFacade.create(cartaFianza);
                        } else {
                            //Edita los datos ya que esa carta no se encuentra en otro registro
                            //No ha seleccionado de la lista nada y esta editando los datos
                            cartaFianza = registro.getCartaFianzaId();
                            cartaFianza.setNumero(regNroCartaFianza.trim());
                            cartaFianza.setTipoFinancieraId(regTipoFinancieraCFSelected);
                            cartaFianza.setFinancieraId(regEntidadFinancieraCFSelected);
                            cartaFianza.setMoneda(regTipoMonedaCFSelected.getId());
                            cartaFianza.setMonto(regMontoCartaFianza);
                            cartaFianza.setVigenciaInicio(regFechVigenciaIni);
                            cartaFianza.setVigenciaFin(regFechVigenciaFin);
                            cartaFianza.setNombreArchivo(fileNameArchivoCF);
                            cartaFianza.setFecha(new Date());
                            cartaFianza.setActivo(JsfUtil.TRUE);
                            cartaFianza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            cartaFianza.setAudNumIp(JsfUtil.getIpAddress());
                            cartaFianza = (SspCartaFianza) JsfUtil.entidadMayusculas(cartaFianza, "");
                            ejbSspCartaFianzaFacade.edit(cartaFianza);
                        }
                    }

                    //Si ha seleccionado otro archivo de carta fianza lo convierte en byte
                    if (cfByte != null) {
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CF").getValor() + cartaFianza.getNombreArchivo()), cfByte);
                    }
                    registro.setCartaFianzaId(cartaFianza);
                    /* FIN DE GUARDA LA CARTA FIANZA */
                }
                //Fin Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 

                /*if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_VP") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")){
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
                if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                    registro.setRegistroId(resolucionPrincipal.getRegistroId());
                }

                //El estado CREADO al inicio no cambia, cuando modifica o corrige las observaciones se tiene que ver
                //registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                //registro.setComprobanteId(regDatoRecibos);//Datos del Comprobante Pagado
                //El comprobante de pago todavia no esta habilitado
                //registro.setComprobanteId(null);
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {
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
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                    //Busca el Representante Registro y actualiza
                    representanteRegistro = ejbSspRepresentanteRegistroFacade.representRegFindByRegistroId(registro.getId(), registro.getEmpresaId().getId());
                    if (registro.getRepresentanteId() == null) {
                        //Si no existe crea el registro
                        representanteRegistro.setId(null);
                        representanteRegistro.setRegistroId(registro);
                        representanteRegistro.setRepresentanteId(regPersonasRLSelected);                        
                        
                        if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                            //Graba el Representante Legal
                            representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));
                        } else if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                            //Graba el Responsable Legal
                            representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RESLEG"));
                        }
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
                        if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                            //Graba el Representante Legal
                            representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));
                        } else if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                            //Graba el Responsable Legal
                            representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RESLEG"));
                        }
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
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

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
                    
                    //BROWN2023
                    representanteRegistro.setDireccionRLId(regSbDireccionRL);
                    representanteRegistro.setAsientoRLId(asientoPersonaRL);
                    ejbSspRepresentanteRegistroFacade.edit(representanteRegistro);

                    /*Panel de Local de Prestación de Servicio*/
                    //Si es un nuevo local se registra
                    if ((Long.parseLong(localAutorizacionSelectedString)) < 0) {

                        for (LocalAutorizacionDetalle xItemLocal : localAutorizacionListado) {
                            if (localAutorizacionSelectedString.equals(xItemLocal.getId().toString())) {
                                localAutorizacion = new SspLocalAutorizacion();
                                localAutorizacion.setId(null);
                                localAutorizacion.setTipoUbicacionId(xItemLocal.getTipoUbicacionId());
                                localAutorizacion.setDireccion(xItemLocal.getDireccion());
                                localAutorizacion.setNroFisico(xItemLocal.getNroFisico());
                                localAutorizacion.setDistritoId(xItemLocal.getDistritoId());
                                localAutorizacion.setReferencia(xItemLocal.getReferencia());
                                localAutorizacion.setGeoLatitud(xItemLocal.getGeoLatitud());
                                localAutorizacion.setGeoLongitud(xItemLocal.getGeoLongitud());
                                localAutorizacion.setFecha(new Date());
                                localAutorizacion.setActivo(JsfUtil.TRUE);
                                localAutorizacion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                localAutorizacion.setAudNumIp(JsfUtil.getIpAddress());
                                localAutorizacion = (SspLocalAutorizacion) JsfUtil.entidadMayusculas(localAutorizacion, "");
                                ejbSspLocalAutorizacionFacade.create(localAutorizacion);
                            }
                        }
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
                        tipoUsoLocal.setTipoLocalId(regTipLocalLPSSelected);
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
                        tipoUsoLocal.setTipoLocalId(regTipLocalLPSSelected);
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
                         
                /* GUARDA DATOS DE MEDIOS DE CONTACTO - FORM EDITAR*/
                if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SISPE") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")){   
                    
                    if (lstContactos.size() > 0) {
                        for (SspContacto itemContacto : lstContactos) {
                            //Si es nuevo contacto lo crea
                            if (itemContacto.getId() == null ) {
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
//                                itemContacto.setActivo(JsfUtil.TRUE);
                                itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                                itemContacto = (SspContacto) JsfUtil.entidadMayusculas(itemContacto, "");
                                ejbSspContactoFacade.edit(itemContacto);
                            }
                        }
                    } else {
                        throw new NullPointerException("Lista de Contactos vacía");
//                        JsfUtil.mensajeError("Se necesita registar un medio de contacto.");
                    }
                    
                    
//                    //Inactiva los IDS que fueron eliminados
//                    if (listaUpdateContactosActivos.size() > 0) {
//                        for (SspContacto it : listaUpdateContactosActivos) {
//                            it.setActivo(JsfUtil.FALSE);
//                            ejbSspContactoFacade.edit(it);
//                        }
//                    }
                            
                }
                /* FIN DE GUARDA DATOS DE MEDIOS DE CONTACTO */

 /* GUARDA DATOS DE LICENCIA MUNICIPAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

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
                
                /* GUARDAR DATOS DE ACCIONISTAS -FORM EDITAR */
                if(regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")){   
                    for(SspParticipante itemSocio : lstSocios){
                        //Si es nuevo socio lo crea
//                        if (itemSocio.getId() < 0) {
                        if (itemSocio.getId() == null) {
                            if(itemSocio.getActivo() == JsfUtil.TRUE){
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
                        } else {
                            //Si es un socio existente edita los datos
                            itemSocio.setRegistroId(registro);
                            itemSocio.setTipoParticipacion(itemSocio.getTipoParticipacion());
                            itemSocio.setCargo((itemSocio.getCargo() != null) ? itemSocio.getCargo() : "");
                            itemSocio.setFecha(new Date());
//                            itemSocio.setActivo(JsfUtil.TRUE);
                            itemSocio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemSocio.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspParticipanteFacade.edit(itemSocio);
                        }
                    }

                    //Inactiva los IDS que fueron eliminados
//                    if (listaUpdateParticipanteActivos.size() > 0) {
//                        for (SspParticipante it : listaUpdateParticipanteActivos) {
//                            it.setActivo(JsfUtil.FALSE);
//                            ejbSspParticipanteFacade.edit(it);
//                        }
//                    }
//                        }
//                    }
                }
                /* FIN DE GUARDAR DATOS DE ACCIONISTAS -FORM EDITAR */
                
                //Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 
                if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                    /* GUARDA ARCHIVO DE CARTA FIANZA */
                    if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

                        if (cartaFianza.getNombreArchivo() != null) {

                            SspArchivo archCartaFianza = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_CF");
                            if (archCartaFianza != null) {
                                //Inactiva el Anterior Requisito del Archivo de Carta Fianza
                                SspRequisito requisitoAnteriorCartaFianza = new SspRequisito();
                                requisitoAnteriorCartaFianza = archCartaFianza.getRequisitoId();
                                requisitoAnteriorCartaFianza.setActivo(JsfUtil.FALSE);
                                requisitoAnteriorCartaFianza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                requisitoAnteriorCartaFianza.setAudNumIp(JsfUtil.getIpAddress());
                                requisitoAnteriorCartaFianza.setFecha(new Date());
                                requisitoAnteriorCartaFianza = (SspRequisito) JsfUtil.entidadMayusculas(requisitoAnteriorCartaFianza, "");
                                ejbSspRequisitoFacade.edit(requisitoAnteriorCartaFianza);

                                //Inactiva el Anterior Archivo de Carta Fianza
                                SspArchivo archivoAnteriorCartaFianza = new SspArchivo();
                                archivoAnteriorCartaFianza = archCartaFianza;
                                archivoAnteriorCartaFianza.setActivo(JsfUtil.FALSE);
                                archivoAnteriorCartaFianza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                archivoAnteriorCartaFianza.setAudNumIp(JsfUtil.getIpAddress());
                                archivoAnteriorCartaFianza = (SspArchivo) JsfUtil.entidadMayusculas(archivoAnteriorCartaFianza, "");
                                ejbSspArchivoFacade.edit(archivoAnteriorCartaFianza);
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
                            requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CF"));
                            requisito.setValorRequisito(cartaFianza.getNumero());
                            requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");
                            ejbSspRequisitoFacade.create(requisito);

                            SspArchivo archivo = new SspArchivo();
                            archivo.setActivo(JsfUtil.TRUE);
                            archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            archivo.setAudNumIp(JsfUtil.getIpAddress());
                            archivo.setId(null);
                            archivo.setPathupload("Documentos_pathUpload_ASSGssp_CF");
                            archivo.setNombre(cartaFianza.getNombreArchivo());
                            archivo.setRequisitoId(requisito);
                            archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                            archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                            ejbSspArchivoFacade.create(archivo);
                        }

                    }
                    /* FIN DE GUARDA ARCHIVO DE CARTA FIANZA */
                }
                //Fin de Solo Guarda la Carta Fianza el Tipo de Local es PRINCIPAL 

                /* GUARDA ARCHIVO DE LICENCIA MUNICIPAL */
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

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
                if (regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_TDV") || regTipoProcesoId.getCodProg().equals("TP_GSSP_MOD_SCBC")) {

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
                
                String fileNameArchivoPOL = "";
                
                if(nomArchivoPOL!=null){
                        fileNameArchivoPOL = nomArchivoPOL;
                        fileNameArchivoPOL = "ASSGssp_POLIZA_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_POLIZA").toString() + fileNameArchivoPOL.substring(fileNameArchivoPOL.lastIndexOf('.'), fileNameArchivoPOL.length());
                        fileNameArchivoPOL = fileNameArchivoPOL.toUpperCase();
                }else{
                        fileNameArchivoPOL = nomArchivoPOLAnterior;
                        fileNameArchivoPOL = fileNameArchivoPOL.toUpperCase();
                }
                
                SspPoliza poliza_edita=new SspPoliza();
                poliza_edita=registro.getSspPolizaList().get(0);
                
                        poliza_edita.setId(poliza_edita.getId());
                        poliza_edita.setNroPoliza(nroPoliza);
                        poliza_edita.setEmpresaAseguradora(empresaAseguradora);
                        poliza_edita.setVigenciaIni(fecIniPoliza);
                        poliza_edita.setVigenciaFin(fecFinPoliza);
                        poliza_edita.setMontoMaximo(valorMontoMaximo);
                        poliza_edita.setRegistroId(registro);
                        poliza_edita.setTipoMoneda(tipoMoneda);
                        poliza_edita.setBienes(bienesAcustodiar);
                        poliza_edita.setArchivoPoliza(fileNameArchivoPOL);
                        poliza_edita.setActivo(JsfUtil.TRUE);
                        poliza_edita.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        poliza_edita.setAudNumIp(JsfUtil.getIpAddress());
                        poliza_edita = (SspPoliza) JsfUtil.entidadMayusculas(poliza_edita, "");
                        ejbSspPolizaFacade.edit(poliza_edita);
                        
                        if (polByte != null) {
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_POLIZA").getValor() + fileNameArchivoPOL), polByte);
                        }
                        partidaSunarpVEH=new SbPartidaSunarp();
                        partidaSunarpVEH.setId(registro.getSspVehiculoList().get(0).getInformacionRegistral().getPartidaId().getId());
                        partidaSunarpVEH.setPartidaRegistral(partidaRegVeh);
                        partidaSunarpVEH.setZonaRegistral(regZonaRegSelectedVEH);
                        partidaSunarpVEH.setOficinaRegistral(regOficinaRegSelectedVEH);
                        partidaSunarpVEH.setFecha(new Date());
                        partidaSunarpVEH.setActivo(JsfUtil.TRUE);
                        partidaSunarpVEH.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        partidaSunarpVEH.setAudNumIp(JsfUtil.getIpAddress());
                        partidaSunarpVEH = (SbPartidaSunarp) JsfUtil.entidadMayusculas(partidaSunarpVEH, "");
                        ejbSbPartidaSunarpFacade.edit(partidaSunarpVEH);
                        
                        asientoSunarpVEH=new SbAsientoSunarp();
                        asientoSunarpVEH.setId(registro.getSspVehiculoList().get(0).getInformacionRegistral().getId());
                        asientoSunarpVEH.setPartidaId(partidaSunarpVEH);
                        asientoSunarpVEH.setNroAsiento(asientoRegVeh);
                        asientoSunarpVEH.setFecha(new Date());
                        asientoSunarpVEH.setActivo(JsfUtil.TRUE);
                        asientoSunarpVEH.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        asientoSunarpVEH.setAudNumIp(JsfUtil.getIpAddress());
                        asientoSunarpVEH = (SbAsientoSunarp) JsfUtil.entidadMayusculas(asientoSunarpVEH, "");
                        ejbSbAsientoSunarpFacade.edit(asientoSunarpVEH);
                        
                String fileNameArchivoPAR_REG_VEH = "";
                String fileNameArchivo_CAV = "";
                
                if(nomArchivoPartidaRegVeh!=null){
                        fileNameArchivoPAR_REG_VEH = nomArchivoPartidaRegVeh;
                        fileNameArchivoPAR_REG_VEH = "ASSGssp_PR_VEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_PR_VEH").toString() + fileNameArchivoPAR_REG_VEH.substring(fileNameArchivoPAR_REG_VEH.lastIndexOf('.'), fileNameArchivoPAR_REG_VEH.length());
                        fileNameArchivoPAR_REG_VEH = fileNameArchivoPAR_REG_VEH.toUpperCase();
                }else{
                    fileNameArchivoPAR_REG_VEH=nomArchivoPartidaRegVehAnterior;
                    fileNameArchivoPAR_REG_VEH=fileNameArchivoPAR_REG_VEH.toUpperCase();
                }
                
                if(checkTipoPropiedad==2){
                    if(nomArchivoCAV!=null) {
                        fileNameArchivo_CAV = nomArchivoCAV;
                        fileNameArchivo_CAV = "ASSGssp_CAV_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CAV").toString() + fileNameArchivo_CAV.substring(fileNameArchivo_CAV.lastIndexOf('.'), fileNameArchivo_CAV.length());
                        fileNameArchivo_CAV = fileNameArchivo_CAV.toUpperCase();
                    }else{
                        fileNameArchivo_CAV=nomArchivoCAVAnterior;
                    } 
                }
                        vehiculo=new SspVehiculoDinero();
                        vehiculo.setId(registro.getSspVehiculoList().get(0).getId());
                        vehiculo.setCarroceria(carroseria);
                        vehiculo.setCategoria(categoria);
                        vehiculo.setPlaca(placa);
                        vehiculo.setRegistroId(registro);
                        vehiculo.setActivo(JsfUtil.TRUE);
                        vehiculo.setAudNumIp(JsfUtil.getIpAddress());
                        vehiculo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        vehiculo.setArrendado((short) checkTipoPropiedad);
                        vehiculo.setInformacionRegistral(asientoSunarpVEH);
                        //vehiculo.setDireccion(ubicacionVeh);
                        //vehiculo.setDistrito(regDistritoVEHSelected);
                        vehiculo.setArchivoRegistral(fileNameArchivoPAR_REG_VEH);
                        vehiculo.setArchivoArrendamiento(fileNameArchivo_CAV);
                        vehiculo = (SspVehiculoDinero) JsfUtil.entidadMayusculas(vehiculo, "");
                        ejbSspVehiculoDineroFacade.edit(vehiculo);
                        
                if(partidaVehByte!=null){
                    
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_PRVEH").getValor() + vehiculo.getArchivoRegistral()), partidaVehByte);
                }
                
                if(cavByte!=null){
                    
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CAV").getValor() + vehiculo.getArchivoArrendamiento()), cavByte);
                }
                
                JsfUtil.mensaje("Se actualizó la solicitud con Código: " + registro.getId() + " y con Nro. de Solicitud " + registro.getNroSolicitiud());
                return prepareList();

            }
        } catch (Exception e) {
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
//            e.printStackTrace();
            System.out.println("guardarEditarSolicitud() " + e.getLocalizedMessage());
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        }

        return null;
    }
    
    public void mostrarEditarSolicitud(Map item) {
        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_TDV")) {
            
            tipoRegistroCodProg = registro.getTipoRegId().getCodProg();
            System.out.println("tipoRegistroCodProg->" + tipoRegistroCodProg);
            iniciarValores_CrearSolicitud();
            estado = EstadoCrud.EDITARTDV;
            cargarDatosEditarSolicitud(registro);
            
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                System.out.println("entro");
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/transporte_cdv/Edit.xhtml");
            } catch (IOException ex) {
                System.out.println("no");
                ex.printStackTrace();
            }
            
        }   else {
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
                if (ce.getActivo() == JsfUtil.TRUE && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")) {
                    lstObservaciones.add(ce);
                }
            }
            //Fin de Busca las Observaciones

            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            regAdminist = registro.getEmpresaId();
            regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());

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
            regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_TDV'");
            regTipoProcesoId = regTipoSeguridadSelected;
            regTipoSeguridadSelected = registro.getTipoProId();

            //Tipo de Registro
            regTipoRegistroList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_REGIST");
            regTipoRegistroSelected = registro.getTipoRegId();

            //Tipo de Operación
            regTipoOperacionList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_OPE");
            regTipoOperacionSelected = registro.getTipoOpeId();

            //Servicio Prestado CON ARMA, SIN ARMA
            //regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP");// 
            regServicioPrestadoList = ejbTipoSeguridadFacade.listarTipoSeguridadXCodProgs("'SI_ARMA_SSP'");
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

            //Si es por Adecuación de 1213 con Resolución del DISCA
            if (tipoRegistroCodProg.equals("TP_REGIST_ADECU")) {

                if (registro.getSspRegistroRegDiscaList().size() > 0) {
                    seleccionaResol_Disca_Vigilancia_Privada_Editar(registro.getSspRegistroRegDiscaList().get(0).getIdResolucionDISCA().toString());

                } else {
                    registro_id_vistaDISCA = null;
                    renderMostrarFormVigilanciaPrivada = false;
                    emptyModel_vistaDISCA = null;
                    registro_resol = null;
                }
            }

            if (registro.getSspLocalRegistroList() != null) {

                regTipLocalLPSSelected = registro.getSspTipoUsoLocalList().get(0).getTipoLocalId();
                listarDepartamentosDistritoTipoLocal();
                regDistritoLPSSelected = registro.getSspLocalRegistroList().get(0).getLocalId().getDistritoId();
                if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_PRIN")) {
                    habilitarCartaFianza = true;
                } else if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                    habilitarCartaFianza = false;
                }
                //Detalle de Locales del distrito Seleccionado
                List<SspLocalAutorizacion> listDetalleLocal = new ArrayList<>();
                //listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocal(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId());
                listDetalleLocal = ejbSspLocalAutorizacionFacade.listarLocalAutorizacionDistritoEmpresaTipoLocalValida(regAdminist.getId(), regDistritoLPSSelected.getId(), regTipLocalLPSSelected.getId(), regTipoSeguridadSelected.getId(), registro.getId());
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
                if (registro.getRegistroId() != null) {
                    //Si el Tipo de Local es SUCURSAL
                    if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                        SspResolucion resolucionBuscaPrincipal = ejbSspResolucionFacade.buscarResolucionPorRegistro(registro.getRegistroId().getId());
                        if (resolucionBuscaPrincipal != null) {
                            resolucionPrincipal = resolucionBuscaPrincipal;

                            //Buscamos los datos del Representante legal Ver - PRINCIPAL
                            representanteLegalVer = resolucionBuscaPrincipal.getRegistroId().getRepresentanteId();

                            SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
                            Long xEmpresaId = regAdminist.getId();
                            xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(representanteLegalVer.getId(), xEmpresaId);

                            if (xSbAsientoPersonaRL != null) {
                                //existeAsientoSunarpRL = true;
                                regRLPartidaRegistralVer = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getPartidaRegistral();
                                regRLAsientoRegistralVer = xSbAsientoPersonaRL.getAsientoId().getNroAsiento();
                                if (xSbAsientoPersonaRL.getAsientoId().getPartidaId() != null) {
                                    regZonaRegSelectedRLVer = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getZonaRegistral().getId());
                                    //Filtra las Oficinas Registrales
                                    regOficinaRegListRLVer = ejbTipoBaseFacade.listarTipoBaseXCodProg(regZonaRegSelectedRLVer.getCodProg());
                                    regOficinaRegSelectedRLVer = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getOficinaRegistral().getId());
                                } else {
                                    regZonaRegSelectedRLVer = null;
                                    regOficinaRegSelectedRLVer = null;
                                    regOficinaRegListRLVer = null;
                                }
                            } else {
                                //existeAsientoSunarpRL = false;
                                regRLPartidaRegistralVer = null;
                                regRLAsientoRegistralVer = null;
                                regZonaRegSelectedRLVer = null;
                                regOficinaRegSelectedRLVer = null;
                                regOficinaRegListRLVer = null;
                            }

                            List<SbDireccionGt> listDireccionPersona = ejbSbDireccionFacade.listarDireccionesXPersona(representanteLegalVer.getId());
                            if (listDireccionPersona.size() > 0) {
                                regTipoViasSelectedVer = listDireccionPersona.get(0).getViaId();
                                regDomicilioRLSelectedVer = listDireccionPersona.get(0).getDireccion();
                                regDistritoRLSelectedVer = listDireccionPersona.get(0).getDistritoId();
                            } else {
                                regTipoViasSelectedVer = null;
                                regDomicilioRLSelectedVer = null;
                                regDistritoRLSelectedVer = null;
                            }
                            //Fin de Buscamos los datos del Representante legal Ver - PRINCIPAL

                        }
                    }
                }

            } else {
                localAutorizacionSelectedString = null;
                regDistritoLPSSelected = null;
                localAutorizacionSelectedString = null;
                localAutorizacionAnterior = null;
                regTipoViasLPSSelected = null;
                regDireccionLPS = null;
                regNroFisicoLPS = null;
                regTipLocalLPSSelected = null;
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
            if (regTipLocalLPSSelected.getCodProg().equals("TP_LOC_SECU")) {
                sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RESLEG"); //RESPONSABLE LEGAL
            } else {
                //Caso contrario es Representante Legal
                sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL
            }

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

            //    
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
            
            nroPoliza=registro.getSspPolizaList().get(0).getNroPoliza();
            fecIniPoliza=registro.getSspPolizaList().get(0).getVigenciaIni();
            fecFinPoliza=registro.getSspPolizaList().get(0).getVigenciaFin();
            empresaAseguradora=registro.getSspPolizaList().get(0).getEmpresaAseguradora();
            valorMontoMaximo=registro.getSspPolizaList().get(0).getMontoMaximo();
            tipoMoneda=registro.getSspPolizaList().get(0).getTipoMoneda();
            bienesAcustodiar=registro.getSspPolizaList().get(0).getBienes();
            nomArchivoPOL=null;
            nomArchivoPOLAnterior=registro.getSspPolizaList().get(0).getArchivoPoliza();
            
            categoria=registro.getSspVehiculoList().get(0).getCategoria();
            carroseria=registro.getSspVehiculoList().get(0).getCarroceria();
            placa=registro.getSspVehiculoList().get(0).getPlaca();
            //ubicacionVeh=registro.getSspVehiculoList().get(0).getDireccion();
            //regDistritoVEHSelected=registro.getSspVehiculoList().get(0).getDistrito();
            partidaRegVeh=registro.getSspVehiculoList().get(0).getInformacionRegistral().getPartidaId().getPartidaRegistral();
            asientoRegVeh=registro.getSspVehiculoList().get(0).getInformacionRegistral().getNroAsiento();
            regZonaRegSelectedVEH=registro.getSspVehiculoList().get(0).getInformacionRegistral().getPartidaId().getZonaRegistral();
            listarOficinasRegistrales_VEH();
            regOficinaRegSelectedVEH=registro.getSspVehiculoList().get(0).getInformacionRegistral().getPartidaId().getOficinaRegistral();
            checkTipoPropiedad=registro.getSspVehiculoList().get(0).getArrendado();
            nomArchivoPartidaRegVeh=null;
            nomArchivoPartidaRegVehAnterior=registro.getSspVehiculoList().get(0).getArchivoRegistral();
            nomArchivoCAV=null;
            if(checkTipoPropiedad==2){esArrendado=true; nomArchivoCAVAnterior=registro.getSspVehiculoList().get(0).getArchivoArrendamiento();}

        } else {
            JsfUtil.mensajeError("No se encontró datos de la solicitud");
        }
    }
    
    public void seleccionaResol_Disca_Vigilancia_Privada_Editar(String nro_resol) {
        registro_id_vistaDISCA = null;
        renderMostrarFormVigilanciaPrivada = false;
        emptyModel_vistaDISCA = null;
        registro_resol = null;
        String servicio_prestado_resol = null;

        if (nro_resol != "" && administRUC != null) {
            if (estado.equals(EstadoCrud.VERVIGILANCIAPRIVADA)) {
                //Selecciona la Vista del DISCA que recupera hasta las Resoluciones no VIGENTES
                emptyModel_vistaDISCA = ejbSspResolucionFacade.buscarAutorizacion_vistaDISCA_VigilanciaPrivada(nro_resol, "2");
            } else {
                //Selecciona la Vista del DISCA que recupera las Resoluciones VIGENTES
                emptyModel_vistaDISCA = ejbSspResolucionFacade.buscarAutorizacion_vistaDISCA_VigilanciaPrivada(nro_resol, "1");
            }

            renderLocalDISCA = false;
            if (emptyModel_vistaDISCA != null && emptyModel_vistaDISCA.size() > 0) {
                //setear el local de la resolucion 
                if (!emptyModel_vistaDISCA.get("PRINCIPAL").equals("PRINCIPAL")) {
                    JsfUtil.mensajeAdvertencia("La Resolución es un Local " + emptyModel_vistaDISCA.get("PRINCIPAL") + ". Debe ser Local PRINCIPAL no se podrá realizar el registro");
                    renderMostrarFormVigilanciaPrivada = false;
                    return;
                }
                registro_id_vistaDISCA = emptyModel_vistaDISCA.get("ID").toString();

                renderMostrarFormVigilanciaPrivada = true;
                renderLocalDISCA = (emptyModel_vistaDISCA.get("REGISTRO_ID") != "") ? false : true;// si es true es DISCA

                if (renderLocalDISCA) {
                    registro_regDisca.setModEmp(Short.parseShort(emptyModel_vistaDISCA.get("MOD_EMP_DISCA").toString()));
                    registro_regDisca.setNroRd(Integer.parseInt(emptyModel_vistaDISCA.get("NRO_RD_DISCA").toString()));
                    registro_regDisca.setAnoRd(Short.parseShort(emptyModel_vistaDISCA.get("ANO_RD_DISCA").toString()));
                }

                //regTipoSeguridadList        = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_AUTORIZ_SEL_CLO'");
                //regTipoSeguridadSelected    = regTipoSeguridadList.get(0);
                //regTipoProcesoId            = regTipoSeguridadSelected;

                /*       
                      
                    ||||||||||---- HOMOLOGAR----DISCA==>RENAGI ----||||||||||||
                      
                    20	AUTORIZACION PARA LA PRESTACION DE TECNOLOGIA DE SEGURIDAD
                    13	PRESTACION DE SERVICIOS DE TECNOLOGIA DE SEGURIDAD - GPS
                    16	PRESTACION DE SERVICIOS DE TECNOLOGIA DE SEGURIDAD - INCENDIOS
                    19	PRESTACION DE SERVICIOS DE TECNOLOGIA DE SEGURIDAD - SEGURIDAD
                    15	PRESTACION DE SERVICIOS DE TECNOLOGIA DE SEGURIDAD - VIDEO A DISTANCIA
                    12	PRESTACION DE SERVICIOS TECNOLOGIA DE SEGURIDAD-CENTRALES RECEPTORAS
                    21	RENOVACION DE AUTORIZACION PARA LA PRESTACION DE TECNOLOGIA DE SEGURIDAD
                      -----------> 2704         SERVICIO DE TECNOLOGÍA DE SEGURIDAD                         TP_GSSP_MOD_TEC

                    8	PRESTACION DE SERVICIO DE CONSULTORIA Y ASESORIA EN TEMAS DE SSP
                      ----------->  2701	SERVICIO DE CONSULTORIA Y ASESORIA EN TEMAS DE SEG.	    TP_GSSP_MOD_CYA
                    5	PRESTACION DE SERVICIO DE PROTECCION PERSONAL
                      ----------->  2698	SERVICIO DE PROTECCIÓN PERSONAL                             TP_GSSP_MOD_PP
                    1	PRESTACION DE SERVICIOS DE VIGILANCIA PRIVADA
                      ----------->  2697	SERVICIO DE VIGILANCIA PRIVADA                              TP_GSSP_MOD_VP
                    3	PRESTACION DEL SERVICIO DE TRANSPORTE DE DINERO Y VALORES
                      ----------->  2699	SERVICIO DE TRANSPORTE Y CUSTODIA DE DINERO Y VALORES       TP_GSSP_MOD_TDV
                    4	SERVICIO INDIVIDUAL DE CONSULTORIA Y ASESORIA EN TEMAS DE SEGURIDAD PRIVADA
                      -----------> 3525         SERVICIO INDIVIDUAL DE CONSULT. Y ASE. EN TEMAS DE SEG.	    TP_GSSP_MOD_ICYA
                    2	SERVICIOS DE PROTECCION POR CUENTA PROPIA
                      -----------> 2700         SERVICIO DE PROTECCIÓN POR CUENTA PROPIA                    TP_GSSP_MOD_PCP
                       
                 */
                boolean sin_arma = false;
                if (emptyModel_vistaDISCA.get("TIP_MOD") != "") {
                    String cod_mod = emptyModel_vistaDISCA.get("TIP_MOD").toString();
                    switch (cod_mod) {
                        case "20":
                        case "13":
                        case "16":
                        case "19":
                        case "15":
                        case "12":
                        case "21":
                            //TP_GSSP_MOD_TEC
                            mod_vinculadaSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_TEC");
                            sin_arma = true;
                            break;
                        case "8":
                            //TP_GSSP_MOD_CYA
                            mod_vinculadaSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_CYA");
                            break;
                        case "5":
                            //TP_GSSP_MOD_PP
                            mod_vinculadaSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_PP");
                            break;
                        case "1":
                            //TP_GSSP_MOD_VP
                            mod_vinculadaSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_VP");
                            break;
                        case "3":
                            //TP_GSSP_MOD_TDV
                            mod_vinculadaSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_TDV");
                            break;
                        case "4":
                            //TP_GSSP_MOD_ICYA
                            mod_vinculadaSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_ICYA");
                            break;
                        case "2":
                            //TP_GSSP_MOD_PCP
                            mod_vinculadaSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_MOD_PCP");
                            sin_arma = true;
                            break;
                        default:
                            mod_vinculadaSelected = null;
                    }

                }

                emptyModel_vistaDISCA.put("TIPO_REG_RESOL", "INICIAL");
                emptyModel_vistaDISCA.put("TIPO_OPE_RESOL", "INICIAL");

                //llenar servicio prestado con DISCA
//                     servicio_prestado_resol = ( emptyModel_vistaDISCA.get("COD_ARM").equals("0")) ? "CON ARMA" : "SIN ARMA"; //con arma sin arma
                if (sin_arma) {
                    regServicioPrestadoList = ejbTipoSeguridadFacade.listarTipoSeguridadXCodProgs("'NO_ARMA_SSP'");
                    regServicioPrestadoSelected = regServicioPrestadoList.get(0);
                    servicio_prestado_resol = regServicioPrestadoSelected.getNombre();
                } else if (emptyModel_vistaDISCA.get("COD_ARM").equals("0")) {
                    regServicioPrestadoList = ejbTipoSeguridadFacade.listarTipoSeguridadXCodProgs("'SI_ARMA_SSP'");
                    regServicioPrestadoSelected = regServicioPrestadoList.get(0);
                    servicio_prestado_resol = regServicioPrestadoSelected.getNombre();
                } else {
                    regServicioPrestadoList = ejbTipoSeguridadFacade.listarTipoSeguridadXCodProgs("'NO_ARMA_SSP'");
                    regServicioPrestadoSelected = regServicioPrestadoList.get(0);
                    servicio_prestado_resol = regServicioPrestadoSelected.getNombre();
                }

                emptyModel_vistaDISCA.put("SERV_PRE_RESOL", servicio_prestado_resol);

                //setear el local de la resolucion 
                if (emptyModel_vistaDISCA.get("PRINCIPAL").equals("PRINCIPAL")) {
                    regTipLocalLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN'");
                    regTipLocalLPSSelected = regTipLocalLPSList.get(0);
                    //llenar el local del modal
                    regTipLocalLPSList_Form = regTipLocalLPSList;//se llena al buscar una autorizacion
                    regTipLocalLPSSelected_Form = regTipLocalLPSSelected;
                } else {
                    regTipLocalLPSList = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_SECU'");
                    regTipLocalLPSSelected = regTipLocalLPSList.get(0);
                    //llenar el local del modal
                    regTipLocalLPSList_Form = regTipLocalLPSList;//se llena al buscar una autorizacion
                    regTipLocalLPSSelected_Form = regTipLocalLPSSelected;
                }

                //Setea Los Departamentos conforme al Tipo Local PRINCIPAL por ahora no hay SUCURSAL 
                listarDepartamentosDistritoTipoLocal();
            }

        }

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
    
    public StreamedContent obtenerArchivoPoliza() {
        
        StreamedContent r = null;
        try {
            String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_POLIZA").getValor();
            FileInputStream f = new FileInputStream(path + registro.getSspPolizaList().get(0).getArchivoPoliza());
            r = new DefaultStreamedContent(f, "application/pdf", registro.getSspPolizaList().get(0).getArchivoPoliza());
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    public StreamedContent obtenerArchivoPartidaVEH() {
        
        StreamedContent r = null;
        try {
            String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_PRVEH").getValor();
            FileInputStream f = new FileInputStream(path + registro.getSspVehiculoList().get(0).getArchivoRegistral());
            r = new DefaultStreamedContent(f, "application/pdf", registro.getSspVehiculoList().get(0).getArchivoRegistral());
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    public StreamedContent obtenerArchivoCAV() {
        
        StreamedContent r = null;
        try {
            String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CAV").getValor();
            FileInputStream f = new FileInputStream(path + registro.getSspVehiculoList().get(0).getArchivoArrendamiento());
            r = new DefaultStreamedContent(f, "application/pdf", registro.getSspVehiculoList().get(0).getArchivoArrendamiento());
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    public void mostrarVerSolicitud(Map item) {
        String URL_Formulario = "";

        //Busca el Registro
        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        if (registro.getTipoProId().getCodProg().equals("TP_GSSP_MOD_TDV")) {

            tipoRegistroCodProg = registro.getTipoRegId().getCodProg();
            estado = EstadoCrud.VERTDV;
            cargarDatosEditarSolicitud(registro);
            //URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/vigilancia/VerSolicitudAutorizaSeg";
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                System.out.println("entro");
                ec.redirect("/sel/faces/aplicacion/gssp/gsspSolicitudAutorizacion/transporte_cdv/View.xhtml");
            } catch (IOException ex) {
                System.out.println("no");
                ex.printStackTrace();
            }
        } 
        
    }
    
    public void deleteContacto(SspContacto contacto) {
         try {
            for (SspContacto contact_x : lstContactos) {
               
                //cuando es un EDITAR
                if(contact_x.getId() !=null){
                    if(contact_x.equals(contacto)){ 
                        contact_x.setActivo(JsfUtil.FALSE);
                    }
                }else{
                    //cuando es un CREAR 
                    
                    if(contact_x.getValor().equals(contacto.getValor()) 
                            && contact_x.getTipoMedioId().equals(contacto.getTipoMedioId())
                            && contact_x.getDescripcion().equals(contacto.getDescripcion())  ){
                        contact_x.setActivo(JsfUtil.FALSE);
                    } 
                }
 
            } 
    
        } catch (Exception e) {
//            e.printStackTrace(); 
            System.out.println("deleteContacto() " + e.getLocalizedMessage());
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }  
          
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
}
