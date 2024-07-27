package pe.gob.sucamec.encuesta.jsf;


import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.encuesta.beans.SbAlternativaFacade;
import pe.gob.sucamec.encuesta.data.SbAlternativa;

@Named("sbAlternativaController")
@SessionScoped
public class SbAlternativaController implements Serializable {

    private SbAlternativa current;
    @EJB
    private SbAlternativaFacade ejbFacade;

    public SbAlternativaController() {
    }

    public SbAlternativa getSbAlternativa(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbAlternativaConverter")
    public static class SbAlternativaControllerConverterN extends SbAlternativaControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbAlternativa.class)
    public static class SbAlternativaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbAlternativaController c = (SbAlternativaController) JsfUtil.obtenerBean("sbAlternativaController", SbAlternativaController.class);
            return c.getSbAlternativa(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbAlternativa) {
                SbAlternativa o = (SbAlternativa) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbAlternativa.class.getName());
            }
        }
    }

}
