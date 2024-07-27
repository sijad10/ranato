package pe.gob.sucamec.bdintegrado.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import pe.gob.sucamec.bdintegrado.data.SbCsMedicoEstabsal;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
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
import java.util.Objects;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.SbCsCertifMedico;
import pe.gob.sucamec.bdintegrado.data.SbCsEstablecimiento;
import pe.gob.sucamec.bdintegrado.data.SbCsMedico;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import wspide.ResPideMigra;

// Nombre de la instancia en la aplicacion //
@Named("sbCsMedicoEstabsalController")
@SessionScoped

/**
 * Clase con SbCsMedicoEstabsalController instanciada como
 * sbCsMedicoEstabsalController. Contiene funciones utiles para la entidad
 * SbCsMedicoEstabsal. Esta vinculada a las páginas
 * SbCsMedicoEstabsal/create.xhtml, SbCsMedicoEstabsal/update.xhtml,
 * SbCsMedicoEstabsal/list.xhtml, SbCsMedicoEstabsal/view.xhtml Nota: Las tablas
 * deben tener la estructura de Sucamec para que funcione adecuadamente, revisar
 * si tiene el campo activo y modificar las búsquedas.
 */
public class SbCsMedicoEstabsalController implements Serializable {

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
    SbCsMedicoEstabsal registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbCsMedicoEstabsal> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SbCsMedicoEstabsal> registrosSeleccionados;
    //
    private Integer tipoBusqueda;
    private SbCsEstablecimiento establecimientoCS;
    private boolean renderComboEstablecimiento;
    private boolean renderMedico;
    private SbCsMedico medicoRegister;
    private int centrosHabilitados;
    private String numDoc;
    private String nroCmp;
    private TipoBaseGt tipoDoc;
    private int maxLength;
    private UploadedFile file;
    private StreamedContent foto;
    private byte[] fotoByte;
    private String fileName;
    private String fileNameTemporal;
    ///
    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbCsMedicoEstabsalFacade ejbSbCsMedicoEstabsalFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbCsMedicoFacade ejbSbCsMedicoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbCsEstablecimientoFacade ejbSbCsEstablecimientoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbCsCertifMedicoFacade ejbSbCsCertifMedicoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @Inject
    WsPide wsPideController;
    
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
    public List<SbCsMedicoEstabsal> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SbCsMedicoEstabsal> a) {
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
            for (SbCsMedicoEstabsal a : registrosSeleccionados) {
                a.setActivo((short) 0);
                ejbSbCsMedicoEstabsalFacade.edit(a);
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
            for (SbCsMedicoEstabsal a : registrosSeleccionados) {
                a.setActivo((short) 1);
                ejbSbCsMedicoEstabsalFacade.edit(a);
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
            registro = (SbCsMedicoEstabsal) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbSbCsMedicoEstabsalFacade.edit(registro);
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
            registro = (SbCsMedicoEstabsal) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbSbCsMedicoEstabsalFacade.edit(registro);
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
    public SbCsMedicoEstabsal getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbCsMedicoEstabsal registro) {
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
    public ListDataModel<SbCsMedicoEstabsal> getResultados() {
        return resultados;
    }
    
    public Integer getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Integer tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public SbCsEstablecimiento getEstablecimientoCS() {
        return establecimientoCS;
    }

    public void setEstablecimientoCS(SbCsEstablecimiento establecimientoCS) {
        this.establecimientoCS = establecimientoCS;
    }

    public boolean isRenderComboEstablecimiento() {
        return renderComboEstablecimiento;
    }

    public void setRenderComboEstablecimiento(boolean renderComboEstablecimiento) {
        this.renderComboEstablecimiento = renderComboEstablecimiento;
    }

    public boolean isRenderMedico() {
        return renderMedico;
    }

    public void setRenderMedico(boolean renderMedico) {
        this.renderMedico = renderMedico;
    }

    public SbCsMedico getMedicoRegister() {
        return medicoRegister;
    }

    public void setMedicoRegister(SbCsMedico medicoRegister) {
        this.medicoRegister = medicoRegister;
    }

    public int getCentrosHabilitados() {
        return centrosHabilitados;
    }

    public void setCentrosHabilitados(int centrosHabilitados) {
        this.centrosHabilitados = centrosHabilitados;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNroCmp() {
        return nroCmp;
    }

    public void setNroCmp(String nroCmp) {
        this.nroCmp = nroCmp;
    }

    public TipoBaseGt getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoBaseGt tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameTemporal() {
        return fileNameTemporal;
    }

    public void setFileNameTemporal(String fileNameTemporal) {
        this.fileNameTemporal = fileNameTemporal;
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
    public List<SbCsMedicoEstabsal> getSelectItems() {
        return ejbSbCsMedicoEstabsalFacade.findAll();
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
        resultados = new ListDataModel(ejbSbCsMedicoEstabsalFacade.selectLike(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (SbCsMedicoEstabsal) resultados.getRowData();
        nroCmp = registro.getMedicoId().getNroCmp();
        estado = EstadoCrud.VER;
        cargarFirma();
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        try {
            reiniciarValores();
            registro = (SbCsMedicoEstabsal) resultados.getRowData();
            resultados = null;
            nroCmp = registro.getMedicoId().getNroCmp();
            estado = EstadoCrud.EDITAR;
            setRenderMedico(false);
            cargarFirma();
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
        try {
            reiniciarValores();
            resultados = null;
            estado = EstadoCrud.CREAR;
            registro = new SbCsMedicoEstabsal();
            registro.setHabilitado(JsfUtil.TRUE);
            registro.setEsDelegado(JsfUtil.FALSE);
            registro.setActivo(JsfUtil.TRUE);
            registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            registro.setAudNumIp(JsfUtil.getIpAddress());
            registro.setUsuarioId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar formulario");
        }
    }

    /**
     * Evento para guardar la información al editar un registro.
     * @return
     */
    public String editar() {
        try {
            if(validacionCreateEdit()) {
                registro = (SbCsMedicoEstabsal) JsfUtil.entidadMayusculas(registro, "");
                
                registro.getMedicoId().setNroCmp(nroCmp);
                if(registro.getEsDelegado() == null){
                    registro.setEsDelegado(JsfUtil.FALSE);
                }
                if(registro.getMedicoId().getId() == null){
                    if(registro.getMedicoId().getPersonaId().getId() == null){
                        ejbSbPersonaFacade.create(registro.getMedicoId().getPersonaId());
                    }
                    ejbSbCsMedicoFacade.create(registro.getMedicoId());
                }else{
                    ejbSbCsMedicoFacade.edit(registro.getMedicoId());
                }
                //// Guardado de firma del médico ///
                if(!Objects.equals(fileNameTemporal, fileName)){
                    String name = file.getFileName();
                    String nombreFoto = "FIRMA_"+registro.getMedicoId().getId() + "_" + (new Date()).getTime()  + name.substring(name.lastIndexOf('.'), name.length());
                    registro.getMedicoId().setAdjuntoFirma(nombreFoto.toUpperCase());
                    registro.getMedicoId().setFechaAdjuntoFirma(new Date());
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_medico").getValor() +  registro.getMedicoId().getAdjuntoFirma() ), fotoByte );
                    ejbSbCsMedicoFacade.edit(registro.getMedicoId());
                }
                //////
                
                ejbSbCsMedicoEstabsalFacade.edit(registro);
                estado = EstadoCrud.BUSCAR;
                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
                return prepareList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     * @return 
     */
    public String crear() {
        try {            
            if(validacionCreateEdit()) {
                registro = (SbCsMedicoEstabsal) JsfUtil.entidadMayusculas(registro, "");
                
                registro.getMedicoId().setNroCmp(nroCmp);
                if(registro.getMedicoId().getId() == null){
                    if(registro.getMedicoId().getPersonaId().getId() == null){
                        ejbSbPersonaFacade.create(registro.getMedicoId().getPersonaId());
                    }
                    ejbSbCsMedicoFacade.create(registro.getMedicoId());
                }else{
                    ejbSbCsMedicoFacade.edit(registro.getMedicoId());
                }
                
                //// Guardado de firma del médico ///
                if(!Objects.equals(fileNameTemporal, fileName)){
                    String name = file.getFileName();
                    String nombreFoto = "FIRMA_"+registro.getMedicoId().getId() + "_" + (new Date()).getTime()  + name.substring(name.lastIndexOf('.'), name.length());
                    registro.getMedicoId().setAdjuntoFirma(nombreFoto.toUpperCase());
                    registro.getMedicoId().setFechaAdjuntoFirma(new Date());
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_medico").getValor() +  registro.getMedicoId().getAdjuntoFirma() ), fotoByte );
                    ejbSbCsMedicoFacade.edit(registro.getMedicoId());
                }
                //////
                
                ejbSbCsMedicoEstabsalFacade.create(registro);
                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado") + " " + registro.getId());                
                return prepareList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SbCsMedicoEstabsal getSbCsMedicoEstabsal(Long id) {
        return ejbSbCsMedicoEstabsalFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SbCsMedicoEstabsalController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbCsMedicoEstabsalConverter")
    public static class SbCsMedicoEstabsalControllerConverterN extends SbCsMedicoEstabsalControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbCsMedicoEstabsal.class)
    public static class SbCsMedicoEstabsalControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbCsMedicoEstabsalController c = (SbCsMedicoEstabsalController) JsfUtil.obtenerBean("sbCsMedicoEstabsalController", SbCsMedicoEstabsalController.class);
            return c.getSbCsMedicoEstabsal(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbCsMedicoEstabsal) {
                SbCsMedicoEstabsal o = (SbCsMedicoEstabsal) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbCsMedicoEstabsal.class.getName());
            }
        }
    }
    
    ///////////////////////////////////////////////////
    
    public boolean esRolCentroSalud(){
        return JsfUtil.buscarPerfilUsuario("AMA_CSALUD");
    }
    
    public String prepareList(){
        if(esRolCentroSalud()){
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            reiniciarCamposBusqueda();
            centrosHabilitados = ejbSbCsEstablecimientoFacade.contarEstablecimientosHabilitadosByRuc(p_user.getNumDoc());
            resultados = null;
            estado = EstadoCrud.BUSCAR;
            
            if(centrosHabilitados == 0){
                JsfUtil.mensajeAdvertencia("Ud. no cuenta con algún centro de salud habilitado o vigente. Por favor, solicite su habilitación como Centro de Salud autorizado en la Plataforma Virtual SEL");
            }else{
                int contInhabilitados = ejbSbCsEstablecimientoFacade.contarEstablecimientosInhabilitadosByRuc(p_user.getNumDoc());
                if(contInhabilitados > 0){
                    JsfUtil.mensajeAdvertencia("Ud. tiene "+ contInhabilitados + " centro(s) de salud inhabilitado(s)");
                }
            }
            
            return "/aplicacion/gamac/sbCsMedicoEstabsal/List";
        }else{
            JsfUtil.mensajeAdvertencia("Esta opción es permitida sólo para perfiles de Centros de Salud"); 
        }
        return null;
    }
    
    public void reiniciarValores(){
        filtro = null;
        nroCmp = null;
        establecimientoCS = null;
        setRenderComboEstablecimiento(false);
        setRenderMedico(true);
        medicoRegister = null;
        foto = null;
        fotoByte = null;
        file = null;
        fileName = "";
        fileNameTemporal = "";
    }
    
    public void cambiaTipoBusqueda(){
        filtro = null;
        establecimientoCS = null;
        if (tipoBusqueda != null && tipoBusqueda.equals(3)) {
            setRenderComboEstablecimiento(true);            
        } else {
            setRenderComboEstablecimiento(false);
        }
        RequestContext.getCurrentInstance().update("listForm:pnlBusquedaCriterio");
    }
    
    public List<SbCsEstablecimiento> getObtenerEstablecimientosCS(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        return ejbSbCsEstablecimientoFacade.listarEstablecimientos(p_user.getNumDoc(), false);
    }
    
    public List<SbCsEstablecimiento> getObtenerEstablecimientosCSActivo(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        return ejbSbCsEstablecimientoFacade.listarEstablecimientos(p_user.getNumDoc(), true);
    }
    
    public void buscarBandeja(){
        List<SbCsMedicoEstabsal> listado = new ArrayList();

        if (tipoBusqueda == null) {
            JsfUtil.mensajeError("Debe seleccionar al menos un criterio de búsqueda para mostrar resultados");
            return;
        }
        
        if (!(tipoBusqueda != null && ((filtro == null || filtro.isEmpty()) && establecimientoCS == null))) {
            resultados = null;
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            HashMap mMap = new HashMap();
            mMap.put("buscarPor", tipoBusqueda);
            mMap.put("filtro", (filtro != null) ? filtro.trim().toUpperCase() : filtro);
            mMap.put("establecimientoCS", (establecimientoCS != null)?establecimientoCS.getId():null );
            mMap.put("ruc", p_user.getNumDoc() );
            
            listado = ejbSbCsMedicoEstabsalFacade.buscarMedicosCS(mMap);
            if (listado != null) {
                resultados = new ListDataModel(listado);
            }
            reiniciarCamposBusqueda();
        } else {
            JsfUtil.mensajeError("Por favor, ingresar el campo a buscar");
        }
    }
    
    public void reiniciarCamposBusqueda(){
        filtro = null;
        tipoBusqueda = null;
        setRenderComboEstablecimiento(false);
        establecimientoCS = null;
    }
    
    public String getTipoDocumento(SbPersonaGt per){
        String tipoDoc = "";
        
        if(per.getTipoDoc() != null){
            tipoDoc = per.getTipoDoc().getNombre();
        }else{
            tipoDoc = "DNI";
        }        
        return tipoDoc;
    }
    
     public String getMedicoActivoXEstablecimiento(SbCsMedicoEstabsal med){
        String activo = "ACTIVO";
        Calendar c_hoy = Calendar.getInstance();
        
        if( (JsfUtil.getFechaSinHora(c_hoy.getTime()).compareTo(JsfUtil.getFechaSinHora(med.getFechaFin())) > 0) || (med.getHabilitado() == 0)  ){
            activo = "INACTIVO";
        }
        
        return activo;
    }
     
     /**
     * Función para borrar registro de medico en establecimiento
     * @author Richar Fernández
     * @version 1.0
     */
    public void borrarRegistro() {
        try {
            registro = (SbCsMedicoEstabsal) resultados.getRowData();
                registro.setActivo(JsfUtil.FALSE);
                ejbSbCsMedicoEstabsalFacade.edit(registro);
                
                List<SbCsMedicoEstabsal> lstTemporal = (List<SbCsMedicoEstabsal>) resultados.getWrappedData();
                lstTemporal.remove(registro);
                resultados = new ListDataModel(lstTemporal);

                JsfUtil.mensaje("Se ha borrado correctamente el registro.");
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
    public boolean renderBtnEditar(SbCsMedicoEstabsal estab){
        boolean validacion = true;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        
        if(estab.getEstablecimientoId().getHabilitado() == 0){
            validacion = false;
        }
        if(!p_user.getTipo().equals("TP_USR_MA")){
            validacion = false;
        }
        
        return validacion;
    }
    
    public boolean renderBtnSuspender(SbCsMedicoEstabsal estab){
        boolean validacion = true;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");

        if(estab.getEstablecimientoId().getHabilitado() == 0){
            validacion = false;
        }
        if(!p_user.getTipo().equals("TP_USR_MA")){
            validacion = false;
        }
        
        return validacion;
    }
    
    public boolean renderBtnVer(SbCsMedicoEstabsal estab){
        boolean validacion = true;

        if(estab.getEstablecimientoId().getHabilitado() == 0){
            validacion = false;
        }
        
        return validacion;
    }
    
    public boolean renderBtnBorrar(SbCsMedicoEstabsal estab){
        boolean validacion = true;
        int cont = 0;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        
        if(estab.getEstablecimientoId().getHabilitado() == 0){
            validacion = false;
            return validacion;
        }
        
        if(estab.getMedicoId().getSbCsCertifMedicoList() != null ){
            Long contCertif = ejbSbCsCertifMedicoFacade.buscarRegistrosCertifMedico(estab.getMedicoId().getId(), estab.getEstablecimientoId().getId());
            if(contCertif > 0){
                validacion = false;
            }
//            for(SbCsCertifMedico certif : estab.getMedicoId().getSbCsCertifMedicoList()){
//                if(certif.getActivo() == 1 && certif.getCertifmedicoId().getActivo() == 1 && Objects.equals(certif.getCertifmedicoId().getEstablecimientoId().getId(), estab.getEstablecimientoId().getId())){
//                    cont++;
//                }
//                if(cont > 0){
//                    validacion = false;
//                    break;
//                }
//            }
        }
        
        if(!p_user.getTipo().equals("TP_USR_MA")){
            validacion = false;
        }
        
        return validacion;
    }
    
    /**
     * Función para campo medico autocompletable
     *
     * @param query Campo a buscar
     * @return Listado de medicos
     * @author Richar Fernández
     * @version 1.0
     */
    public List<SbCsMedico> completeMedico(String query) {
        return ejbSbCsMedicoFacade.selectMedico(query);
    }
    
    /**
     * Función para campo medico autocompletable
     * @param medico medico a buscar
     * @return Descripción de propietario (Tipo Doc : Nro. Documento)
     * @author Richar Fernández
     * @version 1.0
     */
    public String obtenerDescMedico(SbCsMedico medico) {
        String nombre = "";

        if (medico != null) {
            if (medico.getPersonaId().getTipoDoc() == null) {
                nombre = "DNI ";
            }else{
                nombre = medico.getPersonaId().getTipoDoc().getNombre() + " ";
            }
            nombre = nombre + medico.getPersonaId().getNumDoc() + ", " + 
                              medico.getPersonaId().getApellidosyNombres();
        }

        return nombre;
    }
    
    /**
     * Función al seleccionar el medico del campo autocompletable
     * @author Richar Fernández
     * @version 1.0
     */
    public void seleccionaMedico() {
        setRenderMedico(false);
        if(registro != null && registro.getMedicoId() != null){
            nroCmp = registro.getMedicoId().getNroCmp();
            cargarFirma();
        }
    }
    
    /**
     * Función al eliminar propietario de campo autocompletable
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void eliminarMedicoRegistro() {
        registro.setMedicoId(null);
        setRenderMedico(true);
    }
    
    public List<TipoBaseGt> getListarCargos(){
        //return ejbTipoBaseFacade.lstTipoBase("TP_CCS");
        return ejbTipoBaseFacade.lstTiposXCodigosProg("'TP_CCS_PSA','TP_CCS_PCO','TP_CCS_TIT'");
    }
    
    public boolean validacionCreateEdit(){
        boolean validacion = true;
        
        if(registro.getEstablecimientoId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, seleccionar el establecimiento");
        }
        if(registro.getFechaIni() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingresar la fecha de inicio contractual");
        }
        if(registro.getFechaFin() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingresar la fecha de fin contractual");
        }else{
            Calendar c_hoy = Calendar.getInstance();
            if(JsfUtil.getFechaSinHora(registro.getFechaFin()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) < 0 ){
                validacion = false;
                JsfUtil.mensajeError("No puede registrar un médico con fecha contractual vencida.");
            }
        }
        if(registro.getMedicoId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingresar el médico");
        }
        if(registro.getCargoId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, seleccionar el cargo");
        }else{
           if( (registro.getCargoId().getCodProg().equals("TP_CCS_PSA") || registro.getCargoId().getCodProg().equals("TP_CCS_PCO")) && (nroCmp == null || nroCmp.isEmpty())){
                validacion = false;
                JsfUtil.mensajeError("Por favor, ingresar el nro. de CMP");
           }else{
                if(registro.getMedicoId() != null && registro.getMedicoId().getId() == null && nroCmp != null && !nroCmp.isEmpty()){
                    int cont = ejbSbCsMedicoFacade.contarNroCmpMedico(nroCmp);
                    if(cont > 0){
                        validacion = false;
                        JsfUtil.mensajeError("El nro. CMP ya ha sido registrado");
                    }
                }   
           }
           if(!registro.getCargoId().getCodProg().equals("TP_CCS_TIT")){
                if(registro.getFechaIniHab() == null){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor, ingresar la fecha de inicio de habilitación del colegio médico");
                }
                if(registro.getFechaFinHab() == null){
                    validacion = false;
                    JsfUtil.mensajeError("Por favor, ingresar la fecha de fin de habilitación de colegio médico");
                }else{
                    Calendar c_hoy = Calendar.getInstance();
                    if(JsfUtil.getFechaSinHora(registro.getFechaFinHab()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) < 0 ){
                        validacion = false;
                        JsfUtil.mensajeError("No puede registrar un médico con fecha de habilitación del colegio médico vencida.");
                    }
                }
           }
        }
        if(registro.getFechaIni() != null && registro.getFechaFin() != null){
            if(JsfUtil.getFechaSinHora(registro.getFechaIni()).compareTo(JsfUtil.getFechaSinHora(registro.getFechaFin())) >0 ){
                validacion = false;
                JsfUtil.mensajeError("La fecha de inicio no puede ser mayor que la fecha fin, por favor verifique");
            }
        }
        if(registro.getFechaIniHab() != null && registro.getFechaFinHab() != null){
            if(JsfUtil.getFechaSinHora(registro.getFechaIniHab()).compareTo(JsfUtil.getFechaSinHora(registro.getFechaFinHab())) >0 ){
                validacion = false;
                JsfUtil.mensajeError("La fecha de inicio de habilitación no puede ser mayor que la fecha fin, por favor verifique");
            }
        }
        if(registro.getEstablecimientoId() != null && registro.getMedicoId() != null && registro.getCargoId() != null){
            int cont = ejbSbCsMedicoEstabsalFacade.contarDNIPersonMedicoEstablecimiento(registro.getMedicoId().getPersonaId().getNumDoc(), registro.getEstablecimientoId().getId(),registro.getCargoId().getId(), registro.getId());
            if(cont > 0){
                validacion = false;
                JsfUtil.mensajeError("El médico ya se encuentra registrado en el establecimiento con el mismo cargo");
            }else{
                if(estado == EstadoCrud.CREAR){
                    cont = 0;
                    List<SbCsMedicoEstabsal> lstCargos = ejbSbCsMedicoEstabsalFacade.obtenerCargosMedicoXEstablecimiento(registro.getMedicoId().getPersonaId().getNumDoc(), registro.getEstablecimientoId().getId());
                    for(SbCsMedicoEstabsal estab : lstCargos){
                        switch(estab.getCargoId().getCodProg()){
                            case "TP_CCS_PCO":
                                    if(registro.getCargoId().getCodProg().equals("TP_CCS_PSA") ){
                                        validacion = false;
                                        JsfUtil.mensajeError("El médico ya está registrado en este establecimiento con un cargo de PSICÓLOGO");
                                        break;
                                    }
                                    break;
                            case "TP_CCS_PSA":
                                    if(registro.getCargoId().getCodProg().equals("TP_CCS_PCO") ){
                                        validacion = false;
                                        JsfUtil.mensajeError("El médico ya está registrado en este establecimiento con un cargo de PSIQUIATRA");
                                        break;
                                    }
                                    break;
//                            case "TP_CCS_DIR":
//                                    if(registro.getCargoId().getCodProg().equals("TP_CCS_SUB") ){
//                                        validacion = false;
//                                        JsfUtil.mensajeError("El médico ya está registrado en este establecimiento con un cargo de DIRECTOR");
//                                        break;
//                                    }
//                                    break;
//                            case "TP_CCS_SUB":
//                                    if(registro.getCargoId().getCodProg().equals("TP_CCS_DIR") && !Objects.equals(estab.getCargoId().getId(), registro.getCargoId().getId())){
//                                        validacion = false;
//                                        JsfUtil.mensajeError("El médico ya está registrado en este establecimiento con un cargo de SUBDIRECTOR");
//                                        break;
//                                    }
//                                    break;
                        }
                    }
                }   
            }
        }
        
        if(fileName == null || fileName.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor adjunte la firma del médico");
        }
        
        return validacion;
    }
    
    public void openDlgNuevoMedico(){
        if(registro.getEstablecimientoId() != null){
            numDoc = null;
            maxLength = 0;
            tipoDoc = null;
            medicoRegister = new SbCsMedico();
            medicoRegister.setId(null);            
            medicoRegister.setActivo(JsfUtil.TRUE);
            medicoRegister.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            medicoRegister.setAudNumIp(JsfUtil.getIpAddress());
            medicoRegister.setUsuarioId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()) );
            limpiarPersona();
            RequestContext.getCurrentInstance().execute("PF('wvRegistrarMedico').show()");
            RequestContext.getCurrentInstance().update("medicoForm");
        }else{
            JsfUtil.mensajeError("Por favor, seleccione primero el establecimiento");
        }
    }
    
    public void limpiarPersona(){
        medicoRegister.setPersonaId(new SbPersonaGt());
        medicoRegister.getPersonaId().setId(null);
        medicoRegister.getPersonaId().setActivo(JsfUtil.TRUE);
        medicoRegister.getPersonaId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
        medicoRegister.getPersonaId().setAudNumIp(JsfUtil.getIpAddress());
        //medicoRegister.getPersonaId().setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
        medicoRegister.getPersonaId().setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
    }
    
    public boolean validacionNuevoMedico(){
        boolean validacion = true;
        if(medicoRegister.getPersonaId().getNumDoc() == null || medicoRegister.getPersonaId().getNumDoc().isEmpty() ){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingrese el DNI");
        }else{
            int cont = ejbSbCsMedicoFacade.contarDNIPersonaMedico(medicoRegister.getPersonaId().getNumDoc());
            if(cont > 0){
                validacion = false;
                JsfUtil.mensajeError("El médico ya se encuentra registrado");
            }
        }
        if(medicoRegister.getPersonaId().getApePat() == null || medicoRegister.getPersonaId().getApePat().isEmpty() ){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingrese el apellido paterno");
        }
        if(medicoRegister.getPersonaId().getApeMat() == null || medicoRegister.getPersonaId().getApeMat().isEmpty() ){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingrese el apellido materno");
        }
        if(medicoRegister.getPersonaId().getNombres() == null || medicoRegister.getPersonaId().getNombres().isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingrese los nombres");
        }
        return validacion;
    }
    
    public void registrarNuevoMedico(){
        try {
            if(validacionNuevoMedico()){
                registro.setMedicoId(medicoRegister);
                seleccionaMedico();
                
                JsfUtil.mensaje("Médico registrado correctamente");
                RequestContext.getCurrentInstance().execute("PF('wvRegistrarMedico').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al registrar el médico");
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
    
    public void buscarDlgPersona(){
         boolean validacion = true;
        
        limpiarPersona();

        if(tipoDoc == null){
            validacion = false;
            JsfUtil.mensajeAdvertencia("Por favor ingresar el tipo de documento");
        }
        if(numDoc == null || numDoc.isEmpty()){
            validacion = false;
            JsfUtil.mensajeAdvertencia("Por favor ingresar el nro. de DNI");
        }else{
            if(tipoDoc.getCodProg().equals("TP_DOCID_DNI")){
                if(numDoc.trim().length() != 8){
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("El DNI debe tener 8 dígitos.");
                }
            }else{
                if(numDoc.trim().length() != 9){
                    validacion = false;
                    JsfUtil.mensajeAdvertencia("El CE debe tener 9 dígitos.");
                }
            }                
        }
        if(validacion){
            medicoRegister.getPersonaId().setNumDoc(numDoc);
            SbPersonaGt persona = ejbSbPersonaFacade.buscarPersonaSel("", medicoRegister.getPersonaId().getNumDoc());
            if(persona != null){
                medicoRegister.setPersonaId(persona);
            }else{
                if(tipoDoc.getCodProg().equals("TP_DOCID_DNI")){
                        persona = wsPideController.buscarReniec(medicoRegister.getPersonaId());
                        if(persona !=null){
                            if(persona.getNombres() != null){
                                medicoRegister.getPersonaId().setNombres(persona.getNombres());
                                medicoRegister.getPersonaId().setApePat(persona.getApePat());
                                medicoRegister.getPersonaId().setApeMat(persona.getApeMat());
                                medicoRegister.getPersonaId().setGeneroId(persona.getGeneroId());
                                medicoRegister.getPersonaId().setFechaNac(persona.getFechaNac());
                                medicoRegister.getPersonaId().setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                                RequestContext.getCurrentInstance().update(obtenerForm());
                            }else{
                                medicoRegister.getPersonaId().setNumDoc(null);
                                medicoRegister.getPersonaId().setNombres(null);
                                medicoRegister.getPersonaId().setApePat(null);
                                medicoRegister.getPersonaId().setApeMat(null);
                                medicoRegister.getPersonaId().setGeneroId(null);
                                medicoRegister.getPersonaId().setFechaNac(null);
                                JsfUtil.mensajeAdvertencia("No se pudo encontrar a la persona.");
                            }
                        }else{
                            medicoRegister.getPersonaId().setNumDoc(null);
                            medicoRegister.getPersonaId().setNombres(null);
                            medicoRegister.getPersonaId().setApePat(null);
                            medicoRegister.getPersonaId().setApeMat(null);
                            medicoRegister.getPersonaId().setGeneroId(null);
                            medicoRegister.getPersonaId().setFechaNac(null);
                        }
                }else{
                        ResPideMigra resMigra = wsPideController.buscarCarnetExtranjeria( ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_usuario_Pide").getValor() , numDoc);
                        if(resMigra != null){
                            if(resMigra.isRspta()){
                                if(resMigra.getResultado().getStrNumRespuesta().equals("0000")){
                                    medicoRegister.getPersonaId().setNombres(resMigra.getResultado().getStrNombres());
                                    medicoRegister.getPersonaId().setApePat(resMigra.getResultado().getStrPrimerApellido());
                                    medicoRegister.getPersonaId().setApeMat(resMigra.getResultado().getStrSegundoApellido());                                    
                                    medicoRegister.getPersonaId().setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_CE"));
                                    JsfUtil.mensaje("Se encontró datos de la persona en Migraciones");
                                }else{
                                    medicoRegister.getPersonaId().setNumDoc(null);
                                    medicoRegister.getPersonaId().setNombres(null);
                                    medicoRegister.getPersonaId().setApePat(null);
                                    medicoRegister.getPersonaId().setApeMat(null);
                                    medicoRegister.getPersonaId().setGeneroId(null);
                                    medicoRegister.getPersonaId().setFechaNac(null);
                                    JsfUtil.mensajeAdvertencia("No se pudo encontrar datos de la persona en Migraciones");
                                }
                            }else{
                                medicoRegister.getPersonaId().setNumDoc(null);
                                medicoRegister.getPersonaId().setNombres(null);
                                medicoRegister.getPersonaId().setApePat(null);
                                medicoRegister.getPersonaId().setApeMat(null);
                                medicoRegister.getPersonaId().setGeneroId(null);
                                medicoRegister.getPersonaId().setFechaNac(null);
                                JsfUtil.mensajeAdvertencia("Hubo un error consultando el servicio de Migraciones. Tendrá que registrar los datos manualmente");
                            }
                        }else{
                            medicoRegister.getPersonaId().setNumDoc(null);
                            medicoRegister.getPersonaId().setNombres(null);
                            medicoRegister.getPersonaId().setApePat(null);
                            medicoRegister.getPersonaId().setApeMat(null);
                            medicoRegister.getPersonaId().setGeneroId(null);
                            medicoRegister.getPersonaId().setFechaNac(null);
                            JsfUtil.mensajeAdvertencia("El servicio de Migraciones no está disponible. Tendrá que registrar los datos manualmente");
                        }
                }
            }
        }
    }

    public String obtenerDescMedicoVer(SbCsMedico medico) {
        String nombre = "";

        if (medico != null) {
            if (medico.getPersonaId().getTipoDoc() == null) {
                nombre = "DNI ";
            }else{
                nombre = medico.getPersonaId().getTipoDoc().getNombre() + " ";
            }
            nombre = nombre + medico.getPersonaId().getNumDoc() + ", " + 
                              medico.getPersonaId().getApellidosyNombres() ;
        }

        return nombre;
    }
    
    public String obtenerEstadoMedico(SbCsMedicoEstabsal medicoEstab){
        String activo = "";
        Calendar c_hoy = Calendar.getInstance();
        
        if( (JsfUtil.getFechaSinHora(c_hoy.getTime()).compareTo(JsfUtil.getFechaSinHora(medicoEstab.getFechaFin())) > 0) || (medicoEstab.getHabilitado() == 0) ){
            activo = "INACTIVO";
        }else{
            activo = "ACTIVO";
        }
        
        return activo;
    }

    public String obtenerDescripcionEstablecimiento(SbCsEstablecimiento estab){
        String nombre = estab.getNombre() + " -";
        
        if(estab.getDireccionId().getViaId() != null){
            if(!estab.getDireccionId().getViaId().getCodProg().equals("TP_VIA_OTRO")){
                nombre = nombre + " "+ estab.getDireccionId().getViaId().getNombre();
            }
        }
        nombre = nombre + " " + estab.getDireccionId().getDireccion();
        if(estab.getDireccionId().getNumero() != null){
            nombre = nombre + " " + estab.getDireccionId().getNumero();
        }
        
        return nombre;
    }
    
    public void habilitarCentroSalud(){
        try {
            String habilitado = "";
            registro = (SbCsMedicoEstabsal) resultados.getRowData();
            
            if(registro.getHabilitado() == JsfUtil.TRUE){
                registro.setHabilitado(JsfUtil.FALSE);
                habilitado = "suspendido.";
            }else{
                registro.setHabilitado(JsfUtil.TRUE);
                habilitado = "habilitado.";
            }            
            ejbSbCsMedicoEstabsalFacade.edit(registro);
            JsfUtil.mensaje("El médico fue "+habilitado);
            prepareList();            
        } catch (Exception e) {
            JsfUtil.mensajeError("Hubo un error al actualizar el médico");
        }
    }
    
    public void cambiarTipoCargomedico(){
        if(registro != null){
            if(registro.getCargoId() == null){
                JsfUtil.mensajeError("Por favor seleccione un tipo de cargo.");
                return;
            }
             if(registro.getEstablecimientoId() == null){
                registro.setCargoId(null);
                JsfUtil.mensajeError("Por favor seleccione un establecimiento primero.");
                return;
            }
        }
    }
    
    public boolean renderDelegadoMedico(){
        boolean render = false;
        if(registro != null && registro.getCargoId() != null){
            if(!registro.getCargoId().getCodProg().equals("TP_CCS_TIT")){
                render = true;
            }
        }
        return render;
    }
    
    public void cambiaEsDelegado(){
        if(registro != null && registro.getEstablecimientoId() != null && registro.getEsDelegadoBoolean()){
            int cont = ejbSbCsMedicoEstabsalFacade.contarDelegadosXEstablecimiento(registro.getEstablecimientoId().getId());
            if(cont > 0){
                registro.setEsDelegado(JsfUtil.FALSE);
                JsfUtil.mensajeError("Existe un médico en este establecimiento que ya se le asignó como encargado del Titular.");
            }else{
                cont = ejbSbCsMedicoEstabsalFacade.contarDelegadosXEstablecimientoXMedico(registro.getEstablecimientoId().getId(), registro.getMedicoId().getId());
                if(cont > 0){
                    registro.setEsDelegado(JsfUtil.FALSE);
                    JsfUtil.mensajeError("Este médico ya es titular en este mismo establecimiento, no puede ser delegado(a)");
                }
            }
        }
    }
    
    public String obtenerDescEsDelegado(){
        String desc = "NO";
        if(registro != null && registro.getEsDelegadoBoolean()){
            desc = "SI";
        }
        return desc;
    }
    
    public String obtenerDescDelegadoListadoMedicos(SbCsMedicoEstabsal estabMed){
        String desc = "";
        
        if(estabMed != null){
            desc = estabMed.getCargoId().getNombre();
            if(estabMed.getEsDelegadoBoolean()){
                desc += " " + JsfUtil.bundleBDIntegrado("sbCsCertifsalud_crear_dtMedico_descDelegadoT");
            }
        }
        
        return desc;
    }
    
    public List<TipoBaseGt> getListarTipoDoc(){
        return ejbTipoBaseFacade.lstTiposXCodigosProg("'TP_DOCID_DNI','TP_DOCID_CE'"); // 
    }
    
    public void cambiarTipoDoc(){
        numDoc = "";
        maxLength = 0;
        if(tipoDoc != null){
            if(tipoDoc.getCodProg().equals("TP_DOCID_DNI")){
                maxLength = 8;
            }else{
                maxLength = 9;
            }
        }else{
            JsfUtil.mensajeError("Por favor seleccione el tipo de documento");
        }
    }
    
    public void handleFileUploadFirma(FileUploadEvent event) {
        try {
            if (event != null) {
                file = event.getFile();
                if (JsfUtil.verificarJPG(file) || JsfUtil.verificarPDF(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                    fileName = file.getFileName();
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de cargar no tiene formato PDF ni JPG");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String obtenerMsjeInvalidSizeFoto(){
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " " +
               (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("SbCsMedicoEstabsal_sizeFoto").getValor())/1024) + " Kb. ";
    }
    
    public StreamedContent getFotoFirma() {
        try {
            if(fotoByte != null){
                InputStream is = new ByteArrayInputStream(fotoByte);
                foto = new DefaultStreamedContent(is, "image/jpeg", "foto");
            }else if (file != null) {
                fotoByte = IOUtils.toByteArray(file.getInputstream());
                foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
            }
        } catch (Exception ex) {
            fotoByte = null;
            foto = null;
        }
        return foto;
    }
    
    public StreamedContent getPdfFirma() {
        try {
            if(fotoByte != null){
                InputStream is = new ByteArrayInputStream(fotoByte);
                foto = new DefaultStreamedContent(is, "application/pdf", "pdfFirma" );
                
            }else if (file != null) {
                fotoByte = IOUtils.toByteArray(file.getInputstream());
                foto = new DefaultStreamedContent(file.getInputstream(), "application/pdf");
            }
        } catch (Exception ex) {
            fotoByte = null;
            foto = null;
        }
        return foto;
    }
    
    public boolean renderFotoFirma(){
        boolean validacion = false;
        
        if(fileName != null && !fileName.isEmpty()){
            if(fileName.toUpperCase().contains("JPG")){
                validacion = true;
            }
        }
        return validacion;
    }
    
    public boolean renderPdfFirma(){
        boolean validacion = false;
        
        if(fileName != null && !fileName.isEmpty()){
            if(fileName.toUpperCase().contains("PDF")){
                validacion = true;
            }
        }
        return validacion;
    }
    
    public void cargarFirma(){
        try {
            if(registro != null && registro.getMedicoId() != null && registro.getMedicoId().getAdjuntoFirma() != null){
                fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_medico").getValor() + registro.getMedicoId().getAdjuntoFirma() ));
                fileName = registro.getMedicoId().getAdjuntoFirma();
                fileNameTemporal = registro.getMedicoId().getAdjuntoFirma();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
}
