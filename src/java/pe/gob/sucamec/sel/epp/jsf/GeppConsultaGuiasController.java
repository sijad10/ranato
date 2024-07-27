package pe.gob.sucamec.sel.epp.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.sel.epp.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.epp.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("geppConsultaGuiasController")
@SessionScoped

/**
 * Clase con ConsultaGuiasController instanciada como geppConsultaGuiasController.
 * Contiene funciones utiles para la entidad ConsultaGuias. Esta vinculada a las
 * páginas ConsultaGuias/create.xhtml, ConsultaGuias/update.xhtml,
 * ConsultaGuias/list.xhtml, ConsultaGuias/view.xhtml Nota: Las tablas deben
 * tener la estructura de Sucamec para que funcione adecuadamente, revisar si
 * tiene el campo activo y modificar las búsquedas.
 */
public class GeppConsultaGuiasController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, tipo;

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null;

    private StreamedContent archivoGuia;

    @Inject
    LoginController loginController;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.epp.beans.GeppConsultaGuiasFacade consultaGuiasFacade;

    public StreamedContent getArchivoGuia() {
        try {
            registro = (ArrayRecord) resultados.getRowData();
            archivoGuia = JsfUtil.obtenerArchivo("PathGuias", registro.get("PDF_ID")+".pdf", registro.get("NRO_GT")+".pdf", "application/pdf");
        } catch (Exception ex) {
            archivoGuia = JsfUtil.errorDescarga("Error al descargar el archivo", ex);
        }
        return archivoGuia;
    }

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        resultados = null;
        mostrarBuscar();
        return "/aplicacion/consultas/GeppConsultaGuias/List";
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
        String guia = null, expediente = null, rg = null;
        if (tipo.equals("guia")) {
            guia = filtro;
        }
        if (tipo.equals("expediente")) {
            expediente = filtro;
        }
        if (tipo.equals("rg")) {
            rg = filtro;
        }
        resultados = new ListDataModel(consultaGuiasFacade.listarGuias(guia, expediente, rg, loginController.getUsuario().getNumDoc()));
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
    public GeppConsultaGuiasController() {
        estado = EstadoCrud.BUSCAR;
    }

}
