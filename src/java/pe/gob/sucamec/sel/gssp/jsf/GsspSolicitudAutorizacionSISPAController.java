/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbAsientoPersona;
import pe.gob.sucamec.bdintegrado.data.SbAsientoSunarp;
import pe.gob.sucamec.bdintegrado.data.SbPartidaSunarp;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspContacto;
import pe.gob.sucamec.bdintegrado.data.SspForma;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SspRegistroEvento;
import pe.gob.sucamec.bdintegrado.data.SspRepresentantePublico;
import pe.gob.sucamec.bdintegrado.data.SspRepresentanteRegistro;
import pe.gob.sucamec.bdintegrado.data.SspServicio;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.UploadFilesController;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SspArchivo;
import pe.gob.sucamec.bdintegrado.data.SspRequisito;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.PideMigraciones;
import wspide.PideMigraciones_Service;

/**
 *
 * @author locador772.ogtic
 */
@Named(value = "gsspSolicitudAutorizacionSISPAController")
@SessionScoped
public class GsspSolicitudAutorizacionSISPAController implements Serializable {

    @Inject
    WsTramDoc wsTramDocController;
    
    @Inject
    UploadFilesController uploadFilesController;
    
    @Inject
    GsspSolicitudAutorizacionController gsspSolicitudAutorizacionController;
    
    //Observaciones 
    private List<SspRegistroEvento> lstObservaciones;
    
    private EstadoCrud estado;
    private Expediente expediente;
    private String xNroSolicitiud;
    
    private SspRegistro registro;
    private SspRegistro registroN2;
    private SspRepresentanteRegistro representanteRegistro;
    
    private SbPartidaSunarp partidaSunarpRL;
    private SbAsientoSunarp asientoSunarpRL;
    private SbAsientoPersona asientoPersonaRL;
    private SbRelacionPersonaGt relacionPersonaGtRL;
    private SspRepresentantePublico representantePublico;
    
    private SspRegistroEvento registroEvento;
    private SspServicio servicio;
    private SspForma forma;
    
    //============= CREAR SISPA =====================    
    
    private String tipoAdministradoUsuario;  
    private boolean administradoConRUC;
    
    private String[] selectedOptions;
    private boolean termCondicionAceptaDDJJ;    
    private Long contRegCheckDDJJ;    
    
    
    private List<TipoBaseGt> formTipoRegistroLista; 
    private TipoBaseGt formTipoRegistroSelected;
    
    
    //==========     Panel Administ PJ    ===================    
    private SbPersonaGt regAdminist;
    private SbPersonaGt regUserAdminist;
    
    private String administRazonSocial;
    private String administRUC;
        
    //=======================================================
    //=============   Panel Administ PN   ===================
    //=======================================================    
    private String administNombres;
    private String administTipDoc;
    private String administNumDoc;
    
    //=======================================================
    //==============    Panel Modalidad    ==================
    //=======================================================    
    private List<TipoBaseGt> regTipoSeguridadList;    
    private TipoBaseGt regTipoSeguridadSelected;
    
    private TipoBaseGt regTipoProcesoId;
    
    private List<TipoBaseGt> regFormaTipoSeguridadList;
    private TipoBaseGt regFormaTipoSeguridadSelected;
    
    private List<TipoSeguridad> regServicioPrestadoList;
    private TipoSeguridad regServicioPrestadoSelected;
    
    private List<SbRecibos> regListDatoRecibos;
    private SbRecibos regDatoRecibos;
    private String regNroComprobante;
    
    //=======================================================
    //==============    Foto   ==================
    //=======================================================    
    private boolean habilitarAdjuntarFoto;
    private UploadedFile fileFOTO;
    private byte[] fotoByte;
    private StreamedContent archivoFOTO;
    private String nomArchivoFOTO;
    private String nomArchivoFOTO_anterior;
    
    //=======================================================
    //==============    Cetificado Fisico Mental   ===========
    //=======================================================    
    private boolean habilitarAdjuntarCertFiscoMntal;
    private UploadedFile fileCertFiscoMntal;
    private byte[] certFiscoMntalByte;
    private StreamedContent archivoCertFiscoMntal;
    private String nomArchivoCertFiscoMntal;
    private String nomArchivoCertFiscoMntal_anterior;
    
    //=======================================================
    //==============    Copia Contrato   ===========
    //=======================================================    
    private boolean habilitarAdjuntarCopyContrato;
    private UploadedFile fileCopyContrato;
    private byte[] copyContratoByte;
    private StreamedContent archivoCopyContrato;
    private String nomArchivoCopyContrato;
    private String nomArchivoCopyContrato_anterior;

    
    //=======================================================
    //======    Medios Contacto   ===========
    //=======================================================    
    
    private List<TipoBaseGt> regPrioridadLPSList;
    private TipoBaseGt regPrioridadLPSSelected;
    
    private List<TipoBaseGt> regTipoMedioContactoLPSList;
    private TipoBaseGt regTipoMedioContactoLPSSelected;
    
    private ArrayList<SspContacto> lstContactos;
    private List<SspContacto> lstContactosList;
    private SspContacto selectedContacto;
    private Long contRegContacto;
    private String regtextoContactoLPS;   

 
    
    

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroFacade ejbSspRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade ejbTipoSeguridadFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt ejbSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroEventoFacade ejbSspRegistroEventoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspServicioFacade ejbSspServicioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspFormaFacade ejbSspFormaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspContactoFacade ejbSspContactoFacade;    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPaisFacadeGt ejbSbPaisFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRequisitoFacade ejbSspRequisitoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspArchivoFacade ejbSspArchivoFacade;
    
    
    public GsspSolicitudAutorizacionSISPAController() {
        
    }
    
    public void iniciarValores_SISPA() { 
        
        estado = EstadoCrud.CREARSISPA;
        regTipoProcesoId = null;
        regFormaTipoSeguridadSelected = null;
        termCondicionAceptaDDJJ = false;
        selectedOptions = new String[0];
        contRegCheckDDJJ = 0L;
        
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());
        
        //============  Administrado PJ ===============        
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();              

        //============  Administrado PN ===============        
        administNombres = p_user.getNombres()+" "+p_user.getApePat()+""+p_user.getApeMat();
        administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
        administNumDoc = regUserAdminist.getNumDoc();
        
        if(regAdminist.getTipoId().getId().equals(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR").getId())){
            administradoConRUC = true; 
            tipoAdministradoUsuario = "PersonaJuridica";
        }else{
            administradoConRUC = false;
            tipoAdministradoUsuario = "PersonaNatural";
        }
        
        regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_SISPA'");
        regTipoSeguridadSelected = ejbTipoBaseFacade.tipoPorCodProg("TP_GSSP_MOD_SISPA");
        
        regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP"); // (SI_ARMA_SSP, NO_ARMA_SSP)
        regServicioPrestadoSelected = null;
        
        regNroComprobante = null;
        regDatoRecibos = null;
        
        //=============================================================
        //==============          Medios Contactos           ==========
        //=============================================================
        
        regPrioridadLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_CONTAC_PRIN','TP_CONTAC_SEC'");        
        regPrioridadLPSSelected = null;
        
        regTipoMedioContactoLPSList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_MEDCO_COR','TP_MEDCO_FAX','TP_MEDCO_FIJ','TP_MEDCO_MOV'");
        regTipoMedioContactoLPSSelected = null;  
        
        lstContactos = new ArrayList<SspContacto>();
        lstContactosList = null;
        selectedContacto = null;
        contRegContacto = 0L;
        regtextoContactoLPS = null;
        
        //=============================================
        //=========  Foto   ============
        //=============================================
        
        fileFOTO = null;
        fotoByte = null;
        archivoFOTO = null;
        nomArchivoFOTO = null;
        habilitarAdjuntarFoto = false;
        
         //===================================================
        //=========  Certificado Fisico Mental   ============
        //===================================================
        
        fileCertFiscoMntal = null;
        certFiscoMntalByte = null;
        archivoCertFiscoMntal = null;
        nomArchivoCertFiscoMntal = null;
        nomArchivoCertFiscoMntal_anterior = null;
        habilitarAdjuntarCertFiscoMntal = false;
    
        //===================================================
        //=========  Copia de Contrato   ============
        //===================================================
        
        fileCopyContrato = null;
        copyContratoByte = null;
        archivoCopyContrato = null;
        nomArchivoCopyContrato = null;
        nomArchivoCopyContrato_anterior = null;
        habilitarAdjuntarCopyContrato = false;
        
        //=====================================================
        //=====================================================
        //=====================================================
        
        registro = new SspRegistro();
        registro.setId(null);
        registro.setCarneId(null);
        registro.setTipoRegId(null);
        registro.setTipoOpeId(null);
        registro.setTipoProId(null);
        registro.setEmpresaId(null);
        registro.setEstadoId(null);
        registro.setComprobanteId(null);
        registro.setRepresentanteId(null);
        registro.setRegistroId(null);
        registro.setNroSolicitiud(null);
        registro.setSedeSucamec(null);        
        registro.setNroExpediente(null);
        registro.setFechaIni(null);
        registro.setFechaFin(null);
        registro.setObservacion(null);
        registro.setUsuarioCreacionId(null);
        registro.setFecha(new Date());
        registro.setActivo(JsfUtil.TRUE);
        registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registro.setAudNumIp(JsfUtil.getIpAddress());        
        registro.setSspRegistroEventoList(new ArrayList());
        registro.setSspRegistroList(new ArrayList());
        registro.setSspRequisitoList(new ArrayList());
        
        //===========================================================
        
        registroEvento = new SspRegistroEvento();
        registroEvento.setId(null);
        registroEvento.setRegistroId(null);
        registroEvento.setTipoEventoId(null);
        registroEvento.setObservacion(null);
        registroEvento.setUserId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));        
        registroEvento.setFecha(new Date());
        registroEvento.setActivo(JsfUtil.TRUE);
        registroEvento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registroEvento.setAudNumIp(JsfUtil.getIpAddress());
        
        servicio = new SspServicio();
        servicio.setId(null);
        servicio.setRegistroId(null);
        servicio.setServicioPrestadoId(null);
        servicio.setFecha(new Date());
        servicio.setActivo(JsfUtil.TRUE);
        servicio.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        servicio.setAudNumIp(JsfUtil.getIpAddress());
        
        RequestContext context = RequestContext.getCurrentInstance();      
        context.update("pnl_AdministradoPJ");
        context.update("pnl_AdministradoPN");
        context.update("pnlDatosModalidad");
        context.update("pnl_MediosContacto");
        context.update("pnl_Fotografia");
        context.update("pnl_CertFisicoMental");
        context.update("pnl_CopiaContrato");
    }
    
    
    public void onload_SISPA(){
        RequestContext context = RequestContext.getCurrentInstance();        
        context.execute("PF('wvDlgDeclaracionJuradaSISPA').show()"); 
        context.update("frmCuerpoDDJJ");
    }
    
    public String obtenerMsjeFoto() {
        return JsfUtil.bundleBDIntegrado("sspCarne_crear_msjeFoto") + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selFotoPersSeguridadPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }
    
    
    public void selectedOptionsChanged() {        
        contRegCheckDDJJ = 0L;                
        if (selectedOptions != null) {                      
            for (int i = 0; i < selectedOptions.length; i++) {                
                contRegCheckDDJJ++; 
                String codProgItem = selectedOptions[i];        
            }            
        }
    }
     
   
    
    public void btnAceptarDDJJ(){        
        if(contRegCheckDDJJ < 8){
            JsfUtil.mensajeAdvertencia("El administrado debe cumplir con las condiciones establecidas en el artículo 57 del DS Nº XX-2023-IN");
            return;
        } 
        termCondicionAceptaDDJJ = true;   
        
       RequestContext context = RequestContext.getCurrentInstance();       
       context.update("FormSISPA");
       context.execute("PF('wvDlgDeclaracionJuradaSISPA').hide()");
       
    } 
    
    
    public String btnCancelarDDJJ(){
       termCondicionAceptaDDJJ = false;       
       RequestContext context = RequestContext.getCurrentInstance();
       context.update("frmCuerpoDDJJ");
       context.execute("PF('wvDlgDeclaracionJuradaSISPA').hide()");       
       return "/aplicacion/gssp/gsspSolicitudAutorizacion/Create";
    }
    
    public String btnCancelarSISPA(){
       termCondicionAceptaDDJJ = false;       
       RequestContext context = RequestContext.getCurrentInstance();
       context.update("FormSISPA");     
       gsspSolicitudAutorizacionController.mostrarCrear();
       return "/aplicacion/gssp/gsspSolicitudAutorizacion/Create";
    }
    
    public String btnCancelarSISPA_Editar(){
       termCondicionAceptaDDJJ = false;       
       RequestContext context = RequestContext.getCurrentInstance();
       context.update("FormSISPA");
       gsspSolicitudAutorizacionController.prepareList();       
       return "/aplicacion/gssp/gsspSolicitudAutorizacion/List";
    }
    
    public String btnRegresarBandejaSISPA(){
       termCondicionAceptaDDJJ = false;       
       registro = new SspRegistro();      
       
       gsspSolicitudAutorizacionController.setEstado(EstadoCrud.BUSCAR);
       return "/aplicacion/gssp/gsspSolicitudAutorizacion/List";
    }
    
    
    public void handleFileUploadFoto(FileUploadEvent event) {
        try {
            if (event != null) {
                fileFOTO = event.getFile();
                if (JsfUtil.verificarJPG(fileFOTO)) {
                    fotoByte = IOUtils.toByteArray(fileFOTO.getInputstream());
                    fotoByte = JsfUtil.escalarRotarImagen(fotoByte, 480, 640, 0);
                    archivoFOTO = new DefaultStreamedContent(fileFOTO.getInputstream(), "image/jpeg");
                    nomArchivoFOTO = fileFOTO.getFileName();
                    nomArchivoFOTO_anterior = null;
                } else {
                    fileFOTO = null;
                    fotoByte = null;
                    archivoFOTO = null;
                    nomArchivoFOTO = null;
                    nomArchivoFOTO_anterior = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public String validarSiguienteSolicAutorizacion(TipoBaseGt regTipoSeguridadSelected, TipoBaseGt tbTipoRegistroSelected) {
        
        String URL_Formulario = "";
        boolean validacion = true;
        
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());
                
        List<SspRegistro> ListSolicitudPresentados = new ArrayList<>();             
        ListSolicitudPresentados = ejbSspRegistroFacade.listaSolitudesPresentados_Vigentes("TP_GSSP_MOD_SISPA", regUserAdminist.getId());
        //System.out.println("ListSolicitudPresentados->"+ListSolicitudPresentados);
      
        
        if (tbTipoRegistroSelected == null) {                        
            JsfUtil.mensajeAdvertencia("Seleccione tipo de registro.");            
            validacion = false;
            
        }else if (regTipoSeguridadSelected == null) {
            validacion = false;
            JsfUtil.mensajeAdvertencia("Es necesario seleccionar tipo de servicio.");
            
        }else if(tbTipoRegistroSelected.getCodProg().equals("TP_REGIST_ADECU")){ 

            formTipoRegistroSelected = tbTipoRegistroSelected;
            
            Calendar xFechaActualCalendar = Calendar.getInstance();
            Date xFechaActual = JsfUtil.getFechaSinHora(xFechaActualCalendar.getTime());
            
            String xFechaLimiteAdeacuacion = ejbSbParametroFacade.obtenerParametroXCodProg("TP_VIG_2FECFINADECUA").getValor();
            Date formFechaFinAdeacuacion = JsfUtil.stringToDate(xFechaLimiteAdeacuacion, "dd/MM/yyyy");
            
            if(xFechaActual.compareTo(JsfUtil.getFechaSinHora(formFechaFinAdeacuacion)) <= 0){                    
                validacion = true;             
            }else{
                JsfUtil.mensajeAdvertencia("Ud. ya no puede generar un registro de Adecuación. La fecha límite para el registro es hasta "+xFechaLimiteAdeacuacion);                    
                validacion = false;
            } 
        }
        
        
        if(validacion == true){
            if(ListSolicitudPresentados != null){
                setEstado(EstadoCrud.EXISTESISPA);                
                URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/ExisteRegistro"; 
            }else{
                setEstado(EstadoCrud.CREARSISPA);       
                URL_Formulario = "/aplicacion/gssp/gsspSolicitudAutorizacion/sispa/CreateSispa";
                iniciarValores_SISPA();
            }
        }
    
        return URL_Formulario;
    }
    
    
    
    public void handleFileUploadPDF_CertFiscoMntal(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCertFiscoMntal = event.getFile();
                if (JsfUtil.verificarPDF(fileCertFiscoMntal)) {
                    certFiscoMntalByte = IOUtils.toByteArray(fileCertFiscoMntal.getInputstream());
                    archivoCertFiscoMntal = new DefaultStreamedContent(fileCertFiscoMntal.getInputstream(), "application/pdf");
                    nomArchivoCertFiscoMntal = fileCertFiscoMntal.getFileName();
                    nomArchivoCertFiscoMntal_anterior = null;
                } else {
                    fileCertFiscoMntal = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public String obtenerMsjeInvalidSizeFileSelAutoPrestacServ() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage")+" "+(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selAutoPrestacServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }
    
    public String obtenerMsjeInvalidSizeFile_SelCertSaludFisicoMental() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage")+" "+(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selCertSaludFisicoMentalaPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }
    
    public String obtenerMsjeInvalidSizeFile_SelCopiaContrato() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage")+" "+(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selCopiaContratoPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }
    
    public String obtenerMsjeInvalidSizeFile_SelFotoPersSeguridad() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage")+ " - "+(Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selFotoPersSeguridadPrestaServ_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }
    
    public void handleFileUploadPDF_CopyContrato(FileUploadEvent event) {
        try {
            if (event != null) {
                fileCopyContrato= event.getFile();
                if (JsfUtil.verificarPDF(fileCopyContrato)) {
                    copyContratoByte = IOUtils.toByteArray(fileCopyContrato.getInputstream());
                    archivoCopyContrato= new DefaultStreamedContent(fileCopyContrato.getInputstream(), "application/pdf");
                    nomArchivoCopyContrato = fileCopyContrato.getFileName();
                    nomArchivoCopyContrato_anterior = null;
                } else {
                    fileCopyContrato = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void agregarTipoContacto(){
        boolean validacionCont = true;
        
        if(regPrioridadLPSSelected == null){
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.mensajeError("Por favor seleccione prioridad");
        }
        
        if(regTipoMedioContactoLPSSelected == null){
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeError("Por favor seleccione medio de contacto");
        }
        
        
        if(regtextoContactoLPS == null){
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
            JsfUtil.mensajeError("Por favor ingrese contacto");
        }else{
            if(regtextoContactoLPS.equals("")){
                validacionCont = false;
                JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeError("Por favor ingrese contacto");
            }
        }
        
        int contPrincCorreo = 0;
        int contPrincTelFijo = 0;
        int contPrincTelMovil = 0;
        int contPrincFax = 0;
        
        if(validacionCont){
            TipoBaseGt ccPrioridad = ejbTipoBaseFacade.selectLike(regPrioridadLPSSelected.getId().toString()).get(0);
            Long xCodPrioridadPrincipal = (ccPrioridad.getCodProg().equals("TP_CONTAC_PRIN")) ? ccPrioridad.getId() : null;
            
            TipoBaseGt cMedContacto = ejbTipoBaseFacade.selectLike(regTipoMedioContactoLPSSelected.getId().toString()).get(0);
            Long xIdCorreo = (cMedContacto.getCodProg().equals("TP_MEDCO_COR")) ? cMedContacto.getId() : null;
            Long xIdTelFijo = (cMedContacto.getCodProg().equals("TP_MEDCO_FIJ")) ? cMedContacto.getId() : null;
            Long xIdTelMovil = (cMedContacto.getCodProg().equals("TP_MEDCO_MOV")) ? cMedContacto.getId() : null;
            Long xIdFax = (cMedContacto.getCodProg().equals("TP_MEDCO_FAX")) ? cMedContacto.getId() : null;
            
            if(xCodPrioridadPrincipal != null){

                for (SspContacto contact : lstContactos) {

                    if((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdCorreo)) ){
                        contPrincCorreo++;
                    }
                    
                    if((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdTelFijo)) ){
                        contPrincTelFijo++;
                    } 
                    
                    if((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdTelMovil)) ){
                        contPrincTelMovil++;
                    }
                    
                    if((contact.getValor().equals(xCodPrioridadPrincipal.toString())) && (contact.getTipoMedioId().getId().equals(xIdFax)) ){
                        contPrincFax++;
                    }

                }
                
            }
        }
        
        
        if(contPrincCorreo > 0){
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un correo principal");
        }
        
        if(contPrincTelFijo > 0){
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un teléfono fijo principal");
        }
        
        if(contPrincTelMovil > 0){
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un teléfono movil principal");
        }
        
        if(contPrincFax > 0){
            validacionCont = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Ya existe un fax principal");
        }
    
        
        if(validacionCont){            
            SspContacto ItemContacto = new SspContacto();
            contRegContacto++;               
        
            ItemContacto.setId(contRegContacto);
            ItemContacto.setRegistroId(null);
            ItemContacto.setValor(regPrioridadLPSSelected.getId()+"");
            ItemContacto.setTipoMedioId(regTipoMedioContactoLPSSelected);
            ItemContacto.setDescripcion(regtextoContactoLPS);
            lstContactos.add(ItemContacto);            
        }
        
        regPrioridadLPSSelected = null;
        regTipoMedioContactoLPSSelected = null;
        regtextoContactoLPS = null;
    }
    
    
    
    public void deleteContacto(Long id) {
        
        for(int i = 0; i<lstContactos.size(); i++){
            if(lstContactos.get(i).getId().equals(id)){
                lstContactos.remove(i);
                i--;
            }
        }
    }
    
    public String obtenerForm() {
        switch (estado) {
            case CREARSISPA:
                return "FormSISPA";
            case EDITARSISPA:
                return "editForm";
            case VERSISPA:
                return "viewForm";
        }
        return null;
    }
    
    
    public boolean validaRegistroSISPA() {
        boolean validacion = true;
            
//        if(regServicioPrestadoSelected == null){
//            validacion = false;
//            JsfUtil.invalidar(obtenerForm() + ":cboServPrestado");
//            JsfUtil.mensajeAdvertencia("Por favor seleccione servicio prestado");
//            return false;
//        } 
        
        if(lstContactos.size() == 0){
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_Prioridad");
            JsfUtil.invalidar(obtenerForm() + ":cboLPE_Alm_MedioContacto");
            JsfUtil.invalidar(obtenerForm() + ":txtLPE_Alm_DescripMedioContacto");
            JsfUtil.mensajeError("Por favor debe agregar por lo menos un registro en el medios de contacto");
            return false;
        }
   
                 
        if(archivoFOTO == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":idFoto");
            JsfUtil.mensajeAdvertencia("Por favor debe adjuntar fotografia digital y actualizado del rostro de la persona");
            return false;
        }

        if(nomArchivoCertFiscoMntal == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":txf_ArchivoCertFiscoMntal");
            JsfUtil.mensajeAdvertencia("Por favor debe adjuntar archivo de Certificado de Salud Fisico Mental expedido por una Institución Prestadora de Servicios de Salud (IPRESS)");
            return false;
        }

        if(nomArchivoCopyContrato == null){
            validacion = false;
            JsfUtil.invalidar(obtenerForm() + ":txf_ArchivoCopiaContrato");
            JsfUtil.mensajeAdvertencia("Por favor debe adjuntar archivo Copia del contrato");
            return false;
        }
            
      
        
         
        return validacion;
    }
    
    
    
    public String btnRegistrarSISPA(){
        termCondicionAceptaDDJJ = true;
        
        if (validaRegistroSISPA()) {
            
            try {
                
                regTipoProcesoId = regTipoSeguridadSelected;
                 
                registro.setId(null);
                registro.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR")); //TP_REGIST_NOR / TP_REGIST_MOD
                registro.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));    //TP_OPE_INI / TP_OPE_REN / TP_OPE_AMP
                registro.setTipoProId(regTipoProcesoId);
                registro.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_ASS"));                
                registro.setEmpresaId(regAdminist);                
                registro.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));
                registro.setComprobanteId(null);//Datos del Comprobante Pagado
                registro.setRegistroId(null);
                registro.setNroSolicitiud("S/N");       
                registro.setSedeSucamec(ejbTipoBaseFacade.listarAreasLimaOD().get(0));
                registro.setFecha(new Date());
                registro.setNroExpediente(null);
                registro.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));  
                registro = (SspRegistro) JsfUtil.entidadMayusculas(registro, "");                
                ejbSspRegistroFacade.create(registro);


                //=========================================================================================

                registroEvento = (SspRegistroEvento) JsfUtil.entidadMayusculas(registroEvento, "");
                registroEvento.setId(null);
                registroEvento.setRegistroId(registro);
                registroEvento.setTipoEventoId(registro.getEstadoId());
                registroEvento.setObservacion("Crear solicitud de autorización para la prestación de servicio de seguridad.");
                ejbSspRegistroEventoFacade.create(registroEvento);

                servicio = (SspServicio) JsfUtil.entidadMayusculas(servicio, "");
                servicio.setId(null);
                servicio.setRegistroId(registro);
                servicio.setServicioPrestadoId(regServicioPrestadoSelected);
                ejbSspServicioFacade.create(servicio);   
                

                for(SspContacto itemContacto : lstContactos){
                    itemContacto.setId(null);
                    itemContacto.setRegistroId(registro);
                    itemContacto.setValor(itemContacto.getValor());
                    itemContacto.setTipoMedioId(itemContacto.getTipoMedioId());                    
                    itemContacto.setDescripcion(itemContacto.getDescripcion());
                    itemContacto.setFecha(new Date());
                    itemContacto.setActivo(JsfUtil.TRUE);
                    itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSspContactoFacade.create(itemContacto); 
                }
                     
           
                                     
                    //========= REQUISITO FOTO DIGITAL  =================                     
                    String fileNameArchivoFoto = "";
                    fileNameArchivoFoto = nomArchivoFOTO.toUpperCase();
                    fileNameArchivoFoto = "ASSGssp_FOTO_AS" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_FOTO_AS").toString() + fileNameArchivoFoto.substring(fileNameArchivoFoto.lastIndexOf('.'), fileNameArchivoFoto.length());
                    
                    SspRequisito requisito = new SspRequisito();                    
                    requisito.setActivo(JsfUtil.TRUE);
                    requisito.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito.setAudNumIp(JsfUtil.getIpAddress());
                    requisito.setFecha(new Date());
                    requisito.setId(null);
                    requisito.setRegistroId(registro);
                    requisito.setSspArchivoList(new ArrayList());
                    requisito.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_FD"));
                    requisito.setValorRequisito(null);
                    requisito = (SspRequisito) JsfUtil.entidadMayusculas(requisito, "");
                    ejbSspRequisitoFacade.create(requisito);
                    
                    SspArchivo archivo = new SspArchivo();
                    archivo.setActivo(JsfUtil.TRUE);
                    archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivo.setAudNumIp(JsfUtil.getIpAddress());
                    archivo.setId(null);
                    archivo.setPathupload("Documentos_pathUpload_ASSGssp_FOTO");
                    archivo.setNombre(fileNameArchivoFoto);
                    archivo.setRequisitoId(requisito);
                    archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_JPG"));
                    archivo = (SspArchivo) JsfUtil.entidadMayusculas(archivo, "");                    
                    ejbSspArchivoFacade.create(archivo);
                    
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_FOTO").getValor() + fileNameArchivoFoto.toUpperCase()), fotoByte);

                    
                    //========= REQUISITO CERTIFICADO DE SALUD Y MENTAL  =================
                     
                    String fileNameArchivoCERTFISMENTAL = "";
                    fileNameArchivoCERTFISMENTAL = nomArchivoCertFiscoMntal.toUpperCase();
                    fileNameArchivoCERTFISMENTAL = "ASSGssp_CERT_FM" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CERT_FM").toString() + fileNameArchivoCERTFISMENTAL.substring(fileNameArchivoCERTFISMENTAL.lastIndexOf('.'), fileNameArchivoCERTFISMENTAL.length());
                    
                    SspRequisito requisito2 = new SspRequisito();                    
                    requisito2.setActivo(JsfUtil.TRUE);
                    requisito2.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito2.setAudNumIp(JsfUtil.getIpAddress());
                    requisito2.setFecha(new Date());
                    requisito2.setId(null);
                    requisito2.setRegistroId(registro);
                    requisito2.setSspArchivoList(new ArrayList());
                    requisito2.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CFM"));
                    requisito2.setValorRequisito(null);
                    requisito2 = (SspRequisito) JsfUtil.entidadMayusculas(requisito2, "");
                    ejbSspRequisitoFacade.create(requisito2);
                    
                    SspArchivo archivo2 = new SspArchivo();
                    archivo2.setActivo(JsfUtil.TRUE);
                    archivo2.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivo2.setAudNumIp(JsfUtil.getIpAddress());
                    archivo2.setId(null);
                    archivo2.setPathupload("Documentos_pathUpload_ASSGssp_CERTSALUDFM");
                    archivo2.setNombre(fileNameArchivoCERTFISMENTAL);
                    archivo2.setRequisitoId(requisito2);
                    archivo2.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    archivo2 = (SspArchivo) JsfUtil.entidadMayusculas(archivo2, "");                    
                    ejbSspArchivoFacade.create(archivo2);
                    
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CERTSALUDFM").getValor() + fileNameArchivoCERTFISMENTAL.toUpperCase()), certFiscoMntalByte);

                    
                    //========= REQUISITO COPIA CONTRATO DE TRABAJO  =================
                     
                    String fileNameArchivoCOPYCONTRATOTRABAJO= "";
                    fileNameArchivoCOPYCONTRATOTRABAJO = nomArchivoCopyContrato.toUpperCase();
                    fileNameArchivoCOPYCONTRATOTRABAJO = "ASSGssp_COPY_CT" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_COPY_CT").toString() + fileNameArchivoCOPYCONTRATOTRABAJO.substring(fileNameArchivoCOPYCONTRATOTRABAJO.lastIndexOf('.'), fileNameArchivoCOPYCONTRATOTRABAJO.length());
                    
                    SspRequisito requisito3 = new SspRequisito();                    
                    requisito3.setActivo(JsfUtil.TRUE);
                    requisito3.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito3.setAudNumIp(JsfUtil.getIpAddress());
                    requisito3.setFecha(new Date());
                    requisito3.setId(null);
                    requisito3.setRegistroId(registro);
                    requisito3.setSspArchivoList(new ArrayList());
                    requisito3.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CT"));
                    requisito3.setValorRequisito(null);
                    requisito3 = (SspRequisito) JsfUtil.entidadMayusculas(requisito3, "");
                    ejbSspRequisitoFacade.create(requisito3);
                    
                    SspArchivo archivo3 = new SspArchivo();
                    archivo3.setActivo(JsfUtil.TRUE);
                    archivo3.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivo3.setAudNumIp(JsfUtil.getIpAddress());
                    archivo3.setId(null);
                    archivo3.setPathupload("Documentos_pathUpload_ASSGssp_COPYCT");
                    archivo3.setNombre(fileNameArchivoCOPYCONTRATOTRABAJO);
                    archivo3.setRequisitoId(requisito3);
                    archivo3.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    archivo3 = (SspArchivo) JsfUtil.entidadMayusculas(archivo3, "");                    
                    ejbSspArchivoFacade.create(archivo3);
                    
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_COPYCT").getValor() + fileNameArchivoCOPYCONTRATOTRABAJO.toUpperCase()), copyContratoByte);
                              
                        
                //===========================================================================
                
                JsfUtil.mensaje("Se registró la solicitud con Código: " + registro.getId());
                
                return  gsspSolicitudAutorizacionController.prepareList();
                
            } catch (Exception e) {
                System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
                e.printStackTrace();
                JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
            }
            
        }
        
        return null;
        
    }
    
    public String obtenerNroSolicitudEmision() {
        String nroSolicitud = "";
        nroSolicitud = ejbSspRegistroFacade.obtenerNroSolicitudEmisionRegistro() + "-" + JsfUtil.mostrarAnio(new Date()) + "-SUCAMEC/GSSP-EC";
        return nroSolicitud;
    }  
    
    
    //=====================================================        SISPA - VER       =========================================================
    
    
    public void iniciarValores_SISPA_Ver(SspRegistro registro) { 
        
        estado = EstadoCrud.VERSISPA;                
        regTipoProcesoId = registro.getTipoProId();
        regFormaTipoSeguridadSelected = null;
        
        termCondicionAceptaDDJJ = true;        
        
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());

        //============  Administrado PN ===============        
        administNombres = p_user.getNombres()+" "+p_user.getApePat()+""+p_user.getApeMat();
        administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
        administNumDoc = regUserAdminist.getNumDoc();
        
        if(regAdminist.getTipoId().getId().equals(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR").getId())){
            administradoConRUC = true; 
            tipoAdministradoUsuario = "PersonaJuridica";
        }else{
            administradoConRUC = false;
            tipoAdministradoUsuario = "PersonaNatural";
        }
        
        regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_SISPA'");
        regTipoSeguridadSelected = registro.getTipoProId();
        
        regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP"); // (SI_ARMA_SSP, NO_ARMA_SSP)
        regServicioPrestadoSelected = ejbSspServicioFacade.buscarServicioByRegistroId(registro).getServicioPrestadoId();
        
        
        //=============================================================
        //==============          Medios Contactos           ==========
        //=============================================================
        
        
        lstContactosList = ejbSspContactoFacade.listarContactoxIdRegistro(registro.getId());
        lstContactos = null;
        lstContactos = new ArrayList<SspContacto>();
        
        for (SspContacto contact : lstContactosList) {
            SspContacto ItemContacto = new SspContacto();
            
            ItemContacto.setId(contact.getId());
            ItemContacto.setRegistroId(contact.getRegistroId());
            ItemContacto.setValor(contact.getValor());
            ItemContacto.setTipoMedioId(contact.getTipoMedioId());
            ItemContacto.setDescripcion(contact.getDescripcion());
            lstContactos.add(ItemContacto); 
        }   
        
        //=============================================
        //=========  Foto   ============
        //=============================================
        
        fileFOTO = null;
        fotoByte = null;
        archivoFOTO = null;
        nomArchivoFOTO = null;
        nomArchivoFOTO_anterior = null;
        
        SspRequisito requisitoFoto = new SspRequisito();
        requisitoFoto = ejbSspRequisitoFacade.buscarSspRequisito_By_RegistroId_TipoRequisitoId(registro, ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_FD"));
        
        SspArchivo sspArchivoFoto = new SspArchivo();
        sspArchivoFoto = ejbSspArchivoFacade.buscarSspArchivo_By_RequisitoId(requisitoFoto);
        nomArchivoFOTO = sspArchivoFoto.getNombre();
        nomArchivoFOTO_anterior = sspArchivoFoto.getNombre();
        
        try {
            if (nomArchivoFOTO != null) {
                fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_FOTO").getValor() + nomArchivoFOTO));
            } else {
                JsfUtil.mensajeAdvertencia("No se encontró la foto del personal de seguridad");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeAdvertencia("No se pudo cargar la foto del personal de seguridad");
        }
        
         //===================================================
        //=========  Certificado Fisico Mental   ============
        //===================================================
        
        fileCertFiscoMntal = null;
        certFiscoMntalByte = null;
        archivoCertFiscoMntal = null;
        nomArchivoCertFiscoMntal = null;
        nomArchivoCertFiscoMntal_anterior = null;
        
        SspRequisito requisitoCertFiscoMntal = new SspRequisito();
        requisitoCertFiscoMntal = ejbSspRequisitoFacade.buscarSspRequisito_By_RegistroId_TipoRequisitoId(registro, ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CFM"));
        
        SspArchivo sspArchivoCertFiscoMntal = new SspArchivo();
        sspArchivoCertFiscoMntal = ejbSspArchivoFacade.buscarSspArchivo_By_RequisitoId(requisitoCertFiscoMntal);
        nomArchivoCertFiscoMntal = sspArchivoCertFiscoMntal.getNombre();
    
        //===================================================
        //=========  Copia de Contrato   ============
        //===================================================
        
        fileCopyContrato = null;
        copyContratoByte = null;
        archivoCopyContrato = null;
        nomArchivoCopyContrato = null;
        nomArchivoCopyContrato_anterior = null;
        habilitarAdjuntarCopyContrato = false;
        
        SspRequisito requisitoCopyContrato = new SspRequisito();
        requisitoCopyContrato = ejbSspRequisitoFacade.buscarSspRequisito_By_RegistroId_TipoRequisitoId(registro, ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CT"));
        
        SspArchivo sspArchivoCopyContrato = new SspArchivo();
        sspArchivoCopyContrato = ejbSspArchivoFacade.buscarSspArchivo_By_RequisitoId(requisitoCopyContrato);
        nomArchivoCopyContrato = sspArchivoCopyContrato.getNombre();
        
        
        RequestContext context = RequestContext.getCurrentInstance();   
        context.update("FormSISPAVer");
        
    }
    
    public boolean renderBtnArchivoCertFiscoMntal() {
        if (registro != null) {
            return nomArchivoCertFiscoMntal != null;
        }
        return false;
    }
    
    
    
    public boolean renderBtnArchivoCopyContrato() {
        if (registro != null) {
            return nomArchivoCopyContrato != null;
        }
        return false;
    }
    
    
    
    
    
    public StreamedContent cargarDocumentoCertFiscoMntal() {
        try {
            String nombreArch = nomArchivoCertFiscoMntal;
            
            if (fileCertFiscoMntal != null) {
                return new DefaultStreamedContent(fileCertFiscoMntal.getInputstream(), "application/pdf", fileCertFiscoMntal.getFileName());
            } else {
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CERTSALUDFM").getValor(), nombreArch, nombreArch, "application/pdf");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("No se pudo encontrar el archivo");
        }
        return null;
    }
    
    
    public StreamedContent cargarDocumentoCopyContrato() {
        try {
            String nombreArch = nomArchivoCopyContrato;
            
            if (fileCopyContrato != null) {
                return new DefaultStreamedContent(fileCopyContrato.getInputstream(), "application/pdf", fileCopyContrato.getFileName());
            } else {
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_COPYCT").getValor(), nombreArch, nombreArch, "application/pdf");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("No se pudo encontrar el archivo");
        }
        return null;
    }
    
    
    
    public void iniciarValores_SISPA_Editar(SspRegistro registro) { 
        
        lstObservaciones = null;
        //Busca las Observaciones
        if (lstObservaciones == null) {
            lstObservaciones = new ArrayList();
        }
        for (SspRegistroEvento ce : registro.getSspRegistroEventoList()) {
            if (ce.getActivo() == 1 && ce.getTipoEventoId().getCodProg().equals("TP_ECC_OBS")) {
                lstObservaciones.add(ce);
            }
        }
        //Fin de Busca las Observaciones
        
        estado = EstadoCrud.EDITARSISPA;                
        regTipoProcesoId = registro.getTipoProId();
        regFormaTipoSeguridadSelected = null;
        
        termCondicionAceptaDDJJ = true;        
        
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());

        //============  Administrado PN ===============        
        administNombres = p_user.getNombres()+" "+p_user.getApePat()+""+p_user.getApeMat();
        administTipDoc = regUserAdminist.getTipoDoc().getAbreviatura();
        administNumDoc = regUserAdminist.getNumDoc();
        
        if(regAdminist.getTipoId().getId().equals(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR").getId())){
            administradoConRUC = true; 
            tipoAdministradoUsuario = "PersonaJuridica";
        }else{
            administradoConRUC = false;
            tipoAdministradoUsuario = "PersonaNatural";
        }
        
        regTipoSeguridadList = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_MOD_SISPA'");
        regTipoSeguridadSelected = registro.getTipoProId();
        
        regServicioPrestadoList = ejbTipoSeguridadFacade.lstTipoSeguridad("SV_PRESTADO_SSP"); // (SI_ARMA_SSP, NO_ARMA_SSP)
        regServicioPrestadoSelected = ejbSspServicioFacade.buscarServicioByRegistroId(registro).getServicioPrestadoId();
        
        
        //=============================================================
        //==============          Medios Contactos           ==========
        //=============================================================
        
        
        lstContactosList = ejbSspContactoFacade.listarContactoxIdRegistro(registro.getId());
        lstContactos = null;
        lstContactos = new ArrayList<SspContacto>();
        
        for (SspContacto contact : lstContactosList) {
            SspContacto ItemContacto = new SspContacto();
            
            ItemContacto.setId(contact.getId());
            ItemContacto.setRegistroId(contact.getRegistroId());
            ItemContacto.setValor(contact.getValor());
            ItemContacto.setTipoMedioId(contact.getTipoMedioId());
            ItemContacto.setDescripcion(contact.getDescripcion());
            lstContactos.add(ItemContacto); 
        }   
        
        //=============================================
        //=========  Foto   ============
        //=============================================
        
        fileFOTO = null;
        fotoByte = null;
        archivoFOTO = null;
        nomArchivoFOTO = null;
        nomArchivoFOTO_anterior = null;
        
        SspRequisito requisitoFoto = new SspRequisito();
        requisitoFoto = ejbSspRequisitoFacade.buscarSspRequisito_By_RegistroId_TipoRequisitoId(registro, ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_FD"));
        
        SspArchivo sspArchivoFoto = new SspArchivo();
        sspArchivoFoto = ejbSspArchivoFacade.buscarSspArchivo_By_RequisitoId(requisitoFoto);
        nomArchivoFOTO = sspArchivoFoto.getNombre();
        nomArchivoFOTO_anterior = sspArchivoFoto.getNombre();
        
        try {
            if (nomArchivoFOTO != null) {
                fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_FOTO").getValor() + nomArchivoFOTO));
            } else {
                JsfUtil.mensajeAdvertencia("No se encontró la foto del personal de seguridad");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeAdvertencia("No se pudo cargar la foto del personal de seguridad");
        }
        
         //===================================================
        //=========  Certificado Fisico Mental   ============
        //===================================================
        
        fileCertFiscoMntal = null;
        certFiscoMntalByte = null;
        archivoCertFiscoMntal = null;
        nomArchivoCertFiscoMntal = null;
        nomArchivoCertFiscoMntal_anterior = null;
        
        SspRequisito requisitoCertFiscoMntal = new SspRequisito();
        requisitoCertFiscoMntal = ejbSspRequisitoFacade.buscarSspRequisito_By_RegistroId_TipoRequisitoId(registro, ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CFM"));
        
        SspArchivo archivoCertFiscoMntal = new SspArchivo();
        archivoCertFiscoMntal = ejbSspArchivoFacade.buscarSspArchivo_By_RequisitoId(requisitoCertFiscoMntal);
        nomArchivoCertFiscoMntal = archivoCertFiscoMntal.getNombre();        
        nomArchivoCertFiscoMntal_anterior = archivoCertFiscoMntal.getNombre();
    
        //===================================================
        //=========  Copia de Contrato   ============
        //===================================================
        
        fileCopyContrato = null;
        copyContratoByte = null;
        archivoCopyContrato = null;
        nomArchivoCopyContrato = null;
        nomArchivoCopyContrato_anterior = null;
        habilitarAdjuntarCopyContrato = false;
        
        SspRequisito requisitoCopyContrato = new SspRequisito();
        requisitoCopyContrato = ejbSspRequisitoFacade.buscarSspRequisito_By_RegistroId_TipoRequisitoId(registro, ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CT"));
        
        SspArchivo archivoCopyContrato = new SspArchivo();
        archivoCopyContrato = ejbSspArchivoFacade.buscarSspArchivo_By_RequisitoId(requisitoCopyContrato);
        nomArchivoCopyContrato = archivoCopyContrato.getNombre();
        nomArchivoCopyContrato_anterior = archivoCopyContrato.getNombre();
        
        RequestContext context = RequestContext.getCurrentInstance();   
        context.update("FormSISPAVer");
        
    }
    
    
    public List<SspRegistroEvento> getLstObservacionesOrdenadasDesc() {
        if (lstObservaciones != null) {
            Collections.sort(lstObservaciones, new Comparator<SspRegistroEvento>() {
                @Override
                public int compare(SspRegistroEvento one, SspRegistroEvento other) {
                    return other.getFecha().compareTo(one.getFecha());
                }
            });
        }
        return lstObservaciones;
    }
    
    
    public String btnEditarSISPA(){
        termCondicionAceptaDDJJ = true;
        
        if (validaRegistroSISPA()) {
            
            try {
                
                regTipoProcesoId = regTipoSeguridadSelected;                
                registro.setTipoAutId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AUTORIZ_SEL_ASS"));                
                registro.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));  
                registro.setAudNumIp(JsfUtil.getIpAddress());  
                registro = (SspRegistro) JsfUtil.entidadMayusculas(registro, "");                
                ejbSspRegistroFacade.edit(registro);
                
                servicio = ejbSspServicioFacade.buscarServicioByRegistroId(registro);
                servicio.setRegistroId(registro);
                servicio.setServicioPrestadoId(regServicioPrestadoSelected);
                servicio = (SspServicio) JsfUtil.entidadMayusculas(servicio, "");
                ejbSspServicioFacade.edit(servicio);
                
                //===============================       CONTACTOS       =============================================

                List<SspContacto> xListContactosAnt = ejbSspContactoFacade.listarContactoxIdRegistro(registro.getId()); 
                for(SspContacto itemContAnt : xListContactosAnt){
                    itemContAnt.setFecha(new Date());
                    itemContAnt.setActivo(JsfUtil.FALSE);
                    itemContAnt.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    itemContAnt.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSspContactoFacade.edit(itemContAnt); 
                }
                
                //LOS NUEVOS CONTACTOS
                for(SspContacto itemContacto : lstContactos){
                    itemContacto.setId(null);
                    itemContacto.setRegistroId(registro);
                    itemContacto.setValor(itemContacto.getValor());
                    itemContacto.setTipoMedioId(itemContacto.getTipoMedioId());                    
                    itemContacto.setDescripcion(itemContacto.getDescripcion());
                    itemContacto.setFecha(new Date());
                    itemContacto.setActivo(JsfUtil.TRUE);
                    itemContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    itemContacto.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSspContactoFacade.create(itemContacto); 
                }
                
                
                //================================================================================================================
                //===============================       REQUISITO FOTO DIGITAL       =============================================
                //================================================================================================================    
                    
                    String fileNameArchivoFoto = "";
                    if(nomArchivoFOTO_anterior == null){//Editado       
                        fileNameArchivoFoto = nomArchivoFOTO;
                        fileNameArchivoFoto = "ASSGssp_FOTO_AS" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_FOTO_AS").toString() + fileNameArchivoFoto.substring(fileNameArchivoFoto.lastIndexOf('.'), fileNameArchivoFoto.length());                                                
                        fileNameArchivoFoto = fileNameArchivoFoto.toUpperCase();
                    }else{    
                        fileNameArchivoFoto = nomArchivoFOTO;
                        fileNameArchivoFoto = fileNameArchivoFoto.toUpperCase();
                    }
                    
                    List<SspRequisito> listaSspRequisitoFD = ejbSspRequisitoFacade.listarSspRequisito_By_RegistroId_TipoRequisitoId(registro.getId(), "TP_REQ_FD");                    
                    if(listaSspRequisitoFD.size()>0){
                        for(SspRequisito itemSspRequisitoFD : listaSspRequisitoFD){                            
                            SspRequisito itemReqFD = new SspRequisito();
                            itemReqFD = itemSspRequisitoFD;                          
                            itemReqFD.setActivo(JsfUtil.FALSE);
                            itemReqFD.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemReqFD.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspRequisitoFacade.edit(itemReqFD);                             
                        }
                    }
                                  
                    List<SspArchivo> listaSspArchivoFotoSeguridad = ejbSspArchivoFacade.listarArchivos_By_RegistroId_By_Requisitoid(registro.getId(), "TP_REQ_FD");                    
                    if(listaSspArchivoFotoSeguridad.size()>0){                        
                        for(SspArchivo itemArchivo : listaSspArchivoFotoSeguridad){                            
                            SspArchivo itemArchivoOld = new SspArchivo();
                            itemArchivoOld = itemArchivo;
                            itemArchivoOld.getRequisitoId().setActivo(JsfUtil.FALSE);
                            itemArchivoOld.getRequisitoId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemArchivoOld.getRequisitoId().setAudNumIp(JsfUtil.getIpAddress());                            
                            itemArchivoOld.setActivo(JsfUtil.FALSE);
                            itemArchivoOld.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemArchivoOld.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspArchivoFacade.edit(itemArchivoOld);                             
                        }                        
                    }
                        
                    //CREA UN REGISTRO NUEVO - FOTO
                    SspRequisito requisitoFoto = new SspRequisito();
                    requisitoFoto.setId(null);
                    requisitoFoto.setActivo(JsfUtil.TRUE);
                    requisitoFoto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisitoFoto.setAudNumIp(JsfUtil.getIpAddress());
                    requisitoFoto.setFecha(new Date());                    
                    requisitoFoto.setRegistroId(registro);
                    requisitoFoto.setSspArchivoList(new ArrayList());
                    requisitoFoto.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_FD"));
                    requisitoFoto.setValorRequisito(null);
                    requisitoFoto = (SspRequisito) JsfUtil.entidadMayusculas(requisitoFoto, "");
                    ejbSspRequisitoFacade.create(requisitoFoto);

                    SspArchivo archivoFoto = new SspArchivo();
                    archivoFoto.setId(null);
                    archivoFoto.setActivo(JsfUtil.TRUE);
                    archivoFoto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivoFoto.setAudNumIp(JsfUtil.getIpAddress());                    
                    archivoFoto.setPathupload("Documentos_pathUpload_ASSGssp_FOTO");
                    archivoFoto.setNombre(fileNameArchivoFoto);
                    archivoFoto.setRequisitoId(requisitoFoto);
                    archivoFoto.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_JPG"));
                    archivoFoto = (SspArchivo) JsfUtil.entidadMayusculas(archivoFoto, "");                    
                    ejbSspArchivoFacade.create(archivoFoto);
                    
                    if(fotoByte != null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_FOTO").getValor() + fileNameArchivoFoto.toUpperCase()), fotoByte);
                    }
                    
                    
                    //================================================================================================================================
                    //===============================       REQUISITO CERTIFICADO DE SALUD Y MENTAL      =============================================
                    //================================================================================================================================
                    
                    String fileNameArchivoCERTFISMENTAL = "";
                    if(nomArchivoCertFiscoMntal_anterior== null){//Editado
                        fileNameArchivoCERTFISMENTAL = nomArchivoCertFiscoMntal;
                        fileNameArchivoCERTFISMENTAL = "ASSGssp_CERT_FM" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CERT_FM").toString() + fileNameArchivoCERTFISMENTAL.substring(fileNameArchivoCERTFISMENTAL.lastIndexOf('.'), fileNameArchivoCERTFISMENTAL.length());
                        fileNameArchivoCERTFISMENTAL = fileNameArchivoCERTFISMENTAL.toUpperCase();
                    }else{    
                        fileNameArchivoCERTFISMENTAL = nomArchivoCertFiscoMntal;
                        fileNameArchivoCERTFISMENTAL = fileNameArchivoCERTFISMENTAL.toUpperCase();
                    }
                            
                    List<SspRequisito> listaSspRequisitoCertificadoSaludMental = ejbSspRequisitoFacade.listarSspRequisito_By_RegistroId_TipoRequisitoId(registro.getId(), "TP_REQ_CFM");                    
                    if(listaSspRequisitoCertificadoSaludMental.size()>0){
                        for(SspRequisito itemSspRequisitoCFM : listaSspRequisitoCertificadoSaludMental){                            
                            SspRequisito itemReqCFM = new SspRequisito();
                            itemReqCFM = itemSspRequisitoCFM;                          
                            itemReqCFM.setActivo(JsfUtil.FALSE);
                            itemReqCFM.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemReqCFM.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspRequisitoFacade.edit(itemReqCFM);                             
                        }
                    }
                    
                    List<SspArchivo> listaSspArchivoCertificadoSaludMental = ejbSspArchivoFacade.listarArchivos_By_RegistroId_By_Requisitoid(registro.getId(), "TP_REQ_CFM");                              
                    if(listaSspArchivoCertificadoSaludMental.size()>0){                        
                        for(SspArchivo itemArchivoCFM : listaSspArchivoCertificadoSaludMental){                            
                            SspArchivo itemArchivoOld02 = new SspArchivo();
                            itemArchivoOld02 = itemArchivoCFM;
                            itemArchivoOld02.getRequisitoId().setActivo(JsfUtil.FALSE);
                            itemArchivoOld02.getRequisitoId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemArchivoOld02.getRequisitoId().setAudNumIp(JsfUtil.getIpAddress());                            
                            itemArchivoOld02.setActivo(JsfUtil.FALSE);
                            itemArchivoOld02.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemArchivoOld02.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspArchivoFacade.edit(itemArchivoOld02);                             
                        }                        
                    }
                        
                    //CREA UN REGISTRO NUEVO - CFM
                    SspRequisito requisitoCFM = new SspRequisito();
                    requisitoCFM.setId(null);
                    requisitoCFM.setActivo(JsfUtil.TRUE);
                    requisitoCFM.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisitoCFM.setAudNumIp(JsfUtil.getIpAddress());
                    requisitoCFM.setFecha(new Date());                    
                    requisitoCFM.setRegistroId(registro);
                    requisitoCFM.setSspArchivoList(new ArrayList());
                    requisitoCFM.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CFM"));
                    requisitoCFM.setValorRequisito(null);
                    requisitoCFM = (SspRequisito) JsfUtil.entidadMayusculas(requisitoCFM, "");
                    ejbSspRequisitoFacade.create(requisitoCFM);

                    SspArchivo archivooCFM = new SspArchivo();
                    archivooCFM.setId(null);
                    archivooCFM.setActivo(JsfUtil.TRUE);
                    archivooCFM.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivooCFM.setAudNumIp(JsfUtil.getIpAddress());                    
                    archivooCFM.setPathupload("Documentos_pathUpload_ASSGssp_CERTSALUDFM");
                    archivooCFM.setNombre(fileNameArchivoCERTFISMENTAL);
                    archivooCFM.setRequisitoId(requisitoCFM);
                    archivooCFM.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    archivooCFM = (SspArchivo) JsfUtil.entidadMayusculas(archivooCFM, "");                    
                    ejbSspArchivoFacade.create(archivooCFM);
                    
                    if(certFiscoMntalByte != null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_CERTSALUDFM").getValor() + fileNameArchivoCERTFISMENTAL.toUpperCase()), certFiscoMntalByte);
                    }
                    
                    //================================================================================================================================
                    //===============================       REQUISITO COPIA CONTRATO DE TRABAJO       =============================================
                    //================================================================================================================================
                    
                    String fileNameArchivoCOPYCONTRATOTRABAJO = "";
                    if(nomArchivoCopyContrato_anterior == null){//Editado
                        fileNameArchivoCOPYCONTRATOTRABAJO = nomArchivoCopyContrato;
                        fileNameArchivoCOPYCONTRATOTRABAJO = "ASSGssp_COPY_CT" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_COPY_CT").toString() + fileNameArchivoCOPYCONTRATOTRABAJO.substring(fileNameArchivoCOPYCONTRATOTRABAJO.lastIndexOf('.'), fileNameArchivoCOPYCONTRATOTRABAJO.length());
                        fileNameArchivoCOPYCONTRATOTRABAJO = fileNameArchivoCOPYCONTRATOTRABAJO.toUpperCase();
                    }else{    
                        fileNameArchivoCOPYCONTRATOTRABAJO = nomArchivoCopyContrato;
                        fileNameArchivoCOPYCONTRATOTRABAJO = fileNameArchivoCOPYCONTRATOTRABAJO.toUpperCase();
                    }
                            
                    List<SspRequisito> listaSspRequisitoCOPYCONTRATO = ejbSspRequisitoFacade.listarSspRequisito_By_RegistroId_TipoRequisitoId(registro.getId(), "TP_REQ_CT");                    
                    if(listaSspRequisitoCOPYCONTRATO.size()>0){
                        for(SspRequisito itemSspRequisitoCOPYCONTRATO : listaSspRequisitoCOPYCONTRATO){                            
                            SspRequisito itemReqCOPYCONTRATO = new SspRequisito();
                            itemReqCOPYCONTRATO = itemSspRequisitoCOPYCONTRATO;                          
                            itemReqCOPYCONTRATO.setActivo(JsfUtil.FALSE);
                            itemReqCOPYCONTRATO.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemReqCOPYCONTRATO.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspRequisitoFacade.edit(itemReqCOPYCONTRATO);                             
                        }
                    }
                    
                    List<SspArchivo> listaSspArchivoCOPYCONTRATO = ejbSspArchivoFacade.listarArchivos_By_RegistroId_By_Requisitoid(registro.getId(), "TP_REQ_CT");                              
                    if(listaSspArchivoCOPYCONTRATO.size()>0){                        
                        for(SspArchivo itemArchivoCOPYCONTRATO : listaSspArchivoCOPYCONTRATO){                            
                            SspArchivo itemCOPYCONTRATO = new SspArchivo();
                            itemCOPYCONTRATO = itemArchivoCOPYCONTRATO;
                            itemCOPYCONTRATO.getRequisitoId().setActivo(JsfUtil.FALSE);
                            itemCOPYCONTRATO.getRequisitoId().setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemCOPYCONTRATO.getRequisitoId().setAudNumIp(JsfUtil.getIpAddress());                            
                            itemCOPYCONTRATO.setActivo(JsfUtil.FALSE);
                            itemCOPYCONTRATO.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            itemCOPYCONTRATO.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSspArchivoFacade.edit(itemCOPYCONTRATO);                             
                        }                        
                    }
                        
                    //CREA UN REGISTRO NUEVO - CFM
                    
                    SspRequisito requisito3 = new SspRequisito();                    
                    requisito3.setActivo(JsfUtil.TRUE);
                    requisito3.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    requisito3.setAudNumIp(JsfUtil.getIpAddress());
                    requisito3.setFecha(new Date());
                    requisito3.setId(null);
                    requisito3.setRegistroId(registro);
                    requisito3.setSspArchivoList(new ArrayList());
                    requisito3.setTipoRequisitoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_REQ_CT"));
                    requisito3.setValorRequisito(null);
                    requisito3 = (SspRequisito) JsfUtil.entidadMayusculas(requisito3, "");
                    ejbSspRequisitoFacade.create(requisito3);

                    SspArchivo archivo3 = new SspArchivo();
                    archivo3.setActivo(JsfUtil.TRUE);
                    archivo3.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    archivo3.setAudNumIp(JsfUtil.getIpAddress());
                    archivo3.setId(null);
                    archivo3.setPathupload("Documentos_pathUpload_ASSGssp_COPYCT");
                    archivo3.setNombre(fileNameArchivoCOPYCONTRATOTRABAJO);
                    archivo3.setRequisitoId(requisito3);
                    archivo3.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_PDF"));
                    archivo3 = (SspArchivo) JsfUtil.entidadMayusculas(archivo3, "");                    
                    ejbSspArchivoFacade.create(archivo3);
                        
                    
                    if(copyContratoByte != null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ASSGssp_COPYCT").getValor() + fileNameArchivoCOPYCONTRATOTRABAJO.toUpperCase()), copyContratoByte);
                    }
                                        
                    
                //===========================================================================                
                adicionaEvento(registro, "Actualiza solicitud de autorización para SISPA.");                        
                //===========================================================================
                
                JsfUtil.mensaje("Se registró la solicitud con Código: " + registro.getId());
                
                return  gsspSolicitudAutorizacionController.prepareList();
                
            } catch (Exception e) {
                System.err.println("Usuario: " + JsfUtil.getLoggedUser().getLogin());
                e.printStackTrace();
                JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
            }
            
        }
        
        return null;
        
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

        if (reg.getSspRegistroEventoList() == null) {
            reg.setSspRegistroEventoList(new ArrayList());
        }
        reg.getSspRegistroEventoList().add(evento);
    }
    
    //====================================
    //===========  METODOS GET Y SET
    //====================================

    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public SspRegistro getRegistro() {
        return registro;
    }

    public void setRegistro(SspRegistro registro) {
        this.registro = registro;
    }

    public SspRegistro getRegistroN2() {
        return registroN2;
    }

    public void setRegistroN2(SspRegistro registroN2) {
        this.registroN2 = registroN2;
    }

    public SspRepresentanteRegistro getRepresentanteRegistro() {
        return representanteRegistro;
    }

    public void setRepresentanteRegistro(SspRepresentanteRegistro representanteRegistro) {
        this.representanteRegistro = representanteRegistro;
    }

    public SbPartidaSunarp getPartidaSunarpRL() {
        return partidaSunarpRL;
    }

    public void setPartidaSunarpRL(SbPartidaSunarp partidaSunarpRL) {
        this.partidaSunarpRL = partidaSunarpRL;
    }

    public SbAsientoSunarp getAsientoSunarpRL() {
        return asientoSunarpRL;
    }

    public void setAsientoSunarpRL(SbAsientoSunarp asientoSunarpRL) {
        this.asientoSunarpRL = asientoSunarpRL;
    }

    public SbAsientoPersona getAsientoPersonaRL() {
        return asientoPersonaRL;
    }

    public void setAsientoPersonaRL(SbAsientoPersona asientoPersonaRL) {
        this.asientoPersonaRL = asientoPersonaRL;
    }

    public SbRelacionPersonaGt getRelacionPersonaGtRL() {
        return relacionPersonaGtRL;
    }

    public void setRelacionPersonaGtRL(SbRelacionPersonaGt relacionPersonaGtRL) {
        this.relacionPersonaGtRL = relacionPersonaGtRL;
    }

    public SspRepresentantePublico getRepresentantePublico() {
        return representantePublico;
    }

    public void setRepresentantePublico(SspRepresentantePublico representantePublico) {
        this.representantePublico = representantePublico;
    }

    public SspRegistroEvento getRegistroEvento() {
        return registroEvento;
    }

    public void setRegistroEvento(SspRegistroEvento registroEvento) {
        this.registroEvento = registroEvento;
    }

    public SspServicio getServicio() {
        return servicio;
    }

    public void setServicio(SspServicio servicio) {
        this.servicio = servicio;
    }

    public SspForma getForma() {
        return forma;
    }

    public void setForma(SspForma forma) {
        this.forma = forma;
    }

    public String getTipoAdministradoUsuario() {
        return tipoAdministradoUsuario;
    }

    public void setTipoAdministradoUsuario(String tipoAdministradoUsuario) {
        this.tipoAdministradoUsuario = tipoAdministradoUsuario;
    }

    public boolean isAdministradoConRUC() {
        return administradoConRUC;
    }

    public void setAdministradoConRUC(boolean administradoConRUC) {
        this.administradoConRUC = administradoConRUC;
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

    public String getAdministNombres() {
        return administNombres;
    }

    public void setAdministNombres(String administNombres) {
        this.administNombres = administNombres;
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
    
    

    public String getAdministTipDoc() {
        return administTipDoc;
    }

    public void setAdministTipDoc(String administTipDoc) {
        this.administTipDoc = administTipDoc;
    }

    public String getAdministNumDoc() {
        return administNumDoc;
    }

    public void setAdministNumDoc(String administNumDoc) {
        this.administNumDoc = administNumDoc;
    }

    public List<TipoBaseGt> getRegTipoSeguridadList() {
        return regTipoSeguridadList;
    }

    public void setRegTipoSeguridadList(List<TipoBaseGt> regTipoSeguridadList) {
        this.regTipoSeguridadList = regTipoSeguridadList;
    }

    public TipoBaseGt getRegTipoSeguridadSelected() {
        return regTipoSeguridadSelected;
    }

    public void setRegTipoSeguridadSelected(TipoBaseGt regTipoSeguridadSelected) {
        this.regTipoSeguridadSelected = regTipoSeguridadSelected;
    }

    public TipoBaseGt getRegTipoProcesoId() {
        return regTipoProcesoId;
    }

    public void setRegTipoProcesoId(TipoBaseGt regTipoProcesoId) {
        this.regTipoProcesoId = regTipoProcesoId;
    }

    public List<TipoBaseGt> getRegFormaTipoSeguridadList() {
        return regFormaTipoSeguridadList;
    }

    public void setRegFormaTipoSeguridadList(List<TipoBaseGt> regFormaTipoSeguridadList) {
        this.regFormaTipoSeguridadList = regFormaTipoSeguridadList;
    }

    public TipoBaseGt getRegFormaTipoSeguridadSelected() {
        return regFormaTipoSeguridadSelected;
    }

    public void setRegFormaTipoSeguridadSelected(TipoBaseGt regFormaTipoSeguridadSelected) {
        this.regFormaTipoSeguridadSelected = regFormaTipoSeguridadSelected;
    }

    public List<TipoSeguridad> getRegServicioPrestadoList() {
        return regServicioPrestadoList;
    }

    public void setRegServicioPrestadoList(List<TipoSeguridad> regServicioPrestadoList) {
        this.regServicioPrestadoList = regServicioPrestadoList;
    }

    public TipoSeguridad getRegServicioPrestadoSelected() {
        return regServicioPrestadoSelected;
    }

    public void setRegServicioPrestadoSelected(TipoSeguridad regServicioPrestadoSelected) {
        this.regServicioPrestadoSelected = regServicioPrestadoSelected;
    }

    public List<SbRecibos> getRegListDatoRecibos() {
        return regListDatoRecibos;
    }

    public void setRegListDatoRecibos(List<SbRecibos> regListDatoRecibos) {
        this.regListDatoRecibos = regListDatoRecibos;
    }

    public SbRecibos getRegDatoRecibos() {
        return regDatoRecibos;
    }

    public void setRegDatoRecibos(SbRecibos regDatoRecibos) {
        this.regDatoRecibos = regDatoRecibos;
    }

    public String getRegNroComprobante() {
        return regNroComprobante;
    }

    public void setRegNroComprobante(String regNroComprobante) {
        this.regNroComprobante = regNroComprobante;
    }

    public boolean isHabilitarAdjuntarFoto() {
        return habilitarAdjuntarFoto;
    }

    public void setHabilitarAdjuntarFoto(boolean habilitarAdjuntarFoto) {
        this.habilitarAdjuntarFoto = habilitarAdjuntarFoto;
    }

    public UploadedFile getFileFOTO() {
        return fileFOTO;
    }

    public void setFileFOTO(UploadedFile fileFOTO) {
        this.fileFOTO = fileFOTO;
    }

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public StreamedContent getArchivoFOTO() {
        try {
            if (fotoByte != null) {
                InputStream is = new ByteArrayInputStream(fotoByte);
                archivoFOTO = new DefaultStreamedContent(is, "image/jpeg", "foto");
            } else if (fileFOTO != null) {
                fotoByte = IOUtils.toByteArray(fileFOTO.getInputstream());
                archivoFOTO = new DefaultStreamedContent(fileFOTO.getInputstream(), "image/jpeg");
            } else {
                return new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg"), "image/jpeg");
            }
        } catch (Exception ex) {
            fotoByte = null;
            archivoFOTO = null;
        }

        return archivoFOTO;
    }

    public void setArchivoFOTO(StreamedContent archivoFOTO) {
        this.archivoFOTO = archivoFOTO;
    }

    public String getNomArchivoFOTO() {
        return nomArchivoFOTO;
    }

    public void setNomArchivoFOTO(String nomArchivoFOTO) {
        this.nomArchivoFOTO = nomArchivoFOTO;
    }

    public boolean isHabilitarAdjuntarCertFiscoMntal() {
        return habilitarAdjuntarCertFiscoMntal;
    }

    public void setHabilitarAdjuntarCertFiscoMntal(boolean habilitarAdjuntarCertFiscoMntal) {
        this.habilitarAdjuntarCertFiscoMntal = habilitarAdjuntarCertFiscoMntal;
    }

    public UploadedFile getFileCertFiscoMntal() {
        return fileCertFiscoMntal;
    }

    public void setFileCertFiscoMntal(UploadedFile fileCertFiscoMntal) {
        this.fileCertFiscoMntal = fileCertFiscoMntal;
    }

    public byte[] getCertFiscoMntalByte() {
        return certFiscoMntalByte;
    }

    public void setCertFiscoMntalByte(byte[] certFiscoMntalByte) {
        this.certFiscoMntalByte = certFiscoMntalByte;
    }

    public StreamedContent getArchivoCertFiscoMntal() {
        return archivoCertFiscoMntal;
    }

    public void setArchivoCertFiscoMntal(StreamedContent archivoCertFiscoMntal) {
        this.archivoCertFiscoMntal = archivoCertFiscoMntal;
    }

    public String getNomArchivoCertFiscoMntal() {
        return nomArchivoCertFiscoMntal;
    }

    public void setNomArchivoCertFiscoMntal(String nomArchivoCertFiscoMntal) {
        this.nomArchivoCertFiscoMntal = nomArchivoCertFiscoMntal;
    }

    public boolean isHabilitarAdjuntarCopyContrato() {
        return habilitarAdjuntarCopyContrato;
    }

    public void setHabilitarAdjuntarCopyContrato(boolean habilitarAdjuntarCopyContrato) {
        this.habilitarAdjuntarCopyContrato = habilitarAdjuntarCopyContrato;
    }

    public UploadedFile getFileCopyContrato() {
        return fileCopyContrato;
    }

    public void setFileCopyContrato(UploadedFile fileCopyContrato) {
        this.fileCopyContrato = fileCopyContrato;
    }

    public byte[] getCopyContratoByte() {
        return copyContratoByte;
    }

    public void setCopyContratoByte(byte[] copyContratoByte) {
        this.copyContratoByte = copyContratoByte;
    }

    public StreamedContent getArchivoCopyContrato() {
        return archivoCopyContrato;
    }

    public void setArchivoCopyContrato(StreamedContent archivoCopyContrato) {
        this.archivoCopyContrato = archivoCopyContrato;
    }

    public String getNomArchivoCopyContrato() {
        return nomArchivoCopyContrato;
    }

    public void setNomArchivoCopyContrato(String nomArchivoCopyContrato) {
        this.nomArchivoCopyContrato = nomArchivoCopyContrato;
    }
    
    public List<TipoBaseGt> getRegPrioridadLPSList() {
        return regPrioridadLPSList;
    }

    public void setRegPrioridadLPSList(List<TipoBaseGt> regPrioridadLPSList) {
        this.regPrioridadLPSList = regPrioridadLPSList;
    }

    public TipoBaseGt getRegPrioridadLPSSelected() {
        return regPrioridadLPSSelected;
    }

    public void setRegPrioridadLPSSelected(TipoBaseGt regPrioridadLPSSelected) {
        this.regPrioridadLPSSelected = regPrioridadLPSSelected;
    }

    public List<TipoBaseGt> getRegTipoMedioContactoLPSList() {
        return regTipoMedioContactoLPSList;
    }

    public void setRegTipoMedioContactoLPSList(List<TipoBaseGt> regTipoMedioContactoLPSList) {
        this.regTipoMedioContactoLPSList = regTipoMedioContactoLPSList;
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

    public String getxNroSolicitiud() {
        return xNroSolicitiud;
    }

    public void setxNroSolicitiud(String xNroSolicitiud) {
        this.xNroSolicitiud = xNroSolicitiud;
    }

    public String[] getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(String[] selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public Long getContRegCheckDDJJ() {
        return contRegCheckDDJJ;
    }

    public void setContRegCheckDDJJ(Long contRegCheckDDJJ) {
        this.contRegCheckDDJJ = contRegCheckDDJJ;
    }

    public boolean isTermCondicionAceptaDDJJ() {
        return termCondicionAceptaDDJJ;
    }

    public void setTermCondicionAceptaDDJJ(boolean termCondicionAceptaDDJJ) {
        this.termCondicionAceptaDDJJ = termCondicionAceptaDDJJ;
    }

    public String getNomArchivoCertFiscoMntal_anterior() {
        return nomArchivoCertFiscoMntal_anterior;
    }

    public void setNomArchivoCertFiscoMntal_anterior(String nomArchivoCertFiscoMntal_anterior) {
        this.nomArchivoCertFiscoMntal_anterior = nomArchivoCertFiscoMntal_anterior;
    }

    public String getNomArchivoCopyContrato_anterior() {
        return nomArchivoCopyContrato_anterior;
    }

    public void setNomArchivoCopyContrato_anterior(String nomArchivoCopyContrato_anterior) {
        this.nomArchivoCopyContrato_anterior = nomArchivoCopyContrato_anterior;
    }

    public String getNomArchivoFOTO_anterior() {
        return nomArchivoFOTO_anterior;
    }

    public void setNomArchivoFOTO_anterior(String nomArchivoFOTO_anterior) {
        this.nomArchivoFOTO_anterior = nomArchivoFOTO_anterior;
    }

    public List<SspRegistroEvento> getLstObservaciones() {
        return lstObservaciones;
    }

    public void setLstObservaciones(List<SspRegistroEvento> lstObservaciones) {
        this.lstObservaciones = lstObservaciones;
    }
    
    
    
}
