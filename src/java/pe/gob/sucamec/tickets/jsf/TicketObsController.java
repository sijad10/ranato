package pe.gob.sucamec.tickets.jsf;

import java.io.Serializable;
import java.util.GregorianCalendar;
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
import pe.gob.sucamec.bdintegrado.bean.TicketObsFacade;
import pe.gob.sucamec.bdintegrado.data.TicketObs;
import pe.gob.sucamec.sel.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

@Named("ticketObsController")
@SessionScoped
public class TicketObsController implements Serializable {

    private TicketObs current;
    private DataModel items = null;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TicketObsFacade ejbFacade;
    private String filtro;
    private long ticketId;
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

    public void updateActivar() {
        try {
            current = (TicketObs) getItems().getRowData();
            selectedItemIndex = getItems().getRowIndex();
            current.setActivo((short) 1);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroActualizado"));
            buscar();
            recreateModel();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("PersistenceErrorOccured"));
        }
    }

    public boolean verAnular(short estado) {
        if (estado == (short) 1) {
            return true;
        }
        return false;
    }

    public void updateAnular() {
        try {
            current = (TicketObs) getItems().getRowData();
            selectedItemIndex = getItems().getRowIndex();
            current.setActivo((short) 0);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroActualizado"));
            buscar();
            recreateModel();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("PersistenceErrorOccured"));
        }
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String buscar() {
        items = createDataModel();
        return "List";
    }
    
    public DataModel createDataModel() {
        return new ListDataModel(getFacade().selectLikeTicket(ticketId)); // Crear el select like en el facade //
    }

    public TicketObsController() {
    }

    public TicketObs getSelected() {
        if (current == null) {
            current = new TicketObs();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TicketObsFacade getFacade() {
        return ejbFacade;
    }

    public void prepareListFromTickets(long tid) {
        ticketId = tid;
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        filtro = "";
        return "/aplicacion/ticketObs/List";
    }

    public String prepareView() {
        current = (TicketObs) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "View";
    }

    public void prepareCreateFromTickets() {
        current = new TicketObs();
        selectedItemIndex = -1;
    }

    public String prepareCreate() {
        current = new TicketObs();
        selectedItemIndex = -1;
        return "/aplicacion/ticketObs/Create";
    }

    public void createFromTickets() {
        try {
            LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
            TicketsController tc = (TicketsController) JsfUtil.obtenerBean("ticketsController", TicketsController.class);
            current.setFechaHora(GregorianCalendar.getInstance().getTime());
            current.setActivo((short) 1);
            current.setUsuario(new SbUsuarioGt((long) lc.getUsuario().getId()));
            current.setTicket(tc.getSelected());
            getFacade().create(current);
            recreateModel();
            prepareCreateFromTickets();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroCreado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("ErrorDePersistencia"));
            System.err.print("TicketsObsController.createFromTickets():" + e.getMessage());
        }
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

    public void prepareEditFromTickets() {
        current = (TicketObs) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
    }

    public String prepareEdit() {
        current = (TicketObs) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "Edit";
    }

    public void updateFromTickets() {
        try {
            getFacade().edit(current);
            recreateModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("ErrorDePersistencia"));
        }
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
        current = (TicketObs) getItems().getRowData();
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
            items = createDataModel();
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

    public TicketObs getTicketObs(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = TicketObs.class)
    public static class TicketObsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TicketObsController controller = (TicketObsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ticketObsController");
            return controller.getTicketObs(getKey(value));
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
            if (object instanceof TicketObs) {
                TicketObs o = (TicketObs) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TicketObs.class.getName());
            }
        }

    }

}
