package pe.gob.sucamec.renagi.jsf;

import pe.gob.sucamec.renagi.jsf.util.EstadoCrud;
import pe.gob.sucamec.renagi.jsf.util.JsfUtil;

import java.io.Serializable;
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
import javax.inject.Inject;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sistemabase.seguridad.LogController;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;


// Nombre de la instancia en la aplicacion //
@Named("geppLmeRenagiController")
@SessionScoped

/**
 * Clase con GeppLmeRenagiController instanciada como geppLmeRenagiController.
 * Contiene funciones utiles para la entidad GeppLme.
 * Esta vinculada a las páginas GeppLme/create.xhtml, GeppLme/update.xhtml, GeppLme/list.xhtml, GeppLme/view.xhtml
 * Nota: Las tablas deben tener la estructura de Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y modificar las búsquedas.
 */
public class GeppLmeRenagiController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER
     * agregar mas estados de acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;
    private String tipo;
    //private String expediente;
    //private String licencia;
    //private String documento;

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.renagi.beans.GeppLmeRenagiFacade geppLmeFacade;

    @Inject
    LoginController loginController;

    @Inject
    LogController logController;
    
    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public ArrayRecord getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(ArrayRecord registro) {
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
    public ListDataModel<ArrayRecord> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        return "/aplicacion/consultasRenagi/GeppLme/List";
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        //if (resultados != null) buscar();
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        logController.escribirLogBuscar("" + tipo + "\t" + filtro, "BDINTEGRADO.LICENCIA");
        resultados = new ListDataModel(geppLmeFacade.selectLike(loginController, tipo, filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un registro.
     *
     */
    public void mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        logController.escribirLogVer("" + registro.get("NRO_LICENCIA"), "BDINTEGRADO.LICENCIA");
        estado = EstadoCrud.VER;
    }

	/**
     * Obtiene el valor de un campo del hashmap, por algun motivo (bug) el xhtml
     * no permite esto directamente.
     *
     * @param r El resultado del native query como Map.
     * @param c El campo que se desea obtener.
     * @return El valor del campo como Object
     */
    public Object valorCampo(ArrayRecord r, String c) {
        return r.get(c);
    }

    /**
     * Constructor
     *
     */
    public GeppLmeRenagiController() {
        estado = EstadoCrud.BUSCAR;
    }

    /*
    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }
    */

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
