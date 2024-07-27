package pe.gob.sucamec.sel.gamac.jsf;

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
import pe.gob.sucamec.sel.gamac.beans.GamacSbDireccionFacade;
import pe.gob.sucamec.sel.gamac.data.GamacSbDireccion;
import pe.gob.sucamec.sel.gamac.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.gamac.jsf.util.JsfUtil;

// Nombre de la instancia en la aplicacion //
@Named("gamacSbDireccionController")
@SessionScoped

/**
 * Clase con SbDireccionController instanciada como sbDireccionController. Contiene funciones utiles para la
 * entidad SbDireccion. Esta vinculada a las páginas SbDireccion/create.xhtml, SbDireccion/update.xhtml,
 * SbDireccion/list.xhtml, SbDireccion/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec para
 * que funcione adecuadamente, revisar si tiene el campo activo y modificar las búsquedas.
 */
public class GamacSbDireccionController implements Serializable {

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
    GamacSbDireccion registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<GamacSbDireccion> resultados = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de acuerdo a la implementación.
     */
    @EJB
    private GamacSbDireccionFacade gamacSbDireccionFacade;

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
            registro = (GamacSbDireccion) resultados.getRowData();
            registro.setActivo((short) 1);
            gamacSbDireccionFacade.edit(registro);
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
            registro = (GamacSbDireccion) resultados.getRowData();
            registro.setActivo((short) 0);
            gamacSbDireccionFacade.edit(registro);
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
    public GamacSbDireccion getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(GamacSbDireccion registro) {
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
    public ListDataModel<GamacSbDireccion> getResultados() {
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
    public List<GamacSbDireccion> getSelectItems() {
        return gamacSbDireccionFacade.findAll();
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
        //resultados = new ListDataModel(sbDireccionFacade.selectLike(filtro));
        resultados = new ListDataModel(gamacSbDireccionFacade.findAll());
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un registro.
     *
     */
    public void mostrarVer() {
        registro = (GamacSbDireccion) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un registro
     */
    public void mostrarEditar() {
        registro = (GamacSbDireccion) resultados.getRowData();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new GamacSbDireccion();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public void editar() {
        try {
            registro = (GamacSbDireccion) JsfUtil.entidadMayusculas(registro, "");
            gamacSbDireccionFacade.edit(registro);
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
            registro = (GamacSbDireccion) JsfUtil.entidadMayusculas(registro, "");
            gamacSbDireccionFacade.create(registro);
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
    public GamacSbDireccionController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public GamacSbDireccion getGamacSbDireccion(Long id) {
        return gamacSbDireccionFacade.find(id);
    }
    
    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = GamacSbDireccion.class)
    public static class GamacSbDireccionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            
            GamacSbDireccionController c = (GamacSbDireccionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gamacSbDireccionController");
            return c.getGamacSbDireccion(getKey(value));
            
//            SbDireccionGtController c = (SbDireccionGtController) JsfUtil.obtenerBean("sbDireccionGtController", SbDireccionGtController.class);
//            return c.getSbDireccion(Long.valueOf(value));
        }
        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GamacSbDireccion) {
                GamacSbDireccion o = (GamacSbDireccion) object;
                String r="";
                if(o.getId()!=null){
                    r=o.getId().toString();
                }
                return r;
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + GamacSbDireccion.class.getName());
            }
        }
    }
}
