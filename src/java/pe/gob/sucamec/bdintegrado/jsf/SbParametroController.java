package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.SbParametro;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;


// Nombre de la instancia en la aplicacion //
@Named("sbParametroController")
@SessionScoped

/**
 * Clase con SbParametroController instanciada como sbParametroController.
 * Contiene funciones utiles para la entidad SbParametro. Esta vinculada a las
 * páginas SbParametro/create.xhtml, SbParametro/update.xhtml,
 * SbParametro/list.xhtml, SbParametro/view.xhtml Nota: Las tablas deben tener
 * la estructura de Sucamec para que funcione adecuadamente, revisar si tiene el
 * campo activo y modificar las búsquedas.
 */
public class SbParametroController implements Serializable {

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;

    
    public SbParametro obtenerParametroXCodProg(String codProg){
        return ejbSbParametroFacade.obtenerParametroXCodProg(codProg);
    }
    
    public SbParametro obtenerParametroXNombre(String nombre){
        return ejbSbParametroFacade.obtenerParametroXNombre(nombre);
    }
}
