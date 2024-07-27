package pe.gob.sucamec.sel.cydoc.jsf;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import static pe.gob.sucamec.sel.jsf.util.JsfUtil.desencriptar;

// Nombre de la instancia en la aplicacion //
@Named("mvCdExpedienteCodigoController")
@RequestScoped

/**
 * Clase con MvCdExpedienteController instanciada como mvCdExpedienteController.
 * Contiene funciones utiles para la entidad MvCdExpediente. Esta vinculada a
 * las páginas MvCdExpediente/create.xhtml, MvCdExpediente/update.xhtml,
 * MvCdExpediente/list.xhtml, MvCdExpediente/view.xhtml Nota: Las tablas deben
 * tener la estructura de Sucamec para que funcione adecuadamente, revisar si
 * tiene el campo activo y modificar las búsquedas.
 */
public class MvCdExpedienteCodigoController implements Serializable {

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, filtroAnho;

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> detalle = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.cydoc.beans.CdExpedienteFacade mvCdExpedienteFacade;

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public ArrayRecord getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(ArrayRecord registro) {
        this.registro = registro;
    }

    public ListDataModel<ArrayRecord> getDetalle() {
        return detalle;
    }

    public void setDetalle(ListDataModel<ArrayRecord> detalle) {
        this.detalle = detalle;
    }

    /**
     * Todas las preticiones se empiezan por acá.
     */
    @PostConstruct
    public void inicio() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            String codigo = (String) ec.getRequestParameterMap().get("c");
            codigo = desencriptar(codigo);
            if (codigo != null) {
                registro = (ArrayRecord) mvCdExpedienteFacade.listarExpedientePub(codigo).get(0);
                detalle = new ListDataModel(mvCdExpedienteFacade.listarTrazaPub(((BigDecimal) registro.get("ID_EXPEDIENTE")).longValue()));

            }
        } catch (Exception ex) {
        }
    }

    /**
     * Obtiene el valor de un campo del hashmap, por algun motivo (bug) el xhtml
     * no permite esto directamente.
     *
     * @param r El resultado del native query como Map.
     * @param c El campo que se desea obtener.
     * @return El valor del campo como Object
     */
    public Object valorCampo(ArrayRecord r, String c) {
        return r.get(c);
    }

    /**
     * Constructor
     *
     */
    public MvCdExpedienteCodigoController() {
    }

}
