package pe.gob.sucamec.bdintegrado.jsf;

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
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LogController;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("consultaGuiasPNPController")
@SessionScoped

/**
 * Clase con ConsultaGuiasController instanciada como
 * eppConsultaGuiasController. Contiene funciones utiles para la entidad
 * ConsultaGuias. Esta vinculada a las páginas ConsultaGuias/create.xhtml,
 * ConsultaGuias/update.xhtml, ConsultaGuias/list.xhtml,
 * ConsultaGuias/view.xhtml Nota: Las tablas deben tener la estructura de
 * Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class ConsultaGuiasPNPController implements Serializable {

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
    
    /***/
    private StreamedContent archivoGuia;
    
    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null;
    ListDataModel<ArrayRecord> lstConductores = null;
    ListDataModel<ArrayRecord> lstVehiculos = null;
    ListDataModel<ArrayRecord> lstExplosivos = null;

    @Inject
    LoginController loginController;

    @Inject
    LogController logController;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ConsultaGuiasPNPFacade consultaGuiasFacade;

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

    //TODO: Revisar si conviene que todos accedan a las GUIAS en PDF - Usar MostrarGuiaHash
    public StreamedContent getArchivoGuia() {
        try {
            registro = (ArrayRecord) resultados.getRowData();
            logController.escribirLogVer("" + registro.get("PDF_ID"), "BDINTEGRADO.REGISTRO");
            archivoGuia = JsfUtil.obtenerArchivo("PathGuias", registro.get("PDF_ID") + ".pdf", registro.get("NRO_GT") + ".pdf", "application/pdf");
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

    public ListDataModel<ArrayRecord> getLstConductores() {
        return lstConductores;
    }

    public void setLstConductores(ListDataModel<ArrayRecord> lstConductores) {
        this.lstConductores = lstConductores;
    }

    public ListDataModel<ArrayRecord> getLstVehiculos() {
        return lstVehiculos;
    }

    public void setLstVehiculos(ListDataModel<ArrayRecord> lstVehiculos) {
        this.lstVehiculos = lstVehiculos;
    }

    public ListDataModel<ArrayRecord> getLstExplosivos() {
        return lstExplosivos;
    }

    public void setLstExplosivos(ListDataModel<ArrayRecord> lstExplosivos) {
        this.lstExplosivos = lstExplosivos;
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
        if(esRolPNP()){
            resultados = null;
            mostrarBuscar();
            return "/aplicacion/gepp/consultaGuiasPNP/List";
        }else{
           JsfUtil.mensajeAdvertencia("Esta opción es permitida sólo para perfiles de la PNP"); 
        }
        return null;
    }

    ////// CONSULTA DE GUIAS //////
    
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
        resultados = new ListDataModel(consultaGuiasFacade.listarGuias(guia, expediente, rg, ruc));
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
    public ConsultaGuiasPNPController() {
        estado = EstadoCrud.BUSCAR;
    }

    //////////////////////////////////////////////////////////
    ///////// SOLICITUDES GUIA TRANSITO EXTERNA //////////////
    //////////////////////////////////////////////////////////
    
    public String direccionarSolicitud() {
        if(esRolPNP()){
            resultados = null;
            filtro = null;
            return "/aplicacion/gepp/consultaGuiasPNP/ReporteSolicitudes";
        }else{
           JsfUtil.mensajeAdvertencia("Esta opción es permitida sólo para perfiles de la PNP"); 
        }
        return null;
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
            return "/aplicacion/gepp/consultaGuiasPNP/ViewSolicitud";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public boolean esRolPNP(){
        return JsfUtil.buscarPerfilUsuario("SEL_PNPCON");
    }
    
    ////////////////////////////////////
}
