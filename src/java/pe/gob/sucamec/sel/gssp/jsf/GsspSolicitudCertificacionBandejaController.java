/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.jsf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.notificacion.data.NeArchivo;
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

/**
 *
 * @author lbartolo
 */
@Named(value = "gsspSolicitudCertificacionBandejaController")
@SessionScoped
public class GsspSolicitudCertificacionBandejaController implements Serializable {
   
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
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspContactoFacade ejbSspContactoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRepresentanteRegistroFacade ejbSspRepresentanteRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspVehiculoCertificacionFacade ejbSspVehiculoCertificacionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCertifProveeFacade ejbSspCertifProveeFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspPolizaFacade ejbSspPolizaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroEventoFacade ejbSspRegistroEventoFacade;
    @EJB
    private pe.gob.sucamec.notificacion.beans.NeDocumentoFacade neDocumentoFacade;
    
    //==========================================================
    private boolean transBlnProcesoTransmision = false;
    private Integer transTotalProcesados = 0;
    private Integer transActualProcesado = 0;
    private Integer transProgress = 0;
    
    //=================== ADMINISTRADO =========================
    private SbPersonaGt regAdminist;
    private SbPersonaGt regUserAdminist;
    private String administRazonSocial;
    private String administRUC;
    
    private SspRegistro xSspRegistro;
    private String txtObservacionDesistimiento;
    
    private List<Map> resultados;
    private List<Map> resultadosSeleccionados;
    
    private String cboFiltroBuscarPor;
    private String txtFiltroNroDoc;
    
    private Date filtroFechaDesdeSelected;
    private Date filtroFechaHastaSelected;
    
    private Date fechaMinimaCalendario;
    private Date fechaMaximaCalendario;
    
    private List<TipoSeguridad> cboFiltroTipoEstadoListado;
    private TipoSeguridad cboFiltroTipoEstadoSelected;
    
    private List<TipoBaseGt> cboFiltroTipoCertificacionLista;
    private TipoBaseGt cboFiltroTipoCertificacionSelected;
    
    
    
    
    public String mostrarFormBandejaSolicitudCertificacion() {
        
        //=============================================
        //========    Administrado - inicio   =========
        //=============================================
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        regAdminist = ejbSbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        regUserAdminist = ejbSbPersonaFacade.buscarPersonaXUserLogin(p_user.getLogin());

        administRazonSocial = regAdminist.getRznSocial();
        administRUC = regAdminist.getRuc();
        
        fnLimpiarVariables();
        fnCargarListboxVariables();
        return "/aplicacion/gssp/gsspCertificacion/ListarSolicCertificacionBandeja";

    }
    
    public void fnLimpiarVariables(){
        
        resultados = new ArrayList();
        resultadosSeleccionados = new ArrayList();
        
        transBlnProcesoTransmision = false;
        transTotalProcesados = 0;
        transActualProcesado = 0;
        transProgress = 0;
        
        xSspRegistro = new SspRegistro();
        txtObservacionDesistimiento = null;
        
        cboFiltroBuscarPor = null;
        txtFiltroNroDoc = null;
        
        filtroFechaDesdeSelected = null;
        filtroFechaHastaSelected = null;
        
        fechaMinimaCalendario = null;
        fechaMaximaCalendario = null;
        
        cboFiltroTipoEstadoListado = new ArrayList<TipoSeguridad>();
        cboFiltroTipoEstadoSelected = null;
        
        cboFiltroTipoCertificacionLista = new ArrayList<TipoBaseGt>();
        cboFiltroTipoCertificacionSelected = null;
    }
    
    public void fnCargarListboxVariables(){
        
        calcularFechaMaximaCalendario();
        cboFiltroTipoEstadoListado = ejbTipoSeguridadFacade.listarTipoSeguridadXCodProgs("'TP_ECC_CRE', 'TP_ECC_TRA', 'TP_ECC_OBS', 'TP_ECC_NPR', 'TP_ECC_CAN', 'TP_ECC_APR', 'TP_ECC_DES', 'TP_ECC_ANU' ");
        cboFiltroTipoCertificacionLista = ejbTipoBaseFacade.listarTipoBaseXCodProgs("'TP_GSSP_CERT_CVB', 'TP_GSSP_CERT_CEF'");
    }
    
    public void calcularFechaMaximaCalendario() {
        Calendar FechaActual = Calendar.getInstance();
        fechaMaximaCalendario = JsfUtil.getFechaSinHora(FechaActual.getTime());
    }
    
    public void calcularFechaMinimaCalendario() {
        Calendar FechaActual = Calendar.getInstance();
        fechaMinimaCalendario = JsfUtil.getFechaSinHora(FechaActual.getTime());
    }
    
    
    public void buscarBandejaSolicitudCertificacionGral() {
        boolean validacion = true;
        
        if (cboFiltroBuscarPor != null) {
            if(txtFiltroNroDoc == null || txtFiltroNroDoc.equals("")){
                validacion = false;
                JsfUtil.invalidar("formBandejaCertificacion:txtFiltro_NroDoc");
                JsfUtil.mensajeAdvertencia("Ingrese número del documento.");
            }                    
        }
        
        if (cboFiltroBuscarPor == null) {
            if(txtFiltroNroDoc == null || txtFiltroNroDoc.equals("")){                
            } else{
                validacion = false;
                JsfUtil.invalidar("formBandejaCertificacion:cboFiltro_BuscarPor");
                JsfUtil.mensajeAdvertencia("Seleccione el tipo de documento a buscar.");
            }                  
        }
        
        if ((filtroFechaDesdeSelected != null && filtroFechaHastaSelected == null) || (filtroFechaDesdeSelected == null && filtroFechaHastaSelected != null)) {
            validacion = false;
            JsfUtil.invalidar("formBandejaCertificacion:txtFiltro_FechDesde");
            JsfUtil.invalidar("formBandejaCertificacion:txtFiltro_FechHasta");
            JsfUtil.mensajeAdvertencia("Es necesario seleccionar un rango de fecha");
        }
        
        if(validacion){
        
            try {
                
                System.out.println("Se buscar");
            
                HashMap mMap = new HashMap();
                mMap.put("filtroTipo", cboFiltroBuscarPor);
                mMap.put("filtroNumero", txtFiltroNroDoc);
                mMap.put("filtroTipoCertificacionId", (cboFiltroTipoCertificacionSelected != null) ? cboFiltroTipoCertificacionSelected.getId() : null);            
                mMap.put("filtroFechaIni", filtroFechaDesdeSelected);
                mMap.put("filtroFechaFin", filtroFechaHastaSelected);
                mMap.put("filtroTipoEstadoId", (cboFiltroTipoEstadoSelected != null) ? cboFiltroTipoEstadoSelected.getId() : null);
                mMap.put("filtroAdministradoId", (regAdminist != null) ? regAdminist.getId() : null);
                
                System.out.println(mMap);
                resultados = ejbSspRegistroFacade.buscarBandejaSolicitudCertificacionGral(mMap);
                /*
                //Date fecha = new java.util.Date("Mon Dec 15 00:00:00 CST 2014");
                DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
                DateFormat formatTo = new SimpleDateFormat("dd/MM/YY");
                Date date = (Date)formatter.parse(filtroFechaDesdeSelected.toString());
                String finalDate = formatTo.format(date);                
                System.out.println(finalDate); 
                */
                
                
                
            } catch (Exception e){
                e.printStackTrace();
            }
            
        }
        
    }
    
    public String estiloPorTiempo(String observacion, String codProg) {              

        if (observacion != null && codProg.equals("TP_ECC_OBS")) {
            return "datatable-row-alerta";
        }

        /*else if ((observacion == null || observacion.equals("")) && codProg.equals("TP_ECC_TRA")) {
            return "datatable-row-derivado";
        } */
        return "datatable-row-normal";
    }
    
    
    public boolean renderBtnTransmitir(Map item) {
        //
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE") || item.get("ESTADO_CODPROG").equals("TP_ECC_OBS")) {
                validar = true;
            }
        }
        return validar;
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
    
    public boolean renderBtnBorrar(Map item) {
        boolean validar = false;
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_CRE")) {
                validar = true;
            }
        }
        return validar;
    }
    
    public boolean renderBtnConstancia(Map item) {
        boolean validar = false;        
        if (item != null) {
            if (item.get("ESTADO_CODPROG").equals("TP_ECC_APR")) {//Solo APROBADO
                validar = true;
            }
        }
        return validar;
    }
    
    public StreamedContent imprimirConstancia(Map reg) {        
        NeArchivo a = neDocumentoFacade.archivoResolucionGSSP(Long.parseLong(reg.get("ID").toString()));
        if (a == null) {
            return null;
        }
        StreamedContent r = null;
        try {
            String path = pe.gob.sucamec.notificacion.jsf.util.JsfUtil.bundle("PathUpload");
            FileInputStream f = new FileInputStream(path + a.getNombre());
            r = new DefaultStreamedContent(f, "application/pdf", a.getNombre());
        } catch (FileNotFoundException ex) {
            r = null;
            JsfUtil.mensajeError(ex, "No se encontro el archivo");
        }
        return r;
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
    
    public void openDlgConfirmarDesestimiento(Map item) {
        if (item == null) {
            JsfUtil.mensajeError("No se puede generar el desestimiento");
            return;
        }

        xSspRegistro = ejbSspRegistroFacade.find(Long.parseLong(item.get("ID").toString()));
        RequestContext.getCurrentInstance().execute("PF('wvConfirmarDesestimiento').show()");
        RequestContext.getCurrentInstance().update("confirmarDesestimientoForm");
    }
    
    
    public void confirmarDesestimiento() {
        try {
            if (txtObservacionDesistimiento == null || txtObservacionDesistimiento.trim().isEmpty()) {
                JsfUtil.mensajeAdvertencia("Debe de registrar el motivo del desistimiento");
                return;
            }

            txtObservacionDesistimiento = txtObservacionDesistimiento.toUpperCase();
            TipoSeguridad xNuevoEstado = ejbTipoSeguridadFacade.tipoSeguridadXCodProg("TP_ECC_DES");

            if (generaTraza(xNuevoEstado)) {
                xSspRegistro.setEstadoId(xNuevoEstado);
                adicionaEvento(xSspRegistro, txtObservacionDesistimiento);
                ejbSspRegistroFacade.edit(xSspRegistro);

                xSspRegistro = null;
                txtObservacionDesistimiento = null;
                resultados = new ArrayList();
                resultadosSeleccionados = new ArrayList();
                buscarBandejaSolicitudCertificacionGral();                

                JsfUtil.mensaje("Se actualizado correctamente a DESISTIMIENTO.");
                RequestContext.getCurrentInstance().execute("PF('wvConfirmarDesestimiento').hide()");
                RequestContext.getCurrentInstance().update("formBandejaCertificacion");
            } else {
                JsfUtil.mensajeError("Hubo un error al archivar el expediente");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
    public boolean generaTraza(TipoSeguridad nuevoEstadoSolicitud) {
        boolean validacion = true;
        String loginUsuarioAct = "", asunto = "";

        try {
            if (nuevoEstadoSolicitud.getCodProg().equals("TP_ECC_DES") && xSspRegistro.getNroExpediente() != null) {
                loginUsuarioAct = ejbExpedienteFacade.obtenerUsuarioTrazaActualExpediente(xSspRegistro.getNroExpediente());
                asunto = nuevoEstadoSolicitud.getNombre() + " A SOLICITUD DEL ADMINISTRADO. " + txtObservacionDesistimiento;                             
                boolean estadoNE = wsTramDocController.archivarExpediente(xSspRegistro.getNroExpediente(), loginUsuarioAct, asunto); //ARCHIVAMIENTO DEL EXPEDIENTE   
                if (!estadoNE) {
                    JsfUtil.mensajeError("Ocurrió un error al archivar el expediente " + xSspRegistro.getNroExpediente());
                }
            } else {
                validacion = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            validacion = false;
            JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro ID: " + xSspRegistro.getId());
        }

        return validacion;
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
    
    
    public void borrarRegistro(Map item) {

        try {
            if (item != null) {
                
                SspRegistro reg = ejbSspRegistroFacade.find(Long.parseLong(item.get("ID").toString()));
                reg.setActivo(JsfUtil.FALSE);
                ejbSspRegistroFacade.edit(reg);

                //Actualiza como inactivos los contactos del Registro
                if (reg.getSspContactoList().size() > 0) {
                    for (SspContacto listaContacto : reg.getSspContactoList()) {
                        if (listaContacto.getActivo() == 1) {
                            listaContacto.setActivo(JsfUtil.FALSE);
                            ejbSspContactoFacade.edit(listaContacto);
                        }
                    }
                }
        

                //Actualiza como inactivo el Representante del Registro
                if (reg.getRepresentanteId() != null) {
                    SspRepresentanteRegistro xRepresentanteBusca = new SspRepresentanteRegistro();
                    xRepresentanteBusca = ejbSspRepresentanteRegistroFacade.representRegFindByRegistroId(reg.getId(), reg.getEmpresaId().getId());
                    if (xRepresentanteBusca != null) {
                        xRepresentanteBusca.setActivo(JsfUtil.FALSE);
                        ejbSspRepresentanteRegistroFacade.edit(xRepresentanteBusca);
                    }
                }


                //Actualiza como inactivo el Vehiculo Certificacion
                if (reg.getId() != null) {
                    SspVehiculoCertificacion xSspVehiculoCertificacion = new SspVehiculoCertificacion();
                    xSspVehiculoCertificacion = ejbSspVehiculoCertificacionFacade.buscarSspVehiculoCertificacionByRegistroId(reg.getId());
                    if (xSspVehiculoCertificacion != null) {
                        xSspVehiculoCertificacion.setActivo(JsfUtil.FALSE);
                        ejbSspVehiculoCertificacionFacade.edit(xSspVehiculoCertificacion);
                    }
                }
                
                //Actualiza como inactivo el Certificados del Proveedor
                if (reg.getId() != null) {
                    SspCertifProvee xSspCertifProvee = new SspCertifProvee();
                    xSspCertifProvee = ejbSspCertifProveeFacade.buscarSspCertifProveeByRegistroId(reg.getId());
                    if (xSspCertifProvee != null) {
                        xSspCertifProvee.setActivo(JsfUtil.FALSE);
                        ejbSspCertifProveeFacade.edit(xSspCertifProvee);
                    }
                }
                
                //Actualiza como inactivo el Poliza de Seguros
                if (reg.getId() != null) {
                    SspPoliza xSspPoliza = new SspPoliza();
                    xSspPoliza = ejbSspPolizaFacade.buscarSspPolizaByRegistroId(reg.getId());
                    if (xSspPoliza != null) {
                        xSspPoliza.setActivo(JsfUtil.FALSE);
                        ejbSspPolizaFacade.edit(xSspPoliza);
                    }
                }
                
                SspRegistroEvento registroEvento = new SspRegistroEvento();
                registroEvento.setId(null);
                registroEvento.setRegistroId(reg);
                registroEvento.setTipoEventoId(reg.getEstadoId());
                registroEvento.setObservacion("Crear anula la solicitud de certificación de vehículos blindados.");
                registroEvento.setFecha(new Date());
                registroEvento.setActivo(JsfUtil.TRUE);
                registroEvento.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registroEvento.setAudNumIp(JsfUtil.getIpAddress());
                registroEvento = (SspRegistroEvento) JsfUtil.entidadMayusculas(registroEvento, "");
                ejbSspRegistroEventoFacade.create(registroEvento);
                
                resultados.remove(item);
                resultadosSeleccionados.remove(item);
                JsfUtil.mensaje("Se ha borrado correctamente el registro.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorDePersistencia"));
        }

    }
    
    
    
    public Integer getProgressCompletarProceso() {
        if (transTotalProcesados == null) {
            transTotalProcesados = 0;
        }
        if (transActualProcesado == null) {
            transActualProcesado = 0;
        }
        if (transProgress == null) {
            transProgress = 0;
        } else if (transTotalProcesados > 0) {
            transProgress = ((100 * transActualProcesado) / transTotalProcesados);
            if (transProgress > 100) {
                transProgress = 100;
            }
            if (transProgress < 0) {
                transProgress = 0;
            }
        }
        return transProgress;
    }
    
    public void openDlgcompletarProceso(Map item) {
        resultadosSeleccionados = new ArrayList();
        resultadosSeleccionados.add(item);
        
        xSspRegistro = ejbSspRegistroFacade.find(Long.parseLong(item.get("ID").toString()));

        if (!resultadosSeleccionados.isEmpty()) {
            transProgress = 0;
            transActualProcesado = 0;
            transTotalProcesados = 0;
            transBlnProcesoTransmision = false;
            RequestContext.getCurrentInstance().execute("PF('wvDialogCompletarProceso').show()");
            RequestContext.getCurrentInstance().update("frmCompletarProceso");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar al menos un registro para transmitir");
        }
    }
    
    
    public void transmitirSolicitud() {
        if (!resultadosSeleccionados.isEmpty()) {

            int cont = 0;
            transBlnProcesoTransmision = true;
            transTotalProcesados = resultadosSeleccionados.size();

            for (Map item : resultadosSeleccionados) {
                transActualProcesado++;
                xSspRegistro = ejbSspRegistroFacade.find(Long.parseLong(item.get("ID").toString()));
                
                if (!validaTransmitir()) {
                    continue;
                }

                if (!guardarDatosTransmiteGeneraSolicitudCertificacion(xSspRegistro)) {
                    continue;
                }
                
                cont++;
                JsfUtil.mensaje("Se transmitió el registro Id: " + xSspRegistro.getId() + ", con Nro Solicitud: " + xSspRegistro.getNroSolicitiud());
                xSspRegistro = null;
            }
            
            
            xSspRegistro = null;
            txtObservacionDesistimiento = null;
            resultados = new ArrayList();
            resultadosSeleccionados = new ArrayList();
            buscarBandejaSolicitudCertificacionGral();            
            transBlnProcesoTransmision = true;

            RequestContext.getCurrentInstance().execute("PF('wvDialogCompletarProceso').hide()");
            RequestContext.getCurrentInstance().update("formBandejaCertificacion");
            if (cont > 0) {
                JsfUtil.mensaje("Se procesó " + cont + " registro(s) correctamente.");
            } else {
                JsfUtil.mensajeAdvertencia("No se procesó ningún registro.");
            }

        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar al menos un registro para transmitir");
        }
    }
    
    public boolean validaTransmitir() {
        if (xSspRegistro.getEstadoId().getCodProg().equals("TP_ECC_TRA")) {//TRANSMITIDO
            JsfUtil.mensajeAdvertencia("El registro con ID " + xSspRegistro.getId() + " ya ha sido transmitido");
            return false;
        }
        return true;
    }
    
    public boolean guardarDatosTransmiteGeneraSolicitudCertificacion(SspRegistro sspRegistro) {
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
                    strUsuarios = ejbSbParametroFacade.obtenerParametroXNombre("TransmiteAutorizaGSSP_usuTD_TP_AREA_TRAM").getValor();//ACASTILLO
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
            System.err.println("Exception ID:" + xSspRegistro.getId());
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
    
    //================================================
    //============== METODOS GET Y SET
    //================================================

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

    public String getCboFiltroBuscarPor() {
        return cboFiltroBuscarPor;
    }

    public void setCboFiltroBuscarPor(String cboFiltroBuscarPor) {
        this.cboFiltroBuscarPor = cboFiltroBuscarPor;
    }

    public String getTxtFiltroNroDoc() {
        return txtFiltroNroDoc;
    }

    public void setTxtFiltroNroDoc(String txtFiltroNroDoc) {
        this.txtFiltroNroDoc = txtFiltroNroDoc;
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

    public Date getFiltroFechaDesdeSelected() {
        return filtroFechaDesdeSelected;
    }

    public void setFiltroFechaDesdeSelected(Date filtroFechaDesdeSelected) {
        this.filtroFechaDesdeSelected = filtroFechaDesdeSelected;
    }

    public Date getFiltroFechaHastaSelected() {
        return filtroFechaHastaSelected;
    }

    public void setFiltroFechaHastaSelected(Date filtroFechaHastaSelected) {
        this.filtroFechaHastaSelected = filtroFechaHastaSelected;
    }

    public List<TipoSeguridad> getCboFiltroTipoEstadoListado() {
        return cboFiltroTipoEstadoListado;
    }

    public void setCboFiltroTipoEstadoListado(List<TipoSeguridad> cboFiltroTipoEstadoListado) {
        this.cboFiltroTipoEstadoListado = cboFiltroTipoEstadoListado;
    }

    public TipoSeguridad getCboFiltroTipoEstadoSelected() {
        return cboFiltroTipoEstadoSelected;
    }

    public void setCboFiltroTipoEstadoSelected(TipoSeguridad cboFiltroTipoEstadoSelected) {
        this.cboFiltroTipoEstadoSelected = cboFiltroTipoEstadoSelected;
    }

    public List<Map> getResultados() {
        return resultados;
    }

    public void setResultados(List<Map> resultados) {
        this.resultados = resultados;
    }

    public List<TipoBaseGt> getCboFiltroTipoCertificacionLista() {
        return cboFiltroTipoCertificacionLista;
    }

    public void setCboFiltroTipoCertificacionLista(List<TipoBaseGt> cboFiltroTipoCertificacionLista) {
        this.cboFiltroTipoCertificacionLista = cboFiltroTipoCertificacionLista;
    }

    public TipoBaseGt getCboFiltroTipoCertificacionSelected() {
        return cboFiltroTipoCertificacionSelected;
    }

    public void setCboFiltroTipoCertificacionSelected(TipoBaseGt cboFiltroTipoCertificacionSelected) {
        this.cboFiltroTipoCertificacionSelected = cboFiltroTipoCertificacionSelected;
    }

    public SspRegistro getxSspRegistro() {
        return xSspRegistro;
    }

    public void setxSspRegistro(SspRegistro xSspRegistro) {
        this.xSspRegistro = xSspRegistro;
    }

    public String getTxtObservacionDesistimiento() {
        return txtObservacionDesistimiento;
    }

    public void setTxtObservacionDesistimiento(String txtObservacionDesistimiento) {
        this.txtObservacionDesistimiento = txtObservacionDesistimiento;
    }

    public boolean isTransBlnProcesoTransmision() {
        return transBlnProcesoTransmision;
    }

    public void setTransBlnProcesoTransmision(boolean transBlnProcesoTransmision) {
        this.transBlnProcesoTransmision = transBlnProcesoTransmision;
    }

    public Integer getTransTotalProcesados() {
        return transTotalProcesados;
    }

    public void setTransTotalProcesados(Integer transTotalProcesados) {
        this.transTotalProcesados = transTotalProcesados;
    }

    public Integer getTransActualProcesado() {
        return transActualProcesado;
    }

    public void setTransActualProcesado(Integer transActualProcesado) {
        this.transActualProcesado = transActualProcesado;
    }

    public Integer getTransProgress() {
        return transProgress;
    }

    public void setTransProgress(Integer transProgress) {
        this.transProgress = transProgress;
    }

    public List<Map> getResultadosSeleccionados() {
        return resultadosSeleccionados;
    }

    public void setResultadosSeleccionados(List<Map> resultadosSeleccionados) {
        this.resultadosSeleccionados = resultadosSeleccionados;
    }
    
    
    
}
