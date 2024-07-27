package pe.gob.sucamec.sistemabase.seguridad;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pe.gob.sucamec.sistemabase.beans.SbLogUsuarioFacade;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
import pe.gob.sucamec.sistemabase.data.SbLogUsuario;
import pe.gob.sucamec.sistemabase.data.SbTipo;

@Named("logController")
@SessionScoped
public class LogController implements Serializable {

    private static final long serialVersionUID = -215238965323276L;
    @EJB
    private SbLogUsuarioFacade ejbLogUsuarioFacade;
    @EJB
    private SbTipoFacade sbTipoFacade;
    @Inject
    private LoginController loginController;

    public void escribirLogIngreso() {
        try {
            SbLogUsuario lu = new SbLogUsuario();
            SbTipo tum = sbTipoFacade.tipoPorCodProg("LOG_LOGIN");
            lu.setTipoId(tum);
            lu.setFechaHora(new Date());
            lu.setUsuarioId(loginController.getUsuario().getId());
            ejbLogUsuarioFacade.create(lu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void escribirLogSalida() {
        try {
            SbLogUsuario lu = new SbLogUsuario();
            SbTipo tum = sbTipoFacade.tipoPorCodProg("LOG_LOGOUT");
            lu.setTipoId(tum);
            lu.setFechaHora(new Date());
            lu.setUsuarioId(loginController.getUsuario().getId());
            ejbLogUsuarioFacade.create(lu);
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
    
    public void escribirLogBuscar(String datos, String tabla) {
        try {
            SbLogUsuario lu = new SbLogUsuario();
            SbTipo tum = sbTipoFacade.tipoPorCodProg("LOG_BUSCAR");
            lu.setTipoId(tum);
            lu.setFechaHora(new Date());
            lu.setDatos(datos);
            lu.setTabla(tabla);
            lu.setUsuarioId(loginController.getUsuario().getId());
            ejbLogUsuarioFacade.create(lu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void escribirLogVer(String datos, String tabla) {
        try {
            SbLogUsuario lu = new SbLogUsuario();
            SbTipo tum = sbTipoFacade.tipoPorCodProg("LOG_VER");
            lu.setTipoId(tum);
            lu.setFechaHora(new Date());
            lu.setDatos(datos);
            lu.setTabla(tabla);
            lu.setUsuarioId(loginController.getUsuario().getId());
            ejbLogUsuarioFacade.create(lu);
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
}
