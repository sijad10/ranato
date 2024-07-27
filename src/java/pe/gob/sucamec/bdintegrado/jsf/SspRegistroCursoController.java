package pe.gob.sucamec.bdintegrado.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import pe.gob.sucamec.bdintegrado.data.SspRegistroCurso;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import javax.faces.model.ListDataModel;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.TimeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbFeriado;
import pe.gob.sucamec.bdintegrado.data.SbMedioContactoGt;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.SspAlumnoCurso;
import pe.gob.sucamec.bdintegrado.data.SspCursoEvento;
import pe.gob.sucamec.bdintegrado.data.SspInstructor;
import pe.gob.sucamec.bdintegrado.data.SspInstructorModulo;
import pe.gob.sucamec.bdintegrado.data.SspLocal;
import pe.gob.sucamec.bdintegrado.data.SspLocalContacto;
import pe.gob.sucamec.bdintegrado.data.SspModulo;
import pe.gob.sucamec.bdintegrado.data.SspNotas;
import pe.gob.sucamec.bdintegrado.data.SspPersonaFoto;
import pe.gob.sucamec.bdintegrado.data.SspPrograHistorial;
import pe.gob.sucamec.bdintegrado.data.SspPrograHora;
import pe.gob.sucamec.bdintegrado.data.SspProgramacion;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.CursosOption;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.TableDynamic;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import wspide.ResPideMigra;

// Nombre de la instancia en la aplicacion //
@Named("sspRegistroCursoController")
@SessionScoped

/**
 * Clase con SspRegistroCursoController instanciada como
 * sspRegistroCursoController. Contiene funciones utiles para la entidad
 * SspRegistroCurso. Esta vinculada a las páginas SspRegistroCurso/create.xhtml,
 * SspRegistroCurso/update.xhtml, SspRegistroCurso/list.xhtml,
 * SspRegistroCurso/view.xhtml Nota: Las tablas deben tener la estructura de
 * Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class SspRegistroCursoController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    SspRegistroCurso registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SspRegistroCurso> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SspRegistroCurso> registrosSeleccionados;
    ///////////////////
    private int tabIndex;
    private String buscarPor;
    private Date fechaInicio = null;
    private Date fechaFinal = null;
    private List<SspRegistroCurso> resultadosBandeja;
    private List<SspRegistroCurso> resultadosBandejaSeleccionados;
    private TipoSeguridad tipoFormacion;
    private TipoSeguridad tipoServicio;
    private boolean disabledBtnTransmitir;
    private SspLocal local;
    private List<SspLocal> lstLocales;
    private TipoSeguridad estadoCurso;
    private String latitudSelected;
    private String longitudSelected;
    private UploadedFile file;
    private StreamedContent foto;
    private StreamedContent fotoPopUp;
    private byte[] fotoByte;
    private String correo;
    private String correoTemp;
    private String telefonoFijo;
    private String telefonoFijoTemp;
    private String telefonoMovil;
    private String telefonoMovilTemp;
    private List<SspCursoEvento> lstObservaciones;
    private List<Map> lstObservacionesDetalle;
    private String fechaLimite;
    private String tipoEstado;
    private Integer DIAS_PRESENTACION;
    private String direccionMapa;
    private Date fechaMinima;
    private Date fechaNacimiento;
    private boolean flagBtnCambio;
    private SspInstructor instructor;
    private int DIAS_CAMBIO_LOCAL;
    private boolean isSedeLima;
    private Long idColorValidate;
    private List<SspModulo> lstModulosNoRep;
    private boolean renderTipoCurso;
    private TipoSeguridad tipoCursoSelected;
    private Date fechaObs;
    private Integer DIAS_OBSERVACION;
    private boolean esEvento;
    private List<TipoSeguridad> lstModalidades;
    private String tipoCurso;
    private String cursoDlg;
    private String usuarioArchivar;
    /// Local
    private MapModel modelMap;
    private String nombreLocal;
    private String caracteristicas;
    boolean esVirtual = false;
    boolean declaracion = false;
    private SbDistritoGt ubigeoLocal;
    private String direccionLocal;
    private String telefonoLocal;
    private TipoBaseGt tipoUbicacionLocal;
    private String referenciaLocal;
    private String correoLocal;
    private String aforoLocal;
    private String latitudLocal;
    private String longitudLocal;
    private boolean disabledLocal;
    private boolean disabledLocalEdicion;
    private SspLocal localSelected;
    private String motivoLocal;
    // Instructores
    private String tipoDoc;
    private String numDoc;
    private String numDocBusInst;
    private String nombresInstructor;
    private List<CursosOption> lstCursosOpt;
    private List<CursosOption> lstCursosOptSelected;
    private List<CursosOption> lstCursosOptFiltrados;
    private List<SspModulo> lstModulos;
    private List<SspModulo> lstModulosSelected;
    private List<Map> lstInstructoresTabla;
    private List<Map> lstCargaInstructores;
    private List<TableDynamic> dtColumns;
    private boolean disabledTipoFormacion;
    private boolean disabledTipoServicio;
    private List<SspModulo> lstModulosFiltrados;
    private List<SspInstructor> lstInstructores;
    // Agendar
    private SspLocal localAgendarSelected;
    private SspModulo moduloAgendarSelected;
    private Date fechaInicioAgendar;
    private Date fechaFinAgendar;
    private Date horaInicioAgendar;
    private Date horaFinAgendar;
    private int horaInicioPermitido;    //Desde qué hora como mínimo se puede iniciar una clase
    private int horaFinPermitido;   //Hasta qué hora como máximo se puede terminar una clase
    private int limiteHorasAcad;    //Cantidad permitida de horas de clase al día
    private int limiteHorasAcadRef; //Cantidad permitida de horas de refrigerio
    private int aforoLocalPermitido;  //Aforo máximo permitido para plataforma Virtual
    private Date fechaCortePase;
    private Date fechaHorario;
    private List<Date> listaFechasDisponibles;
    private List<SspProgramacion> lstProgramacion;    
    private Long idLocal;
    private boolean disabledFechasHorario;
    private Date horaInicioRefrigerio;
    private Date horaFinRefrigerio;
    private String fechasHorarioCurso;
    /// Vigilante
    private TipoBaseGt tipoDocVigilante;
    private String numDocVigilante;
    private String nombresVigilante;
    private String apePatVigilante;
    private String apeMatVigilante;
    private TipoBaseGt sexoVigilante;
    private String direccionVigilante;
    private SbDistritoGt ubigeoVigilante;
    private TipoBaseGt medioContactoVigilante;
    private String valorContactoVigilante;
    private TipoBaseGt modalidadVigilante;
    private int maxLength;
    private SbDireccionGt direccionCombo;
    private List<SbDireccionGt> lstDirecciones;
    private boolean renderDireccionCombo;
    private boolean esNuevo;
    private boolean disabledNombres;
    private SspAlumnoCurso alumnoVigilante;
    private List<SspAlumnoCurso> lstAlumnosVigilantes;
    private String nombreFoto;
    private boolean renderPanelVigilante;
    private String nombreFotoTemporal;
    private List<Map> lstFotosAlumnos;
    // Popup búsqueda
    private SbDistritoGt ubigeoBusqueda;
    private List<SspLocal> lstLocalesBusqueda;
    //// BANDEJA INSTRUCTOR
    private SbPersonaGt instructorPersona;
    private String nombrePopup;
    private String msjePopUp;
    private SspProgramacion programacionConfirma;
    private short tipoConfirmacion;
    private List<SspNotas> lstNotasTemporales;
    private final int NOTA_APROBATORIA = 13;
    private boolean esInstructor;
    private List<SspNotas> lstNotasActualizacion;
    private String archivo;
    private String archivoTemporal;
    private UploadedFile fileNotas;
    private StreamedContent notaStream;
    private byte[] notasByte;
    private String archivoNotas;
    private String archivoNotasTemporal;
    /// MODIFICATORIA
    private List<SspPrograHistorial> lstProgramacionHistorial;
    private String telefonoContacto;
    private String correoContacto;
    private String instructorContacto;
    private String observacion;
    private boolean cambioLocal;
    private boolean cambioInstructor;
    private boolean cambioHorario;
    /// CAMBIO DE HORARIO
    private List<SspPrograHora> lstCambioHorario;
    private List<SspPrograHora> lstProgramacionHorario;
    private List<SspPrograHora> lstProgramacionHorarioTemp;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroCursoFacade ejbSspRegistroCursoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspLocalFacade ejbSspLocalFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspModuloFacade ejbSspModuloFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspAlumnoCursoFacade ejbSspAlumnoCursoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspProgramacionFacade ejbSspProgramacionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspInstructorFacade ejbSspInstructorFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade ejbTipoSeguridadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt ejbSbDistritoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade ejbSbDerivacionCydocFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDepartamentoFacadeGt ejbSbDepartamentoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPaisFacadeGt ejbSbPaisFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbFeriadoFacade ejbSbFeriadoFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.CursosFacade ejbRmaCursosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.CarteraClienteFacade ejbCarteraClienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspNotasFacade ejbSspNotasFacade;
    @Inject
    WsPide wsPideController;
    @Inject
    WsTramDoc wsTramDocController;    

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

    public TipoSeguridad getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoSeguridad tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @return
     */
    public List<SspRegistroCurso> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SspRegistroCurso> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Anula multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void anularMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (SspRegistroCurso a : registrosSeleccionados) {
                a.setActivo((short) 0);
                ejbSspRegistroCursoFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Activa multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void activarMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (SspRegistroCurso a : registrosSeleccionados) {
                a.setActivo((short) 1);
                ejbSspRegistroCursoFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a activo en un
     * registro de una lista.
     *
     */
    public void activar() {
        try {
            registro = (SspRegistroCurso) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbSspRegistroCursoFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un
     * registro de una lista.
     *
     */
    public void anular() {
        try {
            registro = (SspRegistroCurso) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbSspRegistroCursoFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public SspRegistroCurso getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SspRegistroCurso registro) {
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
     * Propiedad para los resultados de la búsqueda del CRUD.
     *
     * @return Los resultados de la búsqueda.
     */
    public ListDataModel<SspRegistroCurso> getResultados() {
        return resultados;
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

    public List<SspRegistroCurso> getResultadosBandeja() {
        return resultadosBandeja;
    }

    public void setResultadosBandeja(List<SspRegistroCurso> resultadosBandeja) {
        this.resultadosBandeja = resultadosBandeja;
    }

    public List<SspRegistroCurso> getResultadosBandejaSeleccionados() {
        return resultadosBandejaSeleccionados;
    }

    public void setResultadosBandejaSeleccionados(List<SspRegistroCurso> resultadosBandejaSeleccionados) {
        this.resultadosBandejaSeleccionados = resultadosBandejaSeleccionados;
    }

    public boolean isDisabledBtnTransmitir() {
        return disabledBtnTransmitir;
    }

    public void setDisabledBtnTransmitir(boolean disabledBtnTransmitir) {
        this.disabledBtnTransmitir = disabledBtnTransmitir;
    }

    public TipoSeguridad getTipoFormacion() {
        return tipoFormacion;
    }

    public void setTipoFormacion(TipoSeguridad tipoFormacion) {
        this.tipoFormacion = tipoFormacion;
    }

    public SspLocal getLocal() {
        return local;
    }

    public void setLocal(SspLocal local) {
        this.local = local;
    }

    public List<SspLocal> getLstLocales() {
        return lstLocales;
    }

    public void setLstLocales(List<SspLocal> lstLocales) {
        this.lstLocales = lstLocales;
    }

    public TipoSeguridad getEstadoCurso() {
        return estadoCurso;
    }

    public void setEstadoCurso(TipoSeguridad estadoCurso) {
        this.estadoCurso = estadoCurso;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public boolean isEsVirtual() {
        return esVirtual;
    }

    public void setEsVirtual(boolean esVirtual) {
        this.esVirtual = esVirtual;
    }

    public boolean isDeclaracion() {
        return declaracion;
    }

    public void setDeclaracion(boolean declaracion) {
        this.declaracion = declaracion;
    }
    
    public SbDistritoGt getUbigeoLocal() {
        return ubigeoLocal;
    }

    public void setUbigeoLocal(SbDistritoGt ubigeoLocal) {
        this.ubigeoLocal = ubigeoLocal;
    }

    public String getDireccionLocal() {
        return direccionLocal;
    }

    public void setDireccionLocal(String direccionLocal) {
        this.direccionLocal = direccionLocal;
    }

    public String getTelefonoLocal() {
        return telefonoLocal;
    }

    public void setTelefonoLocal(String telefonoLocal) {
        this.telefonoLocal = telefonoLocal;
    }

    public TipoBaseGt getTipoUbicacionLocal() {
        return tipoUbicacionLocal;
    }

    public void setTipoUbicacionLocal(TipoBaseGt tipoUbicacionLocal) {
        this.tipoUbicacionLocal = tipoUbicacionLocal;
    }

    public String getReferenciaLocal() {
        return referenciaLocal;
    }

    public void setReferenciaLocal(String referenciaLocal) {
        this.referenciaLocal = referenciaLocal;
    }

    public String getCorreoLocal() {
        return correoLocal;
    }

    public void setCorreoLocal(String correoLocal) {
        this.correoLocal = correoLocal;
    }

    public String getAforoLocal() {
        return aforoLocal;
    }

    public void setAforoLocal(String aforoLocal) {
        this.aforoLocal = aforoLocal;
    }

    public String getLatitudLocal() {
        return latitudLocal;
    }

    public void setLatitudLocal(String latitudLocal) {
        this.latitudLocal = latitudLocal;
    }

    public String getLongitudLocal() {
        return longitudLocal;
    }

    public void setLongitudLocal(String longitudLocal) {
        this.longitudLocal = longitudLocal;
    }

    public String getLatitudSelected() {
        return latitudSelected;
    }

    public void setLatitudSelected(String latitudSelected) {
        this.latitudSelected = latitudSelected;
    }

    public String getLongitudSelected() {
        return longitudSelected;
    }

    public void setLongitudSelected(String longitudSelected) {
        this.longitudSelected = longitudSelected;
    }

    public MapModel getModelMap() {
        return modelMap;
    }

    public void setModelMap(MapModel modelMap) {
        this.modelMap = modelMap;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNumDocBusInst() {
        return numDocBusInst;
    }

    public void setNumDocBusInst(String numDocBusInst) {
        this.numDocBusInst = numDocBusInst;
    }

    public List<SspModulo> getLstModulos() {
        return lstModulos;
    }

    public void setLstModulos(List<SspModulo> lstModulos) {
        this.lstModulos = lstModulos;
    }

    public String getNombresInstructor() {
        return nombresInstructor;
    }

    public void setNombresInstructor(String nombresInstructor) {
        this.nombresInstructor = nombresInstructor;
    }

    public List<SspModulo> getLstModulosSelected() {
        return lstModulosSelected;
    }

    public void setLstModulosSelected(List<SspModulo> lstModulosSelected) {
        this.lstModulosSelected = lstModulosSelected;
    }

    public List<Map> getLstInstructoresTabla() {
        return lstInstructoresTabla;
    }

    public void setLstInstructoresTabla(List<Map> lstInstructoresTabla) {
        this.lstInstructoresTabla = lstInstructoresTabla;
    }

    public List<Map> getLstCargaInstructores() {
        return lstCargaInstructores;
    }

    public void setLstCargaInstructores(List<Map> lstCargaInstructores) {
        this.lstCargaInstructores = lstCargaInstructores;
    }

    public List<TableDynamic> getDtColumns() {
        return dtColumns;
    }

    public void setDtColumns(List<TableDynamic> dtColumns) {
        this.dtColumns = dtColumns;
    }

    public boolean isDisabledTipoFormacion() {
        return disabledTipoFormacion;
    }

    public void setDisabledTipoFormacion(boolean disabledTipoFormacion) {
        this.disabledTipoFormacion = disabledTipoFormacion;
    }
    public boolean isDisabledTipoServicio() {
        return disabledTipoServicio;
    }

    public void setDisabledTipoServicio(boolean disabledTipoServicio) {
        this.disabledTipoServicio = disabledTipoServicio;
    }

    public List<SspModulo> getLstModulosFiltrados() {
        return lstModulosFiltrados;
    }

    public void setLstModulosFiltrados(List<SspModulo> lstModulosFiltrados) {
        this.lstModulosFiltrados = lstModulosFiltrados;
    }

    public SspLocal getLocalAgendarSelected() {
        return localAgendarSelected;
    }

    public void setLocalAgendarSelected(SspLocal localAgendarSelected) {
        this.localAgendarSelected = localAgendarSelected;
    }

    public SspModulo getModuloAgendarSelected() {
        return moduloAgendarSelected;
    }

    public void setModuloAgendarSelected(SspModulo moduloAgendarSelected) {
        this.moduloAgendarSelected = moduloAgendarSelected;
    }

    public Date getFechaInicioAgendar() {
        return fechaInicioAgendar;
    }

    public void setFechaInicioAgendar(Date fechaInicioAgendar) {
        this.fechaInicioAgendar = fechaInicioAgendar;
    }

    public Date getFechaFinAgendar() {
        return fechaFinAgendar;
    }

    public void setFechaFinAgendar(Date fechaFinAgendar) {
        this.fechaFinAgendar = fechaFinAgendar;
    }

    public List<SspProgramacion> getLstProgramacion() {
        return lstProgramacion;
    }

    public void setLstProgramacion(List<SspProgramacion> lstProgramacion) {
        this.lstProgramacion = lstProgramacion;
    }

    public Long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Long idLocal) {
        this.idLocal = idLocal;
    }

    public TipoBaseGt getTipoDocVigilante() {
        return tipoDocVigilante;
    }

    public void setTipoDocVigilante(TipoBaseGt tipoDocVigilante) {
        this.tipoDocVigilante = tipoDocVigilante;
    }

    public String getNumDocVigilante() {
        return numDocVigilante;
    }

    public void setNumDocVigilante(String numDocVigilante) {
        this.numDocVigilante = numDocVigilante;
    }

    public String getNombresVigilante() {
        return nombresVigilante;
    }

    public void setNombresVigilante(String nombresVigilante) {
        this.nombresVigilante = nombresVigilante;
    }

    public String getApePatVigilante() {
        return apePatVigilante;
    }

    public void setApePatVigilante(String apePatVigilante) {
        this.apePatVigilante = apePatVigilante;
    }

    public String getApeMatVigilante() {
        return apeMatVigilante;
    }

    public void setApeMatVigilante(String apeMatVigilante) {
        this.apeMatVigilante = apeMatVigilante;
    }

    public TipoBaseGt getSexoVigilante() {
        return sexoVigilante;
    }

    public void setSexoVigilante(TipoBaseGt sexoVigilante) {
        this.sexoVigilante = sexoVigilante;
    }

    public String getDireccionVigilante() {
        return direccionVigilante;
    }

    public void setDireccionVigilante(String direccionVigilante) {
        this.direccionVigilante = direccionVigilante;
    }

    public SbDistritoGt getUbigeoVigilante() {
        return ubigeoVigilante;
    }

    public void setUbigeoVigilante(SbDistritoGt ubigeoVigilante) {
        this.ubigeoVigilante = ubigeoVigilante;
    }

    public TipoBaseGt getMedioContactoVigilante() {
        return medioContactoVigilante;
    }

    public void setMedioContactoVigilante(TipoBaseGt medioContactoVigilante) {
        this.medioContactoVigilante = medioContactoVigilante;
    }

    public String getValorContactoVigilante() {
        return valorContactoVigilante;
    }

    public void setValorContactoVigilante(String valorContactoVigilante) {
        this.valorContactoVigilante = valorContactoVigilante;
    }

    public TipoBaseGt getModalidadVigilante() {
        return modalidadVigilante;
    }

    public void setModalidadVigilante(TipoBaseGt modalidadVigilante) {
        this.modalidadVigilante = modalidadVigilante;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public SbDireccionGt getDireccionCombo() {
        return direccionCombo;
    }

    public void setDireccionCombo(SbDireccionGt direccionCombo) {
        this.direccionCombo = direccionCombo;
    }

    public List<SbDireccionGt> getLstDirecciones() {
        return lstDirecciones;
    }

    public void setLstDirecciones(List<SbDireccionGt> lstDirecciones) {
        this.lstDirecciones = lstDirecciones;
    }

    public boolean isRenderDireccionCombo() {
        return renderDireccionCombo;
    }

    public void setRenderDireccionCombo(boolean renderDireccionCombo) {
        this.renderDireccionCombo = renderDireccionCombo;
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

    public List<SspInstructor> getLstInstructores() {
        return lstInstructores;
    }

    public void setLstInstructores(List<SspInstructor> lstInstructores) {
        this.lstInstructores = lstInstructores;
    }

    public Date getHoraInicioAgendar() {
        return horaInicioAgendar;
    }

    public void setHoraInicioAgendar(Date horaInicioAgendar) {
        this.horaInicioAgendar = horaInicioAgendar;
    }

    public Date getHoraFinAgendar() {
        return horaFinAgendar;
    }

    public void setHoraFinAgendar(Date horaFinAgendar) {
        this.horaFinAgendar = horaFinAgendar;
    }

    public int getHoraInicioPermitido() {
        return horaInicioPermitido;
    }

    public int getHoraFinPermitido() {
        return horaFinPermitido;
    }

    public void setHoraFinPermitido(int horaFinPermitido) {
        this.horaFinPermitido = horaFinPermitido;
    }

    public void setHoraInicioPermitido(int horaInicioPermitido) {
        this.horaInicioPermitido = horaInicioPermitido;
    }

    public Date getFechaHorario() {
        return fechaHorario;
    }

    public void setFechaHorario(Date fechaHorario) {
        this.fechaHorario = fechaHorario;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getFoto() {
        try {
            if(file == null && fotoByte != null){
                InputStream is = new ByteArrayInputStream(fotoByte);
                foto = new DefaultStreamedContent(is, "image/jpeg", nombreFoto);
            }
            if (file != null) {
                fotoByte = IOUtils.toByteArray(file.getInputstream());
                foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
            }
        } catch (IOException ex) {
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

    public List<Date> getListaFechasDisponibles() {
        return listaFechasDisponibles;
    }

    public void setListaFechasDisponibles(List<Date> listaFechasDisponibles) {
        this.listaFechasDisponibles = listaFechasDisponibles;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public boolean isRenderPanelVigilante() {
        return renderPanelVigilante;
    }

    public void setRenderPanelVigilante(boolean renderPanelVigilante) {
        this.renderPanelVigilante = renderPanelVigilante;
    }

    public SbDistritoGt getUbigeoBusqueda() {
        return ubigeoBusqueda;
    }

    public void setUbigeoBusqueda(SbDistritoGt ubigeoBusqueda) {
        this.ubigeoBusqueda = ubigeoBusqueda;
    }

    public List<SspLocal> getLstLocalesBusqueda() {
        return lstLocalesBusqueda;
    }

    public void setLstLocalesBusqueda(List<SspLocal> lstLocalesBusqueda) {
        this.lstLocalesBusqueda = lstLocalesBusqueda;
    }

    public boolean isDisabledLocal() {
        return disabledLocal;
    }

    public void setDisabledLocal(boolean disabledLocal) {
        this.disabledLocal = disabledLocal;
    }

    public SspLocal getLocalSelected() {
        return localSelected;
    }

    public void setLocalSelected(SspLocal localSelected) {
        this.localSelected = localSelected;
    }

    public boolean isDisabledFechasHorario() {
        return disabledFechasHorario;
    }

    public void setDisabledFechasHorario(boolean disabledFechasHorario) {
        this.disabledFechasHorario = disabledFechasHorario;
    }

    public SspAlumnoCurso getAlumnoVigilante() {
        return alumnoVigilante;
    }

    public void setAlumnoVigilante(SspAlumnoCurso alumnoVigilante) {
        this.alumnoVigilante = alumnoVigilante;
    }

    public List<SspAlumnoCurso> getLstAlumnosVigilantes() {
        return lstAlumnosVigilantes;
    }

    public void setLstAlumnosVigilantes(List<SspAlumnoCurso> lstAlumnosVigilantes) {
        this.lstAlumnosVigilantes = lstAlumnosVigilantes;
    }

    public String getNombreFotoTemporal() {
        return nombreFotoTemporal;
    }

    public void setNombreFotoTemporal(String nombreFotoTemporal) {
        this.nombreFotoTemporal = nombreFotoTemporal;
    }

    public List<Map> getLstFotosAlumnos() {
        return lstFotosAlumnos;
    }

    public void setLstFotosAlumnos(List<Map> lstFotosAlumnos) {
        this.lstFotosAlumnos = lstFotosAlumnos;
    }

    public List<CursosOption> getLstCursosOpt() {
        return lstCursosOpt;
    }

    public void setLstCursosOpt(List<CursosOption> lstCursosOpt) {
        this.lstCursosOpt = lstCursosOpt;
    }

    public List<CursosOption> getLstCursosOptSelected() {
        return lstCursosOptSelected;
    }

    public void setLstCursosOptSelected(List<CursosOption> lstCursosOptSelected) {
        this.lstCursosOptSelected = lstCursosOptSelected;
    }

    public List<CursosOption> getLstCursosOptFiltrados() {
        return lstCursosOptFiltrados;
    }

    public void setLstCursosOptFiltrados(List<CursosOption> lstCursosOptFiltrados) {
        this.lstCursosOptFiltrados = lstCursosOptFiltrados;
    }

    public SbPersonaGt getInstructorPersona() {
        return instructorPersona;
    }

    public void setInstructorPersona(SbPersonaGt instructorPersona) {
        this.instructorPersona = instructorPersona;
    }

    public String getNombrePopup() {
        return nombrePopup;
    }

    public void setNombrePopup(String nombrePopup) {
        this.nombrePopup = nombrePopup;
    }

    public String getMsjePopUp() {
        return msjePopUp;
    }

    public void setMsjePopUp(String msjePopUp) {
        this.msjePopUp = msjePopUp;
    }

    public SspProgramacion getProgramacionConfirma() {
        return programacionConfirma;
    }

    public void setProgramacionConfirma(SspProgramacion programacionConfirma) {
        this.programacionConfirma = programacionConfirma;
    }

    public short getTipoConfirmacion() {
        return tipoConfirmacion;
    }

    public void setTipoConfirmacion(short tipoConfirmacion) {
        this.tipoConfirmacion = tipoConfirmacion;
    }

    public List<SspNotas> getLstNotasTemporales() {
        return lstNotasTemporales;
    }

    public void setLstNotasTemporales(List<SspNotas> lstNotasTemporales) {
        this.lstNotasTemporales = lstNotasTemporales;
    }

    public boolean isEsInstructor() {
        return esInstructor;
    }

    public void setEsInstructor(boolean esInstructor) {
        this.esInstructor = esInstructor;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoTemp() {
        return correoTemp;
    }

    public void setCorreoTemp(String correoTemp) {
        this.correoTemp = correoTemp;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoFijoTemp() {
        return telefonoFijoTemp;
    }

    public void setTelefonoFijoTemp(String telefonoFijoTemp) {
        this.telefonoFijoTemp = telefonoFijoTemp;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getTelefonoMovilTemp() {
        return telefonoMovilTemp;
    }

    public void setTelefonoMovilTemp(String telefonoMovilTemp) {
        this.telefonoMovilTemp = telefonoMovilTemp;
    }

    public List<SspCursoEvento> getLstObservaciones() {
        return lstObservaciones;
    }

    public void setLstObservaciones(List<SspCursoEvento> lstObservaciones) {
        this.lstObservaciones = lstObservaciones;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public Integer getDIAS_PRESENTACION() {
        return DIAS_PRESENTACION;
    }

    public void setDIAS_PRESENTACION(Integer DIAS_PRESENTACION) {
        this.DIAS_PRESENTACION = DIAS_PRESENTACION;
    }

    public List<SspPrograHistorial> getLstProgramacionHistorial() {
        return lstProgramacionHistorial;
    }

    public void setLstProgramacionHistorial(List<SspPrograHistorial> lstProgramacionHistorial) {
        this.lstProgramacionHistorial = lstProgramacionHistorial;
    }

    public String getMotivoLocal() {
        return motivoLocal;
    }

    public void setMotivoLocal(String motivoLocal) {
        this.motivoLocal = motivoLocal;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public String getCursoDlg() {
        return cursoDlg;
    }

    public void setCursoDlg(String cursoDlg) {
        this.cursoDlg = cursoDlg;
    }

    public String getInstructorContacto() {
        return instructorContacto;
    }

    public void setInstructorContacto(String instructorContacto) {
        this.instructorContacto = instructorContacto;
    }

    public String getDireccionMapa() {
        return direccionMapa;
    }

    public void setDireccionMapa(String direccionMapa) {
        this.direccionMapa = direccionMapa;
    }

    public Date getFechaMinima() {
        return fechaMinima;
    }

    public void setFechaMinima(Date fechaMinima) {
        this.fechaMinima = fechaMinima;
    }

    public List<SspNotas> getLstNotasActualizacion() {
        return lstNotasActualizacion;
    }

    public void setLstNotasActualizacion(List<SspNotas> lstNotasActualizacion) {
        this.lstNotasActualizacion = lstNotasActualizacion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public StreamedContent getFotoPopUp() {
        return fotoPopUp;
    }

    public void setFotoPopUp(StreamedContent fotoPopUp) {
        this.fotoPopUp = fotoPopUp;
    }

    public boolean isFlagBtnCambio() {
        return flagBtnCambio;
    }

    public void setFlagBtnCambio(boolean flagBtnCambio) {
        this.flagBtnCambio = flagBtnCambio;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getArchivoTemporal() {
        return archivoTemporal;
    }

    public void setArchivoTemporal(String archivoTemporal) {
        this.archivoTemporal = archivoTemporal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public SspInstructor getInstructor() {
        return instructor;
    }

    public void setInstructor(SspInstructor instructor) {
        this.instructor = instructor;
    }

    public List<SspPrograHora> getLstProgramacionHorario() {
        return lstProgramacionHorario;
    }

    public void setLstProgramacionHorario(List<SspPrograHora> lstProgramacionHorario) {
        this.lstProgramacionHorario = lstProgramacionHorario;
    }

    public boolean isIsSedeLima() {
        return isSedeLima;
    }

    public void setIsSedeLima(boolean isSedeLima) {
        this.isSedeLima = isSedeLima;
    }

    public List<SspModulo> getLstModulosNoRep() {
        return lstModulosNoRep;
    }

    public void setLstModulosNoRep(List<SspModulo> lstModulosNoRep) {
        this.lstModulosNoRep = lstModulosNoRep;
    }

    public boolean isRenderTipoCurso() {
        return renderTipoCurso;
    }

    public void setRenderTipoCurso(boolean renderTipoCurso) {
        this.renderTipoCurso = renderTipoCurso;
    }

    public TipoSeguridad getTipoCursoSelected() {
        return tipoCursoSelected;
    }

    public void setTipoCursoSelected(TipoSeguridad tipoCursoSelected) {
        this.tipoCursoSelected = tipoCursoSelected;
    }

    public Date getHoraInicioRefrigerio() {
        return horaInicioRefrigerio;
    }

    public void setHoraInicioRefrigerio(Date horaInicioRefrigerio) {
        this.horaInicioRefrigerio = horaInicioRefrigerio;
    }

    public Date getHoraFinRefrigerio() {
        return horaFinRefrigerio;
    }

    public void setHoraFinRefrigerio(Date horaFinRefrigerio) {
        this.horaFinRefrigerio = horaFinRefrigerio;
    }

    public boolean isDisabledLocalEdicion() {
        return disabledLocalEdicion;
    }

    public void setDisabledLocalEdicion(boolean disabledLocalEdicion) {
        this.disabledLocalEdicion = disabledLocalEdicion;
    }

    public List<Map> getLstObservacionesDetalle() {
        return lstObservacionesDetalle;
    }

    public void setLstObservacionesDetalle(List<Map> lstObservacionesDetalle) {
        this.lstObservacionesDetalle = lstObservacionesDetalle;
    }

    public boolean isCambioLocal() {
        return cambioLocal;
    }

    public void setCambioLocal(boolean cambioLocal) {
        this.cambioLocal = cambioLocal;
    }

    public boolean isCambioInstructor() {
        return cambioInstructor;
    }

    public void setCambioInstructor(boolean cambioInstructor) {
        this.cambioInstructor = cambioInstructor;
    }

    public boolean isCambioHorario() {
        return cambioHorario;
    }

    public void setCambioHorario(boolean cambioHorario) {
        this.cambioHorario = cambioHorario;
    }

    public List<SspPrograHora> getLstCambioHorario() {
        return lstCambioHorario;
    }

    public void setLstCambioHorario(List<SspPrograHora> lstCambioHorario) {
        this.lstCambioHorario = lstCambioHorario;
    }

    public String getArchivoNotas() {
        return archivoNotas;
    }

    public void setArchivoNotas(String archivoNotas) {
        this.archivoNotas = archivoNotas;
    }

    public String getArchivoNotasTemporal() {
        return archivoNotasTemporal;
    }

    public void setArchivoNotasTemporal(String archivoNotasTemporal) {
        this.archivoNotasTemporal = archivoNotasTemporal;
    }

    public String getFechasHorarioCurso() {
        return fechasHorarioCurso;
    }

    public void setFechasHorarioCurso(String fechasHorarioCurso) {
        this.fechasHorarioCurso = fechasHorarioCurso;
    }

    public boolean isEsEvento() {
        return esEvento;
    }

    public void setEsEvento(boolean esEvento) {
        this.esEvento = esEvento;
    }

    public List<TipoSeguridad> getLstModalidades() {
        return lstModalidades;
    }

    public void setLstModalidades(List<TipoSeguridad> lstModalidades) {
        this.lstModalidades = lstModalidades;
    }

    public String getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public String getUsuarioArchivar() {
        return usuarioArchivar;
    }

    public void setUsuarioArchivar(String usuarioArchivar) {
        this.usuarioArchivar = usuarioArchivar;
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
    public List<SspRegistroCurso> getSelectItems() {
        return ejbSspRegistroCursoFacade.findAll();
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
//        resultados = new ListDataModel(sspRegistroCursoFacade.selectLike(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     * @param reg
     * @param flagEsInstructor
     */
    public void mostrarVer(SspRegistroCurso reg, boolean flagEsInstructor) {
        reiniciarValores();
        registro = reg;
        esInstructor = flagEsInstructor;
        if(esInstructor){
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            instructorPersona = ejbSbPersonaFacade.buscarPersonaSel("", p_user.getLogin());
        }
        
        cargarDatosCurso(reg);
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     * @param reg
     */
    public void mostrarEditar(SspRegistroCurso reg) {
        reiniciarValores();
        registro = reg;
        
        cargarDatosCurso(reg);
        validarFechaCorte(reg);
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     * @return 
     */
    public String mostrarCrear() {
        reiniciarValores();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        

        if(validaMostrarCrear(p_user.getNumDoc())){
            estado = EstadoCrud.PREVIO;
            return "/aplicacion/gssp/sspRegistroCurso/previoCreate";
        }
        return null;
    }
    
    public void asignarDatoVirtual (){
        ubigeoLocal = ejbSbDistritoFacade.findNombre("LIMA");
        tipoUbicacionLocal = ejbTipoBaseFacade.buscaTipoBaseXCodProg("TP_VIA_VIRT");
    }
    
    public boolean validaMostrarCrear(String ruc){
        boolean validacion = true;
        
        if(!validarCefeoesDpto(ruc)){  //!verificarModalidadesCC
            validacion = false;
            JsfUtil.mensajeError("Ud. no puede ingresar a esta opción porque no cuenta con autorización CEFOES o DEPARTAMENTO");
        }
        
        return validacion;
    }

    public String direccionarMostrarCrear(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        
        SbPersonaGt admin = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        
        if(validaMostrarCrear(p_user.getNumDoc())){
            validarNuevoLocal(null);
            estado = EstadoCrud.CREAR;
            asignarDatoVirtual();
            registro.setId(null);
            registro.setActivo(JsfUtil.TRUE);
            registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            registro.setAudNumIp(JsfUtil.getIpAddress());
            registro.setAdministradoId(admin);
            registro.setFechaRegistro(new Date());
            registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
            registro.setSspCursoEventoList(new ArrayList());
            registro.setSspProgramacionList(new ArrayList());
            registro.setSspAlumnoCursoList(new ArrayList());
            registro.setSspInstructorModuloList(new ArrayList());
            registro.setSspNotasList(new ArrayList());
            if(isSedeLima){
                DIAS_PRESENTACION = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoLima").getValor());    
            }else{
                DIAS_PRESENTACION = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoProvincia").getValor());
            }
            calcularFechaMinima();
            cargarParametrosGlobales();
            return "/aplicacion/gssp/sspRegistroCurso/Create";
        }
        return null;
    }
    
    
    /**
     * Evento para guardar la información al editar un registro.
     *
     * @return 
     */
    public String editar() {
        Long idCursoTemp = 0L;
        try {
            SspRegistroCurso cursoTemp = ejbSspRegistroCursoFacade.find(registro.getId());
            idCursoTemp = registro.getId();
            if(validaCursos() && completarDatosRegistroCursos(cursoTemp)){
                String aux = registro.getExcepcion();
                registro = (SspRegistroCurso) JsfUtil.entidadMayusculas(registro, "");
                registro.setExcepcion(aux);
                //// LOCALES ////
                Long idTemp = 0L;
                for(SspLocal loc : lstLocales){
                    if(loc.getId() < 0){
                        idTemp = loc.getId();
                        loc.setId(null);
                        ejbSspLocalFacade.create(loc);

                        for(SspProgramacion prog : lstProgramacion){
                            if(Objects.equals(prog.getLocalId().getId(), idTemp)){
                                prog.setLocalId(loc);
                            }
                        }
                    }else{
                        ejbSspLocalFacade.edit(loc);
                    }
                }
                ////////////////
                
                //// INSTRUCTORES ////
                idTemp = 0L;
                for(SspInstructor inst : lstInstructores){
                    if(inst.getId() == null){
                        if(inst.getPersonaId().getId() == null){
                            ejbSbPersonaFacade.create(inst.getPersonaId());
                        }else{
                            ejbSbPersonaFacade.edit(inst.getPersonaId());
                        }
                        if(inst.getEmpresaId() != null && inst.getEmpresaId().getId() == null){
                            ejbSbPersonaFacade.create(inst.getEmpresaId());
                        }
                        inst.setId(null);
                        ejbSspInstructorFacade.create(inst);
                    }else{
                        ejbSspInstructorFacade.edit(inst);
                    }
                }
                //////////////////////
                
                //////// FOTOS ///////
                String fileName = "";                            
                for(SspAlumnoCurso alumno : lstAlumnosVigilantes){
                    if(alumno.getFotoId() != null && alumno.getId() < 0){
                        for(Map nmap : lstFotosAlumnos){
                            if(Objects.equals((Long) nmap.get("id"), alumno.getId())){
                                fileName = nmap.get("nombreFoto").toString();
                                fileName = "GSSP_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_FOTC").toString()+fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                                fileName = fileName.toUpperCase();
                                alumno.getFotoId().setNombreFoto(fileName);
                                FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor() + fileName ), (byte[]) nmap.get("fotoByte") );
                            }   
                        }
                    }
                }
                /////////////////////
                
                 ///////////////// VIGILANTES ////////////////
                JsfUtil.borrarIds(lstAlumnosVigilantes);
                registro.setSspAlumnoCursoList(lstAlumnosVigilantes);
                
                for(SspAlumnoCurso alumno : registro.getSspAlumnoCursoList()){
                    if(alumno.getPersonaId().getId() == null){
                        ejbSbPersonaFacade.create(alumno.getPersonaId());
                    }else{
                        ejbSbPersonaFacade.edit(alumno.getPersonaId());
                    }
                }
                //////////////////////////////////////////////
                //adicionaCursoEvento(registro.getEstadoId(), "");
                registro.setSspProgramacionList(lstProgramacion);
                
                ejbSspRegistroCursoFacade.edit(registro);                
                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado") + " Código: " + registro.getId());    
                return prepareList();
            }
            
        } catch (Exception e) {
            System.err.println("ID Curso: " + idCursoTemp);
            e.printStackTrace();            
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }
    
    //validaciones temporales por Curso Virtual:
    public void editarPlataforma(){
        boolean validacion = true;
        
        if(!declaracion){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:declaracion");
            JsfUtil.mensajeError("La declaración jurada es indispensable para registrar la plataforma");
        }
        if(caracteristicas == null || caracteristicas.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:caracteristicas");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_descripcion"));
        }
        if(referenciaLocal == null || referenciaLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:referenciaLocal");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_responsable"));
        }
        if(telefonoLocal == null || telefonoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:telefono");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_telefono"));
        }
        if(correoLocal == null || correoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:correoElectronico");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_correo"));
        }
        
        if(nombreLocal == null || nombreLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:nombreLocal");
            JsfUtil.mensajeError("Por favor ingrese el nombre de la Plataforma Virtual"); //Cambio temporal por Curso Virtual
        }
        
        if(caracteristicas == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:caracteristicas");
            JsfUtil.mensajeError("Por favor ingrese las caracteristicas y las herramientas");
        }else {
            int exceso = caracteristicas.trim().length()-1400;
            if (exceso>0){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:caracteristicas");
                JsfUtil.mensajeError("El texto es grande, borrar "+ exceso+" letra(s) por favor");
            }
        }
        
        if(aforoLocal == null || aforoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:aforoTotal");
            JsfUtil.mensajeError("Por favor ingrese el aforo total del local.");
        }else{
            if(aforoLocal != null && Integer.parseInt(aforoLocal) <= 0){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:aforoTotal");
                JsfUtil.mensajeError("Por favor ingrese el aforo total del local.");
            }
        }
        if (validacion){
            try {
                for(SspLocal loc : lstLocales){
                    loc.setNombreLocal(nombreLocal);
                    loc.setCaracteristicas(caracteristicas);
                    loc.setDireccion(direccionLocal);
                    loc.setAforo(Long.parseLong(aforoLocal));
                    loc.setReferencia(referenciaLocal);
                    //telefonoLocal 
                    //correoLocal 
                    ejbSspLocalFacade.edit(loc);
                    break;
                }
                JsfUtil.mensaje("Los datos de la Plataforma fueron actualizados");
            }catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin()+"  Id Registro: "+ registro.getId());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
            }
        }
    }
    /**
     * Evento para guardar la información al crear un nuevo registro.
     * @return 
     */
    public String crear() {
        try {
            if(validaCursos() && completarDatosRegistroCursos(null)){
                String aux = registro.getExcepcion();
                registro = (SspRegistroCurso) JsfUtil.entidadMayusculas(registro, "");
                registro.setExcepcion(aux);
                //// Locales ////
                Long idTemp = 0L;
                for(SspLocal loc : lstLocales){
                    if(loc.getId() < 0){
                        idTemp = loc.getId();
                        loc.setId(null);
                        ejbSspLocalFacade.create(loc);

                        for(SspProgramacion prog : lstProgramacion){
                            if(Objects.equals(prog.getLocalId().getId(), idTemp)){
                                prog.setLocalId(loc);
                            }
                        }
                    }else{
                        ejbSspLocalFacade.edit(loc);
                    }
                }
                ////////////////
                
                //// Instructores ////
                idTemp = 0L;
                for(SspInstructor inst : lstInstructores){
                    if(inst.getId() == null){
                        if(inst.getPersonaId().getId() == null){
                            ejbSbPersonaFacade.create(inst.getPersonaId());
                        }else{
                            ejbSbPersonaFacade.edit(inst.getPersonaId());
                        }
                        if(inst.getEmpresaId() != null && inst.getEmpresaId().getId() == null){
                            ejbSbPersonaFacade.create(inst.getEmpresaId());
                        }
                        inst.setId(null);
                        ejbSspInstructorFacade.create(inst);
                    }else{
                        ejbSspInstructorFacade.edit(inst);
                    }
                }
                //////////////////////
                
                //////// FOTOS ///////
                String fileName = "";
                String ruta = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor();
                for(SspAlumnoCurso alumno : lstAlumnosVigilantes){
                    if(alumno.getFotoId() != null && alumno.getId() < 0){
                        for(Map nmap : lstFotosAlumnos){
                            if(Objects.equals((Long) nmap.get("id"), alumno.getId())){
                                fileName = nmap.get("nombreFoto").toString();
                                fileName = "GSSP_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_FOTC").toString()+fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                                fileName = fileName.toUpperCase();
                                alumno.getFotoId().setNombreFoto(fileName);
                                FileUtils.writeByteArrayToFile(new File(ruta + fileName ), (byte[]) nmap.get("fotoByte") );
                            }   
                        }
                    }
                }
                /////////////////////
                            
                ///////////////// VIGILANTES ////////////////
                JsfUtil.borrarIds(lstAlumnosVigilantes);                              
                registro.setSspAlumnoCursoList(lstAlumnosVigilantes);
                
                for(SspAlumnoCurso alumno : registro.getSspAlumnoCursoList()){
                    if(alumno.getPersonaId().getId() == null){
                        ejbSbPersonaFacade.create(alumno.getPersonaId());
                    }else{
                        ejbSbPersonaFacade.edit(alumno.getPersonaId());
                    }
                }
                //////////////////////////////////////////////
                
                adicionaCursoEvento(registro.getEstadoId(), "");
                registro.setSspProgramacionList(lstProgramacion);
                registro.setId(null);
                registro.setTipoOpeId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_OPE_INI"));
                registro.setTipoServicio(tipoServicio);
                ejbSspRegistroCursoFacade.create(registro);
                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado") + ", Código: " + registro.getId());
                return prepareList();
            }
        } catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }    
    
    public Date calcularFechaFinalSinSabadoDomingo(Date fechaIni, Calendar c_ffin){
        Calendar c_hoy = Calendar.getInstance();
        int feriados = -1;
        int domingos = -1;
        while( (feriados + domingos) != 0){
            feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(fechaIni), JsfUtil.getFechaSinHora(c_ffin.getTime()));
            domingos = JsfUtil.calcularNroDiasSabadoYDomingoRango(JsfUtil.getFechaSinHora(fechaIni), JsfUtil.getFechaSinHora(c_ffin.getTime()));
            
            if((feriados + domingos) > 0){
                c_hoy.setTime(c_ffin.getTime());
                c_hoy.add(Calendar.DATE, 1);
                fechaIni = c_hoy.getTime();
                c_ffin.add(Calendar.DATE, (feriados + domingos));
                
                if(JsfUtil.getFechaSinHora(fechaIni).compareTo(JsfUtil.getFechaSinHora(c_ffin.getTime())) == 0){
                    int diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
                    if(diaSemana == 1){
                        c_hoy.setTime(fechaIni);
                        c_hoy.add(Calendar.DATE, 1);
                        fechaIni = c_hoy.getTime();
                    }
                }
            }
        }
        return c_ffin.getTime();
    }
    
    public void calcularFechaMinima(){
        Calendar c_ffin = Calendar.getInstance();
        
        if(registro != null && ( registro.getEstadoId().getCodProg().equals("TP_ECC_OBS") || estado == EstadoCrud.EDITAR  )){
            c_ffin.setTime(registro.getFechaRegistro());
        }
        Date fechaIni = c_ffin.getTime();
        c_ffin.add(Calendar.DATE, DIAS_PRESENTACION);
       
        fechaMinima = calcularFechaFinalSinSabadoDomingo(fechaIni, c_ffin );
    }
    
    public void cargarDatosCurso(SspRegistroCurso cRegistro){
        
        /////////////// LOCALES ///////////////
        String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");
        revisarSiEsVirtual(cRegistro);
        for(SspProgramacion progra : cRegistro.getSspProgramacionList()){
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                continue;
            }
            if(progra.getActivo() == 1 && !lstLocales.contains(progra.getLocalId()) ){
                lstLocales.add(progra.getLocalId());
            }
            if (esVirtual){
                nombreLocal = progra.getLocalId().getNombreLocal();
                caracteristicas = progra.getLocalId().getCaracteristicas();
                direccionLocal = progra.getLocalId().getDireccion();
                aforoLocal = progra.getLocalId().getAforo().toString();
                referenciaLocal = progra.getLocalId().getReferencia();
                telefonoLocal = obtenerValorContactoTablaLocal(progra.getLocalId().getSspLocalContactoList(),"TP_MEDIOC_TEL");
                correoLocal = obtenerValorContactoTablaLocal(progra.getLocalId().getSspLocalContactoList(),"TP_MEDIOC_COR");
                declaracion = true;
                break;
            }
        }
        
        DIAS_CAMBIO_LOCAL = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasCambioLocal").getValor());
        for(SspProgramacion progra : cRegistro.getSspProgramacionList()){
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                continue;
            }
            if(progra.getActivo() == 1 && !excepcionModulos.contains(progra.getModuloId().getCodModulo()) ){
                DIAS_PRESENTACION = ((progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 7 || progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 15)?(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoLima").getValor())):(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoProvincia").getValor())));
                break;
            }
        }
        calcularFechaMinima();
        //////////////////////////////////////
        
        /////////////// INSTRUCTORES ///////////////
        int cont = 0;
        Map nmap;
        List<SspInstructor> lstInstructorTemp = new ArrayList();

        for(SspProgramacion prog : cRegistro.getSspProgramacionList()){
            if(prog.getActivo() == 1 && !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")){
                tipoFormacion = prog.getModuloId().getTipoCursoId();
                break;
            }
        }
        esEvento = false;
        if(tipoFormacion != null && (tipoFormacion.getCodProg().equals("TP_FORMC_BASEVNT") || tipoFormacion.getCodProg().equals("TP_FORMC_PRFEVNT"))){
            esEvento = true;
        }
        
        cambiarTipoFormacion();
        isSedeLima = false;
        
        for(SspProgramacion progra : cRegistro.getSspProgramacionList()){
            if(progra.getActivo() == 1){
                for(SspInstructorModulo inst : progra.getModuloId().getSspInstructorModuloList() ){
                    if(inst.getActivo() == 1 && Objects.equals(inst.getRegcursoId().getId(), cRegistro.getId() ) && !lstInstructorTemp.contains(inst.getInstructorId()) ){
                        lstInstructorTemp.add(inst.getInstructorId());
                    }
                }
                if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                    for(SspPrograHora hora : progra.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            horaInicioRefrigerio = hora.getHoraInicio();
                            horaFinRefrigerio = hora.getHoraFin();
                        }
                    }
                }
                
                if(!progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP") &&
                    !excepcionModulos.contains(progra.getModuloId().getCodModulo()) &&
                    progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() != 0 &&
                        (
                            progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 7 || 
                            progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 15
                        )
                   ){
                     isSedeLima = true;
                 }
            }
        }
        for(SspInstructor inst : lstInstructorTemp){
            nmap = new HashMap();
            nmap.put("SspInstructor", inst );
            nmap.put("ID", inst.getId() );
            nmap.put("TIP_USR", ((inst.getPersonaId().getTipoDoc() != null && inst.getPersonaId().getTipoDoc().getCodProg().equals("TP_DOCID_CE"))?"5":"2") );
            nmap.put("COD_USR", inst.getPersonaId().getNumDoc() );
            nmap.put("NUM_DOC", inst.getPersonaId().getNumDoc() );
            nmap.put("FECHA_NAC", inst.getPersonaId().getFechaNac() );
            if(inst.getPersonaId().getGeneroId() != null){
                nmap.put("SEXO", ((inst.getPersonaId().getGeneroId().getCodProg().equals("TP_GEN_MAS"))?"M":"F") );
            }else{
                nmap.put("SEXO", null );
            }
            nmap.put("APELLIDOS", inst.getPersonaId().getApePat() + " " + inst.getPersonaId().getApeMat() );
            nmap.put("NOMBRES", inst.getPersonaId().getNombres() );
            nmap.put("NOMBRES_APELLIDOS", inst.getPersonaId().getNombreCompleto());
            nmap.put("APE_PAT", inst.getPersonaId().getApePat() );
            nmap.put("APE_MAT", inst.getPersonaId().getApeMat() );
            nmap.put("NRO_FICHA", inst.getNroFicha() );
            nmap.put("FECHA_EMISION", inst.getFechaEmicion() );
            nmap.put("FECHA_CADUCIDAD", inst.getFechaVencimiento() );
            if(inst.getPersonaId().getSbMedioContactoList() != null && !inst.getPersonaId().getSbMedioContactoList().isEmpty()){
                int contTelefono = 0, contEmail = 0;
                for(SbMedioContactoGt medio : inst.getPersonaId().getSbMedioContactoList()){
                    if(medio.getActivo() == 1 && medio.getTipoId().getCodProg().equals("TP_MEDCO_MOV")){
                        nmap.put("TELEFONO", medio.getValor() );
                        contTelefono ++;
                        break;
                    }
                }
                if(contTelefono == 0){
                    nmap.put("TELEFONO", null );
                }
                for(SbMedioContactoGt medio : inst.getPersonaId().getSbMedioContactoList()){
                    if(medio.getActivo() == 1 && medio.getTipoId().getCodProg().equals("TP_MEDCO_COR")){
                        nmap.put("EMAIL", medio.getValor() );
                        contEmail ++;
                    }
                }
                if(contEmail == 0){
                    nmap.put("EMAIL", null );
                }
            }
            nmap.put("RUC", ((inst.getEmpresaId() != null)?inst.getEmpresaId().getRuc():null) );
            nmap.put("RZN_SOC_EMPRESA", ((inst.getEmpresaId() != null)?inst.getEmpresaId().getRznSocial():null) );
            nmap.put("TELF_EMPRESA", null );
            if(lstModulos != null){
                for(SspModulo modSelect : lstModulos){
                    cont = 0;                    
                    if(cRegistro.getEstadoId().getCodProg().equals("TP_ECC_EVA") || cRegistro.getEstadoId().getCodProg().equals("TP_ECC_ACT") || cRegistro.getEstadoId().getCodProg().equals("TP_ECC_FIN")){
                        for(SspProgramacion progra : cRegistro.getSspProgramacionList()){
                            if(progra.getActivo() == 1 && progra.getInstructorId() != null && 
                               Objects.equals(progra.getModuloId().getId(), modSelect.getId()) &&
                               Objects.equals(progra.getInstructorId().getId(), inst.getId())){
                                cont++;
                                nmap.put(modSelect.getCodModulo(), "X");
                            }
                        }
                    }else{
                        for(SspInstructorModulo modInst : inst.getSspInstructorModuloList()){
                            if( modInst.getActivo() == 1 && Objects.equals(modInst.getModuloId().getId(), modSelect.getId()) && Objects.equals(cRegistro.getId(), modInst.getRegcursoId().getId() ) ){
                                cont++;
                                nmap.put(modSelect.getCodModulo(), "X");
                            }
                        }
                    }
                    if(cont == 0){
                        nmap.put(modSelect.getCodModulo(), null);
                    }
                }
            }
            lstInstructoresTabla.add(nmap);
        }
        getModulosFiltrados();
        if(!lstInstructoresTabla.isEmpty()){
            setDisabledTipoFormacion(true);
        }
        ///////////////////////////////////////////
        tipoServicio = cRegistro.getTipoServicio();
        
        
        /////////////// AGENDAR ///////////////
        lstProgramacion = cRegistro.getSspProgramacionList();
//        for(SspProgramacion progra : cRegistro.getSspProgramacionList()){
//            if(progra.getActivo() == 1){                
//                List<SspPrograHora> lstHorarios = new ArrayList();
//                for(SspPrograHora horario : progra.getSspPrograHoraList()){
//                    if(horario.getActivo() == 1){
//                        lstHorarios.add(horario);
//                    }
//                }
//                progra.setSspPrograHoraList(lstHorarios);
//                lstProgramacion.add(progra);
//            }
//        }
        ///////////////////////////////////////
        
        
        /////////////// VIGILANTE ///////////////
        lstAlumnosVigilantes = cRegistro.getSspAlumnoCursoList();
        /////////////////////////////////////////
        
        /////////////// OBSERVACIONES ///////////
        Date fechaSubsanacion = null;
        List<SspCursoEvento> lstObservacionTemp;
        Collections.sort(cRegistro.getSspCursoEventoList(), new Comparator<SspCursoEvento>() {
            @Override
            public int compare(SspCursoEvento one, SspCursoEvento other) {
                return other.getFecha().compareTo(one.getFecha());
            }
        });
        lstObservacionTemp = new ArrayList(cRegistro.getSspCursoEventoList());
        
        for(SspCursoEvento ce : cRegistro.getSspCursoEventoList()){
            if(ce.getActivo() == 0){
                continue;
            }
            if(ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")){
                lstObservaciones.add(ce);
                
                Map obs = new HashMap();
                obs.put("id", ce.getId());
                obs.put("tipo", ce.getTipoEventoId().getNombre());
                obs.put("fechaObs", ce.getFecha());
                obs.put("descripcion", ce.getObservacion());
                fechaSubsanacion = null;
                
                for(SspCursoEvento ceTot : lstObservacionTemp){
                    if(ceTot.getActivo() == 1 && ceTot.getTipoEventoId().getCodProg().equals("TP_ECC_TRA") && ceTot.getId() > ce.getId()){
                        fechaSubsanacion = ceTot.getFecha();
                        lstObservacionTemp.remove(ceTot);
                        break;
                    }
                }
                obs.put("fechaSub", ((fechaSubsanacion != null)?fechaSubsanacion:"-"));
                lstObservacionesDetalle.add(obs);
            }
            if(ce.getTipoEventoId().getCodProg().equals("TP_ECC_NPR") || ce.getTipoEventoId().getCodProg().equals("TP_ECC_FDP") || ce.getTipoEventoId().getCodProg().equals("TP_ECC_DES")){
                Map obs = new HashMap();
                obs.put("id", ce.getId());
                obs.put("tipo", ce.getTipoEventoId().getNombre());
                obs.put("fechaObs", ce.getFecha());
                obs.put("descripcion", ce.getObservacion());
                obs.put("fechaSub", "-");
                lstObservacionesDetalle.add(obs);
            }
        }
        /////////////////////////////////////////
    }
    
    public List<SspCursoEvento> getLstObservacionesOrdenadasDesc(){
        if(lstObservaciones != null){
            Collections.sort(lstObservaciones, new Comparator<SspCursoEvento>() {
                @Override
                public int compare(SspCursoEvento one, SspCursoEvento other) {
                    return other.getFecha().compareTo(one.getFecha());
                }
            });
        }
        return lstObservaciones;
    }
    
    public void revertirOperacion(){
        
        ///// Reversión de locales /////
        for(SspLocal loc : lstLocales){
            if(loc.getId() != null){
                loc.setActivo(JsfUtil.FALSE);
            }
            ejbSspLocalFacade.edit(loc);
        }  
        ///////////////////////////////
    }
    
    public boolean completarDatosRegistroCursos(SspRegistroCurso cRegistroOriginal){
        boolean validacion = true;
        
        try {
            ///////////// Instructores /////////////
            lstInstructores = new ArrayList();
            for(Map nmap : lstInstructoresTabla){
                SspInstructor instructor = new SspInstructor();   
                SbPersonaGt per = null;
                
                if(nmap.get("ID") != null){
                    //Instructor ya existe
                    instructor = (SspInstructor) nmap.get("SspInstructor");
                }else{
                    //////////// PersonaID ////////////
                    per = ejbSbPersonaFacade.buscarPersonaSel("", ""+nmap.get("NUM_DOC"));
                    if(per != null){
                        // Persona ya existe
                    }else{
                        per = new SbPersonaGt();
                        per.setActivo(JsfUtil.TRUE);
                        per.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        per.setAudNumIp(JsfUtil.getIpAddress());
                        per.setId(null);
                        per.setNumDoc(""+nmap.get("NUM_DOC"));
                        per.setNombres(""+nmap.get("NOMBRES"));
                        per.setApePat(""+nmap.get("APE_PAT"));
                        per.setApeMat(""+nmap.get("APE_MAT"));
                        per.setFechaNac((Date) nmap.get("FECHA_NAC"));
                        per.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));

                        if(nmap.get("TIP_USR").toString().equals("5")){
                            per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_CE"));
                        }else{
                            per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                        }
                        if(nmap.get("SEXO") != null && nmap.get("SEXO").toString().equals("M")){
                            per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GEN_MAS"));
                        }else{
                            if(nmap.get("SEXO") != null && nmap.get("SEXO").toString().equals("F")){
                                per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GEN_FEM"));
                            }
                        }
                        per.setRuc(null);
                        per.setRznSocial(null);
                        per.setSbMedioContactoList(new ArrayList());
                        if(nmap.get("TELEFONO") != null){
                            SbMedioContactoGt medioTelefono = new SbMedioContactoGt();
                            medioTelefono.setActivo(JsfUtil.TRUE);
                            medioTelefono.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            medioTelefono.setAudNumIp(JsfUtil.getIpAddress());
                            medioTelefono.setId(null);
                            medioTelefono.setPersonaId(per);
                            medioTelefono.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_MOV"));
                            medioTelefono.setValor(nmap.get("TELEFONO").toString());
                            medioTelefono.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                            per.getSbMedioContactoList().add(medioTelefono);
                        }
                        if(nmap.get("EMAIL") != null){
                            SbMedioContactoGt medioCorreo = new SbMedioContactoGt();
                            medioCorreo.setActivo(JsfUtil.TRUE);
                            medioCorreo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            medioCorreo.setAudNumIp(JsfUtil.getIpAddress());
                            medioCorreo.setId(null);
                            medioCorreo.setPersonaId(per);
                            medioCorreo.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_COR"));
                            medioCorreo.setValor(nmap.get("EMAIL").toString());
                            medioCorreo.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                            per.getSbMedioContactoList().add(medioCorreo);
                        }
                        per = (SbPersonaGt) JsfUtil.entidadMayusculas(per, "");
                    }
                    
                    instructor.setPersonaId(per);
                    instructor.setActivo(JsfUtil.TRUE);
                    instructor.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    instructor.setAudNumIp(JsfUtil.getIpAddress());
                    instructor.setEstadoInstruId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_EIN_VIG"));
                    instructor.setId(JsfUtil.tempId());
                    instructor.setSspInstructorModuloList(new ArrayList());
                    
                     //////////// Empresa ////////////
                    per = null;
                    if(nmap.get("RUC") != null && !nmap.get("RUC").toString().equals("0")){
                        per = ejbSbPersonaFacade.buscarPersonaSel("", ""+nmap.get("RUC"));
                        if(per == null){
                            per = new SbPersonaGt();
                            per.setActivo(JsfUtil.TRUE);
                            per.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            per.setAudNumIp(JsfUtil.getIpAddress());
                            per.setId(null);
                            per.setNumDoc(null);
                            per.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_JUR"));
                            per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_RUC"));                 
                            per.setRuc(""+nmap.get("RUC"));
                            per.setRznSocial(""+nmap.get("RZN_SOC_EMPRESA"));
                            per = (SbPersonaGt) JsfUtil.entidadMayusculas(per, "");
                        }
                        instructor.setEmpresaId(per);
                    }   
                    /////////////////////////////////////////////
                }
                instructor.setFechaEmicion((Date)nmap.get("FECHA_EMISION"));
                instructor.setFechaVencimiento((Date)nmap.get("FECHA_CADUCIDAD"));
                instructor.setNroFicha((Long) nmap.get("NRO_FICHA"));
                if(registro.getSspInstructorModuloList() == null){
                    registro.setSspInstructorModuloList(new ArrayList());
                }
                /////////////////////////////////////////////
                
                ////////////// Modulos //////////////
                for(SspModulo modulo : lstModulos){
                    if(nmap.get(modulo.getCodModulo()) != null){
                        boolean validaNuevoModulo = true;
                        
                        for(SspInstructorModulo instModulo : registro.getSspInstructorModuloList()){
                            if(Objects.equals(instModulo.getModuloId().getId(), modulo.getId()) && 
                               Objects.equals(instModulo.getInstructorId().getId(), instructor.getId()) &&
                               Objects.equals(registro.getId(), instModulo.getRegcursoId().getId()) &&
                               instModulo.getActivo() == 1  ){
                                validaNuevoModulo = false;
                            }
                        }
                        if(validaNuevoModulo){
                            SspInstructorModulo nuevoInstModulo = new SspInstructorModulo();
                            nuevoInstModulo.setActivo(JsfUtil.TRUE);
                            nuevoInstModulo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            nuevoInstModulo.setAudNumIp(JsfUtil.getIpAddress());
                            nuevoInstModulo.setFecha(new Date());
                            nuevoInstModulo.setId(null);
                            nuevoInstModulo.setInstructorId(instructor);
                            nuevoInstModulo.setModuloId(modulo);
                            nuevoInstModulo.setRegcursoId(registro);
                            registro.getSspInstructorModuloList().add(nuevoInstModulo);
                        }
                    }
                }
                /////////////////////////////////////////////
                instructor = (SspInstructor) JsfUtil.entidadMayusculas(instructor, "");
                lstInstructores.add(instructor);
            }
            JsfUtil.borrarIds(lstInstructores);
            //////////////////////////////////////////////
            
            if(cRegistroOriginal == null){
                if(horaInicioRefrigerio != null && horaFinRefrigerio != null){
                    List<SspProgramacion> lstTemp = new ArrayList(lstProgramacion);
                    Collections.sort(lstTemp, new Comparator<SspProgramacion>() {
                        @Override
                        public int compare(SspProgramacion one, SspProgramacion other) {
                            return one.getFechaHoraIncio().compareTo(other.getFechaHoraIncio());
                        }
                    });
                    fechaHorario = JsfUtil.getFechaSinHora(lstTemp.get(0).getFechaHoraIncio());
                    Calendar fechaTemp = Calendar.getInstance();
                    fechaTemp.set(JsfUtil.formatoFechaYyyy(fechaHorario), fechaHorario.getMonth(), fechaHorario.getDate(),horaInicioRefrigerio.getHours(), horaInicioRefrigerio.getMinutes(),0 );
                    horaInicioRefrigerio = (fechaTemp.getTime());
                    fechaTemp.set(JsfUtil.formatoFechaYyyy(fechaHorario), fechaHorario.getMonth(), fechaHorario.getDate(),horaFinRefrigerio.getHours(), horaFinRefrigerio.getMinutes(),0 );
                    horaFinRefrigerio = (fechaTemp.getTime());
                    boolean tieneRefrig = false;
                    for(SspProgramacion progra : lstProgramacion){
                        if(progra.getActivo() == 1 && progra.getModuloId().getCodModulo().equals("REFB") && progra.getModuloId().getCodModulo().equals("REFP") ){
                            tieneRefrig = true;
                            break;
                        }
                    }
                    
                    if(!tieneRefrig){
                        SspProgramacion progra = new SspProgramacion();
                        progra.setId(JsfUtil.tempId());
                        progra.setActivo(JsfUtil.TRUE);
                        progra.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        progra.setAudNumIp(JsfUtil.getIpAddress());
                        progra.setFechaHoraIncio(lstTemp.get(0).getFechaHoraIncio());
                        progra.setFechaHoraFin(lstTemp.get(lstTemp.size()-1).getFechaHoraFin());
                        progra.setLocalId(lstProgramacion.get(0).getLocalId());
                        progra.setModuloId(ejbSspModuloFacade.obtenerModuloByCodMoulo("REFB"));
                        progra.setRegistroCursoId(registro);
                        progra.setSspPrograHoraList(new ArrayList());

                        SspPrograHora horario = new SspPrograHora();
                        horario.setActivo(JsfUtil.TRUE);
                        horario.setFechaReg(new Date());
                        horario.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        horario.setAudNumIp(JsfUtil.getIpAddress());
                        horario.setId(JsfUtil.tempId());
                        horario.setFecha(fechaHorario);
                        horario.setHoraInicio(horaInicioRefrigerio);
                        horario.setHoraFin(horaFinRefrigerio);
                        horario.setTipoCurso(null);
                        horario.setProgramacionId(progra);
                        progra.getSspPrograHoraList().add(horario);
                        lstProgramacion.add(progra);
                    }else{
                        for(SspProgramacion progra : lstProgramacion){
                            if(progra.getActivo() == 1 && (progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP"))){
                                for(SspPrograHora hora : progra.getSspPrograHoraList()){
                                    if(hora.getActivo() == 1){
                                        hora.setHoraInicio(horaInicioRefrigerio);
                                        hora.setHoraFin(horaFinRefrigerio);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                if(horaInicioRefrigerio != null && horaFinRefrigerio != null){
                    List<SspProgramacion> lstTemp = new ArrayList(lstProgramacion);
                    Collections.sort(lstTemp, new Comparator<SspProgramacion>() {
                        @Override
                        public int compare(SspProgramacion one, SspProgramacion other) {
                            return one.getFechaHoraIncio().compareTo(other.getFechaHoraIncio());
                        }
                    });
                    fechaHorario = JsfUtil.getFechaSinHora(lstTemp.get(0).getFechaHoraIncio());
                    Calendar fechaTemp = Calendar.getInstance();
                    fechaTemp.set(JsfUtil.formatoFechaYyyy(fechaHorario), fechaHorario.getMonth(), fechaHorario.getDate(),horaInicioRefrigerio.getHours(), horaInicioRefrigerio.getMinutes(),0 );
                    horaInicioRefrigerio = (fechaTemp.getTime());
                    fechaTemp.set(JsfUtil.formatoFechaYyyy(fechaHorario), fechaHorario.getMonth(), fechaHorario.getDate(),horaFinRefrigerio.getHours(), horaFinRefrigerio.getMinutes(),0 );
                    horaFinRefrigerio = (fechaTemp.getTime());
                    
                    for(SspProgramacion progra : lstProgramacion){
                        if(progra.getActivo() == 1 && progra.getModuloId().getCodModulo().equals("REFB")){
                            for(SspPrograHora hora : progra.getSspPrograHoraList()){
                                if(hora.getActivo() == 1){
                                    hora.setHoraInicio(horaInicioRefrigerio);
                                    hora.setHoraFin(horaFinRefrigerio);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            ////////////////// EDICION //////////////////
            if(cRegistroOriginal != null){
                int cont = 0;
                ///////////// INSTRUCTOR MODULO /////////////
                for(SspInstructorModulo instModOri : cRegistroOriginal.getSspInstructorModuloList()){
                    cont = 0;
                    for(SspInstructorModulo instMod : registro.getSspInstructorModuloList()){
                        if(Objects.equals(instModOri.getId(), instMod.getId())){
                            cont++;
                        }
                    }
                    if(cont == 0){
                        instModOri.setActivo(JsfUtil.FALSE);
                        registro.getSspInstructorModuloList().add(instModOri);
                    }
                }
                
                ///////////// PROGRAMACIÓN HORAS /////////////
                cont = 0;
                for(SspProgramacion prograOri : cRegistroOriginal.getSspProgramacionList()){
                    cont = 0;
                    for(SspProgramacion progra : lstProgramacion){
                        if(Objects.equals(prograOri.getId(), progra.getId())){
                            cont++;
                        }
                    }
                    if(cont == 0){
                        prograOri.setActivo(JsfUtil.FALSE);
                        lstProgramacion.add(prograOri);
                    }
                }
                
                for(SspProgramacion progra : lstProgramacion){
                    for(SspProgramacion prograOri : cRegistroOriginal.getSspProgramacionList()){
                        if(Objects.equals(prograOri.getId(), progra.getId())){
                            for(SspPrograHora horaOri : prograOri.getSspPrograHoraList()){
                                cont = 0;
                                for(SspPrograHora hora : progra.getSspPrograHoraList()){
                                    if(Objects.equals(hora.getId(), horaOri.getId())){
                                        cont++;
                                    }
                                }
                                if(cont == 0){
                                    horaOri.setActivo(JsfUtil.FALSE);
                                    progra.getSspPrograHoraList().add(horaOri);
                                }
                            }
                        }
                    }                        
                }
                // PARA LIMPIAR LA CONFIRMACIÓN DE UN ISNTRUCTOR AL ADICIONAR UN NUEVO HORARIO
                List<SspProgramacion> lstTemp = new ArrayList(lstProgramacion);
                Long moduloId;
                for(SspProgramacion progra : lstTemp){
                    if(progra.getId() == null || progra.getId() < 0){
                        moduloId = null;
                        for(SspProgramacion prograOri : cRegistroOriginal.getSspProgramacionList()){
                            if(Objects.equals(prograOri.getModuloId().getId(), progra.getModuloId().getId())){
                                if(prograOri.getInstructorId() != null){
                                    moduloId = prograOri.getModuloId().getId();
                                    break;
                                }
                            }
                        }
                        if(moduloId != null){
                            for(SspProgramacion prog : lstProgramacion){
                                if(Objects.equals(prog.getModuloId().getId(), moduloId)){
                                    prog.setInstructorId(null);
                                }
                            }
                        }
                    }
                }
                
               ///////////////// VIGILANTES ////////////////
                cont = 0;
                for(SspAlumnoCurso alumnoOri : cRegistroOriginal.getSspAlumnoCursoList()){
                    cont = 0;
                    for(SspAlumnoCurso alumno : registro.getSspAlumnoCursoList()){
                        if(Objects.equals(alumnoOri.getId(), alumno.getId())){
                            cont++;
                        }
                    }
                    if(cont == 0){
                        alumnoOri.setActivo(JsfUtil.FALSE);
                        registro.getSspAlumnoCursoList().add(alumnoOri);
                    }
                }
                //////////////////////////////////////////////
            }
            if (lstProgramacion!=null){
                JsfUtil.borrarIds(lstProgramacion);
                for(SspProgramacion prog : lstProgramacion){
                    JsfUtil.borrarIds(prog.getSspPrograHoraList());
                }
            }
            //////////////////////////////////////////////
            
            if(tipoFormacion.getCodProg().equals("TP_FORMC_BAS") || tipoFormacion.getCodProg().equals("TP_FORMC_PRF")){
                registro.setTipoCurso(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GSSP_CURS"));
            }else{
                registro.setTipoCurso(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GSSP_CURSEVT"));
            }
        } catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            validacion = false;
        }
        
        return validacion;
    }
    
    public boolean validaCursos(){
        boolean validacion = true;
        int cont = 0;     
        int ref=0;
        int refin=0;
        
        if(lstLocales.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingrese al menos un local");
        }
        if(lstInstructoresTabla.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingrese al menos un instructor");
        }
        if (registro.getExcepcion()!=null){
            int exceso = registro.getExcepcion().trim().length()-500;
            if (exceso>0){
                validacion = false;
                JsfUtil.mensajeError("El texto es grande, borrar "+ exceso +" letra(s) por favor");
                tabIndex=2;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:excepcion");
                return validacion;
            }
            
        }
        if(lstProgramacion.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingrese la programación del curso");
        }else{
            List<Long> lstDepartamentos = new ArrayList();
            String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");
            // Validación de ubicación (departamento)
            for(SspProgramacion prog : lstProgramacion){
                if(prog.getActivo() == 1 && 
                   prog.getLocalId() != null && prog.getModuloId() != null &&
                   !excepcionModulos.contains(prog.getModuloId().getCodModulo()) &&
                   !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")
                  ){
                    if(!lstDepartamentos.contains(prog.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId())){
                        lstDepartamentos.add(prog.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId());
                    }
                }
            }
            if(lstDepartamentos.size() > 1){
                if(isSedeLima){
                    for(Long depaId : lstDepartamentos){
                        if(depaId != 15 && depaId != 7){
                            JsfUtil.mensajeError("No puede adicionar un curso cuyo departamento de dictado de clases es distinto al de los otros cursos ya ingresados.");
                            validacion = false;
                            break;
                        }
                    }
                }else{
                    if(lstDepartamentos.size() > 1){
                        JsfUtil.mensajeError("No puede adicionar un curso cuyo departamento de dictado de clases es distinto al de los otros cursos ya ingresados.");
                        validacion = false;
                    }
                }
            }
            
            if(horaInicioRefrigerio == null){
                validacion = false;
                JsfUtil.mensajeError("Por favor ingrese la hora de inicio del refrigerio");
            }
            if(horaFinRefrigerio == null){
                validacion = false;
                JsfUtil.mensajeError("Por favor ingrese la hora de fin del refrigerio");
            }
        }
        if(horaInicioRefrigerio != null ){
           Calendar fechaTemp = Calendar.getInstance();
            fechaTemp.set(JsfUtil.formatoFechaYyyy(horaInicioRefrigerio), horaInicioRefrigerio.getMonth(), horaInicioRefrigerio.getDate(), horaFinPermitido, 0,0 );    // permitido hastas las 16:00
            if(horaInicioRefrigerio.getHours() < horaInicioPermitido || (horaInicioRefrigerio.compareTo(fechaTemp.getTime()) > 0) ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaInicioRefrigerio");
                JsfUtil.mensajeError("El horario permitido es entre las "+horaInicioPermitido+":00 y "+horaFinPermitido+":00 horas ");
            }
        }
        if(horaFinRefrigerio != null ){
           Calendar fechaTemp = Calendar.getInstance();
            fechaTemp.set(JsfUtil.formatoFechaYyyy(horaFinRefrigerio), horaFinRefrigerio.getMonth(), horaFinRefrigerio.getDate(), horaFinPermitido, 0,0 );    // permitido hastas las 16:00
            if(horaFinRefrigerio.getHours() < horaInicioPermitido || (horaFinRefrigerio.compareTo(fechaTemp.getTime()) > 0) ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaFinRefrigerio");
                JsfUtil.mensajeError("El horario permitido es entre las "+horaInicioPermitido+":00 y "+horaFinPermitido+":00 horas ");
            }
        }
        if(horaInicioRefrigerio != null && horaFinRefrigerio != null){
            Calendar fechaTemp = Calendar.getInstance();
            fechaHorario = new Date();            
            fechaTemp.set(JsfUtil.formatoFechaYyyy(fechaHorario), fechaHorario.getMonth(), fechaHorario.getDate(),horaInicioRefrigerio.getHours(), horaInicioRefrigerio.getMinutes(),0 );
            horaInicioRefrigerio = (fechaTemp.getTime());
            fechaTemp.set(JsfUtil.formatoFechaYyyy(fechaHorario), fechaHorario.getMonth(), fechaHorario.getDate(),horaFinRefrigerio.getHours(), horaFinRefrigerio.getMinutes(),0 );
            horaFinRefrigerio = (fechaTemp.getTime());

            if(horaInicioRefrigerio.compareTo(horaFinRefrigerio) > 0){
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaInicioRefrigerio");
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaFinRefrigerio");
                JsfUtil.mensajeError("La hora de inicio no puede ser mayor que la hora final");
                validacion = false;
            }
            long minutos = JsfUtil.calculaMinutosEntreHoras(horaInicioRefrigerio, horaFinRefrigerio);
            int horasTotales = (int) Math.ceil((double) minutos/60);
            if( horasTotales > limiteHorasAcadRef  ){
                JsfUtil.mensajeError("No puede ingresar más de "+limiteHorasAcadRef+" hora de refrigerio");
                validacion = false;
            }
        }
        if(lstModulos != null && lstProgramacion != null && !lstProgramacion.isEmpty()){
            //// Validación de que todos los módulos estén adicionados al listado
            for(SspModulo modulosTotales : lstModulos){
                cont = 0; 
                for(SspProgramacion prog: lstProgramacion){
                    ref=0;
                    if(Objects.equals(prog.getModuloId().getId(), modulosTotales.getId())){
                        cont++;
                    }
                   
                }
                
               if(cont == 0)
                   {
                    validacion = false;
                    JsfUtil.mensajeError("Por favor ingresar todos los cursos del tipo de formación: " + modulosTotales.getTipoCursoId().getNombre());
                    break;
                    }
              
                
            }
             //AGREGADO 13-05-2022
             for(SspProgramacion prog: lstProgramacion){
                    for( SspPrograHora lis: prog.getSspPrograHoraList())
                    {
                        ref=0;
                        Date fec=JsfUtil.getFechaSoloHora(lis.getHoraInicio());
                        Date fec2=JsfUtil.getFechaSoloHora(lis.getHoraFin());
                        //Date fechaRefri=JsfUtil.getFechaSoloHora(horaInicioRefrigerio);
                        Date horaRefriIni=JsfUtil.getFechaSoloHora(horaInicioRefrigerio);
                        Date horaRefriFin=JsfUtil.getFechaSoloHora(horaFinRefrigerio);
                        
                        int horaI=fec.getHours();
                        int horaF=fec2.getHours();
                        //JsfUtil.getFechaSoloHora(fec).compareTo(JsfUtil.getFechaSoloHora(horaInicioRefrigerio));
                        
                        boolean b1=lis.getHoraInicio().after(horaInicioRefrigerio);
                        boolean b2=lis.getHoraInicio().before(horaFinRefrigerio);
                        boolean b3=horaInicioRefrigerio.getHours() >= horaI  && horaInicioRefrigerio.getHours()<= horaF;
                        boolean b4=horaFinRefrigerio.getHours() >= horaI  && horaFinRefrigerio.getHours() <= horaF;
                        
                        boolean b5=horaRefriIni.compareTo(fec) < 0 && horaRefriFin.compareTo(fec) > 0;
                        boolean b6=horaRefriIni.compareTo(fec) >= 0 && horaRefriFin.compareTo(fec2) <= 0;
                        boolean b7=horaRefriIni.compareTo(fec) > 0 && horaRefriIni.compareTo(fec2) < 0;
                        boolean b8=horaRefriIni.compareTo(fec) <= 0 && horaRefriFin.compareTo(fec2) >= 0;
                        
                        //JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) < 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0)
                       // JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) >= 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) <= 0) ||
                       // JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0 && JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) < 0) ||
                       // JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) <= 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) >= 0)
                       /* if(horaI==horaInicioRefrigerio.getHours() || horaF==horaFinRefrigerio.getHours())
                        {
                            ref++;
                        }
                        if(b3 && b4)
                        {
                            ref++;
                        }*/
                        if(b5 || b6 || b7 || b8)
                        {
                            ref++;
                            break;
                        }
                        
                        
                    }
                     if(ref>0)
                        {
                             validacion = false;
                             JsfUtil.mensajeError("Por favor ingresar horas de curso fuera del rango del refrigerio: " + prog.getModuloId().getNombre());
                             break;
                        }
             }
            
            if((estado == EstadoCrud.CREAR) || (estado == EstadoCrud.EDITAR && !registro.getEstadoId().getCodProg().equals("TP_ECC_OBS")) ){
                List<SspPrograHora> lstHora = new ArrayList();
                //Validación de la fecha de procesamiento (4 dias para Lima, 8 dias para provincia)
                Calendar c_fecha = Calendar.getInstance();
                c_fecha.add(Calendar.DATE, DIAS_PRESENTACION );
                for(SspProgramacion prog: lstProgramacion){
                    
                    lstHora = new ArrayList();
                    for(SspPrograHora hora : prog.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            lstHora.add(hora);
                        }
                    }
                    Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                        @Override
                        public int compare(SspPrograHora one, SspPrograHora other) {
                            return one.getFecha().compareTo(other.getFecha());
                        }
                    });
                    
                    if(JsfUtil.getFechaSinHora(c_fecha.getTime()).compareTo(JsfUtil.getFechaSinHora(lstHora.get(0).getFecha())) > 0){    // prog.getFechaHoraIncio()
                        validacion = false;
                        JsfUtil.mensajeError("No puede guardar un curso con fechas de inicio menor a "+ DIAS_PRESENTACION + " días");
                        break;
                    }
                }
            }else{
                /// Validacion permitida de 2 dias para poder observar
                if(!lstObservaciones.isEmpty()){
                    Calendar c_hoy = Calendar.getInstance();
                    Integer dias_param = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaObservacion").getValor());
                    Calendar c_ffin = Calendar.getInstance();
                    c_ffin.setTime(lstObservaciones.get(0).getFecha());
                    c_ffin.add(Calendar.DATE, dias_param );

                    int feriados = -1;
                    int domingos = -1;
                    Date fechaIniTemp = lstObservaciones.get(0).getFecha();
                    while( (feriados + domingos) != 0){
                        feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(c_ffin.getTime()));
                        domingos = JsfUtil.calcularNroDiasSabadoYDomingoRango(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(c_ffin.getTime()));
                        if((feriados + domingos) > 0){
                            c_hoy.setTime(c_ffin.getTime());
                            c_hoy.add(Calendar.DATE, 1);                
                            fechaIniTemp = c_hoy.getTime();                            
                            c_ffin.add(Calendar.DATE, (feriados + domingos));
                            
                            int diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
                            if(diaSemana == 1){
                               c_hoy.setTime(fechaIniTemp);
                               c_hoy.add(Calendar.DATE, 1);
                               fechaIniTemp = c_hoy.getTime();                    
                           }
                        }
                    }                    
                    if( JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(c_ffin.getTime())) > 0){
                        validacion = false;
                        JsfUtil.mensajeError("No puede editar el curso porque ha pasado los "+ dias_param + " días permitidos para subsanar");                        
                    }
                }
            }
            
            Collections.sort(lstProgramacion, new Comparator<SspProgramacion>() {
                @Override
                public int compare(SspProgramacion one, SspProgramacion other) {
                    return one.getFechaHoraIncio().compareTo(other.getFechaHoraIncio());
                }
            });
            
            List<SspPrograHora> lstHora = new ArrayList();
            for(SspProgramacion prog : lstProgramacion){
                if(prog.getActivo() == 1){
                    for(SspPrograHora hora : prog.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            lstHora.add(hora);
                        }
                    }
                }
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });
            
            Date fechaIniTemp = lstHora.get(0).getFecha();
            Date fechaFinTemp = lstHora.get(lstHora.size()-1).getFecha();
            int dias = JsfUtil.calculaDiasEntreFechas(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(fechaFinTemp));
            int feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(fechaFinTemp));
            int domingos = JsfUtil.calcularNroDiasSabadoYDomingoRango(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(fechaFinTemp));
            int diasPermitidos = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasRangoTotal").getValor());
            dias = (dias - feriados - domingos + 1);
            
            if(dias > diasPermitidos){
                validacion = false;
                JsfUtil.mensajeError("Todos los cursos no pueden exceder más de "+diasPermitidos+" días. Por favor verifique.");
            }
        }
                
        if(validacion){
            validacion= validaHorasAcademicas();
        }
        
        ///////////// Validación de Locales ////////////
        cont = 0;
        for(SspLocal loc : lstLocales){
            if(loc.getActivo() == 1){
                cont = 0;
                for(SspProgramacion prog : lstProgramacion){
                    if(Objects.equals(prog.getLocalId().getId(), loc.getId())){
                        cont ++;                        
                        break;
                    }
                }
                if(cont == 0){
                    validacion = false;
                    JsfUtil.mensajeError("El local "+loc.getNombreLocal()+" no está en ninguna programación del curso.");
                }
            }
        }
        ////////////////////////////////////////////////
        
        
        ///////////// Validación de alumnos ////////////
        if(lstAlumnosVigilantes.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingrese al menos un prospecto de vigilante");
        }
        ////////////////////////////////////////////////
        
        return validacion;
    }
    
    public void verificarVariableNulls(List<SspProgramacion> lstProgramacion){
        if(tipoFormacion == null){
            for(SspProgramacion prog : lstProgramacion){
                if(prog.getActivo() == 1 && !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")){
                    tipoFormacion = prog.getModuloId().getTipoCursoId();
                    break;
                }
            }
        }
    }
    
    public String obtenerNombreModulo(String codModulo){
        if(lstModulos == null){
            return null;
        }
        for(SspModulo mod : lstModulos){
            if(codModulo.contains(mod.getCodModulo())){
                return mod.getNombre();
            }
        }
        return null;
    }
    
    public int obtenerParametroPorNombre(List<SbParametro> listaParametro, String nombre){
        int valor = 0;
        if(listaParametro != null){
            for(SbParametro param : listaParametro){
                if(param.getNombre().equals(nombre)){
                    return Integer.parseInt(param.getValor());
                }
            }
        }
        return valor;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SspRegistroCurso getSspRegistroCurso(Long id) {
        return ejbSspRegistroCursoFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SspRegistroCursoController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sspRegistroCursoConverter")
    public static class SspRegistroCursoControllerConverterN extends SspRegistroCursoControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SspRegistroCurso.class)
    public static class SspRegistroCursoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SspRegistroCursoController c = (SspRegistroCursoController) JsfUtil.obtenerBean("sspRegistroCursoController", SspRegistroCursoController.class);
            return c.getSspRegistroCurso(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SspRegistroCurso) {
                SspRegistroCurso o = (SspRegistroCurso) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SspRegistroCurso.class.getName());
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void reiniciarCamposBusqueda(){
        buscarPor = null;
        tipoEstado = null;
        filtro = null;        
        fechaInicio = null;
        fechaFinal = null;
        tipoFormacion = null;
        tipoServicio = null;
        local = null;
        estadoCurso = null;
        resultadosBandejaSeleccionados = new ArrayList();
        setDisabledBtnTransmitir(false);
    }
    
    public String prepareList(){
        reiniciarCamposBusqueda();
        cargarParametrosGlobales();
        obtenerListadoLocales();
        resultadosBandeja = null;
        tipoCurso = "TP_GSSP_CURS";
        estado = EstadoCrud.BUSCAR;
        esInstructor = false;
        disabledTipoFormacion=false;
        disabledTipoServicio=false;
        DIAS_CAMBIO_LOCAL = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasCambioLocal").getValor());
        if(ejbSspRegistroCursoFacade.obtenerTotalCursosObservados(JsfUtil.getLoggedUser().getNumDoc()) > 0){
            JsfUtil.mensajeAdvertencia("Ud. tiene algunos registros de solicitudes observadas");
        }
        return "/aplicacion/gssp/sspRegistroCurso/List";
    }
    
    public void cambiarTipoBuscarPor(){
        filtro = null;
    }
    
    public List<TipoSeguridad> getLstTipoFormacionListado(){
        /*if(tipoCurso == null){
            return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BAS','TP_FORMC_PRF'");
        }
        switch(tipoCurso){
            case "TODOS":
                return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BAS','TP_FORMC_PRF','TP_FORMC_BASEVNT','TP_FORMC_PRFEVNT'");
            case "TP_GSSP_CURS":
                return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BAS','TP_FORMC_PRF'");
            case "TP_GSSP_CURSEVT":
                return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BASEVNT','TP_FORMC_PRFEVNT'");
            default:
                return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BAS','TP_FORMC_PRF'");
        }*/
        if(tipoServicio!= null)
        {
            if(tipoServicio.getCodProg().equalsIgnoreCase("TP_EVE_SSP"))
            {
                return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BASEVNT','TP_FORMC_PRFEVNT'");
            }
            else
            {
                return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BAS','TP_FORMC_PRF'");
            }
        }
        else
        {
                return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'NO_EXISTE'");
        }
    }
    
    public List<TipoSeguridad> getLstTipoFormacion(){
        
      if(tipoServicio!= null)
      {
          //JsfUtil.mensajeAdvertencia("jala codigo: " + tipoServicio.getId() + " " + tipoServicio.getCodProg());
                if(tipoServicio.getCodProg().equalsIgnoreCase("TP_EVE_SSP"))
                {
                 esEvento=true; 
                }else
                {
                 esEvento=false; 
                }
         setDisabledTipoFormacion(false);
         
                if(esEvento){
                   return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BASEVNT','TP_FORMC_PRFEVNT'");    
               }
               return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_FORMC_BAS','TP_FORMC_PRF'");
               
       }
      else
      {
          setDisabledTipoFormacion(true);
          return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'NO_EXISTE'");
          
      }
                
        
    }
    
    public void obtenerListadoLocales(){
        lstLocales = new ArrayList();
        lstLocales = ejbSspLocalFacade.listarTodosLocales();
    }
    
    public List<TipoSeguridad> getLstEstadosCursos(){
        return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgsOrderById("'TP_ECC_CRE','TP_ECC_OBS','TP_ECC_TRA','TP_ECC_FIN','TP_ECC_EVA','TP_ECC_ACT','TP_ECC_CAN','TP_ECC_NPR','TP_ECC_NDR','TP_ECC_FDP','TP_ECC_DES'");
    }
    
    public void buscarBandeja(){
        boolean validacion = true;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        
        if(buscarPor == null && estadoCurso == null && local == null && tipoFormacion == null && tipoServicio==null && (fechaInicio == null && fechaFinal == null) ){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":buscarPor");  
            JsfUtil.invalidar(obtenerForm()+":estado");  
            JsfUtil.invalidar(obtenerForm()+":nombreLocal");  
            JsfUtil.invalidar(obtenerForm()+":tipoFormacion");
            JsfUtil.invalidar(obtenerForm()+":tipoServicio");
            JsfUtil.invalidar(obtenerForm()+":fechaInicio");  
            JsfUtil.invalidar(obtenerForm()+":fechaFin");  
            JsfUtil.mensajeAdvertencia("Es necesario ingresar al menos un filtro de búsqueda");
        }else{
            if(buscarPor != null && (filtro == null || filtro.isEmpty())){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":filtroBusqueda");
                JsfUtil.mensajeAdvertencia("Es necesario ingresar el campo a buscar");
                return;
            }
            if(fechaInicio != null && fechaFinal != null){
                if(JsfUtil.getFechaSinHora(fechaInicio).compareTo(JsfUtil.getFechaSinHora(fechaFinal)) > 0){
                    JsfUtil.invalidar(obtenerForm()+":fechaInicio");  
                    JsfUtil.invalidar(obtenerForm()+":fechaFin");  
                    JsfUtil.mensajeAdvertencia("La fecha de inicio no puede ser mayor que la fecha fin");
                }
            }
        }
 
        if(filtro != null){
            filtro = filtro.trim();
        }
        if(buscarPor != null){
            buscarPor = buscarPor.trim();
        }
        SbPersonaGt administ = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        if(validacion){            
            HashMap mMap = new HashMap();
            mMap.put("fechaIni", fechaInicio);
            mMap.put("fechaFin", fechaFinal);
            mMap.put("buscarPor", buscarPor);
            mMap.put("filtro", filtro);
            mMap.put("administrado", administ.getId());
            mMap.put("tipoFormacion", (tipoFormacion != null)?tipoFormacion.getId():null);
            mMap.put("tipoServicio", (tipoServicio != null)?tipoServicio.getId():null);
            mMap.put("estadoCurso", (estadoCurso != null)?estadoCurso.getId():null);
            switch(tipoCurso){
                case "TODOS":
                    mMap.put("tipocurso", "'TP_GSSP_CURS','TP_GSSP_CURSEVT'");
                    break;
                default:
                    //mMap.put("tipocurso", "'"+ tipoCurso + "'");
                    mMap.put("tipocurso", "'TP_GSSP_CURS','TP_GSSP_CURSEVT'");
                    break;
            }
            mMap.put("local", (local != null)?local.getId():null);
            
            resultadosBandeja = ejbSspRegistroCursoFacade.buscarCursosBandeja(mMap);
            reiniciarCamposBusqueda();
        }
    }
    
    public String obtenerColoRegistroApto(SspRegistroCurso curso){
        String color = "datatableCursos-row-apto";
        
        if(curso != null){
            if(!curso.getEstadoId().getCodProg().equals("TP_ECC_CRE")){
                return null;
            }
            if(curso.getSspProgramacionList() != null){
                for(SspProgramacion progra : curso.getSspProgramacionList()){
                    if(progra.getActivo() == 1 && progra.getInstructorId() == null &&
                       !progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP") ){
                        color = "datatableCursos-row-noApto";
                        break;
                    }
                }
            }else{
                color = "datatableCursos-row-noApto";
            }
        }else{
            color = "datatableCursos-row-noApto";
        }
        return color;
    }
    
    public boolean validaTransmitir(SspRegistroCurso curso, boolean isUpdated){
        boolean validacion = true;
        Integer dias_param;
        
        for(SspProgramacion progra : curso.getSspProgramacionList()){
            if(progra.getActivo() == 1 && progra.getInstructorId() == null &&
               !progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP") 
               ){
                validacion = false;
                JsfUtil.mensajeError("No puede transmitir el registro con ID " + curso.getId() + " porque no han sido confirmados todos los instructores");
                break;
            }
        }
        
        if(curso.getEstadoId().getCodProg().equals("TP_ECC_OBS")){
            lstObservaciones = new ArrayList();
            for(SspCursoEvento ce : curso.getSspCursoEventoList()){
                if(ce.getActivo() == 1 && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")){
                    lstObservaciones.add(ce);
                }
            }
            if(!lstObservaciones.isEmpty()){
                if(isUpdated){
                    dias_param = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("SspRegistroCurso_horasObservacionDocs").getValor());
                }else{
                    dias_param = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaObservacion").getValor());
                }
                
                Collections.sort(lstObservaciones, new Comparator<SspCursoEvento>() {
                    @Override
                    public int compare(SspCursoEvento one, SspCursoEvento other) {
                        return other.getFecha().compareTo(one.getFecha());
                    }
                });
                Calendar c_hoy = Calendar.getInstance();
                Calendar c_ffin = Calendar.getInstance();
                c_ffin.setTime(lstObservaciones.get(0).getFecha());
                c_ffin.add(Calendar.DATE, dias_param);
                int feriados = -1;
                int domingos = -1;
                Date fechaIniTemp = lstObservaciones.get(0).getFecha();
                while( (feriados + domingos) != 0){
                    feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(c_ffin.getTime()));
                    domingos = JsfUtil.calcularNroDiasSabadoYDomingoRango(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(c_ffin.getTime()));
                    if((feriados + domingos) > 0){
                        c_hoy.setTime(c_ffin.getTime());
                        c_hoy.add(Calendar.DATE, 1);
                        fechaIniTemp = c_hoy.getTime();
                        c_ffin.add(Calendar.DATE, (feriados + domingos));
                        
                        int diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
                        if(diaSemana == 1){
                           c_hoy.setTime(fechaIniTemp);
                           c_hoy.add(Calendar.DATE, 1);
                           fechaIniTemp = c_hoy.getTime();                    
                       }
                    }
                }
                if( JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(c_ffin.getTime())) > 0){
                    validacion = false;
                    JsfUtil.mensajeError("No puede transmitir el registro con ID " + curso.getId() + " porque ha pasado los "+ dias_param + " días permitidos para subsanar");                        
                }
            }
        }
        
        String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");	
	for(SspProgramacion progra : curso.getSspProgramacionList()){
            if(progra.getActivo() == 1 && !excepcionModulos.contains(progra.getModuloId().getCodModulo()) ){
                DIAS_PRESENTACION = ((progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 7 || progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 15)?(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoLima").getValor())):(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoProvincia").getValor())));
                break;
            }
        }
        
        if(!curso.getEstadoId().getCodProg().equals("TP_ECC_OBS")){
            Date fechaLimiteCurso = obtenerFechaLimiteCursoDate(curso);
            if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fechaLimiteCurso)) > 0){
                validacion = false;
                JsfUtil.mensajeError("No puede transmitir el registro con ID " + curso.getId() + " porque el curso inicia antes de los "+DIAS_PRESENTACION+" días permitidos" );
            }
        }
        return validacion;
    }
    
    public boolean tieneEstadoActualizado(SspRegistroCurso item){
        boolean validation = false;
        if(item == null){
            return validation;
        }
        for(SspCursoEvento evento : item.getSspCursoEventoList()){
            if(evento.getActivo() == 0){
                continue;
            }
            if(evento.getTipoEventoId().getCodProg().equals("TP_ECC_ACT")){
                validation = true;
                break;
            }
        }
        return validation;
    }
    
    public void transmitirCurso(){
        boolean isUpdated = false;

        if(!resultadosBandejaSeleccionados.isEmpty()){            
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            Integer usuaTramDoc = ejbSbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
            SbPersonaGt per = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());            
            int cont = 0;
            String estadoCurso = "";
            String timeStamp = ""+(new Date()).getTime();
            System.err.println("Transmitir curso(s) " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss")+", "+timeStamp+ ", Login: " + p_user.getNumDoc() );
            
            for(SspRegistroCurso curso : resultadosBandejaSeleccionados){                
                usuarioArchivar = "USRWEB";
                registro = ejbSspRegistroCursoFacade.find(curso.getId());
                System.err.println("ID: "+ registro.getId() + ", estado: "+ registro.getEstadoId().getNombre() + ", Login: " + p_user.getNumDoc());
                isUpdated = tieneEstadoActualizado(registro);
                if(!validaTransmitir(registro, isUpdated)){
                    continue;
                }
                estadoCurso = curso.getEstadoId().getCodProg();
                if(!generaExpediente(registro, usuaTramDoc, per, estadoCurso)){
                    continue;
                }
                if(!guardarDatosCurso(isUpdated)){
                    archivarExpediente(registro.getNroExpediente(), usuarioArchivar, estadoCurso);
                    continue;
                }
                JsfUtil.mensaje("Se transmitió el registro Código: " + registro.getId() + ", con expediente: " + registro.getNroExpediente());
                cont++;
                registro = null;
            }
            System.err.println("Fin transmisión :"+timeStamp+ ", Login: "+ p_user.getNumDoc());
            reiniciarCamposBusqueda();
            resultadosBandeja = new ArrayList();
            if(cont > 0){
                JsfUtil.mensaje("Se procesó "+ cont + " registro(s) correctamente.");
                //RequestContext.getCurrentInstance().execute("PF('wvDlgVerComunicado').show()");   //Se retira msj que pide accesos por correo
            }else{
                JsfUtil.mensajeAdvertencia("No se procesó ningún registro.");
            }
        }else{
            JsfUtil.mensajeAdvertencia("Por favor seleccionar al menos un registro para transmitir");
        }
    }
    
    public void archivarExpediente(String nroExpediente, String usuario, String estadoCurso){
        if(estadoCurso.equals("TP_ECC_CRE") ){
            boolean estadoTraza = wsTramDocController.archivarExpediente(nroExpediente, usuario, "");
        }
    }
    
    public boolean guardarDatosCurso(boolean isUpdated){
        try {
            if(registro.getSspCursoEventoList() == null){
                registro.setSspCursoEventoList(new ArrayList());
            }
            if(isUpdated){
                registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_ACT"));                            
            }else{
                registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_TRA"));
            }                        
            adicionaCursoEvento(registro.getEstadoId(), "");
            ejbSspRegistroCursoFacade.edit(registro);
        } catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean generaExpediente(SspRegistroCurso reg, Integer usuaTramDoc, SbPersonaGt per, String estadoCurso){
        boolean validacion = true, estadoTraza = true;
        List<Integer> acciones = new ArrayList();
        
        if(estadoCurso.equals("TP_ECC_CRE") ){
            for(SspProgramacion prog : reg.getSspProgramacionList()){
                if(prog.getActivo() == 1 && !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")){
                    tipoFormacion = prog.getModuloId().getTipoCursoId();
                    break;
                }
            }
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            String asunto = ((reg.getAdministradoId().getRuc() != null)?(reg.getAdministradoId().getRznSocial()+" - RUC: "+reg.getAdministradoId().getRuc() ):(reg.getAdministradoId().getApePat() + " " + reg.getAdministradoId().getApeMat() + " " + reg.getAdministradoId().getNombres() +" - "+ (reg.getAdministradoId().getTipoDoc() != null ?" - "+reg.getAdministradoId().getTipoDoc().getNombre():" - DNI" )+": "+reg.getAdministradoId().getNumDoc() ));
            String titulo = tipoFormacion.getNombre() + ", "+ asunto;
            String expediente = wsTramDocController.crearExpediente_gssp(null, per , asunto , Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_nroProceso").getValor()) , usuaTramDoc, usuaTramDoc, titulo);
            if(expediente != null){
                acciones.add(21);   // Procesado
                registro.setNroExpediente(expediente);
                registro.setHashQr(JsfUtil.crearHash(registro.getNroExpediente() + " - " + registro.getId()));
                ///////// Para obtener usuario a derivar /////////
                SbDerivacionCydoc  derivacion = ejbSbDerivacionCydocFacade.obtenerUsuarioDerivacionDepartamentoCyDoc(p_user.getSistema(), ejbTipoBaseFacade.tipoBaseXCodProg("TP_GSSP_CURS").getId(), obtenerDepartamentoLocal(reg));
                if(derivacion == null){
                    //Si no hay parametrización se deriva a un usuario de Lima
                    derivacion = ejbSbDerivacionCydocFacade.obtenerUsuarioDerivacionDepartamentoCyDoc(p_user.getSistema(), ejbTipoBaseFacade.tipoBaseXCodProg("TP_GSSP_CURS").getId(), obtenerDepartamento("LIMA"));
                    if(derivacion == null){
                        JsfUtil.mensajeError("No se pudo encontrar a un usuario parametrizado para derivación de expediente");
                        return true;
                    }
                }
                //////////////////////////////////////////////////////
                estadoTraza = wsTramDocController.asignarExpediente(expediente, "USRWEB", derivacion.getUsuarioDestinoId().getLogin(), "TRANSMITIDO", "", acciones,false,null);
                if (!estadoTraza) {
                    JsfUtil.mensajeError("No se pudo generar la traza del expediente. ID exp. : " + expediente);
                }else{
                    usuarioArchivar = derivacion.getUsuarioDestinoId().getLogin();
                }
            }else{
                validacion = false;
                JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro Código: " + registro.getId());
            }
        }
        if(estadoCurso.equals("TP_ECC_OBS") ){
            acciones.add(21);   //PROCESADO
            String loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(reg.getNroExpediente());
            estadoTraza = wsTramDocController.asignarExpediente(reg.getNroExpediente(), loginUsuarioAct, loginUsuarioAct, "SUBSANADO", "", acciones,false,null);
            if (!estadoTraza) {
                validacion = false;
                JsfUtil.mensajeError("No se pudo generar la traza del expediente. Exp. : " + reg.getNroExpediente());
            }
        }
        return validacion;
    }
    
    public Long obtenerDepartamentoLocal(SspRegistroCurso reg){
        String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");	
	for(SspProgramacion progra : reg.getSspProgramacionList()){
            if(progra.getActivo() == 1 && !excepcionModulos.contains(progra.getModuloId().getCodModulo()) ){
                return progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId();
            }
        }  
        return null;
    }
    
    public Long obtenerProvinciaLocal(SspRegistroCurso reg){
        String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");	
	for(SspProgramacion progra : reg.getSspProgramacionList()){
            if(progra.getActivo() == 1 && !excepcionModulos.contains(progra.getModuloId().getCodModulo()) ){
                return progra.getLocalId().getDistritoId().getProvinciaId().getId();
            }
        }  
        return null;
    }
    
    public Long obtenerDistritoLocal(SspRegistroCurso reg){
         String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");	
	for(SspProgramacion progra : reg.getSspProgramacionList()){
            if(progra.getActivo() == 1 && !excepcionModulos.contains(progra.getModuloId().getCodModulo()) ){
                return progra.getLocalId().getDistritoId().getId();
            }
        }  
        return null;
    }
    
    public Long obtenerDepartamento(String departamento){
        return ejbSbDepartamentoFacade.obtenerDepartamentoByNombre(departamento).getId();
    }
    
    public void adicionaCursoEvento(TipoSeguridad estado, String observacion){
        SspCursoEvento evento = new SspCursoEvento();
        evento.setId(null);
        evento.setActivo(JsfUtil.TRUE);
        evento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        evento.setAudNumIp(JsfUtil.getIpAddress());
        evento.setRegistroCursoId(registro);
        evento.setFecha(new Date());
        evento.setObservacion(observacion);
        evento.setTipoEventoId(estado);
        evento.setUserId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).getId());
        registro.getSspCursoEventoList().add(evento);
    }
    
    public void seleccionarFilaTablaCurso(SelectEvent event){
        eventoSeleccion();
    }
    
    public void deseleccionarFilaTablaCurso(UnselectEvent event){
        eventoSeleccion();
    }
    
    public void toggleEventFilaCurso(ToggleSelectEvent event){
        eventoSeleccion();
    }
    
    public void eventoSeleccion(){
        setDisabledBtnTransmitir(false);
        for (SspRegistroCurso reg : resultadosBandejaSeleccionados) {
            if(!reg.getEstadoId().getCodProg().equals("TP_ECC_CRE") && !reg.getEstadoId().getCodProg().equals("TP_ECC_OBS")){
                JsfUtil.mensajeAdvertencia("El cambio es sólo para los estados CREADO u OBSERVADO");
                setDisabledBtnTransmitir(true);
                return;
            }else{
                for(SspProgramacion progra : reg.getSspProgramacionList()){
                    if(progra.getActivo() == 1 && progra.getInstructorId() == null &&
                       !progra.getModuloId().getCodModulo().equals("REFB") &&
                       !progra.getModuloId().getCodModulo().equals("REFP") 
                      ){
                        setDisabledBtnTransmitir(true);
                        JsfUtil.mensajeAdvertencia("Aún hay cursos que necesitan ser confirmados por un instructor");
                        return;
                    }
                }
            }
        }
    }
    
    public String obtenerTipoFormacionCursoBandeja(SspRegistroCurso curso){
        String nombre = "";
        
        for(SspProgramacion prog : curso.getSspProgramacionList()){
            if(prog.getActivo() == 1 && !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP") ){
                nombre = prog.getModuloId().getTipoCursoId().getNombre();
                break;
            }
        }
        return nombre;
    }
    
    public String obtenerTipoServicioBandeja(SspRegistroCurso curso){
        String nombre = "";
        
        if(curso.getTipoServicio()!=null)
        {
            if(curso.getTipoServicio().getCodProg().equalsIgnoreCase("TP_EVE_SSP") || curso.getTipoServicio().getCodProg().equalsIgnoreCase("TP_GEN_SSP")){
                nombre = curso.getTipoServicio().getNombre();
                
            }else
            {
                nombre="NO ESPECIFICA";
            }
        } else{
            nombre="NO ESPECIFICA";
        }
            
        
        return nombre;
    }
    
    public String obtenerFechaInicioBandeja(SspRegistroCurso curso){
        List<SspPrograHora> lstHora = new ArrayList();
        String fecha = "";
        
        if(curso.getEstadoId().getCodProg().equals("TP_ECC_ACT") || curso.getEstadoId().getCodProg().equals("TP_ECC_FIN")){
            fecha = "CURSO FINALIZADO";
            return fecha;
        }
        if(curso.getEstadoId().getCodProg().equals("TP_ECC_CAN") || curso.getEstadoId().getCodProg().equals("TP_ECC_NPR") || 
           curso.getEstadoId().getCodProg().equals("TP_ECC_NDR") || curso.getEstadoId().getCodProg().equals("TP_ECC_FDP") || 
           curso.getEstadoId().getCodProg().equals("TP_ECC_DES") ){
            fecha = "-";
            return fecha;
        }
        
        for(SspProgramacion prog : curso.getSspProgramacionList()){
            if(prog.getActivo() == 1){
                if(prog.getModuloId().getCodModulo().equals("REFB") || prog.getModuloId().getCodModulo().equals("REFP")){
                    continue;
                }
                for(SspPrograHora hora : prog.getSspPrograHoraList()){
                    if(hora.getActivo() == 1){
                        lstHora.add(hora);
                    }
                }
            }
        }
        
        Collections.sort(lstHora, new Comparator<SspPrograHora>() {
            @Override
            public int compare(SspPrograHora one, SspPrograHora other) {
                return one.getFecha().compareTo(other.getFecha());
            }
        });        
        if(lstHora != null && !lstHora.isEmpty()){
            Calendar c_hoy = Calendar.getInstance();
            Date fechaIniTemp = new Date();
            Date fechaFinTemp = lstHora.get(0).getFecha();
            int dias = JsfUtil.calculaDiasEntreFechas(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(fechaFinTemp));
            int feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(fechaFinTemp));
            int domingos = JsfUtil.calcularNroDiasSabadoYDomingoRango(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(fechaFinTemp));
            dias = (dias - feriados - domingos);
            c_hoy.setTime(fechaFinTemp);
            int diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
            if(diaSemana == 7){
                dias ++;
            }
            
            switch(dias){
                case 0:
                        fecha = "HOY";
                        break;
                case 1:
                        fecha = "EN 1 DÍA";
                        break;
                default:
                        if(dias < 0){
                            fecha = "HACE "+ Math.abs(dias) +" DÍA(S)";
                        }else{
                            fecha = "EN "+ dias + " DÍA(S)";
                        }
                        break;
            }
        }
        
        return fecha;
    }

    public boolean renderBtnVer(TipoSeguridad reg){
        boolean validar = false;
        
        if(reg != null){
//            if(reg.getCodProg().equals("TP_ECC_CRE") ||
//               reg.getCodProg().equals("TP_ECC_OBS") ||
//               reg.getCodProg().equals("TP_ECC_FIN")
//               ){
                validar = true;
//            }
        }
        
        return validar;
    }
    
    public boolean renderBtnEditar(SspRegistroCurso reg){
        boolean validar = false;
        
        if(reg != null){
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_CRE") ||
               reg.getEstadoId().getCodProg().equals("TP_ECC_OBS")
               ){
                validar = true;
            }
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_OBS")){
                if(tieneEstadoActualizado(reg)){
                     validar = false;
                }
            }                
            if(validar && calcularDiasProgramacionCurso(reg.getSspProgramacionList()) < 1){
                validar = false;
            }
        }        
        return validar;
    }
    
    public boolean renderBtnEditarDocumentos(SspRegistroCurso reg){
        boolean validation = false;
        
        if(reg != null){
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_OBS")){                
                validation = tieneEstadoActualizado(reg);
            }            
        }        
        return validation;
    }
    
    public boolean renderBtnBorrar(SspRegistroCurso reg){
        boolean validar = false;
        if(reg != null){
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_CRE") || reg.getEstadoId().getCodProg().equals("TP_ECC_OBS")){
                //validar = validaDiasBtnBorrar(reg.getSspProgramacionList());
                validar = true;
            }
        }
        return validar;
    }
    
    public void borrarRegistro(SspRegistroCurso reg){
        try {
            String msje = "";
            if(reg != null ){
                if(reg.getEstadoId().getCodProg().equals("TP_ECC_CRE")){
                    reg.setActivo(JsfUtil.FALSE);
                    msje = "Se ha borrado correctamente el registro";
                }else{
                    reg.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CAN"));
                    msje = "Se ha cancelado correctamente el registro";
                }
                if(reg.getNroExpediente() != null && !reg.getNroExpediente().equals("S/N") ){
                    String loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(reg.getNroExpediente());
                    boolean estadoNE = wsTramDocController.archivarExpediente(reg.getNroExpediente(), loginUsuarioAct, "El expediente "+reg.getNroExpediente()+ " fue archivado por desistimiento");
                    if(!estadoNE){
                        JsfUtil.mensajeError("Ocurrió un error al archivar el expediente "+reg.getNroExpediente());
                    }
                }
                ejbSspRegistroCursoFacade.edit(reg);
                resultadosBandeja.remove(reg);

                JsfUtil.mensaje(msje);
            }
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
    public boolean validacionBorradoRegistro(List<SspProgramacion> lstProgramacion){
        boolean validacion = true;
        List<SspPrograHora> lstHora = new ArrayList();
        for(SspProgramacion prog : lstProgramacion){
            if(prog.getActivo() == 1){
                for(SspPrograHora hora : prog.getSspPrograHoraList()){
                    if(hora.getActivo() == 1){
                        lstHora.add(hora);
                    }
                }
            }
        }
        Collections.sort(lstHora, new Comparator<SspPrograHora>() {
            @Override
            public int compare(SspPrograHora one, SspPrograHora other) {
                return one.getFecha().compareTo(other.getFecha());
            }
        });
        Calendar c_hoy = Calendar.getInstance();
        Integer dias_param = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasHabilesCancelacion").getValor());
        Date fechaProgramacion = lstHora.get(0).getFecha(); // getFechaHoraIncio
        Calendar c_fecha = Calendar.getInstance();
        c_fecha.setTime(fechaProgramacion);
        c_fecha.add(Calendar.DATE, (dias_param * -1) );

        int feriados = -1;
        int domingos = -1;
        Date fechaIniTemp = fechaProgramacion;
        while( (feriados + domingos) != 0){
            feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(c_fecha.getTime()), JsfUtil.getFechaSinHora(fechaIniTemp));
            domingos = JsfUtil.calcularNroDiasSabadoYDomingo(JsfUtil.getFechaSinHora(c_fecha.getTime()), JsfUtil.getFechaSinHora(fechaIniTemp));
            if((feriados + domingos) > 0){
                fechaIniTemp = c_fecha.getTime();
                c_fecha.add(Calendar.DATE, ((feriados + domingos)*-1));
            }else{
                c_hoy.setTime(c_fecha.getTime());
                int diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
                if(diaSemana == 1 || diaSemana == 7){
                    fechaIniTemp = c_fecha.getTime();
                    c_fecha.add(Calendar.DATE, -1);
                }
            }
        }                                              

        if( JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(c_fecha.getTime())) > 0){
            validacion = false;
            JsfUtil.mensajeError("No puede borrar el curso porque ha pasado los "+ dias_param + " días hábiles previos al inicio del curso "+ JsfUtil.formatoFechaDdMmYyyy(c_fecha.getTime()));
        }
        
        return validacion;
    }
    
    public boolean validaDiasBtnBorrar(List<SspProgramacion> lstProgramacion){
        List<SspPrograHora> lstHora = new ArrayList();
        for(SspProgramacion prog : lstProgramacion){
            if(prog.getActivo() == 1){
                 for(SspPrograHora hora : prog.getSspPrograHoraList()){
                    if(hora.getActivo() == 1){
                        lstHora.add(hora);
                    }
                }
            }
        }
        Collections.sort(lstHora, new Comparator<SspPrograHora>() {
            @Override
            public int compare(SspPrograHora one, SspPrograHora other) {
                return one.getFecha().compareTo(other.getFecha());
            }
        });
        if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(lstHora.get(0).getFecha() )) < 0 ){
            return true;
        }
        return false;
    }
    
    public int calcularDiasProgramacionCurso(List<SspProgramacion> lstProgramacion){
        int dias = 0;
        if(lstProgramacion != null && !lstProgramacion.isEmpty()){
            List<SspPrograHora> lstHora = new ArrayList();
            for(SspProgramacion prog : lstProgramacion){
                if(prog.getActivo() == 1){
                   for(SspPrograHora hora : prog.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            lstHora.add(hora);
                        }
                    }
                }
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });            
            if(lstHora != null && !lstHora.isEmpty()){
                dias = JsfUtil.calculaDiasEntreFechas(JsfUtil.getFechaSinHora(new Date()), JsfUtil.getFechaSinHora(lstHora.get(0).getFecha()));    
            }
        }
        return dias;
    }
    
    public void reiniciarValores(){
        registro = new SspRegistroCurso();
        lstLocales = new ArrayList();
        lstProgramacion = new ArrayList();
        lstAlumnosVigilantes = new ArrayList();
        lstInstructores = new ArrayList();
        lstNotasTemporales = new ArrayList();
        lstModalidades = new ArrayList();
        tipoFormacion = null;
        instructorPersona = null;
        cambioLocal = false;
        cambioInstructor = false;
        cambioHorario = false;
        setEsNuevo(false);
        setDisabledNombres(false);
        setEsEvento(false);
        limpiarLocal();
        limpiarInstructores();
        limpiarAgendar();
        limpiarVigilante();
        nombreFotoTemporal = "";
        fechaLimite = null;
        fechaMinima = null;
        archivo = null;
        archivoTemporal = null;
        isSedeLima = true;
        renderTipoCurso = false;
        tipoCursoSelected = null;
        idColorValidate = -1L;
        horaInicioRefrigerio = null;
        horaFinRefrigerio = null;
        fileNotas = null;
        notaStream = null;
        notasByte = null;
        usuarioArchivar = "";
        tipoCurso = "TP_GSSP_CURS";
        lstFotosAlumnos = new ArrayList();
        lstObservaciones = new ArrayList();
        lstObservacionesDetalle = new ArrayList();
        lstProgramacionHistorial = new ArrayList();
        lstModulosNoRep = new ArrayList();
    }
    
    public List<TipoBaseGt> getListTipoUbicacion(){
        return ejbTipoBaseFacade.lstTipoBase("TP_VIA");
    }
    
    public List<SbDistritoGt> getListDistritos() {
        return ejbSbDistritoFacade.listarDistritos();
    }
    
    public void eliminarLocalSeleccionado(SspLocal local){
        if(!lstLocales.isEmpty()){
            for(SspLocal loc : lstLocales){
                if(Objects.equals(loc, local)){
                    lstLocales.remove(loc);
                    
                    if(!lstProgramacion.isEmpty()){
                        boolean eliminaProgramacion = false;
                        List<SspProgramacion> lstTemp = new ArrayList(lstProgramacion);
                        for(SspProgramacion p : lstTemp){
                            if(Objects.equals(p.getLocalId().getId(), loc.getId() )){
                                lstProgramacion.remove(p);
                                eliminaProgramacion = true;
                            }
                        }
                        if(eliminaProgramacion){
                            JsfUtil.mensajeAdvertencia("Se eliminaron los horarios relacionados al local eliminado");
                        }
                    }
                    
                    JsfUtil.mensaje("Eliminado de la lista correctamente");
                    break;
                }
            }
        }else{
            JsfUtil.mensajeError("Por favor seleccione al menos un local");
        }
    }
    
    public void agregarLocal(){
        boolean validacion = true;
        
        //validaciones temporales por Curso Virtual:
        if(lstLocales.size() == 1){
            validacion = false;
            JsfUtil.mensajeError("La modalidad virtual, sólo permite un local");
        }
        if(!declaracion){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:declaracion");
            JsfUtil.mensajeError("La declaración jurada es indispensable para registrar la plataforma");
        }
        if(caracteristicas == null || caracteristicas.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:caracteristicas");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_descripcion"));
        }else {
            int exceso = caracteristicas.trim().length()-1400;
            if (exceso>0){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:caracteristicas");
                JsfUtil.mensajeError("El texto es grande, borrar "+ exceso+" letra(s) por favor");
            }
        }
        if(referenciaLocal == null || referenciaLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:referenciaLocal");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_responsable"));
        }
        if(telefonoLocal == null || telefonoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:telefono");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_telefono"));
        }
        if(correoLocal == null || correoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:correoElectronico");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_correo"));
        }
        // fin
        
        if(nombreLocal == null || nombreLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:nombreLocal");
            JsfUtil.mensajeError("Por favor, ingrese el nombre de la Plataforma Virtual"); //Cambio temporal por Curso Virtual
        }
        if(ubigeoLocal == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:ubigeo");
            JsfUtil.mensajeError("Por favor seleccione la ubicación del local");
        }
        if(tipoUbicacionLocal == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:tipoUbicacion");
            JsfUtil.mensajeError("Por favor seleccione un tipo de ubicación");
        }
        
        if(caracteristicas == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:caracteristicas");
            JsfUtil.mensajeError("Por favor ingrese las caracteristicas y las herramientas");
        }
        if(direccionLocal == null || direccionLocal.isEmpty()){
            //validacion = false;   //Cambio temporal por Curso Virtual
            //JsfUtil.invalidar(obtenerForm()+":accordionPanel:direccionLocal");
            //JsfUtil.mensajeError("Por favor ingrese la dirección");
        }

        if(latitudLocal == null || longitudLocal == null){
            //validacion = false; //Cambio temporal por Curso Virtual
            //JsfUtil.invalidar(obtenerForm()+":accordionPanel:btnMapa");
            //JsfUtil.mensajeError("Por favor seleccionar la ubicación en el mapa.");
        }
        if(aforoLocal == null || aforoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:aforoTotal");
            JsfUtil.mensajeError("Por favor ingrese el aforo total del local.");
        }else{
            if(aforoLocal != null && Integer.parseInt(aforoLocal) <= 0){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:aforoTotal");
                JsfUtil.mensajeError("Por favor ingrese el aforo total del local.");
            }
        }
        if(validacion){
            for(SspLocal loc : lstLocales ){
                if(loc.getNombreLocal().equals(nombreLocal.trim()) ){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:nombreLocal");
                    JsfUtil.mensajeError("Ya ha sido ingresado un local con el mismo nombre");
                    break;
                }
            }
        }
        if(validacion){
                SspLocal nuevoLocal = new SspLocal();
                nuevoLocal.setId(JsfUtil.tempId());
                nuevoLocal.setActivo(JsfUtil.TRUE);
                nuevoLocal.setFechaReg(new Date());
                if(direccionLocal == null || direccionLocal.isEmpty()){
                    nuevoLocal.setDireccion("Es Curso Virtual");
                }
                else{
                    nuevoLocal.setDireccion(direccionLocal.trim());
                }
                
                nuevoLocal.setDistritoId(ubigeoLocal);
                if(latitudLocal != null && !latitudLocal.isEmpty()){
                    nuevoLocal.setGeoLat(latitudLocal);
                }
                if(longitudLocal != null && !longitudLocal.isEmpty()){
                    nuevoLocal.setGeoLong(longitudLocal);
                }
                nuevoLocal.setNombreLocal(nombreLocal.trim());
                nuevoLocal.setCaracteristicas(caracteristicas.trim());
                nuevoLocal.setTipoUbicacionId(tipoUbicacionLocal);
                nuevoLocal.setReferencia((referenciaLocal != null)?referenciaLocal.trim():null);
                if(aforoLocal != null && !aforoLocal.isEmpty()){
                    nuevoLocal.setAforo(Long.parseLong(aforoLocal));
                }
                nuevoLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                nuevoLocal.setAudNumIp(JsfUtil.getIpAddress());
                nuevoLocal.setSspLocalContactoList(new ArrayList());
                if(telefonoLocal != null && !telefonoLocal.isEmpty()){
                    SspLocalContacto contacto = new SspLocalContacto();
                    contacto.setId(null);
                    contacto.setLocalId(nuevoLocal);
                    contacto.setTipoMedioId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MEDIOC_TEL"));
                    contacto.setValor(telefonoLocal.trim());
                    contacto.setDescripcion(telefonoLocal.trim());
                    contacto.setActivo(JsfUtil.TRUE);
                    contacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    contacto.setAudNumIp(JsfUtil.getIpAddress());
                    contacto = (SspLocalContacto) JsfUtil.entidadMayusculas(contacto, "");
                    nuevoLocal.getSspLocalContactoList().add(contacto);
                }
                if(correoLocal != null && !correoLocal.isEmpty()){
                    SspLocalContacto contacto = new SspLocalContacto();
                    contacto.setId(null);
                    contacto.setLocalId(nuevoLocal);
                    contacto.setTipoMedioId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MEDIOC_COR"));
                    contacto.setValor(correoLocal.trim());
                    contacto.setDescripcion(correoLocal.trim());
                    contacto.setActivo(JsfUtil.TRUE);
                    contacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    contacto.setAudNumIp(JsfUtil.getIpAddress());
                    contacto = (SspLocalContacto) JsfUtil.entidadMayusculas(contacto, "");
                    nuevoLocal.getSspLocalContactoList().add(contacto);
                }
                nuevoLocal = (SspLocal) JsfUtil.entidadMayusculas(nuevoLocal, "");
                nuevoLocal.setCaracteristicas(caracteristicas.trim());
                lstLocales.add(nuevoLocal);
                
                JsfUtil.mensajeAdvertencia("Por favor adicionar una programación con el local ingresado");
                validarNuevoLocal(null);
        }
    }
    
    public void limpiarLocal(){
        declaracion = false;
        caracteristicas = "";
        nombreLocal = "";
        ubigeoLocal = null;
        direccionLocal = "";
        telefonoLocal = "";
        tipoUbicacionLocal = null;
        referenciaLocal = "";
        correoLocal = "";
        aforoLocal = "";
        latitudLocal = "";
        longitudLocal = "";
        latitudSelected = "";
        longitudSelected = "";
        localSelected = null;
        motivoLocal = "";
        setDisabledLocal(true);
        setDisabledLocalEdicion(true);
    }
    
    public void openDlgMapa(SspLocal localMap){
        if(localMap.getGeoLat() != null && localMap.getGeoLong() != null){
            latitudSelected = ""+localMap.getGeoLat();
            longitudSelected = ""+localMap.getGeoLong();
            direccionMapa = localMap.getDireccion();
            RequestContext.getCurrentInstance().execute("PF('wvDlgVerMapa').show()");
            RequestContext.getCurrentInstance().update("frmVerMapa");
        }else{
            JsfUtil.mensajeAdvertencia("Este local no tiene registrado un mapa");
        }
    }    
    
    public String obtenerMapCenterBuscarMap(){
        if(latitudSelected != null && !latitudSelected.isEmpty() && longitudSelected != null && !longitudSelected.isEmpty()){
            LatLng coord1 = new LatLng(Double.parseDouble(latitudSelected), Double.parseDouble(longitudSelected));
            //Basic marker
            modelMap = new DefaultMapModel();
            modelMap.addOverlay(new Marker(coord1, "punto"));
            return latitudSelected + ", " + longitudSelected;
        }else{
            return ejbSbParametroFacade.obtenerParametroXNombre("coordenadas_sucamec").getValor();
        }
    }
    
    public void onPointSelect(PointSelectEvent event){
        LatLng latlng = event.getLatLng();
        latitudSelected = ""+latlng.getLat();
        longitudSelected = ""+latlng.getLng();
        
        LatLng coord1 = new LatLng(latlng.getLat(), latlng.getLng());
        //Basic marker
        modelMap = new DefaultMapModel();
        modelMap.addOverlay(new Marker(coord1, "punto"));
        
        RequestContext.getCurrentInstance().update("frmMapa");
        JsfUtil.mensaje("Punto seleccionado. Latitud: "+ latlng.getLat()+", Longitud: "+latlng.getLng());
    }
    
    public void onGeocode(GeocodeEvent event) {
        List<GeocodeResult> results = event.getResults();
         
        if (results != null && !results.isEmpty()) {
            modelMap = new DefaultMapModel();                    
             
            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                
                LatLng center = results.get(0).getLatLng();
                latitudSelected = (""+center.getLat());
                longitudSelected = (""+center.getLng());
                modelMap.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
                break;
            }
        }
    }
    
    public void confirmarMapa(){
        if(latitudSelected != null && longitudSelected != null){
            latitudLocal = (latitudSelected);
            longitudLocal = (longitudSelected);
            RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').hide()");
            RequestContext.getCurrentInstance().update(obtenerForm());
            RequestContext.getCurrentInstance().update(obtenerForm()+":pnlRegistroDatosLocal");
        }else{
            JsfUtil.mensajeError("Por favor seleccione un punto en el mapa");
        }
    }
    
    public void cancelarMapa(){
        latitudSelected = (latitudLocal);
        longitudSelected = (longitudLocal);
        RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').hide()");
    }
    
    public void openDlgVerMapa(SspLocal loc){
        if(loc != null && loc.getGeoLat() != null && loc.getGeoLong() != null){
            latitudSelected = ""+loc.getGeoLat();
            longitudSelected = ""+loc.getGeoLong();
            direccionMapa = loc.getDireccion();
            RequestContext.getCurrentInstance().execute("PF('wvDlgVerMapa').show()");
            RequestContext.getCurrentInstance().update("frmVerMapa");
        }else{
            JsfUtil.mensajeAdvertencia("No se encontró las coordenadas del local");
        }
    }
    
    public String obtenerForm(){
        switch(estado){
            case CREAR:
                        return "createForm";
            case EDITAR:
                        return "editForm";
            case VER:
                        return "verForm";
            case BUSCAR:
                        return "listForm";
            case BANDEJA:
                        return "bandejaInstForm";
            case CONFIRMAR:
                        return "confirmarForm";
            case RECTIFICAR:
                        return "modificarForm";
        }
        return null;
    }
    
    public String obtenerMapCenter(){
        if(!latitudSelected.isEmpty() && !longitudSelected.isEmpty()){
            return latitudSelected + ", " + longitudSelected;
        }
        return null;
    }
    
    public String obtenerValorContactoTablaLocal(List<SspLocalContacto> lstContacto, String tipoContacto){
        String valor = "";
        for(SspLocalContacto loc : lstContacto){
            if(loc.getActivo() == 1 && loc.getTipoMedioId().getCodProg().equals(tipoContacto)){
                valor = loc.getValor();
                break;
            }
        }
        return valor;
    }
    
    public void limpiarInstructores(){
        tipoDoc = "";
        numDoc = "";
        numDocBusInst = "";
        nombresInstructor = "";
        telefonoContacto = "";
        correoContacto = "";
        lstModulos = new ArrayList();
        lstModulosSelected = new ArrayList();
        lstCargaInstructores = new ArrayList();
        lstModulosFiltrados = new ArrayList();
        lstInstructoresTabla = new ArrayList();
        setDisabledTipoFormacion(false);
        dtColumns = new ArrayList();
    }
    
    public void buscarInstructor(){
        boolean validacion = true;
        if(tipoDoc == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:tipoDocIdentidad");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de documento");
        }
        if(numDocBusInst == null || numDocBusInst.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:nroDocIdentidad");
            JsfUtil.mensajeError("Por favor ingresar el número de documento");
        }else{
            numDocBusInst = numDocBusInst.trim();
        }
        
        if(validacion){
            for(Map reg : lstInstructoresTabla){
                if(Objects.equals(reg.get("NUM_DOC"), numDocBusInst.trim() )){
                    validacion = false;
                    deshabilitaCursos();
                    JsfUtil.mensajeError("El instructor ya ha sido adicionado al listado");
                }
            }
        }
        
        if(validacion){
            lstCargaInstructores= ejbRmaCursosFacade.buscarInstructorByTipoDocByDoc(tipoDoc, numDocBusInst); // Busqueda en RMA
            if(lstCargaInstructores == null) 
                lstCargaInstructores = new ArrayList();
            if(!lstCargaInstructores.isEmpty()){                
                if (tipoDoc.equals("3")) {                    
                    numDoc = lstCargaInstructores.get(0).get("NUM_DOC").toString().trim();
                } else {
                    numDoc = numDocBusInst;
                }                
                int contPerfil = ejbSbUsuarioFacade.buscarPerfilInstructorUsuario(numDoc);                    
                if(contPerfil > 0){
                    nombresInstructor = lstCargaInstructores.get(0).get("NUM_DOC") + " / " + lstCargaInstructores.get(0).get("NOMBRES") + " " + lstCargaInstructores.get(0).get("APELLIDOS");
                    JsfUtil.mensaje("Se encontró al instructor en el sistema");
                    instructorCursos(true);
                }else{
                    JsfUtil.mensajeError("El instructor ingresado no tiene cuenta y/o perfil de instructor en el sistema SEL");
                }                    
            }else{
                JsfUtil.mensajeError("No se encontró al instructor");
            }
        }
    }
    
    public void cambiarTipoFormacion(){
        if(tipoFormacion != null){
            limpiarVigilante();
            lstAlumnosVigilantes = new ArrayList();
            lstModulos = ejbSspModuloFacade.listarModuloByTipoCursoSinRefrigerio(tipoFormacion.getId());
            lstInstructoresTabla = new ArrayList();
            lstModulosFiltrados = new ArrayList();
            lstCursosOpt = new ArrayList();
            dtColumns = new ArrayList();
            if(lstModulos!=null){
                for(SspModulo mod: lstModulos){
                    CursosOption checkOpt = new CursosOption(mod, false);
                    lstCursosOpt.add(checkOpt);
                }
            }
            prepareTableDynamic();
            instructorCursos(false);
            setDisabledTipoServicio(false);
        }else{
            lstModulos = new ArrayList();
            JsfUtil.mensajeError("Por favor seleccione un tipo de formación del curso");
        }
    }
    
    public void deshabilitaCursos(){
        if(lstCursosOpt != null){
            for(CursosOption curs : lstCursosOpt){
                curs.setDisabled(true);
            }
        }
    }
    
    public void agregarInstructor(){
        boolean validacion = true;

        if(tipoFormacion == null){
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:tipoFormacion");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de formación");
            return;
        }
        
        if(lstModulosSelected.isEmpty()){
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:cursosAutorizados");
            JsfUtil.mensajeError("Por favor seleccionar un curso autorizado para dictar");
            return;
        }
        
        if( !lstCargaInstructores.isEmpty()){
            int cont = 0;            
            for(SspModulo modSelect : lstModulosSelected){
                cont = 0;
                for(Map mapInst : lstCargaInstructores){
                    if(modSelect.getNombre().trim().equals(mapInst.get("NOM_CURSO").toString()) ){
                        cont++;
                    }
                }
                if(cont == 0){
                    validacion = false;
                    JsfUtil.mensajeError("El instructor no cuenta con autorización para dictar el curso de "+ modSelect.getNombre());
                    return;
                }
            }
            
            for(Map reg : lstInstructoresTabla){
                if(Objects.equals(reg.get("NUM_DOC"), lstCargaInstructores.get(0).get("NUM_DOC") )){
                    validacion = false;
                    deshabilitaCursos();
                    JsfUtil.mensajeError("El instructor ya ha sido adicionado al listado");
                    return;
                }
            }
            
            if(validacion){
                Map nmap = new HashMap();
                SspInstructor inst = ejbSspInstructorFacade.buscarInstructorByNumDoc(lstCargaInstructores.get(0).get("NUM_DOC").toString().trim());
                if(inst != null){
                    nmap.put("SspInstructor", inst );
                    nmap.put("ID", inst.getId() );
                }else{
                    nmap.put("SspInstructor", null );
                    nmap.put("ID", null );
                }
                nmap.put("TIP_USR", lstCargaInstructores.get(0).get("TIP_USR") );
                nmap.put("COD_USR", lstCargaInstructores.get(0).get("COD_USR") );
                nmap.put("NUM_DOC", lstCargaInstructores.get(0).get("NUM_DOC") );
                nmap.put("FECHA_NAC", lstCargaInstructores.get(0).get("FECHA_NAC") );
                nmap.put("SEXO", lstCargaInstructores.get(0).get("SEXO") );
                nmap.put("APELLIDOS", lstCargaInstructores.get(0).get("APELLIDOS") );
                nmap.put("NOMBRES", lstCargaInstructores.get(0).get("NOMBRES") );
                nmap.put("NOMBRES_APELLIDOS", lstCargaInstructores.get(0).get("NOMBRES") + " " + lstCargaInstructores.get(0).get("APELLIDOS"));
                nmap.put("APE_PAT", lstCargaInstructores.get(0).get("APE_PAT") );
                nmap.put("APE_MAT", lstCargaInstructores.get(0).get("APE_MAT") );
                BigDecimal bd = (BigDecimal) lstCargaInstructores.get(0).get("NRO_FICHA");
                nmap.put("NRO_FICHA", bd.longValue() );
                nmap.put("FECHA_EMISION", lstCargaInstructores.get(0).get("FECHA_EMISION") );
                nmap.put("FECHA_CADUCIDAD", lstCargaInstructores.get(0).get("FECHA_CADUCIDAD") );
                nmap.put("TELEFONO", lstCargaInstructores.get(0).get("CELULAR") );
                nmap.put("EMAIL", lstCargaInstructores.get(0).get("EMAIL") );
                nmap.put("RUC", lstCargaInstructores.get(0).get("RUC") );
                nmap.put("RZN_SOC_EMPRESA", lstCargaInstructores.get(0).get("RZN_SOC_EMPRESA") );
                nmap.put("TELF_EMPRESA", lstCargaInstructores.get(0).get("TELF_EMPRESA") );
                if(lstModulos != null){
                    for(SspModulo modSelect : lstModulosSelected){
                        cont = 0;
                        for(Map mapInst : lstCargaInstructores){
                            if(modSelect.getNombre().trim().equals(mapInst.get("NOM_CURSO").toString()) ){
                                cont++;
                                nmap.put(modSelect.getCodModulo(), "X");    // ✓ - check
                                break;                                
                            }
                        }
                        if(cont == 0){
                            nmap.put(modSelect.getCodModulo(), null);
                        }
                    }
                }
                
                lstInstructoresTabla.add(nmap);
                tipoDoc = "";
                numDoc = "";
                numDocBusInst = "";
                nombresInstructor = "";
                lstCargaInstructores = new ArrayList();
                lstModulosSelected = new ArrayList();
                deshabilitaCursos();
            }
            getModulosFiltrados();
            
            if(!lstInstructoresTabla.isEmpty()){
                setDisabledTipoFormacion(true);
                setDisabledTipoServicio(true);
            }
        }else{
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:tipoFormacion");
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:cursosAutorizados");
            JsfUtil.mensajeError("Por favor primero busque un instructor");
        }

        if (lstLocales.size()==1 && estado == EstadoCrud.CREAR){
            localAgendarSelected = lstLocales.get(0);
            idLocal=lstLocales.get(0).getId();
        }
    }
    
    public void getModulosFiltrados(){
        lstModulosFiltrados = new ArrayList();
        if(lstInstructoresTabla == null){
            return;
        }
        if(lstModulos == null){
            return;
        }
        for(Map reg : lstInstructoresTabla){
            for(SspModulo modulo : lstModulos){
                if(reg.get(modulo.getCodModulo()) != null){
                    if(!lstModulosFiltrados.contains(modulo)){
                        lstModulosFiltrados.add(modulo);
                    }
                }
            }
        }       
    }
    
    public void instructorCursos(boolean mostrarMsje){
        if(lstCursosOpt!= null && lstCargaInstructores != null){
            int cont = 0;
            boolean flagDisabled = false;
            //lstModulos = ejbSspModuloFacade.listarModuloByTipoCurso(tipoFormacion.getId());        
            //List<SspModulo> lstMod = new ArrayList();

            for(CursosOption checkOpt : lstCursosOpt){
                checkOpt.setDisabled(true);
                cont = 0;
                for(Map mapInst : lstCargaInstructores){
                    if(checkOpt.getModulo().getNombre().trim().equals(mapInst.get("NOM_CURSO").toString()) ){
                        cont++;
                    }
                }
                if(cont > 0){
                    flagDisabled = true;
                    checkOpt.setDisabled(false);
                    //lstMod.add(mod);
                    //lstModulos.remove(mod);
                    //JsfUtil.mensajeError("El instructor no cuenta con autorización para dictar el curso de "+ mod.getNombre());
                    //return;
                }
            }
            if(!flagDisabled && mostrarMsje){
                JsfUtil.mensajeAdvertencia("El instructor no tiene un curso disponible para dictar");
            }
            prepareTableDynamic();
        }
    }
    
    public String obtenerValorMedioContacto(List<SbMedioContactoGt> lstContacto, String tipoContacto){
        String valor = "";
        for(SbMedioContactoGt loc : lstContacto){
            if(loc.getTipoId().getCodProg().equals(tipoContacto)){
                valor = loc.getValor();
                break;
            }
        }
        return valor;
    }
    
    public void prepareTableDynamic(){
        // prepare dynamic columns
        dtColumns = new ArrayList();
        dtColumns.add(new TableDynamic("NOMBRES Y APELLIDOS","NOMBRES_APELLIDOS",false,""));
        //dtColumns.add(new TableDynamic("TELEFONO","TELEFONO",false,""));
        //dtColumns.add(new TableDynamic("CORREO","EMAIL",false,""));
        dtColumns.add(new TableDynamic("MEDIOS DE CONTACTO","CONTACTO",false,"width: 90px;"));
 
        if(lstModulos != null){
            for(SspModulo mod : lstModulos){
                if(mod.getActivo() == 1 && !mod.getCodModulo().equals("REFB") && !mod.getCodModulo().equals("REFP") ){
                    dtColumns.add(new TableDynamic(mod.getCodModulo(),mod.getCodModulo(),false,"width:40px;"));
                }
            }
        }
        RequestContext.getCurrentInstance().update(obtenerForm());
        //RequestContext.getCurrentInstance().update(obtenerForm()+":pnlInstructores");
    }
    
    public void moduloChecked(){
        prepareTableDynamic();
        //RequestContext.getCurrentInstance().execute("tab02();");
    }
    
    public void eliminarInstructorSeleccionado(Map inst){
        for(Map nmap : lstInstructoresTabla){
            if(Objects.equals(nmap.get("NUM_DOC"), inst.get("NUM_DOC"))){

                for(SspProgramacion p : lstProgramacion){
                    if(p.getActivo() == 1 && nmap.get(p.getModuloId().getCodModulo()) != null){
                        JsfUtil.mensajeError("Primero tiene que eliminar el curso en la sección [Agendar Curso] para eliminar este instructor seleccionado");
                        return;
                    }
                }
            }
        }        
        
        
        for(Map reg : lstInstructoresTabla){
            if(Objects.equals(reg.get("NUM_DOC"), inst.get("NUM_DOC"))){
                
                //// Eliminacion de Instructor/Módulo ////
                List<SspInstructorModulo> lstTemp = new ArrayList(registro.getSspInstructorModuloList());
                for(SspInstructorModulo instMod : lstTemp){
                    if(instMod.getActivo() == 1 && 
                       Objects.equals(instMod.getInstructorId().getPersonaId().getNumDoc(), reg.get("NUM_DOC") ) &&
                       reg.get(instMod.getModuloId().getCodModulo()) != null
                      ){
                        registro.getSspInstructorModuloList().remove(instMod);
                    }
                }
                //////////////////////////////////////////
                
                lstInstructoresTabla.remove(reg);
                getModulosFiltrados();
                JsfUtil.mensaje("Se eliminó el registro correctamente");
                break;                
            }
        }
        
        if(lstInstructoresTabla.isEmpty()){
            setDisabledTipoFormacion(false);
            RequestContext.getCurrentInstance().update(obtenerForm());
        }
        disabledTipoServicio=false;
    }
    
    public void limpiarAgendar(){
        localAgendarSelected = null;
        moduloAgendarSelected = null;
        fechaInicioAgendar = null;
        fechaFinAgendar = null;
        idLocal = null;
        horaInicioAgendar = null;
        horaFinAgendar = null;
        fechaHorario = null;
        tipoCursoSelected = null;
        setDisabledFechasHorario(false);
        listaFechasDisponibles = new ArrayList();
        if (lstLocales.size()==1 && (estado == EstadoCrud.CREAR || estado == EstadoCrud.EDITAR)){
            localAgendarSelected = lstLocales.get(0);
            idLocal=lstLocales.get(0).getId();
        }
    }
    
    public void agregarAgendarCurso(){
        boolean validacion = true;
        int horasAcademicas = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horasAcademicas").getValor());
        idColorValidate = -1L;
        
        if(idLocal == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:localAgendar");
            JsfUtil.mensajeError("Por favor seleccione un local para agendar curso");
        }else{
            for(SspLocal loc : lstLocales){
                if(Objects.equals(loc.getId(), idLocal)){
                    localAgendarSelected = loc;
                }
            }
        }
        if(tipoFormacion == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:tipoFormacion");
            JsfUtil.mensajeError("Por favor seleccione el tipo de formación");
        }
        if(moduloAgendarSelected == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:cursoAgendar");
            JsfUtil.mensajeError("Por favor seleccione un curso");
        }
        if(renderTipoCurso){
            if(tipoCursoSelected == null){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:tipoCurso");
                JsfUtil.mensajeError("Por favor seleccione el tipo de curso");    
            }
        }else{
            tipoCursoSelected = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TPCUR_TEO");
        }
        if(fechaInicioAgendar == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechainicio");
            JsfUtil.mensajeError("Por favor ingrese la fecha y hora de inicio");
        }
        if(fechaFinAgendar == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechaFin");
            JsfUtil.mensajeError("Por favor ingrese la fecha y hora de fin");
        }
        if(fechaInicioAgendar != null && fechaFinAgendar != null){
            if(fechaInicioAgendar.compareTo(fechaFinAgendar) > 0){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechainicio");
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechaFin");
                JsfUtil.mensajeError("La fecha de inicio no puede ser mayor a la fecha final");
            }
            Collections.sort(lstProgramacion, new Comparator<SspProgramacion>() {
                @Override
                public int compare(SspProgramacion one, SspProgramacion other) {
                    return one.getFechaHoraIncio().compareTo(other.getFechaHoraIncio());
                }
            });
            int dias = JsfUtil.calculaDiasEntreFechas(fechaInicioAgendar, fechaFinAgendar);
            int feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(fechaInicioAgendar, fechaFinAgendar);
            int domingos = JsfUtil.calcularNroDiasSabadoYDomingoRango(fechaInicioAgendar, fechaFinAgendar);
            int diasPermitidos = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasRangoTotal").getValor());
            dias = (int) (dias - feriados - domingos + 1);
            
            if(dias > diasPermitidos ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechainicio");
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechaFin");
                JsfUtil.mensajeError("La diferencia entre fechas no puede ser mayor a "+diasPermitidos+" días");
            }
        }
        if(fechaHorario == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechaHorario");
            JsfUtil.mensajeError("Debe seleccionar la fecha del horario del curso");
        }
        if(horaInicioAgendar == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaInicio");
            JsfUtil.mensajeError("Debe ingresar la hora de inicio del curso");
        }else{
            if(horaInicioAgendar.getHours() == 0 ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaInicio");
                JsfUtil.mensajeError("Por favor ingrese una hora de inicio correcta");
            }else{
                Calendar fechaTemp = Calendar.getInstance();
                fechaTemp.set(JsfUtil.formatoFechaYyyy(horaInicioAgendar), horaInicioAgendar.getMonth(), horaInicioAgendar.getDate(), horaFinPermitido, 0,0 );    // permitido hastas las 20:00
                if(horaInicioAgendar.getHours() < horaInicioPermitido || (horaInicioAgendar.compareTo(fechaTemp.getTime()) > 0) ){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaInicio");
                    JsfUtil.mensajeError("El horario permitido es entre las "+horaInicioPermitido+":00 y "+horaFinPermitido+":00 horas ");
                }
            }
        }
        if(horaFinAgendar == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaFin");
            JsfUtil.mensajeError("Debe ingresar la hora de fin del curso");
        }else{
            if(horaFinAgendar.getHours() == 0 ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaFin");
                JsfUtil.mensajeError("Por favor ingrese una hora de fin correcta");
            }else{
                Calendar fechaTemp = Calendar.getInstance();
                fechaTemp.set(JsfUtil.formatoFechaYyyy(horaFinAgendar), horaFinAgendar.getMonth(), horaFinAgendar.getDate(), horaFinPermitido, 0,0 );     // permitido hastas las 20:00
                if(horaFinAgendar.getHours() < horaInicioPermitido || (horaFinAgendar.compareTo(fechaTemp.getTime()) > 0) ){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaFin");
                    JsfUtil.mensajeError("El horario permitido es entre las "+horaInicioPermitido+":00 y "+horaFinPermitido+":00 horas ");
                }
            }
        }
        if(horaInicioAgendar != null && horaFinAgendar != null){
            if(fechaHorario != null){
                Calendar fechaTemp = Calendar.getInstance();
                fechaTemp.set(JsfUtil.formatoFechaYyyy(fechaHorario), fechaHorario.getMonth(), fechaHorario.getDate(),horaInicioAgendar.getHours(), horaInicioAgendar.getMinutes(),0 );
                horaInicioAgendar = (fechaTemp.getTime());
                fechaTemp.set(JsfUtil.formatoFechaYyyy(fechaHorario), fechaHorario.getMonth(), fechaHorario.getDate(),horaFinAgendar.getHours(), horaFinAgendar.getMinutes(),0 );
                horaFinAgendar = (fechaTemp.getTime());

                if(horaInicioAgendar.compareTo(horaFinAgendar) > 0){
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaInicio");
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaFin");
                    JsfUtil.mensajeError("La hora de inicio no puede ser mayor que la hora final");
                    validacion = false;
                    return;
                }

                long minutos = JsfUtil.calculaMinutosEntreHoras(horaInicioAgendar, horaFinAgendar);
                if(minutos < horasAcademicas){
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaInicio");
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaFin");
                    JsfUtil.mensajeError("No puede ingresar un rango de horas menor a "+horasAcademicas+" minutos");
                    validacion = false;
                }
                
                int horasAcademicasTotales = (int) Math.ceil((double) minutos/horasAcademicas);
                if(moduloAgendarSelected != null && !moduloAgendarSelected.getCodModulo().equals("REFB") && !moduloAgendarSelected.getCodModulo().equals("REFP") ){
                    if( horasAcademicasTotales > limiteHorasAcad ){
                        JsfUtil.mensajeError("No puede ingresar más de "+limiteHorasAcad+" hora(s) académica(s)");
                        validacion = false;
                    }
                }else{
                    if( horasAcademicasTotales > limiteHorasAcadRef  ){
                        JsfUtil.mensajeError("No puede ingresar más de "+limiteHorasAcadRef+" hora(s) académica(s)");
                        validacion = false;
                    }
                }                
            }
        }
        
        //Para permitir que registren desde provincia, la plataforma virtual que tiene como ubigeo a Lima
        isSedeLima = true;

        if(moduloAgendarSelected != null && localAgendarSelected != null){
            String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");
            
            if(!moduloAgendarSelected.getCodModulo().equals("REFB") && !moduloAgendarSelected.getCodModulo().equals("REFP") &&
               !excepcionModulos.contains(moduloAgendarSelected.getCodModulo()) &&
               localAgendarSelected.getDistritoId().getProvinciaId().getDepartamentoId().getId() != 0 &&
               localAgendarSelected.getDistritoId().getProvinciaId().getDepartamentoId().getId() != 7 && 
               localAgendarSelected.getDistritoId().getProvinciaId().getDepartamentoId().getId() != 15 && isSedeLima
              ){
                JsfUtil.mensajeError("Ud. seleccionó que la sede principal o ampliación se realizará en Lima o Callao pero el local seleccionado no cumple con el requisito");
                validacion = false;
            }
            if(!moduloAgendarSelected.getCodModulo().equals("REFB") && !moduloAgendarSelected.getCodModulo().equals("REFP") &&
               !excepcionModulos.contains(moduloAgendarSelected.getCodModulo()) &&
               (localAgendarSelected.getDistritoId().getProvinciaId().getDepartamentoId().getId() == 7 || localAgendarSelected.getDistritoId().getProvinciaId().getDepartamentoId().getId() == 15 ) && !isSedeLima
              ){
                JsfUtil.mensajeError("Ud. seleccionó que la sede principal o ampliación se realizará en una provincia y el local seleccionado no cumple con el requisito");
                validacion = false;
            }
        }            

        if(validacion){
            long minutos = JsfUtil.calculaMinutosEntreHoras(horaInicioAgendar, horaFinAgendar);
            //Validación de horas academicas x dia
            for(SspProgramacion prog : lstProgramacion){
                if(prog.getActivo() == 1 && !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")){
                    for(SspPrograHora cap : prog.getSspPrograHoraList()){
                        if (cap.getActivo() == 1 && JsfUtil.getFechaSinHora(cap.getFecha()).compareTo(JsfUtil.getFechaSinHora(fechaHorario)) == 0) {
                            minutos += JsfUtil.calculaMinutosEntreHoras(cap.getHoraInicio(), cap.getHoraFin());
                        }
                    }
                }
            }
            int horasAcademicasTotales = (int) Math.ceil((double) minutos/horasAcademicas);
            if( horasAcademicasTotales > limiteHorasAcad  ){
                JsfUtil.mensajeError("No puede ingresar más de "+limiteHorasAcad+" hora(s) académica(s) en la fecha "+JsfUtil.formatoFechaDdMmYyyy(fechaHorario));
                validacion = false;
                return;
            }
            ////
            
            // Validación de fechas
            for(SspProgramacion prog : lstProgramacion){
                if(prog.getActivo() == 1 && Objects.equals(prog.getModuloId().getId(), moduloAgendarSelected.getId()) ){
                   if(!Objects.equals(prog.getLocalId().getId(), idLocal)){

                       for(SspPrograHora cap : prog.getSspPrograHoraList()){
                            if (cap.getActivo() == 1 &&  JsfUtil.getFechaSinHora(cap.getFecha()).compareTo(JsfUtil.getFechaSinHora(fechaHorario)) == 0) {
                                if( (JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) < 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0)  ||
                                    (JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) >= 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) <= 0) ||
                                    (JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0 && JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) < 0) ||
                                    (JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) <= 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) >= 0)
                                    ){
                                    idColorValidate = prog.getId();
                                    JsfUtil.mensajeError("Ya se ha registrado este curso en un local distinto y con un rango de fechas similares.");
                                    validacion = false;
                                    return;
                                }
                            }
                        }
                   }
                }
            }
            
            // Validacion de horas
            for(SspProgramacion prog : lstProgramacion){
                if( prog.getActivo() == 1 ){
                    for(SspPrograHora cap : prog.getSspPrograHoraList()){
                        if (cap.getActivo() == 1 && JsfUtil.getFechaSinHora(cap.getFecha()).compareTo(JsfUtil.getFechaSinHora(fechaHorario)) == 0) {
                            if( (JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) < 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0)  ||
                                (JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) >= 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) <= 0) ||
                                (JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0 && JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) < 0) ||
                                (JsfUtil.getFechaSoloHora(horaInicioAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) <= 0 && JsfUtil.getFechaSoloHora(horaFinAgendar).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) >= 0)
                                ){
                                JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaInicio");
                                JsfUtil.invalidar(obtenerForm()+":accordionPanel:horaFin");
                                JsfUtil.mensajeError("Ya se ha registrado un horario con horas en común");
                                validacion = false;
                            }
                        }
                        if(!validacion)
                            break;
                    }
                }
            }
            
            List<Long> lstDepartamentos = new ArrayList();
            if(moduloAgendarSelected != null){
                if(!lstDepartamentos.contains(localAgendarSelected.getDistritoId().getProvinciaId().getDepartamentoId().getId())){
                    lstDepartamentos.add(localAgendarSelected.getDistritoId().getProvinciaId().getDepartamentoId().getId());
                }
            }
            String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");
            // Validación de ubicación (departamento)
            for(SspProgramacion prog : lstProgramacion){
                if(prog.getActivo() == 1 && 
                   prog.getLocalId() != null && prog.getModuloId() != null &&
                   !excepcionModulos.contains(prog.getModuloId().getCodModulo())
                  ){
                    if(!lstDepartamentos.contains(prog.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId())){
                        lstDepartamentos.add(prog.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId());
                    }
                }
            }
            if(lstDepartamentos.size() > 0 && !excepcionModulos.contains(moduloAgendarSelected.getCodModulo()) &&
                !moduloAgendarSelected.getCodModulo().equals("REFB")  &&
                !moduloAgendarSelected.getCodModulo().equals("REFP") 
                ){
                
                if(isSedeLima){
                    for(Long depaId : lstDepartamentos){
                        if(depaId != 15 && depaId != 7){
                            JsfUtil.mensajeError("No puede adicionar un curso cuyo departamento de dictado de clases es distinto al de los otros cursos ya ingresados.");
                            validacion = false;
                            break;
                        }
                    }
                }else{
                    if(lstDepartamentos.size() > 1){
                        JsfUtil.mensajeError("No puede adicionar un curso cuyo departamento de dictado de clases es distinto al de los otros cursos ya ingresados.");
                        validacion = false;
                    }
                }        
            }
        }
        
        if(validacion){
            boolean esNuevaProgramacion = true;
            for(SspProgramacion prog : lstProgramacion ){
                if(prog.getActivo() ==1 && Objects.equals(prog.getModuloId().getId(), moduloAgendarSelected.getId()) && Objects.equals(prog.getLocalId().getId(), idLocal) ){
                    if( moduloAgendarSelected.getCodModulo().equals("REFB") ){
                        if(!Objects.equals(prog.getLocalId().getId(), localAgendarSelected.getId())){
                            continue;
                        }
                    }
                    SspPrograHora horario = new SspPrograHora();
                    horario.setActivo(JsfUtil.TRUE);
                    horario.setFechaReg(new Date());
                    horario.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    horario.setAudNumIp(JsfUtil.getIpAddress());
                    horario.setId(JsfUtil.tempId());
                    horario.setFecha(fechaHorario);
                    horario.setHoraInicio(horaInicioAgendar);
                    horario.setHoraFin(horaFinAgendar);
                    horario.setTipoCurso(tipoCursoSelected);
                    horario.setProgramacionId(prog);
                    prog.getSspPrograHoraList().add(horario);
                    esNuevaProgramacion = false;
                    break;
                }
            }
            if(esNuevaProgramacion){
                SspProgramacion progra = new SspProgramacion();
                progra.setId(JsfUtil.tempId());
                progra.setActivo(JsfUtil.TRUE);
                progra.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                progra.setAudNumIp(JsfUtil.getIpAddress());
                progra.setFechaHoraIncio(fechaInicioAgendar);
                progra.setFechaHoraFin(fechaFinAgendar);
                progra.setLocalId(localAgendarSelected);
                progra.setModuloId(moduloAgendarSelected);
                progra.setRegistroCursoId(registro);
                progra.setSspPrograHoraList(new ArrayList());
                
                SspPrograHora horario = new SspPrograHora();
                horario.setActivo(JsfUtil.TRUE);
                horario.setFechaReg(new Date());
                horario.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                horario.setAudNumIp(JsfUtil.getIpAddress());
                horario.setId(JsfUtil.tempId());
                horario.setFecha(fechaHorario);
                horario.setHoraInicio(horaInicioAgendar);
                horario.setHoraFin(horaFinAgendar);
                horario.setTipoCurso(tipoCursoSelected);
                horario.setProgramacionId(progra);
                progra.getSspPrograHoraList().add(horario);
                lstProgramacion.add(progra);
            }
            /// limpiando valores
            setDisabledFechasHorario(true);
            horaInicioAgendar = null;
            horaFinAgendar = null;
            fechaHorario = null;
            ////
        }
    }

    public void limpiarVigilante(){
        tipoDocVigilante = null;
        numDocVigilante = null;
        nombresVigilante = null;
        apePatVigilante = null;
        apeMatVigilante = null;
        sexoVigilante = null;
        direccionVigilante = null;
        ubigeoVigilante = null;
        medioContactoVigilante = null;
        valorContactoVigilante = null;
        modalidadVigilante = null;
        correo = null;
        correoTemp = null;
        telefonoFijo = null;
        telefonoFijoTemp = null;
        telefonoMovil = null;
        telefonoMovilTemp = null;
        maxLength = 0;
        nombreFoto = "";
        foto = null;
        fotoByte = null;
        file = null;
        fechaNacimiento = null;
        lstDirecciones = new ArrayList();
        setRenderDireccionCombo(false);
        setDisabledNombres(false);
        setRenderPanelVigilante(false);
    }
    
    public List<TipoBaseGt> getListarTipoDoc(){
        return ejbTipoBaseFacade.lstTiposXCodigosProg("'TP_DOCID_DNI','TP_DOCID_CE'"); // 
    }
    
    public List<TipoBaseGt> getListarSexo(){
        return ejbTipoBaseFacade.lstTipoBase("TP_GEN");
    }
    
    public List<TipoBaseGt> getListarMedioContacto(){
        return ejbTipoBaseFacade.lstTipoBase("TP_MEDCO");
    }
    
    public void cambiarTipoDocVigilante(){
        if(tipoDocVigilante != null){
            numDocVigilante = null;
            nombresVigilante = null;
            apePatVigilante = null;
            apeMatVigilante = null;
            sexoVigilante = null;
            direccionVigilante = null;
            ubigeoVigilante = null;
            medioContactoVigilante = null;
            valorContactoVigilante = null;
            modalidadVigilante = null;
            correo = null;
            correoTemp = null;
            telefonoFijo = null;
            telefonoFijoTemp = null;
            telefonoMovil = null;
            telefonoMovilTemp = null;
            maxLength = 0;
            nombreFoto = "";
            foto = null;
            fotoByte = null;
            file = null;
            lstDirecciones = new ArrayList();
            setRenderDireccionCombo(false);
            setDisabledNombres(false);
            setRenderPanelVigilante(false);
            switch(tipoDocVigilante.getCodProg()){
                case "TP_DOCID_DNI":
                        maxLength = 8;
                        break;
                case "TP_DOCID_CE":
                        maxLength = 9;
                        break;
            }
        }else{
            JsfUtil.mensajeError("Por favor seleccione un tipo de documento");
        }
    }
    
    public void buscarVigilante(int tipo){
        boolean validacion = true;
        if(tipoDocVigilante == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:tipoDocVigilante");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de documento");
        }
        if(numDocVigilante == null || numDocVigilante.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
            JsfUtil.mensajeError("Por favor ingrese el número de documento");
        }else{
            numDocVigilante = numDocVigilante.trim();
        }
        if(tipo == 1){
            if(lstInstructoresTabla == null || lstInstructoresTabla.isEmpty()){
                validacion = false;
                JsfUtil.mensajeError("Por favor ingresar primero el tipo de formación y los instructores");
            }
        }
            
        if(validacion){
            switch(tipoDocVigilante.getCodProg()){
                case "TP_DOCID_DNI":
                        if (numDocVigilante.trim().length() != 8) {
                            JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                            JsfUtil.mensajeAdvertencia("El dni es de 8 dígitos ");
                            validacion = false;                
                        }
                        break;
                case "TP_DOCID_CE":
                        if (numDocVigilante.trim().length() != 9) {
                            JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                            JsfUtil.mensajeAdvertencia("El carnet de extranjería es de 9 dígitos ");
                            validacion = false;                
                        }
                        break;
            }
            
            for(SspAlumnoCurso alu : lstAlumnosVigilantes){
                if(alu.getActivo() == 1 && Objects.equals(alu.getPersonaId().getNumDoc(), numDocVigilante)){
                    validacion = false;
                    JsfUtil.mensajeError("La persona ya ha sido ingresada al listado");
                    break;
                }
            }
        }
        
        if(validacion){
            // Validacion de cursos pendientes del vigilantes
            List<SspRegistroCurso> listadoCursosPendientes = ejbSspRegistroCursoFacade.obtenerTotalCursosPendientes(numDocVigilante);
            if(listadoCursosPendientes != null && !listadoCursosPendientes.isEmpty()){
               for(SspRegistroCurso item : listadoCursosPendientes){
                   JsfUtil.mensajeAdvertencia("La persona ingresada cuenta con un curso pendiente en estado "+ item.getEstadoId().getNombre());
                   return;
               }
            }
            /////
            
            setEsNuevo(false);
            setRenderPanelVigilante(true);
            setRenderDireccionCombo(false);
            setDisabledNombres(false);
            alumnoVigilante = null;
            lstDirecciones = new ArrayList();
            nombreFotoTemporal = "";
            medioContactoVigilante = null;
            valorContactoVigilante = null;
            ubigeoVigilante = null;
            direccionCombo = null;
            direccionVigilante = null;
            correo = null;
            correoTemp = null;
            telefonoFijo = null;
            telefonoFijoTemp = null;
            telefonoMovil = null;
            telefonoMovilTemp = null;
            fechaNacimiento = null;
            boolean encontro = true;
            
            // Si alumno no está en la BD
            alumnoVigilante = new SspAlumnoCurso();
            alumnoVigilante.setActivo(JsfUtil.TRUE);
            alumnoVigilante.setFechaReg(new Date());
            alumnoVigilante.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            alumnoVigilante.setAudNumIp(JsfUtil.getIpAddress());
            alumnoVigilante.setRegistroCursoId(registro);
            alumnoVigilante.setId(JsfUtil.tempId());
            alumnoVigilante.setEvaluacionPj(null);
            alumnoVigilante.setTipoOpeId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_OPE_INI"));
            SbPersonaGt personaVigilante = ejbSbPersonaFacade.selectPersonaActivaxTipoDocyNumDoc(tipoDocVigilante.getId(), numDocVigilante);
            if(personaVigilante != null){
                alumnoVigilante.setPersonaId(personaVigilante);
            }else{
                encontro = false;
            }
            
            if(!encontro){
                personaVigilante = new SbPersonaGt();                
                personaVigilante.setNumDoc(numDocVigilante);
                personaVigilante.setTipoDoc(tipoDocVigilante);
                personaVigilante.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                
                if(tipoDocVigilante.getCodProg().equals("TP_DOCID_CE")){
                    //Extranjero
                    boolean valida = true;
                    ResPideMigra resMigra = wsPideController.buscarCarnetExtranjeria( ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_usuario_Pide").getValor() , numDocVigilante);
                    if(resMigra != null){
                        if(resMigra.isRspta()){
                            if(resMigra.getResultado().getStrNumRespuesta().equals("0000")){
                                personaVigilante.setId(null);
                                personaVigilante.setApePat(resMigra.getResultado().getStrPrimerApellido());
                                personaVigilante.setApeMat(resMigra.getResultado().getStrSegundoApellido());
                                personaVigilante.setNombres(resMigra.getResultado().getStrNombres());
                                personaVigilante.setGeneroId(null);
                                personaVigilante.setFechaNac(null);
                                alumnoVigilante.setPersonaId(personaVigilante);
                                JsfUtil.mensaje("Se encontró datos de la persona en Migraciones");
                            }else{
                                valida = false;
                                setRenderPanelVigilante(false);
                                JsfUtil.mensajeAdvertencia("No se encontró datos de la persona en Migraciones");
                            }
                        }else{
                            valida = false;
                            JsfUtil.mensajeAdvertencia("Hubo un error consultando el servicio de Migraciones. Tendrá que registrar los datos manualmente");
                        }
                    }else{
                        valida = false;
                        JsfUtil.mensajeAdvertencia("El servicio de Migraciones no está disponible. Tendrá que registrar los datos manualmente");
                    }
                    if(!valida){
                        personaVigilante.setApePat(null);
                        personaVigilante.setApeMat(null);
                        personaVigilante.setNombres(null);
                        nombresVigilante = null;
                        apePatVigilante = null;
                        apeMatVigilante = null;
                        sexoVigilante = null;
                        direccionVigilante = null;
                        direccionCombo = null;
                        fechaNacimiento = null;
                        setEsNuevo(true);
                        alumnoVigilante.setPersonaId(null);
                    }
                }else{
                    SbPersonaGt personaTemp  = new SbPersonaGt();
                    personaTemp.setNumDoc(numDocVigilante);
                    personaTemp.setTipoDoc(tipoDocVigilante);
                    personaTemp.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));

                    personaTemp = wsPideController.buscarReniec(personaTemp);
                    if(personaTemp != null){
                        if(personaTemp.getApePat() != null){
                            personaVigilante.setId(null);
                            personaVigilante.setApePat(personaTemp.getApePat());
                            personaVigilante.setApeMat(personaTemp.getApeMat());
                            personaVigilante.setNombres(personaTemp.getNombres());
                            personaVigilante.setGeneroId(personaTemp.getGeneroId());
                            personaVigilante.setFechaNac(personaTemp.getFechaNac());
                            alumnoVigilante.setPersonaId(personaVigilante);
                            JsfUtil.mensaje("Se encontró datos de la persona en el RENIEC");
                        }else{
                            personaVigilante.setApePat(null);
                            personaVigilante.setApeMat(null);
                            personaVigilante.setNombres(null);
                            nombresVigilante = null;
                            apePatVigilante = null;
                            apeMatVigilante = null;
                            sexoVigilante = null;
                            direccionVigilante = null;
                            direccionCombo = null;
                            fechaNacimiento = null;
                            setEsNuevo(true);
                            setRenderPanelVigilante(false);
                            alumnoVigilante.setPersonaId(null);
                            JsfUtil.mensajeAdvertencia("No se encontró datos de la persona en el RENIEC");
                        }
                    }else{
                        personaVigilante.setApePat(null);
                        personaVigilante.setApeMat(null);
                        personaVigilante.setNombres(null);
                        nombresVigilante = null;
                        apePatVigilante = null;
                        apeMatVigilante = null;
                        sexoVigilante = null;
                        direccionVigilante = null;
                        direccionCombo = null;
                        fechaNacimiento = null;
                        setEsNuevo(true);
                        alumnoVigilante.setPersonaId(null);
                        JsfUtil.mensajeAdvertencia("El servicio del RENIEC no está disponible. Tendrá que registrar los datos manualmente");
                    }
                }   
            }else{
                if(alumnoVigilante.getPersonaId().getApePat() == null && alumnoVigilante.getPersonaId().getApeMat() == null ){
                    personaVigilante = new SbPersonaGt();                
                    personaVigilante.setNumDoc(numDocVigilante);
                    personaVigilante.setTipoDoc(tipoDocVigilante);
                    personaVigilante.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));

                    if(tipoDocVigilante.getCodProg().equals("TP_DOCID_CE")){
                        ResPideMigra resMigra = wsPideController.buscarCarnetExtranjeria( ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_usuario_Pide").getValor() , numDocVigilante);
                        if(resMigra != null){
                            if(resMigra.isRspta()){
                                if(resMigra.getResultado().getStrNumRespuesta().equals("0000")){
                                    alumnoVigilante.getPersonaId().setApePat(resMigra.getResultado().getStrPrimerApellido());
                                    alumnoVigilante.getPersonaId().setApeMat(resMigra.getResultado().getStrSegundoApellido());
                                    alumnoVigilante.getPersonaId().setNombres(resMigra.getResultado().getStrNombres());
                                }
                            }
                        }                        
                    }else{
                        SbPersonaGt personaTemp  = new SbPersonaGt();
                        personaTemp.setNumDoc(numDocVigilante);
                        personaTemp.setTipoDoc(tipoDocVigilante);
                        personaTemp.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));

                        personaTemp = wsPideController.buscarReniec(personaTemp);
                        if(personaTemp != null){
                            if(personaTemp.getApePat() != null){
                                alumnoVigilante.getPersonaId().setApePat(personaTemp.getApePat());
                                alumnoVigilante.getPersonaId().setApeMat(personaTemp.getApeMat());
                                alumnoVigilante.getPersonaId().setNombres(personaTemp.getNombres());
                            }
                        }
                    }
                }
            }
            
            if(alumnoVigilante.getPersonaId() != null){
                nombresVigilante = alumnoVigilante.getPersonaId().getNombres();
                apePatVigilante = alumnoVigilante.getPersonaId().getApePat();
                apeMatVigilante = alumnoVigilante.getPersonaId().getApeMat();
                sexoVigilante = alumnoVigilante.getPersonaId().getGeneroId();
                fechaNacimiento = alumnoVigilante.getPersonaId().getFechaNac();
                setDisabledNombres(true);
                
                if(alumnoVigilante.getPersonaId().getSbDireccionList() == null ){
                    alumnoVigilante.getPersonaId().setSbDireccionList(new ArrayList());
                }
                if(alumnoVigilante.getPersonaId().getSbMedioContactoList() == null ){
                    alumnoVigilante.getPersonaId().setSbMedioContactoList(new ArrayList());
                }
                
                for(SbDireccionGt dire : alumnoVigilante.getPersonaId().getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                        direccionCombo = dire;
                        direccionVigilante = dire.getDireccion();
                        ubigeoVigilante = dire.getDistritoId();
                        lstDirecciones.add(dire);
                        break;
                    }
                }
                if(!lstDirecciones.isEmpty()){
                    setRenderDireccionCombo(true);
                }
                for(SbMedioContactoGt medio : alumnoVigilante.getPersonaId().getSbMedioContactoList()){
                    if(medio.getActivo() == 1){
                        switch(medio.getTipoId().getCodProg()){
                            case "TP_MEDCO_COR":
                                    correo = medio.getValor();
                                    correoTemp = medio.getValor();
                                    break;
                            case "TP_MEDCO_FIJ":
                                    telefonoFijo = medio.getValor();
                                    telefonoFijoTemp = medio.getValor();
                                    break;
                            case "TP_MEDCO_MOV":
                                    telefonoMovil = medio.getValor();
                                    telefonoMovilTemp = medio.getValor();
                                    break;
                        }
                    }
                }
                if(alumnoVigilante.getFotoId() != null){
                    nombreFoto = alumnoVigilante.getFotoId().getNombreFoto();
                    nombreFotoTemporal = alumnoVigilante.getFotoId().getNombreFoto();
                    cargarFoto();
                }
            }
            
            
        }
    }
    
    public void cargarFoto(){
        try {
            fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor() + nombreFoto ));
            InputStream is = new ByteArrayInputStream(fotoByte);
            foto = new DefaultStreamedContent(is, "image/jpeg", nombreFoto);
        } catch (Exception e) {
        }
    }
    
    public void agregarVigilante(int tipo){
        boolean validacion = true;
        if(tipo == 1){
            if(lstLocales == null || lstLocales.isEmpty()){
                validacion = false;
                JsfUtil.mensajeError("Por favor ingresar primero el listado de locales");
            }
            if(tipoFormacion == null){
                validacion = false;
                JsfUtil.mensajeError("Por favor seleccione el tipo de formación");
            }
            if(lstInstructoresTabla == null || lstInstructoresTabla.isEmpty()){
                validacion = false;
                JsfUtil.mensajeError("Por favor ingresar primero el listado de instructores");
            }else
            {
                for(Map reg : lstInstructoresTabla){
                if(Objects.equals(reg.get("NUM_DOC"), numDocVigilante )){
                    validacion = false;
                    JsfUtil.mensajeError("El instructor no puede registrarse como alumno");
                    return;
                }
            }
            } 
        }      
            
        if(tipoDocVigilante == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:tipoDocVigilante");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de documento");
        }
        if(numDocVigilante == null || numDocVigilante.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
            JsfUtil.mensajeError("Por favor ingresar el número de documento");
        }
        if(nombresVigilante == null || nombresVigilante.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:nombresVigilante");
            JsfUtil.mensajeError("Por favor ingresar el nombre del vigilante");
        }
        if(apePatVigilante == null || apePatVigilante.isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:apePatVigilante");
            JsfUtil.mensajeError("Por favor ingresar el apellido paterno del vigilante");
        }
        if(ubigeoVigilante == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:ubigeoVigilante");
            JsfUtil.mensajeError("Por favor seleccionar la ubicación");
        }else{        
            if(direccionCombo == null && (direccionVigilante == null || direccionVigilante.isEmpty() )){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:direccionCombo");
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:direccionVigilante");
                JsfUtil.mensajeError("Por favor ingresar la dirección del vigilante");
            }
        }
        if(fechaNacimiento == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechaNacimiento");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_fechaNacimiento"));
        }
        if( (correo == null || correo.trim().isEmpty()) && (telefonoFijo == null || telefonoFijo.trim().isEmpty()) && (telefonoMovil == null || telefonoMovil.trim().isEmpty() )  ){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:correoVigilante");
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:telefonoFijo");
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:telefonoMovil");
            JsfUtil.mensajeError("Por favor ingresar al menos un medio de contacto");
        }else{
            if(correo != null && !correo.trim().isEmpty()){
                if(!JsfUtil.validateExpresionRegular(correo.trim(), JsfUtil.bundleBDIntegrado("RegexEmail"))){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:correoVigilante");
                    JsfUtil.mensajeError("El correo no tiene el formato correcto");
                }
            }
            if(telefonoMovil != null && !telefonoMovil.trim().isEmpty() ){
                telefonoMovil = telefonoMovil.replace("_", "");
                if(telefonoMovil.trim().length() < 8){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:telefonoMovil");
                    JsfUtil.mensajeError("El nro. de teléfono móvil no tiene la cantidad correcta de dígitos");
                }
            }
            if(telefonoFijo != null && !telefonoFijo.trim().isEmpty() ){
                telefonoFijo = telefonoFijo.replace("_", "");
                if(telefonoFijo.trim().length() < 11){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":accordionPanel:telefonoFijo");
                    JsfUtil.mensajeError("El nro. de teléfono fijo no tiene la cantidad correcta de dígitos");
                }
            }
        }
        if(tipoDocVigilante != null && tipoDocVigilante.getCodProg().equals("TP_DOCID_CE")){
            if(fechaNacimiento == null){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechaNacimiento");
                JsfUtil.mensajeError("Por favor ingresar la fecha de nacimiento");
            }
        }
        if(fechaNacimiento != null){
            int anio = JsfUtil.formatoFechaYyyy(fechaNacimiento);
            if(anio < 1900 || anio > JsfUtil.formatoFechaYyyy(new Date()) ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":accordionPanel:fechaNacimiento");
                JsfUtil.mensajeError("Por favor ingresar la fecha de nacimiento correctamente");
            }
        }

        if(numDocVigilante != null && !numDocVigilante.isEmpty() && alumnoVigilante != null && alumnoVigilante.getPersonaId() != null && !Objects.equals(alumnoVigilante.getPersonaId().getNumDoc(), numDocVigilante) ){
            numDocVigilante = alumnoVigilante.getPersonaId().getNumDoc();
        }
        if(tipo == 1){
            if(tipoFormacion != null && lstInstructoresTabla != null && !lstInstructoresTabla.isEmpty() && numDocVigilante != null && !numDocVigilante.isEmpty()){
                tipoFormacion = lstModulos.get(0).getTipoCursoId();
                if(tipoFormacion.getCodProg().equals("TP_FORMC_PRF")){
                    int cont = ejbSspAlumnoCursoFacade.contarRegistrosAprobadosXAlumnoXTipoCurso(numDocVigilante, "TP_GSSP_CURS");
                    if(cont == 0){
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                        JsfUtil.mensajeError("Este vigilante no tiene un curso básico o de perfeccionamiento vigente");
                    }
                }
                if(tipoFormacion.getCodProg().equals("TP_FORMC_PRFEVNT")){
                    int cont = ejbSspAlumnoCursoFacade.contarRegistrosAprobadosXAlumno(numDocVigilante);
                    if(cont == 0){
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                        JsfUtil.mensajeError("Este vigilante no tiene un curso básico o de perfeccionamiento vigente");
                    }
                }
                if(tipoFormacion.getCodProg().equals("TP_FORMC_BAS")){
                    int cont = ejbSspAlumnoCursoFacade.contarRegistrosAprobadosXAlumno(numDocVigilante);
                    if(cont > 0){
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                        JsfUtil.mensajeError("Este vigilante tiene un curso vigente, no puede llevar un curso básico");
                    }
                }
                if(tipoFormacion.getCodProg().equals("TP_FORMC_BASEVNT")){
                    int cont = ejbSspAlumnoCursoFacade.contarRegistrosAprobadosXAlumno(numDocVigilante);
                    if(cont > 0){
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                        JsfUtil.mensajeError("Este vigilante tiene un curso vigente, no puede llevar un curso básico de espectáculos, certámenes y convenciones");
                    }
                }
                
            }
        }else{
            if(tipoFormacion != null && numDocVigilante != null && !numDocVigilante.isEmpty()){
                if(tipo == 1){
                    tipoFormacion = lstModulos.get(0).getTipoCursoId();    
                }
                if(tipoFormacion.getCodProg().equals("TP_FORMC_PRF")){
                    int cont = ejbSspAlumnoCursoFacade.contarRegistrosAprobadosXAlumnoXTipoCurso(numDocVigilante, "TP_GSSP_CURS");
                    if(cont == 0){
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                        JsfUtil.mensajeError("Este vigilante no tiene un curso básico o de perfeccionamiento vigente");
                    }
                }
                if(tipoFormacion.getCodProg().equals("TP_FORMC_BAS")){
                    int cont = ejbSspAlumnoCursoFacade.contarRegistrosAprobadosXAlumnoXTipoCurso(numDocVigilante, "TP_GSSP_CURS");
                    if(cont > 0){
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                        JsfUtil.mensajeError("Este vigilante tiene un curso vigente, no puede llevar un curso básico");
                    }
                }
                if(tipoFormacion.getCodProg().equals("TP_FORMC_BASEVNT")){
                    int cont = ejbSspAlumnoCursoFacade.contarRegistrosAprobadosXAlumnoXTipoCurso(numDocVigilante, "TP_GSSP_CURSEVT");
                    if(cont > 0){
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                        JsfUtil.mensajeError("Este vigilante tiene un curso vigente, no puede llevar un curso básico de espectáculos, certámenes y convenciones");
                    }
                }
                if(tipoFormacion.getCodProg().equals("TP_FORMC_PRFEVNT")){
                    int cont = ejbSspAlumnoCursoFacade.contarRegistrosAprobadosXAlumno(numDocVigilante);
                    if(cont == 0){
                        validacion = false;
                        JsfUtil.invalidar(obtenerForm()+":accordionPanel:numDocVigilante");
                        JsfUtil.mensajeError("Este vigilante no tiene un curso básico o de perfeccionamiento vigente");
                    }
                }
            }
        }
            
        if(lstLocales != null && !lstLocales.isEmpty()){
            Long aforo;
            int totalVigilantesActivos = 0;
            if(lstAlumnosVigilantes != null){
                for(SspAlumnoCurso alu : lstAlumnosVigilantes){
                    if(!validaAlumnoActivo(alu)){
                        continue;
                    }
                    totalVigilantesActivos++;
                }
            }
            
            List<SspLocal> lstLocalTemp = new ArrayList(lstLocales);
            Collections.sort(lstLocalTemp, new Comparator<SspLocal>() {
                @Override
                public int compare(SspLocal one, SspLocal other) {
                    if(one.getAforo() == null){
                        one.setAforo(0L);
                    }
                    if(other.getAforo() == null){
                        other.setAforo(0L);
                    }
                    return ((one.getAforo() > other.getAforo())?1:((Objects.equals(one.getAforo(), other.getAforo()))?0:-1));
                }
            });
            aforo = lstLocalTemp.get(0).getAforo();
            if(aforo > 0 && ((totalVigilantesActivos) >= aforo) ){
                validacion = false;
                JsfUtil.mensajeError("No puedo adicionar un vigilante más por que se llegó al tope de capacidad del local: "+aforo);
            }
            if((totalVigilantesActivos) >= aforoLocalPermitido){
                validacion = false;
                JsfUtil.mensajeError("En plataforma Virtual, no puede superar el aforo permitido: "+ aforoLocalPermitido);
            }
        }
        if(validacion){
            for(SspAlumnoCurso alu : lstAlumnosVigilantes){
                if(!validaAlumnoActivo(alu)){
                    continue;
                }
                if(Objects.equals(alu.getPersonaId().getNumDoc(), numDocVigilante)){
                    validacion = false;
                    JsfUtil.mensajeError("La persona ya ha sido ingresada al listado");
                    break;
                }
            }
        }
        
        if(validacion){
            if(alumnoVigilante.getPersonaId() == null){
               alumnoVigilante.setPersonaId(new SbPersonaGt());
            }
            alumnoVigilante.getPersonaId().setTipoDoc(tipoDocVigilante);
            alumnoVigilante.getPersonaId().setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
            alumnoVigilante.getPersonaId().setNumDoc(numDocVigilante);
            alumnoVigilante.getPersonaId().setNombres(nombresVigilante);
            alumnoVigilante.getPersonaId().setApePat(apePatVigilante);
            alumnoVigilante.getPersonaId().setApeMat(apeMatVigilante);
            alumnoVigilante.getPersonaId().setActivo(JsfUtil.TRUE);
            alumnoVigilante.getPersonaId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
            alumnoVigilante.getPersonaId().setAudNumIp(JsfUtil.getIpAddress());
            alumnoVigilante.getPersonaId().setGeneroId(sexoVigilante);
            alumnoVigilante.getPersonaId().setFechaNac(fechaNacimiento);
            if(alumnoVigilante.getPersonaId().getSbMedioContactoList() == null){
                alumnoVigilante.getPersonaId().setSbMedioContactoList(new ArrayList());
            }
            if(!renderDireccionCombo){
                SbDireccionGt direc = new SbDireccionGt();
                direc.setId(null);
                direc.setActivo(JsfUtil.TRUE);
                direc.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                direc.setAudNumIp(JsfUtil.getIpAddress());
                direc.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DIRECB_FIS"));
                direc.setViaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_VIA_CAL"));
                direc.setZonaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_ZON_URB"));                   
                direc.setPersonaId(alumnoVigilante.getPersonaId());
                direc.setDireccion(direccionVigilante);
                direc.setDistritoId(ubigeoVigilante);
                direc.setPaisId(ejbSbPaisFacade.obtenerPaisByNombre("PERU"));
                if(alumnoVigilante.getPersonaId().getSbDireccionList() == null){
                   alumnoVigilante.getPersonaId().setSbDireccionList(new ArrayList());
                }
                direc = (SbDireccionGt) JsfUtil.entidadMayusculas(direc, "");
                alumnoVigilante.getPersonaId().getSbDireccionList().add(direc);
            }else{
                direccionCombo.setDistritoId(ubigeoVigilante);
                int cont = 0;
                for(SbDireccionGt dire : alumnoVigilante.getPersonaId().getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && !Objects.equals(dire.getId(), direccionCombo.getId())){
                        cont++;
                    }
                }
                if(cont > 0){
                    for(SbDireccionGt dire : alumnoVigilante.getPersonaId().getSbDireccionList()){
                        if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                            dire.setActivo(JsfUtil.FALSE);
                        }
                    }
                    alumnoVigilante.getPersonaId().getSbDireccionList().add(direccionCombo);
                }
            }
            if(alumnoVigilante.getPersonaId().getSbMedioContactoList() == null){
                alumnoVigilante.getPersonaId().setSbMedioContactoList(new ArrayList());
            }
            if(!Objects.equals(correo, correoTemp)){
                if(correo != null && !correo.isEmpty()){
                        SbMedioContactoGt nuevoCorreo = new SbMedioContactoGt();
                        nuevoCorreo.setActivo(JsfUtil.TRUE);
                        nuevoCorreo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        nuevoCorreo.setAudNumIp(JsfUtil.getIpAddress());
                        nuevoCorreo.setId(null);
                        nuevoCorreo.setPersonaId(alumnoVigilante.getPersonaId());
                        nuevoCorreo.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_COR"));
                        nuevoCorreo.setValor(correo);
                        nuevoCorreo.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                        nuevoCorreo = (SbMedioContactoGt) JsfUtil.entidadMayusculas(nuevoCorreo, "");
                        alumnoVigilante.getPersonaId().getSbMedioContactoList().add(nuevoCorreo);
                }else{
                        if(alumnoVigilante.getPersonaId().getSbMedioContactoList() != null){
                            for(SbMedioContactoGt medioCorreo : alumnoVigilante.getPersonaId().getSbMedioContactoList() ){
                                if(medioCorreo.getTipoId().getCodProg().equals("TP_MEDCO_COR") && medioCorreo.getValor().equals(correoTemp) ){
                                    medioCorreo.setActivo(JsfUtil.FALSE);
                                }
                            }
                        }
                }
            }
            if(!Objects.equals(telefonoFijo, telefonoFijoTemp)){
                if(telefonoFijo != null && !telefonoFijo.isEmpty()){
                        SbMedioContactoGt nuevoTelefonoFijo = new SbMedioContactoGt();
                        nuevoTelefonoFijo.setActivo(JsfUtil.TRUE);
                        nuevoTelefonoFijo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        nuevoTelefonoFijo.setAudNumIp(JsfUtil.getIpAddress());
                        nuevoTelefonoFijo.setId(null);
                        nuevoTelefonoFijo.setPersonaId(alumnoVigilante.getPersonaId());
                        nuevoTelefonoFijo.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_FIJ"));
                        nuevoTelefonoFijo.setValor(telefonoFijo);
                        nuevoTelefonoFijo.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                        nuevoTelefonoFijo = (SbMedioContactoGt) JsfUtil.entidadMayusculas(nuevoTelefonoFijo, "");
                        alumnoVigilante.getPersonaId().getSbMedioContactoList().add(nuevoTelefonoFijo);
                }else{
                        if(alumnoVigilante.getPersonaId().getSbMedioContactoList() != null){
                            for(SbMedioContactoGt medioTelefonoFijo : alumnoVigilante.getPersonaId().getSbMedioContactoList() ){
                                if(medioTelefonoFijo.getTipoId().getCodProg().equals("TP_MEDCO_FIJ") && medioTelefonoFijo.getValor().equals(telefonoFijoTemp) ){
                                    medioTelefonoFijo.setActivo(JsfUtil.FALSE);
                                }
                            }
                        }
                }
            }
            if(!Objects.equals(telefonoMovil, telefonoMovilTemp)){
                if(telefonoMovil != null && !telefonoMovil.isEmpty()){
                        SbMedioContactoGt nuevoTelefonoMovil = new SbMedioContactoGt();
                        nuevoTelefonoMovil.setActivo(JsfUtil.TRUE);
                        nuevoTelefonoMovil.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        nuevoTelefonoMovil.setAudNumIp(JsfUtil.getIpAddress());
                        nuevoTelefonoMovil.setId(null);
                        nuevoTelefonoMovil.setPersonaId(alumnoVigilante.getPersonaId());
                        nuevoTelefonoMovil.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_MOV"));
                        nuevoTelefonoMovil.setValor(telefonoMovil);
                        nuevoTelefonoMovil.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                        nuevoTelefonoMovil = (SbMedioContactoGt) JsfUtil.entidadMayusculas(nuevoTelefonoMovil, "");
                        alumnoVigilante.getPersonaId().getSbMedioContactoList().add(nuevoTelefonoMovil);
                }else{
                        if(alumnoVigilante.getPersonaId().getSbMedioContactoList() != null){
                            for(SbMedioContactoGt medioTelefonoMovil : alumnoVigilante.getPersonaId().getSbMedioContactoList() ){
                                if(medioTelefonoMovil.getTipoId().getCodProg().equals("TP_MEDCO_MOV") && medioTelefonoMovil.getValor().equals(telefonoMovilTemp) ){
                                    medioTelefonoMovil.setActivo(JsfUtil.FALSE);
                                }
                            }
                        }
                }
            }
            alumnoVigilante = (SspAlumnoCurso) JsfUtil.entidadMayusculas(alumnoVigilante, "");
            if(alumnoVigilante.getPersonaId() != null){
                alumnoVigilante.setPersonaId((SbPersonaGt) JsfUtil.entidadMayusculas(alumnoVigilante.getPersonaId(), ""));
            }
            
            if(!Objects.equals(nombreFoto, nombreFotoTemporal)){
                SspPersonaFoto nuevaFoto = new SspPersonaFoto();
                nuevaFoto.setId(null);
                nuevaFoto.setActivo(JsfUtil.TRUE);
                nuevaFoto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                nuevaFoto.setAudNumIp(JsfUtil.getIpAddress());                
                nuevaFoto.setAlumnoId(alumnoVigilante);
                nuevaFoto.setFecha(new Date());
                nuevaFoto.setNombreFoto(nombreFoto);
                nuevaFoto.setModuloId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GSSP_CURS"));
                nuevaFoto.setSspAlumnoCursoList(new ArrayList());
                nuevaFoto.getSspAlumnoCursoList().add(alumnoVigilante);
                nuevaFoto = (SspPersonaFoto) JsfUtil.entidadMayusculas(nuevaFoto, "");
                alumnoVigilante.setFotoId(nuevaFoto);
                
                Map nmap = new HashMap();
                nmap.put("id", alumnoVigilante.getId());
                nmap.put("nombreFoto", nuevaFoto.getNombreFoto());
                nmap.put("fotoByte", fotoByte );
                lstFotosAlumnos.add(nmap);
            }            
            lstAlumnosVigilantes.add(alumnoVigilante);
            limpiarVigilante();
        }
    }
    
    public boolean validaAlumnoActivo(SspAlumnoCurso alu){
        if(alu.getActivo() == 0){
            return false;
        }
        if(alu.getTipoOpeId() == null){
            return false;
        }
        if(alu.getTipoOpeId().getCodProg().equals("TP_REGIST_NULL")){
            return false;
        }
        return true;
    }
    
    public void eliminarVigilanteSeleccionado(SspAlumnoCurso alumnoSelected){
        if(!lstAlumnosVigilantes.isEmpty()){
            for(SspAlumnoCurso alum : lstAlumnosVigilantes){
                if(Objects.equals(alum.getPersonaId().getNumDoc(), alumnoSelected.getPersonaId().getNumDoc())){
                    
                    for(Map nmap : lstFotosAlumnos){
                        if(Objects.equals((Long) nmap.get("id"), alum.getId())){
                            lstFotosAlumnos.remove(nmap);
                            break;
                        }
                    }
                    
                    lstAlumnosVigilantes.remove(alum);
                    JsfUtil.mensaje("Eliminado de la lista correctamente");
                    break;
                }
            }
        }else{
            JsfUtil.mensajeError("Por favor seleccione al menos a un vigilante");
        }
    }
    
    /**
     * FORMATEAR FECHA PARA MOSTRAR EN TABLA HORARIOS
     * @author Richar Fernández
     * @version 2.0
     * @param fecha Fecha para formatear
     * @return Cadena de fecha formateada
     */
    public String mostrarFechaHorario(Date fecha){
        String fech = "";
        SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
        SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
        fech = formatDia.format(fecha) + " " + formatMes.format(fecha).toUpperCase();
        
        return fech;
    }
    
    public boolean esDomingo(Date fecha){
        boolean valida = false;
        Calendar c_fecha = Calendar.getInstance();
        c_fecha.setTime(fecha);
        int diaSemana = c_fecha.get(Calendar.DAY_OF_WEEK);
        if(diaSemana == 1){ // Domingo
            valida = true;
        }
        return valida;
    }
    
    public void calcularFechasHorarios(){
        if(fechaInicioAgendar != null && fechaFinAgendar != null){
            if(fechaInicioAgendar.compareTo(fechaFinAgendar) <= 0){
                listaFechasDisponibles = new ArrayList();
                int dias = JsfUtil.calculaDiasEntreFechas(fechaInicioAgendar, fechaFinAgendar);
                boolean agregar;
                List<Date> listFeriados = ejbSbFeriadoFacade.listFeriadosCalendario(fechaInicioAgendar, fechaFinAgendar);
                dias ++;

                Calendar c_hoy = Calendar.getInstance();
                c_hoy.setTime(fechaInicioAgendar);
                
                for(int i=0; i<dias; i++){
                    if(esDomingo(c_hoy.getTime())){
                        c_hoy.add(Calendar.DATE, 1);
                       continue; 
                    }
                    agregar = true;
                    for(Date fecha : listFeriados){
                        if(JsfUtil.getFechaSinHora(fecha).equals(JsfUtil.getFechaSinHora(c_hoy.getTime()))){
                            agregar = false;
                        }
                    }
                    if(agregar){
                        listaFechasDisponibles.add(c_hoy.getTime());    
                    }
                    c_hoy.add(Calendar.DATE, 1);
                }
            }else{
                JsfUtil.mensajeError("La fecha inicial no puede ser mayor que la final");
                fechaFinAgendar = null;
                RequestContext.getCurrentInstance().update(obtenerForm()+":fechaFin");
            }
        }
        RequestContext.getCurrentInstance().update(obtenerForm()+":tabAgendar");        
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
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                    nombreFoto = file.getFileName();
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original, comuníquese con el administrador del sistema");
                }
            }
        } catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
        }
    }
    
    //// BUSQUEDA DE LOCAL ////
    
    public void openDlgBuscarLocal(){
        filtro = null;
        ubigeoBusqueda = null;
        lstLocalesBusqueda = new ArrayList();
        
        RequestContext.getCurrentInstance().execute("PF('wvDlgBusquedaLocal').show()");
        RequestContext.getCurrentInstance().update("frmBusquedaLocal");
    }
    
    public void buscarLocalCurso(){
        if( (filtro == null || filtro.isEmpty()) && (ubigeoBusqueda == null) ){
            if(filtro == null || filtro.isEmpty()){
                JsfUtil.invalidar("frmBusquedaLocal:filtroBusqueda");   
            }
            if(ubigeoBusqueda == null){
                JsfUtil.invalidar("frmBusquedaLocal:ubigeoLocal");    
            }
            JsfUtil.mensajeError("Por favor ingrese al menos un filtro de búsqueda");
        }else{
            if(filtro != null){
                filtro = filtro.trim();
            }
            lstLocalesBusqueda = ejbSspLocalFacade.buscarLocalByNombreDireccionByUbigeo(filtro, ((ubigeoBusqueda != null)?ubigeoBusqueda.getId():null), null);
        }
    }
    
    public void seleccionarLocalCurso(SspLocal loc){
        
        if(estado == EstadoCrud.RECTIFICAR){
            if(Objects.equals(loc.getId(), programacionConfirma.getLocalId().getId())){
                JsfUtil.mensajeError("Por favor seleccionar un local distinto al local actual del curso");
                return;
            }
        }
        
        limpiarLocal();
        localSelected = loc;
        
        nombreLocal = loc.getNombreLocal();
        ubigeoLocal = loc.getDistritoId();
        direccionLocal = loc.getDireccion();
        tipoUbicacionLocal = loc.getTipoUbicacionId();
        referenciaLocal = loc.getReferencia();
        if(loc.getAforo() != null){
            aforoLocal = ""+loc.getAforo();
        }
        if(loc.getGeoLat() != null){
            latitudLocal = ""+loc.getGeoLat();
            latitudSelected = ""+loc.getGeoLat();
        }
        if(loc.getGeoLong() != null){
            longitudLocal = ""+loc.getGeoLong();
            longitudSelected = ""+loc.getGeoLong();
        }
        for(SspLocalContacto cont : loc.getSspLocalContactoList()){
            if(cont.getActivo() == 1){
                if(cont.getTipoMedioId().getCodProg().equals("TP_MEDIOC_TEL") ){
                    telefonoLocal = cont.getValor();
                }
                if(cont.getTipoMedioId().getCodProg().equals("TP_MEDIOC_COR") ){
                    correoLocal = cont.getValor();
                }
            }
        }
        setDisabledLocal(true);        
        setDisabledLocalEdicion(true);
        if(latitudSelected == null || latitudSelected.isEmpty() || longitudSelected == null || longitudSelected.isEmpty()){
            setDisabledLocalEdicion(false);
        }
        
        RequestContext.getCurrentInstance().execute("PF('wvDlgBusquedaLocal').hide()");        
        
        if(estado == EstadoCrud.RECTIFICAR){
            RequestContext.getCurrentInstance().update("frmCambiarLocal");
        }else{
            //RequestContext.getCurrentInstance().execute("tab01()");
            RequestContext.getCurrentInstance().update(obtenerForm());    
        }
        
    }
    
    public void eliminarHorarioSeleccionado(SspPrograHora hora){
        if(!lstProgramacion.isEmpty()){
            boolean eliminarPadre = false;
            SspProgramacion programacionPadre = null;
            for(SspProgramacion p : lstProgramacion){
                for(SspPrograHora h : p.getSspPrograHoraList()){
                    if(Objects.equals(h.getId(), hora.getId())){
                        p.getSspPrograHoraList().remove(h);
                        JsfUtil.mensaje("Eliminado de la lista correctamente");
                        if(p.getSspPrograHoraList().isEmpty()){
                            eliminarPadre = true;
                            programacionPadre = p;
                        }
                        break;
                    }
                }
            }
            if(eliminarPadre){
                for(SspProgramacion p : lstProgramacion){
                    if(Objects.equals(p.getId(), programacionPadre.getId())){
                        lstProgramacion.remove(p);
                        break;
                    }
                }
            }
        }else{
            JsfUtil.mensajeError("Por favor ingrese al menos una programación");
        }
    }
    
    public void eliminarProgramacionSeleccionada(SspProgramacion progra){
        if(!lstProgramacion.isEmpty()){
            for(SspProgramacion p : lstProgramacion){
                if(Objects.equals(p.getId(), progra.getId())){
                    lstProgramacion.remove(p);
                    JsfUtil.mensaje("Eliminado de la lista correctamente");
                    break;
                }
            }
        }else{
            JsfUtil.mensajeError("Por favor ingrese al menos una programación");
        }
    }
    
    public void openDlgVerLocales(SspRegistroCurso cRegistro){
        registro = cRegistro;
        revisarSiEsVirtual(registro);
        RequestContext.getCurrentInstance().execute("PF('wvDlgVerLocales').show()");
        RequestContext.getCurrentInstance().update("frmVerLocales");
    }
    
    public void revisarSiEsVirtual(SspRegistroCurso cRegistro){
        esVirtual = false;
        if(cRegistro != null && cRegistro.getSspProgramacionList() != null){
            for(SspProgramacion progra : cRegistro.getSspProgramacionList()){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(progra.getLocalId().getTipoUbicacionId().getCodProg().equals("TP_VIA_VIRT")){
                    esVirtual = true;
                }
                break;
            }
        }
    }
    
    public void openDlgVerInstructores(SspRegistroCurso cRegistro){
        registro = cRegistro;
        RequestContext.getCurrentInstance().execute("PF('wvDlgVerInstructores').show()");
        RequestContext.getCurrentInstance().update("frmVerInstructores");
    }
    
    public void openDlgVerVigilantes(SspRegistroCurso cRegistro){
        registro = cRegistro;
        lstFotosAlumnos = new ArrayList();
        RequestContext.getCurrentInstance().execute("PF('wvDlgVerVigilantes').show()");
        RequestContext.getCurrentInstance().update("frmVerVigilantes");
    }
    
    public List<SspLocal> obtenerListadoLocalesBandeja(){
        List<SspLocal> listaLocales = new ArrayList();
        if(registro != null && registro.getSspProgramacionList() != null){
            for(SspProgramacion progra : registro.getSspProgramacionList()){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                    continue;
                }
                if(!listaLocales.contains(progra.getLocalId())){
                    listaLocales.add(progra.getLocalId());
                }
            }
        }
        return listaLocales;
    }
    
    public List<SspInstructor> obtenerListadoInstructores(){
        List<SspInstructor> listaInstructores = new ArrayList();
        if(registro != null && registro.getSspProgramacionList() != null && registro.getSspInstructorModuloList() != null){
            for(SspInstructorModulo inst : registro.getSspInstructorModuloList() ){
                if(inst.getActivo() == 1 && Objects.equals(inst.getRegcursoId().getId(), registro.getId() ) && !listaInstructores.contains(inst.getInstructorId()) ){
                    listaInstructores.add(inst.getInstructorId());
                }
            }
        }
        return listaInstructores;
    }
    
    public List<SspAlumnoCurso> obtenerListadoVigilantes(){
        List<SspAlumnoCurso> listaAlumnos = new ArrayList();
        if(registro != null && registro.getSspAlumnoCursoList() != null){
            for(SspAlumnoCurso alumn : registro.getSspAlumnoCursoList()){
                if(!validaAlumnoActivo(alumn)){
                    continue;
                }
                listaAlumnos.add(alumn);
            }
            if(!listaAlumnos.isEmpty()){
                Collections.sort(listaAlumnos, new Comparator<SspAlumnoCurso>() {
                    @Override
                    public int compare(SspAlumnoCurso one, SspAlumnoCurso other) {
                        return ( (one.getPersonaId().getApePat() != null)?one.getPersonaId().getApePat():one.getPersonaId().getNombres()) .compareTo( ((other.getPersonaId().getApePat() != null)?other.getPersonaId().getApePat():other.getPersonaId().getNombres()) );
                    }
                });
            }
        }
        return listaAlumnos;
    }

    public List<SspProgramacion> getLstProgramacionActivos(){
        List<SspProgramacion> lista = new ArrayList();
        if(lstProgramacion != null){
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 1){
                    lista.add(progra);
                }
            }
            if(!lista.isEmpty()){
                Collections.sort(lista, new Comparator<SspProgramacion>() {
                    @Override
                    public int compare(SspProgramacion one, SspProgramacion other) {
                        return one.getId().compareTo(other.getId());
                    }
                });
            }
        }
        return lista;
    }
    
    public String obtenerFechaCurso(){
        List<SspPrograHora> lista = new ArrayList();
        String fechaHorario = "";
        if(lstProgramacion != null){
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                    continue;
                }
                
                for(SspPrograHora horario : progra.getSspPrograHoraList()){
                    if(horario.getActivo() == 0){
                        continue;
                    }
                    lista.add(horario);    
                }
                
            }
            if(!lista.isEmpty()){
                Collections.sort(lista, new Comparator<SspPrograHora>() {
                    @Override
                    public int compare(SspPrograHora one, SspPrograHora other) {
                        return one.getFecha().compareTo(other.getFecha());
                    }
                });
                fechaHorario = "Curso del "+ JsfUtil.formatearFecha(lista.get(0).getFecha(), "dd/MM/yyyy") + " al " + JsfUtil.formatearFecha(lista.get(lista.size()-1).getFecha(), "dd/MM/yyyy") ;
            }
        }
        return fechaHorario;
    }
    
    public List<SspProgramacion> getLstProgramacionSinRefrigerioActivos(){
        List<SspProgramacion> lista = new ArrayList();
        if(lstProgramacion != null){
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 1 && !progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP")){
                    lista.add(progra);
                }
            }
            Collections.sort(lista, new Comparator<SspProgramacion>() {
                @Override
                public int compare(SspProgramacion one, SspProgramacion other) {
                    return one.getFechaHoraIncio().compareTo(other.getFechaHoraIncio());
                }
            });            
        }
        return lista;
    }
    
    public List<SspPrograHora> getSspPrograHoraListActivos(List<SspPrograHora> lstHorarios){
        List<SspPrograHora> lista = new ArrayList();
        if(lstHorarios != null){
            for(SspPrograHora horario : lstHorarios){
                if(horario.getActivo() == 1){
                    lista.add(horario);
                }
            }
            Collections.sort(lista, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });
        }
        return lista;
    }
    
    public List<SspAlumnoCurso> getLstAlumnosVigilantesActivos() {
        List<SspAlumnoCurso> lstAlumnos = new ArrayList();
        if(lstAlumnosVigilantes != null){
            for(SspAlumnoCurso alumno : lstAlumnosVigilantes){
                if(!validaAlumnoActivo(alumno)){
                    continue;
                }
                lstAlumnos.add(alumno);
            }
            
            Collections.sort(lstAlumnos, new Comparator<SspAlumnoCurso>() {
                @Override
                public int compare(SspAlumnoCurso one, SspAlumnoCurso other) {
                    return ( (one.getPersonaId().getApePat() != null)?one.getPersonaId().getApePat():one.getPersonaId().getNombres()) .compareTo( ((other.getPersonaId().getApePat() != null)?other.getPersonaId().getApePat():other.getPersonaId().getNombres()) );
                }
            });
        }
        return lstAlumnos;
    }
    
    public void updateFormMapa(){
        RequestContext.getCurrentInstance().update("frmMapa");
        RequestContext.getCurrentInstance().execute("startProcessMap()");
    }
    
    public void cambiarUbigeoLugarServicio(){
        latitudSelected = null;
        longitudSelected = null;
        latitudLocal = null;
        longitudLocal = null;
    }
    
    public String obtenerNombreUbigeoReferenciado(SbDistritoGt dist){
        if(dist != null){
            return dist.getProvinciaId().getDepartamentoId().getNombre() + " / " + dist.getProvinciaId().getNombre() + " / " + dist.getNombre();
        }
        return "";
    }
    
    public void cambiaTipoContactoVigilante(){
        valorContactoVigilante = null;
    }

    public void onTabChange(TabChangeEvent event) {
        switch(event.getTab().getId().toString()){
            case "tabDatosLocal":
                RequestContext.getCurrentInstance().execute("tab01();");
                break;
                
            case "tabInstructores":
                RequestContext.getCurrentInstance().execute("tab02();");
                break;
                
            case "tabAgendar":
                RequestContext.getCurrentInstance().execute("tab03();");
                break;
                
            case "tabVigilante":
                RequestContext.getCurrentInstance().execute("tab04();");
                break;
        }
    }
       
    ///////////////// BANDEJA DE INSTRUCTOR ////////////////////////
    public String prepareBandejaInstructor(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        if(p_user == null){
            JsfUtil.mensajeError("Sesión del sistema vencida. Vuelva a loguearse");
            return null;
        }
        int cont = ejbRmaCursosFacade.contarInstructorConDiasLimiteVctoFichaByDoc(p_user.getLogin(), 60);
        if(cont > 0){
            reiniciarCamposBusqueda();
            obtenerListadoLocales();
            resultadosBandeja = null;
            estado = EstadoCrud.BANDEJA;
            esInstructor = true;
            cargarParametrosGlobales();
            return "/aplicacion/gssp/sspRegistroCurso/BandejaInstructor";
        }else{
            JsfUtil.mensajeError("Ud. no cuenta con una ficha acreditada de instructor vigente");
        }
        return null;   
    }
    
    public void buscarBandejaInstructor(){
        boolean validacion = true;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");

        if(tipoEstado == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":tipoEstado");
            JsfUtil.mensajeAdvertencia("Es necesario ingresar el estado a filtrar");
        }else{
            if(buscarPor != null && (filtro == null || filtro.isEmpty())){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":filtroBusqueda");
                JsfUtil.mensajeAdvertencia("Es necesario ingresar el campo a buscar");
            }
        }
        if(filtro != null){
            filtro = filtro.trim();
        }
        if(buscarPor != null){
            buscarPor = buscarPor.trim();
        }
        SbPersonaGt inst = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getLogin()); // Búsqueda de innstructor por el login
        
        if(validacion){
            HashMap mMap = new HashMap();
            mMap.put("fechaIni", fechaInicio);
            mMap.put("fechaFin", fechaFinal);
            mMap.put("buscarPor", buscarPor);
            mMap.put("filtro", filtro);
            mMap.put("tipoEstado", tipoEstado);
            mMap.put("instructor", inst.getId());
            mMap.put("nroProceso", ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_nroProcesosTotal").getValor());
            mMap.put("tipoFormacion", (tipoFormacion != null)?tipoFormacion.getId():null);
            mMap.put("local", (local != null)?local.getId():null);
            
            resultadosBandeja = ejbSspRegistroCursoFacade.buscarCursosBandejaInstructor(mMap);
            filtrarBusquedaInstructor();
            reiniciarCamposBusqueda();
        }
    }
    
    public void filtrarBusquedaInstructor(){
        if(resultadosBandeja != null){
            String nroProcesos = ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_nroProcesosTotal").getValor();
            List<SspRegistroCurso> listTemporal = new ArrayList(resultadosBandeja);
            for(SspRegistroCurso reg :listTemporal ){
                if(reg.getEstadoId().getCodProg().equals("TP_ECC_FIN") && reg.getSspNotasList() == null){
                    resultadosBandeja.remove(reg);
                }
                if(reg.getNroExpediente() != null){
                    Expediente ex = ejbExpedienteFacade.obtenerExpedienteXNumero(reg.getNroExpediente());
                    if(ex != null && !nroProcesos.contains(ex.getIdProceso().getIdProceso().toString())){
                        resultadosBandeja.remove(reg);
                    }
                }
            }
        }   
        
        if(tipoEstado != null && tipoEstado.equals("2")){
            int contTotal = 0, contConfirmados = 0;
            List<SspRegistroCurso> lstCursos = new ArrayList();
                    
            for(SspRegistroCurso reg : resultadosBandeja){
                contTotal = 0; 
                contConfirmados = 0;
                for(SspProgramacion progra : reg.getSspProgramacionList()){
                    if(progra.getActivo() == 1 && !progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP") ){
                        contTotal++;
                        if(progra.getInstructorId() != null ){
                            contConfirmados++;
                        }
                    }
                }
                
                if( (contTotal == contConfirmados) && contConfirmados > 0){
                    lstCursos.add(reg);
                }
            }
            resultadosBandeja = new ArrayList(lstCursos);
        }
    }
    
    public String obtenerAdministrado(SbPersonaGt admin){
        String nombre = "";
        if(admin != null){
            if(admin.getRznSocial() != null && !admin.getRznSocial().isEmpty()){
                nombre = admin.getRznSocial();
            }else{
                nombre = admin.getNombreCompleto();
            }
        }
        return nombre;
    }
    
    public boolean renderBtnConfirmar(SspRegistroCurso reg){
        List<SspPrograHora> lstHora = new ArrayList();
        boolean validar = false;
        int cont = 0;
        if(reg != null){
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_CRE") || reg.getEstadoId().getCodProg().equals("TP_ECC_OBS") || reg.getEstadoId().getCodProg().equals("TP_ECC_TRA")
               ){
                validar = true;
                
                for(SspProgramacion progra : reg.getSspProgramacionList()){
                    if(progra.getActivo() == 0){
                        continue;
                    }
                    if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                        continue;
                    }
                    lstHora = new ArrayList();
                    for(SspPrograHora hora : progra.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            lstHora.add(hora);
                        }
                    }
                    Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                        @Override
                        public int compare(SspPrograHora one, SspPrograHora other) {
                            return one.getFecha().compareTo(other.getFecha());
                        }
                    });
                    if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(lstHora.get(0).getFecha())) > 0){
                        validar = false;
                        break;
                    }
                }
            }
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_EVA")
               ){                
                for(SspProgramacion progra : reg.getSspProgramacionList()){
                    if(progra.getActivo() == 0){
                        continue;
                    }
                    if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                        continue;
                    }
                    if(progra.getInstructorId() == null){
                        cont++;
                        break;
                    }
                }
                if(cont > 0){
                    validar = true;
                }
            }
        }
        
        return validar;
    }
    
    public boolean renderBtnActualizar(SspRegistroCurso reg){
        boolean validar = false;
        int cont = 0;
        if(reg != null){
            if(!reg.getEstadoId().getCodProg().equals("TP_ECC_EVA") ){
                validar = false;
            }else{
                /// Se valida que estén todos los cursos confirmados
                for(SspProgramacion progra : reg.getSspProgramacionList()){
                    if(progra.getActivo() == 0){
                        continue;
                    }
                    if(progra.getInstructorId() == null && !progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP") ){
                        cont++;
                        break;
                    }
                }
                if(cont == 0){
                    List<SspPrograHora> lstHora;
                    // Todos los cursos confirmados
                    for(SspProgramacion progra : reg.getSspProgramacionList()){
                        if(progra.getActivo() == 0){
                            continue;
                        }
                        if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                            continue;
                        }
                        ////////
                        lstHora = new ArrayList();
                        for(SspPrograHora hora : progra.getSspPrograHoraList()){
                            if(hora.getActivo() == 1){
                                lstHora.add(hora);
                            }
                        }
                        Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                            @Override
                            public int compare(SspPrograHora one, SspPrograHora other) {
                                return other.getHoraFin().compareTo(one.getHoraFin());
                            }
                        });
                        ////////////
                        if((new Date()).compareTo(lstHora.get(0).getHoraFin()) >= 0){
                            validar = true;
                            break;
                        }
                    }
                }   
            }
        }
        return validar;
    }
    
    public void mostrarConfirmar(SspRegistroCurso reg){
        String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");	
        reiniciarValores();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        registro = reg;
        revisarSiEsVirtual(registro);
        instructorPersona = ejbSbPersonaFacade.buscarPersonaSel("", p_user.getLogin());
        lstProgramacion = registro.getSspProgramacionList();
	for(SspProgramacion progra : reg.getSspProgramacionList()){
            if(progra.getActivo() == 1 && !excepcionModulos.contains(progra.getModuloId().getCodModulo()) ){
                DIAS_PRESENTACION = ((progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 7 || progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 15)?(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoLima").getValor())):(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoProvincia").getValor())));
		break;
            }
        }
        cargarModulosNoRepetidos();
        fechaLimite = obtenerFechaLimiteCurso(reg);
        calcularFechaMinima();
        
        lstObservaciones = new ArrayList();
        if(reg.getEstadoId().getCodProg().equals("TP_ECC_OBS")){
            for(SspCursoEvento ce : reg.getSspCursoEventoList()){
                if(ce.getActivo() == 1 && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")){
                    lstObservaciones.add(ce);
                }
            }
            if(!lstObservaciones.isEmpty()){
                DIAS_OBSERVACION = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaObservacion").getValor());
                Collections.sort(lstObservaciones, new Comparator<SspCursoEvento>() {
                    @Override
                    public int compare(SspCursoEvento one, SspCursoEvento other) {
                        return other.getFecha().compareTo(one.getFecha());
                    }
                });
                Calendar c_hoy = Calendar.getInstance();
                Calendar c_ffin = Calendar.getInstance();
                c_ffin.setTime(lstObservaciones.get(0).getFecha());
                c_ffin.add(Calendar.DATE, DIAS_OBSERVACION);

                int feriados = -1;
                int domingos = -1;
                Date fechaIniTemp = lstObservaciones.get(0).getFecha();
                while( (feriados + domingos) != 0){
                    feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(c_ffin.getTime()));
                    domingos = JsfUtil.calcularNroDiasSabadoYDomingoRango(JsfUtil.getFechaSinHora(fechaIniTemp), JsfUtil.getFechaSinHora(c_ffin.getTime()));
                    if((feriados + domingos) > 0){
                        c_hoy.setTime(c_ffin.getTime());
                        c_hoy.add(Calendar.DATE, 1);
                        fechaIniTemp = c_hoy.getTime();
                        c_ffin.add(Calendar.DATE, (feriados + domingos));
                        
                        int diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
                        if(diaSemana == 1){
                           c_hoy.setTime(fechaIniTemp);
                           c_hoy.add(Calendar.DATE, 1);
                           fechaIniTemp = c_hoy.getTime();                    
                       }
                    }
                }             
                fechaObs = c_ffin.getTime();

                if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fechaObs)) > 0){
                    JsfUtil.mensajeError("No puede editar el registro porque han pasado los "+ DIAS_OBSERVACION + " días permitidos para subsanar");                        
                }
            }
        }
        estado = EstadoCrud.CONFIRMAR;
    }
    
    public void mostrarActualizar(SspRegistroCurso reg, boolean esInstructor){
        String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");
        reiniciarValores();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        registro = reg;
        this.esInstructor = esInstructor;
        if(esInstructor){
            instructorPersona = ejbSbPersonaFacade.buscarPersonaSel("", p_user.getLogin());
        }
        lstProgramacion = registro.getSspProgramacionList();
        for(SspProgramacion progra : reg.getSspProgramacionList()){
            if(progra.getActivo() == 1 && !excepcionModulos.contains(progra.getModuloId().getCodModulo()) ){
                DIAS_PRESENTACION = ((progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 7 || progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 15)?(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoLima").getValor())):(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoProvincia").getValor())));
		break;
            }
        }
        cargarModulosNoRepetidos();
        calcularFechaMinima();
        estado = EstadoCrud.ACTUALIZAR;
        revisarSiEsVirtual(registro);
        validarFechaCorte(reg);
    }
    
    public String obtenerFechaLimiteCurso(SspRegistroCurso reg){
        String fecha = "";
        List<SspProgramacion> lstTemp = new ArrayList(reg.getSspProgramacionList());
        List<SspPrograHora> lstHora = new ArrayList();        
        
        for(SspProgramacion progra : lstTemp){
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                continue;
            }
            if(progra.getActivo() == 1 ){
                for(SspPrograHora hora : progra.getSspPrograHoraList()){
                    if(hora.getActivo() == 1){
                        lstHora.add(hora);
                    }
                }
            }
        }
        Collections.sort(lstHora, new Comparator<SspPrograHora>() {
            @Override
            public int compare(SspPrograHora one, SspPrograHora other) {
                return one.getFecha().compareTo(other.getFecha());
            }
        });
        Date fechaIni = lstHora.get(0).getFecha();
        Calendar c_hoy = Calendar.getInstance();
        c_hoy.setTime(fechaIni);
        c_hoy.add(Calendar.DAY_OF_MONTH, (DIAS_PRESENTACION * -1) );
                
        int feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(c_hoy.getTime()), fechaIni);
        int domingos = JsfUtil.calcularNroDiasSabadoYDomingoRango(JsfUtil.getFechaSinHora(c_hoy.getTime()), fechaIni);
        if((feriados+domingos) > 0){
            c_hoy.add(Calendar.DAY_OF_MONTH, ((feriados+domingos) * -1) );
        }
        
        fecha = JsfUtil.formatoFechaDdMmYyyy(c_hoy.getTime());
        return fecha;
    }
    
    public Date obtenerFechaLimiteCursoDate(SspRegistroCurso reg){
        List<SspProgramacion> lstTemp = new ArrayList(reg.getSspProgramacionList());
        List<SspPrograHora> lstHora = new ArrayList();        
        
        for(SspProgramacion progra : lstTemp){
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                continue;
            }
            if(progra.getActivo() == 1 ){
                for(SspPrograHora hora : progra.getSspPrograHoraList()){
                    if(hora.getActivo() == 1){
                        lstHora.add(hora);
                    }
                }
            }
        }
        Collections.sort(lstHora, new Comparator<SspPrograHora>() {
            @Override
            public int compare(SspPrograHora one, SspPrograHora other) {
                return one.getFecha().compareTo(other.getFecha());
            }
        });
        Date fechaIni = lstHora.get(0).getFecha();
        Calendar c_hoy = Calendar.getInstance();
        Calendar c_adicional = Calendar.getInstance();
        c_hoy.setTime(fechaIni);
        c_hoy.add(Calendar.DAY_OF_MONTH, (DIAS_PRESENTACION * -1) );
        int feriados = -1;
        int domingos = -1;
        Date fechaIniTemp = c_hoy.getTime();
        Calendar fechaFinTemp = Calendar.getInstance();
        fechaFinTemp.setTime(fechaIni);
        
        while( (feriados + domingos) != 0){
            feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(fechaIniTemp), fechaFinTemp.getTime());
            domingos = JsfUtil.calcularNroDiasSabadoYDomingo(JsfUtil.getFechaSinHora(fechaIniTemp), fechaFinTemp.getTime());
            if((feriados+domingos) > 0){
                fechaFinTemp.setTime(fechaIniTemp);
                fechaFinTemp.add(Calendar.DAY_OF_MONTH, -1);
                
                c_adicional.setTime(fechaIniTemp);
                c_adicional.add(Calendar.DAY_OF_MONTH, ((feriados+domingos) * -1) );
                fechaIniTemp = c_adicional.getTime();
            }else{
                c_adicional.setTime(fechaIniTemp);
                int diaSemana = c_adicional.get(Calendar.DAY_OF_WEEK);
                if(diaSemana == 1 || diaSemana == 7){
                    c_adicional.add(Calendar.DAY_OF_MONTH, -1);
                     fechaIniTemp = c_adicional.getTime();
                }
            }
        }
        c_hoy.setTime(fechaIniTemp);
        return c_hoy.getTime();
    }
    
    public void openDlgBuscarMapa(){
        modelMap = new DefaultMapModel();
        if(ubigeoLocal == null){
            JsfUtil.invalidar(obtenerForm()+":accordionPanel:ubigeo");
            JsfUtil.mensajeError("Por favor seleccione primero la ubicación del local");
            return;
        }
                
        RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').show()");
        RequestContext.getCurrentInstance().update("frmMapa");
    }
    
    public String obtenerFechaLimiteConfirmacion(SspProgramacion progra){
        String fecha = "-";
        List<SspPrograHora> lstHora;
        int cont = 0;
        if(fechaLimite != null){
            fecha = fechaLimite;
            if(progra.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_EVA")){
                lstHora = new ArrayList();
                for(SspPrograHora hora : progra.getSspPrograHoraList()){
                    if(hora.getActivo() == 1){
                        lstHora.add(hora);
                    }
                }
                Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                    @Override
                    public int compare(SspPrograHora one, SspPrograHora other) {
                        return other.getFecha().compareTo(one.getFecha());
                    }
                });
                
                fecha = JsfUtil.formatoFechaDdMmYyyy(lstHora.get(0).getFecha());    //progra.getFechaHoraFin()
                return fecha;
            }
            
            for(SspInstructorModulo instModulo : registro.getSspInstructorModuloList()){
                if(Objects.equals(instModulo.getInstructorId().getPersonaId().getId(), instructorPersona.getId()) &&
                   Objects.equals(instModulo.getRegcursoId().getId(), registro.getId()) &&
                   Objects.equals(instModulo.getModuloId().getId(), progra.getModuloId().getId()) &&
                   instModulo.getActivo() == 1
                   ){
                    cont ++;
                }
            }
            if(cont == 0){
                fecha = "-";
            }
            
        }
        return fecha;
    }
    
    public void openDlgConfirmarCurso(SspModulo modulo){
        if(modulo != null){
            List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    lstProgramacionConfirma.add(progra);
                }
            }
            
            if(!lstProgramacionConfirma.isEmpty()){
                Collections.sort(lstProgramacionConfirma, new Comparator<SspProgramacion>() {
                    @Override
                    public int compare(SspProgramacion one, SspProgramacion other) {
                        return one.getId().compareTo(other.getId());
                    }
                });
                
                if(!validacionConfirmacionInstructor(lstProgramacionConfirma, modulo)){
                    return;
                }
                
                programacionConfirma = lstProgramacionConfirma.get(0);
                if(programacionConfirma.getInstructorId() == null){
                    nombrePopup = JsfUtil.bundleBDIntegrado("sspCursos_confirmacion_aceptar");
                    msjePopUp = JsfUtil.bundleBDIntegrado("sspCursos_confirmacion_msjeAceptar");
                    tipoConfirmacion = 1;   // Confirmación
                }else{
                    nombrePopup = JsfUtil.bundleBDIntegrado("sspCursos_confirmacion_liberar");
                    msjePopUp = JsfUtil.bundleBDIntegrado("sspCursos_confirmacion_msjeLiberar");
                    tipoConfirmacion = 2;   // Liberación
                }
            }
            RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarTutoria').show()");
            RequestContext.getCurrentInstance().update("frmConfirmarTutoria");
        }
    }
    
    public boolean validacionConfirmacionInstructor(List<SspProgramacion> lstProgramacionConfirma, SspModulo modulo){
        boolean validacion = true;
        String tipoPersona = "1";
        
        if(instructorPersona != null){
            if(instructorPersona.getTipoDoc().getCodProg().equals("TP_DOCID_CE")){
                tipoPersona = "2";
            }else{
                tipoPersona = "1";
            }
        }
        
        //////////////// Búsqueda de datos de instructor en Disca ////////////////
        List<Map> lstDatosInstructores= ejbRmaCursosFacade.buscarInstructorByTipoDocByDocByCurso(tipoPersona, instructorPersona.getNumDoc().trim(), modulo.getNombre());
        if(lstDatosInstructores == null) 
            lstDatosInstructores = new ArrayList();

        if(!lstDatosInstructores.isEmpty()){
            Date fechaCaducidad = (Date) lstDatosInstructores.get(0).get("FECHA_CADUCIDAD");
            List<SspPrograHora> lstHora = new ArrayList();
            
            for(SspProgramacion prog: lstProgramacionConfirma){
                for(SspPrograHora hora : prog.getSspPrograHoraList()){
                    if(hora.getActivo() == 0){
                        continue;
                    }
                    lstHora.add(hora);
                }                
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return other.getFecha().compareTo(one.getFecha());
                }
            });
            Date fechaFinModulo = lstHora.get(0).getFecha();
            if(JsfUtil.getFechaSinHora(fechaFinModulo).compareTo(JsfUtil.getFechaSinHora(fechaCaducidad)) > 0 ){
                JsfUtil.mensajeError("No puede confirmar porque el curso en su ficha de instrucción no estará vigente durante el dictado del curso ");
                validacion = false;
            }
        }else{
            JsfUtil.mensajeError("No se encontró datos del instructor");
            validacion = false;
        }
        /////////////////////////////////////////////////////////////
        
        return validacion;
    }
    
    public String obtenerNombreBtnConfirmacion(SspModulo modulo){
        String nombre = "";
        int conTotal = 0, contInst = 0;
        if(modulo != null){
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    conTotal++;
                    if(progra.getInstructorId() != null){
                        contInst++;                        
                    }
                }
            }
            if(conTotal != contInst){
                nombre = "Aceptar";                
            }else{
                nombre = "Liberar";
            }
        }
        return nombre;
    }
    
    public boolean mostrarBtnConfirmarCurso(SspModulo modulo){
        boolean validacion = false;
        int cont;
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        List<SspPrograHora> lstHora = new ArrayList();
        
        if(fechaLimite != null && modulo != null){
            if(modulo.getCodModulo().equals("REFB") || modulo.getCodModulo().equals("REFP") ){
                return false;
            }
            Calendar c_hoy = Calendar.getInstance();
            c_hoy.setTime(JsfUtil.stringToDate(fechaLimite, "dd/MM/yyyy"));
            
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    lstProgramacionConfirma.add(progra);
                }
            }
            if(!lstProgramacionConfirma.isEmpty()){
                for(SspProgramacion prog : lstProgramacionConfirma){
                    if(prog.getActivo() == 1){
                        for(SspPrograHora hora : prog.getSspPrograHoraList()){
                            if(hora.getActivo() == 1){
                                lstHora.add(hora);
                            }
                        }
                    }
                }
                Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                    @Override
                    public int compare(SspPrograHora one, SspPrograHora other) {
                        return other.getFecha().compareTo(one.getFecha());
                    }
                });
       
                SspProgramacion progra = lstHora.get(0).getProgramacionId();
                Date fechaMax = lstHora.get(0).getFecha();
                
                switch(progra.getRegistroCursoId().getEstadoId().getCodProg()){
                    case "TP_ECC_EVA":
                                //////////////////////////////////////////////////////////////////////
                                /// VALIDACIONES PARA CONFIRMACIONES DE TUTORIA EN ESTADO EVALUADO ///
                                //////////////////////////////////////////////////////////////////////
                                /// Validar fecha ///
                                if(JsfUtil.getFechaSinHora(new Date()).compareTo(fechaMax ) <= 0 ){     // progra.getFechaHoraFin()
                                    validacion = true;
                                }
                                if(progra.getInstructorId() != null){
                                    validacion = false;
                                }
                                break;
                    case "TP_ECC_OBS":
                                
                                if( JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fechaObs)) <= 0){
                                    validacion = true;
                                }
                                
                                break;
                    case "TP_ECC_CRE":
                    case "TP_ECC_TRA":
                                if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) <= 0 ){
                                    validacion = true;
                                }
                                break;
                    default:
                                if(progra.getInstructorId() != null){
                                   validacion = false;
                                }
                                break;
                }
                
                /// Validar Usuario confirmado //
                if(progra.getInstructorId() != null){
                    if(!Objects.equals(progra.getInstructorId().getPersonaId().getId(), instructorPersona.getId())){
                        validacion = false;
                        return validacion;
                    }
                }
                // Validar que le pertenezca curso
                if(validacion){
                    cont = 0;
                    for(SspInstructorModulo instModulo : registro.getSspInstructorModuloList()){
                        if(Objects.equals(instModulo.getInstructorId().getPersonaId().getId(), instructorPersona.getId()) &&
                           Objects.equals(instModulo.getRegcursoId().getId(), registro.getId()) &&
                           Objects.equals(instModulo.getModuloId().getId(), modulo.getId()) &&
                           instModulo.getActivo() == 1
                           ){
                            cont ++;
                        }
                    }
                    if(cont == 0){
                        validacion = false;
                    }
                }
                
            }
        }
        return validacion;
    }
    
    public boolean mostrarTextFechaLimiteCurso(SspModulo modulo){
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        List<SspPrograHora> lstHora = new ArrayList();
        boolean validacion = false;
        int cont = 0;
        if(fechaLimite != null && modulo != null){
            if(modulo.getCodModulo().equals("REFB") || modulo.getCodModulo().equals("REFP") ){
                return false;
            }
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    lstProgramacionConfirma.add(progra);
                }
            }
            if(!lstProgramacionConfirma.isEmpty()){
                for(SspProgramacion prog : lstProgramacionConfirma){
                    if(prog.getActivo() == 1){
                        for(SspPrograHora hora : prog.getSspPrograHoraList()){
                            if(hora.getActivo() == 1){
                                lstHora.add(hora);
                            }
                        }
                    }
                }
                Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                    @Override
                    public int compare(SspPrograHora one, SspPrograHora other) {
                        return other.getFecha().compareTo(one.getFecha());
                    }
                });
                
                SspProgramacion progra = lstHora.get(0).getProgramacionId();
                Date fechaMax = lstHora.get(0).getFecha();
                
                if(progra.getInstructorId() == null){
                    for(SspInstructorModulo instModulo : registro.getSspInstructorModuloList()){
                        if(Objects.equals(instModulo.getInstructorId().getPersonaId().getId(), instructorPersona.getId()) &&
                           Objects.equals(instModulo.getRegcursoId().getId(), registro.getId()) &&
                           Objects.equals(instModulo.getModuloId().getId(), modulo.getId()) &&
                           instModulo.getActivo() == 1
                           ){
                            cont ++;
                        }
                    }
                    if(cont > 0){
                        /// Validar fecha ///
                        Calendar c_hoy = Calendar.getInstance();                    
                        if(progra.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_EVA")){
                            c_hoy.setTime(fechaMax); // progra.getFechaHoraFin()
                        }else{
                            if(progra.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_OBS")){
                                c_hoy.setTime(fechaObs);  
                            }else{
                                c_hoy.setTime(JsfUtil.stringToDate(fechaLimite, "dd/MM/yyyy"));     
                            }
                        }

                        if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) > 0 ){
                            validacion = true;
                        }
                    }
                }
            }
        }
        return validacion;
    }
    
    public boolean tieneCruceHorario(SspProgramacion prograTemp){
        if(prograTemp == null){
            return false;
        }
        for(SspPrograHora hora : prograTemp.getSspPrograHoraList()){
            if(hora.getActivo() == 0){
                continue;
            }
            Date fecha = JsfUtil.getFechaSinHora(hora.getFecha());
            int cont = ejbSspProgramacionFacade.contarFechaRangoProgramacion(prograTemp.getId(), instructorPersona.getNumDoc(), hora.getHoraInicio(), hora.getHoraFin(), fecha);
            if(cont > 0){
                return true;
            }
        }
        return false;
    }
    
    public void confirmarLiberarCurso(){
        if(programacionConfirma != null){
            List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), programacionConfirma.getModuloId().getId())){
                    lstProgramacionConfirma.add(progra);
                }
	    }
            if(!lstProgramacionConfirma.isEmpty()){
                for(SspProgramacion progra : lstProgramacionConfirma){
                    programacionConfirma = progra;
                    
                    SspProgramacion prograTemp = ejbSspProgramacionFacade.obtenerProgramacionById(programacionConfirma.getId());
                    if(tipoConfirmacion == 1){ // Confirmación
                        if(tieneCruceHorario(prograTemp)){
                            JsfUtil.mensajeError("No puede aceptar este curso porque ya tiene otro confirmado en el mismo día y con horas iguales");
                            return;
                        }
                        if(prograTemp.getInstructorId() == null){
                            prograTemp.setInstructorId(ejbSspInstructorFacade.buscarInstructorByNumDoc(instructorPersona.getNumDoc().trim()));
                            programacionConfirma.setInstructorId(prograTemp.getInstructorId());
                            ejbSspProgramacionFacade.edit(prograTemp);
                        }else{
                            //prograTemp = ejbSspProgramacionFacade.obtenerProgramacionById(programacionConfirma.getId());
                            programacionConfirma.setInstructorId(prograTemp.getInstructorId());
                            JsfUtil.mensajeError("No puede aceptar este curso porque acaba de ser confirmada por otro tutor.");
                            return;
                        }
                    }else{ // Liberación
                        prograTemp.setInstructorId(null);
                        programacionConfirma.setInstructorId(null);
                        ejbSspProgramacionFacade.edit(prograTemp);
                    }
                }
                RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarTutoria').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm());
                JsfUtil.mensaje("Registro actualizado correctamente.");
	    }
        }
    }
    
    public boolean mostrarBtnActualizarNotas(SspModulo modulo){
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        List<SspPrograHora> lstHora = new ArrayList();
        boolean validacion = false;
        if(modulo != null){
            if(modulo.getCodModulo().equals("REFB") || modulo.getCodModulo().equals("REFP") ){
                return validacion;
            }
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    lstProgramacionConfirma.add(progra);
                }
	    }
            if(!lstProgramacionConfirma.isEmpty()){
                if(esInstructor){
                    for(SspProgramacion prog : lstProgramacionConfirma){
                        if(prog.getActivo() == 1){
                            for(SspPrograHora hora : prog.getSspPrograHoraList()){
                                if(hora.getActivo() == 1){
                                    lstHora.add(hora);
                                }
                            }
                        }
                    }
                    Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                        @Override
                        public int compare(SspPrograHora one, SspPrograHora other) {
                            return other.getHoraFin().compareTo(one.getHoraFin());
                        }
                    });

                    SspProgramacion progra = lstHora.get(0).getProgramacionId();
                    Date fechaMax = lstHora.get(0).getHoraFin();
                    if(progra.getInstructorId() != null){
                        if( Objects.equals(progra.getInstructorId().getPersonaId().getId(), instructorPersona.getId()) ){
                            validacion = true;
                        }
                    }
                    if(progra.getRegistroCursoId() != null && !progra.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_EVA")){
                        validacion = false;
                    }
                    if((new Date()).compareTo(fechaMax) < 0){
                        validacion = false;
                    }
                }else{
                    //Administrado
                    validacion = true;
                }   
            }
        }
        return validacion;
    }
    
    public boolean mostrartextoCursoNoFinalizado(SspModulo modulo){
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        List<SspPrograHora> lstHora = new ArrayList();
        boolean validacion = false;
        if(modulo != null){
            if(modulo.getCodModulo().equals("REFB") || modulo.getCodModulo().equals("REFP") ){
                return validacion;
            }
            
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    lstProgramacionConfirma.add(progra);
                }
	    }
            if(!lstProgramacionConfirma.isEmpty()){
                for(SspProgramacion prog : lstProgramacionConfirma){
                    if(prog.getActivo() == 1){
                        for(SspPrograHora hora : prog.getSspPrograHoraList()){
                            if(hora.getActivo() == 1){
                                lstHora.add(hora);
                            }
                        }
                    }
                }
                Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                    @Override
                    public int compare(SspPrograHora one, SspPrograHora other) {
                        return other.getHoraFin().compareTo(one.getHoraFin());
                    }
                });
       
                SspProgramacion progra = lstHora.get(0).getProgramacionId();
                Date fechaMax = lstHora.get(0).getHoraFin();
                if(progra.getInstructorId() != null){
                    if(esInstructor){
                        if( Objects.equals(progra.getInstructorId().getPersonaId().getId(), instructorPersona.getId()) ){
                            validacion = true;
                        }
                    }
                    if(progra.getRegistroCursoId() != null && !progra.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_EVA")){
                        validacion = false;
                    }
                    if(validacion){
                        if((new Date()).compareTo(fechaMax) >= 0){  // progra.getFechaHoraFin()
                            validacion = false;
                        }else{
                            validacion = true;
                        }
                    }
                }
            }
        }
        return validacion;
    }
    
    public boolean mostrarBtnVerNotas(SspProgramacion progra){
        boolean validacion = false;
        if(progra != null && progra.getInstructorId() != null){
            if( Objects.equals(progra.getInstructorId().getPersonaId().getId(), instructorPersona.getId()) ){
                validacion = true;
            }
        }
        if(progra.getRegistroCursoId() != null && !progra.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_ACT") && !progra.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_FIN")){
            validacion = false;
        }
        return validacion;
    }
    
    public void openDlgActualizarNotas(SspModulo modulo){
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        String tipoPersona = "1";
        
        if(modulo != null){
            if(instructorPersona != null){
                if(instructorPersona.getTipoDoc().getCodProg().equals("TP_DOCID_CE")){
                    tipoPersona = "2";
                }else{
                    tipoPersona = "1";
                }
            }          
            
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    lstProgramacionConfirma.add(progra);
                }
	    }
            if(!lstProgramacionConfirma.isEmpty()){
                
                if(instructorPersona != null){
                        //////////////// Búsqueda de datos de instructor en Disca ////////////////
                        List<Map> lstDatosInstructores= ejbRmaCursosFacade.buscarInstructorByTipoDocByDocByCursoSinFechaLimiteCaducidad(tipoPersona, instructorPersona.getNumDoc().trim(), modulo.getNombre());
                        if(lstDatosInstructores == null) 
                            lstDatosInstructores = new ArrayList();

                        if(!lstDatosInstructores.isEmpty()){
                            Date fechaCaducidad = (Date) lstDatosInstructores.get(0).get("FECHA_CADUCIDAD");
                            List<SspPrograHora> lstHora = new ArrayList();

                            for(SspProgramacion prog: lstProgramacionConfirma){
                                for(SspPrograHora hora : prog.getSspPrograHoraList()){
                                    if(hora.getActivo() == 0){
                                        continue;
                                    }
                                    lstHora.add(hora);
                                }                
                            }
                            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                                @Override
                                public int compare(SspPrograHora one, SspPrograHora other) {
                                    return other.getFecha().compareTo(one.getFecha());
                                }
                            });
                            Date fechaFinModulo = lstHora.get(0).getFecha();
                            if(JsfUtil.getFechaSinHora(fechaFinModulo).compareTo(JsfUtil.getFechaSinHora(fechaCaducidad)) > 0 ){
                                JsfUtil.mensajeError("No puede actualizar notas porque el curso en su ficha de instrucción no está vigente durante el dictado del curso ");
                                return;
                            }
                        }else{
                            JsfUtil.mensajeError("No tiene el curso vigente para poder actualizar las notas");
                            return;
                        }
                }                        
                
                Collections.sort(lstProgramacionConfirma, new Comparator<SspProgramacion>() {
                    @Override
                    public int compare(SspProgramacion one, SspProgramacion other) {
                        return one.getId().compareTo(other.getId());
                    }
                });
                SspProgramacion progra = lstProgramacionConfirma.get(0);
                lstNotasTemporales = new ArrayList();
                programacionConfirma = progra;
                archivo = progra.getArchivoAsistencia();
                archivoTemporal = progra.getArchivoAsistencia();
                archivoNotas = progra.getArchivoNotas();
                archivoNotasTemporal = progra.getArchivoNotas();

                if(registro.getSspNotasList() == null){
                    registro.setSspNotasList(new ArrayList());
                }
                for(SspNotas nota : registro.getSspNotasList()){
                    if(Objects.equals(nota.getModuloId().getId(), progra.getModuloId().getId())){
                        if(nota.getAlumnoId() == null){
                            continue;
                        }
                        if(nota.getAlumnoId().getTipoOpeId() == null){
                            continue;
                        }
                        if(nota.getAlumnoId().getTipoOpeId().getCodProg().equals("TP_REGIST_NULL")){
                            continue;
                        }
                        
                        SspNotas nuevaNota = new SspNotas();
                        nuevaNota.setActivo(nota.getActivo());
                        nuevaNota.setAlumnoId(nota.getAlumnoId());
                        nuevaNota.setAsistencia(nota.getAsistencia());
                        nuevaNota.setAudLogin(nota.getAudLogin());
                        nuevaNota.setAudNumIp(nota.getAudNumIp());
                        nuevaNota.setEstado(nota.getEstado());
                        nuevaNota.setId(nota.getId());
                        nuevaNota.setModuloId(nota.getModuloId());
                        nuevaNota.setNota(nota.getNota());
                        nuevaNota.setObservacion(nota.getObservacion());
                        nuevaNota.setRegcursoId(nota.getRegcursoId());
                        lstNotasTemporales.add(nuevaNota);
                    }
                }
                if(esInstructor){
                    if(lstNotasTemporales.isEmpty()){
                        for(SspAlumnoCurso alumno : registro.getSspAlumnoCursoList()){
                            if(!validaAlumnoActivo(alumno)){
                                continue;
                            }
                            SspNotas nuevaNota = new SspNotas();
                            nuevaNota.setActivo(JsfUtil.TRUE);
                            nuevaNota.setAlumnoId(alumno);
                            nuevaNota.setAsistencia(JsfUtil.TRUE);
                            nuevaNota.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            nuevaNota.setAudNumIp(JsfUtil.getIpAddress());
                            nuevaNota.setEstado(null);
                            nuevaNota.setId(JsfUtil.tempId());
                            nuevaNota.setModuloId(progra.getModuloId());
                            nuevaNota.setNota(null);
                            nuevaNota.setObservacion(null);
                            nuevaNota.setRegcursoId(registro);
                            lstNotasTemporales.add(nuevaNota);
                        }
                    }
                }                    
                obtenerListadoNotasActualizacion();
                RequestContext.getCurrentInstance().execute("PF('wvDlgActualizarNota').show()");
                RequestContext.getCurrentInstance().update("frmActualizarNota");
            }
        }
    }
    
    public void openDlgVerNotas(SspProgramacion progra){
        if(progra != null){
            lstNotasTemporales = new ArrayList();
            programacionConfirma = progra;
            if(registro.getSspNotasList() == null){
                registro.setSspNotasList(new ArrayList());
            }
            for(SspNotas nota : registro.getSspNotasList()){
                if(nota.getActivo() == 0){
                    continue;
                }
                if(nota.getAlumnoId() == null){
                    continue;
                }
                if(nota.getAlumnoId().getTipoOpeId() == null){
                    continue;
                }
                if(nota.getAlumnoId().getTipoOpeId().getCodProg().equals("TP_REGIST_NULL")){
                    continue;
                }
                if(Objects.equals(nota.getModuloId().getId(), progra.getModuloId().getId())){
                    lstNotasTemporales.add(nota);
                }
            }
            obtenerListadoNotasActualizacion();
            RequestContext.getCurrentInstance().execute("PF('wvDlgVerNota').show()");
            RequestContext.getCurrentInstance().update("frmVerNota");
        }
    }
    
    public List<SspNotas> obtenerListadoNotasActualizacion(){
        if(lstNotasTemporales == null){
            lstNotasTemporales = new ArrayList();
        }
        List<SspNotas> lstNotas = new ArrayList(lstNotasTemporales);
        lstNotasTemporales = new ArrayList();
        if(programacionConfirma != null){
            for(SspNotas nota : lstNotas){
                if(nota.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(nota.getModuloId().getId(), programacionConfirma.getModuloId().getId())){
                    lstNotasTemporales.add(nota);
                }
            }
            Collections.sort(lstNotasTemporales, new Comparator<SspNotas>() {
                @Override
                public int compare(SspNotas one, SspNotas other) {
                    return ( (one.getAlumnoId().getPersonaId().getApePat() != null)?one.getAlumnoId().getPersonaId().getApePat():one.getAlumnoId().getPersonaId().getNombres()) .compareTo( ((other.getAlumnoId().getPersonaId().getApePat() != null)?other.getAlumnoId().getPersonaId().getApePat():other.getAlumnoId().getPersonaId().getNombres()) );
                }
            });
        }
        return lstNotasTemporales;
    }
    
    public String obtenerRucPersona(SbPersonaGt per) {
        if(per == null){
            return null;
        }
        return per.getRuc();
    }
    
    public String obtenerNombresYApellidosPersona(SbPersonaGt per) {
        if(per == null){
            return null;
        }
        return per.getNombreCompleto();
    }
    
    public String obtenerRznSocialPersona(SbPersonaGt per) {
        if(per == null){
            return null;
        }
        return per.getRznSocial();
    }
    
    public void onCellEdit(CellEditEvent event) {
        if(event.getColumn().getHeaderText().equals("NOTA")){
            Object newValue = event.getNewValue();
            if(newValue != null){
                Double nota = Double.parseDouble(newValue.toString());
                if(nota != null){
                    if( nota > 20 ){
                        lstNotasTemporales.get(event.getRowIndex()).setNota(null);
                        lstNotasTemporales.get(event.getRowIndex()).setEstado(null);
                        JsfUtil.mensajeError("La nota ingresada no puede ser mayor a 20");
                        RequestContext.getCurrentInstance().update("dtNotas");
                        return;
                    }else{
                        if(nota >= NOTA_APROBATORIA ){
                            lstNotasTemporales.get(event.getRowIndex()).setEstado(JsfUtil.TRUE);
                        }else{
                            lstNotasTemporales.get(event.getRowIndex()).setEstado(JsfUtil.FALSE);
                        }
                    }   
                }else{
                    lstNotasTemporales.get(event.getRowIndex()).setEstado(null);
                }
            }else{
                JsfUtil.mensajeError("Ingrese una nota");
            }
        }
    }

    public String obtenerDescEstado(Short estado){
        String descEstado = "";
        
        if(estado != null){
            if(estado == 0){
                descEstado = "DESAPROBADO";
            }else{
                descEstado = "APROBADO";
            }
        }
        return descEstado;
    }
    
    public String obtenerDescAsistencia(Short asistencia){
        String descEstado = "";
        
        if(asistencia != null){
            if(asistencia == 0){
                descEstado = "NO ASISTIÓ";
            }else{
                descEstado = "ASISTIÓ";
            }
        }
        return descEstado;
    }
    
    public void actualizarNotasInstructor(){
        boolean validacion = true;
        int cont = 0;
        
        try {
            if(esInstructor){
                for(SspNotas notaAlumno : lstNotasTemporales){
                    if(notaAlumno.getActivo() == 1 && notaAlumno.getNota() == null && Objects.equals(notaAlumno.getModuloId().getId(), programacionConfirma.getModuloId().getId()) ){
                        validacion = false;
                        JsfUtil.mensajeError("Por favor, ingresar todas las notas de los alumnos.");
                        break;
                    }
                    if(notaAlumno.getActivo() == 1 && notaAlumno.getNota() != null && Objects.equals(notaAlumno.getModuloId().getId(), programacionConfirma.getModuloId().getId()) ){
                        if(notaAlumno.getNota() < 0 || notaAlumno.getNota() > 20){
                            validacion = false;
                            JsfUtil.mensajeError("Por favor, ingresar las notas en el rango correcto");
                        }
                    }
                }
            }                
            if(archivo == null || archivo.isEmpty()){
                validacion = false;
                JsfUtil.mensajeError("Por favor, adjuntar la asistencia y firma de los participantes en un archivo PDF");
            }
            if(archivoNotas == null || archivoNotas.isEmpty()){
                validacion = false;
                JsfUtil.mensajeError("Por favor, adjuntar las notas del módulo en un archivo PDF");
            }
            if(validacion){
                if(esInstructor){
                    for(SspNotas notas : lstNotasTemporales){
                        cont = 0;
                        for(SspNotas notasAct : registro.getSspNotasList()){
                            if(Objects.equals(notasAct.getId(), notas.getId())){
                                cont++;
                                notasAct.setAsistencia(notas.getAsistencia());
                                notasAct.setEstado(notas.getEstado());
                                notasAct.setNota(notas.getNota());
                                notasAct.setObservacion(notas.getObservacion());
                            }
                        }
                        if(cont == 0){
                            notas = (SspNotas) JsfUtil.entidadMayusculas(notas, "");
                            notas.setId(null);                        
                        }
                        if(notas.getId() == null){
                            int contNota = ejbSspNotasFacade.contNotasAlumnoByRegistroIdByModulo(notas.getRegcursoId().getId(), notas.getModuloId().getId(), notas.getAlumnoId().getId());
                            if(contNota > 0){
                                JsfUtil.mensajeError("El alumno ya tiene una nota registrada. Por favor volver a buscar las notas del alumno");
                                return;
                            }
                        }
                        if(cont == 0){
                            registro.getSspNotasList().add(notas);
                        }
                    }
                }                    
                if(programacionConfirma.getEstadoVerifica() == null){
                    programacionConfirma.setEstadoVerifica(JsfUtil.FALSE);
                }
                if(!Objects.equals(archivoTemporal, archivo)){
                    String fileName = file.getFileName();
                    String nombreAdj = "ASISTENCIA_"+programacionConfirma.getRegistroCursoId().getId()+"_"+programacionConfirma.getId()+ fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                    programacionConfirma.setArchivoAsistencia(nombreAdj.toUpperCase());
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor() +  programacionConfirma.getArchivoAsistencia() ), fotoByte );
                }
                if(!Objects.equals(archivoNotasTemporal, archivoNotas)){
                    String fileName = fileNotas.getFileName();
                    String nombreAdj = "NOTAS_"+programacionConfirma.getRegistroCursoId().getId()+"_"+programacionConfirma.getId()+ fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                    programacionConfirma.setArchivoNotas(nombreAdj.toUpperCase());
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor() +  programacionConfirma.getArchivoNotas() ), notasByte );
                }
                if(esInstructor){
                    ejbSspRegistroCursoFacade.edit(registro);
                }
                ejbSspProgramacionFacade.edit(programacionConfirma);
                registro = ejbSspRegistroCursoFacade.find(registro.getId());
                file = null;
                fileNotas = null;
                archivo = null;
                archivoNotas = null;
                
                if(esInstructor){
                    verificarTodasConfirmaciones();
                }
                RequestContext.getCurrentInstance().execute("PF('wvDlgActualizarNota').hide()");
                RequestContext.getCurrentInstance().update("actualizarForm");
            }
        } catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al actualizar las notas");
        }
    }
    
    public void verificarTodasConfirmaciones(){
        boolean flagCambiarEstado = true;
        int cont;
        
        if(registro.getEstadoId().getCodProg().equals("TP_ECC_ACT")){
            return;
        }        
         /////// VERIFICANDO TODAS LAS NOTAS DE TODOS LOS MÓDULOS ///////
        for(SspProgramacion progra : registro.getSspProgramacionList()){                
            cont = 0;
            if(progra.getActivo() == 1 && !progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP") ){
                for(SspNotas notaAlumno : registro.getSspNotasList()){
                    if(notaAlumno.getActivo() == 1 && notaAlumno.getNota() != null && Objects.equals(notaAlumno.getModuloId().getId(),progra.getModuloId().getId()  )){
                        cont++;
                    }
                }
                if(cont == 0){
                    //Le falta registrar todas las notas para cambiarle de estado
                    flagCambiarEstado = false;
                    return;
                }
            }
        }
        if(flagCambiarEstado){
            registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_ACT"));
            registro.setHashQrConst(JsfUtil.crearHash(registro.getNroExpediente() + " - CONSTANCIA_" + registro.getId()));
            adicionaCursoEvento(registro.getEstadoId(), "");
            
            boolean aprobado;
            for(SspAlumnoCurso alumnoCurso : registro.getSspAlumnoCursoList()){
                
                if(validaAlumnoActivo(alumnoCurso)){
                    aprobado = true;                
                    for(SspNotas nota : alumnoCurso.getSspNotasList()){
                        if(nota.getActivo() == 1 && Objects.equals(nota.getRegcursoId().getId(), registro.getId() )){
                            if(nota.getNota().intValue() < NOTA_APROBATORIA){
                                aprobado = false;
                            }
                        }
                    }
                }else{
                    aprobado = false;
                }               
                alumnoCurso.setNotaFinal(null);
                if(aprobado){
                    alumnoCurso.setEstadoFinal(JsfUtil.TRUE);
                }else{
                    alumnoCurso.setEstadoFinal(JsfUtil.FALSE);
                }
                alumnoCurso.setAsistencia(JsfUtil.TRUE);
            }
            ejbSspRegistroCursoFacade.edit(registro);
            
            String loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(registro.getNroExpediente());
            String asunto = JsfUtil.bundleBDIntegrado("sspCursos_msjeDerivacion") + registro.getNroExpediente();
            List<Integer> acciones = new ArrayList();
            acciones.add(21);   // Procesado
            boolean estadoTraza = wsTramDocController.asignarExpediente(registro.getNroExpediente(), loginUsuarioAct, loginUsuarioAct, asunto, "", acciones,false,null);
            if (!estadoTraza) {
                JsfUtil.mensajeError("No se pudo generar la traza del expediente " + registro.getNroExpediente());
            }
        }
        ////////////////////////////////////////////////////////////////
    }    
    ///////////////////////////////////////////////////////////////
    
    
    public String prepareListView(){
        if(esInstructor){
            return prepareBandejaInstructor();
        }else{
            return prepareList();
        }
    }
    
    public String obtenerColorConfirmacionTutoria(SspModulo modulo){
        String color = "";
        int cont = 0;
        
        if(modulo != null){
            for(SspInstructorModulo instModulo : registro.getSspInstructorModuloList()){
                if(Objects.equals(instModulo.getInstructorId().getPersonaId().getId(), instructorPersona.getId()) &&
                   Objects.equals(instModulo.getRegcursoId().getId(), registro.getId()) &&
                   Objects.equals(instModulo.getModuloId().getId(), modulo.getId()) &&
                   instModulo.getActivo() == 1
                   ){
                    cont ++;
                }
            }
            if(cont > 0){
                color = "datatableCursos-row-cursoPermitidoDictado";
            }
        }
        
        return color;
    }
    
    public String obtenerColorBtnActualizar(SspModulo modulo){
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        if(modulo != null && registro != null){
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    lstProgramacionConfirma.add(progra);
                }
            }
            if(!lstProgramacionConfirma.isEmpty()){
                int contTotal = 0;
                if(esInstructor){
                    Collections.sort(lstProgramacionConfirma, new Comparator<SspProgramacion>() {
                        @Override
                        public int compare(SspProgramacion one, SspProgramacion other) {
                            return one.getId().compareTo(other.getId());
                        }
                    });
                    SspProgramacion progra = lstProgramacionConfirma.get(0);
                    int contAlumnos = 0 ;
                    for(SspNotas notaC : registro.getSspNotasList() ){
                        if(Objects.equals(notaC.getModuloId().getId(), progra.getModuloId().getId()) && Objects.equals(notaC.getRegcursoId().getId(), progra.getRegistroCursoId().getId()) ){
                            if(notaC.getActivo() == 1){
                                contTotal++;
                            }
                        }
                    }
                    for(SspAlumnoCurso alum : registro.getSspAlumnoCursoList()){
                        if(alum.getActivo() == 1){
                            contAlumnos++;
                        }
                    }
                    if(contTotal == contAlumnos){
                        return "botonActivar";
                    }
                }else{
                    // Es administrado
                    String estilo = null;
                    contTotal = 0;
                    for(SspProgramacion progra : lstProgramacionConfirma ){
                        if(progra.getActivo() == 0){
                            continue;
                        }
                        if(progra.getArchivoAsistencia() != null && progra.getArchivoNotas() != null){
                            contTotal++;                            
                        }
                    }
                    if(contTotal == 0){
                        estilo = "botonFaltaDoc";
                    }
                    return estilo;
                }
            }   
        }
        return null;
    }
    
    public boolean mostrarColumnConfirmoBoton(SspProgramacion progra){
        boolean validacion = false;
        if(registro != null && !esInstructor ){
            if(progra.getInstructorId() != null){
                validacion = true;
            }
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                validacion = false;
            }
        }
        return validacion;
    }
    
    public boolean mostrarColumnConfirmoTexto(SspProgramacion progra){
        boolean validacion = false;
        if(registro != null && !esInstructor){
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                return true;
            }
            if(progra.getInstructorId() == null){
                validacion = true;
            }
        }
        return validacion;
    }
    
    public boolean mostrarColumnConfirmo(){
        boolean validacion = false;
        if(registro != null && !esInstructor){
            validacion = true;
        }
        return validacion;
    }
    
    public boolean mostrarColumnTieneNota(){
        boolean validacion = false;
        if(registro != null){
            if(registro.getEstadoId().getCodProg().equals("TP_ECC_EVA") && !esInstructor ){
                validacion = true;
            }
        }
        return validacion;
    }
    
    public boolean mostrarColumnTieneNotaDetalle(SspProgramacion progra){
        boolean validacion = false;
        if(registro != null){
            if(registro.getEstadoId().getCodProg().equals("TP_ECC_EVA") && !esInstructor ){
                validacion = true;
            }
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                validacion = false;
            }
        }
        return validacion;
    }

    public boolean mostrarColumnNotasCabecera(){
        boolean validacion = false;
        if(registro != null){
           if(registro.getEstadoId().getCodProg().equals("TP_ECC_ACT") || registro.getEstadoId().getCodProg().equals("TP_ECC_FIN")){
                validacion = true;
           }
        }
        return validacion;
    }
    
    public boolean mostrarColumnAsistencia(){
        boolean validacion = false;
        if(registro != null){
            if((registro.getEstadoId().getCodProg().equals("TP_ECC_ACT") || registro.getEstadoId().getCodProg().equals("TP_ECC_FIN")) && esInstructor ){
                validacion = true;
            }
        }
        return validacion;
    }
    
    public boolean mostrarColumnAsistenciaDetalle(SspProgramacion progra){
        boolean validacion = false;
        if(registro != null){
            if((registro.getEstadoId().getCodProg().equals("TP_ECC_ACT") || registro.getEstadoId().getCodProg().equals("TP_ECC_FIN")) && esInstructor ){
                validacion = true;
            }
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                validacion = false;
            }
        }
        return validacion;
    }
    
    public boolean mostrarColumnNotas(SspProgramacion progra){
        boolean validacion = false;
        if(registro != null && progra != null){
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                return false;
            }
            if(registro.getEstadoId().getCodProg().equals("TP_ECC_ACT") || registro.getEstadoId().getCodProg().equals("TP_ECC_FIN")){
                if(esInstructor){
                    if(progra.getInstructorId() != null){
                        if( Objects.equals(progra.getInstructorId().getPersonaId().getId(), instructorPersona.getId()) ){
                            validacion = true;
                        }
                    }else{
                        validacion = false;
                    }
                }else{
                    validacion = true;
                }
            }
        }
        return validacion;
    }
    
    public String obtenerDescConfirmo(SspProgramacion progra){
        String desc = "";
        if(progra != null){
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                desc = "-";
                return desc;
            }
            if(progra.getInstructorId() != null){
                desc = "SI";
            }else{
                desc = "NO";
            }
        }
        return desc;
    }
    
    public String obtenerDescTieneNota(SspProgramacion progra){
        String desc = "";
        int cont = 0;
        if(progra != null){
            for(SspNotas notas : registro.getSspNotasList()){
                if(Objects.equals(progra.getRegistroCursoId().getId(), notas.getRegcursoId().getId()) &&
                   Objects.equals(progra.getModuloId().getId(), notas.getModuloId().getId())
                  ) {
                    cont++;
                }
            }
            if(cont > 0){
                desc = "SI";
            }else{
                desc = "NO";
            }
        }
        return desc;
    }
    
    public void validarNuevoLocal(SspProgramacion progra){
        limpiarLocal();
        setDisabledLocal(false);
        setDisabledLocalEdicion(false);
        
        if(progra != null){
            ubigeoLocal = progra.getLocalId().getDistritoId();
        }
    }
    
    public boolean renderBtnModificatoria(SspRegistroCurso reg){
        boolean validacion = false;
        List<SspPrograHora> lstHora = new ArrayList();
        
        if(reg != null){
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_CRE") ||
               reg.getEstadoId().getCodProg().equals("TP_ECC_CAN") ||
               reg.getEstadoId().getCodProg().equals("TP_ECC_NPR") ||
               reg.getEstadoId().getCodProg().equals("TP_ECC_NDR") ||
               reg.getEstadoId().getCodProg().equals("TP_ECC_ACT") ||
               reg.getEstadoId().getCodProg().equals("TP_ECC_FDP") ||
               reg.getEstadoId().getCodProg().equals("TP_ECC_DES") ||
               reg.getEstadoId().getCodProg().equals("TP_ECC_FIN")){
                return false;
            }
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_EVA")){
                return true;
            }
            if(reg.getSspProgramacionList() != null){
                for(SspProgramacion prog : reg.getSspProgramacionList()){
                    if(prog.getActivo() == 1){
                        if(prog.getModuloId().getCodModulo().equals("REFB") || prog.getModuloId().getCodModulo().equals("REFP")){
                            continue;
                        }
                        lstHora = new ArrayList();
                        for(SspPrograHora hora : prog.getSspPrograHoraList()){
                            if(hora.getActivo() == 1){
                                lstHora.add(hora);
                            }
                        }
                        Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                            @Override
                            public int compare(SspPrograHora one, SspPrograHora other) {
                                return one.getFecha().compareTo(other.getFecha());
                            }
                        });
                        
                        if(!lstHora.isEmpty()){
                            int diasCalculo = JsfUtil.calculaDiasEntreFechas(JsfUtil.getFechaSinHora(new Date()), JsfUtil.getFechaSinHora(lstHora.get(0).getFecha() ));
                            if(diasCalculo >= DIAS_CAMBIO_LOCAL){
                                validacion = true;
                                return validacion;
                            }
                        }
                    }
                }
            }
        }
        return validacion; 
    }
    
    public boolean renderBtnCancelarCursoModificatoria(){
        if(registro != null && 
           !registro.getEstadoId().getCodProg().equals("TP_ECC_CRE") &&
           !registro.getEstadoId().getCodProg().equals("TP_ECC_OBS")
          ){
            return true;
        }
        return false;
    }
    
    public boolean renderBtnImprimir(SspRegistroCurso reg){
        boolean validacion = false;
        if(reg != null && (reg.getEstadoId().getCodProg().equals("TP_ECC_EVA") || reg.getEstadoId().getCodProg().equals("TP_ECC_ACT") || reg.getEstadoId().getCodProg().equals("TP_ECC_FIN"))){
            validacion = true;
        }
        return validacion; 
    }
    
    public boolean renderBtnListadoAsistencia(SspRegistroCurso reg){
        boolean validacion = false;
        if(reg != null && reg.getEstadoId().getCodProg().equals("TP_ECC_EVA") ){
            validacion = true;
        }
        return validacion; 
    }
    
    public boolean renderBtnTerminoActividad(SspRegistroCurso reg){
        boolean validacion = false;
        if(reg != null && (reg.getEstadoId().getCodProg().equals("TP_ECC_EVA") || reg.getEstadoId().getCodProg().equals("TP_ECC_FIN")) ){
            validacion = true;
        }
        return validacion; 
    }
    
    public void mostrarModificatoria(SspRegistroCurso reg){
        String excepcionModulos = JsfUtil.bundleBDIntegrado("sspCursos_excepcionCursos_multiplesLocales");	
        reiniciarValores();
        registro = reg;
        revisarSiEsVirtual(registro);
        validarFechaCorte(reg);
        lstProgramacion = registro.getSspProgramacionList();
        for(SspProgramacion progra : reg.getSspProgramacionList()){
            if(progra.getActivo() == 1 && !excepcionModulos.contains(progra.getModuloId().getCodModulo()) ){
                DIAS_PRESENTACION = ((progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 7 || progra.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId() == 15)?(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoLima").getValor())):(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoProvincia").getValor())));
		break;
            }
        }        
        for(SspProgramacion progra : reg.getSspProgramacionList()){
            if(progra.getActivo() == 0){
                continue;
            }
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                continue;
            }
            if(!lstLocales.contains(progra.getLocalId()) ){
                lstLocales.add(progra.getLocalId());
            }
        }
        for(SspProgramacion prog : reg.getSspProgramacionList()){
            if(prog.getActivo() == 1 && !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")){
                tipoFormacion = prog.getModuloId().getTipoCursoId();
                
                //tipoServicio.setNombre("LLEGO");
                break;
            }
        }
        esEvento = false;
        if(tipoFormacion != null && (tipoFormacion.getCodProg().equals("TP_FORMC_BASEVNT") || tipoFormacion.getCodProg().equals("TP_FORMC_PRFEVNT"))){
            esEvento = true;
        }
        /////////////// VIGILANTE ///////////////
        lstAlumnosVigilantes = reg.getSspAlumnoCursoList();
        /////////////////////////////////////////
        tipoServicio=reg.getTipoServicio();
        cargarModulosNoRepetidos();
        cargarHistorialProgramacion();
        calcularFechaMinima();
        estado = EstadoCrud.RECTIFICAR;
    }
    
    public void cargarModulosNoRepetidos(){
        if(lstProgramacion != null){
            Collections.sort(lstProgramacion, new Comparator<SspProgramacion>() {
                @Override
                public int compare(SspProgramacion one, SspProgramacion other) {
                    return one.getId().compareTo(other.getId());
                }
            });
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 1 && !lstModulosNoRep.contains(progra.getModuloId()) && !progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP")){
                    lstModulosNoRep.add(progra.getModuloId());
                }
            }            
        }
    }
    
    public void cargarHistorialProgramacion(){
        if(registro != null){
            for(SspProgramacion progra : registro.getSspProgramacionList()){
                if(progra.getActivo() == 1){
                    for(SspPrograHistorial hist : progra.getSspPrograHistorialList()){
                        if(hist.getActivo() == 1){
                            lstProgramacionHistorial.add(hist);
                        }
                    }
                }
            }
        }
    }
    
    public String obtenerTextoBtnModificatoriaCurso(SspModulo modulo){
        String texto = null;
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        for(SspProgramacion progra : lstProgramacion){
            if(progra.getActivo() == 0){
                continue;
            }
            if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                lstProgramacionConfirma.add(progra);
            }
        }
        if(!lstProgramacionConfirma.isEmpty()){
            if(lstProgramacionConfirma.get(0).getInstructorId() == null){
                texto = "+";
            }
        }
        return texto;
    }
    
    public void openDlgModificarLocal(SspProgramacion progra){
        validarNuevoLocal(progra);
        if(progra.getSspPrograHistorialList() == null){
            progra.setSspPrograHistorialList(new ArrayList());
        }
        motivoLocal = "";
        programacionConfirma = progra;
        setFlagBtnCambio(true);
        RequestContext.getCurrentInstance().execute("PF('wvDlgCambiarLocal').show()");
        RequestContext.getCurrentInstance().update("frmCambiarLocal");
    }
    
    public void openDlgModificarInstructor(SspModulo modulo){
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        limpiarInstructores();        
        for(SspProgramacion progra : lstProgramacion){
            if(progra.getActivo() == 0){
                continue;
            }
            if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                lstProgramacionConfirma.add(progra);
            }
        }
        if(!lstProgramacionConfirma.isEmpty()){
            Collections.sort(lstProgramacionConfirma, new Comparator<SspProgramacion>() {
                @Override
                public int compare(SspProgramacion one, SspProgramacion other) {
                    return one.getId().compareTo(other.getId());
                }
            });
            programacionConfirma = lstProgramacionConfirma.get(0);
        }
        if(programacionConfirma.getSspPrograHistorialList() == null){
            programacionConfirma.setSspPrograHistorialList(new ArrayList());
        }
//        if(programacionConfirma.getInstructorId() == null){
//            JsfUtil.mensajeAdvertencia("Este curso aún está pendiente de confirmación por algún instructor");
//            return;
//        }
        motivoLocal = "";
        cursoDlg=modulo.getNombre();
        tipoFormacion = programacionConfirma.getModuloId().getTipoCursoId();
        lstModulos = ejbSspModuloFacade.listarModuloByTipoCurso(tipoFormacion.getId());
        lstInstructoresTabla = new ArrayList();
        lstModulosFiltrados = new ArrayList();
        lstCursosOpt = new ArrayList();
        setDisabledTipoFormacion(true);
        setFlagBtnCambio(false);
        RequestContext.getCurrentInstance().execute("PF('wvDlgCambiarInstructor').show()");
        RequestContext.getCurrentInstance().update("frmCambiarInstructor");
    }
    
    public void confirmarCambioLocalCurso(){
        boolean validacion = true;
        
        if(nombreLocal == null || nombreLocal.isEmpty()){
            validacion = false;
            //JsfUtil.invalidar("frmCambiarLocal:nombreLocal");
            JsfUtil.invalidar("frmCambiarLocal:nombrePlataforma");
            JsfUtil.mensajeError("Por favor, ingrese el nombre de la Plataforma Virtual"); //Cambio temporal por Curso Virtual
        }
        if(caracteristicas == null || caracteristicas.isEmpty()){
            validacion = false;
            JsfUtil.invalidar("frmCambiarLocal:caracteristicas");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_descripcion"));
        }
        if(referenciaLocal == null || referenciaLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar("frmCambiarLocal:referenciaLocal");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_responsable"));
        }
        if(telefonoLocal == null || telefonoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar("frmCambiarLocal:telefono");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_telefono"));
        }
        if(correoLocal == null || correoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar("frmCambiarLocal:correoElectronico");
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursosRequiredMessage_correo"));
        }
        if(ubigeoLocal == null){
            validacion = false;
            JsfUtil.invalidar("frmCambiarLocal:ubigeo");
            JsfUtil.mensajeError("Por favor seleccione la ubicación del local");
        }
        revisarSiEsVirtual(registro);
        if (esVirtual){
            asignarDatoVirtual();
        }
        if(tipoUbicacionLocal == null){
            validacion = false;
            JsfUtil.invalidar("frmCambiarLocal:tipoUbicacion");
            JsfUtil.mensajeError("Por favor seleccione un tipo de ubicación");
        }
        if(direccionLocal == null || direccionLocal.isEmpty()){
            //validacion = false;   //Cambio temporal por Curso Virtual
            //JsfUtil.invalidar("frmCambiarLocal:direccionLocal");
            //JsfUtil.mensajeError("Por favor ingrese la dirección");
        }
        if(latitudLocal == null || longitudLocal == null){
            //Validación temporal por Curso Virtual
            //validacion = false;
            //JsfUtil.invalidar("frmCambiarLocal:btnMapa");
            //JsfUtil.mensajeError("Por favor seleccionar la ubicación en el mapa.");
        }
        if(aforoLocal == null || aforoLocal.isEmpty()){
            validacion = false;
            JsfUtil.invalidar("frmCambiarLocal:aforoTotal");
            JsfUtil.mensajeError("Por favor ingresar el aforo.");
        }
        if(validacion){
//            int contLocales = ejbSspLocalFacade.contarLocalesByNombre(nombreLocal.trim(), (localSelected != null? localSelected.getId():null));
//            if(contLocales > 0){
//                validacion = false;
//                JsfUtil.mensajeError("Ya se ha registrado un local con el mismo nombre, por favor utilice el botón de búsqueda de local");
//            }
//
//            contLocales = ejbSspLocalFacade.contarLocalesByDireccion(nombreLocal.trim(), (localSelected != null? localSelected.getId():null));
//            if(contLocales > 0){
//                validacion = false;
//                JsfUtil.mensajeError("Ya se ha registrado un local con la misma dirección, por favor utilice el botón de búsqueda de local");
//            }
            if(programacionConfirma != null){
                if(!esVirtual && !Objects.equals(ubigeoLocal.getProvinciaId().getDepartamentoId().getId(), programacionConfirma.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getId())){
                    validacion = false;
                    JsfUtil.mensajeError("No puede cambiar el local cuya ubicación se encuentra en un departamento distinto al seleccionado ("+programacionConfirma.getLocalId().getDistritoId().getProvinciaId().getDepartamentoId().getNombre() +")");
                }
            }
        }
        if(validacion){
            RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarCambioLocal').show()");
            RequestContext.getCurrentInstance().update("frmConfirmarCambioLocal");
        }
    }
    
    public void cambiarLocalCurso(){
        try {
            if(motivoLocal == null || motivoLocal.isEmpty()){
                JsfUtil.mensajeError("Por favor ingrese el motivo");
                return;
            }
            SspPrograHistorial nuevoHistorial = new SspPrograHistorial();
            nuevoHistorial.setActivo(JsfUtil.TRUE);
            nuevoHistorial.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            nuevoHistorial.setAudNumIp(JsfUtil.getIpAddress());
            nuevoHistorial.setFecha(new Date());
            nuevoHistorial.setFechaHoraFin(null);
            nuevoHistorial.setFechaHoraIncio(null);
            nuevoHistorial.setHistorialId(null);
            nuevoHistorial.setId(null);
            nuevoHistorial.setInstructorId(null);
            nuevoHistorial.setLocalId(programacionConfirma.getLocalId());
            nuevoHistorial.setMotivo(motivoLocal);
            nuevoHistorial.setProgramacionId(programacionConfirma);
            nuevoHistorial = (SspPrograHistorial) JsfUtil.entidadMayusculas(nuevoHistorial, "");

            for(SspPrograHistorial histo : programacionConfirma.getSspPrograHistorialList()){
                if(Objects.equals(histo.getProgramacionId().getModuloId().getId(), nuevoHistorial.getProgramacionId().getModuloId().getId() ) && histo.getLocalId() != null && histo.getId() == null ){
                    programacionConfirma.getSspPrograHistorialList().remove(histo);
                    break;
                }
            }
            programacionConfirma.getSspPrograHistorialList().add(nuevoHistorial);
            preparaCambioLocal();

            for(SspProgramacion progra : registro.getSspProgramacionList()){
                if (esVirtual){
                    progra.setLocalId(localSelected);
                }else{   
                    if( progra.getActivo() == 1 &&  Objects.equals(progra.getId(), programacionConfirma.getId())){
                        progra.setLocalId(localSelected);
                        break;
                    }
                }
            }
            cambioLocal = true;
            RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarCambioLocal').hide()");
            RequestContext.getCurrentInstance().execute("PF('wvDlgCambiarLocal').hide()");
            RequestContext.getCurrentInstance().update(obtenerForm());
        } catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al procesar los datos");
        }
    }
    
    public void preparaCambioLocal(){
        if(!disabledLocal){
            localSelected = new SspLocal();
            localSelected.setId(JsfUtil.tempId());
            localSelected.setActivo(JsfUtil.TRUE);
            localSelected.setFechaReg(new Date());
            localSelected.setDireccion(direccionLocal.trim());
            localSelected.setDistritoId(ubigeoLocal);
            localSelected.setCaracteristicas(caracteristicas);
            if(latitudLocal != null && !latitudLocal.isEmpty()){
                localSelected.setGeoLat(latitudLocal);
            }
            if(longitudLocal != null && !longitudLocal.isEmpty()){
                localSelected.setGeoLong(longitudLocal);
            }
            localSelected.setNombreLocal(nombreLocal.trim());
            localSelected.setTipoUbicacionId(tipoUbicacionLocal);
            localSelected.setReferencia(referenciaLocal.trim());
            if(aforoLocal != null && !aforoLocal.isEmpty()){
                localSelected.setAforo(Long.parseLong(aforoLocal));
            }
            localSelected.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            localSelected.setAudNumIp(JsfUtil.getIpAddress());
            localSelected.setSspLocalContactoList(new ArrayList());
            if(telefonoLocal != null && !telefonoLocal.isEmpty()){
                SspLocalContacto contacto = new SspLocalContacto();
                contacto.setId(null);
                contacto.setLocalId(localSelected);
                contacto.setTipoMedioId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MEDIOC_TEL"));
                contacto.setValor(telefonoLocal.trim());
                contacto.setDescripcion(telefonoLocal.trim());
                contacto.setActivo(JsfUtil.TRUE);
                contacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                contacto.setAudNumIp(JsfUtil.getIpAddress());
                contacto = (SspLocalContacto) JsfUtil.entidadMayusculas(contacto, "");
                localSelected.getSspLocalContactoList().add(contacto);
            }
            if(correoLocal != null && !correoLocal.isEmpty()){
                SspLocalContacto contacto = new SspLocalContacto();
                contacto.setId(null);
                contacto.setLocalId(localSelected);
                contacto.setTipoMedioId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MEDIOC_COR"));
                contacto.setValor(correoLocal.trim());
                contacto.setDescripcion(correoLocal.trim());
                contacto.setActivo(JsfUtil.TRUE);
                contacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                contacto.setAudNumIp(JsfUtil.getIpAddress());
                contacto = (SspLocalContacto) JsfUtil.entidadMayusculas(contacto, "");
                localSelected.getSspLocalContactoList().add(contacto);
            }
            localSelected = (SspLocal) JsfUtil.entidadMayusculas(localSelected, "");
            localSelected.setCaracteristicas(caracteristicas.trim());
        }else{
            localSelected.setDireccion(direccionLocal);                
            localSelected.setDistritoId(ubigeoLocal);
            localSelected.setCaracteristicas(caracteristicas);
            if(aforoLocal != null && !aforoLocal.isEmpty()){
                localSelected.setAforo(Long.parseLong(aforoLocal));
            }
            if(latitudLocal != null && !latitudLocal.isEmpty()){
                localSelected.setGeoLat(latitudLocal);
            }
            if(longitudLocal != null && !longitudLocal.isEmpty()){
                localSelected.setGeoLong(longitudLocal);
            }
            boolean encontro = false;
            if(telefonoLocal != null && !telefonoLocal.isEmpty()){
                for(SspLocalContacto cont : localSelected.getSspLocalContactoList()){
                    if(cont.getActivo() == 1 && cont.getTipoMedioId().getCodProg().equals("TP_MEDIOC_TEL") && cont.getValor().trim().equals(telefonoLocal.trim()) ){
                        encontro = true;
                    }
                }

                if(!encontro){
                    // Se da de baja la actual
                    for(SspLocalContacto cont : localSelected.getSspLocalContactoList()){
                        if(cont.getActivo() == 1 && cont.getTipoMedioId().getCodProg().equals("TP_MEDIOC_TEL")){
                            cont.setActivo(JsfUtil.FALSE);    
                        }
                    }
                    // Se crea al nuevo contacto
                    SspLocalContacto contacto = new SspLocalContacto();
                    contacto.setId(null);
                    contacto.setLocalId(localSelected);
                    contacto.setTipoMedioId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MEDIOC_TEL"));
                    contacto.setValor(telefonoLocal.trim());
                    contacto.setDescripcion(telefonoLocal.trim());
                    contacto.setActivo(JsfUtil.TRUE);
                    contacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    contacto.setAudNumIp(JsfUtil.getIpAddress());
                    contacto = (SspLocalContacto) JsfUtil.entidadMayusculas(contacto, "");
                    localSelected.getSspLocalContactoList().add(contacto);
                }
            }else{
                for(SspLocalContacto cont : localSelected.getSspLocalContactoList()){
                    if(cont.getActivo() == 1 && cont.getTipoMedioId().getCodProg().equals("TP_MEDIOC_TEL")){
                        cont.setActivo(JsfUtil.FALSE);    
                    }
                }
            }
            encontro = false;
            if(correoLocal != null && !correoLocal.isEmpty()){
                for(SspLocalContacto cont : localSelected.getSspLocalContactoList()){
                    if(cont.getActivo() == 1 && cont.getTipoMedioId().getCodProg().equals("TP_MEDIOC_COR") && cont.getValor().trim().equals(correoLocal.trim()) ){
                        encontro = true;
                    }
                }
                if(!encontro){
                    // Se da de baja la actual
                    for(SspLocalContacto cont : localSelected.getSspLocalContactoList()){
                        if(cont.getActivo() == 1 && cont.getTipoMedioId().getCodProg().equals("TP_MEDIOC_COR")){
                            cont.setActivo(JsfUtil.FALSE);    
                        }
                    }
                    // Se crea al nuevo contacto
                    SspLocalContacto contacto = new SspLocalContacto();
                    contacto.setId(null);
                    contacto.setLocalId(localSelected);
                    contacto.setTipoMedioId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MEDIOC_COR"));
                    contacto.setValor(correoLocal.trim());
                    contacto.setDescripcion(correoLocal.trim());
                    contacto.setActivo(JsfUtil.TRUE);
                    contacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    contacto.setAudNumIp(JsfUtil.getIpAddress());
                    contacto = (SspLocalContacto) JsfUtil.entidadMayusculas(contacto, "");
                    localSelected.getSspLocalContactoList().add(contacto);
                }
            }else{
                for(SspLocalContacto cont : localSelected.getSspLocalContactoList()){
                    if(cont.getActivo() == 1 && cont.getTipoMedioId().getCodProg().equals("TP_MEDIOC_COR")){
                        cont.setActivo(JsfUtil.FALSE);    
                    }
                }
            }
        }
    }
    
    public String guardarModificatoria(){
        Long idTemp = 0L;
        if(!validaHorasAcademicas()){
            return null;
        }
        try {
            boolean cambioAlumnos = false;
            idTemp = registro.getId();
            SspRegistroCurso cRegistroOriginal = ejbSspRegistroCursoFacade.find(registro.getId());
            
            if(validacionGuardarModificatoria(cRegistroOriginal)){
                /////////////////////////////////////////
                ////////// Validación de local //////////
                /////////////////////////////////////////
                List<Map> emailsInst = new ArrayList();            
                for(SspProgramacion progra : registro.getSspProgramacionList()){
                    if( progra.getActivo() == 1 ){

                        for(SspPrograHistorial his : progra.getSspPrograHistorialList()){
                            if(his.getActivo() == 1 && his.getId() == null && his.getLocalId() != null){
                                if(his.getProgramacionId().getInstructorId() != null){

                                    for(SbMedioContactoGt medio : his.getProgramacionId().getInstructorId().getPersonaId().getSbMedioContactoList()){
                                        if(medio.getActivo() == 1 && medio.getTipoId().getCodProg().equals("TP_MEDCO_COR")){
                                            Map nmap = new HashMap();
                                            nmap.put("correo", medio.getValor());
                                            nmap.put("curso", progra.getModuloId().getNombre() );
                                            nmap.put("local", progra.getLocalId().getNombreLocal());
                                            nmap.put("direccion", progra.getLocalId().getDireccion());
                                            nmap.put("motivo", his.getMotivo());
                                            emailsInst.add(nmap);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if(progra.getLocalId().getId() < 0){
                            progra.getLocalId().setId(null);
                            ejbSspLocalFacade.create(progra.getLocalId());
                        }else{
                            ejbSspLocalFacade.edit(progra.getLocalId());
                        }
                    }
                }

                /////////////// Envío de emails a instructores ///////////////
                if(!emailsInst.isEmpty()){
                    for(Map item : emailsInst){
                        String msje = JsfUtil.bundleBDIntegrado("sspCursos_notificacion_mensaje") + item.get("curso") + " " +
                                      JsfUtil.bundleBDIntegrado("sspCursos_notificacion_mensaje2")+ item.get("local") + 
                                      JsfUtil.bundleBDIntegrado("sspCursos_notificacion_mensaje3")+ item.get("direccion") +
                                      JsfUtil.bundleBDIntegrado("sspCursos_notificacion_mensaje4")+ " " + item.get("motivo") + ".";
                        if(item.get("correo") != null){
                            JsfUtil.enviarCorreo( item.get("correo").toString(), JsfUtil.bundleBDIntegrado("sspCursos_notificacion_asunto"), msje);
                        }
                    }
                }
                //////////////////////////////////////////////////////////////


                //////////////////////////////////////////////
                ////////// Validación de Instructor //////////
                //////////////////////////////////////////////
                for(SspInstructorModulo instMod : registro.getSspInstructorModuloList()){
                    if(instMod.getId() == null){
                        if(instMod.getInstructorId().getPersonaId().getId() == null){
                            if(instMod.getInstructorId().getPersonaId().getFechaNac() != null){
                                int anio = JsfUtil.formatoFechaYyyy(instMod.getInstructorId().getPersonaId().getFechaNac());
                                if(anio < 1900 || anio > JsfUtil.formatoFechaYyyy(new Date()) ){
                                    instMod.getInstructorId().getPersonaId().setFechaNac(null);
                                }
                            }
                            
                            ejbSbPersonaFacade.create(instMod.getInstructorId().getPersonaId());
                        }
                        if(instMod.getInstructorId().getEmpresaId() != null && instMod.getInstructorId().getEmpresaId().getId() == null){
                            if(instMod.getInstructorId().getEmpresaId().getFechaNac() != null){
                                int anio = JsfUtil.formatoFechaYyyy(instMod.getInstructorId().getEmpresaId().getFechaNac());
                                if(anio < 1900 || anio > JsfUtil.formatoFechaYyyy(new Date()) ){
                                    instMod.getInstructorId().getEmpresaId().setFechaNac(null);
                                }
                            }
                            ejbSbPersonaFacade.create(instMod.getInstructorId().getEmpresaId());
                        }
                        if(instMod.getInstructorId().getId() == null){
                            ejbSspInstructorFacade.create(instMod.getInstructorId());
                        }
                    }
                }
                
                
                /////////////////////////////////////////////
                ///////////////// VIGILANTES ////////////////
                /////////////////////////////////////////////
                int cont = 0;
                for(SspAlumnoCurso alumnoOri : cRegistroOriginal.getSspAlumnoCursoList()){
                    cont = 0;
                    for(SspAlumnoCurso alumno : registro.getSspAlumnoCursoList()){
                        if(Objects.equals(alumnoOri.getId(), alumno.getId())){
                            cont++;
                        }
                    }
                    if(cont == 0){
                        alumnoOri.setActivo(JsfUtil.FALSE);
                        registro.getSspAlumnoCursoList().add(alumnoOri);
                    }
                }
                if(cRegistroOriginal.getSspAlumnoCursoList().size() != registro.getSspAlumnoCursoList().size()){
                    cambioAlumnos = true;
                }
                //////// FOTOS ///////
                String fileName = "";                            
                for(SspAlumnoCurso alumno : lstAlumnosVigilantes){
                    if(alumno.getFotoId() != null && alumno.getId() < 0){
                        for(Map nmap : lstFotosAlumnos){
                            if(Objects.equals((Long) nmap.get("id"), alumno.getId())){
                                fileName = nmap.get("nombreFoto").toString();
                                fileName = "GSSP_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_FOTC").toString()+fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                                fileName = fileName.toUpperCase();
                                alumno.getFotoId().setNombreFoto(fileName);
                                FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor() + fileName ), (byte[]) nmap.get("fotoByte") );
                            }
                        }
                    }
                    if(alumno.getId() == null){
                        alumno.setId(JsfUtil.tempId());
                    }
                }
                JsfUtil.borrarIds(lstAlumnosVigilantes);
                registro.setSspAlumnoCursoList(lstAlumnosVigilantes);
                
                for(SspAlumnoCurso alumno : registro.getSspAlumnoCursoList()){
                    if(alumno.getPersonaId().getId() == null){
                        ejbSbPersonaFacade.create(alumno.getPersonaId());
                    }else{
                        ejbSbPersonaFacade.edit(alumno.getPersonaId());
                    }
                }
                //////////////////////////////////////////////
                
                //////////////////////////////////////////
                ejbSspRegistroCursoFacade.edit(registro);
                JsfUtil.mensaje("El registro se actualizó correctamente");
                //////////////////////////////////////////
                
                
                ///////////// ENVÍO DE CORREO AL EVALUADOR //////////////
                String usuarioActual = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(registro.getNroExpediente());
                SbUsuarioGt usuarioDev = ejbSbUsuarioFacade.buscarUsuarioByLogin(usuarioActual);
                if(usuarioDev != null && usuarioDev.getCorreo() != null && !usuarioDev.getCorreo().isEmpty()){
                    String msjeCorreo = JsfUtil.bundleBDIntegrado("sspCursos_notificacion_mensajeModificatoria") + registro.getNroExpediente()+ 
                                        JsfUtil.bundleBDIntegrado("sspCursos_notificacion_mensajeModificatoria2");
                    if(cambioHorario){
                        msjeCorreo += "- Cambio de horario. \n";
                    }
                    if(cambioLocal){
                        if (esVirtual){
                            msjeCorreo += "- Cambio de plataforma virtual. \n";    
                        }else{
                            msjeCorreo += "- Cambio de local. \n";
                        }
                    }
                    if(cambioInstructor){
                        msjeCorreo += "- Cambio de instructor. \n";
                    }
                    if(cambioAlumnos){
                        msjeCorreo += "- Cambio de alumnos. \n";
                    }                    
                    JsfUtil.enviarCorreo( usuarioDev.getCorreo(), JsfUtil.bundleBDIntegrado("sspCursos_notificacion_asuntoModificatoria"), msjeCorreo);
                }
                /////////////////////////////////////////////////////////                
                
                ////////////////////// NUEVA TRAZA //////////////////////
                List<Integer> acciones = new ArrayList();
                acciones.add(21);   // Procesado
                boolean estadoExp = wsTramDocController.asignarExpediente(registro.getNroExpediente(), usuarioActual, usuarioActual, JsfUtil.bundleBDIntegrado("sspCursos_msjeModificatoria"), "", acciones,false,null);
                if (!estadoExp) {
                    JsfUtil.mensajeError("No se pudo derivar el expediente creado "+ registro.getNroExpediente());
                }      
                /////////////////////////////////////////////////////////
                
                return prepareList();
            }
        } catch (Exception e) {
            System.err.println("ID Curso: " + idTemp);
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al guardar el registro.");
        }
        return null;
    }
    
    public boolean validacionGuardarModificatoria(SspRegistroCurso cRegistroOriginal){
        boolean validacion = true, flagCambioRegistro = false;
        long sumaHoras = 0;
        Date fechaIni, fechaFin;
        
        if(!Objects.equals(registro.getEstadoId().getId(), cRegistroOriginal.getEstadoId().getId())){
            flagCambioRegistro = true;
        }
        if(flagCambioRegistro){
            if(registro.getEstadoId().getCodProg().equals("TP_ECC_TRA")){
                JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursos_mensajeCambioEstadoTransmitido"));
            }else{
                JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("sspCursos_mensajeCambioEstadoOtroEstado"));
            }
            return false;
        }
            
        
        // Validacion de horas académicas //
        if(lstProgramacionHorario != null && !lstProgramacionHorario.isEmpty()){
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 1){
                    for(SspPrograHora hora : progra.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            fechaIni = hora.getHoraInicio();
                            fechaFin = hora.getHoraFin();
                            if(lstProgramacionHorario.contains(hora)){
                                for(SspPrograHora cap : lstProgramacionHorario){
                                    if(Objects.equals(cap.getId(), hora.getId())){
                                        fechaIni = cap.getHoraInicio();
                                        fechaFin = cap.getHoraFin();
                                        break;
                                    }
                                }
                            }
                            long minutos = JsfUtil.calculaMinutosEntreHoras(fechaIni, fechaFin);
                            sumaHoras += minutos;
                        }
                    }        
                }
            }
            TipoSeguridad tipForm = null;
            for(SspPrograHora cap : lstProgramacionHorario){
                if(cap.getActivo() == 1){
                    long minutos = JsfUtil.calculaMinutosEntreHoras(cap.getHoraInicio(), cap.getHoraFin());
                    sumaHoras += minutos;
                }
                if(!cap.getProgramacionId().getModuloId().getCodModulo().equals("REFB") && !cap.getProgramacionId().getModuloId().getCodModulo().equals("REFP")){
                    tipForm = cap.getProgramacionId().getModuloId().getTipoCursoId();
                }
            }
            int horasAcademicas = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horasAcademicas").getValor());
            int horasTotales = (int) Math.ceil((double) sumaHoras/horasAcademicas);
            int horasBasica = 0, horasPerfecc = 0;
            
            switch(tipForm.getCodProg()){
                case "TP_FORMC_BAS":
                        horasBasica = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horasFormBasica").getValor());
                        break;
                case "TP_FORMC_BASEVNT":
                        horasBasica = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horasFormBasicaEvento").getValor());
                        break;
                case "TP_FORMC_PRF":
                        horasPerfecc = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horasPerfecciona").getValor()); 
                        break;
                case "TP_FORMC_PRFEVNT":
                        horasPerfecc = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horasFormPerfeccEvento").getValor()); 
                        break;
            }
            if(tipForm.getCodProg().equals("TP_FORMC_BAS") || tipForm.getCodProg().equals("TP_FORMC_BASEVNT")){                
                if(horasTotales < horasBasica){
                    JsfUtil.mensajeError("No ha cumplido con las "+ horasBasica +" horas académicas del curso" );
                    validacion = false;
                }
            }else{               
               if(horasTotales < horasPerfecc){
                    JsfUtil.mensajeError("No ha cumplido con las "+ horasPerfecc +" horas académicas del curso");
                    validacion = false;
                }
            }
        }
        
        ///////////// Validación de alumnos ////////////
        if(lstAlumnosVigilantes == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingrese al menos un prospecto de vigilante");
        }
        if(lstAlumnosVigilantes.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingrese al menos un prospecto de vigilante");
        }
        ////////////////////////////////////////////////
        return validacion;
    }    
    
    public List<SspPrograHistorial> getLstProgramacionHistorialDesc() {
        if(lstProgramacionHistorial != null){
            Collections.sort(lstProgramacionHistorial, new Comparator<SspPrograHistorial>() {
                @Override
                public int compare(SspPrograHistorial one, SspPrograHistorial other) {
                    return other.getFecha().compareTo(one.getFecha());
                }
            });
        }
        return lstProgramacionHistorial;
    }
    
    public void confirmarCambioInstructorCurso(){
        boolean validacion = true;
        if(tipoDoc == null){
            validacion = false;
            JsfUtil.invalidar("frmCambiarInstructor:tipoDocIdentidad");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de documento");
        }
        if(numDoc == null || numDoc.isEmpty()){
            validacion = false;
            JsfUtil.invalidar("frmCambiarInstructor:nroDocIdentidad");
            JsfUtil.mensajeError("Por favor ingresar el número de documento");
        }else{
            numDoc = numDoc.trim();
        }
        if(lstCargaInstructores.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor busque el instructor a cambiar");
        }
        if(validacion){
            if(programacionConfirma.getInstructorId() != null && Objects.equals(programacionConfirma.getInstructorId().getPersonaId().getNumDoc(), numDoc) ){
                JsfUtil.mensajeError("El instructor a cambiar es el mismo del curso actual confirmado.");
                return;
            }
            RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarCambioInstructor').show()");
            RequestContext.getCurrentInstance().update("frmConfirmarCambioInstructor");
        }
    }
    
    public void buscarInstructorModificatoria(){
        boolean validacion = true;
        int cont = 0;
        if(tipoDoc == null){
            validacion = false;
            JsfUtil.invalidar("frmCambiarInstructor:tipoDocIdentidad");
            JsfUtil.mensajeError("Por favor seleccionar el tipo de documento");
        }
        if(numDoc == null || numDoc.isEmpty()){
            validacion = false;
            JsfUtil.invalidar("frmCambiarInstructor:nroDocIdentidad");
            JsfUtil.mensajeError("Por favor ingresar el número de documento");
        }else{
            numDoc = numDoc.trim();
        }
        
        if(validacion){
            if(programacionConfirma.getInstructorId() != null && Objects.equals(programacionConfirma.getInstructorId().getPersonaId().getNumDoc(), numDoc) ){
                JsfUtil.mensajeError("El instructor a cambiar es el mismo del curso actual confirmado.");
                return;
            }
            lstCargaInstructores= ejbRmaCursosFacade.buscarInstructorByTipoDocByDoc(tipoDoc, numDoc); // Busqueda en RMA
            if(lstCargaInstructores == null)
                lstCargaInstructores = new ArrayList();
            if(!lstCargaInstructores.isEmpty()){
                int contPerfil = ejbSbUsuarioFacade.buscarPerfilInstructorUsuario(""+lstCargaInstructores.get(0).get("NUM_DOC"));
                if(contPerfil == 0){
                    flagBtnCambio = false;
                    JsfUtil.mensajeError("El instructor ingresado no tiene cuenta y/o perfil de instructor en el sistema SEL");
                    return;
                }
                
                for(Map mapInst : lstCargaInstructores){
                    if(programacionConfirma.getModuloId().getNombre().trim().equals(mapInst.get("NOM_CURSO").toString()) ){
                        cont++;
                    }
                }
                if(cont == 0){
                    flagBtnCambio = false;
                    JsfUtil.mensajeError("El instructor ingresado no está autorizado a llevar el mismo curso");
                    return;
                }
                nombresInstructor = lstCargaInstructores.get(0).get("NOMBRES") + " " + lstCargaInstructores.get(0).get("APELLIDOS");
                flagBtnCambio = true;
                JsfUtil.mensaje("Se encontró al instructor en el sistema");
            }else{
                flagBtnCambio = false;
                JsfUtil.mensajeError("No se encontró al instructor");
            }
        }
    }
    
    public void cambiarInstructorCurso(){
        try {
            if(motivoLocal == null || motivoLocal.isEmpty()){
                JsfUtil.mensajeError("Por favor ingrese el motivo");
                return;
            }
            SspInstructor instTemp = null;
            if(programacionConfirma.getInstructorId() != null){
                instTemp = ejbSspInstructorFacade.find(programacionConfirma.getInstructorId().getId());
            }
            for(SspProgramacion progra : registro.getSspProgramacionList()){
                if(Objects.equals(progra.getModuloId().getId(), programacionConfirma.getModuloId().getId())){
                    SspPrograHistorial nuevoHistorial = new SspPrograHistorial();
                    nuevoHistorial.setActivo(JsfUtil.TRUE);
                    nuevoHistorial.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    nuevoHistorial.setAudNumIp(JsfUtil.getIpAddress());
                    nuevoHistorial.setFecha(new Date());
                    nuevoHistorial.setFechaHoraFin(null);
                    nuevoHistorial.setFechaHoraIncio(null);
                    nuevoHistorial.setHistorialId(null);
                    nuevoHistorial.setId(null);
                    nuevoHistorial.setInstructorId(instTemp);
                    nuevoHistorial.setLocalId(null);
                    nuevoHistorial.setMotivo(motivoLocal);
                    nuevoHistorial.setProgramacionId(progra);
                    nuevoHistorial = (SspPrograHistorial) JsfUtil.entidadMayusculas(nuevoHistorial, "");
                    
                    for(SspPrograHistorial histo : progra.getSspPrograHistorialList()){
                        if(Objects.equals(histo.getProgramacionId().getModuloId().getId(), nuevoHistorial.getProgramacionId().getModuloId().getId() ) && histo.getInstructorId() != null && histo.getId() == null ){
                            progra.getSspPrograHistorialList().remove(histo);
                            break;
                        }
                    }
                    progra.getSspPrograHistorialList().add(nuevoHistorial);
                    preparaCambioInstructor();
                    progra.setInstructorId(null);
                }
            }
            cambioInstructor = true;
            RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarCambioInstructor').hide()");
            RequestContext.getCurrentInstance().execute("PF('wvDlgCambiarInstructor').hide()");
            RequestContext.getCurrentInstance().update(obtenerForm());
        } catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al procesar los datos");
        }
    }
    
    public void preparaCambioInstructor(){
        SspInstructor instructor = ejbSspInstructorFacade.buscarInstructorByNumDoc(lstCargaInstructores.get(0).get("NUM_DOC").toString().trim());
        SbPersonaGt per = null;
        
        if(instructor == null){
            instructor = new SspInstructor();
            
            //////////// PersonaID ////////////
            per = ejbSbPersonaFacade.buscarPersonaSel("", ""+lstCargaInstructores.get(0).get("NUM_DOC"));
            if(per != null){
                // Persona ya existe
            }else{
                per = new SbPersonaGt();
                per.setActivo(JsfUtil.TRUE);
                per.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                per.setAudNumIp(JsfUtil.getIpAddress());
                per.setId(null);
                per.setNumDoc(""+lstCargaInstructores.get(0).get("NUM_DOC"));
                per.setNombres(""+lstCargaInstructores.get(0).get("NOMBRES"));
                per.setApePat(""+lstCargaInstructores.get(0).get("APE_PAT"));
                per.setApeMat(""+lstCargaInstructores.get(0).get("APE_MAT"));
                per.setFechaNac((Date) lstCargaInstructores.get(0).get("FECHA_NAC"));
                per.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));

                if(lstCargaInstructores.get(0).get("TIP_USR").toString().equals("5")){
                    per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_CE"));
                }else{
                    per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                }
                if(lstCargaInstructores.get(0).get("SEXO") != null && lstCargaInstructores.get(0).get("SEXO").toString().equals("M")){
                    per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GEN_MAS"));
                }else{
                    if(lstCargaInstructores.get(0).get("SEXO") != null && lstCargaInstructores.get(0).get("SEXO").toString().equals("F")){
                        per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GEN_FEM"));
                    }
                }
                per.setRuc(null);
                per.setRznSocial(null);
                per.setSbMedioContactoList(new ArrayList());
                if(lstCargaInstructores.get(0).get("TELEFONO") != null){
                    SbMedioContactoGt medioTelefono = new SbMedioContactoGt();
                    medioTelefono.setActivo(JsfUtil.TRUE);
                    medioTelefono.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    medioTelefono.setAudNumIp(JsfUtil.getIpAddress());
                    medioTelefono.setId(null);
                    medioTelefono.setPersonaId(per);
                    medioTelefono.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_MOV"));
                    medioTelefono.setValor(lstCargaInstructores.get(0).get("TELEFONO").toString());
                    medioTelefono.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                    per.getSbMedioContactoList().add(medioTelefono);
                }
                if(lstCargaInstructores.get(0).get("EMAIL") != null){
                    SbMedioContactoGt medioCorreo = new SbMedioContactoGt();
                    medioCorreo.setActivo(JsfUtil.TRUE);
                    medioCorreo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    medioCorreo.setAudNumIp(JsfUtil.getIpAddress());
                    medioCorreo.setId(null);
                    medioCorreo.setPersonaId(per);
                    medioCorreo.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_COR"));
                    medioCorreo.setValor(lstCargaInstructores.get(0).get("EMAIL").toString());
                    medioCorreo.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                    per.getSbMedioContactoList().add(medioCorreo);
                }
                per = (SbPersonaGt) JsfUtil.entidadMayusculas(per, "");
            }
            instructor.setPersonaId(per);
            instructor.setActivo(JsfUtil.TRUE);
            instructor.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            instructor.setAudNumIp(JsfUtil.getIpAddress());
            instructor.setEstadoInstruId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_EIN_VIG"));
            instructor.setId(null);
            instructor.setSspInstructorModuloList(new ArrayList());
            
            //////////// Empresa ////////////
            per = null;
            if(lstCargaInstructores.get(0).get("RUC") != null && !lstCargaInstructores.get(0).get("RUC").toString().equals("0")){
                per = ejbSbPersonaFacade.buscarPersonaSel("", ""+lstCargaInstructores.get(0).get("RUC"));
                if(per == null){
                    per = new SbPersonaGt();
                    per.setActivo(JsfUtil.TRUE);
                    per.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    per.setAudNumIp(JsfUtil.getIpAddress());
                    per.setId(null);
                    per.setNumDoc(null);
                    per.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_JUR"));
                    per.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_RUC"));                 
                    per.setRuc(""+lstCargaInstructores.get(0).get("RUC"));
                    per.setRznSocial(""+lstCargaInstructores.get(0).get("RZN_SOC_EMPRESA"));
                    per = (SbPersonaGt) JsfUtil.entidadMayusculas(per, "");
                }
                instructor.setEmpresaId(per);
            }   
            /////////////////////////////////////////////
        }
        BigDecimal bd = (BigDecimal) lstCargaInstructores.get(0).get("NRO_FICHA");
        instructor.setNroFicha( bd.longValue());
        instructor.setFechaEmicion((Date)lstCargaInstructores.get(0).get("FECHA_EMISION"));
        instructor.setFechaVencimiento((Date)lstCargaInstructores.get(0).get("FECHA_CADUCIDAD"));
        instructor = (SspInstructor) JsfUtil.entidadMayusculas(instructor, "");        
        
        boolean insertaInstructor = true;
        if(registro.getSspInstructorModuloList() != null){
            for(SspInstructorModulo instMod : registro.getSspInstructorModuloList()){
                if(instMod.getActivo() == 0){
                    continue;
                }
                if( Objects.equals(instMod.getModuloId().getId(), programacionConfirma.getModuloId().getId()) &&
                    Objects.equals(instMod.getInstructorId().getPersonaId().getNumDoc(), instructor.getPersonaId().getNumDoc()) 
                   ){
                    insertaInstructor = false;
                }
            }
        }
        
        if(insertaInstructor){
            SspInstructorModulo nuevoInstModulo = new SspInstructorModulo();
            nuevoInstModulo.setActivo(JsfUtil.TRUE);
            nuevoInstModulo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            nuevoInstModulo.setAudNumIp(JsfUtil.getIpAddress());
            nuevoInstModulo.setFecha(new Date());
            nuevoInstModulo.setId(null);
            nuevoInstModulo.setInstructorId(instructor);
            nuevoInstModulo.setModuloId(programacionConfirma.getModuloId());
            nuevoInstModulo.setRegcursoId(registro);
            registro.getSspInstructorModuloList().add(nuevoInstModulo);
        }
    }
    
    public String obtenerNombreInstructorModificatoria(SspModulo modulo){
        String nombre = "Pendiente de confirmación por el instructor";
        if(modulo != null){
            for(SspProgramacion progra : lstProgramacion){
                if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                    if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                        return "-";
                    }
                    if(progra.getInstructorId() != null){
                        nombre = progra.getInstructorId().getPersonaId().getNombreCompleto();    
                    }
                }
            }
        }
        return nombre;
    }
    
    public void openDlgMediosContacto(Map reg){
        telefonoContacto = ""+(reg.get("TELEFONO") != null ?reg.get("TELEFONO").toString():"");
        correoContacto = ""+(reg.get("EMAIL") != null ?reg.get("EMAIL").toString():"");
        instructorContacto = ""+(reg.get("NOMBRES_APELLIDOS") != null ?reg.get("NOMBRES_APELLIDOS").toString():"");
        
        RequestContext.getCurrentInstance().execute("PF('wvDlgMediosContacto').show()");
        RequestContext.getCurrentInstance().update("frmMediosContacto");
    }
    
    public boolean renderBtnMedioContactoInstructor(String header){
        boolean validacion = false;
        if(header != null && header.contains("CONTACTO")){
            validacion = true;
        }
        return validacion;
    }
    
    public String obtenerDescEstadoFinalCurso(SspAlumnoCurso alumno){
        String estadoCurso = "";
        if(alumno != null && alumno.getEstadoFinal() != null){
            if(alumno.getEstadoFinal() == 1){
                estadoCurso = "APROBADO";
            }else{
                estadoCurso = "DESAPROBADO";
            }
        }
        return estadoCurso;
    }
    
    public String obtenerColorEstadoCurso(SspAlumnoCurso alumno){
        String colorCurso = "";
        
        if(alumno != null && alumno.getEstadoFinal() != null){
            if(alumno.getEstadoFinal() == 1){
                colorCurso = "CursoAprobado";
            }else{
                colorCurso = "CursoDesaprobado";
            }
        }
        
        return colorCurso;
    }
    
    public boolean mostrarTextAprobado(SspAlumnoCurso alumno){
        boolean validacion = false;
        
        if(alumno != null){
            if(alumno.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_FIN")){
                validacion = true;
            }
        }
        
        return validacion;
    }
    
    public String getFeriadosStrList() {
        List<SbFeriado> feriadosList = ejbSbFeriadoFacade.listaFeriadosCalendario();

        String s = "[";
        for (SbFeriado feriado : feriadosList) {
            s = s
                    + "\"" + JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 2)
                    + "-" + JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 3)
                    + "-" + JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 1) + "\",";
        }
        s = s.substring(0, s.length() - 1);
        s += "]";
        return s;
    }
    
    public void cambiaAsistencia(SspNotas nota){
        if(nota.getAsistencia() == 0){
            nota.setNota((double)0);
            nota.setEstado(JsfUtil.FALSE);
        }else{
            if(nota.getNota() != null){
                if(nota.getNota() >= NOTA_APROBATORIA){
                    if(nota.getNota() > 20){
                        nota.setEstado(null);        
                    }else{
                        nota.setEstado(JsfUtil.TRUE);
                    }
                }else{
                    nota.setEstado(JsfUtil.FALSE);
                }
            }else{
                nota.setEstado(null);
            }
        }
    }
    public String obtenercolorNota(Integer nota){
        String color = "";
        if(nota == null){
            color = "color: red;";
        }else{ 
            if(nota >= NOTA_APROBATORIA){
                color = "color:#107EBE;"; 
            }else{
                color = "color: red;";
            }
        }
        return color;
    }
    
    public String obtenerDescAsistenciaActualizacion(SspNotas nota){
        String asistencia = "ASISTIÓ";
        if(nota.getAsistencia() == 0){
            asistencia = "FALTÓ";
        }
        return asistencia;
    }
    
    public String obtenerColorAsistencia(SspNotas nota){
        String asistencia = "color:#107EBE;";
        if(nota.getAsistencia() == 0){
            asistencia = "color:red;";
        }
        return asistencia;
    }
    
    /**
     * FUNCION PARA GENERAR CONSTANCIA DE CURSOS
     * @author Richar Fernández
     * @version 1.0
     * @param reg
     * @return Reporte pdf
     */
    public StreamedContent imprimirConstancia(SspRegistroCurso reg){
        try {
            registro = reg;
            List<SspRegistroCurso> lp = new ArrayList();
            lp.add(registro);

            String nombreArch = ""+registro.getId();
            if(reg.getEstadoId().getCodProg().equals("TP_ECC_FIN")){
                if (JsfUtil.buscarPdfRepositorio(""+registro.getId(), ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor())) {
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor(), nombreArch + ".pdf", nombreArch + ".pdf", "application/pdf");
                }else{
                    JsfUtil.subirPdfSel(""+registro.getId(), parametrosPdf(registro), lp, "/aplicacion/gssp/sspRegistroCurso/reportes/constanciaCurso.jasper", ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor());
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor(), nombreArch + ".pdf", nombreArch + ".pdf", "application/pdf");
                }
            }else{
                if(esVirtual){
                    return JsfUtil.generarReportePdf("/aplicacion/gssp/sspRegistroCurso/reportes/constanciaCursoVirtual.jasper", lp, parametrosPdf(registro), nombreArch, "", "");
                }else{
                    return JsfUtil.generarReportePdf("/aplicacion/gssp/sspRegistroCurso/reportes/constanciaCurso.jasper", lp, parametrosPdf(registro), nombreArch, "", "");
                }  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
     /**
     * PARAMETROS PARA GENERAR PDF
     * @author Richar Fernández
     * @version 1.0
     * @param ppdf Registro de cursos
     * @return Listado de parámetros para constancia
     */
    private HashMap parametrosPdf(SspRegistroCurso ppdf){
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        
        preparaDatosCursos(ppdf);
        
        ph.put("P_FECHASTRING", ReportUtil.mostrarFechaString(ppdf.getFechaRegistro()));
        ph.put("P_LOGOSUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        ph.put("P_MARCAAGUA", ec.getRealPath("/resources/imagenes/marcaAgua_GSSP.png"));
        ph.put("P_EXPEDIENTE", ppdf.getNroExpediente() );
        ph.put("P_TIPOFORMACION", tipoFormacion.getNombre() );
        ph.put("P_ADMINISTRADO", ppdf.getAdministradoId().getRznSocial() + ", RUC: " + ppdf.getAdministradoId().getRuc() );
        ph.put("P_NUM_ALUMNOS", lstAlumnosVigilantes.size() );
        ph.put("P_LISTA_LOCALES", listarLocalesConstancia() );
        ph.put("P_LISTA_INSTRUCTORES", lstInstructoresTabla );
        ph.put("P_LISTA_PROGRAMACION", listarProgramacionConstancia() );
        ph.put("P_LISTA_ALUMNOS", listarALumnosConstancia() );
        ph.put("P_HASH_QR", ppdf.getHashQr());
        ph.put("P_ESTADO_CODPROG", ppdf.getEstadoId().getCodProg());
        ph.put("P_QR", "https://www.sucamec.gob.pe/sel/faces/qr/constanciaCursos.xhtml?h="+ppdf.getHashQr());
        
        return ph;
    }
    
    public void preparaDatosCursos(SspRegistroCurso cRegistro){
        limpiarDatosImpresion();
                
        if(cRegistro.getSspProgramacionList() != null){            
            for(SspProgramacion prog : cRegistro.getSspProgramacionList()){
                if(prog.getActivo() == 1 && !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")){
                    tipoFormacion = prog.getModuloId().getTipoCursoId();
                    break;
                }
            }
        }
        
        /////////////// LOCALES ///////////////
        for(SspProgramacion progra : cRegistro.getSspProgramacionList()){
            if(progra.getActivo() == 0){
                continue;
            }
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP")){
                continue;
            }
            if(progra.getLocalId() == null){
                continue;
            }
            boolean insertar = true;
            for(SspLocal item : lstLocales){
                if(Objects.equals(item.getNombreLocal(), progra.getLocalId().getNombreLocal()) && Objects.equals(item.getDireccion(), progra.getLocalId().getDireccion()) ){
                    insertar = false;
                }
            }
            if(insertar){
                lstLocales.add(progra.getLocalId());
            }
        }
        //////////////////////////////////////
        
        
        /////////////// INSTRUCTORES ///////////////
        Map nmap;
        List<SspInstructor> lstInstructorTemp = new ArrayList();
        for(SspProgramacion progra : cRegistro.getSspProgramacionList()){
            if(progra.getActivo() == 0){
                continue;
            }
            if(cRegistro.getEstadoId().getCodProg().equals("TP_ECC_CRE") || cRegistro.getNroExpediente() == null){
                for(SspInstructorModulo inst : progra.getModuloId().getSspInstructorModuloList() ){
                    if(inst.getActivo() == 1 && Objects.equals(inst.getRegcursoId().getId(), cRegistro.getId() ) && !lstInstructorTemp.contains(inst.getInstructorId()) ){
                        lstInstructorTemp.add(inst.getInstructorId());
                    }
                }
            }else{
                if(progra.getInstructorId() != null && !lstInstructorTemp.contains(progra.getInstructorId()) ){
                    lstInstructorTemp.add(progra.getInstructorId());
                }
            }
        }
        for(SspInstructor inst : lstInstructorTemp){
            nmap = new HashMap();
            //nmap.put("NUM_DOC", inst.getPersonaId().getNumDoc() );
            nmap.put("APELLIDOS", inst.getPersonaId().getApePat() + " " + inst.getPersonaId().getApeMat() );
            nmap.put("NOMBRES", inst.getPersonaId().getNombres() );
            
            if(!lstInstructoresTabla.contains(nmap)){
                lstInstructoresTabla.add(nmap);
            }
        }
        ///////////////////////////////////////////


        ///////////////// AGENDAR /////////////////
        for(SspProgramacion progra : cRegistro.getSspProgramacionList() ){
            if(progra.getActivo() == 1){
                lstProgramacion.add(progra);
            }
        }
        ///////////////////////////////////////////


        /////////////// VIGILANTE ///////////////
        for(SspAlumnoCurso alumn : cRegistro.getSspAlumnoCursoList() ){
            if(!validaAlumnoActivo(alumn)){
                continue;
            }
            lstAlumnosVigilantes.add(alumn);
        }
        /////////////////////////////////////////
    }
    
    public void limpiarDatosImpresion(){        
        lstLocales = new ArrayList();
        lstInstructoresTabla = new ArrayList();
        lstProgramacion = new ArrayList();
        lstAlumnosVigilantes = new ArrayList();
        lstInstructores = new ArrayList();
        lstFotosAlumnos = new ArrayList();
        lstObservaciones = new ArrayList();
        lstProgramacionHistorial = new ArrayList();
    }
    
    public List<Map> listarLocalesConstancia(){
        List<Map> lista = new ArrayList();
        Map nmap;
        
        for(SspLocal loc  : lstLocales){
            nmap = new HashMap();
            nmap.put("nombre", loc.getNombreLocal() );
            nmap.put("direccion", loc.getDireccion() );
            lista.add(nmap);
        }
        
        return lista;
    }
    
    public List<Map> listarProgramacionConstancia(){
        List<Map> lista = new ArrayList();
        List<SspPrograHora> lstHora = new ArrayList();
        Map nmap;
        
        Collections.sort(lstProgramacion, new Comparator<SspProgramacion>() {
            @Override
            public int compare(SspProgramacion one, SspProgramacion other) {
                return one.getModuloId().getId().compareTo(other.getModuloId().getId());
            }
        });
        
        for(SspProgramacion progra  : lstProgramacion){
            nmap = new HashMap();
            lstHora = new ArrayList();
            for(SspPrograHora hora : progra.getSspPrograHoraList()){
                if(hora.getActivo() == 1){
                    lstHora.add(hora);
                }
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });            
            
            nmap.put("local", progra.getLocalId().getNombreLocal() );
            nmap.put("curso", progra.getModuloId().getNombre() );
            
            if(!lstHora.isEmpty()){
                Date fechaIni = lstHora.get(0).getFecha();
                Date fechaFin = lstHora.get(lstHora.size()-1).getFecha();
                nmap.put("fechaIni", JsfUtil.formatoFechaDdMmYyyy(fechaIni) );  // progra.getFechaHoraIncio()
                nmap.put("fechaFin", JsfUtil.formatoFechaDdMmYyyy(fechaFin) );  // progra.getFechaHoraFin()
            }else{
                nmap.put("fechaIni", "-" );
                nmap.put("fechaFin", "-" );
            }
                
            lista.add(nmap);
        }
        
        return lista;
    }
    
    public List<Map> listarALumnosConstancia(){
        List<Map> lista = new ArrayList();
        Map nmap;
        Collections.sort(lstAlumnosVigilantes, new Comparator<SspAlumnoCurso>() {
            @Override
            public int compare(SspAlumnoCurso one, SspAlumnoCurso other) {
                return ( (one.getPersonaId().getApePat() != null)?one.getPersonaId().getApePat():one.getPersonaId().getNombres()) .compareTo( ((other.getPersonaId().getApePat() != null)?other.getPersonaId().getApePat():other.getPersonaId().getNombres()) );
            }
        });
        for(SspAlumnoCurso alumn : lstAlumnosVigilantes){
            if(!validaAlumnoActivo(alumn)){
                continue;
            }
            nmap = new HashMap();
            nmap.put("apellidos", alumn.getPersonaId().getApePat() + " " + alumn.getPersonaId().getApeMat());
            nmap.put("nombres", alumn.getPersonaId().getNombres());
            nmap.put("tipoDoc", ((alumn.getPersonaId().getTipoDoc() != null)?alumn.getPersonaId().getTipoDoc().getNombre():"DNI") );
            nmap.put("numDoc", alumn.getPersonaId().getNumDoc());
            lista.add(nmap);
        }
        return lista;
    }
    
    public boolean renderHabilitarEdicion(){        
        return !(registro != null && (registro.getEstadoId().getCodProg().equals("TP_ECC_TRA") || registro.getEstadoId().getCodProg().equals("TP_ECC_EVA") ));
    }
    
    public boolean renderHabilitarEdicionCambioLocal(){
        boolean valida = !(registro != null && (registro.getEstadoId().getCodProg().equals("TP_ECC_TRA") || registro.getEstadoId().getCodProg().equals("TP_ECC_EVA") ));
        if(lstProgramacion != null && !lstProgramacion.isEmpty()){
            List<SspPrograHora> lstHora = new ArrayList();
            for(SspProgramacion prog : lstProgramacion){
                if(prog.getActivo() == 1){
                    for(SspPrograHora hora : prog.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            lstHora.add(hora);
                        }
                    }
                }
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });
            int diasCalculo = JsfUtil.calculaDiasEntreFechas(JsfUtil.getFechaSinHora(new Date()), JsfUtil.getFechaSinHora(lstHora.get(0).getFecha()));
            if(diasCalculo < DIAS_CAMBIO_LOCAL){
                valida = false;
            }
        }
        return valida;
    }

    public Date getFechaMaxima(){
        return new Date();
    }
    
    public boolean validarCefeoesDpto(String ruc){
    
        List<Map> lstAutCefoesDpto = ejbCarteraClienteFacade.buscarCefoesDpto(ruc);
        
        if(lstAutCefoesDpto.size()!=0){
            return true;
        }
        
        return false;
    }
    
    public boolean verificarModalidadesCC(String ruc){
        int cont = 0;
        lstModalidades = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_MCO_TEC','TP_MCO_PRO','TP_MCO_TRA','TP_MCO_VIG','TP_MCO_SIS','TP_MCO_CTA'");
        List<Map> lstModalidadResolucion = ejbCarteraClienteFacade.buscarModalidadesPermitidasEmpresa(ruc);

        if(lstModalidadResolucion != null){
            //////// TECNOLOGIA ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("TECNOLOGIA")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidades){
                    if(ts.getCodProg().equals("TP_MCO_TEC")){
                        lstModalidades.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////

            //////// PROTECCION PERSONAL ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("PROTECCION PERSONAL")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidades){
                    if(ts.getCodProg().equals("TP_MCO_PRO")){
                        lstModalidades.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////

            //////// TRANSPORTE DE DINERO ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("TRANSPORTE DE DINERO")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidades){
                    if(ts.getCodProg().equals("TP_MCO_TRA")){
                        lstModalidades.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////

            //////// VIGILANCIA PRIVADA ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("VIGILANCIA PRIVADA")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidades){
                    if(ts.getCodProg().equals("TP_MCO_VIG")){
                        lstModalidades.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////

            //////// SEGURIDAD PERSONAL ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("INDIVIDUAL DE SEGURIDAD PERSONAL")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidades){
                    if(ts.getCodProg().equals("TP_MCO_SIS")){
                        lstModalidades.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////

            //////// EVENTUAL ////////
            cont = 0;
            for(TipoSeguridad ts : lstModalidades){
                if(ts.getCodProg().equals("TP_MCO_PRO") || ts.getCodProg().equals("TP_MCO_VIG")){
                    cont++;
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidades){
                    if(ts.getCodProg().equals("TP_MCO_EVE")){
                        lstModalidades.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////
            
            
            //////// SERVICIOS POR CUENTA PROPIA ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("PROTECCION POR CUENTA PROPIA")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidades){
                    if(ts.getCodProg().equals("TP_MCO_CTA")){
                        lstModalidades.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////
        }
        if(lstModalidades != null && !lstModalidades.isEmpty()){
            return true;
        }
        return false;
    }
    
    public void openDlgFoto(SspAlumnoCurso alumno){
        byte[] fotoByteDlg=null;
        try {
            boolean encontro = false;
            for(Map nmap : lstFotosAlumnos){
                if(Objects.equals((Long) nmap.get("id"), alumno.getId())){
                    fotoByteDlg = (byte[]) nmap.get("fotoByte");
                    encontro = true;
                    break;
                }
            }
            if(!encontro){
                fotoByteDlg = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor() + alumno.getFotoId().getNombreFoto()));    
            }
            if(fotoByteDlg != null){
                InputStream is = new ByteArrayInputStream(fotoByteDlg);
                fotoPopUp =  new DefaultStreamedContent(is, "image/jpeg", alumno.getFotoId().getNombreFoto() );

                RequestContext.getCurrentInstance().execute("PF('wvDialogFoto').show()");
                RequestContext.getCurrentInstance().update("frmFoto");
            }else{
                JsfUtil.mensajeAdvertencia("No se pudo cargar la foto");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeAdvertencia("Hubo un error al cargar la foto");
        }
    }
    
    public boolean renderBtnModificatoriaProgramacionInstructor(SspModulo modulo, int tipo){
        boolean validacion = true;
        List<SspPrograHora> lstHora = new ArrayList();
        List<SspProgramacion> lstProgramacionConfirma = new ArrayList();
        if(modulo == null){
            return validacion;
        }
        for(SspProgramacion progra : lstProgramacion){
            if(progra.getActivo() == 0){
                continue;
            }
            if(Objects.equals(progra.getModuloId().getId(), modulo.getId())){
                lstProgramacionConfirma.add(progra);
            }
        }
        if(!lstProgramacionConfirma.isEmpty()){
            SspProgramacion progra = lstProgramacionConfirma.get(0);
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                if(tipo == 1){
                    return false;
                }
                return true;
            }
            for(SspProgramacion prog : lstProgramacionConfirma){
                if(prog.getActivo() == 1){
                    for(SspPrograHora hora : prog.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            lstHora.add(hora);
                        }
                    }
                }
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });
            
            if(JsfUtil.getFechaSinHora(lstHora.get(0).getFecha()).compareTo(JsfUtil.getFechaSinHora(new Date())) < 0){
                validacion = false;
            }
        }
            
        return validacion;
    }
    
    public boolean renderBtnModificatoriaProgramacionHorario(SspProgramacion progra){
        List<SspPrograHora> lstHora = new ArrayList();
        boolean validacion = true;        
        int contTotal = 0, contHora = 0;
        
        if(progra == null){
            return false;
        }
        if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
            return false;
        }
        for(SspPrograHora hora : progra.getSspPrograHoraList()){
            if(hora.getActivo() == 1){
                contTotal++;
                if(JsfUtil.getFechaSinHora(hora.getFecha()).compareTo(JsfUtil.getFechaSinHora(new Date())) < 0){
                    contHora++;
                }
                lstHora.add(hora);
            }
        }
        Collections.sort(lstHora, new Comparator<SspPrograHora>() {
            @Override
            public int compare(SspPrograHora one, SspPrograHora other) {
                return one.getFecha().compareTo(other.getFecha());
            }
        });
        int diasCalculo = JsfUtil.calculaDiasEntreFechas(JsfUtil.getFechaSinHora(new Date()), JsfUtil.getFechaSinHora(lstHora.get(0).getFecha()));
        if(diasCalculo >= DIAS_CAMBIO_LOCAL){
            validacion = true;
        }else{
            validacion = false;    
        }
        if(contTotal > 0 && (contTotal == contHora)){
            validacion = false;
        }
        
        return validacion;
    }
    
    public boolean renderBtnModificatoriaProgramacionLocal(SspProgramacion progra){
        List<SspPrograHora> lstHora = new ArrayList();
        boolean validacion = false;
        
        if(progra != null){
            if(progra.getModuloId().getCodModulo().equals("REFB") || progra.getModuloId().getCodModulo().equals("REFP") ){
                return false;
            }
            for(SspPrograHora hora : progra.getSspPrograHoraList()){
                if(hora.getActivo() == 1){
                    lstHora.add(hora);
                }
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });
            
            int diasCalculo = JsfUtil.calculaDiasEntreFechas(JsfUtil.getFechaSinHora(new Date()), JsfUtil.getFechaSinHora(lstHora.get(0).getFecha()));
            if(diasCalculo >= DIAS_CAMBIO_LOCAL){
                validacion = true;
            }else{
                validacion = false;    
            }
            /*
            Calendar c_ffin = Calendar.getInstance();
            Date fechaIni = c_ffin.getTime();
            c_ffin.add(Calendar.DATE, DIAS_CAMBIO_LOCAL);
            Date fechaFinCalculo = calcularFechaFinalSinSabadoDomingo(fechaIni, c_ffin );
            
            if( JsfUtil.getFechaSinHora(fechaFinCalculo).compareTo(JsfUtil.getFechaSinHora(JsfUtil.getFechaSinHora(lstHora.get(0).getFecha()))) > 0){
                validacion = false;
            }else{
                validacion = true;
            }*/
        }
        
        return validacion;
    }
    
    public void handleFileUploadAdjuntoPDF(FileUploadEvent event){
        try {
            if (event != null) {
                file = event.getFile();
                if ( JsfUtil.verificarPDF(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    foto = new DefaultStreamedContent(file.getInputstream(), "application/pdf");
                    archivo = file.getFileName();
                    
                    if(programacionConfirma.getArchivoAsistencia() != null){
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
    
    public void handleFileUploadAdjuntoNotasPDF(FileUploadEvent event){
        try {
            if (event != null) {
                fileNotas = event.getFile();
                if ( JsfUtil.verificarPDF(fileNotas)) {
                    notasByte = IOUtils.toByteArray(fileNotas.getInputstream());
                    notaStream = new DefaultStreamedContent(fileNotas.getInputstream(), "application/pdf");
                    archivoNotas = fileNotas.getFileName();
                    
                    if(programacionConfirma.getArchivoNotas() != null){
                        JsfUtil.mensajeAdvertencia("El archivo cargado reemplazará al archivo actual");
                    }
                } else {
                    fileNotas = null;
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
                String nombreArchivo = programacionConfirma.getArchivoAsistencia();
                if(nombreArchivo != null && !nombreArchivo.isEmpty()){
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public StreamedContent cargarDocumentoNotas(){
        try {
            if(fileNotas != null){
                return new DefaultStreamedContent(fileNotas.getInputstream(), "application/pdf", fileNotas.getFileName() );
            }else{
                String nombreArchivo = programacionConfirma.getArchivoNotas();
                if(nombreArchivo != null && !nombreArchivo.isEmpty()){
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public StreamedContent cargarDocumentoVer(SspProgramacion progra){
        try {
            String nombreArchivo = progra.getArchivoAsistencia();
            if(nombreArchivo != null && !nombreArchivo.isEmpty()){
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public StreamedContent cargarDocumentoNotasVer(SspProgramacion progra){
        try {
            String nombreArchivo = progra.getArchivoNotas();
            if(nombreArchivo != null && !nombreArchivo.isEmpty()){
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_cursosGssp").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void openDlgCancelarCurso(){
        if(registro != null){
            if(!validacionCancelacionCurso()){
                return;
            }
            
            registro.setObservacion(null);
            RequestContext.getCurrentInstance().execute("PF('wvConfirmarCancelacion').show()");
            RequestContext.getCurrentInstance().update("frmConfirmarCancelacion");
        }
    }
    
    public boolean validacionCancelacionCurso(){
        boolean validacion = true;
        List<SspPrograHora> lstHora = new ArrayList();
        
        if(registro.getSspProgramacionList() != null){
            for(SspProgramacion prog : registro.getSspProgramacionList()){
                if(prog.getActivo() == 0){
                    continue;
                }
                if(prog.getModuloId().getCodModulo().equals("REFB") || prog.getModuloId().getCodModulo().equals("REFP")){
                    continue;
                }
                for(SspPrograHora hora : prog.getSspPrograHoraList()){
                    if(hora.getActivo() == 1){
                        lstHora.add(hora);
                    }
                }
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });
            
            Calendar c_hoy = Calendar.getInstance();
            Integer dias_param = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("SspRegistroCurso_diasCancelacion").getValor());
            Date fechaProgramacion = lstHora.get(0).getFecha();
            Calendar c_fecha = Calendar.getInstance();
            c_fecha.setTime(fechaProgramacion);
            c_fecha.add(Calendar.DATE, (dias_param * -1) );

            int feriados = -1;
            int domingos = -1;
            Date fechaIniTemp = fechaProgramacion;
            while( (feriados + domingos) != 0){
                feriados = ejbSbFeriadoFacade.feriadosCalendarioSinSabado(JsfUtil.getFechaSinHora(c_fecha.getTime()), JsfUtil.getFechaSinHora(fechaIniTemp));
                domingos = JsfUtil.calcularNroDiasSabadoYDomingo(JsfUtil.getFechaSinHora(c_fecha.getTime()), JsfUtil.getFechaSinHora(fechaIniTemp));
                if((feriados + domingos) > 0){
                    fechaIniTemp = c_fecha.getTime();
                    c_fecha.add(Calendar.DATE, ((feriados + domingos)*-1));
                }else{
                    c_hoy.setTime(c_fecha.getTime());
                    int diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
                    if(diaSemana == 1 || diaSemana == 7){
                        fechaIniTemp = c_fecha.getTime();
                        c_fecha.add(Calendar.DATE, -1);
                    }
                }
            }                                              

            if( JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(c_fecha.getTime())) > 0){
                validacion = false;
                JsfUtil.mensajeError("No puede cancelar el curso porque ha pasado los "+ dias_param + " días hábiles previos al inicio del curso ");
            }
        }
        
        return validacion;
    }
    
    public String obtenerMensajeCancelacion(){
        if(registro == null){
            return null;
        }
        return JsfUtil.bundleBDIntegrado("sspCursos_msjeCancelacion1") + JsfUtil.formatoFechaDdMmYyyy(registro.getFechaRegistro()) + " con expediente " + registro.getNroExpediente() ;
    }
    
    public String cancelacionCurso(){
        if(registro != null){
            if(registro.getObservacion() != null && !registro.getObservacion().isEmpty()){
                registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CAN"));
                adicionaCursoEvento(registro.getEstadoId(), ((registro.getObservacion().length() > 200)?registro.getObservacion().substring(0,200):registro.getObservacion()) );
                ejbSspRegistroCursoFacade.edit(registro);
                RequestContext.getCurrentInstance().execute("PF('wvConfirmarCancelacion').hide()");
                JsfUtil.mensaje("El curso fue cancelado correctamente");
                
                ////// NOTIFICAR A QUIEN TIENE EL EXPEDIENTE //////
               notificarCancelacionCurso();
                ///////////////////////////////////////////////////
                
                return prepareList();
            }else{
                JsfUtil.mensajeError("Por favor ingrese el motivo de cancelación del curso");
            }
        }
        return null;
    }
    
    public void notificarCancelacionCurso(){
        List<Integer> acciones = new ArrayList();
        String msje = "";
        acciones.add(21);   //PROCESADO
        String loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(registro.getNroExpediente());
        SbUsuarioGt usuario = ejbSbUsuarioFacade.buscarUsuarioByLogin(loginUsuarioAct);
        boolean estadoTraza = wsTramDocController.asignarExpediente(registro.getNroExpediente(), loginUsuarioAct, loginUsuarioAct, "CANCELADO por el administrado", "", acciones, false, null);        
        if(estadoTraza && usuario != null && usuario.getCorreo() != null){
            msje = "Estimado(a)\nSe le comunica que el administrado "+ registro.getAdministradoId().getRznSocial() +" ha CANCELADO el curso de formación para personal de seguridad privada con expediente "+ registro.getNroExpediente();
            JsfUtil.enviarCorreo( usuario.getCorreo(), JsfUtil.bundleBDIntegrado("sspCursos_notificacionCorreo_asunto"), msje);
        }
    }
    
    /**
     * FUNCION PARA GENERAR CONSTANCIA DE CURSOS
     * @author Richar Fernández
     * @version 1.0
     * @param reg
     * @return Reporte pdf
     */
    public StreamedContent imprimirListadoAsistencia(SspRegistroCurso reg){
        try {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt inst = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getLogin()); // Búsqueda de innstructor por el login
            registro = reg;
            List<SspRegistroCurso> lp = new ArrayList();
            lp.add(registro);
            
            return JsfUtil.generarPdfCursoAsistencia("/aplicacion/gssp/sspRegistroCurso/reportes/listadoAsistencia.jasper", lp, ""+registro.getId(), inst);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String obtenerKeyGoogle(){
        return ejbSbParametroFacade.obtenerParametroXNombre("google_srcapi_key").getValor();
    }
    
    public String obtenerApiGoogle(){
        return ejbSbParametroFacade.obtenerParametroXNombre("google_jsapi").getValor();
    }
    
    public String obtenerMsjeInvalidSize(){
        return JsfUtil.bundleBDIntegrado("sspCursos_fileUpload_InvalidSizeMessage") + " " + 
               (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_sizeLimite_asistencia").getValor())/1024) + " Kb";
    }
    
    public void openDlgVerDatosIntructor(SspProgramacion progra){
        instructor = progra.getInstructorId();
        RequestContext.getCurrentInstance().execute("PF('wvDlgVerDatosInstructor').show()");
        RequestContext.getCurrentInstance().update("frmVerDatosInstructor");        
    }
    
    public void openDlgModificarHorario(SspProgramacion progra){
        programacionConfirma = progra;
        lstCambioHorario = new ArrayList();
        lstProgramacionHorario = new ArrayList();
        lstProgramacionHorarioTemp = new ArrayList();
        for(SspPrograHora horario : programacionConfirma.getSspPrograHoraList()){
            if(horario.getActivo() == 1){
                SspPrograHora newHorario = new SspPrograHora();
                newHorario.setId(horario.getId());
                newHorario.setFechaReg(new Date());
                newHorario.setActivo(horario.getActivo());
                newHorario.setAudLogin(horario.getAudLogin());
                newHorario.setAudNumIp(horario.getAudNumIp());                
                newHorario.setFecha(horario.getFecha());
                newHorario.setHoraInicio(horario.getHoraInicio());
                newHorario.setHoraFin(horario.getHoraFin());
                newHorario.setTipoCurso(horario.getTipoCurso());
                newHorario.setProgramacionId(horario.getProgramacionId());
                lstCambioHorario.add(newHorario);
                
                lstProgramacionHorario.add(horario);                
                
                SspPrograHora newProTemp = new SspPrograHora();
                newProTemp.setId(horario.getId());
                newHorario.setFechaReg(new Date());
                newProTemp.setFecha(horario.getFecha());
                newProTemp.setHoraInicio(horario.getHoraInicio());
                newProTemp.setHoraFin(horario.getHoraFin());
                newProTemp.setTipoCurso(horario.getTipoCurso());
                lstProgramacionHorarioTemp.add(newProTemp);
            }
        }
        setFlagBtnCambio(true);
        RequestContext.getCurrentInstance().execute("PF('wvDlgCambiarHorario').show()");
        RequestContext.getCurrentInstance().update("frmCambiarHorario");       
    }
    
    public String obtenerTituloCambioHorario(){
        String nombre = "";
        List<SspPrograHora> lstHora = new ArrayList();
        
        if(programacionConfirma != null){
            for(SspPrograHora hora : programacionConfirma.getSspPrograHoraList()){
                if(hora.getActivo() == 1){
                    lstHora.add(hora);
                }
            }
            Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                @Override
                public int compare(SspPrograHora one, SspPrograHora other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });
            Date fechaIni = lstHora.get(0).getFecha();
            Date fechaFin = lstHora.get(lstHora.size()-1).getFecha();

            nombre = "El curso " + programacionConfirma.getModuloId().getNombre() + 
                            " con fecha desde " + JsfUtil.formatoFechaDdMmYyyy(fechaIni) + 
                            " hasta el " + JsfUtil.formatoFechaDdMmYyyy(fechaFin) +
                            " tiene los siguientes horarios: ";
        }
        return nombre;
    }
    
    public void confirmarCambioHorarioCurso(){
        boolean validacion = true;
       
        try {
            int horasAcademicas = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horasAcademicas").getValor());
            for(SspPrograHora horario : lstProgramacionHorario){
                if(horario.getActivo() == 1){
                    //////////////// HORA DE INICIO ////////////////
                    if(horario.getHoraInicio() == null){
                        validacion = false;
                        JsfUtil.mensajeError("Debe ingresar todas las horas de inicio del horario");
                        return;
                    }else{
                        if(horario.getHoraInicio().getHours() == 0 ){
                            validacion = false;
                            JsfUtil.mensajeError("Por favor verificar que la hora de inicio esté ingresada correctamente");
                            return;
                        }else{
                            Calendar fechaTemp = Calendar.getInstance();
                            fechaTemp.set(JsfUtil.formatoFechaYyyy(horario.getHoraInicio()), horario.getHoraInicio().getMonth(), horario.getHoraInicio().getDate(), horaFinPermitido, 0,0 );    // permitido hastas las 20:00
                            if(horario.getHoraInicio().getHours() < horaInicioPermitido || (horario.getHoraInicio().compareTo(fechaTemp.getTime()) > 0) ){
                                validacion = false;
                                JsfUtil.mensajeError("El horario permitido es entre las "+horaInicioPermitido+":00 y "+horaFinPermitido+":00 horas ");
                                return;
                            }
                        }
                    }
                    /////////////////////////////////////////////
                    
                    //////////////// HORA DE FIN ////////////////                    
                    if(horario.getHoraFin() == null){
                        validacion = false;
                        JsfUtil.mensajeError("Debe ingresar todas las horas de fin del horario");
                    }else{
                        if(horario.getHoraFin().getHours() == 0 ){
                            validacion = false;
                            JsfUtil.mensajeError("Por favor verificar que la hora de fin esté ingresada correctamente");
                        }else{
                            Calendar fechaTemp = Calendar.getInstance();
                            fechaTemp.set(JsfUtil.formatoFechaYyyy(horario.getHoraFin()), horario.getHoraFin().getMonth(), horario.getHoraFin().getDate(), horaFinPermitido, 0,0 );     // permitido hastas las 20:00
                            if(horario.getHoraFin().getHours() < horaInicioPermitido || (horario.getHoraFin().compareTo(fechaTemp.getTime()) > 0) ){
                                validacion = false;
                                JsfUtil.mensajeError("El horario permitido es entre las "+horaInicioPermitido+":00 y "+horaFinPermitido+":00 horas ");
                            }
                        }
                    }
                    ////////////////////////////////////////////////
                    
                    if(horario.getHoraInicio() != null && horario.getHoraFin() != null){
                        if(horario.getFecha() != null){
                            Calendar fechaTemp = Calendar.getInstance();
                            fechaTemp.set(JsfUtil.formatoFechaYyyy(horario.getFecha()), horario.getFecha().getMonth(), horario.getFecha().getDate(),horario.getHoraInicio().getHours(), horario.getHoraInicio().getMinutes(),0 );
                            horario.setHoraInicio(fechaTemp.getTime());
                            fechaTemp.set(JsfUtil.formatoFechaYyyy(horario.getFecha()), horario.getFecha().getMonth(), horario.getFecha().getDate(),horario.getHoraFin().getHours(), horario.getHoraFin().getMinutes(),0 );
                            horario.setHoraFin(fechaTemp.getTime());

                            if(horario.getHoraInicio().compareTo(horario.getHoraFin()) > 0){
                                JsfUtil.mensajeError("La hora de inicio no puede ser mayor que la hora final");
                                validacion = false;
                                return;
                            }

                            long minutos = JsfUtil.calculaMinutosEntreHoras(horario.getHoraInicio(), horario.getHoraFin());
                            if(minutos < horasAcademicas){
                                JsfUtil.mensajeError("No puede ingresar un rango de horas menor a "+horasAcademicas+" minutos");
                                validacion = false;
                            }
                            int horasAcademicasTotales = (int) Math.ceil((double) minutos/horasAcademicas);
                            if(!horario.getProgramacionId().getModuloId().getCodModulo().equals("REFB") && !horario.getProgramacionId().getModuloId().getCodModulo().equals("REFP") ){
                                if( horasAcademicasTotales > limiteHorasAcad  ){
                                    JsfUtil.mensajeError("No puede ingresar más de "+limiteHorasAcad+" hora(s) académica(s)");
                                    validacion = false;
                                }
                            }else{
                                if( horasAcademicasTotales > limiteHorasAcadRef  ){
                                    JsfUtil.mensajeError("No puede ingresar más de "+limiteHorasAcadRef+" hora(s) académica(s)");
                                    validacion = false;
                                }
                            }
                        }
                    }

                    if(validacion){
                        //Validación de horas academicas x dia
                        long minutos = 0;
                        for(SspProgramacion prog : lstProgramacion){
                            if(prog.getActivo() == 0){
                                continue;
                            }
                            if(prog.getModuloId().getCodModulo().equals("REFB") || prog.getModuloId().getCodModulo().equals("REFP")){
                                continue;
                            }
                            for(SspPrograHora cap : prog.getSspPrograHoraList()){
                                if(cap.getActivo() == 0){
                                    continue;
                                }
                                if (cap.getActivo() == 1 && JsfUtil.getFechaSinHora(cap.getFecha()).compareTo(JsfUtil.getFechaSinHora(horario.getFecha())) == 0) {
                                    minutos += JsfUtil.calculaMinutosEntreHoras(cap.getHoraInicio(), cap.getHoraFin());
                                }
                            }
                        }
                        int horasAcademicasTotales = (int) Math.ceil((double) minutos/horasAcademicas);
                        if( horasAcademicasTotales > limiteHorasAcad  ){
                            JsfUtil.mensajeError("No puede ingresar más de "+limiteHorasAcad+" hora(s) académica(s) en la fecha "+JsfUtil.formatoFechaDdMmYyyy(horario.getFecha()));
                            validacion = false;
                            return;
                        }
                        ///

                        // Validación de fechas
                        for(SspProgramacion prog : lstProgramacion){
                            if(prog.getActivo() == 0){
                                continue;
                            }
                            if(Objects.equals(prog.getId(), programacionConfirma.getId())){
                                continue;
                            }
                            
                            if(!Objects.equals(prog.getLocalId().getId(), programacionConfirma.getLocalId().getId())){

                               for(SspPrograHora cap : prog.getSspPrograHoraList()){
                                    if (cap.getActivo() == 1 && JsfUtil.getFechaSinHora(cap.getFecha()).compareTo(JsfUtil.getFechaSinHora(horario.getFecha())) == 0) {
                                        if( (JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) < 0 && JsfUtil.getFechaSoloHora(horario.getHoraFin()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0)  ||
                                            (JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) >= 0 && JsfUtil.getFechaSoloHora(horario.getHoraFin()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) <= 0) ||
                                            (JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0 && JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) < 0) ||
                                            (JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) <= 0 && JsfUtil.getFechaSoloHora(horario.getHoraFin()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) >= 0)
                                            ){
                                            idColorValidate = prog.getId();
                                            JsfUtil.mensajeError("Ya se ha registrado este curso en un local distinto y con un rango de fechas similares.");
                                            validacion = false;
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Validacion de horas
                        for(SspProgramacion prog : lstProgramacion){
                            if(prog.getActivo() == 0){
                                continue;
                            }
                            for(SspPrograHora cap : prog.getSspPrograHoraList()){
                                if(cap.getActivo() == 1){
                                    if(Objects.equals(cap.getId(), horario.getId())){
                                        continue;
                                    }
                                    if(Objects.equals(cap.getProgramacionId().getId(), prog.getId())){
                                        continue;
                                    }
                                    if (JsfUtil.getFechaSinHora(cap.getFecha()).compareTo(JsfUtil.getFechaSinHora(horario.getFecha())) == 0) {
                                        if( (JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) < 0 && JsfUtil.getFechaSoloHora(horario.getHoraFin()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0)  ||
                                            (JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) >= 0 && JsfUtil.getFechaSoloHora(horario.getHoraFin()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) <= 0) ||
                                            (JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) > 0 && JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) < 0) ||
                                            (JsfUtil.getFechaSoloHora(horario.getHoraInicio()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraInicio())) <= 0 && JsfUtil.getFechaSoloHora(horario.getHoraFin()).compareTo(JsfUtil.getFechaSoloHora(cap.getHoraFin())) >= 0)
                                            ){
                                            JsfUtil.mensajeError("Ya se ha registrado un horario con horas en común. Horario: "+ JsfUtil.formatoFechaDdMmYyyy(horario.getFecha()));
                                            validacion = false;
                                        }
                                    }
                                    if(!validacion)
                                        break;
                                }
                            }
                        }
                    }
                }
            }
            
            if(validacion){
                cambioHorario = true;
                RequestContext.getCurrentInstance().execute("PF('wvDlgCambiarHorario').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm()); 
            }
        } catch (Exception e) {
            System.err.println("Usuario: "+JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al validar los horarios");
        }
    }
    
    public void cancelarCambioHorarioCurso(){
        if(lstProgramacionHorarioTemp != null && !lstProgramacionHorarioTemp.isEmpty()){
            for(SspPrograHora horario : lstProgramacionHorario){
                if(horario.getActivo() == 1){
                    for(SspPrograHora horarioOriginal : lstProgramacionHorarioTemp ){
                        if( Objects.equals(horarioOriginal.getId(), horario.getId()) ){
                            horario.setHoraInicio(horarioOriginal.getHoraInicio());
                            horario.setHoraFin(horarioOriginal.getHoraFin());
                            break;
                        }
                    }                    
                }
            }
            for(SspPrograHora horario : programacionConfirma.getSspPrograHoraList()){
                if(horario.getActivo() == 1){
                    for(SspPrograHora horarioOriginal : lstProgramacionHorarioTemp ){
                        if( Objects.equals(horarioOriginal.getId(), horario.getId()) ){
                            horario.setHoraInicio(horarioOriginal.getHoraInicio());
                            horario.setHoraFin(horarioOriginal.getHoraFin());
                            break;
                        }
                    }                    
                }
            }
        }
        RequestContext.getCurrentInstance().execute("PF('wvDlgCambiarHorario').hide()");
        RequestContext.getCurrentInstance().update(":frmCambiarHorario:dtProgramacion");
    }
    
    public void timeSelectListener(TimeSelectEvent timeSelectEvent){
        ///RequestContext.getCurrentInstance().update(":frmCambiarHorario:dtProgramacion");
    }
  
    public void closeListener(CloseEvent closeEvent) {  
        //RequestContext.getCurrentInstance().update(":frmCambiarHorario:dtProgramacion");
    }  
    
    public void onCellEditHorario(CellEditEvent event) {
        Calendar fechaTemp = null;
        if(event.getColumn().getHeaderText().equals(JsfUtil.bundleBDIntegrado("sspCursos_crear_dtProgramacion_horaIni"))){
            Object newValue = event.getNewValue();
            if(newValue != null){
                Date nuevaHora = (Date) newValue;
                if(nuevaHora != null){
                    if(nuevaHora.getHours() == 0 ){
                        JsfUtil.mensajeError("Por favor ingrese una hora de inicio correcta");
                        return;
                    }else{
                        fechaTemp = Calendar.getInstance();
                        fechaTemp.set(JsfUtil.formatoFechaYyyy(nuevaHora), nuevaHora.getMonth(), nuevaHora.getDate(), horaFinPermitido, 0,0 );    // permitido hastas las 20:00
                        if(nuevaHora.getHours() < horaInicioPermitido || (nuevaHora.compareTo(fechaTemp.getTime()) > 0) ){
                            RequestContext.getCurrentInstance().update("dtProgramacion");                        
                            JsfUtil.mensajeError("El horario permitido es entre las "+horaInicioPermitido+":00 y "+horaFinPermitido+":00 horas ");
                            return;
                        }
                    }
                    fechaTemp = Calendar.getInstance();
                    fechaTemp.set(JsfUtil.formatoFechaYyyy(lstProgramacionHorario.get(event.getRowIndex()).getHoraFin()), 
                                    lstProgramacionHorario.get(event.getRowIndex()).getHoraFin().getMonth(),
                                    lstProgramacionHorario.get(event.getRowIndex()).getHoraFin().getDate(),
                                    nuevaHora.getHours(),
                                    nuevaHora.getMinutes(),0
                                 );
                    lstCambioHorario.get(event.getRowIndex()).setHoraInicio(fechaTemp.getTime());
                    for(int i = 0; i < lstProgramacionHorario.size(); i++){
                        if(i != event.getRowIndex()){
                            lstProgramacionHorario.get(i).setHoraInicio(lstCambioHorario.get(i).getHoraInicio());
                        }                        
                    }
                }else{
                    JsfUtil.mensajeError("Error al procesar la hora de inicio");
                    return;
                }
            }else{
                JsfUtil.mensajeError("Ingrese la hora de inicio");
                return;
            }
        }
        if(event.getColumn().getHeaderText().equals(JsfUtil.bundleBDIntegrado("sspCursos_crear_dtProgramacion_horaFin"))){
            Object newValue = event.getNewValue();
            if(newValue != null){
                Date nuevaHora = (Date) newValue;
                if(nuevaHora != null){
                    if(nuevaHora.getHours() == 0 ){
                        JsfUtil.mensajeError("Por favor ingrese una hora de fin correcta");
                        return;
                    }else{
                        fechaTemp = Calendar.getInstance();
                        fechaTemp.set(JsfUtil.formatoFechaYyyy(nuevaHora), nuevaHora.getMonth(), nuevaHora.getDate(), horaFinPermitido, 0,0 );    // permitido hastas las 20:00
                        if(nuevaHora.getHours() < horaInicioPermitido || (nuevaHora.compareTo(fechaTemp.getTime()) > 0) ){
                            RequestContext.getCurrentInstance().update("dtProgramacion");                        
                            JsfUtil.mensajeError("El horario permitido es entre las "+horaInicioPermitido+":00 y "+horaFinPermitido+":00 horas ");
                            return;
                        }
                    }
                    fechaTemp = Calendar.getInstance();
                    fechaTemp.set(JsfUtil.formatoFechaYyyy(lstProgramacionHorario.get(event.getRowIndex()).getHoraInicio()), 
                                    lstProgramacionHorario.get(event.getRowIndex()).getHoraInicio().getMonth(),
                                    lstProgramacionHorario.get(event.getRowIndex()).getHoraInicio().getDate(),
                                    nuevaHora.getHours(),
                                    nuevaHora.getMinutes(),0
                                 );
                    lstCambioHorario.get(event.getRowIndex()).setHoraFin(fechaTemp.getTime());
                    for(int i = 0; i < lstProgramacionHorario.size(); i++){
                        if(i != event.getRowIndex()){
                            lstProgramacionHorario.get(i).setHoraFin(lstCambioHorario.get(i).getHoraFin());
                        }                        
                    }
                }else{
                    JsfUtil.mensajeError("Error al procesar la hora de fin");
                    return;
                }
            }else{
                JsfUtil.mensajeError("Ingrese la hora de fin");
                return;
            }
        }
    }
    
    /**
     * FUNCION PARA GENERAR CONSTANCIA DE CURSOS
     * @author Richar Fernández
     * @version 1.0
     * @param reg
     * @return Reporte pdf
     */
    public StreamedContent imprimirListadoTermino(SspRegistroCurso reg){
        try {
            registro = reg;
            List<SspRegistroCurso> lp = new ArrayList();
            lp.add(registro);
            
            return JsfUtil.generarReportePdf("/aplicacion/gssp/sspRegistroCurso/reportes/listadoTermino.jasper", lp, parametrosPdftermino(reg), ""+registro.getId());            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap parametrosPdftermino(SspRegistroCurso ppdf){
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();

        preparaDatosCursos(ppdf);
        List<SspModulo> lstModulosTemp = ejbSspModuloFacade.listarModuloByTipoCursoSinRefrigerio(tipoFormacion.getId());
        ph.put("P_LOGOSUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_h.png"));
        ph.put("P_EXPEDIENTE", ppdf.getNroExpediente() );
        ph.put("P_TIPOFORMACION", tipoFormacion.getCodProg() );
        ph.put("P_LISTA_ALUMNOS", listarALumnosConstanciaTermino(lstModulosTemp) );
        ph.put("P_MODULOS", listarModulosConstanciaTermino(lstModulosTemp) );
        
        return ph;
    }
    
    public List<Map> listarModulosConstanciaTermino(List<SspModulo> lstModulosTemp){
        List<Map> lista = new ArrayList();
        for(SspModulo mod : lstModulosTemp){
            if(mod.getActivo() == 1){
                Map nmap = new HashMap();
                nmap.put("cod_modulo", mod.getCodModulo() );
                nmap.put("nombre", mod.getNombre() );
                lista.add(nmap);
            }
        }
        return lista;
    }
    
    public List<Map> listarALumnosConstanciaTermino(List<SspModulo> lstModulosTemp){
        List<Map> lista = new ArrayList();
        int cont = 0;
        boolean flag;
        Map nmap = null;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        
        SbPersonaGt admin = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        
        Collections.sort(lstAlumnosVigilantes, new Comparator<SspAlumnoCurso>() {
            @Override
            public int compare(SspAlumnoCurso one, SspAlumnoCurso other) {
                return ( (one.getPersonaId().getApePat() != null)?one.getPersonaId().getApePat():one.getPersonaId().getNombres()) .compareTo( ((other.getPersonaId().getApePat() != null)?other.getPersonaId().getApePat():other.getPersonaId().getNombres()) );
            }
        });
        for(SspAlumnoCurso alumn : lstAlumnosVigilantes){
            if(!validaAlumnoActivo(alumn)){
                continue;
            }
            
            nmap = new HashMap();
            cont++;
            nmap.put("apellido_pat", alumn.getPersonaId().getApePat() );
            nmap.put("apellido_mat", alumn.getPersonaId().getApeMat() );
            nmap.put("nombres", alumn.getPersonaId().getNombres());
            nmap.put("numDoc", alumn.getPersonaId().getNumDoc());
            nmap.put("id", cont);
            
            for(SspModulo mod : lstModulosTemp ){
                flag = false;
                for(SspProgramacion progra : alumn.getRegistroCursoId().getSspProgramacionList() ){
                    if(progra.getActivo() == 1 && progra.getInstructorId() != null &&
                       Objects.equals( progra.getModuloId().getId(), mod.getId()) &&
                       Objects.equals( progra.getInstructorId().getPersonaId().getId(), admin.getId() )
                       ){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    if(alumn.getRegistroCursoId().getEstadoId().getCodProg().equals("TP_ECC_EVA")){
                        nmap.put(mod.getCodModulo(), " ");
                    }else{
                        boolean tieneNota = false;
                        for(SspNotas notasAct : alumn.getSspNotasList()){
                            if(notasAct.getActivo() == 1 &&
                               Objects.equals(notasAct.getAlumnoId().getId(), alumn.getId()) &&
                               Objects.equals(notasAct.getModuloId().getId(), mod.getId()) &&
                               Objects.equals(notasAct.getRegcursoId().getId(), alumn.getRegistroCursoId().getId())
                               ){
                               nmap.put(mod.getCodModulo(), ""+notasAct.getNotaInt());
                               tieneNota = true;
                               break;
                            }
                        }
                        if(!tieneNota){
                            nmap.put(mod.getCodModulo(), " ");
                        }
                    }                        
                }else{
                    nmap.put(mod.getCodModulo(), " X ");
                }
            }
            lista.add(nmap);
        }
        return lista;
    }
    
    public void cambiarLocalAgendaCurso(){
        if(idLocal != null && lstLocales != null && !lstLocales.isEmpty()){
            for(SspLocal loc : lstLocales){
                if(Objects.equals(loc.getId(), idLocal)){
                    DIAS_PRESENTACION = ((loc.getDistritoId().getProvinciaId().getDepartamentoId().getId() == 7 || loc.getDistritoId().getProvinciaId().getDepartamentoId().getId() == 15)?(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoLima").getValor())):(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_diasFechaProcesamientoProvincia").getValor())));
                    break;
                }
            }
        }
    }
    
    public List<CursosOption> getListadoModulosSinRefrigerio(){
        List<CursosOption> lista = new ArrayList();
        if(lstCursosOpt != null && !lstCursosOpt.isEmpty() ){
            for(CursosOption co : lstCursosOpt){
                if(!co.getModulo().getCodModulo().equals("REFB") && !co.getModulo().getCodModulo().equals("REFP")
                   ){
                    lista.add(co);
                }
            }
        }
        return lista;
    }
    
    public String mostrarDescModuloRegistro(SspModulo mod){
        String nombre = "";
        if(mod != null){
            nombre = mod.getNombre();
            if(!mod.getCodModulo().equals("REFB") && !mod.getCodModulo().equals("REFP") ){
                nombre += (" " + mod.getCodModulo());
            }
        }   
        return nombre;
    }
    
    public List<SspModulo> getLstModulosSinRefrigerio() {
        List<SspModulo> listado = new ArrayList();
        if(lstModulos != null && !lstModulos.isEmpty()){
            for(SspModulo mod : lstModulos){
                if(!mod.getCodModulo().equals("REFB") && !mod.getCodModulo().equals("REFP") ){
                    listado.add(mod);
                }
            }
        }
        return listado;
    }
    
    public String obtenerColorValidacion(SspProgramacion progra){
        String style = "";
        
        if(Objects.equals(progra.getId(), idColorValidate)){
            style = "cursoValidate";
        }
        
        return style;
    }
    
    public List<SspProgramacion> obtenerDetalleProgramacionXCurso(SspModulo item){
        List<SspProgramacion> lista = new ArrayList();
        if(lstProgramacion != null && item != null){
            for(SspProgramacion progra : lstProgramacion){
                if(progra.getActivo() == 1 && Objects.equals(item.getId(), progra.getModuloId().getId())){
                    lista.add(progra);
                }
            }
            Collections.sort(lista, new Comparator<SspProgramacion>() {
                @Override
                public int compare(SspProgramacion one, SspProgramacion other) {
                    return one.getId().compareTo(other.getId());
                }
            });
        }
        return lista;
    }
    
    public List<SspProgramacion> obtenerProgramacionXModulo(){
        List<SspProgramacion> listado = new ArrayList();
        if(programacionConfirma != null){
            for(SspProgramacion progra : lstProgramacion){
                if(Objects.equals(progra.getModuloId().getId(), programacionConfirma.getModuloId().getId())){
                    listado.add(progra);
                }
            }
        }
        return listado;
    }
    
    public String obtenerNombreInstructor(SspProgramacion progra){
        String nombre = "-";
        if(progra != null){
            if(!progra.getModuloId().getCodModulo().equals("REFB") && !progra.getModuloId().getCodModulo().equals("REFP") ){
                nombre = progra.getInstructorId().getPersonaId().getNombreCompleto();
            }
        }else{
            nombre = "";
        }
        return nombre;
    }
 
    public String obtenerColorVer(SspProgramacion progra){
        String color = null;
        int cont = 0;        
        
        if(progra != null){
            if(!esInstructor){
                return null;
            }
            for(SspInstructorModulo instModulo : registro.getSspInstructorModuloList()){
                if(Objects.equals(instModulo.getInstructorId().getPersonaId().getId(), JsfUtil.getLoggedUser().getPersonaId()) &&
                   Objects.equals(instModulo.getRegcursoId().getId(), registro.getId()) &&
                   Objects.equals(instModulo.getModuloId().getId(), progra.getModuloId().getId()) &&
                   instModulo.getActivo() == 1
                   ){
                    cont ++;
                }
            }
            if(cont > 0){
                color = "datatableCursos-row-cursoPermitidoDictado";
            }
        }
        return color;
    }
    
    public void updateCambioHorarioInicio(){
        String value = JsfUtil.getRequestParameter("javax.faces.source");
        if(value != null && !value.isEmpty()){
            String[] parts = value.split(":");
            if(parts.length > 4){
                String indiceTable = parts[5];
                RequestContext.getCurrentInstance().update(":frmCambiarHorario:dtProgramacion:"+indiceTable+":horaInicio");
            }
        }
    }
    
    public void updateCambioHorarioFin(){
        String value = JsfUtil.getRequestParameter("javax.faces.source");
        if(value != null && !value.isEmpty()){
            String[] parts = value.split(":");
            if(parts.length > 4){
                String indiceTable = parts[5];
                RequestContext.getCurrentInstance().update(":frmCambiarHorario:dtProgramacion:"+indiceTable+":horaFin");
            } 
        }
    }
    
    public boolean disabledFechaHorario(Date fecha){
        boolean disabled = true;
        if(fecha != null){
            if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fecha)) <= 0  ){
                disabled = false;
            }
        }
        return disabled;
    }
    
    public String obtenerColorHorario(Date fecha){
        String color = "";
        if(fecha != null){
            if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fecha)) <= 0  ){
                color = "background-color:#A9E2F3;";
            }
        }
        return color;
    }
    
    public String obtenerColorCambioHorario(SspPrograHora hora, int tipo){
        String color = "";
        Date fecha = null;
        if(hora != null){
            if(tipo == 1){
                if(hora.getHoraInicio()== null){
                    return color;
                }
                Calendar fechaTemp = Calendar.getInstance();
                fechaTemp.set(JsfUtil.formatoFechaYyyy(hora.getFecha()), hora.getFecha().getMonth(), hora.getFecha().getDate(),hora.getHoraInicio().getHours(), hora.getHoraInicio().getMinutes(),0 );
                hora.setHoraInicio(fechaTemp.getTime());                
                fecha = hora.getHoraInicio();
            }else{
                if(hora.getHoraFin()== null){
                    return color;
                }
                Calendar fechaTemp = Calendar.getInstance();
                fechaTemp.set(JsfUtil.formatoFechaYyyy(hora.getFecha()), hora.getFecha().getMonth(), hora.getFecha().getDate(),hora.getHoraFin().getHours(), hora.getHoraFin().getMinutes(),0 );
                hora.setHoraFin(fechaTemp.getTime());
                fecha = hora.getHoraFin();
            }
            if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fecha)) <= 0  ){
                color = "background-color:#A9E2F3;";
            }
        }
        return color;
    }
    
    public void cambiarModulo(){
        renderTipoCurso = false;
        if(moduloAgendarSelected != null){
            if(moduloAgendarSelected.getCodModulo().equals("ACM66") || moduloAgendarSelected.getCodModulo().equals("ACM47") || moduloAgendarSelected.getCodModulo().equals("ACM48") || moduloAgendarSelected.getCodModulo().equals("ACM49") ||// ARMAS: CONOCIMIENTO Y MANIPULACION
               moduloAgendarSelected.getCodModulo().equals("DP68") || moduloAgendarSelected.getCodModulo().equals("DP50") || moduloAgendarSelected.getCodModulo().equals("DP60") ||  moduloAgendarSelected.getCodModulo().equals("DP51") || moduloAgendarSelected.getCodModulo().equals("DP61") ||    // DEFENSA PERSONAL
               moduloAgendarSelected.getCodModulo().equals("PA49") || moduloAgendarSelected.getCodModulo().equals("PA67") || moduloAgendarSelected.getCodModulo().equals("PA68") || moduloAgendarSelected.getCodModulo().equals("PA69") ||      // PRIMEROS AUXILIOS
               moduloAgendarSelected.getCodModulo().equals("RED63")                                                             // REDACCIÓN Y ELABORACIÓN DE DOCUMENTOS
               ){
                renderTipoCurso = true;
                
                if(tipoFormacion != null && tipoFormacion.getCodProg().equals("TP_FORMC_PRF") && moduloAgendarSelected.getCodModulo().equals("RED63") ){
                    renderTipoCurso = false;    
                }
            }
        }else{
            JsfUtil.mensajeAdvertencia("Por favor seleccionar un módulo");
        }
    }
    
    public List<TipoSeguridad> getLstTipoCursos(){
        return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_TPCUR_TEO','TP_TPCUR_PRA'");
    }
    
    public String obtenerColorHorarioTeoria(SspPrograHora horario){
        String color = "";
        
        if(horario != null && horario.getTipoCurso() != null){
            if(horario.getTipoCurso().getCodProg().equals("TP_TPCUR_TEO")){
                color = "dtHorario-row-teoria";
            }else{
                color = "dtHorario-row-practica";
            }
        }
        return color;
    }
    
    public boolean renderTabAlumnos(){
        boolean validacion = true;
        if(registro != null && registro.getSspProgramacionList() != null){
            List<SspPrograHora> lstHora = new ArrayList();
            for(SspProgramacion prog : registro.getSspProgramacionList()){
                if(prog.getActivo() == 1){
                    if(prog.getModuloId().getCodModulo().equals("REFB") || prog.getModuloId().getCodModulo().equals("REFP")){
                        continue;
                    }
                    lstHora = new ArrayList();
                    for(SspPrograHora hora : prog.getSspPrograHoraList()){
                        if(hora.getActivo() == 1){
                            lstHora.add(hora);
                        }
                    }
                    Collections.sort(lstHora, new Comparator<SspPrograHora>() {
                        @Override
                        public int compare(SspPrograHora one, SspPrograHora other) {
                            return one.getFecha().compareTo(other.getFecha());
                        }
                    });

                    if( JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(lstHora.get(0).getFecha())) >= 0){
                        validacion = false;
                    }
                }
            }
        }else{
            validacion = false;
        }
        return validacion;
    }
    
    public boolean renderColumnEvaluacionPj(){
        return !esInstructor;
    }
    
    public String obtenerOficioMSIAP(SspAlumnoCurso alumn){
        String nombre = "";
        if(alumn.getOficioMsiap() != null && !alumn.getOficioMsiap().isEmpty()){
            nombre = "Oficio MSIAP: <b>"+alumn.getOficioMsiap()+"</b><br/>" ;
        }
        return nombre;
    }
    
    public String obtenerIconEvaluacion(SspAlumnoCurso alumno){
        boolean valida = false;
        String nombre = "";
        if(alumno != null){
            if(!valida && alumno.getEvaluacionPj() != null){
                if(alumno.getOficioMsiap() != null && !alumno.getOficioMsiap().isEmpty()){
                    if(alumno.getEvaluacionPj() == 1){
                        nombre = "/resources/imagenes/icon_error.png";
                    }else{
                        nombre = "/resources/imagenes/icon_check_yellow.png";
                    }
                }else{
                    nombre = "/resources/imagenes/icon_check.png";
                }
            }else if(alumno.getEvaluacionPj() == null){
                nombre = "/resources/imagenes/icon_admiracion.png";
            }
        }
        return nombre;
    }
    
    public String obtenerMsjeEvaluacion(SspAlumnoCurso alumno){
        boolean valida = false;
        String nombre = "";
        if(alumno != null){
            if(!valida && alumno.getEvaluacionPj() != null){
                if(alumno.getEvaluacionPj() == 1){
                    nombre = "CON ANTECEDENTES";
                }else{
                    nombre = "SIN OBSERVACIÓN";
                }
            }else if(alumno.getEvaluacionPj() == null){
                nombre = "PENDIENTE DE EVALUACIÓN CON EL PODER JUDICIAL";
            }
        }
        return nombre;
    }
    
    public Date obtenerFechaTransmision(SspRegistroCurso cRegistro){
        if(cRegistro != null){
            Collections.sort(cRegistro.getSspCursoEventoList(), new Comparator<SspCursoEvento>() {
                @Override
                public int compare(SspCursoEvento one, SspCursoEvento other) {
                    return one.getFecha().compareTo(other.getFecha());
                }
            });
            for(SspCursoEvento evento : cRegistro.getSspCursoEventoList()){
                if(evento.getActivo() == 1 && evento.getTipoEventoId().getCodProg().equals("TP_ECC_TRA")){
                    return evento.getFecha();
                }
            }
        }
        return null;
    }
    
    public String obtenerMsjeInvalidSizeNotas(){
        return JsfUtil.bundleBDIntegrado("sspCursos_fileUpload_InvalidSizeMessage") + " " + 
               (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_sizeLimite_archivoNotas").getValor())/1024) + " Kb";
    }
    
    public boolean esNoPresentado(){
        if(registro != null && (registro.getEstadoId().getCodProg().equals("TP_ECC_NPR") || 
                                registro.getEstadoId().getCodProg().equals("TP_ECC_FDP") || 
                                registro.getEstadoId().getCodProg().equals("TP_ECC_DES")) ){
            return true;
        }
        return false;
    }
    
    public boolean tieneResolucionVigilanciaPrivada(){
        if(lstModalidades != null){
            for(TipoSeguridad tipo : lstModalidades ){
                if(tipo.getCodProg().equals("TP_MCO_VIG")){
                    return true;
                }
            }
        }
        return false;
    }
    
    public String obtenerTitleList(){
        if(esEvento){
            return JsfUtil.bundleBDIntegrado("sspCursos_eventos_list_title");
        }
        return JsfUtil.bundleBDIntegrado("sspCursos_list_title");
    }
    
    public String obtenerTitleEdit(){
        if(esEvento){
            return JsfUtil.bundleBDIntegrado("sspCursos_eventos_editar_title");
        }
        return JsfUtil.bundleBDIntegrado("sspCursos_editar_title");
    }
    
    public String obtenerTitleCreate(){
        if(esEvento){
            return JsfUtil.bundleBDIntegrado("sspCursos_eventos_crear_title");
        }
        return JsfUtil.bundleBDIntegrado("sspCursos_crear_title");
    }
    
    public String obtenerTitleVer(){
        if(esEvento){
            return JsfUtil.bundleBDIntegrado("sspCursos_eventos_ver_title");
        }
        return JsfUtil.bundleBDIntegrado("sspCursos_ver_title");
    }
    
    public String obtenerTitleModificatoria(){
        if(esEvento){
            return JsfUtil.bundleBDIntegrado("sspCursos_eventos_modificatoria_title");
        }
        return JsfUtil.bundleBDIntegrado("sspCursos_modificatoria_title");
    }
    
    public String obtenerTitleActualizacion(){
        if(esEvento){
            return JsfUtil.bundleBDIntegrado("sspCursos_eventos_actualizacionDocs_title");
        }
        return JsfUtil.bundleBDIntegrado("sspCursos_actualizacionDocs_title");
    }
    
    public String obtenerColorTipoCurso(SspRegistroCurso item){
        String color = "";
        if(item.getTipoCurso().getCodProg().equals("TP_GSSP_CURSEVT")){
            color = "dtHorario-row-teoria";
        }
        return color;
    }
    
    public void cargarParametrosGlobales(){
        limiteHorasAcad = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_maximoHorasAcademicas").getValor());
        limiteHorasAcadRef = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_maximoHorasAcademicasRefrigerio").getValor());
        horaInicioPermitido = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horaInicioPermitido").getValor());
        horaFinPermitido = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horaFinPermitido").getValor());
        aforoLocalPermitido = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_aforoLocal").getValor());
        fechaCortePase = JsfUtil.stringToDate(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_fechaCortePase").getValor(), "dd/MM/yyyy");
    }
    
    public void validarFechaCorte(SspRegistroCurso cRegistro){
        for(SspCursoEvento evento : cRegistro.getSspCursoEventoList()){
            if(evento.getActivo() == 0){
                continue;
            }
            if(evento.getTipoEventoId().getCodProg().equals("TP_ECC_TRA")){
                if(JsfUtil.getFechaSinHora((Date) evento.getFecha()).compareTo(JsfUtil.getFechaSinHora(fechaCortePase)) < 0){
                    limiteHorasAcad = Integer.parseInt(JsfUtil.bundleBDIntegrado("sspCursos_crear_maximoHorasAcademicas"));
                    aforoLocalPermitido = Integer.parseInt(JsfUtil.bundleBDIntegrado("sspCursos_crear_aforoLocal"));
                    return;
                }
            }
        }
        limiteHorasAcad = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_maximoHorasAcademicas").getValor());
        aforoLocalPermitido = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_aforoLocal").getValor());
    }
    
    public boolean validaHorasAcademicas(){
        boolean validacion = true;
        // Validacion de horas académicas //
        long minConstitucion = 0, minLegislacion = 0, minEtica = 0, minNormas = 0, minControl = 0, minAtencion = 0, minAlarmas = 0;
        long minArmaTeoria = 0, minArmaPractica = 0, minDefTeoria = 0, minDefPractica = 0, minPAuxTeoria = 0, minPAuxPractica = 0, minRedTeoria = 0, minRedPractica = 0;
        List<Date> lstFechas = new ArrayList();
        long sumaHoras = 0;        
        verificarVariableNulls(lstProgramacion);
        
        if(lstModulos == null){
            lstModulos = new ArrayList();
        }
        if(lstModulos.isEmpty()){
            lstModulos = ejbSspModuloFacade.listarModuloByTipoCurso(tipoFormacion.getId());
        }
        for(SspProgramacion prog : lstProgramacion){
            if(prog.getActivo() == 1 && !prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")){
                for(SspPrograHora cap : prog.getSspPrograHoraList()){
                    if(cap.getActivo() == 1){
                        long minutos = JsfUtil.calculaMinutosEntreHoras(cap.getHoraInicio(), cap.getHoraFin());                                

                        if(!prog.getModuloId().getCodModulo().equals("REFB") && !prog.getModuloId().getCodModulo().equals("REFP")){
                            if(!lstFechas.contains(cap.getFecha())){
                                lstFechas.add(cap.getFecha());
                            }
                            sumaHoras += minutos;
                        }

                        if(tipoFormacion.getCodProg().equals("TP_FORMC_BAS") || tipoFormacion.getCodProg().equals("TP_FORMC_BASEVNT")){
                            switch(prog.getModuloId().getCodModulo()){
                                case "CDH40":
                                case "CDH50":
                                            minConstitucion += minutos;
                                            break;
                                case "LSP60":
                                case "LSP62":
                                            minLegislacion += minutos;
                                            break;
                                case "ESP42":
                                case "ESP52":
                                            minEtica += minutos;
                                            break;
                                case "NPS61":
                                case "NPS71":
                                            minNormas += minutos;
                                            break;
                                case "CES44":
                                case "CES54":
                                            minControl += minutos;
                                            break;
                                case "AUP62":
                                case "AUP72":
                                            minAtencion += minutos;
                                            break;
                                case "ACM47":
                                        if(cap.getTipoCurso() == null || (cap.getTipoCurso() != null && cap.getTipoCurso().getCodProg().equals("TP_TPCUR_TEO"))){
                                            minArmaTeoria += minutos;
                                        }else{
                                            minArmaPractica += minutos;
                                        }
                                        break;
                                case "RED63":
                                        if(cap.getTipoCurso() == null || (cap.getTipoCurso() != null && cap.getTipoCurso().getCodProg().equals("TP_TPCUR_TEO"))){
                                            minRedTeoria += minutos;
                                        }else{
                                            minRedPractica += minutos;
                                        }
                                        break;
                                case "PA49":
                                case "PA69":
                                        if(cap.getTipoCurso() == null || (cap.getTipoCurso() != null && cap.getTipoCurso().getCodProg().equals("TP_TPCUR_TEO"))){
                                            minPAuxTeoria += minutos;
                                        }else{
                                            minPAuxPractica += minutos;
                                        }
                                        break;
                                case "CSA64":
                                case "CSA74":
                                        minAlarmas += minutos;
                                        break;
                                case "DP50":
                                case "DP60":
                                        if(cap.getTipoCurso() == null || (cap.getTipoCurso() != null && cap.getTipoCurso().getCodProg().equals("TP_TPCUR_TEO"))){
                                            minDefTeoria += minutos;
                                        }else{
                                            minDefPractica += minutos;
                                        }
                                        break;

                            }
                        }else{
                            switch(prog.getModuloId().getCodModulo()){
                                case "LSP41":
                                case "LSP61":
                                        minLegislacion += minutos;
                                        break;
                                case "CES65":
                                case "CES55":
                                        minControl += minutos;
                                        break;
                                case "AUP46":
                                case "AUP71":
                                        minAtencion += minutos;
                                        break;
                                case "ACM66":
                                        if(cap.getTipoCurso() == null || (cap.getTipoCurso() != null && cap.getTipoCurso().getCodProg().equals("TP_TPCUR_TEO"))){
                                            minArmaTeoria += minutos;
                                        }else{
                                            minArmaPractica += minutos;
                                        }
                                        break;
                                case "PA67":
                                case "PA68":
                                        if(cap.getTipoCurso() == null || (cap.getTipoCurso() != null && cap.getTipoCurso().getCodProg().equals("TP_TPCUR_TEO"))){
                                            minPAuxTeoria += minutos;
                                        }else{
                                            minPAuxPractica += minutos;
                                        }
                                        break;
                                case "CSA45":
                                case "CSA75":
                                        minAlarmas += minutos; 
                                        break;
                                case "DP68":
                                case "DP61":
                                        if(cap.getTipoCurso() == null || (cap.getTipoCurso() != null && cap.getTipoCurso().getCodProg().equals("TP_TPCUR_TEO"))){
                                            minDefTeoria += minutos;
                                        }else{
                                            minDefPractica += minutos;
                                        }
                                        break;
                            }
                        }
                    }
                }
            }
        }
        int horasAcademicas = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_horasAcademicas").getValor());

        int paramHorasArmasTeoria = 0, paramHorasArmasPractica = 0, 
            paramHorasDefTeoria = 0, paramHorasDefPractica = 0, 
            paramHorasPAuxTeoria = 0, paramHorasPAuxPractica = 0,
            paramHorasRedTeoria = 0, paramHorasRedPractica = 0,
            paramHorasConstitucion = 0, paramHorasLegislacion = 0,
            paramHorasEtica = 0, paramHorasNormas = 0,
            paramHorasControl = 0, paramHorasAtencion = 0,
            paramHorasAlarmas = 0;
        String  nombresHoras = "";
        List<SbParametro> listParametros;

        switch(tipoFormacion.getCodProg()){
            case "TP_FORMC_BAS":
                    nombresHoras = "'SspRegistroCurso_horasConstitucionBasica','SspRegistroCurso_horasLegislacionBasica',"+
                                   "'SspRegistroCurso_horasEticaBasica','SspRegistroCurso_horasNormasBasica','SspRegistroCurso_horasEmergenciaBasica',"+
                                   "'SspRegistroCurso_horasAtencionBasica','SspRegistroCurso_horasAlarmasBasica',"+
                                   "'sspCursos_horasArmasTeoriaBasico','sspCursos_horasArmasPracticaBasico',"+
                                   "'sspCursos_horasRedTeoriaBasico','sspCursos_horasRedPracticaBasico',"+
                                   "'sspCursos_horasPrimAuxTeoriaBasico','sspCursos_horasPrimAuxPracticaBasico',"+
                                   "'sspCursos_horasDefPersTeoriaBasico','sspCursos_horasDefPersPracticaBasico'";
                    listParametros = ejbSbParametroFacade.obtenerListadoParametroXNombre(nombresHoras);
                    paramHorasConstitucion = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasConstitucionBasica");
                    paramHorasLegislacion = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasLegislacionBasica");
                    paramHorasEtica = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasEticaBasica");
                    paramHorasNormas = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasNormasBasica");
                    paramHorasControl = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasEmergenciaBasica");
                    paramHorasAtencion = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasAtencionBasica");
                    paramHorasAlarmas = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasAlarmasBasica");
                    paramHorasArmasTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasArmasTeoriaBasico");
                    paramHorasArmasPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasArmasPracticaBasico");
                    paramHorasDefTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasDefPersTeoriaBasico");
                    paramHorasDefPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasDefPersPracticaBasico");
                    paramHorasPAuxTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasPrimAuxTeoriaBasico");
                    paramHorasPAuxPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasPrimAuxPracticaBasico");
                    paramHorasRedTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasRedTeoriaBasico");
                    paramHorasRedPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasRedPracticaBasico");
                    break;
            case "TP_FORMC_BASEVNT":
                    nombresHoras = "'sspCursos_horasConstitucionBasicaEvento','sspCursos_horasLegislacionBasicaEvento',"+
                                      "'sspCursos_horasEticaBasicaEvento','sspCursos_horasNormaBasicaEvento','sspCursos_horasControlBasicaEvento',"+
                                      "'sspCursos_horasAtencionBasicaEvento','sspCursos_horasConocimientoBasicaEvento',"+
                                      "'sspCursos_horasDefPersTeoriaBasicaEvento','sspCursos_horasDefPersPracticaBasicaEvento',"+
                                      "'sspCursos_horasPrimAuxTeoriaBasicaEvento','sspCursos_horasPrimAuxPracticaBasicaEvento' ";

                    listParametros = ejbSbParametroFacade.obtenerListadoParametroXNombre(nombresHoras);
                    paramHorasConstitucion = 0;//obtenerParametroPorNombre(listParametros,"sspCursos_horasConstitucionBasicaEvento");
                    paramHorasLegislacion = obtenerParametroPorNombre(listParametros,"sspCursos_horasLegislacionBasicaEvento");
                    paramHorasEtica = obtenerParametroPorNombre(listParametros,"sspCursos_horasEticaBasicaEvento");
                    paramHorasNormas = obtenerParametroPorNombre(listParametros,"sspCursos_horasNormaBasicaEvento");
                    paramHorasControl = obtenerParametroPorNombre(listParametros,"sspCursos_horasControlBasicaEvento");
                    paramHorasAtencion = obtenerParametroPorNombre(listParametros,"sspCursos_horasAtencionBasicaEvento");
                    paramHorasAlarmas = obtenerParametroPorNombre(listParametros,"sspCursos_horasConocimientoBasicaEvento");
                    paramHorasArmasTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasArmasTeoriaBasicoEvento"); // 0
                    paramHorasArmasPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasArmasPracticaBasicoEvento"); // 0
                    paramHorasDefTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasDefPersTeoriaBasicaEvento");
                    paramHorasDefPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasDefPersPracticaBasicaEvento");
                    paramHorasPAuxTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasPrimAuxTeoriaBasicaEvento");
                    paramHorasPAuxPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasPrimAuxPracticaBasicaEvento");
                    paramHorasRedTeoria = 0;
                    paramHorasRedPractica = 0;
                    break;
            case "TP_FORMC_PRF":
                    nombresHoras = "'SspRegistroCurso_horasLegislacionPerfecc','SspRegistroCurso_horasEmergenciaPerfecc',"+
                                   "'SspRegistroCurso_horasAtencionPerfecc',"+
                                   "'sspCursos_horasArmasTeoriaPerfecc','sspCursos_horasArmasPracticaPerfecc',"+
                                   "'sspCursos_horasPrimAuxTeoriaPerfecc','sspCursos_horasPrimAuxPracticaPerfecc',"+
                                   "'SspRegistroCurso_horasAlarmasPerfecc',"+
                                   "'sspCursos_horasDefPersTeoriaPerfecc','sspCursos_horasDefPersPracticaPerfecc'";
                    listParametros = ejbSbParametroFacade.obtenerListadoParametroXNombre(nombresHoras);

                    paramHorasLegislacion = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasLegislacionPerfecc");
                    paramHorasControl = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasEmergenciaPerfecc");
                    paramHorasAtencion = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasAtencionPerfecc");
                    paramHorasArmasTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasArmasTeoriaPerfecc");
                    paramHorasArmasPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasArmasPracticaPerfecc");
                    paramHorasPAuxTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasPrimAuxTeoriaPerfecc");
                    paramHorasPAuxPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasPrimAuxPracticaPerfecc");
                    paramHorasDefTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasDefPersTeoriaPerfecc");
                    paramHorasDefPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasDefPersPracticaPerfecc");
                    paramHorasAlarmas = obtenerParametroPorNombre(listParametros,"SspRegistroCurso_horasAlarmasPerfecc");
                    break;
            case "TP_FORMC_PRFEVNT":
                    nombresHoras = "'sspCursos_horasLegislacionPerfeccEvento','sspCursos_horasControlPerfeccEvento',"+
                                   "'sspCursos_horasAtencionPerfeccEvento',"+
                                   "'sspCursos_horasConocimientoPerfeccEvento','sspCursos_horasDefPersTeoriaPerfeccEvento',"+
                                   "'sspCursos_horasDefPersPracticaPerfeccEvento','sspCursos_horasPrimAuxTeoriaPerfeccEvento',"+
                                   "'sspCursos_horasPrimAuxPracticaPerfeccEvento' ";
                    listParametros = ejbSbParametroFacade.obtenerListadoParametroXNombre(nombresHoras);

                    paramHorasLegislacion = 0;//obtenerParametroPorNombre(listParametros,"sspCursos_horasLegislacionPerfeccEvento");
                    paramHorasControl = obtenerParametroPorNombre(listParametros,"sspCursos_horasControlPerfeccEvento");
                    paramHorasAtencion = obtenerParametroPorNombre(listParametros,"sspCursos_horasAtencionPerfeccEvento");
                    paramHorasArmasTeoria = 1;
                    paramHorasArmasPractica = 1;
                    paramHorasPAuxTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasPrimAuxTeoriaPerfeccEvento");
                    paramHorasPAuxPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasPrimAuxPracticaPerfeccEvento");
                    paramHorasDefTeoria = obtenerParametroPorNombre(listParametros,"sspCursos_horasDefPersTeoriaPerfeccEvento");
                    paramHorasDefPractica = obtenerParametroPorNombre(listParametros,"sspCursos_horasDefPersPracticaPerfeccEvento");
                    paramHorasNormas = obtenerParametroPorNombre(listParametros,"sspCursos_horasNormaPerfeccEvento");
                    paramHorasAlarmas = 0;//obtenerParametroPorNombre(listParametros,"sspCursos_horasConocimientoPerfeccEvento");
                    break;
        }

        if(tipoFormacion.getCodProg().equals("TP_FORMC_BAS") || tipoFormacion.getCodProg().equals("TP_FORMC_BASEVNT")){

            int horasBasica = (paramHorasConstitucion + paramHorasLegislacion + paramHorasEtica + paramHorasNormas + paramHorasControl + paramHorasAtencion + paramHorasAlarmas+
                               paramHorasArmasTeoria + paramHorasArmasPractica + paramHorasDefTeoria + paramHorasDefPractica + 
                               paramHorasPAuxTeoria + paramHorasPAuxPractica + paramHorasRedTeoria + paramHorasRedPractica);
            if(sumaHoras < (horasBasica * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con las "+ horasBasica +" horas académicas en los cursos de formación básica" );
            }
            if(minConstitucion < (paramHorasConstitucion * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasConstitucion +" hora(s) académicas del curso " + obtenerNombreModulo("CDH40, CDH50") );
            }
            if(minConstitucion > (paramHorasConstitucion * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasConstitucion +" hora(s) académicas del curso " + obtenerNombreModulo("CDH40, CDH50") );
            }
            if(minLegislacion < (paramHorasLegislacion * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasLegislacion +" hora(s) académicas del curso " + obtenerNombreModulo("LSP60, LSP62") );
            }
            if(minLegislacion > (paramHorasLegislacion * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasLegislacion +" hora(s) académicas del curso " + obtenerNombreModulo("LSP60, LSP62") );
            }
            if(minEtica < (paramHorasEtica * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasEtica +" hora(s) académicas del curso " + obtenerNombreModulo("ESP42, ESP52") );
            }
            if(minEtica > (paramHorasEtica * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasEtica +" hora(s) académicas del curso " + obtenerNombreModulo("ESP42, ESP52") );
            }
            if(minNormas < (paramHorasNormas * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasNormas +" hora(s) académicas del curso " + obtenerNombreModulo("NPS61, NPS71") );
            }
            if(minNormas > (paramHorasNormas * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasNormas +" hora(s) académicas del curso " + obtenerNombreModulo("NPS61, NPS71") );
            }
            if(minControl < (paramHorasControl * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasControl +" hora(s) académicas del curso " + obtenerNombreModulo("CES44, CES54") );
            }
            if(minControl > (paramHorasControl * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasControl +" hora(s) académicas del curso " + obtenerNombreModulo("CES44, CES54") );
            }
            if(minAtencion < (paramHorasAtencion * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasAtencion +" hora(s) académicas del curso " + obtenerNombreModulo("AUP62, AUP72") );
            }
            if(minAtencion > (paramHorasAtencion * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasAtencion +" hora(s) académicas del curso " + obtenerNombreModulo("AUP62, AUP72") );
            }
            if(tipoFormacion.getCodProg().equals("TP_FORMC_BAS")){
                if(minArmaTeoria < (paramHorasArmasTeoria * horasAcademicas) ){
                    validacion = false;
                    JsfUtil.mensajeError("No ha cumplido con "+ paramHorasArmasTeoria +" hora(s) académicas de teoría del curso "+ obtenerNombreModulo("ACM47") );
                }
                if(minArmaTeoria > (paramHorasArmasTeoria * horasAcademicas)){
                    validacion = false;
                    JsfUtil.mensajeError("Se ha excedido de "+ paramHorasArmasTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("ACM47") );
                }
                if(minArmaPractica < (paramHorasArmasPractica * horasAcademicas) ){
                    validacion = false;
                    JsfUtil.mensajeError("No ha cumplido con "+ paramHorasArmasPractica +" hora(s) académicas de práctica del curso "+ obtenerNombreModulo("ACM47") );
                }
                if(minArmaPractica > (paramHorasArmasPractica * horasAcademicas)){
                    validacion = false;
                    JsfUtil.mensajeError("Se ha excedido de "+ paramHorasArmasPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("ACM47") );
                }
                if(minRedTeoria < (paramHorasRedTeoria * horasAcademicas) ){
                    validacion = false;
                    JsfUtil.mensajeError("No ha cumplido con "+ paramHorasRedTeoria +" hora(s) académicas de teoría del curso "+ obtenerNombreModulo("RED63") );
                }
                if(minRedTeoria > (paramHorasRedTeoria * horasAcademicas)){
                    validacion = false;
                    JsfUtil.mensajeError("Se ha excedido de "+ paramHorasRedTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("RED63") );
                }
                if(minRedPractica < (paramHorasRedPractica * horasAcademicas) ){
                    validacion = false;
                    JsfUtil.mensajeError("No ha cumplido con "+ paramHorasRedPractica +" hora(s) académicas de práctica del curso "+ obtenerNombreModulo("RED63") );
                }
                if(minRedPractica > (paramHorasRedPractica * horasAcademicas)){
                    validacion = false;
                    JsfUtil.mensajeError("Se ha excedido de "+ paramHorasRedPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("RED63") );
                }
            }
            if(minPAuxTeoria < (paramHorasPAuxTeoria * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasPAuxTeoria +" hora(s) académicas de teoría del curso "+ obtenerNombreModulo("PA49, PA69") );
            }
            if(minPAuxTeoria > (paramHorasPAuxTeoria * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasPAuxTeoria +" hora(s) académicas de teoría del curso "+ obtenerNombreModulo("PA49, PA69") );
            }
            if(minPAuxPractica < (paramHorasPAuxPractica * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasPAuxPractica +" hora(s) académicas de práctica del curso "+ obtenerNombreModulo("PA49, PA69") );
            }
            if(minPAuxPractica > (paramHorasPAuxPractica * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasPAuxPractica +" hora(s) académicas de práctica del curso "+ obtenerNombreModulo("PA49, PA69") );
            }
            if(minAlarmas < (paramHorasAlarmas * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasAlarmas +" hora(s) académicas del curso " + obtenerNombreModulo("CSA64, CSA74") );
            }
            if(minAlarmas > (paramHorasAlarmas * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasAlarmas +" hora(s) académicas del curso " + obtenerNombreModulo("CSA64, CSA74") );
            }
            if(minDefTeoria < (paramHorasDefTeoria * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasDefTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("DP50, DP60") );
            }
            if(minDefTeoria > (paramHorasDefTeoria * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasDefTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("DP50, DP60") );
            }
            if(minDefPractica < (paramHorasDefPractica * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasDefPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("DP50, DP60") );
            }
            if(minDefPractica > (paramHorasDefPractica * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasDefPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("DP50, DP60") );
            }
        }else{

            int horasPerfecc = (paramHorasLegislacion + paramHorasControl + paramHorasAtencion + paramHorasArmasTeoria + paramHorasArmasPractica +
                                paramHorasPAuxTeoria + paramHorasPAuxPractica + paramHorasDefTeoria + paramHorasDefPractica + paramHorasAlarmas );
            if(sumaHoras < (horasPerfecc * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con las "+ horasPerfecc +" horas académicas en los cursos de perfeccionamiento");
            }                    
            if(minLegislacion < (paramHorasLegislacion * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasLegislacion +" hora(s) académicas del curso " + obtenerNombreModulo("LSP41, LSP61") );
            }
            if(minLegislacion > (paramHorasLegislacion * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasLegislacion +" hora(s) académicas del curso " + obtenerNombreModulo("LSP41, LSP61") );
            }
            if(minControl < (paramHorasControl * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasControl +" hora(s) académicas del curso " + obtenerNombreModulo("CES65, CES55") );
            }
            if(minControl > (paramHorasControl * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasControl +" hora(s) académicas del curso " + obtenerNombreModulo("CES65, CES55") );
            }
            if(minAtencion < (paramHorasAtencion * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasAtencion +" hora(s) académicas del curso " + obtenerNombreModulo("AUP46, AUP71") );
            }
            if(minAtencion > (paramHorasAtencion * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasAtencion +" hora(s) académicas del curso " + obtenerNombreModulo("AUP46, AUP71") );
            }
            if(minNormas < (paramHorasNormas * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasNormas +" hora(s) académicas del curso " + obtenerNombreModulo("NPS72") );
            }
            if(minNormas > (paramHorasNormas * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasNormas +" hora(s) académicas del curso " + obtenerNombreModulo("NPS72") );
            }
            if(tipoFormacion.getCodProg().equals("TP_FORMC_PRF")){
                if(minArmaTeoria < (paramHorasArmasTeoria * horasAcademicas) ){
                    validacion = false;
                    JsfUtil.mensajeError("No ha cumplido con "+ paramHorasArmasTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("ACM66") );
                }
                if(minArmaTeoria > (paramHorasArmasTeoria * horasAcademicas) ){
                    validacion = false;
                    JsfUtil.mensajeError("Se ha excedido de "+ paramHorasArmasTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("ACM66") );
                }
                if(minArmaPractica < (paramHorasArmasPractica * horasAcademicas) ){
                    validacion = false;
                    JsfUtil.mensajeError("No ha cumplido con "+ paramHorasArmasPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("ACM66") );
                }
                if(minArmaPractica > (paramHorasArmasPractica * horasAcademicas) ){
                    validacion = false;
                    JsfUtil.mensajeError("Se ha excedido de "+ paramHorasArmasPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("ACM66") );
                }
            }
            if(minPAuxTeoria < (paramHorasPAuxTeoria * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasPAuxTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("PA67, PA68") );
            }
            if(minPAuxTeoria > (paramHorasPAuxTeoria * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasPAuxTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("PA67, PA68") );
            }
            if(minPAuxPractica < (paramHorasPAuxPractica * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasPAuxPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("PA67, PA68") );
            }
            if(minPAuxPractica > (paramHorasPAuxPractica * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasPAuxPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("PA67, PA68") );
            }
            if(minAlarmas < (paramHorasAlarmas * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasAlarmas +" hora(s) académicas del curso " + obtenerNombreModulo("CSA45, CSA75") );
            }
            if(minAlarmas > (paramHorasAlarmas * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasAlarmas +" hora(s) académicas del curso " + obtenerNombreModulo("CSA45, CSA75") );
            }
            if(minDefTeoria < (paramHorasDefTeoria * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasDefTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("DP68, DP61") );
            }
            if(minDefTeoria > (paramHorasDefTeoria * horasAcademicas)){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasDefTeoria +" hora(s) académicas de teoría del curso " + obtenerNombreModulo("DP68, DP61") );
            }
            if(minDefPractica < (paramHorasDefPractica * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("No ha cumplido con "+ paramHorasDefPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("DP68, DP61") );
            }
            if(minDefPractica > (paramHorasDefPractica * horasAcademicas) ){
                validacion = false;
                JsfUtil.mensajeError("Se ha excedido de "+ paramHorasDefPractica +" hora(s) académicas de práctica del curso " + obtenerNombreModulo("DP68, DP61") );
            }
        }                
        return validacion;
    }
    // AGREGADO MILTON JHOAN ABRIL 2022
    public List<TipoSeguridad> getLstTipoServicio(){
        
        return ejbTipoSeguridadFacade.tipoServicioXCodigosProgs();
    }
    
    public void ActivarCmbForma(){
        //JsfUtil.mensajeAdvertencia("entro al combo");
        lstModalidades = getLstTipoFormacion();
        
    }
    
    public void ActivarCmbFormaList(){
        
        //JsfUtil.mensajeAdvertencia("entro al combo");
        getLstTipoFormacionListado();
    }
    
}

// previoCreate.xhtml, Line 33
//rendered="#{sspRegistroCursoController.tieneResolucionVigilanciaPrivada()}"