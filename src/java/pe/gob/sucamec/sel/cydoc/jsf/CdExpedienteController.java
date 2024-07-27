package pe.gob.sucamec.sel.cydoc.jsf;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.disca.jsf.util.EstadoCrud;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;


// Nombre de la instancia en la aplicacion //
@Named("cdExpedienteController")
@SessionScoped

/**
 * Clase con CdExpedienteController instanciada como cdExpedienteController.
 * Contiene funciones utiles para la entidad CdExpediente.
 * Esta vinculada a las páginas CdExpediente/create.xhtml, CdExpediente/update.xhtml, CdExpediente/list.xhtml, CdExpediente/view.xhtml
 * Nota: Las tablas deben tener la estructura de Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y modificar las búsquedas.
 */
public class CdExpedienteController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER
     * agregar mas estados de acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;
    private String expediente;
    private String estadoExp = "R";
    private Date fechaInicio = new Date();
    private Date fechaFin = new Date();

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null;
    private ListDataModel<ArrayRecord> resultadosTraza = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.cydoc.beans.CdExpedienteFacade cdExpedienteFacade;

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

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        return "/aplicacion/consultas/CdExpedientes/List";
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
        resultados = new ListDataModel(cdExpedienteFacade.listarExpediente(loginController.getUsuario().getNumDoc(), this.getExpediente(), this.getFiltro(), this.getEstadoExp(), this.getFechaInicio(), this.getFechaFin()));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un registro.
     *
     */
    public void mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        BigDecimal idExpediente = (BigDecimal) registro.get(new DatabaseField("ID_EXPEDIENTE"));
        resultadosTraza = new ListDataModel(cdExpedienteFacade.listarTraza(idExpediente.longValue()));
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
    public CdExpedienteController() {
        estado = EstadoCrud.BUSCAR;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getEstadoExp() {
        return estadoExp;
    }

    public void setEstadoExp(String estadoExp) {
        this.estadoExp = estadoExp;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public ListDataModel<ArrayRecord> getResultadosTraza() {
        return resultadosTraza;
    }

    public void setResultadosTraza(ListDataModel<ArrayRecord> resultadosTraza) {
        this.resultadosTraza = resultadosTraza;
    }
    
}
