package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

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
import pe.gob.sucamec.bdintegrado.data.SbDepartamentoGt;

// Nombre de la instancia en la aplicacion //
@Named("sbDepartamentoControllerGt")
@SessionScoped

/**
 * Clase con SbDepartamentoController instanciada como sbDepartamentoController.
 * Contiene funciones utiles para la entidad SbDepartamentoGt. Esta vinculada a
 * las páginas SbDepartamentoGt/create.xhtml, SbDepartamentoGt/update.xhtml,
 * SbDepartamentoGt/list.xhtml, SbDepartamentoGt/view.xhtml Nota: Las tablas deben
 * tener la estructura de Sucamec para que funcione adecuadamente, revisar si
 * tiene el campo activo y modificar las búsquedas.
 */
public class SbDepartamentoControllerGt implements Serializable {

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    SbDepartamentoGt registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbDepartamentoGt> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SbDepartamentoGt> registrosSeleccionados;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDepartamentoFacadeGt sbDepartamentoFacade;

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
    public List<SbDepartamentoGt> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SbDepartamentoGt> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public SbDepartamentoGt getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbDepartamentoGt registro) {
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
    public ListDataModel<SbDepartamentoGt> getResultados() {
        return resultados;
    }


    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<SbDepartamentoGt> getSelectItems() {
        return sbDepartamentoFacade.findAll();
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SbDepartamentoGt getSbDepartamento(Long id) {
        return sbDepartamentoFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SbDepartamentoControllerGt() {
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbDepartamentoConverter")
    public static class SbDepartamentoControllerConverterN extends SbDepartamentoControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbDepartamentoGt.class)
    public static class SbDepartamentoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbDepartamentoControllerGt c = (SbDepartamentoControllerGt) JsfUtil.obtenerBean("sbDepartamentoControllerGt", SbDepartamentoControllerGt.class);
            return c.getSbDepartamento(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbDepartamentoGt) {
                SbDepartamentoGt o = (SbDepartamentoGt) object;
                String r="";
                if(o.getId()!=null){
                    r=o.getId().toString();
                }
                return r;
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbDepartamentoGt.class.getName());
            }
        }
    }
}
