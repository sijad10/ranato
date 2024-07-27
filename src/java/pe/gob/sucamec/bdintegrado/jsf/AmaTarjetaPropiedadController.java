package pe.gob.sucamec.bdintegrado.jsf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.jsf.util.PaginationHelper;
import pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

@Named("amaTarjetaPropiedadController")
@SessionScoped
public class AmaTarjetaPropiedadController implements Serializable {

    private AmaTarjetaPropiedad current;
    private DataModel items = null;

    //tarjeta electronica
    private Date fechaIniFiltroEle;
    private Date fechaFinFiltroEle;
    private String filtroSimpleTipo;
    private String filtroSimpleTexto;
    private List<Map> lstTarEleMap = new ArrayList();
    private List<AmaTarjetaPropiedad> lstTarEle = new ArrayList();

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AmaTarjetaPropiedadController() {
    }

    public AmaTarjetaPropiedad getSelected() {
        if (current == null) {
            current = new AmaTarjetaPropiedad();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AmaTarjetaPropiedadFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    /*GETTERS & SETTERS*/
    public Date getFechaIniFiltroEle() {
        return fechaIniFiltroEle;
    }

    public void setFechaIniFiltroEle(Date fechaIniFiltroEle) {
        this.fechaIniFiltroEle = fechaIniFiltroEle;
    }

    public Date getFechaFinFiltroEle() {
        return fechaFinFiltroEle;
    }

    public void setFechaFinFiltroEle(Date fechaFinFiltroEle) {
        this.fechaFinFiltroEle = fechaFinFiltroEle;
    }

    public String getFiltroSimpleTipo() {
        return filtroSimpleTipo;
    }

    public void setFiltroSimpleTipo(String filtroSimpleTipo) {
        this.filtroSimpleTipo = filtroSimpleTipo;
    }

    public String getFiltroSimpleTexto() {
        return filtroSimpleTexto;
    }

    public void setFiltroSimpleTexto(String filtroSimpleTexto) {
        this.filtroSimpleTexto = filtroSimpleTexto;
    }

    public List<Map> getLstTarEleMap() {
        return lstTarEleMap;
    }

    public void setLstTarEleMap(List<Map> lstTarEleMap) {
        this.lstTarEleMap = lstTarEleMap;
    }

    public List<AmaTarjetaPropiedad> getLstTarEle() {
        return lstTarEle;
    }

    public void setLstTarEle(List<AmaTarjetaPropiedad> lstTarEle) {
        this.lstTarEle = lstTarEle;
    }

    /**
     * METODOS
     */
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (AmaTarjetaPropiedad) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new AmaTarjetaPropiedad();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaTarjetaPropiedadCreated"));
            return prepareCreate();
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (AmaTarjetaPropiedad) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaTarjetaPropiedadUpdated"));
            return "View";
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (AmaTarjetaPropiedad) getItems().getRowData();
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
            getFacade().remove(current);
            // JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaTarjetaPropiedadDeleted"));
        } catch (Exception e) {
            //   JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
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
    public AmaTarjetaPropiedad getAmaTarjetaPropiedad(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = AmaTarjetaPropiedad.class)
    public static class AmaTarjetaPropiedadControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AmaTarjetaPropiedadController controller = (AmaTarjetaPropiedadController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "amaTarjetaPropiedadController");
            return controller.getAmaTarjetaPropiedad(getKey(value));
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
            if (object instanceof AmaTarjetaPropiedad) {
                AmaTarjetaPropiedad o = (AmaTarjetaPropiedad) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + AmaTarjetaPropiedad.class.getName());
            }
        }

    }

    public String prepareListTarEle() {
        limpiarVariables();
        return "/aplicacion/gamac/amaTarjetaPropiedad/BandejaTarEle.xhtml";
    }

    public void limpiarVariables() {
        fechaIniFiltroEle = null;
        fechaFinFiltroEle = null;
        filtroSimpleTipo = null;
        filtroSimpleTexto = null;
        lstTarEleMap = new ArrayList();
        lstTarEle = new ArrayList();
    }

    public void buscarBandejaTarEle() {

        DatosUsuario datosUsuario = JsfUtil.getLoggedUser();

        lstTarEle = new ArrayList();
        lstTarEleMap = new ArrayList();
        String tipoDoc = datosUsuario.getTipoDoc();
        String nroDoc = datosUsuario.getLogin();

        if (!validarBusquedaTarEle()) {
            return;
        }
        lstTarEle = ejbFacade.listarTarjetaElectronica(tipoDoc, nroDoc, filtroSimpleTipo, filtroSimpleTexto, fechaIniFiltroEle, fechaFinFiltroEle);

        for (AmaTarjetaPropiedad atp : lstTarEle) {
            Map map = new HashMap();
            map.put("tarjeta", atp);
            map.put("nroExpediente", atp.getNroExpediente());
            map.put("nroPropietario", atp.getPersonaCompradorId().getNumDoc());
            map.put("nombrePropietario", atp.getPersonaCompradorId().getNombreCompleto());
            map.put("nroRua", atp.getArmaId().getNroRua());
            //FALTA TIPOARMA/MARCA/MODELO/CALIBRE
            map.put("caracteristicas", "");
            map.put("serie", atp.getArmaId().getSerie());
            map.put("fechaEmision", atp.getFechaEmision());
            map.put("modalidad", atp.getModalidadId().getNombre());

            lstTarEleMap.add(map);
        }
    }

    public boolean validarBusquedaTarEle() {

        if (filtroSimpleTipo == null && fechaIniFiltroEle == null) {
            JsfUtil.mensajeError("Por favor, escriba un criterio de búsqueda o ingrese las fechas");
            return false;
        }

        if (filtroSimpleTipo != null) {
            if (filtroSimpleTexto == null) {
                JsfUtil.mensajeError("Por favor, escriba un criterio de búsqueda ");
                return false;
            }
        }

        if (fechaIniFiltroEle != null) {
            if (fechaFinFiltroEle == null) {
                JsfUtil.mensajeError("Ingrese las fechas para para busqueda multiple");
                return false;
            } else {
                long diffInMillies = Math.abs(fechaFinFiltroEle.getTime() - fechaIniFiltroEle.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if (diff > 365) {
                    JsfUtil.mensajeError("Las fechas seleccionadas deben tener un rango maximo de 1 año");
                    return false;
                }
            }
        }
        return true;
    }

    public StreamedContent obtenerArchivo(AmaTarjetaPropiedad tarSelected) {
        StreamedContent r = null;
        try {
            String path = pe.gob.sucamec.sel.gamac.jsf.util.JsfUtil.bundleGamac("Documentos_pathUpload_TP");
            FileInputStream f = new FileInputStream(path + tarSelected.getId() + "[R].pdf");
            r = new DefaultStreamedContent(f, "application/pdf", "" + tarSelected.getId() + "[R].pdf");
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }

}
