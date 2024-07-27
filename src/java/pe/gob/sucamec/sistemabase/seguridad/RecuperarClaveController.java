package pe.gob.sucamec.sistemabase.seguridad;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
import pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade;
import pe.gob.sucamec.sistemabase.beans.SbValidacionWebFacade;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.data.SbValidacionWeb;
import pe.gob.sucamec.validaqr.jsf.util.JsfUtil;

@Named("recuperarClaveController")
@SessionScoped
public class RecuperarClaveController implements Serializable {

    public enum Estados {
        INICIO, DATOS, FIN, NUEVA_CLAVE, FIN_CLAVE, ERROR
    };

    private Estados estado;
    private String hash, documento, usuario, clave1, clave2, correo, datosUsuario, tipoDoc, msgError;
    private SbValidacionWeb vw;

    private static final long serialVersionUID = -215238965323276L;
    @EJB
    private SbValidacionWebFacade validacionWebFacade;
    @EJB
    private SbUsuarioFacade usuarioFacade;
    @EJB
    private SbTipoFacade tipoBaseFacade;

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

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
        setMsgError(JsfUtil.bundle("RecuperarClave_Error1"));
        if (hash == null || hash.equals("")) {
            estado = Estados.ERROR;
            return;
        } else {
            vw = validacionWebFacade.buscarPorHashValido(hash);
            if (vw == null) {
                estado = Estados.ERROR;
                return;
            } else {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, -1);  // number of days to add
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dt = sdf.format(c.getTime()) + " 23:59:59";  // dt is now the new date
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    c.setTime(sdf2.parse(dt));                    
                } catch (Exception e) {
                    
                }
                //Syso(vw.getFechaFin() + " before " + c.getTime());
                if (vw.getFechaFin().before(c.getTime())) {
                    setMsgError(JsfUtil.bundle("RecuperarClave_Error2"));
                    estado = Estados.ERROR;
                    return;
                }
                if (vw.getActivo()==0) {
                    setMsgError(JsfUtil.bundle("RecuperarClave_Error3"));
                    estado = Estados.ERROR;
                    return;
                }
                
            }
            estado = Estados.NUEVA_CLAVE;
        }
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public void volverInicio() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        estado = Estados.INICIO;
    }

    public String direccionarLogin() {
        volverInicio();
        return "/login.xhtml?faces-redirect=true";
    }

    public void solicitarCambioClave() {
        if (tipoDoc.equals("RUC") && !JsfUtil.ValidarDniRuc(documento, JsfUtil.TipoDoc.RUC)) {
            JsfUtil.invalidar("formRecuperar:documento");
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorValidacionRuc"));
            return;
        }
        if (tipoDoc.equals("DNI") && documento.length() != 8) {
            JsfUtil.invalidar("formRecuperar:documento");
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorValidacionDni"));
            return;
        }
        try {
            List<SbUsuario> l = usuarioFacade.selectUsuarioExterno(usuario, tipoDoc, documento);
            if (!l.isEmpty() ) {
                SbUsuario u = l.get(0);
                Date hoy = new Date();
                if(!((u.getFechaIni().getTime() < hoy.getTime())
                      && ((u.getFechaFin() == null)
                      || ((u.getFechaFin() != null) && (u.getFechaFin().getTime() > hoy.getTime()))) 
                     )
                   ){
                    JsfUtil.mensajeError("El usuario no existe o se encuentra inactivo");
                    estado = Estados.ERROR;
                    msgError = "El usuario no existe o se encuentra inactivo";
                    return;
                }
                if (correo.equalsIgnoreCase(u.getCorreo())) {
                    if (!correoNuevo(u)) {
                        JsfUtil.mensajeError(JsfUtil.bundle("RecuperarClave_ErrorEnvio"));
                        return;
                    }
                }
            }else{
                estado = Estados.ERROR;
                msgError = "El usuario no existe o se encuentra inactivo";
                return;
            }
            estado = Estados.FIN;
        } catch (Exception e) {
            JsfUtil.invalidar("formRecuperar:documento");
            JsfUtil.mensajeError(JsfUtil.bundle("RecuperarClave_ErrorEnvio"));
        }
    }

    public boolean correoNuevo(SbUsuario u) {
        //Crear validacion Web //
        //validacionWebFacade.desactivarValidacionesWeb(u);
        String hash = validacionWebFacade.crearValidacionWeb(tipoBaseFacade.tipoPorCodProg("TP_VW_NU"), u);
        //Preparar Correo
        String mensaje = "Estimado(a) " + u.getNombres() + " " + u.getApePat() + " "
                + u.getApeMat() + ".\n\n" + JsfUtil.bundle("RecuperarClave_TextoCorreo")
                + "\n" + JsfUtil.bundle("RecuperarClave_Url") + hash
                + "\n\n";
        JsfUtil.enviarCorreoGmail(u.getCorreo(), JsfUtil.bundle("RecuperarClave_Asunto"), mensaje);
        return JsfUtil.enviarCorreo(u.getCorreo(), JsfUtil.bundle("RecuperarClave_Asunto"), mensaje);
    }

    public void cambiarClave() {
        if (!clave1.equals(clave2)) {
            JsfUtil.invalidar("formRecuperar:clave1");
            JsfUtil.invalidar("formRecuperar:clave2");
            JsfUtil.mensajeError(JsfUtil.bundle("ActivarCuentaValidator_claves"));
            return;
        }
        if (vw != null) {
            SbUsuario u = vw.getUsuarioId();
            // Actualizar Usuario //
            u.setActivo((short) 1);
            u.setFechaFin(null);
            u.setClave(JsfUtil.crearHash(clave1));
            usuarioFacade.edit(u);
            // Actualizar Verificacion //
            vw.setFechaVal(new Date());
            vw.setActivo((short) 0);
            validacionWebFacade.edit(vw);
            datosUsuario = u.getNombres() + " " + u.getApePat() + " " + u.getApeMat();
            validacionWebFacade.desactivarValidacionesWeb(u, vw.getId());
            estado = Estados.FIN_CLAVE;
            return;
        }
        estado = Estados.ERROR;
    }

    public void mostrarRecuperar() {
        documento = usuario = correo = "";
        estado = Estados.DATOS;
    }

    public RecuperarClaveController() {
        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        estado = Estados.INICIO;
    }

    @PostConstruct    
    public void construirController() {
        estado = Estados.INICIO;
        //Syso("estado:" + estado);
        //inicializar();
    }
}
