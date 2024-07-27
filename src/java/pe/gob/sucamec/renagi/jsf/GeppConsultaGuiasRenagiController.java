package pe.gob.sucamec.renagi.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.renagi.jsf.util.EstadoCrud;
import pe.gob.sucamec.sistemabase.seguridad.LogController;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("geppConsultaGuiasRenagiController")
@SessionScoped

/**
 * Clase con ConsultaGuiasRenagiController instanciada como
 * eppConsultaGuiasRenagiController. Contiene funciones utiles para la entidad
 * ConsultaGuias. Esta vinculada a las páginas ConsultaGuias/create.xhtml,
 * ConsultaGuias/update.xhtml, ConsultaGuias/list.xhtml,
 * ConsultaGuias/view.xhtml Nota: Las tablas deben tener la estructura de
 * Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class GeppConsultaGuiasRenagiController implements Serializable {

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
    ListDataModel<ArrayRecord> lstConductores = null;
    ListDataModel<ArrayRecord> lstVehiculos = null;
    ListDataModel<ArrayRecord> lstExplosivos = null;

    private StreamedContent archivoGuia;

    @Inject
    LoginController loginController;

    @Inject
    LogController logController;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.renagi.beans.GeppConsultaGuiasRenagiFacade consultaGuiasFacade;

    public ListDataModel<ArrayRecord> getLstExplosivos() {
        return lstExplosivos;
    }

    public ListDataModel<ArrayRecord> getLstConductores() {
        return lstConductores;
    }

    public ListDataModel<ArrayRecord> getLstVehiculos() {
        return lstVehiculos;
    }

    public String mostrarGuiaHash() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            registro = (ArrayRecord) resultados.getRowData();
            logController.escribirLogVer("" + registro.get("HASH_QR"), "BDINTEGRADO.REGISTRO");
            ec.redirect("https://www.sucamec.gob.pe/sel/faces/qr/resgepp.xhtml?h=" + registro.get("HASH_QR"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
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
        return "/aplicacion/consultasRenagi/GeppConsultaGuias/List";
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
        String guia = null, expediente = null, rg = null, ruc = null;
        if (tipo.equals("guia")) {
            guia = filtro;
        }
        if (tipo.equals("expediente")) {
            expediente = filtro;
        }
        if (tipo.equals("rg")) {
            rg = filtro;
        }
        if (tipo.equals("ruc")) {
            ruc = filtro;
        }
        logController.escribirLogBuscar("" + tipo + "\t" + filtro, "BDINTEGRADO.REGISTRO");
        resultados = new ListDataModel(
                consultaGuiasFacade.listarGuiasxFiltro(tipo, filtro)
        );
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
    public GeppConsultaGuiasRenagiController() {
        estado = EstadoCrud.BUSCAR;
    }

    //////////////////////////////////////////////////////////
    ///////// SOLICITUDES GUIA TRANSITO EXTERNA //////////////
    //////////////////////////////////////////////////////////
    public String direccionarSolicitud() {
        resultados = null;
        filtro = null;
        return "/aplicacion/consultasRenagi/GeppConsultaGuias/ReporteSolicitudes";
    }

    public void buscarSolicitudes() {
        resultados = new ListDataModel(consultaGuiasFacade.listarSolicitudesGuia(filtro, loginController.getUsuario().getPersona()));
    }

    public String verSolicitud() {
        try {
            registro = (ArrayRecord) resultados.getRowData();
            lstExplosivos = new ListDataModel(consultaGuiasFacade.listarSolicitudesExplosivos(Long.valueOf(registro.get("ID").toString())));
            lstConductores = new ListDataModel(consultaGuiasFacade.listarSolicitudesConduct(Long.valueOf(registro.get("ID").toString())));
            lstVehiculos = new ListDataModel(consultaGuiasFacade.listarSolicitudesVehiculo(Long.valueOf(registro.get("ID").toString())));
            return "/aplicacion/consultas/GeppConsultaGuias/ViewSolicitud";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

}
