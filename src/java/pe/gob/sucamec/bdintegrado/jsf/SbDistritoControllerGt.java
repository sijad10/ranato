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
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;

// Nombre de la instancia en la aplicacion //
@Named("sbDistritoControllerGt")
@SessionScoped

/**
 * Clase con SbDistritoController instanciada como sbDistritoController.
 * Contiene funciones utiles para la entidad SbDistritoGt. Esta vinculada a las
 * páginas SbDistritoGt/create.xhtml, SbDistritoGt/update.xhtml,
 * SbDistritoGt/list.xhtml, SbDistritoGt/view.xhtml Nota: Las tablas deben tener la
 * estructura de Sucamec para que funcione adecuadamente, revisar si tiene el
 * campo activo y modificar las búsquedas.
 */
public class SbDistritoControllerGt implements Serializable {


    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    SbDistritoGt registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbDistritoGt> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SbDistritoGt> registrosSeleccionados;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt sbDistritoFacade;

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
    public List<SbDistritoGt> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SbDistritoGt> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public SbDistritoGt getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbDistritoGt registro) {
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
    public ListDataModel<SbDistritoGt> getResultados() {
        return resultados;
    }


    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<SbDistritoGt> getSelectItems() {
        return sbDistritoFacade.findAll();
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SbDistritoGt getSbDistrito(Long id) {
        return sbDistritoFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SbDistritoControllerGt() {
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbDistritoConverter")
    public static class SbDistritoControllerConverterN extends SbDistritoControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbDistritoGt.class)
    public static class SbDistritoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbDistritoControllerGt c = (SbDistritoControllerGt) JsfUtil.obtenerBean("sbDistritoControllerGt", SbDistritoControllerGt.class);
            return c.getSbDistrito(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbDistritoGt) {
                SbDistritoGt o = (SbDistritoGt) object;
                String r="";
                if(o.getId()!=null){
                    r=o.getId().toString();
                }
                return r;
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbDistritoGt.class.getName());
            }
        }
    }
}
