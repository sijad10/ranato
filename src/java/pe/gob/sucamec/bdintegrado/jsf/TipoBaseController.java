package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.sistemabase.data.TipoBase;
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
import javax.faces.model.SelectItem;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
import pe.gob.sucamec.sistemabase.data.SbTipo;

// Nombre de la instancia en la aplicacion //
@Named("tipoBaseController")
@SessionScoped

/**
 * Clase con TipoBaseController instanciada como tipoBaseController. Contiene
 * funciones utiles para la entidad TipoBase. Esta vinculada a las páginas
 * TipoBase/create.xhtml, TipoBase/update.xhtml, TipoBase/list.xhtml,
 * TipoBase/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class TipoBaseController implements Serializable {

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    TipoBaseGt registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<TipoBaseGt> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<TipoBaseGt> registrosSeleccionados;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade tipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt tipoBaseFacadeGt;
    @EJB
    private SbTipoFacade ejbSbTipoFacade;

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
    public List<TipoBaseGt> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<TipoBaseGt> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public TipoBaseGt getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(TipoBaseGt registro) {
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
    public ListDataModel<TipoBaseGt> getResultados() {
        return resultados;
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<TipoBaseGt> getSelectItems() {
        return tipoBaseFacade.findAll();
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public TipoBaseGt getTipoBase(Long id) {
        return tipoBaseFacade.find(id);
    }

    /**
     * lista tipo base cod prog
     *
     * @param tipo
     * @param nulo
     * @return
     */
    public List<TipoBaseGt> selectItemsTipo(String tipo, boolean nulo) {
        return tipoBaseFacade.lstTipoBase(tipo);
    }

    public List<TipoBaseGt> selectTipoBase(String tipo, boolean nulo) {
        String codProgT = "";
        switch (tipo) {
            case "TID":
                codProgT = "TP_DIRECB";
                break;
            case "TVIA":
                codProgT = "TP_VIA";
                break;
            case "TZNA":
                codProgT = "TP_ZON";
                break;

            default:
                break;
        }
        return selectItemsTipo(codProgT, false);
    }

    public List<SbTipo> selectSbTipo(String tipo, boolean nulo) {
        String codProgT = "";
        switch (tipo) {
            case "TID":
                codProgT = "TP_DIRECB";
                break;
            case "TVIA":
                codProgT = "TP_VIA";
                break;
            case "TZNA":
                codProgT = "TP_ZON";
                break;
            default:
                break;
        }
        return ejbSbTipoFacade.selectItemsTipo(codProgT);
    }

    /**
     * Constructor
     *
     */
    public TipoBaseController() {
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "tipoBaseConverter")
    public static class TipoBaseControllerConverterN extends TipoBaseControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = TipoBaseGt.class)
    public static class TipoBaseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoBaseController c = (TipoBaseController) JsfUtil.obtenerBean("tipoBaseController", TipoBaseController.class);
            return c.getTipoBase(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TipoBase) {
                TipoBase tb = (TipoBase) object;
                return tb.getId().toString();
            }if (object instanceof TipoBaseGt) {
                TipoBaseGt o = (TipoBaseGt) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + TipoBaseGt.class.getName());
            }
        }
    }

    public List<TipoBaseGt> getLstGradoPolicia() {
        return tipoBaseFacade.lstTipoBase("TP_GRADOPNP");
    }

    public List<TipoBaseGt> getLstUnidadPolicia() {
        return tipoBaseFacade.lstTipoBase("TP_UNIDADPNP");
    }
    
    /**
     * @return the listGenero
     */
    public List<TipoBaseGt> getListGenero() {
        return tipoBaseFacade.lstTipoBase("TP_GEN");
    }

    /**
     * @return the listOcupacion
     */
    public List<TipoBaseGt> getListOcupacion() {
        return tipoBaseFacade.lstTipoBase("TP_OCUPA");
    }

    /**
     * @return the listEstCivil
     */
    public List<TipoBaseGt> getListEstCivil() {
        return tipoBaseFacade.lstTipoBase("TP_ECIV");
    }
    
    public TipoBaseGt tipoPorCodProg(String codProg) {
        return tipoBaseFacadeGt.tipoPorCodProg(codProg);
    }
    
    public SelectItem[] selectItemsTipoAbreviatura(String tipo, boolean nulo) {
        return pe.gob.sucamec.sistemabase.jsf.util.JsfUtil.getSelectItems(tipoBaseFacadeGt.selectItemsTipo(tipo), nulo, "abreviatura");
    }
    
    public SelectItem[] selectItemsAreas(boolean nulo) {
        return pe.gob.sucamec.sistemabase.jsf.util.JsfUtil.getSelectItems(tipoBaseFacadeGt.selectItemsTipo("TP_AREA"), nulo, "abreviatura", "id");
    }

}
