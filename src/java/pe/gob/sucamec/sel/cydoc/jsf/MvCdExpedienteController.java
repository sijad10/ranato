package pe.gob.sucamec.sel.cydoc.jsf;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.disca.jsf.util.JsfUtil;

// Nombre de la instancia en la aplicacion //
@Named("mvCdExpedienteController")
@SessionScoped

/**
 * Clase con MvCdExpedienteController instanciada como mvCdExpedienteController.
 * Contiene funciones utiles para la entidad MvCdExpediente. Esta vinculada a
 * las páginas MvCdExpediente/create.xhtml, MvCdExpediente/update.xhtml,
 * MvCdExpediente/list.xhtml, MvCdExpediente/view.xhtml Nota: Las tablas deben
 * tener la estructura de Sucamec para que funcione adecuadamente, revisar si
 * tiene el campo activo y modificar las búsquedas.
 */
public class MvCdExpedienteController implements Serializable {

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, filtroAnho;

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null, detalle = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.cydoc.beans.CdExpedienteFacade mvCdExpedienteFacade;

    public String getFiltroAnho() {
        return filtroAnho;
    }

    public void setFiltroAnho(String filtroAnho) {
        this.filtroAnho = filtroAnho;
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

    public SelectItem[] getUltimosAnhos() {
        return JsfUtil.listaUltimosAnhos(4);
    }
    
    public ListDataModel<ArrayRecord> getDetalle() {
        return detalle;
    }

    public void setDetalle(ListDataModel<ArrayRecord> detalle) {
        this.detalle = detalle;
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
        return "List";
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public String mostrarBuscar() {
        //if (resultados != null) buscar();
        detalle = null;
        return "pm:buscarPage";
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        resultados = new ListDataModel(mvCdExpedienteFacade.listarExpedientePub(filtro, filtro, filtroAnho));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     * @return 
     */
    public String mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        detalle = new ListDataModel(mvCdExpedienteFacade.listarTrazaPub(((BigDecimal) registro.get("ID_EXPEDIENTE")).longValue()));
        return "pm:verPage";
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
    public MvCdExpedienteController() {
    }

}
