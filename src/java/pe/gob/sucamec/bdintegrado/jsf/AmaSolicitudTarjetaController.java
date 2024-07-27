package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.AmaSolicitudTarjeta;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbProcesoTupa;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

// Nombre de la instancia en la aplicacion //
@Named("amaSolicitudTarjetaController")
@SessionScoped

/**
 * Clase con AmaSolicitudTarjetaController instanciada como
 * amaSolicitudTarjetaController. Contiene funciones utiles para la entidad
 * AmaSolicitudTarjeta. Esta vinculada a las páginas
 * AmaSolicitudTarjeta/create.xhtml, AmaSolicitudTarjeta/update.xhtml,
 * AmaSolicitudTarjeta/list.xhtml, AmaSolicitudTarjeta/view.xhtml Nota: Las
 * tablas deben tener la estructura de Sucamec para que funcione adecuadamente,
 * revisar si tiene el campo activo y modificar las búsquedas.
 */
public class AmaSolicitudTarjetaController implements Serializable {

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
    AmaSolicitudTarjeta registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<AmaSolicitudTarjeta> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<Map> resultadosSeleccionados;

    private List<Map> resultadosSolicitudes;
    private String tipoBusquedaSolicitud = null;
    private TipoGamac tipoEstadoSolicitud = null;
    private List<String> lstClavesActas;
    private String claveActa;
    private Map datosActaInt;
    private TipoGamac tipoTramite;
    private SbPersonaGt representanteLegal;
    private SbRecibos recibo;
    private boolean disableReciboBn = false;
    private SbProcesoTupa parametrizacionTupa;
    private Map registroSelected;
    private boolean disabledBtnTransmitir;
    private String tipoFiltro;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaSolicitudTarjetaFacade ejbAmaSolicitudTarjetaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade ejbAmaGuiaTransitoFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.RaessFacade ejbRaessFacade;    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbProcesoTupaFacade ejbSbProcesoTupaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaInventarioArmaFacade ejbAmaInventarioArmaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade ejbSbDerivacionCydocFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt ejbTipoBaseFacade;
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
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public AmaSolicitudTarjeta getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(AmaSolicitudTarjeta registro) {
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
    public ListDataModel<AmaSolicitudTarjeta> getResultados() {
        return resultados;
    }

    public String getTipoBusquedaSolicitud() {
        return tipoBusquedaSolicitud;
    }

    public void setTipoBusquedaSolicitud(String tipoBusquedaSolicitud) {
        this.tipoBusquedaSolicitud = tipoBusquedaSolicitud;
    }

    public TipoGamac getTipoEstadoSolicitud() {
        return tipoEstadoSolicitud;
    }

    public void setTipoEstadoSolicitud(TipoGamac tipoEstadoSolicitud) {
        this.tipoEstadoSolicitud = tipoEstadoSolicitud;
    }

    public List<String> getLstClavesActas() {
        return lstClavesActas;
    }

    public void setLstClavesActas(List<String> lstClavesActas) {
        this.lstClavesActas = lstClavesActas;
    }

    public String getClaveActa() {
        return claveActa;
    }

    public void setClaveActa(String claveActa) {
        this.claveActa = claveActa;
    }

    public Map getDatosActaInt() {
        return datosActaInt;
    }

    public void setDatosActaInt(Map datosActaInt) {
        this.datosActaInt = datosActaInt;
    }

    public TipoGamac getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoGamac tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public SbPersonaGt getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(SbPersonaGt representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public SbRecibos getRecibo() {
        return recibo;
    }

    public void setRecibo(SbRecibos recibo) {
        this.recibo = recibo;
    }

    public boolean isDisableReciboBn() {
        return disableReciboBn;
    }

    public void setDisableReciboBn(boolean disableReciboBn) {
        this.disableReciboBn = disableReciboBn;
    }

    public SbProcesoTupa getParametrizacionTupa() {
        return parametrizacionTupa;
    }

    public void setParametrizacionTupa(SbProcesoTupa parametrizacionTupa) {
        this.parametrizacionTupa = parametrizacionTupa;
    }

    public List<Map> getResultadosSolicitudes() {
        return resultadosSolicitudes;
    }

    public void setResultadosSolicitudes(List<Map> resultadosSolicitudes) {
        this.resultadosSolicitudes = resultadosSolicitudes;
    }

    public Map getRegistroSelected() {
        return registroSelected;
    }

    public void setRegistroSelected(Map registroSelected) {
        this.registroSelected = registroSelected;
    }

    public boolean isDisabledBtnTransmitir() {
        return disabledBtnTransmitir;
    }

    public void setDisabledBtnTransmitir(boolean disabledBtnTransmitir) {
        this.disabledBtnTransmitir = disabledBtnTransmitir;
    }

    public List<Map> getResultadosSeleccionados() {
        return resultadosSeleccionados;
    }

    public void setResultadosSeleccionados(List<Map> resultadosSeleccionados) {
        this.resultadosSeleccionados = resultadosSeleccionados;
    }

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
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
    public List<AmaSolicitudTarjeta> getSelectItems() {
        return ejbAmaSolicitudTarjetaFacade.findAll();
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
        resultados = new ListDataModel(ejbAmaSolicitudTarjetaFacade.selectLike(filtro));
    }
    
    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public AmaSolicitudTarjeta getAmaSolicitudTarjeta(Long id) {
        return ejbAmaSolicitudTarjetaFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public AmaSolicitudTarjetaController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "amaSolicitudTarjetaConverter")
    public static class AmaSolicitudTarjetaControllerConverterN extends AmaSolicitudTarjetaControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = AmaSolicitudTarjeta.class)
    public static class AmaSolicitudTarjetaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AmaSolicitudTarjetaController c = (AmaSolicitudTarjetaController) JsfUtil.obtenerBean("amaSolicitudTarjetaController", AmaSolicitudTarjetaController.class);
            return c.getAmaSolicitudTarjeta(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AmaSolicitudTarjeta) {
                AmaSolicitudTarjeta o = (AmaSolicitudTarjeta) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + AmaSolicitudTarjeta.class.getName());
            }
        }
    }
    
    
    public String prepareListadoAmnistia(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        reiniciarCamposBusqueda();
        resultadosSolicitudes = null;
        
        if(!esPerfilInpe()){
            int contAutorizado = ejbRaessFacade.contarResolucionesVigentesEmpresa(p_user.getNumDoc());
            if(contAutorizado == 0 ){
                JsfUtil.mensajeError("Usted no cuenta con AUTORIZACIÓN VIGENTE PARA UTILIZAR ARMAS EN SUS ACTIVIDADES. Por Favor acérquese a las Oficinas de SUCAMEC para regularizar");
                return null;
            }
        }
        
        parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", "TP_GAMAC_AMN");
        Map item = crearMapBusqueda();
        resultadosSolicitudes =  ejbAmaSolicitudTarjetaFacade.buscarArmasNoRegularizadasSinTarjetaProp(item);
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/gamac/amaSolicitudTarjeta/List";
    }
    
    public boolean renderBtnCrearSolicitud(Map item){
        if(item == null){
            return false;
        }
        if(item.get("SOLICITUD_ID") != null){
            return false;
        }
        if(item.get("INVENTARIO_ID") != null){
            if(item.get("TIPO_INTERNAMIENTO_CODPROG").toString().equals("TP_INTER_DEF") || item.get("TIPO_INTERNAMIENTO_CODPROG").toString().equals("TP_INTER_IDD")){
                return false;
            }
            return true;
        }
        return false;        
    }
    
    public boolean renderBtnEditarSolicitud(Map item){
        if(item == null){
            return false;
        }
        if(item.get("SOLICITUD_ID") != null){
            if(item.get("ESTADO_CODPROG").toString().equals("TP_ESTAU_CRE") || item.get("ESTADO_CODPROG").toString().equals("TP_ESTAU_OBS")){
                return true;
            }
        }
        return false;
    }
    
    public boolean renderBtnVerSolicitud(Map item){
        if(item == null){
            return false;
        }
        if(item.get("SOLICITUD_ID") != null){
            return true;
        }
        return false;
    }
    
    public boolean renderBtnBorrarSolicitud(Map item){
        if(item == null){
            return false;
        }
        if(item.get("SOLICITUD_ID") != null){
            if(item.get("ESTADO_CODPROG").toString().equals("TP_ESTAU_CRE")){
                return true;
            }
        }
        return false;
    }
    
    public boolean renderBtnConstancia(Map item){
        if(item == null){
            return false;
        }
        if(item.get("SOLICITUD_ID") != null){
            if(!item.get("ESTADO_CODPROG").toString().equals("TP_ESTAU_CRE")){
                return true;
            }
        }
        return false;
    }
    
    public void mostrarCrearSolicitud(Map item){
        reiniciarValores();
        
        cargarDatos(item);
        if(registroSelected == null){
            JsfUtil.mensajeError("No se encontró datos del arma para poder procesar la solicitud");
            return;
        }
        if(item.get("INVENTARIO_ID") == null || item.get("GT_ID") == null){
            JsfUtil.mensajeError("No puede crear una solicitud porque no se encontraron datos del internamiento");
            return;
        }
        
        registro = new AmaSolicitudTarjeta();
        registro.setActivo((short) 1);
        registro.setId(null);
        registro.setActaInternamientoId(null);
        registro.setComprobanteId(null);
        registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registro.setAudNumIp(JsfUtil.getIpAddress());
        registro.setEstadoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ESTAU_CRE"));
        registro.setPersonaId(ejbPersonaFacade.obtenerEmpresaId(JsfUtil.getLoggedUser().getPersonaId()));
        registro.setTipoOpeId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_OPET_AMN"));
        registro.setTipoTramiteId(parametrizacionTupa.getProcesoIntegradoId());
        registro.setUsuarioCreacionId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
        if(item.get("GT_ID") != null){
            registro.setActaInternamientoId(ejbAmaGuiaTransitoFacade.buscarGuiaTransitoById(Long.parseLong(item.get("GT_ID").toString())));
        }
        estado = EstadoCrud.CREAR;
    }
    
    public void mostrarEditarSolicitud(Map item){
        reiniciarValores();
        cargarDatos(item);
        estado = EstadoCrud.EDITAR;
    }
    
    public void mostrarVerSolicitud(Map item){
        reiniciarValores();
        cargarDatos(item);
        estado = EstadoCrud.VER;
    }
    
    public void borrarSolicitud(Map item){
        try {
            registro = ejbAmaSolicitudTarjetaFacade.buscarSolicitudTarjetaById(Long.parseLong(item.get("SOLICITUD_ID").toString()));
            registro.setActivo(JsfUtil.FALSE);        
            ejbAmaSolicitudTarjetaFacade.edit(registro);

            if(registro.getComprobanteId() != null){
                for(SbReciboRegistro recReg : registro.getComprobanteId().getSbReciboRegistroList()){
                    if(recReg.getActivo() == 0){
                        continue;
                    }
                    recReg.setActivo(JsfUtil.FALSE);
                    break;
                }            
                ejbSbRecibosFacade.edit(registro.getComprobanteId());
            }
            resultadosSolicitudes.remove(item);
            resultadosSeleccionados.remove(item);

            JsfUtil.mensaje("Se ha borrado correctamente la solicitud");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("amaSolicitudTarjeta_msjeBorradoRegistro"));
        }
    }
    
    public boolean cargarDatos(Map item){
        registroSelected = item;
        if(item.get("INVENTARIO_ID") != null && item.get("GT_ID") != null){
            datosActaInt = ejbAmaInventarioArmaFacade.buscarDatosInternamientoSolicitud( Long.parseLong(item.get("INVENTARIO_ID").toString()), Long.parseLong(item.get("GT_ID").toString()) );
        }
        if(item.get("SOLICITUD_ID") != null){
            registro = ejbAmaSolicitudTarjetaFacade.buscarSolicitudTarjetaById(Long.parseLong(item.get("SOLICITUD_ID").toString()));
            
            if(registro != null){
                if(registro.getComprobanteId() != null){
                    recibo = registro.getComprobanteId();
                    setDisableReciboBn(true);
                }
                if(registro.getRepresentanteLegalId() != null){
                    representanteLegal = registro.getRepresentanteLegalId();
                }
            }else{
                JsfUtil.mensajeError("No se encontró datos de la solicitud");
                return false;
            }
        }
        parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", "TP_GAMAC_AMN");
        return true;
    }
    
    public String obtenerSituacionSolicitud(Map item){
        if(item == null){
            return "-";
        }
        if(item.get("INVENTARIO_ID") != null){
            if(item.get("DES_SIT") == null){
                return "-";
            }
            return item.get("DES_SIT").toString();
        }
        return JsfUtil.bundleBDIntegrado("amaSolicitudTarjeta_porVerificar");
    }
    
    public String obtenerEstadoSolicitud(Map item){
        if(item == null){
            return "-";
        }
        if(item.get("SOLICITUD_ID") != null){
            return item.get("ESTADO_NOMBRE").toString();
        }
        if(item.get("INVENTARIO_ID") != null){
            if(item.get("TIPO_INTERNAMIENTO_CODPROG").toString().equals("TP_INTER_DEF") || item.get("TIPO_INTERNAMIENTO_CODPROG").toString().equals("TP_INTER_IDD")){
                return item.get("TIPO_INTERNAMIENTO").toString();
            }
            return JsfUtil.bundleBDIntegrado("amaSolicitudTarjeta_paraCrearSolicitud");
        }        
        return JsfUtil.bundleBDIntegrado("amaSolicitudTarjeta_paraInternar");
    }
    
    public String obtenerCalibreSolicitud(Map item){
        if(item == null){
            return "-";
        }
        if(item.get("CALIBRE") != null){
            return item.get("CALIBRE").toString();
        }
        return item.get("CALIBRE_DISCA").toString();
    }
    
    public void seleccionarFilaTabla(SelectEvent event){
        eventoSeleccion();
    }
    
    public void deseleccionarFilaTabla(UnselectEvent event){
        eventoSeleccion();
    }
    
    public void toggleEventFila(ToggleSelectEvent event){
        eventoSeleccion();
    }
    
    public void eventoSeleccion(){
        setDisabledBtnTransmitir(false);
        if(resultadosSeleccionados == null){
            setDisabledBtnTransmitir(true);
            return;
        }
        for (Map reg : resultadosSeleccionados) {
            if(reg.get("SOLICITUD_ID") == null){
                JsfUtil.mensajeAdvertencia("Registro seleccionado no cuenta con solicitud generada");
                setDisabledBtnTransmitir(true);
                return;
            }
            if(!reg.get("ESTADO_CODPROG").equals("TP_ESTAU_CRE") && !reg.get("ESTADO_CODPROG").equals("TP_ESTAU_OBS")){
                JsfUtil.mensajeAdvertencia("El cambio es sólo para los estados CREADO u OBSERVADO");
                setDisabledBtnTransmitir(true);
                return;
            }
        }
    }
    
    public void cambiaTipoBusquedaSolicitud(){
        filtro = null;
        tipoEstadoSolicitud = null;
        
        if(tipoBusquedaSolicitud == null){
            return;
        }
        if(tipoBusquedaSolicitud.equals("estado")){
            filtro = null;
        }else{
            tipoEstadoSolicitud = null;
        }
    }
    
    public void buscarBandejaSolicitud(){
        if(!validacionBuscarBandeja()){            
            return;
        }
        Map item = crearMapBusqueda();        
        resultadosSolicitudes =  ejbAmaSolicitudTarjetaFacade.buscarArmasNoRegularizadasSinTarjetaProp(item);        
    }
    
    public boolean validacionBuscarBandeja(){
        if(tipoBusquedaSolicitud != null){
            if((tipoBusquedaSolicitud.equals("licencia") || tipoBusquedaSolicitud.equals("serie")) && (filtro == null || filtro.isEmpty()) ){
                JsfUtil.mensajeError("Por favor ingrese el campo a buscar");
                JsfUtil.invalidar("listForm:filtroBusqueda");
                return false;
            }
            if(tipoBusquedaSolicitud.equals("estado") && tipoEstadoSolicitud == null ){
                JsfUtil.mensajeError("Por favor seleccione el estado a buscar");
                JsfUtil.invalidar("listForm:tipoEstado");
                return false;
            }
        }
        return true;
    }
    
    public Map crearMapBusqueda(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        Map item = new HashMap();
        item.put("ruc", p_user.getNumDoc());
        item.put("tipoBusqueda", tipoBusquedaSolicitud);
        item.put("filtro", filtro);
        item.put("tipoFiltro", tipoFiltro);
        item.put("tipados", obtenerArrayConcatenadosTipadosGT());
        item.put("tipoEstado", (tipoEstadoSolicitud != null)?tipoEstadoSolicitud.getId():null);
        
        return item;
    }
    
    public String obtenerArrayConcatenadosTipadosGT(){
        String tipados = "";
        List<TipoGamac> listadoTipado = ejbTipoGamacFacade.listarTipadoRecursivoById(195L); // Tipado GT
        if(listadoTipado != null && !listadoTipado.isEmpty()){
            for(TipoGamac tipado : listadoTipado){
                if(tipados.isEmpty()){
                    tipados = "" + tipado.getId();
                }else{
                    tipados += ", " + tipado.getId();
                }
            }
        }
        return tipados;
    }
    
    public List<TipoGamac> obtenerListadoEstadosArma(){
        return ejbTipoGamacFacade.listarTipoGamacByManyCodProgs("'TP_ESTAU_CRE','TP_ESTAU_OBS','TP_ESTAU_TRA','TP_ESTAU_TEMI'");
    }
    
    public String crearSolicitud(){
        try {
            if(!validacionGuardar()){
                return null;
            }
            
            // Solicitud de emisión de tarjeta
            registro.setRepresentanteLegalId(representanteLegal);
            registro.setComprobanteId(recibo);
            registro.setFechaCreacion(new Date());
            ejbAmaSolicitudTarjetaFacade.create(registro);
            //
            // Recibo utilizado
            SbReciboRegistro rec = new SbReciboRegistro();
            rec.setId(null);
            rec.setRegistroId(null);
            rec.setNroExpediente("STP-"+registro.getId());
            rec.setReciboId(registro.getComprobanteId());
            rec.setActivo((short) 1);
            rec.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            rec.setAudNumIp(JsfUtil.getIpAddress());
            if(registro.getComprobanteId().getSbReciboRegistroList() == null){
                registro.getComprobanteId().setSbReciboRegistroList(new ArrayList());
            }
            registro.getComprobanteId().getSbReciboRegistroList().add(rec);
            ejbSbRecibosFacade.edit(registro.getComprobanteId());
            //
            
            JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("amaSolicitudTarjeta_msjeCreacionRegistro") + registro.getId());
            return direccionarBandeja();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al momento de guardar el registro");
        }
        return null;
    }
    
    public String editarSolicitud(){
        try {
            if(!validacionGuardar()){
                return null;
            }
            // Recibo utilizado
            if(!Objects.equals( recibo.getId(), registro.getComprobanteId().getId() )){
                if(registro.getComprobanteId() != null){
                    for(SbReciboRegistro recReg : registro.getComprobanteId().getSbReciboRegistroList()){
                        if(recReg.getActivo() == 1){
                            recReg.setActivo(JsfUtil.FALSE);
                            ejbSbRecibosFacade.edit(registro.getComprobanteId());
                            break;
                        }
                    } 
                }
                SbReciboRegistro rec = new SbReciboRegistro();
                rec.setId(null);
                rec.setRegistroId(null);
                rec.setNroExpediente("S/N");
                rec.setReciboId(recibo);
                rec.setActivo(JsfUtil.TRUE);
                rec.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                rec.setAudNumIp(JsfUtil.getIpAddress());
                if(recibo.getSbReciboRegistroList() == null){
                    recibo.setSbReciboRegistroList(new ArrayList());
                }
                recibo.getSbReciboRegistroList().add(rec);
                ejbSbRecibosFacade.edit(recibo);
            }
            //
            
            // Solicitud de emisión de tarjeta
            registro.setRepresentanteLegalId(representanteLegal);
            registro.setComprobanteId(recibo);
            ejbAmaSolicitudTarjetaFacade.edit(registro);
            //
            JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("amaSolicitudTarjeta_msjeEdicionRegistro"));
            return direccionarBandeja();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String direccionarBandeja(){
        estado = EstadoCrud.BUSCAR;
        resultadosSolicitudes = null;
        reiniciarCamposBusqueda();
        return "/aplicacion/gamac/amaSolicitudTarjeta/List";
    }
    
    public boolean validacionGuardar(){
        if(representanteLegal == null){
            JsfUtil.invalidar(obtenerForm()+":representante");
            JsfUtil.mensajeError("Por favor seleccione un representante legal");
            return false;
        }
        if(recibo == null){
            JsfUtil.invalidar(obtenerForm()+":reciboBn");
            JsfUtil.mensajeError("Por favor ingrese un recibo");
            return false;
        }else{
            if(!recibo.getNroDocumento().trim().equals(registro.getPersonaId().getRuc()) &&
               !recibo.getNroDocumento().trim().equals(registro.getPersonaId().getNumDoc())){
                JsfUtil.invalidar(obtenerForm()+":reciboBn");
                JsfUtil.mensajeError("El recibo " + recibo.getNroSecuencia() + " le pertenece a otro administrado.");
                return false;
            }
        }
        return true;
    }
    
    public String obtenerForm(){
        switch(estado){
            case BUSCAR:
                return "listForm";                
            case CREAR:
                return "createSolicitudForm";
            case EDITAR:
                return "editarSolicitudForm";
            case VER:
                return "verSolicitudForm";
        }
        return "listForm";
    }    
    
    public List<SbRelacionPersonaGt> obtenerListadoRepresentantes(){
        List<SbRelacionPersonaGt> listado = new ArrayList();
        if(registro.getPersonaId() != null){
            for(SbRelacionPersonaGt per : registro.getPersonaId().getSbRelacionPersonaList()){
                if(per.getActivo() == 0){
                    continue;
                }
                if(per.getPersonaDestId() == null){
                    continue;
                }
                if(per.getPersonaDestId().getId() == 0L){
                    continue;
                }
                listado.add(per);
            }
        }
        return listado;
    }
    
    public List<SbRecibos> autoCompleteRecibos(String query) {
        try {
            borrarValoresRecibo();
            List<SbRecibos> filteredList = new ArrayList();
            HashMap mMap = new HashMap();
            mMap.put("tipo", null);
            mMap.put("importe", parametrizacionTupa.getImporte() );
            mMap.put("codigo", parametrizacionTupa.getCodTributo() );

            List<SbRecibos> lstUniv = ejbSbRecibosFacade.listarRecibosByImporteByCodAtributo(registro.getPersonaId(), mMap);
            for (SbRecibos x : lstUniv) {
                if (x.getNroSecuencia().toString().equals(query.trim())) {
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
    
    private void borrarValoresRecibo() {
        recibo = null;
    }

    public void borrarReciboSeleccionado() {
        borrarValoresRecibo();
        setDisableReciboBn(false);
    }
    
    public void lstnrSelectRecibo(SelectEvent event) {
        try {
            borrarValoresRecibo();
            if (event!=null) {
                recibo = (SbRecibos) event.getObject();
                
                if(!validaReciboSeleccionado(recibo)){
                    return;
                }
                
                setDisableReciboBn(true);
                JsfUtil.mensaje("Recibo " + recibo.getNroSecuencia() + " seleccionado correctamente.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeError(ex.getMessage());
        }
    }
    
    public boolean validaReciboSeleccionado(SbRecibos rec){
        if(rec == null){
            JsfUtil.mensajeError("Por favor seleccione el recibo");
            return false;
        }
        if(!rec.getNroDocumento().trim().equals(registro.getPersonaId().getRuc()) &&
           !rec.getNroDocumento().trim().equals(registro.getPersonaId().getNumDoc())){
            HashMap mMap = new HashMap();
            mMap.put("tipo", null);
            mMap.put("importe", parametrizacionTupa.getImporte() );
            mMap.put("codigo", parametrizacionTupa.getCodTributo() );
            mMap.put("nroSecuencia", rec.getId() );            

            SbRecibos reciboTemp = ejbSbRecibosFacade.listarRecibosBusquedaSELXNroSecuencia(registro.getPersonaId(), mMap);
            if(reciboTemp != null){
                if(!reciboTemp.getNroDocumento().trim().equals(registro.getPersonaId().getRuc()) &&
                   !reciboTemp.getNroDocumento().trim().equals(registro.getPersonaId().getNumDoc())){
                    recibo = null;
                    JsfUtil.mensajeError("Recibo " + reciboTemp.getNroSecuencia() + " le pertenece a otro administrado.");
                    return false;
                }else{
                    recibo = reciboTemp;
                }
            }else{
                recibo = null;
                JsfUtil.mensajeError("El recibo " + rec.getNroSecuencia() + " le pertenece a otro administrado.");
                return false;
            } 
        }
        return true;
    }
    
    public String obtenerDatosRecibo(){
        String desc = "";
        
        if(recibo != null){
            desc = "NRO. RECIBO: " + recibo.getNroSecuencia() + ", FECHA: " + JsfUtil.dateToString(recibo.getFechaMovimiento(), "dd/MM/yyyy") + ", MONTO: S/." + recibo.getImporte();
        }
        
        return desc;
    }
    
    public void reiniciarValores(){
        registro = null;
        filtro = null;
        tipoBusquedaSolicitud = null;
        tipoEstadoSolicitud = null;
        resultadosSolicitudes = null;        
        claveActa = null;
        lstClavesActas = null;
        tipoTramite = null;
        representanteLegal = null;
        recibo = null;
        parametrizacionTupa = null;
        setDisableReciboBn(false);
        setDisabledBtnTransmitir(true);
        resultadosSeleccionados = new ArrayList();
    }
    
    public void reiniciarCamposBusqueda(){
        filtro = null;
        recibo = null;
        datosActaInt = null;
        representanteLegal = null;
        tipoBusquedaSolicitud = null;
        tipoEstadoSolicitud = null;
        resultadosSeleccionados = null;
        tipoFiltro = "0";
    }
    
    public boolean esPerfilInpe(){
        return JsfUtil.buscarPerfilUsuario("AMA_INPE");
    }
    
    public String obtenerAlmacenActaInternamiento(){
        String almacen = "";
        if(datosActaInt != null){
            almacen = datosActaInt.get("DIRECCION").toString();
            if(datosActaInt.get("DISTRITO_ID") != null && Integer.parseInt(datosActaInt.get("DISTRITO_ID").toString()) > 0 ){
                almacen += ( " - " +datosActaInt.get("UBIGEO").toString());
            }
        }
        return almacen;
    }
    
    public boolean validaTransmitir(Map item){
        if(item == null){
            return false;
        }
        if(item.get("SOLICITUD_ID") == null){
            return false;
        }
        return true;
    }
    
    public void transmitirSolicitud(){
        if(resultadosSeleccionados == null || resultadosSeleccionados.isEmpty()){
           JsfUtil.mensajeAdvertencia("Por favor seleccionar al menos un registro para transmitir"); 
           return;
        }
        
        Integer usuaTramDoc = ejbSbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
        SbPersonaGt per = ejbPersonaFacade.obtenerEmpresaId(JsfUtil.getLoggedUser().getPersonaId());
        String estadoCurso;
        boolean flagComprobante;
        int cont = 0;

        for(Map item : resultadosSeleccionados){

            if(!validaTransmitir(item)){
                continue;
            }

            BigDecimal idTemp = (BigDecimal) item.get("SOLICITUD_ID");
            registro = ejbAmaSolicitudTarjetaFacade.find(idTemp.longValue());
            estadoCurso = registro.getEstadoId().getCodProg();
            flagComprobante = false;

            if(registro.getNroExpediente() == null){
                flagComprobante = true;
            }

            if(!generaExpediente(registro, usuaTramDoc, per, estadoCurso, item)){
                JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro ID: " + registro.getId());
                continue;
            }
            registro.setEstadoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ESTAU_TRA"));
            registro.setFechaTransmision(new Date());
            ejbAmaSolicitudTarjetaFacade.edit(registro);
            cont++;

            if(flagComprobante && registro.getComprobanteId() != null){
                for(SbReciboRegistro recReg : registro.getComprobanteId().getSbReciboRegistroList()){
                    if(recReg.getActivo() == 1){
                        recReg.setNroExpediente(registro.getNroExpediente());
                        break;
                    }
                }
                ejbSbRecibosFacade.edit(registro.getComprobanteId());
            }

            JsfUtil.mensaje("Se transmitió el registro ID: " + registro.getId() + ", con expediente: " + registro.getNroExpediente());
            registro = null;

        }
        reiniciarCamposBusqueda();
        resultadosSolicitudes = new ArrayList();
        if(cont > 0){
            JsfUtil.mensaje("Se procesó "+ cont + " registro(s) correctamente.");
        }
    }
    
    public boolean generaExpediente(AmaSolicitudTarjeta reg, Integer usuaTramDoc, SbPersonaGt per, String estadoCurso, Map item){
        boolean validacion = true;
        
        if(estadoCurso.equals("TP_ESTAU_CRE") ){
            if(registro.getNroExpediente() != null){
                return true;
            }
            String asunto = ((reg.getPersonaId().getRuc() != null)?(reg.getPersonaId().getRznSocial()+" - RUC: "+reg.getPersonaId().getRuc() ):(reg.getPersonaId().getApePat() + " " + reg.getPersonaId().getApeMat() + " " + reg.getPersonaId().getNombres() +" - "+ (reg.getPersonaId().getTipoDoc() != null ?" - "+reg.getPersonaId().getTipoDoc().getNombre():" - DNI" )+": "+reg.getPersonaId().getNumDoc() ));
            String titulo = "SOLICITUD WEB DE EMISIÓN DE TARJETA DE PROPIEDAD";
            
            if(item.get("INVENTARIO_ID") != null && item.get("GT_ID") != null){
                datosActaInt = ejbAmaInventarioArmaFacade.buscarDatosInternamientoSimplificadoSolicitud( Long.parseLong(item.get("INVENTARIO_ID").toString()), Long.parseLong(item.get("GT_ID").toString()) );
                if(datosActaInt != null){
                    titulo = "SERIE: " + datosActaInt.get("NRO_SERIE");
                    if(datosActaInt.get("TIPO_ARMA") != null){
                        titulo += ", TIPO ARMA: " + datosActaInt.get("TIPO_ARMA");
                    }
                    if(datosActaInt.get("MARCA") != null){
                        titulo += ", MARCA: " + datosActaInt.get("MARCA");
                    }
                    if(datosActaInt.get("MODELO") != null){
                        titulo += ", MODELO: " + datosActaInt.get("MODELO");
                    }
                    if(datosActaInt.get("CALIBRE") != null){
                        titulo += ", CALIBRE: " + datosActaInt.get("CALIBRE");
                    }
                }
            }
            
            String expediente = wsTramDocController.crearExpediente_gssp(reg.getComprobanteId(), per , asunto , parametrizacionTupa.getCydocIdProceso() , usuaTramDoc, usuaTramDoc, titulo);
            if(expediente != null){
                List<Integer> acciones = new ArrayList();
                acciones.add(17);   // En Evaluación
                registro.setNroExpediente(expediente);
                
                 try {
                    DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                    ///////// Para obtener usuario a derivar /////////
                    SbDerivacionCydoc  derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionCyDoc(p_user.getSistema(), ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GAMAC_AMN").getId(), 76L);
                    if(derivacion == null){
                        JsfUtil.mensajeError("No se pudo encontrar a un usuario parametrizado para derivación del expediente");
                        return true;
                    }
                    /////////
                    boolean estadoTraza = wsTramDocController.asignarExpediente(expediente, "USRWEB", derivacion.getUsuarioDestinoId().getLogin(), asunto, "", acciones,false,null);
                    if (!estadoTraza) {
                        JsfUtil.mensajeError("No se pudo generar la traza del expediente. ID exp. : " + expediente);
                    }
                    ////////
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }else{
                validacion = false;
            }
        }
        return validacion;
    }
    
    public StreamedContent imprimirConstancia(Map item){
        try {
            registro = ejbAmaSolicitudTarjetaFacade.buscarSolicitudTarjetaById(Long.parseLong(item.get("SOLICITUD_ID").toString()));
            List<AmaSolicitudTarjeta> lp = new ArrayList();
            lp.add(registro);

            String nombreArch = "CONSTANCIA_"+registro.getId();
            String ruta = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_solicitudTarjeta").getValor();
            if (JsfUtil.buscarPdfRepositorio(nombreArch, ruta)) {
                return JsfUtil.obtenerArchivoBDIntegrado(ruta, nombreArch + ".pdf", nombreArch + ".pdf", "application/pdf");
            }else{
                JsfUtil.subirPdfSel(nombreArch, parametrosPdf(registro), lp, "/aplicacion/gamac/amaSolicitudTarjeta/reportes/constanciaSolicitudTarjeta.jasper", ruta);
                return JsfUtil.obtenerArchivoBDIntegrado(ruta, nombreArch + ".pdf", nombreArch + ".pdf", "application/pdf");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
     public HashMap parametrosPdf(AmaSolicitudTarjeta ppdf){
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        
        ph.put("P_FECHASTRING", JsfUtil.dateToString(ppdf.getFechaTransmision(), "dd/MM/yyyy"));
        ph.put("P_HORA", JsfUtil.dateToString(ppdf.getFechaTransmision(), "hh:mm:ss a"));
        ph.put("P_EXPEDIENTE", ppdf.getNroExpediente() );
        ph.put("P_USUARIO", ppdf.getUsuarioCreacionId().getLogin() );        
        ph.put("P_LISTA_DATOS", listarDatosConstancia(ppdf) );
        ph.put("P_LISTA_DOCUMENTOS", listarDocumentosConstancia(ppdf, ppdf.getFechaTransmision()) );
        
        return ph;
    }
     
    public List<Map> listarDatosConstancia(AmaSolicitudTarjeta registroId){
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
        if(registroId.getPersonaId().getRznSocial() != null){
            nombres = registroId.getPersonaId().getRznSocial() + ", RUC: " + registroId.getPersonaId().getRuc();
        }else{
            nombres = registroId.getPersonaId().getNombreCompleto() + ", DNI: " + registroId.getPersonaId().getNumDoc();
        }
        nmap.put("valor",  nombres );
        listado.add(nmap);

        return listado;
    }
    
    public List<Map> listarDocumentosConstancia(AmaSolicitudTarjeta registroId, Date fechaTransmision){
        List<Map> listado = new ArrayList();
        Map nmap;
        
        if(registroId.getComprobanteId() != null){
            nmap = new HashMap();
            nmap.put("nombre", "VOUCHER:");
            nmap.put("descripcion", "NRO. "+registroId.getComprobanteId().getNroSecuencia() + ", MONTO: S/."+ registroId.getComprobanteId().getImporte());
            nmap.put("textoFecha", "FECHA:" );
            nmap.put("fecha", JsfUtil.dateToString(registroId.getComprobanteId().getFechaMovimiento(), "dd/MM/yyyy") );
            listado.add(nmap);
        }
        return listado;
    }

    public String estiloPorTipo(Map item){
         if(item == null){
            return null;
        }
        if(item.get("SOLICITUD_ID") != null){
            return "dtSolicitudtarjeta-row-creada";
        }else{ 
            if(item.get("TIPO_INTERNAMIENTO_CODPROG") == null ){
                return null;
            }
            if(item.get("TIPO_INTERNAMIENTO_CODPROG").toString().equals("TP_INTER_DEF") || item.get("TIPO_INTERNAMIENTO_CODPROG").toString().equals("TP_INTER_IDD")){
                return null;
            }else{
                return "dtSolicitudtarjeta-row-sinverificar";
            }
        }
    }
    
}
