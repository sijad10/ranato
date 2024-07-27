package pe.gob.sucamec.tickets.jsf;

import java.io.Serializable;
import java.util.GregorianCalendar;
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
import javax.inject.Inject;
import javax.inject.Named;
import pe.gob.sucamec.bdintegrado.bean.TicketRutasFacade;
import pe.gob.sucamec.bdintegrado.data.TicketRutas;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

@Named("ticketRutasController")
@SessionScoped
public class TicketRutasController implements Serializable {

    private TicketRutas current;
    private DataModel items = null, listDerivados = null;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TicketRutasFacade ejbFacade;
    @Inject
    LoginController loginController;
    @Inject
    TicketsController ticketsController;
    private Long ticketId;
    private int selectedItemIndex;
    private String filtro, filtroEstado, filtroTipo, filtroTramite, filtroArea,
            filtroUsuario, filtroCanal, filtroRel, filtroActivo, filtroCerrado, tipoBusDer = "ASUNTO";
    private boolean ultimos;

    public String getTipoBusDer() {
        return tipoBusDer;
    }

    public void setTipoBusDer(String tipoBusDer) {
        this.tipoBusDer = tipoBusDer;
    }

    public boolean isUltimos() {
        return ultimos;
    }

    public void setUltimos(boolean ultimos) {
        this.ultimos = ultimos;
    }

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
            current = (TicketRutas) getItems().getRowData();
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

    public DataModel getListDerivados() {
        return listDerivados;
    }

    public void setListDerivados(DataModel listDerivados) {
        this.listDerivados = listDerivados;
    }

    public String getFiltroEstado() {
        return filtroEstado;
    }

    public void setFiltroEstado(String filtroEstado) {
        this.filtroEstado = filtroEstado;
    }

    public String getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(String filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    public String getFiltroTramite() {
        return filtroTramite;
    }

    public void setFiltroTramite(String filtroTramite) {
        this.filtroTramite = filtroTramite;
    }

    public String getFiltroArea() {
        return filtroArea;
    }

    public void setFiltroArea(String filtroArea) {
        this.filtroArea = filtroArea;
    }

    public String getFiltroUsuario() {
        return filtroUsuario;
    }

    public void setFiltroUsuario(String filtroUsuario) {
        this.filtroUsuario = filtroUsuario;
    }

    public String getFiltroCanal() {
        return filtroCanal;
    }

    public void setFiltroCanal(String filtroCanal) {
        this.filtroCanal = filtroCanal;
    }

    public String getFiltroRel() {
        return filtroRel;
    }

    public void setFiltroRel(String filtroRel) {
        this.filtroRel = filtroRel;
    }

    public String getFiltroActivo() {
        return filtroActivo;
    }

    public void setFiltroActivo(String filtroActivo) {
        this.filtroActivo = filtroActivo;
    }

    public String getFiltroCerrado() {
        return filtroCerrado;
    }

    public void setFiltroCerrado(String filtroCerrado) {
        this.filtroCerrado = filtroCerrado;
    }

    public boolean verAnular(short estado) {
        if (estado == (short) 1) {
            return true;
        }
        return false;
    }

    public String updateAnular() {
        try {
            current = (TicketRutas) getItems().getRowData();
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
        return new ListDataModel(getFacade().selectLikeTicket(ticketId));
    }

    public TicketRutasController() {
    }

    public TicketRutas getSelected() {
        if (current == null) {
            current = new TicketRutas();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TicketRutasFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        filtro = "";
        return "/aplicacion/ticketRutas/List";
    }

    public void buscarDerivados() {
        listDerivados = new ListDataModel(getFacade().selectLikeTicketRutasDerivados(tipoBusDer, filtro, filtroEstado, filtroTipo, filtroTramite, filtroArea, filtroUsuario, filtroCanal, filtroCerrado, loginController.getUsuario().getId(), ultimos));
    }

    public String prepareListDerivados() {
        ultimos = true;
        filtro = "";
        filtroEstado = "%";
        filtroTipo = "%";
        filtroTramite = "%";
        filtroArea = "%";
        filtroUsuario = "%";
        filtroCanal = "%";
        filtroCerrado = "%";
        listDerivados = new ListDataModel(getFacade().selectLikeTicketRutasDerivados(tipoBusDer, filtro, filtroEstado, filtroTipo, filtroTramite, filtroArea, filtroUsuario, filtroCanal, filtroCerrado, loginController.getUsuario().getId(), ultimos));
        return "/aplicacion/sistemaIntegrado/tickets/ListDerivados";
    }

    public SelectItem[] getItemsSelectOneAreasTickets() {
        return getItemsSelectOneAreasTickets(false);
    }

    public SelectItem[] getItemsSelectOneAreasTickets(boolean nulo) {
        List<SbUsuario> l = ejbFacade.selectLoginAreaTickets(loginController.getUsuario().getLogin());
        SelectItem[] lista;
        int i = 0, tam = nulo ? l.size() + 1 : l.size();
        lista = new SelectItem[tam];
        if (nulo) {
            lista[0] = new SelectItem("", "---");
            i++;
        }
        for (SbUsuario u : l) {
            String ticketDesc = u.getDescripcion();
            if (ticketDesc == null) {
                ticketDesc = "";
            }
            lista[i++] = new SelectItem(u, "" + u.getAreaId().getAbreviatura() + " - " + u.getLogin() + " (" + ticketDesc + ")");
        }
        return lista;
    }

    public void prepareListFromTickets(long tid) {
        ticketId = tid;
        recreateModel();
    }

    public String prepareViewTicket() {
        current = (TicketRutas) listDerivados.getRowData();
        return ticketsController.prepareViewFromRutas(current.getTicket());
    }

    public String prepareView() {
        current = (TicketRutas) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new TicketRutas();
        selectedItemIndex = -1;
        return "Create";
    }

    public void prepareCreateFromTickets() {
        current = new TicketRutas();
        selectedItemIndex = -1;
    }

    public void createFromTickets() {
        try {
            LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
            TicketsController tc = (TicketsController) JsfUtil.obtenerBean("ticketsController", TicketsController.class);
            current.setFechaHora(GregorianCalendar.getInstance().getTime());
            current.setActivo((short) 1);
            SbUsuarioGt usr = new SbUsuarioGt((long) lc.getUsuario().getId());
            current.setUsuario(usr);
            current.setTicket(tc.getSelected());
            getFacade().create(current);
            tc.transferirTicket(current.getUsuarioDestino());
            recreateModel();
            prepareCreateFromTickets();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleIntegrado").getString("MensajeTicketTransferido"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleIntegrado").getString("ErrorDePersistencia"));
            System.err.print("TicketsRutasController.createFromTickets():" + e.getMessage());
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

    public String prepareEdit() {
        current = (TicketRutas) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "Edit";
    }

    public void prepareEditFromTickets() {
        current = (TicketRutas) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
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
        current = (TicketRutas) getItems().getRowData();
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

    public TicketRutas getTicketRutas(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = TicketRutas.class)
    public static class TicketRutasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TicketRutasController controller = (TicketRutasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ticketRutasController");
            return controller.getTicketRutas(getKey(value));
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
            if (object instanceof TicketRutas) {
                TicketRutas o = (TicketRutas) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TicketRutas.class.getName());
            }
        }

    }

}
