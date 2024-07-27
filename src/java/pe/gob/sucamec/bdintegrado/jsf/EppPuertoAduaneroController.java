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
import pe.gob.sucamec.bdintegrado.bean.EppPuertoAduaneroFacade;
import pe.gob.sucamec.bdintegrado.data.EppPuertoAduanero;

@Named("eppPuertoAduaneroController")
@SessionScoped
public class EppPuertoAduaneroController implements Serializable {

    private EppPuertoAduanero current;
    private DataModel items = null;
    @EJB
    private EppPuertoAduaneroFacade ejbFacade;
    private int selectedItemIndex;

    public EppPuertoAduaneroController() {
    }

    public EppPuertoAduanero getSelected() {
        if (current == null) {
            current = new EppPuertoAduanero();
            selectedItemIndex = -1;
        }
        return current;
    }

    private EppPuertoAduaneroFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (EppPuertoAduanero) getItems().getRowData();
        return "View";
    }

    public String prepareCreate() {
        current = new EppPuertoAduanero();
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
        current = (EppPuertoAduanero) getItems().getRowData();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            return "View";
        } catch (Exception e) {
            return null;
        }
    }

    public String destroy() {
        current = (EppPuertoAduanero) getItems().getRowData();
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

    public EppPuertoAduanero getEppPuertoAduanero(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = EppPuertoAduanero.class)
    public static class EppPuertoAduaneroControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EppPuertoAduaneroController controller = (EppPuertoAduaneroController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "eppPuertoAduaneroController");
            return controller.getEppPuertoAduanero(getKey(value));
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
            if (object instanceof EppPuertoAduanero) {
                EppPuertoAduanero o = (EppPuertoAduanero) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EppPuertoAduanero.class.getName());
            }
        }

    }

}
