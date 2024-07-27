package pe.gob.sucamec.sel.citas.jsf;

import pe.gob.sucamec.sel.citas.data.CitaTipoGamac;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.citas.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.citas.bean.CitaTipoGamacFacade;

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
@Named("citaTipoGamacController")
@SessionScoped

/**
 * Clase con TipoGamacController instanciada como tipoGamacController. Contiene
 * funciones utiles para la entidad TipoGamac. Esta vinculada a las páginas
 * TipoGamac/create.xhtml, TipoGamac/update.xhtml, TipoGamac/list.xhtml,
 * TipoGamac/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class CitaTipoGamacController implements Serializable {

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
    CitaTipoGamac registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<CitaTipoGamac> resultados = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTipoGamacFacade ejbFacade;

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
            registro = (CitaTipoGamac) resultados.getRowData();
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
            registro = (CitaTipoGamac) resultados.getRowData();
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
    public CitaTipoGamac getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(CitaTipoGamac registro) {
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
    public ListDataModel<CitaTipoGamac> getResultados() {
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
    public List<CitaTipoGamac> getSelectItems() {
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
        registro = (CitaTipoGamac) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        registro = (CitaTipoGamac) resultados.getRowData();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new CitaTipoGamac();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public void editar() {
        try {
            registro = (CitaTipoGamac) JsfUtil.entidadMayusculas(registro, "");
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
            registro = (CitaTipoGamac) JsfUtil.entidadMayusculas(registro, "");
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
    public CitaTipoGamac getTipoGamac(Long id) {
        return ejbFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public CitaTipoGamacController() {
        estado = EstadoCrud.BUSCAR;
    }
    
    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "citaTipoGamacConverter")
    public static class CitaTipoGamacControllerConvertern extends CitaTipoGamacControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = CitaTipoGamac.class)
    public static class CitaTipoGamacControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CitaTipoGamacController c = (CitaTipoGamacController) JsfUtil.obtenerBean("citaTipoGamacController", CitaTipoGamacController.class);
            return c.getTipoGamac(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CitaTipoGamac) {
                CitaTipoGamac o = (CitaTipoGamac) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + CitaTipoGamac.class.getName());
            }
        }
    }

    public List<CitaTipoGamac> getItemsTipoTramite() {
        return ejbFacade.lstTipoTraPol();
    }

    public List<CitaTipoGamac> getItemsTipoTramite20() {
        return ejbFacade.lstTipoTraPol20();
    }
    
    public List<CitaTipoGamac> getItemsTipoMultimodal() {
        return ejbFacade.lstTipoTraMulti();
    }

    public List<CitaTipoGamac> itemsTipoArma(CitaTipoGamac tipoTramite) {
//        tipoTramite = ejbFacade.buscarTipoGamacXId(tipoTramite.getId());
//        if (tipoTramite != null) {
//            if (tipoTramite.getCodProg().equals("TP_TRA_DEF")) {
//                return ejbFacade.lstDefPerTipoArma();
//            }
//        }
        return ejbFacade.lstTipoGamac("TP_ARM");
    }

    public List<CitaTipoGamac> getItemsTipoArma() {
        return ejbFacade.lstTipoGamac("TP_ARM");
    }

    public List<CitaTipoGamac> getItemsTipoResultado() {
        return ejbFacade.lstTipoGamac("TP_RES");
    }

    public List<CitaTipoGamac> getItemsTipoCalibre(CitaTipoGamac tipoArma) {
        return ejbFacade.lstTipoGamac(tipoArma.getCodProg());
    }

    public List<CitaTipoGamac> getItemsTipoOperacionPol() {
        return ejbFacade.lstTipoOpePol();
    }

    public List<CitaTipoGamac> getItemsTipoOperacionPolRen() {
        return ejbFacade.lstTipoOpePolRen();
    }
    
    public String nombreTipoGamac(CitaTipoGamac tg) {
        if (tg == null || tg.getId() == null) {
            return "";
        }
        return "" + ejbFacade.buscarTipoGamacXId(tg.getId()).getNombre();
    }
    
}
