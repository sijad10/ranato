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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.SbAsientoPersona;
import pe.gob.sucamec.bdintegrado.data.SbAsientoSunarp;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbPaisGt;
import pe.gob.sucamec.bdintegrado.data.SbPartidaSunarp;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspCertifProvee;
import pe.gob.sucamec.bdintegrado.data.SspContacto;
import pe.gob.sucamec.bdintegrado.data.SspPoliza;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SspRegistroEvento;
import pe.gob.sucamec.bdintegrado.data.SspRepresentanteRegistro;
import pe.gob.sucamec.bdintegrado.data.SspVehiculoCertificacion;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.UploadFilesController;
import pe.gob.sucamec.sel.gssp.data.PersonaDetalle;
import pe.gob.sucamec.sistemabase.data.SbPais;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.PideMigraciones;
import wspide.PideMigraciones_Service;

/**
 *
 * @author lbartolo
 */
@Named(value = "gsspSolicitudCertificacionCVBController")
@SessionScoped
public class GsspSolicitudCertificacionCVBController implements Serializable{
   
    @Inject
    UploadFilesController uploadFilesController;
    
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
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRepresentanteRegistroFacade ejbSspRepresentanteRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPartidaSunarpFacade ejbSbPartidaSunarpFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbAsientoSunarpFacade ejbSbAsientoSunarpFacade;    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbAsientoPersonaFacade ejbSbAsientoPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPaisFacadeGt ejbSbPaisFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt ejbSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspContactoFacade ejbSspContactoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspVehiculoCertificacionFacade ejbSspVehiculoCertificacionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCertifProveeFacade ejbSspCertifProveeFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspPolizaFacade ejbSspPolizaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroEventoFacade ejbSspRegistroEventoFacade;
    
    
    private Date fechaMinimaCalendario_CertProv;
    private Date fechaMinimaCalendario_Poliza;
    
    private EstadoCrud estado;
    private boolean formHabilitarSoloLectura;
    private List<TipoBaseGt> formTipoRegistroLista;
    private TipoBaseGt formTipoRegistroSelected;
    private String tipoRegistroCodProg;
    
    private SspRegistro regSspRegistroSCVB;  
    private SspRepresentanteRegistro regSspRepresentanteRegistroRL;
    private SbDireccionGt regSbDireccionGtRL;
    private SbAsientoPersona regSbAsientoPersonaRL;
    private SbAsientoSunarp regSbAsientoSunarpRL;
    private SbPartidaSunarp regSbPartidaSunarpRL;
    private SspVehiculoCertificacion regSspVehiculoCertificacionVC;
    private SbAsientoSunarp regSbAsientoSunarpVC;
    private SbPartidaSunarp regSbPartidaSunarpVC; 
    private SspCertifProvee regSspCertifProveeVCP;
    private SspPoliza regSspPolizaVPoliza;
    
    private boolean blnProcesoRegSCVB = false;
    private Integer totalProcesadosSCVB = 0;
    private Integer actualProcesadoSCVB = 0;
    private Integer progressSCVB = 0;
    
    //=================== ADMINISTRADO =========================
    private SbPersonaGt regAdminist;
    private SbPersonaGt regUserAdminist;
    private String administRazonSocial;
    private String administRUC;
    
    //=================== RESOLUCION DE SERVICIO DE TRANSPORTE Y CUSTODIA DE DINERO Y VALORES (APROBADO) =========================
    private boolean renderMostrarFormSolicCertVehBlindados;
    private SspRegistro registroResolucionServTransYCustDineroValores;    
    private HashMap emptyModel_vistaDataServTranspCustodiaDineroYValores = new HashMap<>();
    
    //=================== VARIABLE PARA LOS LISTBOX =========================
    
    private SbPersonaGt regDatoPersonaByNumDocRL;
    
    private List<PersonaDetalle> personaDetalleListado;
    private String personaDetalleSelectedString;
    
    private List<TipoBaseGt> cboDlgTipoDocLista;
    private TipoBaseGt cboDlgTipoDocSelected;
    private String txtDlgNroDoc;
    private String txtDlgNombre;
    private String txtDlgApePat;
    private String txtDlgApeMat;
    private Date txtDlgAFechaNac;
    
    //---------------------------------------------
    //Variables Para Representante Legal
    //---------------------------------------------
    
    private List<TipoBaseGt> cboZonaRegistralRLLista;
    private TipoBaseGt cboZonaRegistralRLSelected;
    
    private List<TipoBaseGt> cboOficinaRegistralRLLista;
    private TipoBaseGt cboOficinaRegistralRLSelected;
    
    private List<TipoBaseGt> cboTipoUbicacionRLLista;
    private TipoBaseGt cboTipoUbicacionRLSelected;
    
    private List<SbDistritoGt> cboDistritoRLLista;
    private SbDistritoGt cboDistritoRLSelected;
    
    private String txtRLPartRegistral;
    private String txtRLAsientoRegistral;
    private String txtRLDomicilioLegal;
    
    private boolean existeAsientoSunarpRL;
    
    //---------------------------------------------
    //Variables Para Contactos
    //---------------------------------------------
    
    private ArrayList<SspContacto> contactoLista;
    private SspContacto contactoSelected;
    
    private List<TipoBaseGt> cboDlgPrioridadLista;
    private TipoBaseGt cboDlgPrioridadSelected;
    
    private List<TipoBaseGt> cboDlgMedioContactoLista;
    private TipoBaseGt cboDlgMedioContactoSelected;
    
    private boolean dlgFragmentContacto;
    private String dlgLabelContacto;
    private String dlgtextoContacto;
    
    //---------------------------------------------
    //Variables Para Vehiculos
    //---------------------------------------------
    
    private SspVehiculoCertificacion sspVehiculoCertificacion;
    
    private String vehCategoria;
    private String vehCarroceria;
    private String vehPlaca;
    private String vehSerie;
    private String vehMarca;
    private String vehModelo;
    
    private SbAsientoSunarp vehSbAsientoSunarp;
    private String txtVehPartRegistral;
    private String txtVehAsientoRegistral;
    
    private UploadedFile vehPartRegUploadedFile;
    private byte[] vehPartRegByte;
    private StreamedContent vehPartRegArchivo;
    private String vehPartRegNomArchivo;
    private String vehPartRegNomArchivoAnterior;
    
    private Short vehArrendado;
    private boolean renderArrendatario;
    
    private UploadedFile vehContraArrendaUploadedFile;
    private byte[] vehContraArrendaRegByte;
    private StreamedContent vehContraArrendaArchivo;
    private String vehContraArrendaNomArchivo;
    private String vehContraArrendaNomArchivoAnterior;
    
    private List<TipoBaseGt> cboVehZonaRegistralRLLista;
    private TipoBaseGt cboVehZonaRegistralRLSelected;
    
    private List<TipoBaseGt> cboVehOficinaRegistralRLLista;
    private TipoBaseGt cboVehOficinaRegistralRLSelected;
    
    private List<SbDistritoGt> cboVehSbDistritoGtLista;
    private SbDistritoGt cboVehSbDistritoGtSelected;
    
    //---------------------------------------------
    //Variables Para Certificados del Proveedor
    //---------------------------------------------
    
    private List<SbPaisGt> cboCertProvPaisLista;
    private SbPaisGt cboCertProvPaisSelected;
    private String cboCertProvPaisSelectedStringID;
    
    private String txtCertProvNroDocCertificadora;
    private String txtCertProvNomEmpCertificadora;
    private Date txtCertProvFechaIni;
    private Date txtCertProvFechaFin;
    private String txtCertNumeroCert;
    
    private UploadedFile vehCertProvUploadedFile;
    private byte[] vehCertProvRegByte;
    private StreamedContent vehCertProvArchivo;
    private String vehCertProvNomArchivo;
    private String vehCertProvNomArchivoAnterior;
    
    //---------------------------------------------
    //Variables Para Poliza de Seguro
    //---------------------------------------------
    
    private String txtPolizaNumRUCEmpAseguradora;
    private String txtPolizaNombreEmpAseguradora;
    private String txtPolizaNumPoliza;
    private Date txtPolizaFechaDesde;
    private Date txtPolizaFechaHasta;
    
    private List<TipoBaseGt> cboPolizaTipoMonedaLista;
    private TipoBaseGt cboPolizaTipoMonedaSelected;
    
    private BigDecimal txtPolizaMontoMaximo;
    
    private UploadedFile vehPolizaUploadedFile;
    private byte[] vehPolizaRegByte;
    private StreamedContent vehPolizaArchivo;
    private String vehPolizaNomArchivo;
    private String vehPolizaNomArchivoAnterior;
    
    private String vehPolizaBienesCustodia;
    
    
    //==========================================================
    //============ METODOS PARA EL FORMULARIO  =================
    //==========================================================
    
    public String mostrarFormCrearSolicitudCertificacionVB() {
        
        fnLimpiarFormulario();
        estado = EstadoCrud.CREARCERTVEHBLIND;//NUEVO
        renderMostrarFormSolicCertVehBlindados = false;
        
        //========    Administrado - INI   =========
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());        
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();
        //========    Administrado - FIN   =========        
        
        fnLlenarLisbox(regAdminist);
        
        return "/aplicacion/gssp/gsspCertificacion/certificado_cvb/CreateSolicCertificadoVB";

    }
    
    public void fnLimpiarFormulario() {
        
        formHabilitarSoloLectura = false;
        fechaMinimaCalendario_CertProv = null;
        fechaMinimaCalendario_Poliza = null;
        emptyModel_vistaDataServTranspCustodiaDineroYValores = new HashMap<>();
        
        regSspRegistroSCVB = new SspRegistro();
        regSspRepresentanteRegistroRL = new SspRepresentanteRegistro();
        regSbDireccionGtRL = new SbDireccionGt();
        regSbAsientoPersonaRL = new SbAsientoPersona();
        regSbAsientoSunarpRL = new SbAsientoSunarp();
        regSbPartidaSunarpRL = new SbPartidaSunarp();      
        regSspVehiculoCertificacionVC = new SspVehiculoCertificacion();
        regSbAsientoSunarpVC = new SbAsientoSunarp();
        regSbPartidaSunarpVC = new SbPartidaSunarp();
        regSspCertifProveeVCP = new SspCertifProvee();
        regSspPolizaVPoliza = new SspPoliza();
        
        blnProcesoRegSCVB = false;
        totalProcesadosSCVB = 0;
        actualProcesadoSCVB = 0;
        progressSCVB = 0;
                
        personaDetalleListado = new ArrayList<PersonaDetalle>();
        personaDetalleSelectedString = null;
        
        //----------------------------------------------------
        //----- LIMPIAR VARIABLES PARA REPRESENTANTE LEGAL
        //----------------------------------------------------
        //Dialog Representante Legal
        
        existeAsientoSunarpRL = false;
        
        cboDlgTipoDocLista = new ArrayList<TipoBaseGt>();
        cboDlgTipoDocSelected = null;
        
        cboZonaRegistralRLLista = new ArrayList<TipoBaseGt>();
        cboZonaRegistralRLSelected = null;
        
        cboOficinaRegistralRLLista = new ArrayList<TipoBaseGt>();
        cboOficinaRegistralRLSelected = null;
        
        cboTipoUbicacionRLLista = new ArrayList<TipoBaseGt>();
        cboTipoUbicacionRLSelected = null;
        
        cboDistritoRLLista = new ArrayList<SbDistritoGt>();
        cboDistritoRLSelected = null;
        
        txtRLPartRegistral = null;
        txtRLAsientoRegistral = null;
        txtRLDomicilioLegal = null;
        
        //----------------------------------------------------
        //----- LIMPIAR VARIABLES PARA CONTACTOS
        //----------------------------------------------------
        
        contactoLista = new ArrayList<SspContacto>();
        contactoSelected = null;
        
        cboDlgPrioridadLista = new ArrayList<TipoBaseGt>();
        cboDlgPrioridadSelected = null;
        
        cboDlgMedioContactoLista = new ArrayList<TipoBaseGt>();
        cboDlgMedioContactoSelected = null;
        
        dlgLabelContacto = null;
        dlgtextoContacto = null;
        dlgFragmentContacto = false;
        
        //----------------------------------------------------
        //----- LIMPIAR VARIABLES PARA VEHICULOS
        //----------------------------------------------------
        
        sspVehiculoCertificacion = null;
        vehCategoria = null;
        vehCarroceria = null;
        vehPlaca = null;
        vehSerie = null;
        vehMarca = null;
        vehModelo = null;   
        
        vehArrendado = null;
        renderArrendatario = false;
        
        txtVehPartRegistral = null;
        txtVehAsientoRegistral = null;
        cboVehZonaRegistralRLLista = new ArrayList<TipoBaseGt>();
        cboVehZonaRegistralRLSelected = null;
        cboVehOficinaRegistralRLLista = new ArrayList<TipoBaseGt>();
        cboVehOficinaRegistralRLSelected = null;
        cboVehSbDistritoGtLista = new ArrayList<SbDistritoGt>();
        cboVehSbDistritoGtSelected = null;
        
        vehSbAsientoSunarp = null;
        vehPartRegUploadedFile = null;
        vehPartRegByte = null;
        vehPartRegArchivo = null;
        vehPartRegNomArchivo = null;
        vehPartRegNomArchivoAnterior = null;
        
        vehContraArrendaUploadedFile = null;
        vehContraArrendaRegByte = null;
        vehContraArrendaArchivo = null;
        vehContraArrendaNomArchivo = null;
        vehPartRegNomArchivo = null;
        vehContraArrendaNomArchivoAnterior = null;
        
        //----------------------------------------------------
        //----- LIMPIAR VARIABLES PARA CERTIFICADOS DE PROVEEDOR
        //----------------------------------------------------
        
        cboCertProvPaisLista = new ArrayList<SbPaisGt>();
        cboCertProvPaisSelected = null;
        cboCertProvPaisSelectedStringID = null;
        
        txtCertProvNroDocCertificadora = null;
        txtCertProvNomEmpCertificadora = null;
        txtCertProvFechaIni = null;
        txtCertProvFechaFin = null;
        txtCertNumeroCert = null;
        
        vehCertProvUploadedFile = null;
        vehCertProvRegByte = null;
        vehCertProvArchivo = null;
        vehCertProvNomArchivo = null;
        vehCertProvNomArchivoAnterior = null;
        
        
        //----------------------------------------------------
        //----- LIMPIAR VARIABLES PARA POLIZA DE SEGUROS
        //----------------------------------------------------
        
        txtPolizaNumRUCEmpAseguradora = null;
        txtPolizaNombreEmpAseguradora = null;
        txtPolizaNumPoliza = null;
        txtPolizaFechaDesde = null;
        txtPolizaFechaHasta = null;
        
        cboPolizaTipoMonedaLista = new ArrayList<TipoBaseGt>();
        cboPolizaTipoMonedaSelected = null;
        
        txtPolizaMontoMaximo = null;
        
        vehPolizaUploadedFile = null;
        vehPolizaRegByte = null;
        vehPolizaArchivo = null;
        vehPolizaNomArchivo = null;
        vehPolizaNomArchivoAnterior = null;
        
        vehPolizaBienesCustodia = null;
        
    
    }
    
    public void fnLlenarLisbox(SbPersonaGt administradoEmp) {
        
        cboDlgTipoDocLista = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_DOCID_DNI','TP_DOCID_CE' ");//Dialog Representante Legal
        cboZonaRegistralRLLista = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
        cboTipoUbicacionRLLista = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_VIA");
        cboDistritoRLLista = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
        cboDlgPrioridadLista = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_CONTAC_PRIN','TP_CONTAC_SEC'");
        cboDlgMedioContactoLista = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_MEDCO_COR','TP_MEDCO_FAX','TP_MEDCO_FIJ','TP_MEDCO_MOV'");
        
        List<SbPersonaGt> listDetallePersona = new ArrayList<>();
        listDetallePersona = ejbSbPersonaFacade.listarPersonaXIdRelacionPersonaUnionSspRegistro(regAdminist.getId(), null, ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL").getId());
        if (listDetallePersona != null) {
            PersonaDetalle detallePersonaTemp;
            for (SbPersonaGt PersonaDetalle : listDetallePersona) {
                detallePersonaTemp = new PersonaDetalle();
                detallePersonaTemp.setId(PersonaDetalle.getId());
                detallePersonaTemp.setNumDoc(PersonaDetalle.getNumDoc());
                detallePersonaTemp.setApePat(PersonaDetalle.getApePat());
                detallePersonaTemp.setApeMat(PersonaDetalle.getApeMat());
                detallePersonaTemp.setNombres(PersonaDetalle.getNombres());
                personaDetalleListado.add(detallePersonaTemp);
            }
            
        }
        
        //----------------------------------------------------
        //-----  LISTBOX PARA VEHICULOS
        //----------------------------------------------------
       
        cboVehZonaRegistralRLLista = ejbTipoBaseFacade.listarTipoBaseXCodProg("TP_ZR_SUNARP");
        cboVehSbDistritoGtLista = ejbSbDistritoFacadeGtFacade.obtenerUbigeo("");
        
        //----------------------------------------------------
        //----- LISTBOX PARA PAIS
        //----------------------------------------------------
        
        cboCertProvPaisLista = ejbSbPaisFacadeGt.listarPaises();
        
        //----------------------------------------------------
        //----- LISTBOX PARA MOENDA
        //----------------------------------------------------
        
        cboPolizaTipoMonedaLista = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_MNDA_SOL','TP_MNDA_DOL'");
    }
    
    
    public void mostrarEditarSolicitud(Map item) {
                
        try {
            
            fnLimpiarFormulario();        
            estado = EstadoCrud.EDITARCERTVEHBLIND;//EDITAR

            //========    Administrado - INI   =========
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
            regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());        
            administRazonSocial = regAdminist.getRznSocial();
            administRUC = regAdminist.getRuc();
            //========    Administrado - FIN   =========   

            fnLlenarLisbox(regAdminist); 

            regSspRegistroSCVB = ejbSspRegistroFacade.buscarRegistroById(Long.parseLong(item.get("ID").toString()));

            if (regSspRegistroSCVB.getTipoProId().getCodProg().equals("TP_GSSP_CERT_CVB")){            

                renderMostrarFormSolicCertVehBlindados = true;

                //Cargar Datos de la Resolucion de Solicitud de Custodia de Dinero y Valores (Id:3)    
                String RESOL_FORMATEADO = regSspRegistroSCVB.getReqResolucionId().getNumero();
                emptyModel_vistaDataServTranspCustodiaDineroYValores =  ejbSspResolucionFacade.BuscarResolucionAprobado_ServTransYCustDineroValores_ByNumResolucionFormateado(RESOL_FORMATEADO, administRUC);


                //REPRESENTANTE LEGAL
                regSspRepresentanteRegistroRL = ejbSspRepresentanteRegistroFacade.representRegFindByRegistroId(regSspRegistroSCVB.getId(), regSspRegistroSCVB.getEmpresaId().getId());
                regSbDireccionGtRL = ejbSbDireccionFacade.find(regSspRepresentanteRegistroRL.getDireccionRLId().getId());
                regSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.find(regSspRepresentanteRegistroRL.getAsientoRLId().getId());
                regSbAsientoSunarpRL = ejbSbAsientoSunarpFacade.find(regSbAsientoPersonaRL.getAsientoId().getId());
                regSbPartidaSunarpRL = ejbSbPartidaSunarpFacade.find(regSbAsientoSunarpRL.getPartidaId().getId());
                personaDetalleSelectedString = regSspRegistroSCVB.getRepresentanteId().getId().toString();
                mostrarDomicilioRepresentanteLegal();

                //LLENAR DATOS DE CONTACTOS
                List<SspContacto> editListaContactos = new ArrayList<SspContacto>();
                editListaContactos = ejbSspContactoFacade.listarContactoxIdRegistro(regSspRegistroSCVB.getId());            
                for (SspContacto itemContacto: editListaContactos) {
                    contactoLista.add(itemContacto);                 
                }

                //LLENAR DATOS DEL VEHICULO            
                regSspVehiculoCertificacionVC = ejbSspVehiculoCertificacionFacade.buscarSspVehiculoCertificacionByRegistroId(regSspRegistroSCVB.getId());
                regSbAsientoSunarpVC = ejbSbAsientoSunarpFacade.find(regSspVehiculoCertificacionVC.getAsientoSunarpId().getId());
                regSbPartidaSunarpVC = ejbSbPartidaSunarpFacade.find(regSbAsientoSunarpVC.getPartidaId().getId());            

                vehCategoria = regSspVehiculoCertificacionVC.getCategoria();
                vehCarroceria = regSspVehiculoCertificacionVC.getCarroceria();
                vehPlaca = regSspVehiculoCertificacionVC.getPlaca();
                vehSerie = regSspVehiculoCertificacionVC.getSerie();
                vehMarca = regSspVehiculoCertificacionVC.getMarca();
                vehModelo = regSspVehiculoCertificacionVC.getModelo();
                txtVehPartRegistral = regSspVehiculoCertificacionVC.getAsientoSunarpId().getPartidaId().getPartidaRegistral();
                txtVehAsientoRegistral = regSspVehiculoCertificacionVC.getAsientoSunarpId().getNroAsiento();
                cboVehZonaRegistralRLSelected = regSspVehiculoCertificacionVC.getAsientoSunarpId().getPartidaId().getZonaRegistral();
                fnListarOficinasRegistralesVehiculo();
                cboVehOficinaRegistralRLSelected = regSspVehiculoCertificacionVC.getAsientoSunarpId().getPartidaId().getOficinaRegistral();
                vehPartRegNomArchivo = regSspVehiculoCertificacionVC.getArchPartidaRegistral();
                cboVehSbDistritoGtSelected = regSspVehiculoCertificacionVC.getDistritoId();
                vehArrendado = regSspVehiculoCertificacionVC.getArrendado();
                renderArrendatario = (vehArrendado.equals(JsfUtil.TRUE) ? true : false);
                vehContraArrendaNomArchivo = regSspVehiculoCertificacionVC.getArchContratArrenda();


                //LLENAR CERTIFICADO DEL PROVEEDOR
                regSspCertifProveeVCP = ejbSspCertifProveeFacade.buscarSspCertifProveeByRegistroId(regSspRegistroSCVB.getId());
                cboCertProvPaisSelectedStringID = regSspCertifProveeVCP.getPaisId().getId().toString();
                txtCertProvNroDocCertificadora = regSspCertifProveeVCP.getDocEmpresaCert();
                txtCertProvNomEmpCertificadora = regSspCertifProveeVCP.getEmpresaCertCaract();
                txtCertProvFechaIni = regSspCertifProveeVCP.getFechaIni();
                txtCertProvFechaFin = regSspCertifProveeVCP.getFechaFin();
                txtCertNumeroCert = regSspCertifProveeVCP.getNroCertificado();
                vehCertProvNomArchivo = regSspCertifProveeVCP.getArchivoCertProv();

                //LLENAR POLIZA DE SEGURO
                regSspPolizaVPoliza = ejbSspPolizaFacade.buscarSspPolizaByRegistroId(regSspRegistroSCVB.getId());
                txtPolizaNumRUCEmpAseguradora = regSspPolizaVPoliza.getRuc();
                txtPolizaNombreEmpAseguradora = regSspPolizaVPoliza.getEmpresaAseguradora();
                txtPolizaNumPoliza = regSspPolizaVPoliza.getNroPoliza();
                txtPolizaFechaDesde = regSspPolizaVPoliza.getVigenciaIni();
                txtPolizaFechaHasta = regSspPolizaVPoliza.getVigenciaFin();
                cboPolizaTipoMonedaSelected = regSspPolizaVPoliza.getTipoMoneda();
                txtPolizaMontoMaximo = regSspPolizaVPoliza.getMontoMaximo();
                vehPolizaNomArchivo = regSspPolizaVPoliza.getArchivoPoliza();
                vehPolizaBienesCustodia = regSspPolizaVPoliza.getBienes();

                fechaMinimaCalendario_CertProv = txtCertProvFechaIni;
                fechaMinimaCalendario_Poliza = txtPolizaFechaDesde;
            
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("/sel/faces/aplicacion/gssp/gsspCertificacion/certificado_cvb/CreateSolicCertificadoVB.xhtml");
            }
        } catch (IOException ex) {
            System.out.println("ex->"+ex);
            ex.printStackTrace();
        }            
            
    } 
    
    
    public void mostrarVerSolicitud(Map item) {
        
        fnLimpiarFormulario();        
        estado = EstadoCrud.VERCERTVEHBLIND;//VER
        formHabilitarSoloLectura = true;
        
        //========    Administrado - INI   =========
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());        
        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();
        //========    Administrado - FIN   =========   
        
        fnLlenarLisbox(regAdminist); 
        
        regSspRegistroSCVB = ejbSspRegistroFacade.buscarRegistroById(Long.parseLong(item.get("ID").toString()));
        
        if (regSspRegistroSCVB.getTipoProId().getCodProg().equals("TP_GSSP_CERT_CVB")){            
            
            renderMostrarFormSolicCertVehBlindados = true;
            
            //Cargar Datos de la Resolucion de Solicitud de Custodia de Dinero y Valores (Id:3)    
            String RESOL_FORMATEADO = regSspRegistroSCVB.getReqResolucionId().getNumero();
            emptyModel_vistaDataServTranspCustodiaDineroYValores =  ejbSspResolucionFacade.BuscarResolucionAprobado_ServTransYCustDineroValores_ByNumResolucionFormateado(RESOL_FORMATEADO, administRUC);
            
            
            //REPRESENTANTE LEGAL
            regSspRepresentanteRegistroRL = ejbSspRepresentanteRegistroFacade.representRegFindByRegistroId(regSspRegistroSCVB.getId(), regSspRegistroSCVB.getEmpresaId().getId());
            regSbDireccionGtRL = ejbSbDireccionFacade.find(regSspRepresentanteRegistroRL.getDireccionRLId().getId());
            regSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.find(regSspRepresentanteRegistroRL.getAsientoRLId().getId());
            regSbAsientoSunarpRL = ejbSbAsientoSunarpFacade.find(regSbAsientoPersonaRL.getAsientoId().getId());
            regSbPartidaSunarpRL = ejbSbPartidaSunarpFacade.find(regSbAsientoSunarpRL.getPartidaId().getId());
            personaDetalleSelectedString = regSspRegistroSCVB.getRepresentanteId().getId().toString();
            mostrarDomicilioRepresentanteLegal();
            
            //LLENAR DATOS DE CONTACTOS
            List<SspContacto> editListaContactos = new ArrayList<SspContacto>();
            editListaContactos = ejbSspContactoFacade.listarContactoxIdRegistro(regSspRegistroSCVB.getId());            
            for (SspContacto itemContacto: editListaContactos) {
                contactoLista.add(itemContacto);                 
            }
            
            //LLENAR DATOS DEL VEHICULO            
            regSspVehiculoCertificacionVC = ejbSspVehiculoCertificacionFacade.buscarSspVehiculoCertificacionByRegistroId(regSspRegistroSCVB.getId());
            regSbAsientoSunarpVC = ejbSbAsientoSunarpFacade.find(regSspVehiculoCertificacionVC.getAsientoSunarpId().getId());
            regSbPartidaSunarpVC = ejbSbPartidaSunarpFacade.find(regSbAsientoSunarpVC.getPartidaId().getId());            
            
            vehCategoria = regSspVehiculoCertificacionVC.getCategoria();
            vehCarroceria = regSspVehiculoCertificacionVC.getCarroceria();
            vehPlaca = regSspVehiculoCertificacionVC.getPlaca();
            vehSerie = regSspVehiculoCertificacionVC.getSerie();
            vehMarca = regSspVehiculoCertificacionVC.getMarca();
            vehModelo = regSspVehiculoCertificacionVC.getModelo();
            txtVehPartRegistral = regSspVehiculoCertificacionVC.getAsientoSunarpId().getPartidaId().getPartidaRegistral();
            txtVehAsientoRegistral = regSspVehiculoCertificacionVC.getAsientoSunarpId().getNroAsiento();
            cboVehZonaRegistralRLSelected = regSspVehiculoCertificacionVC.getAsientoSunarpId().getPartidaId().getZonaRegistral();
            fnListarOficinasRegistralesVehiculo();
            cboVehOficinaRegistralRLSelected = regSspVehiculoCertificacionVC.getAsientoSunarpId().getPartidaId().getOficinaRegistral();
            vehPartRegNomArchivo = regSspVehiculoCertificacionVC.getArchPartidaRegistral();
            cboVehSbDistritoGtSelected = regSspVehiculoCertificacionVC.getDistritoId();
            vehArrendado = regSspVehiculoCertificacionVC.getArrendado();
            renderArrendatario = (vehArrendado.equals(JsfUtil.TRUE) ? true : false);
            vehContraArrendaNomArchivo = regSspVehiculoCertificacionVC.getArchContratArrenda();
            
             
            //LLENAR CERTIFICADO DEL PROVEEDOR
            regSspCertifProveeVCP = ejbSspCertifProveeFacade.buscarSspCertifProveeByRegistroId(regSspRegistroSCVB.getId());
            cboCertProvPaisSelectedStringID = regSspCertifProveeVCP.getPaisId().getId().toString();
            txtCertProvNroDocCertificadora = regSspCertifProveeVCP.getDocEmpresaCert();
            txtCertProvNomEmpCertificadora = regSspCertifProveeVCP.getEmpresaCertCaract();
            txtCertProvFechaIni = regSspCertifProveeVCP.getFechaIni();
            txtCertProvFechaFin = regSspCertifProveeVCP.getFechaFin();
            txtCertNumeroCert = regSspCertifProveeVCP.getNroCertificado();
            vehCertProvNomArchivo = regSspCertifProveeVCP.getArchivoCertProv();

            //LLENAR POLIZA DE SEGURO
            regSspPolizaVPoliza = ejbSspPolizaFacade.buscarSspPolizaByRegistroId(regSspRegistroSCVB.getId());
            txtPolizaNumRUCEmpAseguradora = regSspPolizaVPoliza.getRuc();
            txtPolizaNombreEmpAseguradora = regSspPolizaVPoliza.getEmpresaAseguradora();
            txtPolizaNumPoliza = regSspPolizaVPoliza.getNroPoliza();
            txtPolizaFechaDesde = regSspPolizaVPoliza.getVigenciaIni();
            txtPolizaFechaHasta = regSspPolizaVPoliza.getVigenciaFin();
            cboPolizaTipoMonedaSelected = regSspPolizaVPoliza.getTipoMoneda();
            txtPolizaMontoMaximo = regSspPolizaVPoliza.getMontoMaximo();
            vehPolizaNomArchivo = regSspPolizaVPoliza.getArchivoPoliza();
            vehPolizaBienesCustodia = regSspPolizaVPoliza.getBienes();
             
            fechaMinimaCalendario_CertProv = txtCertProvFechaIni;
            fechaMinimaCalendario_Poliza = txtPolizaFechaDesde;
            
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("/sel/faces/aplicacion/gssp/gsspCertificacion/certificado_cvb/CreateSolicCertificadoVB.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }            
        }     
    }
    
    
    public void fnListarOficinasRegistralesRL() {

        if (cboZonaRegistralRLSelected != null) {
            cboOficinaRegistralRLLista = ejbTipoBaseFacade.listarTipoBaseXCodProg(cboZonaRegistralRLSelected.getCodProg());
            cboOficinaRegistralRLSelected = null;
        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la zona registral");
            cboOficinaRegistralRLLista = null;
            cboOficinaRegistralRLSelected = null;
        }

    }
    
    public void fnListarOficinasRegistralesVehiculo() {

        if (cboVehZonaRegistralRLSelected != null) {
            cboVehOficinaRegistralRLLista = ejbTipoBaseFacade.listarTipoBaseXCodProg(cboVehZonaRegistralRLSelected.getCodProg());
            cboVehOficinaRegistralRLSelected = null;
        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la zona registral del Vehículo");
            cboVehOficinaRegistralRLLista = null;
            cboVehOficinaRegistralRLSelected = null;
        }

    }
    
    public void obtenerNumCaractDocumentoRepresentanteLegal() {
        System.out.println("obtenerNumCaractDocumentoRepresentanteLegal->ok");
    }
    
    public void fnValidarFragmentMedioContacto(){
        
        System.out.println("cboDlgMedioContactoSelected->"+cboDlgMedioContactoSelected);
        
        if(cboDlgMedioContactoSelected == null){
            JsfUtil.mensajeAdvertencia("Seleccione Medio de Contacto");
            dlgFragmentContacto = false;
            dlgLabelContacto = "-";
            return;
        }
        
        switch (cboDlgMedioContactoSelected.getCodProg()) { 
            case "TP_MEDCO_COR":
                dlgFragmentContacto = true;
                dlgLabelContacto = "Ingrese Correo:*";
             break;
            case "TP_MEDCO_FAX":
                dlgFragmentContacto = true;
                dlgLabelContacto = "Ingrese Nro. Fax:*";
             break;
            case "TP_MEDCO_FIJ" :
                dlgFragmentContacto = true;
                dlgLabelContacto = "Ingrese Nro. Fijo:*";
                break;
            case "TP_MEDCO_MOV" :
                dlgFragmentContacto = true;
                dlgLabelContacto = "Ingrese Nro. Movil:*";
             break;
            default:
             dlgFragmentContacto = false;
             dlgLabelContacto = "-";
          }

    }
    
    public void fnMostrarModalRepresentanteLegal() {  
        
        cboDlgTipoDocSelected = null;
        txtDlgNroDoc = null;
        txtDlgNombre = null;
        txtDlgApePat = null;
        txtDlgApeMat = null;
        txtDlgAFechaNac = null;
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvDlgRepresentanteLegal').show()");
        context.update("frmCuerpoPersona");
    }
    
    public void fnCancelarDlgRL() {  
        RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
        RequestContext.getCurrentInstance().update("frmCuerpoPersona");        
        RequestContext.getCurrentInstance().update("formRegSolicCertVB:bcoNewRepresentantelegal");
    }
    
    public void fnMostrarModalContacos() {    
        
        int totRegPrincipal = 0;
        for (SspContacto itemContactos : contactoLista) {                    
            if(itemContactos.getActivo() == JsfUtil.TRUE && ejbTipoBaseFacade.verDatosTipoBaseXId(Long.parseLong(itemContactos.getValor())).getCodProg().equals("TP_CONTAC_PRIN")) {
                totRegPrincipal++;             
            }                    
        }
        
        if(totRegPrincipal>0){
            cboDlgPrioridadLista = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_CONTAC_SEC'");
        }else{
            cboDlgPrioridadLista = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_CONTAC_PRIN','TP_CONTAC_SEC'");
        }
        
        cboDlgPrioridadSelected = null;
        cboDlgMedioContactoSelected = null;
        dlgFragmentContacto = false;
        dlgtextoContacto = null;
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvDlgContacto').show()");
        context.update("frmCuerpoContacto");
    }   
    
    
    public void fnCancelarDlgMedioContacto() { 
        RequestContext.getCurrentInstance().execute("PF('wvDlgContacto').hide()");
        RequestContext.getCurrentInstance().update("frmCuerpoContacto");
    }
    
    
    public void btnRegistrarPersonaRL() {
        boolean validarRegistroPersonaRL = true;
        
        if (cboDlgTipoDocSelected == null) {
            validarRegistroPersonaRL = false;
            JsfUtil.invalidar("frmCuerpoPersona:cboPersonaTipDoc");
            JsfUtil.mensajeAdvertencia("Seleccione un tipo de documento");
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            return;
        }
        
        if (txtDlgNroDoc == null || txtDlgNroDoc.equals("")) {
            validarRegistroPersonaRL = false;
            JsfUtil.invalidar("frmCuerpoPersona:txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Ingrese número del documento");
            RequestContext.getCurrentInstance().update("frmCuerpoPersona");
            return;
        }
        
        if (txtDlgAFechaNac == null || txtDlgAFechaNac.equals("")) {
            validarRegistroPersonaRL = false;
            JsfUtil.invalidar("frmCuerpoPersona:txtfrmRLFechaNac");
            JsfUtil.mensajeAdvertencia("Debe de ingresar la Fecha de Nacimiento");
            return;
            
        }else{
            //Fecha de Nacimiento no debe ser mayor a la de hoy dia
            Date fechaMayorDeEdadCalendario = getFechaMinMayorEdad();
            if (JsfUtil.getFechaSinHora(txtDlgAFechaNac).compareTo(JsfUtil.getFechaSinHora(fechaMayorDeEdadCalendario)) > 0) {
                validarRegistroPersonaRL = false;
                JsfUtil.invalidar("frmCuerpoPersona:txtfrmRLFechaNac");
                JsfUtil.mensajeAdvertencia("La fecha de Nacimiento debe ser de una persona mayor de Edad");
            }
        }
        
        if (personaDetalleListado != null) {
            int xContExisteRL = 0;
            for (PersonaDetalle personaBusca : personaDetalleListado) {
                if (personaBusca.getNumDoc().equals(txtDlgNroDoc)) {
                    xContExisteRL++;
                }
            }
            
            if(xContExisteRL>0){
                validarRegistroPersonaRL = false;
                JsfUtil.mensajeAdvertencia("Esta persona ya fue registrada como Representante Legal, con este Número de documento: " + txtDlgNroDoc);
                return;
            }
        }
        
        
        if(regDatoPersonaByNumDocRL == null){
            
            List<SbPersonaGt> xListaPersonaEncontrado = ejbSbPersonaFacade.selectPersonaActivoInactivoxTipoDocyNumDoc(cboDlgTipoDocSelected.getId(), txtDlgNroDoc);
            if(xListaPersonaEncontrado.size() == 0){
                try {
                    regDatoPersonaByNumDocRL = new SbPersonaGt();
                    regDatoPersonaByNumDocRL.setId(null);
                    regDatoPersonaByNumDocRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                    regDatoPersonaByNumDocRL.setTipoDoc(cboDlgTipoDocSelected);
                    regDatoPersonaByNumDocRL.setNumDoc(txtDlgNroDoc);
                    regDatoPersonaByNumDocRL.setApePat(txtDlgApePat);
                    regDatoPersonaByNumDocRL.setApeMat(txtDlgApeMat);
                    regDatoPersonaByNumDocRL.setNombres(txtDlgNombre);
                    regDatoPersonaByNumDocRL.setFechaNac(txtDlgAFechaNac);
                    regDatoPersonaByNumDocRL.setFechaReg(new Date());
                    regDatoPersonaByNumDocRL.setActivo(JsfUtil.TRUE);
                    regDatoPersonaByNumDocRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regDatoPersonaByNumDocRL.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSbPersonaFacade.create(regDatoPersonaByNumDocRL); 
                    validarRegistroPersonaRL = true;
                } catch (Exception e) {
                    JsfUtil.mensajeAdvertencia(e.getCause().getCause().getMessage());
                    validarRegistroPersonaRL = false;
                }
            }else{
                
                int xContActivo = 0;
                int xContInactivo = 0;
                for (SbPersonaGt personaAI : xListaPersonaEncontrado) {
                    if (personaAI.getActivo() == 1) {
                        xContActivo++;
                    }else{
                        xContInactivo++;
                    }
                }
                
                if(xContInactivo>0 && xContActivo==0){
                    validarRegistroPersonaRL = false;
                    JsfUtil.mensajeAdvertencia("La persona se encuentra Desactivado, con este Número de documento: " + txtDlgNroDoc + ". Mande un ticket sobre este caso, para su atención.");
                    return;
                }
            
            }
            
        } 
        
        
        if (validarRegistroPersonaRL) {           
                
            PersonaDetalle detallePersonaTemp = new PersonaDetalle();
            detallePersonaTemp = new PersonaDetalle();        
            detallePersonaTemp.setId(regDatoPersonaByNumDocRL.getId());
            detallePersonaTemp.setNumDoc(regDatoPersonaByNumDocRL.getNumDoc());
            detallePersonaTemp.setApePat(regDatoPersonaByNumDocRL.getApePat());
            detallePersonaTemp.setApeMat(regDatoPersonaByNumDocRL.getApeMat());
            detallePersonaTemp.setNombres(regDatoPersonaByNumDocRL.getNombres());
            //Agrega a la lista en forma temporal, ya se creo en Persona, no en relacionPersona
            personaDetalleListado.add(detallePersonaTemp);  

            personaDetalleSelectedString = detallePersonaTemp.getId().toString();

            RequestContext.getCurrentInstance().execute("PF('wvDlgRepresentanteLegal').hide()");
            RequestContext.getCurrentInstance().update("formRegSolicCertVB:fsRepresentanteNuevo");

            mostrarDomicilioRepresentanteLegal();
            
        }
        
    }
    
    
    public void mostrarDomicilioRepresentanteLegal() {
        
        String IdPersonaRLSelectedString = personaDetalleSelectedString;
        
        if (personaDetalleListado != null) {            
            if (IdPersonaRLSelectedString != null) {
                for (PersonaDetalle personaDetalle : personaDetalleListado) {
                    if (personaDetalle.getId().equals(Long.parseLong(IdPersonaRLSelectedString))) {                       
                                                
                        SbAsientoPersona xSbAsientoPersonaRL = new SbAsientoPersona();
                        Long xEmpresaId = regAdminist.getId();

                        System.out.println("*****************************************************************");
                        System.out.println("IdPersonaRLSelectedString->"+IdPersonaRLSelectedString);                        
                        System.out.println("xEmpresaId->"+xEmpresaId);                        
                        System.out.println("*****************************************************************");
                        
                        xSbAsientoPersonaRL = ejbSbAsientoPersonaFacade.buscarAsientoPersonaByPersonaId(Long.parseLong(IdPersonaRLSelectedString), xEmpresaId);                        
                        if (xSbAsientoPersonaRL != null) {
                            //valida si es la misma empresa
                            if(xSbAsientoPersonaRL.getEmpresaId().getId().equals(xEmpresaId)){
                                existeAsientoSunarpRL = true;
                                txtRLPartRegistral = xSbAsientoPersonaRL.getAsientoId().getPartidaId().getPartidaRegistral();
                                txtRLAsientoRegistral = xSbAsientoPersonaRL.getAsientoId().getNroAsiento();
                         
                                if (xSbAsientoPersonaRL.getAsientoId().getPartidaId() != null) {
                                    cboZonaRegistralRLSelected = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getZonaRegistral().getId());
                                    listarOficinasRegistrales_RL(cboZonaRegistralRLSelected.getCodProg());
                                    cboOficinaRegistralRLSelected = ejbTipoBaseFacade.find(xSbAsientoPersonaRL.getAsientoId().getPartidaId().getOficinaRegistral().getId());
                                } else {
                                    cboZonaRegistralRLSelected = null;
                                    cboOficinaRegistralRLSelected = null;
                                }    
                            }else{
                                existeAsientoSunarpRL = false;
                
                                txtRLPartRegistral = null;
                                txtRLAsientoRegistral = null;
                                cboZonaRegistralRLSelected = null;
                                cboOficinaRegistralRLSelected = null;
                                cboTipoUbicacionRLSelected = null;
                                txtRLDomicilioLegal = null;
                            }
                            
                        } else {
                            existeAsientoSunarpRL = false;
                
                            txtRLPartRegistral = null;
                            txtRLAsientoRegistral = null;
                            cboZonaRegistralRLSelected = null;
                            cboOficinaRegistralRLSelected = null;
                            cboTipoUbicacionRLSelected = null;
                            txtRLDomicilioLegal = null;
                        }

                        List<SbDireccionGt> listDireccionPersona = ejbSbDireccionFacade.listarDireccionesXPersona(Long.parseLong(IdPersonaRLSelectedString));
                        if (listDireccionPersona.size() > 0) {
                            cboTipoUbicacionRLSelected = listDireccionPersona.get(0).getViaId();
                            txtRLDomicilioLegal = listDireccionPersona.get(0).getDireccion();
                            cboDistritoRLSelected = listDireccionPersona.get(0).getDistritoId();
                        } else {
                            cboTipoUbicacionRLSelected = null;
                            txtRLDomicilioLegal = null;
                            cboDistritoRLSelected = null;
                        }
                        break;
                    }
                }

            } else {
                //Si es nulo la selección del Representante Legal
                existeAsientoSunarpRL = false;
                
                txtRLPartRegistral = null;
                txtRLAsientoRegistral = null;
                cboZonaRegistralRLSelected = null;
                cboOficinaRegistralRLSelected = null;
                cboTipoUbicacionRLSelected = null;
                txtRLDomicilioLegal = null;
            }
        } else {
            //Si es nulo la selección del Representante Legal
            existeAsientoSunarpRL = false;      
        }
    }
    
    
    public void listarOficinasRegistrales_RL(String xCodProgZonaReg) {

        if ((xCodProgZonaReg != null) || !(xCodProgZonaReg.equals(""))) {
            cboOficinaRegistralRLLista = ejbTipoBaseFacade.listarTipoBaseXCodProg(xCodProgZonaReg);
            cboOficinaRegistralRLSelected = null;

        } else {
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la zona registral");
            cboOficinaRegistralRLLista = null;
            cboOficinaRegistralRLSelected = null;
        }

    }
    
    public Date getFechaMinMayorEdad() {
        return calculoFechaVencMeses(new Date(), -12 * 18);
    }
    
    public Date calculoFechaVencMeses(Date fecha, int meses) {
        String hoy = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        String[] dataTemp = hoy.split("/");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(dataTemp[2]), Integer.parseInt(dataTemp[1]) - 1, Integer.parseInt(dataTemp[0]));
        c.add(Calendar.MONTH, meses);
        return c.getTime();
    }
    
    public String getMostrarDescripcionPrioridad(String id) {
        String nomPrioridad = ((ejbTipoBaseFacade.find(Long.parseLong(id)) != null) ? ejbTipoBaseFacade.find(Long.parseLong(id)).getNombre() : "-");        
        return nomPrioridad;
    }
    
    public void btnAgregarMedioContacto() { 
        boolean validacionContacto = true;
        
        if (cboDlgPrioridadSelected == null) {
            validacionContacto = false;
            JsfUtil.invalidar("frmCuerpoContacto:cboPrioridad");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar la prioridad");
            
        }else if (cboDlgMedioContactoSelected == null) {
            validacionContacto = false;
            JsfUtil.invalidar("frmCuerpoContacto:cboLPE_Alm_MedioContacto");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el medio de contacto");
            
        }else if(dlgtextoContacto == null || dlgtextoContacto.equals("")){
            
            if (cboDlgMedioContactoSelected.getCodProg().equals("TP_MEDCO_COR")) {               
                JsfUtil.invalidar("frmCuerpoContacto:txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el Correo Electrónico");
                validacionContacto = false;
                
            }else if (cboDlgMedioContactoSelected.getCodProg().equals("TP_MEDCO_FAX")) {
                JsfUtil.invalidar("frmCuerpoContacto:txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeAdvertencia("Debe de ingresar Nro de FAX");
                validacionContacto = false;
                
            }else if (cboDlgMedioContactoSelected.getCodProg().equals("TP_MEDCO_FIJ")) {
                JsfUtil.invalidar("frmCuerpoContacto:txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeAdvertencia("Debe de ingresar Nro de Teléfono Fijo");
                validacionContacto = false;
                
            }else if (cboDlgMedioContactoSelected.getCodProg().equals("TP_MEDCO_MOV")) {
                JsfUtil.invalidar("frmCuerpoContacto:txtLPE_Alm_DescripMedioContacto");
                JsfUtil.mensajeAdvertencia("Debe de ingresar Nro de Teléfono Móvil");
                validacionContacto = false;
                
            }
            
        }
        
        
        if(validacionContacto){
            
            for (SspContacto itemContacto : contactoLista) {                    
                if(itemContacto.getActivo() == JsfUtil.TRUE) {
                    if(itemContacto.getValor().equals(cboDlgPrioridadSelected.getId().toString()) && itemContacto.getTipoMedioId().getId().equals(cboDlgMedioContactoSelected.getId()) && itemContacto.getDescripcion().toUpperCase().equals(dlgtextoContacto.toUpperCase().trim()) 
                    ){
                        JsfUtil.invalidar("frmCuerpoContacto:txtLPE_Alm_DescripMedioContacto");
                        JsfUtil.mensajeAdvertencia("Ya existe un "+itemContacto.getTipoMedioId().getNombre()+" registrado ");
                        validacionContacto = false;
                        return;
                    }                        
                }                    
            }


            //Agregar contacto nuevo a la lista tempral de contactos
            SspContacto ItemContacto = new SspContacto();
            ItemContacto.setId(null);
            ItemContacto.setRegistroId(null);
            ItemContacto.setValor(cboDlgPrioridadSelected.getId()+"");
            ItemContacto.setTipoMedioId(cboDlgMedioContactoSelected);
            ItemContacto.setDescripcion(dlgtextoContacto.toUpperCase().trim()); 
            ItemContacto.setActivo(JsfUtil.TRUE); 
            contactoLista.add(ItemContacto); 
            
            System.out.println("contactoLista->"+contactoLista.size());
    
            dlgFragmentContacto = false;
            dlgLabelContacto = "-";
            dlgtextoContacto = null;

            
            RequestContext.getCurrentInstance().update("frmCuerpoContacto");
            RequestContext.getCurrentInstance().execute("PF('wvDlgContacto').hide()");
            
          
            
        } 
    }
    
    
            
    public void fnEliminarContacto(SspContacto contacto) {

        for (SspContacto xItemContacto : contactoLista) {                
            if(xItemContacto.getId() != null){//cuando es un EDITAR
                if(xItemContacto.equals(contacto)){ 
                    xItemContacto.setActivo(JsfUtil.FALSE);
                }
            }else{//cuando es un CREAR 
                if(xItemContacto.getValor().equals(contacto.getValor()) 
                        && xItemContacto.getTipoMedioId().equals(contacto.getTipoMedioId()) 
                        && xItemContacto.getDescripcion().equals(contacto.getDescripcion())  ){
                    xItemContacto.setActivo(JsfUtil.FALSE);
                } 
            }
        }
        
    }
    
    
    
    public List<GsspSolicitudCertificacionCVBController.resol_vista> autocompleteResolucionAprobados_ServTransYCustDineroValores(String nro_resol) {
        System.out.println("nro_resol->"+nro_resol);
        if (nro_resol != "" && administRUC != null) {
            try {
                List<resol_vista> list = new ArrayList<>();
                List<Object[]> lista_filtro = new ArrayList();
                lista_filtro = ejbSspResolucionFacade.list_autocomplete_BuscarResolucionAprobado_ServTransYCustDineroValores(nro_resol, administRUC);
                if (lista_filtro.size() > 0) {
                    for (Object[] obj : lista_filtro) {
                        list.add(new resol_vista(obj[0].toString(), obj[1].toString(), obj[2].toString()));
                    }
                    return list;
                } else {
                    return list;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;

    }
    
    
    public class resol_vista {

        String ID;
        String NRO_RD;
        String RESOL_FORMATEADO;

        public resol_vista(String ID, String NRO_RD, String RESOL_FORMATEADO) {
            this.ID = ID;
            this.NRO_RD = NRO_RD;
            this.RESOL_FORMATEADO = RESOL_FORMATEADO;
        }

        public String getID() {
            return ID;
        }

        public String getNRO_RD() {
            return NRO_RD;
        }

        public String getRESOL_FORMATEADO() {
            return RESOL_FORMATEADO;
        }
    }
    
    
    public void eligeResolAprobado_ServTransYCustDineroValores(SelectEvent event) {
        
        renderMostrarFormSolicCertVehBlindados = false;
        registroResolucionServTransYCustDineroValores = null;
        String servicio_prestado_resol = null;
        Long registroRS = null;
        String registroRS_TipoReg = null;
        String registroRS_TipoOpe = null;
        
        if (event.getObject() != null) {
            
            String RESOL_FORMATEADO = (String) event.getObject().toString();
            System.out.println("RESOL_FORMATEADO->"+RESOL_FORMATEADO); 
            
            if (RESOL_FORMATEADO != "" && administRUC != null) {
                
              emptyModel_vistaDataServTranspCustodiaDineroYValores =  ejbSspResolucionFacade.BuscarResolucionAprobado_ServTransYCustDineroValores_ByNumResolucionFormateado(RESOL_FORMATEADO, administRUC);
              if(emptyModel_vistaDataServTranspCustodiaDineroYValores !=null){
                    
                    renderMostrarFormSolicCertVehBlindados = true;                  
                  
                    if (emptyModel_vistaDataServTranspCustodiaDineroYValores.get("COD_ARM").equals("0")) {
                        servicio_prestado_resol = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("SI_ARMA_SSP").getNombre();
                    } else {
                        servicio_prestado_resol = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("NO_ARMA_SSP").getNombre();
                    }
                    
                    if(emptyModel_vistaDataServTranspCustodiaDineroYValores.get("REGISTRO_ID") !=  null){
                        registroRS = Long.parseLong(emptyModel_vistaDataServTranspCustodiaDineroYValores.get("REGISTRO_ID").toString());
                        registroRS_TipoReg = ejbSspRegistroFacade.buscarRegistroById(registroRS).getTipoRegId().getNombre();
                        registroRS_TipoOpe = ejbSspRegistroFacade.buscarRegistroById(registroRS).getTipoOpeId().getNombre();
                    }else{
                        registroRS_TipoReg = "INICIAL";
                        registroRS_TipoOpe = "INICIAL";
                    }
                    
                    emptyModel_vistaDataServTranspCustodiaDineroYValores.put("TIPO_REG_RESOL", registroRS_TipoReg);
                    emptyModel_vistaDataServTranspCustodiaDineroYValores.put("TIPO_OPE_RESOL", registroRS_TipoOpe);
                    emptyModel_vistaDataServTranspCustodiaDineroYValores.put("SERV_PRE_RESOL", servicio_prestado_resol);
                    
              }else{
                  JsfUtil.mensajeAdvertencia("La Resolución no esta vinculado a una Solicitud de Servicio de Transporte y Custodia de Dinero y Valores" );
                  renderMostrarFormSolicCertVehBlindados = false;
                  return;
              }
                
            }
        }
    }
    
    
    
    public void openDlgConfirmRegistrarSolic() {
        //RequestContext.getCurrentInstance().update("formRegSolicCertVB");
        //RequestContext.getCurrentInstance().update("formRegSolicCertVB:fsRepresentanteNuevo");
        if (validaRegistroSolicitudCertificacionVB()) {            
            RequestContext.getCurrentInstance().execute("PF('wvDialogConfirmarRegSCVB').show()");
            RequestContext.getCurrentInstance().update("frmConfirmarRegSCVB");
            
       }     
    }
    
    
    public boolean validaRegistroSolicitudCertificacionVB() {
        boolean validacion = true;

        //============================================
        //----  Validamos Representante Legal
        //============================================
        if (personaDetalleSelectedString == null || personaDetalleSelectedString.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:bcoNewRepresentantelegal");
            JsfUtil.mensajeAdvertencia("Debe seleccionar Representante Legal");
            return false;
        }
        
        if (txtRLPartRegistral == null || txtRLPartRegistral.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtRLPartidaReg");
            JsfUtil.mensajeAdvertencia("Debe ingresar Partida Registral del Representante Legal");
            return false;
        }
        
        if (txtRLAsientoRegistral == null || txtRLAsientoRegistral.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtRLAsientoReg");
            JsfUtil.mensajeAdvertencia("Debe ingresar Asiento Registral del Representante Legal");
            return false;
        }
        
        if (cboZonaRegistralRLSelected == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:bcoZonaRegRL");
            JsfUtil.mensajeAdvertencia("Debe seleccionar Zona Registral del Representante Legal");
            return false;
        }
        
        if (cboOficinaRegistralRLSelected == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:cboOficinaRegRL");
            JsfUtil.mensajeAdvertencia("Debe seleccionar Oficina Registral del Representante Legal");
            return false;
        }
        
        if (cboTipoUbicacionRLSelected == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:bcoTipoUbicacionRL");
            JsfUtil.mensajeAdvertencia("Debe seleccionar Tipo de Ubicación del Representante Legal");
            return false;
        }
        
        if (txtRLDomicilioLegal == null || txtRLDomicilioLegal.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtDomicilioLegalRL");
            JsfUtil.mensajeAdvertencia("Debe seleccionar Tipo de Ubicación del Representante Legal");
            return false;
        }
        
        if (cboDistritoRLSelected == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:cboUbigueoRL");
            JsfUtil.mensajeAdvertencia("Debe Ubigeo del Representante Legal");
            return false;
        }
        
        //============================================
        //----  Validamos Contactos
        //============================================
        
        ArrayList<SspContacto> xContactosActivos = new ArrayList<>();
        xContactosActivos = getContactoListaActivas();
        
        if (xContactosActivos.size() == 0) {
            validacion = false;
            JsfUtil.mensajeAdvertencia("Debe registrar por lo menos un contacto principal");
            return false;
        }
        
        if (xContactosActivos.size() > 0) {
            int xContPrincipal = 0;
            for (SspContacto itemContacto : xContactosActivos) {                    
                if(itemContacto.getActivo() == JsfUtil.TRUE && ejbTipoBaseFacade.find(Long.parseLong(itemContacto.getValor())).getCodProg().equals("TP_CONTAC_PRIN") ) {
                     xContPrincipal++;                      
                }                    
            }
            
            if(xContPrincipal == 0){
                validacion = false;
                JsfUtil.mensajeAdvertencia("No registró un contacto principal");
                return false;
            }
        }
        
        //============================================
        //----  Validamos Vehiculo
        //============================================
       
        
        if (vehCategoria == null || vehCategoria.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtVeh_Categoria");
            JsfUtil.mensajeAdvertencia("Ingrese categoría del vehículo");
            return false;
        }
        
        if (vehCarroceria == null || vehCarroceria.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtVeh_Carroceria");
            JsfUtil.mensajeAdvertencia("Ingrese carrocería del vehículo");
            return false;
        }
        
        if (vehPlaca == null || vehPlaca.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtVeh_Placa");
            JsfUtil.mensajeAdvertencia("Ingrese nro. placa del vehículo");
            return false;
        }
        
        if (vehSerie == null || vehSerie.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtVeh_Serie");
            JsfUtil.mensajeAdvertencia("Ingrese nro. serie del vehículo");
            return false;
        }
        
        if (vehMarca == null || vehMarca.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtVeh_Marca");
            JsfUtil.mensajeAdvertencia("Ingrese marca del vehículo");
            return false;
        }
        
        if (vehModelo == null || vehModelo.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtVeh_Modelo");
            JsfUtil.mensajeAdvertencia("Ingrese modelo del vehículo");
            return false;
        }
        
        if (txtVehPartRegistral == null || txtVehPartRegistral.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtVeh_PartReg");
            JsfUtil.mensajeAdvertencia("Ingrese partida registral del vehículo");
            return false;
        }
        
        if (txtVehAsientoRegistral == null || txtVehAsientoRegistral.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtVeh_AsientoReg");
            JsfUtil.mensajeAdvertencia("Ingrese asiento registral del vehículo");
            return false;
        }
        
        if (cboVehZonaRegistralRLSelected == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:CboVeh_ZonaReg");
            JsfUtil.mensajeAdvertencia("Seleccione zona registral del vehículo");
            return false;
        }
        
        if (cboVehOficinaRegistralRLSelected == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:CboVeh_OficReg");
            JsfUtil.mensajeAdvertencia("Seleccione oficina registral del vehículo");
            return false;
        }
        
        if (vehPartRegNomArchivo == null || vehPartRegNomArchivo.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txfVeh_ArchivoPartReg");
            JsfUtil.mensajeAdvertencia("Debe adjuntar Partida Registral del vehículo");
            return false;
        }
        
        if (cboVehSbDistritoGtSelected == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:CboVeh_UbigeoReg");
            JsfUtil.mensajeAdvertencia("Seleccione distrito donde se encuentra el vehículo");
            return false;
        }
        
        if (vehArrendado == null  || vehArrendado.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:RadioVeh_FormAdquiridoVeh");
            JsfUtil.mensajeAdvertencia("Seleccione forma de adquisición del vehículo");
            return false;
        }
        
        if ((renderArrendatario == true) && (vehContraArrendaNomArchivo == null || vehContraArrendaNomArchivo.equals("")) ) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txfVeh_ArchivoContraArend");
            JsfUtil.mensajeAdvertencia("Debe adjuntar Partida Registral del vehículo");
            return false;
        }
        
        //============================================
        //----  Validamos Certificacion Proveedor
        //============================================
        
        
        if (cboCertProvPaisSelectedStringID == null || cboCertProvPaisSelectedStringID.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:CboPaisProveedor");
            JsfUtil.mensajeAdvertencia("Seleccione País de Origen de la Empresa del Proveedor");
            return false;
        }
        
        if (txtCertProvNroDocCertificadora == null  || txtCertProvNroDocCertificadora.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtCertProv_NumDocEmpCert");
            JsfUtil.mensajeAdvertencia("Ingrese Nro de Documento de la Empresa Certificadora");
            return false;
        }
        
        if (txtCertProvNomEmpCertificadora == null  || txtCertProvNomEmpCertificadora.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtCertProv_NomEmpCert");
            JsfUtil.mensajeAdvertencia("Ingrese Nombre de la Empresa que emite Certificado");
            return false;
        }
        
        if (txtCertProvFechaIni == null  || txtCertProvFechaIni.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtCertProv_FechIni");
            JsfUtil.mensajeAdvertencia("Seleccione fecha vigencia desde");
            return false;
        }
        
        if (txtCertProvFechaFin == null  || txtCertProvFechaFin.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtCertProv_FechFin");
            JsfUtil.mensajeAdvertencia("Seleccione fecha vigencia hasta");
            return false;
        }
        
        if (txtCertNumeroCert == null  || txtCertNumeroCert.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtCertProv_NumeroCert");
            JsfUtil.mensajeAdvertencia("Ingrese Nro de Certificado");
            return false;
        }
        
        if (vehCertProvNomArchivo == null  || vehCertProvNomArchivo.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtCertProv_ArchivoCertificadoProv");
            JsfUtil.mensajeAdvertencia("Adjunte Certificado en formato PDF");
            return false;
        }
        
        
        //============================================
        //----  Validamos Certificacion Proveedor
        //============================================
        
        
        if (txtPolizaNumRUCEmpAseguradora == null || txtPolizaNumRUCEmpAseguradora.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_NumRucEmpAseguradora");
            JsfUtil.mensajeAdvertencia("Ingrese RUC de la Empresa Aseguradora");
            return false;
        }
        
        if (txtPolizaNombreEmpAseguradora == null || txtPolizaNombreEmpAseguradora.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_NombreEmpAseguradora");
            JsfUtil.mensajeAdvertencia("Ingrese Nombre de la Empresa Aseguradora");
            return false;
        }
        
        if (txtPolizaNumPoliza == null || txtPolizaNumPoliza.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_NroPoliza");
            JsfUtil.mensajeAdvertencia("Ingrese Número de la Póliza");
            return false;
        }
        
        if (txtPolizaFechaDesde == null || txtPolizaFechaDesde.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_FechaDesde");
            JsfUtil.mensajeAdvertencia("Seleccione Fecha Desde");
            return false;
        }
        
        if (txtPolizaFechaHasta == null || txtPolizaFechaHasta.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_FechaHasta");
            JsfUtil.mensajeAdvertencia("Seleccione Fecha Hasta");
            return false;
        }
        
        if (cboPolizaTipoMonedaSelected == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:cboPoliza_TipoMoneda");
            JsfUtil.mensajeAdvertencia("Seleccione Tipo de Moneda");
            return false;
        }
        
        
        if (txtPolizaMontoMaximo == null) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_MontoMaximo");
            JsfUtil.mensajeAdvertencia("Ingrese Monto Máximo.");
            return false;
        }
        
        if (txtPolizaMontoMaximo != null) {
            if (Double.parseDouble(("" + txtPolizaMontoMaximo).trim()) <= 0) {
                validacion = false;
                JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_MontoMaximo");
                JsfUtil.mensajeAdvertencia("Debe de ingresar el monto de la Póliza");
            }
        }
        
        if (vehPolizaNomArchivo == null || vehPolizaNomArchivo.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_Archivo");
            JsfUtil.mensajeAdvertencia("Adjunte Archivo Póliza");
            return false;
        }
        
        if (vehPolizaBienesCustodia == null || vehPolizaBienesCustodia.equals("")) {
            validacion = false;
            JsfUtil.invalidar("formRegSolicCertVB:txtPoliza_BienesCustodia");
            JsfUtil.mensajeAdvertencia("Ingrese Descripción de Bienes a Transportar y Custodiar");
            return false;
        }
        
        
        return validacion;
    }
    
    
    
    public void BuscarPersonaRepresentanteLegal() {
        
        boolean validacionPersonaRL = true;
        txtDlgNombre = null;
        txtDlgApePat = null;        
        txtDlgApeMat = null;
        txtDlgAFechaNac = null;
        
        if (cboDlgTipoDocSelected == null) {
            validacionPersonaRL = false;
            JsfUtil.invalidar("frmCuerpoPersona:cboPersonaTipDoc");
            JsfUtil.mensajeAdvertencia("Debe de seleccionar el tipo documento de la persona");
            return;
        }

        if (txtDlgNroDoc == null || txtDlgNroDoc.equals("")) {
            validacionPersonaRL = false;
            JsfUtil.invalidar("frmCuerpoPersona:txtPersonaNumDoc");
            JsfUtil.mensajeAdvertencia("Debe de ingresar el numero de documento de la persona");
            return;
        }

        if (cboDlgTipoDocSelected != null) {
            
            if (cboDlgTipoDocSelected.getCodProg().equals("TP_DOCID_DNI")) {//Valida el DNI
                if (txtDlgNroDoc.length() != 8) {
                    validacionPersonaRL = false;
                    JsfUtil.invalidar("frmCuerpoPersona:txtPersonaNumDoc");
                }
            }

            
            if (cboDlgTipoDocSelected.getCodProg().equals("TP_DOCID_CE")) {//Valida el Carnet de Extranjeria
                if (txtDlgNroDoc.length() < 12) {
                    validacionPersonaRL = false;
                    JsfUtil.invalidar("frmCuerpoPersona:txtPersonaNumDoc");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar mínimo 12 dígitos, en el número de documento para Carnet de Extranjeria");
                }
                if (txtDlgNroDoc.length() > 12) {
                    validacionPersonaRL = false;
                    JsfUtil.invalidar("frmCuerpoPersona:txtPersonaNumDoc");
                    JsfUtil.mensajeAdvertencia("Debe de ingresar máximo 12 dígitos, en el número de documento para Carnet de Extranjeria");
                }
            }
            
        }
        
        if(validacionPersonaRL){ 
            
            Consulta_Service serR = new Consulta_Service();
            PideMigraciones_Service serMigra = new PideMigraciones_Service();

            Consulta port = serR.getConsultaPort();
            PideMigraciones portMigra = serMigra.getPideMigracionesPort();

            wspide.Persona pn = null;
            wspide.ResPideMigra pext = null;
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date xFechaNac = null;

            try {

                
                regDatoPersonaByNumDocRL = ejbSbPersonaFacade.selectPersonaActivaxTipoDocyNumDoc(cboDlgTipoDocSelected.getId(), txtDlgNroDoc);
                
                if (regDatoPersonaByNumDocRL == null) {
                    
                    regDatoPersonaByNumDocRL = ejbSbPersonaFacade.buscarPersonaSel(null, txtDlgNroDoc);//verifica si existe la persona con el NRO DOC
                    if (regDatoPersonaByNumDocRL != null) {
                        //si el tipo de documento de la persona encontrada no coincide con el tipo de documento seleccionado en el modal form
                        if (!regDatoPersonaByNumDocRL.getTipoDoc().getCodProg().equals(cboDlgTipoDocSelected.getCodProg())) {
                            JsfUtil.mensajeAdvertencia("Se encontró un registro con el nro de documento ingresado, pero no coincide con el tipo de documento seleccionado, verifique nuevamente.");
                            regDatoPersonaByNumDocRL = null;
                            JsfUtil.invalidar("frmCuerpoPersona:cboPersonaTipDoc");
                            return;
                        }
                    }

                    if ("TP_DOCID_DNI".equals(cboDlgTipoDocSelected.getCodProg())) {

                        pn = port.consultaDNI(txtDlgNroDoc);
                        if (pn != null) {
                            
                            String xApePat = pn.getAPPAT();
                            String xApeMat = pn.getAPMAT();
                            String xNombre = pn.getNOMBRES();
                            xFechaNac = (pn.getFENAC() != null) ? formatter.parse(pn.getFENAC()) : null;                            

                            regDatoPersonaByNumDocRL = new SbPersonaGt();
                            regDatoPersonaByNumDocRL.setId(null);
                            regDatoPersonaByNumDocRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                            regDatoPersonaByNumDocRL.setTipoDoc(cboDlgTipoDocSelected);
                            regDatoPersonaByNumDocRL.setNumDoc(txtDlgNroDoc);
                            regDatoPersonaByNumDocRL.setApePat(xApePat);
                            regDatoPersonaByNumDocRL.setApeMat(xApeMat);
                            regDatoPersonaByNumDocRL.setNombres(xNombre);
                            if (xFechaNac != null) {
                                regDatoPersonaByNumDocRL.setFechaNac(txtDlgAFechaNac);
                            };
                            regDatoPersonaByNumDocRL.setFechaReg(new Date());
                            regDatoPersonaByNumDocRL.setActivo(JsfUtil.TRUE);
                            regDatoPersonaByNumDocRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            regDatoPersonaByNumDocRL.setAudNumIp(JsfUtil.getIpAddress());
                            ejbSbPersonaFacade.create(regDatoPersonaByNumDocRL);

                        } else {
                            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeServicioConsultaReniecNulo"));
                        }

                    } else if ("TP_DOCID_CE".equals(cboDlgTipoDocSelected.getCodProg())) {
                        pext = portMigra.consultarDocExt(JsfUtil.getLoggedUser().getLogin(), txtDlgNroDoc, cboDlgTipoDocSelected.getAbreviatura());
                        System.out.println("pext->"+pext);                        
                        if (pext != null) {
                                                       
                            if (pext.isRspta()) {

                                if (pext.getMensaje().contains("0000")) {

                                    regDatoPersonaByNumDocRL = new SbPersonaGt();
                                    regDatoPersonaByNumDocRL.setId(null);
                                    regDatoPersonaByNumDocRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                                    regDatoPersonaByNumDocRL.setTipoDoc(cboDlgTipoDocSelected);
                                    regDatoPersonaByNumDocRL.setNumDoc(txtDlgNroDoc);
                                    regDatoPersonaByNumDocRL.setApePat(pext.getResultado().getStrPrimerApellido());
                                    regDatoPersonaByNumDocRL.setApeMat(pext.getResultado().getStrSegundoApellido());
                                    regDatoPersonaByNumDocRL.setNombres(pext.getResultado().getStrNombres());
                                    regDatoPersonaByNumDocRL.setFechaNac(null);
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
                                } else{
                                    msj = pext.getMensaje();
                                }
                                JsfUtil.mensajeError(msj);
                                JsfUtil.mensajeAdvertencia("Ingrese los datos manualmente.");
                                return;
                            }
                        }
                        
                    }
                }
                
                txtDlgNombre = regDatoPersonaByNumDocRL.getNombres();
                txtDlgApePat = regDatoPersonaByNumDocRL.getApePat();
                txtDlgApeMat = regDatoPersonaByNumDocRL.getApeMat();
                txtDlgAFechaNac = regDatoPersonaByNumDocRL.getFechaNac();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.mensajeError(ex, JsfUtil.bundle("ErrorDePersistencia"));
            }

        
        }
    }
    
   
    
    public Integer getProgresoRegistrarSCVB() {
        
        if (totalProcesadosSCVB == null) {
            totalProcesadosSCVB = 0;
        }
        if (actualProcesadoSCVB == null) {
            actualProcesadoSCVB = 0;
        }
        if (progressSCVB == null) {
            progressSCVB = 0;
        } else if (totalProcesadosSCVB > 0) {
            progressSCVB = ((100 * actualProcesadoSCVB) / totalProcesadosSCVB);
            if (progressSCVB > 100) {
                progressSCVB = 100;
            }
            if (progressSCVB < 0) {
                progressSCVB = 0;
            }
        }

        return progressSCVB;
    }
    
    
    
     public String registroFinalSCVB(){
         
         String URL_Formulario = "";
         
         try {
             
             if(validaRegistroSolicitudCertificacionVB()){
                 
                 if(estado.equals(EstadoCrud.CREARCERTVEHBLIND)){//NUEVO
                     
                    System.out.println("estado_01->"+estado);
                     
                    SbPersonaGt xPersonaRLCboSelected = ejbSbPersonaFacade.find(Long.parseLong(personaDetalleSelectedString));

                    regSspRegistroSCVB .setId(null);
                    regSspRegistroSCVB.setCarneId(null);
                    regSspRegistroSCVB.setTipoRegId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REGIST_NOR"));
                    regSspRegistroSCVB.setTipoOpeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_OPE_INI"));
                    regSspRegistroSCVB.setTipoProId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GSSP_CERT_CVB"));//CERTIFICACION DE VEHICULOS BLINDADOS
                    regSspRegistroSCVB.setEmpresaId(regAdminist);
                    regSspRegistroSCVB.setEstadoId(ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_CRE"));//CREADO
                    regSspRegistroSCVB.setRepresentanteId(xPersonaRLCboSelected);                
                    regSspRegistroSCVB.setRegistroId(null);                
                    regSspRegistroSCVB.setNroSolicitiud(obtenerNroSolicitudEmision());
                    regSspRegistroSCVB.setSedeSucamec(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AREA_TRAM"));
                    regSspRegistroSCVB.setFecha(new Date());
                    regSspRegistroSCVB.setNroExpediente(null);
                    regSspRegistroSCVB.setFechaIni(null);
                    regSspRegistroSCVB.setFechaFin(null);
                    regSspRegistroSCVB.setObservacion("Se crea solicitud de certificación de vehículos blindados");
                    regSspRegistroSCVB.setActivo(JsfUtil.TRUE);
                    regSspRegistroSCVB.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspRegistroSCVB.setAudNumIp(JsfUtil.getIpAddress());
                    regSspRegistroSCVB.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                    regSspRegistroSCVB.setTipoAutId(null);
                    regSspRegistroSCVB.setUsuarioRecepcionId(null);
                    regSspRegistroSCVB.setCarneId(null);
                    regSspRegistroSCVB.setModalidadVinculadaId(null);
                    regSspRegistroSCVB.setSspRegistroEventoList(new ArrayList());
                    regSspRegistroSCVB.setSspRegistroList(new ArrayList());
                    regSspRegistroSCVB.setSspRequisitoList(new ArrayList());

                    String resolucionFormateado = emptyModel_vistaDataServTranspCustodiaDineroYValores.get("RESOL_FORMATEADO").toString();
                    regSspRegistroSCVB.setReqResolucionId(ejbSspResolucionFacade.buscarResolucionPorNroResolucion(resolucionFormateado));

                    ejbSspRegistroFacade.create(regSspRegistroSCVB);
                    
                    //=========== DATOS REGISTRALES DEL REPRESENTANTE LEGAL ===================
                    regSbPartidaSunarpRL.setId(null);
                    regSbPartidaSunarpRL.setPartidaRegistral(txtRLPartRegistral);
                    regSbPartidaSunarpRL.setZonaRegistral(cboZonaRegistralRLSelected);
                    regSbPartidaSunarpRL.setOficinaRegistral(cboOficinaRegistralRLSelected);
                    regSbPartidaSunarpRL.setFecha(new Date());
                    regSbPartidaSunarpRL.setActivo(JsfUtil.TRUE);
                    regSbPartidaSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbPartidaSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbPartidaSunarpRL = (SbPartidaSunarp) JsfUtil.entidadMayusculas(regSbPartidaSunarpRL, "");
                    ejbSbPartidaSunarpFacade.create(regSbPartidaSunarpRL);
                    
                    regSbAsientoSunarpRL.setId(null);
                    regSbAsientoSunarpRL.setPartidaId(regSbPartidaSunarpRL);
                    regSbAsientoSunarpRL.setNroAsiento(txtRLAsientoRegistral);
                    regSbAsientoSunarpRL.setFecha(new Date());
                    regSbAsientoSunarpRL.setActivo(JsfUtil.TRUE);
                    regSbAsientoSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbAsientoSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbAsientoSunarpRL = (SbAsientoSunarp) JsfUtil.entidadMayusculas(regSbAsientoSunarpRL, "");
                    ejbSbAsientoSunarpFacade.create(regSbAsientoSunarpRL);
                    
                    regSbAsientoPersonaRL.setId(null);
                    regSbAsientoPersonaRL.setAsientoId(regSbAsientoSunarpRL);
                    regSbAsientoPersonaRL.setPersonaId(xPersonaRLCboSelected);
                    regSbAsientoPersonaRL.setEmpresaId(regSspRegistroSCVB.getEmpresaId());
                    regSbAsientoPersonaRL.setFecha(new Date());
                    regSbAsientoPersonaRL.setActivo(JsfUtil.TRUE);
                    regSbAsientoPersonaRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbAsientoPersonaRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbAsientoPersonaRL = (SbAsientoPersona) JsfUtil.entidadMayusculas(regSbAsientoPersonaRL, "");
                    ejbSbAsientoPersonaFacade.create(regSbAsientoPersonaRL);
                    
                    
                    regSbDireccionGtRL.setId(null);
                    regSbDireccionGtRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DIRECB_FIS"));
                    regSbDireccionGtRL.setPersonaId(xPersonaRLCboSelected);
                    regSbDireccionGtRL.setZonaId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_ZON_URB"));
                    regSbDireccionGtRL.setViaId(cboTipoUbicacionRLSelected);
                    regSbDireccionGtRL.setDireccion(txtRLDomicilioLegal);
                    regSbDireccionGtRL.setDistritoId(cboDistritoRLSelected);
                    regSbDireccionGtRL.setNumero(null);
                    regSbDireccionGtRL.setReferencia(null);
                    regSbDireccionGtRL.setGeoLat(null);
                    regSbDireccionGtRL.setGeoLong(null);
                    regSbDireccionGtRL.setActivo(JsfUtil.TRUE);
                    regSbDireccionGtRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbDireccionGtRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbDireccionGtRL.setPaisId(ejbSbPaisFacadeGt.obtenerPaisByNombre("PERU"));
                    regSbDireccionGtRL = (SbDireccionGt) JsfUtil.entidadMayusculas(regSbDireccionGtRL, "");
                    ejbSbDireccionFacade.create(regSbDireccionGtRL);
                    
                    
                    regSspRepresentanteRegistroRL.setId(null);
                    regSspRepresentanteRegistroRL.setRegistroId(regSspRegistroSCVB);
                    regSspRepresentanteRegistroRL.setRepresentanteId(xPersonaRLCboSelected);
                    regSspRepresentanteRegistroRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));
                    regSspRepresentanteRegistroRL.setDireccionRLId(regSbDireccionGtRL);
                    regSspRepresentanteRegistroRL.setAsientoRLId(regSbAsientoPersonaRL);
                    regSspRepresentanteRegistroRL.setFecha(new Date());
                    regSspRepresentanteRegistroRL.setActivo(JsfUtil.TRUE);
                    regSspRepresentanteRegistroRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspRepresentanteRegistroRL.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSspRepresentanteRegistroFacade.create(regSspRepresentanteRegistroRL);
                    
                    
                    //========================== DATOS SSP_CONTACTO ===================
                    if (getContactoListaActivas().size() > 0) {                
                        for (SspContacto itemContacto : getContactoListaActivas()) {
                            SspContacto tempSspContacto  = new SspContacto();
                            tempSspContacto.setId(null);
                            tempSspContacto.setRegistroId(regSspRegistroSCVB);
                            tempSspContacto.setValor(itemContacto.getValor());
                            tempSspContacto.setTipoMedioId(itemContacto.getTipoMedioId());
                            tempSspContacto.setDescripcion(itemContacto.getDescripcion());
                            tempSspContacto.setFecha(new Date());
                            tempSspContacto.setActivo(JsfUtil.TRUE);
                            tempSspContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            tempSspContacto.setAudNumIp(JsfUtil.getIpAddress());
                            tempSspContacto = (SspContacto) JsfUtil.entidadMayusculas(tempSspContacto, "");
                            ejbSspContactoFacade.create(tempSspContacto);
                        }
                    }
                    
                    
                    //=========== DATOS DEL VEHICULO A CERTIFICAR  ===================
                    regSbPartidaSunarpVC.setId(null);
                    regSbPartidaSunarpVC.setPartidaRegistral(txtVehPartRegistral);
                    regSbPartidaSunarpVC.setZonaRegistral(cboVehZonaRegistralRLSelected);
                    regSbPartidaSunarpVC.setOficinaRegistral(cboVehOficinaRegistralRLSelected);
                    regSbPartidaSunarpVC.setFecha(new Date());
                    regSbPartidaSunarpVC.setActivo(JsfUtil.TRUE);
                    regSbPartidaSunarpVC.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbPartidaSunarpVC.setAudNumIp(JsfUtil.getIpAddress());
                    regSbPartidaSunarpVC = (SbPartidaSunarp) JsfUtil.entidadMayusculas(regSbPartidaSunarpVC, "");
                    ejbSbPartidaSunarpFacade.create(regSbPartidaSunarpVC);
                    
                    regSbAsientoSunarpVC.setId(null);
                    regSbAsientoSunarpVC.setPartidaId(regSbPartidaSunarpVC);
                    regSbAsientoSunarpVC.setNroAsiento(txtVehAsientoRegistral);
                    regSbAsientoSunarpVC.setFecha(new Date());
                    regSbAsientoSunarpVC.setActivo(JsfUtil.TRUE);
                    regSbAsientoSunarpVC.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbAsientoSunarpVC.setAudNumIp(JsfUtil.getIpAddress());
                    regSbAsientoSunarpVC = (SbAsientoSunarp) JsfUtil.entidadMayusculas(regSbAsientoSunarpVC, "");
                    ejbSbAsientoSunarpFacade.create(regSbAsientoSunarpVC);
                    
                    
                    //Generamos Nombre de PartidaRegistral Vehiculo
                    String fileNameArchivoPartRegVehiculo = "";
                    fileNameArchivoPartRegVehiculo = vehPartRegNomArchivo;
                    fileNameArchivoPartRegVehiculo = "SCVBGssp_PRVEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_PR_VEH").toString() + fileNameArchivoPartRegVehiculo.substring(fileNameArchivoPartRegVehiculo.lastIndexOf('.'), fileNameArchivoPartRegVehiculo.length());
                    fileNameArchivoPartRegVehiculo = fileNameArchivoPartRegVehiculo.toUpperCase();

                    regSspVehiculoCertificacionVC.setId(null);
                    regSspVehiculoCertificacionVC.setRegistroId(regSspRegistroSCVB);
                    regSspVehiculoCertificacionVC.setCategoria(vehCategoria);
                    regSspVehiculoCertificacionVC.setCarroceria(vehCarroceria);
                    regSspVehiculoCertificacionVC.setPlaca(vehPlaca);
                    regSspVehiculoCertificacionVC.setSerie(vehSerie);
                    regSspVehiculoCertificacionVC.setMarca(vehMarca);
                    regSspVehiculoCertificacionVC.setModelo(vehModelo);
                    regSspVehiculoCertificacionVC.setAsientoSunarpId(regSbAsientoSunarpVC);
                    regSspVehiculoCertificacionVC.setArchPartidaRegistral(fileNameArchivoPartRegVehiculo);
                    regSspVehiculoCertificacionVC.setDistritoId(cboVehSbDistritoGtSelected);
                    regSspVehiculoCertificacionVC.setArrendado(vehArrendado);

                    if(vehArrendado.equals(JsfUtil.TRUE)){

                        //Generamos Nombre de ContratoArrendo Vehiculo
                        String fileNameArchivoContratoArrendaVehiculo = "";
                        fileNameArchivoContratoArrendaVehiculo = vehContraArrendaNomArchivo;
                        fileNameArchivoContratoArrendaVehiculo = "SCVBGssp_CAVEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CAV").toString() + fileNameArchivoContratoArrendaVehiculo.substring(fileNameArchivoContratoArrendaVehiculo.lastIndexOf('.'), fileNameArchivoContratoArrendaVehiculo.length());
                        fileNameArchivoContratoArrendaVehiculo = fileNameArchivoContratoArrendaVehiculo.toUpperCase();

                        regSspVehiculoCertificacionVC.setArchContratArrenda(fileNameArchivoContratoArrendaVehiculo);
                    }else{
                        regSspVehiculoCertificacionVC.setArchContratArrenda(null);
                    }

                    regSspVehiculoCertificacionVC.setActivo(JsfUtil.TRUE);
                    regSspVehiculoCertificacionVC.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspVehiculoCertificacionVC.setAudNumIp(JsfUtil.getIpAddress());
                    regSspVehiculoCertificacionVC = (SspVehiculoCertificacion) JsfUtil.entidadMayusculas(regSspVehiculoCertificacionVC, "");
                    ejbSspVehiculoCertificacionFacade.create(regSspVehiculoCertificacionVC);

                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_PRVEH").getValor() + regSspVehiculoCertificacionVC.getArchPartidaRegistral()), vehPartRegByte);

                    if(vehArrendado.equals(JsfUtil.TRUE)){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_CAVEH").getValor() + regSspVehiculoCertificacionVC.getArchContratArrenda()), vehContraArrendaRegByte);
                    }
                    
                    //=========== DATOS CERTIFICADO DEL PROVEEDOR  =================== 
                    //Generamos Nombre Doc Certificado Proveedor
                    String fileNameArchivoCertProveVehiculo = "";
                    fileNameArchivoCertProveVehiculo = vehCertProvNomArchivo;
                    fileNameArchivoCertProveVehiculo = "SCVBGssp_CERTPROVEVEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CERTPROVE").toString() + fileNameArchivoCertProveVehiculo.substring(fileNameArchivoCertProveVehiculo.lastIndexOf('.'), fileNameArchivoCertProveVehiculo.length());
                    fileNameArchivoCertProveVehiculo = fileNameArchivoCertProveVehiculo.toUpperCase();                

                    regSspCertifProveeVCP.setId(null);
                    regSspCertifProveeVCP.setRegistroId(regSspRegistroSCVB);
                    regSspCertifProveeVCP.setPaisId(ejbSbPaisFacadeGt.find(Long.parseLong(cboCertProvPaisSelectedStringID)));
                    regSspCertifProveeVCP.setDocEmpresaCert(txtCertProvNroDocCertificadora);
                    regSspCertifProveeVCP.setEmpresaCertCaract(txtCertProvNomEmpCertificadora);
                    regSspCertifProveeVCP.setFechaIni(txtCertProvFechaIni);
                    regSspCertifProveeVCP.setFechaFin(txtCertProvFechaFin);
                    regSspCertifProveeVCP.setNroCertificado(txtCertNumeroCert);
                    regSspCertifProveeVCP.setArchivoCertProv(fileNameArchivoCertProveVehiculo);
                    regSspCertifProveeVCP.setActivo(JsfUtil.TRUE);
                    regSspCertifProveeVCP.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspCertifProveeVCP.setAudNumIp(JsfUtil.getIpAddress());
                    regSspCertifProveeVCP = (SspCertifProvee) JsfUtil.entidadMayusculas(regSspCertifProveeVCP, "");
                    ejbSspCertifProveeFacade.create(regSspCertifProveeVCP);
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_CERTPROV").getValor() + regSspCertifProveeVCP.getArchivoCertProv()), vehCertProvRegByte);

                    
                    //=========== DATOS POLIZA DE SEGURO  ===================                
                    //Generamos Nombre Doc Certificado Proveedor
                    String fileNameArchivoPolizaSegVehiculo = "";
                    fileNameArchivoPolizaSegVehiculo = vehPolizaNomArchivo;
                    fileNameArchivoPolizaSegVehiculo = "SCVBGssp_POLIZASEGVEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_POLIZA").toString() + fileNameArchivoPolizaSegVehiculo.substring(fileNameArchivoPolizaSegVehiculo.lastIndexOf('.'), fileNameArchivoPolizaSegVehiculo.length());
                    fileNameArchivoPolizaSegVehiculo = fileNameArchivoPolizaSegVehiculo.toUpperCase();

                    regSspPolizaVPoliza.setId(null);
                    regSspPolizaVPoliza.setRegistroId(regSspRegistroSCVB);
                    regSspPolizaVPoliza.setRuc(txtPolizaNumRUCEmpAseguradora);
                    regSspPolizaVPoliza.setEmpresaAseguradora(txtPolizaNombreEmpAseguradora);
                    regSspPolizaVPoliza.setNroPoliza(txtPolizaNumPoliza);                
                    regSspPolizaVPoliza.setVigenciaIni(txtPolizaFechaDesde);
                    regSspPolizaVPoliza.setVigenciaFin(txtPolizaFechaHasta);
                    regSspPolizaVPoliza.setTipoMoneda(cboPolizaTipoMonedaSelected);
                    regSspPolizaVPoliza.setMontoMaximo(txtPolizaMontoMaximo);
                    regSspPolizaVPoliza.setArchivoPoliza(fileNameArchivoPolizaSegVehiculo);
                    regSspPolizaVPoliza.setBienes(vehPolizaBienesCustodia);
                    regSspPolizaVPoliza.setActivo(JsfUtil.TRUE);
                    regSspPolizaVPoliza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspPolizaVPoliza.setAudNumIp(JsfUtil.getIpAddress());
                    regSspPolizaVPoliza = (SspPoliza) JsfUtil.entidadMayusculas(regSspPolizaVPoliza, "");
                    ejbSspPolizaFacade.create(regSspPolizaVPoliza);
                    FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_POLIZAVEH").getValor() + regSspPolizaVPoliza.getArchivoPoliza()), vehPolizaRegByte);

                    SspRegistroEvento registroEvento = new SspRegistroEvento();
                    registroEvento.setId(null);
                    registroEvento.setRegistroId(regSspRegistroSCVB);
                    registroEvento.setTipoEventoId(regSspRegistroSCVB.getEstadoId());
                    registroEvento.setObservacion("Crear solicitud de certificación de vehículos blindados.");
                    registroEvento.setFecha(new Date());
                    registroEvento.setActivo(JsfUtil.TRUE);
                    registroEvento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    registroEvento.setAudNumIp(JsfUtil.getIpAddress());
                    registroEvento = (SspRegistroEvento) JsfUtil.entidadMayusculas(registroEvento, "");
                    ejbSspRegistroEventoFacade.create(registroEvento);
                     
                 }else if(estado.equals(EstadoCrud.EDITARCERTVEHBLIND)){//EDITAR
                     
                     System.out.println("estado_02->"+estado);
                    
                    SbPersonaGt xPersonaRLCboSelected = ejbSbPersonaFacade.find(Long.parseLong(personaDetalleSelectedString));

                    regSspRegistroSCVB.setRepresentanteId(xPersonaRLCboSelected);                
                    regSspRegistroSCVB.setFecha(new Date());
                    regSspRegistroSCVB.setObservacion("Se editó solicitud de certificación de vehículos blindados");
                    regSspRegistroSCVB.setActivo(JsfUtil.TRUE);
                    regSspRegistroSCVB.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspRegistroSCVB.setAudNumIp(JsfUtil.getIpAddress());
                    regSspRegistroSCVB.setUsuarioCreacionId(ejbSbUsuarioFacadeGt.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                    ejbSspRegistroFacade.edit(regSspRegistroSCVB);
                    
                    //=========== DATOS REGISTRALES DEL REPRESENTANTE LEGAL ===================
                    regSbPartidaSunarpRL.setPartidaRegistral(txtRLPartRegistral);
                    regSbPartidaSunarpRL.setZonaRegistral(cboZonaRegistralRLSelected);
                    regSbPartidaSunarpRL.setOficinaRegistral(cboOficinaRegistralRLSelected);
                    regSbPartidaSunarpRL.setFecha(new Date());
                    regSbPartidaSunarpRL.setActivo(JsfUtil.TRUE);
                    regSbPartidaSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbPartidaSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbPartidaSunarpRL = (SbPartidaSunarp) JsfUtil.entidadMayusculas(regSbPartidaSunarpRL, "");
                    ejbSbPartidaSunarpFacade.edit(regSbPartidaSunarpRL);
                    
                    regSbAsientoSunarpRL.setPartidaId(regSbPartidaSunarpRL);
                    regSbAsientoSunarpRL.setNroAsiento(txtRLAsientoRegistral);
                    regSbAsientoSunarpRL.setFecha(new Date());
                    regSbAsientoSunarpRL.setActivo(JsfUtil.TRUE);
                    regSbAsientoSunarpRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbAsientoSunarpRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbAsientoSunarpRL = (SbAsientoSunarp) JsfUtil.entidadMayusculas(regSbAsientoSunarpRL, "");
                    ejbSbAsientoSunarpFacade.edit(regSbAsientoSunarpRL);
                    
                    regSbAsientoPersonaRL.setAsientoId(regSbAsientoSunarpRL);
                    regSbAsientoPersonaRL.setPersonaId(xPersonaRLCboSelected);
                    regSbAsientoPersonaRL.setEmpresaId(regSspRegistroSCVB.getEmpresaId());
                    regSbAsientoPersonaRL.setFecha(new Date());
                    regSbAsientoPersonaRL.setActivo(JsfUtil.TRUE);
                    regSbAsientoPersonaRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbAsientoPersonaRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbAsientoPersonaRL = (SbAsientoPersona) JsfUtil.entidadMayusculas(regSbAsientoPersonaRL, "");
                    ejbSbAsientoPersonaFacade.edit(regSbAsientoPersonaRL);
                    
                    
                    regSbDireccionGtRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DIRECB_FIS"));
                    regSbDireccionGtRL.setPersonaId(xPersonaRLCboSelected);
                    regSbDireccionGtRL.setZonaId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_ZON_URB"));
                    regSbDireccionGtRL.setViaId(cboTipoUbicacionRLSelected);
                    regSbDireccionGtRL.setDireccion(txtRLDomicilioLegal);
                    regSbDireccionGtRL.setDistritoId(cboDistritoRLSelected);
                    regSbDireccionGtRL.setActivo(JsfUtil.TRUE);
                    regSbDireccionGtRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbDireccionGtRL.setAudNumIp(JsfUtil.getIpAddress());
                    regSbDireccionGtRL.setPaisId(ejbSbPaisFacadeGt.obtenerPaisByNombre("PERU"));
                    regSbDireccionGtRL = (SbDireccionGt) JsfUtil.entidadMayusculas(regSbDireccionGtRL, "");
                    ejbSbDireccionFacade.edit(regSbDireccionGtRL);
                    
                    
                    regSspRepresentanteRegistroRL.setRegistroId(regSspRegistroSCVB);
                    regSspRepresentanteRegistroRL.setRepresentanteId(xPersonaRLCboSelected);
                    regSspRepresentanteRegistroRL.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_REL_RL"));
                    regSspRepresentanteRegistroRL.setDireccionRLId(regSbDireccionGtRL);
                    regSspRepresentanteRegistroRL.setAsientoRLId(regSbAsientoPersonaRL);
                    regSspRepresentanteRegistroRL.setFecha(new Date());
                    regSspRepresentanteRegistroRL.setActivo(JsfUtil.TRUE);
                    regSspRepresentanteRegistroRL.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspRepresentanteRegistroRL.setAudNumIp(JsfUtil.getIpAddress());
                    ejbSspRepresentanteRegistroFacade.edit(regSspRepresentanteRegistroRL);
                    
                    //========================== DATOS SSP_CONTACTO ===================
                    ejbSspContactoFacade.anularContactosByIdRegistro(regSspRegistroSCVB.getId()); //Anulamos los registros existentes
                    if (getContactoListaActivas().size() > 0) {                
                        for (SspContacto itemContacto : getContactoListaActivas()) {
                            SspContacto tempSspContacto  = new SspContacto();
                            tempSspContacto.setId(null);
                            tempSspContacto.setRegistroId(regSspRegistroSCVB);
                            tempSspContacto.setValor(itemContacto.getValor());
                            tempSspContacto.setTipoMedioId(itemContacto.getTipoMedioId());
                            tempSspContacto.setDescripcion(itemContacto.getDescripcion());
                            tempSspContacto.setFecha(new Date());
                            tempSspContacto.setActivo(JsfUtil.TRUE);
                            tempSspContacto.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            tempSspContacto.setAudNumIp(JsfUtil.getIpAddress());
                            tempSspContacto = (SspContacto) JsfUtil.entidadMayusculas(tempSspContacto, "");
                            ejbSspContactoFacade.create(tempSspContacto);
                        }
                    }
                    
                    
                    //=========== DATOS DEL VEHICULO A CERTIFICAR  ===================
                    regSbPartidaSunarpVC.setPartidaRegistral(txtVehPartRegistral);
                    regSbPartidaSunarpVC.setZonaRegistral(cboVehZonaRegistralRLSelected);
                    regSbPartidaSunarpVC.setOficinaRegistral(cboVehOficinaRegistralRLSelected);
                    regSbPartidaSunarpVC.setFecha(new Date());
                    regSbPartidaSunarpVC.setActivo(JsfUtil.TRUE);
                    regSbPartidaSunarpVC.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbPartidaSunarpVC.setAudNumIp(JsfUtil.getIpAddress());
                    regSbPartidaSunarpVC = (SbPartidaSunarp) JsfUtil.entidadMayusculas(regSbPartidaSunarpVC, "");
                    ejbSbPartidaSunarpFacade.edit(regSbPartidaSunarpVC);
                    
                    
                    regSbAsientoSunarpVC.setPartidaId(regSbPartidaSunarpVC);
                    regSbAsientoSunarpVC.setNroAsiento(txtVehAsientoRegistral);
                    regSbAsientoSunarpVC.setFecha(new Date());
                    regSbAsientoSunarpVC.setActivo(JsfUtil.TRUE);
                    regSbAsientoSunarpVC.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSbAsientoSunarpVC.setAudNumIp(JsfUtil.getIpAddress());
                    regSbAsientoSunarpVC = (SbAsientoSunarp) JsfUtil.entidadMayusculas(regSbAsientoSunarpVC, "");
                    ejbSbAsientoSunarpFacade.edit(regSbAsientoSunarpVC);
                    
                    
                    
                    
                    regSspVehiculoCertificacionVC.setRegistroId(regSspRegistroSCVB);
                    regSspVehiculoCertificacionVC.setCategoria(vehCategoria);
                    regSspVehiculoCertificacionVC.setCarroceria(vehCarroceria);
                    regSspVehiculoCertificacionVC.setPlaca(vehPlaca);
                    regSspVehiculoCertificacionVC.setSerie(vehSerie);
                    regSspVehiculoCertificacionVC.setMarca(vehMarca);
                    regSspVehiculoCertificacionVC.setModelo(vehModelo);
                    regSspVehiculoCertificacionVC.setAsientoSunarpId(regSbAsientoSunarpVC);
                    
                    if(vehPartRegByte !=null){
                        //Generamos Nombre de PartidaRegistral Vehiculo
                        String fileNameArchivoPartRegVehiculo = "";
                        fileNameArchivoPartRegVehiculo = vehPartRegNomArchivo;
                        fileNameArchivoPartRegVehiculo = "SCVBGssp_PRVEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_PR_VEH").toString() + fileNameArchivoPartRegVehiculo.substring(fileNameArchivoPartRegVehiculo.lastIndexOf('.'), fileNameArchivoPartRegVehiculo.length());
                        fileNameArchivoPartRegVehiculo = fileNameArchivoPartRegVehiculo.toUpperCase();
                        regSspVehiculoCertificacionVC.setArchPartidaRegistral(fileNameArchivoPartRegVehiculo);
                    }                    
                    regSspVehiculoCertificacionVC.setDistritoId(cboVehSbDistritoGtSelected);
                    regSspVehiculoCertificacionVC.setArrendado(vehArrendado);

                    if(vehArrendado.equals(JsfUtil.TRUE)){

                        if(vehContraArrendaRegByte != null){
                            //Generamos Nombre de ContratoArrendo Vehiculo
                            String fileNameArchivoContratoArrendaVehiculo = "";
                            fileNameArchivoContratoArrendaVehiculo = vehContraArrendaNomArchivo;
                            fileNameArchivoContratoArrendaVehiculo = "SCVBGssp_CAVEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CAV").toString() + fileNameArchivoContratoArrendaVehiculo.substring(fileNameArchivoContratoArrendaVehiculo.lastIndexOf('.'), fileNameArchivoContratoArrendaVehiculo.length());
                            fileNameArchivoContratoArrendaVehiculo = fileNameArchivoContratoArrendaVehiculo.toUpperCase();

                            regSspVehiculoCertificacionVC.setArchContratArrenda(fileNameArchivoContratoArrendaVehiculo);
                        }
                        
                    }else{
                        regSspVehiculoCertificacionVC.setArchContratArrenda(null);
                    }

                    regSspVehiculoCertificacionVC.setActivo(JsfUtil.TRUE);
                    regSspVehiculoCertificacionVC.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspVehiculoCertificacionVC.setAudNumIp(JsfUtil.getIpAddress());
                    regSspVehiculoCertificacionVC = (SspVehiculoCertificacion) JsfUtil.entidadMayusculas(regSspVehiculoCertificacionVC, "");
                    ejbSspVehiculoCertificacionFacade.edit(regSspVehiculoCertificacionVC);

                    if(vehPartRegByte !=null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_PRVEH").getValor() + regSspVehiculoCertificacionVC.getArchPartidaRegistral()), vehPartRegByte);
                    }
                    
                    if(vehArrendado.equals(JsfUtil.TRUE)){
                        if(vehContraArrendaRegByte != null){
                            FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_CAVEH").getValor() + regSspVehiculoCertificacionVC.getArchContratArrenda()), vehContraArrendaRegByte);
                        }
                    }
                    
                    
                    //=========== DATOS CERTIFICADO DEL PROVEEDOR  ===================                                    

                    regSspCertifProveeVCP.setRegistroId(regSspRegistroSCVB);
                    regSspCertifProveeVCP.setPaisId(ejbSbPaisFacadeGt.find(Long.parseLong(cboCertProvPaisSelectedStringID)));
                    regSspCertifProveeVCP.setDocEmpresaCert(txtCertProvNroDocCertificadora);
                    regSspCertifProveeVCP.setEmpresaCertCaract(txtCertProvNomEmpCertificadora);
                    regSspCertifProveeVCP.setFechaIni(txtCertProvFechaIni);
                    regSspCertifProveeVCP.setFechaFin(txtCertProvFechaFin);
                    regSspCertifProveeVCP.setNroCertificado(txtCertNumeroCert);
                    
                    if(vehCertProvRegByte !=null){
                        //Generamos Nombre Doc Certificado Proveedor
                        String fileNameArchivoCertProveVehiculo = "";
                        fileNameArchivoCertProveVehiculo = vehCertProvNomArchivo;
                        fileNameArchivoCertProveVehiculo = "SCVBGssp_CERTPROVEVEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_CERTPROVE").toString() + fileNameArchivoCertProveVehiculo.substring(fileNameArchivoCertProveVehiculo.lastIndexOf('.'), fileNameArchivoCertProveVehiculo.length());
                        fileNameArchivoCertProveVehiculo = fileNameArchivoCertProveVehiculo.toUpperCase(); 
                        regSspCertifProveeVCP.setArchivoCertProv(fileNameArchivoCertProveVehiculo);
                    }
                    
                    regSspCertifProveeVCP.setActivo(JsfUtil.TRUE);
                    regSspCertifProveeVCP.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspCertifProveeVCP.setAudNumIp(JsfUtil.getIpAddress());
                    regSspCertifProveeVCP = (SspCertifProvee) JsfUtil.entidadMayusculas(regSspCertifProveeVCP, "");
                    ejbSspCertifProveeFacade.edit(regSspCertifProveeVCP);
                    
                    if(vehCertProvRegByte !=null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_CERTPROV").getValor() + regSspCertifProveeVCP.getArchivoCertProv()), vehCertProvRegByte);
                    }
                    
                    //=========== DATOS POLIZA DE SEGURO  =================== 
                    regSspPolizaVPoliza.setRegistroId(regSspRegistroSCVB);
                    regSspPolizaVPoliza.setRuc(txtPolizaNumRUCEmpAseguradora);
                    regSspPolizaVPoliza.setEmpresaAseguradora(txtPolizaNombreEmpAseguradora);
                    regSspPolizaVPoliza.setNroPoliza(txtPolizaNumPoliza);                
                    regSspPolizaVPoliza.setVigenciaIni(txtPolizaFechaDesde);
                    regSspPolizaVPoliza.setVigenciaFin(txtPolizaFechaHasta);
                    regSspPolizaVPoliza.setTipoMoneda(cboPolizaTipoMonedaSelected);
                    regSspPolizaVPoliza.setMontoMaximo(txtPolizaMontoMaximo);
                    
                    if(vehPolizaRegByte != null){
                        //Generamos Nombre Doc Certificado Proveedor
                        String fileNameArchivoPolizaSegVehiculo = "";
                        fileNameArchivoPolizaSegVehiculo = vehPolizaNomArchivo;
                        fileNameArchivoPolizaSegVehiculo = "SCVBGssp_POLIZASEGVEH_" + ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_SEL_POLIZA").toString() + fileNameArchivoPolizaSegVehiculo.substring(fileNameArchivoPolizaSegVehiculo.lastIndexOf('.'), fileNameArchivoPolizaSegVehiculo.length());
                        fileNameArchivoPolizaSegVehiculo = fileNameArchivoPolizaSegVehiculo.toUpperCase();
                        regSspPolizaVPoliza.setArchivoPoliza(fileNameArchivoPolizaSegVehiculo);
                    }
                    
                    regSspPolizaVPoliza.setBienes(vehPolizaBienesCustodia);
                    regSspPolizaVPoliza.setActivo(JsfUtil.TRUE);
                    regSspPolizaVPoliza.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    regSspPolizaVPoliza.setAudNumIp(JsfUtil.getIpAddress());
                    regSspPolizaVPoliza = (SspPoliza) JsfUtil.entidadMayusculas(regSspPolizaVPoliza, "");
                    ejbSspPolizaFacade.edit(regSspPolizaVPoliza);
                    
                    if(vehPolizaRegByte != null){
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_POLIZAVEH").getValor() + regSspPolizaVPoliza.getArchivoPoliza()), vehPolizaRegByte);
                    }

                    SspRegistroEvento registroEvento = new SspRegistroEvento();
                    registroEvento.setId(null);
                    registroEvento.setRegistroId(regSspRegistroSCVB);
                    registroEvento.setTipoEventoId(regSspRegistroSCVB.getEstadoId());
                    registroEvento.setObservacion("Crear edita la solicitud de certificación de vehículos blindados.");
                    registroEvento.setFecha(new Date());
                    registroEvento.setActivo(JsfUtil.TRUE);
                    registroEvento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    registroEvento.setAudNumIp(JsfUtil.getIpAddress());
                    registroEvento = (SspRegistroEvento) JsfUtil.entidadMayusculas(registroEvento, "");
                    ejbSspRegistroEventoFacade.create(registroEvento);
                    
                 }else{
                    System.out.println("estado_03->"+estado);
                 }            
                
                
                //=================================================================
                
                int contProceso = 0;
                blnProcesoRegSCVB = false;
                totalProcesadosSCVB = 3;

                for (int i = 1; i <= totalProcesadosSCVB; i++) {
                    actualProcesadoSCVB++;
                    contProceso++;           
                    Thread.sleep(1000); //Pause for 1 seconds
                }

                if (contProceso > 0) {
                    if(estado.equals(EstadoCrud.CREARCERTVEHBLIND)){//NUEVO
                        System.out.println("Se registro con RegistroId: "+regSspRegistroSCVB.getId());
                        JsfUtil.mensaje("Se registró correctamente con ID:"+regSspRegistroSCVB.getId());
                    }else if(estado.equals(EstadoCrud.EDITARCERTVEHBLIND)){//editar
                        System.out.println("Se actualizó el RegistroId: "+regSspRegistroSCVB.getId());
                        JsfUtil.mensaje("Se actualizó correctamente el ID:"+regSspRegistroSCVB.getId());
                    }                    
                    URL_Formulario = "/aplicacion/gssp/gsspCertificacion/ListarSolicCertificacionBandeja";
                } else {
                    JsfUtil.mensajeAdvertencia("No se procesó el registro.");
                }

             }
             
         } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        return URL_Formulario;
        
     }
     
    public void fnValidarFechaMinimaHasta_CertProv(){
        fechaMinimaCalendario_CertProv = txtCertProvFechaIni;
    }
    
    public void fnValidarFechaMinimaHasta_Poliza(){
        fechaMinimaCalendario_Poliza = txtPolizaFechaDesde;
    }
    
     
    public String obtenerNroSolicitudEmision() {
        String nroSolicitud = "";
        nroSolicitud = ejbSspRegistroFacade.obtenerNroSolicitudEmisionRegistro() + "-" + JsfUtil.mostrarAnio(new Date()) + "-SUCAMEC/GSSP-EC";
        return nroSolicitud;
    }
    
    public String obtenerMsjeInvalidSizeFileMaximo(String nombreSbParametro) {
        return "Solo se permite subir archivos con un maximo de " + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre(nombreSbParametro).getValor()) / 1024) + " Kb. ";
    } 
    
    public void handleFileUploadPDF_VEHPARTREG(FileUploadEvent event) {
        try {
            if (event != null) {
                vehPartRegUploadedFile = event.getFile();
                if (JsfUtil.verificarPDF(vehPartRegUploadedFile)) {
                    vehPartRegByte = IOUtils.toByteArray(vehPartRegUploadedFile.getInputstream());
                    vehPartRegArchivo = new DefaultStreamedContent(vehPartRegUploadedFile.getInputstream(), "application/pdf");
                    vehPartRegNomArchivo = vehPartRegUploadedFile.getFileName();
                } else {
                    vehPartRegUploadedFile = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public StreamedContent fnObtenerArchivoVEHPARTREG() {        
        StreamedContent r = null;
        String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_PRVEH").getValor(); 
        String xNomArchivo = regSspVehiculoCertificacionVC.getArchPartidaRegistral();        
        try {
            FileInputStream f = new FileInputStream(path + xNomArchivo);
            r = new DefaultStreamedContent(f, "application/pdf", xNomArchivo);
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    public void handleFileUploadPDF_VEHCONTRATARRENDA(FileUploadEvent event) {
        try {
            if (event != null) {
                vehContraArrendaUploadedFile = event.getFile();
                if (JsfUtil.verificarPDF(vehContraArrendaUploadedFile)) {
                    vehContraArrendaRegByte = IOUtils.toByteArray(vehContraArrendaUploadedFile.getInputstream());
                    vehContraArrendaArchivo = new DefaultStreamedContent(vehContraArrendaUploadedFile.getInputstream(), "application/pdf");
                    vehContraArrendaNomArchivo = vehContraArrendaUploadedFile.getFileName();
                } else {
                    vehContraArrendaUploadedFile = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public StreamedContent fnObtenerArchivoVEHCONTRATARRENDA() {        
        StreamedContent r = null;
        String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_CAVEH").getValor(); 
        String xNomArchivo = regSspVehiculoCertificacionVC.getArchContratArrenda();  
        try {
            FileInputStream f = new FileInputStream(path + xNomArchivo);
            r = new DefaultStreamedContent(f, "application/pdf", xNomArchivo);
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    public void handleFileUploadPDF_VEHCERTPROVEEDOR(FileUploadEvent event) {
        try {
            if (event != null) {
                vehCertProvUploadedFile = event.getFile();
                if (JsfUtil.verificarPDF(vehCertProvUploadedFile)) {
                    vehCertProvRegByte = IOUtils.toByteArray(vehCertProvUploadedFile.getInputstream());
                    vehCertProvArchivo = new DefaultStreamedContent(vehCertProvUploadedFile.getInputstream(), "application/pdf");
                    vehCertProvNomArchivo= vehCertProvUploadedFile.getFileName();
                } else {
                    vehCertProvUploadedFile = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public StreamedContent fnObtenerArchivoVEHCERTPROVEEDOR() {        
        StreamedContent r = null;
        String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_CERTPROV").getValor(); 
        String xNomArchivo = regSspCertifProveeVCP.getArchivoCertProv();  
        try {
            FileInputStream f = new FileInputStream(path + xNomArchivo);
            r = new DefaultStreamedContent(f, "application/pdf", xNomArchivo);
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    public void handleFileUploadPDF_VEHPOLIZASEG(FileUploadEvent event) {
        try {
            if (event != null) {
                vehPolizaUploadedFile = event.getFile();
                if (JsfUtil.verificarPDF(vehPolizaUploadedFile)) {
                    vehPolizaRegByte= IOUtils.toByteArray(vehPolizaUploadedFile.getInputstream());
                    vehPolizaArchivo = new DefaultStreamedContent(vehPolizaUploadedFile.getInputstream(), "application/pdf");
                    vehPolizaNomArchivo= vehPolizaUploadedFile.getFileName();
                } else {
                    vehPolizaUploadedFile = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF original");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public StreamedContent fnObtenerArchivoVEHPOLIZASEG() {        
        StreamedContent r = null;
        String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_SCVBGssp_POLIZAVEH").getValor(); 
        String xNomArchivo = regSspPolizaVPoliza.getArchivoPoliza();  
        try {
            FileInputStream f = new FileInputStream(path + xNomArchivo);
            r = new DefaultStreamedContent(f, "application/pdf", xNomArchivo);
        } catch (FileNotFoundException ex) {
            r = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.errorDescarga("No se encontro el archivo", ex);
        }
        return r;
    }
    
    
    public void fnMostrarRenderArrendatario() {
        System.out.println("vehArrendado->"+vehArrendado);
        
        if(vehArrendado.equals(JsfUtil.TRUE)){
            renderArrendatario = true;
        }else{
            renderArrendatario = false;
        }
    }
    
    public String fnCancelarSolicitud(){
        
        System.out.println("estado->"+estado);
        String URL_Formulario = null;
        if(estado.equals(EstadoCrud.CREARCERTVEHBLIND)){//NUEVO
            mostrarFormCrearSolicitudCertificacionVB();
        }else if(estado.equals(EstadoCrud.EDITARCERTVEHBLIND) || estado.equals(EstadoCrud.VERCERTVEHBLIND)){//EDITAR O VER
            URL_Formulario = "/aplicacion/gssp/gsspCertificacion/ListarSolicCertificacionBandeja";
        }                    
        return URL_Formulario;
    }
     
    //================================================
    //============ METODOS GET Y SET =================
    //================================================

    public SspRegistro getRegistroResolucionServTransYCustDineroValores() {
        return registroResolucionServTransYCustDineroValores;
    }

    public void setRegistroResolucionServTransYCustDineroValores(SspRegistro registroResolucionServTransYCustDineroValores) {
        this.registroResolucionServTransYCustDineroValores = registroResolucionServTransYCustDineroValores;
    }

    public boolean isRenderMostrarFormSolicCertVehBlindados() {
        return renderMostrarFormSolicCertVehBlindados;
    }

    public void setRenderMostrarFormSolicCertVehBlindados(boolean renderMostrarFormSolicCertVehBlindados) {
        this.renderMostrarFormSolicCertVehBlindados = renderMostrarFormSolicCertVehBlindados;
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

    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
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

    public HashMap getEmptyModel_vistaDataServTranspCustodiaDineroYValores() {
        return emptyModel_vistaDataServTranspCustodiaDineroYValores;
    }

    public void setEmptyModel_vistaDataServTranspCustodiaDineroYValores(HashMap emptyModel_vistaDataServTranspCustodiaDineroYValores) {
        this.emptyModel_vistaDataServTranspCustodiaDineroYValores = emptyModel_vistaDataServTranspCustodiaDineroYValores;
    }

    public List<PersonaDetalle> getPersonaDetalleListado() {
        return personaDetalleListado;
    }

    public void setPersonaDetalleListado(List<PersonaDetalle> personaDetalleListado) {
        this.personaDetalleListado = personaDetalleListado;
    }
    

    public List<TipoBaseGt> getCboDlgTipoDocLista() {
        return cboDlgTipoDocLista;
    }

    public void setCboDlgTipoDocLista(List<TipoBaseGt> cboDlgTipoDocLista) {
        this.cboDlgTipoDocLista = cboDlgTipoDocLista;
    }

    public TipoBaseGt getCboDlgTipoDocSelected() {
        return cboDlgTipoDocSelected;
    }

    public void setCboDlgTipoDocSelected(TipoBaseGt cboDlgTipoDocSelected) {
        this.cboDlgTipoDocSelected = cboDlgTipoDocSelected;
    }

    public List<TipoBaseGt> getCboZonaRegistralRLLista() {
        return cboZonaRegistralRLLista;
    }

    public void setCboZonaRegistralRLLista(List<TipoBaseGt> cboZonaRegistralRLLista) {
        this.cboZonaRegistralRLLista = cboZonaRegistralRLLista;
    }

    public TipoBaseGt getCboZonaRegistralRLSelected() {
        return cboZonaRegistralRLSelected;
    }

    public void setCboZonaRegistralRLSelected(TipoBaseGt cboZonaRegistralRLSelected) {
        this.cboZonaRegistralRLSelected = cboZonaRegistralRLSelected;
    }

    public List<TipoBaseGt> getCboOficinaRegistralRLLista() {
        return cboOficinaRegistralRLLista;
    }

    public void setCboOficinaRegistralRLLista(List<TipoBaseGt> cboOficinaRegistralRLLista) {
        this.cboOficinaRegistralRLLista = cboOficinaRegistralRLLista;
    }

    public TipoBaseGt getCboOficinaRegistralRLSelected() {
        return cboOficinaRegistralRLSelected;
    }

    public void setCboOficinaRegistralRLSelected(TipoBaseGt cboOficinaRegistralRLSelected) {
        this.cboOficinaRegistralRLSelected = cboOficinaRegistralRLSelected;
    }

    public List<TipoBaseGt> getCboTipoUbicacionRLLista() {
        return cboTipoUbicacionRLLista;
    }

    public void setCboTipoUbicacionRLLista(List<TipoBaseGt> cboTipoUbicacionRLLista) {
        this.cboTipoUbicacionRLLista = cboTipoUbicacionRLLista;
    }

    public TipoBaseGt getCboTipoUbicacionRLSelected() {
        return cboTipoUbicacionRLSelected;
    }

    public void setCboTipoUbicacionRLSelected(TipoBaseGt cboTipoUbicacionRLSelected) {
        this.cboTipoUbicacionRLSelected = cboTipoUbicacionRLSelected;
    }

    public List<SbDistritoGt> getCboDistritoRLLista() {
        return cboDistritoRLLista;
    }

    public void setCboDistritoRLLista(List<SbDistritoGt> cboDistritoRLLista) {
        this.cboDistritoRLLista = cboDistritoRLLista;
    }

    public SbDistritoGt getCboDistritoRLSelected() {
        return cboDistritoRLSelected;
    }

    public void setCboDistritoRLSelected(SbDistritoGt cboDistritoRLSelected) {
        this.cboDistritoRLSelected = cboDistritoRLSelected;
    }
    
    public ArrayList<SspContacto> getContactoListaActivas() {
        ArrayList<SspContacto> xListaContactoActualizada = new ArrayList<SspContacto>();
        for (SspContacto itemContacto : contactoLista) {                    
            if(itemContacto.getActivo() == JsfUtil.TRUE) {
                xListaContactoActualizada.add(itemContacto);
            }                    
        }
        return xListaContactoActualizada;
    }

    public ArrayList<SspContacto> getContactoLista() {
        return contactoLista;
    }

    public void setContactoLista(ArrayList<SspContacto> contactoLista) {
        this.contactoLista = contactoLista;
    }

    public SspContacto getContactoSelected() {
        return contactoSelected;
    }

    public void setContactoSelected(SspContacto contactoSelected) {
        this.contactoSelected = contactoSelected;
    }

    public List<TipoBaseGt> getCboDlgPrioridadLista() {
        return cboDlgPrioridadLista;
    }

    public void setCboDlgPrioridadLista(List<TipoBaseGt> cboDlgPrioridadLista) {
        this.cboDlgPrioridadLista = cboDlgPrioridadLista;
    }

    public TipoBaseGt getCboDlgPrioridadSelected() {
        return cboDlgPrioridadSelected;
    }

    public void setCboDlgPrioridadSelected(TipoBaseGt cboDlgPrioridadSelected) {
        this.cboDlgPrioridadSelected = cboDlgPrioridadSelected;
    }

    public List<TipoBaseGt> getCboDlgMedioContactoLista() {
        return cboDlgMedioContactoLista;
    }

    public void setCboDlgMedioContactoLista(List<TipoBaseGt> cboDlgMedioContactoLista) {
        this.cboDlgMedioContactoLista = cboDlgMedioContactoLista;
    }

    public TipoBaseGt getCboDlgMedioContactoSelected() {
        return cboDlgMedioContactoSelected;
    }

    public void setCboDlgMedioContactoSelected(TipoBaseGt cboDlgMedioContactoSelected) {
        this.cboDlgMedioContactoSelected = cboDlgMedioContactoSelected;
    }

    public String getDlgtextoContacto() {
        return dlgtextoContacto;
    }

    public void setDlgtextoContacto(String dlgtextoContacto) {
        this.dlgtextoContacto = dlgtextoContacto;
    }

    public String getDlgLabelContacto() {
        return dlgLabelContacto;
    }

    public void setDlgLabelContacto(String dlgLabelContacto) {
        this.dlgLabelContacto = dlgLabelContacto;
    }
    
    

    public boolean isDlgFragmentContacto() {
        return dlgFragmentContacto;
    }

    public void setDlgFragmentContacto(boolean dlgFragmentContacto) {
        this.dlgFragmentContacto = dlgFragmentContacto;
    }

    public String getTxtDlgNroDoc() {
        return txtDlgNroDoc;
    }

    public void setTxtDlgNroDoc(String txtDlgNroDoc) {
        this.txtDlgNroDoc = txtDlgNroDoc;
    }

    public String getTxtDlgNombre() {
        return txtDlgNombre;
    }

    public void setTxtDlgNombre(String txtDlgNombre) {
        this.txtDlgNombre = txtDlgNombre;
    }

    public String getTxtDlgApePat() {
        return txtDlgApePat;
    }

    public void setTxtDlgApePat(String txtDlgApePat) {
        this.txtDlgApePat = txtDlgApePat;
    }

    public String getTxtDlgApeMat() {
        return txtDlgApeMat;
    }

    public void setTxtDlgApeMat(String txtDlgApeMat) {
        this.txtDlgApeMat = txtDlgApeMat;
    }

    public Date getTxtDlgAFechaNac() {
        return txtDlgAFechaNac;
    }

    public void setTxtDlgAFechaNac(Date txtDlgAFechaNac) {
        this.txtDlgAFechaNac = txtDlgAFechaNac;
    }

    public String getTxtRLPartRegistral() {
        return txtRLPartRegistral;
    }

    public void setTxtRLPartRegistral(String txtRLPartRegistral) {
        this.txtRLPartRegistral = txtRLPartRegistral;
    }

    public String getTxtRLAsientoRegistral() {
        return txtRLAsientoRegistral;
    }

    public void setTxtRLAsientoRegistral(String txtRLAsientoRegistral) {
        this.txtRLAsientoRegistral = txtRLAsientoRegistral;
    }

    public String getTxtRLDomicilioLegal() {
        return txtRLDomicilioLegal;
    }

    public void setTxtRLDomicilioLegal(String txtRLDomicilioLegal) {
        this.txtRLDomicilioLegal = txtRLDomicilioLegal;
    }

    public boolean isBlnProcesoRegSCVB() {
        return blnProcesoRegSCVB;
    }

    public void setBlnProcesoRegSCVB(boolean blnProcesoRegSCVB) {
        this.blnProcesoRegSCVB = blnProcesoRegSCVB;
    }

    public Integer getActualProcesadoSCVB() {
        return actualProcesadoSCVB;
    }

    public void setActualProcesadoSCVB(Integer actualProcesadoSCVB) {
        this.actualProcesadoSCVB = actualProcesadoSCVB;
    }

    public Integer getProgressSCVB() {
        return progressSCVB;
    }

    public void setProgressSCVB(Integer progressSCVB) {
        this.progressSCVB = progressSCVB;
    }

    public Integer getTotalProcesadosSCVB() {
        return totalProcesadosSCVB;
    }

    public void setTotalProcesadosSCVB(Integer totalProcesadosSCVB) {
        this.totalProcesadosSCVB = totalProcesadosSCVB;
    }

    public SspRegistro getRegSspRegistroSCVB() {
        return regSspRegistroSCVB;
    }

    public void setRegSspRegistroSCVB(SspRegistro regSspRegistroSCVB) {
        this.regSspRegistroSCVB = regSspRegistroSCVB;
    }

    public SbPersonaGt getRegDatoPersonaByNumDocRL() {
        return regDatoPersonaByNumDocRL;
    }

    public void setRegDatoPersonaByNumDocRL(SbPersonaGt regDatoPersonaByNumDocRL) {
        this.regDatoPersonaByNumDocRL = regDatoPersonaByNumDocRL;
    }

    public String getPersonaDetalleSelectedString() {
        return personaDetalleSelectedString;
    }

    public void setPersonaDetalleSelectedString(String personaDetalleSelectedString) {
        this.personaDetalleSelectedString = personaDetalleSelectedString;
    }

    public boolean isExisteAsientoSunarpRL() {
        return existeAsientoSunarpRL;
    }

    public void setExisteAsientoSunarpRL(boolean existeAsientoSunarpRL) {
        this.existeAsientoSunarpRL = existeAsientoSunarpRL;
    }

    public SspVehiculoCertificacion getSspVehiculoCertificacion() {
        return sspVehiculoCertificacion;
    }

    public void setSspVehiculoCertificacion(SspVehiculoCertificacion sspVehiculoCertificacion) {
        this.sspVehiculoCertificacion = sspVehiculoCertificacion;
    }

    public String getVehCategoria() {
        return vehCategoria;
    }

    public void setVehCategoria(String vehCategoria) {
        this.vehCategoria = vehCategoria;
    }

    public String getVehCarroceria() {
        return vehCarroceria;
    }

    public void setVehCarroceria(String vehCarroceria) {
        this.vehCarroceria = vehCarroceria;
    }

    public String getVehPlaca() {
        return vehPlaca;
    }

    public void setVehPlaca(String vehPlaca) {
        this.vehPlaca = vehPlaca;
    }

    public String getVehSerie() {
        return vehSerie;
    }

    public void setVehSerie(String vehSerie) {
        this.vehSerie = vehSerie;
    }

    public String getVehMarca() {
        return vehMarca;
    }

    public void setVehMarca(String vehMarca) {
        this.vehMarca = vehMarca;
    }

    public String getVehModelo() {
        return vehModelo;
    }

    public void setVehModelo(String vehModelo) {
        this.vehModelo = vehModelo;
    }

    public Short getVehArrendado() {
        return vehArrendado;
    }

    public void setVehArrendado(Short vehArrendado) {
        this.vehArrendado = vehArrendado;
    }


    public String getTxtVehPartRegistral() {
        return txtVehPartRegistral;
    }

    public void setTxtVehPartRegistral(String txtVehPartRegistral) {
        this.txtVehPartRegistral = txtVehPartRegistral;
    }

    public String getTxtVehAsientoRegistral() {
        return txtVehAsientoRegistral;
    }

    public void setTxtVehAsientoRegistral(String txtVehAsientoRegistral) {
        this.txtVehAsientoRegistral = txtVehAsientoRegistral;
    }

    public List<TipoBaseGt> getCboVehZonaRegistralRLLista() {
        return cboVehZonaRegistralRLLista;
    }

    public void setCboVehZonaRegistralRLLista(List<TipoBaseGt> cboVehZonaRegistralRLLista) {
        this.cboVehZonaRegistralRLLista = cboVehZonaRegistralRLLista;
    }

    public TipoBaseGt getCboVehZonaRegistralRLSelected() {
        return cboVehZonaRegistralRLSelected;
    }

    public void setCboVehZonaRegistralRLSelected(TipoBaseGt cboVehZonaRegistralRLSelected) {
        this.cboVehZonaRegistralRLSelected = cboVehZonaRegistralRLSelected;
    }

    public List<TipoBaseGt> getCboVehOficinaRegistralRLLista() {
        return cboVehOficinaRegistralRLLista;
    }

    public void setCboVehOficinaRegistralRLLista(List<TipoBaseGt> cboVehOficinaRegistralRLLista) {
        this.cboVehOficinaRegistralRLLista = cboVehOficinaRegistralRLLista;
    }

    public TipoBaseGt getCboVehOficinaRegistralRLSelected() {
        return cboVehOficinaRegistralRLSelected;
    }

    public void setCboVehOficinaRegistralRLSelected(TipoBaseGt cboVehOficinaRegistralRLSelected) {
        this.cboVehOficinaRegistralRLSelected = cboVehOficinaRegistralRLSelected;
    }

    public List<SbDistritoGt> getCboVehSbDistritoGtLista() {
        return cboVehSbDistritoGtLista;
    }

    public void setCboVehSbDistritoGtLista(List<SbDistritoGt> cboVehSbDistritoGtLista) {
        this.cboVehSbDistritoGtLista = cboVehSbDistritoGtLista;
    }

    public SbDistritoGt getCboVehSbDistritoGtSelected() {
        return cboVehSbDistritoGtSelected;
    }

    public void setCboVehSbDistritoGtSelected(SbDistritoGt cboVehSbDistritoGtSelected) {
        this.cboVehSbDistritoGtSelected = cboVehSbDistritoGtSelected;
    }

    public SbAsientoSunarp getVehSbAsientoSunarp() {
        return vehSbAsientoSunarp;
    }

    public void setVehSbAsientoSunarp(SbAsientoSunarp vehSbAsientoSunarp) {
        this.vehSbAsientoSunarp = vehSbAsientoSunarp;
    }

    public UploadedFile getVehPartRegUploadedFile() {
        return vehPartRegUploadedFile;
    }

    public void setVehPartRegUploadedFile(UploadedFile vehPartRegUploadedFile) {
        this.vehPartRegUploadedFile = vehPartRegUploadedFile;
    }

    public byte[] getVehPartRegByte() {
        return vehPartRegByte;
    }

    public void setVehPartRegByte(byte[] vehPartRegByte) {
        this.vehPartRegByte = vehPartRegByte;
    }

    public StreamedContent getVehPartRegArchivo() {
        return vehPartRegArchivo;
    }

    public void setVehPartRegArchivo(StreamedContent vehPartRegArchivo) {
        this.vehPartRegArchivo = vehPartRegArchivo;
    }

    public String getVehPartRegNomArchivo() {
        return vehPartRegNomArchivo;
    }

    public void setVehPartRegNomArchivo(String vehPartRegNomArchivo) {
        this.vehPartRegNomArchivo = vehPartRegNomArchivo;
    }

    public String getVehPartRegNomArchivoAnterior() {
        return vehPartRegNomArchivoAnterior;
    }

    public void setVehPartRegNomArchivoAnterior(String vehPartRegNomArchivoAnterior) {
        this.vehPartRegNomArchivoAnterior = vehPartRegNomArchivoAnterior;
    }

    public UploadedFile getVehContraArrendaUploadedFile() {
        return vehContraArrendaUploadedFile;
    }

    public void setVehContraArrendaUploadedFile(UploadedFile vehContraArrendaUploadedFile) {
        this.vehContraArrendaUploadedFile = vehContraArrendaUploadedFile;
    }

    public byte[] getVehContraArrendaRegByte() {
        return vehContraArrendaRegByte;
    }

    public void setVehContraArrendaRegByte(byte[] vehContraArrendaRegByte) {
        this.vehContraArrendaRegByte = vehContraArrendaRegByte;
    }

    public StreamedContent getVehContraArrendaArchivo() {
        return vehContraArrendaArchivo;
    }

    public void setVehContraArrendaArchivo(StreamedContent vehContraArrendaArchivo) {
        this.vehContraArrendaArchivo = vehContraArrendaArchivo;
    }

    public String getVehContraArrendaNomArchivo() {
        return vehContraArrendaNomArchivo;
    }

    public void setVehContraArrendaNomArchivo(String vehContraArrendaNomArchivo) {
        this.vehContraArrendaNomArchivo = vehContraArrendaNomArchivo;
    }

    public String getVehContraArrendaNomArchivoAnterior() {
        return vehContraArrendaNomArchivoAnterior;
    }

    public void setVehContraArrendaNomArchivoAnterior(String vehContraArrendaNomArchivoAnterior) {
        this.vehContraArrendaNomArchivoAnterior = vehContraArrendaNomArchivoAnterior;
    }

    public boolean isRenderArrendatario() {
        return renderArrendatario;
    }

    public void setRenderArrendatario(boolean renderArrendatario) {
        this.renderArrendatario = renderArrendatario;
    }

    public List<SbPaisGt> getCboCertProvPaisLista() {
        return cboCertProvPaisLista;
    }

    public void setCboCertProvPaisLista(List<SbPaisGt> cboCertProvPaisLista) {
        this.cboCertProvPaisLista = cboCertProvPaisLista;
    }

    public SbPaisGt getCboCertProvPaisSelected() {
        return cboCertProvPaisSelected;
    }

    public void setCboCertProvPaisSelected(SbPaisGt cboCertProvPaisSelected) {
        this.cboCertProvPaisSelected = cboCertProvPaisSelected;
    }

    public String getTxtCertProvNroDocCertificadora() {
        return txtCertProvNroDocCertificadora;
    }

    public void setTxtCertProvNroDocCertificadora(String txtCertProvNroDocCertificadora) {
        this.txtCertProvNroDocCertificadora = txtCertProvNroDocCertificadora;
    }

    public String getTxtCertProvNomEmpCertificadora() {
        return txtCertProvNomEmpCertificadora;
    }

    public void setTxtCertProvNomEmpCertificadora(String txtCertProvNomEmpCertificadora) {
        this.txtCertProvNomEmpCertificadora = txtCertProvNomEmpCertificadora;
    }

    public Date getTxtCertProvFechaFin() {
        return txtCertProvFechaFin;
    }

    public void setTxtCertProvFechaFin(Date txtCertProvFechaFin) {
        this.txtCertProvFechaFin = txtCertProvFechaFin;
    }

    public String getTxtCertNumeroCert() {
        return txtCertNumeroCert;
    }

    public void setTxtCertNumeroCert(String txtCertNumeroCert) {
        this.txtCertNumeroCert = txtCertNumeroCert;
    }

    public UploadedFile getVehCertProvUploadedFile() {
        return vehCertProvUploadedFile;
    }

    public void setVehCertProvUploadedFile(UploadedFile vehCertProvUploadedFile) {
        this.vehCertProvUploadedFile = vehCertProvUploadedFile;
    }

    public byte[] getVehCertProvRegByte() {
        return vehCertProvRegByte;
    }

    public void setVehCertProvRegByte(byte[] vehCertProvRegByte) {
        this.vehCertProvRegByte = vehCertProvRegByte;
    }

    public StreamedContent getVehCertProvArchivo() {
        return vehCertProvArchivo;
    }

    public void setVehCertProvArchivo(StreamedContent vehCertProvArchivo) {
        this.vehCertProvArchivo = vehCertProvArchivo;
    }

    public String getVehCertProvNomArchivo() {
        return vehCertProvNomArchivo;
    }

    public void setVehCertProvNomArchivo(String vehCertProvNomArchivo) {
        this.vehCertProvNomArchivo = vehCertProvNomArchivo;
    }

    public String getVehCertProvNomArchivoAnterior() {
        return vehCertProvNomArchivoAnterior;
    }

    public void setVehCertProvNomArchivoAnterior(String vehCertProvNomArchivoAnterior) {
        this.vehCertProvNomArchivoAnterior = vehCertProvNomArchivoAnterior;
    }

    public Date getTxtCertProvFechaIni() {
        return txtCertProvFechaIni;
    }

    public void setTxtCertProvFechaIni(Date txtCertProvFechaIni) {
        this.txtCertProvFechaIni = txtCertProvFechaIni;
    }

    public String getCboCertProvPaisSelectedStringID() {
        return cboCertProvPaisSelectedStringID;
    }

    public void setCboCertProvPaisSelectedStringID(String cboCertProvPaisSelectedStringID) {
        this.cboCertProvPaisSelectedStringID = cboCertProvPaisSelectedStringID;
    }

    public String getTxtPolizaNumRUCEmpAseguradora() {
        return txtPolizaNumRUCEmpAseguradora;
    }

    public void setTxtPolizaNumRUCEmpAseguradora(String txtPolizaNumRUCEmpAseguradora) {
        this.txtPolizaNumRUCEmpAseguradora = txtPolizaNumRUCEmpAseguradora;
    }

    public String getTxtPolizaNombreEmpAseguradora() {
        return txtPolizaNombreEmpAseguradora;
    }

    public void setTxtPolizaNombreEmpAseguradora(String txtPolizaNombreEmpAseguradora) {
        this.txtPolizaNombreEmpAseguradora = txtPolizaNombreEmpAseguradora;
    }

    public String getTxtPolizaNumPoliza() {
        return txtPolizaNumPoliza;
    }

    public void setTxtPolizaNumPoliza(String txtPolizaNumPoliza) {
        this.txtPolizaNumPoliza = txtPolizaNumPoliza;
    }

    public Date getTxtPolizaFechaDesde() {
        return txtPolizaFechaDesde;
    }

    public void setTxtPolizaFechaDesde(Date txtPolizaFechaDesde) {
        this.txtPolizaFechaDesde = txtPolizaFechaDesde;
    }

    public Date getTxtPolizaFechaHasta() {
        return txtPolizaFechaHasta;
    }

    public void setTxtPolizaFechaHasta(Date txtPolizaFechaHasta) {
        this.txtPolizaFechaHasta = txtPolizaFechaHasta;
    }

    public List<TipoBaseGt> getCboPolizaTipoMonedaLista() {
        return cboPolizaTipoMonedaLista;
    }

    public void setCboPolizaTipoMonedaLista(List<TipoBaseGt> cboPolizaTipoMonedaLista) {
        this.cboPolizaTipoMonedaLista = cboPolizaTipoMonedaLista;
    }

    public TipoBaseGt getCboPolizaTipoMonedaSelected() {
        return cboPolizaTipoMonedaSelected;
    }

    public void setCboPolizaTipoMonedaSelected(TipoBaseGt cboPolizaTipoMonedaSelected) {
        this.cboPolizaTipoMonedaSelected = cboPolizaTipoMonedaSelected;
    }

    public BigDecimal getTxtPolizaMontoMaximo() {
        return txtPolizaMontoMaximo;
    }

    public void setTxtPolizaMontoMaximo(BigDecimal txtPolizaMontoMaximo) {
        this.txtPolizaMontoMaximo = txtPolizaMontoMaximo;
    }

    public UploadedFile getVehPolizaUploadedFile() {
        return vehPolizaUploadedFile;
    }

    public void setVehPolizaUploadedFile(UploadedFile vehPolizaUploadedFile) {
        this.vehPolizaUploadedFile = vehPolizaUploadedFile;
    }

    public byte[] getVehPolizaRegByte() {
        return vehPolizaRegByte;
    }

    public void setVehPolizaRegByte(byte[] vehPolizaRegByte) {
        this.vehPolizaRegByte = vehPolizaRegByte;
    }

    public StreamedContent getVehPolizaArchivo() {
        return vehPolizaArchivo;
    }

    public void setVehPolizaArchivo(StreamedContent vehPolizaArchivo) {
        this.vehPolizaArchivo = vehPolizaArchivo;
    }

    public String getVehPolizaNomArchivo() {
        return vehPolizaNomArchivo;
    }

    public void setVehPolizaNomArchivo(String vehPolizaNomArchivo) {
        this.vehPolizaNomArchivo = vehPolizaNomArchivo;
    }

    public String getVehPolizaNomArchivoAnterior() {
        return vehPolizaNomArchivoAnterior;
    }

    public void setVehPolizaNomArchivoAnterior(String vehPolizaNomArchivoAnterior) {
        this.vehPolizaNomArchivoAnterior = vehPolizaNomArchivoAnterior;
    }

    public String getVehPolizaBienesCustodia() {
        return vehPolizaBienesCustodia;
    }

    public void setVehPolizaBienesCustodia(String vehPolizaBienesCustodia) {
        this.vehPolizaBienesCustodia = vehPolizaBienesCustodia;
    }

    public SbPartidaSunarp getRegSbPartidaSunarpRL() {
        return regSbPartidaSunarpRL;
    }

    public void setRegSbPartidaSunarpRL(SbPartidaSunarp regSbPartidaSunarpRL) {
        this.regSbPartidaSunarpRL = regSbPartidaSunarpRL;
    }

    public SspRepresentanteRegistro getRegSspRepresentanteRegistroRL() {
        return regSspRepresentanteRegistroRL;
    }

    public void setRegSspRepresentanteRegistroRL(SspRepresentanteRegistro regSspRepresentanteRegistroRL) {
        this.regSspRepresentanteRegistroRL = regSspRepresentanteRegistroRL;
    }

    public SbAsientoPersona getRegSbAsientoPersonaRL() {
        return regSbAsientoPersonaRL;
    }

    public void setRegSbAsientoPersonaRL(SbAsientoPersona regSbAsientoPersonaRL) {
        this.regSbAsientoPersonaRL = regSbAsientoPersonaRL;
    }

    public SbAsientoSunarp getRegSbAsientoSunarpRL() {
        return regSbAsientoSunarpRL;
    }

    public void setRegSbAsientoSunarpRL(SbAsientoSunarp regSbAsientoSunarpRL) {
        this.regSbAsientoSunarpRL = regSbAsientoSunarpRL;
    }

    public SbDireccionGt getRegSbDireccionGtRL() {
        return regSbDireccionGtRL;
    }

    public void setRegSbDireccionGtRL(SbDireccionGt regSbDireccionGtRL) {
        this.regSbDireccionGtRL = regSbDireccionGtRL;
    }

    public SspVehiculoCertificacion getRegSspVehiculoCertificacionVC() {
        return regSspVehiculoCertificacionVC;
    }

    public void setRegSspVehiculoCertificacionVC(SspVehiculoCertificacion regSspVehiculoCertificacionVC) {
        this.regSspVehiculoCertificacionVC = regSspVehiculoCertificacionVC;
    }

    public SbAsientoSunarp getRegSbAsientoSunarpVC() {
        return regSbAsientoSunarpVC;
    }

    public void setRegSbAsientoSunarpVC(SbAsientoSunarp regSbAsientoSunarpVC) {
        this.regSbAsientoSunarpVC = regSbAsientoSunarpVC;
    }

    public SbPartidaSunarp getRegSbPartidaSunarpVC() {
        return regSbPartidaSunarpVC;
    }

    public void setRegSbPartidaSunarpVC(SbPartidaSunarp regSbPartidaSunarpVC) {
        this.regSbPartidaSunarpVC = regSbPartidaSunarpVC;
    }

    public SspCertifProvee getRegSspCertifProveeVCP() {
        return regSspCertifProveeVCP;
    }

    public void setRegSspCertifProveeVCP(SspCertifProvee regSspCertifProveeVCP) {
        this.regSspCertifProveeVCP = regSspCertifProveeVCP;
    }

    public SspPoliza getRegSspPolizaVPoliza() {
        return regSspPolizaVPoliza;
    }

    public void setRegSspPolizaVPoliza(SspPoliza regSspPolizaVPoliza) {
        this.regSspPolizaVPoliza = regSspPolizaVPoliza;
    }

    public Date getFechaMinimaCalendario_CertProv() {
        return fechaMinimaCalendario_CertProv;
    }

    public void setFechaMinimaCalendario_CertProv(Date fechaMinimaCalendario_CertProv) {
        this.fechaMinimaCalendario_CertProv = fechaMinimaCalendario_CertProv;
    }

    public Date getFechaMinimaCalendario_Poliza() {
        return fechaMinimaCalendario_Poliza;
    }

    public void setFechaMinimaCalendario_Poliza(Date fechaMinimaCalendario_Poliza) {
        this.fechaMinimaCalendario_Poliza = fechaMinimaCalendario_Poliza;
    }

    public boolean isFormHabilitarSoloLectura() {
        return formHabilitarSoloLectura;
    }

    public void setFormHabilitarSoloLectura(boolean formHabilitarSoloLectura) {
        this.formHabilitarSoloLectura = formHabilitarSoloLectura;
    }
    
    
    
}
