package pe.gob.sucamec.bdintegrado.jsf;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import java.util.List;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.bean.SbPublicacionDocFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade;
import pe.gob.sucamec.bdintegrado.data.SbPublicacionDoc;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

// Nombre de la instancia en la aplicacion //
@Named("sbPublicacionDocController")
@SessionScoped

/**
 * Clase con SbDepartamentoController instanciada como sbDepartamentoController.
 * Contiene funciones utiles para la entidad SbDepartamento. Esta vinculada a
 * las páginas SbDepartamento/create.xhtml, SbDepartamento/update.xhtml,
 * SbDepartamento/list.xhtml, SbDepartamento/view.xhtml Nota: Las tablas deben
 * tener la estructura de Sucamec para que funcione adecuadamente, revisar si
 * tiene el campo activo y modificar las búsquedas.
 */
public class SbPublicacionDocController implements Serializable {

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
    SbPublicacionDoc registro;
    SbPublicacionDoc registroBtn;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbPublicacionDoc> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SbPublicacionDoc> registrosSeleccionados;
    //
    private String tipoBusqueda;    
    private String archivo;
    private String numeroDoc;    
    private String extension;
    private String tipoArchivo;
    private UploadedFile file;
    private StreamedContent scFile;
    private byte[] docByte;    
    private TipoBaseGt area;
    private boolean disabledArea;    
    private List<TipoBaseGt> lstAreas;
    private Integer anioReporte;
    private TipoBaseGt tipoDoc;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private SbPublicacionDocFacade ejbSbPublicacionDocFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;
    @EJB
    private TipoBaseFacade ejbTipoBaseFacade;
    
    

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
    public List<SbPublicacionDoc> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SbPublicacionDoc> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public SbPublicacionDoc getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbPublicacionDoc registro) {
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

    
    public SbPublicacionDoc getRegistroBtn() {
        return registroBtn;
    }

    /**
     * Propiedad para los resultados de la búsqueda del CRUD.
     *
     * @return Los resultados de la búsqueda.
     */
    public void setRegistroBtn(SbPublicacionDoc registroBtn) {    
        this.registroBtn = registroBtn;
    }

    public ListDataModel<SbPublicacionDoc> getResultados() {
        return resultados;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getScFile() {
        return scFile;
    }

    public void setScFile(StreamedContent scFile) {
        this.scFile = scFile;
    }

    public byte[] getDocByte() {
        return docByte;
    }

    public void setDocByte(byte[] docByte) {
        this.docByte = docByte;
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

    public TipoBaseGt getArea() {
        return area;
    }

    public void setArea(TipoBaseGt area) {
        this.area = area;
    }

    public boolean isDisabledArea() {
        return disabledArea;
    }

    public void setDisabledArea(boolean disabledArea) {
        this.disabledArea = disabledArea;
    }

    public List<TipoBaseGt> getLstAreas() {
        return lstAreas;
    }

    public void setLstAreas(List<TipoBaseGt> lstAreas) {
        this.lstAreas = lstAreas;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public Integer getAnioReporte() {
        return anioReporte;
    }

    public void setAnioReporte(Integer anioReporte) {
        this.anioReporte = anioReporte;
    }

    public TipoBaseGt getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoBaseGt tipoDoc) {
        this.tipoDoc = tipoDoc;
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
    public List<SbPublicacionDoc> getSelectItems() {
        return ejbSbPublicacionDocFacade.findAll();
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
        resultados = new ListDataModel(ejbSbPublicacionDocFacade.selectLike(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (SbPublicacionDoc) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        registro = (SbPublicacionDoc) resultados.getRowData();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new SbPublicacionDoc();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public void editar() {
        try {
            registro = (SbPublicacionDoc) JsfUtil.entidadMayusculas(registro, "");
            ejbSbPublicacionDocFacade.edit(registro);
            estado = EstadoCrud.BUSCAR;
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     */
    public void crear() {
        try {
            registro = (SbPublicacionDoc) JsfUtil.entidadMayusculas(registro, "");
            ejbSbPublicacionDocFacade.create(registro);
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
    public SbPublicacionDoc getSbPublicacionDoc(Long id) {
        return ejbSbPublicacionDocFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SbPublicacionDocController() {
        estado = EstadoCrud.BUSCAR;
    }

    
    @FacesConverter(value = "sbPublicacionDocConverter")
    public static class SbPublicacionDocControllerConverterN extends SbPublicacionDocControllerConverter {
    }

    
    @FacesConverter(forClass = SbPublicacionDoc.class)
    public static class SbPublicacionDocControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbPublicacionDocController c = (SbPublicacionDocController) JsfUtil.obtenerBean("sbPublicacionDocController", SbPublicacionDocController.class);
            return c.getSbPublicacionDoc(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbPublicacionDoc) {
                SbPublicacionDoc o = (SbPublicacionDoc) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbPublicacionDoc.class.getName());
            }
        }
    }
    
    public void reiniciarValoresListado(){
        tipoBusqueda = "expe";
        filtro = "";
    }
    
    public void limpiarValoresRegistro(){
        file = null;
        archivo = "";
        registro = null;
        area = null;
        tipoDoc = null;
        numeroDoc = "";
        disabledArea = false;
    }
    
    public Date getFechaDia(){
        return new Date();
    }
    
    public String obtenerMsjeInvalidSize(){
        return JsfUtil.bundle("SbPublicacionDoc_fileUpload_InvalidSizeMessage") + " " + 
               (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("sbPublicacionDoc_sizeLimite_archivo").getValor())/(1024*1024)) + " Mb";
    }
    
    public void verificaExtension(String item){
        if(item.trim().toUpperCase().contains(".PDF")){
            extension = "PDF";
            tipoArchivo = "application/pdf"; 
        }
        if(item.trim().toUpperCase().contains(".DOC")){
            extension = "DOC";
            tipoArchivo = "application/msword";
        }
        if(item.trim().toUpperCase().contains(".XLS")){
            extension = "XLS";
            tipoArchivo = "application/vnd.ms-excel";
        }
    }
    
    public StreamedContent cargarDocumento(){
        try {
            if(file != null){                
                return new DefaultStreamedContent(file.getInputstream(), tipoArchivo, file.getFileName() );
            }
            if(registro == null){
                return null;
            }
            String nombreArchivo = registro.getArchivo();
            verificaExtension(nombreArchivo);
            if(nombreArchivo != null && !nombreArchivo.isEmpty()){
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_sbPublicacionDoc").getValor(), nombreArchivo, nombreArchivo, tipoArchivo);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }        
    
    public StreamedContent descagarArchivo(SbPublicacionDoc item){
        try {
            if(item == null){
                return null;
            }
            verificaExtension(item.getArchivo());
            return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_sbPublicacionDoc").getValor(), item.getArchivo(), item.getArchivo(), tipoArchivo);
        } catch (Exception e) {
            JsfUtil.mensajeError("Hubo un error al descargar el archivo");
        }
        return null;
    }

    public List<TipoBaseGt> getListadoAreas(){
        if(area == null){
            area = ejbTipoBaseFacade.tipoBaseXCodProg("TP_AREA_SN");
        }
        if(anioReporte == null){
            anioReporte = Integer.parseInt(JsfUtil.dateToString(new Date(), "yyyy"));
        }
        if(tipoDoc == null){
            tipoDoc = ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOC_RES");
        }
        buscarPublicacionesMovil();
        String areasCodProgs = ejbSbParametroFacade.obtenerParametroXNombre("sbPublicacionDoc_areas").getValor();
        return ejbTipoBaseFacade.lstTiposXCodigosProg(areasCodProgs);
    }
    
    public List<TipoBaseGt> getListadoTipoDocs(){        
        return ejbTipoBaseFacade.lstTiposXCodigosProg("'TP_DOC_RES','TP_DOC_DIR','TP_DOC_INF','TP_DOC_CVN','TP_DOC_OFI'");    
    }    
    
    public void buscarPublicacionesMovil(){
        List<SbPublicacionDoc> listado = ejbSbPublicacionDocFacade.buscarPublicacionMovil(filtro, area.getId(), anioReporte.longValue(), tipoDoc.getId());
        if(listado != null){
            resultados = new ListDataModel<>(listado);
        }
    }
    
    public List<Integer> obtenerListadoAnio(){
        List<Integer> listado = new ArrayList();
        Integer anioInicial = Integer.parseInt(JsfUtil.dateToString(new Date(), "yyyy"));
        Integer anioFinal = 2013;
        for(int i = anioInicial; i >= anioFinal ;i--){
            listado.add(i);
        }
        return listado;
    }
    
    public String obtenerDescDocumento(){
        if(tipoDoc == null){
            return "Nro. documento";
        }
        if(tipoDoc.getCodProg().equals("TP_DOC_RES") || tipoDoc.getCodProg().equals("TP_DOC_DIR")){
            return "Nro. de " + tipoDoc.getNombre().toLowerCase();
        }else{
            return "Nombre de " + tipoDoc.getNombre().toLowerCase();
        }
    }
    
    public String obtenerDescFecha(){
        if(tipoDoc == null){
            return "Fecha de documento";
        }
        return "Fecha de " + tipoDoc.getNombre().toLowerCase();
    }
    
}
