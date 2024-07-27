package pe.gob.sucamec.sel.gamac.jsf;

import pe.gob.sucamec.bdintegrado.jsf.util.PaginationHelper;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pe.gob.sucamec.sel.gamac.beans.GamacAmaCatalogoFacade;
import pe.gob.sucamec.sel.gamac.data.GamacAmaCatalogo;

@Named("gamacAmaCatalogoController")
@SessionScoped
public class GamacAmaCatalogoController implements Serializable {

    private GamacAmaCatalogo current;
    private DataModel items = null;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaCatalogoFacade gamacAmaCatalogoFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public GamacAmaCatalogoController() {
    }

    public GamacAmaCatalogo getSelected() {
        if (current == null) {
            current = new GamacAmaCatalogo();
            selectedItemIndex = -1;
        }
        return current;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return gamacAmaCatalogoFacade.count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(gamacAmaCatalogoFacade.findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (GamacAmaCatalogo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new GamacAmaCatalogo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            gamacAmaCatalogoFacade.create(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaCatalogoCreated"));
            return prepareCreate();
        } catch (Exception e) {
           // JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (GamacAmaCatalogo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            gamacAmaCatalogoFacade.edit(current);
          //  JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaCatalogoUpdated"));
            return "View";
        } catch (Exception e) {
           // JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (GamacAmaCatalogo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
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
            gamacAmaCatalogoFacade.remove(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaCatalogoDeleted"));
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = gamacAmaCatalogoFacade.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = gamacAmaCatalogoFacade.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

//    public SelectItem[] getItemsAvailableSelectMany() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
//    }
//
//    public SelectItem[] getItemsAvailableSelectOne() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
//    }

    public GamacAmaCatalogo getGamacAmaCatalogo(java.lang.Long id) {
        return gamacAmaCatalogoFacade.find(id);
    }

    @FacesConverter(forClass = GamacAmaCatalogo.class)
    public static class gamacAmaCatalogoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GamacAmaCatalogoController controller = (GamacAmaCatalogoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gamacAmaCatalogoController");
            return controller.getGamacAmaCatalogo(getKey(value));
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
            if (object instanceof GamacAmaCatalogo) {
                GamacAmaCatalogo o = (GamacAmaCatalogo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + GamacAmaCatalogo.class.getName());
            }
        }

    }

}
