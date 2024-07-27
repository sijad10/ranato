package pe.gob.sucamec.sistemabase.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import pe.gob.sucamec.sistemabase.data.SbRelacionPersona;
import pe.gob.sucamec.sistemabase.jsf.util.EstadoCrud;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;

// Nombre de la instancia en la aplicacion //
@Named("sbRelacionPersonaController")
@SessionScoped

/**
 * Clase con SbRelacionPersonaController instanciada como sbRelacionPersonaController. Contiene funciones
 * utiles para la entidad SbRelacionPersona. Esta vinculada a las páginas SbRelacionPersona/create.xhtml,
 * SbRelacionPersona/update.xhtml, SbRelacionPersona/list.xhtml, SbRelacionPersona/view.xhtml Nota: Las tablas
 * deben tener la estructura de Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class SbRelacionPersonaController implements Serializable {

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
    SbRelacionPersona registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbRelacionPersona> resultados = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbRelacionPersonaFacade sbRelacionPersonaFacade;

    // Si la tabla no tiene el campo activo borrar DESDE ACA //
    /**
     * Devuelve si el registro actual se encuentra activo, se usa para borrado lógico.
     *
     * @return true si se encuenta activo, false si no.
     */
    public boolean isActivo() {
        return (registro.getActivo() == 1);
    }

    /**
     * Cambia el estado del registro a activo o inactivo, se usa para borrado lógico.
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
     * Evento tipo 'actionListener' para cambiar el estado a activo en un registro de una lista.
     *
     */
    public void activar() {
        try {
            registro = (SbRelacionPersona) resultados.getRowData();
            registro.setActivo((short) 1);
            sbRelacionPersonaFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un registro de una lista.
     *
     */
    public void anular() {
        try {
            registro = (SbRelacionPersona) resultados.getRowData();
            registro.setActivo((short) 0);
            sbRelacionPersonaFacade.edit(registro);
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
    public SbRelacionPersona getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbRelacionPersona registro) {
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
    public ListDataModel<SbRelacionPersona> getResultados() {
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
     * Devuelve una lista básica de items para un selectOneMenu, debe implementarse en cada controller de
     * acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<SbRelacionPersona> getSelectItems() {
        return sbRelacionPersonaFacade.findAll();
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
        //resultados = new ListDataModel(sbRelacionPersonaFacade.selectLike(filtro));
        resultados = new ListDataModel(sbRelacionPersonaFacade.findAll());
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un registro.
     *
     */
    public void mostrarVer() {
        registro = (SbRelacionPersona) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un registro
     */
    public void mostrarEditar() {
        registro = (SbRelacionPersona) resultados.getRowData();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new SbRelacionPersona();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public void editar() {
        try {
            registro = (SbRelacionPersona) JsfUtil.entidadMayusculas(registro, "");
            sbRelacionPersonaFacade.edit(registro);
            estado = EstadoCrud.BUSCAR;
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     */
    public void crear() {
        try {
            registro = (SbRelacionPersona) JsfUtil.entidadMayusculas(registro, "");
            sbRelacionPersonaFacade.create(registro);
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
    public SbRelacionPersonaController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbRelacionPersona.class)
    public static class SbRelacionPersonaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            long id = Long.valueOf(value);
            return new SbRelacionPersona(id);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbRelacionPersona) {
                SbRelacionPersona o = (SbRelacionPersona) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbRelacionPersona.class.getName());
            }
        }
    }
}
