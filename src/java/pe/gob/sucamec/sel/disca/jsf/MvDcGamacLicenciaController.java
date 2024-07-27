package pe.gob.sucamec.sel.disca.jsf;


import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.sel.disca.jsf.util.JsfUtil;


// Nombre de la instancia en la aplicacion //
@Named("mvDcGamacLienciaController")
@SessionScoped

/**
 * Clase con MvDcGamacLienciaController instanciada como mvDcGamacLienciaController.
 * Contiene funciones utiles para la entidad MvDcGsspEmpresas.
 * Esta vinculada a las páginas MvDcGsspEmpresas/create.xhtml, MvDcGsspEmpresas/update.xhtml, MvDcGsspEmpresas/list.xhtml, MvDcGsspEmpresas/view.xhtml
 * Nota: Las tablas deben tener la estructura de Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y modificar las búsquedas.
 */
public class MvDcGamacLicenciaController implements Serializable {

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null;
    //
    private String estado = "";
    private String msjeEstado = "";
    private SbParametro paramOriDatArma;
    private String desdeMigra;
    private Map itemSelected;
    private boolean accionEjecutada = false;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.disca.beans.MvDcGamacLicenciaFacade mvDcGamacLicenciaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppCarneFacade ejbEppCarneFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;    
    @EJB
    private SbUsuarioFacadeGt ejbSbUsuarioFacadeGt ;
    
    @PostConstruct
    public void inicializar() {
        // Parámetro origen data ARMAS
        setParamOriDatArma(ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_ORIGDAT_ARMDIS", 5L));
        if (paramOriDatArma != null) {
            setDesdeMigra(paramOriDatArma.getValor());
        } else {
            setDesdeMigra("DISCA");
        }
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMsjeEstado() {
        return msjeEstado;
    }

    public void setMsjeEstado(String msjeEstado) {
        this.msjeEstado = msjeEstado;
    }

    public SbParametro getParamOriDatArma() {
        return paramOriDatArma;
    }

    public void setParamOriDatArma(SbParametro paramOriDatArma) {
        this.paramOriDatArma = paramOriDatArma;
    }

    public String getDesdeMigra() {
        return desdeMigra;
    }

    public void setDesdeMigra(String desdeMigra) {
        this.desdeMigra = desdeMigra;
    }

    public Map getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(Map itemSelected) {
        this.itemSelected = itemSelected;
    }

    public boolean isAccionEjecutada() {
        return accionEjecutada;
    }

    public void setAccionEjecutada(boolean accionEjecutada) {
        this.accionEjecutada = accionEjecutada;
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
        if (resultados != null) buscar();
        return "pm:buscarPage";
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        switch (desdeMigra){
            case "DISCA":
                resultados = new ListDataModel(mvDcGamacLicenciaFacade.listarLicenciasDisca(filtro));
                break;

            case "MIGRA":
                resultados = new ListDataModel(mvDcGamacLicenciaFacade.listarLicenciasDiscaMigra(filtro));             
                // ejbAmaMaestroArmasFacade.estadoLicenciaDisca(Long.valueOf(armaLic.get("ID_DISCA").toString()));
                break;
        }        
        filtro = "";        
        if(resultados.getRowCount() == 0){
            JsfUtil.mensajeAdvertencia("No se encontraron resultados");
        }
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un registro.
     *
     */
    public String mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
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
    public MvDcGamacLicenciaController() {
        resultados = null;
        filtro = null;
    }
    
    public String limpiar() {
        resultados = null;
        filtro = null;
        return null;
    }


    public String obtenerEstadoLicencia(){
        estado = "";
        msjeEstado = "";
        String tipoPropietario = "";
        boolean mostrarMsje = false;
        int cont = 0;
        if(resultados != null && resultados.getRowCount() > 0){
            for(Map nmap : resultados){
                if(nmap.get("SISTEMA").toString().equals("DISCA")){
                    mostrarMsje = true;
                }
            }
            for(Map nmap : resultados){
                switch (desdeMigra){
                    case "DISCA":
                        estado = nmap.get("ESTADO").toString();
                        break;

                    case "MIGRA":
                        estado = mvDcGamacLicenciaFacade.obtenerEstadoLicDisca(Long.parseLong(nmap.get("ID_MIGRA_DISCA_FOX").toString()));
                        break;
                }
                if(cont == 0){
                    tipoPropietario = nmap.get("TIPO_PROPIETARIO").toString();
                }
                cont++;
                if(estado.equals("VIGENTE")){                    
                    break;
                }
            }
        }
        if(mostrarMsje)
            obtenerMensajeEstado(tipoPropietario);
        return estado;
    }
    
    public void obtenerMensajeEstado(String tipoPropietario){
        switch(estado){
            case "VIGENTE":                
                    msjeEstado = JsfUtil.bundle("MvDcGamacLicenciasVigente");
                    break;
            case "CANCELADO":
            case "CANCELADA":
                    estado = "CANCELADA";
                    msjeEstado = JsfUtil.bundle("MvDcGamacLicenciasCancelada");
                    break;
            case "VENCIDO":
            case "VENCIDA":
                    estado = "VENCIDA";
                    if(tipoPropietario.toUpperCase().contains("JURIDICA")){
                        msjeEstado = JsfUtil.bundle("MvDcGamacLicenciasVencidaJuridica");
                    }else{
                        msjeEstado = JsfUtil.bundle("MvDcGamacLicenciasVencida");
                    }
                        
                    break;
        }
    }
    
    public void buscarLicenciasMovil(){
        if(!validacionBusquedaCarnesMovil()){
            return;
        }
        accionEjecutada = true;
        itemSelected = mvDcGamacLicenciaFacade.listarLicenciasMovil(filtro);
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
    
    public StreamedContent generarPDFCarneFront(){
        try {
            if(itemSelected == null){
                return null;
            }
            String pathFoto = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
            SbUsuarioGt usuarioGerenteGssp = ejbSbUsuarioFacadeGt.obtenerUsuarioxPerfil("AMA_GERCOM"); 
            return JsfUtil.generaPdfLicenciaGamac(itemSelected, pathFoto, usuarioGerenteGssp.getNombresCompletosUsuario(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public StreamedContent generarPDFCarneBack(){
        try {
            if(itemSelected == null){
                return null;
            }
            String pathFoto = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
            SbUsuarioGt usuarioGerenteGssp = ejbSbUsuarioFacadeGt.obtenerUsuarioxPerfil("AMA_GERCOM"); 
            return JsfUtil.generaPdfLicenciaGamac(itemSelected, pathFoto, usuarioGerenteGssp.getLogin(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
