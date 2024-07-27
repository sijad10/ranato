/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.seguridad;

import java.util.ArrayList;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Renato
 */
public class LoginPhaseListener implements PhaseListener {

    @Inject
    LoginController loginController;

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        ExternalContext ec = context.getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) ec.getRequest();
        String metodo = httpReq.getMethod();
        if (LoginFilter.accesoPublico(httpReq)) {
            return;
        }
        if (((PhaseId.INVOKE_APPLICATION == event.getPhaseId() && metodo.equals("POST")) || (PhaseId.RESTORE_VIEW == event.getPhaseId() && metodo.equals("GET")))) {
            HttpSession session = (HttpSession) ec.getSession(false);
            try {
                String uri = (String) context.getViewRoot().getViewId(),
                        huri = (String) httpReq.getRequestURI(),
                        path = ec.getRequestContextPath();
                // Verificar si existe la sesion //
                if ((session == null) || (loginController == null)) {
                    return;
                }

                // Verificar el estado en el cual ejecutar las funciones //
                // Esto es debido a que el filtro se ejecuta antes de generar los Beans se sesion, por eso no se puede extraer de un bean de sesion.
                DatosUsuario usr = (DatosUsuario) loginController.getUsuario();
                ArrayList<DatosPagina> paginasSistema = loginController.getPaginasSistema();
                if (usr == null) {
                    ec.redirect(path + LoginFilter.LOGIN_URL);
                } else {
                    // reglas x perfiles //
                    ArrayList<String> perfiles = usr.getPerfiles();
                    if (perfiles.isEmpty()) {
                        ec.redirect(path + LoginFilter.LOGIN_URL);
                        session.invalidate();
                        return;
                    }
                    Long paginaActual = 0L;  // Pagina actual, para reglas página //
                    // Bloqueo de páginas donde no hay acceso //
                    for (DatosPagina dp : paginasSistema) {
                        if (uri.startsWith(dp.getUrl()) && !dp.isAcceso()) {
                            ec.redirect(path + LoginFilter.LOGIN_URL);
                            session.invalidate();
                            return;
                        }
                        if (uri.equals(dp.getUrl())) {
                            paginaActual = dp.getId();
                            break;
                        }
                    }
                    loginController.setPaginaActual(paginaActual);
                }
            } catch (Exception ex) {
                session.invalidate();
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
