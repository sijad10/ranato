package pe.gob.sucamec.notificacion.jsf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.notificacion.data.NeArchivo;
import pe.gob.sucamec.notificacion.data.NeDocumento;
import pe.gob.sucamec.notificacion.data.NeEvento;
import pe.gob.sucamec.notificacion.jsf.util.EstadoCrud;
import pe.gob.sucamec.notificacion.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("neDocumentoController")
@SessionScoped

/**
 * Clase con NeDocumentoController instanciada como neDocumentoController.
 * Contiene funciones utiles para la entidad NeDocumento. Esta vinculada a las
 * páginas NeDocumento/create.xhtml, NeDocumento/update.xhtml,
 * NeDocumento/list.xhtml, NeDocumento/view.xhtml Nota: Las tablas deben tener
 * la estructura de Sucamec para que funcione adecuadamente, revisar si tiene el
 * campo activo y modificar las búsquedas.
 */
public class NeDocumentoController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, filtroAnho;

    /**
     * Registro actual para editar o ver.
     */
    NeDocumento registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<NeDocumento> resultados = null;

    private String labelExpediente;
    private Boolean existeNroExp;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.notificacion.beans.NeDocumentoFacade neDocumentoFacade;
    @EJB
    private SbTipoFacade tipoFacade;
    @Inject
    private LoginController loginController;

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

    public String getFiltroAnho() {
        return filtroAnho;
    }

    public void setFiltroAnho(String filtroAnho) {
        this.filtroAnho = filtroAnho;
    }

    public SelectItem[] getUltimosAnhos() {
        final int max = 5;
        SelectItem[] si = new SelectItem[max];
        int anho = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < max; i++) {
            si[i] = new SelectItem("" + (anho - i), "" + (anho - i));
        }
        return si;
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public NeDocumento getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(NeDocumento registro) {
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
    public ListDataModel<NeDocumento> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        return "/aplicacion/notificacion/Documentos/List";
    }

    public StreamedContent obtenerArchivo() {
        NeArchivo a = neDocumentoFacade.archivoFirmado(registro);
        if (a == null) {
            return null;
        }
        StreamedContent r = null;
        try {
            String path = JsfUtil.bundle("PathUpload");
            FileInputStream f = new FileInputStream(path + a.getNombre());
            r = new DefaultStreamedContent(f, "application/pdf", a.getNombre());
        } catch (FileNotFoundException ex) {
            r = JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<NeDocumento> getSelectItems() {
        return neDocumentoFacade.findAll();
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        buscar();
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        resultados = new ListDataModel(neDocumentoFacade.selectLike(filtro, filtroAnho, loginController.getUsuario().getPersonaId()));
    }

    public String obtenerNumeroExpediente(NeDocumento documento) {
        String numero = documento.getNroExp() + " - " + documento.getAnoExp() + " - " + documento.getSecExp();
        if (existeNumeroExpediente(documento)) {
            numero = documento.getNroExpediente();
        }
        return numero;
    }

    public Boolean existeNumeroExpediente(NeDocumento documento) {
        Boolean resultado = Boolean.FALSE;
        if (documento.getNroExpediente() != null && !documento.getNroExpediente().isEmpty()) {
            resultado = Boolean.TRUE;
        }
        return resultado;
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (NeDocumento) resultados.getRowData();
        existeNroExp = existeNumeroExpediente(registro);
        if (existeNroExp) {
            setLabelExpediente(JsfUtil.bundle("Expediente"));
        } else {
            setLabelExpediente(JsfUtil.bundle("ExpDisca"));
        }
        if (registro.getEstadoId().getCodProg().equals("TP_EV_ENV")) {
            SbTipo estCre = tipoFacade.tipoPorCodProg("TP_EST_CRE"),
                    evLei = tipoFacade.tipoPorCodProg("TP_EV_LEI");
            registro.setEstadoId(evLei);
            List<NeEvento> docEventos = registro.getNeEventoList();
            docEventos.add(new NeEvento(new Date(), (short) 1, registro,
                    estCre, evLei, new SbUsuario(loginController.getUsuario().getId())));
            registro.setNeEventoList(docEventos);
            neDocumentoFacade.edit(registro);
        }
        estado = EstadoCrud.VER;
    }

    public String imagenEstado(String codProg) {
        if (codProg.equals("TP_EV_ENV")) {
            return "/resources/imagenes/enviado.png";
        }
        if (codProg.equals("TP_EV_LEI")) {
            return "/resources/imagenes/leido.png";
        }
        return "/resources/imagenes/ninguno.png";
    }

    /**
     * Constructor
     *
     */
    public NeDocumentoController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * @return the labelExpediente
     */
    public String getLabelExpediente() {
        return labelExpediente;
    }

    /**
     * @param labelExpediente the labelExpediente to set
     */
    public void setLabelExpediente(String labelExpediente) {
        this.labelExpediente = labelExpediente;
    }

    /**
     * @return the existeNroExp
     */
    public Boolean getExisteNroExp() {
        return existeNroExp;
    }

    /**
     * @param existeNroExp the existeNroExp to set
     */
    public void setExisteNroExp(Boolean existeNroExp) {
        this.existeNroExp = existeNroExp;
    }

    public Date fechaNotificacion(NeDocumento reg){
        Date fecha = null;
        if (reg!=null) {
            for (NeEvento even : reg.getNeEventoList()) {
                if (even.getActivo()==1) {
                    if (even.getEstadoId().getCodProg().equals("TP_EV_ENV")) {
                        fecha = even.getFecha();
                    }
                }
            }
            
        }
        return fecha;
    }
    
    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = NeDocumento.class)
    public static class NeDocumentoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            long id = Long.valueOf(value);
            return new NeDocumento(id);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof NeDocumento) {
                NeDocumento o = (NeDocumento) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + NeDocumento.class.getName());
            }
        }
    }
    
    public boolean renderBtnDescargarArchivo(){
        if(registro.getNeArchivoList() != null && !registro.getNeArchivoList().isEmpty()){
            return true;
        }
        return false;
    }
}
