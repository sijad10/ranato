package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.SbCsEstablecimiento;
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
import java.util.Objects;
import org.primefaces.context.RequestContext;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbCsMedicoEstabsal;
import pe.gob.sucamec.bdintegrado.data.SbCsSuspension;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.Documento;
import pe.gob.sucamec.bdintegrado.data.SbCsHorario;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

// Nombre de la instancia en la aplicacion //
@Named("sbCsEstablecimientoController")
@SessionScoped

/**
 * Clase con SbCsEstablecimientoController instanciada como
 * sbCsEstablecimientoController. Contiene funciones utiles para la entidad
 * SbCsEstablecimiento. Esta vinculada a las páginas
 * SbCsEstablecimiento/create.xhtml, SbCsEstablecimiento/update.xhtml,
 * SbCsEstablecimiento/list.xhtml, SbCsEstablecimiento/view.xhtml Nota: Las
 * tablas deben tener la estructura de Sucamec para que funcione adecuadamente,
 * revisar si tiene el campo activo y modificar las búsquedas.
 */
public class SbCsEstablecimientoController implements Serializable {

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
    SbCsEstablecimiento registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbCsEstablecimiento> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SbCsEstablecimiento> registrosSeleccionados;
    // LISTADO  //
    private Integer tipoBusqueda;
    private Integer estadoCentroSalud;
    private Integer busquedaSuspension;
    private boolean renderComboEstado;
    private boolean renderComboSuspension;
    private boolean renderPropietario;
    private List<SbCsMedicoEstabsal> lstMedicosXEstablecimiento;
    private SbDireccionGt direccionRegistro = new SbDireccionGt();
    private List<SbDireccionGt> lstDireccion;
    private List<TipoBaseGt> lstDiasSemana;
    private Date horaInicio;
    private Date horaFin;
    ///////////
    ////// Mapa //////
    private Double latitudSelected;
    private Double longitudSelected;
    private MapModel modelMap;
    private String latitudEstablecimiento;
    private String longitudEstablecimiento;
    ////////////////////////
    ////// Suspension //////
    private EstadoCrud estadoDlgSuspension;
    private TipoBaseGt tipoSuspension;
    private Expediente expediente;
    private List<Documento> lstDocumentoSuspension;
    private Documento documentoSuspension;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private SbCsSuspension suspension;
    ////////////////////////
    
    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbCsEstablecimientoFacade ejbSbCsEstablecimientoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt ejbSbDistritoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt ejbSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPaisFacadeGt ejbSbPaisFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.DocumentoFacade ejbDocumentoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;

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
    public List<SbCsEstablecimiento> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SbCsEstablecimiento> a) {
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
            for (SbCsEstablecimiento a : registrosSeleccionados) {
                a.setActivo((short) 0);
                ejbSbCsEstablecimientoFacade.edit(a);
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
            for (SbCsEstablecimiento a : registrosSeleccionados) {
                a.setActivo((short) 1);
                ejbSbCsEstablecimientoFacade.edit(a);
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
            registro = (SbCsEstablecimiento) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbSbCsEstablecimientoFacade.edit(registro);
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
            registro = (SbCsEstablecimiento) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbSbCsEstablecimientoFacade.edit(registro);
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
    public SbCsEstablecimiento getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbCsEstablecimiento registro) {
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
    public ListDataModel<SbCsEstablecimiento> getResultados() {
        return resultados;
    }

    public Integer getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Integer tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public Integer getEstadoCentroSalud() {
        return estadoCentroSalud;
    }

    public void setEstadoCentroSalud(Integer estadoCentroSalud) {
        this.estadoCentroSalud = estadoCentroSalud;
    }

    public Integer getBusquedaSuspension() {
        return busquedaSuspension;
    }

    public void setBusquedaSuspension(Integer busquedaSuspension) {
        this.busquedaSuspension = busquedaSuspension;
    }

    public boolean isRenderComboEstado() {
        return renderComboEstado;
    }

    public void setRenderComboEstado(boolean renderComboEstado) {
        this.renderComboEstado = renderComboEstado;
    }

    public boolean isRenderPropietario() {
        return renderPropietario;
    }

    public void setRenderPropietario(boolean renderPropietario) {
        this.renderPropietario = renderPropietario;
    }

    public List<SbCsMedicoEstabsal> getLstMedicosXEstablecimiento() {
        return lstMedicosXEstablecimiento;
    }

    public void setLstMedicosXEstablecimiento(List<SbCsMedicoEstabsal> lstMedicosXEstablecimiento) {
        this.lstMedicosXEstablecimiento = lstMedicosXEstablecimiento;
    }

    public SbDireccionGt getDireccionRegistro() {
        return direccionRegistro;
    }

    public void setDireccionRegistro(SbDireccionGt direccionRegistro) {
        this.direccionRegistro = direccionRegistro;
    }

    public List<SbDireccionGt> getLstDireccion() {
        return lstDireccion;
    }

    public void setLstDireccion(List<SbDireccionGt> lstDireccion) {
        this.lstDireccion = lstDireccion;
    }

    public List<TipoBaseGt> getLstDiasSemana() {
        return lstDiasSemana;
    }

    public void setLstDiasSemana(List<TipoBaseGt> lstDiasSemana) {
        this.lstDiasSemana = lstDiasSemana;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public Double getLatitudSelected() {
        return latitudSelected;
    }

    public void setLatitudSelected(Double latitudSelected) {
        this.latitudSelected = latitudSelected;
    }

    public Double getLongitudSelected() {
        return longitudSelected;
    }

    public void setLongitudSelected(Double longitudSelected) {
        this.longitudSelected = longitudSelected;
    }

    public MapModel getModelMap() {
        return modelMap;
    }

    public void setModelMap(MapModel modelMap) {
        this.modelMap = modelMap;
    }

    public String getLatitudEstablecimiento() {
        return latitudEstablecimiento;
    }

    public void setLatitudEstablecimiento(String latitudEstablecimiento) {
        this.latitudEstablecimiento = latitudEstablecimiento;
    }

    public String getLongitudEstablecimiento() {
        return longitudEstablecimiento;
    }

    public void setLongitudEstablecimiento(String longitudEstablecimiento) {
        this.longitudEstablecimiento = longitudEstablecimiento;
    }

    public EstadoCrud getEstadoDlgSuspension() {
        return estadoDlgSuspension;
    }

    public void setEstadoDlgSuspension(EstadoCrud estadoDlgSuspension) {
        this.estadoDlgSuspension = estadoDlgSuspension;
    }

    public TipoBaseGt getTipoSuspension() {
        return tipoSuspension;
    }

    public void setTipoSuspension(TipoBaseGt tipoSuspension) {
        this.tipoSuspension = tipoSuspension;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public List<Documento> getLstDocumentoSuspension() {
        return lstDocumentoSuspension;
    }

    public void setLstDocumentoSuspension(List<Documento> lstDocumentoSuspension) {
        this.lstDocumentoSuspension = lstDocumentoSuspension;
    }

    public Documento getDocumentoSuspension() {
        return documentoSuspension;
    }

    public void setDocumentoSuspension(Documento documentoSuspension) {
        this.documentoSuspension = documentoSuspension;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public SbCsSuspension getSuspension() {
        return suspension;
    }

    public void setSuspension(SbCsSuspension suspension) {
        this.suspension = suspension;
    }

    public boolean isRenderComboSuspension() {
        return renderComboSuspension;
    }

    public void setRenderComboSuspension(boolean renderComboSuspension) {
        this.renderComboSuspension = renderComboSuspension;
    }
        
    public ListDataModel<SbCsEstablecimiento> getResultadosFull() {
        if(resultados == null){
            resultados = new ListDataModel(ejbSbCsEstablecimientoFacade.listarEstablecimientos(""));
        }
        return resultados;
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
    public List<SbCsEstablecimiento> getSelectItems() {
        return ejbSbCsEstablecimientoFacade.findAll();
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
        resultados = new ListDataModel(ejbSbCsEstablecimientoFacade.selectLike(filtro));
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscarMovil() {
        resultados = new ListDataModel(ejbSbCsEstablecimientoFacade.listarEstablecimientos(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        try {
            reiniciarValores();
            registro = (SbCsEstablecimiento) resultados.getRowData();
            resultados = null;
            setRenderPropietario(false);
            for(SbCsMedicoEstabsal medicoEstab : registro.getSbCsMedicoEstabsalList()){
                if(medicoEstab.getActivo() == 1 && medicoEstab.getMedicoId().getActivo() == 1){
                    lstMedicosXEstablecimiento.add(medicoEstab);
                }
            }            
            estado = EstadoCrud.VER;
            latitudSelected = registro.getDireccionId().getGeoLat();
            longitudSelected = registro.getDireccionId().getGeoLong();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar formulario");
        }
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        try {
            reiniciarValores();
            registro = (SbCsEstablecimiento) resultados.getRowData();
            resultados = null;
            seleccionaPropietario(2);
            for(SbCsMedicoEstabsal medicoEstab : registro.getSbCsMedicoEstabsalList()){
                if(medicoEstab.getActivo() == 1 && medicoEstab.getMedicoId().getActivo() == 1){
                    lstMedicosXEstablecimiento.add(medicoEstab);
                }
            }
            estado = EstadoCrud.EDITAR;
            latitudSelected = registro.getDireccionId().getGeoLat();
            longitudSelected = registro.getDireccionId().getGeoLong();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar formulario");
        }
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new SbCsEstablecimiento();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public String editar() {
        try {
            if(validacionCreateEdit()) {
                registro = (SbCsEstablecimiento) JsfUtil.entidadMayusculas(registro, "");
                Calendar c_hoy = Calendar.getInstance();
                if(JsfUtil.getFechaSinHora(c_hoy.getTime()).compareTo(registro.getFechaFin()) > 0){
                    registro.setHabilitado(JsfUtil.FALSE);
                }
                registro.getDireccionId().setGeoLat(latitudSelected);
                registro.getDireccionId().setGeoLong(longitudSelected);
                ejbSbDireccionFacade.edit(registro.getDireccionId());
                JsfUtil.borrarIds(registro.getSbCsHorarioList());
                ejbSbCsEstablecimientoFacade.edit(registro);
                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
                
                return prepareList();
            }
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     */
    public void crear() {
        try {
            registro = (SbCsEstablecimiento) JsfUtil.entidadMayusculas(registro, "");
            ejbSbCsEstablecimientoFacade.create(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado"));
            mostrarCrear();
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SbCsEstablecimiento getSbCsEstablecimiento(Long id) {
        return ejbSbCsEstablecimientoFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SbCsEstablecimientoController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbCsEstablecimientoConverter")
    public static class SbCsEstablecimientoControllerConverterN extends SbCsEstablecimientoControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbCsEstablecimiento.class)
    public static class SbCsEstablecimientoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbCsEstablecimientoController c = (SbCsEstablecimientoController) JsfUtil.obtenerBean("sbCsEstablecimientoController", SbCsEstablecimientoController.class);
            return c.getSbCsEstablecimiento(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbCsEstablecimiento) {
                SbCsEstablecimiento o = (SbCsEstablecimiento) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbCsEstablecimiento.class.getName());
            }
        }
    }
    
    public void cambiaTipoBusqueda(){
        filtro = null;
        estadoCentroSalud = null;
        busquedaSuspension = null;
        setRenderComboSuspension(false);
        setRenderComboEstado(false);
        
        if (tipoBusqueda != null) {
            if(tipoBusqueda.equals(4)){
                setRenderComboEstado(true);    
            }
            if(tipoBusqueda.equals(6)){
                setRenderComboSuspension(true);    
            }
        }
        RequestContext.getCurrentInstance().update("listForm:pnlBusquedaCriterio");
    }
    
    public void buscarBandeja(){
        List<SbCsEstablecimiento> listado = new ArrayList();

        if (tipoBusqueda == null) {
            JsfUtil.mensajeError("Debe seleccionar al menos un criterio de búsqueda para mostrar resultados");
            return;
        }
        
        if (!(((filtro == null || filtro.isEmpty()) && estadoCentroSalud == null && busquedaSuspension == null))) {
            resultados = null;

            HashMap mMap = new HashMap();
            mMap.put("buscarPor", tipoBusqueda);
            mMap.put("ruc", JsfUtil.getLoggedUser().getNumDoc());
            mMap.put("filtro", (filtro != null) ? filtro.trim().toUpperCase() : filtro);
            mMap.put("estadoCentroSalud", estadoCentroSalud );
            mMap.put("busquedaSuspension", busquedaSuspension );

            listado = ejbSbCsEstablecimientoFacade.buscarEstablecimientosByPropietario(mMap);
            if (listado != null) {
                resultados = new ListDataModel(listado);
            }
            reiniciarCamposBusqueda();
        } else {
            JsfUtil.mensajeError("Por favor, ingresar el campo a buscar");
        }
    }
    
    public void precargaListado(){
        List<SbCsEstablecimiento> listado;
        HashMap mMap = new HashMap();
        mMap.put("ruc", JsfUtil.getLoggedUser().getNumDoc());

        listado = ejbSbCsEstablecimientoFacade.buscarEstablecimientosByPropietario(mMap);
        if (listado != null) {
            resultados = new ListDataModel(listado);
        }
    }
    
    public String prepareList(){
        reiniciarCamposBusqueda();
        resultados = null;
        estado = EstadoCrud.BUSCAR;
        precargaListado();
        return "/aplicacion/gamac/sbCsEstablecimiento/List";
    }
    
    public void reiniciarCamposBusqueda(){
        filtro = null;
        tipoBusqueda = null;
        setRenderComboEstado(false);
        setRenderComboSuspension(false);
        estadoCentroSalud = null;
        busquedaSuspension = null;
    }
    
    public String obtenerDescripcionDireccion(SbDireccionGt dire){
        String direccion = "";
        
        if(dire != null){
            if(dire.getViaId() != null){
                if(!dire.getViaId().getCodProg().equals("TP_VIA_OTRO")){
                    direccion = dire.getViaId().getNombre();
                }                
            }
            direccion = direccion + " " + dire.getDireccion();

            if(dire.getNumero() != null){
                direccion = direccion + " " + dire.getNumero();
            }

            direccion = direccion + " - " + dire.getDistritoId().getProvinciaId().getDepartamentoId().getNombre() + "/" +
                                            dire.getDistritoId().getProvinciaId().getNombre() + "/" +
                                            dire.getDistritoId().getNombre();
        }
            
        return direccion;
    }
    
    public String obtenerDescripcionCentroSalud(SbCsEstablecimiento item){
        String desc = "ACTIVO";
        
        if(item != null && item.getHabilitado() == 0){
            return "SUSPENDIDO";
        }
        
        return desc;
    }
    
    public boolean renderBtnEditar(){
        for(String perfil : JsfUtil.getLoggedUser().getPerfiles()){
            if(perfil.equals("AMA_SALUD")){
                return true;
            }
        }        
        return false;
    }
    
    public String obtenerKeyGoogle(){
        return ejbSbParametroFacade.obtenerParametroXNombre("google_srcapi_key").getValor();
    }
    
    public String obtenerApiGoogle(){
        return ejbSbParametroFacade.obtenerParametroXNombre("google_jsapi").getValor();
    }
    
    public void reiniciarValores(){
        setRenderPropietario(true);
        lstMedicosXEstablecimiento = new ArrayList();
        lstDireccion = new ArrayList();
        lstDiasSemana = new ArrayList();
        latitudSelected = null;
        longitudSelected = null;
        horaInicio = null;
        horaFin = null;
    }
    
    public void seleccionaPropietario(Integer tipo) {
        if(registro.getPropietarioId() != null){
            setRenderPropietario(false);
            cargarDireccionesEmpresa();
            
            if(tipo == 1){
                registro.setDireccionId(null);
            }
        }else{
            lstDireccion = new ArrayList();
        }
    }
    
    public List<SbDireccionGt> cargarDireccionesEmpresa(){
        lstDireccion = new ArrayList();
        
        if(registro.getPropietarioId() != null){
            if(registro.getPropietarioId().getSbDireccionList() != null){
                for(SbDireccionGt dire : registro.getPropietarioId().getSbDireccionList()){
                    if(dire.getActivo() == 1 && dire.getDistritoId().getId() != 0 && (dire.getTipoId().getCodProg().equals("TP_DIRECB_FIS") || dire.getTipoId().getCodProg().equals("TP_DIRECB_COM")) ){ //distrito = 0 = MIGRACION
                        lstDireccion.add(dire);
                    }
                }
            }
        }   
        
        return lstDireccion;
    }
    
    public String obtenerDescPropietario(SbPersonaGt prop) {
        String nombre = "";

        if (prop != null) {
            if (prop.getTipoDoc() == null) {
                if (prop.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    nombre = "RUC: " + prop.getRuc() + " - " + prop.getRznSocial();
                } else {
                    nombre = "DNI: " + prop.getNumDoc() + " - " + prop.getApePat() + " " + prop.getApeMat() + " " + prop.getNombres();
                }
            } else {
                nombre = prop.getTipoDoc().getNombre() + ": ";
                if (prop.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")) {
                    nombre += prop.getRuc() + " - " + prop.getRznSocial();
                } else {
                    nombre += prop.getNumDoc() + " - " + prop.getApePat() + " " + prop.getApeMat() + " " + prop.getNombres();
                }
            }
        }

        return nombre;
    }
    
    public void openDlgVerMapa(){
        if(registro.getDireccionId() != null){
            if(registro.getDireccionId().getGeoLat() != null && registro.getDireccionId().getGeoLong() != null){
                latitudSelected = registro.getDireccionId().getGeoLat();
                longitudSelected = registro.getDireccionId().getGeoLong();
                RequestContext.getCurrentInstance().execute("PF('wvDlgVerMapa').show()");
                RequestContext.getCurrentInstance().update("frmVerMapa");
            }else{
                JsfUtil.mensajeError("No se ha registrado las coordenadas de ubicación");
            }
        }else{
            JsfUtil.mensajeError("No se ha registrado la dirección");
        }
    }
    
    public List<SbCsHorario> getHorariosActivos(){
        List<SbCsHorario> lstHorariosActivos = new ArrayList();
        if(registro != null && registro.getSbCsHorarioList() != null){
            for(SbCsHorario horario : registro.getSbCsHorarioList()){
                if(horario.getActivo() == 0){
                    continue;
                }
                lstHorariosActivos.add(horario);
            }            
        }
        return lstHorariosActivos;
    }
    
    public String obtenerNombresDeHorario(SbCsHorario item ){
        String nombres = "";
        if(item == null){
            return nombres;
        }
        
        for(TipoBaseGt dia : item.getTipoBaseList()){
            if(nombres.isEmpty()){
                nombres = dia.getNombre();
            }else{
                nombres += " / " +dia.getNombre();
            }
        }
        
        return nombres;
    }
    
    public String obtenerDesHorarioAtencion(SbCsHorario horario){
        if(horario.getHoraInicio() == null || horario.getHoraFin() == null){
            return null;
        }
        return JsfUtil.formatearFecha(horario.getHoraInicio(), "HH:mm") + " - " + JsfUtil.formatearFecha(horario.getHoraFin(), "HH:mm");
    }
    
    public String obtenerNombreCargoVer(SbCsMedicoEstabsal estab){
        String nombre = "";
        if(estab != null){
            nombre = estab.getCargoId().getNombre();
            if(estab.getEsDelegadoBoolean()){
                //nombre += " " + JsfUtil.bundle("sbCsEstablecimiento_crear_dtMedico_descDelegadoT");
                nombre += " " + "(DELEGADO TITULAR)";
            }
        }
        return nombre;
    }
    
    public List<SbCsSuspension> getListadoSuspensionesCsActivas(){
        List<SbCsSuspension> lstSuspension;
        if(registro == null){
            return null;
        }
        if(registro.getSbCsSuspensionList() == null){
            return null;
        }
        lstSuspension = new ArrayList();
        for(SbCsSuspension suspens : registro.getSbCsSuspensionList()){
            if(suspens.getActivo() == 0){
                continue;
            }
            lstSuspension.add(suspens);
        }
        Collections.sort(lstSuspension, new Comparator<SbCsSuspension>() {
            @Override
            public int compare(SbCsSuspension one, SbCsSuspension other) {
                return other.getId().compareTo(one.getId());
            }
        });
        return lstSuspension;
    }
    
    public String obtenerColorSuspensionActiva(SbCsSuspension item){
        String color = "";
        if(item != null){
            if(item.getFechaFin() == null){
                color = "dtSuspension-activa";
                return color;
            }
            if(JsfUtil.getFechaSinHora(item.getFechaFin()).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0 ){
                if(item.getEstablecimientoId().getHabilitado() == 0){
                    color = "dtSuspension-activa";
                }else{
                    color = "";
                }
            }
        }
        return color;
    }
    
    public void openDlgVerSuspensionCs(SbCsSuspension item){
        limpiaDatosSuspension();
        suspension = item;
        cargarDatosSuspension();
        RequestContext.getCurrentInstance().execute("PF('wvVerSuspension').show()");
        RequestContext.getCurrentInstance().update("verSuspensionForm");
    }
    
    public void limpiaDatosSuspension(){
        tipoSuspension = null;
        expediente = null;
        lstDocumentoSuspension = null;
        documentoSuspension = null;
        descripcion = "";
        fechaInicio = null;
        fechaFin = null;
        suspension = null;
    }
    
    public void cargarDatosSuspension(){
        tipoSuspension = suspension.getTipoId();
        fechaInicio = suspension.getFechaInicio();
        fechaFin = suspension.getFechaFin();
        List<Expediente> lstExpediente = ejbExpedienteFacade.selectExpedientesSuspensionCSByNroExp(suspension.getNroExpediente());
        if(lstExpediente != null && !lstExpediente.isEmpty()){
            expediente = lstExpediente.get(0);
        }
        descripcion = suspension.getDescripcion();
        if(expediente != null){
            lstDocumentoSuspension = ejbDocumentoFacade.listDocumentosSuspensionCS(expediente.getNumero());
        }
        if(suspension.getNroDocSustento() != null && !suspension.getNroDocSustento().equals("S/N")){
            documentoSuspension = ejbDocumentoFacade.buscarDocumentoByNroDoc(suspension.getNroDocSustento());    
        }
    }
    
    public String obtenerForm(){
        String nombreForm = "";
        switch(estado){            
            case CREAR:
                    nombreForm = "createForm";
                    break;
            case EDITAR:
                    nombreForm = "editForm";
                    break;
            case VER:
                    nombreForm = "viewForm";
                    break;
        }
        return nombreForm;
    }
    
    ////////////// Mapa //////////////
    public String obtenerMapCenter(){
        if(latitudSelected != null && longitudSelected != null){
            LatLng coord1 = new LatLng(latitudSelected, longitudSelected);
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
        latitudSelected = latlng.getLat();
        longitudSelected = latlng.getLng();
        
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
                latitudSelected = center.getLat();
                longitudSelected = center.getLng();
                modelMap.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
                break;
            }
        }
    }
    
    public void confirmarMapa(){
        if(latitudSelected != null && longitudSelected != null){
            registro.getDireccionId().setGeoLat(latitudSelected);
            registro.getDireccionId().setGeoLong(longitudSelected);
            RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').hide()");
            RequestContext.getCurrentInstance().update(obtenerForm());
        }else{
            JsfUtil.mensajeError("Por favor seleccione un punto en el mapa");
        }
    }
    
    public void cancelarMapa(){
        latitudSelected = registro.getDireccionId().getGeoLat();
        longitudSelected = registro.getDireccionId().getGeoLong();
        RequestContext.getCurrentInstance().execute("PF('wvDlgBuscarMapa').hide()");
    }
 
    public boolean getEsSuspensionTemporal(){
        if(tipoSuspension != null){
            if(tipoSuspension.getCodProg().equals("TP_ALTA_TMP")){
                return true;
            }
        }
        return false;
    }
    
    public String obtenerDescDocumentoSustento(Documento docSustento){
        if(docSustento != null){
            return docSustento.getNumero() + " (" + JsfUtil.formatoFechaDdMmYyyy(docSustento.getFechaCreacion()) + ")";
        }
        return null;
    }
    
    public List<TipoBaseGt> obtenerDiaSemana(){
        return ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_DIA");
    }
    
    public void agregarHorario(){
        if(!validarAdicionarHorario()){
            return;
        }
        SbCsHorario horario 
                = new SbCsHorario(
                            JsfUtil.tempId(),                  // id
                            horaInicio,                         // horaInicio
                            horaFin,                            // horaFin
                            (short) 1,                          // activo
                            JsfUtil.getLoggedUser().getLogin(), // audLogin
                            JsfUtil.getIpAddress(),             // audNumIp
                            lstDiasSemana,                      // tipoBaseList
                            registro                            // establecimientoId
                          );
        
        registro.getSbCsHorarioList().add(horario);
        limpiarDatosHorario();
    }
    
    public void limpiarDatosHorario(){
        horaInicio = null;
        horaFin = null;
        lstDiasSemana = new ArrayList();
    }
    
    public boolean validarAdicionarHorario(){
        boolean validacion = true;
        
        if(horaInicio == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":horaInicio");
            JsfUtil.mensajeError("Por favor ingresar la hora de inicio");
        }
        if(horaFin == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":horaFin");
            JsfUtil.mensajeError("Por favor ingresar la hora de fin");
        }
        if(horaInicio != null && horaFin != null){
            if(horaInicio.compareTo(horaFin) >= 0){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":horaInicio");
                JsfUtil.invalidar(obtenerForm()+":horaFin");
                JsfUtil.mensajeError("La fecha de inicio no puede ser mayor o igual a la fecha de fin");
            }
        }
        if(lstDiasSemana == null || lstDiasSemana.isEmpty()){
            validacion = false;            
            JsfUtil.invalidar(obtenerForm()+":diasSemana");
            JsfUtil.mensajeError("Por favor seleccione al menos un día de la semana");
        }else{
            if(registro.getSbCsHorarioList() != null && validacion){
                Calendar fechaTemp = Calendar.getInstance();
                Date hoy = new Date();
                fechaTemp.set(JsfUtil.formatoFechaYyyy(hoy), hoy.getMonth(), hoy.getDate(),horaInicio.getHours(), horaInicio.getMinutes(),0 );
                horaInicio = (fechaTemp.getTime());
                fechaTemp.set(JsfUtil.formatoFechaYyyy(hoy), hoy.getMonth(), hoy.getDate(),horaFin.getHours(), horaFin.getMinutes(),0 );
                horaFin = (fechaTemp.getTime());
                
                for(SbCsHorario horario : registro.getSbCsHorarioList()){
                    if(horario.getActivo() == 0){
                        continue;
                    }
                    for(TipoBaseGt diaHorario : horario.getTipoBaseList()){                        
                        for(TipoBaseGt dias : lstDiasSemana){
                            if(Objects.equals(dias.getId(), diaHorario.getId())){   // Mismo dia ingresado
                                if(JsfUtil.getFechaSoloHora(horaInicio).compareTo(JsfUtil.getFechaSoloHora(horario.getHoraInicio())) >= 0 &&   
                                   JsfUtil.getFechaSoloHora(horaInicio).compareTo(JsfUtil.getFechaSoloHora(horario.getHoraFin())) <= 0
                                  ){
                                    validacion = false;                                    
                                }
                                if(JsfUtil.getFechaSoloHora(horaFin).compareTo(JsfUtil.getFechaSoloHora(horario.getHoraInicio())) >= 0 &&   
                                   JsfUtil.getFechaSoloHora(horaFin).compareTo(JsfUtil.getFechaSoloHora(horario.getHoraFin())) <= 0
                                  ){
                                    validacion = false;
                                }
                                if(JsfUtil.getFechaSoloHora(horaInicio).compareTo(JsfUtil.getFechaSoloHora(horario.getHoraInicio())) <= 0 &&   
                                   JsfUtil.getFechaSoloHora(horaFin).compareTo(JsfUtil.getFechaSoloHora(horario.getHoraFin())) >= 0
                                  ){
                                    validacion = false;
                                }
                                if(!validacion){
                                    JsfUtil.mensajeError("En el dia " + dias.getNombre() + " hay un cruce de horario");
                                    return validacion;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return validacion;
    }
    
    public void borrarRegistroHorario(SbCsHorario item){
        if(registro != null && registro.getSbCsHorarioList() != null){
            for(SbCsHorario horario : registro.getSbCsHorarioList()){
                if(horario.getActivo() == 0){
                    continue;
                }
                if(Objects.equals(item.getId(), horario.getId())){
                    if(item.getId() < 0){
                        registro.getSbCsHorarioList().remove(item);
                    }else{
                        horario.setActivo(JsfUtil.FALSE);
                    }
                    JsfUtil.mensaje("Horario eliminado correctamente");
                    return;
                }
            }
        }
    }
    
    public boolean renderBtnEditarSuspension(SbCsSuspension suspension){
        if(suspension == null){
            return false;
        }
        return ( (suspension.getFechaFin() == null) || 
                 (suspension.getFechaFin() != null && JsfUtil.getFechaSinHora(suspension.getFechaFin()).compareTo(JsfUtil.getFechaSinHora(new Date())) >= 0) 
               );
    }
    
    public boolean validacionCreateEdit(){
        boolean validacion = true;
        
        if(registro.getTipoId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, seleccionar el tipo de centro de salud");
        }
        if(registro.getNombre() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingresar la denominación del centro de salud");
        }
        if(registro.getPropietarioId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingresar el propietario del centro de salud");
        }
        if(registro.getDireccionId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, seleccionar la dirección del centro de salud");
        }else{
            if(latitudSelected == null || longitudSelected == null){
                validacion = false;
                JsfUtil.mensajeError("Por favor seleccione las coordenadas del establecimiento de salud");
            }
        }
        if(registro.getNroResFuncionamiento() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingresar el nro. de resolución de GAMAC");
        }
        if(registro.getFechaIni() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingresar la fecha de inicio de la resolución de GAMAC");
        }
        if(registro.getFechaFin() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingresar la fecha de fin de la resolución de GAMAC");
        }else{
            Calendar c_hoy = Calendar.getInstance();
            if(JsfUtil.getFechaSinHora(registro.getFechaFin()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) < 0 ){
                validacion = false;
                JsfUtil.mensajeError("No puede registrar un establecimiento con fecha de autorización GAMAC vencida.");
            }
        }
        if(registro.getFechaIni() != null && registro.getFechaFin() != null){
            if(JsfUtil.getFechaSinHora(registro.getFechaIni()).compareTo(JsfUtil.getFechaSinHora(registro.getFechaFin())) >0 ){
                validacion = false;
                JsfUtil.mensajeError("La fecha de inicio no puede ser mayor que la fecha fin de la resolución GAMAC, por favor verifique");
            }
        }
        
        if(registro.getNroResMinsa() == null || registro.getNroResMinsa().isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":rgMinsa");
            JsfUtil.mensajeError("Por favor, ingresar el nro. de resolución del MINSA");
        }
        if(registro.getFechaIniRgMinsa() == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":fechaIniRgMinsa");
            JsfUtil.mensajeError("Por favor, ingresar la fecha de inicio de la Resolución del MINSA");
        }
        if(registro.getFechaFinRgMinsa() == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":fechaFinRgMinsa");
            JsfUtil.mensajeError("Por favor, ingresar la fecha de fin de la Resolución del MINSA");
        }else{
            Calendar c_hoy = Calendar.getInstance();
            if(JsfUtil.getFechaSinHora(registro.getFechaFinRgMinsa()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) < 0 ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":fechaIniRgMinsa");
                JsfUtil.invalidar(obtenerForm()+":fechaFinRgMinsa");
                JsfUtil.mensajeError("No puede registrar un establecimiento con fecha de autorización del MINSA vencida.");
            }
        }
        if(registro.getFechaIniRgMinsa() != null && registro.getFechaFinRgMinsa() != null){
            if(JsfUtil.getFechaSinHora(registro.getFechaIniRgMinsa()).compareTo(JsfUtil.getFechaSinHora(registro.getFechaFinRgMinsa())) >0 ){
                validacion = false;
                JsfUtil.invalidar(obtenerForm()+":fechaIniRgMinsa");
                JsfUtil.invalidar(obtenerForm()+":fechaFinRgMinsa");
                JsfUtil.mensajeError("La fecha de inicio no puede ser mayor que la fecha fin de la RG del MINSA, por favor verifique");
            }
        }
        if(registro.getSbCsHorarioList().isEmpty()){
            validacion = false;
            JsfUtil.invalidar(obtenerForm()+":diasSemana");
            JsfUtil.mensajeError("Debe ingresar el horario de atención al centro de salud");
        }
        
        return validacion;
    }

    
}