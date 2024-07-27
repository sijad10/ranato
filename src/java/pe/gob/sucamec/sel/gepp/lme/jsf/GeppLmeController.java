package pe.gob.sucamec.sel.gepp.lme.jsf;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.disca.jsf.util.EstadoCrud;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("geppLmeController")
@SessionScoped

/**
 * Clase con GeppLmeController instanciada como geppLmeController. Contiene
 * funciones utiles para la entidad GeppLme. Esta vinculada a las páginas
 * GeppLme/create.xhtml, GeppLme/update.xhtml, GeppLme/list.xhtml,
 * GeppLme/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec para
 * que funcione adecuadamente, revisar si tiene el campo activo y modificar las
 * búsquedas.
 */
public class GeppLmeController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
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
    ListDataModel<ArrayRecord> resultadosPub = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.gepp.lme.beans.GeppLmeFacade geppLmeFacade;

    @Inject
    LoginController loginController;

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

    public ListDataModel<ArrayRecord> getResultadosPub() {
        return resultadosPub;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        return "/aplicacion/consultas/GeppLme/List";
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
        resultados = new ListDataModel(geppLmeFacade.selectLike(loginController, tipo, filtro, false));
    }

    /**
     * RESULTADOS PUBLICOS
     */
    public void buscarPublica() throws Exception {
        resultadosPub = new ListDataModel(geppLmeFacade.selectLike(loginController, "numdoc", filtro, true));
        filtro = null;
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
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
    public GeppLmeController() {
        estado = EstadoCrud.BUSCAR;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String handleErrorWebPage() throws IOException{
        FacesContext fc = FacesContext.getCurrentInstance();
	
	// invalidate session
	ExternalContext ec = fc.getExternalContext();
        ec.redirect(ec.getRequestContextPath());
	return "";
    }

}
