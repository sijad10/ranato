package pe.gob.sucamec.bdintegrado.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import pe.gob.sucamec.bdintegrado.data.AmaRegEmpArma;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.AmaRegEmpProceso;
import pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbProcesoTupa;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtil;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.notificacion.beans.NeDocumentoFacade;
import pe.gob.sucamec.notificacion.data.NeDocumento;
import pe.gob.sucamec.rma1369.data.SitArma;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.ResPideMigra;

// Nombre de la instancia en la aplicacion //
@Named("amaRegEmpArmaController")
@SessionScoped

/**
 * Clase con AmaRegEmpArmaController instanciada como amaRegEmpArmaController.
 * Contiene funciones utiles para la entidad AmaRegEmpArma. Esta vinculada a las
 * páginas AmaRegEmpArma/create.xhtml, AmaRegEmpArma/update.xhtml,
 * AmaRegEmpArma/list.xhtml, AmaRegEmpArma/view.xhtml Nota: Las tablas deben
 * tener la estructura de Sucamec para que funcione adecuadamente, revisar si
 * tiene el campo activo y modificar las búsquedas.
 */
public class AmaRegEmpArmaController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    AmaRegEmpArma registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<AmaRegEmpArma> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<AmaRegEmpArma> registrosSeleccionados;

    private Map registroSelected;
    private String tipoBusqueda = null;
    private String tipoEstado = null;
    private boolean mostrarEstadoLicencia = false;
    private List<Map> resultadosArmas;
    private boolean acepto;
    private UploadedFile fileAdjunto1;
    private UploadedFile file;
    private StreamedContent foto;
    private byte[] fotoByte;
    private byte[] fileByte;
    private String opcionPanel1;
    private String opcionPanel2;
    private boolean disabledPanel1;
    private boolean disabledPanel2;
    private AmaModelos selectedArma;
    private String selectedArmaString;
    private String archivo = "";
    private List<Map> lstFotos;
    private boolean disabledBtnFoto;
    private int contPorEmitir;
    private int contNoPertenecen;
    private int contEmitidos;
    private boolean renderImprimir;
    private boolean renderRegistrar;
    private TipoGamac estadoSituacion;
    private TipoGamac situacionArma;
    private Date fechaIncautacion;
    private String observacion;
    private String armaEmitido;
    private int contParrafo1;
    private int contParrafo2;
    private int contParrafo3;
    private Date currentDate;
    private boolean renderEstadoArma;
    private Integer progress;
    private Integer totalProcesados;
    private Integer actualProcesado;
    private boolean renderProgressBar;
    private boolean errorArchivo = false;
    private boolean blnGenerarPdf;
    private String txtBtnImprimir;
    private AmaRegEmpProceso procesoHis;
    private Map datosActaInt;
    private boolean tieneInternamiento;
    private boolean disabledBtnGuardar;
    private String numDocPosiblePropietario;
    private String tipoDocPosiblePropietario;
    private Integer maxLengthPosiblePropietario;
    private String datosPosiblePropietario;
    private boolean habilitaJuridica;
    private Date fechaLimiteJuridica;
    ////////// bandeja de observaciones //////////
    private List<Map> resultadosBandeja;
    private List<Map> resultadosBandejaSeleccionados;
    private String descripOservacion;
    private String asunto;
    private boolean disabledEdicionObservacion;
    private Integer tipoRegistroObservacion; // 1=Crear,2=Editar,3=Ver
    private boolean blnSubsanar;
    private boolean procesaNuevoExpediente;    
    private boolean serieDifiere;
    ////////// INPE //////////
    private List<Map> lstExpedientesImpresion;
    private TipoBaseGt regionInpe;
    private TipoBaseGt penalInpe;
    private TipoBaseGt regionInpeNoPropiedad;
    private TipoBaseGt penalInpeNoPropiedad;
    private TipoBaseGt regionInpeSelected;
    private Integer activeTabDeclaracion;
    private boolean habilitaInpe;
    private Date fechaLimiteInpe;
    //////////////////////////////////////////////   
    
    
    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaRegEmpArmaFacade ejbAmaRegEmpArmaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaModelosFacade ejbAmaModelosFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.amaArmaFacade ejbArmaFacadeFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaArmaFacade ejbAmaArmaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaRegEmpProcesoFacade ejbAmaRegEmpProcesoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt ejbSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade ejbAmaTarjetaPropiedadFacade;
    @EJB
    private pe.gob.sucamec.rma1369.bean.RaessFacade ejbRaessFacade;
    @EJB
    private NeDocumentoFacade ejbNeDocumentoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbProcesoTupaFacade ejbSbProcesoTupaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaInventarioArmaFacade ejbAmaInventarioArmaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDerivacionCydocFacade ejbSbDerivacionCydocFacade;
    @Inject
    WsTramDoc wsTramDocController;
    @Inject
    WsPide wsPideController;
    // Si la tabla no tiene el campo activo borrar DESDE ACA //
    /**
     * Devuelve si el registro actual se encuentra activo, se usa para borrado
     * lógico.
     *
     * @return true si se encuenta activo, false si no.
     */
    public boolean isActivo() {
        return (registro.getActivo() == 1);
    }

    /**
     * Cambia el estado del registro a activo o inactivo, se usa para borrado
     * lógico.
     *
     * @param activo true o false para cambiar el estado.
     */
    public void setActivo(boolean activo) {
        if (activo) {
            registro.setActivo((short) 1);
        } else {
            registro.setActivo((short) 0);
        }
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @return
     */
    public List<AmaRegEmpArma> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<AmaRegEmpArma> a) {
        this.registrosSeleccionados = a;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public String getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public boolean isMostrarEstadoLicencia() {
        return mostrarEstadoLicencia;
    }

    public void setMostrarEstadoLicencia(boolean mostrarEstadoLicencia) {
        this.mostrarEstadoLicencia = mostrarEstadoLicencia;
    }

    public List<Map> getResultadosArmas() {
        return resultadosArmas;
    }

    public void setResultadosArmas(List<Map> resultadosArmas) {
        this.resultadosArmas = resultadosArmas;
    }

    public boolean isAcepto() {
        return acepto;
    }

    public void setAcepto(boolean acepto) {
        this.acepto = acepto;
    }

    public Map getRegistroSelected() {
        return registroSelected;
    }

    public void setRegistroSelected(Map registroSelected) {
        this.registroSelected = registroSelected;
    }

    public UploadedFile getFileAdjunto1() {
        return fileAdjunto1;
    }

    public void setFileAdjunto1(UploadedFile fileAdjunto1) {
        this.fileAdjunto1 = fileAdjunto1;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getFoto() {
        try {
            return foto;    
        } catch (Exception e) {            
            return null;
        }        
    }

    public void setFoto(StreamedContent foto) {
        this.foto = foto;
    }

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public String getOpcionPanel1() {
        return opcionPanel1;
    }

    public void setOpcionPanel1(String opcionPanel1) {
        this.opcionPanel1 = opcionPanel1;
    }

    public String getOpcionPanel2() {
        return opcionPanel2;
    }

    public void setOpcionPanel2(String opcionPanel2) {
        this.opcionPanel2 = opcionPanel2;
    }

    public boolean isDisabledPanel1() {
        return disabledPanel1;
    }

    public void setDisabledPanel1(boolean disabledPanel1) {
        this.disabledPanel1 = disabledPanel1;
    }

    public boolean isDisabledPanel2() {
        return disabledPanel2;
    }

    public void setDisabledPanel2(boolean disabledPanel2) {
        this.disabledPanel2 = disabledPanel2;
    }

    public AmaModelos getSelectedArma() {
        return selectedArma;
    }

    public void setSelectedArma(AmaModelos selectedArma) {
        this.selectedArma = selectedArma;
    }

    public List<Map> getLstFotos() {
        return lstFotos;
    }

    public void setLstFotos(List<Map> lstFotos) {
        this.lstFotos = lstFotos;
    }

    public boolean isDisabledBtnFoto() {
        return disabledBtnFoto;
    }

    public void setDisabledBtnFoto(boolean disabledBtnFoto) {
        this.disabledBtnFoto = disabledBtnFoto;
    }

    public byte[] getFileByte() {
        return fileByte;
    }

    public void setFileByte(byte[] fileByte) {
        this.fileByte = fileByte;
    }

    public int getContPorEmitir() {
        return contPorEmitir;
    }

    public void setContPorEmitir(int contPorEmitir) {
        this.contPorEmitir = contPorEmitir;
    }

    public int getContNoPertenecen() {
        return contNoPertenecen;
    }

    public void setContNoPertenecen(int contNoPertenecen) {
        this.contNoPertenecen = contNoPertenecen;
    }

    public int getContEmitidos() {
        return contEmitidos;
    }

    public void setContEmitidos(int contEmitidos) {
        this.contEmitidos = contEmitidos;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public boolean isRenderImprimir() {
        return renderImprimir;
    }

    public void setRenderImprimir(boolean renderImprimir) {
        this.renderImprimir = renderImprimir;
    }

    public boolean isRenderRegistrar() {
        return renderRegistrar;
    }

    public void setRenderRegistrar(boolean renderRegistrar) {
        this.renderRegistrar = renderRegistrar;
    }

    public TipoGamac getEstadoSituacion() {
        return estadoSituacion;
    }

    public void setEstadoSituacion(TipoGamac estadoSituacion) {
        this.estadoSituacion = estadoSituacion;
    }

    public Date getFechaIncautacion() {
        return fechaIncautacion;
    }

    public void setFechaIncautacion(Date fechaIncautacion) {
        this.fechaIncautacion = fechaIncautacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getArmaEmitido() {
        return armaEmitido;
    }

    public void setArmaEmitido(String armaEmitido) {
        this.armaEmitido = armaEmitido;
    }

    public int getContParrafo1() {
        return contParrafo1;
    }

    public void setContParrafo1(int contParrafo1) {
        this.contParrafo1 = contParrafo1;
    }

    public int getContParrafo2() {
        return contParrafo2;
    }

    public void setContParrafo2(int contParrafo2) {
        this.contParrafo2 = contParrafo2;
    }

    public int getContParrafo3() {
        return contParrafo3;
    }

    public void setContParrafo3(int contParrafo3) {
        this.contParrafo3 = contParrafo3;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public TipoGamac getSituacionArma() {
        return situacionArma;
    }

    public void setSituacionArma(TipoGamac situacionArma) {
        this.situacionArma = situacionArma;
    }

    public boolean isRenderEstadoArma() {
        return renderEstadoArma;
    }

    public void setRenderEstadoArma(boolean renderEstadoArma) {
        this.renderEstadoArma = renderEstadoArma;
    }

    public String getSelectedArmaString() {
        return selectedArmaString;
    }

    public void setSelectedArmaString(String selectedArmaString) {
        this.selectedArmaString = selectedArmaString;
    }

    public List<Map> getResultadosBandeja() {
        return resultadosBandeja;
    }

    public void setResultadosBandeja(List<Map> resultadosBandeja) {
        this.resultadosBandeja = resultadosBandeja;
    }

    public List<Map> getResultadosBandejaSeleccionados() {
        return resultadosBandejaSeleccionados;
    }

    public void setResultadosBandejaSeleccionados(List<Map> resultadosBandejaSeleccionados) {
        this.resultadosBandejaSeleccionados = resultadosBandejaSeleccionados;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripOservacion() {
        return descripOservacion;
    }

    public void setDescripOservacion(String descripOservacion) {
        this.descripOservacion = descripOservacion;
    }

    public boolean isDisabledEdicionObservacion() {
        return disabledEdicionObservacion;
    }

    public void setDisabledEdicionObservacion(boolean disabledEdicionObservacion) {
        this.disabledEdicionObservacion = disabledEdicionObservacion;
    }

    public Integer getTipoRegistroObservacion() {
        return tipoRegistroObservacion;
    }

    public void setTipoRegistroObservacion(Integer tipoRegistroObservacion) {
        this.tipoRegistroObservacion = tipoRegistroObservacion;
    }

    public boolean isBlnSubsanar() {
        return blnSubsanar;
    }

    public void setBlnSubsanar(boolean blnSubsanar) {
        this.blnSubsanar = blnSubsanar;
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

    public boolean isRenderProgressBar() {
        return renderProgressBar;
    }

    public void setRenderProgressBar(boolean renderProgressBar) {
        this.renderProgressBar = renderProgressBar;
    }

    public boolean isErrorArchivo() {
        return errorArchivo;
    }

    public void setErrorArchivo(boolean errorArchivo) {
        this.errorArchivo = errorArchivo;
    }

    public boolean isBlnGenerarPdf() {
        return blnGenerarPdf;
    }

    public void setBlnGenerarPdf(boolean blnGenerarPdf) {
        this.blnGenerarPdf = blnGenerarPdf;
    }

    public String getTxtBtnImprimir() {
        return txtBtnImprimir;
    }

    public void setTxtBtnImprimir(String txtBtnImprimir) {
        this.txtBtnImprimir = txtBtnImprimir;
    }

    public AmaRegEmpProceso getProcesoHis() {
        return procesoHis;
    }

    public void setProcesoHis(AmaRegEmpProceso procesoHis) {
        this.procesoHis = procesoHis;
    }

    public List<Map> getLstExpedientesImpresion() {
        return lstExpedientesImpresion;
    }

    public void setLstExpedientesImpresion(List<Map> lstExpedientesImpresion) {
        this.lstExpedientesImpresion = lstExpedientesImpresion;
    }

    public TipoBaseGt getRegionInpe() {
        return regionInpe;
    }

    public void setRegionInpe(TipoBaseGt regionInpe) {
        this.regionInpe = regionInpe;
    }

    public TipoBaseGt getPenalInpe() {
        return penalInpe;
    }

    public void setPenalInpe(TipoBaseGt penalInpe) {
        this.penalInpe = penalInpe;
    }

    public TipoBaseGt getRegionInpeNoPropiedad() {
        return regionInpeNoPropiedad;
    }

    public void setRegionInpeNoPropiedad(TipoBaseGt regionInpeNoPropiedad) {
        this.regionInpeNoPropiedad = regionInpeNoPropiedad;
    }

    public TipoBaseGt getPenalInpeNoPropiedad() {
        return penalInpeNoPropiedad;
    }

    public void setPenalInpeNoPropiedad(TipoBaseGt penalInpeNoPropiedad) {
        this.penalInpeNoPropiedad = penalInpeNoPropiedad;
    }

    public boolean isProcesaNuevoExpediente() {
        return procesaNuevoExpediente;
    }

    public void setProcesaNuevoExpediente(boolean procesaNuevoExpediente) {
        this.procesaNuevoExpediente = procesaNuevoExpediente;
    }

    public Integer getActiveTabDeclaracion() {
        return activeTabDeclaracion;
    }

    public void setActiveTabDeclaracion(Integer activeTabDeclaracion) {
        this.activeTabDeclaracion = activeTabDeclaracion;
    }

    public Map getDatosActaInt() {
        return datosActaInt;
    }

    public void setDatosActaInt(Map datosActaInt) {
        this.datosActaInt = datosActaInt;
    }

    public boolean isTieneInternamiento() {
        return tieneInternamiento;
    }

    public void setTieneInternamiento(boolean tieneInternamiento) {
        this.tieneInternamiento = tieneInternamiento;
    }

    public boolean isDisabledBtnGuardar() {
        return disabledBtnGuardar;
    }

    public void setDisabledBtnGuardar(boolean disabledBtnGuardar) {
        this.disabledBtnGuardar = disabledBtnGuardar;
    }

    public String getNumDocPosiblePropietario() {
        return numDocPosiblePropietario;
    }

    public void setNumDocPosiblePropietario(String numDocPosiblePropietario) {
        this.numDocPosiblePropietario = numDocPosiblePropietario;
    }

    public String getTipoDocPosiblePropietario() {
        return tipoDocPosiblePropietario;
    }

    public void setTipoDocPosiblePropietario(String tipoDocPosiblePropietario) {
        this.tipoDocPosiblePropietario = tipoDocPosiblePropietario;
    }

    public Integer getMaxLengthPosiblePropietario() {
        return maxLengthPosiblePropietario;
    }

    public void setMaxLengthPosiblePropietario(Integer maxLengthPosiblePropietario) {
        this.maxLengthPosiblePropietario = maxLengthPosiblePropietario;
    }

    public String getDatosPosiblePropietario() {
        return datosPosiblePropietario;
    }

    public void setDatosPosiblePropietario(String datosPosiblePropietario) {
        this.datosPosiblePropietario = datosPosiblePropietario;
    }

    public TipoBaseGt getRegionInpeSelected() {
        return regionInpeSelected;
    }

    public void setRegionInpeSelected(TipoBaseGt regionInpeSelected) {
        this.regionInpeSelected = regionInpeSelected;
    }

    public boolean isHabilitaInpe() {
        return habilitaInpe;
    }

    public void setHabilitaInpe(boolean habilitaInpe) {
        this.habilitaInpe = habilitaInpe;
    }

    public Date getFechaLimiteInpe() {
        return fechaLimiteInpe;
    }

    public void setFechaLimiteInpe(Date fechaLimiteInpe) {
        this.fechaLimiteInpe = fechaLimiteInpe;
    }

    public boolean isHabilitaJuridica() {
        return habilitaJuridica;
    }

    public void setHabilitaJuridica(boolean habilitaJuridica) {
        this.habilitaJuridica = habilitaJuridica;
    }

    public Date isFechaLimiteJuridica() {
        return fechaLimiteJuridica;
    }

    public void setFechaLimiteJuridica(Date fechaLimiteJuridica) {
        this.fechaLimiteJuridica = fechaLimiteJuridica;
    }

    public boolean isSerieDifiere() {
        return serieDifiere;
    }

    public void setSerieDifiere(boolean serieDifiere) {
        this.serieDifiere = serieDifiere;
    }

    /**
     * Anula multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void anularMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (AmaRegEmpArma a : registrosSeleccionados) {
                a.setActivo((short) 0);
                ejbAmaRegEmpArmaFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Activa multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void activarMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (AmaRegEmpArma a : registrosSeleccionados) {
                a.setActivo((short) 1);
                ejbAmaRegEmpArmaFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a activo en un
     * registro de una lista.
     *
     */
    public void activar() {
        try {
            registro = (AmaRegEmpArma) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbAmaRegEmpArmaFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un
     * registro de una lista.
     *
     */
    public void anular() {
        try {
            registro = (AmaRegEmpArma) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbAmaRegEmpArmaFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public AmaRegEmpArma getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(AmaRegEmpArma registro) {
        this.registro = registro;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @return el filtro de busquedas
     */
    public String getFiltro() {
        return filtro;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @param filtro el nuevo filtro de busquedas
     */
    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    /**
     * Propiedad para el estado del CRUD.
     *
     * @return el estado actual del CRUD.
     */
    public EstadoCrud getEstado() {
        return estado;
    }

    /**
     * Propiedad para los resultados de la búsqueda del CRUD.
     *
     * @return Los resultados de la búsqueda.
     */
    public ListDataModel<AmaRegEmpArma> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        return "List";
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<AmaRegEmpArma> getSelectItems() {
        return ejbAmaRegEmpArmaFacade.findAll();
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        if (resultados != null) {
            buscar();
        }
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        resultados = new ListDataModel(ejbAmaRegEmpArmaFacade.selectLike(filtro));
    }
    
    /**
     * FUNCIÓN PARA MOSTRAR FORMULARIO DE VISUALIZACIÓN DE REGISTRO DE REGULARIZACIÓN DE ARMAS
     * @author Richar Fernández
     * @version 1.0
     * @param reg Map con datos de arma
     * @param esSubsanado Flag de subsanado
     */
    public void mostrarVer(Map reg, boolean esSubsanado) {        
        reiniciarValores();
        Long nroLicencia = null;
        
        try {
            if(reg.get("ID") != null){
                registro = ejbAmaRegEmpArmaFacade.obtenerArmaVerificada(Long.parseLong(""+reg.get("ID")));    
                
                if(registro.getEstadoVerfica() == 1 || 
                   registro.getEstadoVerfica() == 2) {
                    registroSelected = ejbArmaFacadeFacade.buscarArmaRMA(""+reg.get("NRO_LIC"));
                }else{
                    registroSelected = reg;
                }
                observacion = registro.getDescMotivo();
                estadoSituacion = registro.getEstadoArmaId();
                situacionArma = registro.getSituacionArma();
                regionInpe = registro.getRegionId();
                regionInpeNoPropiedad = registro.getRegionId();
                penalInpe = registro.getPenalId();
                penalInpeNoPropiedad = registro.getPenalId();
                
                nroLicencia = registro.getLicDiscaId();
            }else{
                registro = new AmaRegEmpArma();
                registro.setEstadoVerfica(4L);
                
                if(reg.get("NRO_LIC") != null){
                    nroLicencia = Long.parseLong(reg.get("NRO_LIC").toString());    
                }
            }
            cargarActaInternamiento(nroLicencia, registro.getSerie(), registro.getModeloId());
            
            switch(registro.getEstadoVerfica().intValue()){
                case 1:
                case 2:
                        // VERIFICADO / NO ME PERTENECE
                        estado = EstadoCrud.VERVERIF;
                        if(registro.getSituacionArma().getCodProg().equals("TP_SITU_POS") ){
                            cambiaPanel2(false);
                            if(registro.getModeloId() != null){
                                selectedArma = ejbAmaModelosFacade.obtenerModeloById(registro.getModeloId());   
                                selectedArmaString = obtenerArmaEditarVer(selectedArma);
                            }
                            lstFotos = new ArrayList();
                            cargarFotos();
                        }else{
                            cambiaPanel1();
                            cargarArchivo();
                        }                        
                        cambiaSituacion();
                        numDocPosiblePropietario = registro.getNdocPosibleProp();
                        break;
                case 3:
                        //NUEVA ARMA
                        estado = EstadoCrud.VERARMA;
                        lstFotos = new ArrayList();
                        selectedArma = ejbAmaModelosFacade.obtenerModeloById(registro.getModeloId());
                        selectedArmaString = obtenerArmaEditarVer(selectedArma);
                        
                        if(registro.getEstadoArmaId() != null && registro.getEstadoArmaId().getCodProg().equals("TP_ESTA_OPE")){
                            cargarFotos();
                        }
                        cambiaSituacionNuevaArma();
                        cargarArchivo();
                        numDocPosiblePropietario = registro.getNdocPosibleProp();
                        break;
                case 4:
                        registroSelected = ejbArmaFacadeFacade.buscarArmaRMA(""+reg.get("NRO_LIC"));
                        
                        //EDICION DE REGISTRO DE EMITIDOS
                        armaEmitido = null;
                        lstFotos = new ArrayList();
                        cargarFotos();
                        
                        if(armaEmitido == null){
                            selectedArma = ejbAmaModelosFacade.obtenerModeloById(registro.getModeloId());
                            selectedArmaString = obtenerArmaEditarVer(selectedArma);
                            armaEmitido = obtenerDescripArmaListado(selectedArma);
                        }
                        numDocPosiblePropietario = registro.getNdocPosibleProp();
                        estado = EstadoCrud.VEREMITIDO;
                        break;
            }
            asignarTipoDocPosiblePropietario();
            asignarDatosPosiblePropietario();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar datos del registro");
        }
    }
    
    public void cargarActaInternamiento(Long nroLicencia, String nroSerie, Long modeloId){
        String tipados = obtenerArrayConcatenadosTipadosGT();
        tieneInternamiento = false;
        if(nroLicencia != null){
            datosActaInt = ejbAmaInventarioArmaFacade.buscarDatosInternamientoRaessXLicencia(nroLicencia, tipados);
        }
        if(datosActaInt == null && modeloId != null){
            datosActaInt = ejbAmaInventarioArmaFacade.buscarDatosInternamientoRaessXSerieXModelo(nroSerie, modeloId, tipados);    
        }
        if(datosActaInt != null){
            tieneInternamiento = true;
        }
    }
    
    /**
     * FUNCION PARA CARGAR LAS FOTOS
     * @author Richar Fernández
     * @version 1.0
     */
    public void cargarFotos(){
        boolean cargaDatos = true;
        Map mapa = new HashMap();
        String nombreArchivo = "";
        String extension = "";
        String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
        
        //// Foto 1
        if(registro.getNombreFoto1Id() != null){
            nombreArchivo = registro.getNombreFoto1Id().substring(0, registro.getNombreFoto1Id().lastIndexOf(".") );
            extension = registro.getNombreFoto1Id().substring(registro.getNombreFoto1Id().lastIndexOf("."), registro.getNombreFoto1Id().length());
            mapa.put("nro", "1");
            mapa.put("nombre", registro.getNombreFoto1Id());
            
            try {
                mapa.put("byte", FileUtils.readFileToByteArray(new File(path + nombreArchivo + extension.toLowerCase() )));            
                lstFotos.add(mapa);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mapa.put("byte", FileUtils.readFileToByteArray(new File(path + nombreArchivo + extension.toUpperCase() )));
                    lstFotos.add(mapa);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    cargaDatos = false;
                }
            }
        }     
        
        //// Foto 2
        if(registro.getNombreFoto2Id() != null){
            nombreArchivo = registro.getNombreFoto2Id().substring(0, registro.getNombreFoto2Id().lastIndexOf(".") );
            extension = registro.getNombreFoto2Id().substring(registro.getNombreFoto2Id().lastIndexOf("."), registro.getNombreFoto2Id().length());
            mapa = new HashMap();
            mapa.put("nro", "2");
            mapa.put("nombre", registro.getNombreFoto2Id());
            try {
                mapa.put("byte", FileUtils.readFileToByteArray(new File(path + nombreArchivo + extension.toLowerCase() )));
                lstFotos.add(mapa);
            }catch (Exception e) {
                e.printStackTrace();
                try {
                    mapa.put("byte", FileUtils.readFileToByteArray(new File(path + nombreArchivo + extension.toUpperCase() )));
                    lstFotos.add(mapa);
                }catch (Exception ex) {
                    ex.printStackTrace();
                    cargaDatos = false;
                }
            }
        }
        
        if(!cargaDatos){
            JsfUtil.mensajeError("Hubo un error al cargar las fotos del registro.");
        }else{
            setDisabledBtnFoto(true);
        }
    }
    
    /**
     * FUNCION PARA CARGAR ARCHIVO ADJUNTO
     * @author Richar Fernández
     * @version 1.0
     */
    public void cargarArchivo(){
        boolean cargaDatos = true;
        
        if(registro.getAdjuntoDocId() != null){
            String nombreArchivo = registro.getAdjuntoDocId().substring(0, registro.getAdjuntoDocId().lastIndexOf(".") );
            String extension = registro.getAdjuntoDocId().substring(registro.getAdjuntoDocId().lastIndexOf("."), registro.getAdjuntoDocId().length());
            String pathDocumento = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_documento").getValor();

            try {                        
                fileByte = FileUtils.readFileToByteArray(new File(pathDocumento + nombreArchivo + extension.toLowerCase() ));
                archivo = registro.getAdjuntoDocId();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    fileByte = FileUtils.readFileToByteArray(new File(pathDocumento + nombreArchivo + extension.toUpperCase() ));
                    archivo = registro.getAdjuntoDocId();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    cargaDatos = false;
                }
            }
        }
        
        if(!cargaDatos){
            JsfUtil.mensajeError("Hubo un error al cargar el documento adjunto.");
        }
    }

    public void asignarTipoDocPosiblePropietario(){
        if(numDocPosiblePropietario != null && !numDocPosiblePropietario.isEmpty()){
            switch(numDocPosiblePropietario.length()){
                case 8: tipoDocPosiblePropietario = "DNI"; break;
                case 9: tipoDocPosiblePropietario = "CE"; break;
                case 11: tipoDocPosiblePropietario = "RUC"; break;
            }
        }
    }
    
    public void asignarDatosPosiblePropietario(){
        if(numDocPosiblePropietario != null && !numDocPosiblePropietario.isEmpty()){
            TipoBaseGt tipoDoc = null;
            SbPersonaGt p = null;
            switch(numDocPosiblePropietario.length()){
                case 8:
                    tipoDoc = ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI");
                    p = ejbPersonaFacade.selectPersonaActivaxTipoDocyNumDoc(tipoDoc.getId(),registro.getNdocPosibleProp());
                    datosPosiblePropietario = p.getApePat() + " " + (p.getApeMat() != null ? p.getApeMat(): "") + " " + p.getNombres(); 
                    break;
                case 9: 
                    tipoDoc = ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_CE");
                    p = ejbPersonaFacade.selectPersonaActivaxTipoDocyNumDoc(tipoDoc.getId(),registro.getNdocPosibleProp());//(filtro, asunto).registro.getNdocPosibleProp();
                    datosPosiblePropietario = p.getApePat() + " " + (p.getApeMat() != null ? p.getApeMat(): "") + " " + p.getNombres(); 
                    break;
                case 11: 
                    tipoDoc = ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_RUC");
                    p = ejbPersonaFacade.selectPersonaActivaxRuc(registro.getNdocPosibleProp());//(filtro, asunto).registro.getNdocPosibleProp();
                    datosPosiblePropietario = p.getRznSocial().trim();
                    break;
            }
        }
    }
        
    /**
     * FUNCIÓN PARA MOSTRAR FORMULARIO DE EDICIÓN DE REGULARIZACIÓN DE ARMA
     * @author Richar Fernández
     * @version 1.0
     * @param reg Map con datos de arma
     */
    public void mostrarEditar(Map reg) {
        reiniciarValores();
        Long nroLicencia = null;
        
        try {
            if(reg.get("ID") != null){
                registro = ejbAmaRegEmpArmaFacade.obtenerArmaVerificada(Long.parseLong(""+reg.get("ID")));
                
                if(registro.getEstadoVerfica() == 1 ||
                   registro.getEstadoVerfica() == 2) {
                    registroSelected = ejbArmaFacadeFacade.buscarArmaRMA(""+reg.get("NRO_LIC"));
                }else{
                    registroSelected = reg;
                }
                
                observacion = registro.getDescMotivo();
                estadoSituacion = registro.getEstadoArmaId();
                situacionArma = registro.getSituacionArma();
                regionInpe = registro.getRegionId();
                regionInpeNoPropiedad = registro.getRegionId();
                penalInpe = registro.getPenalId();
                penalInpeNoPropiedad = registro.getPenalId();
                
                nroLicencia = registro.getLicDiscaId();
            }else{
                registro = new AmaRegEmpArma();
                registro.setEstadoVerfica(4L);
                
                if(reg.get("NRO_LIC") != null){
                    nroLicencia = Long.parseLong(reg.get("NRO_LIC").toString());
                }
            }
            cargarActaInternamiento(nroLicencia, registro.getSerie(), registro.getModeloId());
            if(datosActaInt != null){
                opcionPanel1 = "1";
            }
            
            switch(registro.getEstadoVerfica().intValue()){
                case 1:
                case 2:
                        // VERIFICADO / NO ME PERTENECE
                        estado = EstadoCrud.EDITARVERIF;
                        if(registro.getSituacionArma().getCodProg().equals("TP_SITU_POS") ){
                            cambiaPanel2(false);
                            if(registro.getModeloId() != null){
                                selectedArma = ejbAmaModelosFacade.obtenerModelo(registro.getModeloId());   
                                if(selectedArma != null){
                                    selectedArmaString = obtenerArmaEditarVer(selectedArma);    
                                }else{
                                    JsfUtil.mensajeError("El modelo guardado no se encuentra activo");
                                }
                            }
                            lstFotos = new ArrayList();
                            cargarFotos();
                        }else{
                            cambiaPanel1();
                            cargarArchivo();
                        }
                        if(registro.getSeriePropuesta() != null && !registro.getSeriePropuesta().isEmpty()){
                            serieDifiere = true;
                        }
                        cambiaSituacion();
                        numDocPosiblePropietario = registro.getNdocPosibleProp();
                        break;
                case 3:
                        //NUEVA ARMA
                        estado = EstadoCrud.EDITARARMA;
                        lstFotos = new ArrayList();
                        selectedArma = ejbAmaModelosFacade.obtenerModelo(registro.getModeloId());    
                        if(selectedArma != null){
                            selectedArmaString = obtenerArmaEditarVer(selectedArma);
                        }else{
                            JsfUtil.mensajeError("El modelo guardado no se encuentra activo");
                        }
                        
                        if(registro.getEstadoArmaId() != null && registro.getEstadoArmaId().getCodProg().equals("TP_ESTA_OPE")){                            
                            cargarFotos();
                        }
                        cambiaSituacionNuevaArma();
                        numDocPosiblePropietario = registro.getNdocPosibleProp();
                        cargarArchivo();
                        break;
                case 4:
                        registroSelected = ejbArmaFacadeFacade.buscarArmaRMA(""+reg.get("NRO_LIC"));
                        Map amaArma = ejbAmaArmaFacade.buscarDatosEmitidas(Long.parseLong(""+reg.get("NRO_LIC")));
                        
                        if(amaArma != null){
                            if(reg.get("ID") == null){ //NUEVO REGISTRO DE EMITIDOS
                                //del tipo 4 Emitidos y existentes
                                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                                SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
                                registro.setPersonaId(per);
                                registro.setActivo((short) 1);
                                registro.setEstadoVerfica(4L);
                                registro.setLicDiscaId(Long.parseLong(""+reg.get("NRO_LIC")));
                                registro.setSerie(""+reg.get("NRO_SERIE"));
                                registro.setModeloId(Long.parseLong(""+reg.get("COD_MODELO")));
                                registro.setEstadoArmaId(ejbTipoGamacFacade.obtenerXId(Long.parseLong(""+amaArma.get("estadoId"))).get(0));
                                registro.setSituacionArma(ejbTipoGamacFacade.obtenerXId(Long.parseLong(""+amaArma.get("situacionId"))).get(0));
                                armaEmitido = reg.get("DES_ARM")+ "/" + reg.get("DES_MARCA")+ "/" + reg.get("DES_MODELO") + "/" + reg.get("CALIBRE");
                                setDisabledBtnFoto(false);
                                lstFotos = new ArrayList();                            
                            }else{
                                //EDICION DE REGISTRO DE EMITIDOS
                                armaEmitido = null;
                                lstFotos = new ArrayList();

                                cargarFotos();
                            }
                            if(armaEmitido == null){
                                if(registro.getModeloId() != null){
                                    selectedArma = ejbAmaModelosFacade.obtenerModeloById(registro.getModeloId());
                                    selectedArmaString = obtenerArmaEditarVer(selectedArma);
                                    armaEmitido = obtenerDescripArmaListado(selectedArma);
                                }
                            }
                            numDocPosiblePropietario = registro.getNdocPosibleProp();
                            estado = EstadoCrud.EDITAREMITIDO;
                            break;
                        }else{
                            JsfUtil.mensajeError("No se encontró datos del arma.");
                        }
            }
            asignarTipoDocPosiblePropietario();
            asignarDatosPosiblePropietario();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar datos del registro");
        }
    }

    /**
     * FUNCIÓN PARA MOSTRAR FORMULARIO DE REGISTRO DE REGULARIZACIÓN DE ARMA
     * @author Richar Fernández
     * @version 1.0
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new AmaRegEmpArma();
        registro.setActivo((short) 1);
    }
    
    public void validarNombresAdjuntos(){
        try {
            String nombreArchivo = "", extension = "";
            
            if(registro.getNombreFoto1Id() != null && !registro.getNombreFoto1Id().isEmpty()){
                nombreArchivo = registro.getNombreFoto1Id().substring(0, registro.getNombreFoto1Id().lastIndexOf(".") - 1 ); // extension - ultima letra
                extension = registro.getNombreFoto1Id().substring(registro.getNombreFoto1Id().lastIndexOf(".") - 1, registro.getNombreFoto1Id().length()); // extension + ultima letra
                registro.setNombreFoto1Id(nombreArchivo + extension.toLowerCase());
            }
            
            if(registro.getNombreFoto2Id() != null && !registro.getNombreFoto2Id().isEmpty()){
                nombreArchivo = registro.getNombreFoto2Id().substring(0, registro.getNombreFoto2Id().lastIndexOf(".") - 1 ); // extension - ultima letra
                extension = registro.getNombreFoto2Id().substring(registro.getNombreFoto2Id().lastIndexOf(".") - 1, registro.getNombreFoto2Id().length());  // extension + ultima letra
                registro.setNombreFoto2Id(nombreArchivo + extension.toLowerCase());
            }
                
            if(registro.getAdjuntoDocId() != null && !registro.getAdjuntoDocId().isEmpty()){
                nombreArchivo = registro.getAdjuntoDocId().substring(0, registro.getAdjuntoDocId().lastIndexOf(".") );
                extension = registro.getAdjuntoDocId().substring(registro.getAdjuntoDocId().lastIndexOf("."), registro.getAdjuntoDocId().length());
                registro.setAdjuntoDocId(nombreArchivo + extension.toLowerCase());
            }
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean procesoRaessPlazoPermitido() {
        return (JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.stringToDate(JsfUtil.bundleBDIntegrado("amaRegEmpArma_fechaLimiteRaess"), "dd/MM/yyyy")) <= 0);
    }
    
    public boolean esNuevoProceso(){
        return (JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.stringToDate(JsfUtil.bundleBDIntegrado("amaRegEmpArma_fechaRaess"), "dd/MM/yyyy")) >= 0 );
    }
    
    public String obtenerAsuntoExpediente(AmaRegEmpArma amaRegistro){
        String asunto = "";
        Map armaDisca = null;
        AmaModelos modelo = null;
        if(amaRegistro.getEstadoVerfica() != 3){ // Verificadas y No me pertenecen
            armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+amaRegistro.getLicDiscaId());
            asunto = asunto + "DATOS DISCA = Serie: "+ armaDisca.get("NRO_SERIE") +", Tipo Arma:" + armaDisca.get("DES_ARM") +", Marca:"+ armaDisca.get("DES_MARCA")+", Modelo:"+ (armaDisca.get("DES_MODELO")!=null?"-":armaDisca.get("DES_MODELO") ) +", Calibre:"+ armaDisca.get("CALIBRE")+ ", Situación del arma:" + armaDisca.get("ESTADO");
            if(amaRegistro.getEstadoArmaId() != null){
                asunto = asunto + "; DATOS ARMA VERIFICADA = Estado del arma:"+ amaRegistro.getEstadoArmaId().getNombre();
            }else{
                asunto = asunto + "; DATOS ARMA VERIFICADA = Situación del arma: "+ amaRegistro.getSituacionArma().getNombre() +", Serie:"+ (amaRegistro.getSerie() != null?amaRegistro.getSerie().trim():"") ;    
            }
        }else{ //Nuevo
            armaDisca = null;
            if(amaRegistro.getEstadoArmaId() != null){
                asunto = "DATOS ARMA VERIFICADA = Estado del arma: "+ amaRegistro.getEstadoArmaId().getNombre() +", Situación del arma: "+ amaRegistro.getSituacionArma().getNombre() +", Serie:"+amaRegistro.getSerie();
            }else{
                asunto = "DATOS ARMA VERIFICADA = Situación del arma: "+ amaRegistro.getSituacionArma().getNombre() +", Serie:"+ (amaRegistro.getSerie() != null?amaRegistro.getSerie().trim():"");
            }
        }                        
        if(amaRegistro.getModeloId() != null){
            modelo =  ejbAmaModelosFacade.obtenerModeloById(amaRegistro.getModeloId());
            asunto = asunto + ", Tipo Arma:" + modelo.getTipoArmaId().getNombre()+", Marca:"+modelo.getMarcaId().getNombre()+", Modelo:"+modelo.getModelo()+", Calibre:"+obtenerCalibreArma(modelo.getAmaCatalogoList());                            
            if(amaRegistro.getPesoArma() != null){
                asunto = asunto + ", Peso:" + amaRegistro.getPesoArma();
            }
        }else{
            if(amaRegistro.getPesoArma() != null){
                asunto = asunto + ", Peso:" + amaRegistro.getPesoArma();
            }
        }
        return asunto;
    }
    
    public Integer obtenerNroProcesoEdicion(AmaRegEmpArma item){
        Integer nroProceso = null;
        if(esPerfilInpe()){
            nroProceso = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_Inpe_nroProcesoIndividual").getValor());
        }else{
            nroProceso = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoIndividual").getValor());
        }
        return nroProceso;
    }
    
    /**
     * FUNCIÓN PARA EDITAR REGISTRO DE REGULARIZACIÓN DE ARMA
     * @author Richar Fernández
     * @version 1.0     
     * @return Pagina a redireccionar
     */
    public String editar() {
        try {
            String path = "", pathDocumento= "";
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            if(validacionCreate()){
                registro = (AmaRegEmpArma) JsfUtil.entidadMayusculas(registro, "");            
                String fileName = "";
                AmaRegEmpArma temp = ejbAmaRegEmpArmaFacade.obtenerArmaVerificada(registro.getId());
                if(registro.getSituacionArma() != null){
                    if(!registro.getSituacionArma().getCodProg().equals("TP_SITU_INC")){
                        registro.setFechaIncauta(null);
                    }
                }
                if(isBlnSubsanar() && registro.getExpediente() != null){
                    registro.setFechaSub(new Date());
                    registro.setTipoEstadoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EVA_SUB"));
                    
                    if(isProcesaNuevoExpediente() && !esPerfilInpe() ){ //procesoRaessPlazoPermitido() && 
                        List<Integer> acciones = new ArrayList();
                        acciones.add(21);   // Procesado
                        String usuarioActual = ejbAmaRegEmpArmaFacade.obtenerUsuarioTrazaActualExpediente(registro.getExpediente());
                        boolean estadoNE = wsTramDocController.archivarExpediente(registro.getExpediente(), usuarioActual, JsfUtil.bundleBDIntegrado("amaRegEmpArma_asunto_archivarExpediente"));
                        Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
                        SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
                        String nuevoExpediente = wsTramDocController.crearExpediente_regularizacion(null, per , obtenerAsuntoExpediente(registro) , obtenerNroProcesoEdicion(registro), usuaTramDoc, usuaTramDoc,JsfUtil.bundleBDIntegrado("amaRegEmpArma_nombreProcesoRaess_nuevo"));
                        if(nuevoExpediente != null){
                            registro.setTipoEstadoId(null);
                            registro.setTipoEvaluacionId(null);
                            registro.setAsuntoObsSis(null);
                            registro.setDescObsSis(null);
                            registro.setExpediente(nuevoExpediente);
                            usuarioActual = ejbAmaRegEmpArmaFacade.obtenerUsuarioTrazaActualExpediente(nuevoExpediente);
                            estadoNE = wsTramDocController.asignarExpediente(nuevoExpediente, usuarioActual, JsfUtil.bundleBDIntegrado("amaRegEmpArma_usuarioNuevoExpediente"), "", "", acciones,false,null);
                            if (!estadoNE) {
                                JsfUtil.mensajeError("No se pudo derivar el expediente creado "+ nuevoExpediente);
                            }
                        }else{
                            JsfUtil.mensajeError("Hubo un error al crear el nuevo expediente");
                            return null;
                        }
                    }
                }
                if(opcionPanel1 != null){
                    lstFotos = new ArrayList();
                    registro.setModeloId(null);
                    registro.setNombreFoto1Id(null);
                    registro.setNombreFoto2Id(null);
                    registro.setRegionId(regionInpeNoPropiedad);
                    registro.setPenalId(penalInpeNoPropiedad);
                    
                    if(registro.getSituacionArma().getCodProg().equals("TP_EST_EMP_NOP") || registro.getSituacionArma().getCodProg().equals("TP_SITU_VEN") || registro.getSituacionArma().getCodProg().equals("TP_ESTA_VEN") ){
                        registro.setEstadoVerfica(2L);
                    }else{
                        registro.setEstadoVerfica(1L);
                    }
                    registro.setNdocPosibleProp(numDocPosiblePropietario);
                    if(registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") && datosActaInt != null && registro.getModeloId() == null){
                        registro.setModeloId(Long.parseLong(datosActaInt.get("ID_MODELO").toString()));
                    }
                    if(fileAdjunto1 != null){
                        pathDocumento = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_documento").getValor();
                        fileName = fileAdjunto1.getFileName();
                        fileName = fileName.toLowerCase();
                        registro.setAdjuntoDocId("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_DCRE").toString()+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                        FileUtils.writeByteArrayToFile(new File(pathDocumento + registro.getAdjuntoDocId()), fileByte );
                    }
                }else{
                    registro.setEstadoArmaId(estadoSituacion);
                    registro.setSituacionArma(situacionArma);
                    registro.setDescMotivo(observacion);
                    registro.setAdjuntoDocId(null);
                    registro.setRegionId(regionInpe);
                    registro.setPenalId(penalInpe);
                    registro.setEstadoVerfica(1L);
                    if(selectedArma != null){
                        registro.setModeloId(selectedArma.getId());
                    }else{
                        registro.setModeloId(null);
                    }
                    if(!esPerfilInpe()){
                        registro.setSituacionArma(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_SITU_POS"));
                    }
                    
                    if(lstFotos != null && !lstFotos.isEmpty()){
                        path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();                        
                        fileName = ""+ lstFotos.get(0).get("nombre");
                        if(!Objects.equals(fileName.toUpperCase(), ( temp.getNombreFoto1Id() == null?null: temp.getNombreFoto1Id().toUpperCase()) )){
                            fileName = fileName.toLowerCase();
                            registro.setNombreFoto1Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"a"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                            FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto1Id()), (byte[])lstFotos.get(0).get("byte"));
                        }
                        fileName = ""+ lstFotos.get(1).get("nombre");
                        if(!Objects.equals(fileName.toUpperCase(), ( temp.getNombreFoto2Id() == null?null: temp.getNombreFoto2Id().toUpperCase()) )){
                            fileName = fileName.toLowerCase();
                            registro.setNombreFoto2Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"b"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                            FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto2Id()), (byte[])lstFotos.get(1).get("byte"));
                        }
                    }
                }
                
                if(esPerfilInpe()){
                    registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_INP"));
                }else{
                    registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_SEG"));
                }
                if(isBlnSubsanar() && registro.getExpediente() != null){
                    Integer nroProcesoIndividualEnPosesInt = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoInpeEnPoseIntern").getValor());
                    Integer nroProcesoIndividualPerRob = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoInpePerdRob").getValor());
                    Integer nroProcesoIndividualNoEsMiProp = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoNoEsMiProp").getValor());
                    Integer nroProcesoIndividualTransfer = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoTransferido").getValor());            
                    Integer nroProcesoIndividual;
                    switch(registro.getSituacionArma().getCodProg()){
                        case "TP_SITU_POS":
                        case "TP_SITU_INT":
                                nroProcesoIndividual = nroProcesoIndividualEnPosesInt;
                                break;
                        case "TP_SITU_PER":
                        case "TP_SITU_ROB":
                                nroProcesoIndividual = nroProcesoIndividualPerRob;
                                break;
                        case "TP_EST_EMP_NOP":
                                nroProcesoIndividual = nroProcesoIndividualNoEsMiProp;
                                break;
                        case "TP_SITU_VEN":
                                nroProcesoIndividual = nroProcesoIndividualTransfer;
                                break;
                        default:
                                nroProcesoIndividual = nroProcesoIndividualEnPosesInt;
                                break;
                    } 
                    int conExp = ejbAmaRegEmpArmaFacade.actualizarProcesoTituloExpediente(registro.getExpediente(), nroProcesoIndividual, obtenerTituloExpPorSituacion(registro));
                }
                
                validarNombresAdjuntos();
                ejbAmaRegEmpArmaFacade.edit(registro);
                JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroCreado"));
                
                if(isBlnSubsanar()){
                    return prepareBandejaObservaciones();
                }
                return prepareListRegularizacionArma();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
        return null;
    }
    
    /**
     * FUNCIÓN PARA EDITAR REGISTRO DE ARMA EMITIDA
     * @author Richar Fernández
     * @version 1.0     
     * @return Pagina a redireccionar
     */
    public String editarEmitidos(){
        try {
            String path = "";
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            
            if(validacionCreateEmitidos()){
                String fileName = "";
                AmaRegEmpArma temp = null;
                if(registro.getId() == null){
                    temp = null;
                }else{
                    temp = ejbAmaRegEmpArmaFacade.obtenerArmaVerificada(registro.getId());
                }
                
                if(lstFotos != null && !lstFotos.isEmpty()){
                    path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
                    fileName = ""+ lstFotos.get(0).get("nombre");
                    if(!Objects.equals(fileName.toUpperCase(), (temp == null ?null:temp.getNombreFoto1Id().toUpperCase()) )){                    
                        fileName = fileName.toLowerCase();
                        registro.setNombreFoto1Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"a"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                        FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto1Id()), (byte[])lstFotos.get(0).get("byte"));
                    }
                    fileName = ""+ lstFotos.get(1).get("nombre");
                    if(!Objects.equals(fileName.toUpperCase(), (temp == null ?null:temp.getNombreFoto2Id().toUpperCase()) )){
                        fileName = fileName.toLowerCase();
                        registro.setNombreFoto2Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"b"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                        FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto2Id()), (byte[])lstFotos.get(1).get("byte"));
                    }
                }
                    
                if(isBlnSubsanar() && registro.getExpediente() != null){
                    registro.setFechaSub(new Date());
                    registro.setTipoEstadoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EVA_SUB"));
                    
                    if(isProcesaNuevoExpediente()){ //procesoRaessPlazoPermitido() && 
                        List<Integer> acciones = new ArrayList();
                        acciones.add(21);   // Procesado
                        String usuarioActual = ejbAmaRegEmpArmaFacade.obtenerUsuarioTrazaActualExpediente(registro.getExpediente());
                        boolean estadoNE = wsTramDocController.archivarExpediente(registro.getExpediente(), usuarioActual, JsfUtil.bundleBDIntegrado("amaRegEmpArma_asunto_archivarExpediente"));
                        Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
                        SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
                        String nuevoExpediente = wsTramDocController.crearExpediente_regularizacion(null, per , obtenerAsuntoExpediente(registro) , obtenerNroProcesoEdicion(registro), usuaTramDoc, usuaTramDoc,JsfUtil.bundleBDIntegrado("amaRegEmpArma_nombreProcesoRaess_nuevo"));
                        if(nuevoExpediente != null){
                            registro.setTipoEstadoId(null);
                            registro.setTipoEvaluacionId(null);
                            registro.setAsuntoObsSis(null);
                            registro.setDescObsSis(null);
                            registro.setExpediente(nuevoExpediente);
                            usuarioActual = ejbAmaRegEmpArmaFacade.obtenerUsuarioTrazaActualExpediente(nuevoExpediente);
                            estadoNE = wsTramDocController.asignarExpediente(nuevoExpediente, usuarioActual, JsfUtil.bundleBDIntegrado("amaRegEmpArma_usuarioNuevoExpediente"), "", "", acciones,false,null);
                            if (!estadoNE) {
                                JsfUtil.mensajeError("No se pudo derivar el expediente creado "+ nuevoExpediente);
                            }
                        }else{
                            JsfUtil.mensajeError("Hubo un error al crear el nuevo expediente");
                            return null;
                        }
                    }
                }
                if(esPerfilInpe()){
                    registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_INP"));
                }else{
                    registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_SEG"));
                }
                
                validarNombresAdjuntos();
                if(registro.getId() == null){
                    ejbAmaRegEmpArmaFacade.create(registro);    
                }else{
                    ejbAmaRegEmpArmaFacade.edit(registro);
                }
                
                JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroCreado"));
                if(isBlnSubsanar()){
                    return prepareBandejaObservaciones();
                }
                return prepareListRegularizacionArma();
            }
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
        return null;
    }
    
    /**
     * FUNCIÓN PARA EDITAR REGISTRO DE NUEVA ARMA
     * @author Richar Fernández
     * @version 1.0     
     * @return Pagina a redireccionar
     */
    public String editarNuevaArma(){
        try {
            String path = "", pathDocumento = "";
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            if(validacionCreateNuevaArma()){
                registro = (AmaRegEmpArma) JsfUtil.entidadMayusculas(registro, "");
                AmaRegEmpArma temp = ejbAmaRegEmpArmaFacade.obtenerArmaVerificada(registro.getId());
                String fileName = "";
                
                registro.setEstadoVerfica(3L);
                registro.setModeloId(selectedArma.getId());
                
                if(isBlnSubsanar() && registro.getExpediente() != null){
                    registro.setFechaSub(new Date());
                    registro.setTipoEstadoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EVA_SUB"));
                    
                    if(isProcesaNuevoExpediente()){ // procesoRaessPlazoPermitido() && 
                        List<Integer> acciones = new ArrayList();
                        acciones.add(21);   // Procesado
                        String usuarioActual = ejbAmaRegEmpArmaFacade.obtenerUsuarioTrazaActualExpediente(registro.getExpediente());
                        boolean estadoNE = wsTramDocController.archivarExpediente(registro.getExpediente(), usuarioActual, JsfUtil.bundleBDIntegrado("amaRegEmpArma_asunto_archivarExpediente"));
                        Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
                        SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
                        String nuevoExpediente = wsTramDocController.crearExpediente_regularizacion(null, per , obtenerAsuntoExpediente(registro) , obtenerNroProcesoEdicion(registro), usuaTramDoc, usuaTramDoc,JsfUtil.bundleBDIntegrado("amaRegEmpArma_nombreProcesoRaess_nuevo"));
                        if(nuevoExpediente != null){
                            registro.setTipoEstadoId(null);
                            registro.setTipoEvaluacionId(null);
                            registro.setAsuntoObsSis(null);
                            registro.setDescObsSis(null);
                            registro.setExpediente(nuevoExpediente);
                            usuarioActual = ejbAmaRegEmpArmaFacade.obtenerUsuarioTrazaActualExpediente(nuevoExpediente);
                            estadoNE = wsTramDocController.asignarExpediente(nuevoExpediente, usuarioActual, JsfUtil.bundleBDIntegrado("amaRegEmpArma_usuarioNuevoExpediente"), "", "", acciones,false,null);
                            if (!estadoNE) {
                                JsfUtil.mensajeError("No se pudo derivar el expediente creado "+ nuevoExpediente);
                            }
                        }else{
                            JsfUtil.mensajeError("Hubo un error al crear el nuevo expediente");
                            return null;
                        }
                    }
                }
                
                if(registro.getEstadoArmaId() != null && registro.getEstadoArmaId().getCodProg().equals("TP_ESTA_OPE")){
                    if(lstFotos != null && !lstFotos.isEmpty() ){
                        path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
                        fileName = ""+ lstFotos.get(0).get("nombre");
                        if(!Objects.equals(fileName.toUpperCase(), (temp.getNombreFoto1Id() == null ?null:temp.getNombreFoto1Id().toUpperCase()) )){                    
                            fileName = fileName.toLowerCase();
                            registro.setNombreFoto1Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"a"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                            FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto1Id()), (byte[])lstFotos.get(0).get("byte"));
                        }
                        fileName = ""+ lstFotos.get(1).get("nombre");
                        if(!Objects.equals(fileName.toUpperCase(), (temp.getNombreFoto2Id() == null ?null:temp.getNombreFoto2Id().toUpperCase()) )){
                            fileName = fileName.toLowerCase();
                            registro.setNombreFoto2Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"b"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                            FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto2Id()), (byte[])lstFotos.get(1).get("byte"));
                        }
                    }
                }else{
                    registro.setPesoArma(null);
                }
                
                if(fileAdjunto1 != null){
                    pathDocumento = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_documento").getValor();
                    fileName = fileAdjunto1.getFileName();
                    fileName = fileName.toLowerCase();
                    registro.setAdjuntoDocId("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_DCRE").toString()+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                    FileUtils.writeByteArrayToFile(new File(pathDocumento + registro.getAdjuntoDocId()), fileByte );
                }
                if(isBlnSubsanar()){
                    registro.setFechaSub(new Date());
                    registro.setTipoEstadoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EVA_SUB"));
                }
                if(esPerfilInpe()){
                    registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_INP"));
                    if(registro.getSituacionArma().getCodProg().equals("TP_SITU_VEN")){
                        registro.setNdocPosibleProp(numDocPosiblePropietario);
                    }
                }else{
                    registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_SEG"));
                }
                
                validarNombresAdjuntos();
                ejbAmaRegEmpArmaFacade.edit(registro);
                JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroCreado"));
                
                if(isBlnSubsanar()){
                    return prepareBandejaObservaciones();
                }
                return prepareListRegularizacionArma();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * FUNCIÓN PARA CREAR NUEVO REGISTRO DE REGULARIZACIÓN DE ARMA
     * @author Richar Fernández
     * @version 1.0     
     * @return Pagina a redireccionar
     */
    public String crear() {
        try {
            String path = "" ,pathDocumento = "";
            if(validacionCreate()){
                registro = (AmaRegEmpArma) JsfUtil.entidadMayusculas(registro, "");            
                String fileName = "";
                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
                if(per != null){
                    registro.setPersonaId(per);
                    if(registro.getSituacionArma() != null){
                        if(!registro.getSituacionArma().getCodProg().equals("TP_SITU_INC")){
                            registro.setFechaIncauta(null);
                        }
                    }
                    if(opcionPanel1 != null){
                        lstFotos = new ArrayList();
                        registro.setModeloId(null);
                        registro.setNombreFoto1Id(null);
                        registro.setNombreFoto2Id(null);
                        registro.setRegionId(regionInpeNoPropiedad);
                        registro.setPenalId(penalInpeNoPropiedad);
                        
                        if(registro.getSituacionArma().getCodProg().equals("TP_EST_EMP_NOP") || registro.getSituacionArma().getCodProg().equals("TP_SITU_VEN")){
                            registro.setEstadoVerfica(2L);
                        }else{
                            registro.setEstadoVerfica(1L);
                        }
                        
                        registro.setNdocPosibleProp(numDocPosiblePropietario);
                        if(registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") && datosActaInt != null && registro.getModeloId() == null){
                            registro.setModeloId(Long.parseLong(datosActaInt.get("ID_MODELO").toString()));
                        }
                        if(fileAdjunto1 != null){
                            pathDocumento = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_documento").getValor();
                            fileName = fileAdjunto1.getFileName();
                            fileName = fileName.toLowerCase();
                            registro.setAdjuntoDocId("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_DCRE").toString()+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                            FileUtils.writeByteArrayToFile(new File(pathDocumento + registro.getAdjuntoDocId()), fileByte );
                        }
                    }else{
                        registro.setEstadoArmaId(estadoSituacion);
                        registro.setSituacionArma(situacionArma);
                        registro.setDescMotivo(observacion);
                        registro.setRegionId(regionInpe);
                        registro.setPenalId(penalInpe);
                        registro.setEstadoVerfica(1L);
                        if(selectedArma != null){
                            registro.setModeloId(selectedArma.getId());
                        }
                        if(!esPerfilInpe()){
                            registro.setSituacionArma(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_SITU_POS"));
                        }
                        
                        if(lstFotos != null && !lstFotos.isEmpty()){
                            path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
                            fileName = ""+ lstFotos.get(0).get("nombre");
                            fileName = fileName.toLowerCase();
                            registro.setNombreFoto1Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"a"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                            FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto1Id()), (byte[])lstFotos.get(0).get("byte"));

                            fileName = ""+ lstFotos.get(1).get("nombre");
                            fileName = fileName.toLowerCase();
                            registro.setNombreFoto2Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"b"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                            FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto2Id()), (byte[])lstFotos.get(1).get("byte"));
                        }
                        registro.setAdjuntoDocId(null);                            
                    }
                    if(esPerfilInpe()){
                        registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_INP"));
                    }else{
                        registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_SEG"));
                    }
                    validarNombresAdjuntos();
                    ejbAmaRegEmpArmaFacade.create(registro);
                    JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroCreado"));

                    prepareListRegularizacionArma();
                }else{
                    JsfUtil.mensajeError("No se encontraron datos de la persona: "+p_user.getNumDoc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
        return null;
    }
    
    /**
     * FUNCIÓN PARA CREAR NUEVO REGISTRO DE NUEVA ARMA DE FUEGO
     * @author Richar Fernández
     * @version 1.0     
     * @return Pagina a redireccionar
     */
    public String crearNuevaArma(){
        try {
            String path = "", pathDocumento = "";
            if(validacionCreateNuevaArma()){
                registro = (AmaRegEmpArma) JsfUtil.entidadMayusculas(registro, "");            
                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());                
                
                if(per == null){
                    JsfUtil.mensajeError("No se encontraron datos de la persona: "+p_user.getNumDoc());
                    return null;
                }
                registro.setPersonaId(per);
                String fileName = "";

                registro.setEstadoVerfica(3L);
                registro.setModeloId(selectedArma.getId());

                if(registro.getEstadoArmaId() != null && registro.getEstadoArmaId().getCodProg().equals("TP_ESTA_OPE")){
                    path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto").getValor();
                    fileName = ""+ lstFotos.get(0).get("nombre");
                    fileName = fileName.toLowerCase();
                    registro.setNombreFoto1Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"a"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                    FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto1Id()), (byte[])lstFotos.get(0).get("byte"));

                    fileName = ""+ lstFotos.get(1).get("nombre");
                    fileName = fileName.toLowerCase();
                    registro.setNombreFoto2Id("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_FTRE").toString()+"b"+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                    FileUtils.writeByteArrayToFile(new File(path + registro.getNombreFoto2Id()), (byte[])lstFotos.get(1).get("byte"));
                }else{
                    registro.setPesoArma(null);
                }
                pathDocumento = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_documento").getValor();
                fileName = fileAdjunto1.getFileName();
                fileName = fileName.toLowerCase();
                registro.setAdjuntoDocId("GAMAC_"+ejbNumeracionFacade.buscarNumeracionActual("NUM_DCRE").toString()+fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                FileUtils.writeByteArrayToFile(new File(pathDocumento + registro.getAdjuntoDocId()), fileByte );

                if(esPerfilInpe()){
                    registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_INP"));
                    
                    if(registro.getSituacionArma().getCodProg().equals("TP_SITU_VEN")){
                        registro.setNdocPosibleProp(numDocPosiblePropietario);
                    }
                }else{
                    registro.setTipoReguId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_PR_RAESS_SEG"));
                }

                validarNombresAdjuntos();
                ejbAmaRegEmpArmaFacade.create(registro);
                JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroCreado"));

                prepareListRegularizacionArma();
            }
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public AmaRegEmpArma getAmaRegEmpArma(Long id) {
        return ejbAmaRegEmpArmaFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public AmaRegEmpArmaController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "amaRegEmpArmaConverter")
    public static class AmaRegEmpArmaControllerConverterN extends AmaRegEmpArmaControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = AmaRegEmpArma.class)
    public static class AmaRegEmpArmaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AmaRegEmpArmaController c = (AmaRegEmpArmaController) JsfUtil.obtenerBean("amaRegEmpArmaController", AmaRegEmpArmaController.class);
            return c.getAmaRegEmpArma(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AmaRegEmpArma) {
                AmaRegEmpArma o = (AmaRegEmpArma) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + AmaRegEmpArma.class.getName());
            }
        }
    }
    
    /**
     * FUNCIÓN AL CAMBIAR TIPO BÚSQUEDA 
     * @author Richar Fernández
     * @version 1.0     
     */
    public void cambiaTipoBusqueda(){
        if(tipoBusqueda != null){
            if(tipoBusqueda.equals("3")){
                filtro = null;
                setMostrarEstadoLicencia(true);
            }else{
                tipoEstado = null;
                setMostrarEstadoLicencia(false);
            }
        }else{
            filtro = null;
            tipoEstado = null;
            JsfUtil.mensajeError("Por favor, seleccione un tipo de búsqueda");
        }
    }
    
    /**
     * FUNCIÓN PARA MOSTRAR FORM. DE REGULARIZACIÓN DE ARMAS
     * @author Richar Fernández
     * @version 1.0     
     * @return Página a redireccionar.
     */
    public String prepareListRegularizacionArma(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        reiniciarCamposBusqueda();  
        setBlnSubsanar(false);
        resultadosArmas = null;
        lstExpedientesImpresion = new ArrayList();
        
        if(blnSubsanar){
            estado = EstadoCrud.BUSCAROBS;
            return "/aplicacion/gamac/amaRegEmpArma/ListObservaciones";    
        }
        if(!esPerfilInpe()){
            habilitaJuridica = true;
            fechaLimiteJuridica = JsfUtil.stringToDate(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_fechaLimiteJuridica").getValor() ,"dd/MM/yyyy");
            if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fechaLimiteJuridica)) > 0){
                JsfUtil.mensajeAdvertencia("La fecha límite para regularizar las armas ha culminado");
                habilitaJuridica = false;
            }
            
            SbUsuarioGt userExonerado = ejbUsuarioFacade.buscarUsuarioExonerado(p_user.getNumDoc());
            if(userExonerado != null && userExonerado.getDescripcion() == null) 
                userExonerado.setDescripcion("");
            
            int contAutorizado = ejbRaessFacade.contarResolucionesVigentesEmpresa(p_user.getNumDoc());
            if(contAutorizado == 0 ){
                JsfUtil.mensajeError("Usted no cuenta con AUTORIZACIÓN VIGENTE PARA UTILIZAR ARMAS EN SUS ACTIVIDADES. Por Favor acérquese a las Oficinas de SUCAMEC para regularizar");
                return null;
            }             
            estado = EstadoCrud.BUSCAR;
            try {
                lstExpedientesImpresion = ejbAmaRegEmpArmaFacade.obtenerExpedienteMasterJuridicas(p_user.getNumDoc());
                if(lstExpedientesImpresion != null && lstExpedientesImpresion.size() > 0){
                    setRenderImprimir(true);
                }else{
                    setRenderImprimir(false);
                }
                buscarBandeja(renderImprimir, renderRegistrar, blnGenerarPdf, true);
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.mensajeError("Hubo un error al validar búsqueda de registros");
            }
        }else{
            habilitaInpe = true;
            fechaLimiteInpe = JsfUtil.stringToDate(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_fechaLimiteInpe").getValor() ,"dd/MM/yyyy");
            if(JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fechaLimiteInpe)) > 0){
                JsfUtil.mensajeAdvertencia("La fecha límite para regularizar las armas ha culminado");
                habilitaInpe = false;
            }
            
            //VALIDACIONES PARA ADMINISTRADOS INPE
            lstExpedientesImpresion = ejbAmaRegEmpArmaFacade.obtenerExpedienteMasterInpe(p_user.getNumDoc());
            
            if(lstExpedientesImpresion != null && lstExpedientesImpresion.size() > 0){
                setRenderImprimir(true);
            }else{
                setRenderImprimir(false);
            }

            estado = EstadoCrud.BUSCAR;
            buscarBandeja(renderImprimir, renderRegistrar,blnGenerarPdf, false);
        }
        
        return "/aplicacion/gamac/amaRegEmpArma/List";
    }
    
    /**
     * FUNCIÓN PARA LIMPIAR DATOS DE BÚSQUEDA DE BANDEJA
     * @author Richar Fernández
     * @version 1.0     
     */
    public void reiniciarCamposBusqueda(){
        filtro = null;
        tipoBusqueda = null;
        tipoEstado = null;
        foto = null;
        fotoByte = null;
        file = null;
        fileAdjunto1 = null;
        currentDate = new Date();
        regionInpeSelected = null;
        setErrorArchivo(false);
        setMostrarEstadoLicencia(true);        
        setRenderImprimir(false);
        setRenderRegistrar(true);
        setBlnGenerarPdf(false);
        txtBtnImprimir = "Imprimir";
    }
    
   /**
     * FUNCIÓN PARA BUSCAR DATOS DE REGULARIZACIÓN DE ARMAS 
     * @author Richar Fernández
     * @version 1.0
     * @param rdImprimir Flag de renderización de botón Imprimir
     * @param rdRegistrar Flag de renderización de btotón Registrae
     * @param rdGenerarPdf flag de proceso incompleto
     * @param flagDepurarRepetidos Flag para eliminar registros duplicados
     * @param flagBusquedaInicial
     */
     public void buscarBandeja(boolean rdImprimir, boolean rdRegistrar, boolean rdGenerarPdf, boolean flagBusquedaInicial){
        
        if(tipoBusqueda != null ) {
            if((tipoBusqueda.equals("1") || tipoBusqueda.equals("2")) && (filtro == null || filtro.isEmpty())){
                JsfUtil.mensajeError("Por favor, ingresar el criterio a buscar");
                return;
            }else{
                if(tipoBusqueda.equals("3") && tipoEstado == null){
                    JsfUtil.mensajeError("Por favor, seleccione un estado a filtrar");
                    return;
                }
            }
        }else{
            if(tipoBusqueda == null && tipoEstado != null){
                JsfUtil.mensajeError("Por favor, seleccione un tipo de búsqueda");
                return;
            }else{
                if(tipoBusqueda == null && filtro != null ){
                    JsfUtil.mensajeError("Por favor, seleccione un tipo de búsqueda");
                    return;
                }
            }
        }
        
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        HashMap mMap = new HashMap();        
        mMap.put("filtro", (filtro != null)?filtro.trim().toUpperCase():null);
        mMap.put("idEmpresa", p_user.getPersona().getId());
        mMap.put("ruc", Long.parseLong(p_user.getNumDoc()));        
        mMap.put("tipoBusqueda", tipoBusqueda);
        mMap.put("tipoEstado", tipoEstado);
        mMap.put("regionInpeSelected", ((regionInpeSelected != null)?regionInpeSelected.getId():null));
        
        if(esPerfilInpe()){
            resultadosArmas =  ejbArmaFacadeFacade.buscarArmasRegularizacionInpe(mMap, false);
            //resultadosArmas =  ejbAmaRegEmpArmaFacade.buscarArmasRegularizacionInpe(mMap, false);
        }else{
            resultadosArmas =  ejbArmaFacadeFacade.buscarArmasRegularizacionJuridicas(mMap, false);
        }
        verificarRegistrosDuplicados();   // En caso se encuentren armas verificadas y despues han sido emitidas
        
//        if(flagBusquedaInicial){
//            filtrarSoloPendientes();
//        }
        
        reiniciarCamposBusqueda();
        setRenderImprimir(rdImprimir);
        setRenderRegistrar(rdRegistrar);
        setBlnGenerarPdf(rdGenerarPdf);
    }
    
    public void filtrarSoloPendientes(){
        if(resultadosArmas == null){
            return;
        }
        List<Map> lstTemp = new ArrayList(resultadosArmas);
        for(Map item : lstTemp){
            if(item.get("TIPO") != null && !item.get("TIPO").toString().equals("5")){
                resultadosArmas.remove(item);
            }
        }
    }
    
    public void verificarDuplicadosRegistrosFinalizados(){
        int cont = 0;
        List<Map> lstArmas = new ArrayList();
        Map verif = null;
        
        if(resultadosArmas != null){
            for(int i = 0; i < resultadosArmas.size(); i++){
                verif = resultadosArmas.get(i);
                if(verif.get("ESTADO_VERFICA") != null){
                    if(verif.get("ESTADO_VERFICA").toString().equals("1") || verif.get("ESTADO_VERFICA").toString().equals("2") ){
                        cont = 0;
                        for(int j= (i+1); j< resultadosArmas.size();j++ ){
                            Map nmap = resultadosArmas.get(j);
                            if(Objects.equals( verif.get("NROEXP"), nmap.get("NROEXP")) && nmap.get("ESTADO_VERFICA").toString().equals("4") ){
                                nmap.put("ID", verif.get("ID"));
                                cont++;
                            }
                        }

                        if(cont > 0){

                        }else{
                            lstArmas.add(verif);
                        }
                    }else{
                        lstArmas.add(verif);
                    }
                }else{
                    lstArmas.add(verif);
                }
            }
            resultadosArmas = lstArmas;
        }
    }
    
    /**
     * FUNCIÓN PARA VALIDAR ARMAS VERIFICADAS Y DESPUES HAN SIDO EMITIDAS
     * @author Richar Fernández
     * @version 1.0
     */
    public void verificarRegistrosDuplicados(){
        int cont;
        String idTemp, ids;
        List<Map> lstArmas = new ArrayList(), lstDesactivados = new ArrayList(), lstTemp = new ArrayList();
        Map verif = null;
        
        if(resultadosArmas != null){
            for(int i = 0; i < resultadosArmas.size(); i++){
                cont = 0;
                ids = "";
                idTemp = "";
                lstTemp = new ArrayList();
                verif = resultadosArmas.get(i);
                if(!lstDesactivados.contains(verif)){
                    if(verif.get("ESTADO_VERFICA") == null){
                      if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }
                        continue;  
                    }
                    if(!(verif.get("ESTADO_VERFICA").toString().equals("1") || verif.get("ESTADO_VERFICA").toString().equals("2") )){
                        if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }
                        continue;
                    }

                    for(int j= (i+1); j< resultadosArmas.size();j++ ){
                        Map nmap = resultadosArmas.get(j);

                        if(nmap.get("ESTADO_VERFICA") != null){
                            if(nmap.get("ESTADO_VERFICA").toString().equals("1") || nmap.get("ESTADO_VERFICA").toString().equals("2") ){
                                if(verif.get("NRO_LIC") != null && verif.get("NRO_SERIE") != null){
                                    if( Objects.equals(verif.get("NRO_LIC"), nmap.get("NRO_LIC")) && Objects.equals(verif.get("NRO_SERIE"), nmap.get("NRO_SERIE")) ){
                                        cont++;

                                        if(ids.isEmpty()){
                                            ids = ""+nmap.get("ID");
                                            lstTemp.add(nmap);
                                        }else{
                                            ids = ids + ", " +nmap.get("ID");
                                            lstTemp.add(nmap);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(cont > 1){
                        for(Map temp : lstTemp){
                            lstDesactivados.add(temp);
                        }

                        List<AmaRegEmpArma> lsProcesados = ejbAmaRegEmpArmaFacade.buscarArmasProcesarDuplicadosAsc(ids);
                        int contRepetidos = 0;
                        for(AmaRegEmpArma arma : lsProcesados){
                            if(contRepetidos == 0){
                                if(!lstArmas.contains(verif)){
                                    lstArmas.add(verif);   
                                }
                            }else{
                                arma.setActivo(JsfUtil.FALSE);
                                ejbAmaRegEmpArmaFacade.edit(arma);
                            }
                            contRepetidos++;
                        }
                    }else{
                        if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }
                    }
                }
            }
            lstDesactivados = new ArrayList();
            resultadosArmas = new ArrayList();
            resultadosArmas = lstArmas;
            lstArmas = new ArrayList();

            /// VERIFICA TIPO 1, TIPO 2 Y TIPO 4
            for(int i = 0; i < resultadosArmas.size(); i++ ){
                cont = 0;
                ids = "";
                idTemp = "";
                lstTemp = new ArrayList();
                verif = resultadosArmas.get(i);

                if(verif.get("ESTADO_VERFICA") == null){
                   if(!lstArmas.contains(verif)){
                        lstArmas.add(verif);   
                    }
                    continue;  
                }

                if(verif.get("ESTADO_VERFICA").toString().equals("4")){
                    if(!lstArmas.contains(verif)){
                        lstArmas.add(verif);
                    }
                    continue;
                }

                if(!lstDesactivados.contains(verif)){

                    if(!(verif.get("ESTADO_VERFICA").toString().equals("1") || verif.get("ESTADO_VERFICA").toString().equals("2") || verif.get("ESTADO_VERFICA").toString().equals("4") )){
                        if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);
                        }
                        continue;
                    }

                    for(int j= (i+1); j< resultadosArmas.size();j++ ){
                        Map nmap = resultadosArmas.get(j);
                        if(nmap.get("ESTADO_VERFICA") != null){
                            if(nmap.get("ESTADO_VERFICA").toString().equals("1") || nmap.get("ESTADO_VERFICA").toString().equals("2") || nmap.get("ESTADO_VERFICA").toString().equals("4") ){
                                if(verif.get("NRO_SERIE") != null && verif.get("NRO_LIC") != null){
                                    if( Objects.equals(verif.get("NRO_SERIE"), nmap.get("NRO_SERIE")) && Objects.equals(verif.get("NRO_LIC"), nmap.get("NRO_LIC")) ){
                                        cont++;

                                        if(ids.isEmpty()){
                                            ids = ""+nmap.get("ID");
                                            lstTemp.add(nmap);
                                        }else{
                                            ids = ids + ", " +nmap.get("ID");
                                            lstTemp.add(nmap);
                                        }

                                        if(!verif.get("ESTADO_VERFICA").toString().equals("4") && idTemp.isEmpty()){
                                            idTemp = ""+verif.get("ID");
                                        }
                                    }                        
                                }
                            }
                        }   
                    }

                    if(cont > 1){
                        for(Map temp : lstTemp){
                            lstDesactivados.add(temp);
                        }
                        List<AmaRegEmpArma> lsProcesados = ejbAmaRegEmpArmaFacade.buscarArmasProcesarDuplicadosDesc(ids);
                        int contRepetidos = 0;
                        for(AmaRegEmpArma arma : lsProcesados){
                            if(contRepetidos == 0){
                                if(!lstArmas.contains(verif)){
                                    lstArmas.add(verif);   
                                }
                            }else{
                                if(arma.getEstadoVerfica() == 4){
                                    // no importa duplicados
                                    if(!lstArmas.contains(verif)){
                                        lstArmas.add(verif);   
                                    }
                                }else{
                                    if(arma.getExpedienteMaster() == null) {
                                        //Map armaEmitida = verificaSiExisteArmaEmitida(""+verif.get("NRO_SERIE"), ""+verif.get("NRO_LIC"));
                                        // Aun no se han enviado las armas
                                        arma.setEstadoVerfica(4L);  // Se cambia a Arma verificada
                                        //arma.setModeloId(Long.parseLong(""+ armaEmitida.get("COD_MODELO")));
                                        ejbAmaRegEmpArmaFacade.edit(arma);
                                    }
                                }
                            }
                            contRepetidos++;
                        }
                    }else{
                        if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }
                    }
                }                
            }
            lstDesactivados = new ArrayList();
            resultadosArmas = new ArrayList();
            resultadosArmas = lstArmas;
            lstArmas = new ArrayList();

            /// VERIFICA TIPO 3
            for(int i = 0; i < resultadosArmas.size(); i++ ){
                cont = 0;
                ids = "";
                idTemp = "";
                lstTemp = new ArrayList();
                verif = resultadosArmas.get(i);

                if(!lstDesactivados.contains(verif)){

                    if(verif.get("ESTADO_VERFICA") == null){
                      if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }
                        continue;  
                    }

                    if(!verif.get("ESTADO_VERFICA").toString().equals("3") ){
                        if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }
                        continue;
                    }

                    for(int j= (i+1); j< resultadosArmas.size();j++ ){
                        Map nmap = resultadosArmas.get(j);
                        if(nmap.get("ESTADO_VERFICA") != null){
                            if(nmap.get("ESTADO_VERFICA").toString().equals("3") ){
                                if(verif.get("COD_MODELO") != null && verif.get("NRO_SERIE") != null){
                                    if( Objects.equals(verif.get("COD_MODELO"), nmap.get("COD_MODELO")) && Objects.equals(verif.get("NRO_SERIE"), nmap.get("NRO_SERIE")) ){
                                        cont++;

                                        if(ids.isEmpty()){
                                            ids = ""+nmap.get("ID");
                                            lstTemp.add(nmap);
                                        }else{
                                            ids = ids + ", " +nmap.get("ID");
                                            lstTemp.add(nmap);
                                        }
                                    }
                                }
                            }
                        }   
                    }

                    if(cont > 1){
                        for(Map temp : lstTemp){
                            lstDesactivados.add(temp);
                        }

                        List<AmaRegEmpArma> lsProcesados = ejbAmaRegEmpArmaFacade.buscarArmasProcesarDuplicadosAsc2(ids);
                        int contRepetidos = 0;
                        for(AmaRegEmpArma arma : lsProcesados){
                            if(contRepetidos == 0){
                                if(!lstArmas.contains(verif)){
                                    lstArmas.add(verif);   
                                }
                            }else{
                                arma.setActivo(JsfUtil.FALSE);
                                ejbAmaRegEmpArmaFacade.edit(arma);
                            }
                            contRepetidos++;
                        }
                    }else{
                        if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }
                    }
                }   
            }
            lstDesactivados = new ArrayList();
            resultadosArmas = new ArrayList();
            resultadosArmas = lstArmas;
            lstArmas = new ArrayList();

            /// VERIFICA TIPO 3 Y TIPO 4
            for(int i = 0; i < resultadosArmas.size(); i++ ){
                cont = 0;
                ids = "";
                idTemp = "";
                lstTemp = new ArrayList();
                verif = resultadosArmas.get(i);

                if(verif.get("ESTADO_VERFICA") == null){
                    if(!lstArmas.contains(verif)){
                        lstArmas.add(verif);
                    }
                    continue;
                }

                if(verif.get("ESTADO_VERFICA").toString().equals("4")){
                    if(!lstArmas.contains(verif)){
                        lstArmas.add(verif);
                    }
                    continue;
                }

                if(!lstDesactivados.contains(verif)){
                    if(!(verif.get("ESTADO_VERFICA").toString().equals("3") || verif.get("ESTADO_VERFICA").toString().equals("4")  )){
                        if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }                    
                        continue;
                    }            

                    for(int j= (i+1); j< resultadosArmas.size();j++ ){
                        Map nmap = resultadosArmas.get(j);

                        if(nmap.get("ESTADO_VERFICA") != null){
                            if(nmap.get("ESTADO_VERFICA").toString().equals("3") || nmap.get("ESTADO_VERFICA").toString().equals("4")  ){
                                if(verif.get("COD_MODELO") != null && verif.get("NRO_SERIE") != null){
                                    if( Objects.equals(verif.get("COD_MODELO"), nmap.get("COD_MODELO")) && Objects.equals(verif.get("NRO_SERIE"), nmap.get("NRO_SERIE")) ){
                                        cont++;

                                        if(ids.isEmpty()){
                                            ids = ""+nmap.get("ID");
                                            lstTemp.add(nmap);
                                        }else{
                                            ids = ids + ", " +nmap.get("ID");
                                            lstTemp.add(nmap);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(cont > 1){
                        for(Map temp : lstTemp){
                            lstDesactivados.add(temp);
                        }

                        List<AmaRegEmpArma> lsProcesados = ejbAmaRegEmpArmaFacade.buscarArmasProcesarDuplicadosDesc(ids);
                        int contRepetidos = 0;
                        for(AmaRegEmpArma arma : lsProcesados){
                            if(contRepetidos == 0){
                                if(!lstArmas.contains(verif)){
                                    lstArmas.add(verif);   
                                }
                            }else{
                                if(arma.getEstadoVerfica() == 4){
                                    // no importa duplicados
                                    if(!lstArmas.contains(verif)){
                                        lstArmas.add(verif);   
                                    }
                                }else{
                                    if(arma.getExpedienteMaster() == null) {
                                        //Map armaEmitida = verificaSiExisteArmaEmitida(""+verif.get("NRO_SERIE"), Long.parseLong(""+verif.get("COD_MODELO")));
                                        // Aun no se han enviado las armas
                                        arma.setEstadoVerfica(4L);  // Se cambia a Arma verificada
                                        //arma.setModeloId(Long.parseLong(""+ armaEmitida.get("COD_MODELO")));
                                        ejbAmaRegEmpArmaFacade.edit(arma);
                                    }
                                }
                            }
                            contRepetidos++;
                        }
                    }else{
                        if(!lstArmas.contains(verif)){
                            lstArmas.add(verif);   
                        }
                    }
                }
            }
            lstDesactivados = new ArrayList();
            resultadosArmas = new ArrayList();
            resultadosArmas = lstArmas;
            lstArmas = new ArrayList();

            /// ACTUALIZAR A TIPO 4
            for(int i = 0; i < resultadosArmas.size(); i++ ){
                cont = 0;
                ids = "";
                idTemp = "";
                lstTemp = new ArrayList();
                verif = resultadosArmas.get(i);

                if(!lstArmas.contains(verif)){
                    lstArmas.add(verif);
                }

                if(verif.get("TIPO") != null && verif.get("ESTADO_VERFICA") != null){
                    if(verif.get("TIPO").toString().equals("4") &&  !Objects.equals(verif.get("TIPO") , verif.get("ESTADO_VERFICA")) ){
                        cont++;
                        if(ids.isEmpty()){
                            ids = ""+verif.get("ID");
                        }else{
                            ids = ids + ", " +verif.get("ID");
                        }
                    }
                }
                if(cont > 0){
                    List<AmaRegEmpArma> lsProcesados = ejbAmaRegEmpArmaFacade.buscarArmasProcesarDuplicadosDesc(ids);                
                    for(AmaRegEmpArma arma : lsProcesados){
                        if(arma.getEstadoVerfica() != 4L){
                            arma.setEstadoVerfica(4L);
                            ejbAmaRegEmpArmaFacade.edit(arma);
                        }   
                    }
                }
            }

            lstDesactivados = new ArrayList();
            resultadosArmas = new ArrayList();
            resultadosArmas = lstArmas;
            lstArmas = new ArrayList();
        }
    }
    
    public Map verificaSiExisteArmaEmitida(String serie, Long codModelo){
        Map armaEmitida = null;
        for(Map verif : resultadosArmas ){
            if(Objects.equals(""+verif.get("NRO_SERIE"), serie) && Objects.equals(Long.parseLong(""+verif.get("COD_MODELO")), codModelo) && verif.get("ESTADO_VERFICA").toString().equals("4")){
                armaEmitida = verif;
                break;
            }
        }
        
        return armaEmitida;
    }
    
    public Map verificaSiExisteArmaEmitida(String serie, String licencia){
        Map armaEmitida = null;
        for(Map verif : resultadosArmas ){
            if(Objects.equals(""+verif.get("NRO_SERIE"), serie) && Objects.equals(""+verif.get("NRO_LIC"), licencia) && verif.get("ESTADO_VERFICA").toString().equals("4")){
                armaEmitida = verif;
                break;
            }
        }
        
        return armaEmitida;
    }
    
    public boolean mostrarBtnBorrar(Map reg){
        boolean validacion = false;
        
        if(reg.get("TIPO").toString().equals("3") ){
            validacion = true;
        }
        if(reg.get("NROEXP") != null){
            validacion = false;
        }
        if(esPerfilInpe()){
            return validacion && habilitaInpe;
        }else{
            return validacion && habilitaJuridica;
        }
    }
    
    public void borrarRegularizacionArma(Map reg){
        try {
            AmaRegEmpArma arma = ejbAmaRegEmpArmaFacade.obtenerArmaVerificada(Long.parseLong(""+reg.get("ID")));
            arma.setActivo(JsfUtil.FALSE);        
            ejbAmaRegEmpArmaFacade.edit(arma);

            for(Map regArma : resultadosArmas){
                if(Objects.equals(reg, regArma)){
                    resultadosArmas.remove(regArma);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al eliminar el registro");
        }   
    }
    
    /**
     * FUNCIÓN PARA OBTENER DATOS DE DECLARACIÓN JURADA.
     * @author Richar Fernández
     * @version 1.0     
     * @return Texto de declaración Jurada
     */
    public String obtenerDatosJuramento(){
        String cadena = "";
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        
        try{
            cadena = JsfUtil.bundleBDIntegrado("amaArma_texto1DeclaracionJurada");            
            SbUsuarioGt usu = ejbUsuarioFacade.buscarUsuarioByLogin(p_user.getLogin());
            SbDireccionGt direccionInpe = ejbSbDireccionFacade.mostrarDireccionXRegionInpe("TP_RGN_CENT");  
            if(direccionInpe == null){
                JsfUtil.mensajeError("No se encontró la dirección de la SEDE CENTRAL INPE");
                return cadena;
            }
            if(usu != null){
                cadena = cadena + "Yo, "+ p_user.getNombres()+ " "+ p_user.getApePat()+ " " + p_user.getApeMat() + " identificado con Nro. de documento "+ p_user.getLogin()+", en calidad de usuario responsable del registro de las armas de fuego de la empresa "+
                                p_user.getPersona().getRznSocial() +", con "+ReportUtil.mostrarTipoYNroDocumemtoString_arma(p_user.getPersona())+", domiciliada en "+ ReportUtil.mostrarDireccionEspecifica(direccionInpe);
                
                cadena = cadena + JsfUtil.bundleBDIntegrado("amaArma_texto2DeclaracionJurada") + "<br/><br/> ";
                ////
                cadena = cadena + JsfUtil.bundleBDIntegrado("amaArma_texto3DeclaracionJurada") + "("+(contParrafo1)+ ") " + JsfUtil.bundleBDIntegrado("amaArma_texto31DeclaracionJurada") + "<br/><br/> ";
                ///
                cadena = cadena +  JsfUtil.bundleBDIntegrado("amaArma_texto4DeclaracionJurada") + "("+(contParrafo2)+ ") " + JsfUtil.bundleBDIntegrado("amaArma_texto41DeclaracionJurada")+ "<br/><br/> ";
                ///
                cadena = cadena +  JsfUtil.bundleBDIntegrado("amaArma_texto5DeclaracionJurada") + "("+(contParrafo3)+ ") " + JsfUtil.bundleBDIntegrado("amaArma_texto51DeclaracionJurada")+ "<br/><br/> ";
                ///
                cadena = cadena +  JsfUtil.bundleBDIntegrado("amaArma_texto6DeclaracionJurada");
            }else{
                JsfUtil.mensajeError("No se encontró al usuario "+ p_user.getLogin());
            }
        }catch(Exception ex){
            JsfUtil.mensajeError("No se encontró a la persona con el documento "+ p_user.getLogin());
        }
        
        return cadena;
    }    
    
    /**
     * FUNCIÓN OBTENER ESTILO DE COLOR DE BANDEJA DE REGULARIZACIÓN DE ARMAS
     * @author Richar Fernández
     * @version 1.0
     * @param map Map con datos de arma
     * @return Cadena con estilo de celda
     */
    public String estiloPorTipo(Map map){
        String estilo = "";
        switch(map.get("TIPO").toString()){
            case "1":
                    estilo = "datatable-row-verificado";
                    break;
            case "2":
                    estilo = "datatable-row-nopertenece";
                    break;
            case "3":
                    estilo = "datatable-row-nuevo";
                    break;
            case "4":
                    estilo = "datatable-row-emitidos";
                    break;
            case "5":
                    estilo = "datatable-row-sinverificar";
                    break;
            default:
                    estilo = "datatable-row-sinverificar";
                    break;
        }
        
        return estilo;
    }
    
    /**
     * FUNCIÓN PARA VERIFICAR SI EXPEDIENTE HA SIDO PROCESADO PARCIALMENTE Y POSEE EXPEDIENTE MASTER
     * @author Richar Fernández
     * @version 1.0
     * @param lstArmasRegistro Listado de armas
     * @return Cadena con estilo de celda
     */
    public boolean verificaRegistrosConExpediente(List<AmaRegEmpArma> lstArmasRegistro){
        boolean tieneExpediente = false;
        
        for(AmaRegEmpArma arma : lstArmasRegistro){
            if(arma.getExpediente() != null && !arma.getExpediente().isEmpty()){
                tieneExpediente = true;
                break;
            }
        }
        
        return tieneExpediente;
    }
    
    public boolean verificaRegistrosConExpedienteJuridicas(List<AmaRegEmpArma> lstArmasRegistro){
        boolean tieneExpediente = false;
        
        for(AmaRegEmpArma arma : lstArmasRegistro){
            if(arma.getExpedienteMaster() != null && !arma.getExpedienteMaster().isEmpty()){
                tieneExpediente = true;
                break;
            }
        }
        
        return tieneExpediente;
    }
    
    /**
     * FUNCIÓN PARA OBTENER EXPEDIENTE MASTER DE LISTADO DE ARMAS
     * @author Richar Fernández
     * @version 1.0
     * @param lstArmasRegistro Listado de armas
     * @return Expediente Master
     */
    public String obtenerExpedienteMaster(List<AmaRegEmpArma> lstArmasRegistro){
        String expedienteMaster = "";
        
        for(AmaRegEmpArma arma : lstArmasRegistro){
            if(arma.getExpedienteMaster() != null){
                expedienteMaster = arma.getExpedienteMaster();
                break;
            }
        }
        
        return expedienteMaster;
    }
    
    public void crearHistoricoProcesoRegArma(short estado, SbPersonaGt persona, String nroExpedienteMaster){
        switch(estado){
            case 1: //INICIADO
                    procesoHis = new AmaRegEmpProceso();
                    procesoHis.setActivo(JsfUtil.TRUE);
                    procesoHis.setEstado(estado);
                    procesoHis.setPersonaId(persona);
                    procesoHis.setFechaIni(new Date());
                    procesoHis.setFechaFin(new Date());
                    procesoHis.setId(null);
                    procesoHis.setExpedienteMaster(nroExpedienteMaster);
                    procesoHis.setAmaRegEmpArmaList(new ArrayList());
                    ejbAmaRegEmpProcesoFacade.create(procesoHis);
                    break;
            case 2: // PROCESO TRUNCO
            case 3: // TERMINADO
                    if(procesoHis == null){
                        procesoHis = new AmaRegEmpProceso();
                        procesoHis.setActivo(JsfUtil.TRUE);
                        procesoHis.setPersonaId(persona);
                        procesoHis.setFechaIni(new Date());
                        procesoHis.setId(null);
                        procesoHis.setExpedienteMaster(nroExpedienteMaster);
                        procesoHis.setAmaRegEmpArmaList(new ArrayList());
                    }
                
                    procesoHis.setEstado(estado);
                    procesoHis.setFechaFin(new Date());
                    ejbAmaRegEmpProcesoFacade.edit(procesoHis);
                    break;
        }
        
    }
    
    public String obtenerNombreProcesoCydoc(){
       return JsfUtil.bundleBDIntegrado("amaRegEmpArma_nombreProcesoRaess_nuevo");
    }
    
    /**
     * FUNCIÓN PARA REGISTRAR ARMAS YA REGULARIZADAS Y CREAR DECLARACIÓN JURADA
     * @author Richar Fernández
     * @version 1.0
     * @return Página a redireccionar
     */
    public String registrarJuridicas(){
        List<AmaRegEmpArma> lstArmasRegistro = new ArrayList();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");            
        SbPersonaGt per = null;
        String asunto = "", expedienteMaster = null;

        try {
            errorArchivo = false;
            boolean validacion = true, tieneExpediente = false;
            List<Map> lstArmasParametroReport = new ArrayList();
            Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");                
            per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
            Integer nroProcesoMaster = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoMaster").getValor());
            Integer nroProcesoIndividualOtros = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoIndividual").getValor());
            Integer nroProcesoIndividualPerRob = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoIndividualPerdRob").getValor());
            Integer nroProcesoIndividualObsNoTit = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoIndividualObsNoTit").getValor());
            Integer nroProcesoIndividualAproIlic = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoIndividualAprop").getValor());            
            Integer nroProcesoIndividual;

            if(usuaTramDoc != null){
                if(per != null){
                    lstArmasRegistro = ejbAmaRegEmpArmaFacade.obtenerListadoArmasRegularizacionRaess(p_user.getNumDoc(), null, false);
                    if(lstArmasRegistro == null || lstArmasRegistro.isEmpty()){
                        JsfUtil.mensajeError("No se encontraron las armas para regularizar");
                        return null;
                    }
                    tieneExpediente = verificaRegistrosConExpedienteJuridicas(lstArmasRegistro);

                    //CREACION DE EXPEDIENTE MAESTRO
                    if(tieneExpediente){
                        expedienteMaster = obtenerExpedienteMaster(lstArmasRegistro);
                    }else{
                        asunto = "Por evaluar:"+ contPorEmitir +", Emitidos:"+ contEmitidos +", No me pertenecen:"+contNoPertenecen;
                        expedienteMaster = wsTramDocController.crearExpediente_regularizacion(null, per , asunto , nroProcesoMaster, usuaTramDoc, usuaTramDoc, obtenerNombreProcesoCydoc());
                    }

                    if(expedienteMaster != null){
                        crearHistoricoProcesoRegArma((short) 1, per, expedienteMaster);

                        //CREACION DE EXPEDIENTE POR REGISTRO
                        AmaModelos modelo;
                        Map armaDisca = null;

                        //// Progress Bar
                        actualProcesado = 0;
                        totalProcesados = lstArmasRegistro.size();
                        /////////

                        for(AmaRegEmpArma arma : lstArmasRegistro){
                            Map map = new HashMap();
                            asunto = "";
                            actualProcesado++;

                            if(arma.getExpedienteMaster() != null){
                                //////////////////////////
                                // YA HA SIDO PROCESADO //
                                //////////////////////////
                                if(arma.getEstadoVerfica() == 4){//Emitidas
                                    armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());
                                    map = preparaArmaReporte(arma,null, armaDisca);
                                    map.put("expediente", null );
                                    lstArmasParametroReport.add(map);
                                }else{
                                    if(arma.getEstadoVerfica() != 3){ // Verificadas y No me pertenecen
                                        armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());                                       
                                    }else{
                                        //Nuevo
                                        armaDisca = null;                                        
                                    }
                                    if(arma.getModeloId() != null){
                                        modelo =  ejbAmaModelosFacade.obtenerModeloById(arma.getModeloId());
                                        map = preparaArmaReporte(arma,modelo,null);
                                    }else{
                                        map = preparaArmaReporte(arma,null, armaDisca);
                                    }                                    
                                    map.put("expediente", arma.getExpediente() );
                                    lstArmasParametroReport.add(map);
                                }
                                ////////////////////////////
                            }else{
                                //////////////////////////
                                ///// FALTA PROCESAR /////
                                //////////////////////////

                                if(arma.getEstadoVerfica() == 4){//Emitidas
                                        armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());
                                        map = preparaArmaReporte(arma,null, armaDisca);
                                        map.put("expediente", null );
                                        lstArmasParametroReport.add(map);

                                        // Volviendo a verificar arma antes de actualizar
                                        int conExp = ejbAmaRegEmpArmaFacade.actualizaExpedienteMasterArmaNativo(arma.getId(), "Procesando..." );
                                        if(conExp > 0){
                                            arma.setExpedienteMaster(expedienteMaster);
                                            ejbAmaRegEmpArmaFacade.edit(arma); 
                                            procesoHis.getAmaRegEmpArmaList().add(arma);
                                        }
                                }else{
                                    //////////////////////////
                                    ///// FALTA PROCESAR /////
                                    //////////////////////////
                                    arma.setExpedienteMaster(expedienteMaster);
                                    if(arma.getEstadoVerfica() != 3){ // Verificadas y No me pertenecen
                                        armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());
                                        asunto = asunto + "DATOS DISCA = Serie: "+ armaDisca.get("NRO_SERIE") +", Tipo Arma:" + armaDisca.get("DES_ARM") +", Marca:"+ armaDisca.get("DES_MARCA")+", Modelo:"+ (armaDisca.get("DES_MODELO")!=null?"-":armaDisca.get("DES_MODELO") ) +", Calibre:"+ armaDisca.get("CALIBRE")+ ", Situación del arma:" + armaDisca.get("ESTADO");
                                        if(arma.getEstadoArmaId() != null){
                                            asunto = asunto + "; DATOS ARMA VERIFICADA = Estado del arma:"+ arma.getEstadoArmaId().getNombre();
                                        }else{
                                            asunto = asunto + "; DATOS ARMA VERIFICADA = Situación del arma: "+ arma.getSituacionArma().getNombre() +", Serie:"+ (arma.getSerie() != null?arma.getSerie().trim():"") ;    
                                        }
                                    }else{ //Nuevo
                                        armaDisca = null;
                                        if(arma.getEstadoArmaId() != null){
                                            asunto = "DATOS ARMA VERIFICADA = Estado del arma: "+ arma.getEstadoArmaId().getNombre() +", Situación del arma: "+ arma.getSituacionArma().getNombre() +", Serie:"+arma.getSerie();
                                        }else{
                                            asunto = "DATOS ARMA VERIFICADA = Situación del arma: "+ arma.getSituacionArma().getNombre() +", Serie:"+ (arma.getSerie() != null?arma.getSerie().trim():"");
                                        }
                                    }                        
                                    if(arma.getModeloId() != null){
                                        modelo =  ejbAmaModelosFacade.obtenerModeloById(arma.getModeloId());
                                        asunto = asunto + ", Tipo Arma:" + modelo.getTipoArmaId().getNombre()+", Marca:"+modelo.getMarcaId().getNombre()+", Modelo:"+modelo.getModelo()+", Calibre:"+obtenerCalibreArma(modelo.getAmaCatalogoList());                            
                                        if(arma.getPesoArma() != null){
                                            asunto = asunto + ", Peso:" + arma.getPesoArma();
                                        }
                                        map = preparaArmaReporte(arma,modelo,null);
                                    }else{
                                        if(arma.getPesoArma() != null){
                                            asunto = asunto + ", Peso:" + arma.getPesoArma();
                                        }
                                        map = preparaArmaReporte(arma,null, armaDisca);
                                    }

                                    
                                    if(arma.getEstadoVerfica() == 1 && arma.getSituacionArma().getCodProg().equals("TP_SITU_POS")){
                                        // No generar expediente 
                                        arma.setExpediente(null);
                                        map.put("expediente", null );
                                        lstArmasParametroReport.add(map);
                                        ejbAmaRegEmpArmaFacade.edit(arma);
                                        procesoHis.getAmaRegEmpArmaList().add(arma);
                                    }else{
                                        if(arma.getSituacionArma().getCodProg().equals("TP_SITU_INT")){
                                            // No generar expediente 
                                            arma.setExpediente(null);
                                            map.put("expediente", null );
                                            lstArmasParametroReport.add(map);
                                            ejbAmaRegEmpArmaFacade.edit(arma);
                                            procesoHis.getAmaRegEmpArmaList().add(arma);
                                        }else{
                                            String titulo = obtenerNombreProcesoCydoc();
                                            switch(arma.getSituacionArma().getCodProg()){
                                                case "TP_SITU_PER":
                                                case "TP_SITU_ROB":
                                                case "TP_SITU_ILI":
                                                            nroProcesoIndividual = nroProcesoIndividualPerRob;
                                                            break;
                                                case "TP_SITU_TIT":
                                                            nroProcesoIndividual = nroProcesoIndividualObsNoTit;
                                                            titulo = "POSIBLE PROPIETARIO: " + arma.getNdocPosibleProp();
                                                            break;
                                                case "TP_EST_EMP_NOP":                                                
                                                            nroProcesoIndividual = nroProcesoIndividualAproIlic;
                                                            break;
                                                default:
                                                        nroProcesoIndividual = nroProcesoIndividualOtros;
                                                        break;
                                            }
                                            
                                            // Volviendo a verificar arma antes de guardar
                                            int conExp = ejbAmaRegEmpArmaFacade.actualizaExpedienteArmaNativo(arma.getId(), "Procesando..." );
                                            if(conExp > 0){
                                                String expedienteId = wsTramDocController.crearExpediente_regularizacion(null, per , asunto , nroProcesoIndividual, usuaTramDoc, usuaTramDoc, titulo);
                                                if(expedienteId != null){
                                                    arma.setExpediente(expedienteId);
                                                }else{
                                                    validacion = false;
                                                    JsfUtil.mensajeError("Hubo un error al crear expediente");
                                                    break;
                                                }
                                                map.put("expediente", arma.getExpediente() );
                                                lstArmasParametroReport.add(map);
                                                ejbAmaRegEmpArmaFacade.edit(arma);
                                                procesoHis.getAmaRegEmpArmaList().add(arma);
                                            }
                                        }
                                            
                                    }
                                    ////////////////////////////
                                }
                            }
                        }
                    }else{
                        validacion = false;
                        JsfUtil.mensajeError("Hubo un error al crear expediente");
                    }

                    if(validacion){
                        JsfUtil.mensaje("Registro realizado correctamente");
                        setRenderImprimir(true);
                        setRenderRegistrar(false);
                        setBlnGenerarPdf(false);

                        if (!JsfUtil.buscarPdfRepositorio(expedienteMaster, JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificado")) && lstArmasParametroReport != null ) {
                            if(subirPdf(lstArmasRegistro.get(0),lstArmasParametroReport, expedienteMaster)){
                                asunto = "Por evaluar:"+ contPorEmitir +", Emitidos:"+ contEmitidos +", No me pertenecen:"+contNoPertenecen;                                

                                if(wsTramDocController.adjuntarDeclaracionJurada(usuaTramDoc,"USRWEB",lstArmasRegistro.get(0).getExpedienteMaster(),asunto, FileUtils.readFileToByteArray(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificado") + lstArmasRegistro.get(0).getExpedienteMaster() + ".pdf")) )){
                                    RequestContext.getCurrentInstance().execute("PF('wvDialogDeclaracionJurada').hide()");
                                    RequestContext.getCurrentInstance().update("listBand");
                                    crearHistoricoProcesoRegArma((short) 3, per, expedienteMaster);
                                }else{
                                    crearHistoricoProcesoRegArma((short) 2, per, expedienteMaster);
                                }
                            }else{
                                crearHistoricoProcesoRegArma((short) 2, per, expedienteMaster);
                                setRenderImprimir(false);
                                setRenderRegistrar(false);
                                setBlnGenerarPdf(true);
                                setErrorArchivo(true);
                                JsfUtil.mensajeError("Hubo un error al generar documento de verificación");
                                RequestContext.getCurrentInstance().execute("PF('wvDialogDeclaracionJurada').hide()");
                                RequestContext.getCurrentInstance().update("listBand");
                            }
                        }else{
                            RequestContext.getCurrentInstance().execute("PF('wvDialogDeclaracionJurada').hide()");
                            RequestContext.getCurrentInstance().update("listBand");
                        }
                        return prepareListRegularizacionArma();
                    }else{
                        crearHistoricoProcesoRegArma((short) 2, per, (expedienteMaster != null?expedienteMaster:"-"));
                    }
                }else{
                    JsfUtil.mensajeError("No se encontraron datos de la persona: "+p_user.getNumDoc());
                }
            }else{
                JsfUtil.mensajeError("No se encontró usuario USRWEB");
            }
        } catch (Exception e) {
            e.printStackTrace();
            crearHistoricoProcesoRegArma((short) 2, per, (expedienteMaster != null?expedienteMaster:"-"));
            JsfUtil.mensajeError("No se pudo completar el proceso");
        }
        return null;
    }

        
    /**
     * FUNCIÓN PARA ARMAR MAP CON DATOS DEL ARMA.
     * @author Richar Fernández
     * @version 1.0
     * @param arma Reg. de regularización de arma
     * @param modelo Modelo de arma
     * @param armaDisca Map con datos Disca de Arma
     * @return Map con datos de arma para reporte
     */
    public Map preparaArmaReporte(AmaRegEmpArma arma, AmaModelos modelo, Map armaDisca){
        Map map = new HashMap();
        map.put("serie", arma.getSerie());
        
        if(modelo == null && armaDisca == null){
            map.put("tipoArma", null );
            map.put("marca", null );
            map.put("modelo", null );
            map.put("calibre", null );
        }else{
            if(modelo != null){
                map.put("tipoArma", modelo.getTipoArmaId().getNombre() );
                map.put("marca", modelo.getMarcaId().getNombre() );
                map.put("modelo", modelo.getModelo() );                
                map.put("calibre", obtenerCalibreArma(modelo.getAmaCatalogoList()) );
            }else{            
                map.put("tipoArma", armaDisca.get("DES_ARM") );
                map.put("marca", armaDisca.get("DES_MARCA") );
                map.put("modelo", armaDisca.get("DES_MODELO") );
                map.put("calibre", armaDisca.get("CALIBRE") );
            }
        }

        map.put("peso", arma.getPesoArma() );
        switch(arma.getEstadoVerfica().intValue()){
            case 1:
                    map.put("estado", "VERIFICADO" );
                    break;
            case 2:
                    map.put("estado", "NO ME PERTENECE" );
                    break;
            case 3:
                    map.put("estado", "NUEVO" );
                    break;
            case 4:
                    map.put("estado", "EMITIDAS" );
                    break;
        }
        if(arma.getEstadoArmaId() != null){
            map.put("estadoArma", arma.getEstadoArmaId().getNombre() );
        }else{
            map.put("estadoArma", null );
        }
        if(arma.getSituacionArma() != null){
            map.put("situacion", arma.getSituacionArma().getNombre() );    
        }else{
            map.put("situacion", null );
        }
        map.put("seriePropuesta", arma.getSeriePropuesta() ); 
        return map;
    }    
    
    /**
     * FUNCIÓN PARA ABRIR FORM. DE CREACIÓN DE DECLARACIÓN JURADA
     * @author Richar Fernández
     * @version 1.0
     */
    public void openDlgDeclaracion(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        contEmitidos = ejbAmaRegEmpArmaFacade.contarRegistrosEmitidasRaess(p_user.getNumDoc(), null, null, false);
        progress = 0;
        activeTabDeclaracion = 1;
        
        //// Validacion de direccion ///
        if(!validarCompletarProcesoJuridicas()){
            return;
        }
        ///////////////////////////////
        
        if(ejbUsuarioFacade.buscarUsuarioByLogin(p_user.getLogin()) != null){
            if(validacionNullRegistros(p_user.getNumDoc())){ 
                contNoPertenecen = ejbAmaRegEmpArmaFacade.contarRegistrosNoMePertenecen(p_user.getNumDoc(), null);
                contPorEmitir = ejbAmaRegEmpArmaFacade.contarRegistrosXEmitir(p_user.getNumDoc(), null);
                contParrafo1 = ejbAmaRegEmpArmaFacade.contarArmasPosesion(p_user.getNumDoc(), null) + contEmitidos;
                contParrafo2 = ejbAmaRegEmpArmaFacade.contarArmasMenosLasVendidas(p_user.getNumDoc(), null);
                contParrafo3 = contNoPertenecen;

                if(contParrafo1 > 0 || contParrafo2 > 0 || contParrafo3 > 0 ){
                    setAcepto(false);
                    setRenderProgressBar(false);
                    RequestContext.getCurrentInstance().execute("PF('wvDialogDeclaracionJurada').show()");
                    RequestContext.getCurrentInstance().update("frmDeclaracionJurada");
                }else{
                    JsfUtil.mensajeError("Debe registrar al menos una nueva arma para realizar el proceso");
                }
            }else{
                JsfUtil.mensajeError("No pueden existir armas en estado VERIFICADO que no cuenten con licencia.");
            }
        }else{
            JsfUtil.mensajeError("No se encontró al usuario: "+p_user.getLogin());
        }
    }
    
    public boolean validacionNullRegistros(String ruc){
        boolean validacion = true;
        int cont = ejbAmaRegEmpArmaFacade.contarLicenciaNullRegistros(ruc);
       
        if(cont > 0){
           validacion = false;
        }        
        
        return validacion;
    }
    
    /**
     * FUNCIÓN PARA MOSTRAR FORMULARIO DE VERIFICACIÓN DE ARMA
     * @author Richar Fernández
     * @version 1.0
     * @param reg Map con datos de arma
     */
    public void mostrarCrearVerificacion(Map reg){
        reiniciarValores();
        
        if(reg.get("NRO_LIC") != null){
            estado = EstadoCrud.CREARVERIF;
            registro = new AmaRegEmpArma();
            registro.setActivo(JsfUtil.TRUE);
            registro.setLicDiscaId(Long.parseLong(""+reg.get("NRO_LIC")));
            registro.setSerie(""+reg.get("NRO_SERIE"));
            registroSelected = reg;
            
            cargarActaInternamiento(registro.getLicDiscaId(), registro.getSerie(), registro.getModeloId());                
            if(datosActaInt != null){                    
                opcionPanel1 = "1";
                cambiaPanel1();
            }
        }else{
            JsfUtil.mensajeAdvertencia("No puede verificar un arma que no cuenta con licencia DISCA.");
        }
    }
    
    /**
     * FUNCIÓN PARA MOSTRAR FORMULARIO DE CREACIÓN DE NUEVA ARMA
     * @author Richar Fernández
     * @version 1.0
     */
    public void mostrarRegistroNuevaArma(){
        reiniciarValores();
        estado = EstadoCrud.CREARARMA;
        registro = new AmaRegEmpArma();
        registro.setActivo(JsfUtil.TRUE);
        registroSelected = null;
    }
    
    /**
     * FUNCIÓN PARA LIMPIAR VALORES DE FORMULARIO
     * @author Richar Fernández
     * @version 1.0
     */
    public void reiniciarValores(){
        registro = null;
        registroSelected = null;
        selectedArma = null;
        selectedArmaString = null;
        fileAdjunto1 = null;
        opcionPanel1 = null;
        opcionPanel2 = null;
        lstFotos = null;
        contPorEmitir = 0;
        contNoPertenecen = 0;
        contEmitidos = 0;
        archivo = null;
        estadoSituacion = null;
        situacionArma = null;
        fechaIncautacion = null;
        observacion = null;
        armaEmitido = null;
        contParrafo1 = 0;
        contParrafo2 = 0;
        contParrafo3 = 0;
        currentDate = new Date();
        resultadosBandeja = null;
        resultadosBandejaSeleccionados = null;
        tipoRegistroObservacion = null;
        regionInpe = null;
        penalInpe = null;
        datosActaInt = null;
        activeTabDeclaracion = 0;
        regionInpeNoPropiedad = null;
        penalInpeNoPropiedad = null;
        tieneInternamiento = false;
        numDocPosiblePropietario = "";
        tipoDocPosiblePropietario = "DNI";
        maxLengthPosiblePropietario = 8;
        datosPosiblePropietario = "";
        regionInpeSelected = null;
        setProcesaNuevoExpediente(false);
        setDisabledBtnFoto(false);
        setDisabledPanel1(true);
        setDisabledPanel2(true);
        setRenderEstadoArma(true);
        setDisabledEdicionObservacion(false);
        setBlnSubsanar(false);
        setRenderProgressBar(false);
        setDisabledBtnGuardar(false);
        setSerieDifiere(false);
    }
    
    /**
     * FUNCIÓN PARA CAMBIAR SITUACION DE ARMAS VERIFICADAS
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiaSituacion(){
        setRenderEstadoArma(true);
        numDocPosiblePropietario = "";        
        if(registro.getSituacionArma() != null){
            if(esPerfilInpe()){
                if(!registro.getSituacionArma().getCodProg().equals("TP_SITU_INT")){
                    registro.setEstadoArmaId(null);
                    setRenderEstadoArma(false);
                }
            }else{
                if(registro.getSituacionArma().getCodProg().equals("TP_EST_EMP_NOP") || registro.getSituacionArma().getCodProg().equals("TP_SITU_VEN")){
                    registro.setEstadoArmaId(null);
                    setRenderEstadoArma(false);
                }
            }
        }
    }
    
    public void cambiaSituacionNuevaArma(){
        setRenderEstadoArma(true);
        numDocPosiblePropietario = "";        
        if(registro.getSituacionArma() != null){
            if(esPerfilInpe()){
                if(!registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") && !registro.getSituacionArma().getCodProg().equals("TP_SITU_POS")){
                    registro.setEstadoArmaId(null);
                    setRenderEstadoArma(false);
                }
            }
        }
    }
    
    /**
     * FUNCIÓN PARA CARGAR ARCHIVO ADJUNTO
     * @author Richar Fernández
     * @version 1.0
     * @param event Evento al cargo archivo
     */
    public void handleFileUploadAdjunto1(FileUploadEvent event){
        try{            
            if(event != null){
                fileAdjunto1 = event.getFile();
                archivo = fileAdjunto1.getFileName();
                fileByte = IOUtils.toByteArray(fileAdjunto1.getInputstream());
                
                if(!(JsfUtil.verificarJPG(fileAdjunto1) || JsfUtil.verificarPDF(fileAdjunto1))) {
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo PDF ni JPG");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * FUNCIÓN PARA INICIALIZAR DATOS DE PANEL INFERIOR SEGÚN TIPO DE REGULARIZACIÓN DE ARMA
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiaPanel1(){
        opcionPanel1 = "1";
        opcionPanel2 = null;
        setDisabledPanel1(false);
        setDisabledPanel2(true);
        selectedArma = null;
        selectedArmaString = null;
        estadoSituacion = null;
        situacionArma = null;
        observacion = null;
        registro.setPesoArma(null);
        lstFotos = new ArrayList();
        regionInpe = null;
        penalInpe = null;
        setSerieDifiere(false);
        registro.setSeriePropuesta(null);
        setDisabledBtnGuardar(false);
    }
    
    /**
     * FUNCIÓN PARA INICIALIZAR DATOS DE PANEL SUPERIOR SEGÚN TIPO DE REGULARIZACIÓN DE ARMA
     * @author Richar Fernández
     * @version 1.0
     * @param verifica
     */
    public void cambiaPanel2(boolean verifica){
        
        if(verifica & registro.getLicDiscaId() != null){
            int cont = ejbArmaFacadeFacade.contarArmasConLicenciaDuplicadas(registro.getLicDiscaId());
            if(cont > 0){
                setDisabledBtnGuardar(true);
            }
        }
        
        opcionPanel1 = null;
        opcionPanel2 = "2";
        setDisabledPanel1(true);
        setDisabledPanel2(false);
        registro.setSituacionArma(null);
        registro.setDescMotivo(null);
        fileAdjunto1 = null;
        fileByte = null;
        archivo = null;
        setDisabledBtnFoto(false);
        setSerieDifiere(false);
        lstFotos = new ArrayList();
        regionInpeNoPropiedad = null;
        penalInpeNoPropiedad = null;
    }
    
    /**
     * FUNCIÓN DE VALIDACIÓN DE REGULARIZACIÓN DE ARMA
     * @author Richar Fernández
     * @version 1.0     
     * @return Flag de proceso de validación (TRUE=Correcto, FALSE=Incorrecto)
     */
    public boolean validacionCreate(){
        boolean validar = true;
        
        if(opcionPanel1 == null && opcionPanel2 == null){
            JsfUtil.mensajeError("Por favor seleccione una opción de verificación. ");
            validar = false;
        }
        
        if(opcionPanel1 != null){            
            if(esPerfilInpe()){
                if(registro.getSituacionArma() == null){
                    JsfUtil.mensajeError("Por favor seleccione una situación del arma ");
                    validar = false;
                }else{                    
                    if(registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") && registro.getEstadoArmaId() == null){
                        JsfUtil.mensajeError("Por favor seleccione el estado funcional.");
                        validar = false;
                    }
                    if(registro.getSituacionArma().getCodProg().equals("TP_SITU_VEN") ){
                        if(numDocPosiblePropietario == null || numDocPosiblePropietario.isEmpty()){
                            JsfUtil.mensajeError("Por favor, ingrese el posible propietario del arma de fuego");
                            validar = false;
                        }else{
                            if((tipoDocPosiblePropietario.equals("DNI") || tipoDocPosiblePropietario.equals("RUC")) && (datosPosiblePropietario == null || datosPosiblePropietario.isEmpty() ) ){
                                JsfUtil.mensajeError("Por favor validar los datos del propietario con el botón de RENIEC o SUNAT");
                                validar = false;
                            }
                        }
                    }
                    if(!registro.getSituacionArma().getCodProg().equals("TP_SITU_INT")){
                        registro.setEstadoArmaId(null);
                    }
                }
                if(regionInpeNoPropiedad == null || penalInpeNoPropiedad == null){
                    JsfUtil.mensajeError("Por favor seleccione un penal del INPE");
                    validar = false;  
                }
            }else{
                if(registro.getSituacionArma() == null){
                    JsfUtil.mensajeError("Por favor seleccione una situación del arma ");
                    validar = false;
                }else{
                    if(!registro.getSituacionArma().getCodProg().equals("TP_SITU_VEN") && !registro.getSituacionArma().getCodProg().equals("TP_EST_EMP_NOP")){
                        if(registro.getEstadoArmaId() == null){
                            JsfUtil.mensajeError("Por favor seleccione un estado del arma. ");
                            validar = false;
                        }
                    }
                    if(registro.getSituacionArma().getCodProg().equals("TP_SITU_INC") && registro.getFechaIncauta() == null){
                        JsfUtil.mensajeError("Por favor ingrese la fecha de incautación.");
                        validar = false;
                    }
                }
                if(registro.getSituacionArma() != null && registro.getSituacionArma().getCodProg().equals("TP_SITU_TIT")){
                    if(numDocPosiblePropietario == null || numDocPosiblePropietario.isEmpty()){
                        JsfUtil.mensajeError("Por favor ingresar el número de documento del posible propietario");
                        validar = false;
                    }else{
                        if((tipoDocPosiblePropietario.equals("DNI") || tipoDocPosiblePropietario.equals("RUC")) && (datosPosiblePropietario == null || datosPosiblePropietario.isEmpty() ) ){
                            JsfUtil.mensajeError("Por favor validar los datos del propietario con el botón de RENIEC o SUNAT");
                            validar = false;
                        }
                    }
                }
            }
            if(registro.getDescMotivo() == null){
                JsfUtil.mensajeError("Por favor describa el motivo. ");
                validar = false;
            }else{
                if(registro.getDescMotivo().trim().isEmpty()){
                    JsfUtil.mensajeError("Por favor describa el motivo. ");
                    validar = false;
                }else{
                    registro.setDescMotivo(registro.getDescMotivo().trim());
                }
            }
            if(registro.getSituacionArma() != null && !registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") && archivo == null){
                JsfUtil.mensajeError("Por favor adjunte el documento que acredite la propiedad, pérdida o robo del arma de fuego");
                validar = false;                    
            }
            if(registro.getSituacionArma() != null && registro.getSituacionArma().getCodProg().equals("TP_SITU_INT")){
                archivo = null;
                fileAdjunto1 = null;
                fileByte = null;
            }
        }
        if(opcionPanel2 != null && esPerfilInpe()){
            if(serieDifiere){
                if(registro.getSeriePropuesta() == null || (registro.getSeriePropuesta() != null && registro.getSeriePropuesta().isEmpty())){
                    JsfUtil.mensajeError("Por favor ingrese la serie propuesta");
                    validar = false;
                }
            }else{
                registro.setSeriePropuesta(null);
            }
            if(selectedArma == null){
                JsfUtil.mensajeError("Por favor ingrese el arma");
                validar = false;
            }
            if(registro.getPesoArma() != null){                
                if(registro.getPesoArma() <= 0){    
                    JsfUtil.mensajeError("Por favor ingrese correctamente el peso");
                    validar = false;
                }
            }
            if(registro.getSerie() == null || (registro.getSerie() != null && registro.getSerie().isEmpty())){
                JsfUtil.mensajeError("Por favor ingrese la serie");
                validar = false;
            }
            if(estadoSituacion == null){
                JsfUtil.mensajeError("Por favor seleccione el estado del arma");
                validar = false;
            }
            if(situacionArma == null){
                JsfUtil.mensajeError("Por favor seleccione la situación del arma");
                validar = false;    
            }else{
                if(situacionArma.getCodProg().equals("TP_SITU_POS") && estadoSituacion != null && estadoSituacion.getCodProg().equals("TP_ESTA_INO") && 
                   (observacion == null || observacion.isEmpty()) ){
                    JsfUtil.mensajeError("Por favor ingrese la observación cuando el estado del arma es INOPERATIVO.");
                    validar = false;    
                }
            }
            if(lstFotos == null || lstFotos.size() < 2){
                JsfUtil.mensajeError("Por favor ingresar las dos fotos del arma");
                validar = false;
            }
            
            if(esPerfilInpe()){
                if(regionInpe == null){
                    JsfUtil.mensajeError("Por favor seleccione una región del INPE");
                    validar = false;  
                }            
                if(penalInpe == null){
                    JsfUtil.mensajeError("Por favor seleccione un penal del INPE");
                    validar = false;  
                }
            }
            
        }
        if(esPerfilInpe()){
            if(registro.getSeriePropuesta() != null && registro.getLicDiscaId() != null){
                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                int cont =  ejbAmaRegEmpArmaFacade.contArmasBySerieByLicencia(p_user.getNumDoc(),registro.getSeriePropuesta(), registro.getLicDiscaId(), registro.getId(), esPerfilInpe());
                if(cont > 0){
                    JsfUtil.mensajeError("Ya existe registrado un arma con el mismo número de serie propuesta y licencia.");
                    validar = false;     
                }                
            }
            if(registro.getSeriePropuesta() != null && selectedArma != null){
                int cont = ejbAmaTarjetaPropiedadFacade.contarTarjetaPropiedadEmitidasBySerie(registro.getSeriePropuesta(), selectedArma.getTipoArmaId().getId());
                if(cont > 0){
                    JsfUtil.mensajeError("Existe un arma registrada con el mismo número de serie propuesta y tipo de arma en una tarjeta de propiedad emitida");
                    validar = false;
                }else{
                    AmaArma arma = ejbAmaArmaFacade.buscarArmaBySerieyTipoArma(registro.getSeriePropuesta(), selectedArma.getTipoArmaId().getCodProg());
                    if(arma != null){
                        JsfUtil.mensajeError("Ya existe arma nueva con el mismo número de serie propuesta y tipo de arma.");
                        validar = false;
                    }    
                }
                String rucInpe = "'"+JsfUtil.getLoggedUser().getNumDoc()+"'";// <-- agregar parametro RUC INPE adicionales
                int tipoArmaDisca = convertirTipoArmaIntegradoADisca(selectedArma.getTipoArmaId().getCodProg()); 
                List<Map> listadoLicenciasDiscas = ejbAmaArmaFacade.buscarLicenciasDiscaXNroSerieXTipoArma(rucInpe, registro.getSeriePropuesta(), tipoArmaDisca);
                if(listadoLicenciasDiscas != null && !listadoLicenciasDiscas.isEmpty()){
                    JsfUtil.mensajeError("El arma con la serie ingresada cuenta licencia para regularizar, por favor verifique en la Bandeja.");
                    return false;
                }else{
                    Long nroLic = ejbAmaArmaFacade.obtenerLicenciaDiscaBySerieByTipoArma(registro.getSeriePropuesta(),tipoArmaDisca);
                    if(nroLic != null){
                        //registro.setLicDiscaId(nroLic);//<-- no debería segun actual certificacion de cambio
                        JsfUtil.mensajeError("El arma ingresada cuenta licencia a favor de otra persona, por favor verifique.");
                        return false;
                    }    
                }
            }
        }
        if(registro.getSerie() != null && registro.getLicDiscaId() != null){
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            int cont =  ejbAmaRegEmpArmaFacade.contArmasBySerieByLicencia(p_user.getNumDoc(),registro.getSerie(), registro.getLicDiscaId(), registro.getId(), esPerfilInpe());
            if(cont > 0){
                JsfUtil.mensajeError("Ya existe registrado un arma con el mismo número de serie y licencia.");
                validar = false;
            }
        }
        return validar;
    }
    
    /**
     * FUNCIÓN DE VALIDACIÓN DE ARMAS EMITIDAS
     * @author Richar Fernández
     * @version 1.0     
     * @return Flag de proceso de validación (TRUE=Correcto, FALSE=Incorrecto)
     */
    public boolean validacionCreateEmitidos(){
        boolean validar = true;
        if(lstFotos == null || lstFotos.size() < 2){
            JsfUtil.mensajeError("Por favor ingresar las dos fotos del arma");
            validar = false;
        }
        
        return validar;
    }
    
    /**
     * FUNCIÓN DE VALIDACIÓN PARA CREAR NUEVA ARMA
     * @author Richar Fernández
     * @version 1.0     
     * @return Flag de proceso de validación (TRUE=Correcto, FALSE=Incorrecto)
     */
    public boolean validacionCreateNuevaArma(){
        boolean validar = true;
        
        if(registro.getSituacionArma() == null){
            JsfUtil.mensajeError("Por favor seleccionar la situación del arma");
            validar = false;
        }else{            
            if(registro.getSituacionArma().getCodProg().equals("TP_SITU_INC")){
                if(registro.getFechaIncauta() == null){
                    JsfUtil.mensajeError("Por favor ingresar la fecha de incautación");
                    validar = false;
                }
            }
        }
        
        if(esPerfilInpe()){
            if(registro.getRegionId() == null){
                JsfUtil.mensajeError("Por favor seleccione una región del INPE");
                validar = false;
            }
            if(registro.getPenalId() == null){
                JsfUtil.mensajeError("Por favor seleccione un penal del INPE");
                validar = false;
            }
            if(registro.getSituacionArma() != null && 
               (registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") || registro.getSituacionArma().getCodProg().equals("TP_SITU_POS") ) && 
               registro.getEstadoArmaId() == null){
                JsfUtil.mensajeError("Por favor seleccione el estado funcional.");
                validar = false;
            }
            if(registro.getSituacionArma().getCodProg().equals("TP_SITU_VEN") ){
                if(numDocPosiblePropietario == null || numDocPosiblePropietario.isEmpty()){
                    JsfUtil.mensajeError("Por favor, ingrese el posible propietario del arma de fuego");
                    validar = false;
                }else{
                    if((tipoDocPosiblePropietario.equals("DNI") || tipoDocPosiblePropietario.equals("RUC")) && (datosPosiblePropietario == null || datosPosiblePropietario.isEmpty() ) ){
                        JsfUtil.mensajeError("Por favor validar los datos del propietario con el botón de RENIEC o SUNAT");
                        validar = false;
                    }
                }
            }
            datosActaInt = null;
            cargarActaInternamiento(registro.getLicDiscaId(), registro.getSerie(), ((selectedArma != null)?selectedArma.getId():null));
            if(tieneInternamiento){
                if(!registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") ){
                    JsfUtil.mensajeError("El arma de fuego con datos ingresados está internada.");
                    validar = false;
                }else{
                    registro.setEstadoArmaId(ejbTipoGamacFacade.buscarTipoGamacXCodProg(datosActaInt.get("ESTADOFUNC_CODPROG").toString()));
                }
            }else{
                if(registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") ){
                    JsfUtil.mensajeError("No se encontró el acta de internamiento para el arma ingresada");
                    validar = false;
                }    
            } 
            if(registro.getSituacionArma() != null && !registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") && !registro.getSituacionArma().getCodProg().equals("TP_SITU_POS")){
                registro.setEstadoArmaId(null);
            }
            if(registro.getEstadoArmaId() != null && registro.getEstadoArmaId().getCodProg().equals("TP_ESTA_OPE")){
                if(registro.getPesoArma() != null){
                    if(registro.getPesoArma() <= 0){
                        JsfUtil.mensajeError("Por favor ingrese correctamente el peso ");
                        validar = false;    
                    }
                }
                if(lstFotos == null || lstFotos.size() < 2){
                    JsfUtil.mensajeError("Por favor ingresar las dos fotos del arma");
                    validar = false;
                }
            }
        }else{
            if(registro.getEstadoArmaId() == null){
                JsfUtil.mensajeError("Por favor seleccionar el estado del arma");
                validar = false;
            }else{
                if(registro.getEstadoArmaId().getCodProg().equals("TP_ESTA_OPE")){
                    if(registro.getPesoArma() != null){
                        if(registro.getPesoArma() <= 0){
                            JsfUtil.mensajeError("Por favor ingrese correctamente el peso ");
                            validar = false;    
                        }
                    }
                    if(lstFotos == null || lstFotos.size() < 2){
                        JsfUtil.mensajeError("Por favor ingresar las dos fotos del arma");
                        validar = false;
                    }
                }
            }
        }
        
        if(registro.getSerie() == null){
            JsfUtil.mensajeError("Por favor ingrese la serie del arma");
            validar = false;
        }else{
            if(registro.getSerie().trim().isEmpty()){
                JsfUtil.mensajeError("Por favor ingrese la serie del arma");
                validar = false;
            }else{
                registro.setSerie(registro.getSerie().trim());
            }
        }
        if(selectedArma == null){
            JsfUtil.mensajeError("Por favor ingrese el arma");
            validar = false;
        }        
                
        if(registro.getDescMotivo() == null ){
            JsfUtil.mensajeError("Por favor describa el motivo. ");
            validar = false;
        }else{
            if(registro.getDescMotivo().trim().isEmpty()){
                JsfUtil.mensajeError("Por favor describa el motivo. ");
                validar = false;
            }else{
                registro.setDescMotivo(registro.getDescMotivo().trim());
            }
        }
        if(archivo == null){
            JsfUtil.mensajeError("Por favor adjunte el documento que acredite la propiedad del arma de fuego");
            validar = false;
        }
        if(registro.getSerie() != null && selectedArma != null){
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            int cont =  ejbAmaRegEmpArmaFacade.contArmasBySerie(p_user.getNumDoc(),registro.getSerie(), registro.getId(), esPerfilInpe());
            if(cont > 0){
                JsfUtil.mensajeError("Ya existe registrado un arma con el mismo número de serie, tipo de arma, marca, modelo y calibre.");
                validar = false;
            }
        }
        
        if(esPerfilInpe()){
            if(registro.getSerie() != null && selectedArma != null){
                int cont = ejbAmaTarjetaPropiedadFacade.contarTarjetaPropiedadEmitidasBySerie(registro.getSerie(), selectedArma.getTipoArmaId().getId());
                if(cont > 0){
                    JsfUtil.mensajeError("Existe un arma con el mismo número de serie en una tarjeta de propiedad emitida");
                    return false;
                }
                
                AmaArma arma = ejbAmaArmaFacade.buscarArmaBySerieyTipoArma(registro.getSerie(), selectedArma.getTipoArmaId().getCodProg());
                if(arma != null){
                    JsfUtil.mensajeError("El arma ya se encuentra registrada como arma nueva");
                    return false;
                }
                            
                String rucInpe = "'"+JsfUtil.getLoggedUser().getNumDoc()+"'";// <-- agregar parametro RUC INPE adicionales
                int tipoArmaDisca = convertirTipoArmaIntegradoADisca(selectedArma.getTipoArmaId().getCodProg()); 
                List<Map> listadoLicenciasDiscas = ejbAmaArmaFacade.buscarLicenciasDiscaXNroSerieXTipoArma(rucInpe, registro.getSerie(), tipoArmaDisca);
                if(listadoLicenciasDiscas != null && !listadoLicenciasDiscas.isEmpty()){
                    if(registro.getTipoEstadoId()!=null && registro.getTipoEstadoId().getCodProg().equals("TP_ERA_OBS")){
                        if(listadoLicenciasDiscas.size() == 1){
                            registro.setLicDiscaId(Long.parseLong(listadoLicenciasDiscas.get(0).get("NRO_LIC").toString()));                     
                        }else{
                            JsfUtil.mensajeError("El arma con la serie ingresada cuenta más de una licencia para regularizar, por favor verifique.");
                            return false;
                        }
                    }else{
                        JsfUtil.mensajeError("El arma con la serie ingresada cuenta licencia para regularizar, por favor verifique en la Bandeja.");
                        return false;    
                    }   
                }else{
                    Long nroLic = ejbAmaArmaFacade.obtenerLicenciaDiscaBySerieByTipoArma(registro.getSerie(),tipoArmaDisca);
                    if(nroLic != null){
                        //registro.setLicDiscaId(nroLic);//<-- no debería segun actual certificacion de cambio
                        JsfUtil.mensajeError("El arma ingresada cuenta licencia a favor de otra persona, por favor verifique.");
                        return false;
                    }    
                }
            }
        }
        
        return validar;
    }
    
    /**
     * FUNCIÓN PARA OBTENER LISTADO DE ARMAS PARA CAMPO AUTOCOMPLETABLE
     * @author Richar Fernández
     * @version 1.0
     * @param query Campo a buscar
     * @return Listado de modelo de armas
     */
    public List<String> completeArma(String query) {       
        List<String> filteredArma = new ArrayList<>();
        String[] lstVariosBusqueda = null;
        List<Map> lstArma;
        int cont = 0;
        query = query.toUpperCase();
        
         if(query.contains("S/M")){
            query = query.replaceAll("S/M", "");            
            lstVariosBusqueda = query.split("/");
            if(lstVariosBusqueda.length == 1 && lstVariosBusqueda[0].isEmpty()){
                lstVariosBusqueda[0] = "S/M";
            }else{
                lstVariosBusqueda = append(lstVariosBusqueda,"S/M");
            }
        }else{
            lstVariosBusqueda = query.split("/");
        }
        
        lstVariosBusqueda = limpiarArrayArma(lstVariosBusqueda);
        lstArma = ejbAmaModelosFacade.obtenerListadoArmasMap( null );
//        if(lstVariosBusqueda.length > 1 || query.contains("/") || query.contains("S/M") ){
//            lstArma = ejbAmaModelosFacade.obtenerListadoArmasMap( null );
//        }else{
//            lstArma = ejbAmaModelosFacade.obtenerListadoArmasMap( query );
//        }

        for (Map p : lstArma) {
            cont = 0;
            for(String cadena : lstVariosBusqueda){
                if(!cadena.isEmpty()){
                    if (p.get("TIPO_ARMA").toString().contains(cadena.toUpperCase().trim()) ||
                        p.get("MARCA").toString().contains(cadena.toUpperCase().trim()) ||
                        p.get("MODELO").toString().contains(cadena.toUpperCase().trim()) ||
                        p.get("CALIBRE").toString().contains(cadena.toUpperCase().trim()) ) {

                        cont++;
                    }
                }
            }
            
            if(cont == lstVariosBusqueda.length){
                filteredArma.add(p.get("ID")+ "@/@" + 
                                 p.get("MODELO")+ "@/@" + 
                                 p.get("MARCA_ID")+ "@/@" + 
                                 p.get("TIPO_ARMA_ID")+ "@/@" + 
                                 p.get("CALIBRE")+ "@/@" + 
                                 p.get("TIPO_ARMA")+ "@/@" + 
                                 p.get("MARCA")
                );
            }
        }
        return filteredArma;
    }
    
    /**
     * QUITAR FILA VACIAS DE ARRAY
     * @author Richar Fernández
     * @version 1.0
     * @param array de cadena
     * @return Array de cadena
     */
    public String[] limpiarArrayArma(String[] array){
        String[] nuevoArray = new String[0];
        for(String cadena : array){
            if(!cadena.isEmpty()){
                nuevoArray = append(nuevoArray,cadena);
            }
        }
        
        return nuevoArray;
    }
    
    /**
     * ADICIONAR NUEVA FILA A ARRAY
     * @author Richar Fernández
     * @version 1.0
     */
    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }
    
    /**
     * FUNCIÓN AL SELECCIONAR ARMA DE AUTOCOMPLETABLE DE BÚSQUEDA DE ARMA
     * @author Richar Fernández
     * @version 1.0
     */
    public void seleccionaArma(){
        if(selectedArmaString != null){
            if(!selectedArmaString.isEmpty()){
                String[] lstVariosBusqueda = selectedArmaString.toUpperCase().split("@/@");
                selectedArma = ejbAmaModelosFacade.obtenerModelo(Long.parseLong(""+lstVariosBusqueda[0]));
            }
        }else{
            selectedArma = null;
        }
    }
    
    /**
     * FUNCIÓN PARA OBTENER DESCRIPCIÓN DE ARMA PARA CAMPO AUTOCOMPLETABLE
     * @author Richar Fernández
     * @version 1.0
     * @param mod Modelo de arma
     * @return Descripción de arma (Nombre de Arma / Marca / Modelo / Calibre(s))
     */
    public String obtenerDescripArmaListadoString(String mod){
        String cadena = "";        
        if(mod != null && !mod.isEmpty()){            
            String[] lstVariosBusqueda = mod.toUpperCase().split("@/@");
            
            cadena = lstVariosBusqueda[5] + " / " + lstVariosBusqueda[6] + " / " + lstVariosBusqueda[1] + " / " + lstVariosBusqueda[4];
        }
        return cadena;
    }
    
    /**
     * FUNCIÓN PARA OBTENER DESCRIPCIÓN DE ARMA PARA CAMPO AUTOCOMPLETABLE
     * @author Richar Fernández
     * @version 1.0
     * @param mod Modelo de arma
     * @return Descripción de arma (Nombre de Arma / Marca / Modelo / Calibre(s))
     */
    public String obtenerDescripArmaListado(AmaModelos mod){
        String cadena = "";
        
        if(mod != null){
            cadena = mod.getTipoArmaId().getNombre() + "/" + mod.getMarcaId().getNombre() + "/" + mod.getModelo()+ "/" + obtenerCalibreArma(mod.getAmaCatalogoList());        
        }
        return cadena;
    }
    
    /**
     * FUNCIÓN PARA OBTENER DESCRIPCIÓN DE ARMA AL EDITAR O VER ARMA
     * @author Richar Fernández
     * @version 1.0
     * @param modelo Modelo de arma
     * @return Descripción de arma (Id / Modelo / Marca Id / Tipo de Arma Id / Modelo / Calibre(s)) / Tipo de arma / Marca
     */
    public String obtenerArmaEditarVer(AmaModelos modelo){
        String cadena = modelo.getId() + "@/@" +
                        modelo.getModelo() + "@/@" +
                        ((modelo.getMarcaId() != null)?modelo.getMarcaId().getId():"") + "@/@" +
                        ((modelo.getTipoArmaId() != null)?modelo.getTipoArmaId().getId():"")+ "@/@" +
                        obtenerCalibreArma(modelo.getAmaCatalogoList()) + "@/@" +
                        ((modelo.getTipoArmaId() != null)?modelo.getTipoArmaId().getNombre():"") + "@/@" +
                        ((modelo.getMarcaId() != null)?modelo.getMarcaId().getNombre():"");
        return cadena;
    }
    
    /**
     * FUNCIÓN PARA OBTENER DESCRIPCIÓN DE ARMA PARA FORM. DE VISUALIZACIÓN DE REGISTRO
     * @author Richar Fernández
     * @version 1.0
     * @return Descripción de arma (Nombre de Arma / Marca / Modelo / Calibre(s))
     */
    public String obtenerDescripArmaListadoVer(){
        String cadena = "";
        
        if(selectedArma != null){
            cadena = selectedArma.getTipoArmaId().getNombre() + " / " + selectedArma.getMarcaId().getNombre() + " / " + selectedArma.getModelo()+ " / " + obtenerCalibreArma(selectedArma.getAmaCatalogoList());
        }
        return cadena;
    }
    
    /**
     * FUNCIÓN PARA OBTENER CALIBRE DE ARMA SELECCIONADA
     * @author Richar Fernández
     * @version 1.0
     * @param lstCatalogo Listado de calibres de arma
     * @return Cadena de calibres de un arma
     */
    public String obtenerCalibreArma(List<AmaCatalogo> lstCatalogo){
        String cadena = "";
        
        if(lstCatalogo != null){
            if(lstCatalogo.size() == 1){             
                cadena = lstCatalogo.get(0).getNombre();
            }else{
                for(AmaCatalogo cat : lstCatalogo){
                    if(cat.getActivo() == 1){
                        if(cadena.isEmpty()){
                            cadena = cat.getNombre();
                        }else{
                            cadena = cadena + "/" + cat.getNombre();
                        }
                    }   
                }
            }
        }   
        
        return cadena;
    }
    
    /**
     * FUNCION PARA CARGAR FOTO SELECCIONADA
     * @author Richar Fernández
     * @version 1.0
     * @param event Evento para cargar imagen
     */
    public void handleFileUploadFoto(FileUploadEvent event) {
        try {
            if (event != null) {
                file = event.getFile();
                if (JsfUtil.verificarJPG(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    fotoByte = JsfUtil.escalarRotarImagen(fotoByte, 1024, 768, 0);
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                    
                    if(lstFotos == null){
                        lstFotos = new ArrayList();
                    }
                    Map mapa = new HashMap();
                    mapa.put("nro", lstFotos.size()+1);
                    mapa.put("nombre", file.getFileName());
                    mapa.put("byte", fotoByte);
                    lstFotos.add(mapa);
                    
                    if(lstFotos.size() == 2){
                        setDisabledBtnFoto(true);
                    }else{
                        setDisabledBtnFoto(false);
                    }
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original, comuníquese con el administrador del sistema");
                }
            }
        } catch (Exception e) {
        //    e.printStackTrace();
        }
    }
    
     /**
     * FUNCION PARA ELIMINAR FOTO DE ARMA CARGADA
     * @author Richar Fernández
     * @version 1.0
     * @param fotoSelec Map con datos de arma seleccionada
     */
    public void eliminarFotoVerificacion(Map fotoSelec){
        for (Map foto : lstFotos) {
            if( Objects.equals(foto.get("nro"), fotoSelec.get("nro")) ){
                lstFotos.remove(fotoSelec);
                break;
            }
        }
        if(!lstFotos.isEmpty()){
            Map temp = new HashMap();
            temp.put("nro", "1");
            temp.put("nombre", lstFotos.get(0).get("nombre"));
            temp.put("byte", lstFotos.get(0).get("byte"));
            lstFotos = new ArrayList();
            lstFotos.add(temp);
        }
        
        if(lstFotos.size() == 2){
            setDisabledBtnFoto(true);
        }else{
            setDisabledBtnFoto(false);
        }
    }
    
    public boolean validarCompletarProcesoJuridicas(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        
        //// Validacion de direccion ///
        List<SbDireccionGt> lstDireccTemp = ejbSbDireccionFacade.listarDireccionesXPersonaByRuc(p_user.getNumDoc());
        int cont = 0;
        if(lstDireccTemp != null && !lstDireccTemp.isEmpty()){
            for(SbDireccionGt direc : lstDireccTemp){
                if(direc.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && direc.getActivo() == 1){
                    cont++;
                }
            }
        }
        if(cont == 0){
           JsfUtil.mensajeError(JsfUtil.bundleBDIntegrado("amaRegEmpArma_msjeValidacion_direccionFiscal"));
           return false;
        }
        ///////////////////////////////
        return true;
    }
    
    public void completarProceso(Map regImp){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        AmaRegEmpArma reg = null;
        
        try {
            if(!validarCompletarProcesoJuridicas()){
                return;
            }
       
            List<AmaRegEmpArma> lstArmasRegistro = ejbAmaRegEmpArmaFacade.obtenerListadoArmasRegCompletarProceso(regImp.get("EXPEDIENTE").toString(),p_user.getNumDoc(), false);
            List<Map> lstArmasParametroReport = null;
            totalProcesados = lstArmasRegistro.size();
            
            if(!lstArmasRegistro.isEmpty()){
                reg = lstArmasRegistro.get(0);
               
                if(reg != null){
                    lstArmasParametroReport = generarListadoReporte(lstArmasRegistro);
                    contEmitidos = ejbAmaRegEmpArmaFacade.contarRegistrosEmitidasRaess(p_user.getNumDoc(), reg.getExpedienteMaster(), null, false);
                    contNoPertenecen = ejbAmaRegEmpArmaFacade.contarRegistrosNoMePertenecen(p_user.getNumDoc(), reg.getExpedienteMaster());
                    contPorEmitir = ejbAmaRegEmpArmaFacade.contarRegistrosXEmitir(p_user.getNumDoc(), reg.getExpedienteMaster());
                    contParrafo1 = ejbAmaRegEmpArmaFacade.contarArmasPosesion(p_user.getNumDoc(), reg.getExpedienteMaster()) + contEmitidos;
                    contParrafo2 = ejbAmaRegEmpArmaFacade.contarArmasMenosLasVendidas(p_user.getNumDoc(), reg.getExpedienteMaster());
                    contParrafo3 = contNoPertenecen;

                    if(subirPdf(reg,lstArmasParametroReport, reg.getExpedienteMaster())){
                        SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
                        Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
                        asunto = "Por evaluar:"+ contPorEmitir +", Emitidos:"+ contEmitidos +", No me pertenecen:"+contNoPertenecen;                                
                        
                        if(!reg.getAmaRegEmpProcesoList().isEmpty()){
                            procesoHis = reg.getAmaRegEmpProcesoList().get(0);  
                            if(procesoHis.getAmaRegEmpArmaList() == null){
                                procesoHis.setAmaRegEmpArmaList(new ArrayList());
                            }
                        }
                        for(AmaRegEmpArma arma : lstArmasRegistro){
                            if(!procesoHis.getAmaRegEmpArmaList().contains(arma)){
                                procesoHis.getAmaRegEmpArmaList().add(arma);
                            }
                        }
                        crearHistoricoProcesoRegArma((short) 3, per, reg.getExpedienteMaster());
                        
                        if(wsTramDocController.adjuntarDeclaracionJurada(usuaTramDoc,"USRWEB",reg.getExpedienteMaster(),asunto, FileUtils.readFileToByteArray(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificado") + reg.getExpedienteMaster() + ".pdf")) )){
                        }
                        lstExpedientesImpresion = ejbAmaRegEmpArmaFacade.obtenerExpedienteMasterJuridicas(p_user.getNumDoc());
                        JsfUtil.mensaje("Proceso completado correctamente");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * FUNCION PARA GENERAR CONSTANCIA DE REGULARIZACIÓN DE ARMAS
     * @author Richar Fernández
     * @version 1.0
     * @return Reporte pdf
     */
    public StreamedContent imprimir(){
        try {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            List<AmaRegEmpArma> lstArmasRegistro = ejbAmaRegEmpArmaFacade.obtenerListadoArmasRegularizacionFinalizado(p_user.getNumDoc(), esPerfilInpe());
            AmaRegEmpArma reg = null;
            
            if(!lstArmasRegistro.isEmpty()){
                for(AmaRegEmpArma arma: lstArmasRegistro){
                    if(arma.getExpedienteMaster() != null){
                        reg = arma;
                        break;
                    }
                }
                return constancia(reg, null);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Map> generarListadoReporte(List<AmaRegEmpArma> lstArmasRegistro){
        List<Map> lstArmasParametroReport = new ArrayList();
        AmaModelos modelo;
        Map armaDisca = null;
                            
        for(AmaRegEmpArma arma : lstArmasRegistro){
            Map map = new HashMap();
            actualProcesado++;
            armaDisca = null;
            if(arma.getExpedienteMaster() != null){ 
                if(arma.getEstadoVerfica() == 4){//Emitidas
                    if(arma.getLicDiscaId() != null){
                        armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());
                    }
                    map = preparaArmaReporte(arma,null, armaDisca);
                    map.put("expediente", null );
                    lstArmasParametroReport.add(map);
                }else{
                    if(arma.getEstadoVerfica() != 3){ // Verificadas y No me pertenecen
                        if(arma.getLicDiscaId() != null){
                            armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());
                        }
                    }else{
                        //Nuevo
                        armaDisca = null;                                        
                    }
                    if(arma.getModeloId() != null){
                        modelo =  ejbAmaModelosFacade.obtenerModeloById(arma.getModeloId());
                        map = preparaArmaReporte(arma,modelo,null);
                    }else{
                        map = preparaArmaReporte(arma,null, armaDisca);
                    }                                    
                    map.put("expediente", arma.getExpediente() );
                    lstArmasParametroReport.add(map);
                }
                ////////////////////////////
            }
        }
        
        return lstArmasParametroReport;
    }

    
    /**
     * FUNCION PARA OBTENER CONSTANCIA PDF DE REGULARIZACIÓN DE ARMAS
     * @author Richar Fernández
     * @version 1.0
     * @param pReg Registro de Arma
     * @return Reporte pdf
     */
    public StreamedContent constancia(AmaRegEmpArma pReg, Map regAct) {
        try {
            List<AmaRegEmpArma> lp = new ArrayList();
            lp.add(pReg);

            if (JsfUtil.buscarPdfRepositorio(pReg.getExpedienteMaster(), JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificado"))) {
                return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_certificado").getValor(), pReg.getExpedienteMaster() + ".pdf", pReg.getExpedienteMaster() + ".pdf", "application/pdf");
            }else{
                JsfUtil.mensajeError("No se encontró el archivo pdf");
                return null;
            }
        } catch (Exception ex) {
            return JsfUtil.errorDescarga("Error: EppCertificadoController.reporte :", ex);
        }
    }
    
     /**
     * FUNCION PARA SUBIR PDFs A SERVIDOR
     * @author Richar Fernández
     * @version 1.0
     * @param pReg Registro a crear certificado
     * @param nombreJasper Nombre de certificado (.jasper)
     * @return FLAG DE VALIDACION DE PROCESO DE SUBIR PDF
     */
    private boolean subirPdf(AmaRegEmpArma pReg, List<Map> listArmasReporte,String expedienteMaster) {
        boolean valida = true;
        try {
            SbDireccionGt direccionInpe = ejbSbDireccionFacade.mostrarDireccionXRegionInpe("TP_RGN_CENT");  
            if(direccionInpe == null){
                JsfUtil.mensajeError("No se encontró la dirección de la SEDE CENTRAL INPE");
                return false;
            }
            
            List<AmaRegEmpArma> lp = new ArrayList();
            pReg.setExpedienteMaster(expedienteMaster);
            lp.add(pReg);
            
            JsfUtil.subirPdfSel(pReg.getExpedienteMaster(), parametrosPdf(pReg,listArmasReporte, direccionInpe), lp, "/aplicacion/gamac/amaRegEmpArma/reportes/verificacionArma.jasper", JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificado"));
            
        } catch (Exception ex) {
            JsfUtil.mensajeError("No se pudo subir el archivo: " + ex.getMessage());
            valida = false;
        }
        return valida;
    }

    
     /**
     * PARAMETROS PARA GENERAR PDF
     * @author Richar Fernández
     * @version 1.0
     * @param ppdf Registro de regularizacion de arma
     * @return Listado de parámetros para constancia
     */
    private HashMap parametrosPdf(AmaRegEmpArma ppdf, List<Map> lstArmasRegistro, SbDireccionGt direccionInpe) {        
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");

        ph.put("P_FECHASTRING", ReportUtil.mostrarFechaString(new Date()));
        ph.put("P_EMPRESASTRING", p_user.getPersona().getRznSocial() );
        ph.put("P_USUARIO", p_user.getNombres()+ " "+ p_user.getApePat()+ " " + p_user.getApeMat());
        ph.put("p_dir_emp", ReportUtil.mostrarDireccionEspecifica(direccionInpe) );
        ph.put("P_TIPONRODOC", ReportUtil.mostrarTipoYNroDocumemtoString_arma(p_user.getPersona()) );
        ph.put("P_USUARIO_TIPODOCYNRO", p_user.getLogin());
        ph.put("P_logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        ph.put("P_NROEXPEDIENTE", ppdf.getExpedienteMaster() );
        ph.put("P_CANTIDADREG", lstArmasRegistro.size() );        
        ph.put("P_TABLA_ARMAS", lstArmasRegistro);
        ph.put("P_CONTPARRAFO1", contParrafo1 );
        ph.put("P_CONTPARRAFO2", contParrafo2 );
        ph.put("P_CONTPARRAFO3", contParrafo3 );
        if(esPerfilInpe()){
            ph.put("P_TITULO", "REGISTRO DE VERIFICACIÓN DE ARMAS");
        }else{
            ph.put("P_TITULO", "REGISTRO DE REGULARIZACIÓN DE ARMAS DE EMPRESAS DE SEGURIDAD Y OTRAS PERSONAS JURIDICAS" );
        }
        ph.put("P_TEXTO2", JsfUtil.bundleBDIntegrado("amaArma_texto2DeclaracionJurada") );
  
        return ph;
    }
    
    /**
     * OBTENER LISTADO DE ARMAS REGULARIZADAS
     * @param lstArmasRegistro LISTADO DE ARMAS REGULARIZADAS
     * @author Richar Fernández
     * @version 1.0
     * @return LISTADO DE MAP CON DATOS DE ARMAS.
     */
    public List<Map> obtenerListadoArmas(List<AmaRegEmpArma> lstArmasRegistro){
        List<Map> listado = new ArrayList();
        
        for(AmaRegEmpArma arma : lstArmasRegistro){
            Map map = new HashMap();
            map.put("serie", arma.getSerie());
            
            if(arma.getModeloId() != null){
                AmaModelos  modelo =  ejbAmaModelosFacade.obtenerModelo(arma.getModeloId());
                if(modelo != null){
                    map.put("tipoArma", modelo.getTipoArmaId().getNombre() );
                    map.put("marca", modelo.getMarcaId().getNombre() );
                    map.put("modelo", modelo.getModelo() );                
                    map.put("calibre", obtenerCalibreArma(modelo.getAmaCatalogoList()) );
                }else{
                    map.put("tipoArma", null);
                    map.put("marca", null );
                    map.put("modelo", null );
                    map.put("calibre", null );
                }
            }else{
                map.put("tipoArma", null);
                map.put("marca", null );
                map.put("modelo", null );
                map.put("calibre", null );
            }
            
            map.put("peso", arma.getPesoArma() );
            switch(arma.getEstadoVerfica().intValue()){
                case 1:
                        map.put("situacion", "VERIFICADO" );
                        break;
                case 2:
                        map.put("situacion", "NO ME PERTENECE" );
                        break;
                case 3:
                        map.put("situacion", "NUEVO" );
                        break;
                case 4:
                        map.put("situacion", "EMITIDAS" );
                        break;
            }
            map.put("expediente", ((arma.getExpediente() != null)?arma.getExpediente():"-") );

            listado.add(map);
        }
        
        return listado;
    }
    
    /**
     * OBTENER LISTADO DE ESTADOS DEL ARMA
     * @author Richar Fernández
     * @version 1.0
     * @return Listado de estados del arma
     */
    public List<TipoGamac> obtenerListadoEstados(){
        if(esPerfilInpe()){
            if(registro.getSituacionArma() != null && registro.getSituacionArma().getCodProg().equals("TP_SITU_INT") && datosActaInt != null){
                return ejbTipoGamacFacade.listarTipoGamacByManyCodProgs("'"+datosActaInt.get("ESTADOFUNC_CODPROG").toString()+"'");
            }
        }
        return ejbTipoGamacFacade.obtenerEstadosArmas();
    }
    
    /**
     * OBTENER LISTADO DE ESTADOS OPERATIVO E INOPERATIVO
     * @author Richar Fernández
     * @version 1.0
     * @return Listado de estados del arma (Operativo e Inoperativo)
     */
    public List<TipoGamac> obtenerEstadoOperativo(){
        return ejbTipoGamacFacade.obtenerEstadosOperativoInoperativo();
    }
    
    /**
     * OBTENER LISTADO DE SITUACIONES PARA NUEVA ARMA
     * @author Richar Fernández
     * @version 1.0
     * @return Listado de Situaciones para nueva arma (menos Vendida y no es de mi propiedad)
     */
    public List<TipoGamac> obtenerListadoSituacionArma(){
        if(esPerfilInpe()){
            return ejbTipoGamacFacade.obtenerSituacionNuevaArmaInpe();
        }
        return ejbTipoGamacFacade.obtenerSituacionNuevaArma();
    }
    
    /**
     * OBTENER LISTADO DE SITUACIONES PARA ARMAS QUE NO POSEEN
     * @author Richar Fernández
     * @version 1.0
     * @return Listado de Situaciones de armas que no están en su posesión
     */
    public List<TipoGamac> obtenerSituacionArmaNoPosee(){
        
        if(!esPerfilInpe()){
            if(tieneInternamiento){
                if(tieneInternamientoEspecifico()){
                    return ejbTipoGamacFacade.obtenerSituacionArmaInternamiento();
                }else{
                    return ejbTipoGamacFacade.obtenerSituacionArmaInternamientoNoEsPropiedad();
                }                
            }
            return ejbTipoGamacFacade.obtenerSituacionArmaJuridicas();
        }else{
            if(tieneInternamiento){
                return ejbTipoGamacFacade.obtenerSituacionArmaInternamiento();
            }else{
                return ejbTipoGamacFacade.obtenerSituacionArmaInpe();
            }
        }
    }
    
    public boolean tieneInternamientoEspecifico(){
        if(datosActaInt == null){
            return false;
        }
        if(datosActaInt.get("TIPO_DEPOSITO_CODPROG").toString().equals("TP_GTALM_MANVOL") ||
           datosActaInt.get("TIPO_DEPOSITO_CODPROG").toString().equals("TP_GTALM_VENCAN") ||
           datosActaInt.get("TIPO_DEPOSITO_CODPROG").toString().equals("TP_GTALM_SEGUSO")
           ){
            return true;
        }
        return false;
    }
    
    /**
     * OBTENER LISTADO DE SITUACIONES QUE ESTÁN BAJO SU POSESIÓN
     * @author Richar Fernández
     * @version 1.0
     * @return Listado de Situaciones de armas que están en su posesión
     */
    public List<TipoGamac> obtenerSituacionArmaEnPosesion(){
        return ejbTipoGamacFacade.obtenerSituacionArmaEnPosesion();
    }
    
    /**
     * ABRIR FORMULARIO DE IMAGEN DE AYUDA
     * @author Richar Fernández
     * @version 1.0
     * @param fotoSelected Map de foto selccionadas.
     */
    public void openDlgFoto(Map fotoSelected){
        try {
            if(fotoSelected != null){
                fotoByte = (byte[]) fotoSelected.get("byte");
                InputStream is = new ByteArrayInputStream(fotoByte);
                foto =  new DefaultStreamedContent(is, "image/jpeg", ""+fotoSelected.get("nombre") );
            }
            RequestContext.getCurrentInstance().execute("PF('wvDialogFoto').show()");
            RequestContext.getCurrentInstance().update("frmFoto");
        } catch (Exception e) {
            //e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar la foto");
        }
     }
     
    /**
     * OBTENER ARCHIVO PDF/JPEG DE REGULARIZACIÓN DE ARMAS
     * @author Richar Fernández
     * @version 1.0
     * @return Reporte de documento
     */
     public StreamedContent cargarDocumento(){
        String nombre = registro.getAdjuntoDocId().substring(0, registro.getAdjuntoDocId().lastIndexOf(".") );
        String extension = registro.getAdjuntoDocId().substring(registro.getAdjuntoDocId().lastIndexOf("."), registro.getAdjuntoDocId().length());
        String pathDocumento = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_documento").getValor();
        try {            
            String nombreArchivo = nombre + extension.toLowerCase();
                     
            if(fileAdjunto1 != null){
                if(registro.getAdjuntoDocId().toUpperCase().contains("PDF")){
                    return new DefaultStreamedContent(fileAdjunto1.getInputstream(), "application/pdf", nombreArchivo );
                }else{
                    return new DefaultStreamedContent(fileAdjunto1.getInputstream(), "image/jpeg", nombreArchivo );
                }
            }else{
                if(registro.getAdjuntoDocId().toUpperCase().contains("PDF")){
                    return JsfUtil.obtenerArchivoBDIntegrado(pathDocumento, nombreArchivo, nombreArchivo, "application/pdf");
                }else{
                    return JsfUtil.obtenerArchivoBDIntegrado(pathDocumento, nombreArchivo, nombreArchivo, "image/jpeg");    
                }
            }
         } catch (Exception e) {
             //e.printStackTrace();
             
             try {                
                String nombreArchivo = nombre + extension.toUpperCase();
                if(fileAdjunto1 != null){
                    if(registro.getAdjuntoDocId().toUpperCase().contains("PDF")){
                        return new DefaultStreamedContent(fileAdjunto1.getInputstream(), "application/pdf", nombreArchivo );
                    }else{
                        return new DefaultStreamedContent(fileAdjunto1.getInputstream(), "image/jpeg", nombreArchivo );
                    }
                }else{
                    if(registro.getAdjuntoDocId().toUpperCase().contains("PDF")){
                        return JsfUtil.obtenerArchivoBDIntegrado(pathDocumento, nombreArchivo, nombreArchivo, "application/pdf");
                    }else{
                        return JsfUtil.obtenerArchivoBDIntegrado(pathDocumento, nombreArchivo, nombreArchivo, "image/jpeg");    
                    }
                }
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
             
         }     
        return null;
     }
     
     
     /////////// BANDEJA DE OBSERVACIONES ///////////
    
    /**
     * FUNCIÓN PARA DIRECCIONAR A PÁGINA DE OBSERVACIONES
     * @author Richar Fernández
     * @version 1.0
     * @return Página de observaciones a direccionar
     */
    public String prepareBandejaObservaciones(){
        reiniciarCamposBusqueda();        
        resultadosBandeja = null;
        blnSubsanar = false;
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");        
        if(!esPerfilInpe()){
            int contAutorizado = ejbRaessFacade.contarResolucionesVigentesEmpresa(p_user.getNumDoc());
            if(contAutorizado == 0){
                JsfUtil.mensajeError("Usted no cuenta con AUTORIZACIÓN VIGENTE PARA UTILIZAR ARMAS EN SUS ACTIVIDADES. Por Favor Acérquese a las Oficinas de SUCAMEC para regularizar");
                return null;
            }
        }   
        
        estado = EstadoCrud.BUSCAROBS;
        return "/aplicacion/gamac/amaRegEmpArma/ListObservaciones";
    }
    
    /**
     * FUNCIÓN DE BÚSQUEDA DE BANDEJA DE OBSERVACIONES
     * @author Richar Fernández
     * @version 1.0
     */
    public void buscarBandejaObservaciones(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        resultadosBandeja = null;

        HashMap mMap = new HashMap();
        mMap.put("usuario", p_user.getNumDoc());
        mMap.put("filtro", (filtro != null) ? (filtro.isEmpty()?null:filtro.trim().toUpperCase()) : filtro);

        if(esPerfilInpe()){
            resultadosBandeja = ejbAmaRegEmpArmaFacade.buscarObservacionesRegularizacionInpe(mMap);
        }else{
            resultadosBandeja = ejbAmaRegEmpArmaFacade.buscarObservacionesRegularizacionPJ(mMap);
        }
        
        reiniciarCamposBusqueda();
    }
    
    /**
     * FUNCIÓN ABRIR POPUP DE OBSERVACIÓN
     * @author Richar Fernández
     * @version 1.0
     * @param reg Registro Map seleccionado
     */
    public void openDlgObservacion(Map reg){
        tipoRegistroObservacion = 3;
        asunto = ""+(reg.get("ASUNTO_OBS_SIS") == null ?"":reg.get("ASUNTO_OBS_SIS"));
        descripOservacion = ""+(reg.get("DESC_OBS_SIS") == null ?"":reg.get("DESC_OBS_SIS"));
        RequestContext.getCurrentInstance().execute("PF('wvRegistrarObservacion').show()");
        RequestContext.getCurrentInstance().update("observacionForm");
    }
    
    public void verificaNuevoExpediente(AmaRegEmpArma amaRegistro){
        procesaNuevoExpediente = false;
        NeDocumento notifDocumento = ejbNeDocumentoFacade.buscarNeDocumentoRaessByNroExpediente(amaRegistro.getExpediente());
        if(notifDocumento != null){
            int dias = JsfUtil.calculaDiasEntreFechas(notifDocumento.getNeEventoList().get(0).getFecha(), new Date());
            switch(amaRegistro.getTipoEvaluacionId().getCodProg() ){
                case "TP_EVA_S48H":
                        if(dias > 2){
                            JsfUtil.mensajeAdvertencia("Ud. ha excedido el plazo de 48 horas para subsanar este registro. Se creará una nueva solicitud con nuevo expediente");
                            procesaNuevoExpediente = true;
                        }
                        break;
                case "TP_EVA_S10D":
                        if(dias > 10){
                            JsfUtil.mensajeAdvertencia("Ud. ha excedido el plazo de 10 días para subsanar este registro. Se creará una nueva solicitud con nuevo expediente");
                            procesaNuevoExpediente = true;
                        }
                        break;
            }
        }
    }
    
    /**
     * FUNCIÓN PARA EDITAR OBSERVACIÓN SELECCIONADA
     * @author Richar Fernández
     * @version 1.0
     * @param reg Registro Map seleccionado para editar
     */
    public void mostrarEditarObservaciones(Map reg){
          
        int diasHabilesSubsanacion = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_diasHabiles_subsanacion").getValor());
        Date fechaCalculada =  ejbAmaRegEmpArmaFacade.calcularFechaLimiteSubsanacion((Date) reg.get("FECHA_OFICIO"), diasHabilesSubsanacion);
        
        if ( fechaCalculada != null && JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fechaCalculada)) > 0 ) {
            JsfUtil.mensajeAdvertencia("No puede registrar la subsanación debido a que se encuentra fuera del plazo de "+ diasHabilesSubsanacion + " días hábiles.");
            return;
        }
        
        reiniciarValores();
        Long nroLicencia = null;
        
        try {
            if(reg.get("ID") != null){
                registro = ejbAmaRegEmpArmaFacade.obtenerArmaVerificada(Long.parseLong(""+reg.get("ID")));                
                
                if(reg.get("LIC_DISCA_ID") != null) {
                    registroSelected = ejbArmaFacadeFacade.buscarArmaRMA(""+reg.get("LIC_DISCA_ID"));
                }
                observacion = registro.getDescMotivo();
                estadoSituacion = registro.getEstadoArmaId();
                situacionArma = registro.getSituacionArma();
                regionInpe = registro.getRegionId();
                regionInpeNoPropiedad = registro.getRegionId();
                penalInpe = registro.getPenalId();
                penalInpeNoPropiedad = registro.getPenalId();
                nroLicencia = registro.getLicDiscaId();
                
                cargarActaInternamiento(nroLicencia, registro.getSerie(), registro.getModeloId());
                if(datosActaInt != null){
                    opcionPanel1 = "1";
                }
                
                switch(registro.getEstadoVerfica().intValue()){
                    case 1:
                    case 2:
                            // VERIFICADO / NO ME PERTENECE
                            estado = EstadoCrud.EDITARVERIF;
                            if(registro.getSituacionArma().getCodProg().equals("TP_SITU_POS") ){
                                cambiaPanel2(false);
                                if(registro.getModeloId() != null){
                                    selectedArma = ejbAmaModelosFacade.obtenerModelo(registro.getModeloId());
                                    if(selectedArma != null){
                                        selectedArmaString = obtenerArmaEditarVer(selectedArma);
                                    }else{
                                        JsfUtil.mensajeError("El modelo guardado no se encuentra activo");
                                    }
                                }
                                lstFotos = new ArrayList();
                                cargarFotos();
                               
                            }else{
                                cambiaPanel1();
                                cargarArchivo();
                            }                            
                            if(registro.getSeriePropuesta() != null && !registro.getSeriePropuesta().isEmpty()){
                                serieDifiere = true;
                            }
                            cambiaSituacion();
                            numDocPosiblePropietario = registro.getNdocPosibleProp();
                            break;
                    case 3:
                            //NUEVA ARMA
                            estado = EstadoCrud.EDITARARMA;
                            lstFotos = new ArrayList();
                            selectedArma = ejbAmaModelosFacade.obtenerModelo(registro.getModeloId());
                            if(selectedArma != null){
                                selectedArmaString = obtenerArmaEditarVer(selectedArma);
                            }else{
                                JsfUtil.mensajeError("El modelo guardado no se encuentra activo");
                            }
                            if(registro.getEstadoArmaId().getCodProg().equals("TP_ESTA_OPE")){
                                cargarFotos();
                            }
                            numDocPosiblePropietario = registro.getNdocPosibleProp();
                            cargarArchivo();
                            break;
                    case 4:
                            //EDICION DE REGISTRO DE EMITIDOS
                            armaEmitido = null;
                            lstFotos = new ArrayList();
                            cargarFotos();
                            
                            selectedArma = ejbAmaModelosFacade.obtenerModelo(registro.getModeloId());
                            if(selectedArma != null){
                                selectedArmaString = obtenerArmaEditarVer(selectedArma);
                                armaEmitido = obtenerDescripArmaListado(selectedArma);
                            }else{
                                JsfUtil.mensajeError("El modelo guardado no se encuentra activo");
                            }
                            numDocPosiblePropietario = registro.getNdocPosibleProp();
                            estado = EstadoCrud.EDITAREMITIDO;
                            break;
                }
                asignarTipoDocPosiblePropietario();
                asignarDatosPosiblePropietario(); 
                setBlnSubsanar(true);
                setDisabledEdicionObservacion(true);
                JsfUtil.mensajeAdvertencia(JsfUtil.bundleBDIntegrado("amaArma_editar_mensajeConfirmacion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar datos del registro");
        }
    }
    
    /**
     * FUNCION PARA GENERACION DE REPORTE DE RESOLUCION
     * @author Richar Fernández
     * @version 1.0
     * @param pReg Registro Map seleccionado
     * @return Reporte
     */
    public StreamedContent reporte(Map pReg) {
        try {
            AmaRegEmpArma registroTemp = ejbAmaRegEmpArmaFacade.obtenerArmaVerificada(Long.parseLong("" + pReg.get("ID")));
            Map mapNombres = obtenerNombreReporte();
            
            if (JsfUtil.buscarPdfRepositorio(registroTemp.getExpediente(), mapNombres.get("path").toString() )) {
                return JsfUtil.obtenerArchivoBDIntegrado(mapNombres.get("path").toString(), registroTemp.getExpediente() + ".pdf", registroTemp.getExpediente() + ".pdf", "application/pdf");
            }else{
                JsfUtil.mensajeError("No se encontró el Oficio");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: AmaRegEmpArmaController.reporte :", ex);
        }
        return null;
    }    
    
    /**
     * FUNCION PARA OBTENER DATOS DE REPORTE A GENERAR (NOMBRE JASPER, JRXML Y BUNDLE)
     * @author Richar Fernández
     * @version 1.0
     * @return Map con datos del reporte (Nombre jasper, jrxml y nombre del bundle)
     */
    public Map obtenerNombreReporte(){
        Map nmap = new HashMap();
        // OFICIO DE OBSERVACIONES
        nmap.put("nombreJas", "oficioObservaciones.jasper");
        nmap.put("nombreJrxml", "oficioObservaciones.jrxml");
        nmap.put("path", ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_oficioArma").getValor());
        
        return nmap;
    }
     ////////////////////////////////////////////////
    
    
    //// PROGRESS BAR ////
    public Integer getProgress() {
        if(totalProcesados == null){
            totalProcesados = 0;
        }
        if(actualProcesado == null){
            actualProcesado = 0;
        }
        if(progress == null) {
            progress = 0;
        }
        else {
            if(totalProcesados > 0){                
                progress = ((100*actualProcesado)/totalProcesados) - 5;
                if(progress > 100){
                    progress = 100;
                }
                if(progress < 0){
                    progress = 0;
                }
            }   
        }
         
        return progress;
    }
    
    public void clickAcepto(){
        progress = 0;
        totalProcesados = 0;
        actualProcesado = 0;
    }
    
    public void mostrarProgressbar(){
        setRenderProgressBar(true);
        RequestContext.getCurrentInstance().update("pnlProgressBar");
        RequestContext.getCurrentInstance().update("progressBar");
    }
    
    public Integer getProgressCompletarProceso() {
        if(totalProcesados == null){
            totalProcesados = 0;
        }
        if(actualProcesado == null){
            actualProcesado = 0;
        }
        if(progress == null) {
            progress = 0;
        }
        else {
            if(totalProcesados > 0){                
                progress = ((100*actualProcesado)/totalProcesados);
                if(progress > 100){
                    progress = 100;
                }
                if(progress < 0){
                    progress = 0;
                }
            }   
        }
         
        return progress;
    }
    
    public boolean renderBtnCrearVerificacion(Map reg){
        boolean validacion = false;
        
        if(reg.get("TIPO").toString().equals("5")){
            validacion = true;
        }
        if(esPerfilInpe()){
            return validacion && habilitaInpe;
        }else{
            return validacion && habilitaJuridica;
        }
    }
    
    public boolean renderBtnEditarVerificacion(Map reg){
        boolean validacion = false;

        if(reg.get("PROCESO_ID") != null || reg.get("EXPEDIENTE_MASTER") != null){
            return validacion;
        }
        if( (reg.get("NROEXP") != null) && (!reg.get("TIPO").toString().equals("4"))){
            return validacion;
        }
        if( (!reg.get("TIPO").toString().equals("5")) && (renderRegistrar)  ){
            validacion = true;
        }
        if(esPerfilInpe()){
            return validacion && habilitaInpe;
        }else{
            return validacion && habilitaJuridica;
        }
    }
    
    
    ///////////// INPE /////////////
    public boolean esPerfilInpe(){
        return JsfUtil.buscarPerfilUsuario("AMA_INPE");
    }
    
    /**
     * FUNCIÓN PARA ABRIR FORM. DE CREACIÓN DE DECLARACIÓN JURADA
     * @author Richar Fernández
     * @version 1.0
     */
    public void openDlgDeclaracionInpe(){
        regionInpe = null;
        activeTabDeclaracion = 0;
        RequestContext.getCurrentInstance().execute("PF('wvDialogDeclaracionJurada').show()");
        RequestContext.getCurrentInstance().update("frmDeclaracionJurada");
    }
    
    public boolean validacionNullRegistrosInpe(String ruc){        
        boolean validacion = true;
        int cont = ejbAmaRegEmpArmaFacade.contarLicenciaNullRegistrosInpe(ruc);
       
        if(cont > 0){
           validacion = false;
        }
        
        return validacion;
    }
    
    public String registrar(){
        if(esPerfilInpe()){
            return registrarInpe();
        }else{
            return registrarJuridicas();
        }
    }
    
    /**
     * FUNCIÓN PARA REGISTRAR ARMAS YA REGULARIZADAS Y CREAR DECLARACIÓN JURADA PARA EL INPE
     * @author Richar Fernández
     * @version 1.0
     * @return Página a redireccionar
     */
    public String registrarInpe(){
        
        List<AmaRegEmpArma> lstArmasRegistro = new ArrayList();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");            
        SbPersonaGt per = null;
        String asunto = "", expedienteMaster = null;


        try {
            errorArchivo = false;
            boolean validacion = true, tieneExpediente = false;
            List<Map> lstArmasParametroReport = new ArrayList();
            String userLogin = "USRWEB";
            
            Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc(userLogin);
            per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
            Integer nroProcesoMaster = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_Inpe_nroProcesoMaster").getValor());            
            
            Integer nroProcesoIndividualEnPosesInt = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoInpeEnPoseIntern").getValor());
            Integer nroProcesoIndividualPerRob = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoInpePerdRob").getValor());
            Integer nroProcesoIndividualNoEsMiProp = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoNoEsMiProp").getValor());
            Integer nroProcesoIndividualTransfer = Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("amaRegEmpArma_nroProcesoTransferido").getValor());            
            Integer nroProcesoIndividual;

            if(usuaTramDoc != null){
                if(per != null){
                    lstArmasRegistro = ejbAmaRegEmpArmaFacade.obtenerListadoArmasRegularizacionRaess(p_user.getNumDoc(), regionInpe.getId(), true);
                    if(lstArmasRegistro == null || lstArmasRegistro.isEmpty()){
                        JsfUtil.mensajeError("No se encontraron las armas para regularizar");
                        return null;
                    }
                    
                    tieneExpediente = verificaRegistrosConExpediente(lstArmasRegistro);

                    //CREACION DE EXPEDIENTE MAESTRO
                    if(tieneExpediente){
                        expedienteMaster = obtenerExpedienteMaster(lstArmasRegistro);
                    }else{
                        asunto = "Región:" + regionInpe.getNombre() + ". Por evaluar:"+ contPorEmitir +", Emitidos:"+ contEmitidos +", No me pertenecen:"+contNoPertenecen;
                        expedienteMaster = wsTramDocController.crearExpediente_regularizacion(null, per , asunto , nroProcesoMaster, usuaTramDoc, usuaTramDoc,"Regularización de Armas");
                        
                        SbDerivacionCydoc derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionXSistemaXProcesoCodXCydocProceso(JsfUtil.getLoggedUser().getSistema(), "TP_GAMAC_RINP", nroProcesoMaster.longValue());
                        if(derivacion != null){
                            List<Integer> acciones = new ArrayList();
                            acciones.add(17);   // En evaluación
                            //wsTramDocController.asignarExpediente(expedienteMaster, userLogin, derivacion.getUsuarioDestinoId().getLogin(), "Para su evaluación", "", acciones, false, null);
                            String usuarioActual = ejbAmaRegEmpArmaFacade.obtenerUsuarioTrazaActualExpediente(expedienteMaster);
                            wsTramDocController.asignarExpediente(expedienteMaster, usuarioActual, derivacion.getUsuarioDestinoId().getLogin(), "Para su evaluación", "", acciones, false, null);
                        }
                    }                    

                    if(expedienteMaster != null){
                        crearHistoricoProcesoRegArma((short) 1, per, expedienteMaster);

                        //CREACION DE EXPEDIENTE POR REGISTRO
                        AmaModelos modelo;
                        Map armaDisca = null;

                        //// Progress Bar
                        actualProcesado = 0;
                        totalProcesados = lstArmasRegistro.size();
                        /////////

                        for(AmaRegEmpArma arma : lstArmasRegistro){
                            Map map = new HashMap();
                            asunto = "";
                            actualProcesado++;

                            if(arma.getExpedienteMaster() != null){
                                //////////////////////////
                                // YA HA SIDO PROCESADO //
                                //////////////////////////
                                if(arma.getEstadoVerfica() == 4){//Emitidas
                                    armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());
                                    map = preparaArmaReporte(arma,null, armaDisca);
                                    map.put("expediente", null );
                                    lstArmasParametroReport.add(map);
                                }else{
                                    if(arma.getEstadoVerfica() != 3){ // Verificadas y No me pertenecen
                                        armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());                                       
                                    }else{
                                        //Nuevo
                                        armaDisca = null;                                        
                                    }       
                                    if(arma.getModeloId() != null){
                                        modelo =  ejbAmaModelosFacade.obtenerModeloById(arma.getModeloId());
                                        map = preparaArmaReporte(arma,modelo,null);
                                    }else{
                                        map = preparaArmaReporte(arma,null, armaDisca);
                                    }                                    
                                    map.put("expediente", arma.getExpediente() );
                                    lstArmasParametroReport.add(map);
                                }
                                ////////////////////////////
                            }else{
                                //////////////////////////
                                ///// FALTA PROCESAR /////
                                //////////////////////////

                                if(arma.getEstadoVerfica() == 4){//Emitidas
                                        armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());
                                        map = preparaArmaReporte(arma,null, armaDisca);
                                        map.put("expediente", null );
                                        lstArmasParametroReport.add(map);

                                        // Volviendo a verificar arma antes de actualizar
                                        int conExp = ejbAmaRegEmpArmaFacade.actualizaExpedienteMasterArmaNativo(arma.getId(), "Procesando..." );
                                        if(conExp > 0){
                                            arma.setExpedienteMaster(expedienteMaster);
                                            ejbAmaRegEmpArmaFacade.edit(arma); 
                                            procesoHis.getAmaRegEmpArmaList().add(arma);
                                        }
                                }else{
                                    //////////////////////////
                                    ///// FALTA PROCESAR /////
                                    //////////////////////////
                                    arma.setExpedienteMaster(expedienteMaster);
                                    if(arma.getEstadoVerfica() != 3){ // Verificadas y No me pertenecen
                                        armaDisca = ejbArmaFacadeFacade.buscarArmaRMA(""+arma.getLicDiscaId());
                                        asunto = asunto + "DATOS DISCA = Serie: "+ armaDisca.get("NRO_SERIE") +", Tipo Arma:" + armaDisca.get("DES_ARM") +", Marca:"+ armaDisca.get("DES_MARCA")+", Modelo:"+ (armaDisca.get("DES_MODELO")!=null?"-":armaDisca.get("DES_MODELO") ) +", Calibre:"+ armaDisca.get("CALIBRE")+ ", Situación del arma:" + armaDisca.get("ESTADO");
                                        if(arma.getEstadoArmaId() != null){
                                            asunto = asunto + "; DATOS ARMA VERIFICADA = Estado del arma:"+ arma.getEstadoArmaId().getNombre();
                                        }else{
                                            asunto = asunto + "; DATOS ARMA VERIFICADA = Situación del arma: "+ arma.getSituacionArma().getNombre() +", Serie:"+ (arma.getSerie() != null?arma.getSerie().trim():"") ;    
                                        }
                                    }else{ //Nuevo
                                        armaDisca = null;
                                        if(arma.getEstadoArmaId() != null){
                                            asunto = "DATOS ARMA VERIFICADA = Estado del arma: "+ arma.getEstadoArmaId().getNombre() +", Situación del arma: "+ arma.getSituacionArma().getNombre() +", Serie:"+arma.getSerie();
                                        }else{
                                            asunto = "DATOS ARMA VERIFICADA = Situación del arma: "+ arma.getSituacionArma().getNombre() +", Serie:"+ (arma.getSerie() != null?arma.getSerie().trim():"");
                                        }
                                    }                        
                                    if(arma.getModeloId() != null){
                                        modelo =  ejbAmaModelosFacade.obtenerModeloById(arma.getModeloId());
                                        asunto = asunto + ", Tipo Arma:" + modelo.getTipoArmaId().getNombre()+", Marca:"+modelo.getMarcaId().getNombre()+", Modelo:"+((modelo.getModelo() == null)?"":modelo.getModelo())+", Calibre:"+obtenerCalibreArma(modelo.getAmaCatalogoList());                            
                                        if(arma.getPesoArma() != null){
                                            asunto = asunto + ", Peso:" + arma.getPesoArma();
                                        }
                                        map = preparaArmaReporte(arma,modelo,null);
                                    }else{
                                        if(arma.getPesoArma() != null){
                                            asunto = asunto + ", Peso:" + arma.getPesoArma();
                                        }
                                        map = preparaArmaReporte(arma,null, armaDisca);
                                    }

                                    // Volviendo a verificar arma antes de guardar
                                    int conExp = ejbAmaRegEmpArmaFacade.actualizaExpedienteArmaNativo(arma.getId(), "Procesando..." );
                                    if(conExp > 0){
                                         String codProgProc = "";
                                        switch(arma.getSituacionArma().getCodProg()){
                                            case "TP_SITU_POS":
                                            case "TP_SITU_INT":
                                                    nroProcesoIndividual = nroProcesoIndividualEnPosesInt;
                                                    codProgProc = "TP_GAMAC_TINP";
                                                    break;
                                            case "TP_SITU_PER":
                                            case "TP_SITU_ROB":
                                                    nroProcesoIndividual = nroProcesoIndividualPerRob;
                                                    codProgProc = "TP_GAMAC_RPIN";
                                                    break;
                                            case "TP_EST_EMP_NOP":
                                                    nroProcesoIndividual = nroProcesoIndividualNoEsMiProp;
                                                    codProgProc = "TP_GAMAC_NPIN";
                                                    break;
                                            case "TP_SITU_VEN":
                                                    nroProcesoIndividual = nroProcesoIndividualTransfer;
                                                    codProgProc = "TP_GAMAC_NTIN";
                                                    break;
                                            default:
                                                    nroProcesoIndividual = nroProcesoIndividualEnPosesInt;
                                                    codProgProc = "TP_GAMAC_TINP";
                                                    break;
                                        }                                        
                                        
                                        String expedienteId = wsTramDocController.crearExpediente_regularizacion(null, per , asunto , nroProcesoIndividual, usuaTramDoc, usuaTramDoc, obtenerTituloExpPorSituacion(arma));
                                        if(expedienteId != null){
                                            arma.setExpediente(expedienteId);
                                            
                                            String usuarioActual = ejbAmaRegEmpArmaFacade.obtenerUsuarioTrazaActualExpediente(expedienteId);
                                            ///////// Derivación de expediente /////////
                                            List<Integer> acciones = new ArrayList();
                                            acciones.add(17);   // En evaluación
                                            SbDerivacionCydoc derivacion = ejbSbDerivacionCydocFacade.obtenerDerivacionXSistemaXProcesoCodXCydocProceso(JsfUtil.getLoggedUser().getSistema(), codProgProc, nroProcesoIndividual.longValue());
                                             if(derivacion != null){
                                                wsTramDocController.asignarExpediente(arma.getExpediente(), usuarioActual, derivacion.getUsuarioDestinoId().getLogin(), "Para su evaluación", "", acciones, false, null);
                                            }
                                            /////////////////////////////////////////////
                                        }else{
                                            validacion = false;
                                            JsfUtil.mensajeError("Hubo un error al crear expediente");
                                            break;
                                        }
                                        map.put("expediente", arma.getExpediente() );
                                        lstArmasParametroReport.add(map);
                                        ejbAmaRegEmpArmaFacade.edit(arma);
                                        procesoHis.getAmaRegEmpArmaList().add(arma);
                                    }
                                    ////////////////////////////
                                }
                            }
                        }
                    }else{
                        validacion = false;
                        JsfUtil.mensajeError("Hubo un error al crear expediente");
                    }

                    if(validacion){
                        JsfUtil.mensaje("Registro realizado correctamente");
                        setRenderImprimir(true);
                        setRenderRegistrar(false);
                        setBlnGenerarPdf(false);

                        if (!JsfUtil.buscarPdfRepositorio(expedienteMaster, JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificado")) && lstArmasParametroReport != null ) {
                            if(subirPdf(lstArmasRegistro.get(0),lstArmasParametroReport, expedienteMaster)){
                                asunto = "Por evaluar:"+ contPorEmitir +", Emitidos:"+ contEmitidos +", No me pertenecen:"+contNoPertenecen;                                

                                if(wsTramDocController.adjuntarDeclaracionJurada(usuaTramDoc,"USRWEB",lstArmasRegistro.get(0).getExpedienteMaster(),asunto, FileUtils.readFileToByteArray(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificado") + lstArmasRegistro.get(0).getExpedienteMaster() + ".pdf")) )){
                                    RequestContext.getCurrentInstance().execute("PF('wvDialogDeclaracionJurada').hide()");
                                    RequestContext.getCurrentInstance().update("listBand");
                                    crearHistoricoProcesoRegArma((short) 3, per, expedienteMaster);
                                }else{
                                    crearHistoricoProcesoRegArma((short) 2, per, expedienteMaster);
                                }
                            }else{
                                crearHistoricoProcesoRegArma((short) 2, per, expedienteMaster);
                                setRenderImprimir(false);
                                setRenderRegistrar(false);
                                setBlnGenerarPdf(true);
                                setErrorArchivo(true);
                                JsfUtil.mensajeError("Hubo un error al generar documento de verificación");
                                RequestContext.getCurrentInstance().execute("PF('wvDialogDeclaracionJurada').hide()");
                                RequestContext.getCurrentInstance().update("listBand");
                            }
                        }else{
                            RequestContext.getCurrentInstance().execute("PF('wvDialogDeclaracionJurada').hide()");
                            RequestContext.getCurrentInstance().update("listBand");
                        }
                        return prepareListRegularizacionArma();
                    }else{
                        crearHistoricoProcesoRegArma((short) 2, per, (expedienteMaster != null?expedienteMaster:"-"));
                    }
                }else{
                    JsfUtil.mensajeError("No se encontraron datos de la persona: "+p_user.getNumDoc());
                }
            }else{
                JsfUtil.mensajeError("No se encontró usuario USRWEB");
            }
        } catch (Exception e) {
            e.printStackTrace();
            crearHistoricoProcesoRegArma((short) 2, per, (expedienteMaster != null?expedienteMaster:"-"));
            JsfUtil.mensajeError("No se pudo completar el proceso");
        }

        return null;
    }
    
    public String obtenerEstadoExpediente(Map item){
        String estadoExp = "";
        
        if(item.get("ESTADO").toString().equals("3")){
            estadoExp = "FINALIZADO";
        }else{
            estadoExp = "PENDIENTE GENERER PDF";
        }
        
        return estadoExp;
    }

    public void openDlgImprimir(){
        RequestContext.getCurrentInstance().execute("PF('wvImpresion').show()");
        RequestContext.getCurrentInstance().update("imprimirForm");
    }
    
    public void openDlgImprimirInpe(){
        RequestContext.getCurrentInstance().execute("PF('impresionInpe').show()");
        RequestContext.getCurrentInstance().update("msjeForm");
    }
    
    /**
     * FUNCION PARA GENERAR CONSTANCIA DE REGULARIZACIÓN DE ARMAS
     * @author Richar Fernández
     * @version 1.0
     * @param regImp Registro a validar
     * @return Reporte pdf
     */
    public StreamedContent imprimirConstancia(Map regImp){
        try {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");            
            AmaRegEmpArma reg = null;

            
            reg = ejbAmaRegEmpArmaFacade.obtenerAmaRegByExpedienteMaster(regImp.get("EXPEDIENTE").toString(),p_user.getNumDoc(), esPerfilInpe());
            if(reg != null){
                return constancia(reg, regImp);
            }
        } catch (Exception e) {
            e.printStackTrace();            
            JsfUtil.mensajeError("Hubo un error durante el proceso.");
        }
        return null;
    }
    
    public void actualizaListadoImpresion(Map regAct){
        /////
        if(regAct != null){
            for(Map act : lstExpedientesImpresion){
                if(regAct.get("EXPEDIENTE").toString().equals(act.get("EXPEDIENTE"))){
                    act.put("ESTADO", "3");
                }
            }
        }
        RequestContext.getCurrentInstance().update("msjeForm");
        ////
    }
    
    public void completarProcesoInpe(Map regImp){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        AmaRegEmpArma reg = null;        
        
        try {
            regionInpe = ejbTipoBaseFacade.find(Long.parseLong(regImp.get("REGION_ID").toString()));
            
            List<AmaRegEmpArma> lstArmasRegistro = ejbAmaRegEmpArmaFacade.obtenerListadoArmasRegCompletarProceso(regImp.get("EXPEDIENTE").toString(),p_user.getNumDoc(), true);
            List<Map> lstArmasParametroReport = null;
            
            if(!lstArmasRegistro.isEmpty()){
                reg = lstArmasRegistro.get(0);
               
                if( reg != null){
                    lstArmasParametroReport = generarListadoReporte(lstArmasRegistro);
                    contEmitidos = ejbAmaRegEmpArmaFacade.contarRegistrosEmitidasRaess(p_user.getNumDoc(), reg.getExpedienteMaster(), regionInpe.getId(), true);
                    contNoPertenecen = ejbAmaRegEmpArmaFacade.contarRegistrosNoMePertenecenInpe(p_user.getNumDoc(), reg.getExpedienteMaster(), regionInpe.getId());
                    contPorEmitir = ejbAmaRegEmpArmaFacade.contarRegistrosXEmitirInpe(p_user.getNumDoc(), reg.getExpedienteMaster(), regionInpe.getId());
                    contParrafo1 = ejbAmaRegEmpArmaFacade.contarArmasPosesionInpe(p_user.getNumDoc(), reg.getExpedienteMaster(), regionInpe.getId()) + contEmitidos;
                    contParrafo2 = ejbAmaRegEmpArmaFacade.contarArmasMenosLasVendidasInpe(p_user.getNumDoc(), reg.getExpedienteMaster(), regionInpe.getId());
                    contParrafo3 = contNoPertenecen;
                    
                    if(subirPdf(reg,lstArmasParametroReport, reg.getExpedienteMaster())){
                        SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
                        Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
                        asunto = "Por evaluar:"+ contPorEmitir +", Emitidos:"+ contEmitidos +", No me pertenecen:"+contNoPertenecen;
                        
                        if(!reg.getAmaRegEmpProcesoList().isEmpty()){
                            procesoHis = reg.getAmaRegEmpProcesoList().get(0);  
                            if(procesoHis.getAmaRegEmpArmaList() == null){
                                procesoHis.setAmaRegEmpArmaList(new ArrayList());
                            }
                        }                        
                        for(AmaRegEmpArma arma : lstArmasRegistro){
                            if(!procesoHis.getAmaRegEmpArmaList().contains(arma)){
                                procesoHis.getAmaRegEmpArmaList().add(arma);
                            }
                        }
                        crearHistoricoProcesoRegArma((short) 3, per, reg.getExpedienteMaster());
                        
                        if(wsTramDocController.adjuntarDeclaracionJurada(usuaTramDoc,"USRWEB",reg.getExpedienteMaster(),asunto, FileUtils.readFileToByteArray(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificado") + reg.getExpedienteMaster() + ".pdf")) )){
                            
                        }
                        lstExpedientesImpresion = ejbAmaRegEmpArmaFacade.obtenerExpedienteMasterInpe(p_user.getNumDoc());
                        JsfUtil.mensaje("Proceso completado correctamente");
                    }
                }
            }            
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error durante el proceso");
        }
    }
    
    public List<TipoBaseGt> listarRegionesInpe(){
        return ejbTipoBaseFacade.lstTipoBase("TP_RGN");
    }
    
    public List<TipoBaseGt> listarPenalesInpe(){
        if(opcionPanel1 != null){
            if(regionInpeNoPropiedad != null){
                return ejbTipoBaseFacade.lstTipoBase(regionInpeNoPropiedad.getCodProg());
            }
        }else{
            if(opcionPanel2 != null && regionInpe != null){
                return ejbTipoBaseFacade.lstTipoBase(regionInpe.getCodProg());        
            }
        }        
        return null;
    }
    
    public List<TipoBaseGt> listarPenalesInpeNuevaArma(){
        if(registro.getRegionId() != null){
            return ejbTipoBaseFacade.lstTipoBase(registro.getRegionId().getCodProg());        
        }
        return null;
    }
    
    public void cambiarRegionInpe(){
        penalInpe = null;
        penalInpeNoPropiedad = null;
        registro.setPenalId(null);
    }
    
    public boolean renderBtnSubsanar(Map reg){
        boolean validacion = false;

        if(esPerfilInpe()){
            if(reg.get("ESTADO_CODPROG") != null && reg.get("ESTADO_CODPROG").toString().equals("TP_EVA_SUB")){
                return false;
            }
            return true;
        }
        
        if( reg != null && 
           (reg.get("EVALUACION_CODPROG").toString().equals("TP_EVA_SIS") || reg.get("EVALUACION_CODPROG").toString().equals("TP_EVA_S48H") || reg.get("EVALUACION_CODPROG").toString().equals("TP_EVA_S10D")) &&
           (!reg.get("ESTADO_CODPROG").toString().equals("TP_EVA_SUB"))     
          ){
                int dias = JsfUtil.calculaDiasEntreFechas((Date) reg.get("FECHA_OBS_SIS"), new Date());
                switch(reg.get("EVALUACION_CODPROG").toString()){
                    case "TP_EVA_S48H":
                            if(dias > 2){
                                validacion = false;
                            }else{
                                validacion = true;
                            }
                            break;
                    case "TP_EVA_S10D":
                            if(dias > 10){
                                validacion = false;
                            }else{
                                validacion = true;
                            }
                            break;
                }
        }
        return validacion;
    }
    
    public boolean renderTextFueraPlazoRaess(Map reg){
        boolean validacion = false;

        if(esPerfilInpe()){
            return false;
        }
        
        if( reg != null && 
           (reg.get("EVALUACION_CODPROG").toString().equals("TP_EVA_SIS") || reg.get("EVALUACION_CODPROG").toString().equals("TP_EVA_S48H") || reg.get("EVALUACION_CODPROG").toString().equals("TP_EVA_S10D")) &&
           (!reg.get("ESTADO_CODPROG").toString().equals("TP_EVA_SUB"))     
          ){
            if(!procesoRaessPlazoPermitido()){
                int dias = JsfUtil.calculaDiasEntreFechas((Date) reg.get("FECHA_OBS_SIS"), new Date());
                switch(reg.get("EVALUACION_CODPROG").toString()){
                    case "TP_EVA_S48H":
                            if(dias > 2){
                                validacion = true;
                            }
                            break;
                    case "TP_EVA_S10D":
                            if(dias > 10){
                                validacion = true;
                            }
                            break;
                }
            }
        }
        return validacion;
    }
    
    public void siguienteTabDeclaracionInpe(){        
        
        if(regionInpe == null){
            JsfUtil.mensajeError("Por favor seleccione una región.");
            return;
        }
        
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        contEmitidos = ejbAmaRegEmpArmaFacade.contarRegistrosEmitidasRaess(p_user.getNumDoc(), null, regionInpe.getId(), true);
        progress = 0;
        
        if(ejbUsuarioFacade.buscarUsuarioByLogin(p_user.getLogin()) != null){
            if(validacionNullRegistrosInpe(p_user.getNumDoc())){
                contNoPertenecen = ejbAmaRegEmpArmaFacade.contarRegistrosNoMePertenecenInpe(p_user.getNumDoc(), null, regionInpe.getId());
                contPorEmitir = ejbAmaRegEmpArmaFacade.contarRegistrosXEmitirInpe(p_user.getNumDoc(), null, regionInpe.getId());
                contParrafo1 = ejbAmaRegEmpArmaFacade.contarArmasPosesionInpe(p_user.getNumDoc(), null, regionInpe.getId()) + contEmitidos;
                contParrafo2 = ejbAmaRegEmpArmaFacade.contarArmasMenosLasVendidasInpe(p_user.getNumDoc(), null, regionInpe.getId());
                contParrafo3 = contNoPertenecen;

                if(contParrafo1 > 0 || contParrafo2 > 0 || contParrafo3 > 0 ){
                    activeTabDeclaracion = 1;
                    setAcepto(false);
                    setRenderProgressBar(false);                    
                }else{
                    JsfUtil.mensajeError("No hay armas registradas en la región seleccionada");
                }
            }else{
                JsfUtil.mensajeError("No pueden existir armas en estado VERIFICADO que no cuenten con licencia.");
            }
        }else{
            JsfUtil.mensajeError("No se encontró al usuario: "+ p_user.getLogin());
        }
    }
    
    public void regresarTabDeclaracionInpe(){
        activeTabDeclaracion = 0;
    }    
    
    
    
    public String obtenerArrayConcatenadosTipadosGT(){
        String tipados = "";
        List<TipoGamac> listadoTipado = ejbTipoGamacFacade.listarTipadoRecursivoById(195L); // Tipado GT
        if(listadoTipado != null && !listadoTipado.isEmpty()){
            for(TipoGamac tipado : listadoTipado){
                if(tipados.isEmpty()){
                    tipados = "" + tipado.getId();
                }else{
                    tipados += ", " + tipado.getId();
                }
            }
        }
        return tipados;
    }
    
    public String obtenerAlmacenActaInternamiento(){
        String almacen = "";
        if(datosActaInt != null){
            almacen = datosActaInt.get("DIRECCION").toString();
            if(datosActaInt.get("DISTRITO_ID") != null && Integer.parseInt(datosActaInt.get("DISTRITO_ID").toString()) > 0 ){
                almacen += ( " - " +datosActaInt.get("UBIGEO").toString());
            }
        }
        return almacen;
    }
    
    public boolean disabledRadioSiPoseeArma(){
        if(tieneInternamiento){
            return true;
        }
        return false;
    }
    
    public boolean renderArchivoAdjuntoNoPosee(){
        if(registro == null){
            return false;
        }
        if(registro.getSituacionArma() != null && registro.getSituacionArma().getCodProg().equals("TP_SITU_INT")){
            return false;
        }

        return true;
    }
    
    public boolean renderMensajObsNoTitularidad(){
        if(esPerfilInpe()){
            return false;
        }
        if(registro == null){
            return false;
        }
        if(registro.getSituacionArma() != null && registro.getSituacionArma().getCodProg().equals("TP_SITU_TIT")){
            return true;
        }
        return false;
    }
    
    public void cambiaTipoDocPosiblePropietario(){
        numDocPosiblePropietario = "";
        datosPosiblePropietario = "";
        if(tipoDocPosiblePropietario == null){
            return;
        }
        switch(tipoDocPosiblePropietario){
            case "DNI": maxLengthPosiblePropietario = 8; break;
            case "CE": maxLengthPosiblePropietario = 9; break;
            case "RUC": maxLengthPosiblePropietario = 11; break;
        }
    }
    
    public void buscarReniec() {
        try {
            if(numDocPosiblePropietario == null){
                JsfUtil.mensajeError("Por favor ingrese el DNI");
                return;
            }
            if(numDocPosiblePropietario.trim().length() != 8){
                JsfUtil.mensajeError("El DNI debe tener 8 dígitos");
                return;
            }
            numDocPosiblePropietario = numDocPosiblePropietario.trim();
            datosPosiblePropietario = "";
            Consulta_Service serR = new Consulta_Service();
            Consulta port = serR.getConsultaPort();
            wspide.Persona p = null;
            p = port.consultaDNI(numDocPosiblePropietario);

            if (p != null) {
                datosPosiblePropietario = p.getAPPAT() + " " + ((p.getAPMAT() != null)?p.getAPMAT():"") + " " + p.getNOMBRES();
                JsfUtil.mensaje("Se encontró datos de la persona en el RENIEC");             
            } else {
                JsfUtil.mensajeAdvertencia("No se encontraron datos del DNI ingresado en el RENIEC");
                return;
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeAdvertencia("Hubo un error al consultar los datos en el RENIEC");
        }
    }
    
    public void buscarMigraciones() {
        try {
            if(numDocPosiblePropietario == null){
                JsfUtil.mensajeError("Por favor ingrese el CE");
                return;
            }
            if(numDocPosiblePropietario.trim().length() != 9){
                JsfUtil.mensajeError("El CE debe tener 9 dígitos");
                return;
            }
            numDocPosiblePropietario = numDocPosiblePropietario.trim();
            datosPosiblePropietario = "";
            
            ResPideMigra resMigra = wsPideController.buscarCarnetExtranjeria( ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_usuario_Pide").getValor() , numDocPosiblePropietario);
            if(resMigra != null){
                if(resMigra.isRspta()){
                    if(resMigra.getResultado().getStrNumRespuesta().equals("0000")){
                        datosPosiblePropietario = resMigra.getResultado().getStrPrimerApellido() + " " + ((resMigra.getResultado().getStrSegundoApellido() != null)?resMigra.getResultado().getStrSegundoApellido():"") + " " + resMigra.getResultado().getStrNombres();
                        JsfUtil.mensaje("Se encontró datos de la persona en Migraciones");
                    }else{
                        JsfUtil.mensajeAdvertencia("No se encontró datos de la persona en Migraciones");
                    }
                }else{
                    JsfUtil.mensajeAdvertencia("Hubo un error consultando el servicio de Migraciones.");
                }
            }else{
                JsfUtil.mensajeAdvertencia("El servicio de Migraciones no está disponible.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeAdvertencia("Hubo un error al consultar los datos en Migraciones");
        }
    }
    
    public void buscarSunat(){
        try {
            if(numDocPosiblePropietario == null){
                JsfUtil.mensajeError("Por favor ingrese el RUC");
                return;
            }
            if(numDocPosiblePropietario.trim().length() != 11){
                JsfUtil.mensajeError("El RUC debe tener 11 dígitos");
                return;
            }
            numDocPosiblePropietario = numDocPosiblePropietario.trim();
            datosPosiblePropietario = "";
            
            SbPersonaGt pTemp = new SbPersonaGt();
            pTemp.setRuc(numDocPosiblePropietario);
            pTemp = wsPideController.buscarSunat(pTemp);
            if(pTemp != null){
                if(pTemp.getRznSocial() != null){
                    datosPosiblePropietario = pTemp.getRznSocial();
                    JsfUtil.mensaje("Se encontró datos de la empresa en la SUNAT");
                }else{
                    JsfUtil.mensajeAdvertencia("No se encontró datos de la empresa en la SUNAT");
                }
            }else{
                JsfUtil.mensajeAdvertencia("El servicio de la SUNAT no está disponible");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeAdvertencia("Hubo un error al consultar los datos en SUNAT");
        }
    }
    
    public boolean renderBtnNuevaArma(){
        if(esPerfilInpe()){
            return habilitaInpe;
        }else{
            return habilitaJuridica;
        }
    }
    
    public boolean renderBtnRegistrarInpe(){
        return renderRegistrar && habilitaInpe;
    }
    public boolean renderBtnRegistrarJuridica(){
        return renderRegistrar && habilitaJuridica;
    }
    
    public String obtenerDescripcionDireccion(SbDireccionGt direccionSelected){
        if(direccionSelected == null){
            return "";
        }
        return ((direccionSelected.getViaId().getNombre() != null)?direccionSelected.getViaId().getNombre():"") + " " + 
               direccionSelected.getDireccion() + " " + ((direccionSelected.getNumero() != null)?direccionSelected.getNumero():"") + " - " +
               direccionSelected.getDistritoId().getProvinciaId().getDepartamentoId().getNombre() + " / " + 
               direccionSelected.getDistritoId().getProvinciaId().getNombre() + " / " +
               direccionSelected.getDistritoId().getNombre();        
    }
    
    public String obtenerDireccionRegionInpe(TipoBaseGt regionSelected){
        if(regionSelected == null){
            return null;
        }
        if(regionSelected.getSbDireccionInpeList() == null){
            return null;
        }
        for (SbDireccionGt dire : regionSelected.getSbDireccionInpeList()) {
            if (dire.getActivo() == 1 && dire.getDistritoId() != null && dire.getDistritoId().getId() != 0) { //distrito = 0 = MIGRACION
                return obtenerDescripcionDireccion(dire);
            }
        }
        return null;
    }
    
    public Integer convertirTipoArmaIntegradoADisca(String tipoArmaCodProg){
        switch(tipoArmaCodProg){
            case "TP_ARMCAR":   //CARABINA
                    return 1;
            case "TP_ARMESC":   //ESCOPETA
                    return 2;
            case "TP_ARMPIS":   //PISTOLA
                    return 3;
            case "TP_ARMREV":   //REVOLVER
                    return 4;
            case "TP_ARMCAE":   //CARABINA/ESCOPETA
                    return 6;
            case "TP_ARM_PCAR": //PISTOLA/CARABINA
                    return 5;            
        }
        return null;
    }
    
    public void actualizarSeriePropuesta(){
        registro.setSeriePropuesta(null);
    }
    
    public String obtenerTituloExpPorSituacion(AmaRegEmpArma arma){
        String titulo = "Regularización de armas";
        switch(arma.getSituacionArma().getCodProg()){
            case "TP_SITU_VEN":
                    titulo = "POSIBLE PROPIETARIO: " + arma.getNdocPosibleProp();
                    break;
            default:
                    break;
        }
        return titulo;
    }
}
