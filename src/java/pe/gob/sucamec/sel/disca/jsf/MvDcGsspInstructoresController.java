package pe.gob.sucamec.sel.disca.jsf;


import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.apache.commons.lang3.StringUtils;

// Nombre de la instancia en la aplicacion //
@Named("mvDcGsspInstructoresController")
@SessionScoped

/**
 * Clase con MvDcGsspInstructoresController instanciada como
 * mvDcGsspInstructoresController. Contiene funciones utiles para la entidad
 * MvDcGsspInstructores. Esta vinculada a las páginas
 * MvDcGsspInstructores/create.xhtml, MvDcGsspInstructores/update.xhtml,
 * MvDcGsspInstructores/list.xhtml, MvDcGsspInstructores/view.xhtml Nota: Las
 * tablas deben tener la estructura de Sucamec para que funcione adecuadamente,
 * revisar si tiene el campo activo y modificar las búsquedas.
 */
public class MvDcGsspInstructoresController implements Serializable {

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, curso;

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.disca.beans.ConsultasDiscaFacade mvDcGsspInstructoresFacade;

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public SelectItem[] getListaCursos() {
        List<ArrayRecord> l = mvDcGsspInstructoresFacade.listarCursos();
        SelectItem[] si = new SelectItem[l.size()];
        for (int i = 0; i < si.length; i++) {
            ArrayRecord h = l.get(i);
            si[i] = new SelectItem(h.get("COD_CUR"), h.get("NOM_CUR").toString());
        }
        return si;
    }

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

    /**
     * Propiedad para el filtro de busquedas
     *
     * @return el filtro de busquedas
     */
    public String getFiltro() {
        return filtro;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @param filtro el nuevo filtro de busquedas
     */
    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    /**
     * Propiedad para los resultados de la búsqueda del CRUD.
     *
     * @return Los resultados de la búsqueda.
     */
    public ListDataModel<ArrayRecord> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        return "List";
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public String mostrarBuscar() {
        if (resultados != null) {
            buscar();
        }
        return "pm:buscarPage";
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        resultados = new ListDataModel(mvDcGsspInstructoresFacade.listarInstructoresAutorizados(((filtro != null)?filtro.trim():null), curso));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public String mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        return "pm:verPage";
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
    public MvDcGsspInstructoresController() {
    }
    
    public String obtenerNroDoc(String doc){
        if(doc == null ){
            return null;
        }
        if(doc.trim().length() < 8){
            doc = StringUtils.leftPad(doc, 8, "0");
        }
        return doc;
    }

}
