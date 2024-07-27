package pe.gob.sucamec.bdintegrado.jsf;

import java.io.File;
import pe.gob.sucamec.bdintegrado.data.SspCarteraCliente;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
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
import java.util.Map;
import java.util.Objects;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtil;
import pe.gob.sucamec.bdintegrado.bean.SspCarteraClienteFacade;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbMedioContactoGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspCarne;
import pe.gob.sucamec.bdintegrado.data.SspCarteraArma;
import pe.gob.sucamec.bdintegrado.data.SspCarteraEvento;
import pe.gob.sucamec.bdintegrado.data.SspCarteraVehiculo;
import pe.gob.sucamec.bdintegrado.data.SspCarteraVigilante;
import pe.gob.sucamec.bdintegrado.data.SspDocumento;
import pe.gob.sucamec.bdintegrado.data.SspLugarServicio;
import pe.gob.sucamec.bdintegrado.data.SspVehiculo;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.UploadFilesController;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

// Nombre de la instancia en la aplicacion //
@Named("sspCarteraClienteController")
@SessionScoped

/**
 * Clase con SspCarteraClienteController instanciada como
 * sspCarteraClienteController. Contiene funciones utiles para la entidad
 * SspCarteraCliente. Esta vinculada a las páginas
 * SspCarteraCliente/create.xhtml, SspCarteraCliente/update.xhtml,
 * SspCarteraCliente/list.xhtml, SspCarteraCliente/view.xhtml Nota: Las tablas
 * deben tener la estructura de Sucamec para que funcione adecuadamente, revisar
 * si tiene el campo activo y modificar las búsquedas.
 */
public class SspCarteraClienteController implements Serializable {

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
    SspCarteraCliente registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SspCarteraCliente> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SspCarteraCliente> registrosSeleccionados;    
    ////
    private List<Map> resultadosBandeja;
    private List<Map> resultadosBandejaSeleccionados;
    private String tipoBuscarPor;
    private TipoSeguridad estadoCartera;
    private TipoSeguridad tipoModalidad;
    private TipoSeguridad tipoDeclaracion;
    private Date fechaInicial;
    private Date fechaFinal;
    private String tipoDoc;
    private String numDoc;
    private int maxLength;
    private List<Map> lstTipoDoc;
    private String rznSocial;
    private String direccionRuc;
    private SbDistritoGt ubigeoRuc;
    private String direccionDni;
    private SbDistritoGt ubigeoDni;
    private String correo;
    private String telefono;
    private boolean disabledDireccion;
    private boolean disabledCorreo;
    private boolean disabledTelefono;
    private List<SbPersonaGt> lstRepresentante;
    private List<SspCarteraEvento> lstObservaciones;
    private boolean disabledBusco;
    private boolean blnBusco;
    private boolean esNuevo;
    private boolean disabledBtnTransmitir;
    private boolean esTransporteDinero;
    private boolean renderPnlPersona;
    private boolean renderPnlRepLegal;
    private boolean renderPnlArmas;
    private boolean renderPnlVehiculos;
    private boolean renderPnlVigilantes;    
    private List<TipoSeguridad> lstModalidades;
    private MapModel modelMap;
    private boolean renderRepresentante;
    private boolean renderDireccionRucCombo;
    private boolean renderDireccionDniCombo;
    private SbDireccionGt direccionRucCombo;
    private SbDireccionGt direccionDniCombo;
    private List<SbDireccionGt> lstDireccionCombo;
    private String direccionRepLegalTemp;
    private String telefonoTemp;
    private String correoTemp;
    private boolean esDeclaracionCC;
    private int tipoEliminacionDeclaracion; // 1=Personal Seguridad, 2=Armas, 3=Vehiculos
    private Date fechaFinDeclaracionTemp;
    private String strObservacion;
    private boolean renderObservacionFechaFin;
    private boolean blnEvento;
    private boolean renderEvento;
    private Date horaInicioEvento;
    private Date horaFinEvento;
    private boolean esMigracion;
    private Boolean vigEventos;
    private String usuarioArchivar;
    // Consorcio
    private SbPersonaGt consorcio;
    private String tipoDocConsorcio;
    private String numDocConsorcio;
    private List<Map> lstTipoDocConsorcio;
    private boolean disabledBuscoConsorcio;
    private boolean blnBuscoConsorcio;
    private boolean esNuevoConsorcio;
    private int maxLengthConsorcio;
    private String rznSocialConsorcio;
    private String direccionRucConsorcio;
    private SbDistritoGt ubigeoRucConsorcio;    
    // Rep. Legal
    private String tipoDocRepLegal;
    private String numDocRepLegal;
    private int maxLengthRepLegal;
    private String correoRepLegal;
    private String telefonoRepLegal;
    private boolean blnBuscoRepLegal;
    private boolean disabledBuscoRepLegal;
    private boolean esNuevoRepLegal;
    private boolean disabledDireccionRepLegal;
    private boolean disabledCorreoRepLegal;
    private boolean disabledTelefonoRepLegal;
    private String direccionRepLegal;
    private SbDistritoGt ubigeoRepLegal;
    private String telefonoRepLegalTemp;
    private String correoRepLegalTemp;
    /// Datos del servicio
    private SspLugarServicio lugarServicio;
    private String mapCenter;
    private SspLugarServicio lugarSelected;
    private List<SspLugarServicio> lstServicioSeleccionados;
    private String latitud;
    private String longitud;
    private SspDocumento documento;
    private byte[] fotoByte;
    private UploadedFile file;
    private StreamedContent foto;
    private String archivo;
    private String archivoTemporal;
    private String direccionServicio;
    private Date fechaInicioServicio;
    /// Personal de Seguridad Privada
    private String tipoDocSeguridad;
    private String numDocSeguridad;
    private String nombresSeguridad;
    private String nroCarneSeguridad;
    private Long carneIdSeguridad;
    private String motivoSeguridad;
    private List<Map> listadoBusquedaPersonalSeguridad;
    private String tipoDocSeguridadBusq;
    private String numDocSeguridadBusq;
    private List<Map> lstPersonalSeguridad;
    private List<Map> lstPersonalSeguridadSeleccionados;
    private List<Map> lstPersonalSeguridadMotivosEliminados;
    ///Armas
    private String tipoDocArmas;
    private String documentoArma;
    private String motivoArma;
    private AmaTarjetaPropiedad tarjetaPropiedad;
    private List<SspCarteraArma> lstArmasSeleccionadas;
    private List<Map> lstArmasMotivosEliminadas;
    /// Vehiculos
    private SspVehiculo vehiculo;
    private List<SspCarteraVehiculo> lstVehiculosSeleccionados;
    private String nroPlaca;
    private String motivoVehiculo;
    private List<Map> lstVehiculosMotivosEliminados;
    /// Termino de contrato
    private boolean esReferenciada;
    private String archivoTermino;
    private List<SspCarteraCliente> lstContratosBusqueda;
    /// Adenda de contrato
    private String archivoAdenda;
    private List<TipoSeguridad> lstModalidadesReferenciado;
    private String tipoDocReferenciado;
    private String numDocReferenciado;
    private List<Map> lstTipoDocReferenciado;
    private String rznSocialReferenciado;
    private String direccionRucReferenciado;
    private SbDistritoGt ubigeoRucReferenciado;
    private String direccionDniReferenciado;
    private SbDistritoGt ubigeoDniReferenciado;
    private String correoReferenciado;
    private String telefonoReferenciado;
    private boolean disabledDireccionReferenciado;
    private boolean disabledCorreoReferenciado;
    private boolean disabledTelefonoReferenciado;
    private List<SbPersonaGt> lstRepresentanteReferenciado;
    private boolean blnBuscoReferenciado;
    private boolean esNuevoReferenciado;
    private boolean disabledBtnTransmitirReferenciado;
    private boolean renderPnlPersonaReferenciado;
    private boolean renderPnlRepLegalReferenciado;
    private boolean renderPnlArmasReferenciado;
    private boolean renderPnlVehiculosReferenciado;
    private boolean renderPnlVigilantesReferenciado;
    private String tipoDocRepLegalReferenciado;
    private String numDocRepLegalReferenciado;    
    private String correoRepLegalReferenciado;
    private String telefonoRepLegalReferenciado;
    private boolean blnBuscoRepLegalReferenciado;
    private boolean esNuevoRepLegalReferenciado;
    private boolean disabledDireccionRepLegalReferenciado;
    private boolean disabledCorreoRepLegalReferenciado;
    private boolean disabledTelefonoRepLegalReferenciado;
    private String direccionRepLegalReferenciado;
    private SbDistritoGt ubigeoRepLegalReferenciado;
    private SspLugarServicio lugarServicioReferenciado;
    private String mapCenterReferenciado;
    private SspLugarServicio lugarSelectedReferenciado;
    private String direccionServicioReferenciado;
    private Date fechaInicioServicioReferenciado;
    private String latitudReferenciado;
    private String longitudReferenciado;
    private SspDocumento documentoReferenciado;
    private byte[] fotoByteReferenciado;
    private UploadedFile fileReferenciado;
    private StreamedContent fotoReferenciado;
    private String archivoReferenciado;
    private List<Map> lstPersonalSeguridadReferenciado;    
    private String strObservacionReferenciado;    
    private boolean blnEventoReferenciado;
    private boolean renderEventoReferenciado;
    private String tipoProc;
    private SbDistritoGt ubigeoAdenda;
    private String referencia;
    private boolean esTransporteDineroReferenciado;
    private boolean flagPnlArmasImpresion;
    private boolean flagPnlVigilantesImpresion;
    private boolean flagPnlVehiculosImpresion;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCarteraClienteFacade ejbSspCarteraClienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade ejbTipoSeguridadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt ejbSbDistritoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade ejbAmaTarjetaPropiedadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt ejbSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPaisFacadeGt ejbSbPaisFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspVehiculoFacade ejbSspVehiculoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaLicenciaDeUsoFacade ejbAmaLicenciaDeUsoFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.CarteraClienteFacade ejbCarteraClienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCarneFacade ejbSspCarneFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroFacade ejbSspRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade ejbSbDerivacionCydocFacade;
    @Inject
    WsPide wsPideController;
    @Inject
    WsTramDoc wsTramDocController;
    @Inject
    UploadFilesController uploadFilesController;

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
     * Propiedad de seleccion multiple
     *
     * @return
     */
    public List<SspCarteraCliente> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SspCarteraCliente> a) {
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
            for (SspCarteraCliente a : registrosSeleccionados) {
                a.setActivo((short) 0);
                ejbSspCarteraClienteFacade.edit(a);
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
            for (SspCarteraCliente a : registrosSeleccionados) {
                a.setActivo((short) 1);
                ejbSspCarteraClienteFacade.edit(a);
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
            registro = (SspCarteraCliente) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbSspCarteraClienteFacade.edit(registro);
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
            registro = (SspCarteraCliente) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbSspCarteraClienteFacade.edit(registro);
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
    public SspCarteraCliente getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SspCarteraCliente registro) {
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
    public ListDataModel<SspCarteraCliente> getResultados() {
        return resultados;
    }

    public String getTipoBuscarPor() {
        return tipoBuscarPor;
    }

    public void setTipoBuscarPor(String tipoBuscarPor) {
        this.tipoBuscarPor = tipoBuscarPor;
    }

    public TipoSeguridad getEstadoCartera() {
        return estadoCartera;
    }

    public void setEstadoCartera(TipoSeguridad estadoCartera) {
        this.estadoCartera = estadoCartera;
    }

    public TipoSeguridad getTipoModalidad() {
        return tipoModalidad;
    }

    public void setTipoModalidad(TipoSeguridad tipoModalidad) {
        this.tipoModalidad = tipoModalidad;
    }

    public TipoSeguridad getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(TipoSeguridad tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public SspCarteraClienteFacade getSspCarteraClienteFacade() {
        return ejbSspCarteraClienteFacade;
    }

    public void setSspCarteraClienteFacade(SspCarteraClienteFacade sspCarteraClienteFacade) {
        this.ejbSspCarteraClienteFacade = sspCarteraClienteFacade;
    }

    public List<Map> getResultadosBandeja() {
        return resultadosBandeja;
    }

    public void setResultadosBandeja(List<Map> resultadosBandeja) {
        this.resultadosBandeja = resultadosBandeja;
    }

    public List<Map> getResultadosBandejaSeleccionados() {
        return resultadosBandejaSeleccionados;
    }

    public void setResultadosBandejaSeleccionados(List<Map> resultadosBandejaSeleccionados) {
        this.resultadosBandejaSeleccionados = resultadosBandejaSeleccionados;
    }

    public boolean isDisabledBtnTransmitir() {
        return disabledBtnTransmitir;
    }

    public void setDisabledBtnTransmitir(boolean disabledBtnTransmitir) {
        this.disabledBtnTransmitir = disabledBtnTransmitir;
    }

    public boolean isRenderPnlPersona() {
        return renderPnlPersona;
    }

    public void setRenderPnlPersona(boolean renderPnlPersona) {
        this.renderPnlPersona = renderPnlPersona;
    }

    public boolean isRenderPnlRepLegal() {
        return renderPnlRepLegal;
    }

    public void setRenderPnlRepLegal(boolean renderPnlRepLegal) {
        this.renderPnlRepLegal = renderPnlRepLegal;
    }

    public boolean isRenderPnlArmas() {
        return renderPnlArmas;
    }

    public void setRenderPnlArmas(boolean renderPnlArmas) {
        this.renderPnlArmas = renderPnlArmas;
    }

    public boolean isRenderPnlVehiculos() {
        return renderPnlVehiculos;
    }

    public void setRenderPnlVehiculos(boolean renderPnlVehiculos) {
        this.renderPnlVehiculos = renderPnlVehiculos;
    }

    public boolean isRenderPnlVigilantes() {
        return renderPnlVigilantes;
    }

    public void setRenderPnlVigilantes(boolean renderPnlVigilantes) {
        this.renderPnlVigilantes = renderPnlVigilantes;
    }

    public List<TipoSeguridad> getLstModalidades() {
        return lstModalidades;
    }

    public void setLstModalidades(List<TipoSeguridad> lstModalidades) {
        this.lstModalidades = lstModalidades;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public List<Map> getLstTipoDoc() {
        return lstTipoDoc;
    }

    public void setLstTipoDoc(List<Map> lstTipoDoc) {
        this.lstTipoDoc = lstTipoDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRznSocial() {
        return rznSocial;
    }

    public void setRznSocial(String rznSocial) {
        this.rznSocial = rznSocial;
    }

    public boolean isBlnBusco() {
        return blnBusco;
    }

    public void setBlnBusco(boolean blnBusco) {
        this.blnBusco = blnBusco;
    }

    public boolean isEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(boolean esNuevo) {
        this.esNuevo = esNuevo;
    }

    public String getTipoDocRepLegal() {
        return tipoDocRepLegal;
    }

    public void setTipoDocRepLegal(String tipoDocRepLegal) {
        this.tipoDocRepLegal = tipoDocRepLegal;
    }

    public String getNumDocRepLegal() {
        return numDocRepLegal;
    }

    public void setNumDocRepLegal(String numDocRepLegal) {
        this.numDocRepLegal = numDocRepLegal;
    }

    public int getMaxLengthRepLegal() {
        return maxLengthRepLegal;
    }

    public void setMaxLengthRepLegal(int maxLengthRepLegal) {
        this.maxLengthRepLegal = maxLengthRepLegal;
    }

    public String getDireccionRepLegal() {
        return direccionRepLegal;
    }

    public void setDireccionRepLegal(String direccionRepLegal) {
        this.direccionRepLegal = direccionRepLegal;
    }

    public String getCorreoRepLegal() {
        return correoRepLegal;
    }

    public void setCorreoRepLegal(String correoRepLegal) {
        this.correoRepLegal = correoRepLegal;
    }

    public String getTelefonoRepLegal() {
        return telefonoRepLegal;
    }

    public void setTelefonoRepLegal(String telefonoRepLegal) {
        this.telefonoRepLegal = telefonoRepLegal;
    }

    public boolean isBlnBuscoRepLegal() {
        return blnBuscoRepLegal;
    }

    public void setBlnBuscoRepLegal(boolean blnBuscoRepLegal) {
        this.blnBuscoRepLegal = blnBuscoRepLegal;
    }

    public boolean isEsNuevoRepLegal() {
        return esNuevoRepLegal;
    }

    public void setEsNuevoRepLegal(boolean esNuevoRepLegal) {
        this.esNuevoRepLegal = esNuevoRepLegal;
    }

    public SspLugarServicio getLugarServicio() {
        return lugarServicio;
    }

    public void setLugarServicio(SspLugarServicio lugarServicio) {
        this.lugarServicio = lugarServicio;
    }

    public String getMapCenter() {
        return mapCenter;
    }

    public void setMapCenter(String mapCenter) {
        this.mapCenter = mapCenter;
    }

    public List<SspLugarServicio> getLstServicioSeleccionados() {
        return lstServicioSeleccionados;
    }

    public void setLstServicioSeleccionados(List<SspLugarServicio> lstServicioSeleccionados) {
        this.lstServicioSeleccionados = lstServicioSeleccionados;
    }

    public SspLugarServicio getLugarSelected() {
        return lugarSelected;
    }

    public void setLugarSelected(SspLugarServicio lugarSelected) {
        this.lugarSelected = lugarSelected;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public SspDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(SspDocumento documento) {
        this.documento = documento;
    }

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getFoto() {
        return foto;
    }

    public void setFoto(StreamedContent foto) {
        this.foto = foto;
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

    public String getTipoDocSeguridad() {
        return tipoDocSeguridad;
    }

    public void setTipoDocSeguridad(String tipoDocSeguridad) {
        this.tipoDocSeguridad = tipoDocSeguridad;
    }

    public String getNumDocSeguridad() {
        return numDocSeguridad;
    }

    public void setNumDocSeguridad(String numDocSeguridad) {
        this.numDocSeguridad = numDocSeguridad;
    }

    public String getNombresSeguridad() {
        return nombresSeguridad;
    }

    public void setNombresSeguridad(String nombresSeguridad) {
        this.nombresSeguridad = nombresSeguridad;
    }

    public String getNroCarneSeguridad() {
        return nroCarneSeguridad;
    }

    public void setNroCarneSeguridad(String nroCarneSeguridad) {
        this.nroCarneSeguridad = nroCarneSeguridad;
    }

    public List<Map> getLstPersonalSeguridad() {
        return lstPersonalSeguridad;
    }

    public void setLstPersonalSeguridad(List<Map> lstPersonalSeguridad) {
        this.lstPersonalSeguridad = lstPersonalSeguridad;
    }

    public String getTipoDocSeguridadBusq() {
        return tipoDocSeguridadBusq;
    }

    public void setTipoDocSeguridadBusq(String tipoDocSeguridadBusq) {
        this.tipoDocSeguridadBusq = tipoDocSeguridadBusq;
    }

    public String getNumDocSeguridadBusq() {
        return numDocSeguridadBusq;
    }

    public void setNumDocSeguridadBusq(String numDocSeguridadBusq) {
        this.numDocSeguridadBusq = numDocSeguridadBusq;
    }

    public List<Map> getListadoBusquedaPersonalSeguridad() {
        return listadoBusquedaPersonalSeguridad;
    }

    public void setListadoBusquedaPersonalSeguridad(List<Map> listadoBusquedaPersonalSeguridad) {
        this.listadoBusquedaPersonalSeguridad = listadoBusquedaPersonalSeguridad;
    }

    public List<Map> getLstPersonalSeguridadSeleccionados() {
        return lstPersonalSeguridadSeleccionados;
    }

    public void setLstPersonalSeguridadSeleccionados(List<Map> lstPersonalSeguridadSeleccionados) {
        this.lstPersonalSeguridadSeleccionados = lstPersonalSeguridadSeleccionados;
    }

    public String getTipoDocArmas() {
        return tipoDocArmas;
    }

    public void setTipoDocArmas(String tipoDocArmas) {
        this.tipoDocArmas = tipoDocArmas;
    }

    public String getDocumentoArma() {
        return documentoArma;
    }

    public void setDocumentoArma(String documentoArma) {
        this.documentoArma = documentoArma;
    }

    public AmaTarjetaPropiedad getTarjetaPropiedad() {
        return tarjetaPropiedad;
    }

    public void setTarjetaPropiedad(AmaTarjetaPropiedad tarjetaPropiedad) {
        this.tarjetaPropiedad = tarjetaPropiedad;
    }

    public List<SspCarteraArma> getLstArmasSeleccionadas() {
        return lstArmasSeleccionadas;
    }

    public void setLstArmasSeleccionadas(List<SspCarteraArma> lstArmasSeleccionadas) {
        this.lstArmasSeleccionadas = lstArmasSeleccionadas;
    }

    public SspVehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(SspVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<SspCarteraVehiculo> getLstVehiculosSeleccionados() {
        return lstVehiculosSeleccionados;
    }

    public void setLstVehiculosSeleccionados(List<SspCarteraVehiculo> lstVehiculosSeleccionados) {
        this.lstVehiculosSeleccionados = lstVehiculosSeleccionados;
    }

    public String getDireccionRuc() {
        return direccionRuc;
    }

    public void setDireccionRuc(String direccionRuc) {
        this.direccionRuc = direccionRuc;
    }

    public SbDistritoGt getUbigeoRuc() {
        return ubigeoRuc;
    }

    public void setUbigeoRuc(SbDistritoGt ubigeoRuc) {
        this.ubigeoRuc = ubigeoRuc;
    }

    public String getDireccionDni() {
        return direccionDni;
    }

    public void setDireccionDni(String direccionDni) {
        this.direccionDni = direccionDni;
    }

    public SbDistritoGt getUbigeoDni() {
        return ubigeoDni;
    }

    public void setUbigeoDni(SbDistritoGt ubigeoDni) {
        this.ubigeoDni = ubigeoDni;
    }

    public boolean isDisabledDireccion() {
        return disabledDireccion;
    }

    public void setDisabledDireccion(boolean disabledDireccion) {
        this.disabledDireccion = disabledDireccion;
    }

    public boolean isDisabledCorreo() {
        return disabledCorreo;
    }

    public void setDisabledCorreo(boolean disabledCorreo) {
        this.disabledCorreo = disabledCorreo;
    }

    public boolean isDisabledTelefono() {
        return disabledTelefono;
    }

    public void setDisabledTelefono(boolean disabledTelefono) {
        this.disabledTelefono = disabledTelefono;
    }

    public boolean isDisabledDireccionRepLegal() {
        return disabledDireccionRepLegal;
    }

    public void setDisabledDireccionRepLegal(boolean disabledDireccionRepLegal) {
        this.disabledDireccionRepLegal = disabledDireccionRepLegal;
    }

    public boolean isDisabledCorreoRepLegal() {
        return disabledCorreoRepLegal;
    }

    public void setDisabledCorreoRepLegal(boolean disabledCorreoRepLegal) {
        this.disabledCorreoRepLegal = disabledCorreoRepLegal;
    }

    public boolean isDisabledTelefonoRepLegal() {
        return disabledTelefonoRepLegal;
    }

    public void setDisabledTelefonoRepLegal(boolean disabledTelefonoRepLegal) {
        this.disabledTelefonoRepLegal = disabledTelefonoRepLegal;
    }

    public SbDistritoGt getUbigeoRepLegal() {
        return ubigeoRepLegal;
    }

    public void setUbigeoRepLegal(SbDistritoGt ubigeoRepLegal) {
        this.ubigeoRepLegal = ubigeoRepLegal;
    }

    public MapModel getModelMap() {
        return modelMap;
    }

    public void setModelMap(MapModel modelMap) {
        this.modelMap = modelMap;
    }

    public String getDireccionServicio() {
        return direccionServicio;
    }

    public void setDireccionServicio(String direccionServicio) {
        this.direccionServicio = direccionServicio;
    }

    public Date getFechaInicioServicio() {
        return fechaInicioServicio;
    }

    public void setFechaInicioServicio(Date fechaInicioServicio) {
        this.fechaInicioServicio = fechaInicioServicio;
    }

    public String getNroPlaca() {
        return nroPlaca;
    }

    public void setNroPlaca(String nroPlaca) {
        this.nroPlaca = nroPlaca;
    }

    public List<SbPersonaGt> getLstRepresentante() {
        return lstRepresentante;
    }

    public void setLstRepresentante(List<SbPersonaGt> lstRepresentante) {
        this.lstRepresentante = lstRepresentante;
    }

    public List<SspCarteraEvento> getLstObservaciones() {
        return lstObservaciones;
    }

    public void setLstObservaciones(List<SspCarteraEvento> lstObservaciones) {
        this.lstObservaciones = lstObservaciones;
    }

    public boolean isEsReferenciada() {
        return esReferenciada;
    }

    public void setEsReferenciada(boolean esReferenciada) {
        this.esReferenciada = esReferenciada;
    }

    public String getArchivoTermino() {
        return archivoTermino;
    }

    public void setArchivoTermino(String archivoTermino) {
        this.archivoTermino = archivoTermino;
    }

    public List<SspCarteraCliente> getLstContratosBusqueda() {
        return lstContratosBusqueda;
    }

    public void setLstContratosBusqueda(List<SspCarteraCliente> lstContratosBusqueda) {
        this.lstContratosBusqueda = lstContratosBusqueda;
    }

    public String getArchivoAdenda() {
        return archivoAdenda;
    }

    public void setArchivoAdenda(String archivoAdenda) {
        this.archivoAdenda = archivoAdenda;
    }
    
    //// RG Referenciada ////
    public List<TipoSeguridad> getLstModalidadesReferenciado() {
        return lstModalidadesReferenciado;
    }

    public void setLstModalidadesReferenciado(List<TipoSeguridad> lstModalidadesReferenciado) {
        this.lstModalidadesReferenciado = lstModalidadesReferenciado;
    }

    public String getTipoDocReferenciado() {
        return tipoDocReferenciado;
    }

    public void setTipoDocReferenciado(String tipoDocReferenciado) {
        this.tipoDocReferenciado = tipoDocReferenciado;
    }

    public String getNumDocReferenciado() {
        return numDocReferenciado;
    }

    public void setNumDocReferenciado(String numDocReferenciado) {
        this.numDocReferenciado = numDocReferenciado;
    }

    public List<Map> getLstTipoDocReferenciado() {
        return lstTipoDocReferenciado;
    }

    public void setLstTipoDocReferenciado(List<Map> lstTipoDocReferenciado) {
        this.lstTipoDocReferenciado = lstTipoDocReferenciado;
    }

    public String getRznSocialReferenciado() {
        return rznSocialReferenciado;
    }

    public void setRznSocialReferenciado(String rznSocialReferenciado) {
        this.rznSocialReferenciado = rznSocialReferenciado;
    }

    public String getDireccionRucReferenciado() {
        return direccionRucReferenciado;
    }

    public void setDireccionRucReferenciado(String direccionRucReferenciado) {
        this.direccionRucReferenciado = direccionRucReferenciado;
    }

    public SbDistritoGt getUbigeoRucReferenciado() {
        return ubigeoRucReferenciado;
    }

    public void setUbigeoRucReferenciado(SbDistritoGt ubigeoRucReferenciado) {
        this.ubigeoRucReferenciado = ubigeoRucReferenciado;
    }

    public String getDireccionDniReferenciado() {
        return direccionDniReferenciado;
    }

    public void setDireccionDniReferenciado(String direccionDniReferenciado) {
        this.direccionDniReferenciado = direccionDniReferenciado;
    }

    public SbDistritoGt getUbigeoDniReferenciado() {
        return ubigeoDniReferenciado;
    }

    public void setUbigeoDniReferenciado(SbDistritoGt ubigeoDniReferenciado) {
        this.ubigeoDniReferenciado = ubigeoDniReferenciado;
    }

    public String getCorreoReferenciado() {
        return correoReferenciado;
    }

    public void setCorreoReferenciado(String correoReferenciado) {
        this.correoReferenciado = correoReferenciado;
    }

    public String getTelefonoReferenciado() {
        return telefonoReferenciado;
    }

    public void setTelefonoReferenciado(String telefonoReferenciado) {
        this.telefonoReferenciado = telefonoReferenciado;
    }

    public boolean isDisabledDireccionReferenciado() {
        return disabledDireccionReferenciado;
    }

    public void setDisabledDireccionReferenciado(boolean disabledDireccionReferenciado) {
        this.disabledDireccionReferenciado = disabledDireccionReferenciado;
    }

    public boolean isDisabledCorreoReferenciado() {
        return disabledCorreoReferenciado;
    }

    public void setDisabledCorreoReferenciado(boolean disabledCorreoReferenciado) {
        this.disabledCorreoReferenciado = disabledCorreoReferenciado;
    }

    public boolean isDisabledTelefonoReferenciado() {
        return disabledTelefonoReferenciado;
    }

    public void setDisabledTelefonoReferenciado(boolean disabledTelefonoReferenciado) {
        this.disabledTelefonoReferenciado = disabledTelefonoReferenciado;
    }

    public List<SbPersonaGt> getLstRepresentanteReferenciado() {
        return lstRepresentanteReferenciado;
    }

    public void setLstRepresentanteReferenciado(List<SbPersonaGt> lstRepresentanteReferenciado) {
        this.lstRepresentanteReferenciado = lstRepresentanteReferenciado;
    }

    public boolean isBlnBuscoReferenciado() {
        return blnBuscoReferenciado;
    }

    public void setBlnBuscoReferenciado(boolean blnBuscoReferenciado) {
        this.blnBuscoReferenciado = blnBuscoReferenciado;
    }

    public boolean isEsNuevoReferenciado() {
        return esNuevoReferenciado;
    }

    public void setEsNuevoReferenciado(boolean esNuevoReferenciado) {
        this.esNuevoReferenciado = esNuevoReferenciado;
    }

    public boolean isDisabledBtnTransmitirReferenciado() {
        return disabledBtnTransmitirReferenciado;
    }

    public void setDisabledBtnTransmitirReferenciado(boolean disabledBtnTransmitirReferenciado) {
        this.disabledBtnTransmitirReferenciado = disabledBtnTransmitirReferenciado;
    }

    public boolean isRenderPnlPersonaReferenciado() {
        return renderPnlPersonaReferenciado;
    }

    public void setRenderPnlPersonaReferenciado(boolean renderPnlPersonaReferenciado) {
        this.renderPnlPersonaReferenciado = renderPnlPersonaReferenciado;
    }

    public boolean isRenderPnlRepLegalReferenciado() {
        return renderPnlRepLegalReferenciado;
    }

    public void setRenderPnlRepLegalReferenciado(boolean renderPnlRepLegalReferenciado) {
        this.renderPnlRepLegalReferenciado = renderPnlRepLegalReferenciado;
    }

    public boolean isRenderPnlArmasReferenciado() {
        return renderPnlArmasReferenciado;
    }

    public void setRenderPnlArmasReferenciado(boolean renderPnlArmasReferenciado) {
        this.renderPnlArmasReferenciado = renderPnlArmasReferenciado;
    }

    public boolean isRenderPnlVehiculosReferenciado() {
        return renderPnlVehiculosReferenciado;
    }

    public void setRenderPnlVehiculosReferenciado(boolean renderPnlVehiculosReferenciado) {
        this.renderPnlVehiculosReferenciado = renderPnlVehiculosReferenciado;
    }

    public boolean isRenderPnlVigilantesReferenciado() {
        return renderPnlVigilantesReferenciado;
    }

    public void setRenderPnlVigilantesReferenciado(boolean renderPnlVigilantesReferenciado) {
        this.renderPnlVigilantesReferenciado = renderPnlVigilantesReferenciado;
    }

    public String getTipoDocRepLegalReferenciado() {
        return tipoDocRepLegalReferenciado;
    }

    public void setTipoDocRepLegalReferenciado(String tipoDocRepLegalReferenciado) {
        this.tipoDocRepLegalReferenciado = tipoDocRepLegalReferenciado;
    }

    public String getNumDocRepLegalReferenciado() {
        return numDocRepLegalReferenciado;
    }

    public void setNumDocRepLegalReferenciado(String numDocRepLegalReferenciado) {
        this.numDocRepLegalReferenciado = numDocRepLegalReferenciado;
    }

    public String getCorreoRepLegalReferenciado() {
        return correoRepLegalReferenciado;
    }

    public void setCorreoRepLegalReferenciado(String correoRepLegalReferenciado) {
        this.correoRepLegalReferenciado = correoRepLegalReferenciado;
    }

    public String getTelefonoRepLegalReferenciado() {
        return telefonoRepLegalReferenciado;
    }

    public void setTelefonoRepLegalReferenciado(String telefonoRepLegalReferenciado) {
        this.telefonoRepLegalReferenciado = telefonoRepLegalReferenciado;
    }

    public boolean isBlnBuscoRepLegalReferenciado() {
        return blnBuscoRepLegalReferenciado;
    }

    public void setBlnBuscoRepLegalReferenciado(boolean blnBuscoRepLegalReferenciado) {
        this.blnBuscoRepLegalReferenciado = blnBuscoRepLegalReferenciado;
    }

    public boolean isEsNuevoRepLegalReferenciado() {
        return esNuevoRepLegalReferenciado;
    }

    public void setEsNuevoRepLegalReferenciado(boolean esNuevoRepLegalReferenciado) {
        this.esNuevoRepLegalReferenciado = esNuevoRepLegalReferenciado;
    }

    public boolean isDisabledDireccionRepLegalReferenciado() {
        return disabledDireccionRepLegalReferenciado;
    }

    public void setDisabledDireccionRepLegalReferenciado(boolean disabledDireccionRepLegalReferenciado) {
        this.disabledDireccionRepLegalReferenciado = disabledDireccionRepLegalReferenciado;
    }

    public boolean isDisabledCorreoRepLegalReferenciado() {
        return disabledCorreoRepLegalReferenciado;
    }

    public void setDisabledCorreoRepLegalReferenciado(boolean disabledCorreoRepLegalReferenciado) {
        this.disabledCorreoRepLegalReferenciado = disabledCorreoRepLegalReferenciado;
    }

    public boolean isDisabledTelefonoRepLegalReferenciado() {
        return disabledTelefonoRepLegalReferenciado;
    }

    public void setDisabledTelefonoRepLegalReferenciado(boolean disabledTelefonoRepLegalReferenciado) {
        this.disabledTelefonoRepLegalReferenciado = disabledTelefonoRepLegalReferenciado;
    }

    public String getDireccionRepLegalReferenciado() {
        return direccionRepLegalReferenciado;
    }

    public void setDireccionRepLegalReferenciado(String direccionRepLegalReferenciado) {
        this.direccionRepLegalReferenciado = direccionRepLegalReferenciado;
    }

    public SbDistritoGt getUbigeoRepLegalReferenciado() {
        return ubigeoRepLegalReferenciado;
    }

    public void setUbigeoRepLegalReferenciado(SbDistritoGt ubigeoRepLegalReferenciado) {
        this.ubigeoRepLegalReferenciado = ubigeoRepLegalReferenciado;
    }

    public SspLugarServicio getLugarServicioReferenciado() {
        return lugarServicioReferenciado;
    }

    public void setLugarServicioReferenciado(SspLugarServicio lugarServicioReferenciado) {
        this.lugarServicioReferenciado = lugarServicioReferenciado;
    }

    public String getMapCenterReferenciado() {
        return mapCenterReferenciado;
    }

    public void setMapCenterReferenciado(String mapCenterReferenciado) {
        this.mapCenterReferenciado = mapCenterReferenciado;
    }

    public SspLugarServicio getLugarSelectedReferenciado() {
        return lugarSelectedReferenciado;
    }

    public void setLugarSelectedReferenciado(SspLugarServicio lugarSelectedReferenciado) {
        this.lugarSelectedReferenciado = lugarSelectedReferenciado;
    }

    public String getLatitudReferenciado() {
        return latitudReferenciado;
    }

    public void setLatitudReferenciado(String latitudReferenciado) {
        this.latitudReferenciado = latitudReferenciado;
    }

    public String getLongitudReferenciado() {
        return longitudReferenciado;
    }

    public void setLongitudReferenciado(String longitudReferenciado) {
        this.longitudReferenciado = longitudReferenciado;
    }

    public SspDocumento getDocumentoReferenciado() {
        return documentoReferenciado;
    }

    public void setDocumentoReferenciado(SspDocumento documentoReferenciado) {
        this.documentoReferenciado = documentoReferenciado;
    }

    public byte[] getFotoByteReferenciado() {
        return fotoByteReferenciado;
    }

    public void setFotoByteReferenciado(byte[] fotoByteReferenciado) {
        this.fotoByteReferenciado = fotoByteReferenciado;
    }

    public UploadedFile getFileReferenciado() {
        return fileReferenciado;
    }

    public void setFileReferenciado(UploadedFile fileReferenciado) {
        this.fileReferenciado = fileReferenciado;
    }

    public StreamedContent getFotoReferenciado() {
        return fotoReferenciado;
    }

    public void setFotoReferenciado(StreamedContent fotoReferenciado) {
        this.fotoReferenciado = fotoReferenciado;
    }

    public String getArchivoReferenciado() {
        return archivoReferenciado;
    }

    public void setArchivoReferenciado(String archivoReferenciado) {
        this.archivoReferenciado = archivoReferenciado;
    }

    public String getDireccionServicioReferenciado() {
        return direccionServicioReferenciado;
    }

    public void setDireccionServicioReferenciado(String direccionServicioReferenciado) {
        this.direccionServicioReferenciado = direccionServicioReferenciado;
    }

    public Date getFechaInicioServicioReferenciado() {
        return fechaInicioServicioReferenciado;
    }

    public void setFechaInicioServicioReferenciado(Date fechaInicioServicioReferenciado) {
        this.fechaInicioServicioReferenciado = fechaInicioServicioReferenciado;
    }

    public List<Map> getLstPersonalSeguridadReferenciado() {
        return lstPersonalSeguridadReferenciado;
    }

    public void setLstPersonalSeguridadReferenciado(List<Map> lstPersonalSeguridadReferenciado) {
        this.lstPersonalSeguridadReferenciado = lstPersonalSeguridadReferenciado;
    }

    public boolean isDisabledBusco() {
        return disabledBusco;
    }

    public void setDisabledBusco(boolean disabledBusco) {
        this.disabledBusco = disabledBusco;
    }

    public boolean isDisabledBuscoRepLegal() {
        return disabledBuscoRepLegal;
    }

    public void setDisabledBuscoRepLegal(boolean disabledBuscoRepLegal) {
        this.disabledBuscoRepLegal = disabledBuscoRepLegal;
    }

    public boolean isRenderRepresentante() {
        return renderRepresentante;
    }

    public void setRenderRepresentante(boolean renderRepresentante) {
        this.renderRepresentante = renderRepresentante;
    }

    public boolean isRenderDireccionRucCombo() {
        return renderDireccionRucCombo;
    }

    public void setRenderDireccionRucCombo(boolean renderDireccionRucCombo) {
        this.renderDireccionRucCombo = renderDireccionRucCombo;
    }

    public boolean isRenderDireccionDniCombo() {
        return renderDireccionDniCombo;
    }

    public void setRenderDireccionDniCombo(boolean renderDireccionDniCombo) {
        this.renderDireccionDniCombo = renderDireccionDniCombo;
    }

    public SbDireccionGt getDireccionRucCombo() {
        return direccionRucCombo;
    }

    public void setDireccionRucCombo(SbDireccionGt direccionRucCombo) {
        this.direccionRucCombo = direccionRucCombo;
    }

    public SbDireccionGt getDireccionDniCombo() {
        return direccionDniCombo;
    }

    public void setDireccionDniCombo(SbDireccionGt direccionDniCombo) {
        this.direccionDniCombo = direccionDniCombo;
    }

    public List<SbDireccionGt> getLstDireccionCombo() {
        return lstDireccionCombo;
    }

    public void setLstDireccionCombo(List<SbDireccionGt> lstDireccionCombo) {
        this.lstDireccionCombo = lstDireccionCombo;
    }

    public String getDireccionRepLegalTemp() {
        return direccionRepLegalTemp;
    }

    public void setDireccionRepLegalTemp(String direccionRepLegalTemp) {
        this.direccionRepLegalTemp = direccionRepLegalTemp;
    }

    public String getTelefonoTemp() {
        return telefonoTemp;
    }

    public void setTelefonoTemp(String telefonoTemp) {
        this.telefonoTemp = telefonoTemp;
    }

    public String getCorreoTemp() {
        return correoTemp;
    }

    public void setCorreoTemp(String correoTemp) {
        this.correoTemp = correoTemp;
    }

    public String getTelefonoRepLegalTemp() {
        return telefonoRepLegalTemp;
    }

    public void setTelefonoRepLegalTemp(String telefonoRepLegalTemp) {
        this.telefonoRepLegalTemp = telefonoRepLegalTemp;
    }

    public String getCorreoRepLegalTemp() {
        return correoRepLegalTemp;
    }

    public void setCorreoRepLegalTemp(String correoRepLegalTemp) {
        this.correoRepLegalTemp = correoRepLegalTemp;
    }

    public String getMotivoSeguridad() {
        return motivoSeguridad;
    }

    public void setMotivoSeguridad(String motivoSeguridad) {
        this.motivoSeguridad = motivoSeguridad;
    }

    public boolean isEsDeclaracionCC() {
        return esDeclaracionCC;
    }

    public void setEsDeclaracionCC(boolean esDeclaracionCC) {
        this.esDeclaracionCC = esDeclaracionCC;
    }

    public List<Map> getLstPersonalSeguridadMotivosEliminados() {
        return lstPersonalSeguridadMotivosEliminados;
    }

    public void setLstPersonalSeguridadMotivosEliminados(List<Map> lstPersonalSeguridadMotivosEliminados) {
        this.lstPersonalSeguridadMotivosEliminados = lstPersonalSeguridadMotivosEliminados;
    }

    public String getMotivoArma() {
        return motivoArma;
    }

    public void setMotivoArma(String motivoArma) {
        this.motivoArma = motivoArma;
    }

    public List<Map> getLstArmasMotivosEliminadas() {
        return lstArmasMotivosEliminadas;
    }

    public void setLstArmasMotivosEliminadas(List<Map> lstArmasMotivosEliminadas) {
        this.lstArmasMotivosEliminadas = lstArmasMotivosEliminadas;
    }

    public String getMotivoVehiculo() {
        return motivoVehiculo;
    }

    public void setMotivoVehiculo(String motivoVehiculo) {
        this.motivoVehiculo = motivoVehiculo;
    }

    public List<Map> getLstVehiculosMotivosEliminados() {
        return lstVehiculosMotivosEliminados;
    }

    public void setLstVehiculosMotivosEliminados(List<Map> lstVehiculosMotivosEliminados) {
        this.lstVehiculosMotivosEliminados = lstVehiculosMotivosEliminados;
    }

    public String getTipoDocConsorcio() {
        return tipoDocConsorcio;
    }

    public void setTipoDocConsorcio(String tipoDocConsorcio) {
        this.tipoDocConsorcio = tipoDocConsorcio;
    }

    public String getNumDocConsorcio() {
        return numDocConsorcio;
    }

    public void setNumDocConsorcio(String numDocConsorcio) {
        this.numDocConsorcio = numDocConsorcio;
    }

    public List<Map> getLstTipoDocConsorcio() {
        return lstTipoDocConsorcio;
    }

    public void setLstTipoDocConsorcio(List<Map> lstTipoDocConsorcio) {
        this.lstTipoDocConsorcio = lstTipoDocConsorcio;
    }

    public SbPersonaGt getConsorcio() {
        return consorcio;
    }

    public void setConsorcio(SbPersonaGt consorcio) {
        this.consorcio = consorcio;
    }

    public boolean isDisabledBuscoConsorcio() {
        return disabledBuscoConsorcio;
    }

    public void setDisabledBuscoConsorcio(boolean disabledBuscoConsorcio) {
        this.disabledBuscoConsorcio = disabledBuscoConsorcio;
    }

    public boolean isBlnBuscoConsorcio() {
        return blnBuscoConsorcio;
    }

    public void setBlnBuscoConsorcio(boolean blnBuscoConsorcio) {
        this.blnBuscoConsorcio = blnBuscoConsorcio;
    }

    public boolean isEsNuevoConsorcio() {
        return esNuevoConsorcio;
    }

    public void setEsNuevoConsorcio(boolean esNuevoConsorcio) {
        this.esNuevoConsorcio = esNuevoConsorcio;
    }

    public int getMaxLengthConsorcio() {
        return maxLengthConsorcio;
    }

    public void setMaxLengthConsorcio(int maxLengthConsorcio) {
        this.maxLengthConsorcio = maxLengthConsorcio;
    }

    public String getRznSocialConsorcio() {
        return rznSocialConsorcio;
    }

    public void setRznSocialConsorcio(String rznSocialConsorcio) {
        this.rznSocialConsorcio = rznSocialConsorcio;
    }

    public String getDireccionRucConsorcio() {
        return direccionRucConsorcio;
    }

    public void setDireccionRucConsorcio(String direccionRucConsorcio) {
        this.direccionRucConsorcio = direccionRucConsorcio;
    }

    public SbDistritoGt getUbigeoRucConsorcio() {
        return ubigeoRucConsorcio;
    }

    public void setUbigeoRucConsorcio(SbDistritoGt ubigeoRucConsorcio) {
        this.ubigeoRucConsorcio = ubigeoRucConsorcio;
    }

    public String getStrObservacion() {
        return strObservacion;
    }

    public void setStrObservacion(String strObservacion) {
        this.strObservacion = strObservacion;
    }

    public boolean isRenderObservacionFechaFin() {
        return renderObservacionFechaFin;
    }

    public void setRenderObservacionFechaFin(boolean renderObservacionFechaFin) {
        this.renderObservacionFechaFin = renderObservacionFechaFin;
    }

    public Date getFechaFinDeclaracionTemp() {
        return fechaFinDeclaracionTemp;
    }

    public void setFechaFinDeclaracionTemp(Date fechaFinDeclaracionTemp) {
        this.fechaFinDeclaracionTemp = fechaFinDeclaracionTemp;
    }

    public boolean isBlnEvento() {
        return blnEvento;
    }

    public void setBlnEvento(boolean blnEvento) {
        this.blnEvento = blnEvento;
    }

    public boolean isRenderEvento() {
        return renderEvento;
    }

    public void setRenderEvento(boolean renderEvento) {
        this.renderEvento = renderEvento;
    }

    public boolean isRenderEventoReferenciado() {
        return renderEventoReferenciado;
    }

    public void setRenderEventoReferenciado(boolean renderEventoReferenciado) {
        this.renderEventoReferenciado = renderEventoReferenciado;
    }

    public Date getHoraInicioEvento() {
        return horaInicioEvento;
    }

    public void setHoraInicioEvento(Date horaInicioEvento) {
        this.horaInicioEvento = horaInicioEvento;
    }

    public Date getHoraFinEvento() {
        return horaFinEvento;
    }

    public void setHoraFinEvento(Date horaFinEvento) {
        this.horaFinEvento = horaFinEvento;
    }

    public String getTipoProc() {
        return tipoProc;
    }

    public void setTipoProc(String tipoProc) {
        this.tipoProc = tipoProc;
    }

    public boolean isEsMigracion() {
        return esMigracion;
    }

    public void setEsMigracion(boolean esMigracion) {
        this.esMigracion = esMigracion;
    }

    public SbDistritoGt getUbigeoAdenda() {
        return ubigeoAdenda;
    }

    public void setUbigeoAdenda(SbDistritoGt ubigeoAdenda) {
        this.ubigeoAdenda = ubigeoAdenda;
    }

    public String getStrObservacionReferenciado() {
        return strObservacionReferenciado;
    }

    public void setStrObservacionReferenciado(String strObservacionReferenciado) {
        this.strObservacionReferenciado = strObservacionReferenciado;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public boolean isEsTransporteDinero() {
        return esTransporteDinero;
    }

    public void setEsTransporteDinero(boolean esTransporteDinero) {
        this.esTransporteDinero = esTransporteDinero;
    }

    public int getTipoEliminacionDeclaracion() {
        return tipoEliminacionDeclaracion;
    }

    public void setTipoEliminacionDeclaracion(int tipoEliminacionDeclaracion) {
        this.tipoEliminacionDeclaracion = tipoEliminacionDeclaracion;
    }

    public boolean isBlnEventoReferenciado() {
        return blnEventoReferenciado;
    }

    public void setBlnEventoReferenciado(boolean blnEventoReferenciado) {
        this.blnEventoReferenciado = blnEventoReferenciado;
    }

    public boolean isEsTransporteDineroReferenciado() {
        return esTransporteDineroReferenciado;
    }

    public void setEsTransporteDineroReferenciado(boolean esTransporteDineroReferenciado) {
        this.esTransporteDineroReferenciado = esTransporteDineroReferenciado;
    }

    public boolean isFlagPnlArmasImpresion() {
        return flagPnlArmasImpresion;
    }

    public void setFlagPnlArmasImpresion(boolean flagPnlArmasImpresion) {
        this.flagPnlArmasImpresion = flagPnlArmasImpresion;
    }

    public boolean isFlagPnlVigilantesImpresion() {
        return flagPnlVigilantesImpresion;
    }

    public void setFlagPnlVigilantesImpresion(boolean flagPnlVigilantesImpresion) {
        this.flagPnlVigilantesImpresion = flagPnlVigilantesImpresion;
    }

    public boolean isFlagPnlVehiculosImpresion() {
        return flagPnlVehiculosImpresion;
    }

    public void setFlagPnlVehiculosImpresion(boolean flagPnlVehiculosImpresion) {
        this.flagPnlVehiculosImpresion = flagPnlVehiculosImpresion;
    }

    public Long getCarneIdSeguridad() {
        return carneIdSeguridad;
    }

    public void setCarneIdSeguridad(Long carneIdSeguridad) {
        this.carneIdSeguridad = carneIdSeguridad;
    }

    public Boolean isVigEventos() {
        return vigEventos;
    }

    public void setVigEventos(Boolean vigEventos) {
        this.vigEventos = vigEventos;
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
    public List<SspCarteraCliente> getSelectItems() {
        return ejbSspCarteraClienteFacade.findAll();
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
        resultados = new ListDataModel(ejbSspCarteraClienteFacade.selectLike(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     * @param reg Registro a editar
     */
    public void mostrarVer(Map reg) {
        reiniciarValores();
        registro = ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+reg.get("ID")));

        listarModalidadesCarteraCliente(false,registro.getAdministradoId().getRuc());
        eventoSeleccionModalidad(registro,false);
        cargarDatosCarteraCliente(registro);
        
        switch(registro.getTipoOpeId().getCodProg()){
            case "TP_TDEC_NUEVO":
                    cargarArchivo(registro);
                    archivoTemporal = ""+ archivo;
                    estado = EstadoCrud.VER;
                    break;
            case "TP_TDEC_ADENDA":
                    cargarArchivoAdenda(registro);
                    archivoTemporal = ""+ archivoAdenda;
                    estado = EstadoCrud.VER_ADENDA;
                    break;
            case "TP_TDEC_BAJA":
                    cargarArchivo(registro.getContratoId());
                    cargarArchivoTermino(registro);
                    archivoTemporal = ""+ archivoTermino;
                    estado = EstadoCrud.VER_TERMINO;
                    break;
        }
        if(!blnEvento){
            setRenderEvento(false);
        }
        validarRepresentante(registro);
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     * @param reg Registro a editar
     */
    public void mostrarEditar(Map reg)
    {   reiniciarValores();
        registro = ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+reg.get("ID")));
        
        listarModalidadesCarteraCliente(true, registro.getAdministradoId().getRuc());
        eventoSeleccionModalidad(registro,true);
        cargarDatosCarteraCliente(registro);

        switch(registro.getTipoOpeId().getCodProg()){
            case "TP_TDEC_NUEVO":
                    cargarArchivo(registro);
                    archivoTemporal = ""+ archivo;
                    estado = EstadoCrud.EDITAR;
                    break;
            case "TP_TDEC_ADENDA":
                    cargarArchivoAdenda(registro);
                    archivoTemporal = ""+ archivoAdenda;
                    estado = EstadoCrud.EDITAR_ADENDA;
                    break;
            case "TP_TDEC_BAJA":
                    cargarArchivo(registro.getContratoId());
                    cargarArchivoTermino(registro);
                    archivoTemporal = ""+ archivoTermino;
                    estado = EstadoCrud.EDITAR_TERMINO;
                    break;
        }
        validarRepresentante(registro);
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     * @return Página a redireccionar
     */
    public String mostrarCrear() {
        reiniciarValores();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt per = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        estado = EstadoCrud.CREAR;
        registro = new SspCarteraCliente();
        registro.setAdministradoId(per);
        registro.setActivo(JsfUtil.TRUE);
        registro.setClienteId(new SbPersonaGt());
        registro.setReprCliId(new SbPersonaGt());
        registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
        registro.setFecha(new Date());
        registro.setTipoOpeId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TDEC_NUEVO"));
        registro.setSspLugarServicioList(new ArrayList());
        registro.setSspCarteraClienteList(new ArrayList());
        registro.setSspCarteraEventoList(new ArrayList());
        registro.setSspDocumentoList(new ArrayList());
        registro.setSbPersonaList(new ArrayList());
        registro.setSspCarteraVehiculoList(new ArrayList());
        registro.setSspCarteraVigilanteList(new ArrayList<SspCarteraVigilante>());
        registro.setSspCarteraArmaList(new ArrayList());
        registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registro.setAudNumIp(JsfUtil.getIpAddress());
        registro.setTodoRecursos(JsfUtil.FALSE);
        obtenerRepresentantes(registro);
        listarModalidadesCarteraCliente(true, registro.getAdministradoId().getRuc());
        validarRepresentante(registro);
        
        if(lstModalidades.isEmpty()){
            JsfUtil.mensajeError("Ud. no cuenta con modalidades disponibles");
            return null;
        }
        
        return "/aplicacion/gssp/sspCarteraCliente/Create";
    }
    
    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     * @param reg Registro referenciado
     * @return Página a redireccionar
     */
    public String mostrarCrearTermino(Map reg) {
        reiniciarValores();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt per = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        registro = new SspCarteraCliente();
        registro.setContratoId(ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+reg.get("ID"))));
        registro.getContratoId().setFlagEvaluacion('0');
        registro.setAdministradoId(per);
        registro.setReprAdminId(registro.getContratoId().getReprAdminId());
        registro.setSedeId(registro.getContratoId().getSedeId());
        registro.setFechaInicio(registro.getContratoId().getFechaInicio());
        registro.setExpediente(null);
        registro.setActivo(JsfUtil.TRUE);
        registro.setDirecClienId(registro.getContratoId().getDirecClienId());
        registro.setClienteId(registro.getContratoId().getClienteId());
        registro.setReprCliId(registro.getContratoId().getReprCliId());
        registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
        registro.setFecha(new Date());
        registro.setTipoOpeId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TDEC_BAJA"));
        registro.setObservacion(registro.getContratoId().getObservacion());
        registro.setDetalleMotivo(registro.getContratoId().getDetalleMotivo());
        registro.setEvento(registro.getContratoId().getEvento());
        registro.setTipoEvento(registro.getContratoId().getTipoEvento());
        registro.setTipoSeguridadList(new ArrayList());
        registro.setSspLugarServicioList(new ArrayList());
        registro.setSspCarteraClienteList(new ArrayList());
        registro.setSspCarteraEventoList(new ArrayList());
        registro.setSspDocumentoList(new ArrayList());
        registro.setSspCarteraVehiculoList(new ArrayList());
        registro.setSspCarteraVigilanteList(new ArrayList<SspCarteraVigilante>());
        registro.setSspCarteraArmaList(new ArrayList());
        registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registro.setAudNumIp(JsfUtil.getIpAddress());
        listarModalidadesCarteraCliente(true, registro.getAdministradoId().getRuc());
        cargarListasCarteraCliente(registro.getContratoId());
        validarRepresentante(registro);
        eventoSeleccionModalidad(registro,false);
        cargarDatosCarteraCliente(registro);
        cargarArchivo(registro.getContratoId());
        archivoTemporal = "" + archivo;
        
        estado = EstadoCrud.CREAR_TERMINO;
        return "/aplicacion/gssp/sspCarteraCliente/CreateTermino";
    }
    
    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     * @param reg Registro referenciado
     * @return Página a redireccionar
     */
    public String mostrarCrearAdenda(Map reg) {
        reiniciarValores();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt per = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        registro = new SspCarteraCliente();
        registro.setContratoId(ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+reg.get("ID"))));
        if(registro.getContratoId().getFechaFin() == null){
            JsfUtil.mensajeError("No puede generar una adenda porque el contrato no tiene fecha de fin. Actualice la fecha previamente con la opción disponible");
            return null;
        }
        registro.getContratoId().setFlagEvaluacion('1');
        registro.setAdministradoId(per);
        registro.setReprAdminId(registro.getContratoId().getReprAdminId());
        registro.setSedeId(registro.getContratoId().getSedeId());
        registro.setFechaInicio(null);
        registro.setFechaFin(null);
        registro.setExpediente(null);
        registro.setActivo(JsfUtil.TRUE);
        registro.setDirecClienId(registro.getContratoId().getDirecClienId());
        registro.setClienteId(registro.getContratoId().getClienteId());
        registro.setReprCliId(registro.getContratoId().getReprCliId());
        registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
        registro.setFecha(new Date());
        registro.setTipoOpeId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TDEC_ADENDA"));
        registro.setObservacion(registro.getContratoId().getObservacion());
        registro.setDetalleMotivo(registro.getContratoId().getDetalleMotivo());
        registro.setEvento(registro.getContratoId().getEvento());
        registro.setTipoEvento(registro.getContratoId().getTipoEvento());
        registro.setTodoRecursos(registro.getContratoId().getTodoRecursos());
        registro.setTipoSeguridadList(new ArrayList());
        registro.setSspLugarServicioList(new ArrayList());
        registro.setSspCarteraClienteList(new ArrayList());
        registro.setSspCarteraEventoList(new ArrayList());
        registro.setSspDocumentoList(new ArrayList());
        registro.setSspCarteraVehiculoList(new ArrayList());
        registro.setSspCarteraVigilanteList(new ArrayList<SspCarteraVigilante>());
        registro.setSspCarteraArmaList(new ArrayList());
        registro.setSbPersonaList(new ArrayList());
        registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registro.setAudNumIp(JsfUtil.getIpAddress());
        listarModalidadesCarteraCliente(true, registro.getAdministradoId().getRuc());
        cargarListasCarteraCliente(registro.getContratoId());
        validarRepresentante(registro);
        eventoSeleccionModalidad(registro,false);
        cargarDatosCarteraCliente(registro);
        
        archivoTemporal = null;
        
        estado = EstadoCrud.CREAR_ADENDA;
        return "/aplicacion/gssp/sspCarteraCliente/CreateAdenda";
    }
    
    /**
     * Función tipo 'actionListener' que muestra el formulario para declarar un
     * registro de cartera de clientes
     * @param reg Registro referenciado
     */
    public void mostrarCrearDeclaracion(Map reg) {
        reiniciarValores();
        registro = ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+reg.get("ID")));
        
        listarModalidadesCarteraCliente(true, registro.getAdministradoId().getRuc());
        eventoSeleccionModalidad(registro,false);
        cargarDatosCarteraCliente(registro);
        
        switch(registro.getTipoOpeId().getCodProg()){
            case "TP_TDEC_NUEVO":                    
                    estado = EstadoCrud.DECLARAR;
                    break;
            case "TP_TDEC_ADENDA":
                    estado = EstadoCrud.DECLARAR_ADENDA;
                    break;
        }
        cargarArchivo(registro);
        archivoTemporal = ""+ archivo;
        validarRepresentante(registro);
        esDeclaracionCC = true;
        fechaFinDeclaracionTemp = registro.getFechaFin();
    }

    /**
     * Evento para guardar la información al editar un registro.
     * @return Página a redireccionar
     */
    public String editar() {
        try {
            if(validaCarteraCliente()){
                SspCarteraCliente registroOri = ejbSspCarteraClienteFacade.buscarCarteraCliById(registro.getId());
                if(completarDatosCarteraCliente(registroOri)){
                    registro = (SspCarteraCliente) JsfUtil.entidadMayusculas(registro, "");

                    // NUEVO CLIENTE
                    if(registro.getClienteId() != null){
                        registro.setClienteId( (SbPersonaGt) JsfUtil.entidadMayusculas(registro.getClienteId(), "") );
                        if(registro.getClienteId().getId() == null){
                            ejbSbPersonaFacade.create(registro.getClienteId());
                        }else{
                            ejbSbPersonaFacade.edit(registro.getClienteId());
                        }
                        if(registro.getDirecClienId() == null){
                            registro.setDirecClienId(obtenerDireccionCliente(registro.getClienteId().getSbDireccionList()));    
                        }
                        if(registro.getDirecClienId() != null){
                            if(registro.getDirecClienId().getId() == null  ){
                                ejbSbDireccionFacade.create(registro.getDirecClienId());
                            }else{
                                ejbSbDireccionFacade.edit(registro.getDirecClienId());
                            }
                        }
                    }
                    // NUEVO REPRESENTANTE
                    if(registro.getReprCliId() != null){
                        registro.setReprCliId( (SbPersonaGt) JsfUtil.entidadMayusculas(registro.getReprCliId(), "") );
                        if(registro.getReprCliId().getId() == null){
                            ejbSbPersonaFacade.create(registro.getReprCliId());   
                        }else{
                            ejbSbPersonaFacade.edit(registro.getReprCliId());    
                        }
                    }
                    
                    if(!Objects.equals(archivoTemporal, archivo)){
                        if(file != null){
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor() +  documento.getNombre() ), fotoByte );
                        }
                    }
                    if(renderPnlVehiculos){
                        for(SspCarteraVehiculo cVeh :registro.getSspCarteraVehiculoList()){
                            if(cVeh.getVehiculoId().getId() == null){
                                cVeh = (SspCarteraVehiculo) JsfUtil.entidadMayusculas(cVeh, "");
                                cVeh.setCarteraId(registro);
                                cVeh.setVehiculoId((SspVehiculo) JsfUtil.entidadMayusculas(cVeh.getVehiculoId(), ""));
                                ejbSspVehiculoFacade.create(cVeh.getVehiculoId());
                            }
                        }
                        int cont = 0;
                        for(SspCarteraVehiculo lsOri : registroOri.getSspCarteraVehiculoList()){
                            cont = 0;
                            for(SspCarteraVehiculo ls : registro.getSspCarteraVehiculoList()){
                                if(ls.getActivo() == 1 && (ls.getActualizacion() == null || (ls.getActualizacion() != null && ls.getActualizacion().toString().equals("1")) ) ){
                                    if(Objects.equals(lsOri.getId(), ls.getId())){
                                        cont++;
                                    }
                                }
                            }
                            if(cont == 0){
                                lsOri.setActivo(JsfUtil.FALSE);
                                lsOri.getVehiculoId().setActivo(JsfUtil.FALSE);
                                registro.getSspCarteraVehiculoList().add(lsOri);
                            }
                        }
                    }
                    
                    if(renderPnlVigilantes){
                        SspCarteraVigilante cartera;
                        int cont = 0;
                        for(SspCarteraVigilante lv : registroOri.getSspCarteraVigilanteList()){
                            if(lv.getCarneId() == null){
                                continue;
                            }
                            
                            cont = 0;
                            for(Map nmap : lstPersonalSeguridad){
                                if(nmap.get("declarado") == null || (nmap.get("declarado") != null && nmap.get("declarado").toString().equals("1")) ){
                                    if(Objects.equals(lv.getCarneId().getId(), Long.parseLong(""+nmap.get("carneId")) )){
                                        cont++;
                                    }
                                }   
                            }
                            if(cont == 0){
                                for(SspCarteraVigilante lvAct : registro.getSspCarteraVigilanteList()){
                                    if(Objects.equals(lv.getCarneId().getId(), lvAct.getCarneId().getId() )){
                                        lvAct.setActivo(JsfUtil.FALSE);
                                        break;
                                    }
                                }
                            }
                        }

                        for(Map nmap : lstPersonalSeguridad){
                            if(nmap.get("declarado") == null || (nmap.get("declarado") != null && nmap.get("declarado").toString().equals("1")) ){
                                cont = 0;
                                for(SspCarteraVigilante lv : registroOri.getSspCarteraVigilanteList()){
                                    if(lv.getCarneId() == null){
                                        continue;
                                    }
                                    if(Objects.equals(lv.getCarneId().getId(), Long.parseLong(""+nmap.get("carneId")) )){
                                        cont++;
                                    }
                                }
                                if(cont == 0){
                                    cartera = new SspCarteraVigilante();
                                    cartera.setActivo(JsfUtil.TRUE);
                                    cartera.setActualizacion(null);
                                    cartera.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                    cartera.setAudNumIp(JsfUtil.getIpAddress());
                                    cartera.setCarteraId(registro);
                                    cartera.setId(null);
                                    cartera.setCarneId((SspCarne) nmap.get("carneEntity"));
                                    registro.getSspCarteraVigilanteList().add(cartera);
                                }
                            }   
                        }                        
                    }
                    ////////////////
                    registro.setEvento(JsfUtil.FALSE);
                    if(blnEvento){
                        registro.setEvento(JsfUtil.TRUE);
                    }else{
                        registro.setTipoEvento(null);
                    }
                    /////////////////
                    registro.setFechaFinActualiza(registro.getFechaFin());
                    ejbSspCarteraClienteFacade.edit(registro);
                    if(registro.getDirecClienId() != null){
                       ejbSbDireccionFacade.edit(registro.getDirecClienId());
                    }
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado") + " Código: " + registro.getId());
                    return prepareList();
                }
            }   
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }
    
    public String editarTermino(){
        try {
            if(validaCarteraClienteTermino()){
                if(completarDatosTerminoContrato()){
                    registro = (SspCarteraCliente) JsfUtil.entidadMayusculas(registro, "");
                    
                    if(!Objects.equals(archivoTemporal, archivoTermino)){
                        if(file != null){
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor() +  documento.getNombre() ), fotoByte );
                        }
                    }
                    ejbSspCarteraClienteFacade.edit(registro);
                    
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado") + ", Código: " + registro.getId());
                    return prepareList();
                }   
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Evento para guardar la información al editar un registro.
     * @return Página a redireccionar
     */
    public String editarAdenda() {
        try {
            if(validaCarteraCliente()){
                SspCarteraCliente registroOri = ejbSspCarteraClienteFacade.buscarCarteraCliById(registro.getId());
                if(completarDatosCarteraCliente(registroOri)){
                    registro = (SspCarteraCliente) JsfUtil.entidadMayusculas(registro, "");
                    
                    // NUEVO CLIENTE
                    if(registro.getClienteId().getId() == null){
                        ejbSbPersonaFacade.create(registro.getClienteId());
                    }
                    // NUEVO REPRESENTANTE
                    if(registro.getReprCliId() != null){
                        if(registro.getReprCliId().getId() == null){
                            ejbSbPersonaFacade.create(registro.getReprCliId());   
                        }else{
                            ejbSbPersonaFacade.edit(registro.getReprCliId());    
                        }
                    }
                    if(!Objects.equals(archivoTemporal, archivo)){
                        if(file != null){
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor() +  documento.getNombre() ), fotoByte );
                        }
                    }
                    if(renderPnlVehiculos){
                         for(SspCarteraVehiculo cVeh :registro.getSspCarteraVehiculoList()){
                            if(cVeh.getVehiculoId().getId() == null){
                                cVeh = (SspCarteraVehiculo) JsfUtil.entidadMayusculas(cVeh, "");
                                cVeh.setCarteraId(registro);
                                cVeh.setVehiculoId((SspVehiculo) JsfUtil.entidadMayusculas(cVeh.getVehiculoId(), ""));
                                ejbSspVehiculoFacade.create(cVeh.getVehiculoId());
                            }
                        }
                        int cont = 0;
                        for(SspCarteraVehiculo lsOri : registroOri.getSspCarteraVehiculoList()){
                            cont = 0;
                            for(SspCarteraVehiculo ls : registro.getSspCarteraVehiculoList()){
                                if(ls.getActivo() == 1 && (ls.getActualizacion() == null || (ls.getActualizacion() != null && ls.getActualizacion().toString().equals("1")) ) ){
                                    if(Objects.equals(lsOri.getId(), ls.getId())){
                                        cont++;
                                    }
                                }   
                            }
                            if(cont == 0){
                                lsOri.setActivo(JsfUtil.FALSE);
                                lsOri.getVehiculoId().setActivo(JsfUtil.FALSE);
                                registro.getSspCarteraVehiculoList().add(lsOri);
                            }
                        }
                    }
                    
                    if(renderPnlVigilantes){
                        SspCarteraVigilante cartera;
                        int cont = 0;
                        for(SspCarteraVigilante lv : registroOri.getSspCarteraVigilanteList()){
                            if(lv.getCarneId() == null){
                                continue;
                            }
                            
                            cont = 0;
                            for(Map nmap : lstPersonalSeguridad){
                                if(nmap.get("declarado") == null || (nmap.get("declarado") != null && nmap.get("declarado").toString().equals("1")) ){
                                    if(Objects.equals(lv.getCarneId().getId(), Long.parseLong(""+nmap.get("carneId")) )){
                                        cont++;
                                    }
                                }   
                            }
                            if(cont == 0){
                                for(SspCarteraVigilante lvAct : registro.getSspCarteraVigilanteList()){
                                    if(lvAct.getCarneId() == null){
                                        continue;
                                    }
                                    if(Objects.equals(lv.getCarneId().getId(), lvAct.getCarneId().getId())){
                                        lvAct.setActivo(JsfUtil.FALSE);
                                        break;
                                    }
                                }
                            }
                        }

                        for(Map nmap : lstPersonalSeguridad){
                            if(nmap.get("declarado") == null || (nmap.get("declarado") != null && nmap.get("declarado").toString().equals("1")) ){
                                cont = 0;
                                for(SspCarteraVigilante lv : registroOri.getSspCarteraVigilanteList()){
                                    if(lv.getCarneId() == null){
                                        continue;
                                    }
                                    if(Objects.equals(lv.getCarneId().getId(), Long.parseLong(""+nmap.get("carneId")) )){
                                        cont++;
                                    }
                                }
                                if(cont == 0){
                                    cartera = new SspCarteraVigilante();
                                    cartera.setActivo(JsfUtil.TRUE);
                                    cartera.setActualizacion(null);
                                    cartera.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                    cartera.setAudNumIp(JsfUtil.getIpAddress());
                                    cartera.setCarteraId(registro);
                                    cartera.setId(null);                                    
                                    cartera.setCarneId((SspCarne) nmap.get("carneEntity"));
                                    registro.getSspCarteraVigilanteList().add(cartera);
                                }
                            }   
                        }                        
                    }
                    ////////////////
                    registro.setEvento(JsfUtil.FALSE);
                    if(blnEvento){
                        registro.setEvento(JsfUtil.TRUE);
                    }else{
                        registro.setTipoEvento(null);
                    }
                    /////////////////
                    registro.setFechaFinActualiza(registro.getFechaFin());
                    ejbSspCarteraClienteFacade.edit(registro);
                    //
                    if(registro.getDirecClienId() != null){
                       ejbSbDireccionFacade.edit(registro.getDirecClienId());
                    }
                    ///
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado") + ", Código: " + registro.getId());
                    return prepareList();
                }
            }   
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }
    
    
    /**
     * Evento para guardar la información al crear un nuevo registro.
     * @return Página a redireccionar
     */
    public String crear() {
        try {
            if(validaCarteraCliente()){
                if(completarDatosCarteraCliente(null)){
                    registro = (SspCarteraCliente) JsfUtil.entidadMayusculas(registro, "");

                    if(file != null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor() +  documento.getNombre() ), fotoByte );
                    }
                    // NUEVO CLIENTE
                    if(registro.getClienteId() != null){
                        registro.setClienteId( (SbPersonaGt) JsfUtil.entidadMayusculas(registro.getClienteId(), "") );
                        if(registro.getClienteId().getId() == null){
                            ejbSbPersonaFacade.create(registro.getClienteId());
                        }else{
                            ejbSbPersonaFacade.edit(registro.getClienteId());
                        }
                        if(registro.getDirecClienId() == null){
                            registro.setDirecClienId(obtenerDireccionCliente(registro.getClienteId().getSbDireccionList()));    
                        }
                        if(registro.getDirecClienId() != null){
                            if(registro.getDirecClienId().getId() == null  ){
                                ejbSbDireccionFacade.create(registro.getDirecClienId());
                            }else{
                                ejbSbDireccionFacade.edit(registro.getDirecClienId());
                            }
                        }
                    }
                    // NUEVO REPRESENTANTE
                    if(registro.getReprCliId() != null){
                        registro.setReprCliId( (SbPersonaGt) JsfUtil.entidadMayusculas(registro.getReprCliId(), "") );
                        if(registro.getReprCliId().getId() == null){
                            ejbSbPersonaFacade.create(registro.getReprCliId());
                        }else{
                            ejbSbPersonaFacade.edit(registro.getReprCliId());
                        }
                    }
                    //////////////
                    
                    if(renderPnlVehiculos){
                        for(SspCarteraVehiculo cVeh :registro.getSspCarteraVehiculoList()){
                            cVeh = (SspCarteraVehiculo) JsfUtil.entidadMayusculas(cVeh, "") ;
                            cVeh.setCarteraId(registro);
                            cVeh.setVehiculoId((SspVehiculo) JsfUtil.entidadMayusculas(cVeh.getVehiculoId(), ""));
                            cVeh.getVehiculoId().setId(null);
                            ejbSspVehiculoFacade.create(cVeh.getVehiculoId());
                        }
                    }
                    if(renderPnlVigilantes){
                        SspCarteraVigilante cartera;
                        for(Map nmap : lstPersonalSeguridad){                            
                            cartera = new SspCarteraVigilante();
                            cartera.setActivo(JsfUtil.TRUE);
                            cartera.setActualizacion(null);
                            cartera.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            cartera.setAudNumIp(JsfUtil.getIpAddress());
                            cartera.setCarteraId(registro);
                            cartera.setId(null);
                            cartera.setCarneId((SspCarne) nmap.get("carneEntity"));
                            registro.getSspCarteraVigilanteList().add(cartera);
                        }
                    }
                    /// Consorcio ///
                    if(registro.getSbPersonaList() != null){
                        for(SbPersonaGt cons : registro.getSbPersonaList()){
                            if(cons.getId() == null){
                                ejbSbPersonaFacade.create(cons);
                            }
                        }
                    }
                    ////////////////
                    registro.setEvento(JsfUtil.FALSE);
                    if(blnEvento){
                        registro.setEvento(JsfUtil.TRUE);
                    }else{
                        registro.setTipoEvento(null);
                    }
                    /////////////////
                    registro.setFechaFinActualiza(registro.getFechaFin());
                    adicionaCarteraEvento(registro.getEstadoId(), null);
                    ejbSspCarteraClienteFacade.create(registro);
                    
                    if(file == null){
                        JsfUtil.mensajeAdvertencia("El archivo no ha sido adjuntado. Por favor presentarlo por mesa de partes.");
                    }
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado") + ", Código: " + registro.getId());
                    return prepareList();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }
    
    public String crearTermino(){
        try {
            if(validaCarteraClienteTermino()){
                if(completarDatosTerminoContrato()){
                    registro = (SspCarteraCliente) JsfUtil.entidadMayusculas(registro, "");
                    if(file != null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor() +  documento.getNombre() ), fotoByte );
                    }
                    registro.setSspCarteraVigilanteList(new ArrayList());
                    adicionaCarteraEvento(registro.getEstadoId(), null);
                    
                    if(renderPnlVigilantes){
                        SspCarteraVigilante cartera;
                        for(Map nmap : lstPersonalSeguridad){                            
                            cartera = new SspCarteraVigilante();
                            cartera.setActivo(JsfUtil.TRUE);
                            cartera.setActualizacion(null);
                            cartera.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            cartera.setAudNumIp(JsfUtil.getIpAddress());
                            cartera.setCarteraId(registro);
                            cartera.setId(null);
                            cartera.setCarneId((SspCarne) nmap.get("carneEntity"));
                            registro.getSspCarteraVigilanteList().add(cartera);
                        }
                    }
                    ////////////////
                    registro.setEvento(JsfUtil.FALSE);
                    if(blnEvento){
                        registro.setEvento(JsfUtil.TRUE);
                    }else{
                        registro.setTipoEvento(null);
                    }
                    /////////////////
                    ejbSspCarteraClienteFacade.create(registro);
      
                    // Para guardar contrato referenciado
                    registro.getContratoId().setFlagEvaluacion('0');
                    ejbSspCarteraClienteFacade.edit(registro.getContratoId());

                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado") + ", Código: " + registro.getId());
                    return prepareList();
                }   
            }   
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }
    
    public String crearAdenda(){
        try {
            if(validaCarteraCliente()){
                if(completarDatosCarteraCliente(null)){
                    registro = (SspCarteraCliente) JsfUtil.entidadMayusculas(registro, "");

                    // NUEVO CLIENTE
                    if(registro.getClienteId() != null){
                        registro.setClienteId( (SbPersonaGt) JsfUtil.entidadMayusculas(registro.getClienteId(), "") );
                        if(registro.getClienteId().getId() == null){
                            ejbSbPersonaFacade.create(registro.getClienteId());
                        }else{
                            ejbSbPersonaFacade.edit(registro.getClienteId());
                        }
                    }
                    // NUEVO REPRESENTANTE
                    if(registro.getReprCliId() != null){
                        registro.setReprCliId( (SbPersonaGt) JsfUtil.entidadMayusculas(registro.getReprCliId(), "") );
                        if(registro.getReprCliId().getId() == null){
                            ejbSbPersonaFacade.create(registro.getReprCliId());   
                        }else{
                            ejbSbPersonaFacade.edit(registro.getReprCliId());    
                        }
                    }
                    if(file != null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor() +  documento.getNombre() ), fotoByte );
                    }
                    if(renderPnlVehiculos){
                        for(SspCarteraVehiculo cVeh :registro.getSspCarteraVehiculoList()){
                            if(cVeh.getVehiculoId().getId() == null){
                                cVeh = (SspCarteraVehiculo) JsfUtil.entidadMayusculas(cVeh, "");
                                cVeh.setCarteraId(registro);                                
                                cVeh.setVehiculoId((SspVehiculo) JsfUtil.entidadMayusculas(cVeh.getVehiculoId(), ""));                                
                                cVeh.getVehiculoId().setId(null);
                                ejbSspVehiculoFacade.create(cVeh.getVehiculoId());
                            }
                        }
                    }
                    registro.setSspCarteraVigilanteList(new ArrayList());
                    adicionaCarteraEvento(registro.getEstadoId(), null);
                    
                    if(renderPnlVigilantes){
                        SspCarteraVigilante cartera;
                        for(Map nmap : lstPersonalSeguridad){                            
                            cartera = new SspCarteraVigilante();
                            cartera.setActivo(JsfUtil.TRUE);
                            cartera.setActualizacion(null);
                            cartera.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            cartera.setAudNumIp(JsfUtil.getIpAddress());
                            cartera.setCarteraId(registro);
                            cartera.setId(null);
                            cartera.setCarneId((SspCarne) nmap.get("carneEntity"));
                            registro.getSspCarteraVigilanteList().add(cartera);
                        }
                    }

                    List<SspCarteraArma> listArma = new ArrayList();
                    if(renderPnlArmas){
                        SspCarteraArma arma;
                        for(SspCarteraArma row : registro.getSspCarteraArmaList()){
                            arma = new SspCarteraArma();
                            arma.setCarteraId(registro);
                            arma.setArmaId(row.getArmaId());
                            arma.setActualizacion(row.getActualizacion());
                            arma.setObservacion(row.getObservacion());
                            arma.setActivo(row.getActivo());
                            arma.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            arma.setAudNumIp(JsfUtil.getIpAddress());                    
                            listArma.add(arma);
                        }
                    }
                    registro.setSspCarteraArmaList(listArma);
                    
                    /// Consorcio ///
                    if(registro.getSbPersonaList() != null){
                        for(SbPersonaGt cons : registro.getSbPersonaList()){
                            if(cons.getId() == null){
                                ejbSbPersonaFacade.create(cons);
                            }
                        }
                    }
                    ////////////////
                    registro.setEvento(JsfUtil.FALSE);
                    if(blnEvento){
                        registro.setEvento(JsfUtil.TRUE);
                    }else{
                        registro.setTipoEvento(null);
                    }
                    /////////////////
                    
                    registro.setId(null);
                    registro.setTipoOpeId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TDEC_ADENDA"));
                    registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    registro.setAudNumIp(JsfUtil.getIpAddress()); 
                    registro.setFechaFinActualiza(registro.getFechaFin());
                    ejbSspCarteraClienteFacade.create(registro);

                    // Para guardar contrato referenciado
                    registro.getContratoId().setFlagEvaluacion('1');
                    ejbSspCarteraClienteFacade.edit(registro.getContratoId());
                    //
                    if(registro.getDirecClienId() != null){
                       ejbSbDireccionFacade.edit(registro.getDirecClienId());
                    }
                    if(file == null){
                        JsfUtil.mensajeAdvertencia("El archivo no ha sido adjuntado. Por favor presentarlo por mesa de partes.");
                    }
                    
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado") + ", Código: " + registro.getId());
                    return prepareList();
                }
            }   
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }
    
    public String declarar() {
        try {
            if(validaCarteraCliente()){
                //String ruta = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor();
                SspCarteraCliente registroOri = ejbSspCarteraClienteFacade.buscarCarteraCliById(registro.getId());
                if(completarDatosCarteraClienteDeclaracion(registroOri)){
                    registro = (SspCarteraCliente) JsfUtil.entidadMayusculas(registro, "");

                    if(renderPnlVehiculos){
                        for(SspCarteraVehiculo cVeh :registro.getSspCarteraVehiculoList()){
                            if(cVeh.getVehiculoId().getId() == null){
                                cVeh = (SspCarteraVehiculo) JsfUtil.entidadMayusculas(cVeh, "");
                                cVeh.setCarteraId(registro);
                                cVeh.setActualizacion('1');
                                cVeh.setFechaActualizacion(new Date());
                                cVeh.setVehiculoId((SspVehiculo) JsfUtil.entidadMayusculas(cVeh.getVehiculoId(), ""));
                                ejbSspVehiculoFacade.create(cVeh.getVehiculoId());
                            }
                        }
                        int cont = 0;
                        for(SspCarteraVehiculo lsOri : registroOri.getSspCarteraVehiculoList()){
                            cont = 0;
                            for(SspCarteraVehiculo ls : registro.getSspCarteraVehiculoList()){
                                if(ls.getActivo() == 1 && (ls.getActualizacion() == null || (ls.getActualizacion() != null && ls.getActualizacion().toString().equals("1")) ) ){
                                    if(Objects.equals(lsOri.getId(), ls.getId())){
                                        cont++;
                                    }
                                }
                            }
                            if(cont == 0){
                                lsOri.setActualizacion('0');
                                lsOri.setFechaActualizacion(new Date());
                                for(Map vigEliminado : lstVehiculosMotivosEliminados ){
                                    if(Objects.equals((Long) vigEliminado.get("id"), lsOri.getId())){                                        
                                        lsOri.setObservacion(""+vigEliminado.get("motivoSeguridad"));
                                        break;
                                    }
                                }
                                registro.getSspCarteraVehiculoList().add(lsOri);
                            }
                        }
                        JsfUtil.borrarIds(registro.getSspCarteraVehiculoList());
                    }
                    
                    if(renderPnlVigilantes){
                        SspCarteraVigilante cartera;
                        int cont = 0;
                        for(SspCarteraVigilante lv : registroOri.getSspCarteraVigilanteList()){
                            if(lv.getCarneId() == null){
                                continue;
                            }
                            cont = 0;
                            for(Map nmap : lstPersonalSeguridad){
                                if(nmap.get("declarado") == null || (nmap.get("declarado") != null && nmap.get("declarado").toString().equals("1")) ){
                                    if(Objects.equals(lv.getCarneId().getId(), Long.parseLong(""+nmap.get("carneId")) )){
                                        cont++;
                                    }
                                }   
                            }
                            if(cont == 0){
                                for(SspCarteraVigilante lvAct : registro.getSspCarteraVigilanteList()){
                                    if(lvAct.getCarneId() == null){
                                        continue;
                                    }
                                    if(Objects.equals(lv.getCarneId().getId(), lvAct.getCarneId().getId())){
                                        // No se le da de baja, solo se le agrega el flag
                                        lvAct.setActualizacion('0');
                                        lvAct.setFechaActualizacion(new Date());
                                        for(Map vigEliminado : lstPersonalSeguridadMotivosEliminados ){
                                            if(Objects.equals(Long.parseLong(""+vigEliminado.get("carneId")), lv.getCarneId().getId())){
                                                lvAct.setObservacion(""+vigEliminado.get("motivoSeguridad"));
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        for(Map nmap : lstPersonalSeguridad){
                            if(nmap.get("declarado") == null || (nmap.get("declarado") != null && nmap.get("declarado").toString().equals("1")) ){
                                    cont = 0;
                                    for(SspCarteraVigilante lv : registroOri.getSspCarteraVigilanteList()){
                                        if(lv.getCarneId() == null){
                                            continue;
                                        }
                                        if(lv.getActivo() == 1 && (lv.getActualizacion() == null || (lv.getActualizacion() != null && lv.getActualizacion().toString().equals("1"))  ) ){
                                            if(Objects.equals(lv.getCarneId().getId(), Long.parseLong(""+nmap.get("carneId")) )){
                                                cont++;
                                            }
                                        }
                                    }
                                    if(cont == 0){
                                        cartera = new SspCarteraVigilante();
                                        cartera.setActivo(JsfUtil.TRUE);
                                        cartera.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                        cartera.setAudNumIp(JsfUtil.getIpAddress());
                                        cartera.setCarteraId(registro);
                                        cartera.setId(null);
                                        cartera.setActualizacion('1');
                                        cartera.setFechaActualizacion(new Date());
                                        cartera.setObservacion(""+nmap.get("motivoSeguridad"));
                                        cartera.setCarneId((SspCarne) nmap.get("carneEntity"));
                                        registro.getSspCarteraVigilanteList().add(cartera);
                                    }
                            }
                        }
                    }
                    
                    ////////////////
                    registro.setEvento(JsfUtil.FALSE);
                    if(blnEvento){
                        registro.setEvento(JsfUtil.TRUE);
                    }else{
                        registro.setTipoEvento(null);
                    }
                    /////////////////
                    
                    // Renombrar archivo //
//                    if (JsfUtil.buscarPdfRepositorio(""+registro.getId(), ruta)) {
//                        uploadFilesController.renameFilePath(ruta, registro.getId().toString()+".pdf", registro.getId().toString()+ "_" + (new Date()).getTime()+".pdf");    
//                    }
                    ///////////////////////
                    
                    registro.setFechaActualizacion(new Date());
                    ejbSspCarteraClienteFacade.edit(registro);
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado") + ", Código: " + registro.getId());
                    return prepareList();
                }
            }   
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }
    
    public void obtenerRepresentantes(SspCarteraCliente ccRegistro){
        lstRepresentante = new ArrayList();
        for(SbRelacionPersonaGt relacion : ccRegistro.getAdministradoId().getSbRelacionPersonaList()){
            if(relacion.getActivo() == 1 && relacion.getPersonaDestId().getActivo() == 1){
                if(!lstRepresentante.contains(relacion.getPersonaDestId())){
                    lstRepresentante.add(relacion.getPersonaDestId());    
                }
            }
        }
    }
    
    public void adicionaCarteraEvento(TipoSeguridad estado, String observacion){
        SspCarteraEvento evento = new SspCarteraEvento();
        evento.setId(null);
        evento.setActivo(JsfUtil.TRUE);
        evento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        evento.setAudNumIp(JsfUtil.getIpAddress());
        evento.setCarteraId(registro);
        evento.setFecha(new Date());
        evento.setObservacion(observacion);
        evento.setTipoEventoId(estado);
        evento.setUserId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).getId());
        registro.getSspCarteraEventoList().add(evento);
    }
    
    public SbDireccionGt obtenerDireccionCliente(List<SbDireccionGt> lstDireccion){
        try {
            if(lstDireccion != null && !lstDireccion.isEmpty()){
                orderListResultados(lstDireccion);    
                return lstDireccion.get(0);
            }
            
        } catch (Exception e) {
        }
        return null;
    }
    
    public void orderListResultados(List<SbDireccionGt> po) {
        Collections.sort(po, new Comparator<SbDireccionGt>() {
            @Override
            public int compare(SbDireccionGt o1, SbDireccionGt o2) {
                return o2.getId().toString().compareTo(o1.getId().toString());
            }
        });
    }
    
    public boolean validaCarteraCliente(){
        boolean validacion = true;

        if(registro.getTipoSeguridadList().isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor seleccione al menos una modalidad");
        }else{
            if(renderPnlPersona){
                // Habilitado Panel de datos de cliente
                if(tipoDoc == null){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor seleccione el tipo de documento en los datos del cliente");
                    JsfUtil.invalidar(obtenerForm()+":tipoDoc");
                }
                if(numDoc == null || numDoc.isEmpty()){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor ingresar el nro. de documento en los datos del cliente y presionar el botón BUSCAR");
                    JsfUtil.invalidar(obtenerForm()+":ruc");
                }
                if(tipoDoc != null && !(numDoc == null || numDoc.isEmpty()) ){
                    if(!blnBusco){
                        validacion = false;
                        JsfUtil.mensajeError("Por favor presionar el botón BUSCAR para encontrar los datos del cliente");
                    }
                }
                if(getEsRuc() && blnBusco){
                    if(esNuevo){
                        if(registro.getClienteId().getRznSocial() == null || registro.getClienteId().getRznSocial().isEmpty()){
                            validacion = false;
                            JsfUtil.invalidar(obtenerForm()+":rznSocial");
                            JsfUtil.mensajeError("Por favor ingresar la razón social del cliente");
                        }
                    }
                    if(!renderDireccionRucCombo){
                        if(direccionRuc == null || direccionRuc.isEmpty()){
                            validacion = false;
                            JsfUtil.invalidar(obtenerForm()+":direccionRuc");
                            JsfUtil.mensajeError("Por favor ingresar la dirección del cliente");
                        }                        
                    }else{
                        if(direccionRucCombo == null){
                            validacion = false;
                            JsfUtil.invalidar(obtenerForm()+":direccionRucCombo");
                            JsfUtil.mensajeError("Por favor seleccionar la dirección del cliente");
                        }
                    }
                    if(ubigeoRuc == null){
                        validacion = false;
                        JsfUtil.mensajeError("Por favor ingresar el ubigeo del cliente");
                        JsfUtil.invalidar(obtenerForm()+":ubigeoRuc");
                    }
                }
                if(getEsDni() && blnBusco){
                    if(esNuevo){
                        if(registro.getClienteId().getNombres() == null || registro.getClienteId().getNombres().isEmpty()){
                            validacion = false;                            
                            JsfUtil.mensajeError("Por favor ingresar los nombres del cliente");
                            JsfUtil.invalidar(obtenerForm()+":nombresDni");
                        }
                        if(registro.getClienteId().getApePat()== null || registro.getClienteId().getApePat().isEmpty()){
                            validacion = false;                            
                            JsfUtil.mensajeError("Por favor ingresar el apellido paterno del cliente");
                            JsfUtil.invalidar(obtenerForm()+":apePat");
                        }
                        if(!tipoDoc.equals("4")){
                            if(registro.getClienteId().getApeMat()== null || registro.getClienteId().getApeMat().isEmpty()){
                                validacion = false;                            
                                JsfUtil.mensajeError("Por favor ingresar el apellido materno del cliente");
                                JsfUtil.invalidar(obtenerForm()+":apeMat");
                            }    
                        }
                    }
                    if(!renderDireccionDniCombo){
                        if(direccionDni == null || direccionDni.isEmpty()){
                            validacion = false;
                            JsfUtil.mensajeError("Por favor ingresar la dirección del cliente");
                            JsfUtil.invalidar(obtenerForm()+":direccionDni");
                        }                        
                    }else{
                        if(direccionDniCombo == null){
                            validacion = false;
                            JsfUtil.invalidar(obtenerForm()+":direccionDniCombo");
                            JsfUtil.mensajeError("Por favor seleccionar la dirección del cliente");
                        }
                    }
                    if(ubigeoDni == null){
                        validacion = false;
                        JsfUtil.mensajeError("Por favor ingresar el ubigeo del cliente");
                        JsfUtil.invalidar(obtenerForm()+":ubigeoDni");
                    }
                    if(tipoDoc.equals("4") && registro.getClienteId() != null){
                        //// Validación para exigir la fecha de nacimiento del extranjero.
                        if(registro.getClienteId().getFechaNac() == null){
                            validacion = false;
                            JsfUtil.mensajeError("Por favor ingresar la fecha de nacimiento del cliente");
                            JsfUtil.invalidar(obtenerForm()+":fechaNac");
                        }
                    }
                    if(!disabledCorreo){
//                        if(correo == null || correo.isEmpty()){
//                            validacion = false;
//                            JsfUtil.mensajeError("Por favor ingresar el correo del cliente");
//                            JsfUtil.invalidar(obtenerForm()+":correoDni");
//                        }
                    }
                    if(!disabledTelefono){
//                        if(telefono == null || telefono.isEmpty()){
//                            validacion = false;
//                            JsfUtil.mensajeError("Por favor ingresar el teléfono del cliente");
//                            JsfUtil.invalidar(obtenerForm()+":telefonoDni");
//                        }
                    }
                }
            }
            
            if(renderPnlRepLegal){
                // Habilitado Panel de datos del representante legal
                if(tipoDocRepLegal == null){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor seleccione el tipo de documento del representante legal");
                    JsfUtil.invalidar(obtenerForm()+":tipoDocRepLegal");
                }
                if(numDocRepLegal == null || numDocRepLegal.isEmpty()){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor ingresar el nro. de documento del representante legal y presionar el botón BUSCAR");
                    JsfUtil.invalidar(obtenerForm()+":docRepLegal");
                }
                if(tipoDocRepLegal != null && !(numDocRepLegal == null || numDocRepLegal.isEmpty()) ){
                    if(!blnBuscoRepLegal){
                        validacion = false;
                        JsfUtil.mensajeError("Por favor presionar el botón BUSCAR para encontrar los datos del representante legal");
                    }
                }
                if(blnBuscoRepLegal){
                    if(esNuevoRepLegal){
                        if(registro.getReprCliId().getNombres() == null || registro.getReprCliId().getNombres().isEmpty()){
                            validacion = false;                            
                            JsfUtil.mensajeError("Por favor ingresar los nombres del representante");
                            JsfUtil.invalidar(obtenerForm()+":nombresRepLegal");
                        }
                        if(registro.getReprCliId().getApePat()== null || registro.getReprCliId().getApePat().isEmpty()){
                            validacion = false;                            
                            JsfUtil.mensajeError("Por favor ingresar el apellido paterno del representante");
                            JsfUtil.invalidar(obtenerForm()+":apePatRepLegal");
                        }
                        if(!tipoDoc.equals("4")){
                            if(registro.getReprCliId().getApeMat()== null || registro.getReprCliId().getApeMat().isEmpty()){
                                validacion = false;                            
                                JsfUtil.mensajeError("Por favor ingresar el apellido materno del representante");
                                JsfUtil.invalidar(obtenerForm()+":apeMatRepLegal");
                            }    
                        }
                    }
                    if(tipoDocRepLegal.equals("4") && registro.getReprCliId() != null){
                        //// Validación para exigir la fecha de nacimiento del extranjero.
                        if(registro.getReprCliId().getFechaNac() == null){
                            validacion = false;
                            JsfUtil.mensajeError("Por favor ingresar la fecha de nacimiento del representante");
                            JsfUtil.invalidar(obtenerForm()+":fechaNacRepLegal");
                        }
                    }
                }
            }
        }
        if(registro.getSspLugarServicioList().isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar al menos un lugar de servicio");
        }else{
            int contLugar = 0;
            for(SspLugarServicio lugar : registro.getSspLugarServicioList()){
                if(lugar.getActivo() == 1){
                    contLugar++;
                    
                    if(lugar.getLatitud() == null || lugar.getLatitud() == null){
                        if ("1".equals(JsfUtil.bundleBDIntegrado("sspCarteraCli_GoogleMapsActivo"))){
                            validacion = false;
                            JsfUtil.mensajeError("Debe seleccionar un punto en el mapa del lugar de servicio");
                        }
                    }
                }
            }
            if(contLugar > 1){
                validacion = false;
                JsfUtil.mensajeError("No puede ingresar más de un lugar de servicio");
            }
        }
        if(blnEvento){
            if(registro.getTipoEvento() == null || registro.getTipoEvento().isEmpty()){
                validacion = false;
                JsfUtil.mensajeError("Por favor ingrese el tipo de evento");
                JsfUtil.invalidar(obtenerForm()+":tipoEvento");
            }
            if(horaInicioEvento == null){
                JsfUtil.invalidar(obtenerForm()+":horaInicio");
                JsfUtil.mensajeError("Por favor ingrese la hora de inicio");
                validacion = false;
            }
//            if(horaFinEvento == null){
//                JsfUtil.invalidar(obtenerForm()+":horaFin");
//                JsfUtil.mensajeError("Por favor ingrese la hora de inicio");
//                validacion = false;
//            }
        }
        if(fechaInicioServicio == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar la fecha de inicio del servicio");
            JsfUtil.invalidar(obtenerForm()+":fechaInicio");
        }
        if(fechaInicioServicio != null && registro.getFechaFin() != null){
            if(JsfUtil.getFechaSinHora(fechaInicioServicio).compareTo(registro.getFechaFin()) > 0 ){
                validacion = false;
                JsfUtil.mensajeError("La fecha de inicio del servicio no puede ser mayor que la fecha de término");
                JsfUtil.invalidar(obtenerForm()+":fechaInicio");
                JsfUtil.invalidar(obtenerForm()+":fechaFin");
            }
            if(blnEvento){
                if(horaInicioEvento != null && horaFinEvento != null){
                    fechaInicioServicio.setHours(horaInicioEvento.getHours());
                    fechaInicioServicio.setMinutes(horaInicioEvento.getMinutes());
                    fechaInicioServicio.setSeconds(0);
                    registro.getFechaFin().setHours(horaFinEvento.getHours());
                    registro.getFechaFin().setMinutes(horaFinEvento.getMinutes());
                    registro.getFechaFin().setSeconds(0);
                    if(fechaInicioServicio.compareTo(registro.getFechaFin()) > 0 ){
                        validacion = false;
                        JsfUtil.mensajeError("La fecha y hora de inicio del servicio no puede ser mayor que la fecha y hora de término");
                        JsfUtil.invalidar(obtenerForm()+":fechaInicio");
                        JsfUtil.invalidar(obtenerForm()+":fechaFin");
                        JsfUtil.invalidar(obtenerForm()+":horaInicio");
                        JsfUtil.invalidar(obtenerForm()+":horaFin");
                    }
                }
            }
        }
        if(estado == EstadoCrud.DECLARAR || estado == EstadoCrud.DECLARAR){
            if(isRenderObservacionFechaFin() && (strObservacion == null || strObservacion.isEmpty()) ){
                validacion = false;
                JsfUtil.mensajeError("Debe ingresar el motivo del cambio de la fecha de término de contrato");
                JsfUtil.invalidar(obtenerForm()+":observacionFechaFin");
            }
        }
//        if(file == null && estado == EstadoCrud.CREAR){
//            validacion = false;
//            JsfUtil.mensajeError("Por favor ingrese el archivo adjunto");
//        }
        
        if(!registro.getTipoSeguridadList().isEmpty()){
            if(renderPnlVigilantes){
                if(lstPersonalSeguridad.isEmpty()){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor ingresar al menos un vigilante");
                }
            }

            if(renderPnlArmas){
                if(tieneTransporte(registro) && registro.getSspCarteraArmaList().isEmpty()){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor ingresar al menos una arma");
                }
            }

            if(renderPnlVehiculos){
                if(registro.getSspCarteraVehiculoList().isEmpty()){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor ingresar al menos un vehículo");
                }
            }
        }
        if(estado == EstadoCrud.CREAR || estado == EstadoCrud.EDITAR){
            if(registro.getClienteId() != null && fechaInicioServicio != null && !registro.getSspLugarServicioList().isEmpty()){
                int cont = ejbSspCarteraClienteFacade.contContratosRepetidosActivos(registro.getId(), registro.getClienteId().getId(), fechaInicioServicio, registro.getFechaFin(), registro.getSspLugarServicioList().get(0).getDireccion());
                if(cont > 0){
                    validacion = false;
                    JsfUtil.mensajeError("Ya existe un contrato vigente con la misma información ingresada");
                }
            }
        }
        return validacion;
    }
    
    public boolean tieneSispe(SspCarteraCliente ccRegistro){
        boolean blnSispe = false;        
        for(TipoSeguridad tipo : ccRegistro.getTipoSeguridadList()){
            if(tipo.getCodProg().equals("TP_MCO_SIS")){
                blnSispe = true;
            }
        }
        return blnSispe;
    }
    
    public boolean tieneTransporte(SspCarteraCliente ccRegistro){
        boolean blnTransp = false;        
        for(TipoSeguridad tipo : ccRegistro.getTipoSeguridadList()){
            if(tipo.getCodProg().equals("TP_MCO_TRA")){
                blnTransp = true;
            }
        }
        return blnTransp;
    }
    
    public void cargarDatosCarteraCliente(SspCarteraCliente ccRegistro){
        obtenerRepresentantes(ccRegistro);
        
        if(ccRegistro.getTipoSeguridadList() != null && !ccRegistro.getTipoSeguridadList().isEmpty()){
            for(TipoSeguridad ts : ccRegistro.getTipoSeguridadList()){
                if(ts.getActivo() == 1 && ts.getCodProg().equals("TP_MCO_TRA")){
                    esTransporteDinero = true;
                    break;
                }
            }
        }
        
        if(renderPnlPersona){
            setDisabledBusco(true);
            setBlnBusco(true);
            setEsNuevo(false);
            if(ccRegistro.getClienteId().getRuc() != null){
                maxLength = 11;
                numDoc = ccRegistro.getClienteId().getRuc();
                if(numDoc.startsWith("1")){  // Ruc que inicia con 1 es Natural Juridica
                    tipoDoc = "2";
                }else{
                    setRenderPnlRepLegal(true);
                    tipoDoc = "1";
                }
            }else{
                numDoc = ccRegistro.getClienteId().getNumDoc();
                if(ccRegistro.getClienteId().getNumDoc().trim().length() == 8){
                    tipoDoc = "3";
                    maxLength = 8;
                }else{
                    tipoDoc = "4";
                    maxLength = 9;
                }
            }

            if(ccRegistro.getDirecClienId() != null){
                direccionRucCombo = ccRegistro.getDirecClienId();
                direccionDniCombo = ccRegistro.getDirecClienId();
                direccionRuc = ccRegistro.getDirecClienId().getDireccion();
                direccionDni = ccRegistro.getDirecClienId().getDireccion();
                ubigeoRuc = ccRegistro.getDirecClienId().getDistritoId();
                ubigeoDni = ccRegistro.getDirecClienId().getDistritoId();
                lstDireccionCombo.add(ccRegistro.getDirecClienId());
            }else{
                for(SbDireccionGt dire : ccRegistro.getClienteId().getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                        direccionRuc = dire.getDireccion();
                        direccionDni = dire.getDireccion();
                        ubigeoRuc = dire.getDistritoId();
                        ubigeoDni = dire.getDistritoId();
                        lstDireccionCombo.add(dire);
                    }
                }
            }
            for(SbDireccionGt dire : ccRegistro.getClienteId().getSbDireccionList()){
                if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                    if(!lstDireccionCombo.contains(dire)){
                        lstDireccionCombo.add(dire);
                    }
                }
            }
            if(!lstDireccionCombo.isEmpty()){
                setRenderDireccionRucCombo(true);
                setRenderDireccionDniCombo(true);
            }
            for(SbMedioContactoGt medio : ccRegistro.getClienteId().getSbMedioContactoList()){
                if(medio.getActivo() == 1){
                    if(medio.getTipoId().getCodProg().equals("TP_MEDCO_COR")){
                        correo = medio.getValor();
                        //setDisabledCorreo(true);
                        correoTemp = medio.getValor();
                    }
                    if(medio.getTipoId().getCodProg().equals("TP_MEDCO_MOV")){
                        telefono = medio.getValor();
                        //setDisabledTelefono(true);
                        telefonoTemp = medio.getValor();
                    }
                }
            }        
        }
        
        if(renderPnlRepLegal){
            if(ccRegistro.getReprCliId() != null){
                setDisabledBuscoRepLegal(true);
                setBlnBuscoRepLegal(true);
                setEsNuevoRepLegal(false);
                maxLengthRepLegal = 8;
                if(ccRegistro.getReprCliId().getRuc() != null){
                    maxLengthRepLegal = 11;
                    numDocRepLegal = ccRegistro.getReprCliId().getRuc();
                    if(numDocRepLegal.startsWith("1")){
                        tipoDocRepLegal = "2";
                    }else{
                        tipoDocRepLegal = "1";
                    }
                }else{
                    numDocRepLegal = ccRegistro.getReprCliId().getNumDoc();
                    if(ccRegistro.getReprCliId().getNumDoc().trim().length() == 8){
                        tipoDocRepLegal = "3";
                        maxLengthRepLegal = 8;
                    }else{
                        tipoDocRepLegal = "4";
                        maxLengthRepLegal = 9;
                    }
                }

                for(SbDireccionGt dire : ccRegistro.getReprCliId().getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                        direccionRepLegal = dire.getDireccion();
                        ubigeoRepLegal = dire.getDistritoId();
                        //setDisabledDireccionRepLegal(true);
                        direccionRepLegalTemp = dire.getDireccion();
                    }
                }
                for(SbMedioContactoGt medio : ccRegistro.getReprCliId().getSbMedioContactoList()){
                    if(medio.getActivo() == 1){
                        if(medio.getTipoId().getCodProg().equals("TP_MEDCO_COR")){
                            correoRepLegal = medio.getValor();
                            //setDisabledTelefonoRepLegal(true);     
                            correoRepLegalTemp = medio.getValor();
                        }
                        if(medio.getTipoId().getCodProg().equals("TP_MEDCO_MOV")){
                            telefonoRepLegal = medio.getValor();
                            //setDisabledCorreoRepLegal(true);
                            telefonoRepLegalTemp = medio.getValor();
                        }
                    }
                }
            }
        }
        
        fechaInicioServicio = ccRegistro.getFechaInicio();
        
        strObservacion = ccRegistro.getMotivoFecha();
        blnEvento = (ccRegistro.getEvento() != null && ccRegistro.getEvento() == 1);
        if(blnEvento){
            renderEvento = true;
            if(ccRegistro.getFechaInicio() != null){
                horaInicioEvento = new Date(ccRegistro.getFechaInicio().getTime());    
            }
            if(ccRegistro.getFechaFin() != null){
                horaFinEvento = new Date(ccRegistro.getFechaFin().getTime());
            }
        }
        if(registro.getDetalleMotivo() != null && registro.getDetalleMotivo().trim().equals("MIGRACION")){
            esMigracion = true;
        }
        
        if(renderPnlVigilantes){
            Map nmap;
            if(ccRegistro.getSspCarteraVigilanteList() != null){
                for(SspCarteraVigilante vig : ccRegistro.getSspCarteraVigilanteList()){
                    if(vig.getActivo() == 1 && (vig.getActualizacion() == null || (vig.getActualizacion() != null && vig.getActualizacion().toString().equals("1"))) ){
                        if(vig.getCarneId() == null){
                            continue;
                        }
                        
                        nmap = new HashMap();
                        nmap.put("id", (vig.getId()!=null?vig.getId():JsfUtil.tempId()) );
                        nmap.put("nombres", vig.getCarneId().getVigilanteId().getNombresYApellidos() );
                        nmap.put("doc", vig.getCarneId().getVigilanteId().getNumDoc());
                        nmap.put("carne", ""+vig.getCarneId().getNroCarne());
                        nmap.put("carneId", ""+vig.getCarneId().getId());
                        nmap.put("carneEntity", vig.getCarneId());
                        AmaLicenciaDeUso licencia = ejbAmaLicenciaDeUsoFacade.obtenerLicenciaDeUso(ccRegistro.getAdministradoId().getId(), vig.getCarneId().getVigilanteId().getNumDoc() );
                        if(licencia != null){
                            nmap.put("licencia", licencia.getNroLicencia() );
                        }else{
                            nmap.put("licencia", "");
                        }
                        nmap.put("declarado", vig.getActualizacion());
                        nmap.put("motivoSeguridad", vig.getObservacion());
                        lstPersonalSeguridad.add(nmap);
                    }
                }
            }
        }
        
        for(SspCarteraEvento ce : ccRegistro.getSspCarteraEventoList()){
            if(ce.getActivo() == 1 && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")){
                lstObservaciones.add(ce);
            }
        }
    }

    public boolean completarDatosCarteraCliente(SspCarteraCliente registroOri){        
        boolean validacion = true;
        
        try {
            /// Consorcio ///
            if(registro.getSbPersonaList() != null && !registro.getSbPersonaList().isEmpty()){
                JsfUtil.borrarIds(registro.getSbPersonaList());
            }
            ////////////////
            if(renderPnlPersona){
                if(esNuevo){
                    registro.getClienteId().setId(null);
                }
                if(!renderDireccionRucCombo){
                    SbDireccionGt direc = new SbDireccionGt();
                    direc.setId(null);
                    direc.setActivo(JsfUtil.TRUE);
                    direc.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    direc.setAudNumIp(JsfUtil.getIpAddress());
                    direc.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DIRECB_FIS"));
                    direc.setViaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_VIA_CAL"));
                    direc.setZonaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_ZON_URB"));                   
                    direc.setPersonaId(registro.getClienteId());
                    direc.setPaisId(ejbSbPaisFacade.obtenerPaisByNombre("PERU"));
                    
                    if(getEsRuc()){
                        direc.setDireccion(direccionRuc);
                        direc.setDistritoId(ubigeoRuc);
                    }
                    if(getEsDni()){
                        direc.setDireccion(direccionDni);
                        direc.setDistritoId(ubigeoDni);
                    }
                    if(registro.getClienteId().getSbDireccionList() == null){
                        registro.getClienteId().setSbDireccionList(new ArrayList());
                    }
                    direc = (SbDireccionGt) JsfUtil.entidadMayusculas(direc, "");
                    registro.getClienteId().getSbDireccionList().add(direc);
                }else{
                    if(getEsRuc()){
                        direccionRucCombo.setDistritoId(ubigeoRuc);
                        registro.setDirecClienId(direccionRucCombo);     
                    }
                    if(getEsDni()){
                        direccionDniCombo.setDistritoId(ubigeoDni);
                        registro.setDirecClienId(direccionDniCombo);     
                    }
                }
                if(!Objects.equals(telefonoTemp, telefono)){
                    if(telefono != null && !telefono.isEmpty()){
                        SbMedioContactoGt medioTelefono = new SbMedioContactoGt();
                        medioTelefono.setActivo(JsfUtil.TRUE);
                        medioTelefono.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        medioTelefono.setAudNumIp(JsfUtil.getIpAddress());
                        medioTelefono.setId(null);
                        medioTelefono.setPersonaId(registro.getClienteId());
                        medioTelefono.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_MOV"));
                        medioTelefono.setValor(telefono);
                        medioTelefono.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                        if(registro.getClienteId().getSbMedioContactoList() == null){
                            registro.getClienteId().setSbMedioContactoList(new ArrayList());
                        }
                        medioTelefono = (SbMedioContactoGt) JsfUtil.entidadMayusculas(medioTelefono, "");
                        registro.getClienteId().getSbMedioContactoList().add(medioTelefono);
                    }else{
                        if(registro.getClienteId().getSbMedioContactoList() != null){
                            for(SbMedioContactoGt medioTelefono : registro.getClienteId().getSbMedioContactoList() ){
                                if(medioTelefono.getTipoId().getCodProg().equals("TP_MEDCO_MOV") && medioTelefono.getValor().equals(telefonoTemp) ){
                                    medioTelefono.setActivo(JsfUtil.FALSE);
                                }
                            }
                        }
                    }
                }
                if(!Objects.equals(correoTemp, correo)){
                    if(correo != null && !correo.isEmpty()){
                        SbMedioContactoGt medioCorreo = new SbMedioContactoGt();
                        medioCorreo.setActivo(JsfUtil.TRUE);
                        medioCorreo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        medioCorreo.setAudNumIp(JsfUtil.getIpAddress());
                        medioCorreo.setId(null);
                        medioCorreo.setPersonaId(registro.getClienteId());
                        medioCorreo.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_COR"));
                        medioCorreo.setValor(correo);
                        medioCorreo.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                        if(registro.getClienteId().getSbMedioContactoList() == null){
                            registro.getClienteId().setSbMedioContactoList(new ArrayList());
                        }
                        medioCorreo = (SbMedioContactoGt) JsfUtil.entidadMayusculas(medioCorreo, "");
                        registro.getClienteId().getSbMedioContactoList().add(medioCorreo);
                    }else{
                        if(registro.getClienteId().getSbMedioContactoList() != null){
                            for(SbMedioContactoGt medioCorreo : registro.getClienteId().getSbMedioContactoList() ){
                                if(medioCorreo.getTipoId().getCodProg().equals("TP_MEDCO_COR") && medioCorreo.getValor().equals(correoTemp) ){
                                    medioCorreo.setActivo(JsfUtil.FALSE);
                                }
                            }
                        }
                    }
                }   
            }else{
                registro.setClienteId(null);
            }
            if(renderPnlRepLegal){
                if(registro.getReprCliId() != null){
                    if(!Objects.equals(direccionRepLegalTemp, direccionRepLegal)){
                        if(direccionRepLegal != null && !direccionRepLegal.isEmpty()){
                            SbDireccionGt direc = new SbDireccionGt();
                            direc.setId(null);
                            direc.setActivo(JsfUtil.TRUE);
                            direc.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            direc.setAudNumIp(JsfUtil.getIpAddress());
                            direc.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DIRECB_FIS"));
                            direc.setViaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_VIA_CAL"));
                            direc.setZonaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_ZON_URB"));                   
                            direc.setDireccion(direccionRepLegal);
                            direc.setDistritoId(ubigeoRepLegal);
                            direc.setPersonaId(registro.getReprCliId());
                            direc.setPaisId(ejbSbPaisFacade.obtenerPaisByNombre("PERU"));

                            if(registro.getReprCliId().getSbDireccionList() == null){
                                registro.getReprCliId().setSbDireccionList(new ArrayList());
                            }
                            direc = (SbDireccionGt) JsfUtil.entidadMayusculas(direc, "");
                            registro.getReprCliId().getSbDireccionList().add(direc);
                        }
                    }else{
                        if(registro.getReprCliId() != null){
                            if(registro.getReprCliId().getSbDireccionList() != null){
                                for(SbDireccionGt direc : registro.getReprCliId().getSbDireccionList()){
                                    if(direc.getDireccion().trim().equals(direccionRepLegal)){
                                        direc.setDistritoId(ubigeoRepLegal);
                                    }
                                }
                            }
                        }
                    }
                    if(!Objects.equals(telefonoRepLegalTemp, telefonoRepLegal)){
                        if(telefonoRepLegal != null && !telefonoRepLegal.isEmpty()){
                            SbMedioContactoGt medioTelefono = new SbMedioContactoGt();
                            medioTelefono.setActivo(JsfUtil.TRUE);
                            medioTelefono.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            medioTelefono.setAudNumIp(JsfUtil.getIpAddress());
                            medioTelefono.setId(null);
                            medioTelefono.setPersonaId(registro.getReprCliId());
                            medioTelefono.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_MOV"));
                            medioTelefono.setValor(telefonoRepLegal);
                            medioTelefono.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                            if(registro.getReprCliId().getSbMedioContactoList() == null){
                                registro.getReprCliId().setSbMedioContactoList(new ArrayList());
                            }
                            medioTelefono = (SbMedioContactoGt) JsfUtil.entidadMayusculas(medioTelefono, "");
                            registro.getReprCliId().getSbMedioContactoList().add(medioTelefono);
                        }else{
                            if(registro.getReprCliId().getSbMedioContactoList() != null){
                                for(SbMedioContactoGt medioTelefono : registro.getReprCliId().getSbMedioContactoList() ){
                                    if(medioTelefono.getTipoId().getCodProg().equals("TP_MEDCO_MOV") && medioTelefono.getValor().equals(telefonoRepLegalTemp) ){
                                        medioTelefono.setActivo(JsfUtil.FALSE);
                                    }
                                }
                            }
                        }
                    }
                    if(!Objects.equals(correoRepLegalTemp, correoRepLegal)){
                        if(correoRepLegal != null && !correoRepLegal.isEmpty()){
                            SbMedioContactoGt medioCorreo = new SbMedioContactoGt();
                            medioCorreo.setActivo(JsfUtil.TRUE);
                            medioCorreo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            medioCorreo.setAudNumIp(JsfUtil.getIpAddress());
                            medioCorreo.setId(null);
                            medioCorreo.setPersonaId(registro.getReprCliId());
                            medioCorreo.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_MEDCO_COR"));
                            medioCorreo.setValor(correoRepLegal);
                            medioCorreo.setTipoPrioridad(ejbTipoBaseFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                            if(registro.getReprCliId().getSbMedioContactoList() == null){
                                registro.getReprCliId().setSbMedioContactoList(new ArrayList());
                            }
                            medioCorreo = (SbMedioContactoGt) JsfUtil.entidadMayusculas(medioCorreo, "");
                            registro.getReprCliId().getSbMedioContactoList().add(medioCorreo);
                        }else{
                            if(registro.getReprCliId().getSbMedioContactoList() != null){
                                for(SbMedioContactoGt medioCorreo : registro.getReprCliId().getSbMedioContactoList() ){
                                    if(medioCorreo.getTipoId().getCodProg().equals("TP_MEDCO_COR") && medioCorreo.getValor().equals(correoRepLegalTemp) ){
                                        medioCorreo.setActivo(JsfUtil.FALSE);
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                tipoDocRepLegal = null;
                numDocRepLegal = null;
                maxLengthRepLegal = 8;
                direccionRepLegal = null;
                ubigeoRepLegal = null;        
                correoRepLegal = null;
                telefonoRepLegal = null;
                setDisabledDireccionRepLegal(false);
                setDisabledCorreoRepLegal(false);
                setDisabledTelefonoRepLegal(false);
                registro.setReprCliId(null);
            }
            
            registro.setFechaInicio(fechaInicioServicio);
            if(!registro.getSspLugarServicioList().isEmpty()){
                JsfUtil.borrarIds(registro.getSspLugarServicioList());                
                
                if(!Objects.equals(archivoTemporal, archivo)){
                    if(file != null){
                        String fileName = file.getFileName();
                        String nombreFoto = "";
                        switch(registro.getTipoOpeId().getCodProg()){
                            case "TP_TDEC_NUEVO":
                                    nombreFoto = "CONTRATO_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_CCCLI") + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                                    break;
                            case "TP_TDEC_ADENDA":
                                    nombreFoto = "ADENDA_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_ACCLI") + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                                    break;
                            case "TP_TDEC_BAJA":
                                    nombreFoto = "TERMINO_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_TCCLI") + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                                    break;
                        }
                        documento = new SspDocumento();
                        documento.setActivo(JsfUtil.TRUE);
                        documento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        documento.setAudNumIp(JsfUtil.getIpAddress());
                        documento.setCartera(registro);
                        documento.setId(null);
                        documento.setNombre(nombreFoto);
                        
                        for(SspDocumento doc : registro.getSspDocumentoList()){
                            doc.setActivo(JsfUtil.FALSE);
                        }
                        documento = (SspDocumento) JsfUtil.entidadMayusculas(documento, "");
                        registro.getSspDocumentoList().add(documento);
                    }
                }
            }

            if(!renderPnlVigilantes){
                tipoDocSeguridad = null;
                numDocSeguridad = null;
                nombresSeguridad = null;
                nroCarneSeguridad = null;
                carneIdSeguridad = null;
                motivoSeguridad = null;
                lstPersonalSeguridad = new ArrayList();
                lstPersonalSeguridadMotivosEliminados = new ArrayList();
                listadoBusquedaPersonalSeguridad = new ArrayList();
                tipoDocSeguridadBusq = null;
                numDocSeguridadBusq = null;
                lstPersonalSeguridadSeleccionados = new ArrayList();
                
                if(estado == EstadoCrud.CREAR || estado == EstadoCrud.CREAR_ADENDA){
                    registro.setSspCarteraVigilanteList(new ArrayList());
                }else{
                    if(registro.getSspCarteraVigilanteList() != null){
                        for(SspCarteraVigilante cVig : registro.getSspCarteraVigilanteList()){
                            cVig.setActivo(JsfUtil.FALSE);
                        }
                    }else{
                        registro.setSspCarteraVigilanteList(new ArrayList());
                    }
                }
            }
            if(renderPnlArmas){
                JsfUtil.borrarIds(registro.getSspCarteraArmaList());
            }else{   
                tipoDocArmas = null;
                documentoArma = null;
                motivoArma = null;
                tarjetaPropiedad = null;
                lstArmasSeleccionadas = new ArrayList();
                
                if(estado == EstadoCrud.CREAR || estado == EstadoCrud.CREAR_ADENDA){
                    registro.setSspCarteraArmaList(new ArrayList());
                }else{
                    if(registro.getSspCarteraArmaList() != null){
                        for(SspCarteraArma cArma : registro.getSspCarteraArmaList()){
                            cArma.setActivo(JsfUtil.FALSE);
                        }
                    }
                }
            }
            if(renderPnlVehiculos){
                JsfUtil.borrarIds(registro.getSspCarteraVehiculoList());
            }else{
                vehiculo = new SspVehiculo();
                lstVehiculosSeleccionados = new ArrayList();
                lstVehiculosMotivosEliminados = new ArrayList();
                nroPlaca = null;
                motivoVehiculo = null;
                
                if(estado == EstadoCrud.CREAR || estado == EstadoCrud.CREAR_ADENDA){
                    registro.setSspCarteraVehiculoList(new ArrayList());
                }else{
                    if(registro.getSspCarteraVehiculoList() != null){
                        for(SspCarteraVehiculo cVeh : registro.getSspCarteraVehiculoList()){
                            cVeh.setActivo(JsfUtil.FALSE);
                        }
                    }
                }
            }
            
            if(estado == EstadoCrud.EDITAR || estado == EstadoCrud.EDITAR_ADENDA){                
                int cont = 0;
                
                if(!registroOri.getSspLugarServicioList().isEmpty()){                    
                    for(SspLugarServicio lsOri : registroOri.getSspLugarServicioList()){
                        cont = 0;
                        for(SspLugarServicio ls : registro.getSspLugarServicioList()){
                            if(Objects.equals(lsOri.getId(), ls.getId())){
                                cont++;
                            }
                        }
                        if(cont == 0){
                            lsOri.setActivo(JsfUtil.FALSE);
                            registro.getSspLugarServicioList().add(lsOri);
                        }
                    }
                }
                if(renderPnlArmas){
                    if(!registroOri.getSspCarteraArmaList().isEmpty()){
                        for(SspCarteraArma lsOri : registroOri.getSspCarteraArmaList()){
                            cont = 0;
                            for(SspCarteraArma ls : registro.getSspCarteraArmaList()){
                                if(ls.getActivo() == 1 && (ls.getActualizacion() == null || (ls.getActualizacion() != null && ls.getActualizacion().toString().equals("1")) ) ){
                                    if(Objects.equals(lsOri.getId(), ls.getId())){
                                        cont++;
                                    }
                                }   
                            }
                            if(cont == 0){
                                lsOri.setActivo(JsfUtil.FALSE);
                                registro.getSspCarteraArmaList().add(lsOri);
                            }
                        }
                    }
                }   
            }
        } catch (Exception e) {
            validacion = false;
            e.printStackTrace();
        }
        
        return validacion;
    }
    
    public boolean completarDatosCarteraClienteDeclaracion(SspCarteraCliente registroOri){
        boolean validacion = true;
        
        try {
            /// Consorcio ///
            if(registro.getSbPersonaList() != null && !registro.getSbPersonaList().isEmpty()){
                JsfUtil.borrarIds(registro.getSbPersonaList());
            }
            ////////////////
            if(!renderPnlVigilantes){
                tipoDocSeguridad = null;
                numDocSeguridad = null;
                nombresSeguridad = null;
                nroCarneSeguridad = null;
                carneIdSeguridad = null;
                motivoSeguridad = null;
                lstPersonalSeguridad = new ArrayList();
                lstPersonalSeguridadMotivosEliminados = new ArrayList();
                listadoBusquedaPersonalSeguridad = new ArrayList();
                tipoDocSeguridadBusq = null;
                numDocSeguridadBusq = null;
                lstPersonalSeguridadSeleccionados = new ArrayList();
                
                if(registro.getSspCarteraVigilanteList() != null){
                    for(SspCarteraVigilante cVig : registro.getSspCarteraVigilanteList()){
                        cVig.setActivo(JsfUtil.FALSE);
                    }
                }else{
                    registro.setSspCarteraVigilanteList(new ArrayList());
                }
            }
            if(renderPnlArmas){
                JsfUtil.borrarIds(registro.getSspCarteraArmaList());
            }else{   
                tipoDocArmas = null;
                documentoArma = null;
                motivoArma = null;
                tarjetaPropiedad = null;
                lstArmasSeleccionadas = new ArrayList();
                lstArmasMotivosEliminadas = new ArrayList();
                if(registro.getSspCarteraArmaList() != null){
                    for(SspCarteraArma cArma : registro.getSspCarteraArmaList()){
                        cArma.setActivo(JsfUtil.FALSE);
                    }
                }
            }
            if(renderPnlVehiculos){
               //
            }else{
                vehiculo = new SspVehiculo();
                nroPlaca = null;
                motivoVehiculo = null;
                lstVehiculosSeleccionados = new ArrayList();
                lstVehiculosMotivosEliminados = new ArrayList();
                if(registro.getSspCarteraVehiculoList() != null){
                    for(SspCarteraVehiculo cVeh : registro.getSspCarteraVehiculoList()){
                        cVeh.setActivo(JsfUtil.FALSE);
                    }
                }
            }
            
            if(strObservacion != null && (strObservacion.trim().length() > 400)){
                strObservacion = strObservacion.substring(0,400);
            }
            registro.setMotivoFecha((strObservacion != null && !strObservacion.isEmpty())?strObservacion.trim():null);
            
            if(estado == EstadoCrud.DECLARAR || estado == EstadoCrud.DECLARAR_ADENDA){
                int cont = 0;
                
                if(!registroOri.getSspLugarServicioList().isEmpty()){
                    for(SspLugarServicio lsOri : registroOri.getSspLugarServicioList()){
                        cont = 0;
                        for(SspLugarServicio ls : registro.getSspLugarServicioList()){
                            if(Objects.equals(lsOri.getId(), ls.getId())){
                                cont++;
                            }
                        }
                        if(cont == 0){
                            lsOri.setActivo(JsfUtil.FALSE);
                            registro.getSspLugarServicioList().add(lsOri);
                        }
                    }
                }
                if(renderPnlArmas){
                    if(!registroOri.getSspCarteraArmaList().isEmpty()){
                        for(SspCarteraArma lsOri : registroOri.getSspCarteraArmaList()){
                            cont = 0;
                            for(SspCarteraArma ls : registro.getSspCarteraArmaList()){
                                if(ls.getActivo() == 1 && (ls.getActualizacion() == null || (ls.getActualizacion() != null && ls.getActualizacion().toString().equals("1")) ) ){
                                    if(Objects.equals(lsOri.getId(), ls.getId())){
                                        cont++;
                                    }
                                }
                            }
                            if(cont == 0){
                                lsOri.setActualizacion('0');
                                lsOri.setFechaActualizacion(new Date());
                                for(Map vigEliminado : lstArmasMotivosEliminadas ){
                                    if(Objects.equals((Long) vigEliminado.get("armaId"), lsOri.getArmaId().getId())){
                                        lsOri.setObservacion(""+vigEliminado.get("motivoSeguridad"));
                                        break;
                                    }
                                }
                                
                                registro.getSspCarteraArmaList().add(lsOri);
                            }
                        }
                    }
                }   
            }
        } catch (Exception e) {
            validacion = false;
            e.printStackTrace();
        }
        return validacion;
    }
    
    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SspCarteraCliente getSspCarteraCliente(Long id) {
        return ejbSspCarteraClienteFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SspCarteraClienteController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sspCarteraClienteConverter")
    public static class SspCarteraClienteControllerConverterN extends SspCarteraClienteControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SspCarteraCliente.class)
    public static class SspCarteraClienteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SspCarteraClienteController c = (SspCarteraClienteController) JsfUtil.obtenerBean("sspCarteraClienteController", SspCarteraClienteController.class);
            return c.getSspCarteraCliente(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SspCarteraCliente) {
                SspCarteraCliente o = (SspCarteraCliente) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SspCarteraCliente.class.getName());
            }
        }
    }
    
    ////////////////////
    public void reiniciarCamposBusqueda(){
        tipoBuscarPor = null;
        filtro = null;
        tipoModalidad = null;
        tipoDeclaracion = null;
        fechaInicial = null;
        fechaFinal = null;
        estadoCartera = null;
        resultadosBandejaSeleccionados = new ArrayList();
        setDisabledBtnTransmitir(false);
    }
    
    public void reiniciarValores(){        
        registro = new SspCarteraCliente();
        resultadosBandejaSeleccionados = new ArrayList();        
        lstRepresentante = new ArrayList();
        lstObservaciones = new ArrayList();
        archivoTemporal = null;
        direccionRepLegalTemp = null;
        telefonoTemp = null;
        correoTemp = null;
        telefonoRepLegalTemp = null;
        correoRepLegalTemp = null;
        fechaFinDeclaracionTemp = null;
        strObservacion = null;
        esMigracion = false;
        esTransporteDinero = false;
        setRenderRepresentante(true);
        setRenderPnlPersona(false);
        setRenderPnlRepLegal(false);
        setRenderPnlArmas(false);
        setRenderPnlVehiculos(false);
        setRenderPnlVigilantes(false);
        limpiarDatosSeleccion();
        setRenderObservacionFechaFin(false);
        esDeclaracionCC = false;
        lstArmasMotivosEliminadas = new ArrayList();
    }
    
    public void limpiarDatos(){
        lstTipoDoc = new ArrayList();
    }
    
    public void limpiarDatosSeleccion(){
        setDisabledBusco(false);
        setDisabledBuscoRepLegal(false);
        setBlnBusco(false);
        setEsNuevo(true);
        setBlnEvento(false);
        setRenderEvento(false);
        tipoDoc = null;
        numDoc = null;
        rznSocial = null;
        correo = null;
        telefono = null;
        lstTipoDoc = new ArrayList();
        lstTipoDocConsorcio = new ArrayList();
        direccionRuc = null;
        direccionDni = null;
        ubigeoRuc = null;
        ubigeoDni = null;
        setRenderDireccionRucCombo(false);
        setRenderDireccionDniCombo(false);
        direccionRucCombo = null;
        direccionDniCombo = null;
        lstDireccionCombo = new ArrayList();
        setDisabledDireccion(false);
        setDisabledCorreo(false);
        setDisabledTelefono(false);
        registro.setClienteId(new SbPersonaGt());
        horaInicioEvento = null;
        horaFinEvento = null;
        /// Rep. Legal
        setBlnBuscoRepLegal(false);
        setEsNuevoRepLegal(true);
        tipoDocRepLegal = null;
        numDocRepLegal = null;
        direccionRepLegal = null;
        ubigeoRepLegal = null;        
        correoRepLegal = null;
        telefonoRepLegal = null;
        setDisabledDireccionRepLegal(false);
        setDisabledCorreoRepLegal(false);
        setDisabledTelefonoRepLegal(false);
        registro.setReprCliId(new SbPersonaGt());
        /// Datos del servicio
        lugarServicio = new SspLugarServicio();
        lugarSelected = new SspLugarServicio();
        mapCenter = "";
        lstServicioSeleccionados = new ArrayList();
        documento = null;
        fotoByte = null;
        file = null;
        foto = null;
        archivo = null;
        direccionServicio = null;
        fechaInicioServicio = null;
        /// Personal de seguridad
        tipoDocSeguridad = null;
        numDocSeguridad = null;
        nombresSeguridad = null;
        nroCarneSeguridad = null;
        carneIdSeguridad = null;
        motivoSeguridad = null;
        lstPersonalSeguridad = new ArrayList();
        listadoBusquedaPersonalSeguridad = new ArrayList();
        tipoDocSeguridadBusq = null;
        numDocSeguridadBusq = null;
        lstPersonalSeguridadSeleccionados = new ArrayList();        
        lstPersonalSeguridadMotivosEliminados = new ArrayList();
        /// Armas
        tipoDocArmas = null;
        documentoArma = null;
        tarjetaPropiedad = null;
        motivoArma = null;
        lstArmasSeleccionadas = new ArrayList();
        lstArmasMotivosEliminadas = new ArrayList();
        /// Vehiculos
        vehiculo = new SspVehiculo();
        lstVehiculosSeleccionados = new ArrayList();
        lstVehiculosMotivosEliminados = new ArrayList();
        nroPlaca = null;
        motivoVehiculo = null;
        /// Termino de Contrato
        esReferenciada = false;
        archivoTermino = null;
        
        // Adenda de Contrato
        archivoAdenda = null;
    }
    
    public void limpiarDatosImpresion(){
        tipoDoc = null;
        numDoc = null;
        rznSocial = null;
        correo = null;
        telefono = null;
        lstTipoDoc = new ArrayList();
        lstTipoDocConsorcio = new ArrayList();
        direccionRuc = null;
        direccionDni = null;
        ubigeoRuc = null;
        ubigeoDni = null;
        direccionRucCombo = null;
        direccionDniCombo = null;
        lstDireccionCombo = new ArrayList();
        /// Rep. Legal
        tipoDocRepLegal = null;
        numDocRepLegal = null;
        direccionRepLegal = null;
        ubigeoRepLegal = null;        
        correoRepLegal = null;
        telefonoRepLegal = null;
        /// Datos del servicio
        lugarServicio = new SspLugarServicio();
        lugarSelected = new SspLugarServicio();
        mapCenter = "";
        lstServicioSeleccionados = new ArrayList();
        documento = null;
        fotoByte = null;
        file = null;
        foto = null;
        archivo = null;
        direccionServicio = null;
        fechaInicioServicio = null;
        /// Personal de seguridad
        tipoDocSeguridad = null;
        numDocSeguridad = null;
        nombresSeguridad = null;
        nroCarneSeguridad = null;
        carneIdSeguridad = null;
        motivoSeguridad = null;
        lstPersonalSeguridad = new ArrayList();
        listadoBusquedaPersonalSeguridad = new ArrayList();
        tipoDocSeguridadBusq = null;
        numDocSeguridadBusq = null;
        lstPersonalSeguridadSeleccionados = new ArrayList();        
        lstPersonalSeguridadMotivosEliminados = new ArrayList();
        /// Armas
        tipoDocArmas = null;
        documentoArma = null;
        tarjetaPropiedad = null;
        motivoArma = null;
        lstArmasSeleccionadas = new ArrayList();
        lstArmasMotivosEliminadas = new ArrayList();
        /// Vehiculos
        vehiculo = new SspVehiculo();
        lstVehiculosSeleccionados = new ArrayList();
        lstVehiculosMotivosEliminados = new ArrayList();
        nroPlaca = null;
        motivoVehiculo = null;
        ////
        flagPnlArmasImpresion = false;
        flagPnlVigilantesImpresion = false;
        flagPnlVehiculosImpresion = false;
    }
    
    public String prepareList(){
        reiniciarCamposBusqueda();
        resultadosBandeja = null;
        tipoProc = "todos";
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        listarModalidadesCarteraCliente(true, p_user.getNumDoc());
        if(lstModalidades.isEmpty()){
            JsfUtil.mensajeError("Ud. no puede ingresar a esta opción porque no cuenta con modalidades disponibles");
            return null;
        }
        if(ejbSspCarteraClienteFacade.contContratosConObservaciones(p_user.getNumDoc()) > 0){
            JsfUtil.mensajeAdvertencia("Ud. tiene algunos registros de solicitudes observadas");
        }
        
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/gssp/sspCarteraCliente/List";
    }
    
    public Date getMaximafecha(){
        Calendar c_hoy = Calendar.getInstance();
        c_hoy.add(Calendar.DATE, 30);
        return c_hoy.getTime();
    }
    
    public Date getMinimafecha(){
        if(fechaInicioServicio != null){
            return fechaInicioServicio;
        }
        Calendar c_hoy = Calendar.getInstance();
        c_hoy.add(Calendar.DATE, 30);
        return c_hoy.getTime();
    }
    
    public Date getMinimafechaTermino(){
        if(registro != null && registro.getFechaInicio() != null){
            return registro.getFechaInicio();
        }
        return new Date();
    }
    
    public Date getMaximafechaTerminoFinActualizada(){
        if(registro != null && registro.getFechaFinActualiza() != null){
            return registro.getFechaFinActualiza();
        }
        return null;
    }
    
    public boolean generaExpedienteyTraza(Map reg, Integer usuaTramDoc, SbPersonaGt per, Integer proceso){
        boolean validacion = true, estadoTraza = true;        
        List<Integer> acciones = new ArrayList();
        String usuario = "";
        
        //// Si está en estado CREADO se genera el expediente y traza ////
        if(reg.get("ESTADO_CODPROG").equals("TP_ECC_CRE")){
            String asunto = ((reg.get("CLI_RZN_SOCIAL") != null)?(""+reg.get("CLI_RZN_SOCIAL").toString().trim()+" - RUC: "+reg.get("CLI_RUC")):(""+reg.get("CLI_NOMBRES").toString().trim()+" - "+reg.get("CLI_TIPO_DOC")+": "+reg.get("CLI_NUM_DOC")));
            String expediente = wsTramDocController.crearExpediente_gssp(null, per , asunto , proceso, usuaTramDoc, usuaTramDoc,"Contrato de Prestación de servicio");
            acciones.add(21);   // Procesado
            if(expediente != null){
                registro.setExpediente(Long.parseLong(expediente));
                registro.setFecha(new Date());
                
                SbDerivacionCydoc  derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionCyDoc(JsfUtil.getLoggedUser().getSistema(), ejbTipoBaseFacade.tipoBaseXCodProg("TP_GSSP_CCLI").getId(), ejbTipoBaseFacade.tipoBaseXCodProg("TP_AREA_TRAM").getId());
                if(derivacion == null){
                    usuario = JsfUtil.bundleBDIntegrado("sspCarteraCli_usuario_nuevoExpediente"); // En caso no exista ningun usuario parametrizado
                }else{
                    usuario = derivacion.getUsuarioDestinoId().getLogin();
                }
                
                estadoTraza = wsTramDocController.asignarExpediente(expediente, "USRWEB", usuario, asunto, "", acciones,false,null);
                if (!estadoTraza) {
                    JsfUtil.mensajeError("No se pudo generar la traza del expediente " + expediente);
                }else{
                    usuarioArchivar = JsfUtil.bundleBDIntegrado("sspCarteraCli_usuario_nuevoExpediente");
                }
            }else{
                validacion = false;
            }
        }
        //// Si está en estado OBSERVADO se crea una traza adicional ////
        if(reg.get("ESTADO_CODPROG").equals("TP_ECC_OBS")){
            String loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(registro.getExpediente().toString());
            String asunto = JsfUtil.bundleBDIntegrado("sspCarteraCli_mensaje_subsanacion") + registro.getExpediente().toString();
            acciones.add(21);   // Procesado
            estadoTraza = wsTramDocController.asignarExpediente(registro.getExpediente().toString(), loginUsuarioAct, loginUsuarioAct, asunto, "", acciones, false, null);
            if (!estadoTraza) {
                validacion = false;
                JsfUtil.mensajeError("No se pudo generar la traza del expediente " + registro.getExpediente().toString());
            }
        }
        /////////////////////////////////////////////////////////////////////
        return validacion;
    }

    public void cambiarContrato(){
        if(!resultadosBandejaSeleccionados.isEmpty()){            
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            Integer usuaTramDoc = ejbSbUsuarioFacadeGt.obtenerIdUsuarioTramDoc("USRWEB");
            SbPersonaGt per = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());            
            Integer proceso = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarteraCliente_nroProceso").getValor());
            String estadoCurso = "";
            int cont = 0;
            
            for(Map nmap : resultadosBandejaSeleccionados){
                registro = null;
                usuarioArchivar = "USRWEB";
                registro = ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+nmap.get("ID")));
                estadoCurso = registro.getEstadoId().getCodProg();
                //////
                registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_TRA"));
                if(registro.getSspCarteraEventoList() == null){
                    registro.setSspCarteraEventoList(new ArrayList());
                }
                adicionaCarteraEvento(registro.getEstadoId(), "");
                /////
                
                if(!generaExpedienteyTraza(nmap, usuaTramDoc, per, proceso)){
                    JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro Código: " + registro.getId());
                    continue;
                }
                if(!guardarDatosCartera()){
                    archivarExpediente(registro.getExpediente().toString(), usuarioArchivar, estadoCurso);
                    continue;
                }
                JsfUtil.mensaje("Registro con Código: "+ registro.getId() + " y expediente "+ registro.getExpediente() + " transmitido");
                cont++;
            }
            reiniciarCamposBusqueda();
            resultadosBandeja = new ArrayList();
            if(cont > 0){
                JsfUtil.mensaje("Se procesó "+ cont + " registro(s) correctamente.");
            }else{
                JsfUtil.mensaje("No se procesó ningún registro ");
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
    
    public boolean guardarDatosCartera(){
        try {
            if(registro.getEstadoId().getCodProg().equals("TP_ECC_TRA") && registro.getHashQr() == null){
                registro.setHashQr(JsfUtil.crearHash(registro.getExpediente()+ " - " + registro.getId()));
            }
            ejbSspCarteraClienteFacade.edit(registro);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void buscarBandeja(){
        boolean validacion = true;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        if(tipoProc.equals("todos")){
            if(tipoBuscarPor == null || (filtro == null || filtro.isEmpty()) ){
                if(tipoBuscarPor == null && (filtro == null || filtro.isEmpty())){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":buscarPor");
                    JsfUtil.invalidar(obtenerForm()+":filtroBusqueda");
                    JsfUtil.mensajeAdvertencia("Es necesario ingresar un tipo de búsqueda");
                }
                if((filtro == null || filtro.isEmpty()) && tipoModalidad == null){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":tipoModalidad");
                    JsfUtil.invalidar(obtenerForm()+":filtroBusqueda");
                    JsfUtil.mensajeAdvertencia("Es necesario ingresar el filtro a buscar");
                }
            }else{
                if(tipoBuscarPor != null && (filtro == null || filtro.isEmpty())){
                    validacion = false;
                    JsfUtil.invalidar(obtenerForm()+":filtroBusqueda");
                    JsfUtil.mensajeAdvertencia("Es necesario ingresar el campo a buscar");
                }
            }
        }
        if(validacion && p_user != null){
            SbPersonaGt administ = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
            vigEventos = null;
            
            HashMap mMap = new HashMap();
            mMap.put("fechaIni", fechaInicial);
            mMap.put("fechaFin", fechaFinal);
            mMap.put("tipoBuscarPor", tipoBuscarPor);
            mMap.put("filtro", ((filtro != null)?filtro.trim():null));
            mMap.put("administrado", administ.getId());
            mMap.put("tipoDeclaracion", (tipoDeclaracion != null)?tipoDeclaracion.getId():null);            
            if(tipoModalidad != null){
                if(tipoModalidad.getCodProg().equals("TP_MCO_VIG_EVT")){
                    mMap.put("tipoModalidad", ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_MCO_VIG").getId() );        
                    vigEventos = true;
                }else{
                    vigEventos = false;
                    mMap.put("tipoModalidad", (tipoModalidad != null)?tipoModalidad.getId():null);
                }
            }else{
                mMap.put("tipoModalidad", null);
            }
            mMap.put("vigEventos", vigEventos);
            if(tipoProc.equals("todos")){
                mMap.put("estadoCartera", (estadoCartera != null)?estadoCartera.getId():null);
            }else{
                mMap.put("estadoCartera", ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_OBS").getId());
            }
            resultadosBandeja = ejbSspCarteraClienteFacade.buscarCarteraClientes(mMap);
            reiniciarCamposBusqueda();
        }
    }
    
    public boolean renderBtnVer(Map reg){
        boolean validar = false;
        
        if(reg != null){
            if(reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_CRE") ||
               reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_OBS") ||
               reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_TRA") ||
               reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_APR") ||
               reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_NPR") ||
               reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_FDP") ||
               reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_DES")
               ){
                validar = true;
            }
        }
        
        return validar;
    }
    
    public boolean renderBtnEditar(Map reg){
        boolean validar = false;
        
        if(reg != null){
            if(reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_CRE") ||
               reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_OBS")
               ){
                validar = true;
            }
            if(reg.get("FLAG_EVALUACION") != null){
                validar = false; 
            }
        }        
        return validar;
    }
    
    public boolean renderBtnFechaContrato(Map reg){
        boolean validacion = false;
        
        if(reg != null){
            if(reg.get("FLAG_EVALUACION") != null){
                validacion = false;
            }else{
                if(!reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_NPR") && !reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_FDP") && !reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_DES") ){
                    if(( reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_NUEVO") || reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_ADENDA") ) && reg.get("FECHA_FIN") == null ){
                        validacion = true;
                    }
                }
            }
        }
        return validacion;
    }
    
    public boolean renderBtnBorrar(Map reg){
        boolean validar = false;
        if(reg != null){
            if(reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_CRE")){
                validar = true;
            }
            if(reg.get("FLAG_EVALUACION") != null){
                validar = false; 
            }
        }
        return validar;
    }    
    
    public boolean renderBtnTerminoContrato(Map reg){
        boolean validar = false;
        if(reg != null){
            if(reg.get("FLAG_EVALUACION") != null){
                validar = false; 
            }else{
                if(reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_APR") &&
                   (reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_NUEVO") || reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_ADENDA"))
                  ){
                    if(reg.get("FECHA_FIN") != null){
                        Calendar c_hoy = Calendar.getInstance();
                        Date fecVenc = (Date) reg.get("FECHA_FIN");
                        if(JsfUtil.getFechaSinHora(fecVenc).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) >= 0 ){
                            validar = true;
                        }
                    }else{
                        validar = true;
                    }
                }
            }
        }
        return validar;
    }
    
    public boolean renderBtnAdendaContrato(Map reg){
        boolean validar = false;
        if(reg != null){
            if(reg.get("FLAG_EVALUACION") != null){
                validar = false; 
            }else{
                if(reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_APR") &&
                   (reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_NUEVO") || reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_ADENDA"))
                  ){
                    validar = true;
//                    if(reg.get("FECHA_FIN") != null){
//                        Calendar c_hoy = Calendar.getInstance();
//                        Date fecVenc = (Date) reg.get("FECHA_FIN");
//                        if(JsfUtil.getFechaSinHora(fecVenc).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) >= 0 ){
//                            validar = true;    
//                        }
//                    }else{
//                        validar = true;
//                    }
                }
            }   
        }
        return validar;
    }
    
    public boolean renderBtnDeclaracionContrato(Map reg){
        boolean validar = false;
        if(reg != null){
            if(reg.get("FLAG_EVALUACION") != null){
                validar = false;
            }else{
                if(reg.get("DETALLE_MOTIVO") != null && reg.get("DETALLE_MOTIVO").toString().equals("MIGRACION")){
                    validar = false;
                    return validar;
                }
                
                if(reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_APR") &&
                   (reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_NUEVO") || reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_ADENDA"))
                  ){
                    if(reg.get("FECHA_FIN") == null){
                        validar = true;
                    }else{
                        Calendar hoy = Calendar.getInstance();
                        hoy.setTime((Date)reg.get("FECHA_FIN"));
                        hoy.add(Calendar.DATE, 2);
                        if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(hoy.getTime())) > 0 ){
                            validar = false;                            
                        }else{
                            validar = true;
                        }
                    }
                }
            }
        }
        return validar;
    }
    
    public boolean renderBtnImprimirConstancia(Map reg){
        boolean validar = false;
        
        if(reg != null){
            if((reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_TRA") || reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_APR") || reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_NPR")  ) &&
               (reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_NUEVO") || reg.get("TIPO_OPE_CODPROG").toString().equals("TP_TDEC_ADENDA"))
              ){
                validar = true;
            }
        }
        
        return validar;
    }
    
    public void borrarRegistro(Map reg){
        try {
            registro = ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+reg.get("ID")));
            registro.setActivo(JsfUtil.FALSE);
            ejbSspCarteraClienteFacade.edit(registro);
            
            if(registro.getContratoId() != null){
               registro.getContratoId().setFlagEvaluacion(null);
               ejbSspCarteraClienteFacade.edit(registro.getContratoId());
            }
            resultadosBandeja.remove(reg);

            JsfUtil.mensaje("Se ha borrado correctamente el registro.");
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    public List<TipoSeguridad> getLstEstadosCartera(){
        return ejbTipoSeguridadFacade.lstTiposEstadosBandejaCartera();
    }
    
    public void cambiarTipoBuscarPor(){
        tipoModalidad = null;
        filtro = null;
    }
    
    public List<TipoSeguridad> getLstTipoModalidad(){
        return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_MCO_TEC','TP_MCO_PRO','TP_MCO_TRA','TP_MCO_VIG','TP_MCO_SIS','TP_MCO_VIG_EVT'");
    }
    
    public List<TipoSeguridad> getLstTipoDeclaracion(){
        return ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs(" 'TP_TDEC_NUEVO','TP_TDEC_ADENDA' ");
    }
    
    public void seleccionarFilaTablaCarteraCli(SelectEvent event){
        eventoSeleccion();
    }
    
    public void deseleccionarFilaTablaCarteraCli(UnselectEvent event){
        eventoSeleccion();
    }
    
    public void toggleEventFilaCarteraCli(ToggleSelectEvent event){
        eventoSeleccion();
    }
    
    public void eventoSeleccion(){
        setDisabledBtnTransmitir(false);
        for (Map reg : resultadosBandejaSeleccionados) {
            if(!reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_CRE") && !reg.get("ESTADO_CODPROG").toString().equals("TP_ECC_OBS")){
                JsfUtil.mensajeAdvertencia("El cambio es sólo para los estados CREADO u OBSERVADO");
                setDisabledBtnTransmitir(true);
                return;
            }
        }
    }
    
    public String obtenerFechaReporte(Map reg){
        String nombre = "";
        if(reg != null){
            nombre = JsfUtil.formatoFechaDdMmYyyy((Date) reg.get("FECHA_INICIO"));
            
            if(reg.get("FECHA_FIN") != null){
                nombre += " - " + JsfUtil.formatoFechaDdMmYyyy((Date) reg.get("FECHA_FIN"));
            }else{
                nombre += " - *";
            }
        }
        
        return nombre;
    }
    
    public String obtenerAdministradorReporte(Map reg){
        String nombre = "";
        if(reg != null){
            if(reg.get("ADM_RUC") != null){
                nombre += reg.get("ADM_RZN_SOCIAL") + " - RUC: " +reg.get("ADM_RUC");
            }else{
                if(reg.get("ADM_TIPO_DOC") != null){
                    nombre += reg.get("ADM_NOMBRES") + " - "+ reg.get("ADM_TIPO_DOC") + ": " +reg.get("ADM_NUM_DOC");
                }else{
                    nombre += reg.get("ADM_NOMBRES") + " - DNI: " +reg.get("ADM_NUM_DOC");
                }
            }
        }
        return nombre;
    }
    
    public String obtenerClienteReporte(Map reg){
        String nombre = "";
        if(reg != null){
            if(reg.get("CLI_RUC") != null){
                nombre += reg.get("CLI_RZN_SOCIAL") + " - RUC: " +reg.get("CLI_RUC");
            }else{
                if(reg.get("CLI_TIPO_DOC") != null){
                    nombre += reg.get("CLI_NOMBRES") + " - "+ reg.get("CLI_TIPO_DOC") + ": " +reg.get("CLI_NUM_DOC");
                }else{
                    nombre += reg.get("CLI_NOMBRES") + " - DNI: " +reg.get("CLI_NUM_DOC");
                }
            }
        }
        return nombre;
    }
    
    public String obtenerModalidadReporte(Map reg){
        return ejbSspCarteraClienteFacade.obtenerModalidadByIdCarteraCli(Long.parseLong(""+reg.get("ID")));
    }
    
    /////////// CREATE ///////////
    
    public List<SbDireccionGt> getLstDirecciones(){
         List<SbDireccionGt> listado = new ArrayList();

        if (registro.getAdministradoId().getSbDireccionList() != null) {
            for (SbDireccionGt dire : registro.getAdministradoId().getSbDireccionList()) {
                if (dire.getActivo() == 1 && dire.getDistritoId().getId() != 0) { //distrito = 0 = MIGRACION
                    listado.add(dire);
                }
            }
        }

        return listado;
    }
    
    public void seleccionarFilaTablaModalidades(SelectEvent event){
        eventoSeleccionModalidad(registro, true);
    }
    
    public void deseleccionarFilaTablaModalidades(UnselectEvent event){
        eventoSeleccionModalidad(registro, true);
    }
    
    public void toggleEventFilaModalidades(ToggleSelectEvent event){
        eventoSeleccionModalidad(registro, true);
    }
    
    public void eventoSeleccionModalidad(SspCarteraCliente ccRegistro, boolean blnLimpiar){
        esTransporteDinero = false;
        renderEvento = false;
        setRenderPnlPersona(false);
        setRenderPnlRepLegal(false);
        setRenderPnlArmas(false);
        setRenderPnlVehiculos(false);
        setRenderPnlVigilantes(false);
        flagPnlArmasImpresion = false;
        flagPnlVigilantesImpresion = false;
        flagPnlVehiculosImpresion = false;
        
        if(blnLimpiar){
            limpiarDatos();
        }
        for(TipoSeguridad tipo : lstModalidades){
            tipo.setActivo(JsfUtil.TRUE);
        }
        
        if(ccRegistro.getTipoSeguridadList() != null){
            for (TipoSeguridad tipo : ccRegistro.getTipoSeguridadList()) {
                if(tipo.getActivo() == 1){
                    switch(tipo.getCodProg()){
                        case "TP_MCO_TEC":  // TECNOLOGÍA DE SEGURIDAD
                                setRenderPnlPersona(true);
                                break;
                        case "TP_MCO_PRO":  // PROTECCIÓN PERSONAL
                                setRenderPnlPersona(true);
                                setRenderPnlArmas(true);
                                setRenderPnlVigilantes(true);
                                flagPnlArmasImpresion = true;
                                flagPnlVigilantesImpresion = true;
                                break;
                        case "TP_MCO_TRA":  // TRANSPORTE DE DINERO Y VALORES
                                esTransporteDinero = true;
                                setRenderPnlPersona(true);                                
                                setRenderPnlArmas(true);
                                setRenderPnlVehiculos(true);
                                setRenderPnlVigilantes(true);
                                flagPnlArmasImpresion = true;
                                flagPnlVigilantesImpresion = true;
                                flagPnlVehiculosImpresion = true;
                                break;
                        case "TP_MCO_VIG":  // VIGILANCIA PRIVADA
                                renderEvento = true;
                                setRenderPnlPersona(true);
                                setRenderPnlArmas(true);
                                setRenderPnlVigilantes(true);
                                flagPnlArmasImpresion = true;
                                flagPnlVigilantesImpresion = true;
                                break;
                        case "TP_MCO_SIS":  // SERVICIOS INDIVIDUALES DE SEGURIDAD PERSONAL (SISPE)
                                setRenderPnlPersona(true);                            
                                setRenderPnlArmas(true);
                                flagPnlArmasImpresion = true;
                                break;
                        case "TP_MCO_EVE":  // EVENTUAL
                                setRenderPnlPersona(true);
                                setRenderPnlArmas(true);
                                setRenderPnlVigilantes(true);
                                flagPnlArmasImpresion = true;
                                flagPnlVigilantesImpresion = true;
                                break;
                    }
                }
            }
            
            if(esTransporteDinero){
                if(ccRegistro.getTodoRecursosBoolean()){
                    setRenderPnlArmas(false);
                    setRenderPnlVigilantes(false);
                    setRenderPnlVehiculos(false);
                }
                if(ccRegistro.getTipoSeguridadList().size() > 1){
                    for (TipoSeguridad tipo : ccRegistro.getTipoSeguridadList()) {
                        switch(tipo.getCodProg()){
                            case "TP_MCO_TEC":  // TECNOLOGÍA DE SEGURIDAD
                                    break;
                            case "TP_MCO_PRO":  // PROTECCIÓN PERSONAL
                                    setRenderPnlArmas(true);
                                    setRenderPnlVigilantes(true);
                                    break;
                            case "TP_MCO_TRA":  // TRANSPORTE DE DINERO Y VALORES
                                    if(ccRegistro.getTodoRecursosBoolean()){
                                        setRenderPnlVehiculos(false);
                                    }else{
                                        setRenderPnlVehiculos(true);
                                    }
                                    break;
                            case "TP_MCO_VIG":
                                    setRenderPnlArmas(true);
                                    setRenderPnlVigilantes(true);
                                    break;
                            case "TP_MCO_SIS":
                                    setRenderPnlArmas(true);
                                    break;
                            case "TP_MCO_EVE":
                                    setRenderPnlArmas(true);
                                    setRenderPnlVigilantes(true);
                                    break;
                        }
                    }
                }else{
                    if(ccRegistro.getTodoRecursosBoolean()){
                        setRenderPnlArmas(false);
                        setRenderPnlVigilantes(false);
                        setRenderPnlVehiculos(false);
                    }else{
                        setRenderPnlArmas(true);
                        setRenderPnlVigilantes(true);
                        setRenderPnlVehiculos(true);
                    }
                }
            }
            
            Map nmap = new HashMap();
            nmap.put("id", 1);
            nmap.put("nombre", "Persona Jurídica con RUC");
            lstTipoDoc.add(nmap);
            lstTipoDocConsorcio.add(nmap);
            nmap = new HashMap();
            nmap.put("id", 2);
            nmap.put("nombre", "Persona Natural con RUC");
            lstTipoDoc.add(nmap);
            lstTipoDocConsorcio.add(nmap);
            nmap = new HashMap();
            nmap.put("id", 3);
            nmap.put("nombre", "Persona Natural con DNI");
            lstTipoDoc.add(nmap);
            nmap = new HashMap();
            nmap.put("id", 4);
            nmap.put("nombre", "Persona Natural con CE");
            lstTipoDoc.add(nmap);
 
            
            //Validación adicional
            if(numDoc != null && !numDoc.isEmpty()){
                if(numDoc.startsWith("1")){  // Ruc que inicia con 1 es Natural Juridica
                    tipoDoc = "2";
                }else{
                    setRenderPnlRepLegal(true);
                    tipoDoc = "1";
                }
            }else{
                setRenderPnlRepLegal(false);
            }
            ///
        }else{
            setRenderPnlRepLegal(false);
        }
    }
    
    public boolean getEsRuc(){
        boolean valida = false;
        if(tipoDoc != null && !tipoDoc.isEmpty()){
            if(tipoDoc.equals("1") || tipoDoc.equals("2") ){
                valida = true;
            }
        }        
        return valida;
    }

    public boolean getEsDni(){
        boolean valida = false;
        if(tipoDoc != null && !tipoDoc.isEmpty()){
            if(tipoDoc.equals("3") || tipoDoc.equals("4") ){
                valida = true;
            }
        }        
        return valida;
    }
    
    public void cambiarTipoDoc(){
        registro.setClienteId(new SbPersonaGt());
        setBlnBusco(false);
        setRenderPnlRepLegal(false);
        if(tipoDoc != null && !tipoDoc.isEmpty()){            
            switch(tipoDoc){
                case "1":
                        maxLength = 11;
                        setRenderPnlRepLegal(true);
                        break;
                case "2": 
                        maxLength = 11;   
                        break;
                case "3": 
                        maxLength = 8;
                        break;
                case "4": 
                        maxLength = 9;
                        break;
            }
            direccionRuc = null;
            direccionDni = null;
            ubigeoRuc = null;
            ubigeoDni = null;
            telefono = "";
            correo = "";
            numDoc = "";
            
        }else{
            JsfUtil.mensajeAdvertencia("Por favor ingrese el tipo de documento");
        }
        RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosRepLegal");
        RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosCliente");
    }
    
    public String obtenerTipoDocView(String tipo){
        String nombre = "";
        if(tipo != null){
            switch(tipo){
                case "1":
                        nombre = "Persona Jurídica con RUC";
                        break;
                case "2":
                        nombre = "Persona Natural con RUC";
                        break;
                case "3":
                        nombre = "Persona Natural con DNI";
                        break;
                case "4":
                        nombre = "Persona Natural con CE";
                        break;
            }
        }
            
        return nombre;
    }
    
    public String obtenerForm(){
        switch(estado){
            case CREAR:
                        return "createForm";
            case EDITAR:
                        return "editForm";
            case VER:
                        return "verForm";
            case DECLARAR:
            case DECLARAR_ADENDA:
                        return "declararForm";
            case CREAR_TERMINO:
                        return "createTerminoForm";
            case EDITAR_TERMINO:
                        return "editTerminoForm";
            case VER_TERMINO:
                        return "verTerminoForm";
            case CREAR_ADENDA:
                        return "createAdendaForm";
            case EDITAR_ADENDA:
                        return "editAdendaForm";
            case VER_ADENDA:
                        return "verAdendaForm";
            case BUSCAR:
                        return "listForm";
        }
        return null;
    }
    
    public List<SbDistritoGt> getListDistritos() {
        return ejbSbDistritoFacade.listarDistritos();
    }
    
    public List<TipoBaseGt> getLstGeneros(){
        return ejbTipoBaseFacade.lstTipoBase("TP_GEN");
    }
    
    public void buscarDatosCliente(){
        boolean validacion = true;
        if(tipoDoc == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor seleccione el tipo de cliente");
        }
        if(numDoc == null || numDoc.isEmpty()){
            validacion = false;            
            if(tipoDoc != null){
                if(tipoDoc.equals("1") || tipoDoc.equals("2")){
                    JsfUtil.mensajeError("Por favor ingresar el ruc");    
                }else{
                    JsfUtil.mensajeError("Por favor ingresar el número de documento");
                }
            }else{
                JsfUtil.mensajeError("Por favor ingresar el número de documento");
            }
        }else{
            numDoc = numDoc.trim();
        }
        if(validacion){
            switch(tipoDoc){
                case "1":
                        if (numDoc.trim().length() != 11) {
                            JsfUtil.mensajeAdvertencia("El ruc es de 11 dígitos ");
                            validacion = false;                
                        }
                        if(!numDoc.startsWith("2")){
                            JsfUtil.mensajeAdvertencia("El ruc de persona jurídica no puede iniciar un dígito diferente de 2 ");
                            validacion = false;    
                        }
                        break;
                case "2":
                        if (numDoc.trim().length() != 11) {
                            JsfUtil.mensajeAdvertencia("El ruc es de 11 dígitos ");
                            validacion = false;                
                        }
                        if(!numDoc.startsWith("1")){
                            JsfUtil.mensajeAdvertencia("El ruc de persona natural no puede iniciar un dígito diferente de 1 ");
                            validacion = false;    
                        }
                        break;
                case "3":
                        if (numDoc.trim().length() != 8) {
                            JsfUtil.mensajeAdvertencia("El dni es de 8 dígitos ");
                            validacion = false;                
                        }
                        break;
                case "4":
                        if (numDoc.trim().length() != 9) {
                            JsfUtil.mensajeAdvertencia("El carnet de extranjería es de 9 dígitos ");
                            validacion = false;                
                        }
                        break;
            }
        }
        
        if(validacion){
            setDisabledBusco(false);
            setDisabledDireccion(false);
            setDisabledCorreo(false);
            setDisabledTelefono(false);
            setRenderDireccionDniCombo(false);
            setRenderDireccionRucCombo(false);
            setBlnBusco(true);
            setEsNuevo(false);
            boolean encontroSucamec = false;
            SbPersonaGt personaTemp = null;
            registro.setClienteId(new SbPersonaGt());
            direccionRuc = null;
            direccionDni = null;
            ubigeoRuc = null;
            ubigeoDni = null;
            correo = null;
            telefono = null;
            direccionRucCombo = null;
            direccionDniCombo = null;
            lstDireccionCombo = new ArrayList();
            
            personaTemp = ejbSbPersonaFacade.buscarPersonaSel(null, numDoc);
            if(personaTemp != null){    // Buscando en registros de sucamec
                registro.setClienteId(personaTemp);
                encontroSucamec = true;
                setDisabledBusco(true);
                JsfUtil.mensaje("Se encontró datos de la persona en el sistema");
            }
            
            if(!encontroSucamec){
                setEsNuevo(true);
                registro.getClienteId().setActivo(JsfUtil.TRUE);
                registro.getClienteId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.getClienteId().setAudNumIp(JsfUtil.getIpAddress());
                
                switch(tipoDoc){
                    case "1":
                    case "2":
                            // RUC
                            registro.getClienteId().setRuc(numDoc);
                            personaTemp = wsPideController.buscarSunat(registro.getClienteId());
                            if(personaTemp != null){
                                if(personaTemp.getRznSocial() != null){
                                    setDisabledBusco(true);
                                    registro.getClienteId().setNumDoc(null);
                                    registro.getClienteId().setApePat(null);
                                    registro.getClienteId().setApeMat(null);
                                    registro.getClienteId().setNombres(null);
                                    registro.getClienteId().setRznSocial(personaTemp.getRznSocial());
                                    registro.getClienteId().setGeneroId(null);
                                    registro.getClienteId().setFechaNac(null);
                                }else{
                                    setEsNuevo(false);
                                    setBlnBusco(false);
                                    JsfUtil.mensajeAdvertencia("No se encontró datos de la empresa en la SUNAT");
                                }
                            }else{
                                setEsNuevo(false);
                                setBlnBusco(false);
                                JsfUtil.mensajeAdvertencia("El servicio de la SUNAT no está disponible");
                            }
                            registro.getClienteId().setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_RUC"));
                            if(tipoDoc.equals("1")){ // Juridica con RUC
                                registro.getClienteId().setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_JUR"));
                            }else{
                                registro.getClienteId().setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                            }
                            break;
                    case "3":
                            // DNI
                            registro.getClienteId().setNumDoc(numDoc);
                            personaTemp = wsPideController.buscarReniec(registro.getClienteId());
                            if(personaTemp != null){
                                if(personaTemp.getApePat() != null){
                                    setDisabledBusco(true);
                                    registro.getClienteId().setApePat(personaTemp.getApePat());
                                    registro.getClienteId().setApeMat(personaTemp.getApeMat());
                                    registro.getClienteId().setNombres(personaTemp.getNombres());
                                    registro.getClienteId().setTipoDoc(personaTemp.getTipoDoc());
                                    registro.getClienteId().setTipoId(personaTemp.getTipoId());
                                    registro.getClienteId().setGeneroId(personaTemp.getGeneroId());
                                    registro.getClienteId().setFechaNac(personaTemp.getFechaNac());
                                    JsfUtil.mensajeAdvertencia("Se encontró datos de la persona en el RENIEC");
                                }else{
                                    registro.getClienteId().setApePat(null);
                                    registro.getClienteId().setApeMat(null);
                                    registro.getClienteId().setNombres(null);
                                    setEsNuevo(false);
                                    setBlnBusco(false);
                                    JsfUtil.mensajeAdvertencia("No se encontró datos de la persona en el RENIEC");
                                }
                            }else{
                                registro.getClienteId().setApePat(null);
                                registro.getClienteId().setApeMat(null);
                                registro.getClienteId().setNombres(null);
                                setEsNuevo(false);
                                setBlnBusco(false);
                                JsfUtil.mensajeAdvertencia("El servicio del RENIEC no está disponible");
                            }
                            registro.getClienteId().setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                            registro.getClienteId().setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                            break;
                    case "4":
                            //CE
                            registro.getClienteId().setNumDoc(numDoc);
                            registro.getClienteId().setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_CE"));
                            registro.getClienteId().setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                            registro.getClienteId().setApePat(null);
                            registro.getClienteId().setApeMat(null);
                            registro.getClienteId().setNombres(null);
                            break;
                }
            }
            if(registro.getClienteId().getId() != null){
                for(SbDireccionGt dire : registro.getClienteId().getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                        direccionRuc = dire.getDireccion();
                        direccionDni = dire.getDireccion();
                        ubigeoRuc = dire.getDistritoId();
                        ubigeoDni = dire.getDistritoId();
                        //setDisabledDireccion(true)
                        lstDireccionCombo.add(dire);
                    }
                }
                if(!lstDireccionCombo.isEmpty()){
                    setRenderDireccionRucCombo(true);
                    setRenderDireccionDniCombo(true);
                }
                for(SbMedioContactoGt medio : registro.getClienteId().getSbMedioContactoList()){
                    if(medio.getActivo() == 1){
                        if(medio.getTipoId().getCodProg().equals("TP_MEDCO_COR")){
                            correo = medio.getValor();
                            correoTemp = medio.getValor();
                            //setDisabledCorreo(true);
                        }
                        if(medio.getTipoId().getCodProg().equals("TP_MEDCO_MOV")){
                            telefono = medio.getValor();
                            telefonoTemp = medio.getValor();
                            //setDisabledTelefono(true);
                        }
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosCliente");
    }
    
    /// Rep. Legal ///
    public List<Map> getLstTipoDocRepLegal(){
        List<Map> lstDoc = new ArrayList();
        Map nmap = new HashMap();
        nmap.put("id", 3);
        nmap.put("nombre", "DNI");
        lstDoc .add(nmap);
        nmap = new HashMap();
        nmap.put("id", 4);
        nmap.put("nombre", "CE");
        lstDoc .add(nmap);
        return lstDoc;
    }
    
    public void cambiarTipoDocRepLegal(){
        registro.setReprCliId(new SbPersonaGt());
        setBlnBuscoRepLegal(false);
        setRenderPnlRepLegalReferenciado(false);
        if(tipoDocRepLegal != null && !tipoDocRepLegal.isEmpty()){
            switch(tipoDocRepLegal){
                case "1":
                        maxLengthRepLegal = 11;
                        setRenderPnlRepLegalReferenciado(true);
                        break;
                case "2": 
                        maxLengthRepLegal = 11;
                        break;
                case "3": 
                        maxLengthRepLegal = 8;
                        break;
                case "4": 
                        maxLengthRepLegal = 9;
                        break;
                default:
                        maxLengthRepLegal = 8;
                        break;
            }
            direccionRepLegal = null;
            ubigeoRepLegal = null;
            telefonoRepLegal = "";
            correoRepLegal = "";
            numDocRepLegal = "";
        }else{
            JsfUtil.mensajeAdvertencia("Por favor ingrese el tipo de documento");
        }
        RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosRepLegal");
    }
    
    public void buscarDatosClienteRepLegal(){
        boolean validacion = true;
        if(tipoDocRepLegal == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor seleccione el tipo de documento");
        }
        if(numDocRepLegal == null || numDocRepLegal.isEmpty()){
            validacion = false;            
            JsfUtil.mensajeError("Por favor ingresar el número de documento");
        }else{
            numDocRepLegal = numDocRepLegal.trim();
        }
        if(validacion){
            switch(tipoDocRepLegal){
                case "3":
                        if (numDocRepLegal.trim().length() != 8) {
                            JsfUtil.mensajeAdvertencia("El dni es de 8 dígitos ");
                            validacion = false;                
                        }
                        break;
                case "4":
                        if (numDocRepLegal.trim().length() != 9) {
                            JsfUtil.mensajeAdvertencia("El carnet de extranjería es de 9 dígitos ");
                            validacion = false;                
                        }
                        break;
            }
        }
        
        if(validacion){
            setDisabledBuscoRepLegal(false);
            setBlnBuscoRepLegal(true);
            setEsNuevoRepLegal(false);
            setDisabledDireccionRepLegal(false);
            setDisabledTelefonoRepLegal(false);
            setDisabledCorreoRepLegal(false);
            boolean encontroSucamec = false;
            SbPersonaGt personaTemp = null;
            registro.setReprCliId(new SbPersonaGt());
            direccionRepLegal = null;
            ubigeoRepLegal = null;
            correoRepLegal = null;
            telefonoRepLegal = null;
            
            personaTemp = ejbSbPersonaFacade.buscarPersonaSel(null, numDocRepLegal);
            if(personaTemp != null){    // Buscando en registros de sucamec
                registro.setReprCliId(personaTemp);
                encontroSucamec = true;
                setDisabledBuscoRepLegal(true);
                JsfUtil.mensaje("Se encontró datos del representante en el sistema");
            }
            
            if(!encontroSucamec){
                setEsNuevoRepLegal(true);
                registro.getReprCliId().setActivo(JsfUtil.TRUE);
                registro.getReprCliId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.getReprCliId().setAudNumIp(JsfUtil.getIpAddress());
                
                switch(tipoDocRepLegal){
                    case "3":
                            // DNI
                            registro.getReprCliId().setNumDoc(numDocRepLegal);
                            personaTemp = wsPideController.buscarReniec(registro.getReprCliId());
                            if(personaTemp != null){
                                if(personaTemp.getApePat() != null){
                                    setDisabledBuscoRepLegal(true);
                                    registro.getReprCliId().setApePat(personaTemp.getApePat());
                                    registro.getReprCliId().setApeMat(personaTemp.getApeMat());
                                    registro.getReprCliId().setNombres(personaTemp.getNombres());
                                    registro.getReprCliId().setTipoDoc(personaTemp.getTipoDoc());
                                    registro.getReprCliId().setTipoId(personaTemp.getTipoId());
                                    registro.getReprCliId().setGeneroId(personaTemp.getGeneroId());
                                    registro.getReprCliId().setFechaNac(personaTemp.getFechaNac());
                                    JsfUtil.mensaje("Se encontró datos del representante en el RENIEC");
                                }else{
                                    registro.getReprCliId().setApePat(null);
                                    registro.getReprCliId().setApeMat(null);
                                    registro.getReprCliId().setNombres(null);
                                    setEsNuevoRepLegal(false);
                                    setBlnBuscoRepLegal(false);
                                    JsfUtil.mensajeAdvertencia("No se encontró datos de la persona en el RENIEC");
                                }
                            }else{
                                registro.getReprCliId().setApePat(null);
                                registro.getReprCliId().setApeMat(null);
                                registro.getReprCliId().setNombres(null);
                                setEsNuevoRepLegal(false);
                                setBlnBuscoRepLegal(false);
                                JsfUtil.mensajeAdvertencia("El servicio del RENIEC no está disponible");
                            }
                            registro.getReprCliId().setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                            registro.getReprCliId().setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                            break;
                    case "4":
                            //CE
                            registro.getReprCliId().setNumDoc(numDocRepLegal);
                            registro.getReprCliId().setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_CE"));
                            registro.getReprCliId().setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                            registro.getReprCliId().setApePat(null);
                            registro.getReprCliId().setApeMat(null);
                            registro.getReprCliId().setNombres(null);
                            break;
                }
            }
            if(registro.getReprCliId().getId() != null){
                for(SbDireccionGt dire : registro.getReprCliId().getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                        direccionRepLegal = dire.getDireccion();
                        ubigeoRepLegal = dire.getDistritoId();
                        //setDisabledDireccionRepLegal(true);
                        direccionRepLegalTemp = dire.getDireccion();
                    }
                }
                for(SbMedioContactoGt medio : registro.getReprCliId().getSbMedioContactoList()){
                    if(medio.getActivo() == 1){
                        if(medio.getTipoId().getCodProg().equals("TP_MEDCO_COR")){
                            correoRepLegal = medio.getValor();
                            //setDisabledTelefonoRepLegal(true);
                            correoRepLegalTemp = medio.getValor();
                        }
                        if(medio.getTipoId().getCodProg().equals("TP_MEDCO_MOV")){
                            telefonoRepLegal = medio.getValor();
                            //setDisabledCorreoRepLegal(true);
                            telefonoRepLegalTemp = medio.getValor();
                        }
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosRepLegal");        
    }
    
    /// Servicio ///
    public void openDlgMapa(){
        if(lugarServicio.getDistId() == null){
            JsfUtil.invalidar(obtenerForm()+":ubigeoServicio");
            JsfUtil.mensajeError("Por favor seleccione primero el ubigeo del lugar de servicio");
            return;
        }
            
        modelMap = new DefaultMapModel();
        RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').show()");
        RequestContext.getCurrentInstance().update("frmMapa");
    }
    
    public void updateFormMapa(){        
        RequestContext.getCurrentInstance().update("frmMapa");
        RequestContext.getCurrentInstance().execute("startProcessMap()");
    }
    
    public void confirmarMapa(){
        if(lugarSelected.getLatitud() != null && lugarSelected.getLongitud() != null){
            lugarServicio.setLatitud(lugarSelected.getLatitud());
            lugarServicio.setLongitud(lugarSelected.getLongitud());
            RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').hide()");
            RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosServicio");

            if(estado == EstadoCrud.CREAR_ADENDA){
                latitud = lugarSelected.getLatitud();
                longitud = lugarSelected.getLongitud();
                RequestContext.getCurrentInstance().update("frmEditarLugarServicio");
            }            
        }else{
            JsfUtil.mensajeError("Por favor seleccione un punto en el mapa");
        }
    }
    
    public void cancelarMapa(){
        lugarSelected.setLatitud(lugarServicio.getLatitud());
        lugarSelected.setLongitud(lugarServicio.getLongitud());
        RequestContext.getCurrentInstance().update("frmMapa");
        RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').hide()");
    }
    
    public void onPointSelect(PointSelectEvent event){
        LatLng latlng = event.getLatLng();
        lugarSelected.setLatitud(""+latlng.getLat());
        lugarSelected.setLongitud(""+latlng.getLng());
        
        LatLng coord1 = new LatLng(latlng.getLat(), latlng.getLng());
        //Basic marker
        modelMap = new DefaultMapModel();
        modelMap.addOverlay(new Marker(coord1, "punto"));
        
        JsfUtil.mensaje("Punto seleccionado. Latitud: "+ latlng.getLat()+", Longitud: "+latlng.getLng());
    }
    
    public void onGeocode(GeocodeEvent event) {
        List<GeocodeResult> results = event.getResults();
         
        if (results != null && !results.isEmpty()) {
            modelMap = new DefaultMapModel();                    
             
            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                
                LatLng center = results.get(0).getLatLng();
                lugarSelected.setLatitud(""+center.getLat());
                lugarSelected.setLongitud(""+center.getLng());
                modelMap.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
                break;
            }
        }
    }
    
    public void agregarServicio(){
        boolean validacion = true;
        if(direccionServicio == null || direccionServicio.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar la dirección del servicio");
            JsfUtil.invalidar(obtenerForm()+":direccionServicio");
        }
        if(lugarServicio.getDistId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar el ubigeo del servicio");
            JsfUtil.invalidar(obtenerForm()+":ubigeoServicio");
        }
        if(lugarServicio.getLatitud() == null || lugarServicio.getLatitud().isEmpty() || lugarServicio.getLongitud() == null || lugarServicio.getLongitud().isEmpty() ){
            if ("1".equals(JsfUtil.bundleBDIntegrado("sspCarteraCli_GoogleMapsActivo"))){
                validacion = false;
                JsfUtil.mensajeError("Por favor seleccionar una ubicación en el mapa");
            }
        }
        if(validacion){            
            lugarServicio.setActivo(JsfUtil.TRUE);
            lugarServicio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            lugarServicio.setAudNumIp(JsfUtil.getIpAddress());
            lugarServicio.setDireccion(direccionServicio.toUpperCase());
            lugarServicio.setCarteraId(registro);
            lugarServicio.setId(JsfUtil.tempId());
            lugarServicio = (SspLugarServicio) JsfUtil.entidadMayusculas(lugarServicio, "");
            registro.getSspLugarServicioList().add(lugarServicio);
            
            lugarServicio = new SspLugarServicio();
            direccionServicio = "";
            fechaInicioServicio = null;
        }
    }
    
    public void eliminarServiciosSeleccionados(){
        if(!lstServicioSeleccionados.isEmpty()){
            for(SspLugarServicio lug: lstServicioSeleccionados){
                for(SspLugarServicio lugAct: registro.getSspLugarServicioList()){
                    if(Objects.equals(lug, lugAct)){
                        registro.getSspLugarServicioList().remove(lugAct);
                        break;
                    }
                }
            }
            lstServicioSeleccionados = new ArrayList();
            JsfUtil.mensaje("Registro(s) eliminado(s) correctamente");
        }else{
            JsfUtil.mensajeError("Por favor seleccione al menos un lugar de servicio");
        }
    }
    
    public void openDlgVerMapa(SspLugarServicio lugar){
        if(lugar.getLatitud() != null && lugar.getLongitud() != null){
            lugarSelected = lugar;
            RequestContext.getCurrentInstance().execute("PF('wvDlgVerMapa').show()");
            RequestContext.getCurrentInstance().update("frmVerMapa");
        }else{
            JsfUtil.mensajeAdvertencia("Este registro no tiene el mapa guardado");
        }
    }
    
    public String obtenerMapCenter(){
        if(lugarSelected != null){
            return lugarSelected.getLatitud() + ", " + lugarSelected.getLongitud();
        }
        return null;
    }
    
    public String obtenerMapCenterBuscarMap(){
        if(lugarSelected != null){
            if(lugarSelected.getLatitud() != null && lugarSelected.getLongitud() != null){
                LatLng coord1 = new LatLng(Double.parseDouble(lugarSelected.getLatitud()), Double.parseDouble(lugarSelected.getLongitud()));
                //Basic marker
                modelMap = new DefaultMapModel();
                modelMap.addOverlay(new Marker(coord1, "punto"));
                return lugarSelected.getLatitud() + ", " + lugarSelected.getLongitud();
            }
            return ejbSbParametroFacade.obtenerParametroXNombre("coordenadas_sucamec").getValor();
        }else{
            return ejbSbParametroFacade.obtenerParametroXNombre("coordenadas_sucamec").getValor();
        }
    }

    public MapModel getSimpleModel() {
        MapModel simpleModel = new DefaultMapModel();
          
        if(lugarSelected != null){
            if(lugarSelected.getLatitud() != null && lugarSelected.getLongitud() != null){
                //Shared coordinates
                LatLng coord1 = new LatLng(Double.parseDouble(lugarSelected.getLatitud()), Double.parseDouble(lugarSelected.getLongitud()));
                //Basic marker
                simpleModel.addOverlay(new Marker(coord1, "punto"));
            }
        }
        return simpleModel;
    }
    
    public void handleFileUploadAdjunto(FileUploadEvent event){
        try {
            if (event != null) {
                file = event.getFile();
                if (JsfUtil.verificarPDF(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    foto = new DefaultStreamedContent(file.getInputstream(), "application/pdf");
                    archivo = file.getFileName();
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de cargar no tiene formato PDF");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //////////////
    
    /// Personal de seguridad
    public String obtenerTipoDocSeguridad(){
        String doc = "";
        if(tipoDocSeguridad != null){
            switch(tipoDocSeguridad){
                case "2":
                        doc = "DNI: ";
                        break;
                case "5":
                        doc = "CE: ";
                        break;
                case "1":
                        doc = "NRO. CARNE VIG: ";
                        break;
            }
        }
        return doc;
    }
    
    public void buscarDatosPersonalSeguridad(){
        boolean validacion = true;
        if(tipoDocSeguridadBusq == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor seleccionar un tipo de documento");
        }
        if(numDocSeguridadBusq == null || numDocSeguridadBusq.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar el número de documento");
        }
        if(validacion){            
            listadoBusquedaPersonalSeguridad = ejbSspCarneFacade.buscarVigilanteVigenteCarteraCli(tipoDocSeguridadBusq, numDocSeguridadBusq, registro.getAdministradoId().getRuc()); 
        }
    }
    
    public void openBuscarVigilanteSeguridad(){
        tipoDocSeguridadBusq = null;
        numDocSeguridadBusq = null;
        listadoBusquedaPersonalSeguridad = new ArrayList();
        RequestContext.getCurrentInstance().execute("PF('wvDlgBusquedaVigilante').show()");
        RequestContext.getCurrentInstance().update("frmBusquedaVigilante");
    }
    
    public void seleccionarVigilanteSeguridad(Map nmap){
        nombresSeguridad = ""+nmap.get("NOMBRE");
        nroCarneSeguridad = ""+nmap.get("NRO_CRN_VIG");
        tipoDocSeguridad = ""+nmap.get("TIP_USR");
        numDocSeguridad = ""+nmap.get("DOC");        
        carneIdSeguridad = ejbSspRegistroFacade.find(Long.parseLong(""+nmap.get("REGISTRO_ID"))).getCarneId().getId();
        motivoSeguridad = "";
        
        if(!esDeclaracionCC){
            if(agregarPersonalSeguridad()){
                RequestContext.getCurrentInstance().execute("PF('wvDlgBusquedaVigilante').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm()+":pnlRelacionPersonal");
            }
        }else{
            RequestContext.getCurrentInstance().execute("PF('wvDlgBusquedaVigilante').hide()");
            RequestContext.getCurrentInstance().update(obtenerForm()+":pnlRelacionPersonal");
        }
    }
    
    public boolean agregarPersonalSeguridad(){
        boolean validacion = true;
        if(numDocSeguridad == null || numDocSeguridad.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor busque un vigilante");
        }else{
            for(Map nmap : lstPersonalSeguridad){
                if(nmap.get("declarado") == null || (nmap.get("declarado") != null && nmap.get("declarado").toString().equals("1")) ){
                    if(Objects.equals(Long.parseLong(""+nmap.get("carneId")), carneIdSeguridad)){
                        validacion = false;
                        JsfUtil.mensajeError("El vigilante seleccionado ya ha sido ingresado");
                        break;
                    }
                }
            }
        }
        if(esDeclaracionCC){
            if(motivoSeguridad == null || motivoSeguridad.isEmpty()){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":motivoSeguridad");
                JsfUtil.mensajeError("Por favor ingrese un motivo");
            }
        }
        
        if(validacion){
            Map nmap = new HashMap();
            nmap.put("id", JsfUtil.tempId());
            nmap.put("nombres", nombresSeguridad);
            nmap.put("doc", numDocSeguridad);
            nmap.put("carne", nroCarneSeguridad);
            nmap.put("carneId", carneIdSeguridad);
            nmap.put("carneEntity", ejbSspCarneFacade.buscarCarneById(carneIdSeguridad));
            AmaLicenciaDeUso licencia = ejbAmaLicenciaDeUsoFacade.obtenerLicenciaDeUso(registro.getAdministradoId().getId(), numDocSeguridad);
            if(licencia != null){
                nmap.put("licencia", licencia.getNroLicencia() );
            }else{
                nmap.put("licencia", "");
            }
            if(esDeclaracionCC){
                nmap.put("declarado", "1");
                nmap.put("motivoSeguridad", ((motivoSeguridad != null)?motivoSeguridad.trim().toUpperCase():motivoSeguridad));
            }else{
                nmap.put("declarado", null);
                nmap.put("motivoSeguridad", null);
            }
            
            lstPersonalSeguridad.add(nmap);
            
            nombresSeguridad = null;
            nroCarneSeguridad = null;
            carneIdSeguridad = null;
            tipoDocSeguridad = null;
            numDocSeguridad = null;
            motivoSeguridad = null;
        }
        return validacion;
    }    
    
    public void eliminarPersonalSeguridadSeleccionados(){
        if(!lstPersonalSeguridadSeleccionados.isEmpty()){
            for(Map seg: lstPersonalSeguridadSeleccionados){
                for(Map perSeg: lstPersonalSeguridad){
                    if(Objects.equals(seg, perSeg)){
                        lstPersonalSeguridad.remove(seg);
                        break;
                    }
                }
            }
            lstPersonalSeguridadSeleccionados = new ArrayList();
            JsfUtil.mensaje("Registro(s) eliminado(s) correctamente");
        }else{
            JsfUtil.mensajeError("Por favor seleccione al menos un personal de seguridad");
        }
    }
   
    public void buscarDatosArma(){
        boolean validacion = true;
       if(tipoDocArmas == null){
           validacion = false;
           JsfUtil.mensajeError("Por favor seleccione el tipo de búsqueda");
           JsfUtil.invalidar(obtenerForm()+":tipoDocSeguridad");
       }
       if(documentoArma == null || documentoArma.isEmpty() ){
           validacion = false;           
            if(tipoDocArmas != null){
                if(tipoDocArmas.equals("1")){
                    JsfUtil.mensajeError("Por favor ingrese el nro. de RUA");
                }else{
                    JsfUtil.mensajeError("Por favor ingrese el nro. de serie");
                }
            }else{
                JsfUtil.mensajeError("Por favor ingrese el campo a filtrar");
            }
           JsfUtil.invalidar(obtenerForm()+":numDocSeguridad");
       }
       if(validacion){
            List<AmaTarjetaPropiedad> lstTarjetas = ejbAmaTarjetaPropiedadFacade.listTarjetaArmasByNroRuaByPropietario(tipoDocArmas, documentoArma, registro.getAdministradoId().getId());
            if(lstTarjetas.isEmpty()){                
                documentoArma = null;
                JsfUtil.mensajeAdvertencia("No se encontró el campo ingresado");
            }else{
                tarjetaPropiedad = lstTarjetas.get(0);
            }
       }
    }
    
    public void agregarArmas(){
        boolean validacion = true;
        if(tarjetaPropiedad == null){
            validacion = false;
            JsfUtil.mensajeAdvertencia("Por favor buscar primero una arma");
        }else{
            for(SspCarteraArma tarjeta : registro.getSspCarteraArmaList() ){
                if(tarjeta.getActivo() == 1 && (tarjeta.getActualizacion() == null || (tarjeta.getActualizacion() != null && tarjeta.getActualizacion().toString().equals("1")))){
                    if(Objects.equals(tarjeta.getArmaId().getId(), tarjetaPropiedad.getId())){
                        validacion = false;
                        JsfUtil.mensajeAdvertencia("El arma seleccionada ya se encuentra adicionada");
                        break;
                    }
                }
            }            
        }
        if(esDeclaracionCC){
            if(motivoArma == null || motivoArma.isEmpty() ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":motivoArma");
                JsfUtil.mensajeAdvertencia("Por favor ingrese el motivo.");
            }
        }
        
        if(validacion){
            SspCarteraArma cArma = new SspCarteraArma();
            cArma.setId(JsfUtil.tempId());
            cArma.setActivo(JsfUtil.TRUE);
            cArma.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            cArma.setAudNumIp(JsfUtil.getIpAddress());
            cArma.setActualizacion(null);
            cArma.setArmaId(tarjetaPropiedad);
            cArma.setCarteraId(registro);
            if(esDeclaracionCC){
                cArma.setActualizacion('1');
                cArma.setFechaActualizacion(new Date());
                cArma.setObservacion(motivoArma.trim().toUpperCase());
            }
            registro.getSspCarteraArmaList().add(cArma);
            tarjetaPropiedad = null;
            documentoArma = null;
            motivoArma = null;
        }
    }
    
    public void eliminarArmasSeleccionados(){
        if(!lstArmasSeleccionadas.isEmpty()){
            for(SspCarteraArma tarjeta: lstArmasSeleccionadas){
                for(SspCarteraArma tarjetaActual: registro.getSspCarteraArmaList()){
                    if(Objects.equals(tarjetaActual.getId(), tarjeta.getId())){
                        registro.getSspCarteraArmaList().remove(tarjetaActual);
                        break;
                    }
                }
            }
            lstArmasSeleccionadas = new ArrayList();
            JsfUtil.mensaje("Registro(s) eliminado(s) correctamente");
        }else{
            JsfUtil.mensajeError("Por favor seleccione al menos un arma");
        }
    }
    
    public void agregarVehiculo(){
        boolean validacion = true;
        if(nroPlaca == null || nroPlaca.isEmpty()){
            validacion = false;
            JsfUtil.mensajeAdvertencia("Por favor ingresar la placa del vehículo");
            JsfUtil.invalidar(obtenerForm()+":placaVehiculo");
        }else{
            for(SspCarteraVehiculo cVeh : registro.getSspCarteraVehiculoList()){
                if(cVeh.getActivo() == 1 && (cVeh.getActualizacion() == null || (cVeh.getActualizacion() != null && cVeh.getActualizacion().toString().equals("1")) )){
                    if(cVeh.getVehiculoId().getPlaca() != null && Objects.equals(cVeh.getVehiculoId().getPlaca().trim(), nroPlaca.toUpperCase()) ){
                        validacion = false;
                        JsfUtil.mensajeAdvertencia("Ya se ha ingresado un vehículo con la misma placa");
                    }
                    if(cVeh.getVehiculoId().getTarjetaPropiedad() != null && Objects.equals(cVeh.getVehiculoId().getTarjetaPropiedad().trim(), vehiculo.getTarjetaPropiedad()) ){
                        validacion = false;
                        JsfUtil.mensajeAdvertencia("Ya se ha ingresado un vehículo con la misma marca");
                    }
                }
            }
        }
        if(esDeclaracionCC){
            if(motivoVehiculo == null || motivoVehiculo.isEmpty() ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":motivoVehiculo");
                JsfUtil.mensajeAdvertencia("Por favor ingrese el motivo.");
            }
        }
        
        if(validacion){
            vehiculo.setActivo(JsfUtil.TRUE);
            vehiculo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            vehiculo.setAudNumIp(JsfUtil.getIpAddress());
            vehiculo.setId(null);
            vehiculo.setPlaca(nroPlaca.toUpperCase());
            vehiculo = (SspVehiculo) JsfUtil.entidadMayusculas(vehiculo, "");
            
            SspCarteraVehiculo cVeh = new SspCarteraVehiculo();
            cVeh.setId(JsfUtil.tempId());
            cVeh.setActivo(JsfUtil.TRUE);
            cVeh.setActualizacion(null);
            cVeh.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            cVeh.setAudNumIp(JsfUtil.getIpAddress());
            cVeh.setCarteraId(registro);
            cVeh.setVehiculoId(vehiculo);
            if(esDeclaracionCC){
                cVeh.setActualizacion('1');
                cVeh.setFechaActualizacion(new Date());
                cVeh.setObservacion(motivoVehiculo.trim().toUpperCase());
            }
            registro.getSspCarteraVehiculoList().add(cVeh);
            
            vehiculo = new SspVehiculo();
            nroPlaca = null;
            motivoVehiculo = null;
        }
    }
    
    public void eliminarVehiculosSeleccionados(){
        if(!lstVehiculosSeleccionados.isEmpty()){
            for(SspCarteraVehiculo veh: lstVehiculosSeleccionados){
                for(SspCarteraVehiculo vehActual: registro.getSspCarteraVehiculoList()){
                    if(Objects.equals(veh.getId(), vehActual.getId())){
                        registro.getSspCarteraVehiculoList().remove(veh);
                        break;
                    }
                }
            }
            lstVehiculosSeleccionados = new ArrayList();
            JsfUtil.mensaje("Registro(s) eliminado(s) correctamente");
        }else{
            JsfUtil.mensajeError("Por favor seleccione al menos un vehículo");
        }
    }
    //////////
    
    ///////// EDITAR ////////
    public StreamedContent cargarDocumento(){        
        try {
            if(file != null){
                return new DefaultStreamedContent(file.getInputstream(), "application/pdf", file.getFileName() );
            }else{
                SspDocumento docCarga = null;
                if(registro.getSspDocumentoList() != null){
                    for(SspDocumento doc : registro.getSspDocumentoList()){
                        if(doc.getActivo() == 1){
                            docCarga = doc;
                        }
                    }
                }
                if(docCarga != null){
                    String nombre = docCarga.getNombre().substring(0, docCarga.getNombre().lastIndexOf(".") );
                    String extension = docCarga.getNombre().substring(docCarga.getNombre().lastIndexOf("."), docCarga.getNombre().length());
                    String nombreArchivo = nombre + extension;
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
     }
    
    public void cargarArchivo(SspCarteraCliente ccRegistro){
        try {
            String archivoName = null;
            if(ccRegistro.getSspDocumentoList() != null){
                for(SspDocumento doc : ccRegistro.getSspDocumentoList()){
                    if(doc.getActivo() == 1){
                        archivoName = doc.getNombre();
                    }
                }
            }
            
            archivo = archivoName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public List<SspLugarServicio> getListadoLugarServicio(){
        List<SspLugarServicio> lstLugar = new ArrayList();
        if(registro != null){
            for(SspLugarServicio ls : registro.getSspLugarServicioList()){
                if(ls.getActivo() == 1){
                    lstLugar.add(ls);
                }
            }
        }            
        return lstLugar;
    }
    
    public List<SspCarteraArma>  getListadoArmas(){
        List<SspCarteraArma> lstArmas = new ArrayList();
        if(registro != null){
            for(SspCarteraArma ls : registro.getSspCarteraArmaList()){
                if(ls.getActivo() == 1 && ls.getArmaId().getActivo() == 1){
                    lstArmas.add(ls);
                }
            }
        }
        return lstArmas;
    }
    
    public List<SspCarteraVehiculo>  getListadoVehiculos(){
        List<SspCarteraVehiculo> lstVehiculo = new ArrayList();
        if(registro != null){
            for(SspCarteraVehiculo ls : registro.getSspCarteraVehiculoList() ){
                if(ls.getActivo() == 1 && ls.getVehiculoId().getActivo() == 1){
                    lstVehiculo.add(ls);
                }
            }
        }   
        return lstVehiculo;
    }    
    
    public void listarModalidadesCarteraCliente(boolean validaVista, String ruc){
        int cont = 0;
        //lstModalidades = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_MCO");
        lstModalidades = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_MCO_TEC','TP_MCO_PRO','TP_MCO_TRA','TP_MCO_VIG','TP_MCO_SIS'");
        //System.out.println("lstModalidades -...------>"+ lstModalidades);
        if(validaVista){
            List<Map> lstModalidadResolucion = ejbCarteraClienteFacade.buscarModalidadesPermitidasEmpresa(ruc);
            System.out.println("lstModalidadResolucion -...------>"+ lstModalidadResolucion);
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
            }
        }
    }
    
    public void listarModalidadesCarteraClienteReferenciado(boolean validaVista){
        int cont = 0;
        lstModalidadesReferenciado = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_MCO_TEC','TP_MCO_PRO','TP_MCO_TRA','TP_MCO_VIG','TP_MCO_SIS'");
        
        if(validaVista){
            List<Map> lstModalidadResolucion = ejbCarteraClienteFacade.buscarModalidadesPermitidasEmpresa(registro.getAdministradoId().getRuc());
            if(lstModalidadResolucion != null){
                //////// TECNOLOGIA ////////
                cont = 0;
                for(Map modal : lstModalidadResolucion){
                    if(modal.get("MODALIDAD").toString().contains("TECNOLOGIA")){
                        cont ++ ;                        
                    }
                }
                if(cont == 0){
                    for(TipoSeguridad ts : lstModalidadesReferenciado){
                        if(ts.getCodProg().equals("TP_MCO_TEC")){
                            lstModalidadesReferenciado.remove(ts);
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
                    for(TipoSeguridad ts : lstModalidadesReferenciado){
                        if(ts.getCodProg().equals("TP_MCO_PRO")){
                            lstModalidadesReferenciado.remove(ts);
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
                    for(TipoSeguridad ts : lstModalidadesReferenciado){
                        if(ts.getCodProg().equals("TP_MCO_TRA")){
                            lstModalidadesReferenciado.remove(ts);
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
                    for(TipoSeguridad ts : lstModalidadesReferenciado){
                        if(ts.getCodProg().equals("TP_MCO_VIG")){
                            lstModalidadesReferenciado.remove(ts);
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
                    for(TipoSeguridad ts : lstModalidadesReferenciado){
                        if(ts.getCodProg().equals("TP_MCO_SIS")){
                            lstModalidadesReferenciado.remove(ts);
                            break;
                        }
                    }
                }
                ////////////////////////
            }
        }
    }
    
    
    ///// Termino de Contrato //////
    
    public String obtenerFormTermino(){
        switch(estado){
            case CREAR_TERMINO:
                        return "createTerminoForm";
            case EDITAR_TERMINO:
                        return "editTerminoForm";
            case VER_TERMINO:
                        return "verTerminoForm";
        }
        return null;
    }
    
    public void handleFileUploadAdjuntoTermino(FileUploadEvent event){
        try {
            if (event != null) {
                file = event.getFile();
                if (JsfUtil.verificarPDF(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    foto = new DefaultStreamedContent(file.getInputstream(), "application/pdf");
                    archivoTermino = file.getFileName();
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de cargar no tiene formato PDF");
                }
            }
        } catch (Exception e) {
        //    e.printStackTrace();
        }
    }
    
    public List<TipoSeguridad> getLstMotivosContrato(){
        return ejbTipoSeguridadFacade.lstTipoSeguridad("TP_MOT");
    }
    
    public void openDlgBuscarContrato(){
        reiniciarCamposBusqueda();
        lstContratosBusqueda = new ArrayList();
        RequestContext.getCurrentInstance().execute("PF('wvDlgBusquedaContrato').show()");
        RequestContext.getCurrentInstance().update("frmBusquedaContrato");
    }
    
    public void buscarContratosCliente(){
        boolean validacion = true;
        if(tipoBuscarPor == null || (filtro == null || filtro.isEmpty()) ){
            if(tipoBuscarPor == null && (filtro == null || filtro.isEmpty())){
                validacion = false;
                JsfUtil.mensajeAdvertencia("Es necesario ingresar un tipo de búsqueda");
            }
        }else{
            if(tipoBuscarPor != null && (filtro == null || filtro.isEmpty())){
                validacion = false;
                JsfUtil.mensajeAdvertencia("Es necesario ingresar el campo a buscar");
            }
        }
        
        if(validacion){
            HashMap mMap = new HashMap();
            mMap.put("tipoBuscarPor", tipoBuscarPor);
            mMap.put("filtro", filtro);
            mMap.put("tipoModalidad", (tipoModalidad != null)?tipoModalidad.getId():null);
            
            lstContratosBusqueda = ejbSspCarteraClienteFacade.buscarContratoClientes(mMap);
            reiniciarCamposBusqueda();
        }
    }
    
    public void seleccionarContratoCliente(SspCarteraCliente reg){
        setEsReferenciada(true);
        RequestContext.getCurrentInstance().execute("PF('wvDlgBusquedaContrato').hide()");
        RequestContext.getCurrentInstance().update(obtenerFormTermino());
    }
    
    public String obtenerModalidadBusqueda(List<TipoSeguridad> listado){
        String modalidades = "";
        
        if(listado != null){
            for(TipoSeguridad ts : listado){
                if(ts.getActivo() == 1){
                    if(modalidades.isEmpty()){
                        modalidades = ts.getNombre();
                    }else{
                        modalidades += " / " + ts.getNombre();
                    }
                }
            }
        }
            
        return modalidades;
    }
    
    public boolean validaCarteraClienteTermino(){
        boolean validacion = true;
        if(registro.getMotivoContratoId() == null){
            validacion = false;
            JsfUtil.invalidar(obtenerFormTermino()+ ":motivo");
            JsfUtil.mensajeError("Por favor seleccione un motivo de término de contrato");            
        }
        if(registro.getDetalleMotivo() == null || registro.getDetalleMotivo().isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerFormTermino()+ ":detalleMotivo");
            JsfUtil.mensajeError("Por favor ingrese una descripción del motivo");
        }
//        if(!Objects.equals(archivoTemporal, archivoTermino)){
//            if(file == null){
//                validacion = false;
//                JsfUtil.mensajeError("Por favor adjunte un archivo");
//            }
//        }
        if(registro.getFechaFin() == null){
            validacion = false;
            JsfUtil.invalidar(obtenerFormTermino()+ ":fechaTerminoContrato");
            JsfUtil.mensajeError("Por favor ingrese una fecha término de contrato");
        }else{
            if(JsfUtil.getFechaSinHora(registro.getContratoId().getFechaInicio()).compareTo(registro.getFechaFin()) > 0 ){
                validacion = false;
                JsfUtil.mensajeError("La fecha de término del servicio no puede ser menor que la fecha de inicio");
                JsfUtil.invalidar(obtenerFormTermino()+":fechaFin");
            }
        }
        
        return validacion;
    }
    
    public boolean completarDatosTerminoContrato(){
        boolean validacion = true;
        
        try {
            if(file != null){
                if(!Objects.equals(archivoTemporal, archivoTermino)){
                    if(file != null){
                        String fileName = file.getFileName();
                        String nombreFoto = "";
                        nombreFoto = "TERMINO_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_TCCLI") + fileName.substring(fileName.lastIndexOf('.'), fileName.length());

                        documento = new SspDocumento();
                        documento.setActivo(JsfUtil.TRUE);
                        documento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        documento.setAudNumIp(JsfUtil.getIpAddress());
                        documento.setCartera(registro);
                        documento.setId(null);
                        documento.setNombre(nombreFoto);

                        for(SspDocumento doc : registro.getSspDocumentoList()){
                            doc.setActivo(JsfUtil.FALSE);
                        }
                        documento = (SspDocumento) JsfUtil.entidadMayusculas(documento, "");
                        registro.getSspDocumentoList().add(documento);
                    }
                }
            }
            registro.setFechaInicio(registro.getContratoId().getFechaInicio());

            if(!registro.getSspLugarServicioList().isEmpty()){
                JsfUtil.borrarIds(registro.getSspLugarServicioList());
            }
            if(renderPnlVehiculos){
                JsfUtil.borrarIds(registro.getSspCarteraVehiculoList());
            }else{
                if(registro.getSspCarteraVehiculoList() != null){
                    for(SspCarteraVehiculo cVeh : registro.getSspCarteraVehiculoList()){
                        cVeh.setActivo(JsfUtil.FALSE);
                    }
                }
            }
            if(!renderPnlRepLegal){
                registro.setReprCliId(null);
            }
            if(!renderPnlArmas){
                if(registro.getSspCarteraArmaList() != null){
                    for(SspCarteraArma cArm : registro.getSspCarteraArmaList()){
                        cArm.setActivo(JsfUtil.FALSE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            validacion = false;
        }
        return validacion;
    }    
    
    public StreamedContent cargarDocumentoTermino(){        
        try {
            if(archivo != null && !archivo.isEmpty()){
                String nombre = archivo.substring(0, archivo.lastIndexOf(".") );
                String extension = archivo.substring(archivo.lastIndexOf("."), archivo.length());
                String nombreArchivo = nombre + extension;
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
            }
         } catch (Exception e) {
             e.printStackTrace();
         }
        
        return null;
     }
    
    public void cargarArchivoTermino(SspCarteraCliente ccRegistro){
        try {
            String archivoName = null;
            if(ccRegistro.getSspDocumentoList() != null){
                for(SspDocumento doc : ccRegistro.getSspDocumentoList()){
                    if(doc.getActivo() == 1){
                        archivoName = doc.getNombre();
                    }
                }
            }
            archivoTermino = archivoName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void cargarListasCarteraCliente(SspCarteraCliente ssRegistro){
        
        //// Modalidades ////
        for(TipoSeguridad ts : ssRegistro.getTipoSeguridadList()){
            if(ts.getActivo() == 1){
                registro.getTipoSeguridadList().add(ts);
            }
        }
        
        //// Lugar de Servicio ////
        if(ssRegistro.getSspLugarServicioList() != null && !ssRegistro.getSspLugarServicioList().isEmpty()){
            for(SspLugarServicio ls : ssRegistro.getSspLugarServicioList()){
                if(ls.getActivo() == 1){
                    SspLugarServicio lsNuevo = new SspLugarServicio();
                    lsNuevo.setActivo(JsfUtil.TRUE);
                    lsNuevo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    lsNuevo.setAudNumIp(JsfUtil.getIpAddress());
                    lsNuevo.setDireccion(ls.getDireccion());
                    lsNuevo.setDistId(ls.getDistId());
                    lsNuevo.setLatitud(ls.getLatitud());
                    lsNuevo.setLongitud(ls.getLongitud());
                    lsNuevo.setReferencia(ls.getReferencia());
                    lsNuevo.setCarteraId(registro);
                    lsNuevo.setId(JsfUtil.tempId());
                    registro.getSspLugarServicioList().add(lsNuevo);
                }
            }
        }
        
        //// Vigilantes ////
        if(ssRegistro.getSspCarteraVigilanteList() != null && !ssRegistro.getSspCarteraVigilanteList().isEmpty()){
            SspCarteraVigilante cartera;

            for(SspCarteraVigilante cv : ssRegistro.getSspCarteraVigilanteList()){
                if(cv.getActivo() == 1 && (cv.getActualizacion() == null || (cv.getActualizacion() != null && cv.getActualizacion().toString().equals("1")) ) ){
                        cartera = new SspCarteraVigilante();
                        cartera.setActivo(JsfUtil.TRUE);
                        cartera.setActualizacion(null);
                        cartera.setObservacion(null);
                        cartera.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        cartera.setAudNumIp(JsfUtil.getIpAddress());
                        cartera.setCarteraId(registro);
                        cartera.setId(null);
                        cartera.setCarneId(cv.getCarneId());

                        registro.getSspCarteraVigilanteList().add(cartera);
                }
            }
        }
        
        //// Armas ////
        if(ssRegistro.getSspCarteraArmaList() != null && !ssRegistro.getSspCarteraArmaList().isEmpty()){
            for(SspCarteraArma ama : ssRegistro.getSspCarteraArmaList()){
                if(ama.getActivo() == 1 && (ama.getActualizacion() == null || (ama.getActualizacion() != null && ama.getActualizacion().toString().equals("1")) )){
                        SspCarteraArma nuevaArma = new SspCarteraArma();
                        nuevaArma.setActivo(JsfUtil.TRUE);
                        nuevaArma.setActualizacion(null);
                        nuevaArma.setArmaId(ama.getArmaId());
                        nuevaArma.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        nuevaArma.setAudNumIp(JsfUtil.getIpAddress());
                        nuevaArma.setCarteraId(registro);
                        nuevaArma.setFechaActualizacion(null);
                        nuevaArma.setId(JsfUtil.tempId());
                        nuevaArma.setObservacion(null);
                        registro.getSspCarteraArmaList().add(nuevaArma);    
                }
            }
        }
        
        //// Vehiculos ////
        if(ssRegistro.getSspCarteraVehiculoList() != null && !ssRegistro.getSspCarteraVehiculoList().isEmpty()){
            for(SspCarteraVehiculo veh : ssRegistro.getSspCarteraVehiculoList()){
                if(veh.getActivo() == 1 && (veh.getActualizacion() == null || (veh.getActualizacion() != null && veh.getActualizacion().toString().equals("1")) )){
                        SspCarteraVehiculo nuevoVehiculo = new SspCarteraVehiculo();
                        nuevoVehiculo.setActivo(JsfUtil.TRUE);
                        nuevoVehiculo.setActualizacion(null);
                        nuevoVehiculo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        nuevoVehiculo.setAudNumIp(JsfUtil.getIpAddress());
                        nuevoVehiculo.setCarteraId(registro);
                        nuevoVehiculo.setFechaActualizacion(null);
                        nuevoVehiculo.setId(JsfUtil.tempId());
                        nuevoVehiculo.setObservacion(null);
                        nuevoVehiculo.setVehiculoId(veh.getVehiculoId());
                        registro.getSspCarteraVehiculoList().add(nuevoVehiculo);
                }
            }
        }
        
        //// Consorcio ////
        if(ssRegistro.getSbPersonaList() != null && !ssRegistro.getSbPersonaList().isEmpty()){
            for(SbPersonaGt cons : ssRegistro.getSbPersonaList()){
                if(cons.getActivo() == 1){
                    registro.getSbPersonaList().add(cons);
                }
            }
        }
    }
    
    //////////////////////
    /////// ADENDA ///////
    //////////////////////

    public Date minDateAdenda(){
        if(registro == null){
            return new Date();
        }
        if(registro.getContratoId().getFechaFin() == null){
            return registro.getContratoId().getFechaInicio();
        }
        return registro.getContratoId().getFechaFin();
    }
    
    public void handleFileUploadAdjuntoAdenda(FileUploadEvent event){
        try {
            if (event != null) {
                file = event.getFile();
                if (JsfUtil.verificarPDF(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    foto = new DefaultStreamedContent(file.getInputstream(), "application/pdf");
                    archivoAdenda = file.getFileName();
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de cargar no tiene formato PDF");
                }
            }
        } catch (Exception e) {
        //    e.printStackTrace();
        }
    }
    
    public void cargarArchivoAdenda(SspCarteraCliente ccRegistro){
        try {
            String archivoName = null;
            if(ccRegistro.getSspDocumentoList() != null){
                for(SspDocumento doc : ccRegistro.getSspDocumentoList()){
                    if(doc.getActivo() == 1){
                        archivoName = doc.getNombre();
                    }
                }
            }
            archivoAdenda = archivoName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void openDlgVerContratoReferenciado(){
        limpiarDatosReferenciado();        
        
        listarModalidadesCarteraClienteReferenciado(false);
        eventoSeleccionModalidadReferenciado(registro.getContratoId(),false);
        cargarDatosCarteraClienteReferenciado(registro.getContratoId());
        
        switch(registro.getContratoId().getTipoOpeId().getCodProg()){
            case "TP_TDEC_NUEVO":
                    cargarArchivo(registro.getContratoId());
                    if(archivo != null){
                        archivoReferenciado = ""+ archivo;    
                    }
                    break;
            case "TP_TDEC_ADENDA":
                    cargarArchivoAdenda(registro.getContratoId());
                    if(archivoAdenda != null){
                        archivoReferenciado = ""+ archivoAdenda;
                    }
                    break;
            case "TP_TDEC_BAJA":
                    cargarArchivoTermino(registro.getContratoId());
                    if(archivoTermino != null){
                        archivoReferenciado = ""+ archivoTermino;
                    }
                    break;
        }
        
        RequestContext.getCurrentInstance().execute("PF('wvDlgContratoReferenciado').show()");
        RequestContext.getCurrentInstance().update("frmContratoReferenciado");
    }
    
    public List<SspCarteraEvento> getListadoObservacionesReferenciado(){
        List<SspCarteraEvento> lstObsv = new ArrayList();
        if(registro != null && registro.getContratoId() != null){
            for(SspCarteraEvento ce : registro.getContratoId().getSspCarteraEventoList()){
                if(ce.getActivo() == 1 && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")){
                    lstObsv.add(ce);
                }
            }
        }
        
        return lstObsv;
    }
    
    public void eventoSeleccionModalidadReferenciado(SspCarteraCliente ccRegistro, boolean blnLimpiar){
        esTransporteDineroReferenciado = false;
        setRenderPnlPersonaReferenciado(false);
        setRenderPnlRepLegalReferenciado(false);
        setRenderPnlArmasReferenciado(false);
        setRenderPnlVehiculosReferenciado(false);
        setRenderPnlVigilantesReferenciado(false);

        for(TipoSeguridad tipo : lstModalidadesReferenciado){
            tipo.setActivo(JsfUtil.TRUE);
        }
        
        if(ccRegistro.getTipoSeguridadList() != null){
            for (TipoSeguridad tipo : ccRegistro.getTipoSeguridadList()) {
                if(tipo.getActivo() == 1){
                    switch(tipo.getCodProg()){
                        case "TP_MCO_TEC":  // TECNOLOGÍA DE SEGURIDAD
                                setRenderPnlPersonaReferenciado(true);
                                break;
                        case "TP_MCO_PRO":  // PROTECCIÓN PERSONAL
                                setRenderPnlPersonaReferenciado(true);
                                setRenderPnlArmasReferenciado(true);
                                setRenderPnlVigilantesReferenciado(true);
                                break;
                        case "TP_MCO_TRA":  // TRANSPORTE DE DINERO Y VALORES
                                esTransporteDineroReferenciado = true;
                                setRenderPnlPersonaReferenciado(true);
                                setRenderPnlArmasReferenciado(true);
                                setRenderPnlVehiculosReferenciado(true);
                                setRenderPnlVigilantesReferenciado(true);
                                break;
                        case "TP_MCO_VIG":  // VIGILANCIA PRIVADA
                                setRenderPnlPersonaReferenciado(true);
                                setRenderPnlArmasReferenciado(true);
                                setRenderPnlVigilantesReferenciado(true);
                                break;
                        case "TP_MCO_SIS":  // SERVICIOS INDIVIDUALES DE SEGURIDAD PERSONAL (SISPE)
                                setRenderPnlPersonaReferenciado(true);                            
                                setRenderPnlArmasReferenciado(true);
                                break;
                        case "TP_MCO_EVE":  // EVENTUAL
                                setRenderPnlPersonaReferenciado(true);
                                setRenderPnlArmasReferenciado(true);
                                setRenderPnlVigilantesReferenciado(true);
                                break;
                    }
                }
            }
            
            if(esTransporteDineroReferenciado){
                if(ccRegistro.getTodoRecursosBoolean()){
                    setRenderPnlArmasReferenciado(false);
                    setRenderPnlVigilantesReferenciado(false);
                    setRenderPnlVehiculosReferenciado(false);
                }
                if(ccRegistro.getTipoSeguridadList().size() > 1){
                    for (TipoSeguridad tipo : ccRegistro.getTipoSeguridadList()) {
                        switch(tipo.getCodProg()){
                            case "TP_MCO_TEC":  // TECNOLOGÍA DE SEGURIDAD
                                    break;
                            case "TP_MCO_PRO":  // PROTECCIÓN PERSONAL
                                    setRenderPnlArmasReferenciado(true);
                                    setRenderPnlVigilantesReferenciado(true);
                                    break;
                            case "TP_MCO_TRA":  // TRANSPORTE DE DINERO Y VALORES
                                    if(ccRegistro.getTodoRecursosBoolean()){
                                        setRenderPnlVehiculosReferenciado(false);
                                    }else{
                                        setRenderPnlVehiculosReferenciado(true);
                                    }
                                    break;
                            case "TP_MCO_VIG":
                                    setRenderPnlArmasReferenciado(true);
                                    setRenderPnlVigilantesReferenciado(true);
                                    break;
                            case "TP_MCO_SIS":
                                    setRenderPnlArmasReferenciado(true);
                                    break;
                            case "TP_MCO_EVE":
                                    setRenderPnlArmasReferenciado(true);
                                    setRenderPnlVigilantesReferenciado(true);
                                    break;
                        }
                    }
                }else{
                    if(ccRegistro.getTodoRecursosBoolean()){
                        setRenderPnlArmasReferenciado(false);
                        setRenderPnlVigilantesReferenciado(false);
                        setRenderPnlVehiculosReferenciado(false);
                    }else{
                        setRenderPnlArmasReferenciado(true);
                        setRenderPnlVigilantesReferenciado(true);
                        setRenderPnlVehiculosReferenciado(true);
                    }
                }
            }

            
            Map nmap = new HashMap();
            nmap.put("id", 1);
            nmap.put("nombre", "Persona Jurídica con RUC");
            lstTipoDocReferenciado.add(nmap);
            nmap = new HashMap();
            nmap.put("id", 2);
            nmap.put("nombre", "Persona Natural con RUC");
            lstTipoDocReferenciado .add(nmap);
            nmap = new HashMap();
            nmap.put("id", 3);
            nmap.put("nombre", "Persona Natural con DNI");
            lstTipoDocReferenciado .add(nmap);
            nmap = new HashMap();
            nmap.put("id", 4);
            nmap.put("nombre", "Persona Natural con CE");
            lstTipoDocReferenciado.add(nmap);
        }
    }
    
    public boolean getEsRucReferenciado(){
        boolean valida = false;
        if(tipoDocReferenciado != null && !tipoDocReferenciado.isEmpty()){
            if(tipoDocReferenciado.equals("1") || tipoDocReferenciado.equals("2") ){
                valida = true;
            }
        }        
        return valida;
    }

    public boolean getEsDniReferenciado(){
        boolean valida = false;
        if(tipoDocReferenciado != null && !tipoDocReferenciado.isEmpty()){
            if(tipoDocReferenciado.equals("3") || tipoDocReferenciado.equals("4") ){
                valida = true;
            }
        }        
        return valida;
    }
    
    public List<SspLugarServicio> getListadoLugarServicioReferenciado(){
        List<SspLugarServicio> lstLugar = new ArrayList();
        if(registro != null && registro.getContratoId() != null){
            for(SspLugarServicio ls : registro.getContratoId().getSspLugarServicioList()){
                if(ls.getActivo() == 1){
                    lstLugar.add(ls);
                }
            }
        }            
        return lstLugar;
    }
    
    public StreamedContent cargarDocumentoReferenciado(){        
        try {
            if(fileReferenciado != null){
                return new DefaultStreamedContent(fileReferenciado.getInputstream(), "application/pdf", fileReferenciado.getFileName() );
            }else{
                SspDocumento docCarga = null;
                if(registro.getContratoId().getSspDocumentoList() != null){
                    for(SspDocumento doc : registro.getContratoId().getSspDocumentoList()){
                        if(doc.getActivo() == 1){
                            docCarga = doc;
                        }
                    }
                }
                if(docCarga != null){
                    String nombre = docCarga.getNombre().substring(0, docCarga.getNombre().lastIndexOf(".") );
                    String extension = docCarga.getNombre().substring(docCarga.getNombre().lastIndexOf("."), docCarga.getNombre().length());
                    String nombreArchivo = nombre + extension;
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
                }
            }
        } catch (Exception e) {
         //    e.printStackTrace();
        }
        
        return null;
    }
    
    public List<SspCarteraArma>  getListadoArmasReferenciado(){
        List<SspCarteraArma> lstArmas = new ArrayList();
        if(registro != null){
            if(registro.getContratoId() != null){
                for(SspCarteraArma ls : registro.getContratoId().getSspCarteraArmaList()){
                    if(ls.getActivo() == 1){
                        lstArmas.add(ls);
                    }
                }
            }   
        }
        return lstArmas;
    }
    
    public List<SspCarteraVehiculo>  getListadoVehiculosReferenciado(){
        List<SspCarteraVehiculo> lstVehiculo = new ArrayList();
        if(registro != null && registro.getContratoId() != null ){
            for(SspCarteraVehiculo ls : registro.getContratoId().getSspCarteraVehiculoList() ){
                if(ls.getActivo() == 1){
                    lstVehiculo.add(ls);
                }
            }
        }   
        return lstVehiculo;
    }
    
    public void cargarDatosCarteraClienteReferenciado(SspCarteraCliente ccRegistro){
        
        if(renderPnlPersonaReferenciado){
            setBlnBuscoReferenciado(true);
            setEsNuevoReferenciado(false);
            if(ccRegistro.getClienteId().getRuc() != null){
                numDocReferenciado = ccRegistro.getClienteId().getRuc();
                if(numDocReferenciado.startsWith("1")){
                    tipoDocReferenciado = "2";
                }else{
                    setRenderPnlRepLegalReferenciado(true);
                    tipoDocReferenciado = "1";
                }
            }else{
                numDocReferenciado = ccRegistro.getClienteId().getNumDoc();
                if(ccRegistro.getClienteId().getNumDoc().trim().length() == 8){
                    tipoDocReferenciado = "3";
                }else{
                    tipoDocReferenciado = "4";
                }
            }
           
            for(SbDireccionGt dire : ccRegistro.getClienteId().getSbDireccionList()){
                if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                    direccionRucReferenciado = dire.getDireccion();
                    direccionDniReferenciado = dire.getDireccion();
                    ubigeoRucReferenciado = dire.getDistritoId();
                    ubigeoDniReferenciado = dire.getDistritoId();
                    setDisabledDireccionReferenciado(true);
                }
            }
            for(SbMedioContactoGt medio : ccRegistro.getClienteId().getSbMedioContactoList()){
                if(medio.getActivo() == 1){
                    if(medio.getTipoId().getCodProg().equals("TP_MEDCO_COR")){
                    correoReferenciado = medio.getValor();
                        setDisabledCorreoReferenciado(true);
                    }
                    if(medio.getTipoId().getCodProg().equals("TP_MEDCO_MOV")){
                        telefonoReferenciado = medio.getValor();
                        setDisabledTelefonoReferenciado(true);
                    }
                }
            }        
        }
        
        if(renderPnlRepLegalReferenciado){
            if(ccRegistro.getReprCliId() != null){
                setBlnBuscoRepLegalReferenciado(true);
                setEsNuevoRepLegalReferenciado(false);
                if(ccRegistro.getReprCliId().getRuc() != null){
                    numDocRepLegalReferenciado = ccRegistro.getReprCliId().getRuc();
                    if(numDocRepLegalReferenciado.startsWith("1")){
                        tipoDocRepLegalReferenciado = "2";
                    }else{
                        tipoDocRepLegalReferenciado = "1";
                    }
                }else{
                    numDocRepLegalReferenciado = ccRegistro.getReprCliId().getNumDoc();
                    if(ccRegistro.getReprCliId().getNumDoc().trim().length() == 8){
                        tipoDocRepLegalReferenciado = "3";
                    }else{
                        tipoDocRepLegalReferenciado = "4";
                    }
                }

                for(SbDireccionGt dire : ccRegistro.getReprCliId().getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                        direccionRepLegalReferenciado = dire.getDireccion();
                        ubigeoRepLegalReferenciado = dire.getDistritoId();
                        setDisabledDireccionRepLegalReferenciado(true);
                    }
                }
                for(SbMedioContactoGt medio : ccRegistro.getReprCliId().getSbMedioContactoList()){
                    if(medio.getActivo() == 1){
                        if(medio.getTipoId().getCodProg().equals("TP_MEDCO_COR")){
                            correoRepLegalReferenciado = medio.getValor();
                            setDisabledTelefonoRepLegalReferenciado(true);
                        }
                        if(medio.getTipoId().getCodProg().equals("TP_MEDCO_MOV")){
                            telefonoRepLegalReferenciado = medio.getValor();
                            setDisabledCorreoRepLegalReferenciado(true);
                        }
                    }
                }
            }   
        }
        
        fechaInicioServicioReferenciado = ccRegistro.getFechaInicio();        
        strObservacionReferenciado = ccRegistro.getMotivoFecha();
        blnEventoReferenciado = (ccRegistro.getEvento() != null && ccRegistro.getEvento() == 1);
        if(blnEventoReferenciado){
            renderEventoReferenciado = true;
        }
        
        if(renderPnlVigilantesReferenciado){
            Map nmap;
            for(SspCarteraVigilante vig : ccRegistro.getSspCarteraVigilanteList()){
                nmap = new HashMap();
                nmap.put("id", JsfUtil.tempId());
                nmap.put("nombres", vig.getCarneId().getVigilanteId().getNombresYApellidos());
                nmap.put("doc", vig.getCarneId().getVigilanteId().getNumDoc());
                nmap.put("carne", ""+vig.getCarneId().getNroCarne());
                nmap.put("carneId", ""+vig.getCarneId().getId());
                nmap.put("carneEntity", vig.getCarneId());
                AmaLicenciaDeUso licencia = ejbAmaLicenciaDeUsoFacade.obtenerLicenciaDeUso(ccRegistro.getAdministradoId().getId(), vig.getCarneId().getVigilanteId().getNumDoc());
                if(licencia != null){
                    nmap.put("licencia", licencia.getNroLicencia() );
                }else{
                    nmap.put("licencia", "");
                }
                nmap.put("declarado", vig.getActualizacion());
                nmap.put("motivoSeguridad", vig.getObservacion());
                lstPersonalSeguridadReferenciado.add(nmap);
            }
        }
    }
        
    public void limpiarDatosReferenciado(){
        archivoAdenda = null;
        archivoReferenciado = null;
        lstModalidadesReferenciado = new ArrayList();
        tipoDocReferenciado = null;
        numDocReferenciado = null;
        lstTipoDocReferenciado = new ArrayList();
        rznSocialReferenciado = null;
        direccionRucReferenciado = null;
        ubigeoRucReferenciado = null;
        direccionDniReferenciado = null;
        ubigeoDniReferenciado = null;
        correoReferenciado = null;
        telefonoReferenciado = null;
        disabledDireccionReferenciado = false;
        disabledCorreoReferenciado = false;
        disabledTelefonoReferenciado = false;
        lstRepresentanteReferenciado = new ArrayList();
        blnBuscoReferenciado = false;
        esNuevoReferenciado = false;
        disabledBtnTransmitirReferenciado = false;
        renderPnlPersonaReferenciado = false;
        renderPnlRepLegalReferenciado = false;
        renderPnlArmasReferenciado = false;
        renderPnlVehiculosReferenciado = false;
        renderPnlVigilantesReferenciado = false;
        tipoDocRepLegalReferenciado = null;
        numDocRepLegalReferenciado = null;
        correoRepLegalReferenciado = null;
        telefonoRepLegalReferenciado = null;
        blnBuscoRepLegalReferenciado = false;
        esNuevoRepLegalReferenciado = false;
        disabledDireccionRepLegalReferenciado = false;
        disabledCorreoRepLegalReferenciado = false;
        disabledTelefonoRepLegalReferenciado = false;
        direccionRepLegalReferenciado = null;
        ubigeoRepLegalReferenciado = null;
        lugarServicioReferenciado = null;
        mapCenterReferenciado = null;
        lugarSelectedReferenciado = null;
        direccionServicioReferenciado = null;
        fechaInicioServicioReferenciado = null;
        latitudReferenciado = null;
        longitudReferenciado = null;
        documentoReferenciado = null;
        fotoByteReferenciado = null;
        fileReferenciado = null;
        fotoReferenciado = null;
        archivoReferenciado = null;
        strObservacionReferenciado = null;
        blnEventoReferenciado = false;
        renderEventoReferenciado = false;
        esTransporteDineroReferenciado = false;
        lstPersonalSeguridadReferenciado = new ArrayList(); 
    }

    public void validarRepresentante(SspCarteraCliente ccRegistro){
        setRenderRepresentante(true);
        if(ccRegistro.getAdministradoId() != null && ccRegistro.getAdministradoId().getRuc() != null){
            if(ccRegistro.getAdministradoId().getRuc().startsWith("1")){
                setRenderRepresentante(false);
            }
        }
    }
    
    public void openDlgActuaizaFechaContrato(Map reg){
        registro = null;
        registro = ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+reg.get("ID")));
        RequestContext.getCurrentInstance().execute("PF('wvDlgActualizarFechaFin').show()");
        RequestContext.getCurrentInstance().update("frmActualizarFechaFin");
    }
    
    public void actualizarFechaFinContrato(){
        try {
            if(registro.getFechaFin() == null){
                JsfUtil.mensajeError("Por favor ingrese la fecha de fin de contrato");
            }else{
                registro.setFechaFinActualiza(registro.getFechaFin());
                ejbSspCarteraClienteFacade.edit(registro);
                JsfUtil.mensaje("Actualizado correctamente");
                resultadosBandeja = new ArrayList();
                RequestContext.getCurrentInstance().update("listForm");
                RequestContext.getCurrentInstance().execute("PF('wvDlgActualizarFechaFin').hide()");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al actualizar la fecha de contrato");
        }
    }
    
    public String obtenerNombreUbigeoReferenciado(SbDistritoGt dist){
        if(dist != null){
            return dist.getProvinciaId().getDepartamentoId().getNombre() + " / " + dist.getProvinciaId().getNombre() + " / " + dist.getNombre();
        }
        return "";
    }
    
    public List<SspCarteraEvento> getLstObservacionesOrdenadasDesc(){
        if(lstObservaciones != null){
            Collections.sort(lstObservaciones, new Comparator<SspCarteraEvento>() {
                @Override
                public int compare(SspCarteraEvento one, SspCarteraEvento other) {
                    return other.getFecha().compareTo(one.getFecha());
                }
            });
        }
        return lstObservaciones;
    }
    
    public void cambiarUbigeoLugarServicio(){
        lugarServicio.setLatitud(null);
        lugarServicio.setLongitud(null);
        lugarSelected.setLatitud(null);
        lugarSelected.setLongitud(null);
    }
    
    public void openDlgConfirmarDeclaracion(int tipo){
        boolean abrirPopUp = true;
        motivoSeguridad = null;
        
        switch(tipo){
            case 1: // Personal de Seguridad
                    if(!lstPersonalSeguridadSeleccionados.isEmpty()){
                        if(lstPersonalSeguridadSeleccionados.size() > 1){
                           lstPersonalSeguridadSeleccionados = new ArrayList();
                           JsfUtil.mensajeError("Solo puede seleccionar un registro a la vez para eliminar");
                           return;
                        }else{
                            for(Map seg: lstPersonalSeguridadSeleccionados){
                                if(seg.get("declarado") != null && seg.get("declarado").toString().equals("0")){
                                    JsfUtil.mensajeError("No puede eliminar este personal de seguridad porque ya ha sido eliminado");
                                    return;
                                }
                                for(Map perSeg: lstPersonalSeguridad){
                                    if(Objects.equals(seg, perSeg) && (perSeg.get("declarado") != null && Integer.parseInt(perSeg.get("id").toString()) < 0 ) ){
                                        lstPersonalSeguridad.remove(seg);
                                        abrirPopUp = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }else{
                        JsfUtil.mensajeError("Por favor seleccione al menos un personal de seguridad");
                        return;
                    }
                    break;
            case 2: // Armas
                    if(!lstArmasSeleccionadas.isEmpty()){
                        if(lstArmasSeleccionadas.size() > 1){
                           lstArmasSeleccionadas = new ArrayList();
                           JsfUtil.mensajeError("Solo puede seleccionar un registro a la vez para eliminar");
                           return;
                        }else{
                            for(SspCarteraArma tarjeta: lstArmasSeleccionadas){
                                if(tarjeta.getActualizacion() != null && tarjeta.getActualizacion().toString().equals("0")){
                                    JsfUtil.mensajeError("No puede eliminar esta arma porque ya ha sido eliminada");
                                    return;
                                }
                                for(SspCarteraArma tarjetaActual: registro.getSspCarteraArmaList()){
                                    if(Objects.equals(tarjetaActual.getId(), tarjeta.getId()) && (tarjetaActual.getActualizacion() != null && tarjetaActual.getId() < 0) ){
                                        registro.getSspCarteraArmaList().remove(tarjetaActual);
                                        abrirPopUp = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }else{
                        JsfUtil.mensajeError("Por favor seleccione al menos un arma");
                        return;
                    }
                    break;
            case 3: // Vehiculos
                    if(!lstVehiculosSeleccionados.isEmpty()){
                        if(lstVehiculosSeleccionados.size() > 1){
                           lstVehiculosSeleccionados = new ArrayList();
                           JsfUtil.mensajeError("Solo puede seleccionar un registro a la vez para eliminar");
                           return;
                        }else{
                            for(SspCarteraVehiculo veh: lstVehiculosSeleccionados){
                                if(veh.getActualizacion() != null && veh.getActualizacion().toString().equals("0")){
                                    JsfUtil.mensajeError("No puede eliminar este vehículo porque ya ha sido eliminado");
                                    return;
                                }
                                for(SspCarteraVehiculo vehActual: registro.getSspCarteraVehiculoList()){
                                    if(Objects.equals(veh.getId(), vehActual.getId()) && (vehActual.getActualizacion() != null && vehActual.getId() < 0) ){
                                        registro.getSspCarteraVehiculoList().remove(vehActual);
                                        abrirPopUp = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }else{
                        JsfUtil.mensajeError("Por favor seleccione al menos un vehículo");
                        return;
                    }
                    break;
        }
        tipoEliminacionDeclaracion = tipo;
        if(abrirPopUp){
            RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarEliminar').show()");
            RequestContext.getCurrentInstance().update("frmConfirmarEliminar");
        }   
    }
    
    public void confirmarEliminar(){
        switch(tipoEliminacionDeclaracion){
            case 1: // Personal de Seguridad
                    if(motivoSeguridad == null || motivoSeguridad.isEmpty()){
                        JsfUtil.mensajeError("Por favor ingrese el motivo de eliminación.");
                        return;
                    }
                    for(Map seg: lstPersonalSeguridadSeleccionados){
                        for(Map perSeg: lstPersonalSeguridad){
                            if(Objects.equals(seg, perSeg)){
                                
                                for(Map mapEliminado : lstPersonalSeguridadMotivosEliminados){
                                    if(Objects.equals(mapEliminado.get("carne"), perSeg.get("carne"))){
                                        lstPersonalSeguridadMotivosEliminados.remove(mapEliminado);
                                        break;
                                    }
                                }
                                
                                Map nmap = new HashMap();
                                nmap.put("id", seg.get("id"));
                                nmap.put("doc", seg.get("doc"));
                                nmap.put("carne", seg.get("carne"));
                                nmap.put("carneId", seg.get("carneId"));
                                nmap.put("carneEntity", seg.get("carneEntity"));
                                nmap.put("motivoSeguridad", motivoSeguridad);
                                lstPersonalSeguridadMotivosEliminados.add(nmap);
                                lstPersonalSeguridad.remove(seg);
                                break;
                            }
                        }
                    }
                    lstPersonalSeguridadSeleccionados = new ArrayList();
                    motivoSeguridad = null;
                    JsfUtil.mensaje("Registro eliminado correctamente");
                    RequestContext.getCurrentInstance().update(obtenerForm()+":pnlRelacionPersonal");
                    break;
                    
            case 2: // Armas
                    for(SspCarteraArma tarjeta: lstArmasSeleccionadas){
                        for(SspCarteraArma tarjetaActual: registro.getSspCarteraArmaList()){
                            if(Objects.equals(tarjetaActual.getId(), tarjeta.getId())){
                                
                                for(Map mapEliminado : lstArmasMotivosEliminadas){
                                    if(Objects.equals(Long.parseLong(""+mapEliminado.get("armaId")), tarjeta.getArmaId().getId() )){
                                        lstArmasMotivosEliminadas.remove(mapEliminado);
                                        break;
                                    }
                                }
                                
                                Map nmap = new HashMap();
                                nmap.put("armaId", tarjeta.getArmaId().getId());
                                nmap.put("motivoSeguridad", motivoSeguridad);
                                lstArmasMotivosEliminadas.add(nmap);
                                
                                registro.getSspCarteraArmaList().remove(tarjetaActual);
                                break;
                            }
                        }
                    }
                    lstArmasSeleccionadas = new ArrayList();
                    motivoSeguridad = null;
                    JsfUtil.mensaje("Registro eliminado correctamente");
                    RequestContext.getCurrentInstance().update(obtenerForm()+":pnlRelacionArmas");
                    break;
                    
            case 3: // Vehiculos
                    for(SspCarteraVehiculo veh: lstVehiculosSeleccionados){
                        for(SspCarteraVehiculo vehActual: registro.getSspCarteraVehiculoList()){
                            if(Objects.equals(veh.getId(), vehActual.getId())){
                                
                                for(Map mapEliminado : lstVehiculosMotivosEliminados){
                                    if(Objects.equals(Long.parseLong(""+mapEliminado.get("id")), veh.getId() )){
                                        lstVehiculosMotivosEliminados.remove(mapEliminado);
                                        break;
                                    }
                                }
                                
                                Map nmap = new HashMap();
                                nmap.put("id", veh.getId());
                                nmap.put("motivoSeguridad", motivoSeguridad);
                                lstVehiculosMotivosEliminados.add(nmap);
                                
                                registro.getSspCarteraVehiculoList().remove(vehActual);
                                break;
                            }
                        }
                    }
                    lstVehiculosSeleccionados = new ArrayList();
                    motivoVehiculo = null;
                    JsfUtil.mensaje("Registro eliminado de la lista");
                    RequestContext.getCurrentInstance().update(obtenerForm()+":pnlRelacionvehiculos");
                    break;                    
        }
        RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarEliminar').hide()");
    }
    
    public boolean getEsDeclaracion(){
        return (registro.getFechaActualizacion() != null);
    }

    public List<Map> getLstPersonalSeguridadContratoActivos() {
        List<Map> lstTemp = new ArrayList();
        if(lstPersonalSeguridad != null){
            for(Map nmap : lstPersonalSeguridad){
                if(nmap.get("declarado") == null || (nmap.get("declarado") != null && nmap.get("declarado").toString().equals("1") ) ){
                    lstTemp.add(nmap);
                }
            }
        }
        return lstTemp;
    }

    public String obtenerEstiloTexto(Map nmap){
        String estilo = "";        
        if(nmap != null){
            if( nmap.get("declarado") == null){
                return "text-decoration: none;";
            }else{
                switch(Integer.parseInt(nmap.get("declarado").toString())){
                    case 0:
                            estilo = "text-decoration: line-through;";                            
                            break;
                    case 1:
                            estilo = "text-decoration: underline;";
                            break;
                    default:
                            estilo = "text-decoration: none;";
                            break;
                }
            }
        }
        return estilo;
    }
    
    public void openDlgMotivo(String motivo){
        motivoSeguridad = motivo;
        RequestContext.getCurrentInstance().execute("PF('wvDlgMotivo').show()");
        RequestContext.getCurrentInstance().update("frmMotivo");
    }
    
    public List<SspCarteraArma>  getListadoArmasContratoActivo(){
        List<SspCarteraArma> lstArmas = new ArrayList();
        if(registro != null){
            for(SspCarteraArma ls : registro.getSspCarteraArmaList()){
                if(ls.getActivo() == 1 && ls.getArmaId().getActivo() == 1 && (ls.getActualizacion() == null || (ls.getActualizacion() != null && ls.getActualizacion().toString().equals("1"))) ){
                    lstArmas.add(ls);
                }
            }
        }
        return lstArmas;
    }
    
    public List<SspCarteraVehiculo>  getListadoVehiculosContratoActivo(){
        List<SspCarteraVehiculo> lstVehiculo = new ArrayList();
        if(registro != null){
            for(SspCarteraVehiculo ls : registro.getSspCarteraVehiculoList() ){
                if(ls.getActivo() == 1 && ls.getVehiculoId().getActivo() == 1 && (ls.getActualizacion() == null || (ls.getActualizacion() != null && ls.getActualizacion().toString().equals("1"))) ){
                    lstVehiculo.add(ls);
                }
            }
        }   
        return lstVehiculo;
    }
    
    public String obtenerEstiloTextoCharacter(Character tipo){
        String estilo = "";        
        
        if( tipo == null){
            return "text-decoration: none;";
        }else{
            switch(Integer.parseInt(tipo.toString())){
                case 0:
                        estilo = "text-decoration: line-through;";                            
                        break;
                case 1:
                        estilo = "text-decoration: underline;";
                        break;
                default:
                        estilo = "text-decoration: none;";
                        break;
            }
        }
        
        return estilo;
    }

    /**
     * FUNCION PARA GENERAR CONSTANCIA DE CARTERA DE CLIENTES
     * @author Richar Fernández
     * @version 1.0
     * @param reg
     * @return Reporte pdf
     */
    public StreamedContent imprimirConstancia(Map reg){
        try {
            System.out.println("id->"+reg.get("ID"));
            registro = ejbSspCarteraClienteFacade.buscarCarteraCliById(Long.parseLong(""+reg.get("ID")));

            List<SspCarteraCliente> lp = new ArrayList();
            lp.add(registro);
            System.out.println("registro.getId->"+registro.getId());
            if (JsfUtil.buscarPdfRepositorio(""+registro.getId(), ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor())) {
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor(), registro.getId() + ".pdf", registro.getId() + ".pdf", "application/pdf");
            }else{
                JsfUtil.subirPdfSel(""+registro.getId(), parametrosPdf(registro), lp, "/aplicacion/gssp/sspCarteraCliente/reportes/constanciaCartera.jasper", ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor());
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carteraClientes").getValor(), registro.getId() + ".pdf", registro.getId() + ".pdf", "application/pdf");
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
     * @param ppdf Registro de regularizacion de arma
     * @return Listado de parámetros para constancia
     */
    private HashMap parametrosPdf(SspCarteraCliente ppdf){
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();

        preparaDatosCarteraCliente();
        
        ph.put("P_FECHASTRING", ReportUtil.mostrarFechaString(ppdf.getFecha()));
        ph.put("P_LOGOSUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        ph.put("P_MARCAAGUA", ec.getRealPath("/resources/imagenes/marcaAgua_GSSP.png"));
        ph.put("P_EXPEDIENTE", ((ppdf.getExpediente() != null)?ppdf.getExpediente().toString():"S/N") );
        ph.put("P_SEDE", "" ); //ppdf.getSedeId().getDireccion() + " - " + ppdf.getSedeId().getDistritoId().getNombre() + "/" + ppdf.getSedeId().getDistritoId().getProvinciaId().getNombre() + "/" + ppdf.getSedeId().getDistritoId().getProvinciaId().getDepartamentoId().getNombre()
        ph.put("P_ADMINISTRADO", ppdf.getAdministradoId().getRznSocial() + ", RUC: " + ppdf.getAdministradoId().getRuc()  );
        ph.put("P_REPRESENTANTE", ""  );    // ppdf.getReprAdminId().getNombreCompleto() + " / " + ppdf.getReprAdminId().getNumDoc()
        ph.put("P_MODALIDADES",  obtenerListadoModalidaesImpresion() );
        ph.put("P_TIPO_CLIENTE", obtenerTipoDocView(tipoDoc));
        ph.put("P_TIPODOC_CLIENTE", (getEsRuc())?"RUC":"Documento");
        ph.put("P_DOC_CLIENTE", numDoc);
        ph.put("P_TIPODESC_CLIENTE", (getEsRuc()?"Razón Social":"Nombres y Apellidos"));
        ph.put("P_NOMBRE_CLIENTE", (getEsRuc()?ppdf.getClienteId().getRznSocial():(ppdf.getClienteId().getNombreCompleto())));
        if(isRenderPnlRepLegal()){
            ph.put("P_TIPO_REPRESENTANTE", obtenerTipoDocView(tipoDocRepLegal));
            ph.put("P_TIPODOC_REPRESENTANTE", "Documento");
            ph.put("P_DOC_REPRESENTANTE", ((numDocRepLegal != null)?numDocRepLegal.trim():""));
            ph.put("P_NOMBRE_REPRESENTANTE", ((ppdf.getReprCliId() != null)?ppdf.getReprCliId().getNombreCompleto():""));        
            ph.put("P_TIPODESC_REPRESENTANTE", "Nombres y Apellidos");
        }
        ph.put("P_FECHA_INICIO", ReportUtil.formatoFechaDdMmYyyy(ppdf.getFechaInicio()));
        ph.put("P_FECHA_FIN", (ppdf.getFechaFin() != null?ReportUtil.formatoFechaDdMmYyyy(ppdf.getFechaFin()):"Indefinido") );
        
        if(ppdf.getSspLugarServicioList() != null && !ppdf.getSspLugarServicioList().isEmpty()){
            
            for(SspLugarServicio itemSspLugarServicio : ppdf.getSspLugarServicioList()){
                String xActivo = itemSspLugarServicio.getActivo()+"";           
                if(xActivo.equals("1")){
                    ph.put("P_DIRECCION_SERVICIO", itemSspLugarServicio.getDireccion() + " - " + itemSspLugarServicio.getDistId().getNombre() + "/" + itemSspLugarServicio.getDistId().getProvinciaId().getNombre() + "/" + itemSspLugarServicio.getDistId().getProvinciaId().getDepartamentoId().getNombre()  );
                    ph.put("P_REFERENCIA_SERVICIO", itemSspLugarServicio.getReferencia() );
                }else{
                    ph.put("P_DIRECCION_SERVICIO", ""  );
                    ph.put("P_REFERENCIA_SERVICIO", "" );
                }
            }
            
        }else{
            ph.put("P_DIRECCION_SERVICIO", ""  );
            ph.put("P_REFERENCIA_SERVICIO", "" );
        }
        ph.put("P_NUM_PERSONALSEGURIDAD", contarPersonalSeguridadCarteraClientes());
        ph.put("P_NUM_ARMAS", contarArmasCarteraClientes());
        ph.put("P_NUM_VEHICULOS", contarVehiculosCarteraClientes());
        ph.put("P_FLAG_CLIENTE", isRenderPnlPersona());
        ph.put("P_FLAG_REPRESENTANTE", isRenderPnlRepLegal());
        ph.put("P_FLAG_PERSONALSEGURIDAD", flagPnlVigilantesImpresion);
        ph.put("P_FLAG_PERSONALSEGURIDAD_FIN", isRenderPnlVigilantes());
        ph.put("P_FLAG_ARMAS", flagPnlArmasImpresion);
        ph.put("P_FLAG_ARMAS_FIN", isRenderPnlArmas());
        ph.put("P_FLAG_VEHICULOS", flagPnlVehiculosImpresion);
        ph.put("P_FLAG_VEHICULOS_FIN", isRenderPnlVehiculos());
        ph.put("P_HASH_QR", ppdf.getHashQr());
        ph.put("P_QR", "https://www.sucamec.gob.pe/sel/faces/qr/constanciaCCli.xhtml?h="+ppdf.getHashQr());

        return ph;
    }
    
    public void preparaDatosCarteraCliente(){
        limpiarDatosImpresion();
        listarModalidadesCarteraCliente(false, registro.getAdministradoId().getRuc());
        eventoSeleccionModalidad(registro,false);
        obtenerRepresentantes(registro);
        
        if(renderPnlPersona){
            if(registro.getClienteId().getRuc() != null){
                numDoc = registro.getClienteId().getRuc();
                if(numDoc.startsWith("1")){  // Ruc que inicia con 1 es Natural Juridica
                    tipoDoc = "2";
                }else{
                    setRenderPnlRepLegal(true);
                    tipoDoc = "1";
                }
            }else{
                numDoc = registro.getClienteId().getNumDoc();
                if(registro.getClienteId().getNumDoc().trim().length() == 8){
                    tipoDoc = "3";
                }else{
                    tipoDoc = "4";
                }
            }
        }
        
        if(renderPnlRepLegal){
            if(registro.getReprCliId() != null){
                if(registro.getReprCliId().getRuc() != null){
                    numDocRepLegal = registro.getReprCliId().getRuc();
                    if(numDocRepLegal.startsWith("1")){
                        tipoDocRepLegal = "2";
                    }else{
                        tipoDocRepLegal = "1";
                    }
                }else{
                    numDocRepLegal = registro.getReprCliId().getNumDoc();
                    if(registro.getReprCliId().getNumDoc().trim().length() == 8){
                        tipoDocRepLegal = "3";
                    }else{
                        tipoDocRepLegal = "4";
                    }
                }
            }
        }
        
        fechaInicioServicio = registro.getFechaInicio();
    }
    
    public List<Map> obtenerListadoModalidaesImpresion(){
        List<Map> lstMod = new ArrayList();
        for(TipoSeguridad ts : registro.getTipoSeguridadList()){
            Map nmap = new HashMap();
            nmap.put("nombre", ts.getNombre());
            lstMod.add(nmap);
        }
        return lstMod;
    }
    
    public int contarPersonalSeguridadCarteraClientes(){
        int cont = 0;
        if(isRenderPnlVigilantes() && registro.getSspCarteraVigilanteList() != null){
            for(SspCarteraVigilante vig : registro.getSspCarteraVigilanteList()){
                if(vig.getActivo() == 1){
                    cont++;
                }
            }
        }
        return cont;
    }
    
    public int contarArmasCarteraClientes(){
        int cont = 0;
        if(isRenderPnlArmas()){
            for(SspCarteraArma arma : registro.getSspCarteraArmaList()){
                if(arma.getActivo() == 1){
                    cont++;
                }
            }
        }
        return cont;
    }
    
    public int contarVehiculosCarteraClientes(){
        int cont = 0;
        if(isRenderPnlVehiculos()){
            for(SspCarteraVehiculo veh : registro.getSspCarteraVehiculoList()){
                if(veh.getActivo() == 1){
                    cont++;
                }
            }
        }
        return cont;
    }
    
    
    ////////////////// CONSORCIO ////////////////////
    
    public void openBuscarConsorcio(){
        if(registro.getTipoSeguridadList() == null || registro.getTipoSeguridadList().isEmpty()){
            JsfUtil.mensajeError("Por favor primero seleccione al menos una modalidad.");
            return;
        }
        
        limpiarDatosConsorcio();
        RequestContext.getCurrentInstance().execute("PF('wvDlgConsorcio').show()");
        RequestContext.getCurrentInstance().update("frmConsorcio");
    }
    
    public List<SbPersonaGt> getListadoConsorcio(){
        List<SbPersonaGt> listaConsorcio = new ArrayList();
        if(registro != null && registro.getSbPersonaList() != null){
            for(SbPersonaGt cons : registro.getSbPersonaList()){
                if(cons.getActivo() == 1){
                    listaConsorcio.add(cons);
                }
            }
        }
        return listaConsorcio;
    }
    
    public void cambiarTipoDocConsorcio(){
        registro.setClienteId(new SbPersonaGt());
        setBlnBusco(false);
        
        if(tipoDocConsorcio != null && !tipoDocConsorcio.isEmpty()){            
            switch(tipoDocConsorcio){
                case "1":
                        maxLength = 11;
                        break;
            }
            direccionRuc = null;
            direccionDni = null;
            ubigeoRuc = null;
            ubigeoDni = null;
            numDocConsorcio = "";
            
        }else{
            JsfUtil.mensajeAdvertencia("Por favor ingrese el tipo de documento");
        }
    }
    
    public void limpiarDatosConsorcio(){
        consorcio = null;
        tipoDocConsorcio = null;
        numDocConsorcio = null;
        disabledBuscoConsorcio = false;
        blnBuscoConsorcio = false;
        esNuevoConsorcio = true;
        maxLengthConsorcio = 0;
        rznSocialConsorcio = "";
        direccionRucConsorcio = "";
        ubigeoRucConsorcio = null;
    }
    
    public void buscarDatosClienteConsorcio(){
        boolean validacion = true;
        consorcio = null;
        
        if(tipoDocConsorcio == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor seleccione el tipo de cliente");
        }
        if(numDocConsorcio == null || numDocConsorcio.isEmpty()){
            validacion = false;            
            if(tipoDocConsorcio != null){
                if(tipoDocConsorcio.equals("1") || tipoDocConsorcio.equals("2")){
                    JsfUtil.mensajeError("Por favor ingresar el ruc");
                }else{
                    JsfUtil.mensajeError("Por favor ingresar el número de documento");
                }
            }else{
                JsfUtil.mensajeError("Por favor ingresar el número de documento");
            }
        }else{
            numDocConsorcio = numDocConsorcio.trim();
        }
        if(validacion){
            switch(tipoDocConsorcio){
                case "1":
                        if (numDocConsorcio.trim().length() != 11) {
                            JsfUtil.mensajeAdvertencia("El ruc es de 11 dígitos ");
                            validacion = false;                
                        }
                        if(!numDocConsorcio.startsWith("2")){
                            JsfUtil.mensajeAdvertencia("El ruc de persona jurídica no puede iniciar un dígito diferente de 2 ");
                            validacion = false;    
                        }
                        break;
                case "2":
                        if (numDocConsorcio.trim().length() != 11) {
                            JsfUtil.mensajeAdvertencia("El ruc es de 11 dígitos ");
                            validacion = false;                
                        }
                        if(!numDocConsorcio.startsWith("1")){
                            JsfUtil.mensajeAdvertencia("El ruc de persona natural no puede iniciar un dígito diferente de 1 ");
                            validacion = false;    
                        }
                        break;
            }
        }
        
        if(validacion){
            setDisabledBuscoConsorcio(false);
            setBlnBuscoConsorcio(true);
            setEsNuevoConsorcio(false);
            boolean encontroSucamec = false;
            SbPersonaGt personaTemp = null;
            consorcio = new SbPersonaGt();
            direccionRucConsorcio = null;            
            ubigeoRucConsorcio = null;
            
            personaTemp = ejbSbPersonaFacade.buscarPersonaSel(null, numDocConsorcio);
            if(personaTemp != null){    // Buscando en registros de sucamec
                consorcio = personaTemp;
                encontroSucamec = true;
                setDisabledBuscoConsorcio(true);
                JsfUtil.mensaje("Se encontró datos de la persona en el sistema");
            }
            
            if(!encontroSucamec){
                setEsNuevoConsorcio(true);
                consorcio.setActivo(JsfUtil.TRUE);
                consorcio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                consorcio.setAudNumIp(JsfUtil.getIpAddress());
                
                switch(tipoDocConsorcio){
                    case "1":
                    case "2":
                            // RUC
                            consorcio.setRuc(numDocConsorcio);
                            personaTemp = wsPideController.buscarSunat(consorcio);
                            if(personaTemp != null){
                                if(personaTemp.getRznSocial() != null){
                                    setDisabledBusco(true);
                                    consorcio.setNumDoc(null);
                                    consorcio.setApePat(null);
                                    consorcio.setApeMat(null);
                                    consorcio.setNombres(null);
                                    consorcio.setRznSocial(personaTemp.getRznSocial());
                                    consorcio.setGeneroId(null);
                                    consorcio.setFechaNac(null);
                                }else{
                                    setEsNuevoConsorcio(false);
                                    setBlnBuscoConsorcio(false);
                                    JsfUtil.mensajeAdvertencia("No se encontró datos de la empresa en la SUNAT");
                                }
                            }else{
                                setEsNuevoConsorcio(false);
                                setBlnBuscoConsorcio(false);
                                JsfUtil.mensajeAdvertencia("El servicio de la SUNAT no está disponible");
                            }
                            consorcio.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_RUC"));
                            if(tipoDocConsorcio.equals("1")){ // Juridica con RUC
                                consorcio.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_JUR"));
                            }else{
                                consorcio.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                            }
                            break;
                }
            }
            if(consorcio.getId() != null){
                for(SbDireccionGt dire : consorcio.getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS")){
                        direccionRucConsorcio = dire.getDireccion();
                        ubigeoRucConsorcio = dire.getDistritoId();
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().update("pnlCabecera");
    }
    
    public void agregarConsorcio(){
        if(consorcio != null && blnBuscoConsorcio){
            boolean validacion = true;
            
            for(SbPersonaGt consor : registro.getSbPersonaList()){
                if(consor.getActivo() == 1 && Objects.equals(consor.getRuc(), consorcio.getRuc() ) ){
                    JsfUtil.mensajeError("La emrpesa ya ha sido adicionada.");
                    return;
                }
            }
            
            if(consorcio.getId() == null){
                if(direccionRucConsorcio == null || direccionRucConsorcio.isEmpty()){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor ingrese la dirección");
                }
                if(ubigeoRucConsorcio == null){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor seleccione el ubigeo");
                }
                
                if(validacion){
                    SbDireccionGt direc = new SbDireccionGt();
                    direc.setId(null);
                    direc.setActivo(JsfUtil.TRUE);
                    direc.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    direc.setAudNumIp(JsfUtil.getIpAddress());
                    direc.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DIRECB_FIS"));
                    direc.setViaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_VIA_CAL"));
                    direc.setZonaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_ZON_URB"));                   
                    direc.setPersonaId(consorcio);
                    direc.setDireccion(direccionRucConsorcio);
                    direc.setDistritoId(ubigeoRucConsorcio);
                    direc.setPaisId(ejbSbPaisFacade.obtenerPaisByNombre("PERU"));
                    
                    consorcio.setId(JsfUtil.tempId());
                    consorcio.setSbDireccionList(new ArrayList());
                    consorcio.getSbDireccionList().add(direc);
                }
            }
            if(validacion){
                registro.getSbPersonaList().add(consorcio);
                RequestContext.getCurrentInstance().execute("PF('wvDlgConsorcio').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosAdministrado");
            }
        }else{
            JsfUtil.mensajeError("Por favor busque una empresa para agregarla como consorcio");
        }
    }
    
    public void eliminarConsorcio(SbPersonaGt consorcioReg){
        for(SbPersonaGt cons : registro.getSbPersonaList()){
            if(Objects.equals(cons.getId(), consorcioReg.getId())){
                registro.getSbPersonaList().remove(cons);
                JsfUtil.mensajeError("Se eliminó al consorcio del listado correctamente");
                break;
            }
        }
    }
    
    ///////////////////////////////////////////////////////////
    public void cambiaFechaFinDeclaracion(){
        if(!Objects.equals(fechaFinDeclaracionTemp, registro.getFechaFin())){
            setRenderObservacionFechaFin(true);
        }else{
            setRenderObservacionFechaFin(false);
        }
    }
    
    public void confirmarEvento(){
        RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarEvento').hide()");
    }
    
    public void cancelarEvento(){
        blnEvento = false;
        RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarEvento').hide()");
        RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosAdministradoPG");
        RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosServicio");
    }
    
    public void openDlgConfirmarEvento(){
        if(blnEvento){
            RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmarEvento').show()");
            RequestContext.getCurrentInstance().update("frmConfirmarEvento");    
        }
    }
    
    public String obtenerKeyGoogle(){
        return ejbSbParametroFacade.obtenerParametroXNombre("google_srcapi_key").getValor();
    }
    
    public String obtenerApiGoogle(){
        return ejbSbParametroFacade.obtenerParametroXNombre("google_jsapi").getValor();
    }
    
    public String obtenerMsjeInvalidSize(){
        return JsfUtil.bundleBDIntegrado("sspCarteraCli_fileUpload_InvalidSizeMessage") + " " +
               (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sspCarteraCli_MaxSize").getValor())/1024/1024) + " Mb. "+
               JsfUtil.bundleBDIntegrado("sspCarteraCli_fileUpload_InvalidSizeMessage2");
    }
    
    public boolean renderMapaAdenda(){
        boolean validacion = false;
        if(registro != null && registro.getSspLugarServicioList() != null && !registro.getSspLugarServicioList().isEmpty() ){
            if(registro.getSspLugarServicioList().get(0).getLatitud() == null || 
               registro.getSspLugarServicioList().get(0).getLongitud() == null || 
               registro.getSspLugarServicioList().get(0).getDistId() == null
              ){
                validacion = true;
            }
        }
        return validacion;
    }
    
    public boolean renderPanelLugarServicio(){
        boolean validacion = false;
        
        if(registro != null && registro.getSspLugarServicioList() != null && !registro.getSspLugarServicioList().isEmpty() ){
            validacion = false;
        }else{
            if(registro != null){
                return esMigracion;
            }
        }
        
        return validacion;
    }
    
    public void openDlgMapaAdenda(){
        if(ubigeoAdenda == null){
            JsfUtil.invalidar(obtenerForm()+":ubigeoServicio");
            JsfUtil.mensajeError("Por favor seleccione primero el ubigeo del lugar de servicio");
            return;
        }
            
        modelMap = new DefaultMapModel();
        RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').show()");
        RequestContext.getCurrentInstance().update("frmMapa");
    }
    
    public void openDlgEditarLugarServicio(){
        if(registro != null && registro.getSspLugarServicioList() != null){
            direccionServicio = null;
            ubigeoAdenda = null;
            latitud = null;
            longitud = null;
            referencia = null;
            
            for(SspLugarServicio lugar : registro.getSspLugarServicioList()){
                if(lugar.getActivo() == 1){
                    direccionServicio = lugar.getDireccion();
                    ubigeoAdenda = lugar.getDistId();
                    latitud = lugar.getLatitud();
                    longitud = lugar.getLongitud();
                    referencia = lugar.getReferencia();
                    lugarSelected = new SspLugarServicio();
                    lugarSelected.setDistId(lugar.getDistId());
                    lugarSelected.setLatitud(latitud);
                    lugarSelected.setLongitud(longitud);
                    lugarServicio = lugarSelected;
                }
            }
            RequestContext.getCurrentInstance().execute("PF('wvDlgEditarLugarServicio').show()");
            RequestContext.getCurrentInstance().update("frmEditarLugarServicio");
        }else{
            JsfUtil.mensajeAdvertencia("No puede editar el lugar de servicio");
        }
    }
    
    public void editarServicio(){
        boolean validacion = true;
        if(direccionServicio == null || direccionServicio.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar la dirección del servicio");
            JsfUtil.invalidar(obtenerForm()+":direccionServicio");
        }
        if(ubigeoAdenda == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar el ubigeo del servicio");
            JsfUtil.invalidar(obtenerForm()+":ubigeoServicio");
        }
        if(lugarServicio.getLatitud() == null || lugarServicio.getLatitud().isEmpty() || lugarServicio.getLongitud() == null || lugarServicio.getLongitud().isEmpty() ){
            if ("1".equals(JsfUtil.bundleBDIntegrado("sspCarteraCli_GoogleMapsActivo"))){
                validacion = false;
                JsfUtil.mensajeError("Por favor seleccionar una ubicación en el mapa");
            }
        }
        if(validacion){
            for(SspLugarServicio lugar : registro.getSspLugarServicioList()){
                if(lugar.getActivo() == 1){
                    lugar.setLatitud(latitud);
                    lugar.setLongitud(longitud);
                    lugar.setDireccion(direccionServicio);
                    lugar.setDistId(ubigeoAdenda);
                    lugar.setReferencia(referencia);
                    lugar = (SspLugarServicio) JsfUtil.entidadMayusculas(lugar, "");
                    break;
                }
            }
            RequestContext.getCurrentInstance().execute("PF('wvDlgEditarLugarServicio').hide()");
            RequestContext.getCurrentInstance().update(obtenerForm()+":pnlDatosServicio");
        }
    }
    
    public void cambiarUbigeoLugarServicioAdenda(){
        lugarServicio.setLatitud(null);
        lugarServicio.setLongitud(null);
        lugarSelected.setLatitud(null);
        lugarSelected.setLongitud(null);
        lugarServicio.setDistId(ubigeoAdenda);
        lugarSelected.setDistId(ubigeoAdenda);
    }
    
    public boolean renderCheckTodosRecursos(){
        boolean validacion = false;

        if(registro.getTipoSeguridadList() != null && !registro.getTipoSeguridadList().isEmpty()){
            for(TipoSeguridad ts : registro.getTipoSeguridadList()){
                if(ts.getActivo() == 1 && ts.getCodProg().equals("TP_MCO_TRA")){
                    validacion = true;
                }
            }
        }
        
        return validacion;
    }
    
    public void cambiaCheckTodosRecursos(){
        eventoSeleccionModalidad(registro, true);
    }
    
    public boolean renderCheckTodosRecursosReferenciado(){
        boolean validacion = false;

        if(registro != null && registro.getContratoId() != null && registro.getContratoId().getTipoSeguridadList() != null && !registro.getContratoId().getTipoSeguridadList().isEmpty()){
            for(TipoSeguridad ts : registro.getContratoId().getTipoSeguridadList()){
                if(ts.getActivo() == 1 && ts.getCodProg().equals("TP_MCO_TRA")){
                    validacion = true;
                }
            }
        }
        
        return validacion;
    }
}
