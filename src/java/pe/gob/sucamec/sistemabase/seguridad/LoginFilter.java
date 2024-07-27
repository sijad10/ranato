package pe.gob.sucamec.sistemabase.seguridad;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    public static final String FACES_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<partial-response><redirect url=\"%s\"></redirect></partial-response>";
    public static final String LOGIN_URL = "/faces/login.xhtml?faces-redirect=true",
            SESION_URL = "/errores/errorSesion.xhtml?faces-redirect=true";

    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * Verifica las páginas de acceso publico al sistema
     *
     * @param req Request del Filtro
     * @return true si se tiene acceso, false si no se tiene acceso
     */
    public static boolean accesoPublico(HttpServletRequest req) {
        String uri = req.getRequestURI(),
                path = req.getContextPath();
        uri = uri.substring(uri.indexOf("/", 1));
        return uri.startsWith("/errores/")
                || uri.startsWith("/javax.faces.resource/")
                || uri.startsWith("/resources/")
                || uri.equals("/faces/login.xhtml")
                || uri.startsWith("/webresources/")
                || uri.startsWith("/faces/pub/")
                || uri.startsWith("/faces/m/")
                || uri.startsWith("/faces/errores/")
                || uri.startsWith("/faces/javax.faces.resource/")
                || uri.startsWith("/faces/resources/")
                || uri.startsWith("/faces/qr/");
    }

    /**
     * Permite salir a un página de sesion o login dependiendo del caso.
     *
     * @param req Request del filtro
     * @param res Response del filtro
     * @param url Página a la que se va a salir
     */
    public void salir(HttpServletRequest req, HttpServletResponse res, String url) {
        String path = req.getContextPath();
        try {
            if ("partial/ajax".equals(req.getHeader("Faces-Request"))) {
                res.setContentType("text/xml");
                res.setCharacterEncoding("UTF-8");
                res.getWriter().printf(FACES_REDIRECT_XML, path + url);
            } else {
                res.sendRedirect(path + url);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * @param request
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI(),
                path = req.getContextPath();
        // Revisar si la página es de acceso público o privado // 
        if (!accesoPublico(req)) {
            // Evitar que ingresen a paths que no sean de JSF //
            if (!uri.startsWith(path + "/faces")) {
                salir(req, res, LOGIN_URL);
                return;
            }
            // Revisar si existen datos de sesion //
            if ((session != null) && (session.getAttribute("seguridad_usuario") == null)) {
                salir(req, res, SESION_URL);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }
}
