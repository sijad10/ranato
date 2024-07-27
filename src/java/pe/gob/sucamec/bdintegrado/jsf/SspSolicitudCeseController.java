package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.SspSolicitudCese;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SspSolicitudCeseDet;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtil;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

// Nombre de la instancia en la aplicacion //
@Named("sspSolicitudCeseController")
@SessionScoped

/**
 * Clase con SspSolicitudCeseController instanciada como
 * sspSolicitudCeseController. Contiene funciones utiles para la entidad
 * SspSolicitudCese. Esta vinculada a las páginas SspSolicitudCese/create.xhtml,
 * SspSolicitudCese/update.xhtml, SspSolicitudCese/list.xhtml,
 * SspSolicitudCese/view.xhtml Nota: Las tablas deben tener la estructura de
 * Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class SspSolicitudCeseController implements Serializable {

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
    SspSolicitudCese registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SspSolicitudCese> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SspSolicitudCese> registrosSeleccionados;
    ///////////////////////////////////////
    ////////////  REGISTRO  ///////////////
    ///////////////////////////////////////
    private Date currentDate = new Date();
    private String tipoDocBusqueda = "";
    private String numDocBusqueda = null;    
    private String tipoDoc = "";
    private String numDoc = "";
    private String nombreYApellidos = "";
    private String carnet = "";
    private Date fecEmision = null;
    private Date fecvencimiento = null;
    private String estadoCese = "";
    private String modalidad = "";
    private boolean renderDatosVigilante;
    private List<Map> lstVigilanteBusqueda;
    private List<Map> lstVigilante;
    private int limiteReg;
    private String msjeCese;
    private boolean disabledSiguiente;
    ///////////////////////////////////////
    /////////////  LISTADO  ///////////////
    ///////////////////////////////////////
    private Date fechaIni = null;
    private Date fechaFin = null;
    private String paso = "paso1";
    private String tipoBusqueda;
    private boolean muestraBusquedafecha;
    private Date fechaBusqueda;
    private List<SbRecibos> lstRecibos;
    private List<SbRecibos> lstRecibosBusqueda;
    private List<Map> lstInscritos;    
    private List<TipoSeguridad> lstModalidadesActivas;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspSolicitudCeseFacade ejbSolicitudCeseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.SsEmpVigFacade ejbSsEmpVigFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade ejbTipoSeguridadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TrazaFacade ejbTrazaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.CarteraClienteFacade ejbCarteraClienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade ejbSbDerivacionCydocFacade;
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

    /**
     * Propiedad de seleccion multiple
     *
     * @return
     */
    public List<SspSolicitudCese> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SspSolicitudCese> a) {
        this.registrosSeleccionados = a;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getPaso() {
        return paso;
    }

    public void setPaso(String paso) {
        this.paso = paso;
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

    public String getTipoDocBusqueda() {
        return tipoDocBusqueda;
    }

    public void setTipoDocBusqueda(String tipoDocBusqueda) {
        this.tipoDocBusqueda = tipoDocBusqueda;
    }

    public String getNumDocBusqueda() {
        return numDocBusqueda;
    }

    public void setNumDocBusqueda(String numDocBusqueda) {
        this.numDocBusqueda = numDocBusqueda;
    }

    public String getNombreYApellidos() {
        return nombreYApellidos;
    }

    public void setNombreYApellidos(String nombreYApellidos) {
        this.nombreYApellidos = nombreYApellidos;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public Date getFecEmision() {
        return fecEmision;
    }

    public void setFecEmision(Date fecEmision) {
        this.fecEmision = fecEmision;
    }

    public Date getFecvencimiento() {
        return fecvencimiento;
    }

    public void setFecvencimiento(Date fecvencimiento) {
        this.fecvencimiento = fecvencimiento;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getEstadoCese() {
        return estadoCese;
    }

    public void setEstadoCese(String estadoCese) {
        this.estadoCese = estadoCese;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
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

    public int getLimiteReg() {
        return limiteReg;
    }

    public void setLimiteReg(int limiteReg) {
        this.limiteReg = limiteReg;
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

    public boolean isDisabledSiguiente() {
        return disabledSiguiente;
    }

    public void setDisabledSiguiente(boolean disabledSiguiente) {
        this.disabledSiguiente = disabledSiguiente;
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
            for (SspSolicitudCese a : registrosSeleccionados) {
                a.setActivo((short) 0);
                ejbSolicitudCeseFacade.edit(a);
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
            for (SspSolicitudCese a : registrosSeleccionados) {
                a.setActivo((short) 1);
                ejbSolicitudCeseFacade.edit(a);
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
            registro = (SspSolicitudCese) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbSolicitudCeseFacade.edit(registro);
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
            registro = (SspSolicitudCese) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbSolicitudCeseFacade.edit(registro);
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
    public SspSolicitudCese getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SspSolicitudCese registro) {
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
    public ListDataModel<SspSolicitudCese> getResultados() {
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
    public List<SspSolicitudCese> getSelectItems() {
        return ejbSolicitudCeseFacade.findAll();
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
        resultados = new ListDataModel(ejbSolicitudCeseFacade.selectLike(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (SspSolicitudCese) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        registro = (SspSolicitudCese) resultados.getRowData();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     * @author Richar Fernández
     * @version 1.0
     */
    public void mostrarCrear() {
        reiniciarValores();
        resultados = null;
        
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        
        registro.setActivo(JsfUtil.TRUE);
        registro.setAudLogin("USRWEB");
        registro.setAudNumIp(JsfUtil.getIpAddress());
        if(per != null){
            registro.setPersonaId(per);
            
            paso = "paso1";
            estado = EstadoCrud.CREAR;
        }else{
            JsfUtil.mensajeError("No se encontró a la persona");
        }
    }

    /**
     * Evento para guardar la información al editar un registro.
     * @author Richar Fernández
     * @version 1.0
     */
    public void editar() {
        try {
            registro = (SspSolicitudCese) JsfUtil.entidadMayusculas(registro, "");
            ejbSolicitudCeseFacade.edit(registro);
            estado = EstadoCrud.BUSCAR;
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     * @return Página a redireccionar
     * @author Richar Fernández
     * @version 1.0
     */
    public String crear() {
        String solicitud = "", usuario = "";
        boolean validacion = true;
        try {
            SbDerivacionCydoc  derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionCyDoc(JsfUtil.getLoggedUser().getSistema(), ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_CCRN").getId(), ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AREA_TRAM").getId());
            if(derivacion != null){
                usuario = derivacion.getUsuarioDestinoId().getLogin();
            }else{
                usuario = "RPUMACAYO";
            }
            registro = (SspSolicitudCese) JsfUtil.entidadMayusculas(registro, "");
            registro.setId(null);
            registro.setActivo(JsfUtil.TRUE);
            registro.setCantidadCese((long) lstVigilante.size());
            registro.setFechaSolicitud(new Date());
            registro.setReciboId(lstRecibos.get(0));
            registro.setTipoSolicitudId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_TIPOCESE_WEB"));
            Calendar hoy = Calendar.getInstance();
            registro.setSspSolicitudCeseDetList(new ArrayList());
            registro.getReciboId().getSspSolicitudCeseList().add(registro);
            registro.getReciboId().setSbReciboRegistroList(new ArrayList());
            
            for(Map vigilante : lstVigilante ){
                SspSolicitudCeseDet det = new SspSolicitudCeseDet();
                Map vigilanteCese = new HashMap();
                det.setId(null);
                det.setSolicitudId(registro);
                det.setActivo(JsfUtil.TRUE);
                det.setNroCarne(vigilante.get("NRO_CRN_VIG").toString());
                det.setEstadoDevoId((long)1);
                //det.setFechaEmision( ((vigilante.get("fecEmision") != null)?((Date) vigilante.get("fecEmision")):null) );
                det.setNroExpCese(crearExpediente(vigilante.get("numDoc_desc").toString(), vigilante.get("nombreYApellidos").toString(),registro.getReciboId().getImporte(), vigilante.get("NRO_CRN_VIG").toString(),vigilante.get("DES_MOD").toString(), usuario));
                
                if(det.getNroExpCese() == null){
                    validacion = false;
                    JsfUtil.mensajeError("Hubo un error al crear el expediente para el cese del carné: "+det.getNroCarne());
                    break;
                }else{
                    Expediente exp = ejbExpedienteFacade.obtenerExpedienteXNumero(det.getNroExpCese());
                    if(exp.getNumeroDisca() == null){
                        if(!exp.getTrazaList().isEmpty()){
                            exp.getTrazaList().get(0).setFechaCreacion(new Date());
                            ejbTrazaFacade.edit(exp.getTrazaList().get(0));
                            exp.setNumeroDisca(ejbExpedienteFacade.mostrarNumeroDisca(exp.getNumero()));
                        }
                    }
                    
                    if(exp.getNumeroDisca() != null){
                        vigilanteCese.put("numero_disca", exp.getNumeroDisca());
                        vigilanteCese.put("ruc", registro.getPersonaId().getRuc());
                        vigilanteCese.put("dni", vigilante.get("NUMDOC"));
                        vigilanteCese.put("modalidad", vigilante.get("TIP_MOD"));
                        vigilanteCese.put("numero", det.getNroExpCese());
                        vigilanteCese.put("TIP_USR", vigilante.get("TIPODOC"));

                        if(ejbSsEmpVigFacade.cesarVigilanteRMA(JsfUtil.getLoggedUser().getLogin(),vigilanteCese)){
                            SbReciboRegistro rec = new SbReciboRegistro();
                            rec.setId(null);
                            rec.setRegistroId(null);
                            rec.setNroExpediente(exp.getNumero());
                            rec.setReciboId(registro.getReciboId());
                            rec.setActivo(JsfUtil.TRUE);
                            registro.getReciboId().getSbReciboRegistroList().add(rec);
                            registro.getSspSolicitudCeseDetList().add(det);
                        }else{
                            validacion = false;
                            JsfUtil.mensajeError("No se generó el cese del vigilante del carné: "+det.getNroCarne());
                            break;
                        }
                    }else{
                        ///// print error por no generar disca /////
                        System.err.println("No se encontró el número Disca, Carnet: "+det.getNroCarne() + 
                                        ", Nro expediente: " + exp.getNumero() + 
                                        ", RUC: " + registro.getPersonaId().getRuc() + 
                                        ", DNI: " + vigilante.get("NUMDOC") + 
                                        ", Modalidad: " + vigilante.get("TIP_MOD"));
                        /////////////////////////////////////////////////////////////////////////////
                        validacion = false;
                        JsfUtil.mensajeError("No se encontró el número Disca para el carnet: "+det.getNroCarne());
                        break;
                    }
                }
            }
            
            if(validacion){
                solicitud = ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_CES").toString() + "-" +  ReportUtil.mostrarAnio(hoy.getTime()) + "-SUCAMEC/GSSP";
                registro.setNroSolicitud(solicitud);
                ejbSolicitudCeseFacade.create(registro);
                
                if(!lstRecibos.isEmpty()){
                    ejbSbRecibosFacade.edit(registro.getReciboId());
                }
                
                JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroCreado") + " " + registro.getId());    
            }
            
            return prepareList();
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
        return null;
    }   
    
    /**
     * FUNCIÓN PARA CREAR EXPEDIENTE DE CESADO
     * @param numDoc Número de documento de vigilante
     * @param nombres Nombres y Apellidos de vigilante
     * @param importeRecibo Importe recibo
     * @param nroCarne Nro. de carné
     * @param modalidad Modalidad
     * @param usuario Usuario a quien se deriva el expediente creado
     * @return Nro. de expediente creado
     * @author Richar Fernández
     * @version 1.0
     */
    public String crearExpediente(String numDoc, String nombres, Double importeRecibo, String nroCarne, String modalidad, String usuario){
        List<Integer> acciones = new ArrayList();
        boolean estadoTraza = true;        
        String nroExpediente = null;
        acciones.add(30);   // Finalizado
        String asunto = numDoc + " " + nombres;
        Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
        if(usuaTramDoc != null){
            nroExpediente = wsTramDocController.crearExpediente_capacitacion(lstRecibos.get(0), registro.getPersonaId() , asunto , 713, usuaTramDoc, usuaTramDoc);
            
            if(nroExpediente != null){
                asunto = "S/."+JsfUtil.bundleBDIntegrado("GsspSolicitudCese_recibo_precio") + " de S/." + importeRecibo + ", nro. de carné: "+ nroCarne +", modalidad:"+modalidad;
                estadoTraza = wsTramDocController.asignarExpediente(nroExpediente, "USRWEB", usuario, asunto, "", acciones,false,null);
                if (!estadoTraza) {
                    JsfUtil.mensajeError("No se pudo generar la traza del expediente. ID exp. : " + nroExpediente);
                }else{
                    estadoTraza = wsTramDocController.archivarExpediente(nroExpediente, usuario, "");
                    if (!estadoTraza) {
                        JsfUtil.mensajeError(" No se pudo archivar el expediente. ID exp. : " + nroExpediente);
                    }
                }
            }
        }else{
            JsfUtil.mensajeError("No se encontró el usuario USRWEB");  
        }            
        
        return nroExpediente;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SspSolicitudCese getSspSolicitudCese(Long id) {
        return ejbSolicitudCeseFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SspSolicitudCeseController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sspSolicitudCeseConverter")
    public static class SspSolicitudCeseControllerConverterN extends SspSolicitudCeseControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SspSolicitudCese.class)
    public static class SspSolicitudCeseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SspSolicitudCeseController c = (SspSolicitudCeseController) JsfUtil.obtenerBean("sspSolicitudCeseController", SspSolicitudCeseController.class);
            return c.getSspSolicitudCese(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SspSolicitudCese) {
                SspSolicitudCese o = (SspSolicitudCese) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SspSolicitudCese.class.getName());
            }
        }
    }
    
    ////////////// LISTADO ////////////////
    /**
     * FUNCION AL CAMBIAR TIPO DE DOCUMENTO
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiaTipoDocumento(){
        numDocBusqueda = null;
    }
    /**
     * FUNCION PARA MOSTRAR LISTADO DE CAPACITACIONES
     * @author Richar Fernández
     * @version 1.0
     * @return Página para direccionar
     */
    public String prepareList(){
        reiniciarCamposBusqueda();
        resultados = null;        
        
        if(!verificarModalidades(JsfUtil.getLoggedUser().getNumDoc())){
            JsfUtil.mensajeError("Ud. no puede ingresar a esta opción porque no cuenta con modalidades disponibles");
            return null;
        }
        
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/gssp/sspSolicitudCese/List";
    }
    
    public boolean verificarModalidades(String ruc){
        int cont = 0;
        lstModalidadesActivas = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_MCO");
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
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_TEC")){
                        lstModalidadesActivas.remove(ts);
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
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_PRO")){
                        lstModalidadesActivas.remove(ts);
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
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_TRA")){
                        lstModalidadesActivas.remove(ts);
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
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_VIG")){
                        lstModalidadesActivas.remove(ts);
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
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_SIS")){
                        lstModalidadesActivas.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////

            //////// EVENTUAL ////////
            cont = 0;
            for(TipoSeguridad ts : lstModalidadesActivas){
                if(ts.getCodProg().equals("TP_MCO_PRO") || ts.getCodProg().equals("TP_MCO_VIG")){
                    cont++;
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_EVE")){
                        lstModalidadesActivas.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////
            
            //////// SERVICIOS DE PROTECCION POR CUENTA PROPIA ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("CUENTA PROPIA")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_CTA")){
                        lstModalidadesActivas.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////
            
            //////// ASESORIA Y CONSULTORÍA ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("CONSULTORIA Y ASESORIA")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_ASC")){
                        lstModalidadesActivas.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////
            
            //////// SERVICIOS INDIVIDUALES DE SEGURIDAD PATRIMONIAL ////////
            cont = 0;
            for(Map modal : lstModalidadResolucion){
                if(modal.get("MODALIDAD").toString().contains("PATRIMONIAL")){
                    cont ++ ;                        
                }
            }
            if(cont == 0){
                for(TipoSeguridad ts : lstModalidadesActivas){
                    if(ts.getCodProg().equals("TP_MCO_PAT")){
                        lstModalidadesActivas.remove(ts);
                        break;
                    }
                }
            }
            ////////////////////////
        }
        if(lstModalidadesActivas != null && !lstModalidadesActivas.isEmpty()){
            return true;
        }
        return false;
    }
    
    /**
     * BUSCAR LISTADO DE CAPACITACIONES
     * @author Richar Fernández
     * @version 1.0
     */
    public void buscarBandeja(){
        if(!((fechaIni != null && fechaFin == null) || (fechaIni == null && fechaFin != null))){
            List<SspSolicitudCese> listado = null;
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

            HashMap mMap = new HashMap();
            mMap.put("fechaIni", fechaIni);
            mMap.put("fechaFin", fechaFin);
            mMap.put("personaId", per.getId());

            listado = ejbSolicitudCeseFacade.buscarSolicitudesCese(mMap);
            if(listado != null){
                resultados = new ListDataModel(listado);
            }
            reiniciarCamposBusqueda();        
        }else{
            JsfUtil.mensajeError("Por favor, ingresar ambas fecha");
        }
    }
    
    /**
     * OBTENER DESCRIPCION DE ESTADO DE CESE DE VIGILANTE (PENDIENTE/ENTREGADO)
     * @param lstDetalle
     * @return Cadena con estado de solicitud
     * @author Richar Fernández
     * @version 1.0
     */
    public String obtenerEstado(List<SspSolicitudCeseDet> lstDetalle){
        String estadoSol = "PENDIENTE";
        int cont = 0;
        if(lstDetalle != null){
            for(SspSolicitudCeseDet sol : lstDetalle){
                if(sol.getEstadoDevoId() == 1){
                    cont ++;
                }
            }
            
            if(cont == 0){
                estadoSol = "ENTREGADO";
            }
        }
        return estadoSol;
    }
    
    /**
     * LIMPIAR CAMPOS DE BUSQUEDA DE LISTADO
     * @author Richar Fernández
     * @version 1.0
     */
    public void reiniciarCamposBusqueda(){
        filtro = null;        
        fechaIni = null;
        fechaFin = null;
        lstRecibos = new ArrayList();
        lstRecibosBusqueda = new ArrayList();
    }

    /**
     * VALIDACION DE BOTÓN SIGUIENTE EN FORMULARIO
     * @author Richar Fernández
     * @version 1.0
     * @param p Pestaña a redireccionar (Paso1,Paso2)
     */
    public void siguiente(String p){        
        boolean validacion = true;
        switch (p) {
            case "paso12":
                if(lstRecibos.isEmpty()){
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("Por favor, ingrese un comprobante de pago");
                }
                if(validacion)
                    paso = "paso2";
                break;
            default:
                paso = p;
                break;
        }
        
    }
    
    /**
     * LIMPIAR VALORES DE REGISTRO
     * @author Richar Fernández
     * @version 1.0
     */
    public void reiniciarValores(){
        registro = new SspSolicitudCese();
        lstRecibos = new ArrayList();
        tipoDocBusqueda = null;
        numDocBusqueda = null;
        tipoDoc = null;
        numDoc = null;
        nombreYApellidos = null;
        carnet = null;
        fecEmision = null;
        fecvencimiento = null;
        estadoCese = null;
        modalidad = null;
        limiteReg = 0;
        msjeCese = "";
        setRenderDatosVigilante(false);
        setDisabledSiguiente(true);
        lstVigilanteBusqueda = new ArrayList();
        lstVigilante = new ArrayList();
    }
    
    /**
    * CAMBIAR CAMPO FECHA DE BUSQUEDA
    * @author Richar Fernández
    * @version 1.0
    */
    public void cambiaTipoBusquedaComprobante(){
        filtro = null;
        fechaBusqueda = null;
        setMuestraBusquedafecha(false);
        if(tipoBusqueda != null){
            if(tipoBusqueda.equals("fecha")){                
                setMuestraBusquedafecha(true);
            }
        }
    }
    
    /**
     * BUSCAR COMPROBANTE DE PAGO
     * @author Richar Fernández
     * @version 1.0
     */
    public void buscarDlgComprobante(){
        try{
            if(tipoBusqueda != null){
                if (!(tipoBusqueda != null && ((filtro == null || filtro.isEmpty()) && fechaBusqueda == null))) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    HashMap mMap = new HashMap();
                    mMap.put("tipo", tipoBusqueda);
                    mMap.put("filtro", filtro);
                    mMap.put("fecha", fechaBusqueda);
                    mMap.put("importe", Double.parseDouble(JsfUtil.bundleBDIntegrado("GsspSolicitudCese_recibo_precio")) );
                    mMap.put("fechaLimite", sdf.parse(JsfUtil.bundleBDIntegrado("Parametrizacion_recibo_fecha")) );
                    mMap.put("codigo", Long.parseLong(JsfUtil.bundleBDIntegrado("GsspSolicitudCese_recibo_codigo")) );

                    lstRecibosBusqueda = ejbRecibosFacade.listarRecibosByImporteByCodAtributo(registro.getPersonaId() , mMap);
                }else{
                    JsfUtil.mensajeAdvertencia("Por favor escriba el criterio de búsqueda.");
                } 
            }else{
                JsfUtil.mensajeAdvertencia("Por favor seleccione un tipo de búsqueda.");
            }
        }catch(Exception ex){
            JsfUtil.mensajeAdvertencia("hubo un error al buscar el comprobante de pago.");
        }
    }
    
     /**
     * SELECCIONAR COMPROBANTE DE PAGO
     * @author Richar Fernández
     * @version 1.0
     * @param recibo Recibo Seleccionado
     */
    public void seleccionarDlgComprobante(SbRecibos recibo){
        Double valorReciboXVigilante = Double.parseDouble(JsfUtil.bundleBDIntegrado("GsspSolicitudCese_recibo_precio"));        
        
        if(Math.round(recibo.getImporte()) > 0){
            if(((int)(recibo.getImporte()*100) % (int)(valorReciboXVigilante*100)) == 0){
                limiteReg = (int) Math.floor(recibo.getImporte() / valorReciboXVigilante);
                lstRecibos = new ArrayList();
                lstRecibos.add(recibo);
                setDisabledSiguiente(false);
                
                RequestContext.getCurrentInstance().execute("PF('wvDialogBusquedaComprobante').hide()");
                RequestContext.getCurrentInstance().update("createForm");
            }else{
                JsfUtil.mensajeAdvertencia("El monto del comprobante no es correcto.");
            }
        }else{
            JsfUtil.mensajeAdvertencia("El comprobante debe tener un importe mayor a cero.");
        }
    }
    
    /**
     * OBTENER CANTIDAD DE REGISTROS USADOS SEGÚN RECIBO SELECCIONADO
     * @author Richar Fernández
     * @version 1.0
     * @param recibo seleccionado
     * @return CANTIDAD DE REGISTROS USADOS.
     */
    public int obtenerTotalRecibosUsados(SbRecibos recibo){
        int cont = 0;
        
        if(recibo.getSbReciboRegistroList() != null){
            for(SbReciboRegistro reg : recibo.getSbReciboRegistroList()){
                if(reg.getActivo() == 1){
                    cont ++;
                }
            }
        }
        
        return cont;
    }
    
     /**
     * ABRIR FORMULARIO DE BUSQUEDA DE COMPROBANTE
     * @author Richar Fernández
     * @version 1.0
     */
    public void openDlgComprobante(){
        lstRecibosBusqueda = new ArrayList();
        filtro = null;
        tipoBusqueda = null;
        fechaBusqueda = null;
        setMuestraBusquedafecha(false);
        
        RequestContext.getCurrentInstance().execute("PF('wvDialogBusquedaComprobante').show()");
        RequestContext.getCurrentInstance().update("frmBusComprobante");
    }
    
    /**
     * BUSCAR VIGILANTE A CESAR
     * @author Richar Fernández
     * @version 1.0
     */    
    public void buscarVigilante(){
       
        if(tipoDocBusqueda != null && numDocBusqueda !=null ){
            if(!numDocBusqueda.trim().isEmpty()){
                if(tipoDocBusqueda.equals("5") && numDocBusqueda.length() < 4 ){
                    JsfUtil.mensajeError("Por favor ingresar mínimo 4 caracteres para buscar el carnét de extranjería."); 
                    return;
                }
                
                limpiarVigilante();
                lstVigilanteBusqueda = ejbSsEmpVigFacade.buscarVigilanteCese(Long.parseLong(registro.getPersonaId().getRuc()),Integer.parseInt(tipoDocBusqueda), numDocBusqueda);
                if(lstVigilanteBusqueda != null && !lstVigilanteBusqueda.isEmpty()){
                    Map vigilante = lstVigilanteBusqueda.get(0);
                    if(Integer.parseInt(vigilante.get("TIPODOC").toString()) == 2){
                        tipoDoc = "DNI";
                        numDoc  = StringUtils.leftPad(""+ vigilante.get("NUMDOC").toString(), 8, "0");                    
                        nombreYApellidos = (vigilante.get("APAT") != null ?vigilante.get("APAT").toString():"") + " " + (vigilante.get("AMAT") != null?vigilante.get("AMAT").toString():"") + " " + (vigilante.get("NOMBRES") != null?vigilante.get("NOMBRES").toString():"");
                    }else{
                        tipoDoc = "CARNÉ DE EXTRANJERÍA";
                        numDoc = numDocBusqueda;
                        nombreYApellidos = (vigilante.get("APAT_EXT") != null ?vigilante.get("APAT_EXT").toString():"") + " " + (vigilante.get("AMAT_EXT") != null?vigilante.get("AMAT_EXT").toString():"") + " " + (vigilante.get("NOMBRES_EXT") != null?vigilante.get("NOMBRES_EXT").toString():"");
                    }                

                    numDocBusqueda = null;
                    tipoDocBusqueda = null;
                    setRenderDatosVigilante(true);
                    if(lstVigilanteBusqueda.size() == 1){
                        modalidad = vigilante.get("TIP_MOD").toString();
                        cambiaModalidad();
                    }
                }else{
                    setRenderDatosVigilante(true);
                    JsfUtil.mensajeAdvertencia("No se encontró al vigilante según los datos ingresados");
                }
            }else{
               JsfUtil.mensajeError("Por favor ingresar el número de documento para realizar la búsqueda");
            }
        }else{
            JsfUtil.mensajeError("Por favor ingresar el tipo y número de documento para realizar la búsqueda");
        }
    }
    
    /**
     * EVENTO PARA OBTENER DATOS DE CARNÉ AL CAMBIAR EL TIPO DE MODALIDAD DE CARNÉ
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiaModalidad(){
        if(modalidad != null){
            for(Map vigilante : lstVigilanteBusqueda){
                if(vigilante.get("TIP_MOD").toString().equals(modalidad)){
                    carnet = vigilante.get("NRO_CRN_VIG").toString();
                    fecEmision = (Date) vigilante.get("FEC_EMI");
                    fecvencimiento = (Date) vigilante.get("FEC_VENC");
                    
                    Calendar c_hoy = Calendar.getInstance();                    
                    if(carnet == null || fecEmision == null || fecvencimiento == null){
                        setRenderDatosVigilante(false);
                        JsfUtil.mensajeError("El vigilante no cuenta con todos los datos necesarios. Comunicarse con SUCAMEC");
                    }else{
                        if (JsfUtil.getFechaSinHora(c_hoy.getTime()).compareTo(JsfUtil.getFechaSinHora(fecvencimiento)) > 0) {
                            estadoCese = "VENCIDA";
                        }else{
                            estadoCese = "VIGENTE";
                        }
                        setRenderDatosVigilante(true);
                    }
                    break;
                }
            }
        }else{
            JsfUtil.mensajeError("Por favor seleccione la modalidad del vigilante.");
        }
    }
    
    /**
     * VALIDAR Y ADICIONAR VIGILANTE A TABLA DE VIGILANTES A CESAR
     * @author Richar Fernández
     * @version 1.0
     */
    public void agregarVigilante(){
        int cont = 0;
        if(limiteReg != lstVigilante.size()){
            if(modalidad != null){
                for(Map actual : lstVigilanteBusqueda){
                    if(actual.get("NRO_CRN_VIG").toString().equals(carnet)){
                        for(Map vigilante : lstVigilante){
                            if(vigilante.get("NRO_CRN_VIG").toString().equals(carnet) && vigilante.get("TIP_MOD").toString().equals(modalidad)){
                                cont ++;
                            }
                        }
                        if(cont == 0){
                            Map temp = new HashMap();
                            temp.put("TIPODOC",actual.get("TIPODOC"));
                            temp.put("NUMDOC",actual.get("NUMDOC"));
                            temp.put("APAT",actual.get("APAT"));
                            temp.put("AMAT",actual.get("AMAT"));
                            temp.put("NOMBRES",actual.get("NOMBRES"));
                            temp.put("NRO_CRN_VIG",actual.get("NRO_CRN_VIG"));
                            temp.put("TIP_MOD",actual.get("TIP_MOD"));
                            temp.put("DES_MOD",actual.get("DES_MOD"));
                            temp.put("FEC_EMI",actual.get("FEC_EMI"));
                            temp.put("FEC_VENC",actual.get("FEC_VENC"));                            
                            temp.put("estadoCese",estadoCese);
                            temp.put("tipoDoc_desc",tipoDoc);
                            temp.put("numDoc_desc",numDoc);
                            temp.put("nombreYApellidos",nombreYApellidos);                            
                            temp.put("fecEmision",fecEmision);
                            lstVigilante.add(temp);
                            limpiarVigilante();
                        }else{
                            JsfUtil.mensajeError("Vigilante ya ha sido ingresado.");
                        }
                        break;
                    }
                }
            }else{
                JsfUtil.mensajeError("Por favor seleccione la modalidad del vigilante.");
            }
        }else{
            JsfUtil.mensajeError("Ya no puede adicionar más vigilantes según el importe del comprobante ingresado.");
        }
    }
    
    /**
     * LIMPIAR REGISTRO
     * @author Richar Fernández
     * @version 1.0 
     */
    public void limpiarVigilante(){
        tipoDoc = null;
        numDoc = null;
        nombreYApellidos = null;
        modalidad = null;
        carnet = null;
        fecEmision = null;
        fecvencimiento = null;
        estadoCese = null;
        
        lstVigilanteBusqueda = new ArrayList();    
        setRenderDatosVigilante(false);
    }
    
    /**
     * BORRAR REGISTRO SELECCIONADO DE TABLA DE PERSONAS A CESAR
     * @param reg Registro Map de tabla de Vigilantes
     * @author Richar Fernández
     * @version 1.0
     */
    public void borrarFilaCese(Map reg){
        for(Map vigilante : lstVigilante){
            if(vigilante.get("NRO_CRN_VIG").toString().equals(reg.get("NRO_CRN_VIG").toString()) && vigilante.get("TIP_MOD").toString().equals(reg.get("TIP_MOD").toString())){
                lstVigilante.remove(vigilante);
                break;
            }
        }
    }
    
    /**
     * VALIDACION DE DATOS PREVIO A GUARDAR PROCESO
     * @author Richar Fernández
     * @version 1.0
     */
    public void validacionCese(){
        boolean validacion = true;
        msjeCese = "";
        
        if(lstVigilante.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar al menos un vigilante a cesar.");
        }

        if(validacion){
            if((limiteReg - lstVigilante.size()) > 0){
                validacion = false;
                JsfUtil.mensajeError("Aún existe "+(limiteReg - lstVigilante.size())+" cupo(s) para realizar el cese. Por favor completar el(los) cupo(s).");
            }else{
                msjeCese = msjeCese + "¿Está seguro que desea realizar el cese?";
                RequestContext.getCurrentInstance().execute("PF('confirmCese').show()");
                RequestContext.getCurrentInstance().update("msjeForm");
            }
        }
    }
    
    ////////////////// LISTADO DEVOLUCION CARNE //////////////////
    
    /**
     * ABRIR FORMULARIO DE LISTADO DE INSCRITOS
     * @author Richar Fernández
     * @version 1.0
     * @param sol Registro de Solicitud de Cese
     */
    public void openDlgListadoDevolucionCarne(SspSolicitudCese sol){
        lstInscritos = new ArrayList();
        
        for(SspSolicitudCeseDet det : sol.getSspSolicitudCeseDetList()){
            Map vigilante = new HashMap();
            vigilante.put("nroExpe", det.getNroExpCese());
            vigilante.put("nroCarne", det.getNroCarne());
            
            List<Map> temp = null;
            temp = ejbSsEmpVigFacade.buscarVigilanteDetalleEntregado(Long.parseLong(sol.getPersonaId().getRuc()), det.getNroCarne());
            
            if(!temp.isEmpty()){
                if(Integer.parseInt(temp.get(0).get("TIPODOC").toString()) == 2){
                    vigilante.put("numDoc", StringUtils.leftPad(""+ temp.get(0).get("NUMDOC").toString(), 8, "0"));
                    vigilante.put("nombreVigilante", temp.get(0).get("APAT") + " " + temp.get(0).get("AMAT") + " " + temp.get(0).get("NOMBRES"));
                }else{
                    vigilante.put("numDoc", temp.get(0).get("CRNT_EXT").toString());
                    vigilante.put("nombreVigilante", (temp.get(0).get("APAT_EXT") == null?"":temp.get(0).get("APAT_EXT")) + " " + (temp.get(0).get("AMAT_EXT")==null?"":temp.get(0).get("AMAT_EXT")) + " " + (temp.get(0).get("NOMBRES_EXT") == null?"":temp.get(0).get("NOMBRES_EXT")));
                }

                vigilante.put("modalidad", temp.get(0).get("DES_MOD"));
                vigilante.put("fecCese", sol.getFechaSolicitud());
                vigilante.put("fecDevolucion", det.getFechaDevolucion());
                vigilante.put("expeDevolucion", det.getNroExpDevo());
                vigilante.put("denunciaPolicial", det.getDenunciaPol());
                vigilante.put("estado", (det.getEstadoDevoId() == 1)?"Pendiente":"Entregado" );
                lstInscritos.add(vigilante);
            }
        }
        
        RequestContext.getCurrentInstance().execute("PF('wvDialogListadoInscritos').show()");
        RequestContext.getCurrentInstance().update("frmInscritos");
    }
    

    ////////////////// REPORTE //////////////////
    
    /**
     * PARAMETROS PARA GENERAR PDF
     * @author Richar Fernández
     * @version 1.0
     * @param ppdf Registro de solicitud de cese para crear el reporte
     * @return Listado de parámetros para reporte
     */
    private HashMap parametrosPdf(SspSolicitudCese ppdf) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();        
        HashMap ph = new HashMap();
        
        ph.put("p_dir_emp", ReportUtil.mostrarDireccionPersonaComercio(ppdf.getPersonaId()));
        ph.put("P_FECHASTRING", ReportUtil.mostrarFechaString(ppdf.getFechaSolicitud()));
        ph.put("P_TIPONRODOC", ReportUtil.mostrarTipoYNroDocumemtoString(ppdf.getPersonaId()));
        ph.put("P_EMPRESASTRING", ReportUtil.mostrarClienteString(ppdf.getPersonaId()));
        ph.put("P_logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        ph.put("P_TABLA_VIGILANTE", obtenerListadoVigilantes(ppdf) );
        ph.put("P_CODTRIBUTO", String.valueOf(ppdf.getReciboId().getCodTributo()) );
        ph.put("P_MONTO", String.valueOf(new DecimalFormat("###,###,###,##0.00").getInstance(Locale.ENGLISH).format( ppdf.getReciboId().getImporte()  )));
        ph.put("P_NROSECUENCIA", String.valueOf(ppdf.getReciboId().getNroSecuencia()));
        ph.put("P_FECHAPAGO", ReportUtil.formatoFechaDdMmYyyy(ppdf.getReciboId().getFechaMovimiento()));
        ph.put("P_CODOFICINA", String.valueOf(ppdf.getReciboId().getCodOficina()) );
        return ph;
    }
    
    /**
     * OBTENER LISTADO DE VIGILANTES PARA REPORTE
     * @param sol Registro de Solicitud de Cese
     * @return Listado Map con campos a mostrar en el reporte
     * @author Richar Fernández
     * @version 1.0
     */
    public List<Map> obtenerListadoVigilantes(SspSolicitudCese sol){
        List<Map> listado = new ArrayList();
        
        for(SspSolicitudCeseDet det : sol.getSspSolicitudCeseDetList()){
            Map vigilante = new HashMap();
            vigilante.put("nroExpe", det.getNroExpCese());
            vigilante.put("fecExpe", det.getSolicitudId().getFechaSolicitud());
            
            List<Map> temp = null;
            temp = ejbSsEmpVigFacade.buscarVigilanteDetalleEntregado(Long.parseLong(sol.getPersonaId().getRuc()), det.getNroCarne());
            
            if(!temp.isEmpty()){
                if(Integer.parseInt(temp.get(0).get("TIPODOC").toString()) == 2){
                    vigilante.put("nroDocumento", StringUtils.leftPad(""+ temp.get(0).get("NUMDOC").toString(), 8, "0"));
                    vigilante.put("nombreVigilante", temp.get(0).get("APAT") + " " + temp.get(0).get("AMAT") + " " + temp.get(0).get("NOMBRES"));
                }else{                
                    vigilante.put("nroDocumento", temp.get(0).get("CRNT_EXT").toString());
                    vigilante.put("nombreVigilante", (temp.get(0).get("APAT_EXT") == null?"":temp.get(0).get("APAT_EXT")) + " " + (temp.get(0).get("AMAT_EXT")==null?"":temp.get(0).get("AMAT_EXT")) + " " + (temp.get(0).get("NOMBRES_EXT") == null?"":temp.get(0).get("NOMBRES_EXT")));
                }

                vigilante.put("nroCarne", det.getNroCarne());
                vigilante.put("modalidad", temp.get(0).get("DES_MOD"));
                vigilante.put("fecCese", sol.getFechaSolicitud());
                listado.add(vigilante);
            }
        }
        
        return listado;
    }
    
    /**
     * FUNCION PARA GENERACION DE REPORTE DE RESOLUCION
     * @author Richar Fernández
     * @version 1.0
     * @param pReg Registro de Solicitud de Cese para crear el reporte
     * @return Reporte 
     */
    public StreamedContent reporte(SspSolicitudCese pReg) {
        try {
            List<SspSolicitudCese> lp = new ArrayList();
            lp.add(pReg);
            
            return JsfUtil.generarReportePdf("/aplicacion/gssp/sspSolicitudCese/reportes/solicitudCese.jasper", lp, parametrosPdf(pReg), "solicitudCese.pdf", null, null);
        } catch (Exception ex) {
            return JsfUtil.errorDescarga("Error: SspSolicitudCeseController.reporte :", ex);
        }
    }
    
    /////////////////////////////////////////////
}
