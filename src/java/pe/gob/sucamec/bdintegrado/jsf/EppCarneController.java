package pe.gob.sucamec.bdintegrado.jsf;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import pe.gob.sucamec.bdintegrado.data.EppCarne;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import javax.imageio.ImageIO;
import pe.gob.sucamec.bdintegrado.data.SspRequisito;

// Nombre de la instancia en la aplicacion //
@Named("eppCarneController")
@SessionScoped

/**
 * Clase con EppCarneController instanciada como eppCarneController. Contiene
 * funciones utiles para la entidad EppCarne. Esta vinculada a las páginas
 * EppCarne/create.xhtml, EppCarne/update.xhtml, EppCarne/list.xhtml,
 * EppCarne/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class EppCarneController implements Serializable {

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
    EppRegistro registro;
    EppCarne carne = null;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<EppRegistro> resultados = null;
    ListDataModel<EppCarne> resultadosCarnes = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<EppRegistro> registrosSeleccionados;    
    private String suce;
    private String ubicacionGeo;
    private UploadedFile file;
    private StreamedContent foto;
    private byte[] fotoByte;    
    private String pathFoto;
    private boolean accionEjecutada = false;


    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppCarneFacade ejbEppCarneFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoExplosivoFacade ejbTipoExplosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;


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
    public List<EppRegistro> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<EppRegistro> a) {
        this.registrosSeleccionados = a;
    }

    public String getSuce() {
        return suce;
    }

    public void setSuce(String suce) {
        this.suce = suce;
    }

    public String getUbicacionGeo() {
        return ubicacionGeo;
    }

    public void setUbicacionGeo(String ubicacionGeo) {
        this.ubicacionGeo = ubicacionGeo;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getFoto() {

        try {
            if (registro.getEppCarne().getFotoId() != null) {
                if (registro.getEppCarne().getFotoId().getRutaFile() != null) {
                    fotoByte = FileUtils.readFileToByteArray(new File(pathFoto + registro.getEppCarne().getFotoId().getRutaFile()));
                    InputStream is = new ByteArrayInputStream(fotoByte);
                    foto = new DefaultStreamedContent(is, "image/jpeg", registro.getEppCarne().getFotoId().getRutaFile());
                }
            } else if (file != null) {
                fotoByte = IOUtils.toByteArray(file.getInputstream());
                foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                registro.getEppCarne().setFotoId(null);
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

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public ListDataModel<EppCarne> getResultadosCarnes() {
        return resultadosCarnes;
    }

    public void setResultadosCarnes(ListDataModel<EppCarne> resultadosCarnes) {
        this.resultadosCarnes = resultadosCarnes;
    }

    public EppCarne getCarne() {
        return carne;
    }

    public void setCarne(EppCarne carne) {
        this.carne = carne;
    }

    public boolean isAccionEjecutada() {
        return accionEjecutada;
    }

    public void setAccionEjecutada(boolean accionEjecutada) {
        this.accionEjecutada = accionEjecutada;
    }
    
    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public EppRegistro getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(EppRegistro registro) {
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
    public ListDataModel<EppRegistro> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @author Richar Fernández
     * @version 2.0
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        return "List";
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        if (resultados != null) {
            //  buscar();
        }
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @author Richar Fernández
     * @version 2.0
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public EppCarne getEppCarne(Long id) {
        return ejbEppCarneFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public EppCarneController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "eppCarneConverter")
    public static class EppCarneControllerConverterN extends EppCarneControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = EppCarne.class)
    public static class EppCarneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EppCarneController c = (EppCarneController) JsfUtil.obtenerBean("eppCarneController", EppCarneController.class);
            return c.getEppCarne(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EppCarne) {
                EppCarne o = (EppCarne) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + EppCarne.class.getName());
            }
        }
    }

    ///////////////////// BANDEJA /////////////////////
    public void buscarCarnesMovil(){
        if(!validacionBusquedaCarnesMovil()){
            return;
        }
        accionEjecutada = true;
        registro = null;
        carne = null;
        resultadosCarnes = null;
        List<EppRegistro> listado = ejbEppCarneFacade.buscarRegistrosCarnesMovil(filtro);
        if(listado != null && !listado.isEmpty()){
            registro = listado.get(0);
            if(registro.getEppRegistroList1() != null && !registro.getEppRegistroList1().isEmpty()){
                Collections.sort(registro.getEppRegistroList1(), new Comparator<EppRegistro>() {
                    @Override
                    public int compare(EppRegistro one, EppRegistro other) {
                        return other.getId().compareTo(one.getId());
                    }
                });
                registro = registro.getEppRegistroList1().get(0);
            }
            
            if(registro.getTipoOpeId().getCodProg().equals("TP_OPE_CAN")){
                if(registro.getRegistroId() != null){
                    carne = registro.getRegistroId().getEppCarne();
                }else{
                    registro = null;
                    carne = null;
                }
            }else{
                carne = registro.getEppCarne();
            }
        }
    }
    
    public boolean validacionBusquedaCarnesMovil(){
        if(filtro == null){
            JsfUtil.mensajeError("Por favor ingrese el documento a buscar");
            return false;
        }
        if(filtro.trim().isEmpty()){
            JsfUtil.mensajeError("Por favor ingrese el documento a buscar");
            return false;
        }
        return true;
    }
    
    public String openDlgVerCarne(EppCarne item){
        carne = item;
        return "pm:dlgVerCarne";
    }
        
    public StreamedContent generarPDFCarneFront(){
        try {
            if(carne == null){
                return null;
            }
            pathFoto = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
            return JsfUtil.generaPdfCMP_front(carne, false, pathFoto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public StreamedContent generarPDFCarneBack(){
        try {
            if(carne == null){
                return null;
            }
            pathFoto = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
            return JsfUtil.generaPdfCMP_back(carne, false, pathFoto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String obtenerNroCarne(){
        String descEstado = "";
        if(carne == null){
            return descEstado;
        }
        descEstado = ""+carne.getNroCarne();
        return descEstado;
    }
    
    public String obtenerVigenciaCarne(){
        String descEstado = "";
        if(carne == null){
            return descEstado;
        }
        if(!registro.getEstado().getCodProg().equals("TP_REGEV_FIN")){
            return carne.getRegistroId().getEstado().getNombre();
        }
        if(registro.getTipoOpeId().getCodProg().equals("TP_OPE_CAN") || registro.getTipoOpeId().getCodProg().equals("TP_OPE_COF")){
            descEstado = "CANCELADO";
        }else{
            if(JsfUtil.getFechaSinHora(new Date()).compareTo(carne.getFechaVencimiento()) > 0){
                descEstado = "VENCIDO";
            }else{
                descEstado = "VIGENTE";
            }
        }
        return descEstado;
    }
    
    public String obtenerEstiloCarneCancelado(){
        String descEstado = "";
        if(carne == null){
            return descEstado;
        }        
        if(registro.getTipoOpeId().getCodProg().equals("TP_OPE_CAN") || registro.getTipoOpeId().getCodProg().equals("TP_OPE_COF")){
            descEstado = "color: red;";
        }
        return descEstado;
    }
}
