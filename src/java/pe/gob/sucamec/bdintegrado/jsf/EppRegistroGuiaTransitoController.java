package pe.gob.sucamec.bdintegrado.jsf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import pe.gob.sucamec.bdintegrado.data.EppRegistroGuiaTransito;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt;
import pe.gob.sucamec.bdintegrado.data.EppAlmacen;
import pe.gob.sucamec.bdintegrado.data.EppAlmacenAduanero;
import pe.gob.sucamec.bdintegrado.data.EppContratoAlqPolv;
import pe.gob.sucamec.bdintegrado.data.EppDetalleAutUso;
import pe.gob.sucamec.bdintegrado.data.EppDetalleCom;
import pe.gob.sucamec.bdintegrado.data.EppDetalleIntSal;
import pe.gob.sucamec.bdintegrado.data.EppDocumento;
import pe.gob.sucamec.bdintegrado.data.EppExplosivo;
import pe.gob.sucamec.bdintegrado.data.EppExplosivoSolicitado;
import pe.gob.sucamec.bdintegrado.data.EppGteDeposito;
import pe.gob.sucamec.bdintegrado.data.EppGteExplosivoSolicita;
import pe.gob.sucamec.bdintegrado.data.EppGteRegistro;
import pe.gob.sucamec.bdintegrado.data.EppGuiaTransitoPolicia;
import pe.gob.sucamec.bdintegrado.data.EppGuiaTransitoVehiculo;
import pe.gob.sucamec.bdintegrado.data.EppLicencia;
import pe.gob.sucamec.bdintegrado.data.EppLocal;
import pe.gob.sucamec.bdintegrado.data.EppLugarUso;
import pe.gob.sucamec.bdintegrado.data.EppLugarUsoUbigeo;
import pe.gob.sucamec.bdintegrado.data.EppPlantaExplosivo;
import pe.gob.sucamec.bdintegrado.data.EppPolvorin;
import pe.gob.sucamec.bdintegrado.data.EppPuertoAduanero;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.EppRegistroEvento;
import pe.gob.sucamec.bdintegrado.data.EppResolucion;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbDepartamentoGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbProcesoTupa;
import pe.gob.sucamec.bdintegrado.data.SbProvinciaGt;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import pe.gob.sucamec.bdintegrado.jsf.util.DocumentoTemp;
import pe.gob.sucamec.bdintegrado.jsf.util.OpcionesBandejaGte;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtilGuiaTransito;
import pe.gob.sucamec.bdintegrado.jsf.util.UploadFilesController;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

// Nombre de la instancia en la aplicacion //
@Named("eppRegistroGuiaTransitoController")
@SessionScoped

/**
 * Clase con EppRegistroGuiaTransitoController instanciada como
 * eppRegistroGuiaTransitoController. Contiene funciones utiles para la entidad
 * EppRegistroGuiaTransito. Esta vinculada a las páginas
 * EppRegistroGuiaTransito/create.xhtml, EppRegistroGuiaTransito/update.xhtml,
 * EppRegistroGuiaTransito/list.xhtml, EppRegistroGuiaTransito/view.xhtml Nota:
 * Las tablas deben tener la estructura de Sucamec para que funcione
 * adecuadamente, revisar si tiene el campo activo y modificar las búsquedas.
 */
public class EppRegistroGuiaTransitoController implements Serializable {

    @Inject
    WsTramDoc wsTramDocController;
    @Inject
    WsPide wsPideController;
    @Inject
    UploadFilesController uploadFilesController;

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppRegistroGuiaTransitoFacade eppRegistroGuiaTransitoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade sbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppLugarUsoFacade ejbLugarUsoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt distritoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbProvinciaFacadeGt provinciaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDepartamentoFacadeGt departamentoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppResolucionFacade ejbResolucionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppPolvorinFacade ejbPolvorinFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoExplosivoFacade ejbTipoExplosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppExplosivoFacade ejbExplosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppRegistroFacade ejbRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppDetalleAutUsoFacade ejbDetalleAutorizacionUsoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppDetalleComFacade ejbDetalleComFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppDocumentoFacade ejbDocumentoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppLicenciaFacade ejbLicenciaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbReciboRegistroFacade ejbSbReciboRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppGuiaTransitoPoliciaFacade eppGuiaTransitoPoliciaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppGuiaTransitoVehiculoFacade eppGuiaTransitoVehiculoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbFeriadoFacade sbFeriadoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppGteExplosivoSolicitaFacade eppGteExplosivoSolicitaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppGteRegistroFacade eppGteRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade sbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppLocalFacade ejbEppLocalFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppContratoAlqPolvFacade ejbEppContratoAlqPolvFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbProcesoTupaFacade ejbSbProcesoTupaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppContratoAlqPolvFacade ejbEppContratoAlquilerPolvorinFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppGteDepositoFacade ejbEppGteDepositoFacade;

    private EppRegistroGuiaTransito current;
    private EppGteRegistro currentBulk;
    private String ldField;
    private String ldFiltro;
    ListDataModel<EppRegistroGuiaTransito> resultados = null;
    private ListDataModel<EppGteRegistro> resultadosGteReg = null;
    List<EppRegistroGuiaTransito> registrosSeleccionados;
    private List<EppGteRegistro> registrosGteRegSeleccionados;
    ListDataModel<SbRecibos> lstRecibosBn = null;
    List<SbRecibos> lstRecibosBnUnq = null;
    private String tipoBusqueda = null;
    private String filtro;
    private String filtroDni;
    private String filtroRznSoc;
    private Date fechaIni;
    private String criterioResGuia;
    private String txtTipoTramite;
    private boolean deshabilitaRadioTipoRg = false;
    private TipoExplosivoGt tipoOrigen;
    private TipoExplosivoGt tipoDestino;
    private TipoExplosivoGt tipoTransporte;
    private TipoExplosivoGt tipodocumento;
    private EppRegistro resolucionGerencia;
    //BLOQUEA ORIGEN/DESTINO
    private boolean bloqueaSelectOrigen = false;
    private boolean bloqueaSelectDestino = false;
    private boolean bloqueaBotonBuscarOrigen = false;
    private boolean bloqueaBotonBuscarDestino = false;
    private String nombreEmpresaOrigen;
    private String nombreEmpresaDestino;
    private Integer cantidadAutorizada;
    private Double cantidadATransportar;
    //private EppRegistro documento = new EppRegistro();
    //private EppRegistroGuiaTransito registroGuiaTransito = new EppRegistroGuiaTransito();
    private List lstExplosivosCombo = new ArrayList();
    private List<TipoExplosivoGt> lstTipoGtOrigen = new ArrayList();
    private List<TipoExplosivoGt> lstTipoGtDestino = new ArrayList();
    private List<EppDocumento> listaDocumentos = new ArrayList();
    private boolean mostrarresglobprev = false;
    private SbPersonaGt empresa;
    private SbPersonaGt empresaTransporte;
    private EppExplosivo explosivoSeleccionado;
    private Date fechaCaducidadResolucionGerencia;
    private String numeroResolucionGerencia;
    private String tipoResolucionGerencia;
    private List lstlugardeuso = new ArrayList();
    private EppPolvorin polvorin;
    private EppLugarUso lugarUso;
    private EppLocal fabrica;
    private boolean mostrarLugarUsoUbigeoOrigen = false;
    private boolean mostrarLugarUsoUbigeoDestino = false;
    private EppLugarUsoUbigeo lugarUsoUbigeoOrigen;
    private EppLugarUsoUbigeo lugarUsoUbigeoDestino;
    private boolean disableLugarUsoUbigeoOrigen = false;
    private boolean disableLugarUsoUbigeoDestino = false;
    private EppDocumento docmtc;
    private Double saldoActualExplSolic;
    private Date fechaFactura;
    private boolean flgCustodio = true;
    private List<EppLicencia> lstEliminadosLicencia;
    private List<EppGteExplosivoSolicita> lstEliminadosExpSolic;
    private List<EppDocumento> lstEliminadosDocumento;
    private SbRecibos reciboBn;
    private List<EppRegistro> itemsXGT = null;
    private List<Map> lstDlgMapPolvorin = null;
    private List<EppPolvorin> lstDlgPolvorin = null;
    private List<EppLugarUso> lstDlgLugarUso = new ArrayList();
    private List<SbProvinciaGt> lstProvincia = new ArrayList();
    private List<SbDistritoGt> lstDistrito = new ArrayList();
    private SbDepartamentoGt dept;
    private SbProvinciaGt prov;
    private SbDistritoGt dist;
    private List<SbPersonaGt> lstDlgEmpresaTransp = null;
    private List<EppDocumento> lstDlgDocumento = null;
    private List<EppLicencia> lstDlgConductor = null;
    private Date fechaLlegada;
    private Date fechaSalida;
    private TipoExplosivoGt tipoEstadoBandeja;
    private String nroVoucher;
    private EppDocumento documento = new EppDocumento();
    private List<UploadedFile> uploadedFileList;
    private List<EppDocumento> lstDocumentosEliminados;
    private List<EppDocumento> lstDocumentosTemp;
    private List<EppLicencia> lstDlgCustodio = null;
    private List<EppGuiaTransitoPolicia> lstDlgPolicia = null;
    private List<EppGuiaTransitoVehiculo> lstDlgVehiculo = null;
    private List<EppLicencia> lstCustodiosPrivadosEliminados = null;
    private List<EppGuiaTransitoPolicia> lstCustodiosPoliciaEliminados = null;
    private String strConductor;
    private String strConductorIni;
    private String strVehiculo;
    private EppGuiaTransitoVehiculo vehiculo;
    private SbDistritoGt ubigeo;
    private String strNroSecuencia;
    private String strFechaMovimiento;
    private String strImporte;
    private boolean solicitarRecibo;
    private boolean solicitarVoucherTemp;
    private SbProcesoTupa parametrizacionTupa;
    private Long nroDeposito;
    private EppGteDeposito depositoTemp;
    private Double montoDeposito;
    private Date fechaDeposito;
    private String depositoAdjunto;
    private String depositoAdjuntoTemp;
    private UploadedFile fileDeposito;
    private StreamedContent depositoStreamed;
    private byte[] depositoByte;
    private Date fechaMinDeposito;
    private String imagenAyuda;
    //ORIGEN / DESTINO GUIA EXTERNA
    private List<EppPolvorin> lstDestinoPolvorin = new ArrayList();
    private List<EppLugarUso> lstDestinoLugarUso = new ArrayList();
    private List<EppLugarUsoUbigeo> lstLugarUsoUbigeo = new ArrayList();
    private boolean renderCboDestinoPolvorin = false;
    private boolean renderCboDestinoLugarUso = false;
    private boolean flgAceptoTransmitir = false;
    private boolean renderValidarTransmitir = false;
    /////////////////////
    /// Form Preview ////
    /////////////////////
    private String strViewRefRg;
    private String strViewEmpresa;
    private String strViewOrigen;
    private String strViewDestino;
    private String strViewTipoTransp;
    private String strViewEmpTransp;
    private String strViewCustodia;
    private String strViewVencimiento;
    private String strViewExpediente;
    private boolean disabledFechaLlegada;
    ///////////////////////
    private EppGuiaTransitoPolicia policia = new EppGuiaTransitoPolicia();
    private String strVerifDigit;
    private boolean disableReciboBn = false;
    private boolean disableResolucion = false;
    private boolean disableEmpTransporte = false;
    // FABRICAS
    private List<EppLocal> lstDlgLocal = null;
    private List<Map> lstDlgLocalMap = null;

    ////////// REPORTES ////////////
    private EppRegistro rgReporte;
    private EppGteRegistro solReporte;
    private boolean disableRgReporte = false;
    private List<EppRegistro> lstReporteRg = null;
    private List<EppRegistroGuiaTransito> lstReporteGuias = null;
    private boolean renderTablaRegistro = false;
    private boolean renderTablaGteRegistro = false;
    private List<DocumentoTemp> lstDocAdjTemp;

    //////// OPTIMIZACION ////////
    private List<Map> resultadosGuia = null;
    private List<Map> resultadosGuiaGte = null;    
    private List<Map> resultadosGuiaSeleccionados = null;
    private List<Map> resultadosGuiaGteSeleccionados = null;
    
    //////// GTE TIPO 3, 5 Y 6 ////////
    private List<Map> tiposDeGuia;
    private List<Map> tiposGTEDisponibles;
    private List lstExplosivosTemp = new ArrayList();
    private boolean renderSaldoActual = true;
    private boolean esSinGTRef = false;
    private boolean esGtSinTransf = false;
    private SbRecibos reciboBnTemp;
    private static final String gteConTransf = "'TP_RGUIA_ADQUI1', 'TP_RGUIA_COMFAB'";
    private static final String gteSinTransf = "'TP_RGUIA_ADQUI2', 'TP_RGUIA_INTER', 'TP_RGUIA_PLAPLA'";
    
    //// LME
    private int maxLength;
    private ListDataModel<EppRegistro> resultadosLME = null;
    
    /// DATOS ADICIONALES
    private UploadedFile file;
    private StreamedContent foto;
    private byte[] fotoByte;
    private String archivo;
    private String archivoTemporal;
    
    /// ELEGIR VOUCHER
    private boolean eligeReciboLista;

    public boolean isDisableEmpTransporte() {
        return disableEmpTransporte;
    }

    public void setDisableEmpTransporte(boolean disableEmpTransporte) {
        this.disableEmpTransporte = disableEmpTransporte;
    }

    public List<DocumentoTemp> getLstDocAdjTemp() {
        return lstDocAdjTemp;
    }

    public void setLstDocAdjTemp(List<DocumentoTemp> lstDocAdjTemp) {
        this.lstDocAdjTemp = lstDocAdjTemp;
    }

    public List<EppDocumento> getLstDocumentosTemp() {
        return lstDocumentosTemp;
    }

    public void setLstDocumentosTemp(List<EppDocumento> lstDocumentosTemp) {
        this.lstDocumentosTemp = lstDocumentosTemp;
    }

    public String getStrConductorIni() {
        return strConductorIni;
    }

    public void setStrConductorIni(String strConductorIni) {
        this.strConductorIni = strConductorIni;
    }

    public EppGteRegistro getSolReporte() {
        return solReporte;
    }

    public void setSolReporte(EppGteRegistro solReporte) {
        this.solReporte = solReporte;
    }

    public boolean isRenderValidarTransmitir() {
        return renderValidarTransmitir;
    }

    public void setRenderValidarTransmitir(boolean renderValidarTransmitir) {
        this.renderValidarTransmitir = renderValidarTransmitir;
    }

    public EppGteRegistro getCurrentBulk() {
        return currentBulk;
    }

    public void setCurrentBulk(EppGteRegistro currentBulk) {
        this.currentBulk = currentBulk;
    }

    public boolean isRenderTablaRegistro() {
        return renderTablaRegistro;
    }

    public void setRenderTablaRegistro(boolean renderTablaRegistro) {
        this.renderTablaRegistro = renderTablaRegistro;
    }

    public boolean isRenderTablaGteRegistro() {
        return renderTablaGteRegistro;
    }

    public void setRenderTablaGteRegistro(boolean renderTablaGteRegistro) {
        this.renderTablaGteRegistro = renderTablaGteRegistro;
    }

    public ListDataModel<EppGteRegistro> getResultadosGteReg() {
        return resultadosGteReg;
    }

    public void setResultadosGteReg(ListDataModel<EppGteRegistro> resultadosGteReg) {
        this.resultadosGteReg = resultadosGteReg;
    }

    public List<EppGteRegistro> getRegistrosGteRegSeleccionados() {
        return registrosGteRegSeleccionados;
    }

    public void setRegistrosGteRegSeleccionados(List<EppGteRegistro> registrosGteRegSeleccionados) {
        this.registrosGteRegSeleccionados = registrosGteRegSeleccionados;
    }

    public boolean isDisableResolucion() {
        return disableResolucion;
    }

    public void setDisableResolucion(boolean disableResolucion) {
        this.disableResolucion = disableResolucion;
    }

    public EppRegistro getRgReporte() {
        return rgReporte;
    }

    public void setRgReporte(EppRegistro rgReporte) {
        this.rgReporte = rgReporte;
    }

    public boolean isDisableRgReporte() {
        return disableRgReporte;
    }

    public void setDisableRgReporte(boolean disableRgReporte) {
        this.disableRgReporte = disableRgReporte;
    }

    public List<EppRegistro> getLstReporteRg() {
        return lstReporteRg;
    }

    public void setLstReporteRg(List<EppRegistro> lstReporteRg) {
        this.lstReporteRg = lstReporteRg;
    }

    public List<EppRegistroGuiaTransito> getLstReporteGuias() {
        return lstReporteGuias;
    }

    public void setLstReporteGuias(List<EppRegistroGuiaTransito> lstReporteGuias) {
        this.lstReporteGuias = lstReporteGuias;
    }

    public boolean isDisableReciboBn() {
        return disableReciboBn;
    }

    public void setDisableReciboBn(boolean disableReciboBn) {
        this.disableReciboBn = disableReciboBn;
    }

    public String getStrViewTipoTransp() {
        return strViewTipoTransp;
    }

    public void setStrViewTipoTransp(String strViewTipoTransp) {
        this.strViewTipoTransp = strViewTipoTransp;
    }

    public String getStrVerifDigit() {
        return strVerifDigit;
    }

    public void setStrVerifDigit(String strVerifDigit) {
        this.strVerifDigit = strVerifDigit;
    }

    public EppGuiaTransitoPolicia getPolicia() {
        return policia;
    }

    public void setPolicia(EppGuiaTransitoPolicia policia) {
        this.policia = policia;
    }

    public String getStrViewEmpTransp() {
        return strViewEmpTransp;
    }

    public void setStrViewEmpTransp(String strViewEmpTransp) {
        this.strViewEmpTransp = strViewEmpTransp;
    }

    public String getStrViewRefRg() {
        return strViewRefRg;
    }

    public void setStrViewRefRg(String strViewRefRg) {
        this.strViewRefRg = strViewRefRg;
    }

    public String getStrViewEmpresa() {
        return strViewEmpresa;
    }

    public void setStrViewEmpresa(String strViewEmpresa) {
        this.strViewEmpresa = strViewEmpresa;
    }

    public String getStrViewOrigen() {
        return strViewOrigen;
    }

    public void setStrViewOrigen(String strViewOrigen) {
        this.strViewOrigen = strViewOrigen;
    }

    public String getStrViewDestino() {
        return strViewDestino;
    }

    public void setStrViewDestino(String strViewDestino) {
        this.strViewDestino = strViewDestino;
    }

    public String getStrViewCustodia() {
        return strViewCustodia;
    }

    public void setStrViewCustodia(String strViewCustodia) {
        this.strViewCustodia = strViewCustodia;
    }

    public String getStrViewVencimiento() {
        return strViewVencimiento;
    }

    public void setStrViewVencimiento(String strViewVencimiento) {
        this.strViewVencimiento = strViewVencimiento;
    }

    public String getStrViewExpediente() {
        return strViewExpediente;
    }

    public void setStrViewExpediente(String strViewExpediente) {
        this.strViewExpediente = strViewExpediente;
    }

    public boolean isFlgAceptoTransmitir() {
        return flgAceptoTransmitir;
    }

    public void setFlgAceptoTransmitir(boolean flgAceptoTransmitir) {
        this.flgAceptoTransmitir = flgAceptoTransmitir;
    }

    public boolean isRenderCboDestinoPolvorin() {
        return renderCboDestinoPolvorin;
    }

    public void setRenderCboDestinoPolvorin(boolean renderCboDestinoPolvorin) {
        this.renderCboDestinoPolvorin = renderCboDestinoPolvorin;
    }

    public boolean isRenderCboDestinoLugarUso() {
        return renderCboDestinoLugarUso;
    }

    public void setRenderCboDestinoLugarUso(boolean renderCboDestinoLugarUso) {
        this.renderCboDestinoLugarUso = renderCboDestinoLugarUso;
    }

    public List<EppPolvorin> getLstDestinoPolvorin() {
        return lstDestinoPolvorin;
    }

    public void setLstDestinoPolvorin(List<EppPolvorin> lstDestinoPolvorin) {
        this.lstDestinoPolvorin = lstDestinoPolvorin;
    }

    public List<EppLugarUso> getLstDestinoLugarUso() {
        return lstDestinoLugarUso;
    }

    public void setLstDestinoLugarUso(List<EppLugarUso> lstDestinoLugarUso) {
        this.lstDestinoLugarUso = lstDestinoLugarUso;
    }

    public String getStrNroSecuencia() {
        return strNroSecuencia;
    }

    public void setStrNroSecuencia(String strNroSecuencia) {
        this.strNroSecuencia = strNroSecuencia;
    }

    public String getStrFechaMovimiento() {
        return strFechaMovimiento;
    }

    public void setStrFechaMovimiento(String strFechaMovimiento) {
        this.strFechaMovimiento = strFechaMovimiento;
    }

    public String getStrImporte() {
        return strImporte;
    }

    public void setStrImporte(String strImporte) {
        this.strImporte = strImporte;
    }

    public SbDistritoGt getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(SbDistritoGt ubigeo) {
        this.ubigeo = ubigeo;
    }

    public EppGuiaTransitoVehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(EppGuiaTransitoVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getStrConductor() {
        return strConductor;
    }

    public void setStrConductor(String strConductor) {
        this.strConductor = strConductor;
    }

    public String getStrVehiculo() {
        return strVehiculo;
    }

    public void setStrVehiculo(String strVehiculo) {
        this.strVehiculo = strVehiculo;
    }

    public List<EppLicencia> getLstCustodiosPrivadosEliminados() {
        return lstCustodiosPrivadosEliminados;
    }

    public void setLstCustodiosPrivadosEliminados(List<EppLicencia> lstCustodiosPrivadosEliminados) {
        this.lstCustodiosPrivadosEliminados = lstCustodiosPrivadosEliminados;
    }

    public List<EppGuiaTransitoPolicia> getLstCustodiosPoliciaEliminados() {
        return lstCustodiosPoliciaEliminados;
    }

    public void setLstCustodiosPoliciaEliminados(List<EppGuiaTransitoPolicia> lstCustodiosPoliciaEliminados) {
        this.lstCustodiosPoliciaEliminados = lstCustodiosPoliciaEliminados;
    }

    public List<EppLicencia> getLstDlgCustodio() {
        return lstDlgCustodio;
    }

    public void setLstDlgCustodio(List<EppLicencia> lstDlgCustodio) {
        this.lstDlgCustodio = lstDlgCustodio;
    }

    public List<EppGuiaTransitoPolicia> getLstDlgPolicia() {
        return lstDlgPolicia;
    }

    public void setLstDlgPolicia(List<EppGuiaTransitoPolicia> lstDlgPolicia) {
        this.lstDlgPolicia = lstDlgPolicia;
    }

    public List<EppGuiaTransitoVehiculo> getLstDlgVehiculo() {
        return lstDlgVehiculo;
    }

    public void setLstDlgVehiculo(List<EppGuiaTransitoVehiculo> lstDlgVehiculo) {
        this.lstDlgVehiculo = lstDlgVehiculo;
    }

    public List<EppDocumento> getLstDocumentosEliminados() {
        return lstDocumentosEliminados;
    }

    public void setLstDocumentosEliminados(List<EppDocumento> lstDocumentosEliminados) {
        this.lstDocumentosEliminados = lstDocumentosEliminados;
    }

    public List<UploadedFile> getUploadedFileList() {
        if (uploadedFileList == null) {
            uploadedFileList = new ArrayList<>();
        }
        return uploadedFileList;

    }

    public void setUploadedFileList(List<UploadedFile> uploadedFileList) {
        this.uploadedFileList = uploadedFileList;
    }

    public EppDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(EppDocumento documento) {
        this.documento = documento;
    }

    public String getNroVoucher() {
        return nroVoucher;
    }

    public void setNroVoucher(String nroVoucher) {
        this.nroVoucher = nroVoucher;
    }

    public TipoExplosivoGt getTipoEstadoBandeja() {
        return tipoEstadoBandeja;
    }

    public void setTipoEstadoBandeja(TipoExplosivoGt tipoEstadoBandeja) {
        this.tipoEstadoBandeja = tipoEstadoBandeja;
    }

    public EppRegistroGuiaTransito getCurrent() {
        return current;
    }

    public void setCurrent(EppRegistroGuiaTransito current) {
        this.current = current;
    }

    public String getLdField() {
        return ldField;
    }

    public void setLdField(String ldField) {
        this.ldField = ldField;
    }

    public String getLdFiltro() {
        return ldFiltro;
    }

    public void setLdFiltro(String ldFiltro) {
        this.ldFiltro = ldFiltro;
    }

    public ListDataModel<EppRegistroGuiaTransito> getResultados() {
        return resultados;
    }

    public void setResultados(ListDataModel<EppRegistroGuiaTransito> resultados) {
        this.resultados = resultados;
    }

    public ListDataModel<SbRecibos> getLstRecibosBn() {
        return lstRecibosBn;
    }

    public void setLstRecibosBn(ListDataModel<SbRecibos> lstRecibosBn) {
        this.lstRecibosBn = lstRecibosBn;
    }

    public List<SbRecibos> getLstRecibosBnUnq() {
        return lstRecibosBnUnq;
    }

    public void setLstRecibosBnUnq(List<SbRecibos> lstRecibosBnUnq) {
        this.lstRecibosBnUnq = lstRecibosBnUnq;
    }

    public List<EppRegistroGuiaTransito> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    public void setRegistrosSeleccionados(List<EppRegistroGuiaTransito> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getFiltroDni() {
        return filtroDni;
    }

    public void setFiltroDni(String filtroDni) {
        this.filtroDni = filtroDni;
    }

    public String getFiltroRznSoc() {
        return filtroRznSoc;
    }

    public void setFiltroRznSoc(String filtroRznSoc) {
        this.filtroRznSoc = filtroRznSoc;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getCriterioResGuia() {
        return criterioResGuia;
    }

    public void setCriterioResGuia(String criterioResGuia) {
        this.criterioResGuia = criterioResGuia;
    }

    public String getTxtTipoTramite() {
        return txtTipoTramite;
    }

    public void setTxtTipoTramite(String txtTipoTramite) {
        this.txtTipoTramite = txtTipoTramite;
    }

    public boolean isDeshabilitaRadioTipoRg() {
        return deshabilitaRadioTipoRg;
    }

    public void setDeshabilitaRadioTipoRg(boolean deshabilitaRadioTipoRg) {
        this.deshabilitaRadioTipoRg = deshabilitaRadioTipoRg;
    }

    public TipoExplosivoGt getTipoOrigen() {
        return tipoOrigen;
    }

    public void setTipoOrigen(TipoExplosivoGt tipoOrigen) {
        this.tipoOrigen = tipoOrigen;
    }

    public TipoExplosivoGt getTipoDestino() {
        return tipoDestino;
    }

    public void setTipoDestino(TipoExplosivoGt tipoDestino) {
        this.tipoDestino = tipoDestino;
    }

    public TipoExplosivoGt getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoExplosivoGt tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public TipoExplosivoGt getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(TipoExplosivoGt tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public EppRegistro getResolucionGerencia() {
        return resolucionGerencia;
    }

    public void setResolucionGerencia(EppRegistro resolucionGerencia) {
        this.resolucionGerencia = resolucionGerencia;
    }

    public boolean isBloqueaSelectOrigen() {
        return bloqueaSelectOrigen;
    }

    public void setBloqueaSelectOrigen(boolean bloqueaSelectOrigen) {
        this.bloqueaSelectOrigen = bloqueaSelectOrigen;
    }

    public boolean isBloqueaSelectDestino() {
        return bloqueaSelectDestino;
    }

    public void setBloqueaSelectDestino(boolean bloqueaSelectDestino) {
        this.bloqueaSelectDestino = bloqueaSelectDestino;
    }

    public boolean isBloqueaBotonBuscarOrigen() {
        return bloqueaBotonBuscarOrigen;
    }

    public void setBloqueaBotonBuscarOrigen(boolean bloqueaBotonBuscarOrigen) {
        this.bloqueaBotonBuscarOrigen = bloqueaBotonBuscarOrigen;
    }

    public boolean isBloqueaBotonBuscarDestino() {
        return bloqueaBotonBuscarDestino;
    }

    public void setBloqueaBotonBuscarDestino(boolean bloqueaBotonBuscarDestino) {
        this.bloqueaBotonBuscarDestino = bloqueaBotonBuscarDestino;
    }

    public String getNombreEmpresaOrigen() {
        return nombreEmpresaOrigen;
    }

    public void setNombreEmpresaOrigen(String nombreEmpresaOrigen) {
        this.nombreEmpresaOrigen = nombreEmpresaOrigen;
    }

    public String getNombreEmpresaDestino() {
        return nombreEmpresaDestino;
    }

    public void setNombreEmpresaDestino(String nombreEmpresaDestino) {
        this.nombreEmpresaDestino = nombreEmpresaDestino;
    }

    public Integer getCantidadAutorizada() {
        return cantidadAutorizada;
    }

    public void setCantidadAutorizada(Integer cantidadAutorizada) {
        this.cantidadAutorizada = cantidadAutorizada;
    }

    public Double getCantidadATransportar() {
        return cantidadATransportar;
    }

    public void setCantidadATransportar(Double cantidadATransportar) {
        this.cantidadATransportar = cantidadATransportar;
    }

    public List getLstExplosivosCombo() {
        return lstExplosivosCombo;
    }

    public void setLstExplosivosCombo(List lstExplosivosCombo) {
        this.lstExplosivosCombo = lstExplosivosCombo;
    }

    public List<TipoExplosivoGt> getLstTipoGtOrigen() {
        return lstTipoGtOrigen;
    }

    public void setLstTipoGtOrigen(List<TipoExplosivoGt> lstTipoGtOrigen) {
        this.lstTipoGtOrigen = lstTipoGtOrigen;
    }

    public List<TipoExplosivoGt> getLstTipoGtDestino() {
        return lstTipoGtDestino;
    }

    public void setLstTipoGtDestino(List<TipoExplosivoGt> lstTipoGtDestino) {
        this.lstTipoGtDestino = lstTipoGtDestino;
    }

    public List<EppDocumento> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<EppDocumento> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public boolean isMostrarresglobprev() {
        return mostrarresglobprev;
    }

    public void setMostrarresglobprev(boolean mostrarresglobprev) {
        this.mostrarresglobprev = mostrarresglobprev;
    }

    public SbPersonaGt getEmpresa() {
        return empresa;
    }

    public void setEmpresa(SbPersonaGt empresa) {
        this.empresa = empresa;
    }

    public SbPersonaGt getEmpresaTransporte() {
        return empresaTransporte;
    }

    public void setEmpresaTransporte(SbPersonaGt empresaTransporte) {
        this.empresaTransporte = empresaTransporte;
    }

    public EppExplosivo getExplosivoSeleccionado() {
        return explosivoSeleccionado;
    }

    public void setExplosivoSeleccionado(EppExplosivo explosivoSeleccionado) {
        this.explosivoSeleccionado = explosivoSeleccionado;
    }

    public Date getFechaCaducidadResolucionGerencia() {
        return fechaCaducidadResolucionGerencia;
    }

    public void setFechaCaducidadResolucionGerencia(Date fechaCaducidadResolucionGerencia) {
        this.fechaCaducidadResolucionGerencia = fechaCaducidadResolucionGerencia;
    }

    public String getNumeroResolucionGerencia() {
        return numeroResolucionGerencia;
    }

    public void setNumeroResolucionGerencia(String numeroResolucionGerencia) {
        this.numeroResolucionGerencia = numeroResolucionGerencia;
    }

    public String getTipoResolucionGerencia() {
        return tipoResolucionGerencia;
    }

    public void setTipoResolucionGerencia(String tipoResolucionGerencia) {
        this.tipoResolucionGerencia = tipoResolucionGerencia;
    }

    public List getLstlugardeuso() {
        return lstlugardeuso;
    }

    public void setLstlugardeuso(List lstlugardeuso) {
        this.lstlugardeuso = lstlugardeuso;
    }

    public EppPolvorin getPolvorin() {
        return polvorin;
    }

    public void setPolvorin(EppPolvorin polvorin) {
        this.polvorin = polvorin;
    }

    public EppLugarUso getLugarUso() {
        return lugarUso;
    }

    public void setLugarUso(EppLugarUso lugarUso) {
        this.lugarUso = lugarUso;
    }

    public boolean isMostrarLugarUsoUbigeoOrigen() {
        return mostrarLugarUsoUbigeoOrigen;
    }

    public void setMostrarLugarUsoUbigeoOrigen(boolean mostrarLugarUsoUbigeoOrigen) {
        this.mostrarLugarUsoUbigeoOrigen = mostrarLugarUsoUbigeoOrigen;
    }

    public boolean isMostrarLugarUsoUbigeoDestino() {
        return mostrarLugarUsoUbigeoDestino;
    }

    public void setMostrarLugarUsoUbigeoDestino(boolean mostrarLugarUsoUbigeoDestino) {
        this.mostrarLugarUsoUbigeoDestino = mostrarLugarUsoUbigeoDestino;
    }

    public EppLugarUsoUbigeo getLugarUsoUbigeoOrigen() {
        return lugarUsoUbigeoOrigen;
    }

    public void setLugarUsoUbigeoOrigen(EppLugarUsoUbigeo lugarUsoUbigeoOrigen) {
        this.lugarUsoUbigeoOrigen = lugarUsoUbigeoOrigen;
    }

    public EppLugarUsoUbigeo getLugarUsoUbigeoDestino() {
        return lugarUsoUbigeoDestino;
    }

    public void setLugarUsoUbigeoDestino(EppLugarUsoUbigeo lugarUsoUbigeoDestino) {
        this.lugarUsoUbigeoDestino = lugarUsoUbigeoDestino;
    }

    public List<EppLugarUsoUbigeo> getLstLugarUsoUbigeo() {
        return lstLugarUsoUbigeo;
    }

    public void setLstLugarUsoUbigeo(List<EppLugarUsoUbigeo> lstLugarUsoUbigeo) {
        this.lstLugarUsoUbigeo = lstLugarUsoUbigeo;
    }

    public boolean isDisableLugarUsoUbigeoOrigen() {
        return disableLugarUsoUbigeoOrigen;
    }

    public void setDisableLugarUsoUbigeoOrigen(boolean disableLugarUsoUbigeoOrigen) {
        this.disableLugarUsoUbigeoOrigen = disableLugarUsoUbigeoOrigen;
    }

    public boolean isDisableLugarUsoUbigeoDestino() {
        return disableLugarUsoUbigeoDestino;
    }

    public void setDisableLugarUsoUbigeoDestino(boolean disableLugarUsoUbigeoDestino) {
        this.disableLugarUsoUbigeoDestino = disableLugarUsoUbigeoDestino;
    }

    public EppDocumento getDocmtc() {
        return docmtc;
    }

    public void setDocmtc(EppDocumento docmtc) {
        this.docmtc = docmtc;
    }

    public Double getSaldoActualExplSolic() {
        return saldoActualExplSolic;
    }

    public void setSaldoActualExplSolic(Double saldoActualExplSolic) {
        this.saldoActualExplSolic = saldoActualExplSolic;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public boolean isFlgCustodio() {
        return flgCustodio;
    }

    public void setFlgCustodio(boolean flgCustodio) {
        this.flgCustodio = flgCustodio;
    }

    public List<EppLicencia> getLstEliminadosLicencia() {
        return lstEliminadosLicencia;
    }

    public void setLstEliminadosLicencia(List<EppLicencia> lstEliminadosLicencia) {
        this.lstEliminadosLicencia = lstEliminadosLicencia;
    }

    public List<EppGteExplosivoSolicita> getLstEliminadosExpSolic() {
        return lstEliminadosExpSolic;
    }

    public void setLstEliminadosExpSolic(List<EppGteExplosivoSolicita> lstEliminadosExpSolic) {
        this.lstEliminadosExpSolic = lstEliminadosExpSolic;
    }

    public List<EppDocumento> getLstEliminadosDocumento() {
        return lstEliminadosDocumento;
    }

    public void setLstEliminadosDocumento(List<EppDocumento> lstEliminadosDocumento) {
        this.lstEliminadosDocumento = lstEliminadosDocumento;
    }

    public SbRecibos getReciboBn() {
        return reciboBn;
    }

    public void setReciboBn(SbRecibos reciboBn) {
        this.reciboBn = reciboBn;
    }

    public List<EppRegistro> getItemsXGT() {
        return itemsXGT;
    }

    public void setItemsXGT(List<EppRegistro> itemsXGT) {
        this.itemsXGT = itemsXGT;
    }

    public List<EppPolvorin> getLstDlgPolvorin() {
        return lstDlgPolvorin;
    }

    public void setLstDlgPolvorin(List<EppPolvorin> lstDlgPolvorin) {
        this.lstDlgPolvorin = lstDlgPolvorin;
    }

    public List<EppLugarUso> getLstDlgLugarUso() {
        return lstDlgLugarUso;
    }

    public void setLstDlgLugarUso(List<EppLugarUso> lstDlgLugarUso) {
        this.lstDlgLugarUso = lstDlgLugarUso;
    }

    public List<SbProvinciaGt> getLstProvincia() {
        return lstProvincia;
    }

    public void setLstProvincia(List<SbProvinciaGt> lstProvincia) {
        this.lstProvincia = lstProvincia;
    }

    public List<SbDistritoGt> getLstDistrito() {
        return lstDistrito;
    }

    public void setLstDistrito(List<SbDistritoGt> lstDistrito) {
        this.lstDistrito = lstDistrito;
    }

    public SbDepartamentoGt getDept() {
        return dept;
    }

    public void setDept(SbDepartamentoGt dept) {
        this.dept = dept;
    }

    public SbProvinciaGt getProv() {
        return prov;
    }

    public void setProv(SbProvinciaGt prov) {
        this.prov = prov;
    }

    public SbDistritoGt getDist() {
        return dist;
    }

    public void setDist(SbDistritoGt dist) {
        this.dist = dist;
    }

    public List<SbPersonaGt> getLstDlgEmpresaTransp() {
        return lstDlgEmpresaTransp;
    }

    public void setLstDlgEmpresaTransp(List<SbPersonaGt> lstDlgEmpresaTransp) {
        this.lstDlgEmpresaTransp = lstDlgEmpresaTransp;
    }

    public List<EppDocumento> getLstDlgDocumento() {
        return lstDlgDocumento;
    }

    public void setLstDlgDocumento(List<EppDocumento> lstDlgDocumento) {
        this.lstDlgDocumento = lstDlgDocumento;
    }

    public List<EppLicencia> getLstDlgConductor() {
        return lstDlgConductor;
    }

    public void setLstDlgConductor(List<EppLicencia> lstDlgConductor) {
        this.lstDlgConductor = lstDlgConductor;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public List<Map> getResultadosGuia() {
        return resultadosGuia;
    }

    public void setResultadosGuia(List<Map> resultadosGuia) {
        this.resultadosGuia = resultadosGuia;
    }

    public List<Map> getResultadosGuiaGte() {
        return resultadosGuiaGte;
    }

    public void setResultadosGuiaGte(List<Map> resultadosGuiaGte) {
        this.resultadosGuiaGte = resultadosGuiaGte;
    }

    public List<Map> getResultadosGuiaSeleccionados() {
        return resultadosGuiaSeleccionados;
    }

    public void setResultadosGuiaSeleccionados(List<Map> resultadosGuiaSeleccionados) {
        this.resultadosGuiaSeleccionados = resultadosGuiaSeleccionados;
    }

    public List<Map> getResultadosGuiaGteSeleccionados() {
        return resultadosGuiaGteSeleccionados;
    }

    public void setResultadosGuiaGteSeleccionados(List<Map> resultadosGuiaGteSeleccionados) {
        this.resultadosGuiaGteSeleccionados = resultadosGuiaGteSeleccionados;
    }

    public boolean isDisabledFechaLlegada() {
        return disabledFechaLlegada;
    }

    public void setDisabledFechaLlegada(boolean disabledFechaLlegada) {
        this.disabledFechaLlegada = disabledFechaLlegada;
    }

    public List<EppLocal> getLstDlgLocal() {
        return lstDlgLocal;
    }

    public void setLstDlgLocal(List<EppLocal> lstDlgLocal) {
        this.lstDlgLocal = lstDlgLocal;
    }

    public EppLocal getFabrica() {
        return fabrica;
    }

    public void setFabrica(EppLocal fabrica) {
        this.fabrica = fabrica;
    }

    public List<Map> getTiposDeGuia() {
        return tiposDeGuia;
    }

    public void setTiposDeGuia(List<Map> tiposDeGuia) {
        this.tiposDeGuia = tiposDeGuia;
    }

    public List<Map> getTiposGTEDisponibles() {
        return tiposGTEDisponibles;
    }

    public void setTiposGTEDisponibles(List<Map> tiposGTEDisponibles) {
        this.tiposGTEDisponibles = tiposGTEDisponibles;
    }

    public boolean isRenderSaldoActual() {
        return renderSaldoActual;
    }

    public void setRenderSaldoActual(boolean renderSaldoActual) {
        this.renderSaldoActual = renderSaldoActual;
    }

    public SbRecibos getReciboBnTemp() {
        return reciboBnTemp;
    }

    public void setReciboBnTemp(SbRecibos reciboBnTemp) {
        this.reciboBnTemp = reciboBnTemp;
    }

    public boolean isEsSinGTRef() {
        return esSinGTRef;
    }

    public void setEsSinGTRef(boolean esSinGTRef) {
        this.esSinGTRef = esSinGTRef;
    }

    public boolean isEsGtSinTransf() {
        return esGtSinTransf;
    }

    public void setEsGtSinTransf(boolean esGtSinTransf) {
        this.esGtSinTransf = esGtSinTransf;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public ListDataModel<EppRegistro> getResultadosLME() {
        return resultadosLME;
    }

    public void setResultadosLME(ListDataModel<EppRegistro> resultadosLME) {
        this.resultadosLME = resultadosLME;
    }

    public List<Map> getLstDlgLocalMap() {
        return lstDlgLocalMap;
    }

    public void setLstDlgLocalMap(List<Map> lstDlgLocalMap) {
        this.lstDlgLocalMap = lstDlgLocalMap;
    }

    public boolean isSolicitarRecibo() {
        return solicitarRecibo;
    }

    public void setSolicitarRecibo(boolean solicitarRecibo) {
        this.solicitarRecibo = solicitarRecibo;
    }

    public boolean isSolicitarVoucherTemp() {
        return solicitarVoucherTemp;
    }

    public void setSolicitarVoucherTemp(boolean solicitarVoucherTemp) {
        this.solicitarVoucherTemp = solicitarVoucherTemp;
    }

    public List<Map> getLstDlgMapPolvorin() {
        return lstDlgMapPolvorin;
    }

    public void setLstDlgMapPolvorin(List<Map> lstDlgMapPolvorin) {
        this.lstDlgMapPolvorin = lstDlgMapPolvorin;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Long getNroDeposito() {
        return nroDeposito;
    }

    public void setNroDeposito(Long nroDeposito) {
        this.nroDeposito = nroDeposito;
    }

    public String getDepositoAdjunto() {
        return depositoAdjunto;
    }

    public void setDepositoAdjunto(String depositoAdjunto) {
        this.depositoAdjunto = depositoAdjunto;
    }

    public UploadedFile getFileDeposito() {
        return fileDeposito;
    }

    public void setFileDeposito(UploadedFile fileDeposito) {
        this.fileDeposito = fileDeposito;
    }

    public StreamedContent getDepositoStreamed() {
        return depositoStreamed;
    }

    public void setDepositoStreamed(StreamedContent depositoStreamed) {
        this.depositoStreamed = depositoStreamed;
    }

    public byte[] getDepositoByte() {
        return depositoByte;
    }

    public void setDepositoByte(byte[] depositoByte) {
        this.depositoByte = depositoByte;
    }

    public String getArchivoTemporal() {
        return archivoTemporal;
    }

    public void setArchivoTemporal(String archivoTemporal) {
        this.archivoTemporal = archivoTemporal;
    }

    public double getMontoDeposito() {
        return montoDeposito;
    }

    public void setMontoDeposito(double montoDeposito) {
        this.montoDeposito = montoDeposito;
    }

    public Date getFechaDeposito() {
        return fechaDeposito;
    }

    public void setFechaDeposito(Date fechaDeposito) {
        this.fechaDeposito = fechaDeposito;
    }

    public Date getFechaMinDeposito() {
        return fechaMinDeposito;
    }

    public void setFechaMinDeposito(Date fechaMinDeposito) {
        this.fechaMinDeposito = fechaMinDeposito;
    }

    public EppGteDeposito getDepositoTemp() {
        return depositoTemp;
    }

    public void setDepositoTemp(EppGteDeposito depositoTemp) {
        this.depositoTemp = depositoTemp;
    }

    public String getImagenAyuda() {
        return imagenAyuda;
    }

    public void setImagenAyuda(String imagenAyuda) {
        this.imagenAyuda = imagenAyuda;
    }

    public boolean isEligeReciboLista() {
        return eligeReciboLista;
    }

    public void setEligeReciboLista(boolean eligeReciboLista) {
        this.eligeReciboLista = eligeReciboLista;
    }    
    
    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public EppRegistroGuiaTransito getEppRegistroGuiaTransito(Long id) {
        return eppRegistroGuiaTransitoFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public EppRegistroGuiaTransitoController() {
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "eppRegistroGuiaTransitoConverter")
    public static class EppRegistroGuiaTransitoControllerConverterN extends EppRegistroGuiaTransitoControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = EppRegistroGuiaTransito.class)
    public static class EppRegistroGuiaTransitoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EppRegistroGuiaTransitoController c = (EppRegistroGuiaTransitoController) JsfUtil.obtenerBean("eppRegistroGuiaTransitoController", EppRegistroGuiaTransitoController.class);
            return c.getEppRegistroGuiaTransito(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EppRegistroGuiaTransito) {
                EppRegistroGuiaTransito o = (EppRegistroGuiaTransito) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + EppRegistroGuiaTransito.class.getName());
            }
        }
    }

    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    ////////////////  PROCESO GUIA DE TRANSITO EXTERNO   ////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    public void buscarRecibo() {
        lstRecibosBn = new ListDataModel(sbRecibosFacade.lstRecibos(JsfUtil.getLoggedUser().getNumDoc(), nroVoucher));
        if (nroVoucher != null && !"".equals(nroVoucher)) {
            if (lstRecibosBn.getRowCount() == 0) {
                JsfUtil.mensajeAdvertencia("No se encontró ningún registro con el criterio ingresado");
            }
        }
    }

    public String direccionarReporteTraslado() {
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        if(empresa != null){
            if (permitirCrearListarReporte(empresa)) {
                ///////|
                rgReporte = null;
                lstReporteRg = null;
                lstReporteGuias = null;
                setDisableRgReporte(false);
                return "/aplicacion/eppRegistroGuiaTransito/ReporteTraslados";
            } else {
                JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN DE ADQUISICIÓN Y USO DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
            }
        }else{
            JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN DE ADQUISICIÓN Y USO DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
        }
        return null;
    }

    public String direccionarReporteSolicitudes() {
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        filtro = null;
        resultadosGteReg = null;
        resultadosGuiaGte = null;
        return "/aplicacion/eppRegistroGuiaTransito/ReporteSolicitudes";
    }

    /**
     * Metodo para agregar datos adicionales a la GT
     *
     * @return
     */
    public String emitirGt() {
        Calendar c_hoy = Calendar.getInstance();
        reiniciarValoresVerPreview();
        currentBulk = new EppGteRegistro();
        currentBulk = (EppGteRegistro) resultadosGteReg.getRowData();
        
        tipoTransporte = currentBulk.getTipoTransporte();
        empresaTransporte = currentBulk.getEmpresaTranspId();
        
        obtenerTipoGTECurrentBulk(false,currentBulk);
        cargarRazonSocOrigenDestinoBulk(currentBulk);
        strViewEmpresa = mostrarClienteString(currentBulk.getEmpresaId());
        strViewRefRg = "REF. R.G.:<b>" + currentBulk.getResolucionId().getNumero() + "</b>";
        strViewOrigen = mostrarDatosOrigenBulk(currentBulk, false);
        strViewDestino = mostrarDatosDestinoBulk(currentBulk, false);
        strViewEmpTransp = mostrarClienteString(currentBulk.getEmpresaTranspId()) == null ? "" : ("Los explosivos serán trasladados por:<br/>"
                + "EMPRESA DE TRANSPORTES: " + mostrarClienteString(currentBulk.getEmpresaTranspId()));
        return "/aplicacion/eppRegistroGuiaTransito/ViewEmitirGt";
    }

    /**
     * DIRECCIONAMIENTO AL MENU BANDEJA DE USUARIO
     *
     * @return
     */
    public String direccionarVerPreview() {
        Calendar c_hoy = Calendar.getInstance();
        if(currentBulk.getResolucionId() != null){
            if (JsfUtil.getFechaSinHora(c_hoy.getTime()).compareTo(JsfUtil.getFechaSinHora(currentBulk.getResolucionId().getFechaFin())) > 0) {
                // VENCIDA
                JsfUtil.mensajeAdvertencia("La resolución de la guía transferida está vencida.");
                return null;
            }
        }
        
        reiniciarValoresVerPreview();
        if(currentBulk.getResolucionId() != null){
            strViewRefRg = "REF. R.G.:<b>" + currentBulk.getResolucionId().getNumero() + "</b>";    
        }
        
        strViewEmpresa = mostrarClienteString(currentBulk.getEmpresaId());
        strViewOrigen = mostrarDatosOrigenBulk(currentBulk, true);
        strViewDestino = mostrarDatosDestinoBulk(currentBulk, true);
        strViewEmpTransp = mostrarClienteString(currentBulk.getEmpresaTranspId()) == null ? "" : ("Los explosivos serán trasladados por:<br/>"
                + "EMPRESA DE TRANSPORTES: " + mostrarClienteString(currentBulk.getEmpresaTranspId()));
        strViewCustodia = "<b>" + (currentBulk.getCustodia() == 1 ? "REQUIERE CUSTODIA" : "NO REQUIERE CUSTODIA") + "</b>";
        strViewVencimiento = "30 DÍAS HÁBILES A PARTIR DE HOY";
        strViewExpediente = "<b>NRO EXPEDIENTE PENDIENTE</b>";
        return "/aplicacion/eppRegistroGuiaTransito/ViewEmitirGtPreview";
    }

    private void reiniciarValoresVerPreview() {
        strViewRefRg = null;
        strViewEmpresa = null;
        strViewOrigen = null;
        strViewDestino = null;
        strViewCustodia = null;
        strViewVencimiento = null;
        strViewExpediente = null;
        strConductor = null;
        strVehiculo = null;
    }

    /**
     * Muestra el nombre del cliente de acuerdo al tipo de persona
     *
     * @param cli
     * @return
     */
    private String mostrarClienteString(SbPersonaGt cli) {
        try {
            String strCli;
            strCli = "" + ((cli.getRznSocial() == null) ? cli.getNombreCompleto() : cli.getRznSocial());
            return strCli;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Listado de Guias de Transito
     *
     * @return
     */
    public String direccionarListadoGTUsua() {
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        if(empresa != null){
            llenarTiposGTE();
            if (validarRGpermitidasConTransfer(empresa)) {                
                resultados = null;
                resultadosGteReg = null;
                resultadosGuia = null;
                resultadosGuiaGte = null;
                registrosSeleccionados = null;
                registrosGteRegSeleccionados = null;
                resultadosGuiaSeleccionados = null;
                resultadosGuiaGteSeleccionados = null;
                filtro = null;
                fechaIni = null;
                tipoEstadoBandeja = null;
                tipoBusqueda = null;
                setRenderValidarTransmitir(false);
                setEsGtSinTransf(false);
                setEsSinGTRef(false);
                if(tiposGTEDisponibles.size() == 1){
                    JsfUtil.mensajeAdvertencia("Usted solo cuenta con autorización de "+ tiposGTEDisponibles.get(0).get("descripcion").toString().substring(2));
                }
                return "/aplicacion/eppRegistroGuiaTransito/ListadoUsuario";
            } else {
                JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN DE ADQUISICIÓN Y USO DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
            }
        }else{
            JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN DE ADQUISICIÓN Y USO DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
        }
        return null;
    }

    /**
     * DIRECCIONA AL MENU BANDEJA DEL FABRICANTE
     *
     * @return
     */
    public String direccionarGTFabri() {
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        if(empresa != null){
            llenarTiposGTE();
            if (permitirEmitir(empresa)) {
                if(!cargarDatos()){
                    JsfUtil.mensajeError("No se encontró parametrización de los recibos");
                    return null;
                }
                DatosUsuario d = JsfUtil.getLoggedUser();
                //lstRecibosBn = new ListDataModel(sbRecibosFacade.lstRecibos(d.getNumDoc(), nroVoucher));                
                resultados = null;
                resultadosGteReg = null;
                resultadosGuia = null;
                resultadosGuiaGte = null;
                registrosSeleccionados = null;
                registrosGteRegSeleccionados = null;
                resultadosGuiaSeleccionados = null;
                resultadosGuiaGteSeleccionados = null;
                filtro = null;
                tipoEstadoBandeja = null;
                tipoBusqueda = null;
                strConductor = null;
                strVehiculo = null;
                setEsGtSinTransf(false);
                setEsSinGTRef(false);
                RequestContext.getCurrentInstance().update("listForm");
                return "/aplicacion/eppRegistroGuiaTransito/BandejaFabricante";
            } else {
                JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN PARA LA FABRICACIÓN DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
            }
        }else{
            JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN PARA LA FABRICACIÓN DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
        } 
        return null;
    }
    
    public void actualizaBuscarPor(){
        RequestContext.getCurrentInstance().update("listForm:pnlBusqueda");
    }

    /**
     * DIRECCIONA AL MENU BANDEJA DEL FABRICANTE
     *
     * @return
     */
    public String regresarEmitir() {        
        if(currentBulk != null){
            // Conductor
            if(currentBulk.getEppLicenciaList1() != null ){
                if(!currentBulk.getEppLicenciaList1().isEmpty()){
                    strConductor = currentBulk.getEppLicenciaList1().get(0).getPersonaId().getNombreCompleto();
                }
            }
            // Vehículo
            if(currentBulk.getEppGuiaTransitoVehiculoList() != null ){
                if(!currentBulk.getEppGuiaTransitoVehiculoList().isEmpty()){
                    strVehiculo = currentBulk.getEppGuiaTransitoVehiculoList().get(0).getMarca() + " / " + currentBulk.getEppGuiaTransitoVehiculoList().get(0).getPlaca();
                }
            }
        }
        return "/aplicacion/eppRegistroGuiaTransito/ViewEmitirGt";
    }

    public void inicializar() {
        setCriterioResGuia(null);
        setMostrarresglobprev(false);
        setMostrarLugarUsoUbigeoOrigen(false);
        setMostrarLugarUsoUbigeoDestino(false);
        tipoOrigen = null;
        tipoDestino = null;
        resolucionGerencia = null;
        numeroResolucionGerencia = null;
        fechaCaducidadResolucionGerencia = null;
        tipoResolucionGerencia = null;
        lstTipoGtOrigen = null;
        lstTipoGtDestino = null;
        nombreEmpresaOrigen = null;
        nombreEmpresaDestino = null;
        tipoTransporte = null;
        empresaTransporte = null;
        setEligeReciboLista(false);
        reciboBn = null;
        reciboBnTemp = null;
        strNroSecuencia = null;
        strFechaMovimiento = null;
        strImporte = null;
        setDisableReciboBn(false);
        setDisableResolucion(false);
        setDisableEmpTransporte(false);
        reciboBn = null;
        lstDocumentosTemp = null;
        setRenderSaldoActual(true);
        nroDeposito = null;
        montoDeposito = 0D;
        fechaDeposito = null;
        depositoTemp = null;
        depositoAdjunto = null;
        depositoAdjuntoTemp = null;
        fileDeposito = null;
        depositoByte = null;
        depositoStreamed = null;
        depositoTemp = null;
    }

    public boolean cargarDatos(){
        try {
            solicitarRecibo = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXNombre("HABILITACION_TUPA").getValor());
            solicitarVoucherTemp = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXNombre("HABILITACION_VOUCHER_TEMP").getValor());
        } catch (Exception e) {
            solicitarRecibo = false;
            solicitarVoucherTemp = false;
        }
        parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoId("TP_TUP_SUC1", 710);  // TUPA 2018, Proceso GTE
        if(solicitarVoucherTemp){
            fechaMinDeposito = JsfUtil.stringToDate(ejbSbParametroFacade.obtenerParametroXNombre("eppRegistroGuiaTransito_fechaMinVc").getValor(), "dd/MM/yyyy");
        }
        return true;
    }
    
    /**
     * METODO PARA EL REGISTRO DE LA GUIA DE TRANSITO DEL ADMINISTRADO
     *
     * @return
     */
    public String mostrarCrear() {
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        if(empresa != null){
            llenarTiposGTE();
            if (validarRGpermitidas(empresa)) {
                inicializar();
                
                if(!cargarDatos()){
                    return null;
                }
                //INICIANDO ATRIBUTOS DE GUIA DE TRANSITO        
                currentBulk = new EppGteRegistro();
                currentBulk.setEmpresaId(empresa);
                //currentBulk.setEppLicenciaList(new ArrayList<EppLicencia>());
                //currentBulk.setEppLicenciaList1(new ArrayList<EppLicencia>());
                currentBulk.setEppGteExplosivoSolicitaList(new ArrayList<EppGteExplosivoSolicita>());
                currentBulk.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                currentBulk.setAudNumIp(JsfUtil.getIpAddress());
                currentBulk.setActivo((short) 1);

                //INICIANDO ATRIBUTOS DE REGISTRO        
                currentBulk.setEppDocumentoList(new ArrayList());
                currentBulk.setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                currentBulk.setTipoProId(ejbTipoBaseFacade.findByCodProg("TP_PRTUP_GUIS").get(0));
                currentBulk.setTipoRegId(ejbTipoBaseFacade.findByCodProg("TP_REGIST_NOR").get(0));
                currentBulk.setTipoOpeId(ejbTipoBaseFacade.findByCodProg("TP_OPE_INI").get(0));
                currentBulk.setEmpresaId(empresa);
                currentBulk.setFecha(new Date());
                currentBulk.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                currentBulk.setAudNumIp(JsfUtil.getIpAddress());
                currentBulk.setActivo((short) 1);
                setDisableResolucion(true);
                setRenderSaldoActual(true);
                return "/aplicacion/eppRegistroGuiaTransito/Create";
            } else {
                JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN DE ADQUISICIÓN Y USO DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
            }
        }else{
            JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN DE ADQUISICIÓN Y USO DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
        }
        return null;            
    }

    /**
     * METODO PARA EDITAR LA GUIA DE TRANSITO DEL ADMINISTRADO
     *
     * @return
     */
    public String mostrarEditar() {
        currentBulk = new EppGteRegistro();
        currentBulk = (EppGteRegistro) resultadosGteReg.getRowData();
        lstDocumentosTemp = null;
        depositoTemp = null;
        llenarTiposGTE();
        
        if(!cargarDatos()){
            return null;
        }
        
        /// recibo
        if(currentBulk.getSbRecibosList() != null && !currentBulk.getSbRecibosList().isEmpty()){
            reciboBn = currentBulk.getSbRecibosList().get(0);
            strNroSecuencia = "<b>" + reciboBn.getNroSecuencia() + "</b>";
            strFechaMovimiento = "<b>" + formatoFechaDdMmYyyy(reciboBn.getFechaMovimiento()) + "</b>";
            strImporte = "<b>" + reciboBn.getImporte() + "</b>";
        }
        if(currentBulk.getEppGteDepositoList() != null && !currentBulk.getEppGteDepositoList().isEmpty()){
            for(EppGteDeposito deposito : currentBulk.getEppGteDepositoList() ){
                if(deposito.getActivo() == 1){
                    depositoTemp = deposito;
                    break;
                }
            }
            if(depositoTemp != null){
                strNroSecuencia = "<b>" + depositoTemp.getNroDeposito() + "</b>";                
                strFechaMovimiento = "<b>" + formatoFechaDdMmYyyy(depositoTemp.getFechaDeposito()) + "</b>";
                strImporte = "<b>" + depositoTemp.getMontoDeposito() + "</b>";
                
                nroDeposito = depositoTemp.getNroDeposito();
                fechaDeposito = depositoTemp.getFechaDeposito();
                montoDeposito = depositoTemp.getMontoDeposito();
                depositoAdjunto = depositoTemp.getImagenDeposito();
                depositoAdjuntoTemp = depositoTemp.getImagenDeposito();
                
            }
        }
        ///
        
        //empresa = ejbPersonaFacade.obtenerEmpresaXRuc(JsfUtil.getLoggedUser().getNumDoc());
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        lstRecibosBnUnq = new ArrayList();
        if (reciboBn != null) {
            lstRecibosBnUnq.add(reciboBn);
            reciboBnTemp = ejbRecibosFacade.buscarReciboXId(reciboBn.getId());
        }

        tipoTransporte = currentBulk.getTipoTransporte();
        //fechaLlegada = currentBulk.getFechaLlegada();
        //fechaSalida = currentBulk.getFechaSalida();
        //fechaFactura = currentBulk.getFechaFactura();
        empresaTransporte = currentBulk.getEmpresaTranspId();
        cargarRazonSocOrigenDestinoBulk(currentBulk);

        for (EppDocumento d : currentBulk.getEppDocumentoList()) {
            if (d.getTipoId().getCodProg().equals("TP_DOC_RMTC") || d.getTipoId().getCodProg().equals("TP_DOC_CMTC")) {
                docmtc = d;
                tipodocumento = d.getTipoId();
            }
        }
        lstDocumentosTemp = currentBulk.getEppDocumentoList();

        obtenerTipoGTECurrentBulk(true, currentBulk);
        
        setBloqueaSelectOrigen(false);
        setBloqueaSelectDestino(false);
        setBloqueaBotonBuscarDestino(false);
        setBloqueaBotonBuscarOrigen(false);

        flgCustodio = currentBulk.getCustodia() == 1;
        borrarValoresProducto();
        lstEliminadosExpSolic = null;
        lstEliminadosLicencia = null;
        lstEliminadosDocumento = null;
        setDisableResolucion(true);
        setDisableReciboBn(true);

        if (currentBulk.getEmpresaTranspId() == null) {
            setDisableEmpTransporte(false);
        } else {
            setDisableEmpTransporte(true);
        }

        return "/aplicacion/eppRegistroGuiaTransito/Edit";
    }

    /**
     * METODO PARA VISUALIZAR LA GUIA DE TRANSITO DEL ADMINISTRADO
     *
     * @return
     */
    public String mostrarVer() {
        current = new EppRegistroGuiaTransito();
        current = (EppRegistroGuiaTransito) resultados.getRowData();
        
        if(current.getRegistroId().getSbRecibosList() != null && !current.getRegistroId().getSbRecibosList().isEmpty()){
            reciboBn = current.getRegistroId().getSbRecibosList().get(0);    
            strNroSecuencia = "<b>" + reciboBn.getNroSecuencia() + "</b>";
            strFechaMovimiento = "<b>" + formatoFechaDdMmYyyy(reciboBn.getFechaMovimiento()) + "</b>";
            strImporte = "<b>" + reciboBn.getImporte() + "</b>";
        }

        //empresa = ejbPersonaFacade.obtenerEmpresaXRuc(JsfUtil.getLoggedUser().getNumDoc());
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        lstRecibosBnUnq = new ArrayList();
        lstRecibosBnUnq.add(reciboBn);

        tipoTransporte = current.getTipoTransporte();
        fechaLlegada = current.getFechaLlegada();
        //fechaSalida = current.getFechaSalida();
        //fechaFactura = current.getFechaFactura();
        empresaTransporte = current.getEmpresaTranspId();
        cargarRazonSocOrigenDestino(current);

        for (EppDocumento d : current.getRegistroId().getEppDocumentoList()) {
            if (d.getTipoId().getCodProg().equals("TP_DOC_RMTC") || d.getTipoId().getCodProg().equals("TP_DOC_CMTC")) {
                docmtc = d;
                tipodocumento = d.getTipoId();
            }
        }

        obtenerTipoGTECurrent(true);

        setBloqueaSelectOrigen(false);
        setBloqueaSelectDestino(false);
        setBloqueaBotonBuscarDestino(false);
        setBloqueaBotonBuscarOrigen(false);

        flgCustodio = current.getCustodia() == 1;
        borrarValoresProducto();
        lstEliminadosExpSolic = null;
        lstEliminadosLicencia = null;
        lstEliminadosDocumento = null;
        return "/aplicacion/eppRegistroGuiaTransito/View";
    }

    public String mostrarVerBulk() {
        currentBulk = new EppGteRegistro();
        currentBulk = (EppGteRegistro) resultadosGteReg.getRowData();
        lstDocumentosTemp = null;
        llenarTiposGTE();
        
        if(!cargarDatos()){
            return null;
        }
        
        if(currentBulk.getSbRecibosList() != null && !currentBulk.getSbRecibosList().isEmpty()){
            reciboBn = currentBulk.getSbRecibosList().get(0);
            strNroSecuencia = "<b>" + reciboBn.getNroSecuencia() + "</b>";
            strFechaMovimiento = "<b>" + formatoFechaDdMmYyyy(reciboBn.getFechaMovimiento()) + "</b>";
            strImporte = "<b>" + reciboBn.getImporte() + "</b>";
        }
        if(currentBulk.getEppGteDepositoList() != null && !currentBulk.getEppGteDepositoList().isEmpty()){
            for(EppGteDeposito deposito : currentBulk.getEppGteDepositoList() ){
                if(deposito.getActivo() == 1){
                    depositoTemp = deposito;
                    break;
                }
            }
            if(depositoTemp != null){
                strNroSecuencia = "<b>" + depositoTemp.getNroDeposito() + "</b>";
                strFechaMovimiento = "<b>" + formatoFechaDdMmYyyy(depositoTemp.getFechaDeposito()) + "</b>";
                strImporte = "<b>" + depositoTemp.getMontoDeposito() + "</b>";
                depositoAdjunto = depositoTemp.getImagenDeposito();
                depositoAdjuntoTemp = depositoTemp.getImagenDeposito();
            }
        }


        //empresa = ejbPersonaFacade.obtenerEmpresaXRuc(JsfUtil.getLoggedUser().getNumDoc());
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        lstRecibosBnUnq = new ArrayList();
        lstRecibosBnUnq.add(reciboBn);

        tipoTransporte = currentBulk.getTipoTransporte();
        //fechaLlegada = currentBulk.getFechaLlegada();
        //fechaSalida = currentBulk.getFechaSalida();
        //fechaFactura = currentBulk.getFechaFactura();
        empresaTransporte = currentBulk.getEmpresaTranspId();
        cargarRazonSocOrigenDestinoBulk(currentBulk);

        for (EppDocumento d : currentBulk.getEppDocumentoList()) {
            if (d.getTipoId().getCodProg().equals("TP_DOC_RMTC") || d.getTipoId().getCodProg().equals("TP_DOC_CMTC")) {
                docmtc = d;
                tipodocumento = d.getTipoId();
            }
        }
        lstDocumentosTemp = currentBulk.getEppDocumentoList();
        
        obtenerTipoGTECurrentBulk(true, currentBulk);

        setBloqueaSelectOrigen(false);
        setBloqueaSelectDestino(false);
        setBloqueaBotonBuscarDestino(false);
        setBloqueaBotonBuscarOrigen(false);

        flgCustodio = currentBulk.getCustodia() == 1;
        borrarValoresProducto();
        lstEliminadosExpSolic = null;
        lstEliminadosLicencia = null;
        lstEliminadosDocumento = null;
        return "/aplicacion/eppRegistroGuiaTransito/ViewBulk";
    }

    /**
     * METODO PARA REALIZAR LA BUSQUEDA EN LA BANDEJA DEL ADMINISTRADO
     *
     * @param tipob
     */
    public void buscarBandeja(String tipob) {
        String gtes = "";
        resultados = null;
        resultadosGteReg = null;
        resultadosGuia = null;
        resultadosGuiaGte = null;
        if (validaBusquedaBandejaUsuario()) {
            if (tipob.equals("2")) {
                tipoEstadoBandeja = ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0);
            }
            if (tipob.equals("3")) {
                gtes = gteSinTransf;
            }else{
                gtes = gteConTransf;
            }
            if (tipob.equals("1")) {
                gtes = "";
                for(Map nmap : tiposGTEDisponibles){
                    if(gtes.isEmpty()){
                        gtes = "'" + nmap.get("codProg") + "'";
                    }else{
                        gtes += ", '" + nmap.get("codProg") + "'";
                    }
                }
            }
            if (tipoBusqueda != null) {

                switch (tipoBusqueda) {
                    case "1"://expediente
                        resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuario(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro, null, null, null, null, gtes));
                        break;
                    case "2"://nro. guia
                        resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuario(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, filtro, null, null, null, gtes));
                        break;
                    case "3"://fecha emision
                        resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuario(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, fechaIni, null, null, gtes));
                        break;
                    case "4"://nro rg
                        if (tipoEstadoBandeja != null) {
                            if (!tipoEstadoBandeja.getCodProg().equals("TP_REGEV_FIN")) {
                                resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuarioBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro,gtes, null));
                            } else {
                                resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuario(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, filtro, null, gtes));
                            }
                        } else {
                            resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuario(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, filtro, null, gtes));
                        }
                        break;
                    case "5"://nro comprobante
                        resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuario(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, null, filtro, gtes));
                        break;
                    case "6"://fecha de solicitud
                        resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuarioBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro,gtes, fechaIni));
                        break;
                    default:
                        resultados = null;
                        break;
                }
            } else {
                String x = "";

                if (tipoEstadoBandeja != null) {
                    switch (tipoEstadoBandeja.getCodProg()) {
                        case "TP_REGEV_CRE":
                            resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuarioBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro, gtes, null));
                            break;
                        case "TP_REGEV_ANU":
                            resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuarioBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro, gtes, null));
                            break;
                        case "TP_REGEV_TRAN":
                            resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuarioBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro, gtes, null));
                            break;
                        default:
                            resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoUsuario(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, null, null, gtes));
                            break;
                    }
                } else {
                    JsfUtil.mensajeAdvertencia("Debe ingresar algún criterio para la búsqueda.");
                }
            }
        }
    }

    /**
     * METODO PARA VALIDAR LA BUSQUEDA EN LA BANDEJA DEL ADMINISTRADO
     *
     * @return
     */
    private boolean validaBusquedaBandejaUsuario() {
        boolean valida = true;

        if (tipoBusqueda != null) {
            if (tipoBusqueda.equals("3")) {
                if (fechaIni == null) {
                    valida = false;
                    JsfUtil.mensajeAdvertencia("Debe ingresar la fecha de búsqueda");
                }
            }

            if (tipoBusqueda.equals("1") || tipoBusqueda.equals("2") || tipoBusqueda.equals("4")) {
                if (filtro == null || "".equals(filtro)) {
                    valida = false;
                    JsfUtil.mensajeAdvertencia("Debe ingresar el texto para la búsqueda");
                }
            }
        }

        return valida;
    }

    /**
     * METODO PARA MOSTRAR LOS DATOS DEL ORIGEN O DESTINO EN LA GUIA DE TRANSITO
     * DEL ADMINISTRADO
     *
     * @param pRgt
     */
    public void cargarRazonSocOrigenDestino(EppRegistroGuiaTransito pRgt) {
        //ORIGEN
        if (pRgt.getOrigenAlmacen() != null) {
            //almacen
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_ALM").get(0);
            nombreEmpresaOrigen = "" + pRgt.getOrigenAlmacen().getRazonSocial() + ((pRgt.getOrigenAlmacen().getDireccion() != null)?(", "+pRgt.getOrigenAlmacen().getDireccion()):"");
            //if (flgCopiaGuia) {
            //almacen = pRgt.getOrigenAlmacen();
            current.setOrigenAlmacen(pRgt.getOrigenAlmacen());
            //}
        }
        if (pRgt.getOrigenPolvorin() != null) {
            //polvorin
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);            
            nombreEmpresaOrigen = "" + pRgt.getOrigenPolvorin().getDescripcion() + ((pRgt.getOrigenPolvorin().getDireccion() != null)?(", "+pRgt.getOrigenPolvorin().getDireccion()):"");
            
            //if (flgCopiaGuia) {
            //polvorin = pRgt.getOrigenPolvorin();
            current.setOrigenPolvorin(pRgt.getOrigenPolvorin());
            current.setOrigenAlquiler(pRgt.getOrigenAlquiler());
            //}
        }
        if (pRgt.getOrigenAlmacenAduana() != null) {
            //ALMACEN ADUANERO
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_ALM").get(0);
            nombreEmpresaOrigen = "" + pRgt.getOrigenAlmacenAduana().getRazonSocial() + ((pRgt.getOrigenAlmacenAduana().getDireccion() != null)?(", "+pRgt.getOrigenAlmacenAduana().getDireccion()):"");
            //if (flgCopiaGuia) {
            //almAduanero = pRgt.getOrigenAlmacenAduana();
            current.setOrigenAlmacenAduana(pRgt.getOrigenAlmacenAduana());
            //}
        }
        if (pRgt.getPuertoAduaneroOrigen() != null) {
            //PUERTO ADUANERO
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_PUE").get(0);
            nombreEmpresaOrigen = "" + pRgt.getPuertoAduaneroOrigen().getNombre() + ((pRgt.getPuertoAduaneroOrigen().getDireccion() != null)?(", "+pRgt.getPuertoAduaneroOrigen().getDireccion()):"");
            //if (flgCopiaGuia) {
            //ptoAduanero = pRgt.getPuertoAduaneroOrigen();
            current.setPuertoAduaneroOrigen(pRgt.getPuertoAduaneroOrigen());
            //}
        }
        if (pRgt.getLugarUsoOrigen() != null) {
            //LUGAR USO
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_LUSO").get(0);
            nombreEmpresaOrigen = "" + ejbLugarUsoFacade.find(pRgt.getLugarUsoOrigen().getId()).getNombre();
            //if (flgCopiaGuia) {
            current.setLugarUsoOrigen(pRgt.getLugarUsoOrigen());
            setMostrarLugarUsoUbigeoOrigen(true);
            current.setLugarUsoUbigeoOrigen(pRgt.getLugarUsoUbigeoOrigen());
            lugarUsoUbigeoOrigen = pRgt.getLugarUsoUbigeoOrigen();
            lstLugarUsoUbigeo = new ArrayList();
//            lstLugarUsoUbigeo.add(pRgt.getLugarUsoUbigeoDestino());
            lstLugarUsoUbigeo = pRgt.getLugarUsoOrigen().getEppLugarUsoUbigeoList();
            setDisableLugarUsoUbigeoOrigen(false);
            //}
        }
        if (pRgt.getOrigenFabrica() != null) {
            //fabrica
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_FAB").get(0);
            current.setOrigenFabrica(pRgt.getOrigenFabrica());
            current.setOrigenAlquiler(pRgt.getOrigenAlquiler());
            if(current.getOrigenAlquiler() != null ){
                nombreEmpresaOrigen = pRgt.getOrigenAlquiler().getArrendatariaId().getRznSocial() + ((pRgt.getOrigenFabrica().getDireccion() != null)?(", "+pRgt.getOrigenFabrica().getDireccion()):"");
            }else{
                nombreEmpresaOrigen = "" + pRgt.getOrigenFabrica().getPersonaId().getRznSocial() + ((pRgt.getOrigenFabrica().getDireccion() != null)?(", "+pRgt.getOrigenFabrica().getDireccion()):"");
            }
        }
        //DESTINO
        if (pRgt.getDestinoAlmacen() != null) {
            //almacen
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_ALM").get(0);
            nombreEmpresaDestino = "" + pRgt.getDestinoAlmacen().getRazonSocial() + ((pRgt.getDestinoAlmacen().getDireccion() != null)?(", "+pRgt.getDestinoAlmacen().getDireccion()):"");
            //if (flgCopiaGuia) {
            //almacen = pRgt.getDestinoAlmacen();
            current.setDestinoAlmacen(pRgt.getDestinoAlmacen());
            //}
        }
        if (pRgt.getDestinoPolvorin() != null) {
            //polvorin
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
            nombreEmpresaDestino = "" + pRgt.getDestinoPolvorin().getDescripcion()+ ((pRgt.getDestinoPolvorin().getDireccion() != null)?(", "+pRgt.getDestinoPolvorin().getDireccion()):"");
            //if (flgCopiaGuia) {
            //polvorin = pRgt.getDestinoPolvorin();
            current.setDestinoPolvorin(pRgt.getDestinoPolvorin());
            current.setDestinoAlquiler(pRgt.getDestinoAlquiler());
            //}
        }
        if (pRgt.getDestinoAlmacenAduana() != null) {
            //polvorin
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_ALM").get(0);
            nombreEmpresaDestino = "" + pRgt.getDestinoAlmacenAduana().getRazonSocial() + ((pRgt.getDestinoAlmacenAduana().getDireccion() != null)?(", "+pRgt.getDestinoAlmacenAduana().getDireccion()):"");
            //if (flgCopiaGuia) {
            current.setDestinoAlmacenAduana(pRgt.getDestinoAlmacenAduana());
            //}
        }
        if (pRgt.getPuertoAduaneroDestino() != null) {
            //PUERTO ADUANERO
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_PUE").get(0);
            nombreEmpresaDestino = "" + pRgt.getPuertoAduaneroDestino().getNombre() + ((pRgt.getPuertoAduaneroDestino().getDireccion() != null)?(", "+pRgt.getPuertoAduaneroDestino().getDireccion()):"");
            //if (flgCopiaGuia) {
            current.setPuertoAduaneroDestino(pRgt.getPuertoAduaneroDestino());
            //}
        }
        if (pRgt.getLugarUsoDestino() != null) {
            //LUGAR USO
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_LUSO").get(0);
            nombreEmpresaDestino = "" + ejbLugarUsoFacade.find(pRgt.getLugarUsoDestino().getId()).getNombre();
            //if (flgCopiaGuia) {
            current.setLugarUsoDestino(pRgt.getLugarUsoDestino());
            setMostrarLugarUsoUbigeoDestino(true);
            current.setLugarUsoUbigeoDestino(pRgt.getLugarUsoUbigeoDestino());
            lugarUsoUbigeoDestino = pRgt.getLugarUsoUbigeoDestino();
            lstLugarUsoUbigeo = new ArrayList();
//            lstLugarUsoUbigeo.add(pRgt.getLugarUsoUbigeoDestino());
            lstLugarUsoUbigeo = pRgt.getLugarUsoDestino().getEppLugarUsoUbigeoList();
            setDisableLugarUsoUbigeoDestino(false);
            //}||tipoOperacion.getCodProg().equals("TP_OPE_REN")
        }
        if (pRgt.getDestinoFabrica() != null) {
            //fabrica
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_FAB").get(0);
            current.setDestinoFabrica(pRgt.getDestinoFabrica());
            current.setDestinoAlquiler(pRgt.getDestinoAlquiler());
            if(current.getDestinoAlquiler() != null ){
                nombreEmpresaDestino = pRgt.getDestinoAlquiler().getArrendatariaId().getRznSocial() + ((pRgt.getDestinoFabrica().getDireccion() != null)?(", "+pRgt.getDestinoFabrica().getDireccion()):"");
            }else{
                nombreEmpresaDestino = "" + pRgt.getDestinoFabrica().getPersonaId().getRznSocial() + ((pRgt.getDestinoFabrica().getDireccion() != null)?(", "+pRgt.getDestinoFabrica().getDireccion()):"");
            }
        }
    }

    public void cargarRazonSocOrigenDestinoBulk(EppGteRegistro pRgt) {
        //ORIGEN
        if (pRgt.getOrigenAlmacen() != null) {
            //almacen
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_ALM").get(0);
            nombreEmpresaOrigen = "" + pRgt.getOrigenAlmacen().getRazonSocial() + ((pRgt.getOrigenAlmacen().getDireccion() != null)?(", "+pRgt.getOrigenAlmacen().getDireccion()):"");
            //if (flgCopiaGuia) {
            //almacen = pRgt.getOrigenAlmacen();
            currentBulk.setOrigenAlmacen(pRgt.getOrigenAlmacen());
            //}
        }
        if (pRgt.getOrigenPolvorin() != null) {
            //polvorin
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
            nombreEmpresaOrigen = "" + pRgt.getOrigenPolvorin().getDescripcion() + ((pRgt.getOrigenPolvorin().getDireccion() != null)?(", "+pRgt.getOrigenPolvorin().getDireccion()):"");
            //if (flgCopiaGuia) {
            //polvorin = pRgt.getOrigenPolvorin();
            currentBulk.setOrigenPolvorin(pRgt.getOrigenPolvorin());
            currentBulk.setOrigenAlquilerId(pRgt.getOrigenAlquilerId());
            //}
        }
        if (pRgt.getOrigenAlmacenAduana() != null) {
            //ALMACEN ADUANERO
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_ALM").get(0);
            nombreEmpresaOrigen = "" + pRgt.getOrigenAlmacenAduana().getRazonSocial() + ((pRgt.getOrigenAlmacenAduana().getDireccion() != null)?(", "+pRgt.getOrigenAlmacenAduana().getDireccion()):"");
            //if (flgCopiaGuia) {
            //almAduanero = pRgt.getOrigenAlmacenAduana();
            currentBulk.setOrigenAlmacenAduana(pRgt.getOrigenAlmacenAduana());
            //}
        }
        if (pRgt.getPuertoAduaneroOrigen() != null) {
            //PUERTO ADUANERO
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_PUE").get(0);
            nombreEmpresaOrigen = "" + pRgt.getPuertoAduaneroOrigen().getNombre() + ((pRgt.getPuertoAduaneroOrigen().getDireccion() != null)?(", "+pRgt.getPuertoAduaneroOrigen().getDireccion()):"");
            //if (flgCopiaGuia) {
            //ptoAduanero = pRgt.getPuertoAduaneroOrigen();
            currentBulk.setPuertoAduaneroOrigen(pRgt.getPuertoAduaneroOrigen());
            //}
        }
        if (pRgt.getLugarUsoOrigen() != null) {
            //LUGAR USO
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_LUSO").get(0);
            nombreEmpresaOrigen = "" + ejbLugarUsoFacade.find(pRgt.getLugarUsoOrigen().getId()).getNombre();
            //if (flgCopiaGuia) {
            currentBulk.setLugarUsoOrigen(pRgt.getLugarUsoOrigen());
            setMostrarLugarUsoUbigeoOrigen(true);
            currentBulk.setLugarUsoUbigeoOrigen(pRgt.getLugarUsoUbigeoOrigen());
            lugarUsoUbigeoOrigen = pRgt.getLugarUsoUbigeoOrigen();
            lstLugarUsoUbigeo = new ArrayList();
//            lstLugarUsoUbigeo.add(pRgt.getLugarUsoUbigeoDestino());
            lstLugarUsoUbigeo = pRgt.getLugarUsoOrigen().getEppLugarUsoUbigeoList();
            setDisableLugarUsoUbigeoOrigen(false);
            //}
        }
        if (pRgt.getOrigenFabrica() != null) {
            //fabrica
            tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_FAB").get(0);
            currentBulk.setOrigenFabrica(pRgt.getOrigenFabrica());
            currentBulk.setOrigenAlquilerId(pRgt.getOrigenAlquilerId());
            if(currentBulk.getOrigenAlquilerId()!= null ){
                nombreEmpresaOrigen = pRgt.getOrigenAlquilerId().getArrendatariaId().getRznSocial() + ((pRgt.getOrigenFabrica().getDireccion() != null)?(", "+pRgt.getOrigenFabrica().getDireccion()):"");
            }else{
                nombreEmpresaOrigen = "" + pRgt.getOrigenFabrica().getPersonaId().getRznSocial() + ((pRgt.getOrigenFabrica().getDireccion() != null)?(", "+pRgt.getOrigenFabrica().getDireccion()):"");
            }
        }
        //DESTINO
        if (pRgt.getDestinoAlmacen() != null) {
            //almacen
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_ALM").get(0);
            nombreEmpresaDestino = "" + pRgt.getDestinoAlmacen().getRazonSocial() + ((pRgt.getDestinoAlmacen().getDireccion() != null)?(", "+pRgt.getDestinoAlmacen().getDireccion()):"");
            //if (flgCopiaGuia) {
            //almacen = pRgt.getDestinoAlmacen();
            currentBulk.setDestinoAlmacen(pRgt.getDestinoAlmacen());
            //}
        }
        if (pRgt.getDestinoPolvorin() != null) {
            //polvorin
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
            nombreEmpresaDestino = "" + pRgt.getDestinoPolvorin().getDescripcion() + ((pRgt.getDestinoPolvorin().getDireccion() != null)?(", "+pRgt.getDestinoPolvorin().getDireccion()):"");
            //if (flgCopiaGuia) {
            //polvorin = pRgt.getDestinoPolvorin();
            currentBulk.setDestinoPolvorin(pRgt.getDestinoPolvorin());
            currentBulk.setDestinoAlquilerId(pRgt.getDestinoAlquilerId());
            //}
        }
        if (pRgt.getDestinoAlmacenAduana() != null) {
            //polvorin
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_ALM").get(0);
            nombreEmpresaDestino = "" + pRgt.getDestinoAlmacenAduana().getRazonSocial() + ((pRgt.getDestinoAlmacenAduana().getDireccion() != null)?(", "+pRgt.getDestinoAlmacenAduana().getDireccion()):"");
            //if (flgCopiaGuia) {
            currentBulk.setDestinoAlmacenAduana(pRgt.getDestinoAlmacenAduana());
            //}
        }
        if (pRgt.getPuertoAduaneroDestino() != null) {
            //PUERTO ADUANERO
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_PUE").get(0);
            nombreEmpresaDestino = "" + pRgt.getPuertoAduaneroDestino().getNombre() + ((pRgt.getPuertoAduaneroDestino().getDireccion() != null)?(", "+pRgt.getPuertoAduaneroDestino().getDireccion()):"");
            //if (flgCopiaGuia) {
            currentBulk.setPuertoAduaneroDestino(pRgt.getPuertoAduaneroDestino());
            //}
        }
        if (pRgt.getLugarUsoDestino() != null) {
            //LUGAR USO
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ALMEMDE_LUSO").get(0);
            nombreEmpresaDestino = "" + ejbLugarUsoFacade.find(pRgt.getLugarUsoDestino().getId()).getNombre();
            //if (flgCopiaGuia) {
            currentBulk.setLugarUsoDestino(pRgt.getLugarUsoDestino());
            setMostrarLugarUsoUbigeoDestino(true);
            currentBulk.setLugarUsoUbigeoDestino(pRgt.getLugarUsoUbigeoDestino());
            lugarUsoUbigeoDestino = pRgt.getLugarUsoUbigeoDestino();
            lstLugarUsoUbigeo = new ArrayList();
//            lstLugarUsoUbigeo.add(pRgt.getLugarUsoUbigeoDestino());
            lstLugarUsoUbigeo = pRgt.getLugarUsoDestino().getEppLugarUsoUbigeoList();
            setDisableLugarUsoUbigeoDestino(false);
            //}||tipoOperacion.getCodProg().equals("TP_OPE_REN")
        }
        if (pRgt.getDestinoFabrica() != null) {
            //fabrica
            tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_FAB").get(0);
            currentBulk.setDestinoFabrica(pRgt.getDestinoFabrica());
            
            currentBulk.setDestinoAlquilerId(pRgt.getDestinoAlquilerId());
            if(currentBulk.getDestinoAlquilerId()!= null ){
                nombreEmpresaDestino = pRgt.getDestinoAlquilerId().getArrendatariaId().getRznSocial() + ((pRgt.getDestinoFabrica().getDireccion() != null)?(", "+pRgt.getDestinoFabrica().getDireccion()):"");
            }else{
                nombreEmpresaDestino = "" + pRgt.getDestinoFabrica().getPersonaId().getRznSocial() + ((pRgt.getDestinoFabrica().getDireccion() != null)?(", "+pRgt.getDestinoFabrica().getDireccion()):"");
            }
        }
    }

    /**
     * METODO PARA MOSTRAR LA RAZON SOCIAL DEL ORIGEN SELECCIONADO
     *
     * @param rgt
     * @param booSubStr
     * @return
     */
    public String mostrarNombreOrigen(EppRegistroGuiaTransito rgt, boolean booSubStr) {
        String razonSocial = "";
        if (rgt.getOrigenAlmacen() != null) {
            razonSocial = "" + rgt.getOrigenAlmacen().getRazonSocial() + " - " + rgt.getOrigenAlmacen().getDireccion();
        }
        if (rgt.getOrigenPolvorin() != null) {
            razonSocial = "" + rgt.getOrigenPolvorin().getDescripcion() + " - " + rgt.getOrigenPolvorin().getDireccion();
        }
        if (rgt.getOrigenAlmacenAduana() != null) {
            razonSocial = "" + rgt.getOrigenAlmacenAduana().getRazonSocial() + " - " + rgt.getOrigenAlmacenAduana().getDireccion();
        }
        if (rgt.getPuertoAduaneroOrigen() != null) {
            razonSocial = "" + rgt.getPuertoAduaneroOrigen().getNombre() + " - " + rgt.getPuertoAduaneroOrigen().getDireccion();
        }
        if (rgt.getLugarUsoOrigen() != null) {
            razonSocial = "" + rgt.getLugarUsoOrigen().getNombre();
        }
        if (rgt.getOrigenFabrica() != null) {
            if(rgt.getOrigenAlquiler() != null){
                razonSocial = rgt.getOrigenAlquiler().getArrendatariaId().getRznSocial() + " - " + rgt.getOrigenFabrica().getDireccion();
            }else{
                razonSocial = "" + rgt.getOrigenFabrica().getPersonaId().getRznSocial() + " - " + rgt.getOrigenFabrica().getDireccion();
            }
        }

        if (booSubStr) {
            if (razonSocial != null && !"".equals(razonSocial)) {
                if (razonSocial.length() > 130) {
                    razonSocial = razonSocial.substring(0, 130) + "... ";
                } else {
                    razonSocial = razonSocial;//.substring(0, razonSocial.length() - 1) + "... ";
                }
            }
        }

        return razonSocial;
    }

    /**
     * METODO PARA MOSTRAR LA RAZON SOCIAL DEL DESTINO SELECCIONADO
     *
     * @param rgt
     * @param booSubStr
     * @return
     */
    public String mostrarNombreDestino(EppRegistroGuiaTransito rgt, boolean booSubStr) {
        String razonSocial = "";
        if (rgt.getDestinoAlmacen() != null) {
            razonSocial = "" + rgt.getDestinoAlmacen().getRazonSocial() + " - " + rgt.getDestinoAlmacen().getDireccion();
        }
        if (rgt.getDestinoPolvorin() != null) {
            razonSocial = "" + rgt.getDestinoPolvorin().getDescripcion() + " - " + rgt.getDestinoPolvorin().getDireccion();
        }
        if (rgt.getDestinoAlmacenAduana() != null) {
            razonSocial = "" + rgt.getDestinoAlmacenAduana().getRazonSocial() + " - " + rgt.getDestinoAlmacenAduana().getDireccion();
        }
        if (rgt.getPuertoAduaneroDestino() != null) {
            razonSocial = "" + rgt.getPuertoAduaneroDestino().getNombre() + " - " + rgt.getPuertoAduaneroDestino().getDireccion();
        }
        if (rgt.getLugarUsoDestino() != null) {
            razonSocial = "" + ejbLugarUsoFacade.find(rgt.getLugarUsoDestino().getId()).getNombre();
        }
        if (rgt.getDestinoFabrica() != null) {
            if(rgt.getDestinoAlquiler() != null){
                razonSocial = rgt.getDestinoAlquiler().getArrendatariaId().getRznSocial() + " - " + rgt.getDestinoFabrica().getDireccion();
            }else{
                razonSocial = "" + rgt.getDestinoFabrica().getPersonaId().getRznSocial() + " - " + rgt.getDestinoFabrica().getDireccion();
            }
        }
        if (booSubStr) {
            if (razonSocial != null && !"".equals(razonSocial)) {
                if (razonSocial.length() > 130) {
                    razonSocial = razonSocial.substring(0, 130) + "... ";
                } else {
                    razonSocial = razonSocial;//.substring(0, razonSocial.length() - 1) + "... ";
                }
            }
        }

        return razonSocial;
    }

    /**
     * METODO QUE AUTORIZA LA VISUALIZACION DEL BOTON EDITAR EN LA BANDEJA
     *
     * @param mbe
     * @return
     */
    public boolean mostrarBtnEditar(EppRegistroGuiaTransito mbe) {
        return mbe.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_CRE");
    }

    public boolean mostrarBtnEditarBulk(TipoExplosivoGt mbe) {
        return mbe.getCodProg().equals("TP_REGEV_CRE");
    }

    public boolean mostrarBtnCopiar(EppRegistroGuiaTransito mbe) {
        return mbe.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_CRE");
    }

    public boolean mostrarBtnCopiarBulk(EppGteRegistro mbe) {
        return mbe.getEstado().getCodProg().equals("TP_REGEV_CRE") && 
               (mbe.getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI1") || mbe.getTipoTramite().getCodProg().equals("TP_RGUIA_COMFAB") ); // Solo se permite copiar guias creadas y del tipo 1 y 6
    }
    
    public boolean mostrarBtnCopiarBulkSinTransfer(EppGteRegistro mbe) {
        return mbe.getEstado().getCodProg().equals("TP_REGEV_CRE") &&
               (mbe.getTipoTramite().getCodProg().equals("TP_RGUIA_INTER") || mbe.getTipoTramite().getCodProg().equals("TP_RGUIA_PLAPLA") || mbe.getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI2")  );
    }

    public boolean mostrarBtnPdfBulk(TipoExplosivoGt mbe) {
        return mbe.getCodProg().equals("TP_REGEV_TRAN");
    }
    
    public boolean mostrarBtnPdfBulkSinTransfer(TipoExplosivoGt mbe) {
        return mbe.getCodProg().equals("TP_REGEV_CRE");
    }
    
    public boolean mostrarBtnPdfBulkMap(String estado) {
        return estado.equals("TP_REGEV_TRAN");
    }

    public boolean mostrarBtnDatosAdicionalesGt(EppRegistroGuiaTransito mbe) {
        boolean vda = true;
        if (mbe.getFechaLlegada() != null) {
            vda = false;
        }

        return vda && mbe.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_FIN");
    }

    public boolean mostrarBtnBorrarGt(EppRegistroGuiaTransito mbe) {
        return mbe.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_CRE") || mbe.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_TRAN");
    }

    public boolean mostrarBtnBorrarGtBulk(TipoExplosivoGt mbe) {
        return mbe.getCodProg().equals("TP_REGEV_CRE") || mbe.getCodProg().equals("TP_REGEV_TRAN");
    }

    /**
     * METODO QUE AUTORIZA LA VISUALIZACION DEL BOTON REPORTE EN LA BANDEJA
     *
     * @param mbr
     * @return
     */
    public boolean mostrarBtnReporte(EppRegistroGuiaTransito mbr) {
        return (mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_TRAN")
                || mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_OBS")
                || mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_VIS")
                || mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_FIR")
                || mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_FIN"))
                && mbr.getRegistroId().getEppResolucion() != null;
    }
    
    public boolean mostrarBtnReporteMap(String estado, String resolucion) {
        return (estado.equals("TP_REGEV_TRAN")
                || estado.equals("TP_REGEV_OBS")
                || estado.equals("TP_REGEV_VIS")
                || estado.equals("TP_REGEV_FIR")
                || estado.equals("TP_REGEV_FIN"))
                && resolucion != null;
    }

    public boolean mostrarBtnAnular(EppRegistroGuiaTransito mbr) {
        return mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_TRAN");
    }

    public boolean mostrarBtnAnularBulk(EppGteRegistro mbr) {
        return mbr.getEstado().getCodProg().equals("TP_REGEV_TRAN");
    }
    
    public boolean mostrarBtnAnularBulkMap(Map reg) {
        return reg.get("ESTADO_CODPROG").equals("TP_REGEV_TRAN");
    }

    /**
     * METODO QUE AUTORIZA LA VISUALIZACION DEL BOTON REPORTE PARA LA BANDEJA
     * DEL USUARIO
     *
     * @param mbr
     * @return
     */
    public boolean mostrarBtnReporteUsuario(EppRegistroGuiaTransito mbr) {
        return (mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_TRAN")
                || mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_OBS")
                || mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_VIS")
                || mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_FIR")
                || mbr.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_CRE"));
    }

    /**
     * METODO PARA VISUALIZAR EL REPORTE EN PDF
     *
     * @param pReg
     * @return
     */
    public StreamedContent reporte(EppRegistroGuiaTransito pReg) {
        try {
            boolean muestrarpt = false;
            if (pReg.getRegistroId().getEppResolucion() != null) {
                if (pReg.getRegistroId().getEppResolucion().getFechaFin() != null) {
                    Date dff = pReg.getRegistroId().getEppResolucion().getFechaFin();
                    Date hoy = new Date();
                    if (dff.after(hoy)) {
                        muestrarpt = true;
                    }
                }
            }

            if (muestrarpt) {
                List<EppRegistroGuiaTransito> lp = new ArrayList();
                lp.add(pReg);
                if (pReg.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_FIN")) {
                    if (JsfUtil.buscarPdfRepositorio(pReg.getRegistroId().getId().toString(), JsfUtil.bundleGt("Documentos_pathUpload_resolucion"))) {
                        return JsfUtil.obtenerArchivo("Documentos_pathUpload_resolucion", pReg.getRegistroId().getId() + ".pdf", pReg.getRegistroId().getId() + ".pdf", "application/pdf");
                    } else {
                        subirPdf(pReg.getRegistroId());
                        return JsfUtil.obtenerArchivo("Documentos_pathUpload_resolucion", pReg.getRegistroId().getId() + ".pdf", pReg.getRegistroId().getId() + ".pdf", "application/pdf");
                    }
                } else {
                    return JsfUtil.generarReportePdf(JsfUtil.bundleGt("rutaPdfGte"), lp, parametrosPdf(pReg.getRegistroId()), "gt.pdf", null, null);
                }
            } else {
                return JsfUtil.mensajeHtml("No puede visualizar la Guía ya que se encuentra vencida.");
            }
        } catch (Exception ex) {
            return JsfUtil.errorDescarga("Error: EppRegistroGuiaTransitoController.reporte :", ex);
        }
    }
    
    public boolean validaGenerarPdfGTE(EppRegistroGuiaTransito pReg){
        boolean valida = false;
        
        if(pReg.getTipoTramite().getCodProg().equals("TP_RGUIA_PLAPLA")){ // Se permite generar PDF a guia tipo 5 que no tiene RG asociada
            valida = true;
        }
        return valida;
    }
    
    /**
     * METODO PARA VISUALIZAR EL REPORTE EN PDF
     *
     * @param id
     * @return
     */
    public StreamedContent reporteMap(Long id) {
        try {
            EppRegistroGuiaTransito pReg = eppRegistroGuiaTransitoFacade.obtenerGuiTransitoById(id);
            
            if(pReg != null){
                boolean muestrarpt = false;
                if (pReg.getRegistroId().getEppResolucion() != null) {
                    if (pReg.getRegistroId().getEppResolucion().getFechaFin() != null) {
                        Date dff = pReg.getRegistroId().getEppResolucion().getFechaFin();
                        Date hoy = new Date();
                        if (dff.after(hoy)) {
                            muestrarpt = true;
                        }
                    }
                }

                if (muestrarpt || validaGenerarPdfGTE(pReg)) {
                    List<EppRegistroGuiaTransito> lp = new ArrayList();
                    lp.add(pReg);
                    if (pReg.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_FIN")) {
                        if (JsfUtil.buscarPdfRepositorio(pReg.getRegistroId().getId().toString(), JsfUtil.bundleGt("Documentos_pathUpload_resolucion"))) {
                            return JsfUtil.obtenerArchivo("Documentos_pathUpload_resolucion", pReg.getRegistroId().getId() + ".pdf", pReg.getRegistroId().getId() + ".pdf", "application/pdf");
                        } else {
                            subirPdf(pReg.getRegistroId());
                            return JsfUtil.obtenerArchivo("Documentos_pathUpload_resolucion", pReg.getRegistroId().getId() + ".pdf", pReg.getRegistroId().getId() + ".pdf", "application/pdf");
                        }
                    } else {
                        return JsfUtil.generarReportePdf(JsfUtil.bundleGt("rutaPdfGte"), lp, parametrosPdf(pReg.getRegistroId()), "gt.pdf", null, null);
                    }
                } else {
                    return JsfUtil.mensajeHtml("No puede visualizar la Guía ya que se encuentra vencida.");
                }
            }else{
                return JsfUtil.mensajeHtml("No se encontró datos del registro de guía de tránsito");
            }
        } catch (Exception ex) {
            return JsfUtil.errorDescarga("Error: EppRegistroGuiaTransitoController.reporte :", ex);
        }
    }

    /**
     * Metodo que envia el archivo pdf hacia el repositorio donde quedará
     * guardado
     *
     * @param pReg
     * @return
     */
    private boolean subirPdf(EppRegistro pReg) {
        boolean valida = true;
        try {
            List<EppRegistroGuiaTransito> lp = new ArrayList();
            lp.add(pReg.getEppRegistroGuiaTransito());
            JsfUtil.subirPdfGte(pReg.getId().toString(), parametrosPdf(pReg), lp, JsfUtil.bundleGt("rutaPdfGte"), JsfUtil.bundleGt("Documentos_pathUpload_resolucion"));
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeError("No se pudo subir el archivo: " + ex.getMessage());
            valida = false;
        }
        return valida;
    }

    /**
     * Metodo que genera los parametros a utilizar en el reporte de la guia de
     * transito pirotecnico
     *
     * @param ppdf
     * @return
     */
    private HashMap parametrosPdf(EppRegistro ppdf) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap p = new HashMap();
        p.put("p_dir_emp", ReportUtilGuiaTransito.mostrarDireccionPersona(ppdf.getEmpresaId()));
        p.put("p_fecha_exp", ReportUtilGuiaTransito.mostrarFechaExpediente(ejbExpedienteFacade, ppdf.getNroExpediente()));
        if (ppdf.getEstado().getCodProg().equals("TP_REGEV_FIN")) {
            p.put("p_qr", JsfUtil.bundleGt("urlQr") + ppdf.getEppResolucion().getHashQr());
        } else {
            p.put("p_qr", "x");
        }
        p.put("P_IMG_DIR", ec.getRealPath("/resources/imagenes/") + "/");
        p.put("logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        if (ppdf.getEstado().getCodProg().equals("TP_REGEV_FIR") || ppdf.getEstado().getCodProg().equals("TP_REGEV_FIN")) {
            p.put("P_IMG_FIRMA", mostrarFirmaXOde());
        } else {
            p.put("P_IMG_FIRMA", null);
        }
        p.put("P_GTE_REF", eppGteRegistroFacade.nroGteAsociada(ppdf));
        p.put("P_TIPO_GT", tituloReporteGuiaTran(ppdf.getEppRegistroGuiaTransito()));
        p.put("PARRAFO2", mostrarDatosOrigen(ppdf.getEppRegistroGuiaTransito(), true));
        p.put("PARRAFO3", mostrarDatosDestino(ppdf.getEppRegistroGuiaTransito(), true));
        p.put("FECHAACTUAL", fechaGtePdf(ppdf));
        p.put("VENCIMIENTO", ReportUtilGuiaTransito.mostrarFechaString(ppdf.getEppResolucion().getFechaFin()));
        p.put("productos", listarExpSolicActivos(ppdf.getEppRegistroGuiaTransito().getEppExplosivoSolicitadoList(), ppdf));
        p.put("P_NRO_GT", ppdf.getEppResolucion() != null ? ppdf.getEppResolucion().getNumero() : "");
        p.put("P_EMPRESA_STR", mostrarClienteString(ppdf.getEmpresaId()));
        p.put("P_EMPRESA_TRANSP_STR", mostrarClienteString(ppdf.getEppRegistroGuiaTransito().getEmpresaTranspId()) == null ? "" : ("Los explosivos serán trasladados por:<br/>"
                + "EMPRESA DE TRANSPORTES: " + mostrarClienteString(ppdf.getEppRegistroGuiaTransito().getEmpresaTranspId())));
        if (ppdf.getEppResolucion() != null) {
            if (ppdf.getEppResolucion().getNumero() != null && ppdf.getEppResolucion().getHashQr() != null) {
                p.put("P_SEL_CODE", ppdf.getEppResolucion().getHashQr().substring(0, 4) + " - " + ppdf.getEppResolucion().getNumero());
            } else {
                p.put("P_SEL_CODE", "BORRADOR");
            }
        } else {
            p.put("P_SEL_CODE", "BORRADOR");
        }
        if (ppdf.getEppRegistroGuiaTransito().getResolucionId() != null) {
            if (ppdf.getEppRegistroGuiaTransito().getResolucionId().getNumero() != null) {
                p.put("P_RG_REF_GT", "REF. R.G.: <b>" + ppdf.getEppRegistroGuiaTransito().getResolucionId().getNumero() + "</b>");
            } else {
                p.put("P_RG_REF_GT", "");
            }
        } else {
            p.put("P_RG_REF_GT", "");
        }
        return p;
    }

    public List<EppExplosivoSolicitado> listarExpSolicActivos(List<EppExplosivoSolicitado> paramLst, EppRegistro rtgt) {
        List<EppExplosivoSolicitado> lstTemp = new ArrayList();
        for (EppExplosivoSolicitado p : paramLst) {
            if (p.getActivo() == 1) {                
                switch (rtgt.getEppRegistroGuiaTransito().getTipoTramite().getCodProg()) {
                    case "TP_RGUIA_INTER":
                    case "TP_RGUIA_PLAPLA":
                    case "TP_RGUIA_COMFAB":
                    case "TP_RGUIA_TRA":
                    case "TP_RGUIA_OTR":
                                p.setCantidadAutorizada(-1.0);
                                break;
                }
                lstTemp.add(p);
            }
        }
        return lstTemp;
    }

    public List<EppGteExplosivoSolicita> listarExpSolicActivosGte(List<EppGteExplosivoSolicita> paramLst) {
        List<EppGteExplosivoSolicita> lstTemp = new ArrayList();
        for (EppGteExplosivoSolicita p : paramLst) {
            if (p.getActivo() == 1) {
                lstTemp.add(p);
            }
        }
        return lstTemp;
    }

    /**
     * Metodo que muestra la fecha concatenada con la dependencia desde donde se
     * emite
     *
     * @param du
     * @return
     */
    public String fechaActualDependencia(DatosUsuario du) {
        String fecha = ReportUtilGuiaTransito.mostrarFechaString(new Date());
        return "Magdalena del Mar" + ", " + fecha;
    }
    
    public String fechaSolicitudGtePdf(EppGteRegistro reg) {
        Date fechaTemp = new Date();
        if(reg != null && reg.getFecha() != null){
            fechaTemp = reg.getFecha();
        }
        String fecha = ReportUtilGuiaTransito.mostrarFechaString(fechaTemp);
        return "Magdalena del Mar" + ", " + fecha;
    }
    
    public String fechaGtePdf(EppRegistro reg) {
        Date fechaTemp = new Date();
        if(reg != null && reg.getEppResolucion() != null){
            fechaTemp = reg.getEppResolucion().getFecha();
        }
        String fecha = ReportUtilGuiaTransito.mostrarFechaString(fechaTemp);
        return "Magdalena del Mar" + ", " + fecha;
    }

    /**
     * Metodo que muestra los datos del origen
     *
     * @param ro
     * @return
     */
    private String mostrarDatosOrigen(EppRegistroGuiaTransito ro, boolean txtprev) {
        String r = "";
        if (ro.getOrigenAlmacen() != null) {
            r = ro.getOrigenAlmacen().getRazonSocial() + ". Dirección: "
                    + ro.getOrigenAlmacen().getDireccion()
                    + ", Distrito de " + ro.getOrigenAlmacen().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getOrigenAlmacen().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getOrigenAlmacen().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getOrigenPolvorin() != null) {
            r = ro.getOrigenPolvorin().getDescripcion() + ". Dirección: "
                    + ro.getOrigenPolvorin().getDireccion()
                    + ", Distrito de " + ro.getOrigenPolvorin().getDistritoId().getNombre()
                    + ", Provincia de " + ro.getOrigenPolvorin().getDistritoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getOrigenPolvorin().getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getOrigenAlmacenAduana() != null) {
            r = ro.getOrigenAlmacenAduana().getRazonSocial() + ". Dirección: "
                    + ro.getOrigenAlmacenAduana().getDireccion()
                    + ", Distrito de " + ro.getOrigenAlmacenAduana().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getOrigenAlmacenAduana().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getOrigenAlmacenAduana().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getPuertoAduaneroOrigen() != null) {
            r = ro.getPuertoAduaneroOrigen().getNombre() + ". Dirección: "
                    + ro.getPuertoAduaneroOrigen().getDireccion()
                    + ", Distrito de " + ro.getPuertoAduaneroOrigen().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getPuertoAduaneroOrigen().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getPuertoAduaneroOrigen().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getLugarUsoOrigen() != null) {
            r = ro.getLugarUsoOrigen().getNombre() + ". Dirección: "
                    + "Distrito de " + ro.getLugarUsoUbigeoOrigen().getDistId().getNombre()
                    + ", Provincia de " + ro.getLugarUsoUbigeoOrigen().getDistId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getLugarUsoUbigeoOrigen().getDistId().getProvinciaId().getDepartamentoId().getNombre();
        }else if (ro.getOrigenFabrica() != null) {
            if(ro.getOrigenAlquiler() != null ){
                r = ro.getOrigenAlquiler().getArrendatariaId().getRznSocial();
            }else{
                r = ro.getOrigenFabrica().getPersonaId().getRznSocial();
            }
            r = r + ". Dirección: "
                    + ro.getOrigenFabrica().getDireccion()
                    + ", Distrito de " + ro.getOrigenFabrica().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getOrigenFabrica().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getOrigenFabrica().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        }
        return (txtprev ? "<b>Para que pueda remitir desde:</b> " : "") + r + ".";
    }

    private String mostrarDatosOrigenBulk(EppGteRegistro ro, boolean txtprev) {
        String r = "";
        if (ro.getOrigenAlmacen() != null) {
            r = ro.getOrigenAlmacen().getRazonSocial() + ". Dirección: "
                    + ro.getOrigenAlmacen().getDireccion()
                    + ", Distrito de " + ro.getOrigenAlmacen().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getOrigenAlmacen().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getOrigenAlmacen().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getOrigenPolvorin() != null) {
            r = ro.getOrigenPolvorin().getDescripcion() + ". Dirección: "
                    + ro.getOrigenPolvorin().getDireccion()
                    + ", Distrito de " + ro.getOrigenPolvorin().getDistritoId().getNombre()
                    + ", Provincia de " + ro.getOrigenPolvorin().getDistritoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getOrigenPolvorin().getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getOrigenAlmacenAduana() != null) {
            r = ro.getOrigenAlmacenAduana().getRazonSocial() + ". Dirección: "
                    + ro.getOrigenAlmacenAduana().getDireccion()
                    + ", Distrito de " + ro.getOrigenAlmacenAduana().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getOrigenAlmacenAduana().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getOrigenAlmacenAduana().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getPuertoAduaneroOrigen() != null) {
            r = ro.getPuertoAduaneroOrigen().getNombre() + ". Dirección: "
                    + ro.getPuertoAduaneroOrigen().getDireccion()
                    + ", Distrito de " + ro.getPuertoAduaneroOrigen().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getPuertoAduaneroOrigen().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getPuertoAduaneroOrigen().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getLugarUsoOrigen() != null) {
            r = ro.getLugarUsoOrigen().getNombre() + ". Dirección: "
                    + "Distrito de " + ro.getLugarUsoUbigeoOrigen().getDistId().getNombre()
                    + ", Provincia de " + ro.getLugarUsoUbigeoOrigen().getDistId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getLugarUsoUbigeoOrigen().getDistId().getProvinciaId().getDepartamentoId().getNombre();
        }else if (ro.getOrigenFabrica() != null) {
            if(ro.getOrigenAlquilerId()!= null ){
                r = ro.getOrigenAlquilerId().getArrendatariaId().getRznSocial();
            }else{
                r = ro.getOrigenFabrica().getPersonaId().getRznSocial();
            }
            r = r + ". Dirección: "
                    + ro.getOrigenFabrica().getDireccion()
                    + ", Distrito de " + ro.getOrigenFabrica().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getOrigenFabrica().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getOrigenFabrica().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        }
        return (txtprev ? "<b>Para que pueda remitir desde:</b> " : "") + r + ".";
    }

    /**
     * Metodo que muestra los datos del destino
     *
     * @param ro
     * @return
     */
    private String mostrarDatosDestino(EppRegistroGuiaTransito ro, boolean txtprev) {
        String r = "";
        if (ro.getDestinoAlmacen() != null) {
            r = ro.getDestinoAlmacen().getRazonSocial() + ". Dirección: "
                    + ro.getDestinoAlmacen().getDireccion()
                    + ", Distrito de " + ro.getDestinoAlmacen().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getDestinoAlmacen().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getDestinoAlmacen().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getDestinoPolvorin() != null) {
            r = ro.getDestinoPolvorin().getDescripcion() + ". Dirección: "
                    + ro.getDestinoPolvorin().getDireccion()
                    + ", Distrito de " + ro.getDestinoPolvorin().getDistritoId().getNombre()
                    + ", Provincia de " + ro.getDestinoPolvorin().getDistritoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getDestinoPolvorin().getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getDestinoAlmacenAduana() != null) {
            r = ro.getDestinoAlmacenAduana().getRazonSocial() + ". Dirección: "
                    + ro.getDestinoAlmacenAduana().getDireccion()
                    + ", Distrito de " + ro.getDestinoAlmacenAduana().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getDestinoAlmacenAduana().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getDestinoAlmacenAduana().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getPuertoAduaneroDestino() != null) {
            r = ro.getPuertoAduaneroDestino().getNombre() + ". Dirección: "
                    + ro.getPuertoAduaneroDestino().getDireccion()
                    + ", Distrito de " + ro.getPuertoAduaneroDestino().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getPuertoAduaneroDestino().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getPuertoAduaneroDestino().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getLugarUsoDestino() != null) {
            r = ro.getLugarUsoDestino().getNombre() + ". Dirección: "
                    + "Distrito de " + ro.getLugarUsoUbigeoDestino().getDistId().getNombre()
                    + ", Provincia de " + ro.getLugarUsoUbigeoDestino().getDistId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getLugarUsoUbigeoDestino().getDistId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getDestinoFabrica() != null) {
            if(ro.getDestinoAlquiler() != null ){
                r = ro.getDestinoAlquiler().getArrendatariaId().getRznSocial();
            }else{
                r = ro.getDestinoFabrica().getPersonaId().getRznSocial();
            }
            r = r + ". Dirección: "
                    + ro.getDestinoFabrica().getDireccion()
                    + ", Distrito de " + ro.getDestinoFabrica().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getDestinoFabrica().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getDestinoFabrica().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        }
        return (txtprev ? "<b>Con destino a:</b> " : "") + r + ".";
    }

    private String mostrarDatosDestinoBulk(EppGteRegistro ro, boolean txtprev) {
        String r = "";
        if (ro.getDestinoAlmacen() != null) {
            r = ro.getDestinoAlmacen().getRazonSocial() + ". Dirección: "
                    + ro.getDestinoAlmacen().getDireccion()
                    + ", Distrito de " + ro.getDestinoAlmacen().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getDestinoAlmacen().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getDestinoAlmacen().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getDestinoPolvorin() != null) {
            r = ro.getDestinoPolvorin().getDescripcion() + ". Dirección: "
                    + ro.getDestinoPolvorin().getDireccion()
                    + ", Distrito de " + ro.getDestinoPolvorin().getDistritoId().getNombre()
                    + ", Provincia de " + ro.getDestinoPolvorin().getDistritoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getDestinoPolvorin().getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getDestinoAlmacenAduana() != null) {
            r = ro.getDestinoAlmacenAduana().getRazonSocial() + ". Dirección: "
                    + ro.getDestinoAlmacenAduana().getDireccion()
                    + ", Distrito de " + ro.getDestinoAlmacenAduana().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getDestinoAlmacenAduana().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getDestinoAlmacenAduana().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getPuertoAduaneroDestino() != null) {
            r = ro.getPuertoAduaneroDestino().getNombre() + ". Dirección: "
                    + ro.getPuertoAduaneroDestino().getDireccion()
                    + ", Distrito de " + ro.getPuertoAduaneroDestino().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getPuertoAduaneroDestino().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getPuertoAduaneroDestino().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        } else if (ro.getLugarUsoDestino() != null) {
            r = ro.getLugarUsoDestino().getNombre() + ". Dirección: "
                    + "Distrito de " + ro.getLugarUsoUbigeoDestino().getDistId().getNombre()
                    + ", Provincia de " + ro.getLugarUsoUbigeoDestino().getDistId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getLugarUsoUbigeoDestino().getDistId().getProvinciaId().getDepartamentoId().getNombre();
        }else if (ro.getDestinoFabrica() != null) {
            if(ro.getDestinoAlquilerId()!= null ){
                r = ro.getDestinoAlquilerId().getArrendatariaId().getRznSocial();
            }else{
                r = ro.getDestinoFabrica().getPersonaId().getRznSocial();
            }
            r = r + ". Dirección: "
                    + ro.getDestinoFabrica().getDireccion()
                    + ", Distrito de " + ro.getDestinoFabrica().getUbigeoId().getNombre()
                    + ", Provincia de " + ro.getDestinoFabrica().getUbigeoId().getProvinciaId().getNombre()
                    + ", Departamento de " + ro.getDestinoFabrica().getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        }
        return (txtprev ? "<b>Con destino a:</b> " : "") + r + ".";
    }

    /**
     * Metodo que muestra los titulos del pdf de Guia de Transito
     *
     * @param p
     * @return
     */
    private String tituloReporteGuiaTran(EppRegistroGuiaTransito p) {

        String tituloGt; //P_TIPO_GT

        switch (p.getTipoTramite().getCodProg()) {
            case "TP_RGUIA_ADQUI1":
                //1. ADQUISICION Y USO : FAB -> POLV
                tituloGt = "GUÍA DE TRANSITO VINCULADA CON UNA AUTORIZACIÓN DE ADQUISICIÓN Y USO";
                break;
            case "TP_RGUIA_ADQUI2":
                //2. ADQUISICION Y USO : POLV>LU/LU>POLV
                tituloGt = "GUÍA DE TRANSITO DE POLVORÍN A LUGAR DE USO";
                break;
            case "TP_RGUIA_INTER":
                //3. TRASLADO INTERNO
                tituloGt = "GUÍA DE TRANSITO DE TRASLADO INTERNO";
                break;
            case "TP_RGUIA_DEVOL":
                //4. DEVOLUCION
                tituloGt = "GUÍA DE TRÁNSITO PARA EL EXTORNO DE SALDO";
                break;
            case "TP_RGUIA_PLAPLA":
                //5. TRASLADO PLANTA/PLANTA
                tituloGt = "GUÍA DE TRÁNSITO PARA EL TRASLADO ENTRE PLANTAS DEL MISMO FABRICANTE";
                break;
            case "TP_RGUIA_COMFAB":
                //6. COMERCIALIZACION ENTRE FABRICAS
                tituloGt = "GUÍA DE TRÁNSITO ENTRE PLANTAS DE DISTINTO FABRICANTE";
                break;
            case "TP_RGUIA_INT":
                //7. COMERCIO I/S  
                tituloGt = "GUÍA DE TRÁNSITO DE INTERNAMIENTO";
                break;
            case "TP_RGUIA_SAL":
                //8. COMERCIO I/S
                tituloGt = "GUÍA DE TRÁNSITO DE SALIDA";
                break;
            case "TP_RGUIA_TRA":
                //9. TRANSITO INTERNACIONAL 
                tituloGt = "GUÍA DE TRÁNSITO INTERNACIONAL";
                break;
            case "TP_RGUIA_OTR":
                //10. ZPAE 
                tituloGt = "GUÍA DE TRÁNSITO PARA EL TRASLADO HACIA ZONA PRIMARIA CON AUTORIZACIÓN ESPECIAL";
                break;
            default:
                tituloGt = "";
                break;
        }

        return tituloGt;
    }

    /**
     * Metodo que muestra la firma de la ODE
     *
     * @return
     */
    private String mostrarFirmaXOde() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String rutaImagen = "" + servletContext.getRealPath("/resources/imagenes");
        rutaImagen = rutaImagen + "/FIRMA_10_GEPP.png";
        return rutaImagen;
    }

    public List<TipoExplosivoGt> getLstTipoEstadoGuia() {
        return ejbTipoExplosivoFacade.lstTipoEstadoGuiaExternaAdministrado();
    }

    public List<TipoExplosivoGt> getLstTipoEstadoGuiaListado() {
        return ejbTipoExplosivoFacade.lstTipoEstadoGuiaExternaListado();
    }

    public List<TipoExplosivoGt> getLstTipoEstadoGuiaFabricante() {
        return ejbTipoExplosivoFacade.lstTipoEstadoGuiaExternaFabricante();
    }

    /**
     * METODO PARA VISUALIZAR LOS DATOS DE LA RESOLUCION SELECCIONADA
     */
    public void mostrarResolucionPrevia() {
        explosivoSeleccionado = null;
        lstExplosivosCombo = new ArrayList();
        numeroResolucionGerencia = "";        
        fechaCaducidadResolucionGerencia = null;
        tipoResolucionGerencia = "";
        strViewVencimiento = "";
        tipoDestino = null;
        tipoOrigen = null;
        tipoTransporte = null;
        setMostrarLugarUsoUbigeoOrigen(false);
        setMostrarLugarUsoUbigeoDestino(false);
        currentBulk.setEppGteExplosivoSolicitaList(new ArrayList());
        borrarResolucionSeleccionada();
        borrarEmpTransporteSeleccionado();
        
        if(currentBulk.getId() == null){
            setDisableResolucion(false);
        }
                
        switch (criterioResGuia) {
            case "1":
                //ADQUISICION Y USO: FAB -> POLV
                reiniciaOrigenDestino();
                setMostrarresglobprev(true);
                lstTipoGtOrigen = ejbTipoExplosivoFacade.lstTipoODGuiaTransito("1");
                lstTipoGtDestino = ejbTipoExplosivoFacade.lstTipoODGuiaTransito("1D");
                tipoDestino = null;//nejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
                tipoOrigen = null;//ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
                txtTipoTramite = "";
                setRenderSaldoActual(true);
                setBloqueaSelectDestino(false);
                setBloqueaSelectOrigen(true);
                break;
            case "2":
                //ADQUISICION Y USO: POLVORIN / LUGAR DE USO O LUGAR DE USO / POLVORIN
                reiniciaOrigenDestino();
                setMostrarresglobprev(true);
                //lstTipoODGuiaTransito = tipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                lstTipoGtOrigen = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                lstTipoGtDestino = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                txtTipoTramite = "";
                reiniciaOrigenDestino();
                setRenderSaldoActual(true);
                break;
            case "3":
                //TRASLADO INTERNO
                reiniciaOrigenDestino();
                setMostrarresglobprev(true);
                setRenderSaldoActual(true);
                //lstTipoODGuiaTransito = tipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                lstTipoGtOrigen = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                lstTipoGtDestino = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                //tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
                //tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
                txtTipoTramite = "";
                break;
            case "5":
                //PLANTA / PLANTA
                reiniciaOrigenDestino();
                setMostrarresglobprev(false);
                setRenderSaldoActual(false);
                //lstTipoODGuiaTransito = tipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                lstTipoGtOrigen = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                lstTipoGtDestino = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                txtTipoTramite = "PLANTA A PLANTA";                    
                nombreEmpresaOrigen = "";
                nombreEmpresaDestino = "";
                lstExplosivosCombo = new ArrayList(lstExplosivosTemp);                
                setDisableResolucion(true);
                break;
            case "6":
                //COMERC. ENTRE FABRICAS
                reiniciaOrigenDestino();
                setMostrarresglobprev(true);
                setRenderSaldoActual(false);
                lstTipoGtOrigen = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);
                lstTipoGtDestino = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);                
                txtTipoTramite = "";
                break;    
            default:
                reiniciaOrigenDestino();
                lstTipoGtOrigen = new ArrayList();
                lstTipoGtDestino = new ArrayList();
                txtTipoTramite = "";
                tipoDestino = null;
                tipoOrigen = null;
                setDisableResolucion(true);
                resolucionGerencia = null;
                setRenderSaldoActual(false);
                break;
        }
    }

    /**
     * METODO PARA REINICIAR LOS DATOS DEL ORIGEN O DESTINO
     */
    public void reiniciaOrigenDestino() {
        tipoDestino = null;
        tipoOrigen = null;
        setBloqueaSelectDestino(false);
        setBloqueaSelectOrigen(false);
        nombreEmpresaOrigen = "";
        nombreEmpresaDestino = "";
    }

    /**
     * METODO PARA ABRIR EL DIALOGO DE LA RESOLUCION REFERENCIADA
     */
    public void openDlgResolucionPrevia() {
        filtro = null;
        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogResGer').show()");
        RequestContext.getCurrentInstance().update("frmDlgResGer");

    }

    /**
     * METODO PARA ABRIR EL DIALOGO DEL ORIGEN
     */
    /*public void openDialogOrigenXTipo() {
        filtro = null;
        JsfUtil.setSessionAttribute("actionODGt", JsfUtil.parametroHtml("actionODGt"));
        if (tipoOrigen != null) {
            if (tipoOrigen.getCodProg().equals("TP_ODRGT_POL")) {
                lstDlgPolvorin = new ArrayList();
                RequestContext.getCurrentInstance().execute("PF('CreateViewDialogPolvorin').show()");
                RequestContext.getCurrentInstance().update("frmDlgPolvorin");
            }
            if (tipoOrigen.getCodProg().equals("TP_ALMEMDE_LUSO")) {
                dept = null;
                prov = null;
                dist = null;
                lstDlgLugarUso = null;
                RequestContext.getCurrentInstance().execute("PF('CreateViewDialogLugarUso').show()");
                RequestContext.getCurrentInstance().update("frmDlgLugarUso");
            }
        } else {
            JsfUtil.mensajeError("Primero debe seleccionar tipo de Origen");
        }
    }*/
    /**
     * METODO PARA ABRIR EL DIALOGO DEL DESTINO
     */
    /*public void openDialogDestinoXTipo() {
        filtro = null;
        JsfUtil.setSessionAttribute("actionODGt", JsfUtil.parametroHtml("actionODGt"));
        if (tipoDestino != null) {

            if (tipoDestino.getCodProg().equals("TP_ODRGT_POL")) {
                lstDlgPolvorin = new ArrayList();
                for (EppPolvorin p : resolucionGerencia.getEppPolvorinList()) {
                    if (p.getActivo() == 1) {
                        lstDlgPolvorin.add(p);
                    }
                }
                RequestContext.getCurrentInstance().execute("PF('CreateViewDialogPolvorin').show()");
                RequestContext.getCurrentInstance().update("frmDlgPolvorin");
            }
            if (tipoDestino.getCodProg().equals("TP_ALMEMDE_LUSO")) {
                if (resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_GLO")
                        || resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_EXC")
                        || resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_AAFOR")
                        || resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_AAHID")
                        || resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_AAPOR")) {
                    lstlugardeuso = resolucionGerencia.getComId().getEppLugarUsoList();
                } else {
                    lstlugardeuso = resolucionGerencia.getEppLugarUsoList();
                }
                RequestContext.getCurrentInstance().execute("PF('CreateViewDialogLugarUso').show()");
                RequestContext.getCurrentInstance().update("frmDlgLugarUso");
            }
        } else {
            JsfUtil.mensajeAdvertencia("Primero debe seleccionar tipo de Destino");
        }
    }*/
    
    public void cargarPolvorinesOrigenPreviaBusqueda(){
        lstDlgMapPolvorin = new ArrayList();
        if (criterioResGuia.equals("2") || criterioResGuia.equals("3")) { 
            for (EppPolvorin p : resolucionGerencia.getEppPolvorinList()) {
                if(p.getActivo() == 0){
                    continue;
                }
                if(p.getRegistroId() == null){
                    continue;
                }
                if(p.getRegistroId().getTipoOpeId().getCodProg().equals("TP_OPE_CAN")){
                    continue;
                }
                if (p.getRegistroId().getEppResolucion().getActivo() == 0) {
                    continue;
                }
                if(!p.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_FIN")){
                    continue;
                }
                    
                Map nmap = new HashMap();
                nmap.put("TIPO", 1);
                nmap.put("id", p.getId());
                nmap.put("descripcion", p.getDescripcion());
                nmap.put("activo", p.getActivo());

                nmap.put("numero", p.getRegistroId().getEppResolucion().getNumero());
                nmap.put("fechaFin", p.getRegistroId().getEppResolucion().getFechaFin());
                nmap.put("direccion", p.getDireccion());
                nmap.put("rznSocial", p.getPropietarioId().getRznSocial());
                nmap.put("ubigeo", p.getDistritoId().getNombre() + " / " + p.getDistritoId().getProvinciaId().getNombre() + " / " + p.getDistritoId().getProvinciaId().getDepartamentoId().getNombre());
                nmap.put("contratoId", 0);
                lstDlgMapPolvorin.add(nmap);
            }
        }
        if (criterioResGuia.equals("5")) {
            lstDlgMapPolvorin = ejbPolvorinFacade.lstMapPovorinXEmpresa(currentBulk.getEmpresaId());
        }
        if (criterioResGuia.equals("6")) {
            lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGt(null, resolucionGerencia.getEmpresaId(), null);
        }
        if(lstDlgMapPolvorin != null){
            List<Map> lstTempPolvorin = new ArrayList(lstDlgMapPolvorin);
            lstDlgMapPolvorin = new ArrayList();
            for(Map polv : lstTempPolvorin){
                if(polv.get("activo").toString().equals("1") && polv.get("fechaFin") != null && JsfUtil.getFechaSinHora((Date) polv.get("fechaFin")).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0 ){
                    lstDlgMapPolvorin.add(polv);
                }
            }
        }
    }
    
    public void cargarPolvorinesDestinoPreviaBusqueda(){
        lstDlgMapPolvorin = new ArrayList();
        if (criterioResGuia.equals("1") || criterioResGuia.equals("2") || criterioResGuia.equals("3")) {
            for (EppPolvorin p : resolucionGerencia.getEppPolvorinList()) {
                if(p.getActivo() == 0){
                    continue;
                }
                if(p.getRegistroId() == null){
                    continue;
                }
                if(p.getRegistroId().getTipoOpeId().getCodProg().equals("TP_OPE_CAN")){
                    continue;
                }
                if(p.getRegistroId().getEppResolucion().getActivo() == 0) {
                    continue;
                }
                Map nmap = new HashMap();
                nmap.put("TIPO", 1);
                nmap.put("id", p.getId());
                nmap.put("descripcion", p.getDescripcion());
                nmap.put("activo", p.getActivo());

                nmap.put("numero", p.getRegistroId().getEppResolucion().getNumero());
                nmap.put("fechaFin", p.getRegistroId().getEppResolucion().getFechaFin());
                nmap.put("direccion", p.getDireccion());
                nmap.put("rznSocial", p.getPropietarioId().getRznSocial());
                nmap.put("ubigeo", p.getDistritoId().getNombre() + " / " + p.getDistritoId().getProvinciaId().getNombre() + " / " + p.getDistritoId().getProvinciaId().getDepartamentoId().getNombre());
                nmap.put("contratoId", 0);
                lstDlgMapPolvorin.add(nmap);
            }
        }
        if (criterioResGuia.equals("5")) {
            lstDlgMapPolvorin = ejbPolvorinFacade.lstMapPovorinXEmpresa(empresa);
        }
        if (criterioResGuia.equals("6")) {
            lstDlgMapPolvorin = ejbPolvorinFacade.lstMapPovorinXEmpresa(empresa);
        }
        if(lstDlgMapPolvorin != null){
            List<Map> lstTempPolvorin = new ArrayList(lstDlgMapPolvorin);
            lstDlgMapPolvorin = new ArrayList();
            for(Map polv : lstTempPolvorin){
                if(polv.get("activo").toString().equals("1") && polv.get("fechaFin") != null && JsfUtil.getFechaSinHora((Date) polv.get("fechaFin")).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0 ){
                    lstDlgMapPolvorin.add(polv);
                }
            }
        }
    }
    
    /**
     * METODO PARA LIMPIAR EL NOMBRE DE LA EMPRESA DE ORIGEN
     */
    public void openDlgOrigen() {
        try {
            if (criterioResGuia == null || criterioResGuia.equals("0")) {
                JsfUtil.mensajeAdvertencia("Debe indicar primero el Tipo de Guía en la sección Datos de Autorización");
                tipoOrigen = null;
            } else if (resolucionGerencia == null
                    && (criterioResGuia.equals("7") || criterioResGuia.equals("1")
                    || criterioResGuia.equals("2") || criterioResGuia.equals("3")
                    || criterioResGuia.equals("4") || criterioResGuia.equals("6"))) {
                JsfUtil.mensajeAdvertencia("Debe cargar primero la Resolución de Gerencia en la sección Datos de Autorización");
                tipoOrigen = null;
            } else if (resolucionGerencia != null || criterioResGuia.equals("5") ) {
                if (tipoOrigen != null) {
                    
                    switch (tipoOrigen.getCodProg()) {
                        case "TP_ODRGT_POL":
                            if(criterioResGuia.equals("2")){
                                if(tipoOrigen.getCodProg().equals("TP_ODRGT_POL") && tipoDestino != null && tipoDestino.getCodProg().equals("TP_ODRGT_POL")){
                                    JsfUtil.mensajeError("No puede seleccionar el origen polvorín en este tipo de guía");
                                    tipoOrigen = null;
                                    return;
                                }
                            }
                            JsfUtil.setSessionAttribute("actionODGt", "1");
                            filtro = null;
                            ubigeo = null;
                            cargarPolvorinesOrigenPreviaBusqueda();
                            setRenderCboDestinoPolvorin(true);
                            setRenderCboDestinoLugarUso(false);
                            setMostrarLugarUsoUbigeoOrigen(false);
                            RequestContext.getCurrentInstance().execute("PF('wvDlgPolvOrigen').show()");
                            RequestContext.getCurrentInstance().update("frmDlgPolvOrigen");
                            break;
                        case "TP_ODRGT_FAB":                            
                            JsfUtil.setSessionAttribute("actionODGt", "1");
                            filtro = null;
                            lstDlgLocalMap = new ArrayList();
                            if (criterioResGuia.equals("5")) {
                                lstDlgLocalMap = ejbEppLocalFacade.buscarLocalByDireccionByRznSocialMap(null,currentBulk.getEmpresaId().getId());
                            }
                            if (criterioResGuia.equals("6")) {
                                lstDlgLocalMap = ejbEppLocalFacade.buscarLocalByDireccionByRznSocialMap(null,resolucionGerencia.getEmpresaId().getId());
                            }
                            ordenarLocales(lstDlgLocalMap);
                            
                            RequestContext.getCurrentInstance().execute("PF('CreateViewDialogFabrica').show()");
                            RequestContext.getCurrentInstance().update("frmDlgFabrica");
                            break;
                        case "TP_ALMEMDE_LUSO":
                            if(criterioResGuia.equals("2")){
                                if(tipoOrigen.getCodProg().equals("TP_ALMEMDE_LUSO") && tipoDestino != null && tipoDestino.getCodProg().equals("TP_ALMEMDE_LUSO")){
                                    JsfUtil.mensajeError("No puede seleccionar el origen lugar de uso en este tipo de guía");
                                    tipoOrigen = null;
                                    return;
                                }
                            }
                            JsfUtil.setSessionAttribute("actionODGt", "1");
                            if (criterioResGuia.equals("2")) {
                                if (!resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_EVE")) {
                                    lstDlgLugarUso = resolucionGerencia.getComId().getEppLugarUsoList();
                                } else {
                                    lstDlgLugarUso = resolucionGerencia.getEppLugarUsoList();
                                }
                            }
                            setMostrarLugarUsoUbigeoOrigen(true);
                            if(criterioResGuia.equals("2")){
                                setMostrarLugarUsoUbigeoDestino(false);
                            }
                            RequestContext.getCurrentInstance().execute("PF('CreateViewDialogLugarUso').show()");
                            RequestContext.getCurrentInstance().update("frmDlgLugarUso");
                            break;
                        default:
                            break;
                    }
                } else {
                    nombreEmpresaOrigen = null;
                    setMostrarLugarUsoUbigeoOrigen(false);
                    RequestContext.getCurrentInstance().update("pnlgOrigenDestino");
                }
            }
            //nombreEmpresaOrigen = "";
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * METODO PARA LIMPIAR EL NOMBRE DE LA EMPRESA DE DESTINO
     */
    public void openDlgDestino() {
        if (criterioResGuia== null || criterioResGuia.equals("0")) {
            JsfUtil.mensajeAdvertencia("Debe indicar primero el Tipo de Guía en la sección Datos de Autorización");
            tipoDestino = null;
        } else if (resolucionGerencia == null 
                 && (criterioResGuia.equals("7") || criterioResGuia.equals("1")
                || criterioResGuia.equals("2") || criterioResGuia.equals("3")
                || criterioResGuia.equals("4") || criterioResGuia.equals("6")) ) {
            JsfUtil.mensajeAdvertencia("Debe cargar primero la Resolución de Gerencia en la sección Datos de Autorización");
            tipoDestino = null;
        } else if (resolucionGerencia != null || criterioResGuia.equals("5") ) {
            if (tipoDestino != null) {
                switch (tipoDestino.getCodProg()) {
                    case "TP_ODRGT_POL":
                        if(criterioResGuia.equals("2")){
                            if(tipoOrigen!= null && tipoOrigen.getCodProg().equals("TP_ODRGT_POL") && tipoDestino.getCodProg().equals("TP_ODRGT_POL")){
                                JsfUtil.mensajeError("No puede seleccionar el destino polvorín en este tipo de guía");
                                tipoDestino = null;
                                return;
                            }
                        }
                        JsfUtil.setSessionAttribute("actionODGt", "2");                        
                        filtro = "";
                        cargarPolvorinesDestinoPreviaBusqueda();
                        setRenderCboDestinoPolvorin(true);
                        setRenderCboDestinoLugarUso(false);
                        setMostrarLugarUsoUbigeoDestino(false);
                        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogPolvorin').show()");
                        RequestContext.getCurrentInstance().update("frmDlgPolvorin");
                        break;
                    case "TP_ALMEMDE_LUSO":
                        if(criterioResGuia.equals("2")){
                            if(tipoOrigen!= null && tipoOrigen.getCodProg().equals("TP_ALMEMDE_LUSO") && tipoDestino.getCodProg().equals("TP_ALMEMDE_LUSO")){
                                JsfUtil.mensajeError("No puede seleccionar el destino lugar de uso en este tipo de guía");
                                tipoDestino = null;
                                return;
                            }
                        }
                        JsfUtil.setSessionAttribute("actionODGt", "2");
                        lstDlgLugarUso = new ArrayList();
                        if (resolucionGerencia.getComId() != null) {
                            lstDlgLugarUso = resolucionGerencia.getComId().getEppLugarUsoList();
                        } else {
                            lstDlgLugarUso = resolucionGerencia.getEppLugarUsoList();
                        }
                        setRenderCboDestinoPolvorin(false);
                        setRenderCboDestinoLugarUso(true);
                        setMostrarLugarUsoUbigeoDestino(true);
                        if(criterioResGuia.equals("2")){
                            setMostrarLugarUsoUbigeoOrigen(false);
                        }
                        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogLugarUso').show()");
                        RequestContext.getCurrentInstance().update("frmDlgLugarUso");
                        break;
                    case "TP_ODRGT_FAB":                        
                        JsfUtil.setSessionAttribute("actionODGt", "2");
                        filtro = null;
                        lstDlgLocalMap = new ArrayList();
                        if (criterioResGuia.equals("5") || criterioResGuia.equals("6")) {
                            lstDlgLocalMap = ejbEppLocalFacade.buscarLocalByDireccionByRznSocialMap(null, currentBulk.getEmpresaId().getId());
                            ordenarLocales(lstDlgLocalMap);
                        }
                        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogFabrica').show()");
                        RequestContext.getCurrentInstance().update("frmDlgFabrica");
                        break;
                    default:
                        break;
                }
            }else{
                nombreEmpresaDestino = "";
                setMostrarLugarUsoUbigeoDestino(false);
            }
        }
        nombreEmpresaDestino = "";
    }
    
    public boolean validacionSeleccionRG(EppRegistro p_rg){
        boolean valida = true;
        if(p_rg.getTipoProId().getCodProg().equals("TP_PRTUP_EXC") || p_rg.getTipoProId().getCodProg().equals("TP_PRTUP_AAPOR")){
            valida = false;
            JsfUtil.mensajeError("Ud. no puede seleccionar este tipo de resolución de "+p_rg.getTipoProId().getNombre()+". Debe presentar el expediente por mesa de partes.");
        }
        //if(criterioResGuia.equals("1") || criterioResGuia.equals("2")){
            EppRegistro item = ejbRegistroFacade.buscarRegistrosReferenciadasXId(p_rg.getId());
            if(item != null){
                if(!item.getEstado().getCodProg().equals("TP_REGEV_FIN")){
                    valida = false;
                    JsfUtil.mensajeError("Ud. no puede seleccionar esta RG porque se encuentra en proceso de trámite de "+ item.getTipoRegId().getNombre().toLowerCase());
                }else{
                    if(item.getEppResolucion() != null && item.getEppResolucion().getFechaIni() != null){
                        if(JsfUtil.getFechaSinHora(item.getEppResolucion().getFechaIni()).compareTo(JsfUtil.getFechaSinHora(new Date())) <= 0){
                            valida = false;
                            JsfUtil.mensajeError("Ud. no puede seleccionar esta RG porque tiene un nuevo trámite");
                        }
                    }
                }
            }
        //}
            
        return valida;
    }

    /**
     * RETORNA EL VALOR PARA LA PERSONA DESDE EL DIALOG
     *
     * @param event
     */
    public void seleccionarDlgResGer(SelectEvent event) {
        try {            
            resolucionGerencia = (EppRegistro) event.getObject();
            if(!validacionSeleccionRG(resolucionGerencia)){
                resolucionGerencia = null;
                setDisableResolucion(false);
                return;
            }
            
            mostrarResolucionPrevia();
            setDisableResolucion(true);
            resolucionGerencia = (EppRegistro) event.getObject();
            
            cargarExplosivos(resolucionGerencia);
            polvorin = null;
            lugarUso = null;
            fabrica = null;
            lugarUsoUbigeoOrigen = null;
            lugarUsoUbigeoDestino = null;
            nombreEmpresaOrigen = null;
            nombreEmpresaDestino = null;
            lstLugarUsoUbigeo = new ArrayList();
            if (lstExplosivosCombo.isEmpty()) {
                JsfUtil.mensajeAdvertencia("La resolución de gerencia elegida no tiene explosivos registrados, debe verificar");
                resolucionGerencia = null;
                numeroResolucionGerencia = "";
                fechaCaducidadResolucionGerencia = null;
                strViewVencimiento = null;
                tipoResolucionGerencia = "";
            } else {
                numeroResolucionGerencia = "<b>" + resolucionGerencia.getEppResolucion().getNumero() + "</b>";
                fechaCaducidadResolucionGerencia = resolucionGerencia.getEppResolucion().getFechaFin();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                tipoResolucionGerencia = "<b>" + resolucionGerencia.getTipoProId().getAbreviatura() + "</b>";
                JsfUtil.mensaje("Se cargaron " + lstExplosivosCombo.size() + " explosivos de la resolución N°:" + resolucionGerencia.getEppResolucion().getNumero());
            }

            /////////  REINICIANDO DATOS DE DESTINO Y EXPLOSIVOS ///////////
            tipoDestino = null;
            polvorin = null;
            lugarUso = null;
            lugarUsoUbigeoDestino = null;
            currentBulk.setDestinoPolvorin(null);
            currentBulk.setLugarUsoDestino(null);
            currentBulk.setLugarUsoUbigeoDestino(null);
            currentBulk.setDestinoFabrica(null);
            currentBulk.setDestinoAlquilerId(null);
            nombreEmpresaDestino = null;
            if (currentBulk.getEppGteExplosivoSolicitaList() != null) {
                if (!currentBulk.getEppGteExplosivoSolicitaList().isEmpty()) {
                    for (EppGteExplosivoSolicita es : currentBulk.getEppGteExplosivoSolicitaList()) {
                        es.setActivo((short) 0);
                    }
                }
            } else {
                currentBulk.setEppGteExplosivoSolicitaList(new ArrayList());
            }
            ///////////////////////////////////////////////////////////////            

            if (currentBulk.getId() == null) {
                RequestContext.getCurrentInstance().update("crearForm");
            } else {
                RequestContext.getCurrentInstance().update("editarForm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reiniciarFormulario() {

    }

    /**
     * METODO QUE CARGA LOS EXPLOSIVOS DE ACUERDO AL TIPO DE AUTORIZACION
     *
     * @param resolucionGerencia
     */
    private void cargarExplosivos(EppRegistro p_rg) {
        lstExplosivosCombo = new ArrayList();
        switch (p_rg.getTipoProId().getCodProg()) {
            case "TP_PRTUP_GLO":
            case "TP_PRTUP_EVE":
            case "TP_PRTUP_EXC":
            case "TP_PRTUP_AAFOR":
            case "TP_PRTUP_AAHID":
            case "TP_PRTUP_AAPOR":
                for (EppDetalleAutUso dau : p_rg.getEppDetalleAutUsoList()) {
                    if (dau.getActivo() == 1) {
                        lstExplosivosCombo.add(dau.getExplosivoId());
                    }
                }
                break;
            case "TP_PRTUP_AINT":
            case "TP_PRTUP_INT":
            case "TP_PRTUP_ASAL":
            case "TP_PRTUP_SAL":
                for (EppDetalleIntSal dau : p_rg.getEppRegistroIntSal().getEppDetalleIntSalList() ) {
                    if (dau.getActivo() == 1) {
                        lstExplosivosCombo.add(dau.getExplosivoId());
                    }
                }
                break;
            case "TP_PRTUP_PLA":
            case "TP_PRTUP_AFAB":
                for (EppPlantaExplosivo dau : p_rg.getEppPlantaExplosivoList()) {
                    if (dau.getActivo() == 1) {
                        lstExplosivosCombo.add(dau.getExplosivoId());
                    }
                }
                lstExplosivosCombo.add(ejbExplosivoFacade.find(new Long("26")));
                lstExplosivosCombo.add(ejbExplosivoFacade.find(new Long("9")));
                break;
            case "TP_PRTUP_COM":
            case "TP_PRTUP_ACOM":
                for (EppPlantaExplosivo dau : p_rg.getEppPlantaExplosivoList()) {
                    if (dau.getActivo() == 1) {
                        lstExplosivosCombo.add(dau.getExplosivoId());
                    }
                }
                lstExplosivosCombo.add(ejbExplosivoFacade.find(new Long("26")));
                lstExplosivosCombo.add(ejbExplosivoFacade.find(new Long("9")));
                break;
            default:
                break;
        }
    }

    /**
     * LISTA TIPO DE DOCUMENTOS DE AUTORIZACION MTC PARA LA GUIA DE TRANSITO
     *
     * @return
     */
    public List<TipoExplosivoGt> getLstTipoDocGuiaTransito() {
        return ejbTipoExplosivoFacade.lstTipoDocGuiaTransito();
    }

    /**
     * SETEA EL VALOR DE LUGAR DE USO ORIGEN
     */
    public void lstnrLugarUsoUbigeoOrigen() {
        currentBulk.setLugarUsoUbigeoOrigen(lugarUsoUbigeoOrigen);
    }

    /**
     * SETEA EL VALOR DE LUGAR DE USO DESTINO
     */
    public void lstnrLugarUsoUbigeoDestino() {
        currentBulk.setLugarUsoUbigeoDestino(lugarUsoUbigeoDestino);
    }

    /**
     * MUESTRA EL DIALOGO PARA BUSCAR EMPRESA DE TRANSPORTE
     */
    public void openListDialogEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 800);
        options.put("contentHeight", 460);
        // Modificar directorio y agregar archivo ListDialog //
        context.openDialog("/reutilizables/dlgBuscarEmpresa.xhtml", options, null);
    }

    /**
     * RETORNA EL VALOR PARA EL EXPEDIENTE DESDE EL DIALOG
     *
     * @param event
     */
    public void getEmpresaTransporteSeleccionada(SelectEvent event) {
        empresaTransporte = (SbPersonaGt) event.getObject();
    }

    /**
     * METODO DE BUSQUEDA DE DOCUMENTO MTC
     *
     * @return
     */
    public List<EppDocumento> selectDataDocumentos() {
        return ejbDocumentoFacade.findDocumento(tipodocumento == null ? "" : (tipodocumento.getId().equals(0L) ? "" : tipodocumento.getId().toString()), filtro == null ? "" : filtro.trim());
    }

    /**
     * METODO QUE SELECCIONA EL DOCUMENTO MTC
     *
     * @param event
     */
    public void getDocumentoSeleccionado(SelectEvent event) {
        docmtc = (EppDocumento) event.getObject();
        currentBulk.setEppDocumentoList(new ArrayList<EppDocumento>());
        currentBulk.getEppDocumentoList().add(docmtc);
    }

    /**
     * RETORNA EL VALOR DEL REGISTRO EN CURSO
     *
     * @return
     */
    public EppRegistroGuiaTransito getSelected() {
        if (current == null) {
            current = new EppRegistroGuiaTransito();
        }
        return current;
    }

    public EppGteRegistro getSelectedBulk() {
        if (currentBulk == null) {
            currentBulk = new EppGteRegistro();
        }
        return currentBulk;
    }

    /**
     * METODO PARA BORRAR UN CONDUCTOR DE LA TABLA
     *
     * @param l
     */
    public void borrarGuiaTransitoConductor(EppLicencia l) {
        current.getEppLicenciaList().remove(l);
    }

    /**
     * MUESTRA EL SALDO ACTUAL DEL EXPLOSIVO SELECCIONADO
     */
    public void mostrarSaldoActual() {
        boolean flagCalculaSaldo = true;
        double saldoTemp = 0;
        String polvorinesAnidados = "", resolucionesAnidadas = "";
        EppPolvorin polvTemp = null;
        EppRegistro regTemp = resolucionGerencia;
        saldoActualExplSolic = 0.0;
        
        if (resolucionGerencia != null && explosivoSeleccionado != null
                && !criterioResGuia.equals("2")
                && !criterioResGuia.equals("3")
                && !criterioResGuia.equals("4")) {

            switch (resolucionGerencia.getTipoProId().getCodProg()) {
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                case "TP_PRTUP_AAPOR":
                    if (resolucionGerencia.getEppDetalleAutUsoList() != null) {
                        for (EppDetalleAutUso dau : resolucionGerencia.getEppDetalleAutUsoList()) {
                            if (dau.getActivo() == 1 && Objects.equals(dau.getExplosivoId().getId(), explosivoSeleccionado.getId()) && Objects.equals(dau.getRegistroId().getId(), resolucionGerencia.getId())) {
                                saldoActualExplSolic = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoRgGee(resolucionGerencia.getEppResolucion(), explosivoSeleccionado);//dau.getSaldo();//dau.getCantidad().intValue();// + ;
                                break;
                            }
                        }
                    } else {
                        saldoActualExplSolic = 0.0;
                    }
                    break;
                case "TP_PRTUP_AINT":
                case "TP_PRTUP_ASAL":
                case "TP_PRTUP_INT":
                case "TP_PRTUP_SAL":
                    if (resolucionGerencia.getEppRegistroIntSal().getEppDetalleIntSalList() != null) {
                        saldoActualExplSolic = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoRgIs(resolucionGerencia.getEppResolucion(), explosivoSeleccionado);
                    } else {
                        saldoActualExplSolic = 0.0;
                    }
                    break;
                case "TP_PRTUP_PLA":
                case "TP_PRTUP_AFAB":
                    saldoActualExplSolic = 0.0;
                    break;
                default:
                    saldoActualExplSolic = 0.0;
                    break;
            }
        } else {
            switch (criterioResGuia) {
                case "2":
                    if(explosivoSeleccionado == null){
                        JsfUtil.mensajeError("Por favor seleccione el explosivo");
                        return;
                    }
                    if (currentBulk.getOrigenPolvorin() != null && currentBulk.getLugarUsoDestino() != null && currentBulk.getLugarUsoUbigeoDestino() != null) {
                        polvTemp = currentBulk.getOrigenPolvorin();
                        polvorinesAnidados = obtenerPolvorinesAnidados(polvTemp);
                        resolucionesAnidadas = obtenerResolucionesAnidadas(regTemp);
                        
                        saldoActualExplSolic = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoSumatoriaPolvLuuAnidados(
                                                    resolucionesAnidadas,
                                                    explosivoSeleccionado,
                                                    polvorinesAnidados );
                        
                    } else if (currentBulk.getLugarUsoOrigen() != null && currentBulk.getLugarUsoUbigeoOrigen() != null && currentBulk.getDestinoPolvorin() != null) {
                        while(flagCalculaSaldo){
                            saldoTemp = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoSumatoriaLuuPolv(
                                                        regTemp.getEppResolucion(),
                                                        explosivoSeleccionado,
                                                        currentBulk.getLugarUsoOrigen(),
                                                        currentBulk.getLugarUsoUbigeoOrigen());
                            if(regTemp.getRegistroId() != null){
                                regTemp = regTemp.getRegistroId();
                                if(regTemp.getEppResolucion() == null){
                                    flagCalculaSaldo = false;
                                }
                            }else{
                                flagCalculaSaldo = false;
                            }
                            saldoActualExplSolic += saldoTemp;
                        }
                            
                    } else {
                        explosivoSeleccionado = null;
                        JsfUtil.mensajeAdvertencia("Debe completar los datos de traslado");
                    }
                    break;
                case "3":
                    if(explosivoSeleccionado == null){
                        JsfUtil.mensajeError("Por favor seleccione el explosivo");
                        return;
                    }
                    if (currentBulk.getOrigenPolvorin() != null && currentBulk.getDestinoPolvorin() != null) {
                        polvTemp = currentBulk.getOrigenPolvorin();
                        polvorinesAnidados = obtenerPolvorinesAnidados(polvTemp);
                        resolucionesAnidadas = obtenerResolucionesAnidadas(regTemp);
                        saldoActualExplSolic = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoSumatoriaPolvLuuAnidados(
                                                                                        resolucionesAnidadas,
                                                                                        explosivoSeleccionado,
                                                                                        polvorinesAnidados);
                        if(saldoActualExplSolic < 0){
                            saldoActualExplSolic = 0.0;
                        }
                    } else {
                        explosivoSeleccionado = null;
                        JsfUtil.mensajeAdvertencia("Debe completar los datos de traslado origen y destino");
                    }
                    break;
                default:
                    saldoActualExplSolic = 0.0;
                    cantidadATransportar = 0.0;
                    break;
            }
        }
    }
    
    public String obtenerResolucionesAnidadas(EppRegistro item){
        String resoluciones = "";
        boolean flagContinuar = true;
        
        while(flagContinuar){
            if(item.getEppResolucion() != null){
                if(resoluciones.isEmpty()){
                    resoluciones = ""+item.getEppResolucion().getId();
                }else{
                    resoluciones += ", "+item.getEppResolucion().getId();
                }
            }
            if(item.getRegistroId() != null){
                item = item.getRegistroId();
                if(item.getEppResolucion() == null){
                    flagContinuar = false;
                }
            }else{
                flagContinuar = false;
            }
        }
        return resoluciones;
    }
    
    public String obtenerPolvorinesAnidados(EppPolvorin item){
        String ids = "";
        boolean flagContinuar = true;
        while(flagContinuar){
            if(ids.isEmpty()){
                ids = "" + item.getId();
            }else{
                ids += ", " + item.getId();
            }
            if(item.getRegistroId() != null && item.getRegistroId().getRegistroId() != null){
                if(item.getRegistroId().getRegistroId().getEppPolvorin() != null){
                    item = item.getRegistroId().getRegistroId().getEppPolvorin();
                }else{
                    flagContinuar = false;
                }                
            }else{
                flagContinuar = false;
            }
        }
        return ids;
    }

    /**
     * AGREGA UN PRODUCTO A LA LISTA DE EXPLOSIVOS
     */
    public void agregarProducto() {
        if (explosivoSeleccionado != null
                && cantidadATransportar != null
                && cantidadATransportar > 0) {
            if (validarSaldoExplosivo(saldoActualExplSolic, cantidadATransportar)) {
                if (validaExplosivoDuplicado(explosivoSeleccionado)) {
                    EppExplosivoSolicitado es = new EppExplosivoSolicitado();
                    es.setId(JsfUtil.tempId());
                    es.setExplosivoId(explosivoSeleccionado);
                    es.setCantidadAutorizada(saldoActualExplSolic);
                    es.setCantidad(cantidadATransportar);
                    es.setRegistroId(current);
                    es.setActivo((short) 1);
                    es.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    es.setAudNumIp(JsfUtil.getIpAddress());
                    List<EppExplosivoSolicitado> lst = new ArrayList<>();
                    lst = current.getEppExplosivoSolicitadoList();
                    current.setEppExplosivoSolicitadoList(new ArrayList<EppExplosivoSolicitado>());
                    current.setEppExplosivoSolicitadoList(lst);
                    current.getEppExplosivoSolicitadoList().add(es);
                    borrarValoresProducto();
                } else {
                    JsfUtil.mensajeError("Explosivo duplicado en la lista. Debe verificar.");
                }
            } else {
                JsfUtil.mensajeError("La cantidad ingresada supera al stock actual registrado.");
            }
        } else {
            JsfUtil.mensajeError("Ingrese valores correctos para agregar el explosivo");
        }
    }

    public void agregarProductoBulk() {
        if (explosivoSeleccionado != null
                && cantidadATransportar != null
                && cantidadATransportar > 0) {
            if (validarSaldoExplosivo(saldoActualExplSolic, cantidadATransportar)) {
                if (validaAgregarExplosivo(explosivoSeleccionado)) {
                    EppGteExplosivoSolicita es = new EppGteExplosivoSolicita();
                    es.setId(JsfUtil.tempId());
                    es.setExplosivoId(explosivoSeleccionado);
                    //es.setCantidadAutorizada(saldoActualExplSolic);
                    es.setCantidad(cantidadATransportar);
                    es.setRegistroId(currentBulk);
                    es.setActivo((short) 1);
                    es.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    es.setAudNumIp(JsfUtil.getIpAddress());
                    List<EppGteExplosivoSolicita> lst = new ArrayList<>();
                    lst = currentBulk.getEppGteExplosivoSolicitaList();
                    currentBulk.setEppGteExplosivoSolicitaList(new ArrayList<EppGteExplosivoSolicita>());
                    currentBulk.setEppGteExplosivoSolicitaList(lst);
                    currentBulk.getEppGteExplosivoSolicitaList().add(es);
                    borrarValoresProducto();
                }
            } else {
                JsfUtil.mensajeError("La cantidad ingresada supera al stock actual registrado.");
            }
        } else {
            JsfUtil.mensajeError("Ingrese valores correctos para agregar el explosivo");
        }
    }

    /**
     * MUESTRA EL SALDO ACTUAL DEL EXPLOSIVO EN LA TABLA
     *
     * @param c1
     * @param c2
     * @return
     */
    public String mostrarSaldo(Double c1, Double c2) {
        Double resultado = c1 - c2;
        if (resultado < 0) {
            return "0";
        } else {
            return "" + resultado;
        }
    }

    /**
     * VALIDA EL INGRESO DE UN EXPLOSIVO DUPLICADO EN LA TABLA
     *
     * @param p_v_explosivo
     * @return
     */
    public boolean validaExplosivoDuplicado(EppExplosivo p_v_explosivo) {
        boolean p_valida = true;

        for (EppExplosivoSolicitado es : current.getEppExplosivoSolicitadoList()) {
            if (Objects.equals(p_v_explosivo, es.getExplosivoId()) && es.getActivo() == 1) {
                p_valida = false;
            }
        }
        return p_valida;
    }

    /**
     * Valida Agregar explosivo multiples criterios
     *
     * @param p_v_explosivo
     * @return
     */
    public boolean validaAgregarExplosivo(EppExplosivo p_v_explosivo) {
        boolean p_valida = true;

        //VALIDA SI SE HA SELECCIONADO LA EMPRESA DE ORIGEN
        if (currentBulk.getOrigenPolvorin() == null && currentBulk.getOrigenFabrica() == null && currentBulk.getLugarUsoOrigen() == null) {
            p_valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar primero el polvorín, lugar de uso o fábrica de Orígen.");
        } else {
            //VERIFICANDO SI EL EXPLOSIVO ESTÁ ENTRE LOS EXPLOSIVOS DE LAS RG 
            //COMERCIALIZACION DEL POLVORIN ORIGEN
            /* 30112016-1701: LA GEPP SOLICITO SE QUITE LA VALIDACION TEMPORALMENTE
            List<EppExplosivo> lstE = eppRegistroGuiaTransitoFacade.listaExplosivosXRgComercializacionExp(currentBulk.getOrigenPolvorin().getRegistroId().getEmpresaId());
            if (lstE == null || lstE.isEmpty()) {
                p_valida = false;
                JsfUtil.mensajeAdvertencia("El explosivo que intenta agregar no se encuentra registrado en las autorizaciones de comercialización de "
                        + "la empresa fabricante.");
            } else {
                boolean valecc = false;
                for (EppExplosivo ec : lstE) {
                    if (Objects.equals(ec.getId(), p_v_explosivo.getId())) {
                        valecc = true;
                        break;
                    }
                }
                if (!valecc) {
                    p_valida = false;
                    JsfUtil.mensajeAdvertencia("El explosivo que intenta agregar no se encuentra registrado en las autorizaciones de comercialización de "
                            + "la empresa fabricante.");
                }
            }*/

            //VALIDA EXPLOSIVO DUPLICADO
            for (EppGteExplosivoSolicita es : currentBulk.getEppGteExplosivoSolicitaList()) {
                if (Objects.equals(p_v_explosivo.getId(), es.getExplosivoId().getId()) && es.getActivo() == 1) {
                    p_valida = false;
                    JsfUtil.mensajeAdvertencia("Explosivo duplicado en la lista. Debe verificar.");
                    break;
                }
            }

            //VERIFICANDO LA LISTA DE EXPLOSIVOS COMPATIBLES
            //EXPLOSIVO A AGREGAR: p_v_explosivo
            //BOOLEAN VALIDADOR TEMPORAL
            boolean val = true;
            boolean val2 = false;
            //LISTA EXPLOSIVOS AGREGADOS
            for (EppGteExplosivoSolicita es : currentBulk.getEppGteExplosivoSolicitaList()) {
                //LISTA COMPATIBLES POR CADA EXPLOSIVO AGREGADO
                if (es.getActivo() == 1) {
                    if (es.getExplosivoId().getEppExplosivoList() != null && !es.getExplosivoId().getEppExplosivoList().isEmpty()) {
                        for (EppExplosivo ecomp : es.getExplosivoId().getEppExplosivoList()) {
                            if (ecomp.getActivo() == 1) {
                                val2 = false;
                                //EVALUANDO SI ENCUENTRA EL EXPLOSIVO QUE SE ESTA AGREGANDO EN LA LISTA DE COMPATIBLES
                                val2 = Objects.equals(ecomp.getId(), p_v_explosivo.getId());
                                //SI LO ENCONTRO SALE DEL BUCLE
                                val = val2;
                                if (val2) {
                                    break;
                                }
                            }
                        }
                    } else {
                        val = false;
                    }
                }
                if (!val) {
                    break;
                }
            }
            //GENERA RESPUESTA SEGUN BOOLEAN RESULTADO
            if (!val) {
                p_valida = false;
                JsfUtil.mensajeAdvertencia("No se puede agregar: "
                        + p_v_explosivo.getNombre()
                        + " por que no es compatible con los explosivos "
                        + "ya agregados en la lista. Debe verificar.");
            }
        }

        return p_valida;
    }

    /**
     * BORRA LOS VALORES DEL PRODUCTO LUEGO DE QUE HAN SIDO AGREGADOS A LA LISTA
     */
    public void borrarValoresProducto() {
        explosivoSeleccionado = null;
        cantidadAutorizada = null;
        cantidadATransportar = null;
        saldoActualExplSolic = null;
    }

    /**
     * VALIDA SALDO POSITIVO
     *
     * @param cantactual
     * @param cantsolicitada
     * @return
     */
    public boolean validarSaldoExplosivo(double cantactual, double cantsolicitada) {
        try {
            if (resolucionGerencia == null) {
                return true;
            }
            if (resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_PLA")
                    || resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_AFAB")
                    || resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_COM")
                    || resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_ACOM")
                    || resolucionGerencia.getTipoProId().getCodProg().equals("TP_PRTUP_GUI")
                    || criterioResGuia.equals("10")) {
                return true;
            } else {
                String x = "";
                if (cantactual > 0) {
                    return ((cantactual - cantsolicitada) >= 0);
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            JsfUtil.mensajeError("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * BUSCA LA RESOLUCION REFERENCIADA
     */
    public void buscarResolucion() {
        itemsXGT = ejbRegistroFacade.buscarResolucionesGuiaExterna(filtro, empresa);
    }

    /**
     * BUSCA EL POLVORIN EN EL DIALOGO
     */
    public void buscarPolvorinGT() {
        String param = "" + JsfUtil.getSessionAttribute("actionODGt");
        if (filtro != null && !"".equals(filtro)) {
            if (param != null) {
                switch (param) {
                    case "1":
                        switch(criterioResGuia){
                            case "1":
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGt(filtro, null, ubigeo);
                                    break;
                            case "2":
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtTipo2(resolucionGerencia, filtro);
                                    break;
                            case "3":
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtDestino(filtro, resolucionGerencia, ubigeo);
                                    break;
                            case "5":
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtTipo5(filtro, currentBulk.getEmpresaId(), ubigeo);
                                    break;
                            case "6":
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtTipo5(filtro, resolucionGerencia.getEmpresaId(), ubigeo);
                                    break;
                            default:
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtTipo5(filtro, null, ubigeo);
                                    break;
                        }
                        break;
                    case "2":
                        switch(criterioResGuia){
                            case "1":
                            case "3":
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtDestino(filtro, resolucionGerencia, null);
                                    break;
                            case "2":
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtTipo2(resolucionGerencia, filtro);
                                    break;
                            case "5":
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtTipo5(filtro, currentBulk.getEmpresaId(), ubigeo);
                                    break;
                            case "6":    
                                    lstDlgMapPolvorin = ejbPolvorinFacade.buscarMapPolvorinGtTipo5(filtro, empresa, ubigeo);
                                    break;
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            JsfUtil.mensajeAdvertencia("Debe ingresar un criterio para la búsqueda");
        }
    }

    /**
     * SELECCIONA EL POLVORIN PARA AGREGARLO A LA LISTA
     *
     * @param polvMap
     */
    public void seleccionarDlgPolvorinGt(Map polvMap) {
        EppPolvorin polv = ejbPolvorinFacade.find(polvMap.get("id"));
        String param = "" + JsfUtil.getSessionAttribute("actionODGt");
        if (tipoOrigen != null && param.equals("1")) {
            if (tipoOrigen.getCodProg().equals("TP_ODRGT_POL")) {
                polvorin = polv;
                lugarUso = null;
                nombreEmpresaOrigen = polvorin.getDescripcion() + ((polvorin.getDireccion()!=null)?(", "+polvorin.getDireccion()):"");
                currentBulk.setOrigenPolvorin(polvorin);
                currentBulk.setLugarUsoOrigen(null);
                currentBulk.setLugarUsoUbigeoOrigen(null);
                currentBulk.setOrigenFabrica(null);
                currentBulk.setOrigenAlquilerId(null);
                if(polvMap.get("TIPO").toString().equals("2")){
                    currentBulk.setOrigenAlquilerId(ejbEppContratoAlquilerPolvorinFacade.buscarContratoById(((BigDecimal) polvMap.get("contratoId")).longValue()));
                } 
            }
        } else if (tipoDestino != null && param.equals("2")) {
            if (tipoDestino.getCodProg().equals("TP_ODRGT_POL")) {
                polvorin = polv;
                lugarUso = null;
                nombreEmpresaDestino = polvorin.getDescripcion() + ((polvorin.getDireccion()!=null)?(", "+polvorin.getDireccion()):"");
                currentBulk.setDestinoPolvorin(polvorin);
                currentBulk.setLugarUsoDestino(null);
                currentBulk.setLugarUsoUbigeoDestino(null);
                currentBulk.setDestinoFabrica(null);
                currentBulk.setDestinoAlquilerId(null);
                if(polvMap.get("TIPO").toString().equals("2")){
                    currentBulk.setDestinoAlquilerId(ejbEppContratoAlquilerPolvorinFacade.buscarContratoById(((BigDecimal) polvMap.get("contratoId")).longValue()));
                } 
            }
        }

        if (currentBulk.getId() == null) {
            RequestContext.getCurrentInstance().update("crearForm:pnlgOrigenDestino");
        } else {
            RequestContext.getCurrentInstance().update("editarForm:pnlgOrigenDestino");
        }
    }

    public void seleccionarDlgPolvorinDestinoGt(Map polvMap) {
        EppPolvorin polv = ejbPolvorinFacade.find(polvMap.get("id"));
        String param = "" + JsfUtil.getSessionAttribute("actionODGt");
        if (tipoDestino != null && param.equals("2")) {
            if (tipoDestino.getCodProg().equals("TP_ODRGT_POL")) {
                polvorin = polv;
                lugarUso = null;
                nombreEmpresaDestino = polvorin.getDescripcion() + ((polvorin.getDireccion()!=null)?(", "+polvorin.getDireccion()):"");
                currentBulk.setDestinoPolvorin(polvorin);
                currentBulk.setLugarUsoDestino(null);
                currentBulk.setLugarUsoUbigeoDestino(null);
                currentBulk.setDestinoFabrica(null);
                currentBulk.setDestinoAlquilerId(null);
                if(polvMap.get("TIPO").toString().equals("2")){
                    currentBulk.setDestinoAlquilerId(ejbEppContratoAlquilerPolvorinFacade.buscarContratoById(((BigDecimal) polvMap.get("contratoId")).longValue()));
                } 
            }
        }
        if (currentBulk.getId() == null) {
            RequestContext.getCurrentInstance().update("crearForm:pnlgOrigenDestino");
        } else {
            RequestContext.getCurrentInstance().update("editarForm:pnlgOrigenDestino");
        }
    }

    /**
     * RETORNA LA LISTA DE DEPARTAMENTOS
     *
     * @return
     */
    public List<SbDepartamentoGt> getLstDepartamentos() {
        return departamentoFacade.lstDepartamentos();
    }

    /**
     * RETORNA LA LISTA DE PROVINCIAS SEGUN DEPARTAMENTO
     */
    public void cargarProvincias() {
        if (dept != null) {
            lstProvincia = provinciaFacade.lstProvincias(dept);
        } else {
            lstProvincia = new ArrayList<>();
        }
    }

    /**
     * RETORNA LA LISTA DE DISTRITOS SEGUN PROVINCIA
     */
    public void cargarDistritos() {
        if (prov != null) {
            lstDistrito = distritoFacade.lstDistritos(prov);
        } else {
            lstDistrito = new ArrayList<>();
        }
    }

    /**
     * REALIZA LA BUSQUEDA DE UN LUGAR DE USO SEGUN DEPARTAMENTO O PROVINCIA O
     * DISTRITO
     */
    public void buscarLugarUso() {
        lstDlgLugarUso = ejbLugarUsoFacade.buscarLugarUso(filtro, ubigeo, resolucionGerencia);
    }

    /**
     * SELECCIONA UN LUGAR DE USO PARA AGREGARLO A LA GUIA DE TRANSITO
     *
     * @param lugUso
     */
    public void seleccionarDlgLugarUsoGt(EppLugarUso lugUso) {
        String param = "" + JsfUtil.getSessionAttribute("actionODGt");
        if (tipoOrigen != null && param.equals("1")) {
            if (tipoOrigen.getCodProg().equals("TP_ALMEMDE_LUSO")) {
                lugarUso = lugUso;
                nombreEmpresaOrigen = lugarUso.getNombre();
                currentBulk.setOrigenPolvorin(null);
                currentBulk.setOrigenFabrica(null);
                currentBulk.setOrigenAlquilerId(null);
                currentBulk.setLugarUsoOrigen(lugarUso);
                setMostrarLugarUsoUbigeoOrigen(true);
                lugarUsoUbigeoOrigen = null;
                currentBulk.setLugarUsoUbigeoOrigen(null);
                lstLugarUsoUbigeo = lugarUso.getEppLugarUsoUbigeoList();
                if(!criterioResGuia.equals("2")){
                    tipoDestino = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
                    setBloqueaSelectDestino(true);
                }
            }
        } else if (tipoDestino != null && param.equals("2")) {
            if (tipoDestino.getCodProg().equals("TP_ALMEMDE_LUSO")) {
                lugarUso = lugUso;
                nombreEmpresaDestino = lugarUso.getNombre();
                currentBulk.setDestinoPolvorin(null);
                currentBulk.setDestinoFabrica(null);
                currentBulk.setDestinoAlquilerId(null);
                currentBulk.setLugarUsoDestino(lugarUso);
                setMostrarLugarUsoUbigeoDestino(true);
                lugarUsoUbigeoDestino = null;
                currentBulk.setLugarUsoUbigeoDestino(null);
                lstLugarUsoUbigeo = lugarUso.getEppLugarUsoUbigeoList();
                if(!criterioResGuia.equals("2")){
                    tipoOrigen = ejbTipoExplosivoFacade.findByCodProg("TP_ODRGT_POL").get(0);
                    setBloqueaSelectOrigen(true);
                }
            }
        }
        if (currentBulk.getId() == null) {
            RequestContext.getCurrentInstance().update("crearForm:pnlgOrigenDestino");
        } else {
            RequestContext.getCurrentInstance().update("editarForm:pnlgOrigenDestino");
        }
    }

    /**
     * METODO QUE RETORNA LA LISTA DE LOS TIPOS DE VIA
     *
     * @return
     */
    public List<TipoExplosivoGt> getLstTiposVia() {
        return ejbTipoExplosivoFacade.lstTipoExplosivo("TP_PTOA");
    }

    /**
     * METODO QUE MUESTRA EL DIALOGO DE LA BUSQUEDA DE LAS EMPRESAS DE
     * TRANSPORTE
     */
    public void openDlgEmpresaTransporte() {
        filtro = null;
        lstDlgEmpresaTransp = null;
        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogEmpresaTransp').show()");
        RequestContext.getCurrentInstance().update("fmrDlgEmpTransp");
    }

    /**
     * METODO QUE REALIZA LA BUSQUEDA DE LA EMPRESA DE TRANSPORTE
     */
    public void buscarDlgEmpresaTransp() {
        if (filtro != null && !"".equals(filtro)) {
            lstDlgEmpresaTransp = ejbPersonaFacade.buscar(filtro.toUpperCase());
        } else {
            JsfUtil.mensajeAdvertencia("Debe ingresar un criterio para la búsqueda");
        }
    }

    /**
     * METODO QUE MUESTRA EL DIALOGO DE CREACION DE DOCUMENTO
     */
    public void openDlgDocumento() {
        tipodocumento = null;
        filtro = null;
        lstDlgDocumento = null;
        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogDocumento').show()");
        RequestContext.getCurrentInstance().update("frmDlgDocumento");
    }

    /**
     * METODO QUE REALIZA LA BUSQUEDA DEL DOCUMENTO
     */
    public void buscarDlgDocumentoGt() {
        lstDlgDocumento = ejbDocumentoFacade.buscarDocumentoGuiaTransitoExterna(tipodocumento.getCodProg(), filtro, empresa);
    }

    /**
     * METODO QUE SELECCIONA Y AGREGA EL DOCUMENTO A LA GUIA DE TRANSITO
     *
     * @param docgt
     */
    public void seleccionarDlgDocumentoGt(EppDocumento docgt) {

        if (lstDocumentosTemp == null) {
            lstDocumentosTemp = new ArrayList();
        }

        lstDocumentosTemp.add(docgt);
        /*
        if (lstDocAdjTemp == null) {
            lstDocAdjTemp = new ArrayList();
        }

        DocumentoTemp dtemp = new DocumentoTemp();
        dtemp.setId(docgt.getId());
        dtemp.setRuta(docgt.getRuta());
        dtemp.setUpfile(uploadedFileList.get(0));
        lstDocAdjTemp.add(dtemp);
         */
        if (currentBulk.getId() == null) {
            RequestContext.getCurrentInstance().update("crearForm:pnlgDocumentos");
        } else {
            RequestContext.getCurrentInstance().update("editarForm:pnlgDocumentos");
        }
    }

    /**
     * METODO PARA ELIMINAR LOS EXPLOSIVOS SELECCIONADOS
     */
    public void eliminarExplosivosSeleccionados() {
        if (!lstEliminadosExpSolic.isEmpty()) {
            for (EppGteExplosivoSolicita ls : lstEliminadosExpSolic) {
                for (EppGteExplosivoSolicita l : currentBulk.getEppGteExplosivoSolicitaList()) {
                    if (Objects.equals(ls.getId(), l.getId())) {
                        l.setActivo((short) 0);
                        ls.setActivo((short) 0);
                        if (currentBulk.getId() == null) {
                            currentBulk.getEppGteExplosivoSolicitaList().remove(l);
                        }
                        break;
                    }
                }
            }
            explosivoSeleccionado = null;
            cantidadATransportar = null;
            saldoActualExplSolic = null;
            RequestContext.getCurrentInstance().update("pnlProductos");
            JsfUtil.mensaje("EppRegistro(s) Eliminado(s)");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar un documento para eliminar");
        }
    }

    /**
     * METODO PARA REVERSAR LOS SALDOS EN LA INTERFAZ DE EDICION DE LA GUIA DE
     * TRANSITO
     */
    public void reversarSaldo() {
        for (EppGteExplosivoSolicita p_es : lstEliminadosExpSolic) {
            for (EppGteExplosivoSolicita es : currentBulk.getEppGteExplosivoSolicitaList()) {
                if (p_es.getId() != null) {
                    if ((Objects.equals(es, p_es)) && p_es.getActivo() == 1) {
                        if (p_es.getId() > 0) {
                            devolverSaldos(p_es);
                            p_es.setActivo((short) 0);
                            eppGteExplosivoSolicitaFacade.edit(p_es);
                        } else {
                            currentBulk.getEppGteExplosivoSolicitaList().remove(p_es);
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * METODO QUE MUESTRA SOLO LOS EXPLOSIVOS ACTIVOS EN LA GUIA DE TRANSITO
     *
     * @return
     */
    public List<EppExplosivoSolicitado> getLstExplosivosActivos() {
        List<EppExplosivoSolicitado> lsttemp = new ArrayList();
        if (current != null) {
            if (current.getEppExplosivoSolicitadoList() != null) {
                if (!current.getEppExplosivoSolicitadoList().isEmpty()) {
                    for (EppExplosivoSolicitado l : current.getEppExplosivoSolicitadoList()) {
                        if (l.getActivo() == 1) {
                            lsttemp.add(l);
                        }
                    }
                }
            }
        }
        return lsttemp;
    }

    public List<EppGteExplosivoSolicita> getLstExplosivosActivosBulk() {
        List<EppGteExplosivoSolicita> lsttemp = new ArrayList();
        if (currentBulk != null) {
            if (currentBulk.getEppGteExplosivoSolicitaList() != null) {
                if (!currentBulk.getEppGteExplosivoSolicitaList().isEmpty()) {
                    for (EppGteExplosivoSolicita l : currentBulk.getEppGteExplosivoSolicitaList()) {
                        if (l.getActivo() == 1) {
                            lsttemp.add(l);
                        }
                    }
                }
            }
        }
        return lsttemp;
    }

    /**
     * METODO PARA VALIDAR EL REGISTRO Y LA EDICION DE LA GUIA DE TRANSITO
     *
     * @return
     */
    private boolean validar() {
        boolean valida = true;
        if (criterioResGuia == null || criterioResGuia.equals("0")) {
            JsfUtil.mensajeAdvertencia("Debe ingresar el tipo de Guia");
            valida = false;
        }

        if (resolucionGerencia == null) {
            JsfUtil.mensajeAdvertencia("Debe ingresar la resolución de Gerencia");
            valida = false;
        }

        if (tipoOrigen == null) {
            JsfUtil.mensajeAdvertencia("Debe ingresar el Origen");
            valida = false;
        }
        if (tipoDestino == null) {
            JsfUtil.mensajeAdvertencia("Debe ingresar el Destino");
            valida = false;
        }

        if (tipoTransporte == null) {
            JsfUtil.mensajeAdvertencia("Debe ingresar el tipo de transporte");
            valida = false;
        }
        if (empresaTransporte == null) {
            JsfUtil.mensajeAdvertencia("Debe ingresar la empresa de transporte");
            valida = false;
        }

        if (current.getEppExplosivoSolicitadoList().isEmpty()) {
            JsfUtil.mensajeAdvertencia("Debe ingresar los explosivos");
            valida = false;
        }

        if (current.getRegistroId().getEppDocumentoList().isEmpty()) {
            JsfUtil.mensajeAdvertencia("Debe ingresar por lo menos un documento adjunto");
            valida = false;
        }

        return valida;
    }

    private boolean validarBulk() {
        boolean valida = true;
        int cont = 0;
        if (criterioResGuia == null || criterioResGuia.equals("0")) {
            JsfUtil.mensajeAdvertencia("Debe ingresar el tipo de Guia");
            valida = false;
        }else{
            if (!criterioResGuia.equals("5") && resolucionGerencia == null) {
                JsfUtil.mensajeAdvertencia("Debe ingresar la resolución de Gerencia");
                valida = false;
            }
            if(criterioResGuia.equals("6")){
                if(currentBulk.getOrigenPolvorin() != null){
                    if(Objects.equals( resolucionGerencia.getEmpresaId().getId(), currentBulk.getOrigenPolvorin().getPropietarioId().getId() )){
                         cont++;
                    }else{
                        if(currentBulk.getOrigenAlquilerId() != null){
                            if(Objects.equals( resolucionGerencia.getEmpresaId().getId(), currentBulk.getOrigenAlquilerId().getArrendatariaId().getId() )){
                                cont++;
                            }        
                        }else{
                            JsfUtil.mensajeError("Alquiler de polvorín de origen no encontrado");
                            valida = false;
                        }
                    }
                }else{
                    if(currentBulk.getOrigenFabrica() != null){
                        if(Objects.equals( resolucionGerencia.getEmpresaId().getId(), currentBulk.getOrigenFabrica().getPersonaId().getId() )){
                            cont++;
                        }else{
                            /// Fabricas alquiladas
                            if(currentBulk.getOrigenAlquilerId() != null){
                                if(Objects.equals( resolucionGerencia.getEmpresaId().getId(), currentBulk.getOrigenAlquilerId().getArrendatariaId().getId() )){
                                     cont++;
                                 }
                            }else{
                                 JsfUtil.mensajeError("Alquiler de fábrica de origen no encontrada");
                                 valida = false;
                             }    
                         }
                    } 
                }
                if(cont == 0){
                    JsfUtil.mensajeAdvertencia("El origen no debe ser una distinta empresa de la resolución seleccionada");
                    valida = false;
                }
                
                cont = 0;
                if(currentBulk.getDestinoPolvorin() != null){
                    if(Objects.equals( currentBulk.getEmpresaId().getId(), currentBulk.getDestinoPolvorin().getPropietarioId().getId() )){
                         cont++;
                    }else{
                        if(currentBulk.getDestinoAlquilerId() != null){
                            if(Objects.equals( currentBulk.getEmpresaId().getId(), currentBulk.getDestinoAlquilerId().getArrendatariaId().getId() )){
                                cont++;
                            }        
                        }else{
                            JsfUtil.mensajeError("Alquiler de polvorín de destino no encontrado");
                            valida = false;
                        }
                    }
                }else{
                    if(currentBulk.getDestinoFabrica() != null){
                        if(Objects.equals( currentBulk.getEmpresaId().getId(), currentBulk.getDestinoFabrica().getPersonaId().getId() )){
                            cont++;
                        }else{
                            if(currentBulk.getDestinoAlquilerId() != null){
                                if(Objects.equals( currentBulk.getEmpresaId().getId(), currentBulk.getDestinoAlquilerId().getArrendatariaId().getId() )){
                                    cont++;
                                }        
                            }else{
                                JsfUtil.mensajeError("Alquiler de fábrica de destino no encontrada");
                                valida = false;
                            }
                        }
                    }
                }
                if(cont == 0){
                    JsfUtil.mensajeAdvertencia("El destino debe ser un polvorín o fábrica de su empresa");
                    valida = false;
                }
            }
        }

        if (tipoOrigen == null) {
            JsfUtil.mensajeAdvertencia("Debe ingresar el Origen");
            valida = false;
        }
        if (tipoDestino == null) {
            JsfUtil.mensajeAdvertencia("Debe ingresar el Destino");
            valida = false;
        } else {
            String x = "";
            if (currentBulk.getLugarUsoDestino() != null) {
                if (currentBulk.getLugarUsoUbigeoDestino() == null) {
                    JsfUtil.mensajeAdvertencia("Debe ingresar el Ubigeo del Lugar de Uso Destino");
                    valida = false;
                }
            }
        }

        if (tipoTransporte == null) {
            JsfUtil.mensajeAdvertencia("Debe ingresar el tipo de transporte");
            valida = false;
        }

        if (currentBulk.getEppGteExplosivoSolicitaList().isEmpty()) {
            JsfUtil.mensajeAdvertencia("Debe ingresar los explosivos");
            valida = false;
        } else {
            int contes = 0;
            for (EppGteExplosivoSolicita es : currentBulk.getEppGteExplosivoSolicitaList()) {
                if (es.getActivo() == 1) {
                    contes++;
                }
            }
            if (contes == 0) {
                JsfUtil.mensajeAdvertencia("Debe ingresar los explosivos");
                valida = false;
            }
        }

        if (lstDocumentosTemp == null || lstDocumentosTemp.isEmpty()) {
            JsfUtil.mensajeAdvertencia("Debe ingresar por lo menos un documento adjunto");
            valida = false;
        }

        if (solicitarRecibo) {
            if (eligeReciboLista) {
                if(reciboBn == null){
                    JsfUtil.mensajeAdvertencia("Por favor ingresar el nro. de comprobante");
                    valida = false;
                }else{
                    if(!reciboBn.getNroDocumento().trim().equals(currentBulk.getEmpresaId().getRuc()) &&
                       !reciboBn.getNroDocumento().trim().equals(currentBulk.getEmpresaId().getNumDoc())){                        
                        JsfUtil.mensajeError("El recibo " + reciboBn.getNroSecuencia() + " le pertenece a otro administrado.");
                        return false;
                    }
                }
            } else {
                borrarValoresRecibo();
                setDisableReciboBn(false);
                JsfUtil.mensajeAdvertencia("Por favor ingresar el nro. de comprobante");
                valida = false;
            }
        }
        
        if(solicitarVoucherTemp){
            if(nroDeposito == null || nroDeposito <= 0){
                JsfUtil.mensajeAdvertencia("Por favor ingresar el nro. de depósito");
                valida = false;
            }else{
                cont = ejbEppGteDepositoFacade.contarDepositosGteByNroDeposito(nroDeposito, ((depositoTemp != null)?depositoTemp.getId():null) );
                if(cont > 0){
                    JsfUtil.mensajeAdvertencia("El nro. de depósito ingresado ya ha sido utilizado");
                    valida = false;    
                }
            }
            if(montoDeposito == null){
                JsfUtil.mensajeAdvertencia("Por favor ingresar el monto del depósito");
                valida = false;
            }else{
                if(!Objects.equals(montoDeposito.doubleValue(), parametrizacionTupa.getImporte().doubleValue())){                    
                    JsfUtil.mensajeAdvertencia("Monto del depósito incorrecto. El importe debe ser "+ parametrizacionTupa.getImporte());
                    montoDeposito = parametrizacionTupa.getImporte();
                    valida = false;
                }
            }
            if(fechaDeposito == null){
                JsfUtil.mensajeAdvertencia("Por favor ingresar la fecha del depósito");
                valida = false;
            }
        }
            

        return valida;
    }

    /**
     * METODO PARA ACTUALIZAR LOS SALDOS DE LAS RESOLUCION REFERENCIADA EN LA
     * GUIA DE TRANSITO
     */
    private void actualizarSaldos() {
        //ACTUALIZA LOS SALDOS EN AUTORIZACIONES
        //VERIFICAMOS EL TIPO DE AUTORIZACION
        if (resolucionGerencia != null) {
            switch (resolucionGerencia.getTipoProId().getCodProg()) {
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                case "TP_PRTUP_AAPOR":
                    for (EppExplosivoSolicitado e : current.getEppExplosivoSolicitadoList()) {
                        for (EppDetalleAutUso d : resolucionGerencia.getEppDetalleAutUsoList()) {
                            if (Objects.equals(e.getExplosivoId(), d.getExplosivoId()) && d.getActivo() == 1 && e.getActivo() == 1) {
                                d.setSaldo(d.getSaldo() - e.getCantidad());
                                ejbDetalleAutorizacionUsoFacade.edit(d);
                                break;
                            }
                        }

                        if (resolucionGerencia.getComId() != null) {
                            for (EppDetalleCom d : resolucionGerencia.getComId().getEppDetalleComList()) {
                                if (Objects.equals(e.getExplosivoId(), d.getExplosivoId()) && d.getActivo() == 1 && e.getActivo() == 1) {
                                    d.setSaldo(d.getSaldo() - e.getCantidad());
                                    ejbDetalleComFacade.edit(d);
                                    break;
                                }
                            }
                        }

                    }
                    break;
                default:
                    break;
            }
        } else {
            JsfUtil.mensaje("Sin saldos que descontar debido a que no tiene resolución");
        }
    }

    /**
     * METODO PARA MOSTRAR EQUIVALENCIA EN LOS TIPADOS DE LA GUIA DE TRANSITO
     *
     * @param p_t_base
     * @return
     */
    public String getTipoProIdGuiaTransito(String p_t_base) {
        String p_v_retorno = "";
        switch (p_t_base) {
            case "TP_PRTUP_GLO":
            case "TP_PRTUP_AAFOR":
                p_v_retorno = "TP_PRO_GLO";
                break;
            case "TP_PRTUP_EVE":
            case "TP_PRTUP_AAHID":
                p_v_retorno = "TP_PRO_EVE";
                break;
            case "TP_PRTUP_EXC":
            case "TP_PRTUP_AAPOR":
                p_v_retorno = "TP_PRO_EXC";
                break;            
            case "TP_PRTUP_PLA":
            case "TP_PRTUP_AFAB":
                p_v_retorno = "TP_PRO_PLA";
                break;
            case "TP_PRTUP_AINT":
            case "TP_PRTUP_INT":
                p_v_retorno = "TP_PRO_INT";
                break;
            case "TP_PRTUP_ASAL":
            case "TP_PRTUP_SAL":
                p_v_retorno = "TP_PRO_SAL";
                break;
            case "TP_PRTUP_COM":
            case "TP_PRTUP_ACOM":
                p_v_retorno = "TP_PRO_COM";
                break;
            case "TP_PRTUP_GUI":
                p_v_retorno = "TP_PRO_GT";
                break;
            default:
                p_v_retorno = "TP_PRO_PEN";
                break;
        }
        return p_v_retorno;
    }

    /**
     * METODO PARA CREAR UN REGISTRO EVENTO
     *
     * @param rrev
     */
    private void creaRegistroEvento(EppRegistro rrev) {
        //CREAMOS OBJ REGISTRO EVENTO
        EppRegistroEvento rev = new EppRegistroEvento();

        rev.setUserId(ejbUsuarioFacade.selectUsuario(JsfUtil.getLoggedUser().getLogin()).get(0));//USUARIO
        rev.setFecha(new Date());
        rev.setTipoEventoId(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
        rev.setRegistroId(rrev);
        rev.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        rev.setAudNumIp(JsfUtil.getIpAddress());
        //SI LA LISTA DE EVENTOS ESTA NULA LA INICIALIZAMOS
        if (rrev.getEppRegistroEventoList() == null) {
            rrev.setEppRegistroEventoList(new ArrayList());
        }
        //AGREGAMOS EL OBJETO REGISTRO EVENTO A LA LISTA DEL REGISTRO
        rrev.getEppRegistroEventoList().add(rev);
    }

    /**
     * METODO PARA CREAR LA GUIA DE TRANSITO
     *
     * @return
     */
    public String crear() {
        try {
            if (validarBulk()) {
                //ASIGNAR EXPEDIENTE
                //currentBulk.setNroExpediente("0");
                if (empresaTransporte != null) {
                    currentBulk.setEmpresaTranspId(empresaTransporte);
                }
                if (tipoTransporte != null) {
                    currentBulk.setTipoTransporte(tipoTransporte);
                }
                currentBulk.setTipoEstado(ejbTipoExplosivoFacade.findByCodProg("TP_ESTA_SINU").get(0));
                //currentBulk.setFechaFactura(fechaFactura);
                
                switch (criterioResGuia) {
                    case "1":
                        //ADQUISICION Y USO : FAB -> POLV
                        currentBulk.setResolucionId(resolucionGerencia.getEppResolucion());
                        currentBulk.setTipoProceso(ejbTipoExplosivoFacade.findByCodProg(getTipoProIdGuiaTransito(resolucionGerencia.getTipoProId().getCodProg())).get(0));
                        currentBulk.setTipoTramite(ejbTipoExplosivoFacade.findByCodProg("TP_RGUIA_ADQUI1").get(0));
                        break;
                    case "2":
                        //ADQUISICION Y USO : POLV>LU/LU>POLV
                        currentBulk.setResolucionId(resolucionGerencia.getEppResolucion());
                        currentBulk.setTipoProceso(ejbTipoExplosivoFacade.findByCodProg(getTipoProIdGuiaTransito(resolucionGerencia.getTipoProId().getCodProg())).get(0));
                        currentBulk.setTipoTramite(ejbTipoExplosivoFacade.findByCodProg("TP_RGUIA_ADQUI2").get(0));
                        break;
                    case "3":
                        //TRASLADO INTERNO
                        currentBulk.setResolucionId(resolucionGerencia.getEppResolucion());
                        currentBulk.setTipoProceso(ejbTipoExplosivoFacade.findByCodProg(getTipoProIdGuiaTransito(resolucionGerencia.getTipoProId().getCodProg())).get(0));
                        currentBulk.setTipoTramite(ejbTipoExplosivoFacade.findByCodProg("TP_RGUIA_INTER").get(0));
                        break;
                    case "4":
                        //DEVOLUCION
                        break;
                    case "5":
                        //TRASLADO PLANTA/PLANTA
                        currentBulk.setTipoProceso(ejbTipoExplosivoFacade.findByCodProg(getTipoProIdGuiaTransito("")).get(0));
                        currentBulk.setTipoTramite(ejbTipoExplosivoFacade.findByCodProg("TP_RGUIA_PLAPLA").get(0));
                        break;
                    case "6":
                        //COMERCIALIZACION ENTRE FABRICAS
                        currentBulk.setResolucionId(resolucionGerencia.getEppResolucion());
                        currentBulk.setTipoProceso(ejbTipoExplosivoFacade.findByCodProg(getTipoProIdGuiaTransito(resolucionGerencia.getTipoProId().getCodProg())).get(0));
                        currentBulk.setTipoTramite(ejbTipoExplosivoFacade.findByCodProg("TP_RGUIA_COMFAB").get(0));
                        break;
                    case "7": //INTERNAMIENTO
                    case "8": //SALIDA
                    case "9": //TRANSITO INTERNACIONAL
                    case "10": //ASIGNARA RG PENDIENTE ZPAE
                        break;
                    default:
                        break;
                }
                
                //CUSTODIO ? (CREATE)
                boolean reqCustodio = false;
                //REQUIERE CUSTODIO SEGUN TIPO_ID DE EXPLOSIVO
                for (EppGteExplosivoSolicita es : currentBulk.getEppGteExplosivoSolicitaList()) {
                    if (es.getActivo() == 1) {
                        if (es.getExplosivoId().getTipoId().getCodProg().equals("TP_CUS_SI")) {
                            reqCustodio = true;  //JMRM Cambio de estado por pandemia
                            break;
                        }
                    }
                }
                currentBulk.setCustodia(reqCustodio ? ((short) 1) : ((short) 0));

                //FORMATO: YYYY + ########
                 if(esGtSinTransf || 
                    currentBulk.getTipoTramite().getCodProg().equals("TP_RGUIA_INTER") || 
                    currentBulk.getTipoTramite().getCodProg().equals("TP_RGUIA_PLAPLA") ||
                    currentBulk.getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI2") ){
                     
                    SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
                    String anio = formatAnio.format(new Date());
                    String fechaFormat = sbNumeracionFacade.buscarNumeracionActual("TP_NUM_GTE").toString();
                    fechaFormat = StringUtils.leftPad(fechaFormat, 8, "0");
                    currentBulk.setNroSolicitud(anio + fechaFormat);
                }
                
                //BORRANDO IDS TEMPORALES DE LISTAS
                JsfUtil.borrarIds(currentBulk.getEppGteExplosivoSolicitaList());

                //SEPARAMOS LOS DOCUMENTOS EXISTENTES
                List<EppDocumento> lstDocsExistentes = new ArrayList();
                List<EppDocumento> lstDocsNoExistentes = new ArrayList();

                for (EppDocumento dt : lstDocumentosTemp) {
                    if (dt.getId() > 0) {
                        lstDocsExistentes.add(dt);
                    } else {
                        lstDocsNoExistentes.add(dt);
                    }
                }

                //BORRANDO IDS DE DOCUMENTOS TEMPORALES
                JsfUtil.borrarIds(lstDocsNoExistentes);
                currentBulk.setEppDocumentoList(lstDocsNoExistentes);

                //Se utiliza el recibo
                if(reciboBn != null){
                    currentBulk.setSbRecibosList(new ArrayList());
                    currentBulk.getSbRecibosList().add(reciboBn);
                }
                
                // Voucher temporal
                if(solicitarVoucherTemp){
                    EppGteDeposito deposito = new EppGteDeposito();
                    deposito.setActivo(JsfUtil.TRUE);
                    deposito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    deposito.setAudNumIp(JsfUtil.getIpAddress());
                    deposito.setNroDeposito(nroDeposito);
                    deposito.setFechaDeposito(fechaDeposito);
                    deposito.setGteRegistroId(currentBulk);
                    deposito.setId(null);
                    deposito.setMontoDeposito(montoDeposito);
                    deposito.setImagenDeposito("DEP_"+nroDeposito+"_"+(new Date()).getTime() + depositoAdjunto.substring(depositoAdjunto.lastIndexOf("."), depositoAdjunto.length()));
                    deposito = (EppGteDeposito) JsfUtil.entidadMayusculas(deposito, "");
                    
                    currentBulk.setEppGteDepositoList(new ArrayList());
                    currentBulk.getEppGteDepositoList().add(deposito);                    
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_depositoGte").getValor() + deposito.getImagenDeposito() ), depositoByte);
                }
                    
                eppGteRegistroFacade.create(currentBulk);
                JsfUtil.mensaje("Solicitud de GTE creada correctamente, " + "Nro: " + currentBulk.getId());
                //actualizarSaldos();
                //JsfUtil.mensaje("Se actualizaron los saldos de la Resolución");
                //SE UTILIZA EL RECIBO
                if(reciboBn != null){
                    //reciboBn.setRegistroId(currentBulk.getRegistroId());
                    SbReciboRegistro rec = new SbReciboRegistro();
                    rec.setId(null);
                    rec.setRegistroId(null);
                    rec.setNroExpediente("GTE-"+currentBulk.getId());
                    rec.setReciboId(reciboBn);
                    rec.setActivo(JsfUtil.TRUE);
                    ejbSbReciboRegistroFacade.create(rec);
                }                    

                //GRABANDO DOCUMENTOS YA EXISTENTES
                for (EppDocumento de : lstDocsExistentes) {
                    currentBulk.getEppDocumentoList().add(de);
                }
                eppGteRegistroFacade.edit(currentBulk);

                //SUBIENDO ADJUNTOS DE DOCUMENTOS
                if (lstDocAdjTemp != null && !lstDocAdjTemp.isEmpty()) {
                    for (EppDocumento d : currentBulk.getEppDocumentoList()) {
                        for (DocumentoTemp dtmp : lstDocAdjTemp) {
                            if (d.getRuta().equals(dtmp.getRuta())) {
                                uploadFilesController.uploadDocumento(dtmp.getUpfile(), dtmp.getRuta(), true);
                            }
                        }
                    }
                }

                switch (criterioResGuia) {
                    case "1":
                    case "6":
                            return direccionarListadoGTUsua();
                    case "2":
                    case "3":
                    case "5":
                            return direccionarGTESinTransfer();
                }
                return direccionarGTESinTransfer();
            } else {
                return "/aplicacion/eppRegistroGuiaTransito/Create";
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrio un error al grabar la guia de transito, ERROR: " + e.getMessage());
            return "/aplicacion/eppRegistroGuiaTransito/Create";
        }
    }

    /**
     * METODO PARA EDITAR LA GUIA DE TRANSITO
     *
     * @return
     */
    public String editar() {
        try {
            if (validarBulk()) {
                //GRABANDO DATOS DE LA GUIA DE TRANSITO
                //current.setFechaLlegada(fechaLlegada);
                //current.setFechaSalida(fechaSalida);
                //current.setFechaFactura(fechaFactura);

                //ACTUALIZANDO EMPRESA DE TRANSPORTE
                if (empresaTransporte != null) {
                    currentBulk.setEmpresaTranspId(empresaTransporte);
                }
                if (tipoTransporte != null) {
                    currentBulk.setTipoTransporte(tipoTransporte);
                }

                //CUSTODIA ? (UPDATE) 
                boolean reqCustodio = false;
                for (EppGteExplosivoSolicita es : currentBulk.getEppGteExplosivoSolicitaList()) {
                    if (es.getActivo() == 1) {
                        if (es.getExplosivoId().getTipoId().getCodProg().equals("TP_CUS_SI")) {
                            reqCustodio = false;  //JNG Cambio de urgencia por pandemia COVID 19 //true;
                            break;
                        }
                    }
                }
                currentBulk.setCustodia(reqCustodio == true ? ((short) 1) : ((short) 0));

                //BORRANDO IDS TEMPORALES DE LISTAS
                JsfUtil.borrarIds(currentBulk.getEppGteExplosivoSolicitaList());

                /*List<EppGteExplosivoSolicita> lstEsEdit = new ArrayList();
                for (EppGteExplosivoSolicita es : currentBulk.getEppGteExplosivoSolicitaList()) {
                    if (es.getId() == null && es.getActivo() == 1) {
                        lstEsEdit.add(es);
                    }
                }*/
                //BORRANDO IDS DE DOCUMENTOS TEMPORALES
                JsfUtil.borrarIds(lstDocumentosTemp);
                currentBulk.setEppDocumentoList(lstDocumentosTemp);
                
                //Se utiliza el recibo
                if(reciboBn != null){
                    currentBulk.setSbRecibosList(new ArrayList());
                    currentBulk.getSbRecibosList().add(reciboBn);
                    
                    if(reciboBnTemp == null){
                        // No ha tenido recibo antes
                        if(reciboBn != null){
                            SbReciboRegistro rec = new SbReciboRegistro();
                            rec.setId(null);
                            rec.setRegistroId(null);
                            rec.setNroExpediente("GTE-"+currentBulk.getId());
                            rec.setReciboId(reciboBn);
                            rec.setActivo(JsfUtil.TRUE);
                            ejbSbReciboRegistroFacade.create(rec);
                        }
                    }else{
                        if(!Objects.equals(reciboBnTemp.getId(),reciboBn.getId() )){
                            SbReciboRegistro rec = ejbSbReciboRegistroFacade.buscarReciboRegistroByExpediente("GTE-"+currentBulk.getId());
                            rec.setReciboId(reciboBn);
                            ejbSbReciboRegistroFacade.edit(rec);
                        }
                    }
                }
                if( depositoTemp != null && nroDeposito != null && montoDeposito != null){
                    for(EppGteDeposito deposito : currentBulk.getEppGteDepositoList()){
                        if(deposito.getActivo() == 0){
                            continue;
                        }                        
                        deposito.setMontoDeposito(montoDeposito);
                        deposito.setFechaDeposito(fechaDeposito);
                        
                        if(!Objects.equals(nroDeposito, depositoTemp.getNroDeposito())){
                            deposito.setNroDeposito(nroDeposito);    
                            deposito.setImagenDeposito("DEP_"+nroDeposito+"_"+(new Date()).getTime() + depositoAdjunto.substring(depositoAdjunto.lastIndexOf("."), depositoAdjunto.length()));
                        }
                        if(!Objects.equals(depositoAdjunto, depositoAdjuntoTemp)){
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_depositoGte").getValor() + deposito.getImagenDeposito() ), depositoByte);
                        }   
                        break;
                    }
                }

                //EDITANDO
                eppGteRegistroFacade.edit(currentBulk);
                JsfUtil.mensaje("Solicitud de GTE editada correctamente, " + "Nro: " + currentBulk.getId());

                //SUBIENDO ADJUNTOS DE DOCUMENTOS
                if (lstDocAdjTemp != null && !lstDocAdjTemp.isEmpty()) {
                    for (EppDocumento d : currentBulk.getEppDocumentoList()) {
                        for (DocumentoTemp dtmp : lstDocAdjTemp) {
                            if (d.getRuta().equals(dtmp.getRuta())) {
                                uploadFilesController.uploadDocumento(dtmp.getUpfile(), dtmp.getRuta(), true);
                            }
                        }
                    }
                }

                /*if (!lstEsEdit.isEmpty()) {
                    actualizarSaldosEdit(lstEsEdit);
                    //JsfUtil.mensaje("Se actualizaron los saldos de la Resolución");
                }*/                
                switch (currentBulk.getTipoTramite().getCodProg()) {
                    case "TP_RGUIA_ADQUI1":
                    case "TP_RGUIA_COMFAB":
                            return direccionarListadoGTUsua();
                    case "TP_RGUIA_ADQUI2":
                    case "TP_RGUIA_INTER":
                    case "TP_RGUIA_PLAPLA":
                            return direccionarGTESinTransfer();
                }
                return direccionarListadoGTUsua();
            } else {
                return "/aplicacion/eppRegistroGuiaTransito/Edit";
            }
        } catch (Exception e) {
            e.printStackTrace();
            /*Exception cause = e.getCausedByException();
            if (cause instanceof ConstraintViolationException) {
                @SuppressWarnings("ThrowableResultIgnored")
                ConstraintViolationException cve = (ConstraintViolationException) e.getCausedByException();
                for (ConstraintViolation<? extends Object> v : cve.getConstraintViolations()) {
                    System.err.println(v);
                    System.err.println("==>>" + v.getMessage());
                }
            }*/
            JsfUtil.mensajeError("Ocurrio un error al editar la guia de transito, ERROR: " + e.getMessage());
            return "/aplicacion/eppRegistroGuiaTransito/Edit";
        }
    }

    public String grabarDatosAdicionales() {
        try {
            if (validaDatosAdic()) {
                List<EppGuiaTransitoPolicia> lstPolicias = new ArrayList();
                JsfUtil.borrarIds(current.getEppGuiaTransitoPoliciaList());
                for(EppGuiaTransitoPolicia polic :  current.getEppGuiaTransitoPoliciaList()){
                    if(polic.getId() == null){
                        eppGuiaTransitoPoliciaFacade.create(polic);
                        lstPolicias.add(polic);
                    }else{
                        EppGuiaTransitoPolicia policitaTemp = eppGuiaTransitoPoliciaFacade.buscarPoliciaById(polic.getId());
                        if(policitaTemp != null){
                            if( !Objects.equals(policitaTemp.getGradoId(), polic.getGradoId()) || !Objects.equals(policitaTemp.getNroPlaca(), polic.getNroPlaca())){
                                // Se da de baja al anterior
                                policitaTemp.setActivo(JsfUtil.FALSE);
                                eppGuiaTransitoPoliciaFacade.edit(policitaTemp);
                                
                                // se crea un nuevo registro
                                polic.setId(null);
                                eppGuiaTransitoPoliciaFacade.create(polic);
                                lstPolicias.add(polic);
                            }
                            lstPolicias.add(polic);
                        }
                    }
                }
                if(!Objects.equals(archivoTemporal, archivo)){
                    String fileName = file.getFileName();
                    String nombreAdj = "ASISTENCIA_" + current.getId() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                    current.setDocAdjunto(nombreAdj.toUpperCase());
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_guiasExp").getValor() +  current.getDocAdjunto() ), fotoByte );
                }
                
                current.setEppGuiaTransitoPoliciaList(lstPolicias);
                current.setFechaLlegada(fechaLlegada);
                ejbRegistroFacade.edit(current.getRegistroId());
                JsfUtil.mensaje("Datos adicionales grabados correctamente. Id: " + current.getRegistroId().getId());
                return direccionarDatosAdiGTE();
            } else {
                return "/aplicacion/eppRegistroGuiaTransito/ViewDatosAdicGt";
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrio un error al grabar los datos, ERROR: " + e.getMessage());
            return "/aplicacion/eppRegistroGuiaTransito/ViewDatosAdicGt";
        }
    }

    private boolean validaDatosAdic() {
        boolean vda = true;
        if ((current.getCustodia() == 1) && (current.getEppGuiaTransitoPoliciaList() == null || current.getEppGuiaTransitoPoliciaList().isEmpty()) ) {
            vda = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar los datos del custódio.");
        }
        if(fechaLlegada == null){
            vda = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar la fecha de llegada");
        }
        return vda;
    }

    /**
     * ACTUALIZA SOLO LOS EDITADOS DEL LISTADO DE EXPLOSIVOS
     *
     * @param p_lst_edit
     */
    private void actualizarSaldosEdit(List<EppExplosivoSolicitado> p_lst_edit) {
        //ACTUALIZA LOS SALDOS EN AUTORIZACIONES
        //VERIFICAMOS EL TIPO DE AUTORIZACION
        if (resolucionGerencia != null) {
            switch (resolucionGerencia.getTipoProId().getCodProg()) {
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                case "TP_PRTUP_AAPOR":
                    for (EppExplosivoSolicitado e : p_lst_edit) {
                        for (EppDetalleAutUso d : resolucionGerencia.getEppDetalleAutUsoList()) {
                            if (e.getId() == null && Objects.equals(e.getExplosivoId(), d.getExplosivoId()) && d.getActivo() == 1 && e.getActivo() == 1) {
                                d.setSaldo(d.getSaldo() - e.getCantidad());
                                ejbDetalleAutorizacionUsoFacade.edit(d);
                                break;
                            }
                        }
                        for (EppDetalleCom d : resolucionGerencia.getComId().getEppDetalleComList()) {
                            if (e.getId() == null && Objects.equals(e.getExplosivoId(), d.getExplosivoId()) && d.getActivo() == 1 && e.getActivo() == 1) {
                                d.setSaldo(d.getSaldo() - e.getCantidad());
                                ejbDetalleComFacade.edit(d);
                                break;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            JsfUtil.mensaje("Explosivo eliminado sin devolver saldos debido a EppResolucion Pendiente");
        }
    }

    /**
     * METODO PARA TRANSFERIR LOS REGISTROS SELECCIONADOS AL FABRICANTE
     */
    public void transferirFabricante() {
        try {
            if (flgAceptoTransmitir) {
                if (!registrosGteRegSeleccionados.isEmpty()) {
                    /*for (EppRegistroGuiaTransito rgt : registrosSeleccionados) {
                    rgt.getRegistroId().setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_TRAN").get(0));
                    ejbRegistroFacade.edit(rgt.getRegistroId());
                    JsfUtil.mensaje("EppRegistro ID: " + rgt.getRegistroId().getId() + " transferido correctamente.");
                }*/
                    boolean validacion;
                    for (EppGteRegistro rgt : registrosGteRegSeleccionados) {
                        
                        ////// Validación de RG referenciada MEMO 1160-2017 //////
                        validacion = true;
                        if(rgt.getResolucionId() != null){
                            if(rgt.getResolucionId().getActivo() == 0 || rgt.getResolucionId().getRegistroId().getActivo() == 0){
                                validacion = false;
                            }
                        }
                        if(validacion){
                        /////////
                            //FORMATO: YYYY + ########
                            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
                            String anio = formatAnio.format(new Date());
                            String fechaFormat = sbNumeracionFacade.buscarNumeracionActual("TP_NUM_GTE").toString();
                            fechaFormat = StringUtils.leftPad(fechaFormat, 8, "0");
                            rgt.setNroSolicitud(anio + fechaFormat);
                            rgt.setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_TRAN").get(0));
                            eppGteRegistroFacade.edit(rgt);
                            
                            /// Subir solicitud GTE ///
                            subirSolicitudPdf(rgt);
                            ///////////////////////////
                            
                            JsfUtil.mensaje("Solicitud de GTE ID: " + rgt.getId() + " transferido correctamente.");
                        }else{
                            JsfUtil.mensajeAdvertencia("No se puede transferir la solicitud de GTE ID: " + rgt.getId() + " porque tiene la RG inactiva.");
                        }
                        /////////
                    }

                    RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmTransmitir').hide()");
                    RequestContext.getCurrentInstance().update("listForm");
                } else {
                    JsfUtil.mensajeAdvertencia("Debe seleccionar algún documento para transferirlo al fabricante");
                }
                resultadosGteReg = null;
                registrosGteRegSeleccionados = null;
                resultadosGuiaGte = null;
                tipoEstadoBandeja = null;
                tipoBusqueda = null;
                filtro = null;                
            } else {
                JsfUtil.mensajeAdvertencia("Usted no ha aceptado los terminos establecidos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * METODO PARA MOSTRAR EL NUMERO DE EXPEDIENTE
     *
     * @param id
     * @return
     */
    public String mostrarNumeroExpediente(String id) {
        try {
            return ejbExpedienteFacade.mostrarNumeroExpediente(id);
        } catch (Exception e) {
            return "-x-";
        }
    }

    /**
     * METODO QUE MUESTRA EL NUMERO DEL RECIBO BN
     *
     * @param rc
     * @return
     */
    public String mostrarNroComprobante(EppRegistro rc) {
        if (sbRecibosFacade.buscarReciboXIdRegistro(rc) != null) {
            SbRecibos r = sbRecibosFacade.buscarReciboXIdRegistro(rc);
            return "" + r.getNroSecuencia();
        } else {
            return "";
        }
    }

    /**
     * METODO QUE MUESTRA EL NUMERO DEL RECIBO BN
     *
     * @param rc
     * @return
     */
    public String mostrarFechaComprobante(EppRegistro rc) {
        if (sbRecibosFacade.buscarReciboXIdRegistro(rc) != null) {
            SbRecibos r = sbRecibosFacade.buscarReciboXIdRegistro(rc);
            return "" + mostrarFechaDdMmYyyy(r.getFechaMovimiento());
        } else {
            return "";
        }
    }

    /**
     * METODO PARA ELIMINAR LAS GUIAS SELECCIONADAS
     */
    public void eliminarGuiasSeleccionadasUsua() {

    }

    //////////////////////////////////////////////////
    ////////////  BANDEJA FABRICANTE  ////////////////
    //////////////////////////////////////////////////
    private boolean validarEmision() {
        boolean valida = true;

        if (currentBulk.getEppGuiaTransitoVehiculoList().isEmpty()) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el vehiculo");
        }

        if (currentBulk.getEppLicenciaList1().isEmpty()) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el vehiculo");
        }

        if (currentBulk.getEmpresaTranspId() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar la empresa de transporte.");
        }
        EppGteRegistro solicitudGte = eppGteRegistroFacade.find(currentBulk.getId());
        if(solicitudGte != null){
            if(solicitudGte.getRegistroGtId() != null){
                valida = false;
            }
        }else{
            valida = false;
            JsfUtil.mensajeAdvertencia("No se encontró datos de la solicitud de guía de tránsito");
        }
        return valida;
    }

    /**
     * METODO PAR APROBAR LA EMISION DE GUIA DE TRANSITO DEL ADMINISTRADO
     *
     * @return
     */
    public String aprobarEmision() {
        try {
            if (validarEmision()) {
                //EppRegistroGuiaTransito varrgt = new EppRegistroGuiaTransito();
                //loadEppRegistroGuiaTransito(varrgt, currentBulk);
                //varrgt = current;
                //1.GENERO EXPEDIENTE
                String ne = generarExpedienteBulk(currentBulk);
                if (ne != null) {
                    Expediente e = ejbExpedienteFacade.obtenerExpedienteXNumero(ne);
                    //2.ACTUALIZO EL NUMERO DE EXPEDIENTE EN EL REGISTRO
                    if (e != null) {
                        //--pendiente de setear nro expediente al registro
                        //varrgt.getRegistroId().setNroExpediente(e.getNumero());
                        //3.ACTUALIZO RECIBO CON NRO DE EXPEDIENTE
                        //SbRecibos sbr = currentBulk.getSbRecibosList().get(0);//sbRecibosFacade.buscarReciboXIdRegistro(varrgt.getRegistroId());
                        //sbr.setNroExpediente(e.getNumero());
                        //sbRecibosFacade.edit(sbr);
                        //4.GENERO RESOLUCION
                        String nroResol = generarResolucionBulk(e);
                        if (nroResol != null) {
                            if (loadEppRegistroGuiaTransito(currentBulk, ne, nroResol)) {
                                return direccionarGT();
                            } else {
                                return null;
                            }
                        } else {
                            JsfUtil.mensajeError("No se pudo generar la resolución para el registro: " + currentBulk.getId());
                        }
                    } else {
                        JsfUtil.mensajeError("El Nro de Expediente: " + ne + " no se ha encontrado en la Base de Datos del Sistema."
                                + " Debe comunicarse con el Administrador del Sistema. ID EppRegistro: " + currentBulk.getId());
                    }
                } else {
                    JsfUtil.mensajeError("No se pudo generar el expediente para el registro: " + currentBulk.getId());
                }

                resultados = null;
                registrosSeleccionados = null;
                resultadosGuia = null;
                resultadosGuiaSeleccionados = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al grabar el registro: " + e.getMessage());
        }
        return null;
    }

    /**
     * Metodo para pasar los valores del borrador al registro definitivo
     *
     * @param cb
     * @param nroExpediente
     * @param nroResolucion
     * @return
     */
    private boolean loadEppRegistroGuiaTransito(EppGteRegistro cb, String nroExpediente, String nroResolucion) {
        boolean ok = true;
        try {
            //AGREGANDO DATOS A LA GUIA DE TRANSITO
            EppRegistroGuiaTransito registro = new EppRegistroGuiaTransito();
            //POLICIA
            registro.setEppGuiaTransitoPoliciaList(new ArrayList<EppGuiaTransitoPolicia>());
            //VEHICULOS
            registro.setEppGuiaTransitoVehiculoList(new ArrayList<EppGuiaTransitoVehiculo>());
            //CUSTODIOS
            registro.setEppLicenciaList(new ArrayList<EppLicencia>());
            //CONDUCTORES
            registro.setEppLicenciaList1(new ArrayList<EppLicencia>());
            //EXPLOSIVOS
            registro.setEppExplosivoSolicitadoList(new ArrayList<EppExplosivoSolicitado>());
            //CARGANDO DATOS GUIA TRANSITO
            registro.setEmpresaId(cb.getEmpresaId());
            registro.setEmpresaTranspId(cb.getEmpresaTranspId());
            registro.setTipoTransporte(cb.getTipoTransporte());
            registro.setTipoEstado(cb.getTipoEstado());
            registro.setResolucionId(cb.getResolucionId());
            registro.setTipoProceso(cb.getTipoProceso());
            registro.setTipoTramite(cb.getTipoTramite());
            registro.setCustodia(cb.getCustodia());
            //GRABANDO DATOS DE ORIGEN Y DESTINO
            registro.setOrigenPolvorin(cb.getOrigenPolvorin());
            registro.setDestinoPolvorin(cb.getDestinoPolvorin());
            registro.setLugarUsoDestino(cb.getLugarUsoDestino());
            registro.setLugarUsoUbigeoDestino(cb.getLugarUsoUbigeoDestino());
            registro.setOrigenFabrica(cb.getOrigenFabrica());
            registro.setDestinoFabrica(cb.getDestinoFabrica());
            registro.setOrigenAlquiler(cb.getOrigenAlquilerId());
            registro.setDestinoAlquiler(cb.getDestinoAlquilerId());

            registro.setLugarUsoOrigen(cb.getLugarUsoOrigen());
            registro.setLugarUsoUbigeoOrigen(cb.getLugarUsoUbigeoOrigen());
            registro.setOrigenAlmacen(cb.getOrigenAlmacen());
            registro.setOrigenAlmacenAduana(cb.getOrigenAlmacenAduana());
            registro.setDestinoAlmacen(cb.getDestinoAlmacen());
            registro.setDestinoAlmacenAduana(cb.getDestinoAlmacenAduana());
            
            //GRABANDO DATOS DE AUDITORIA
            registro.setAudLogin(cb.getAudLogin());
            registro.setAudNumIp(cb.getAudNumIp());
            registro.setActivo(cb.getActivo());

            //CARGANDO DATOS DE CONDUCTORES
            registro.setEppLicenciaList1(cb.getEppLicenciaList1());

            //CARGANDO DATOS DE VEHICULOS
            registro.setEppGuiaTransitoVehiculoList(cb.getEppGuiaTransitoVehiculoList());

            //AGREGANDO EL DETALLE DE EXPLOSIVOS
            for (EppGteExplosivoSolicita es : cb.getEppGteExplosivoSolicitaList()) {
                EppExplosivoSolicitado exs = new EppExplosivoSolicitado();
                exs.setActivo(es.getActivo());
                exs.setAudLogin(es.getAudLogin());
                exs.setAudNumIp(es.getAudNumIp());
                exs.setExplosivoId(es.getExplosivoId());
                exs.setCantidad(es.getCantidad());
                ////// CALCULANDO EL SALDO /////
                //Double sld = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoRgGee(cb.getResolucionId(), es.getExplosivoId());
                Double sld = calcularSaldoActualEmitir(cb, es.getExplosivoId());
                exs.setCantidadAutorizada(sld);
                if(validarSaldoExplosivoPreview(cb)){
                    if (sld < 0.0 || (sld - exs.getCantidad() < 0.0)) {
                        ok = false;
                        JsfUtil.mensajeAdvertencia("El explosivo " + es.getExplosivoId().getNombre() + " no cuenta con saldo suficiente.");
                        return ok;
                    }
                }
                ////////////////////////////////
                exs.setRegistroId(registro);
                registro.getEppExplosivoSolicitadoList().add(exs);
            }

            //GENERANDO ENTIDAD REGISTRO    
            registro.setRegistroId(new EppRegistro());
            //AGREGANDO LOS VALORES POR CAMPO            
            registro.getRegistroId().setTipoProId(cb.getTipoProId());
            registro.getRegistroId().setTipoRegId(cb.getTipoRegId());
            registro.getRegistroId().setTipoOpeId(cb.getTipoOpeId());
            registro.getRegistroId().setEmpresaId(cb.getEmpresaId());
            registro.getRegistroId().setFecha(new Date());
            registro.getRegistroId().setNroExpediente(nroExpediente);
            registro.getRegistroId().setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_FIN").get(0));
            registro.getRegistroId().setAudLogin(cb.getAudLogin());
            registro.getRegistroId().setAudNumIp(cb.getAudNumIp());
            registro.getRegistroId().setActivo(cb.getActivo());

            //AGREGANDO LOS DOCUMENTOS
            registro.getRegistroId().setEppDocumentoList(cb.getEppDocumentoList());

            //GENERANDO LA RESOLUCION
            registro.getRegistroId().setEppResolucion(new EppResolucion());
            registro.getRegistroId().getEppResolucion().setActivo((short) 1);
            registro.getRegistroId().getEppResolucion().setAudLogin(JsfUtil.getLoggedUser().getLogin());
            registro.getRegistroId().getEppResolucion().setAudNumIp(JsfUtil.getIpAddress());
            registro.getRegistroId().getEppResolucion().setRegistroId(registro.getRegistroId());
            registro.getRegistroId().getEppResolucion().setFecha(new Date());
            registro.getRegistroId().getEppResolucion().setFechaIni(new Date());
            //INICIO ESTABLECIENDO VIGENCIA DE 30 DIAS
            Calendar hoy = Calendar.getInstance(new Locale("ES"));
            hoy.setTime(registro.getRegistroId().getEppResolucion().getFechaIni());
            hoy.add(Calendar.DAY_OF_YEAR, 30);
            int diasAdic = sbFeriadoFacade.diasNoLaborables(registro.getRegistroId().getEppResolucion().getFechaIni(), hoy.getTime());
            hoy.setTime(new Date());
            hoy.add(Calendar.DAY_OF_YEAR, (30 + (diasAdic > 0 ? (diasAdic) : 0)));
            registro.getRegistroId().getEppResolucion().setFechaFin(hoy.getTime());
            //FIN ESTABLECIENDO VIGENCIA DE 30 DIAS
            registro.getRegistroId().getEppResolucion().setEstadoId(ejbTipoExplosivoFacade.findByCodProg("TP_RESEV_APR").get(0));
            registro.getRegistroId().getEppResolucion().setNumero(nroResolucion);
            registro.getRegistroId().getEppResolucion().setHashQr(JsfUtil.crearHash("SUCAMEC" + registro.getRegistroId().getEppResolucion().getNumero()));

            //REFERENCIANDO REGISTRO CON REGISTRO_GUIA_TRANSITO
            registro.getRegistroId().setEppRegistroGuiaTransito(registro);

            if (ok) {
                //GUARDANDO TODO EN UNA SOLA TRANSACCION
                ejbRegistroFacade.create(registro.getRegistroId());
                
                //FINALIZO REGISTRO BORRADOR
                cb.setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_FIN").get(0));
                cb.setRegistroGtId(registro.getRegistroId());
                eppGteRegistroFacade.edit(cb);   
                
                //ARCHIVANDO EXPEDIENTE 
                registraTrazaExpediente(registro.getRegistroId());

                //ACTUALIZANDO EL NUMERO DE EXPEDIENTE A LOS DOCUMENTOS
                for (EppDocumento d : registro.getRegistroId().getEppDocumentoList()) {
                    if (d.getActivo() == 1) {
                        d.setNroExpediente(nroExpediente);
                        ejbDocumentoFacade.edit(d);
                    }
                }

                //COPIAR DOCUMENTOS ADJUNTOS DE TEMPORAL A CARPETA OFICIAL
                for (EppDocumento d : registro.getRegistroId().getEppDocumentoList()) {
                    if (d.getActivo() == 1) {
                        uploadFilesController.moveFile(d.getRuta(), d.getRuta());
                    }
                }

                //ACTUALIZA RECIBO CON REFERENCIA HACIA REGISTRO
                // Actualización del recibo
                if(cb.getSbRecibosList() != null && !cb.getSbRecibosList().isEmpty()){
                    SbReciboRegistro rec = ejbSbReciboRegistroFacade.buscarReciboRegistroByExpediente("GTE-"+cb.getId());
                    if(rec != null){
                        rec.setRegistroId(registro.getRegistroId());
                        rec.setNroExpediente(nroExpediente);
                        ejbSbReciboRegistroFacade.edit(rec);
                    }
                }
                
                if(esGtSinTransf){
                    // Para subir solicitud al emitir la guía 
                    subirSolicitudPdf(cb);
                }
                
                //              
                JsfUtil.mensaje("Registro : " + registro.getRegistroId().getEppResolucion().getNumero() + " aprobado para emisión correctamente.");
            } else {
                JsfUtil.mensajeError("Ocurrió un error al emitir el registro. Debe verificar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
            JsfUtil.mensajeError("Ocurrió un error al emitir el registro. Comuníquese con el administrador del sistema");
        }
        return ok;
    }

    /**
     * METODO PARA GENERAR RESOLUCION AL APROBAR LA EMISION DE LA GUIA
     *
     * @param pe
     * @param pr
     * @return
     */
    /*
    private boolean generarResolucion(Expediente pe, EppRegistroGuiaTransito pr) {
        boolean generaResol = true;
        try {
            if (pe != null) {
                Integer usuaTramDoc = 821;//ejbUsuarioFacade.obtenerIdUsuarioTramDoc(JsfUtil.getLoggedUser().getLogin());
                Calendar c_hoy = Calendar.getInstance();
                Date date = c_hoy.getTime();
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String today = formatter.format(date);
                String numResWs = wsTramDocController.crearResolucion(pe.getNumero(), 449, usuaTramDoc, today, "RG-GUIATRANSITO-EXT");
                if (numResWs != null) {
                    if (!numResWs.equals("S/N")) {
                        EppResolucion rs = new EppResolucion();
                        rs.setRegistroId(pr.getRegistroId());
                        rs.setFecha(new Date());
                        rs.setFechaIni(new Date());
                        //INICIO ESTABLECIENDO VIGENCIA DE 30 DIAS
                        Calendar hoy = Calendar.getInstance(new Locale("ES"));
                        hoy.setTime(rs.getFechaIni());
                        hoy.add(Calendar.DAY_OF_YEAR, 30);
                        int diasAdic = sbFeriadoFacade.diasNoLaborables(rs.getFechaIni(), hoy.getTime());
                        hoy.setTime(new Date());
                        hoy.add(Calendar.DAY_OF_YEAR, (30 + (diasAdic > 0 ? (diasAdic) : 0)));
                        rs.setFechaFin(hoy.getTime());
                        //FIN ESTABLECIENDO VIGENCIA DE 30 DIAS
                        rs.setEstadoId(ejbTipoExplosivoFacade.findByCodProg("TP_RESEV_APR").get(0));
                        rs.setNumero(numResWs);
                        rs.setHashQr(JsfUtil.crearHash(pr.getRegistroId().getId().toString() + rs.getNumero()));
                        rs.setActivo((short) 1);
                        rs.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        rs.setAudNumIp(JsfUtil.getIpAddress());
                        ejbResolucionFacade.create(rs);
                        pr.getRegistroId().setEppResolucion(rs);
                    } else {
                        JsfUtil.mensajeError("La numeración generada no tiene ningun formato, debe verificar sus permisos: " + numResWs);
                        generaResol = false;
                    }
                } else {
                    JsfUtil.mensajeError("Ocurrio un error al finalizar la Resolución. No se generó Número de la Resolución");
                    generaResol = false;
                }
            } else {
                JsfUtil.mensajeError("Ocurrio un error al finalizar la Resolución. No se encontró Expediente de la Resolución");
                generaResol = false;
            }
            return generaResol;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
*/
    private String generarResolucionBulk(Expediente pe) {
        try {
            if (pe != null) {
                Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdTramdocUsuarioGerente(); //ejbUsuarioFacade.obtenerIdUsuarioTramDoc(JsfUtil.getLoggedUser().getLogin());
                Calendar c_hoy = Calendar.getInstance();
                Date date = c_hoy.getTime();
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String today = formatter.format(date);
                String numResWs = wsTramDocController.crearResolucion(pe.getNumero(), 464, usuaTramDoc, today, "RG-GTE-GEPP");
                if (numResWs != null) {
                    if (!numResWs.equals("S/N")) {
                        return numResWs;
                    } else {
                        JsfUtil.mensajeError("La numeración generada no tiene ningun formato, debe verificar sus permisos: " + numResWs);
                        return null;
                    }
                } else {
                    JsfUtil.mensajeError("Ocurrió un error al finalizar la Resolución. No se generó Número de la Resolución");
                }
            } else {
                JsfUtil.mensajeError("Ocurrió un error al finalizar la Resolución. No se encontró Expediente de la Resolución");
                return null;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * METODO PARA GENERAR EXPEDIENTE AL APROBAR LA EMISION DE LA GUIA
     *
     * @param prgt
     * @return
     */
    private String generarExpediente(EppRegistroGuiaTransito prgt) {
        String nroexp = "";
        Map mapValues = new HashMap();
        mapValues.put("tipocliente", prgt.getRegistroId().getEmpresaId().getTipoId().getCodProg().equals("TP_PER_JUR") ? 1 : 2);
        mapValues.put("ruc", prgt.getRegistroId().getEmpresaId().getTipoId().getCodProg().equals("TP_PER_JUR") ? prgt.getRegistroId().getEmpresaId().getRuc() : prgt.getRegistroId().getEmpresaId().getNumDoc());
        mapValues.put("razonsocial", prgt.getRegistroId().getEmpresaId().getTipoId().getCodProg().equals("TP_PER_JUR") ? prgt.getRegistroId().getEmpresaId().getRznSocial() : prgt.getRegistroId().getEmpresaId().getNombreCompleto());
        mapValues.put("fechaDeposito", JsfUtil.dateToString(new Date(), "dd/MM/yyyy"));
        try {
            if (sbRecibosFacade.buscarReciboXIdRegistro(prgt.getRegistroId()) != null) {
                SbRecibos sbr = sbRecibosFacade.buscarReciboXIdRegistro(prgt.getRegistroId());
                mapValues.put("depositoNro", sbr.getNroSecuencia());
                mapValues.put("depositoImporte", sbr.getImporte());
                mapValues.put("depositoFecha", JsfUtil.dateToString(sbr.getFechaMovimiento(), "dd/MM/yyyy"));
                nroexp = wsTramDocController.crearExpediente(mapValues);
                return nroexp;
            } else {
                JsfUtil.mensajeError("No se encontró recibo del BN para el documento: " + prgt.getRegistroId().getId());
                return null;
            }

        } catch (Exception e) {
            JsfUtil.mensajeError("Ocurrio un error al generar el número de Expediente. ID EppRegistro: " + prgt.getRegistroId().getId());
            e.printStackTrace();
            return null;
        }
    }

    private String generarExpedienteBulk(EppGteRegistro prgt) {
        String nroexp = "";
        Map mapValues = new HashMap();
        mapValues.put("tipocliente", prgt.getEmpresaId().getTipoId().getCodProg().equals("TP_PER_JUR") ? 1 : 2);
        mapValues.put("ruc", prgt.getEmpresaId().getTipoId().getCodProg().equals("TP_PER_JUR") ? prgt.getEmpresaId().getRuc() : prgt.getEmpresaId().getNumDoc());
        mapValues.put("razonsocial", prgt.getEmpresaId().getTipoId().getCodProg().equals("TP_PER_JUR") ? prgt.getEmpresaId().getRznSocial() : prgt.getEmpresaId().getNombreCompleto());
        mapValues.put("fechaDeposito", JsfUtil.dateToString(new Date(), "dd/MM/yyyy"));
        try {
            SbRecibos sbr = null;//prgt.getSbRecibosList().get(0);
            if(prgt.getSbRecibosList() != null){
                for(SbRecibos rec : prgt.getSbRecibosList()){
                    if(rec.getActivo() == 1){
                        sbr = rec;
                        break;
                    }
                }
            }
            
            mapValues.put("depositoNro", (sbr == null)?"0":sbr.getNroSecuencia());
            mapValues.put("depositoImporte", (sbr == null)?"0.0":sbr.getImporte());
            mapValues.put("depositoFecha", JsfUtil.dateToString((sbr == null)?new Date():sbr.getFechaMovimiento(), "dd/MM/yyyy"));
            nroexp = wsTramDocController.crearExpediente(mapValues);
            return nroexp;

        } catch (Exception e) {
            JsfUtil.mensajeError("Ocurrio un error al generar el número de Expediente. ID EppRegistro: " + prgt.getId());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * METODO DE BUSQUEDA DE LA BANDEJA DEL FABRICANTE
     */
    public void buscarBandejaFabricante() {
        resultados = null;
        resultadosGteReg = null;
        if (validaBusquedaBandejaFabricante()) {
            if (tipoBusqueda != null) {
                if (tipoEstadoBandeja != null) {
                    switch (tipoEstadoBandeja.getCodProg()) {
                        case "TP_REGEV_TRAN":
                            switch (tipoBusqueda) {
                                case "1":
                                    resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro, null, null));
                                    break;
                                case "2":
                                    resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, filtro, null));
                                    break;
                                case "3":
                                    resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, filtro));
                                    break;
                                default:
                                    resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null));
                                    break;
                            }
                            break;
                        case "TP_REGEV_FIN":
                            switch (tipoBusqueda) {
                                case "1":
                                    resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricante(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro, null, null, null));
                                    break;
                                case "2":
                                    resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricante(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, filtro, null, null));
                                    break;
                                case "3":
                                    resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricante(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, filtro, null));
                                    break;
                                case "4":
                                    resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricante(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, filtro));
                                    break;
                                default:
                                    resultados = null;
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }

            } else {
                String x = "";
                if (tipoEstadoBandeja.getCodProg().equals("TP_REGEV_TRAN")) {
                    resultadosGteReg = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulk(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null));
                } else {
                    resultados = new ListDataModel<>(eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricante(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, null));
                }
            }
        }
    }
    
    
    public void buscarBandejaFabricanteMap() {
        resultados = null;
        resultadosGteReg = null;
        resultadosGuia = null;
        resultadosGuiaGte = null;
        
        if (validaBusquedaBandejaFabricante()) {
            if (tipoBusqueda != null) {
                if (tipoEstadoBandeja != null) {
                    switch (tipoEstadoBandeja.getCodProg()) {
                        case "TP_REGEV_TRAN":
                            switch (tipoBusqueda) {
                                case "1":
                                    resultadosGuiaGte = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulkMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro, null, null, null);
                                    break;
                                case "2":
                                    resultadosGuiaGte = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulkMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, filtro, null, null);
                                    break;
                                case "3":
                                    resultadosGuiaGte = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulkMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, filtro, null);
                                    break;
                                case "5":
                                    resultadosGuiaGte = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulkMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, filtro);
                                    break;
                                default:
                                    resultadosGuiaGte = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulkMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, null);
                                    break;
                            }
                            break;
                        case "TP_REGEV_FIN":
                            switch (tipoBusqueda) {
                                case "1":
                                    resultadosGuia = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, filtro, null, null, null, null);
                                    break;
                                case "2":
                                    resultadosGuia = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, filtro, null, null, null);
                                    break;
                                case "3":
                                    resultadosGuia = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, filtro, null, null);
                                    break;
                                case "4":
                                    resultadosGuia = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, filtro, null);
                                    break;
                                case "5":
                                    resultadosGuia = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, null, filtro);
                                    break;
                                default:
                                    resultadosGuia = null;
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }else{
                    JsfUtil.mensajeAdvertencia("Por favor seleccionar el tipo de estado");
                }
            } else {
                String x = "";
                if (tipoEstadoBandeja.getCodProg().equals("TP_REGEV_TRAN")) {
                    resultadosGuiaGte = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteBulkMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, null);
                } else {
                    resultadosGuia = eppRegistroGuiaTransitoFacade.buscarGuiasExternoFabricanteMap(empresa, tipoEstadoBandeja == null ? null : tipoEstadoBandeja, null, null, null, null, null);
                }
            }
        }
        if(resultadosGuiaGte != null){
            orderListResultados(resultadosGuiaGte);
        }
    }
    
    public void orderListResultados(List<Map> po) {
        Collections.sort(po, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                return o2.get("ID").toString().compareTo(o1.get("ID").toString());
            }
        });
    }

    /**
     * METODO PARA VALIDACION DE LA BUSQUEDA EN LA BANDEJA DEL FABRICANTE
     *
     * @return
     */
    private boolean validaBusquedaBandejaFabricante() {
        boolean valida = true;

        if (tipoBusqueda == null && tipoEstadoBandeja == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe seleccionar algún criterio de búsqueda");
        }

        if (tipoBusqueda != null) {
            if (filtro == null || "".equals(filtro)) {
                valida = false;
                JsfUtil.mensajeAdvertencia("Debe ingresar el texto para la búsqueda");
            }
        }

        return valida;
    }

    ////////////////////////////////////////////////
    ////////////////////////////////////////////////
    //////////  DEVOLUCION DE SALDOS  //////////////
    ////////////////////////////////////////////////
    ////////////////////////////////////////////////
    /**
     * METODO PARA DEVOLUCION DE SALDOS DEL EXPLOSIVO SELECCIONADO
     *
     * @param p_d_expSol
     */
    private void devolverSaldos(EppGteExplosivoSolicita p_d_expSol) {
        //DEVUELVE LOS SALDOS EN AUTORIZACIONES
        //VERIFICAMOS EL TIPO DE AUTORIZACION

        if (resolucionGerencia != null) {
            switch (resolucionGerencia.getTipoProId().getCodProg()) {
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                case "TP_PRTUP_AAPOR":
                    for (EppExplosivoSolicitado e : current.getEppExplosivoSolicitadoList()) {
                        for (EppDetalleAutUso d : resolucionGerencia.getEppDetalleAutUsoList()) {
                            if (Objects.equals(e, p_d_expSol) && Objects.equals(e.getExplosivoId(), d.getExplosivoId()) && d.getActivo() == 1 && e.getActivo() == 1) {
                                d.setSaldo(d.getSaldo() + e.getCantidad());
                                ejbDetalleAutorizacionUsoFacade.edit(d);
                                break;
                            }
                        }
                        for (EppDetalleCom d : resolucionGerencia.getComId().getEppDetalleComList()) {
                            if (Objects.equals(e, p_d_expSol) && Objects.equals(e.getExplosivoId(), d.getExplosivoId()) && d.getActivo() == 1 && e.getActivo() == 1) {
                                d.setSaldo(d.getSaldo() + e.getCantidad());
                                ejbDetalleComFacade.edit(d);
                                break;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

        } else {
            JsfUtil.mensaje("La guía no cuenta con Resolución de gerencia");
        }
    }

    /**
     * REGISTRAR TRAZA DE EXPEDIENTE
     *
     * @author Richar Fernández
     * @version 2.0
     * @param reg Registro a crear traza
     * @return Flag de documento de traza correctamente
     */
    public boolean registraTrazaExpediente(EppRegistro reg) {
        List<Integer> acciones = new ArrayList();
        String userRemitente = "1003";
        String userDestino = "1003";
        String observacion = "";
        SbUsuarioGt usuario = new SbUsuarioGt();
        String suce;
        Expediente expediente;
        boolean estadoTraza = true, adjuntar = false;
        
        try {
            if (reg.getEstado().getCodProg().equals("TP_REGEV_TRAN")
                    || reg.getEstado().getCodProg().equals("TP_REGEV_OBS")
                    || reg.getEstado().getCodProg().equals("TP_REGEV_VIS")
                    || reg.getEstado().getCodProg().equals("TP_REGEV_FIR")
                    || reg.getEstado().getCodProg().equals("TP_REGEV_FIN")) {

                expediente = ejbExpedienteFacade.obtenerExpedienteXNumeroList(reg.getNroExpediente()).get(0);
                acciones.add(13);   // Archivar    
                observacion = "Trámite concluido. Se procede a la entrega de la GTE "
                        + reg.getEppResolucion().getNumero() + " del "
                        + formatoFechaDdMmYyyy(reg.getEppResolucion().getFecha());

                if (!reg.getEstado().getCodProg().equals("TP_REGEV_FIN")) {
                    estadoTraza = wsTramDocController.asignarExpediente(reg.getNroExpediente(), userRemitente, userDestino, observacion, "", acciones, adjuntar, reg.getEppResolucion().getNumero());
                }
                if (!estadoTraza) {
                    JsfUtil.mensajeError("No se pudo generar la traza del expediente. ID exp. : " + reg.getNroExpediente());
                } else if (reg.getEstado().getCodProg().equals("TP_REGEV_FIN")) {
                    estadoTraza = wsTramDocController.archivarExpediente(reg.getNroExpediente(), "USRWEB", observacion);
                    if (!estadoTraza) {
                        JsfUtil.mensajeError(" No se pudo archivar el expediente. ID exp. : " + reg.getNroExpediente());
                    }
                }
            }
        } catch (Exception e) {
            estadoTraza = false;
            e.printStackTrace();
        }
            
        return estadoTraza;
    }

    /**
     * OBTENER NOMBRE DE NOTIFICACION DESTINO
     *
     * @author Richar Fernández
     * @version 2.0
     * @return Flag de documento de traza correctamente
     */
    public String obtenerNombreNotificacionDestino() {
        String nombre = "";
        /*switch (strNotificacion) {
            case "COURIER":
                nombre = " al domicilio de la solicitante. ";
                break;
            case "TP_AREA_GEPP":
                nombre = " a la ventanilla de entregas de la Sede Central de la SUCAMEC conforme a lo requerido por la solicitante. ";
                break;
            default:
                TipoBaseGt tipo = tipoBaseFacadeGt.tipoBaseXCodProg(strNotificacion);
                nombre = " a la " + tipo.getNombre() + " conforme a lo requerido por la solicitante. ";
                break;
        }*/

        return nombre;
    }

    /**
     * *
     * FORMATEAR FECHA: dd/mm/yyyy
     *
     * @param param
     * @return
     */
    public static String formatoFechaDdMmYyyy(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            //mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            return dia + "/" + mes + "/" + anio;
        } else {
            return "BORRADOR";
        }
    }

    /**
     * ABRE DIALOG CONFIRMACION
     */
    public void lstnrConfirmarCambioTipoUso() {
        RequestContext.getCurrentInstance().execute("PF('confirmTipoUso').show()");

    }

    /**
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (getUploadedFileList().size() >= 1) {//
                JsfUtil.mensajeAdvertencia("Solo se permite adjuntar un (1) archivo.");
            } else {
                getUploadedFileList().add(event.getFile());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFile(UploadedFile uplfile) {
        try {
            if (!getUploadedFileList().remove(uplfile)) {
                JsfUtil.mensajeError("NO");
            }
        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia(ResourceBundle.getBundle("/Bundle").getString("FileUploadErrorOccured"));
        }
    }

    public void openDlgDocumentoAdjuntoGt() {
        filtro = null;
        uploadedFileList = new ArrayList();
        documento = new EppDocumento();
        RequestContext.getCurrentInstance().execute("PF('DocViewDialog').show()");
        RequestContext.getCurrentInstance().update("doctForm");
    }

    /**
     *
     * CREAR UN NUEVO DOCUMENTO
     */
    public void nuevoDocumento() {
        try {
            if (validarDocumento()) {
                documento.setEmpresaId(currentBulk.getEmpresaId());
                documento.setNroExpediente("TEMPORAL");
                documento.setActivo((short) 1);
                documento.setUtilizado((short) 0);
                documento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                documento.setAudNumIp(JsfUtil.getIpAddress());

                if (documento.getId() == null) {
                    ejbDocumentoFacade.create(documento);
                    currentBulk.getEppDocumentoList().add(documento);
                    lstDocumentosTemp.add(documento);
                    JsfUtil.mensaje("Documento Creado correctamente");
                } else {
                    ejbDocumentoFacade.edit(documento);
                    JsfUtil.mensaje("Documento Editado correctamente");
                }

                if (!uploadedFileList.isEmpty()) {
                    documento.setRuta(documento.getId() + "_" + documento.getTipoId().getNombre() + "." + FilenameUtils.getExtension(uploadedFileList.get(0).getFileName()));
                    ejbDocumentoFacade.edit(documento);
                    if (!uploadFilesController.uploadDocumento(uploadedFileList.get(0), documento.getRuta(), true)) {
                        JsfUtil.mensajeAdvertencia(ResourceBundle.getBundle("/BundleGeppGte").getString("DocumentosErrorPath"));
                    }
                }
                RequestContext.getCurrentInstance().execute("PF('DocViewDialog').hide()");
                RequestContext.getCurrentInstance().update("crearForm:pnlDocumentos");
                RequestContext.getCurrentInstance().update("editarForm:pnlDocumentos");
                documento = new EppDocumento();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al grabar el documento.");
        }
    }

    public void crearDocumentoTemp() {
        if (validarDocumento()) {
            if (lstDocumentosTemp == null) {
                lstDocumentosTemp = new ArrayList();
            }

            documento.setId(JsfUtil.tempId());
            documento.setEmpresaId(currentBulk.getEmpresaId());
            documento.setNroExpediente("TEMPORAL");
            documento.setActivo((short) 1);
            documento.setUtilizado((short) 0);
            documento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            documento.setAudNumIp(JsfUtil.getIpAddress());
            String id = sbNumeracionFacade.buscarNumeracionActual("TP_NUM_DOC_GTE").toString();
            documento.setRuta(id + "-" + documento.getTipoId().getCodProg() + "." + FilenameUtils.getExtension(uploadedFileList.get(0).getFileName()));
            lstDocumentosTemp.add(documento);

            if (lstDocAdjTemp == null) {
                lstDocAdjTemp = new ArrayList();
            }

            DocumentoTemp dtemp = new DocumentoTemp();
            dtemp.setId(documento.getId());
            dtemp.setRuta(documento.getRuta());
            dtemp.setUpfile(uploadedFileList.get(0));
            lstDocAdjTemp.add(dtemp);

            JsfUtil.mensaje("Documento guardado correctamente");

            RequestContext.getCurrentInstance().execute("PF('DocViewDialog').hide()");
            RequestContext.getCurrentInstance().update("crearForm:pnlDocumentos");
            RequestContext.getCurrentInstance().update("editarForm:pnlDocumentos");
            documento = new EppDocumento();
            uploadedFileList = new ArrayList();
        }
    }

    private boolean validarDocumento() {
        boolean valida = true;

        if (documento.getTipoId() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el tipo de documento");
        } else {
            String x = "";
            if (documento.getTipoId().getCodProg().equals("TP_DOC_FE")) {
                if (uploadedFileList != null && !uploadedFileList.get(0).getContentType().endsWith("pdf")) {
                    valida = false;
                    JsfUtil.mensajeAdvertencia("Para el caso de Factura Electrónica sólo se acepta archivos en formato PDF.");
                }

            }
        }

        if (documento.getNumero() == null || "".equals(documento.getNumero())) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el número de documento");
        }

        if (documento.getFechaEmision() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar la fecha de emisión del documento");
        }

        if (uploadedFileList == null || uploadedFileList.isEmpty()) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el documento adjunto");
        }
        return valida;
    }

    /**
     * Muestra la lista de los documentos adjuntados
     *
     * @return
     */
    public List<EppDocumento> getLstDocumentosGt() {
        List<EppDocumento> lstFinal = new ArrayList();
        if (current != null) {
            if (current.getRegistroId() != null) {
                if (current.getRegistroId().getEppDocumentoList() != null && !current.getRegistroId().getEppDocumentoList().isEmpty()) {
                    for (EppDocumento re : current.getRegistroId().getEppDocumentoList()) {
                        if (re.getActivo() == 1) {
                            lstFinal.add(re);
                        }
                    }
                }
            }
        }
        return lstFinal;
    }

    public List<EppDocumento> getLstDocumentosGtBulk() {
        List<EppDocumento> lstFinal = new ArrayList();
        //if (currentBulk != null) {
        //if (currentBulk.getEppDocumentoList() != null && !currentBulk.getEppDocumentoList().isEmpty()) {
        if (lstDocumentosTemp != null && !lstDocumentosTemp.isEmpty()) {
            for (EppDocumento re : lstDocumentosTemp) {
                if (re.getActivo() == 1) {
                    lstFinal.add(re);
                }
            }
        }
        //}
        return lstFinal;
    }

    /**
     * Metodo para eliminar documentos
     */
    public void borrarDocumentosSeleccionados() {
        if (!lstDocumentosEliminados.isEmpty()) {
            for (EppDocumento ls : lstDocumentosEliminados) {
                //for (EppDocumento l : currentBulk.getEppDocumentoList()) {
                for (EppDocumento l : lstDocumentosTemp) {
                    if (Objects.equals(ls.getId(), l.getId())) {
                        l.setActivo((short) 0);
                        //currentBulk.getEppDocumentoList().remove(l);
                        lstDocumentosTemp.remove(l);
                        break;
                    }
                }
            }
            RequestContext.getCurrentInstance().update("pnlgDocumentos");
            JsfUtil.mensaje("Registro(s) Eliminado(s)");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar un documento para eliminar");
        }
    }

    /**
     * Metodo para editar documento
     *
     * @param eppd
     */
    public void editarDocumento(EppDocumento eppd) {
        try {

            documento = eppd;

            RequestContext.getCurrentInstance().execute("PF('DocViewDialog').show()");
            RequestContext.getCurrentInstance().update("doctForm");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al intentar editar el documento");
        }
    }

    /**
     * Metodo para anular la guia de transito desde la bandeja del fabricante
     *
     * @param gtanular
     */
    public void anularGt(EppRegistroGuiaTransito gtanular) {
        try {
            gtanular.getRegistroId().setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_ANU").get(0));
            ejbRegistroFacade.edit(gtanular.getRegistroId());
            //SE LIBERA EL RECIBO
            /*SbRecibos rb = new SbRecibos();
            rb = sbRecibosFacade.buscarReciboXIdRegistro(gtanular.getRegistroId());
            rb.setRegistroId(null);
            ejbRecibosFacade.edit(rb);*/
            resultados = null;
            resultadosGteReg = null;
            tipoEstadoBandeja = null;
            tipoBusqueda = null;
            JsfUtil.mensaje("Guia Anulada correctamente");
        } catch (Exception e) {
            JsfUtil.mensajeError("Ocurrió un error al anular la Guia, " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void anularGtBulk(EppGteRegistro gtanular) {
        try {
            gtanular.setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_ANU").get(0));
            eppGteRegistroFacade.edit(gtanular);
            //SE LIBERA EL RECIBO
            /*SbRecibos rb = new SbRecibos();
            rb = gtanular.getSbRecibosList().get(0);//sbRecibosFacade.buscarReciboXIdRegistro(gtanular.getRegistroId());
            rb.setRegistroId(null);
            ejbRecibosFacade.edit(rb);*/
            JsfUtil.mensaje("Guia Anulada correctamente");
        } catch (Exception e) {
            JsfUtil.mensajeError("Ocurrió un error al anular la Guia, " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodo para borrar una Guia de transito desde la bandeja del usuario
     *
     * @param gtborrar
     */
    public void borrarGt(EppRegistroGuiaTransito gtborrar) {
        try {
            gtborrar.getRegistroId().setActivo((short) 0);
            ejbRegistroFacade.edit(gtborrar.getRegistroId());
            
            //SE LIBERA EL RECIBO
            if(gtborrar.getRegistroId().getSbRecibosList() != null && !gtborrar.getRegistroId().getSbRecibosList().isEmpty()){
                SbRecibos rb = new SbRecibos();
                for(SbRecibos rec : gtborrar.getRegistroId().getSbRecibosList()){
                    if(rec.getActivo() == 1){
                        rb = rec;
                        break;
                    }
                }
                SbReciboRegistro rr = new SbReciboRegistro();
                for(SbReciboRegistro recReg : rb.getSbReciboRegistroList()){
                    if(recReg.getActivo() == 1){
                        rr = recReg;
                        break;
                    }
                }
                rr.setActivo(JsfUtil.FALSE);
                ejbSbReciboRegistroFacade.edit(rr);
            }
            JsfUtil.mensaje("Solicitud de GTE Borrada correctamente");
            resultados = null;
            resultadosGteReg = null;
        } catch (Exception e) {
            JsfUtil.mensajeError("Ocurrió un error al borrar la Guia, " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void borrarGtBulk(EppGteRegistro gtborrar) {
        try {
            gtborrar.setActivo((short) 0);
            eppGteRegistroFacade.edit(gtborrar);
            //SE LIBERA EL RECIBO
            if(gtborrar.getSbRecibosList() != null && !gtborrar.getSbRecibosList().isEmpty()){
                SbRecibos rb = new SbRecibos();
                for(SbRecibos rec : gtborrar.getSbRecibosList()){
                    if(rec.getActivo() == 1){
                        rb = rec;
                        break;
                    }
                }
                SbReciboRegistro rr = new SbReciboRegistro();
                for(SbReciboRegistro recReg : rb.getSbReciboRegistroList()){
                    if(recReg.getActivo() == 1){
                        rr = recReg;
                        break;
                    }
                }
                rr.setActivo(JsfUtil.FALSE);
                ejbSbReciboRegistroFacade.edit(rr);
            }
            if(gtborrar.getEppGteDepositoList() != null && !gtborrar.getEppGteDepositoList().isEmpty()){
                EppGteDeposito deposito = new EppGteDeposito();
                for(EppGteDeposito depo : gtborrar.getEppGteDepositoList()){
                    if(depo.getActivo() == 0){
                        continue;
                    }
                    deposito = depo;
                    break;
                }
                deposito.setActivo(JsfUtil.FALSE);
                ejbEppGteDepositoFacade.edit(deposito);
            }
            JsfUtil.mensaje("Solicitud de GTE Borrada correctamente");
            resultados = null;
            resultadosGteReg = null;
        } catch (Exception e) {
            JsfUtil.mensajeError("Ocurrió un error al borrar la Guia, " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodo para agregar datos adicionales a la GT
     *
     * @return
     */
    public String datosAdicionalesGt() {
        current = new EppRegistroGuiaTransito();
        current = (EppRegistroGuiaTransito) resultados.getRowData();
        tipoTransporte = current.getTipoTransporte();
        empresaTransporte = current.getEmpresaTranspId();
        fechaLlegada = current.getFechaLlegada();
        
        obtenerTipoGTECurrent(false);
        setDisabledFechaLlegada(false);        
        //cargarRazonSocOrigenDestino(current);
        reciboBn = sbRecibosFacade.buscarReciboXIdRegistro(current.getRegistroId());
        if(reciboBn != null){
            strNroSecuencia = "<b>" + reciboBn.getNroSecuencia().toString() + "</b>";
            strFechaMovimiento = "<b>" + mostrarFechaDdMmYyyy(reciboBn.getFechaMovimiento()) + "</b>";
            strImporte = "<b>" + reciboBn.getImporte().toString() + "</b>";
        }
        //////////////        
        strViewOrigen = "<b>" + mostrarDatosOrigen(current, false) + "</b>";
        strViewDestino = "<b>" + mostrarDatosDestino(current, false) + "</b>";
        strViewTipoTransp = "<b>" + current.getTipoTransporte().getNombre() + "</b>";
        strViewEmpTransp = mostrarClienteString(current.getEmpresaTranspId()) == null ? "" : ("Los explosivos serán trasladados por:<br/>"
                + "EMPRESA DE TRANSPORTES: " + mostrarClienteString(current.getEmpresaTranspId()));
        JsfUtil.mensajeAdvertencia("SOLO PODRÁ AGREGAR DATOS ADICIONALES POR ÚNICA VEZ PARA ESTA GUIA. POR FAVOR VERIFIQUE ANTES DE GUARDAR.");
        return "/aplicacion/eppRegistroGuiaTransito/ViewDatosAdicGt";
    }

    public boolean mostrarEmitirGt(EppRegistroGuiaTransito vrgt) {
        return vrgt.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_TRAN");
    }

    public boolean mostrarEmitirGtBulk(EppGteRegistro vrgt) {
        return vrgt.getEstado().getCodProg().equals("TP_REGEV_TRAN");
    }
    
    public boolean mostrarEmitirGtBulkMap(Map reg) {
        return reg.get("ESTADO_CODPROG").toString().equals("TP_REGEV_TRAN");
    }

    public void openDlgBuscarPolicia() {
        filtro = null;
        lstDlgPolicia = null;
        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogPolicia').show()");
        RequestContext.getCurrentInstance().update("frmDlgPolicia");
    }

    public void buscarDlgPolicia() {
        lstDlgPolicia = eppGuiaTransitoPoliciaFacade.buscarPolicias(filtro);
    }
    
    public boolean validaPolicias(EppGuiaTransitoPolicia selectedPolicia){
        boolean validacion = true;
        
        for(EppGuiaTransitoPolicia p : current.getEppGuiaTransitoPoliciaList()){
            if(Objects.equals(p.getId(), selectedPolicia.getId())){
                validacion = false;
                break;
            }
        }
        
        return validacion;
    }

    public void seleccionarDlgPoliciaGt(EppGuiaTransitoPolicia p) {
        if (current.getEppGuiaTransitoPoliciaList() == null) {
            current.setEppGuiaTransitoPoliciaList(new ArrayList());
        }
        
        if(validaPolicias(p)){
            current.getEppGuiaTransitoPoliciaList().add(p);
            RequestContext.getCurrentInstance().update("verDatosAdicForm:pnlDatosCustodio");
            RequestContext.getCurrentInstance().update("editarDatosAdicForm:pnlDatosCustodio");
        }else{
            JsfUtil.mensajeError("El policía ya ha sido adicionado a la lista");
        } 
    }

    public void eliminarPoliciasSeleccionados() {
        if (!lstCustodiosPoliciaEliminados.isEmpty()) {
            for (EppGuiaTransitoPolicia ls : lstCustodiosPoliciaEliminados) {
                for (EppGuiaTransitoPolicia l : current.getEppGuiaTransitoPoliciaList()) {
                    if (Objects.equals(ls.getId(), l.getId())) {
                        current.getEppGuiaTransitoPoliciaList().remove(l);
                        break;
                    }
                }
            }
            RequestContext.getCurrentInstance().update("pnlDatosCustodio");
            JsfUtil.mensaje("Registro(s) Eliminado(s)");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar un registro para eliminar");
        }
    }

    public void openDlgBuscarCustodio() {
        filtro = null;
        lstDlgPolicia = null;
        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogCust').show()");
        RequestContext.getCurrentInstance().update("frmDlgCust");
    }

    public void buscarDlgCustodio() {
        lstDlgCustodio = ejbLicenciaFacade.buscarCustodio(filtro, filtroDni);
    }

    public void seleccionarDlgCustodioGt(EppLicencia l) {
        if (current.getRegistroId().getEppLicenciaList() == null) {
            current.getRegistroId().setEppLicenciaList(new ArrayList());
        }
        current.getRegistroId().getEppLicenciaList().add(l);
        RequestContext.getCurrentInstance().update("verForm:pnlDatosCustodioPrivado");
    }

    public void eliminarCustodiosSeleccionados() {
        if (!lstCustodiosPrivadosEliminados.isEmpty()) {
            for (EppLicencia ls : lstCustodiosPrivadosEliminados) {
                for (EppLicencia l : current.getEppLicenciaList()) {
                    if (Objects.equals(ls.getId(), l.getId())) {
                        current.getEppLicenciaList().remove(l);
                        break;
                    }
                }
            }
            RequestContext.getCurrentInstance().update("pnlDatosCustodioPrivado");
            JsfUtil.mensaje("Registro(s) Eliminado(s)");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar un registro para eliminar");
        }
    }

    /**
     * METODO QUE ABRE EL DIALOGO PARA LA BUSQUEDA DEL CONDUCTOR
     */
    public void openDlgBuscarConductor() {
        filtro = null;
        lstDlgConductor = null;
        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogConductor').show()");
        RequestContext.getCurrentInstance().update("frmDlgConductor");
    }

    public void openDlgBuscarConductorAdic() {
        filtro = null;
        lstDlgConductor = null;
        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogConductor').show()");
        RequestContext.getCurrentInstance().update("frmDlgConductor");
    }

    /**
     * METODO QUE REALIZA LA BUSQUEDA DEL CONDUCTOR
     */
    public void buscarConductor() {
        lstDlgConductor = ejbLicenciaFacade.buscarConductor(filtro.trim().toUpperCase(), filtroDni);
    }

    /**
     * METODO QUE SELECCIONA UN CONDUCTOR Y LO AGREGA A LA GUIA DE TRANSITO
     *
     * @param conduct
     */
    public void seleccionarDlgConductorGt(EppLicencia conduct) {
        EppLicencia r = conduct;
        currentBulk.setEppLicenciaList1(new ArrayList());
        currentBulk.getEppLicenciaList1().add(r);
        strConductor = r.getPersonaId().getNombreCompleto();
        RequestContext.getCurrentInstance().update("verForm:pnlgDatosCyV");
    }

    /*public boolean validaConductorDuplicadoBulk(EppLicencia con) {
        if (currentBulk.getEppLicenciaList1() == null) {
            return true;
        }
        for (EppLicencia l : currentBulk.getEppLicenciaList1()) {
            if (Objects.equals(l, con)) {
                JsfUtil.mensajeAdvertencia("El conductor seleccionado ya existe en el listado. Debe verificar.");
                return false;
            }
        }
        return true;
    }*/

    public void seleccionarDlgConductorAdicGt(EppLicencia conduct) {
        EppLicencia r = conduct;
        if (current.getEppLicenciaList1() == null) {
            current.setEppLicenciaList1(new ArrayList());
        }

        if (validaConductorDuplicado(r)) {
            if (current.getEppLicenciaList1().size() < 2) {
                current.getEppLicenciaList1().add(r);
                //strConductor = r.getPersonaId().getNombreCompleto();
                RequestContext.getCurrentInstance().update("verDatosAdicForm:pnlConductor");
            } else {
                JsfUtil.mensajeAdvertencia("Solo se puede agregar dos (2) conductores.");
            }
        }

    }

    /**
     * METODO QUE VALIDA SI EL CONDUCTOR SELECCIONADO SE ENCUENTRA DUPLICADO EN
     * EL LISTADO
     *
     * @param con
     * @return
     */
    public boolean validaConductorDuplicado(EppLicencia con) {
        if (current.getEppLicenciaList1() == null) {
            return true;
        }
        for (EppLicencia l : current.getEppLicenciaList1()) {
            if (Objects.equals(l, con)) {
                JsfUtil.mensajeAdvertencia("El conductor seleccionado ya existe en el listado. Debe verificar.");
                return false;
            }
        }
        return true;
    }

    public void openDlgBuscarVehiculo() {
        filtro = null;
        lstDlgVehiculo = null;
        RequestContext.getCurrentInstance().execute("PF('CreateViewDialogVehiculo').show()");
        RequestContext.getCurrentInstance().update("frmDlgVehiculo");
    }

    public void buscarDlgVehiculo() {
        lstDlgVehiculo = eppGuiaTransitoVehiculoFacade.buscarVehiculo(filtro);
    }

    public void seleccionarDlgVehiculoGt(EppGuiaTransitoVehiculo v) {
        currentBulk.setEppGuiaTransitoVehiculoList(new ArrayList());
        currentBulk.getEppGuiaTransitoVehiculoList().add(v);
        strVehiculo = v.getMarca() + " / " + v.getPlaca();
        RequestContext.getCurrentInstance().update("verForm:pnlDatosCyV");
    }

    public void openDlgCrearVehiculo() {
        vehiculo = new EppGuiaTransitoVehiculo();
        RequestContext.getCurrentInstance().execute("PF('wvDlgVehiculo').show()");
        RequestContext.getCurrentInstance().update("frmVehiculo");
    }

    public void crearVehiculo() {
        try {
            if (validarCrearVehiculo()) {
                vehiculo = (EppGuiaTransitoVehiculo) JsfUtil.entidadMayusculas(vehiculo, "");
                vehiculo.setActivo((short) 1);
                vehiculo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                vehiculo.setAudNumIp(JsfUtil.getIpAddress());
                eppGuiaTransitoVehiculoFacade.create(vehiculo);
                strVehiculo = vehiculo.getMarca() + " / " + vehiculo.getPlaca();
                currentBulk.setEppGuiaTransitoVehiculoList(new ArrayList());
                currentBulk.getEppGuiaTransitoVehiculoList().add(vehiculo);
                RequestContext.getCurrentInstance().execute("PF('wvDlgVehiculo').hide()");
                RequestContext.getCurrentInstance().update("verForm:pnlDatosCyV");
                JsfUtil.mensaje("Vehículo creado correctamente, ID: " + vehiculo.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al crear el vehiculo, " + e.getMessage());
        }
    }

    private boolean validarCrearVehiculo() {
        boolean valida = true;
        if (vehiculo.getPlaca() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar la Placa del vehículo");
        }
        if (vehiculo.getMarca() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar la Marca del vehículo");
        }
        if (vehiculo.getAnio() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el Año del vehículo");
        } else {
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String anio = formatAnio.format(new Date());
            if (vehiculo.getAnio() < 1800) {
                valida = false;
                JsfUtil.mensajeAdvertencia("El año del vehículo es inferior a 1800, debe verificar.");
            }
            if (vehiculo.getAnio() > (Long.valueOf(anio) + 1)) {
                valida = false;
                JsfUtil.mensajeAdvertencia("El Año del vehículo puede ser como máximo el valor del año siguiente.");
            }
        }
        return valida;
    }

    public List<SbDistritoGt> completeUbigeo(String query) {
        List<SbDistritoGt> filteredUbigeo = new ArrayList<>();
        List<SbDistritoGt> lstUbigeo = distritoFacade.listarDistritos();
        for (SbDistritoGt d : lstUbigeo) {
            if (d.getNombreCompleto().contains(query.toUpperCase())) {
                filteredUbigeo.add(d);
            }
        }
        return filteredUbigeo;
    }

    public List<SbPersonaGt> getLstRepresentantesLegales() {
        List<SbPersonaGt> lstFinal = new ArrayList();
        if (current != null) {
            if (current.getRegistroId() != null) {
                if (current.getRegistroId().getEmpresaId() != null) {
                    if (current.getRegistroId().getEmpresaId().getSbRelacionPersonaList() != null) {
                        for (SbRelacionPersonaGt re : current.getRegistroId().getEmpresaId().getSbRelacionPersonaList()) {
                            if (re.getPersonaDestId().getActivo() == 1) {
                                lstFinal.add(re.getPersonaDestId());
                            }
                        }
                    }
                }
            }
        }
        return lstFinal;
    }

    /**
     * Autocompletar para la busqueda de recibos
     *
     * @param query
     * @return
     */
    public List<SbRecibos> autoCompleteRecibos(String query) {
        try {
            Long recFilter = null;
            try {
                recFilter = Long.parseLong(query.trim());
            } catch (Exception e) {
                JsfUtil.mensajeError("El voucher debe contener sólo numeros");
                return null;
            }
            
            borrarValoresRecibo();
            List<SbRecibos> filteredList = new ArrayList();
            HashMap mMap = new HashMap();
            mMap.put("tipo", null);
            mMap.put("importe", parametrizacionTupa.getImporte() );
            mMap.put("codigo", parametrizacionTupa.getCodTributo() );

            List<SbRecibos> lstUniv = sbRecibosFacade.listarRecibosByImporteByCodAtributo(currentBulk.getEmpresaId(), mMap);
            for (SbRecibos x : lstUniv) {
                if (x.getNroSecuencia().equals(recFilter)) {
                    filteredList.add(x);
                }
            }
            return filteredList;
        } catch (Exception e) {
            borrarValoresRecibo();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Autocompletar para la busqueda de resoluciones
     *
     * @param query
     * @return
     */
    public List<EppRegistro> autoCompleteResoluciones(String query) {
        try {
            List<EppRegistro> filteredList = new ArrayList();
            List<EppRegistro> lstUniv = new ArrayList();
            
            if(criterioResGuia != null){
                switch(criterioResGuia){
                    case "1":
                    case "2":
                    case "3":
                            lstUniv =  ejbRegistroFacade.buscarResolucionesGuiaExterna(null, empresa);
                            break;
                    case "6":
                            lstUniv =  ejbRegistroFacade.buscarAutorizacionesComercializacionGTE(null, empresa);
                            break;
                    default:
                            break;
                }
            }else{
                lstUniv =  ejbRegistroFacade.buscarResolucionesGuiaExterna(null, empresa);
            }
            
            for (EppRegistro x : lstUniv) {
                String nrorg = "" + x.getEppResolucion().getNumero();
                if (nrorg.contains(query.toUpperCase())) {
                    filteredList.add(x);
                }
            }
            return filteredList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void lstnrSelectRecibo(SelectEvent event) {
        try {
            if(event == null){
                return;
            }
            borrarValoresRecibo();
            if (event!=null) {
                reciboBn = (SbRecibos) event.getObject();
                strNroSecuencia = "<b>" + reciboBn.getNroSecuencia() + "</b>";
                strFechaMovimiento = "<b>" + formatoFechaDdMmYyyy(reciboBn.getFechaMovimiento()) + "</b>";
                strImporte = "<b>" + reciboBn.getImporte() + "</b>";
                setDisableReciboBn(true);
                setEligeReciboLista(true);
                JsfUtil.mensaje("Recibo " + reciboBn.getNroSecuencia() + " seleccionado correctamente.");
            }
        } catch (Exception ex) {
            setEligeReciboLista(false);
            JsfUtil.mensajeError(ex.getMessage());
        }

    }

    public void borrarReciboSeleccionado() {
        borrarValoresRecibo();
        setDisableReciboBn(false);
    }

    private void borrarValoresRecibo() {
        reciboBn = null;
        strNroSecuencia = null;
        strFechaMovimiento = null;
        strImporte = null;
        setEligeReciboLista(false);
    }

    public void borrarResolucionSeleccionada() {
        resolucionGerencia = null;
        numeroResolucionGerencia = "";
        fechaCaducidadResolucionGerencia = null;
        strViewVencimiento = null;
        tipoResolucionGerencia = "";
        //criterioResGuia = "0";
        tipoOrigen = null;
        tipoDestino = null;
        polvorin = null;
        lugarUso = null;
        lugarUsoUbigeoOrigen = null;
        lugarUsoUbigeoDestino = null;
        nombreEmpresaOrigen = null;
        nombreEmpresaDestino = null;
        explosivoSeleccionado = null;
        lstExplosivosCombo = new ArrayList();
        lstDocumentosTemp = new ArrayList();
        currentBulk.setOrigenPolvorin(null);
        currentBulk.setDestinoPolvorin(null);
        currentBulk.setLugarUsoDestino(null);
        currentBulk.setLugarUsoUbigeoDestino(null);
        currentBulk.setOrigenFabrica(null);
        currentBulk.setDestinoFabrica(null);
        currentBulk.setOrigenAlquilerId(null);
        currentBulk.setDestinoAlquilerId(null);
        currentBulk.setEppGteExplosivoSolicitaList(new ArrayList());
        setDisableResolucion(true);
        if(criterioResGuia != null && !criterioResGuia.equals("0")){
            setDisableResolucion(false);
        }
    }

    public String subStringTool(String fieldx) {
        String fs = "";
        if (fieldx != null && fieldx.length() > 0) {
            if (fieldx.length() > 50) {
                fs = fieldx.substring(0, 50) + "... ";
            } else {
                fs = fieldx.substring(0, fieldx.length() - 1) + "... ";
            }
        }
        return fs;
    }

    public String subStringToolGte(String fieldx) {
        String fs = "";
        if (fieldx != null && fieldx.length() > 0) {
            if (fieldx.length() > 30) {
                fs = fieldx.substring(0, 30) + "... ";
            } else {
                fs = fieldx.substring(0, fieldx.length() - 1) + "... ";
            }
        }
        return fs;
    }

    public void cancelarSeleccionarDlgPolvorinOrigenGt() {
        tipoOrigen = null;
        RequestContext.getCurrentInstance().update("crearForm:pnlgOrigenDestino");
        RequestContext.getCurrentInstance().update("editarForm:pnlgOrigenDestino");
    }

    public void cancelarSeleccionarDlgPolvorinGt() {
        tipoDestino = null;
        RequestContext.getCurrentInstance().update("crearForm:pnlgOrigenDestino");
        RequestContext.getCurrentInstance().update("editarForm:pnlgOrigenDestino");
    }

    public void cancelarSeleccionarDlgLugarUsoGt() {
        tipoDestino = null;
        lugarUsoUbigeoDestino = null;
        setMostrarLugarUsoUbigeoOrigen(false);
        setMostrarLugarUsoUbigeoDestino(false);
        RequestContext.getCurrentInstance().update("crearForm:pnlgOrigenDestino");
        RequestContext.getCurrentInstance().update("editarForm:pnlgOrigenDestino");
    }

    /**
     * Autocompletar para la busqueda de recibos
     *
     * @param query
     * @return
     */
    public List<SbPersonaGt> autoCompleteEmpresaTransporte(String query) {
        try {
            List<SbPersonaGt> filteredList = new ArrayList();
            List<SbPersonaGt> lstUniv = ejbPersonaFacade.buscarEmpresaTransporteGtExterna();
            for (SbPersonaGt x : lstUniv) {
                if (x.getRuc().contains(query.trim()) || (x.getRznSocial() != null && x.getRznSocial().contains(query.trim().toUpperCase())) ) {
                    filteredList.add(x);
                }
            }
            return filteredList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * METODO QUE SELECCIONA LA EMPRESA DE TRANSPORTE PARA AGREGARLA A LA GUIA
     * DE TRANSITO
     *
     * @param event
     */
    public void seleccionarDlgEmpresaTransp(SelectEvent event) {
        empresaTransporte = (SbPersonaGt) event.getObject();
        currentBulk.setEmpresaTranspId(empresaTransporte);
        if (currentBulk.getId() == null) {
            RequestContext.getCurrentInstance().update("crearForm:pnlgEmpresaTransporte");
        } else {
            RequestContext.getCurrentInstance().update("editarForm:pnlgEmpresaTransporte");
        }
    }

    public void seleccionarDlgEmpresaTranspBulk(SelectEvent event) {
        empresaTransporte = (SbPersonaGt) event.getObject();
        currentBulk.setEmpresaTranspId(empresaTransporte);
        setDisableEmpTransporte(true);
        if (currentBulk.getId() == null) {
            RequestContext.getCurrentInstance().update("crearForm:pnlgEmpresaTransporte");
        } else {
            RequestContext.getCurrentInstance().update("editarForm:pnlgEmpresaTransporte");
        }
    }

    public void confirmarTransmitir() {
        flgAceptoTransmitir = false;
        if (registrosGteRegSeleccionados != null && !registrosGteRegSeleccionados.isEmpty()) {
            Long idTemp = 0L;
            String resolucion = "";
            boolean validagte = true, validateVenc = false;
            Calendar c_hoy = Calendar.getInstance();
            for (EppGteRegistro rgtr : registrosGteRegSeleccionados) {
                if (!rgtr.getEstado().getCodProg().equals("TP_REGEV_CRE")) {
                    validagte = false;
                    break;
                }
                if(rgtr.getResolucionId() != null){
                    if (JsfUtil.getFechaSinHora(c_hoy.getTime()).compareTo(JsfUtil.getFechaSinHora(rgtr.getResolucionId().getFechaFin())) > 0) {
                        // VENCIDA
                        validagte = false;
                        validateVenc = true;
                        idTemp = rgtr.getId();
                        resolucion = rgtr.getResolucionId().getNumero();
                        break;
                    }
                }
            }
            if (validagte) {
                RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmTransmitir').show()");
                RequestContext.getCurrentInstance().update("frmConfirmTransmitir");
            } else {
                if(validateVenc){
                    JsfUtil.mensajeAdvertencia("El registro nro. "+idTemp+" tiene la resolución " + resolucion + " vencida.");
                }else{
                    JsfUtil.mensajeAdvertencia("Sólo puede Transmitir registros en estado Creado, debe verificar.");
                }
            }
        } else {
            JsfUtil.mensajeAdvertencia("No ha seleccionado ningún regístro para Transmitir");
        }
    }

    public boolean validarTransmitir() {
        if (tipoEstadoBandeja != null) {
            if (tipoEstadoBandeja.getCodProg().equals("TP_REGEV_CRE")) {
                setRenderValidarTransmitir(true);
            } else {
                setRenderValidarTransmitir(false);
            }
        } else {
            setRenderValidarTransmitir(false);
        }
        return isRenderValidarTransmitir();
    }

    public String mostrarFechaString(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());

            return dia + " de " + mes + " de " + anio;
        } else {
            return "BORRADOR";
        }
    }

    /**
     * Abre la ventana de dialogo crear policia
     */
    public void openDlgCrearPolicia() {
        policia = new EppGuiaTransitoPolicia();
        strVerifDigit = null;
        RequestContext.getCurrentInstance().execute("PF('wvCrearPolicia').show()");
        RequestContext.getCurrentInstance().update("cpForm");
    }

    /**
     * Metodo para registrar policia
     */
    public void crearPolicia() {
        try {
            if (validarCrearPolicia()) {
                policia.setActivo((short) 1);
                policia.setId(JsfUtil.tempId());
                //eppGuiaTransitoPoliciaFacade.create(policia);
                if (current.getEppGuiaTransitoPoliciaList() == null) {
                    current.setEppGuiaTransitoPoliciaList(new ArrayList());
                    current.getEppGuiaTransitoPoliciaList().add(policia);
                } else {
                    current.getEppGuiaTransitoPoliciaList().add(policia);
                }
                RequestContext.getCurrentInstance().update("verDatosAdicForm:pnlDatosCustodio");
                RequestContext.getCurrentInstance().update("editarDatosAdicForm:pnlDatosCustodio");
                RequestContext.getCurrentInstance().execute("PF('wvCrearPolicia').hide()");
                //JsfUtil.mensaje("Policía registrado correctamente. ID: " + policia.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al registrar el policía");
        }
    }

    /**
     * Valida el registro de policia
     *
     * @return
     */
    private boolean validarCrearPolicia() {
        boolean valida = true;

        if (policia.getDniPolicia() != null) {
            if (!ejbPersonaFacade.selectPersonaActivaxDoc(policia.getDniPolicia()).isEmpty()) {
                SbPersonaGt p = ejbPersonaFacade.selectPersonaActivaxDoc(policia.getDniPolicia()).get(0);
                policia.setNombresPolicia(p.getNombres());
                policia.setApaternoPolicia(p.getApePat());
                policia.setAmaternoPolicia(p.getApeMat());
            } else {
                SbPersonaGt p = new SbPersonaGt();
//                if (strVerifDigit != null && !"".equals(strVerifDigit)) {
                p.setNumDoc(policia.getDniPolicia());
                //p.setNumDocVal(strVerifDigit);
                p = wsPideController.buscarReniec(p);
                if (p != null) {
                    String x = "";
                    if (p.getNombres() != null) {
                        policia.setNombresPolicia(p.getNombres());
                        policia.setApaternoPolicia(p.getApePat());
                        policia.setAmaternoPolicia(p.getApeMat());
                    } else {
                        policia.setNombresPolicia(p.getNombres());
                        policia.setApaternoPolicia(p.getApePat());
                        policia.setAmaternoPolicia(p.getApeMat());
                        valida = false;
                        JsfUtil.mensajeAdvertencia("El dni del policía no es válido. Debe verificar.");
                    }
                } else {
                    valida = true;
                }
//                } else {
//                    valida = false;
//                    JsfUtil.mensajeAdvertencia("Debe ingresar el digito de verificación del dni");
//                }
            }
            RequestContext.getCurrentInstance().update("cpForm");

            if (!eppGuiaTransitoPoliciaFacade.buscarPoliciaDuplicado(policia.getDniPolicia()).isEmpty()) {
                valida = false;
                JsfUtil.mensajeAdvertencia("El dni del policía ya está registrado.");
            }

        } else {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar primero el dni del policía");
        }

        if (policia.getNombresPolicia() == null || "".equals(policia.getNombresPolicia())) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el nombre del policía");
        }

        if (policia.getApaternoPolicia() == null || "".equals(policia.getApaternoPolicia())) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el apellido paterno del policía");
        }

        if (policia.getAmaternoPolicia() == null || "".equals(policia.getAmaternoPolicia())) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el apellido materno del policía");
        }
        if (policia.getNroPlaca() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar la placa del policía");
        }

        /*if (policia.getGradoId() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el grado del policía");
        }

        if (policia.getUnidadPolicialId() == null) {
            valida = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar la unidad policía");
        }*/
        return valida;
    }

    public static String mostrarFechaDdMmYyyy(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            //mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            return dia + "/" + mes + "/" + anio;
        } else {
            return "BORRADOR";
        }
    }

    /**
     * Metodo para copiar guia
     *
     * @return
     */
    /*
    public String copiarGuia() {
        current = new EppRegistroGuiaTransito();
        empresa = ejbPersonaFacade.obtenerEmpresaXRuc(JsfUtil.getLoggedUser().getNumDoc());
        //INICIANDO ATRIBUTOS DE REGISTRO
        current.setRegistroId(new EppRegistro());
        current.getRegistroId().setEppDocumentoList(new ArrayList());
        current.getRegistroId().setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
        current.getRegistroId().setTipoProId(ejbTipoBaseFacade.findByCodProg("TP_PRTUP_GUIS").get(0));
        current.getRegistroId().setTipoRegId(ejbTipoBaseFacade.findByCodProg("TP_REGIST_NOR").get(0));
        current.getRegistroId().setTipoOpeId(ejbTipoBaseFacade.findByCodProg("TP_OPE_INI").get(0));
        current.getRegistroId().setEmpresaId(empresa);
        current.getRegistroId().setFecha(new Date());
        current.getRegistroId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
        current.getRegistroId().setAudNumIp(JsfUtil.getIpAddress());
        current.getRegistroId().setActivo((short) 1);
        //////////////////////////////
        EppRegistroGuiaTransito registroGuiaCopia = new EppRegistroGuiaTransito();
        registroGuiaCopia = (EppRegistroGuiaTransito) resultados.getRowData();

        tipoTransporte = registroGuiaCopia.getTipoTransporte();
        fechaLlegada = registroGuiaCopia.getFechaLlegada();
        fechaSalida = registroGuiaCopia.getFechaSalida();
        fechaFactura = registroGuiaCopia.getFechaFactura();
        empresaTransporte = registroGuiaCopia.getEmpresaTranspId();
        cargarRazonSocOrigenDestino(registroGuiaCopia);

        for (EppDocumento d : registroGuiaCopia.getRegistroId().getEppDocumentoList()) {
            if (d.getTipoId().getCodProg().equals("TP_DOC_RMTC") || d.getTipoId().getCodProg().equals("TP_DOC_CMTC")) {
                docmtc = d;
                tipodocumento = d.getTipoId();
            }
        }

        setCriterioResGuia("1");
        setMostrarresglobprev(true);
        resolucionGerencia = registroGuiaCopia.getResolucionId().getRegistroId();
        numeroResolucionGerencia = "" + registroGuiaCopia.getResolucionId().getNumero();
        fechaCaducidadResolucionGerencia = resolucionGerencia.getFecha();
        tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
        lstExplosivosCombo = new ArrayList();
        cargarExplosivos(resolucionGerencia);

        lstTipoGtOrigen = ejbTipoExplosivoFacade.lstTipoODGuiaTransito("1Origen");
        lstTipoGtDestino = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia);

        setBloqueaSelectOrigen(false);
        setBloqueaSelectDestino(false);
        setBloqueaBotonBuscarDestino(false);
        setBloqueaBotonBuscarOrigen(false);

        current.setEppExplosivoSolicitadoList(new ArrayList());
        for (EppExplosivoSolicitado es : registroGuiaCopia.getEppExplosivoSolicitadoList()) {
            if (es.getActivo() == 1) {
                es.setId(JsfUtil.tempId());
                es.setRegistroId(current);
                current.getEppExplosivoSolicitadoList().add(es);
            }
        }

        current.getRegistroId().setEppDocumentoList(new ArrayList());
        for (EppDocumento d : registroGuiaCopia.getRegistroId().getEppDocumentoList()) {
            if (d.getActivo() == 1) {
                current.getRegistroId().getEppDocumentoList().add(d);
            }
        }
        current.setEmpresaId(empresa);
        flgCustodio = current.getCustodia() == 1;
        borrarValoresProducto();
        lstEliminadosExpSolic = null;
        lstEliminadosLicencia = null;
        lstEliminadosDocumento = null;
        //LIMPIANDO DATOS DE RECIBO
        reciboBn = null;
        strNroSecuencia = null;
        strFechaMovimiento = null;
        strImporte = null;

        setDisableReciboBn(false);
        return "/aplicacion/eppRegistroGuiaTransito/CopiarGuia";
    }
     */
    public String copiarGuiaBulk() {
        currentBulk = new EppGteRegistro();
        //empresa = ejbPersonaFacade.obtenerEmpresaXRuc(JsfUtil.getLoggedUser().getNumDoc());
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        lstDocumentosTemp = null;
        //INICIANDO ATRIBUTOS DE REGISTRO
        //currentBulk.setRegistroId(new EppRegistro());
        currentBulk.setEppDocumentoList(new ArrayList());
        currentBulk.setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
        currentBulk.setTipoProId(ejbTipoBaseFacade.findByCodProg("TP_PRTUP_GUIS").get(0));
        currentBulk.setTipoRegId(ejbTipoBaseFacade.findByCodProg("TP_REGIST_NOR").get(0));
        currentBulk.setTipoOpeId(ejbTipoBaseFacade.findByCodProg("TP_OPE_INI").get(0));
        currentBulk.setEmpresaId(empresa);
        currentBulk.setFecha(new Date());
        currentBulk.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        currentBulk.setAudNumIp(JsfUtil.getIpAddress());
        currentBulk.setActivo((short) 1);
        llenarTiposGTE();
        
        if(!cargarDatos()){
            return null;
        }
        
        //////////////////////////////
        EppGteRegistro registroGuiaCopia = new EppGteRegistro();
        registroGuiaCopia = (EppGteRegistro) resultadosGteReg.getRowData();

        tipoTransporte = registroGuiaCopia.getTipoTransporte();
        //fechaLlegada = registroGuiaCopia.getFechaLlegada();
        //fechaSalida = registroGuiaCopia.getFechaSalida();
        //fechaFactura = registroGuiaCopia.getFechaFactura();
        empresaTransporte = registroGuiaCopia.getEmpresaTranspId();
        cargarRazonSocOrigenDestinoBulk(registroGuiaCopia);

        for (EppDocumento d : registroGuiaCopia.getEppDocumentoList()) {
            if (d.getTipoId().getCodProg().equals("TP_DOC_RMTC") || d.getTipoId().getCodProg().equals("TP_DOC_CMTC")) {
                docmtc = d;
                tipodocumento = d.getTipoId();
            }
        }
        lstDocumentosTemp = registroGuiaCopia.getEppDocumentoList();
        
        obtenerTipoGTECurrentBulk(true, registroGuiaCopia);
        if(resolucionGerencia != null){
            fechaCaducidadResolucionGerencia = resolucionGerencia.getFecha();
            strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
        }   
        
        setBloqueaSelectOrigen(false);
        setBloqueaSelectDestino(false);
        setBloqueaBotonBuscarDestino(false);
        setBloqueaBotonBuscarOrigen(false);

        currentBulk.setEppGteExplosivoSolicitaList(new ArrayList());
        for (EppGteExplosivoSolicita es : registroGuiaCopia.getEppGteExplosivoSolicitaList()) {
            if (es.getActivo() == 1) {
                es.setId(JsfUtil.tempId());
                es.setRegistroId(currentBulk);
                currentBulk.getEppGteExplosivoSolicitaList().add(es);
            }
        }

        currentBulk.setEppDocumentoList(new ArrayList());
        for (EppDocumento d : registroGuiaCopia.getEppDocumentoList()) {
            if (d.getActivo() == 1) {
                currentBulk.getEppDocumentoList().add(d);
            }
        }
        currentBulk.setEmpresaId(empresa);
        flgCustodio = currentBulk.getCustodia() == 1;
        borrarValoresProducto();
        lstEliminadosExpSolic = null;
        lstEliminadosLicencia = null;
        lstEliminadosDocumento = null;
        //LIMPIANDO DATOS DE RECIBO
        reciboBn = null;
        reciboBnTemp = null;
        strNroSecuencia = null;
        strFechaMovimiento = null;
        strImporte = null;
        nroDeposito = null;
        fechaDeposito = null;
        montoDeposito = null;
        depositoAdjunto = null;
        depositoAdjuntoTemp = null;nroDeposito = null;
        montoDeposito = 0D;
        fechaDeposito = null;
        depositoTemp = null;
        depositoAdjunto = null;
        depositoAdjuntoTemp = null;
        fileDeposito = null;
        depositoByte = null;
        depositoStreamed = null;
        depositoTemp = null;
        

        setDisableReciboBn(false);
        return "/aplicacion/eppRegistroGuiaTransito/CopiarGuia";
    }

    public void limpiarEmpresaTransporte() {
        //empresaTransporte = null;
        borrarEmpTransporteSeleccionado();
    }

    /**
     * RETORNA EL VALOR PARA LA PERSONA DESDE EL DIALOG
     *
     * @param event
     */
    public void seleccionarRgReporte(SelectEvent event) {
        try {
            rgReporte = (EppRegistro) event.getObject();
            setDisableRgReporte(true);
            lstReporteRg = new ArrayList();
            lstReporteRg.add(rgReporte);

            lstReporteGuias = new ArrayList();
            lstReporteGuias = eppRegistroGuiaTransitoFacade.buscarGuiasReporte(rgReporte.getEppResolucion());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void borrarRgSeleccionada() {
        lstReporteRg = new ArrayList();
        lstReporteGuias = new ArrayList();
        rgReporte = null;
        setDisableRgReporte(false);
    }

    /**
     *
     * @param rdet
     * @return
     */
    public List<EppDetalleAutUso> lstActivosDetalleAutUso(EppRegistro rdet) {
        List<EppDetalleAutUso> lstFinal = new ArrayList();
        if (rdet != null) {
            if (rdet.getEppDetalleAutUsoList() != null) {
                if (!rdet.getEppDetalleAutUsoList().isEmpty()) {
                    for (EppDetalleAutUso edat : rdet.getEppDetalleAutUsoList()) {
                        if (edat.getActivo() == 1) {
                            lstFinal.add(edat);
                        }
                    }
                }
            }
        }
        return lstFinal;
    }

    /**
     *
     * @param rgtdet
     * @return
     */
    public List<EppExplosivoSolicitado> lstActivosDetalleGuiaTransito(EppRegistroGuiaTransito rgtdet) {
        List<EppExplosivoSolicitado> lstFinal = new ArrayList();
        if (rgtdet != null) {
            if (rgtdet.getEppExplosivoSolicitadoList() != null) {
                if (!rgtdet.getEppExplosivoSolicitadoList().isEmpty()) {
                    for (EppExplosivoSolicitado edat : rgtdet.getEppExplosivoSolicitadoList()) {
                        if (edat.getActivo() == 1) {
                            lstFinal.add(edat);
                        }
                    }
                }
            }
        }
        return lstFinal;
    }

    public void confirmarEmitir() {
        RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmEmitir').show()");
        RequestContext.getCurrentInstance().update("frmConfirmEmitir");
    }

    private List<OpcionesBandejaGte> lstOpcionesBandeja = null;

    public List<OpcionesBandejaGte> getLstOpcionesBandeja() {
        return lstOpcionesBandeja;
    }

    public void setLstOpcionesBandeja(List<OpcionesBandejaGte> lstOpcionesBandeja) {
        this.lstOpcionesBandeja = lstOpcionesBandeja;
    }

    public void lstnrMostrarTabla() {

        resultados = null;
        registrosSeleccionados = null;
        resultadosGteReg = null;
        registrosGteRegSeleccionados = null;

        if (tipoEstadoBandeja != null) {
            lstOpcionesBandeja = new ArrayList();
            tipoBusqueda = null;
            switch (tipoEstadoBandeja.getCodProg()) {
                case "TP_REGEV_CRE":
                case "TP_REGEV_ANU":
                case "TP_REGEV_TRAN":
                    setRenderTablaGteRegistro(true);
                    setRenderTablaRegistro(false);
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("4", "NRO. RG"));
                    if(esGtSinTransf){
                        lstOpcionesBandeja.add(new OpcionesBandejaGte("6", "FECHA DE SOLICITUD"));
                    }
                    break;
                case "TP_REGEV_FIN":
                    setRenderTablaGteRegistro(false);
                    setRenderTablaRegistro(true);
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("1", "NRO. EXPEDIENTE"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("2", "NRO. GUIA"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("3", "FECHA EMISIÓN"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("4", "NRO. RG"));
                    break;
                default:
                    setRenderTablaGteRegistro(false);
                    setRenderTablaRegistro(false);
                    break;
            }

        } else {
            setRenderTablaGteRegistro(false);
            setRenderTablaRegistro(false);
        }
        setRenderValidarTransmitir(validarTransmitir());
    }

    public void lstnrMostrarTablaFabricante() {
        if (tipoEstadoBandeja != null) {
            lstOpcionesBandeja = new ArrayList();
            tipoBusqueda = null;
            filtro = null;
            switch (tipoEstadoBandeja.getCodProg()) {
                case "TP_REGEV_TRAN":
                    setRenderTablaGteRegistro(true);
                    setRenderTablaRegistro(false);
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("1", "NOMBRE COMPRADOR"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("2", "RUC COMPRADOR"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("3", "DESTINO"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("5", "EXPLOSIVO"));
                    break;
                case "TP_REGEV_FIN":
                    setRenderTablaGteRegistro(false);
                    setRenderTablaRegistro(true);
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("1", "NOMBRE COMPRADOR"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("2", "RUC COMPRADOR"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("3", "DESTINO"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("4", "NRO. GUIA"));
                    lstOpcionesBandeja.add(new OpcionesBandejaGte("5", "EXPLOSIVO"));
                    break;
                default:
                    setRenderTablaGteRegistro(false);
                    setRenderTablaRegistro(false);
                    break;
            }

        } else {
            setRenderTablaGteRegistro(false);
            setRenderTablaRegistro(false);
        }
    }

    public String mostrarNombreOrigenGteBulk(EppGteRegistro rgt, boolean booSubStr) {
        String razonSocial = "";
        if (rgt.getOrigenAlmacen() != null) {
            razonSocial = "" + rgt.getOrigenAlmacen().getRazonSocial() + " - " + rgt.getOrigenAlmacen().getDireccion();
        }
        if (rgt.getOrigenPolvorin() != null) {
            razonSocial = "" + rgt.getOrigenPolvorin().getDescripcion() + " - " + rgt.getOrigenPolvorin().getDireccion();
        }
        if (rgt.getOrigenAlmacenAduana() != null) {
            razonSocial = "" + rgt.getOrigenAlmacenAduana().getRazonSocial() + " - " + rgt.getOrigenAlmacenAduana().getDireccion();
        }
        if (rgt.getPuertoAduaneroOrigen() != null) {
            razonSocial = "" + rgt.getPuertoAduaneroOrigen().getNombre() + " - " + rgt.getPuertoAduaneroOrigen().getDireccion();
        }
        if (rgt.getLugarUsoOrigen() != null) {
            razonSocial = "" + rgt.getLugarUsoOrigen().getNombre();
        }
        if (rgt.getOrigenFabrica() != null) {
            if(rgt.getOrigenAlquilerId()!= null){
                razonSocial = rgt.getOrigenAlquilerId().getArrendatariaId().getRznSocial() + " - " + rgt.getOrigenFabrica().getDireccion();
            }else{
                razonSocial = "" + rgt.getOrigenFabrica().getPersonaId().getRznSocial() + " - " + rgt.getOrigenFabrica().getDireccion();
            }
            
        }
        if (booSubStr) {
            if (razonSocial != null && !"".equals(razonSocial)) {
                if (razonSocial.length() > 130) {
                    razonSocial = razonSocial.substring(0, 130) + "... ";
                } else {
                    razonSocial = razonSocial;//.substring(0, razonSocial.length() - 1) + "... ";
                }
            }
        }

        return razonSocial;
    }
    
    
    public String mostrarNombreDestinoGteBulk(EppGteRegistro rgt, boolean booSubStr) {
        String razonSocial = "";
        if (rgt.getDestinoAlmacen() != null) {
            razonSocial = "" + rgt.getDestinoAlmacen().getRazonSocial() + " - " + rgt.getDestinoAlmacen().getDireccion();
        }
        if (rgt.getDestinoPolvorin() != null) {
            razonSocial = "" + rgt.getDestinoPolvorin().getDescripcion() + " - " + rgt.getDestinoPolvorin().getDireccion();
        }
        if (rgt.getDestinoAlmacenAduana() != null) {
            razonSocial = "" + rgt.getDestinoAlmacenAduana().getRazonSocial() + " - " + rgt.getDestinoAlmacenAduana().getDireccion();
        }
        if (rgt.getPuertoAduaneroDestino() != null) {
            razonSocial = "" + rgt.getPuertoAduaneroDestino().getNombre() + " - " + rgt.getPuertoAduaneroDestino().getDireccion();
        }
        if (rgt.getLugarUsoDestino() != null) {
            razonSocial = "" + ejbLugarUsoFacade.find(rgt.getLugarUsoDestino().getId()).getNombre();
        }
        if (rgt.getDestinoFabrica() != null) {
            if(rgt.getDestinoAlquilerId()!= null){
                razonSocial = rgt.getDestinoAlquilerId().getArrendatariaId().getRznSocial() + " - " + rgt.getDestinoFabrica().getDireccion();
            }else{
                razonSocial = "" + rgt.getDestinoFabrica().getPersonaId().getRznSocial() + " - " + rgt.getDestinoFabrica().getDireccion();
            }
        }

        if (booSubStr) {
            if (razonSocial != null && !"".equals(razonSocial)) {
                if (razonSocial.length() > 130) {
                    razonSocial = razonSocial.substring(0, 130) + "... ";
                } else {
                    razonSocial = razonSocial;//.substring(0, razonSocial.length() - 1) + "... ";
                }
            }
        }

        return razonSocial;
    }

    /**
     *
     * @param regId
     * @param expId
     * @return
     */
    public String saldoActualRpt(EppResolucion regId, EppExplosivo expId) {
        return "" + eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoRgGee(regId, expId);
    }

    public boolean permitirCrearListarReporte(SbPersonaGt p) {
        return !ejbRegistroFacade.buscarResolucionesGuiaExterna(null, p).isEmpty();
    }

    public boolean permitirEmitir(SbPersonaGt p) {
        return !ejbRegistroFacade.buscarResolucionesPlanta(null, p).isEmpty();
    }
    
    public boolean permitirSinTransfer(SbPersonaGt p) {
        return !ejbRegistroFacade.buscarResolucionesPlanta(null, p).isEmpty() || !ejbRegistroFacade.buscarResolucionesGuiaExterna(null, p).isEmpty();
    }

    public StreamedContent solicitudGte(EppGteRegistro pReg) {
        try {
            List<EppGteRegistro> lp = new ArrayList();
            lp.add(pReg);
            if (pReg.getEstado().getCodProg().equals("TP_REGEV_FIN")) {
                if (JsfUtil.buscarPdfRepositorio(pReg.getId().toString(), JsfUtil.bundleGt("Documentos_pathUpload_solicitudGte"))) {
                    return JsfUtil.obtenerArchivo("Documentos_pathUpload_solicitudGte", pReg.getId() + ".pdf", pReg.getId() + ".pdf", "application/pdf");
                } else {
                    subirSolicitudPdf(pReg);
                    return JsfUtil.obtenerArchivo("Documentos_pathUpload_solicitudGte", pReg.getId() + ".pdf", pReg.getId() + ".pdf", "application/pdf");
                }
            } else {
                return JsfUtil.generarReportePdf(JsfUtil.bundleGt("rutaSolicitudPdfGte"), lp, parametrosSolicitudGte(pReg), "gt.pdf", null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: EppRegistroGuiaTransitoController.reporte :", ex);
        }
    }

    private HashMap parametrosSolicitudGte(EppGteRegistro ppdf) {
        try {
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            HashMap p = new HashMap();
            p.put("p_dir_emp", ReportUtilGuiaTransito.mostrarDireccionPersona(ppdf.getEmpresaId()));
            p.put("P_IMG_DIR", ec.getRealPath("/resources/imagenes/") + "/");
            p.put("logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
            p.put("P_IMG_FIRMA", mostrarFirmaXOde());
            p.put("P_TIPO_GT", "(USO EXCLUSIVO PARA TRÁMITE DE CUSTODIA)");
            p.put("PARRAFO2", mostrarDatosOrigenBulk(ppdf, true));
            p.put("PARRAFO3", mostrarDatosDestinoBulk(ppdf, true));
            p.put("FECHAACTUAL", fechaSolicitudGtePdf(ppdf));
            p.put("productos", listarExpSolicActivosGte(ppdf.getEppGteExplosivoSolicitaList()));
            p.put("P_NRO_GT", ppdf.getNroSolicitud());
            p.put("P_EMPRESA_STR", mostrarClienteString(ppdf.getEmpresaId()));
            p.put("P_EMPRESA_TRANSP_STR", mostrarClienteString(ppdf.getEmpresaTranspId()) == null ? "" : ("Los explosivos serán trasladados por:<br/>"
                    + "EMPRESA DE TRANSPORTES: " + mostrarClienteString(ppdf.getEmpresaTranspId())));
            if (ppdf.getResolucionId() != null) {
                if (ppdf.getResolucionId().getNumero() != null) {
                    p.put("P_RG_REF_GT", "REF. R.G.: <b>" + ppdf.getResolucionId().getNumero() + "</b>");
                } else {
                    p.put("P_RG_REF_GT", "");
                }
            } else {
                p.put("P_RG_REF_GT", "");
            }
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean subirSolicitudPdf(EppGteRegistro pReg) {
        boolean valida = true;
        try {
            List<EppGteRegistro> lp = new ArrayList();
            lp.add(pReg);
            JsfUtil.subirPdfGte(pReg.getId().toString(), parametrosSolicitudGte(pReg), lp, JsfUtil.bundleGt("rutaSolicitudPdfGte"), JsfUtil.bundleGt("Documentos_pathUpload_solicitudGte"));
        } catch (Exception ex) {
            JsfUtil.mensajeError("No se pudo subir el archivo: " + ex.getMessage());
            valida = false;
        }
        return valida;
    }

    public void buscarSolicitudesGte() {
        try {
            resultadosGteReg = null;
            if (filtro == null || filtro.equals("")) {
                resultadosGteReg = new ListDataModel<>(ejbRegistroFacade.buscarSolicitudesGuiaExterna(filtro, empresa));
            } else {
                List<EppGteRegistro> lst = ejbRegistroFacade.buscarSolicitudesGuiaExterna(filtro, empresa);
                if (lst.isEmpty()) {
                    JsfUtil.mensajeAdvertencia("El Nro. de Solicitud de GTE ingresado no existe.");
                } else {
                    boolean permiteListar = true;
                    for (EppGteRegistro eppgter : lst) {
                        if (eppgter.getRegistroGtId() != null) {
                            permiteListar = false;
                            break;
                        }
                    }
                    if (permiteListar) {
                        resultadosGteReg = new ListDataModel<>(lst);
                    } else {
                        JsfUtil.mensajeAdvertencia("El Nro. de Solicitud de GTE ingresado ya tiene Guía de Tránsito Electrónica generada.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> headers(int tipoh) {
        List<String> lstHeaders = new ArrayList<>();

        switch (tipoh) {
            case 1:
                lstHeaders.add("ID");
                lstHeaders.add("NRO_COMPROBANTE");
                lstHeaders.add("FECHA_COMPROBANTE");
                lstHeaders.add("RG_AUT_USO");
                lstHeaders.add("ORIGEN");
                lstHeaders.add("DESTINO");
                lstHeaders.add("ESTADO");
                lstHeaders.add("NRO_GTE");
                lstHeaders.add("RESPONSABLE");
                lstHeaders.add("NOMBRE EXPLOSIVO");
                lstHeaders.add("CANTIDAD");
                lstHeaders.add("UNIDAD DE MEDIDA");
                break;
            case 2:
                lstHeaders.add("ID");
                lstHeaders.add("NRO_COMPROBANTE");
                lstHeaders.add("FECHA_COMPROBANTE");
                lstHeaders.add("RG_AUT_USO");
                lstHeaders.add("ORIGEN");
                lstHeaders.add("DESTINO");
                lstHeaders.add("ESTADO");
                lstHeaders.add("NRO_SOLICITUD");
                lstHeaders.add("RESPONSABLE");
                lstHeaders.add("NOMBRE EXPLOSIVO");
                lstHeaders.add("CANTIDAD");
                lstHeaders.add("UNIDAD DE MEDIDA");
                break;
            case 3:
                lstHeaders.add("NUMERO DE GUIA");
                lstHeaders.add("EXPEDIENTE");
                lstHeaders.add("USUARIO");
                lstHeaders.add("VIGENCIA INICIO");
                lstHeaders.add("VIGENCIA FIN");
                lstHeaders.add("TIPO GUIA");
                lstHeaders.add("ESTADO");
                lstHeaders.add("EMPRESA DE TRANSPORTE");
                lstHeaders.add("ORIGEN");
                lstHeaders.add("DESTINO");
                lstHeaders.add("PRODUCTO");
                lstHeaders.add("CANTIDAD");
                lstHeaders.add("CANTIDAD EXTORNADA");
                break;
            default:
                break;
        }

        return lstHeaders;
    }

    public StreamedContent downloadExcel(int tipo) {
        switch (tipo) {
            case 1:
                return new DefaultStreamedContent(new ByteArrayInputStream(excelListadoUsuario(generarTablaReporte(resultados), headers(1))), "application/vnd.ms-excel", "reporte.xls");
            case 2:
                return new DefaultStreamedContent(new ByteArrayInputStream(excelListadoUsuarioTemp(generarTablaReporteTemp(resultadosGteReg), headers(2))), "application/vnd.ms-excel", "reporte.xls");
            case 3:
                return new DefaultStreamedContent(new ByteArrayInputStream(excelListadoReporteTraslado(lstReporteGuias, headers(3))), "application/vnd.ms-excel", "reporte.xls");
            default:
                return null;
        }
    }

    public List<EppRegistroGuiaTransito> generarTablaReporte(ListDataModel model) {
        List<EppRegistroGuiaTransito> lstRgt = new ArrayList();

        //CONVERTIMOS EL MODELO EN UN LIST SIMPLE
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setRowIndex(i);
            lstRgt.add((EppRegistroGuiaTransito) model.getRowData());
        }

        return lstRgt;
    }

    public List<EppGteRegistro> generarTablaReporteTemp(ListDataModel model) {
        List<EppGteRegistro> lstRgt = new ArrayList();

        //CONVERTIMOS EL MODELO EN UN LIST SIMPLE
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setRowIndex(i);
            lstRgt.add((EppGteRegistro) model.getRowData());
        }

        return lstRgt;
    }

    private byte[] excelListadoUsuario(List<EppRegistroGuiaTransito> table, List<String> camposExcel) {
        Row row = null;
        Cell cell = null;
        try {
            Workbook wb = new HSSFWorkbook();
            HSSFCellStyle styleHeader = (HSSFCellStyle) wb.createCellStyle();
            HSSFFont fontHeader = (HSSFFont) wb.createFont();
            fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            styleHeader.setFont(fontHeader);
            Sheet sheet = wb.createSheet("sheet");
            row = sheet.createRow((short) 0);

            int i = 0;
            for (String campo : camposExcel) {
                cell = row.createCell(i++);
                cell.setCellValue(campo.toUpperCase());
                cell.setCellStyle(styleHeader);
            }

            int j = 1;

            for (EppRegistroGuiaTransito gt : table) {
                row = sheet.createRow((short) j++);
                i = 0;
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getRegistroId().getId());
                
                //NRO COMPROBANTE
                cell = row.createCell(i++);
                cell.setCellValue(obtenerComprobanteGTE(gt, 1));
                //FECHA COMPROBANTE
                cell = row.createCell(i++);
                cell.setCellValue(obtenerComprobanteGTE(gt, 2));
                //NRO RESOLUCION
                cell = row.createCell(i++);
                cell.setCellValue("" + ( (gt.getResolucionId() != null)?gt.getResolucionId().getNumero():"" ));
                //ORIGEN
                cell = row.createCell(i++);
                cell.setCellValue("" + mostrarNombreOrigen(gt, false));
                //DESTINO
                cell = row.createCell(i++);
                cell.setCellValue("" + mostrarNombreDestino(gt, false));
                //ESTADO
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getRegistroId().getEstado().getNombre());
                //NRO GTE
                cell = row.createCell(i++);
                cell.setCellValue("" + (gt.getRegistroId().getEppResolucion().getNumero() == null ? "" : gt.getRegistroId().getEppResolucion().getNumero()));
                //RESPONSABLE
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getRegistroId().getAudLogin());
            }

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            wb.write(file);
            return file.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] excelListadoUsuarioTemp(List<EppGteRegistro> table, List<String> camposExcel) {
        Row row = null;
        Cell cell = null;
        try {
            Workbook wb = new HSSFWorkbook();
            HSSFCellStyle styleHeader = (HSSFCellStyle) wb.createCellStyle();
            HSSFFont fontHeader = (HSSFFont) wb.createFont();
            fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            styleHeader.setFont(fontHeader);
            Sheet sheet = wb.createSheet("sheet");
            row = sheet.createRow((short) 0);

            int i = 0;
            for (String campo : camposExcel) {
                cell = row.createCell(i++);
                cell.setCellValue(campo.toUpperCase());
                cell.setCellStyle(styleHeader);
            }

            int j = 1;

            for (EppGteRegistro gt : table) {
                row = sheet.createRow((short) j++);
                i = 0;
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getId());
                //NRO COMPROBANTE
                cell = row.createCell(i++);
                cell.setCellValue("");
                //FECHA COMPROBANTE
                cell = row.createCell(i++);
                cell.setCellValue("");
                //NRO RESOLUCION
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getResolucionId().getNumero());
                //ORIGEN
                cell = row.createCell(i++);
                cell.setCellValue("" + mostrarNombreOrigenGteBulk(gt, false));
                //DESTINO
                cell = row.createCell(i++);
                cell.setCellValue("" + mostrarNombreDestinoGteBulk(gt, false));
                //ESTADO
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getEstado().getNombre());
                //NRO SOLICITUD
                cell = row.createCell(i++);
                cell.setCellValue("" + (gt.getNroSolicitud() == null ? "" : gt.getNroSolicitud()));
                //RESPONSABLE
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getAudLogin());
            }

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            wb.write(file);
            return file.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] excelListadoReporteTraslado(List<EppRegistroGuiaTransito> table, List<String> camposExcel) {
        Row row = null;
        Cell cell = null;
        try {
            Workbook wb = new HSSFWorkbook();
            HSSFCellStyle styleHeader = (HSSFCellStyle) wb.createCellStyle();
            HSSFFont fontHeader = (HSSFFont) wb.createFont();
            fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            styleHeader.setFont(fontHeader);
            Sheet sheet = wb.createSheet("sheet");
            row = sheet.createRow((short) 0);

            int i = 0;
            for (String campo : camposExcel) {
                cell = row.createCell(i++);
                cell.setCellValue(campo.toUpperCase());
                cell.setCellStyle(styleHeader);
            }

            int j = 1;

            for (EppRegistroGuiaTransito gt : table) {
                for (EppExplosivoSolicitado es : gt.getEppExplosivoSolicitadoList()) {
                    row = sheet.createRow((short) j++);
                    i = 0;
                    //1.NUMERO DE GUIA
                    cell = row.createCell(i++);
                    cell.setCellValue("" + gt.getRegistroId().getEppResolucion().getNumero());
                    //2.EXPEDIENTE
                    cell = row.createCell(i++);
                    cell.setCellValue("" + gt.getRegistroId().getNroExpediente());
                    //3.USUARIO
                    cell = row.createCell(i++);
                    cell.setCellValue("" + gt.getAudLogin());
                    //4.VIGENCIA INICIO
                    cell = row.createCell(i++);
                    cell.setCellValue("" + mostrarFechaDdMmYyyy(gt.getRegistroId().getEppResolucion().getFechaIni()));
                    //5.VIGENCIA FIN
                    cell = row.createCell(i++);
                    cell.setCellValue("" + mostrarFechaDdMmYyyy(gt.getRegistroId().getEppResolucion().getFechaFin()));
                    //6.TIPO GUIA
                    cell = row.createCell(i++);
                    cell.setCellValue("" + gt.getTipoTramite().getNombre());
                    //7.ESTADO
                    cell = row.createCell(i++);
                    cell.setCellValue("" + gt.getRegistroId().getEstado().getNombre());
                    //8.EMPRESA DE TRANSPORTE
                    cell = row.createCell(i++);
                    cell.setCellValue("" + mostrarClienteString(gt.getEmpresaTranspId()));
                    //9.ORIGEN
                    cell = row.createCell(i++);
                    cell.setCellValue("" + mostrarNombreOrigen(gt, false));
                    //10.DESTINO
                    cell = row.createCell(i++);
                    cell.setCellValue("" + mostrarNombreDestino(gt, false));
                    //11.PRODUCTO
                    cell = row.createCell(i++);
                    cell.setCellValue("" + es.getExplosivoId().getNombre());
                    //12.CANTIDAD
                    cell = row.createCell(i++);
                    cell.setCellValue("" + es.getCantidad());
                    //13.CANTIDAD EXTORNADA
                    cell = row.createCell(i++);
                    cell.setCellValue("" + (es.getCantidadExtornada() == null ? "0" : es.getCantidadExtornada()));
                }
            }

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            wb.write(file);
            return file.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date minDate() {
//        Calendar fecha = Calendar.getInstance();
//        fecha.add(Calendar.DATE, 1);
//        Date fechaNueva = fecha.getTime();
        Date fechaNueva = current.getRegistroId().getEppResolucion().getFechaIni();
        return fechaNueva;
    }
    
    public Date maxDate(){
        return new Date();
    }

    public void lstnrTipoBusqueda() {
        if (tipoBusqueda == null) {
            filtro = null;
            fechaIni = null;
        }
    }

    public void borrarEmpTransporteSeleccionado() {
        empresaTransporte = null;
        currentBulk.setEmpresaTranspId(null);
        setDisableEmpTransporte(false);
    }

    public String colorearFila(EppRegistroGuiaTransito fila) {
        String color = "";
        if (fila != null) {
            if ((fila.getFechaLlegada() == null) || 
                (fila.getEppLicenciaList1() == null || fila.getEppLicenciaList1().isEmpty())) {
                color = "coloredgt";
            }
        }
        return color;
    }

    public List<Map> construirLstGuiaDetailExp(String p_de) {
        return ejbRegistroFacade.rptGuiaDetailExp(p_de);
    }
    
    public List<Map> construirLstGuiaDetailExpGte(String p_de) {
        return ejbRegistroFacade.rptGuiaDetailExpGte(p_de);
    }
    
    public String mostrarNombreOrigenReporte(Map rgt, boolean blnTipo) {
        String razonSocial = "";
        if (rgt.get("ALMORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("ALMORI_NOMBRE");
        }
        if (rgt.get("POLORI_NOMBRE") != null) {
            razonSocial = "" + rgt.get("POLORI_NOMBRE");
            if(rgt.get("ALQORI_NOMBRE") != null){
                razonSocial += " - "+rgt.get("ALQORI_NOMBRE");    
            }            
        }
        if (rgt.get("ADUAORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("ADUAORI_NOMBRE");
        }
        if (rgt.get("PUEORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("PUEORI_NOMBRE");
        }
        if (rgt.get("LUGORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("LUGORI_NOMBRE");
        }
        if (rgt.get("FABORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("FABORI_NOMBRE");
            if(rgt.get("ALQORI_NOMBRE") != null){
                razonSocial = ""+rgt.get("ALQORI_NOMBRE");    
            }
        }
        
        if(blnTipo){
            if(!razonSocial.isEmpty() ){
                if(razonSocial.length() > 100){
                    razonSocial = razonSocial.substring(0,100)+"...";
                }
            }
        }
        
        return razonSocial;
    }
    
    public String mostrarNombreDestinoReporte(Map rgt, boolean blnTipo) {
        String razonSocial = "";
        if (rgt.get("ALMDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("ALMDES_NOMBRE");
        }
        if (rgt.get("POLDES_NOMBRE") != null) { 
            razonSocial = ""+rgt.get("POLDES_NOMBRE");
            if(rgt.get("ALQDES_NOMBRE") != null){
                razonSocial += " - "+rgt.get("ALQDES_NOMBRE");    
            }            
        }
        if (rgt.get("ADUADES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("ADUADES_NOMBRE");
        }
        if (rgt.get("PUEDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("PUEDES_NOMBRE");
        }
        if (rgt.get("LUGDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("LUGDES_NOMBRE");
        }
        if (rgt.get("FABDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("FABDES_NOMBRE");
            if(rgt.get("ALQDES_NOMBRE") != null){
                razonSocial = ""+rgt.get("ALQDES_NOMBRE");    
            }
        }
        
        if(blnTipo){
            if(!razonSocial.isEmpty() ){
                if(razonSocial.length() > 100){
                    razonSocial = razonSocial.substring(0,100) + "...";
                }
            }
        }
        return razonSocial;
    }
    
    public String mostrarNombreOrigenGteBulkReporte(Map rgt, boolean booSubStr) {
        String razonSocial = "";

        if (rgt.get("ALMORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("ALMORI_NOMBRE");
        }
        if (rgt.get("POLORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("POLORI_NOMBRE");
            if(rgt.get("ALQORI_NOMBRE") != null){
                razonSocial += " - "+rgt.get("ALQORI_NOMBRE");    
            }
        }
        if (rgt.get("ADUAORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("ADUAORI_NOMBRE");
        }
        if (rgt.get("PUEORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("PUEORI_NOMBRE");
        }
        if (rgt.get("LUGORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("LUGORI_NOMBRE");
        }
        if (rgt.get("FABORI_NOMBRE") != null) {
            razonSocial = ""+rgt.get("FABORI_NOMBRE");
            if(rgt.get("ALQORI_NOMBRE") != null){
                razonSocial = ""+rgt.get("ALQORI_NOMBRE");    
            }
        }

        if (booSubStr) {
            if (razonSocial != null && !"".equals(razonSocial)) {
                if (razonSocial.length() > 100) {
                    razonSocial = razonSocial.substring(0, 100) + "... ";
                }
            }
        }

        return razonSocial;
    }
    
    public String mostrarNombreDestinoGteBulkReporte(Map rgt, boolean booSubStr) {
        String razonSocial = "";
        
        if (rgt.get("ALMDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("ALMDES_NOMBRE");
        }
        if (rgt.get("POLDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("POLDES_NOMBRE");
            if(rgt.get("ALQDES_NOMBRE") != null){
                razonSocial += " - "+rgt.get("ALQDES_NOMBRE");    
            }
        }
        if (rgt.get("ADUADES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("ADUADES_NOMBRE");
        }
        if (rgt.get("PUEDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("PUEDES_NOMBRE");
        }
        if (rgt.get("LUGDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("LUGDES_NOMBRE");
        }
        if (rgt.get("FABDES_NOMBRE") != null) {
            razonSocial = ""+rgt.get("FABDES_NOMBRE");
            if(rgt.get("ALQDES_NOMBRE") != null){
                razonSocial = ""+rgt.get("ALQDES_NOMBRE");    
            }
        }

        if (booSubStr) {
            if (razonSocial != null && !"".equals(razonSocial)) {
                if (razonSocial.length() > 100) {
                    razonSocial = razonSocial.substring(0, 100) + "... ";
                }
            }
        }

        return razonSocial;
    }
    
    public String emitirGtMap(Long id) {
        Calendar c_hoy = Calendar.getInstance();
        reiniciarValoresVerPreview();
        currentBulk = new EppGteRegistro();
        currentBulk = eppGteRegistroFacade.obtenerGuiaGteById(id);
        
        ////// Validación de RG referenciada MEMO 1160-2017 //////        
        if(currentBulk.getResolucionId() != null){
            if(currentBulk.getResolucionId().getActivo() == 0 || currentBulk.getResolucionId().getRegistroId().getActivo() == 0){
                JsfUtil.mensajeAdvertencia("No se puede emitir esta GTE porque la RG referenciada se encuentra inactiva");
                return null;
            }
        }
        ///////////
        
        tipoTransporte = currentBulk.getTipoTransporte();
        empresaTransporte = currentBulk.getEmpresaTranspId();
    
        setEsSinGTRef(false);
        setEsGtSinTransf(false);
        obtenerTipoGTECurrentBulk(false, currentBulk);
        fechaCaducidadResolucionGerencia = resolucionGerencia.getFecha();
        cargarRazonSocOrigenDestinoBulk(currentBulk);
        strViewEmpresa = mostrarClienteString(currentBulk.getEmpresaId());
        strViewRefRg = "REF. R.G.:<b>" + currentBulk.getResolucionId().getNumero() + "</b>";
        strViewOrigen = mostrarDatosOrigenBulk(currentBulk, false);
        strViewDestino = mostrarDatosDestinoBulk(currentBulk, false);
        strViewEmpTransp = mostrarClienteString(currentBulk.getEmpresaTranspId()) == null ? "" : ("Los explosivos serán trasladados por:<br/>"
                + "EMPRESA DE TRANSPORTES: " + mostrarClienteString(currentBulk.getEmpresaTranspId()));
        return "/aplicacion/eppRegistroGuiaTransito/ViewEmitirGt";
    }
    
    public void anularGtBulkMap(Map reg) {
        try {
            Long id = Long.parseLong(""+reg.get("ID"));
            EppGteRegistro gtanular = eppGteRegistroFacade.obtenerGuiaGteById(id);
            gtanular.setEstado(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_ANU").get(0));
            eppGteRegistroFacade.edit(gtanular);
            
            //SE LIBERA EL RECIBO
            if(gtanular.getSbRecibosList() != null && !gtanular.getSbRecibosList().isEmpty()){
                SbRecibos rb = new SbRecibos();
                for(SbRecibos rec : gtanular.getSbRecibosList()){
                    if(rec.getActivo() == 1){
                        rb = rec;
                        break;
                    }
                }
                SbReciboRegistro rr = new SbReciboRegistro();
                for(SbReciboRegistro recReg : rb.getSbReciboRegistroList()){
                    if(recReg.getActivo() == 1){
                        rr = recReg;
                        break;
                    }
                }
                rr.setActivo(JsfUtil.FALSE);
                ejbSbReciboRegistroFacade.edit(rr);                
            }
            resultadosGuiaGte.remove(reg);
            JsfUtil.mensaje("Guia Anulada correctamente");
        } catch (Exception e) {
            JsfUtil.mensajeError("Ocurrió un error al anular la Guia, " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public StreamedContent solicitudGteMap(Long id) {
        try {
            EppGteRegistro pReg = eppGteRegistroFacade.obtenerGuiaGteById(id);
            List<EppGteRegistro> lp = new ArrayList();
            lp.add(pReg);
            if (pReg.getEstado().getCodProg().equals("TP_REGEV_FIN")) {
                if (JsfUtil.buscarPdfRepositorio(pReg.getId().toString(), JsfUtil.bundleGt("Documentos_pathUpload_solicitudGte"))) {
                    return JsfUtil.obtenerArchivo("Documentos_pathUpload_solicitudGte", pReg.getId() + ".pdf", pReg.getId() + ".pdf", "application/pdf");
                } else {
                    subirSolicitudPdf(pReg);
                    return JsfUtil.obtenerArchivo("Documentos_pathUpload_solicitudGte", pReg.getId() + ".pdf", pReg.getId() + ".pdf", "application/pdf");
                }
            } else {
                return JsfUtil.generarReportePdf(JsfUtil.bundleGt("rutaSolicitudPdfGte"), lp, parametrosSolicitudGte(pReg), "gt.pdf", null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: EppRegistroGuiaTransitoController.reporte :", ex);
        }
    }
    
    public StreamedContent downloadExcelMap(int tipo) {
        switch (tipo) {
            case 1:
                return new DefaultStreamedContent(new ByteArrayInputStream(excelListadoUsuarioMap(resultadosGuia, headers(1))), "application/vnd.ms-excel", "reporte.xls");
            case 2:
                return new DefaultStreamedContent(new ByteArrayInputStream(excelListadoUsuarioTempMap(resultadosGuiaGte, headers(2))), "application/vnd.ms-excel", "reporte.xls");
            case 3:
                return new DefaultStreamedContent(new ByteArrayInputStream(excelListadoReporteTraslado(lstReporteGuias, headers(3))), "application/vnd.ms-excel", "reporte.xls");
            default:
                return null;
        }
    }
    
    private byte[] excelListadoUsuarioMap(List<Map> table, List<String> camposExcel) {
        Row row = null;
        Cell cell = null;
        try {
            Workbook wb = new HSSFWorkbook();
            HSSFCellStyle styleHeader = (HSSFCellStyle) wb.createCellStyle();
            HSSFFont fontHeader = (HSSFFont) wb.createFont();
            fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            styleHeader.setFont(fontHeader);
            Sheet sheet = wb.createSheet("sheet");
            row = sheet.createRow((short) 0);

            int i = 0;
            for (String campo : camposExcel) {
                cell = row.createCell(i++);
                cell.setCellValue(campo.toUpperCase());
                cell.setCellStyle(styleHeader);
            }

            int j = 1;

            for (Map gt : table) {
                row = sheet.createRow((short) j++);
                i = 0;
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.get("ID_REGISTRO") );
                //NRO COMPROBANTE
                cell = row.createCell(i++);
                cell.setCellValue("");
                //FECHA COMPROBANTE
                cell = row.createCell(i++);
                cell.setCellValue("");
                //NRO RESOLUCION
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.get("RESOLUCION_NUMERO"));
                //ORIGEN
                cell = row.createCell(i++);
                cell.setCellValue("" + mostrarNombreOrigenReporte(gt, false));
                //DESTINO
                cell = row.createCell(i++);
                cell.setCellValue("" + mostrarNombreDestinoReporte(gt, false));
                //ESTADO
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.get("ESTADO"));
                //NRO GTE
                cell = row.createCell(i++);
                cell.setCellValue("" + (gt.get("RESOLUCION") == null ? "" : ""+gt.get("RESOLUCION") ));
                //RESPONSABLE
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.get("LOGIN"));
                
                List<Map> lstExplosivos = ejbRegistroFacade.rptGuiaDetailExp("" + gt.get("ID"));
                
                switch(lstExplosivos.size()){
                    case 0:
                            //PRODUCTO
                            cell = row.createCell(i++);
                            cell.setCellValue("");
                            //CANTIDAD
                            cell = row.createCell(i++);
                            cell.setCellValue("");
                            //UNIDADES
                            cell = row.createCell(i++);
                            cell.setCellValue("");
                            break;
                    default:
                            int cont = 0;
                            for(Map mapExp : lstExplosivos){
                                if(cont == 0){
                                    //PRODUCTO
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_NOM"));
                                    //CANTIDAD
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_CANT_AUT"));
                                    //UNIDADES
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_UM"));
                                }else{
                                    row = sheet.createRow((short) j++);
                                    i = 0;
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + gt.get("ID_REGISTRO") );
                                    //NRO COMPROBANTE
                                    cell = row.createCell(i++);
                                    cell.setCellValue("");
                                    //FECHA COMPROBANTE
                                    cell = row.createCell(i++);
                                    cell.setCellValue("");
                                    //NRO RESOLUCION
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + gt.get("RESOLUCION_NUMERO"));
                                    //ORIGEN
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + mostrarNombreOrigenReporte(gt, false));
                                    //DESTINO
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + mostrarNombreDestinoReporte(gt, false));
                                    //ESTADO
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + gt.get("ESTADO"));
                                    //NRO GTE
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + (gt.get("RESOLUCION") == null ? "" : ""+gt.get("RESOLUCION") ));
                                    //RESPONSABLE
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + gt.get("LOGIN"));
                                    //PRODUCTO
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_NOM"));
                                    //CANTIDAD
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_CANT_AUT"));
                                    //UNIDADES
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_UM"));
                                }
                                cont++;
                            }
                            break;
                }
            }

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            wb.write(file);
            return file.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] excelListadoUsuarioTempMap(List<Map> table, List<String> camposExcel) {
        Row row = null;
        Cell cell = null;
        try {
            Workbook wb = new HSSFWorkbook();
            HSSFCellStyle styleHeader = (HSSFCellStyle) wb.createCellStyle();
            HSSFFont fontHeader = (HSSFFont) wb.createFont();
            fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            styleHeader.setFont(fontHeader);
            Sheet sheet = wb.createSheet("sheet");
            row = sheet.createRow((short) 0);

            int i = 0;
            for (String campo : camposExcel) {
                cell = row.createCell(i++);
                cell.setCellValue(campo.toUpperCase());
                cell.setCellStyle(styleHeader);
            }

            int j = 1;

            for (Map gt : table) {
                row = sheet.createRow((short) j++);
                i = 0;
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.get("ID") );
                //NRO COMPROBANTE
                cell = row.createCell(i++);
                cell.setCellValue("");
                //FECHA COMPROBANTE
                cell = row.createCell(i++);
                cell.setCellValue("");
                //NRO RESOLUCION
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.get("RESOLUCION"));
                //ORIGEN
                cell = row.createCell(i++);
                cell.setCellValue("" + mostrarNombreOrigenGteBulkReporte(gt, false));
                //DESTINO
                cell = row.createCell(i++);
                cell.setCellValue("" + mostrarNombreDestinoGteBulkReporte(gt, false));
                //ESTADO
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.get("ESTADO"));
                //NRO SOLICITUD
                cell = row.createCell(i++);
                cell.setCellValue("" + (gt.get("NROSOLICITUD")) == null ? "" : ""+gt.get("NROSOLICITUD") );
                //RESPONSABLE
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.get("LOGIN") );
                
                List<Map> lstExplosivos = ejbRegistroFacade.rptGuiaDetailExpGte("" + gt.get("ID"));
                
                switch(lstExplosivos.size()){
                    case 0:
                            //PRODUCTO
                            cell = row.createCell(i++);
                            cell.setCellValue("");
                            //CANTIDAD
                            cell = row.createCell(i++);
                            cell.setCellValue("");
                            //UNIDADES
                            cell = row.createCell(i++);
                            cell.setCellValue("");
                            break;
                    default:
                            int cont = 0;
                            for(Map mapExp : lstExplosivos){
                                if(cont == 0){
                                    //PRODUCTO
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_NOM"));
                                    //CANTIDAD
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_CANT_AUT"));
                                    //UNIDADES
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_UM"));
                                }else{
                                    row = sheet.createRow((short) j++);
                                    i = 0;
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + gt.get("ID") );
                                    //NRO COMPROBANTE
                                    cell = row.createCell(i++);
                                    cell.setCellValue("");
                                    //FECHA COMPROBANTE
                                    cell = row.createCell(i++);
                                    cell.setCellValue("");
                                    //NRO RESOLUCION
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + gt.get("RESOLUCION"));
                                    //ORIGEN
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + mostrarNombreOrigenGteBulkReporte(gt, false));
                                    //DESTINO
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + mostrarNombreDestinoGteBulkReporte(gt, false));
                                    //ESTADO
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + gt.get("ESTADO"));
                                    //NRO SOLICITUD
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + (gt.get("NROSOLICITUD")) == null ? "" : ""+gt.get("NROSOLICITUD") );
                                    //RESPONSABLE
                                    cell = row.createCell(i++);
                                    cell.setCellValue("" + gt.get("LOGIN") );
                                    //PRODUCTO
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_NOM"));
                                    //CANTIDAD
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_CANT_AUT"));
                                    //UNIDADES
                                    cell = row.createCell(i++);
                                    cell.setCellValue(""+ mapExp.get("EXPS_UM"));
                                }
                                cont++;
                            }
                            break;
                }               
                
                
            }

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            wb.write(file);
            return file.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Abre la ventana de dialogo editar policia
     * @param poli Policia a editar
     */
    public void openDlgEditarPolicia(EppGuiaTransitoPolicia poli) {
        policia = poli;
        strVerifDigit = null;
        RequestContext.getCurrentInstance().execute("PF('wvEditarPolicia').show()");
        RequestContext.getCurrentInstance().update("editarPoliciaForm");
    }
    
    /**
     * Metodo para registrar policia
     */
    public void editarPolicia() {
        try {
           
            for(EppGuiaTransitoPolicia poli : current.getEppGuiaTransitoPoliciaList() ){
                if(Objects.equals(poli.getId(), policia.getId())){
                    poli = policia;
                }
            }

            RequestContext.getCurrentInstance().update("verDatosAdicForm:pnlDatosCustodio");
            RequestContext.getCurrentInstance().update("editarDatosAdicForm:pnlDatosCustodio");
            RequestContext.getCurrentInstance().execute("PF('wvEditarPolicia').hide()");
            
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al editar el policía");
        }
    }
    
    public boolean mostrarBtnEditarDatosAdicionales(EppRegistroGuiaTransito mbe) {
        boolean vda = false;
            
        if(mbe.getFechaLlegada() != null){
            vda = true;
            
            int diasAdic = sbFeriadoFacade.diasLaborables(mbe.getFechaLlegada(), new Date());   
            if(diasAdic > 10){
                vda = false;
            }
        }

        return vda && mbe.getRegistroId().getEstado().getCodProg().equals("TP_REGEV_FIN");
    }
    
    /**
     * Metodo para editar datos adicionales a la GT
     *
     * @return
     */
    public String editarDatosAdicionalesGt() {
        current = new EppRegistroGuiaTransito();
        current = (EppRegistroGuiaTransito) resultados.getRowData();
        tipoTransporte = current.getTipoTransporte();
        empresaTransporte = current.getEmpresaTranspId();

        obtenerTipoGTECurrent(false);        
        fechaLlegada = current.getFechaLlegada();        
        if(fechaLlegada == null){
            setDisabledFechaLlegada(false);
        }else{
            setDisabledFechaLlegada(true);
        }
        if(resolucionGerencia != null){
            strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(resolucionGerencia.getFechaFin()) + "</b>";
        }
        strViewOrigen = "<b>" + mostrarDatosOrigen(current, false) + "</b>";
        strViewDestino = "<b>" + mostrarDatosDestino(current, false) + "</b>";
        strViewTipoTransp = "<b>" + current.getTipoTransporte().getNombre() + "</b>";
        strViewEmpTransp = mostrarClienteString(current.getEmpresaTranspId()) == null ? "" : ("Los explosivos serán trasladados por:<br/>"
                + "EMPRESA DE TRANSPORTES: " + mostrarClienteString(current.getEmpresaTranspId()));
        return "/aplicacion/eppRegistroGuiaTransito/EditDatosAdicGt";
    }
    
    public String editarDatosAdicionales() {
        try {

            if (validaDatosAdic()) {
                
                JsfUtil.borrarIds(current.getEppGuiaTransitoPoliciaList());
                List<EppGuiaTransitoPolicia> lstPolicias = new ArrayList();                        
                JsfUtil.borrarIds(current.getEppGuiaTransitoPoliciaList());
                
                for(EppGuiaTransitoPolicia polic :  current.getEppGuiaTransitoPoliciaList()){
                    if(polic.getId() == null){
                        eppGuiaTransitoPoliciaFacade.create(polic);
                        lstPolicias.add(polic);
                    }else{
                        EppGuiaTransitoPolicia policitaTemp = eppGuiaTransitoPoliciaFacade.buscarPoliciaById(polic.getId());
                        if(policitaTemp != null){
                            if( !Objects.equals(policitaTemp.getGradoId(), polic.getGradoId()) || !Objects.equals(policitaTemp.getNroPlaca(), polic.getNroPlaca())){
                                // Se da de baja al anterior
                                policitaTemp.setActivo(JsfUtil.FALSE);
                                eppGuiaTransitoPoliciaFacade.edit(policitaTemp);
                                
                                // se crea un nuevo registro
                                polic.setId(null);
                                eppGuiaTransitoPoliciaFacade.create(polic);                                
                            }
                            lstPolicias.add(polic);
                        }
                    }
                }
                if(!Objects.equals(archivoTemporal, archivo)){
                    String fileName = file.getFileName();
                    String nombreAdj = "ASISTENCIA_" + current.getId() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                    current.setDocAdjunto(nombreAdj.toUpperCase());
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_guiasExp").getValor() +  current.getDocAdjunto() ), fotoByte );
                }
                
                current.setFechaLlegada(fechaLlegada);
                current.setEppGuiaTransitoPoliciaList(lstPolicias);
                ejbRegistroFacade.edit(current.getRegistroId());
                JsfUtil.mensaje("Datos adicionales actualizados correctamente. Id: " + current.getRegistroId().getId());
                return direccionarDatosAdiGTE();
            } else {
                return "/aplicacion/eppRegistroGuiaTransito/EditDatosAdicGt";
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrio un error al grabar los datos, ERROR: " + e.getMessage());
            return "/aplicacion/eppRegistroGuiaTransito/EditDatosAdicGt";
        }
    }
    
    public String obtenerTipoDocListado(String codProgDoc, String tipoDoc, String ruc){
        String doc = "";
        
        if(codProgDoc != null){
            if(codProgDoc.trim().equals("TP_DOCID_DNI") && ruc != null){
                doc = "RUC";
            }else{
                doc = tipoDoc;
            }
        }else{
            doc = "RUC";
        }
        return doc;
    }
    
    // FABRICAS
     public void buscarFabricaGt() {         
        if (filtro != null && !"".equals(filtro)) {
            String param = "" + JsfUtil.getSessionAttribute("actionODGt");
            Long empresaId = null;
            if (criterioResGuia == null) {
                JsfUtil.mensajeAdvertencia("Por favor, seleccionar primero el tipo de guía de tránsito.");
                return;
            }
            if(param.equals("1")){
                switch(criterioResGuia){
                    case "5":
                            empresaId = currentBulk.getEmpresaId().getId();
                            break;
                    case "6":
                            if(resolucionGerencia == null){
                                JsfUtil.mensajeAdvertencia("Por favor, ingresar la RG de comercialización");
                                return;
                            }
                            empresaId = resolucionGerencia.getEmpresaId().getId();
                            break;
                    default:
                            empresaId = null;
                            break;
                }
            }else{
                empresaId = currentBulk.getEmpresaId().getId();
            }

            lstDlgLocalMap = ejbEppLocalFacade.buscarLocalByDireccionByRznSocialMap(filtro, empresaId);
            ordenarLocales(lstDlgLocalMap);
        }else{
            JsfUtil.mensajeAdvertencia("Por favor, ingresar un criterio de búsqueda ");
        }        
    }
     
    public void ordenarLocales(List<Map> po) {
        if(po != null){
            Collections.sort(po, new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    return (Integer.parseInt(o1.get("ID").toString()) > Integer.parseInt(o2.get("ID").toString()))?1:((Integer.parseInt(o1.get("ID").toString()) == Integer.parseInt(o2.get("ID").toString()))?0:-1);
                }
            });
        }
    } 
     
    public void seleccionarDlgFabricacionGt(Map localMap) {
        if(localMap != null){
            EppLocal loc = ejbEppLocalFacade.find(localMap.get("ID"));
            String param = "" + JsfUtil.getSessionAttribute("actionODGt");
            if (tipoOrigen != null && param.equals("1")) {
                if (tipoOrigen.getCodProg().equals("TP_ODRGT_FAB")) {
                    fabrica = loc;
                    polvorin = null;
                    lugarUso = null;
                    //nombreEmpresaOrigen = fabrica.getPersonaId().getRznSocial();
                    nombreEmpresaOrigen = localMap.get("RZN_SOCIAL").toString() + ( (localMap.get("DIRECCION") != null)?(", "+localMap.get("DIRECCION").toString()):"");
                    currentBulk.setOrigenPolvorin(null);
                    currentBulk.setLugarUsoOrigen(null);
                    currentBulk.setLugarUsoUbigeoOrigen(null);
                    currentBulk.setOrigenFabrica(loc);
                    currentBulk.setOrigenAlquilerId(null);
                    if(localMap.get("TIPO").toString().equals("2")){
                        currentBulk.setOrigenAlquilerId(ejbEppContratoAlqPolvFacade.buscarContratoById(((BigDecimal) localMap.get("CONTRATO_ID")).longValue()));
                    }
                }
            } else if (tipoDestino != null && param.equals("2")) {
                if (tipoDestino.getCodProg().equals("TP_ODRGT_FAB")) {
                    fabrica = loc;
                    polvorin = null;
                    lugarUso = null;
                    //nombreEmpresaDestino = fabrica.getPersonaId().getRznSocial();
                    nombreEmpresaDestino = localMap.get("RZN_SOCIAL").toString() + ( (localMap.get("DIRECCION") != null)?(", "+localMap.get("DIRECCION").toString()):"");
                    currentBulk.setDestinoPolvorin(null);
                    currentBulk.setLugarUsoDestino(null);
                    currentBulk.setLugarUsoUbigeoDestino(null);
                    currentBulk.setDestinoFabrica(loc);
                    currentBulk.setDestinoAlquilerId(null);
                    if(localMap.get("TIPO").toString().equals("2")){
                        currentBulk.setDestinoAlquilerId(ejbEppContratoAlqPolvFacade.buscarContratoById(((BigDecimal) localMap.get("CONTRATO_ID")).longValue()));
                    }
                }
            }
            if (currentBulk.getId() == null){
                RequestContext.getCurrentInstance().update("crearForm:pnlgOrigenDestino");
            }else{
                RequestContext.getCurrentInstance().update("editarForm:pnlgOrigenDestino");
            }
        }
    }
     
     ///////////// GTE Tipo 3, 5 y 6 /////////////
    public void llenarTiposGTE(){
        Map nmap = new HashMap();
        tiposDeGuia = new ArrayList();
                
        nmap.put("id", "1");
        nmap.put("codProg", "TP_RGUIA_ADQUI1");
        nmap.put("color", "datatable-row-guia1");
        nmap.put("descripcion", "1. GT de Adquisición y Uso (de fábrica a polvorín)");
        tiposDeGuia.add(nmap);
        
        nmap = new HashMap(); 
        nmap.put("id", "2");
        nmap.put("codProg", "TP_RGUIA_ADQUI2");
        nmap.put("color", "datatable-row-guia2");
        nmap.put("descripcion", "2. GT de Adquisición y Uso (de polvorín a lugar de uso / de lugar de uso a polvorín)");
        tiposDeGuia.add(nmap);
        
        nmap = new HashMap();        
        nmap.put("id", "3");
        nmap.put("color", "datatable-row-guia3");
        nmap.put("codProg", "TP_RGUIA_INTER");
        nmap.put("descripcion", "3. GT de Traslado Interno (de polvorín a polvorín)");
        tiposDeGuia.add(nmap);
        
        nmap = new HashMap();        
        nmap.put("id", "5");        
        nmap.put("codProg", "TP_RGUIA_PLAPLA");
        nmap.put("color", "datatable-row-guia5");
        nmap.put("descripcion", "5. GT entre plantas de la misma fabricante");
        tiposDeGuia.add(nmap);
        
        nmap = new HashMap();        
        nmap.put("id", "6");
        nmap.put("codProg", "TP_RGUIA_COMFAB");
        nmap.put("color", "datatable-row-guia6");
        nmap.put("descripcion", "6. GT de comercialización entre fábricas");
        tiposDeGuia.add(nmap);
    }
     
    public Map buscarTipoGTEById(String tipo){
        for(Map tguia : tiposDeGuia){
            if(tguia.get("id").toString().equals(tipo)){
                return tguia;
            }
        }
        return null;
    }
    
    public Map buscarTipoGTEByCodProg(String tipo){
        for(Map tguia : tiposDeGuia){
            if(tguia.get("codProg").toString().equals(tipo)){
                return tguia;
            }
        }
        return null;
    }
      
    public boolean validarRGpermitidas(SbPersonaGt p) {
        boolean validacion = false;
        tiposGTEDisponibles = new ArrayList();
                
        if(!ejbRegistroFacade.buscarResolucionesGuiaExterna(null, p).isEmpty()){
            tiposGTEDisponibles.add(buscarTipoGTEById("1"));
            tiposGTEDisponibles.add(buscarTipoGTEById("2"));
            tiposGTEDisponibles.add(buscarTipoGTEById("3"));
            validacion = true;
        }
        
        lstExplosivosTemp = ejbExplosivoFacade.lstExplosivosActivosGtPlantaPlantaGTE(p);
        if(!lstExplosivosTemp.isEmpty()){
            tiposGTEDisponibles.add(buscarTipoGTEById("5"));
            validacion = true;
        }
        if(!ejbRegistroFacade.buscarAutorizacionesComercializacionGTE(p,null).isEmpty()){
            tiposGTEDisponibles.add(buscarTipoGTEById("6"));
            validacion = true;
        }
        return validacion;
    }
    
    public boolean validarRGpermitidasConTransfer(SbPersonaGt p) {
        boolean validacion = false;
        tiposGTEDisponibles = new ArrayList();
                
        if(!ejbRegistroFacade.buscarResolucionesGuiaExterna(null, p).isEmpty()){
            tiposGTEDisponibles.add(buscarTipoGTEById("1"));
            validacion = true;
        }
        if(!ejbRegistroFacade.buscarAutorizacionesComercializacionGTE(p,null).isEmpty()){
            tiposGTEDisponibles.add(buscarTipoGTEById("6"));
            validacion = true;
        }
        return validacion;
    }
    
    public boolean validarRGpermitidasSinTransfer(SbPersonaGt p) {
        boolean validacion = false;
        tiposGTEDisponibles = new ArrayList();
                
        if(!ejbRegistroFacade.buscarResolucionesGuiaExterna(null, p).isEmpty()){
            tiposGTEDisponibles.add(buscarTipoGTEById("2"));
            tiposGTEDisponibles.add(buscarTipoGTEById("3"));
            validacion = true;
        }
        lstExplosivosTemp = ejbExplosivoFacade.lstExplosivosActivosGtPlantaPlantaGTE(p);
        if(!lstExplosivosTemp.isEmpty()){
            tiposGTEDisponibles.add(buscarTipoGTEById("5"));
            validacion = true;
        }
        return validacion;
    }
    
    public void obtenerTipoGTECurrent(boolean cargarExplosivo){
        boolean tipoOrigen = false, tipoDestino = false;
        fotoByte = null;
        foto = null;
        archivo = current.getDocAdjunto();
        archivoTemporal = current.getDocAdjunto();
        
        //DATOS DE AUTORIZACION
        switch (current.getTipoTramite().getCodProg()) {
            case "TP_RGUIA_ADQUI1":
                setCriterioResGuia("1");
                setMostrarresglobprev(true);
                tipoDestino = true;
                resolucionGerencia = current.getResolucionId().getRegistroId();
                numeroResolucionGerencia = "" + current.getResolucionId().getNumero();
                fechaCaducidadResolucionGerencia = resolucionGerencia.getFechaFin();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
                lstExplosivosCombo = new ArrayList();
                if(cargarExplosivo){
                    cargarExplosivos(resolucionGerencia);
                }
                break;
            case "TP_RGUIA_ADQUI2":
                setCriterioResGuia("2");
                setMostrarresglobprev(true);
                resolucionGerencia = current.getResolucionId().getRegistroId();
                numeroResolucionGerencia = "" + current.getResolucionId().getNumero();
                fechaCaducidadResolucionGerencia = resolucionGerencia.getFechaFin();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
                lstExplosivosCombo = new ArrayList();
                if(cargarExplosivo){
                    cargarExplosivos(resolucionGerencia);
                }
                break;
            case "TP_RGUIA_INTER":
                //3. TRASLADO INTERNO
                setCriterioResGuia("3");
                setMostrarresglobprev(true);
                resolucionGerencia = current.getResolucionId().getRegistroId();
                numeroResolucionGerencia = "" + current.getResolucionId().getNumero();
                fechaCaducidadResolucionGerencia = resolucionGerencia.getFechaFin();
                tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                lstExplosivosCombo = new ArrayList();
                cargarExplosivos(resolucionGerencia);
                break;
            case "TP_RGUIA_DEVOL":
                //4. DEVOLUCION
                break;
            case "TP_RGUIA_PLAPLA":
                //5. TRASLADO PLANTA/PLANTA
                setCriterioResGuia("5");
                txtTipoTramite = "PLANTA A PLANTA";
                lstExplosivosCombo = ejbExplosivoFacade.lstExplosivosActivos();
                break;
            case "TP_RGUIA_COMFAB":
                //6. COMERCIALIZACION ENTRE FABRICAS
                setCriterioResGuia("6");
                setMostrarresglobprev(true);
                resolucionGerencia = current.getResolucionId().getRegistroId();
                numeroResolucionGerencia = "" + current.getResolucionId().getNumero();
                fechaCaducidadResolucionGerencia = resolucionGerencia.getFechaFin();
                tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                lstExplosivosCombo = new ArrayList();
                cargarExplosivos(resolucionGerencia);
                break;
            case "TP_RGUIA_INT":
                //7. COMERCIO I/S  
                break;
            case "TP_RGUIA_SAL":
                //8. COMERCIO I/S
                break;

            case "TP_RGUIA_TRA":
                //9. TRANSITO INTERNACIONAL
                break;
            case "TP_RGUIA_OTR":
                //10. ZPAE
                break;
            default:
                break;
        }
        
        //ORIGEN Y DESTINO        
        lstTipoGtOrigen = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia + (tipoOrigen?"O":""));
        lstTipoGtDestino = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia + (tipoDestino?"D":"") );
    }
    
    public void obtenerTipoGTECurrentBulk(boolean cargarExplosivo, EppGteRegistro registroBulk){
        boolean tipoOrigen = false, tipoDestino = false;
        //DATOS DE AUTORIZACION
        switch (registroBulk.getTipoTramite().getCodProg()) {
            case "TP_RGUIA_ADQUI1":
                setCriterioResGuia("1");
                setMostrarresglobprev(true);
                tipoDestino = true;
                resolucionGerencia = registroBulk.getResolucionId().getRegistroId();
                numeroResolucionGerencia = "" + registroBulk.getResolucionId().getNumero();
                fechaCaducidadResolucionGerencia = resolucionGerencia.getFechaFin();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
                lstExplosivosCombo = new ArrayList();
                setRenderSaldoActual(true);
                if(cargarExplosivo){
                    cargarExplosivos(resolucionGerencia);
                }
                break;
            case "TP_RGUIA_ADQUI2":
                setCriterioResGuia("2");
                setMostrarresglobprev(true);
                tipoDestino = true;
                resolucionGerencia = registroBulk.getResolucionId().getRegistroId();
                numeroResolucionGerencia = "" + registroBulk.getResolucionId().getNumero();
                fechaCaducidadResolucionGerencia = resolucionGerencia.getFechaFin();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
                lstExplosivosCombo = new ArrayList();
                setRenderSaldoActual(true);
                if(cargarExplosivo){
                    cargarExplosivos(resolucionGerencia);
                }
                break;
            case "TP_RGUIA_INTER":
                //3. TRASLADO INTERNO
                setCriterioResGuia("3");
                setMostrarresglobprev(true);
                resolucionGerencia = registroBulk.getResolucionId().getRegistroId();
                numeroResolucionGerencia = "" + registroBulk.getResolucionId().getNumero();
                fechaCaducidadResolucionGerencia = resolucionGerencia.getFechaFin();
                tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                lstExplosivosCombo = new ArrayList();
                cargarExplosivos(resolucionGerencia);
                setRenderSaldoActual(true);
                break;
            case "TP_RGUIA_DEVOL":
                //4. DEVOLUCION
                break;
            case "TP_RGUIA_PLAPLA":
                //5. TRASLADO PLANTA/PLANTA
                setCriterioResGuia("5");
                resolucionGerencia = null;
                txtTipoTramite = "PLANTA A PLANTA";
                lstExplosivosCombo = ejbExplosivoFacade.lstExplosivosActivosGtPlantaPlantaGTE(registroBulk.getEmpresaId());
                setRenderSaldoActual(false);
                break;
            case "TP_RGUIA_COMFAB":
                //6. COMERCIALIZACION ENTRE FABRICAS
                setCriterioResGuia("6");
                setMostrarresglobprev(true);
                resolucionGerencia = registroBulk.getResolucionId().getRegistroId();
                numeroResolucionGerencia = "" + registroBulk.getResolucionId().getNumero();
                fechaCaducidadResolucionGerencia = resolucionGerencia.getFechaFin();
                tipoResolucionGerencia = resolucionGerencia.getTipoProId().getAbreviatura();
                strViewVencimiento = "<b>" + mostrarFechaDdMmYyyy(fechaCaducidadResolucionGerencia) + "</b>";
                lstExplosivosCombo = new ArrayList();
                cargarExplosivos(resolucionGerencia);
                setRenderSaldoActual(false);
                break;
            case "TP_RGUIA_INT":
                //7. COMERCIO I/S  
                break;
            case "TP_RGUIA_SAL":
                //8. COMERCIO I/S
                break;

            case "TP_RGUIA_TRA":
                //9. TRANSITO INTERNACIONAL
                break;
            case "TP_RGUIA_OTR":
                //10. ZPAE
                break;
            default:
                break;
        }
        
        //ORIGEN Y DESTINO        
        lstTipoGtOrigen = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia + (tipoOrigen?"O":""));
        lstTipoGtDestino = ejbTipoExplosivoFacade.lstTipoODGuiaTransito(criterioResGuia + (tipoDestino?"D":"") );
    }
        
    public double calcularSaldoActualEmitir(EppGteRegistro reg, EppExplosivo exp) {
        double saldo = 0;
        boolean flagCalculaSaldo = true;
        String polvorinesAnidados = "", resolucionesAnidadas = "";
        EppPolvorin polvTemp = null;
        EppRegistro regTemp = resolucionGerencia;
        saldoActualExplSolic = 0.0;        
        
        if (reg.getResolucionId() != null
                && !criterioResGuia.equals("2")
                && !criterioResGuia.equals("3")
                && !criterioResGuia.equals("4")) {

            switch (reg.getResolucionId().getRegistroId().getTipoProId().getCodProg()) {
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                case "TP_PRTUP_AAPOR":
                    saldo = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoRgGee(reg.getResolucionId(), exp);
                    break;
                case "TP_PRTUP_AINT":
                case "TP_PRTUP_ASAL":
                case "TP_PRTUP_INT":
                case "TP_PRTUP_SAL":
                    saldo = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoRgIs(reg.getResolucionId(), exp);
                    break;
                case "TP_PRTUP_PLA":
                case "TP_PRTUP_AFAB":
                    saldo = 0;
                    break;
                default:
                    saldo = 0;
                    break;
            }
        } else {
            switch (criterioResGuia) {
                case "2":
                    if (reg.getOrigenPolvorin() != null && reg.getLugarUsoDestino() != null && reg.getLugarUsoUbigeoDestino() != null) {
                        polvTemp = reg.getOrigenPolvorin();
                        polvorinesAnidados = obtenerPolvorinesAnidados(polvTemp);
                        resolucionesAnidadas = obtenerResolucionesAnidadas(regTemp);
                        
                        saldoActualExplSolic = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoSumatoriaPolvLuuAnidados(
                                                                resolucionesAnidadas,
                                                                exp,
                                                                polvorinesAnidados );
                        saldo = saldoActualExplSolic;
                        
                    } else if (reg.getLugarUsoOrigen() != null && reg.getLugarUsoUbigeoOrigen() != null && reg.getDestinoPolvorin() != null) {
                        while(flagCalculaSaldo){
                            saldo = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoSumatoriaLuuPolv(
                                                                                regTemp.getEppResolucion(),
                                                                                exp,
                                                                                reg.getLugarUsoOrigen(),
                                                                                reg.getLugarUsoUbigeoOrigen());
                            if(regTemp.getRegistroId() != null){
                                regTemp = regTemp.getRegistroId();
                                if(regTemp.getEppResolucion() == null){
                                    flagCalculaSaldo = false;
                                }
                            }else{
                                flagCalculaSaldo = false;
                            }
                            saldoActualExplSolic += saldo;
                        }
                        saldo = saldoActualExplSolic;  
                        if(saldo < 0){
                            saldo = 0;
                        }
                    } else {
                        exp = null;
                        JsfUtil.mensajeAdvertencia("Debe completar los datos de traslado");
                    }
                    break;
                case "3":
                    if (reg.getOrigenPolvorin() != null && reg.getDestinoPolvorin() != null) {
                        polvTemp = reg.getOrigenPolvorin();
                        polvorinesAnidados = obtenerPolvorinesAnidados(polvTemp);
                        resolucionesAnidadas = obtenerResolucionesAnidadas(regTemp);
                        
                        saldoActualExplSolic = eppRegistroGuiaTransitoFacade.calculaSaldoExplosivoSumatoriaPolvLuuAnidados(
                                                                                resolucionesAnidadas,
                                                                                exp,
                                                                                polvorinesAnidados );
                        saldo = saldoActualExplSolic;
                        if(saldo < 0){
                            saldo = 0;
                        }
                    } else {
                        JsfUtil.mensajeAdvertencia("El explosivo "+exp.getNombre() + " no tiene polvorín de origen o destino");
                    }
                    break;
                default:
                    saldo = 0.0;                    
                    break;
            }
        }
        return saldo;
    }
    
    public String obtenerDescripcionTipoGuia(){
        String nombre = "";
        if(criterioResGuia != null && !criterioResGuia.equals("0")){
            Map nmap = buscarTipoGTEById(criterioResGuia);
            nombre = ""+nmap.get("descripcion");
        }
        return nombre;
    }
    
    public String obtenerDatoGTEListadoUsuario(String tipoGuia, String dato){
        String nombre = "";
        Map nmap = buscarTipoGTEByCodProg(tipoGuia);
        if(nmap != null){
            nombre = ""+nmap.get(dato);
        }
        return nombre;
    }

    public String direccionarGTESinTransfer() {
        empresa = ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
        if(empresa != null){
            llenarTiposGTE();
            if (permitirSinTransfer(empresa)) {
                if(!cargarDatos()){
                    JsfUtil.mensajeError("No se encontró parametrización de los recibos");
                    return null;
                }
                DatosUsuario d = JsfUtil.getLoggedUser();
                //lstRecibosBn = new ListDataModel(sbRecibosFacade.lstRecibos(d.getNumDoc(), nroVoucher));
                resultados = null;
                resultadosGteReg = null;
                resultadosGuia = null;
                resultadosGuiaGte = null;
                registrosSeleccionados = null;
                registrosGteRegSeleccionados = null;
                resultadosGuiaSeleccionados = null;
                resultadosGuiaGteSeleccionados = null;
                lstOpcionesBandeja = new ArrayList();
                filtro = null;
                tipoEstadoBandeja = null;
                tipoBusqueda = null;
                strConductor = null;
                strVehiculo = null;
                setEsGtSinTransf(true);
                setEsSinGTRef(false);
                RequestContext.getCurrentInstance().update("listForm");
                return "/aplicacion/eppRegistroGuiaTransito/BandejaSinTransfer";
            } else {
                JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN PARA LA FABRICACIÓN DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
            }
        }else{
            JsfUtil.mensajeAdvertencia("Usted no cuenta con AUTORIZACIÓN PARA LA FABRICACIÓN DE EXPLOSIVOS O MATERIALES RELACIONADOS vigente. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar.");
        } 
        return null;
    }    
    
    public String obtenerComprobanteGTEBulk(EppGteRegistro gte, int tipo){
        String nombre = "";
        if(gte.getSbRecibosList() != null){
            for(SbRecibos rec: gte.getSbRecibosList()){
                if(rec.getActivo() == 1){
                    if(tipo == 1 ){
                        // Nro. de recibo
                        nombre = ""+rec.getNroSecuencia();
                    }else{
                        // Fecha de recibo
                        nombre = JsfUtil.formatoFechaDdMmYyyy(rec.getFechaMovimiento());
                    }
                }
            }
        }
        if(gte.getEppGteDepositoList() != null){
            for(EppGteDeposito depo: gte.getEppGteDepositoList()){
                if(depo.getActivo() == 0){
                    continue;
                }
                if(tipo == 1 ){
                    // Nro. de recibo
                    nombre = ""+depo.getNroDeposito();
                }else{
                    // Fecha de recibo
                    nombre = JsfUtil.formatoFechaDdMmYyyy(depo.getFechaDeposito());
                }
                break;
            }            
        }
        return nombre;
    }
    
    public String obtenerComprobanteGTE(EppRegistroGuiaTransito gte, int tipo){
        String nombre = "";
        if(gte.getRegistroId().getEppGteRegistroList() != null && !gte.getRegistroId().getEppGteRegistroList().isEmpty()){
            for(EppGteRegistro gteRegistro: gte.getRegistroId().getEppGteRegistroList()){
                if(gteRegistro.getActivo() == 0){
                    continue;
                }
                if(gteRegistro.getSbRecibosList() != null && !gteRegistro.getSbRecibosList().isEmpty()){
                    for(SbRecibos rec: gteRegistro.getSbRecibosList() ){
                        if(rec.getActivo() == 0){
                            continue;
                        }
                        if(tipo == 1 ){
                            // Nro. de recibo
                            nombre = ""+rec.getNroSecuencia();                        
                        }else{
                            // Fecha de recibo
                            nombre = JsfUtil.formatoFechaDdMmYyyy(rec.getFechaMovimiento());
                        }
                        return nombre;
                    }
                }
                if(gteRegistro.getEppGteDepositoList() != null && !gteRegistro.getEppGteDepositoList().isEmpty()){
                    for(EppGteDeposito rec: gteRegistro.getEppGteDepositoList() ){
                        if(rec.getActivo() == 0){
                            continue;
                        }
                        if(tipo == 1 ){
                            // Nro. de recibo
                            nombre = ""+rec.getNroDeposito();
                        }else{
                            // Fecha de recibo
                            nombre = JsfUtil.formatoFechaDdMmYyyy(rec.getFechaDeposito());
                        }
                        return nombre;
                    }
                }   
            }                
        }
        return nombre;
    }
    
    public List<TipoExplosivoGt> getLstTipoEstadoGuiaSinTransfer() {
        return ejbTipoExplosivoFacade.lstTipoEstadoGuiaExternaSinTransfer();
    }
    
    public boolean mostrarEmitirGtBulkMapSinTransf(String estado){
        return estado.equals("TP_REGEV_CRE");
    }
    
    public String emitirGtMapSinTransf(EppGteRegistro gte) {
        reiniciarValoresVerPreview();
        currentBulk = new EppGteRegistro();
        currentBulk = gte;
        
        ////// Validación de RG referenciada MEMO 1160-2017 //////        
        if(currentBulk.getResolucionId() != null){
            if(currentBulk.getResolucionId().getActivo() == 0 || currentBulk.getResolucionId().getRegistroId().getActivo() == 0){
                JsfUtil.mensajeAdvertencia("No se puede emitir esta GTE porque la RG referenciada se encuentra inactiva");
                return null;
            }
        }
        ///////////
        
        tipoTransporte = currentBulk.getTipoTransporte();
        empresaTransporte = currentBulk.getEmpresaTranspId();
        esGtSinTransf = true;
        
        obtenerTipoGTECurrentBulk(false, currentBulk);
        if(resolucionGerencia != null){
            fechaCaducidadResolucionGerencia = resolucionGerencia.getFecha();
            strViewRefRg = "REF. R.G.:<b>" + currentBulk.getResolucionId().getNumero() + "</b>";
        }else{
            setEsSinGTRef(true);
        }
            
        cargarRazonSocOrigenDestinoBulk(currentBulk);
        strViewEmpresa = mostrarClienteString(currentBulk.getEmpresaId());
        strViewOrigen = mostrarDatosOrigenBulk(currentBulk, false);
        strViewDestino = mostrarDatosDestinoBulk(currentBulk, false);
        strViewEmpTransp = mostrarClienteString(currentBulk.getEmpresaTranspId()) == null ? "" : ("Los explosivos serán trasladados por:<br/>"
                + "EMPRESA DE TRANSPORTES: " + mostrarClienteString(currentBulk.getEmpresaTranspId()));
        return "/aplicacion/eppRegistroGuiaTransito/ViewEmitirGt";
    }
    
    public String direccionarGT(){
        if(esGtSinTransf){
            return direccionarGTESinTransfer();
        }else{
            return direccionarGTFabri();
        }
    }
    
    public String direccionarDatosAdiGTE(){
        if(esGtSinTransf){
            return direccionarGTESinTransfer();
        }else{
            return direccionarListadoGTUsua();
        }
    }
    
    public String obtenerNombreBtnGTE(){
        if(esGtSinTransf){
            return "Ir a Bandeja GTE";
        }else{
            return "Ir a Listado Traslado";
        }
    }
    
    public boolean mostrarBtnReporteSinTransf(String estado) {
        return (estado.equals("TP_REGEV_TRAN")
                || estado.equals("TP_REGEV_OBS")
                || estado.equals("TP_REGEV_VIS")
                || estado.equals("TP_REGEV_FIR")
                || estado.equals("TP_REGEV_FIN")
                );
    }
    
    public String getHeaderPreviewImgGTE(){
        String img = "/resources/imagenes/header-gt-preview.png";
        switch(currentBulk.getTipoTramite().getCodProg()){
            case "TP_RGUIA_ADQUI2":
                img = "/resources/imagenes/header-gt-preview-gt2.png";
                break;
            case "TP_RGUIA_INTER":
                img = "/resources/imagenes/header-gt-preview-gt3.png";
                break;
            case "TP_RGUIA_PLAPLA":
                img = "/resources/imagenes/header-gt-preview-gt5.png";
                break;
            case "TP_RGUIA_COMFAB":
                img = "/resources/imagenes/header-gt-preview-gt6.png";
                break;
        }
        return img;
    }
    
    public boolean validarSaldoExplosivoPreview(EppGteRegistro reg) {
        try {

            if (reg.getResolucionId() == null) {
                return false;
            }
            if (reg.getResolucionId().getRegistroId().getTipoProId().getCodProg().equals("TP_PRTUP_PLA")
                    || reg.getResolucionId().getRegistroId().getTipoProId().getCodProg().equals("TP_PRTUP_AFAB")
                    || reg.getResolucionId().getRegistroId().getTipoProId().getCodProg().equals("TP_PRTUP_COM")
                    || reg.getResolucionId().getRegistroId().getTipoProId().getCodProg().equals("TP_PRTUP_ACOM")
                    || reg.getResolucionId().getRegistroId().getTipoProId().getCodProg().equals("TP_PRTUP_GUI")
                    //|| reg.getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI2")
                    || reg.getTipoTramite().getCodProg().equals("TP_RGUIA_OTR")
                    ) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            JsfUtil.mensajeError("Error: " + e.getMessage());
            return true;
        }
    }
    
    public List<TipoExplosivoGt> getLstTipoDocGtExt() {
        if(criterioResGuia != null && (criterioResGuia.equals("5") || criterioResGuia.equals("6"))){
            return ejbTipoExplosivoFacade.lstTipoDocGtExtTipo6();
        }
        return ejbTipoExplosivoFacade.lstTipoDocGtExt();
    }

    ///// LICENCIA MANIPULADOR DE EXPLOSIVOS /////
    
    public String prepareLME(){        
        reiniciarCamposBusqueda();
        resultadosLME = null;
        return "/aplicacion/eppRegistroGuiaTransito/ListLicenciaME";
    }
    
    public void cambiaTipoBusquedaLME(){
        if(tipoBusqueda != null){
            maxLength = 0;
            if(tipoBusqueda.equals("1")){
                maxLength = 99999;
            }else{
                maxLength = 8;
            }
        }
    }
    
    public void buscarBandejaLME(){
        if (tipoBusqueda != null && (filtro != null && !filtro.isEmpty()) ) {
            if(filtro != null)
                filtro = ((filtro.length()==0)?null:filtro.trim());
            
            List<EppRegistro> listado;
            HashMap mMap = new HashMap();
            mMap.put("tipoBusqueda", tipoBusqueda);
            mMap.put("filtro", (filtro != null)?filtro.trim().toUpperCase():filtro );            

            listado = ejbRegistroFacade.buscarLMEBandeja(mMap);
            if(listado != null){
                resultadosLME = new ListDataModel(listado);
            }
            reiniciarCamposBusqueda();

        }else{
            JsfUtil.mensajeAdvertencia("Por favor escriba el criterio de búsqueda");
        }
    }
    
    public void reiniciarCamposBusqueda(){
        filtro = null;
        tipoBusqueda = null;
    }
    
    public String obtenerDescVigencia(EppRegistro reg){
        String nombre = "";
        Calendar c_hoy = Calendar.getInstance();
        for(EppLicencia lic : reg.getEppLicenciaList1()){
            if(JsfUtil.getFechaSinHora(lic.getFecVenc()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) >= 0){
                nombre = "VIGENTE";
            }else{
                nombre = "VENCIDA";
            }
            break;
        }
        
        return nombre;
    }
    
    public String obtenerNroLicencia(EppRegistro regLicencia){
        String cadena = "";
        
        EppRegistro regTemp;
        if(regLicencia.getTipoOpeId().getCodProg().equals("TP_OPE_CAN")){
            regTemp = regLicencia.getRegistroId();
        }else{
            regTemp = regLicencia;
        }
        
        for(EppLicencia lic : regTemp.getEppLicenciaList1()){
            if(lic.getNroLicencia() == null){
                cadena = "-";
            }else{
                cadena = "" + lic.getNroLicencia();   
            }            
            break;
        }
        return cadena;
    }
    
    public String obtenerDatosLicencia(EppRegistro regLicencia){
        String cadena = "";
        
        EppRegistro regTemp;
        if(regLicencia.getTipoOpeId().getCodProg().equals("TP_OPE_CAN")){
            regTemp = regLicencia.getRegistroId();
        }else{
            regTemp = regLicencia;
        }
        
        for(EppLicencia lic : regTemp.getEppLicenciaList1()){
            cadena = lic.getPersonaId().getApePat() + " " + lic.getPersonaId().getApeMat() + " " + lic.getPersonaId().getNombres();
            break;
        }
        return cadena;
    }
    
    public Date obtenerFechaLicencia(EppRegistro regLicencia, int tipo){
       
        for(EppLicencia lic : regLicencia.getEppLicenciaList1()){
            if(tipo == 1){
                return lic.getFecEmi();
            }else{
                return lic.getFecVenc();
            }
        }
        return null;
    }
    
    public String obtenerDescEmpresa(SbPersonaGt empr){
        String desc = "";
        
        if(empr != null){
            desc = ((empr.getRuc() != null)?empr.getRuc():"") +  ", " + ((empr.getRznSocial()!= null)?empr.getRznSocial():empr.getNombresYApellidos());
        }        
        return desc;
    }
    
    public String obtenerMsjeInvalidSize(){
        return JsfUtil.bundleBDIntegrado("eppRegistroGuiaTransito_fileUpload_InvalidSizeMessage") + " " + 
               (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("eppRegistroGuiaTransito_sizeLimite_archivo").getValor())/1024) + " Kb";
    }
 
    public void handleFileUploadAdjuntoPDF(FileUploadEvent event){
        try {
            if (event != null) {
                file = event.getFile();
                if ( JsfUtil.verificarPDF(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    foto = new DefaultStreamedContent(file.getInputstream(), "application/pdf");
                    archivo = file.getFileName();
                    
                    if(current.getDocAdjunto() != null){
                        JsfUtil.mensajeAdvertencia("El archivo cargado reemplazará al archivo actual");
                    }
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de cargar no tiene formato PDF");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public StreamedContent cargarDocumento(){
        try {
            if(file != null){
                return new DefaultStreamedContent(file.getInputstream(), "application/pdf", file.getFileName() );
            }else{
                String nombreArchivo = current.getDocAdjunto();
                if(nombreArchivo != null && !nombreArchivo.isEmpty()){
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_guiasExp").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    
    /////////// CARGA DE DEPOSITO TEMPORAL GT ///////////
    public void handleFileUploadDeposito(FileUploadEvent event) {
        try {
            if (event != null) {
                fileDeposito = event.getFile();
                if (JsfUtil.verificarJPG(fileDeposito)) {
                    depositoByte = IOUtils.toByteArray(fileDeposito.getInputstream());
                    depositoStreamed = new DefaultStreamedContent(fileDeposito.getInputstream(), "image/jpeg");
                    depositoAdjunto = fileDeposito.getFileName();
                } else {
                    fileDeposito = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String obtenerMsjeInvalidSizeAdjunto(){
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " " +
               (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("eppRegistroGuiaTransito_sizeImageDep").getValor())/1024) + " Kb. ";
    }
    
    public StreamedContent cargarDeposito(){
        String nombre = depositoAdjunto.substring(0, depositoAdjunto.lastIndexOf(".") );
        String extension = depositoAdjunto.substring(depositoAdjunto.lastIndexOf("."), depositoAdjunto.length());
        
        try {            
            String nombreArchivo = nombre + extension.toLowerCase();
            if(fileDeposito != null){
                return new DefaultStreamedContent(fileDeposito.getInputstream(), "image/jpeg", fileDeposito.getFileName() );
            }else{
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_depositoGte").getValor(), nombreArchivo, nombreArchivo, "image/jpeg");    
            }
        } catch (Exception e) {
             e.printStackTrace();
         }     
        return null;
     }
    
    public void openDlgAyuda(int tipo){
        switch(tipo){
            case 1:
                imagenAyuda = "ayuda_deposito_bn.png";
                break;
        }        
        RequestContext.getCurrentInstance().execute("PF('dlgAyuda').show()");
        RequestContext.getCurrentInstance().update("frmAyuda");
    }
    
    public Date getMaxDate(){
        return new Date();
    }
    
    
}

