package pe.gob.sucamec.bdintegrado.jsf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import pe.gob.sucamec.bdintegrado.data.SspCarne;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.PieChartModel;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbProcesoTupa;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.SspArchivo;
import pe.gob.sucamec.bdintegrado.data.SspPersonaFoto;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SspRegistroEvento;
import pe.gob.sucamec.bdintegrado.data.SspRequisito;
import pe.gob.sucamec.bdintegrado.data.SspAlumnoCurso;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCarneGssp;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtil;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.notificacion.beans.NeDocumentoFacade;
import pe.gob.sucamec.notificacion.data.NeDocumento;
import pe.gob.sucamec.notificacion.data.NeEvento;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import wspide.ResPideMigra;
import wsreniecmq.ws.RespuestaMq;
import pe.gob.sucamec.bdintegrado.jsf.util.UploadFilesController;
import pe.gob.sucamec.sel.citas.data.CitaDetalleArma;

// Nombre de la instancia en la aplicacion //
@Named("sspCarneController")
@SessionScoped

/**
 * Clase con SspCarneController instanciada como sspCarneController. Contiene
 * funciones utiles para la entidad SspCarne. Esta vinculada a las páginas
 * SspCarne/create.xhtml, SspCarne/update.xhtml, SspCarne/list.xhtml,
 * SspCarne/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class SspCarneController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;
    String filtroNroCarne;
    Date filtroFecNac;

    /**
     * Registro actual para editar o ver.
     */
    SspRegistro registro;

    /////////////////// BANDEJA ///////////////////
    private String buscarPor;
    private Date fechaInicio = null;
    private Date fechaFinal = null;
    private String procesoCodProg = "";
    private TipoSeguridad estadoSelected;
    private TipoBaseGt tipoRegSelected;
    private TipoBaseGt tipoOpeSelected;
    private List<Map> resultados;
    private List<Map> resultadosSeleccionados;
    private boolean disabledBtnTransmitir;
    private TipoSeguridad modalidadSelected;
    private TipoSeguridad tipoServicio;
    private TipoBaseGt sedeSelected;
    private List<TipoSeguridad> lstModalidadesActivas;
    private boolean renderDenunciaPolicial;
    private boolean esDevuelto;
    private boolean esEstandar;
    private String usuarioArchivar;
    private String observacion;
    private Integer totalCarnesImpresion;
    private Integer totalCarnesIniciales;
    private Integer totalCarnesDuplicados;
    private Integer totalCarnesRenovados;
    private Integer totalCarnesCambioEmpresa;
    private PieChartModel pieEstadistica;
    private Date fechaLimiteDuplicado;
    private Map itemSelected = null;
    private boolean accionEjecutada = false;
    private String pathCarneDigital = "";
    private String pathCacheCarneDigital = "";
    private String pathMagic = "";
    private boolean blnProcesoTransmision = false;
    private Integer totalProcesados = 0;
    private Integer actualProcesado = 0;
    private Integer progress = 0;
    private String labelCursoVig = "";
    private String labelMsjCurso = "";
    ///////////////////////////////////////////////

    //////////////////// CREAR ////////////////////
    private TipoBaseGt areaSelected;
    private TipoBaseGt tipoDocSelected;
    private String numDoc;
    private String numDocView;
    private String nombres;
    private String apePat;
    private String apeMat;
    private Date fechaNac;
    private String tipoDoc;
    private UploadedFile file;
    private StreamedContent foto;
    private byte[] fotoByte;
    private Integer maxLength;
    private boolean esNuevo;
    private boolean disabledNombres;
    private String nombreFoto;
    private String nombreFotoTemporal;
    private UploadedFile fileDJ;
    private StreamedContent archivoDJ;
    private byte[] djByte;
    private String archivo;
    private String archivoTemporal;
    private UploadedFile fileDP;
    private StreamedContent archivoDP;
    private byte[] cmByte;
    private String certificado;
    private String certificadoTemporal;
    private UploadedFile fileCM;
    private StreamedContent archivoCM;
    private byte[] dpByte;
    private String archivoDenuncia;
    private String archivoTemporalDenuncia;
    private List<SspRegistroEvento> lstObservaciones;
    private String detalleDuplicado;
    private SbRecibos reciboNuevo;
    private boolean disabledDatosAdministrado;
    private boolean disabledDatosAdministradoModalidad;
    private boolean disabledDatosVigilante, disabledFotoVigilante;
    private boolean disabledDatosRequisitos;
    private double importeDuplicado;
    private double importeEmision;
    private Long codAtributo;
    private boolean tieneObservacion;
    private boolean renderDatosDuplicado;
    private int DIAS_RENOVACION;
    private List<TipoBaseGt> lstAreas;
    private Long nroSecuencia;
    private Long nroSecuenciaVerDuplicado;
    private String archivoVerDuplicado;
    private boolean esDuplicado;
    private boolean solicitarRecibo;
    private String tipoCarne;
    private Integer tipoImagenAyuda;
    private TipoBaseGt sedeRecojoDuplicado;
    ///////////////////////////////////////////////

    //////////////////// CESE ////////////////////
    private String paso;
    private String tipoBusqueda;
    private boolean muestraBusquedafecha;
    private Date fechaBusqueda;
    private List<SbRecibos> lstRecibos;
    private List<SbRecibos> lstRecibosBusqueda;
    private int limiteReg;
    private boolean disabledSiguiente;
    private boolean renderDatosVigilante;
    private List<Map> lstVigilanteBusqueda;
    private List<Map> lstVigilante;
    private SspCarne carneSelected;
    private String estadoCese;
    private List<TipoSeguridad> lstModalidadesVig;
    private String msjeCese;
    private List<Map> lstInscritos;
    ///////////////////////////////////////////////

    //////////////////// CARNE ELECTRONICO ////////////////////
    private String pathFondo;
    private String pathFirma;
    private String linkCarne;
    private String carneDigital;
    
    private String rutaArchivoCargaCese;
    private String nombreArchivoCargaCese;

    private List<Map> lstObservacionCargaMasiva;
    private List<Map> lstVigilanteSelect;

    ///////////////////////////////////////////////

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCarneFacade ejbSspCarneFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroFacade ejbSspRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspPersonaFotoFacade ejbSspPersonaFotoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroCursoFacade ejbSspRegistroCursoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade ejbTipoSeguridadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade ejbSbDerivacionCydocFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDepartamentoFacadeGt ejbSbDepartamentoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.CarteraClienteFacade ejbCarteraClienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TrazaFacade ejbTrazaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbFeriadoFacade ejbSbFeriadoFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbTipoFacade ejbSbTipoFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbPersonaFacade ejbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbProcesoTupaFacade ejbSbProcesoTupaFacade;
    @EJB
    private NeDocumentoFacade ejbNeDocumentoFacade;

    @Inject
    WsTramDoc wsTramDocController;
    @Inject
    WsPide wsPideController;
    @Inject
    private UploadFilesController upFilesCont;
    
    // Si la tabla no tiene el campo activo borrar DESDE ACA //
    /**
     * Devuelve si el registro actual se encuentra activo, se usa para borrado
     * lógico.
     *
     * @return true si se encuenta activo, false si no.
     */
    public boolean isActivo() {
        return (registro.getActivo() == 1);
    }

    /**
     * Cambia el estado del registro a activo o inactivo, se usa para borrado
     * lógico.
     *
     * @param activo true o false para cambiar el estado.
     */
    public void setActivo(boolean activo) {
        if (activo) {
            registro.setActivo((short) 1);
        } else {
            registro.setActivo((short) 0);
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public SspRegistro getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SspRegistro registro) {
        this.registro = registro;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @return el filtro de busquedas
     */
    public String getFiltro() {
        return filtro;
    }

    public String getFiltroNroCarne() {
        return filtroNroCarne;
    }

    public void setFiltroNroCarne(String filtroNroCarne) {
        this.filtroNroCarne = filtroNroCarne;
    }

    public Date getFiltroFecNac() {
        return filtroFecNac;
    }

    public void setFiltroFecNac(Date filtroFecNac) {
        this.filtroFecNac = filtroFecNac;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @param filtro el nuevo filtro de busquedas
     */
    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    /**
     * Propiedad para el estado del CRUD.
     *
     * @return el estado actual del CRUD.
     */
    public EstadoCrud getEstado() {
        return estado;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        return "List";
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<SspCarne> getSelectItems() {
        return ejbSspCarneFacade.findAll();
    }

    public String getBuscarPor() {
        return buscarPor;
    }

    public void setBuscarPor(String buscarPor) {
        this.buscarPor = buscarPor;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public TipoSeguridad getEstadoSelected() {
        return estadoSelected;
    }

    public void setEstadoSelected(TipoSeguridad estadoSelected) {
        this.estadoSelected = estadoSelected;
    }

    public TipoBaseGt getTipoRegSelected() {
        return tipoRegSelected;
    }

    public void setTipoRegSelected(TipoBaseGt tipoRegSelected) {
        this.tipoRegSelected = tipoRegSelected;
    }

    public TipoBaseGt getTipoOpeSelected() {
        return tipoOpeSelected;
    }

    public void setTipoOpeSelected(TipoBaseGt tipoOpeSelected) {
        this.tipoOpeSelected = tipoOpeSelected;
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

    public List<TipoSeguridad> getListadoEstados() {
        return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgsOrderById("'TP_ECC_CRE','TP_ECC_OBS','TP_ECC_APR','TP_ECC_NPR','TP_ECC_TRA','TP_ECC_FDP','TP_ECC_DES'");
    }

    public List<TipoBaseGt> getListadoTipoRegistro() {
        return ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_REGIST_NOR','TP_REGIST_EMP'");
    }

    public List<TipoBaseGt> getListadoTipoOperacion() {
        return ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_OPE_INI','TP_OPE_REN','TP_OPE_COP'");
    }

    public List<TipoSeguridad> getListadoModalidades() {
        return ejbTipoSeguridadFacade.lstTipoSeguridad("TP_MCO");
    }

    public List<TipoSeguridad> getListadoModalidadesBandeja() {
        switch (tipoCarne) {
            case "TODOS":
                return ejbTipoSeguridadFacade.lstTipoSeguridad("TP_MCO");
            case "ESTANDAR":
                return ejbTipoSeguridadFacade.lstModalidadesSinEventos();
            default:
                return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_MCO_EVT'");
        }
    }

    public List<TipoBaseGt> getListadoTipoDoc() {
        return ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");
    }

    public List<TipoSeguridad> getListadoMotivosCarnes() {
        return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_ECRN_ROB','TP_ECRN_DTD','TP_ECRN_PER','TP_ECRN_DTR'");
    }

    public List<TipoBaseGt> getListadoAreas() {
        return ejbTipoBaseFacade.listarAreasLimaOD();
    }

    public boolean isDisabledBtnTransmitir() {
        return disabledBtnTransmitir;
    }

    public void setDisabledBtnTransmitir(boolean disabledBtnTransmitir) {
        this.disabledBtnTransmitir = disabledBtnTransmitir;
    }

    public TipoSeguridad getModalidadSelected() {
        return modalidadSelected;
    }

    public void setModalidadSelected(TipoSeguridad modalidadSelected) {
        this.modalidadSelected = modalidadSelected;
    }

    public TipoSeguridad getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoSeguridad tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public List<TipoSeguridad> getLstModalidadesActivas() {
        return lstModalidadesActivas;
    }

    public void setLstModalidadesActivas(List<TipoSeguridad> lstModalidadesActivas) {
        this.lstModalidadesActivas = lstModalidadesActivas;
    }

    public TipoBaseGt getAreaSelected() {
        return areaSelected;
    }

    public void setAreaSelected(TipoBaseGt areaSelected) {
        this.areaSelected = areaSelected;
    }

    public TipoBaseGt getTipoDocSelected() {
        return tipoDocSelected;
    }

    public void setTipoDocSelected(TipoBaseGt tipoDocSelected) {
        this.tipoDocSelected = tipoDocSelected;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getLabelCursoVig() {
        return labelCursoVig;
    }

    public void setLabelCursoVig(String labelCursoVig) {
        this.labelCursoVig = labelCursoVig;
    }

    public String getLabelMsjCurso() {
        return labelMsjCurso;
    }

    public void setLabelMsjCurso(String labelMsjCurso) {
        this.labelMsjCurso = labelMsjCurso;
    }

    public StreamedContent getFoto() {
        try {
            if (fotoByte != null) {
                InputStream is = new ByteArrayInputStream(fotoByte);
                foto = new DefaultStreamedContent(is, "image/jpeg", "foto");
            } else if (file != null) {
                fotoByte = IOUtils.toByteArray(file.getInputstream());
                foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
            } else {
                return new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg"), "image/jpeg");
            }
        } catch (Exception ex) {
            fotoByte = null;
            foto = null;
        }

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

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(boolean esNuevo) {
        this.esNuevo = esNuevo;
    }

    public boolean isDisabledNombres() {
        return disabledNombres;
    }

    public void setDisabledNombres(boolean disabledNombres) {
        this.disabledNombres = disabledNombres;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public String getNombreFotoTemporal() {
        return nombreFotoTemporal;
    }

    public void setNombreFotoTemporal(String nombreFotoTemporal) {
        this.nombreFotoTemporal = nombreFotoTemporal;
    }

    public UploadedFile getFileDJ() {
        return fileDJ;
    }

    public void setFileDJ(UploadedFile fileDJ) {
        this.fileDJ = fileDJ;
    }

    public StreamedContent getArchivoDJ() {
        return archivoDJ;
    }

    public void setArchivoDJ(StreamedContent archivoDJ) {
        this.archivoDJ = archivoDJ;
    }

    public byte[] getDjByte() {
        return djByte;
    }

    public void setDjByte(byte[] djByte) {
        this.djByte = djByte;
    }

    public byte[] getCmByte() {
        return cmByte;
    }

    public void setCmByte(byte[] cmByte) {
        this.cmByte = cmByte;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public String getCertificadoTemporal() {
        return certificadoTemporal;
    }

    public void setCertificadoTemporal(String certificadoTemporal) {
        this.certificadoTemporal = certificadoTemporal;
    }

    public UploadedFile getFileCM() {
        return fileCM;
    }

    public void setFileCM(UploadedFile fileCM) {
        this.fileCM = fileCM;
    }

    public StreamedContent getArchivoCM() {
        return archivoCM;
    }

    public void setArchivoCM(StreamedContent archivoCM) {
        this.archivoCM = archivoCM;
    }

    public String getArchivoTemporal() {
        return archivoTemporal;
    }

    public void setArchivoTemporal(String archivoTemporal) {
        this.archivoTemporal = archivoTemporal;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public List<SspRegistroEvento> getLstObservaciones() {
        return lstObservaciones;
    }

    public void setLstObservaciones(List<SspRegistroEvento> lstObservaciones) {
        this.lstObservaciones = lstObservaciones;
    }

    public String getDetalleDuplicado() {
        return detalleDuplicado;
    }

    public void setDetalleDuplicado(String detalleDuplicado) {
        this.detalleDuplicado = detalleDuplicado;
    }

    public SbRecibos getReciboNuevo() {
        return reciboNuevo;
    }

    public void setReciboNuevo(SbRecibos reciboNuevo) {
        this.reciboNuevo = reciboNuevo;
    }

    public boolean isDisabledDatosAdministrado() {
        return disabledDatosAdministrado;
    }

    public void setDisabledDatosAdministrado(boolean disabledDatosAdministrado) {
        this.disabledDatosAdministrado = disabledDatosAdministrado;
    }

    public boolean isDisabledDatosVigilante() {
        return disabledDatosVigilante;
    }

    public void setDisabledDatosVigilante(boolean disabledDatosVigilante) {
        this.disabledDatosVigilante = disabledDatosVigilante;
    }

    public boolean isDisabledFotoVigilante() {
        return disabledFotoVigilante;
    }

    public void setDisabledFotoVigilante(boolean disabledFotoVigilante) {
        this.disabledFotoVigilante = disabledFotoVigilante;
    }

    public boolean isDisabledDatosRequisitos() {
        return disabledDatosRequisitos;
    }

    public void setDisabledDatosRequisitos(boolean disabledDatosRequisitos) {
        this.disabledDatosRequisitos = disabledDatosRequisitos;
    }

    public boolean isDisabledDatosAdministradoModalidad() {
        return disabledDatosAdministradoModalidad;
    }

    public void setDisabledDatosAdministradoModalidad(boolean disabledDatosAdministradoModalidad) {
        this.disabledDatosAdministradoModalidad = disabledDatosAdministradoModalidad;
    }

    public String getPaso() {
        return paso;
    }

    public void setPaso(String paso) {
        this.paso = paso;
    }

    public String getProcesoCodProg() {
        return procesoCodProg;
    }

    public void setProcesoCodProg(String procesoCodProg) {
        this.procesoCodProg = procesoCodProg;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public boolean isMuestraBusquedafecha() {
        return muestraBusquedafecha;
    }

    public void setMuestraBusquedafecha(boolean muestraBusquedafecha) {
        this.muestraBusquedafecha = muestraBusquedafecha;
    }

    public Date getFechaBusqueda() {
        return fechaBusqueda;
    }

    public void setFechaBusqueda(Date fechaBusqueda) {
        this.fechaBusqueda = fechaBusqueda;
    }

    public List<SbRecibos> getLstRecibos() {
        return lstRecibos;
    }

    public void setLstRecibos(List<SbRecibos> lstRecibos) {
        this.lstRecibos = lstRecibos;
    }

    public List<SbRecibos> getLstRecibosBusqueda() {
        return lstRecibosBusqueda;
    }

    public void setLstRecibosBusqueda(List<SbRecibos> lstRecibosBusqueda) {
        this.lstRecibosBusqueda = lstRecibosBusqueda;
    }

    public int getLimiteReg() {
        return limiteReg;
    }

    public void setLimiteReg(int limiteReg) {
        this.limiteReg = limiteReg;
    }

    public boolean isDisabledSiguiente() {
        return disabledSiguiente;
    }

    public void setDisabledSiguiente(boolean disabledSiguiente) {
        this.disabledSiguiente = disabledSiguiente;
    }

    public boolean isRenderDatosVigilante() {
        return renderDatosVigilante;
    }

    public void setRenderDatosVigilante(boolean renderDatosVigilante) {
        this.renderDatosVigilante = renderDatosVigilante;
    }

    public List<Map> getLstVigilanteBusqueda() {
        return lstVigilanteBusqueda;
    }

    public void setLstVigilanteBusqueda(List<Map> lstVigilanteBusqueda) {
        this.lstVigilanteBusqueda = lstVigilanteBusqueda;
    }

    public List<Map> getLstVigilante() {
        return lstVigilante;
    }

    public void setLstVigilante(List<Map> lstVigilante) {
        this.lstVigilante = lstVigilante;
    }

    public SspCarne getCarneSelected() {
        return carneSelected;
    }

    public void setCarneSelected(SspCarne carneSelected) {
        this.carneSelected = carneSelected;
    }

    public String getEstadoCese() {
        return estadoCese;
    }

    public void setEstadoCese(String estadoCese) {
        this.estadoCese = estadoCese;
    }

    public List<TipoSeguridad> getLstModalidadesVig() {
        return lstModalidadesVig;
    }

    public void setLstModalidadesVig(List<TipoSeguridad> lstModalidadesVig) {
        this.lstModalidadesVig = lstModalidadesVig;
    }

    public String getMsjeCese() {
        return msjeCese;
    }

    public void setMsjeCese(String msjeCese) {
        this.msjeCese = msjeCese;
    }

    public List<Map> getLstInscritos() {
        return lstInscritos;
    }

    public void setLstInscritos(List<Map> lstInscritos) {
        this.lstInscritos = lstInscritos;
    }

    public boolean isTieneObservacion() {
        return tieneObservacion;
    }

    public void setTieneObservacion(boolean tieneObservacion) {
        this.tieneObservacion = tieneObservacion;
    }

    public boolean isRenderDatosDuplicado() {
        return renderDatosDuplicado;
    }

    public void setRenderDatosDuplicado(boolean renderDatosDuplicado) {
        this.renderDatosDuplicado = renderDatosDuplicado;
    }

    public UploadedFile getFileDP() {
        return fileDP;
    }

    public void setFileDP(UploadedFile fileDP) {
        this.fileDP = fileDP;
    }

    public StreamedContent getArchivoDP() {
        return archivoDP;
    }

    public void setArchivoDP(StreamedContent archivoDP) {
        this.archivoDP = archivoDP;
    }

    public String getArchivoDenuncia() {
        return archivoDenuncia;
    }

    public void setArchivoDenuncia(String archivoDenuncia) {
        this.archivoDenuncia = archivoDenuncia;
    }

    public String getArchivoTemporalDenuncia() {
        return archivoTemporalDenuncia;
    }

    public void setArchivoTemporalDenuncia(String archivoTemporalDenuncia) {
        this.archivoTemporalDenuncia = archivoTemporalDenuncia;
    }

    public boolean isRenderDenunciaPolicial() {
        return renderDenunciaPolicial;
    }

    public void setRenderDenunciaPolicial(boolean renderDenunciaPolicial) {
        this.renderDenunciaPolicial = renderDenunciaPolicial;
    }

    public boolean isEsDevuelto() {
        return esDevuelto;
    }

    public void setEsDevuelto(boolean esDevuelto) {
        this.esDevuelto = esDevuelto;
    }

    public List<TipoBaseGt> getLstAreas() {
        return lstAreas;
    }

    public void setLstAreas(List<TipoBaseGt> lstAreas) {
        this.lstAreas = lstAreas;
    }

    public Long getNroSecuencia() {
        return nroSecuencia;
    }

    public void setNroSecuencia(Long nroSecuencia) {
        this.nroSecuencia = nroSecuencia;
    }

    public Long getNroSecuenciaVerDuplicado() {
        return nroSecuenciaVerDuplicado;
    }

    public void setNroSecuenciaVerDuplicado(Long nroSecuenciaVerDuplicado) {
        this.nroSecuenciaVerDuplicado = nroSecuenciaVerDuplicado;
    }

    public String getArchivoVerDuplicado() {
        return archivoVerDuplicado;
    }

    public void setArchivoVerDuplicado(String archivoVerDuplicado) {
        this.archivoVerDuplicado = archivoVerDuplicado;
    }

    public boolean isEsDuplicado() {
        return esDuplicado;
    }

    public void setEsDuplicado(boolean esDuplicado) {
        this.esDuplicado = esDuplicado;
    }

    public boolean isSolicitarRecibo() {
        return solicitarRecibo;
    }

    public void setSolicitarRecibo(boolean solicitarRecibo) {
        this.solicitarRecibo = solicitarRecibo;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDocView() {
        return numDocView;
    }

    public void setNumDocView(String numDocView) {
        this.numDocView = numDocView;
    }

    public boolean isEsEstandar() {
        return esEstandar;
    }

    public void setEsEstandar(boolean esEstandar) {
        this.esEstandar = esEstandar;
    }

    public String getTipoCarne() {
        return tipoCarne;
    }

    public void setTipoCarne(String tipoCarne) {
        this.tipoCarne = tipoCarne;
    }

    public Integer getTipoImagenAyuda() {
        return tipoImagenAyuda;
    }

    public void setTipoImagenAyuda(Integer tipoImagenAyuda) {
        this.tipoImagenAyuda = tipoImagenAyuda;
    }

    public TipoBaseGt getSedeRecojoDuplicado() {
        return sedeRecojoDuplicado;
    }

    public void setSedeRecojoDuplicado(TipoBaseGt sedeRecojoDuplicado) {
        this.sedeRecojoDuplicado = sedeRecojoDuplicado;
    }

    public String getUsuarioArchivar() {
        return usuarioArchivar;
    }

    public void setUsuarioArchivar(String usuarioArchivar) {
        this.usuarioArchivar = usuarioArchivar;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getTotalCarnesImpresion() {
        return totalCarnesImpresion;
    }

    public void setTotalCarnesImpresion(Integer totalCarnesImpresion) {
        this.totalCarnesImpresion = totalCarnesImpresion;
    }

    public Integer getTotalCarnesIniciales() {
        return totalCarnesIniciales;
    }

    public void setTotalCarnesIniciales(Integer totalCarnesIniciales) {
        this.totalCarnesIniciales = totalCarnesIniciales;
    }

    public Integer getTotalCarnesDuplicados() {
        return totalCarnesDuplicados;
    }

    public void setTotalCarnesDuplicados(Integer totalCarnesDuplicados) {
        this.totalCarnesDuplicados = totalCarnesDuplicados;
    }

    public Integer getTotalCarnesRenovados() {
        return totalCarnesRenovados;
    }

    public void setTotalCarnesRenovados(Integer totalCarnesRenovados) {
        this.totalCarnesRenovados = totalCarnesRenovados;
    }

    public Integer getTotalCarnesCambioEmpresa() {
        return totalCarnesCambioEmpresa;
    }

    public void setTotalCarnesCambioEmpresa(Integer totalCarnesCambioEmpresa) {
        this.totalCarnesCambioEmpresa = totalCarnesCambioEmpresa;
    }

    public TipoBaseGt getSedeSelected() {
        return sedeSelected;
    }

    public void setSedeSelected(TipoBaseGt sedeSelected) {
        this.sedeSelected = sedeSelected;
    }

    public PieChartModel getPieEstadistica() {
        return pieEstadistica;
    }

    public void setPieEstadistica(PieChartModel pieEstadistica) {
        this.pieEstadistica = pieEstadistica;
    }

    public Map getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(Map itemSelected) {
        this.itemSelected = itemSelected;
    }

    public boolean isAccionEjecutada() {
        return accionEjecutada;
    }

    public void setAccionEjecutada(boolean accionEjecutada) {
        this.accionEjecutada = accionEjecutada;
    }

    public boolean isBlnProcesoTransmision() {
        return blnProcesoTransmision;
    }

    public void setBlnProcesoTransmision(boolean blnProcesoTransmision) {
        this.blnProcesoTransmision = blnProcesoTransmision;
    }

    public String getCarneDigital() {
        return carneDigital;
    }

    public void setCarneDigital(String carneDigital) {
        this.carneDigital = carneDigital;
    }

    public String getRutaArchivoCargaCese() {
        return rutaArchivoCargaCese;
    }

    public void setRutaArchivoCargaCese(String rutaArchivoCargaCese) {
        this.rutaArchivoCargaCese = rutaArchivoCargaCese;
    }

    public String getNombreArchivoCargaCese() {
        return nombreArchivoCargaCese;
    }

    public void setNombreArchivoCargaCese(String nombreArchivoCargaCese) {
        this.nombreArchivoCargaCese = nombreArchivoCargaCese;
    }
    
    public List<Map> getLstObservacionCargaMasiva() {
        return lstObservacionCargaMasiva;
    }

    public void setLstObservacionCargaMasiva(List<Map> lstObservacionCargaMasiva) {
        this.lstObservacionCargaMasiva = lstObservacionCargaMasiva;
    }

    public List<Map> getLstVigilanteSelect() {
        return lstVigilanteSelect;
    }

    public void setLstVigilanteSelect(List<Map> lstVigilanteSelect) {
        this.lstVigilanteSelect = lstVigilanteSelect;
    }
    
    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        if (resultados != null) {
            buscar();
        }
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
//        resultados = new ListDataModel(ejbSspCarneFacade.selectLike(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     * @param item
     */
    public void mostrarVer(Map item) {
        reiniciarValores();
        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        cargarDatos();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     *
     * @param item
     */
    public void mostrarEditar(Map item) {
        reiniciarValores();
        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        cargarDatos();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     *
     * @return
     */
    public String mostrarCrear() {
        reiniciarValores();

        if (validacionPrevia()) {
            //solicitarRecibo = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXNombre("HABILITACION_TUPA").getValor()); //rfv
            solicitarRecibo = true;

            if (solicitarRecibo) {
                SbProcesoTupa parametrizacionEmision = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoIdByTipoOpeCodProg("TP_TUP_SUC1", 1402, "TP_OPE_INI");
                SbProcesoTupa parametrizacionDuplicado = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoIdByTipoOpeCodProg("TP_TUP_SUC1", 1402, "TP_OPE_COP");

                importeDuplicado = 0; //parametrizacionDuplicado.getImporte();
                importeEmision = parametrizacionEmision.getImporte();
                codAtributo = parametrizacionEmision.getCodTributo();
            }

            esEstandar = true;
            return direccionarMostrarCrear();

        }
        return null;
    }

    public String direccionarMostrarCrear() {
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt admin = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

        estado = EstadoCrud.CREAR;
        registro = new SspRegistro();
        registro.setActivo(JsfUtil.TRUE);
        registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registro.setAudNumIp(JsfUtil.getIpAddress());
        registro.setCarneId(new SspCarne());
        registro.setComprobanteId(null);
        registro.setEmpresaId(admin);
        registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
        registro.setFecha(new Date());
        registro.setFechaIni(null);
        registro.setFechaFin(null);
        registro.setId(null);
        registro.setNroExpediente(null);
        registro.setObservacion(null);
        registro.setRegistroId(null);
        registro.setRepresentanteId(null);
        registro.setSedeSucamec(null);
        registro.setSspRegistroEventoList(new ArrayList());
        registro.setSspRegistroList(new ArrayList());
        registro.setSspRequisitoList(new ArrayList());
        registro.setTipoRegId(null);
        registro.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));
        registro.setTipoProId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN"));
        registro.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));

        registro.getCarneId().setActivo(JsfUtil.TRUE);
        registro.getCarneId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registro.getCarneId().setAudNumIp(JsfUtil.getIpAddress());
        registro.getCarneId().setCese(JsfUtil.FALSE);
        registro.getCarneId().setDetalleDupli(null);
        registro.getCarneId().setEmitida(JsfUtil.FALSE);
        registro.getCarneId().setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECRN_NVO"));
        registro.getCarneId().setFechaBaja(null);
        registro.getCarneId().setFechaEmision(null);
        registro.getCarneId().setFechaIni(null);
        registro.getCarneId().setFechaFin(null);
        registro.getCarneId().setFotoId(null);
        registro.getCarneId().setHashQr(null);
        registro.getCarneId().setId(null);
        registro.getCarneId().setModalidadId(null);
        registro.getCarneId().setNroCarne(0);
        registro.getCarneId().setSspRegistroList(new ArrayList());
        registro.getCarneId().setVigilanteId(null);

        if (!esEstandar) {
            registro.getCarneId().setModalidadId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_EVT"));
        }

        return "/aplicacion/gssp/sspCarne/Create";
    }

    public void mostrarRenovar(Map item) {
        try {
            SspRegistro registroSeleccionado = ejbSspRegistroFacade.find((Long) item.get("ID"));
            if (validaRenovacion(registroSeleccionado)) {
                reiniciarValores();

                ////////// Actualización de registro anterior //////////
                if (JsfUtil.getFechaSinHora(registroSeleccionado.getCarneId().getFechaFin()).compareTo(JsfUtil.getFechaSinHora(new Date())) <= 0) {
                    registroSeleccionado.getCarneId().setActivo(JsfUtil.FALSE);     // Se desactiva solo si está vencido
                }
                registroSeleccionado.getCarneId().setFechaBaja(new Date());
                ////////////////////////////////////////////////////////////

                /////////////////// Nuevo Registro ////////////////////////
                SspPersonaFoto foto = new SspPersonaFoto();
                foto.setAlumnoId(null);
                foto.setId(null);
                foto.setFecha(new Date());
                foto.setActivo(JsfUtil.TRUE);
                foto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                foto.setAudNumIp(JsfUtil.getIpAddress());
                foto.setNombreFoto(registroSeleccionado.getCarneId().getFotoId().getNombreFoto());
                foto.setModuloId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN"));
                foto = (SspPersonaFoto) JsfUtil.entidadMayusculas(foto, "");

                registro = new SspRegistro();
                registro.setActivo(JsfUtil.TRUE);
                registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.setAudNumIp(JsfUtil.getIpAddress());
                registro.setCarneId(new SspCarne());
                registro.setComprobanteId(null);
                registro.setEmpresaId(registroSeleccionado.getEmpresaId());
                registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                registro.setFecha(new Date());
                registro.setFechaIni(null);
                registro.setFechaFin(null);
                registro.setId(null);
                registro.setNroExpediente(null);
                registro.setObservacion(JsfUtil.bundleBDIntegrado("sspCarne_renovacion_mensajePorEmitir"));
                registro.setRepresentanteId(null);
                registro.setRegistroId(registroSeleccionado);
                registro.setSedeSucamec(registroSeleccionado.getSedeSucamec());
                registro.setSspRegistroEventoList(new ArrayList());
                registro.setSspRegistroList(new ArrayList());
                registro.setSspRequisitoList(new ArrayList());
                registro.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR"));
                registro.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_REN"));
                registro.setTipoProId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN"));
                registro.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                registro = (SspRegistro) JsfUtil.entidadMayusculas(registro, "");

                registro.getCarneId().setActivo(JsfUtil.TRUE);
                registro.getCarneId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.getCarneId().setAudNumIp(JsfUtil.getIpAddress());
                registro.getCarneId().setCese(JsfUtil.FALSE);
                registro.getCarneId().setDetalleDupli(null);
                registro.getCarneId().setEmitida(JsfUtil.FALSE);
                registro.getCarneId().setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECRN_NVO"));
                registro.getCarneId().setFechaBaja(null);
                registro.getCarneId().setFechaEmision(null);
                registro.getCarneId().setFechaIni(null);
                registro.getCarneId().setFechaFin(null);
                registro.getCarneId().setFotoId(foto);
                registro.getCarneId().setHashQr(null);
                registro.getCarneId().setId(null);
                registro.getCarneId().setModalidadId(registroSeleccionado.getCarneId().getModalidadId());
                registro.getCarneId().setNroCarne(registroSeleccionado.getCarneId().getNroCarne());
                registro.getCarneId().setSspRegistroList(new ArrayList());
                registro.getCarneId().setVigilanteId(registroSeleccionado.getCarneId().getVigilanteId());
                registro.setCarneId((SspCarne) JsfUtil.entidadMayusculas(registro.getCarneId(), ""));
                
                SbProcesoTupa parametrizacionRenovacion = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoIdByTipoOpeCodProg("TP_TUP_SUC1", 1427, "TP_OPE_REN");

                importeEmision = parametrizacionRenovacion.getImporte();
                codAtributo = parametrizacionRenovacion.getCodTributo();

                cargarDatos();
                nombreFotoTemporal = "";
                estado = EstadoCrud.RENOVAR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar los datos.");
        }
    }

    public boolean validaRenovacion(SspRegistro registroSeleccionado) {
        SspRegistro carneTemp = ejbSspRegistroFacade.buscarRegistrActivosVigenteNoDuplicado(registroSeleccionado.getCarneId().getModalidadId().getId(), registroSeleccionado.getCarneId().getVigilanteId().getId());
        if (carneTemp == null) {
            JsfUtil.mensajeError("No se encontró el registro inicial");
            return false;
        }
        int cont = ejbSspRegistroFacade.buscarRegistroVigenteReferenciado(registroSeleccionado.getCarneId().getModalidadId().getId(), registroSeleccionado.getCarneId().getVigilanteId().getId(), registroSeleccionado.getId());
        if (cont > 0) {
            JsfUtil.mensajeError("No puede renovar el carné porque ya existe un registro de renovación");
            return false;
        }

        int diasRenovacionVencimiento = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("SspCarne_diasHabilesVencimientoRenova").getValor());
        Date fechaIni;
        int feriados = -1;
        int domingos = -1;
        Calendar c_fechaFin = Calendar.getInstance();
        Calendar c_adicional = Calendar.getInstance();

        c_adicional.setTime(carneTemp.getCarneId().getFechaFin());
        c_fechaFin.setTime(carneTemp.getCarneId().getFechaFin());
        c_adicional.add(Calendar.DATE, (-1 * diasRenovacionVencimiento));
        fechaIni = c_adicional.getTime();

        while ((feriados + domingos) != 0) {
            feriados = ejbSbFeriadoFacade.feriadosCalendario(JsfUtil.getFechaSinHora(fechaIni), c_fechaFin.getTime());
            domingos = JsfUtil.calcularNroDiasSabadoYDomingo(JsfUtil.getFechaSinHora(fechaIni), c_fechaFin.getTime());
            if ((feriados + domingos) > 0) {
                c_fechaFin.setTime(fechaIni);
                c_adicional.setTime(fechaIni);
                c_adicional.add(Calendar.DAY_OF_MONTH, ((feriados + domingos) * -1));
                fechaIni = c_adicional.getTime();
            } else {
                c_adicional.setTime(fechaIni);
                int diaSemana = c_adicional.get(Calendar.DAY_OF_WEEK);
                if (diaSemana == 1 || diaSemana == 7) {
                    c_adicional.add(Calendar.DAY_OF_MONTH, -1);
                    fechaIni = c_adicional.getTime();
                }
            }
        }

        if (JsfUtil.getFechaSinHora(fechaIni).compareTo(JsfUtil.getFechaSinHora(new Date())) > 0) {
            JsfUtil.mensajeError("Sólo se permite renovar el carné desde los " + diasRenovacionVencimiento + " días hábiles antes del vencimiento");
            return false;
        }

        return true;
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     * @return
     */
    public String editar() {
        Long idCarneTemp = 0L;
        try {
            if (validaCarnes()) {
                idCarneTemp = registro.getId();
                registro = (SspRegistro) JsfUtil.entidadMayusculas(registro, "");
                SspRegistro registroTemp = ejbSspRegistroFacade.find(registro.getId());
                prepararRegistro();

                if (registro.getCarneId().getVigilanteId().getTipoDoc().getCodProg().equals("TP_DOCID_CE")) {
                    ejbSbPersonaFacade.edit(registro.getCarneId().getVigilanteId());
                }
                if (registro.getTipoRegId().getCodProg().equals("TP_REGIST_EMP")) {
                    SspRegistro reg = ejbSspRegistroFacade.buscarRegistroCesadoXRucXModalidadXDoc(null, registro.getCarneId().getModalidadId().getId(), registro.getCarneId().getVigilanteId().getId(), registro.getId());
                    if (reg == null) {
                        JsfUtil.mensajeError("No se pudo encontrar el registro de cese");
                        return null;
                    }
                    registro.setRegistroId(reg);
                    registro.setObservacion(JsfUtil.bundleBDIntegrado("sspCarne_cambioEmpresa_mensajePorEmitir"));
                    registro.getCarneId().setNroCarne(reg.getCarneId().getNroCarne());
                }

                if (!Objects.equals(nombreFoto, nombreFotoTemporal)) {
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + registro.getCarneId().getFotoId().getNombreFoto()), fotoByte);
                    registroTemp.getCarneId().getFotoId().setActivo(JsfUtil.FALSE);
                    ejbSspPersonaFotoFacade.edit(registroTemp.getCarneId().getFotoId());
                }
                if (!Objects.equals(archivo, archivoTemporal)) {
                    for (SspRequisito req : registro.getSspRequisitoList()) {
                        if (req.getActivo() == 1 && req.getId() == null && req.getTipoRequisitoId().getCodProg().equals("TP_TREQ_DJ")) {
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + req.getSspArchivoList().get(0).getNombre()), djByte);
                        }
                    }
                }
                //Verifica si cambió el certificado médico
                if (!Objects.equals(certificado, certificadoTemporal)) {
                    for (SspRequisito req : registro.getSspRequisitoList()) {
                        if (req.getActivo() == 1 && req.getId() == null && req.getTipoRequisitoId().getCodProg().equals("TP_TREQ_CM")) {
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + req.getSspArchivoList().get(0).getNombre()), cmByte);
                        }
                    }
                }
                //
                ejbSspRegistroFacade.edit(registro);
                if (registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
                    if (registro.getRegistroId() != null && registroTemp.getRegistroId() != null) {
                        boolean flagGuardarRef = (!Objects.equals(registroTemp.getRegistroId().getCarneId().getEstadoId().getId(), registro.getRegistroId().getCarneId().getEstadoId().getId())
                                || !Objects.equals(registroTemp.getRegistroId().getCarneId().getDetalleDupli(), registro.getRegistroId().getCarneId().getDetalleDupli()));
                        if (!Objects.equals(archivoDenuncia, archivoTemporalDenuncia)) {
                            if (!((archivoDenuncia == null || archivoDenuncia.isEmpty()) && (archivoTemporalDenuncia != null && !archivoTemporalDenuncia.isEmpty()))) {
                                for (SspRequisito req : registro.getSspRequisitoList()) {
                                    if (req.getActivo() == 1 && req.getId() == null && req.getTipoRequisitoId().getCodProg().equals("TP_TREQ_DP")) {
                                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + req.getSspArchivoList().get(0).getNombre()), dpByte);
                                        flagGuardarRef = true;
                                    }
                                }
                            }
                        }
                        if (flagGuardarRef) {
                            ejbSspRegistroFacade.edit(registro.getRegistroId());
                        }
                    }
                }

                //////////////////////// Actualización de recibo ////////////////////////
                if (solicitarRecibo) {
                    if (!Objects.equals(((registroTemp.getComprobanteId() != null) ? registroTemp.getComprobanteId().getId() : null), ((registro.getComprobanteId() != null) ? registro.getComprobanteId().getId() : null))) {
                        if (registroTemp.getComprobanteId() != null) {
                            for (SbReciboRegistro recReg : registroTemp.getComprobanteId().getSbReciboRegistroList()) {
                                if (recReg.getActivo() == 1) {
                                    recReg.setActivo(JsfUtil.FALSE);
                                    ejbSbRecibosFacade.edit(registroTemp.getComprobanteId());
                                    break;
                                }
                            }
                        }
                        if (registro.getComprobanteId() != null) {
                            SbReciboRegistro rec = new SbReciboRegistro();
                            rec.setId(null);
                            rec.setRegistroId(null);
                            rec.setNroExpediente("S/N");    // Pendiente de actualizar al transmitir
                            rec.setReciboId(registro.getComprobanteId());
                            rec.setActivo((short) 1);
                            rec.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            rec.setAudNumIp(JsfUtil.getIpAddress());
                            if (registro.getComprobanteId().getSbReciboRegistroList() == null) {
                                registro.getComprobanteId().setSbReciboRegistroList(new ArrayList());
                            }
                            registro.getComprobanteId().getSbReciboRegistroList().add(rec);
                            ejbSbRecibosFacade.edit(registro.getComprobanteId());
                        }
                    }
                }
                ////////////////////////////////////////////////////////////////////////

                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado") + " Código: " + registro.getId());
                return prepareList();
            }
        } catch (Exception e) {
            System.err.println("ID Carné: " + idCarneTemp);
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     * @return
     */
    public String crear() {
        try {
            if (validaCarnes()) {
                prepararRegistro();
                registro = (SspRegistro) JsfUtil.entidadMayusculas(registro, "");

                if (registro.getCarneId().getVigilanteId().getTipoDoc().getCodProg().equals("TP_DOCID_CE")) {
                    ejbSbPersonaFacade.edit(registro.getCarneId().getVigilanteId());
                }
                if (registro.getTipoRegId().getCodProg().equals("TP_REGIST_EMP") && (registro.getRegistroId() == null || registro.getCarneId().getNroCarne() == 0)) {
                    SspRegistro reg = ejbSspRegistroFacade.buscarRegistroCesadoXRucXModalidadXDoc(null, registro.getCarneId().getModalidadId().getId(), registro.getCarneId().getVigilanteId().getId(), registro.getId());
                    if (reg == null) {
                        JsfUtil.mensajeError("No se pudo encontrar el registro de cese");
                        return null;
                    }
                    registro.setRegistroId(reg);
                    registro.setObservacion(JsfUtil.bundleBDIntegrado("sspCarne_cambioEmpresa_mensajePorEmitir"));
                    registro.getCarneId().setNroCarne(reg.getCarneId().getNroCarne());
                }

                if (!Objects.equals(nombreFoto, nombreFotoTemporal)) {
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + registro.getCarneId().getFotoId().getNombreFoto()), fotoByte);
                }
                if (!Objects.equals(archivo, archivoTemporal)) {
                    for (SspRequisito req : registro.getSspRequisitoList()) {
                        if (req.getActivo() == 1 && req.getId() == null && req.getTipoRequisitoId().getCodProg().equals("TP_TREQ_DJ")) {
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + req.getSspArchivoList().get(0).getNombre()), djByte);
                        }
                    }
                }
                //Certificado Médico
                if (!Objects.equals(certificado, certificadoTemporal)) {
                    for (SspRequisito req : registro.getSspRequisitoList()) {
                        if (req.getActivo() == 1 && req.getId() == null && req.getTipoRequisitoId().getCodProg().equals("TP_TREQ_CM")) {
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + req.getSspArchivoList().get(0).getNombre()), cmByte);
                        }
                    }
                }
                //

                adicionaEvento(registro, "");
                registro.setId(null);
                registro.getCarneId().setId(null);
                registro.setNroSolicitiud(obtenerNroSolicitudEmision());
                ejbSspRegistroFacade.create(registro);

                /////////////////////////// RENOVACION ////////////////////////////////
                if (registro.getTipoOpeId().getCodProg().equals("TP_OPE_REN") && registro.getRegistroId() != null) {
                    ejbSspRegistroFacade.edit(registro.getRegistroId());
                }
                /////////////////////////////////////////////////////////////////////////

                //////////////////////// Actualización de recibo ////////////////////////                
              /*  if (solicitarRecibo && registro.getComprobanteId() != null) {
                    SbReciboRegistro rec = new SbReciboRegistro();
                    rec.setId(null);
                    rec.setRegistroId(null);
                    rec.setNroExpediente("S/N");    // Pendiente de actualizar al transmitir
                    rec.setReciboId(registro.getComprobanteId());
                    rec.setActivo((short) 1);
                    rec.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    rec.setAudNumIp(JsfUtil.getIpAddress());
                    if (registro.getComprobanteId().getSbReciboRegistroList() == null) {
                        registro.getComprobanteId().setSbReciboRegistroList(new ArrayList());
                    }
                    registro.getComprobanteId().getSbReciboRegistroList().add(rec);
                    ejbSbRecibosFacade.edit(registro.getComprobanteId());
                }*/
                ////////////////////////////////////////////////////////////////////////

                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado") + " Código: " + registro.getId() + " con Nro. de solicitud: " + registro.getNroSolicitiud());
                return prepareList();
            }
        } catch (Exception e) {
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }

    public boolean validaCarnes() {
        boolean validacion = true;

        if (registro.getSedeSucamec() == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":dondeRecoger");
            JsfUtil.mensajeError("Por favor seleccionar la sede");
        }
        if (registro.getCarneId().getModalidadId() == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":modalidad");
            JsfUtil.mensajeError("Por favor seleccionar la modalidad");
        }
        if (registro.getTipoRegId() == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":tipoRegistro");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de registro");
        }
        if (tipoDocSelected == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":tipoDoc");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de documento");
        }
        if (numDoc != null) {
            numDoc = numDoc.trim();
        } else {
            numDoc = "";
        }
        if (registro.getTipoOpeId() != null && registro.getRegistroId() != null && registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
            if (registro.getRegistroId().getCarneId().getEstadoId() == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":motivo");
                JsfUtil.mensajeError("Por favor seleccionar el estado del carné referenciado");
            }
            if (registro.getRegistroId().getCarneId().getDetalleDupli() == null || registro.getRegistroId().getCarneId().getDetalleDupli().isEmpty()) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":detalle");
                JsfUtil.mensajeError("Por favor ingresar el detalle por el cual gestionó el duplicado");
            }
        }
        if (numDoc.isEmpty()) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":numDoc");
            JsfUtil.mensajeError("Por favor ingresar el nro. de documento");
        } else if (registro.getCarneId().getVigilanteId() == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":tipoDoc");
            JsfUtil.invalidar(obtenerForm() + ":numDoc");
            JsfUtil.mensajeError("Por favor buscar a la persona del carné de identidad");
        } else {
            if (nombres == null || nombres.isEmpty()) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":nombres");
                JsfUtil.mensajeError("Por favor ingresar los nombres");
            }
            if (apePat == null || apePat.isEmpty()) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":apePat");
                JsfUtil.mensajeError("Por favor ingresar el apellido paterno");
            }
            if (tipoDocSelected != null && tipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                if (fechaNac == null) {
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm() + ":fechaNac");
                    JsfUtil.mensajeError("Por favor ingresar la fecha de nacimiento");
                }
            }
        }
        if (nombreFoto == null) {
            nombreFoto = "";
        }
        if (nombreFoto.isEmpty()) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":idFoto");
            JsfUtil.mensajeError("Por favor cargar la foto del personal de seguridad");
        }
        /*if (registro.getComprobanteId() == null && solicitarRecibo) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":comprobante");
            JsfUtil.mensajeError("Por favor ingresar el comprobante");
        }*/
        if (archivo == null) {
            archivo = "";
        }
        if (archivo.isEmpty() && registro.getTipoOpeId() != null && !registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":archivoDJ");
            JsfUtil.mensajeError("Por favor cargar la DJ en formato PDF");
        }
        if (JsfUtil.getFechaSinHora((Date) registro.getFecha()).compareTo(JsfUtil.stringToDate(JsfUtil.bundleBDIntegrado("sspCarne_create_fechaCMedico"), "dd/MM/yyyy")) >= 0) {
            if (certificado == null) {
                certificado = "";
            }
            if (certificado.isEmpty() && registro.getTipoOpeId() != null && !registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":certificadoMedico");
                JsfUtil.mensajeError("Por favor cargar el certificado médico en formato PDF");
            }
        }

        if (tienePerfilSispe()) {
            if (registro.getCarneId().getModalidadId() != null
                    && !registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_SIS")
                    && !registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_PAT")
                    && !registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_ASC")) {
                validacion = false;
                JsfUtil.mensajeError(registro.getCarneId().getModalidadId().getNombre() + " no puede seleccionarse con el perfil SISPE");
            }
        }
        if (validacion) {
            if (estado == EstadoCrud.EDITAR) {
                if (registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
                    return validacion;
                }
            } else {
                registro.setSspRequisitoList(new ArrayList());
            }
            if (!validaBusquedaVigilante()) {
                setDisabledNombres(true);
                validacion = false;
            }
        }

        return validacion;
    }

    public boolean renderBtnArchivoDjEditar() {
        if (registro != null) {
            return archivo != null;
        }
        return false;
    }

    public boolean renderBtnArchivoCmEditar() {
        if (registro != null) {
            if (JsfUtil.getFechaSinHora((Date) registro.getFecha()).compareTo(JsfUtil.stringToDate(JsfUtil.bundleBDIntegrado("sspCarne_create_fechaCMedico"), "dd/MM/yyyy")) >= 0) {
                return true;
            }
            return certificado != null;
        }
        return false;
    }

    public String obtenerNroSolicitudEmision() {
        String nroSolicitud = "";
        nroSolicitud = ejbSspRegistroFacade.obtenerNroSolicitudEmisionRegistro() + "-" + JsfUtil.mostrarAnio(new Date()) + "-SUCAMEC/GSSP-EC";
        return nroSolicitud;
    }

    public String obtenerNroSolicitudCese() {
        String nroSolicitud = "";
        nroSolicitud = ejbSspRegistroFacade.obtenerNroSolicitudCeseRegistro() + "-" + JsfUtil.mostrarAnio(new Date()) + "-SUCAMEC/GSSP-CC";
        return nroSolicitud;
    }

    public String obtenerNroSolicitudDevolucion() {
        String nroSolicitud = "";
        nroSolicitud = ejbSspRegistroFacade.obtenerNroSolicitudDevolucionRegistro() + "-" + JsfUtil.mostrarAnio(new Date()) + "-SUCAMEC/GSSP-DC";
        return nroSolicitud;
    }

    public void prepararRegistro() {
        String fileName = "";
        if (archivo != null && archivo.isEmpty()) {
            archivo = null;
        }
        if (archivoTemporal != null && archivoTemporal.isEmpty()) {
            archivoTemporal = null;
        }
        if (certificado != null && certificado.isEmpty()) {
            certificado = null;
        }
        if (certificadoTemporal != null && certificadoTemporal.isEmpty()) {
            certificadoTemporal = null;
        }
        if (nombreFoto != null && nombreFoto.isEmpty()) {
            nombreFoto = null;
        }
        if (archivoDenuncia != null && archivoDenuncia.isEmpty()) {
            archivoDenuncia = null;
        }
        registro.getCarneId().getVigilanteId().setFechaNac(fechaNac);
        if (!Objects.equals(nombreFoto, nombreFotoTemporal)) {
            fileName = nombreFoto;
            fileName = "FOTO_C" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_FOTOCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());

            SspPersonaFoto foto = new SspPersonaFoto();
            foto.setAlumnoId(null);
            foto.setId(null);
            foto.setFecha(new Date());
            foto.setActivo(JsfUtil.TRUE);
            foto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            foto.setAudNumIp(JsfUtil.getIpAddress());
            foto.setNombreFoto(fileName);
            foto.setModuloId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN"));
            foto = (SspPersonaFoto) JsfUtil.entidadMayusculas(foto, "");
            registro.getCarneId().setFotoId(foto);
        }
        if (!Objects.equals(archivo, archivoTemporal)) {
            fileName = archivo;
            fileName = "DJ_" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_DJCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());

            SspRequisito requisito = new SspRequisito();
            requisito.setActivo(JsfUtil.TRUE);
            requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            requisito.setAudNumIp(JsfUtil.getIpAddress());
            requisito.setFecha(new Date());
            requisito.setId(null);
            requisito.setRegistroId(registro);
            requisito.setSspArchivoList(new ArrayList());
            requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TREQ_DJ"));
            requisito.setValorRequisito(null);
            requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");

            SspArchivo archivo = new SspArchivo();
            archivo.setActivo(JsfUtil.TRUE);
            archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            archivo.setAudNumIp(JsfUtil.getIpAddress());
            archivo.setId(null);
            archivo.setNombre(fileName);
            archivo.setRequisitoId(requisito);
            archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
            archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
            requisito.getSspArchivoList().add(archivo);
            registro.getSspRequisitoList().add(requisito);
        }
        if (!Objects.equals(certificado, certificadoTemporal)) {
            fileName = certificado;

            fileName = "CM_" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_DJCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());

            SspRequisito requisito = new SspRequisito();
            requisito.setActivo(JsfUtil.TRUE);
            requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            requisito.setAudNumIp(JsfUtil.getIpAddress());
            requisito.setFecha(new Date());
            requisito.setId(null);
            requisito.setRegistroId(registro);
            requisito.setSspArchivoList(new ArrayList());
            requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TREQ_CM"));
            requisito.setValorRequisito(null);
            requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");

            SspArchivo certificado = new SspArchivo();
            certificado.setActivo(JsfUtil.TRUE);
            certificado.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            certificado.setAudNumIp(JsfUtil.getIpAddress());
            certificado.setId(null);
            certificado.setNombre(fileName);
            certificado.setRequisitoId(requisito);
            certificado.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
            certificado = (SspArchivo) JsfUtil.entidadMayusculas(certificado, "");
            requisito.getSspArchivoList().add(certificado);
            registro.getSspRequisitoList().add(requisito);
        }

        //Falta definif si el certificado médico entra en la lógica de duplicado
        if (registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP") && registro != null && registro.getRegistroId() != null) {
            if (registro.getRegistroId().getCarneId().getEstadoId().getCodProg().equals("TP_ECRN_ROB")) {
                if (!Objects.equals(archivoDenuncia, archivoTemporalDenuncia)) {
                    if ((archivoDenuncia == null || archivoDenuncia.isEmpty()) && (archivoTemporalDenuncia != null && !archivoTemporalDenuncia.isEmpty())) {
                        for (SspRequisito req : registro.getSspRequisitoList()) {
                            if (req.getActivo() == 1 && Objects.equals(req.getValorRequisito(), archivoTemporalDenuncia)) {
                                req.setActivo(JsfUtil.FALSE);
                                break;
                            }
                        }
                    } else {
                        fileName = archivoDenuncia;
                        fileName = "DP_" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_DPCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());

                        SspRequisito requisito = new SspRequisito();
                        requisito.setActivo(JsfUtil.TRUE);
                        requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        requisito.setAudNumIp(JsfUtil.getIpAddress());
                        requisito.setFecha(new Date());
                        requisito.setId(null);
                        requisito.setRegistroId(registro);
                        requisito.setSspArchivoList(new ArrayList());
                        requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TREQ_DP"));
                        requisito.setValorRequisito(null);
                        requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");

                        SspArchivo archivo = new SspArchivo();
                        archivo.setActivo(JsfUtil.TRUE);
                        archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        archivo.setAudNumIp(JsfUtil.getIpAddress());
                        archivo.setId(null);
                        archivo.setNombre(fileName);
                        archivo.setRequisitoId(requisito);
                        archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                        archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                        requisito.getSspArchivoList().add(archivo);
                        registro.getSspRequisitoList().add(requisito);
                    }
                }
            } else if (registro.getRegistroId().getSspRequisitoList() != null) {
                for (SspRequisito req : registro.getRegistroId().getSspRequisitoList()) {
                    if (req.getActivo() == 1 && req.getTipoRequisitoId().getCodProg().equals("TP_TREQ_DP")) {
                        req.setActivo(JsfUtil.FALSE);
                        break;
                    }
                }
            }
        }
        if (estado == EstadoCrud.CREAR && registro.getTipoRegId() != null && registro.getTipoRegId().getCodProg().equals("TP_REGIST_EMP")) {
            SspRegistro reg = ejbSspRegistroFacade.buscarRegistroCesadoXRucXModalidadXDoc(null, registro.getCarneId().getModalidadId().getId(), registro.getCarneId().getVigilanteId().getId(), registro.getId());
            registro.setRegistroId(reg);
            registro.setObservacion(JsfUtil.bundleBDIntegrado("sspCarne_cambioEmpresa_mensajePorEmitir"));
            registro.getCarneId().setNroCarne(reg.getCarneId().getNroCarne());
        }
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SspCarne getSspCarne(Long id) {
        return ejbSspCarneFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SspCarneController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sspCarneConverter")
    public static class SspCarneControllerConverterN extends SspCarneControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SspCarne.class)
    public static class SspCarneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SspCarneController c = (SspCarneController) JsfUtil.obtenerBean("sspCarneController", SspCarneController.class);
            return c.getSspCarne(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SspCarne) {
                SspCarne o = (SspCarne) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SspCarne.class.getName());
            }
        }
    }

    ////////////////////////
    public String prepareBuscar() {
        reiniciarCamposBusqueda();
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/gssp/sspCarne/List";
    }

    public String prepareList() {
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        reiniciarCamposBusqueda();
        resultados = null;
        esDevuelto = false;

        if (p_user.getPersona() == null) {
            JsfUtil.mensajeAdvertencia("Opción válida para personas con RUC");
            return null;
        }
        if (p_user.getPersona().getRuc() == null) {
            JsfUtil.mensajeAdvertencia("Opción válida para personas con RUC");
            return null;
        }

        verificarModalidades(p_user.getPersona().getRuc());
        tieneObservacion = (ejbSspRegistroFacade.obtenerTotalRegistrosObservados(p_user.getPersona().getRuc()) > 0);
        if (tieneObservacion) {
            JsfUtil.mensajeAdvertencia("Ud. tiene algunos registros de solicitudes observadas");
        }
        obtenerParametrosInicial();
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/gssp/sspCarne/List";
    }

    public void obtenerParametrosInicial() {
        DIAS_RENOVACION = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_diasProcEmi").getValor());
        fechaLimiteDuplicado = JsfUtil.stringToDate(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_fechaCorteDuplicado").getValor(), "dd/MM/yyyy");
        procesoCodProg = "TP_GSSP_ECRN";
        tipoCarne = "ESTANDAR";
        blnProcesoTransmision = false;
        totalProcesados = 0;
        actualProcesado = 0;
        progress = 0;
    }

    public void reiniciarCamposBusqueda() {
        buscarPor = null;
        filtro = null;
        numDoc = null;
        tipoDoc = null;
        fechaInicio = null;
        fechaFinal = null;
        estadoSelected = null;
        tipoRegSelected = null;
        tipoOpeSelected = null;
        modalidadSelected = null;
        sedeSelected = null;
        maxLength = 0;
        resultadosSeleccionados = new ArrayList();
        lstModalidadesActivas = new ArrayList();
        labelCursoVig = null;
        labelMsjCurso = null;
        setDisabledBtnTransmitir(false);
    }

    public void reiniciarValores() {
        buscarPor = null;
        filtro = null;
        areaSelected = null;
        tipoDocSelected = null;
        numDoc = null;
        numDocView = null;
        nombres = null;
        apePat = null;
        apeMat = null;
        tipoDoc = null;
        fechaInicio = null;
        fechaFinal = null;
        maxLength = 0;
        estadoSelected = null;
        tipoRegSelected = null;
        tipoOpeSelected = null;
        modalidadSelected = null;
        sedeSelected = null;
        nombreFoto = null;
        nombreFotoTemporal = null;
        archivo = null;
        certificado = null;
        archivoTemporal = null;
        certificadoTemporal = null;
        foto = null;
        fotoByte = null;
        file = null;
        archivoDJ = null;
        djByte = null;
        cmByte = null;
        fileDJ = null;
        importeEmision = 0;
        importeDuplicado = 0;
        detalleDuplicado = null;
        reciboNuevo = null;
        limiteReg = 0;
        carneSelected = null;
        estadoCese = null;
        fileDP = null;
        archivoDP = null;
        dpByte = null;
        archivoDenuncia = null;
        nroSecuencia = null;
        archivoTemporalDenuncia = null;
        sedeRecojoDuplicado = null;
        esDuplicado = false;
        resultados = new ArrayList();
        resultadosSeleccionados = new ArrayList();
        lstModalidadesActivas = new ArrayList();
        lstAreas = new ArrayList();
        lstObservaciones = new ArrayList();
        lstRecibos = new ArrayList();
        lstVigilanteBusqueda = new ArrayList();
        lstVigilante = new ArrayList();
        lstVigilanteSelect = new ArrayList();
        lstModalidadesVig = new ArrayList();
        usuarioArchivar = "";
        totalCarnesImpresion = 0;
        totalCarnesIniciales = 0;
        totalCarnesDuplicados = 0;
        totalCarnesRenovados = 0;
        totalCarnesCambioEmpresa = 0;
        resultados = null;
        carneDigital = null;

        setDisabledBtnTransmitir(false);
        setDisabledNombres(true);
        setEsNuevo(false);
        setDisabledDatosAdministrado(false);
        setDisabledDatosVigilante(false);
        setDisabledFotoVigilante(false);
        setDisabledDatosRequisitos(false);
        setDisabledDatosAdministradoModalidad(false);
        setDisabledSiguiente(true);
        setRenderDatosVigilante(false);
        setRenderDatosDuplicado(false);
        setRenderDenunciaPolicial(false);
        setEsEstandar(true);

        pieEstadistica = new PieChartModel();
    }

    public void cambiarTipoBuscarPor() {
        filtro = null;
        modalidadSelected = null;
        sedeSelected = null;
        tipoServicio = null;
    }

    public String obtenerForm() {
        switch (estado) {
            case CREAR:
                return "createForm";
            case EDITAR:
                return "editForm";
            case VER:
                return "viewForm";
            case BUSCAR:
                return "listForm";
            case RENOVAR:
                return "renovarForm";
            case DUPLICAR:
                return "duplicadoForm";
            case BANDEJACESE:
                return "ceseListForm";
            case BANDEJADEV:
                return "devListForm";
            case CESE:
                return "ceseForm";
            case IMPRIMIR:
                return "impresionForm";
        }
        return null;
    }

    public void buscarBandeja() {
        boolean validacion = true;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");

        if (buscarPor != null) {
            if (!buscarPor.equals("5") && (filtro == null || filtro.isEmpty()) && !buscarPor.equals("7") ) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":filtroBusqueda");
                JsfUtil.mensajeAdvertencia("Es necesario ingresar un tipo de búsqueda");
            }
            if (buscarPor.equals("5") && modalidadSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":modalidades");
                JsfUtil.mensajeAdvertencia("Es necesario seleccionar una modalidad");
            }
            if (buscarPor.equals("6") && sedeSelected == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":dondeRecoger");
                JsfUtil.mensajeAdvertencia("Es necesario seleccionar una sede");
            }
            if (buscarPor.equals("7") && tipoServicio == null) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":tipoServicio");
                JsfUtil.mensajeAdvertencia("Es necesario seleccionar un tipo de servicio");
            }
        }
        if (fechaInicio != null && fechaFinal != null) {
            if (JsfUtil.getFechaSinHora(fechaInicio).compareTo(JsfUtil.getFechaSinHora(fechaFinal)) > 0) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":fechaInicio");
                JsfUtil.invalidar(obtenerForm() + ":fechaFin");
                JsfUtil.mensajeAdvertencia("La fecha de inicio no puede ser mayor que la fecha fin");
            }
        }
        if (filtro != null) {
            filtro = filtro.trim().toUpperCase();
        }
        if (buscarPor != null) {
            buscarPor = buscarPor.trim().toUpperCase();
        }
        if (validacion) {
            System.out.println("p_user.getTipoDoc()->"+p_user.getTipoDoc());
            System.out.println("p_user.getNumDoc()->"+p_user.getNumDoc());
            
            SbPersonaGt administ = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
            HashMap mMap = new HashMap();
            mMap.put("buscarPor", buscarPor);
            mMap.put("filtro", filtro);
            mMap.put("estado", ((estadoSelected != null) ? estadoSelected.getId() : null));
            mMap.put("fechaIni", fechaInicio);
            mMap.put("fechaFin", fechaFinal);
            mMap.put("tipoReg", ((tipoRegSelected != null) ? tipoRegSelected.getId() : null));
            mMap.put("tipoPro", procesoCodProg);
            mMap.put("modalidad", ((modalidadSelected != null) ? modalidadSelected.getId() : null));
            mMap.put("administrado", administ.getId());
            mMap.put("tipoCarne", tipoCarne);
            mMap.put("tipoServicio", ((tipoServicio != null) ? tipoServicio.getId() : null));

            switch (procesoCodProg) {
                case "TP_GSSP_ECRN":
                    resultados = ejbSspCarneFacade.buscarBandeja(mMap);
                    break;
                case "TP_GSSP_CCRN":
                    mMap.put("esDevuelto", null);
                    resultados = ejbSspCarneFacade.buscarBandejaCeseDev(mMap);
                    break;
                case "TP_GSSP_DCRN":
                    mMap.put("esDevuelto", esDevuelto);
                    resultados = ejbSspCarneFacade.buscarBandejaCeseDev(mMap);
                    break;
            }
            reiniciarCamposBusqueda();
        }
    }

    public String getMensajeConfirmacionProceso() {
        if (tieneObservacion) {
            return JsfUtil.bundleBDIntegrado("sspCarne_confirmarTransmitirObservaciones");
        } else {
            return JsfUtil.bundleBDIntegrado("sspCarne_confirmarTransmitir");
        }
    }

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

    public void openDlgcompletarProceso() {
        progress = 0;
        actualProcesado = 0;
        totalProcesados = 0;
        blnProcesoTransmision = false;
        RequestContext.getCurrentInstance().execute("PF('wvDialogCompletarProceso').show()");
        RequestContext.getCurrentInstance().update("frmCompletarProceso");
    }

    public void transmitirCarne() {
        if (!resultadosSeleccionados.isEmpty()) {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            Integer usuaTramDoc = ejbSbUsuarioFacadeGt.obtenerIdUsuarioTramDoc("USRWEB");
            SbPersonaGt per = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
            boolean flagComprobante;
            int cont = 0;
            String timeStamp = "" + (new Date()).getTime();
            System.err.println("Transmitir carné(s) " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss") + ", " + timeStamp + ", Login: " + p_user.getNumDoc());
            blnProcesoTransmision = true;
            totalProcesados = resultadosSeleccionados.size();

            for (Map item : resultadosSeleccionados) {
                actualProcesado++;
                registro = ejbSspRegistroFacade.buscarRegistroById((Long) item.get("ID"));
                System.err.println("ID: " + registro.getId() + ", estado: " + registro.getEstadoId().getNombre() + ", Login: " + p_user.getNumDoc());
                flagComprobante = false;
                usuarioArchivar = "USRWEB";

                if (!validaTransmitir()) {
                    continue;
                }
                if (registro.getNroExpediente() == null) {
                    flagComprobante = true;
                }
                if (!generaExpediente(registro, usuaTramDoc, per, registro.getEstadoId().getCodProg())) {
                    JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro con código: " + registro.getId());
                    continue;
                }
                if (!guardarDatosCarne()) {
                    archivarExpediente(registro.getNroExpediente(), usuarioArchivar);
                    continue;
                }
                guardarDatosRecibo(flagComprobante);

                cont++;
                JsfUtil.mensaje("Se transmitió el registro Código: " + registro.getId() + ", con expediente: " + registro.getNroExpediente());
                registro = null;
            }
            reiniciarCamposBusqueda();
            resultados = new ArrayList();
            System.err.println("Fin transmisión :" + timeStamp + ", Login: " + p_user.getNumDoc());

            blnProcesoTransmision = true;

            RequestContext.getCurrentInstance().execute("PF('wvDialogCompletarProceso').hide()");
            RequestContext.getCurrentInstance().update("listForm");
            if (cont > 0) {
                JsfUtil.mensaje("Se procesó " + cont + " registro(s) correctamente.");
            } else {
                JsfUtil.mensajeAdvertencia("No se procesó ningún registro.");
            }
        } else {
            JsfUtil.mensajeAdvertencia("Por favor seleccionar al menos un registro para transmitir");
        }
    }

    public boolean guardarDatosCarne() {
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

    public boolean validaTransmitir() {
        //Se reritó Rev 743f0fb validación para no permitir transmitir carnés con tipo de operación RENOVACIÓN
        if (registro.getEstadoId().getCodProg().equals("TP_ECC_TRA")) {
            JsfUtil.mensajeError("El registro con ID " + registro.getId() + " ya ha sido transmitido");
            return false;
        }
        if (registro != null && registro.getTipoRegId().getCodProg().equals("TP_REGIST_NOR") && registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
            if (registro.getCarneId().getFechaFin() != null && JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(registro.getCarneId().getFechaFin())) > 0) {
                JsfUtil.mensajeAdvertencia("Ud. no puede transmitir este DUPLICADO porque el carné se encuentra vencido.");
                return false;
            }

        }
        return true;
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

    public boolean getRenderBuscarPor() {
        boolean valida = false;
        if (buscarPor != null && !buscarPor.equals("4")) {
            valida = true;
        } else if (buscarPor == null) {
            valida = true;
        }
        return valida;
    }

    public boolean renderBtnVer() {
        return true;
    }

    public boolean renderBtnEditar(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE") || item.get("ESTADO_CODPROG").equals("TP_ECC_OBS")) {
                validar = true;
//                if(item.get("TIPO_OPE_CODPROG").equals("TP_OPE_COP") ){
//                    validar = false;
//                }
            }
        }
        return validar;
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

    public boolean renderBtnRenovar(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_APR")
                    && item.get("FECHA_FIN") != null && item.get("FECHA_INI") != null) {
                validar = true;
            }
        }
        return validar;
    }

    public boolean renderBtnDuplicado(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_APR")
                    && item.get("FECHA_FIN") != null && JsfUtil.getFechaSinHora((Date) item.get("FECHA_FIN")).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0) {
                validar = true;
            }
            if (item.get("FECHA_INI") != null && JsfUtil.getFechaSinHora((Date) item.get("FECHA_INI")).compareTo(JsfUtil.getFechaSinHora(fechaLimiteDuplicado)) >= 0) {
                validar = false;
            }
        }
        return validar;
    }

    public boolean renderBtnConstancia(Map item) {
        boolean validar = true;
        if (item != null) {
                       
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE")) {
                validar = false;
            }
            
            
            if(item.get("ESTADOCURSO").equals("NO")){
                validar = false;   
            }
        }
        return validar;
    }

    public boolean renderBtnFinProcedimiento(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_TRA")) {
                validar = true;
            }
            
            /*if(item.get("ESTADOCURSO").equals("NO")){
                validar = false;   
            }*/
        }
        return validar;
    }

    public void borrarRegistro(Map item) {
        try {
            if (item != null) {
                SspRegistro reg = ejbSspRegistroFacade.find((Long) item.get("ID"));
                reg.setActivo(JsfUtil.FALSE);
                reg.getCarneId().setActivo(JsfUtil.FALSE);
                ejbSspRegistroFacade.edit(reg);

                if (reg.getComprobanteId() != null) {
                    for (SbReciboRegistro recReg : reg.getComprobanteId().getSbReciboRegistroList()) {
                        if (recReg.getActivo() == 1) {
                            recReg.setActivo(JsfUtil.FALSE);
                            break;
                        }
                    }
                    ejbSbRecibosFacade.edit(reg.getComprobanteId());
                }

                ///////// PARA REGISTROS REFERENCIADOS /////////
                if (reg.getRegistroId() != null && !reg.getTipoRegId().getCodProg().equals("TP_REGIST_EMP")) {
                    reg.getRegistroId().setActivo(JsfUtil.TRUE);
                    reg.getRegistroId().getCarneId().setActivo(JsfUtil.TRUE);
                    reg.getRegistroId().getCarneId().setFechaBaja(null);
                    reg.getRegistroId().getCarneId().setDetalleDupli(null);
                    if (reg.getTipoOpeId().getCodProg().equals("TP_OPE_REN") || reg.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
                        reg.getRegistroId().getCarneId().setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECRN_NVO"));
                    }
                    ejbSspRegistroFacade.edit(reg.getRegistroId());
                }
                ////////////////////////////////////////////////

                resultados.remove(item);
                resultadosSeleccionados.remove(item);

                JsfUtil.mensaje("Se ha borrado correctamente el registro.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    public boolean generaExpediente(SspRegistro reg, Integer usuaTramDoc, SbPersonaGt per, String estadoCurso) {
        boolean validacion = true, estadoTraza = true;
        Integer nroProceso = null;

        try {
            if (estadoCurso.equals("TP_ECC_CRE")) {
                if (registro.getNroExpediente() != null && !registro.getNroExpediente().isEmpty()) {
                    return true;
                }
                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                String asunto = ((reg.getEmpresaId().getRuc() != null) ? (((reg.getEmpresaId().getRznSocial() != null) ? reg.getEmpresaId().getRznSocial().trim() : reg.getEmpresaId().getNombresYApellidos().trim()) + " - RUC: " + reg.getEmpresaId().getRuc()) : (reg.getEmpresaId().getApePat() + " " + reg.getEmpresaId().getApeMat() + " " + reg.getEmpresaId().getNombres() + " - " + (reg.getEmpresaId().getTipoDoc() != null ? " - " + reg.getEmpresaId().getTipoDoc().getNombre() : " - DNI") + ": " + reg.getEmpresaId().getNumDoc()));
                String titulo = "";
                if (registro.getCarneId().getVigilanteId().getTipoDoc() != null) {
                    titulo = registro.getCarneId().getVigilanteId().getTipoDoc().getNombre() + ": ";
                }
                titulo += registro.getCarneId().getVigilanteId().getNumDoc() + ", " + registro.getCarneId().getVigilanteId().getNombreCompleto();
                if (registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_VIG_EVT")) {// TP_MCO_EVT
                    nroProceso = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_nroProcesoEvento").getValor());
                } else {
                    nroProceso = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_nroProceso").getValor());
                }
                registro = ejbSspRegistroFacade.buscarRegistroById(reg.getId());

                if (!validaTransmitir()) {
                    return false;
                }
                String expediente = wsTramDocController.crearExpediente_gssp(reg.getComprobanteId(), per, asunto, nroProceso, usuaTramDoc, usuaTramDoc, titulo);
                if (expediente != null) {
                    List<Integer> acciones = new ArrayList();
                    acciones.add(17);   // En Evaluación
                    registro.setNroExpediente(expediente);

                    try {
                        ///////// Para obtener usuario a derivar /////////
                        SbDerivacionCydoc derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionCyDoc(p_user.getSistema(), ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN").getId(), reg.getSedeSucamec().getId());
                        if (derivacion == null) {
                            System.err.println("***Error derivación, ID:" + registro.getId() + ", expediente: " + expediente);
                            JsfUtil.mensajeError("No se pudo encontrar a un usuario parametrizado para derivación del expediente");
                            return true;
                        }
                        /////////
                        estadoTraza = wsTramDocController.asignarExpediente(expediente, "USRWEB", derivacion.getUsuarioDestinoId().getLogin(), asunto, "", acciones, false, null);
                        if (!estadoTraza) {
                            System.err.println("***No se pudo generar la traza del expediente " + expediente + ", ID:" + registro.getId());
                            //JsfUtil.mensajeError("No se pudo generar la traza del expediente " + expediente);
                        } else {
                            usuarioArchivar = derivacion.getUsuarioDestinoId().getLogin();
                        }
                        ////////
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

    public Long obtenerDepartamento(String departamento) {
        return ejbSbDepartamentoFacade.obtenerDepartamentoByNombre(departamento).getId();
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

        if (reg.getSspRegistroEventoList() == null) {
            reg.setSspRegistroEventoList(new ArrayList());
        }
        reg.getSspRegistroEventoList().add(evento);
    }

    public boolean validacionPrevia() {
        boolean validacion = true;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");

        if (p_user.getPersona() == null) {
            validacion = false;
            JsfUtil.mensajeAdvertencia("Opción válida para personas con RUC");
        }
        if (p_user.getPersona().getRuc() == null) {
            validacion = false;
            JsfUtil.mensajeAdvertencia("Opción válida para personas con RUC");
        }

        if (!verificarModalidades(p_user.getPersona().getRuc())) {
            validacion = false;
            JsfUtil.mensajeError("Ud. no puede ingresar a esta opción porque no cuenta con modalidades disponibles");
        }

        return validacion;
    }

    public boolean validacionPostPrevia(String ruc) {
        boolean validacion = true;

        if (!verificarModalidades(ruc)) {
            validacion = false;
            JsfUtil.mensajeError("Ud. no puede ingresar a esta opción porque no cuenta con modalidades disponibles");
        }

        return validacion;
    }

    public boolean verificarModalidades(String ruc) {
        int cont = 0;
        String modalidades = "'TP_MCO_CTA','TP_MCO_ASC','TP_MCO_TEC','TP_MCO_PRO','TP_MCO_TRA','TP_MCO_VIG','TP_MCO_VIG_EVT','TP_MCO_TRA','TP_MCO_CBC'"; // 'TP_MCO_PAT','TP_MCO_SIS',

        if (tienePerfilSispe()) {
            modalidades = "'TP_MCO_ASC'";//'TP_MCO_SIS' 'TP_MCO_PAT'
            lstAreas = ejbTipoBaseFacade.listarAreasLimaOD();
        } else {
            lstAreas = ejbTipoBaseFacade.listarAreasSedesCarnesByResolucionDisca(ruc);
            //lstAreas = ejbTipoBaseFacade.listarAreasLimaOD();  //jng solo sede central
        }
        List<Map> lstModalidadResolucion = ejbCarteraClienteFacade.buscarModalidadesPermitidasEmpresa(ruc);

        if (lstModalidadResolucion != null) {
            if (esEstandar) {
                lstModalidadesActivas = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs(modalidades);

                //////// TECNOLOGIA ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("TECNOLOGIA") || modal.get("MODALIDAD").toString().contains("TECNOLOGÍA")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_TEC")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////

                //////// PROTECCION PERSONAL ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("PROTECCION PERSONAL")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_PRO")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////

                //////// TRANSPORTE DE DINERO ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("TRANSPORTE DE DINERO")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_TRA")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////

                //////// VIGILANCIA PRIVADA ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("VIGILANCIA PRIVADA")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_VIG")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////

                //////// SEGURIDAD PERSONAL ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("INDIVIDUAL DE SEGURIDAD PERSONAL")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_SIS")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////

                //////// EVENTUAL ////////
                cont = 0;
                for (TipoSeguridad ts : lstModalidadesActivas) {
                    if (ts.getCodProg().equals("TP_MCO_PRO") || ts.getCodProg().equals("TP_MCO_VIG")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_EVE")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////

                //////// SERVICIOS DE PROTECCION POR CUENTA PROPIA ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("CUENTA PROPIA")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_CTA")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////

                //////// ASESORIA Y CONSULTORÍA ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("CONSULTORIA Y ASESORIA")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_ASC")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////

                //////// SERVICIOS INDIVIDUALES DE SEGURIDAD PATRIMONIAL ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("PATRIMONIAL")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_PAT")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////
                
                //////// EVENTOS ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("EVENTOS")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_VIG_EVT")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////
                
                //////// BIENES CONTROLADOS ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("CONTROLADOS")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    for (TipoSeguridad ts : lstModalidadesActivas) {
                        if (ts.getCodProg().equals("TP_MCO_CBC")) {
                            lstModalidadesActivas.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////
            } else {
                lstModalidadesActivas = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_MCO_VIG'");

                //////// VIGILANCIA PRIVADA ////////
                cont = 0;
                for (Map modal : lstModalidadResolucion) {
                    if (modal.get("MODALIDAD").toString().contains("VIGILANCIA PRIVADA")) {
                        cont++;
                    }
                }
                if (cont == 0) {
                    lstModalidadesActivas = null;
                } else {
                    lstModalidadesActivas = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_MCO_EVT'");
                }
                ////////////////////////
            }
            if (lstModalidadesActivas != null && !lstModalidadesActivas.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean tienePerfilSispe() {
        if (JsfUtil.getLoggedUser().getPerfiles() != null && !JsfUtil.getLoggedUser().getPerfiles().isEmpty()) {
            for (String perfil : JsfUtil.getLoggedUser().getPerfiles()) {
                if (perfil.equals("SEL_SISP")) {
                    return true;
                }
            }
        }
        return false;
    }

    public String obtenerDescDondeRecoger(TipoBaseGt area) {
        String abreviatura = "";
        if (area != null) {
            abreviatura = area.getAbreviatura();
            if (area.getCodProg().equals("TP_AREA_TRAM")) {
                abreviatura = "SEDE CENTRAL";
                return area.getNombre() + " - " + abreviatura;
            }
            return abreviatura + " - " + area.getNombre();
        }
        return abreviatura;
    }

    public void cambiarTipoDoc(boolean flagLimpiar) {
        if (tipoDocSelected != null) {
            if (flagLimpiar) {
                numDocView = null;
                apePat = null;
                apeMat = null;
                nombres = null;
                tipoDoc = null;
            }
            numDoc = null;
            foto = null;
            fotoByte = null;
            file = null;
            switch (tipoDocSelected.getCodProg()) {
                case "TP_DOCID_DNI":
                    maxLength = 8;
                    break;
                case "TP_DOCID_CE":
                    maxLength = 9;
                    break;
            }
        } else {
            JsfUtil.mensajeError("Por favor seleccione un tipo de documento");
        }
    }

    public void buscarVigilante() {
        boolean validacion = true;

        if (registro.getCarneId().getModalidadId() == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":modalidad");
            JsfUtil.mensajeError("Por favor seleccionar primero la modalidad");
        }
        if (registro.getTipoRegId() == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":tipoRegistro");
            JsfUtil.mensajeError("Por favor seleccionar primero el tipo de registro");
        }
        if (tipoDocSelected == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":tipoDoc");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de documento");
        }
        if (numDoc == null || numDoc.isEmpty()) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":numDoc");
            JsfUtil.mensajeError("Por favor ingrese el número de documento");
        } else {
            numDoc = numDoc.trim();
        }
        if (validacion) {
            switch (tipoDocSelected.getCodProg()) {
                case "TP_DOCID_DNI":
                    if (numDoc.trim().length() != 8) {
                        JsfUtil.invalidar(obtenerForm() + ":numDoc");
                        JsfUtil.mensajeAdvertencia("El dni es de 8 dígitos ");
                        validacion = false;
                    }
                    break;
                case "TP_DOCID_CE":
                    if (numDoc.trim().length() != 9) {
                        JsfUtil.invalidar(obtenerForm() + ":numDoc");
                        JsfUtil.mensajeAdvertencia("El carnet de extranjería es de 9 dígitos ");
                        validacion = false;
                    }
                    break;
            }
        }
        if (validacion && tienePerfilSispe()) {
            if (!JsfUtil.getLoggedUser().getPersona().getNumDoc().equals(numDoc.trim())) {
                JsfUtil.invalidar(obtenerForm() + ":numDoc");
                JsfUtil.mensajeAdvertencia("La creación de una solicitud " + registro.getCarneId().getModalidadId().getNombre() + " es personal. No puede procesar el carné de otra persona");
                validacion = false;
            }
        }
        if (validacion) {
            setEsNuevo(false);
            setDisabledNombres(false);
            nombres = null;
            apePat = null;
            apeMat = null;
            tipoDoc = null;
            foto = null;
            fotoByte = null;
            SbPersonaGt personaVigilante = ejbSbPersonaFacade.selectPersonaActivaxTipoDocyNumDoc(tipoDocSelected.getId(), numDoc);
            if (personaVigilante != null) {
                registro.getCarneId().setVigilanteId(personaVigilante);
                ///////////// Validaciones adicionales /////////////
                setDisabledNombres(true);

                if (!validaBusquedaVigilante()) {
                    registro.getCarneId().setVigilanteId(null);
                    return;
                }
                if (registro.getCarneId().getVigilanteId().getApeMat() != null && registro.getCarneId().getVigilanteId().getApeMat().trim().toUpperCase().equals("X")) {
                    registro.getCarneId().getVigilanteId().setApeMat("");
                }
                nombres = registro.getCarneId().getVigilanteId().getNombres();
                apePat = registro.getCarneId().getVigilanteId().getApePat();
                apeMat = registro.getCarneId().getVigilanteId().getApeMat();
                fechaNac = registro.getCarneId().getVigilanteId().getFechaNac();

//                if(!validaReniec()){                    
//                    numDoc = "";
//                    nombres = "";
//                    apePat = "";
//                    apeMat = "";
//                    tipoDoc = null;
//                    fechaNac = null;
//                    foto = null;
//                    fotoByte = null;
//                }                
            } else {
                numDoc = "";
                nombres = "";
                apePat = "";
                apeMat = "";
                fechaNac = null;
                tipoDoc = null;
                foto = null;
                fotoByte = null;
                labelCursoVig = "";
                labelMsjCurso = "";
                JsfUtil.mensajeError("El documento ingresado no existe");
                setDisabledNombres(true);
            }
        }
    }

    public boolean validaReniec() {
        boolean validacion = true;

        SbPersonaGt personaVigilante = new SbPersonaGt();
        personaVigilante.setNumDoc(registro.getCarneId().getVigilanteId().getNumDoc());
        personaVigilante.setTipoDoc(registro.getCarneId().getVigilanteId().getTipoDoc());
        personaVigilante.setTipoId(registro.getCarneId().getVigilanteId().getTipoId());

        if (personaVigilante.getTipoDoc() == null) {
            if (registro.getCarneId().getVigilanteId().getNumDoc().trim().length() == 8) {
                personaVigilante.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
            } else {
                personaVigilante.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_CE"));
            }
        }

        if (personaVigilante.getTipoDoc().getCodProg().equals("TP_DOCID_CE")) {
            ResPideMigra resMigra = wsPideController.buscarCarnetExtranjeria(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_usuario_Pide").getValor(), registro.getCarneId().getVigilanteId().getNumDoc());
            if (resMigra != null) {
                if (resMigra.isRspta()) {   
                    if (resMigra.getResultado().getStrNumRespuesta().equals("0000")) {
                        registro.getCarneId().getVigilanteId().setApePat(resMigra.getResultado().getStrPrimerApellido());
                        registro.getCarneId().getVigilanteId().setApeMat(resMigra.getResultado().getStrSegundoApellido());
                        registro.getCarneId().getVigilanteId().setNombres(resMigra.getResultado().getStrNombres());
                        JsfUtil.mensaje("Datos validados con Migraciones, correctamente");
                    } else {
                        validacion = false;
                        JsfUtil.mensajeAdvertencia("No se encontró datos de la persona en Migraciones");
                    }
                } else {
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("Hubo un error consultando el servicio de Migraciones.");
                }
            } else {
                validacion = false;
                JsfUtil.mensajeAdvertencia("El servicio de Migraciones no está disponible para validar los datos.");
            }
        } else {
            personaVigilante.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
            personaVigilante = wsPideController.buscarReniec(personaVigilante);
            if (personaVigilante != null) {
                if (personaVigilante.getApePat() != null) {
                    registro.getCarneId().getVigilanteId().setApePat(personaVigilante.getApePat());
                    registro.getCarneId().getVigilanteId().setApeMat(personaVigilante.getApeMat());
                    registro.getCarneId().getVigilanteId().setNombres(personaVigilante.getNombres());
                    JsfUtil.mensaje("Datos validados con el RENIEC, correctamente");
                } else {
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("No se encontró datos de la persona en el RENIEC");
                }
            } else {
                validacion = false;
                JsfUtil.mensajeAdvertencia("El servicio del RENIEC no está disponible. Tendrá que registrar los datos manualmente");
            }
        }

        return validacion;
    }

    public boolean validaBusquedaVigilante() {
        String tipoCurso = "";

        if (registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_EVT")) {
            tipoCurso = "'TP_GSSP_CURS','TP_GSSP_CURSEVT'";
            
        } else {
            tipoCurso = "'TP_GSSP_CURS'";
            
        }
        
        for(SspAlumnoCurso curso : registro.getCarneId().getVigilanteId().getSspAlumnoCursoList())
        {
                Date hoy = new Date();
                if(curso.getFechaFin()!=null)
                {
                    if(curso.getFechaFin().compareTo(hoy) > 0 && curso.getActivo()==1)
                    {
                        tipoServicio=curso.getRegistroCursoId().getTipoServicio();
                    }
                }
        }
        if(tipoServicio!=null)
        {
            if(tipoServicio.getCodProg().equals("TP_EVE_SSP"))
            {
                labelCursoVig="Curso Vigente: Espectáculos, Certámenes y Convenciones";
            }else{
                labelCursoVig="Curso Vigente: General";
            }
        }else{ labelCursoVig="El vigilante no cuenta con curso vigente"; }
        
        
        labelMsjCurso="Solo podrá brindar servicios para los eventos señalados";

        // Validación cursos
        int total = ejbSspRegistroCursoFacade.obtenerTotalCursosVigentesXPersonaIdXTipoCurso(registro.getCarneId().getVigilanteId().getId(), tipoCurso);
        if (total == 0) {
            JsfUtil.mensajeError("El prospecto no cuenta con curso vigente ");
            return false;
        }

        if (registro.getTipoRegId().getCodProg().equals("TP_REGIST_NOR") && !registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_SIS") && !registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_PAT") && !registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_ASC")) {
            int cont = ejbSspRegistroFacade.buscarRegistroOtraEmpresaXRuc(JsfUtil.getLoggedUser().getNumDoc(), registro.getCarneId().getVigilanteId().getId(), registro.getId());
            if (cont > 0) {
                JsfUtil.mensajeError("Esta persona no puede sacar un carné de personal de seguridad con esta empresa porque ya cuenta con uno en una distinta empresa");
                return false;
            }
        }

        if (!registro.getTipoOpeId().getCodProg().equals("TP_OPE_REN")) {
            // Validación carné
            if (registro.getTipoRegId().getCodProg().equals("TP_REGIST_NOR")) {
                // Inicial
                SspRegistro registroTemp = ejbSspRegistroFacade.buscarRegistroXRucXModalidadXDocVigente(null, registro.getCarneId().getModalidadId().getId(), registro.getCarneId().getVigilanteId().getId(), registro.getId());
                if (registroTemp != null) {
                    if (registroTemp.getEstadoId().getCodProg().equals("TP_ECC_APR")) {
                        switch (registroTemp.getTipoProId().getCodProg()) {
                            case "TP_GSSP_ECRN":
                                if (JsfUtil.getFechaSinHora(registroTemp.getCarneId().getFechaFin()).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0) {
                                    JsfUtil.mensajeError("Este personal de seguridad ya cuenta con el carné nro. " + registroTemp.getCarneId().getNroCarne() + ", aprobado y vigente");
                                } else {
                                    //JsfUtil.mensajeError("Este personal de seguridad ya cuenta con el carné nro. "+registroTemp.getCarneId().getNroCarne()+", vencido. Por favor intente renovar el carné");  // rfv
                                    JsfUtil.mensajeError("Este personal de seguridad ya cuenta con el carné nro. " + registroTemp.getCarneId().getNroCarne() + ".");
                                }
                                break;
                            case "TP_GSSP_CCRN":
                                if (registroTemp.getCarneId().getEstadoId().getCodProg().equals("TP_ECRN_PTE") || registroTemp.getCarneId().getEstadoId().getCodProg().equals("TP_ECRN_DVT")) {
                                    JsfUtil.mensajeError("Este personal de seguridad cuenta con el carné cesado nro. " + registroTemp.getCarneId().getNroCarne() + ", " + registroTemp.getCarneId().getEstadoId().getNombre().toLowerCase() + ". Por favor intente renovar o cambiar de empresa");
                                } else {
                                    JsfUtil.mensajeError("Este personal de seguridad tiene el carné " + registroTemp.getCarneId().getNroCarne() + " cesado. Por favor cambie el tipo de registro a Cambio de empresa");
                                }
                                break;
                            case "TP_GSSP_DCRN":
                                if (registroTemp.getRegistroId() != null && registroTemp.getRegistroId().getTipoOpeId().getCodProg().equals("TP_REGIST_NULL")) {
                                    int cont = ejbSspRegistroFacade.contarRegistroAnteriorXRucXModalidadXDoc(null, registro.getCarneId().getModalidadId().getId(), registro.getCarneId().getVigilanteId().getId(), registroTemp.getId());
                                    if (cont == 0) {
                                        return true; // Se acepta para una nueva emisión
                                    }
                                } else {
                                    JsfUtil.mensajeError("Este personal de seguridad tiene el carné " + registroTemp.getCarneId().getNroCarne() + " cesado. Por favor cambie el tipo de registro a Cambio de empresa");
                                }
                                break;
                            default:
                                JsfUtil.mensajeError("Este personal de seguridad ya cuenta con un carné en estado: " + registroTemp.getCarneId().getEstadoId().getNombre());
                                break;
                        }
                        if (!Objects.equals(registroTemp.getEmpresaId().getId(), registro.getEmpresaId().getId())) {
                            JsfUtil.mensajeError("El registro o carné ya emitido tiene un solicitante distinto. " + ((registroTemp.getEmpresaId().getRuc() != null) ? ("RUC: " + registroTemp.getEmpresaId().getRuc()) : ("Doc:" + registroTemp.getEmpresaId().getNumDoc())));
                        }
                    } else {
                        JsfUtil.mensajeError("Este personal de seguridad cuenta con un registro en la misma modalidad en estado " + registroTemp.getEstadoId().getNombre());
                    }
                    return false;
                }
            } else {
                // Cambio de empresa TP_REGIST_EMP
                SspRegistro registroTemp = ejbSspRegistroFacade.buscarUltimoRegistroXRucXModalidadXDoc(null, registro.getCarneId().getModalidadId().getId(), registro.getCarneId().getVigilanteId().getId(), registro.getId());
                if (registroTemp == null) {
                    JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCarne_crear_msjeCambioEmpresa_1erCarne"));
                    return false;
                } else if (registroTemp.getTipoProId().getCodProg().equals("TP_GSSP_ECRN")) {
                    if (registroTemp.getEstadoId().getCodProg().equals("TP_ECC_APR")) {
                        if (registroTemp.getCarneId().getCese()==0){
                            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCarne_crear_msjeCambioEmpresa_noCese"));
                        }else{
                            return true;
                        }
                    } else {
                        JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCarne_crear_msjeCambioEmpresa_registroPendiente") + registroTemp.getEstadoId().getNombre() + " con la empresa " + registroTemp.getEmpresaId().getRznSocial());
                    }
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * EVENTO PARA CARGAR FOTO
     *
     * @author Richar Fernández
     * @version 2.0
     * @param event Evento al cargar archivo
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (event != null) {
                file = event.getFile();
                if (JsfUtil.verificarJPG(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    fotoByte = JsfUtil.escalarRotarImagen(fotoByte, 480, 640, 0);
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                    nombreFoto = file.getFileName();
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * EVENTO PARA CARGAR PDF
     *
     * @author Richar Fernández
     * @version 2.0
     * @param event Evento al cargar archivo
     */
    public void handleFileUploadPDF(FileUploadEvent event) {
        try {
            if (event != null) {
                fileDJ = event.getFile();
                if (JsfUtil.verificarPDF(fileDJ)) {
                    djByte = IOUtils.toByteArray(fileDJ.getInputstream());
                    archivoDJ = new DefaultStreamedContent(fileDJ.getInputstream(), "application/pdf");
                    archivo = fileDJ.getFileName();
                } else {
                    fileDJ = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUploadCM(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCM = event.getFile();
                if (JsfUtil.verificarPDF(fileCM)) {
                    cmByte = IOUtils.toByteArray(fileCM.getInputstream());
                    archivoCM = new DefaultStreamedContent(fileCM.getInputstream(), "application/pdf");
                    certificado = fileCM.getFileName();
                } else {
                    fileCM = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SbRecibos> autoCompleteRecibos(String query) {
        try {
            List<SbRecibos> filteredList = new ArrayList();

            if (registro.getTipoOpeId() != null) {
                HashMap mMap = new HashMap();
                if (registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
                    mMap.put("importe", importeDuplicado);
                } else {
                    mMap.put("importe", importeEmision);
                }
                mMap.put("codigo", codAtributo);
                mMap.put("registroId", registro.getId());
                mMap.put("comprobanteId", ((registro.getComprobanteId() != null) ? registro.getComprobanteId().getId() : null));

                //List<SbRecibos> lstUniv = ejbSbRecibosFacade.listarRecibosByImporteByCodAtributoGssp(registro.getEmpresaId(), mMap);
                List<SbRecibos> lstUniv = ejbSbRecibosFacade.listarRecibosByRucByDocByImporteByCodAtributo(registro.getEmpresaId(), mMap);

                for (SbRecibos x : lstUniv) {
                    if (x.getNroSecuencia().toString().contains(query)) {
                        filteredList.add(x);
                    }
                }
            } else {
                JsfUtil.mensajeError("Por favor seleccione primero el tipo de operación");
            }
            return filteredList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void lstnrSelectRecibo(SelectEvent event) {
        try {
            registro.setComprobanteId((SbRecibos) event.getObject());
            //JsfUtil.mensaje("Recibo " + registro.getComprobanteId().getNroSecuencia() + " seleccionado correctamente.");
        } catch (Exception ex) {
            JsfUtil.mensajeError(ex.getMessage());
        }
    }

    public String obtenerMsjeInvalidSizeFoto() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_sizeFoto").getValor()) / 1024) + " Kb. ";
    }

    public String obtenerMsjeInvalidSizeDJ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_sizeDj").getValor()) / 1024) + " Kb. ";
    }

    public String obtenerMsjeInvalidSizeCM() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_sizeDj").getValor()) * 2 / 1024) + " Kb. ";
    }

    public String obtenerComprobanteSeleccionado(SbRecibos recibo) {
        if (recibo == null) {
            return null;
        }
        return "Monto: S/. " + recibo.getImporte() + ", Fecha: " + JsfUtil.formatoFechaDdMmYyyy(recibo.getFechaMovimiento());
    }

    public String obtenerComprobanteSeleccionadoVer(SbRecibos recibo) {
        if (recibo == null) {
            return null;
        }
        return "Nro." + recibo.getNroSecuencia() + ", Monto: S/. " + recibo.getImporte() + ", Fecha: " + JsfUtil.formatoFechaDdMmYyyy(recibo.getFechaMovimiento());
    }

    public void cargarDatos() {
        try {
            boolean isOk;
            if (registro.getCarneId().getVigilanteId().getTipoDoc() != null) {
                tipoDocSelected = registro.getCarneId().getVigilanteId().getTipoDoc();
            } else {
                tipoDocSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI");
            }
            if (tipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {
                maxLength = 8;
            } else {
                maxLength = 9;
            }
            numDoc = registro.getCarneId().getVigilanteId().getNumDoc();
            nombres = registro.getCarneId().getVigilanteId().getNombres();
            apePat = registro.getCarneId().getVigilanteId().getApePat();
            apeMat = registro.getCarneId().getVigilanteId().getApeMat();
            fechaNac = registro.getCarneId().getVigilanteId().getFechaNac();
            //registro.getCarneId().getVigilanteId().getSspAlumnoCursoList()
            for(SspAlumnoCurso curso : registro.getCarneId().getVigilanteId().getSspAlumnoCursoList())
            {
                Date hoy = new Date();
                if(curso.getFechaFin()!=null)
                {
                    if(curso.getFechaFin().compareTo(hoy) > 0 && curso.getActivo()==1)
                    {
                        tipoServicio=curso.getRegistroCursoId().getTipoServicio();
                    }
                }
            }
            if(tipoServicio!=null)
            {
                if(tipoServicio.getCodProg().equals("TP_EVE_SSP"))
                {
                    labelCursoVig="Curso Vigente: Espectáculos, Certámenes y Convenciones";
                }else{
                    labelCursoVig="Curso Vigente: General";
                }
            }else{ labelCursoVig="El vigilante no cuenta con curso vigente"; }
        
            labelMsjCurso="Solo podrá brindar servicios para los eventos señalados";
            
            setDisabledNombres(true);

            nombreFoto = registro.getCarneId().getFotoId().getNombreFoto();
            nombreFotoTemporal = registro.getCarneId().getFotoId().getNombreFoto();
            try {
                if (nombreFoto != null && !nombreFoto.equals("S/F")) {
                    fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + nombreFoto));
                } else {
                    JsfUtil.mensajeAdvertencia("No se encontró la foto del personal de seguridad");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.mensajeAdvertencia("No se pudo cargar la foto del personal de seguridad");
            }
            if (registro.getSspRequisitoList() != null) {
                Collections.sort(registro.getSspRequisitoList(), new Comparator<SspRequisito>() {
                    @Override
                    public int compare(SspRequisito one, SspRequisito other) {
                        return other.getId().compareTo(one.getId());
                    }
                });
                isOk = false;
                for (SspRequisito requisito : registro.getSspRequisitoList()) {
                    if (requisito.getActivo() == 1 && requisito.getTipoRequisitoId().getCodProg().equals("TP_TREQ_DJ")) {
                        for (SspArchivo arch : requisito.getSspArchivoList()) {
                            if (arch.getActivo() == 1 && arch.getTipoId().getCodProg().equals("TP_TARCH_PDF")) {
                                try {
                                    if (esDuplicado) {
                                        archivoVerDuplicado = arch.getNombre();
                                    } else {
                                        archivo = arch.getNombre();
                                        archivoTemporal = arch.getNombre();
                                    }
                                    djByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + archivo));
                                    isOk = true;
                                    break;
                                } catch (Exception e) {
                                    isOk = false;
                                }
                            }
                        }
                        if (isOk) {
                            break;
                        }
                    }
                }
                //Cargar Certificado Médico
                isOk = false;
                for (SspRequisito requisito : registro.getSspRequisitoList()) {
                    if (requisito.getActivo() == 1 && requisito.getTipoRequisitoId().getCodProg().equals("TP_TREQ_CM")) {
                        for (SspArchivo arch : requisito.getSspArchivoList()) {
                            if (arch.getActivo() == 1 && arch.getTipoId().getCodProg().equals("TP_TARCH_PDF")) {
                                try {
                                    if (esDuplicado) {
                                        archivoVerDuplicado = arch.getNombre();
                                    } else {
                                        certificado = arch.getNombre();
                                        certificadoTemporal = arch.getNombre();
                                    }
                                    cmByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + certificado));
                                    isOk = true;
                                    break;
                                } catch (Exception e) {
                                    isOk = false;
                                }
                            }
                        }
                        if (isOk) {
                            break;
                        }
                    }
                }
            }
            if (!esDuplicado) {
                if (registro.getRegistroId() != null && registro.getSspRequisitoList() != null && registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
                    if (registro.getRegistroId().getCarneId().getEstadoId().getCodProg().equals("TP_ECRN_ROB") || registro.getRegistroId().getCarneId().getEstadoId().getCodProg().equals("TP_ECRN_PER")) {

                        isOk = false;
                        for (SspRequisito requisito : registro.getSspRequisitoList()) {
                            if (requisito.getActivo() == 1 && requisito.getTipoRequisitoId().getCodProg().equals("TP_TREQ_DP")) {
                                for (SspArchivo arch : requisito.getSspArchivoList()) {
                                    if (arch.getActivo() == 1) {
                                        try {
                                            archivoDenuncia = arch.getNombre();
                                            archivoTemporalDenuncia = arch.getNombre();
                                            dpByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + archivoDenuncia));
                                            renderDenunciaPolicial = true;
                                            isOk = true;
                                            break;
                                        } catch (Exception e) {
                                            isOk = false;
                                        }
                                    }
                                }
                            }
                            if (isOk) {
                                break;
                            }
                        }
                    }
                }
            }

            switch (registro.getTipoOpeId().getCodProg()) {
                case "TP_OPE_COP":
                    setRenderDatosDuplicado(true);
                    setDisabledDatosAdministrado(true);
                    setDisabledDatosAdministradoModalidad(true);
                    setDisabledDatosVigilante(true);
                    setDisabledFotoVigilante(true);
                    setDisabledDatosRequisitos(false);
                    break;
                case "TP_OPE_REN":
                    setDisabledDatosAdministrado(false);
                    setDisabledDatosAdministradoModalidad(true);
                    setDisabledDatosVigilante(true);
                    setDisabledFotoVigilante(false);
                    setDisabledDatosRequisitos(false);
                    break;
            }

            //solicitarRecibo = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXNombre("HABILITACION_TUPA").getValor());
            solicitarRecibo = true;

            if (solicitarRecibo) {
                SbProcesoTupa parametrizacionEmision = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoIdByTipoOpeCodProg("TP_TUP_SUC1", 1402, "TP_OPE_INI");
                SbProcesoTupa parametrizacionDuplicado = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoIdByTipoOpeCodProg("TP_TUP_SUC1", 1402, "TP_OPE_COP");

                importeDuplicado = 0;//parametrizacionDuplicado.getImporte();
                importeEmision = parametrizacionEmision.getImporte();
                codAtributo = parametrizacionEmision.getCodTributo();
            }
            if (registro.getComprobanteId() != null) {
                if (esDuplicado) {
                    nroSecuenciaVerDuplicado = registro.getComprobanteId().getNroSecuencia();
                } else {
                    nroSecuencia = registro.getComprobanteId().getNroSecuencia();
                }
            }

            /// Estandar o Eventos ///
            esEstandar = true;
            if (registro.getCarneId() != null && registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_EVT")) {
                esEstandar = false;
            }
            //////////////////////////

            /////////////// OBSERVACIONES ///////////
            if (lstObservaciones == null) {
                lstObservaciones = new ArrayList();
            }
            for (SspRegistroEvento ce : registro.getSspRegistroEventoList()) {
                if (ce.getActivo() == 1 && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")) {
                    lstObservaciones.add(ce);
                }
            }
            /////////////////////////////////////////

            //////////////////NOMBRE DEL CARNÉ DIGITAL///////////////////////
            switch (registro.getEstadoId().getCodProg()) {
                case "TP_ECC_APR":  //Cargar Carné Digital Firmado
                    carneDigital = registro.getId().toString() + "[R].pdf";
                    break;
                default:
                    carneDigital = null;
                    break;
            }

            verificarModalidades(JsfUtil.getLoggedUser().getPersona().getRuc());
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar los datos del registro");
        }
    }

    public StreamedContent cargarDocumento() {
        try {
            String nombreArch = "";
            if (esDuplicado) {
                nombreArch = archivoVerDuplicado;
            } else {
                nombreArch = archivo;
            }
            if (fileDJ != null) {
                return new DefaultStreamedContent(fileDJ.getInputstream(), "application/pdf", fileDJ.getFileName());
            } else {
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor(), nombreArch, nombreArch, "application/pdf");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("No se pudo encontrar el archivo");
        }
        return null;
    }

    public StreamedContent cargarCertificado() {
        try {
            String nombreArch = "";
            if (esDuplicado) {
                nombreArch = archivoVerDuplicado;
            } else {
                nombreArch = certificado;
            }
            if (fileCM != null) {
                return new DefaultStreamedContent(fileCM.getInputstream(), "application/pdf", fileCM.getFileName());
            } else {
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor(), nombreArch, nombreArch, "application/pdf");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("No se pudo encontrar el certificado");
        }
        return null;
    }

    public StreamedContent cargarDocumentoDenunciaPolicial() {
        try {
            if (fileDP != null) {
                return new DefaultStreamedContent(fileDP.getInputstream(), "application/pdf", fileDP.getFileName());
            } else {
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor(), archivoDenuncia, archivoDenuncia, "application/pdf");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("No se pudo encontrar el archivo");
        }
        return null;
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

    public Date getMaximaFechaDelDia() {
        return new Date();
    }

    public String obtenerFechaVigenciaCarne(Map item) {
        String fechaDesc = "";
        if (item != null) {
            if (item.get("FECHA_INI") != null && item.get("FECHA_FIN") != null) {
                fechaDesc = "EMISIÓN: <b>" + JsfUtil.formatoFechaDdMmYyyy((Date) item.get("FECHA_INI")) + "</b> <br/> ";
                //"VCTO.: <b>" + obtenerFechaFinCarne((Date)item.get("FECHA_FIN")) + "</b>";    // rfv - temp
            }
        }
        return fechaDesc;
    }

    public String obtenerFechaVigenciaCarneSinCaracteres(Map item) {
        String fechaDesc = "";
        if (item != null) {
            if (item.get("FECHA_INI") != null && item.get("FECHA_FIN") != null) {
                fechaDesc = "EMISIÓN: " + JsfUtil.formatoFechaDdMmYyyy((Date) item.get("FECHA_INI")) + " ";
            }
        }
        return fechaDesc;
    }
        
    public String obtenerFechaFinCarne(Date fechaFin) {
        if (fechaFin == null) {
            return "-";
        }
        return JsfUtil.dateToString(fechaFin, "dd/MM/yyyy");
    }

    public void openDlgBuscarCarne() {
        cargarDatos();
        RequestContext.getCurrentInstance().execute("PF('wvDlgVerDatosCarne').show()");
        RequestContext.getCurrentInstance().update("frmVerDatosCarne");
    }

    public void openDlgSolicitudDuplicado(Map item) {
        if (item != null) {
            registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
            if (validaDuplicado()) {
                estadoSelected = null;
                detalleDuplicado = "";
                reciboNuevo = null;
                nroSecuencia = null;
                nroSecuenciaVerDuplicado = null;
                file = null;
                djByte = null;
                cmByte = null;
                archivoDJ = null;
                archivo = null;
                certificado = null;
                archivoDenuncia = null;
                archivoVerDuplicado = null;
                foto = null;
                fotoByte = null;
                nombreFoto = null;
                nombreFotoTemporal = null;
                sedeRecojoDuplicado = registro.getSedeSucamec();
                setRenderDenunciaPolicial(false);
                if (registro.getCarneId().getFotoId() != null) {
                    nombreFoto = registro.getCarneId().getFotoId().getNombreFoto();
                    nombreFotoTemporal = registro.getCarneId().getFotoId().getNombreFoto();

                    if (registro.getCarneId().getFotoId().getNombreFoto() != null && registro.getCarneId().getFotoId().getNombreFoto().trim().toUpperCase().equals("S/F")) {
                        nombreFoto = null;
                        nombreFotoTemporal = null;
                    }
                }
                for (TipoBaseGt sede : lstAreas) {
                    if (Objects.equals(sede.getId(), registro.getSedeSucamec().getId())) {
                        sedeRecojoDuplicado = sede;
                    }
                }
                esDuplicado = true;
                solicitarRecibo=false;
                importeDuplicado = Double.parseDouble(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_Duplicado_precio").getValor());
                codAtributo = Long.parseLong(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_Emision_codigo").getValor());
                RequestContext.getCurrentInstance().execute("PF('wvDlgSolicitudDuplicado').show()");
                RequestContext.getCurrentInstance().update("frmSolicitudDuplicado");
            }
        }
    }

    public boolean validaDuplicado() {
        boolean validacion = true;
        if (!registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_SIS") && !registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_PAT") && !registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_ASC")) {    // SISPE / SISPA / CONSULTORIA 

            Long modalidadId = registro.getCarneId().getModalidadId().getId();
            if (registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_EVT")) {   // Eventos
                modalidadId = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_VIG").getId();
            }

            int contModalidad = ejbSspCarneFacade.contarModalidadVigenteByRucByModalidad(JsfUtil.getLoggedUser().getNumDoc(), modalidadId);
            if (contModalidad == 0) {
                validacion = false;
                JsfUtil.mensajeError("Ud. no tiene autorización vigente en esta modalidad");
                return validacion;
            }
        }

        if (JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(registro.getCarneId().getFechaFin())) > 0) {
            validacion = false;
            JsfUtil.mensajeError("No puede procesar un duplicado a un carné vencido");
            return validacion;
        }

        int cont = ejbSspRegistroFacade.buscarRegistroNoFinalizadoVigenteReferenciado(registro.getCarneId().getModalidadId().getId(), registro.getCarneId().getVigilanteId().getId(), registro.getId());
        if (cont > 0) {
            JsfUtil.mensajeError("No puede acceder a esta opción porque ya hay un registro pendiente modificando al carné");
            validacion = false;
        }

        lstAreas = ejbTipoBaseFacade.listarAreasSedesCarnesByResolucionDisca(JsfUtil.getLoggedUser().getPersona().getRuc());
        if (lstAreas == null || lstAreas.isEmpty()) {
            JsfUtil.mensajeError("Ud. no cuenta con al menos una modalidad vigente");
            validacion = false;
        }

        return validacion;
    }

    public String guardarDuplicado() {
        boolean validacion = true;
        String fileName = "";
        if (sedeRecojoDuplicado == null) {
            validacion = false;
            JsfUtil.invalidar("frmSolicitudDuplicado:dondeRecogerDuplicado");
            JsfUtil.mensajeError("Por favor seleccionar la sede de recojo del carné");
        }
        if (estadoSelected == null) {
            validacion = false;
            JsfUtil.invalidar("frmSolicitudDuplicado:motivo");
            JsfUtil.mensajeError("Por favor seleccionar el motivo del duplicado");
        }
        /*if (solicitarRecibo && reciboNuevo == null) {
            validacion = false;
            JsfUtil.invalidar("frmSolicitudDuplicado:nroSecuenciaDuplicado");
            JsfUtil.mensajeError("Por favor ingresar el comprobante");
        }*/
        if (registro != null) {
            if (registro.getCarneId().getFotoId() == null && foto == null) {
                validacion = false;
                JsfUtil.mensajeError("Por favor cargar una foto");
            }
            if (registro.getCarneId().getFotoId() != null && registro.getCarneId().getFotoId().getNombreFoto() != null && registro.getCarneId().getFotoId().getNombreFoto().trim().toUpperCase().equals("S/F") && foto == null) {
                validacion = false;
                JsfUtil.mensajeError("Por favor cargar una foto");
            }
        }
//        if(fileDJ == null ){
//            validacion = false;
//            JsfUtil.invalidar(obtenerForm() + ":archivoDJ");
//            JsfUtil.mensajeError("Por favor cargar la Declaración Jurada en formato PDF");
//        }
        if (validacion) {
            try {
                /////////////////// Nuevo Registro ////////////////////////
                SspPersonaFoto foto = new SspPersonaFoto();
                foto.setAlumnoId(null);
                foto.setId(null);
                foto.setFecha(new Date());
                foto.setActivo(JsfUtil.TRUE);
                foto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                foto.setAudNumIp(JsfUtil.getIpAddress());
                foto.setModuloId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN"));
                if (!Objects.equals(nombreFoto, nombreFotoTemporal)) {
                    fileName = nombreFoto.toUpperCase();
                    fileName = "FOTO_C" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_FOTOCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + fileName), fotoByte);
                } else {
                    fileName = registro.getCarneId().getFotoId().getNombreFoto();
                }
                foto.setNombreFoto(fileName);
                foto = (SspPersonaFoto) JsfUtil.entidadMayusculas(foto, "");

                SspRegistro registroDuplicado = new SspRegistro();
                registroDuplicado.setActivo(JsfUtil.TRUE);
                registroDuplicado.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registroDuplicado.setAudNumIp(JsfUtil.getIpAddress());
                registroDuplicado.setCarneId(new SspCarne());
                registroDuplicado.setComprobanteId(reciboNuevo);
                registroDuplicado.setEmpresaId(registro.getEmpresaId());
                registroDuplicado.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                registroDuplicado.setFecha(new Date());
                registroDuplicado.setFechaIni(null);
                registroDuplicado.setFechaFin(null);
                registroDuplicado.setId(null);
                registroDuplicado.setNroExpediente(null);
                registroDuplicado.setNroSolicitiud(obtenerNroSolicitudEmision());
                registroDuplicado.setObservacion(JsfUtil.bundleBDIntegrado("sspCarne_duplicado_mensajePorEmitir"));
                registroDuplicado.setRegistroId(registro);
                registroDuplicado.setRepresentanteId(null);
                registroDuplicado.setSedeSucamec(sedeRecojoDuplicado);
                registroDuplicado.setSspRegistroEventoList(new ArrayList());
                registroDuplicado.setSspRegistroList(new ArrayList());
                registroDuplicado.setSspRequisitoList(new ArrayList());
                registroDuplicado.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR"));
                registroDuplicado.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_COP"));
                registroDuplicado.setTipoProId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_ECRN"));
                registroDuplicado.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                registroDuplicado = (SspRegistro) JsfUtil.entidadMayusculas(registroDuplicado, "");
                registroDuplicado.getCarneId().setActivo(JsfUtil.TRUE);
                registroDuplicado.getCarneId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registroDuplicado.getCarneId().setAudNumIp(JsfUtil.getIpAddress());
                registroDuplicado.getCarneId().setCese(JsfUtil.FALSE);
                registroDuplicado.getCarneId().setDetalleDupli(null);
                registroDuplicado.getCarneId().setEmitida(JsfUtil.FALSE);
                registroDuplicado.getCarneId().setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECRN_NVO"));
                registroDuplicado.getCarneId().setFechaBaja(null);
                registroDuplicado.getCarneId().setFechaEmision(null);
                if (JsfUtil.getFechaSinHora(registro.getCarneId().getFechaFin()).compareTo(JsfUtil.getFechaSinHora(new Date())) > 0) {
                    registroDuplicado.getCarneId().setFechaIni(JsfUtil.getFechaSinHora(new Date()));
                } else {
                    registroDuplicado.getCarneId().setFechaIni(registro.getCarneId().getFechaIni());
                }
                registroDuplicado.getCarneId().setFechaFin(registro.getCarneId().getFechaFin());
                registroDuplicado.getCarneId().setFotoId(foto);
                registroDuplicado.getCarneId().setHashQr(null);
                registroDuplicado.getCarneId().setId(null);
                registroDuplicado.getCarneId().setModalidadId(registro.getCarneId().getModalidadId());
                registroDuplicado.getCarneId().setNroCarne(registro.getCarneId().getNroCarne());
                registroDuplicado.getCarneId().setSspRegistroList(new ArrayList());
                registroDuplicado.getCarneId().setVigilanteId(registro.getCarneId().getVigilanteId());
                registroDuplicado.setCarneId((SspCarne) JsfUtil.entidadMayusculas(registroDuplicado.getCarneId(), ""));

                /////////////////// Nuevo requisito DJ ///////////////////
                if (solicitarRecibo && fileDJ != null) {
                    fileName = archivo;
                    fileName = "DJ_" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_DJCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());

                    SspRequisito requisito = new SspRequisito();
                    requisito.setActivo(JsfUtil.TRUE);
                    requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito.setAudNumIp(JsfUtil.getIpAddress());
                    requisito.setFecha(new Date());
                    requisito.setId(null);
                    requisito.setRegistroId(registroDuplicado);
                    requisito.setSspArchivoList(new ArrayList());
                    requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TREQ_DJ"));
                    requisito.setValorRequisito(null);
                    requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");

                    SspArchivo archivo = new SspArchivo();
                    archivo.setActivo(JsfUtil.TRUE);
                    archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivo.setAudNumIp(JsfUtil.getIpAddress());
                    archivo.setId(null);
                    archivo.setNombre(fileName);
                    archivo.setRequisitoId(requisito);
                    archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                    requisito.getSspArchivoList().add(archivo);
                    registroDuplicado.getSspRequisitoList().add(requisito);

                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + archivo.getNombre()), djByte);
                }
                //////////////////////////////////////////////////////////

                /////////////////// Nuevo requisito CERTIFICADO MEDICO (CM) ///////////////////
                if (solicitarRecibo && fileDJ != null) {
                    fileName = certificado;
                    fileName = "CM_" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_CMCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());

                    SspRequisito requisito = new SspRequisito();
                    requisito.setActivo(JsfUtil.TRUE);
                    requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito.setAudNumIp(JsfUtil.getIpAddress());
                    requisito.setFecha(new Date());
                    requisito.setId(null);
                    requisito.setRegistroId(registroDuplicado);
                    requisito.setSspArchivoList(new ArrayList());
                    requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TREQ_CM"));
                    requisito.setValorRequisito(null);
                    requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");

                    SspArchivo certificado = new SspArchivo();
                    certificado.setActivo(JsfUtil.TRUE);
                    certificado.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    certificado.setAudNumIp(JsfUtil.getIpAddress());
                    certificado.setId(null);
                    certificado.setNombre(fileName);
                    certificado.setRequisitoId(requisito);
                    certificado.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    certificado = (SspArchivo) JsfUtil.entidadMayusculas(certificado, "");
                    requisito.getSspArchivoList().add(certificado);
                    registroDuplicado.getSspRequisitoList().add(requisito);

                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + certificado.getNombre()), cmByte);
                }
                //////////////////////////////////////////////////////////

                //////////// Nuevo requisito denuncia policial ////////////
                if (estadoSelected.getCodProg().equals("TP_ECRN_ROB") && fileDP != null) {
                    fileName = archivoDenuncia;
                    fileName = "DP_" + ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_DPCRN").toString() + fileName.substring(fileName.lastIndexOf('.'), fileName.length());

                    SspRequisito requisito = new SspRequisito();
                    requisito.setActivo(JsfUtil.TRUE);
                    requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito.setAudNumIp(JsfUtil.getIpAddress());
                    requisito.setFecha(new Date());
                    requisito.setId(null);
                    requisito.setRegistroId(registroDuplicado);
                    requisito.setSspArchivoList(new ArrayList());
                    requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TREQ_DP"));
                    requisito.setValorRequisito(null);
                    requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");

                    SspArchivo archivo = new SspArchivo();
                    archivo.setActivo(JsfUtil.TRUE);
                    archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivo.setAudNumIp(JsfUtil.getIpAddress());
                    archivo.setId(null);
                    archivo.setNombre(fileName);
                    archivo.setRequisitoId(requisito);
                    archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");
                    requisito.getSspArchivoList().add(archivo);
                    registroDuplicado.getSspRequisitoList().add(requisito);

                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + archivo.getNombre()), dpByte);
                }
                ////////////////////////////////////////////////////////////

                adicionaEvento(registroDuplicado, JsfUtil.bundleBDIntegrado("sspCarne_duplicado_mensajePorEmitir"));
                ejbSspRegistroFacade.create(registroDuplicado);
                ////////////////////////////////////////////////////                    

                ////////// Actualización de registro anterior //////////
                registro.getCarneId().setEstadoId(estadoSelected);
                //registro.getCarneId().setActivo(JsfUtil.FALSE);
                //registro.getCarneId().setFechaBaja(new Date());
                registro.getCarneId().setDetalleDupli(detalleDuplicado);
                ejbSspRegistroFacade.edit(registro);
                ////////////////////////////////////////////////////////////

                //////////////////////// Actualización de recibo ////////////////////////
                if (solicitarRecibo && registroDuplicado.getComprobanteId() != null) {
                    SbReciboRegistro rec = new SbReciboRegistro();
                    rec.setId(null);
                    rec.setRegistroId(null);
                    rec.setNroExpediente("S/N");    // Pendiente de actualizar al transmitir
                    rec.setReciboId(registroDuplicado.getComprobanteId());
                    rec.setActivo(JsfUtil.TRUE);
                    rec.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    rec.setAudNumIp(JsfUtil.getIpAddress());
                    if (registroDuplicado.getComprobanteId().getSbReciboRegistroList() == null) {
                        registroDuplicado.getComprobanteId().setSbReciboRegistroList(new ArrayList());
                    }
                    registroDuplicado.getComprobanteId().getSbReciboRegistroList().add(rec);
                    ejbSbRecibosFacade.edit(registroDuplicado.getComprobanteId());
                }
                ////////////////////////////////////////////////////////////////////////

                /////////////////////////////
                for (Map item : resultados) {
                    if (Objects.equals((Long) item.get("ID"), registro.getId())) {
                        item.put("ID", registroDuplicado.getId());
                        item.put("NRO_EXPEDIENTE", registroDuplicado.getNroExpediente());
                        item.put("FECHA_INI", registroDuplicado.getCarneId().getFechaIni());
                        item.put("FECHA_FIN", registroDuplicado.getCarneId().getFechaFin());
                        item.put("TIPO_OPE_CODPROG", registroDuplicado.getTipoOpeId().getCodProg());
                        item.put("TIPO_OPE_NOMBRE", registroDuplicado.getTipoOpeId().getNombre());
                        item.put("TIPO_REG_CODPROG", registroDuplicado.getTipoRegId().getCodProg());
                        item.put("TIPO_REG_NOMBRE", registroDuplicado.getTipoRegId().getNombre());
                        item.put("MODALIDAD_CODPROG", registroDuplicado.getCarneId().getModalidadId().getCodProg());
                        item.put("MODALIDAD_NOMBRE", registroDuplicado.getCarneId().getModalidadId().getNombre());
                        item.put("ESTADO_CODPROG", registroDuplicado.getEstadoId().getCodProg());
                        item.put("ESTADO_NOMBRE", registroDuplicado.getEstadoId().getNombre());
                        item.put("ESTADO_CARNE_CODPROG", registroDuplicado.getCarneId().getEstadoId().getCodProg());
                        item.put("ESTADO_CARNE_NOMBRE", registroDuplicado.getCarneId().getEstadoId().getNombre());
                    }
                }
                ////////////

                RequestContext.getCurrentInstance().execute("PF('wvDlgSolicitudDuplicado').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm());
                JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("sspCarne_duplicado_mensajeRegistro") + registroDuplicado.getId() + " con Nro. de solicitud: " + registroDuplicado.getNroSolicitiud());
                return prepareBuscar();
                ////////////////////////////////////////////////////////////////////////////////   
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.mensajeError("Hubo un error al realizar el proceso");
            }
        }
        return null;
    }

    public List<SbRecibos> autoCompleteRecibosDuplicado(String query) {
        try {
            List<SbRecibos> filteredList = new ArrayList();

            HashMap mMap = new HashMap();
            mMap.put("importe", importeDuplicado);
            mMap.put("codigo", codAtributo);

            List<SbRecibos> lstUniv = ejbSbRecibosFacade.listarRecibosByRucByDocByImporteByCodAtributo(registro.getEmpresaId(), mMap);
            for (SbRecibos x : lstUniv) {
                if (x.getNroSecuencia().toString().contains(query)) {
                    filteredList.add(x);
                }
            }
            return filteredList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String obtenerDocVigilante(Map item) {
        String doc = "";
        if (item.get("TIPO_DOC") != null) {
            doc = "<b>" + item.get("TIPO_DOC").toString() + ": </b>";
        } else {
            doc = "<b>DNI: </b>";
        }
        doc += "<i>" + item.get("NUM_DOC").toString() + "</i>";

        return doc;
    }
    
    public String obtenerDocVigilanteSinCaracteres(Map item) {
        String doc = "";
        if (item.get("TIPO_DOC") != null) {
            doc = item.get("TIPO_DOC").toString() + ": ";
        } else {
            doc = "DNI: ";
        }
        doc += item.get("NUM_DOC").toString();
        return doc;
    }

    public boolean renderEstadoCarne() {
        boolean validacion = false;
        if (registro != null && registro.getTipoOpeId() != null && registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
            validacion = true;
        }
        return validacion;
    }

    //////////////////////////////////////// CESE DE CARNÉS ////////////////////////////////////////
    public String prepareListCese() {
        reiniciarCamposBusqueda();
        resultados = null;

        if (!JsfUtil.getLoggedUser().getTipoDoc().trim().equals("RUC")) {
            JsfUtil.mensajeAdvertencia("Opción válida para personas jurídicas");
            return null;
        }
        verificarModalidades(JsfUtil.getLoggedUser().getNumDoc());
        if (ejbSspRegistroFacade.obtenerTotalRegistrosObservados(JsfUtil.getLoggedUser().getNumDoc()) > 0) {
            JsfUtil.mensajeAdvertencia("Ud. tiene algunos registros de solicitudes observadas");
        }
        procesoCodProg = "TP_GSSP_CCRN";
        estado = EstadoCrud.BANDEJACESE;
        return "/aplicacion/gssp/sspCarne/ListCese";
    }

    public void mostrarCrearCese() {
        reiniciarValores();
        resultados = null;
        estado = EstadoCrud.CESE;
        solicitarRecibo = false; //Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXNombre("HABILITACION_TUPA").getValor());
        if (solicitarRecibo) {
            paso = "paso1";
        } else {
            paso = "paso2";
        }
    }

    /**
     * ABRIR FORMULARIO DE BUSQUEDA DE COMPROBANTE
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void openDlgComprobante() {
        lstRecibosBusqueda = new ArrayList();
        filtro = null;
        tipoBusqueda = null;
        fechaBusqueda = null;
        setMuestraBusquedafecha(false);

        RequestContext.getCurrentInstance().execute("PF('wvDialogBusquedaComprobante').show()");
        RequestContext.getCurrentInstance().update("frmBusComprobante");
    }

    /**
     * CAMBIAR CAMPO FECHA DE BUSQUEDA
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiaTipoBusquedaComprobante() {
        filtro = null;
        fechaBusqueda = null;
        setMuestraBusquedafecha(false);
        if (tipoBusqueda != null) {
            if (tipoBusqueda.equals("fecha")) {
                setMuestraBusquedafecha(true);
            }
        }
    }

    /**
     * BUSCAR COMPROBANTE DE PAGO
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void buscarDlgComprobante() {
        try {
            if (tipoBusqueda != null) {
                if (!(tipoBusqueda != null && ((filtro == null || filtro.isEmpty()) && fechaBusqueda == null))) {
                    HashMap mMap = new HashMap();
                    mMap.put("tipo", tipoBusqueda);
                    mMap.put("filtro", filtro);
                    mMap.put("fecha", fechaBusqueda);
                    mMap.put("importe", Double.parseDouble(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_Cese_precio").getValor()));
                    mMap.put("codigo", Long.parseLong(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_Cese_codigo").getValor()));

                    SbPersonaGt pers = ejbSbPersonaFacade.find(JsfUtil.getLoggedUser().getPersona().getId());
                    lstRecibosBusqueda = ejbSbRecibosFacade.listarRecibosByImporteByCodAtributo(pers, mMap);
                } else {
                    JsfUtil.mensajeAdvertencia("Por favor escriba el criterio de búsqueda.");
                }
            } else {
                JsfUtil.mensajeAdvertencia("Por favor seleccione un tipo de búsqueda.");
            }
        } catch (Exception ex) {
            JsfUtil.mensajeAdvertencia("hubo un error al buscar el comprobante de pago.");
        }
    }

    /**
     * SELECCIONAR COMPROBANTE DE PAGO
     *
     * @author Richar Fernández
     * @version 1.0
     * @param recibo Recibo Seleccionado
     */
    public void seleccionarDlgComprobante(SbRecibos recibo) {
        Double valorReciboXVigilante = Double.parseDouble(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_Cese_precio").getValor());

        if (Math.round(recibo.getImporte()) > 0) {
            if (((int) (recibo.getImporte() * 100) % (int) (valorReciboXVigilante * 100)) == 0) {
                limiteReg = (int) Math.floor(recibo.getImporte() / valorReciboXVigilante);
                lstRecibos = new ArrayList();
                lstRecibos.add(recibo);
                setDisabledSiguiente(false);

                RequestContext.getCurrentInstance().execute("PF('wvDialogBusquedaComprobante').hide()");
                RequestContext.getCurrentInstance().update("ceseForm");
            } else {
                JsfUtil.mensajeAdvertencia("El monto del comprobante no es correcto.");
            }
        } else {
            JsfUtil.mensajeAdvertencia("El comprobante debe tener un importe mayor a cero.");
        }
    }

    /**
     * VALIDACION DE BOTÓN SIGUIENTE EN FORMULARIO
     *
     * @author Richar Fernández
     * @version 1.0
     * @param p Pestaña a redireccionar (Paso1,Paso2)
     */
    public void siguiente(String p) {
        boolean validacion = true;
        switch (p) {
            case "paso12":
                if (lstRecibos.isEmpty()) {
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("Por favor, ingrese un comprobante de pago");
                }
                if (validacion) {
                    paso = "paso2";
                }
                break;
            default:
                paso = p;
                break;
        }
    }

    public void buscarVigilanteCese() {
        boolean validacion = true;
        if (tipoDocSelected != null && numDoc != null) {
            if (!numDoc.trim().isEmpty()) {
                switch (tipoDocSelected.getCodProg()) {
                    case "TP_DOCID_DNI":
                        if (numDoc.trim().length() != 8) {
                            JsfUtil.invalidar(obtenerForm() + ":numDoc");
                            JsfUtil.mensajeAdvertencia("El dni es de 8 dígitos ");
                            validacion = false;
                        }
                        break;
                    case "TP_DOCID_CE":
                        if (numDoc.trim().length() != 9) {
                            JsfUtil.invalidar(obtenerForm() + ":numDoc");
                            JsfUtil.mensajeAdvertencia("El carnet de extranjería es de 9 dígitos ");
                            validacion = false;
                        }
                        break;
                }
                if (validacion) {
                    limpiarVigilante();
                    lstVigilanteBusqueda = ejbSspCarneFacade.buscarVigilanteXEmpresaXNumDoc(numDoc, JsfUtil.getLoggedUser().getPersonaId());
                    if (lstVigilanteBusqueda != null && !lstVigilanteBusqueda.isEmpty()) {
                        Map vigilante = lstVigilanteBusqueda.get(0);

                        if (!validaFechaEmision((Date) vigilante.get("FEC_EMI"))) {
                            JsfUtil.mensajeError("No puede cesar el carné porque ha sido emitido hace menos de 2 días hábiles ");
                            return;
                        }

                        llenarModalidadesVigilante();
                        numDoc = null;
                        tipoDocSelected = null;
                        setRenderDatosVigilante(true);

                        numDocView = ((lstVigilanteBusqueda.get(0).get("NUMDOC") != null) ? lstVigilanteBusqueda.get(0).get("NUMDOC").toString() : null);
                        nombres = ((lstVigilanteBusqueda.get(0).get("NOMBRES") != null) ? lstVigilanteBusqueda.get(0).get("NOMBRES").toString() : null);
                        apePat = ((lstVigilanteBusqueda.get(0).get("APAT") != null) ? lstVigilanteBusqueda.get(0).get("APAT").toString() : null);
                        apeMat = ((lstVigilanteBusqueda.get(0).get("AMAT") != null) ? lstVigilanteBusqueda.get(0).get("AMAT").toString() : null);
                        tipoDoc = ((lstVigilanteBusqueda.get(0).get("TIPODOC") != null) ? lstVigilanteBusqueda.get(0).get("TIPODOC").toString() : null);
                        if (lstVigilanteBusqueda.size() == 1) {
                            modalidadSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodProg(vigilante.get("TIP_MOD").toString());
                            cambiaModalidad();
                        } else if (lstVigilanteBusqueda.size() > 1) {
                            JsfUtil.mensajeAdvertencia("Por favor seleccione una modalidad");
                        }
                    } else {
                        setRenderDatosVigilante(true);
                        JsfUtil.mensajeAdvertencia("No se encontró al personal de seguridad según los datos ingresados");
                    }
                }
            } else {
                JsfUtil.mensajeError("Por favor ingresar el número de documento para realizar la búsqueda");
            }
        } else {
            JsfUtil.mensajeError("Por favor ingresar el tipo y número de documento para realizar la búsqueda");
        }
    }

    public boolean validaFechaEmision(Date fechaEmision) {
        Date fechaLimite = ejbSspCarneFacade.calcularFechaLimite(fechaEmision, 2);
        if (JsfUtil.getFechaSinHora(fechaLimite).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0) {
            return false;
        }
        return true;
    }

    public void llenarModalidadesVigilante() {
        lstModalidadesVig = new ArrayList();
        for (Map vig : lstVigilanteBusqueda) {
            TipoSeguridad mod = ejbTipoSeguridadFacade.tipoSeguridadXCodProg(vig.get("TIP_MOD").toString());
            if (mod != null) {
                if (!lstModalidadesVig.contains(mod)) {
                    lstModalidadesVig.add(mod);
                }
            }
        }
    }

    /**
     * LIMPIAR REGISTRO
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void limpiarVigilante() {
        carneSelected = null;
        estadoCese = null;
        modalidadSelected = null;
        sedeSelected = null;
        numDocView = "";
        nombres = "";
        apePat = "";
        apeMat = "";
        tipoDoc = "";
        labelCursoVig = "";
        labelMsjCurso = "";
        lstVigilanteBusqueda = new ArrayList();
        lstModalidadesVig = new ArrayList();
        setRenderDatosVigilante(false);
    }

    public void cambiaModalidad() {
        if (modalidadSelected != null) {
            for (Map vigilante : lstVigilanteBusqueda) {
                if (vigilante.get("TIP_MOD").toString().equals(modalidadSelected.getCodProg())) {
                    SspRegistro regTemp = ejbSspRegistroFacade.buscarRegistroById(Long.parseLong(vigilante.get("ID").toString()));
                    if (regTemp != null) {
                        carneSelected = regTemp.getCarneId();
                        Calendar c_hoy = Calendar.getInstance();
                        if (JsfUtil.getFechaSinHora(c_hoy.getTime()).compareTo(JsfUtil.getFechaSinHora(carneSelected.getFechaFin())) > 0) {
                            estadoCese = "VENCIDA";
                        } else {
                            estadoCese = "VIGENTE";
                        }
                        if (carneSelected.getNroCarne() == 0 || carneSelected.getFechaIni() == null || carneSelected.getFechaFin() == null) {
                            setRenderDatosVigilante(false);
                            JsfUtil.mensajeError("El personal de seguridad no cuenta con todos los datos necesarios .");
                        } else {
                            setRenderDatosVigilante(true);
                        }
                        break;
                    }
                }
            }
        } else {
            JsfUtil.mensajeError("Por favor seleccione la modalidad del personal de seguridad.");
        }
    }

    /**
     * VALIDAR Y ADICIONAR VIGILANTE A TABLA DE VIGILANTES A CESAR
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void agregarVigilanteCese() {
        int cont = 0;
        //if(limiteReg != lstVigilante.size()){
        if (modalidadSelected != null && carneSelected != null) {
            for (Map actual : lstVigilanteBusqueda) {
                if (actual.get("NRO_CRN_VIG").toString().equals("" + carneSelected.getNroCarne())) {
                    for (Map vigilante : lstVigilante) {
                        if (vigilante.get("NRO_CRN_VIG").toString().equals("" + carneSelected.getNroCarne()) && vigilante.get("TIP_MOD").toString().equals(modalidadSelected.getCodProg())) {
                            cont++;
                        }
                    }
                    if (cont == 0) {
                        int contPendiente = ejbSspRegistroFacade.buscarRegistroNoFinalizadoVigenteReferenciado(carneSelected.getModalidadId().getId(), carneSelected.getVigilanteId().getId(), obtenerRegistroEmisionCarne(carneSelected.getSspRegistroList()));
                        if (contPendiente > 0) {
                            JsfUtil.mensajeError("No puede cesar a esta persona porque ya hay un registro pendiente modificando al carné");
                            return;
                        }

                        Map temp = new HashMap();
                        temp.put("ID", actual.get("ID"));
                        temp.put("TIPODOC", actual.get("TIPODOC"));
                        temp.put("NUMDOC", actual.get("NUMDOC"));
                        temp.put("APAT", actual.get("APAT"));
                        temp.put("AMAT", actual.get("AMAT"));
                        temp.put("NOMBRES", actual.get("NOMBRES"));
                        temp.put("NRO_CRN_VIG", actual.get("NRO_CRN_VIG"));
                        temp.put("TIP_MOD", actual.get("TIP_MOD"));
                        temp.put("DES_MOD", actual.get("DES_MOD"));
                        temp.put("FEC_EMI", actual.get("FEC_EMI"));
                        temp.put("FEC_VENC", actual.get("FEC_VENC"));
                        temp.put("estadoCese", estadoCese);
                        temp.put("tipoDoc_desc", ((carneSelected.getVigilanteId().getTipoDoc() != null) ? carneSelected.getVigilanteId().getTipoDoc().getNombre() : "-"));
                        temp.put("numDoc_desc", carneSelected.getVigilanteId().getNumDoc());
                        temp.put("nombreYApellidos", carneSelected.getVigilanteId().getNombresYApellidos());
                        temp.put("fecEmision", carneSelected.getFechaIni());
                        lstVigilante.add(temp);
                        numDoc = null;
                        limpiarVigilante();
                    } else {
                        JsfUtil.mensajeError("Vigilante ya ha sido ingresado.");
                    }
                    break;
                }
            }
        } else {
            JsfUtil.mensajeError("Por favor seleccione la modalidad del personal de seguridad.");
        }
        //}else{
        //    JsfUtil.mensajeError("Ya no puede adicionar más personal según el importe del comprobante ingresado.");
        //}
    }

    public Long obtenerRegistroEmisionCarne(List<SspRegistro> listado) {
        if (listado == null) {
            return null;
        }
        for (SspRegistro item : listado) {
            if (item.getTipoProId().getCodProg().equals("TP_GSSP_ECRN")) {
                return item.getId();
            }
        }
        return null;
    }

    /**
     * BORRAR REGISTRO SELECCIONADO DE TABLA DE PERSONAS A CESAR
     *
     * @param reg Registro Map de tabla de Vigilantes
     * @author Richar Fernández
     * @version 1.0
     */
    public void borrarFilaCese(Map reg) {
        for (Map vigilante : lstVigilante) {
            if (vigilante.get("NRO_CRN_VIG").toString().equals(reg.get("NRO_CRN_VIG").toString()) && vigilante.get("TIP_MOD").toString().equals(reg.get("TIP_MOD").toString())) {
                lstVigilante.remove(vigilante);
                JsfUtil.mensaje("Registro eliminado correctamente");
                break;
            }
        }
    }

    /**
     * VALIDACION DE DATOS PREVIO A GUARDAR PROCESO
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void validacionCese() {
        boolean validacion = true;
        msjeCese = "";

        if (lstVigilante.isEmpty()) {
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar al menos un personal de seguridad a cesar.");
        }

        if (validacion) {
            if (solicitarRecibo && (limiteReg - lstVigilante.size()) > 0) {
                validacion = false;
                JsfUtil.mensajeError("Aún existe " + (limiteReg - lstVigilante.size()) + " cupo(s) para realizar el cese. Por favor completar el(los) cupo(s).");
            } else {
                //msjeCese = msjeCese + "¿Está seguro que desea realizar el cese?";
                msjeCese = msjeCese + "¿Está seguro que desea realizar el cese\n de " +  lstVigilante.size() + " Carné(s)?";
                RequestContext.getCurrentInstance().execute("PF('confirmCese').show()");
                RequestContext.getCurrentInstance().update("msjeForm");
            }
        }
    }

    public String crearCese() {
        List<String> listaCesados = new ArrayList<>();
        SbRecibos reciboSelected = null;
        boolean valida;

        try {
            if (validaCrearCese()) {
                System.err.println("Cese iniciado: " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss") + ", user: " + JsfUtil.getLoggedUser().getNumDoc());

                if (solicitarRecibo) {
                    reciboSelected = lstRecibos.get(0);
                    reciboSelected.setSbReciboRegistroList(new ArrayList());
                }
                String nroSolicitudCese = obtenerNroSolicitudCese();

                //Ya no se generara solicitud de Devolución
                //Seleccionar Numero de Solicitud 
                //String nroSolicitudDev = obtenerNroSolicitudDevolucion();
                //PENDIENTE DE DEVOLUCIÓN
                //TipoSeguridad estadoPendiente = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECRN_PTE");

                //Ahora lo Cesa con el mismo estado del carne como NUEVO  TP_ECRN_NVO
                TipoSeguridad estadoPendiente = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECRN_NVO");
                
                for (Map vigilante : lstVigilante) {
                    registro = ejbSspRegistroFacade.buscarRegistroById(Long.parseLong(vigilante.get("ID").toString()));
                    registro.getCarneId().setActivo(JsfUtil.FALSE);
                    registro.getCarneId().setCese(JsfUtil.TRUE);
                    registro.getCarneId().setFechaBaja(new Date());
                    registro.getCarneId().setEstadoId(estadoPendiente);
                    //Crea el primer Registro en SSP_REGISTRO como solicitud de cese
                    valida = nuevoRegistro("TP_GSSP_CCRN", "POR CESE", reciboSelected, nroSolicitudCese, null);
                    if (!valida) {
                        JsfUtil.mensajeError("No se pudo procesar el carné " + registro.getCarneId().getNroCarne());
                        System.err.println("ID: " + registro.getId() + " error cese, fecha: " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
                        continue;
                    }
                    System.err.println("ID: " + registro.getId() + ", fecha: " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
                    ejbSspCarneFacade.edit(registro.getCarneId());
                    //Se comenta el segundo registo de solicitud de Devolución de Cese
                    //porque todos los carnes vigentes son digitales y no son fisicos
                    /*registro = ejbSspRegistroFacade.buscarRegistroById(Long.parseLong(vigilante.get("ID").toString()));
                    for (SspRegistro regCesado : registro.getCarneId().getSspRegistroList()) {
                        if (regCesado.getActivo() == 1 && regCesado.getTipoProId().getCodProg().equals("TP_GSSP_CCRN")) {
                            nuevoRegistro("TP_GSSP_DCRN", "POR DEVOLUCIÓN", reciboSelected, nroSolicitudDev, regCesado);
                            ejbSspCarneFacade.edit(registro.getCarneId());
                            break;
                        }
                    }*/
                    guardarListadoCesado(listaCesados, vigilante);
                }
                if (solicitarRecibo) {
                    ejbSbRecibosFacade.edit(reciboSelected);
                }
                System.err.println("Fin cese :" + JsfUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss") + ", user: " + JsfUtil.getLoggedUser().getNumDoc());
                if (!listaCesados.isEmpty()) {    // Si hay vigilantes cesados
                    enviarNotificacionCesados(listaCesados);
                }
                JsfUtil.mensaje("Cese realizado correctamente con nro. de solicitud: " + nroSolicitudCese);

                RequestContext.getCurrentInstance().execute("PF('confirmCese').hide()");
                return prepareListCese();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void enviarNotificacionCesados(List<String> listaCesados) {
        List<String> emails = new ArrayList();
        List<String> procesados = new ArrayList();
        int nroCarnesProcesar = 0;

        if (JsfUtil.getLoggedUser() != null && JsfUtil.getLoggedUser().getCorreo() != null) {
            emails.add(JsfUtil.getLoggedUser().getCorreo());
        }
        if (!emails.isEmpty()) {
            enviarCorreo(emails);
        }
        for (int i = 0; i <= (listaCesados.size() / 20); i++) {
            if (i == (listaCesados.size() / 20)) {
                nroCarnesProcesar = listaCesados.size() % 20;
            } else {
                nroCarnesProcesar = 20;
            }
            if (nroCarnesProcesar > 0) {
                procesados = listaCesados.subList((i * 20), ((i * 20) + nroCarnesProcesar));
            }
            if (!registro.getNroExpediente().equals("S/N")) {
                enviarNotificacion(nroCarnesProcesar, procesados);
            }
        }
    }

    public void enviarCorreo(List<String> emails) {
        String msje = JsfUtil.bundleBDIntegrado("sspCarne_notificacionCorreo_msjeCese");
        for (String correo : emails) {
            JsfUtil.enviarCorreo(correo, JsfUtil.bundleBDIntegrado("sspCarne_notificacionCorreo_asuntoCese"), msje);
        }
    }

    public Boolean enviarNotificacion(int nroCarnesDevueltos, List<String> procesados) {
        try {
            String usrNotif = ejbSbParametroFacade.obtenerParametroXNombre("usuario_notifica_gssp").getValor();
            Integer usuaTramDoc = ejbSbUsuarioFacadeGt.obtenerIdUsuarioTramDoc(usrNotif);
            SbUsuario user = ejbSbUsuarioFacade.find(JsfUtil.getLoggedUser().getId());
            String asunto = "", referencia = "";

            NeDocumento neDocumento = new NeDocumento();
            neDocumento.setId(null);
            neDocumento.setActivo(JsfUtil.TRUE);
            neDocumento.setAnoExp(0);
            neDocumento.setNroExp(0);
            neDocumento.setSecExp(0);
            neDocumento.setNroExpediente(null);

            referencia = "Devolución de " + nroCarnesDevueltos + " carné(s) de personal de seguridad privada";
            asunto = "Carné(s) a devolver con nro.: " + procesados.toString();
            neDocumento.setAsunto(asunto);
            neDocumento.setAreaId(ejbSbTipoFacade.tipoPorCodProg("TP_AREA_GSSP"));
            neDocumento.setFechaDoc(new Date());
            neDocumento.setEstadoId(ejbSbTipoFacade.tipoPorCodProg("TP_EV_ENV"));
            ///
            Integer procesoNotifica = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("proceso_notifica_gssp").getValor());
            String numResWs = wsTramDocController.crearResolucion(registro.getNroExpediente(), procesoNotifica, usuaTramDoc, JsfUtil.formatoFechaDdMmYyyy(new Date()), "NOTIFICACIONVIRTUAL-CARNES");
            if (numResWs != null && !"S/N".equalsIgnoreCase(numResWs)) {
                neDocumento.setNroDoc(numResWs);
            } else {
                JsfUtil.mensajeError("El servicio de notificación no se encuentra disponible.");
                return Boolean.FALSE;
            }
            //
            SbTipo estCre = ejbSbTipoFacade.tipoPorCodProg("TP_EST_CRE");
            SbTipo evCre = null;
            neDocumento.setNeEventoList(new ArrayList());
            for (int i = 0; i < 3; i++) {
                switch (i) {
                    case 0:
                        evCre = ejbSbTipoFacade.tipoPorCodProg("TP_EV_CRE");
                        break;
                    case 1:
                        //evCre = ejbSbTipoFacade.tipoPorCodProg("TP_EV_FIR");
                        break;
                    case 2:
                        evCre = ejbSbTipoFacade.tipoPorCodProg("TP_EV_ENV");
                        break;
                }
                // Grabar Traza //
                NeEvento e = new NeEvento(new Date(), JsfUtil.TRUE, neDocumento,
                        estCre, evCre, user, JsfUtil.getLoggedUser().getLogin(), JsfUtil.getIpAddress());
                neDocumento.getNeEventoList().add(e);
            }
            neDocumento.setPersonaId(ejbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId()));
            neDocumento.setReferencia(referencia);
            neDocumento.setTipoId(ejbSbTipoFacade.tipoPorCodProg("TP_DOC_NOTVIR"));
            neDocumento.setUsuarioId(user);
            neDocumento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            neDocumento.setAudNumIp(JsfUtil.getIpAddress());
            ejbNeDocumentoFacade.create(neDocumento);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    public void guardarListadoCesado(List<String> listaCesados, Map item) {
        listaCesados.add(item.get("NRO_CRN_VIG").toString());
    }

    public boolean nuevoRegistro(String procesoCodProg, String observacion, SbRecibos recibo, String nroSolicitud, SspRegistro registroId) {
        boolean validacion = true;
        SbPersonaGt persona = ejbSbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());

        SspRegistro registroDevuelto = new SspRegistro();
        registroDevuelto.setActivo(JsfUtil.TRUE);
        registroDevuelto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registroDevuelto.setAudNumIp(JsfUtil.getIpAddress());
        registroDevuelto.setCarneId(registro.getCarneId());
        registroDevuelto.setComprobanteId(recibo);
        registroDevuelto.setEmpresaId(persona);
        registroDevuelto.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_APR"));
        registroDevuelto.setFecha(new Date());
        registroDevuelto.setFechaIni(null);
        registroDevuelto.setFechaFin(null);
        registroDevuelto.setId(null);
        if (procesoCodProg.equals("TP_GSSP_CCRN")) {
            String expediente = crearExpediente(registro.getCarneId().getVigilanteId().getNumDoc(), registro.getCarneId().getVigilanteId().getNombresYApellidos(), recibo, "" + registro.getCarneId().getNroCarne(), registro.getCarneId().getModalidadId().getNombre());
            if (expediente == null) {
                expediente = "";
            }
            if (expediente.trim().isEmpty()) {
                validacion = false;
                JsfUtil.mensajeError("Hubo un error al crear el expediente para el cese del carné: " + registro.getCarneId().getNroCarne());
                System.err.println("Error al crear el expediente de carné: " + registro.getCarneId().getNroCarne());
                return validacion;
            } else {
                registroDevuelto.setNroExpediente(expediente);
                if (solicitarRecibo && recibo != null) {
                    SbReciboRegistro rec = new SbReciboRegistro();
                    rec.setId(null);
                    rec.setRegistroId(null);
                    rec.setNroExpediente(expediente);
                    rec.setReciboId(recibo);
                    rec.setActivo(JsfUtil.TRUE);

                    if (recibo.getSbReciboRegistroList() == null) {
                        recibo.setSbReciboRegistroList(new ArrayList());
                    }
                    recibo.getSbReciboRegistroList().add(rec);
                }
            }
            registroDevuelto.setRegistroId(registro);
        } else {
            registroDevuelto.setNroExpediente("S/N");
            registroDevuelto.setRegistroId(registroId);
        }
        registroDevuelto.setNroSolicitiud(nroSolicitud);
        registroDevuelto.setObservacion(observacion);
        registroDevuelto.setRepresentanteId(null);
        registroDevuelto.setSedeSucamec(registro.getSedeSucamec());
        registroDevuelto.setSspRegistroEventoList(new ArrayList());
        registroDevuelto.setSspRegistroList(new ArrayList());
        registroDevuelto.setSspRequisitoList(new ArrayList());
        registroDevuelto.setTipoRegId(registro.getTipoRegId());
        registroDevuelto.setTipoOpeId(registro.getTipoOpeId());
        registroDevuelto.setTipoProId(ejbTipoBaseFacade.buscarTipoBaseXCodProg(procesoCodProg));
        registroDevuelto.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
        registroDevuelto = (SspRegistro) JsfUtil.entidadMayusculas(registroDevuelto, "");

        if (registro.getCarneId().getSspRegistroList() == null) {
            registro.getCarneId().setSspRegistroList(new ArrayList());
        }
        registro.getCarneId().getSspRegistroList().add(registroDevuelto);
        adicionaEvento(registroDevuelto, observacion);

        return validacion;
    }

    public boolean validaCrearCese() {
        boolean validacion = true;

        if (solicitarRecibo && (lstRecibos == null || lstRecibos.isEmpty())) {
            validacion = false;
            JsfUtil.mensajeError("No se encontró el recibo");
        }

        return validacion;
    }

    /**
     * FUNCIÓN PARA CREAR EXPEDIENTE DE CESADO
     *
     * @param numDoc Número de documento de vigilante
     * @param nombres Nombres y Apellidos de vigilante
     * @param recibo recibo
     * @param nroCarne Nro. de carné
     * @param modalidad Modalidad
     * @return Nro. de expediente creado
     * @author Richar Fernández
     * @version 1.0
     */
    public String crearExpediente(String numDoc, String nombres, SbRecibos recibo, String nroCarne, String modalidad) {
        List<Integer> acciones = new ArrayList();
        boolean estadoTraza = true;
        String nroExpediente = null, usuario = "";
        acciones.add(30);   // Finalizado
        String asunto = numDoc + " " + nombres;
        Integer nroProceso = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_nroProcesoCese").getValor());
        Integer usuaTramDoc = ejbSbUsuarioFacadeGt.obtenerIdUsuarioTramDoc("USRWEB");
        if (usuaTramDoc != null) {
            SbPersonaGt admin = ejbSbPersonaFacade.find(JsfUtil.getLoggedUser().getPersonaId());
            nroExpediente = wsTramDocController.crearExpediente_capacitacion(recibo, admin, asunto, nroProceso, usuaTramDoc, usuaTramDoc);

            if (nroExpediente != null) {
                SbDerivacionCydoc derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionCyDoc(JsfUtil.getLoggedUser().getSistema(), ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_CCRN").getId(), ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AREA_TRAM").getId());
                if (derivacion == null) {
                    usuario = JsfUtil.bundleBDIntegrado("sspCarne_derivacionCydocCese_contingencia"); // En caso no exista ningun usuario parametrizado
                } else {
                    usuario = derivacion.getUsuarioDestinoId().getLogin();
                }
                if (solicitarRecibo) {
                    asunto = "S/." + JsfUtil.bundleBDIntegrado("GsspSolicitudCese_recibo_precio") + " de S/." + recibo.getImporte() + ", nro. de carné: " + nroCarne + ", modalidad:" + modalidad;
                } else {
                    asunto = "Nro. de carné: " + nroCarne + ", modalidad:" + modalidad;
                }
                System.err.println("Generar traza: " + nroExpediente + ", usuario: " + usuario + ", asunto: " + asunto);
                estadoTraza = wsTramDocController.asignarExpediente(nroExpediente, "USRWEB", usuario, asunto, "", acciones, false, null);
                if (!estadoTraza) {
                    System.err.println("Error traza, expediente: " + nroExpediente + ", usuario: " + usuario + ", asunto: " + asunto);
                    JsfUtil.mensajeError("No se pudo generar la traza del expediente. ID exp. : " + nroExpediente);
                } else {
                    estadoTraza = wsTramDocController.archivarExpediente(nroExpediente, usuario, "");
                    if (!estadoTraza) {
                        System.err.println("Error traza, expediente: " + nroExpediente + ", usuario: " + usuario + ", asunto: " + asunto);
                        JsfUtil.mensajeError(" No se pudo archivar el expediente. ID exp. : " + nroExpediente);
                    }
                }
            }
        } else {
            JsfUtil.mensajeError("No se encontró el usuario USRWEB");
        }

        return nroExpediente;
    }

    /**
     * FUNCION PARA GENERACION DE REPORTE DE RESOLUCION
     *
     * @author Richar Fernández
     * @version 1.0
     * @param item Registro de Solicitud de Cese para crear el reporte
     * @return Reporte
     */
    public StreamedContent reporteSolicitudCese(Map item) {
        try {
            if (item != null) {
                if (item.get("NRO_SOLICITUD").toString().equals("S/N")) {
                    JsfUtil.mensajeAdvertencia("El registro seleccionado no tiene solicitud de cese");
                    return null;
                }

                List<SspRegistro> lp = new ArrayList();
                List<SspRegistro> listado = ejbSspRegistroFacade.buscarCarnesByNroSolicitudCese(item.get("NRO_SOLICITUD").toString());
                if (!listado.isEmpty()) {
                    lp.add(listado.get(0));
                    return JsfUtil.generarReportePdf("/aplicacion/gssp/sspCarne/reportes/solicitudCese.jasper", lp, parametrosPdfSolicitudCese(listado), "solicitudCese.pdf", null, null);
                }
            }
            return null;
        } catch (Exception ex) {
            return JsfUtil.errorDescarga("Error: SspSolicitudCeseController.reporte :", ex);
        }
    }

    /**
     * PARAMETROS PARA GENERAR PDF
     *
     * @author Richar Fernández
     * @version 1.0
     * @param ppdf Registro de solicitud de cese para crear el reporte
     * @return Listado de parámetros para reporte
     */
    private HashMap parametrosPdfSolicitudCese(List<SspRegistro> listado) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();

        ph.put("p_dir_emp", ReportUtil.mostrarDireccionPersonaComercio(listado.get(0).getEmpresaId()));
        ph.put("P_FECHASTRING", ReportUtil.mostrarFechaString(listado.get(0).getCarneId().getFechaBaja()));
        ph.put("P_TIPONRODOC", ReportUtil.mostrarTipoYNroDocumemtoString(listado.get(0).getEmpresaId()));
        ph.put("P_EMPRESASTRING", ReportUtil.mostrarClienteString(listado.get(0).getEmpresaId()));
        ph.put("P_logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        ph.put("P_TABLA_VIGILANTE", obtenerListadoVigilantes(listado));
        ph.put("NRO_SOLICITUD", listado.get(0).getNroSolicitiud());
        ph.put("CANTIDAD_CESE", "" + listado.size());
        if (listado.get(0).getComprobanteId() != null) {
            ph.put("P_CODTRIBUTO", String.valueOf(listado.get(0).getComprobanteId().getCodTributo()));
            ph.put("P_MONTO", String.valueOf(new DecimalFormat("###,###,###,##0.00").getInstance(Locale.ENGLISH).format(listado.get(0).getComprobanteId().getImporte())));
            ph.put("P_NROSECUENCIA", String.valueOf(listado.get(0).getComprobanteId().getNroSecuencia()));
            ph.put("P_FECHAPAGO", ReportUtil.formatoFechaDdMmYyyy(listado.get(0).getComprobanteId().getFechaMovimiento()));
            ph.put("P_CODOFICINA", String.valueOf(listado.get(0).getComprobanteId().getCodOficina()));
        } else {
            ph.put("P_CODTRIBUTO", "-");
            ph.put("P_MONTO", "-");
            ph.put("P_NROSECUENCIA", "-");
            ph.put("P_FECHAPAGO", "-");
            ph.put("P_CODOFICINA", "-");
        }

        return ph;
    }

    /**
     * OBTENER LISTADO DE VIGILANTES PARA REPORTE
     *
     * @param registros Registro de Solicitud de Cese
     * @return Listado Map con campos a mostrar en el reporte
     * @author Richar Fernández
     * @version 1.0
     */
    public List<Map> obtenerListadoVigilantes(List<SspRegistro> registros) {
        List<Map> listado = new ArrayList();

        for (SspRegistro det : registros) {
            Map vigilante = new HashMap();
            vigilante.put("nroExpe", det.getNroExpediente());
            vigilante.put("fecExpe", ejbExpedienteFacade.mostrarFechaExpedienteByNumero(det.getNroExpediente()));
            vigilante.put("nroDocumento", det.getCarneId().getVigilanteId().getNumDoc());
            vigilante.put("nombreVigilante", det.getCarneId().getVigilanteId().getApellidosyNombres());
            vigilante.put("nroCarne", "" + det.getCarneId().getNroCarne());
            vigilante.put("modalidad", det.getCarneId().getModalidadId().getNombre());
            vigilante.put("fecCese", det.getFecha());

            listado.add(vigilante);
        }

        return listado;
    }

    /**
     * ABRIR FORMULARIO DE LISTADO DE INSCRITOS
     *
     * @author Richar Fernández
     * @version 1.0
     * @param item Registro
     */
    public void openDlgListadoDevolucionCarne(Map item) {
        if (item != null) {
            if (item.get("NRO_SOLICITUD").toString().equals("S/N")) {
                JsfUtil.mensajeAdvertencia("El registro seleccionado no tiene solicitud de cese");
                return;
            }

            List<SspRegistro> listado = ejbSspRegistroFacade.buscarCarnesByNroSolicitudCese(item.get("NRO_SOLICITUD").toString());
            lstInscritos = new ArrayList();
            if (listado != null && !listado.isEmpty()) {
                for (SspRegistro reg : listado) {
                    Map vigilante = new HashMap();
                    vigilante.put("nroExpe", reg.getNroExpediente());
                    vigilante.put("nroCarne", "" + reg.getCarneId().getNroCarne());
                    vigilante.put("numDoc", reg.getCarneId().getVigilanteId().getNumDoc());
                    vigilante.put("nombreVigilante", reg.getCarneId().getVigilanteId().getNombresYApellidos());
                    vigilante.put("modalidad", reg.getCarneId().getModalidadId().getNombre());
                    vigilante.put("fecCese", reg.getCarneId().getFechaBaja());
                    SspRegistro regDevolucion = obtenerRegistroDevolucion(reg.getCarneId());
                    if (regDevolucion != null && !regDevolucion.getNroExpediente().equals("S/N")) {
                        vigilante.put("fecDevolucion", regDevolucion.getFecha());
                        vigilante.put("expeDevolucion", regDevolucion.getNroExpediente());
                        vigilante.put("estado", "Entregado");
                    } else {
                        vigilante.put("fecDevolucion", "");
                        vigilante.put("expeDevolucion", "");
                        vigilante.put("estado", "Pendiente");
                    }
                    vigilante.put("denunciaPolicial", "");

                    lstInscritos.add(vigilante);
                }

                RequestContext.getCurrentInstance().execute("PF('wvDialogListadoInscritos').show()");
                RequestContext.getCurrentInstance().update("frmInscritos");
            } else {
                JsfUtil.mensajeAdvertencia("No se encontraron registros");
            }
        }
    }

    public SspRegistro obtenerRegistroDevolucion(SspCarne carneReg) {
        if (carneReg.getSspRegistroList() != null) {
            for (SspRegistro item : carneReg.getSspRegistroList()) {
                if (item.getActivo() == 1 && item.getTipoProId().getCodProg().equals("TP_GSSP_DCRN")) {
                    return item;
                }
            }
        }
        return null;
    }

    //////////////////////////////////////// DEVOLUCION DE CARNÉS ////////////////////////////////////////
    public String prepareListDevolucion() {
        reiniciarCamposBusqueda();
        resultados = null;

        if (!JsfUtil.getLoggedUser().getTipoDoc().trim().equals("RUC")) {
            JsfUtil.mensajeAdvertencia("Opción válida para personas jurídicas");
            return null;
        }
        verificarModalidades(JsfUtil.getLoggedUser().getNumDoc());
        if (ejbSspRegistroFacade.obtenerTotalRegistrosObservados(JsfUtil.getLoggedUser().getNumDoc()) > 0) {
            JsfUtil.mensajeAdvertencia("Ud. tiene algunos registros de solicitudes observadas");
        }
        procesoCodProg = "TP_GSSP_DCRN";
        estado = EstadoCrud.BANDEJADEV;
        return "/aplicacion/gssp/sspCarne/ListDevolucion";
    }

    public Date getCurrentDate() {
        return new Date();
    }

    public void mostrarCrearDevolucion() {
        reiniciarValores();
        resultados = null;
        paso = "paso1";
        estado = EstadoCrud.CREAR;
    }

    public String obtenerOnClick(Map item) {
        String desc = "";
        if (item != null) {
            if (!item.get("NRO_SOLICITUD").toString().equals("S/N")) {
                desc = "this.form.target = '_blank'";
            }
        }
        return desc;
    }

    public String obtenerOnBlur(Map item) {
        String desc = "";
        if (item != null) {
            if (!item.get("NRO_SOLICITUD").toString().equals("S/N")) {
                desc = "this.form.target = '_self'";
            }
        }
        return desc;
    }

    public String obtenerMsjeFoto() {
        return JsfUtil.bundleBDIntegrado("sspCarne_crear_msjeFoto") + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_sizeFoto").getValor()) / 1024) + " Kb. ";
    }

    public String obtenerMsjeSizeFile() {
        return JsfUtil.bundleBDIntegrado("sspCarne_crear_msjeFoto") + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_sizeDj").getValor()) / 1024) + " Kb. ";
    }

    public String obtenerMsjeSizeCertificado() {
        return JsfUtil.bundleBDIntegrado("sspCarne_crear_msjeFoto") + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_sizeCM").getValor()) / 1024) + " Kb. ";
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void handleFileUploadDenuniciaPDF(FileUploadEvent event) {
        try {
            if (event != null) {
                fileDP = event.getFile();
                if (JsfUtil.verificarPDF(fileDP)) {
                    dpByte = IOUtils.toByteArray(fileDP.getInputstream());
                    archivoDP = new DefaultStreamedContent(fileDP.getInputstream(), "application/pdf");
                    archivoDenuncia = fileDP.getFileName();
                } else {
                    fileDP = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cambiarEstadoDuplicado(TipoSeguridad estadoSeleccionado) {
        if (estadoSeleccionado != null) {
            setRenderDenunciaPolicial(false);
            if (estadoSeleccionado.getCodProg().equals("TP_ECRN_ROB") || estadoSeleccionado.getCodProg().equals("TP_ECRN_PER")) {
                setRenderDenunciaPolicial(true);
            } else {
                dpByte = null;
                archivoDenuncia = null;
                archivoDP = null;
            }
        } else {
            JsfUtil.mensajeError("Por favor seleccione un estado");
        }
    }

    public void cambiarModalidad() {
        if (registro.getCarneId().getModalidadId() != null) {
            if (registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_SIS") || registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_PAT") || registro.getCarneId().getModalidadId().getCodProg().equals("TP_MCO_ASC")) {
                if (JsfUtil.getLoggedUser().getTipoDoc().equals("RUC") && !JsfUtil.getLoggedUser().getNumDoc().startsWith("10") && !JsfUtil.getLoggedUser().getNumDoc().startsWith("15") && !JsfUtil.getLoggedUser().getNumDoc().startsWith("17")) {
                    registro.getCarneId().setModalidadId(null);
                    JsfUtil.mensajeError("Ud. no puede seleccionar esta modalidad porque solo es permitida a personas naturales.");
                    return;
                }
            }
        } else {
            JsfUtil.mensajeError("Por favor seleccione una modalidad");
        }
    }

    public void validaSede() {
        if (registro.getSedeSucamec() != null) {
            if (registro.getSedeSucamec().getId() != 76) {
                //registro.setSedeSucamec(null); //Comentado hasta habilitar carne firma digital
                //JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCarne_create_validaSede")); //Comentado hasta habilitar carne firma digital
            }
        } else {
            JsfUtil.mensajeError("Por favor seleccione una sede");
        }
    }

    public boolean renderFotoDuplicado() {
        boolean validacion = false;
        if (registro != null) {
            if (registro.getCarneId().getFotoId() == null) {
                return true;
            }
            if (registro.getCarneId().getFotoId().getNombreFoto() != null && registro.getCarneId().getFotoId().getNombreFoto().equals("S/F")) {
                return true;
            }
        }
        return validacion;
    }

    public StreamedContent downloadFormatoDJ() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/xls/DJ_PersonaJuridicaYNatural.xls");
        return new DefaultStreamedContent(stream, "application/vnd.ms-excel", "DJ_PersonaJuridicaYNatural.xls");
    }

    public void buscarRecibosCarne() {
        try {

            if (registro.getTipoOpeId() != null) {
                if (registro.getComprobanteId() != null && nroSecuencia != null && Objects.equals(registro.getComprobanteId().getNroSecuencia(), nroSecuencia)) {
                    JsfUtil.mensaje("Recibo encontrado");
                    return;
                }
                

                HashMap mMap = new HashMap();
                if (registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
                    mMap.put("importe", importeDuplicado);
                } else {
                    mMap.put("importe", importeEmision);
                }
                
                if (registro.getTipoOpeId().getCodProg().equals("TP_OPE_REN")) {
                    SbProcesoTupa parametrizacionRenovacion = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoIdByTipoOpeCodProg("TP_TUP_SUC1", 1427, "TP_OPE_REN");

                    importeEmision = parametrizacionRenovacion.getImporte();
                    codAtributo = parametrizacionRenovacion.getCodTributo();
                    mMap.put("importe", importeEmision);
                }
                
                mMap.put("codigo", codAtributo);
                mMap.put("nroSecuencia", nroSecuencia);
                mMap.put("registroId", registro.getId());
                mMap.put("comprobanteId", ((registro.getComprobanteId() != null) ? registro.getComprobanteId().getId() : null));

                SbRecibos rcb = ejbSbRecibosFacade.buscarReciboByRucByDocByImporteByCodAtributoBySecuencia(registro.getEmpresaId(), mMap);
                if (rcb == null) {
                    nroSecuencia = null;
                    JsfUtil.mensajeError("No se encontró el recibo");
                } else {
                    JsfUtil.mensaje("Recibo encontrado");
                }
                registro.setComprobanteId(rcb);
            } else {
                JsfUtil.mensajeError("Por favor, seleccione primero el tipo de operación");
            }
        } catch (Exception e) {
            e.printStackTrace();
            registro.setComprobanteId(null);
        }
    }

    public void buscarRecibosCarneDuplicado() {
        try {
            HashMap mMap = new HashMap();
            mMap.put("importe", importeDuplicado);
            mMap.put("codigo", codAtributo);
            mMap.put("nroSecuencia", nroSecuencia);
            mMap.put("registroId", registro.getId());
            mMap.put("comprobanteId", ((reciboNuevo != null) ? reciboNuevo.getId() : null));

            SbRecibos rcb = ejbSbRecibosFacade.buscarReciboByRucByDocByImporteByCodAtributoBySecuencia(registro.getEmpresaId(), mMap);
            if (rcb == null) {
                nroSecuencia = null;
                JsfUtil.mensajeError("No se encontró el recibo");
            } else {
                JsfUtil.mensaje("Recibo encontrado");
            }
            reciboNuevo = rcb;
        } catch (Exception e) {
            e.printStackTrace();
            registro.setComprobanteId(null);
        }
    }

    public StreamedContent imprimirConstancia(Map reg) {
        try {
            registro = ejbSspRegistroFacade.buscarRegistroById((Long) reg.get("ID"));
            List<SspRegistro> lp = new ArrayList();
            lp.add(registro);

            String nombreArch = "CONSTANCIA_" + registro.getId();
            String ruta = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor();
            if (JsfUtil.buscarPdfRepositorio(nombreArch, ruta)) {
                return JsfUtil.obtenerArchivoBDIntegrado(ruta, nombreArch + ".pdf", nombreArch + ".pdf", "application/pdf");
            } else {
                JsfUtil.subirPdfSel(nombreArch, parametrosPdf(registro), lp, "/aplicacion/gssp/sspCarne/reportes/constanciaRegistroCarne.jasper", ruta);
                return JsfUtil.obtenerArchivoBDIntegrado(ruta, nombreArch + ".pdf", nombreArch + ".pdf", "application/pdf");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap parametrosPdf(SspRegistro ppdf) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        Date fechaTransmision = obtenerFechaTransmision(ppdf);

        ph.put("P_FECHASTRING", JsfUtil.dateToString(fechaTransmision, "dd/MM/yyyy"));
        ph.put("P_HORA", JsfUtil.dateToString(fechaTransmision, "hh:mm:ss a"));
        ph.put("P_EXPEDIENTE", ppdf.getNroExpediente());
        ph.put("P_USUARIO", ppdf.getUsuarioCreacionId().getLogin());
        ph.put("P_LISTA_DATOS", listarDatosConstancia(ppdf));
        ph.put("P_LISTA_DOCUMENTOS", listarDocumentosConstancia(ppdf, fechaTransmision));
        ph.put("P_HASH_QR", ppdf.getCarneId().getHashQr());
        ph.put("P_QR", "https://www.sucamec.gob.pe/sel/faces/qr/constanciaCarneGssp.xhtml?h=" + ppdf.getCarneId().getHashQr());

        return ph;
    }

    public Date obtenerFechaTransmision(SspRegistro ppdf) {
        if (ppdf == null) {
            return (new Date());
        }
        if (ppdf.getSspRegistroEventoList() != null) {
            Collections.sort(ppdf.getSspRegistroEventoList(), new Comparator<SspRegistroEvento>() {
                @Override
                public int compare(SspRegistroEvento one, SspRegistroEvento other) {
                    return one.getId().compareTo(other.getId());
                }
            });
            for (SspRegistroEvento evento : ppdf.getSspRegistroEventoList()) {
                if (evento.getActivo() == 0) {
                    continue;
                }
                if (evento.getTipoEventoId().getCodProg().equals("TP_ECC_TRA")) {
                    return evento.getFecha();
                }
            }
        }
        return ppdf.getFecha();
    }

    public List<Map> listarDatosConstancia(SspRegistro registroId) {
        List<Map> listado = new ArrayList();
        Map nmap;
        String nombres = "";
        Expediente expediente = ejbExpedienteFacade.obtenerExpedienteXNumero(registroId.getNroExpediente());

        nmap = new HashMap();
        nmap.put("nombre", "Título: ");
        nmap.put("valor", expediente.getTitulo());
        listado.add(nmap);

        nmap = new HashMap();
        nmap.put("nombre", "Proceso: ");
        nmap.put("valor", expediente.getIdProceso().getNombre());
        listado.add(nmap);

        nmap = new HashMap();
        nmap.put("nombre", "Administrado: ");
        if (registroId.getEmpresaId().getRznSocial() != null) {
            nombres = registroId.getEmpresaId().getRznSocial() + ", RUC: " + registroId.getEmpresaId().getRuc();
        } else {
            nombres = registroId.getEmpresaId().getNombreCompleto() + ", DNI: " + registroId.getEmpresaId().getNumDoc();
        }
        nmap.put("valor", nombres);
        listado.add(nmap);

        return listado;
    }

    public List<Map> listarDocumentosConstancia(SspRegistro registroId, Date fechaTransmision) {
        List<Map> listado = new ArrayList();
        Map nmap;

        for (SspRequisito requisito : registroId.getSspRequisitoList()) {
            if (requisito.getActivo() == 0) {
                continue;
            }
            if (requisito.getTipoRequisitoId().getCodProg().equals("TP_TREQ_JNE") || requisito.getTipoRequisitoId().getCodProg().equals("TP_TREQ_PJ")) {
                continue;
            }
            nmap = new HashMap();
            nmap.put("nombre", "DOCUMENTO:");
            nmap.put("descripcion", requisito.getTipoRequisitoId().getNombre());
            nmap.put("textoFecha", "FECHA:");
            if (JsfUtil.getFechaSinHora(fechaTransmision).compareTo(JsfUtil.getFechaSinHora(requisito.getFecha())) >= 0) {
                nmap.put("fecha", JsfUtil.dateToString(fechaTransmision, "dd/MM/yyyy"));
            } else {
                nmap.put("fecha", JsfUtil.dateToString(requisito.getFecha(), "dd/MM/yyyy"));
            }

            listado.add(nmap);
        }
        if (registroId.getComprobanteId() != null) {
            nmap = new HashMap();
            nmap.put("nombre", "VOUCHER:");
            nmap.put("descripcion", "NRO. " + registroId.getComprobanteId().getNroSecuencia() + ", MONTO: S/." + registroId.getComprobanteId().getImporte());
            nmap.put("textoFecha", "FECHA:");
            nmap.put("fecha", JsfUtil.dateToString(registroId.getComprobanteId().getFechaMovimiento(), "dd/MM/yyyy"));
            listado.add(nmap);
        }
        return listado;
    }

    public void openDlgAyuda(int imagen) {
        tipoImagenAyuda = imagen;
        RequestContext.getCurrentInstance().execute("PF('wvDialogAyuda').show()");
        RequestContext.getCurrentInstance().update("frmAyuda");
    }

    public String getTipoImagen() {
        String imagen = "";
        if (tipoImagenAyuda == null) {
            return imagen;
        }
        switch (tipoImagenAyuda) {
            case 1:
                imagen = "ayuda_cese.png";
                break;
            case 2:
                imagen = "ayuda_carneGssp.jpg";
                break;
        }
        return imagen;
    }

    public void archivarExpediente(String nroExpediente, String usuario) {
        boolean estadoTraza = wsTramDocController.archivarExpediente(nroExpediente, usuario, "");
    }

    public void confirmarDesestimiento() {
        try {
            if (observacion == null || observacion.trim().isEmpty()) {
                JsfUtil.mensajeError("Por favor ingrese el motivo");
                return;
            }
            TipoSeguridad tipo = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_DES");

            if (generaTraza(tipo)) {
                registro.setEstadoId(tipo);
                adicionaEvento(registro.getEstadoId(), observacion);
                ejbSspRegistroFacade.edit(registro);

                ///////// PARA REGISTROS REFERENCIADOS /////////
                if (registro.getRegistroId() != null && !registro.getTipoRegId().getCodProg().equals("TP_REGIST_EMP")) {
                    registro.getRegistroId().setActivo(JsfUtil.TRUE);
                    registro.getRegistroId().getCarneId().setActivo(JsfUtil.TRUE);
                    registro.getRegistroId().getCarneId().setFechaBaja(null);
                    registro.getRegistroId().getCarneId().setDetalleDupli(null);
                    if (registro.getTipoOpeId().getCodProg().equals("TP_OPE_REN") || registro.getTipoOpeId().getCodProg().equals("TP_OPE_COP")) {
                        registro.getRegistroId().getCarneId().setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECRN_NVO"));
                    }
                    ejbSspRegistroFacade.edit(registro.getRegistroId());
                }
                ////////////////////////////////////////////////

                reiniciarCamposBusqueda();
                resultados = new ArrayList();

                JsfUtil.mensaje("Registro actualizado correctamente.");
                RequestContext.getCurrentInstance().execute("PF('wvConfirmarDesestimiento').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm());
            } else {
                JsfUtil.mensajeError("Hubo un error al archivar el expediente");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    public void adicionaEvento(TipoSeguridad estado, String observacion) {
        SspRegistroEvento evento = new SspRegistroEvento();
        evento.setId(null);
        evento.setActivo(JsfUtil.TRUE);
        evento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        evento.setAudNumIp(JsfUtil.getIpAddress());
        evento.setRegistroId(registro);
        evento.setFecha(new Date());
        evento.setObservacion(observacion);
        evento.setTipoEventoId(estado);
        evento.setUserId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
        registro.getSspRegistroEventoList().add(evento);
    }

    public boolean generaTraza(TipoSeguridad nuevoEstadoCurso) {
        boolean validacion = true;
        String loginUsuarioAct = "", asunto = "";

        try {
            /////////////////////////////////////////////////////////////////////
            if (nuevoEstadoCurso.getCodProg().equals("TP_ECC_DES")) {
                loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(registro.getNroExpediente());
                asunto = nuevoEstadoCurso.getNombre() + " A SOLICITUD DEL ADMINISTRADO. " + observacion;
                //ARCHIVAMIENTO DEL EXPEDIENTE                
                boolean estadoNE = wsTramDocController.archivarExpediente(registro.getNroExpediente(), loginUsuarioAct, asunto);
                if (!estadoNE) {
                    JsfUtil.mensajeError("Ocurrió un error al archivar el expediente " + registro.getNroExpediente());
                }
            }
            /////////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            e.printStackTrace();
            validacion = false;
            JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro Código: " + registro.getId());
        }

        return validacion;
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

    ///////////////////
    public String prepareListImpresion() {
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        reiniciarCamposBusqueda();
        reiniciarValores();

        if (p_user.getPersona() == null) {
            JsfUtil.mensajeAdvertencia("Opción válida para personas con RUC");
            return null;
        }
        if (!tienePerfilSispe()) {
            if (p_user.getPersona().getRuc() == null) {
                JsfUtil.mensajeAdvertencia("Opción válida para personas con RUC");
                return null;
            }
        }

        verificarModalidades(p_user.getPersona().getRuc());
        fechaLimiteDuplicado = JsfUtil.stringToDate(ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_fechaCorteDuplicado").getValor(), "dd/MM/yyyy");
        pathFondo = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathGssp").getValor();
        pathFirma = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathFirmas").getValor();
        linkCarne = ejbSbParametroFacade.obtenerParametroXNombre("sspCarne_linkQr_carneElectronico").getValor();
        estado = EstadoCrud.IMPRIMIR;
        return "/aplicacion/gssp/sspCarne/ListImpresion";
    }

    public void buscarBandejaImpresion() {
        boolean validacion = true;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");

        if (buscarPor == null && fechaInicio == null && fechaFinal == null && (filtro == null || filtro.isEmpty()) && sedeSelected == null) {
            JsfUtil.mensajeAdvertencia("Por favor ingrese un campo de búsqueda");
            return;
        }
        if (buscarPor != null && buscarPor.equals("5") && modalidadSelected == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":modalidades");
            JsfUtil.mensajeAdvertencia("Por favor seleccionar una modalidad");
            return;
        }
        if (buscarPor != null && !buscarPor.equals("5") && (filtro == null || filtro.isEmpty())) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":filtroBusqueda");
            JsfUtil.mensajeAdvertencia("Es necesario ingresar un tipo de búsqueda");
        }
        if (fechaInicio != null && fechaFinal == null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":fechaFin");
            JsfUtil.mensajeAdvertencia("Por favor ingresar la fecha fin de búsqueda");
        }
        if (fechaInicio == null && fechaFinal != null) {
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":fechaInicio");
            JsfUtil.mensajeAdvertencia("Por favor ingresar la fecha inicio de búsqueda");
        }
        if (fechaInicio != null && fechaFinal != null) {
            if (JsfUtil.getFechaSinHora(fechaInicio).compareTo(JsfUtil.getFechaSinHora(fechaFinal)) > 0) {
                validacion = false;
                JsfUtil.invalidar(obtenerForm() + ":fechaInicio");
                JsfUtil.invalidar(obtenerForm() + ":fechaFin");
                JsfUtil.mensajeAdvertencia("La fecha de inicio no puede ser mayor que la fecha fin");
            }
            Calendar c_ffin = Calendar.getInstance();
            c_ffin.setTime(fechaInicio);
            c_ffin.add(Calendar.MONTH, 1);

            if (JsfUtil.getFechaSinHora(fechaFinal).compareTo(JsfUtil.getFechaSinHora(c_ffin.getTime())) > 0) {
                JsfUtil.mensajeAdvertencia("No puede seleccionar un rango de fechas que sea mayor a un mes");
                return;
            }
        }
        if (filtro != null) {
            filtro = filtro.trim().toUpperCase();
        }
        if (buscarPor != null) {
            buscarPor = buscarPor.trim().toUpperCase();
        }
        if (!validacion) {
            return;
        }
        SbPersonaGt administ = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        HashMap mMap = new HashMap();
        mMap.put("buscarPor", buscarPor);
        mMap.put("filtro", filtro);
        mMap.put("fechaIni", fechaInicio);
        mMap.put("fechaFin", fechaFinal);
        mMap.put("administrado", administ.getId());
        mMap.put("modalidad", ((modalidadSelected != null) ? modalidadSelected.getId() : null));
        mMap.put("sede", ((sedeSelected != null) ? sedeSelected.getId() : null));
        mMap.put("fechaLimiteDuplicado", fechaLimiteDuplicado);
        totalCarnesImpresion = 0;
        totalCarnesCambioEmpresa = 0;
        totalCarnesDuplicados = 0;
        totalCarnesIniciales = 0;
        totalCarnesRenovados = 0;
        resultados = ejbSspCarneFacade.buscarBandejaImpresion(mMap);
        analizarResultadosBandeja();
        reiniciarCamposBusqueda();
    }

    public void analizarResultadosBandeja() {
        if (resultados == null) {
            return;
        }
        totalCarnesImpresion = resultados.size();
        for (Map item : resultados) {
            if (item.get("TIPO_REG_CODPROG").toString().equals("TP_REGIST_NOR") && item.get("TIPO_OPE_CODPROG").toString().equals("TP_OPE_INI")) {
                totalCarnesIniciales++;
            }
            if (item.get("TIPO_OPE_CODPROG").toString().equals("TP_OPE_COP")) {
                totalCarnesDuplicados++;
            }
            if (item.get("TIPO_OPE_CODPROG").toString().equals("TP_OPE_REN")) {
                totalCarnesRenovados++;
            }
            if (item.get("TIPO_REG_CODPROG").toString().equals("TP_REGIST_EMP")) {
                totalCarnesCambioEmpresa++;
            }
        }
    }

    public StreamedContent generarPDFImpresion(Map item) {
        try {
            List<Map> lstResults = new ArrayList();
            if (item == null) {
                lstResults = resultados;
            } else {
                lstResults.add(item);
            }
            String pathFoto = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor();
            return JsfUtil.generaPdfCarneGssp(lstResults, pathFoto, pathFirma, pathFondo, linkCarne);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TipoSeguridad> getListadoModalidadesBandejaImpresion() {
        return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_MCO_ASC','TP_MCO_PAT','TP_MCO_TEC','TP_MCO_PRO','TP_MCO_TRA','TP_MCO_VIG','TP_MCO_SIS'");
    }

    public void openDlgEstadisticasImpresion() {
        pieEstadistica = new PieChartModel();
        construirGraficoPie();
        RequestContext.getCurrentInstance().execute("PF('wvEstadisticas').show()");
        RequestContext.getCurrentInstance().update("estadisticasForm");
    }

    public void construirGraficoPie() {
        pieEstadistica.set("Carnés iniciales para impresión", totalCarnesIniciales);
        pieEstadistica.set("Duplicados de carnés para impresión", totalCarnesDuplicados);
        pieEstadistica.set("Carnés por cambio de empresa para impresión", totalCarnesCambioEmpresa);
        pieEstadistica.set("Carnés renovados para impresión", totalCarnesRenovados);

        pieEstadistica.setTitle("Estadísticas de resultados de carné para impresión");
        pieEstadistica.setLegendPosition("e");
        pieEstadistica.setShowDataLabels(true);
    }

    public String obtenerColorTipoCarne(Map item) {
        if (item == null) {
            return null;
        }
        if (item.get("TIPO_OPE_CODPROG").toString().equals("TP_OPE_COP")) {
            return "carne-duplicado";
        }
        if (item.get("TIPO_OPE_CODPROG").toString().equals("TP_OPE_REN")) {
            return "carne-renovado";
        }
        if (item.get("TIPO_REG_CODPROG").toString().equals("TP_REGIST_NOR")) {
            return "carne-inicial";
        }
        if (item.get("TIPO_REG_CODPROG").toString().equals("TP_REGIST_EMP")) {
            return "carne-cambioEmpresa";
        }
        return null;
    }

    /////////////////// Movil ///////////////////
    public void buscarCarnesMovil() {
        if (!validacionBusquedaCarnesMovil()) {
            return;
        }
        setAccionEjecutada(true);
        itemSelected = ejbSspCarneFacade.buscarBandejaMovil(filtro, filtroNroCarne);
        if (pathCarneDigital.isEmpty()) {
            pathCarneDigital = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGsspDigital").getValor();
        }
        if (pathCacheCarneDigital.isEmpty()) {
            pathCacheCarneDigital = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_optCacheCarneDigital").getValor();
        }
        if (pathMagic.isEmpty()) {
            pathMagic = ejbSbParametroFacade.obtenerParametroXNombre("PathImageMagick").getValor();
        }
    }

    public StreamedContent descargarCarnesMovil() {
        try {
            String nombreArchivo = itemSelected.get("ID") + ".pdf";
            //InputStream stream = Download(pathCarneDigital, itemSelected.get("ID_CARNE") + ".pdf");
            //InputStream stream = new FileInputStream(pathCarneDigital + itemSelected.get("ID_CARNE") + ".pdf");

            //return new DefaultStreamedContent(stream, "application/pdf", "" + itemSelected.get("ID_CARNE") + ".pdf");
            return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGsspDigital").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
        } catch (IOException ex) {
            JsfUtil.errorDescarga("Error: sspCarneController :", ex);
        } catch (Exception ex) {
            Logger.getLogger(SspCarneController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public InputStream Download(String path, String fileName) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        try {
            InputStream in = new FileInputStream(path + fileName);
            //InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(pathAppDownload + fileName);
            //file = new DefaultStreamedContent(stream,"application/pdf", fileName);

            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "inline; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.

            BufferedInputStream input = null;
            BufferedOutputStream output = null;

            try {
                input = new BufferedInputStream(in);
                output = new BufferedOutputStream(response.getOutputStream());

                byte[] buffer = new byte[10240];
                for (int length; (length = input.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }
                return in;
            } catch (Exception e) {
            } finally {
                output.close();
                input.close();
            }
            facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.

        } catch (Exception e) {
            JsfUtil.showRedirectHtmlError("No existe el archivo: <font color=\"red\">" + fileName + "</font> en la ruta especificada, debe revisar.");
        }
        return null;

    }

    public boolean validacionBusquedaCarnesMovil() {
        if (filtro == null) {
            JsfUtil.mensajeError("Por favor ingrese el documento a buscar");
            return false;
        }
        if (filtro.trim().isEmpty()) {
            JsfUtil.mensajeError("Por favor ingrese el documento a buscar");
            return false;
        }
        if (filtroNroCarne == null) {
            JsfUtil.mensajeError("Por favor ingrese el nro. de carné a buscar");
            return false;
        }
        if (filtroNroCarne.trim().isEmpty()) {
            JsfUtil.mensajeError("Por favor ingrese el nro. de carné a buscar");
            return false;
        }
        //if (filtroFecNac == null) {
        //    JsfUtil.mensajeError("Por favor ingrese la fecha de nacimiento");
        //    return false;
        //}
        Map item = ejbSspCarneFacade.buscarBandejaMovil(filtro, filtroNroCarne);
        if (item == null) {
            JsfUtil.mensajeError("No se encontró registro con los datos ingresados");
            return false;
        } else {
            String nroCarne = "" + item.get("NRO_CARNE");
            if (!nroCarne.equals(filtroNroCarne) //|| !validarFechaNacimiento(filtroFecNac)
                    ) {
                JsfUtil.mensajeError("No se encontró registro con los datos ingresados");
                return false;
            }
        }
        return true;
    }

    public boolean validarFechaNacimiento(Date fecNac) {
        String usuarioMQ = ejbSbParametroFacade.obtenerParametroXCodProg("TP_USR_2USRWSCE").getValor();
        RespuestaMq respuestaMq = wsPideController.buscarReniecMQ(usuarioMQ, filtro);

        if (respuestaMq == null) {
            JsfUtil.mensajeError("Hubo un error al consultar al servicio de la RENIEC");
            return false;
        }

        if (!respuestaMq.isRspta()) {
            JsfUtil.mensajeError(respuestaMq.getMensaje());
            return false;
        }
        if (!respuestaMq.getRespuestaDatos().getCodigoError().equals("0000")) {
            JsfUtil.mensajeError("Hubo un error al consultar al servicio de la RENIEC");
            return false;
        }
        String mqFechaStr = respuestaMq.getRespuestaDatos().getFechaNaci();
        mqFechaStr = mqFechaStr.substring(6, 8) + "/" + mqFechaStr.substring(4, 6) + "/" + mqFechaStr.substring(0, 4);
        Date mqFecha = JsfUtil.stringToDate(mqFechaStr, "dd/MM/yyyy");
        if (!Objects.equals(JsfUtil.getFechaSinHoraddmmyy(mqFecha), JsfUtil.getFechaSinHoraddmmyy(filtroFecNac))) {
            return false;
        }
        return true;
    }

    public StreamedContent generarPDFCarne() {
        try {
            if (itemSelected == null) {
                return null;
            }
            generarArchivosCarneDigital(itemSelected.get("ID").toString());
            byte[] byteCarne = FileUtils.readFileToByteArray(new File(pathCacheCarneDigital + itemSelected.get("ID") + ".png"));
            return new DefaultStreamedContent(new ByteArrayInputStream(byteCarne), "application/pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public StreamedContent generarPDFCarneFront() {
        try {
            if (itemSelected == null) {
                return null;
            }
            generarArchivosCarneDigital(itemSelected.get("ID") + "_front");
            byte[] byteCarne = FileUtils.readFileToByteArray(new File(pathCacheCarneDigital + itemSelected.get("ID") + "_front.png"));
            return new DefaultStreamedContent(new ByteArrayInputStream(byteCarne), "application/pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public StreamedContent generarPDFCarneBack() {
        try {
            if (itemSelected == null) {
                return null;
            }
            generarArchivosCarneDigital(itemSelected.get("ID") + "_back");
            byte[] byteCarne = FileUtils.readFileToByteArray(new File(pathCacheCarneDigital + itemSelected.get("ID") + "_back.png"));
            return new DefaultStreamedContent(new ByteArrayInputStream(byteCarne), "application/pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void generarArchivosCarneDigital(String id) throws Exception {
        String arch = id + ".pdf", img = id + ".png";
        File f = new File(pathCarneDigital + arch);

        if (f.exists() && convertirPdfEnPngColor(pathCarneDigital + arch, pathCacheCarneDigital + img, f.lastModified())) {
            File dir = new File(pathCacheCarneDigital);
        }
    }

    public boolean convertirPdfEnPngColor(String pdf, String png, long ultimaMod) {
        try {
            // Verificar Archivos de una pagina //
            File aPng = new File(png);
            if (aPng.exists() && aPng.lastModified() > ultimaMod) {
                return true;
            }
            // Verificar Archivos de varias paginas //
            aPng = new File(png.replace(".png", "-0.png"));
            if (aPng.exists() && aPng.lastModified() > ultimaMod) {
                return true;
            }
            ConvertCmd cmd = new ConvertCmd();
            cmd.setSearchPath(pathMagic);
            IMOperation op = new IMOperation();
            // El orden importa para que las operaciones funcionen OK.
            op.density(200, 200); // Resolucion 115 dpi para vertical y 82 para horizontal
            op.gravity("NorthWest"); // Posición inicia arriba izquierda
            op.alpha("Opaque"); // Que la transparencia este ok
            op.background("#FFFFFF"); // Evitar fondos negros o transparentes
            op.fill("#FFFFFF"); // Evitar fondos negros o transparentes
            op.transparentColor("#FFFFFF"); // Evitar fondos negros o transparentes
            op.addImage(pdf); // Archivo pdf de origen 1ro
            op.depth(5); // Colores 2^4 = 16 colores.
            op.quality(0d); // Maxima compresion en png
            //op.type("Grayscale"); // Que sea escala de grises puro
            op.addImage(png); // Archivo png de destino 2do
            cmd.run(op);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String obtenerVigenciaCarne() {
        if (itemSelected == null) {
            return null;
        }
        if (itemSelected.get("TIPO_OPE_CODPROG").toString().equals("TP_REGIST_NULL")) {
            return EstadoCarneGssp.ANULADO.toString();
        }
        if (itemSelected.get("CESE").toString().equals("1")) {
            return EstadoCarneGssp.CESADO.toString();
        }
        if (itemSelected.get("ACTIVO").toString().equals("0")) {
            return EstadoCarneGssp.INACTIVO.toString();
        }
        //if (JsfUtil.getFechaSinHora((Date) itemSelected.get("FECHA_FIN")).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0) {
        //    return EstadoCarneGssp.VIGENTE.toString();
        //}
        if (JsfUtil.getFechaSinHora((Date) itemSelected.get("FECHA_INI")).compareTo(JsfUtil.stringToDate(JsfUtil.bundleBDIntegrado("sspCarne_fechaCarne"), "dd/MM/yyyy")) >= 0
                && JsfUtil.getFechaSinHora((Date) itemSelected.get("FECHA_FIN")).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0) {
            return EstadoCarneGssp.VIGENTE.toString();
        }
        return EstadoCarneGssp.VENCIDO.toString();
    }

    public String obtenerEstiloCarne() {
        String descEstado = "color: red;";
        if (itemSelected == null) {
            return descEstado;
        }
        if (JsfUtil.getFechaSinHora((Date) itemSelected.get("FECHA_FIN")).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0) {
            descEstado = "";
        }
        if (JsfUtil.getFechaSinHora((Date) itemSelected.get("FECHA_INI")).compareTo(JsfUtil.stringToDate(JsfUtil.bundleBDIntegrado("sspCarne_fechaCarne"), "dd/MM/yyyy")) >= 0) {
            descEstado = "";
        }
        return descEstado;
    }

    public boolean renderBtnFirmadoCarneDigital() {
        if (registro != null) {
            return carneDigital != null && "TP_ECC_APR".equals(registro.getEstadoId().getCodProg());
        }
        return false;
    }

    public StreamedContent cargarCarneDigitalFirmado() {
        String nombreArch = carneDigital;
        String nombreDescarga = registro.getCarneId().getNroCarne() + ".pdf";
        String path = ejbSbParametroFacade.obtenerParametroXNombre("pathUpload_carneElectronicoGSSP").getValor();
        try {
            return JsfUtil.obtenerArchivoBDIntegrado(path, nombreArch, nombreDescarga, "application/pdf");
        } catch (Exception e) {
            try {
                path = JsfUtil.bundleBDIntegrado("PathUpload");
                return JsfUtil.obtenerArchivoBDIntegrado(path, nombreArch, nombreDescarga, "application/pdf");
            } catch (Exception e1) {
                e1.printStackTrace();
                JsfUtil.mensajeError("No se pudo encontrar el carné electrónico");
            }

        }
        return null;
    }
    
    public List<TipoSeguridad> getLstTipoServicio(){
        
        return ejbTipoSeguridadFacade.tipoServicioXCodigosProgs();
    }
    
    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        sheet.autoSizeColumn(1,true);
        sheet.autoSizeColumn(2,true);
        sheet.autoSizeColumn(3,true);
        sheet.autoSizeColumn(4,true);
        sheet.autoSizeColumn(5,true);
        sheet.autoSizeColumn(6,true);
        sheet.autoSizeColumn(7,true);
        sheet.autoSizeColumn(8,true);
        sheet.autoSizeColumn(9,true);
        sheet.autoSizeColumn(10,true);
        sheet.autoSizeColumn(11,true);
        sheet.autoSizeColumn(12,true);
        sheet.autoSizeColumn(13,true);
        sheet.autoSizeColumn(14,true);
        sheet.autoSizeColumn(15,true);
        sheet.autoSizeColumn(16,true);
        sheet.autoSizeColumn(17,true);
        sheet.autoSizeColumn(18,true);
        sheet.autoSizeColumn(19,true);
        sheet.autoSizeColumn(20,true);
        
        HSSFRow header = sheet.getRow(0);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
        }
    }

    public void openDlgCargarArchivoCese() {
        //rutaRecibo = null;
        //nombreArchivo = null;
        rutaArchivoCargaCese = null;
        nombreArchivoCargaCese = null;
    }    
    
    /**
     * METODO QUE RECIBE EL ARCHIVO DE TXT PARA LA CARGA
     *
     * @param event
     */
    public void lstnrFile(FileUploadEvent event) {
        try {
            String rutaParametro; 
            rutaArchivoCargaCese = null;
            nombreArchivoCargaCese = null;
            file = event.getFile();
            rutaParametro = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesCeseMasivoGssp").getValor();
            rutaArchivoCargaCese =  rutaParametro + event.getFile().getFileName();
            nombreArchivoCargaCese = event.getFile().getFileName();
            upFilesCont.copyFileUploadDocumento(event.getFile().getFileName(), event.getFile().getInputstream(),rutaParametro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
        /**
     * METODO PARA REALIZAR LA COPIA DE RECIBOS AL REPOSITORIO
     *
     * @throws IOException
     */
    public void cargarDlgVigilantesArchivo() throws IOException {

        try {
            //if (validarCarga()) {
                TipoSeguridad buscaModalidadSelected = null;
                File filex = new File(rutaArchivoCargaCese);
                if (filex.exists()) {
                    BufferedReader alt = new BufferedReader(new FileReader(filex));

                    //sbrp.setRutaArchivo(rutaRecibo);
                    lstObservacionCargaMasiva = new ArrayList();
                    Map tempObservacion = new HashMap();
                    String mensajeObserva = "";
                    boolean existeObservacionesCarga = false;
                    int cont = 0;
                    int registrosAgregados = 0;
                    String descripTipoDocPersona = "";
                    String numeroDocPersona = "";
                    String descripModalidad = "";
                    int posicionCaracter = 0;
                    boolean validacion = true;
                    int contAgrega = 0;
                    
                    while (true) {
                        String a = alt.readLine();
                        if (a == null) {
                            break;
                        /*} else if (a.startsWith("9999")) {
                            break;*/
                        }
                        if(!a.isEmpty()){
                            cont++;  
                            if (a.contains("|")) { 
                                if (a.substring(0, 3).trim().equals("DNI")) {
                                    descripTipoDocPersona = a.substring(0, 3).trim();
                                } else if (a.substring(0, 2).trim().equals("CE")) {
                                    descripTipoDocPersona = a.substring(0, 2).trim();
                                } else { 
                                    //JsfUtil.mensajeError("El tipo de documento no existe en la línea " + cont);
                                    //Añade Observación
                                    mensajeObserva = "El tipo de documento no existe en la línea " + cont;
                                    //JsfUtil.mensajeError(mensajeObserva);
                                    tempObservacion = new HashMap();
                                    tempObservacion.put("observacion", mensajeObserva);
                                    tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                    lstObservacionCargaMasiva.add(tempObservacion);
                                    existeObservacionesCarga = true;
                                    //Fin de Añade Observación
                                }
                                
                                if (descripTipoDocPersona.equals("DNI")) {
                                    numeroDocPersona = a.substring(5, 13).trim();
                                    tipoDocSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI");
                                } 
                                
                                if (descripTipoDocPersona.equals("CE")) {
                                    numeroDocPersona = a.substring(4, 13).trim();
                                    tipoDocSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_CE");
                                    
                                } 
                                
                                if (tipoDocSelected == null) {
                                    //JsfUtil.mensajeError("En la linea " + cont + " el tipo de Documento no existe. Verifique");
                                    //Añade Observación
                                    mensajeObserva = "En la linea " + cont + " el tipo de Documento no existe. Verifique";
                                    //JsfUtil.mensajeError(mensajeObserva);
                                    tempObservacion = new HashMap();
                                    tempObservacion.put("observacion", mensajeObserva);
                                    tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                    lstObservacionCargaMasiva.add(tempObservacion);
                                    existeObservacionesCarga = true;
                                    //Fin de Añade Observación
                                }
                                
                                if (numeroDocPersona.isEmpty()) {
                                    //JsfUtil.mensajeError("En la linea " + cont + " el Documento no tiene datos. Verifique");
                                    //Añade Observación
                                    mensajeObserva = "En la linea " + cont + " el Documento no tiene datos. Verifique";
                                    //JsfUtil.mensajeError(mensajeObserva);
                                    tempObservacion = new HashMap();
                                    tempObservacion.put("observacion", mensajeObserva);
                                    tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                    lstObservacionCargaMasiva.add(tempObservacion);
                                    existeObservacionesCarga = true;
                                    //Fin de Añade Observación
                                }
                                
                                numDoc = numeroDocPersona;
                                
                                //Selecciona la descripción de la modalidad
                                posicionCaracter = a.indexOf("|");
                                descripModalidad = a.substring(posicionCaracter+1, a.length()).trim();
                                //Busca los Datos del Vigilante
                                //buscarVigilanteCese();
                                
                                //Buscamos los datos del Vigilante
                                validacion = true;
                                if (tipoDocSelected != null && numDoc != null) {
                                    if (!numDoc.trim().isEmpty()) {
                                        switch (tipoDocSelected.getCodProg()) {
                                            case "TP_DOCID_DNI":
                                                if (numDoc.trim().length() != 8) {
                                                    //JsfUtil.mensajeError("El dni es de 8 dígitos ");
                                                    //Añade Observación
                                                    mensajeObserva = "En la linea " + cont + " El dni " + numDoc + " debe ser de 8 dígitos. Verifique";
                                                    //JsfUtil.mensajeError(mensajeObserva);
                                                    tempObservacion = new HashMap();
                                                    tempObservacion.put("observacion", mensajeObserva);
                                                    tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                                    lstObservacionCargaMasiva.add(tempObservacion);
                                                    existeObservacionesCarga = true;
                                                    //Fin de Añade Observación

                                                    validacion = false;
                                                }
                                                break;
                                            case "TP_DOCID_CE":
                                                if (numDoc.trim().length() != 9) {
                                                    //JsfUtil.mensajeError("El carnet de extranjería es de 9 dígitos ");
                                                    //Añade Observación
                                                    mensajeObserva = "En la linea " + cont + " El carnet de extranjería " + numDoc + " debe ser de 9 dígitos. Verifique";
                                                    //JsfUtil.mensajeError(mensajeObserva);
                                                    tempObservacion = new HashMap();
                                                    tempObservacion.put("observacion", mensajeObserva);
                                                    tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                                    lstObservacionCargaMasiva.add(tempObservacion);
                                                    existeObservacionesCarga = true;
                                                    //Fin de Añade Observación
                                                    
                                                    validacion = false;
                                                }
                                                break;
                                        }
                                        if (validacion) {
                                            limpiarVigilante();
                                            lstVigilanteBusqueda = ejbSspCarneFacade.buscarVigilanteXEmpresaXNumDoc(numDoc, JsfUtil.getLoggedUser().getPersonaId());
                                            if (lstVigilanteBusqueda != null && !lstVigilanteBusqueda.isEmpty()) {
                                                Map vigilante = lstVigilanteBusqueda.get(0);

                                                if (!validaFechaEmision((Date) vigilante.get("FEC_EMI"))) {
                                                    //JsfUtil.mensajeError("No puede cesar el carné : " + vigilante.get("NRO_CRN_VIG") + " porque ha sido emitido hace menos de 2 días hábiles");
                                                    //Añade Observación
                                                    mensajeObserva = "En la linea " + cont + " No puede cesar el carné : " + vigilante.get("NRO_CRN_VIG") + " porque ha sido emitido hace menos de 2 días hábiles";
                                                    //JsfUtil.mensajeError(mensajeObserva);
                                                    tempObservacion = new HashMap();
                                                    tempObservacion.put("observacion", mensajeObserva);
                                                    tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                                    lstObservacionCargaMasiva.add(tempObservacion);
                                                    existeObservacionesCarga = true;
                                                    //Fin de Añade Observación
                                                    //return;
                                                }

                                                llenarModalidadesVigilante();
                                                //numDoc = null;
                                                //tipoDocSelected = null;
                                                setRenderDatosVigilante(true);

                                                numDocView = ((lstVigilanteBusqueda.get(0).get("NUMDOC") != null) ? lstVigilanteBusqueda.get(0).get("NUMDOC").toString() : null);
                                                nombres = ((lstVigilanteBusqueda.get(0).get("NOMBRES") != null) ? lstVigilanteBusqueda.get(0).get("NOMBRES").toString() : null);
                                                apePat = ((lstVigilanteBusqueda.get(0).get("APAT") != null) ? lstVigilanteBusqueda.get(0).get("APAT").toString() : null);
                                                apeMat = ((lstVigilanteBusqueda.get(0).get("AMAT") != null) ? lstVigilanteBusqueda.get(0).get("AMAT").toString() : null);
                                                tipoDoc = ((lstVigilanteBusqueda.get(0).get("TIPODOC") != null) ? lstVigilanteBusqueda.get(0).get("TIPODOC").toString() : null);
                                                if (lstVigilanteBusqueda.size() == 1) {
                                                    modalidadSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodProg(vigilante.get("TIP_MOD").toString());
                                                    cambiaModalidad();
                                                /*} else if (lstVigilanteBusqueda.size() > 1) {
                                                    JsfUtil.mensajeAdvertencia("Por favor seleccione una modalidad");*/
                                                }
                                            } else {
                                                setRenderDatosVigilante(true);
                                                //Añade Observación
                                                mensajeObserva = "En la linea " + cont + " con Documento: " + numDoc +  ". No se encontró al personal de seguridad según los datos ingresados";
                                                //JsfUtil.mensajeError(mensajeObserva);
                                                tempObservacion = new HashMap();
                                                tempObservacion.put("observacion", mensajeObserva);
                                                tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                                lstObservacionCargaMasiva.add(tempObservacion);
                                                existeObservacionesCarga = true;
                                                //Fin de Añade Observación
                                            }
                                        }
                                    } else {
                                        //JsfUtil.mensajeError("En la linea " + cont + " el Documento no tiene datos. Verifique");
                                        //Añade Observación
                                        mensajeObserva = "En la linea " + cont + " el Documento no tiene datos. Verifique";
                                        //JsfUtil.mensajeError(mensajeObserva);
                                        tempObservacion = new HashMap();
                                        tempObservacion.put("observacion", mensajeObserva);
                                        tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                        lstObservacionCargaMasiva.add(tempObservacion);
                                        existeObservacionesCarga = true;
                                        //Fin de Añade Observación
                                    }
                                } else {
                                    //JsfUtil.mensajeError("En la linea " + cont + " el Tipo de Documento no tiene datos. Verifique");
                                    //Añade Observación
                                    mensajeObserva = "En la linea " + cont + " el Tipo de Documento no tiene datos. Verifique";
                                    //JsfUtil.mensajeError(mensajeObserva);
                                    tempObservacion = new HashMap();
                                    tempObservacion.put("observacion", mensajeObserva);
                                    tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                    lstObservacionCargaMasiva.add(tempObservacion);
                                    existeObservacionesCarga = true;
                                    //Fin de Añade Observación
                                }
                                //Fin de Buscamos los datos del Vigilante
                                
                                buscaModalidadSelected = ejbTipoSeguridadFacade.tipoSeguridadXDescripcion(descripModalidad);
                                if (buscaModalidadSelected == null) {
                                    //JsfUtil.mensajeError("La línea " + cont + " tiene la modalidad : " + descripModalidad + " que no existe. Verifique");
                                    //Añade Observación
                                    mensajeObserva = "La línea " + cont + " tiene la modalidad : " + descripModalidad + " no existe. Verifique";
                                    //JsfUtil.mensajeError(mensajeObserva);
                                    tempObservacion = new HashMap();
                                    tempObservacion.put("observacion", mensajeObserva);
                                    tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                    lstObservacionCargaMasiva.add(tempObservacion);
                                    existeObservacionesCarga = true;
                                    //Fin de Añade Observación
                                } else { 
                                    modalidadSelected = buscaModalidadSelected;
                                    
                                    //agregarVigilanteCese();
                                    contAgrega = 0;
                                    //Agrega a la lista 
                                    if (modalidadSelected != null && carneSelected != null) {
                                        for (Map actual : lstVigilanteBusqueda) {
                                            if (actual.get("NRO_CRN_VIG").toString().equals("" + carneSelected.getNroCarne())) {
                                                for (Map vigilante : lstVigilante) {
                                                    if (vigilante.get("NRO_CRN_VIG").toString().equals("" + carneSelected.getNroCarne()) && vigilante.get("TIP_MOD").toString().equals(modalidadSelected.getCodProg())) {
                                                        contAgrega++;
                                                    }
                                                }
                                                if (contAgrega == 0) {
                                                    int contPendiente = ejbSspRegistroFacade.buscarRegistroNoFinalizadoVigenteReferenciado(carneSelected.getModalidadId().getId(), carneSelected.getVigilanteId().getId(), obtenerRegistroEmisionCarne(carneSelected.getSspRegistroList()));
                                                    if (contPendiente > 0) {
                                                        //JsfUtil.mensajeError("No puede cesar a esta persona porque ya hay un registro pendiente modificando al carné");
                                                        //Añade Observación
                                                        mensajeObserva = "La línea " + cont + " No puede cesar a esta persona con documento: " + numDoc + " porque ya hay un registro pendiente modificando al carné";
                                                        //JsfUtil.mensajeError(mensajeObserva);
                                                        tempObservacion = new HashMap();
                                                        tempObservacion.put("observacion", mensajeObserva);
                                                        tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                                        lstObservacionCargaMasiva.add(tempObservacion);
                                                        existeObservacionesCarga = true;
                                                        //Fin de Añade Observación

                                                        //return;
                                                    } else {
                                                        Map temp = new HashMap();
                                                        temp.put("ID", actual.get("ID"));
                                                        temp.put("TIPODOC", actual.get("TIPODOC"));
                                                        temp.put("NUMDOC", actual.get("NUMDOC"));
                                                        temp.put("APAT", actual.get("APAT"));
                                                        temp.put("AMAT", actual.get("AMAT"));
                                                        temp.put("NOMBRES", actual.get("NOMBRES"));
                                                        temp.put("NRO_CRN_VIG", actual.get("NRO_CRN_VIG"));
                                                        temp.put("TIP_MOD", actual.get("TIP_MOD"));
                                                        temp.put("DES_MOD", actual.get("DES_MOD"));
                                                        temp.put("FEC_EMI", actual.get("FEC_EMI"));
                                                        temp.put("FEC_VENC", actual.get("FEC_VENC"));
                                                        temp.put("estadoCese", estadoCese);
                                                        temp.put("tipoDoc_desc", ((carneSelected.getVigilanteId().getTipoDoc() != null) ? carneSelected.getVigilanteId().getTipoDoc().getNombre() : "-"));
                                                        temp.put("numDoc_desc", carneSelected.getVigilanteId().getNumDoc());
                                                        temp.put("nombreYApellidos", carneSelected.getVigilanteId().getNombresYApellidos());
                                                        temp.put("fecEmision", carneSelected.getFechaIni());
                                                        lstVigilante.add(temp);
                                                        numDoc = null;
                                                        limpiarVigilante();
                                                        registrosAgregados++;
                                                    }
                                                } else {
                                                    //JsfUtil.mensajeError("La línea " + cont + " el Vigilante con Documento: " + numDoc + " ya ha sido ingresado.");
                                                    //Añade Observación
                                                     mensajeObserva = "La línea " + cont + " el Vigilante con Documento: " + numDoc + " ya ha sido ingresado.";
                                                     //JsfUtil.mensajeError(mensajeObserva);
                                                     tempObservacion = new HashMap();
                                                     tempObservacion.put("observacion", mensajeObserva);
                                                     tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                                     lstObservacionCargaMasiva.add(tempObservacion);
                                                     existeObservacionesCarga = true;
                                                     //Fin de Añade Observación
                                                }
                                                break;
                                            }
                                        }
                                    } else {
                                        //JsfUtil.mensajeError("La línea " + cont + " tiene la modalidad : " + descripModalidad + " que no existe. Verifique");
                                        //Añade Observación
                                        /*mensajeObserva = "La línea " + cont + " tiene la modalidad : " + descripModalidad + " no existe. Verifique";
                                        //JsfUtil.mensajeError(mensajeObserva);
                                        tempObservacion = new HashMap();
                                        tempObservacion.put("observacion", mensajeObserva);
                                        tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                        lstObservacionCargaMasiva.add(tempObservacion);
                                        existeObservacionesCarga = true;*/
                                        //Fin de Añade Observación
                                    }
                                    //Fin de Agrega a la lista
                                }
                            } else {
                                //JsfUtil.mensajeError("La línea " + cont + " no tiene la estructura del archivo correcto");
                                //Añade Observación
                                mensajeObserva = "La línea " + cont + " no tiene la estructura del archivo correcto";
                                //JsfUtil.mensajeError(mensajeObserva);
                                tempObservacion = new HashMap();
                                tempObservacion.put("observacion", mensajeObserva);
                                tempObservacion.put("nombreArchivo", nombreArchivoCargaCese);
                                lstObservacionCargaMasiva.add(tempObservacion);
                                existeObservacionesCarga = true;
                                //Fin de Añade Observación
                            }
                                                  
                            
                        }
                    }
                    
                    if (existeObservacionesCarga) {
                        JsfUtil.mensajeError("Existen errores en la Carga Masiva del Archivo. Verifique la lista de los Errores de Carga Masiva por Cese");    
                    }
                    JsfUtil.mensaje("Se cargaron masivamente " + registrosAgregados + " registros(s)");
                    RequestContext.getCurrentInstance().execute("PF('wvDlgFrmCeseMasivo').hide()");
                    RequestContext.getCurrentInstance().update(obtenerForm() + ":tabGestion");
                    
                }
            //}
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrio un error al cargar el archivo, comunique al administrador del sistema.");
        }
        
    }

    public void removerCarnesTemporal() {
        if (!JsfUtil.isNullOrEmpty(lstVigilanteSelect)) {
            if (lstVigilante.containsAll(lstVigilanteSelect)) {
                lstVigilante.removeAll(lstVigilanteSelect);
            }
            if (lstVigilante.isEmpty()) {
                lstVigilante = null;
                lstVigilante = new ArrayList();
            }
        } else {
           JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeSeleccionElementoEliminar"));
        }
    }
    
    public SbParametro obtenerParametroXNombre(String nombre){
        return ejbSbParametroFacade.obtenerParametroXNombre(nombre);
    }
    
     public String obtenerMsjeInvalidSize(){
        String byteParametrizado = "";
        try {
            byteParametrizado = ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_sizeLimite_txtCeseMasivo").getValor();
        } catch (Exception e) {
            byteParametrizado = null;
        }        
        if(byteParametrizado == null){
            byteParametrizado = "0";
        }
        return JsfUtil.bundle("CeseMasivo_fileUpload_InvalidFileMessage") + " " + 
               (Integer.parseInt(byteParametrizado)/1024) + " Kb";
    }
}
