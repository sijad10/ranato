package pe.gob.sucamec.sistemabase.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.jsf.util.EstadoCrud;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;

// Nombre de la instancia en la aplicacion //
@Named("sbPersonaController")
@SessionScoped

/**
 * Clase con SbPersonaController instanciada como sbPersonaController. Contiene
 * funciones utiles para la entidad SbPersona. Esta vinculada a las páginas
 * SbPersona/create.xhtml, SbPersona/update.xhtml, SbPersona/list.xhtml,
 * SbPersona/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class SbPersonaController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    SbPersona registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbPersona> resultados = null;

    //VARIABLES PARA LA AGREGAR PERSONAS AUTORIZADAS
    private SbPersona casaComercial = null;
    private SbPersona personaVinc = null;
    private List<SbRelacionPersonaGt> perVincLst = new ArrayList();
    private String filtroNroDoc = null;
    private List<SbPersona> personaList = new ArrayList();
    private String msjConfirmacionRegistroPersona;
    private SbPersona personaNew = new SbPersona();
    private String cantNumeros;
    private Boolean disabledBtVal;
    
    private Date fechaHoy = new Date();
    //

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbPersonaFacade sbPersonaFacade;
   
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
     * Evento tipo 'actionListener' para cambiar el estado a activo en un
     * registro de una lista.
     *
     */
    public void activar() {
        try {
            registro = (SbPersona) resultados.getRowData();
            registro.setActivo((short) 1);
            sbPersonaFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un
     * registro de una lista.
     *
     */
    public void anular() {
        try {
            registro = (SbPersona) resultados.getRowData();
            registro.setActivo((short) 0);
            sbPersonaFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public SbPersona getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbPersona registro) {
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
    public ListDataModel<SbPersona> getResultados() {
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
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<SbPersona> getSelectItems() {
        return sbPersonaFacade.findAll();
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
        //resultados = new ListDataModel(sbPersonaFacade.selectLike(filtro));
        resultados = new ListDataModel(sbPersonaFacade.findAll());
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (SbPersona) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        registro = (SbPersona) resultados.getRowData();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new SbPersona();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public void editar() {
        try {
            registro = (SbPersona) JsfUtil.entidadMayusculas(registro, "");
            sbPersonaFacade.edit(registro);
            estado = EstadoCrud.BUSCAR;
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
     /**
     * Función para campo almacén autocompletable
     *
     * @param per Propietario a buscar
     * @return Descripción de propietario (Tipo Doc : Nro. Documento)
     * @author Gino Chávez
     * @version 1.0
     */
    public String obtenerDescPersona(SbPersona per) {
        String nombre = "";

        if (per != null) {
            if (per.getTipoDoc() == null) {
                if (per.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    nombre = "RUC: " + per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre = "DNI: " + per.getNumDoc() + " - " + per.getApePat() + " " + per.getApeMat() + " " + per.getNombres();
                }
            } else {
                nombre = per.getTipoDoc().getNombre() + ": ";
                if (per.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")
                        || per.getTipoDoc().getCodProg().equals("TP_DOCID_EXT")) {
                    nombre += per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre += per.getNumDoc() + " - " + per.getApePat() + " " + (per.getApeMat() == null ? "" : per.getApeMat()) + " " + per.getNombres();
                }
            }
        }

        return nombre;
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     */
    public void crear() {
        try {
            registro = (SbPersona) JsfUtil.entidadMayusculas(registro, "");
            sbPersonaFacade.create(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado"));
            mostrarCrear();
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Constructor
     *
     */
    public SbPersonaController() {
        estado = EstadoCrud.BUSCAR;
    }

    public SbPersona getCasaComercial() {
        return casaComercial;
    }

    public void setCasaComercial(SbPersona casaComercial) {
        this.casaComercial = casaComercial;
    }

    public SbPersona getPersonaVinc() {
        return personaVinc;
    }

    public void setPersonaVinc(SbPersona personaVinc) {
        this.personaVinc = personaVinc;
    }

    public List<SbRelacionPersonaGt> getPerVincLst() {
        return perVincLst;
    }

    public void setPerVincLst(List<SbRelacionPersonaGt> perVincLst) {
        this.perVincLst = perVincLst;
    }

    public String getFiltroNroDoc() {
        return filtroNroDoc;
    }

    public void setFiltroNroDoc(String filtroNroDoc) {
        this.filtroNroDoc = filtroNroDoc;
    }

    public List<SbPersona> getPersonaList() {
        return personaList;
    }

    public void setPersonaList(List<SbPersona> personaList) {
        this.personaList = personaList;
    }

    public String getMsjConfirmacionRegistroPersona() {
        return msjConfirmacionRegistroPersona;
    }

    public void setMsjConfirmacionRegistroPersona(String msjConfirmacionRegistroPersona) {
        this.msjConfirmacionRegistroPersona = msjConfirmacionRegistroPersona;
    }

    public SbPersona getPersonaNew() {
        return personaNew;
    }

    public void setPersonaNew(SbPersona personaNew) {
        this.personaNew = personaNew;
    }

    public String getCantNumeros() {
        return cantNumeros;
    }

    public void setCantNumeros(String cantNumeros) {
        this.cantNumeros = cantNumeros;
    }

    public Boolean getDisabledBtVal() {
        return disabledBtVal;
    }

    public void setDisabledBtVal(Boolean disabledBtVal) {
        this.disabledBtVal = disabledBtVal;
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbPersona.class)
    public static class SbPersonaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            long id = Long.valueOf(value);
            return new SbPersona(id);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbPersona) {
                SbPersona o = (SbPersona) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbPersona.class.getName());
            }
        }
    }
}
