package pe.gob.sucamec.sel.gamac.jsf;

import pe.gob.sucamec.sel.gamac.data.GamacTipoBase;
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

// Nombre de la instancia en la aplicacion //
@Named("gamacTipoBaseController")
@SessionScoped

/**
 * Clase con TipoBaseController instanciada como tipoBaseController. Contiene
 * funciones utiles para la entidad TipoBase. Esta vinculada a las páginas
 * TipoBase/create.xhtml, TipoBase/update.xhtml, TipoBase/list.xhtml,
 * TipoBase/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class GamacTipoBaseController implements Serializable {


    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    GamacTipoBase registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<GamacTipoBase> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<GamacTipoBase> registrosSeleccionados;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacTipoBaseFacade gamacTipoBaseFacade;

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
    public List<GamacTipoBase> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<GamacTipoBase> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public GamacTipoBase getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(GamacTipoBase registro) {
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
    public ListDataModel<GamacTipoBase> getResultados() {
        return resultados;
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<GamacTipoBase> getSelectItems() {
        return gamacTipoBaseFacade.findAll();
    }


    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public GamacTipoBase getGamacTipoBase(Long id) {
        return gamacTipoBaseFacade.find(id);
    }
    
     /**
     * lista tipo base cod prog
     *
     * @param tipo
     * @param nulo
     * @return
     */
    public List<GamacTipoBase> selectItemsTipo(String tipo, boolean nulo) {
        return gamacTipoBaseFacade.lstTipoBase(tipo);
    }
    public List<GamacTipoBase> selectTipoBase(String tipo, boolean nulo) {
        String codProgT="";
        switch(tipo){
            case "TID":codProgT ="TP_DIRECB";
                 break;
            case "TVIA":codProgT ="TP_VIA";
                 break;
            case "TZNA":codProgT ="TP_ZON";
                 break;
                
            default:break;
        }
        return selectItemsTipo(codProgT, false);
    }
    
    /**
     * Constructor
     *
     */
    public GamacTipoBaseController() {
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "gamacTipoBaseConverter")
    public static class GamacTipoBaseControllerConverterN extends GamacTipoBaseControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = GamacTipoBase.class)
    public static class GamacTipoBaseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GamacTipoBaseController c = (GamacTipoBaseController) JsfUtil.obtenerBean("gamacTipoBaseController", GamacTipoBaseController.class);
            return c.getGamacTipoBase(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GamacTipoBase) {
                GamacTipoBase o = (GamacTipoBase) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + GamacTipoBase.class.getName());
            }
        }
    }
    
    public List<GamacTipoBase> getLstGradoPolicia() {
        return gamacTipoBaseFacade.lstTipoBase("TP_GRADOPNP");
    }
    
    public List<GamacTipoBase> getLstUnidadPolicia() {
        return gamacTipoBaseFacade.lstTipoBase("TP_UNIDADPNP");
    }
   
}
