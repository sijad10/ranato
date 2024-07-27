/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.seguridad;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.transaction.Transactional;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbMedioContactoFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbPaisFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbPerfilFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbRegistroUsuarioFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbMedioContactoGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRegistroUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.SbValidacionWebGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.sistemabase.beans.SbMedioContactoFacade;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
import pe.gob.sucamec.sistemabase.beans.SbValidacionWebFacadeGt;
import pe.gob.sucamec.sistemabase.data.SbMedioContacto;
import pe.gob.sucamec.validaqr.jsf.util.Captcha;
import pe.gob.sucamec.validaqr.jsf.util.JsfUtil;
import wspide.Consulta;
import wspide.Consulta_Service;

/**
 *
 * @author gchavez
 */
@Named("inscripcionAccesoController")
@SessionScoped
public class InscripcionAccesoController implements Serializable {

    private static final long serialVersionUID = -215238965323276L;
    private DatosInscripcion datos;
    private Captcha captcha;
    private List<TipoBaseGt> listTipoDoc;
    private List<TipoBaseGt> listGenero;
    private List<TipoBaseGt> listEstCivil;
    private List<TipoBaseGt> listOcupacion;
    private List<TipoBaseGt> listTipoVia;
    private Boolean flagAceptar;
    private String msjTerminosYCond;
    private String cantNumeros;
    private String msjErrorWsPide;
    private Boolean existePerReniec;
    private Boolean existePerBD;
    private SbUsuarioGt usuario;
    private SbRegistroUsuarioGt regUsuario;
    private SbPersonaGt persona;
    private Date fechaMaxima;
    private static final String vacio = "";
    private static final String espacio = " ";
    private String mensajeExito;
    private String mensajeError;
    private Estados estado;
    //private Boolean maternoOblg;

    @EJB
    private TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private SbDistritoFacadeGt ejbSbDistritoFacade;
    @EJB
    private SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private SbDireccionFacadeGt ejbSbDireccionFacadeGt;
    @EJB
    private SbRegistroUsuarioFacadeGt ejbSbRegistroUsuarioFacadeGt;
    @EJB
    private SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private SbValidacionWebFacadeGt ejbValidacionWebFacade;
    @EJB
    private SbPerfilFacadeGt ejbSbPerfilFacadeGt;
    @EJB
    private SbMedioContactoFacade ejbSbMedioContactoFacade;
    @EJB
    private SbMedioContactoFacadeGt ejbSbMedioContactoFacadeGt;
    @EJB
    private SbTipoFacade ejbSbTipoFacade;
    @EJB
    private SbPaisFacadeGt ejbSbPaisFacadeGt;

    /**
     * @return the estado
     */
    public Estados getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    /**
     * @return the mensajeExito
     */
    public String getMensajeExito() {
        return mensajeExito;
    }

    /**
     * @param mensajeExito the mensajeExito to set
     */
    public void setMensajeExito(String mensajeExito) {
        this.mensajeExito = mensajeExito;
    }

    /**
     * @return the mensajeError
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * @param mensajeError the mensajeError to set
     */
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public enum Estados {

        INICIO, FIN, ERROR
    };

    /**
     *
     */
    public InscripcionAccesoController() {
        inicializar();
    }

    private void inicializar() {
        datos = new DatosInscripcion();
        captcha = new Captcha();
        estado = Estados.INICIO;
    }

    public static enum TipoDoc {
        RUC, DNI
    }

    public void verificarCorreo() {
        if (datos != null && !"".equalsIgnoreCase(datos.getCorreoElec().trim())) {
            SbUsuarioGt temp = ejbSbUsuarioFacadeGt.obtenerUsuarioXCorreo(datos.getCorreoElec());
            if (temp != null) {
                JsfUtil.mensajeError("El correo ingresado ya se encuentra registrado en nuestra Base de Datos. Por favor ingrese otro correo.");
                datos.setCorreoElec(null);
            }
        }
    }

    public String registrarInscripcion() {
        setMensajeError(null);
        setMensajeExito(null);
        //String url = LoginFilter.LOGIN_URL;
        if (flagAceptar) {
            regresar();
            Boolean isTipoDocCE = Boolean.FALSE;
            Boolean existePersonaBD = Boolean.FALSE;
            existePerReniec = Boolean.FALSE;
            msjErrorWsPide = vacio;
            persona = ejbSbPersonaFacade.buscarPersonaSel(datos.getTipoDoc().getNombre(), datos.getNumDoc());
            if (persona != null) {
//                if ("TP_DOCID_CE".equalsIgnoreCase(persona.getTipoDoc().getCodProg())
//                        || ("TP_DOCID_DNI".equalsIgnoreCase(persona.getTipoDoc().getCodProg())
//                        && (persona.getNumDocVal() != null && persona.getNumDocVal().equals(datos.getNumCod())))) {
                existePersonaBD = Boolean.TRUE;
//                } else {
//                    JsfUtil.mensajeError(JsfUtil.bundle("MensajePersonaRegistradaDif"));
//                    return null;
//                }
            } else if ("TP_DOCID_DNI".equalsIgnoreCase(datos.getTipoDoc().getCodProg())) {
                if (JsfUtil.ValidarDniRuc(datos.getNumDoc() + datos.getNumCod(), JsfUtil.TipoDoc.DNI)) {
                    buscarReniec();
                } else {
                    JsfUtil.mensajeError("El Número B que corresponde al número de documento, no es válido.");
                    return null;
                }
            } else if ("TP_DOCID_CE".equalsIgnoreCase(datos.getTipoDoc().getCodProg())) {
                isTipoDocCE = Boolean.TRUE;
            }
            if (existePerReniec || isTipoDocCE) {
                try {
                    persona = setearDatosPersona();
                    ejbSbPersonaFacade.create(persona);
                    if (persona.getId() != null) {
                        registrarMedioContacto();
                        registrarDireccion();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
                    return null;
                }
            }
            if (!vacio.equals(msjErrorWsPide)) {
                JsfUtil.mensajeError(msjErrorWsPide);
                return null;
            }
            if (persona.getId() != null) {
                if (!ejbSbUsuarioFacadeGt.existeUsuario(persona.getNumDoc(), persona.getTipoDoc().getNombre(), persona.getNumDoc())) {
                    if (existePersonaBD) {
                        actualizarDatosPersona();
                    }
                    setearDatosUsuario();
                    crearUsuario();
                } else {
                    usuario = ejbSbUsuarioFacadeGt.usuInactivoPersonaPorIdPersona(persona.getId());
                    if (usuario != null) {
                        regUsuario = ejbSbRegistroUsuarioFacadeGt.obtenerRegUsuarioPorIdUsu(usuario.getId());
                        SbValidacionWebGt vw = ejbValidacionWebFacade.obtenerValidacionPorIdUsuario(usuario.getId());
                        if (vw != null) {
                            if (correoNuevo()) {
                                setMensajeExito(JsfUtil.bundle("MensajeRegistroCreado") + espacio + JsfUtil.bundle("RecuperarClave_Texto3"));
                                //JsfUtil.mensaje(JsfUtil.bundle("MensajeCorreoEnviado"));
                            } else {
                                setMensajeError(JsfUtil.bundle("ErrorEnvioCorreo"));
                                return null;
                            }
                        } else {
                            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeUsuarioRegistrado"));
                            return null;
                        }
                    } else {
                        JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeUsuarioRegistrado"));
                        return null;
                    }
                }
            }
        } else {
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeDebeAceptarCondiciones"));
            return null;
        }
        if (getMensajeExito() == null) {
            //JsfUtil.mensajeError(mensajeError);
            estado = Estados.ERROR;
            RequestContext.getCurrentInstance().update("formInscAcceso");
            return null;
        } else {
            //datos = null;
            persona = null;
            usuario = null;
            regUsuario = null;
            inicializar();
            //JsfUtil.mensaje(mensajeExito);
            estado = Estados.FIN;
            RequestContext.getCurrentInstance().update("formInscAcceso");
            return null;
        }
    }

    public void actualizarDatosPersona() {
        List<SbMedioContactoGt> listMedio = persona.getSbMedioContactoList();
        List<SbDireccionGt> listDireccion = persona.getSbDireccionList();
        if (listMedio != null && !listMedio.isEmpty()) {
            for (SbMedioContactoGt mc : listMedio) {
                if ("TP_MEDCON_COR".equalsIgnoreCase(mc.getTipoId().getCodProg())) {
                    mc.setValor(datos.getCorreoElec());
                } else if ("TP_MEDCON_MOV".equalsIgnoreCase(mc.getTipoId().getCodProg())) {
                    mc.setValor(datos.getNumCelular());
                } else if ("TP_MEDCON_FIJ".equalsIgnoreCase(mc.getTipoId().getCodProg()) && (datos.getNumTelf() != null && !vacio.equals(datos.getNumTelf()))) {
                    mc.setValor(datos.getNumTelf());
                }
                mc = (SbMedioContactoGt) JsfUtil.entidadMayusculas(mc, vacio);
                ejbSbMedioContactoFacadeGt.edit(mc);
            }
            persona.setSbMedioContactoList(listMedio);
        } else {
            persona.setSbMedioContactoList(registrarMedioContacto());
        }
        if (listDireccion != null && !listDireccion.isEmpty()) {
            for (SbDireccionGt dir : listDireccion) {
                dir.setNumero(datos.getNumero());
                dir.setDireccion(datos.getDireccion());
                dir.setTipoId(datos.getTipoDoc());
                dir.setReferencia(datos.getReferencia());
                dir.setDistritoId(datos.getUbigeo());
                dir = (SbDireccionGt) JsfUtil.entidadMayusculas(dir, vacio);
                ejbSbDireccionFacadeGt.edit(dir);
                break;
            }
            persona.setSbDireccionList(listDireccion);
        } else {
            persona.setSbDireccionList(registrarDireccion());
        }
        persona = setearDatosPersona();
        ejbSbPersonaFacade.edit(persona);
    }

    public List<SbMedioContactoGt> registrarMedioContacto() {
        List<SbMedioContacto> listTemp = new ArrayList<>();
        SbMedioContacto medio = null;
        Boolean registrar = Boolean.TRUE;
        for (int i = 0; i < 3; i++) {
            medio = new SbMedioContacto();
            switch (i) {
                case 0:
                    medio.setValor(datos.getCorreoElec());
                    medio.setTipoPrioridad(ejbSbTipoFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                    medio.setTipoId(ejbSbTipoFacade.tipoBaseXCodProg("TP_MEDCON_COR"));
                    break;
                case 1:
                    medio.setValor(datos.getNumCelular());
                    medio.setTipoPrioridad(ejbSbTipoFacade.tipoBaseXCodProg("TP_CONTAC_PRIN"));
                    medio.setTipoId(ejbSbTipoFacade.tipoBaseXCodProg("TP_MEDCON_MOV"));
                    break;
                case 2:
                    if (!vacio.equals(datos.getNumTelf()) && datos.getNumTelf() != null) {
                        medio.setValor(datos.getNumTelf());
                        medio.setTipoPrioridad(ejbSbTipoFacade.tipoBaseXCodProg("TP_CONTAC_SEC"));
                        medio.setTipoId(ejbSbTipoFacade.tipoBaseXCodProg("TP_MEDCON_FIJ"));
                    } else {
                        registrar = Boolean.FALSE;
                    }
                    break;
                default:
                    break;
            }
            if (registrar) {
                medio.setActivo((short) 1);
                medio.setPersonaId(persona);
                medio.setAudLogin(persona.getNumDoc());
                medio.setAudNumIp(JsfUtil.getNumIP());
                medio = (SbMedioContacto) JsfUtil.entidadMayusculas(medio, vacio);
                ejbSbMedioContactoFacade.create(medio);
                listTemp.add(medio);
            }
        }
        return convertirMedioAMedioGt(listTemp);
    }

    public List<SbMedioContactoGt> convertirMedioAMedioGt(List<SbMedioContacto> listIn) {
        List<SbMedioContactoGt> listRes = new ArrayList<>();
        for (SbMedioContacto medio : listIn) {
            listRes.add(ejbSbMedioContactoFacadeGt.find(medio.getId()));
        }
        return listRes;
    }

    public List<SbDireccionGt> registrarDireccion() {
        List<SbDireccionGt> listRes = new ArrayList<>();
        SbDireccionGt direccion = new SbDireccionGt();
        direccion.setDireccion(datos.getDireccion());
        direccion.setDistritoId(datos.getUbigeo());
        direccion.setNumero(datos.getNumero());
        direccion.setPersonaId(persona);
        direccion.setReferencia(datos.getReferencia());
        direccion.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DIRECB_PRO"));
        direccion.setZonaId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_ZON_URB"));
        direccion.setPaisId(ejbSbPaisFacadeGt.find(Long.valueOf(36)));
        direccion.setViaId(datos.getTipoVia());
        direccion.setActivo((short) 1);
        direccion.setAudLogin(datos.getNumDoc());
        direccion.setAudNumIp(JsfUtil.getNumIP());
        direccion = (SbDireccionGt) JsfUtil.entidadMayusculas(direccion, vacio);
        ejbSbDireccionFacadeGt.create(direccion);
        listRes.add(direccion);
        return listRes;
    }

    public SbPersonaGt setearDatosPersona() {
        SbPersonaGt personaRes = null;
        if (persona != null) {
            personaRes = persona;
        } else {
            personaRes = new SbPersonaGt();
            personaRes.setTipoDoc(datos.getTipoDoc());
            personaRes.setNumDoc(datos.getNumDoc());
            personaRes.setNumDocVal(datos.getNumCod());
            personaRes.setGeneroId(datos.getGenero());
            personaRes.setFechaNac(datos.getFechaNac());
            personaRes.setFechaReg(new Date());
            personaRes.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
            personaRes.setActivo((short) 1);
            personaRes.setAudLogin(datos.getNumDoc());
            personaRes.setAudNumIp(JsfUtil.getNumIP());
            personaRes.setNombres(datos.getNombres());
            personaRes.setApePat(datos.getApePat());
            personaRes.setApeMat(datos.getApeMat());
        }
        personaRes.setEstCivilId(datos.getEstCivil());
        personaRes.setOcupacionId(datos.getOcupacion());
        personaRes = (SbPersonaGt) JsfUtil.entidadMayusculas(personaRes, vacio);
        return personaRes;
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void crearUsuario() {
        try {
            Calendar c = Calendar.getInstance();
            Date ahora = c.getTime();
            c.add(Calendar.DATE, 15); // Agregar 15 días //
            Date fechaFin = c.getTime();
            TipoBaseGt tipoSol = ejbTipoBaseFacade.tipoBaseXCodProg("TP_SOL_AD");
            usuario = (SbUsuarioGt) JsfUtil.entidadMayusculas(usuario, vacio);
            regUsuario = new SbRegistroUsuarioGt();
            regUsuario = (SbRegistroUsuarioGt) JsfUtil.entidadMayusculas(regUsuario, vacio);
            // Datos Usuario //
            usuario.setId(null);
            usuario.setClave("X");
            usuario.setFechaIni(ahora);
            usuario.setFechaFin(fechaFin);
            usuario.setFormaRegistroId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_TIPUSU_WEB"));
            usuario.setTipoAutenId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_AUT_BD"));
            usuario.setLogin(usuario.getNumDoc());
            usuario = (SbUsuarioGt) JsfUtil.entidadMayusculas(usuario, vacio);
            ejbSbUsuarioFacadeGt.create(usuario);

            // Datos Registro Usuario //
            regUsuario.setId(null);
            regUsuario.setActivo((short) 1);
            regUsuario.setTipoSolicitante(tipoSol.getNombre());
            regUsuario.setEmailAcceso(usuario.getCorreo());
            regUsuario.setFechaDoc(ahora);
            regUsuario.setFechaPrc(ahora);
            regUsuario.setUsuarioId(usuario.getUsuarioId());
            regUsuario.setActivo((short) 1);
            regUsuario.setUsuario(usuario.getLogin());
            //if (tipoSol.getCodProg().equals("TP_SOL_AD")) {
            //usuario.setCantidadUsuario(1L);
            regUsuario.setAdministradoNombres(usuario.getNombres() + espacio + usuario.getApePat() + espacio + usuario.getApeMat());
            regUsuario.setAdministradoNroDoc(usuario.getNumDoc());
            regUsuario.setAdministradoTipoDoc(usuario.getPersonaId().getTipoDoc().getNombre());
            regUsuario.setSolicitanteNombres(regUsuario.getAdministradoNombres());
            regUsuario.setSolicitanteNroDoc(regUsuario.getAdministradoNroDoc());
            regUsuario.setSolicitanteTipoDoc(regUsuario.getAdministradoTipoDoc());
            regUsuario.setAudLogin(usuario.getNumDoc());
            regUsuario.setAudNumIp(JsfUtil.getNumIP());
            //}
            // Agregar tabla detalle //
            regUsuario.setUsuarioId(usuario); // Importante
            regUsuario = (SbRegistroUsuarioGt) JsfUtil.entidadMayusculas(regUsuario, vacio);
            ejbSbRegistroUsuarioFacadeGt.create(regUsuario);
            usuario.setSbPerfilList(Arrays.asList(ejbSbPerfilFacadeGt.perfilCodProg("SEL_NOTIF")));
            usuario.setSbRegistroUsuarioList(Arrays.asList(regUsuario));
            ejbSbUsuarioFacadeGt.edit(usuario);
            if (correoNuevo()) {
                setMensajeExito(JsfUtil.bundle("MensajeRegistroCreado") + espacio + JsfUtil.bundle("RecuperarClave_Texto3"));
                //JsfUtil.mensaje(JsfUtil.bundle("MensajeCorreoEnviado"));
            } else {
                setMensajeError(JsfUtil.bundle("ErrorEnvioCorreo"));
                return;
            }
            //JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado"));
            mostrarVerCredenciales();
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    public Boolean setearDatosUsuario() {
        usuario = new SbUsuarioGt();
        usuario.setPersonaId(persona);
        usuario.setApePat(persona.getApePat());
        usuario.setApeMat(persona.getApeMat());
        usuario.setCantidadUsuario(0l);
        usuario.setNombres(persona.getNombres());
        usuario.setNumDoc(persona.getNumDoc());
        usuario.setTipoAutenId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_AUT_BD"));
        usuario.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_USR_MA"));
        usuario.setCorreo(datos.getCorreoElec());
        usuario.setDescripcion("Usuario externo");
        usuario.setActivo((short) 0);
        usuario.setAudLogin(persona.getNumDoc());
        usuario.setAudNumIp(JsfUtil.getNumIP());
        return true;
    }

    public Boolean correoNuevo() {
        Boolean res = Boolean.FALSE;
        //Crear validacion Web //
        if (usuario != null && regUsuario != null) {
            ejbValidacionWebFacade.desactivarValidacionesWeb(usuario);
            String hash = ejbValidacionWebFacade.crearValidacionWebGt(ejbTipoBaseFacade.tipoBaseXCodProg("TP_VW_NU"), usuario);
            //Preparar Correo
            String mensaje = "Estimado(a) " + usuario.getNombres() + espacio + usuario.getApePat() + espacio
                    + usuario.getApeMat() + ".\n\n" + JsfUtil.bundle("Correo_CuerpoNuevo")
                    + "\n" + "Enlace de activación: " + "\n" + JsfUtil.bundle("Correo_UrlActivacion") + hash
                    + "\n\n" + "Credenciales:" + "\n" + "TIPO DE DOCUMENTO: " + regUsuario.getAdministradoTipoDoc()
                    + " \n" + "NRO: " + regUsuario.getAdministradoNroDoc() + " \n" + "USUARIO: " + usuario.getLogin()
                    + " \n\n" + JsfUtil.bundle("Correo_CuerpoNuevo2") + "\n"
                    + JsfUtil.bundle("Correo_UrlWeb");
            res = JsfUtil.enviarCorreo(usuario.getCorreo(), JsfUtil.bundle("Correo_AsuntoNuevo"), mensaje);
            JsfUtil.enviarCorreoGmail(usuario.getCorreo(), JsfUtil.bundle("Correo_AsuntoNuevo"), mensaje);
        }
        return res;
    }

    public void mostrarVerCredenciales() {
        usuario = ejbSbUsuarioFacadeGt.find(usuario.getId());
        List<SbRegistroUsuarioGt> l = usuario.getSbRegistroUsuarioList();
        if (l == null || l.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorRegistroDatosUsr"));
            return;
        } else {
            regUsuario = l.get(0);
        }
    }

    public List<SbDistritoGt> completeUbigeo(String query) {
        List<SbDistritoGt> filteredUbigeo = new ArrayList<>();
        List<SbDistritoGt> lstUbigeo = ejbSbDistritoFacade.obtenerUbigeo(query);
        for (SbDistritoGt d : lstUbigeo) {
            if (d.getNombre().contains(query.toUpperCase())
                    || d.getProvinciaId().getNombre().contains(query.toUpperCase())
                    || d.getProvinciaId().getDepartamentoId().getNombre().contains(query.toUpperCase())) {
                filteredUbigeo.add(d);
            }
        }
        return filteredUbigeo;
    }

    /**
     * EVENTO PARA ABRIR FORMULARIO DE CONFIRMACION
     *
     * @author Gino Chávez
     * @version 2.0
     */
    public void openTerminos() {
        if (documentoOk(datos.getNumDoc().length())) {
            Boolean ok = captcha.ok();
            if (ok) {
                //setMsjTerminosYCond("Los terminos y condiciones son los siguientes");
                RequestContext.getCurrentInstance().execute("PF('TermCondViewDialog').show()");
                RequestContext.getCurrentInstance().update("formMensajeTerminos");
            } else {
                JsfUtil.mensajeError(JsfUtil.bundle("Captcha_Invalido"));
                JsfUtil.invalidar("validaForm:textoCaptcha");
                setFlagAceptar((Boolean) false);
            }
        } else {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeValidacionNumeroTipoDocumento"));
        }
    }

    public void openAyudaDNI() {
        RequestContext.getCurrentInstance().execute("PF('ImagenDNIViewDialog').show()");
        RequestContext.getCurrentInstance().update("formDNI");
    }

    public void cerrarAyuda() {
        RequestContext.getCurrentInstance().execute("PF('ImagenDNIViewDialog').hide()");
    }

    public void regresar() {
        RequestContext.getCurrentInstance().execute("PF('TermCondViewDialog').hide()");
        setFlagAceptar((Boolean) false);
    }

    public void validarDatoIngresado(String in) {
        String tipo = vacio;
        int tam = 0;
        String valor = vacio;
        String idCampo = vacio;
        switch (in) {
            case "NUMDOC":
                if (datos.getNumDoc() != null && !vacio.equals(datos.getNumDoc())) {
                    tam = datos.getNumDoc().length();
                    valor = datos.getNumDoc();
                    tipo = "N";
                }
                idCampo = "numDoc";
                break;
            case "NOM":
                if (datos.getNombres() != null && !vacio.equals(datos.getNombres())) {
                    tam = datos.getNombres().length();
                    valor = datos.getNombres();
                    tipo = "L";
                }
                idCampo = "nomb";
                break;
            case "APPAT":
                if (datos.getApePat() != null && !vacio.equals(datos.getApePat())) {
                    tam = datos.getApePat().length();
                    valor = datos.getApePat();
                    tipo = "L";
                }
                idCampo = "appat";
                break;
            case "APMAT":
                if (datos.getApeMat() != null && !vacio.equals(datos.getApeMat())) {
                    tam = datos.getApeMat().length();
                    valor = datos.getApeMat();
                    tipo = "L";
                }
                idCampo = "apmat";
                break;
            case "NUMDIR":
                if (datos.getNumero() != null && !vacio.equals(datos.getNumero())) {
                    tam = datos.getNumero().length();
                    valor = datos.getNumero();
                    tipo = "N";
                }
                idCampo = "numero";
                break;
            case "CEL":
                if (datos.getNumCelular() != null && !vacio.equals(datos.getNumCelular())) {
                    tam = datos.getNumCelular().length();
                    valor = datos.getNumCelular();
                    tipo = "N";
                }
                idCampo = "celular";
                break;
            case "TEL":
                if (datos.getNumTelf() != null && !vacio.equals(datos.getNumTelf())) {
                    tam = datos.getNumTelf().length();
                    valor = datos.getNumTelf();
                    tipo = "N";
                }
                idCampo = "telefono";
                break;
            default:
                break;
        }
        String nomCampo = vacio;
        Boolean isLetra = Boolean.TRUE;
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
                //return;
            }
        } else {
            for (int i = 0; i < tam; i++) {
                if (!Character.isLetter(valor.charAt(i))
                        && !Character.isSpaceChar(valor.charAt(i))) {
                    isLetra = Boolean.FALSE;
                    break;
                }
            }
            if (!isLetra) {
                nomCampo = limpiarObtenerCampo(in);
                JsfUtil.mensajeAdvertencia("El campo " + nomCampo + " debe contener solamente letras");
            }
        }
        if (!isLetra || !isNumero) {
            RequestContext.getCurrentInstance().update("formInscAcceso:" + idCampo);
            return;
        } else if ("NUMDOC".equalsIgnoreCase(in)) {
            if (!documentoOk(tam)) {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeValidacionNumeroTipoDocumento"));
                return;
            }
        }
    }

    public Boolean documentoOk(int tam) {
        Boolean res = Boolean.TRUE;
        if ("TP_DOCID_DNI".equalsIgnoreCase(datos.getTipoDoc().getCodProg())
                && tam < 8) {
            res = Boolean.FALSE;
        }
        if ("TP_DOCID_CE".equalsIgnoreCase(datos.getTipoDoc().getCodProg())
                && tam < 9) {
            res = Boolean.FALSE;
        }
        return res;
    }

    public String limpiarObtenerCampo(String campo) {
        String nomCampo = vacio;
        switch (campo) {
            case "NUMDOC":
                datos.setNumDoc(null);
                nomCampo = "Número de documento";
                break;
            case "NOM":
                datos.setNombres(null);
                nomCampo = "Nombres";
                break;
            case "APPAT":
                datos.setApePat(null);
                nomCampo = "Apellido paterno";
                break;
            case "APMAT":
                datos.setApeMat(null);
                nomCampo = "Apellido materno";
                break;
            case "NUMDIR":
                datos.setNumero(null);
                nomCampo = "Dirección - número";
                break;
            case "CEL":
                datos.setNumCelular(null);
                nomCampo = "Número de celular";
                break;
            case "TEL":
                datos.setNumTelf(null);
                nomCampo = "Número de teléfono";
                break;
            default:
                break;
        }
        return nomCampo;
    }

    public void obtenerNumCaract() {
        if (datos.getTipoDoc() != null) {
            if ("TP_DOCID_DNI".equalsIgnoreCase(datos.getTipoDoc().getCodProg())) {
                //maternoOblg = Boolean.TRUE;
                cantNumeros = "8";
            } else if ("TP_DOCID_CE".equalsIgnoreCase(datos.getTipoDoc().getCodProg())) {
                cantNumeros = "9";
                //maternoOblg = Boolean.FALSE;
            }
            datos.setNumDoc(null);
        }
    }

    public void buscarReniec() {
        try {
            Consulta_Service serR = new Consulta_Service();
            Consulta port = serR.getConsultaPort();
            wspide.Persona p = null;
            p = port.consultaDNI(datos.getNumDoc());
            datos.setApePat(null);
            datos.setApeMat(null);
            datos.setNombres(null);
            if (p != null) {
                datos.setApePat(p.getAPPAT());
                datos.setApeMat(p.getAPMAT());
                datos.setNombres(p.getNOMBRES());
                if (p.getFENAC() != null) {
                    Date date = null;
                    try {
                        date = obtenerFechaDeString(p.getFENAC().substring(6, 8), p.getFENAC().substring(4, 6), p.getFENAC().substring(0, 4));
                    } catch (Exception ex) {
                        date = null;
                    }
                    if (date != null) {
                        datos.setFechaNac(date);
                    }
                }
                if (p.getSEXO() != null) {
                    datos.setGenero(null);
                    if ("F".equals(p.getSEXO())) {
                        datos.setGenero(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GEN_FEM"));
                    } else {
                        datos.setGenero(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GEN_MAS"));
                    }
                }
                existePerReniec = Boolean.TRUE;
            } else {
                msjErrorWsPide = JsfUtil.bundle("MensajeServicioConsultaReniecNulo");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msjErrorWsPide = JsfUtil.bundle("MensajeServicioReniecNoDisponible");
        }
    }

    public Date obtenerFechaDeString(String dia, String mes, String anio) {
        Date fecha = null;
        try {
            if (anio != null && mes != null && dia != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateInString = dia + "/" + mes + "/" + anio;
                fecha = formatter.parse(dateInString);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fecha;
    }

    public String direccionarLogin() {
        datos = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        estado = Estados.INICIO;
        return "/login.xhtml?faces-redirect=true";
    }

    public void volverInicio() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        estado = Estados.INICIO;
    }

    /**
     * @return the datos
     */
    public DatosInscripcion getDatos() {
        return datos;
    }

    /**
     * @param datos the datos to set
     */
    public void setDatos(DatosInscripcion datos) {
        this.datos = datos;
    }

    /**
     * @return the captcha
     */
    public Captcha getCaptcha() {
        return captcha;
    }

    /**
     * @param captcha the captcha to set
     */
    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }

    /**
     * @return the listTipoDoc
     */
    public List<TipoBaseGt> getListTipoDoc() {
        List<TipoBaseGt> listTBTemp = new ArrayList<>();
        listTipoDoc = ejbTipoBaseFacade.lstTipoBase("TP_DOCID");
        listTBTemp.addAll(listTipoDoc);
        for (TipoBaseGt tb : listTBTemp) {
            if ("TP_DOCID_RUC".equalsIgnoreCase(tb.getCodProg())
                    || "TP_DOCID_EXT".equalsIgnoreCase(tb.getCodProg())) {
                listTipoDoc.remove(tb);
            }
        }
        return listTipoDoc;
    }

    /**
     * @param listTipoDoc the listTipoDoc to set
     */
    public void setListTipoDoc(List<TipoBaseGt> listTipoDoc) {
        this.listTipoDoc = listTipoDoc;
    }

    /**
     * @return the listGenero
     */
    public List<TipoBaseGt> getListGenero() {
        return ejbTipoBaseFacade.lstTipoBase("TP_GEN");
    }

    /**
     * @param listGenero the listGenero to set
     */
    public void setListGenero(List<TipoBaseGt> listGenero) {
        this.listGenero = listGenero;
    }

    /**
     * @return the listEstCivil
     */
    public List<TipoBaseGt> getListEstCivil() {
        return ejbTipoBaseFacade.lstTipoBase("TP_ECIV");
    }

    /**
     * @param listEstCivil the listEstCivil to set
     */
    public void setListEstCivil(List<TipoBaseGt> listEstCivil) {
        this.listEstCivil = listEstCivil;
    }

    /**
     * @return the listOcupacion
     */
    public List<TipoBaseGt> getListOcupacion() {
        return ejbTipoBaseFacade.lstTipoBase("TP_OCUPA");
    }

    /**
     * @param listOcupacion the listOcupacion to set
     */
    public void setListOcupacion(List<TipoBaseGt> listOcupacion) {
        this.listOcupacion = listOcupacion;
    }

    /**
     * @return the listTipoVia
     */
    public List<TipoBaseGt> getListTipoVia() {
        return ejbTipoBaseFacade.lstTipoBase("TP_VIA");
    }

    /**
     * @param listTipoVia the listTipoVia to set
     */
    public void setListTipoVia(List<TipoBaseGt> listTipoVia) {
        this.listTipoVia = listTipoVia;
    }

    /**
     * @return the flagAceptar
     */
    public Boolean getFlagAceptar() {
        return flagAceptar;
    }

    /**
     * @param flagAceptar the flagAceptar to set
     */
    public void setFlagAceptar(Boolean flagAceptar) {
        this.flagAceptar = flagAceptar;
    }

    /**
     * @return the msjTerminosYCond
     */
    public String getMsjTerminosYCond() {
        return msjTerminosYCond;
    }

    /**
     * @param msjTerminosYCond the msjTerminosYCond to set
     */
    public void setMsjTerminosYCond(String msjTerminosYCond) {
        this.msjTerminosYCond = msjTerminosYCond;
    }

    /**
     * @return the cantNumeros
     */
    public String getCantNumeros() {
        return cantNumeros;
    }

    /**
     * @param cantNumeros the cantNumeros to set
     */
    public void setCantNumeros(String cantNumeros) {
        this.cantNumeros = cantNumeros;
    }

    /**
     * @return the usuario
     */
    public SbUsuarioGt getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(SbUsuarioGt usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the fechaMaxima
     */
    public Date getFechaMaxima() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis((new Date()).getTime());
        cal.add(Calendar.DATE, -365 * 18);
        fechaMaxima = new Date(cal.getTimeInMillis());
        return fechaMaxima;
    }

    /**
     * @param fechaMaxima the fechaMaxima to set
     */
    public void setFechaMaxima(Date fechaMaxima) {
        this.fechaMaxima = fechaMaxima;
    }
}
