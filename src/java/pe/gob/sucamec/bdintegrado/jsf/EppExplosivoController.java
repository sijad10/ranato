package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.EppExplosivo;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.bean.EppExplosivoFacade;

import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import java.util.List;
import javax.inject.Named;
import pe.gob.sucamec.bdintegrado.data.UnidadMedida;

// Nombre de la instancia en la aplicacion //
@Named("eppExplosivoController")
@SessionScoped

/**
 * Clase con EppExplosivoController instanciada como eppExplosivoController.
 * Contiene funciones utiles para la entidad EppExplosivo. Esta vinculada a las
 * páginas EppExplosivo/create.xhtml, EppExplosivo/update.xhtml,
 * EppExplosivo/list.xhtml, EppExplosivo/view.xhtml Nota: Las tablas deben tener
 * la estructura de Sucamec para que funcione adecuadamente, revisar si tiene el
 * campo activo y modificar las búsquedas.
 */
public class EppExplosivoController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    EppExplosivo registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<EppExplosivo> resultados = null;
    UnidadMedida unidadMedida;

    /**
     * Lista de areas para seleccion multiple
     */
    List<EppExplosivo> registrosSeleccionados;
    List<UnidadMedida> listuMedidas;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppExplosivoFacade eppExplosivoFacade;

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoExplosivoFacade tipoExplosivoFacade;

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public List<UnidadMedida> getListuMedidas() {
        return listuMedidas;
    }

    public void setListuMedidas(List<UnidadMedida> listuMedidas) {
        this.listuMedidas = listuMedidas;
    }

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
     * Propiedad de seleccion multiple
     *
     * @return
     */
    public List<EppExplosivo> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<EppExplosivo> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Anula multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void anularMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (EppExplosivo a : registrosSeleccionados) {
                a.setActivo((short) 0);
                eppExplosivoFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Activa multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void activarMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (EppExplosivo a : registrosSeleccionados) {
                a.setActivo((short) 1);
                eppExplosivoFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a activo en un
     * registro de una lista.
     *
     */
    public void activar() {
        try {
            registro = (EppExplosivo) resultados.getRowData();
            registro.setActivo((short) 1);
            eppExplosivoFacade.edit(registro);
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
            registro = (EppExplosivo) resultados.getRowData();
            registro.setActivo((short) 0);
            eppExplosivoFacade.edit(registro);
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
    public EppExplosivo getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(EppExplosivo registro) {
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
    public ListDataModel<EppExplosivo> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        return "List";
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<EppExplosivo> getSelectItems() {
        return eppExplosivoFacade.findAll();
    }



    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        resultados = new ListDataModel(eppExplosivoFacade.selectLike(filtro));
    }









    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public EppExplosivo getEppExplosivo(Long id) {
        return eppExplosivoFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public EppExplosivoController() {
        
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "eppExplosivoConverter")
    public static class EppExplosivoControllerConverterN extends EppExplosivoControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = EppExplosivo.class)
    public static class EppExplosivoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EppExplosivoController c = (EppExplosivoController) JsfUtil.obtenerBean("eppExplosivoController", EppExplosivoController.class);
            return c.getEppExplosivo(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EppExplosivo) {
                EppExplosivo o = (EppExplosivo) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + EppExplosivo.class.getName());
            }
        }
    }

    public String menuExplosivos() {
        
        return "/aplicacion/eppExplosivo/List";
    }
}
