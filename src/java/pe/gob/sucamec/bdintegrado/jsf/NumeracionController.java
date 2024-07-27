package pe.gob.sucamec.bdintegrado.jsf;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade;
import pe.gob.sucamec.bdintegrado.data.SbNumeracion;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;


@Named("numeracionController")
@SessionScoped
public class NumeracionController implements Serializable {


    private SbNumeracion current;
    private DataModel items = null;
    @EJB private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbFacade;
    private String filtro;
    private int selectedItemIndex;

    public boolean isActivo() {
        return (this.getSelected().getActivo() == 1) ? true : false;
    }

    public void setActivo(boolean activo) {
        if (activo) {
            this.getSelected().setActivo((short) 1);
        } else {
            this.getSelected().setActivo((short) 0);
        }
    }
    
    public String updateActivar() {
        try {
            current = (SbNumeracion)getItems().getRowData();
            selectedItemIndex = getItems().getRowIndex();
            current.setActivo((short) 1);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroActualizado"));
            buscar();
            recreateModel();
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public boolean verAnular(short estado) {
        if (estado == (short) 1) {
            return true;
        }
        return false;
    }

    public String updateAnular() {
        try {
            current = (SbNumeracion)getItems().getRowData();
            selectedItemIndex = getItems().getRowIndex();
            current.setActivo((short) 0);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroActualizado"));
            buscar();
            recreateModel();
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String buscar() {
        items = createDataModel(filtro);
        return "List";
    }

    public DataModel createDataModel(String filtro) {
        return new ListDataModel(getFacade().selectLike(filtro));
    }

    public NumeracionController() {
    }

    public SbNumeracion getSelected() {
        if (current == null) {
            current = new SbNumeracion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private SbNumeracionFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        filtro = "";
        return "/aplicacion/sistemaIntegrado/numeracion/List";
    }

    public String prepareView() {
        current = (SbNumeracion)getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new SbNumeracion();
        selectedItemIndex = -1;
        return "/aplicacion/sistemaIntegrado/numeracion/Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroCreado"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("ErrorDePersistencia"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (SbNumeracion)getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroActualizado"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("ErrorDePersistencia"));
            return null;
        }
    }

    public String destroy() {
        current = (SbNumeracion)getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        performDestroy();
        buscar();
        return "List";
    }

    public long nuevoNumero(long tipo) {
        long val = 0;
        List<SbNumeracion> n = getFacade().selectNumero(tipo);
        if (!n.isEmpty()) {
            recreateModel();
            current = n.get(0);//(SbNumeracion) getItems().getRowData();
            val = current.getValor() + 1L;
            System.err.println("VAL : " +  val);
            current.setValor(val);
            getFacade().edit(current);
        }
        return val;
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroBorrado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("ErrorDePersistencia"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count-1;
            // go to previous page if last page disappeared:
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex+1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = createDataModel(filtro);
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public SbNumeracion getSbNumeracion(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass=SbNumeracion.class)
    public static class NumeracionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            NumeracionController controller = (NumeracionController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "numeracionController");
            return controller.getSbNumeracion(getKey(value));
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
            if (object instanceof SbNumeracion) {
                SbNumeracion o = (SbNumeracion) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+SbNumeracion.class.getName());
            }
        }

    }

}
