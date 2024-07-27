package pe.gob.sucamec.bdintegrado.jsf;


import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import pe.gob.sucamec.bdintegrado.bean.EppAlmacenAduaneroFacade;
import pe.gob.sucamec.bdintegrado.data.EppAlmacenAduanero;

@Named("eppAlmacenAduaneroController")
@SessionScoped
public class EppAlmacenAduaneroController implements Serializable {

    private EppAlmacenAduanero current;
    private DataModel items = null;
    @EJB
    private EppAlmacenAduaneroFacade ejbFacade;
    private int selectedItemIndex;

    public EppAlmacenAduaneroController() {
    }

    public EppAlmacenAduanero getSelected() {
        if (current == null) {
            current = new EppAlmacenAduanero();
            selectedItemIndex = -1;
        }
        return current;
    }

    private EppAlmacenAduaneroFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (EppAlmacenAduanero) getItems().getRowData();
        return "View";
    }

    public String prepareCreate() {
        current = new EppAlmacenAduanero();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            return prepareCreate();
        } catch (Exception e) {
            return null;
        }
    }

    public String prepareEdit() {
        current = (EppAlmacenAduanero) getItems().getRowData();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EppAlmacenAduaneroUpdated"));
            return "View";
        } catch (Exception e) {
            return null;
        }
    }

    public String destroy() {
        current = (EppAlmacenAduanero) getItems().getRowData();
        performDestroy();
        recreatePagination();
        recreateModel();
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
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EppAlmacenAduaneroDeleted"));
        } catch (Exception e) {
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
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
    }

    public String next() {
        recreateModel();
        return "List";
    }

    public String previous() {
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return null;
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return null;
    }

    public EppAlmacenAduanero getEppAlmacenAduanero(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = EppAlmacenAduanero.class)
    public static class EppAlmacenAduaneroControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EppAlmacenAduaneroController controller = (EppAlmacenAduaneroController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "eppAlmacenAduaneroController");
            return controller.getEppAlmacenAduanero(getKey(value));
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
            if (object instanceof EppAlmacenAduanero) {
                EppAlmacenAduanero o = (EppAlmacenAduanero) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EppAlmacenAduanero.class.getName());
            }
        }

    }

}
