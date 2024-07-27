/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbExpVirtualAdjunto;
import pe.gob.sucamec.bdintegrado.data.SbExpVirtualRequisito;
import pe.gob.sucamec.bdintegrado.data.SbExpVirtualSolicitud;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbTupa;
import pe.gob.sucamec.bdintegrado.data.SbTupaRequisito;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sel.citas.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.citas.jsf.util.RenagiUploadFilesController;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.sel.citas.data.CitaTurTurno;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.citas.data.CitaTurLicenciaReg;
import pe.gob.sucamec.sel.citas.data.CitaTurComprobante;

/**
 *
 * @author msalinas
 */
@Named("tramiteVirtualController")
@SessionScoped
public class TramiteVirtualController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * ListSolicitudesTramVirtual
     */
    private String tipoFiltro;
    //private List<TipoBase> listaTipoFiltros = new ArrayList();
    private TipoBaseGt tipoEstadoSolicitud;
    private List<TipoBaseGt> listaTipoFiltros = new ArrayList();
    private String filtroLbl;
    private List<SbExpVirtualSolicitud> expVirtualSolicitudLst = new ArrayList();
    //variable para mostrar el link tutorial();
    private String linkTutorial;
    /**
     * CrearSolicitudTramVirtual
     */
    private SbExpVirtualSolicitud registro;
    //panel DATOS DEL SOLICITANTE
    private SbPersona personaLogeada;
    private List<SbDistrito> listDistritos = new ArrayList();
    private List<TipoBaseGt> listTipoTelefonoContacto = new ArrayList();
    private int maxLengthDoc;
    private int maxLengthDocPerRef;
    private SbPersonaGt solicitanteRegistro = new SbPersonaGt();
    private boolean renderPanelNuevoRep = false;

    //panel PROCESO TUPA
    private String procedimientoFiltro;
    private List<SbTupa> tupaLst = new ArrayList();
    private SbTupa tupaSelected;
    private boolean renderTablaProcedimiento = false;

    //panel PERSONA REF
    private boolean visiblePanelPersonaRef = false;

    //panel EMPOCE
    private boolean visiblePanelEmpoce = false;
    private Long empoceFiltro;
    private List<SbRecibos> recibosLst = new ArrayList();
    private SbRecibos reciboSelected;
    private String mensajeErrorEmpoce;

    //panel REQUISITOS
    private boolean visiblePanelRequisitos = false;
    private List<SbExpVirtualRequisito> expVirtualRequisitoLst = new ArrayList();

    //CHECKBOX
    private boolean visiblePanelNotificaciones = false;
    private boolean notificacionViaSelBoolean = false;
    private boolean visibleNotificacionAlertaCelularBoolean = false;
    private boolean notificacionAlertaCelularBoolean = false;

    //BOTONES
    private boolean visibleGuardar = false;
    private boolean visibleTransmitir = false;

    private String mensajeAvisoSede="";

    @Inject
    private LoginController loginController;
    @Inject
    private RenagiUploadFilesController upFilesCont;
    @Inject
    WsTramDoc wsTramDocController;
    @Inject
    WsPide wsPideController;

    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbPersonaFacade ejbSbPersonaFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbTipoFacade ejbSbTipoFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaSbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbDireccionFacade ejbSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbTupaFacade ejbSbTupaFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbDistritoFacade distritoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade tipoBaseGtFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade recibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbExpVirtualSolicitudFacade ejbSbExpVirtualSolicitudFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade ejbSbDerivacionCydocFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaGtFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurTurnoFacade ejbCitaTurnoFacade;
    @EJB
    private pe.gob.sucamec.sel.cydoc.beans.CdExpedienteFacade ejbCdExpedienteFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurLicenciaRegFacade ejbLicenciaRegFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurComprobanteFacade ejbTurComprobanteFacade;

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public TipoBaseGt getTipoEstadoSolicitud() {
        return tipoEstadoSolicitud;
    }

    public void setTipoEstadoSolicitud(TipoBaseGt tipoEstadoSolicitud) {
        this.tipoEstadoSolicitud = tipoEstadoSolicitud;
    }

    public boolean isRenderTablaProcedimiento() {
        return renderTablaProcedimiento;
    }

    public void setRenderTablaProcedimiento(boolean renderTablaProcedimiento) {
        this.renderTablaProcedimiento = renderTablaProcedimiento;
    }

    public List<TipoBaseGt> getListaTipoFiltros() {
        return listaTipoFiltros;
    }

    public void setListaTipoFiltros(List<TipoBaseGt> listaTipoFiltros) {
        this.listaTipoFiltros = listaTipoFiltros;
    }

    public String getFiltroLbl() {
        return filtroLbl;
    }

    public void setFiltroLbl(String filtroLbl) {
        this.filtroLbl = filtroLbl;
    }

    public List<SbExpVirtualSolicitud> getExpVirtualSolicitudLst() {
        return expVirtualSolicitudLst;
    }

    public void setExpVirtualSolicitudLst(List<SbExpVirtualSolicitud> expVirtualSolicitudLst) {
        this.expVirtualSolicitudLst = expVirtualSolicitudLst;
    }

    public TramiteVirtualController() {
    }

    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public String getProcedimientoFiltro() {
        return procedimientoFiltro;
    }

    public void setProcedimientoFiltro(String procedimientoFiltro) {
        this.procedimientoFiltro = procedimientoFiltro;
    }

    public List<SbTupa> getTupaLst() {
        return tupaLst;
    }

    public void setTupaLst(List<SbTupa> tupaLst) {
        this.tupaLst = tupaLst;
    }

    public SbTupa getTupaSelected() {
        return tupaSelected;
    }

    public void setTupaSelected(SbTupa tupaSelected) {
        this.tupaSelected = tupaSelected;
    }

    public SbPersona getPersonaLogeada() {
        return personaLogeada;
    }

    public void setPersonaLogeada(SbPersona personaLogeada) {
        this.personaLogeada = personaLogeada;
    }

    public List<SbDistrito> getListDistritos() {
        listDistritos = distritoFacade.listarDistritos();
        return listDistritos;
    }

    public void setListDistritos(List<SbDistrito> listDistritos) {
        this.listDistritos = listDistritos;
    }

    public List<TipoBaseGt> getListTipoTelefonoContacto() {
        listTipoTelefonoContacto = tipoBaseGtFacade.lstTelefonoContacto();
        return listTipoTelefonoContacto;
    }

    public void setListTipoTelefonoContacto(List<TipoBaseGt> listTipoTelefonoContacto) {
        this.listTipoTelefonoContacto = listTipoTelefonoContacto;
    }

    public SbExpVirtualSolicitud getRegistro() {
        return registro;
    }

    public void setRegistro(SbExpVirtualSolicitud registro) {
        this.registro = registro;
    }

    public Long getEmpoceFiltro() {
        return empoceFiltro;
    }

    public void setEmpoceFiltro(Long empoceFiltro) {
        this.empoceFiltro = empoceFiltro;
    }

    public List<SbRecibos> getRecibosLst() {
        return recibosLst;
    }

    public void setRecibosLst(List<SbRecibos> recibosLst) {
        this.recibosLst = recibosLst;
    }

    public SbRecibos getReciboSelected() {
        return reciboSelected;
    }

    public void setReciboSelected(SbRecibos reciboSelected) {
        this.reciboSelected = reciboSelected;
    }

    public String getMensajeErrorEmpoce() {
        return mensajeErrorEmpoce;
    }

    public void setMensajeErrorEmpoce(String mensajeErrorEmpoce) {
        this.mensajeErrorEmpoce = mensajeErrorEmpoce;
    }

    public List<SbExpVirtualRequisito> getExpVirtualRequisitoLst() {
        return expVirtualRequisitoLst;
    }

    public void setExpVirtualRequisitoLst(List<SbExpVirtualRequisito> expVirtualRequisitoLst) {
        this.expVirtualRequisitoLst = expVirtualRequisitoLst;
    }

    public boolean isVisiblePanelEmpoce() {
        return visiblePanelEmpoce;
    }

    public void setVisiblePanelEmpoce(boolean visiblePanelEmpoce) {
        this.visiblePanelEmpoce = visiblePanelEmpoce;
    }

    public boolean isVisiblePanelRequisitos() {
        return visiblePanelRequisitos;
    }

    public void setVisiblePanelRequisitos(boolean visiblePanelRequisitos) {
        this.visiblePanelRequisitos = visiblePanelRequisitos;
    }

    public boolean isVisiblePanelNotificaciones() {
        return visiblePanelNotificaciones;
    }

    public void setVisiblePanelNotificaciones(boolean visiblePanelNotificaciones) {
        this.visiblePanelNotificaciones = visiblePanelNotificaciones;
    }

    public boolean isNotificacionViaSelBoolean() {
        return notificacionViaSelBoolean;
    }

    public void setNotificacionViaSelBoolean(boolean notificacionViaSelBoolean) {
        this.notificacionViaSelBoolean = notificacionViaSelBoolean;
    }

    public boolean isVisibleNotificacionAlertaCelularBoolean() {
        return visibleNotificacionAlertaCelularBoolean;
    }

    public void setVisibleNotificacionAlertaCelularBoolean(boolean visibleNotificacionAlertaCelularBoolean) {
        this.visibleNotificacionAlertaCelularBoolean = visibleNotificacionAlertaCelularBoolean;
    }

    public boolean isNotificacionAlertaCelularBoolean() {
        return notificacionAlertaCelularBoolean;
    }

    public void setNotificacionAlertaCelularBoolean(boolean notificacionAlertaCelularBoolean) {
        this.notificacionAlertaCelularBoolean = notificacionAlertaCelularBoolean;
    }

    public boolean isVisibleGuardar() {
        return visibleGuardar;
    }

    public void setVisibleGuardar(boolean visibleGuardar) {
        this.visibleGuardar = visibleGuardar;
    }

    public boolean isVisibleTransmitir() {
        return visibleTransmitir;
    }

    public void setVisibleTransmitir(boolean visibleTransmitir) {
        this.visibleTransmitir = visibleTransmitir;
    }

    public int getMaxLengthDoc() {
        return maxLengthDoc;
    }

    public void setMaxLengthDoc(int maxLengthDoc) {
        this.maxLengthDoc = maxLengthDoc;
    }

    public boolean isRenderPanelNuevoRep() {
        return renderPanelNuevoRep;
    }

    public void setRenderPanelNuevoRep(boolean renderPanelNuevoRep) {
        this.renderPanelNuevoRep = renderPanelNuevoRep;
    }

    public int getMaxLengthDocPerRef() {
        return maxLengthDocPerRef;
    }

    public void setMaxLengthDocPerRef(int maxLengthDocPerRef) {
        this.maxLengthDocPerRef = maxLengthDocPerRef;
    }

    public boolean isVisiblePanelPersonaRef() {
        return visiblePanelPersonaRef;
    }

    public void setVisiblePanelPersonaRef(boolean visiblePanelPersonaRef) {
        this.visiblePanelPersonaRef = visiblePanelPersonaRef;
    }

    public String getLinkTutorial() {
        return linkTutorial;
    }

    public void setLinkTutorial(String linkTutorial) {
        this.linkTutorial = linkTutorial;
    }

    public String getMensajeAvisoSede() {
        return mensajeAvisoSede;
    }

    public void setMensajeAvisoSede(String mensajeAvisoSede) {
        this.mensajeAvisoSede = mensajeAvisoSede;
    }
    
    //LIMPIAR LISTA
    public void reiniciarValoresLista() {
        listDistritos = new ArrayList();
        procedimientoFiltro = null;
        tupaLst = new ArrayList();
        tupaSelected = null;
        reiniciarValoresEmpoce();
        reiniciarValoresRequisitos();
        notificacionAlertaCelularBoolean = false;
        notificacionViaSelBoolean = false;
        visibleNotificacionAlertaCelularBoolean = false;
        visibleGuardar = false;
        visibleTransmitir = false;
        visiblePanelNotificaciones = false;
        visiblePanelPersonaRef = false;
    }

    //LIMIAR CREATE
    public void reiniciarValoresCrear() {
        registro = null;
        personaLogeada = null;
        procedimientoFiltro = null;
        listDistritos = new ArrayList();
        procedimientoFiltro = null;
        tupaLst = new ArrayList();
        tupaSelected = null;
        reiniciarValoresEmpoce();
        reiniciarValoresRequisitos();
        notificacionAlertaCelularBoolean = false;
        notificacionViaSelBoolean = false;
        visibleNotificacionAlertaCelularBoolean = false;
        visibleGuardar = false;
        visibleTransmitir = false;
        visiblePanelNotificaciones = false;
        renderPanelNuevoRep = false;
        visiblePanelPersonaRef = false;
        linkTutorial=ejbSbParametroFacade.obtenerParametroXCodProg("TP_LINK_TUTOVV").getValor();
    }

    //LIMPIAR PANEL EMPOCE
    public void reiniciarValoresEmpoce() {
        visiblePanelEmpoce = false;
        empoceFiltro = null;
        recibosLst = new ArrayList();
        reciboSelected = null;
        mensajeErrorEmpoce = null;
        visibleGuardar = false;
        visibleTransmitir = false;
        visiblePanelNotificaciones = false;
    }

    //LIMPIAR PANEL REQUISITOS
    public void reiniciarValoresRequisitos() {
        visiblePanelRequisitos = false;
        expVirtualRequisitoLst = new ArrayList();
    }

    //PREPARE LISTA
    public String prepareList() {
        reiniciarValoresLista();
        expVirtualSolicitudLst = new ArrayList();
        personaLogeada = loginController.getUsuario().getPersona();
        TipoBaseGt tipoCreado = tipoBaseGtFacade.buscaTipoBaseXCodProg("TP_EXVIEST_CRE");
        int cont = ejbSbExpVirtualSolicitudFacade.contarSolicitudesPorSolicitanteYEstado(personaLogeada.getId(), tipoCreado.getId());
        if (cont > 0) {
            JsfUtil.addWarningMessage("Usted tiene al menos 01 solicitud pendiente de presentar ante SUCAMEC, búsquelo con el estado CREADO");
        }
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/tramiteVirtual/ListSolicitudesTramVirtual.xhtml";
    }

    //45836493
    //PREPARE CREATE
    public String prepareCreate() {
        reiniciarValoresCrear();
        registro = new SbExpVirtualSolicitud();
        personaLogeada = loginController.getUsuario().getPersona();
        estado = EstadoCrud.CREAR;
        return "/aplicacion/tramiteVirtual/CrearSolicitudTramVirtual.xhtml";
    }

    public void buscarProcedimiento() {
        setMensajeAvisoSede("");
        if (procedimientoFiltro != null) {
            if ((procedimientoFiltro == null || procedimientoFiltro.isEmpty())) {
                JsfUtil.mensajeError("Por favor ingrese el procedimiento a buscar");
            } else {
                tupaSelected = null;
                reiniciarValoresEmpoce();
                reiniciarValoresRequisitos();
                visiblePanelPersonaRef = false;
                tupaLst = ejbSbTupaFacade.obtenerTupaLike(procedimientoFiltro.trim().toUpperCase());
                renderTablaProcedimiento = true;
                procedimientoFiltro = null;
            }
        }
    }

    public void seleccionarProcedimiento(SbTupa seleccion) {
        reiniciarValoresEmpoce();
        reiniciarValoresRequisitos();
        visiblePanelPersonaRef = false;
        tupaSelected = seleccion;
        visiblePanelEmpoce = mostrarBuscarEmpoce(seleccion);
        visiblePanelPersonaRef = (solicitarReciboSoloPersonaRef(seleccion) || solicitarReciboPersonaRefYSolicitante(seleccion) || mostrarPersonaRefSinRecibo(seleccion));
        renderTablaProcedimiento = false;
        visualizaAvisoSede();
        if (!visiblePanelEmpoce) {
            cargarPanelRequisitos();
        }
    }

    public void buscarEmpoce() {
        String mensajeUsuario="";
        
        mensajeErrorEmpoce = null;
        if (empoceFiltro == null) {
            JsfUtil.mensajeError("Debe ingresar un número de recibo.");
            return;
        }
        HashMap mMap = new HashMap();
        mMap.put("importe", tupaSelected.getImporte().doubleValue());
        mMap.put("codigo", tupaSelected.getCodTributo()); //<-- codigo tributo del sb_tupa seleccionado
        String nroDoc = "";
        String tupasPNconRUCByRUC = ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupaPNconRUCByRUC").getValor();
        String tupasPNconRUCByRUCDNI = ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasPNconRUCByRUCDNI").getValor();
        boolean solRecSoloPersonaRef = solicitarReciboSoloPersonaRef(tupaSelected);
        boolean solRecPersonaRefSolicitante = solicitarReciboPersonaRefYSolicitante(tupaSelected);
        if (solRecSoloPersonaRef) {
            if (registro.getDocumentoRefPersona() == null) {
                JsfUtil.mensajeError("Debe ingresar el número de documento de la persona a la cual se solicita el trámite.");
                return;
            }
            nroDoc = registro.getDocumentoRefPersona();
        } else {
            if (personaLogeada.getTipoId().getCodProg().equals("TP_PER_NAT")) {
                if (tupasPNconRUCByRUCDNI.contains(tupaSelected.getTupaProcedimientoId().getCodProg().substring(8))) {
                    nroDoc = "'" + personaLogeada.getNumDoc() + "','" + personaLogeada.getRuc() + "'";
                } else if (tupasPNconRUCByRUC.contains(tupaSelected.getTupaProcedimientoId().getCodProg().substring(8))) {
                    nroDoc = personaLogeada.getRuc();
                } else {
                    nroDoc = personaLogeada.getNumDoc();
                }
            } else if (personaLogeada.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                nroDoc = personaLogeada.getRuc();
            }
            if (solRecPersonaRefSolicitante) {
                nroDoc += "'" + registro.getDocumentoRefPersona() + "'";
            }
        }
        mMap.put("numDoc", nroDoc);
        mMap.put("filtro", empoceFiltro);
        if (filtrarEmpoceCitasVerif()) {
            mMap.put("flag_citas_verif", "si");
        }
        SbExpVirtualSolicitud solRecUsado = buscarReciboUsadoSolicitud(mMap);
        if (solRecUsado == null) {
            recibosLst = recibosFacade.listarRecibosTramiteVirtualByFiltro(mMap);
            //Buscamos el numero siguiente si es nulo
            if (recibosLst == null || recibosLst.size() == 0) {
                mMap.remove("filtro");
                mMap.put("filtro",empoceFiltro+1);
                //Verificamos que no este utilizado en otro registro previamente
                solRecUsado = buscarReciboUsadoSolicitud(mMap);
                //Solo si es que no lo encuentra como usado en una anterior solicitud le suma y actualiza un numero al empoce
                if (solRecUsado == null) {
                    recibosLst = recibosFacade.listarRecibosTramiteVirtualByFiltro(mMap);
                    if (recibosLst.size()>0) {
                        empoceFiltro = empoceFiltro + 1;
                        mensajeUsuario = "Se ha encontrado que el banco ha procesado su empoce con el numero: " + empoceFiltro;
                        JsfUtil.mensaje(mensajeUsuario);
                    }
                }    
            }
            //Buscamos el numero anterior si es nulo
            if (recibosLst == null || recibosLst.size() == 0) {
                mMap.remove("filtro");
                mMap.put("filtro", empoceFiltro-1);
                //Verificamos que no este utilizado en otro registro previamente
                solRecUsado = buscarReciboUsadoSolicitud(mMap);
                //Solo si es que no lo encuentra como usado en una anterior solicitud le resta y actualiza un numero al empoce
                if (solRecUsado == null) {
                    recibosLst = recibosFacade.listarRecibosTramiteVirtualByFiltro(mMap);
                    if (recibosLst.size()>0) {
                        empoceFiltro = empoceFiltro - 1;
                        mensajeUsuario = "Se ha encontrado que el banco ha procesado su empoce con el numero: " + empoceFiltro;
                        JsfUtil.mensaje(mensajeUsuario);
                    }
                }    
            }
            if (recibosLst == null || recibosLst.size() == 0) {
                if (solRecSoloPersonaRef) {
                    mensajeErrorEmpoce = "No se encontraron empoces disponibles a nombre de la Persona a la que solicita el trámite con el número ingresado. Número ingresado : " + empoceFiltro;
                    JsfUtil.mensajeError("No se encontraron empoces disponibles a nombre de la Persona a la que solicita el trámite con el número ingresado. Número ingresado : " + empoceFiltro);
                } else if (solRecPersonaRefSolicitante) {
                    mensajeErrorEmpoce = "No se encontraron empoces disponibles a su nombre o de la Persona a la que solicita el trámite con el número ingresado. Número ingresado : " + empoceFiltro;
                    JsfUtil.mensajeError("No se encontraron empoces disponibles a su nombre o de la Persona a la que solicita el trámite con el número ingresado. Número ingresado : " + empoceFiltro);
                } else {
                    mensajeErrorEmpoce = "No se encontraron empoces disponibles a su nombre con el número ingresado. Número ingresado : " + empoceFiltro;
                    JsfUtil.mensajeError("No se encontraron empoces disponibles a su nombre con el número ingresado. Número ingresado : " + empoceFiltro);
                }

            } else {
                reciboSelected = null;
            }
        } else {
            String ndoc = "";
            if (solRecUsado.getEstadoId().getCodProg().equals("TP_EXVIEST_CRE")) {
                ndoc = "el borrador de solicitud " + solRecUsado.getNumeroSolicitud() + " registrado el " + JsfUtil.formatoFechaDdMmYyyy(solRecUsado.getFechaRegistro());
            } else {
                ndoc = (solRecUsado.getNroExpediente() != null) ? "la solicitud con Nro. Expediente " + solRecUsado.getNroExpediente() : "la solicitud con Nro. Solicitud " + solRecUsado.getNumeroSolicitud();
                ndoc += " en estado " + solRecUsado.getEstadoId().getNombre();
            }
            JsfUtil.mensajeError("El empoce con número ingresado está usado en " + ndoc + ". Número ingresado : " + empoceFiltro);
            mensajeErrorEmpoce = "El empoce con número ingresado está usado en " + ndoc + ". Número ingresado : " + empoceFiltro;
        }
        empoceFiltro = null;
    }

    public boolean filtrarEmpoceCitasVerif() {
        boolean res = false;
        switch (tupaSelected.getTupaProcedimientoId().getCodProg()) {
            case "TP_TUPA_2018093": // OJO 
            case "TP_TUPA_2018094":
            case "TP_TUPA_2018095":
            case "TP_TUPA_2018096":
            case "TUPA22_PA3400FB9C": // ANTES 94 
            case "TUPA22_PA3400B133": // ANTES 95 
            case "TP_PROG_VER_TNJ": // para detectar transferencias

                res = true;
                break;
            default:
                res = false;
        }
        return res;
    }

    public boolean esInpe(String ruc) {
        if (ruc == null || ruc.isEmpty()) {
            return false;
        }
        return ruc.trim().equals(JsfUtil.bundle("amaRegEmpArma_rucInpe"));
    }
        
    public boolean mostrarBuscarEmpoce(SbTupa proc) {
        String NumDocUsuario = ""; 
        //Busca si el RUC                 
        if (loginController.getUsuario().getPersona().getRuc() != null) {
            NumDocUsuario = loginController.getUsuario().getPersona().getRuc();
        }
        if (esInpe(NumDocUsuario)) {
            return false;
        } else {
            if (proc != null) {
                Double importe = proc.getImporte().doubleValue();
                return (importe > 0.0);
            } else {
                return false;
            }
        }    
    }

    public void cargarPanelRequisitos() {
        visiblePanelRequisitos = true;
        visiblePanelNotificaciones = true;
        visibleGuardar = true;
        for (SbTupaRequisito tupaReq : tupaSelected.getSbTupaRequisitoList()) {
            if (tupaReq.getActivo() == 1) {
                SbExpVirtualRequisito req = new SbExpVirtualRequisito();
                req.setActivo((short) 1);
                req.setAudLogin(loginController.getUsuario().getLogin());
                req.setAudNumIp(loginController.getNumIP());
                req.setExpVirtualSolicitudId(registro);
                req.setFechaRegistro(new Date());
                req.setTupaRequisitoId(tupaReq);
                expVirtualRequisitoLst.add(req);
            }
        }
    }

    public void seleccionarEmpoce(SbRecibos seleccion) {
        reiniciarValoresRequisitos();
        reciboSelected = seleccion;
        cargarPanelRequisitos();
    }

    public void seleccionarEmpoceEdit(SbRecibos seleccion) {
        reciboSelected = seleccion;
        visiblePanelRequisitos = true;
        visiblePanelNotificaciones = true;
        visibleGuardar = true;
    }

    public StreamedContent descargarFormato(String nomArchivo) {
        try {
            String pathPdf = ejbSbParametroFacade.obtenerParametroXNombre("ventanillaVirtual_pathDocs").getValor();
            nomArchivo="tupa2022.pdf"; // mientras se parametriza por proceso
            JsfUtil.PdfDownloadFormatos(pathPdf + nomArchivo);
        } catch (IOException ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: tramiteVirtualController :", ex);
        }
        return null;

    }

    public String condicionStr(short condicionPresentacion) {
        switch (condicionPresentacion) {
            case 1:
                return "Obligatorio";
            default:
                return "Opcional";
        }
    }

    public void eliminarAdjunto(SbExpVirtualAdjunto adjuntoSelec) {
        List<SbExpVirtualRequisito> lstTemp = new ArrayList();
        lstTemp = expVirtualRequisitoLst;
        if (lstTemp != null && !lstTemp.isEmpty()) {
            for (SbExpVirtualRequisito req : lstTemp) {
                if (req.getSbExpVirtualAdjuntoList() != null && !req.getSbExpVirtualAdjuntoList().isEmpty()) {
                    for (Iterator<SbExpVirtualAdjunto> it = req.getSbExpVirtualAdjuntoList().iterator(); it.hasNext();) {
                        SbExpVirtualAdjunto adj = it.next();
                        if (Objects.equals(adj.getNombre(), adjuntoSelec.getNombre())) {
                            if (adjuntoSelec.getId() != null) {
                                adj.setActivo((short) 0);
                            } else {
                                it.remove();
                            }
                        }
                    }
                }
            }
        }
        expVirtualRequisitoLst = new ArrayList();
        expVirtualRequisitoLst = lstTemp;
    }

    public void handleFileUploadArchivo(FileUploadEvent event) {
        try {
            if (event != null) {
                UploadedFile file = event.getFile();

                long sizeLimit = 0L, sizeAdj = 0L;
                int nroPagina = 0, sizeLimitMostrar = 0;
                if (file.getFileName().toLowerCase().endsWith(".pdf")) {
                    if (JsfUtil.verificarPDF(file)) {
                        sizeLimit = Long.parseLong(ejbSbParametroFacade.obtenerParametroXNombre("ventanillaVirtual_sizeLimite_archivoPDF").getValor());
                        nroPagina = JsfUtil.contarPaginasPDF(file);
                        sizeAdj = file.getSize() / nroPagina;
                        sizeLimitMostrar = Math.round(sizeLimit / 1024);
                        tupaSelected.getCydocIdProceso();
                        if (sizeLimit < sizeAdj) {
                            JsfUtil.mensajeAdvertencia("Solo se permite subir archivos con un máximo de " + sizeLimitMostrar + " KB por página.");
                            return;
                        }
                        //djByte = IOUtils.toByteArray(fileDJ.getInputstream());                    
                        //archivoDJ = new DefaultStreamedContent(fileDJ.getInputstream(), "application/pdf");
                        //archivo = fileDJ.getFileName();
                    } else {
                        //fileDJ = null;
                        JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                        return;
                    }
                } else if (file.getFileName().toLowerCase().endsWith(".jpg") || file.getFileName().toLowerCase().endsWith(".jpeg")) {
                    if (JsfUtil.verificarJPG(file) && JsfUtil.analyzeImageJPG(file)) {
                        sizeLimit = Long.parseLong(ejbSbParametroFacade.obtenerParametroXNombre("ventanillaVirtual_sizeLimite_archivoJPG").getValor());
                        sizeAdj = file.getSize();
                        sizeLimitMostrar = Math.round(sizeLimit / 1024);
                        if (sizeLimit < sizeAdj) {
                            JsfUtil.mensajeAdvertencia("Solo se permite subir archivos con un máximo de " + sizeLimitMostrar + " KB.");
                            return;
                        }
                    } else {
                        JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG original");
                        return;
                    }
                }

                SbExpVirtualRequisito origen = (SbExpVirtualRequisito) event.getComponent().getAttributes().get("origen");
                for (SbExpVirtualRequisito req : expVirtualRequisitoLst) {
                    if (Objects.equals(req.getTupaRequisitoId(), origen.getTupaRequisitoId())) {
                        if (req.getSbExpVirtualAdjuntoList() == null || req.getSbExpVirtualAdjuntoList().isEmpty()) {
                            req.setSbExpVirtualAdjuntoList(new ArrayList());
                        }

                        String nombreArchivo = file.getFileName().substring(0, file.getFileName().lastIndexOf("."));
                        String extensionArchivo = file.getFileName().substring(file.getFileName().lastIndexOf("."), file.getFileName().length());
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        SbExpVirtualAdjunto expVirtualAdj = new SbExpVirtualAdjunto();
                        expVirtualAdj.setActivo((short) 1);
                        expVirtualAdj.setAudLogin(loginController.getUsuario().getLogin());
                        expVirtualAdj.setAudNumIp(loginController.getNumIP());
                        expVirtualAdj.setNombre("adj_" + timestamp.getTime() + extensionArchivo);
                        expVirtualAdj.setExpVirtualRequisitoId(req);
                        expVirtualAdj.setFechaRegistro(new Date());
                        expVirtualAdj.setAdjuntoBytes(IOUtils.toByteArray(file.getInputstream()));
                        req.getSbExpVirtualAdjuntoList().add(expVirtualAdj);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transmitirSolicitud(SbExpVirtualSolicitud item) {
        /*if (esTupaParaCitaPoligono(item)) {
            JsfUtil.mensajeError("Por favor, reserve la cita de polígono para continuar con el trámite");
            return;
        }*/
            
        try {
            //actualizar estado a PRESENTADO
            item.setEstadoId(tipoBaseGtFacade.buscaTipoBaseXCodProg("TP_EXVIEST_PRE"));

            item.setFechaSolicitud(new Date());
            item.setNumeroSolicitud("SV" + JsfUtil.dateToString(item.getFechaSolicitud(), "yyyy") + StringUtils.leftPad(ejbSbExpVirtualSolicitudFacade.obtenerNroSolicitud().toString(), 7, "0"));

            // asignar a personal de TD para que lo atienda
            String strUsuarios = "";
            if (ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_usuTD_" + item.getSedeSucamecId().getCodProg()) != null) {
                strUsuarios = ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_usuTD_" + item.getSedeSucamecId().getCodProg()).getValor();
                item.setUsuarioRecepcionId(ejbSbUsuarioFacade.find(ejbSbExpVirtualSolicitudFacade.obtenerIdUsuarioTDSegunListaSede(strUsuarios)));
            }

            ejbSbExpVirtualSolicitudFacade.edit(item);

            JsfUtil.mensaje("Se hizo la transmisión a SUCAMEC de manera exitosa");
            reiniciarValoresCrear();
            prepareList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void guardar() {
        if (validarGuardar()) {
            registro.setActivo((short) 1);
            registro.setAudLogin(loginController.getUsuario().getLogin());
            registro.setAudNumIp(loginController.getNumIP());

            registro.setAdministradoId(personaLogeada);
            registro.setComprobanteId(reciboSelected);

            registro.setNotificacionViaSel(notificacionViaSelBoolean ? (short) 1 : (short) 0);
            registro.setNotificacionAlertaCelular(notificacionAlertaCelularBoolean ? (short) 1 : (short) 0);

            registro.setProcedimientoTupaId(tupaSelected);
            if (ejbSbUsuarioFacade.selectUsuarioXLogin(loginController.getUsuario().getLogin()) != null) {
                SbUsuario usuarioLogeado = ejbSbUsuarioFacade.selectUsuarioXLogin(loginController.getUsuario().getLogin()).get(0);
                registro.setUsuarioCreacionId(usuarioLogeado);
                //registro.setSedeSucamecId(tipoBaseGtFacade.buscaTipoBaseXCodProg("TP_AREA_TRAM"));
            }
            registro.setEstadoId(tipoBaseGtFacade.buscaTipoBaseXCodProg("TP_EXVIEST_CRE"));
            registro.setFechaRegistro(new Date());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            registro.setNumeroSolicitud("Borrador_" + ("" + timestamp.getTime()).substring(0, 10));

            registro.setSbExpVirtualRequisitoList(expVirtualRequisitoLst);
            registro = (SbExpVirtualSolicitud) JsfUtil.entidadMayusculas(registro, "");
            ejbSbExpVirtualSolicitudFacade.create(registro);

            //guardar archivos en el repositorio
            subirAdjuntos(registro.getSbExpVirtualRequisitoList());

            //mostrar boton TRANSMITIR
            //setVisibleTransmitir(true);
            reiniciarValoresCrear();
            JsfUtil.mensaje("Se guardaron los cambios con éxito, para generar el expediente deberá transmitir a SUCAMEC desde la bandeja");
            //  JsfUtil.mostrarMensajeDialog("\"Usted tiene una solicitud pendiente de presentar ante SUCAMEC, se encuentra con estado CREADO ", 3);
            //redireccionar a la bandeja
           regresarLista(true);
        }
    }

    public void subirAdjuntos(List<SbExpVirtualRequisito> requisitosLst) {
        for (SbExpVirtualRequisito requisito : requisitosLst) {
            for (SbExpVirtualAdjunto adjunto : requisito.getSbExpVirtualAdjuntoList()) {
                if (adjunto.getActivo() == 1 && adjunto.getAdjuntoBytes() != null) {
                    try {
                        //String extensionArchivo = adjunto.getNombre().substring(adjunto.getNombre().lastIndexOf(".")+1, adjunto.getNombre().length());
                        FileUtils.writeByteArrayToFile(new File((ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ExpVirtualSolicitud_adjunto").getValor()) + adjunto.getNombre()), adjunto.getAdjuntoBytes());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean validarGuardar() {
        //validar estado PARA COMPLETAR REQUISITOS antes de guardar
        boolean flagValido = true;
        if (registro.getDireccion() == null || registro.getDireccion().trim().equals("")) {
            flagValido = false;
            JsfUtil.mensajeError("Debe ingresar una dirección");
        } else {
            registro.setDireccion(registro.getDireccion().trim());
            if (registro.getDireccion().length() > 350) {
                flagValido = false;
                JsfUtil.mensajeError("La dirección debe tener 350 carácteres como máximo.");
            }
        }
        if (personaLogeada.getTipoId().getCodProg().equals("TP_PER_JUR")) {
            if (renderPanelNuevoRep) {
                registro.setRepresentanteLegalId(null);
                if (registro.getTipoDocNuevoRepLegalId() == null) {
                    flagValido = false;
                    JsfUtil.mensajeError("Debe seleccionar el tipo de documento del representante legal.");
                }
                if (registro.getDocumentoNuevoRepLegal() == null || registro.getDocumentoNuevoRepLegal().trim() == null || registro.getDocumentoNuevoRepLegal().trim().isEmpty()) {
                    flagValido = false;
                    JsfUtil.mensajeError("Debe indicar el número de documento de representante legal.");
                }
                if (registro.getTipoDocNuevoRepLegalId() != null && registro.getDocumentoNuevoRepLegal() != null) {
                    if (registro.getTipoDocNuevoRepLegalId().getCodProg().equals("TP_DOCID_DNI")
                            && registro.getDocumentoNuevoRepLegal().trim().length() != 8) {
                        flagValido = false;
                        JsfUtil.mensajeError("El DNI del representante legal debe tener 8 números.");
                    }
                }
            } else {
                registro.setDocumentoNuevoRepLegal(null);
                registro.setTipoDocNuevoRepLegalId(null);
                if (registro.getRepresentanteLegalId() == null) {
                    flagValido = false;
                    JsfUtil.mensajeError("Debe seleccionar un representante legal.");
                }
            }

        }
        if (registro.getUbigeoDireccionId() == null) {
            flagValido = false;
            JsfUtil.mensajeError("Debe seleccionar la ubicación geográfica de la dirección ingresada");
        }
        if (registro.getTelefonoContacto() != null) {
            registro.setTelefonoContacto(registro.getTelefonoContacto().trim());
            if (registro.getTelefonoContacto().length() > 40) {
                flagValido = false;
                JsfUtil.mensajeError("El teléfono de contacto debe tener 40 carácteres como máximo.");
            }
        }
        if (tupaSelected == null) {
            flagValido = false;
            JsfUtil.mensajeError("Debe seleccionar un procedimiento o servicio TUPA");
        }
        if (solicitarPersonaRef(tupaSelected)) {
            if (!tieneDatosPersonaRef()) {
                flagValido = false;
            }
        }
        if (mostrarBuscarEmpoce(tupaSelected) && reciboSelected == null) {
            flagValido = false;
            JsfUtil.mensajeError("Debe seleccionar un recibo de pago del Banco de la Nación.");
        }
        if (registro.getSedeSucamecId() == null) {
            flagValido = false;
            JsfUtil.mensajeError("Debe seleccionar una sede de SUCAMEC.");
        }
        if (expVirtualRequisitoLst != null && !expVirtualRequisitoLst.isEmpty()) {
            int contAdj = 0;
            for (SbExpVirtualRequisito req : expVirtualRequisitoLst) {
                if (req.getTupaRequisitoId().getCondicionPresentacion() == 1) {
                    contAdj = 0;
                    if (req.getSbExpVirtualAdjuntoList() != null && !req.getSbExpVirtualAdjuntoList().isEmpty()) {
                        for (SbExpVirtualAdjunto adj : req.getSbExpVirtualAdjuntoList()) {
                            if (adj.getActivo() == 1) {
                                contAdj++;
                            }
                        }
                    }
                    //Valida que Tenga una respuesta o adjunto un archivo
                    /*if (contAdj == 0 && (req.getRespuestaRequisito() == null || req.getRespuestaRequisito().trim().equals(""))) {
                        flagValido = false;
                        JsfUtil.mensajeError("Debe ingresar una respuesta y/o adjuntar al menos un archivo del requisito: " + req.getTupaRequisitoId().getDescripcion());
                    }*/
                    //Valida Si es un Requisito Obligatorio que tenga un archivo Adjunto cuando se habilita la opcion de descarga
                    if (contAdj == 0) {
                        flagValido = false;
                        JsfUtil.mensajeError("Debe adjuntar al menos un archivo del requisito: " + req.getTupaRequisitoId().getDescripcion());
                    }
                }
            }
        }
        if (notificacionViaSelBoolean == false) {
            flagValido = false;
            JsfUtil.mensajeError("Es necesario aceptar recibir notificaciones en el Buzón Electrónico de la plataforma virtual SEL para poder continuar");
        }
        if (notificacionAlertaCelularBoolean == true && (registro.getTelefonoContacto() == null || registro.getTelefonoContacto().trim().isEmpty())) {
            flagValido = false;
            JsfUtil.mensajeError("Para recibir alertas en el celular, por favor indique su número de teléfono");
        }
        return flagValido;
    }

    public void regresar() {
        reiniciarValoresCrear();
        registro = new SbExpVirtualSolicitud();
        personaLogeada = loginController.getUsuario().getPersona();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //prepareList();
    }

    public void regresarLista(boolean mostrarMsjPendienteTransmitir) {
        reiniciarValoresLista();
        registro = new SbExpVirtualSolicitud();
        personaLogeada = loginController.getUsuario().getPersona();
        expVirtualSolicitudLst = ejbSbExpVirtualSolicitudFacade.listarSolicitudesXCriterios((filtroLbl != null) ? filtroLbl.trim() : "", (tipoFiltro != null) ? tipoFiltro.trim() : "", personaLogeada.getId(), (tipoEstadoSolicitud != null) ? tipoEstadoSolicitud.getCodProg() : null);
        estado = EstadoCrud.BUSCAR;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {      
            RequestContext.getCurrentInstance().update(":listForm:buscarDatatableRegistro");
            RequestContext.getCurrentInstance().update("buscarDatatableRegistro"); 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            if (mostrarMsjPendienteTransmitir) {
               JsfUtil.mensajeAdvertencia("Usted tiene una solicitud pendiente de presentar ante SUCAMEC, se encuentra con estado CREADO");
              //JsfUtil.mensaje("Usted tiene una solicitud pendiente de presentar ante SUCAMEC, se encuentra con estado CREADO");
               //         JsfUtil.mostrarMensajeDialog("\"Usted tiene una solicitud pendiente de presentar ante SUCAMEC, se encuentra con estado CREADO ", 3);
            }
             ec.redirect("/sel/faces/aplicacion/tramiteVirtual/ListSolicitudesTramVirtual.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<SbPersona> listarRepresentante() {
        return ejbSbPersonaFacade.listxRepresentante(personaLogeada.getId());
    }

    public String getDocumentoRznSocApeNombresCompleto() {
        if (personaLogeada.getTipoId().getCodProg().equals("TP_PER_JUR")) {
            return "RUC : " + personaLogeada.getRuc() + " - " + personaLogeada.getRznSocial();
        } else if (personaLogeada.getTipoId().getCodProg().equals("TP_PER_NAT")) {
            return personaLogeada.getTipoDoc().getNombre() + " : " + personaLogeada.getNumDoc() + " - " + personaLogeada.getApellidosyNombres();
        }
        return "";
    }

    public List<TipoBaseGt> listarTipoEstado() {
        return tipoBaseGtFacade.lstEstadosSolicitud();
    }

    public void buscarSolicitudes() {
        if (!validacionBuscarBandeja()) {
            return;
        }
        try {
            expVirtualSolicitudLst = ejbSbExpVirtualSolicitudFacade.listarSolicitudesXCriterios((filtroLbl != null) ? filtroLbl.trim() : "", (tipoFiltro != null) ? tipoFiltro.trim() : "", personaLogeada.getId(), (tipoEstadoSolicitud != null) ? tipoEstadoSolicitud.getCodProg() : null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean validacionBuscarBandeja() {
        /*if (tipoFiltro == null) {
            JsfUtil.mensajeError("Por favor seleccione un criterio para buscar");
            JsfUtil.invalidar(":listForm:tipoEstado");
            return false;
        }*/
        if (tipoFiltro != null) {
            if ((filtroLbl == null || filtroLbl.isEmpty())) {
                JsfUtil.mensajeError("Por favor ingrese el campo a buscar");
                JsfUtil.invalidar(":listForm:filtroBusqueda");
                return false;
            }

        }
        return true;
    }

    public boolean renderBtnEditarSolicitud(SbExpVirtualSolicitud item) {
        if (item == null) {
            return false;
        }
        if (item != null) {
            if (item.getEstadoId().getCodProg().equals("TP_EXVIEST_CRE")) {
                return true;
            }
        }
        return false;
    }

    public boolean renderBtnTransmitirSolicitud(SbExpVirtualSolicitud item) {
        if (item == null) {
            return false;
        }
        if (item != null) {
            if (item.getEstadoId().getCodProg().equals("TP_EXVIEST_CRE")) {
                return true;
            }
        }
        return false;
    }

    public boolean renderBtnVerSolicitud(SbExpVirtualSolicitud item) {
        if (item == null) {
            return false;
        }
        if (item != null) {
            return true;
        }
        return false;
    }

    public boolean renderBtnBorrarSolicitud(SbExpVirtualSolicitud item) {
        if (item == null) {
            return false;
        }
        if (item != null) {
            if (item.getEstadoId().getCodProg().equals("TP_EXVIEST_CRE")) {
                return true;
            }
        }
        return false;
    }

    public boolean renderBtnNoPresentadoSolicitud(SbExpVirtualSolicitud item) {
        if (item == null) {
            return false;
        }
        if (item != null) {
            boolean res = false;
            switch (item.getEstadoId().getCodProg()) {
                case "TP_EXVIEST_LCP": //CON CITA POLIGONO RESERVADA 
                case "TP_EXVIEST_OBSREQ": //PARA COMPLETAR REQUISITOS
                case "TP_EXVIEST_REVTRA": //REVISADO POR TRÁMITE
                    res = true;
                    break;
                default:
                    res = false;
            }
            return res;
        }
        return false;
    }
        
    public boolean renderBtnConstancia(SbExpVirtualSolicitud item) {
        if (item == null) {
            return false;
        }
        if (item != null) {
            boolean res = false;
            switch (item.getEstadoId().getCodProg()) {
                case "TP_EXVIEST_TRA":
                case "TP_EXVIEST_TROBS":
                case "TP_EXVIEST_APRO":
                    res = true;
                    break;
                default:
                    res = false;
            }
            return res;
        }
        return false;
    }

    public boolean renderBtnSubsanarSolicitud(SbExpVirtualSolicitud item) {
        if (item == null) {
            return false;
        }
        if (item != null) {
            if (item.getEstadoId().getCodProg().equals("TP_EXVIEST_OBSREQ")
                    || item.getEstadoId().getCodProg().equals("TP_EXVIEST_TROBS")) {
                return true;
            }
        }
        return false;
    }

    public void borrarSolicitud(SbExpVirtualSolicitud item) {
        try {
            SbExpVirtualSolicitud solicitud = item;
            solicitud.setActivo((short) 0);
            ejbSbExpVirtualSolicitudFacade.edit(solicitud);

            expVirtualSolicitudLst.remove(item);

            JsfUtil.mensaje("Se ha borrado correctamente la solicitud");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al borrar la solicitud.");
        }
    }

    public void cambiarNoPresentadoSolicitud(SbExpVirtualSolicitud item) {
        try {
            SbExpVirtualSolicitud solicitud = item;
            //Cambia a no Presentado
            solicitud.setEstadoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_EXVIEST_NPR"));
            ejbSbExpVirtualSolicitudFacade.edit(solicitud);
            
            //Actualiza como inactivos los Turnos
            List<CitaTurTurno> lstTurno = ejbCitaTurnoFacade.buscarTurnoXSolicitud(item.getId());
            if (lstTurno.size() > 0) { 
                for (CitaTurTurno tur : lstTurno) {
                    //Actualiza como inactivos en tur_licencia_reg
                    List<CitaTurLicenciaReg> lstLicencias = ejbLicenciaRegFacade.listarLicenciasPorTurno(tur.getId());
                    if (lstLicencias.size() > 0) { 
                        for (CitaTurLicenciaReg licReg : lstLicencias) {
                            if (licReg.getComprobanteId() != null) {
                                List<CitaTurComprobante> lstComprobante = ejbTurComprobanteFacade.listarComprobantePorTurno(licReg.getComprobanteId().getId());
                                for (CitaTurComprobante listCompro : lstComprobante) {
                                    listCompro.setActivo((short) 0);
                                    ejbTurComprobanteFacade.edit(listCompro);
                                }
                            }    
                            licReg.setActivo((short) 0);
                            ejbLicenciaRegFacade.edit(licReg);
                        }
                    }
                    tur.setActivo((short) 0);
                    ejbCitaTurnoFacade.edit(tur);
                }
            }    
            
            //solicitud.setActivo((short) 0);

            JsfUtil.mensaje("Se ha cambiado a NO PRESENTADO correctamente la solicitud");
            reiniciarValoresCrear();
            prepareList();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al cambiar a NO PRESENTADO la solicitud.");
        }
    }
        
    public String mostrarEditarSolicitud(SbExpVirtualSolicitud item) {
        reiniciarValoresLista();
        cargarDatos(item);
        estado = EstadoCrud.EDITAR;
        return "/aplicacion/tramiteVirtual/EditarSolicitudTramVirtual.xhtml";
    }

    public String mostrarVerSolicitud(SbExpVirtualSolicitud item) {
        reiniciarValoresLista();
        cargarDatos(item);
        estado = EstadoCrud.VER;
        return "/aplicacion/tramiteVirtual/VerSolicitudTramVirtual.xhtml";
    }

    public String mostrarSubsanarSolicitud(SbExpVirtualSolicitud item) {
        //validar estado PARA COMPLETAR REQUISITOS antes de guardar
        reiniciarValoresLista();
        cargarDatos(item);
        estado = EstadoCrud.EDITAR;
        return "/aplicacion/tramiteVirtual/SubsanarSolicitudTramVirtual.xhtml";
    }

    public boolean cargarDatos(SbExpVirtualSolicitud item) {
        registro = null;
        if (item != null) {
            //registro = item;
            registro = ejbSbExpVirtualSolicitudFacade.find(item.getId());
            if (registro != null) {
                personaLogeada = registro.getAdministradoId();
                visiblePanelEmpoce = mostrarBuscarEmpoce(registro.getProcedimientoTupaId());//true;
                visiblePanelPersonaRef = (solicitarReciboSoloPersonaRef(registro.getProcedimientoTupaId()) || solicitarReciboPersonaRefYSolicitante(registro.getProcedimientoTupaId()) || mostrarPersonaRefSinRecibo(registro.getProcedimientoTupaId()));
                renderPanelNuevoRep = (registro.getDocumentoNuevoRepLegal() != null);
                tupaSelected = registro.getProcedimientoTupaId();
                reciboSelected = registro.getComprobanteId();
                visiblePanelRequisitos = true;
                //expVirtualRequisitoLst = registro.getSbExpVirtualRequisitoList();
                List<SbExpVirtualRequisito> lstTemp = new ArrayList();
                lstTemp = registro.getSbExpVirtualRequisitoList();
                if (lstTemp != null && !lstTemp.isEmpty()) {
                    for (SbExpVirtualRequisito req : lstTemp) {
                        if (req.getSbExpVirtualAdjuntoList() != null && !req.getSbExpVirtualAdjuntoList().isEmpty()) {
                            for (Iterator<SbExpVirtualAdjunto> it = req.getSbExpVirtualAdjuntoList().iterator(); it.hasNext();) {
                                SbExpVirtualAdjunto adj = it.next();
                                if (adj.getActivo() == (short) 0) {
                                    it.remove();
                                }
                            }
                        }
                    }
                }
                Collections.sort(lstTemp, new Comparator<SbExpVirtualRequisito>() {
                    @Override
                    public int compare(SbExpVirtualRequisito one, SbExpVirtualRequisito other) {
                        return one.getTupaRequisitoId().getId().compareTo(other.getTupaRequisitoId().getId());
                    }
                });
                expVirtualRequisitoLst = new ArrayList();
                expVirtualRequisitoLst = lstTemp;
                visiblePanelNotificaciones = true;
                visibleGuardar = true;
                notificacionViaSelBoolean = registro.getNotificacionViaSel() == (short) 1 ? true : false;
                notificacionAlertaCelularBoolean = registro.getNotificacionAlertaCelular() == (short) 1 ? true : false;
                cargarLongitudTipoDocumentoPersonaRef();
            } else {
                JsfUtil.mensajeError("No se encontró datos de la solicitud");
                return false;
            }
        }
        return true;
    }

    public void editarSolicitud() {
        if (validarGuardar()) {
            if (registro.getComprobanteId() != null && reciboSelected != null) {
                if (!Objects.equals(registro.getComprobanteId().getNroSecuencia(), reciboSelected.getNroSecuencia())) {
                    registro.setComprobanteId(reciboSelected);
                }
            }

            registro.setNotificacionAlertaCelular(notificacionAlertaCelularBoolean ? (short) 1 : (short) 0);

            //registro.setProcedimientoTupaId(tupaSelected);
            registro.setSbExpVirtualRequisitoList(expVirtualRequisitoLst);
            registro = (SbExpVirtualSolicitud) JsfUtil.entidadMayusculas(registro, "");
            ejbSbExpVirtualSolicitudFacade.edit(registro);

            //guardar archivos en el repositorio
            subirAdjuntos(registro.getSbExpVirtualRequisitoList());

            reiniciarValoresCrear();
            JsfUtil.mensaje("Se guardaron los cambios con éxito, para generar el expediente deberá transmitir a SUCAMEC desde la bandeja");

            //redireccionar a la bandeja
            //prepareList();
            regresarLista(true);
        }
    }

    public void enviarSubsanacion() {
        if (validarGuardar()) {
            //actualizar estado a PRESENTADO
            registro.setEstadoId(tipoBaseGtFacade.buscaTipoBaseXCodProg("TP_EXVIEST_PRE"));

            registro.setSbExpVirtualRequisitoList(expVirtualRequisitoLst);
            ejbSbExpVirtualSolicitudFacade.edit(registro);

            //guardar archivos en el repositorio
            subirAdjuntos(registro.getSbExpVirtualRequisitoList());

            reiniciarValoresCrear();
            JsfUtil.mensaje("Se guardaron los cambios con éxito.");
            regresarLista(false);
        }
    }

    public StreamedContent imprimirConstancia(SbExpVirtualSolicitud item) {
        try {
            List<SbExpVirtualSolicitud> lp = new ArrayList();
            lp.add(item);

            String nombreArch = "CONSTANCIA_" + item.getNumeroSolicitud();
            String ruta = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ExpVirtualSolicitud_adjunto").getValor();
            if (JsfUtil.buscarPdfRepositorio(nombreArch, ruta)) {
                return obtenerArchivoBDIntegrado(ruta, nombreArch + ".pdf", nombreArch + ".pdf", "application/pdf");
            } else {
                JsfUtil.subirPdf(nombreArch, parametrosPdf(item), lp, "/aplicacion/gamac/amaSolicitudTarjeta/reportes/constanciaSolicitudTarjeta.jasper", ruta);
                return obtenerArchivoBDIntegrado(ruta, nombreArch + ".pdf", nombreArch + ".pdf", "application/pdf");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StreamedContent obtenerArchivoBDIntegrado(String rutaBundle, String nombre, String descarga, String mime) throws Exception {
        FileInputStream f = new FileInputStream(rutaBundle + File.separator + nombre);
        return new DefaultStreamedContent(f, mime, descarga);
    }

    public HashMap parametrosPdf(SbExpVirtualSolicitud ppdf) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();

        ph.put("P_FECHASTRING", JsfUtil.dateToString(ppdf.getFechaExpediente(), "dd/MM/yyyy"));
        ph.put("P_HORA", JsfUtil.dateToString(ppdf.getFechaExpediente(), "hh:mm:ss a"));
        ph.put("P_EXPEDIENTE", ppdf.getNroExpediente());
        ph.put("P_USUARIO", ppdf.getUsuarioCreacionId().getLogin());
        ph.put("P_LISTA_DATOS", listarDatosConstancia(ppdf));
        ph.put("P_LISTA_DOCUMENTOS", listarDocumentosConstancia(ppdf, ppdf.getFechaExpediente()));

        return ph;
    }

    public List<Map> listarDatosConstancia(SbExpVirtualSolicitud registroId) {
        List<Map> listado = new ArrayList();
        Map nmap;
        String nombres = "";
        Expediente expediente = ejbExpedienteFacade.obtenerExpedienteXNumero(registroId.getNroExpediente());

        nmap = new HashMap();
        nmap.put("nombre", "Título: ");
        nmap.put("valor", "Solicitud Virtual de Expediente");//expediente.getTitulo()
        listado.add(nmap);

        nmap = new HashMap();
        nmap.put("nombre", "Proceso: ");
        nmap.put("valor", expediente.getIdProceso().getNombre());
        listado.add(nmap);

        nmap = new HashMap();
        nmap.put("nombre", "Administrado: ");
        if (registroId.getAdministradoId().getRznSocial() != null) {
            nombres = registroId.getAdministradoId().getRznSocial() + ", RUC: " + registroId.getAdministradoId().getRuc();
        } else {
            nombres = registroId.getAdministradoId().getNombreCompleto() + ", DNI: " + registroId.getAdministradoId().getNumDoc();
        }
        nmap.put("valor", nombres);
        listado.add(nmap);

        return listado;
    }

    public List<Map> listarDocumentosConstancia(SbExpVirtualSolicitud registroId, Date fechaTransmision) {
        List<Map> listado = new ArrayList();
        Map nmap;

        if (registroId.getComprobanteId() != null) {
            nmap = new HashMap();
            nmap.put("nombre", "VOUCHER:");
            nmap.put("descripcion", "NRO. " + registroId.getComprobanteId().getNroSecuencia() + ", MONTO: S/." + registroId.getComprobanteId().getImporte());
            nmap.put("textoFecha", "FECHA:");
            nmap.put("fecha", JsfUtil.dateToString(registroId.getComprobanteId().getFechaMovimiento(), "dd/MM/yyyy"));
            listado.add(nmap);
        }
        return listado;
    }

    public List<TipoBaseGt> listarSedeSucamec() {
        String sedesDisponibles = "'TP_AREA_OD_JUNIN','TP_AREA_TRAM','TP_AREA_OD_ANCASH','TP_AREA_OD_AQP','TP_AREA_OD_CHICLA','TP_AREA_OD_CUSCO','TP_AREA_OD_LIBERTA','TP_AREA_OD_PIURA','TP_AREA_OD_PUNO','TP_AREA_OD_TACNA','TP_AREA_OD_ICA','TP_AREA_OD_CAJAMARCA','TP_AREA_OD_LOR'";
        String flag = (ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_flagAfectoNoAtencion") != null) ? ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_flagAfectoNoAtencion").getValor() : "";
        if (flag.equals("true")) {
            String tupasAfectoNoAtencion = (ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasAfectoNoAtencion") != null) ? ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasAfectoNoAtencion").getValor() : "";
            List<String> sedesDispLst = Arrays.asList(sedesDisponibles.split(","));
            sedesDisponibles = "";
            String sedesAfectoNoAtencion = (ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_sedesAfectoNoAtencion") != null) ? ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_sedesAfectoNoAtencion").getValor() : "";
            String sedeEval = "";
            for (String sede : sedesDispLst) {
                sedeEval = sede.substring(1, sede.length() - 1);
                if (!sedesAfectoNoAtencion.contains(sedeEval) || !tupasAfectoNoAtencion.contains(tupaSelected.getTupaProcedimientoId().getCodProg().substring(8))) {
                    sedesDisponibles += "'" + sedeEval + "',";
                }
            }
            sedesDisponibles = sedesDisponibles.substring(0, sedesDisponibles.length() - 1);
        }
        return tipoBaseGtFacade.lstTiposXCodigosProg(sedesDisponibles);
    }

    public StreamedContent verAdjunto(String nomArch) {
        String pathPdf = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ExpVirtualSolicitud_adjunto").getValor();
        try {
            if (nomArch.endsWith(".pdf")) {
                JsfUtil.PdfDownload(pathPdf, nomArch);
            } else {
                JsfUtil.PdfDownloadImagen(pathPdf, nomArch);
            }

        } catch (IOException ex) {
            //ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: Ver Adjunto Ventanilla Virtual :", ex);
        }
        return null;
    }

    public List<TipoBaseGt> getTipoDocumentosSolicitante() {
        return tipoBaseGtFacade.lstTiposXCodigosProg("'TP_DOCID_DNI','TP_DOCID_CE'");
    }

    public void mostrarPanelNuevoRepLegal() {
        renderPanelNuevoRep = true;
        registro.setTipoDocNuevoRepLegalId(null);
        registro.setDocumentoNuevoRepLegal(null);
    }

    public void ocultarPanelNuevoRepLegal() {
        renderPanelNuevoRep = false;
        registro.setRepresentanteLegalId(null);
    }

    public void cambiaTipoDocumentoRepLegal() {
        maxLengthDoc = 0;

        if (registro.getTipoDocNuevoRepLegalId() != null) {
            switch (registro.getTipoDocNuevoRepLegalId().getCodProg()) {
                case "TP_DOCID_DNI":
                    maxLengthDoc = 8;
                    break;
                case "TP_DOCID_CE":
                    maxLengthDoc = 9;
                    break;
            }
        } else {
            JsfUtil.mensajeError("Por favor, ingrese el tipo de documento del nuevo representante legal");
        }
        registro.setDocumentoNuevoRepLegal(null);
    }

    public void buscarPersonaRepLegal() {
        if (registro.getTipoDocNuevoRepLegalId() != null) {
            if (registro.getTipoDocNuevoRepLegalId().getCodProg().equals("TP_DOCID_DNI")) {
                buscarDlgPersonaReniec(registro.getTipoDocNuevoRepLegalId(), registro.getDocumentoNuevoRepLegal());
                if (registro.getDocumentoNuevoRepLegal() != null) {
                    registro.setDocumentoNuevoRepLegal(registro.getDocumentoNuevoRepLegal().trim());
                }
            }
        } else {
            JsfUtil.mensajeError("Por favor, ingrese el tipo de documento del nuevo representante legal");
        }
    }

    public void buscarDlgPersonaReniec(TipoBaseGt tipoDoc, String numDoc) {
        boolean validacion = true;
        SbPersonaGt persona = null;

        if (tipoDoc == null) {
            validacion = false;
            //JsfUtil.invalidar("createForm:tipoDocumento");
            JsfUtil.mensajeError("Por favor ingresar el tipo de documento");
        }
        if (numDoc == null) {
            JsfUtil.mensajeError("Por favor ingresar el nro. de DNI");
            return;
        }
        if (numDoc.isEmpty()) {
            //JsfUtil.invalidar("createForm:numDocNuevoSolicitante");
            JsfUtil.mensajeError("Por favor ingresar el nro. de DNI");
            return;
        }
        if (numDoc.length() != 8) {
            //JsfUtil.invalidar("createForm:numDocNuevoSolicitante");
            JsfUtil.mensajeError("El DNI no tiene la cantidad de dígitos correctos");
            return;
        }
        /*persona = ejbSbPersonaGtFacade.buscarPersonaSel("", solicitanteRegistro.getNumDoc());
        if(persona != null){
            JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
            JsfUtil.mensajeError("El DNI ya se encuentra registrado, por favor verifique");
            return;
        }*/

        if (validacion) {
            solicitanteRegistro = new SbPersonaGt();
            solicitanteRegistro.setTipoDoc(tipoDoc);
            solicitanteRegistro.setNumDoc(numDoc);
            persona = wsPideController.buscarReniec(solicitanteRegistro);
            if (persona != null) {
                if (persona.getNombres() != null) {
                    String apeMat = "";
                    if (persona.getApeMat().trim().equals("X")) {
                        apeMat = "";
                    } else {
                        apeMat = persona.getApeMat().trim();
                    }
                    registro.setApePatRefPersona(persona.getApePat().trim());
                    registro.setApeMatRefPersona(apeMat);
                    registro.setNombresRefPersona(persona.getNombres().trim());
                    //RequestContext.getCurrentInstance().update("createForm");
                } else {
                    //JsfUtil.invalidar("createForm:numDocNuevoSolicitante");
                    JsfUtil.mensajeAdvertencia("Verifique el DNI. No se encontró el DNI con el servicio del RENIEC.");
                }
            } else {
                JsfUtil.mensajeAdvertencia("El servicio del RENIEC no se encuentra disponible, verifique el DNI.");
            }
        }
    }

    public boolean esTupaParaCitaPoligono(SbExpVirtualSolicitud item) {
        boolean res = false;
        switch (item.getProcedimientoTupaId().getTupaProcedimientoId().getCodProg()) {
            //case "TP_TUPA_2018072":
            //case "TP_TUPA_2018074":
            //case "TP_TUPA_2018076":
            //case "TP_TUPA_2018080":
            //case "TP_TUPA_2018081":
            //case "TP_TUPA_2018082":
            //case "TP_TUPA_2018083":
            //case "TP_TUPA_2018088":
            case "TUPA22_PA3400DED2": // ANTES TUPA 72  
            case "TUPA22_PA3400A790": // ANTES TUPA 74 
            case "TUPA22_PA34007189": // ANTES TUPA 76 
            case "TUPA22_PA340089AD": // ANTES TUPA 80 
            case "TUPA22_PA34004360": // ANTES TUPA 81 
            case "TUPA22_PA3400C585": // ANTES TUPA 82 
            case "TUPA22_PA3400C730": // ANTES TUPA 83 
            case "TUPA22_PA34004F01": // ANTES TUPA 88

                res = true;
                break;
            default:
                res = false;
        }
        return res;
    }

    public void cambiaTipoDocumentoPersonaRef() {
        maxLengthDocPerRef = 0;

        if (registro.getTipoDocRefPersonaId() != null) {
            switch (registro.getTipoDocRefPersonaId().getCodProg()) {
                case "TP_DOCID_DNI":
                    maxLengthDocPerRef = 8;
                    break;
                case "TP_DOCID_CE":
                    maxLengthDocPerRef = 9;
                    break;
            }

        } else {
            JsfUtil.mensajeError("Por favor, ingrese el tipo de documento de la Persona a la que solicita el trámite.");
        }
        registro.setDocumentoRefPersona(null);
        registro.setApePatRefPersona(null);
        registro.setApeMatRefPersona(null);
        registro.setNombresRefPersona(null);
        recibosLst = new ArrayList();
        if (registro.getEstadoId() != null) {
            if (!registro.getEstadoId().getCodProg().equals("TP_EXVIEST_OBSREQ")) {
                if (solicitarReciboSoloPersonaRef(registro.getProcedimientoTupaId()) || solicitarReciboPersonaRefYSolicitante(registro.getProcedimientoTupaId())) {
                    reciboSelected = null;
                }
            }
        }    
        mensajeErrorEmpoce = null;
    }
    
    public void cargarLongitudTipoDocumentoPersonaRef() {
        maxLengthDocPerRef = 0;

        if (registro.getTipoDocRefPersonaId() != null) {
            switch (registro.getTipoDocRefPersonaId().getCodProg()) {
                case "TP_DOCID_DNI":
                    maxLengthDocPerRef = 8;
                    break;
                case "TP_DOCID_CE":
                    maxLengthDocPerRef = 9;
                    break;
            }
        }
    }

    public void buscarPersonaRef() {
        if (registro.getTipoDocRefPersonaId() != null) {
            if (registro.getTipoDocRefPersonaId().getCodProg().equals("TP_DOCID_DNI")) {
                buscarDlgPersonaReniec(registro.getTipoDocRefPersonaId(), registro.getDocumentoRefPersona());
                if (registro.getDocumentoRefPersona() != null) {
                    registro.setDocumentoRefPersona(registro.getDocumentoRefPersona().trim());
                }
            }
            if (!mostrarPersonaRefSinRecibo(tupaSelected)) {
                recibosLst = new ArrayList();
                if (registro.getEstadoId() != null) {
                    if (!registro.getEstadoId().getCodProg().equals("TP_EXVIEST_OBSREQ")) {
                        if (solicitarReciboSoloPersonaRef(registro.getProcedimientoTupaId()) || solicitarReciboPersonaRefYSolicitante(registro.getProcedimientoTupaId())) {
                            reciboSelected = null;
                        }
                    }
                }
                mensajeErrorEmpoce = null;
            }
        } else {
            JsfUtil.mensajeError("Por favor, ingrese el tipo de documento de la Persona a la que solicita el trámite.");
        }
    }

    public boolean solicitarReciboSoloPersonaRef(SbTupa tupa) {
        String tupasReciboSoloPersonaRef = (ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasReciboPersonaRef") != null) ? ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasReciboPersonaRef").getValor() : "";
        if (tupasReciboSoloPersonaRef.contains(tupa.getTupaProcedimientoId().getCodProg().substring(8))) {
            return true;
        }
        return false;
    }

    public boolean solicitarReciboPersonaRefYSolicitante(SbTupa tupa) {
        String tupasReciboPersonaRefSol = (ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasReciboSolPersonaRef") != null) ? ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasReciboSolPersonaRef").getValor() : "";
        if (tupasReciboPersonaRefSol.contains(tupa.getTupaProcedimientoId().getCodProg().substring(8))) {
            return true;
        }
        return false;
    }
    
    public boolean mostrarPersonaRefSinRecibo(SbTupa tupa) {
        String tupasReciboPersonaRefSol = (ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasPersonaRefSinRecibo") != null) ? ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasPersonaRefSinRecibo").getValor() : "";
        if (tupasReciboPersonaRefSol.contains(tupa.getTupaProcedimientoId().getCodProg().substring(8))) {
            return true;
        }
        return false;
    }
    
    public boolean solicitarPersonaRef(SbTupa tupa) {
        String tupasReciboPersonaRefSol = (ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasPersonaRefObligatorio") != null) ? ejbSbParametroFacade.obtenerParametroXNombre("VentanillaVirtual_tupasPersonaRefObligatorio").getValor() : "";
        if (tupasReciboPersonaRefSol.contains(tupa.getTupaProcedimientoId().getCodProg().substring(8))) {
            return true;
        }
        return false;
    }

    public boolean tieneDatosPersonaRef(){
        boolean flagValido = true;
        if (registro.getTipoDocRefPersonaId() == null || registro.getDocumentoRefPersona() == null || registro.getDocumentoRefPersona().equals("")) {
            flagValido = false;
            JsfUtil.mensajeError("Debe ingresar el tipo y número de documento de la Persona a la que solicita el trámite.");
        }
        if (registro.getApePatRefPersona() == null || registro.getApePatRefPersona().equals("")) {
            flagValido = false;
            JsfUtil.mensajeError("Debe ingresar el Apellido Paterno de la Persona a la que solicita el trámite.");
        }
        if (registro.getNombresRefPersona() == null || registro.getNombresRefPersona().equals("")) {
            flagValido = false;
            JsfUtil.mensajeError("Debe ingresar los Nombres de la Pesona a la que solicita el trámite.");
        }
        return flagValido;
    }
    
    public SbExpVirtualSolicitud buscarReciboUsadoSolicitud(Map mMap) {
        return ejbSbExpVirtualSolicitudFacade.buscarSolicitudPorRecibo(mMap);
    }

    public SbExpVirtualSolicitud getSbExpVirtualSolicitud(java.lang.Long id) {
        return ejbSbExpVirtualSolicitudFacade.find(id);
    }

    @FacesConverter(forClass = SbExpVirtualSolicitud.class)
    public static class SbExpVirtualSolicitudControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TramiteVirtualController controller = (TramiteVirtualController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tramiteVirtualController");
            return controller.getSbExpVirtualSolicitud(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbExpVirtualSolicitud) {
                SbExpVirtualSolicitud o = (SbExpVirtualSolicitud) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SbExpVirtualSolicitud.class.getName());
            }
        }

    }
    
    public String estiloPorTiempo(SbExpVirtualSolicitud s) {       
        if (s.getEstadoId().getCodProg().equals("TP_EXVIEST_OBSREQ")){
            return "datatable-row-rojo";
        } else {
            return "datatable-row-normal";
        }        
    }
    
    public void visualizaAvisoSede() {       
        if (tupaSelected != null) {
            List<ArrayRecord> buscaProcesoTramite = ejbCdExpedienteFacade.buscaProcesoTramiteRevisa(tupaSelected.getCydocIdProceso());
            if (buscaProcesoTramite != null ) {
                if (buscaProcesoTramite.size() > 0) {
                    setMensajeAvisoSede("(La cita de Polígono debe registrarla en la misma Sede seleccionada)");
                } else {
                    setMensajeAvisoSede("");
                }         
            } else {
                setMensajeAvisoSede("");
            }
        }    
    }
}
