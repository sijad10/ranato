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
import pe.gob.sucamec.bdintegrado.bean.SiTipoFacade;
import pe.gob.sucamec.bdintegrado.data.SiTipo;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

@Named("tipoIntegradoController")
@SessionScoped
public class TipoIntegradoController implements Serializable {

    private SiTipo current;
    private DataModel items = null;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SiTipoFacade ejbFacade;
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
            current = (SiTipo) getItems().getRowData();
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
            current = (SiTipo) getItems().getRowData();
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

    public TipoIntegradoController() {
    }

    public SiTipo getSelected() {
        if (current == null) {
            current = new SiTipo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private SiTipoFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        filtro = "";
        return "/aplicacion/sistemaIntegrado/tipoIntegrado/List";
    }

    public String prepareView() {
        current = (SiTipo) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new SiTipo();
        selectedItemIndex = -1;
        return "Create";
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
        current = (SiTipo) getItems().getRowData();
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
        current = (SiTipo) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        performDestroy();
        buscar();
        return "List";
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
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
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

    public SelectItem[] itemsTipoTkEstView() {
        return JsfUtil.getSelectItems(getTipoIntegradoTkEst(), false);
    }

    public SelectItem[] itemsTipoPorTipo(String tipo) {
        return itemsTipoPorTipo(tipo, true);
    }

    public SelectItem[] itemsTipoPorTipo(String tipo, boolean nulo) {
        return JsfUtil.getSelectItems(getTipoIntegradoPorTipo(tipo), nulo);
    }

    public SiTipo getTipoIntegrado(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public SiTipo tipoPorCodProg(String codProg) {
        List<SiTipo> r = ejbFacade.findByCodProg(codProg);
        if (r.isEmpty()) {
            return null;
        } else {
            return r.get(0);
        }
    }

    public List<SiTipo> getTipoIntegradoTkEst() {
        List<SiTipo> r = ejbFacade.findByTkEst();
        if (r.isEmpty()) {
            return null;
        } else {
            return r;
        }
    }

    public List<SiTipo> getTipoIntegradoPorTipo(String t) {
        List<SiTipo> r = ejbFacade.findByTipo(t);
        if (r.isEmpty()) {
            return null;
        } else {
            return r;
        }
    }

    @FacesConverter(forClass = SiTipo.class)
    public static class TipoIntegradoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoIntegradoController controller = (TipoIntegradoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipoIntegradoController");
            return controller.getTipoIntegrado(getKey(value));
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
            if (object instanceof SiTipo) {
                SiTipo o = (SiTipo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SiTipo.class.getName());
            }
        }

    }

}
