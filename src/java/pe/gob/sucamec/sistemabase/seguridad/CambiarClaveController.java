package pe.gob.sucamec.sistemabase.seguridad;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.data.SbValidacionWeb;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;

@Named("cambiarClaveController")
@SessionScoped
public class CambiarClaveController implements Serializable {

    private String claveActual, clave1, clave2;
    private SbValidacionWeb vw;

    private static final long serialVersionUID = -215238965323276L;
    @EJB
    private SbUsuarioFacade usuarioFacade;
    @Inject
    private LoginController loginController;
    
    public String getClaveActual() {
        return claveActual;
    }

    public void setClaveActual(String claveActual) {
        this.claveActual = claveActual;
    }

    public String getClave1() {
        return clave1;
    }

    public void setClave1(String clave1) {
        this.clave1 = clave1;
    }

    public String getClave2() {
        return clave2;
    }

    public void setClave2(String clave2) {
        this.clave2 = clave2;
    }

    public void cambiarClave() {
        DatosUsuario du = loginController.getUsuario();
        if (!JsfUtil.crearHash(claveActual).equals(du.getHashClave())) {
            JsfUtil.invalidar("formClave:claveActual");
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorValidacionClaveActual"));
            return;
        }
        if (!clave1.equals(clave2)) {
            JsfUtil.invalidar("formClave:clave1");
            JsfUtil.invalidar("formClave:clave2");
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorValidacionClaves"));
            return;
        }
        SbUsuario u = usuarioFacade.find(du.getId());
        u.setClave(JsfUtil.crearHash(clave1));
        usuarioFacade.edit(u);
        loginController.crearDatosUsuario(u, du.getTipoDoc(), du.getPerfiles());
        JsfUtil.mensaje(JsfUtil.bundle("CambioClaveExitosa"));
    }

}
