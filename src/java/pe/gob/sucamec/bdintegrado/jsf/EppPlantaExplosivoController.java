package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.EppPlantaExplosivo;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("eppPlantaExplosivoController")
@SessionScoped
public class EppPlantaExplosivoController implements Serializable {

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppPlantaExplosivoFacade ejbFacade;
    private List<EppPlantaExplosivo> items = null;
    private EppPlantaExplosivo selected;

    public EppPlantaExplosivoController() {
    }

    public EppPlantaExplosivo getSelected() {
        return selected;
    }

    public void setSelected(EppPlantaExplosivo selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

//    private EppPlantaExplosivoFacade getFacade() {
//        return ejbFacade;
//    }
    public EppPlantaExplosivo getEppPlantaExplosivo(java.lang.Long id) {
        return ejbFacade.find(id);
    }
//
//    public List<EppPlantaExplosivo> getItemsAvailableSelectMany() {
//        return getFacade().findAll();
//    }
//
//    public List<EppPlantaExplosivo> getItemsAvailableSelectOne() {
//        return getFacade().findAll();
//    }

    @FacesConverter(forClass = EppPlantaExplosivo.class)
    public static class EppPlantaExplosivoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EppPlantaExplosivoController controller = (EppPlantaExplosivoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "eppPlantaExplosivoController");
            return controller.getEppPlantaExplosivo(getKey(value));
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
            if (object instanceof EppPlantaExplosivo) {
                EppPlantaExplosivo o = (EppPlantaExplosivo) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), EppPlantaExplosivo.class.getName()});
                return null;
            }
        }

    }

}
