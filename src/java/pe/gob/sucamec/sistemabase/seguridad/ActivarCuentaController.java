package pe.gob.sucamec.sistemabase.seguridad;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.sistemabase.beans.SbMedioContactoFacade;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
import pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade;
import pe.gob.sucamec.sistemabase.beans.SbValidacionWebFacade;
import pe.gob.sucamec.sistemabase.data.SbMedioContacto;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.data.SbValidacionWeb;
import pe.gob.sucamec.validaqr.jsf.util.JsfUtil;
import wspide.Consulta;
import wspide.Consulta_Service;

@Named("activarCuentaController")
@SessionScoped
public class ActivarCuentaController implements Serializable {

    /**
     * @return the enviado
     */
    public Boolean getEnviado() {
        return enviado;
    }

    /**
     * @param enviado the enviado to set
     */
    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    /**
     * @return the activarLink
     */
    public Boolean getActivarLink() {
        return activarLink;
    }

    /**
     * @param activarLink the activarLink to set
     */
    public void setActivarLink(Boolean activarLink) {
        this.activarLink = activarLink;
    }

    public String getMensajeFinal() {
        return mensajeFinal;
    }

    public void setMensajeFinal(String mensajeFinal) {
        this.mensajeFinal = mensajeFinal;
    }

    public enum Estados {

        INICIO, DATOS, FIN, ERROR
    };

    private Estados estado;
    private String hash, documento, usuario, clave1, clave2, datosUsuario;
    private SbValidacionWeb vw;
    private Boolean envioMensaje;
    private String celular;
    private String codigoVer;
    private String codigoGenerado;
    private String mensajeFinal;
    private static final String vacio = "";
    private static final String espacio = " ";
    private Boolean enviado;
    private Boolean activarLink;
    private RequestContext rc;
    private FacesContext fc;

    private static final long serialVersionUID = -215238965323276L;
    @EJB
    private SbValidacionWebFacade validacionWebFacade;
    @EJB
    private SbUsuarioFacade usuarioFacade;
    @EJB
    private SbMedioContactoFacade ejbSbMedioContactoFacade;
    @EJB
    private SbTipoFacade ejbSbTipoFacade;

    public String getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(String datosUsuario) {
        this.datosUsuario = datosUsuario;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    /**
     * @return the envioMensaje
     */
    public Boolean getEnvioMensaje() {
        return envioMensaje;
    }

    /**
     * @param envioMensaje the envioMensaje to set
     */
    public void setEnvioMensaje(Boolean envioMensaje) {
        this.envioMensaje = envioMensaje;
    }

    /**
     * @return the celular
     */
    public String getCelular() {
        return celular;
    }

    /**
     * @param celular the celular to set
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     * @return the codigoVer
     */
    public String getCodigoVer() {
        return codigoVer;
    }

    /**
     * @param codigoVer the codigoVer to set
     */
    public void setCodigoVer(String codigoVer) {
        this.codigoVer = codigoVer;
    }

    public void volverInicio() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        estado = Estados.INICIO;
    }

    public String direccionarLogin() {
//        if (envioMensaje) {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        estado = Estados.INICIO;
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean correoNuevaContrasenia(SbUsuario u) {
        //Crear validacion Web //
        validacionWebFacade.desactivarValidacionesWeb(u);
        String hash = validacionWebFacade.crearValidacionWeb(ejbSbTipoFacade.tipoPorCodProg("TP_VW_NU"), u);
        //Preparar Correo
        String mensaje = "Estimado(a) " + u.getNombres() + " " + u.getApePat() + " "
                + u.getApeMat() + ".\n\n" + JsfUtil.bundle("RecuperarClave_TextoCorreo2")
                + "\n" + JsfUtil.bundle("RecuperarClave_Url") + hash
                + "\n\n";
        JsfUtil.enviarCorreoGmail(u.getCorreo(), JsfUtil.bundle("RecuperarClave_Asunto"), mensaje);
        return JsfUtil.enviarCorreo(u.getCorreo(), JsfUtil.bundle("RecuperarClave_Asunto"), mensaje);
    }

    public void validarDatoIngresado(String in) {
        String tipo = vacio;
        int tam = 0;
        String valor = vacio;
        String idCampo = vacio;
        switch (in) {
            case "CEL":
                if (celular != null && !vacio.equals(celular)) {
                    tam = celular.length();
                    valor = celular;
                    tipo = "N";
                }
                idCampo = "celular";
                break;
            case "COD":
                if (codigoVer != null && !vacio.equals(codigoVer)) {
                    tam = codigoVer.length();
                    valor = codigoVer;
                    tipo = "N";
                }
                idCampo = "codigo";
                break;
            case "DOC":
                if (documento != null && !vacio.equals(documento)) {
                    tam = documento.length();
                    valor = documento;
                    tipo = "N";
                }
                idCampo = "documento";
                break;
            case "USU":
                if (usuario != null && !vacio.equals(usuario)) {
                    tam = usuario.length();
                    valor = usuario;
                    tipo = "N";
                }
                idCampo = "usuario";
                break;
            default:
                break;
        }
        String nomCampo = vacio;
        Boolean isNumero = Boolean.TRUE;
        if ("N".equalsIgnoreCase(tipo)) {
            for (int i = 0; i < tam; i++) {
                if (!Character.isDigit(valor.charAt(i))) {
                    isNumero = Boolean.FALSE;
                    break;
                }
            }
            if (!isNumero) {
                nomCampo = limpiarObtenerCampo(in);
                JsfUtil.mensajeAdvertencia("El campo " + nomCampo + " debe contener solamente números");
            }
        }
        RequestContext.getCurrentInstance().update("formActivar:" + idCampo);
    }

    public String limpiarObtenerCampo(String campo) {
        String nomCampo = vacio;
        switch (campo) {
            case "CEL":
                celular = null;
                nomCampo = "Celular";
                break;
            case "COD":
                codigoVer = null;
                nomCampo = "Código";
                break;
            case "DOC":
                documento = null;
                nomCampo = "Documento";
                break;
            case "USU":
                usuario = null;
                nomCampo = "Usuario";
                break;
            default:
                break;
        }
        return nomCampo;
    }

    public void activarCuenta() {
        String mensaje = vacio;
        if (envioMensaje) {
            if (!(codigoVer != null && !vacio.equals(codigoVer))) {
                mensaje = JsfUtil.bundle("MensajeIngreseCodigoVerificacion");
            }
            if (!(codigoGenerado != null && !vacio.equals(codigoGenerado)) && vacio.equals(mensaje)) {
                mensaje = JsfUtil.bundle("MensajeSoliciteCodigoVerificacion");
            }
            if (!(Objects.equals(codigoVer, codigoGenerado)) && vacio.equals(mensaje)) {
                mensaje = JsfUtil.bundle("MensajeCodigoInvalido");
            }
        }
        if (vacio.equals(mensaje)) {
            if (!clave1.equals(clave2)) {
                JsfUtil.invalidar("formActivar:clave1");
                JsfUtil.invalidar("formActivar:clave2");
                JsfUtil.mensajeError(JsfUtil.bundle("ActivarCuentaValidator_claves"));
                return;
            }
            if (vw != null) {
                SbUsuario u = vw.getUsuarioId();
                SbPersona p = u.getPersonaId();
                String numDoc = p.getTipoId().getCodProg().equals("TP_PER_JUR")
                        ? p.getRuc() : (p.getTipoId().getCodProg().equals("TP_PER_NAT") && (p.getRuc() != null && !"".equals(p.getRuc().trim()))
                        ? p.getRuc() : p.getNumDoc());
//                if (p.getTipoId().getCodProg().equals("TP_PER_JUR")) {
//                    numDoc = p.getRuc();
//                } else if (p.getTipoId().getCodProg().equals("TP_PER_NAT")&&(p.getRuc()!=null&&!"".equals(p.getRuc().trim()))) {
//                    numDoc = p.getRuc();
//                }else{
//                    numDoc = p.getNumDoc();
//                }
                if (usuario != null && !usuario.trim().equals(u.getLogin())) {
                    JsfUtil.invalidar("formActivar:usuario");
                    JsfUtil.mensajeError(JsfUtil.bundle("ActivarCuentaValidator_usuario2"));
                    return;
                }
                if (documento != null && !documento.trim().equals(numDoc)) {
                    JsfUtil.invalidar("formActivar:documento");
                    JsfUtil.mensajeError(JsfUtil.bundle("ActivarCuentaValidator_documento2"));
                    return;
                }

                // Actualizar Usuario //
                u.setActivo((short) 1);
                u.setFechaFin(null);
                int aleatorio = (int) (Math.random());
                u.setClave(JsfUtil.crearHash((clave1 != null && !"".equals(clave1.trim())) ? clave1 : (String.valueOf(aleatorio) + "temporal")));
                actualizarCelular(p);
                usuarioFacade.edit(u);
                // Actualizar Verificacion //
                vw.setFechaVal(new Date());
                vw.setActivo((short) 0);
                validacionWebFacade.edit(vw);
                mensajeFinal = JsfUtil.bundle("ActivarCuenta_Texto4");
                //Solo cuando no está disponible el servicio de mensajería
//                if (!envioMensaje) {
//                    if (!correoNuevaContrasenia(u)) {
//                        JsfUtil.mensajeError(JsfUtil.bundle("RecuperarClave_ErrorEnvio"));
//                        return;
//                    }
//                    mensajeFinal = JsfUtil.bundle("ActivarCuenta_Texto5");
//                }
                datosUsuario = u.getNombres() + " " + u.getApePat() + " " + u.getApeMat();
                estado = Estados.FIN;
                return;
            }
            estado = Estados.ERROR;

        } else {
            JsfUtil.mensajeError(mensaje);
        }
    }

    public void limpiarNumCel() {
        celular = null;
    }

    public void actualizarCelular(SbPersona p) {
        if (celular != null) {
            //Lista medios de contacto para actualizar el numero de celular en caso sea diferente al de la BD
            List<SbMedioContacto> listMC = ejbSbMedioContactoFacade.listarMedioContactoPorPersona(p);
            for (SbMedioContacto mc : listMC) {
                if ("TP_MEDCON_MOV".equalsIgnoreCase(mc.getTipoId().getCodProg())) {
                    if (!mc.getValor().equals(celular)) {
                        mc.setValor(celular);
                        ejbSbMedioContactoFacade.edit(mc);
                    }
                }
            }
        }
    }

    public void mostrarActivarCuenta() {
        activarLink = Boolean.FALSE;
        vw = validacionWebFacade.buscarPorHash(hash);
        if (vw != null) {
            List<SbTipo> listReniec = ejbSbTipoFacade.listaTipos("TP_WS_EXT");
            setEnvioMensaje(Boolean.FALSE);
            if (listReniec != null) {
                for (SbTipo r : listReniec) {
                    switch (r.getCodProg()) {
                        case "TP_WS_EXT_SMS":
                            if (r.getActivo() == JsfUtil.TRUE) {
                                setEnvioMensaje(Boolean.TRUE);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            estado = Estados.DATOS;
            usuario = documento = clave1 = clave2 = vacio;
        } else {
            estado = Estados.ERROR;
        }
    }

    public void solicitarCodigo() {
        String msjValidacion = vacio;
        if (vw != null) {
            if (celular != null && !vacio.equals(celular.trim())) {
                SbUsuario u = vw.getUsuarioId();
                SbPersona p = u.getPersonaId();
                String numDoc;
                if (p.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    numDoc = p.getRuc();
                } else {
                    numDoc = p.getNumDoc();
                }
                if (!documento.trim().equals(numDoc)) {
                    msjValidacion = JsfUtil.bundle("MensajeErrorDocumentoIn");
                } else if (!usuario.trim().equals(vw.getUsuarioId().getLogin())) {
                    msjValidacion = JsfUtil.bundle("MensajeErrorUsuarioIn");
                }
            } else {
                msjValidacion = JsfUtil.bundle("ActivarCuentaRequiredMessage_Celular");
            }
        }
        try {
            if (vacio.equals(msjValidacion)) {
                if (celular != null && celular.length() == 9 && celular.startsWith("9")) {
                    Consulta_Service serR = new Consulta_Service();
                    Consulta port = serR.getConsultaPort();
                    enviado = Boolean.FALSE;
                    codigoGenerado = vacio;
                    for (int i = 0; i < 5; i++) {
                        int aleatorio = (int) (Math.random() * 9 + 0);
                        codigoGenerado += String.valueOf(aleatorio);
                    }
                    String mensaje = JsfUtil.bundle("MensajeCodigoVerificacion") + espacio + codigoGenerado;
                    enviado = port.enviaSMS(mensaje, celular);
                    if (enviado) {
                        activarLink = Boolean.TRUE;
                        RequestContext.getCurrentInstance().update("formActivar:commLSolicitar");
                        ejecutarTimer();
                        JsfUtil.mensaje(JsfUtil.bundle("MensajeCodigoEnviado1") + espacio + celular + JsfUtil.bundle("MensajeCodigoEnviado2"));
                    } else {
                        JsfUtil.mensajeError(JsfUtil.bundle("MensajeErrorEnvioCodigo"));
                    }
                } else {
                    JsfUtil.mensajeError(JsfUtil.bundle("MensajeNumeroCelularNoValido"));
                }
            } else {
                JsfUtil.mensajeError(msjValidacion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeServicioNoDisponible"));
        }
    }

    public void ejecutarTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                activarLink = Boolean.FALSE;
                //RequestContext.getCurrentInstance().update("formActivar:commLSolicitar");
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 8000);
    }

    public ActivarCuentaController() {
        estado = Estados.INICIO;
    }

}
