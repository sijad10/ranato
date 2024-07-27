package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
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
@Named("tipoExplosivoController")
@SessionScoped

/**
 * Clase con TipoExplosivoController instanciada como tipoExplosivoController.
 * Contiene funciones utiles para la entidad TipoExplosivo. Esta vinculada a las
 * páginas TipoExplosivo/create.xhtml, TipoExplosivo/update.xhtml,
 * TipoExplosivo/list.xhtml, TipoExplosivo/view.xhtml Nota: Las tablas deben
 * tener la estructura de Sucamec para que funcione adecuadamente, revisar si
 * tiene el campo activo y modificar las búsquedas.
 */
public class TipoExplosivoController implements Serializable {
 private List<TipoExplosivoGt> listTipoExplosivos;

    public List<TipoExplosivoGt> getListTipoExplosivos() {
        return listTipoExplosivos;
    }

    public void setListTipoExplosivos(List<TipoExplosivoGt> listTipoExplosivos) {
        this.listTipoExplosivos = listTipoExplosivos;
    }
 
 

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    TipoExplosivoGt registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<TipoExplosivoGt> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<TipoExplosivoGt> registrosSeleccionados;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoExplosivoFacade tipoExplosivoFacade;

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
    public List<TipoExplosivoGt> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<TipoExplosivoGt> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public TipoExplosivoGt getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(TipoExplosivoGt registro) {
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
    public ListDataModel<TipoExplosivoGt> getResultados() {
        return resultados;
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<TipoExplosivoGt> getSelectItems() {
        return tipoExplosivoFacade.findAll();
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public TipoExplosivoGt getTipoExplosivo(Long id) {
        return tipoExplosivoFacade.find(id);
    }

    
     /**
     * lista tipo base cod prog
     *
     * @param tipo
     * @param nulo
     * @return
     */
    public List<TipoExplosivoGt> selectItemsTipo(String tipo, boolean nulo) {
        listTipoExplosivos = tipoExplosivoFacade.lstTipoExplosivo(tipo);
        return listTipoExplosivos;
    }
    
    /**
     * lista tipo base cod prog
     *
     * @param tipo
     * @param nulo
     * @return
     */
    public List<TipoExplosivoGt> selectItemsTipoLibro(String tipo, boolean nulo) {
        listTipoExplosivos = tipoExplosivoFacade.findByCodProg(tipo);
        return listTipoExplosivos;
    }
    
    /**
     * Constructor
     *
     */
    public TipoExplosivoController() {
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "tipoExplosivoConverter")
    public static class TipoExplosivoControllerConverterN extends TipoExplosivoControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = TipoExplosivoGt.class)
    public static class TipoExplosivoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoExplosivoController c = (TipoExplosivoController) JsfUtil.obtenerBean("tipoExplosivoController", TipoExplosivoController.class);
            return c.getTipoExplosivo(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TipoExplosivoGt) {
                TipoExplosivoGt o = (TipoExplosivoGt) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + TipoExplosivoGt.class.getName());
            }
        }
    }

    /**
     * Tipo Explosivo para los documentos adjuntos de la GT
     * @return 
     */
    public List<TipoExplosivoGt> getLstTipoDocGtExt() {
        return tipoExplosivoFacade.lstTipoDocGtExt();
    }
}
