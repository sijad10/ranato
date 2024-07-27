/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.jsf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbEmpresaExtranjeraFacade;
import pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SspAgenciaFinancieraFacade;
import pe.gob.sucamec.bdintegrado.bean.SspContactoFacade;
import pe.gob.sucamec.bdintegrado.bean.SspJefeSeguridadFacade;
import pe.gob.sucamec.bdintegrado.bean.SspRegistroFacade;
import pe.gob.sucamec.bdintegrado.bean.SspRepresentanteRegistroFacade;
import pe.gob.sucamec.bdintegrado.bean.SspResolucionFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbEmpresaExtranjera;
import pe.gob.sucamec.bdintegrado.data.SbPaisGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspAgenciaFinanciera;
import pe.gob.sucamec.bdintegrado.data.SspCartaFianza;
import pe.gob.sucamec.bdintegrado.data.SspContacto;
import pe.gob.sucamec.bdintegrado.data.SspJefeSeguridad;
import pe.gob.sucamec.bdintegrado.data.SspParticipante;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SspRegistroEvento;
import pe.gob.sucamec.bdintegrado.data.SspRepresentanteRegistro;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.UploadFilesController;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sel.gssp.data.LocalAutorizacionDetalle;
import pe.gob.sucamec.sel.gssp.data.PersonaDetalle;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.PideMigraciones;
import wspide.PideMigraciones_Service;
import pe.gob.sucamec.sel.gssp.jsf.GsspSolicitudAutorizacionController;
/**
 *
 * @author mpalomino
 */
@Named(value = "gsspSolicitudCertificacionRMEFController")
@SessionScoped
public class GsspSolicitudCertificacionRMEF implements Serializable{
    
    @Inject
    UploadFilesController uploadFilesController;
    @Inject
    WsTramDoc wsTramDocController;
    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspResolucionFacade ejbSspResolucionFacade;
     @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade ejbTipoSeguridadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroFacade ejbSspRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt ejbSbDistritoFacadeGtFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbEmpresaExtranjeraFacade ejbSbEmpresaExtranjera; 
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRepresentanteRegistroFacade ejbSspRepresentanteRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspContactoFacade ejbSspContactoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspAgenciaFinancieraFacade ejbSspAgenciaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspJefeSeguridadFacade ejbSspJefeSeguridadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    
    private EstadoCrud estado;
    private List<TipoBaseGt> formTipoRegistroLista;
    private TipoBaseGt formTipoRegistroSelected;
    private String tipoRegistroCodProg;
    
    private GsspSolicitudAutorizacionController gsspSolicitudAutorizacionController;
    
    private SbPersonaGt regAdminist;
    private SbPersonaGt regUserAdminist;
    
    private String observacion;
    
    private TipoBaseGt regTipoProcesoId;

    private String administRazonSocial;
    private String administRUC;
    
    private boolean renderMostrarFormSolicCertRMEF;
    
    private String codigoOficina;
    private String nombreOficina;
    private String direccionOficina;
    private String referenciaOficina;
    private String latitudOficina;
    private String longitudOficina;
    private TipoSeguridad tipoLocalOficina;
    private SbDistritoGt distritoIdOficina;
    private List<TipoSeguridad> regTipLocalLPSList;
    private List<SbDistritoGt> regDistritoLPSList;
    private int checkTipoRiesgo;
    
    private List<TipoBaseGt> regTipoViasLPSList_Form;
    private TipoBaseGt regTipoViasLPSSelected_Form;
    private List<TipoSeguridad> regTipUsoLPSList_Form;
    private TipoSeguridad regTipUsoLPSSelected_Form;
    private String regDireccionLPS_Form;
    private String regNroFisicoLPS_Form;
    private String regReferenciaLPS_Form;
    private String regGeoLatitudLPS_Form;
    private String regGeoLongitudPS_Form;
    private List<SbDistritoGt> listaDistritoMapaLocal;
    private List<LocalAutorizacionDetalle> localAutorizacionListado;
    private String localAutorizacionSelectedString;
    Double lat_x, long_x;
    private boolean oficinaDataIngresada;
    
    private String regPaisSelected_id;
    private String accionistaNumDoc, accionistaApePat, accionistaApeMat, accionistaNombres;
    private SbPersonaGt regDatoPersonaByNumDocRL;
    private TipoBaseGt regTipoDocSelected;
    private String regNroDocBuscar;
    private List<SbPersonaGt> regPersonasRLList;
    private SbPersonaGt regPersonasRLSelected;

    private SbRelacionPersonaGt registroSbRelacionPersonaGt;

    private String regDomicilioRLSelected;

    private SbDireccionGt regSbDireccionRL;

    private List<SbDistritoGt> regDistritoRLSList;
    private SbDistritoGt regDistritoRLSelected;
    private int cantNumerosMinimaCarnetTipoDocRepresentanteLegal;
    private String cantNumerosDniCarnetTipoDocRepresentanteLegal;
    private Date regRepresentanteFechaNacimiento;
    private boolean regDisabledRepresentanteFechaNacimiento = true;
    private boolean renderEsAccionista = false;
    private List<SbPaisGt> regPaisLista;
    private SbPaisGt regPaisSelected;
    private SbEmpresaExtranjera regParticipacionistaData;
    
    private boolean buscaNuevoModalRepresentanteLegal = false;
    private boolean buscaNuevoModalJefeSeg = false;
    private Date fechaMayorDeEdadCalendario;
    private List<PersonaDetalle> personaDetalleListado;
    private String personaDetalleSelectedString;
    private String tituloBuscaModalRepresentanteLegal;
    private List<TipoBaseGt> regTipoDocList;
    private TipoBaseGt regTipoPersonaSelected;
    
    private String regNombreJefeEncontrado;
    private String cargoJefeSeg;
    private String prioridadJefeSeg;
    private TipoBaseGt regPrioridadLPSSelected;
    private List<TipoBaseGt> regPrioridadLPSList;
    private ArrayList<SspJefeSeguridad> lstJefes;
    private SspJefeSeguridad jefeSelected;
    private TipoBaseGt regTipDocJefeSelected;
    private String regNumeroDocJefeSeg;
    private SbPersonaGt regDatosJefeEncontrado;
    private Long contRegJefe;
    
    private TipoBaseGt regPrioridadMCSelected;
    private TipoBaseGt regTipoMedioContactoLPSSelected;
    private ArrayList<SspContacto> lstContactos;
    private List<SspContacto> lstContactosList;
    private SspContacto selectedContacto;
    private List<SspContacto> listaUpdateContactosActivos = new ArrayList<>();
    private Long contRegContacto;
    private String regtextoContactoLPS;
    private String regtextoCorreoContactoLPS;
    private List<TipoBaseGt> regTipoMedioContactoLPSList;
    
    private String nroResSBS;
    private Date fechaResSBS;
    private String nomArchivoResSBS;
    private String nomArchivoResSBSAnterior;
    private UploadedFile fileResSBS;
    private byte[] resSBSByte;
    private StreamedContent archivoResSBS;
    
    private String nomArchivoMR;
    private String nomArchivoMRAnterior;
    private UploadedFile fileResMR;
    private byte[] MRByte;
    private StreamedContent archivoMR;
    
    private SspRegistro registro;
    private SspAgenciaFinanciera agencia;
    private SspJefeSeguridad jefeRegistro;
    private SspRepresentanteRegistro representanteRegistro;
    
    private TipoBaseGt regTipoRegistroSelected;
    private TipoBaseGt regTipoOperacionSelected;
    
    private String filtroBuscarPor;
    private String filtroNumero;
    private Date filtroFechaIniSelected;
    private Date filtroFechaFinSelected;
    private Date fechaMinimaCalendario;
    private Date fechaMaximaCalendario;
    private List<TipoSeguridad> filtroTipoEstadoListado;
    private TipoSeguridad filtroTipoEstadoSelected;
    private List<Map> resultados;
    private List<SspRegistroEvento> lstObservaciones;
    private boolean habilitarVerResSBS, habilitarVerResMR;
    
    private List<Map> resultadosSeleccionados;
    private boolean blnProcesoTransmision = false;
    private Integer totalProcesados = 0;
    private Integer actualProcesado = 0;
    private Integer progress = 0;
    

    public UploadFilesController getUploadFilesController() {
        return uploadFilesController;
    }

    public void setUploadFilesController(UploadFilesController uploadFilesController) {
        this.uploadFilesController = uploadFilesController;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<Map> getResultados() {
        return resultados;
    }

    public void setResultados(List<Map> resultados) {
        this.resultados = resultados;
    }

    public List<SspRegistroEvento> getLstObservaciones() {
        return lstObservaciones;
    }

    public void setLstObservaciones(List<SspRegistroEvento> lstObservaciones) {
        this.lstObservaciones = lstObservaciones;
    }

    public SbPersonaFacadeGt getEjbSbPersonaFacade() {
        return ejbSbPersonaFacade;
    }

    public void setEjbSbPersonaFacade(SbPersonaFacadeGt ejbSbPersonaFacade) {
        this.ejbSbPersonaFacade = ejbSbPersonaFacade;
    }

    public SspResolucionFacade getEjbSspResolucionFacade() {
        return ejbSspResolucionFacade;
    }

    public void setEjbSspResolucionFacade(SspResolucionFacade ejbSspResolucionFacade) {
        this.ejbSspResolucionFacade = ejbSspResolucionFacade;
    }

    public TipoBaseFacadeGt getEjbTipoBaseFacade() {
        return ejbTipoBaseFacade;
    }

    public void setEjbTipoBaseFacade(TipoBaseFacadeGt ejbTipoBaseFacade) {
        this.ejbTipoBaseFacade = ejbTipoBaseFacade;
    }

    public TipoSeguridadFacade getEjbTipoSeguridadFacade() {
        return ejbTipoSeguridadFacade;
    }

    public void setEjbTipoSeguridadFacade(TipoSeguridadFacade ejbTipoSeguridadFacade) {
        this.ejbTipoSeguridadFacade = ejbTipoSeguridadFacade;
    }

    public SspRegistroFacade getEjbSspRegistroFacade() {
        return ejbSspRegistroFacade;
    }

    public void setEjbSspRegistroFacade(SspRegistroFacade ejbSspRegistroFacade) {
        this.ejbSspRegistroFacade = ejbSspRegistroFacade;
    }

    public SbParametroFacade getEjbSbParametroFacade() {
        return ejbSbParametroFacade;
    }

    public void setEjbSbParametroFacade(SbParametroFacade ejbSbParametroFacade) {
        this.ejbSbParametroFacade = ejbSbParametroFacade;
    }

    public SbUsuarioFacadeGt getEjbSbUsuarioFacadeGt() {
        return ejbSbUsuarioFacadeGt;
    }

    public void setEjbSbUsuarioFacadeGt(SbUsuarioFacadeGt ejbSbUsuarioFacadeGt) {
        this.ejbSbUsuarioFacadeGt = ejbSbUsuarioFacadeGt;
    }

    public SspRepresentanteRegistroFacade getEjbSspRepresentanteRegistroFacade() {
        return ejbSspRepresentanteRegistroFacade;
    }

    public void setEjbSspRepresentanteRegistroFacade(SspRepresentanteRegistroFacade ejbSspRepresentanteRegistroFacade) {
        this.ejbSspRepresentanteRegistroFacade = ejbSspRepresentanteRegistroFacade;
    }

    public SspContactoFacade getEjbSspContactoFacade() {
        return ejbSspContactoFacade;
    }

    public void setEjbSspContactoFacade(SspContactoFacade ejbSspContactoFacade) {
        this.ejbSspContactoFacade = ejbSspContactoFacade;
    }

    public SspAgenciaFinancieraFacade getEjbSspAgenciaFacade() {
        return ejbSspAgenciaFacade;
    }

    public void setEjbSspAgenciaFacade(SspAgenciaFinancieraFacade ejbSspAgenciaFacade) {
        this.ejbSspAgenciaFacade = ejbSspAgenciaFacade;
    }

    public SbNumeracionFacade getEjbSbNumeracionFacade() {
        return ejbSbNumeracionFacade;
    }

    public void setEjbSbNumeracionFacade(SbNumeracionFacade ejbSbNumeracionFacade) {
        this.ejbSbNumeracionFacade = ejbSbNumeracionFacade;
    }

    public SspJefeSeguridadFacade getEjbSspJefeSeguridadFacade() {
        return ejbSspJefeSeguridadFacade;
    }

    public void setEjbSspJefeSeguridadFacade(SspJefeSeguridadFacade ejbSspJefeSeguridadFacade) {
        this.ejbSspJefeSeguridadFacade = ejbSspJefeSeguridadFacade;
    }

    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public List<TipoBaseGt> getFormTipoRegistroLista() {
        return formTipoRegistroLista;
    }

    public void setFormTipoRegistroLista(List<TipoBaseGt> formTipoRegistroLista) {
        this.formTipoRegistroLista = formTipoRegistroLista;
    }

    public TipoBaseGt getFormTipoRegistroSelected() {
        return formTipoRegistroSelected;
    }

    public void setFormTipoRegistroSelected(TipoBaseGt formTipoRegistroSelected) {
        this.formTipoRegistroSelected = formTipoRegistroSelected;
    }

    public String getTipoRegistroCodProg() {
        return tipoRegistroCodProg;
    }

    public void setTipoRegistroCodProg(String tipoRegistroCodProg) {
        this.tipoRegistroCodProg = tipoRegistroCodProg;
    }

    public SbPersonaGt getRegAdminist() {
        return regAdminist;
    }

    public void setRegAdminist(SbPersonaGt regAdminist) {
        this.regAdminist = regAdminist;
    }

    public SbPersonaGt getRegUserAdminist() {
        return regUserAdminist;
    }

    public void setRegUserAdminist(SbPersonaGt regUserAdminist) {
        this.regUserAdminist = regUserAdminist;
    }

    public String getAdministRazonSocial() {
        return administRazonSocial;
    }

    public void setAdministRazonSocial(String administRazonSocial) {
        this.administRazonSocial = administRazonSocial;
    }

    public String getAdministRUC() {
        return administRUC;
    }

    public void setAdministRUC(String administRUC) {
        this.administRUC = administRUC;
    }

    public boolean isRenderMostrarFormSolicCertRMEF() {
        return renderMostrarFormSolicCertRMEF;
    }

    public void setRenderMostrarFormSolicCertRMEF(boolean renderMostrarFormSolicCertRMEF) {
        this.renderMostrarFormSolicCertRMEF = renderMostrarFormSolicCertRMEF;
    }

    public String getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(String codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public String getNombreOficina() {
        return nombreOficina;
    }

    public void setNombreOficina(String nombreOficina) {
        this.nombreOficina = nombreOficina;
    }

    public String getDireccionOficina() {
        return direccionOficina;
    }

    public void setDireccionOficina(String direccionOficina) {
        this.direccionOficina = direccionOficina;
    }

    public String getReferenciaOficina() {
        return referenciaOficina;
    }

    public void setReferenciaOficina(String referenciaOficina) {
        this.referenciaOficina = referenciaOficina;
    }

    public String getLatitudOficina() {
        return latitudOficina;
    }

    public void setLatitudOficina(String latitudOficina) {
        this.latitudOficina = latitudOficina;
    }

    public String getLongitudOficina() {
        return longitudOficina;
    }

    public void setLongitudOficina(String longitudOficina) {
        this.longitudOficina = longitudOficina;
    }

    public TipoSeguridad getTipoLocalOficina() {
        return tipoLocalOficina;
    }

    public void setTipoLocalOficina(TipoSeguridad tipoLocalOficina) {
        this.tipoLocalOficina = tipoLocalOficina;
    }

    public SbDistritoGt getDistritoIdOficina() {
        return distritoIdOficina;
    }

    public void setDistritoIdOficina(SbDistritoGt distritoIdOficina) {
        this.distritoIdOficina = distritoIdOficina;
    }

    public List<TipoSeguridad> getRegTipLocalLPSList() {
        return regTipLocalLPSList;
    }

    public void setRegTipLocalLPSList(List<TipoSeguridad> regTipLocalLPSList) {
        this.regTipLocalLPSList = regTipLocalLPSList;
    }

    public List<SbDistritoGt> getRegDistritoLPSList() {
        return regDistritoLPSList;
    }

    public void setRegDistritoLPSList(List<SbDistritoGt> regDistritoLPSList) {
        this.regDistritoLPSList = regDistritoLPSList;
    }

    public SbDistritoFacadeGt getEjbSbDistritoFacadeGtFacade() {
        return ejbSbDistritoFacadeGtFacade;
    }

    public void setEjbSbDistritoFacadeGtFacade(SbDistritoFacadeGt ejbSbDistritoFacadeGtFacade) {
        this.ejbSbDistritoFacadeGtFacade = ejbSbDistritoFacadeGtFacade;
    }

    public List<TipoBaseGt> getRegTipoViasLPSList_Form() {
        return regTipoViasLPSList_Form;
    }

    public void setRegTipoViasLPSList_Form(List<TipoBaseGt> regTipoViasLPSList_Form) {
        this.regTipoViasLPSList_Form = regTipoViasLPSList_Form;
    }

    public TipoBaseGt getRegTipoViasLPSSelected_Form() {
        return regTipoViasLPSSelected_Form;
    }

    public void setRegTipoViasLPSSelected_Form(TipoBaseGt regTipoViasLPSSelected_Form) {
        this.regTipoViasLPSSelected_Form = regTipoViasLPSSelected_Form;
    }

    public List<TipoSeguridad> getRegTipUsoLPSList_Form() {
        return regTipUsoLPSList_Form;
    }

    public void setRegTipUsoLPSList_Form(List<TipoSeguridad> regTipUsoLPSList_Form) {
        this.regTipUsoLPSList_Form = regTipUsoLPSList_Form;
    }

    public TipoSeguridad getRegTipUsoLPSSelected_Form() {
        return regTipUsoLPSSelected_Form;
    }

    public void setRegTipUsoLPSSelected_Form(TipoSeguridad regTipUsoLPSSelected_Form) {
        this.regTipUsoLPSSelected_Form = regTipUsoLPSSelected_Form;
    }

    public String getRegDireccionLPS_Form() {
        return regDireccionLPS_Form;
    }

    public void setRegDireccionLPS_Form(String regDireccionLPS_Form) {
        this.regDireccionLPS_Form = regDireccionLPS_Form;
    }

    public String getRegNroFisicoLPS_Form() {
        return regNroFisicoLPS_Form;
    }

    public void setRegNroFisicoLPS_Form(String regNroFisicoLPS_Form) {
        this.regNroFisicoLPS_Form = regNroFisicoLPS_Form;
    }

    public String getRegReferenciaLPS_Form() {
        return regReferenciaLPS_Form;
    }

    public void setRegReferenciaLPS_Form(String regReferenciaLPS_Form) {
        this.regReferenciaLPS_Form = regReferenciaLPS_Form;
    }

    public String getRegGeoLatitudLPS_Form() {
        return regGeoLatitudLPS_Form;
    }

    public void setRegGeoLatitudLPS_Form(String regGeoLatitudLPS_Form) {
        this.regGeoLatitudLPS_Form = regGeoLatitudLPS_Form;
    }

    public String getRegGeoLongitudPS_Form() {
        return regGeoLongitudPS_Form;
    }

    public void setRegGeoLongitudPS_Form(String regGeoLongitudPS_Form) {
        this.regGeoLongitudPS_Form = regGeoLongitudPS_Form;
    }

    public List<SbDistritoGt> getListaDistritoMapaLocal() {
        return listaDistritoMapaLocal;
    }

    public void setListaDistritoMapaLocal(List<SbDistritoGt> listaDistritoMapaLocal) {
        this.listaDistritoMapaLocal = listaDistritoMapaLocal;
    }

    public List<LocalAutorizacionDetalle> getLocalAutorizacionListado() {
        return localAutorizacionListado;
    }

    public void setLocalAutorizacionListado(List<LocalAutorizacionDetalle> localAutorizacionListado) {
        this.localAutorizacionListado = localAutorizacionListado;
    }

    public String getLocalAutorizacionSelectedString() {
        return localAutorizacionSelectedString;
    }

    public void setLocalAutorizacionSelectedString(String localAutorizacionSelectedString) {
        this.localAutorizacionSelectedString = localAutorizacionSelectedString;
    }

    public Double getLat_x() {
        return lat_x;
    }

    public void setLat_x(Double lat_x) {
        this.lat_x = lat_x;
    }

    public Double getLong_x() {
        return long_x;
    }

    public void setLong_x(Double long_x) {
        this.long_x = long_x;
    }

    public String getRegPaisSelected_id() {
        return regPaisSelected_id;
    }

    public void setRegPaisSelected_id(String regPaisSelected_id) {
        this.regPaisSelected_id = regPaisSelected_id;
    }

    public String getAccionistaNumDoc() {
        return accionistaNumDoc;
    }

    public void setAccionistaNumDoc(String accionistaNumDoc) {
        this.accionistaNumDoc = accionistaNumDoc;
    }

    public String getAccionistaApePat() {
        return accionistaApePat;
    }

    public void setAccionistaApePat(String accionistaApePat) {
        this.accionistaApePat = accionistaApePat;
    }

    public String getAccionistaApeMat() {
        return accionistaApeMat;
    }

    public void setAccionistaApeMat(String accionistaApeMat) {
        this.accionistaApeMat = accionistaApeMat;
    }

    public String getAccionistaNombres() {
        return accionistaNombres;
    }

    public void setAccionistaNombres(String accionistaNombres) {
        this.accionistaNombres = accionistaNombres;
    }

    public SbPersonaGt getRegDatoPersonaByNumDocRL() {
        return regDatoPersonaByNumDocRL;
    }

    public void setRegDatoPersonaByNumDocRL(SbPersonaGt regDatoPersonaByNumDocRL) {
        this.regDatoPersonaByNumDocRL = regDatoPersonaByNumDocRL;
    }

    public SbEmpresaExtranjeraFacade getEjbSbEmpresaExtranjera() {
        return ejbSbEmpresaExtranjera;
    }

    public void setEjbSbEmpresaExtranjera(SbEmpresaExtranjeraFacade ejbSbEmpresaExtranjera) {
        this.ejbSbEmpresaExtranjera = ejbSbEmpresaExtranjera;
    }

    public TipoBaseGt getRegTipoDocSelected() {
        return regTipoDocSelected;
    }

    public void setRegTipoDocSelected(TipoBaseGt regTipoDocSelected) {
        this.regTipoDocSelected = regTipoDocSelected;
    }

    public String getRegNroDocBuscar() {
        return regNroDocBuscar;
    }

    public void setRegNroDocBuscar(String regNroDocBuscar) {
        this.regNroDocBuscar = regNroDocBuscar;
    }

    public List<SbPersonaGt> getRegPersonasRLList() {
        return regPersonasRLList;
    }

    public void setRegPersonasRLList(List<SbPersonaGt> regPersonasRLList) {
        this.regPersonasRLList = regPersonasRLList;
    }

    public SbPersonaGt getRegPersonasRLSelected() {
        return regPersonasRLSelected;
    }

    public void setRegPersonasRLSelected(SbPersonaGt regPersonasRLSelected) {
        this.regPersonasRLSelected = regPersonasRLSelected;
    }

    public SbRelacionPersonaGt getRegistroSbRelacionPersonaGt() {
        return registroSbRelacionPersonaGt;
    }

    public void setRegistroSbRelacionPersonaGt(SbRelacionPersonaGt registroSbRelacionPersonaGt) {
        this.registroSbRelacionPersonaGt = registroSbRelacionPersonaGt;
    }

    public String getRegDomicilioRLSelected() {
        return regDomicilioRLSelected;
    }

    public void setRegDomicilioRLSelected(String regDomicilioRLSelected) {
        this.regDomicilioRLSelected = regDomicilioRLSelected;
    }

    public SbDireccionGt getRegSbDireccionRL() {
        return regSbDireccionRL;
    }

    public void setRegSbDireccionRL(SbDireccionGt regSbDireccionRL) {
        this.regSbDireccionRL = regSbDireccionRL;
    }

    public List<SbDistritoGt> getRegDistritoRLSList() {
        return regDistritoRLSList;
    }

    public void setRegDistritoRLSList(List<SbDistritoGt> regDistritoRLSList) {
        this.regDistritoRLSList = regDistritoRLSList;
    }

    public SbDistritoGt getRegDistritoRLSelected() {
        return regDistritoRLSelected;
    }

    public void setRegDistritoRLSelected(SbDistritoGt regDistritoRLSelected) {
        this.regDistritoRLSelected = regDistritoRLSelected;
    }

    public int getCantNumerosMinimaCarnetTipoDocRepresentanteLegal() {
        return cantNumerosMinimaCarnetTipoDocRepresentanteLegal;
    }

    public void setCantNumerosMinimaCarnetTipoDocRepresentanteLegal(int cantNumerosMinimaCarnetTipoDocRepresentanteLegal) {
        this.cantNumerosMinimaCarnetTipoDocRepresentanteLegal = cantNumerosMinimaCarnetTipoDocRepresentanteLegal;
    }

    public String getCantNumerosDniCarnetTipoDocRepresentanteLegal() {
        return cantNumerosDniCarnetTipoDocRepresentanteLegal;
    }

    public void setCantNumerosDniCarnetTipoDocRepresentanteLegal(String cantNumerosDniCarnetTipoDocRepresentanteLegal) {
        this.cantNumerosDniCarnetTipoDocRepresentanteLegal = cantNumerosDniCarnetTipoDocRepresentanteLegal;
    }

    public Date getRegRepresentanteFechaNacimiento() {
        return regRepresentanteFechaNacimiento;
    }

    public void setRegRepresentanteFechaNacimiento(Date regRepresentanteFechaNacimiento) {
        this.regRepresentanteFechaNacimiento = regRepresentanteFechaNacimiento;
    }

    public boolean isRegDisabledRepresentanteFechaNacimiento() {
        return regDisabledRepresentanteFechaNacimiento;
    }

    public void setRegDisabledRepresentanteFechaNacimiento(boolean regDisabledRepresentanteFechaNacimiento) {
        this.regDisabledRepresentanteFechaNacimiento = regDisabledRepresentanteFechaNacimiento;
    }

    public boolean isRenderEsAccionista() {
        return renderEsAccionista;
    }

    public void setRenderEsAccionista(boolean renderEsAccionista) {
        this.renderEsAccionista = renderEsAccionista;
    }

    public List<SbPaisGt> getRegPaisLista() {
        return regPaisLista;
    }

    public void setRegPaisLista(List<SbPaisGt> regPaisLista) {
        this.regPaisLista = regPaisLista;
    }

    public SbPaisGt getRegPaisSelected() {
        return regPaisSelected;
    }

    public void setRegPaisSelected(SbPaisGt regPaisSelected) {
        this.regPaisSelected = regPaisSelected;
    }

    public SbEmpresaExtranjera getRegParticipacionistaData() {
        return regParticipacionistaData;
    }

    public void setRegParticipacionistaData(SbEmpresaExtranjera regParticipacionistaData) {
        this.regParticipacionistaData = regParticipacionistaData;
    }

    public boolean isBuscaNuevoModalRepresentanteLegal() {
        return buscaNuevoModalRepresentanteLegal;
    }

    public void setBuscaNuevoModalRepresentanteLegal(boolean buscaNuevoModalRepresentanteLegal) {
        this.buscaNuevoModalRepresentanteLegal = buscaNuevoModalRepresentanteLegal;
    }

    public boolean isBuscaNuevoModalAccionista() {
        return buscaNuevoModalJefeSeg;
    }

    public void setBuscaNuevoModalAccionista(boolean buscaNuevoModalJefeSeg) {
        this.buscaNuevoModalJefeSeg = buscaNuevoModalJefeSeg;
    }

    public Date getFechaMayorDeEdadCalendario() {
        return fechaMayorDeEdadCalendario;
    }

    public void setFechaMayorDeEdadCalendario(Date fechaMayorDeEdadCalendario) {
        this.fechaMayorDeEdadCalendario = fechaMayorDeEdadCalendario;
    }

    public List<PersonaDetalle> getPersonaDetalleListado() {
        return personaDetalleListado;
    }

    public void setPersonaDetalleListado(List<PersonaDetalle> personaDetalleListado) {
        this.personaDetalleListado = personaDetalleListado;
    }

    public String getPersonaDetalleSelectedString() {
        return personaDetalleSelectedString;
    }

    public void setPersonaDetalleSelectedString(String personaDetalleSelectedString) {
        this.personaDetalleSelectedString = personaDetalleSelectedString;
    }

    public String getTituloBuscaModalRepresentanteLegal() {
        return tituloBuscaModalRepresentanteLegal;
    }

    public void setTituloBuscaModalRepresentanteLegal(String tituloBuscaModalRepresentanteLegal) {
        this.tituloBuscaModalRepresentanteLegal = tituloBuscaModalRepresentanteLegal;
    }

    public List<TipoBaseGt> getRegTipoDocList() {
        return regTipoDocList;
    }

    public void setRegTipoDocList(List<TipoBaseGt> regTipoDocList) {
        this.regTipoDocList = regTipoDocList;
    }

    public boolean isBuscaNuevoModalJefeSeg() {
        return buscaNuevoModalJefeSeg;
    }

    public void setBuscaNuevoModalJefeSeg(boolean buscaNuevoModalJefeSeg) {
        this.buscaNuevoModalJefeSeg = buscaNuevoModalJefeSeg;
    }

    public TipoBaseGt getRegTipoPersonaSelected() {
        return regTipoPersonaSelected;
    }

    public void setRegTipoPersonaSelected(TipoBaseGt regTipoPersonaSelected) {
        this.regTipoPersonaSelected = regTipoPersonaSelected;
    }

    public String getRegNombreJefeEncontrado() {
        return regNombreJefeEncontrado;
    }

    public void setRegNombreJefeEncontrado(String regNombreJefeEncontrado) {
        this.regNombreJefeEncontrado = regNombreJefeEncontrado;
    }

    public TipoBaseGt getRegPrioridadLPSSelected() {
        return regPrioridadLPSSelected;
    }

    public void setRegPrioridadLPSSelected(TipoBaseGt regPrioridadLPSSelected) {
        this.regPrioridadLPSSelected = regPrioridadLPSSelected;
    }

    public List<TipoBaseGt> getRegPrioridadLPSList() {
        return regPrioridadLPSList;
    }

    public void setRegPrioridadLPSList(List<TipoBaseGt> regPrioridadLPSList) {
        this.regPrioridadLPSList = regPrioridadLPSList;
    }

    public String getCargoJefeSeg() {
        return cargoJefeSeg;
    }

    public void setCargoJefeSeg(String cargoJefeSeg) {
        this.cargoJefeSeg = cargoJefeSeg;
    }

    public ArrayList<SspJefeSeguridad> getLstJefes() {
        return lstJefes;
    }

    public void setLstJefes(ArrayList<SspJefeSeguridad> lstJefes) {
        this.lstJefes = lstJefes;
    }

    public SspJefeSeguridad getJefeSelected() {
        return jefeSelected;
    }

    public void setJefeSelected(SspJefeSeguridad jefeSelected) {
        this.jefeSelected = jefeSelected;
    }

    public TipoBaseGt getRegTipDocJefeSelected() {
        return regTipDocJefeSelected;
    }

    public void setRegTipDocJefeSelected(TipoBaseGt regTipDocJefeSelected) {
        this.regTipDocJefeSelected = regTipDocJefeSelected;
    }

    public String getRegNumeroDocJefeSeg() {
        return regNumeroDocJefeSeg;
    }

    public void setRegNumeroDocJefeSeg(String regNumeroDocJefeSeg) {
        this.regNumeroDocJefeSeg = regNumeroDocJefeSeg;
    }

    public SbPersonaGt getRegDatosJefeEncontrado() {
        return regDatosJefeEncontrado;
    }

    public void setRegDatosJefeEncontrado(SbPersonaGt regDatosJefeEncontrado) {
        this.regDatosJefeEncontrado = regDatosJefeEncontrado;
    }

    public String getPrioridadJefeSeg() {
        return prioridadJefeSeg;
    }

    public void setPrioridadJefeSeg(String prioridadJefeSeg) {
        this.prioridadJefeSeg = prioridadJefeSeg;
    }

    public Long getContRegJefe() {
        return contRegJefe;
    }

    public void setContRegJefe(Long contRegJefe) {
        this.contRegJefe = contRegJefe;
    }

    public TipoBaseGt getRegPrioridadMCSelected() {
        return regPrioridadMCSelected;
    }

    public void setRegPrioridadMCSelected(TipoBaseGt regPrioridadMCSelected) {
        this.regPrioridadMCSelected = regPrioridadMCSelected;
    }

    public TipoBaseGt getRegTipoMedioContactoLPSSelected() {
        return regTipoMedioContactoLPSSelected;
    }

    public void setRegTipoMedioContactoLPSSelected(TipoBaseGt regTipoMedioContactoLPSSelected) {
        this.regTipoMedioContactoLPSSelected = regTipoMedioContactoLPSSelected;
    }

    public ArrayList<SspContacto> getLstContactos() {
        return lstContactos;
    }

    public void setLstContactos(ArrayList<SspContacto> lstContactos) {
        this.lstContactos = lstContactos;
    }

    public List<SspContacto> getLstContactosList() {
        return lstContactosList;
    }

    public void setLstContactosList(List<SspContacto> lstContactosList) {
        this.lstContactosList = lstContactosList;
    }

    public SspContacto getSelectedContacto() {
        return selectedContacto;
    }

    public void setSelectedContacto(SspContacto selectedContacto) {
        this.selectedContacto = selectedContacto;
    }

    public List<SspContacto> getListaUpdateContactosActivos() {
        return listaUpdateContactosActivos;
    }

    public void setListaUpdateContactosActivos(List<SspContacto> listaUpdateContactosActivos) {
        this.listaUpdateContactosActivos = listaUpdateContactosActivos;
    }

    public Long getContRegContacto() {
        return contRegContacto;
    }

    public void setContRegContacto(Long contRegContacto) {
        this.contRegContacto = contRegContacto;
    }

    public String getRegtextoContactoLPS() {
        return regtextoContactoLPS;
    }

    public void setRegtextoContactoLPS(String regtextoContactoLPS) {
        this.regtextoContactoLPS = regtextoContactoLPS;
    }

    public String getRegtextoCorreoContactoLPS() {
        return regtextoCorreoContactoLPS;
    }

    public void setRegtextoCorreoContactoLPS(String regtextoCorreoContactoLPS) {
        this.regtextoCorreoContactoLPS = regtextoCorreoContactoLPS;
    }

    public List<TipoBaseGt> getRegTipoMedioContactoLPSList() {
        return regTipoMedioContactoLPSList;
    }

    public void setRegTipoMedioContactoLPSList(List<TipoBaseGt> regTipoMedioContactoLPSList) {
        this.regTipoMedioContactoLPSList = regTipoMedioContactoLPSList;
    }

    public String getNroResSBS() {
        return nroResSBS;
    }

    public void setNroResSBS(String nroResSBS) {
        this.nroResSBS = nroResSBS;
    }

    public Date getFechaResSBS() {
        return fechaResSBS;
    }

    public void setFechaResSBS(Date fechaResSBS) {
        this.fechaResSBS = fechaResSBS;
    }

    public String getNomArchivoResSBS() {
        return nomArchivoResSBS;
    }

    public void setNomArchivoResSBS(String nomArchivoResSBS) {
        this.nomArchivoResSBS = nomArchivoResSBS;
    }

    public String getNomArchivoResSBSAnterior() {
        return nomArchivoResSBSAnterior;
    }

    public void setNomArchivoResSBSAnterior(String nomArchivoResSBSAnterior) {
        this.nomArchivoResSBSAnterior = nomArchivoResSBSAnterior;
    }

    public UploadedFile getFileResSBS() {
        return fileResSBS;
    }

    public void setFileResSBS(UploadedFile fileResSBS) {
        this.fileResSBS = fileResSBS;
    }

    public byte[] getResSBSByte() {
        return resSBSByte;
    }

    public void setResSBSByte(byte[] resSBSByte) {
        this.resSBSByte = resSBSByte;
    }

    public StreamedContent getArchivoResSBS() {
        return archivoResSBS;
    }

    public void setArchivoResSBS(StreamedContent archivoResSBS) {
        this.archivoResSBS = archivoResSBS;
    }

    public String getNomArchivoMR() {
        return nomArchivoMR;
    }

    public void setNomArchivoMR(String nomArchivoMR) {
        this.nomArchivoMR = nomArchivoMR;
    }

    public String getNomArchivoMRAnterior() {
        return nomArchivoMRAnterior;
    }

    public void setNomArchivoMRAnterior(String nomArchivoMRAnterior) {
        this.nomArchivoMRAnterior = nomArchivoMRAnterior;
    }

    public UploadedFile getFileResMR() {
        return fileResMR;
    }

    public void setFileResMR(UploadedFile fileResMR) {
        this.fileResMR = fileResMR;
    }

    public byte[] getMRByte() {
        return MRByte;
    }

    public void setMRByte(byte[] MRByte) {
        this.MRByte = MRByte;
    }

    public StreamedContent getArchivoMR() {
        return archivoMR;
    }

    public void setArchivoMR(StreamedContent archivoMR) {
        this.archivoMR = archivoMR;
    }

    public SspRegistro getRegistro() {
        return registro;
    }

    public void setRegistro(SspRegistro registro) {
        this.registro = registro;
    }

    public SspAgenciaFinanciera getAgencia() {
        return agencia;
    }

    public void setAgencia(SspAgenciaFinanciera agencia) {
        this.agencia = agencia;
    }

    public SspJefeSeguridad getJefeRegistro() {
        return jefeRegistro;
    }

    public void setJefeRegistro(SspJefeSeguridad jefeRegistro) {
        this.jefeRegistro = jefeRegistro;
    }

    public SspRepresentanteRegistro getRepresentanteRegistro() {
        return representanteRegistro;
    }

    public boolean isOficinaDataIngresada() {
        return oficinaDataIngresada;
    }

    public void setOficinaDataIngresada(boolean oficinaDataIngresada) {
        this.oficinaDataIngresada = oficinaDataIngresada;
    }

    public TipoBaseGt getRegTipoProcesoId() {
        return regTipoProcesoId;
    }

    public void setRegTipoProcesoId(TipoBaseGt regTipoProcesoId) {
        this.regTipoProcesoId = regTipoProcesoId;
    }

    public TipoBaseGt getRegTipoRegistroSelected() {
        return regTipoRegistroSelected;
    }

    public void setRegTipoRegistroSelected(TipoBaseGt regTipoRegistroSelected) {
        this.regTipoRegistroSelected = regTipoRegistroSelected;
    }

    public TipoBaseGt getRegTipoOperacionSelected() {
        return regTipoOperacionSelected;
    }

    public void setRegTipoOperacionSelected(TipoBaseGt regTipoOperacionSelected) {
        this.regTipoOperacionSelected = regTipoOperacionSelected;
    }

    public void setRepresentanteRegistro(SspRepresentanteRegistro representanteRegistro) {
        this.representanteRegistro = representanteRegistro;
    }

    public String getFiltroBuscarPor() {
        return filtroBuscarPor;
    }

    public void setFiltroBuscarPor(String filtroBuscarPor) {
        this.filtroBuscarPor = filtroBuscarPor;
    }

    public String getFiltroNumero() {
        return filtroNumero;
    }

    public void setFiltroNumero(String filtroNumero) {
        this.filtroNumero = filtroNumero;
    }

    public Date getFiltroFechaIniSelected() {
        return filtroFechaIniSelected;
    }

    public void setFiltroFechaIniSelected(Date filtroFechaIniSelected) {
        this.filtroFechaIniSelected = filtroFechaIniSelected;
    }

    public Date getFiltroFechaFinSelected() {
        return filtroFechaFinSelected;
    }

    public void setFiltroFechaFinSelected(Date filtroFechaFinSelected) {
        this.filtroFechaFinSelected = filtroFechaFinSelected;
    }

    public Date getFechaMinimaCalendario() {
        return fechaMinimaCalendario;
    }

    public void setFechaMinimaCalendario(Date fechaMinimaCalendario) {
        this.fechaMinimaCalendario = fechaMinimaCalendario;
    }

    public Date getFechaMaximaCalendario() {
        return fechaMaximaCalendario;
    }

    public void setFechaMaximaCalendario(Date fechaMaximaCalendario) {
        this.fechaMaximaCalendario = fechaMaximaCalendario;
    }

    public List<TipoSeguridad> getFiltroTipoEstadoListado() {
        return filtroTipoEstadoListado;
    }

    public void setFiltroTipoEstadoListado(List<TipoSeguridad> filtroTipoEstadoListado) {
        this.filtroTipoEstadoListado = filtroTipoEstadoListado;
    }

    public TipoSeguridad getFiltroTipoEstadoSelected() {
        return filtroTipoEstadoSelected;
    }

    public void setFiltroTipoEstadoSelected(TipoSeguridad filtroTipoEstadoSelected) {
        this.filtroTipoEstadoSelected = filtroTipoEstadoSelected;
    }

    public boolean isHabilitarVerResSBS() {
        return habilitarVerResSBS;
    }

    public void setHabilitarVerResSBS(boolean habilitarVerResSBS) {
        this.habilitarVerResSBS = habilitarVerResSBS;
    }

    public boolean isHabilitarVerResMR() {
        return habilitarVerResMR;
    }

    public void setHabilitarVerResMR(boolean habilitarVerResMR) {
        this.habilitarVerResMR = habilitarVerResMR;
    }

    public List<Map> getResultadosSeleccionados() {
        return resultadosSeleccionados;
    }

    public void setResultadosSeleccionados(List<Map> resultadosSeleccionados) {
        this.resultadosSeleccionados = resultadosSeleccionados;
    }

    public boolean isBlnProcesoTransmision() {
        return blnProcesoTransmision;
    }

    public void setBlnProcesoTransmision(boolean blnProcesoTransmision) {
        this.blnProcesoTransmision = blnProcesoTransmision;
    }

    public Integer getTotalProcesados() {
        return totalProcesados;
    }

    public void setTotalProcesados(Integer totalProcesados) {
        this.totalProcesados = totalProcesados;
    }

    public Integer getActualProcesado() {
        return actualProcesado;
    }

    public void setActualProcesado(Integer actualProcesado) {
        this.actualProcesado = actualProcesado;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public int getCheckTipoRiesgo() {
        return checkTipoRiesgo;
    }

    public void setCheckTipoRiesgo(int checkTipoRiesgo) {
        this.checkTipoRiesgo = checkTipoRiesgo;
    }
    
    
    public void iniciarValores_CrearSolicitud() { 
        
        regTipoProcesoId=ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_CERT_CEF");
        regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE'");
        tituloBuscaModalRepresentanteLegal = JsfUtil.bundleBDIntegrado("gsspSolicitudAutorizacion_Titulo_Modal_Representante_Legal");
        personaDetalleListado = null;
        personaDetalleListado = new ArrayList<PersonaDetalle>();
        regPrioridadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_CONTAC");
        regTipoMedioContactoLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_MEDCO");
        regTipoRegistroSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR");
        regTipoOperacionSelected = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"); 
        lstJefes=new ArrayList<SspJefeSeguridad>();
        lstContactos = new ArrayList<SspContacto>();
        lstContactosList = null;
        codigoOficina="";
        nombreOficina="";
        tipoLocalOficina=null;
        distritoIdOficina=null;
        direccionOficina="";
        referenciaOficina="";
        lat_x=0.0;
        long_x=0.0;
        nroResSBS=null;
        fechaResSBS=null;
        oficinaDataIngresada=false;
        personaDetalleSelectedString=null;
        regNombreJefeEncontrado="";
        regPrioridadLPSSelected=null;
        cargoJefeSeg="";
        regPrioridadMCSelected=null;
        regTipoMedioContactoLPSSelected=null;
        regtextoContactoLPS="";
        nroResSBS="";
        fechaResSBS=null;
        nomArchivoResSBS=null;
        nomArchivoMR=null;
        habilitarVerResMR=false;
        habilitarVerResSBS=false;
        checkTipoRiesgo=0;
        
        registro = new SspRegistro();
        agencia = new SspAgenciaFinanciera();
        jefeRegistro = new SspJefeSeguridad();
        representanteRegistro = new SspRepresentanteRegistro();
    
    }
    
    public String mostrarFormCrearSolicitudCertificacionRMEF() {
        
        //=============================================
        //========    Administrado - inicio   =========
        //=============================================
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());

        estado = EstadoCrud.CREARCERTVEHBLIND;
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();
        
        regTipLocalLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_OFI_FINAN");
        regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
        
        iniciarValores_CrearSolicitud();

        //renderMostrarFormSolicCertVehBlindados = false;
        return "/aplicacion/gssp/gsspCertificacion/gssp_financiera/Create";

    }
    
    public static String obtnerRutaServer(String directorio) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            URL resourceUrl = externalContext.getResource("/resources/");
            if (resourceUrl != null) {
                String fullPath = resourceUrl.getPath();
                int index = fullPath.indexOf("/resources/");
                if (index >= 0) {
                    return (fullPath.substring(0, index) + "/resources/").replace("/server/", "/");
                }
            }
        } catch (MalformedURLException e) {
            // Manejar la excepción o mostrar un mensaje de error
        }
        return null;
    }
    
    public void openDlgMapa() {

        if (distritoIdOficina == null) {
            JsfUtil.mensajeAdvertencia("Debe seleccionar el ambito de operaciones");
        } else {

            //regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_VIA_JIR','TP_VIA_AVE','TP_VIA_PAS','TP_VIA_URB','TP_VIA_CAL','TP_VIA_OTRO','TP_VIA_VIRT'");        
            regTipoViasLPSList_Form = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
            regTipoViasLPSSelected_Form = null;

            //regTipLocalLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'TP_LOC_PRIN','TP_LOC_SECU'");        
            //regTipLocalLPSSelected_Form = null;
            regTipUsoLPSList_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'");
            regTipUsoLPSSelected_Form = ejbTipoSeguridadFacade.tipoSeguridadXCodigosProgs("'LC_OPER_SSP'").get(0);

            regDireccionLPS_Form = null;
            regNroFisicoLPS_Form = null;
            regReferenciaLPS_Form = null;
            regGeoLatitudLPS_Form = null;
            regGeoLongitudPS_Form = null;

            String nombre_dis = "";
            String nombre_prov = "";
            String nombre_dep = "";
            long iddist = distritoIdOficina.getId();//10101;
            listaDistritoMapaLocal = ejbSbDistritoFacadeGtFacade.lstDistritosxID(iddist); 
            
            SbDistritoGt distrito_x =  ejbSbDistritoFacadeGtFacade.find(iddist);
            
            nombre_dis      = distrito_x.getNombre();//#{item.provinciaId.departamentoId.nombre} / #{item.provinciaId.nombre} / #{item.nombre};
            nombre_prov      = distrito_x.getProvinciaId().getNombre(); 
            nombre_dep      = distrito_x.getProvinciaId().getDepartamentoId().getNombre();
            
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('wvDlgMapa').show()");
            context.update("frmCuerpoMapa");
            
            String concat1 = "'"+nombre_dep+"', '"+ nombre_prov +"', '"+ nombre_dis+"'";  
            context.execute("irMapa()");
            context.execute("consumirApi(" + concat1 + ")");

        }

    }
    
    public void btnAgregarLocalAutorizacionMapa() {
    
        boolean validacionRegLocal = true;
        
        if (regTipoViasLPSSelected_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de ubicación");
        }
        if (regDireccionLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de ingresar la dirección de oficina");
        } else if (regDireccionLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese dirección de oficina");
        }
        
        if (regReferenciaLPS_Form == null) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese referencia");
        } else if (regReferenciaLPS_Form.equals("")) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Ingrese referencia");
        }
        
        if (regGeoLatitudLPS_Form == null || regGeoLatitudLPS_Form == "" || regGeoLatitudLPS_Form.isEmpty()
                || regGeoLongitudPS_Form == null || regGeoLongitudPS_Form == "" || regGeoLongitudPS_Form.isEmpty()) {
            validacionRegLocal = false;
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la ubicación de coordenadas en el mapa");
        }
        
        if (validacionRegLocal) {
            
            try {

                    LocalAutorizacionDetalle detalleLocalTemp = new LocalAutorizacionDetalle();
                    detalleLocalTemp.setId(JsfUtil.tempIdN());
                    detalleLocalTemp.setTipoUbicacionId(regTipoViasLPSSelected_Form);
                    detalleLocalTemp.setDireccion(regDireccionLPS_Form.toUpperCase());
//                    detalleLocalTemp.setNroFisico(regNroFisicoLPS_Form.toUpperCase());
                    detalleLocalTemp.setDistritoId(distritoIdOficina);
                    detalleLocalTemp.setReferencia(regReferenciaLPS_Form.toUpperCase());
                    detalleLocalTemp.setGeoLatitud(regGeoLatitudLPS_Form);
                    detalleLocalTemp.setGeoLongitud(regGeoLongitudPS_Form);

                    //Agrega a la lista en forma temporal
                    localAutorizacionListado = new ArrayList<LocalAutorizacionDetalle>();
                    localAutorizacionListado.add(detalleLocalTemp);
                    localAutorizacionSelectedString = detalleLocalTemp.getId().toString();

                    mostrarDatosLocalSeleccionado();

                    RequestContext.getCurrentInstance().execute("PF('wvDlgMapa').hide()");
                    RequestContext.getCurrentInstance().update("FormRegSolicCertRMEF:pnl_DatosOficina");

                    RequestContext.getCurrentInstance().execute("updateController = true;");

                } catch (Exception e) {
                    System.out.println("Exception->" + e.getMessage());
                }
        
        }
    
    }
    
    public void mostrarDatosLocalSeleccionado() {
        if (localAutorizacionListado != null) {
            if (true) {
                
               oficinaDataIngresada=true;
                
                for (LocalAutorizacionDetalle localDetalle : localAutorizacionListado) {
                    //System.out.println("(Local Seleccionado->" + localAutorizacionSelectedString + ")");

                    if (true) {
                        //Setea los datos encontrados
                        direccionOficina = (localDetalle.getTipoUbicacionId().getNombre() != null) ? localDetalle.getTipoUbicacionId().getNombre() : null;
                        direccionOficina +=  " " + ((localDetalle.getDireccion() != null) ? localDetalle.getDireccion() : "");
                        direccionOficina = direccionOficina.toUpperCase();
                        direccionOficina = direccionOficina.replaceAll("NULL", "");
                        direccionOficina = direccionOficina.trim();
                        referenciaOficina = (localDetalle.getReferencia() != null) ? localDetalle.getReferencia() : "";
                        lat_x = (!StringUtils.isEmpty(localDetalle.getGeoLatitud())) ? Double.parseDouble(localDetalle.getGeoLatitud()) : 0.0;
                        long_x = (!StringUtils.isEmpty(localDetalle.getGeoLongitud())) ? Double.parseDouble(localDetalle.getGeoLongitud()) : 0.0;
                        //break;
                    }
                }
            } else {
                //limpiar
                //regTipoViasLPSSelected = null;
                direccionOficina = null;
                referenciaOficina = null;
                //renderVerMapa = false;
                lat_x = 0.0;
                long_x = 0.0;
                RequestContext.getCurrentInstance().execute("updateController = false;");
            }
        }
    }
    
    public void limpiarModalFormAccionista(){
        
        regPaisSelected_id          = null;
        accionistaNombres           = null;
        regDatoPersonaByNumDocRL    = null;
    }
    
    public void obtenerNumCaractDocumentoRepresentanteLegal() {
        limpiarModalFormAccionista();
        if (regTipoDocSelected != null) {
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {
                regNroDocBuscar             = null;
                cantNumerosDniCarnetTipoDocRepresentanteLegal = "8";
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Deshabilita para DNI
                regDisabledRepresentanteFechaNacimiento = true;
                renderEsAccionista = false;
                
            } else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                regNroDocBuscar             = null;
                cantNumerosMinimaCarnetTipoDocRepresentanteLegal = 9;
                cantNumerosDniCarnetTipoDocRepresentanteLegal = "12";
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Habilita para Carnet de Extranjeria
                regDisabledRepresentanteFechaNacimiento = false;
                renderEsAccionista = false;
            }  else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")) {
                regNroDocBuscar             = null;
                cantNumerosMinimaCarnetTipoDocRepresentanteLegal = 7;
                cantNumerosDniCarnetTipoDocRepresentanteLegal = "20";
                
                //Para habilitar la Fecha de Nacimiento
                regRepresentanteFechaNacimiento = null;
                //Habilita para Carnet de Extranjeria
                regDisabledRepresentanteFechaNacimiento = true;
                renderEsAccionista = false;
//                renderEsAccionista = true;

            }
        }
    }
    
    public void BuscarPersonaRepresentanteLegal() {
        
        boolean validacionPersonaRL = true;
        renderEsAccionista = false;
        if (regTipoDocSelected == null) {
            validacionPersonaRL = false;
            //JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo documento de la persona");
            return;
        }
        
        if (regNroDocBuscar == null) {
            validacionPersonaRL = false;
            //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el numero de documento de la persona");
            return;
        }
        
        if (regNroDocBuscar != null) {
            if (regNroDocBuscar.equals("")) {
                validacionPersonaRL = false;
                //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el numero de documento de la persona");
                return;
            }
        }
        
        if (regTipoDocSelected != null) {
            //Valida el DNI
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {
                if (regNroDocBuscar.length() != Integer.parseInt(cantNumerosDniCarnetTipoDocRepresentanteLegal)) {
                    validacionPersonaRL = false;
                    //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                }
            }

            //Valida el Carnet de Extranjeria
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                if (regNroDocBuscar.length() < cantNumerosMinimaCarnetTipoDocRepresentanteLegal) {
                    validacionPersonaRL = false;
                    //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar mínimo " + cantNumerosMinimaCarnetTipoDocRepresentanteLegal + " dígitos, en el número de documento para Carnet de Extranjeria");
                }
                if (regNroDocBuscar.length() > Integer.parseInt(cantNumerosDniCarnetTipoDocRepresentanteLegal)) {
                    validacionPersonaRL = false;
                    //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar máximo " + cantNumerosDniCarnetTipoDocRepresentanteLegal + " dígitos, en el número de documento para Carnet de Extranjeria");
                }
            }
            
            //Valida Empresa consorcio
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")) {
                validacionPersonaRL = validarLongitudEC();
            }
        }
        
        if(validacionPersonaRL){
            
            if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC") ){
                regParticipacionistaData = new SbEmpresaExtranjera();
                regParticipacionistaData = ejbSbEmpresaExtranjera.selectEmpresaExtActivaxTipoDocyNumDoc(regTipoDocSelected.getId(), regNroDocBuscar);
                if(regParticipacionistaData == null){  
                    //si NO encuentra un registro de Empresa Extranjera 
                    // por tipo de documento y nro de documento

                    //verifica si existe la persona con el NRO DOC
                    /*regDatoPersonaByNumDocRL = ejbSbPersonaFacade.buscarPersonaSel(regTipoDocSelected.getId().toString(), regNroDocBuscar);
                    if(regDatoPersonaByNumDocRL != null){
                        
                        //SI el tipo de documento de la persona encontrada no coincide con el tipo de documento seleccionado en el modal form
                        if(!regDatoPersonaByNumDocRL.getTipoDoc().getCodProg().equals(regTipoDocSelected.getCodProg())){
                            JsfUtil.mensajeAdvertencia("Se encontró un registro con el nro de documento ingresado, pero no coincide con el tipo de documento seleccionado, verifique nuevamente.");
                            regDatoPersonaByNumDocRL = null;
                            JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
                            renderEsAccionista = false;
                            return;
                        }                    
                    }else{*/
                        //activar los campos para ingresar los datos 
                        renderEsAccionista = true;
                    /*}*/
                    //limpiar
                    limpiarModalFormAccionista();
//                    renderEsAccionista = false;
                }else{
                    // SI encuentra un registro de Empresa Extranjera 
                    
                    //lenar data form EC
//                    regTipoPersonaSelected  = regParticipacionistaData.getTipoDoc();  
                    regPaisSelected         = regParticipacionistaData.getPaisId();
                    regPaisSelected_id      = regPaisSelected.getId().toString();
                    accionistaNombres       = regParticipacionistaData.getDenominacion();
                    renderEsAccionista = false;
                }
            }else{
                
                Consulta_Service serR = new Consulta_Service();
                PideMigraciones_Service serMigra = new PideMigraciones_Service();

                Consulta port = serR.getConsultaPort();
                PideMigraciones portMigra = serMigra.getPideMigracionesPort();

                wspide.Persona pn = null;
                wspide.ResPideMigra pext = null;
                
                try {
                    
                    regDatoPersonaByNumDocRL = ejbSbPersonaFacade.selectPersonaActivaxTipoDocyNumDoc(regTipoDocSelected.getId(), regNroDocBuscar);
                    
                    if (regDatoPersonaByNumDocRL == null) {
                        
                        //verifica si existe la persona con el NRO DOC
                        regDatoPersonaByNumDocRL = ejbSbPersonaFacade.buscarPersonaSel(regTipoDocSelected.getId().toString(), regNroDocBuscar);
                            if (regDatoPersonaByNumDocRL != null) {
                                //si el tipo de documento de la persona encontrada no coincide con el tipo de documento seleccionado en el modal form
                                if (!regDatoPersonaByNumDocRL.getTipoDoc().getCodProg().equals(regTipoDocSelected.getCodProg())) {
                                    JsfUtil.mensajeAdvertencia("Se encontró un registro con el nro de documento ingresado, pero no coincide con el tipo de documento seleccionado, verifique nuevamente.");
                                    regDatoPersonaByNumDocRL = null;
                                    //JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
                                    renderEsAccionista = false;
                                    return;
                                }
                            }

                                if ("TP_DOCID_DNI".equals(regTipoDocSelected.getCodProg())) {

                                pn = port.consultaDNI(regNroDocBuscar);
                                if (pn != null) {
                                    String xApePat = pn.getAPPAT();
                                    String xApeMat = pn.getAPMAT();
                                    String xNombre = pn.getNOMBRES();

                                    if (pn.getFENAC() != null) {
                                        try {
                                            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                            regRepresentanteFechaNacimiento = formatter.parse(pn.getFENAC());
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        regRepresentanteFechaNacimiento = null;
                                    }

                                    regDatoPersonaByNumDocRL = new SbPersonaGt();
                                    regDatoPersonaByNumDocRL.setId(null);
                                    regDatoPersonaByNumDocRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER"));
                                    regDatoPersonaByNumDocRL.setTipoDoc(regTipoDocSelected);
                                    regDatoPersonaByNumDocRL.setNumDoc(regNroDocBuscar);
                                    regDatoPersonaByNumDocRL.setApePat(xApePat);
                                    regDatoPersonaByNumDocRL.setApeMat(xApeMat);
                                    regDatoPersonaByNumDocRL.setNombres(xNombre);
                                    if (regRepresentanteFechaNacimiento != null) {
                                        regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                                    };
                                    regDatoPersonaByNumDocRL.setFechaReg(new Date());
                                    regDatoPersonaByNumDocRL.setActivo(JsfUtil.TRUE);
                                    regDatoPersonaByNumDocRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                    regDatoPersonaByNumDocRL.setAudNumIp(JsfUtil.getIpAddress());
                                    ejbSbPersonaFacade.create(regDatoPersonaByNumDocRL);

                                } else {
                                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeServicioConsultaReniecNulo"));
                                }

                            }else if ("TP_DOCID_CE".equals(regTipoDocSelected.getCodProg())) {
                        pext = portMigra.consultarDocExt(JsfUtil.getLoggedUser().getLogin(), regNroDocBuscar, regTipoDocSelected.getAbreviatura());
                        if (pext != null) {
                            if (pext.isRspta()) {

                                if (pext.getMensaje().contains("0000")) {

                                    regDatoPersonaByNumDocRL = new SbPersonaGt();
                                    regDatoPersonaByNumDocRL.setId(null);
                                    regDatoPersonaByNumDocRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER"));
                                    regDatoPersonaByNumDocRL.setTipoDoc(regTipoDocSelected);
                                    regDatoPersonaByNumDocRL.setNumDoc(regNroDocBuscar);
                                    regDatoPersonaByNumDocRL.setApePat(pext.getResultado().getStrPrimerApellido());
                                    regDatoPersonaByNumDocRL.setApeMat(pext.getResultado().getStrSegundoApellido());
                                    regDatoPersonaByNumDocRL.setNombres(pext.getResultado().getStrNombres());
                                    if (regRepresentanteFechaNacimiento != null) {
                                        regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                                    };
                                    regDatoPersonaByNumDocRL.setFechaReg(new Date());
                                    regDatoPersonaByNumDocRL.setActivo(JsfUtil.TRUE);
                                    regDatoPersonaByNumDocRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                    regDatoPersonaByNumDocRL.setAudNumIp(JsfUtil.getIpAddress());
                                    ejbSbPersonaFacade.create(regDatoPersonaByNumDocRL);

                                } else {
                                    JsfUtil.mensajeAdvertencia("No existen datos con el número de Carnet de Extranjería ingresado o no se encuentra vigente");
                                }

                            } else {
                                String msj = "";
                                if (pext.getMensaje().contains("0007")) {
                                    msj = "La información del documento consultado no puede ser mostrada porque pertenece a un menor de edad.";
                                } else if (pext.getMensaje().contains("0006")) {
                                    msj = "El tipo de documento ingresado no es el correcto.";
                                } else if (pext.getMensaje().contains("0004")) {
                                    msj = "No está permitido el uso de valores nulos o vacíos en la consulta.";
                                } else if (pext.getMensaje().contains("0003")) {
                                    msj = "Transacción no exitosa.";
                                } else if (pext.getMensaje().contains("0002")) {
                                    msj = "Sin conexión.";
                                }
                                JsfUtil.mensajeAdvertencia(msj);
                            }
                        }
                        }else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC") ) {
                        renderEsAccionista = true;
//                            regDisabledRepresentanteFechaNacimiento = false;
                        JsfUtil.mensaje("No existen datos con el número de Documento de Identidad ingresado, ingrese los datos para registrarlos.");
                        }
                    //}--
                    
                    }else {
                    //Cuando lo Encuentra en el base de datos
                    //Cuando lo crea lo inactiva la fecha
                    regRepresentanteFechaNacimiento = null;
                    //Setea la Fecha de Nacimiento de la busqueda
                    if (regDatoPersonaByNumDocRL.getFechaNac() != null) {
                        regRepresentanteFechaNacimiento = regDatoPersonaByNumDocRL.getFechaNac();
                    }                    
                }
                
                }catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.mensajeError(ex, JsfUtil.bundle("ErrorDePersistencia"));
            }
            
            
            }
        
        }
    
    }
    
    public boolean  validarLongitudEC(){
        if (regNroDocBuscar.length() < cantNumerosMinimaCarnetTipoDocRepresentanteLegal) {
//            validacionPersonaRL = false;
            //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar mínimo " + cantNumerosMinimaCarnetTipoDocRepresentanteLegal + " dígitos, en el número de documento para Carnet de Extranjeria");
            return false;
        }
        if (regNroDocBuscar.length() > Integer.parseInt(cantNumerosDniCarnetTipoDocRepresentanteLegal)) {    
//            validacionPersonaRL = false;
            //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar máximo " + cantNumerosDniCarnetTipoDocRepresentanteLegal + " dígitos, en el número de documento para Carnet de Extranjeria");
            return false;
        }
        return true;
    }
    
    public void btnRegistrarPersonaRL() {
    
        boolean validarRegistroPersonaRL = true;

        if (regTipoDocSelected == null) {
            validarRegistroPersonaRL = false;
            //JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            JsfUtil.mensajeAdvertencia("Seleccione un tipo de documento");
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            return;
        }
        
        if (regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")  ) {
            
//            <!--accionistaNumDoc,accionistaApePat,accionistaApeMat,accionistaNombres-->
//            if(regNroDocBuscar == null || regNroDocBuscar == ""){
            if (StringUtils.isEmpty(regNroDocBuscar)) {
                validarRegistroPersonaRL = false;
                //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el nro de documento");
                RequestContext.getCurrentInstance().update("frmCuerpoPersona:txtPersonaNumDoc");
                return;
            }
            
            validarRegistroPersonaRL = validarLongitudEC();
            if(!validarRegistroPersonaRL) return;
            
            if(regPaisSelected_id == null ){
                validarRegistroPersonaRL = false;
                //JsfUtil.invalidar(obtenerForm() + ":cboPaisLista"); 
                JsfUtil.mensajeAdvertencia("Debe de seleccionar un País");
                RequestContext.getCurrentInstance().update("frmCuerpoPersona");
                return;
            }
            
//            if(accionistaNombres == null || accionistaNombres == ""){
            if(StringUtils.isEmpty(accionistaNombres) ){
                validarRegistroPersonaRL = false;
                //JsfUtil.invalidar(obtenerForm() + ":txtRazonSocial"); 
                JsfUtil.mensajeAdvertencia("Debe de ingresar la Razón Social");
                RequestContext.getCurrentInstance().update("frmCuerpoPersona");
                return;
            } 
            
        }
        
        if(regDatoPersonaByNumDocRL == null && !regTipoDocSelected.getCodProg().equals("TP_DOCID_EC")){
                    validarRegistroPersonaRL = false;
            //JsfUtil.invalidar(obtenerForm() + ":cboPersonaTipDoc");
            //JsfUtil.invalidar(obtenerForm() + ":txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar los datos de la persona");
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            return;
        }
        
        //Si es Carnet de Extranjeria valida la Fecha de Nacimiento que tenga
        if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
            if (regRepresentanteFechaNacimiento == null) {
                validarRegistroPersonaRL = false;
                JsfUtil.invalidar("frmCuerpoPersona:txt_Representante_FechaNac");
                JsfUtil.mensajeAdvertencia("Debe de ingresar la Fecha de Nacimiento");
            }

            //Fecha de Nacimiento no debe ser mayor a la de hoy dia
            if ((regRepresentanteFechaNacimiento != null)) {
                if (JsfUtil.getFechaSinHora(regRepresentanteFechaNacimiento).compareTo(JsfUtil.getFechaSinHora(fechaMayorDeEdadCalendario)) > 0) {
                    validarRegistroPersonaRL = false;
                    JsfUtil.invalidar("frmCuerpoPersona:txt_Representante_FechaNac");
                    JsfUtil.mensajeAdvertencia("La fecha de Nacimiento debe ser de una persona mayor de Edad");
                }
            }

            
        }
        
         //Si se ha llamado el Modal por Registro/Busqueda de Representante Legal
        if (buscaNuevoModalRepresentanteLegal) {
            if (validarRegistroPersonaRL) {

                TipoBaseGt sTipoBase = new TipoBaseGt();
                //Si es Sucursal es Responsable Legal
                sTipoBase = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"); //REPRESENTANTE LEGAL

                //Valida si existe en la lista de Representantes Legal o Rsponsables Legal
                boolean encontroPersonaLegal = false;
                //Si tiene datos en la lista busca si ya existe
                if (personaDetalleListado != null) {
                    for (PersonaDetalle personaBusca : personaDetalleListado) {
                        if (personaBusca.getNumDoc().equals(regDatoPersonaByNumDocRL.getNumDoc())) {
                            encontroPersonaLegal = true;
                            break;
                        }
                    }
                }

                if (encontroPersonaLegal) {
                    JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Representante Legal, con este Número de documento: " + regNroDocBuscar);
                    return;
                }

                List<SbPersonaGt> xRLPersona = new ArrayList<SbPersonaGt>();
                xRLPersona = ejbSbPersonaFacade.listarRelacionPersonaXTipoId_idPersOri_idPersDest(sTipoBase.getId(), regAdminist.getId(), regDatoPersonaByNumDocRL.getId());

                if (xRLPersona.size() > 0) {
                    validarRegistroPersonaRL = false;
                    JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Representante Legal, con este Número de documento: " + regNroDocBuscar);
                    return;
                } else {

                    PersonaDetalle detallePersonaTemp = new PersonaDetalle();
                    detallePersonaTemp = new PersonaDetalle();
                    //detallePersonaTemp.setId(JsfUtil.tempIdN());        
                    detallePersonaTemp.setId(regDatoPersonaByNumDocRL.getId());
                    detallePersonaTemp.setNumDoc(regDatoPersonaByNumDocRL.getNumDoc());
                    detallePersonaTemp.setApePat(regDatoPersonaByNumDocRL.getApePat());
                    detallePersonaTemp.setApeMat(regDatoPersonaByNumDocRL.getApeMat());
                    detallePersonaTemp.setNombres(regDatoPersonaByNumDocRL.getNombres());
                    //Agrega a la lista en forma temporal, ya se creo en Persona, no en relacionPersona
                    personaDetalleListado.add(detallePersonaTemp);

                    /*registroSbRelacionPersonaGt.setId(null);
                    registroSbRelacionPersonaGt.setTipoId(sTipoBase);
                    registroSbRelacionPersonaGt.setPersonaOriId(regAdminist);
                    registroSbRelacionPersonaGt.setPersonaDestId(regDatoPersonaByNumDocRL);
                    registroSbRelacionPersonaGt.setFecha(new Date());
                    registroSbRelacionPersonaGt.setObservacion("Se Registro desde Solicitud de Autorizacion");
                    registroSbRelacionPersonaGt.setActivo(JsfUtil.TRUE);
                    registroSbRelacionPersonaGt.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    registroSbRelacionPersonaGt.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSbRelacionPersonaFacadeGt.create(registroSbRelacionPersonaGt);*/
                    //regPersonasRLList = ejbSbPersonaFacade.listarPersonaXIdRelacionPersona(regAdminist.getId());
                    //regPersonasRLSelected = regDatoPersonaByNumDocRL;
                    personaDetalleSelectedString = detallePersonaTemp.getId().toString();
                    regTipoDocSelected = regDatoPersonaByNumDocRL.getTipoDoc();

                    //Actualiza la Fecha de Nacimiento si es Carnet de Extranjeria 
                    if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                        if (regDatoPersonaByNumDocRL != null) {
                            regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                            ejbSbPersonaFacade.edit(regDatoPersonaByNumDocRL);
                        }
                    }

                    RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
                    RequestContext.getCurrentInstance().update("FormRegSolicCertRMEF:pnl_RepreLegal");

                    //mostrarDomicilioRepresentanteLegal();
                }
            }

        }
        
        if (buscaNuevoModalJefeSeg) {
            if (validarRegistroPersonaRL) {
                if (regTipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {

                    if (regDatoPersonaByNumDocRL != null) {
                        regNombreJefeEncontrado = regDatoPersonaByNumDocRL.getTipoDoc().getNombre() + " " + regDatoPersonaByNumDocRL.getNumDoc() + " - " + regDatoPersonaByNumDocRL.getApePat() + " " + regDatoPersonaByNumDocRL.getApeMat() + " " + regDatoPersonaByNumDocRL.getNombres();
                        regTipDocJefeSelected = regDatoPersonaByNumDocRL.getTipoDoc();
                        regNumeroDocJefeSeg = regDatoPersonaByNumDocRL.getNumDoc();
                        regDatosJefeEncontrado = regDatoPersonaByNumDocRL;
                    }

                } //Actualiza la Fecha de Nacimiento si es Carnet de Extranjeria 
                else if (regTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {
                    if (regDatoPersonaByNumDocRL != null) {
                        regDatoPersonaByNumDocRL.setFechaNac(regRepresentanteFechaNacimiento);
                        ejbSbPersonaFacade.edit(regDatoPersonaByNumDocRL);
                    }

                    regNombreJefeEncontrado = regDatoPersonaByNumDocRL.getTipoDoc().getNombre() + " " + regDatoPersonaByNumDocRL.getNumDoc() + " - " + regDatoPersonaByNumDocRL.getApePat() + " " + regDatoPersonaByNumDocRL.getApeMat() + " " + regDatoPersonaByNumDocRL.getNombres();
                    regTipDocJefeSelected = regDatoPersonaByNumDocRL.getTipoDoc();
                    regNumeroDocJefeSeg = regDatoPersonaByNumDocRL.getNumDoc();
                    regDatosJefeEncontrado = regDatoPersonaByNumDocRL;
                    
                }    
                
                RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
                RequestContext.getCurrentInstance().update("FormRegSolicCertRMEF:pnl_JefeSeguridad");
            }
        }
    }
    
    public void regresarRegPersona() {
        //Si se ha llamado el Modal por Registro/Busqueda de Representante Legal
        if (buscaNuevoModalRepresentanteLegal) {
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
            RequestContext.getCurrentInstance().update("FormRegSolicCertRMEF:pnl_RepreLegal");
        };

       

    }
    
    public void openDlgPersonaRepresentanteLegal() {

       //Se utiliza el mismo modal para Representante Legal y Accionista/Socio
        buscaNuevoModalRepresentanteLegal = true;
        buscaNuevoModalJefeSeg = false;
        //Titulo como Representante Legal o Responsable Legal
        tituloBuscaModalRepresentanteLegal = JsfUtil.bundleBDIntegrado("gsspSolicitudAutorizacion_Titulo_Modal_Representante_Legal");
        
        regTipoDocSelected = null;
        regNroDocBuscar = null;
        regDatoPersonaByNumDocRL = null;
        regRepresentanteFechaNacimiento = null;
        regDisabledRepresentanteFechaNacimiento = true;

        //Lista de Tipo de Documento para el Representante Legal
        regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE' ");
        regTipoDocSelected = null;
        renderEsAccionista = false;

        RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').show()");
        RequestContext.getCurrentInstance().update("frmCuerpoPersona");
        RequestContext.getCurrentInstance().update("dlgRepresentanteLegal");
    }
    
    public void openDlgJefeSeguridad() {
        //Setea el Titulo del Modal de Accionista/Socio y la busqueda para retornar los valores
        //Se utiliza el mismo modal para Representante Legal y Accionista/Socio
        buscaNuevoModalRepresentanteLegal = false;
        buscaNuevoModalJefeSeg = true;
        tituloBuscaModalRepresentanteLegal = JsfUtil.bundleBDIntegrado("gsspSolicitudAutorizacion_Titulo_Modal_Accionista_Socio");

        regTipoDocSelected = null;
        regNroDocBuscar = null;
        regDatoPersonaByNumDocRL = null;
        regRepresentanteFechaNacimiento = null;
        regDisabledRepresentanteFechaNacimiento = true;

        //Lista de Tipo de Documento para el Representante Legal
        regTipoDocList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE', 'TP_DOCID_EC' ");    
        regTipoDocSelected = null;
        
        regTipoPersonaSelected  = null; 
        regPaisSelected         = null;
        accionistaNombres       = null;
        regPaisSelected_id      = null;
        
        RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').show()");
        RequestContext.getCurrentInstance().update("frmCuerpoPersona");
        RequestContext.getCurrentInstance().update("dlgRepresentanteLegal");

    }
    
    public void agregarJefeSeguridad() {
    
        boolean validacionJefe = true;
        
        if (regNombreJefeEncontrado == null) {
            validacionJefe = false;
            JsfUtil.mensajeAdvertencia("Debe de buscar el Documento de la persona para agregarse");
            return;
        }
    
    }
    
    public void agregarListaJefe() {
        
        boolean validacionJefe = true;
        
        if (regNombreJefeEncontrado == null) {
            validacionJefe = false;
            JsfUtil.mensajeAdvertencia("Debe de buscar el Documento de la persona para agregarse");
            return;
        }
        
        if (regPrioridadLPSSelected == null) {
            validacionJefe = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_Alm_Prioridad");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la prioridad del jefe de oficina");
            return;
        }
        
        if (cargoJefeSeg == null || cargoJefeSeg.equals("")) {
            validacionJefe = false;
            JsfUtil.mensajeAdvertencia("Debe ingresar el cargo del jefe de oficina");
            return;
        }
        
        boolean encontroNumeroTipo = false;
        boolean principalJefe = false;
        
        if (regNumeroDocJefeSeg != null) {
            
            if (lstJefes != null) {
                for (SspJefeSeguridad jefeBusca : lstJefes) {
                    if(jefeBusca.getPersonaId()!=null){
                        if (jefeBusca.getPersonaId().getNumDoc().equals(regNumeroDocJefeSeg) && jefeBusca.getActivo() == JsfUtil.TRUE) {
                            encontroNumeroTipo = true;
                            break;
                        }
                    }
                    if(jefeBusca.getPrincipal()==1 && regPrioridadLPSSelected.getCodProg().equals("TP_CONTAC_PRIN") && jefeBusca.getActivo()==1 )
                    {
                        principalJefe=true;
                        break;
                    }
                }
            }
        
        
        }
        
        if (principalJefe) {
            validacionJefe = false;
            //JsfUtil.invalidar(obtenerForm() + ":txtAcc_NroDoc");
            JsfUtil.mensajeAdvertencia("Solo se puede agregar a un solo jefe como prioridad PRINCIPAL");
        }
        
        if (encontroNumeroTipo) {
            validacionJefe = false;
            //JsfUtil.invalidar(obtenerForm() + ":txtAcc_NroDoc");
            JsfUtil.mensajeAdvertencia("Ya se ha agregado la persona con el mismo documento en la lista");
        }
        
        if (validacionJefe) {
            try {
                SspJefeSeguridad itemJefe=new SspJefeSeguridad();
                 //contRegJefe++;
                 
                 itemJefe.setId(null);
                 itemJefe.setActivo(JsfUtil.TRUE);
                 itemJefe.setPersonaId(regDatosJefeEncontrado);
                 itemJefe.setCargo(cargoJefeSeg.toUpperCase());
                 
                 if(regPrioridadLPSSelected.getCodProg().equals("TP_CONTAC_PRIN"))
                 {
                     itemJefe.setPrincipal((short)1);
                 }else{ itemJefe.setPrincipal((short)2); }
                 
                 lstJefes.add(itemJefe);
                 
                 regDatosJefeEncontrado = null;
                 regTipDocJefeSelected = null;
                 cargoJefeSeg=null;
                 regPrioridadLPSSelected=null;
                 regNombreJefeEncontrado=null;
                 
                 RequestContext.getCurrentInstance().update("FormRegSolicCertRMEF:pnl_JefeSeguridad");  
                 RequestContext.getCurrentInstance().reset("FormRegSolicCertRMEF:pnl_JefeSeguridad");
            
            }catch(Exception e){
                System.out.println("agregarListaJefe()");
                e.printStackTrace();
            }
        }
    }
    
    public void deleteJefe(SspJefeSeguridad jefe) {
    
        try {
            for (SspJefeSeguridad jefeX : lstJefes) {
                if(jefeX.getId() ==null){
                    if(jefeX.getPersonaId().getNumDoc().equals(jefe.getPersonaId().getNumDoc())){
                        jefeX.setActivo(JsfUtil.FALSE);
                        //lstJefes.remove(jefeX);
                        break;
                    }
                }else{
                    if(jefeX.getPersonaId().getNumDoc().equals(jefe.getPersonaId().getNumDoc())
                            && jefe.getActivo()==JsfUtil.TRUE ){
                        jefeX.setActivo(JsfUtil.FALSE);
                        break;
                    }
                
                }
                
            }
        }catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    
    }
    
    public void deleteContacto(SspContacto contacto) {
    
        try {
            for (SspContacto contact_x : lstContactos) {
               
                //cuando es un EDITAR
                if(contact_x.getId() !=null){
                    if(contact_x.equals(contacto)){ 
                        contact_x.setActivo(JsfUtil.FALSE);
                    }
                }else{
                    //cuando es un CREAR 
                    
                    if(contact_x.getValor().equals(contacto.getValor()) 
                            && contact_x.getTipoMedioId().equals(contacto.getTipoMedioId())
                            && contact_x.getDescripcion().equals(contacto.getDescripcion())  ){
                        contact_x.setActivo(JsfUtil.FALSE);
                    } 
                }
 
            } 
        }catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    
    }
    
    public void agregarTipoContacto() {
        boolean validacionCont = true;

        if (regPrioridadMCSelected == null) {
            validacionCont = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_mc_Prioridad");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la prioridad");
        }

        if (regTipoMedioContactoLPSSelected == null) {
            validacionCont = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el medio de contacto");
        }

        if (regTipoMedioContactoLPSSelected != null) {
            //Valida cuando ha seleccionado el tipo de Correo Electrónico
            if (regTipoMedioContactoLPSSelected.getCodProg().equals("TP_MEDCO_COR")) {
//                if(regtextoCorreoContactoLPS == null){
                if(regtextoContactoLPS == null){
                    validacionCont = false;
//                    JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_CorreoElectronico");
                    JsfUtil.invalidar("FormRegSolicCertRMEF:txtLPE_Alm_DescripMedioContacto");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar el Correo Electrónico");
                }else{
//                    if(regtextoCorreoContactoLPS.equals("")){
                    if(regtextoContactoLPS.equals("")){
                        validacionCont = false;
//                        JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_CorreoElectronico");
                        JsfUtil.invalidar("FormRegSolicCertRMEF:txtLPE_Alm_DescripMedioContacto");
                        JsfUtil.mensajeAdvertencia("Debe de ingresar el Correo Electrónico");
                    }
                }
            }else //Valida cuando no es Tipo de Correo Electrónico
            if (regtextoContactoLPS == null) {
                validacionCont = false;
                JsfUtil.invalidar("FormRegSolicCertRMEF:txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el contacto");
            } else if (regtextoContactoLPS.equals("")) {
                validacionCont = false;
                JsfUtil.invalidar("FormRegSolicCertRMEF:txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el contacto");
            }
        }

        int contPrincCorreo = 0;
        int contPrincTelFijo = 0;
        int contPrincTelMovil = 0;
        int contPrincFax = 0;
        
        Boolean existe_princ = false;
        Boolean existe_sec   = false;
        
        if(validacionCont){
            //regPrioridadLPSSelected           PRIORIDAD           tipobase        valor(String    )
//            regTipoMedioContactoLPSSelected   TIPO MEDIO          tipobase        tipoMedioId(tipomedio)
//            regtextoContactoLPS               NRO o CORREO        string          descripcion(String)
            if (lstContactos.size() > 0) {
                    System.out.println("==========agregarTipoContacto()");
                for (SspContacto contact_x : lstContactos) {
                    if(contact_x.getActivo() == JsfUtil.TRUE) {
                        TipoBaseGt cod_prog_prioridad = ejbTipoBaseFacade.verDatosTipoBaseXId(Long.parseLong(contact_x.getValor()) );

                        System.out.println("=====  CONTACTO ENCONTRADO =====");
                        System.out.println("=====contact_x=====: " + contact_x.getValor()+"["+cod_prog_prioridad.getCodProg()+"]" +" |  " + contact_x.getTipoMedioId().getCodProg() + " |  " + contact_x.getDescripcion());
                        System.out.println("==========");
                        System.out.println("regPrioridadMCSelected.getCodProg(): "+regPrioridadMCSelected.getCodProg());
                        System.out.println("regTipoMedioContactoLPSSelected.getCodProg(): "+regTipoMedioContactoLPSSelected.getCodProg());
                        System.out.println("regtextoContactoLPS: "+regtextoContactoLPS );
                        //si hay un contacto con Prioridad PRINCIPAL[TP_CONTAC_PRIN] y el 
                        // mismo tipo medio
                        if ("TP_CONTAC_PRIN".equals(regPrioridadMCSelected.getCodProg()) 
                                && contact_x.getActivo() == JsfUtil.TRUE && contact_x.getValor().equals("290") ) {
    //                        contPrincCorreo++;
                            System.out.println("=====   TRUE PRINC  =====");
                            JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_Alm_MedioContacto");
                            JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_Alm_MedioContacto");
                            JsfUtil.mensajeAdvertencia("Ya existe un Contacto Principal ");
                            existe_princ = true;
                            regtextoContactoLPS = null;
                            break;

                        }
                        if (cod_prog_prioridad.getCodProg().equals(regPrioridadMCSelected.getCodProg())
                                && contact_x.getTipoMedioId().getCodProg().equals(regTipoMedioContactoLPSSelected.getCodProg()) 
                                && contact_x.getDescripcion().equals(regtextoContactoLPS.toUpperCase().trim())  
                                && contact_x.getActivo() == JsfUtil.TRUE    ) {
    //                        contPrincCorreo++;
                            System.out.println("=====   TRUE SEC  =====");
                            JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_Alm_MedioContacto");
                            JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_Alm_MedioContacto");
                            JsfUtil.mensajeAdvertencia("Ya existe un "+contact_x.getTipoMedioId().getNombre()+" secundario");
                            existe_sec = true;
                            regtextoContactoLPS = null;
                            break;

                        } 
                    } 

                }
                
                if(existe_princ || existe_sec) return;
            }
            
            //agregar contacto nuevo a la lista tempral de contactos
            SspContacto ItemContacto = new SspContacto();
            ItemContacto.setId(null);
            ItemContacto.setRegistroId(null);
            ItemContacto.setValor(regPrioridadMCSelected.getId()+"");
            ItemContacto.setTipoMedioId(regTipoMedioContactoLPSSelected);
            ItemContacto.setDescripcion(regtextoContactoLPS.toUpperCase()); 
            ItemContacto.setActivo(JsfUtil.TRUE); 
            lstContactos.add(ItemContacto);            
             
             
        }

         
        regPrioridadMCSelected = null;
        regTipoMedioContactoLPSSelected = null;
        regtextoContactoLPS = null;
    }
    
    public void handleFileUploadPDF_RES_SBS(FileUploadEvent event) {
        try {
            if (event != null) {
                fileResSBS = event.getFile();
                if (JsfUtil.verificarPDF(fileResSBS)) {
                    resSBSByte = IOUtils.toByteArray(fileResSBS.getInputstream());
                    archivoResSBS = new DefaultStreamedContent(fileResSBS.getInputstream(), "application/pdf");
                    nomArchivoResSBS = fileResSBS.getFileName();
                } else {
                    fileResSBS = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleFileUploadPDF_MR(FileUploadEvent event) {
        try {
            if (event != null) {
                fileResMR = event.getFile();
                if (JsfUtil.verificarPDF(fileResMR)) {
                    MRByte = IOUtils.toByteArray(fileResMR.getInputstream());
                    archivoMR = new DefaultStreamedContent(fileResMR.getInputstream(), "application/pdf");
                    nomArchivoMR = fileResMR.getFileName();
                } else {
                    fileResMR = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String obtenerMsjeInvalidSizeFile() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selLicenciaMunicipalPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }
    
    public String guardarDatosSolicitud() {

        if (registro.getId() == null) {
            return crearSolicitud();
        } else {
            return guardarEditarSolicitud();
        }
    }
    
    public String crearSolicitud() {
        
        try {
            if(validaRegistroSolCertificacion()){
                
                registro.setId(null);
                guadarSspRegistro();
                representanteRegistro.setId(null);
                guardarRepresentante();
                guardarContacto();
                agencia.setId(null);
                guardarAgencia();
                guardarJefeSeguridad();
                
                /* FIN GUARDA DATOS DE DISCA DE LA RESOLUCION POR ADECUACION 1213 */
                JsfUtil.mensaje("Se registró la solicitud con Código: " + registro.getId());

                return prepareList();
            
            }
        }catch(Exception e){
            
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        
        return null;
    }
    
    public String guardarEditarSolicitud() {
        
        try {
            if(validaRegistroSolCertificacion()){
                
                guadarSspRegistro();
                guardarRepresentante();
                guardarContacto();
                guardarAgencia();
                guardarJefeSeguridad();
                
                /* FIN GUARDA DATOS DE DISCA DE LA RESOLUCION POR ADECUACION 1213 */
            JsfUtil.mensaje("Se actualizó la solicitud con Código: " + registro.getId() + " y con Nro. de Solicitud " + registro.getNroSolicitiud());
                
                prepareList();
                return prepareList();
            
            }
        }catch(Exception e){
            
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        
        return "";
    }
    
    public boolean validaRegistroSolCertificacion() {
        
        boolean validacion = true;
        
         //============================================
        //Validar Panel - Datos de la oficina
        //============================================
        if (distritoIdOficina == null) {
            validacion = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:bcoLPE_AmbitoOperaDIstrito");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la ubicación de la oficina");
            return false;
        }
        
        if (codigoOficina == null || codigoOficina.equals("")) {
            validacion = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:txtCodOfi");
            JsfUtil.mensajeAdvertencia("Debe ingresar el código de oficina");
            return false;
        }
        
        if (nombreOficina == null || nombreOficina.equals("")) {
            validacion = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:txtNomComOfi");
            JsfUtil.mensajeAdvertencia("Debe ingresar el nombre comercial de oficina");
            return false;
        }
        
        if (tipoLocalOficina == null) {
            validacion = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_TipLocal");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo de oficina");
            return false;
        }
        
        if(!oficinaDataIngresada)
        {
            validacion = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:txtDireOfi");
            JsfUtil.mensajeAdvertencia("Debe ingresar los datos de ubicacón de la oficina");
            return false;
        }
        
        if (personaDetalleSelectedString == null) {
            validacion = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:cboRepresLegal");
            JsfUtil.mensajeAdvertencia("Debe ingresar el representante legal");
            return false;
        }
        
        if (lstJefes.size() == 0) {
                validacion = false;
                JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_Alm_Prioridad");
                JsfUtil.invalidar("FormRegSolicCertRMEF:txtCargo");
                JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un jefe de seguridad");
                return false;
        }else{
                if(lstJefes.size() > 0){
                    short contador_activos = 0;
                    for (SspJefeSeguridad sociox : lstJefes) {
                        //si hay al menos un socio con ESTADO activo
                        if(sociox.getActivo()== JsfUtil.TRUE){
                            contador_activos++;
                        }
                    }
            
                    if(contador_activos <= 0){  
                        validacion = false;
                        JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_Alm_Prioridad");
                        JsfUtil.invalidar("FormRegSolicCertRMEF:txtCargo");
                        JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un jefe de seguridad");
                        return false;
                    }
                }
            }
        
        if(lstContactos.size() == 0){
                validacion = false;
                JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_mc_Prioridad");
                JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un registro en Medios de Contacto");
                return false;
        }else{
                if(lstContactos.size() > 0){
                    short contador_activos = 0;
                    for (SspContacto contacto_x : lstContactos) {
                        //si hay al menos un contacto con ESTADO activo
                        if(contacto_x.getActivo()== JsfUtil.TRUE){
                            contador_activos++;
                        }
                    }

                    if(contador_activos <= 0){  
                        validacion = false;
                        JsfUtil.invalidar("FormRegSolicCertRMEF:cboLPE_mc_Prioridad");
                        JsfUtil.mensajeAdvertencia("Debe de agregar por lo menos un registro en Medios de Contacto");
                        return false;
                    }
                }
        }
        
        if (nroResSBS == null || nroResSBS.equals("")) {
            validacion = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:txtNroResSBS");
            JsfUtil.mensajeAdvertencia("Debe ingresar el nro de resolución de la SBS");
            return false;
        }
        
        if (fechaResSBS == null) {
            validacion = false;
            JsfUtil.invalidar("FormRegSolicCertRMEF:txt_Fecha_SBS");
            JsfUtil.mensajeAdvertencia("Debe ingresar la fecha de la resolución SBS");
            return false;
        }
        
        if (nomArchivoResSBS == null && nomArchivoResSBSAnterior == null) {
                validacion = false;
                JsfUtil.invalidar("FormRegSolicCertRMEF:txf_ArchivoLM");
                JsfUtil.mensajeAdvertencia("Debe de adjuntar el archivo PDF de la resolución SBS");
        }
        
        if (nomArchivoMR == null && nomArchivoMRAnterior == null) {
                validacion = false;
                JsfUtil.invalidar("FormRegSolicCertRMEF:txf_Archivo_MR");
                JsfUtil.mensajeAdvertencia("Debe de adjuntar el archivo PDF de la matriz de riesgo");
        }
             
        return validacion;
    }
    
    public void guadarSspRegistro(){
    
         try {
                registro.setTipoProId(regTipoProcesoId);
                registro.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_ASS"));
                registro.setEmpresaId(regAdminist);
                registro.setComprobanteId(null);
                regPersonasRLSelected = ejbSbPersonaFacade.find(Long.parseLong(personaDetalleSelectedString));
                registro.setRepresentanteId(regPersonasRLSelected);
                registro.setRegistroId(null);
                registro.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registro.setFecha(new Date());
                //registro.setNroExpediente(null);
                registro.setTipoRegId(regTipoRegistroSelected);
                registro.setTipoOpeId(regTipoOperacionSelected);
                registro.setActivo(JsfUtil.TRUE);
                registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.setAudNumIp(JsfUtil.getIpAddress());
                registro.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                registro = (SspRegistro) JsfUtil.entidadMayusculas(registro, "");
                
                if (registro.getId() == null) {
                    registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                    registro.setNroSolicitiud("S/N");
                    ejbSspRegistroFacade.create(registro);
                } else {
                    ejbSspRegistroFacade.edit(registro);
                }
                
         }catch(Exception e){
            
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
    public void guardarRepresentante(){
        
        try {
                    
                    representanteRegistro.setRegistroId(registro);
                    representanteRegistro.setRepresentanteId(regPersonasRLSelected);
                    representanteRegistro.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));
                    representanteRegistro.setFecha(new Date());
                    representanteRegistro.setActivo(JsfUtil.TRUE);
                    representanteRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    representanteRegistro.setAudNumIp(JsfUtil.getIpAddress());
                    representanteRegistro = (SspRepresentanteRegistro) JsfUtil.entidadMayusculas(representanteRegistro, "");
                    
                    if(representanteRegistro.getId()==null){
                        ejbSspRepresentanteRegistroFacade.create(representanteRegistro);
                    }else{
                        ejbSspRepresentanteRegistroFacade.edit(representanteRegistro);
                    }
                    
        }catch(Exception e){
            
            System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    
    }
    
    public void guardarContacto(){
        
        try {
                if (lstContactos.size() > 0) {
                                for (SspContacto itemContacto : lstContactos) {
                                    if (itemContacto.getActivo() == JsfUtil.TRUE && itemContacto.getId()==null) {
                                        itemContacto.setId(null);
                                        itemContacto.setRegistroId(registro);
                                        itemContacto.setValor(itemContacto.getValor());
                                        itemContacto.setTipoMedioId(itemContacto.getTipoMedioId());
                                        itemContacto.setDescripcion(itemContacto.getDescripcion());
                                        itemContacto.setFecha(new Date());
                                        itemContacto.setActivo(JsfUtil.TRUE);
                                        itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                        itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                                        itemContacto = (SspContacto) JsfUtil.entidadMayusculas(itemContacto, "");
                                        ejbSspContactoFacade.create(itemContacto);
                                    }
                                    if(itemContacto.getActivo() == JsfUtil.FALSE && itemContacto.getId()!=null){
                                        ejbSspContactoFacade.edit(itemContacto);
                                    }
                                }
                } else {
                        throw new NullPointerException("Lista de Contactos vacía");
              }
    
        }catch(Exception e){
             System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
             e.printStackTrace();
             JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        
    }
    
    public void guardarAgencia(){
    
        try{
            
            String fileNameArchivo_SBS = "";
            
             if(nomArchivoResSBS!=null){
                fileNameArchivo_SBS = nomArchivoResSBS;
                fileNameArchivo_SBS = "RES_SBS_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_CES").toString() + fileNameArchivo_SBS.substring(fileNameArchivo_SBS.lastIndexOf('.'), fileNameArchivo_SBS.length());
                fileNameArchivo_SBS = fileNameArchivo_SBS.toUpperCase();
             }else{
                 fileNameArchivo_SBS=nomArchivoResSBSAnterior;
                 fileNameArchivo_SBS=fileNameArchivo_SBS.toUpperCase();
             }
             
             
             String fileNameArchivo_MR = "";
             
             if(nomArchivoMR!=null){
                fileNameArchivo_MR = nomArchivoMR;
                fileNameArchivo_MR = "MR_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_REC").toString() + fileNameArchivo_MR.substring(fileNameArchivo_MR.lastIndexOf('.'), fileNameArchivo_MR.length());
                fileNameArchivo_MR = fileNameArchivo_MR.toUpperCase();
             }else{
                 fileNameArchivo_MR=nomArchivoMRAnterior;
                 fileNameArchivo_MR=fileNameArchivo_MR.toUpperCase();
             }
                        
            agencia.setNonbreOficina(nombreOficina);
            agencia.setCodigoOficina(codigoOficina);
            agencia.setDireccion(direccionOficina);
            agencia.setDistritoId(distritoIdOficina);
            agencia.setReferencia(referenciaOficina);
            agencia.setRegistroId(registro);
            agencia.setLatitud(lat_x+"");
            agencia.setLongitud(long_x+"");
            agencia.setTipoLocalId(tipoLocalOficina);
            agencia.setNumeroRsSbs(nroResSBS);
            agencia.setFechaRs(fechaResSBS);
            agencia.setArchivoRs(fileNameArchivo_SBS);
            agencia.setArchivoMatriz(fileNameArchivo_MR);
            agencia.setAudLogin(JsfUtil.getIpAddress());
            agencia.setAudNumIp(JsfUtil.getIpAddress());
            agencia.setActivo(JsfUtil.TRUE);
            agencia.setPriorizacion((short) checkTipoRiesgo);
            agencia = (SspAgenciaFinanciera) JsfUtil.entidadMayusculas(agencia, "");

            if(agencia.getId()==null){

                ejbSspAgenciaFacade.create(agencia);
            }else{
                ejbSspAgenciaFacade.edit(agencia);
            }
            
            
            if(resSBSByte!=null){
                FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_SBS").getValor() + agencia.getArchivoRs()), resSBSByte);
            }
            
            if(MRByte!=null){
                FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_MR").getValor() + agencia.getArchivoMatriz()), MRByte);
            }
            
        }catch(Exception e){
             System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
             e.printStackTrace();
             JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        
    }
    
    public void guardarJefeSeguridad(){
        
        try {
                if (lstJefes.size() > 0) {
                                for (SspJefeSeguridad itemContacto : lstJefes) {
                                    if (itemContacto.getActivo() == JsfUtil.TRUE && itemContacto.getId()==null) {
                                        itemContacto.setId(null);
                                        itemContacto.setRegistroId(registro);
                                        itemContacto.setActivo(JsfUtil.TRUE);
                                        itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                        itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                                        itemContacto.setPrincipal(itemContacto.getPrincipal());
                                        itemContacto.setPersonaId(itemContacto.getPersonaId());
                                        itemContacto.setCargo(itemContacto.getCargo());
                                        itemContacto = (SspJefeSeguridad) JsfUtil.entidadMayusculas(itemContacto, "");
                                        ejbSspJefeSeguridadFacade.create(itemContacto);
                                    }
                                    if (itemContacto.getActivo() == JsfUtil.TRUE && itemContacto.getId()==null) {
                                        ejbSspJefeSeguridadFacade.edit(itemContacto);
                                    }
                                }
                } else {
                        throw new NullPointerException("Lista de Contactos vacía");
              }
    
        }catch(Exception e){
             System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
             e.printStackTrace();
             JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    
    }
    
    public String prepareList() {
        
        filtroBuscarPor = null;
        filtroNumero = null;
        filtroTipoEstadoListado = ejbTipoSeguridadFacade.listarTipoSeguridadXCodProgs("'TP_ECC_CRE', 'TP_ECC_TRA', 'TP_ECC_OBS', 'TP_ECC_NPR', 'TP_ECC_CAN', 'TP_ECC_APR', 'TP_ECC_DES', 'TP_ECC_ANU' ");
        resultados=null;
        filtroFechaIniSelected = null;
        filtroFechaFinSelected = null;
        return "/aplicacion/gssp/gsspCertificacion/gssp_financiera/List";
    
    }
    
    public void buscarBandejaSolicitud() {
        boolean validacion = true;

        if (filtroBuscarPor != null) {

            if (filtroBuscarPor.equals("1") || filtroBuscarPor.equals("2") || filtroBuscarPor.equals("3")) {
                if (filtroNumero == null) {
                    validacion = false;
                    JsfUtil.invalidar("listFormCert:buscarPor");
                    JsfUtil.mensajeAdvertencia("Es necesario ingresar el número del documento.");
                } else if (filtroNumero.equals("")) {
                    validacion = false;
                    JsfUtil.invalidar("listFormCert:buscarPor");
                    JsfUtil.mensajeAdvertencia("Es necesario ingresar el número del documento");
                }
            }

        }

        if ((filtroFechaIniSelected != null && filtroFechaFinSelected == null) || (filtroFechaIniSelected == null && filtroFechaFinSelected != null)) {
            validacion = false;
            JsfUtil.invalidar("listFormCert:txfFechaInicio");
            JsfUtil.invalidar("listFormCert:txfFechaFinal");
            JsfUtil.mensajeAdvertencia("Es necesario seleccionar un rango de fecha");
        }

        if (validacion) {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt administ = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

            HashMap mMap = new HashMap();
            mMap.put("buscarPor", filtroBuscarPor);
            mMap.put("filtroNumero", filtroNumero);
            mMap.put("filtroFechaIni", filtroFechaIniSelected);
            mMap.put("filtroFechaFin", filtroFechaFinSelected);
            mMap.put("filtroTipoEstadoId", (filtroTipoEstadoSelected != null) ? filtroTipoEstadoSelected.getId() : null);
            mMap.put("filtroAdministradoId", (administ != null) ? administ.getId() : null);
            mMap.put("filtroAdministradoNumDoc", (administ != null) ? administ.getRuc() : null);

            resultados = ejbSspRegistroFacade.buscarBandejaSolicitudCertificacionEF(mMap);
        }
    }
    
    public boolean renderBtnEditar(Map item) {
        boolean validar = false;

        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE") || item.get("ESTADO_CODPROG").equals("TP_ECC_OBS")) {
                validar = true;
            }
        }

        return validar;
    }
    
    public void mostrarEditarSolicitud(Map item) {
        
            System.out.println("tipoRegistroCodProg->" + tipoRegistroCodProg);
            regTipLocalLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_OFI_FINAN");
            regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
            
            iniciarValores_CrearSolicitud();
            estado = EstadoCrud.EDITARVIGILANCIAPRIVADA;
            registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
            tipoRegistroCodProg = registro.getTipoRegId().getCodProg();
            habilitarVerResMR=true;
            habilitarVerResSBS=true;
            cargarDatosEditarSolicitud(registro);
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspCertificacion/gssp_financiera/Create.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    
    }
    
    public void cargarDatosEditarSolicitud(SspRegistro item) {
    
        registro = null;
        if (item != null) {
        
            registro = item;
            
            lstObservaciones = null;
            //Busca las Observaciones
            if (lstObservaciones == null) {
                lstObservaciones = new ArrayList();
            }
            
            for (SspRegistroEvento ce : registro.getSspRegistroEventoList()) {
                if (ce.getActivo() == JsfUtil.TRUE && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")) {
                    lstObservaciones.add(ce);
                }
            }
            //Fin de Busca las Observaciones

            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            regAdminist = registro.getEmpresaId();
            regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());
            agencia = new SspAgenciaFinanciera();
            SspAgenciaFinanciera agf= new SspAgenciaFinanciera();
            agf=registro.getSspAgenciaList().get(0);
            agencia=agf;
            
            distritoIdOficina=agf.getDistritoId();
            direccionOficina=agf.getDireccion();
            referenciaOficina=agf.getReferencia();
            lat_x=Double.parseDouble(agf.getLatitud());
            long_x=Double.parseDouble(agf.getLongitud());
            codigoOficina=agf.getCodigoOficina();
            nombreOficina=agf.getNonbreOficina();
            tipoLocalOficina=agf.getTipoLocalId();
            nroResSBS=agf.getNumeroRsSbs();
            fechaResSBS=agf.getFechaRs();
            nomArchivoMRAnterior=agf.getArchivoMatriz();
            nomArchivoResSBSAnterior=agf.getArchivoRs();
            checkTipoRiesgo=agf.getPriorizacion();
            oficinaDataIngresada=true;
            
            PersonaDetalle detallePersonaTemp = new PersonaDetalle();
                    detallePersonaTemp = new PersonaDetalle();
                    detallePersonaTemp.setId(registro.getRepresentanteId().getId());
                    detallePersonaTemp.setNumDoc(registro.getRepresentanteId().getNumDoc());
                    detallePersonaTemp.setApePat(registro.getRepresentanteId().getApePat());
                    detallePersonaTemp.setApeMat(registro.getRepresentanteId().getApeMat());
                    detallePersonaTemp.setNombres(registro.getRepresentanteId().getNombres());
                    //Agrega a la lista en forma temporal, ya se creo en Persona, no en relacionPersona
                    personaDetalleListado.add(detallePersonaTemp);
                    
            personaDetalleSelectedString = registro.getRepresentanteId().getId().toString();
            
            lstJefes = new ArrayList<SspJefeSeguridad>();
            for (SspJefeSeguridad jefeActivo : registro.getSspJefeSeguridadList()) {
                if (jefeActivo.getActivo() == 1) {
                    lstJefes.add(jefeActivo);
                }
            }
            
            lstContactos = new ArrayList<SspContacto>();
            for (SspContacto contactoActivo : registro.getSspContactoList()) {
                if (contactoActivo.getActivo() == 1) {
                    lstContactos.add(contactoActivo);
                }
            }
            
        }
    }
    
    public StreamedContent obtenerArchivoResSBS() {
        //SspArchivo arch = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_CF");
        SspAgenciaFinanciera arch = registro.getSspAgenciaList().get(0);
        if (arch == null) {
            return null;
        }
        StreamedContent r = null;
        try {
            String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_SBS").getValor();
            FileInputStream f = new FileInputStream(path + arch.getArchivoRs());
            r = new DefaultStreamedContent(f, "application/pdf", arch.getArchivoRs());
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    public StreamedContent obtenerArchivoMR() {
        //SspArchivo arch = ejbSspRequisitoFacade.buscarRequisitoArchivoPorRegistroAutoriza(registro.getId(), "TP_REQ_CF");
        SspAgenciaFinanciera arch = registro.getSspAgenciaList().get(0);
        if (arch == null) {
            return null;
        }
        StreamedContent r = null;
        try {
            String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_MR").getValor();
            FileInputStream f = new FileInputStream(path + arch.getArchivoMatriz());
            r = new DefaultStreamedContent(f, "application/pdf", arch.getArchivoMatriz());
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    public void adicionaEvento(SspRegistro reg, String observacion) {
        SspRegistroEvento evento = new SspRegistroEvento();
        evento.setId(null);
        evento.setActivo(JsfUtil.TRUE);
        evento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        evento.setAudNumIp(JsfUtil.getIpAddress());
        evento.setRegistroId(reg);
        evento.setFecha(new Date());
        evento.setObservacion(observacion);
        evento.setTipoEventoId(reg.getEstadoId());
        evento.setUserId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
        evento = (SspRegistroEvento) JsfUtil.entidadMayusculas(evento, "");
        if (reg.getSspRegistroEventoList() == null) {
            reg.setSspRegistroEventoList(new ArrayList());
        }
        reg.getSspRegistroEventoList().add(evento);
    }
    
    public boolean renderBtnTransmitir(Map item) {
        boolean validar = false;

        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE") || item.get("ESTADO_CODPROG").equals("TP_ECC_OBS")) {
                validar = true;
            }
        }

        return validar;
    }
    
    public void openDlgcompletarProceso(Map item) {
        resultadosSeleccionados = new ArrayList();
        resultadosSeleccionados.add(item);
        //Busca el Registro para conocer el tipo de modalidad al procesar
        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));

        System.out.println("registro->" + registro);
        System.out.println("getSspTipoUsoLocalList->" + registro.getSspTipoUsoLocalList().size());

        //localPrincipalTransmitir = false;

        

        if (!resultadosSeleccionados.isEmpty()) {
            /*progress = 0;
            actualProcesado = 0;
            totalProcesados = 0;*/
            blnProcesoTransmision = false;
            RequestContext.getCurrentInstance().execute("PF('wvDialogCompletarProceso').show()");
            RequestContext.getCurrentInstance().update("frmCompletarProceso");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar al menos un registro para transmitir");
        }
    }
    
    public boolean validaTransmitir() {
        if (registro.getEstadoId().getCodProg().equals("TP_ECC_TRA")) {//TRANSMITIDO
            JsfUtil.mensajeAdvertencia("El registro con ID " + registro.getId() + " ya ha sido transmitido");
            return false;
        }
        
        return true;
    }
    
    public void transmitirSolicitud() {
        if (!resultadosSeleccionados.isEmpty()) {

            int cont = 0;

            blnProcesoTransmision = true;
            totalProcesados = resultadosSeleccionados.size();

            for (Map item : resultadosSeleccionados) {
                actualProcesado++;
                registro = ejbSspRegistroFacade.buscarRegistroById((Long) item.get("ID"));

                String estadoAnterior = registro.getEstadoId().getCodProg();

                if (!validaTransmitir()) {
                    continue;
                }

                if (!guardarDatosTransmiteGeneraSolicitudAutoServSeg(registro)) {
                    continue;
                }

                

                cont++;
                JsfUtil.mensaje("Se transmitió el registro Id: " + registro.getId() + ", con Nro Solicitud: " + registro.getNroSolicitiud());
                registro = null;
            }
            reiniciarCamposBusqueda();
            resultados = new ArrayList();
            blnProcesoTransmision = true;

            RequestContext.getCurrentInstance().execute("PF('wvDialogCompletarProceso').hide()");
            RequestContext.getCurrentInstance().update("listFormCert");
            if (cont > 0) {
                JsfUtil.mensaje("Se procesó " + cont + " registro(s) correctamente.");
            } else {
                JsfUtil.mensajeAdvertencia("No se procesó ningún registro.");
            }

        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar al menos un registro para transmitir");
        }
    }
    
    public void reiniciarCamposBusqueda() {
        filtroBuscarPor = null;
        filtroNumero = null;
        filtroFechaIniSelected = null;
        filtroFechaFinSelected = null;
        filtroTipoEstadoSelected = null;
        //setDisabledBtnTransmitir(false);
    }
    
    public boolean guardarDatosTransmiteGeneraSolicitudAutoServSeg(SspRegistro sspRegistro) {
        try {
            //Solo cuando esta en estado CREADO u OBSERVADO

            String estadoRegistro = sspRegistro.getEstadoId().getCodProg();
            if (estadoRegistro.equals("TP_ECC_CRE") || estadoRegistro.equals("TP_ECC_OBS")) {

                if (sspRegistro.getNroSolicitiud().equals("S/N") || sspRegistro.getNroSolicitiud() == null) {//Si no tiene numero de Solicitud le genera una
                    sspRegistro.setNroSolicitiud(obtenerNroSolicitudEmision());
                }

                if (estadoRegistro.equals("TP_ECC_OBS") && sspRegistro.getNroExpediente() != null) {
                    sspRegistro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_PRE")); //Actualiza a PRESENTADO A SIST. GGSP
                    sspRegistro.setObservacion(null);
                } else {
                    sspRegistro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_TRA")); //Actualiza a TRANSMITIDO A SIST. INTEGRADO
                    sspRegistro.setObservacion(null);
                }

                //Asignar a personal de TD para que lo atienda
                String strUsuarios = "";
                if (ejbSbParametroFacade.obtenerParametroXNombre("TransmiteAutorizaGSSP_usuTD_TP_AREA_TRAM") != null) {
                    strUsuarios = ejbSbParametroFacade.obtenerParametroXNombre("TransmiteAutorizaGSSP_usuTD_TP_AREA_TRAM").getValor();
                    sspRegistro.setUsuarioRecepcionId(ejbSbUsuarioFacadeGt.find(ejbSspRegistroFacade.obtenerIdUsuarioTDparaTransmitirAutorizacionSeguridad(strUsuarios)));
                } else {
                    JsfUtil.mensajeAdvertencia("No ha sido configurado la persona de Tramite Documentario que recepciona la Solicitud");
                }

                if (sspRegistro.getSspRegistroEventoList() == null) {
                    sspRegistro.setSspRegistroEventoList(new ArrayList());
                }
                adicionaEvento(sspRegistro, "");
                ejbSspRegistroFacade.edit(sspRegistro);
            }
        } catch (EJBException e) {
            System.err.println("Exception ID:" + registro.getId());
            Exception cause = e.getCausedByException();
            if (cause instanceof ConstraintViolationException) {
                @SuppressWarnings("ThrowableResultIgnored")
                ConstraintViolationException cve = (ConstraintViolationException) e.getCausedByException();
                for (ConstraintViolation<? extends Object> v : cve.getConstraintViolations()) {
                    System.err.println(v);
                    System.err.println("==>>" + v.getMessage());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public String obtenerNroSolicitudEmision() {
        String nroSolicitud = "";
        nroSolicitud = ejbSspRegistroFacade.obtenerNroSolicitudEmisionRegistro() + "-" + JsfUtil.mostrarAnio(new Date()) + "-SUCAMEC/GSSP-EC";
        return nroSolicitud;
    }
    
    public boolean renderBtnVer() {
        return true;
    }
    
    public String mostrarVerSolicitud(Map item) {
    
        String URL_Formulario = "";
        
         //=============================================
        //========    Administrado - inicio   =========
        //=============================================
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());

        estado = EstadoCrud.CREARCERTVEHBLIND;
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();
        regTipLocalLPSList = ejbTipoSeguridadFacade.lstTipoSeguridad("TP_OFI_FINAN");
        regDistritoLPSList = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");

        iniciarValores_CrearSolicitud();
        //Busca el Registro
        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        habilitarVerResMR=true;
        habilitarVerResSBS=true;
        cargarDatosEditarSolicitud(registro);
        URL_Formulario = "/sel/faces/aplicacion/gssp/gsspCertificacion/gssp_financiera/View.xhtml";
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        
        try {
                ec.redirect("/sel/faces/aplicacion/gssp/gsspCertificacion/gssp_financiera/View.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
        return URL_Formulario;
    }
    
    public void openDlgConfirmarDesestimiento(Map item) {
        if (item == null) {
            JsfUtil.mensajeError("No se puede generar el desestimiento");
            return;
        }

        registro = ejbSspRegistroFacade.find((Long) item.get("ID"));
        observacion = "";
        RequestContext.getCurrentInstance().execute("PF('wvConfirmarDesestimiento').show()");
        RequestContext.getCurrentInstance().update("confirmarDesestimientoForm");
    }
    
    public void confirmarDesestimiento() {
        try {
            if (observacion == null || observacion.trim().isEmpty()) {
                JsfUtil.mensajeAdvertencia("Debe de registrar el motivo del desistimiento");
                return;
            }

            observacion = observacion.toUpperCase();
            TipoSeguridad tipo = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_DES");

            if (generaTraza(tipo)) {
                registro.setEstadoId(tipo);
                adicionaEvento(registro, observacion);
                ejbSspRegistroFacade.edit(registro);

                registro = null;
                reiniciarCamposBusqueda();
                resultados = new ArrayList();

                JsfUtil.mensaje("Registro actualizado correctamente.");
                RequestContext.getCurrentInstance().execute("PF('wvConfirmarDesestimiento').hide()");
                RequestContext.getCurrentInstance().update("listFormCert");
            } else {
                JsfUtil.mensajeError("Hubo un error al archivar el expediente");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
    public boolean generaTraza(TipoSeguridad nuevoEstadoCurso) {
        boolean validacion = true;
        String loginUsuarioAct = "", asunto = "";

        try {
            /////////////////////////////////////////////////////////////////////
            if (nuevoEstadoCurso.getCodProg().equals("TP_ECC_DES") && registro.getNroExpediente() != null) {
                loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(registro.getNroExpediente());
                asunto = nuevoEstadoCurso.getNombre() + " A SOLICITUD DEL ADMINISTRADO. " + observacion;
                //ARCHIVAMIENTO DEL EXPEDIENTE                
                boolean estadoNE = wsTramDocController.archivarExpediente(registro.getNroExpediente(), loginUsuarioAct, asunto);
                if (!estadoNE) {
                    JsfUtil.mensajeError("Ocurrió un error al archivar el expediente " + registro.getNroExpediente());
                }
            } else {
                validacion = true;
            }
            /////////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            e.printStackTrace();
            validacion = false;
            JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro Código: " + registro.getId());
        }

        return validacion;
    }
    
    public boolean renderBtnFinProcedimiento(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_TRA") || item.get("ESTADO_CODPROG").equals("TP_ECC_OBS") || item.get("ESTADO_CODPROG").equals("TP_ECC_PRE")) {
                validar = true;
            }
        }
        return validar;
    }
    
    public void borrarRegistro(Map item) {
        
        try {
            
                SspRegistro reg = ejbSspRegistroFacade.find((Long) item.get("ID"));
                reg.setActivo(JsfUtil.FALSE);
                ejbSspRegistroFacade.edit(reg);
                
                resultados.remove(item);
                resultadosSeleccionados.remove(item);

                JsfUtil.mensaje("Se ha borrado correctamente el registro.");
        
        }catch(Exception e) {
            
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
        
        }
    
    }
    
    public boolean renderBtnBorrar(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE")) {
                validar = true;
            }
        }
        return validar;
    }
}
