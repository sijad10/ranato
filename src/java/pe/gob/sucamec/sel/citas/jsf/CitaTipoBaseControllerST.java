package pe.gob.sucamec.sel.citas.jsf;

import pe.gob.sucamec.sel.citas.data.CitaTipoBase;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.citas.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.citas.bean.CitaTipoBaseFacade;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import java.util.List;

// Nombre de la instancia en la aplicacion //
@Named("citaTipoBaseControllerST")
@SessionScoped

/**
 * Clase con TipoBaseController instanciada como tipoBaseController. Contiene
 * funciones utiles para la entidad TipoBase. Esta vinculada a las páginas
 * TipoBase/create.xhtml, TipoBase/update.xhtml, TipoBase/list.xhtml,
 * TipoBase/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class CitaTipoBaseControllerST implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    CitaTipoBase registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<CitaTipoBase> resultados = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTipoBaseFacade ejbFacade;

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
            registro = (CitaTipoBase) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbFacade.edit(registro);
            JsfUtil.addSuccessMessage(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un
     * registro de una lista.
     *
     */
    public void anular() {
        try {
            registro = (CitaTipoBase) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbFacade.edit(registro);
            JsfUtil.addSuccessMessage(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public CitaTipoBase getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(CitaTipoBase registro) {
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
    public ListDataModel<CitaTipoBase> getResultados() {
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
    public List<CitaTipoBase> getSelectItems() {
        return ejbFacade.findAll();
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
//        resultados = new ListDataModel(ejbFacade.selectLike(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (CitaTipoBase) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        registro = (CitaTipoBase) resultados.getRowData();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new CitaTipoBase();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public void editar() {
        try {
            registro = (CitaTipoBase) JsfUtil.entidadMayusculas(registro, "");
            ejbFacade.edit(registro);
            estado = EstadoCrud.BUSCAR;
            JsfUtil.addSuccessMessage(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     */
    public void crear() {
        try {
            registro = (CitaTipoBase) JsfUtil.entidadMayusculas(registro, "");
            ejbFacade.create(registro);
            JsfUtil.addSuccessMessage(JsfUtil.bundle("MensajeRegistroCreado"));
            mostrarCrear();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public CitaTipoBase getTipoBase(Long id) {
        return ejbFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public CitaTipoBaseControllerST() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = CitaTipoBase.class)
    public static class TipoBaseControllerSTConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CitaTipoBaseControllerST c = (CitaTipoBaseControllerST) JsfUtil.obtenerBean("citaTipoBaseControllerST", CitaTipoBaseControllerST.class);
            return c.getTipoBase(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CitaTipoBase) {
                CitaTipoBase o = (CitaTipoBase) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + CitaTipoBase.class.getName());
            }
        }
    }

    public List<CitaTipoBase> getItemsTipoDocumento() {
        return ejbFacade.lstTipoDoc();
    }

    public List<CitaTipoBase> getItemsTipoBanco() {
        return ejbFacade.lstTipoBase("TP_BCO");
    }

}
